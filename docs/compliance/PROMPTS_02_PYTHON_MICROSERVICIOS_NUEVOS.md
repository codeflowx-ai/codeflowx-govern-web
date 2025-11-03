# PROMPTS - CREACIÓN NUEVOS MICROSERVICIOS PYTHON
## EU AI ACT COMPLIANCE - Microservicios Especializados

**Equipo:** Python Team - Nuevos Microservicios  
**Fecha:** 15 de noviembre de 2025  
**Objetivo:** Crear 3-4 microservicios Python nuevos para funcionalidades específicas EU AI Act  
**Esfuerzo Estimado:** 8-10 días (con 2-3 chats en paralelo)

---

## 🏗️ PATRÓN DE MICROSERVICIO ESTÁNDAR

**Todos los nuevos microservicios deben seguir:**

```
ESTRUCTURA BASE:
├── main.py (FastAPI app)
├── routers/ (endpoints organizados)
├── services/ (lógica de negocio)
├── models/ (Pydantic models)
├── utils/ (helpers)
├── tests/ (pytest)
├── requirements.txt
├── Dockerfile
├── k8s/ (deployment, service, configmap)
├── README.md
└── .env.example

CARACTERÍSTICAS:
- Framework: FastAPI
- Logging: structlog (JSON)
- Metrics: Prometheus
- Health: /health endpoint
- Docs: OpenAPI automática
- STATELESS: Sin base de datos propia
- Container: Docker multi-stage
- Orchestration: Kubernetes
```

---

## 📋 MICROSERVICIOS A CREAR

### **MICRO 1: leka-technical-documentation-generator** (Puerto 8008)

---

### **PROMPT 1 - Technical Documentation Generator**

**Objetivo:** Generar documentación técnica completa según Anexo IV del EU AI Act (9 secciones obligatorias).

**Prompt Específico:**

```
Necesito CREAR microservicio Python leka-technical-documentation-generator para generar documentación técnica de sistemas IA según Anexo IV del EU AI Act.

ESPECIFICACIONES:
- Nombre: leka-technical-documentation-generator
- Puerto: 8008
- Ubicación: /mnt/c/Users/ManuelGonzalez/git/leka-technical-documentation-generator
- Framework: FastAPI
- Arquitectura: STATELESS

FUNCIONALIDAD PRINCIPAL:
Generar documento técnico completo (Anexo IV - 9 secciones) a partir de metadata del proyecto/modelo.

ENDPOINT PRINCIPAL:

POST /api/documentation/generate-annex-iv
Input: {
  "project_metadata": {
    "project_name": "CustomerServiceAI",
    "provider_name": "Acme Corp",
    "version": "2.1.0",
    "purpose": "Automated customer service chatbot",
    "model_info": {
      "base_model": "gpt-4-turbo",
      "fine_tuned": true,
      "training_data_summary": {...},
      "architecture": "transformer"
    },
    "deployment_info": {...},
    "risk_assessment": {...},
    "human_oversight": {...}
  },
  "output_format": "markdown",  // markdown, pdf, html, json
  "language": "es"  // es, en
}
Output: {
  "document_id": "uuid",
  "generated_document": {
    "section_1_general_description": {
      "content": "...",
      "completeness_score": 0.95,
      "mandatory_elements": 8,
      "completed_elements": 8
    },
    "section_2_development_details": {
      "content": "...",
      "completeness_score": 0.89,
      "mandatory_elements": 8,
      "completed_elements": 7,
      "missing": ["cybersecurity_measures"]
    },
    // ... secciones 3-9
  },
  "completeness_overall": 0.91,  // 91% completo
  "missing_information": [
    {"section": 2, "element": "cybersecurity_measures", "severity": "HIGH"}
  ],
  "annex_iv_compliant": false,  // Falta info crítica
  "document_url": "https://...",  // Si se guarda en S3/storage
  "recommendation": "REVISAR - Completar cybersecurity_measures para compliance"
}

SECCIONES ANEXO IV A GENERAR:
1. Descripción general del sistema de IA (8 elementos)
2. Descripción detallada desarrollo (8 elementos)
3. Información supervisión, funcionamiento y control
4. Descripción parámetros de rendimiento
5. Descripción sistema gestión de riesgos (Art. 9)
6. Descripción cambios ciclo de vida
7. Lista normas armonizadas aplicadas
8. Copia declaración UE de conformidad
9. Sistema vigilancia poscomercialización

GENERACIÓN DE DOCUMENTO:
- Templates: Jinja2 (NO hardcoded en código - GAP user memory)
- Idiomas: ES, EN
- Formatos: Markdown, HTML, PDF (weasyprint)
- Personalización: Por sector, por tipo sistema

VALIDACIÓN:
- Completeness checker: verifica 100% elementos obligatorios
- Quality scoring: qué tan completo está cada sección
- Missing elements highlighting

REQUISITOS TÉCNICOS:
- Jinja2 para templates (almacenados en config/storage externo)
- weasyprint para PDF generation
- markdown2 para HTML
- pydantic para validación structure
- Integración con otros micros para obtener datos (opcional)

NO INCLUIR:
- Base de datos (stateless)
- Autenticación (gateway maneja)

ESTRUCTURA:
app/
├── main.py
├── routers/
│   └── documentation_router.py
├── services/
│   ├── annex_iv_generator.py
│   ├── template_service.py
│   └── validation_service.py
├── models/
│   └── documentation_models.py
├── templates/ (external storage reference)
└── utils/
    └── pdf_generator.py
```

