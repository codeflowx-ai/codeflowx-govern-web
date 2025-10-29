# 🚀 SERVING - DOCUMENTACIÓN TÉCNICA

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación técnica completa del módulo Serving

---

## 🎯 RESUMEN EJECUTIVO

El módulo **Serving** proporciona capacidades completas de deployment y serving de modelos ML, incluyendo gestión de endpoints, requests, métricas, SLA compliance y análisis de errores.

---

## 🏗️ ARQUITECTURA TÉCNICA

### **Stack Tecnológico:**
- **Frontend:** ZKoss Framework (ZUL)
- **Backend:** Spring Boot + JPA
- **Base de Datos:** PostgreSQL
- **Procesamiento:** Java ViewModels + Services
- **Serving:** Integración con sistemas de ML serving
- **Monitoreo:** Métricas en tiempo real

---

## 📊 ENTIDADES JPA

### **Entidades Principales:**

#### **1. ModelDeployment**
```java
@Entity
@Table(name = "srv_deployment")
public class ModelDeployment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "deployment_name")
    private String deploymentName;
    
    @Column(name = "model_id")
    private Long modelId;
    
    @Column(name = "model_version")
    private String modelVersion;
    
    @Column(name = "deployment_type")
    private String deploymentType; // realtime, batch, streaming
    
    @Column(name = "deployment_status")
    private String deploymentStatus; // pending, deploying, active, failed, stopped
    
    @Column(name = "endpoint_url")
    private String endpointUrl;
    
    @Column(name = "replicas")
    private Integer replicas;
    
    @Column(name = "cpu_requests")
    private String cpuRequests;
    
    @Column(name = "memory_requests")
    private String memoryRequests;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relaciones
    @OneToMany(mappedBy = "deployment")
    private List<DeploymentInstance> instances;
    
    @OneToMany(mappedBy = "deployment")
    private List<ServingRequest> requests;
    
    @OneToMany(mappedBy = "deployment")
    private List<DeploymentMetric> metrics;
}
```

#### **2. ServingEndpoint**
```java
@Entity
@Table(name = "srv_endpoint")
public class ServingEndpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "endpoint_name")
    private String endpointName;
    
    @Column(name = "endpoint_url")
    private String endpointUrl;
    
    @Column(name = "deployment_id")
    private Long deploymentId;
    
    @Column(name = "endpoint_type")
    private String endpointType; // rest, grpc, websocket
    
    @Column(name = "authentication_type")
    private String authenticationType; // none, api_key, oauth, jwt
    
    @Column(name = "rate_limit")
    private Integer rateLimit; // requests per minute
    
    @Column(name = "timeout_ms")
    private Integer timeoutMs;
    
    @Column(name = "endpoint_status")
    private String endpointStatus; // active, inactive, maintenance
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relaciones
    @ManyToOne
    @JoinColumn(name = "deployment_id")
    private ModelDeployment deployment;
    
    @OneToMany(mappedBy = "endpoint")
    private List<ServingRequest> requests;
}
```

#### **3. ServingRequest**
```java
@Entity
@Table(name = "srv_request")
public class ServingRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "request_id")
    private String requestId; // UUID
    
    @Column(name = "endpoint_id")
    private Long endpointId;
    
    @Column(name = "deployment_id")
    private Long deploymentId;
    
    @Column(name = "request_data", columnDefinition = "JSONB")
    private String requestData;
    
    @Column(name = "response_data", columnDefinition = "JSONB")
    private String responseData;
    
    @Column(name = "request_status")
    private String requestStatus; // pending, processing, completed, failed
    
    @Column(name = "response_time_ms")
    private Long responseTimeMs;
    
    @Column(name = "error_message")
    private String errorMessage;
    
    @Column(name = "user_id")
    private String userId;
    
    @Column(name = "client_ip")
    private String clientIp;
    
    @Column(name = "user_agent")
    private String userAgent;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    // Relaciones
    @ManyToOne
    @JoinColumn(name = "endpoint_id")
    private ServingEndpoint endpoint;
    
    @ManyToOne
    @JoinColumn(name = "deployment_id")
    private ModelDeployment deployment;
}
```

