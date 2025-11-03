# GAPS DETECTED - EU AI ACT COMPLIANCE VERIFICATION

**Date Started:** November 2, 2025  
**Status:** In Progress  
**Purpose:** Track gaps detected during article-by-article verification against official EU AI Act  
**For:** Internal implementation roadmap

---

## VERIFICATION PROGRESS

### Sección 2: Requisitos de los sistemas de IA de alto riesgo

| Article | Title | Status | Gaps Found |
|---------|-------|--------|------------|
| Art. 6 | Clasificación de sistemas de alto riesgo | ✅ Verified | 1 |
| Art. 9 | Sistema de gestión de riesgos | ✅ Verified | 2 |
| Art. 10 | Datos y gobernanza de datos | ✅ Verified | 1 |
| Art. 11 + Anexo IV | Documentación técnica | ✅ Verified | 4 |
| Art. 12 | Mantenimiento de registros | ✅ Verified | 2 |
| Art. 13 | Transparencia e información | ✅ Verified | 3 |
| Art. 14 | Supervisión humana | ✅ Verified | 3 |
| Art. 15 | Precisión, robustez y ciberseguridad | ✅ Verified | 6 |

### Sección 3: Obligaciones de proveedores e implantadores

| Article | Title | Status | Gaps Found |
|---------|-------|--------|------------|
| Art. 16 | Obligaciones de los proveedores | ✅ Verified | 5 |
| Art. 17 | Sistema de gestión de la calidad | ✅ Verified | 6 |
| Art. 18 | Conservación de la documentación | ✅ Verified | 1 |
| Art. 19 | Registros generados automáticamente | ✅ Verified | 4 |
| Art. 20 | Acciones correctoras y deber de información | ✅ Verified | 3 |
| Art. 21 | Cooperación con las autoridades | ✅ Verified | 0 (covered in GAP-031, GAP-018) |
| Art. 22 | Representantes autorizados | ✅ Verified | 0 (covered in GAP-019) |
| Art. 23 | Obligaciones de los importadores | ✅ Verified | 0 (N/A - not importer) |
| Art. 24 | Obligaciones de los distribuidores | ✅ Verified | 0 (N/A - not distributor) |
| Art. 25 | Responsabilidades cadena de valor | ✅ Verified | 1 |
| Art. 26 | Obligaciones de los implantadores | ✅ Verified | 2 |
| Art. 27 + Anexo IX | Evaluación de impacto derechos fundamentales | ✅ Verified | 3 |

### Sección 4: Autoridades de notificación y organismos notificados

| Article | Title | Status | Gaps Found |
|---------|-------|--------|------------|
| Art. 28-39 | Autoridades y organismos notificados | ✅ Verified | 0 (informativo - no obligations) |

### Sección 5: Normas, evaluación de conformidad, certificados, registro

| Article | Title | Status | Gaps Found |
|---------|-------|--------|------------|
| Art. 40-42 | Normas armonizadas | ✅ Verified | 0 (informativo) |
| Art. 43 + Anexo VI | Evaluación de conformidad | ✅ Verified | 0 (covered in GAP-015) |
| Art. 44-46 | Certificados y salvaguardias | ✅ Verified | 0 (informativo) |
| Art. 47 + Anexo V | Declaración de conformidad UE | ✅ Verified | 1 |
| Art. 48 | Marcado CE | ✅ Verified | 0 (covered in GAP-015) |
| Art. 49 + Anexo VIII | Registro en base de datos UE | ✅ Verified | 1 |

### Sección 6: Post-market monitoring, information sharing, market surveillance

| Article | Title | Status | Gaps Found |
|---------|-------|--------|------------|
| Art. 60 | Pruebas en condiciones reales | ✅ Verified | 0 (N/A - optional) |
| Art. 62 | Notificación de malfuncionamiento | ✅ Verified | 0 (covered in GAP-025) |
| Art. 72 | Vigilancia poscomercialización | ✅ Verified | 0 (covered in GAP-017) |
| Art. 73 | Notificación de incidentes graves | ✅ Verified | 0 (covered in GAP-025) |
| Art. 79 | Evaluación y notificación de riesgos | ✅ Verified | 0 (covered in GAP-031) |

### Capítulo V: Modelos de IA de propósito general (GPAI) - FASE II

| Article | Title | Status | Gaps Found |
|---------|-------|--------|------------|
| Art. 51 + Anexo XIII | Clasificación GPAI riesgo sistémico | ✅ Verified Fase II | 2 |
| Art. 52 | Procedimiento clasificación | ✅ Verified Fase II | 0 (covered in GAP-063) |
| Art. 53 + Anexo XI, XII | Obligaciones proveedores GPAI | ✅ Verified Fase II | 6 |
| Art. 54 | Autoridades GPAI | ✅ Verified Fase II | 0 (informativo) |
| Art. 55 | Obligaciones GPAI riesgo sistémico | ✅ Verified Fase II | 4 (condicional) |
| Art. 56 | Códigos de conducta | ✅ Verified Fase II | 0 (voluntario) |

---

## CRITICAL GAPS (MUST IMPLEMENT)

### GAP-001: Documentación de Sistemas NO Alto Riesgo
**Article:** Art. 6.4  
**Reference:** https://artificialintelligenceact.eu/es/article/6/  
**Priority:** 🔴 Critical  
**Impact:** Certification blocker

**Requirement:**
> "El proveedor que considere que un sistema de IA contemplado en el anexo III no es de alto riesgo **documentará su evaluación** antes de que dicho sistema se comercialice. Dicho proveedor estará sujeto a la **obligación de registro establecida en el apartado 2 del artículo 49**."

**Current State:**
- ✅ We have risk assessment
- ❌ Missing: Specific documentation for systems evaluated as NOT high-risk
- ❌ Missing: Process for registration of these systems in Art. 49.2

**Required Actions:**
1. Create workflow for "Not High-Risk" evaluation documentation
2. Create entity/table for storing these evaluations
3. Integrate with Art. 49.2 registration process
4. Add to BPMN process

**Estimated Effort:** 1 day (1 chat)

---

### GAP-002: Evaluación de Impacto en Menores y Grupos Vulnerables
**Article:** Art. 9.9  
**Reference:** https://artificialintelligenceact.eu/es/article/9/  
**Priority:** 🔴 Critical  
**Impact:** Certification blocker

**Requirement:**
> "Al aplicar el sistema de gestión de riesgos, los proveedores tendrán en cuenta si, habida cuenta de su finalidad prevista, el sistema de IA de alto riesgo **puede tener repercusiones negativas en los menores de 18 años** y, en su caso, **en otros grupos vulnerables**."

**Current State:**
- ✅ We have general risk assessment
- ❌ Missing: Specific assessment for minors (<18 years)
- ❌ Missing: Specific assessment for vulnerable groups
- ❌ Missing: Documentation of these considerations

**Required Actions:**
1. Add "Vulnerable Groups Impact Assessment" module to risk management
2. Create checklist for minors impact
3. Create checklist for vulnerable groups (elderly, disabled, minorities, etc.)
4. Add to risk assessment workflow
5. Create entity/fields to store this information

**Estimated Effort:** 1 day (1 chat)

**Suggested Microservice:** `leka-llm-evaluation` (add new endpoint `/evaluate-vulnerable-groups-impact`)

---

### GAP-003: Proceso Documentado para Datos Sensibles en Detección de Sesgos
**Article:** Art. 10.5  
**Reference:** https://artificialintelligenceact.eu/es/article/10/  
**Priority:** 🔴 Critical  
**Impact:** Certification blocker

**Requirement:**
> "Los proveedores de dichos sistemas podrán **tratar excepcionalmente categorías especiales de datos personales** para detectar y corregir sesgos, **con sujeción a las garantías adecuadas**. Deben cumplirse todas las condiciones siguientes:
> - (a) No puede hacerse con datos sintéticos/anonimizados
> - (b) Limitaciones técnicas de reutilización, pseudonimización
> - (c) Seguridad, acceso controlado
> - (d) No transmisión a terceros
> - (e) Supresión tras corrección
> - (f) Registros de actividades de tratamiento (razones, justificación)"

**Current State:**
- ✅ We detect bias in data
- ❌ Missing: Documented process for exceptional use of sensitive data
- ❌ Missing: Compliance with 6 conditions above
- ❌ Missing: Audit trail for this exceptional processing

**Required Actions:**
1. Create BPMN process for "Exceptional Sensitive Data Use for Bias Correction"
2. Create approval workflow (HITL)
3. Add security controls and logging
4. Create entity to store justifications and audit trail
5. Add automatic deletion after bias correction
6. Document in data governance policy

**Estimated Effort:** 1 day (1 chat)

---

### GAP-009: Detección de Adversarial Examples
**Article:** Art. 15.5  
**Reference:** https://artificialintelligenceact.eu/es/article/15/  
**Priority:** 🔴 Critical  
**Impact:** Certification blocker

**Requirement:**
> "Las soluciones técnicas incluirán medidas para prevenir, detectar, responder, resolver y controlar los ataques que intenten... **las entradas diseñadas para hacer que el modelo de IA cometa un error (ejemplos adversos o evasión de modelos)**."

**Current State:**
- ✅ We have robustness tests
- ❌ Missing: Adversarial examples detection
- ❌ Missing: Adversarial robustness testing (FGSM, PGD, C&W)
- ❌ Missing: Input sanitization against adversarial attacks

**Required Actions:**
1. Create new microservice `leka-adversarial-robustness`
2. Implement adversarial detection algorithms
3. Add adversarial robustness metrics
4. Integrate with model evaluation pipeline

**Estimated Effort:** 2 days (1 chat)

**Suggested Implementation:**
- New microservice: `leka-adversarial-robustness` (port 8012)
- Endpoints: `/detect-adversarial`, `/test-robustness`, `/evaluate-epsilon-robustness`
- Frameworks: Foolbox, ART (Adversarial Robustness Toolbox), CleverHans

---

### GAP-010: Detección de Model Evasion
**Article:** Art. 15.5  
**Reference:** https://artificialintelligenceact.eu/es/article/15/  
**Priority:** 🔴 Critical  
**Impact:** Certification blocker

**Requirement:**
> "Medidas para prevenir, detectar... **evasión de modelos**."

**Current State:**
- ❌ Missing: Model evasion detection
- ❌ Missing: Query-based attack detection
- ❌ Missing: Black-box attack mitigation

**Required Actions:**
1. Add to `leka-adversarial-robustness` microservice
2. Implement query pattern analysis
3. Add rate limiting and anomaly detection for queries
4. Detect extraction attempts

**Estimated Effort:** Included in GAP-009 (same microservice)

---

### GAP-011: Detección de Model Poisoning
**Article:** Art. 15.5  
**Reference:** https://artificialintelligenceact.eu/es/article/15/  
**Priority:** 🔴 Critical  
**Impact:** Certification blocker

**Requirement:**
> "Medidas para prevenir, detectar... **envenenamiento de modelos (componentes preentrenados utilizados en el entrenamiento)**."

**Current State:**
- ❌ Missing: Model poisoning detection
- ❌ Missing: Backdoor detection in pretrained models
- ❌ Missing: Model integrity validation
- ❌ Missing: Supply chain security for models

**Required Actions:**
1. Extend `leka-model-wrapper` or `leka-llm-evaluation`
2. Add model integrity checks
3. Implement backdoor detection
4. Add model provenance tracking
5. Validate checksums/signatures of pretrained models

**Estimated Effort:** 1 day (1 chat)

**Suggested Implementation:**
- Extend `leka-llm-evaluation` with `/detect-model-backdoor` endpoint
- Add model fingerprinting and integrity checks

---

### GAP-012: Detección de Feedback Loop Bias
**Article:** Art. 15.4  
**Reference:** https://artificialintelligenceact.eu/es/article/15/  
**Priority:** 🔴 Critical  
**Impact:** Certification blocker

**Requirement:**
> "Los sistemas de IA de alto riesgo que **sigan aprendiendo después de su comercialización** se desarrollarán de forma que se elimine o reduzca el riesgo de que los **resultados posiblemente sesgados influyan en los datos de entrada para futuras operaciones (bucles de retroalimentación)**, y que se garantice que dichos **bucles de retroalimentación se abordan debidamente con medidas de mitigación adecuadas**."

**Current State:**
- ✅ We have drift detection
- ❌ Missing: Feedback loop bias detection
- ❌ Missing: Retraining-induced bias amplification detection
- ❌ Missing: Mitigation strategies for biased feedback loops

