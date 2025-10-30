## Integración RAG con leka-server (Python)

Objetivo: desacoplar la lógica RAG (ingesta, embeddings, búsqueda, métricas) hacia un microservicio Python (leka-server), manteniendo los ViewModels ZK en Java como orquestadores. Este documento detalla, por ViewModel, qué llamadas sustituir por endpoints HTTP/gRPC y qué payloads/estados manejar.

### Principios
- Todas las lecturas complejas (views/métricas) salen a endpoints de lectura del servicio RAG.
- Operaciones (ingesta, rebuild, rollback, versionado) invocan endpoints de acción idempotentes y asíncronos cuando aplique.
- Autenticación: JWT Bearer propagado desde portal. Añadir `X-Request-Id` para trazabilidad.
- Timeouts, retries (exponencial backoff) y circuit breaker en cliente Java.

### Arquitectura de separación (Gestión vs Ejecución)

#### Capa de Gestión, Gobierno y Auditoría (Java + PostgreSQL)

**Responsabilidad:** Metadatos, configuración, gobierno, permisos, auditoría, consumo/costes, integración con proyectos.

**Entidades JPA (PostgreSQL):**
- `RagSystem` (id, name, type, status, approvalStatus, projectId, tenantId, version, vectorBackend[qdrant|cohere|pinecone], connectionRef, createdBy, createdAt...)
- `RagDataSource` (id, systemId, name, kind[documents|api|database|web], connectionConfig, syncFrequency, lastSyncAt, indexStatus, docCount, ragReady, complianceTags)
- `RagVersion` (id, systemId, version, changes, embeddingsVersion, configSnapshot, createdAt, promotedBy)
- `RagPolicy` (id, systemId, subjectType[role|user|project], subjectId, actions[search|ingest|admin], scope)
- `RagApiToken` (id, systemId, tokenHash, scopes[], expiresAt, rateLimit)
- `RagConsumption` (id, systemId, projectId, period, tokens, requests, cost, provider)
- `RagAuditLog` (id, actor, action, entity, entityId, details, timestamp)

**Funciones:**
- CRUD de sistemas RAG, datasources, versiones
- Vinculación a proyectos/tenants/agentes
- Gestión de permisos y tokens (ACL, JWT scopes)
- Configuración de compliance/retención/lineage de datos
- Auditoría completa de acciones (búsquedas, ingestas, rollbacks)
- Consolidación de métricas de consumo/costes (tokens, requests, proveedores)
- Workflows de aprobación (governance status)
- Reporting y exportación de estadísticas

**Dónde:** `suinsit.nova.web` (portal ZK, JPA, vistas SQL, workflow BPMN)

---

#### Capa de Ejecución RAG (leka-server, Python)

**Responsabilidad:** Chunking, embeddings, vectorización, índices, búsqueda semántica, retrieval, reindex, evaluación técnica.

**Persistencia:**
- Qdrant/Cohere/Pinecone (vectores + payload: {systemId, datasourceId, projectId, domainId, docId, section, metadata, embeddingsVersion})
- Redis (caché de búsquedas, rate limiting)
- S3/FS (blobs opcionales: PDFs, datasets raw)

**Funciones:**
- Ingesta de documentos/datasets (chunking según estrategia configurable)
- Generación de embeddings (OpenAI, Azure, Cohere, Local/Ollama)
- Upsert a colecciones/índices vectoriales (con payload filters por tenant/proyecto/dominio)
- Búsqueda semántica (query + top-k + filtros de payload)
- Reindex incremental (por cambio de embeddings_version o configuración)
- Rollback de vectores (restaurar snapshot de versión anterior)
- Evaluación de calidad (retrieval precision, relevancia, coverage, distribución chunks)
- Métricas técnicas (latencia, RPS, hit/miss ratio, coste por query)
- Limpieza y garbage collection de vectores huérfanos

**Dónde:** microservicio Python FastAPI + Celery (tareas async) + Qdrant/Cohere/Pinecone

---

#### Contrato de integración