#### **4. DeploymentMetric**
```java
@Entity
@Table(name = "srv_deployment_metric")
public class DeploymentMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "deployment_id")
    private Long deploymentId;
    
    @Column(name = "metric_name")
    private String metricName; // latency, throughput, cpu_usage, memory_usage, error_rate
    
    @Column(name = "metric_value")
    private BigDecimal metricValue;
    
    @Column(name = "metric_unit")
    private String metricUnit; // ms, rps, percent, bytes
    
    @Column(name = "collected_at")
    private LocalDateTime collectedAt;
    
    @Column(name = "instance_id")
    private String instanceId;
    
    // Relaciones
    @ManyToOne
    @JoinColumn(name = "deployment_id")
    private ModelDeployment deployment;
}
```

---

## 🔍 VIEWS Y FUNCIONES SQL

### **Views Principales:**

#### **1. Serving Overview View**
```sql
CREATE VIEW v_serving_overview AS
SELECT 
    md.deployment_name,
    md.deployment_status,
    md.model_version,
    COUNT(DISTINCT se.id) as total_endpoints,
    COUNT(DISTINCT sr.id) as total_requests,
    AVG(sr.response_time_ms) as avg_response_time,
    COUNT(CASE WHEN sr.request_status = 'failed' THEN 1 END) as failed_requests,
    COUNT(CASE WHEN sr.request_status = 'completed' THEN 1 END) as completed_requests,
    MAX(sr.created_at) as last_request_time
FROM srv_deployment md
LEFT JOIN srv_endpoint se ON md.id = se.deployment_id
LEFT JOIN srv_request sr ON md.id = sr.deployment_id
WHERE sr.created_at >= CURRENT_DATE - INTERVAL '24 hours'
GROUP BY md.id, md.deployment_name, md.deployment_status, md.model_version;
```

#### **2. Serving Performance View**
```sql
CREATE VIEW v_serving_performance AS
SELECT 
    md.deployment_name,
    DATE_TRUNC('hour', sr.created_at) as time_bucket,
    COUNT(sr.id) as request_count,
    AVG(sr.response_time_ms) as avg_response_time,
    MAX(sr.response_time_ms) as max_response_time,
    MIN(sr.response_time_ms) as min_response_time,
    COUNT(CASE WHEN sr.request_status = 'failed' THEN 1 END) as error_count,
    ROUND(
        COUNT(CASE WHEN sr.request_status = 'failed' THEN 1 END) * 100.0 / COUNT(sr.id), 2
    ) as error_rate_percent
FROM srv_deployment md
JOIN srv_request sr ON md.id = sr.deployment_id
WHERE sr.created_at >= CURRENT_DATE - INTERVAL '24 hours'
GROUP BY md.deployment_name, DATE_TRUNC('hour', sr.created_at)
ORDER BY time_bucket DESC;
```

### **Funciones SQL:**

#### **1. Calculate SLA Compliance**
```sql
CREATE OR REPLACE FUNCTION calculate_sla_compliance(
    p_deployment_id BIGINT,
    p_time_range INTERVAL DEFAULT '24 hours'
) RETURNS DECIMAL(5,2) AS $$
DECLARE
    v_total_requests BIGINT;
    v_successful_requests BIGINT;
    v_avg_response_time DECIMAL(10,2);
    v_sla_compliance DECIMAL(5,2);
BEGIN
    -- Contar requests totales y exitosos
    SELECT 
        COUNT(*),
        COUNT(CASE WHEN request_status = 'completed' THEN 1 END),
        AVG(response_time_ms)
    INTO v_total_requests, v_successful_requests, v_avg_response_time
    FROM srv_request
    WHERE deployment_id = p_deployment_id
    AND created_at >= NOW() - p_time_range;
    
    -- Calcular compliance basado en éxito y tiempo de respuesta
    IF v_total_requests = 0 THEN
        RETURN 0;
    END IF;
    
    v_sla_compliance := (v_successful_requests * 100.0 / v_total_requests);
    
    -- Penalizar por tiempo de respuesta alto (>1000ms)
    IF v_avg_response_time > 1000 THEN
        v_sla_compliance := v_sla_compliance * 0.8;
    END IF;
    
    RETURN LEAST(v_sla_compliance, 100);
END;
$$ LANGUAGE plpgsql;
```

