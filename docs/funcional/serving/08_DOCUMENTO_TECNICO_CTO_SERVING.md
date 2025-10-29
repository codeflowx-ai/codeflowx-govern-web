# 🚀 SERVING - DOCUMENTO TÉCNICO CTO

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Audiencia:** CTOs, Arquitectos de Software, Líderes Técnicos  
**Propósito:** Especificación técnica detallada del módulo Serving

---

## 🎯 RESUMEN EJECUTIVO TÉCNICO

**CodeflowX Govern Serving** es una plataforma de serving de modelos ML enterprise-grade construida sobre **arquitectura cloud-native**, con **auto-scaling inteligente**, **observabilidad completa** y **SLA garantizado del 99.9%**.

### **Stack Tecnológico:**
- **Orchestration:** Kubernetes 1.28+
- **Backend:** Java 17, Spring Boot 3.2
- **Inferencia:** Python 3.11, FastAPI, TorchServe, TensorFlow Serving
- **Monitoreo:** Prometheus, Grafana, ELK Stack
- **Base de Datos:** PostgreSQL 15 (principal), Redis (caché)

---

## 🏗️ ARQUITECTURA TÉCNICA

### **1. Arquitectura General**

```
┌─────────────────────────────────────────────────────────────┐
│                    Load Balancer (Nginx)                    │
│                     (SSL Termination)                       │
└─────────────────────┬───────────────────────────────────────┘
                      │
         ┌────────────┴──────────────┐
         ▼                           ▼
┌──────────────────┐        ┌──────────────────┐
│  API Gateway     │        │  WebSocket       │
│  (Spring Cloud)  │        │  Gateway         │
└────────┬─────────┘        └────────┬─────────┘
         │                           │
         ├───────────────────────────┤
         │                           │
    ┌────▼────────────────────────────▼────┐
    │      Serving Service Layer           │
    │  (Deployment, Prediction, Health)    │
    └────┬────────────────────────────┬────┘
         │                            │
    ┌────▼────┐                  ┌────▼────┐
    │ Model   │                  │ Metrics │
    │ Registry│                  │ Service │
    └────┬────┘                  └────┬────┘
         │                            │
    ┌────▼────────────────────────────▼────┐
    │         Kubernetes Cluster           │
    │  ┌──────┐  ┌──────┐  ┌──────┐       │
    │  │Model │  │Model │  │Model │       │
    │  │ Pod  │  │ Pod  │  │ Pod  │ ...   │
    │  └──────┘  └──────┘  └──────┘       │
    └──────────────────────────────────────┘
```

### **2. Componentes Principales**

#### **A. Deployment Service**
```java
@Service
@Transactional
public class DeploymentService {
    
    @Autowired
    private KubernetesClient k8sClient;
    
    @Autowired
    private ModelRegistry modelRegistry;
    
    @Autowired
    private MetricsCollector metricsCollector;
    
    /**
     * Crea un deployment de modelo en Kubernetes
     * 
     * @param request Configuración del deployment
     * @return Deployment creado
     */
    public Deployment createDeployment(DeploymentRequest request) {
        // 1. Validar modelo
        Model model = modelRegistry.getModel(request.getModelId(), request.getModelVersion());
        validateModel(model);
        
        // 2. Crear deployment en K8s
        io.fabric8.kubernetes.api.model.apps.Deployment k8sDeployment = 
            buildKubernetesDeployment(model, request);
        k8sClient.apps().deployments().create(k8sDeployment);
        
        // 3. Crear service en K8s
        Service k8sService = buildKubernetesService(model, request);
        k8sClient.services().create(k8sService);
        
        // 4. Configurar HPA (Horizontal Pod Autoscaler)
        if (request.isAutoScaling()) {
            HorizontalPodAutoscaler hpa = buildHPA(model, request);
            k8sClient.autoscaling().v2().horizontalPodAutoscalers().create(hpa);
        }
        
        // 5. Registrar deployment
        Deployment deployment = persistDeployment(model, request);
        
        // 6. Iniciar monitoreo
        metricsCollector.startMonitoring(deployment.getId());
        
        return deployment;
    }
}
```

