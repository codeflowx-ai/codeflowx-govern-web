# 🔌 API Y SDK - MÓDULO AGENTES

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Definir API REST y SDK para integración externa del módulo de agentes

---

## 📊 RESUMEN EJECUTIVO

El módulo de agentes expone una **API REST completa** y **SDKs** para múltiples lenguajes, permitiendo integración externa para desarrollo, despliegue y monitoreo de agentes de IA.

---

## 🌐 API REST

### **Base URL:**
```
https://api.codeflowx-govern.com/v1/agents
```

### **Autenticación:**
```http
Authorization: Bearer <jwt_token>
Content-Type: application/json
```

---

## 📋 ENDPOINTS PRINCIPALES

### **1. Gestión de Agentes**

#### **GET /agents**
Lista todos los agentes con filtros y paginación.

**Parámetros:**
```json
{
  "page": 1,
  "size": 20,
  "status": "ACTIVE",
  "type": "LLM",
  "search": "nombre_agente"
}
```

**Respuesta:**
```json
{
  "content": [
    {
      "id": 123,
      "name": "Customer Service Agent",
      "type": "LLM",
      "status": "ACTIVE",
      "version": "1.2.0",
      "createdAt": "2025-10-01T10:00:00Z",
      "createdBy": "user@company.com",
      "approvalStatus": "APPROVED",
      "healthScore": 85.5,
      "complianceScore": 92.0
    }
  ],
  "totalElements": 150,
  "totalPages": 8,
  "page": 1,
  "size": 20
}
```

#### **POST /agents**
Crea un nuevo agente.

**Request:**
```json
{
  "name": "New Customer Agent",
  "type": "LLM",
  "description": "Agent for customer service",
  "capabilities": {
    "language": "es",
    "domains": ["customer_service", "billing"],
    "maxTokens": 4000
  },
  "configuration": {
    "model": "gpt-4",
    "temperature": 0.7,
    "maxRetries": 3
  },
  "metadata": {
    "team": "customer_success",
    "priority": "high"
  }
}
```

**Respuesta:**
```json
{
  "id": 124,
  "name": "New Customer Agent",
  "status": "DRAFT",
  "approvalStatus": "PENDING",
  "createdAt": "2025-10-01T10:30:00Z",
  "approvalWorkflowId": "wf_123456"
}
```

#### **GET /agents/{id}**
Obtiene detalles de un agente específico.

**Respuesta:**
```json
{
  "id": 123,
  "name": "Customer Service Agent",
  "type": "LLM",
  "status": "ACTIVE",
  "version": "1.2.0",
  "description": "Agent for customer service",
  "capabilities": {
    "language": "es",
    "domains": ["customer_service", "billing"],
    "maxTokens": 4000
  },
  "configuration": {
    "model": "gpt-4",
    "temperature": 0.7,
    "maxRetries": 3
  },
  "metadata": {
    "team": "customer_success",
    "priority": "high"
  },
  "approvalStatus": "APPROVED",
  "approvedBy": "governance@company.com",
  "approvedAt": "2025-09-28T15:00:00Z",
  "healthScore": 85.5,
  "complianceScore": 92.0,
  "createdAt": "2025-10-01T10:00:00Z",
  "updatedAt": "2025-10-01T12:00:00Z"
}
```

#### **PUT /agents/{id}**
Actualiza un agente existente.

#### **DELETE /agents/{id}**
Elimina un agente (soft delete).

---

### **2. Aprobación de Agentes**

#### **POST /agents/{id}/approval**
Inicia proceso de aprobación.

**Request:**
```json
{
  "reason": "New version with improved accuracy",
  "priority": "HIGH",
  "notifyStakeholders": true
}
```

#### **GET /agents/{id}/approval/status**
Obtiene estado del proceso de aprobación.

**Respuesta:**
```json
{
  "workflowId": "wf_123456",
  "status": "IN_PROGRESS",
  "currentStep": "COMPLIANCE_CHECK",
  "progress": 60,
  "estimatedCompletion": "2025-10-02T10:00:00Z",
  "steps": [
    {
      "name": "RISK_ASSESSMENT",
      "status": "COMPLETED",
      "score": 85,
      "completedAt": "2025-10-01T11:00:00Z"
    },
    {
      "name": "COMPLIANCE_CHECK",
      "status": "IN_PROGRESS",
      "score": null,
      "assignedTo": "compliance@company.com"
    },
    {
      "name": "ETHICS_REVIEW",
      "status": "PENDING",
      "score": null,
      "assignedTo": null
    }
  ]
}
```

#### **POST /agents/{id}/approval/decide**
Toma decisión de aprobación (solo para usuarios autorizados).

**Request:**
```json
{
  "decision": "APPROVE",
  "comments": "Agent meets all requirements",
  "conditions": ["Monitor performance for 30 days"]
}
```

