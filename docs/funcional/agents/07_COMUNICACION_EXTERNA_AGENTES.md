# 🌐 COMUNICACIÓN EXTERNA DE AGENTES - SISTEMAS DE TELEMETRÍA

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentar cómo los agentes externos comunican métricas y decisiones al sistema central

---

## 📊 RESUMEN EJECUTIVO

Los agentes externos o desplegados en otros entornos necesitan **múltiples canales de comunicación** para enviar métricas, decisiones y eventos al sistema central de CodeflowX Govern. El sistema actual tiene la capacidad de **recibir y procesar** esta información, pero necesita **endpoints específicos** para la comunicación externa.

---

## 🔄 ARQUITECTURA DE COMUNICACIÓN

### **Flujo de Comunicación:**

```
┌─────────────────────────────────────────────────────┐
│  AGENTE EXTERNO/DESPLEGADO                          │
│  • Kubernetes Cluster                              │
│  • Cloud Provider (AWS/Azure/GCP)                   │
│  • On-premise Server                               │
│  • Third-party Platform                            │
└─────────────────────────────────────────────────────┘
                             ↓
┌─────────────────────────────────────────────────────┐
│  CANALES DE COMUNICACIÓN                            │
│  • REST API Endpoints                               │
│  • Webhooks                                         │
│  • Message Queues (Kafka/RabbitMQ)                 │
│  • gRPC Streams                                     │
└─────────────────────────────────────────────────────┘
                             ↓
┌─────────────────────────────────────────────────────┐
│  CODEFLOWX GOVERN CENTRAL                           │
│  • TelemetryReceiverService                         │
│  • MetricsCollectorService                          │
│  • DecisionLoggerService                            │
│  • ComplianceMonitoringService                      │
└─────────────────────────────────────────────────────┘
```

---

## 🌐 ENDPOINTS DE COMUNICACIÓN

### **1. REST API para Métricas**

#### **Endpoint Principal:**
```http
POST /api/v1/telemetry/metrics
Content-Type: application/json
Authorization: Bearer <agent_token>
```

#### **Request Body:**
```json
{
  "agentId": "agent_123",
  "timestamp": "2025-10-01T12:00:00Z",
  "environment": "production",
  "version": "1.2.0",
  "metrics": {
    "performance": {
      "responseTime": 245.5,
      "throughput": 1250.0,
      "errorRate": 0.02,
      "availability": 99.9,
      "cpuUsage": 65.2,
      "memoryUsage": 78.5,
      "diskUsage": 45.1
    },
    "business": {
      "requestsProcessed": 15000,
      "successfulRequests": 14970,
      "failedRequests": 30,
      "averageSessionDuration": 180.5,
      "userSatisfactionScore": 4.2
    },
    "ai": {
      "modelAccuracy": 94.5,
      "predictionConfidence": 0.87,
      "biasScore": 0.12,
      "explainabilityScore": 0.78,
      "fairnessScore": 0.91
    }
  },
  "metadata": {
    "deploymentId": "dep_789",
    "nodeId": "node_001",
    "region": "us-east-1",
    "dataCenter": "dc-01"
  }
}
```

#### **Response:**
```json
{
  "status": "success",
  "message": "Metrics received and processed",
  "metricsId": "metrics_456",
  "processedAt": "2025-10-01T12:00:01Z",
  "alerts": [
    {
      "type": "WARNING",
      "message": "CPU usage above threshold",
      "threshold": 70,
      "current": 65.2
    }
  ]
}
```

### **2. Webhook para Decisiones**

#### **Endpoint:**
```http
POST /api/v1/telemetry/decisions
Content-Type: application/json
X-Webhook-Signature: sha256=<signature>
```