**Artículos Cubiertos:** Art. 11, Anexo IV completo  
**Esfuerzo:** 3-4 días  
**Prioridad:** 🔴 CRÍTICA

---

### **MICRO 2: leka-conformity-assessment** (Puerto 8009)

---

### **PROMPT 2 - Conformity Assessment Processor**

**Objetivo:** Procesar evaluaciones de conformidad según Anexo VI (4 pasos) y generar reportes.

**Prompt Específico:**

```
Necesito CREAR microservicio Python leka-conformity-assessment para procesar evaluaciones de conformidad según Anexo VI del EU AI Act.

ESPECIFICACIONES:
- Nombre: leka-conformity-assessment
- Puerto: 8009
- Ubicación: /mnt/c/Users/ManuelGonzalez/git/leka-conformity-assessment
- Framework: FastAPI
- Arquitectura: STATELESS

ENDPOINT PRINCIPAL:

POST /api/conformity/assess-annex-vi
Input: {
  "project_id": "uuid",
  "assessment_type": "self_assessment",  // self_assessment, notified_body
  "qms_evidence": {
    "qms_established": bool,
    "qms_documentation_url": "...",
    "qms_certification": "ISO_9001"  // opcional
  },
  "technical_documentation": {
    "annex_iv_complete": bool,
    "documentation_url": "...",
    "completeness_score": 0.94
  },
  "design_process_evidence": {...},
  "post_market_monitoring_plan": {...}
}
Output: {
  "assessment_id": "uuid",
  "annex_vi_compliant": bool,
  "step_results": {
    "step_1_intro": {"status": "N/A"},
    "step_2_qms_check": {
      "status": "PASS",
      "qms_compliant": true,
      "evidence_validated": true,
      "score": 1.0
    },
    "step_3_technical_doc": {
      "status": "PASS",
      "doc_complete": true,
      "requirements_met": 28,
      "requirements_total": 30,
      "missing": ["cybersecurity_specific_measures", "change_management_detailed"],
      "score": 0.93
    },
    "step_4_consistency": {
      "status": "PASS",
      "design_process_consistent": true,
      "pms_plan_adequate": true,
      "score": 0.98
    }
  },
  "overall_score": 0.97,
  "compliance_gaps": [
    {"section": "technical_doc", "gap": "cybersecurity_specific_measures", "severity": "MEDIUM"}
  ],
  "ready_for_certification": true,
  "recommendation": "COMPLIANCE ALCANZADO - Gaps menores no bloquean certificación",
  "report_url": "https://..."  // Reporte generado PDF/HTML
}

FUNCIONALIDAD:
- Validador de evidencias (checks cada paso Anexo VI)
- Scoring system (0-1 por cada paso)
- Gap identification automática
- Report generation (PDF/HTML)
- Recomendaciones accionables

ENDPOINTS ADICIONALES:

1. POST /api/conformity/generate-checklist
   - Genera checklist interactivo Anexo VI

2. POST /api/conformity/validate-evidence
   - Valida evidencias proporcionadas (links, docs, etc.)

3. GET /api/conformity/annex-vi-template
   - Retorna template/guía Anexo VI

REQUISITOS TÉCNICOS:
- Pydantic validation
- Jinja2 templates
- PDF generation (weasyprint)
- Scoring algorithms
- NO persistencia (stateless)
```

