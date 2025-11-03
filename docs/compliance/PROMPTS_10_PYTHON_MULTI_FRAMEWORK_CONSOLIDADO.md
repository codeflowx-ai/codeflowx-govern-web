# PROMPTS 10 - PYTHON MULTI-FRAMEWORK (CONSOLIDADO)
## Extensiones Microservicios Existentes + Nuevos Necesarios

**Fecha:** Noviembre 2025  
**Objetivo:** Completar 100% multi-framework SIN duplicar funcionalidades  
**Estrategia:** EXTENDER existentes (PROMPTS_01/02) + Crear solo lo absolutamente nuevo  
**Total Prompts:** 8 (5 extensiones + 3 nuevos)

**CRÍTICO:** Este documento CONSOLIDA con PROMPTS_01 y PROMPTS_02 para evitar duplicados.

---

## 🔍 ANÁLISIS DUPLICADOS DETECTADOS

### **SOLAPAMIENTOS IDENTIFICADOS:**

| PROMPTS_10 Original | Micro Existente | Acción |
|---------------------|-----------------|--------|
| leka-pii-discovery-advanced (8050) | leka-prompt-governance (8003) ya tiene Presidio | ❌ ELIMINAR - Extender 8003 |
| leka-privacy-notice-generator (8053) | leka-technical-documentation-generator (8008) | ❌ ELIMINAR - Extender 8008 |
| leka-gdpr-compliance-scorer (8051) | Múltiples micros con compliance | ❌ CONSOLIDAR - Nuevo micro agregador |
| leka-audit-report-generator (8033) | leka-technical-documentation-generator (8008) | ❌ ELIMINAR - Extender 8008 |

---

## 📋 PROMPTS CONSOLIDADOS

### **GRUPO PC1: EXTENSIONES MICROSERVICIOS EXISTENTES (5 prompts)**

---

## PROMPT PC1.1 - Extender leka-prompt-governance (GDPR PII Advanced)

**REVISAR:** PROMPTS_01 ya define extensiones para leka-prompt-governance (8003)

**Añadir a PROMPTS_01 Prompt A.3:**

```
ENDPOINT ADICIONAL:

POST /api/pii/scan-database-pii
Input: {
  "connection_string": "postgresql://...",
  "tables_to_scan": ["users", "customers", "orders"],  // o [] para todas
  "sample_size": 1000  // rows a sample por tabla
}
Output: {
  "pii_found": true,
  "tables_analyzed": 45,
  "tables_with_pii": [
    {
      "table": "users",
      "columns_with_pii": [
        {
          "column": "email",
          "pii_type": "EMAIL_ADDRESS",
          "samples_scanned": 1000,
          "pii_detected_percentage": 100%,
          "confidence": 0.99
        },
        {
          "column": "notes",
          "pii_type": "PERSON_NAME",
          "samples_scanned": 1000,
          "pii_detected_percentage": 23%,
          "confidence": 0.87
        }
      ],
      "total_pii_columns": 5,
      "gdpr_risk_level": "HIGH"
    }
  ],
  "overall_gdpr_risk": "HIGH",
  "recommendations": [
    "Encrypt column users.email",
    "Pseudonymize column users.notes",
    "Add data retention policy for users table"
  ]
}

Funcionalidad:
- Conectar a PostgreSQL
- Scan todas las columnas
- Sample N rows por columna
- Run Presidio analyzer
- Detectar PII types
- Calculate % rows con PII
- GDPR risk assessment

Librerías adicionales:
- psycopg2 o asyncpg (PostgreSQL)
- pandas (data sampling)
- Presidio (ya existe en micro)
```

**Esfuerzo adicional:** +1 día sobre PROMPTS_01 A.3  
**Prioridad:** 🔴 CRÍTICA (GDPR Art. 30 ROPA)

---

## PROMPT PC1.2 - Extender leka-technical-documentation-generator (GDPR + ISO Reports)

**REVISAR:** PROMPTS_02 ya define leka-technical-documentation-generator (8008)

**Añadir a PROMPTS_02 Prompt 1:**

