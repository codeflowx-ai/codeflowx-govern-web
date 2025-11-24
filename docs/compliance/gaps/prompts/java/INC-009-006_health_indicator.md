# PROMPT: INC-009-006 - Health Indicator para Telemetría
## Monitorización en Tiempo Real - EU AI Act Art. 12

**Incidencia:** INC-009-006  
**Prioridad:** 🟢 BAJA  
**Artículo:** Art. 12  
**Estado:** ✅ COMPLETADO

---

## DESCRIPCIÓN

Implementar HealthIndicator específico para el sistema de telemetría que monitoree: estado de Telemetry Service, Workers activos, tamaño de cola RabbitMQ, lag de procesamiento, etc.

---

## REQUISITOS

### 1. HealthIndicator

**Ubicación:** `codeflowx-aios-telemetry/src/main/java/com/codeflowx/aios/telemetry/health/TelemetryHealthIndicator.java`

**Checks:**
- ✅ RabbitMQ conectado
- ✅ Workers procesando (último mensaje procesado < 1 minuto)
- ✅ Cola no saturada (< 80% capacidad)
- ✅ BD accesible
- ✅ Throughput normal (eventos/segundo dentro de rango esperado)

**Response:**
```json
{
  "status": "UP",
  "details": {
    "rabbitmq": {
      "status": "UP",
      "queue_size": 5000,
      "queue_usage": 0.05,
      "max_queue_size": 100000
    },
    "workers": {
      "status": "UP",
      "active_workers": 5,
      "last_message_processed": "2025-11-17T10:30:00Z",
      "lag_seconds": 0
    },
    "database": {
      "status": "UP"
    },
    "throughput": {
      "status": "UP",
      "events_per_second": 150,
      "expected_range": "100-200"
    }
  }
}
```

### 2. Métricas Expuestas

**Endpoint:** `/actuator/health/telemetry`

**Métricas:**
- `telemetry.health.status`: UP, DOWN, DEGRADED
- `telemetry.queue.size`: Tamaño actual de cola
- `telemetry.queue.usage`: Porcentaje de uso (0.0 - 1.0)
- `telemetry.workers.active`: Número de workers activos
- `telemetry.workers.lag_seconds`: Lag de procesamiento (segundos)
- `telemetry.throughput.events_per_second`: Throughput actual

### 3. Alertas

**Condiciones para alertar:**
- Telemetry Service DOWN
- Cola RabbitMQ > 80% capacidad
- Lag de procesamiento > 5 minutos
- Workers no procesan mensajes > 1 minuto
- Tasa de error > 1%

---

## IMPLEMENTACIÓN

### 1. TelemetryHealthIndicator

