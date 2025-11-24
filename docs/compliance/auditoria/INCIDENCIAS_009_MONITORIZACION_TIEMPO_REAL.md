# INCIDENCIAS Y RECOMENDACIONES - AUDITORÍA 009
## Monitorización en Tiempo Real

**Fecha:** 2025-11-17  
**Auditor:** Sistema de Gobierno de IA - CodeflowX  
**Ámbito:** Monitorización en Tiempo Real - EU AI Act Art. 12, Art. 15, Art. 19  
**Base Legal:** EU AI Act (Reglamento UE 2024/1689)

---

## RESUMEN EJECUTIVO

**Total Incidencias Identificadas:** 6  
**Críticas (🔴):** 0  
**Medias (🟡):** 4  
**Bajas (🟢):** 2

**Estado General:** ✅ **Sistema funcional con mejoras recomendadas**

---

## PARTE 1: INCIDENCIAS - CAPACIDAD Y RENDIMIENTO

### INC-009-001: Falta Documentación Formal de Capacidad de Throughput
**Prioridad:** 🟡 MEDIA  
**Artículo:** Art. 12, Art. 15  
**Ubicación:** Documentación técnica, tests de carga

**Descripción:**
El sistema documenta estimaciones de capacidad (10M eventos/día, picos 1K-10K eventos/segundo) pero no hay evidencia de tests de carga formales que validen estos límites en condiciones reales de producción.

**Evidencia:**
- Documentación menciona "10M eventos/día = ~115 eventos/segundo promedio"
- Picos documentados: "1K - 10K eventos/segundo"
- No hay documentación de:
  * Tests de carga realizados
  * Límites reales medidos en producción
  * SLA de throughput garantizado
  * Estrategia de throttling bajo carga extrema

**Impacto:**
- ⚠️ No se puede garantizar capacidad bajo carga extrema
- ⚠️ Riesgo de degradación de servicio si se excede capacidad no documentada
- ⚠️ Dificulta planificación de escalado

**Recomendación:**
1. Realizar tests de carga formales con herramientas (JMeter, Gatling, k6)
2. Documentar límites reales medidos:
   - Throughput máximo sostenido (eventos/segundo)
   - Latencia p95, p99 bajo carga
   - Punto de degradación
   - Capacidad de RabbitMQ con múltiples workers
3. Definir SLA de throughput garantizado
4. Establecer estrategia de throttling:
   - Rate limiting en Telemetry Service
   - Backpressure en RabbitMQ
   - Circuit breaker si Workers sobrecargados
5. Implementar alertas cuando throughput se acerca a límites

**Acción Correctiva:**
```yaml
# Plan de Tests de Carga
tests:
  - name: "Throughput sostenido"
    duration: "1 hora"
    target: "1000 eventos/segundo"
    metrics:
      - latency_p95 < 500ms
      - error_rate < 0.1%
      - throughput estable
  
  - name: "Pico de carga"
    duration: "10 minutos"
    target: "10000 eventos/segundo"
    metrics:
      - sistema no se cae
      - recovery time < 5 minutos
  
  - name: "Carga extrema"
    duration: "5 minutos"
    target: "50000 eventos/segundo"
    metrics:
      - throttling activado
      - mensajes en DLQ < 1%
```

**Esfuerzo Estimado:** 2-3 días  
**Responsable:** SRE Team + Backend Team

---

### INC-009-002: Falta Estrategia de Throttling y Backpressure
**Prioridad:** 🟡 MEDIA  
**Artículo:** Art. 12  
**Ubicación:** `TelemetryService`, `TelemetryWorker`, configuración RabbitMQ

**Descripción:**
El sistema no implementa mecanismos explícitos de throttling o backpressure cuando la carga excede la capacidad de procesamiento. Esto puede llevar a degradación de servicio o pérdida de eventos.

**Evidencia:**
- Telemetry Service acepta eventos sin límite de tasa
- RabbitMQ no tiene configuración de backpressure explícita
- Workers procesan sin límite de mensajes concurrentes
- No hay circuit breaker si Workers fallan repetidamente

**Impacto:**
- ⚠️ Riesgo de saturación de RabbitMQ bajo carga extrema
- ⚠️ Workers pueden sobrecargarse y fallar
- ⚠️ Posible pérdida de eventos si cola se llena
- ⚠️ Degradación de latencia para otros servicios