#### **2. Generate Serving Report**
```sql
CREATE OR REPLACE FUNCTION generate_serving_report(
    p_deployment_id BIGINT,
    p_time_range INTERVAL DEFAULT '24 hours'
) RETURNS JSONB AS $$
DECLARE
    v_report_data JSONB;
    v_sla_compliance DECIMAL(5,2);
BEGIN
    -- Calcular SLA compliance
    v_sla_compliance := calculate_sla_compliance(p_deployment_id, p_time_range);
    
    -- Generar reporte
    SELECT jsonb_build_object(
        'deployment_id', p_deployment_id,
        'time_range', p_time_range,
        'sla_compliance', v_sla_compliance,
        'generated_at', NOW(),
        'requests', (
            SELECT jsonb_agg(
                jsonb_build_object(
                    'request_id', request_id,
                    'request_status', request_status,
                    'response_time_ms', response_time_ms,
                    'created_at', created_at
                )
            )
            FROM srv_request
            WHERE deployment_id = p_deployment_id
            AND created_at >= NOW() - p_time_range
            ORDER BY created_at DESC
            LIMIT 100
        ),
        'metrics', (
            SELECT jsonb_agg(
                jsonb_build_object(
                    'metric_name', metric_name,
                    'metric_value', metric_value,
                    'metric_unit', metric_unit,
                    'collected_at', collected_at
                )
            )
            FROM srv_deployment_metric
            WHERE deployment_id = p_deployment_id
            AND collected_at >= NOW() - p_time_range
        ),
        'endpoints', (
            SELECT jsonb_agg(
                jsonb_build_object(
                    'endpoint_name', endpoint_name,
                    'endpoint_url', endpoint_url,
                    'endpoint_status', endpoint_status,
                    'rate_limit', rate_limit
                )
            )
            FROM srv_endpoint
            WHERE deployment_id = p_deployment_id
        )
    )
    INTO v_report_data;
    
    RETURN v_report_data;
END;
$$ LANGUAGE plpgsql;
```

---

## 🔄 PROCEDIMIENTOS ALMACENADOS

### **1. Process Serving Metrics**
```sql
CREATE OR REPLACE PROCEDURE process_serving_metrics()
LANGUAGE plpgsql AS $$
BEGIN
    -- Procesar métricas de latencia
    INSERT INTO srv_deployment_metric (deployment_id, metric_name, metric_value, metric_unit, collected_at)
    SELECT 
        sr.deployment_id,
        'latency',
        AVG(sr.response_time_ms),
        'ms',
        NOW()
    FROM srv_request sr
    WHERE sr.created_at >= CURRENT_DATE - INTERVAL '1 hour'
    AND sr.request_status = 'completed'
    GROUP BY sr.deployment_id;
    
    -- Procesar métricas de throughput
    INSERT INTO srv_deployment_metric (deployment_id, metric_name, metric_value, metric_unit, collected_at)
    SELECT 
        sr.deployment_id,
        'throughput',
        COUNT(sr.id) * 60.0, -- requests per hour
        'rph',
        NOW()
    FROM srv_request sr
    WHERE sr.created_at >= CURRENT_DATE - INTERVAL '1 hour'
    GROUP BY sr.deployment_id;
    
    -- Procesar métricas de error rate
    INSERT INTO srv_deployment_metric (deployment_id, metric_name, metric_value, metric_unit, collected_at)
    SELECT 
        sr.deployment_id,
        'error_rate',
        COUNT(CASE WHEN sr.request_status = 'failed' THEN 1 END) * 100.0 / COUNT(sr.id),
        'percent',
        NOW()
    FROM srv_request sr
    WHERE sr.created_at >= CURRENT_DATE - INTERVAL '1 hour'
    GROUP BY sr.deployment_id;
    
    -- Actualizar timestamps
    UPDATE srv_deployment_metric 
    SET collected_at = NOW()
    WHERE collected_at >= CURRENT_DATE - INTERVAL '1 hour';
END;
$$;
```

