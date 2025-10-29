# ⚖️ ETHICS - API & SDK

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación de API y SDK del módulo Ethics

---

## 🎯 RESUMEN EJECUTIVO

El módulo **Ethics** proporciona APIs RESTful completas para:
- **Ethical Reviews:** Revisiones éticas de sistemas AI
- **Bias Detection:** Detección de sesgos
- **Fairness Assessment:** Evaluación de equidad
- **Impact Analysis:** Análisis de impacto social
- **Transparency:** Métricas de transparencia

**Total de Endpoints:** ~30  
**Formato:** REST JSON  
**Autenticación:** JWT Bearer Token

---

## 🌐 ENDPOINTS PRINCIPALES

### **1. ETHICAL REVIEWS API**

#### **GET /api/ethics/reviews**
Obtener revisiones éticas

**Request:**
```bash
GET /api/ethics/reviews?entityType=MODEL&entityId=101&status=PENDING
Authorization: Bearer {token}
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "entityType": "MODEL",
      "entityId": 101,
      "entityName": "Credit Scoring Model v2.0",
      "reviewType": "COMPREHENSIVE",
      "status": "PENDING",
      "requestedBy": 201,
      "requestedAt": "2025-10-15T10:00:00Z",
      "ethicalIssues": [
        {
          "category": "FAIRNESS",
          "severity": "HIGH",
          "description": "Potential demographic disparity detected"
        }
      ],
      "overallScore": null,
      "recommendation": null
    }
  ],
  "totalElements": 12
}
```

#### **POST /api/ethics/reviews**
Solicitar revisión ética

**Request:**
```bash
POST /api/ethics/reviews
Authorization: Bearer {token}
Content-Type: application/json

{
  "entityType": "MODEL",
  "entityId": 101,
  "reviewType": "COMPREHENSIVE",
  "priority": "HIGH",
  "stakeholders": [201, 202, 203],
  "notes": "Model for production deployment, needs ethical clearance"
}
```

**Response:**
```json
{
  "id": 13,
  "entityType": "MODEL",
  "entityId": 101,
  "status": "PENDING",
  "estimatedCompletionDate": "2025-10-25T10:00:00Z",
  "message": "Ethical review request created successfully"
}
```

#### **PUT /api/ethics/reviews/{id}/complete**
Completar revisión ética

**Request:**
```bash
PUT /api/ethics/reviews/1/complete
Authorization: Bearer {token}
Content-Type: application/json

{
  "overallScore": 8.5,
  "recommendation": "APPROVED_WITH_CONDITIONS",
  "conditions": [
    "Implement fairness constraints",
    "Monitor for bias quarterly",
    "Provide explainability reports"
  ],
  "ethicalAssessment": {
    "fairness": 8.0,
    "transparency": 9.0,
    "accountability": 8.5,
    "privacy": 9.5,
    "safety": 8.0
  },
  "reviewerNotes": "Model shows good ethical practices overall, minor improvements needed in fairness"
}
```

---

### **2. BIAS DETECTION API**

#### **POST /api/ethics/bias/detect**
Detectar sesgos en datos o modelos

**Request:**
```bash
POST /api/ethics/bias/detect
Authorization: Bearer {token}
Content-Type: application/json

{
  "entityType": "MODEL",
  "entityId": 101,
  "sensitiveAttributes": ["gender", "ethnicity", "age_group"],
  "fairnessMetrics": ["demographic_parity", "equalized_odds", "equal_opportunity"],
  "datasetId": 501
}
```

