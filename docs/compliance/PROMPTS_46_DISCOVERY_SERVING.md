# PROMPTS_46_DISCOVERY_SERVING

> **Objetivo**: Refactorizar el modelo de datos de *serving*, consolidar las relaciones con `models.model`/`projects.project` y construir un microservicio de *model discovery* con caché Redis que permita localizar endpoints publicados en cualquier topología (cluster propio, cluster externo, serverless o VM pods). **El micro discovery devolverá únicamente URL + credenciales/token del endpoint; no actuará como wrapper/proxy de inferencia.**

## Contexto actual
- Las entidades `serving` almacenan despliegues, endpoints, instancias y logs, pero faltan PKs, FKs y metadatos de infraestructura.
- Existen tablas duplicadas (`SRVMODELS`, `SRVMODELVERSIONS`) que deben eliminarse en favor de `models.model`.
- El tenant se resuelve vía `projects.project`; los registros actuales no tienen FK ni `project_id`.
- El micro discovery necesitará reutilizar esta información sin stressar la base de datos; Redis se usará como caché de respuestas.
- Decisión pendiente: **¿el micro discovery actúa como wrapper/proxy de inferencia o sólo devuelve URL + credenciales al consumidor (micro/front)?** → Escalar esta pregunta a arquitectura antes de implementar la ruta final.

## Entradas necesarias
- **JPA `nocode.service.entitys`** (`serving`, `models`, `infrastructure`, `projects`).
- Esquema PostgreSQL actual (`SRV*`, `INF*`, `PRO*`).
- Catálogos de estados (`deployment_status`, `endpoint_type`, `deployment_scope`).
- Requisitos de seguridad (autenticación, acceso multi-tenant) del portal y APIs externas.

## Entregables esperados
1. **Scripts SQL (patch)**
   - Eliminación de tablas duplicadas (`SRVMODELS`, `SRVMODELVERSIONS`) tras migrar datos si aplicase.
   - Nuevas columnas: `project_id`, `deployment_scope`, `base_url`, `namespace`, `cluster_id`, `provider_id`, `auth_config` (JSONB), `git_commit`, `pipeline_id`, etc.
   - Creación de claves foráneas y claves primarias (`idx + nombre tabla`, `BIGSERIAL`):
     - `ModelDeployment` → `projects.project`, `infrastructure.kubernetesclusters`/`cloud_provider` (nullable).
     - `ServingEndpoint`, `DeploymentInstance`, `ServingRequest`, `DeploymentLog`, `DeploymentMetric`, `ModelPrediction`, `ModelMetrics` → `ModelDeployment`.
     - Cuando aplique, FK a `ServingEndpoint`.
   - Índices por `project_id`, `deployment_scope`, `deployment_id`, `endpoint_id`.
2. **Refactor JPA** (`nocode.service.entitys`)
   - Remover clases duplicadas (`serving.Model`, `serving.ModelVersion`).
   - Añadir `@Id` + `@GeneratedValue` (IDENTITY) y `@ManyToOne` + `@JoinColumn` coherentes.
   - Normalizar enumeraciones (`deploymentScope`, `deploymentStatus`, `endpointType`) con catálogos/Enums Java.
3. **Microservicio `codeflowx-discovery-serving`**
   - Servicio Spring Boot (hexagonal, SOLID) con API REST:
     - `/v1/discovery/models/{projectId}`: lista de despliegues activos con metadatos (modelo, versión, scope, cluster/provider, endpoints, credenciales).
     - `/v1/discovery/endpoints/{deploymentId}` o `{endpointId}`: detalle enriquecido (URL completa, namespace, auth policy, pipeline, métricas).
     - Endpoint health (`/actuator/health`) y métricas (`/actuator/prometheus`).
   - Integración Redis (Spring Data Redis):
     - Cachear resultados por `projectId`/`deploymentId` con TTL configurable.
     - Invalidación automática ante cambios (`ModelDeployment.updatedAt`, hooks Flowable) o mediante eventos (Kafka/Rabbit si disponible).
   - Cliente DB:
     - Repositorios Spring Data JPA para entidades normalizadas.
     - Proyecciones/DTOs orientadas a la vista discovery.
   - Seguridad: `OAuth2`/JWT Gatekeeper (reutilizar configuración `govern`), control de acceso por tenant.