#### **Request Body:**
```json
{
  "agentId": "agent_123",
  "decisionId": "decision_789",
  "timestamp": "2025-10-01T12:00:00Z",
  "decision": {
    "type": "APPROVAL",
    "action": "APPROVE",
    "confidence": 0.92,
    "reasoning": "Customer meets all eligibility criteria",
    "input": {
      "customerId": "cust_456",
      "requestType": "LOAN_APPLICATION",
      "amount": 50000,
      "creditScore": 750
    },
    "output": {
      "approved": true,
      "approvedAmount": 50000,
      "interestRate": 4.5,
      "conditions": ["Income verification required"]
    },
    "modelUsed": "loan_approval_v2",
    "modelVersion": "2.1.0"
  },
  "context": {
    "sessionId": "session_123",
    "userId": "user_456",
    "requestId": "req_789",
    "environment": "production"
  },
  "compliance": {
    "gdprCompliant": true,
    "auditTrail": "audit_trail_123",
    "dataRetention": "7_years"
  }
}
```

### **3. Stream de Eventos en Tiempo Real**

#### **gRPC Service:**
```protobuf
service AgentTelemetryService {
  rpc StreamMetrics(stream MetricsEvent) returns (MetricsResponse);
  rpc StreamDecisions(stream DecisionEvent) returns (DecisionResponse);
  rpc StreamAlerts(stream AlertEvent) returns (AlertResponse);
}

message MetricsEvent {
  string agent_id = 1;
  int64 timestamp = 2;
  map<string, double> metrics = 3;
  string environment = 4;
  string version = 5;
}

message DecisionEvent {
  string agent_id = 1;
  string decision_id = 2;
  int64 timestamp = 3;
  string decision_type = 4;
  string action = 5;
  double confidence = 6;
  string reasoning = 7;
  map<string, string> input_data = 8;
  map<string, string> output_data = 9;
}
```

---

## 🔧 SERVICIOS DE RECEPCIÓN

### **1. TelemetryReceiverService**

```java
@RestController
@RequestMapping("/api/v1/telemetry")
@Slf4j
public class TelemetryReceiverController {

    @Autowired
    private TelemetryReceiverService telemetryService;

    @PostMapping("/metrics")
    public ResponseEntity<TelemetryResponse> receiveMetrics(
            @RequestBody AgentMetricsRequest request,
            @RequestHeader("Authorization") String token) {
        
        try {
            // 1. Validar token del agente
            Agent agent = validateAgentToken(token);
            
            // 2. Procesar métricas
            TelemetryResponse response = telemetryService.processMetrics(agent, request);
            
            // 3. Guardar en PerformanceMetrics
            savePerformanceMetrics(agent, request);
            
            // 4. Verificar alertas
            List<Alert> alerts = checkAlerts(agent, request);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error processing metrics: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(TelemetryResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/decisions")
    public ResponseEntity<TelemetryResponse> receiveDecisions(
            @RequestBody AgentDecisionRequest request,
            @RequestHeader("X-Webhook-Signature") String signature) {
        
        try {
            // 1. Validar firma webhook
            validateWebhookSignature(request, signature);
            
            // 2. Procesar decisión
            TelemetryResponse response = telemetryService.processDecision(request);
            
            // 3. Guardar en AgentDecision
            saveAgentDecision(request);
            
            // 4. Registrar en auditoría
            logActivity("DECISION_RECEIVED", "AGENT", request.getAgentId(), 
                "Decision: " + request.getDecision().getAction());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error processing decision: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(TelemetryResponse.error(e.getMessage()));
        }
    }
}
```

### **2. MetricsCollectorService**