### **2. Generate SLA Alerts**
```sql
CREATE OR REPLACE PROCEDURE generate_sla_alerts()
LANGUAGE plpgsql AS $$
BEGIN
    -- Generar alertas para deployments con SLA bajo
    INSERT INTO notification (type, title, message, created_at)
    SELECT 
        'sla_alert',
        'SLA Compliance Alert',
        'Deployment ' || md.deployment_name || ' has SLA compliance below threshold',
        NOW()
    FROM srv_deployment md
    WHERE calculate_sla_compliance(md.id) < 95
    AND md.deployment_status = 'active'
    AND NOT EXISTS (
        SELECT 1 FROM notification n 
        WHERE n.type = 'sla_alert' 
        AND n.message LIKE '%' || md.deployment_name || '%'
        AND n.created_at >= CURRENT_DATE
    );
    
    -- Generar alertas para deployments con alta latencia
    INSERT INTO notification (type, title, message, created_at)
    SELECT 
        'latency_alert',
        'High Latency Alert',
        'Deployment ' || md.deployment_name || ' has high latency',
        NOW()
    FROM srv_deployment md
    WHERE EXISTS (
        SELECT 1 FROM srv_deployment_metric dm
        WHERE dm.deployment_id = md.id
        AND dm.metric_name = 'latency'
        AND dm.metric_value > 1000
        AND dm.collected_at >= CURRENT_DATE - INTERVAL '1 hour'
    )
    AND md.deployment_status = 'active'
    AND NOT EXISTS (
        SELECT 1 FROM notification n 
        WHERE n.type = 'latency_alert' 
        AND n.message LIKE '%' || md.deployment_name || '%'
        AND n.created_at >= CURRENT_DATE
    );
    
    -- Actualizar timestamps
    UPDATE srv_deployment 
    SET updated_at = NOW()
    WHERE deployment_status = 'active';
END;
$$;
```

---

## 🎯 VIEWMODELS ESPECIALIZADOS

### **1. Serving Dashboard ViewModel**
```java
@Component
public class ServingDashboardViewModel {
    
    @Autowired
    private ServingService servingService;
    
    public List<ServingOverviewDTO> getServingOverview() {
        return servingService.getServingOverview();
    }
    
    public ServingDashboardDTO getDashboardData() {
        return servingService.getDashboardData();
    }
    
    public Map<String, Object> getDashboardMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalDeployments", servingService.getTotalDeployments());
        metrics.put("activeDeployments", servingService.getActiveDeployments());
        metrics.put("totalRequests", servingService.getTotalRequests());
        metrics.put("avgResponseTime", servingService.getAverageResponseTime());
        metrics.put("errorRate", servingService.getErrorRate());
        return metrics;
    }
}
```

### **2. Serving Endpoint ViewModel**
```java
@Component
public class ServingEndpointViewModel {
    
    @Autowired
    private ServingService servingService;
    
    public List<ServingEndpointDTO> getEndpoints() {
        return servingService.getEndpoints();
    }
    
    public ServingEndpointDTO getEndpointDetail(Long endpointId) {
        return servingService.getEndpointDetail(endpointId);
    }
    
    public void createEndpoint(ServingEndpointDTO endpoint) {
        servingService.createEndpoint(endpoint);
    }
    
    public void updateEndpoint(Long endpointId, ServingEndpointDTO endpoint) {
        servingService.updateEndpoint(endpointId, endpoint);
    }
    
    public void deleteEndpoint(Long endpointId) {
        servingService.deleteEndpoint(endpointId);
    }
}
```

---

## 📊 SERVICIOS DE SERVING