**Required Actions:**
1. Extend `leka-bias-detection-service`
2. Add feedback loop analysis module
3. Detect bias amplification in retraining cycles
4. Add temporal bias tracking
5. Implement mitigation recommendations

**Estimated Effort:** 1 day (1 chat)

**Suggested Implementation:**
- Extend `leka-bias-detection-service` with `/analyze-feedback-loop-bias` endpoint

---

## MEDIUM PRIORITY GAPS (SHOULD IMPLEMENT)

### GAP-004: Pruebas en Condiciones Reales
**Article:** Art. 9.7  
**Reference:** https://artificialintelligenceact.eu/es/article/9/  
**Priority:** 🟡 Medium  
**Impact:** Compliance enhancement

**Requirement:**
> "Los procedimientos de ensayo podrán incluir **ensayos en condiciones reales** de conformidad con el artículo 60."

**Current State:**
- ⚠️ Need to verify: Do we support real-world testing workflows?
- ⚠️ Need to verify: Do we have Art. 60 compliance for real-world testing?

**Required Actions:**
1. Review Art. 60 requirements
2. Determine if platform needs real-world testing support
3. If yes, add workflow and documentation

**Estimated Effort:** TBD (pending Art. 60 review)

---

### GAP-005: Características Geográficas/Contextuales/Conductuales
**Article:** Art. 10.4  
**Reference:** https://artificialintelligenceact.eu/es/article/10/  
**Priority:** 🟡 Medium  
**Impact:** Compliance enhancement

**Requirement:**
> "Los conjuntos de datos tendrán en cuenta, en la medida en que lo exija la finalidad perseguida, las **características o elementos propios del entorno geográfico, contextual, conductual o funcional específico** en el que esté previsto utilizar el sistema de IA de alto riesgo."

**Current State:**
- ⚠️ Need to verify: Do we capture geographical/contextual metadata?
- ⚠️ Need to verify: Do we assess dataset representativeness by context?

**Required Actions:**
1. Add geographical/contextual metadata to dataset assessments
2. Add representativeness checks by context
3. Document in data governance

**Estimated Effort:** 0.5 days (enhancement to existing services)

---

### GAP-006: Rendimiento por Grupos Específicos
**Article:** Art. 13.3(b)(v)  
**Reference:** https://artificialintelligenceact.eu/es/article/13/  
**Priority:** 🟡 Medium  
**Impact:** Documentation completeness

**Requirement:**
> "Las instrucciones de uso contendrán... **su rendimiento en relación con las personas o grupos de personas específicos sobre los que se pretende utilizar el sistema**."

**Current State:**
- ✅ We have general performance metrics
- ⚠️ Need to verify: Do we report performance by demographic groups?
- ⚠️ Need to verify: Do we include this in instructions for use?

**Required Actions:**
1. Add disaggregated performance reporting by groups
2. Include in technical documentation auto-generation
3. Add to instructions for use template

**Estimated Effort:** 0.5 days (enhancement to documentation)

---

### GAP-007: Cambios Predeterminados del Sistema
**Article:** Art. 13.3(c)  
**Reference:** https://artificialintelligenceact.eu/es/article/13/  
**Priority:** 🟡 Medium  
**Impact:** Documentation completeness

**Requirement:**
> "Las instrucciones de uso contendrán... **los cambios en el sistema de IA de alto riesgo y en su funcionamiento que hayan sido predeterminados por el proveedor en el momento de la evaluación inicial de la conformidad**."

**Current State:**
- ⚠️ Need to verify: Do we document predetermined changes (e.g., model updates, retraining schedules)?
- ⚠️ Need to verify: Is this included in instructions for use?

**Required Actions:**
1. Add field for "Predetermined Changes" in model documentation
2. Include in instructions for use template
3. Add to version control documentation

**Estimated Effort:** 0.5 days (documentation enhancement)

---

### GAP-008: Recursos, Vida Útil, Mantenimiento
**Article:** Art. 13.3(e)  
**Reference:** https://artificialintelligenceact.eu/es/article/13/  
**Priority:** 🟡 Medium  
**Impact:** Documentation completeness

**Requirement:**
> "Las instrucciones de uso contendrán... **los recursos informáticos y de hardware necesarios, la vida útil prevista del sistema de IA de alto riesgo y las medidas de mantenimiento y cuidado necesarias, incluida su frecuencia**."

**Current State:**
- ⚠️ Need to verify: Do we document hardware requirements?
- ⚠️ Need to verify: Do we specify expected lifespan?
- ⚠️ Need to verify: Do we document maintenance schedules?

**Required Actions:**
1. Add fields for hardware requirements, lifespan, maintenance schedule
2. Include in instructions for use template
3. Add to deployment documentation

**Estimated Effort:** 0.5 days (documentation enhancement)

---

### GAP-013: Data Poisoning Detection (Específico)
**Article:** Art. 15.5  
**Reference:** https://artificialintelligenceact.eu/es/article/15/  
**Priority:** 🟡 Medium  
**Impact:** Enhanced security

**Requirement:**
> "Medidas para prevenir, detectar... **envenenamiento del conjunto de datos de entrenamiento (envenenamiento de datos)**."

**Current State:**
- ✅ We have data quality checks
- ⚠️ Partial: General outlier detection
- ❌ Missing: Intentional poisoning detection
- ❌ Missing: Malicious data injection detection

**Required Actions:**
1. Extend `leka-bias-detection-service`
2. Add poisoning-specific detection algorithms
3. Add anomaly detection for malicious data
4. Implement data provenance tracking

**Estimated Effort:** 1 day (enhancement to existing service)

---

### GAP-014: Model Inversion / Membership Inference
**Article:** Art. 15.5  
**Reference:** https://artificialintelligenceact.eu/es/article/15/  
**Priority:** 🟡 Medium  
**Impact:** Privacy protection

**Requirement:**
> "Medidas para... **ataques a la confidencialidad**."

**Current State:**
- ✅ We have PII detection
- ✅ We have security scans
- ❌ Missing: Model inversion attack detection
- ❌ Missing: Membership inference attack detection
- ❌ Missing: Data extraction prevention

**Required Actions:**
1. Extend `leka-prompt-governance` or `leka-llm-evaluation`
2. Add membership inference testing
3. Add model inversion detection
4. Implement differential privacy checks

**Estimated Effort:** 1 day (enhancement to existing service)

---

## GAPS FROM PROVIDER OBLIGATIONS (ART. 16-20)

### GAP-015: Conformity Assessment Integration
**Article:** Art. 16.a, 16.b  
**Reference:** EUR-Lex Art. 16 - Obligations of providers  
**Priority:** 🔴 Critical  
**Impact:** Certification blocker

**Requirement:**
> Art. 16: Los proveedores de sistemas de IA de alto riesgo deberán:
> - (a) Garantizar que sus sistemas **se someten al procedimiento de evaluación de la conformidad** con arreglo al artículo 43 antes de su introducción en el mercado
> - (b) Elaborar la **documentación técnica requerida** con arreglo al artículo 11
> - (c) Cuando la conformidad se haya demostrado, elaborar una **declaración de conformidad de la UE** con arreglo al artículo 47 y colocar el **marcado CE** con arreglo al artículo 48

**Current State:**
- ✅ We have technical documentation generation
- ❌ Missing: Formal conformity assessment workflow (Art. 43)
- ❌ Missing: EU Declaration of Conformity generation (Art. 47)
- ❌ Missing: CE marking process (Art. 48)
- ❌ Missing: Integration between assessment → declaration → marking

**Required Actions:**
1. Create BPMN process "Conformity Assessment Workflow" (Art. 43)
2. Create entity `ConformityAssessment` with fields:
   - Assessment type (internal/external)
   - Notified body (if applicable)
   - Assessment date
   - Assessment result
   - Certificate number
3. Create automated "EU Declaration of Conformity" generator (Art. 47)
4. Add CE marking readiness indicator
5. Create entity `EUConformityDeclaration`
6. Link to Art. 49 registration

**Estimated Effort:** 2 days (1 chat for BPMN + Java backend)

---

### GAP-016: Instructions for Use Auto-Generation
**Article:** Art. 16.k  
**Reference:** EUR-Lex Art. 16 - Obligations of providers  
**Priority:** 🔴 Critical  
**Impact:** Certification blocker

**Requirement:**
> Art. 16.k: Los proveedores deberán **proporcionar instrucciones de uso** con arreglo al artículo 13.

**Current State:**
- ✅ We have documentation generation
- ⚠️ Partial: Technical documentation
- ❌ Missing: Formal "Instructions for Use" document per Art. 13
- ❌ Missing: Template for Instructions for Use
- ❌ Missing: Auto-generation from model metadata

**Required Actions:**
1. Create "Instructions for Use" template (per Art. 13 requirements)
2. Add auto-generation from model metadata
3. Include all Art. 13.3 mandatory elements:
   - Identity and contact details
   - Characteristics, capabilities, limitations
   - Performance metrics by group
   - Changes predeterminados
   - Human oversight measures
   - Expected lifetime
   - Computational requirements
4. Create entity `InstructionsForUse`
5. Multilingual support (Spanish, English minimum)

**Estimated Effort:** 1 day (Java backend + template)

---

### GAP-017: Post-Market Monitoring System
**Article:** Art. 16.g, 16.h  
**Reference:** EUR-Lex Art. 16 - Obligations of providers  
**Priority:** 🔴 Critical  
**Impact:** Certification blocker

**Requirement:**
> Art. 16.g: Los proveedores deberán **establecer y documentar un sistema de vigilancia poscomercialización** con arreglo al artículo 72.
> Art. 16.h: Los proveedores deberán **registrar el sistema** de conformidad con el artículo 49.

**Current State:**
- ✅ We have continuous monitoring (drift, performance, bias)
- ❌ Missing: Formal "Post-Market Monitoring Plan" document (Art. 72)
- ❌ Missing: Documented post-market monitoring system
- ❌ Missing: Reporting of serious incidents and malfunctions
- ❌ Missing: Post-market surveillance reports

**Required Actions:**
1. Create entity `PostMarketMonitoringPlan`
2. Create BPMN process "Post-Market Monitoring" (Art. 72)
3. Link to existing monitoring microservices
4. Add "Post-Market Surveillance Report" auto-generation
5. Add "Serious Incident Report" workflow
6. Define incident thresholds and notification triggers
7. Link to Art. 49 registration

**Estimated Effort:** 2 days (Java backend + BPMN + documentation)

---

### GAP-018: Action Obligations for Non-Compliant Systems
**Article:** Art. 16.l, 16.m, 16.n  
**Reference:** EUR-Lex Art. 16 - Obligations of providers  
**Priority:** 🟡 Medium  
**Impact:** Compliance enhancement

**Requirement:**
> Art. 16.l: Cuando tengan razones para considerar que un sistema **no es conforme**, adoptar **inmediatamente las acciones correctoras** necesarias
> Art. 16.m: Cuando el sistema **presente un riesgo**, informar **inmediatamente** a las autoridades nacionales competentes
> Art. 16.n: **Cooperar con las autoridades competentes** y demostrar conformidad cuando se solicite

**Current State:**
- ✅ We have corrective actions tracking
- ⚠️ Partial: Risk notification (internal only)
- ❌ Missing: Notification to competent authorities
- ❌ Missing: Authority cooperation workflow
- ❌ Missing: Formal non-compliance response process

**Required Actions:**
1. Create BPMN process "Non-Compliance Response"
2. Add "Notify Competent Authority" action
3. Create entity `AuthorityNotification` with fields:
   - Authority name
   - Country
   - Date notified
   - Incident description
   - Actions taken
4. Add templates for authority communication
5. Link to Art. 20 corrective actions

**Estimated Effort:** 1 day (Java backend + BPMN)

---

### GAP-019: Authorized Representative Support (If Applicable)
**Article:** Art. 16.o  
**Reference:** EUR-Lex Art. 16 - Obligations of providers  
**Priority:** 🟢 Low  
**Impact:** Conditional (only if non-EU providers)

**Requirement:**
> Art. 16.o: Si el proveedor está **establecido fuera de la UE**, designar un **representante autorizado** con arreglo al artículo 22.

**Current State:**
- ⚠️ Need to determine: Will CodeflowX serve non-EU providers?
- ❌ Missing: Support for authorized representative designation

**Required Actions:**
1. Determine if this applies to CodeflowX use case
2. If yes, add entity `AuthorizedRepresentative`
3. Add fields for representative contact info, mandate
4. Link to provider entity

**Estimated Effort:** 0.5 days (conditional, only if needed)

