# 👨‍💻 GUÍA PARA DEVELOPERS BACKEND - CONFORMITY DECLARATION

**Versión:** 1.0
**Fecha:** Enero 2025
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
8. [DTOs](#dtos)
9. [Validaciones y Reglas](#validaciones-y-reglas)

---

## 🏗️ ARQUITECTURA GENERAL

### Capas de la Aplicación

```
Frontend (Next.js)
    ↓
API Routes Next.js
    ↓
BFF (Backend for Frontend)
    ↓
Business Microservice (Conformity Declaration Service)
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
   - Ubicación: `codeflowx-governance-conformity-declaration-service`
   - Responsabilidad: Endpoints REST para Conformity Declaration
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

#### Controller
- **Ubicación:** `controller/ConformityDeclarationController.java`
- **Responsabilidad:** Expone endpoints REST para frontend
- **Características:**
  - Maneja requests HTTP
  - Retorna DTOs optimizados
  - Manejo de errores con `onErrorResume`
  - Reactivo (Mono)

**Ejemplo:**
```java
@RestController
@RequestMapping("/api/v1/conformity-declaration")
@RequiredArgsConstructor
@Slf4j
public class ConformityDeclarationController {

    private final ConformityDeclarationService conformityDeclarationService;

    @GetMapping("/projects")
    public Mono<ResponseEntity<ConformityDeclarationProjectsListResponseDto>> listProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String hasDeclarations,
            @RequestParam(required = false) String search) {

        return conformityDeclarationService.listProjectsWithDeclarations(page, size, hasDeclarations, search)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(/* empty response */));
    }
}
```

#### Service Interface
- **Ubicación:** `service/ConformityDeclarationService.java`
- **Responsabilidad:** Define contratos de servicio

#### Service Implementation
- **Ubicación:** `service/impl/ConformityDeclarationServiceImpl.java`
- **Responsabilidad:** Implementa llamadas a microservicio de negocio
- **Características:**
  - Usa WebClient para comunicación reactiva
  - Implementa Circuit Breaker y Retry (Resilience4j)
  - Logging completo
  - Métricas de compliance

**Ejemplo:**
```java
@Service
@Slf4j
public class ConformityDeclarationServiceImpl implements ConformityDeclarationService {

    private final WebClient webClient;
    private final CircuitBreaker conformityDeclarationCircuitBreaker;
    private final Retry conformityDeclarationRetry;

    @Override
    public Mono<ConformityDeclarationProjectsListResponseDto> listProjectsWithDeclarations(
            int page, int size, String hasDeclarations, String search) {

        StringBuilder uriBuilder = new StringBuilder("/api/v1/conformity-declaration/projects");
        uriBuilder.append("?page=").append(page);
        uriBuilder.append("&size=").append(size);
        // ... más parámetros

        return webClient.get()
                .uri(uriBuilder.toString())
                .retrieve()
                .bodyToMono(ConformityDeclarationProjectsListResponseDto.class)
                .transformDeferred(CircuitBreakerOperator.of(conformityDeclarationCircuitBreaker))
                .transformDeferred(RetryOperator.of(conformityDeclarationRetry))
                .doOnError(error -> log.error("Error listing projects", error));
    }
}
```

### 2. Microservicio Layer

**Ubicación:** `nocode.service/codeflowx-governance-conformity-declaration-service/`

#### Application Class
- **Ubicación:** `src/main/java/com/codeflowx/govern/conformity/ConformityDeclarationServiceApplication.java`
- **Características:**
  - Spring Boot Application
  - Configuración JPA
  - OpenAPI/Swagger
  - Scan de packages

#### Controller REST
- **Ubicación:** `src/main/java/com/codeflowx/govern/conformity/controller/ConformityDeclarationController.java`
- **Características:**
  - Endpoints REST reactivos
  - Conversión Entidad → DTO
  - Manejo de errores
  - Validaciones

**Ejemplo:**
```java
@RestController
@RequestMapping("/api/v1/conformity-declaration")
@RequiredArgsConstructor
@Slf4j
public class ConformityDeclarationController {

    private final ConformityDeclarationBusinessService businessService;

