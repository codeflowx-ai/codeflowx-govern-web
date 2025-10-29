# 🌐 API Y SDK - MÓDULO MODELOS

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Especificación completa de APIs REST y SDKs para integración con el módulo de modelos

---

## 🎯 RESUMEN EJECUTIVO

El módulo de **modelos** expone **30+ endpoints REST** organizados en **6 categorías funcionales**, con **3 SDKs oficiales** (Python, JavaScript, Java) y **autenticación robusta** para integración segura con sistemas externos.

### **Características de la API:**
- **RESTful design** con OpenAPI 3.0
- **Autenticación JWT** y API Keys
- **Rate limiting** por plan y usuario
- **Webhooks** para eventos en tiempo real
- **Versionado** con compatibilidad hacia atrás

---

## 🔗 ENDPOINTS PRINCIPALES

### **1. Gestión de Modelos**

#### **GET /api/v1/models**
Obtiene lista de modelos con filtros y paginación.

**Parámetros de Query:**
- `page` (int): Número de página (default: 1)
- `size` (int): Tamaño de página (default: 20)
- `type` (string): Filtro por tipo de modelo
- `status` (string): Filtro por estado
- `framework` (string): Filtro por framework
- `search` (string): Búsqueda por nombre/descripción

**Respuesta:**
```json
{
  "content": [
    {
      "idxmodel": 1,
      "mdlname": "Customer Sentiment Classifier",
      "mdltype": "CLASSIFICATION",
      "mdlframework": "TENSORFLOW",
      "mdlversion": "2.1.0",
      "mdlstatus": "PRODUCTION",
      "mdlperformance": "{\"accuracy\": 0.94, \"f1_score\": 0.92}",
      "mdlcompliance": "APPROVED",
      "mdlcreatedat": "2025-10-01T10:00:00Z",
      "mdlcreatedby": "john.doe@company.com"
    }
  ],
  "totalElements": 150,
  "totalPages": 8,
  "size": 20,
  "number": 1
}
```

#### **POST /api/v1/models**
Registra un nuevo modelo.

**Request:**
```json
{
  "mdlname": "New Sentiment Classifier",
  "mdldescription": "Model for customer sentiment analysis",
  "mdltype": "CLASSIFICATION",
  "mdlcategory": "NLP",
  "mdlframework": "TENSORFLOW",
  "mdlarchitecture": "BERT-based",
  "mdlparameters": {
    "learning_rate": 0.001,
    "batch_size": 32,
    "epochs": 10
  },
  "mdlhyperparameters": {
    "optimizer": "Adam",
    "loss_function": "categorical_crossentropy"
  }
}
```

**Respuesta:**
```json
{
  "idxmodel": 123,
  "mdlname": "New Sentiment Classifier",
  "mdlstatus": "DRAFT",
  "mdlversion": "1.0.0",
  "mdlcreatedat": "2025-10-01T12:00:00Z",
  "mdlcreatedby": "john.doe@company.com"
}
```

#### **GET /api/v1/models/{id}**
Obtiene detalles de un modelo específico.

**Respuesta:**
```json
{
  "idxmodel": 123,
  "mdlname": "Customer Sentiment Classifier",
  "mdldescription": "Model for customer sentiment analysis",
  "mdltype": "CLASSIFICATION",
  "mdlcategory": "NLP",
  "mdlframework": "TENSORFLOW",
  "mdlversion": "2.1.0",
  "mdlstatus": "PRODUCTION",
  "mdlarchitecture": "BERT-based",
  "mdlparameters": {
    "learning_rate": 0.001,
    "batch_size": 32,
    "epochs": 10
  },
  "mdlhyperparameters": {
    "optimizer": "Adam",
    "loss_function": "categorical_crossentropy"
  },
  "mdlperformance": {
    "accuracy": 0.94,
    "f1_score": 0.92,
    "precision": 0.93,
    "recall": 0.91
  },
  "mdlcompliance": "APPROVED",
  "mdlbias": "{\"bias_score\": 0.85, \"protected_attributes\": []}",
  "mdltransparency": "HIGH",
  "mdlinterpretability": "MEDIUM",
  "mdlendpoint": "https://api.company.com/models/sentiment/v2",
  "mdlcontainer": "company/sentiment-classifier:2.1.0",
  "mdlresources": {
    "cpu": "2 cores",
    "memory": "4GB",
    "gpu": "1x T4"
  },
  "mdlcreatedat": "2025-10-01T10:00:00Z",
  "mdlcreatedby": "john.doe@company.com",
  "mdlupdatedat": "2025-10-01T15:00:00Z",
  "mdlupdatedby": "john.doe@company.com",
  "mdlapprovedat": "2025-10-01T14:30:00Z",
  "mdlapprovedby": "jane.smith@company.com"
}
```