**Recomendación:**
1. Implementar rate limiting en Telemetry Service:
   ```java
   @RateLimiter(name = "telemetry-ingestion", 
                fallbackMethod = "throttleResponse")
   @PostMapping("/api/v1/aios/telemetry/events")
   public ResponseEntity<?> receiveTelemetry(...) {
       // ...
   }
   ```

2. Configurar backpressure en RabbitMQ:
   ```yaml
   rabbitmq:
     queue:
       max-length: 100000  # Máximo mensajes en cola
       max-length-bytes: 1GB
       overflow: reject-publish  # Rechazar nuevos mensajes si cola llena
   ```

3. Implementar circuit breaker en Workers:
   ```java
   @CircuitBreaker(name = "telemetry-processing",
                   fallbackMethod = "fallbackProcessing")
   public void processTelemetryEvent(...) {
       // ...
   }
   ```

4. Alertar cuando:
   - Cola RabbitMQ > 80% capacidad
   - Workers procesando > 90% CPU
   - Latencia p95 > umbral

**Acción Correctiva:**
```java
// TelemetryService con rate limiting
@RestController
public class TelemetryController {
    
    @RateLimiter(name = "telemetry-ingestion")
    @PostMapping("/api/v1/aios/telemetry/events")
    public ResponseEntity<?> receiveTelemetry(
            @RequestBody TelemetryEventRequestDto event) {
        
        // Verificar capacidad de cola
        if (rabbitMQQueueSize() > MAX_QUEUE_SIZE * 0.8) {
            return ResponseEntity.status(503)
                .body(Map.of("error", "Service temporarily unavailable",
                             "retry_after", 60));
        }
        
        rabbitTemplate.convertAndSend(RabbitMQConfig.TELEMETRY_QUEUE, event);
        return ResponseEntity.accepted().build();
    }
    
    private ResponseEntity<?> throttleResponse(Exception ex) {
        return ResponseEntity.status(429)
            .body(Map.of("error", "Too many requests",
                         "retry_after", 10));
    }
}
```

**Esfuerzo Estimado:** 2-3 días  
**Responsable:** Backend Team

---

### INC-009-003: Análisis en Tiempo Real Puede Afectar Latencia
**Prioridad:** 🟡 MEDIA  
**Artículo:** Art. 12  
**Ubicación:** `RealtimeGovernanceServiceImpl`

**Descripción:**
El análisis de gobernanza (bias, PII, secretos, compliance) se ejecuta síncronamente en el Worker, lo que puede aumentar la latencia de procesamiento si hay múltiples análisis pesados.

**Evidencia:**
- Todos los análisis se ejecutan secuencialmente en `processTelemetryEvent()`
- Análisis de bias puede llamar a microservicio externo (latencia adicional)
- Análisis de compliance puede ejecutar reglas Drools complejas
- No hay diferenciación entre análisis críticos (síncronos) y no críticos (asíncronos)

**Impacto:**
- ⚠️ Latencia de procesamiento puede aumentar significativamente
- ⚠️ Workers pueden tardar más en procesar eventos
- ⚠️ Cola RabbitMQ puede crecer si procesamiento es lento
- ⚠️ Análisis no críticos bloquean análisis críticos

**Recomendación:**
1. Clasificar análisis por criticidad:
   - **Críticos (síncronos):** Detección de secretos, PII, compliance violations
   - **No críticos (asíncronos):** Análisis de bias, toxicidad (puede ejecutarse después)

2. Implementar procesamiento asíncrono para análisis no críticos:
   ```java
   // Análisis crítico: síncrono
   if (payload.getRequiresSecretDetection()) {
       SecretDetectionResult result = secretDetectionService.detect(event);
       if (result.hasFindings()) {
           criticalIssueDetected = true;
       }
   }
   
   // Análisis no crítico: asíncrono
   if (payload.getRequiresBiasCheck()) {
       CompletableFuture.supplyAsync(() -> 
           biasDetectionService.detect(event)
       ).thenAccept(result -> {
           // Guardar resultado en BD
           updateTelemetryWithBiasResult(telemetry.getId(), result);
       });
   }
   ```