---

## GAPS FROM QUALITY MANAGEMENT SYSTEM (ART. 17)

### GAP-020: ISO 9001-Compliant Quality Management System
**Article:** Art. 17.1  
**Reference:** EUR-Lex Art. 17 - Quality management system  
**Priority:** 🔴 Critical  
**Impact:** Certification blocker

**Requirement:**
> Art. 17.1: Los proveedores de sistemas de IA de alto riesgo pondrán en marcha un **sistema de gestión de la calidad** que garantice el cumplimiento del presente Reglamento. El sistema de gestión de la calidad se documentará de forma sistemática y ordenada en forma de **políticas, procedimientos e instrucciones por escrito** y comprenderá al menos los siguientes aspectos:
> - (a) Estrategia de cumplimiento normativo
> - (b) Diseño, control de diseño y verificación
> - (c) Técnicas de gestión, validación y aceptación de datos
> - (d) Documentación técnica
> - (e) Gestión de logs
> - (f) Gestión de recursos
> - (g) Sistema de gestión de riesgos (Art. 9)
> - (h) Sistema de vigilancia poscomercialización (Art. 72)
> - (i) Procedimientos de información de incidentes graves y malfuncionamiento
> - (j) Gestión de comunicaciones con autoridades
> - (k) Sistemas y procedimientos para registro de información

**Current State:**
- ✅ We have individual components (risk management, monitoring, logs, etc.)
- ❌ Missing: **Formal Quality Management System (QMS) framework**
- ❌ Missing: QMS documentation structure
- ❌ Missing: QMS policies and procedures (written)
- ❌ Missing: QMS as integrated system (linking all components)

**Required Actions:**
1. Create **Quality Management System** module in Java backend
2. Create entity `QualityManagementSystem` linking to:
   - Risk management processes (Art. 9) ✅ existing
   - Data governance (Art. 10) ✅ existing
   - Technical documentation (Art. 11) ⚠️ partial
   - Record keeping (Art. 12) ⚠️ partial
   - Transparency (Art. 13) ✅ existing
   - Human oversight (Art. 14) ✅ existing
   - Accuracy/robustness (Art. 15) ✅ existing
   - Post-market monitoring (Art. 72) ❌ missing
3. Create BPMN process "QMS Review and Update"
4. Add QMS documentation auto-generation
5. Create QMS policy templates:
   - Compliance strategy
   - Design control
   - Data management
   - Resource management
   - Incident reporting
   - Authority communication
6. Create entity `QMSPolicy` with versioning

**Estimated Effort:** 3 days (2 chats - comprehensive QMS module)

---

### GAP-021: QMS Continuous Improvement and Review
**Article:** Art. 17.1  
**Reference:** EUR-Lex Art. 17 - Quality management system  
**Priority:** 🟡 Medium  
**Impact:** QMS effectiveness

**Requirement:**
> Art. 17.1 (continued): El sistema de gestión de la calidad se **examinará y actualizará** periódicamente.

**Current State:**
- ❌ Missing: QMS periodic review workflow
- ❌ Missing: QMS update tracking
- ❌ Missing: QMS effectiveness metrics

**Required Actions:**
1. Create BPMN process "QMS Periodic Review"
2. Add scheduled QMS reviews (quarterly recommended)
3. Create entity `QMSReview` with fields:
   - Review date
   - Reviewer
   - Findings
   - Action items
   - Effectiveness score
4. Add QMS version control
5. Link to continuous improvement process

**Estimated Effort:** 1 day (included in GAP-020)

---

### GAP-022: Design and Development Control
**Article:** Art. 17.1(b)  
**Reference:** EUR-Lex Art. 17 - Quality management system  
**Priority:** 🟡 Medium  
**Impact:** QMS completeness

**Requirement:**
> Art. 17.1(b): Técnicas, procedimientos y acciones sistemáticas de **diseño, control de diseño y verificación del diseño** del sistema de IA de alto riesgo.

**Current State:**
- ✅ We have model development workflows
- ⚠️ Partial: Design documentation
- ❌ Missing: Formal design control process
- ❌ Missing: Design verification workflow
- ❌ Missing: Design change management

**Required Actions:**
1. Create BPMN process "AI System Design Control"
2. Add design review checkpoints
3. Create entity `DesignControl` with fields:
   - Design specification
   - Design review date
   - Reviewers
   - Verification results
   - Design changes log
4. Link to model versioning
5. Add design verification checklist

**Estimated Effort:** 1 day (enhancement to existing workflows)

---

### GAP-023: Data Management and Validation Procedures
**Article:** Art. 17.1(c)  
**Reference:** EUR-Lex Art. 17 - Quality management system  
**Priority:** 🟡 Medium  
**Impact:** Data governance integration

**Requirement:**
> Art. 17.1(c): Técnicas, procedimientos y acciones sistemáticas de **gestión, validación y aceptación de datos** para la gobernanza de datos.

**Current State:**
- ✅ We have data quality checks (leka-bias-detection-service)
- ✅ We have data validation
- ⚠️ Partial: Formal data acceptance criteria
- ❌ Missing: Documented data validation procedures
- ❌ Missing: Data acceptance workflow (formal approval)

**Required Actions:**
1. Create BPMN process "Data Validation and Acceptance"
2. Add formal data acceptance criteria
3. Create entity `DataAcceptanceCriteria`
4. Add HITL approval for dataset acceptance
5. Document data management procedures
6. Link to existing data quality modules

**Estimated Effort:** 1 day (documentation + BPMN workflow)

---

### GAP-024: Technical Documentation Procedures
**Article:** Art. 17.1(d)  
**Reference:** EUR-Lex Art. 17 - Quality management system  
**Priority:** 🟡 Medium  
**Impact:** Documentation completeness

**Requirement:**
> Art. 17.1(d): Procedimientos para **preparar la documentación técnica** mencionada en el artículo 11, conservarla o actualizarla, y garantizar que sea completa.

**Current State:**
- ✅ We have documentation generation
- ⚠️ Partial: Technical documentation (Art. 11)
- ❌ Missing: Formal procedures for tech doc preparation
- ❌ Missing: Documentation completeness checklist
- ❌ Missing: Documentation update workflow

**Required Actions:**
1. Create BPMN process "Technical Documentation Management"
2. Create completeness checklist (per Art. 11 requirements)
3. Add documentation version control
4. Add documentation review workflow
5. Add documentation update triggers (e.g., after model changes)
6. Link to Art. 11 requirements

**Estimated Effort:** 1 day (documentation procedures + workflow)

---

### GAP-025: Incident and Malfunction Reporting Procedures
**Article:** Art. 17.1(i)  
**Reference:** EUR-Lex Art. 17 - Quality management system  
**Priority:** 🔴 Critical  
**Impact:** Certification blocker

**Requirement:**
> Art. 17.1(i): Procedimientos de **información de incidentes graves** contemplados en el artículo 73 y de **malfuncionamiento** contemplado en el artículo 62.

**Current State:**
- ✅ We have incident tracking
- ❌ Missing: Definition of "serious incident" (Art. 73)
- ❌ Missing: Formal incident reporting workflow
- ❌ Missing: Notification to authorities (Art. 73)
- ❌ Missing: Malfunction reporting (Art. 62)

**Required Actions:**
1. Define "serious incident" thresholds per Art. 73
2. Create BPMN process "Serious Incident Reporting"
3. Create entity `SeriousIncident` with fields:
   - Incident type
   - Severity
   - Date detected
   - Affected systems
   - Root cause
   - Corrective actions
   - Authority notified
4. Add automatic notification triggers
5. Create incident report template
6. Link to Art. 20 corrective actions
7. Add malfunction detection and reporting (Art. 62)

**Estimated Effort:** 2 days (1 chat for incident management module)

---

## GAPS FROM AUTOMATICALLY GENERATED LOGS (ART. 19)

### GAP-026: Log Retention Period and Format
**Article:** Art. 19.1  
**Reference:** EUR-Lex Art. 19 - Automatically generated logs  
**Priority:** 🔴 Critical  
**Impact:** Certification blocker

**Requirement:**
> Art. 19.1: Los sistemas de IA de alto riesgo generarán **registros automáticamente** durante el período de funcionamiento. Los registros serán **completos, precisos e inalterables** y contendrán información sobre:
> - Período cubierto por los registros
> - Información sobre eventos
> - Información necesaria para interpretar los registros
>
> Los registros se conservarán durante un **período apropiado**, que será de al menos **6 meses**, a menos que la legislación de la Unión o nacional aplicable disponga otra cosa.

**Current State:**
- ✅ We have logging (application logs, audit trails)
- ⚠️ Partial: Model execution logs
- ❌ Missing: AI Act-specific log format
- ❌ Missing: Log completeness guarantee (all required fields)
- ❌ Missing: Log immutability guarantee (tamper-proof)
- ❌ Missing: Retention policy enforcement (6 months minimum)
- ❌ Missing: Log archival workflow

**Required Actions:**
1. Define AI Act compliant log format with mandatory fields:
   - Timestamp
   - System ID
   - Model version
   - Input summary (without PII)
   - Output/decision
   - Confidence score
   - Human oversight flag
   - Any errors/warnings
2. Implement **immutable logging** (append-only, cryptographic hash chain)
3. Add log retention policy enforcement
4. Create BPMN process "Log Archival and Retention"
5. Create entity `AIActComplianceLog`
6. Add log export functionality (for auditors)
7. Link to existing monitoring microservices

**Estimated Effort:** 2 days (1 chat for logging infrastructure)

**Technical Requirements:**
- Append-only log storage
- Cryptographic integrity (e.g., Merkle tree or blockchain-like)
- Automated archival after 6 months
- Audit-friendly export format (CSV, JSON)

---

### GAP-027: Log Accessibility for Deployers
**Article:** Art. 19.2  
**Reference:** EUR-Lex Art. 19 - Automatically generated logs  
**Priority:** 🟡 Medium  
**Impact:** Deployer support

**Requirement:**
> Art. 19.2: Los **implantadores de sistemas de IA de alto riesgo** conservarán los registros que generen automáticamente dichos sistemas y que se encuentren **bajo su control**.

**Current State:**
- ✅ We have logs accessible via API
- ❌ Missing: Deployer-specific log access controls
- ❌ Missing: Log handover process (provider → deployer)
- ❌ Missing: Documentation for deployers on log management

**Required Actions:**
1. Add role-based log access for "Deployers"
2. Create deployer-facing log export functionality
3. Add documentation for deployers on:
   - How to access logs
   - Log retention requirements
   - Log format interpretation
4. Create "Log Handover Report" template
5. Link to Art. 26 deployer obligations

**Estimated Effort:** 0.5 days (access control + documentation)

---

### GAP-028: Log-Based Incident Investigation
**Article:** Art. 19.1 (implied)  
**Reference:** EUR-Lex Art. 19 - Automatically generated logs  
**Priority:** 🟡 Medium  
**Impact:** Incident response effectiveness

**Requirement:**
> Art. 19.1 (implicit): Los registros deben contener información suficiente para permitir la **investigación de incidentes** y el **análisis de fallos**.

**Current State:**
- ✅ We have logs
- ⚠️ Partial: Correlation between logs and incidents
- ❌ Missing: Log-based incident investigation workflow
- ❌ Missing: Log search and analysis tools (for auditors)

**Required Actions:**
1. Create "Log Analysis for Incident Investigation" workflow
2. Add log search/filter functionality by:
   - Time range
   - System ID
   - Incident type
   - Error codes
3. Add log correlation with incidents
4. Create log analysis dashboard for auditors
5. Link to GAP-025 (serious incident reporting)

**Estimated Effort:** 1 day (log analysis tools)

---

### GAP-029: Log Security and Access Control
**Article:** Art. 19.1 (implied - immutability)  
**Reference:** EUR-Lex Art. 19 - Automatically generated logs  
**Priority:** 🔴 Critical  
**Impact:** Log integrity

**Requirement:**
> Art. 19.1: Los registros serán **inalterables** (tamper-proof).

**Current State:**
- ⚓ Need to verify: Are current logs tamper-proof?
- ❌ Missing: Cryptographic log integrity verification
- ❌ Missing: Log access audit trail (who accessed logs?)

**Required Actions:**
1. Implement **tamper-proof logging** using:
   - Append-only storage
   - Cryptographic hash chain (each log entry hashes previous)
   - Digital signatures
2. Add log integrity verification
3. Add audit trail for log access
4. Create entity `LogAccessAudit`
5. Implement write-once-read-many (WORM) storage for critical logs

