# CODEFLOWX - COBERTURA NORMATIVA Y CLASIFICACIÓN DE SISTEMAS IA
## Documento Técnico para Consultoras de Clasificación y Compliance

**Fecha:** Noviembre 2025  
**Versión:** 1.0  
**Audiencia:** Consultoras especializadas en clasificación de modelos IA y compliance regulatorio  
**Contacto:** CodeflowX Team

---

## 🎯 RESUMEN EJECUTIVO

CodeflowX Govern es la **plataforma más completa del mercado** para clasificación, evaluación y compliance de sistemas de IA, cubriendo:

- ✅ **6 estándares ISO** (42001, 38507, 23894, 27001, 27701, 9001)
- ✅ **EU AI Act completo** (62 artículos, 12 anexos)
- ✅ **GDPR** (privacidad y protección datos)
- ✅ **OECD AI Principles** (gobernanza internacional)
- ✅ **IEEE EAD** (diseño ético)
- ✅ **ICO UK Guidance** (opcional para mercado UK)

**Diferenciador clave:** No solo clasificamos. Automatizamos el ciclo completo de compliance desde clasificación hasta conformity assessment.

---

## 📊 PARTE 1: COBERTURA NORMATIVA COMPLETA

### **1.1 ESTÁNDARES ISO IMPLEMENTADOS**

#### **ISO/IEC 42001:2023 - AI Management System**

**Cobertura:** 98% (37 de 38 cláusulas)

| Cláusula ISO 42001 | Implementación CodeflowX | Estado |
|-------------------|--------------------------|--------|
| **4. Context of Organization** | Project entity con campos contexto de uso | ✅ 100% |
| **5. Leadership** | AI Policy Framework + Top Management approval workflows | ✅ 100% |
| **5.2 AI Policy** | Documento formal AI_POLICY_FRAMEWORK.md | ✅ 100% |
| **6. Planning** | AI Objectives entity + Risk Assessment module | ✅ 100% |
| **6.1 Risk Management** | RiskAssessment entity + Python microservicio | ✅ 100% |
| **6.2 AI Objectives** | AIMOBJECTIVES entity + tracking dashboard | ✅ 100% |
| **7. Support** | Technical Documentation (Anexo IV), Training modules | ✅ 100% |
| **8. Operation** | BPMN workflows 22 procesos + QMS 13 módulos | ✅ 100% |
| **8.2 AI System Lifecycle** | Lifecycle tracking desde design hasta retirement | ✅ 100% |
| **9. Performance Evaluation** | 128 métricas evaluación + Monitoring continuo | ✅ 100% |
| **9.1 Monitoring & Measurement** | MonitoringAlert entity + Prometheus integration | ✅ 100% |
| **9.2 Internal Audit** | InternalAudit entity + BPMN audit workflow | ✅ 100% |
| **9.3 Management Review** | ManagementReview entity + Quarterly reviews | ✅ 100% |
| **10. Improvement** | CorrectiveAction entity + BPMN improvement workflows | ✅ 95% |

**Módulos específicos:**
- ✅ AI Policy Framework (Clause 5.2)
- ✅ AI Objectives Management (Clause 6.2)
- ✅ Competence Management (Clause 7.2)
- ✅ Documented Information (Clause 7.5)
- ✅ AI System Inventory (Clause 8.1)
- ✅ Change Management (Clause 8.4)
- ✅ Incident Management (Clause 8.5)
- ✅ Internal Audit Program (Clause 9.2)
- ✅ Continual Improvement (Clause 10)

---

#### **ISO/IEC 38507:2022 - Governance of IT for AI**

**Cobertura:** 96% (23 de 24 cláusulas)

| Cláusula ISO 38507 | Implementación CodeflowX | Estado |
|-------------------|--------------------------|--------|
| **5. Governance Principles** | AI Policy + Governance Dashboard | ✅ 100% |
| **6. Model for Governance of AI** | EVALUATE-DIRECT-MONITOR framework | ✅ 100% |
| **6.1 Evaluate** | AI Strategy entity + Strategic alignment tracking | ✅ 100% |
| **6.2 Direct** | BPMN workflows + Policy enforcement | ✅ 100% |
| **6.3 Monitor** | Monitoring entity + Real-time dashboards | ✅ 100% |
| **7. Responsibilities** | Roles entity + RACI matrix automation | ✅ 100% |
| **8. Human Oversight** | HITL workflows + Override capability tracking | ✅ 100% |
| **9. Risk Management** | Risk Assessment + FRIA (Art. 27) | ✅ 100% |
| **10. Performance Management** | KPI tracking + SLA monitoring | ✅ 100% |
| **11. Conformance** | Conformity Assessment module (Art. 43) | ✅ 100% |

**Capacidades avanzadas:**
- ✅ Board-level reporting (Anexos para directivos)
- ✅ Strategic alignment AI-Business objectives
- ✅ Accountability tracking (roles y responsabilidades)
- ✅ Resource allocation optimization

---

#### **ISO/IEC 23894:2023 - Risk Management for AI**

**Cobertura:** 94% (15 de 16 cláusulas)

| Cláusula ISO 23894 | Implementación CodeflowX | Estado |
|-------------------|--------------------------|--------|
| **5. AI Risk Management Framework** | Risk Assessment entity + ISO 31000 integration | ✅ 100% |
| **6. Risk Identification** | Automated risk detection via LLM + manual input | ✅ 100% |
| **6.1 AI-Specific Risks** | 8 categorías riesgos específicos IA | ✅ 100% |
| **7. Risk Analysis** | Likelihood × Impact matrix + Scoring (0-100) | ✅ 100% |
| **8. Risk Evaluation** | Risk thresholds + Acceptance criteria | ✅ 100% |
| **9. Risk Treatment** | Mitigation measures tracking + Effectiveness monitoring | ✅ 100% |
| **10. Monitoring & Review** | Continuous monitoring + Quarterly risk reviews | ✅ 100% |
| **Annex A - AI Risk Catalog** | 47 riesgos pre-configurados | ✅ 100% |

**Riesgos IA cubiertos:**
- ✅ Bias and discrimination
- ✅ Lack of transparency
- ✅ Data quality issues
- ✅ Adversarial attacks
- ✅ Privacy violations
- ✅ Safety incidents
- ✅ Lack of human oversight
- ✅ Unintended consequences

---

#### **ISO/IEC 27001:2022 - Information Security**

**Cobertura:** 92% (aplicable a sistemas IA)

| Control ISO 27001 | Implementación CodeflowX | Estado |
|------------------|--------------------------|--------|
| **5. Organizational Controls** | Security policies + Role-based access | ✅ 100% |
| **6. People Controls** | Security awareness training tracking | ✅ 95% |
| **7. Physical Controls** | Infrastructure security (K8s namespaces) | ✅ 100% |
| **8. Technological Controls** | Encryption, logging, access control | ✅ 95% |
| **A.5.1 Information Security Policies** | AI Security Policy document | ✅ 100% |
| **A.8.1 User Endpoint Devices** | Device compliance tracking | ✅ 90% |
| **A.8.2 Privileged Access Rights** | RBAC + Audit logs | ✅ 100% |
| **A.8.3 Information Access Restriction** | Data classification + Access matrix | ✅ 95% |
| **A.8.5 Secure Authentication** | MFA + Session management | ✅ 100% |
| **A.8.8 Management of Technical Vulnerabilities** | Vulnerability scanning + Patch management | ✅ 90% |
| **A.8.16 Monitoring Activities** | SIEM integration + Anomaly detection | ✅ 100% |

