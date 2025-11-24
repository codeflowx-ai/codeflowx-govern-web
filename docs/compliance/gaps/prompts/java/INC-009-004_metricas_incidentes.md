# PROMPT: INC-009-004 - Métricas de Incidentes
## Monitorización en Tiempo Real - EU AI Act Art. 15

**Incidencia:** INC-009-004  
**Prioridad:** 🟡 MEDIA  
**Artículo:** Art. 15  
**Estado:** ✅ COMPLETADO

---

## DESCRIPCIÓN

Crear sistema de tracking de métricas de incidentes para medir efectividad de detección automática: tiempo de detección, tiempo de respuesta, tasa de falsos positivos, etc.

---

## REQUISITOS

### 1. Entidad JPA - IncidentMetrics

**Tabla:** `AIOINCIDENTMETRICS`  
**Prefijo:** INC  
**Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/telemetry/AioIncidentMetric.java`

**Campos:**
- `IDXINCIDENTMETRIC` (BIGSERIAL, PK)
- `iduuid` (VARCHAR(36), UNIQUE)
- `INCINCIDENTID` (VARCHAR(36), FK a incidente)
- `INCDETECTIONTIME` (TIMESTAMP) - Tiempo de detección
- `INCALERTTIME` (TIMESTAMP) - Tiempo de alerta
- `INCACTIONTIME` (TIMESTAMP) - Tiempo de acción
- `INCRESOLUTIONTIME` (TIMESTAMP) - Tiempo de resolución
- `INCDETECTIONLATENCYMS` (INTEGER) - Latencia detección → alerta (ms)
- `INCALERTLATENCYMS` (INTEGER) - Latencia alerta → acción (ms)
- `INCRESPONSELATENCYMS` (INTEGER) - Latencia detección → resolución (ms)
- `INCFALSEPOSITIVE` (BOOLEAN) - Es falso positivo?
- `INCIMPACTESTIMATED` (VARCHAR(50)) - Impacto estimado: LOW, MEDIUM, HIGH, CRITICAL
- `INCCREATEDAT` (TIMESTAMP)
- `INCUPDATEDAT` (TIMESTAMP)

### 2. Repository

**Ubicación:** `codeflowx-aios-telemetry-worker/src/main/java/com/codeflowx/aios/telemetry/worker/repository/AioIncidentMetricRepository.java`

```java
public interface AioIncidentMetricRepository extends JpaRepository<AioIncidentMetric, Long> {
    Optional<AioIncidentMetric> findByIncidentId(String incidentId);
    List<AioIncidentMetric> findByDetectionTimeBetween(OffsetDateTime start, OffsetDateTime end);
    List<AioIncidentMetric> findByFalsePositive(boolean falsePositive);
}
```

### 3. Service - IncidentMetricsService

**Ubicación:** `codeflowx-aios-telemetry-worker/src/main/java/com/codeflowx/aios/telemetry/worker/service/IncidentMetricsService.java`

**Métodos:**
- `recordIncidentDetection(String incidentId)` - Registrar detección
- `recordIncidentAlert(String incidentId)` - Registrar alerta
- `recordIncidentAction(String incidentId)` - Registrar acción
- `recordIncidentResolution(String incidentId, boolean falsePositive, String impactEstimated)` - Registrar resolución
- `generateMonthlyReport(int year, int month)` - Generar reporte mensual

### 4. Reporte Mensual

**DTO:** `IncidentMetricsReportDto`

**Métricas:**
- Total incidentes detectados
- Tiempo promedio de detección (detección → alerta)
- Tiempo promedio de respuesta (alerta → acción)
- Tiempo promedio de resolución (detección → resolución)
- Tasa de falsos positivos (%)
- Tasa de resolución automática vs manual
- Distribución por tipo de incidente (DRIFT, SECURITY, COMPLIANCE, BIAS)
- Distribución por impacto (LOW, MEDIUM, HIGH, CRITICAL)

---

## INTEGRACIÓN

### 1. En RealtimeGovernanceServiceImpl

```java
@Autowired
private IncidentMetricsService incidentMetricsService;

