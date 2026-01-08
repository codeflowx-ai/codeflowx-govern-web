# 👨‍💻 GUÍA PARA DEVELOPERS BACKEND - TELEMETRÍA

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
9. [DTOs](#dtos)

---

## 🏗️ ARQUITECTURA GENERAL

### Capas de la Aplicación

```
Frontend (Next.js)
    ↓
BFF (Backend for Frontend)
    ↓
Business Microservice (Telemetry Analytics Service)
    ↓
Business Services (Lógica de Negocio)
    ↓
Repositories (JPA)
    ↓
Database (PostgreSQL - codeflowx_telemetry)
```

### Componentes Principales

1. **BFF (Backend for Frontend)**
   - Ubicación: `codeflowx.govern.bff.telemetry`
   - Responsabilidad: Agregar datos, optimizar respuestas para frontend
   - Tecnología: Spring WebFlux (Reactivo)
   - Circuit Breaker y Retry: Resilience4j

2. **Business Microservice**
   - Ubicación: `codeflowx-governance-telemetry-analytics-service`
   - Responsabilidad: Endpoints REST para telemetría
   - Tecnología: Spring WebFlux (Reactivo)

3. **Business Services**
   - Ubicación: `codeflowx.govern.business`
   - Responsabilidad: Lógica de negocio, agregaciones, búsquedas
   - Tecnología: Spring Boot (Transaccional)

4. **Entities & Repositories**
   - Ubicación: `nocode.service.entitys`, `codeflowx.govern.repository`
   - Responsabilidad: Persistencia de datos
   - Tecnología: JPA/Hibernate
   - Base de Datos: `codeflowx_telemetry` (PostgreSQL)

---

## 📁 ESTRUCTURA DE CAPAS

### 1. BFF Layer

**Ubicación:** `nocode.service/codeflowx.govern.bff.telemetry/`

**Componentes:**
- **Controller:** `controller/TelemetryAnalyticsController.java`
  - Expone endpoints REST para frontend
  - Maneja requests HTTP reactivos
  - Retorna DTOs optimizados

- **Service:** `service/TelemetryAnalyticsService.java` (interface)
  - Define contratos de servicio

- **Service Implementation:** `service/impl/TelemetryAnalyticsServiceImpl.java`
  - Implementa llamadas a microservicio de negocio
  - Usa WebClient para comunicación reactiva
  - Implementa Circuit Breaker y Retry (Resilience4j)

**Ejemplo:**
```java
@RestController
@RequestMapping("/api/v1/telemetry-analytics")
public class TelemetryAnalyticsController {
    private final TelemetryAnalyticsService telemetryAnalyticsService;

    @GetMapping("/kpis")
    public Mono<ResponseEntity<TelemetryKpisResponseDto>> getKpis(
            @RequestParam(required = false) LocalDateTime startTime,
            @RequestParam(required = false) LocalDateTime endTime) {
        return telemetryAnalyticsService.getKpis(startTime, endTime)
                .map(ResponseEntity::ok);
    }
}
```

---

### 2. Business Microservice Layer

**Ubicación:** `nocode.service/codeflowx-governance-telemetry-analytics-service/`

**Componentes:**
- **Controller:** `controller/TelemetryAnalyticsController.java`
  - Endpoints REST del microservicio
  - Llama a Business Services
  - Convierte Entities a DTOs
  - Maneja paginación

**Ejemplo:**
```java
@RestController
@RequestMapping("/api/v1/telemetry-analytics")
public class TelemetryAnalyticsController {
    private final TelemetryAnalyticsBusinessService businessService;

    @GetMapping("/kpis")
    public Mono<ResponseEntity<TelemetryKpisResponseDto>> getKpis(
            @RequestParam(required = false) LocalDateTime startTime,
            @RequestParam(required = false) LocalDateTime endTime) {
        return Mono.fromCallable(() -> businessService.getKpis(startTime, endTime))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok);
    }
}
```

---

### 3. Business Services Layer

**Ubicación:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/telemetry/`

**Servicios Principales:**

#### `TelemetryAnalyticsBusinessService`

**Responsabilidades:**
- Obtener KPIs agregados de telemetría
- Buscar eventos por contenido en payload JSONB
- Buscar eventos por componente UUID
- Buscar eventos por proyecto
- Obtener estadísticas agregadas por componente

**Métodos Principales:**
```java
@Service
@Transactional(readOnly = true)
public class TelemetryAnalyticsBusinessService {

    private final TelemetryAnalyticsRepository repository;

    /**
     * Obtener KPIs agregados de telemetría.
     */
    public TelemetryKpisResponseDto getKpis(LocalDateTime startTime, LocalDateTime endTime) {
        Timestamp start = Timestamp.valueOf(startTime);
        Timestamp end = Timestamp.valueOf(endTime);

        // Obtener estadísticas por componente
        List<Object[]> componentStats = repository.getComponentStatistics(start, end);

        // Calcular KPIs agregados
        long totalEvents = 0;
        double totalCost = 0.0;
        double totalTokens = 0.0;
        double avgLatency = 0.0;
        int componentCount = componentStats.size();

        for (Object[] stat : componentStats) {
            totalEvents += ((Number) stat[1]).longValue();
            if (stat[3] != null) {
                totalTokens += ((Number) stat[3]).doubleValue();
            }
            if (stat[4] != null) {
                totalCost += ((Number) stat[4]).doubleValue();
            }
            if (stat[2] != null) {
                avgLatency += ((Number) stat[2]).doubleValue();
            }
        }

        if (componentCount > 0) {
            avgLatency = avgLatency / componentCount;
        }

        return TelemetryKpisResponseDto.builder()
                .totalEvents(totalEvents)
                .totalCostUsd(totalCost)
                .totalTokens(totalTokens)
                .avgLatencyMs(avgLatency)
                .componentCount(componentCount)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    /**
     * Buscar eventos por contenido en payload.
     */
    public List<AioTelemetry> searchByContent(
            String searchText,
            LocalDateTime startTime,
            LocalDateTime endTime,
            int limit) {
        Timestamp start = Timestamp.valueOf(startTime);
        Timestamp end = Timestamp.valueOf(endTime);
        return repository.searchByContent(searchText, start, end, limit);
    }

    /**
     * Buscar eventos por contenido con filtros de compliance/security.
     */
    public List<AioTelemetry> searchByContentWithFilters(
            String searchText,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String complianceStatus,
            String riskLevel,
            String complianceCategory,
            String issueTag,
            int limit) {
        Timestamp start = Timestamp.valueOf(startTime);
        Timestamp end = Timestamp.valueOf(endTime);
        return repository.searchByContentWithFilters(
            searchText, start, end, complianceStatus, riskLevel,
            complianceCategory, issueTag, limit);
    }

    /**
     * Buscar eventos por componente UUID.
     */
    public List<AioTelemetry> getEventsByComponent(
            String componentUuid,
            LocalDateTime startTime,
            LocalDateTime endTime) {
        Timestamp start = Timestamp.valueOf(startTime);
        Timestamp end = Timestamp.valueOf(endTime);
        return repository.findByComponentUuidAndTimeRange(componentUuid, start, end);
    }

    /**
     * Buscar eventos por proyecto.
     */
    public List<AioTelemetry> searchByProject(
            Long projectId,
            LocalDateTime startTime,
            LocalDateTime endTime) {
        Timestamp start = Timestamp.valueOf(startTime);
        Timestamp end = Timestamp.valueOf(endTime);
        return repository.findByProjectIdAndTimeRange(projectId, start, end);
    }

    /**
     * Obtener estadísticas por componente.
     */
    public List<ComponentStatisticsDto> getComponentStatistics(
            LocalDateTime startTime,
            LocalDateTime endTime) {
        Timestamp start = Timestamp.valueOf(startTime);
        Timestamp end = Timestamp.valueOf(endTime);

        List<Object[]> stats = repository.getComponentStatistics(start, end);

        return stats.stream()
                .map(stat -> ComponentStatisticsDto.builder()
                        .componentUuid((String) stat[0])
                        .totalEvents(((Number) stat[1]).longValue())
                        .avgLatencyMs(stat[2] != null ? ((Number) stat[2]).doubleValue() : 0.0)
                        .totalTokens(stat[3] != null ? ((Number) stat[3]).longValue() : 0L)
                        .totalCostUsd(stat[4] != null ? ((Number) stat[4]).doubleValue() : 0.0)
                        .build())
                .collect(Collectors.toList());
    }
}
```

---

## 🗄️ ENTIDADES JPA

### Ubicación
`nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/telemetry/`

### Entidad Principal

#### `AioTelemetry`

**Tabla:** `AIOTELEMETRY` (en base de datos `codeflowx_telemetry`)

**Campos Principales:**
- `idxtel` (PK) - ID autonumérico
- `teluuid` (UUID) - UUID único del evento
- `teltimestamp` (Timestamp) - Fecha y hora del evento
- `telcomponentuuid` (String) - UUID del componente que generó el evento
- `telagentexternalid` (String, opcional) - ID externo del agente
- `televenttype` (String) - Tipo de evento (ej: INTERACTION_COMPLETED, ERROR)
- `telseverity` (String) - Severidad (INFO, WARN, ERROR, DEBUG)
- `teltraceid` (String, opcional) - ID de traza para correlación
- `telrunid` (String, opcional) - ID de ejecución
- `telmetrics` (JSONB) - Métricas en formato JSON:
  ```json
  {
    "latency_ms": 250.5,
    "tokens_used": 1000,
    "cost_usd": 0.01
  }
  ```
- `telpayload` (JSONB) - Payload completo del evento en formato JSON
- `telsourcetool` (String, opcional) - Herramienta origen
- `telbiaschecked` (Boolean) - Si se verificó sesgo
- `teltoxicitychecked` (Boolean) - Si se verificó toxicidad
- `telpiidetected` (Boolean) - Si se detectó PII
- `telsecretdetected` (Boolean) - Si se detectó secreto
- `telcompliancestatus` (String, opcional) - Estado de cumplimiento calculado automáticamente: PASS, WARNING, REVIEW_REQUIRED, VIOLATION, CRITICAL_VIOLATION
- `telrisklevel` (String, opcional) - Nivel de riesgo calculado automáticamente: LOW, MEDIUM, HIGH, CRITICAL
- `telcompliancecategory` (String, opcional) - Categoría de compliance: GDPR, SECURITY, LEGAL
- `telissuetags` (JSONB, opcional) - Array de etiquetas de problemas: ["bias_critical", "pii_exposure", "secret_leak", etc.]
- `telanalysisresults` (JSONB, opcional) - Resultados de análisis en formato JSON

**Ejemplo:**
```java
@Entity
@Table(name = "AIOTELEMETRY", schema = "codeflowx_telemetry")
public class AioTelemetry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXTEL")
    private Long idxtel;

    @Column(name = "TELUUID", unique = true, nullable = false)
    private String teluuid;

    @Column(name = "TELTIMESTAMP", nullable = false)
    private Timestamp teltimestamp;

    @Column(name = "TELCOMPONENTUUID", nullable = false)
    private String telcomponentuuid;

    @Column(name = "TELMETRICS", columnDefinition = "jsonb")
    private String telmetrics;

    @Column(name = "TELPAYLOAD", columnDefinition = "jsonb")
    private String telpayload;

    // ... más campos
}
```

---

## 📚 REPOSITORIOS

### Ubicación
`nocode.service/codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/telemetry/`

### Repositorio Principal

#### `TelemetryAnalyticsRepository`

```java
@Repository
public interface TelemetryAnalyticsRepository extends JpaRepository<AioTelemetry, Long> {

    /**
     * Buscar eventos por componente UUID en un rango de tiempo.
     */
    @Query("SELECT t FROM AioTelemetry t WHERE t.telcomponentuuid = :componentUuid " +
           "AND t.teltimestamp BETWEEN :startTime AND :endTime " +
           "ORDER BY t.teltimestamp DESC")
    List<AioTelemetry> findByComponentUuidAndTimeRange(
        @Param("componentUuid") String componentUuid,
        @Param("startTime") Timestamp startTime,
        @Param("endTime") Timestamp endTime
    );

    /**
     * Buscar eventos por contenido en payload (búsqueda JSONB).
     */
    @Query(value = "SELECT * FROM AIOTELEMETRY " +
           "WHERE (:searchPattern IS NULL OR TELPAYLOAD::text ILIKE %:searchPattern% " +
           "       OR TELEVENTTYPE ILIKE %:searchPattern% " +
           "       OR TELCOMPONENTUUID ILIKE %:searchPattern% " +
           "       OR TELAGENTEXTERNALID ILIKE %:searchPattern%) " +
           "AND TELTIMESTAMP >= :startTime " +
           "AND TELTIMESTAMP <= :endTime " +
           "ORDER BY TELTIMESTAMP DESC " +
           "LIMIT :limit", nativeQuery = true)
    List<AioTelemetry> searchByContent(
        @Param("searchPattern") String searchPattern,
        @Param("startTime") Timestamp startTime,
        @Param("endTime") Timestamp endTime,
        @Param("limit") int limit
    );

    /**
     * Buscar eventos por contenido con filtros de compliance/security.
     * Permite filtrar por complianceStatus, riskLevel, complianceCategory e issueTags.
     */
    @Query(value = "SELECT * FROM AIOTELEMETRY " +
           "WHERE (:searchPattern IS NULL OR TELPAYLOAD::text ILIKE %:searchPattern% " +
           "       OR TELEVENTTYPE ILIKE %:searchPattern% " +
           "       OR TELCOMPONENTUUID ILIKE %:searchPattern% " +
           "       OR TELAGENTEXTERNALID ILIKE %:searchPattern%) " +
           "AND TELTIMESTAMP >= :startTime " +
           "AND TELTIMESTAMP <= :endTime " +
           "AND (:complianceStatus IS NULL OR TELCOMPLIANCESTATUS = :complianceStatus) " +
           "AND (:riskLevel IS NULL OR TELRISKLEVEL = :riskLevel) " +
           "AND (:complianceCategory IS NULL OR TELCOMPLIANCECATEGORY = :complianceCategory) " +
           "AND (:issueTag IS NULL OR TELISSUETAGS::text ILIKE %:issueTag%) " +
           "ORDER BY TELTIMESTAMP DESC " +
           "LIMIT :limit", nativeQuery = true)
    List<AioTelemetry> searchByContentWithFilters(
        @Param("searchPattern") String searchPattern,
        @Param("startTime") Timestamp startTime,
        @Param("endTime") Timestamp endTime,
        @Param("complianceStatus") String complianceStatus,
        @Param("riskLevel") String riskLevel,
        @Param("complianceCategory") String complianceCategory,
        @Param("issueTag") String issueTag,
        @Param("limit") int limit
    );

    /**
     * Buscar eventos asociados a un proyecto.
     */
    @Query("SELECT t FROM AioTelemetry t WHERE t.telcomponentuuid IN " +
           "(SELECT c.iduuid FROM AioComponent c WHERE c.idxproject = :projectId) " +
           "AND t.teltimestamp BETWEEN :startTime AND :endTime " +
           "ORDER BY t.teltimestamp DESC")
    List<AioTelemetry> findByProjectIdAndTimeRange(
        @Param("projectId") Long projectId,
        @Param("startTime") Timestamp startTime,
        @Param("endTime") Timestamp endTime
    );

    /**
     * Obtener estadísticas agregadas por componente.
     */
    @Query(value = "SELECT " +
           "TELCOMPONENTUUID as componentUuid, " +
           "COUNT(*) as totalEvents, " +
           "AVG((TELMETRICS->>'latency_ms')::numeric) as avgLatencyMs, " +
           "SUM((TELMETRICS->>'tokens_used')::numeric) as totalTokens, " +
           "SUM((TELMETRICS->>'cost_usd')::numeric) as totalCostUsd " +
           "FROM AIOTELEMETRY " +
           "WHERE TELTIMESTAMP >= :startTime AND TELTIMESTAMP <= :endTime " +
           "GROUP BY TELCOMPONENTUUID", nativeQuery = true)
    List<Object[]> getComponentStatistics(
        @Param("startTime") Timestamp startTime,
        @Param("endTime") Timestamp endTime
    );
}
```

---

## 🔄 FLUJOS DE NEGOCIO

### Flujo 1: Obtener KPIs Agregados

```
1. Frontend → GET /api/v1/telemetry-analytics/kpis?startTime=...&endTime=...
2. BFF → Business Microservice → GET /api/v1/telemetry-analytics/kpis
3. Business Microservice → TelemetryAnalyticsBusinessService.getKpis()
4. Service → TelemetryAnalyticsRepository.getComponentStatistics()
5. Repository ejecuta query SQL agregada
6. Service calcula KPIs agregados (suma, promedio)
7. Service retorna TelemetryKpisResponseDto
8. Controller convierte a Mono y retorna
9. BFF retorna DTO al frontend
```

### Flujo 2: Buscar Eventos por Contenido

```
1. Frontend → GET /api/v1/telemetry-analytics/search/content?searchText=...&startTime=...&endTime=...
2. BFF → Business Microservice → GET /api/v1/telemetry-analytics/search/content
3. Business Microservice → TelemetryAnalyticsBusinessService.searchByContent()
4. Service → TelemetryAnalyticsRepository.searchByContent()
5. Repository ejecuta query nativa con ILIKE en JSONB
6. Repository retorna List<AioTelemetry>
7. Service retorna lista de entidades
8. Controller convierte a DTOs (TelemetryEventDto)
9. Controller construye TelemetryEventsResponseDto con paginación
10. BFF retorna DTO al frontend
```

### Flujo 2b: Buscar Eventos con Filtros de Compliance/Security

```
1. Frontend → GET /api/v1/telemetry-analytics/search/content?searchText=...&complianceStatus=...&riskLevel=...&complianceCategory=...&issueTag=...
2. BFF → Business Microservice → GET /api/v1/telemetry-analytics/search/content (con filtros)
3. Business Microservice → TelemetryAnalyticsBusinessService.searchByContentWithFilters()
4. Service → TelemetryAnalyticsRepository.searchByContentWithFilters()
5. Repository ejecuta query nativa con filtros de compliance/security
6. Repository retorna List<AioTelemetry>
7. Service retorna lista de entidades
8. Controller convierte a DTOs (TelemetryEventDto) incluyendo campos de compliance/security
9. Controller construye TelemetryEventsResponseDto con paginación
10. BFF retorna DTO al frontend
```

### Flujo 3: Obtener Eventos por Componente

```
1. Frontend → GET /api/v1/telemetry-analytics/components/{uuid}/events?startTime=...&endTime=...
2. BFF → Business Microservice → GET /api/v1/telemetry-analytics/components/{uuid}/events
3. Business Microservice → TelemetryAnalyticsBusinessService.getEventsByComponent()
4. Service → TelemetryAnalyticsRepository.findByComponentUuidAndTimeRange()
5. Repository ejecuta query JPA con filtros
6. Repository retorna List<AioTelemetry> ordenados por timestamp DESC
7. Service retorna lista de entidades
8. Controller convierte a DTOs con paginación
9. BFF retorna DTO al frontend
```

### Flujo 4: Obtener Estadísticas por Componente

```
1. Frontend → GET /api/v1/telemetry-analytics/components/statistics?startTime=...&endTime=...
2. BFF → Business Microservice → GET /api/v1/telemetry-analytics/components/statistics
3. Business Microservice → TelemetryAnalyticsBusinessService.getComponentStatistics()
4. Service → TelemetryAnalyticsRepository.getComponentStatistics()
5. Repository ejecuta query SQL agregada con GROUP BY
6. Repository retorna List<Object[]> con estadísticas
7. Service convierte Object[] a ComponentStatisticsDto
8. Service retorna List<ComponentStatisticsDto>
9. Controller construye ComponentStatisticsListResponseDto
10. BFF retorna DTO al frontend
```

---

## 📦 DTOs

### Ubicación
`nocode.service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/telemetry/`

### DTOs Principales

#### `TelemetryKpisResponseDto`

```java
@Builder
@Data
public class TelemetryKpisResponseDto {
    private Long totalEvents;
    private Double totalCostUsd;
    private Long totalTokens;
    private Double avgLatencyMs;
    private Integer componentCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
```

#### `TelemetryEventDto`

```java
@Builder
@Data
public class TelemetryEventDto {
    private Long id;
    private String uuid;
    private String timestamp;
    private String componentUuid;
    private String agentExternalId;
    private String eventType;
    private String severity;
    private String traceId;
    private String runId;
    private String metrics; // JSON string
    private String payload; // JSON string
    private String sourceTool;
    private Boolean biasChecked;
    private Boolean toxicityChecked;
    private Boolean piiDetected;
    private Boolean secretDetected;
    private String complianceStatus; // PASS, WARNING, REVIEW_REQUIRED, VIOLATION, CRITICAL_VIOLATION
    private String riskLevel; // LOW, MEDIUM, HIGH, CRITICAL
    private String complianceCategory; // GDPR, SECURITY, LEGAL
    private String issueTags; // JSONB array como String: ["bias_critical", "pii_exposure", etc.]
    private String analysisResults; // JSON string
}
```

#### `TelemetryEventsResponseDto`

```java
@Builder
@Data
public class TelemetryEventsResponseDto {
    private List<TelemetryEventDto> events;
    private Long total;
    private Integer page;
    private Integer size;
}
```

#### `ComponentStatisticsDto`

```java
@Builder
@Data
public class ComponentStatisticsDto {
    private String componentUuid;
    private Long totalEvents;
    private Double avgLatencyMs;
    private Long totalTokens;
    private Double totalCostUsd;
}
```

#### `ComponentStatisticsListResponseDto`

```java
@Builder
@Data
public class ComponentStatisticsListResponseDto {
    private List<ComponentStatisticsDto> statistics;
    private Integer total;
}
```

**IMPORTANTE:** Todos los endpoints retornan DTOs tipados. **NUNCA** usar `Mono<Map<String, Object>>` o `List<Map<String, Object>>`.

---

## 🔄 PROCESAMIENTO DE EVENTOS Y CÁLCULO DE COMPLIANCE/SECURITY

### Worker de Telemetría

**Ubicación:** `codeflowx-aios-telemetry-worker/src/main/java/com/codeflowx/aios/telemetry/worker/service/impl/RealtimeGovernanceServiceImpl.java`

### Flujo de Procesamiento

1. **Recepción de Evento:** Evento recibido de RabbitMQ
2. **Guardado Inicial:** Evento guardado en BD como `AioTelemetry`
3. **Análisis de Gobernanza:** Si el payload requiere análisis:
   - Análisis de bias (si `requiresBiasCheck = true`)
   - Análisis de toxicidad (si `requiresToxicityCheck = true`)
   - Detección de PII (si `requiresPiiDetection = true`)
   - Detección de secretos (si `requiresSecretDetection = true`)
   - Verificación de compliance (si `requiresComplianceCheck = true`)
   - Verificación de data leakage (si `requiresDataLeakageCheck = true`)
4. **Cálculo de Campos de Compliance/Security:**
   - Se ejecuta `calculateComplianceSecurityFields()` basándose en los resultados del análisis
   - Calcula `complianceStatus`, `riskLevel`, `complianceCategory` e `issueTags`
5. **Actualización:** Evento actualizado en BD con todos los campos calculados

### Lógica de Cálculo

**Método:** `calculateComplianceSecurityFields(Map<String, Object> analysisResults, boolean criticalIssueDetected)`

**Reglas de Cálculo:**

1. **complianceStatus:**
   - `PASS`: Por defecto si no hay problemas
   - `WARNING`: Si hay problemas menores (bias MEDIUM, toxicidad 0.4-0.6)
   - `REVIEW_REQUIRED`: Si hay problemas de compliance
   - `VIOLATION`: Si hay problemas graves (bias HIGH, toxicidad >0.6, PII detectado)
   - `CRITICAL_VIOLATION`: Si hay problemas críticos (bias CRITICAL, toxicidad >0.8, secretos detectados)

2. **riskLevel:**
   - `LOW`: Por defecto
   - `MEDIUM`: Si hay problemas menores
   - `HIGH`: Si hay problemas graves (PII, bias HIGH, toxicidad >0.6)
   - `CRITICAL`: Si hay problemas críticos (secretos, bias CRITICAL, toxicidad >0.8)

3. **complianceCategory:**
   - `GDPR`: Si se detecta PII
   - `SECURITY`: Si se detecta bias, toxicidad o secretos
   - `LEGAL`: Si hay problemas de compliance

4. **issueTags:**
   - `bias_critical`, `bias_high`, `bias_medium`: Según severidad de bias
   - `toxicity_critical`, `toxicity_high`, `toxicity_medium`: Según score de toxicidad
   - `pii_exposure`: Si se detecta PII
   - `secret_leak`: Si se detectan secretos
   - `compliance_issue`: Si hay problemas de compliance

**Ejemplo de Implementación:**

```java
private ComplianceSecurityResult calculateComplianceSecurityFields(
        Map<String, Object> analysisResults, boolean criticalIssueDetected) {

    String complianceStatus = "PASS";
    String riskLevel = "LOW";
    String complianceCategory = null;
    List<String> issueTags = new ArrayList<>();

    // Analizar resultados de bias
    if (analysisResults.containsKey("bias_result")) {
        Map<String, Object> biasResult = (Map<String, Object>) analysisResults.get("bias_result");
        String severity = (String) biasResult.get("severity");
        if ("CRITICAL".equals(severity)) {
            complianceStatus = "CRITICAL_VIOLATION";
            riskLevel = "CRITICAL";
            issueTags.add("bias_critical");
        } else if ("HIGH".equals(severity)) {
            complianceStatus = "VIOLATION";
            riskLevel = "HIGH";
            issueTags.add("bias_high");
        }
        // ... más lógica
    }

    // Analizar resultados de toxicidad, PII, secretos, etc.
    // ...

    return new ComplianceSecurityResult(complianceStatus, riskLevel, complianceCategory, issueTags);
}
```

---

## 🔧 CONFIGURACIÓN

### BFF Configuration

**Archivo:** `codeflowx.govern.bff.telemetry/src/main/resources/application.yml`

```yaml
services:
  telemetry-analytics:
    base-url: http://localhost:8096

resilience4j:
  circuitbreaker:
    instances:
      telemetryAnalyticsServiceCircuitBreaker:
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
      telemetryAnalyticsServiceRetry:
        maxAttempts: 3
        waitDuration: 1s
        enableExponentialBackoff: true
        exponentialBackoffMultiplier: 2
```

### WebClient Configuration

**Archivo:** `codeflowx.govern.bff.telemetry/src/main/java/com/codeflowx/govern/bff/telemetry/config/WebClientConfig.java`

```java
@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8096")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
```

---

## ✅ REGLAS Y VALIDACIONES

### Reglas de Búsqueda

1. **Búsqueda por Contenido:**
   - Usa `ILIKE` para búsqueda case-insensitive en JSONB
   - Limita resultados con `LIMIT` para evitar sobrecarga
   - Por defecto: 100 resultados máximo

2. **Filtros Temporales:**
   - Si no se proporcionan fechas, se usan valores por defecto:
     - KPIs: últimos 7 días
     - Búsquedas: últimos 30 días
   - Las fechas se convierten a `Timestamp` para queries SQL

3. **Paginación:**
   - Los endpoints de eventos soportan paginación
   - `page` y `size` se incluyen en la respuesta
   - `total` indica el número total de resultados

### Reglas de Agregación

1. **KPIs Agregados:**
   - Se calculan sumando/agregando estadísticas por componente
   - La latencia promedio es el promedio de promedios por componente
   - Los valores nulos se manejan con 0.0 o 0L

2. **Estadísticas por Componente:**
   - Se agrupan por `TELCOMPONENTUUID`
   - Se usan funciones SQL agregadas (COUNT, AVG, SUM)
   - Se extraen valores de JSONB usando operadores `->>`

---

## 🔗 REFERENCIAS

### Archivos Clave

**BFF:**
- `nocode.service/codeflowx.govern.bff.telemetry/src/main/java/com/codeflowx/govern/bff/telemetry/controller/TelemetryAnalyticsController.java`
- `nocode.service/codeflowx.govern.bff.telemetry/src/main/java/com/codeflowx/govern/bff/telemetry/service/impl/TelemetryAnalyticsServiceImpl.java`
- `nocode.service/codeflowx.govern.bff.telemetry/src/main/java/com/codeflowx/govern/bff/telemetry/config/WebClientConfig.java`
- `nocode.service/codeflowx.govern.bff.telemetry/src/main/java/com/codeflowx/govern/bff/telemetry/config/ResilienceConfig.java`

**Business Services:**
- `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/telemetry/TelemetryAnalyticsBusinessService.java`

**Microservice:**
- `nocode.service/codeflowx-governance-telemetry-analytics-service/src/main/java/com/codeflowx/governance/telemetry/analytics/controller/TelemetryAnalyticsController.java`

**Entities:**
- `nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/telemetry/AioTelemetry.java`

**Repositories:**
- `nocode.service/codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/telemetry/TelemetryAnalyticsRepository.java`

**DTOs:**
- `nocode.service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/telemetry/`

---

**Última Actualización:** Diciembre 2025

**Cambios Recientes:**
- Agregados campos de compliance/security en entidad `AioTelemetry` (complianceStatus, riskLevel, complianceCategory, issueTags)
- Implementada lógica de cálculo automático de campos de compliance/security en `RealtimeGovernanceServiceImpl`
- Agregado método `searchByContentWithFilters()` para búsqueda con filtros de compliance/security
- Actualizado endpoint de búsqueda para aceptar filtros de compliance/security
- Cambio de método HTTP de POST a GET para búsqueda (REST best practices)