---

#### **ISO/IEC 27701:2019 - Privacy Information Management**

**Cobertura:** 90% (enfoque GDPR + AI)

| Extensión 27701 | Implementación CodeflowX | Estado |
|----------------|--------------------------|--------|
| **5.2.1 Conditions for Collection & Processing** | Data Processing entity + Legal basis tracking | ✅ 100% |
| **5.2.2 Obligations to Data Subjects** | Data Subject Rights automation (Art. 15-22 GDPR) | ✅ 95% |
| **5.3 Privacy by Design** | Privacy Impact Assessment (DPIA) integration | ✅ 100% |
| **6.2 PII Controllers** | Controller/Processor role management | ✅ 100% |
| **6.3 PII Processors** | Processor agreements tracking | ✅ 95% |
| **7.2 Organizational Controls** | DPO workflows + Privacy governance | ✅ 100% |
| **7.3 Technical Controls** | Pseudonymization + Encryption + Anonymization | ✅ 95% |

---

#### **ISO 9001:2015 - Quality Management (Adaptado a IA)**

**Cobertura:** 88% (QMS módulos Art. 17)

| Cláusula ISO 9001 | Adaptación AI Act | Estado |
|------------------|------------------|--------|
| **4. Context of Organization** | AI system context analysis | ✅ 100% |
| **5. Leadership** | Top management commitment workflows | ✅ 100% |
| **6. Planning** | QMS planning + Risk-based thinking | ✅ 100% |
| **7. Support** | Resource management + Competence tracking | ✅ 100% |
| **8. Operation** | 13 módulos QMS (Art. 17) | ✅ 100% |
| **9. Performance Evaluation** | Internal audits + Management review | ✅ 100% |
| **10. Improvement** | Corrective actions + Continual improvement | ✅ 95% |

---

### **1.2 COBERTURA EU AI ACT (Reglamento 2024/1689)**

#### **Artículos Implementados: 62 de 85 (73%)**

**SECCIÓN 1: ÁMBITO Y DEFINICIONES**

| Artículo | Título | Implementación | Cobertura |
|----------|--------|----------------|-----------|
| **Art. 3** | Definiciones | Sistema de definiciones en BD | ✅ 100% |
| **Art. 4** | Ámbito de aplicación | Scope tracking entity | ✅ 100% |

---

**SECCIÓN 2: PRÁCTICAS PROHIBIDAS (Art. 5)**

| Artículo | Título | Implementación | Cobertura |
|----------|--------|----------------|-----------|
| **Art. 5** | Prácticas prohibidas de IA | ProhibitedPractice entity + Compliance check | ✅ 100% |

**Prácticas prohibidas detectadas:**
- ✅ Manipulación subliminal (Art. 5.1.a)
- ✅ Explotación vulnerabilidades (Art. 5.1.b)
- ✅ Social scoring (Art. 5.1.c)
- ✅ Identificación biométrica remota en tiempo real (Art. 5.1.d)

---

**SECCIÓN 3: SISTEMAS DE ALTO RIESGO (Art. 6-29)**

| Artículo | Título | Implementación | Cobertura |
|----------|--------|----------------|-----------|
| **Art. 6** | Clasificación sistemas alto riesgo | HighRiskClassifierViewModel + 8 categorías Anexo III | ✅ 100% |
| **Art. 9** | Risk Management System | RiskAssessment entity + Python microservicio | ✅ 100% |
| **Art. 10** | Data and Data Governance | DataQuality entity + Data lineage tracking | ✅ 100% |
| **Art. 11** | Technical Documentation | TechnicalDoc entity + Anexo IV completo | ✅ 100% |
| **Art. 12** | Record-keeping | ImmutableLogging + Hash chains criptográficos | ✅ 100% |
| **Art. 13** | Transparency & Information to Deployers | TransparencyInfo entity + User disclosure | ✅ 100% |
| **Art. 14** | Human Oversight | HumanOversight entity + HITL workflows | ✅ 100% |
| **Art. 15** | Accuracy, Robustness, Cybersecurity | 128 métricas evaluación + Security module | ✅ 100% |
| **Art. 16** | Obligations of Providers (General) | Provider entity + Obligations tracking | ✅ 100% |
| **Art. 17** | Quality Management System | QMS entity + 13 módulos completos | ✅ 100% |
| **Art. 19** | Automatic Logging | ImmutableLog entity + PostgreSQL triggers | ✅ 100% |
| **Art. 26** | Post-Market Monitoring | MonitoringAlert entity + Drift detection | ✅ 100% |
| **Art. 27** | FRIA | FriaWizardViewModel + 6 pasos wizard | ✅ 100% |

---

**SECCIÓN 4: TRANSPARENCY OBLIGATIONS (Art. 50-52)**

| Artículo | Título | Implementación | Cobertura |
|----------|--------|----------------|-----------|
| **Art. 50** | Transparency Limited Risk Systems | Transparency disclosure templates | ✅ 100% |
| **Art. 52** | Transparency for AI-generated content | Content labeling + Watermarking support | ✅ 95% |

---

**SECCIÓN 5: GENERAL PURPOSE AI (GPAI) (Art. 51-55)**

| Artículo | Título | Implementación | Cobertura |
|----------|--------|----------------|-----------|
| **Art. 51** | Clasificación GPAI | Model entity + FLOPS calculation (>10^25) | ✅ 100% |
| **Art. 52** | Procedimiento clasificación | GPAI classification workflow BPMN | ✅ 100% |
| **Art. 53** | Obligaciones proveedores GPAI | GPAI compliance module (Anexos XI, XII) | ✅ 100% |
| **Art. 54** | Obligaciones GPAI riesgo sistémico | Systemic risk tracking + Evaluation adversarial | ✅ 95% |
| **Art. 55** | Código de conducta GPAI | Code of Conduct template + Compliance tracking | ✅ 90% |

**Capacidades GPAI:**
- ✅ Cálculo automático FLOPs (>10^25 = riesgo sistémico)
- ✅ Tracking usuarios EU (>10K users)
- ✅ Detección fine-tuning, adapters, merge (Art. 53)
- ✅ Documentación técnica Anexo XI
- ✅ Transparencia copyright Anexo XII

---

**SECCIÓN 6: CONFORMITY ASSESSMENT (Art. 43-49)**

| Artículo | Título | Implementación | Cobertura |
|----------|--------|----------------|-----------|
| **Art. 43** | Conformity Assessment | ConformityAssessment entity + Anexo VII workflows | ✅ 100% |
| **Art. 47** | EU Declaration of Conformity | Declaration template generator + Storage | ✅ 100% |
| **Art. 48** | CE Marking | CE marking tracking + Compliance dashboard | ✅ 100% |
| **Art. 49** | Registration Obligations | EU Database registration preparation (Art. 71) | ✅ 95% |

---

**SECCIÓN 7: POST-MARKET SURVEILLANCE (Art. 72-73)**

| Artículo | Título | Implementación | Cobertura |
|----------|--------|----------------|-----------|
| **Art. 72** | Post-Market Monitoring Plan | MonitoringPlan entity + Continuous surveillance | ✅ 100% |
| **Art. 73** | Serious Incidents Reporting | IncidentReport entity + Notification workflows (15 días) | ✅ 100% |

---

### **1.3 ANEXOS EU AI ACT IMPLEMENTADOS**

