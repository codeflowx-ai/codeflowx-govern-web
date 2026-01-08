# PROMPT: Inventario de Entidades JPA y DTOs FaaS

## 1. Objetivo
Centralizar la definición de entidades JPA, tablas físicas y DTOs transversales que los agentes deben respetar al generar nuevas capacidades FaaS (procesos, delegates, reglas, microservicios y UI). Este inventario funciona como contrato maestro de datos evitando divergencias entre sectores y garantizando tercera forma normal (3FN), prefijos consistentes y arquitectura hexagonal.

## 2. Convenciones de Modelado
- **Prefijo de tablas**: tres caracteres por dominio (`cor_` core, `gov_` governance, `met_` métricas, `prc_` procesos, `evd_` evidencias). Las tablas sectoriales pueden añadir un sufijo específico (`agr_`, `hlc_`, etc.).
- **Primary Key**: columna `id` autonumérica (`BIGSERIAL`) gestionada por la BD. En JPA se mapea con `@Id @GeneratedValue(strategy = GenerationType.IDENTITY)`.
- **Auditoría**: columnas estándar `created_at`, `created_by`, `updated_at`, `updated_by` (`TIMESTAMP WITH TIME ZONE`).
- **FKs / relaciones**: siempre con constraint explícito y nombre `<tabla>_<col>_fk`.
- **Paquetes Java**:
  - Entidades: `com.codeflowx.faas.core.persistence.entity`
  - DTOs (records): `com.codeflowx.faas.core.dto`
- **DTOs**: usar Java records, inmutables, nombrados `<EntityName>DTO`.
- **Serialización**: JSON (Jackson) y YAML siguiendo snake_case en payloads externos.
- **Naming**: columnas snake_case, atributos camelCase. Nombres semánticos orientados a negocio.

## 3. Catálogo de Entidades JPA

| Entidad JPA | Tabla | Descripción | Relaciones clave | DTO asociado |
|-------------|-------|-------------|------------------|--------------|
| `FrameworkEntity` | `cor_framework` | Catálogo maestro de frameworks FaaS (sector, versión, estado). | 1..n `FrameworkWorkflowEntity`, 1..n `FrameworkPolicyEntity`, 1..n `FrameworkMicroserviceEntity` | `FrameworkDTO` |
| `SectorEntity` | `cor_sector` | Sectores y subsectores normalizados con códigos oficiales. | 1..n `FrameworkEntity` | `SectorDTO` |
| `NormativeReferenceEntity` | `cor_normative_reference` | Normas, artículos y jurisdicciones aplicables. | n..n `FrameworkEntity` vía `cor_framework_normative` | `NormativeReferenceDTO` |
| `FrameworkWorkflowEntity` | `prc_framework_workflow` | Asociación framework ↔ proceso BPMN. | FK `cor_framework`, FK `prc_workflow_catalog` | `FrameworkWorkflowDTO` |
| `WorkflowCatalogEntity` | `prc_workflow_catalog` | Registro de procesos BPMN comunes (ubicación `classpath`). | n..n `FrameworkWorkflowEntity` | `WorkflowCatalogDTO` |
| `FrameworkPolicyEntity` | `gov_framework_policy` | Mapea frameworks a paquetes Drools y reglas activas. | FK `cor_framework`, FK `gov_policy_catalog` | `FrameworkPolicyDTO` |
| `PolicyCatalogEntity` | `gov_policy_catalog` | Políticas/reglas reutilizables con `policy_code` y `drl_source`. | n..n `FrameworkPolicyEntity` | `PolicyCatalogDTO` |
| `FrameworkMicroserviceEntity` | `cor_framework_microservice` | Microservicios obligatorios/opcionales por framework. | FK `cor_framework` | `FrameworkMicroserviceDTO` |
| `GovernanceEventEntity` | `gov_event` | Eventos normalizados que alimentan delegates/reglas. | FK `cor_framework`, FK `cor_sector` | `GovernanceEventDTO` |
| `EvidenceEntity` | `evd_evidence` | Evidencias documentales asociadas a eventos o KPIs. | FK `gov_event`, FK `met_metric_snapshot` | `EvidenceDTO` |
| `KpiCatalogEntity` | `met_kpi_catalog` | KPI con owner, unidad, fórmula y criticidad. | FK `cor_framework` (opcional) | `KpiCatalogDTO` |
| `MetricSnapshotEntity` | `met_metric_snapshot` | Valoración de KPIs por periodo y geo. | FK `met_kpi_catalog`, FK `cor_framework` | `MetricSnapshotDTO` |
| `AgentExecutionLogEntity` | `cor_agent_execution_log` | Bitácora de agentes generadores (prompt, checklist, resultado). | FK `cor_framework` | `AgentExecutionLogDTO` |