```java
@Component
public class TelemetryHealthIndicator implements HealthIndicator {
    
    private final RabbitAdmin rabbitAdmin;
    private final RabbitTemplate rabbitTemplate;
    private final AioTelemetryRepository telemetryRepository;
    
    @Value("${telemetry.queue.max-size:100000}")
    private int maxQueueSize;
    
    @Value("${telemetry.workers.expected-count:5}")
    private int expectedWorkers;
    
    @Override
    public Health health() {
        Health.Builder builder = new Health.Builder();
        Map<String, Object> details = new HashMap<>();
        boolean allChecksPass = true;
        
        // Check RabbitMQ
        HealthStatus rabbitmqStatus = checkRabbitMQ(details);
        if (rabbitmqStatus != HealthStatus.UP) {
            allChecksPass = false;
        }
        
        // Check Workers
        HealthStatus workersStatus = checkWorkers(details);
        if (workersStatus != HealthStatus.UP) {
            allChecksPass = false;
        }
        
        // Check Database
        HealthStatus dbStatus = checkDatabase(details);
        if (dbStatus != HealthStatus.UP) {
            allChecksPass = false;
        }
        
        // Check Throughput
        HealthStatus throughputStatus = checkThroughput(details);
        if (throughputStatus != HealthStatus.UP) {
            allChecksPass = false;
        }
        
        if (allChecksPass) {
            return builder.up()
                .withDetails(details)
                .build();
        } else {
            return builder.down()
                .withDetails(details)
                .build();
        }
    }
    
    private HealthStatus checkRabbitMQ(Map<String, Object> details) {
        try {
            QueueInformation queueInfo = rabbitAdmin.getQueueInfo("aios.telemetry.events");
            if (queueInfo == null) {
                details.put("rabbitmq", Map.of("status", "DOWN", "reason", "Queue not found"));
                return HealthStatus.DOWN;
            }
            
            int queueSize = queueInfo.getMessageCount();
            double queueUsage = (double) queueSize / maxQueueSize;
            
            Map<String, Object> rabbitmqDetails = new HashMap<>();
            rabbitmqDetails.put("status", queueUsage < 0.8 ? "UP" : "DEGRADED");
            rabbitmqDetails.put("queue_size", queueSize);
            rabbitmqDetails.put("queue_usage", queueUsage);
            rabbitmqDetails.put("max_queue_size", maxQueueSize);
            
            details.put("rabbitmq", rabbitmqDetails);
            
            return queueUsage < 0.8 ? HealthStatus.UP : HealthStatus.DEGRADED;
        } catch (Exception ex) {
            details.put("rabbitmq", Map.of("status", "DOWN", "error", ex.getMessage()));
            return HealthStatus.DOWN;
        }
    }
    
    private HealthStatus checkWorkers(Map<String, Object> details) {
        try {
            // Obtener último mensaje procesado
            AioTelemetry lastProcessed = telemetryRepository
                .findTopByOrderByTeltimestampDesc()
                .orElse(null);
            
            if (lastProcessed == null) {
                details.put("workers", Map.of("status", "UNKNOWN", "reason", "No messages processed"));
                return HealthStatus.UNKNOWN;
            }
            
            long lagSeconds = Duration.between(
                lastProcessed.getTeltimestamp().toInstant(),
                Instant.now()
            ).getSeconds();
            
            Map<String, Object> workersDetails = new HashMap<>();
            workersDetails.put("status", lagSeconds < 60 ? "UP" : "DEGRADED");
            workersDetails.put("last_message_processed", lastProcessed.getTeltimestamp());
            workersDetails.put("lag_seconds", lagSeconds);
            
            details.put("workers", workersDetails);
            
            return lagSeconds < 60 ? HealthStatus.UP : HealthStatus.DEGRADED;
        } catch (Exception ex) {
            details.put("workers", Map.of("status", "DOWN", "error", ex.getMessage()));
            return HealthStatus.DOWN;
        }
    }
    
    private HealthStatus checkDatabase(Map<String, Object> details) {
        try {
            telemetryRepository.count();
            details.put("database", Map.of("status", "UP"));
            return HealthStatus.UP;
        } catch (Exception ex) {
            details.put("database", Map.of("status", "DOWN", "error", ex.getMessage()));
            return HealthStatus.DOWN;
        }
    }
    
    private HealthStatus checkThroughput(Map<String, Object> details) {
        try {
            // Calcular throughput últimos 60 segundos
            OffsetDateTime oneMinuteAgo = OffsetDateTime.now().minusMinutes(1);
            long eventCount = telemetryRepository.countByTeltimestampAfter(
                Timestamp.from(oneMinuteAgo.toInstant())
            );
            double eventsPerSecond = eventCount / 60.0;
            
            Map<String, Object> throughputDetails = new HashMap<>();
            throughputDetails.put("status", "UP");
            throughputDetails.put("events_per_second", eventsPerSecond);
            throughputDetails.put("expected_range", "100-200");
            
            details.put("throughput", throughputDetails);
            
            return HealthStatus.UP;
        } catch (Exception ex) {
            details.put("throughput", Map.of("status", "DOWN", "error", ex.getMessage()));
            return HealthStatus.DOWN;
        }
    }
}
```

### 2. Repository Method

```java
// En AioTelemetryRepository
Optional<AioTelemetry> findTopByOrderByTeltimestampDesc();
long countByTeltimestampAfter(Timestamp timestamp);
```

---

## CONFIGURACIÓN

### application.yml

```yaml
management:
  endpoint:
    health:
      show-details: when-authorized
      probes:
        enabled: true
  health:
    livenessState:
      enabled: true
    readinessState:
      enabled: true

telemetry:
  queue:
    max-size: ${TELEMETRY_QUEUE_MAX_SIZE:100000}
  workers:
    expected-count: ${TELEMETRY_WORKERS_EXPECTED:5}
```

---

## MONITOREO

### Alertas Prometheus

```yaml
# Alerta: Telemetry Service DOWN
- alert: TelemetryServiceDown
  expr: telemetry_health_status == 0
  for: 1m
  annotations:
    summary: "Telemetry Service is DOWN"

# Alerta: Cola saturada
- alert: TelemetryQueueSaturated
  expr: telemetry_queue_usage > 0.8
  for: 5m
  annotations:
    summary: "Telemetry queue is > 80% capacity"

# Alerta: Lag de procesamiento
- alert: TelemetryProcessingLag
  expr: telemetry_workers_lag_seconds > 300
  for: 2m
  annotations:
    summary: "Telemetry processing lag > 5 minutes"
```

---

## TESTING

### Test 1: Health Check UP

```java
@Test
void testHealthCheckUp() {
    // Configurar mocks para todos los checks OK
    when(rabbitAdmin.getQueueInfo(anyString()))
        .thenReturn(new QueueInformation(5000, 0));
    when(telemetryRepository.findTopByOrderByTeltimestampDesc())
        .thenReturn(Optional.of(createRecentTelemetry()));
    
    Health health = healthIndicator.health();
    
    assertEquals(Status.UP, health.getStatus());
    assertTrue(health.getDetails().containsKey("rabbitmq"));
    assertTrue(health.getDetails().containsKey("workers"));
}
```

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_009_MONITORIZACION_TIEMPO_REAL.md#inc-009-006`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_009_MONITORIZACION_TIEMPO_REAL.md`

---

**Estado:** ✅ COMPLETADO  
**Esfuerzo Estimado:** 1-2 días  
**Responsable:** SRE Team + Backend Team

