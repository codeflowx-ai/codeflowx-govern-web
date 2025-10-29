# 🚀 SERVING - MONITOREO

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación de monitoreo del módulo Serving

---

## 🎯 RESUMEN EJECUTIVO

El módulo **Serving** incluye monitoreo completo de:
- **Health Status:** Estado de salud de deployments
- **Performance Metrics:** Métricas de rendimiento
- **Resource Usage:** Uso de recursos
- **SLA Compliance:** Cumplimiento de SLA
- **Drift Detection:** Detección de drift

---

## 📊 MÉTRICAS PRINCIPALES

### **1. MÉTRICAS DE PERFORMANCE**

#### **Latencia de Predicciones**
```sql
-- Vista para latencia de predicciones
CREATE OR REPLACE VIEW srv_v_prediction_latency AS
SELECT 
    d.srv_id AS deployment_id,
    d.srv_model_name,
    d.srv_model_version,
    AVG(p.srv_latency_ms) AS avg_latency_ms,
    PERCENTILE_CONT(0.50) WITHIN GROUP (ORDER BY p.srv_latency_ms) AS p50_latency_ms,
    PERCENTILE_CONT(0.95) WITHIN GROUP (ORDER BY p.srv_latency_ms) AS p95_latency_ms,
    PERCENTILE_CONT(0.99) WITHIN GROUP (ORDER BY p.srv_latency_ms) AS p99_latency_ms,
    MAX(p.srv_latency_ms) AS max_latency_ms,
    COUNT(*) AS prediction_count,
    DATE_TRUNC('hour', p.srv_timestamp) AS hour_timestamp
FROM srv_deployments d
JOIN srv_predictions p ON d.srv_id = p.srv_deployment_id
WHERE p.srv_timestamp >= CURRENT_TIMESTAMP - INTERVAL '24 hours'
GROUP BY d.srv_id, d.srv_model_name, d.srv_model_version, DATE_TRUNC('hour', p.srv_timestamp);
```

#### **Throughput**
```sql
-- Vista para throughput de predicciones
CREATE OR REPLACE VIEW srv_v_prediction_throughput AS
SELECT 
    d.srv_id AS deployment_id,
    d.srv_model_name,
    COUNT(p.srv_id) AS total_predictions,
    COUNT(p.srv_id) / EXTRACT(EPOCH FROM (MAX(p.srv_timestamp) - MIN(p.srv_timestamp))) AS predictions_per_second,
    DATE_TRUNC('minute', p.srv_timestamp) AS minute_timestamp
FROM srv_deployments d
JOIN srv_predictions p ON d.srv_id = p.srv_deployment_id
WHERE p.srv_timestamp >= CURRENT_TIMESTAMP - INTERVAL '1 hour'
GROUP BY d.srv_id, d.srv_model_name, DATE_TRUNC('minute', p.srv_timestamp);
```

#### **Error Rate**
```sql
-- Vista para tasa de errores
CREATE OR REPLACE VIEW srv_v_error_rate AS
SELECT 
    d.srv_id AS deployment_id,
    d.srv_model_name,
    COUNT(*) FILTER (WHERE p.srv_error IS NOT NULL) AS error_count,
    COUNT(*) AS total_predictions,
    (COUNT(*) FILTER (WHERE p.srv_error IS NOT NULL)::DECIMAL / COUNT(*)) * 100 AS error_rate_percent,
    DATE_TRUNC('hour', p.srv_timestamp) AS hour_timestamp
FROM srv_deployments d
JOIN srv_predictions p ON d.srv_id = p.srv_deployment_id
WHERE p.srv_timestamp >= CURRENT_TIMESTAMP - INTERVAL '24 hours'
GROUP BY d.srv_id, d.srv_model_name, DATE_TRUNC('hour', p.srv_timestamp);
```

---

### **2. MÉTRICAS DE RECURSOS**