### 3.1 FrameworkEntity (`cor_framework`)

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| `id` | BIGINT | PK, autoincrement | Identificador interno. |
| `frameworkCode` | VARCHAR(64) | UNIQUE | Código metamodelo (ej. `AGRIFOOD_v1.0.0`). |
| `sectorCode` | VARCHAR(32) | FK `cor_sector(sector_code)` | Sector/subsector. |
| `version` | VARCHAR(16) | NOT NULL | Versionado semántico. |
| `riskProfile` | VARCHAR(12) | NOT NULL | `LOW`, `MEDIUM`, `HIGH`, `CRITICAL`. |
| `status` | VARCHAR(16) | NOT NULL | `DRAFT`, `READY`, `DEPRECATED`. |
| `geoScope` | JSONB | NOT NULL | Lista de códigos ISO-3166. |
| `metadata` | JSONB | NULL | Campos adicionales (datasets, prompts). |
| `createdAt` / `updatedAt` | TIMESTAMPTZ | NOT NULL | Auditoría. |
| `createdBy` / `updatedBy` | VARCHAR(64) | NOT NULL | Usuario/servicio. |

### 3.2 SectorEntity (`cor_sector`)

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| `id` | BIGINT | PK | |
| `sectorCode` | VARCHAR(32) | UNIQUE | Código corto (`HOSPITALITY_TOURISM`). |
| `sectorGroup` | VARCHAR(32) | NOT NULL | Macro sector (`HOSPITALITY`). |
| `subSector` | VARCHAR(64) | NOT NULL | Subsector granular (`TOUR_OPERATORS`). |
| `jurisdiction` | VARCHAR(16) | NOT NULL | `EU`, `BR`, `MX`, `DE`, `CO`, etc. |
| `description` | TEXT | NOT NULL | Resumen funcional. |
| `knowledgeCutoff` | DATE | NOT NULL | Última fecha de respaldo documental. |

### 3.3 NormativeReferenceEntity (`cor_normative_reference`)

| Campo | Tipo | Restricciones |
|-------|------|---------------|
| `id` | BIGINT | PK |
| `normativeCode` | VARCHAR(64) | UNIQUE |
| `title` | VARCHAR(256) | NOT NULL |
| `jurisdiction` | VARCHAR(16) | NOT NULL |
| `article` | VARCHAR(32) | NOT NULL |
| `summary` | TEXT | NOT NULL |
| `sourceUrl` | VARCHAR(512) | NULL |

Tabla intermedia `cor_framework_normative`:

| Campo | Tipo | Restricciones |
|-------|------|---------------|
| `id` | BIGINT | PK |
| `framework_id` | BIGINT | FK `cor_framework` |
| `normative_id` | BIGINT | FK `cor_normative_reference` |

### 3.4 FrameworkWorkflowEntity (`prc_framework_workflow`)

| Campo | Tipo | Restricciones |
|-------|------|---------------|
| `id` | BIGINT | PK |
| `framework_id` | BIGINT | FK `cor_framework` |
| `workflow_id` | BIGINT | FK `prc_workflow_catalog` |
| `activationStatus` | VARCHAR(16) | NOT NULL (`MANDATORY`/`OPTIONAL`) |
| `slaProfile` | VARCHAR(32) | NOT NULL |

### 3.5 WorkflowCatalogEntity (`prc_workflow_catalog`)

| Campo | Tipo | Restricciones |
|-------|------|---------------|
| `id` | BIGINT | PK |
| `workflowCode` | VARCHAR(64) | UNIQUE |
| `bpmnPath` | VARCHAR(256) | NOT NULL |
| `version` | VARCHAR(16) | NOT NULL |
| `description` | TEXT | NOT NULL |

### 3.6 FrameworkPolicyEntity (`gov_framework_policy`)

| Campo | Tipo | Restricciones |
|-------|------|---------------|
| `id` | BIGINT | PK |
| `framework_id` | BIGINT | FK `cor_framework` |
| `policy_id` | BIGINT | FK `gov_policy_catalog` |
| `activationCondition` | JSONB | NULL |