```
ENDPOINTS ADICIONALES:

1. POST /api/documentation/generate-gdpr-privacy-notice
   Input: {
     "organization_info": {...},
     "processing_activities": [...],
     "legal_basis": "...",
     "data_retention": {...},
     "data_subject_rights": [...],
     "language": "es"
   }
   Output: {
     "privacy_notice_html": "...",
     "privacy_notice_pdf_url": "...",
     "gdpr_compliant": true,
     "articles_covered": ["Art. 13", "Art. 14"],
     "completeness_score": 1.0
   }
   
   Genera Privacy Notice según GDPR Art. 13-14.

2. POST /api/documentation/generate-iso42001-audit-report
   Input: {
     "audit_data": {...},
     "non_conformities": [...],
     "corrective_actions": [...],
     "audit_date": "2025-11-15"
   }
   Output: {
     "audit_report_pdf_url": "...",
     "iso42001_format": true,
     "completeness_score": 1.0
   }
   
   Genera Audit Report formato ISO 42001 Clause 9.2.

3. POST /api/documentation/generate-ropa
   Input: {
     "processing_activities": [...],
     "organization_info": {...}
   }
   Output: {
     "ropa_excel_url": "...",
     "ropa_pdf_url": "...",
     "gdpr_art_30_compliant": true,
     "activities_documented": 45
   }
   
   Genera ROPA (Records of Processing Activities) según GDPR Art. 30.

Templates adicionales:
- gdpr_privacy_notice_template_es.jinja2
- gdpr_privacy_notice_template_en.jinja2
- iso42001_audit_report_template.jinja2
- gdpr_ropa_template.jinja2
```

**Esfuerzo adicional:** +2 días sobre PROMPTS_02 Prompt 1  
**Prioridad:** 🔴 CRÍTICA

---

## PROMPT PC1.3 - Extender leka-llm-evaluation (ISO/OECD Scoring)

**REVISAR:** PROMPTS_01 ya define extensiones para leka-llm-evaluation (8002)

**Añadir a PROMPTS_01 Prompt A.2:**

```
ENDPOINT ADICIONAL:

POST /api/llm/calculate-oecd-trustworthiness-score
Input: {
  "model_id": "gpt_4_production",
  "evaluation_data": {
    "transparency_metrics": {...},
    "fairness_metrics": {...},
    "robustness_metrics": {...},
    "safety_metrics": {...},
    "accountability_metrics": {...}
  }
}
Output: {
  "oecd_trustworthiness_score": 0.89,  // Overall 0-1
  "principle_scores": {
    "inclusive_growth_sustainability": 0.82,
    "human_centered_fairness": 0.91,
    "transparency_explainability": 0.95,
    "robustness_security_safety": 0.87,
    "accountability": 0.92
  },
  "strengths": [
    "Transparency: 95% (explainability excellent)",
    "Accountability: 92% (logging comprehensive)"
  ],
  "weaknesses": [
    "Sustainability: 82% (no CO2 tracking)"
  ],
  "oecd_compliant": true,
  "recommendation": "EXCELENTE - Add sustainability metrics para 95%+"
}

Calcula score según 5 principios OECD AI Principles.
```

**Esfuerzo adicional:** +0.5 día sobre PROMPTS_01 A.2  
**Prioridad:** 🟡 MEDIA

---

## PROMPT PC1.4 - Extender leka-rag-evaluation (KB Sustainability)

**REVISAR:** PROMPTS_01 ya define extensiones para leka-rag-evaluation (8004)

**Añadir a PROMPTS_01 Prompt B.1:**

```
ENDPOINT ADICIONAL:

POST /api/rag/analyze-kb-sustainability
Input: {
  "vector_db_connection": {...},
  "index_name": "production_kb"
}
Output: {
  "sustainability_metrics": {
    "total_embeddings": 234556,
    "embedding_model": "text-embedding-3-large",
    "embedding_dimensions": 3072,
    "estimated_storage_gb": 2.8,
    "estimated_co2_kg": 0.34,  // Estimación embedding generation
    "optimization_potential": {
      "quantization_8bit": {"storage_reduction": 75%, "co2_reduction": 70%},
      "dimension_reduction_1536": {"storage_reduction": 50%, "quality_loss": 3%}
    }
  },
  "efficiency_score": 0.67,
  "recommendation": "OPTIMIZABLE - Quantization podría ahorrar 75% storage + 70% CO2"
}

Analiza eficiencia y sostenibilidad knowledge base.
```

