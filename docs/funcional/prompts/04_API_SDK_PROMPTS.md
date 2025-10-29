# 🌐 API Y SDK - MÓDULO PROMPTS

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Especificación completa de APIs REST y SDKs para integración con el módulo de prompts

---

## 🎯 RESUMEN EJECUTIVO

El módulo de **prompts** expone **25+ endpoints REST** organizados en **5 categorías funcionales**, con **3 SDKs oficiales** (Python, JavaScript, Java) y **autenticación robusta** para integración segura con sistemas externos.

### **Características de la API:**
- **RESTful design** con OpenAPI 3.0
- **Autenticación JWT** y API Keys
- **Rate limiting** por plan y usuario
- **Webhooks** para eventos en tiempo real
- **Versionado** con compatibilidad hacia atrás

---

## 🔗 ENDPOINTS PRINCIPALES

### **1. Gestión de Prompts**

#### **GET /api/v1/prompts**
Obtiene lista de prompts con filtros y paginación.

**Parámetros de Query:**
- `page` (int): Número de página (default: 1)
- `size` (int): Tamaño de página (default: 20)
- `type` (string): Filtro por tipo de prompt
- `status` (string): Filtro por estado
- `category` (string): Filtro por categoría
- `search` (string): Búsqueda por nombre/descripción

**Respuesta:**
```json
{
  "content": [
    {
      "idxprompt": 1,
      "prmname": "Customer Support Assistant",
      "prmtype": "TEXT_GENERATION",
      "prmcategory": "customer_service",
      "prmstatus": "ACTIVE",
      "prmversion": "1.2.0",
      "prmapprovalstatus": "APPROVED",
      "prmcreatedat": "2025-10-01T10:00:00Z",
      "prmcreatedby": "john.doe@company.com"
    }
  ],
  "totalElements": 150,
  "totalPages": 8,
  "size": 20,
  "number": 1
}
```

#### **POST /api/v1/prompts**
Crea un nuevo prompt.

**Request:**
```json
{
  "prmname": "New Customer Support Prompt",
  "prmdescription": "Prompt for handling customer inquiries",
  "prmtype": "TEXT_GENERATION",
  "prmcategory": "customer_service",
  "prmcontent": "You are a helpful customer support assistant...",
  "prmparameters": {
    "temperature": 0.7,
    "max_tokens": 500,
    "model": "gpt-4"
  },
  "prmmetadata": {
    "department": "customer_service",
    "priority": "high"
  }
}
```

**Respuesta:**
```json
{
  "idxprompt": 123,
  "prmname": "New Customer Support Prompt",
  "prmstatus": "DRAFT",
  "prmversion": "1.0.0",
  "prmcreatedat": "2025-10-01T12:00:00Z",
  "prmcreatedby": "john.doe@company.com"
}
```

#### **GET /api/v1/prompts/{id}**
Obtiene detalles de un prompt específico.

**Respuesta:**
```json
{
  "idxprompt": 123,
  "prmname": "Customer Support Assistant",
  "prmdescription": "Prompt for handling customer inquiries",
  "prmtype": "TEXT_GENERATION",
  "prmcategory": "customer_service",
  "prmversion": "1.2.0",
  "prmstatus": "ACTIVE",
  "prmcontent": "You are a helpful customer support assistant...",
  "prmparameters": {
    "temperature": 0.7,
    "max_tokens": 500,
    "model": "gpt-4"
  },
  "prmmetadata": {
    "department": "customer_service",
    "priority": "high"
  },
  "prmapprovalstatus": "APPROVED",
  "prmapprovedby": "jane.smith@company.com",
  "prmapprovedat": "2025-10-01T14:30:00Z",
  "prmcreatedat": "2025-10-01T10:00:00Z",
  "prmcreatedby": "john.doe@company.com",
  "prmupdatedat": "2025-10-01T15:00:00Z",
  "prmupdatedby": "john.doe@company.com"
}
```