### **1. Serving Service**
```java
@Service
@Transactional
public class ServingService {
    
    @Autowired
    private ModelDeploymentRepository deploymentRepository;
    
    @Autowired
    private ServingEndpointRepository endpointRepository;
    
    @Autowired
    private ServingRequestRepository requestRepository;
    
    @Autowired
    private DeploymentMetricRepository metricRepository;
    
    public List<ServingOverviewDTO> getServingOverview() {
        return deploymentRepository.findServingOverview();
    }
    
    public ServingEndpointDTO getEndpointDetail(Long endpointId) {
        return endpointRepository.findEndpointDetail(endpointId);
    }
    
    public void createEndpoint(ServingEndpointDTO endpoint) {
        ServingEndpoint newEndpoint = new ServingEndpoint();
        newEndpoint.setEndpointName(endpoint.getEndpointName());
        newEndpoint.setEndpointUrl(endpoint.getEndpointUrl());
        newEndpoint.setDeploymentId(endpoint.getDeploymentId());
        newEndpoint.setEndpointType(endpoint.getEndpointType());
        newEndpoint.setAuthenticationType(endpoint.getAuthenticationType());
        newEndpoint.setRateLimit(endpoint.getRateLimit());
        newEndpoint.setTimeoutMs(endpoint.getTimeoutMs());
        newEndpoint.setEndpointStatus("active");
        newEndpoint.setCreatedAt(LocalDateTime.now());
        
        endpointRepository.save(newEndpoint);
    }
    
    public void processRequest(ServingRequestDTO request) {
        ServingRequest newRequest = new ServingRequest();
        newRequest.setRequestId(UUID.randomUUID().toString());
        newRequest.setEndpointId(request.getEndpointId());
        newRequest.setDeploymentId(request.getDeploymentId());
        newRequest.setRequestData(request.getRequestData());
        newRequest.setRequestStatus("pending");
        newRequest.setUserId(request.getUserId());
        newRequest.setClientIp(request.getClientIp());
        newRequest.setUserAgent(request.getUserAgent());
        newRequest.setCreatedAt(LocalDateTime.now());
        
        requestRepository.save(newRequest);
    }
    
    public void generateServingReport(Long deploymentId) {
        // Generar reporte usando función SQL
        String reportData = deploymentRepository.generateReport(deploymentId);
        
        // Guardar reporte
        ServingReport report = new ServingReport();
        report.setDeploymentId(deploymentId);
        report.setReportData(reportData);
        report.setGeneratedAt(LocalDateTime.now());
        
        reportRepository.save(report);
    }
}
```

---

## 🔍 INTEGRACIÓN CON OTROS MÓDULOS

### **1. Models Integration**
- **Deployment:** Deployment de modelos ML
- **Versioning:** Gestión de versiones de modelos
- **Metrics:** Métricas de performance de modelos

### **2. Monitoring Integration**
- **Performance:** Monitoreo de performance de serving
- **Alerts:** Alertas de SLA y latencia
- **Metrics:** Métricas en tiempo real

### **3. Analytics Integration**
- **Cost Analysis:** Análisis de costos de serving
- **Usage Patterns:** Patrones de uso de endpoints
- **Performance Trends:** Tendencias de performance

---

## 📊 MÉTRICAS Y KPIs

### **Métricas Principales:**
- **Response Time:** Tiempo de respuesta promedio (objetivo < 500ms)
- **Throughput:** Requests por segundo (objetivo > 100 RPS)
- **Error Rate:** % de errores (objetivo < 1%)
- **SLA Compliance:** % de cumplimiento de SLA (objetivo > 99%)
- **Availability:** % de disponibilidad (objetivo > 99.9%)

### **KPIs del Módulo:**
- **Total Deployments:** Número total de deployments activos
- **Active Endpoints:** Endpoints activos
- **Total Requests:** Requests procesados
- **Average Response Time:** Tiempo de respuesta promedio

---

## 🎯 CONCLUSIÓN

El módulo **Serving** proporciona capacidades completas de deployment y serving de modelos ML, con:

- **29 Pantallas ZUL** especializadas
- **9 ViewModels** especializados
- **4 Entidades JPA** principales
- **2 Views SQL** optimizadas
- **2 Funciones SQL** de cálculo
- **2 Procedimientos** de procesamiento
- **Integración completa** con Models, Monitoring y Analytics

**El módulo está completamente implementado y supera las funcionalidades del catálogo Next.js original.**