**Esfuerzo adicional:** +0.5 día sobre PROMPTS_01 B.1  
**Prioridad:** 🟢 BAJA (sustainability nice-to-have)

---

## PROMPT PC1.5 - Extender leka-model-wrapper (ISO/OECD Benchmarking)

**REVISAR:** PROMPTS_01 ya define extensiones para leka-model-wrapper (8006)

**Añadir a PROMPTS_01 Prompt C.2:**

```
ENDPOINT ADICIONAL:

POST /api/model/calculate-iso42001-compliance-score
Input: {
  "model_id": "gpt_4_production",
  "assessment_type": "full",  // full, quick
  "annex_a_controls": ["A.6.1", "A.6.2", "A.6.3", "A.6.4", "A.6.5", "A.6.6"]  // Model controls
}
Output: {
  "iso42001_model_score": 0.91,
  "control_scores": {
    "A.6.1_development": 0.95,
    "A.6.2_validation": 0.92,
    "A.6.3_documentation": 0.89,
    "A.6.4_versioning": 1.0,
    "A.6.5_monitoring": 0.94,
    "A.6.6_explainability": 0.98
  },
  "gaps": [
    {"control": "A.6.3", "gap": "Training data provenance incomplete", "severity": "MEDIUM"}
  ],
  "certification_ready": true,
  "recommendation": "READY - Minor gaps no bloquean certificación ISO 42001"
}

Calcula compliance con ISO 42001 Annex A controles de modelos (A.6.1-A.6.6).
```

**Esfuerzo adicional:** +0.5 día sobre PROMPTS_01 C.2  
**Prioridad:** 🟡 MEDIA

---

### **GRUPO PC2: NUEVOS MICROSERVICIOS NECESARIOS (3 prompts)**

---

## PROMPT PC2.1 - NUEVO: leka-multi-framework-compliance-aggregator

**Puerto:** 8060  
**Objetivo:** Agregador de compliance scores de todos los frameworks.

**PROMPT:**

```
Necesito CREAR microservicio leka-multi-framework-compliance-aggregator que agrega scores de compliance de TODOS los frameworks (EU AI Act, ISO 42001, OECD, GDPR, etc.).

ESPECIFICACIONES:
- Nombre: leka-multi-framework-compliance-aggregator
- Puerto: 8060
- Framework: FastAPI
- Arquitectura: STATELESS

ENDPOINT PRINCIPAL:

POST /api/compliance/calculate-overall-score
Input: {
  "project_id": "uuid",
  "frameworks": ["eu_ai_act", "iso_42001", "oecd_principles", "gdpr", "iso_27001"]
}
Output: {
  "overall_compliance_score": 0.92,  // Weighted average
  "framework_scores": {
    "eu_ai_act": {
      "score": 0.95,
      "weight": 0.40,  // 40% weight (más crítico)
      "source": "Java API /api/compliance/eu-ai-act-score"
    },
    "iso_42001": {
      "score": 0.92,
      "weight": 0.25,
      "source": "leka-iso42001-annex-a-assessor:8030"
    },
    "oecd_principles": {
      "score": 0.91,
      "weight": 0.15,
      "source": "leka-llm-evaluation:8002/api/llm/calculate-oecd-score"
    },
    "gdpr": {
      "score": 0.90,
      "weight": 0.15,
      "source": "Java API + leka-prompt-governance:8003"
    },
    "iso_27001": {
      "score": 0.87,
      "weight": 0.05,
      "source": "Java API /api/compliance/iso27001-score"
    }
  },
  "certification_readiness": {
    "iso_42001": "READY",
    "iso_27001": "PARTIAL",
    "gdpr": "READY"
  },
  "gaps_critical": [
    {"framework": "ISO 42001", "gap": "Ethics committee not established", "severity": "MEDIUM"}
  ],
  "recommendation": "EXCELLENT - 92% overall, ready for ISO 42001 and GDPR certification"
}

FUNCIONALIDAD:
- Llama a múltiples microservicios y APIs Java
- Agrega scores con pesos configurables
- Identifica gaps críticos cross-framework
- Dashboard compliance general

LLAMADAS A OTROS MICROS:
- leka-iso42001-annex-a-assessor:8030
- leka-llm-evaluation:8002
- leka-prompt-governance:8003
- Java backend APIs

ENDPOINTS ADICIONALES:

1. GET /api/compliance/frameworks-supported
   - Lista frameworks soportados + endpoints

2. POST /api/compliance/export-compliance-report
   - Genera PDF multi-framework compliance
   - Include all scores, gaps, evidence

3. GET /api/compliance/certification-roadmap
   - Roadmap certificaciones por framework
   - Timeline estimado por gaps
```