- **Quién decide qué:** Java gestiona catálogo, aprobaciones, cuotas; leka ejecuta pipelines.
- **Estados sincronizados:** Java persiste flags (`indexStatus`, `lastIndexAt`, `docCount`, `embeddingsVersion`); leka expone progresos y métricas.
- **Seguridad:** JWT con scopes `rag:search`, `rag:ingest`, `rag:admin`; filtros de tenant/proyecto se trasladan a Qdrant payload_filters o namespace en Pinecone/Cohere.
- **Datasets de dominio:** Metadatos (DomainDataset: filePath, compliance, isRagReady, trainingRelevance) en Java; chunks/embeddings/índices en leka-server tras ingesta.
- **Flujo típico:**
  1. Java: usuario crea RagSystem + RagDataSource (metadatos).
  2. Java: botón "Ingestar" → POST /rag/systems/{id}/ingest (leka).
  3. Leka: procesa (chunking, embeddings, upsert a Qdrant/Cohere/Pinecone), devuelve taskId.
  4. Java: actualiza indexStatus=IN_PROGRESS, muestra toast con taskId.
  5. Leka: al terminar, callback/webhook o polling → Java actualiza indexStatus=COMPLETED, docCount, lastIndexAt.
  6. Java: audita en RagAuditLog; actualiza consumo en RagConsumption (tokens/cost).

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

> **Nota:** Para la gestión de Domain Datasets (entrenamiento de dominios de negocio), consultar el documento dedicado: `docs/funcional/datasets/01_ARQUITECTURA_DATASETS.md`

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

#### Gestión (DB Java - PostgreSQL/JPA):

**Módulos de gestión RAG:**
- RagSystemsOverview/Detail (atributos de sistema, approval, relación con proyectos, tokens, **NO vectores**)
- RagDataSource* (metadatos de fuentes: nombre, tipo, conexión, frecuencia sync, compliance, **NO chunks/embeddings**)
- RagVersion* (metadatos de versionado: cambios, configuración, **NO snapshots vectoriales**)
- RagAccessControl (políticas ACL, permisos a nivel catálogo)
- RagCompliance (config y decisiones de gobierno, **NO análisis técnico de bias**)
- RagBiasDetection (configuración de umbrales y reglas, **NO ejecución de detección**)
- Audit/Consumption (RagAuditLog: acciones de usuario; RagConsumption: tokens/costes consolidados)


**Qué permanece en BusinessService (DB):**
- Crear/editar/borrar sistemas RAG, datasources, versiones, dominios, datasets
- Gestión de permisos, tokens, políticas de acceso
- Workflows de aprobación (BPMN)
- Auditoría de acciones de usuario
- Consolidación de métricas de consumo/facturación

---

#### Ejecución (leka-server - Python + Qdrant/Cohere/Pinecone):

**Operaciones vectoriales:**
- indexDataSources, rebuildIndex, rollbackVersion, createVersion (**materialización real en índices**)
- Ingesta de datasets: chunking (estrategias: fixed-size, semantic, sliding-window), embeddings batch, upsert vectorial
- Búsqueda semántica: query expansion, retrieval top-k, reranking
- Evaluación técnica de calidad: precision/recall, coverage de chunks, distribución semántica
- Bias detection ejecutado (análisis estadístico/ML sobre vectores)

**Panels de métricas técnicas:**
- metrics-summary, usage-by-agent (desde logs de Qdrant/proveedor)
- retrieval-quality (precision@k, recall@k, MRR)
- health (estado de colecciones, latencia promedio)
- embedding-progress (% completado, ETA, velocidad de ingesta)
- statistics, chunk-distribution (histogramas, densidad semántica)
- search-analytics (top queries, hit/miss ratio)

**Qué NO existe en Java DB:**
- Vectores, chunks, embeddings
- Payload de Qdrant/Cohere/Pinecone
- Snapshots de índices
- Logs de queries semánticas (solo auditoría de "quién buscó qué" en RagAuditLog)

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