#### **PUT /api/v1/models/{id}**
Actualiza un modelo existente.

#### **DELETE /api/v1/models/{id}**
Elimina un modelo (soft delete).

---

### **2. Gestión de Versiones**

#### **GET /api/v1/models/{id}/versions**
Obtiene todas las versiones de un modelo.

**Respuesta:**
```json
{
  "content": [
    {
      "idxmodelversion": 1,
      "mdlversion": "2.1.0",
      "mdlversiondescription": "Improved accuracy with new training data",
      "mdlversionstatus": "PRODUCTION",
      "mdlversionmetrics": "{\"accuracy\": 0.94, \"f1_score\": 0.92}",
      "mdlversioncreatedat": "2025-10-01T15:00:00Z",
      "mdlversioncreatedby": "john.doe@company.com"
    },
    {
      "idxmodelversion": 2,
      "mdlversion": "2.0.0",
      "mdlversiondescription": "Initial production version",
      "mdlversionstatus": "DEPRECATED",
      "mdlversionmetrics": "{\"accuracy\": 0.91, \"f1_score\": 0.89}",
      "mdlversioncreatedat": "2025-10-01T10:00:00Z",
      "mdlversioncreatedby": "john.doe@company.com"
    }
  ],
  "totalElements": 2,
  "totalPages": 1
}
```

#### **POST /api/v1/models/{id}/versions**
Crea una nueva versión del modelo.

**Request:**
```json
{
  "mdlversion": "2.2.0",
  "mdlversiondescription": "Added support for new languages",
  "mdlversionfile": "sentiment_classifier_v2_2_0.pkl",
  "mdlversionchecksum": "sha256:abc123...",
  "mdlversionpath": "/models/sentiment/v2.2.0/"
}
```

#### **GET /api/v1/models/{id}/versions/{versionId}**
Obtiene detalles de una versión específica.

#### **POST /api/v1/models/{id}/versions/{versionId}/promote**
Promueve una versión a producción.

---

### **3. Validaciones**

#### **GET /api/v1/models/{id}/validations**
Obtiene todas las validaciones de un modelo.

**Respuesta:**
```json
{
  "content": [
    {
      "idxmodelvalidation": 1,
      "mdlvalidationtype": "PERFORMANCE",
      "mdlvalidationstatus": "COMPLETED",
      "mdlvalidationresult": "PASS",
      "mdlvalidationscore": 94.5,
      "mdlvalidationmetrics": "{\"accuracy\": 0.94, \"latency\": 120}",
      "mdlvalidationcreatedat": "2025-10-01T16:00:00Z",
      "mdlvalidationcreatedby": "system"
    },
    {
      "idxmodelvalidation": 2,
      "mdlvalidationtype": "BIAS",
      "mdlvalidationstatus": "COMPLETED",
      "mdlvalidationresult": "PASS",
      "mdlvalidationscore": 85.0,
      "mdlvalidationmetrics": "{\"bias_score\": 0.85, \"protected_attributes\": []}",
      "mdlvalidationcreatedat": "2025-10-01T16:05:00Z",
      "mdlvalidationcreatedby": "system"
    }
  ]
}
```