    @PostMapping
    public Mono<ResponseEntity<ConformityDeclarationDto>> createConformityDeclaration(
            @Valid @RequestBody Mono<CreateConformityDeclarationRequestDto> requestMono) {

        return requestMono
                .flatMap(request -> {
                    ConformityDeclaration declaration = businessService.createDeclaration(
                            request.getAssessmentId(),
                            request.getProviderName(),
                            request.getAiSystemName()
                    );
                    return Mono.just(convertToDto(declaration));
                })
                .map(ResponseEntity::ok)
                .onErrorResume(error -> {
                    log.error("Error creating declaration", error);
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }
}
```

#### Exception Handler
- **Ubicación:** `src/main/java/com/codeflowx/govern/conformity/exception/GlobalExceptionHandler.java`
- **Características:**
  - Manejo global de excepciones
  - Retorna DTOs tipados (ErrorResponseDto)
  - Nunca retorna Map

**Ejemplo:**
```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleIllegalArgumentException(
            IllegalArgumentException ex) {

        ErrorResponseDto error = ErrorResponseDto.of(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            ex.getMessage(),
            "/api/v1/conformity-declaration"
        );

        return Mono.just(ResponseEntity.badRequest().body(error));
    }
}
```

### 3. Business Service Layer

**Ubicación:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/`

#### ConformityDeclarationBusinessService
- **Ubicación:** `ConformityDeclarationBusinessService.java`
- **Responsabilidades:**
  - Crear declaraciones desde assessments
  - Firmar declaraciones
  - Obtener declaraciones por proyecto/assessment
  - Calcular estadísticas
  - Validaciones de negocio

**Métodos Principales:**
```java
@Service
@Slf4j
public class ConformityDeclarationBusinessService {

    @Autowired
    private ConformityDeclarationRepository repository;

    @Autowired
    private ComplianceAssessmentRepository assessmentRepository;

    @Autowired
    private ProjectRepository projectRepository;

    /**
     * Crea una nueva declaración de conformidad basada en un assessment
     */
    @Transactional
    public ConformityDeclaration createDeclaration(
            Long assessmentId, String providerName, String aiSystemName) {

        log.info("Creating conformity declaration for assessment: {}", assessmentId);

        // 1. Obtener assessment
        ComplianceAssessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new IllegalArgumentException("Assessment not found: " + assessmentId));

        // 2. Validar que el assessment esté listo para certificación
        if (assessment.getComreadyforcertification() == null ||
            !assessment.getComreadyforcertification()) {
            throw new IllegalArgumentException(
                "Assessment " + assessmentId + " is not ready for certification. " +
                "Cannot create declaration."
            );
        }

        // 3. Crear declaración
        ConformityDeclaration declaration = new ConformityDeclaration();
        declaration.setAssessment(assessment);
        declaration.setProviderName(providerName);
        declaration.setAiSystemName(aiSystemName);
        declaration.setStatus("DRAFT");
        declaration.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        // 4. Copiar scores del assessment
        declaration.setOverallComplianceScore(assessment.getComoverallscore());
        declaration.setCompliancePercentage(
            assessment.getComoverallscore() != null ?
                assessment.getComoverallscore().multiply(new BigDecimal("100")) :
                BigDecimal.ZERO
        );

        // 5. Generar versión
        Long existingDeclarationsCount = repository.countByProjectId(
            assessment.getProject().getIdxproject()
        );
        declaration.setAiSystemVersion("v1." + (existingDeclarationsCount + 1));

        // 6. Guardar
        declaration = repository.save(declaration);
        log.info("Conformity declaration created with ID: {}", declaration.getIdxDeclaration());

        return declaration;
    }

    /**
     * Firma una declaración de conformidad
     */
    @Transactional
    public ConformityDeclaration signDeclaration(
            Long declarationId, String signedBy, String digitalSignature) {

        log.info("Signing conformity declaration: {} by: {}", declarationId, signedBy);

        ConformityDeclaration declaration = repository.findById(declarationId)
                .orElseThrow(() -> new IllegalArgumentException("Declaration not found: " + declarationId));

        if (!"DRAFT".equals(declaration.getStatus())) {
            throw new IllegalArgumentException(
                "Only DRAFT declarations can be signed. Current status: " + declaration.getStatus()
            );
        }

        declaration.setStatus("SIGNED");
        declaration.setSignedBy(signedBy);
        declaration.setSignatureDate(Timestamp.valueOf(LocalDateTime.now()));
        declaration.setDigitalSignature(digitalSignature);
        declaration.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        declaration = repository.save(declaration);
        log.info("Conformity declaration {} signed successfully.", declarationId);

        return declaration;
    }

    /**
     * Obtiene todas las declaraciones de un proyecto
     */
    public List<ConformityDeclaration> getDeclarationsByProject(Long projectId) {
        return repository.findByAssessmentProjectIdxprojectOrderByCreatedAtDesc(projectId);
    }

    /**
     * Obtiene una declaración por ID
     */
    public Optional<ConformityDeclaration> getDeclarationById(Long declarationId) {
        return repository.findById(declarationId);
    }
}
```

### 4. Repository Layer

**Ubicación:** `nocode.service/codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/compliance/`

#### ConformityDeclarationRepository
- **Ubicación:** `ConformityDeclarationRepository.java`
- **Características:**
  - Extiende `GenericRepository<ConformityDeclaration, Long>`
  - Métodos de consulta personalizados
  - Queries JPA

**Ejemplo:**
```java
@Repository
public interface ConformityDeclarationRepository
    extends GenericRepository<ConformityDeclaration, Long> {

    List<ConformityDeclaration> findByAssessmentProjectIdxprojectOrderByCreatedAtDesc(
        Long projectId
    );

    List<ConformityDeclaration> findByAssessmentIdxcomplianceassessmentOrderByCreatedAtDesc(
        Long assessmentId
    );

    @Query("SELECT COUNT(cd) FROM ConformityDeclaration cd " +
           "WHERE cd.assessment.project.idxproject = :projectId")
    Long countByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT COUNT(cd) FROM ConformityDeclaration cd " +
           "WHERE cd.assessment.project.idxproject = :projectId " +
           "AND cd.status = 'SIGNED'")
    Long countSignedByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT COUNT(cd) FROM ConformityDeclaration cd " +
           "WHERE cd.assessment.project.idxproject = :projectId " +
           "AND cd.status = 'DRAFT'")
    Long countDraftsByProjectId(@Param("projectId") Long projectId);

    Optional<ConformityDeclaration> findTopByAssessmentProjectIdxprojectOrderByCreatedAtDesc(
        Long projectId
    );
}
```

### 5. Entity Layer

**Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/governance/`

#### ConformityDeclaration
- **Ubicación:** `ConformityDeclaration.java`
- **Tabla:** `GOVCONFORMITYDECLARATIONS`
- **Características:**
  - Campos completos según Annex V EU AI Act
  - Relación con `ComplianceAssessment`
  - Estados: DRAFT, SIGNED, PUBLISHED, REVOKED
  - Información del proveedor y sistema IA
  - Compliance por artículo (Art. 9-15)
  - Scores y porcentajes
  - Firma y PDF

**Campos Principales:**
```java
@Entity
@Table(name = "GOVCONFORMITYDECLARATIONS")
public class ConformityDeclaration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXDECLARATION")
    private Long idxDeclaration;