### 3.7 PolicyCatalogEntity (`gov_policy_catalog`)

| Campo | Tipo | Restricciones |
|-------|------|---------------|
| `id` | BIGINT | PK |
| `policyCode` | VARCHAR(64) | UNIQUE |
| `drlPackage` | VARCHAR(128) | NOT NULL |
| `drlSource` | VARCHAR(256) | NOT NULL |
| `description` | TEXT | NOT NULL |

### 3.8 FrameworkMicroserviceEntity (`cor_framework_microservice`)

| Campo | Tipo | Restricciones |
|-------|------|---------------|
| `id` | BIGINT | PK |
| `framework_id` | BIGINT | FK `cor_framework` |
| `microserviceId` | VARCHAR(128) | NOT NULL |
| `activation` | VARCHAR(16) | NOT NULL (`MANDATORY`, `OPTIONAL`) |
| `ownerTeam` | VARCHAR(64) | NOT NULL |

### 3.9 GovernanceEventEntity (`gov_event`)

| Campo | Tipo | Restricciones |
|-------|------|---------------|
| `id` | BIGINT | PK |
| `eventCode` | VARCHAR(64) | NOT NULL |
| `framework_id` | BIGINT | FK `cor_framework` |
| `sector_id` | BIGINT | FK `cor_sector` |
| `payload` | JSONB | NOT NULL |
| `sourceSystem` | VARCHAR(64) | NOT NULL |
| `ingestedAt` | TIMESTAMPTZ | NOT NULL |

### 3.10 EvidenceEntity (`evd_evidence`)

| Campo | Tipo | Restricciones |
|-------|------|---------------|
| `id` | BIGINT | PK |
| `evidenceCode` | VARCHAR(64) | NOT NULL |
| `event_id` | BIGINT | FK `gov_event` |
| `metric_snapshot_id` | BIGINT | FK `met_metric_snapshot` |
| `location` | VARCHAR(512) | NOT NULL (URL, S3, hash) |
| `hash` | VARCHAR(128) | NOT NULL |
| `issuedBy` | VARCHAR(128) | NOT NULL |
| `issuedAt` | TIMESTAMPTZ | NOT NULL |

### 3.11 KpiCatalogEntity (`met_kpi_catalog`)

| Campo | Tipo | Restricciones |
|-------|------|---------------|
| `id` | BIGINT | PK |
| `kpiCode` | VARCHAR(128) | UNIQUE |
| `framework_id` | BIGINT | FK `cor_framework` (nullable para KPIs globales) |
| `label` | VARCHAR(256) | NOT NULL |
| `unit` | VARCHAR(32) | NOT NULL |
| `category` | VARCHAR(32) | NOT NULL (`RISK`, `QUALITY`, etc.) |
| `criticality` | VARCHAR(16) | NOT NULL |
| `formula` | TEXT | NOT NULL |

### 3.12 MetricSnapshotEntity (`met_metric_snapshot`)

| Campo | Tipo | Restricciones |
|-------|------|---------------|
| `id` | BIGINT | PK |
| `kpi_id` | BIGINT | FK `met_kpi_catalog` |
| `framework_id` | BIGINT | FK `cor_framework` |
| `geo` | VARCHAR(8) | NOT NULL |
| `timeframeStart` | TIMESTAMPTZ | NOT NULL |
| `timeframeEnd` | TIMESTAMPTZ | NOT NULL |
| `value` | NUMERIC(18,4) | NOT NULL |
| `trend` | VARCHAR(12) | NOT NULL (`UP`, `DOWN`, `FLAT`) |

### 3.13 AgentExecutionLogEntity (`cor_agent_execution_log`)

| Campo | Tipo | Restricciones |
|-------|------|---------------|
| `id` | BIGINT | PK |
| `framework_id` | BIGINT | FK `cor_framework` |
| `agentName` | VARCHAR(64) | NOT NULL |
| `promptVersion` | VARCHAR(16) | NOT NULL |
| `checklist` | JSONB | NOT NULL |
| `status` | VARCHAR(16) | NOT NULL (`SUCCESS`, `WARNING`, `FAILED`) |
| `notes` | TEXT | NULL |
| `executedAt` | TIMESTAMPTZ | NOT NULL |

## 4. DTOs Asociados