#### **POST /api/v1/models/{id}/validations**
Ejecuta una nueva validación.

**Request:**
```json
{
  "mdlvalidationtype": "PERFORMANCE",
  "mdlvalidationcriteria": {
    "min_accuracy": 0.90,
    "max_latency": 200,
    "test_dataset": "validation_set_v2"
  }
}
```

#### **GET /api/v1/models/{id}/validations/{validationId}**
Obtiene detalles de una validación específica.

---

### **4. Aprobaciones**

#### **GET /api/v1/models/{id}/approvals**
Obtiene todas las aprobaciones de un modelo.

#### **POST /api/v1/models/{id}/approvals**
Solicita aprobación de un modelo.

**Request:**
```json
{
  "approvalType": "NEW_MODEL",
  "targetEnvironment": "PRODUCTION",
  "businessJustification": "Improve customer service automation"
}
```

#### **GET /api/v1/models/{id}/approvals/{approvalId}**
Obtiene detalles de una aprobación específica.

#### **PUT /api/v1/models/{id}/approvals/{approvalId}**
Actualiza el estado de una aprobación.

**Request:**
```json
{
  "governanceApproval": "APPROVED",
  "riskAssessment": "LOW",
  "governanceNotes": "Model meets all compliance requirements"
}
```

---

### **5. Uso y Métricas**

#### **GET /api/v1/models/{id}/usage**
Obtiene estadísticas de uso del modelo.

**Respuesta:**
```json
{
  "idxmodel": 123,
  "mdlname": "Customer Sentiment Classifier",
  "usage": {
    "total_requests": 1250,
    "successful_requests": 1187,
    "failed_requests": 63,
    "avg_latency": 120,
    "max_latency": 450,
    "min_latency": 45,
    "total_cost": 12.50,
    "avg_confidence": 0.92,
    "unique_users": 25
  },
  "period": {
    "start": "2025-10-01T00:00:00Z",
    "end": "2025-10-31T23:59:59Z"
  }
}
```

#### **GET /api/v1/models/{id}/metrics**
Obtiene métricas de rendimiento del modelo.

**Respuesta:**
```json
{
  "idxmodel": 123,
  "mdlname": "Customer Sentiment Classifier",
  "metrics": {
    "accuracy": 0.94,
    "f1_score": 0.92,
    "precision": 0.93,
    "recall": 0.91,
    "latency_p95": 180,
    "latency_p99": 250,
    "throughput": 100,
    "error_rate": 0.05
  },
  "trends": {
    "accuracy_trend": "STABLE",
    "latency_trend": "IMPROVING",
    "usage_trend": "INCREASING"
  }
}
```

#### **GET /api/v1/models/{id}/drift**
Obtiene análisis de drift del modelo.

**Respuesta:**
```json
{
  "idxmodel": 123,
  "drift_detected": false,
  "drift_score": 0.08,
  "baseline_metrics": {
    "avg_confidence": 0.92,
    "avg_latency": 115,
    "success_rate": 0.95
  },
  "current_metrics": {
    "avg_confidence": 0.91,
    "avg_latency": 120,
    "success_rate": 0.94
  },
  "timestamp": "2025-10-01T16:00:00Z"
}
```

---

### **6. Reentrenamiento**

#### **POST /api/v1/models/{id}/retrain**
Inicia proceso de reentrenamiento.

**Request:**
```json
{
  "retrainReason": "Performance degradation detected",
  "newDataset": "dataset_v3",
  "hyperparameters": {
    "learning_rate": 0.0005,
    "batch_size": 64
  },
  "strategy": "INCREMENTAL"
}
```

#### **GET /api/v1/models/{id}/retrain/{retrainId}**
Obtiene estado del reentrenamiento.

#### **POST /api/v1/models/{id}/retrain/{retrainId}/abort**
Cancela proceso de reentrenamiento.

---

## 🔧 SDKs OFICIALES

### **1. Python SDK**