    @ManyToOne
    @JoinColumn(name = "IDXCOMPLIANCEASSESSMENT")
    private ComplianceAssessment assessment;

    @Column(name = "PROVIDERNAME")
    private String providerName;

    @Column(name = "AISYSTEMNAME")
    private String aiSystemName;

    @Column(name = "AISYSTEMVERSION")
    private String aiSystemVersion;

    @Column(name = "STATUS")
    private String status; // DRAFT, SIGNED, PUBLISHED, REVOKED

    @Column(name = "OVERALLCOMPLIANCESCORE")
    private BigDecimal overallComplianceScore;

    @Column(name = "COMPLIANCEPERCENTAGE")
    private BigDecimal compliancePercentage;

    @Column(name = "SIGNEDBY")
    private String signedBy;

    @Column(name = "SIGNATUREDATE")
    private Timestamp signatureDate;

    @Column(name = "DIGITALSIGNATURE")
    private String digitalSignature;

    @Column(name = "PDFPATH")
    private String pdfPath;

    // Compliance por artículo
    @Column(name = "ART9RISKMANAGEMENT")
    private Boolean art9RiskManagement;

    @Column(name = "ART10DATAGOVERNANCE")
    private Boolean art10DataGovernance;

    // ... más campos

    @Column(name = "CREATEDAT")
    private Timestamp createdAt;

