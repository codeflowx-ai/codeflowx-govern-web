# 👨‍💻 GUÍA PARA DEVELOPERS BACKEND - IMMUTABLE LOGS

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
8. [Hash Chain SHA-256](#hash-chain-sha-256)
9. [Verificación de Integridad](#verificación-de-integridad)
10. [Integración BPMN](#integración-bpmn)

---

## 🏗️ ARQUITECTURA GENERAL

### Capas de la Aplicación

```
Frontend (Next.js)
    ↓
API Routes (Next.js BFF)
    ↓
BFF (Backend for Frontend) - Spring WebFlux
    ↓
Business Microservice (Immutable Logs Service) - Spring WebFlux
    ↓
Business Services (Lógica de Negocio) - Spring Boot
    ↓
Repositories (JPA)
    ↓
Database (PostgreSQL)
```

### Componentes Principales

1. **BFF (Backend for Frontend)**
   - Ubicación: `codeflowx.govern.bff.compliance`
   - Responsabilidad: Proxy reactivo con Circuit Breaker y Retry
   - Tecnología: Spring WebFlux (Reactivo)

2. **Business Microservice**
   - Ubicación: `codeflowx-governance-immutable-logs-service`
   - Responsabilidad: Endpoints REST para Immutable Logs
   - Tecnología: Spring WebFlux (Reactivo)

3. **Business Services**
   - Ubicación: `codeflowx.govern.business`
   - Responsabilidad: Lógica de negocio, hash chain, verificación
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
- **Controller:** `controller/ImmutableLogsController.java`
  - Expone endpoints REST para frontend
  - Maneja requests HTTP
  - Retorna DTOs optimizados

- **Service:** `service/ImmutableLogsService.java` (interface)
  - Define contratos de servicio

- **Service Implementation:** `service/impl/ImmutableLogsServiceImpl.java`
  - Implementa llamadas a microservicio de negocio
  - Usa WebClient para comunicación reactiva
  - Implementa Circuit Breaker y Retry (Resilience4j)

**Ejemplo:**
```java
@RestController
@RequestMapping("/api/v1/immutable-logs")
public class ImmutableLogsController {
    private final ImmutableLogsService immutableLogsService;

    @PostMapping("/search")
    public Mono<ResponseEntity<ImmutableLogSearchResponseDto>> search(
            @Valid @RequestBody ImmutableLogSearchCriteriaDto criteria) {
        return immutableLogsService.search(criteria)
                .map(ResponseEntity::ok);
    }
}
```

---

### 2. Business Microservice Layer

**Ubicación:** `nocode.service/codeflowx-governance-immutable-logs-service/`

**Componentes:**
- **Controller:** `controller/ImmutableLogsController.java`
  - Endpoints REST del microservicio
  - Llama a Business Services
  - Convierte Entities a DTOs

**Ejemplo:**
```java
@RestController
@RequestMapping("/api/v1/immutable-logs")
public class ImmutableLogsController {
    private final ImmutableLoggingBusinessService businessService;

    @PostMapping("/search")
    public Mono<ResponseEntity<ImmutableLogSearchResponseDto>> search(
            @Valid @RequestBody ImmutableLogSearchCriteriaDto criteria) {
        return Mono.fromCallable(() -> {
                SearchResult searchResult = businessService.searchLogsWithCriteria(
                    criteria.getEntityType(),
                    criteria.getEntityId(),
                    // ... otros parámetros
                );
                return toDto(searchResult);
            })
            .subscribeOn(Schedulers.boundedElastic())
            .map(ResponseEntity::ok);
    }
}
```

---

### 3. Business Services Layer

**Ubicación:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/logging/`

**Servicios Principales:**

#### `ImmutableLoggingBusinessService`

**Responsabilidades:**
- Crear logs inmutables con hash chain SHA-256
- Verificar integridad de hash chain
- Buscar logs según criterios
- Obtener detalle de log con cadena completa
- Integrar con BPMN para alertas

**Métodos Principales:**

##### `createLogEntry()`

Crea un nuevo log inmutable con hash chain.

```java
@Service
public class ImmutableLoggingBusinessService {

    @Autowired
    private ImmutableLogRepository repository;

    @Autowired(required = false)
    private BpmnWorkflowClient bpmnWorkflowClient;

    @Transactional
    public ImmutableLog createLogEntry(String entityType, Long entityId, String action,
                                       Long userId, String userName, Map<String, Object> data) {
        log.info("Creating immutable log entry for entity: {}/{}, action: {}",
                 entityType, entityId, action);

        // 1. Obtener hash del log anterior
        ImmutableLog lastLog = getLastLog();
        String previousHash = lastLog != null ? lastLog.getImlcurrenthash() :
            "0000000000000000000000000000000000000000000000000000000000000000";

        // 2. Crear nuevo log
        ImmutableLog newLog = new ImmutableLog();
        newLog.setImlentitytype(entityType);
        newLog.setImlentityid(entityId);
        newLog.setImlaction(action);
        newLog.setImluserid(userId);
        newLog.setImlusername(userName);
        newLog.setImlprevioushash(previousHash);

        // 3. Serializar data a JSON
        String dataJson = objectMapper.writeValueAsString(data);
        newLog.setImldata(dataJson);

        // 4. Timestamps
        Timestamp now = new Timestamp(System.currentTimeMillis());
        newLog.setImltimestamp(now);
        newLog.setImltimestampepoch(System.currentTimeMillis());
        newLog.setImlcreatedat(now);
        newLog.setIduuid(UUID.randomUUID().toString());

        // 5. Calcular current hash
        String currentHash = calculateHash(newLog);
        newLog.setImlcurrenthash(currentHash);
        newLog.setImlintegritystatus("UNVERIFIED");

        // 6. Guardar (solo INSERT - APPEND-ONLY)
        newLog = repository.save(newLog);
        log.info("Immutable log created with ID: {}, hash: {}",
                 newLog.getIdximmutablelog(), currentHash);

        return newLog;
    }
}
```

##### `verifyIntegrity()`

Verifica la integridad de la hash chain en un rango de logs.

```java
@Transactional(readOnly = true)
public LogIntegrityReport verifyIntegrity(Long startId, Long endId) {
    log.info("Verifying integrity of logs from {} to {}", startId, endId);

    // 1. Obtener logs en rango
    List<ImmutableLog> logs = repository.findByIdRange(startId, endId);

    // 2. Crear reporte
    LogIntegrityReport report = new LogIntegrityReport();
    report.setTotalLogsChecked(logs.size());
    report.setIntegrityValid(true);

    // 3. Verificar cada log
    String expectedPreviousHash = null;
    for (ImmutableLog ilog : logs) {
        // Verificar hash actual
        String calculatedHash = calculateHash(ilog);
        if (!calculatedHash.equals(ilog.getImlcurrenthash())) {
            report.setIntegrityValid(false);
            report.addCorruptedLog(ilog.getIdximmutablelog(), "Current hash mismatch");
            log.error("Hash mismatch detected for log ID: {}", ilog.getIdximmutablelog());
        }

        // Verificar chain
        if (expectedPreviousHash != null &&
            !ilog.getImlprevioushash().equals(expectedPreviousHash)) {
            report.setIntegrityValid(false);
            report.addCorruptedLog(ilog.getIdximmutablelog(), "Chain broken");
            log.error("Chain broken at log ID: {}", ilog.getIdximmutablelog());
        }

        expectedPreviousHash = ilog.getImlcurrenthash();
    }

    // 4. Si se detectó corrupción, disparar workflow BPMN
    if (!report.isIntegrityValid() &&
        report.getCorruptedLogs() != null &&
        !report.getCorruptedLogs().isEmpty()) {
        triggerIntegrityAlertWorkflow(startId, endId, report.getCorruptedLogs().size());
    }

    return report;
}
```

##### `searchLogsWithCriteria()`

Busca logs según múltiples criterios con paginación.

```java
@Transactional(readOnly = true)
public SearchResult searchLogsWithCriteria(String entityType, Long entityId, Long userId,
                                           String hash, String searchText,
                                           Timestamp startDate, Timestamp endDate,
                                           int page, int pageSize) {
    log.info("Searching immutable logs: entityType={}, entityId={}, userId={}",
             entityType, entityId, userId);

    // 1. Obtener todos los logs
    List<ImmutableLog> allLogs = repository.findAll();

    // 2. Aplicar filtros
    List<ImmutableLog> filteredLogs = allLogs.stream()
        .filter(log -> entityType == null || log.getImlentitytype().equals(entityType))
        .filter(log -> entityId == null || log.getImlentityid().equals(entityId))
        .filter(log -> userId == null || log.getImluserid().equals(userId))
        .filter(log -> startDate == null || !log.getImltimestamp().before(startDate))
        .filter(log -> endDate == null || !log.getImltimestamp().after(endDate))
        .filter(log -> hash == null || hash.isEmpty() ||
                      log.getImlcurrenthash().contains(hash) ||
                      log.getImlprevioushash().contains(hash))
        .filter(log -> {
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }
            String searchLower = searchText.toLowerCase();
            return log.getImlentitytype().toLowerCase().contains(searchLower) ||
                   log.getImlaction().toLowerCase().contains(searchLower) ||
                   (log.getImldata() != null && log.getImldata().toLowerCase().contains(searchLower));
        })
        .sorted((a, b) -> b.getImltimestamp().compareTo(a.getImltimestamp()))
        .collect(Collectors.toList());

    // 3. Aplicar paginación
    int total = filteredLogs.size();
    int totalPages = (int) Math.ceil((double) total / pageSize);
    int start = page * pageSize;
    int end = Math.min(start + pageSize, total);

    List<ImmutableLog> paginatedLogs = start < total
        ? filteredLogs.subList(start, end)
        : Collections.emptyList();

    // 4. Construir resultado
    SearchResult result = new SearchResult();
    result.setLogs(paginatedLogs);
    result.setTotal((long) total);
    result.setPage(page);
    result.setPageSize(pageSize);
    result.setTotalPages(totalPages);

    return result;
}
```

##### `getLogDetailWithChain()`

Obtiene el detalle completo de un log con su hash chain.

```java
@Transactional(readOnly = true)
public LogDetailResult getLogDetailWithChain(Long id) {
    log.info("Getting log detail with chain for ID: {}", id);

    // 1. Obtener log
    ImmutableLog log = findById(id);
    if (log == null) {
        return null;
    }

    // 2. Obtener cadena completa de logs de la misma entidad
    List<ImmutableLog> chain = getLogChain(log.getImlentitytype(), log.getImlentityid());

    // 3. Encontrar posición actual en la cadena
    int currentIndex = -1;
    for (int i = 0; i < chain.size(); i++) {
        if (chain.get(i).getIdximmutablelog().equals(id)) {
            currentIndex = i;
            break;
        }
    }

    // 4. Obtener log anterior y siguiente
    ImmutableLog previousLog = currentIndex > 0 ? chain.get(currentIndex - 1) : null;
    ImmutableLog nextLog = currentIndex >= 0 && currentIndex < chain.size() - 1
        ? chain.get(currentIndex + 1) : null;

    // 5. Construir resultado
    LogDetailResult result = new LogDetailResult();
    result.setLog(log);
    result.setChain(chain);
    result.setPreviousLog(previousLog);
    result.setNextLog(nextLog);
    result.setChainLength(chain.size());
    result.setCurrentPosition(currentIndex >= 0 ? currentIndex + 1 : 0);

    return result;
}
```

---

## 🗄️ ENTIDADES JPA

### Ubicación
`nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/logging/`

### Entidad Principal

#### `ImmutableLog`

**Tabla:** `GOVIMMUTABLELOGS`

**Campos Principales:**
- `IDXIMMUTABLELOG` (PK) - ID autonumérico
- `IDUUID` (UUID) - UUID único
- `IMLENTITYTYPE` (String) - Tipo de entidad: Model, Agent, Prompt, Project
- `IMLENTITYID` (Long) - ID de entidad afectada
- `IMLACTION` (String) - Acción: MODEL_DEPLOYMENT, AGENT_EXECUTION, etc.
- `IMLUSERID` (Long) - ID de usuario
- `IMLUSERNAME` (String) - Nombre de usuario
- `IMLDATA` (JSONB) - Datos del log en formato JSON
- `IMLCURRENTHASH` (String) - Hash SHA-256 del log actual
- `IMLPREVIOUSHASH` (String) - Hash del log anterior (hash chain)
- `IMLTIMESTAMP` (Timestamp) - Fecha y hora del evento
- `IMLTIMESTAMPEPOCH` (Long) - Timestamp en epoch (millis)
- `IMLVERIFIED` (Boolean) - Si ha sido verificado
- `IMLINTEGRITYSTATUS` (String) - Estado: UNVERIFIED, VERIFIED, CORRUPTED
- `IMLLASTVERIFICATIONDATE` (Timestamp) - Última verificación
- `IMLIPADDRESS` (String) - IP del usuario
- `IMLUSERAGENT` (String) - User agent del navegador
- `IMLCREATEDAT` (Timestamp) - Fecha de creación del registro

**Ejemplo:**
```java
@Entity
@Table(name = "GOVIMMUTABLELOGS")
public class ImmutableLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXIMMUTABLELOG")
    private Long idximmutablelog;

    @Column(name = "IDUUID", length = 36)
    private String iduuid;

    @Column(name = "IMLENTITYTYPE", length = 100)
    private String imlentitytype;

    @Column(name = "IMLENTITYID")
    private Long imlentityid;

    @Column(name = "IMLACTION", length = 100)
    private String imlaction;

    @Column(name = "IMLCURRENTHASH", length = 64)
    private String imlcurrenthash;

    @Column(name = "IMLPREVIOUSHASH", length = 64)
    private String imlprevioushash;

    @Column(name = "IMLDATA", columnDefinition = "JSONB")
    private String imldata;

    @Column(name = "IMLTIMESTAMP")
    private Timestamp imltimestamp;

    // ... otros campos
}
```

---

## 📦 REPOSITORIOS

### Ubicación
`nocode.service/codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/logging/`

### Repositorio Principal

#### `ImmutableLogRepository`

**Extiende:** `GenericRepository<ImmutableLog, Long>`

**Métodos Especializados:**
```java
@Repository
public interface ImmutableLogRepository extends GenericRepository<ImmutableLog, Long> {

    /**
     * Obtiene logs en un rango de IDs
     */
    @Query("SELECT il FROM ImmutableLog il WHERE il.idximmutablelog BETWEEN :startId AND :endId ORDER BY il.imltimestamp ASC")
    List<ImmutableLog> findByIdRange(@Param("startId") Long startId, @Param("endId") Long endId);

    /**
     * Obtiene el último log (para hash chain)
     */
    Optional<ImmutableLog> findTopByOrderByIdximmutablelogDesc();

    /**
     * Obtiene cadena de logs de una entidad
     */
    List<ImmutableLog> findByImlentitytypeAndImlentityidOrderByImltimestampAsc(
        String entityType, Long entityId);
}
```

---

## 🔐 HASH CHAIN SHA-256

### Algoritmo de Cálculo

El hash se calcula concatenando los siguientes campos en orden:

```java
private String calculateHash(ImmutableLog ilog) {
    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // Concatenar campos en orden determinista
        String hashInput = ilog.getImlprevioushash() +
                           ilog.getImltimestampepoch() +
                           ilog.getImlentitytype() +
                           ilog.getImlentityid() +
                           ilog.getImlaction() +
                           ilog.getImluserid() +
                           ilog.getImldata();

        byte[] hashBytes = digest.digest(hashInput.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hashBytes);

    } catch (NoSuchAlgorithmException e) {
        log.error("SHA-256 algorithm not available", e);
        throw new RuntimeException("SHA-256 not available", e);
    }
}
```

### Características

- **Determinista:** Mismo input siempre produce mismo hash
- **Inmutable:** Cualquier cambio en datos altera el hash
- **Chain:** Cada hash incluye el hash anterior
- **Verificable:** Se puede verificar la integridad en cualquier momento

---

## ✅ VERIFICACIÓN DE INTEGRIDAD

### Proceso de Verificación

1. **Obtener logs en rango:** `findByIdRange(startId, endId)`
2. **Verificar cada log:**
   - Calcular hash esperado
   - Comparar con hash almacenado
   - Verificar que `previousHash` coincide con hash del log anterior
3. **Generar reporte:**
   - Score de integridad (0.00 - 1.00)
   - Lista de logs corruptos
   - Estado: INTEGRITY_OK, INTEGRITY_PARTIAL, INTEGRITY_BROKEN

### Integración BPMN

Cuando se detecta corrupción, se dispara automáticamente un workflow BPMN:

```java
private String triggerIntegrityAlertWorkflow(Long startId, Long endId, int corruptedCount) {
    if (bpmnWorkflowClient == null || !bpmnWorkflowClient.isAvailable()) {
        return null;
    }

    Map<String, Object> variables = new HashMap<>();
    variables.put("startId", startId);
    variables.put("endId", endId);
    variables.put("corruptedCount", corruptedCount);
    variables.put("alertDate", LocalDateTime.now().toString());
    variables.put("alertType", "IMMUTABLE_LOG_INTEGRITY_ALERT");

    return bpmnWorkflowClient.startProcess(
        "immutable-logs-integrity-alert-workflow",
        variables
    );
}
```

---

## 🔄 APPEND-ONLY ENFORCEMENT

### Reglas Críticas

1. **Solo INSERT permitido:** Nunca UPDATE ni DELETE
2. **Hash calculado automáticamente:** No se puede modificar manualmente
3. **Timestamp automático:** Se establece al crear
4. **UUID único:** Generado automáticamente

### Implementación

- **Repository:** No expone métodos `update()` o `delete()`
- **Business Service:** Solo método `createLogEntry()`
- **Database:** Trigger PostgreSQL puede prevenir modificaciones (opcional)

---

## 📝 DTOs

### Ubicación
`nocode.service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/`

### DTOs Principales

- `ImmutableLogDto` - DTO para log individual
- `ImmutableLogSearchCriteriaDto` - Criterios de búsqueda
- `ImmutableLogSearchResponseDto` - Respuesta de búsqueda
- `ImmutableLogDetailResponseDto` - Detalle con hash chain
- `IntegrityVerificationRequestDto` - Request de verificación
- `IntegrityVerificationResultDto` - Resultado de verificación

---

---

## 🔄 FLUJOS DE NEGOCIO

### Flujo 1: Crear Log Inmutable

```
1. Sistema genera evento (ej: despliegue de modelo)
2. Llamada a ImmutableLoggingBusinessService.createLogEntry()
3. Service obtiene último log para hash anterior
4. Service crea nuevo log con datos del evento
5. Service calcula hash SHA-256 (previousHash + datos)
6. Service guarda log en BD (solo INSERT - APPEND-ONLY)
7. Log queda con integrityStatus = UNVERIFIED
```

### Flujo 2: Búsqueda de Logs

```
1. Frontend → POST /api/compliance/immutable-logs/search
2. API Route (Next.js) → POST /api/v1/immutable-logs/search (BFF)
3. BFF → ImmutableLogsService.search() con Circuit Breaker y Retry
4. BFF → Business Microservice → POST /api/v1/immutable-logs/search
5. Microservice → ImmutableLoggingBusinessService.searchLogsWithCriteria()
6. Service aplica filtros y paginación
7. Service retorna SearchResult (entidades internas)
8. Microservice convierte a DTOs
9. BFF retorna DTOs al frontend
```

### Flujo 3: Verificación de Integridad

```
1. Frontend → POST /api/compliance/immutable-logs/verify-integrity
2. API Route → POST /api/v1/immutable-logs/verify-integrity (BFF)
3. BFF → ImmutableLogsService.verifyIntegrity()
4. BFF → Business Microservice → POST /api/v1/immutable-logs/verify-integrity
5. Microservice → ImmutableLoggingBusinessService.verifyIntegrity(startId, endId)
6. Service obtiene logs en rango
7. Service verifica hash de cada log
8. Service verifica hash chain (previousHash coincide)
9. Si detecta corrupción → Service.triggerIntegrityAlertWorkflow()
10. Service retorna LogIntegrityReport
11. Microservice convierte a DTO
12. BFF retorna resultado al frontend
```

### Flujo 4: Obtener Detalle con Hash Chain

```
1. Frontend → GET /api/compliance/immutable-logs/{id}
2. API Route → GET /api/v1/immutable-logs/{id} (BFF)
3. BFF → ImmutableLogsService.getLogDetail(id)
4. BFF → Business Microservice → GET /api/v1/immutable-logs/{id}
5. Microservice → ImmutableLoggingBusinessService.getLogDetailWithChain(id)
6. Service obtiene log por ID
7. Service obtiene cadena completa de logs de la misma entidad
8. Service identifica log anterior y siguiente
9. Service calcula posición en cadena
10. Service retorna LogDetailResult
11. Microservice convierte a DTO
12. BFF retorna detalle completo al frontend
```

---

## ✅ VALIDACIONES Y REGLAS

### Validaciones de Creación de Log

**Ubicación:** `ImmutableLoggingBusinessService.createLogEntry()`

**Reglas:**
1. `entityType` es obligatorio y no puede ser null o vacío
2. `entityId` es obligatorio y debe ser > 0
3. `action` es obligatorio y no puede ser null o vacío
4. `userId` es obligatorio
5. `data` puede ser null (se serializa como "{}")
6. Hash se calcula automáticamente (no se puede proporcionar manualmente)
7. Timestamp se establece automáticamente (no se puede modificar)

### Reglas de Hash Chain

**Ubicación:** `ImmutableLoggingBusinessService.calculateHash()`

**Reglas:**
1. Hash se calcula concatenando en orden:
   - `previousHash`
   - `timestampepoch`
   - `entityType`
   - `entityId`
   - `action`
   - `userId`
   - `data` (JSON serializado)
2. Hash siempre es SHA-256 (64 caracteres hexadecimales)
3. Primer log tiene `previousHash = "0"` (64 ceros)
4. Hash es determinista: mismo input siempre produce mismo hash

### Reglas de Verificación de Integridad

**Ubicación:** `ImmutableLoggingBusinessService.verifyIntegrity()`

**Reglas:**
1. `startId` debe ser <= `endId`
2. Si no hay logs en rango, retorna reporte vacío
3. Score de integridad = `verifiedLogs / totalLogs`
4. Estado INTEGRITY_OK si score = 1.00
5. Estado INTEGRITY_PARTIAL si 0.90 <= score < 1.00
6. Estado INTEGRITY_BROKEN si score < 0.90
7. Si se detecta corrupción, se dispara workflow BPMN automáticamente

### Reglas de Búsqueda

**Ubicación:** `ImmutableLoggingBusinessService.searchLogsWithCriteria()`

**Reglas:**
1. Todos los filtros son opcionales (pueden ser null)
2. Filtros se combinan con AND (todos deben cumplirse)
3. Búsqueda de texto busca en: `entityType`, `action`, `data` (JSON)
4. Búsqueda de hash busca en: `currentHash`, `previousHash`
5. Paginación: `page` empieza en 0, `pageSize` mínimo 1, máximo 1000
6. Ordenamiento por defecto: `timestamp DESC` (más recientes primero)

### Reglas APPEND-ONLY

**Ubicación:** Todo el sistema

**Reglas Críticas:**
1. **NUNCA** se permite UPDATE de logs existentes
2. **NUNCA** se permite DELETE de logs existentes
3. Solo se permite INSERT de nuevos logs
4. Repository no expone métodos `save()` que puedan hacer UPDATE
5. Si se intenta modificar un log, la hash chain se rompe automáticamente

---

## ⚙️ CONFIGURACIÓN

### Configuración BFF (Resilience4j)

**Ubicación:** `codeflowx.govern.bff.compliance/src/main/resources/application.yml`

**Circuit Breaker:**
```yaml
resilience4j:
  circuitbreaker:
    instances:
      immutableLogsService:
        registerHealthIndicator: true
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        permittedNumberOfCallsInHalfOpenState: 3
        automaticTransitionFromOpenToHalfOpenEnabled: true
        waitDurationInOpenState: 10s
        failureRateThreshold: 50