**Artículos Cubiertos:** Art. 43, Anexo VI  
**Esfuerzo:** 2-3 días  
**Prioridad:** 🟡 Media-Alta

---

### **MICRO 3: leka-eu-declaration-generator** (Puerto 8010)

---

### **PROMPT 3 - EU Declaration Generator**

**Objetivo:** Generar Declaraciones UE de Conformidad según Anexo V (8 elementos obligatorios).

**Prompt Específico:**

```
Necesito CREAR microservicio Python leka-eu-declaration-generator para generar Declaraciones UE de Conformidad según Anexo V del EU AI Act.

ESPECIFICACIONES:
- Nombre: leka-eu-declaration-generator
- Puerto: 8010
- Ubicación: /mnt/c/Users/ManuelGonzalez/git/leka-eu-declaration-generator
- Framework: FastAPI
- Arquitectura: STATELESS

ENDPOINT PRINCIPAL:

POST /api/declaration/generate-eu-conformity
Input: {
  "ai_system_info": {
    "name": "CustomerServiceAI",
    "type": "conversational_agent",
    "unique_reference": "CS-AI-2024-001",
    "version": "2.1.0"
  },
  "provider_info": {
    "name": "Acme Corporation",
    "address": "Calle Mayor 123, 28001 Madrid, España",
    "authorized_representative": {...}  // opcional
  },
  "conformity_info": {
    "conforms_to_ai_act": true,
    "conforms_to_gdpr": true,
    "other_regulations": ["ISO_42001", "ISO_27001"]
  },
  "harmonized_standards": ["EN_ISO_9001", "EN_ISO_27001"],
  "notified_body": {
    "name": "TÜV SÜD",
    "id": "NB-1234",
    "certificate_number": "CERT-2025-001",
    "certificate_date": "2025-11-01"
  },  // opcional si self-assessment
  "signatory": {
    "name": "Juan García",
    "position": "CEO",
    "location": "Madrid",
    "date": "2025-11-15"
  },
  "output_format": "pdf"  // pdf, html, json
}
Output: {
  "declaration_id": "uuid",
  "annex_v_compliant": true,
  "generated_declaration": {
    "element_1_system_identification": "✅ Complete",
    "element_2_provider_info": "✅ Complete",
    "element_3_responsibility_statement": "✅ Complete",
    "element_4_conformity_declaration": "✅ Complete",
    "element_5_gdpr_statement": "✅ Complete",
    "element_6_standards_references": "✅ Complete",
    "element_7_notified_body": "N/A (self-assessment)",
    "element_8_signature_info": "✅ Complete"
  },
  "completeness_score": 1.0,
  "missing_mandatory": [],
  "declaration_text": "...",  // Texto completo declaración
  "declaration_url": "https://...",  // PDF generado
  "digital_signature_ready": true,  // Si tiene hash para firmar
  "document_hash": "sha256:a3f5d8...",  // Para firma digital
  "recommendation": "READY - Declaración completa, lista para firma"
}

GENERACIÓN DECLARACIÓN:
- Template oficial Anexo V
- Multi-idioma (ES, EN, FR, DE)
- Formato oficial UE
- Campos obligatorios validados
- PDF con formato profesional
- Hash SHA-256 para firma digital

ENDPOINTS ADICIONALES:

1. POST /api/declaration/validate
   - Valida declaración existente vs Anexo V

2. GET /api/declaration/template
   - Retorna template Anexo V (JSON schema)

3. POST /api/declaration/generate-hash
   - Genera hash para firma digital externa

REQUISITOS TÉCNICOS:
- Jinja2 templates (Anexo V oficial)
- weasyprint (PDF generation)
- pydantic validation (8 elementos)
- Multi-idioma support
- Digital signature preparation (hashlib)
- NO persistencia

FORMATO PDF:
- Profesional, estilo oficial UE
- Logo UE (si permitido)
- Formato A4
- Firma digital placeholder
```