```java
@Service
@Slf4j
public class MetricsCollectorService {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private ComplianceMonitoringService complianceService;

    public void collectAndProcessMetrics(Agent agent, AgentMetricsRequest request) {
        try {
            // 1. Crear registro de PerformanceMetrics
            PerformanceMetrics metrics = new PerformanceMetrics();
            metrics.setPrfentitytype("AGENT");
            metrics.setPrfentityid(agent.getIdxagent());
            metrics.setPrflatencyms(request.getMetrics().getPerformance().getResponseTime());
            metrics.setPrfthroughput(request.getMetrics().getPerformance().getThroughput());
            metrics.setPrferrorrate(request.getMetrics().getPerformance().getErrorRate());
            metrics.setPrfmeasuredat(new Timestamp(request.getTimestamp()));
            metrics.setPrfcreatedat(new Timestamp(System.currentTimeMillis()));
            metrics.setPrfcreatedby("SYSTEM_TELEMETRY");

            businessService.save(metrics);

            // 2. Actualizar métricas del agente
            updateAgentMetrics(agent, request);

            // 3. Verificar compliance
            complianceService.checkSystemCompliance("AGENT", agent.getIdxagent());

            // 4. Generar alertas si es necesario
            generateAlertsIfNeeded(agent, request);

        } catch (Exception e) {
            log.error("Error collecting metrics for agent {}: {}", agent.getIdxagent(), e.getMessage(), e);
        }
    }

    private void updateAgentMetrics(Agent agent, AgentMetricsRequest request) {
        // Actualizar campos de métricas en la entidad Agent
        agent.setAgtscore(BigDecimal.valueOf(request.getMetrics().getAi().getModelAccuracy()));
        agent.setAgtupdatedat(new Timestamp(System.currentTimeMillis()));
        agent.setAgtupdatedby("SYSTEM_TELEMETRY");
        
        businessService.save(agent);
    }

    private void generateAlertsIfNeeded(Agent agent, AgentMetricsRequest request) {
        // Verificar umbrales y generar alertas
        if (request.getMetrics().getPerformance().getErrorRate() > 0.05) {
            createAlert(agent, "HIGH_ERROR_RATE", "CRITICAL", 
                "Error rate: " + request.getMetrics().getPerformance().getErrorRate());
        }
        
        if (request.getMetrics().getPerformance().getCpuUsage() > 90) {
            createAlert(agent, "HIGH_CPU_USAGE", "WARNING", 
                "CPU usage: " + request.getMetrics().getPerformance().getCpuUsage() + "%");
        }
    }
}
```

---

## 🔐 AUTENTICACIÓN Y SEGURIDAD

### **1. Autenticación de Agentes**

#### **API Key Authentication:**
```java
@Component
public class AgentAuthenticationService {

    public Agent validateAgentToken(String token) {
        try {
            // Extraer token del header "Bearer <token>"
            String apiKey = token.replace("Bearer ", "");
            
            // Buscar agente por API key
            String sql = "SELECT * FROM AGTAGENTS WHERE AGTAPIKEY = :apiKey AND AGTSTATUS = 'ACTIVE'";
            Map<String, Object> params = new HashMap<>();
            params.put("apiKey", apiKey);
            
            List<Agent> agents = businessService.findByParams(Agent.class, sql, params);
            
            if (agents.isEmpty()) {
                throw new UnauthorizedException("Invalid or inactive agent token");
            }
            
            return agents.get(0);
            
        } catch (Exception e) {
            log.error("Error validating agent token: {}", e.getMessage(), e);
            throw new UnauthorizedException("Token validation failed");
        }
    }
}
```

#### **Webhook Signature Validation:**
```java
@Component
public class WebhookSignatureValidator {

    private static final String HMAC_SHA256 = "HmacSHA256";

    public boolean validateSignature(String payload, String signature, String secret) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), HMAC_SHA256);
            mac.init(secretKeySpec);
            
            byte[] hash = mac.doFinal(payload.getBytes());
            String expectedSignature = "sha256=" + bytesToHex(hash);
            
            return MessageDigest.isEqual(signature.getBytes(), expectedSignature.getBytes());
            
        } catch (Exception e) {
            log.error("Error validating webhook signature: {}", e.getMessage(), e);
            return false;
        }
    }
}
```

### **2. Rate Limiting**

```java
@Component
public class TelemetryRateLimiter {

    private final Map<String, RateLimiter> agentLimiters = new ConcurrentHashMap<>();

    public boolean isAllowed(String agentId, int requestsPerMinute) {
        RateLimiter limiter = agentLimiters.computeIfAbsent(agentId, 
            id -> RateLimiter.create(requestsPerMinute / 60.0));
        
        return limiter.tryAcquire();
    }
}
```