**Estimated Effort:** Included in GAP-026 (same implementation)

---

## GAPS FROM CORRECTIVE ACTIONS (ART. 20)

### GAP-030: Market Withdrawal and Recall Process
**Article:** Art. 20.1, 20.2  
**Reference:** EUR-Lex Art. 20 - Corrective actions and duty to inform  
**Priority:** 🔴 Critical  
**Impact:** Certification blocker

**Requirement:**
> Art. 20.1: Cuando los proveedores de sistemas de IA de alto riesgo tengan razones para considerar que un sistema que han **introducido en el mercado no es conforme** con el presente Reglamento, adoptarán **inmediatamente las acciones correctoras** necesarias para **garantizar la conformidad**, **retirar** dicho sistema o **recuperarlo**.
>
> Art. 20.2: Informarán inmediatamente a los **distribuidores** del sistema de IA de alto riesgo de que se trate y, cuando proceda, al **representante autorizado** y a los **importadores**.

**Current State:**
- ✅ We have corrective actions tracking
- ❌ Missing: Market withdrawal workflow
- ❌ Missing: System recall process
- ❌ Missing: Notification to distributors/importers
- ❌ Missing: Recall tracking and verification

**Required Actions:**
1. Create BPMN process "System Recall and Withdrawal"
2. Create entity `SystemRecall` with fields:
   - Recall date
   - Reason (non-conformity description)
   - Affected systems (list)
   - Distributors notified
   - Importers notified
   - Corrective actions taken
   - Recall completion date
3. Add notification workflow for:
   - Distributors
   - Authorized representatives
   - Importers
4. Add recall verification (confirmation from distributors)
5. Link to Art. 16 provider obligations

**Estimated Effort:** 2 days (1 chat for recall management module)

---

### GAP-031: Authority Notification Process
**Article:** Art. 20.4  
**Reference:** EUR-Lex Art. 20 - Corrective actions and duty to inform  
**Priority:** 🔴 Critical  
**Impact:** Certification blocker

**Requirement:**
> Art. 20.4: Cuando el sistema de IA de alto riesgo **presente un riesgo** a que se refiere el artículo 79, apartado 1, los proveedores también informarán **inmediatamente a las autoridades nacionales competentes** de los Estados miembros en los que hayan puesto a disposición el sistema, proporcionando detalles en particular sobre:
> - La no conformidad y las acciones correctoras adoptadas
> - Los Estados miembros afectados
> - La naturaleza del riesgo

**Current State:**
- ✅ We have risk assessment
- ❌ Missing: Risk threshold definition (Art. 79.1)
- ❌ Missing: Automatic authority notification workflow
- ❌ Missing: Authority contact database (by Member State)
- ❌ Missing: Notification template

**Required Actions:**
1. Define risk thresholds requiring authority notification (per Art. 79.1)
2. Create BPMN process "Notify Competent Authorities"
3. Create entity `AuthorityNotification` with fields:
   - Authority name
   - Member State
   - Date notified
   - Risk description
   - Non-conformity details
   - Corrective actions taken
   - Acknowledgment received
4. Add authority contact database (EU-wide)
5. Create notification template (multilingual)
6. Add automatic notification triggers based on risk level
7. Link to GAP-018 and GAP-025

**Estimated Effort:** 1 day (authority notification module)

---

### GAP-032: Corrective Action Tracking and Effectiveness
**Article:** Art. 20.1 (implied)  
**Reference:** EUR-Lex Art. 20 - Corrective actions and duty to inform  
**Priority:** 🟡 Medium  
**Impact:** Continuous improvement

**Requirement:**
> Art. 20.1 (implicit): Las acciones correctoras deben ser **efectivas** y verificables.

**Current State:**
- ✅ We have corrective actions (ComplianceFinding)
- ⚠️ Partial: Tracking of corrective action status
- ❌ Missing: Corrective action effectiveness measurement
- ❌ Missing: Verification workflow (confirm issue resolved)

**Required Actions:**
1. Extend `ComplianceFinding` entity with:
   - Corrective action effectiveness score
   - Verification date
   - Verifier
   - Verification method
2. Create BPMN process "Corrective Action Verification"
3. Add verification checklist
4. Add corrective action effectiveness metrics
5. Link to QMS continuous improvement (GAP-021)

**Estimated Effort:** 1 day (enhancement to existing module)

---

## GAPS FROM ANNEXES (ART. 11, 27, 47, 49)

### GAP-045: Anexo IV - Technical Documentation Completeness
**Article:** Art. 11 + Anexo IV  
**Reference:** EUR-Lex Anexo IV - Technical documentation  
**Priority:** 🔴 Critical  
**Impact:** Certification blocker

**Requirement:**
> Anexo IV requiere documentación técnica completa incluyendo:
> - Descripción general del sistema (finalidad, versión, proveedor)
> - Descripción detallada (algoritmos, lógica de decisión, componentes clave)
> - Especificaciones técnicas (arquitectura, hardware, software)
> - Descripción del proceso de desarrollo y metodologías de prueba
> - Sistemas de gestión de calidad aplicados
> - Información sobre datos de entrenamiento (fuentes, características, sesgo)
> - Evaluación de precisión, robustez, ciberseguridad
> - Documentación de cambios

**Current State:**
- ✅ We have partial technical documentation
- ⚠️ Partial: Model cards exist
- ❌ Missing: Anexo IV compliant structure
- ❌ Missing: Development process documentation
- ❌ Missing: QMS linkage in documentation
- ❌ Missing: Change log documentation

**Required Actions:**
1. Create "Technical Documentation Generator" (Anexo IV compliant)
2. Create entity `TechnicalDocumentation` with all Anexo IV fields:
   - General description section
   - Detailed description section
   - Technical specifications section
   - Development process section
   - QMS section
   - Training data section
   - Performance evaluation section
   - Change log section
3. Auto-generate from existing metadata
4. Add templates for each section
5. Create version control for documentation
6. Link to GAP-020 (QMS)

**Estimated Effort:** 3 días (2 chats - comprehensive doc generator)

---

### GAP-046: Anexo IV - Development Methodology Documentation
**Article:** Art. 11 + Anexo IV  
**Reference:** EUR-Lex Anexo IV  
**Priority:** 🟡 Medium  
**Impact:** Documentation completeness

**Requirement:**
> Anexo IV requiere documentar:
> - Metodologías de desarrollo utilizadas
> - Técnicas y procedimientos de prueba
> - Resultados de validación
> - Decisiones de diseño y su justificación

**Current State:**
- ⚠️ Partial: Testing procedures exist
- ❌ Missing: Formal development methodology documentation
- ❌ Missing: Design decisions rationale
- ❌ Missing: Validation results documentation

**Required Actions:**
1. Document development lifecycle methodology
2. Create design decision log template
3. Add validation results to technical documentation
4. Link to GAP-022 (design control)

**Estimated Effort:** 1 día (documentation + templates)

---

### GAP-047: Anexo IV - Training Data Documentation
**Article:** Art. 11 + Anexo IV + Art. 10  
**Reference:** EUR-Lex Anexo IV  
**Priority:** 🔴 Critical  
**Impact:** Data governance completeness

**Requirement:**
> Anexo IV requiere documentar datos de entrenamiento:
> - Fuentes y origen de los datos
> - Características principales de los datos
> - Métodos de preparación y procesamiento
> - Evaluación de sesgo en datos
> - Representatividad de los datos

**Current State:**
- ✅ We have data quality assessment
- ✅ We have bias detection
- ⚠️ Partial: Data lineage tracking
- ❌ Missing: Formal Anexo IV training data documentation
- ❌ Missing: Data preparation documentation
- ❌ Missing: Representativeness assessment

**Required Actions:**
1. Extend data governance module
2. Add "Training Data Documentation" section
3. Create entity `TrainingDataDocumentation`
4. Document data sources, characteristics, preparation
5. Link to bias detection results
6. Add representativeness assessment

**Estimated Effort:** 2 días (extend data governance)

---

### GAP-048: Anexo IV - Change Management in Documentation
**Article:** Art. 11 + Anexo IV  
**Reference:** EUR-Lex Anexo IV  
**Priority:** 🟡 Medium  
**Impact:** Documentation maintenance

**Requirement:**
> Anexo IV requiere:
> - Documentación de cambios en el sistema
> - Trazabilidad de versiones
> - Actualización de documentación tras cambios

**Current State:**
- ✅ We have model versioning
- ⚠️ Partial: Change tracking
- ❌ Missing: Formal change documentation
- ❌ Missing: Documentation version sync

**Required Actions:**
1. Create "Change Log" module
2. Automatic documentation update triggers
3. Create entity `SystemChange`
4. Link changes to documentation versions
5. Add change approval workflow

**Estimated Effort:** 1 día (change management)

---

### GAP-049: Anexo V - EU Declaration of Conformity Generator
**Article:** Art. 47 + Anexo V  
**Reference:** EUR-Lex Anexo V - EU Declaration of Conformity  
**Priority:** 🔴 Critical  
**Impact:** Certification blocker

**Requirement:**
> Anexo V requiere Declaración UE de Conformidad con:
> - Identificación del sistema de IA y su tipo
> - Nombre y dirección del proveedor
> - Declaración de conformidad con el Reglamento
> - Referencias a normas armonizadas aplicadas
> - Descripción del procedimiento de evaluación de conformidad
> - Información del organismo notificado (si aplica)
> - Lugar y fecha de emisión
> - Firma autorizada del proveedor

**Current State:**
- ❌ Missing: EU Declaration of Conformity generator
- ❌ Missing: Template Anexo V
- ❌ Missing: Authorized signature workflow

**Required Actions:**
1. Create "EU Declaration of Conformity Generator"
2. Create entity `EUConformityDeclaration` (ya mencionado en GAP-015)
3. Template exactly per Anexo V format
4. Auto-populate from:
   - System metadata
   - Provider information
   - Conformity assessment results
   - Applied harmonized standards
5. Add authorized signature workflow
6. PDF generation with official format
7. Version control of declarations
8. Link to GAP-015 (conformity assessment)

**Estimated Effort:** 1 día (included in GAP-015)

---

### GAP-050: Anexo VIII - Registration Information Completeness
**Article:** Art. 49 + Anexo VIII  
**Reference:** EUR-Lex Anexo VIII - Registration information  
**Priority:** 🔴 Critical  
**Impact:** EU Database registration

**Requirement:**
> Anexo VIII requiere información de registro incluyendo:
> - Nombre, dirección y datos de contacto del proveedor
> - Nombre comercial del sistema de IA
> - Descripción del sistema y su finalidad prevista
> - Categoría de alto riesgo (Anexo III)
> - Estados miembros donde se comercializa
> - Declaración de conformidad UE
> - Instrucciones de uso
> - URL donde se encuentra la información pública
> - Nombre del organismo notificado (si aplica)

**Current State:**
- ⚠️ Partial: Basic system information exists
- ❌ Missing: Anexo VIII compliant registration form
- ❌ Missing: All required fields
- ❌ Missing: Integration with EU database API (pending API release)

**Required Actions:**
1. Create "EU Registration Form" (Anexo VIII compliant)
2. Create entity `EURegistrationInformation`
3. Collect all required fields per Anexo VIII:
   - Provider details (complete)
   - System details (complete)
   - High-risk category classification
   - Member states list
   - Conformity declaration reference
   - Instructions for use URL
   - Public information URL
4. Prepare for EU API integration (when available)
5. Add validation of completeness
6. Link to GAP-049 (declaration) and GAP-016 (instructions)

**Estimated Effort:** 1 día (form + validation) + TBD (API integration when available)

---

### GAP-051: Anexo IX - Fundamental Rights Impact Assessment (FRIA)
**Article:** Art. 27 + Anexo IX  
**Reference:** EUR-Lex Anexo IX - FRIA methodology  
**Priority:** 🔴 Critical  
**Impact:** Certification blocker for deployers (clients)

**Requirement:**
> Anexo IX requiere Evaluación de Impacto en Derechos Fundamentales (FRIA) incluyendo:
> - Identificación de derechos fundamentales afectados
> - Descripción del uso previsto del sistema
> - Evaluación de riesgos para derechos fundamentales:
>   - Dignidad humana
>   - Libertad personal
>   - No discriminación
>   - Privacidad y protección de datos
>   - Libertad de expresión
>   - Derechos del niño
>   - Otros derechos de la Carta
> - Medidas de mitigación
> - Consulta con partes interesadas