---

### **3. Despliegue de Agentes**

#### **POST /agents/{id}/deploy**
Inicia proceso de despliegue.

**Request:**
```json
{
  "environment": "production",
  "version": "1.2.0",
  "strategy": "rolling",
  "config": {
    "replicas": 3,
    "resources": {
      "cpu": "1000m",
      "memory": "2Gi"
    }
  }
}
```

**Respuesta:**
```json
{
  "deploymentId": "dep_789012",
  "status": "IN_PROGRESS",
  "environment": "production",
  "version": "1.2.0",
  "estimatedDuration": "30 minutes",
  "deploymentUrl": "https://agents.company.com/customer-service"
}
```

#### **GET /agents/{id}/deployments**
Lista despliegues del agente.

#### **GET /agents/{id}/deployments/{deploymentId}/status**
Obtiene estado del despliegue.

#### **POST /agents/{id}/deployments/{deploymentId}/rollback**
Ejecuta rollback del despliegue.

---

### **4. Monitoreo y Métricas**

#### **GET /agents/{id}/metrics**
Obtiene métricas del agente.

**Parámetros:**
```json
{
  "timeRange": "24h",
  "metrics": ["response_time", "accuracy", "throughput"],
  "granularity": "1h"
}
```

**Respuesta:**
```json
{
  "agentId": 123,
  "timeRange": "24h",
  "metrics": {
    "response_time": {
      "avg": 250.5,
      "p95": 450.0,
      "p99": 800.0,
      "data": [
        {"timestamp": "2025-10-01T00:00:00Z", "value": 245.2},
        {"timestamp": "2025-10-01T01:00:00Z", "value": 255.8}
      ]
    },
    "accuracy": {
      "avg": 94.2,
      "data": [
        {"timestamp": "2025-10-01T00:00:00Z", "value": 94.5},
        {"timestamp": "2025-10-01T01:00:00Z", "value": 93.9}
      ]
    }
  }
}
```

#### **GET /agents/{id}/health**
Obtiene estado de salud del agente.

**Respuesta:**
```json
{
  "agentId": 123,
  "status": "HEALTHY",
  "score": 85.5,
  "lastCheck": "2025-10-01T12:00:00Z",
  "checks": [
    {
      "name": "response_time",
      "status": "PASS",
      "value": 250.5,
      "threshold": 500.0
    },
    {
      "name": "error_rate",
      "status": "WARN",
      "value": 2.1,
      "threshold": 1.0
    }
  ]
}
```

#### **GET /agents/{id}/alerts**
Lista alertas activas del agente.

#### **POST /agents/{id}/alerts/{alertId}/resolve**
Resuelve una alerta.

---

### **5. Evaluación y Testing**

#### **POST /agents/{id}/evaluate**
Ejecuta evaluación del agente.

**Request:**
```json
{
  "testSuite": "customer_service_tests",
  "parameters": {
    "temperature": 0.7,
    "maxTokens": 1000
  },
  "async": true
}
```

#### **GET /agents/{id}/evaluations/{evaluationId}/results**
Obtiene resultados de evaluación.

---

### **6. Telemetría y Métricas (Para Agentes Externos)**

#### **POST /agents/{id}/telemetry/metrics**
Envía métricas de performance desde agentes externos.

**Request:**
```json
{
  "timestamp": "2025-10-01T12:00:00Z",
  "metrics": {
    "response_time_ms": 245.5,
    "throughput_rps": 1250.0,
    "error_rate": 0.02,
    "cpu_usage_percent": 65.2,
    "memory_usage_percent": 78.5,
    "gpu_usage_percent": 45.0,
    "accuracy_score": 0.94,
    "latency_p95": 450.0,
    "latency_p99": 800.0
  },
  "context": {
    "environment": "production",
    "version": "1.2.0",
    "instance_id": "agent-instance-001",
    "region": "us-east-1"
  }
}
```

**Respuesta:**
```json
{
  "status": "received",
  "metricsId": "metrics_123456",
  "processedAt": "2025-10-01T12:00:01Z",
  "alertsTriggered": []
}
```

#### **POST /agents/{id}/telemetry/decisions**
Registra decisiones tomadas por el agente.

**Request:**
```json
{
  "timestamp": "2025-10-01T12:00:00Z",
  "decisionId": "dec_789012",
  "decisionType": "APPROVAL",
  "decision": "APPROVED",
  "confidence": 0.87,
  "input": {
    "requestType": "customer_query",
    "complexity": "medium",
    "domain": "billing"
  },
  "output": {
    "response": "Account balance is $1,250.00",
    "reasoning": "Retrieved from customer database",
    "sources": ["customer_db", "billing_system"]
  },
  "metadata": {
    "processingTime": 150,
    "modelVersion": "gpt-4-turbo",
    "temperature": 0.7
  }
}
```