---

## 📊 PROCESAMIENTO DE DATOS

### **1. Pipeline de Procesamiento**

```java
@Service
public class TelemetryProcessingPipeline {

    @Autowired
    private MetricsCollectorService metricsCollector;

    @Autowired
    private DecisionLoggerService decisionLogger;

    @Autowired
    private ComplianceMonitoringService complianceService;

    @Autowired
    private AlertService alertService;

    public void processTelemetryData(TelemetryData data) {
        try {
            // 1. Validar datos
            validateTelemetryData(data);

            // 2. Enriquecer con contexto
            enrichWithContext(data);

            // 3. Procesar según tipo
            switch (data.getType()) {
                case METRICS:
                    metricsCollector.collectAndProcessMetrics(data.getAgent(), data.getMetricsRequest());
                    break;
                case DECISION:
                    decisionLogger.logDecision(data.getAgent(), data.getDecisionRequest());
                    break;
                case ALERT:
                    alertService.processAlert(data.getAgent(), data.getAlertRequest());
                    break;
            }

            // 4. Actualizar dashboards
            updateDashboards(data);

            // 5. Verificar compliance
            complianceService.checkSystemCompliance("AGENT", data.getAgent().getIdxagent());

        } catch (Exception e) {
            log.error("Error processing telemetry data: {}", e.getMessage(), e);
            throw new TelemetryProcessingException("Failed to process telemetry data", e);
        }
    }
}
```

### **2. Almacenamiento de Datos**

#### **Entidades de Almacenamiento:**
| Entidad | Propósito | Campos Clave |
|---------|-----------|--------------|
| **PerformanceMetrics** | Métricas de rendimiento | `prfentitytype`, `prfentityid`, `prflatencyms`, `prfthroughput` |
| **AgentDecision** | Decisiones del agente | `decisiontype`, `action`, `confidence`, `reasoning` |
| **AgentAlert** | Alertas del agente | `alerttype`, `severity`, `message`, `status` |
| **TelemetryLog** | Log de telemetría | `agentid`, `datatype`, `payload`, `timestamp` |

---

## 🚨 GESTIÓN DE ALERTAS EN TIEMPO REAL

### **1. Sistema de Alertas**

```java
@Service
public class RealTimeAlertService {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private NotificationService notificationService;

    public void processRealTimeAlert(Agent agent, AlertData alertData) {
        try {
            // 1. Crear alerta en BBDD
            AgentAlert alert = createAgentAlert(agent, alertData);

            // 2. Determinar severidad
            String severity = determineSeverity(alertData);

            // 3. Enviar notificaciones
            sendNotifications(alert, severity);

            // 4. Iniciar proceso BPMN si es crítico
            if ("CRITICAL".equals(severity)) {
                startCriticalAlertProcess(alert);
            }

        } catch (Exception e) {
            log.error("Error processing real-time alert: {}", e.getMessage(), e);
        }
    }

    private void startCriticalAlertProcess(AgentAlert alert) {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("alertId", alert.getIdxagentalert());
            variables.put("agentId", alert.getFkidxagent());
            variables.put("severity", alert.getAgtseverity());
            variables.put("message", alert.getAgtmessage());

            runtimeService.startProcessInstanceByKey("critical-alert-response", variables);

        } catch (Exception e) {
            log.error("Error starting critical alert process: {}", e.getMessage(), e);
        }
    }
}
```

### **2. Notificaciones**

```java
@Service
public class NotificationService {

    public void sendAlertNotification(AgentAlert alert, String severity) {
        // 1. Email notifications
        if (isEmailNotificationEnabled(severity)) {
            sendEmailNotification(alert);
        }

        // 2. Slack notifications
        if (isSlackNotificationEnabled(severity)) {
            sendSlackNotification(alert);
        }

        // 3. SMS notifications (critical only)
        if ("CRITICAL".equals(severity)) {
            sendSMSNotification(alert);
        }

        // 4. Webhook notifications
        sendWebhookNotifications(alert);
    }
}
```