**Artículos Cubiertos:** Art. 47, Anexo V  
**Esfuerzo:** 2 días  
**Prioridad:** 🔴 Crítica

---

### **MICRO 4: leka-fria-generator** (Puerto 8012)

---

### **PROMPT 4 - FRIA Generator (Fundamental Rights Impact Assessment)**

**Objetivo:** Generar evaluaciones de impacto en derechos fundamentales según Art. 27.

**Prompt Específico:**

```
Necesito CREAR microservicio Python leka-fria-generator para generar Evaluaciones de Impacto en Derechos Fundamentales (FRIA) según Art. 27 del EU AI Act.

ESPECIFICACIONES:
- Nombre: leka-fria-generator
- Puerto: 8012
- Ubicación: /mnt/c/Users/ManuelGonzalez/git/leka-fria-generator
- Framework: FastAPI
- Arquitectura: STATELESS

ENDPOINT PRINCIPAL:

POST /api/fria/generate-assessment
Input: {
  "deployer_info": {
    "organization": "Hospital Regional Madrid",
    "type": "public_authority",  // public_authority, private_providing_public_services
    "contact": {...}
  },
  "ai_system_info": {
    "name": "Patient Triage AI",
    "purpose": "Emergency patient prioritization",
    "provider": "HealthTech SL",
    "annex_iii_category": "III.5.d",  // Categoría alto riesgo
    "version": "1.2.0"
  },
  "deployment_details": {
    "processes": "Emergency room patient intake and triage",
    "usage_period": "Continuous",
    "usage_frequency": "500-800 patients/day",
    "affected_categories": ["emergency_patients", "elderly", "children"],
    "geographic_scope": "Madrid community hospitals"
  },
  "risk_analysis": {
    "identified_risks": [
      {
        "risk": "Misclassification of emergency severity",
        "affected_groups": ["elderly", "non_spanish_speakers"],
        "severity": "HIGH",
        "probability": "MEDIUM",
        "impact": "Delayed treatment, potential health harm"
      }
    ]
  },
  "human_oversight": {
    "measures": "Nurse reviews all AI triage decisions",
    "override_capability": true,
    "training_provided": true
  },
  "mitigation_measures": {
    "preventive": [...],
    "detective": [...],
    "corrective": [...]
  }
}
Output: {
  "fria_id": "uuid",
  "art_27_compliant": true,
  "generated_fria": {
    "element_a_process_description": {
      "content": "...",
      "complete": true,
      "quality_score": 0.94
    },
    "element_b_time_frequency": {
      "content": "Continuous usage, 500-800 patients/day",
      "complete": true,
      "quality_score": 1.0
    },
    "element_c_affected_categories": {
      "content": "Emergency patients including elderly, children, non-spanish speakers...",
      "vulnerable_groups_identified": true,
      "complete": true,
      "quality_score": 0.96
    },
    "element_d_specific_risks": {
      "content": "...",
      "risks_count": 3,
      "severity_analysis": {"HIGH": 1, "MEDIUM": 2},
      "complete": true,
      "quality_score": 0.89
    },
    "element_e_human_oversight": {
      "content": "...",
      "hitl_configured": true,
      "complete": true,
      "quality_score": 0.92
    },
    "element_f_mitigation_measures": {
      "content": "...",
      "measures_count": 8,
      "complete": true,
      "quality_score": 0.87
    }
  },
  "completeness_score": 0.93,
  "mandatory_elements": 6,
  "completed_elements": 6,
  "quality_overall": 0.91,
  "fundamental_rights_analysis": {
    "charter_articles_affected": [
      "Art. 3 (Right to integrity)",
      "Art. 21 (Non-discrimination)",
      "Art. 35 (Health care)"
    ],
    "severity_assessment": "HIGH_IMPACT"
  },
  "fria_document_url": "https://...",
  "ready_for_authority_notification": true,
  "recommendation": "READY - FRIA completo, listo para notificar autoridad vigilancia"
}

FUNCIONALIDADES ADICIONALES:

1. POST /api/fria/integrate-with-dpia
   - Integra FRIA con DPIA (Data Protection Impact Assessment - GDPR)
   - Genera documento unificado FRIA+DPIA

2. POST /api/fria/analyze-fundamental-rights
   - Analiza qué artículos Carta Derechos Fundamentales UE aplican
   - Input: descripción sistema
   - Output: lista artículos Charter afectados

3. GET /api/fria/template
   - Template FRIA según Art. 27.5 (Oficina IA)

REQUISITOS TÉCNICOS:
- Jinja2 templates (FRIA oficial)
- NLP para análisis de riesgos (spaCy)
- Mapping automático: caso uso → Charter articles
- PDF generation professional
- Multi-idioma
- Cuestionario interactivo (template Oficina IA)
- NO persistencia

TEMPLATES:
- Almacenar en config externo (no hardcoded)
- Personalizable por sector
- Compatible con template Oficina IA UE (Art. 27.5)
```

