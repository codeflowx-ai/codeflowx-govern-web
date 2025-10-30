## Integración RAG con leka-server (Python)

Objetivo: desacoplar la lógica RAG (ingesta, embeddings, búsqueda, métricas) hacia un microservicio Python (leka-server), manteniendo los ViewModels ZK en Java como orquestadores. Este documento detalla, por ViewModel, qué llamadas sustituir por endpoints HTTP/gRPC y qué payloads/estados manejar.

### Principios
- Todas las lecturas complejas (views/métricas) salen a endpoints de lectura del servicio RAG.
- Operaciones (ingesta, rebuild, rollback, versionado) invocan endpoints de acción idempotentes y asíncronos cuando aplique.
- Autenticación: JWT Bearer propagado desde portal. Añadir `X-Request-Id` para trazabilidad.
- Timeouts, retries (exponencial backoff) y circuit breaker en cliente Java.

### Arquitectura de separación (Gestión vs Ejecución)

- Gestión (Java + PostgreSQL)
  - Qué: alta/edición de sistemas RAG, fuentes, versiones; permisos (ACL/policies), vinculación a proyectos/tenants, tokens, cuotas/consumo, auditoría, reporting.
  - Dónde: suinsit.nova.web (JPA + vistas SQL). UI ZK + APIs internas. Persistencia de metadatos y estado funcional.
  - Entidades sugeridas: RagSystem, RagDataSource, RagVersion, RagPolicy (ACL), RagProjectLink, RagApiToken, RagConsumption, RagAuditLog.

- Ejecución RAG (leka-server, Python)
  - Qué: chunking, embeddings, índices/colecciones Qdrant, búsqueda/retrieval, reindex, limpieza de vectores, métricas técnicas.
  - Dónde: microservicio Python con Qdrant/Redis/S3 según aplique.
  - Persistencia: Qdrant (vectores+payload); opcional blobs en S3/FS.

- Contrato
  - Java decide “qué/para quién”; leka ejecuta el “cómo”.
  - Estados sincronizados: Java guarda flags/fechas (lastIndexAt, indexStatus, docCount), leka expone métricas y progresos.
  - Seguridad: JWT con scopes rag:*; filtros por tenant/proyecto trasladados a payload filters en Qdrant.

### Soporte multi-backend vectorial (Qdrant, Cohere, Pinecone, ...)

El diseño de leka-server debe contemplar múltiples proveedores de base vectorial con una capa de abstracción única:

- Configuración por sistema/tenant
  - `vector_backend`: qdrant | cohere | pinecone | <otro>
  - `connection`: parámetros del proveedor (host/apiKey/collection/index/region)
  - `embedding_model`: proveedor del embedding (OpenAI/Azure/Local/Ollama)

- Contrato uniforme (endpoints no cambian)
  - Ingesta/Chunking/Upsert → se enruta al backend seleccionado
  - Búsqueda (query) → responde con `{id, text, score, metadata}` independientemente del proveedor
  - Métricas → normalizadas (latencia, rps, costo estimado, tamaño índice)

- Backends
  - Qdrant: colecciones por systemId/tenant; payload filters
  - Cohere (Embeddings + Rerank + Vector DB): usar SDK/HTTP; mapear nombres de índices
  - Pinecone: índices/namespace por tenant; filtros en metadata

- Gobernanza/Seguridad
  - Secretos por tenant en vault (apiKey, environment)
  - Scopes por acción (rag:search, rag:ingest) y límites de cuota por backend

- Consideraciones de portabilidad
  - Campos comunes en payload (`systemId`, `datasourceId`, `version`, `metadata`)
  - Versionado de embeddings (`embeddings_version`) para reindex incremental
  - Herramientas de migración (dump de vectores + payload) entre backends

### Endpoints base propuestos (leka-server)
- GET /rag/systems?search=&status=&type=&approval=&page=&size=
- GET /rag/systems/{id}
- POST /rag/systems            (crear)
- PUT  /rag/systems/{id}       (actualizar)
- DELETE /rag/systems/{id}
- GET /rag/metrics/summary     (métricas globales)
- GET /rag/systems/{id}/datasources
- GET /rag/systems/{id}/versions
- POST /rag/systems/{id}/ingest           (lanza ingesta/indexación)
- POST /rag/systems/{id}/rebuild-index    (reindex)
- POST /rag/systems/{id}/versions         (crea versión)
- POST /rag/systems/{id}/rollback         (rollback de versión)
- GET /rag/analytics/search               (analíticas de búsqueda)
- GET /rag/quality/retrieval              (métricas de retrieval)
- GET /rag/monitoring/health              (index health)
- GET /rag/monitoring/metrics-summary     (resumen métricas)
- GET /rag/monitoring/usage-by-agent      (uso por agente)
- GET /rag/datasources/embedding-progress (progreso embeddings)
- GET /rag/datasources/coverage-analysis  (cobertura documentos)
- GET /rag/datasources/statistics         (estadísticas)
- GET /rag/quality/chunk-distribution     (distribución chunks)
- GET /rag/governance/compliance          (compliance)
- GET /rag/governance/bias-detection      (sesgos)
- GET /rag/governance/access-control      (ACL)