---

## 🔄 CONFIGURACIÓN DE AGENTES EXTERNOS

### **1. SDK para Agentes**

#### **Python SDK:**
```python
from codeflowx_agent_sdk import TelemetryClient

# Configurar cliente
client = TelemetryClient(
    api_key="agent_api_key_123",
    base_url="https://govern.codeflowx.com/api/v1",
    agent_id="agent_123"
)

# Enviar métricas
client.send_metrics({
    "performance": {
        "responseTime": 245.5,
        "throughput": 1250.0,
        "errorRate": 0.02
    },
    "ai": {
        "modelAccuracy": 94.5,
        "predictionConfidence": 0.87
    }
})

# Enviar decisión
client.send_decision({
    "type": "APPROVAL",
    "action": "APPROVE",
    "confidence": 0.92,
    "reasoning": "Customer meets criteria"
})
```

#### **JavaScript SDK:**
```javascript
const { TelemetryClient } = require('@codeflowx/agent-sdk');

const client = new TelemetryClient({
    apiKey: 'agent_api_key_123',
    baseUrl: 'https://govern.codeflowx.com/api/v1',
    agentId: 'agent_123'
});

// Enviar métricas
await client.sendMetrics({
    performance: {
        responseTime: 245.5,
        throughput: 1250.0,
        errorRate: 0.02
    }
});

// Enviar decisión
await client.sendDecision({
    type: 'APPROVAL',
    action: 'APPROVE',
    confidence: 0.92,
    reasoning: 'Customer meets criteria'
});
```

### **2. Configuración de Agente**

```yaml
# agent-config.yaml
telemetry:
  enabled: true
  endpoint: "https://govern.codeflowx.com/api/v1/telemetry"
  api_key: "${AGENT_API_KEY}"
  
  metrics:
    interval: 60s  # Enviar métricas cada 60 segundos
    batch_size: 100
    retry_attempts: 3
    
  decisions:
    enabled: true
    webhook_secret: "${WEBHOOK_SECRET}"
    
  alerts:
    enabled: true
    thresholds:
      error_rate: 0.05
      response_time: 500
      cpu_usage: 90
      memory_usage: 90

logging:
  level: INFO
  format: json
  
security:
  tls_verify: true
  timeout: 30s
```

---

## 📈 MONITOREO DE COMUNICACIÓN

### **1. Métricas de Comunicación**

```java
@Component
public class CommunicationMetricsService {

    public void trackCommunicationMetrics(String agentId, String endpoint, long responseTime, boolean success) {
        try {
            CommunicationMetrics metrics = new CommunicationMetrics();
            metrics.setAgentId(agentId);
            metrics.setEndpoint(endpoint);
            metrics.setResponseTime(responseTime);
            metrics.setSuccess(success);
            metrics.setTimestamp(new Timestamp(System.currentTimeMillis()));
            
            businessService.save(metrics);
            
        } catch (Exception e) {
            log.error("Error tracking communication metrics: {}", e.getMessage(), e);
        }
    }
}
```

### **2. Dashboard de Comunicación**

**Métricas Monitoreadas:**
- **Latencia de comunicación** por agente
- **Tasa de éxito** de mensajes
- **Volumen de datos** recibidos
- **Errores de comunicación** por tipo
- **Agentes desconectados** o inactivos

---

## ✅ CONCLUSIÓN

El sistema de comunicación externa de agentes incluye:

- ✅ **REST API** para métricas y decisiones
- ✅ **Webhooks** para eventos en tiempo real
- ✅ **gRPC streams** para comunicación continua
- ✅ **Autenticación robusta** con API keys y firmas
- ✅ **Rate limiting** y seguridad
- ✅ **SDKs** para múltiples lenguajes
- ✅ **Procesamiento en tiempo real** de datos
- ✅ **Sistema de alertas** automático
- ✅ **Monitoreo** de comunicación

**Estado:** Sistema de comunicación externa completamente documentado ✅

**Próximo:** Implementar los endpoints y servicios faltantes
