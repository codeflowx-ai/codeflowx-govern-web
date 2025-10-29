# 🌐 API Y SDK - MÓDULO RAG

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Especificación completa de APIs REST y SDKs para integración con el módulo RAG

---

## 🎯 RESUMEN EJECUTIVO

El módulo de **RAG** expone **25+ endpoints REST** organizados en **5 categorías funcionales**, con **3 SDKs oficiales** (Python, JavaScript, Java) y **autenticación robusta** para integración segura con sistemas externos.

---

## 🔗 ENDPOINTS PRINCIPALES

### **1. Gestión de Sistemas RAG**

#### **GET /api/v1/rag/systems**
Obtiene lista de sistemas RAG con filtros y paginación.

**Respuesta:**
```json
{
  "content": [
    {
      "idxragsystem": 1,
      "rgsname": "Customer Support RAG",
      "rgstype": "CONVERSATIONAL",
      "rgsstatus": "PRODUCTION",
      "rgsconfiguration": "{\"embeddings\": \"openai\", \"retrieval\": \"vector\"}",
      "rgsmetrics": "{\"accuracy\": 0.92, \"latency\": 120}",
      "rgscreatedat": "2025-10-01T10:00:00Z"
    }
  ],
  "totalElements": 25,
  "totalPages": 3
}
```

#### **POST /api/v1/rag/systems**
Crea un nuevo sistema RAG.

**Request:**
```json
{
  "rgsname": "Document Search RAG",
  "rgsdescription": "RAG system for document search",
  "rgstype": "SEARCH",
  "rgsconfiguration": {
    "embeddings": "sentence-transformers",
    "retrieval": "dense",
    "generation": "gpt-3.5-turbo"
  }
}
```

### **2. Fuentes de Datos**

#### **GET /api/v1/rag/systems/{id}/datasources**
Obtiene fuentes de datos de un sistema RAG.

#### **POST /api/v1/rag/systems/{id}/datasources**
Añade nueva fuente de datos.

**Request:**
```json
{
  "rgdname": "Knowledge Base",
  "rgdtype": "DATABASE",
  "rgdconnection": "postgresql://localhost:5432/knowledge",
  "rgdconfiguration": {
    "tables": ["documents", "articles"],
    "indexing": "automatic"
  }
}
```

### **3. Evaluación y Calidad**

#### **POST /api/v1/rag/systems/{id}/evaluate**
Ejecuta evaluación de sistema RAG.

**Request:**
```json
{
  "rgetype": "QUALITY",
  "rgecriteria": {
    "min_accuracy": 0.8,
    "max_latency": 200,
    "test_dataset": "validation_set"
  }
}
```

#### **GET /api/v1/rag/systems/{id}/evaluations**
Obtiene evaluaciones de un sistema.

### **4. Métricas y Rendimiento**

#### **GET /api/v1/rag/systems/{id}/metrics**
Obtiene métricas de rendimiento.

**Respuesta:**
```json
{
  "idxragsystem": 1,
  "metrics": {
    "accuracy": 0.92,
    "latency": 120,
    "throughput": 150,
    "coverage": 0.85
  },
  "period": {
    "start": "2025-10-01T00:00:00Z",
    "end": "2025-10-31T23:59:59Z"
  }
}
```

#### **GET /api/v1/rag/systems/{id}/usage**
Obtiene estadísticas de uso.

### **5. Versionado**

#### **GET /api/v1/rag/systems/{id}/versions**
Obtiene versiones de un sistema RAG.

#### **POST /api/v1/rag/systems/{id}/versions**
Crea nueva versión.

---

## 🔧 SDKs OFICIALES

### **Python SDK**
```python
from codeflowx_rag import RagClient

client = RagClient(api_key="your_api_key")

# Crear sistema RAG
system = client.systems.create({
    "name": "Document Search RAG",
    "type": "SEARCH",
    "configuration": {
        "embeddings": "sentence-transformers",
        "retrieval": "dense"
    }
})

# Añadir fuente de datos
datasource = client.systems.datasources.create(system.id, {
    "name": "Knowledge Base",
    "type": "DATABASE",
    "connection": "postgresql://localhost:5432/knowledge"
})

# Evaluar sistema
evaluation = client.systems.evaluate(system.id, {
    "type": "QUALITY",
    "criteria": {"min_accuracy": 0.8}
})

# Obtener métricas
metrics = client.systems.metrics.get(system.id)
```

### **JavaScript SDK**
```javascript
import { RagClient } from '@codeflowx/rag-sdk';

const client = new RagClient({
  apiKey: 'your_api_key'
});

// Crear sistema RAG
const system = await client.systems.create({
  name: 'Document Search RAG',
  type: 'SEARCH',
  configuration: {
    embeddings: 'sentence-transformers',
    retrieval: 'dense'
  }
});

// Evaluar sistema
const evaluation = await client.systems.evaluate(system.id, {
  type: 'QUALITY',
  criteria: { minAccuracy: 0.8 }
});
```

### **Java SDK**
```java
import com.codeflowx.rag.RagClient;

RagClient client = RagClient.builder()
    .apiKey("your_api_key")
    .build();

// Crear sistema RAG
RagSystemRequest request = RagSystemRequest.builder()
    .name("Document Search RAG")
    .type("SEARCH")
    .configuration(Map.of(
        "embeddings", "sentence-transformers",
        "retrieval", "dense"
    ))
    .build();

RagSystem system = client.systems().create(request);

// Evaluar sistema
EvaluationRequest evalRequest = EvaluationRequest.builder()
    .type("QUALITY")
    .criteria(Map.of("min_accuracy", 0.8))
    .build();

RagEvaluation evaluation = client.systems().evaluate(system.getId(), evalRequest);
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

### **Roles y Permisos:**
- **RAG_ADMIN:** Acceso completo a todas las operaciones
- **RAG_MANAGER:** Gestión de sistemas y evaluaciones
- **RAG_ENGINEER:** Creación y edición de sistemas
- **RAG_USER:** Solo uso de sistemas RAG
- **RAG_AUDITOR:** Solo lectura para auditoría

---

## 📊 RATE LIMITING

### **Límites por Plan:**
- **Free:** 100 requests/hour
- **Professional:** 1,000 requests/hour
- **Enterprise:** 10,000 requests/hour

---

## 🔔 WEBHOOKS

### **Eventos Disponibles:**
- `rag.system.created` - Sistema RAG creado
- `rag.system.updated` - Sistema RAG actualizado
- `rag.evaluation.completed` - Evaluación completada
- `rag.datasource.synced` - Fuente de datos sincronizada
- `rag.degradation.detected` - Degradación detectada

---

## ✅ CONCLUSIÓN

La **API y SDKs del módulo RAG** proporcionan una **integración completa** y **robusta** para el gobierno de sistemas RAG con:

- 🌐 **25+ endpoints REST** organizados por funcionalidad
- 🔧 **3 SDKs oficiales** para Python, JavaScript y Java
- 🔐 **Autenticación robusta** con múltiples métodos
- 📊 **Rate limiting** y **webhooks** para escalabilidad
- ❌ **Manejo completo** de errores y excepciones

**Esta API está diseñada** para soportar integraciones complejas y alto volumen de operaciones con sistemas RAG.
