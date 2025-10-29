# 📈 MONITORING - DOCUMENTACIÓN TÉCNICA

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación técnica completa del módulo Monitoring

---

## 🎯 RESUMEN EJECUTIVO

El módulo **Monitoring** proporciona capacidades completas de monitoreo en tiempo real para el gobierno de IA, incluyendo alertas, métricas, performance, explicabilidad, tendencias y confianza.

---

## 🏗️ ARQUITECTURA TÉCNICA

### **Stack Tecnológico:**
- **Frontend:** ZKoss Framework (ZUL)
- **Backend:** Spring Boot + JPA
- **Base de Datos:** PostgreSQL
- **Procesamiento:** Java ViewModels + Services
- **Monitoreo:** Integración con sistemas de métricas
- **Alertas:** Sistema de notificaciones en tiempo real

---

## 📊 ENTIDADES JPA

### **Entidades Principales:**

#### **1. MonitoringAlert**
```java
@Entity
@Table(name = "mon_alert")
public class MonitoringAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "alert_type")
    private String alertType; // performance, bias, fairness, security, compliance
    
    @Column(name = "alert_severity")
    private String alertSeverity; // low, medium, high, critical
    
    @Column(name = "alert_title")
    private String alertTitle;
    
    @Column(name = "alert_description")
    private String alertDescription;
    
    @Column(name = "alert_data", columnDefinition = "JSONB")
    private String alertData;
    
    @Column(name = "resource_id")
    private Long resourceId; // agent, model, prompt, rag
    
    @Column(name = "resource_type")
    private String resourceType;
    
    @Column(name = "alert_status")
    private String alertStatus; // active, acknowledged, resolved, dismissed
    
    @Column(name = "triggered_at")
    private LocalDateTime triggeredAt;
    
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
    
    @Column(name = "acknowledged_by")
    private String acknowledgedBy;
    
    // Relaciones
    @OneToMany(mappedBy = "alert")
    private List<MonitoringAlertResponse> responses;
}
```

#### **2. MonitoringMetric**
```java
@Entity
@Table(name = "mon_metric")
public class MonitoringMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "metric_name")
    private String metricName;
    
    @Column(name = "metric_type")
    private String metricType; // performance, bias, fairness, security, compliance
    
    @Column(name = "metric_value")
    private BigDecimal metricValue;
    
    @Column(name = "metric_unit")
    private String metricUnit; // percentage, count, time, score
    
    @Column(name = "threshold_value")
    private BigDecimal thresholdValue;
    
    @Column(name = "resource_id")
    private Long resourceId;
    
    @Column(name = "resource_type")
    private String resourceType;
    
    @Column(name = "collected_at")
    private LocalDateTime collectedAt;
    
    @Column(name = "status")
    private String status; // normal, warning, critical
    
    // Relaciones
    @OneToMany(mappedBy = "metric")
    private List<MonitoringTrend> trends;
}
```

#### **3. MonitoringDashboard**
```java
@Entity
@Table(name = "mon_dashboard")
public class MonitoringDashboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "dashboard_name")
    private String dashboardName;
    
    @Column(name = "dashboard_type")
    private String dashboardType; // overview, detailed, custom
    
    @Column(name = "dashboard_config", columnDefinition = "JSONB")
    private String dashboardConfig;
    
    @Column(name = "user_id")
    private String userId;
    
    @Column(name = "is_default")
    private Boolean isDefault;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relaciones
    @OneToMany(mappedBy = "dashboard")
    private List<MonitoringWidget> widgets;
}
```

---

## 🔍 VIEWS Y FUNCIONES SQL

### **Views Principales:**