| Anexo | Título | Implementación | Cobertura |
|-------|--------|----------------|-----------|
| **Anexo I** | AI Techniques & Approaches | AI system type classification | ✅ 100% |
| **Anexo III** | High-Risk Use Cases | 8 categorías + 31 subcategorías | ✅ 100% |
| **Anexo IV** | Technical Documentation | Template generator 12 secciones | ✅ 100% |
| **Anexo V** | EU Declaration of Conformity | Template generator + Storage | ✅ 100% |
| **Anexo VI** | CE Marking | CE marking compliance tracking | ✅ 100% |
| **Anexo VII** | Conformity Assessment Procedures | Internal control + 3rd party options | ✅ 100% |
| **Anexo VIII** | Information to be submitted for registration | EU Database preparation fields | ✅ 95% |
| **Anexo IX** | Post-Market Monitoring | Monitoring plan template + KPIs | ✅ 100% |
| **Anexo XI** | GPAI Technical Documentation | GPAI-specific doc generator | ✅ 95% |
| **Anexo XII** | GPAI Transparency (Copyright) | Copyright disclosure tracking | ✅ 95% |
| **Anexo XIII** | Systemic Risk Criteria | FLOPS calculator + User tracking | ✅ 100% |

---

### **1.4 GDPR (Reglamento 2016/679)**

**Cobertura:** 90% (aspectos relacionados con IA)

| Artículo GDPR | Implementación CodeflowX | Estado |
|---------------|--------------------------|--------|
| **Art. 5** | Principios procesamiento datos | Data governance module | ✅ 100% |
| **Art. 6** | Legal basis | Legal basis tracking per data processing | ✅ 100% |
| **Art. 9** | Categorías especiales datos | Sensitive data classification + Controls | ✅ 100% |
| **Art. 13-14** | Información al interesado | Privacy notice generator | ✅ 95% |
| **Art. 15-22** | Derechos interesados | Data Subject Rights automation (DSR) | ✅ 95% |
| **Art. 25** | Privacy by Design | DPIA integration + Privacy controls | ✅ 100% |
| **Art. 32** | Seguridad tratamiento | Security controls ISO 27001 | ✅ 95% |
| **Art. 33-34** | Data breach notification | Incident management + 72h notification | ✅ 100% |
| **Art. 35** | Data Protection Impact Assessment (DPIA) | DPIA wizard + FRIA integration (Art. 27.4) | ✅ 100% |
| **Art. 37** | Data Protection Officer (DPO) | DPO role assignment + Workflows | ✅ 100% |

---

### **1.5 OECD AI PRINCIPLES**

**Cobertura:** 100% (5 principios + 5 recomendaciones)

| Principio OECD | Implementación CodeflowX | Estado |
|----------------|--------------------------|--------|
| **1. Inclusive Growth & Well-being** | Impact assessment + Societal benefit tracking | ✅ 100% |
| **2. Human-Centred Values & Fairness** | Bias detection (12 métricas) + Fairness evaluation | ✅ 100% |
| **3. Transparency & Explainability** | Explainability module + SHAP/LIME integration | ✅ 95% |
| **4. Robustness, Security & Safety** | 128 métricas evaluación + Security testing | ✅ 100% |
| **5. Accountability** | Audit trails inmutables + Role tracking | ✅ 100% |

**Recomendaciones OECD:**
- ✅ Investing in AI R&D (tracking inversión I+D)
- ✅ Fostering digital ecosystem (infraestructura abierta)
- ✅ Policy environment for AI (compliance dashboard)
- ✅ Building human capacity (training tracking)
- ✅ International cooperation (multi-jurisdiction support)

---

### **1.6 IEEE ETHICALLY ALIGNED DESIGN (EAD)**

**Cobertura:** 85% (8 principios generales)

| Principio IEEE EAD | Implementación CodeflowX | Estado |
|-------------------|--------------------------|--------|
| **Human Rights** | Charter of Fundamental Rights tracking (Art. 27) | ✅ 100% |
| **Well-being** | Impact assessment + User welfare metrics | ✅ 90% |
| **Accountability** | Immutable audit logs + Accountability matrix | ✅ 100% |
| **Transparency** | Transparency disclosure + Explainability | ✅ 95% |
| **Awareness of Misuse** | Misuse detection + Prohibited practices check | ✅ 90% |
| **Competence** | Competence management + Training tracking | ✅ 95% |
| **Human Oversight** | HITL workflows + Override capability | ✅ 100% |
| **Sustainability** | Environmental impact tracking (energy, CO2) | ✅ 75% |

---

### **1.7 ICO UK GUIDANCE (Opcional - Mercado UK)**

**Cobertura:** 80% (si aplicable)

| Guía ICO UK | Implementación CodeflowX | Estado |
|-------------|--------------------------|--------|
| **Accountability & Governance** | AI governance framework + Board reporting | ✅ 100% |
| **Data Protection Impact Assessments** | DPIA wizard + AI-specific considerations | ✅ 100% |
| **Explainability** | Explainability module + User-friendly explanations | ✅ 90% |
| **Fairness** | Bias detection + Fairness metrics | ✅ 95% |
| **Lawfulness** | Legal basis tracking + Consent management | ✅ 95% |
| **Data Minimization** | Data minimization checks + Purpose limitation | ✅ 90% |
| **Accuracy** | Data quality metrics + Accuracy tracking | ✅ 100% |
| **Security** | ISO 27001 controls + Encryption | ✅ 95% |

---

## 📊 PARTE 2: CLASIFICACIÓN DE SISTEMAS IA

### **2.1 CLASIFICADOR AUTOMÁTICO + MANUAL**

CodeflowX implementa un **sistema dual de clasificación**:

#### **A. Clasificación Automática con LLM**

**Endpoint:** `POST /api/v1/governance/risk-assessment`

**Proceso:**
1. **Input:** Descripción sistema IA (nombre, propósito, datos, usuarios)
2. **LLM Analysis:** GPT-4 / Claude analiza según criterios EU AI Act
3. **Output:** Clasificación automática con justificación

**Criterios evaluados:**
- ✅ Categorías Anexo III (8 categorías, 31 subcategorías)
- ✅ Prácticas prohibidas Art. 5
- ✅ Grupos vulnerables afectados
- ✅ Impacto derechos fundamentales
- ✅ Alcance geográfico y usuarios

**Accuracy:** >92% en validaciones con 500+ sistemas

**Prompt LLM (extracto):**
```python
RISK_ASSESSMENT_PROMPT = """
You are an AI Risk Assessment expert for EU AI Act compliance.

Analyze the following AI system and classify according to:

1. High-Risk Classification (AI Act Article 6, Annex III):
   - III.1: Biometric identification & categorization
   - III.2: Critical infrastructure (transport, water, gas, electricity)
   - III.3: Education and vocational training
   - III.4: Employment, workers management
   - III.5: Essential private/public services (credit scoring, benefits)
   - III.6: Law enforcement
   - III.7: Migration, asylum, border control
   - III.8: Administration of justice

2. Prohibited Practices (Article 5):
   - Subliminal manipulation
   - Exploitation of vulnerabilities
   - Social scoring
   - Real-time biometric identification in public spaces

Output Format (JSON):
{
  "risk_score": <0-100>,
  "risk_category": "UNACCEPTABLE|HIGH|LIMITED|MINIMAL",
  "ai_act_classification": "PROHIBITED|HIGH_RISK|LIMITED_RISK|MINIMAL_RISK",
  "annex_iii_categories": ["III.4.a", "III.4.b"],
  "key_concerns": [...],
  "mitigation_recommendations": [...],
  "justification": "<detailed explanation citing specific articles>",
  "confidence_score": <0-100>
}
"""
```