#### **POST /agents/{id}/telemetry/errors**
Reporta errores y excepciones.

**Request:**
```json
{
  "timestamp": "2025-10-01T12:00:00Z",
  "errorId": "err_345678",
  "errorType": "TIMEOUT",
  "severity": "HIGH",
  "message": "Model inference timeout after 30s",
  "stackTrace": "TimeoutException at ModelInference.execute()...",
  "context": {
    "requestId": "req_123456",
    "modelVersion": "gpt-4-turbo",
    "inputSize": 1024,
    "retryCount": 2
  },
  "resolution": {
    "action": "RETRY_WITH_FALLBACK",
    "success": true,
    "resolutionTime": 5000
  }
}
```

#### **POST /agents/{id}/telemetry/health**
Reporta estado de salud del agente.

**Request:**
```json
{
  "timestamp": "2025-10-01T12:00:00Z",
  "status": "HEALTHY",
  "healthScore": 85.5,
  "checks": [
    {
      "name": "model_availability",
      "status": "PASS",
      "responseTime": 120,
      "details": "Model responding normally"
    },
    {
      "name": "database_connectivity",
      "status": "PASS",
      "responseTime": 45,
      "details": "Database connection stable"
    },
    {
      "name": "memory_usage",
      "status": "WARN",
      "value": 85.2,
      "threshold": 80.0,
      "details": "Memory usage approaching limit"
    }
  ],
  "uptime": 86400,
  "lastRestart": "2025-09-30T00:00:00Z"
}
```

#### **POST /agents/{id}/telemetry/batch**
Envía múltiples eventos de telemetría en lote.

**Request:**
```json
{
  "batchId": "batch_456789",
  "timestamp": "2025-10-01T12:00:00Z",
  "events": [
    {
      "type": "METRICS",
      "data": {
        "response_time_ms": 245.5,
        "throughput_rps": 1250.0
      }
    },
    {
      "type": "DECISION",
      "data": {
        "decisionId": "dec_001",
        "decision": "APPROVED",
        "confidence": 0.87
      }
    },
    {
      "type": "HEALTH",
      "data": {
        "status": "HEALTHY",
        "healthScore": 85.5
      }
    }
  ]
}
```

---

## 🛠️ SDKs

### **1. Python SDK**

#### **Instalación:**
```bash
pip install codeflowx-agents-sdk
```

#### **Uso:**
```python
from codeflowx_agents import AgentClient

# Inicializar cliente
client = AgentClient(
    api_key="your_api_key",
    base_url="https://api.codeflowx-govern.com"
)

# Crear agente
agent = client.agents.create({
    "name": "My Agent",
    "type": "LLM",
    "description": "My custom agent",
    "capabilities": {
        "language": "en",
        "domains": ["customer_service"]
    }
})

# Obtener agente
agent = client.agents.get(agent.id)

# Iniciar aprobación
approval = client.agents.start_approval(agent.id, {
    "reason": "Production deployment",
    "priority": "HIGH"
})

# Desplegar agente
deployment = client.agents.deploy(agent.id, {
    "environment": "production",
    "version": "1.0.0"
})

# Obtener métricas
metrics = client.agents.get_metrics(agent.id, {
    "timeRange": "24h",
    "metrics": ["response_time", "accuracy"]
})

# Enviar telemetría desde agente externo
client.agents.send_telemetry(agent.id, {
    "type": "metrics",
    "data": {
        "response_time_ms": 245.5,
        "throughput_rps": 1250.0,
        "error_rate": 0.02,
        "cpu_usage_percent": 65.2
    }
})

# Registrar decisión del agente
client.agents.log_decision(agent.id, {
    "decisionId": "dec_001",
    "decisionType": "APPROVAL",
    "decision": "APPROVED",
    "confidence": 0.87,
    "input": {"requestType": "customer_query"},
    "output": {"response": "Account balance is $1,250.00"}
})

# Reportar error
client.agents.report_error(agent.id, {
    "errorId": "err_001",
    "errorType": "TIMEOUT",
    "severity": "HIGH",
    "message": "Model inference timeout"
})

# Enviar estado de salud
client.agents.send_health_status(agent.id, {
    "status": "HEALTHY",
    "healthScore": 85.5,
    "checks": [
        {"name": "model_availability", "status": "PASS"},
        {"name": "memory_usage", "status": "WARN", "value": 85.2}
    ]
})

# Envío en lote
client.agents.send_batch_telemetry(agent.id, [
    {"type": "METRICS", "data": {"response_time_ms": 245.5}},
    {"type": "DECISION", "data": {"decision": "APPROVED"}},
    {"type": "HEALTH", "data": {"status": "HEALTHY"}}
])
```