#### **CPU y Memoria**
```sql
-- Vista para uso de CPU y memoria
CREATE OR REPLACE VIEW srv_v_resource_usage AS
SELECT 
    d.srv_id AS deployment_id,
    d.srv_model_name,
    d.srv_replicas,
    AVG(m.srv_cpu_usage_percent) AS avg_cpu_usage,
    MAX(m.srv_cpu_usage_percent) AS max_cpu_usage,
    AVG(m.srv_memory_usage_percent) AS avg_memory_usage,
    MAX(m.srv_memory_usage_percent) AS max_memory_usage,
    AVG(m.srv_disk_usage_percent) AS avg_disk_usage,
    DATE_TRUNC('minute', m.srv_timestamp) AS minute_timestamp
FROM srv_deployments d
JOIN srv_resource_metrics m ON d.srv_id = m.srv_deployment_id
WHERE m.srv_timestamp >= CURRENT_TIMESTAMP - INTERVAL '1 hour'
GROUP BY d.srv_id, d.srv_model_name, d.srv_replicas, DATE_TRUNC('minute', m.srv_timestamp);
```

#### **Réplicas Disponibles**
```sql
-- Vista para estado de réplicas
CREATE OR REPLACE VIEW srv_v_replica_status AS
SELECT 
    d.srv_id AS deployment_id,
    d.srv_model_name,
    d.srv_replicas AS desired_replicas,
    COUNT(r.srv_id) FILTER (WHERE r.srv_status = 'RUNNING') AS running_replicas,
    COUNT(r.srv_id) FILTER (WHERE r.srv_status = 'FAILED') AS failed_replicas,
    COUNT(r.srv_id) FILTER (WHERE r.srv_status = 'PENDING') AS pending_replicas,
    (COUNT(r.srv_id) FILTER (WHERE r.srv_status = 'RUNNING')::DECIMAL / d.srv_replicas) * 100 AS availability_percent
FROM srv_deployments d
LEFT JOIN srv_replicas r ON d.srv_id = r.srv_deployment_id
GROUP BY d.srv_id, d.srv_model_name, d.srv_replicas;
```

---

### **3. MÉTRICAS DE SLA**

#### **Compliance Dashboard**
```sql
-- Vista para compliance de SLA
CREATE OR REPLACE VIEW srv_v_sla_compliance AS
SELECT 
    d.srv_id AS deployment_id,
    d.srv_model_name,
    s.srv_max_response_time_ms AS sla_max_latency,
    AVG(p.srv_latency_ms) AS actual_avg_latency,
    CASE 
        WHEN AVG(p.srv_latency_ms) <= s.srv_max_response_time_ms THEN 'COMPLIANT'
        ELSE 'NON_COMPLIANT'
    END AS latency_compliance,
    s.srv_min_availability AS sla_min_availability,
    (COUNT(*) FILTER (WHERE p.srv_error IS NULL)::DECIMAL / COUNT(*)) * 100 AS actual_availability,
    CASE 
        WHEN (COUNT(*) FILTER (WHERE p.srv_error IS NULL)::DECIMAL / COUNT(*)) * 100 >= s.srv_min_availability THEN 'COMPLIANT'
        ELSE 'NON_COMPLIANT'
    END AS availability_compliance,
    s.srv_max_error_rate AS sla_max_error_rate,
    (COUNT(*) FILTER (WHERE p.srv_error IS NOT NULL)::DECIMAL / COUNT(*)) * 100 AS actual_error_rate,
    CASE 
        WHEN (COUNT(*) FILTER (WHERE p.srv_error IS NOT NULL)::DECIMAL / COUNT(*)) * 100 <= s.srv_max_error_rate THEN 'COMPLIANT'
        ELSE 'NON_COMPLIANT'
    END AS error_rate_compliance
FROM srv_deployments d
JOIN srv_sla_config s ON d.srv_id = s.srv_deployment_id
JOIN srv_predictions p ON d.srv_id = p.srv_deployment_id
WHERE p.srv_timestamp >= CURRENT_TIMESTAMP - INTERVAL '24 hours'
GROUP BY d.srv_id, d.srv_model_name, s.srv_max_response_time_ms, s.srv_min_availability, s.srv_max_error_rate;
```

---

### **4. MÉTRICAS DE DRIFT**