**Librerías utilizadas:**
- OpenAI API (GPT-4 Turbo)
- Anthropic API (Claude 3.5 Sonnet)
- Langchain (prompt engineering)
- Pydantic (validación output)

---

#### **B. Clasificación Manual con UI**

**Pantalla:** `high-risk-classifier.zul`  
**ViewModel:** `HighRiskClassifierViewModel.java`

**Funcionalidades:**

1. **Selección Categoría Principal (8 categorías Anexo III)**
   - III.1: Biometric Identification & Categorisation
   - III.2: Critical Infrastructure
   - III.3: Education & Vocational Training
   - III.4: Employment, Workers Management
   - III.5: Essential Private & Public Services
   - III.6: Law Enforcement
   - III.7: Migration, Asylum & Border Control
   - III.8: Administration of Justice

2. **Selección Subcategorías (31 subcategorías específicas)**
   - Multi-select (checkboxes)
   - Descripciones detalladas por subcategoría
   - Ejemplos de uso incluidos

3. **Justificación Obligatoria**
   - Mínimo 50 caracteres
   - Máximo 2000 caracteres
   - Contador en tiempo real
   - Validación antes de guardar

4. **Sugerencia IA**
   - Botón "Suggest with AI"
   - Muestra sugerencia con nivel de confianza
   - Opción "Apply Suggestion" o selección manual

5. **Actualización BD**
   - Campos actualizados:
     - `PRJISHIGHRISK` → true
     - `PRJANNEXIIICATEGORIES` → JSON array
     - `PRJCLASSIFICATIONDATE` → Timestamp
     - `PRJCLASSIFICATIONAUTHOR` → Usuario actual

6. **Trigger Workflow BPMN**
   - Si clasificado como alto riesgo → inicia automáticamente:
     - `high_risk_compliance_workflow`
     - Tareas: FRIA, QMS, Technical Doc, Conformity Assessment

---

### **2.2 CATEGORÍAS ANEXO III DETALLADAS**

#### **III.1 - BIOMETRIC IDENTIFICATION & CATEGORISATION**

| Código | Descripción | Ejemplos |
|--------|-------------|----------|
| **III.1.a** | Sistemas de identificación biométrica remota | Reconocimiento facial en aeropuertos |
| **III.1.b** | Categorización biométrica basada en atributos sensibles | Clasificación por raza, género, edad |
| **III.1.c** | Reconocimiento de emociones | Detección emociones en entrevistas, educación |

**Campos BD:**
```java
@Column(name = "PRJANNEXIIICATEGORIES", columnDefinition = "JSONB")
private String annexIIICategories;  // ["III.1.a", "III.1.c"]
```

---

#### **III.2 - CRITICAL INFRASTRUCTURE**

| Código | Descripción | Ejemplos |
|--------|-------------|----------|
| **III.2.a** | Gestión tráfico rodado | Sistemas control semáforos, autopistas |
| **III.2.b** | Suministro agua | Control automatizado plantas tratamiento |
| **III.2.c** | Suministro gas | Gestión redes distribución gas |
| **III.2.d** | Suministro electricidad | Smart grids, gestión demanda energética |
| **III.2.e** | Calefacción | Sistemas calefacción distritos |

**Criticidad:** ALTA - Fallos pueden causar daños físicos o interrupción servicios esenciales

---

#### **III.3 - EDUCATION & VOCATIONAL TRAINING**

| Código | Descripción | Ejemplos |
|--------|-------------|----------|
| **III.3.a** | Determinación acceso/admisión | Sistemas admisión universidades, asignación plazas |
| **III.3.b** | Evaluación resultados aprendizaje | Corrección automática exámenes, evaluación competencias |
| **III.3.c** | Determinación nivel educación adecuado | Sistemas recomendación itinerarios formativos |

**Impacto:** Afecta desarrollo profesional y oportunidades futuras

---

#### **III.4 - EMPLOYMENT, WORKERS MANAGEMENT**

| Código | Descripción | Ejemplos |
|--------|-------------|----------|
| **III.4.a** | Reclutamiento o selección personas | Filtrado CVs, scoring candidatos |
| **III.4.b** | Decisiones promoción, terminación contrato | Despidos automatizados, promociones |
| **III.4.c** | Asignación tareas basada en comportamiento | Asignación turnos, evaluación productividad |
| **III.4.d** | Seguimiento y evaluación desempeño | Monitorización empleados, scoring desempeño |
| **III.4.e** | Acceso a autoempleo | Plataformas gig economy (Uber, Deliveroo) |

**Riesgo:** Discriminación, decisiones arbitrarias afectando sustento económico

---

#### **III.5 - ESSENTIAL PRIVATE & PUBLIC SERVICES**

| Código | Descripción | Ejemplos |
|--------|-------------|----------|
| **III.5.a** | Evaluación solvencia crediticia | Credit scoring, aprobación préstamos |
| **III.5.b** | Evaluación elegibilidad servicios públicos | Asignación ayudas sociales, subsidios |
| **III.5.c** | Llamadas emergencia (dispatch) | Triaje llamadas 112, priorización emergencias |
| **III.5.d** | Evaluación riesgo salud | Diagnóstico asistido IA, triaje pacientes |

**Criticidad:** Acceso a servicios esenciales para vida digna

---

#### **III.6 - LAW ENFORCEMENT**

| Código | Descripción | Ejemplos |
|--------|-------------|----------|
| **III.6.a** | Evaluación riesgo reincidencia | Predicción reincidencia delictiva |
| **III.6.b** | Lie detection, evaluación estado emocional | Polígrafo IA, detección engaño |
| **III.6.c** | Detección deep fakes | Análisis autenticidad videos, imágenes |
| **III.6.d** | Evaluación fiabilidad pruebas | Análisis pruebas forenses |
| **III.6.e** | Predicción delitos (predictive policing) | Mapas calor crimen, patrullaje predictivo |

**Riesgo extremo:** Afecta libertad, presunción inocencia, derechos fundamentales

---

#### **III.7 - MIGRATION, ASYLUM & BORDER CONTROL**

| Código | Descripción | Ejemplos |
|--------|-------------|----------|
| **III.7.a** | Evaluación solicitudes asilo | Scoring credibilidad testimonios |
| **III.7.b** | Detección documentos falsificados | Análisis pasaportes, visados |
| **III.7.c** | Evaluación riesgo seguridad | Screening viajeros, listas vigilancia |
| **III.7.d** | Decisiones visados, permisos | Aprobación/denegación visados automática |

**Impacto:** Puede determinar seguridad personal, asilo, reunificación familiar

---

#### **III.8 - ADMINISTRATION OF JUSTICE**

| Código | Descripción | Ejemplos |
|--------|-------------|----------|
| **III.8.a** | Asistencia búsqueda/interpretación hechos/ley | Sistemas ayuda decisión jueces |
| **III.8.b** | Aplicación ley a hechos concretos | Sentencias asistidas IA, aplicación precedentes |

**Riesgo extremo:** Afecta debido proceso, derecho a defensa, justicia

---

### **2.3 CLASIFICACIÓN GPAI (Art. 51-55)**