#### **PUT /api/v1/prompts/{id}**
Actualiza un prompt existente.

#### **DELETE /api/v1/prompts/{id}**
Elimina un prompt (soft delete).

---

### **2. Gestión de Versiones**

#### **GET /api/v1/prompts/{id}/versions**
Obtiene todas las versiones de un prompt.

**Respuesta:**
```json
{
  "content": [
    {
      "idxpromptversion": 1,
      "prmversion": "1.2.0",
      "prmdescription": "Added error handling improvements",
      "prmstatus": "ACTIVE",
      "prmcreatedat": "2025-10-01T15:00:00Z",
      "prmcreatedby": "john.doe@company.com"
    },
    {
      "idxpromptversion": 2,
      "prmversion": "1.1.0",
      "prmdescription": "Initial version",
      "prmstatus": "DEPRECATED",
      "prmcreatedat": "2025-10-01T10:00:00Z",
      "prmcreatedby": "john.doe@company.com"
    }
  ],
  "totalElements": 2,
  "totalPages": 1
}
```

#### **POST /api/v1/prompts/{id}/versions**
Crea una nueva versión del prompt.

**Request:**
```json
{
  "prmcontent": "Updated prompt content...",
  "prmchanges": "Added new features and improved accuracy",
  "prmparameters": {
    "temperature": 0.8,
    "max_tokens": 600
  }
}
```

#### **GET /api/v1/prompts/{id}/versions/{versionId}**
Obtiene detalles de una versión específica.

#### **POST /api/v1/prompts/{id}/versions/{versionId}/rollback**
Hace rollback a una versión anterior.

---

### **3. Validaciones**

#### **GET /api/v1/prompts/{id}/validations**
Obtiene todas las validaciones de un prompt.

**Respuesta:**
```json
{
  "content": [
    {
      "idxpromptvalidation": 1,
      "prmvalidationtype": "SAFETY_CHECK",
      "prmvalidationresult": "PASS",
      "prmvalidationscore": 85.5,
      "prmstatus": "COMPLETED",
      "prmvalidatedat": "2025-10-01T16:00:00Z",
      "prmvalidatedby": "system"
    },
    {
      "idxpromptvalidation": 2,
      "prmvalidationtype": "COMPLIANCE_CHECK",
      "prmvalidationresult": "PASS",
      "prmvalidationscore": 92.0,
      "prmstatus": "COMPLETED",
      "prmvalidatedat": "2025-10-01T16:05:00Z",
      "prmvalidatedby": "system"
    }
  ]
}
```

#### **POST /api/v1/prompts/{id}/validations**
Ejecuta una nueva validación.

**Request:**
```json
{
  "prmvalidationtype": "SAFETY_CHECK",
  "prmvalidationdetails": {
    "check_jailbreak": true,
    "check_injection": true,
    "check_malicious_content": true
  }
}
```

#### **GET /api/v1/prompts/{id}/validations/{validationId}**
Obtiene detalles de una validación específica.

---

### **4. Aprobaciones**

#### **GET /api/v1/prompts/{id}/approvals**
Obtiene todas las aprobaciones de un prompt.

#### **POST /api/v1/prompts/{id}/approvals**
Solicita aprobación de un prompt.

**Request:**
```json
{
  "prmapprovaltype": "NEW_PROMPT",
  "prmrequestreason": "New prompt for customer service automation",
  "prmapprovalstatus": "PENDING"
}
```

#### **GET /api/v1/prompts/{id}/approvals/{approvalId}**
Obtiene detalles de una aprobación específica.

#### **PUT /api/v1/prompts/{id}/approvals/{approvalId}**
Actualiza el estado de una aprobación.

**Request:**
```json
{
  "prmapprovalstatus": "APPROVED",
  "prmapprovalnotes": "Approved after review",
  "prmapproverid": "jane.smith@company.com",
  "prmapprovername": "Jane Smith"
}
```

---

### **5. Métricas y Analytics**