3. Implementar timeout para análisis síncronos:
   ```java
   @Timeout(value = 5, unit = TimeUnit.SECONDS)
   public SecretDetectionResult detect(TelemetryEventRequestDto event) {
       // ...
   }
   ```

4. Monitorear latencia de cada tipo de análisis

**Acción Correctiva:**
```java
// RealtimeGovernanceServiceImpl optimizado
public void processTelemetryEvent(TelemetryEventRequestDto event) {
    // 1. Guardar en BD (rápido)
    AioTelemetry telemetry = saveTelemetry(event);
    
    // 2. Análisis críticos: síncronos
    boolean criticalIssueDetected = executeCriticalAnalysis(event, telemetry);
    
    // 3. Disparar BPMN si crítico
    if (criticalIssueDetected) {
        triggerIncidentProcess(telemetry, analysisResults);
    }
    
    // 4. Análisis no críticos: asíncronos (no bloquean)
    executeNonCriticalAnalysisAsync(event, telemetry);
}

private boolean executeCriticalAnalysis(
        TelemetryEventRequestDto event, 
        AioTelemetry telemetry) {
    // Solo análisis que requieren acción inmediata
    boolean critical = false;
    
    if (event.getPayload().getRequiresSecretDetection()) {
        SecretDetectionResult result = secretDetectionService
            .detect(event)
            .orTimeout(5, TimeUnit.SECONDS)
            .join();
        if (result.hasFindings()) {
            critical = true;
        }
    }
    
    if (event.getPayload().getRequiresPiiDetection()) {
        PiiDetectionResult result = piiDetectionService
            .detect(event)
            .orTimeout(5, TimeUnit.SECONDS)
            .join();
        if (result.hasFindings()) {
            critical = true;
        }
    }
    
    return critical;
}

private void executeNonCriticalAnalysisAsync(
        TelemetryEventRequestDto event,
        AioTelemetry telemetry) {
    // Análisis que no requieren acción inmediata
    if (event.getPayload().getRequiresBiasCheck()) {
        CompletableFuture.supplyAsync(() -> 
            biasDetectionService.detect(event)
        ).thenAccept(result -> {
            updateTelemetryWithBiasResult(telemetry.getId(), result);
        });
    }
    
    if (event.getPayload().getRequiresToxicityCheck()) {
        CompletableFuture.supplyAsync(() -> 
            toxicityDetectionService.detect(event)
        ).thenAccept(result -> {
            updateTelemetryWithToxicityResult(telemetry.getId(), result);
        });
    }
}
```

**Esfuerzo Estimado:** 3-5 días  
**Responsable:** Backend Team

---

## PARTE 2: INCIDENCIAS - DOCUMENTACIÓN Y CASOS REALES

### INC-009-004: Falta Documentación de Casos Reales de Producción
**Prioridad:** 🟡 MEDIA  
**Artículo:** Art. 15  
**Ubicación:** Documentación técnica

**Descripción:**
Aunque el sistema tiene casos documentados de detección automática (drift, secretos, bias), falta documentación más extensa de casos reales en producción con métricas de tiempo de respuesta y efectividad.

**Evidencia:**
- Casos documentados son ejemplos/escenarios
- No hay métricas de:
  * Tiempo promedio de detección (detección → alerta)
  * Tiempo promedio de respuesta (alerta → acción)
  * Tasa de falsos positivos
  * Tasa de detección exitosa
- No hay dashboard de casos resueltos automáticamente

**Impacto:**
- ⚠️ Dificulta evaluación de efectividad del sistema
- ⚠️ No se puede demostrar ROI de detección automática
- ⚠️ Dificulta mejora continua (no hay datos históricos)

**Recomendación:**
1. Crear dashboard de casos de detección automática:
   - Total incidentes detectados
   - Tiempo promedio de detección
   - Tiempo promedio de respuesta
   - Tasa de resolución automática vs manual
   - Falsos positivos

2. Documentar casos reales con:
   - Timestamp exacto de detección
   - Timestamp de acción correctiva
   - Impacto evitado (estimado)
   - Métricas de telemetría antes/después