**Esfuerzo:** 2 días  
**Prioridad:** 🔴 CRÍTICA  
**Artículos:** Aggregador multi-framework

---

## PROMPT PC2.2 - NUEVO: leka-iso42001-annex-a-assessor

**Puerto:** 8030  
**Objetivo:** Evaluador específico 39 controles Anexo A ISO 42001.

**PROMPT:**

```
Necesito CREAR microservicio leka-iso42001-annex-a-assessor para evaluar cobertura 39 controles Anexo A ISO 42001.

ESPECIFICACIONES:
- Nombre: leka-iso42001-annex-a-assessor
- Puerto: 8030
- Framework: FastAPI
- Arquitectura: STATELESS

ENDPOINT PRINCIPAL:

POST /api/iso42001/assess-annex-a
Input: {
  "project_id": "uuid",
  "include_evidence": true
}
Output: {
  "overall_compliance": 0.91,
  "controls_assessed": 39,
  "controls_compliant": 32,  // >90% score
  "controls_partial": 5,  // 50-90%
  "controls_non_compliant": 2,  // <50%
  "controls_by_category": {
    "organizational": 0.95,
    "people": 0.89,
    "physical": 1.0,
    "technological": 0.88,
    "data": 0.93,
    "model": 0.91,
    "system": 0.87
  },
  "detailed_results": [
    {
      "control_id": "A.1.1",
      "control_name": "AI policy",
      "category": "ORGANIZATIONAL",
      "score": 1.0,
      "evidence": [
        "/docs/governance/AI_POLICY_FRAMEWORK_ISO42001.md"
      ],
      "gaps": [],
      "status": "COMPLIANT"
    },
    {
      "control_id": "A.1.3",
      "control_name": "Roles and responsibilities",
      "category": "ORGANIZATIONAL",
      "score": 0.85,
      "evidence": [
        "AI Policy section 3",
        "AICOMPETENCE table"
      ],
      "gaps": [
        "Missing formal RACI matrix"
      ],
      "status": "PARTIAL"
    }
    // ... 37 más
  ],
  "certification_ready": false,
  "estimated_gap_closure": "3-4 weeks",
  "recommendation": "NEAR READY - 7 gaps identificados, effort 3-4 weeks"
}

FUNCIONALIDAD:
- Llama Java API para obtener evidencias
- Evalúa cada uno de los 39 controles
- Scoring automático basado en evidencias
- Gap identification
- Certification readiness assessment

LLAMADAS A:
- Java API /api/governance/iso42001-controls (tabla ICOISO42001CONTROL)
- Java API /api/projects/{id} (para metadata)
- leka-llm-evaluation (para métricas modelos)
- leka-prompt-governance (para métricas datos/prompts)

ENDPOINTS ADICIONALES:

1. POST /api/iso42001/assess-control-specific
   - Evalúa un control específico (ej: A.6.3)
   
2. POST /api/iso42001/generate-gap-closure-plan
   - Genera plan acción para cerrar gaps
   
3. GET /api/iso42001/controls-reference
   - Lista 39 controles + descripción
```

**Esfuerzo:** 2 días  
**Prioridad:** 🔴 CRÍTICA  
**Artículos:** ISO 42001 Annex A

---

## PROMPT PC2.3 - NUEVO: leka-board-governance-calculator

**Puerto:** 8061  
**Objetivo:** Calcular métricas board-level ISO 38507 EDM (Evaluate-Direct-Monitor).

**PROMPT:**

