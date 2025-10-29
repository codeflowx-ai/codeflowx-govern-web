# 🚀 SERVING - API & SDK

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación de API y SDK del módulo Serving

---

## 🎯 RESUMEN EJECUTIVO

El módulo **Serving** proporciona APIs RESTful completas para:
- **Deployments:** Gestión de deployments de modelos
- **Endpoints:** Configuración de endpoints
- **Predictions:** Gestión de predicciones
- **Health:** Monitoreo de salud
- **SLA:** Configuración de SLA

**Total de Endpoints:** ~40  
**Formato:** REST JSON  
**Autenticación:** JWT Bearer Token

---

## 🌐 ENDPOINTS PRINCIPALES

### **1. DEPLOYMENT API**

#### **GET /api/serving/deployments**
Obtener listado de deployments

**Request:**
```bash
GET /api/serving/deployments?page=0&size=20&sort=deploymentDate,desc
Authorization: Bearer {token}
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "modelId": 101,
      "modelName": "Fraud Detection Model",
      "modelVersion": "v2.1.0",
      "deploymentType": "REALTIME",
      "status": "ACTIVE",
      "replicas": 3,
      "cpuUsage": 45.5,
      "memoryUsage": 62.3,
      "requestsPerSecond": 150.2,
      "avgLatency": 45.8,
      "deploymentDate": "2025-10-15T10:30:00Z",
      "endpointUrl": "https://api.codeflowx.io/models/fraud-detection/predict"
    }
  ],
  "totalElements": 45,
  "totalPages": 3,
  "size": 20,
  "number": 0
}
```

#### **POST /api/serving/deployments**
Crear nuevo deployment

**Request:**
```bash
POST /api/serving/deployments
Authorization: Bearer {token}
Content-Type: application/json

{
  "modelId": 101,
  "modelVersion": "v2.1.0",
  "deploymentType": "REALTIME",
  "replicas": 3,
  "cpuLimit": "2000m",
  "memoryLimit": "4Gi",
  "autoScaling": true,
  "minReplicas": 2,
  "maxReplicas": 10,
  "targetCPUUtilization": 70
}
```

**Response:**
```json
{
  "id": 1,
  "modelId": 101,
  "deploymentType": "REALTIME",
  "status": "DEPLOYING",
  "replicas": 3,
  "deploymentDate": "2025-10-15T10:30:00Z",
  "message": "Deployment initiated successfully"
}
```

#### **PUT /api/serving/deployments/{id}**
Actualizar deployment existente

**Request:**
```bash
PUT /api/serving/deployments/1
Authorization: Bearer {token}
Content-Type: application/json

{
  "replicas": 5,
  "cpuLimit": "3000m",
  "memoryLimit": "6Gi",
  "maxReplicas": 15
}
```

#### **DELETE /api/serving/deployments/{id}**
Eliminar deployment

**Request:**
```bash
DELETE /api/serving/deployments/1
Authorization: Bearer {token}
```

---

### **2. ENDPOINTS API**

#### **GET /api/serving/endpoints**
Obtener listado de endpoints

**Request:**
```bash
GET /api/serving/endpoints?deploymentId=1
Authorization: Bearer {token}
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "deploymentId": 1,
      "endpointType": "REST",
      "url": "https://api.codeflowx.io/models/fraud-detection/predict",
      "method": "POST",
      "authentication": "JWT",
      "rateLimitPerSecond": 100,
      "timeoutMs": 5000,
      "status": "ACTIVE",
      "requestCount": 125000,
      "avgResponseTime": 45.8,
      "creationDate": "2025-10-15T10:35:00Z"
    }
  ],
  "totalElements": 8
}
```

#### **POST /api/serving/endpoints**
Crear nuevo endpoint

**Request:**
```bash
POST /api/serving/endpoints
Authorization: Bearer {token}
Content-Type: application/json

{
  "deploymentId": 1,
  "endpointType": "REST",
  "method": "POST",
  "authentication": "JWT",
  "rateLimitPerSecond": 100,
  "timeoutMs": 5000,
  "retryPolicy": {
    "maxRetries": 3,
    "retryDelayMs": 1000
  }
}
```

---

### **3. PREDICTIONS API**

#### **POST /api/serving/predictions/realtime**
Realizar predicción en tiempo real