#### **Data Drift Detection**
```sql
-- Vista para detección de drift
CREATE OR REPLACE VIEW srv_v_data_drift AS
SELECT 
    d.srv_id AS deployment_id,
    d.srv_model_name,
    dr.srv_feature_name,
    dr.srv_drift_score,
    dr.srv_drift_threshold,
    CASE 
        WHEN dr.srv_drift_score >= dr.srv_drift_threshold THEN 'DRIFT_DETECTED'
        ELSE 'NO_DRIFT'
    END AS drift_status,
    dr.srv_analysis_timestamp,
    dr.srv_recommendation
FROM srv_deployments d
JOIN srv_drift_analysis dr ON d.srv_id = dr.srv_deployment_id
WHERE dr.srv_analysis_timestamp >= CURRENT_TIMESTAMP - INTERVAL '7 days'
ORDER BY dr.srv_drift_score DESC;
```

---

## 🎯 DASHBOARDS Y VISUALIZACIONES

### **Dashboard Principal de Serving**

#### **Pantallas ZUL:**
1. **`deployment-health-dashboard.zul`** - Estado general de deployments
2. **`prediction-metrics-dashboard.zul`** - Métricas de predicciones
3. **`resource-monitoring-dashboard.zul`** - Monitoreo de recursos
4. **`sla-compliance-dashboard.zul`** - Cumplimiento de SLA
5. **`drift-detection-dashboard.zul`** - Detección de drift

---

## 🚨 ALERTAS Y NOTIFICACIONES

### **Configuración de Alertas**

```java
@Service
public class ServingAlertService {
    
    @Scheduled(fixedRate = 60000) // Cada minuto
    public void checkAlertConditions() {
        List<Deployment> deployments = deploymentService.getActiveDeployments();
        
        for (Deployment deployment : deployments) {
            // Check SLA violations
            checkSlaViolations(deployment);
            
            // Check resource thresholds
            checkResourceThresholds(deployment);
            
            // Check health status
            checkHealthStatus(deployment);
            
            // Check drift
            checkDriftThresholds(deployment);
        }
    }
    
    private void checkSlaViolations(Deployment deployment) {
        SlaMetrics metrics = metricsService.getSlaMetrics(deployment.getId());
        SlaConfig config = slaService.getConfig(deployment.getId());
        
        // Latency alert
        if (metrics.getAvgLatency() > config.getMaxResponseTimeMs()) {
            alertService.sendAlert(Alert.builder()
                .type(AlertType.SLA_VIOLATION)
                .severity(AlertSeverity.HIGH)
                .title("Latency SLA Violation")
                .message(String.format(
                    "Deployment %s exceeds latency SLA: %.2fms > %.2fms",
                    deployment.getName(),
                    metrics.getAvgLatency(),
                    config.getMaxResponseTimeMs()
                ))
                .deploymentId(deployment.getId())
                .build());
        }
        
        // Availability alert
        if (metrics.getAvailability() < config.getMinAvailability()) {
            alertService.sendAlert(Alert.builder()
                .type(AlertType.SLA_VIOLATION)
                .severity(AlertSeverity.CRITICAL)
                .title("Availability SLA Violation")
                .message(String.format(
                    "Deployment %s below availability SLA: %.2f%% < %.2f%%",
                    deployment.getName(),
                    metrics.getAvailability(),
                    config.getMinAvailability()
                ))
                .deploymentId(deployment.getId())
                .build());
        }
    }
    
    private void checkResourceThresholds(Deployment deployment) {
        ResourceMetrics metrics = metricsService.getResourceMetrics(deployment.getId());
        
        // CPU alert
        if (metrics.getCpuUsage() > 80.0) {
            alertService.sendAlert(Alert.builder()
                .type(AlertType.HIGH_RESOURCE_USAGE)
                .severity(AlertSeverity.MEDIUM)
                .title("High CPU Usage")
                .message(String.format(
                    "Deployment %s CPU usage: %.2f%%",
                    deployment.getName(),
                    metrics.getCpuUsage()
                ))
                .deploymentId(deployment.getId())
                .build());
        }
        
        // Memory alert
        if (metrics.getMemoryUsage() > 85.0) {
            alertService.sendAlert(Alert.builder()
                .type(AlertType.HIGH_RESOURCE_USAGE)
                .severity(AlertSeverity.MEDIUM)
                .title("High Memory Usage")
                .message(String.format(
                    "Deployment %s memory usage: %.2f%%",
                    deployment.getName(),
                    metrics.getMemoryUsage()
                ))
                .deploymentId(deployment.getId())
                .build());
        }
    }
}
```