```
Necesito CREAR microservicio leka-board-governance-calculator para calcular métricas board-level según ISO 38507.

ESPECIFICACIONES:
- Nombre: leka-board-governance-calculator
- Puerto: 8061
- Framework: FastAPI
- Arquitectura: STATELESS

ENDPOINT PRINCIPAL:

POST /api/board/calculate-edm-metrics
Input: {
  "period": "Q4_2025",
  "organization_id": "uuid"
}
Output: {
  "evaluate_metrics": {
    "ai_proposals_submitted": 12,
    "ai_proposals_approved": 9,
    "approval_rate": 0.75,
    "ai_investment_total_eur": 450000,
    "ai_roi_expected": 2.3
  },
  "direct_metrics": {
    "ai_policies_active": 5,
    "ai_objectives_defined": 23,
    "ai_strategy_alignment_score": 0.87,
    "ai_risk_appetite": "MODERATE"
  },
  "monitor_metrics": {
    "ai_systems_production": 15,
    "ai_compliance_score": 0.92,
    "ai_incidents_reported": 3,
    "ai_performance_vs_objectives": 0.89,
    "ai_ethics_issues": 1
  },
  "overall_health_score": 0.88,
  "iso38507_compliant": true,
  "board_dashboard_ready": true,
  "recommendation": "HEALTHY - All EDM metrics within targets"
}

FUNCIONALIDAD:
- Llama Java APIs para datos governance
- Calcula métricas EDM (Evaluate-Direct-Monitor)
- Scoring ponderado
- Trend analysis si datos históricos

LLAMADAS A:
- Java API /api/governance/board-reports (tabla BRDBOARDREPORT)
- Java API /api/governance/ai-objectives (tabla AIMOBJECTIVES)
- Java API /api/projects (para count sistemas en producción)
- Java API /api/incidents (para count incidentes)

ENDPOINTS ADICIONALES:

1. POST /api/board/generate-executive-summary
   - Resumen ejecutivo para board (1 slide)
   
2. POST /api/board/calculate-health-score
   - Overall AI health score weighted
```

**Esfuerzo:** 1.5 días  
**Prioridad:** 🟡 MEDIA (enterprise/public)  
**Artículos:** ISO 38507 EDM model

---

## PROMPT PC2.4 - NUEVO: leka-sustainability-metrics

**Puerto:** 8062  
**Objetivo:** Métricas sostenibilidad (CO2, energy, water) para IA según OECD.

**PROMPT:**

```
Necesito CREAR microservicio leka-sustainability-metrics para calcular métricas de sostenibilidad de sistemas IA según OECD sustainability principles.

ESPECIFICACIONES:
- Nombre: leka-sustainability-metrics
- Puerto: 8062
- Framework: FastAPI
- Arquitectura: STATELESS

ENDPOINT PRINCIPAL:

POST /api/sustainability/calculate-ai-footprint
Input: {
  "model_info": {
    "model_size_parameters": 70000000000,  // 70B parameters
    "training_compute_flops": 1.2e24,
    "training_duration_hours": 1200,
    "hardware": "A100_gpus",
    "gpu_count": 256
  },
  "inference_info": {
    "monthly_requests": 1500000,
    "avg_tokens_per_request": 350,
    "hardware": "T4_gpus",
    "gpu_count": 8
  },
  "data_storage_gb": 5000,  // Knowledge base, logs, etc.
  "region": "eu_west_1"  // Para carbon intensity
}
Output: {
  "carbon_footprint": {
    "training_co2_kg": 12500,
    "inference_monthly_co2_kg": 340,
    "storage_monthly_co2_kg": 15,
    "total_lifetime_co2_kg": 18920,
    "equivalent": "95 round-trip flights Madrid-Barcelona"
  },
  "energy_consumption": {
    "training_kwh": 45600,
    "inference_monthly_kwh": 1240,
    "storage_monthly_kwh": 55,
    "total_monthly_kwh": 1295
  },
  "water_usage": {
    "training_liters": 678000,  // Datacenter cooling
    "inference_monthly_liters": 18500,
    "total_monthly_liters": 18500
  },
  "sustainability_score": 0.67,  // 0-1, higher = more sustainable
  "optimization_recommendations": [
    "Use quantized model (4-bit) → -60% energy",
    "Reduce inference batch size → -20% latency, -15% energy",
    "Move to renewable energy datacenter → -80% CO2"
  ],
  "oecd_sustainability_compliant": true,
  "recommendation": "MODERATE - Optimizations available, consider quantization"
}

FUNCIONALIDAD:
- Estimación CO2 basado en modelo, hardware, región
- Energy consumption calculations
- Water usage (datacenter cooling)
- Optimization suggestions
- Equivalencias comprensibles (flights, trees, etc.)

CÁLCULOS:
- ML CO2 Impact calculator (CodeCarbon library)
- Carbon intensity por región (electricitymap.org API)
- GPU power consumption (TDP tables)
- Datacenter PUE (Power Usage Effectiveness)

ENDPOINTS ADICIONALES:

1. POST /api/sustainability/compare-models
   - Compara footprint 2+ modelos
   
2. POST /api/sustainability/calculate-rag-footprint
   - Footprint específico RAG (embeddings + storage)
   
3. POST /api/sustainability/optimization-simulator
   - Simula impacto optimizaciones (quantization, pruning, etc.)
```