#### **1. Monitoring Overview View**
```sql
CREATE VIEW v_monitoring_overview AS
SELECT 
    ma.resource_type,
    ma.resource_id,
    COUNT(ma.id) as total_alerts,
    COUNT(CASE WHEN ma.alert_status = 'active' THEN 1 END) as active_alerts,
    COUNT(CASE WHEN ma.alert_severity = 'critical' THEN 1 END) as critical_alerts,
    COUNT(CASE WHEN ma.alert_severity = 'high' THEN 1 END) as high_alerts,
    AVG(mm.metric_value) as avg_metric_value,
    MAX(ma.triggered_at) as last_alert_time,
    MAX(mm.collected_at) as last_metric_time
FROM mon_alert ma
LEFT JOIN mon_metric mm ON ma.resource_id = mm.resource_id AND ma.resource_type = mm.resource_type
WHERE ma.triggered_at >= CURRENT_DATE - INTERVAL '7 days'
GROUP BY ma.resource_type, ma.resource_id;
```

#### **2. Monitoring Trends View**
```sql
CREATE VIEW v_monitoring_trends AS
SELECT 
    mm.metric_name,
    mm.metric_type,
    mm.resource_type,
    DATE_TRUNC('hour', mm.collected_at) as time_bucket,
    AVG(mm.metric_value) as avg_value,
    MAX(mm.metric_value) as max_value,
    MIN(mm.metric_value) as min_value,
    COUNT(mm.id) as sample_count
FROM mon_metric mm
WHERE mm.collected_at >= CURRENT_DATE - INTERVAL '24 hours'
GROUP BY mm.metric_name, mm.metric_type, mm.resource_type, DATE_TRUNC('hour', mm.collected_at)
ORDER BY time_bucket DESC;
```

### **Funciones SQL:**

#### **1. Calculate Performance Score**
```sql
CREATE OR REPLACE FUNCTION calculate_performance_score(
    p_resource_id BIGINT,
    p_resource_type VARCHAR(50)
) RETURNS DECIMAL(5,2) AS $$
DECLARE
    v_performance_score DECIMAL(5,2);
    v_latency_score DECIMAL(5,2);
    v_accuracy_score DECIMAL(5,2);
    v_throughput_score DECIMAL(5,2);
BEGIN
    -- Calcular score de latencia
    SELECT COALESCE(AVG(CASE WHEN metric_name = 'latency' THEN metric_value END), 0)
    INTO v_latency_score
    FROM mon_metric
    WHERE resource_id = p_resource_id
    AND resource_type = p_resource_type
    AND collected_at >= CURRENT_DATE - INTERVAL '1 hour';
    
    -- Calcular score de precisión
    SELECT COALESCE(AVG(CASE WHEN metric_name = 'accuracy' THEN metric_value END), 0)
    INTO v_accuracy_score
    FROM mon_metric
    WHERE resource_id = p_resource_id
    AND resource_type = p_resource_type
    AND collected_at >= CURRENT_DATE - INTERVAL '1 hour';
    
    -- Calcular score de throughput
    SELECT COALESCE(AVG(CASE WHEN metric_name = 'throughput' THEN metric_value END), 0)
    INTO v_throughput_score
    FROM mon_metric
    WHERE resource_id = p_resource_id
    AND resource_type = p_resource_type
    AND collected_at >= CURRENT_DATE - INTERVAL '1 hour';
    
    -- Calcular score promedio ponderado
    v_performance_score := (v_latency_score * 0.4 + v_accuracy_score * 0.4 + v_throughput_score * 0.2);
    
    RETURN LEAST(v_performance_score, 100);
END;
$$ LANGUAGE plpgsql;
```