**Request:**
```bash
POST /api/serving/predictions/realtime
Authorization: Bearer {token}
Content-Type: application/json

{
  "deploymentId": 1,
  "inputs": {
    "transaction_amount": 1500.00,
    "merchant_category": "electronics",
    "user_id": "user_12345",
    "timestamp": "2025-10-15T14:30:00Z"
  },
  "metadata": {
    "requestId": "req_789xyz",
    "source": "mobile_app"
  }
}
```

**Response:**
```json
{
  "predictionId": "pred_456abc",
  "deploymentId": 1,
  "modelVersion": "v2.1.0",
  "predictions": {
    "is_fraud": false,
    "fraud_probability": 0.12,
    "risk_score": 2.5,
    "confidence": 0.95
  },
  "latencyMs": 42.5,
  "timestamp": "2025-10-15T14:30:00.150Z",
  "metadata": {
    "requestId": "req_789xyz",
    "modelId": 101
  }
}
```

#### **POST /api/serving/predictions/batch**
Realizar predicción en lote

**Request:**
```bash
POST /api/serving/predictions/batch
Authorization: Bearer {token}
Content-Type: application/json

{
  "deploymentId": 1,
  "batchSize": 1000,
  "inputDataUrl": "s3://bucket/batch-input.parquet",
  "outputDataUrl": "s3://bucket/batch-output.parquet",
  "notificationEmail": "team@company.com"
}
```

**Response:**
```json
{
  "batchJobId": "batch_123xyz",
  "deploymentId": 1,
  "status": "QUEUED",
  "estimatedCompletionTime": "2025-10-15T16:00:00Z",
  "creationDate": "2025-10-15T14:30:00Z"
}
```

#### **GET /api/serving/predictions/history**
Obtener historial de predicciones

**Request:**
```bash
GET /api/serving/predictions/history?deploymentId=1&page=0&size=50
Authorization: Bearer {token}
```

---

### **4. HEALTH API**

#### **GET /api/serving/health/status**
Obtener estado de salud de deployments

**Request:**
```bash
GET /api/serving/health/status?deploymentId=1
Authorization: Bearer {token}
```

**Response:**
```json
{
  "deploymentId": 1,
  "status": "HEALTHY",
  "replicas": {
    "desired": 3,
    "available": 3,
    "unavailable": 0
  },
  "health": {
    "cpuUsage": 45.5,
    "memoryUsage": 62.3,
    "diskUsage": 35.2,
    "networkLatency": 12.5
  },
  "metrics": {
    "requestsPerSecond": 150.2,
    "avgLatency": 45.8,
    "errorRate": 0.002,
    "p95Latency": 78.5,
    "p99Latency": 125.3
  },
  "lastHealthCheck": "2025-10-15T14:30:00Z"
}
```

#### **GET /api/serving/health/metrics**
Obtener métricas detalladas

**Request:**
```bash
GET /api/serving/health/metrics?deploymentId=1&timeRange=1h
Authorization: Bearer {token}
```

---

### **5. SLA API**

#### **GET /api/serving/sla/configuration**
Obtener configuración de SLA

**Request:**
```bash
GET /api/serving/sla/configuration?deploymentId=1
Authorization: Bearer {token}
```

**Response:**
```json
{
  "deploymentId": 1,
  "sla": {
    "maxResponseTimeMs": 100,
    "minAvailability": 99.9,
    "maxErrorRate": 0.01,
    "alertThresholds": {
      "responseTime": 80,
      "availability": 99.0,
      "errorRate": 0.005
    }
  },
  "currentCompliance": {
    "avgResponseTime": 45.8,
    "availability": 99.95,
    "errorRate": 0.002,
    "status": "COMPLIANT"
  }
}
```

#### **POST /api/serving/sla/configuration**
Configurar SLA

**Request:**
```bash
POST /api/serving/sla/configuration
Authorization: Bearer {token}
Content-Type: application/json

{
  "deploymentId": 1,
  "maxResponseTimeMs": 100,
  "minAvailability": 99.9,
  "maxErrorRate": 0.01,
  "alertThresholds": {
    "responseTime": 80,
    "availability": 99.0,
    "errorRate": 0.005
  }
}
```

---

## 📦 SDK CLIENT