### **2. JavaScript/Node.js SDK**

#### **Instalación:**
```bash
npm install @codeflowx/agents-sdk
```

#### **Uso:**
```javascript
const { AgentClient } = require('@codeflowx/agents-sdk');

// Inicializar cliente
const client = new AgentClient({
  apiKey: 'your_api_key',
  baseUrl: 'https://api.codeflowx-govern.com'
});

// Crear agente
const agent = await client.agents.create({
  name: 'My Agent',
  type: 'LLM',
  description: 'My custom agent',
  capabilities: {
    language: 'en',
    domains: ['customer_service']
  }
});

// Obtener métricas
const metrics = await client.agents.getMetrics(agent.id, {
  timeRange: '24h',
  metrics: ['response_time', 'accuracy']
});

// Enviar telemetría desde agente externo
await client.agents.sendTelemetry(agent.id, {
  type: 'metrics',
  data: {
    response_time_ms: 245.5,
    throughput_rps: 1250.0,
    error_rate: 0.02,
    cpu_usage_percent: 65.2
  }
});

// Registrar decisión del agente
await client.agents.logDecision(agent.id, {
  decisionId: 'dec_001',
  decisionType: 'APPROVAL',
  decision: 'APPROVED',
  confidence: 0.87,
  input: { requestType: 'customer_query' },
  output: { response: 'Account balance is $1,250.00' }
});

// Reportar error
await client.agents.reportError(agent.id, {
  errorId: 'err_001',
  errorType: 'TIMEOUT',
  severity: 'HIGH',
  message: 'Model inference timeout'
});

// Enviar estado de salud
await client.agents.sendHealthStatus(agent.id, {
  status: 'HEALTHY',
  healthScore: 85.5,
  checks: [
    { name: 'model_availability', status: 'PASS' },
    { name: 'memory_usage', status: 'WARN', value: 85.2 }
  ]
});

// Envío en lote
await client.agents.sendBatchTelemetry(agent.id, [
  { type: 'METRICS', data: { response_time_ms: 245.5 } },
  { type: 'DECISION', data: { decision: 'APPROVED' } },
  { type: 'HEALTH', data: { status: 'HEALTHY' } }
]);
```

### **3. Java SDK**

#### **Maven Dependency:**
```xml
<dependency>
    <groupId>com.codeflowx</groupId>
    <artifactId>agents-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### **Uso:**
```java
import com.codeflowx.agents.AgentClient;
import com.codeflowx.agents.model.Agent;
import com.codeflowx.agents.model.AgentCreateRequest;

// Inicializar cliente
AgentClient client = new AgentClient.Builder()
    .apiKey("your_api_key")
    .baseUrl("https://api.codeflowx-govern.com")
    .build();

// Crear agente
AgentCreateRequest request = AgentCreateRequest.builder()
    .name("My Agent")
    .type("LLM")
    .description("My custom agent")
    .capabilities(Map.of(
        "language", "en",
        "domains", Arrays.asList("customer_service")
    ))
    .build();

Agent agent = client.agents().create(request);

// Obtener métricas
Metrics metrics = client.agents().getMetrics(agent.getId(), 
    MetricsRequest.builder()
        .timeRange("24h")
        .metrics(Arrays.asList("response_time", "accuracy"))
        .build()
);

// Enviar telemetría desde agente externo
TelemetryRequest telemetryRequest = TelemetryRequest.builder()
    .type("metrics")
    .data(Map.of(
        "response_time_ms", 245.5,
        "throughput_rps", 1250.0,
        "error_rate", 0.02,
        "cpu_usage_percent", 65.2
    ))
    .build();
client.agents().sendTelemetry(agent.getId(), telemetryRequest);

// Registrar decisión del agente
DecisionRequest decisionRequest = DecisionRequest.builder()
    .decisionId("dec_001")
    .decisionType("APPROVAL")
    .decision("APPROVED")
    .confidence(0.87)
    .input(Map.of("requestType", "customer_query"))
    .output(Map.of("response", "Account balance is $1,250.00"))
    .build();
client.agents().logDecision(agent.getId(), decisionRequest);

// Reportar error
ErrorRequest errorRequest = ErrorRequest.builder()
    .errorId("err_001")
    .errorType("TIMEOUT")
    .severity("HIGH")
    .message("Model inference timeout")
    .build();
client.agents().reportError(agent.getId(), errorRequest);

// Enviar estado de salud
HealthRequest healthRequest = HealthRequest.builder()
    .status("HEALTHY")
    .healthScore(85.5)
    .checks(Arrays.asList(
        HealthCheck.builder().name("model_availability").status("PASS").build(),
        HealthCheck.builder().name("memory_usage").status("WARN").value(85.2).build()
    ))
    .build();