**Artículos Cubiertos:** Art. 27, integración con Anexo IX  
**Esfuerzo:** 3 días  
**Prioridad:** 🔴 CRÍTICA

---

### **MICRO 5: leka-copyright-compliance** (Puerto 8013)

---

### **PROMPT 5 - Copyright Compliance Checker**

**Objetivo:** Verificar cumplimiento de copyright en datos de entrenamiento GPAI según Art. 53.1.c.

**Prompt Específico:**

```
Necesito CREAR microservicio Python leka-copyright-compliance para verificar cumplimiento de copyright en datos de entrenamiento según EU AI Act Art. 53.1.c y Directiva 2019/790.

ESPECIFICACIONES:
- Nombre: leka-copyright-compliance
- Puerto: 8013
- Ubicación: /mnt/c/Users/ManuelGonzalez/git/leka-copyright-compliance
- Framework: FastAPI
- Arquitectura: STATELESS

ENDPOINT PRINCIPAL:

POST /api/copyright/check-training-data-compliance
Input: {
  "training_data_sources": [
    {
      "source_type": "web_scraping",
      "url": "https://example.com/content",
      "scraped_at": "2025-01-15",
      "content_type": "articles"
    },
    {
      "source_type": "dataset",
      "dataset_name": "CommonCrawl",
      "version": "2024-10",
      "license": "CC-BY-4.0"
    },
    {
      "source_type": "licensed_content",
      "provider": "Reuters",
      "license_agreement": "TDM_License_2024",
      "expiry_date": "2026-12-31"
    }
  ],
  "model_type": "GPAI",  // GPAI, specific
  "intended_use": "text_generation"
}
Output: {
  "compliance_score": 0.87,
  "compliant": true,
  "source_analysis": [
    {
      "source": "https://example.com/content",
      "compliance_status": "COMPLIANT",
      "license_detected": "CC-BY-4.0",
      "tdm_opt_out_checked": true,
      "tdm_opt_out_found": false,  // No opt-out = OK usar
      "robots_txt_compliant": true,
      "risk_level": "LOW"
    },
    {
      "source": "CommonCrawl",
      "compliance_status": "COMPLIANT",
      "license": "CC-BY-4.0",
      "attribution_required": true,
      "risk_level": "LOW"
    },
    {
      "source": "Reuters TDM_License_2024",
      "compliance_status": "VALID",
      "license_expiry": "2026-12-31",
      "days_until_expiry": 413,
      "risk_level": "LOW"
    }
  ],
  "opt_out_sources": [],  // Fuentes con TDM opt-out (NO USAR)
  "unlicensed_sources": [],
  "expired_licenses": [],
  "high_risk_sources": [],
  "directive_2019_790_compliant": true,
  "attribution_requirements": [
    {"source": "CommonCrawl", "attribution": "CC-BY-4.0", "text": "Data includes..."}
  ],
  "recommendation": "COMPLIANT - Todas las fuentes válidas, verificar expiry en 400 días"
}

FUNCIONALIDADES:

1. Detección licencias automática:
   - Web scraping de license info
   - Robots.txt parsing
   - TDM (Text and Data Mining) opt-out detection
   - Creative Commons detection

2. Validación TDM Directive 2019/790:
   - Art. 4.3 opt-out reservation detection
   - Machine-readable opt-out (meta tags, robots.txt)

3. License tracking:
   - Expiry warnings
   - Attribution requirements
   - Commercial vs non-commercial use

ENDPOINTS ADICIONALES:

1. POST /api/copyright/detect-opt-out
   - Detecta TDM opt-out en URLs/contenido

2. POST /api/copyright/generate-attribution
   - Genera texto de atribución para datos usados

3. POST /api/copyright/validate-license
   - Valida compatibilidad licencias múltiples

REQUISITOS TÉCNICOS:
- requests, beautifulsoup (web scraping)
- license-expression library
- robots.txt parser
- Metadata extraction
- NO persistencia

IMPORTANTE:
- Respetar robots.txt
- Rate limiting en scraping
- Cache responses (in-memory)
```