#### **B. Prediction Service**
```java
@Service
public class PredictionService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private MetricsService metricsService;
    
    /**
     * Realiza predicción en tiempo real con caché
     */
    @Cacheable(value = "predictions", key = "#request.cacheKey()")
    public PredictionResponse predict(PredictionRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            // 1. Obtener endpoint del deployment
            String endpointUrl = getEndpointUrl(request.getDeploymentId());
            
            // 2. Realizar predicción
            PredictionResponse response = restTemplate.postForObject(
                endpointUrl + "/predict",
                request.getInputs(),
                PredictionResponse.class
            );
            
            // 3. Calcular latencia
            long latency = System.currentTimeMillis() - startTime;
            response.setLatencyMs(latency);
            
            // 4. Persistir predicción
            persistPrediction(request, response);
            
            // 5. Publicar métricas
            metricsService.recordPrediction(request.getDeploymentId(), latency, true);
            
            return response;
            
        } catch (Exception e) {
            long latency = System.currentTimeMillis() - startTime;
            metricsService.recordPrediction(request.getDeploymentId(), latency, false);
            throw new PredictionException("Prediction failed", e);
        }
    }
}
```

#### **C. Auto-Scaling Service**
```java
@Service
public class AutoScalingService {
    
    @Autowired
    private MetricsService metricsService;
    
    @Autowired
    private KubernetesClient k8sClient;
    
    /**
     * Lógica personalizada de auto-scaling
     */
    @Scheduled(fixedRate = 30000) // Cada 30 segundos
    public void evaluateScaling() {
        List<Deployment> deployments = deploymentRepository.findAllActive();
        
        for (Deployment deployment : deployments) {
            if (!deployment.isAutoScaling()) continue;
            
            // Obtener métricas recientes
            ScalingMetrics metrics = metricsService.getScalingMetrics(
                deployment.getId(), Duration.ofMinutes(5)
            );
            
            // Calcular réplicas necesarias
            int desiredReplicas = calculateDesiredReplicas(deployment, metrics);
            int currentReplicas = getCurrentReplicas(deployment);
            
            // Escalar si es necesario
            if (desiredReplicas != currentReplicas) {
                scaleDeployment(deployment, desiredReplicas);
            }
        }
    }
    
    private int calculateDesiredReplicas(Deployment deployment, ScalingMetrics metrics) {
        // CPU-based scaling
        double cpuTarget = deployment.getTargetCpuUtilization();
        double cpuCurrent = metrics.getAvgCpuUsage();
        int cpuBasedReplicas = (int) Math.ceil(
            deployment.getReplicas() * (cpuCurrent / cpuTarget)
        );
        
        // Request-based scaling
        double requestTarget = deployment.getTargetRequestsPerSecond();
        double requestCurrent = metrics.getAvgRequestsPerSecond();
        int requestBasedReplicas = (int) Math.ceil(
            deployment.getReplicas() * (requestCurrent / requestTarget)
        );
        
        // Tomar el máximo
        int desired = Math.max(cpuBasedReplicas, requestBasedReplicas);
        
        // Aplicar límites
        desired = Math.max(desired, deployment.getMinReplicas());
        desired = Math.min(desired, deployment.getMaxReplicas());
        
        return desired;
    }
}
```

---

## 🔧 TECNOLOGÍAS Y FRAMEWORKS

### **Backend Services**
```xml
<!-- Spring Boot Dependencies -->
<dependencies>
    <!-- Spring Boot Starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <version>3.2.0</version>
    </dependency>
    
    <!-- Kubernetes Client -->
    <dependency>
        <groupId>io.fabric8</groupId>
        <artifactId>kubernetes-client</artifactId>
        <version>6.9.2</version>
    </dependency>
    
    <!-- Redis -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    
    <!-- Metrics -->
    <dependency>
        <groupId>io.micrometer</groupId>
        <artifactId>micrometer-registry-prometheus</artifactId>
    </dependency>
</dependencies>
```

### **Model Serving Runtime**
```python
# requirements.txt para model serving
fastapi==0.104.1
uvicorn[standard]==0.24.0
torch==2.1.0
torchserve==0.9.0
tensorflow==2.15.0
tensorflow-serving-api==2.15.0
onnxruntime==1.16.3
transformers==4.35.2
numpy==1.26.2
pandas==2.1.3
scikit-learn==1.3.2
redis==5.0.1
prometheus-client==0.19.0
```

---

## 📊 MODELO DE DATOS

### **Esquema de Base de Datos**