### **Tipos de Alertas:**

| Tipo | Severidad | Descripción |
|------|-----------|-------------|
| **SLA_VIOLATION** | CRITICAL | Violación de SLA |
| **HIGH_LATENCY** | HIGH | Latencia elevada |
| **HIGH_ERROR_RATE** | HIGH | Tasa de errores elevada |
| **LOW_AVAILABILITY** | CRITICAL | Disponibilidad baja |
| **HIGH_RESOURCE_USAGE** | MEDIUM | Uso de recursos elevado |
| **REPLICA_FAILURE** | HIGH | Fallo de réplica |
| **DATA_DRIFT** | MEDIUM | Drift detectado |
| **PREDICTION_ANOMALY** | MEDIUM | Anomalía en predicciones |

---

## 📊 INTEGRACIÓN CON PROMETHEUS

### **Métricas Expuestas:**

```java
@Component
public class ServingMetricsExporter {
    
    private final Counter predictionsTotal = Counter.builder("serving_predictions_total")
        .description("Total number of predictions")
        .labelNames("deployment_id", "model_name", "status")
        .register(Metrics.globalRegistry);
        
    private final Histogram predictionLatency = Histogram.builder("serving_prediction_latency_seconds")
        .description("Prediction latency in seconds")
        .labelNames("deployment_id", "model_name")
        .register(Metrics.globalRegistry);
        
    private final Gauge activeDeployments = Gauge.builder("serving_active_deployments")
        .description("Number of active deployments")
        .register(Metrics.globalRegistry);
        
    private final Gauge replicasRunning = Gauge.builder("serving_replicas_running")
        .description("Number of running replicas")
        .labelNames("deployment_id", "model_name")
        .register(Metrics.globalRegistry);
        
    public void recordPrediction(Prediction prediction) {
        predictionsTotal
            .labelValues(
                prediction.getDeploymentId().toString(),
                prediction.getModelName(),
                prediction.getError() == null ? "success" : "error"
            )
            .increment();
            
        predictionLatency
            .labelValues(
                prediction.getDeploymentId().toString(),
                prediction.getModelName()
            )
            .observe(prediction.getLatencyMs() / 1000.0);
    }
}
```

---

## 📈 GRAFANA DASHBOARDS

### **Dashboards Disponibles:**

1. **Serving Overview** - Vista general de todos los deployments
2. **Deployment Details** - Detalles de deployment específico
3. **Performance Metrics** - Métricas de rendimiento
4. **Resource Usage** - Uso de recursos
5. **SLA Compliance** - Cumplimiento de SLA
6. **Drift Analysis** - Análisis de drift

### **Ejemplo de Query Prometheus:**

```promql
# Latencia promedio por deployment
rate(serving_prediction_latency_seconds_sum[5m]) 
/ 
rate(serving_prediction_latency_seconds_count[5m])

# Tasa de errores
rate(serving_predictions_total{status="error"}[5m]) 
/ 
rate(serving_predictions_total[5m])

# Throughput
rate(serving_predictions_total[1m])
```

---

## 🎯 CONCLUSIÓN

El módulo **Serving** incluye monitoreo completo con:

- ✅ **Métricas de Performance** - Latencia, throughput, error rate
- ✅ **Métricas de Recursos** - CPU, memoria, disco, réplicas
- ✅ **Métricas de SLA** - Compliance en tiempo real
- ✅ **Detección de Drift** - Monitoreo continuo
- ✅ **Alertas Automáticas** - 8 tipos de alertas
- ✅ **Integración Prometheus** - Métricas exportadas
- ✅ **Dashboards Grafana** - 6 dashboards predefinidos
- ✅ **Vistas SQL** - 7 vistas especializadas

**Monitoreo end-to-end para todos los deployments de modelos ML.**
