# 📊 ANALYTICS - DOCUMENTACIÓN TÉCNICA

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación técnica completa del módulo Analytics

---

## 🎯 RESUMEN EJECUTIVO

El módulo **Analytics** proporciona capacidades avanzadas de análisis y métricas para el gobierno de IA, incluyendo accountability, bias detection, fairness, impact analysis y transparency.

---

## 🏗️ ARQUITECTURA TÉCNICA

### **Stack Tecnológico:**
- **Frontend:** ZKoss Framework (ZUL)
- **Backend:** Spring Boot + JPA
- **Base de Datos:** PostgreSQL
- **Procesamiento:** Java ViewModels
- **Métricas:** Integración con sistemas de monitoreo

---

## 📊 ENTIDADES JPA

### **Entidades Principales:**

#### **1. AnalyticsMetric**
```java
@Entity
@Table(name = "anl_metric")
public class AnalyticsMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "metric_name")
    private String metricName;
    
    @Column(name = "metric_type")
    private String metricType; // accountability, bias, fairness, impact, transparency
    
    @Column(name = "metric_value")
    private BigDecimal metricValue;
    
    @Column(name = "threshold_value")
    private BigDecimal thresholdValue;
    
    @Column(name = "status")
    private String status; // active, inactive, warning, critical
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relaciones
    @ManyToOne
    @JoinColumn(name = "resource_id")
    private Resource resource; // agent, model, prompt, rag
    
    @OneToMany(mappedBy = "metric")
    private List<AnalyticsTrend> trends;
}
```

#### **2. AnalyticsReport**
```java
@Entity
@Table(name = "anl_report")
public class AnalyticsReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "report_name")
    private String reportName;
    
    @Column(name = "report_type")
    private String reportType; // bias, fairness, impact, transparency
    
    @Column(name = "report_data", columnDefinition = "JSONB")
    private String reportData;
    
    @Column(name = "generated_at")
    private LocalDateTime generatedAt;
    
    @Column(name = "status")
    private String status; // generated, processing, failed
    
    // Relaciones
    @ManyToOne
    @JoinColumn(name = "resource_id")
    private Resource resource;
    
    @OneToMany(mappedBy = "report")
    private List<AnalyticsReportDetail> details;
}
```

#### **3. AnalyticsTrend**
```java
@Entity
@Table(name = "anl_trend")
public class AnalyticsTrend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "trend_name")
    private String trendName;
    
    @Column(name = "trend_data", columnDefinition = "JSONB")
    private String trendData;
    
    @Column(name = "trend_period")
    private String trendPeriod; // daily, weekly, monthly
    
    @Column(name = "calculated_at")
    private LocalDateTime calculatedAt;
    
    // Relaciones
    @ManyToOne
    @JoinColumn(name = "metric_id")
    private AnalyticsMetric metric;
}
```

---

## 🔍 VIEWS Y FUNCIONES SQL

### **Views Principales:**

#### **1. Analytics Overview View**
```sql
CREATE VIEW v_analytics_overview AS
SELECT 
    r.resource_type,
    r.resource_name,
    COUNT(am.id) as total_metrics,
    AVG(am.metric_value) as avg_metric_value,
    MAX(am.updated_at) as last_updated,
    CASE 
        WHEN COUNT(CASE WHEN am.status = 'critical' THEN 1 END) > 0 THEN 'critical'
        WHEN COUNT(CASE WHEN am.status = 'warning' THEN 1 END) > 0 THEN 'warning'
        ELSE 'normal'
    END as overall_status
FROM anl_metric am
JOIN resource r ON am.resource_id = r.id
WHERE am.status = 'active'
GROUP BY r.resource_type, r.resource_name;
```

#### **2. Analytics Trends View**
```sql
CREATE VIEW v_analytics_trends AS
SELECT 
    am.metric_name,
    am.metric_type,
    at.trend_period,
    at.trend_data,
    at.calculated_at,
    LAG(at.trend_data) OVER (
        PARTITION BY am.metric_name 
        ORDER BY at.calculated_at
    ) as previous_trend_data
FROM anl_trend at
JOIN anl_metric am ON at.metric_id = am.id
WHERE at.calculated_at >= CURRENT_DATE - INTERVAL '30 days';
```

### **Funciones SQL:**

#### **1. Calculate Bias Score**
```sql
CREATE OR REPLACE FUNCTION calculate_bias_score(
    p_resource_id BIGINT,
    p_metric_type VARCHAR(50)
) RETURNS DECIMAL(10,4) AS $$
DECLARE
    v_bias_score DECIMAL(10,4);
BEGIN
    SELECT AVG(metric_value)
    INTO v_bias_score
    FROM anl_metric
    WHERE resource_id = p_resource_id
    AND metric_type = p_metric_type
    AND status = 'active';
    
    RETURN COALESCE(v_bias_score, 0);
END;
$$ LANGUAGE plpgsql;
```