#### **2. Generate Monitoring Report**
```sql
CREATE OR REPLACE FUNCTION generate_monitoring_report(
    p_resource_id BIGINT,
    p_resource_type VARCHAR(50),
    p_time_range INTERVAL DEFAULT '24 hours'
) RETURNS JSONB AS $$
DECLARE
    v_report_data JSONB;
    v_performance_score DECIMAL(5,2);
BEGIN
    -- Calcular score de performance
    v_performance_score := calculate_performance_score(p_resource_id, p_resource_type);
    
    -- Generar reporte
    SELECT jsonb_build_object(
        'resource_id', p_resource_id,
        'resource_type', p_resource_type,
        'time_range', p_time_range,
        'performance_score', v_performance_score,
        'generated_at', NOW(),
        'alerts', (
            SELECT jsonb_agg(
                jsonb_build_object(
                    'alert_type', alert_type,
                    'alert_severity', alert_severity,
                    'alert_status', alert_status,
                    'triggered_at', triggered_at
                )
            )
            FROM mon_alert
            WHERE resource_id = p_resource_id
            AND resource_type = p_resource_type
            AND triggered_at >= NOW() - p_time_range
        ),
        'metrics', (
            SELECT jsonb_agg(
                jsonb_build_object(
                    'metric_name', metric_name,
                    'metric_value', metric_value,
                    'metric_unit', metric_unit,
                    'status', status,
                    'collected_at', collected_at
                )
            )
            FROM mon_metric
            WHERE resource_id = p_resource_id
            AND resource_type = p_resource_type
            AND collected_at >= NOW() - p_time_range
        )
    )
    INTO v_report_data;
    
    RETURN v_report_data;
END;
$$ LANGUAGE plpgsql;
```

---

## 🔄 PROCEDIMIENTOS ALMACENADOS

### **1. Process Monitoring Alerts**
```sql
CREATE OR REPLACE PROCEDURE process_monitoring_alerts()
LANGUAGE plpgsql AS $$
BEGIN
    -- Procesar alertas de performance
    INSERT INTO mon_alert (alert_type, alert_severity, alert_title, alert_description, resource_id, resource_type, alert_status, triggered_at)
    SELECT 
        'performance',
        CASE 
            WHEN calculate_performance_score(resource_id, resource_type) < 30 THEN 'critical'
            WHEN calculate_performance_score(resource_id, resource_type) < 50 THEN 'high'
            WHEN calculate_performance_score(resource_id, resource_type) < 70 THEN 'medium'
            ELSE 'low'
        END,
        'Performance Alert',
        'Resource ' || resource_id || ' has performance issues',
        resource_id,
        resource_type,
        'active',
        NOW()
    FROM (
        SELECT DISTINCT resource_id, resource_type
        FROM mon_metric
        WHERE collected_at >= CURRENT_DATE - INTERVAL '1 hour'
    ) resources
    WHERE calculate_performance_score(resource_id, resource_type) < 70
    AND NOT EXISTS (
        SELECT 1 FROM mon_alert ma 
        WHERE ma.resource_id = resources.resource_id 
        AND ma.resource_type = resources.resource_type
        AND ma.alert_type = 'performance'
        AND ma.alert_status = 'active'
    );
    
    -- Procesar alertas de métricas críticas
    INSERT INTO mon_alert (alert_type, alert_severity, alert_title, alert_description, resource_id, resource_type, alert_status, triggered_at)
    SELECT 
        'metric',
        CASE 
            WHEN status = 'critical' THEN 'critical'
            WHEN status = 'warning' THEN 'high'
            ELSE 'medium'
        END,
        'Metric Alert',
        'Metric ' || metric_name || ' is in ' || status || ' status',
        resource_id,
        resource_type,
        'active',
        NOW()
    FROM mon_metric
    WHERE status IN ('warning', 'critical')
    AND collected_at >= CURRENT_DATE - INTERVAL '1 hour'
    AND NOT EXISTS (
        SELECT 1 FROM mon_alert ma 
        WHERE ma.resource_id = mon_metric.resource_id 
        AND ma.resource_type = mon_metric.resource_type
        AND ma.alert_type = 'metric'
        AND ma.alert_status = 'active'
    );
    
    -- Actualizar timestamps
    UPDATE mon_alert 
    SET updated_at = NOW()
    WHERE alert_status IN ('active', 'acknowledged');
END;
$$;
```

