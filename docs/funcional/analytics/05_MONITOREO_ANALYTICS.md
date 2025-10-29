# 📊 ANALYTICS - MONITOREO

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación de monitoreo del módulo Analytics

---

## 🎯 RESUMEN EJECUTIVO

El módulo **Analytics** incluye monitoreo completo de:
- **Data Quality:** Calidad de datos analíticos
- **Report Generation:** Generación de reportes
- **Query Performance:** Performance de consultas
- **System Health:** Salud del sistema de analytics
- **Data Freshness:** Actualización de datos

---

## 📊 MÉTRICAS PRINCIPALES

### **1. MÉTRICAS DE CALIDAD DE DATOS**

#### **Data Completeness**
```sql
-- Vista para completitud de datos
CREATE OR REPLACE VIEW anl_v_data_completeness AS
SELECT 
    entity_type,
    entity_id,
    metric_name,
    COUNT(*) AS total_records,
    COUNT(NULLIF(metric_value, 0)) AS non_null_records,
    (COUNT(NULLIF(metric_value, 0))::DECIMAL / COUNT(*)) * 100 AS completeness_percent,
    DATE_TRUNC('day', timestamp) AS date
FROM anl_metrics
WHERE timestamp >= CURRENT_TIMESTAMP - INTERVAL '7 days'
GROUP BY entity_type, entity_id, metric_name, DATE_TRUNC('day', timestamp);
```

#### **Data Accuracy**
```sql
-- Vista para precisión de datos
CREATE OR REPLACE VIEW anl_v_data_accuracy AS
SELECT 
    m.entity_type,
    m.entity_id,
    m.metric_name,
    AVG(m.metric_value) AS avg_value,
    STDDEV(m.metric_value) AS std_dev,
    (STDDEV(m.metric_value) / NULLIF(AVG(m.metric_value), 0)) AS coefficient_of_variation,
    CASE 
        WHEN (STDDEV(m.metric_value) / NULLIF(AVG(m.metric_value), 0)) < 0.1 THEN 'HIGH'
        WHEN (STDDEV(m.metric_value) / NULLIF(AVG(m.metric_value), 0)) < 0.3 THEN 'MEDIUM'
        ELSE 'LOW'
    END AS accuracy_rating
FROM anl_metrics m
WHERE m.timestamp >= CURRENT_TIMESTAMP - INTERVAL '30 days'
GROUP BY m.entity_type, m.entity_id, m.metric_name;
```

#### **Data Freshness**
```sql
-- Vista para frescura de datos
CREATE OR REPLACE VIEW anl_v_data_freshness AS
SELECT 
    entity_type,
    entity_id,
    metric_name,
    MAX(timestamp) AS last_update,
    EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - MAX(timestamp))) / 60 AS minutes_since_update,
    CASE 
        WHEN EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - MAX(timestamp))) / 60 < 5 THEN 'FRESH'
        WHEN EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - MAX(timestamp))) / 60 < 60 THEN 'ACCEPTABLE'
        WHEN EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - MAX(timestamp))) / 60 < 1440 THEN 'STALE'
        ELSE 'OUTDATED'
    END AS freshness_status
FROM anl_metrics
GROUP BY entity_type, entity_id, metric_name;
```

---

### **2. MÉTRICAS DE REPORTES**

#### **Report Generation Performance**
```sql
-- Vista para performance de generación de reportes
CREATE OR REPLACE VIEW anl_v_report_performance AS
SELECT 
    r.anl_report_type,
    COUNT(*) AS total_reports,
    AVG(EXTRACT(EPOCH FROM (r.anl_completed_at - r.anl_started_at))) AS avg_generation_time_seconds,
    PERCENTILE_CONT(0.50) WITHIN GROUP (ORDER BY EXTRACT(EPOCH FROM (r.anl_completed_at - r.anl_started_at))) AS p50_generation_time,
    PERCENTILE_CONT(0.95) WITHIN GROUP (ORDER BY EXTRACT(EPOCH FROM (r.anl_completed_at - r.anl_started_at))) AS p95_generation_time,
    COUNT(*) FILTER (WHERE r.anl_status = 'COMPLETED') AS successful_reports,
    COUNT(*) FILTER (WHERE r.anl_status = 'FAILED') AS failed_reports,
    (COUNT(*) FILTER (WHERE r.anl_status = 'COMPLETED')::DECIMAL / COUNT(*)) * 100 AS success_rate_percent,
    DATE_TRUNC('day', r.anl_started_at) AS date
FROM anl_reports r
WHERE r.anl_started_at >= CURRENT_TIMESTAMP - INTERVAL '30 days'
GROUP BY r.anl_report_type, DATE_TRUNC('day', r.anl_started_at);
```

