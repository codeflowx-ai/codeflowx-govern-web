# 📊 ANALYTICS - API & SDK

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación de API y SDK del módulo Analytics

---

## 🎯 RESUMEN EJECUTIVO

El módulo **Analytics** proporciona APIs RESTful completas para:
- **Metrics:** Métricas de modelos y sistemas
- **Reports:** Generación de reportes
- **Trends:** Análisis de tendencias
- **Dashboards:** Datos para dashboards
- **KPIs:** Indicadores clave de rendimiento

**Total de Endpoints:** ~35  
**Formato:** REST JSON  
**Autenticación:** JWT Bearer Token

---

## 🌐 ENDPOINTS PRINCIPALES

### **1. METRICS API**

#### **GET /api/analytics/metrics**
Obtener métricas agregadas

**Request:**
```bash
GET /api/analytics/metrics?entityType=MODEL&entityId=101&timeRange=7d
Authorization: Bearer {token}
```

**Response:**
```json
{
  "entityType": "MODEL",
  "entityId": 101,
  "entityName": "Fraud Detection Model v2.1",
  "timeRange": "7d",
  "metrics": {
    "performance": {
      "accuracy": 0.9567,
      "precision": 0.9423,
      "recall": 0.9678,
      "f1Score": 0.9549,
      "auc": 0.9812
    },
    "usage": {
      "totalPredictions": 125000,
      "avgLatencyMs": 45.3,
      "errorRate": 0.0023,
      "throughput": 150.2
    },
    "drift": {
      "dataDrift": 0.12,
      "conceptDrift": 0.08,
      "driftStatus": "STABLE"
    }
  },
  "timestamp": "2025-10-29T10:30:00Z"
}
```

#### **POST /api/analytics/metrics/custom**
Registrar métrica personalizada

**Request:**
```bash
POST /api/analytics/metrics/custom
Authorization: Bearer {token}
Content-Type: application/json

{
  "entityType": "MODEL",
  "entityId": 101,
  "metricName": "business_impact_score",
  "metricValue": 8.5,
  "metricUnit": "score",
  "tags": {
    "department": "fraud",
    "priority": "high"
  }
}
```

#### **GET /api/analytics/metrics/timeseries**
Obtener serie temporal de métricas

**Request:**
```bash
GET /api/analytics/metrics/timeseries?metricName=accuracy&entityId=101&from=2025-10-01&to=2025-10-29&interval=1d
Authorization: Bearer {token}
```

**Response:**
```json
{
  "metricName": "accuracy",
  "entityId": 101,
  "interval": "1d",
  "dataPoints": [
    {
      "timestamp": "2025-10-01T00:00:00Z",
      "value": 0.9534,
      "sampleCount": 5420
    },
    {
      "timestamp": "2025-10-02T00:00:00Z",
      "value": 0.9567,
      "sampleCount": 5680
    }
  ],
  "statistics": {
    "min": 0.9512,
    "max": 0.9589,
    "avg": 0.9556,
    "stdDev": 0.0023
  }
}
```

---

### **2. REPORTS API**

#### **GET /api/analytics/reports**
Obtener listado de reportes

**Request:**
```bash
GET /api/analytics/reports?reportType=MODEL_PERFORMANCE&page=0&size=20
Authorization: Bearer {token}
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "reportType": "MODEL_PERFORMANCE",
      "title": "Model Performance Report - October 2025",
      "description": "Comprehensive performance analysis",
      "entityType": "MODEL",
      "entityId": 101,
      "generatedAt": "2025-10-29T08:00:00Z",
      "generatedBy": "system",
      "status": "COMPLETED",
      "fileUrl": "/api/analytics/reports/1/download",
      "format": "PDF"
    }
  ],
  "totalElements": 45
}
```

#### **POST /api/analytics/reports/generate**
Generar nuevo reporte

**Request:**
```bash
POST /api/analytics/reports/generate
Authorization: Bearer {token}
Content-Type: application/json

{
  "reportType": "MODEL_PERFORMANCE",
  "entityType": "MODEL",
  "entityId": 101,
  "dateRange": {
    "from": "2025-10-01",
    "to": "2025-10-31"
  },
  "format": "PDF",
  "includeCharts": true,
  "includeRawData": false,
  "notifyEmail": "team@company.com"
}
```

**Response:**
```json
{
  "reportId": 46,
  "status": "PROCESSING",
  "estimatedCompletionTime": "2025-10-29T10:35:00Z",
  "message": "Report generation started"
}
```

#### **GET /api/analytics/reports/{id}/download**
Descargar reporte

**Request:**
```bash
GET /api/analytics/reports/1/download
Authorization: Bearer {token}
```

**Response:** Binary PDF/CSV/Excel file

---

### **3. TRENDS API**

#### **GET /api/analytics/trends**
Obtener análisis de tendencias

**Request:**
```bash
GET /api/analytics/trends?metricName=accuracy&entityIds=101,102,103&timeRange=30d
Authorization: Bearer {token}
```