**Current State:**
- ✅ We have risk assessment (general)
- ✅ We have bias detection (discrimination)
- ✅ We have PII detection (privacy)
- ❌ Missing: Formal FRIA methodology (Anexo IX)
- ❌ Missing: Fundamental rights checklist
- ❌ Missing: FRIA report generator

**Required Actions:**
1. Create "Fundamental Rights Impact Assessment" module
2. Create entity `FundamentalRightsImpactAssessment`
3. Implement Anexo IX methodology:
   - Rights identification checklist (Carta EU)
   - Risk assessment per right
   - Mitigation measures
   - Stakeholder consultation process
4. Auto-generate FRIA report
5. Link to:
   - GAP-002 (vulnerable groups)
   - Bias detection results
   - Privacy analysis results
6. Add FRIA workflow (HITL approval)
7. **NOTE:** Art. 27 applies to "Deployers" (CodeflowX clients), but CodeflowX should provide tools to facilitate FRIA

**Estimated Effort:** 2 días (FRIA module + report generator)

---

### GAP-052: Anexo IX - Fundamental Rights by Category
**Article:** Art. 27 + Anexo IX  
**Reference:** EUR-Lex Anexo IX  
**Priority:** 🟡 Medium  
**Impact:** FRIA completeness

**Requirement:**
> Anexo IX requiere evaluación específica por categoría de derechos:
> - Artículo 1 Carta: Dignidad humana
> - Artículo 7-8 Carta: Privacidad y protección de datos
> - Artículo 21 Carta: No discriminación
> - Artículo 24 Carta: Derechos del niño
> - Otros artículos relevantes de la Carta

**Current State:**
- ✅ Privacy/data protection covered (Art. 7-8)
- ✅ Non-discrimination covered (Art. 21)
- ⚠️ Partial: Children's rights (Art. 24) via GAP-002
- ❌ Missing: Human dignity assessment (Art. 1)
- ❌ Missing: Systematic assessment by Charter article

**Required Actions:**
1. Create "Charter Rights Checklist" per Anexo IX
2. Assess each relevant Charter article
3. Create templates for each rights category
4. Link to existing modules (privacy, bias, vulnerable groups)
5. Add human dignity assessment

**Estimated Effort:** Included in GAP-051

---

### GAP-053: Anexo IX - Stakeholder Consultation Process
**Article:** Art. 27 + Anexo IX  
**Reference:** EUR-Lex Anexo IX  
**Priority:** 🟡 Medium  
**Impact:** FRIA methodology completeness

**Requirement:**
> Anexo IX requiere:
> - Consulta con partes interesadas relevantes
> - Documentación de feedback recibido
> - Incorporación de retroalimentación en evaluación

**Current State:**
- ❌ Missing: Stakeholder consultation workflow
- ❌ Missing: Feedback collection mechanism
- ❌ Missing: Consultation documentation

**Required Actions:**
1. Create "Stakeholder Consultation" workflow
2. Add feedback collection forms
3. Create entity `StakeholderConsultation`
4. Document consultation results
5. Link to FRIA report

**Estimated Effort:** Included in GAP-051

---

## GAPS FROM RECORD-KEEPING (ART. 12)

### GAP-054: Record-Keeping System Integration
**Article:** Art. 12  
**Reference:** EUR-Lex Art. 12 - Record-keeping  
**Priority:** 🔴 Critical  
**Impact:** Certification blocker

**Requirement:**
> Art. 12: Los sistemas de IA de alto riesgo deberán diseñarse y desarrollarse con capacidades que **permitan el registro automático de eventos (logs)** durante su funcionamiento. Los registros deberán ser **apropiados** para permitir el **monitoreo del funcionamiento** del sistema y facilitar la **investigación de incidentes**.

**Current State:**
- ✅ We have logging capabilities
- ✅ We have monitoring (leka-agent-monitoring)
- ⚠️ Partial: Logs linked to Art. 19 requirements
- ❌ Missing: Formal record-keeping system per Art. 12
- ❌ Missing: Integration between Art. 12 records + Art. 19 logs
- ❌ Missing: Record retention policy (linked to Art. 18)

**Required Actions:**
1. Create "Record-Keeping System" module
2. Differentiate between:
   - Art. 12 records (operational events, system behavior)
   - Art. 19 logs (automatically generated during operation)
3. Create entity `OperationalRecord` (Art. 12)
4. Link to `AIActComplianceLog` (Art. 19 - GAP-026)
5. Add record completeness validation
6. Link to GAP-026 (logs) and GAP-056 (retention)

**Estimated Effort:** 1 día (included in GAP-026 - same logging infrastructure)

---

### GAP-055: Record Accessibility for Monitoring
**Article:** Art. 12  
**Reference:** EUR-Lex Art. 12  
**Priority:** 🟡 Medium  
**Impact:** Operational effectiveness

**Requirement:**
> Art. 12 (implied): Los registros deben ser **accesibles** para permitir **monitoreo del funcionamiento** y **facilitar investigación de incidentes**.

**Current State:**
- ✅ We have monitoring dashboards
- ⚠️ Partial: Record search capabilities
- ❌ Missing: Record query tools for investigations
- ❌ Missing: Record export for external audits

**Required Actions:**
1. Add record search and filter functionality
2. Create "Record Investigation" dashboard
3. Add record export for auditors (CSV, JSON, PDF)
4. Link to GAP-028 (log-based incident investigation)

**Estimated Effort:** Included in GAP-028

---

## GAPS FROM HUMAN OVERSIGHT (ART. 14)

### GAP-056: Human Oversight Measures Documentation
**Article:** Art. 14  
**Reference:** EUR-Lex Art. 14 - Human oversight  
**Priority:** 🔴 Critical  
**Impact:** High-risk system requirement

**Requirement:**
> Art. 14.1: Los sistemas de IA de alto riesgo se diseñarán y desarrollarán de manera que puedan ser **supervisados eficazmente por personas físicas** durante su uso. Las **medidas de supervisión humana** incluirán:
> - (a) Capacidad de comprender completamente las capacidades y limitaciones del sistema
> - (b) Mantener consciencia situacional
> - (c) Detectar y abordar anomalías, disfunciones y rendimiento inesperado
> - (d) Capacidad de decidir no usar el sistema o invalidar decisiones
> - (e) Capacidad de intervenir o interrumpir el sistema

**Current State:**
- ✅ We have HITL (Human-in-the-Loop) workflows in BPMN processes
- ✅ We have human approval checkpoints
- ⚠️ Partial: Documentation of oversight measures
- ❌ Missing: Formal Art. 14 compliant oversight documentation
- ❌ Missing: Override/intervention capabilities documentation
- ❌ Missing: Situational awareness tools

**Required Actions:**
1. Document all HITL measures per Art. 14.1 requirements
2. Create "Human Oversight Documentation" section
3. Create entity `HumanOversightMeasure` with fields:
   - Oversight type (approval, review, intervention, override)
   - System capabilities explained to human
   - Anomaly detection capabilities
   - Intervention mechanisms
   - Override procedures
4. Link to existing BPMN HITL workflows
5. Add to technical documentation (GAP-045)
6. Create "Override Decision Log"

**Estimated Effort:** 2 días (documentation + oversight tracking)

---

### GAP-057: Human Oversight Training Requirements
**Article:** Art. 14.4  
**Reference:** EUR-Lex Art. 14  
**Priority:** 🟡 Medium  
**Impact:** Effective oversight

**Requirement:**
> Art. 14.4: Las medidas de supervisión humana incluirán la asignación del sistema a **personas físicas que dispongan de las competencias, la formación y la autoridad necesarias**, así como del **apoyo necesario**.

**Current State:**
- ⚠️ Assumption: Clients manage their own users
- ❌ Missing: Oversight personnel requirements documentation
- ❌ Missing: Training requirements for overseers
- ❌ Missing: Competency tracking

**Required Actions:**
1. Document required competencies for human overseers
2. Create "Oversight Personnel Requirements" guideline
3. Add training recommendations
4. Create entity `OversightPersonnel` (optional for clients)
5. Provide training materials/documentation for clients

**Estimated Effort:** 1 día (documentation + guidelines)

---

### GAP-058: Oversight Interface Design Requirements
**Article:** Art. 14.3  
**Reference:** EUR-Lex Art. 14  
**Priority:** 🟡 Medium  
**Impact:** Usability and safety

**Requirement:**
> Art. 14.3: La interfaz de usuario se diseñará de manera que las **personas físicas a las que se asignen medidas de supervisión humana** puedan **interpretar los resultados del sistema** y **tomar decisiones informadas**.

**Current State:**
- ✅ We have dashboards and UI
- ✅ We have AI Interpreter for explanations
- ⚠️ Partial: Decision support interfaces
- ❌ Missing: Art. 14.3 compliant UI design documentation
- ❌ Missing: Interpretability validation (can overseers understand?)

**Required Actions:**
1. Document UI design principles per Art. 14.3
2. Add interpretability validation
3. Create "Decision Support Interface" guidelines
4. Ensure all critical decisions have:
   - Clear explanations
   - Confidence scores
   - Override options visible
5. User testing for oversight effectiveness

**Estimated Effort:** Included in GAP-056

---

## GAPS FROM DOCUMENTATION RETENTION (ART. 18)

### GAP-059: Documentation Retention Period Enforcement
**Article:** Art. 18  
**Reference:** EUR-Lex Art. 18 - Retention of documentation  
**Priority:** 🔴 Critical  
**Impact:** Regulatory compliance

**Requirement:**
> Art. 18: El proveedor conservará la **documentación técnica** del sistema de IA de alto riesgo contemplada en el artículo 11, así como la documentación relativa al **sistema de gestión de calidad** contemplado en el artículo 17 y la **documentación relativa al cambio** del sistema, a disposición de las **autoridades nacionales competentes** durante un período de **10 años** a partir de la fecha en que el sistema de IA se haya introducido en el mercado o se haya puesto en servicio.

**Current State:**
- ✅ We have documentation storage
- ❌ Missing: 10-year retention policy enforcement
- ❌ Missing: Automated retention management
- ❌ Missing: Archival workflow for 10-year period
- ❌ Missing: Retention compliance tracking

**Required Actions:**
1. Implement **10-year retention policy** for:
   - Technical documentation (Art. 11 + Anexo IV)
   - QMS documentation (Art. 17)
   - Change documentation
   - Conformity declarations
   - Instructions for use
2. Create automated archival system
3. Create entity `DocumentationRetentionLog`
4. Add retention period tracking per document
5. Add automatic archival after system decommissioning
6. Add deletion workflow (only after 10 years + approval)
7. Link to GAP-045 (technical documentation)
8. Link to GAP-020 (QMS documentation)

**Estimated Effort:** 1 día (retention policy + archival automation)

---

## GAPS FROM VALUE CHAIN & DEPLOYERS (ART. 25, 26)

### GAP-060: Value Chain Responsibility Clarification
**Article:** Art. 25  
**Reference:** EUR-Lex Art. 25 - Responsibilities along the AI value chain  
**Priority:** 🟡 Medium  
**Impact:** Legal clarity

**Requirement:**
> Art. 25: Aclaración de **responsabilidades a lo largo de la cadena de valor** cuando hay múltiples operadores (provider, distributor, deployer, etc.).

**Current State:**
- ⚠️ Need to clarify: CodeflowX role(s) in value chain
- ❌ Missing: Value chain responsibility documentation
- ❌ Missing: Client contracts with AI Act responsibilities

**Required Actions:**
1. Document CodeflowX's role(s): Provider, Platform Operator, Enabler
2. Create "Value Chain Responsibility Matrix"
3. Define where CodeflowX responsibility ends and client responsibility begins
4. Add to legal/contract templates
5. Create entity `ValueChainResponsibility`

**Estimated Effort:** 0.5 días (legal documentation)

---

### GAP-061: Deployer Support Tools (Art. 26)
**Article:** Art. 26  
**Reference:** EUR-Lex Art. 26 - Obligations of deployers  
**Priority:** 🔴 Critical  
**Impact:** Client compliance enablement

**Requirement:**
> Art. 26: Los **implantadores** (deployers - los clientes de CodeflowX) tienen obligaciones incluyendo:
> - Usar el sistema según las instrucciones de uso
> - Asignar supervisión humana
> - Monitorear el funcionamiento
> - Conservar los logs
> - Realizar evaluaciones de impacto de derechos fundamentales (Art. 27)
> - Informar incidentes graves