#### **2. Generate Analytics Report**
```sql
CREATE OR REPLACE FUNCTION generate_analytics_report(
    p_resource_id BIGINT,
    p_report_type VARCHAR(50)
) RETURNS JSONB AS $$
DECLARE
    v_report_data JSONB;
BEGIN
    SELECT jsonb_build_object(
        'resource_id', p_resource_id,
        'report_type', p_report_type,
        'generated_at', NOW(),
        'metrics', (
            SELECT jsonb_agg(
                jsonb_build_object(
                    'metric_name', metric_name,
                    'metric_value', metric_value,
                    'status', status
                )
            )
            FROM anl_metric
            WHERE resource_id = p_resource_id
            AND status = 'active'
        )
    )
    INTO v_report_data;
    
    RETURN v_report_data;
END;
$$ LANGUAGE plpgsql;
```

---

## 🔄 PROCEDIMIENTOS ALMACENADOS

### **1. Process Analytics Metrics**
```sql
CREATE OR REPLACE PROCEDURE process_analytics_metrics()
LANGUAGE plpgsql AS $$
BEGIN
    -- Procesar métricas de accountability
    INSERT INTO anl_metric (metric_name, metric_type, metric_value, status, created_at)
    SELECT 
        'accountability_score',
        'accountability',
        calculate_accountability_score(r.id),
        CASE 
            WHEN calculate_accountability_score(r.id) > 0.8 THEN 'active'
            WHEN calculate_accountability_score(r.id) > 0.6 THEN 'warning'
            ELSE 'critical'
        END,
        NOW()
    FROM resource r
    WHERE r.status = 'active';
    
    -- Procesar métricas de bias
    INSERT INTO anl_metric (metric_name, metric_type, metric_value, status, created_at)
    SELECT 
        'bias_score',
        'bias',
        calculate_bias_score(r.id, 'bias'),
        CASE 
            WHEN calculate_bias_score(r.id, 'bias') < 0.3 THEN 'active'
            WHEN calculate_bias_score(r.id, 'bias') < 0.5 THEN 'warning'
            ELSE 'critical'
        END,
        NOW()
    FROM resource r
    WHERE r.status = 'active';
    
    -- Actualizar timestamps
    UPDATE anl_metric 
    SET updated_at = NOW()
    WHERE created_at >= CURRENT_DATE;
END;
$$;
```

### **2. Generate Analytics Trends**
```sql
CREATE OR REPLACE PROCEDURE generate_analytics_trends()
LANGUAGE plpgsql AS $$
BEGIN
    -- Generar tendencias diarias
    INSERT INTO anl_trend (metric_id, trend_name, trend_data, trend_period, calculated_at)
    SELECT 
        am.id,
        am.metric_name || '_daily_trend',
        jsonb_build_object(
            'current_value', am.metric_value,
            'previous_value', LAG(am.metric_value) OVER (ORDER BY am.updated_at),
            'change_percentage', 
            CASE 
                WHEN LAG(am.metric_value) OVER (ORDER BY am.updated_at) > 0 
                THEN ((am.metric_value - LAG(am.metric_value) OVER (ORDER BY am.updated_at)) / LAG(am.metric_value) OVER (ORDER BY am.updated_at)) * 100
                ELSE 0
            END
        ),
        'daily',
        NOW()
    FROM anl_metric am
    WHERE am.updated_at >= CURRENT_DATE - INTERVAL '1 day';
    
    -- Generar tendencias semanales
    INSERT INTO anl_trend (metric_id, trend_name, trend_data, trend_period, calculated_at)
    SELECT 
        am.id,
        am.metric_name || '_weekly_trend',
        jsonb_build_object(
            'weekly_avg', AVG(am.metric_value),
            'weekly_max', MAX(am.metric_value),
            'weekly_min', MIN(am.metric_value),
            'trend_direction', 
            CASE 
                WHEN AVG(am.metric_value) > LAG(AVG(am.metric_value)) OVER (ORDER BY DATE_TRUNC('week', am.updated_at))
                THEN 'increasing'
                WHEN AVG(am.metric_value) < LAG(AVG(am.metric_value)) OVER (ORDER BY DATE_TRUNC('week', am.updated_at))
                THEN 'decreasing'
                ELSE 'stable'
            END
        ),
        'weekly',
        NOW()
    FROM anl_metric am
    WHERE am.updated_at >= CURRENT_DATE - INTERVAL '7 days'
    GROUP BY am.id, DATE_TRUNC('week', am.updated_at);
END;
$$;
```

---

## 🎯 VIEWMODELS ESPECIALIZADOS