#### **Report Usage Statistics**
```sql
-- Vista para estadísticas de uso de reportes
CREATE OR REPLACE VIEW anl_v_report_usage AS
SELECT 
    r.anl_report_type,
    COUNT(*) AS reports_generated,
    COUNT(DISTINCT r.anl_generated_by) AS unique_users,
    COUNT(rd.anl_id) AS total_downloads,
    AVG(rd.anl_download_count) AS avg_downloads_per_report,
    MAX(rd.anl_last_downloaded_at) AS last_downloaded
FROM anl_reports r
LEFT JOIN anl_report_downloads rd ON r.anl_id = rd.anl_report_id
WHERE r.anl_generated_at >= CURRENT_TIMESTAMP - INTERVAL '30 days'
GROUP BY r.anl_report_type;
```

---

### **3. MÉTRICAS DE QUERIES**

#### **Query Performance**
```sql
-- Vista para performance de queries
CREATE OR REPLACE VIEW anl_v_query_performance AS
SELECT 
    query_type,
    COUNT(*) AS total_queries,
    AVG(execution_time_ms) AS avg_execution_time,
    PERCENTILE_CONT(0.50) WITHIN GROUP (ORDER BY execution_time_ms) AS p50_execution_time,
    PERCENTILE_CONT(0.95) WITHIN GROUP (ORDER BY execution_time_ms) AS p95_execution_time,
    PERCENTILE_CONT(0.99) WITHIN GROUP (ORDER BY execution_time_ms) AS p99_execution_time,
    MAX(execution_time_ms) AS max_execution_time,
    COUNT(*) FILTER (WHERE execution_time_ms > 1000) AS slow_queries,
    DATE_TRUNC('hour', executed_at) AS hour
FROM anl_query_log
WHERE executed_at >= CURRENT_TIMESTAMP - INTERVAL '24 hours'
GROUP BY query_type, DATE_TRUNC('hour', executed_at);
```

#### **Query Cache Hit Rate**
```sql
-- Vista para tasa de acierto de caché
CREATE OR REPLACE VIEW anl_v_cache_hit_rate AS
SELECT 
    DATE_TRUNC('hour', executed_at) AS hour,
    COUNT(*) AS total_queries,
    COUNT(*) FILTER (WHERE cache_hit = true) AS cache_hits,
    COUNT(*) FILTER (WHERE cache_hit = false) AS cache_misses,
    (COUNT(*) FILTER (WHERE cache_hit = true)::DECIMAL / COUNT(*)) * 100 AS hit_rate_percent
FROM anl_query_log
WHERE executed_at >= CURRENT_TIMESTAMP - INTERVAL '24 hours'
GROUP BY DATE_TRUNC('hour', executed_at)
ORDER BY hour DESC;
```

---

### **4. MÉTRICAS DE SISTEMA**

#### **System Resource Usage**
```sql
-- Vista para uso de recursos del sistema
CREATE OR REPLACE VIEW anl_v_system_resources AS
SELECT 
    DATE_TRUNC('minute', timestamp) AS minute,
    AVG(cpu_usage_percent) AS avg_cpu_usage,
    MAX(cpu_usage_percent) AS max_cpu_usage,
    AVG(memory_usage_percent) AS avg_memory_usage,
    MAX(memory_usage_percent) AS max_memory_usage,
    AVG(disk_io_mbps) AS avg_disk_io,
    MAX(disk_io_mbps) AS max_disk_io
FROM anl_system_metrics
WHERE timestamp >= CURRENT_TIMESTAMP - INTERVAL '1 hour'
GROUP BY DATE_TRUNC('minute', timestamp)
ORDER BY minute DESC;
```