    @Column(name = "UPDATEDAT")
    private Timestamp updatedAt;
}
```

---

## 📦 DTOs

**Ubicación:** `nocode.service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/`

### DTOs Principales

1. **ConformityDeclarationDto**
   - DTO principal para declaraciones
   - Incluye todos los campos de la entidad

2. **CreateConformityDeclarationRequestDto**
   - Request para crear declaración
   - Campos: `assessmentId`, `providerName`, `aiSystemName`

3. **SignConformityDeclarationRequestDto**
   - Request para firmar declaración
   - Campos: `declarationId`, `signedBy`, `digitalSignature`

4. **ConformityDeclarationProjectsListResponseDto**
   - Response para lista de proyectos
   - Incluye: `projects`, `pagination`, `statistics`

5. **ConformityDeclarationProjectSummaryDto**
   - Resumen de declaraciones por proyecto
   - Incluye: total, firmadas, borradores, última declaración

6. **ConformityDeclarationProjectsStatisticsDto**
   - Estadísticas globales
   - Incluye: total proyectos, con declaraciones, total declaraciones, etc.

7. **ConformityDeclarationPaginationDto**
   - Información de paginación
   - Campos: `page`, `size`, `totalElements`, `totalPages`

8. **ConformityDeclarationManagerDataDto**
   - Datos para pantalla de gestión
   - Incluye: `projectId`, `projectName`, `assessments`, `declarations`, `preview`

9. **ConformityDeclarationPreviewDto**
   - Vista previa de declaración
   - Campos: `systemName`, `providerName`, `assessmentId`

---

## ✅ VALIDACIONES Y REGLAS

### Validaciones de Negocio

#### 1. Creación de Declaración

```java
// Assessment debe existir
ComplianceAssessment assessment = assessmentRepository.findById(assessmentId)
    .orElseThrow(() -> new IllegalArgumentException("Assessment not found"));

// Assessment debe estar listo para certificación
if (assessment.getComreadyforcertification() == null ||
    !assessment.getComreadyforcertification()) {
    throw new IllegalArgumentException(
        "Assessment is not ready for certification"
    );
}
```

#### 2. Firma de Declaración

```java
// Declaración debe existir
ConformityDeclaration declaration = repository.findById(declarationId)
    .orElseThrow(() -> new IllegalArgumentException("Declaration not found"));

// Solo declaraciones DRAFT pueden ser firmadas
if (!"DRAFT".equals(declaration.getStatus())) {
    throw new IllegalArgumentException(
        "Only DRAFT declarations can be signed"
    );
}

// signedBy es requerido
if (signedBy == null || signedBy.trim().isEmpty()) {
    throw new IllegalArgumentException("signedBy is required");
}
```

### Reglas de Negocio

#### 1. Generación de Versiones

```java
// Versión automática: v1.{count + 1}
Long existingDeclarationsCount = repository.countByProjectId(projectId);
declaration.setAiSystemVersion("v1." + (existingDeclarationsCount + 1));
```

#### 2. Copia de Datos del Assessment

```java
// Copiar scores del assessment
declaration.setOverallComplianceScore(assessment.getComoverallscore());
declaration.setCompliancePercentage(
    assessment.getComoverallscore() != null ?
        assessment.getComoverallscore().multiply(new BigDecimal("100")) :
        BigDecimal.ZERO
);