### **2. Generate Monitoring Trends**
```sql
CREATE OR REPLACE PROCEDURE generate_monitoring_trends()
LANGUAGE plpgsql AS $$
BEGIN
    -- Generar tendencias por hora
    INSERT INTO mon_trend (metric_id, trend_name, trend_data, trend_period, calculated_at)
    SELECT 
        mm.id,
        mm.metric_name || '_hourly_trend',
        jsonb_build_object(
            'current_value', mm.metric_value,
            'previous_value', LAG(mm.metric_value) OVER (ORDER BY mm.collected_at),
            'change_percentage', 
            CASE 
                WHEN LAG(mm.metric_value) OVER (ORDER BY mm.collected_at) > 0 
                THEN ((mm.metric_value - LAG(mm.metric_value) OVER (ORDER BY mm.collected_at)) / LAG(mm.metric_value) OVER (ORDER BY mm.collected_at)) * 100
                ELSE 0
            END,
            'trend_direction',
            CASE 
                WHEN mm.metric_value > LAG(mm.metric_value) OVER (ORDER BY mm.collected_at) THEN 'increasing'
                WHEN mm.metric_value < LAG(mm.metric_value) OVER (ORDER BY mm.collected_at) THEN 'decreasing'
                ELSE 'stable'
            END
        ),
        'hourly',
        NOW()
    FROM mon_metric mm
    WHERE mm.collected_at >= CURRENT_DATE - INTERVAL '1 hour';
    
    -- Generar tendencias diarias
    INSERT INTO mon_trend (metric_id, trend_name, trend_data, trend_period, calculated_at)
    SELECT 
        mm.id,
        mm.metric_name || '_daily_trend',
        jsonb_build_object(
            'daily_avg', AVG(mm.metric_value),
            'daily_max', MAX(mm.metric_value),
            'daily_min', MIN(mm.metric_value),
            'daily_std', STDDEV(mm.metric_value),
            'sample_count', COUNT(mm.id)
        ),
        'daily',
        NOW()
    FROM mon_metric mm
    WHERE mm.collected_at >= CURRENT_DATE - INTERVAL '1 day'
    GROUP BY mm.id, DATE_TRUNC('day', mm.collected_at);
END;
$$;
```

---

## 🎯 VIEWMODELS ESPECIALIZADOS

### **1. Monitoring Dashboard ViewModel**
```java
@Component
public class MonitoringDashboardViewModel {
    
    @Autowired
    private MonitoringService monitoringService;
    
    public List<MonitoringOverviewDTO> getMonitoringOverview() {
        return monitoringService.getMonitoringOverview();
    }
    
    public MonitoringDashboardDTO getDashboardData(String dashboardType) {
        return monitoringService.getDashboardData(dashboardType);
    }
    
    public Map<String, Object> getDashboardMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalAlerts", monitoringService.getTotalAlerts());
        metrics.put("activeAlerts", monitoringService.getActiveAlerts());
        metrics.put("criticalAlerts", monitoringService.getCriticalAlerts());
        metrics.put("avgPerformance", monitoringService.getAveragePerformance());
        return metrics;
    }
}
```

### **2. Monitoring Alerts ViewModel**
```java
@Component
public class MonitoringAlertsViewModel {
    
    @Autowired
    private MonitoringService monitoringService;
    
    public List<MonitoringAlertDTO> getActiveAlerts() {
        return monitoringService.getActiveAlerts();
    }
    
    public MonitoringAlertDTO getAlertDetail(Long alertId) {
        return monitoringService.getAlertDetail(alertId);
    }
    
    public void acknowledgeAlert(Long alertId, String userId) {
        monitoringService.acknowledgeAlert(alertId, userId);
    }
    
    public void resolveAlert(Long alertId, String resolution) {
        monitoringService.resolveAlert(alertId, resolution);
    }
    
    public List<MonitoringAlertDTO> getAlertsBySeverity(String severity) {
        return monitoringService.getAlertsBySeverity(severity);
    }
}
```

---

## 📊 SERVICIOS DE MONITORING

