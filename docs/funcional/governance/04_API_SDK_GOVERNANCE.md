# 🌐 API Y SDK - MÓDULO GOVERNANCE

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Especificación completa de APIs REST y SDKs para integración con el módulo governance

---

## 🎯 RESUMEN EJECUTIVO

El módulo de **governance** expone **20+ endpoints REST** organizados en **4 categorías funcionales**, con **3 SDKs oficiales** (Python, JavaScript, Java) y **autenticación robusta** para integración segura con sistemas externos.

---

## 🔗 ENDPOINTS PRINCIPALES

### **1. Gestión de Métricas de Gobierno**

#### **GET /api/v1/governance/metrics**
Obtiene lista de métricas de gobierno con filtros y paginación.

**Respuesta:**
```json
{
  "content": [
    {
      "idxgovernancemetric": 1,
      "gvmname": "Overall Governance Score",
      "gvmtype": "SCORE",
      "gvmcategory": "GENERAL",
      "gvmvalue": "85.5",
      "gvmstatus": "HEALTHY",
      "gvmcompliance": "COMPLIANT",
      "gvmrisk": "LOW",
      "gvmcreatedat": "2025-10-01T10:00:00Z"
    }
  ],
  "totalElements": 50,
  "totalPages": 3
}
```

#### **POST /api/v1/governance/metrics**
Crea una nueva métrica de gobierno.

**Request:**
```json
{
  "gvmname": "Model Compliance Score",
  "gvmdescription": "Compliance score for AI models",
  "gvmtype": "SCORE",
  "gvmcategory": "MODELS",
  "gvmthreshold": "80.0",
  "gvmcompliance": "COMPLIANT"
}
```

#### **GET /api/v1/governance/metrics/{id}**
Obtiene detalles de una métrica específica.

#### **PUT /api/v1/governance/metrics/{id}**
Actualiza una métrica existente.

### **2. Evaluación de Gobierno**

#### **POST /api/v1/governance/evaluate**
Ejecuta evaluación integral de gobierno.

**Request:**
```json
{
  "category": "MODELS",
  "period": "MONTHLY",
  "includeCompliance": true,
  "includeRisk": true,
  "includePerformance": true
}
```

**Respuesta:**
```json
{
  "evaluationId": "eval_123",
  "overallScore": 85.5,
  "complianceRate": 92.3,
  "riskLevel": "LOW",
  "status": "COMPLETED",
  "recommendations": [
    "Improve model documentation",
    "Enhance monitoring coverage"
  ],
  "timestamp": "2025-10-01T12:00:00Z"
}
```

#### **GET /api/v1/governance/evaluations**
Obtiene evaluaciones de gobierno.

#### **GET /api/v1/governance/evaluations/{id}**
Obtiene detalles de una evaluación específica.

### **3. Reportes y Documentación**

#### **POST /api/v1/governance/reports**
Genera reporte de gobierno.

**Request:**
```json
{
  "reportType": "EXECUTIVE_SUMMARY",
  "period": {
    "start": "2025-10-01T00:00:00Z",
    "end": "2025-10-31T23:59:59Z"
  },
  "includeCharts": true,
  "includeRecommendations": true
}
```

**Respuesta:**
```json
{
  "reportId": "report_456",
  "reportUrl": "https://api.codeflowx.com/reports/governance/456",
  "reportType": "EXECUTIVE_SUMMARY",
  "status": "COMPLETED",
  "summary": {
    "overallScore": 85.5,
    "complianceRate": 92.3,
    "riskLevel": "LOW",
    "totalMetrics": 150
  },
  "timestamp": "2025-10-01T14:00:00Z"
}
```

#### **GET /api/v1/governance/reports**
Obtiene lista de reportes generados.

#### **GET /api/v1/governance/reports/{id}**
Obtiene detalles de un reporte específico.

### **4. Auditoría y Compliance**

#### **GET /api/v1/governance/audit-trail**
Obtiene trazabilidad de auditoría.

**Respuesta:**
```json
{
  "content": [
    {
      "idxgovernanceaudittrail": 1,
      "gatoperation": "UPDATE",
      "gatentity": "governance_metric",
      "gatentityid": 123,
      "gatoldvalue": "80.0",
      "gatnewvalue": "85.5",
      "gatchangedby": "john.doe@company.com",
      "gatchangedat": "2025-10-01T15:00:00Z",
      "gatreason": "Automatic score update"
    }
  ],
  "totalElements": 1000,
  "totalPages": 50
}
```

#### **GET /api/v1/governance/compliance**
Obtiene estado de compliance.

**Respuesta:**
```json
{
  "overallCompliance": 92.3,
  "complianceStatus": "GOOD",
  "violationsCount": 8,
  "categories": [
    {
      "category": "MODELS",
      "complianceRate": 95.0,
      "violations": 2,
      "status": "COMPLIANT"
    },
    {
      "category": "AGENTS",
      "complianceRate": 90.0,
      "violations": 3,
      "status": "COMPLIANT"
    }
  ],
  "recommendations": [
    "Address remaining model violations",
    "Improve agent monitoring"
  ]
}
```