**Esfuerzo:** 2 días  
**Prioridad:** 🟢 BAJA (ESG, nice-to-have)  
**Artículos:** OECD Principle 1 (Sustainability)

---

## PROMPT PC2.5 - NUEVO: leka-ethics-risk-assessor

**Puerto:** 8063  
**Objetivo:** Evaluación riesgos éticos y derechos fundamentales (ISO 38507 + OECD).

**PROMPT:**

```
Necesito CREAR microservicio leka-ethics-risk-assessor para evaluar riesgos éticos y impacto derechos fundamentales según ISO 38507 y OECD.

ESPECIFICACIONES:
- Nombre: leka-ethics-risk-assessor
- Puerto: 8063
- Framework: FastAPI
- Arquitectura: STATELESS

ENDPOINT PRINCIPAL:

POST /api/ethics/assess-risks
Input: {
  "ai_system_description": "Patient triage AI for emergency room...",
  "deployment_context": {
    "sector": "healthcare",
    "user_demographics": ["patients", "elderly", "children"],
    "decision_type": "prioritization",
    "consequences": "Health outcomes, wait times"
  },
  "intended_use": "Emergency room patient prioritization",
  "geographic_scope": "EU"
}
Output: {
  "ethics_risk_score": 0.67,  // 0-1, higher = more ethical concerns
  "charter_articles_affected": [
    {
      "article": "Art. 3 - Right to integrity of the person",
      "relevance": "HIGH",
      "potential_impact": "Misclassification could delay critical care",
      "severity": "CRITICAL"
    },
    {
      "article": "Art. 21 - Non-discrimination",
      "relevance": "HIGH",
      "potential_impact": "Bias in prioritization by demographics",
      "severity": "HIGH"
    },
    {
      "article": "Art. 35 - Health care",
      "relevance": "HIGH",
      "potential_impact": "Access to timely healthcare",
      "severity": "HIGH"
    }
  ],
  "vulnerable_groups_affected": ["elderly", "children", "non_spanish_speakers"],
  "ethical_concerns": [
    {
      "concern": "Algorithmic bias in emergency prioritization",
      "severity": "CRITICAL",
      "mitigation_required": true,
      "recommendations": [
        "Mandatory human oversight for all triage decisions",
        "Regular fairness audits by demographics",
        "Override capability for medical staff"
      ]
    }
  ],
  "oecd_principles_assessment": {
    "human_centered_values": 0.72,  // Concerns detected
    "fairness": 0.68,  // Bias risk
    "transparency": 0.85,
    "safety": 0.91,
    "accountability": 0.88
  },
  "iso38507_ethics_compliant": false,  // Requiere ethics committee review
  "fria_required": true,  // Art. 27 if public authority
  "recommendation": "HIGH ETHICS RISK - Mandatory ethics committee review + FRIA"
}

FUNCIONALIDAD:
- NLP analysis de descripción sistema
- Mapping automático: caso uso → Charter articles
- Identificación grupos vulnerables
- Scoring ético por OECD principles
- Recomendaciones mitigación

INTEGRACIONES:
- Puede enriquecer FRIA (leka-fria-generator:8012)
- Alimenta ethics committee decisions (Java)

ENDPOINTS ADICIONALES:

1. POST /api/ethics/map-charter-articles
   - Mapeo automático caso uso → EU Charter articles
   
2. POST /api/ethics/identify-vulnerable-groups
   - NLP identification grupos vulnerables en descripción
```