Nota: los GET admiten filtros por query params; todos devuelven JSON paginado `{content, total, page, size}` cuando aplique.

---

### Integración por ViewModel (qué cambiar)

#### 1) RagSystemsOverviewViewModel (govern/.../rag)
- loadData(): reemplazar `businessService.findAllView(RagOverview...)` por GET /rag/systems con mapeo de filtros `searchTerm, selectedStatus, selectedType, selectedApprovalStatus, pageParams`.
- loadMetrics(): reemplazar `findAllView(RagMetricsSummary...)` por GET /rag/metrics/summary.
- Navegación sin cambios.

#### 2) RagSystemsDetailViewModel (govern/.../rag)
- loadRagSystem(id): GET /rag/systems/{id}.
- loadRagDataSources(): GET /rag/systems/{id}/datasources.
- loadRagVersions(): GET /rag/systems/{id}/versions.
- saveRagSystem(): POST /rag/systems (crear) o PUT /rag/systems/{id} (actualizar); reflejar IDs devueltos.
- indexDataSources(): reemplazar `callProcedure(IngestDocuments)` por POST /rag/systems/{id}/ingest. Mostrar mensaje con `taskId` o `documentsIngested` si disponible. Refrescar datos con GETs.
- createNewVersion(): sustituir TODO por POST /rag/systems/{id}/versions; refrescar versiones/estadísticas.
- Opcional: POST /rag/systems/{id}/rebuild-index, POST /rag/systems/{id}/rollback si se añaden botones.

#### 3) RagSystemOverviewViewModel (platform/.../rag)
- Listado de sistemas: `businessService.findAllEntity` -> GET /rag/systems (mismos filtros de overview).
- Acciones CRUD: POST/PUT/DELETE sobre /rag/systems.

#### 4) RagSystemDetailViewModel (platform/.../rag)
- Carga/guardado del sistema: GET/POST/PUT /rag/systems.
- Secciones descendentes (datasources/versions): GET /rag/systems/{id}/datasources, GET /rag/systems/{id}/versions.
- Botón “Indexar fuentes”: POST /rag/systems/{id}/ingest; feedback asíncrono.

#### 5) RagDataSourceOverviewViewModel / RagDataSourceDetailViewModel / RagDataSourceViewModel
- Listado y detalle: GET /rag/systems/{id}/datasources o GET /rag/datasources?systemId=.
- Crear/actualizar/eliminar datasource: POST/PUT/DELETE /rag/datasources.
- Ingesta puntual por datasource (si aplica): POST /rag/datasources/{id}/ingest.

#### 6) RagVersionOverviewViewModel / RagVersionDetailViewModel
- Listado versiones: GET /rag/systems/{id}/versions.
- Crear versión: POST /rag/systems/{id}/versions (payload con `changes, config, embeddingsVersion`).
- Rollback versión: POST /rag/systems/{id}/rollback (sourceVersionId, targetVersionId opcional).

#### 7) RagRegistryViewModel
- Sustituir `businessService.findAllEntity` y `save/remove` por API de systems/datasources según el caso.

#### 8) RagRollbackViewModel
- `findEntity(RagSystem...)` y búsqueda de versiones -> GET /rag/systems, /versions.
- Crear record de rollback (si se persiste en BD propia) o invocar POST /rag/systems/{id}/rollback y registrar auditoría local.

#### 9) Métricas y analíticas (Overview)
- SearchAnalyticsOverviewViewModel: `findAllView` -> GET /rag/analytics/search.
- RetrievalQualityMetricsOverviewViewModel: -> GET /rag/quality/retrieval.
- RagMetricsSummaryOverviewViewModel: -> GET /rag/monitoring/metrics-summary.
- RagUsageByAgentOverviewViewModel: -> GET /rag/monitoring/usage-by-agent.
- IndexHealthDashboardOverviewViewModel: -> GET /rag/monitoring/health.

#### 10) Progreso/estadísticas de datos
- EmbeddingGenerationProgressOverviewViewModel: -> GET /rag/datasources/embedding-progress (filtros: systemId, datasourceId, dateRange).
- DocumentCoverageAnalysisOverviewViewModel: -> GET /rag/datasources/coverage-analysis.
- DatasourceStatisticsOverviewViewModel: -> GET /rag/datasources/statistics.
- ChunkDistributionStatsOverviewViewModel: -> GET /rag/quality/chunk-distribution.

#### 11) Gobierno/seguridad
- RagComplianceViewModel: -> GET /rag/governance/compliance (filtros por sistema/versión/periodo). Export CSV vía endpoint dedicado si aplica.
- RagBiasDetectionViewModel: -> GET /rag/governance/bias-detection (métricas de sesgo por dataset/modelo).
- RagAccessControlViewModel: -> GET/PUT /rag/governance/access-control (roles, políticas de acceso a colecciones/Qdrant payload_filters).