client.agents().sendHealthStatus(agent.getId(), healthRequest);

// Envío en lote
List<BatchTelemetryEvent> events = Arrays.asList(
    BatchTelemetryEvent.builder().type("METRICS").data(Map.of("response_time_ms", 245.5)).build(),
    BatchTelemetryEvent.builder().type("DECISION").data(Map.of("decision", "APPROVED")).build(),
    BatchTelemetryEvent.builder().type("HEALTH").data(Map.of("status", "HEALTHY")).build()
);
client.agents().sendBatchTelemetry(agent.getId(), events);
```

---

## 🔧 IMPLEMENTACIÓN DE ENDPOINTS DE TELEMETRÍA

### **1. Controlador REST para Telemetría**

**Ubicación:** `src/main/java/com/codeflowx/govern/api/AgentTelemetryController.java`

```java
@RestController
@RequestMapping("/api/v1/agents")
@Slf4j
public class AgentTelemetryController {

    @Autowired
    private AgentTelemetryService telemetryService;

    @PostMapping("/{agentId}/telemetry/metrics")
    public ResponseEntity<TelemetryResponse> sendMetrics(
            @PathVariable Long agentId,
            @RequestBody MetricsTelemetryRequest request) {
        
        try {
            TelemetryResponse response = telemetryService.processMetrics(agentId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing metrics for agent {}: {}", agentId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(TelemetryResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{agentId}/telemetry/decisions")
    public ResponseEntity<TelemetryResponse> logDecision(
            @PathVariable Long agentId,
            @RequestBody DecisionTelemetryRequest request) {
        
        try {
            TelemetryResponse response = telemetryService.processDecision(agentId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing decision for agent {}: {}", agentId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(TelemetryResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{agentId}/telemetry/errors")
    public ResponseEntity<TelemetryResponse> reportError(
            @PathVariable Long agentId,
            @RequestBody ErrorTelemetryRequest request) {
        
        try {
            TelemetryResponse response = telemetryService.processError(agentId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing error report for agent {}: {}", agentId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(TelemetryResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{agentId}/telemetry/health")
    public ResponseEntity<TelemetryResponse> sendHealthStatus(
            @PathVariable Long agentId,
            @RequestBody HealthTelemetryRequest request) {
        
        try {
            TelemetryResponse response = telemetryService.processHealth(agentId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing health status for agent {}: {}", agentId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(TelemetryResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{agentId}/telemetry/batch")
    public ResponseEntity<TelemetryResponse> sendBatchTelemetry(
            @PathVariable Long agentId,
            @RequestBody BatchTelemetryRequest request) {
        
        try {
            TelemetryResponse response = telemetryService.processBatch(agentId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing batch telemetry for agent {}: {}", agentId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(TelemetryResponse.error(e.getMessage()));
        }
    }
}
```

### **2. Servicio de Procesamiento de Telemetría**

**Ubicación:** `src/main/java/com/codeflowx/govern/services/AgentTelemetryService.java`

```java
@Service
@Slf4j
public class AgentTelemetryService {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private ComplianceMonitoringService complianceService;

    @Autowired
    private AlertService alertService;

    public TelemetryResponse processMetrics(Long agentId, MetricsTelemetryRequest request) {
        try {
            // 1. Validar agente existe
            Agent agent = validateAgent(agentId);
            
            // 2. Crear registro de PerformanceMetrics
            PerformanceMetrics metrics = new PerformanceMetrics();
            metrics.setPrfentitytype("AGENT");
            metrics.setPrfentityid(agentId);
            metrics.setPrflatencyms(request.getMetrics().getResponseTimeMs());
            metrics.setPrfthroughput(request.getMetrics().getThroughputRps());
            metrics.setPrferrorrate(request.getMetrics().getErrorRate());
            metrics.setPrfmeasuredat(Timestamp.valueOf(LocalDateTime.now()));
            metrics.setPrfcreatedat(Timestamp.valueOf(LocalDateTime.now()));
            metrics.setPrfcreatedby("TELEMETRY_SYSTEM");
            
            businessService.save(metrics);
            
            // 3. Verificar umbrales y generar alertas
            List<String> alertsTriggered = checkThresholds(agentId, request.getMetrics());
            
            // 4. Actualizar estado del agente
            updateAgentHealthStatus(agentId, request.getMetrics());
            
            return TelemetryResponse.success(metrics.getIdxperformancemetric(), alertsTriggered);
            
        } catch (Exception e) {
            log.error("Error processing metrics for agent {}: {}", agentId, e.getMessage(), e);
            throw new RuntimeException("Failed to process metrics: " + e.getMessage(), e);
        }
    }

    public TelemetryResponse processDecision(Long agentId, DecisionTelemetryRequest request) {
        try {
            // 1. Validar agente existe
            Agent agent = validateAgent(agentId);
            
            // 2. Crear registro de AgentDecision
            AgentDecision decision = new AgentDecision();
            decision.setFkidxagent(agentId);
            decision.setAgtdecisiontype(request.getDecisionType());
            decision.setAgtdecision(request.getDecision());
            decision.setAgtconfidence(request.getConfidence());
            decision.setAgtinputdata(toJsonString(request.getInput()));
            decision.setAgtoutputdata(toJsonString(request.getOutput()));
            decision.setAgtprocessingtime(request.getMetadata().getProcessingTime());
            decision.setAgtcreatedat(Timestamp.valueOf(LocalDateTime.now()));
            decision.setAgtcreatedby("TELEMETRY_SYSTEM");
            
            businessService.save(decision);
            
            // 3. Registrar en auditoría
            logActivity("DECISION_LOG", "AGENT", agentId, 
                String.format("Decision %s logged with confidence %.2f", 
                    request.getDecision(), request.getConfidence()));
            
            return TelemetryResponse.success(decision.getIdxagentdecision(), Collections.emptyList());
            
        } catch (Exception e) {
            log.error("Error processing decision for agent {}: {}", agentId, e.getMessage(), e);
            throw new RuntimeException("Failed to process decision: " + e.getMessage(), e);
        }
    }

    public TelemetryResponse processError(Long agentId, ErrorTelemetryRequest request) {
        try {
            // 1. Validar agente existe
            Agent agent = validateAgent(agentId);
            
            // 2. Crear registro de AgentAlert
            AgentAlert alert = new AgentAlert();
            alert.setFkidxagent(agentId);
            alert.setAgtalerttype(request.getErrorType());
            alert.setAgtseverity(request.getSeverity());
            alert.setAgtmessage(request.getMessage());
            alert.setAgtstacktrace(request.getStackTrace());
            alert.setAgtcontextdata(toJsonString(request.getContext()));
            alert.setAgtstatus("ACTIVE");
            alert.setAgtcreatedat(Timestamp.valueOf(LocalDateTime.now()));
            alert.setAgtcreatedby("TELEMETRY_SYSTEM");
            
            businessService.save(alert);
            
            // 3. Generar alerta si es crítica
            if ("CRITICAL".equals(request.getSeverity()) || "HIGH".equals(request.getSeverity())) {
                alertService.createAlert(agentId, request.getErrorType(), request.getSeverity(), request.getMessage());
            }
            
            return TelemetryResponse.success(alert.getIdxagentalert(), Collections.emptyList());
            
        } catch (Exception e) {
            log.error("Error processing error for agent {}: {}", agentId, e.getMessage(), e);
            throw new RuntimeException("Failed to process error: " + e.getMessage(), e);
        }
    }

    public TelemetryResponse processHealth(Long agentId, HealthTelemetryRequest request) {
        try {
            // 1. Validar agente existe
            Agent agent = validateAgent(agentId);
            
            // 2. Crear registro de AgentHealth
            AgentHealth health = new AgentHealth();
            health.setFkidxagent(agentId);
            health.setAgthealthstatus(request.getStatus());
            health.setAgthealthscore(request.getHealthScore());
            health.setAgthealthchecks(toJsonString(request.getChecks()));
            health.setAgtuptime(request.getUptime());
            health.setAgtlastrestart(Timestamp.valueOf(request.getLastRestart()));
            health.setAgtmeasuredat(Timestamp.valueOf(LocalDateTime.now()));
            health.setAgtcreatedat(Timestamp.valueOf(LocalDateTime.now()));
            health.setAgtcreatedby("TELEMETRY_SYSTEM");
            
            businessService.save(health);
            
            // 3. Verificar salud y generar alertas
            List<String> alertsTriggered = checkHealthThresholds(agentId, request);
            
            return TelemetryResponse.success(health.getIdxagenthealth(), alertsTriggered);
            
        } catch (Exception e) {
            log.error("Error processing health for agent {}: {}", agentId, e.getMessage(), e);
            throw new RuntimeException("Failed to process health: " + e.getMessage(), e);
        }
    }

    public TelemetryResponse processBatch(Long agentId, BatchTelemetryRequest request) {
        try {
            List<String> allAlerts = new ArrayList<>();
            List<String> processedIds = new ArrayList<>();
            
            for (BatchTelemetryEvent event : request.getEvents()) {
                switch (event.getType()) {
                    case "METRICS":
                        MetricsTelemetryRequest metricsReq = new MetricsTelemetryRequest();
                        metricsReq.setMetrics(event.getData());
                        TelemetryResponse metricsResp = processMetrics(agentId, metricsReq);
                        processedIds.add(metricsResp.getMetricsId());
                        allAlerts.addAll(metricsResp.getAlertsTriggered());
                        break;
                        
                    case "DECISION":
                        DecisionTelemetryRequest decisionReq = new DecisionTelemetryRequest();
                        decisionReq.setDecisionType(event.getData().get("decisionType"));
                        decisionReq.setDecision(event.getData().get("decision"));
                        decisionReq.setConfidence(Double.parseDouble(event.getData().get("confidence")));
                        TelemetryResponse decisionResp = processDecision(agentId, decisionReq);
                        processedIds.add(decisionResp.getMetricsId());
                        break;
                        
                    case "HEALTH":
                        HealthTelemetryRequest healthReq = new HealthTelemetryRequest();
                        healthReq.setStatus(event.getData().get("status"));
                        healthReq.setHealthScore(Double.parseDouble(event.getData().get("healthScore")));
                        TelemetryResponse healthResp = processHealth(agentId, healthReq);
                        processedIds.add(healthResp.getMetricsId());
                        allAlerts.addAll(healthResp.getAlertsTriggered());
                        break;
                        
                    case "ERROR":
                        ErrorTelemetryRequest errorReq = new ErrorTelemetryRequest();
                        errorReq.setErrorType(event.getData().get("errorType"));
                        errorReq.setSeverity(event.getData().get("severity"));
                        errorReq.setMessage(event.getData().get("message"));
                        TelemetryResponse errorResp = processError(agentId, errorReq);
                        processedIds.add(errorResp.getMetricsId());
                        break;
                }
            }
            
            return TelemetryResponse.batchSuccess(processedIds, allAlerts);
            
        } catch (Exception e) {
            log.error("Error processing batch telemetry for agent {}: {}", agentId, e.getMessage(), e);
            throw new RuntimeException("Failed to process batch telemetry: " + e.getMessage(), e);
        }
    }

    private Agent validateAgent(Long agentId) {
        Agent agent = businessService.findById(Agent.class, agentId);
        if (agent == null) {
            throw new IllegalArgumentException("Agent not found: " + agentId);
        }
        return agent;
    }

    private List<String> checkThresholds(Long agentId, MetricsData metrics) {
        List<String> alerts = new ArrayList<>();
        
        if (metrics.getResponseTimeMs() > 500) {
            alerts.add("HIGH_RESPONSE_TIME");
        }
        if (metrics.getErrorRate() > 0.05) {
            alerts.add("HIGH_ERROR_RATE");
        }
        if (metrics.getCpuUsagePercent() > 90) {
            alerts.add("HIGH_CPU_USAGE");
        }
        
        return alerts;
    }

    private List<String> checkHealthThresholds(Long agentId, HealthTelemetryRequest request) {
        List<String> alerts = new ArrayList<>();
        
        if (request.getHealthScore() < 70) {
            alerts.add("LOW_HEALTH_SCORE");
        }
        
        for (HealthCheck check : request.getChecks()) {
            if ("WARN".equals(check.getStatus()) || "FAIL".equals(check.getStatus())) {
                alerts.add("HEALTH_CHECK_" + check.getName().toUpperCase());
            }
        }
        
        return alerts;
    }

    private void updateAgentHealthStatus(Long agentId, MetricsData metrics) {
        try {
            Agent agent = businessService.findById(Agent.class, agentId);
            if (agent != null) {
                // Actualizar campos de salud del agente
                agent.setAgtscore(new BigDecimal(metrics.getAccuracyScore()));
                agent.setAgtupdatedat(Timestamp.valueOf(LocalDateTime.now()));
                agent.setAgtupdatedby("TELEMETRY_SYSTEM");
                businessService.save(agent);
            }
        } catch (Exception e) {
            log.error("Error updating agent health status: {}", e.getMessage(), e);
        }
    }

    private void logActivity(String action, String model, Long pk, String mensaje) {
        try {
            Ssoractividad activityLog = new Ssoractividad();
            activityLog.setUsername("TELEMETRY_SYSTEM");
            activityLog.setAccion(action);
            activityLog.setAlta(new Timestamp(System.currentTimeMillis()));
            activityLog.setModulo(model);
            activityLog.setIdtupla(pk != null ? pk.intValue() : 0);
            activityLog.setAplicacion("CODEFLOWX_GOVERN");
            activityLog.setValuetupla(mensaje);
            businessService.save(activityLog);
        } catch (Exception e) {
            log.error("Error al auditar acción: {} en módulo: {}", action, model, e);
        }
    }

    private String toJsonString(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("Error converting to JSON: {}", e.getMessage());
            return "{}";
        }
    }
}
```

### **3. DTOs de Request/Response**

**Ubicación:** `src/main/java/com/codeflowx/govern/api/dto/`

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricsTelemetryRequest {
    private String timestamp;
    private MetricsData metrics;
    private TelemetryContext context;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricsData {
    private Double responseTimeMs;
    private Double throughputRps;
    private Double errorRate;
    private Double cpuUsagePercent;
    private Double memoryUsagePercent;
    private Double gpuUsagePercent;
    private Double accuracyScore;
    private Double latencyP95;
    private Double latencyP99;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryContext {
    private String environment;
    private String version;
    private String instanceId;
    private String region;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryResponse {
    private String status;
    private String metricsId;
    private String processedAt;
    private List<String> alertsTriggered;
    
    public static TelemetryResponse success(String id, List<String> alerts) {
        return new TelemetryResponse("received", id, 
            LocalDateTime.now().toString(), alerts);
    }
    
    public static TelemetryResponse error(String message) {
        return new TelemetryResponse("error", null, 
            LocalDateTime.now().toString(), Collections.emptyList());
    }
    
    public static TelemetryResponse batchSuccess(List<String> ids, List<String> alerts) {
        return new TelemetryResponse("batch_received", 
            String.join(",", ids), LocalDateTime.now().toString(), alerts);
    }
}
```

---

## 🔐 AUTENTICACIÓN Y AUTORIZACIÓN

### **API Keys:**
```http
X-API-Key: your_api_key
```

### **JWT Tokens:**
```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### **Scopes por Rol:**
| Rol | Scopes |
|-----|--------|
| **Admin** | `agents:read`, `agents:write`, `agents:delete`, `agents:approve`, `agents:deploy` |
| **Gestor** | `agents:read`, `agents:approve`, `agents:deploy` |
| **Científico** | `agents:read`, `agents:write`, `agents:evaluate` |
| **Operativo** | `agents:read`, `agents:monitor` |

---

## 📊 WEBHOOKS

### **Configuración:**
```json
{
  "url": "https://your-app.com/webhooks/agents",
  "events": [
    "agent.approved",
    "agent.deployed",
    "agent.alert.created",
    "agent.health.degraded"
  ],
  "secret": "your_webhook_secret"
}
```

### **Eventos Disponibles:**
| Evento | Descripción | Payload |
|--------|-------------|---------|
| `agent.created` | Agente creado | `{agentId, name, status}` |
| `agent.approved` | Agente aprobado | `{agentId, approvedBy, approvedAt}` |
| `agent.deployed` | Agente desplegado | `{agentId, environment, version}` |
| `agent.alert.created` | Alerta creada | `{agentId, alertType, severity}` |
| `agent.health.degraded` | Salud degradada | `{agentId, healthScore, threshold}` |

---

## 📈 RATE LIMITING

### **Límites por Plan:**
| Plan | Requests/min | Burst | Concurrent |
|------|-------------|-------|------------|
| **Free** | 100 | 200 | 10 |
| **Pro** | 1000 | 2000 | 100 |
| **Enterprise** | 10000 | 20000 | 1000 |

### **Headers de Rate Limiting:**
```http
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 999
X-RateLimit-Reset: 1640995200
```

---

## 🔍 FILTROS Y BÚSQUEDA

### **Filtros Disponibles:**
```json
{
  "status": ["ACTIVE", "DRAFT", "DEPRECATED"],
  "type": ["LLM", "RULE_BASED", "HYBRID"],
  "approvalStatus": ["APPROVED", "PENDING", "REJECTED"],
  "createdAfter": "2025-01-01T00:00:00Z",
  "createdBefore": "2025-12-31T23:59:59Z",
  "healthScoreMin": 80,
  "complianceScoreMin": 90
}
```

### **Búsqueda Full-Text:**
```json
{
  "search": "customer service",
  "searchFields": ["name", "description", "metadata"]
}
```

---

## 📋 CÓDIGOS DE ERROR

| Código | Descripción |
|--------|-------------|
| `400` | Bad Request - Parámetros inválidos |
| `401` | Unauthorized - Token inválido |
| `403` | Forbidden - Sin permisos |
| `404` | Not Found - Recurso no encontrado |
| `409` | Conflict - Estado inconsistente |
| `422` | Unprocessable Entity - Validación fallida |
| `429` | Too Many Requests - Rate limit excedido |
| `500` | Internal Server Error - Error del servidor |

---

## ✅ CONCLUSIÓN

El módulo de agentes expone una **API REST completa** con:
- ✅ **25+ endpoints** para gestión completa
- ✅ **3 SDKs** (Python, JavaScript, Java)
- ✅ **Autenticación JWT** y API Keys
- ✅ **Webhooks** para eventos en tiempo real
- ✅ **Rate limiting** por plan
- ✅ **Filtros avanzados** y búsqueda full-text

**Estado:** Documentación de API y SDK completa ✅  
**Próximo:** Documento de integración de agentes