### **Java Client Example**

```java
// Configuración del cliente
CodeflowxServingClient client = CodeflowxServingClient.builder()
    .baseUrl("https://api.codeflowx.io")
    .apiKey("your-api-key")
    .build();

// Crear deployment
DeploymentRequest request = DeploymentRequest.builder()
    .modelId(101L)
    .modelVersion("v2.1.0")
    .deploymentType(DeploymentType.REALTIME)
    .replicas(3)
    .autoScaling(true)
    .build();

DeploymentResponse deployment = client.deployments().create(request);

// Realizar predicción
PredictionRequest predictionReq = PredictionRequest.builder()
    .deploymentId(deployment.getId())
    .inputs(Map.of(
        "transaction_amount", 1500.00,
        "merchant_category", "electronics"
    ))
    .build();

PredictionResponse prediction = client.predictions().realtime(predictionReq);
System.out.println("Fraud Probability: " + prediction.getPredictions().get("fraud_probability"));

// Monitorear salud
HealthStatus health = client.health().getStatus(deployment.getId());
System.out.println("Status: " + health.getStatus());
System.out.println("Avg Latency: " + health.getMetrics().getAvgLatency() + "ms");
```

### **Python Client Example**

```python
from codeflowx_serving import ServingClient

# Configuración del cliente
client = ServingClient(
    base_url="https://api.codeflowx.io",
    api_key="your-api-key"
)

# Crear deployment
deployment = client.deployments.create(
    model_id=101,
    model_version="v2.1.0",
    deployment_type="REALTIME",
    replicas=3,
    auto_scaling=True
)

# Realizar predicción
prediction = client.predictions.realtime(
    deployment_id=deployment.id,
    inputs={
        "transaction_amount": 1500.00,
        "merchant_category": "electronics"
    }
)

print(f"Fraud Probability: {prediction.predictions['fraud_probability']}")

# Monitorear salud
health = client.health.get_status(deployment.id)
print(f"Status: {health.status}")
print(f"Avg Latency: {health.metrics.avg_latency}ms")
```

---

## 🔐 AUTENTICACIÓN

### **JWT Token Authentication**

```bash
# Obtener token
POST /api/auth/login
Content-Type: application/json

{
  "username": "user@company.com",
  "password": "secure_password"
}

# Response
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "expiresIn": 3600
}

# Usar token en requests
GET /api/serving/deployments
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```

---

## 📊 RATE LIMITING

### **Límites por Endpoint:**
- **GET /deployments:** 100 req/min
- **POST /deployments:** 20 req/min
- **POST /predictions/realtime:** 1000 req/min
- **POST /predictions/batch:** 10 req/min
- **GET /health:** 200 req/min

### **Response Headers:**
```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 95
X-RateLimit-Reset: 1634567890
```

---

## 🎯 ERROR HANDLING

### **Error Response Format:**

```json
{
  "error": {
    "code": "DEPLOYMENT_FAILED",
    "message": "Insufficient resources for deployment",
    "details": {
      "requiredCPU": "4000m",
      "availableCPU": "2000m"
    },
    "timestamp": "2025-10-15T14:30:00Z",
    "requestId": "req_789xyz"
  }
}
```

### **Common Error Codes:**
- `DEPLOYMENT_FAILED` - Error al crear deployment
- `MODEL_NOT_FOUND` - Modelo no encontrado
- `INSUFFICIENT_RESOURCES` - Recursos insuficientes
- `ENDPOINT_UNAVAILABLE` - Endpoint no disponible
- `PREDICTION_ERROR` - Error en predicción
- `SLA_VIOLATION` - Violación de SLA

---

## 🎯 CONCLUSIÓN

El módulo **Serving** proporciona APIs completas para:

- ✅ **Gestión de Deployments** - CRUD completo
- ✅ **Configuración de Endpoints** - REST, gRPC, WebSocket
- ✅ **Predicciones** - Realtime y Batch
- ✅ **Monitoreo de Salud** - Métricas en tiempo real
- ✅ **Configuración de SLA** - Compliance y alertas
- ✅ **SDKs** - Java y Python
- ✅ **Autenticación** - JWT seguro
- ✅ **Rate Limiting** - Control de tráfico

**Total de ~40 endpoints REST completamente documentados y funcionales.**