#### **Analytics Pipeline Health**
```sql
-- Vista para salud del pipeline de analytics
CREATE OR REPLACE VIEW anl_v_pipeline_health AS
SELECT 
    pipeline_name,
    pipeline_stage,
    COUNT(*) AS total_executions,
    COUNT(*) FILTER (WHERE status = 'SUCCESS') AS successful_executions,
    COUNT(*) FILTER (WHERE status = 'FAILED') AS failed_executions,
    AVG(duration_seconds) AS avg_duration,
    MAX(duration_seconds) AS max_duration,
    (COUNT(*) FILTER (WHERE status = 'SUCCESS')::DECIMAL / COUNT(*)) * 100 AS success_rate_percent,
    MAX(executed_at) AS last_execution
FROM anl_pipeline_executions
WHERE executed_at >= CURRENT_TIMESTAMP - INTERVAL '24 hours'
GROUP BY pipeline_name, pipeline_stage;
```

---

## 🚨 ALERTAS Y NOTIFICACIONES

### **Sistema de Alertas**

```java
@Service
public class AnalyticsAlertService {
    
    @Autowired
    private MetricsService metricsService;
    
    @Autowired
    private AlertService alertService;
    
    @Scheduled(fixedRate = 300000) // Cada 5 minutos
    public void checkAnalyticsAlerts() {
        // Check data freshness
        checkDataFreshness();
        
        // Check report generation
        checkReportGeneration();
        
        // Check query performance
        checkQueryPerformance();
        
        // Check system health
        checkSystemHealth();
    }
    
    private void checkDataFreshness() {
        List<DataFreshnessMetric> staleData = metricsService
            .getStaleMetrics(Duration.ofHours(1));
        
        for (DataFreshnessMetric metric : staleData) {
            if (metric.getMinutesSinceUpdate() > 60) {
                alertService.sendAlert(Alert.builder()
                    .type(AlertType.DATA_STALE)
                    .severity(AlertSeverity.HIGH)
                    .title("Stale Analytics Data")
                    .message(String.format(
                        "Metric %s for %s %d has not been updated for %d minutes",
                        metric.getMetricName(),
                        metric.getEntityType(),
                        metric.getEntityId(),
                        metric.getMinutesSinceUpdate()
                    ))
                    .build());
            }
        }
    }
    
    private void checkReportGeneration() {
        List<Report> failedReports = reportService.getFailedReports(
            LocalDateTime.now().minusHours(1)
        );
        
        if (!failedReports.isEmpty()) {
            alertService.sendAlert(Alert.builder()
                .type(AlertType.REPORT_GENERATION_FAILED)
                .severity(AlertSeverity.MEDIUM)
                .title("Report Generation Failures")
                .message(String.format(
                    "%d reports failed to generate in the last hour",
                    failedReports.size()
                ))
                .build());
        }
    }
    
    private void checkQueryPerformance() {
        QueryPerformanceMetrics metrics = metricsService
            .getQueryPerformanceMetrics(Duration.ofMinutes(15));
        
        // Alert on slow queries
        if (metrics.getP95ExecutionTime() > 5000) {
            alertService.sendAlert(Alert.builder()
                .type(AlertType.SLOW_QUERIES)
                .severity(AlertSeverity.MEDIUM)
                .title("Slow Query Performance")
                .message(String.format(
                    "P95 query execution time: %.2fms (threshold: 5000ms)",
                    metrics.getP95ExecutionTime()
                ))
                .build());
        }
        
        // Alert on low cache hit rate
        if (metrics.getCacheHitRate() < 70.0) {
            alertService.sendAlert(Alert.builder()
                .type(AlertType.LOW_CACHE_HIT_RATE)
                .severity(AlertSeverity.LOW)
                .title("Low Cache Hit Rate")
                .message(String.format(
                    "Cache hit rate: %.2f%% (threshold: 70%%)",
                    metrics.getCacheHitRate()
                ))
                .build());
        }
    }
    
    private void checkSystemHealth() {
        SystemMetrics metrics = metricsService.getSystemMetrics();
        
        // Alert on high CPU usage
        if (metrics.getCpuUsage() > 80.0) {
            alertService.sendAlert(Alert.builder()
                .type(AlertType.HIGH_CPU_USAGE)
                .severity(AlertSeverity.HIGH)
                .title("High CPU Usage - Analytics")
                .message(String.format(
                    "CPU usage: %.2f%%",
                    metrics.getCpuUsage()
                ))
                .build());
        }
        
        // Alert on high memory usage
        if (metrics.getMemoryUsage() > 85.0) {
            alertService.sendAlert(Alert.builder()
                .type(AlertType.HIGH_MEMORY_USAGE)
                .severity(AlertSeverity.HIGH)
                .title("High Memory Usage - Analytics")
                .message(String.format(
                    "Memory usage: %.2f%%",
                    metrics.getMemoryUsage()
                ))
                .build());
        }
    }
}
```