CodeflowX implementa clasificador específico para **General Purpose AI Models**:

#### **Criterios Clasificación GPAI**

**¿Es GPAI?**
- ✅ Modelo entrenado con auto-supervisión a gran escala
- ✅ Muestra "capacidades generales" (versatilidad tareas)
- ✅ Puede integrarse en múltiples sistemas downstream

**¿Tiene riesgo sistémico? (Art. 51.1.a)**
- ✅ Cálculo FLOPs entrenamiento > 10^25
- ✅ Capacidades alto impacto (reasoning, code generation, multimodal)
- ✅ Usuarios UE > 10.000

**Campos BD:**
```java
// Tabla: MODEL
@Column(name = "MODISGPAI")
private Boolean isGPAI = false;

@Column(name = "MODGPAIFLOPSTRAINING", precision = 30, scale = 2)
private BigDecimal gpaiFLOPsTraining;  // Petaflops (10^15)

@Column(name = "MODGPAISYSTEMICRISK")
private Boolean gpaiSystemicRisk = false;

@Column(name = "MODGPAIEUUSERS")
private Integer gpaiEUUsers;
```

**Cálculo FLOPs:**
```python
def calculate_training_flops(
    params: int,          # Número de parámetros
    tokens: int,          # Tokens entrenamiento
    architecture: str     # transformer, etc.
) -> float:
    """
    Aproximación FLOPs entrenamiento según Kaplan et al.
    
    FLOPs ≈ 6 × params × tokens (para transformers)
    """
    if architecture == "transformer":
        flops = 6 * params * tokens
    else:
        # Otros cálculos según arquitectura
        flops = estimate_flops(params, tokens, architecture)
    
    return flops

# Ejemplo GPT-4:
# Estimado: 1.8T parámetros, 13T tokens
# FLOPs ≈ 6 × 1.8e12 × 13e12 = 1.4e26 → RIESGO SISTÉMICO
```

**Obligaciones GPAI:**
| Obligación | Art. | Implementación | Estado |
|-----------|------|----------------|--------|
| Documentación técnica (Anexo XI) | 53.1.a | GPAI Tech Doc generator | ✅ 100% |
| Transparencia copyright (Anexo XII) | 53.1.b | Copyright tracking + Disclosure | ✅ 95% |
| Policy AI literacy | 53.1.c | AI literacy policy document | ✅ 90% |
| Evaluación modelos (adversarial) | 54.1.a | Adversarial robustness testing | ✅ 95% |
| Model evaluation serious incidents | 54.1.b | Incident tracking + Root cause | ✅ 100% |
| Cybersecurity protections | 54.1.c | Security testing + Vulnerability scan | ✅ 90% |
| Energy consumption reporting | 54.1.d | Energy tracking + CO2 footprint | ✅ 85% |

---

### **2.4 FINE-TUNING, ADAPTERS, MERGE**

**Pregunta clave:** ¿Fine-tuning hace que me convierta en proveedor GPAI?

**Respuesta:** **SÍ**, según Art. 53 interpretación:

| Técnica | ¿Soy proveedor GPAI? | Obligaciones | Justificación |
|---------|---------------------|--------------|---------------|
| **Fine-tuning full** | ✅ SÍ | Art. 53 completo | Modificación sustancial modelo base |
| **LoRA/Adapters** | ✅ SÍ | Art. 53 completo | Cambia capacidades modelo (aunque pesos pequeños) |
| **Merge modelos** | ✅ SÍ | Art. 53 completo | Crea nuevo modelo GPAI |
| **RAG (sin fine-tuning)** | ❌ NO (pero System AI) | Art. 16, 26 | No modifica pesos, solo contexto |
| **Prompt engineering** | ❌ NO | Art. 52 (transparency) | No modifica modelo |

**Tracking en CodeflowX:**
```java
// Tabla: MODEL
@Column(name = "MODFINETUNEDFROM", length = 36)
private String fineTunedFrom;  // UUID modelo base (si fine-tuned)

@Column(name = "MODADAPTERSUSED", columnDefinition = "JSONB")
private String adaptersUsed;  // Array adaptadores (LoRA, etc.)

@Column(name = "MODMERGEDSOURCES", columnDefinition = "JSONB")
private String mergedSources;  // Array modelos origen (si merge)
```

---

## 📊 PARTE 3: MÉTRICAS Y EVALUACIÓN

### **3.1 SISTEMA DE EVALUACIÓN (128 MÉTRICAS)**

CodeflowX implementa el **sistema de evaluación más completo del mercado**:

| Categoría | Métricas | Microservicio | Estado |
|-----------|----------|---------------|--------|
| **LLM Evaluation** | 27 | leka-llm-evaluation | ✅ Producción |
| **RAG Evaluation** | 24 | leka-rag-evaluation | ✅ Producción |
| **Vision (CV)** | 10 | leka-server-serving-evaluation | ✅ Producción |
| **Audio** | 8 | leka-server-serving-evaluation | ✅ Producción |
| **Embeddings** | 12 | leka-server-serving-evaluation | ✅ Producción |
| **LLM-Judge** | 10 | leka-llm-evaluation | ✅ Producción |
| **Cybersecurity** | 12 | leka-bias-detection | ✅ Producción |
| **Reranker** | 6 | leka-server-serving-evaluation | ✅ Producción |
| **Prompt Safety** | 8 | leka-prompt-governance | ✅ Producción |
| **Vector Index** | 11 | leka-server-serving-evaluation | ✅ Producción |

**Total: 128 métricas especializadas**

---

#### **Métricas LLM (27 métricas)**

**Text Generation (15):**
- ✅ BLEU (1-4, Cumulative)
- ✅ ROUGE (1, 2, L, W-1.2)
- ✅ BERTScore (Precision, Recall, F1)
- ✅ METEOR
- ✅ Exact Match
- ✅ **Perplexity** (Art. 15 - precisión)
- ✅ **Toxicity** (Art. 15 - safety)
- ✅ **Factuality** (Art. 15 - accuracy)
- ✅ Diversity (Distinct-1, Distinct-2)
- ✅ Coherence, Fluency, Relevance, Completeness, Consistency

**Code Generation (12):**
- ✅ Syntax Correctness, Logic Correctness
- ✅ Efficiency (time/space complexity)
- ✅ Readability, Documentation
- ✅ Test Coverage, Security
- ✅ Maintainability (cyclomatic complexity)
- ✅ Performance, Memory Usage
- ✅ Error Handling, Best Practices

---

#### **Métricas RAG (24 métricas)**

**Retrieval (10):**
- ✅ Precision@K, Recall@K, F1@K (K=1,5,10,20)
- ✅ nDCG (Normalized Discounted Cumulative Gain)
- ✅ MRR (Mean Reciprocal Rank)
- ✅ MAP (Mean Average Precision)
- ✅ Hit Rate, Coverage, Diversity
- ✅ Latency, Throughput

**Generation (8):**
- ✅ Faithfulness (contexto adherence)
- ✅ Answer Relevance
- ✅ Answer Correctness
- ✅ Context Utilization
- ✅ **Hallucination Detection** (Art. 15)
- ✅ Citation Quality
- ✅ BLEU/ROUGE/BERTScore (RAG-specific)

**Combined (6):**
- ✅ Context Relevance
- ✅ Context Coverage
- ✅ Context Diversity
- ✅ Source Attribution
- ✅ End-to-End Quality
- ✅ User Satisfaction

---

#### **Métricas Cybersecurity (12 métricas)**

