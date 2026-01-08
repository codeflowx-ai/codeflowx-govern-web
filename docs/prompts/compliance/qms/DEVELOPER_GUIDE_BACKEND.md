# 👨‍💻 GUÍA PARA DEVELOPERS BACKEND - QMS

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
6. [Microservicio REST](#microservicio-rest)
7. [Flujos de Negocio](#flujos-de-negocio)
8. [Validaciones y Reglas](#validaciones-y-reglas)

---

## 🏗️ ARQUITECTURA GENERAL

### Capas de la Aplicación

```
Frontend (Next.js)
    ↓
API Routes (Next.js)
    ↓
Microservicio QMS (Spring WebFlux)
    ↓
Business Services (Lógica de Negocio)
    ↓
Repositories (JPA)
    ↓
Database (PostgreSQL)
```

### Componentes Principales

1. **Microservicio QMS**
   - Ubicación: `codeflowx-governance-qms-service`
   - Responsabilidad: Endpoints REST para QMS
   - Tecnología: Spring WebFlux (Reactivo)

2. **Business Services**
   - Ubicación: `codeflowx.govern.business`
   - Responsabilidad: Lógica de negocio, validaciones, reglas
   - Tecnología: Spring Boot (Transaccional)

3. **Entities & Repositories**
   - Ubicación: `nocode.service.entitys`, `codeflowx.govern.repository`
   - Responsabilidad: Persistencia de datos
   - Tecnología: JPA/Hibernate

---

## 📁 ESTRUCTURA DE CAPAS

### 1. Microservicio Layer

**Ubicación:** `nocode.service/codeflowx-governance-qms-service/`

**Componentes:**
- **Controller:** `controller/QmsController.java`
  - Expone endpoints REST para frontend
  - Maneja requests HTTP
  - Retorna DTOs optimizados
  - Manejo de excepciones centralizado

- **Exception Handler:** `exception/GlobalExceptionHandler.java`
  - Manejo centralizado de excepciones
  - Retorna `ErrorResponseDto` (nunca `Map`)
  - Conversión de excepciones a respuestas HTTP

**Ejemplo:**
```java
@RestController
@RequestMapping("/api/v1/qms")
public class QmsController {
    private final QualityManagementSystemBusinessService qmsBusinessService;

    @GetMapping
    public Mono<ResponseEntity<QmsDataDto>> getQmsData(
            @RequestParam Long projectId) {
        return Mono.fromCallable(() -> {
                    QmsDataDto data = qmsBusinessService.getQmsData(projectId);
                    return ResponseEntity.ok(data);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorResume(error -> {
                    log.error("Error getting QMS data", error);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }
}
```

### 2. Business Services Layer

**Ubicación:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/`

**Componente Principal:**
- **QualityManagementSystemBusinessService**
  - Ubicación: `QualityManagementSystemBusinessService.java`
  - Responsabilidad: Lógica de negocio completa para QMS
  - Estado: ✅ **100% IMPLEMENTADO**

**Características:**
- ✅ Sin TODOs críticos
- ✅ 13 módulos implementados con entidades JPA
- ✅ Cálculo de scores automático
- ✅ Detección de gaps automática
- ✅ Integración con BPMN

---

## 🔧 SERVICIOS DE NEGOCIO

### `QualityManagementSystemBusinessService`

**Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/QualityManagementSystemBusinessService.java`

**Responsabilidades:**
- Calcular scores QMS por módulo y overall
- Detectar gaps automáticamente
- Gestionar proyectos con QMS
- Procesar revisiones de conformidad
- Integrar con workflows BPMN

#### Métodos Principales

##### `calculateQmsComplianceScore(Long projectId)`

**Descripción:** Calcula el score overall de QMS para un proyecto.

**Proceso:**
1. Obtiene o crea entidad `QualityManagementSystem` para el proyecto
2. Evalúa cada uno de los 13 módulos (A-M)
3. Calcula score por módulo usando métodos específicos
4. Calcula score overall (promedio de 13 módulos)
5. Guarda scores en entidad QMS (JSONB)
6. Retorna score overall

**Código:**
```java
public BigDecimal calculateQmsComplianceScore(Long projectId) {
    QualityManagementSystem qms = getOrCreateQms(projectId);

    Map<String, BigDecimal> moduleScores = new HashMap<>();

    // Evaluar cada módulo
    moduleScores.put("COMPLIANCE_STRATEGY", calculateComplianceStrategyScore(projectId));
    moduleScores.put("DESIGN_CONTROL", calculateDesignControlScore(projectId));
    moduleScores.put("QUALITY_ASSURANCE", calculateQualityAssuranceScore(projectId));
    moduleScores.put("TEST_VALIDATION", calculateTestValidationScore(projectId));
    moduleScores.put("TECHNICAL_STANDARDS", calculateTechnicalStandardsScore(projectId));
    moduleScores.put("DATA_MANAGEMENT", calculateDataManagementScore(projectId));
    moduleScores.put("RISK_MANAGEMENT", calculateRiskManagementScore(projectId));
    moduleScores.put("POST_MARKET_MONITORING", calculatePostMarketMonitoringScore(projectId));
    moduleScores.put("SERIOUS_INCIDENTS", calculateSeriousIncidentsScore(projectId));
    moduleScores.put("AUTHORITY_COMMUNICATIONS", calculateAuthorityCommunicationsScore(projectId));
    moduleScores.put("DOCUMENTATION_REGISTRY", calculateDocumentationRegistryScore(projectId));
    moduleScores.put("RESOURCE_MANAGEMENT", calculateResourceManagementScore(projectId));
    moduleScores.put("ACCOUNTABILITY_FRAMEWORK", calculateAccountabilityFrameworkScore(projectId));

    // Calcular promedio
    BigDecimal sum = moduleScores.values().stream()
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    BigDecimal overallScore = sum.divide(new BigDecimal(13), 4, RoundingMode.HALF_UP);

    // Guardar en QMS
    qms.setQmsmodulescores(objectMapper.writeValueAsString(moduleScores));
    qms.setQmsoverallscore(overallScore);
    qms.setQmsupdatedat(new Timestamp(System.currentTimeMillis()));
    qmsRepository.save(qms);

    return overallScore;
}
```

##### `getQmsGaps(Long projectId)`

**Descripción:** Detecta gaps en módulos QMS (score < 0.80).

**Proceso:**
1. Obtiene entidad QMS del proyecto
2. Parsea scores de módulos desde JSONB
3. Identifica módulos con score < 0.80
4. Crea objetos `QmsGap` con información detallada
5. Retorna lista de gaps

**Código:**
```java
public List<QmsGap> getQmsGaps(Long projectId) {
    QualityManagementSystem qms = getOrCreateQms(projectId);
    Map<String, BigDecimal> moduleScores = parseModuleScores(qms.getQmsmodulescores());

    List<QmsGap> gaps = new ArrayList<>();
    BigDecimal threshold = new BigDecimal("0.80");

    for (Map.Entry<String, BigDecimal> entry : moduleScores.entrySet()) {
        if (entry.getValue().compareTo(threshold) < 0) {
            QmsGap gap = new QmsGap();
            gap.setModule(entry.getKey());
            gap.setCurrentScore(entry.getValue());
            gap.setTargetScore(threshold);
            gap.setGap(threshold.subtract(entry.getValue()));
            gap.setDescription(getGapDescription(entry.getKey()));
            gap.setSeverity(calculateSeverity(entry.getValue()));
            gap.setRecommendedActions(getRecommendedActions(entry.getKey()));
            gaps.add(gap);
        }
    }

    // Guardar gaps en QMS
    qms.setQmsgaps(objectMapper.writeValueAsString(gaps));
    qmsRepository.save(qms);

    return gaps;
}
```

##### `getProjectsWithQms(int page, int size, String status, String search)`

**Descripción:** Lista proyectos con información QMS resumida.

**Proceso:**
1. Construye consulta con filtros (status, search)
2. Usa `JOIN FETCH` para cargar datos de proyecto eficientemente
3. Pagina resultados
4. Convierte entidades a objetos `ProjectQmsSummary`
5. Retorna lista paginada

**Código:**
```java
public List<ProjectQmsSummary> getProjectsWithQms(int page, int size, String status, String search) {
    Pageable pageable = PageRequest.of(page, size);

    List<QualityManagementSystem> qmsList = qmsRepository.findWithFilters(
        status, search, pageable);

    return qmsList.stream()
        .map(this::convertToProjectSummary)
        .collect(Collectors.toList());
}

private ProjectQmsSummary convertToProjectSummary(QualityManagementSystem qms) {
    Map<String, BigDecimal> moduleScores = parseModuleScores(qms.getQmsmodulescores());
    long compliantModules = moduleScores.values().stream()
        .filter(score -> score.compareTo(new BigDecimal("0.80")) >= 0)
        .count();

    List<QmsGap> gaps = parseGaps(qms.getQmsgaps());

    return ProjectQmsSummary.builder()
        .projectId(qms.getProject().getIdxproject())
        .projectName(qms.getProject().getPrjname())
        .overallScore(qms.getQmsoverallscore())
        .complianceStatus(determineComplianceStatus(qms.getQmsoverallscore()))
        .totalModules(13)
        .compliantModules((int) compliantModules)
        .gapsCount(gaps.size())
        .lastUpdated(qms.getQmsupdatedat() != null ?
            qms.getQmsupdatedat().toLocalDateTime() :
            qms.getQmscardat().toLocalDateTime())
        .createdAt(qms.getQmscardat().toLocalDateTime())
        .build();
}
```

##### `processQmsReview(...)`

**Descripción:** Procesa revisión de gaps QMS y completa tareas BPMN.

**Proceso:**
1. Valida datos de revisión
2. Guarda decisión en QMS
3. Si hay `taskId`, completa tarea BPMN
4. Si decisión es `CORRECTIONS_REQUIRED`, lanza workflow de correcciones
5. Retorna resultado de revisión

**Código:**
```java
public QmsReviewResult processQmsReview(
        Long projectId,
        String decision,
        String reviewerName,
        String reviewNotes,
        String taskId) {

    QualityManagementSystem qms = getOrCreateQms(projectId);

    // Guardar decisión
    Map<String, Object> reviewData = new HashMap<>();
    reviewData.put("decision", decision);
    reviewData.put("reviewerName", reviewerName);
    reviewData.put("reviewNotes", reviewNotes);
    reviewData.put("reviewDate", LocalDateTime.now());
    qms.setQmscompliancestrategy(objectMapper.writeValueAsString(reviewData));
    qmsRepository.save(qms);

    // Completar tarea BPMN si está presente
    if (taskId != null && bpmnWorkflowClient != null) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("decision", decision);
        variables.put("reviewNotes", reviewNotes);
        bpmnWorkflowClient.completeTask(taskId, variables);
    }

    // Lanzar workflow de correcciones si es necesario
    if ("CORRECTIONS_REQUIRED".equals(decision) && bpmnWorkflowClient != null) {
        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put("projectId", projectId);
        bpmnWorkflowClient.startProcess("qms-corrections", processVariables);
    }

    return QmsReviewResult.builder()
        .success(true)
        .message("Review processed successfully")
        .build();
}
```

#### Métodos por Módulo

Cada módulo (A-M) tiene métodos específicos:

##### Módulo A: Compliance Strategy
```java
public QmsComplianceStrategy getComplianceStrategy(Long projectId) {
    return complianceStrategyRepository.findByProjectId(projectId)
        .orElseGet(() -> {
            QmsComplianceStrategy newStrategy = new QmsComplianceStrategy();
            newStrategy.setProject(projectRepository.findById(projectId).orElse(null));
            newStrategy.setStrategyDefined(false);
            newStrategy.setApplicableRegulations("[]");
            newStrategy.setComplianceScore(BigDecimal.ZERO);
            return complianceStrategyRepository.save(newStrategy);
        });
}

public BigDecimal calculateComplianceStrategyScore(Long projectId) {
    QmsComplianceStrategy strategy = getComplianceStrategy(projectId);
    BigDecimal score = BigDecimal.ZERO;

    if (strategy.isStrategyDefined()) {
        score = score.add(new BigDecimal("0.50"));
    }

    // Evaluar regulaciones aplicables
    List<String> regulations = parseRegulations(strategy.getApplicableRegulations());
    if (!regulations.isEmpty()) {
        score = score.add(new BigDecimal("0.30"));
    }

    if (strategy.getStrategyDescription() != null &&
        !strategy.getStrategyDescription().isEmpty()) {
        score = score.add(new BigDecimal("0.20"));
    }

    strategy.setComplianceScore(score);
    complianceStrategyRepository.save(strategy);

    return score;
}
```

**Patrón Similar:** Todos los módulos siguen el mismo patrón:
- `get[Module]()` - Obtiene o crea entidad del módulo
- `update[Module]()` - Actualiza datos del módulo
- `calculate[Module]Score()` - Calcula score del módulo

---

## 🗄️ ENTIDADES JPA

### Ubicación
`nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/`

### Entidad Principal

#### `QualityManagementSystem`

**Tabla:** `GOVQUALITYMANAGEMENTSYSTEMS`

**Campos Principales:**
- `idxqualitymanagementsystem` (PK, Long) - ID autonumérico
- `idxproject` (FK, Long) - Referencia a proyecto
- `qmsoverallscore` (BigDecimal) - Score overall (0.00 - 1.00)
- `qmsmodulescores` (String, JSONB) - Scores por módulo (JSON)
- `qmsgaps` (String, JSONB) - Gaps detectados (JSON)
- `qmscompliancestrategy` (String) - Estrategia o datos de revisión
- `qmscardat` (Timestamp) - Fecha creación
- `qmsupdatedat` (Timestamp) - Fecha actualización
- `iduuid` (String) - UUID único

**Relaciones:**
- `@ManyToOne` con `Project`

### Entidades por Módulo

#### Módulo A: `QmsComplianceStrategy`
- Tabla: `GOVQMSCOMPLIANCESTRATEGY`
- Campos: `strategyDefined`, `applicableRegulations` (JSON), `complianceScore`

#### Módulo B: `QmsDesignReview`
- Tabla: `GOVQMSDESIGNREVIEWS`
- Campos: `reviewType`, `reviewDate`, `reviewerName`, `reviewStatus`, `reviewScore`

#### Módulo C: `QmsQualityMetrics`
- Tabla: `GOVQMSQUALITYMETRICS`
- Campos: `qualityScore`, `metricsData` (JSON), `lastUpdated`

#### Módulo D: `QmsTestExecution`
- Tabla: `GOVQMSTESTEXECUTIONS`
- Campos: `testType`, `testDate`, `testResult`, `testScore`

#### Módulo E: `QmsProjectStandard`
- Tabla: `GOVQMSPROJECTSTANDARDS`
- Campos: `standardName`, `standardVersion`, `complianceStatus`, `complianceScore`

#### Módulo F: `QmsDataManagement`
- Tabla: `GOVQMSDATAMANAGEMENT`
- Campos: `dataGovernanceDefined`, `dataQualityScore`, `datasets` (JSON)

#### Módulo G: `QmsRiskRegister`
- Tabla: `GOVQMSRISKREGISTER`
- Campos: `riskDescription`, `riskSeverity`, `riskLikelihood`, `riskMitigationStatus`, `overallRiskScore`

#### Módulo H: `PostMarketMonitoring`
- Tabla: `GOVPOSTMARKETMONITORING` (existente)
- Integración con módulo PMM

#### Módulo I: `QmsSeriousIncident`
- Tabla: `GOVQMSSERIOUSINCIDENTS`
- Campos: `incidentType`, `incidentDescription`, `incidentSeverity`, `authorityNotified`, `notificationDate`

#### Módulo J: `QmsAuthorityCommunication`
- Tabla: `GOVQMSAUTHORITYCOMMUNICATIONS`
- Campos: `communicationType`, `communicationDate`, `authorityName`, `communicationStatus`

#### Módulo K: `QmsTechnicalDocument`
- Tabla: `GOVQMSTECHNICALDOCUMENTS`
- Campos: `documentName`, `documentType`, `documentStatus`, `documentPath`

#### Módulo L: `QmsResourceManagement`
- Tabla: `GOVQMSRESOURCEMANAGEMENT`
- Campos: `resourceType`, `resourceCount`, `resourceAdequacy`, `resourceScore`

#### Módulo M: `QmsAccountabilityAssignment`
- Tabla: `GOVQMSACCOUNTABILITYASSIGNMENTS`
- Campos: `responsibilityArea`, `assignedTo`, `assignmentDate`, `assignmentStatus`

---

## 📚 REPOSITORIOS

### Ubicación
`codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/compliance/`

### Repositorio Principal

#### `QualityManagementSystemRepository`

**Métodos Personalizados:**
```java
@Repository
public interface QualityManagementSystemRepository extends JpaRepository<QualityManagementSystem, Long> {

    Optional<QualityManagementSystem> findByProjectIdxproject(Long projectId);

    @Query("SELECT qms FROM QualityManagementSystem qms " +
           "JOIN FETCH qms.project p " +
           "WHERE (:status IS NULL OR " +
           "  CASE " +
           "    WHEN qms.qmsoverallscore >= 0.85 THEN 'COMPLIANT' " +
           "    WHEN qms.qmsoverallscore >= 0.70 THEN 'PARTIAL' " +
           "    ELSE 'NON_COMPLIANT' " +
           "  END = :status) " +
           "AND (:search IS NULL OR p.prjname LIKE %:search%) " +
           "ORDER BY qms.qmsupdatedat DESC")
    List<QualityManagementSystem> findWithFilters(
        @Param("status") String status,
        @Param("search") String search,
        Pageable pageable);

    @Query("SELECT COUNT(DISTINCT qms.project.idxproject) FROM QualityManagementSystem qms")
    Long countDistinctProjects();

    @Query("SELECT COUNT(DISTINCT qms.project.idxproject) FROM QualityManagementSystem qms " +
           "WHERE qms.qmsoverallscore >= 0.85")
    Long countCompliant();

    @Query("SELECT AVG(qms.qmsoverallscore) FROM QualityManagementSystem qms")
    BigDecimal calculateAverageScore();
}
```

### Repositorios por Módulo

Cada módulo tiene su repositorio con métodos personalizados:

```java
@Repository
public interface QmsComplianceStrategyRepository extends JpaRepository<QmsComplianceStrategy, Long> {
    Optional<QmsComplianceStrategy> findByProjectIdxproject(Long projectId);
}

@Repository
public interface QmsRiskRegisterRepository extends JpaRepository<QmsRiskRegister, Long> {
    List<QmsRiskRegister> findByProjectIdxproject(Long projectId);
    Long countByProjectIdxproject(Long projectId);
    Long countMitigatedByProjectIdxproject(Long projectId);
}
```

---

## 🔌 MICROSERVICIO REST

### Ubicación
`nocode.service/codeflowx-governance-qms-service/src/main/java/com/codeflowx/govern/qms/controller/QmsController.java`

### Endpoints Principales

#### `GET /api/v1/qms?projectId={id}`
Obtiene datos completos QMS para un proyecto.

**Response:**
```java
@GetMapping
public Mono<ResponseEntity<QmsDataDto>> getQmsData(@RequestParam Long projectId) {
    return Mono.fromCallable(() -> {
                QmsDataDto data = qmsBusinessService.getQmsData(projectId);
                return ResponseEntity.ok(data);
            })
            .subscribeOn(Schedulers.boundedElastic());
}
```

#### `POST /api/v1/qms/calculate`
Calcula scores QMS para un proyecto.

**Request:**
```json
{
  "projectId": 1,
  "action": "calculate"
}
```

#### `GET /api/v1/qms/gaps?projectId={id}`
Obtiene gaps detectados para un proyecto.

#### `POST /api/v1/qms/review`
Procesa revisión de gaps QMS.

**Request:**
```json
{
  "projectId": 1,
  "decision": "APPROVED",
  "reviewerName": "John Doe",
  "reviewNotes": "All gaps reviewed and approved",
  "taskId": "task-123"
}
```

#### `GET /api/v1/qms/projects`
Lista proyectos con información QMS.

**Query Parameters:**
- `page` (default: 0)
- `size` (default: 20)
- `status` (opcional: COMPLIANT, PARTIAL, NON_COMPLIANT)
- `search` (opcional: búsqueda por nombre)

### Endpoints por Módulo

Cada módulo tiene endpoints específicos:

- `GET /api/v1/qms/compliance-strategy?projectId={id}` - Módulo A
- `GET /api/v1/qms/risk-management?projectId={id}` - Módulo G
- `POST /api/v1/qms/risks` - Módulo G (registrar riesgo)
- `GET /api/v1/qms/serious-incidents?projectId={id}` - Módulo I
- `POST /api/v1/qms/serious-incidents` - Módulo I (registrar incidente)
- ... (ver documentación completa de endpoints)

---

## 🔄 FLUJOS DE NEGOCIO

### Flujo 1: Cálculo de Scores QMS

```
1. Frontend llama POST /api/v1/qms/calculate
2. Controller llama qmsBusinessService.calculateQmsComplianceScore(projectId)
3. Business Service:
   - Obtiene/crea entidad QMS
   - Evalúa cada módulo (A-M)
   - Calcula score por módulo
   - Calcula score overall (promedio)
   - Guarda en BD
4. Retorna score overall
```

### Flujo 2: Detección de Gaps

```
1. Frontend llama GET /api/v1/qms/gaps?projectId={id}
2. Controller llama qmsBusinessService.getQmsGaps(projectId)
3. Business Service:
   - Obtiene scores de módulos
   - Identifica módulos con score < 0.80
   - Crea objetos QmsGap
   - Guarda gaps en BD (JSONB)
4. Retorna lista de gaps
```

### Flujo 3: Revisión de Conformidad

```
1. Frontend llama POST /api/v1/qms/review
2. Controller llama qmsBusinessService.processQmsReview(...)
3. Business Service:
   - Guarda decisión en QMS
   - Si hay taskId, completa tarea BPMN
   - Si decisión es CORRECTIONS_REQUIRED, lanza workflow
4. Retorna resultado
```

---

## ✅ VALIDACIONES Y REGLAS

### Reglas de Negocio

1. **Score Overall:**
   - Calculado como promedio de 13 módulos
   - Rango: 0.00 - 1.00
   - Threshold de compliance: 0.80

2. **Gaps:**
   - Detectados automáticamente si score módulo < 0.80
   - Severidad calculada según gap:
     - HIGH: gap >= 0.20
     - MEDIUM: gap >= 0.10
     - LOW: gap < 0.10

3. **Estados de Compliance:**
   - COMPLIANT: score >= 0.85
   - PARTIAL: score >= 0.70 y < 0.85
   - NON_COMPLIANT: score < 0.70

4. **Revisión:**
   - Notas de revisión son obligatorias
   - Solo se puede aprobar si hay notas
   - Tarea BPMN se completa si taskId está presente

---

## 🔧 EVOLUCIONES Y CORRECCIONES

### Cómo Agregar un Nuevo Módulo QMS

Si necesitas agregar un nuevo módulo (ej: Módulo N), sigue estos pasos:

#### 1. Crear Entidad JPA

**Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/QmsNewModule.java`

**Ejemplo:**
```java
@Entity
@Table(name = "GOVQMSNEWMODULE")
public class QmsNewModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idxqmsnewmodule")
    private Long idxqmsnewmodule;

    @Column(name = "iduuid", length = 36)
    private String iduuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idxproject")
    private Project project;

    @Column(name = "qmsnewmodulefield1")
    private String field1;

    @Column(name = "qmsnewmodulescore")
    private BigDecimal score;

    @Column(name = "qmsnewmodulecardat")
    private Timestamp cardat;

    @Column(name = "qmsnewmoduleupdatedat")
    private Timestamp updatedat;

    // Getters y Setters
}
```

**Reglas:**
- Prefijo de tabla: `GOVQMS` + nombre del módulo en mayúsculas
- PK autonumérica: `idxqmsnewmodule`
- FK a proyecto: `idxproject`
- Campo UUID: `iduuid`
- Campos de auditoría: `cardat`, `updatedat`

#### 2. Crear Repositorio

**Ubicación:** `codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/compliance/QmsNewModuleRepository.java`

**Ejemplo:**
```java
@Repository
public interface QmsNewModuleRepository extends JpaRepository<QmsNewModule, Long> {
    Optional<QmsNewModule> findByProjectIdxproject(Long projectId);
    Long countByProjectIdxproject(Long projectId);
}
```

#### 3. Agregar Métodos al Business Service

**Ubicación:** `QualityManagementSystemBusinessService.java`

**Pasos:**
1. Inyectar el repositorio:
```java
@Autowired
private QmsNewModuleRepository newModuleRepository;
```

2. Agregar método `getNewModule()`:
```java
public QmsNewModule getNewModule(Long projectId) {
    log.info("Getting new module for project: {}", projectId);
    Optional<QmsNewModule> entityOpt = newModuleRepository.findByProjectIdxproject(projectId);
    QmsNewModule entity = entityOpt.orElse(new QmsNewModule());
    entity.setProject(projectRepository.findById(projectId).orElse(null));
    if (entity.getIduuid() == null) {
        entity.setIduuid(UUID.randomUUID().toString());
        entity.setCardat(new Timestamp(System.currentTimeMillis()));
    }
    entity.setUpdatedat(new Timestamp(System.currentTimeMillis()));
    newModuleRepository.save(entity);
    return entity;
}
```

3. Agregar método `calculateNewModuleScore()`:
```java
public BigDecimal calculateNewModuleScore(Long projectId) {
    QmsNewModule module = getNewModule(projectId);
    BigDecimal score = BigDecimal.ZERO;

    // Lógica de cálculo de score
    if (module.getField1() != null && !module.getField1().isEmpty()) {
        score = score.add(new BigDecimal("0.50"));
    }

    // Más validaciones...

    module.setScore(score);
    newModuleRepository.save(module);
    return score;
}
```

4. Actualizar `calculateQmsComplianceScore()`:
```java
// En calculateQmsComplianceScore(), agregar:
moduleScores.put("NEW_MODULE", calculateNewModuleScore(projectId));

// Y actualizar el divisor de 13 a 14:
BigDecimal overallScore = sum.divide(new BigDecimal(14), 4, RoundingMode.HALF_UP);
```

#### 4. Crear DTO

**Ubicación:** `codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/QmsNewModuleDto.java`

**Ejemplo:**
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QmsNewModuleDto {
    private Long projectId;
    private String field1;
    private BigDecimal score;
}
```

#### 5. Agregar Endpoints REST

**Ubicación:** `QmsController.java`

**Ejemplo:**
```java
@GetMapping("/new-module")
@Operation(summary = "Obtener nuevo módulo", description = "Retorna datos del nuevo módulo QMS")
public Mono<ResponseEntity<QmsNewModuleDto>> getNewModule(@RequestParam Long projectId) {
    return Mono.fromCallable(() -> {
                QualityManagementSystemBusinessService.QmsNewModule module =
                    qmsBusinessService.getNewModule(projectId);
                return ResponseEntity.ok(convertToNewModuleDto(module));
            })
            .subscribeOn(Schedulers.boundedElastic())
            .onErrorResume(error -> {
                log.error("Error getting new module", error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
}
```

#### 6. Actualizar Traducciones

**Ubicación:** `app/config/i18n/modules/governance/compliance.ts`

Agregar traducciones para el nuevo módulo en todos los idiomas:
```typescript
qms: {
  moduleNames: {
    NEW_MODULE: {
      es: "Nuevo Módulo",
      en: "New Module",
      fr: "Nouveau Module",
      // ...
    }
  }
}
```

### Cómo Corregir un Bug Común

#### Bug: Score no se actualiza después de modificar datos

**Causa:** El score se calcula solo cuando se llama explícitamente a `calculateQmsComplianceScore()`.

**Solución:**
1. Verificar que el método de actualización llame al cálculo:
```java
public void updateNewModule(Long projectId, QmsNewModuleDto dto) {
    QmsNewModule module = getNewModule(projectId);
    // Actualizar campos...
    newModuleRepository.save(module);

    // Recalcular score del módulo
    calculateNewModuleScore(projectId);

    // Recalcular score overall
    calculateQmsComplianceScore(projectId);
}
```

2. O usar `@Transactional` y eventos JPA para recalcular automáticamente.

#### Bug: Gaps no se detectan correctamente

**Causa:** El threshold de gaps está hardcodeado o el método `getQmsGaps()` no se llama después de calcular scores.

**Solución:**
1. Verificar threshold en `getQmsGaps()`:
```java
BigDecimal threshold = new BigDecimal("0.80"); // Debe ser 0.80
```

2. Llamar `getQmsGaps()` después de `calculateQmsComplianceScore()`:
```java
public QmsData getQmsData(Long projectId) {
    // Calcular scores
    calculateQmsComplianceScore(projectId);

    // Detectar gaps
    List<QmsGap> gaps = getQmsGaps(projectId);

    // Retornar datos
    // ...
}
```

### Troubleshooting

#### Problema: Error "Entity not found" al obtener datos QMS

**Diagnóstico:**
1. Verificar que el proyecto existe en BD
2. Verificar que existe entidad `QualityManagementSystem` para el proyecto
3. Revisar logs de `getOrCreateQms()`

**Solución:**
```java
// En getOrCreateQms(), agregar validación:
public QualityManagementSystem getOrCreateQms(Long projectId) {
    if (!projectRepository.existsById(projectId)) {
        throw new IllegalArgumentException("Project not found: " + projectId);
    }
    // Resto del código...
}
```

#### Problema: BPMN task no se completa

**Diagnóstico:**
1. Verificar que `bpmnWorkflowClient` está habilitado
2. Verificar que `taskId` no es null
3. Revisar logs de `BpmnWorkflowClient.completeTask()`

**Solución:**
```java
// En processQmsReview(), agregar validación:
if (taskId != null && !taskId.isEmpty()) {
    if (bpmnWorkflowClient == null) {
        log.warn("BPMN client not available, task will not be completed");
    } else if (!bpmnWorkflowClient.isEnabled()) {
        log.warn("BPMN service is disabled, task will not be completed");
    } else {
        bpmnWorkflowClient.completeTask(taskId, variables);
    }
}
```

#### Problema: Scores calculados incorrectamente

**Diagnóstico:**
1. Verificar lógica de cálculo en `calculate[Module]Score()`
2. Verificar que todos los módulos se evalúan
3. Verificar que el promedio se calcula correctamente (divisor = número de módulos)

**Solución:**
```java
// Usar constante para número de módulos:
private static final int TOTAL_MODULES = 13; // Actualizar si agregas módulos

BigDecimal overallScore = sum.divide(
    new BigDecimal(TOTAL_MODULES),
    4,
    RoundingMode.HALF_UP
);
```

### Testing

#### Unit Tests para Business Service

**Ubicación:** `codeflowx.govern.business/src/test/java/com/codeflowx/govern/business/compliance/QualityManagementSystemBusinessServiceTest.java`

**Ejemplo:**
```java
@SpringBootTest
@Transactional
class QualityManagementSystemBusinessServiceTest {

    @Autowired
    private QualityManagementSystemBusinessService qmsService;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void testCalculateQmsComplianceScore() {
        // Arrange
        Project project = createTestProject();
        Long projectId = project.getIdxproject();

        // Act
        BigDecimal score = qmsService.calculateQmsComplianceScore(projectId);

        // Assert
        assertThat(score).isNotNull();
        assertThat(score).isBetween(BigDecimal.ZERO, BigDecimal.ONE);
    }

    @Test
    void testGetQmsGaps() {
        // Arrange
        Project project = createTestProject();
        Long projectId = project.getIdxproject();
        qmsService.calculateQmsComplianceScore(projectId);

        // Act
        List<QmsGap> gaps = qmsService.getQmsGaps(projectId);

        // Assert
        assertThat(gaps).isNotNull();
        // Verificar que gaps tienen score < 0.80
        gaps.forEach(gap -> {
            assertThat(gap.getCurrentScore()).isLessThan(new BigDecimal("0.80"));
        });
    }
}
```

#### Integration Tests para Controller

**Ubicación:** `codeflowx-governance-qms-service/src/test/java/com/codeflowx/govern/qms/controller/QmsControllerTest.java`

**Ejemplo:**
```java
@SpringBootTest
@AutoConfigureWebTestClient
class QmsControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testGetQmsData() {
        webTestClient
            .get()
            .uri("/api/v1/qms?projectId=1")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.projectId").exists()
            .jsonPath("$.overallScore").exists();
    }
}
```

### Migraciones de Base de Datos

#### Crear Tabla para Nuevo Módulo

**Ubicación:** `nocode.service.entitys/src/main/resources/db/migration/`

**Ejemplo:** `V20251201__Create_QmsNewModule_table.sql`

```sql
CREATE TABLE GOVQMSNEWMODULE (
    idxqmsnewmodule BIGSERIAL PRIMARY KEY,
    iduuid VARCHAR(36) NOT NULL UNIQUE,
    idxproject BIGINT NOT NULL,
    qmsnewmodulefield1 VARCHAR(500),
    qmsnewmodulescore DECIMAL(5,4),
    qmsnewmodulecardat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    qmsnewmoduleupdatedat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_qmsnewmodule_project
        FOREIGN KEY (idxproject) REFERENCES PROJECTS(idxproject)
);

CREATE INDEX idx_qmsnewmodule_project ON GOVQMSNEWMODULE(idxproject);
CREATE INDEX idx_qmsnewmodule_uuid ON GOVQMSNEWMODULE(iduuid);
```

### Configuración del Entorno

#### Variables de Entorno Requeridas

```properties
# Base de datos
spring.datasource.url=jdbc:postgresql://localhost:5432/codeflowx
spring.datasource.username=codeflowx
spring.datasource.password=***

# BPMN Service
bpmn.service.enabled=true
bpmn.service.url=http://localhost:8080/bpmn

# Logging
logging.level.com.codeflowx.govern=DEBUG
```

#### Configuración de Circuit Breaker

**Ubicación:** `application.yml`

```yaml
resilience4j:
  circuitbreaker:
    instances:
      qmsService:
        registerHealthIndicator: true
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        failureRateThreshold: 50
        waitDurationInOpenState: 10s
```

---

## 📚 REFERENCIAS

- **Estado de Implementación:** `docs/prompts/compliance/qms/ESTADO_IMPLEMENTACION_QMS.md`
- **Endpoints REST:** `docs/prompts/compliance/QMS_ENDPOINTS_F_M.md`
- **Arquitectura Frontend:** `docs/ARQUITECTURA_FRONTEND.md`

---

**Última actualización:** Diciembre 2025
**Versión:** 1.0.0
