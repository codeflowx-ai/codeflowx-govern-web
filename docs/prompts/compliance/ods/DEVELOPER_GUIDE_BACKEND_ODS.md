# 👨‍💻 GUÍA PARA DEVELOPERS BACKEND - ODS IMPACT

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Desarrolladores Backend (Java/Spring Boot)

---

## 📋 ÍNDICE

1. [Arquitectura General](#arquitectura-general)
2. [Estructura de Capas](#estructura-de-capas)
3. [Servicios de Negocio](#servicios-de-negocio)
4. [Cálculo de KPIs](#cálculo-de-kpis)
5. [BFF (Backend for Frontend)](#bff-backend-for-frontend)
6. [Microservicio de Negocio](#microservicio-de-negocio)
7. [Queries SQL](#queries-sql)
8. [DTOs](#dtos)

---

## 🏗️ ARQUITECTURA GENERAL

### Capas de la Aplicación

```
Frontend (Next.js)
    ↓ HTTP
BFF (Backend for Frontend) - codeflowx.govern.bff.compliance
    ↓ HTTP/WebClient (Reactivo) - Gateway
Business Microservice (ODS Impact Service) - codeflowx-governance-ods-impact-service
    ↓ Mono.fromCallable() (envuelve llamadas síncronas)
Business Services (Lógica de Negocio) - codeflowx.govern.business
    ↓ Repositorios JPA / JdbcTemplate
Repositories (JPA) - codeflowx.govern.repository
    ↓
Database (PostgreSQL)
```

**Nota sobre Gateway:**
- El BFF llama al **gateway interno** (`services.gateway.base-url`)
- El gateway enruta automáticamente las peticiones a los microservicios correctos
- Las rutas en el WebClient son relativas al gateway (ej: `/api/v1/compliance/ods-impact`)

### Componentes Principales

1. **BFF (Backend for Frontend)**
   - Ubicación: `codeflowx.govern.bff.compliance` (recomendado para módulos de compliance)
   - Responsabilidad: Agregar datos, optimizar respuestas para frontend
   - Tecnología: Spring WebFlux (Reactivo)
   - Comunicación: Usa WebClient para llamar al gateway interno
   - Resiliencia: Circuit Breaker y Retry con operadores reactivos de Resilience4j

2. **Business Microservice**
   - Ubicación: `codeflowx-governance-ods-impact-service` (a crear)
   - Responsabilidad: Endpoints REST para ODS Impact
   - Tecnología: Spring WebFlux (Reactivo)

3. **Business Services**
   - Ubicación: `codeflowx.govern.business`
   - Responsabilidad: Cálculo de KPIs, lógica de negocio
   - Tecnología: Spring Boot (Transaccional)

4. **Entities & Repositories**
   - Ubicación: `nocode.service.entitys`, `codeflowx.govern.repository`
   - Responsabilidad: Persistencia de datos
   - Tecnología: JPA/Hibernate

---

## 📁 ESTRUCTURA DE CAPAS

### 1. BFF Layer

**Ubicación:** `nocode.service/codeflowx.govern.bff.compliance/` (recomendado para módulos de compliance)

**Componentes:**
- **Controller:** `controller/ODSImpactController.java`
  - Expone endpoints REST reactivos para frontend
  - Maneja requests HTTP
  - Retorna DTOs optimizados
  - Manejo de errores con `onErrorResume`

- **Service:** `service/ODSImpactService.java` (interface)
  - Define contratos de servicio reactivos (`Mono<T>`)

- **Service Implementation:** `service/impl/ODSImpactServiceImpl.java`
  - Implementa llamadas a microservicio de negocio usando WebClient
  - Usa operadores reactivos de Resilience4j (CircuitBreakerOperator, RetryOperator)
  - Patrón KISS: simple y directo

**Ejemplo de Controller:**
```java
@Slf4j
@RestController
@RequestMapping("/api/v1/compliance/ods-impact")
@RequiredArgsConstructor
@Tag(name = "ODS Impact", description = "Impacto de la plataforma en Objetivos de Desarrollo Sostenible")
@Timed(value = "compliance.ods-impact.controller", description = "Métricas del controller ODS Impact")
public class ODSImpactController {

    private final ODSImpactService odsImpactService;

    @GetMapping
    @Operation(
        summary = "Obtener dashboard principal de impacto ODS",
        description = "Retorna métricas generales, lista de ODS, top KPIs críticos y evolución temporal"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Dashboard obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = ODSImpactDashboardDto.class))
        ),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Timed(value = "compliance.ods-impact.dashboard", description = "Tiempo de obtención de dashboard")
    public Mono<ResponseEntity<ODSImpactDashboardDto>> getDashboard() {
        log.info("Obteniendo dashboard principal de impacto ODS");

        return odsImpactService.getDashboard()
                .map(ResponseEntity::ok)
                .onErrorResume(error -> {
                    log.error("Error obteniendo dashboard ODS", error);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/ods-{odsNumber}")
    @Operation(
        summary = "Obtener datos de un ODS específico",
        description = "Retorna todos los KPIs y métricas de un ODS específico"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Datos del ODS obtenidos exitosamente",
            content = @Content(schema = @Schema(implementation = ODSImpactDto.class))
        ),
        @ApiResponse(responseCode = "404", description = "ODS no encontrado")
    })
    @Timed(value = "compliance.ods-impact.ods", description = "Tiempo de obtención de datos ODS")
    public Mono<ResponseEntity<ODSImpactDto>> getODSData(
            @PathVariable Integer odsNumber) {
        log.info("Obteniendo datos del ODS: odsNumber={}", odsNumber);

        return odsImpactService.getODSData(odsNumber)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(error -> {
                    log.error("Error obteniendo datos del ODS: odsNumber={}", odsNumber, error);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/ods-{odsNumber}/kpis/{kpiCode}")
    @Operation(
        summary = "Obtener un KPI específico",
        description = "Retorna los datos de un KPI específico de un ODS"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "KPI obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = ODSKPIDto.class))
        ),
        @ApiResponse(responseCode = "404", description = "KPI no encontrado")
    })
    @Timed(value = "compliance.ods-impact.kpi", description = "Tiempo de obtención de KPI")
    public Mono<ResponseEntity<ODSKPIDto>> getKPI(
            @PathVariable Integer odsNumber,
            @PathVariable String kpiCode) {
        log.info("Obteniendo KPI: odsNumber={}, kpiCode={}", odsNumber, kpiCode);

        return odsImpactService.getKPI(odsNumber, kpiCode)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(error -> {
                    log.error("Error obteniendo KPI: odsNumber={}, kpiCode={}", odsNumber, kpiCode, error);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }
}
```

**Ejemplo de Service Implementation (Patrón KISS):**
```java
@Slf4j
@Service
public class ODSImpactServiceImpl implements ODSImpactService {

    private final WebClient webClient;
    private final CircuitBreaker odsImpactServiceCircuitBreaker;
    private final Retry odsImpactServiceRetry;

    public ODSImpactServiceImpl(
            WebClient webClient,
            @Qualifier("odsImpactServiceCircuitBreaker") CircuitBreaker odsImpactServiceCircuitBreaker,
            @Qualifier("odsImpactServiceRetry") Retry odsImpactServiceRetry) {
        this.webClient = webClient;
        this.odsImpactServiceCircuitBreaker = odsImpactServiceCircuitBreaker;
        this.odsImpactServiceRetry = odsImpactServiceRetry;
    }

    @Override
    public Mono<ODSImpactDashboardDto> getDashboard() {
        log.debug("Obteniendo dashboard principal de impacto ODS");

        return webClient.get()
                .uri("/api/v1/compliance/ods-impact")
                .retrieve()
                .bodyToMono(ODSImpactDashboardDto.class)
                .transformDeferred(CircuitBreakerOperator.of(odsImpactServiceCircuitBreaker))
                .transformDeferred(RetryOperator.of(odsImpactServiceRetry))
                .doOnError(error -> log.error("Error obteniendo dashboard ODS", error));
    }

    @Override
    public Mono<ODSImpactDto> getODSData(Integer odsNumber) {
        log.debug("Obteniendo datos del ODS: odsNumber={}", odsNumber);

        return webClient.get()
                .uri("/api/v1/compliance/ods-impact/ods-{odsNumber}", odsNumber)
                .retrieve()
                .bodyToMono(ODSImpactDto.class)
                .transformDeferred(CircuitBreakerOperator.of(odsImpactServiceCircuitBreaker))
                .transformDeferred(RetryOperator.of(odsImpactServiceRetry))
                .doOnError(error -> log.error("Error obteniendo datos del ODS: odsNumber={}", odsNumber, error));
    }

    @Override
    public Mono<ODSKPIDto> getKPI(Integer odsNumber, String kpiCode) {
        log.debug("Obteniendo KPI: odsNumber={}, kpiCode={}", odsNumber, kpiCode);

        String kpiCodeEncoded = URLEncoder.encode(kpiCode, StandardCharsets.UTF_8);
        return webClient.get()
                .uri("/api/v1/compliance/ods-impact/ods-{odsNumber}/kpis/{kpiCode}", odsNumber, kpiCodeEncoded)
                .retrieve()
                .bodyToMono(ODSKPIDto.class)
                .transformDeferred(CircuitBreakerOperator.of(odsImpactServiceCircuitBreaker))
                .transformDeferred(RetryOperator.of(odsImpactServiceRetry))
                .doOnError(error -> log.error("Error obteniendo KPI: odsNumber={}, kpiCode={}", odsNumber, kpiCode, error));
    }
}
```

---

### 2. Configuración del BFF

**Ubicación:** `codeflowx.govern.bff.compliance/src/main/resources/application.yml`

**Agregar configuración del servicio ODS Impact:**

```yaml
# Services Configuration
# ✅ ARQUITECTURA: El BFF siempre llama al gateway interno, nunca directamente a otros microservicios
# Todas las rutas en los servicios son relativas al gateway (ej: /api/v1/compliance/ods-impact/...)
services:
  gateway:
    base-url: ${GATEWAY_BASE_URL:http://localhost:8080}
  # Nota: No es necesario configurar URL individual para ODS Impact Service
  # El gateway enruta automáticamente basándose en la ruta (/api/v1/compliance/ods-impact)

# Resilience4j Configuration
resilience4j:
  circuitbreaker:
    instances:
      # Agregar circuit breaker para ODS Impact Service
      odsImpactService:
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
      # Agregar retry para ODS Impact Service
      odsImpactService:
        maxAttempts: 3
        waitDuration: 1s
        retryExceptions:
          - java.net.ConnectException
          - java.util.concurrent.TimeoutException
```

**Configuración de WebClient:**

**IMPORTANTE:** El BFF usa un **gateway único** para todas las llamadas a microservicios. Ver ejemplo en `codeflowx.govern.bff.compliance/src/main/java/.../config/WebClientConfig.java`.

**Patrón Real (usando gateway):**

```java
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(
            @Value("${services.gateway.base-url:http://localhost:8080}") String gatewayBaseUrl,
            MeterRegistry meterRegistry) {
        log.info("Configurando WebClient con gateway base URL: {}", gatewayBaseUrl);
        return WebClient.builder()
                .baseUrl(gatewayBaseUrl)  // ✅ Gateway único para todos los servicios
                .filter(metricsFilter(meterRegistry))
                .filter(logRequest())
                .filter(logResponse())
                .build();
    }
}
```

**En el Service Implementation, usar el WebClient del gateway:**

```java
@Service
public class ODSImpactServiceImpl implements ODSImpactService {

    private final WebClient webClient;  // ✅ WebClient del gateway (único para todos los servicios)
    private final CircuitBreaker odsImpactServiceCircuitBreaker;
    private final Retry odsImpactServiceRetry;

    public ODSImpactServiceImpl(
            WebClient webClient,  // ✅ Inyectar el WebClient del gateway
            @Qualifier("odsImpactServiceCircuitBreaker") CircuitBreaker odsImpactServiceCircuitBreaker,
            @Qualifier("odsImpactServiceRetry") Retry odsImpactServiceRetry) {
        this.webClient = webClient;
        this.odsImpactServiceCircuitBreaker = odsImpactServiceCircuitBreaker;
        this.odsImpactServiceRetry = odsImpactServiceRetry;
    }

    @Override
    public Mono<ODSImpactDashboardDto> getDashboard() {
        // ✅ Las rutas son relativas al gateway (ej: /api/v1/compliance/ods-impact)
        return webClient.get()
                .uri("/api/v1/compliance/ods-impact")  // Gateway enruta al microservicio correcto
                .retrieve()
                .bodyToMono(ODSImpactDashboardDto.class)
                .transformDeferred(CircuitBreakerOperator.of(odsImpactServiceCircuitBreaker))
                .transformDeferred(RetryOperator.of(odsImpactServiceRetry))
                .doOnError(error -> log.error("Error obteniendo dashboard ODS", error));
    }
}
```

**Configuración de Circuit Breaker y Retry (ResilienceConfig):**

Ver ejemplo en `codeflowx.govern.bff.governance/src/main/java/.../config/ResilienceConfig.java`:

```java
@Configuration
public class ResilienceConfig {

    @Bean
    @Qualifier("odsImpactServiceCircuitBreaker")
    public CircuitBreaker odsImpactServiceCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker("odsImpactService");
    }

    @Bean
    @Qualifier("odsImpactServiceRetry")
    public Retry odsImpactServiceRetry(RetryRegistry registry) {
        return registry.retry("odsImpactService");
    }
}
```

**Nota sobre Gateway:**
- El gateway (`services.gateway.base-url`) enruta automáticamente las peticiones a los microservicios correctos
- Las rutas en el WebClient son relativas al gateway (ej: `/api/v1/compliance/ods-impact`)
- No es necesario configurar URLs individuales por servicio en el WebClient

---

### 3. Business Microservice Layer

**Ubicación:** `nocode.service/codeflowx-governance-ods-impact-service/` (a crear)

**Componentes:**
- **Controller:** `controller/ODSImpactController.java`
  - Endpoints REST reactivos del microservicio
  - Llama a Business Services (síncronos)
  - Envuelve llamadas síncronas en `Mono.fromCallable()` con `Schedulers.boundedElastic()`
  - Convierte Entities a DTOs antes de retornar

**Ejemplo:**
```java
@Slf4j
@RestController
@RequestMapping("/api/v1/compliance/ods-impact")
@RequiredArgsConstructor
@Tag(name = "ODS Impact", description = "Impacto de la plataforma en Objetivos de Desarrollo Sostenible")
public class ODSImpactController {

    private final ODSImpactBusinessService odsImpactBusinessService;

    @GetMapping
    @Operation(summary = "Obtener dashboard principal de impacto ODS")
    public Mono<ResponseEntity<ODSImpactDashboardDto>> getDashboard() {
        log.info("Calculando dashboard principal de impacto ODS");

        return Mono.fromCallable(() -> odsImpactBusinessService.calculateDashboard())
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .onErrorResume(error -> {
                    log.error("Error calculando dashboard ODS", error);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/ods-{odsNumber}")
    @Operation(summary = "Obtener datos de un ODS específico")
    public Mono<ResponseEntity<ODSImpactDto>> getODSData(
            @PathVariable Integer odsNumber) {
        log.info("Calculando datos del ODS: odsNumber={}", odsNumber);

        return Mono.fromCallable(() -> odsImpactBusinessService.calculateODSData(odsNumber))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .onErrorResume(error -> {
                    log.error("Error calculando datos del ODS: odsNumber={}", odsNumber, error);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/ods-{odsNumber}/kpis/{kpiCode}")
    @Operation(summary = "Obtener un KPI específico")
    public Mono<ResponseEntity<ODSKPIDto>> getKPI(
            @PathVariable Integer odsNumber,
            @PathVariable String kpiCode) {
        log.info("Calculando KPI: odsNumber={}, kpiCode={}", odsNumber, kpiCode);

        return Mono.fromCallable(() -> odsImpactBusinessService.calculateKPI(odsNumber, kpiCode))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(error -> {
                    log.error("Error calculando KPI: odsNumber={}, kpiCode={}", odsNumber, kpiCode, error);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }
}
```

---

### 4. Business Services Layer

**Ubicación:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/governance/`

**Servicios Principales:**

#### `ODSImpactBusinessService`

**Responsabilidades:**
- Calcular dashboard principal
- Calcular datos por ODS
- Calcular KPIs individuales
- Agregar datos de múltiples fuentes

**Métodos Principales:**
```java
@Service
public class ODSImpactBusinessService {

    private final ODSKPICalculationService kpiCalculationService;
    private final ProjectRepository projectRepository;
    // ... otros repositorios

    public ODSImpactDashboardDto calculateDashboard() {
        // 1. Calcular score general (promedio de todos los ODS)
        double overallScore = calculateOverallScore();

        // 2. Calcular tasa de cumplimiento
        double complianceRate = calculateComplianceRate();

        // 3. Obtener datos de todos los ODS
        List<ODSImpactDto> odsList = getAllODSData();

        // 4. Obtener top 5 KPIs críticos
        List<ODSKPIDto> topCriticalKPIs = getTopCriticalKPIs(5);

        // 5. Calcular evolución temporal (últimos 12 meses)
        List<TemporalDataPoint> evolution = calculateTemporalEvolution(12);

        return ODSImpactDashboardDto.builder()
                .overallScore(overallScore)
                .complianceRate(complianceRate)
                .odsList(odsList)
                .topCriticalKPIs(topCriticalKPIs)
                .temporalEvolution(evolution)
                .build();
    }

    public ODSImpactDto calculateODSData(Integer odsNumber) {
        // 1. Obtener KPIs del ODS
        List<ODSKPIDto> kpis = kpiCalculationService.calculateKPIsForODS(odsNumber);

        // 2. Calcular score del ODS (promedio ponderado de KPIs)
        double odsScore = calculateODSScore(kpis);

        // 3. Determinar estado (GOOD, WARNING, CRITICAL)
        String status = determineStatus(odsScore);

        // 4. Calcular tendencia
        String trend = calculateTrend(odsNumber);

        // 5. Obtener módulos que impactan
        List<String> modules = getModulesForODS(odsNumber);

        return ODSImpactDto.builder()
                .odsNumber(odsNumber)
                .odsName(getODSName(odsNumber))
                .score(odsScore)
                .status(status)
                .trend(trend)
                .modulesCount(modules.size())
                .kpis(kpis)
                .build();
    }

    private double calculateOverallScore() {
        // Promedio de scores de todos los ODS
        List<Integer> odsNumbers = Arrays.asList(3, 4, 5, 7, 8, 9, 10, 12, 16, 17);
        double sum = 0.0;
        int count = 0;

        for (Integer odsNumber : odsNumbers) {
            ODSImpactDto odsData = calculateODSData(odsNumber);
            sum += odsData.getScore();
            count++;
        }

        return count > 0 ? sum / count : 0.0;
    }

    private double calculateComplianceRate() {
        // Porcentaje de KPIs que cumplen sus metas
        List<ODSKPIDto> allKPIs = getAllKPIs();
        long fulfilledKPIs = allKPIs.stream()
                .filter(kpi -> kpi.getValue() >= kpi.getMeta())
                .count();

        return allKPIs.size() > 0
                ? (fulfilledKPIs * 100.0 / allKPIs.size())
                : 0.0;
    }
}
```

#### `ODSKPICalculationService`

**Responsabilidades:**
- Calcular KPIs individuales
- Ejecutar queries SQL para KPIs
- Validar y formatear resultados

**Métodos Principales:**
```java
@Service
@Slf4j
public class ODSKPICalculationService {

    // ✅ Usar Repositorios JPA para operaciones estándar
    private final ProjectRepository projectRepository;
    private final ImmutableLogRepository immutableLogRepository;
    private final QualityManagementSystemRepository qmsRepository;
    private final ComplianceAssessmentRepository complianceAssessmentRepository;
    // ... otros repositorios

    // ✅ Usar JdbcTemplate solo para SQL nativo que no se puede hacer con JPA
    private final JdbcTemplate jdbcTemplate;

    public List<ODSKPIDto> calculateKPIsForODS(Integer odsNumber) {
        List<ODSKPIDto> kpis = new ArrayList<>();

        switch (odsNumber) {
            case 16:
                kpis.add(calculateKPI161()); // Tasa de Trazabilidad Completa
                kpis.add(calculateKPI162()); // Tasa de Certificación
                kpis.add(calculateKPI163()); // Score Promedio Compliance
                kpis.add(calculateKPI164()); // Tasa de Registro BD UE
                kpis.add(calculateKPI165()); // Tasa de HITL
                break;
            case 9:
                kpis.add(calculateKPI91()); // Score QMS Promedio
                kpis.add(calculateKPI92()); // Tasa de Documentación
                kpis.add(calculateKPI93()); // Tiempo Promedio Certificación
                kpis.add(calculateKPI94()); // Tasa de Reutilización Agentes
                kpis.add(calculateKPI95()); // Tiempo Promedio MLOps
                kpis.add(calculateKPI96()); // Tasa de Adopción LLMs
                break;
            // ... otros ODS
        }

        return kpis;
    }

    private ODSKPIDto calculateKPI161() {
        // KPI 16.1: Tasa de Trazabilidad Completa
        // ✅ Usar JdbcTemplate para SQL nativo (no se puede hacer con JPA)
        String sql = """
            WITH systems_with_traceability AS (
              SELECT DISTINCT
                p.IDXPROJECT,
                CASE
                  WHEN EXISTS (
                    SELECT 1 FROM IMLIMMUTABLELOGS iml
                    WHERE iml.IMLENTITYTYPE = 'Project'
                      AND iml.IMLENTITYID = p.IDXPROJECT
                      AND iml.IMLACTION IN ('MODEL_DEPLOYMENT', 'AGENT_EXECUTION', 'PROMPT_CHANGE')
                  ) THEN 1 ELSE 0
                END as has_traceability
              FROM PRJPROJECTS p
            )
            SELECT
              (COUNT(CASE WHEN has_traceability = 1 THEN 1 END) * 100.0 / COUNT(*)) as traceability_rate
            FROM systems_with_traceability
            """;

        Double rate = jdbcTemplate.queryForObject(sql, Double.class);

        return ODSKPIDto.builder()
                .code("KPI 16.1")
                .name("Tasa de Trazabilidad Completa")
                .value(rate)
                .unit("%")
                .meta(95.0)
                .criticalThreshold(80.0)
                .status(determineKPIStatus(rate, 95.0, 80.0))
                .trend(calculateKPITrend("KPI 16.1"))
                .description("Porcentaje de sistemas/activos con trazabilidad completa")
                .frequency("daily")
                .sources(Arrays.asList("IMLIMMUTABLELOGS", "PRJPROJECTS"))
                .build();
    }

    private ODSKPIDto calculateKPI162() {
        // KPI 16.2: Tasa de Certificación de Sistemas
        // ✅ Usar JdbcTemplate para SQL nativo con agregaciones complejas
        String sql = """
            SELECT
              (COUNT(CASE WHEN ca.READYFORCERTIFICATION = true THEN 1 END) * 100.0 /
               COUNT(CASE WHEN p.PRJISHIGHRISK = true THEN 1 END)) as certification_rate
            FROM PRJPROJECTS p
            LEFT JOIN COMCOMPLIANCEASSESSMENTS ca ON ca.IDXPROJECT = p.IDXPROJECT
            WHERE p.PRJISHIGHRISK = true
            """;

        Double rate = jdbcTemplate.queryForObject(sql, Double.class);

        return ODSKPIDto.builder()
                .code("KPI 16.2")
                .name("Tasa de Certificación de Sistemas")
                .value(rate)
                .unit("%")
                .meta(80.0)
                .criticalThreshold(60.0)
                .status(determineKPIStatus(rate, 80.0, 60.0))
                .trend(calculateKPITrend("KPI 16.2"))
                .description("Porcentaje de sistemas certificados según EU AI Act")
                .frequency("weekly")
                .sources(Arrays.asList("COMCOMPLIANCEASSESSMENTS", "PRJPROJECTS"))
                .build();
    }

    // ... más métodos de cálculo de KPIs

    private String determineKPIStatus(Double value, Double meta, Double criticalThreshold) {
        if (value >= meta) {
            return "good";
        } else if (value >= criticalThreshold) {
            return "warning";
        } else {
            return "critical";
        }
    }

    private String calculateKPITrend(String kpiCode) {
        // Calcular tendencia comparando valor actual con valor anterior (último mes)
        // Retornar "up", "down", o "stable"
        // TODO: Implementar lógica de comparación temporal
        return "stable";
    }
}
```

---

## 🗄️ QUERIES SQL

### Ubicación de Queries

Las queries SQL para calcular KPIs están documentadas en:
- `docs/prompts/compliance/ODS_IMPACT_SQL_QUERIES.md`

### Ejemplos de Queries

#### KPI 16.1: Tasa de Trazabilidad Completa
```sql
WITH systems_with_traceability AS (
  SELECT DISTINCT
    p.IDXPROJECT,
    CASE
      WHEN EXISTS (
        SELECT 1 FROM IMLIMMUTABLELOGS iml
        WHERE iml.IMLENTITYTYPE = 'Project'
          AND iml.IMLENTITYID = p.IDXPROJECT
          AND iml.IMLACTION IN ('MODEL_DEPLOYMENT', 'AGENT_EXECUTION', 'PROMPT_CHANGE')
      ) THEN 1 ELSE 0
    END as has_traceability
  FROM PRJPROJECTS p
)
SELECT
  (COUNT(CASE WHEN has_traceability = 1 THEN 1 END) * 100.0 / COUNT(*)) as traceability_rate
FROM systems_with_traceability;
```

#### KPI 16.3: Score Promedio de Compliance
```sql
SELECT
  AVG(qms.QMSOVERALLSCORE) as avg_compliance_score
FROM GOVQUALITYMANAGEMENTSYSTEMS qms;
```

---

## 📦 DTOs

### Ubicación
**IMPORTANTE:** Todos los DTOs deben estar en el módulo compartido:
`nocode.service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/`

**Reglas:**
- ✅ **Reutilizar DTOs existentes** de `codeflowx.govern.nocode.dtos`
- ❌ **NO crear DTOs** en el BFF ni en los microservicios
- ❌ **NO duplicar DTOs** - si falta uno, crearlo en el módulo compartido

### DTOs Principales

#### `ODSImpactDashboardDto`
```java
@Data
@Builder
public class ODSImpactDashboardDto {
    private Double overallScore; // 0-100
    private Double complianceRate; // Porcentaje de KPIs que cumplen metas
    private List<ODSImpactDto> odsList;
    private List<ODSKPIDto> topCriticalKPIs;
    private List<TemporalDataPoint> temporalEvolution; // Últimos 12 meses
}
```

#### `ODSImpactDto`
```java
@Data
@Builder
public class ODSImpactDto {
    private Integer odsNumber;
    private String odsName;
    private Double score; // 0-100
    private String status; // "good", "warning", "critical"
    private String trend; // "up", "down", "stable"
    private Integer modulesCount;
    private List<ODSKPIDto> kpis;
}
```

#### `ODSKPIDto`
```java
@Data
@Builder
public class ODSKPIDto {
    private String code; // Ej: "KPI 16.1"
    private String name;
    private Double value;
    private String unit; // "%", "", "horas", etc.
    private Double meta; // Valor objetivo
    private Double criticalThreshold; // Umbral crítico
    private String status; // "good", "warning", "critical"
    private String trend; // "up", "down", "stable"
    private Double trendValue; // Porcentaje de cambio
    private String description;
    private String frequency; // "daily", "weekly", "monthly"
    private List<String> sources; // Tablas de BD utilizadas
}
```

#### `TemporalDataPoint`
```java
@Data
@Builder
public class TemporalDataPoint {
    private String date; // ISO 8601
    private Double value; // Score o valor del KPI
}
```

---

## 🔄 FLUJOS DE NEGOCIO

### Flujo 1: Obtener Dashboard Principal

```
1. Frontend → GET /api/v1/compliance/ods-impact
2. BFF Controller (ODSImpactController) → ODSImpactService.getDashboard()
3. BFF Service (ODSImpactServiceImpl) → WebClient → GET /api/v1/compliance/ods-impact
   - Aplica CircuitBreakerOperator y RetryOperator
4. Business Microservice Controller → Mono.fromCallable() → ODSImpactBusinessService.calculateDashboard()
5. Business Service (síncrono):
   - Calcula score general
   - Calcula tasa de cumplimiento
   - Calcula datos de todos los ODS
   - Obtiene top 5 KPIs críticos
   - Calcula evolución temporal
   - Retorna ODSImpactDashboardDto
6. Controller convierte entidad a DTO (si es necesario)
7. Controller retorna Mono<ResponseEntity<ODSImpactDashboardDto>>
8. BFF Service retorna Mono<ODSImpactDashboardDto>
9. BFF Controller retorna Mono<ResponseEntity<ODSImpactDashboardDto>>
10. Frontend recibe dashboard completo
```

### Flujo 2: Obtener Datos de un ODS Específico

```
1. Frontend → GET /api/v1/compliance/ods-impact/ods-16
2. BFF → Business Microservice → GET /api/v1/compliance/ods-impact/ods-16
3. Business Microservice → ODSImpactBusinessService.calculateODSData(16)
4. Service calcula KPIs del ODS 16
5. Service calcula score del ODS
6. Service determina estado y tendencia
7. Service retorna DTO del ODS
8. Frontend recibe datos del ODS
```

### Flujo 3: Calcular un KPI Específico

```
1. Frontend → GET /api/v1/compliance/ods-impact/ods-16/kpis/KPI%2016.1
2. BFF → Business Microservice → GET /api/v1/compliance/ods-impact/ods-16/kpis/KPI%2016.1
3. Business Microservice → ODSKPICalculationService.calculateKPI161()
4. Service ejecuta query SQL
5. Service valida y formatea resultado
6. Service determina estado y tendencia
7. Service retorna DTO del KPI
8. Frontend recibe datos del KPI
```

---

## ✅ VALIDACIONES Y REGLAS

### Validaciones de KPIs

1. **Valores No Nulos:** Todos los valores de KPIs deben ser no nulos
2. **Rangos Válidos:** Scores deben estar entre 0-100, porcentajes entre 0-100
3. **Metas Positivas:** Las metas deben ser valores positivos
4. **Umbrales Críticos:** Los umbrales críticos deben ser menores que las metas

### Reglas de Negocio

1. **Score de ODS:** Se calcula como promedio ponderado de todos sus KPIs
2. **Score General:** Se calcula como promedio de scores de todos los ODS
3. **Tasa de Cumplimiento:** Porcentaje de KPIs que cumplen sus metas
4. **Estado de KPI:**
   - GOOD: Valor >= Meta
   - WARNING: Valor entre Umbral Crítico y Meta
   - CRITICAL: Valor < Umbral Crítico

### Reglas de Arquitectura

1. **DTOs en BFF y Controladores:** BFF y controladores de microservicios SOLO trabajan con DTOs
2. **Conversión DTO ↔ Entidad:** La conversión se hace en los controladores del microservicio, nunca en servicios de negocio
3. **Servicios de Negocio Síncronos:** Los business services son síncronos, se envuelven en `Mono.fromCallable()` en los controladores
4. **BFF Reactivo:** El BFF es completamente reactivo, usa operadores de Resilience4j (CircuitBreakerOperator, RetryOperator)
5. **Patrón KISS:** Los servicios del BFF deben ser simples y directos (ver `ARQUITECTURA_FRONTEND.md`)
6. **NO usar Map en respuestas:** SIEMPRE crear DTOs tipados para todas las respuestas de API

---

## 📚 REFERENCIAS

- **Arquitectura Frontend:** `docs/ARQUITECTURA_FRONTEND.md` (sección "Arquitectura de Backend y Microservicios de Negocio")
- **Queries SQL:** `docs/prompts/compliance/ODS_IMPACT_SQL_QUERIES.md`
- **Análisis de Factibilidad:** `docs/prompts/compliance/ODS_IMPACT_FEASIBILITY_ANALYSIS_UPDATED.md`
- **KPIs y Dashboards:** `docs/prompts/compliance/PROMPT_KPIS_DASHBOARDS_ODS.md`
- **Impacto ODS:** `docs/prompts/compliance/IMPACTO_ODS_COMPLIANCE.md`
- **Plantilla de Microservicio:** `nocode.service/codeflowx-governance-classification-service/` (referencia para crear nuevos microservicios)
- **BFF de Referencia:** `nocode.service/codeflowx.govern.bff.governance/` (ejemplo de implementación BFF)
- **DTOs Compartidos:** `nocode.service/codeflowx.govern.nocode.dtos/`

---

**Última Actualización:** Diciembre 2025