#### **Instalación:**
```bash
pip install codeflowx-models-sdk
```

#### **Uso Básico:**
```python
from codeflowx_models import ModelClient

# Configuración
client = ModelClient(
    api_key="your_api_key",
    base_url="https://api.codeflowx.com"
)

# Registrar modelo
model = client.models.create({
    "name": "Customer Sentiment Classifier",
    "type": "CLASSIFICATION",
    "framework": "TENSORFLOW",
    "architecture": "BERT-based",
    "parameters": {
        "learning_rate": 0.001,
        "batch_size": 32
    }
})

# Obtener modelo
model_details = client.models.get(model.id)

# Crear versión
version = client.models.versions.create(model.id, {
    "version": "2.2.0",
    "description": "Added support for new languages",
    "file": "sentiment_classifier_v2_2_0.pkl"
})

# Ejecutar validación
validation = client.models.validations.create(model.id, {
    "type": "PERFORMANCE",
    "criteria": {
        "min_accuracy": 0.90,
        "max_latency": 200
    }
})

# Obtener métricas
metrics = client.models.metrics.get(model.id, {
    "period": "30d"
})

# Solicitar aprobación
approval = client.models.approvals.create(model.id, {
    "type": "NEW_MODEL",
    "targetEnvironment": "PRODUCTION"
})
```

#### **Métodos Avanzados:**
```python
# Batch operations
models = client.models.batch_create([
    {"name": "Model 1", "type": "CLASSIFICATION"},
    {"name": "Model 2", "type": "REGRESSION"}
])

# Análisis de drift
drift_analysis = client.models.drift.analyze(model.id)

# Reentrenamiento
retrain = client.models.retrain.start(model.id, {
    "reason": "Performance degradation",
    "strategy": "INCREMENTAL"
})

# Comparación de modelos
comparison = client.models.compare(model1.id, model2.id)

# A/B testing
ab_test = client.models.ab_test.create(model.id, {
    "baseline_version": "2.1.0",
    "test_version": "2.2.0",
    "traffic_split": 0.1
})
```

### **2. JavaScript SDK**

#### **Instalación:**
```bash
npm install @codeflowx/models-sdk
```

#### **Uso Básico:**
```javascript
import { ModelClient } from '@codeflowx/models-sdk';

// Configuración
const client = new ModelClient({
  apiKey: 'your_api_key',
  baseUrl: 'https://api.codeflowx.com'
});

// Registrar modelo
const model = await client.models.create({
  name: 'Customer Sentiment Classifier',
  type: 'CLASSIFICATION',
  framework: 'TENSORFLOW',
  architecture: 'BERT-based',
  parameters: {
    learningRate: 0.001,
    batchSize: 32
  }
});

// Obtener modelo
const modelDetails = await client.models.get(model.id);

// Crear versión
const version = await client.models.versions.create(model.id, {
  version: '2.2.0',
  description: 'Added support for new languages',
  file: 'sentiment_classifier_v2_2_0.pkl'
});

// Ejecutar validación
const validation = await client.models.validations.create(model.id, {
  type: 'PERFORMANCE',
  criteria: {
    minAccuracy: 0.90,
    maxLatency: 200
  }
});

// Obtener métricas
const metrics = await client.models.metrics.get(model.id, {
  period: '30d'
});

// Solicitar aprobación
const approval = await client.models.approvals.create(model.id, {
  type: 'NEW_MODEL',
  targetEnvironment: 'PRODUCTION'
});
```

#### **Métodos Avanzados:**
```javascript
// Batch operations
const models = await client.models.batchCreate([
  { name: 'Model 1', type: 'CLASSIFICATION' },
  { name: 'Model 2', type: 'REGRESSION' }
]);

// Análisis de drift
const driftAnalysis = await client.models.drift.analyze(model.id);

// Reentrenamiento
const retrain = await client.models.retrain.start(model.id, {
  reason: 'Performance degradation',
  strategy: 'INCREMENTAL'
});

// Comparación de modelos
const comparison = await client.models.compare(model1.id, model2.id);

// A/B testing
const abTest = await client.models.abTest.create(model.id, {
  baselineVersion: '2.1.0',
  testVersion: '2.2.0',
  trafficSplit: 0.1
});
```