**Response:**
```json
{
  "detectionId": "bias_det_123",
  "entityType": "MODEL",
  "entityId": 101,
  "biasResults": {
    "demographic_parity": {
      "gender": {
        "male": 0.75,
        "female": 0.68,
        "disparity": 0.07,
        "disparityRatio": 1.103,
        "threshold": 0.80,
        "passed": false
      },
      "ethnicity": {
        "group_a": 0.72,
        "group_b": 0.71,
        "disparity": 0.01,
        "disparityRatio": 1.014,
        "passed": true
      }
    },
    "equalized_odds": {
      "gender": {
        "tpr_male": 0.82,
        "tpr_female": 0.78,
        "fpr_male": 0.15,
        "fpr_female": 0.18,
        "tpr_disparity": 0.04,
        "fpr_disparity": 0.03,
        "passed": true
      }
    }
  },
  "overallBiasScore": 7.5,
  "biasLevel": "MODERATE",
  "recommendations": [
    "Consider rebalancing training data for gender",
    "Apply fairness constraints during model training",
    "Monitor gender disparity in production"
  ],
  "detectedAt": "2025-10-29T11:00:00Z"
}
```

#### **GET /api/ethics/bias/history**
Obtener historial de detección de sesgos

**Request:**
```bash
GET /api/ethics/bias/history?entityId=101&timeRange=30d
Authorization: Bearer {token}
```

**Response:**
```json
{
  "entityId": 101,
  "timeRange": "30d",
  "detections": [
    {
      "date": "2025-10-01",
      "overallBiasScore": 6.8,
      "biasLevel": "HIGH"
    },
    {
      "date": "2025-10-15",
      "overallBiasScore": 7.5,
      "biasLevel": "MODERATE"
    }
  ],
  "trend": "IMPROVING",
  "improvementPercent": 10.29
}
```

---

### **3. FAIRNESS ASSESSMENT API**

#### **POST /api/ethics/fairness/assess**
Evaluar equidad del sistema

**Request:**
```bash
POST /api/ethics/fairness/assess
Authorization: Bearer {token}
Content-Type: application/json

{
  "entityType": "MODEL",
  "entityId": 101,
  "protectedAttributes": ["gender", "ethnicity"],
  "outcomeVariable": "credit_approval",
  "assessmentType": "COMPREHENSIVE",
  "includeRemediation": true
}
```

**Response:**
```json
{
  "assessmentId": "fair_assess_456",
  "entityId": 101,
  "fairnessMetrics": {
    "statistical_parity": {
      "score": 0.85,
      "threshold": 0.80,
      "passed": true,
      "interpretation": "Model shows acceptable statistical parity"
    },
    "disparate_impact": {
      "score": 0.78,
      "threshold": 0.80,
      "passed": false,
      "interpretation": "Potential disparate impact detected"
    },
    "calibration": {
      "score": 0.92,
      "passed": true
    }
  },
  "overallFairnessScore": 8.2,
  "fairnessLevel": "GOOD",
  "remediationStrategies": [
    {
      "strategy": "REWEIGHTING",
      "description": "Apply sample reweighting to balance representation",
      "expectedImprovement": 0.15,
      "complexity": "LOW"
    },
    {
      "strategy": "THRESHOLD_OPTIMIZATION",
      "description": "Optimize decision thresholds per group",
      "expectedImprovement": 0.08,
      "complexity": "MEDIUM"
    }
  ],
  "assessedAt": "2025-10-29T11:15:00Z"
}
```

---

### **4. IMPACT ANALYSIS API**

#### **POST /api/ethics/impact/analyze**
Analizar impacto social del sistema

**Request:**
```bash
POST /api/ethics/impact/analyze
Authorization: Bearer {token}
Content-Type: application/json

{
  "entityType": "MODEL",
  "entityId": 101,
  "impactDimensions": [
    "SOCIAL_WELFARE",
    "ECONOMIC_IMPACT",
    "ENVIRONMENTAL",
    "INDIVIDUAL_RIGHTS"
  ],
  "stakeholders": ["applicants", "lenders", "regulators"],
  "timeHorizon": "12_MONTHS"
}
```