#### **GET /api/v1/prompts/{id}/metrics**
Obtiene métricas de rendimiento de un prompt.

**Respuesta:**
```json
{
  "idxprompt": 123,
  "prmname": "Customer Support Assistant",
  "metrics": {
    "effectiveness": 87.5,
    "cost_per_execution": 0.0025,
    "total_tokens": 1250,
    "success_rate": 94.2,
    "average_response_time": 1.2,
    "user_satisfaction": 4.3
  },
  "period": {
    "start": "2025-10-01T00:00:00Z",
    "end": "2025-10-31T23:59:59Z"
  }
}
```

#### **GET /api/v1/prompts/{id}/usage-statistics**
Obtiene estadísticas de uso.

**Respuesta:**
```json
{
  "idxprompt": 123,
  "usage": {
    "daily": 45,
    "weekly": 315,
    "monthly": 1350,
    "peak_hour": 14,
    "average_session_duration": 3.5,
    "unique_users": 25
  },
  "trends": {
    "daily_growth": 5.2,
    "weekly_growth": 12.8,
    "monthly_growth": 28.5
  }
}
```

#### **GET /api/v1/prompts/{id}/cost-analysis**
Obtiene análisis de costos.

**Respuesta:**
```json
{
  "idxprompt": 123,
  "costs": {
    "total_cost": 3.375,
    "cost_per_execution": 0.0025,
    "daily_cost": 0.1125,
    "monthly_cost": 3.375,
    "yearly_cost": 41.1
  },
  "optimization": {
    "potential_savings": 0.675,
    "savings_percentage": 20.0,
    "recommendations": [
      "Reduce token count by 15%",
      "Use more efficient model"
    ]
  }
}
```

#### **GET /api/v1/prompts/{id}/performance-comparison**
Compara rendimiento entre versiones.

#### **GET /api/v1/prompts/{id}/optimization-opportunities**
Obtiene oportunidades de optimización.

---

## 🔧 SDKs OFICIALES

### **1. Python SDK**

#### **Instalación:**
```bash
pip install codeflowx-prompts-sdk
```

#### **Uso Básico:**
```python
from codeflowx_prompts import PromptClient

# Configuración
client = PromptClient(
    api_key="your_api_key",
    base_url="https://api.codeflowx.com"
)

# Crear prompt
prompt = client.prompts.create({
    "name": "Customer Support Assistant",
    "type": "TEXT_GENERATION",
    "content": "You are a helpful customer support assistant...",
    "parameters": {
        "temperature": 0.7,
        "max_tokens": 500
    }
})

# Obtener prompt
prompt = client.prompts.get(prompt.id)

# Crear versión
version = client.prompts.versions.create(prompt.id, {
    "content": "Updated content...",
    "changes": "Added new features"
})

# Ejecutar validación
validation = client.prompts.validations.create(prompt.id, {
    "type": "SAFETY_CHECK"
})

# Obtener métricas
metrics = client.prompts.metrics.get(prompt.id, {
    "period": "30d"
})

# Solicitar aprobación
approval = client.prompts.approvals.create(prompt.id, {
    "type": "NEW_PROMPT",
    "reason": "New prompt for automation"
})
```

#### **Métodos Avanzados:**
```python
# Batch operations
prompts = client.prompts.batch_create([
    {"name": "Prompt 1", "content": "..."},
    {"name": "Prompt 2", "content": "..."}
])

# Optimización automática
optimization = client.prompts.optimize(prompt.id, {
    "target_reduction": 15.0
})

# Comparación de versiones
comparison = client.prompts.compare_versions(prompt.id, "1.1.0", "1.2.0")

# Análisis de costos
cost_analysis = client.prompts.cost_analysis.get(prompt.id)

# Estadísticas de uso
usage_stats = client.prompts.usage_statistics.get(prompt.id)
```

### **2. JavaScript SDK**

#### **Instalación:**
```bash
npm install @codeflowx/prompts-sdk
```