**Current State:**
- ⚠️ Key insight: **Los clientes de CodeflowX son "Deployers"** y tienen obligaciones propias
- ✅ CodeflowX proporciona herramientas para muchas obligaciones (monitoring, logs, FRIA)
- ❌ Missing: Deployer-specific compliance dashboard
- ❌ Missing: Deployer obligation checklist
- ❌ Missing: Tools to help deployers comply with Art. 26

**Required Actions:**
1. Create "Deployer Compliance Dashboard" for clients
2. Add deployer obligation checklist per Art. 26
3. Provide tools for deployers:
   - Human oversight assignment tracking
   - Log retention for deployers (GAP-027)
   - FRIA tool (GAP-051)
   - Incident reporting (for deployers to report to provider)
4. Create "Deployer Compliance Guide" documentation
5. Create entity `DeployerCompliance` (track client compliance)

**Estimated Effort:** 2 días (deployer support module)

**STRATEGIC IMPORTANCE:** Esto es un **diferenciador clave** - ayudar a clientes (deployers) a cumplir sus obligaciones Art. 26.

---

### GAP-062: Deployer Incident Reporting Channel
**Article:** Art. 26.5  
**Reference:** EUR-Lex Art. 26  
**Priority:** 🟡 Medium  
**Impact:** Incident management completeness

**Requirement:**
> Art. 26.5: Los implantadores deberán **informar a los proveedores** sobre cualquier **incidente grave** o **malfuncionamiento**.

**Current State:**
- ⚠️ Implication: CodeflowX (provider) needs to receive incident reports FROM clients (deployers)
- ❌ Missing: Incident reporting channel for deployers
- ❌ Missing: Deployer-to-provider incident workflow

**Required Actions:**
1. Create "Deployer Incident Reporting" channel
2. Add API/form for deployers to report incidents to CodeflowX
3. Link to GAP-025 (serious incident reporting)
4. Create entity `DeployerIncidentReport`
5. Add notification workflow when deployer reports incident

**Estimated Effort:** Included in GAP-061

---

## GAPS FROM GPAI - FASE II (ART. 51-55 + ANEXOS XI-XIII)

**NOTA:** Estos gaps aplican cuando CodeflowX lance **Fase II: Model Serving/Fine-tuning/RAG as a Service**

### GAP-063: GPAI Model Classification System
**Article:** Art. 51 + Anexo XIII  
**Reference:** EUR-Lex Art. 51 + Anexo XIII  
**Priority:** 🔴 Critical Fase II  
**Impact:** GPAI compliance foundation

**Requirement:**
> Art. 51.1: Un modelo de IA de uso general se considerará con **riesgo sistémico** si cumple uno de los siguientes criterios:
> - Cantidad acumulada de cálculo >10^25 FLOPS
> - Capacidades de alto impacto equivalentes
> 
> Anexo XIII especifica criterios:
> - Número de parámetros
> - Tamaño dataset (tokens)
> - FLOPS de entrenamiento
> - Modalidades (multimodal)
> - >10,000 usuarios profesionales en EU
> - Número de usuarios finales

**Current State:**
- ❌ Missing: GPAI classification system
- ❌ Missing: Systemic risk assessment per Anexo XIII
- ❌ Missing: Tracking of professional users in EU (per model)

**Required Actions:**
1. Create "GPAI Classification System" module
2. Implement Anexo XIII criteria assessment:
   - Track model parameters
   - Document training FLOPS (from Hugging Face metadata)
   - Track EU professional users per model
   - Track end users
   - Assess high-impact capabilities
3. Create entity `GPAIClassification`
4. Automated classification workflow
5. **Determine for each model:** Standard GPAI vs. Systemic Risk GPAI

**Estimated Effort:** 2 días (Fase II - classification module)

---

### GAP-064: GPAI Model Registry with Hugging Face Integration
**Article:** Art. 51, 53  
**Reference:** EUR-Lex Art. 51, 53  
**Priority:** 🔴 Critical Fase II  
**Impact:** GPAI tracking and governance

**Requirement:**
> Art. 53: Proveedores GPAI deben mantener **documentación técnica actualizada**.

**Current State:**
- ⚠️ CodeflowX permite acceso a 3M+ modelos de Hugging Face
- ❌ Missing: GPAI model registry
- ❌ Missing: Tracking which models are used by clients
- ❌ Missing: Integration with Hugging Face metadata

**Required Actions:**
1. Create "GPAI Model Registry" module
2. Integrate with Hugging Face API to fetch:
   - Model cards
   - Technical documentation
   - Licenses
   - Training data info (when available)
3. Create entity `GPAIModelRegistry`
4. Track models used by clients
5. Link to GAP-063 (classification)
6. Add model version tracking

**Estimated Effort:** 2 días (Fase II - HF integration + registry)

---

### GAP-065: Anexo XI - GPAI Technical Documentation
**Article:** Art. 53.1(a) + Anexo XI  
**Reference:** EUR-Lex Anexo XI  
**Priority:** 🔴 Critical Fase II  
**Impact:** GPAI provider compliance

**Requirement:**
> Anexo XI requiere documentación técnica de modelos GPAI incluyendo:
> 1. Descripción general (tareas, arquitectura, parámetros, modalidades, licencia)
> 2. Proceso de desarrollo (métodos entrenamiento, datos, recursos computacionales, energía)

**Current State:**
- ⚠️ If using Hugging Face models: Documentation exists on HF
- ⚠️ If fine-tuning: Need to document changes
- ❌ Missing: Anexo XI compliant documentation access
- ❌ Missing: Fine-tuning documentation (if applicable)

**Required Actions:**
1. **Si modelos son de Hugging Face:**
   - Fetch Anexo XI info from HF model cards
   - Display to clients (downstream providers)
   - Track which info is available vs. missing
2. **Si CodeflowX fine-tunea modelos:**
   - Document fine-tuning process per Anexo XI
   - Document changes to base model
   - Document additional training data
   - Document computational resources used
3. Create entity `GPAITechnicalDocumentation`
4. Link to GAP-064 (model registry)

**Estimated Effort:** 2 días (Fase II - doc aggregation + fine-tuning docs)

---

### GAP-066: Anexo XII - GPAI Transparency Information for Clients
**Article:** Art. 53.1(b) + Anexo XII  
**Reference:** EUR-Lex Anexo XII  
**Priority:** 🔴 Critical Fase II  
**Impact:** Client (downstream provider) compliance

**Requirement:**
> Anexo XII requiere información de transparencia para proveedores posteriores (tus clientes):
> - Descripción general del modelo
> - Tareas que puede realizar
> - Sistemas IA donde puede integrarse
> - Políticas de uso aceptable
> - Fecha lanzamiento y distribución
> - Instrucciones de integración (HW/SW)
> - Modalidades input/output
> - Tamaño máximo (context window)
> - Licencia

**Current State:**
- ⚠️ Hugging Face provides most of this info
- ❌ Missing: Anexo XII compliant format for clients
- ❌ Missing: Integration instructions for CodeflowX platform
- ❌ Missing: Use policies specific to CodeflowX

**Required Actions:**
1. Create "Model Information Sheet" (Anexo XII format)
2. Auto-generate from HF metadata + CodeflowX specifics
3. Display in Developer Portal/Playground
4. Include CodeflowX-specific info:
   - Integration via SDK
   - API endpoints
   - Rate limits
   - Use policies
5. Create entity `GPAITransparencyInfo`
6. **CRITICAL:** Clients NEED this to be compliant as downstream providers

**Estimated Effort:** 1 día (Fase II - info aggregation + display)

---

### GAP-067: Copyright Compliance Policy (GPAI)
**Article:** Art. 53.1(c)  
**Reference:** EUR-Lex Art. 53.1(c) + Directiva (UE) 2019/790  
**Priority:** 🔴 Critical Fase II  
**Impact:** Legal risk (copyright infringement)

**Requirement:**
> Art. 53.1(c): Elaborar y poner a disposición del público una **política de cumplimiento de derechos de autor**, incluyendo:
> - Identificación de reservas de derechos (opt-outs)
> - Cumplimiento con Directiva 2019/790 Art. 4.3
> - Proceso para reclamaciones de copyright

**Current State:**
- ❌ Missing: Public copyright compliance policy
- ❌ Missing: Opt-out mechanism tracking
- ❌ Missing: Copyright claims process

**Required Actions:**
1. Create "Copyright Compliance Policy" (public document)
2. Document:
   - How training data copyright is handled (by HF + CodeflowX)
   - Opt-out mechanism (for content owners)
   - Copyright claims procedure
3. Publish on website
4. Create entity `CopyrightPolicy`
5. Add copyright metadata to GPAI registry

**Estimated Effort:** 1 día (Fase II - legal + documentation)

---

### GAP-068: Public Training Data Summary (GPAI)
**Article:** Art. 53.1(d)  
**Reference:** EUR-Lex Art. 53.1(d)  
**Priority:** 🟡 Medium Fase II  
**Impact:** Transparency requirement

**Requirement:**
> Art. 53.1(d): Elaborar y poner a disposición del público un **resumen del contenido utilizado para entrenamiento** del modelo GPAI.

**Current State:**
- ⚠️ Hugging Face provides training data summaries for most models
- ❌ Missing: Public aggregated summary of training data
- ❌ Missing: Fine-tuning data summary (if applicable)

**Required Actions:**
1. Aggregate training data summaries from HF
2. Publish "Training Data Summary" per model
3. If fine-tuning: Document additional training data
4. Create entity `TrainingDataSummary`

**Estimated Effort:** 0.5 días (Fase II - aggregation)

---

### GAP-069: GPAI Incident Reporting to AI Office (if Systemic Risk)
**Article:** Art. 53.1(e) + Art. 55  
**Reference:** EUR-Lex Art. 53, 55  
**Priority:** 🔴 Critical Fase II (conditional)  
**Impact:** Systemic risk management

**Requirement:**
> Art. 53.1(e): Si modelo GPAI presenta **riesgo sistémico**, notificar **incidentes graves a la Oficina de IA**.

**Current State:**
- ⚠️ Conditional: Only if serving systemic risk models
- ❌ Missing: AI Office notification workflow
- ❌ Missing: Systemic risk incident definition
- ❌ Missing: EU AI Office contact/API

**Required Actions:**
1. **IF** any model has systemic risk (GAP-063 classification):
   - Create "AI Office Notification" workflow
   - Define systemic risk incidents
   - Create entity `SystemicRiskIncident`
   - Add EU AI Office communication channel
2. Link to GAP-063 (classification determines if this applies)

**Estimated Effort:** 1 día (Fase II - conditional, only if systemic risk)

---

### GAP-070: Red Teaming and Adversarial Evaluation (if Systemic Risk)
**Article:** Art. 55.1(a) + Anexo XI Sección 2  
**Reference:** EUR-Lex Art. 55 + Anexo XI  
**Priority:** 🔴 Critical Fase II (conditional)  
**Impact:** Systemic risk mitigation

**Requirement:**
> Art. 55.1(a): Proveedores de GPAI con riesgo sistémico deberán realizar:
> - Evaluaciones del modelo basadas en protocolos públicos
> - Pruebas adversarias (red teaming)
> - Alineación y puesta a punto
> - Documentación de estrategias de evaluación

**Current State:**
- ✅ We have model evaluation (leka-llm-evaluation)
- ⚠️ Partial: Adversarial testing (GAP-009 for high-risk systems)
- ❌ Missing: Formal red teaming protocols for GPAI
- ❌ Missing: Anexo XI Sección 2 documentation

**Required Actions:**
1. **IF** systemic risk model:
   - Implement formal red teaming protocols
   - Document evaluation strategies (Anexo XI Sección 2)
   - Add adversarial testing specific to GPAI
   - Create entity `GPAIRedTeamEvaluation`
2. Link to GAP-009 (adversarial detection)
3. Extend leka-llm-evaluation for GPAI-specific tests

**Estimated Effort:** 2 días (Fase II - conditional, red teaming)

---

### GAP-071: GPAI Cybersecurity and Systemic Risk Mitigation
**Article:** Art. 55.1(b)  
**Reference:** EUR-Lex Art. 55  
**Priority:** 🔴 Critical Fase II (conditional)  
**Impact:** Systemic risk security

**Requirement:**
> Art. 55.1(b): Proveedores GPAI riesgo sistémico deberán garantizar **nivel adecuado de ciberseguridad** para proteger el modelo y su infraestructura.

**Current State:**
- ⚠️ CodeflowX infrastructure security exists
- ❌ Missing: GPAI-specific cybersecurity measures
- ❌ Missing: Systemic risk protection (model theft, poisoning at scale)