**Response:**
```json
{
  "metricName": "accuracy",
  "timeRange": "30d",
  "trends": [
    {
      "entityId": 101,
      "entityName": "Fraud Detection v2.1",
      "trendDirection": "IMPROVING",
      "trendStrength": 0.85,
      "changePercent": 2.3,
      "currentValue": 0.9567,
      "previousValue": 0.9345,
      "forecast": {
        "next7Days": 0.9589,
        "next30Days": 0.9612,
        "confidence": 0.92
      }
    }
  ],
  "summary": {
    "totalEntities": 3,
    "improving": 2,
    "declining": 0,
    "stable": 1
  }
}
```

#### **POST /api/analytics/trends/forecast**
Obtener forecast predictivo

**Request:**
```bash
POST /api/analytics/trends/forecast
Authorization: Bearer {token}
Content-Type: application/json

{
  "metricName": "error_rate",
  "entityId": 101,
  "historicalDays": 90,
  "forecastDays": 30,
  "confidenceLevel": 0.95
}
```

**Response:**
```json
{
  "metricName": "error_rate",
  "entityId": 101,
  "forecast": [
    {
      "date": "2025-10-30",
      "predictedValue": 0.0023,
      "lowerBound": 0.0018,
      "upperBound": 0.0028,
      "confidence": 0.95
    }
  ],
  "modelUsed": "ARIMA",
  "accuracy": 0.89
}
```

---

### **4. DASHBOARDS API**

#### **GET /api/analytics/dashboards/{type}**
Obtener datos para dashboard

**Request:**
```bash
GET /api/analytics/dashboards/executive?timeRange=7d
Authorization: Bearer {token}
```

**Response:**
```json
{
  "dashboardType": "EXECUTIVE",
  "timeRange": "7d",
  "data": {
    "modelsSummary": {
      "totalModels": 45,
      "activeModels": 38,
      "avgAccuracy": 0.9234,
      "totalPredictions": 2500000
    },
    "performanceTrends": {
      "accuracy": [0.9201, 0.9223, 0.9234],
      "latency": [45.2, 43.8, 42.1],
      "throughput": [1450, 1523, 1589]
    },
    "alerts": {
      "critical": 2,
      "high": 5,
      "medium": 12
    },
    "topModels": [
      {
        "id": 101,
        "name": "Fraud Detection v2.1",
        "accuracy": 0.9567,
        "predictions": 125000
      }
    ]
  },
  "refreshedAt": "2025-10-29T10:30:00Z"
}
```

#### **GET /api/analytics/dashboards/kpis**
Obtener KPIs principales

**Request:**
```bash
GET /api/analytics/dashboards/kpis?entityType=PORTFOLIO&timeRange=30d
Authorization: Bearer {token}
```

**Response:**
```json
{
  "kpis": [
    {
      "name": "Overall Model Accuracy",
      "value": 0.9234,
      "unit": "percentage",
      "target": 0.9500,
      "status": "AT_RISK",
      "trend": "IMPROVING",
      "changePercent": 1.2
    },
    {
      "name": "Average Latency",
      "value": 42.1,
      "unit": "ms",
      "target": 50.0,
      "status": "HEALTHY",
      "trend": "IMPROVING",
      "changePercent": -5.3
    },
    {
      "name": "Total Predictions",
      "value": 2500000,
      "unit": "count",
      "target": 2000000,
      "status": "HEALTHY",
      "trend": "GROWING",
      "changePercent": 15.2
    }
  ]
}
```

---

### **5. COMPARISON API**

#### **POST /api/analytics/compare/models**
Comparar múltiples modelos

**Request:**
```bash
POST /api/analytics/compare/models
Authorization: Bearer {token}
Content-Type: application/json

{
  "modelIds": [101, 102, 103],
  "metrics": ["accuracy", "precision", "recall", "f1_score"],
  "timeRange": "30d"
}
```

**Response:**
```json
{
  "comparison": [
    {
      "modelId": 101,
      "modelName": "Fraud Detection v2.1",
      "metrics": {
        "accuracy": 0.9567,
        "precision": 0.9423,
        "recall": 0.9678,
        "f1_score": 0.9549
      }
    },
    {
      "modelId": 102,
      "modelName": "Credit Risk v1.5",
      "metrics": {
        "accuracy": 0.9234,
        "precision": 0.9123,
        "recall": 0.9345,
        "f1_score": 0.9233
      }
    }
  ],
  "winner": {
    "modelId": 101,
    "reasons": ["Higher accuracy", "Better F1 score"]
  }
}
```

---

### **6. EXPORT API**

#### **POST /api/analytics/export**
Exportar datos analíticos

**Request:**
```bash
POST /api/analytics/export
Authorization: Bearer {token}
Content-Type: application/json

{
  "dataType": "METRICS",
  "entityType": "MODEL",
  "entityIds": [101, 102],
  "dateRange": {
    "from": "2025-10-01",
    "to": "2025-10-31"
  },
  "format": "CSV",
  "includeHeaders": true,
  "compression": "GZIP"
}
```