3. Implementar tracking de métricas de efectividad:
   ```sql
   CREATE TABLE AIOINCIDENTMETRICS (
       IDXINCIDENTMETRIC BIGSERIAL PRIMARY KEY,
       INCINCIDENTID VARCHAR(36) NOT NULL,
       INCDETECTIONTIME TIMESTAMP NOT NULL,
       INCALERTTIME TIMESTAMP,
       INCACTIONTIME TIMESTAMP,
       INCRESOLUTIONTIME TIMESTAMP,
       INCDETECTIONLATENCYMS INTEGER,  -- detección → alerta
       INCALERTLATENCYMS INTEGER,      -- alerta → acción
       INCRESPONSELATENCYMS INTEGER,   -- detección → resolución
       INCFALSEPOSITIVE BOOLEAN DEFAULT FALSE,
       INCIMPACTESTIMATED VARCHAR(50)  -- LOW, MEDIUM, HIGH, CRITICAL
   );
   ```

4. Generar reporte mensual de efectividad

**Acción Correctiva:**
```java
// Servicio para tracking de métricas de incidentes
@Service
public class IncidentMetricsService {
    
    public void recordIncidentDetection(String incidentId) {
        IncidentMetric metric = new IncidentMetric();
        metric.setIncidentId(incidentId);
        metric.setDetectionTime(OffsetDateTime.now());
        incidentMetricsRepository.save(metric);
    }
    
    public void recordIncidentAlert(String incidentId) {
        IncidentMetric metric = incidentMetricsRepository
            .findByIncidentId(incidentId)
            .orElseThrow();
        metric.setAlertTime(OffsetDateTime.now());
        metric.setDetectionLatencyMs(
            Duration.between(
                metric.getDetectionTime(),
                metric.getAlertTime()
            ).toMillis()
        );
        incidentMetricsRepository.save(metric);
    }
    
    public void recordIncidentResolution(String incidentId, boolean falsePositive) {
        IncidentMetric metric = incidentMetricsRepository
            .findByIncidentId(incidentId)
            .orElseThrow();
        metric.setResolutionTime(OffsetDateTime.now());
        metric.setFalsePositive(falsePositive);
        metric.setResponseLatencyMs(
            Duration.between(
                metric.getDetectionTime(),
                metric.getResolutionTime()
            ).toMillis()
        );
        incidentMetricsRepository.save(metric);
    }
    
    public IncidentMetricsReport generateMonthlyReport(int year, int month) {
        // Calcular métricas agregadas
        // - Tiempo promedio de detección
        // - Tiempo promedio de respuesta
        // - Tasa de falsos positivos
        // - Tasa de resolución automática
        // ...
    }
}
```

**Esfuerzo Estimado:** 2-3 días  
**Responsable:** Backend Team + Analytics Team

---

### INC-009-005: Falta Optimización de Queries de Telemetría
**Prioridad:** 🟢 BAJA  
**Artículo:** Art. 12  
**Ubicación:** `TelemetryRepository`, queries SQL

**Descripción:**
Algunas queries de telemetría pueden ser lentas cuando se consultan grandes volúmenes de datos históricos, especialmente queries que escanean JSONB fields sin índices GIN adecuados.

**Evidencia:**
- Queries que filtran por campos dentro de JSONB pueden ser lentas
- No hay índices parciales para queries frecuentes
- Continuous aggregates solo cubren estadísticas diarias, no por hora

**Impacto:**
- ⚠️ Dashboards pueden ser lentos al cargar datos históricos
- ⚠️ Análisis de drift puede tardar si consulta mucha telemetría
- ⚠️ Experiencia de usuario degradada

**Recomendación:**
1. Crear índices GIN adicionales para campos JSONB frecuentes:
   ```sql
   -- Índice para búsqueda por severity en payload
   CREATE INDEX idx_tel_payload_severity_gin 
   ON AIOTELEMETRY 
   USING GIN ((TELPAYLOAD->>'severity'))
   WHERE TELPAYLOAD->>'severity' IS NOT NULL;
   
   -- Índice para búsqueda por event_type en payload
   CREATE INDEX idx_tel_payload_event_type_gin 
   ON AIOTELEMETRY 
   USING GIN ((TELPAYLOAD->>'event_type'))
   WHERE TELPAYLOAD->>'event_type' IS NOT NULL;
   ```