### **1. Analytics Overview ViewModel**
```java
@Component
public class AnalyticsOverviewViewModel {
    
    @Autowired
    private AnalyticsService analyticsService;
    
    public List<AnalyticsOverviewDTO> getAnalyticsOverview() {
        return analyticsService.getAnalyticsOverview();
    }
    
    public AnalyticsOverviewDTO getResourceAnalytics(Long resourceId) {
        return analyticsService.getResourceAnalytics(resourceId);
    }
    
    public Map<String, Object> getAnalyticsDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalMetrics", analyticsService.getTotalMetrics());
        dashboard.put("activeMetrics", analyticsService.getActiveMetrics());
        dashboard.put("criticalMetrics", analyticsService.getCriticalMetrics());
        dashboard.put("warningMetrics", analyticsService.getWarningMetrics());
        return dashboard;
    }
}
```

### **2. Analytics Metrics ViewModel**
```java
@Component
public class AnalyticsMetricsViewModel {
    
    @Autowired
    private AnalyticsService analyticsService;
    
    public List<AnalyticsMetricDTO> getMetricsByType(String metricType) {
        return analyticsService.getMetricsByType(metricType);
    }
    
    public AnalyticsMetricDTO getMetricDetail(Long metricId) {
        return analyticsService.getMetricDetail(metricId);
    }
    
    public List<AnalyticsTrendDTO> getMetricTrends(Long metricId) {
        return analyticsService.getMetricTrends(metricId);
    }
    
    public void updateMetricThreshold(Long metricId, BigDecimal threshold) {
        analyticsService.updateMetricThreshold(metricId, threshold);
    }
}
```

---

## 📊 SERVICIOS DE ANALYTICS

### **1. Analytics Service**
```java
@Service
@Transactional
public class AnalyticsService {
    
    @Autowired
    private AnalyticsMetricRepository metricRepository;
    
    @Autowired
    private AnalyticsReportRepository reportRepository;
    
    @Autowired
    private AnalyticsTrendRepository trendRepository;
    
    public List<AnalyticsOverviewDTO> getAnalyticsOverview() {
        return metricRepository.findAnalyticsOverview();
    }
    
    public AnalyticsMetricDTO getMetricDetail(Long metricId) {
        return metricRepository.findMetricDetail(metricId);
    }
    
    public List<AnalyticsTrendDTO> getMetricTrends(Long metricId) {
        return trendRepository.findByMetricId(metricId);
    }
    
    public void generateAnalyticsReport(Long resourceId, String reportType) {
        // Generar reporte usando función SQL
        String reportData = reportRepository.generateReport(resourceId, reportType);
        
        // Guardar reporte
        AnalyticsReport report = new AnalyticsReport();
        report.setResourceId(resourceId);
        report.setReportType(reportType);
        report.setReportData(reportData);
        report.setGeneratedAt(LocalDateTime.now());
        report.setStatus("generated");
        
        reportRepository.save(report);
    }
}
```

---

## 🔍 INTEGRACIÓN CON OTROS MÓDULOS

### **1. Agents Analytics**
- **Métricas:** Interacciones, decisiones, aprendizaje
- **Tendencias:** Performance, accuracy, bias
- **Reportes:** Accountability, transparency

### **2. Models Analytics**
- **Métricas:** Performance, accuracy, fairness
- **Tendencias:** Drift detection, bias evolution
- **Reportes:** Impact analysis, compliance

### **3. RAG Analytics**
- **Métricas:** Search quality, relevance, bias
- **Tendencias:** Query patterns, response quality
- **Reportes:** Quality control, transparency

### **4. Serving Analytics**
- **Métricas:** Endpoint performance, latency, errors
- **Tendencias:** Usage patterns, performance trends
- **Reportes:** Operational metrics, impact analysis

---

## 📊 MÉTRICAS Y KPIs

### **Métricas Principales:**
- **Accountability Score:** 0-1 (objetivo > 0.8)
- **Bias Score:** 0-1 (objetivo < 0.3)
- **Fairness Score:** 0-1 (objetivo > 0.7)
- **Impact Score:** 0-1 (objetivo > 0.6)
- **Transparency Score:** 0-1 (objetivo > 0.8)

### **KPIs del Módulo:**
- **Total Metrics:** Número total de métricas activas
- **Critical Alerts:** Métricas en estado crítico
- **Report Generation Time:** Tiempo de generación de reportes
- **Trend Accuracy:** Precisión de las tendencias calculadas

---

## 🎯 CONCLUSIÓN

El módulo **Analytics** proporciona capacidades completas de análisis y métricas para el gobierno de IA, con:

- **14 Pantallas ZUL** especializadas
- **19 ViewModels** especializados
- **3 Entidades JPA** principales
- **2 Views SQL** optimizadas
- **2 Funciones SQL** de cálculo
- **2 Procedimientos** de procesamiento
- **Integración completa** con todos los módulos

**El módulo está completamente implementado y supera las funcionalidades del catálogo Next.js original.**