---

## 🔧 SDKs OFICIALES

### **Python SDK**
```python
from codeflowx_governance import GovernanceClient

client = GovernanceClient(api_key="your_api_key")

# Crear métrica de gobierno
metric = client.metrics.create({
    "name": "Model Compliance Score",
    "type": "SCORE",
    "category": "MODELS",
    "threshold": "80.0"
})

# Ejecutar evaluación de gobierno
evaluation = client.evaluate({
    "category": "MODELS",
    "period": "MONTHLY",
    "includeCompliance": True
})

# Generar reporte ejecutivo
report = client.reports.create({
    "reportType": "EXECUTIVE_SUMMARY",
    "period": {
        "start": "2025-10-01T00:00:00Z",
        "end": "2025-10-31T23:59:59Z"
    }
})

# Obtener estado de compliance
compliance = client.compliance.get()

# Obtener auditoría
audit_trail = client.audit_trail.get({
    "entity": "governance_metric",
    "startDate": "2025-10-01T00:00:00Z"
})
```

### **JavaScript SDK**
```javascript
import { GovernanceClient } from '@codeflowx/governance-sdk';

const client = new GovernanceClient({
  apiKey: 'your_api_key'
});

// Crear métrica de gobierno
const metric = await client.metrics.create({
  name: 'Model Compliance Score',
  type: 'SCORE',
  category: 'MODELS',
  threshold: '80.0'
});

// Ejecutar evaluación de gobierno
const evaluation = await client.evaluate({
  category: 'MODELS',
  period: 'MONTHLY',
  includeCompliance: true
});

// Generar reporte ejecutivo
const report = await client.reports.create({
  reportType: 'EXECUTIVE_SUMMARY',
  period: {
    start: '2025-10-01T00:00:00Z',
    end: '2025-10-31T23:59:59Z'
  }
});

// Obtener estado de compliance
const compliance = await client.compliance.get();
```

### **Java SDK**
```java
import com.codeflowx.governance.GovernanceClient;

GovernanceClient client = GovernanceClient.builder()
    .apiKey("your_api_key")
    .build();

// Crear métrica de gobierno
GovernanceMetricRequest request = GovernanceMetricRequest.builder()
    .name("Model Compliance Score")
    .type("SCORE")
    .category("MODELS")
    .threshold("80.0")
    .build();

GovernanceMetric metric = client.metrics().create(request);

// Ejecutar evaluación de gobierno
EvaluationRequest evalRequest = EvaluationRequest.builder()
    .category("MODELS")
    .period("MONTHLY")
    .includeCompliance(true)
    .build();

GovernanceEvaluation evaluation = client.evaluate(evalRequest);

// Generar reporte ejecutivo
ReportRequest reportRequest = ReportRequest.builder()
    .reportType("EXECUTIVE_SUMMARY")
    .period(Period.builder()
        .start(LocalDateTime.parse("2025-10-01T00:00:00"))
        .end(LocalDateTime.parse("2025-10-31T23:59:59"))
        .build())
    .build();

GovernanceReport report = client.reports().create(reportRequest);
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
- **GOVERNANCE_ADMIN:** Acceso completo a todas las operaciones
- **GOVERNANCE_MANAGER:** Gestión de métricas y evaluaciones
- **GOVERNANCE_AUDITOR:** Solo lectura para auditoría
- **GOVERNANCE_VIEWER:** Solo lectura de reportes
- **EXECUTIVE:** Acceso a reportes ejecutivos

---

## 📊 RATE LIMITING

### **Límites por Plan:**
- **Free:** 50 requests/hour
- **Professional:** 500 requests/hour
- **Enterprise:** 5,000 requests/hour

---

## 🔔 WEBHOOKS

### **Eventos Disponibles:**
- `governance.metric.updated` - Métrica actualizada
- `governance.evaluation.completed` - Evaluación completada
- `governance.compliance.violation` - Violación de compliance detectada
- `governance.report.generated` - Reporte generado
- `governance.anomaly.detected` - Anomalía detectada

---

## ✅ CONCLUSIÓN

La **API y SDKs del módulo governance** proporcionan una **integración completa** y **robusta** para el gobierno de IA con:

- 🌐 **20+ endpoints REST** organizados por funcionalidad
- 🔧 **3 SDKs oficiales** para Python, JavaScript y Java
- 🔐 **Autenticación robusta** con múltiples métodos
- 📊 **Rate limiting** y **webhooks** para escalabilidad
- ❌ **Manejo completo** de errores y excepciones

**Esta API está diseñada** para soportar integraciones complejas y alto volumen de operaciones con sistemas de gobierno de IA.