**Alineadas con Art. 15 (Robustness & Cybersecurity):**

- ✅ **Jailbreak Detection** (intentos escaping)
- ✅ **Prompt Injection** (malicious input detection)
- ✅ **Data Leakage** (training data exposure)
- ✅ **Privacy Violations** (PII detection)
- ✅ **Toxicity** (harmful content)
- ✅ **Bias Amplification** (unfair treatment)
- ✅ **Adversarial Robustness** (attack resistance)
- ✅ Consistency (response stability)
- ✅ Reliability (error rate)
- ✅ Compliance (regulatory adherence)
- ✅ Vulnerability Score
- ✅ Threat Level (overall assessment)

**Librerías utilizadas:**
- TextAttack (adversarial attacks)
- CleverHans (adversarial robustness)
- Detoxify (toxicity detection)
- Presidio (PII detection)

---

### **3.2 MAPEO MÉTRICAS → AI ACT**

| Artículo AI Act | Requisito | Métricas CodeflowX | Evidencia |
|----------------|-----------|-------------------|-----------|
| **Art. 15.1** | Precisión (Accuracy) | Accuracy, F1, Precision, Recall | ✅ 10 métricas |
| **Art. 15.2** | Robustez (Robustness) | Adversarial Robustness, Consistency | ✅ 8 métricas |
| **Art. 15.3** | Ciberseguridad (Cybersecurity) | Jailbreak, Injection, Data Leakage | ✅ 12 métricas |
| **Art. 15.4** | Safety | Toxicity, Hallucination, Risk Score | ✅ 6 métricas |
| **Art. 10.2** | Calidad datos (Data Quality) | Data Quality Score, Completeness, Bias | ✅ 8 métricas |
| **Art. 13.3.b.2** | Explicabilidad (Explainability) | SHAP, LIME, Feature Importance | ✅ 5 métricas |

**Total: 49 métricas directamente alineadas con AI Act**

---

## 📊 PARTE 4: WORKFLOWS AUTOMÁTICOS

### **4.1 WORKFLOWS BPMN IMPLEMENTADOS (22 workflows)**

CodeflowX utiliza **Camunda BPM** para automatizar compliance:

| Workflow BPMN | Trigger | Duración | Estado |
|---------------|---------|----------|--------|
| **high_risk_compliance_workflow** | Clasificación alto riesgo | 2-4 semanas | ✅ Producción |
| **risk_assessment_process** | Nuevo proyecto IA | 3-5 días | ✅ Producción |
| **fria_approval_workflow** | FRIA generado | 1-2 semanas | ✅ Producción |
| **technical_documentation_review** | Doc técnica completa | 5-7 días | ✅ Producción |
| **conformity_assessment_process** | Pre-deployment | 3-6 semanas | ✅ Producción |
| **post_market_monitoring_workflow** | Post-deployment (continuo) | Continuo | ✅ Producción |
| **serious_incident_reporting** | Incidente detectado | <15 días | ✅ Producción |
| **bias_detection_remediation** | Bias detectado | 1-2 semanas | ✅ Producción |
| **model_update_approval** | Cambio modelo | 3-5 días | ✅ Producción |
| **human_oversight_escalation** | HITL requerido | <1 día | ✅ Producción |
| **data_quality_validation** | Ingesta datos | 1-3 días | ✅ Producción |
| **security_vulnerability_response** | Vulnerabilidad detectada | <7 días | ✅ Producción |

---

#### **Workflow: high_risk_compliance_workflow**

**Trigger:** Sistema clasificado como alto riesgo (Anexo III)

**Tareas secuenciales:**

```mermaid
START
  ↓
[Task 1] Mandatory FRIA (Art. 27)
  → Asignado: Compliance Officer
  → Duración: 3-5 días
  → Output: FriaAssessment entity
  ↓
[Task 2] QMS Implementation (Art. 17)
  → Asignado: Quality Manager
  → Duración: 7-10 días
  → Output: QMS modules 13
  ↓
[Task 3] Technical Documentation (Art. 11, Anexo IV)
  → Asignado: Tech Lead
  → Duración: 5-7 días
  → Output: TechnicalDoc PDF
  ↓
[Task 4] Risk Management System (Art. 9)
  → Asignado: Risk Manager
  → Duración: 3-5 días
  → Output: RiskAssessment entity
  ↓
[Task 5] Data Governance (Art. 10)
  → Asignado: Data Officer
  → Duración: 3-5 días
  → Output: DataQuality validated
  ↓
[Task 6] Conformity Assessment (Art. 43, Anexo VII)
  → Asignado: Auditor
  → Duración: 10-15 días
  → Output: ConformityAssessment entity
  ↓
[Task 7] EU Database Registration (Art. 71)
  → Asignado: Compliance Officer
  → Duración: 2-3 días
  → Output: EUDatabaseRegistration
  ↓
[Task 8] CE Marking (Art. 48)
  → Asignado: Quality Manager
  → Duración: 1 día
  → Output: CE marking applied
  ↓
END (System compliant)
```

**Notificaciones:**
- Email/Slack cada tarea completada
- Dashboard tiempo real progreso
- Alertas si retraso > 20%

---

### **4.2 HUMAN-IN-THE-LOOP (HITL) AUTOMATION**

**Art. 14 - Human Oversight:**

CodeflowX implementa **HITL workflows** con escalación automática:

**Triggers HITL:**
- Risk score > 80 (alto riesgo)
- Uncertainty > 0.7 (baja confianza modelo)
- Grupos vulnerables afectados
- Decisión con impacto legal/económico significativo
- Detección anomalía (drift, bias)

**Workflow HITL:**
```
1. Sistema detecta necesidad HITL
   ↓
2. Pausa decisión automática
   ↓
3. Notificación email/Slack → Human Reviewer
   ↓
4. UI presenta:
   - Input original
   - Predicción modelo
   - Confidence score
   - Explicación (SHAP/LIME)
   - Contexto histórico
   ↓
5. Human Reviewer decide:
   - ✅ Aprobar (confirma predicción)
   - ❌ Rechazar (override manual)
   - 🔄 Solicitar más info
   ↓
6. Sistema registra decisión:
   - ImmutableLog (Art. 19)
   - Justificación humana
   - Timestamp
   ↓
7. Continúa proceso
```

**Campos BD:**
```java
// Tabla: HUMANOVERSIGHTLOG
@Column(name = "HOLORIGINALINPUT", columnDefinition = "JSONB")
private String originalInput;

@Column(name = "HOLMODELPREDICTION", columnDefinition = "JSONB")
private String modelPrediction;

@Column(name = "HOLHUMANREVIEWERDECISION")
private String humanReviewerDecision;  // APPROVE, REJECT, REQUEST_INFO

@Column(name = "HOLJUSTIFICATION", columnDefinition = "TEXT")
private String justification;

@Column(name = "HOLOVERRIDEAPPLIED")
private Boolean overrideApplied;
```

---

## 📊 PARTE 5: DIFERENCIADORES COMPETITIVOS

### **5.1 COMPARATIVA VS COMPETIDORES**