```sql
-- Tabla principal de deployments
CREATE TABLE srv_deployments (
    srv_id BIGSERIAL PRIMARY KEY,
    srv_model_id BIGINT NOT NULL,
    srv_model_name VARCHAR(255) NOT NULL,
    srv_model_version VARCHAR(100) NOT NULL,
    srv_deployment_type VARCHAR(50) NOT NULL, -- REALTIME, BATCH, STREAMING
    srv_status VARCHAR(50) NOT NULL, -- DEPLOYING, ACTIVE, PAUSED, FAILED
    srv_replicas INTEGER NOT NULL DEFAULT 1,
    srv_cpu_limit VARCHAR(20),
    srv_memory_limit VARCHAR(20),
    srv_auto_scaling BOOLEAN DEFAULT false,
    srv_min_replicas INTEGER DEFAULT 1,
    srv_max_replicas INTEGER DEFAULT 10,
    srv_target_cpu_utilization DECIMAL(5,2),
    srv_endpoint_url VARCHAR(500),
    srv_deployment_date TIMESTAMP NOT NULL,
    srv_created_by BIGINT,
    srv_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    srv_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_srv_model FOREIGN KEY (srv_model_id) REFERENCES mdl_models(mdl_id)
);

-- Índices
CREATE INDEX idx_srv_deployments_model ON srv_deployments(srv_model_id);
CREATE INDEX idx_srv_deployments_status ON srv_deployments(srv_status);
CREATE INDEX idx_srv_deployments_date ON srv_deployments(srv_deployment_date);

-- Tabla de predicciones
CREATE TABLE srv_predictions (
    srv_id BIGSERIAL PRIMARY KEY,
    srv_deployment_id BIGINT NOT NULL,
    srv_prediction_id VARCHAR(100) UNIQUE NOT NULL,
    srv_input_hash VARCHAR(64),
    srv_output_hash VARCHAR(64),
    srv_latency_ms DECIMAL(10,2),
    srv_error TEXT,
    srv_timestamp TIMESTAMP NOT NULL,
    srv_metadata JSONB,
    CONSTRAINT fk_srv_prediction_deployment FOREIGN KEY (srv_deployment_id) 
        REFERENCES srv_deployments(srv_id)
);

-- Particionamiento por fecha para performance
CREATE TABLE srv_predictions_y2025m10 PARTITION OF srv_predictions
    FOR VALUES FROM ('2025-10-01') TO ('2025-11-01');

-- Índices
CREATE INDEX idx_srv_predictions_deployment ON srv_predictions(srv_deployment_id);
CREATE INDEX idx_srv_predictions_timestamp ON srv_predictions(srv_timestamp);
CREATE INDEX idx_srv_predictions_latency ON srv_predictions(srv_latency_ms);

-- Tabla de endpoints
CREATE TABLE srv_endpoints (
    srv_id BIGSERIAL PRIMARY KEY,
    srv_deployment_id BIGINT NOT NULL,
    srv_endpoint_type VARCHAR(50) NOT NULL, -- REST, GRPC, WEBSOCKET
    srv_url VARCHAR(500) NOT NULL,
    srv_method VARCHAR(20),
    srv_authentication VARCHAR(50),
    srv_rate_limit_per_second INTEGER,
    srv_timeout_ms INTEGER,
    srv_status VARCHAR(50) NOT NULL,
    srv_creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_srv_endpoint_deployment FOREIGN KEY (srv_deployment_id) 
        REFERENCES srv_deployments(srv_id)
);

-- Tabla de métricas de recursos
CREATE TABLE srv_resource_metrics (
    srv_id BIGSERIAL PRIMARY KEY,
    srv_deployment_id BIGINT NOT NULL,
    srv_replica_id VARCHAR(100),
    srv_cpu_usage_percent DECIMAL(5,2),
    srv_memory_usage_percent DECIMAL(5,2),
    srv_disk_usage_percent DECIMAL(5,2),
    srv_network_in_mbps DECIMAL(10,2),
    srv_network_out_mbps DECIMAL(10,2),
    srv_timestamp TIMESTAMP NOT NULL,
    CONSTRAINT fk_srv_metrics_deployment FOREIGN KEY (srv_deployment_id) 
        REFERENCES srv_deployments(srv_id)
);

-- Particionamiento para retención
CREATE TABLE srv_resource_metrics_y2025m10 PARTITION OF srv_resource_metrics
    FOR VALUES FROM ('2025-10-01') TO ('2025-11-01');

-- Tabla de configuración de SLA
CREATE TABLE srv_sla_config (
    srv_id BIGSERIAL PRIMARY KEY,
    srv_deployment_id BIGINT NOT NULL UNIQUE,
    srv_max_response_time_ms INTEGER NOT NULL,
    srv_min_availability DECIMAL(5,2) NOT NULL,
    srv_max_error_rate DECIMAL(5,4) NOT NULL,
    srv_alert_threshold_latency INTEGER,
    srv_alert_threshold_availability DECIMAL(5,2),
    srv_alert_threshold_error_rate DECIMAL(5,4),
    srv_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    srv_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_srv_sla_deployment FOREIGN KEY (srv_deployment_id) 
        REFERENCES srv_deployments(srv_id)
);

-- Tabla de análisis de drift
CREATE TABLE srv_drift_analysis (
    srv_id BIGSERIAL PRIMARY KEY,
    srv_deployment_id BIGINT NOT NULL,
    srv_feature_name VARCHAR(255),
    srv_drift_score DECIMAL(5,4),
    srv_drift_threshold DECIMAL(5,4),
    srv_drift_detected BOOLEAN,
    srv_recommendation TEXT,
    srv_analysis_timestamp TIMESTAMP NOT NULL,
    CONSTRAINT fk_srv_drift_deployment FOREIGN KEY (srv_deployment_id) 
        REFERENCES srv_deployments(srv_id)
);

CREATE INDEX idx_srv_drift_deployment ON srv_drift_analysis(srv_deployment_id);
CREATE INDEX idx_srv_drift_timestamp ON srv_drift_analysis(srv_analysis_timestamp);
```