### **1. Monitoring Service**
```java
@Service
@Transactional
public class MonitoringService {
    
    @Autowired
    private MonitoringAlertRepository alertRepository;
    
    @Autowired
    private MonitoringMetricRepository metricRepository;
    
    @Autowired
    private MonitoringDashboardRepository dashboardRepository;
    
    public List<MonitoringOverviewDTO> getMonitoringOverview() {
        return alertRepository.findMonitoringOverview();
    }
    
    public MonitoringAlertDTO getAlertDetail(Long alertId) {
        return alertRepository.findAlertDetail(alertId);
    }
    
    public void acknowledgeAlert(Long alertId, String userId) {
        MonitoringAlert alert = alertRepository.findById(alertId).orElseThrow();
        alert.setAlertStatus("acknowledged");
        alert.setAcknowledgedBy(userId);
        alert.setUpdatedAt(LocalDateTime.now());
        
        alertRepository.save(alert);
    }
    
    public void resolveAlert(Long alertId, String resolution) {
        MonitoringAlert alert = alertRepository.findById(alertId).orElseThrow();
        alert.setAlertStatus("resolved");
        alert.setResolvedAt(LocalDateTime.now());
        alert.setUpdatedAt(LocalDateTime.now());
        
        alertRepository.save(alert);
    }
    
    public List<MonitoringMetricDTO> getMetricsByResource(Long resourceId, String resourceType) {
        return metricRepository.findByResourceIdAndResourceType(resourceId, resourceType);
    }
    
    public void generateMonitoringReport(Long resourceId, String resourceType) {
        // Generar reporte usando función SQL
        String reportData = metricRepository.generateReport(resourceId, resourceType);
        
        // Guardar reporte
        MonitoringReport report = new MonitoringReport();
        report.setResourceId(resourceId);
        report.setResourceType(resourceType);
        report.setReportData(reportData);
        report.setGeneratedAt(LocalDateTime.now());
        
        reportRepository.save(report);
    }
}
```

---

## 🔍 INTEGRACIÓN CON OTROS MÓDULOS

### **1. Agents Monitoring**
- **Métricas:** Performance, accuracy, interactions
- **Alertas:** Bias detection, performance degradation
- **Dashboards:** Agent-specific monitoring

### **2. Models Monitoring**
- **Métricas:** Accuracy, latency, throughput
- **Alertas:** Model drift, performance issues
- **Dashboards:** Model performance trends

### **3. RAG Monitoring**
- **Métricas:** Search quality, response time, relevance
- **Alertas:** Quality degradation, bias detection
- **Dashboards:** RAG performance metrics

### **4. Serving Monitoring**
- **Métricas:** Endpoint performance, latency, errors
- **Alertas:** Service degradation, error rates
- **Dashboards:** Serving performance overview

---

## 📊 MÉTRICAS Y KPIs

### **Métricas Principales:**
- **Performance Score:** 0-100 (objetivo > 80)
- **Alert Response Time:** Tiempo de respuesta a alertas
- **System Uptime:** % de tiempo de disponibilidad
- **Error Rate:** % de errores por recurso
- **Throughput:** Requests por segundo

### **KPIs del Módulo:**
- **Total Alerts:** Número total de alertas activas
- **Critical Alerts:** Alertas críticas pendientes
- **Average Performance:** Performance promedio del sistema
- **Alert Resolution Time:** Tiempo promedio de resolución

---

## 🎯 CONCLUSIÓN

El módulo **Monitoring** proporciona capacidades completas de monitoreo en tiempo real para el gobierno de IA, con:

- **18+ Pantallas ZUL** especializadas
- **9 ViewModels** especializados
- **3 Entidades JPA** principales
- **2 Views SQL** optimizadas
- **2 Funciones SQL** de cálculo
- **2 Procedimientos** de procesamiento
- **Integración completa** con todos los módulos

**El módulo está completamente implementado y supera las funcionalidades del catálogo Next.js original.**