// Copiar compliance por artículo
declaration.setArt9RiskManagement(
    assessment.getComstep2qmsscore() != null &&
    assessment.getComstep2qmsscore().compareTo(new BigDecimal("0.80")) >= 0
);
// ... más artículos
```

---

## 🔧 CONFIGURACIÓN

### BFF Configuration

**Ubicación:** `codeflowx.govern.bff.compliance/src/main/resources/application.yml`

```yaml
services:
  conformity-declaration:
    base-url: ${CONFORMITY_DECLARATION_SERVICE_URL:http://localhost:8099}

resilience4j:
  circuitbreaker:
    instances:
      conformityDeclarationService:
        registerHealthIndicator: true
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        permittedNumberOfCallsInHalfOpenState: 3
        automaticTransitionFromOpenToHalfOpenEnabled: true
        waitDurationInOpenState: 10s
        failureRateThreshold: 50
        slowCallRateThreshold: 100
        slowCallDurationThreshold: 2s

  retry:
    instances:
      conformityDeclarationService:
        maxAttempts: 3
        waitDuration: 1s
        retryExceptions:
          - java.net.ConnectException
          - java.util.concurrent.TimeoutException
```

### Microservicio Configuration

**Ubicación:** `codeflowx-governance-conformity-declaration-service/src/main/resources/application.yml`

```yaml
spring:
  application:
    name: governance-conformity-declaration-service
  datasource:
    url: ${DATASOURCE_URL:jdbc:postgresql://localhost:5432/nocode}
    username: ${DATASOURCE_USERNAME:nocode}
    password: ${DATASOURCE_PASSWORD:nocode}
  jpa:
    hibernate:
      ddl-auto: none
    show-sql: ${JPA_SHOW_SQL:false}

server:
  port: ${SERVER_PORT:8099}
```

---

## 🚀 COMPILACIÓN Y EJECUCIÓN

### Compilar BFF

```bash
cd nocode.service/codeflowx.govern.bff.compliance
mvn clean install
```

### Compilar Microservicio

```bash
cd nocode.service/codeflowx-governance-conformity-declaration-service
mvn clean install
```

### Ejecutar Microservicio

```bash
cd nocode.service/codeflowx-governance-conformity-declaration-service
mvn spring-boot:run
```

### Swagger UI

Una vez iniciado el microservicio:
- **Swagger UI:** http://localhost:8099/swagger-ui.html
- **API Docs:** http://localhost:8099/api-docs

---

## 📝 NOTAS IMPORTANTES

### 1. Separación de Capas

- **BFF:** Solo trabaja con DTOs, nunca con Entidades JPA
- **Microservicio:** Convierte Entidad → DTO antes de retornar
- **Business Service:** Trabaja con Entidades JPA
- **Repository:** Trabaja con Entidades JPA

### 2. Reactividad

- **BFF:** Completamente reactivo (WebFlux, Mono)
- **Microservicio:** Completamente reactivo
- **Business Service:** Síncrono, envuelto en `Mono.fromCallable()` en el microservicio

### 3. Manejo de Errores

- Siempre retornar DTOs tipados (ErrorResponseDto), nunca Map
- Usar `@RestControllerAdvice` para manejo global
- Logging completo de errores

### 4. Transaccionalidad

- Métodos que modifican datos deben ser `@Transactional`
- Business Services son transaccionales
- Repositories no son transaccionales (lo maneja el Service)

---

## 🧪 TESTING

### Testing de Business Service

```java
@SpringBootTest
@Transactional
class ConformityDeclarationBusinessServiceTest {

    @Autowired
    private ConformityDeclarationBusinessService service;

    @Autowired
    private ConformityDeclarationRepository repository;

    @Test
    void testCreateDeclaration_Success() {
        // Given
        Long assessmentId = 1L;
        String providerName = "Test Provider";
        String aiSystemName = "Test System";

        // When
        ConformityDeclaration declaration = service.createDeclaration(
            assessmentId, providerName, aiSystemName
        );

        // Then
        assertNotNull(declaration);
        assertEquals("DRAFT", declaration.getStatus());
        assertEquals(providerName, declaration.getProviderName());
    }

    @Test
    void testCreateDeclaration_AssessmentNotReady_ThrowsException() {
        // Given
        Long assessmentId = 999L; // Assessment no listo

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> {
            service.createDeclaration(assessmentId, "Provider", "System");
        });
    }
}
```

### Testing de Controller (Microservicio)

```java
@WebFluxTest(ConformityDeclarationController.class)
class ConformityDeclarationControllerTest {

    @MockBean
    private ConformityDeclarationBusinessService businessService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testCreateDeclaration_Success() {
        // Given
        CreateConformityDeclarationRequestDto request = new CreateConformityDeclarationRequestDto();
        request.setAssessmentId(1L);
        request.setProviderName("Test Provider");
        request.setAiSystemName("Test System");

        ConformityDeclaration declaration = new ConformityDeclaration();
        when(businessService.createDeclaration(any(), any(), any()))
            .thenReturn(declaration);

        // When/Then
        webTestClient.post()
            .uri("/api/v1/conformity-declaration")
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk();
    }
}
```

### Testing de Repository

```java
@DataJpaTest
class ConformityDeclarationRepositoryTest {

    @Autowired
    private ConformityDeclarationRepository repository;

    @Test
    void testFindByProjectId() {
        // Given
        Long projectId = 1L;

        // When
        List<ConformityDeclaration> declarations =
            repository.findByAssessmentProjectIdxprojectOrderByCreatedAtDesc(projectId);

        // Then
        assertNotNull(declarations);
    }
}
```

---

## 🐛 TROUBLESHOOTING

### Problemas Comunes

#### 1. Error: "Assessment not found"
**Causa:** El assessmentId no existe en la BD.
**Solución:**
- Verificar que el assessment existe: `SELECT * FROM COMPLIANCEASSESSMENTS WHERE IDXCOMPLIANCEASSESSMENT = ?`
- Verificar logs: `log.error("Assessment not found: {}", assessmentId)`
- Revisar que el ID se está pasando correctamente desde el frontend

#### 2. Error: "Assessment is not ready for certification"
**Causa:** `comreadyforcertification = false` o `NULL`.
**Solución:**
- Verificar estado del assessment: `SELECT comreadyforcertification FROM COMPLIANCEASSESSMENTS WHERE IDXCOMPLIANCEASSESSMENT = ?`
- Completar todos los pasos del assessment
- Recalcular overall score

#### 3. Error de Transacción: "Transaction rolled back"
**Causa:** Excepción no manejada dentro de método `@Transactional`.
**Solución:**
- Revisar logs para excepción raíz
- Verificar que todas las validaciones están antes de operaciones de BD
- Verificar constraints de BD (FKs, NOT NULL, etc.)

#### 4. Error: "Circuit Breaker is OPEN"
**Causa:** El microservicio está fallando y el circuit breaker se abrió.
**Solución:**
- Verificar que el microservicio está corriendo: `curl http://localhost:8099/actuator/health`
- Revisar logs del microservicio
- Esperar a que el circuit breaker se cierre automáticamente (10s por defecto)
- Verificar configuración de Resilience4j

#### 5. Error: "Connection refused" en WebClient
**Causa:** El microservicio no está accesible desde el BFF.
**Solución:**
- Verificar URL del microservicio en `application.yml`
- Verificar que el microservicio está corriendo
- Verificar firewall/red
- Verificar variables de entorno: `CONFORMITY_DECLARATION_SERVICE_URL`

### Debugging

#### Habilitar Logs Detallados

```yaml
# application.yml
logging:
  level:
    com.codeflowx.govern.business.compliance: DEBUG
    com.codeflowx.govern.bff.compliance: DEBUG
    org.springframework.web: DEBUG
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

#### Verificar Estado de Servicios

```bash
# Health check del microservicio
curl http://localhost:8099/actuator/health

# Health check del BFF
curl http://localhost:8090/actuator/health

# Métricas de circuit breaker
curl http://localhost:8090/actuator/metrics/resilience4j.circuitbreaker.calls
```

#### Analizar Logs

```bash
# Buscar errores en logs
grep "ERROR" logs/application.log | grep "ConformityDeclaration"

# Buscar transacciones rollback
grep "Transaction rolled back" logs/application.log

# Buscar circuit breaker
grep "CircuitBreaker" logs/application.log
```

---

## 🔄 EXTENSIBILIDAD

### Agregar Nuevo Endpoint

1. **BFF Layer:**
   - Agregar método en `ConformityDeclarationService.java` (interfaz)
   - Implementar en `ConformityDeclarationServiceImpl.java`
   - Agregar endpoint en `ConformityDeclarationController.java` (BFF)

2. **Microservicio Layer:**
   - Agregar endpoint en `ConformityDeclarationController.java` (microservicio)
   - Agregar método en `ConformityDeclarationBusinessService.java` si es necesario
   - Crear DTOs si es necesario

3. **Business Service:**
   - Agregar lógica de negocio
   - Agregar validaciones
   - Agregar logging

### Agregar Nuevo Campo a Entidad

1. **Migración de BD:**
```sql
-- migrations/V2__add_new_field.sql
ALTER TABLE GOVCONFORMITYDECLARATIONS
ADD COLUMN NEWFIELD VARCHAR(255);
```

2. **Actualizar Entidad:**
```java
@Column(name = "NEWFIELD")
private String newField;
```

3. **Actualizar DTOs:**
```java
private String newField;
```

4. **Actualizar Business Service:**
```java
declaration.setNewField(value);
```

5. **Actualizar Repository:**
- Agregar query si es necesario para filtrar/buscar por nuevo campo

### Agregar Nueva Validación

1. **En Business Service:**
```java
@Transactional
public ConformityDeclaration createDeclaration(...) {
    // Validación existente
    if (assessment.getComreadyforcertification() == null ||
        !assessment.getComreadyforcertification()) {
        throw new IllegalArgumentException("Assessment not ready");
    }

    // Nueva validación
    if (providerName == null || providerName.trim().isEmpty()) {
        throw new IllegalArgumentException("Provider name is required");
    }

    // ...
}
```

2. **En DTO (Validación de Request):**
```java
public class CreateConformityDeclarationRequestDto {

    @NotNull(message = "assessmentId is required")
    private Long assessmentId;

    @NotBlank(message = "providerName is required")
    @Size(max = 255, message = "providerName must not exceed 255 characters")
    private String providerName;
}
```

---

## 🔒 SEGURIDAD Y AUTENTICACIÓN

### Autenticación

**Nota:** La autenticación se maneja a nivel de gateway/API Gateway. Los microservicios deben validar tokens JWT.

```java
// Ejemplo de validación de token (futuro)
@RestController
public class ConformityDeclarationController {

    @PreAuthorize("hasRole('COMPLIANCE_OFFICER')")
    @PostMapping
    public Mono<ResponseEntity<ConformityDeclarationDto>> createDeclaration(...) {
        // Solo usuarios con rol COMPLIANCE_OFFICER pueden crear
    }
}
```

### Autorización

**Roles y Permisos:**
- **COMPLIANCE_OFFICER:** Crear, firmar, gestionar declaraciones
- **PROJECT_MANAGER:** Ver declaraciones de sus proyectos
- **ADMIN:** Acceso completo

**Validación en Business Service:**
```java
@Transactional
public ConformityDeclaration signDeclaration(Long declarationId, String signedBy) {
    // Validar que el usuario tiene permiso para firmar
    if (!hasPermission(signedBy, "SIGN_DECLARATION")) {
        throw new SecurityException("User does not have permission to sign declarations");
    }

    // Continuar con firma
}
```

---

## 📊 PERFORMANCE

### Optimizaciones de BD

1. **Índices:**
```sql
-- Índice en FK
CREATE INDEX idx_declaration_assessment
ON GOVCONFORMITYDECLARATIONS(IDXCOMPLIANCEASSESSMENT);

-- Índice en status para filtros
CREATE INDEX idx_declaration_status
ON GOVCONFORMITYDECLARATIONS(STATUS);

-- Índice en created_at para ordenamiento
CREATE INDEX idx_declaration_created
ON GOVCONFORMITYDECLARATIONS(CREATEDAT DESC);
```

2. **Queries Optimizadas:**
```java
// Usar JOIN FETCH para evitar N+1
@Query("SELECT cd FROM ConformityDeclaration cd " +
       "JOIN FETCH cd.assessment a " +
       "JOIN FETCH a.project p " +
       "WHERE p.idxproject = :projectId")
List<ConformityDeclaration> findByProjectWithJoins(@Param("projectId") Long projectId);
```

3. **Paginación:**
```java
// Siempre usar paginación para listas grandes
Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").desc());
Page<ConformityDeclaration> declarations = repository.findAll(pageable);
```

### Caché

```java
// Ejemplo de caché para estadísticas
@Cacheable(value = "declarationStatistics", key = "#projectId")
public ConformityDeclarationProjectSummary getProjectDeclarationSummary(Long projectId) {
    // Cálculo costoso
}
```

### Async Processing

```java
// Para operaciones costosas (ej: generación de PDF)
@Async
public CompletableFuture<byte[]> generatePdfAsync(Long declarationId) {
    return CompletableFuture.supplyAsync(() -> {
        // Generación de PDF
    });
}
```

---

## 📝 MIGRACIONES Y CAMBIOS DE ESQUEMA

### Flyway Migrations

**Ubicación:** `src/main/resources/db/migration/`

**Estructura:**
```
V1__create_conformity_declarations_table.sql
V2__add_new_field.sql
V3__add_indexes.sql
```

**Ejemplo de Migración:**
```sql
-- V2__add_pdf_generation_date.sql
ALTER TABLE GOVCONFORMITYDECLARATIONS
ADD COLUMN PDFGENERATEDAT TIMESTAMP;

CREATE INDEX idx_declaration_pdf_generated
ON GOVCONFORMITYDECLARATIONS(PDFGENERATEDAT);
```

### Cambios Breaking

Si se hace un cambio breaking:

1. **Versionar API:**
   - Mantener `/api/v1/conformity-declaration`
   - Crear `/api/v2/conformity-declaration`

2. **Deprecar Gradualmente:**
```java
@Deprecated
@GetMapping("/api/v1/conformity-declaration/{id}")
public Mono<ResponseEntity<ConformityDeclarationDto>> getDeclarationV1(...) {
    // Implementación antigua
}

@GetMapping("/api/v2/conformity-declaration/{id}")
public Mono<ResponseEntity<ConformityDeclarationDtoV2>> getDeclarationV2(...) {
    // Nueva implementación
}
```

---

## 🔗 DEPENDENCIAS ENTRE MÓDULOS

### Compliance Assessment
- **Dependencia:** `ConformityDeclaration` → `ComplianceAssessment`
- **FK:** `IDXCOMPLIANCEASSESSMENT` en `GOVCONFORMITYDECLARATIONS`
- **Impacto:** Cambios en `ComplianceAssessment` pueden afectar declaraciones
- **Validación:** Siempre verificar que assessment existe antes de crear declaración

### Projects
- **Dependencia:** `ConformityDeclaration` → `Project` (vía Assessment)
- **Impacto:** Eliminar proyecto puede afectar declaraciones
- **Estrategia:** Usar `ON DELETE RESTRICT` o `ON DELETE CASCADE` según requerimientos

### EU Registration (Futuro)
- **Dependencia:** `ConformityDeclaration` → `EuRegistration`
- **Impacto:** Publicar declaración en Registro EU
- **Integración:** Endpoint para publicar declaración firmada

---

## 📚 CASOS EDGE Y ERRORES CONOCIDOS

### Casos Edge Documentados

1. **Versiones Concurrentes:**
   - Si dos usuarios crean declaraciones simultáneamente, pueden tener la misma versión
   - **Solución:** Usar `@Transactional(isolation = Isolation.SERIALIZABLE)` o locks optimistas

2. **Assessment Eliminado:**
   - Si se elimina un assessment, las declaraciones quedan huérfanas
   - **Solución:** Implementar `ON DELETE RESTRICT` o `ON DELETE SET NULL` según requerimientos

3. **Transacciones Largas:**
   - Generación de PDF puede tardar mucho
   - **Solución:** Implementar procesamiento asíncrono con polling

4. **Circuit Breaker Abierto:**
   - Si el microservicio falla, el circuit breaker se abre
   - **Solución:** Implementar fallback o retry con exponential backoff

### Errores Conocidos

1. **Race Condition en Versiones:**
   - **Causa:** `countByProjectId()` puede retornar valor incorrecto si hay creación concurrente
   - **Solución:** Usar `SELECT FOR UPDATE` o locks optimistas

2. **Memory Leak en WebClient:**
   - **Causa:** No cerrar conexiones correctamente
   - **Solución:** Usar `WebClient` con connection pool configurado

3. **Timeout en Generación de PDF:**
   - **Causa:** PDF grande o proceso lento
   - **Solución:** Aumentar timeout o implementar async processing

---

## 🔗 REFERENCIAS

- **Documentación Frontend:** `docs/prompts/compliance/conformidad/DEVELOPER_GUIDE_FRONTEND.md`
- **Estado de Implementación:** `docs/prompts/compliance/conformidad/ESTADO_IMPLEMENTACION_CONFORMITY_DECLARATION.md`
- **Guía de Usuario:** `docs/prompts/compliance/conformidad/user_guide/GUIA_FUNCIONAL_CONFORMITY_DECLARATION.md`
- **EU AI Act Art. 48:** EU Declaration of Conformity
- **EU AI Act Annex V:** Template de Declaración