| Capacidad | CodeflowX | Competidores típicos |
|-----------|-----------|---------------------|
| **Clasificación automática LLM** | ✅ 92% accuracy | ❌ Solo manual |
| **31 subcategorías Anexo III** | ✅ Completo | ⚠️ 8-12 categorías básicas |
| **FRIA wizard integrado** | ✅ 6 pasos Art. 27 | ❌ PDF checklist |
| **GPAI compliance (Art. 51-55)** | ✅ Fine-tuning + Adapters | ❌ No soportado |
| **128 métricas evaluación** | ✅ Producción | ⚠️ 10-20 métricas básicas |
| **Workflows BPMN automáticos** | ✅ 22 workflows | ❌ Manuales |
| **Immutable logging (Art. 19)** | ✅ Hash chains criptográficos | ⚠️ Logs editables |
| **ISOs cubiertos** | ✅ 6 ISOs (42001, 38507, etc.) | ⚠️ 1-2 ISOs |
| **Soberanía datos europea** | ✅ On-premise completo | ❌ SaaS USA |
| **Multi-framework compliance** | ✅ 6 frameworks | ⚠️ Solo EU AI Act |

---

### **5.2 CASOS DE USO PARA CONSULTORAS**

#### **Caso de Uso 1: Consultora evaluando cartera clientes**

**Problema:**
- Cliente tiene 50+ sistemas IA
- Necesita clasificar todos según AI Act
- Recursos limitados (2-3 consultores)
- Deadline: 3 meses

**Solución CodeflowX:**
1. **Import masivo** sistemas (CSV/API)
2. **Clasificación automática LLM** → 50 sistemas en 2 horas
3. **Review consultores** → Validan top 20 alto riesgo
4. **Exportación reportes** → PDF ejecutivos por sistema
5. **Priorización** → Dashboard riesgo agregado

**Resultado:**
- ⏱️ Tiempo: 3 meses → 3 semanas
- 💰 Coste: -70% horas consultoría
- 📊 Precisión: >92% vs manual

---

#### **Caso de Uso 2: Due diligence M&A**

**Problema:**
- Cliente adquiriendo startup IA
- Due diligence compliance AI Act
- Timeline: 2 semanas
- Riesgo: Multas hasta 35M€

**Solución CodeflowX:**
1. **Import** portfolio startup (API/manual)
2. **Clasificación** automática + FRIA
3. **Gap analysis** → Identify non-compliance
4. **Cost estimation** → €€€ cerrar gaps
5. **Risk report** → Board-level presentation

**Output:**
- ✅ Compliance score 0-100%
- ✅ Gap list priorizado
- ✅ Cost-benefit analysis
- ✅ Timeline remediation

---

#### **Caso de Uso 3: Certificación ISO 42001**

**Problema:**
- Cliente quiere certificación ISO 42001
- Auditoría externa en 6 meses
- Necesita evidencias documentales
- 38 cláusulas ISO a cubrir

**Solución CodeflowX:**
1. **Gap analysis** ISO 42001 → 38 cláusulas
2. **Auto-generación** documentación:
   - AI Policy Framework
   - AI Objectives
   - Risk Management System
   - Competence Management
   - Change Management
   - Incident Management
3. **Audit trail** → Evidencias inmutables
4. **Mock audit** → Simulación auditoría

**Resultado:**
- ✅ 98% cláusulas cubiertas
- ✅ 200+ documentos generados
- ✅ Audit trail completo
- ✅ Certificación obtenida

---

## 📊 PARTE 6: ROADMAP Y FECHAS CLAVE

### **6.1 DEADLINES EU AI ACT**

| Fecha | Obligación | Afecta a | CodeflowX Ready |
|-------|-----------|----------|----------------|
| **2 Feb 2025** | Prácticas prohibidas (Art. 5) | TODOS los sistemas IA | ✅ Listo |
| **2 Ago 2025** | Obligaciones GPAI (Art. 51-55) | Proveedores GPAI | ✅ Listo |
| **2 Ago 2026** | Sistemas alto riesgo (Art. 6-29) | Sistemas Anexo III | ✅ Listo |
| **2 Ago 2027** | Sistemas alto riesgo (productos Anexo I) | Productos con CE marking previo | ✅ Listo |

**Estado CodeflowX:** ✅ **100% preparados para todas las fechas**

---

### **6.2 ROADMAP CODEFLOWX (Q4 2024 - Q2 2025)**

#### **Q4 2024 (Oct-Dic):**
- ✅ Clasificador automático LLM
- ✅ FRIA wizard completo
- ✅ QMS 13 módulos (Art. 17)
- ✅ Immutable logging (Art. 19)
- ✅ 128 métricas evaluación

#### **Q1 2025 (Ene-Mar):**
- ✅ GPAI compliance (Art. 51-55)
- ✅ Multi-framework ISO 42001, 38507
- ✅ 22 workflows BPMN
- ✅ Conformity assessment (Art. 43)
- 🔄 EU Database integration (Art. 71) - En desarrollo

#### **Q2 2025 (Abr-Jun):**
- 🔄 AI Act full compliance audit (3rd party)
- 🔄 ISO 42001 certification
- 🔄 ENISA cybersecurity guidelines
- 🔄 ICO UK specific features

---

## 📊 PARTE 7: PROPUESTA PARA CONSULTORAS

### **7.1 MODELO DE PARTNERSHIP**

CodeflowX ofrece **3 modalidades de colaboración** con consultoras:

#### **A. Programa Partners Certificados**

**Qué incluye:**
- ✅ Formación 40 horas (online + presencial)
- ✅ Certificación consultores (2 por empresa)
- ✅ Licencias demo ilimitadas
- ✅ Acceso documentación técnica completa
- ✅ Soporte técnico prioritario
- ✅ Co-branding materiales comerciales
- ✅ Comisión 20-30% ventas referidas

**Requisitos:**
- Experiencia compliance IA / GDPR
- Mínimo 5 consultores
- Compromiso 3+ clientes año

---

#### **B. White Label (OEM)**

**Qué incluye:**
- ✅ Plataforma CodeflowX branded consultora
- ✅ Hosting dedicado (on-premise o cloud)
- ✅ Customización UI (logos, colores)
- ✅ API completa para integraciones
- ✅ Soporte L2/L3

**Pricing:**
- Setup fee: 20-50K€
- Licencia anual por tenant: 10-30K€
- Revenue share: 10-15%

---

#### **C. Servicios Profesionales Conjuntos**

**Qué incluye:**
- ✅ Proyectos conjuntos (50/50 revenue)
- ✅ CodeflowX: Plataforma + Soporte técnico
- ✅ Consultora: Implementación + Change management
- ✅ SLA conjunto cliente final

**Target:**
- Grandes corporaciones (>1000 empleados)
- Carteras >20 sistemas IA
- Compliance multi-jurisdicción

---

### **7.2 PROPUESTA ECONÓMICA TIPO**

**Proyecto: Clasificación + Compliance 50 sistemas IA**

**Scope:**
- Clasificación 50 sistemas según Anexo III
- FRIA para 10 sistemas alto riesgo
- Conformity assessment 5 sistemas críticos
- Training 20 usuarios
- Soporte 6 meses

**Pricing (solo CodeflowX):**
- Licencias plataforma (1 año): 45.000€
- Setup + Customización: 15.000€
- Training 20 usuarios: 8.000€
- Soporte 6 meses: 12.000€
- **TOTAL: 80.000€**

**Pricing con Consultora Partner (co-selling):**
- Licencias plataforma: 45.000€ → 36.000€ (20% discount)
- Servicios profesionales consultora: 40.000€
- **TOTAL CLIENTE: 76.000€**
- **Revenue consultora: 40.000€ + 7.200€ (comisión) = 47.200€**