### **3. Java SDK**

#### **Maven Dependency:**
```xml
<dependency>
    <groupId>com.codeflowx</groupId>
    <artifactId>models-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### **Uso Básico:**
```java
import com.codeflowx.models.ModelClient;
import com.codeflowx.models.model.*;

// Configuración
ModelClient client = ModelClient.builder()
    .apiKey("your_api_key")
    .baseUrl("https://api.codeflowx.com")
    .build();

// Registrar modelo
ModelRequest request = ModelRequest.builder()
    .name("Customer Sentiment Classifier")
    .type("CLASSIFICATION")
    .framework("TENSORFLOW")
    .architecture("BERT-based")
    .parameters(Map.of(
        "learning_rate", 0.001,
        "batch_size", 32
    ))
    .build();

Model model = client.models().create(request);

// Obtener modelo
Model modelDetails = client.models().get(model.getId());

// Crear versión
VersionRequest versionRequest = VersionRequest.builder()
    .version("2.2.0")
    .description("Added support for new languages")
    .file("sentiment_classifier_v2_2_0.pkl")
    .build();

ModelVersion version = client.models().versions().create(model.getId(), versionRequest);

// Ejecutar validación
ValidationRequest validationRequest = ValidationRequest.builder()
    .type("PERFORMANCE")
    .criteria(Map.of(
        "min_accuracy", 0.90,
        "max_latency", 200
    ))
    .build();

ModelValidation validation = client.models().validations().create(model.getId(), validationRequest);

// Obtener métricas
Metrics metrics = client.models().metrics().get(model.getId(), 
    MetricsRequest.builder()
        .period("30d")
        .build()
);

// Solicitar aprobación
ApprovalRequest approvalRequest = ApprovalRequest.builder()
    .type("NEW_MODEL")
    .targetEnvironment("PRODUCTION")
    .build();

ModelApproval approval = client.models().approvals().create(model.getId(), approvalRequest);
```

#### **Métodos Avanzados:**
```java
// Batch operations
List<ModelRequest> requests = Arrays.asList(
    ModelRequest.builder().name("Model 1").type("CLASSIFICATION").build(),
    ModelRequest.builder().name("Model 2").type("REGRESSION").build()
);
List<Model> models = client.models().batchCreate(requests);

// Análisis de drift
DriftAnalysis driftAnalysis = client.models().drift().analyze(model.getId());

// Reentrenamiento
RetrainRequest retrainRequest = RetrainRequest.builder()
    .reason("Performance degradation")
    .strategy("INCREMENTAL")
    .build();
RetrainResult retrain = client.models().retrain().start(model.getId(), retrainRequest);

// Comparación de modelos
ComparisonResult comparison = client.models().compare(model1.getId(), model2.getId());

// A/B testing
ABTestRequest abTestRequest = ABTestRequest.builder()
    .baselineVersion("2.1.0")
    .testVersion("2.2.0")
    .trafficSplit(0.1)
    .build();