// Cuando se detecta problema crítico
if (criticalIssueDetected) {
    String incidentId = UUID.randomUUID().toString();
    incidentMetricsService.recordIncidentDetection(incidentId);
    
    processInstanceId = flowableProcessService.startAlertResponseProcess(...);
}
```

### 2. En Proceso BPMN

Cuando se envía alerta:
```java
incidentMetricsService.recordIncidentAlert(incidentId);
```

Cuando se toma acción:
```java
incidentMetricsService.recordIncidentAction(incidentId);
```

Cuando se resuelve:
```java
incidentMetricsService.recordIncidentResolution(incidentId, falsePositive, impact);
```

---

## SCRIPT SQL

**Archivo:** `nocode.service.entitys/src/main/resources/sql/ai_incident_metrics.sql`

```sql
CREATE TABLE AIOINCIDENTMETRICS (
    IDXINCIDENTMETRIC BIGSERIAL PRIMARY KEY,
    iduuid VARCHAR(36) UNIQUE NOT NULL DEFAULT uuid_generate_v4()::text,
    INCINCIDENTID VARCHAR(36) NOT NULL,
    INCDETECTIONTIME TIMESTAMP NOT NULL,
    INCALERTTIME TIMESTAMP,
    INCACTIONTIME TIMESTAMP,
    INCRESOLUTIONTIME TIMESTAMP,
    INCDETECTIONLATENCYMS INTEGER,
    INCALERTLATENCYMS INTEGER,
    INCRESPONSELATENCYMS INTEGER,
    INCFALSEPOSITIVE BOOLEAN DEFAULT FALSE,
    INCIMPACTESTIMATED VARCHAR(50),
    INCCREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INCUPDATEDAT TIMESTAMP,
    CONSTRAINT fk_incident_metric UNIQUE (INCINCIDENTID)
);

CREATE INDEX idx_inc_incident_id ON AIOINCIDENTMETRICS(INCINCIDENTID);
CREATE INDEX idx_inc_detection_time ON AIOINCIDENTMETRICS(INCDETECTIONTIME DESC);
CREATE INDEX idx_inc_false_positive ON AIOINCIDENTMETRICS(INCFALSEPOSITIVE);
```

---

## DASHBOARD

### Endpoint REST

**GET** `/api/v1/aios/telemetry/incidents/metrics`

**Query Params:**
- `year` (opcional)
- `month` (opcional)
- `type` (opcional): DRIFT, SECURITY, COMPLIANCE, BIAS

**Response:**
```json
{
  "period": "2025-11",
  "total_incidents": 45,
  "avg_detection_latency_ms": 1250,
  "avg_alert_latency_ms": 3200,
  "avg_response_latency_ms": 15600,
  "false_positive_rate": 0.08,
  "auto_resolution_rate": 0.65,
  "by_type": {
    "DRIFT": 20,
    "SECURITY": 15,
    "COMPLIANCE": 7,
    "BIAS": 3
  },
  "by_impact": {
    "LOW": 10,
    "MEDIUM": 20,
    "HIGH": 12,
    "CRITICAL": 3
  }
}
```

---

## TESTING

### Test 1: Registro de Métricas

```java
@Test
void testRecordIncidentMetrics() {
    String incidentId = UUID.randomUUID().toString();
    
    // Registrar detección
    metricsService.recordIncidentDetection(incidentId);
    
    // Registrar alerta (1 segundo después)
    Thread.sleep(1000);
    metricsService.recordIncidentAlert(incidentId);
    
    // Verificar latencia
    AioIncidentMetric metric = repository.findByIncidentId(incidentId).orElseThrow();
    assertNotNull(metric.getIncDetectionlatencyms());
    assertTrue(metric.getIncDetectionlatencyms() >= 1000);
}
```

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_009_MONITORIZACION_TIEMPO_REAL.md#inc-009-004`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_009_MONITORIZACION_TIEMPO_REAL.md`

---

**Estado:** ✅ COMPLETADO  
**Esfuerzo Estimado:** 2-3 días  
**Responsable:** Backend Team + Analytics Team