**Response:**
```json
{
  "analysisId": "impact_789",
  "entityId": 101,
  "impactAssessment": {
    "SOCIAL_WELFARE": {
      "score": 7.5,
      "positiveImpacts": [
        "Increased access to credit for underserved communities",
        "Faster decision times reduce anxiety"
      ],
      "negativeImpacts": [
        "Potential for algorithmic discrimination",
        "Reduced human oversight"
      ]
    },
    "ECONOMIC_IMPACT": {
      "score": 8.2,
      "estimatedBenefit": 2500000,
      "estimatedCost": 450000,
      "netBenefit": 2050000
    },
    "INDIVIDUAL_RIGHTS": {
      "score": 7.0,
      "concerns": [
        "Data privacy considerations",
        "Right to explanation needed"
      ]
    }
  },
  "overallImpactScore": 7.6,
  "recommendedActions": [
    "Implement explainability features",
    "Establish human appeal process",
    "Regular fairness audits"
  ],
  "analyzedAt": "2025-10-29T11:30:00Z"
}
```

---

### **5. TRANSPARENCY API**

#### **GET /api/ethics/transparency/report**
Obtener reporte de transparencia

**Request:**
```bash
GET /api/ethics/transparency/report?entityId=101
Authorization: Bearer {token}
```

**Response:**
```json
{
  "entityId": 101,
  "entityName": "Credit Scoring Model v2.0",
  "transparencyMetrics": {
    "explainability": {
      "score": 8.5,
      "methods": ["SHAP", "LIME", "Feature Importance"],
      "availableExplanations": true
    },
    "documentation": {
      "score": 9.0,
      "modelCard": true,
      "datasheetForDataset": true,
      "technicalDocumentation": true
    },
    "auditability": {
      "score": 8.0,
      "auditTrail": true,
      "versionControl": true,
      "reproducibility": true
    },
    "disclosure": {
      "score": 7.5,
      "limitationsDisclosed": true,
      "riskDisclosure": true,
      "performanceMetrics": true
    }
  },
  "overallTransparencyScore": 8.25,
  "recommendations": [
    "Enhance external communication of limitations",
    "Provide more detailed risk disclosures"
  ]
}
```

---

### **6. COMPLIANCE CHECK API**

#### **POST /api/ethics/compliance/check**
Verificar compliance ético

**Request:**
```bash
POST /api/ethics/compliance/check
Authorization: Bearer {token}
Content-Type: application/json

{
  "entityType": "MODEL",
  "entityId": 101,
  "frameworks": ["EU_AI_ACT", "IEEE_7000", "OECD_AI"],
  "includeRemediation": true
}
```

**Response:**
```json
{
  "checkId": "eth_comp_999",
  "entityId": 101,
  "complianceResults": {
    "EU_AI_ACT": {
      "compliant": true,
      "score": 8.8,
      "requirements": [
        {
          "requirement": "Risk Assessment",
          "status": "MET",
          "evidence": "Comprehensive risk assessment completed"
        },
        {
          "requirement": "Human Oversight",
          "status": "MET",
          "evidence": "Human-in-the-loop implemented"
        }
      ]
    },
    "IEEE_7000": {
      "compliant": true,
      "score": 8.5
    },
    "OECD_AI": {
      "compliant": true,
      "score": 9.0
    }
  },
  "overallCompliance": true,
  "checkedAt": "2025-10-29T11:45:00Z"
}
```

---

## 📦 SDK CLIENT

### **Java Client Example**