**Required Actions:**
1. **IF** systemic risk model:
   - Enhanced cybersecurity for model storage
   - Protection against large-scale attacks
   - Model access controls (stricter)
   - Infrastructure hardening
2. Create entity `GPAICybersecurityMeasures`
3. Document security measures

**Estimated Effort:** 1 día (Fase II - conditional, security enhancement)

---

### GAP-072: GPAI Systemic Risk Tracking and Reporting
**Article:** Art. 55.1(c)  
**Reference:** EUR-Lex Art. 55  
**Priority:** 🟡 Medium Fase II (conditional)  
**Impact:** Systemic risk monitoring

**Requirement:**
> Art. 55.1(c): Seguimiento, documentación y notificación de **incidentes graves** y posibles **medidas correctoras** a la Oficina de IA y autoridades nacionales.

**Current State:**
- ✅ We have incident tracking (GAP-025)
- ❌ Missing: Systemic risk specific incident tracking
- ❌ Missing: EU AI Office reporting (vs. national authorities)

**Required Actions:**
1. Extend GAP-025 (incident reporting) for systemic risk
2. Add "Systemic Impact Assessment" for incidents
3. Add AI Office notification (separate from national authorities)
4. Link to GAP-069

**Estimated Effort:** Included in GAP-069

---

## GAPS SUMMARY - TOTAL COMPLIANCE (FASE I + FASE II)

| Priority | Fase I (Governance) | Fase II (MLOps/GPAI) | TOTAL |
|----------|---------------------|----------------------|-------|
| 🔴 Critical | 29 | 5 (+3 condicional) | **34-37** |
| 🟡 Medium | 20 | 2 | **22** |
| 🟢 Low | 1 | 0 | **1** |
| **TOTAL** | **50** | **7-10** | **57-60** |

---

### **BREAKDOWN DETALLADO:**

#### **FASE I - AI GOVERNANCE (50 gaps):**

**High-Risk Requirements (Art. 6-15):** 21 gaps
- Art. 6: 1 gap
- Art. 9: 2 gaps
- Art. 10: 1 gap
- Art. 11 + Anexo IV: 4 gaps
- Art. 12: 2 gaps
- Art. 13: 3 gaps
- Art. 14: 3 gaps
- Art. 15: 6 gaps

**Provider Obligations (Art. 16-27):** 24 gaps
- Art. 16: 5 gaps
- Art. 17: 6 gaps
- Art. 18: 1 gap
- Art. 19: 4 gaps
- Art. 20: 3 gaps
- Art. 25: 1 gap
- Art. 26: 2 gaps
- Art. 27 + Anexo IX: 3 gaps

**Anexos (Fase I):** 5 gaps adicionales
- Anexo IV: incluido en Art. 11
- Anexo V: 1 gap
- Anexo VIII: 1 gap
- Anexo IX: incluido en Art. 27

**Total Fase I:** 50 gaps (29 críticos, 20 medium, 1 low)

---

#### **FASE II - MLOps/GPAI (7-10 gaps):**

**GPAI Base Requirements (Art. 53):** 5 gaps
- GAP-063: GPAI Classification System
- GAP-064: GPAI Model Registry (HF integration)
- GAP-065: Anexo XI Technical Documentation
- GAP-066: Anexo XII Transparency Info
- GAP-067: Copyright Compliance Policy

**GPAI Medium Priority:** 2 gaps
- GAP-068: Training Data Summary
- GAP-072: Systemic Risk Tracking (conditional)

**GPAI Systemic Risk (condicional - Art. 55):** 3 gaps
- GAP-069: AI Office Incident Reporting
- GAP-070: Red Teaming
- GAP-071: Enhanced Cybersecurity

**Total Fase II:** 7 gaps base + 3 condicional = **10 gaps máximo**

---

### **TOTAL GENERAL:**

**Sin riesgo sistémico:** 57 gaps (50 Fase I + 7 Fase II)  
**Con riesgo sistémico:** 60 gaps (50 Fase I + 10 Fase II)

**Estimación conservadora:** **60 gaps para 100% compliance**

---

## IMPLEMENTATION ROADMAP

### Phase 1: Critical Governance Infrastructure (Week 1 - Days 1-3)
- [ ] GAP-020: Quality Management System (QMS) framework (3 days, 2 chats) - **FOUNDATION**
- [ ] GAP-025: Incident and malfunction reporting (2 days, 1 chat)
- [ ] GAP-026: AI Act compliant immutable logs (2 days, 1 chat)
- [ ] GAP-029: Log security and access control (included in GAP-026)

### Phase 2: Certification Prerequisites (Week 1 - Days 4-5)
- [ ] GAP-015: Conformity assessment integration (2 days, 1 chat)
- [ ] GAP-016: Instructions for use auto-generation (1 day)
- [ ] GAP-017: Post-market monitoring system (2 days)
- [ ] GAP-030: Market withdrawal and recall process (2 days, 1 chat)
- [ ] GAP-031: Authority notification process (1 day)

### Phase 3: Data and Risk Management (Week 2 - Days 1-2)
- [ ] GAP-001: Documentation for non-high-risk systems (1 day)
- [ ] GAP-002: Vulnerable groups impact assessment (1 day)
- [ ] GAP-003: Sensitive data processing for bias (1 day)

### Phase 4: Adversarial Security (Week 2 - Days 3-4)
- [ ] GAP-009: Adversarial examples detection (2 days, new microservice)
- [ ] GAP-010: Model evasion detection (included in GAP-009)
- [ ] GAP-011: Model poisoning detection (1 day, extend LLM evaluation)
- [ ] GAP-012: Feedback loop bias detection (1 day, extend bias detection)

### Phase 5: QMS Components and Enhancements (Week 2 - Days 5+)
- [ ] GAP-021: QMS continuous improvement (included in GAP-020)
- [ ] GAP-022: Design and development control (1 day)
- [ ] GAP-023: Data management and validation procedures (1 day)
- [ ] GAP-024: Technical documentation procedures (1 day)
- [ ] GAP-018: Non-compliant systems response (1 day)

### Phase 6: Medium Priority Enhancements (Week 3)
- [ ] GAP-004 to GAP-008: Documentation enhancements (0.5 days each)
- [ ] GAP-013: Data poisoning detection (1 day)
- [ ] GAP-014: Confidentiality attacks (1 day)
- [ ] GAP-027: Log accessibility for deployers (0.5 days)
- [ ] GAP-028: Log-based incident investigation (1 day)
- [ ] GAP-032: Corrective action effectiveness tracking (1 day)

### Phase 7: Conditional/Optional (As Needed)
- [ ] GAP-019: Authorized representative support (0.5 days, if non-EU providers)

---

## NEW MICROSERVICES REQUIRED

### 1. leka-adversarial-robustness (Port 8012)
**Purpose:** Adversarial robustness testing and detection  
**Covers:** GAP-009, GAP-010  
**Endpoints:**
- `/api/adversarial/detect-examples`
- `/api/adversarial/test-robustness`
- `/api/adversarial/evaluate-epsilon-robustness`
- `/api/adversarial/detect-evasion`

**Frameworks:** Foolbox, ART, CleverHans

---

## MICROSERVICES TO EXTEND

### 1. leka-bias-detection-service
**New Capabilities:**
- Feedback loop bias detection (GAP-012)
- Data poisoning detection (GAP-013)

### 2. leka-llm-evaluation
**New Capabilities:**
- Vulnerable groups impact assessment (GAP-002)
- Model poisoning/backdoor detection (GAP-011)
- Membership inference testing (GAP-014)

### 3. leka-prompt-governance
**New Capabilities:**
- Model inversion detection (GAP-014)

---

## JAVA BACKEND CHANGES REQUIRED

### New Entities/Tables (Priority Order):

#### **CRITICAL - Phase 1-2:**
1. `QualityManagementSystem` (GAP-020) - **FOUNDATION**
2. `QMSPolicy` (GAP-020)
3. `QMSReview` (GAP-021)
4. `SeriousIncident` (GAP-025)
5. `AIActComplianceLog` (GAP-026)
6. `LogAccessAudit` (GAP-029)
7. `ConformityAssessment` (GAP-015)
8. `EUConformityDeclaration` (GAP-015)
9. `InstructionsForUse` (GAP-016)
10. `PostMarketMonitoringPlan` (GAP-017)
11. `SystemRecall` (GAP-030)
12. `AuthorityNotification` (GAP-031)

#### **HIGH - Phase 3-5:**
13. `NotHighRiskEvaluation` (GAP-001)
14. `VulnerableGroupsImpactAssessment` (GAP-002)
15. `SensitiveDataProcessingLog` (GAP-003)
16. `DesignControl` (GAP-022)
17. `DataAcceptanceCriteria` (GAP-023)

#### **MEDIUM - Phase 6:**
18. `AuthorizedRepresentative` (GAP-019, conditional)

### New BPMN Processes (Priority Order):

#### **CRITICAL - Phase 1-2:**
1. "QMS Review and Update" (GAP-020)
2. "QMS Periodic Review" (GAP-021)
3. "Serious Incident Reporting" (GAP-025)
4. "Log Archival and Retention" (GAP-026)
5. "Conformity Assessment Workflow" (GAP-015)
6. "Post-Market Monitoring" (GAP-017)
7. "System Recall and Withdrawal" (GAP-030)
8. "Notify Competent Authorities" (GAP-031)

#### **HIGH - Phase 3-5:**
9. "Evaluate Non-High-Risk System" (GAP-001)
10. "Assess Vulnerable Groups Impact" (GAP-002)
11. "Exceptional Sensitive Data Use" (GAP-003)
12. "AI System Design Control" (GAP-022)
13. "Data Validation and Acceptance" (GAP-023)
14. "Technical Documentation Management" (GAP-024)
15. "Non-Compliance Response" (GAP-018)

#### **MEDIUM - Phase 6:**
16. "Corrective Action Verification" (GAP-032)
17. "Log Analysis for Incident Investigation" (GAP-028)

### New Services (Priority Order):

#### **CRITICAL - Phase 1-2:**
1. `QualityManagementSystemService` (GAP-020) - **FOUNDATION**
2. `SeriousIncidentService` (GAP-025)
3. `AIActComplianceLogService` (GAP-026)
4. `ConformityAssessmentService` (GAP-015)
5. `PostMarketMonitoringService` (GAP-017)
6. `SystemRecallService` (GAP-030)
7. `AuthorityNotificationService` (GAP-031)

#### **HIGH - Phase 3-5:**
8. `NotHighRiskEvaluationService` (GAP-001)
9. `VulnerableGroupsAssessmentService` (GAP-002)
10. `SensitiveDataGovernanceService` (GAP-003)
11. `DesignControlService` (GAP-022)
12. `DataAcceptanceService` (GAP-023)

---

## NEXT ARTICLES TO REVIEW

### High Priority (Sección 2 & 3 - Alto Riesgo)
- [ ] Art. 11 - Documentación técnica
- [ ] Art. 12 - Mantenimiento de registros
- [ ] Art. 14 - Supervisión humana
- [x] **Art. 16 - Obligaciones de proveedores** ✅ **VERIFIED** (5 gaps)
- [x] **Art. 17 - Sistema de gestión de calidad** ✅ **VERIFIED** (6 gaps)
- [ ] Art. 18 - Conservación de la documentación
- [x] **Art. 19 - Registros generados automáticamente** ✅ **VERIFIED** (4 gaps)
- [x] **Art. 20 - Acciones correctoras y deber de información** ✅ **VERIFIED** (3 gaps)
- [ ] Art. 21 - Cooperación con las autoridades competentes
- [ ] Art. 26 - Obligaciones de los implantadores de sistemas de alto riesgo
- [ ] Art. 27 - Evaluación de impacto sobre derechos fundamentales

### Medium Priority (Evaluación de conformidad)
- [ ] Art. 43 - Evaluación de conformidad **(Referenced in GAP-015)**
- [ ] Art. 47 - Declaración de conformidad UE **(Referenced in GAP-015)**
- [ ] Art. 48 - Marcado CE **(Referenced in GAP-015)**
- [ ] Art. 60 - Pruebas en condiciones reales
- [ ] Art. 62 - Malfuncionamiento **(Referenced in GAP-025)**
- [ ] Art. 72 - Post-market monitoring **(Referenced in GAP-017)**
- [ ] Art. 73 - Serious incidents **(Referenced in GAP-025)**
- [ ] Art. 79 - Risk assessment for authority notification **(Referenced in GAP-031)**