ABTest abTest = client.models().abTest().create(model.getId(), abTestRequest);
```

---

## 🔐 AUTENTICACIÓN Y AUTORIZACIÓN

### **API Keys:**
```http
X-API-Key: your_api_key
```

### **JWT Tokens:**
```http
Authorization: Bearer your_jwt_token
```

### **OAuth2:**
```http
Authorization: Bearer your_oauth_token
```

### **Roles y Permisos:**
- **MODEL_ADMIN:** Acceso completo a todas las operaciones
- **MODEL_MANAGER:** Gestión de modelos y aprobaciones
- **MODEL_ENGINEER:** Creación y edición de modelos
- **MODEL_USER:** Solo uso de modelos
- **MODEL_AUDITOR:** Solo lectura para auditoría

---

## 📊 RATE LIMITING

### **Límites por Plan:**
- **Free:** 100 requests/hour
- **Professional:** 1,000 requests/hour
- **Enterprise:** 10,000 requests/hour

### **Headers de Rate Limiting:**
```http
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 999
X-RateLimit-Reset: 1640995200
```

---

## 🔔 WEBHOOKS

### **Eventos Disponibles:**
- `model.created` - Modelo creado
- `model.updated` - Modelo actualizado
- `model.approved` - Modelo aprobado
- `model.rejected` - Modelo rechazado
- `model.version.created` - Nueva versión creada
- `model.validation.completed` - Validación completada
- `model.drift.detected` - Drift detectado
- `model.retrain.completed` - Reentrenamiento completado

### **Configuración de Webhook:**
```json
{
  "url": "https://your-app.com/webhooks/models",
  "events": ["model.created", "model.approved"],
  "secret": "your_webhook_secret"
}
```

### **Payload de Webhook:**
```json
{
  "event": "model.approved",
  "timestamp": "2025-10-01T16:00:00Z",
  "data": {
    "idxmodel": 123,
    "mdlname": "Customer Sentiment Classifier",
    "mdlstatus": "APPROVED",
    "mdlapprovedby": "jane.smith@company.com"
  }
}
```

---

## ❌ CÓDIGOS DE ERROR

### **Códigos HTTP:**
- **200:** OK
- **201:** Created
- **400:** Bad Request
- **401:** Unauthorized
- **403:** Forbidden
- **404:** Not Found
- **429:** Too Many Requests
- **500:** Internal Server Error

### **Códigos de Error Específicos:**
- **MODEL_001:** Modelo no encontrado
- **MODEL_002:** Modelo ya existe
- **MODEL_003:** Versión inválida
- **MODEL_004:** Validación fallida
- **MODEL_005:** Aprobación denegada
- **MODEL_006:** Versión no encontrada
- **MODEL_007:** Reentrenamiento fallido

### **Respuesta de Error:**
```json
{
  "error": {
    "code": "MODEL_001",
    "message": "Model not found",
    "details": "Model with ID 123 does not exist",
    "timestamp": "2025-10-01T16:00:00Z",
    "request_id": "req_123456"
  }
}
```

---

## 📈 FILTROS Y BÚSQUEDA

### **Filtros Disponibles:**
- `type` - Tipo de modelo
- `status` - Estado del modelo
- `framework` - Framework utilizado
- `created_by` - Usuario creador
- `created_after` - Fecha de creación desde
- `created_before` - Fecha de creación hasta
- `compliance_status` - Estado de compliance

### **Búsqueda de Texto:**
- `search` - Búsqueda en nombre y descripción
- `architecture_search` - Búsqueda en arquitectura
- `parameters_search` - Búsqueda en parámetros

### **Ordenamiento:**
- `sort` - Campo de ordenamiento
- `order` - Dirección (asc/desc)

### **Ejemplo de Filtros:**
```http
GET /api/v1/models?type=CLASSIFICATION&status=PRODUCTION&framework=TENSORFLOW&search=sentiment&sort=mdlcreatedat&order=desc&page=1&size=20
```

---

## ✅ CONCLUSIÓN

La **API y SDKs del módulo modelos** proporcionan una **integración completa** y **robusta** para el gobierno de modelos de IA con:

- 🌐 **30+ endpoints REST** organizados por funcionalidad
- 🔧 **3 SDKs oficiales** para Python, JavaScript y Java
- 🔐 **Autenticación robusta** con múltiples métodos
- 📊 **Rate limiting** y **webhooks** para escalabilidad
- ❌ **Manejo completo** de errores y excepciones
- 🔍 **Filtros avanzados** y **búsqueda** de texto
- 📈 **Métricas y analytics** integrados

**Esta API está diseñada** para soportar integraciones complejas y alto volumen de operaciones con modelos de IA.