2. Crear continuous aggregates por hora para análisis más granulares:
   ```sql
   CREATE MATERIALIZED VIEW telemetry_hourly_stats
   WITH (timescaledb.continuous) AS
   SELECT 
       time_bucket('1 hour', TELTIMESTAMP) AS hour,
       TELCOMPONENTUUID,
       TELEVENTTYPE,
       COUNT(*) AS event_count,
       AVG((TELMETRICS->>'latency_ms')::numeric) AS avg_latency_ms
   FROM AIOTELEMETRY
   GROUP BY hour, TELCOMPONENTUUID, TELEVENTTYPE;
   ```

3. Implementar paginación eficiente para queries grandes:
   ```java
   // Usar cursor-based pagination en lugar de offset
   public List<AioTelemetry> findByComponentUuidAfter(
           String componentUuid, 
           Long lastId,
           int limit) {
       return telemetryRepository.findByComponentUuidAndIdxtelemetryGreaterThan(
           componentUuid, lastId, PageRequest.of(0, limit)
       );
   }
   ```

4. Cachear resultados de queries frecuentes (Redis)

**Esfuerzo Estimado:** 2-3 días  
**Responsable:** DBA Team + Backend Team

---

### INC-009-006: Falta Monitoreo de Salud del Sistema de Telemetría
**Prioridad:** 🟢 BAJA  
**Artículo:** Art. 12  
**Ubicación:** Dashboards, alertas

**Descripción:**
Aunque el sistema monitorea la salud de los componentes AI (ai-runtime-health-v1), no hay monitoreo específico de la salud del propio sistema de telemetría (Telemetry Service, Workers, RabbitMQ).

**Evidencia:**
- No hay dashboard específico para salud de telemetría
- No hay alertas si Telemetry Service está caído
- No hay alertas si Workers no procesan mensajes
- No hay métricas de lag de procesamiento (eventos en cola)

**Impacto:**
- ⚠️ No se detecta rápidamente si sistema de telemetría falla
- ⚠️ Eventos pueden perderse sin saberlo
- ⚠️ Dificulta troubleshooting

**Recomendación:**
1. Crear dashboard de salud de telemetría:
   - Estado de Telemetry Service (UP/DOWN)
   - Número de Workers activos
   - Tamaño de cola RabbitMQ
   - Lag de procesamiento (eventos en cola)
   - Throughput (eventos/segundo)
   - Latencia de procesamiento (p95, p99)
   - Tasa de error

2. Implementar health checks:
   ```java
   @Component
   public class TelemetryHealthIndicator implements HealthIndicator {
       
       @Override
       public Health health() {
           // Verificar:
           // - RabbitMQ conectado
           // - Workers procesando
           // - Cola no saturada
           // - BD accesible
           
           if (allChecksPass()) {
               return Health.up()
                   .withDetail("queue_size", rabbitMQQueueSize())
                   .withDetail("workers_active", activeWorkersCount())
                   .build();
           } else {
               return Health.down()
                   .withDetail("reason", "Telemetry system degraded")
                   .build();
           }
       }
   }
   ```

3. Configurar alertas:
   - Telemetry Service DOWN
   - Cola RabbitMQ > 80% capacidad
   - Lag de procesamiento > 5 minutos
   - Workers no procesan mensajes > 1 minuto
   - Tasa de error > 1%

**Esfuerzo Estimado:** 1-2 días  
**Responsable:** SRE Team + Backend Team

---

## RESUMEN DE ACCIONES

| ID | Prioridad | Esfuerzo | Responsable | Estado |
|----|-----------|----------|-------------|--------|
| INC-009-001 | 🟡 MEDIA | 2-3 días | SRE + Backend | ⏳ Pendiente |
| INC-009-002 | 🟡 MEDIA | 2-3 días | Backend | ⏳ Pendiente |
| INC-009-003 | 🟡 MEDIA | 3-5 días | Backend | ⏳ Pendiente |
| INC-009-004 | 🟡 MEDIA | 2-3 días | Backend + Analytics | ⏳ Pendiente |
| INC-009-005 | 🟢 BAJA | 2-3 días | DBA + Backend | ⏳ Pendiente |
| INC-009-006 | 🟢 BAJA | 1-2 días | SRE + Backend | ⏳ Pendiente |

**Total Esfuerzo Estimado:** 12-19 días  
**Prioridad Alta (🟡):** 4 incidencias (9-13 días)  
**Prioridad Baja (🟢):** 2 incidencias (3-5 días)

---

**Fin del Documento de Incidencias y Recomendaciones**