4. **Refactor Front (ZUL + ViewModels)**
   - Actualizar formularios de registro/edición en `src/main/webapp/console/gobierno/...` (modelos, deployments, endpoints, instancias) para capturar nuevos campos: `deploymentScope`, asociación a `project`, cluster/provider, `baseUrl`, `namespace`, `authConfig`, `pipelineId`, etc.
   - Ajustar `ViewModels` correspondientes (por ejemplo `ModelDeploymentViewModel`, `ServingEndpointViewModel`, `ModelRegistryViewModel`) para soportar los cambios de JPA y proveer selectores/validaciones.
   - Incluir combos/catalogos dinámicos (`deploymentScope`, `status`, `endpointType`) y bindings con multi-tenant.
   - Pruebas manuales/UI: crear despliegue en cada scope (plataforma, cluster externo, serverless, VM) y verificar que la información se persiste y se refleja en la consola y en el micro discovery.
5. **Documentación & operativa**
   - README técnico (config Redis, perfiles `local/pre/pro`).
   - Guía de despliegue Helm/Kubernetes (recursos, variables, secrets).
   - Tests integrados (`@DataJpaTest`, `@SpringBootTest`, WireMock si wrappers externos).
   - Postman / Insomnia collection para QA.

## Pasos sugeridos
1. **Análisis & Diseño**
   - Confirmar con arquitectura la decisión *wrapper vs credentials* y reflejarla en el API.
   - Definir `enum DeploymentScope { PLATFORM_CLUSTER, EXTERNAL_CLUSTER, SERVERLESS, VM_POD }`.
   - Mapear `projectId` para todos los registros existentes (consulta a `projects.project`).
2. **Migración de base de datos**
   - Escribir patch incremental en `sql-scripts/patches/XX_serving_discovery.sql`.
   - Probar en copia de BBDD; documentar rollback.
3. **Refactor JPA**
   - Actualizar entidades, tests y mapeos en `nocode.service.entitys` y consumidores (`codeflowx-govern-backend`).
   - Ejecutar `mvn clean test` completos.
4. **Implementación microservice**
   - Crear módulo Maven (o repo) `codeflowx-discovery-serving`.
   - Configurar Redis (`spring.redis.*`, perfiles) y caché con anotaciones `@Cacheable`, `@CacheEvict` o capa manual.
   - Implementar queries optimizadas (fetch join, vistas SQL si necesario) y endpoints REST.
5. **QA & Observabilidad**
   - Tests unitarios + integración (Docker Compose para Redis/Postgres).
   - Instrumentar logs (structured logging), métricas (Micrometer) y tracing (OpenTelemetry si disponible).
   - Validar escenarios: cluster propio, cluster externo (con provider info), serverless (sin cluster), VM pods.
6. **Entrega**
   - PR + documentación (README, changelog, scripts deploy).
   - Checklist: migración aplicada, micro desplegado, caché funcionando, decisión wrapper cerrada.

## Decisiones / Preguntas abiertas
- **Discovery como wrapper**: responder si debe proxyar la llamada al modelo (streaming de respuesta, control de cuotas) o limitarse a devolver URL + credenciales al consumidor. Decisión condiciona seguridad, latency y contract API.
- Estrategia de invalidación de caché (event-driven vs TTL fijo).
- ¿Se expondrá catálogo para UI (ZUL) o sólo API machine-to-machine?
- ¿Necesitamos registrar credenciales en Vault/Secrets y entregar tokens efímeros?

---
**Asignaciones**: Este prompt está listo para ser tomado por un nuevo chat/equipo que ejecute la refactorización, scripts y microservicio siguiendo las directrices.