**Artículos Cubiertos:** Art. 53.1.c (GPAI), Directiva 2019/790  
**Esfuerzo:** 2-3 días  
**Prioridad:** 🟡 Media (Fase II GPAI)

---

## 📊 RESUMEN PROMPTS - NUEVOS MICROSERVICIOS PYTHON

| Prompt | Microservicio | Puerto | Funcionalidad | Esfuerzo | Prioridad |
|--------|---------------|--------|---------------|----------|-----------|
| **1** | leka-technical-documentation-generator | 8008 | Generador Anexo IV (9 secciones) | 3-4 días | 🔴 Crítica |
| **2** | leka-conformity-assessment | 8009 | Evaluación conformidad Anexo VI | 2-3 días | 🟡 Media-Alta |
| **3** | leka-eu-declaration-generator | 8010 | Declaración UE Anexo V | 2 días | 🔴 Crítica |
| **4** | leka-fria-generator | 8012 | FRIA Art. 27 (6 elementos) | 3 días | 🔴 Crítica |
| **5** | leka-copyright-compliance | 8013 | Copyright compliance GPAI | 2-3 días | 🟡 Media |

**TOTAL ESFUERZO:** 12-15 días  
**CON 3 CHATS PARALELOS:** 4-5 días reales  
**CON 5 CHATS PARALELOS:** 2-3 días reales

---

## 🎯 DISTRIBUCIÓN TRABAJO PARALELO RECOMENDADA

### **CHAT A - Documentation Generators (Crítico):**
- Prompt 1 (leka-technical-documentation-generator)
- Prompt 3 (leka-eu-declaration-generator)
- **Esfuerzo:** 5-6 días

### **CHAT B - Compliance Assessment:**
- Prompt 2 (leka-conformity-assessment)
- Prompt 4 (leka-fria-generator)
- **Esfuerzo:** 5-6 días

### **CHAT C - GPAI & Copyright (Fase II):**
- Prompt 5 (leka-copyright-compliance)
- **Esfuerzo:** 2-3 días

**Timeline con 3 chats:** 5-6 días reales

---

## 📋 PLANTILLA ESTÁNDAR MICROSERVICIO

**Cada nuevo microservicio debe tener:**

```
README.md con:
- Descripción funcionalidad
- Artículos EU AI Act cubiertos
- Endpoints disponibles
- Ejemplos de uso
- Setup instructions
- Docker/K8s deployment

requirements.txt con:
- fastapi
- uvicorn[standard]
- pydantic
- structlog
- prometheus-client
- pytest
- + librerías específicas

Dockerfile:
- Multi-stage build
- Python 3.11+
- Non-root user
- Health check

k8s/:
- deployment.yaml
- service.yaml
- configmap.yaml (para templates externos)

tests/:
- test_endpoints.py
- test_services.py
- > 80% coverage
```

---

## ⚠️ CONSIDERACIONES ESPECIALES

### **Templates Externos (User Memory):**
```
REQUISITO: Templates NO hardcoded en código

SOLUCIÓN:
- Templates en ConfigMaps K8s
- O templates en S3/storage externo
- Cargar dinámicamente en runtime
- Permite modificar sin redeployar
```

### **Multi-idioma:**
```
Soportar mínimo:
- Español (ES)
- Inglés (EN)

Templates por idioma:
- annex_iv_template_es.jinja2
- annex_iv_template_en.jinja2
- annex_v_template_es.jinja2
- etc.
```

### **PDF Generation Quality:**
```
Usar weasyprint:
- CSS profesional
- Fonts embebidos
- Headers/footers
- Page numbers
- Table of contents automático
- Bookmarks PDF
```

---

**Fin Documento 2 de 5**