### For Information (Roles específicos - posiblemente N/A)
- [ ] Art. 22 - Representantes autorizados de proveedores **(Referenced in GAP-019)**
- [ ] Art. 23 - Obligaciones de los importadores
- [ ] Art. 24 - Obligaciones de los distribuidores
- [ ] Art. 25 - Responsabilidades a lo largo de la cadena de valor

### GPAI (General Purpose AI Models) - **NOT APPLICABLE**
- [x] **Art. 51-56** ✅ **CONFIRMED N/A** (CodeflowX is NOT a GPAI provider)
- [x] **Anexo XI-XIII** ✅ **CONFIRMED N/A**

---

## OFFICIAL SOURCES REVIEWED

1. **AI Act Article Browser** (Future of Life Institute)
   - https://artificialintelligenceact.eu/es/
   - Articles reviewed: 6, 9, 10, 13, 15, 49

2. **EUR-Lex Official Text** (European Union)
   - https://eur-lex.europa.eu/legal-content/ES/TXT/HTML/?uri=OJ:L_202401689
   - Reglamento (UE) 2024/1689 - Official Journal version
   - Reviewed Sections:
     - Sección 3: Obligaciones proveedores e implantadores
     - Sección 4: Autoridades de notificación y organismos notificados
     - Anexo XI: Documentación técnica proveedores GPAI
     - Anexo XII: Información transparencia GPAI
     - Anexo XIII: Criterios clasificación GPAI riesgo sistémico

---

## IMPORTANT OBSERVATIONS

### 📋 GPAI Models (Capítulo V) - NOT APPLICABLE TO CODEFLOWX

**Analysis completed based on EUR-Lex Anexos XI-XIII:**

**Question:** Is CodeflowX a GPAI provider?

**Answer:** **NO**

**Reasoning:**
1. **CodeflowX is a governance/evaluation platform**, NOT a foundation model provider
2. CodeflowX does NOT develop/train/distribute general-purpose AI models
3. CodeflowX evaluates and governs AI systems developed by CLIENTS
4. CodeflowX MAY use GPAI models internally (e.g., Phi-3 Mini for AI Interpreter), but does NOT provide them to market

**GPAI Definition (Anexo XIII criteria):**
- Models with >10^25 FLOPS training compute
- Models with >10,000 professional users in EU
- Multi-modal, high-capacity models
- Models requiring extensive technical documentation per Anexo XI/XII

**CodeflowX's AI Interpreter (Phi-3 Mini):**
- ✅ Used internally for explanation generation
- ✅ NOT provided/distributed to clients
- ✅ NOT a GPAI with systemic risk
- ✅ Small model (~3.8B parameters)
- ✅ Falls below GPAI thresholds

**Conclusion:**
- ✅ **Chapter V (Art. 51-55) is NOT APPLICABLE**
- ✅ **Anexos XI, XII, XIII are NOT APPLICABLE**
- ✅ **Focus remains on Title III (High-Risk Systems)** as CodeflowX is a governance platform provider

**CodeflowX's Role:** **Provider of High-Risk AI System** (AI Governance Platform)

**NOT:** Provider of GPAI models

---

### 📋 GPAI Anexos Summary (For Reference Only)

**Anexo XI - Technical Documentation for GPAI Providers:**
- Model architecture, parameters, training data
- Computational resources, energy consumption
- Training methods, design decisions
- Data provenance, bias detection methods

**Anexo XII - Transparency Information for Downstream Providers:**
- Model capabilities, acceptable use policies
- Input/output formats, context window
- Integration requirements, hardware specs

**Anexo XIII - Criteria for Systemic Risk GPAI Classification:**
- Number of parameters
- Training compute (FLOPS)
- Dataset size/quality
- Market reach (>10,000 professional users in EU)
- Modalities (text-to-text, text-to-image, multimodal)

**Status for CodeflowX:** ❌ NOT APPLICABLE (not a GPAI provider)

---

### 📋 Roles in the AI Value Chain

**Need to clarify CodeflowX's role:**

| Role | Definition | Applicable? |
|------|------------|-------------|
| **Provider** | Develops/places AI system on market | ✅ YES - We provide governance platform |
| **Deployer** | Uses AI system under own authority | ⚠️ PARTIAL - Our clients are deployers |
| **Importer** | Places AI system from 3rd country on EU market | ❓ TBD |
| **Distributor** | Makes AI system available without affecting it | ❓ TBD |
| **Authorized Representative** | Represents non-EU provider | ❌ NO |

**Primary role:** **Provider of High-Risk AI System** (governance/evaluation platform)

**Implication:** Art. 16-21 are CRITICAL for us as providers.

---

### 🔍 Key Articles from EUR-Lex Sección 3

Based on the official text, these articles from Sección 3 are **critical** for CodeflowX as a provider:

**MUST VERIFY NEXT:**

1. **Art. 16** - General obligations of providers (comprehensive list)
2. **Art. 17** - Quality Management System (QMS requirements)
3. **Art. 19** - Automatically generated logs (retention, format, access)
4. **Art. 20** - Corrective actions and duty to inform (incident management)
5. **Art. 27** - Fundamental rights impact assessment (HITL processes)

**These are likely to reveal additional gaps.**

---

## NOTES

- **Art. 49 (Registration):** Already corrected in external compliance document. Pending EU API release (external dependency, not a gap).
- **All gaps** are being tracked for parallel implementation using multiple AI chats.
- **Priority** is based on certification impact and auditor expectations.
- **EUR-Lex text** is the official source and supersedes any other interpretation.
- **Sección 3** (Art. 16-27) is HIGH PRIORITY as it defines provider obligations.

### **MAJOR FINDINGS FROM ART. 16-20 VERIFICATION (November 2, 2025):**

**Article 16-20** (Provider obligations) revealed **18 new gaps**, including:
- **11 Critical gaps** (certification blockers)
- **5 Medium gaps** (compliance enhancement)
- **1 Low gap** (conditional)

**Most Critical Findings:**
1. **GAP-020 (QMS):** Missing formal Quality Management System - **THIS IS THE FOUNDATION** for all other compliance
2. **GAP-026 (Logs):** Current logging not AI Act compliant (immutability, 6-month retention)
3. **GAP-025 (Incidents):** Missing serious incident reporting to authorities (Art. 73)
4. **GAP-015 (Conformity):** Missing conformity assessment workflow (Art. 43), EU Declaration (Art. 47), CE marking (Art. 48)
5. **GAP-017 (Post-Market):** Missing formal post-market monitoring system (Art. 72)

**Impact:** These gaps significantly affect our compliance score. **QMS (GAP-020) must be implemented FIRST** as it's the framework that integrates all other modules.

**Recommendation:** Prioritize Phase 1 (QMS, Incidents, Logs) before proceeding to certification.

---

### **MAJOR FINDINGS FROM ANEXOS IV, V, VIII, IX (November 2, 2025):**

**Anexos críticos** revelaron **9 new gaps adicionales**:
- **5 Critical gaps** (certification blockers)
- **4 Medium gaps** (completeness)

**Most Critical Findings from Annexes:**
1. **GAP-045 (Anexo IV):** Missing Anexo IV compliant technical documentation structure - **CRITICAL FOR CERT**
2. **GAP-047 (Anexo IV):** Missing formal training data documentation per Anexo IV requirements
3. **GAP-049 (Anexo V):** Missing EU Declaration of Conformity generator (Anexo V format)
4. **GAP-050 (Anexo VIII):** Missing EU Registration form with all Anexo VIII fields
5. **GAP-051 (Anexo IX):** Missing Fundamental Rights Impact Assessment (FRIA) methodology

**Key Insights:**
- **Anexo IV (Technical Documentation):** Requires comprehensive 8-section documentation structure. Current model cards insufficient.
- **Anexo V (EU Declaration):** Requires specific format for conformity declaration. Must be auto-generated.
- **Anexo VIII (Registration):** Requires 9+ specific fields for EU database. API integration pending EU release.
- **Anexo IX (FRIA):** Requires systematic fundamental rights assessment per Charter articles. CodeflowX should provide tools for clients.

**Impact:** Anexo IV (technical documentation) is **foundation for certification**. Must be implemented early as it links to:
- Art. 11 (technical documentation requirement)
- GAP-020 (QMS - quality management system)
- GAP-015 (conformity assessment)

**Recommendation:** 
- Implement GAP-045 (Anexo IV documentation) in Phase 1 alongside QMS
- GAP-051 (FRIA) is critical for deployers (clients), CodeflowX provides tools

### **ART. 51-56 VERIFICATION (November 2, 2025):**

User provided links to Art. 51-56 (GPAI - General Purpose AI Models). **Confirmed again: NOT APPLICABLE to CodeflowX.**

**Reasoning:**
- CodeflowX is a **governance platform** (high-risk AI system provider)
- CodeflowX is **NOT a foundation model provider**
- CodeflowX does NOT train/distribute GPAI models
- CodeflowX evaluates and governs AI systems developed by clients
- Internal use of small models (e.g., Phi-3 Mini) does NOT make us a GPAI provider

**Conclusion:** Chapter V (Art. 51-56) and Anexos XI-XIII are **NOT APPLICABLE**. Focus remains on Title III (High-Risk Systems).

---

**Last Updated:** November 2, 2025 - **VERIFICACIÓN COMPLETA 100% FINALIZADA** ✅  

**Articles Verified:** 32 artículos completos (Art. 6, 9-28, 40-49, 51-56, 60, 62, 72, 73, 79)  
**Annexes Verified:** **13 anexos COMPLETOS (I-XIII)** - Verificados contra documento oficial PDF  
**Coverage:** 100% de artículos y anexos aplicables a CodeflowX  

**Gaps Identificados:**
- **Fase I (Governance):** 48 gaps (31 críticos, 16 medium, 1 low)
- **Fase II (MLOps/GPAI):** 15 gaps (10 críticos, 5 medium)
- **Opcionales (Valor Comercial):** 2 gaps
- **TOTAL:** 63 gaps obligatorios + 2 opcionales = **65 gaps total**

**Official Source:** EUR-Lex Reglamento (UE) 2024/1689 | https://eur-lex.europa.eu/legal-content/ES/TXT/HTML/?uri=OJ:L_202401689

---

## 🎯 VERIFICACIÓN COMPLETA - RESUMEN EJECUTIVO

### **ARTÍCULOS VERIFICADOS: 100% COVERAGE**

```
┌────────────────────────────────────────────────────────┐
│ EU AI ACT - VERIFICACIÓN TOTAL COMPLETADA            │
├────────────────────────────────────────────────────────┤
│                                                        │
│ ✅ FASE I (Governance):          32 artículos        │
│    • Sección 2 (Art. 6-15):      8/8 ✅              │
│    • Sección 3 (Art. 16-27):    12/12 ✅             │
│    • Sección 4 (Art. 28-39):     1/1 ✅ (info)       │
│    • Sección 5 (Art. 40-49):     9/9 ✅              │
│    • Sección 6 (Art. 60-79):     5/5 ✅              │
│    • Anexos Fase I:              4/4 ✅              │
│                                                        │
│ ✅ FASE II (MLOps/GPAI):          6 artículos        │
│    • Capítulo V (Art. 51-56):    6/6 ✅              │
│    • Anexos GPAI (XI-XIII):      3/3 ✅              │
│                                                        │
│ 📊 GAPS TOTALES: 57-60                               │
│    • Fase I:  50 gaps                                 │
│    • Fase II:  7-10 gaps (3 condicionales)           │
│                                                        │
│ ✅ VERIFICACIÓN: 100% COMPLETA                       │
└────────────────────────────────────────────────────────┘
```

### **CONCLUSIONES CLAVE:**

1. **CodeflowX tiene DUAL ROL:**
   - ✅ Provider de High-Risk AI System (plataforma de governance)
   - ✅ Provider de GPAI (cuando lance serving/fine-tuning - Fase II)

2. **Compliance actual estimado:** ~70% (infraestructura existe, falta formalización)

3. **Compliance post-implementación:** 100% (ambas fases)

4. **Bloqueadores críticos identificados:**
   - QMS (GAP-020) - FUNDACIÓN
   - Logs inmutables (GAP-026)
   - Technical documentation generator (GAP-045)
   - Conformity assessment workflow (GAP-015)
   - FRIA tools (GAP-051)

5. **Diferenciador estratégico:**
   - GAP-061 (Deployer Support) - Ayudar a clientes a cumplir Art. 26
   - GAP-066 (Anexo XII) - Transparencia para downstream providers

**PRÓXIMO PASO:** Generar documento de PROMPTS para implementar los 60 gaps en paralelo