---

### Mapa Gestión vs Ejecución por ViewModel (resumen)

- Gestión (DB Java):
  - RagSystemsOverview/Detail (atributos de sistema, approval, relación con proyectos, tokens)
  - RagDataSource* (metadatos de fuentes, conexión referenciada, estados de sincronización)
  - RagVersion* (metadatos de versionado)
  - RagAccessControl (políticas ACL), RagCompliance, RagBiasDetection (config y decisiones de gobierno)
  - Audit/Consumption (RagAuditLog, RagConsumption)

- Ejecución (leka-server):
  - indexDataSources, rebuildIndex, rollbackVersion, createVersion (materialización en índices)
  - Panels: metrics-summary, usage-by-agent, retrieval-quality, health, coverage, embedding-progress, statistics, chunk-distribution, search-analytics
  - Search/QA endpoints

### Checklist de implementación

1) Crear RagApiClient (WebClient/Feign) con timeouts, retries, JWT propagation.
2) Sustituir en ViewModels:
   - findAllView/findAllEntity/findById/save/update/removeFromID → llamadas Gestión (DB) o Ejecución (API) según tabla anterior.
   - callProcedure(IngestDocuments) → POST /rag/systems/{id}/ingest (guardar taskId si aplica).
3) Añadir DTOs y mapeos para responses de leka-server y paginación.
4) Persistir consumo/costes en RagConsumption, y auditoría en RagAuditLog tras cada comando exitoso.
5) Testing end-to-end con un sistema RAG de demo y Qdrant en staging.

### Cambios técnicos en los ViewModels (transversales)
- Sustituir `BusinessService.findAllEntity/findAllView/findById/callProcedure/save/update/removeFromID` por un `RagApiClient` (WebClient/Feign) con:
  - métodos Java tipados por endpoint (p.ej., `getSystems`, `getMetricsSummary`, `ingestSystem`, etc.).
  - manejo de paginación, filtros y DTOs equivalentes a las vistas actuales.
- Añadir headers: `Authorization: Bearer <jwt>` y `X-Request-Id`.
- Timeouts (connect 1-2s, read 5-15s según endpoint), retries para GET idempotentes.
- Log y auditoría: mantener `Ssoractividad` al finalizar cada comando exitoso con el resumen de la acción y `taskId` cuando exista.

### DTOs sugeridos (resumen)
- SystemDto: { id, name, type, status, version, approvalStatus, createdAt, updatedAt, documents, datasources }
- DatasourceDto: { id, systemId, name, type, status, documentCount, lastSyncAt, indexStatus }
- VersionDto: { id, systemId, version, createdAt, changes, embeddingsVersion }
- MetricsSummaryDto: { totalRagSystems, activeRagSystems, draftRagSystems, pendingApproval, approvedRagSystems, totalDatasourcesAll, totalDocumentsIndexed }
- RetrievalQualityDto, SearchAnalyticsDto, UsageByAgentDto, ComplianceDto, BiasDto: según vistas actuales.

### Estados y UX
- Ingest/rebuild/rollback: respuestas 202 con `taskId`; mostrar toast “tarea iniciada” y polling opcional.
- Filtros y paginación: mantener `PageParams` pero mapeando a query params del API.
- Errores: mensajes de negocio del leka-server se muestran tal cual; reconexión automática en 5s opcional para paneles métricos.

### Checklist por archivo (extracto)
- govern/viewmodel/rag/RagSystemsOverviewViewModel.java: loadData, loadMetrics → API; filtros → query params.
- govern/viewmodel/rag/RagSystemsDetailViewModel.java: CRUD sistema, datasources, versions, indexDataSources → API.
- platform/viewmodel/rag/*OverviewViewModel.java: todos los `findAll(View/Entity)` → GET endpoints arriba.
- platform/viewmodel/rag/*DetailViewModel.java: CRUD → POST/PUT/DELETE; navegación intacta.
- platform/viewmodel/rag/RagRollbackViewModel.java: rollback → POST /rag/systems/{id}/rollback.
- platform/viewmodel/rag/RagAccessControlViewModel.java: ACL → GET/PUT /rag/governance/access-control.
- platform/viewmodel/rag/RagComplianceViewModel.java: cumplimiento → GET /rag/governance/compliance.
- platform/viewmodel/rag/RagBiasDetectionViewModel.java: sesgos → GET /rag/governance/bias-detection.

### Siguientes pasos
1) Definir OpenAPI en leka-server con estos endpoints y DTOs.
2) Generar cliente Java (OpenAPI Generator) y publicarlo (Maven interno).
3) Sustituir llamadas por `RagApiClient` en ViewModels listados (otro chat).
4) Pruebas end-to-end con Qdrant en staging (colecciones por sistema/tenant).