### **Tipos de Alertas:**

| Tipo | Severidad | Descripción |
|------|-----------|-------------|
| **DATA_STALE** | HIGH | Datos no actualizados |
| **DATA_QUALITY_LOW** | MEDIUM | Baja calidad de datos |
| **REPORT_GENERATION_FAILED** | MEDIUM | Error al generar reporte |
| **SLOW_QUERIES** | MEDIUM | Queries lentas |
| **LOW_CACHE_HIT_RATE** | LOW | Baja tasa de acierto de caché |
| **HIGH_CPU_USAGE** | HIGH | Uso elevado de CPU |
| **HIGH_MEMORY_USAGE** | HIGH | Uso elevado de memoria |
| **PIPELINE_FAILURE** | HIGH | Fallo en pipeline |
| **DATA_ANOMALY** | MEDIUM | Anomalía detectada en datos |

---

## 📊 DASHBOARDS

### **Analytics Health Dashboard**

**Pantallas ZUL:**
1. **`analytics-overview.zul`** - Vista general de analytics
2. **`analytics-metrics.zul`** - Métricas del sistema
3. **`analytics-reports.zul`** - Estado de reportes
4. **`analytics-trends.zul`** - Tendencias de uso

---

## 📈 REPORTES AUTOMÁTICOS

### **Reportes Programados**

```java
@Service
public class ScheduledReportService {
    
    @Scheduled(cron = "0 0 8 * * MON") // Lunes 8AM
    public void generateWeeklyReport() {
        // Reporte semanal de métricas
        WeeklyMetricsReport report = analyticsService.generateWeeklyReport(
            LocalDate.now().minusWeeks(1),
            LocalDate.now()
        );
        
        // Enviar por email
        emailService.sendReport(
            "analytics-team@company.com",
            "Weekly Analytics Report",
            report
        );
    }
    
    @Scheduled(cron = "0 0 0 1 * *") // Primer día del mes
    public void generateMonthlyReport() {
        // Reporte mensual completo
        MonthlyAnalyticsReport report = analyticsService.generateMonthlyReport(
            LocalDate.now().minusMonths(1).withDayOfMonth(1),
            LocalDate.now().withDayOfMonth(1)
        );
        
        // Enviar a stakeholders
        emailService.sendReport(
            "executives@company.com",
            "Monthly Analytics Report",
            report
        );
    }
}
```

---

## 🔍 AUDITORÍA

### **Audit Trail de Analytics**

```java
@Aspect
@Component
public class AnalyticsAuditAspect {
    
    @Autowired
    private AuditService auditService;
    
    @Around("@annotation(Audited)")
    public Object auditAnalyticsOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        String operation = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        
        // Registrar inicio
        AuditEntry startEntry = AuditEntry.builder()
            .operation(operation)
            .arguments(Arrays.toString(args))
            .timestamp(LocalDateTime.now())
            .status("STARTED")
            .build();
        
        auditService.log(startEntry);
        
        try {
            // Ejecutar operación
            Object result = joinPoint.proceed();
            
            // Registrar éxito
            auditService.log(startEntry.toBuilder()
                .status("COMPLETED")
                .result(result.toString())
                .build());
            
            return result;
            
        } catch (Exception e) {
            // Registrar error
            auditService.log(startEntry.toBuilder()
                .status("FAILED")
                .error(e.getMessage())
                .build());
            
            throw e;
        }
    }
}
```

---

## 🎯 CONCLUSIÓN

El módulo **Analytics** incluye monitoreo completo con:

- ✅ **Calidad de Datos** - Completitud, precisión, frescura
- ✅ **Performance de Reportes** - Generación y uso
- ✅ **Performance de Queries** - Ejecución y caché
- ✅ **Salud del Sistema** - Recursos y pipeline
- ✅ **Alertas Automáticas** - 9 tipos de alertas
- ✅ **Dashboards** - 4 dashboards especializados
- ✅ **Reportes Programados** - Semanales y mensuales
- ✅ **Auditoría Completa** - Tracking de operaciones

**Monitoreo end-to-end para garantizar calidad y performance del sistema de analytics.**