**ViewModels RAG (govern & platform):**
- govern/viewmodel/rag/RagSystemsOverviewViewModel.java: loadData, loadMetrics → **DB (metadatos) + API (métricas técnicas)**.
- govern/viewmodel/rag/RagSystemsDetailViewModel.java: CRUD sistema → **DB**; datasources/versions metadata → **DB**; indexDataSources → **POST /rag/systems/{id}/ingest (API)**.
- platform/viewmodel/rag/*OverviewViewModel.java: todos los `findAll(View/Entity)` de metadatos → **DB**; panels de métricas → **GET endpoints API**.
- platform/viewmodel/rag/*DetailViewModel.java: CRUD → **DB**; navegación intacta.
- platform/viewmodel/rag/RagRollbackViewModel.java: metadata de rollback → **DB**; ejecución → **POST /rag/systems/{id}/rollback (API)**.
- platform/viewmodel/rag/RagAccessControlViewModel.java: políticas ACL → **DB**; enforcement payload filters → **PUT /rag/governance/access-control (API)**.
- platform/viewmodel/rag/RagComplianceViewModel.java: configuración cumplimiento → **DB**; análisis técnico → **GET /rag/governance/compliance (API)**.
- platform/viewmodel/rag/RagBiasDetectionViewModel.java: umbrales/reglas → **DB**; detección ejecutada → **GET /rag/governance/bias-detection (API)**.


### Siguientes pasos
1) Definir OpenAPI en leka-server con estos endpoints y DTOs.
2) Generar cliente Java (OpenAPI Generator) y publicarlo (Maven interno).
3) Sustituir llamadas por `RagApiClient` en ViewModels listados (otro chat).
4) Pruebas end-to-end con Qdrant en staging (colecciones por sistema/tenant).

---

## Resumen Ejecutivo

### ¿Qué se gestiona en Java (suinsit.nova.web)?

**Catálogo y configuración:**
- Sistemas RAG (nombre, tipo, versión, estado, aprobación, proyecto/tenant, backend vectorial seleccionado)
- Fuentes de datos (nombre, tipo, parámetros de conexión, frecuencia sync, compliance, estado)
- Versiones (metadata de cambios, configuración)
- Dominios de negocio (AgentDomain con categorías, skills, knowledgeAreas, datasets[referencias])
- Datasets para entrenamiento (DomainDataset: metadatos, filePath, compliance, isRagReady, trainingRelevance)

**Gobierno y seguridad:**
- Políticas de acceso (ACL: quién puede buscar/ingestar en qué sistemas)
- Tokens de API (scopes, rate limits, expiración)
- Configuración de umbrales de compliance y bias detection
- Workflows de aprobación (BPMN)

**Auditoría y facturación:**
- Registro de acciones de usuario (RagAuditLog)
- Consolidación de consumo/costes (RagConsumption: tokens, requests, cost por proveedor)

### ¿Qué se ejecuta en leka-server (Python)?

**Procesamiento vectorial:**
- Chunking de documentos/datasets (estrategias configurables)
- Generación de embeddings (OpenAI/Azure/Cohere/Local)
- Upsert a índices vectoriales (Qdrant colecciones, Cohere índices, Pinecone namespaces)
- Búsqueda semántica (retrieval top-k, reranking)
- Reindex, rollback de vectores, garbage collection

**Métricas técnicas:**
- Retrieval quality (precision@k, recall, MRR)
- Index health (latencia, disponibilidad, tamaño)
- Embedding progress (%, ETA, velocidad)
- Coverage analysis, chunk distribution, search analytics
- Bias detection ejecutado (análisis estadístico sobre vectores)

**Backends soportados:**
- Qdrant (colecciones + payload filters por tenant)
- Cohere (embeddings + vector DB + rerank)
- Pinecone (índices + namespaces + metadata filters)

### Puntos de integración clave

1. **CRUD de metadatos:** Java (DB)
2. **Ingesta/Indexación:** Java lanza → leka ejecuta → Java actualiza estado
3. **Búsquedas:** Java audita → leka ejecuta → Java registra consumo
4. **Métricas de UI:** leka expone → Java consulta y presenta
5. **Gobierno:** Java configura reglas → leka las aplica (filters, ACL)
6. **Datasets de dominio:** Java guarda metadatos → leka procesa e indexa chunks/embeddings

### Dependencias de despliegue

- Java requiere: leka-server disponible (health check antes de mostrar botones de ingesta)
- Leka requiere: Qdrant/Cohere/Pinecone configurado por tenant; credenciales en vault
- Autenticación: JWT emitido por Java, validado por leka (scopes rag:*)
- Observabilidad: correlation-id propagado en ambas capas; traces en APM