---

## 🔒 SEGURIDAD

### **1. Autenticación y Autorización**
```java
@Configuration
@EnableWebSecurity
public class ServingSecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/serving/health").permitAll()
                .requestMatchers("/api/serving/metrics").permitAll()
                .requestMatchers("/api/serving/**").authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter()))
            )
            .csrf().disable();
            
        return http.build();
    }
}
```

### **2. Rate Limiting**
```java
@Component
public class RateLimitingFilter extends OncePerRequestFilter {
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response,
                                   FilterChain filterChain) throws ServletException, IOException {
        String apiKey = request.getHeader("X-API-Key");
        String rateLimitKey = "rate_limit:" + apiKey;
        
        Long requestCount = redisTemplate.opsForValue().increment(rateLimitKey);
        
        if (requestCount == 1) {
            redisTemplate.expire(rateLimitKey, 60, TimeUnit.SECONDS);
        }
        
        RateLimitConfig config = getRateLimitConfig(apiKey);
        
        if (requestCount > config.getMaxRequestsPerMinute()) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Rate limit exceeded");
            return;
        }
        
        filterChain.doFilter(request, response);
    }
}
```

---

## 📊 MONITOREO Y OBSERVABILIDAD

### **1. Prometheus Metrics**
```java
@Component
public class ServingMetrics {
    
    private final Counter predictionsTotal;
    private final Histogram predictionLatency;
    private final Gauge activeDeployments;
    
    public ServingMetrics(MeterRegistry registry) {
        this.predictionsTotal = Counter.builder("serving_predictions_total")
            .description("Total predictions made")
            .tags("deployment_id", "status")
            .register(registry);
            
        this.predictionLatency = Histogram.builder("serving_prediction_latency_seconds")
            .description("Prediction latency")
            .tags("deployment_id")
            .register(registry);
            
        this.activeDeployments = Gauge.builder("serving_active_deployments")
            .description("Active deployments")
            .register(registry);
    }
}
```

### **2. Distributed Tracing**
```java
@Configuration
public class TracingConfig {
    
    @Bean
    public Tracer tracer() {
        return GlobalOpenTelemetry.getTracer("codeflowx-serving");
    }
    
    @Around("@annotation(Traced)")
    public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {
        Span span = tracer.spanBuilder(joinPoint.getSignature().getName()).startSpan();
        try (Scope scope = span.makeCurrent()) {
            return joinPoint.proceed();
        } finally {
            span.end();
        }
    }
}
```

---

## 🎯 CONCLUSIONES TÉCNICAS

### **Fortalezas Arquitectónicas:**
1. ✅ **Cloud-Native:** Kubernetes-native, escalabilidad ilimitada
2. ✅ **High Performance:** < 100ms latencia, caché inteligente
3. ✅ **Auto-Scaling:** Horizontal y vertical automático
4. ✅ **Observabilidad:** Prometheus, Grafana, distributed tracing
5. ✅ **Resilencia:** Multi-zona, health checks, auto-recovery

### **Decisiones de Diseño:**
1. **Kubernetes:** Orchestration estándar de la industria
2. **Spring Boot:** Framework enterprise-proven
3. **PostgreSQL:** ACID compliance, particionamiento
4. **Redis:** Caché de alta performance
5. **Prometheus:** Monitoreo cloud-native

### **Escalabilidad:**
- **Horizontal:** Ilimitada con Kubernetes
- **Vertical:** Configuración por deployment
- **Multi-región:** Soporte nativo
- **Edge deployment:** Roadmap Q2 2026

---

## 📞 CONTACTO TÉCNICO

**Arquitectura:** architecture@codeflowx.com  
**Soporte Técnico:** support@codeflowx.com  
**Documentación:** docs.codeflowx.com/serving  
**GitHub:** github.com/codeflowx/serving