```java
// Configuración del cliente
CodeflowxEthicsClient client = CodeflowxEthicsClient.builder()
    .baseUrl("https://api.codeflowx.io")
    .apiKey("your-api-key")
    .build();

// Solicitar revisión ética
EthicalReviewRequest request = EthicalReviewRequest.builder()
    .entityType(EntityType.MODEL)
    .entityId(101L)
    .reviewType(ReviewType.COMPREHENSIVE)
    .priority(Priority.HIGH)
    .build();

EthicalReview review = client.reviews().create(request);

// Detectar sesgos
BiasDetectionRequest biasReq = BiasDetectionRequest.builder()
    .entityId(101L)
    .sensitiveAttributes(Arrays.asList("gender", "ethnicity"))
    .fairnessMetrics(Arrays.asList("demographic_parity", "equalized_odds"))
    .build();

BiasDetectionResult biasResult = client.bias().detect(biasReq);
System.out.println("Bias Level: " + biasResult.getBiasLevel());

// Evaluar equidad
FairnessAssessmentRequest fairReq = FairnessAssessmentRequest.builder()
    .entityId(101L)
    .protectedAttributes(Arrays.asList("gender", "ethnicity"))
    .includeRemediation(true)
    .build();

FairnessAssessment fairness = client.fairness().assess(fairReq);
System.out.println("Fairness Score: " + fairness.getOverallFairnessScore());

// Analizar impacto
ImpactAnalysisRequest impactReq = ImpactAnalysisRequest.builder()
    .entityId(101L)
    .impactDimensions(Arrays.asList(
        ImpactDimension.SOCIAL_WELFARE,
        ImpactDimension.ECONOMIC_IMPACT
    ))
    .build();

ImpactAnalysis impact = client.impact().analyze(impactReq);
```

### **Python Client Example**

```python
from codeflowx_ethics import EthicsClient

# Configuración del cliente
client = EthicsClient(
    base_url="https://api.codeflowx.io",
    api_key="your-api-key"
)

# Solicitar revisión ética
review = client.reviews.create(
    entity_type="MODEL",
    entity_id=101,
    review_type="COMPREHENSIVE",
    priority="HIGH"
)

# Detectar sesgos
bias_result = client.bias.detect(
    entity_id=101,
    sensitive_attributes=["gender", "ethnicity"],
    fairness_metrics=["demographic_parity", "equalized_odds"]
)

print(f"Bias Level: {bias_result.bias_level}")

# Evaluar equidad
fairness = client.fairness.assess(
    entity_id=101,
    protected_attributes=["gender", "ethnicity"],
    include_remediation=True
)

print(f"Fairness Score: {fairness.overall_fairness_score}")

# Analizar impacto
impact = client.impact.analyze(
    entity_id=101,
    impact_dimensions=["SOCIAL_WELFARE", "ECONOMIC_IMPACT"]
)

print(f"Overall Impact Score: {impact.overall_impact_score}")
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
GET /api/ethics/reviews
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```

---

## 📊 RATE LIMITING

### **Límites por Endpoint:**
- **GET /reviews:** 100 req/min
- **POST /reviews:** 20 req/min
- **POST /bias/detect:** 10 req/min
- **POST /fairness/assess:** 10 req/min
- **POST /impact/analyze:** 5 req/min

---

## 🎯 ERROR HANDLING

### **Error Response Format:**

```json
{
  "error": {
    "code": "INSUFFICIENT_DATA",
    "message": "Insufficient data for bias detection analysis",
    "details": {
      "required_samples": 1000,
      "available_samples": 500
    },
    "timestamp": "2025-10-29T14:30:00Z",
    "requestId": "req_789xyz"
  }
}
```

### **Common Error Codes:**
- `REVIEW_NOT_FOUND` - Revisión no encontrada
- `INSUFFICIENT_DATA` - Datos insuficientes para análisis
- `INVALID_ATTRIBUTE` - Atributo sensible inválido
- `ANALYSIS_FAILED` - Error en análisis
- `ENTITY_NOT_FOUND` - Entidad no encontrada

---

## 🎯 CONCLUSIÓN

El módulo **Ethics** proporciona APIs completas para:

- ✅ **Revisiones Éticas** - Evaluación completa
- ✅ **Detección de Sesgos** - Múltiples métricas
- ✅ **Evaluación de Equidad** - Fairness assessment
- ✅ **Análisis de Impacto** - Social y económico
- ✅ **Transparencia** - Reporting completo
- ✅ **Compliance** - Verificación de frameworks
- ✅ **SDKs** - Java y Python
- ✅ **Autenticación** - JWT seguro

**Total de ~30 endpoints REST completamente documentados y funcionales.**