| DTO (record) | Campos principales | Uso |
|--------------|-------------------|-----|
| `FrameworkDTO(String frameworkCode, String sectorCode, String version, String riskProfile, List<String> geoScope, Map<String,Object> metadata)` | Proveer metadatos a microservicios/UI. |
| `SectorDTO(String sectorCode, String sectorGroup, String subSector, String jurisdiction, String description, LocalDate knowledgeCutoff)` | Selección de sector y control de vigencia documental. |
| `NormativeReferenceDTO(String normativeCode, String title, String jurisdiction, String article, String summary, URI sourceUrl)` | Delegates y prompts normativos. |
| `FrameworkWorkflowDTO(String frameworkCode, String workflowCode, String bpmnPath, String slaProfile, Activation activation)` | Orquestación Flowable. |
| `WorkflowCatalogDTO(String workflowCode, String description, String version, URI bpmnUri)` | Registro central de procesos. |
| `FrameworkPolicyDTO(String frameworkCode, String policyCode, String drlPackage, URI drlSource, JsonNode activationCondition)` | Carga de reglas en runtime. |
| `PolicyCatalogDTO(String policyCode, String description, String drlPackage, URI drlSource)` | Gestión de repositorio Drools. |
| `FrameworkMicroserviceDTO(String frameworkCode, String microserviceId, Activation activation, String ownerTeam)` | Descubrimiento de capacidades. |
| `GovernanceEventDTO(String eventCode, String frameworkCode, String sectorCode, OffsetDateTime ingestedAt, JsonNode payload, String sourceSystem)` | Entrada estándar para delegates. |
| `EvidenceDTO(String evidenceCode, String location, String hash, String issuedBy, OffsetDateTime issuedAt, String relatedFrameworkCode, String relatedKpiCode)` | Presentación de evidencias en UI y reportes. |
| `KpiCatalogDTO(String kpiCode, String label, String unit, String category, String criticality, String formula, String frameworkCode)` | Configuración de dashboards y microservicios. |
| `MetricSnapshotDTO(String kpiCode, String frameworkCode, String geo, OffsetDateTime timeframeStart, OffsetDateTime timeframeEnd, BigDecimal value, Trend trend, List<EvidenceDTO> evidences)` | Serie temporal para UI y APIs. |
| `AgentExecutionLogDTO(String agentName, String promptVersion, String frameworkCode, OffsetDateTime executedAt, String status, JsonNode checklist, String notes)` | Auditoría del pipeline de agentes. |

## 5. Reglas para Actualizar Entidades/DTOs (agentes)
- Validar que nuevas columnas cumplen prefijo y 3FN; evitar campos calculados, usar vistas/materializaciones si es necesario.
- Toda columna nueva requiere ajuste del DTO y mapeadores MapStruct correspondientes (`faas-core`).
- Actualizar `docs/prompts/PROMPTS_FAAS_MICROS.md`, `PROMPTS_FAAS_PROCESOS_BPMN.md`, `PROMPTS_FAAS_JAVA_DELEGATES.md` y `PROMPTS_FAAS_REGLAS_DROOLS.md` con los campos impactados.
- Registrar migraciones Flyway (`db/migration/V<timestamp>__<desc>.sql`) en el microservicio correspondiente.
- Añadir ejemplos de payload en `docs/examples/` (JSON) cuando se incorporen campos críticos.
- Mantener sincronizado el catálogo en `metamodel/framework.yaml` (referencias a KPIs, procesos, microservicios).

## 6. Checklist para Agentes
- [ ] Confirmar vigencia documental (`SectorDTO.knowledgeCutoff`) para el sector/subsector abordado.
- [ ] Revisar si la normativa está en `NormativeReferenceEntity`; si no, crear registro con fuente citada.
- [ ] Ajustar `FrameworkDTO` y dependencias cuando se agreguen procesos/reglas/microservicios.
- [ ] Validar constraints (unique, fk) antes de generar scripts DDL.
- [ ] Ejecutar pruebas automáticas (unitarias + integración) después de modificar repositorios JPA.
- [ ] Documentar cambios en `docs/ORQUESTACION_AGENTES.md` y actualizar el log `AgentExecutionLogEntity`.

## 7. Próximos Pasos
- Generar plantillas en `docs/templates/` para scripts Flyway, MapStruct y repositorios JPA.
- Publicar ejemplo completo de entidad + DTO + repository + mapper para acelerar la automatización con agentes.
- Integrar el inventario con el prompt maestro (`docs/prompts/PROMPTS_FAAST_AGENT_MASTER.md`) para reforzar el contrato de datos en cada ejecución.
