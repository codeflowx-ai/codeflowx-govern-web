# 👨‍💻 GUÍA PARA DEVELOPERS BACKEND - FRIA

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Desarrolladores Backend (Java/Spring Boot)

---

## 📋 ÍNDICE

1. [Arquitectura General](#arquitectura-general)
2. [Estructura de Archivos](#estructura-de-archivos)
3. [Estructura de Capas](#estructura-de-capas)
4. [Servicios de Negocio](#servicios-de-negocio)
5. [Entidades JPA](#entidades-jpa)
6. [Repositorios](#repositorios)
7. [BFF (Backend for Frontend)](#bff-backend-for-frontend)
8. [Microservicio de Negocio](#microservicio-de-negocio)
9. [Flujos de Negocio](#flujos-de-negocio)
10. [Validaciones y Reglas](#validaciones-y-reglas)
11. [Integraciones Python](#integraciones-python)
12. [Configuración](#configuración)
13. [Dependencias](#dependencias)
14. [Migraciones de Base de Datos](#migraciones-de-base-de-datos)
15. [Testing](#testing)
16. [Troubleshooting](#troubleshooting)
17. [Performance y Optimización](#performance-y-optimización)
18. [Seguridad](#seguridad)

---

## 🏗️ ARQUITECTURA GENERAL

### Capas de la Aplicación

```
Frontend (Next.js)
    ↓
BFF (Backend for Frontend)
    ↓
Business Microservice (FRIA Service)
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
   - Ubicación: `codeflowx-governance-fria-service`
   - Responsabilidad: Endpoints REST para FRIA
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

## 📁 ESTRUCTURA DE ARCHIVOS

### Estructura Completa del Proyecto

```
nocode.service/
├── codeflowx.govern.bff.compliance/          # BFF Layer
│   ├── src/main/java/com/codeflowx/govern/bff/compliance/
│   │   ├── controller/
│   │   │   └── FriaController.java          # Endpoints REST BFF
│   │   ├── service/
│   │   │   ├── FriaService.java              # Interface
│   │   │   └── impl/
│   │   │       └── FriaServiceImpl.java      # Implementación reactiva
│   │   ├── config/
│   │   │   └── ResilienceConfig.java         # Configuración Resilience4j
│   │   └── metrics/
│   │       └── ComplianceMetrics.java       # Métricas Micrometer
│   └── src/main/resources/
│       ├── application.yml                   # Configuración principal
│       └── application-prod.yml              # Configuración producción
│
├── codeflowx-governance-fria-service/        # Microservicio FRIA
│   ├── src/main/java/com/codeflowx/govern/fria/
│   │   ├── controller/
│   │   │   └── FriaController.java          # Endpoints REST microservicio
│   │   ├── FriaServiceApplication.java       # Clase principal
│   └── src/main/resources/
│       └── application.yml                   # Configuración microservicio
│
├── codeflowx.govern.business/                # Business Services
│   └── src/main/java/com/codeflowx/govern/business/compliance/
│       └── FriaAssessmentBusinessService.java # Lógica de negocio
│
├── codeflowx.govern.repository/              # Repositorios
│   └── src/main/java/com/codeflowx/govern/repository/compliance/
│       └── FriaAssessmentRepository.java     # Repositorio JPA
│
├── nocode.service.entitys/                    # Entidades JPA
│   └── src/main/java/com/codeflowx/govern/entity/compliance/
│       └── FriaAssessment.java               # Entidad principal
│
└── codeflowx.govern.nocode.dtos/              # DTOs
    └── src/main/java/com/codeflowx/govern/nocode/dtos/compliance/
        ├── FriaAssessmentDto.java
        ├── FriaCreateRequestDto.java
        ├── FriaStepUpdateRequestDto.java
        ├── FriaCalculateRiskResponseDto.java
        ├── FriaCrossValidateResponseDto.java
        ├── FriaNotifyAuthorityResponseDto.java
        ├── FriaProjectsListResponseDto.java
        ├── ProjectWithFriasDto.java
        └── FriaAssessmentSummaryDto.java
```

### Convenciones de Nombres

**Archivos Java:**
- Controllers: `*Controller.java`
- Services: `*Service.java` (interface), `*ServiceImpl.java` (implementación)
- Repositories: `*Repository.java`
- Entities: `*.java` (nombre de entidad)
- DTOs: `*Dto.java`, `*RequestDto.java`, `*ResponseDto.java`
- Config: `*Config.java`

**Paquetes:**
- `controller` - Controladores REST
- `service` - Interfaces de servicio
- `service.impl` - Implementaciones de servicio
- `repository` - Repositorios JPA
- `entity` - Entidades JPA
- `dto` - Data Transfer Objects
- `config` - Configuraciones

---

## 📁 ESTRUCTURA DE CAPAS

### 1. BFF Layer

**Ubicación:** `nocode.service/codeflowx.govern.bff.compliance/`

**Componentes:**
- **Controller:** `controller/FriaController.java`
  - Expone endpoints REST para frontend
  - Maneja requests HTTP
  - Retorna DTOs optimizados

- **Service:** `service/FriaService.java` (interface)
  - Define contratos de servicio

- **Service Implementation:** `service/impl/FriaServiceImpl.java`
  - Implementa llamadas a microservicio de negocio
  - Usa WebClient para comunicación reactiva
  - Implementa Circuit Breaker y Retry (Resilience4j)

**Ejemplo:**
```java
@RestController
@RequestMapping("/api/v1/fria")
public class FriaController {
    private final FriaService friaService;

    @GetMapping("/projects")
    public Mono<ResponseEntity<FriaProjectsListResponseDto>> listProjectsWithFrias(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status) {
        return friaService.listProjectsWithFrias(page, size, search, status)
                .map(ResponseEntity::ok);
    }
}
```

---

### 2. Business Microservice Layer

**Ubicación:** `nocode.service/codeflowx-governance-fria-service/`

**Componentes:**
- **Controller:** `controller/FriaController.java`
  - Endpoints REST del microservicio
  - Llama a Business Services
  - Convierte Entities a DTOs

**Ejemplo:**
```java
@RestController
@RequestMapping("/api/v1/fria")
public class FriaController {
    private final FriaAssessmentBusinessService businessService;

    @PostMapping("/create")
    public Mono<ResponseEntity<FriaAssessmentDto>> createFria(
            @Valid @RequestBody FriaCreateRequestDto request) {
        FriaAssessment fria = businessService.createFria(
            request.getProjectId(),
            request.getUserId()
        );
        return Mono.just(ResponseEntity.ok(toDto(fria)));
    }
}
```

---

### 3. Business Services Layer

**Ubicación:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/`

**Servicios Principales:**

#### `FriaAssessmentBusinessService`

**Responsabilidades:**
- Crear evaluaciones FRIA
- Actualizar pasos del wizard (6 pasos según Art. 27.1)
- Calcular score de completitud
- Calcular riesgo final según Anexo IX
- Validación cruzada con métricas técnicas (INC-007)
- Notificación a autoridades (Art. 27.3)
- Gestión de versiones

**Métodos Principales:**
```java
@Service
@Slf4j
public class FriaAssessmentBusinessService {

    @Autowired
    private FriaAssessmentRepository repository;

    @Autowired
    private AIGovernanceClient aiGovernanceClient;

    /**
     * Crea nueva FRIA (estado DRAFT)
     */
    @Transactional
    public FriaAssessment createFria(Long projectId, Long deployerUserId) {
        log.info("Creating FRIA for project: {}, deployer: {}", projectId, deployerUserId);

        FriaAssessment fria = new FriaAssessment();
        fria.setIdxproject(projectId);
        fria.setIdxuser(deployerUserId);
        fria.setFriaart27compliant(false);
        fria.setFriaapproved(false);
        fria.setFrianotified(false);
        fria.setFriacreatedat(new Timestamp(System.currentTimeMillis()));
        fria.setIduuid(UUID.randomUUID().toString());

        return repository.save(fria);
    }

    /**
     * Actualiza sección específica de FRIA (paso del wizard)
     */
    @Transactional
    public void updateFriaSection(Long friaId, String section, Object data) {
        log.info("Updating FRIA section: {} for ID: {}", section, friaId);

        FriaAssessment fria = repository.findById(friaId)
            .orElseThrow(() -> new IllegalArgumentException("FRIA not found: " + friaId));

        switch (section) {
            case "PROCESS_DESCRIPTION": // Art. 27.1.a
                fria.setFriaprocessdescription((String) data);
                break;
            case "USAGE_PERIOD": // Art. 27.1.b
                fria.setFriausageperiod((String) data);
                break;
            case "USAGE_FREQUENCY": // Art. 27.1.b
                fria.setFriausagefrequency((String) data);
                break;
            case "AFFECTED_CATEGORIES": // Art. 27.1.c
                fria.setFriaaffectedcategories((String) data);
                break;
            case "RISKS": // Art. 27.1.d
                fria.setFriarisks((String) data);
                break;
            case "HUMAN_OVERSIGHT": // Art. 27.1.e
                fria.setFriahumanoversight((String) data);
                break;
            case "MITIGATION_MEASURES": // Art. 27.1.f
                fria.setFriamitigationmeasures((String) data);
                break;
        }

        fria.setFriaupdatedat(new Timestamp(System.currentTimeMillis()));
        repository.save(fria);
    }

    /**
     * Calcula completeness score (0.00 - 1.00)
     * Verifica que los 6 elementos mandatorios Art. 27.1 estén completos
     */
    @Transactional
    public BigDecimal calculateCompletenessScore(Long friaId) {
        FriaAssessment fria = repository.findById(friaId)
            .orElseThrow(() -> new IllegalArgumentException("FRIA not found: " + friaId));

        int completedElements = 0;
        int totalElements = 6; // Art. 27.1 tiene 6 elementos mandatorios

        // Art. 27.1.a - Process description
        if (fria.getFriaprocessdescription() != null &&
            !fria.getFriaprocessdescription().isEmpty()) {
            completedElements++;
        }

        // Art. 27.1.b - Usage period/frequency
        if (fria.getFriausageperiod() != null &&
            fria.getFriausagefrequency() != null) {
            completedElements++;
        }

        // Art. 27.1.c - Affected categories
        if (fria.getFriaaffectedcategories() != null) {
            completedElements++;
        }

        // Art. 27.1.d - Risks
        if (fria.getFriarisks() != null) {
            completedElements++;
        }

        // Art. 27.1.e - Human oversight
        if (fria.getFriahumanoversight() != null &&
            !fria.getFriahumanoversight().isEmpty()) {
            completedElements++;
        }

        // Art. 27.1.f - Mitigation measures
        if (fria.getFriamitigationmeasures() != null &&
            !fria.getFriamitigationmeasures().trim().isEmpty()) {
            completedElements++;
        }

        BigDecimal score = BigDecimal.valueOf(completedElements)
            .divide(BigDecimal.valueOf(totalElements), 2, RoundingMode.HALF_UP);

        fria.setFriacompletenessscore(score);
        repository.save(fria);

        return score;
    }

    /**
     * Calcula riesgo final según Anexo IX del EU AI Act
     * Fórmula: Risk = (Severity × Probability × Impact) × (1 - Mitigation Effectiveness)
     */
    @Transactional
    public BigDecimal calculateFinalRisk(Long friaId) {
        FriaAssessment fria = repository.findById(friaId)
            .orElseThrow(() -> new IllegalArgumentException("FRIA not found: " + friaId));

        // Parsear riesgos y medidas desde JSON
        List<Map<String, Object>> risks = parseRisks(fria.getFriarisks());
        List<Map<String, Object>> measures = parseMeasures(fria.getFriamitigationmeasures());

        BigDecimal totalRisk = BigDecimal.ZERO;

        for (Map<String, Object> risk : risks) {
            BigDecimal severity = mapSeverityToValue((String) risk.get("severity"));
            BigDecimal probability = (BigDecimal) risk.get("probability");
            BigDecimal impact = mapImpactToValue((String) risk.get("impact"));

            // Calcular riesgo base
            BigDecimal baseRisk = severity.multiply(probability).multiply(impact);

            // Aplicar medidas de mitigación asociadas
            BigDecimal mitigationEffectiveness = calculateMitigationForRisk(
                risk, measures
            );

            BigDecimal riskAfterMitigation = baseRisk.multiply(
                BigDecimal.ONE.subtract(mitigationEffectiveness)
            );

            totalRisk = totalRisk.add(riskAfterMitigation);
        }

        // Normalizar a rango 0.0 - 1.0
        BigDecimal finalRisk = totalRisk.divide(
            BigDecimal.valueOf(risks.size()),
            2,
            RoundingMode.HALF_UP
        );

        fria.setFriafinalrisk(finalRisk);
        fria.setFriarisklevel(determineRiskLevel(finalRisk));
        repository.save(fria);

        return finalRisk;
    }

    /**
     * Validación cruzada con métricas técnicas (INC-007)
     * Compara FRIA documental con métricas técnicas reales
     */
    @CircuitBreaker(name = "friaGenerator", fallbackMethod = "crossValidateFallback")
    @Retry(name = "friaGenerator")
    @TimeLimiter(name = "friaGenerator")
    public CrossValidationResult crossValidate(Long friaId) {
        FriaAssessment fria = repository.findById(friaId)
            .orElseThrow(() -> new IllegalArgumentException("FRIA not found: " + friaId));

        // Preparar datos para validación
        FriaDataDTO friaData = buildFriaDataDTO(fria);

        // Llamar a microservicio Python leka-fria-generator
        CrossValidationResult result = aiGovernanceClient
            .friaGenerator()
            .crossValidate(friaData);

        // Guardar resultado
        fria.setFriacrossvalidationscore(result.getConsistencyScore());
        fria.setFriacrossvalidationresult(objectMapper.writeValueAsString(result));
        repository.save(fria);

        return result;
    }

    /**
     * Notifica a autoridades según Art. 27.3
     * Solo si riesgo final >= 0.75
     */
    @CircuitBreaker(name = "governanceApi", fallbackMethod = "notifyAuthorityFallback")
    @Retry(name = "governanceApi")
    @TimeLimiter(name = "governanceApi")
    public FriaNotifyAuthorityResponse notifyAuthority(Long friaId) {
        FriaAssessment fria = repository.findById(friaId)
            .orElseThrow(() -> new IllegalArgumentException("FRIA not found: " + friaId));

        // Validar que FRIA esté completa
        if (fria.getFriacompletenessscore() == null ||
            fria.getFriacompletenessscore().compareTo(new BigDecimal("0.90")) < 0) {
            throw new IllegalStateException("FRIA must be complete before notifying");
        }

        // Validar que riesgo >= 0.75
        if (fria.getFriafinalrisk() == null ||
            fria.getFriafinalrisk().compareTo(new BigDecimal("0.75")) < 0) {
            throw new IllegalStateException("Final risk must be >= 0.75 to notify authorities");
        }

        // Preparar request
        FriaNotifyAuthorityRequest request = FriaNotifyAuthorityRequest.builder()
            .friaId(friaId)
            .notificationDetails(buildNotificationDetails(fria))
            .authorityName("EU AI Act Authority")
            .build();

        // Llamar a microservicio Python codeflowx-governance-api
        FriaNotifyAuthorityResponse response = aiGovernanceClient
            .governanceApi()
            .notifyAuthority(friaId, request);

        // Actualizar FRIA
        fria.setFrianotified(true);
        fria.setFrianotificationdate(new Timestamp(System.currentTimeMillis()));
        fria.setFrianotificationid(response.getNotificationId());
        repository.save(fria);

        return response;
    }
}
```

---

## 🗄️ ENTIDADES JPA

### `FriaAssessment`

**Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/FriaAssessment.java`

**Campos Principales:**

```java
@Entity
@Table(name = "fria_assessment")
public class FriaAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx_fria_assessment")
    private Long idxfriaassessment;

    @ManyToOne
    @JoinColumn(name = "idx_project")
    private Project project;

    @Column(name = "idx_user")
    private Long idxuser;

    @Column(name = "id_uuid")
    private String iduuid;

    // Art. 27.1.a - Process description
    @Column(name = "fria_process_description", columnDefinition = "TEXT")
    private String friaprocessdescription;

    // Art. 27.1.b - Usage period and frequency
    @Column(name = "fria_usage_period")
    private String friausageperiod;

    @Column(name = "fria_usage_frequency")
    private String friausagefrequency;

    // Art. 27.1.c - Affected categories
    @Column(name = "fria_affected_categories", columnDefinition = "TEXT")
    private String friaaffectedcategories;

    @Column(name = "fria_vulnerable_groups_included")
    private Boolean friavulnerablegroupsincluded;

    @Column(name = "fria_vulnerable_groups_description", columnDefinition = "TEXT")
    private String friavulnerablegroupsdescription;

    // Art. 27.1.d - Risks
    @Column(name = "fria_risks", columnDefinition = "TEXT")
    private String friarisks; // JSON array

    // Art. 27.1.e - Human oversight
    @Column(name = "fria_hitl_enabled")
    private Boolean friahitlenabled;

    @Column(name = "fria_human_oversight", columnDefinition = "TEXT")
    private String friahumanoversight;

    // Art. 27.1.f - Mitigation measures
    @Column(name = "fria_mitigation_measures", columnDefinition = "TEXT")
    private String friamitigationmeasures; // JSON array

    // Scores and calculations
    @Column(name = "fria_completeness_score", precision = 3, scale = 2)
    private BigDecimal friacompletenessscore;

    @Column(name = "fria_final_risk", precision = 3, scale = 2)
    private BigDecimal friafinalrisk;

    @Column(name = "fria_risk_level")
    private String friarisklevel; // low, medium, high, critical

    // Cross-validation (INC-007)
    @Column(name = "fria_cross_validation_score", precision = 3, scale = 2)
    private BigDecimal friacrossvalidationscore;

    @Column(name = "fria_cross_validation_result", columnDefinition = "TEXT")
    private String friacrossvalidationresult; // JSON

    // Notification (Art. 27.3)
    @Column(name = "fria_notified")
    private Boolean frianotified;

    @Column(name = "fria_notification_date")
    private Timestamp frianotificationdate;

    @Column(name = "fria_notification_id")
    private String frianotificationid;

    // Compliance flags
    @Column(name = "fria_art27_compliant")
    private Boolean friaart27compliant;

    @Column(name = "fria_approved")
    private Boolean friaapproved;

    @Column(name = "fria_approval_date")
    private Timestamp friaapprovaldate;

    // DPIA integration (Art. 27.4)
    @Column(name = "fria_dpia_integrated")
    private Boolean friadpiaintegrated;

    @Column(name = "fria_dpia_id")
    private Long friadpiaid;

    // Timestamps
    @Column(name = "fria_created_at")
    private Timestamp friacreatedat;

    @Column(name = "fria_updated_at")
    private Timestamp friaupdatedat;
}
```

---

## 📦 REPOSITORIOS

### `FriaAssessmentRepository`

**Ubicación:** `codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/compliance/FriaAssessmentRepository.java`

**Métodos Principales:**

```java
@Repository
public interface FriaAssessmentRepository extends JpaRepository<FriaAssessment, Long> {

    // Buscar por proyecto
    List<FriaAssessment> findByProject_Idxproject(Long projectId);

    // Buscar última FRIA de un proyecto
    Optional<FriaAssessment> findFirstByProject_IdxprojectOrderByFriacreatedatDesc(Long projectId);

    // Buscar por estado
    List<FriaAssessment> findByFriaart27compliant(Boolean compliant);

    // Buscar por riesgo
    List<FriaAssessment> findByFriafinalriskGreaterThanEqual(BigDecimal minRisk);

    // Buscar notificadas
    List<FriaAssessment> findByFrianotifiedTrue();
}
```

---

## 🔄 FLUJOS DE NEGOCIO

### Flujo 1: Crear Nueva Evaluación FRIA

```
1. Frontend → POST /api/v1/fria/create
2. BFF → FriaService.createFria()
3. Microservicio → FriaController.createFria()
4. Business Service → FriaAssessmentBusinessService.createFria()
5. Repository → save() → Estado DRAFT
6. Retorna friaId y status
```

### Flujo 2: Actualizar Paso del Wizard

```
1. Frontend → PUT /api/v1/fria/{friaId}/step/{stepNumber}
2. BFF → FriaService.updateStep()
3. Microservicio → FriaController.updateStep()
4. Business Service → updateFriaSection()
5. Business Service → calculateCompletenessScore()
6. Repository → save()
7. Retorna completenessScore actualizado
```

### Flujo 3: Calcular Riesgo Final

```
1. Frontend → POST /api/v1/fria/{friaId}/calculate-risk
2. BFF → FriaService.calculateRisk()
3. Microservicio → FriaController.calculateRisk()
4. Business Service → calculateFinalRisk()
   - Parsea riesgos y medidas (JSON)
   - Aplica fórmula Anexo IX
   - Determina nivel de riesgo
5. Repository → save()
6. Retorna finalRisk y riskLevel
```

### Flujo 4: Validación Cruzada (INC-007)

```
1. Frontend → POST /api/v1/fria/{friaId}/cross-validate
2. BFF → FriaService.crossValidate()
3. Microservicio → FriaController.crossValidate()
4. Business Service → crossValidate()
5. AIGovernanceClient → FRIAGeneratorClient.crossValidate()
6. Microservicio Python (leka-fria-generator) → Compara con métricas técnicas
7. Retorna consistencyScore e inconsistencies
8. Repository → save()
```

### Flujo 5: Notificar Autoridades (Art. 27.3)

```
1. Frontend → POST /api/v1/fria/{friaId}/notify-authority
2. BFF → FriaService.notifyAuthority()
3. Microservicio → FriaController.notifyAuthority()
4. Business Service → notifyAuthority()
   - Valida completitud >= 0.90
   - Valida riesgo >= 0.75
5. AIGovernanceClient → GovernanceApiClient.notifyAuthority()
6. Microservicio Python (codeflowx-governance-api) → Envía notificación
7. Actualiza frianotified, frianotificationdate, frianotificationid
8. Repository → save()
9. Retorna notificationId
```

---

## ✅ VALIDACIONES Y REGLAS

### Validaciones de Negocio

1. **Crear FRIA:**
   - Proyecto debe existir
   - Usuario debe existir
   - No validación de FRIA activa (permite múltiples FRIA por proyecto)

2. **Actualizar Paso:**
   - FRIA debe existir
   - FRIA no debe estar completada (solo DRAFT puede editarse)
   - Datos del paso deben ser válidos según tipo

3. **Calcular Riesgo:**
   - FRIA debe tener todos los pasos completos
   - Riesgos deben tener severidad, probabilidad e impacto válidos
   - Medidas deben tener efectividad válida (0.0 - 1.0)

4. **Notificar Autoridades:**
   - FRIA debe estar completa (completeness >= 0.90)
   - Riesgo final debe ser >= 0.75
   - No debe estar ya notificada

5. **Validación Cruzada:**
   - FRIA debe tener datos suficientes
   - Score de consistencia < 0.70 requiere justificación

---

## 🐍 INTEGRACIONES PYTHON

### 1. Validación Cruzada (INC-007)

**Microservicio:** `leka-fria-generator`

**Cliente:** `FRIAGeneratorClient`

**Endpoint:** `POST /api/v1/fria/cross-validate`

**Uso:**
```java
@Autowired
private AIGovernanceClient aiGovernanceClient;

CrossValidationResult result = aiGovernanceClient
    .friaGenerator()
    .crossValidate(friaData);
```

**Resiliencia:**
- Circuit Breaker: `friaGenerator`
- Retry: `friaGenerator`
- TimeLimiter: `friaGenerator`

### 2. Notificación a Autoridades (Art. 27.3)

**Microservicio:** `codeflowx-governance-api`

**Cliente:** `GovernanceApiClient`

**Endpoint:** `POST /api/v1/fria/{friaId}/notify-authority`

**Uso:**
```java
@Autowired
private AIGovernanceClient aiGovernanceClient;

FriaNotifyAuthorityResponse response = aiGovernanceClient
    .governanceApi()
    .notifyAuthority(friaId, request);
```

**Resiliencia:**
- Circuit Breaker: `governanceApi`
- Retry: `governanceApi`
- TimeLimiter: `governanceApi`

---

## 📝 DTOs PRINCIPALES

### Request DTOs

- `FriaCreateRequestDto` - Crear FRIA
- `FriaStepUpdateRequestDto` - Actualizar paso
- `FriaNotifyAuthorityRequest` - Notificar autoridades

### Response DTOs

- `FriaAssessmentDto` - FRIA completo
- `FriaAssessmentSummaryDto` - Resumen para listados
- `FriaCalculateRiskResponseDto` - Resultado cálculo de riesgo
- `FriaCrossValidateResponseDto` - Resultado validación cruzada
- `FriaNotifyAuthorityResponseDto` - Resultado notificación
- `FriaProjectsListResponseDto` - Lista de proyectos con FRIA
- `ProjectWithFriasDto` - Proyecto con evaluaciones FRIA

---

## 🔧 CONFIGURACIÓN

### Archivos de Configuración

#### `application.yml` (BFF)

**Ubicación:** `codeflowx.govern.bff.compliance/src/main/resources/application.yml`

**Configuración Principal:**
```yaml
server:
  port: 8083

spring:
  application:
    name: compliance-bff
  cloud:
    loadbalancer:
      enabled: true

# Resilience4j Configuration
resilience4j:
  circuitbreaker:
    instances:
      friaService:
        registerHealthIndicator: true
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        permittedNumberOfCallsInHalfOpenState: 3
        automaticTransitionFromOpenToHalfOpenEnabled: true
        waitDurationInOpenState: 10s
        failureRateThreshold: 50
        eventConsumerBufferSize: 10
  retry:
    instances:
      friaService:
        maxAttempts: 3
        waitDuration: 1s
        retryExceptions:
          - java.net.ConnectException
          - java.util.concurrent.TimeoutException

# Actuator y Observabilidad
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus,circuitbreakers,circuitbreakerevents
      base-path: /actuator
  endpoint:
    health:
      show-details: when-authorized
    metrics:
      enabled: true
    prometheus:
      enabled: true
  metrics:
    export:
      prometheus:
        enabled: true
        step: 30s

# Services Configuration
services:
  gateway:
    base-url: ${GATEWAY_BASE_URL:http://localhost:8080}

# Logging
logging:
  level:
    com.codeflowx.govern.bff.compliance: INFO
    org.springframework.web: DEBUG
    io.github.resilience4j: DEBUG
```

#### `application.yml` (Microservicio FRIA)

**Ubicación:** `codeflowx-governance-fria-service/src/main/resources/application.yml`

**Configuración Similar:** Misma estructura que BFF, puerto diferente

### Variables de Entorno

**Variables Requeridas:**
- `GATEWAY_BASE_URL` - URL del gateway interno (default: `http://localhost:8080`)
- `SPRING_PROFILES_ACTIVE` - Perfil activo (`dev`, `prod`)
- `APP_VERSION` - Versión de la aplicación (default: `1.0.0`)

**Variables Opcionales:**
- `SPRING_DATASOURCE_URL` - URL de base de datos
- `SPRING_DATASOURCE_USERNAME` - Usuario de BD
- `SPRING_DATASOURCE_PASSWORD` - Password de BD

### Configuración de Resilience4j

**Circuit Breakers:**
- `friaService` - Para llamadas al microservicio FRIA
- `friaGenerator` - Para validación cruzada (Python)
- `governanceApi` - Para notificación (Python)

**Parámetros Comunes:**
- `slidingWindowSize: 10` - Ventana deslizante de 10 llamadas
- `minimumNumberOfCalls: 5` - Mínimo 5 llamadas antes de evaluar
- `failureRateThreshold: 50` - Abre si >50% fallan
- `waitDurationInOpenState: 10s` - Espera 10s antes de half-open
- `permittedNumberOfCallsInHalfOpenState: 3` - Permite 3 llamadas en half-open

**Retry:**
- `maxAttempts: 3` - Máximo 3 intentos
- `waitDuration: 1s` - Espera 1s entre intentos
- `retryExceptions` - Solo reintenta en ConnectException y TimeoutException

**TimeLimiter:**
- Configurado en anotaciones `@TimeLimiter`
- Timeout de 30 segundos para llamadas Python

### Configuración de WebClient

**Ubicación:** Configurado en `FriaServiceImpl`

**Configuración:**
```java
@Bean
public WebClient webClient() {
    return WebClient.builder()
        .baseUrl(servicesConfig.getGateway().getBaseUrl())
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
        .build();
}
```

---

## 📦 DEPENDENCIAS

### Dependencias Principales (Maven)

**Ubicación:** `pom.xml` en cada módulo

**Dependencias Críticas:**

```xml
<!-- Spring Boot WebFlux (Reactivo) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
    <version>3.5.8</version>
</dependency>

<!-- Resilience4j -->
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-spring-boot3</artifactId>
    <version>2.1.0</version>
</dependency>
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-reactor</artifactId>
    <version>2.1.0</version>
</dependency>

<!-- Micrometer Prometheus -->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>

<!-- Spring Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- PostgreSQL Driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
```

### Versiones de Librerías

| Librería | Versión | Notas |
|----------|---------|-------|
| Spring Boot | 3.5.8 | Versión estable |
| Resilience4j | 2.1.0 | Compatible con Spring Boot 3 |
| Micrometer | Incluido en Spring Boot | Versión del BOM |
| PostgreSQL Driver | Incluido en Spring Boot | Versión del BOM |
| Jackson | Incluido en Spring Boot | Para JSON |

### Compatibilidad

- **Java:** 17+
- **Spring Boot:** 3.5.x
- **PostgreSQL:** 12+
- **Resilience4j:** 2.1.x

---

## 🗄️ MIGRACIONES DE BASE DE DATOS

### Tabla Principal: `FRIA_ASSESSMENT`

**Script de Creación:**
```sql
CREATE TABLE FRIA_ASSESSMENT (
    IDX_FRIA_ASSESSMENT BIGSERIAL PRIMARY KEY,
    IDX_PROJECT BIGINT NOT NULL,
    IDX_USER BIGINT NOT NULL,
    ID_UUID VARCHAR(255),

    -- Art. 27.1.a
    FRIA_PROCESS_DESCRIPTION TEXT,

    -- Art. 27.1.b
    FRIA_USAGE_PERIOD VARCHAR(200),
    FRIA_USAGE_FREQUENCY VARCHAR(200),

    -- Art. 27.1.c
    FRIA_AFFECTED_CATEGORIES TEXT, -- JSONB
    FRIA_VULNERABLE_GROUPS_INCLUDED BOOLEAN,
    FRIA_VULNERABLE_GROUPS_DESCRIPTION TEXT,

    -- Art. 27.1.d
    FRIA_RISKS TEXT, -- JSONB

    -- Art. 27.1.e
    FRIA_HITL_ENABLED BOOLEAN,
    FRIA_HUMAN_OVERSIGHT TEXT,

    -- Art. 27.1.f
    FRIA_MITIGATION_MEASURES TEXT, -- JSONB

    -- Scores
    FRIA_COMPLETENESS_SCORE DECIMAL(3,2),
    FRIA_FINAL_RISK DECIMAL(3,2),
    FRIA_RISK_LEVEL VARCHAR(50),

    -- Cross-validation
    FRIA_CROSS_VALIDATION_SCORE DECIMAL(3,2),
    FRIA_CROSS_VALIDATION_RESULT TEXT, -- JSONB

    -- Notification
    FRIA_NOTIFIED BOOLEAN,
    FRIA_NOTIFICATION_DATE TIMESTAMP,
    FRIA_NOTIFICATION_ID VARCHAR(255),

    -- Compliance
    FRIA_ART27_COMPLIANT BOOLEAN,
    FRIA_APPROVED BOOLEAN,
    FRIA_APPROVAL_DATE TIMESTAMP,

    -- DPIA
    FRIA_DPIA_INTEGRATED BOOLEAN,
    FRIA_DPIA_ID BIGINT,

    -- Versioning
    FRIA_VERSION INTEGER DEFAULT 1,
    FRIA_PREVIOUS_VERSION_ID BIGINT,

    -- Timestamps
    FRIA_CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FRIA_UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (IDX_PROJECT) REFERENCES PROJECT(IDX_PROJECT),
    FOREIGN KEY (FRIA_PREVIOUS_VERSION_ID) REFERENCES FRIA_ASSESSMENT(IDX_FRIA_ASSESSMENT)
);

CREATE INDEX IDX_FRIA_PROJECT ON FRIA_ASSESSMENT(IDX_PROJECT);
CREATE INDEX IDX_FRIA_STATUS ON FRIA_ASSESSMENT(FRIA_ART27_COMPLIANT);
CREATE INDEX IDX_FRIA_RISK ON FRIA_ASSESSMENT(FRIA_FINAL_RISK);
CREATE INDEX IDX_FRIA_NOTIFIED ON FRIA_ASSESSMENT(FRIA_NOTIFIED);
```

### Estrategia de Migración

**Versión 1.0.0:**
- Tabla `FRIA_ASSESSMENT` creada
- Índices básicos creados
- Sin migraciones previas

**Futuras Migraciones:**
- Usar Flyway o Liquibase para versionado
- Scripts en `src/main/resources/db/migration/`
- Nomenclatura: `V{version}__{descripcion}.sql`

---

## 🧪 TESTING

### Configuración de Testing

**Dependencias:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>io.projectreactor</groupId>
    <artifactId>reactor-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <scope>test</scope>
</dependency>
```

### Tests Unitarios

#### Ejemplo: `FriaAssessmentBusinessServiceTest`

**Ubicación:** `codeflowx.govern.business/src/test/java/com/codeflowx/govern/business/compliance/`

```java
@ExtendWith(MockitoExtension.class)
class FriaAssessmentBusinessServiceTest {

    @Mock
    private FriaAssessmentRepository repository;

    @Mock
    private AIGovernanceClient aiGovernanceClient;

    @InjectMocks
    private FriaAssessmentBusinessService service;

    @Test
    void testCreateFria() {
        // Given
        Long projectId = 1L;
        Long userId = 123L;
        FriaAssessment savedFria = new FriaAssessment();
        savedFria.setIdxfriaassessment(1L);

        when(repository.save(any(FriaAssessment.class))).thenReturn(savedFria);

        // When
        FriaAssessment result = service.createFria(projectId, userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdxfriaassessment()).isEqualTo(1L);
        verify(repository).save(any(FriaAssessment.class));
    }

    @Test
    void testCalculateCompletenessScore() {
        // Given
        Long friaId = 1L;
        FriaAssessment fria = new FriaAssessment();
        fria.setIdxfriaassessment(friaId);
        fria.setFriaprocessdescription("Description");
        fria.setFriausageperiod("2025-01-01 to 2025-12-31");
        fria.setFriausagefrequency("DAILY");
        fria.setFriaaffectedcategories("[]");
        fria.setFriarisks("[]");
        fria.setFriahumanoversight("Oversight");
        fria.setFriamitigationmeasures("[]");

        when(repository.findById(friaId)).thenReturn(Optional.of(fria));
        when(repository.save(any(FriaAssessment.class))).thenReturn(fria);

        // When
        BigDecimal score = service.calculateCompletenessScore(friaId);

        // Then
        assertThat(score).isEqualByComparingTo(BigDecimal.ONE);
        verify(repository).save(any(FriaAssessment.class));
    }

    @Test
    void testCalculateFinalRisk() {
        // Given
        Long friaId = 1L;
        FriaAssessment fria = new FriaAssessment();
        fria.setIdxfriaassessment(friaId);
        fria.setFriarisks("[{\"severity\":\"HIGH\",\"probability\":0.5,\"impact\":\"HIGH\"}]");
        fria.setFriamitigationmeasures("[{\"effectiveness\":0.5}]");

        when(repository.findById(friaId)).thenReturn(Optional.of(fria));
        when(repository.save(any(FriaAssessment.class))).thenReturn(fria);

        // When
        BigDecimal risk = service.calculateFinalRisk(friaId);

        // Then
        assertThat(risk).isBetween(BigDecimal.ZERO, BigDecimal.ONE);
        verify(repository).save(any(FriaAssessment.class));
    }
}
```

#### Ejemplo: `FriaServiceImplTest`

**Ubicación:** `codeflowx.govern.bff.compliance/src/test/java/com/codeflowx/govern/bff/compliance/service/impl/`

```java
@ExtendWith(MockitoExtension.class)
class FriaServiceImplTest {

    @Mock
    private WebClient webClient;

    @Mock
    private CircuitBreaker circuitBreaker;

    @Mock
    private Retry retry;

    @InjectMocks
    private FriaServiceImpl service;

    @Test
    void testCreateFria() {
        // Given
        FriaCreateRequestDto request = new FriaCreateRequestDto();
        request.setProjectId(1L);
        request.setUserId(123L);

        FriaAssessmentDto responseDto = new FriaAssessmentDto();
        responseDto.setId(1L);

        // Mock WebClient response
        // ... configuración de mocks ...

        // When
        Mono<FriaAssessmentDto> result = service.createFria(request);

        // Then
        StepVerifier.create(result)
            .expectNext(responseDto)
            .verifyComplete();
    }
}
```

### Tests de Integración

#### Ejemplo: `FriaControllerIntegrationTest`

**Ubicación:** `codeflowx-governance-fria-service/src/test/java/com/codeflowx/govern/fria/controller/`

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class FriaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FriaAssessmentRepository repository;

    @Test
    void testCreateFria() throws Exception {
        // Given
        FriaCreateRequestDto request = new FriaCreateRequestDto();
        request.setProjectId(1L);
        request.setUserId(123L);

        // When & Then
        mockMvc.perform(post("/api/v1/fria/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.status").value("DRAFT"));
    }
}
```

### Mocking de Servicios Externos

**Para Tests de Integración con Python:**

```java
@MockBean
private AIGovernanceClient aiGovernanceClient;

@Test
void testCrossValidate() {
    // Given
    CrossValidationResult mockResult = CrossValidationResult.builder()
        .consistencyScore(new BigDecimal("0.85"))
        .inconsistencies(Collections.emptyList())
        .build();

    when(aiGovernanceClient.friaGenerator().crossValidate(any()))
        .thenReturn(mockResult);

    // When
    CrossValidationResult result = service.crossValidate(1L);

    // Then
    assertThat(result.getConsistencyScore()).isEqualByComparingTo(new BigDecimal("0.85"));
}
```

---

## 🔍 TROUBLESHOOTING

### Problemas Comunes y Soluciones

#### 1. Circuit Breaker Siempre Abierto

**Síntomas:**
- Llamadas a microservicios siempre fallan
- Error: `CircuitBreakerOpenException`
- Logs muestran circuit breaker en estado OPEN

**Causas:**
- Microservicio Python no disponible
- Timeout muy corto
- Tasa de fallos > 50%

**Solución:**
1. Verificar salud del microservicio:
   ```bash
   curl http://localhost:8012/health
   ```

2. Revisar logs de Resilience4j:
   ```bash
   grep "CircuitBreaker" logs/application.log
   ```

3. Resetear circuit breaker (si es necesario):
   ```java
   circuitBreakerRegistry.circuitBreaker("friaService").reset();
   ```

4. Ajustar configuración si es necesario:
   ```yaml
   resilience4j:
     circuitbreaker:
       instances:
         friaService:
           failureRateThreshold: 70  # Aumentar umbral
           waitDurationInOpenState: 30s  # Aumentar tiempo de espera
   ```

#### 2. Error: "FRIA not found"

**Síntomas:**
- Error 404 al acceder a FRIA
- `IllegalArgumentException: FRIA not found`

**Causas:**
- ID de FRIA incorrecto
- FRIA eliminado
- Problema de permisos

**Solución:**
1. Verificar ID en base de datos:
   ```sql
   SELECT * FROM FRIA_ASSESSMENT WHERE IDX_FRIA_ASSESSMENT = ?;
   ```

2. Verificar logs:
   ```bash
   grep "FRIA not found" logs/application.log
   ```

3. Verificar permisos del usuario

#### 3. Error: "FRIA must be complete before notifying"

**Síntomas:**
- No se puede notificar a autoridades
- Error: `IllegalStateException: FRIA must be complete`

**Causas:**
- Completitud < 0.90
- Pasos del wizard incompletos

**Solución:**
1. Verificar completitud:
   ```sql
   SELECT FRIA_COMPLETENESS_SCORE FROM FRIA_ASSESSMENT WHERE IDX_FRIA_ASSESSMENT = ?;
   ```

2. Completar pasos faltantes en el wizard

3. Recalcular completitud:
   ```java
   service.calculateCompletenessScore(friaId);
   ```

#### 4. Error: "Final risk must be >= 0.75 to notify authorities"

**Síntomas:**
- No se puede notificar aunque FRIA esté completa
- Error: `IllegalStateException: Final risk must be >= 0.75`

**Causas:**
- Riesgo final < 0.75
- Riesgo no calculado

**Solución:**
1. Verificar riesgo final:
   ```sql
   SELECT FRIA_FINAL_RISK FROM FRIA_ASSESSMENT WHERE IDX_FRIA_ASSESSMENT = ?;
   ```

2. Calcular riesgo si no está calculado:
   ```java
   service.calculateFinalRisk(friaId);
   ```

3. Si riesgo < 0.75, no se puede notificar (comportamiento esperado)

#### 5. Timeout en Llamadas Python

**Síntomas:**
- TimeoutException en logs
- Llamadas a Python fallan

**Causas:**
- Microservicio Python lento
- Timeout muy corto
- Problemas de red

**Solución:**
1. Verificar salud del microservicio Python:
   ```bash
   curl http://localhost:8012/health
   ```

2. Aumentar timeout en `@TimeLimiter`:
   ```java
   @TimeLimiter(name = "friaGenerator", fallbackMethod = "crossValidateFallback")
   ```

3. Verificar configuración de timeout en `application.yml`

#### 6. Error de Serialización JSON

**Síntomas:**
- Error al parsear JSON de riesgos o medidas
- `JsonProcessingException`

**Causas:**
- JSON mal formado en base de datos
- Cambio en estructura de DTOs

**Solución:**
1. Verificar JSON en base de datos:
   ```sql
   SELECT FRIA_RISKS FROM FRIA_ASSESSMENT WHERE IDX_FRIA_ASSESSMENT = ?;
   ```

2. Validar formato JSON:
   ```bash
   echo '{"risks": [...]}' | jq .
   ```

3. Corregir JSON si está mal formado

### Logs Importantes

**Ubicación de Logs:**
- Desarrollo: `logs/application.log`
- Producción: Configurado en `logback-spring.xml`

**Logs Clave a Revisar:**
```bash
# Errores de FRIA
grep "FRIA" logs/application.log | grep ERROR

# Circuit Breaker
grep "CircuitBreaker" logs/application.log

# Llamadas a Python
grep "friaGenerator\|governanceApi" logs/application.log

# Validaciones
grep "validation\|Validation" logs/application.log
```

### Pasos de Diagnóstico

1. **Verificar Salud del Sistema:**
   ```bash
   curl http://localhost:8083/actuator/health
   ```

2. **Verificar Circuit Breakers:**
   ```bash
   curl http://localhost:8083/actuator/circuitbreakers
   ```

3. **Verificar Métricas:**
   ```bash
   curl http://localhost:8083/actuator/metrics
   ```

4. **Revisar Logs:**
   ```bash
   tail -f logs/application.log | grep FRIA
   ```

---

## ⚡ PERFORMANCE Y OPTIMIZACIÓN

### Consideraciones de Performance

1. **Llamadas a Python:**
   - Usar Circuit Breaker para evitar llamadas cuando servicio está caído
   - Timeout de 30s para evitar bloqueos
   - Retry solo en errores transitorios

2. **Consultas a Base de Datos:**
   - Índices en campos de búsqueda frecuente
   - Paginación en listados
   - Evitar N+1 queries

3. **Serialización JSON:**
   - Cachear resultados de parseo cuando sea posible
   - Usar ObjectMapper reutilizable

### Optimizaciones Aplicadas

1. **Índices en Base de Datos:**
   ```sql
   CREATE INDEX IDX_FRIA_PROJECT ON FRIA_ASSESSMENT(IDX_PROJECT);
   CREATE INDEX IDX_FRIA_STATUS ON FRIA_ASSESSMENT(FRIA_ART27_COMPLIANT);
   CREATE INDEX IDX_FRIA_RISK ON FRIA_ASSESSMENT(FRIA_FINAL_RISK);
   ```

2. **Paginación:**
   - Listado de proyectos con paginación del backend
   - Tamaño de página configurable (default: 10)

3. **Circuit Breakers:**
   - Evitan llamadas cuando servicios están caídos
   - Reducen latencia en caso de fallos

### Métricas Clave a Monitorear

**Micrometer Metrics:**
- `http.server.requests` - Tiempo de respuesta HTTP
- `resilience4j.circuitbreaker.calls` - Estado de circuit breakers
- `resilience4j.retry.calls` - Intentos de retry

**Métricas Personalizadas:**
- `compliance.fria.create` - Tiempo de creación
- `compliance.fria.calculate.risk` - Tiempo de cálculo de riesgo
- `compliance.fria.cross.validate` - Tiempo de validación cruzada

**Acceso a Métricas:**
```bash
# Prometheus
curl http://localhost:8083/actuator/prometheus

# Métricas específicas
curl http://localhost:8083/actuator/metrics/http.server.requests
```

---

## 🔒 SEGURIDAD

### Validaciones de Seguridad

1. **Autenticación:**
   - Todos los endpoints requieren autenticación
   - Usar Spring Security

2. **Autorización:**
   - Validar permisos por rol
   - Roles requeridos: `admin`, `compliance_officer`, `project_manager`

3. **Validación de Input:**
   - Validar todos los inputs con `@Valid`
   - Sanitizar datos de usuario
   - Validar longitud de campos

4. **Validación de Negocio:**
   - Verificar que proyecto existe
   - Verificar que usuario tiene permisos
   - Validar estado de FRIA antes de operaciones

### Consideraciones de Privacidad

1. **Datos Personales:**
   - No almacenar datos personales en FRIA
   - Validar que descripciones no contengan datos sensibles

2. **Auditoría:**
   - Registrar todas las operaciones
   - Mantener historial de cambios

### Configuración de Seguridad

**Spring Security:**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Configuración de seguridad
    // Validación de roles
    // CORS si es necesario
}
```

---

**Última actualización:** Diciembre 2025