**Esfuerzo:** 2 días  
**Prioridad:** 🟡 MEDIA (enterprise/public)  
**Artículos:** ISO 38507 Ethics, OECD Principle 2, Art. 27 FRIA support

---

## 📊 RESUMEN CONSOLIDADO PYTHON

### **EXTENSIONES (Añadir a PROMPTS_01/02):**

| Micro Base | Extensión | Esfuerzo Adicional | Documento Base |
|------------|-----------|-------------------|----------------|
| leka-prompt-governance (8003) | GDPR PII Database Scan | +1 día | PROMPTS_01 A.3 |
| leka-technical-documentation-generator (8008) | GDPR + ISO Reports | +2 días | PROMPTS_02 Prompt 1 |
| leka-llm-evaluation (8002) | OECD Trustworthiness Score | +0.5 día | PROMPTS_01 A.2 |
| leka-rag-evaluation (8004) | KB Sustainability | +0.5 día | PROMPTS_01 B.1 |
| leka-model-wrapper (8006) | ISO 42001 Model Compliance | +0.5 día | PROMPTS_01 C.2 |

**Subtotal extensiones:** +4.5 días sobre PROMPTS_01/02 existentes

---

### **NUEVOS MICROSERVICIOS (Necesarios):**

| Micro Nuevo | Puerto | Propósito | Esfuerzo |
|-------------|--------|-----------|----------|
| leka-multi-framework-compliance-aggregator | 8060 | Agregador compliance multi-framework | 2 días |
| leka-iso42001-annex-a-assessor | 8030 | Evaluador 39 controles Anexo A | 2 días |
| leka-board-governance-calculator | 8061 | Métricas board EDM | 1.5 días |
| leka-sustainability-metrics | 8062 | CO2, energy, water footprint | 2 días |
| leka-ethics-risk-assessor | 8063 | Ethics + fundamental rights | 2 días |

**Subtotal nuevos:** 9.5 días

---

## 📊 TOTAL PYTHON MULTI-FRAMEWORK:

| Categoría | Items | Esfuerzo |
|-----------|-------|----------|
| **PROMPTS_01 (existentes)** | 7 extensiones | 16-18 días |
| **PROMPTS_02 (nuevos)** | 5 micros nuevos | 12-15 días |
| **PROMPTS_10 extensiones** | 5 extensiones | 4.5 días |
| **PROMPTS_10 nuevos** | 5 micros nuevos | 9.5 días |
| **TOTAL** | **22 micros (7 ext + 10 nuevos + 5 ext + 5 nuevos)** | **42-47 días** |

**Con 3 chats paralelos:** ~14-16 días reales

---

## ✅ ESTRATEGIA CONSOLIDADA:

### **FASE 1 (YA EN DESARROLLO según tu mensaje):**
- PROMPTS_01 (7 extensiones micros existentes)
- PROMPTS_02 (5 micros nuevos EU AI Act)

### **FASE 2 (Post-lanzamiento):**
- PROMPTS_10 extensiones (5 extensiones adicionales)
- PROMPTS_10 nuevos (5 micros multi-framework)

---

## 🚀 RECOMENDACIÓN FINAL:

**NO uses PROMPTS_10 original** (tiene duplicados).

**SÍ usa:**
1. **PROMPTS_01** (ya en desarrollo) ✅
2. **PROMPTS_02** (ya en desarrollo) ✅
3. **PROMPTS_10_CONSOLIDADO** (este documento) - Para Fase 2 multi-framework

**Resultado:** 
- Evitas duplicados
- Aprovechas micros existentes
- Solo 5 micros nuevos necesarios (vs 13 original)
- Esfuerzo: 9.5 días nuevos + 4.5 días extensiones = **14 días** (vs 23 días original)

---

**¿Procedo a actualizar README con esta consolidación?** 🚀