```

**Retry:**
```yaml
resilience4j:
  retry:
    instances:
      immutableLogsService:
        maxAttempts: 3
        waitDuration: 1s
        retryExceptions:
          - java.net.ConnectException
          - java.util.concurrent.TimeoutException
        ignoreExceptions:
          - com.codeflowx.govern.bff.compliance.exception.NotFoundException
```

**URL del Microservicio:**
```yaml
services:
  immutable-logs:
    base-url: ${IMMUTABLE_LOGS_SERVICE_BASE_URL:http://localhost:8095}
```

### Configuración Beans (Resilience4j)

**Ubicación:** `codeflowx.govern.bff.compliance/src/main/java/com/codeflowx/govern/bff/compliance/config/ResilienceConfig.java`

```java
@Bean("immutableLogsServiceCircuitBreaker")
public CircuitBreaker immutableLogsServiceCircuitBreaker() {
    return circuitBreakerRegistry.circuitBreaker("immutableLogsService");
}

@Bean("immutableLogsServiceRetry")
public Retry immutableLogsServiceRetry() {
    return retryRegistry.retry("immutableLogsService");
}
```

---

## 🧪 TESTING

### Tests Unitarios Recomendados

**Business Service:**
- `createLogEntry()` - Verificar hash calculation
- `verifyIntegrity()` - Verificar detección de corrupción
- `searchLogsWithCriteria()` - Verificar filtros y paginación
- `getLogDetailWithChain()` - Verificar cadena completa

**Repository:**
- `findByIdRange()` - Verificar query de rango
- `findTopByOrderByIdximmutablelogDesc()` - Verificar último log
- `findByImlentitytypeAndImlentityidOrderByImltimestampAsc()` - Verificar cadena por entidad

**Hash Chain:**
- Verificar cálculo de hash determinista
- Verificar que cambio en datos altera hash
- Verificar que hash chain se rompe si se modifica un log

---

## 🔗 REFERENCIAS

### Archivos Clave

**BFF:**
- `nocode.service/codeflowx.govern.bff.compliance/src/main/java/com/codeflowx/govern/bff/compliance/controller/ImmutableLogsController.java`
- `nocode.service/codeflowx.govern.bff.compliance/src/main/java/com/codeflowx/govern/bff/compliance/service/ImmutableLogsService.java`
- `nocode.service/codeflowx.govern.bff.compliance/src/main/java/com/codeflowx/govern/bff/compliance/service/impl/ImmutableLogsServiceImpl.java`
- `nocode.service/codeflowx.govern.bff.compliance/src/main/java/com/codeflowx/govern/bff/compliance/config/ResilienceConfig.java`
- `nocode.service/codeflowx.govern.bff.compliance/src/main/resources/application.yml`

**Business Microservice:**
- `nocode.service/codeflowx-governance-immutable-logs-service/src/main/java/com/codeflowx/govern/immutablelogs/controller/ImmutableLogsController.java`

**Business Services:**
- `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/logging/ImmutableLoggingBusinessService.java`

**Entities:**
- `nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/logging/ImmutableLog.java`

**Repositories:**
- `nocode.service/codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/logging/ImmutableLogRepository.java`

**DTOs:**
- `nocode.service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/ImmutableLogDto.java`
- `nocode.service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/ImmutableLogSearchCriteriaDto.java`
- `nocode.service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/ImmutableLogSearchResponseDto.java`
- `nocode.service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/ImmutableLogDetailResponseDto.java`
- `nocode.service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/IntegrityVerificationRequestDto.java`
- `nocode.service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/IntegrityVerificationResultDto.java`

---

## 🐛 TROUBLESHOOTING

### Problemas Comunes

**1. Hash Chain Rota:**
- **Síntoma:** Verificación de integridad detecta corrupción
- **Causa:** Log fue modificado o eliminado (violación APPEND-ONLY)
- **Solución:** Revisar logs corruptos, verificar que no hay UPDATE/DELETE en BD

**2. Circuit Breaker Abierto:**
- **Síntoma:** Errores 503 Service Unavailable
- **Causa:** Microservicio no responde, demasiados fallos
- **Solución:** Verificar que microservicio está corriendo, revisar logs

**3. Búsqueda Lenta:**
- **Síntoma:** Búsquedas tardan mucho tiempo
- **Causa:** Muchos logs, falta de índices
- **Solución:** Agregar índices en campos de búsqueda frecuente

**4. Hash No Coincide:**
- **Síntoma:** Hash calculado no coincide con almacenado
- **Causa:** Datos fueron modificados después de crear log
- **Solución:** Verificar integridad de datos, revisar que no hay modificaciones

---

## 🚀 PRÓXIMOS PASOS

### Mejoras Opcionales

1. **Optimización de Queries:**
   - Índices en campos de búsqueda frecuente
   - Particionamiento de tabla para grandes volúmenes

2. **Caché:**
   - Caché de resultados de búsqueda frecuentes
   - Caché de hash chains completas

3. **Archiving:**
   - Archiving automático de logs antiguos
   - Compresión de datos JSONB

---

**Última actualización:** Diciembre 2025
**Versión:** 1.0.0