#### **Uso Básico:**
```javascript
import { PromptClient } from '@codeflowx/prompts-sdk';

// Configuración
const client = new PromptClient({
  apiKey: 'your_api_key',
  baseUrl: 'https://api.codeflowx.com'
});

// Crear prompt
const prompt = await client.prompts.create({
  name: 'Customer Support Assistant',
  type: 'TEXT_GENERATION',
  content: 'You are a helpful customer support assistant...',
  parameters: {
    temperature: 0.7,
    maxTokens: 500
  }
});

// Obtener prompt
const promptDetails = await client.prompts.get(prompt.id);

// Crear versión
const version = await client.prompts.versions.create(prompt.id, {
  content: 'Updated content...',
  changes: 'Added new features'
});

// Ejecutar validación
const validation = await client.prompts.validations.create(prompt.id, {
  type: 'SAFETY_CHECK'
});

// Obtener métricas
const metrics = await client.prompts.metrics.get(prompt.id, {
  period: '30d'
});

// Solicitar aprobación
const approval = await client.prompts.approvals.create(prompt.id, {
  type: 'NEW_PROMPT',
  reason: 'New prompt for automation'
});
```

#### **Métodos Avanzados:**
```javascript
// Batch operations
const prompts = await client.prompts.batchCreate([
  { name: 'Prompt 1', content: '...' },
  { name: 'Prompt 2', content: '...' }
]);

// Optimización automática
const optimization = await client.prompts.optimize(prompt.id, {
  targetReduction: 15.0
});

// Comparación de versiones
const comparison = await client.prompts.compareVersions(prompt.id, '1.1.0', '1.2.0');

// Análisis de costos
const costAnalysis = await client.prompts.costAnalysis.get(prompt.id);

// Estadísticas de uso
const usageStats = await client.prompts.usageStatistics.get(prompt.id);
```

### **3. Java SDK**

#### **Maven Dependency:**
```xml
<dependency>
    <groupId>com.codeflowx</groupId>
    <artifactId>prompts-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### **Uso Básico:**
```java
import com.codeflowx.prompts.PromptClient;
import com.codeflowx.prompts.model.*;

// Configuración
PromptClient client = PromptClient.builder()
    .apiKey("your_api_key")
    .baseUrl("https://api.codeflowx.com")
    .build();

// Crear prompt
PromptRequest request = PromptRequest.builder()
    .name("Customer Support Assistant")
    .type("TEXT_GENERATION")
    .content("You are a helpful customer support assistant...")
    .parameters(Map.of(
        "temperature", 0.7,
        "max_tokens", 500
    ))
    .build();

Prompt prompt = client.prompts().create(request);

// Obtener prompt
Prompt promptDetails = client.prompts().get(prompt.getId());

// Crear versión
VersionRequest versionRequest = VersionRequest.builder()
    .content("Updated content...")
    .changes("Added new features")
    .build();

PromptVersion version = client.prompts().versions().create(prompt.getId(), versionRequest);

// Ejecutar validación
ValidationRequest validationRequest = ValidationRequest.builder()
    .type("SAFETY_CHECK")
    .build();

PromptValidation validation = client.prompts().validations().create(prompt.getId(), validationRequest);

// Obtener métricas
Metrics metrics = client.prompts().metrics().get(prompt.getId(), 
    MetricsRequest.builder()
        .period("30d")
        .build()
);

// Solicitar aprobación
ApprovalRequest approvalRequest = ApprovalRequest.builder()
    .type("NEW_PROMPT")
    .reason("New prompt for automation")
    .build();

PromptApproval approval = client.prompts().approvals().create(prompt.getId(), approvalRequest);
```

#### **Métodos Avanzados:**
```java
// Batch operations
List<PromptRequest> requests = Arrays.asList(
    PromptRequest.builder().name("Prompt 1").content("...").build(),
    PromptRequest.builder().name("Prompt 2").content("...").build()
);
List<Prompt> prompts = client.prompts().batchCreate(requests);

