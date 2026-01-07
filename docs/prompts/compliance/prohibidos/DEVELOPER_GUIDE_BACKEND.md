# 👨‍💻 GUÍA PARA DEVELOPERS BACKEND - PROHIBITED SYSTEMS

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
10. [BPMN Workflow Integration](#bpmn-workflow-integration)
11. [Scripts SQL de Base de Datos](#scripts-sql-de-base-de-datos)

---

## 🏗️ ARQUITECTURA GENERAL

### Capas de la Aplicación

```
Frontend (Next.js)
    ↓
BFF (Backend for Frontend)
    ↓
Business Microservice (Prohibited Systems Service)
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
   - Ubicación: `codeflowx-governance-prohibited-systems-service`
   - Responsabilidad: Endpoints REST para Prohibited Systems
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
- **Controller:** `controller/ProhibitedSystemController.java`
  - Expone endpoints REST para frontend
  - Maneja requests HTTP
  - Retorna DTOs optimizados

- **Service:** `service/ProhibitedSystemService.java` (interface)
  - Define contratos de servicio

- **Service Implementation:** `service/impl/ProhibitedSystemServiceImpl.java`
  - Implementación reactiva con WebClient
  - Circuit Breaker y Retry para resiliencia
  - Solo trabaja con DTOs, nunca con entidades JPA

**Ejemplo de Endpoint BFF:**

```java
@RestController
@RequestMapping("/api/compliance/prohibited-systems")
public class ProhibitedSystemController {

    @GetMapping("/check")
    public Mono<ProhibitedSystemCheckResultDto> checkProhibitedSystem(
            @RequestParam Long projectId) {
        return prohibitedSystemService.checkProhibitedSystem(projectId);
    }
}
```

### 2. Business Microservice Layer

**Ubicación:** `nocode.service/codeflowx-governance-prohibited-systems-service/`

**Componentes:**
- **Controller:** `controller/ProhibitedSystemController.java`
  - Endpoints REST reactivos
  - Mapeo DTO ↔ Entity
  - Llama a Business Services

- **Application:** `ProhibitedSystemServiceApplication.java`
  - Configuración Spring Boot
  - Escaneo de paquetes
  - OpenAPI/Swagger

**Endpoints del Microservicio:**

```
GET  /api/v1/prohibited-systems/check?projectId={id}
POST /api/v1/prohibited-systems/block
GET  /api/v1/prohibited-systems/catalog
GET  /api/v1/prohibited-systems/catalog/{id}
POST /api/v1/prohibited-systems/catalog
PUT  /api/v1/prohibited-systems/catalog/{id}
DELETE /api/v1/prohibited-systems/catalog/{id}
GET  /api/v1/prohibited-systems/{id}
GET  /api/v1/prohibited-systems/health
```

### 3. Business Service Layer

**Ubicación:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/`

**Componentes:**
- **Service:** `ProhibitedSystemBusinessService.java`
  - Lógica de negocio
  - Validaciones
  - Integración con BPMN
  - Usa Repositories JPA

**Ejemplo de Método Business Service:**

```java
@Service
public class ProhibitedSystemBusinessService {

    @Autowired
    private ProhibitedSystemRepository prohibitedSystemRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired(required = false)
    private BpmnWorkflowClient bpmnWorkflowClient;

    public ProhibitedSystemCheckResult checkProhibitedSystem(Long projectId) {
        // 1. Obtener proyecto
        Optional<Project> projectOpt = projectRepository.findById(projectId);
        if (projectOpt.isEmpty()) {
            throw new IllegalArgumentException("Project not found: " + projectId);
        }

        // 2. Obtener sistemas prohibidos activos
        List<ProhibitedSystem> activeSystems =
            prohibitedSystemRepository.findByPrsactiveTrue();

        // 3. Verificar coincidencias por keywords
        List<ProhibitedSystem> detected = new ArrayList<>();
        for (ProhibitedSystem system : activeSystems) {
            if (matchesKeywords(project, system)) {
                detected.add(system);
            }
        }

        // 4. Iniciar workflow BPMN si hay detección
        if (!detected.isEmpty() && bpmnWorkflowClient != null) {
            String workflowInstanceId = bpmnWorkflowClient.startProcess(
                "prohibited-system-detection-workflow",
                createWorkflowVariables(projectId, detected)
            );
        }

        return new ProhibitedSystemCheckResult(detected, !detected.isEmpty());
    }
}
```

### 4. Repository Layer

**Ubicación:** `nocode.service/codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/compliance/`

**Repositorios:**
- `ProhibitedSystemRepository.java` - CRUD de sistemas prohibidos
- `ProjectRepository.java` - CRUD de proyectos (con campos de bloqueo)

**Ejemplo de Repository:**

```java
@Repository
public interface ProhibitedSystemRepository extends JpaRepository<ProhibitedSystem, Long> {

    List<ProhibitedSystem> findByPrsactiveTrue();

    List<ProhibitedSystem> findByPrscategory(String category);

    Optional<ProhibitedSystem> findByPrsname(String name);
}
```

---

## 🔄 FLUJOS DE NEGOCIO

### 1. Verificación de Sistema Prohibido

**Flujo:**
1. Frontend llama a BFF: `GET /api/compliance/prohibited-systems/check?projectId={id}`
2. BFF llama a Microservicio: `GET /api/v1/prohibited-systems/check?projectId={id}`
3. Microservicio llama a Business Service: `checkProhibitedSystem(projectId)`
4. Business Service:
   - Obtiene proyecto de BD
   - Obtiene sistemas prohibidos activos
   - Verifica coincidencias por keywords
   - Inicia workflow BPMN si hay detección
5. Retorna resultado al frontend

**Código:**

```java
// BFF Service
public Mono<ProhibitedSystemCheckResultDto> checkProhibitedSystem(Long projectId) {
    return webClient.get()
        .uri("/api/v1/prohibited-systems/check?projectId={id}", projectId)
        .retrieve()
        .bodyToMono(ProhibitedSystemCheckResultDto.class)
        .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
        .transformDeferred(RetryOperator.of(retry));
}

// Business Service
public ProhibitedSystemCheckResult checkProhibitedSystem(Long projectId) {
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new IllegalArgumentException("Project not found"));

    List<ProhibitedSystem> activeSystems =
        prohibitedSystemRepository.findByPrsactiveTrue();

    List<ProhibitedSystem> detected = activeSystems.stream()
        .filter(system -> matchesKeywords(project, system))
        .collect(Collectors.toList());

    return new ProhibitedSystemCheckResult(detected, !detected.isEmpty());
}
```

### 2. Bloqueo de Despliegue

**Flujo:**
1. Frontend llama a BFF: `POST /api/compliance/prohibited-systems/block`
2. BFF llama a Microservicio: `POST /api/v1/prohibited-systems/block`
3. Microservicio llama a Business Service: `blockDeployment(projectId, reason)`
4. Business Service:
   - Obtiene proyecto de BD
   - Actualiza campos: `PRJDEPLOYMENTBLOCKED = true`, `PRJBLOCKREASON = reason`
   - Guarda proyecto
5. Retorna confirmación al frontend

**Código:**

```java
// Business Service
public void blockDeployment(Long projectId, String reason) {
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new IllegalArgumentException("Project not found"));

    project.setPrjdeploymentblocked(true);
    project.setPrjblockreason(reason);
    project.setUpdatedat(new Timestamp(System.currentTimeMillis()));

    projectRepository.save(project);

    log.info("Deployment blocked for project {}: {}", projectId, reason);
}
```

### 3. Gestión del Catálogo

**Flujo CRUD:**
1. Frontend llama a BFF: `GET /api/compliance/prohibited-systems/catalog`
2. BFF llama a Microservicio: `GET /api/v1/prohibited-systems/catalog`
3. Microservicio llama a Business Service: `getAllProhibitedSystems()`
4. Business Service obtiene todos los sistemas de BD
5. Retorna lista al frontend

**Operaciones:**
- **CREATE:** `POST /api/v1/prohibited-systems/catalog`
- **READ:** `GET /api/v1/prohibited-systems/catalog` y `GET /api/v1/prohibited-systems/catalog/{id}`
- **UPDATE:** `PUT /api/v1/prohibited-systems/catalog/{id}`
- **DELETE:** `DELETE /api/v1/prohibited-systems/catalog/{id}` (soft delete)

---

## 🗄️ ENTIDADES JPA

### ProhibitedSystem

**Ubicación:** `nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/ProhibitedSystem.java`

**Tabla:** `GOVPROHIBITEDSYSTEMS`

**Índices y Constraints:**
- **Clave Única:** `PRSNAME` (nombre debe ser único)
- **Clave Única:** `IDUUID` (UUID estándar)
- **Índice:** `PRSACTIVE` (para búsquedas de sistemas activos)
- **Índice:** `PRSCATEGORY` (para filtros por categoría)
- **Índice Compuesto:** `PRSACTIVE, PRSCATEGORY` (para búsquedas combinadas)
- **Índice:** `PRSCREATEDAT` (para ordenamiento por fecha)
- **Índice GIN:** `PRSKEYWORDS` (JSONB, creado manualmente en SQL - ver scripts)

**Campos principales:**

```java
@Entity
@Table(
    name = "GOVPROHIBITEDSYSTEMS",
    indexes = {
        @Index(name = "idx_prohibited_systems_name", columnList = "PRSNAME", unique = true),
        @Index(name = "idx_prohibited_systems_active", columnList = "PRSACTIVE"),
        @Index(name = "idx_prohibited_systems_category", columnList = "PRSCATEGORY"),
        @Index(name = "idx_prohibited_systems_active_category", columnList = "PRSACTIVE,PRSCATEGORY"),
        @Index(name = "idx_prohibited_systems_created_at", columnList = "PRSCREATEDAT")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_prohibited_systems_uuid", columnNames = {"IDUUID"}),
        @UniqueConstraint(name = "uk_prohibited_systems_name", columnNames = {"PRSNAME"})
    }
)
public class ProhibitedSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXPROHIBITEDSYSTEM", nullable = false)
    private Long idxprohibitedsystem;

    @NotNull
    @Size(max = 36)
    @Column(name = "IDUUID", unique = true, nullable = false, length = 36)
    private String iduuid;

    @NotNull
    @Size(max = 255)
    @Column(name = "PRSNAME", nullable = false, length = 255)
    private String prsname;

    @Column(name = "PRSDESCRIPTION", columnDefinition = "TEXT")
    private String prsdescription;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "PRSCATEGORY", nullable = false, length = 50)
    private ProhibitedSystemCategory prscategory;

    @Type(type = "jsonb")
    @Column(name = "PRSKEYWORDS", columnDefinition = "JSONB")
    private List<String> prskeywords;

    @NotNull
    @Column(name = "PRSACTIVE", nullable = false)
    private Boolean prsactive = true;

    // ... campos de auditoría
}
```

### Project (Campos Agregados)

**Ubicación:** `nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/projects/Project.java`

**Índices Agregados:**
- **Índice:** `PRJDEPLOYMENTBLOCKED` (para búsquedas de proyectos bloqueados)
- **Índice:** `PRJPROHIBITEDUSECHECKED` (para búsquedas de proyectos verificados)
- **Índice Compuesto:** `PRJDEPLOYMENTBLOCKED, PRJPROHIBITEDUSECHECKED` (para búsquedas combinadas)

**Campos agregados:**

```java
@Entity
@Table(
    name = "PRJPROJECTS",
    indexes = {
        @Index(name = "idx_projects_deployment_blocked", columnList = "PRJDEPLOYMENTBLOCKED"),
        @Index(name = "idx_projects_prohibited_use_checked", columnList = "PRJPROHIBITEDUSECHECKED"),
        @Index(name = "idx_projects_blocked_checked", columnList = "PRJDEPLOYMENTBLOCKED,PRJPROHIBITEDUSECHECKED")
    }
)
public class Project {
    // ... campos existentes ...

    // Bloqueo de Despliegue por Sistemas Prohibidos (Art. 5)
    @Column(name = "PRJDEPLOYMENTBLOCKED", nullable = true)
    @Field(criteria = true, auditar = true, filter = true, label = "Despliegue Bloqueado", type = "BOOLEAN")
    private Boolean prjdeploymentblocked;

    @Size(min = 0, max = 500)
    @Column(name = "PRJBLOCKREASON", nullable = true, length = 500)
    @Field(criteria = true, auditar = true, filter = true, label = "Razón del Bloqueo", type = "VARCHAR")
    private String prjblockreason;
}
```

**Nota:** JPA actualizará automáticamente la base de datos al iniciar la aplicación si `ddl-auto: update` está configurado.

---

## 📦 DTOs

**Ubicación:** `nocode.service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/dto/compliance/`

**DTOs Implementados:**

1. **ProhibitedSystemDto**
   - Representa un sistema prohibido del catálogo
   - Usado en listados y detalles

2. **ProhibitedSystemCheckResultDto**
   - Resultado de verificación de sistema prohibido
   - Incluye: `hasProhibitedSystem`, `systems`, `confidence`, `matchedKeywords`

3. **ProhibitedSystemActionResponseDto**
   - Respuesta de acciones (bloquear, desbloquear, etc.)
   - Incluye: `success`, `message`, `timestamp`

4. **ProhibitedSystemDetectionDetailDto**
   - Detalle completo de una detección
   - Incluye: información del proyecto, sistema detectado, keywords, evidencia

5. **BlockDeploymentRequestDto**
   - Request para bloquear despliegue
   - Incluye: `projectId`, `reason`

**Regla Importante:**
- ✅ **SÍ:** Usar DTOs en BFF y Controllers
- ❌ **NO:** Usar entidades JPA en BFF o Controllers
- ✅ **SÍ:** Mapear DTO ↔ Entity en el Microservicio

---

## 🔧 CONFIGURACIÓN

### application.yml (BFF)

**Ubicación:** `nocode.service/codeflowx.govern.bff.compliance/src/main/resources/application.yml`

```yaml
services:
  prohibited-systems:
    url: http://localhost:8101

resilience4j:
  circuitbreaker:
    instances:
      prohibitedSystemsCircuitBreaker:
        registerHealthIndicator: true
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        permittedNumberOfCallsInHalfOpenState: 3
        waitDurationInOpenState: 10s
        failureRateThreshold: 50
        eventConsumerBufferSize: 10

  retry:
    instances:
      prohibitedSystemsRetry:
        maxAttempts: 3
        waitDuration: 1s
        retryExceptions:
          - org.springframework.web.reactive.function.client.WebClientResponseException
```

### application.yml (Microservicio)

**Ubicación:** `nocode.service/codeflowx-governance-prohibited-systems-service/src/main/resources/application.yml`

```yaml
spring:
  application:
    name: prohibited-systems-service

  server:
    port: 8101

  datasource:
    url: jdbc:postgresql://localhost:5432/governance
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true

management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus
```

---

## 🔄 BPMN WORKFLOW INTEGRATION

### Iniciar Workflow

**Código:**

```java
@Autowired(required = false)
private BpmnWorkflowClient bpmnWorkflowClient;

public ProhibitedSystemCheckResult checkProhibitedSystem(Long projectId) {
    // ... lógica de detección ...

    if (!detected.isEmpty() && bpmnWorkflowClient != null) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("projectId", projectId);
        variables.put("detectedSystems", detected.stream()
            .map(ProhibitedSystem::getPrsname)
            .collect(Collectors.toList()));
        variables.put("detectionDate", LocalDateTime.now().toString());

        String workflowInstanceId = bpmnWorkflowClient.startProcess(
            "prohibited-system-detection-workflow",
            variables
        );

        log.info("BPMN workflow started: {}", workflowInstanceId);
    }

    return result;
}
```

### Workflow ID

**Process ID:** `prohibited-system-detection-workflow`

**Ubicación:** `nocode.service/codeflowx.govern.workflow.lib/src/main/resources/processes/compliance/prohibited-system-detection-workflow.bpmn20.xml`

**Ver:** `BPMN_WORKFLOW_GUIDE.md` para documentación completa del workflow.

---

## ✅ VALIDACIONES Y REGLAS

### Validaciones de Negocio

1. **Verificación de Proyecto:**
   - El proyecto debe existir
   - El proyecto debe estar activo

2. **Detección por Keywords:**
   - Búsqueda case-insensitive
   - Coincidencias en nombre y descripción del proyecto
   - Múltiples keywords pueden coincidir

3. **Bloqueo de Despliegue:**
   - Solo se puede bloquear si hay sistema prohibido detectado
   - La razón del bloqueo es obligatoria (máx. 500 caracteres)

4. **Catálogo:**
   - El nombre del sistema es obligatorio y único
   - La categoría debe ser válida (Art. 5.1.a, b, c, d)
   - Los keywords deben ser una lista no vacía

---

## 🚀 EJEMPLOS DE USO

### Ejemplo 1: Verificar Sistema Prohibido

```java
// En el Controller del Microservicio
@GetMapping("/check")
public Mono<ProhibitedSystemCheckResultDto> checkProhibitedSystem(
        @RequestParam Long projectId) {
    return Mono.fromCallable(() -> {
        ProhibitedSystemBusinessService.ProhibitedSystemCheckResult result =
            businessService.checkProhibitedSystem(projectId);
        return mapToDto(result);
    }).subscribeOn(Schedulers.boundedElastic());
}
```

### Ejemplo 2: Bloquear Despliegue

```java
// En el Controller del Microservicio
@PostMapping("/block")
public Mono<ProhibitedSystemActionResponseDto> blockDeployment(
        @RequestBody BlockDeploymentRequestDto request) {
    return Mono.fromCallable(() -> {
        businessService.blockDeployment(request.getProjectId(), request.getReason());
        return new ProhibitedSystemActionResponseDto(true, "Deployment blocked", LocalDateTime.now());
    }).subscribeOn(Schedulers.boundedElastic());
}
```

---

## 📜 SCRIPTS SQL DE BASE DE DATOS

### Ubicación de Scripts

Todos los scripts SQL están ubicados en:
```
codeflowx-studio/docs/prompts/compliance/prohibidos/sql/
```

### Scripts Disponibles

1. **`01_indexes.sql`** - Índices adicionales (GIN para JSONB)
2. **`02_triggers.sql`** - Triggers de auditoría
3. **`03_functions.sql`** - Funciones de búsqueda y utilidades

### Ejecución de Scripts

**Importante:** Estos scripts deben ejecutarse **después** de que JPA haya creado las tablas base. JPA creará automáticamente los índices definidos en las anotaciones `@Index`, pero algunos índices especiales (como GIN para JSONB) y objetos de BD (triggers, funciones) deben crearse manualmente.

**Orden de Ejecución:**
1. Iniciar aplicación (JPA crea tablas e índices básicos)
2. Ejecutar `01_indexes.sql` (índices adicionales)
3. Ejecutar `02_triggers.sql` (triggers)
4. Ejecutar `03_functions.sql` (funciones)

**Ver:** Directorio `sql/` para scripts completos y documentación detallada.

---

## ⚠️ MANEJO DE EXCEPCIONES

### Excepciones en Business Service

El `ProhibitedSystemBusinessService` puede lanzar las siguientes excepciones:

1. **IllegalArgumentException**
   - Cuando los parámetros de entrada son inválidos
   - Ejemplo: `projectId` es null, `reason` es vacío o excede 500 caracteres
   - Ubicación: Validaciones en métodos `checkProhibitedSystem()`, `blockDeployment()`, `createProhibitedSystem()`

2. **EntityNotFoundException** (implícito)
   - Cuando un proyecto o sistema prohibido no existe
   - Ejemplo: `projectRepository.findById()` retorna `Optional.empty()`
   - Ubicación: `checkProhibitedSystem()`, `blockDeployment()`, `updateProhibitedSystem()`, `deleteProhibitedSystem()`

3. **RuntimeException**
   - Errores genéricos durante operaciones de negocio
   - Ejemplo: Error al consultar repositorios, error al guardar entidades
   - Ubicación: Cualquier método del business service

### Manejo de Excepciones en Controller

El `ProhibitedSystemController` (microservicio) maneja excepciones de la siguiente manera:

```java
@GetMapping("/check")
public Mono<ResponseEntity<ProhibitedSystemCheckResultDto>> checkProhibitedSystem(
        @RequestParam Long projectId) {
    return Mono.fromCallable(() -> {
        ProhibitedSystemBusinessService.ProhibitedSystemCheckResult result =
            businessService.checkProhibitedSystem(projectId);
        return mapToDto(result);
    })
    .subscribeOn(Schedulers.boundedElastic())
    .map(ResponseEntity::ok)
    .onErrorResume(IllegalArgumentException.class, error -> {
        log.warn("Invalid request: {}", error.getMessage());
        return Mono.just(ResponseEntity.<ProhibitedSystemCheckResultDto>badRequest().build());
    })
    .onErrorResume(error -> {
        log.error("Error checking prohibited systems", error);
        return Mono.just(ResponseEntity.<ProhibitedSystemCheckResultDto>status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    });
}
```

### Manejo de Excepciones en BFF Service

El `ProhibitedSystemServiceImpl` (BFF) maneja excepciones de WebClient:

```java
public Mono<ProhibitedSystemCheckResultDto> checkProhibitedSystem(Long projectId) {
    return webClient.get()
        .uri("/api/v1/prohibited-systems/check?projectId={id}", projectId)
        .retrieve()
        .bodyToMono(ProhibitedSystemCheckResultDto.class)
        .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
        .transformDeferred(RetryOperator.of(retry))
        .onErrorResume(WebClientResponseException.NotFound.class, error -> {
            log.warn("Project not found: {}", projectId);
            return Mono.just(new ProhibitedSystemCheckResultDto()); // Retornar resultado vacío
        })
        .onErrorResume(WebClientResponseException.BadRequest.class, error -> {
            log.warn("Invalid request: {}", error.getMessage());
            return Mono.error(new IllegalArgumentException("Invalid project ID"));
        })
        .onErrorResume(error -> {
            log.error("Error calling prohibited-systems service", error);
            return Mono.error(new RuntimeException("Service unavailable", error));
        });
}
```

### Códigos HTTP Retornados

| Situación | Código HTTP | Descripción |
|-----------|-------------|-------------|
| Éxito | 200 OK | Operación completada exitosamente |
| Request inválido | 400 Bad Request | Parámetros inválidos o validación fallida |
| No encontrado | 404 Not Found | Proyecto o sistema prohibido no existe |
| Error interno | 500 Internal Server Error | Error no manejado en el servidor |
| Servicio no disponible | 503 Service Unavailable | Circuit breaker abierto o servicio caído |

### Excepciones de Integración

#### BPMN Workflow Client
- **No bloquea la operación principal**: Si el workflow falla, se registra en logs pero la detección se guarda
- **Manejo**: Try-catch en métodos que disparan workflows

```java
try {
    String workflowInstanceId = bpmnWorkflowClient.startProcess(
        "prohibited-system-detection-workflow",
        variables
    );
    log.info("BPMN workflow started: {}", workflowInstanceId);
} catch (Exception e) {
    log.error("Error al disparar workflow BPMN", e);
    // No fallar la operación si el workflow falla
}
```

#### Circuit Breaker y Retry
- **Circuit Breaker**: Abre cuando el failure rate supera el threshold (50%)
- **Retry**: Reintenta hasta 3 veces con delay de 1 segundo
- **Fallback**: Retorna error o valor por defecto según configuración

---

## 🧪 TESTING

### Unit Tests

**Ubicación:** `nocode.service/codeflowx.govern.business/src/test/java/com/codeflowx/govern/business/compliance/`

#### Ejemplo: Test de Business Service

```java
@ExtendWith(MockitoExtension.class)
class ProhibitedSystemBusinessServiceTest {
    @Mock
    private ProhibitedSystemRepository prohibitedSystemRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private BpmnWorkflowClient bpmnWorkflowClient;

    @InjectMocks
    private ProhibitedSystemBusinessService businessService;

    @Test
    void testCheckProhibitedSystem_NoProhibitedSystems() {
        // Arrange
        Long projectId = 1L;
        Project project = createMockProject(projectId, "Test Project", "Test Description");
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(prohibitedSystemRepository.findByPrsactiveTrueOrderByPrsname())
            .thenReturn(Collections.emptyList());

        // Act
        ProhibitedSystemBusinessService.ProhibitedSystemCheckResult result =
            businessService.checkProhibitedSystem(projectId);

        // Assert
        assertNotNull(result);
        assertFalse(result.getHasProhibitedSystem());
        assertEquals(0, result.getDetectedSystems().size());
        assertEquals(1.0, result.getConfidence()); // 100% de confianza si no hay detección
    }

    @Test
    void testCheckProhibitedSystem_ProhibitedSystemDetected() {
        // Arrange
        Long projectId = 1L;
        Project project = createMockProject(projectId, "Social Scoring System", "Behavioral evaluation");
        ProhibitedSystem system = createMockProhibitedSystem("Social Scoring",
            ProhibitedSystem.ProhibitedSystemCategory.ART_5_1_C,
            Arrays.asList("social scoring", "behavioral evaluation"));

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(prohibitedSystemRepository.findByPrsactiveTrueOrderByPrsname())
            .thenReturn(Arrays.asList(system));
        when(bpmnWorkflowClient.startProcess(anyString(), anyMap()))
            .thenReturn("workflow-instance-123");

        // Act
        ProhibitedSystemBusinessService.ProhibitedSystemCheckResult result =
            businessService.checkProhibitedSystem(projectId);

        // Assert
        assertNotNull(result);
        assertTrue(result.getHasProhibitedSystem());
        assertEquals(1, result.getDetectedSystems().size());
        assertEquals("Social Scoring", result.getDetectedSystems().get(0).getPrsname());
        assertTrue(result.getMatchedKeywords().contains("social scoring"));
        assertTrue(result.getMatchedKeywords().contains("behavioral evaluation"));
    }

    @Test
    void testCheckProhibitedSystem_ProjectNotFound() {
        // Arrange
        Long projectId = 999L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            businessService.checkProhibitedSystem(projectId);
        });
    }

    @Test
    void testBlockDeployment() {
        // Arrange
        Long projectId = 1L;
        String reason = "Sistema prohibido detectado: Social Scoring";
        Project project = createMockProject(projectId, "Test Project", "Description");
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        businessService.blockDeployment(projectId, reason);

        // Assert
        assertTrue(project.getPrjdeploymentblocked());
        assertEquals(reason, project.getPrjblockreason());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void testBlockDeployment_ProjectNotFound() {
        // Arrange
        Long projectId = 999L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            businessService.blockDeployment(projectId, "Reason");
        });
    }

    @Test
    void testCreateProhibitedSystem() {
        // Arrange
        String name = "Test System";
        ProhibitedSystem.ProhibitedSystemCategory category =
            ProhibitedSystem.ProhibitedSystemCategory.ART_5_1_C;
        String description = "Test description";
        List<String> keywords = Arrays.asList("keyword1", "keyword2");
        String createdBy = "user123";

        when(prohibitedSystemRepository.save(any(ProhibitedSystem.class)))
            .thenAnswer(invocation -> {
                ProhibitedSystem system = invocation.getArgument(0);
                system.setIdxprohibitedsystem(1L);
                return system;
            });

        // Act
        ProhibitedSystem result = businessService.createProhibitedSystem(
            name, category, description, keywords, createdBy);

        // Assert
        assertNotNull(result);
        assertEquals(name, result.getPrsname());
        assertEquals(category, result.getPrscategory());
        assertEquals(description, result.getPrsdescription());
        assertEquals(keywords, result.getPrskeywords());
        assertTrue(result.getPrsactive());
        assertEquals(createdBy, result.getPrscreatedby());
        verify(prohibitedSystemRepository, times(1)).save(any(ProhibitedSystem.class));
    }

    // Helper methods
    private Project createMockProject(Long id, String name, String description) {
        Project project = new Project();
        project.setIdxproject(id);
        project.setName(name);
        project.setDescription(description);
        project.setIsactive(true);
        return project;
    }

    private ProhibitedSystem createMockProhibitedSystem(String name,
            ProhibitedSystem.ProhibitedSystemCategory category, List<String> keywords) {
        ProhibitedSystem system = new ProhibitedSystem();
        system.setIdxprohibitedsystem(1L);
        system.setPrsname(name);
        system.setPrscategory(category);
        system.setPrskeywords(keywords);
        system.setPrsactive(true);
        return system;
    }
}
```

### Integration Tests

**Ubicación:** `nocode.service/codeflowx-governance-prohibited-systems-service/src/test/java/com/codeflowx/govern/prohibitedsystems/controller/`

#### Ejemplo: Test de Controller

```java
@SpringBootTest
@AutoConfigureMockMvc
class ProhibitedSystemControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProhibitedSystemBusinessService businessService;

    @Test
    void testCheckProhibitedSystem_Success() throws Exception {
        // Arrange
        Long projectId = 1L;
        ProhibitedSystemBusinessService.ProhibitedSystemCheckResult mockResult =
            createMockCheckResult(false, Collections.emptyList());
        when(businessService.checkProhibitedSystem(projectId)).thenReturn(mockResult);

        // Act & Assert
        mockMvc.perform(get("/api/v1/prohibited-systems/check")
                .param("projectId", projectId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.detected").value(false))
                .andExpect(jsonPath("$.systems").isEmpty());
    }

    @Test
    void testCheckProhibitedSystem_ProjectNotFound() throws Exception {
        // Arrange
        Long projectId = 999L;
        when(businessService.checkProhibitedSystem(projectId))
            .thenThrow(new IllegalArgumentException("Project not found: " + projectId));

        // Act & Assert
        mockMvc.perform(get("/api/v1/prohibited-systems/check")
                .param("projectId", projectId.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testBlockDeployment_Success() throws Exception {
        // Arrange
        Long projectId = 1L;
        String reason = "Sistema prohibido detectado";
        BlockDeploymentRequestDto request = new BlockDeploymentRequestDto();
        request.setProjectId(projectId);
        request.setReason(reason);

        doNothing().when(businessService).blockDeployment(projectId, reason);

        // Act & Assert
        mockMvc.perform(post("/api/v1/prohibited-systems/block")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetCatalog_Success() throws Exception {
        // Arrange
        List<ProhibitedSystem> systems = Arrays.asList(
            createMockProhibitedSystem(1L, "System 1"),
            createMockProhibitedSystem(2L, "System 2")
        );
        when(businessService.getActiveProhibitedSystems()).thenReturn(systems);

        // Act & Assert
        mockMvc.perform(get("/api/v1/prohibited-systems/catalog"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // Helper methods
    private ProhibitedSystemBusinessService.ProhibitedSystemCheckResult createMockCheckResult(
            boolean detected, List<ProhibitedSystem> systems) {
        return new ProhibitedSystemBusinessService.ProhibitedSystemCheckResult(systems, detected);
    }

    private ProhibitedSystem createMockProhibitedSystem(Long id, String name) {
        ProhibitedSystem system = new ProhibitedSystem();
        system.setIdxprohibitedsystem(id);
        system.setPrsname(name);
        system.setPrsactive(true);
        return system;
    }
}
```

### Testing de Mapeo DTO

```java
@Test
void testMapToCheckResultDto() {
    // Arrange
    ProhibitedSystem system = createMockProhibitedSystem(1L, "Social Scoring");
    List<ProhibitedSystem> detected = Arrays.asList(system);
    ProhibitedSystemBusinessService.ProhibitedSystemCheckResult result =
        new ProhibitedSystemBusinessService.ProhibitedSystemCheckResult(detected, true);

    ProhibitedSystemController controller = new ProhibitedSystemController(mockBusinessService);

    // Act
    ProhibitedSystemCheckResultDto dto = controller.mapToCheckResultDto(result);

    // Assert
    assertNotNull(dto);
    assertTrue(dto.getDetected());
    assertEquals(1, dto.getSystems().size());
    assertEquals("Social Scoring", dto.getSystems().get(0).getName());
}
```

---

## 🔧 TROUBLESHOOTING Y PROBLEMAS COMUNES

### Problema 1: Error "Project not found"

**Síntoma:** Al verificar un proyecto, se recibe 400 Bad Request o 404 Not Found.

**Causas posibles:**
- El proyecto no existe en la base de datos
- El `projectId` es incorrecto o null
- El proyecto fue eliminado

**Solución:**
1. Verificar que el proyecto existe: `SELECT * FROM PRJPROJECTS WHERE IDXPROJECT = ?`
2. Verificar que el `projectId` no es null en el request
3. Revisar logs del business service: `IllegalArgumentException: Project not found: {id}`
4. Verificar que el proyecto está activo: `SELECT * FROM PRJPROJECTS WHERE IDXPROJECT = ? AND ISACTIVE = true`

---

### Problema 2: Sistema Prohibido no se detecta

**Síntoma:** Un proyecto debería ser detectado como prohibido pero no lo es.

**Causas posibles:**
- Los keywords del sistema prohibido no coinciden con el nombre/descripción del proyecto
- El sistema prohibido está inactivo (`PRSACTIVE = false`)
- La búsqueda es case-sensitive (aunque debería ser case-insensitive)

**Solución:**
1. Verificar que el sistema prohibido está activo:
   ```sql
   SELECT * FROM GOVPROHIBITEDSYSTEMS WHERE IDXPROHIBITEDSYSTEM = ? AND PRSACTIVE = true;
   ```
2. Verificar los keywords del sistema:
   ```sql
   SELECT PRSKEYWORDS FROM GOVPROHIBITEDSYSTEMS WHERE IDXPROHIBITEDSYSTEM = ?;
   ```
3. Verificar el nombre y descripción del proyecto:
   ```sql
   SELECT NAME, DESCRIPTION FROM PRJPROJECTS WHERE IDXPROJECT = ?;
   ```
4. Revisar logs de detección: `Keyword match found: '{keyword}' in project {id}`
5. Verificar que la búsqueda es case-insensitive (debería serlo por defecto)

---

### Problema 3: Workflow BPMN no se dispara

**Síntoma:** Se detecta un sistema prohibido pero el workflow BPMN no se ejecuta.

**Causas posibles:**
- `BpmnWorkflowClient` no está configurado (`@Autowired(required = false)`)
- El servicio BPMN no está disponible
- Error en las variables del workflow
- El workflow no existe o el Process ID es incorrecto

**Solución:**
1. Verificar que `BpmnWorkflowClient` está inyectado: Revisar logs de inicio de aplicación
2. Verificar conectividad con servicio BPMN: `curl http://bpmn-service:port/health`
3. Verificar que el Process ID es correcto: `prohibited-system-detection-workflow`
4. Revisar logs: `Error al disparar workflow BPMN` o `BPMN workflow started: {instanceId}`
5. **Nota:** El workflow es opcional, la detección se guarda aunque falle el workflow

---

### Problema 4: Bloqueo de despliegue no funciona

**Síntoma:** Se intenta bloquear un despliegue pero el campo `PRJDEPLOYMENTBLOCKED` no se actualiza.

**Causas posibles:**
- El proyecto no existe
- Error al guardar en la base de datos
- Transacción no se completa
- JPA no actualiza el campo

**Solución:**
1. Verificar que el proyecto existe antes de bloquear
2. Verificar que la transacción se completa: Revisar logs `Deployment blocked for project {}: {}`
3. Verificar en BD después del bloqueo:
   ```sql
   SELECT PRJDEPLOYMENTBLOCKED, PRJBLOCKREASON FROM PRJPROJECTS WHERE IDXPROJECT = ?;
   ```
4. Verificar que JPA tiene `@Transactional` en el método (si aplica)
5. Revisar logs de errores de JPA

---

### Problema 5: Circuit Breaker abierto

**Síntoma:** El BFF retorna errores 503 Service Unavailable aunque el microservicio esté funcionando.

**Causas posibles:**
- El Circuit Breaker está abierto debido a muchos fallos previos
- El microservicio estuvo caído y el Circuit Breaker se abrió
- El failure rate superó el threshold (50%)

**Solución:**
1. Verificar estado del Circuit Breaker:
   ```yaml
   # En application.yml, verificar configuración
   resilience4j:
     circuitbreaker:
       instances:
         prohibitedSystemsCircuitBreaker:
           failureRateThreshold: 50
           waitDurationInOpenState: 10s
   ```
2. Verificar métricas del Circuit Breaker: `/actuator/health` o `/actuator/metrics`
3. Esperar el tiempo de `waitDurationInOpenState` (10 segundos) para que pase a half-open
4. Reiniciar el servicio BFF si es necesario
5. Verificar que el microservicio está disponible: `curl http://localhost:8101/health`

---

### Problema 6: Keywords no coinciden correctamente

**Síntoma:** Los keywords deberían coincidir pero no lo hacen, o coinciden cuando no deberían.

**Causas posibles:**
- La búsqueda es demasiado estricta o demasiado laxa
- Los keywords contienen caracteres especiales
- El texto del proyecto tiene formato especial (HTML, markdown, etc.)

**Solución:**
1. Revisar el método `matchesKeywords()` en `ProhibitedSystemBusinessService`
2. Verificar que la búsqueda es case-insensitive (debería serlo)
3. Verificar que se busca en nombre Y descripción del proyecto
4. Considerar normalizar el texto (eliminar HTML, markdown, etc.)
5. Revisar logs de detección para ver qué keywords se están comparando

---

### Problema 7: Error al crear sistema prohibido

**Síntoma:** Al crear un sistema prohibido, se recibe error de validación o constraint violation.

**Causas posibles:**
- El nombre del sistema ya existe (debe ser único)
- La categoría no es válida
- Los keywords están vacíos
- Campos obligatorios son null

**Solución:**
1. Verificar que el nombre es único:
   ```sql
   SELECT * FROM GOVPROHIBITEDSYSTEMS WHERE PRSNAME = ?;
   ```
2. Verificar que la categoría es válida: `ART_5_1_A`, `ART_5_1_B`, `ART_5_1_C`, `ART_5_1_D`
3. Verificar que los keywords no están vacíos
4. Verificar que todos los campos obligatorios están presentes:
   - `PRSNAME` (obligatorio)
   - `PRSCATEGORY` (obligatorio)
   - `PRSKEYWORDS` (debe tener al menos un keyword)
   - `PRSACTIVE` (obligatorio, default: true)

---

## 🚀 GUÍA PARA IMPLEMENTAR NUEVAS FUNCIONALIDADES

### Paso 1: Definir la Funcionalidad

1. **Identificar el flujo:**
   - ¿Qué entidad JPA se necesita?
   - ¿Qué repositorio se necesita?
   - ¿Qué validaciones se requieren?

2. **Definir el contrato:**
   - ¿Qué DTOs se necesitan?
   - ¿Qué endpoints REST se necesitan?

### Paso 2: Implementar en Business Service

1. **Crear método en `ProhibitedSystemBusinessService`:**

```java
@Transactional
public ProhibitedSystemNewData newFunctionality(Long projectId, String param) {
    // 1. Validar parámetros
    if (projectId == null) {
        throw new IllegalArgumentException("projectId is required");
    }

    // 2. Consultar repositorios
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new IllegalArgumentException("Project not found: " + projectId));

    // 3. Lógica de negocio
    // ...

    // 4. Retornar clase de datos interna (NO DTO)
    return new ProhibitedSystemNewData();
}

// Clase de datos interna
public static class ProhibitedSystemNewData {
    public Long id;
    public String field1;
    // ... más campos
}
```

### Paso 3: Implementar en Controller del Microservicio

1. **Crear endpoint en `ProhibitedSystemController`:**

```java
@GetMapping("/new-endpoint")
@Operation(summary = "Nueva funcionalidad")
public Mono<ResponseEntity<ProhibitedSystemNewDto>> newEndpoint(
        @RequestParam Long projectId,
        @RequestParam String param) {
    return Mono.fromCallable(() ->
            businessService.newFunctionality(projectId, param))
        .subscribeOn(Schedulers.boundedElastic())
        .map(data -> {
            ProhibitedSystemNewDto dto = mapToNewDto(data);
            return ResponseEntity.ok(dto);
        })
        .onErrorResume(IllegalArgumentException.class, error -> {
            log.warn("Invalid request: {}", error.getMessage());
            return Mono.just(ResponseEntity.<ProhibitedSystemNewDto>badRequest().build());
        })
        .onErrorResume(error -> {
            log.error("Error in new functionality", error);
            return Mono.just(ResponseEntity
                .<ProhibitedSystemNewDto>status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        });
}

// Método de mapeo
private ProhibitedSystemNewDto mapToNewDto(ProhibitedSystemBusinessService.ProhibitedSystemNewData data) {
    ProhibitedSystemNewDto dto = new ProhibitedSystemNewDto();
    dto.setId(data.id);
    dto.setField1(data.field1);
    return dto;
}
```

### Paso 4: Implementar en BFF Service

1. **Agregar método en `ProhibitedSystemService` (interface):**

```java
Mono<ProhibitedSystemNewDto> newFunctionality(Long projectId, String param);
```

2. **Implementar en `ProhibitedSystemServiceImpl`:**

```java
@Override
public Mono<ProhibitedSystemNewDto> newFunctionality(Long projectId, String param) {
    return webClient.get()
        .uri("/api/v1/prohibited-systems/new-endpoint?projectId={id}&param={param}",
            projectId, param)
        .retrieve()
        .bodyToMono(ProhibitedSystemNewDto.class)
        .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
        .transformDeferred(RetryOperator.of(retry))
        .onErrorResume(error -> {
            log.error("Error calling prohibited-systems service", error);
            return Mono.error(error);
        });
}
```

3. **Agregar endpoint en `ProhibitedSystemController` (BFF):**

```java
@GetMapping("/new-endpoint")
public Mono<ProhibitedSystemNewDto> newEndpoint(
        @RequestParam Long projectId,
        @RequestParam String param) {
    return prohibitedSystemService.newFunctionality(projectId, param);
}
```

### Paso 5: Crear DTO

1. **Crear DTO en módulo de DTOs:**

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProhibitedSystemNewDto {
    private Long id;
    private String field1;
    // ... más campos
}
```

2. **Ubicación:** `nocode.service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/dto/compliance/`

### Paso 6: Integrar BPMN (si es necesario)

```java
if (bpmnWorkflowClient != null) {
    try {
        Map<String, Object> variables = Map.of(
            "projectId", projectId,
            "param", param
        );
        String workflowInstanceId = bpmnWorkflowClient.startProcess(
            "new-prohibited-system-workflow",
            variables
        );
        log.info("BPMN workflow started: {}", workflowInstanceId);
    } catch (Exception e) {
        log.error("Error al disparar workflow BPMN", e);
        // No fallar la operación si el workflow falla
    }
}
```

### Paso 7: Testing

1. **Unit Test del Business Service:**

```java
@Test
void testNewFunctionality() {
    // Arrange
    when(projectRepository.findById(1L))
        .thenReturn(Optional.of(createMockProject()));

    // Act
    ProhibitedSystemBusinessService.ProhibitedSystemNewData result =
        businessService.newFunctionality(1L, "test");

    // Assert
    assertNotNull(result);
    assertEquals("test", result.field1);
}
```

2. **Integration Test del Controller:**

```java
@Test
void testNewEndpoint() throws Exception {
    mockMvc.perform(get("/api/v1/prohibited-systems/new-endpoint")
            .param("projectId", "1")
            .param("param", "test"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.field1").value("test"));
}
```

### Checklist de Implementación

- [ ] Método implementado en Business Service
- [ ] Clase de datos interna creada (si es necesaria)
- [ ] Endpoint REST creado en Controller del microservicio
- [ ] Método de mapeo DTO implementado
- [ ] Endpoint BFF creado
- [ ] Método en BFF Service implementado
- [ ] DTO creado en módulo de DTOs
- [ ] Validaciones implementadas
- [ ] Manejo de excepciones implementado
- [ ] Integración BPMN (si aplica)
- [ ] Unit tests escritos
- [ ] Integration tests escritos
- [ ] Documentación actualizada

---

## 🎯 MEJORES PRÁCTICAS

### 1. **Separación de Capas**
- Business Services trabajan con Entidades JPA
- Controllers trabajan con DTOs
- Conversión DTO ↔ Entity en controllers del microservicio, no en business services
- BFF solo trabaja con DTOs y WebClient

### 2. **Manejo de Errores**
- Usar excepciones específicas (`IllegalArgumentException` para validaciones)
- No exponer detalles internos en DTOs
- Logging apropiado con niveles correctos (INFO, WARN, ERROR)
- Manejar errores de integración (BPMN, Circuit Breaker) sin fallar operaciones principales

### 3. **Transacciones**
- Usar `@Transactional` en business services para operaciones que modifican BD
- No usar transacciones en controllers reactivos (WebFlux)
- Usar `Mono.fromCallable().subscribeOn(Schedulers.boundedElastic())` para llamadas síncronas

### 4. **BPMN Workflows**
- Manejar errores de BPMN sin fallar la operación principal
- Logging de errores de workflows
- Workflow es opcional (`@Autowired(required = false)`)

### 5. **Resiliencia**
- Usar Circuit Breaker para proteger contra servicios caídos
- Usar Retry para reintentar llamadas fallidas
- Configurar timeouts apropiados
- Monitorear métricas de Circuit Breaker y Retry

### 6. **Keywords Matching**
- Búsqueda case-insensitive
- Buscar en múltiples campos del proyecto (nombre, descripción)
- Considerar normalización de texto (eliminar HTML, markdown)
- Logging de keywords coincidentes para debugging

### 7. **Validaciones**
- Validar parámetros de entrada en business services
- Usar `@Valid` en controllers para validación de DTOs
- Validar existencia de entidades antes de operar
- Validar reglas de negocio (ej: solo bloquear si hay detección)

---

## 📊 PERFORMANCE Y OPTIMIZACIÓN

### Consultas Optimizadas

#### Repository Queries

```java
@Repository
public interface ProhibitedSystemRepository extends JpaRepository<ProhibitedSystem, Long> {

    // ✅ Usar índices en campos de búsqueda frecuente
    @Query("SELECT p FROM ProhibitedSystem p WHERE p.prsactive = true ORDER BY p.prsname")
    List<ProhibitedSystem> findByPrsactiveTrueOrderByPrsname();

    // ✅ Búsqueda por categoría (índice recomendado en PRSCATEGORY)
    List<ProhibitedSystem> findByPrscategory(ProhibitedSystem.ProhibitedSystemCategory category);

    // ✅ Búsqueda por nombre (índice recomendado en PRSNAME)
    Optional<ProhibitedSystem> findByPrsname(String name);
}
```

### Índices Recomendados en Base de Datos

Los índices están definidos en las anotaciones JPA y se crean automáticamente. Para índices adicionales (como GIN para JSONB), ver scripts SQL en `sql/01_indexes.sql`.

### Optimización de Keywords Matching

**Problema:** Si hay muchos sistemas prohibidos activos, la búsqueda puede ser lenta.

**Solución:**
1. **Cachear sistemas activos** (si no cambian frecuentemente):
   ```java
   @Cacheable("activeProhibitedSystems")
   public List<ProhibitedSystem> getActiveProhibitedSystems() {
       return prohibitedSystemRepository.findByPrsactiveTrueOrderByPrsname();
   }
   ```

2. **Usar búsqueda en BD** para keywords (PostgreSQL JSONB):
   ```java
   @Query("SELECT p FROM ProhibitedSystem p WHERE p.prsactive = true " +
          "AND JSONB_EXISTS(p.prskeywords, :keyword)")
   List<ProhibitedSystem> findByKeywordInActiveSystems(@Param("keyword") String keyword);
   ```

3. **Paralelizar búsqueda** si hay muchos sistemas:
   ```java
   List<ProhibitedSystem> detected = activeSystems.parallelStream()
       .filter(system -> matchesKeywords(project, system))
       .collect(Collectors.toList());
   ```

### Caching Strategies

**Cuándo usar cache:**
- Catálogo de sistemas prohibidos activos (cambia poco)
- Resultados de verificación (cache corto, 5-10 minutos)

**Ejemplo de Cache:**

```java
@Cacheable(value = "prohibitedSystemsCatalog", unless = "#result.isEmpty()")
public List<ProhibitedSystem> getActiveProhibitedSystems() {
    return prohibitedSystemRepository.findByPrsactiveTrueOrderByPrsname();
}

@CacheEvict(value = "prohibitedSystemsCatalog", allEntries = true)
public ProhibitedSystem createProhibitedSystem(...) {
    // ...
}
```

---

## 🔒 SEGURIDAD

### Autenticación y Autorización

**Roles Requeridos:**
- **Verificación:** Cualquier usuario autenticado
- **Bloqueo de Despliegue:** `compliance-officers`, `deployment-manager`
- **Gestión de Catálogo:** `compliance-officers`, `admin` (solo lectura para usuarios finales)

**Implementación:**

```java
@PreAuthorize("hasAnyRole('compliance-officers', 'deployment-manager')")
@PostMapping("/block")
public Mono<ResponseEntity<ProhibitedSystemActionResponseDto>> blockDeployment(
        @RequestBody BlockDeploymentRequestDto request) {
    // ...
}
```

### Validación de Inputs

1. **Validar en DTOs:**
   ```java
   @Data
   public class BlockDeploymentRequestDto {
       @NotNull(message = "Project ID is required")
       private Long projectId;

       @NotBlank(message = "Reason is required")
       @Size(max = 500, message = "Reason must not exceed 500 characters")
       private String reason;
   }
   ```

2. **Validar en Business Service:**
   ```java
   if (projectId == null) {
       throw new IllegalArgumentException("Project ID is required");
   }
   if (reason == null || reason.trim().isEmpty()) {
       throw new IllegalArgumentException("Reason is required");
   }
   if (reason.length() > 500) {
       throw new IllegalArgumentException("Reason must not exceed 500 characters");
   }
   ```

### Sanitización de Datos

**Para campos de texto (descripción, razón):**
- Eliminar HTML/JavaScript malicioso
- Limitar longitud
- Validar caracteres permitidos

**Ejemplo:**
```java
private String sanitizeReason(String reason) {
    if (reason == null) return null;
    // Eliminar HTML tags
    String sanitized = reason.replaceAll("<[^>]*>", "");
    // Limitar longitud
    return sanitized.length() > 500 ? sanitized.substring(0, 500) : sanitized;
}
```

### Logging Seguro

**No loguear información sensible:**
- ✅ Loguear IDs de proyecto
- ❌ NO loguear descripciones completas de proyectos
- ✅ Loguear nombres de sistemas prohibidos
- ❌ NO loguear razones de bloqueo completas (solo resumen)

**Ejemplo:**
```java
log.info("Deployment blocked for project {}: {}", projectId,
    reason != null && reason.length() > 50 ? reason.substring(0, 50) + "..." : reason);
```

---

## 🔄 MIGRACIONES DE BASE DE DATOS

### Agregar Nuevos Campos a Entidades

**Proceso:**
1. Agregar campo a la entidad JPA con anotaciones apropiadas
2. JPA actualizará automáticamente la BD si `ddl-auto: update`
3. Para producción, crear script SQL de migración

**Ejemplo: Agregar campo `PRSVERIFIEDBY` a `ProhibitedSystem`**

```java
// 1. Agregar campo a entidad
@Column(name = "PRSVERIFIEDBY", length = 100)
private String prsverifiedby;
```

**Script SQL de migración (para producción):**

```sql
-- Migración: Agregar campo PRSVERIFIEDBY a GOVPROHIBITEDSYSTEMS
ALTER TABLE GOVPROHIBITEDSYSTEMS
ADD COLUMN PRSVERIFIEDBY VARCHAR(100);

-- Índice opcional si se busca frecuentemente
CREATE INDEX idx_prohibited_systems_verified_by ON GOVPROHIBITEDSYSTEMS(PRSVERIFIEDBY);
```

### Cambios en Esquema

**Cuándo crear script SQL manual:**
- Cambios en tipos de datos
- Cambios en constraints (NOT NULL, UNIQUE, etc.)
- Migración de datos
- Cambios en índices

**Ejemplo: Cambiar longitud de campo**

```sql
-- Antes: PRSNAME VARCHAR(200)
-- Después: PRSNAME VARCHAR(500)

ALTER TABLE GOVPROHIBITEDSYSTEMS
ALTER COLUMN PRSNAME TYPE VARCHAR(500);
```

### Migración de Datos

**Ejemplo: Migrar datos existentes**

```sql
-- Actualizar sistemas existentes con un valor por defecto
UPDATE GOVPROHIBITEDSYSTEMS
SET PRSVERIFIEDBY = PRSCREATEDBY
WHERE PRSVERIFIEDBY IS NULL;
```

---

## 📚 REFERENCIAS

- **Base Legal:** EU AI Act Art. 5, Anexo II
- **Documentación Frontend:** Ver `DEVELOPER_GUIDE_FRONTEND.md`
- **Guía BPMN:** Ver `BPMN_WORKFLOW_GUIDE.md`
- **Guía de Usuario:** Ver `user_guide/GUIA_FUNCIONAL_PROHIBITED_SYSTEMS.md`
- **Scripts SQL:** Ver directorio `sql/`
- **Arquitectura General:** Ver documentación de arquitectura del proyecto
- **Spring WebFlux:** https://docs.spring.io/spring-framework/reference/web/webflux.html
- **Resilience4j:** https://resilience4j.readme.io/
- **Spring Data JPA:** https://spring.io/projects/spring-data-jpa

---

**Última actualización:** Diciembre 2025
**Versión del documento:** 1.1