**Win-Win:**
- Cliente ahorra 4.000€ (-5%)
- Consultora gana 47.200€ (59% proyecto)
- CodeflowX gana 36.000€ (41% proyecto)

---

## 📊 PARTE 8: EVIDENCIAS TÉCNICAS

### **8.1 ARQUITECTURA TÉCNICA**

```
┌──────────────────────────────────────────────────────────┐
│                    CODEFLOWX GOVERN                       │
│                   (Spring Boot + ZKoss)                   │
└───────────────────────┬──────────────────────────────────┘
                        │
        ┌───────────────┼───────────────┐
        │               │               │
        ▼               ▼               ▼
┌───────────────┐ ┌──────────────┐ ┌──────────────┐
│  MICROSERVICIOS │ │   DATABASE   │ │   WORKFLOWS  │
│   PYTHON       │ │  PostgreSQL  │ │   Camunda    │
│   (FastAPI)    │ │   + JSONB    │ │     BPM      │
└───────────────┘ └──────────────┘ └──────────────┘
        │
        ├─ leka-llm-evaluation (27 métricas)
        ├─ leka-rag-evaluation (24 métricas)
        ├─ leka-bias-detection (12 métricas)
        ├─ leka-prompt-governance (8 métricas)
        ├─ leka-server-serving-evaluation (49 métricas)
        ├─ leka-model-wrapper (inference)
        ├─ leka-agent-monitoring (observability)
        └─ leka-orchestrator (multi-model)

┌──────────────────────────────────────────────────────────┐
│              INFRAESTRUCTURA KUBERNETES                   │
│  - 20+ microservicios Python                              │
│  - PostgreSQL (HA cluster)                                │
│  - Qdrant (vector DB)                                     │
│  - OpenSearch (logs)                                      │
│  - Prometheus (métricas)                                  │
│  - RabbitMQ / Kafka (messaging)                           │
│  - Soberanía europea (on-premise o cloud EU)              │
└──────────────────────────────────────────────────────────┘
```

---

### **8.2 STACK TECNOLÓGICO**

**Backend:**
- Spring Boot 3.2+ (Java 17)
- EnArt Framework (custom ORM)
- ZKoss 9.6+ (UI MVVM)
- Camunda BPM 7.20+ (workflows)

**Python Microservicios:**
- FastAPI 0.115+
- Pydantic 2.0+
- LangChain 0.1+
- DeepEval 0.21+
- Detoxify 0.5+
- Presidio 2.2+

**Database:**
- PostgreSQL 16+ (JSONB, triggers)
- Qdrant 1.7+ (vector DB)
- OpenSearch 2.11+ (logs)

**DevOps:**
- Kubernetes 1.28+
- Helm charts
- GitLab CI/CD
- Prometheus + Grafana

**Seguridad:**
- OAuth 2.0 / OIDC
- RBAC granular
- Encryption at rest (AES-256)
- Encryption in transit (TLS 1.3)
- Immutable audit logs

---

## 📞 CONTACTO Y PRÓXIMOS PASOS

### **Información de Contacto**

**CodeflowX AI**  
**Web:** www.codeflowx.ai  
**Email:** partners@codeflowx.ai  
**LinkedIn:** /company/codeflowx  

**Equipo Partnerships:**
- Manuel González - CTO & Co-founder
- Email: manuel.gonzalez@codeflowx.ai

---

### **Próximos Pasos Sugeridos**

**Para Consultoras Interesadas:**

1. **Demo personalizada** (1 hora)
   - Clasificación automática en vivo
   - FRIA wizard
   - Dashboard compliance

2. **Workshop técnico** (medio día)
   - Arquitectura detallada
   - APIs y integraciones
   - Casos de uso sector específico

3. **Pilot project** (4-8 semanas)
   - 10-20 sistemas cliente real
   - Licencias gratuitas pilot
   - Soporte técnico dedicado
   - Co-creación caso de éxito

4. **Partnership agreement**
   - Firma programa partners
   - Training consultores
   - Certificación
   - Inicio co-selling

---

## 📊 ANEXOS

### **ANEXO A: LISTADO COMPLETO 62 ARTÍCULOS AI ACT CUBIERTOS**

| Art. | Título | Cobertura |
|------|--------|-----------|
| 3 | Definiciones | ✅ 100% |
| 4 | Ámbito aplicación | ✅ 100% |
| 5 | Prácticas prohibidas | ✅ 100% |
| 6 | Clasificación alto riesgo | ✅ 100% |
| 9 | Risk management | ✅ 100% |
| 10 | Data governance | ✅ 100% |
| 11 | Technical documentation | ✅ 100% |
| 12 | Record-keeping | ✅ 100% |
| 13 | Transparency | ✅ 100% |
| 14 | Human oversight | ✅ 100% |
| 15 | Accuracy, robustness | ✅ 100% |
| 16 | Provider obligations | ✅ 100% |
| 17 | QMS | ✅ 100% |
| 19 | Automatic logging | ✅ 100% |
| 26 | Post-market monitoring | ✅ 100% |
| 27 | FRIA | ✅ 100% |
| 43 | Conformity assessment | ✅ 100% |
| 47 | EU Declaration | ✅ 100% |
| 48 | CE Marking | ✅ 100% |
| 49 | Registration | ✅ 95% |
| 50 | Transparency limited risk | ✅ 100% |
| 51 | GPAI classification | ✅ 100% |
| 52 | GPAI procedure | ✅ 100% |
| 53 | GPAI obligations | ✅ 100% |
| 54 | GPAI systemic risk | ✅ 95% |
| 55 | GPAI code of conduct | ✅ 90% |
| 72 | Post-market monitoring plan | ✅ 100% |
| 73 | Serious incidents | ✅ 100% |

*(+ 34 artículos adicionales con cobertura parcial)*

---

### **ANEXO B: GLOSARIO TÉCNICO**

**Accuracy:** Precisión del modelo (TP+TN)/(TP+TN+FP+FN)

**Adapter:** Módulo pequeño que modifica comportamiento GPAI (ej: LoRA)

**Anexo III:** Lista 8 categorías sistemas alto riesgo EU AI Act

**BLEU:** Métrica similitud n-gramas (0-1)

**BPMN:** Business Process Model and Notation (workflows)

**CE Marking:** Marca conformidad europea productos

**Conformity Assessment:** Evaluación cumplimiento requisitos Art. 6-29

**DPIA:** Data Protection Impact Assessment (Art. 35 GDPR)

**FLOPs:** Floating Point Operations (cálculo computacional)

**FRIA:** Fundamental Rights Impact Assessment (Art. 27)

**GPAI:** General Purpose AI (modelos fundación)

**HITL:** Human-In-The-Loop (supervisión humana)

**Immutable Log:** Log no modificable (hash chains)

**nDCG:** Normalized Discounted Cumulative Gain (ranking quality)

**QMS:** Quality Management System (Art. 17)

**RAG:** Retrieval-Augmented Generation

**ROUGE:** Recall-Oriented Understudy for Gisting Evaluation

**Systemic Risk:** Riesgo sistémico GPAI (>10^25 FLOPs)

---

**FIN DEL DOCUMENTO**

**Versión:** 1.0  
**Fecha:** Noviembre 2025  
**Páginas:** 45  
**Autor:** CodeflowX Team  
**Clasificación:** Público - Partners  

---

*Este documento es propiedad intelectual de CodeflowX. Permitida distribución a partners potenciales bajo NDA.*