**Response:**
```json
{
  "exportId": "exp_789xyz",
  "status": "PROCESSING",
  "downloadUrl": null,
  "estimatedCompletionTime": "2025-10-29T10:32:00Z"
}
```

---

## 📦 SDK CLIENT

### **Java Client Example**

```java
// Configuración del cliente
CodeflowxAnalyticsClient client = CodeflowxAnalyticsClient.builder()
    .baseUrl("https://api.codeflowx.io")
    .apiKey("your-api-key")
    .build();

// Obtener métricas
MetricsResponse metrics = client.metrics().getMetrics(
    MetricsRequest.builder()
        .entityType(EntityType.MODEL)
        .entityId(101L)
        .timeRange("7d")
        .build()
);

System.out.println("Accuracy: " + metrics.getPerformance().getAccuracy());

// Generar reporte
ReportGenerationResponse report = client.reports().generate(
    ReportRequest.builder()
        .reportType(ReportType.MODEL_PERFORMANCE)
        .entityId(101L)
        .format(ReportFormat.PDF)
        .includeCharts(true)
        .build()
);

// Analizar tendencias
TrendsResponse trends = client.trends().analyze(
    TrendsRequest.builder()
        .metricName("accuracy")
        .entityIds(Arrays.asList(101L, 102L, 103L))
        .timeRange("30d")
        .build()
);

trends.getTrends().forEach(trend -> 
    System.out.println(String.format("%s: %s (%.2f%%)", 
        trend.getEntityName(),
        trend.getTrendDirection(),
        trend.getChangePercent()
    ))
);

// Obtener KPIs
KPIsResponse kpis = client.dashboards().getKPIs(
    KPIsRequest.builder()
        .entityType(EntityType.PORTFOLIO)
        .timeRange("30d")
        .build()
);
```

### **Python Client Example**

```python
from codeflowx_analytics import AnalyticsClient

# Configuración del cliente
client = AnalyticsClient(
    base_url="https://api.codeflowx.io",
    api_key="your-api-key"
)

# Obtener métricas
metrics = client.metrics.get_metrics(
    entity_type="MODEL",
    entity_id=101,
    time_range="7d"
)

print(f"Accuracy: {metrics.performance.accuracy}")

# Generar reporte
report = client.reports.generate(
    report_type="MODEL_PERFORMANCE",
    entity_id=101,
    format="PDF",
    include_charts=True
)

print(f"Report ID: {report.report_id}")

# Analizar tendencias
trends = client.trends.analyze(
    metric_name="accuracy",
    entity_ids=[101, 102, 103],
    time_range="30d"
)

for trend in trends.trends:
    print(f"{trend.entity_name}: {trend.trend_direction} ({trend.change_percent}%)")

# Obtener KPIs
kpis = client.dashboards.get_kpis(
    entity_type="PORTFOLIO",
    time_range="30d"
)

for kpi in kpis.kpis:
    print(f"{kpi.name}: {kpi.value} {kpi.unit} ({kpi.status})")
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

# Usar token
GET /api/analytics/metrics
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```

---

## 📊 RATE LIMITING

### **Límites por Endpoint:**
- **GET /metrics:** 100 req/min
- **POST /reports/generate:** 10 req/min
- **GET /trends:** 50 req/min
- **GET /dashboards:** 200 req/min
- **POST /export:** 5 req/min

---

## 🎯 ERROR HANDLING

### **Error Response Format:**

```json
{
  "error": {
    "code": "METRIC_NOT_FOUND",
    "message": "Metric 'custom_metric' not found for entity 101",
    "details": {
      "entityId": 101,
      "metricName": "custom_metric"
    },
    "timestamp": "2025-10-29T14:30:00Z",
    "requestId": "req_789xyz"
  }
}
```

### **Common Error Codes:**
- `METRIC_NOT_FOUND` - Métrica no encontrada
- `ENTITY_NOT_FOUND` - Entidad no encontrada
- `INVALID_TIME_RANGE` - Rango de tiempo inválido
- `REPORT_GENERATION_FAILED` - Error al generar reporte
- `INSUFFICIENT_DATA` - Datos insuficientes para análisis
- `EXPORT_LIMIT_EXCEEDED` - Límite de exportación excedido

---

## 🎯 CONCLUSIÓN

El módulo **Analytics** proporciona APIs completas para:

- ✅ **Métricas** - Agregación y series temporales
- ✅ **Reportes** - Generación automatizada
- ✅ **Tendencias** - Análisis predictivo
- ✅ **Dashboards** - KPIs en tiempo real
- ✅ **Comparaciones** - Análisis multi-modelo
- ✅ **Exportación** - Datos en múltiples formatos
- ✅ **SDKs** - Java y Python
- ✅ **Autenticación** - JWT seguro

**Total de ~35 endpoints REST completamente documentados y funcionales.**

