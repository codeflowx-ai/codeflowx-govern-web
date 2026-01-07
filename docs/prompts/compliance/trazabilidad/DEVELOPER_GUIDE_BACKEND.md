# 👨‍💻 GUÍA PARA DEVELOPERS BACKEND - TRACEABILITY

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Desarrolladores Backend (Java/Spring Boot)

---

## 📋 ÍNDICE

1. [Arquitectura General](#arquitectura-general)
2. [Estructura de Capas](#estructura-de-capas)
3. [Servicios de Negocio](#servicios-de-negocio)
4. [Entidades JPA](#entidades-jpa)
5. [Repositorios](#repositorios)
6. [BFF (Backend for Frontend)](#bff-backend-for-frontend)
7. [Microservicio de Negocio](#microservicio-de-negocio)
8. [Flujos de Negocio](#flujos-de-negocio)
9. [Validaciones y Reglas](#validaciones-y-reglas)
10. [Scripts SQL - Triggers, Funciones y Procedimientos](#scripts-sql---triggers-funciones-y-procedimientos)

---

## 🏗️ ARQUITECTURA GENERAL

### Capas de la Aplicación

```
Frontend (Next.js)
    ↓
BFF (Backend for Frontend)
    ↓
Business Microservice (Traceability Service)
    ↓
Business Services (Lógica de Negocio)
    ↓
Repositories (JPA)
    ↓
Database (PostgreSQL)
```

### Componentes Principales

1. **BFF (Backend for Frontend)**
   - Ubicación: `codeflowx.govern.bff.compliance`
   - Responsabilidad: Agregar datos, optimizar respuestas para frontend
   - Tecnología: Spring WebFlux (Reactivo)

2. **Business Microservice**
   - Ubicación: `codeflowx-governance-traceability-service`
   - Responsabilidad: Endpoints REST para Traceability
   - Tecnología: Spring WebFlux (Reactivo)

3. **Business Services**
   - Ubicación: `codeflowx.govern.business`
   - Responsabilidad: Lógica de negocio, validaciones, reglas
   - Tecnología: Spring Boot (Transaccional)

4. **Entities & Repositories**
   - Ubicación: `nocode.service.entitys`, `codeflowx.govern.repository`
   - Responsabilidad: Persistencia de datos
   - Tecnología: JPA/Hibernate

---

## 📁 ESTRUCTURA DE CAPAS

### 1. BFF Layer

**Ubicación:** `nocode.service/codeflowx.govern.bff.compliance/`

**Componentes:**
- **Controller:** `controller/TraceabilityController.java`
  - Expone endpoints REST para frontend
  - Maneja requests HTTP
  - Retorna DTOs optimizados

- **Service:** `service/TraceabilityService.java` (interface)
  - Define contratos de servicio

- **Service Implementation:** `service/impl/TraceabilityServiceImpl.java`
  - Implementa llamadas a microservicio de negocio
  - Usa WebClient para comunicación reactiva
  - Implementa Circuit Breaker y Retry (Resilience4j)

**Ejemplo:**
```java
@RestController
@RequestMapping("/api/v1/traceability")
public class TraceabilityController {
    private final TraceabilityService traceabilityService;

    @GetMapping
    public Mono<ResponseEntity<TraceabilityEvidenceDto>> getTraceability(
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) Long entityId) {
        return traceabilityService.getTraceability(entityType, entityId)
                .map(ResponseEntity::ok);
    }
}
```

---

### 2. Business Microservice Layer

**Ubicación:** `nocode.service/codeflowx-governance-traceability-service/`

**Componentes:**
- **Controller:** `controller/TraceabilityController.java`
  - Endpoints REST del microservicio
  - Llama a Business Services
  - Convierte Entities a DTOs

**Ejemplo:**
```java
@RestController
@RequestMapping("/api/v1/traceability")
public class TraceabilityController {
    private final TraceabilityService traceabilityService;

    @GetMapping("/{entityType}/{id}")
    public Mono<ResponseEntity<TraceabilityEvidenceDto>> getEntityTraceability(
            @PathVariable String entityType,
            @PathVariable Long id) {
        return Mono.fromCallable(() -> {
                TraceabilityService.TraceabilityEvidence evidence =
                    traceabilityService.exportTraceabilityEvidence(id, entityType);
                return toEvidenceDto(evidence);
            })
            .subscribeOn(Schedulers.boundedElastic())
            .map(ResponseEntity::ok);
    }
}
```

---

### 3. Business Services Layer

**Ubicación:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/`

**Servicios Principales:**

#### `TraceabilityService`

**Responsabilidades:**
- Obtener trazabilidad completa de Model, Project, Agent
- Verificar integridad de logs inmutables
- Exportar evidencias de trazabilidad
- Disparar workflows BPMN para alertas de integridad

**Métodos Principales:**
```java
@Service
public class TraceabilityService {

    // Obtener trazabilidad de modelo
    public ModelTraceability getModelTraceability(Long modelId) {
        // Obtener modelo
        Model model = modelRepository.findById(modelId)
            .orElseThrow(() -> new IllegalArgumentException("Model not found"));

        // Obtener logs inmutables
        List<ImmutableLog> logs = immutableLogRepository
            .findByImlentitytypeAndImlentityidOrderByImltimestampAsc("Model", modelId);

        // Obtener decisiones HITL
        List<HitlDecision> decisions = hitlDecisionRepository
            .findByEntityType("Model").stream()
            .filter(d -> d.getIdxentity() != null && d.getIdxentity().equals(modelId))
            .collect(Collectors.toList());

        // Construir trazabilidad
        ModelTraceability traceability = new ModelTraceability();
        traceability.setModel(model);
        traceability.setLogs(logs);
        traceability.setDecisions(decisions);

        return traceability;
    }

    // Obtener trazabilidad de proyecto
    public ProjectTraceability getProjectTraceability(Long projectId) {
        // Obtener proyecto
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        // Obtener modelos del proyecto a través de ModelDeployment
        List<Model> models = modelDeploymentRepository
            .findDistinctModelsByProjectId(projectId);

        // Obtener logs inmutables
        List<ImmutableLog> logs = immutableLogRepository
            .findByImlentitytypeAndImlentityidOrderByImltimestampAsc("Project", projectId);

        // Obtener decisiones HITL
        List<HitlDecision> decisions = hitlDecisionRepository
            .findByEntityType("Project").stream()
            .filter(d -> d.getIdxentity() != null && d.getIdxentity().equals(projectId))
            .collect(Collectors.toList());

        // Construir trazabilidad
        ProjectTraceability traceability = new ProjectTraceability();
        traceability.setProject(project);
        traceability.setModels(models);
        traceability.setLogs(logs);
        traceability.setDecisions(decisions);

        return traceability;
    }

    // Obtener trazabilidad de agente
    public AgentTraceability getAgentTraceability(Long agentId) {
        // Similar a getModelTraceability pero para Agent
        // ...
    }

    // Exportar evidencias con verificación de integridad
    public TraceabilityEvidence exportTraceabilityEvidence(Long entityId, String entityType) {
        TraceabilityEvidence evidence = new TraceabilityEvidence();
        evidence.setEntityType(entityType);
        evidence.setEntityId(entityId);

        // Obtener trazabilidad según tipo
        switch (entityType.toUpperCase()) {
            case "MODEL":
                evidence.setData(getModelTraceability(entityId));
                break;
            case "PROJECT":
                evidence.setData(getProjectTraceability(entityId));
                break;
            case "AGENT":
                evidence.setData(getAgentTraceability(entityId));
                break;
            default:
                throw new IllegalArgumentException("Invalid entity type: " + entityType);
        }

        // Verificar integridad de logs
        List<ImmutableLog> logs = evidence.getLogs();
        if (logs != null && logs.size() > 1) {
            Long firstId = logs.get(0).getIdximmutablelog();
            Long lastId = logs.get(logs.size() - 1).getIdximmutablelog();
            ImmutableLoggingBusinessService.LogIntegrityReport integrityReport =
                immutableLoggingBusinessService.verifyIntegrity(firstId, lastId);

            evidence.setIntegrityScore(integrityReport.isIntegrityValid() ? 1.0 : 0.0);
            evidence.setIntegrityStatus(integrityReport.isIntegrityValid() ? "INTEGRITY_OK" : "INTEGRITY_ERROR");
            evidence.setVerifiedLogs(integrityReport.getTotalLogsChecked());
            evidence.setTotalLogs(logs.size());
        }

        // Si hay problemas de integridad, disparar workflow BPMN
        if (evidence.getIntegrityStatus() != null &&
            (evidence.getIntegrityStatus().equals("INTEGRITY_ERROR") ||
             evidence.getIntegrityStatus().equals("INTEGRITY_WARNING"))) {
            triggerIntegrityAlertWorkflow(entityType, entityId, evidence.getIntegrityStatus());
        }

        return evidence;
    }

    // Disparar workflow BPMN para alerta de integridad
    public String triggerIntegrityAlertWorkflow(String entityType, Long entityId, String integrityStatus) {
        if (bpmnWorkflowClient == null || !bpmnWorkflowClient.isAvailable()) {
            log.warn("BpmnWorkflowClient no está disponible");
            return null;
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("entityType", entityType);
        variables.put("entityId", entityId);
        variables.put("integrityStatus", integrityStatus);

        return bpmnWorkflowClient.startProcess(
            "traceability-integrity-alert-workflow",
            variables
        );
    }
}
```

---

## 🗄️ ENTIDADES JPA

### Ubicación
`nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/`

### Entidades Principales

#### `Model`
**Tabla:** `SRVMODELS` o `MODMODELS`

**Campos Principales:**
- `idxmodel` (PK)
- `modname` (nombre del modelo)
- `modversion` (versión)
- `moddescription` (descripción)
- Relación con `Project` a través de `ModelDeployment` (srv_project_id)

#### `Project`
**Tabla:** `PRJPROJECTS`

**Campos Principales:**
- `idxproject` (PK)
- `name` (nombre del proyecto)
- `description` (descripción)
- `status` (estado)

#### `Agent`
**Tabla:** `AGTAGENTS`

**Campos Principales:**
- `idxagent` (PK)
- `agtname` (nombre del agente)
- `agtdescription` (descripción)
- `agtstatus` (estado)

#### `ImmutableLog`
**Tabla:** `IMLIMMUTABLELOGS`

**Campos Principales:**
- `idximmutablelog` (PK)
- `iduuid` (UUID, UNIQUE)
- `imlentitytype` (tipo de entidad: Model, Project, Agent)
- `imlentityid` (ID de la entidad)
- `imlaction` (acción realizada)
- `imluserid` (usuario)
- `imlcurrenthash` (hash actual, UNIQUE)
- `imlprevioushash` (hash anterior)
- `imltimestamp` (timestamp)
- `imlintegritystatus` (estado de integridad)

**Índices:**
- `idx_iml_entity`: `(IMLENTITYTYPE, IMLENTITYID)` - Para búsquedas por entidad
- `idx_iml_timestamp`: `(IMLTIMESTAMP)` - Para ordenamiento temporal
- `idx_iml_epoch`: `(IMLTIMESTAMPEPOCH)` - Para sorting eficiente
- `idx_iml_hash`: `(IMLCURRENTHASH)` - Para búsquedas por hash
- `idx_iml_user`: `(IMLUSERID)` - Para búsquedas por usuario

**Claves Únicas:**
- `iduuid`: UUID único
- `imlcurrenthash`: Hash único (garantiza unicidad de logs)

**CRÍTICO:** Esta tabla es APPEND-ONLY. NUNCA se permite UPDATE ni DELETE. Ver script SQL para trigger.

#### `HitlDecision`
**Tabla:** `GOVHITLDECISIONS`

**Campos Principales:**
- `idxhitldecision` (PK)
- `iduuid` (UUID, UNIQUE)
- `idxhitlsupervision` (FK a HitlSupervision)
- `hitlentitytype` (tipo de entidad)
- `idxentity` (ID de la entidad)
- `hitldecision` (decisión: APPROVED, REJECTED, PENDING)
- `hitldecisionreason` (razón)
- `hitldecisiondate` (fecha de decisión)
- `hitluserid` (usuario)

**Índices:**
- `idx_hitl_entity`: `(HITLENTITYTYPE, IDXENTITY)` - Para búsquedas por entidad
- `idx_hitl_decision_date`: `(HITLDECISIONDATE)` - Para ordenamiento temporal
- `idx_hitl_user`: `(HITLUSERID)` - Para búsquedas por usuario
- `idx_hitl_supervision`: `(IDXHITLSUPERVISION)` - Para búsquedas por supervisión
- `idx_hitl_decision_type`: `(HITLDECISION)` - Para filtros por tipo de decisión

**Claves Únicas:**
- `uk_hitl_uuid`: UUID único

#### `ModelDeployment`
**Tabla:** `srvdeployment`

**Campos Principales:**
- `srv_id` (PK)
- `srv_project_id` (FK a Project)
- `srv_model_id` (FK a Model)
- `srv_model_version_id` (FK a ModelVersion)
- `srv_name` (nombre del deployment)
- `srv_status` (estado)
- `srv_is_active` (activo)
- `srv_created_at` (fecha de creación)

**Índices:**
- `idx_srv_project`: `(srv_project_id)` - Para búsquedas por proyecto
- `idx_srv_model`: `(srv_model_id)` - Para búsquedas por modelo
- `idx_srv_project_model`: `(srv_project_id, srv_model_id)` - Para queries de trazabilidad (composite)
- `idx_srv_status`: `(srv_status)` - Para filtros por estado
- `idx_srv_active`: `(srv_is_active)` - Para filtros por activo
- `idx_srv_created_at`: `(srv_created_at)` - Para ordenamiento temporal

**Relaciones:**
- `@ManyToOne` con `Project` (srv_project_id)
- `@ManyToOne` con `Model` (srv_model_id)

---

## 📚 REPOSITORIOS

### Ubicación
`nocode.service/codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/`

### Repositorios Principales

#### `ModelRepository`
```java
@Repository
public interface ModelRepository extends GenericRepository<Model, Long> {
    List<Model> findByType(String type);
}
```

#### `ProjectRepository`
```java
@Repository
public interface ProjectRepository extends GenericRepository<Project, Long> {
    // Métodos heredados de GenericRepository
}
```

#### `AgentRepository`
```java
@Repository
public interface AgentRepository extends GenericRepository<Agent, Long> {
    // Métodos heredados de GenericRepository
}
```

#### `ImmutableLogRepository`
```java
@Repository
public interface ImmutableLogRepository extends GenericRepository<ImmutableLog, Long> {
    List<ImmutableLog> findByImlentitytypeAndImlentityidOrderByImltimestampAsc(
        String entityType, Long entityId);
}
```

#### `HitlDecisionRepository`
```java
@Repository
public interface HitlDecisionRepository extends GenericRepository<HitlDecision, Long> {
    List<HitlDecision> findByEntityType(String entityType);
}
```

#### `ModelDeploymentRepository`
```java
@Repository
public interface ModelDeploymentRepository extends GenericRepository<ModelDeployment, Long> {
    List<ModelDeployment> findByProject(Project project);

    @Query("SELECT d FROM ModelDeployment d WHERE d.project.idxproject = :projectId")
    List<ModelDeployment> findByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT DISTINCT d.model FROM ModelDeployment d WHERE d.project.idxproject = :projectId AND d.model IS NOT NULL")
    List<Model> findDistinctModelsByProjectId(@Param("projectId") Long projectId);
}
```

---

## 🔄 FLUJOS DE NEGOCIO

### Flujo 1: Obtener Trazabilidad de Entidad

```
1. Frontend → GET /api/v1/traceability/{entityType}/{id}
2. BFF → Business Microservice → GET /api/v1/traceability/{entityType}/{id}
3. Business Microservice → TraceabilityService.getModelTraceability() / getProjectTraceability() / getAgentTraceability()
4. Service obtiene entidad del repositorio
5. Service obtiene logs inmutables del repositorio
6. Service obtiene decisiones HITL del repositorio
7. Service obtiene modelos del proyecto (si es Project) a través de ModelDeploymentRepository
8. Service construye objeto de trazabilidad
9. Service convierte a DTO
10. Frontend recibe trazabilidad completa
```

### Flujo 2: Exportar Evidencias con Verificación de Integridad

```
1. Frontend → POST /api/v1/traceability/export?entityType=Model&entityId=123&format=JSON
2. BFF → Business Microservice → POST /api/v1/traceability/export
3. Business Microservice → TraceabilityService.exportTraceabilityEvidence()
4. Service obtiene trazabilidad completa
5. Service verifica integridad usando ImmutableLoggingBusinessService.verifyIntegrity()
6. Service calcula score de integridad (0.0 - 1.0)
7. Service determina estado (INTEGRITY_OK, INTEGRITY_WARNING, INTEGRITY_ERROR)
8. Si hay problemas de integridad, Service dispara workflow BPMN de alerta
9. Service retorna evidencias con verificación de integridad
10. Frontend recibe evidencias exportables
```

### Flujo 3: Búsqueda de Trazabilidad con Criterios

```
1. Frontend → POST /api/v1/traceability/search
2. BFF → Business Microservice → POST /api/v1/traceability/search
3. Business Microservice → TraceabilityService.exportTraceabilityEvidence() con criterios
4. Service aplica filtros según criterios:
   - entityType, entityId
   - Rango de fechas
   - Usuario
   - Tipo de acción
5. Service retorna trazabilidad filtrada
6. Frontend recibe resultados filtrados
```

---

## ✅ VALIDACIONES Y REGLAS

### Validaciones de Trazabilidad

**Ubicación:** `TraceabilityService`

**Reglas:**
1. EntityType debe ser válido: MODEL, PROJECT, o AGENT
2. EntityId debe existir en la base de datos
3. Los logs inmutables deben estar ordenados por timestamp ascendente
4. La verificación de integridad requiere al menos 2 logs
5. Si hay problemas de integridad, se dispara workflow BPMN automáticamente

### Reglas de Verificación de Integridad

**Ubicación:** `ImmutableLoggingBusinessService.verifyIntegrity()`

**Reglas:**
1. Verifica cadena de hashes (cada log debe tener el hash del anterior)
2. Calcula score de integridad (porcentaje de logs verificados)
3. Determina estado según score:
   - INTEGRITY_OK: score = 1.0
   - INTEGRITY_WARNING: 0.5 <= score < 1.0
   - INTEGRITY_ERROR: score < 0.5

### Reglas de Relación Model-Project

**Ubicación:** `ModelDeploymentRepository`

**Reglas:**
1. La relación Model-Project se obtiene a través de `ModelDeployment`
2. Un proyecto puede tener múltiples modelos (a través de deployments)
3. Un modelo puede estar en múltiples proyectos
4. Se usa `findDistinctModelsByProjectId()` para obtener modelos únicos

---

## 📜 SCRIPTS SQL - TRIGGERS, FUNCIONES Y PROCEDIMIENTOS

### Ubicación de Scripts

Todos los scripts SQL deben ubicarse en:
```
nocode.service/codeflowx.govern.repository/src/main/resources/db/migration/
```

**Convención de Nomenclatura:**
- `V{version}__{descripcion}.sql` (Flyway)
- Ejemplo: `V100__traceability_immutable_logs_trigger.sql`

**Referencia Completa:**
Ver archivo: `docs/prompts/compliance/trazabilidad/sql/TRACEABILITY_SQL_SCRIPTS.md`

---

## ⚙️ CONFIGURACIÓN

### Application.yml (BFF)

**Ubicación:** `nocode.service/codeflowx.govern.bff.compliance/src/main/resources/application.yml`

**Configuración de Servicios:**
```yaml
services:
  traceability:
    base-url: http://codeflowx-governance-traceability-service:8080
    timeout: 30s
    circuit-breaker:
      name: traceabilityServiceCircuitBreaker
      failure-rate-threshold: 50
      wait-duration-in-open-state: 10s
      sliding-window-size: 10
    retry:
      name: traceabilityServiceRetry
      max-attempts: 3
      wait-duration: 1s
```

**Configuración de BPMN (Opcional):**
```yaml
bpmn:
  workflow:
    enabled: true
    base-url: http://bpmn-engine:8080
    integrity-alert-process-key: traceability-integrity-alert-workflow
```

### Application.yml (Microservicio)

**Ubicación:** `nocode.service/codeflowx-governance-traceability-service/src/main/resources/application.yml`

**Configuración de Base de Datos:**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/governance_db
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

**Configuración de Observabilidad:**
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
```

---

## 📦 ESTRUCTURA DE DTOs

### TraceabilityEvidenceDto

**Ubicación:** `nocode.service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/TraceabilityEvidenceDto.java`

**Campos:**
```java
public class TraceabilityEvidenceDto {
    private String entityType;              // MODEL, PROJECT, AGENT
    private Long entityId;                  // ID de la entidad
    private Double integrityScore;          // 0.0 - 1.0
    private String integrityStatus;         // INTEGRITY_OK, INTEGRITY_WARNING, INTEGRITY_ERROR, NO_LOGS
    private Integer verifiedLogs;           // Número de logs verificados
    private Integer totalLogs;              // Número total de logs
    private String exportDate;              // ISO 8601 (opcional)
    private Object data;                     // ModelTraceabilityDto, ProjectTraceabilityDto o AgentTraceabilityDto
}
```

### ModelTraceabilityDto

**Ubicación:** `nocode.service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/ModelTraceabilityDto.java`

**Campos:**
```java
public class ModelTraceabilityDto {
    private ModelInfoDto model;            // Información del modelo
    private Long trainingDatasetId;         // ID del dataset de entrenamiento (opcional)
    private List<ImmutableLogDto> logs;     // Logs inmutables
    private List<HitlDecisionDto> decisions; // Decisiones HITL
    private List<ModelOutputDto> outputs;  // Outputs generados

    // Inner classes
    public static class ModelInfoDto {
        private Long id;
        private String name;
        private String version;
        private String description;
    }

    public static class ModelOutputDto {
        private Long id;
        private String timestamp;           // ISO 8601
        private String input;
        private String output;
        private Double confidence;          // 0.0 - 1.0
    }
}
```

### ProjectTraceabilityDto

**Ubicación:** `nocode.service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/ProjectTraceabilityDto.java`

**Campos:**
```java
public class ProjectTraceabilityDto {
    private ProjectInfoDto project;        // Información del proyecto
    private List<ModelInfoDto> models;      // Modelos asociados (a través de ModelDeployment)
    private List<ImmutableLogDto> logs;     // Logs inmutables
    private List<HitlDecisionDto> decisions; // Decisiones HITL
    private List<ProjectOutputDto> outputs; // Outputs generados

    // Inner classes
    public static class ProjectInfoDto {
        private Long id;
        private String name;
        private String description;
        private String status;
    }

    public static class ProjectOutputDto {
        private Long id;
        private String timestamp;           // ISO 8601
        private String input;
        private String output;
        private Long modelId;               // ID del modelo que generó el output
    }
}
```

### TraceabilitySearchCriteriaDto

**Ubicación:** `nocode.service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/TraceabilitySearchCriteriaDto.java`

**Campos:**
```java
public class TraceabilitySearchCriteriaDto {
    private String entityType;             // MODEL, PROJECT, AGENT (opcional)
    private Long entityId;                 // ID de la entidad (opcional)
    private String startDate;              // ISO 8601 (opcional)
    private String endDate;                // ISO 8601 (opcional)
    private String userId;                 // ID del usuario (opcional)
    private String actionType;             // Tipo de acción (opcional)
}
```

---

## 🛠️ MANEJO DE ERRORES Y EXCEPCIONES

### Excepciones Comunes

#### EntityNotFoundException
**Cuándo ocurre:** Cuando se intenta obtener trazabilidad de una entidad que no existe.

**Código:**
```java
// TraceabilityService.java
Model model = modelRepository.findById(modelId)
    .orElseThrow(() -> new IllegalArgumentException("Model not found: " + modelId));
```

**Manejo en Controller:**
```java
@ExceptionHandler(IllegalArgumentException.class)
public Mono<ResponseEntity<ErrorResponse>> handleIllegalArgument(IllegalArgumentException ex) {
    return Mono.just(ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse("ENTITY_NOT_FOUND", ex.getMessage())));
}
```

#### InvalidEntityTypeException
**Cuándo ocurre:** Cuando se proporciona un entityType inválido.

**Código:**
```java
// TraceabilityService.java
switch (entityType.toUpperCase()) {
    case "MODEL":
        evidence.setData(getModelTraceability(entityId));
        break;
    case "PROJECT":
        evidence.setData(getProjectTraceability(entityId));
        break;
    case "AGENT":
        evidence.setData(getAgentTraceability(entityId));
        break;
    default:
        throw new IllegalArgumentException("Invalid entity type: " + entityType);
}
```

#### IntegrityVerificationException
**Cuándo ocurre:** Cuando hay problemas críticos en la verificación de integridad.

**Manejo:**
```java
// TraceabilityService.java
if (evidence.getIntegrityStatus() != null &&
    evidence.getIntegrityStatus().equals("INTEGRITY_ERROR")) {
    log.error("Critical integrity error detected for {} {}", entityType, entityId);
    triggerIntegrityAlertWorkflow(entityType, entityId, evidence.getIntegrityStatus());
}
```

### Circuit Breaker y Retry

**Configuración en BFF:**
```java
@CircuitBreaker(name = "traceabilityServiceCircuitBreaker", fallbackMethod = "getTraceabilityFallback")
@Retry(name = "traceabilityServiceRetry")
public Mono<TraceabilityEvidenceDto> getTraceability(String entityType, Long entityId) {
    // Llamada al microservicio
}

// Fallback method
public Mono<TraceabilityEvidenceDto> getTraceabilityFallback(String entityType, Long entityId, Exception ex) {
    log.warn("Circuit breaker opened, returning empty evidence", ex);
    TraceabilityEvidenceDto fallback = new TraceabilityEvidenceDto();
    fallback.setEntityType(entityType);
    fallback.setEntityId(entityId);
    fallback.setIntegrityStatus("SERVICE_UNAVAILABLE");
    return Mono.just(fallback);
}
```

---

## 🧪 TESTING

### Unit Tests - TraceabilityService

**Ubicación:** `nocode.service/codeflowx.govern.business/src/test/java/com/codeflowx/govern/business/compliance/TraceabilityServiceTest.java`

**Ejemplo:**
```java
@ExtendWith(MockitoExtension.class)
class TraceabilityServiceTest {

    @Mock
    private ModelRepository modelRepository;

    @Mock
    private ImmutableLogRepository immutableLogRepository;

    @Mock
    private HitlDecisionRepository hitlDecisionRepository;

    @InjectMocks
    private TraceabilityService traceabilityService;

    @Test
    void testGetModelTraceability_Success() {
        // Given
        Long modelId = 123L;
        Model model = new Model();
        model.setIdxmodel(modelId);
        model.setModname("Test Model");

        when(modelRepository.findById(modelId)).thenReturn(Optional.of(model));
        when(immutableLogRepository.findByImlentitytypeAndImlentityidOrderByImltimestampAsc("Model", modelId))
            .thenReturn(Collections.emptyList());
        when(hitlDecisionRepository.findByEntityType("Model"))
            .thenReturn(Collections.emptyList());

        // When
        TraceabilityService.ModelTraceability result =
            traceabilityService.getModelTraceability(modelId);

        // Then
        assertNotNull(result);
        assertEquals(modelId, result.getModel().getIdxmodel());
        verify(modelRepository).findById(modelId);
    }

    @Test
    void testGetModelTraceability_NotFound() {
        // Given
        Long modelId = 999L;
        when(modelRepository.findById(modelId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> {
            traceabilityService.getModelTraceability(modelId);
        });
    }
}
```

### Integration Tests - Controller

**Ubicación:** `nocode.service/codeflowx-governance-traceability-service/src/test/java/com/codeflowx/govern/traceability/controller/TraceabilityControllerTest.java`

**Ejemplo:**
```java
@SpringBootTest
@AutoConfigureWebTestClient
class TraceabilityControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TraceabilityService traceabilityService;

    @Test
    void testGetEntityTraceability_Success() {
        // Given
        String entityType = "Model";
        Long entityId = 123L;
        TraceabilityService.TraceabilityEvidence evidence =
            createMockEvidence(entityType, entityId);

        when(traceabilityService.exportTraceabilityEvidence(entityId, entityType))
            .thenReturn(evidence);

        // When/Then
        webTestClient.get()
            .uri("/api/v1/traceability/{entityType}/{id}", entityType, entityId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(TraceabilityEvidenceDto.class)
            .value(dto -> {
                assertEquals(entityType, dto.getEntityType());
                assertEquals(entityId, dto.getEntityId());
            });
    }
}
```

---

## 🔧 TROUBLESHOOTING COMÚN

### Problema 1: Circuit Breaker Abierto

**Síntoma:** Las llamadas al microservicio fallan inmediatamente con `SERVICE_UNAVAILABLE`.

**Causas Posibles:**
- Microservicio no está disponible
- Timeout muy corto
- Tasa de fallos alta

**Solución:**
1. Verificar que el microservicio esté corriendo
2. Revisar logs del microservicio
3. Aumentar `wait-duration-in-open-state` en configuración
4. Verificar conectividad de red

### Problema 2: Integridad No Verificada

**Síntoma:** `integrityStatus` es `INTEGRITY_WARNING` o `INTEGRITY_ERROR`.

**Causas Posibles:**
- Logs modificados manualmente
- Hash chain rota
- Logs faltantes

**Solución:**
1. Revisar logs individuales en la base de datos
2. Verificar que cada log tiene el hash correcto del anterior
3. Identificar el primer log con hash incorrecto
4. Investigar causa (modificación manual, error en inserción, etc.)
5. Corregir el problema y re-verificar

### Problema 3: Modelos No Aparecen en Trazabilidad de Proyecto

**Síntoma:** `ProjectTraceabilityDto.models` está vacío aunque hay deployments.

**Causas Posibles:**
- No hay registros en `ModelDeployment` para ese proyecto
- Query `findDistinctModelsByProjectId` no encuentra resultados
- Relación `ModelDeployment.project` o `ModelDeployment.model` es null

**Solución:**
1. Verificar en BD que existen registros en `srvdeployment` con `srv_project_id` correcto
2. Verificar que `srv_model_id` no es null
3. Revisar query JPQL en `ModelDeploymentRepository`
4. Verificar que las relaciones JPA están correctamente mapeadas

### Problema 4: BPMN Workflow No Se Dispara

**Síntoma:** Cuando hay problemas de integridad, el workflow BPMN no se ejecuta.

**Causas Posibles:**
- `BpmnWorkflowClient` no está configurado
- `bpmn.workflow.enabled = false`
- BPMN Engine no está disponible
- Error en el proceso BPMN

**Solución:**
1. Verificar configuración `bpmn.workflow.enabled = true`
2. Verificar que `BpmnWorkflowClient` está inyectado correctamente
3. Revisar logs para errores de conexión con BPMN Engine
4. Verificar que el proceso `traceability-integrity-alert-workflow` existe en BPMN Engine
5. Revisar manejo de errores en `triggerIntegrityAlertWorkflow()` (no debe fallar la exportación si BPMN falla)

---

## 📚 DEPENDENCIAS ENTRE COMPONENTES

### Diagrama de Dependencias

```
TraceabilityController (BFF)
    ↓ depende de
TraceabilityService (BFF Interface)
    ↓ implementado por
TraceabilityServiceImpl (BFF)
    ↓ llama a
TraceabilityController (Microservicio)
    ↓ llama a
TraceabilityService (Business)
    ↓ depende de
    ├── ModelRepository
    ├── ProjectRepository
    ├── AgentRepository
    ├── ImmutableLogRepository
    ├── HitlDecisionRepository
    ├── ModelDeploymentRepository
    ├── ImmutableLoggingBusinessService
    └── BpmnWorkflowClient (opcional)
```

### Orden de Inicialización

1. **Repositories** (JPA) - Se inicializan primero
2. **Business Services** - Dependen de Repositories
3. **BpmnWorkflowClient** - Se inicializa si está configurado
4. **Microservicio Controller** - Depende de Business Services
5. **BFF Service Implementation** - Depende de WebClient
6. **BFF Controller** - Depende de BFF Service

---

## 🔄 EJEMPLOS DE EVOLUCIÓN

### Ejemplo 1: Agregar Nuevo Tipo de Entidad (Dataset)

**Pasos:**

1. **Crear DTO:**
   - Crear `DatasetTraceabilityDto.java` en `codeflowx.govern.nocode.dtos`
   - Similar a `ModelTraceabilityDto` pero con campos específicos de Dataset

2. **Actualizar TraceabilityService:**
   - Agregar método `getDatasetTraceability(Long datasetId)`
   - Agregar caso "DATASET" en `exportTraceabilityEvidence()`

3. **Actualizar Controller (Microservicio):**
   - Agregar método `toDatasetTraceabilityDto()` para conversión
   - Actualizar switch en `toEvidenceDto()` para incluir "DATASET"

4. **Actualizar BFF:**
   - No requiere cambios (usa DTO genérico)

5. **Actualizar Frontend:**
   - Agregar caso "Dataset" en páginas de trazabilidad
   - Agregar mock data para Dataset

### Ejemplo 2: Agregar Filtro por Tipo de Log

**Pasos:**

1. **Actualizar DTO:**
   - Agregar campo `logType` a `TraceabilitySearchCriteriaDto`

2. **Actualizar Service:**
   - Modificar `exportTraceabilityEvidence()` para filtrar logs por tipo
   - Agregar query en `ImmutableLogRepository` si es necesario

3. **Actualizar Controller:**
   - No requiere cambios (usa DTO)

4. **Actualizar Frontend:**
   - Agregar selector de tipo de log en filtros

---

## 🔗 REFERENCIAS

### Archivos Clave

**BFF:**
- `nocode.service/codeflowx.govern.bff.compliance/src/main/java/com/codeflowx/govern/bff/compliance/controller/TraceabilityController.java`
- `nocode.service/codeflowx.govern.bff.compliance/src/main/java/com/codeflowx/govern/bff/compliance/service/impl/TraceabilityServiceImpl.java`

**Business Services:**
- `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/TraceabilityService.java`

**Microservicio:**
- `nocode.service/codeflowx-governance-traceability-service/src/main/java/com/codeflowx/govern/traceability/controller/TraceabilityController.java`

**Entities:**
- `nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/models/Model.java`
- `nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/projects/Project.java`
- `nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/agents/Agent.java`
- `nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/logging/ImmutableLog.java`
- `nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/HitlDecision.java`
- `nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/serving/ModelDeployment.java`

**Repositories:**
- `nocode.service/codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/models/ModelRepository.java`
- `nocode.service/codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/compliance/ProjectRepository.java`
- `nocode.service/codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/agents/AgentRepository.java`
- `nocode.service/codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/logging/ImmutableLogRepository.java`
- `nocode.service/codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/compliance/HitlDecisionRepository.java`
- `nocode.service/codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/serving/ModelDeploymentRepository.java`

**Scripts SQL:**
- Ver directorio: `docs/prompts/compliance/trazabilidad/sql/`

**DTOs:**
- `nocode.service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/TraceabilityEvidenceDto.java`
- `nocode.service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/ModelTraceabilityDto.java`
- `nocode.service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/ProjectTraceabilityDto.java`
- `nocode.service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/AgentTraceabilityDto.java`
- `nocode.service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/TraceabilitySearchCriteriaDto.java`

---

**Última Actualización:** Diciembre 2025