// Optimización automática
OptimizationRequest optRequest = OptimizationRequest.builder()
    .targetReduction(15.0)
    .build();
OptimizationResult optimization = client.prompts().optimize(prompt.getId(), optRequest);

// Comparación de versiones
ComparisonResult comparison = client.prompts().compareVersions(
    prompt.getId(), "1.1.0", "1.2.0"
);

// Análisis de costos
CostAnalysis costAnalysis = client.prompts().costAnalysis().get(prompt.getId());

// Estadísticas de uso
UsageStatistics usageStats = client.prompts().usageStatistics().get(prompt.getId());
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
- **PROMPT_ADMIN:** Acceso completo a todas las operaciones
- **PROMPT_MANAGER:** Gestión de prompts y aprobaciones
- **PROMPT_ENGINEER:** Creación y edición de prompts
- **PROMPT_VIEWER:** Solo lectura de prompts

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
- `prompt.created` - Prompt creado
- `prompt.updated` - Prompt actualizado
- `prompt.approved` - Prompt aprobado
- `prompt.rejected` - Prompt rechazado
- `validation.completed` - Validación completada
- `version.created` - Nueva versión creada

### **Configuración de Webhook:**
```json
{
  "url": "https://your-app.com/webhooks/prompts",
  "events": ["prompt.created", "prompt.approved"],
  "secret": "your_webhook_secret"
}
```

### **Payload de Webhook:**
```json
{
  "event": "prompt.approved",
  "timestamp": "2025-10-01T16:00:00Z",
  "data": {
    "idxprompt": 123,
    "prmname": "Customer Support Assistant",
    "prmapprovalstatus": "APPROVED",
    "prmapproverid": "jane.smith@company.com"
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
- **PROMPT_001:** Prompt no encontrado
- **PROMPT_002:** Prompt ya existe
- **PROMPT_003:** Contenido inválido
- **PROMPT_004:** Validación fallida
- **PROMPT_005:** Aprobación denegada
- **PROMPT_006:** Versión no encontrada
- **PROMPT_007:** Rollback no permitido

### **Respuesta de Error:**
```json
{
  "error": {
    "code": "PROMPT_001",
    "message": "Prompt not found",
    "details": "Prompt with ID 123 does not exist",
    "timestamp": "2025-10-01T16:00:00Z",
    "request_id": "req_123456"
  }
}
```

---

## 📈 FILTROS Y BÚSQUEDA

### **Filtros Disponibles:**
- `type` - Tipo de prompt
- `status` - Estado del prompt
- `category` - Categoría
- `created_by` - Usuario creador
- `created_after` - Fecha de creación desde
- `created_before` - Fecha de creación hasta
- `approval_status` - Estado de aprobación

### **Búsqueda de Texto:**
- `search` - Búsqueda en nombre y descripción
- `content_search` - Búsqueda en contenido
- `metadata_search` - Búsqueda en metadatos

### **Ordenamiento:**
- `sort` - Campo de ordenamiento
- `order` - Dirección (asc/desc)

### **Ejemplo de Filtros:**
```http
GET /api/v1/prompts?type=TEXT_GENERATION&status=ACTIVE&category=customer_service&search=support&sort=prmcreatedat&order=desc&page=1&size=20
```

---

## ✅ CONCLUSIÓN

La **API y SDKs del módulo prompts** proporcionan una **integración completa** y **robusta** para el gobierno de prompts de IA con:

- 🌐 **25+ endpoints REST** organizados por funcionalidad
- 🔧 **3 SDKs oficiales** para Python, JavaScript y Java
- 🔐 **Autenticación robusta** con múltiples métodos
- 📊 **Rate limiting** y **webhooks** para escalabilidad
- ❌ **Manejo completo** de errores y excepciones
- 🔍 **Filtros avanzados** y **búsqueda** de texto
- 📈 **Métricas y analytics** integrados

**Esta API está diseñada** para soportar integraciones complejas y alto volumen de operaciones con prompts de IA.

