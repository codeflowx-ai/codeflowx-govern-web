# PROMPTS 07 - MULTI-FRAMEWORK 100% COMPLIANCE
## Cerrar TODOS los Gaps - Nivel Agresivo Técnico

**Fecha:** Noviembre 2025  
**Objetivo:** 100% compliance en TODOS los frameworks internacionales relevantes  
**Target Frameworks:**
- ✅ EU AI Act (95% → 100%)
- ✅ ISO/IEC 42001:2023 (92% → 100%)
- ✅ ISO/IEC 38507:2022 (75% → 98%)
- ✅ OECD AI Principles (90% → 100%)
- ✅ OECD Accountability (95% → 100%)
- ✅ GDPR (90% → 98%)
- ✅ ISO 27001:2022 (85% → 95%)
- ✅ ISO 27701:2019 (88% → 95%)
- 🇬🇧 ICO UK Guidance (OPCIONAL - solo si venta UK)

**Approach:** NO MVP - Implementación completa, agresiva, best-in-class

---

## 🔄 Avance Implementación (Noviembre 2025)

### 📌 Entregables Completados
- **AI Policy Framework** (`docs/governance/AI_POLICY_FRAMEWORK_ISO42001.md`) formalizado según Clause 5.2, con principios, roles, gestión de riesgos y compromisos EU AI Act / ISO.
- **Módulo AI Objectives (Clause 6.2)**
  - Entidad `AIMOBJECTIVES` + script `sql-scripts/ai_objectives.sql`.
  - Business Service `AIObjectivesBusinessService`, ViewModel `AIObjectivesViewModel` y pantalla `ai_objectives_management.zul`.
- **Módulo Competence & Awareness (Clauses 7.2 / 7.3)**
  - Entidades `AICCOMPETENCE`, `AITTRAININGRECORD` + script `sql-scripts/ai_competence.sql`.
  - Business Service `AICompetenceBusinessService`, ViewModel `AICompetenceViewModel` y pantalla `ai_competence_management.zul`.
  - Documento `AI_AWARENESS_PROGRAM_ISO42001.md` con plan de formación 3.5h anual.

### ⏳ Pendiente por Ejecutar
- **Prompt A.1.4 Inventario**: extender `PRJPROJECTS`, servicios de reporte y pestaña ISO 42001 Inventory.
- **Prompt A.1.5 AIMS Performance**: entidad `APSAIMSPERFORMANCE`, KPIs Clause 9, checklist auditoría y workflow BPMN `iso42001_management_review`.
- **Prompt A.1.6 Continual Improvement**: entidades `ANCNONCONFORMITY` y `AIMPROVEMENT`, workflow de acciones correctivas y dashboard.
- **Prompt A.1.7 Annex A Controls**: matriz `ICOISO42001CONTROL` con seed de 39 controles y UI de cobertura.
- **Prompt A.1.8 Readiness Assessment**: entidad `IRDISO42001READINESS`, lógica de auto-evaluación y reporte ejecutivo PDF.
- **Grupos A.2, A.3, B.1, B.2, C.1**: módulos pendientes (board-level governance, OECD/GDPR, ISO 27001/27701, UK guidance) incluyendo servicios, ZUL, documentación y BPMN.
- Integrar scripts en pipeline de migraciones, generar DTOs/SDK clientes y añadir pruebas automatizadas.

**Total Prompts:** 47 (38 críticos + 9 opcionales UK)

---

## 📋 ÍNDICE POR PRIORIDAD

### **GRUPO A: CRÍTICO EU/GLOBAL (28 prompts) - 2-3 semanas**
- ISO/IEC 42001 gaps (8 prompts)
- ISO/IEC 38507 gaps (10 prompts)
- OECD gaps (5 prompts)
- GDPR gaps (5 prompts)

### **GRUPO B: SECURITY/PRIVACY (10 prompts) - 2 semanas**
- ISO 27001 gaps (6 prompts)
- ISO 27701 gaps (4 prompts)

### **GRUPO C: OPCIONAL UK (9 prompts) - 1 semana**
- ICO UK specific (solo si clientes UK)

---

# GRUPO A: CRÍTICO EU/GLOBAL

---

## 🎯 SECCIÓN A.1 - ISO/IEC 42001:2023 GAPS (8 prompts)

### **Estado Actual:** 92%+ → **Target:** 100%

---

### **PROMPT A.1.1 - AI Policy Framework (ISO 42001 Clause 5.2)**

**Objetivo:** Política IA top-level documentada formalmente según ISO 42001.

**GAP ACTUAL:**
- Tenemos políticas dispersas en BPMNs
- Falta documento formal "AI Policy" único
- ISO 42001 requiere política aprobada por top management

**PROMPT:**

```
Necesito CREAR documento formal "AI Policy Framework" según ISO 42001:2023 Clause 5.2.

REQUISITOS ISO 42001 Clause 5.2:
1. Appropriate to purpose and context of organization
2. Framework for setting AI objectives
3. Commitment to satisfy applicable requirements
4. Commitment to continual improvement of AIMS (AI Management System)

ESTRUCTURA DOCUMENTO:

1. INTRODUCTION
   - Scope: CodeflowX AI operations
   - Purpose: Govern AI development, deployment, monitoring
   - Applicability: All AI systems (models, agents, RAG, prompts)

2. AI GOVERNANCE PRINCIPLES
   - Human oversight (ISO 42001 6.1.2)
   - Transparency and explainability (ISO 42001 6.1.3)
   - Fairness and non-discrimination (ISO 42001 6.1.4)
   - Safety and robustness (ISO 42001 6.1.5)
   - Accountability (ISO 42001 6.1.6)
   - Privacy and data protection (ISO 42001 6.1.7)

3. ROLES AND RESPONSIBILITIES
   - Top management approval
   - AI Officer (who?)
   - Development teams
   - QA/Compliance teams

4. AI SYSTEM LIFECYCLE GOVERNANCE
   - Design phase requirements
   - Development requirements
   - Deployment requirements
   - Monitoring requirements

5. RISK MANAGEMENT
   - Reference to ISO 42001 Clause 6.1
   - Integration with EU AI Act Art. 9

6. CONTINUAL IMPROVEMENT
   - Review frequency (annual minimum)
   - Metrics for improvement
   - Corrective actions

7. COMPLIANCE COMMITMENTS
   - EU AI Act (Reglamento 2024/1689)
   - GDPR
   - ISO standards applicable

OUTPUT:
- Documento markdown ~5-8 páginas
- Formato formal ISO-compliant
- Secciones numeradas
- Aprobación: Top management signature section

UBICACIÓN:
/docs/governance/AI_POLICY_FRAMEWORK_ISO42001.md

Referencias:
- ISO/IEC 42001:2023 Clause 5.2
- Existing: BPMN workflows, Art. 17 QMS modules
```

**Esfuerzo:** 1 día  
**Prioridad:** 🔴 CRÍTICA  
**Framework:** ISO 42001

---

### **PROMPT A.1.2 - AI Objectives and Planning (ISO 42001 Clause 6.2)**

**Objetivo:** Objetivos IA medibles vinculados a política.

**PROMPT:**

```
Necesito CREAR módulo "AI Objectives Management" según ISO 42001 Clause 6.2.

REQUISITOS ISO 42001:
- Objectives must be measurable
- Consistent with AI policy
- Monitored and communicated
- Updated as needed

NUEVA ENTIDAD JAVA (EnArt):

Tabla: AIMOBJECTIVES
Prefijo: AIO
Package: com.codeflowx.govern.entity.governance

Campos:
- idxaiobjective (PK)
- aiodescription (TEXT) - Descripción objetivo
- aiocategory (VARCHAR 50) - TRANSPARENCY, FAIRNESS, ROBUSTNESS, PRIVACY, SAFETY
- aiometric (VARCHAR 100) - Métrica medible (ej: "Explainability coverage >95%")
- aiotargetvalue (DECIMAL) - Valor objetivo
- aiocurrentvalue (DECIMAL) - Valor actual
- aiostatus (VARCHAR 20) - ACTIVE, ACHIEVED, REVISED, DISCONTINUED
- aioresponsible (VARCHAR 100) - Responsable
- aioreviewfrequency (VARCHAR 20) - MONTHLY, QUARTERLY, ANNUAL
- aiolastreviewed (TIMESTAMP)
- aionextreview (TIMESTAMP)
- aiorelatedpolicy (TEXT) - Referencia a sección AI Policy

BUSINESSSERVICE:

Package: com.codeflowx.govern.business.governance
Class: AIObjectivesBusinessService

Métodos:
1. createObjective(objective) - Crear objetivo
2. updateProgress(id, currentValue) - Actualizar progreso
3. reviewObjective(id, decision) - Review periódico
4. generateObjectivesReport() - Reporte ISO 42001 compliance

VIEWMODEL + ZUL:

Pantalla: /console/zul/governance/ai_objectives_management.zul
ViewModel: AIObjectivesViewModel

Funcionalidades UI:
- CRUD objetivos IA
- Dashboard progreso objetivos (gráficos)
- Alertas objetivos no alcanzados
- Export reporte ISO 42001 format

MÉTRICAS EJEMPLO (pre-populate):
1. Transparency: "Explainability coverage models >95%" (target: 95%, current: 92%)
2. Fairness: "Bias detection rate >98%" (target: 98%, current: 96%)
3. Robustness: "Adversarial attacks detected >99%" (target: 99%, current: 97%)
4. Privacy: "PII detection accuracy >99.5%" (target: 99.5%, current: 99.2%)
5. Safety: "Human oversight interventions <2% false positives" (target: <2%, current: 3.1%)

Referencias ISO 42001:
- Clause 6.2: AI objectives
- Clause 9.1: Monitoring, measurement, analysis
```

**Esfuerzo:** 2 días  
**Prioridad:** 🔴 CRÍTICA

---

### **PROMPT A.1.3 - Competence and Awareness (ISO 42001 Clause 7.2, 7.3)**

**Objetivo:** Gestión competencias equipo IA + awareness program.

**PROMPT:**

```
Necesito CREAR módulo "AI Competence & Awareness Management" según ISO 42001 Clauses 7.2, 7.3.

ISO 42001 REQUIREMENTS:
- Clause 7.2: Determine necessary competence
- Clause 7.3: Ensure persons aware of AI policy, objectives, risks

NUEVA ENTIDAD: AICOMPETENCE

Tabla: AICCOMPETENCE
Prefijo: AIC
Package: com.codeflowx.govern.entity.governance

Campos:
- idxaiccompetence (PK)
- aicpersonname (VARCHAR 100)
- aicpersonemail (VARCHAR 100)
- aicrole (VARCHAR 50) - AI_DEVELOPER, AI_ENGINEER, DATA_SCIENTIST, QA, COMPLIANCE
- aicrequiredcompetencies (TEXT) - JSON array competencias requeridas rol
- aiccurrentcompetencies (TEXT) - JSON array competencias actuales
- aicgapanalysis (TEXT) - Gaps identificados
- aictrainingplan (TEXT) - Plan formación
- aiclastawarenesstrained (TIMESTAMP) - Última formación awareness
- aicawarenesslevel (VARCHAR 20) - NONE, BASIC, INTERMEDIATE, ADVANCED
- aiccertifications (TEXT) - Certificaciones externas (ej: ISO 42001 Lead Implementer)

NUEVA ENTIDAD: AITRAININGRECORD

Tabla: AITTRAININGRECORD
Prefijo: AIT
Package: com.codeflowx.govern.entity.governance

Campos:
- idxaittraining (PK)
- aitpersonid (FK → AICCOMPETENCE)
- aittrainingtype (VARCHAR 50) - AWARENESS, TECHNICAL, COMPLIANCE, CERTIFICATION
- aittrainingtitle (VARCHAR 200)
- aittrainingdate (TIMESTAMP)
- aitduration (INTEGER) - Horas
- aitprovider (VARCHAR 100) - Proveedor formación
- aitcertificateobtained (BOOLEAN)
- aitcertificateurl (TEXT) - URL certificado

BUSINESSSERVICE:

Class: AICompetenceBusinessService

Métodos:
1. assessCompetenceGap(personId) - Gap analysis
2. createTrainingPlan(personId, gaps) - Plan formación
3. recordTraining(trainingRecord) - Registro formación
4. generateCompetenceReport() - Reporte ISO 42001

AWARENESS PROGRAM CONTENT (Documentar):

Crear documento: /docs/governance/AI_AWARENESS_PROGRAM_ISO42001.md

Contenido:
1. AI Policy overview (30 min)
2. ISO 42001 requirements relevant to role (1 hour)
3. EU AI Act obligations (1 hour)
4. Risk management AI (30 min)
5. Incident reporting procedures (30 min)

Total: 3.5 horas awareness básico
Frequency: Anual + onboarding nuevos

PANTALLA ZUL:

/console/zul/governance/ai_competence_management.zul

Funcionalidades:
- Matriz competencias por rol
- Gap analysis visual
- Training plan tracking
- Awareness program schedule
- Certificates repository

Referencias ISO 42001:
- Clause 7.2: Competence
- Clause 7.3: Awareness
- Clause 7.4: Communication (relacionado)
```

**Esfuerzo:** 3 días  
**Prioridad:** 🔴 CRÍTICA

---

### **PROMPT A.1.4 - AI System Inventory (ISO 42001 Clause 8.1)**

**Objetivo:** Inventario completo sistemas IA con metadata governance.

**PROMPT:**

```
Necesito EXTENDER entidad existente PROJECT con metadata ISO 42001 AI System Inventory.

ISO 42001 REQUIREMENT:
- Clause 8.1: Organization shall plan, implement, control AI system processes
- Inventory debe incluir: purpose, lifecycle stage, risk level, controls applied

MODIFICACIÓN ENTIDAD EXISTING:

Tabla: PRJPROJECTS (ya existe)
Añadir campos (ALTER TABLE):

- prjaisystemtype (VARCHAR 50) - MODEL, AGENT, RAG, PROMPT_TEMPLATE, HYBRID
- prjailifecyclestage (VARCHAR 50) - DESIGN, DEVELOPMENT, TESTING, DEPLOYMENT, MONITORING, DECOMMISSIONED
- prjairisklevel (VARCHAR 20) - HIGH_RISK, LIMITED_RISK, MINIMAL_RISK, PROHIBITED (según EU AI Act)
- prjaipurpose (TEXT) - Propósito del sistema IA
- prjaiintendeduse (TEXT) - Uso previsto
- prjaiusers (TEXT) - Usuarios previstos (demográficos, contexto)
- prjaicontrolsapplied (TEXT) - JSON array controles ISO 42001 aplicados
- prjaiperformancemetrics (TEXT) - JSON métricas rendimiento esperadas
- prjailastinventoryreview (TIMESTAMP) - Última revisión inventario
- prjainextinventoryreview (TIMESTAMP) - Próxima revisión

MIGRATION SQL:

ALTER TABLE PRJPROJECTS 
ADD COLUMN prjaisystemtype VARCHAR(50),
ADD COLUMN prjailifecyclestage VARCHAR(50) DEFAULT 'DESIGN',
ADD COLUMN prjairisklevel VARCHAR(20),
ADD COLUMN prjaipurpose TEXT,
ADD COLUMN prjaiintendeduse TEXT,
ADD COLUMN prjaiusers TEXT,
ADD COLUMN prjaicontrolsapplied TEXT,
ADD COLUMN prjaiperformancemetrics TEXT,
ADD COLUMN prjailastinventoryreview TIMESTAMP,
ADD COLUMN prjainextinventoryreview TIMESTAMP;

-- Índices
CREATE INDEX idx_prj_aisystemtype ON PRJPROJECTS(prjaisystemtype);
CREATE INDEX idx_prj_ailifecyclestage ON PRJPROJECTS(prjailifecyclestage);
CREATE INDEX idx_prj_airisklevel ON PRJPROJECTS(prjairisklevel);

BUSINESSSERVICE EXTENSION:

Class: ProjectBusinessService (extender existente)

Nuevos métodos:
1. updateAIInventoryMetadata(projectId, metadata) - Actualizar metadata ISO 42001
2. reviewAISystemInventory(projectId) - Marcar como revisado
3. generateAIInventoryReport() - Reporte completo inventario
4. getSystemsByLifecycleStage(stage) - Filtrar por etapa
5. getSystemsByRiskLevel(risk) - Filtrar por riesgo

PANTALLA ZUL EXTENSION:

Extender: /console/zul/projects/project_detail.zul

Nueva pestaña: "ISO 42001 Inventory"

Campos adicionales en form:
- AI System Type (dropdown)
- Lifecycle Stage (dropdown con workflow state sync)
- Risk Level (auto-calculated from Anexo III + manual override)
- Purpose (textarea)
- Intended Use (textarea)
- Users (textarea)
- Controls Applied (multi-select de Anexo A ISO 42001)
- Performance Metrics (JSON editor)

REPORTE ISO 42001 INVENTORY:

Formato: Excel + PDF
Columnas:
- Project ID
- System Name
- AI System Type
- Lifecycle Stage
- Risk Level
- Purpose
- Controls Applied (count + list)
- Last Review Date
- Next Review Date
- Responsible Person

Referencias ISO 42001:
- Clause 8.1: Operational planning and control
- Annex A: Controls (39 controles)
```

**Esfuerzo:** 2 días  
**Prioridad:** 🔴 MUY CRÍTICA

---

### **PROMPT A.1.5 - Performance Evaluation (ISO 42001 Clause 9)**

**Objetivo:** Evaluación rendimiento AIMS (AI Management System).

**PROMPT:**

```
Necesito CREAR módulo "AIMS Performance Evaluation" según ISO 42001 Clause 9.

ISO 42001 REQUIREMENTS:
- Clause 9.1: Monitoring, measurement, analysis, evaluation
- Clause 9.2: Internal audit
- Clause 9.3: Management review

NUEVA ENTIDAD: AIMSPERFORMANCE

Tabla: APSAIMSPERFORMANCE
Prefijo: APS
Package: com.codeflowx.govern.entity.governance

Campos:
- idxapsperformance (PK)
- apsperiod (VARCHAR 20) - MONTHLY, QUARTERLY, ANNUAL
- apsperiodstart (TIMESTAMP)
- apsperiodend (TIMESTAMP)
- apskpimetrics (TEXT) - JSON KPIs ISO 42001
- apscompliancescore (DECIMAL) - Score 0-100
- apsnonconformities (TEXT) - JSON lista no conformidades
- apscorrectiveactions (TEXT) - JSON acciones correctivas
- apsauditdate (TIMESTAMP) - Fecha internal audit
- apsauditor (VARCHAR 100)
- apsauditfindings (TEXT)
- apsmanagementreviewdate (TIMESTAMP)
- apsmanagementdecisions (TEXT) - Decisiones management review
- apsnextreview (TIMESTAMP)

BUSINESSSERVICE:

Class: AIMSPerformanceBusinessService

Métodos:
1. calculatePeriodPerformance(periodStart, periodEnd) - Calcular KPIs periodo
2. recordInternalAudit(auditData) - Registrar auditoría interna
3. recordManagementReview(reviewData) - Registrar management review
4. generatePerformanceReport(periodId) - Reporte ISO 42001 Clause 9
5. getNonConformities() - Lista no conformidades pendientes
6. trackCorrectiveAction(actionId) - Tracking acciones correctivas

KPIs ISO 42001 (auto-calculate):

1. AI Objectives Achievement Rate
   - Formula: (objectives achieved / total objectives) * 100
   - Target: >90%

2. Incident Response Time
   - Formula: AVG(incident close time)
   - Target: <24h

3. Training Compliance Rate
   - Formula: (persons trained / total persons) * 100
   - Target: 100%

4. Risk Treatment Effectiveness
   - Formula: (risks mitigated / total risks) * 100
   - Target: >95%

5. Audit Non-Conformities
   - Formula: COUNT(non-conformities)
   - Target: <5 per audit

6. AI System Inventory Coverage
   - Formula: (systems documented / total systems) * 100
   - Target: 100%

INTERNAL AUDIT CHECKLIST (Template):

Crear: /docs/governance/ISO42001_INTERNAL_AUDIT_CHECKLIST.md

Secciones:
- Clause 4: Context of organization
- Clause 5: Leadership (policy review)
- Clause 6: Planning (objectives, risks)
- Clause 7: Support (competence, awareness)
- Clause 8: Operation (AI system processes)
- Clause 9: Performance evaluation (esta sección)
- Clause 10: Improvement

PANTALLA ZUL:

/console/zul/governance/aims_performance.zul

Funcionalidades:
- Dashboard KPIs ISO 42001
- Internal audit scheduler
- Management review scheduler
- Non-conformities tracking
- Corrective actions workflow
- Export audit report ISO 42001 format

WORKFLOW BPMN:

Crear: /workflow/iso42001_management_review.bpmn

Steps:
1. Schedule management review (quarterly)
2. Prepare performance data (auto)
3. Review by top management
4. Document decisions
5. Track action items
6. Archive review record

Referencias ISO 42001:
- Clause 9.1: Monitoring, measurement, analysis, evaluation
- Clause 9.2: Internal audit
- Clause 9.3: Management review
```

**Esfuerzo:** 3 días  
**Prioridad:** 🔴 CRÍTICA

---

### **PROMPT A.1.6 - Continual Improvement (ISO 42001 Clause 10)**

**Objetivo:** Proceso mejora continua AIMS documentado.

**PROMPT:**

```
Necesito CREAR módulo "AIMS Continual Improvement" según ISO 42001 Clause 10.

ISO 42001 REQUIREMENTS:
- Clause 10.1: General (continual improvement obligation)
- Clause 10.2: Nonconformity and corrective action
- Clause 10.3: Continual improvement

NUEVA ENTIDAD: AIMSNONCONFORMITY

Tabla: ANCNONCONFORMITY
Prefijo: ANC
Package: com.codeflowx.govern.entity.governance

Campos:
- idxancnonconformity (PK)
- ancdescription (TEXT) - Descripción no conformidad
- ancsource (VARCHAR 50) - INTERNAL_AUDIT, EXTERNAL_AUDIT, INCIDENT, MONITORING, CUSTOMER
- ancseverity (VARCHAR 20) - CRITICAL, MAJOR, MINOR
- ancclause (VARCHAR 50) - ISO 42001 clause afectada
- ancdetecteddate (TIMESTAMP)
- ancdetectedby (VARCHAR 100)
- ancrootcause (TEXT) - Análisis causa raíz
- anccorrectiveaction (TEXT) - Acción correctiva propuesta
- ancresponsible (VARCHAR 100)
- ancduedate (TIMESTAMP)
- ancstatus (VARCHAR 20) - OPEN, IN_PROGRESS, RESOLVED, VERIFIED, CLOSED
- ancresolutiondate (TIMESTAMP)
- ancverifiedby (VARCHAR 100)
- ancverificationdate (TIMESTAMP)
- anceffectivenesscheck (TEXT) - Verificación efectividad acción

NUEVA ENTIDAD: AIMSIMPROVEMENT

Tabla: AIMPROVEMENT
Prefijo: AIM
Package: com.codeflowx.govern.entity.governance

Campos:
- idxaimprovement (PK)
- aimdescription (TEXT) - Descripción mejora propuesta
- aimsource (VARCHAR 50) - MANAGEMENT_REVIEW, AUDIT, SUGGESTION, INNOVATION
- aimcategory (VARCHAR 50) - PROCESS, TECHNOLOGY, TRAINING, DOCUMENTATION
- aimexpectedbenefit (TEXT) - Beneficio esperado
- aimproposedby (VARCHAR 100)
- aimproposaldate (TIMESTAMP)
- aimstatus (VARCHAR 20) - PROPOSED, APPROVED, IMPLEMENTING, COMPLETED, REJECTED
- aimapprovedby (VARCHAR 100)
- aimapprovaldate (TIMESTAMP)
- aimimplementationplan (TEXT)
- aimresponsible (VARCHAR 100)
- aimestimatedeffort (INTEGER) - Días estimados
- aimcompletiondate (TIMESTAMP)
- aimactualben benefit (TEXT) - Beneficio real obtenido
- aimlessonslearned (TEXT)

BUSINESSSERVICE:

Class: AIMSImprovementBusinessService

Métodos:
1. reportNonConformity(nonConformity) - Reportar no conformidad
2. analyzeRootCause(nonConformityId) - Análisis causa raíz (plantilla 5-Why)
3. createCorrectiveAction(nonConformityId, action) - Acción correctiva
4. verifyCorrectiveAction(nonConformityId, verifier) - Verificar efectividad
5. proposeImprovement(improvement) - Proponer mejora
6. approveImprovement(improvementId, approver) - Aprobar mejora
7. trackImprovementImplementation(improvementId) - Track implementación
8. generateImprovementReport() - Reporte mejora continua

ROOT CAUSE ANALYSIS TEMPLATE (5-Why):

Integrar en pantalla:
- Why 1: [Input text]
- Why 2: [Input text]
- Why 3: [Input text]
- Why 4: [Input text]
- Why 5 (Root Cause): [Input text]

WORKFLOW BPMN:

Crear: /workflow/iso42001_corrective_action.bpmn

Steps:
1. Report non-conformity
2. Assign to responsible
3. Root cause analysis (5-Why)
4. Define corrective action
5. Approve corrective action
6. Implement corrective action
7. Verify effectiveness
8. Close non-conformity

PANTALLA ZUL:

/console/zul/governance/aims_improvement.zul

Pestañas:
1. Non-Conformities
   - Lista no conformidades (filtros: status, severity, clause)
   - Form root cause analysis
   - Corrective actions tracking
   
2. Improvements
   - Lista mejoras propuestas
   - Approval workflow
   - Implementation tracking
   - Benefits realization

3. Dashboard
   - Non-conformities by clause (chart)
   - Time to resolution (KPI)
   - Improvements implemented (trend)
   - ROI improvements (if quantifiable)

MÉTRICAS CONTINUAL IMPROVEMENT:

1. Mean Time To Resolution (MTTR) - Non-conformities
2. Recurrence Rate - Same root cause
3. Improvement Proposal Rate - Per person/quarter
4. Improvement Implementation Rate - Approved vs completed
5. Effectiveness Score - Corrective actions verified effective

Referencias ISO 42001:
- Clause 10.1: General
- Clause 10.2: Nonconformity and corrective action
- Clause 10.3: Continual improvement
```

**Esfuerzo:** 3 días  
**Prioridad:** 🔴 CRÍTICA

---

### **PROMPT A.1.7 - ISO 42001 Annex A Controls Coverage Matrix**

**Objetivo:** Matriz cobertura 39 controles Anexo A ISO 42001.

**PROMPT:**

```
Necesito CREAR matriz cobertura "ISO 42001 Annex A Controls" con evidencias.

ISO 42001 ANNEX A:
39 controles AI-específicos agrupados en:
- A.1: Organizational controls (5 controles)
- A.2: People controls (3 controles)
- A.3: Physical controls (2 controles)
- A.4: Technological controls (8 controles)
- A.5: Data controls (7 controles)
- A.6: Model controls (6 controles)
- A.7: AI system controls (8 controles)

NUEVA ENTIDAD: ISO42001CONTROL

Tabla: ICOISO42001CONTROL
Prefijo: ICO
Package: com.codeflowx.govern.entity.governance

Campos:
- idxicocontrol (PK)
- icocontrolid (VARCHAR 20) - Ej: "A.1.1", "A.5.3"
- icocontrolname (VARCHAR 200)
- icocontrolcategory (VARCHAR 50) - ORGANIZATIONAL, PEOPLE, PHYSICAL, TECHNOLOGICAL, DATA, MODEL, SYSTEM
- icocontroldescription (TEXT) - Descripción control
- icoimplementationstatus (VARCHAR 20) - NOT_IMPLEMENTED, PARTIAL, IMPLEMENTED, NOT_APPLICABLE
- icocoveragepercentage (DECIMAL) - 0-100%
- icoevidence (TEXT) - Evidencias implementación (URLs, docs, code)
- icoresponsible (VARCHAR 100)
- icolastassessed (TIMESTAMP)
- iconextassessment (TIMESTAMP)
- icocomments (TEXT)

BUSINESSSERVICE:

Class: ISO42001ControlsBusinessService

Métodos:
1. loadAnnexAControls() - Cargar 39 controles (seed data)
2. assessControlCoverage(controlId) - Evaluar cobertura
3. updateControlEvidence(controlId, evidence) - Actualizar evidencias
4. generateAnnexAComplianceReport() - Reporte cobertura
5. getControlsByCoverage(threshold) - Filtrar por % cobertura
6. calculateOverallCompliance() - % general compliance Anexo A

SEED DATA (39 controles - pre-populate):

INSERT INTO ICOISO42001CONTROL VALUES:

-- A.1 Organizational controls
('A.1.1', 'AI policy', 'ORGANIZATIONAL', 'Organization shall establish AI policy...', 'IMPLEMENTED', 100, '/docs/governance/AI_POLICY_FRAMEWORK_ISO42001.md', 'CTO', NOW(), NOW() + INTERVAL '6 months', 'Policy created per Prompt A.1.1'),

('A.1.2', 'AI objectives', 'ORGANIZATIONAL', 'Organization shall establish AI objectives...', 'IMPLEMENTED', 100, 'Table: AIMOBJECTIVES', 'CTO', NOW(), NOW() + INTERVAL '3 months', 'Module created per Prompt A.1.2'),

('A.1.3', 'Roles and responsibilities', 'ORGANIZATIONAL', 'Roles and responsibilities for AIMS...', 'PARTIAL', 85, 'AI Policy doc + AICOMPETENCE table', 'HR', NOW(), NOW() + INTERVAL '3 months', 'Need formal RACI matrix'),

('A.1.4', 'Risk assessment', 'ORGANIZATIONAL', 'Organization shall identify AI risks...', 'IMPLEMENTED', 95, 'Art. 9 Risk Management System', 'Risk Officer', NOW(), NOW() + INTERVAL '6 months', 'EU AI Act Art. 9 covers this'),

('A.1.5', 'Legal and regulatory requirements', 'ORGANIZATIONAL', 'Determine applicable legal requirements...', 'IMPLEMENTED', 100, 'EU AI Act 32 articles + GDPR compliance', 'Legal', NOW(), NOW() + INTERVAL '12 months', '100% EU AI Act + GDPR'),

-- A.2 People controls
('A.2.1', 'Competence', 'PEOPLE', 'Persons shall be competent...', 'IMPLEMENTED', 90, 'AICOMPETENCE table per Prompt A.1.3', 'HR', NOW(), NOW() + INTERVAL '6 months', 'Gap analysis + training plan operational'),

('A.2.2', 'Awareness', 'PEOPLE', 'Persons shall be aware of AI policy...', 'IMPLEMENTED', 95, 'AI Awareness Program doc', 'HR', NOW(), NOW() + INTERVAL '12 months', '3.5h awareness program created'),

('A.2.3', 'Communication', 'PEOPLE', 'Internal and external communication...', 'PARTIAL', 75, 'Partial - need formal communication plan', 'Comms', NOW(), NOW() + INTERVAL '3 months', 'Need stakeholder communication matrix'),

-- A.3 Physical controls
('A.3.1', 'Physical security', 'PHYSICAL', 'Physical security of AI infrastructure...', 'IMPLEMENTED', 100, 'Kubernetes namespaces isolated, datacenter access controls', 'Ops', NOW(), NOW() + INTERVAL '12 months', 'Datacenter ISO 27001 certified'),

('A.3.2', 'Environmental controls', 'PHYSICAL', 'Environmental controls for AI infrastructure...', 'IMPLEMENTED', 95, 'Datacenter redundancy, HVAC, UPS', 'Ops', NOW(), NOW() + INTERVAL '12 months', 'Tier 3 datacenter standards'),

-- A.4 Technological controls
('A.4.1', 'Access control', 'TECHNOLOGICAL', 'Access control to AI systems...', 'IMPLEMENTED', 95, 'RBAC, Authentication, Authorization per user roles', 'Security', NOW(), NOW() + INTERVAL '6 months', 'MFA enabled'),

('A.4.2', 'Cryptographic controls', 'TECHNOLOGICAL', 'Cryptographic controls for AI data...', 'IMPLEMENTED', 100, 'TLS 1.3, encryption at rest, hash chains (Art. 19)', 'Security', NOW(), NOW() + INTERVAL '12 months', 'Logs immutables with SHA-256'),

('A.4.3', 'Logging and monitoring', 'TECHNOLOGICAL', 'Logging of AI system activities...', 'IMPLEMENTED', 100, 'IMLIMMUTABLELOGS + Prometheus + Grafana', 'Ops', NOW(), NOW() + INTERVAL '6 months', 'Art. 19 compliance'),

('A.4.4', 'Vulnerability management', 'TECHNOLOGICAL', 'Vulnerability management AI systems...', 'IMPLEMENTED', 90, 'leka-llm-evaluation (adversarial testing)', 'Security', NOW(), NOW() + INTERVAL '3 months', 'Automated adversarial testing'),

('A.4.5', 'Backup and recovery', 'TECHNOLOGICAL', 'Backup of AI systems and data...', 'IMPLEMENTED', 100, 'PostgreSQL WAL archiving, S3 offsite backup', 'Ops', NOW(), NOW() + INTERVAL '6 months', 'Daily backups, 10-year retention'),

('A.4.6', 'Network security', 'TECHNOLOGICAL', 'Network security for AI systems...', 'IMPLEMENTED', 95, 'Kubernetes network policies, firewalls, VPC isolation', 'Security', NOW(), NOW() + INTERVAL '6 months', 'Zero-trust networking'),

('A.4.7', 'Secure development', 'TECHNOLOGICAL', 'Secure development lifecycle AI...', 'PARTIAL', 80, 'Code reviews, SAST/DAST partial', 'Dev', NOW(), NOW() + INTERVAL '3 months', 'Need formal SDLC AI documentation'),

('A.4.8', 'Testing and validation', 'TECHNOLOGICAL', 'Testing and validation AI systems...', 'IMPLEMENTED', 95, 'leka-llm-evaluation, leka-rag-evaluation, test automation', 'QA', NOW(), NOW() + INTERVAL '3 months', 'Comprehensive testing'),

-- A.5 Data controls (7 controles)
('A.5.1', 'Data quality', 'DATA', 'Data quality for AI systems...', 'IMPLEMENTED', 95, 'leka-bias-detection (data quality checks), Art. 10', 'Data', NOW(), NOW() + INTERVAL '6 months', 'Automated quality checks'),

('A.5.2', 'Data governance', 'DATA', 'Data governance framework...', 'IMPLEMENTED', 90, 'Art. 10 data governance, GDPR compliance', 'Data', NOW(), NOW() + INTERVAL '6 months', 'Data lineage tracked'),

('A.5.3', 'Data privacy', 'DATA', 'Data privacy protection...', 'IMPLEMENTED', 95, 'leka-prompt-governance (PII Presidio), GDPR Art. 25', 'Privacy', NOW(), NOW() + INTERVAL '6 months', 'PII detection 99.5%'),

('A.5.4', 'Data security', 'DATA', 'Data security measures...', 'IMPLEMENTED', 95, 'Encryption, access control, DLP', 'Security', NOW(), NOW() + INTERVAL '6 months', 'ISO 27001 A.8.11, A.8.12'),

('A.5.5', 'Data labeling', 'DATA', 'Data labeling and annotation...', 'IMPLEMENTED', 90, 'Metadata tracking, provenance', 'Data', NOW(), NOW() + INTERVAL '6 months', 'Version control datasets'),

('A.5.6', 'Data retention', 'DATA', 'Data retention and disposal...', 'PARTIAL', 75, 'PostgreSQL retention, need formal policy', 'Data', NOW(), NOW() + INTERVAL '3 months', 'Need GDPR retention policy documented'),

('A.5.7', 'Data bias', 'DATA', 'Data bias detection and mitigation...', 'IMPLEMENTED', 98, 'leka-bias-detection (fairness metrics), Art. 10', 'Data', NOW(), NOW() + INTERVAL '3 months', 'Disparate impact, equal opportunity'),

-- A.6 Model controls (6 controles)
('A.6.1', 'Model development', 'MODEL', 'Model development process...', 'IMPLEMENTED', 90, 'leka-model-wrapper, training pipelines', 'ML Eng', NOW(), NOW() + INTERVAL '6 months', 'Reproducible training'),

('A.6.2', 'Model validation', 'MODEL', 'Model validation and testing...', 'IMPLEMENTED', 95, 'leka-llm-evaluation, performance metrics', 'ML Eng', NOW(), NOW() + INTERVAL '3 months', 'Automated validation'),

('A.6.3', 'Model documentation', 'MODEL', 'Model documentation...', 'IMPLEMENTED', 92, 'Art. 11 + Anexo IV, Anexo XI (GPAI)', 'ML Eng', NOW(), NOW() + INTERVAL '6 months', 'Technical docs auto-generated'),

('A.6.4', 'Model versioning', 'MODEL', 'Model versioning and tracking...', 'IMPLEMENTED', 100, 'Model registry, git-like versioning', 'ML Eng', NOW(), NOW() + INTERVAL '6 months', 'Full version control'),

('A.6.5', 'Model monitoring', 'MODEL', 'Model monitoring in production...', 'IMPLEMENTED', 95, 'Art. 72 post-market surveillance, drift detection', 'MLOps', NOW(), NOW() + INTERVAL '3 months', 'Real-time monitoring'),

('A.6.6', 'Model explainability', 'MODEL', 'Model explainability...', 'IMPLEMENTED', 100, 'leka-ai-interpreter, SHAP/LIME, Art. 13', 'ML Eng', NOW(), NOW() + INTERVAL '6 months', '100% explainability - DIFERENCIADOR'),

-- A.7 AI system controls (8 controles)
('A.7.1', 'AI system design', 'SYSTEM', 'AI system design process...', 'IMPLEMENTED', 90, 'Art. 9-15 requirements high-risk', 'Architect', NOW(), NOW() + INTERVAL '6 months', 'EU AI Act design requirements'),

('A.7.2', 'AI system deployment', 'SYSTEM', 'AI system deployment process...', 'IMPLEMENTED', 95, 'BPMN workflows, Art. 16-27', 'DevOps', NOW(), NOW() + INTERVAL '3 months', 'Automated deployment pipelines'),

('A.7.3', 'AI system monitoring', 'SYSTEM', 'AI system monitoring...', 'IMPLEMENTED', 98, 'Prometheus, Grafana, Art. 72', 'MLOps', NOW(), NOW() + INTERVAL '3 months', 'Real-time monitoring'),

('A.7.4', 'AI system maintenance', 'SYSTEM', 'AI system maintenance...', 'IMPLEMENTED', 90, 'Art. 20 corrective actions, updates', 'MLOps', NOW(), NOW() + INTERVAL '6 months', 'Automated updates'),

('A.7.5', 'AI system decommissioning', 'SYSTEM', 'AI system decommissioning...', 'PARTIAL', 70, 'Need formal decommissioning workflow', 'MLOps', NOW(), NOW() + INTERVAL '3 months', 'Need BPMN workflow'),

('A.7.6', 'Human oversight', 'SYSTEM', 'Human oversight mechanisms...', 'IMPLEMENTED', 98, 'Art. 14 HITL, stop button, agent supervision', 'Product', NOW(), NOW() + INTERVAL '6 months', 'HITL integrated'),

('A.7.7', 'Transparency', 'SYSTEM', 'Transparency to users...', 'IMPLEMENTED', 100, 'Art. 13, Art. 52, AI Interpreter', 'Product', NOW(), NOW() + INTERVAL '6 months', '100% transparency'),

('A.7.8', 'Incident management', 'SYSTEM', 'AI incident management...', 'IMPLEMENTED', 95, 'Art. 73 incident reporting, BPMN workflows', 'Ops', NOW(), NOW() + INTERVAL '3 months', 'Automated incident detection');

PANTALLA ZUL:

/console/zul/governance/iso42001_annex_a_controls.zul

Funcionalidades:
- Tabla 39 controles con status/coverage
- Filtros por categoría, status, coverage %
- Form evidencias por control
- Upload documentos evidencia
- Export compliance matrix Excel/PDF
- Dashboard compliance by category (chart)
- Trend compliance over time

REPORTE ISO 42001 ANNEX A:

Formato: Excel/PDF
Secciones:
1. Executive Summary (overall compliance %)
2. Compliance by Category (7 categorías)
3. Control Details (39 controles con evidencias)
4. Gaps Analysis (controles <100%)
5. Action Plan (controles <100% con plan cierre)

Referencias ISO 42001:
- Annex A: 39 controls
- Clause 6.1.3: AI-specific controls
```

**Esfuerzo:** 2 días (seed data + UI)  
**Prioridad:** 🔴 MUY CRÍTICA

---

### **PROMPT A.1.8 - ISO 42001 Certification Readiness Assessment**

**Objetivo:** Assessment automático readiness certificación ISO 42001.

**PROMPT:**

```
Necesito CREAR herramienta "ISO 42001 Certification Readiness Assessment".

OBJETIVO:
Auto-evaluación preparación para auditoría de certificación ISO 42001.

NUEVA ENTIDAD: ISO42001READINESS

Tabla: IRDISO42001READINESS
Prefijo: IRD
Package: com.codeflowx.govern.entity.governance

Campos:
- idxirdreadiness (PK)
- irdassessmentdate (TIMESTAMP)
- irdassessedby (VARCHAR 100)
- irdoverallscore (DECIMAL) - Score 0-100
- irdclause4score (DECIMAL) - Context of organization
- irdclause5score (DECIMAL) - Leadership
- irdclause6score (DECIMAL) - Planning
- irdclause7score (DECIMAL) - Support
- irdclause8score (DECIMAL) - Operation
- irdclause9score (DECIMAL) - Performance evaluation
- irdclause10score (DECIMAL) - Improvement
- irdannexascore (DECIMAL) - Annex A controls
- irdgapsidentified (TEXT) - JSON gaps
- irdactionplan (TEXT) - JSON action plan
- irdestimatedreadiness (VARCHAR 20) - NOT_READY, PARTIAL, READY, AUDIT_READY
- irdnextassessment (TIMESTAMP)

BUSINESSSERVICE:

Class: ISO42001ReadinessBusinessService

Métodos:
1. performReadinessAssessment() - Auto-assessment full
2. calculateClauseScore(clauseNumber) - Score por cláusula
3. calculateAnnexAScore() - Score Anexo A (from controls table)
4. identifyGaps() - Gaps automático
5. generateActionPlan() - Plan acción cierre gaps
6. generateReadinessReport() - Reporte certification readiness
7. estimateCertificationDate() - Fecha estimada certification ready

ASSESSMENT LOGIC:

Clause 4 (Context):
- AI Policy exists? (A.1.1)
- AI Objectives defined? (A.1.2)
- Stakeholders identified?
- Score: (items compliant / total items) * 100

Clause 5 (Leadership):
- Top management commitment?
- Roles and responsibilities defined? (A.1.3)
- Policy communicated?

Clause 6 (Planning):
- Risks identified? (A.1.4)
- Objectives established? (A.1.2)
- Legal requirements determined? (A.1.5)

Clause 7 (Support):
- Competence assessed? (A.2.1)
- Awareness program? (A.2.2)
- Documentation controlled?

Clause 8 (Operation):
- AI system inventory? (Prompt A.1.4)
- Processes implemented?
- Controls applied? (Annex A)

Clause 9 (Performance):
- Monitoring active? (Prompt A.1.5)
- Internal audits scheduled?
- Management reviews conducted?

Clause 10 (Improvement):
- Non-conformities tracked? (Prompt A.1.6)
- Corrective actions implemented?
- Improvements recorded?

Annex A:
- Controls coverage from ICOISO42001CONTROL table
- Score: AVG(coverage percentage 39 controls)

OVERALL SCORE:
- Weighted average:
  - Clauses 4-10: 70% weight
  - Annex A: 30% weight

CERTIFICATION READINESS THRESHOLDS:

- <70%: NOT_READY (6+ months to certification)
- 70-85%: PARTIAL (3-6 months to certification)
- 85-95%: READY (1-3 months to certification)
- >95%: AUDIT_READY (schedule certification audit now)

PANTALLA ZUL:

/console/zul/governance/iso42001_readiness.zul

Secciones:
1. Dashboard Readiness
   - Overall score gauge (0-100)
   - Score by clause (radar chart)
   - Annex A compliance (bar chart)
   - Readiness level badge

2. Gaps Analysis
   - Lista gaps por cláusula
   - Severity: CRITICAL, MAJOR, MINOR
   - Estimated effort to close

3. Action Plan
   - Roadmap cierre gaps
   - Milestones
   - Responsible persons
   - Target certification date

4. Assessment History
   - Trend readiness over time (line chart)
   - Improvements between assessments

BUTTON: "Perform Assessment"
- Runs full auto-assessment
- Generates report
- Updates dashboard

REPORTE CERTIFICATION READINESS:

Formato: PDF (professional)
Secciones:
1. Executive Summary
   - Overall readiness: XX%
   - Estimated certification date: [date]
   - Critical gaps: X

2. Clause-by-Clause Assessment
   - Clause 4: XX% - [status]
   - Clause 5: XX% - [status]
   - ...

3. Annex A Controls Assessment
   - Overall: XX%
   - By category breakdown

4. Gaps and Action Plan
   - Gap 1: [description] - [effort] - [responsible] - [due date]
   - Gap 2: ...

5. Recommendations
   - Prioritized actions
   - External resources needed (consultants, training)

Referencias ISO 42001:
- All clauses 4-10
- Annex A
```

**Esfuerzo:** 2 días  
**Prioridad:** 🔴 ALTA

---

## **RESUMEN SECCIÓN A.1 (ISO 42001):**

**8 prompts, 18 días esfuerzo total**

**Resultado:** ISO 42001 de 92% → **100%** ✅

**Entidades nuevas:** 8  
**Pantallas ZUL:** 6  
**Workflows BPMN:** 2  
**Documentos:** 3

---

## 🎯 SECCIÓN A.2 - ISO/IEC 38507:2022 GAPS (10 prompts)

### **Estado Actual:** 75-80% → **Target:** 98%

---

### **PROMPT A.2.1 - Board-Level AI Governance Dashboard**

**Objetivo:** Dashboard ejecutivo órganos gobierno (board) según ISO 38507.

**GAP ACTUAL:**
- Dashboards actuales son técnicos/operativos
- ISO 38507 requiere reporting board-level (EDM model)

**PROMPT:**

```
Necesito CREAR "Board-Level AI Governance Dashboard" según ISO 38507:2022.

ISO 38507 FRAMEWORK:
Modelo EDM (Evaluate, Direct, Monitor)
- Evaluate: Board evalúa propuestas uso IA
- Direct: Board establece políticas y estrategia
- Monitor: Board supervisa conformidad y performance

NUEVA ENTIDAD: BOARDREPORT

Tabla: BRDBOARDREPORT
Prefijo: BRD
Package: com.codeflowx.govern.entity.governance

Campos:
- idxbrdreport (PK)
- brdperiod (VARCHAR 20) - MONTHLY, QUARTERLY, ANNUAL
- brdperiodstart (TIMESTAMP)
- brdperiodend (TIMESTAMP)
- brdreporttype (VARCHAR 50) - EVALUATE, DIRECT, MONITOR
- brdexecutivesummary (TEXT)
- brdkeymetrics (TEXT) - JSON KPIs board-level
- brdrisksummary (TEXT) - Top AI risks
- brdopportunities (TEXT) - Oportunidades IA
- brddecisionsrequired (TEXT) - JSON decisiones pendientes board
- brdactionstaken (TEXT) - Acciones board periodo anterior
- brdcompliancestatus (VARCHAR 50) - COMPLIANT, MINOR_ISSUES, MAJOR_ISSUES, NON_COMPLIANT
- brdgenerateddate (TIMESTAMP)
- brdgeneratedby (VARCHAR 100)
- brdpresentedtoboard (BOOLEAN)
- brdpresentationdate (TIMESTAMP)
- brdboarddecisions (TEXT) - Decisiones tomadas por board

BUSINESSSERVICE:

Class: BoardGovernanceBusinessService

Métodos:
1. generateBoardReport(period, type) - Generar reporte board
2. getEvaluateMetrics() - Métricas Evaluate (propuestas IA)
3. getDirectMetrics() - Métricas Direct (políticas activas)
4. getMonitorMetrics() - Métricas Monitor (compliance, performance)
5. recordBoardDecision(reportId, decision) - Registrar decisión board
6. exportBoardReportPDF(reportId) - Export formato board (executive)

BOARD KPIs (High-Level):

EVALUATE (Evaluar propuestas):
1. AI Proposals Submitted: COUNT(new AI projects)
2. AI Proposals Approved: COUNT(approved) / COUNT(submitted)
3. AI Investment Total: SUM(budget AI projects)
4. AI ROI Expected: AVG(expected ROI proposals)

DIRECT (Dirigir):
1. AI Policies Active: COUNT(policies)
2. AI Objectives Defined: COUNT(objectives)
3. AI Strategy Alignment Score: (objectives aligned / total) * 100
4. AI Risk Appetite: [defined by board]

MONITOR (Supervisar):
1. AI Systems in Production: COUNT(deployed systems)
2. AI Compliance Score: AVG(compliance all systems)
3. AI Incidents Reported: COUNT(incidents period)
4. AI Performance vs Objectives: (objectives met / total) * 100
5. AI Ethics Issues: COUNT(ethics concerns raised)

PANTALLA ZUL:

/console/zul/governance/board_ai_dashboard.zul

Layout: Executive-friendly (charts, gauges, minimal text)

Secciones:
1. Executive Summary
   - Overall AI health score (0-100)
   - Key highlights (3-5 bullets)
   - Critical issues requiring board attention

2. Evaluate View
   - AI proposals pipeline (funnel chart)
   - Investment vs ROI (scatter plot)
   - Strategic alignment score (gauge)

3. Direct View
   - Active policies (count + list)
   - Objectives tracking (progress bars)
   - Risk appetite vs actual risk (comparison)

4. Monitor View
   - Systems in production (count + list critical)
   - Compliance status (traffic light)
   - Incidents trend (line chart)
   - Performance vs objectives (bar chart)

5. Decisions Required
   - Lista decisiones pendientes board approval
   - Priority: HIGH, MEDIUM, LOW
   - Deadline

EXPORT PDF:

Formato: Executive presentation (PowerPoint-style)
- 1 slide: Executive Summary
- 1 slide: Evaluate metrics
- 1 slide: Direct metrics
- 1 slide: Monitor metrics
- 1 slide: Decisions required

Diseño: Clean, professional, charts > tables

Referencias ISO 38507:
- Clause 5: Governance framework (EDM)
- Clause 6: Governance system components
```

**Esfuerzo:** 2 días  
**Prioridad:** 🔴 CRÍTICA (enterprise/public sector)

---

*[Continúa con 9 prompts más ISO 38507...]*

**POR ESPACIO, RESUMO RESTO DE PROMPTS:**

**A.2.2** - Ethics Committee Workflow (ISO 38507 ethics governance)  
**A.2.3** - Stakeholder Management Module (ISO 38507 stakeholder engagement)  
**A.2.4** - AI Strategic Planning Module (ISO 38507 strategy alignment)  
**A.2.5** - Board Decision Tracking (ISO 38507 decision audit trail)  
**A.2.6** - External Stakeholder Communication (ISO 38507 transparency)  
**A.2.7** - AI Investment Portfolio Management (ISO 38507 resource allocation)  
**A.2.8** - Third-Party AI Risk Assessment (ISO 38507 supply chain)  
**A.2.9** - AI Sustainability Metrics (ISO 38507 + OECD sustainability)  
**A.2.10** - Board Education Program on AI (ISO 38507 competence)

---

## 🎯 SECCIÓN A.3 - OECD + GDPR GAPS (10 prompts)

**A.3.1** - OECD Sustainability Metrics (CO2, energy efficiency)  
**A.3.2** - OECD Multi-Stakeholder Reporting  
**A.3.3** - GDPR Privacy Notice Templates Auto-Generate  
**A.3.4** - GDPR Data Portability API  
**A.3.5** - GDPR Right to Object Workflow  
**A.3.6** - GDPR Records of Processing Activities (ROPA) Auto-Generate  
**A.3.7** - OECD Trustworthy AI Scorecard  
**A.3.8** - OECD Incident Transparency Reporting  
**A.3.9** - GDPR Data Protection by Design Checklist  
**A.3.10** - OECD Human Rights Impact Assessment

---

# GRUPO B: SECURITY/PRIVACY (10 prompts)

## 🎯 SECCIÓN B.1 - ISO 27001:2022 GAPS (6 prompts)

**B.1.1** - ISMS Documentation Formal (ISO 27001 Clause 4-10)  
**B.1.2** - Security Awareness Training Program (ISO 27001 A.6.3)  
**B.1.3** - Test Environment Protection (ISO 27001 A.8.31)  
**B.1.4** - Supplier Security Assessment (ISO 27001 A.5.22)  
**B.1.5** - Business Continuity AI Systems (ISO 27001 A.5.30)  
**B.1.6** - Security Incident Response Automation (ISO 27001 A.5.26)

---

## 🎯 SECCIÓN B.2 - ISO 27701:2019 GAPS (4 prompts)

**B.2.1** - PIMS Documentation (ISO 27701 Clause 5)  
**B.2.2** - Privacy by Default Settings (ISO 27701 6.7.1.3)  
**B.2.3** - Data Subject Rights Portal (ISO 27701 7.3)  
**B.2.4** - Privacy Impact Assessment Automation (ISO 27701 6.7.2.7)

---

# GRUPO C: OPCIONAL UK (9 prompts)

## 🎯 SECCIÓN C.1 - ICO UK GUIDANCE (Solo si venta UK)

**C.1.1** - ICO UK Accountability Framework Templates  
**C.1.2** - ICO UK GDPR Specific Guidance Implementation  
**C.1.3** - ICO UK AI Auditing Framework  
**C.1.4** - UK DPIA Templates (vs EU templates actuales)  
**C.1.5** - ICO UK Lawful Basis Assessment Tool  
**C.1.6** - ICO UK Data Sharing Agreements Templates  
**C.1.7** - ICO UK Children's Code Compliance (si aplicable)  
**C.1.8** - ICO UK Automated Decision-Making Register  
**C.1.9** - ICO UK International Transfers Assessment (post-Brexit)

---

## 📊 RESUMEN TOTAL PROMPTS

| Grupo | Framework | Prompts | Esfuerzo | Prioridad |
|-------|-----------|---------|----------|-----------|
| **A.1** | ISO 42001 | 8 | 18 días | 🔴 CRÍTICA |
| **A.2** | ISO 38507 | 10 | 20 días | 🔴 CRÍTICA |
| **A.3** | OECD + GDPR | 10 | 15 días | 🔴 ALTA |
| **B.1** | ISO 27001 | 6 | 12 días | 🟡 MEDIA |
| **B.2** | ISO 27701 | 4 | 8 días | 🟡 MEDIA |
| **C.1** | ICO UK | 9 | 10 días | 🟢 OPCIONAL |
| **TOTAL** | **TODOS** | **47** | **83 días** | - |

---

## 🚀 ROADMAP EJECUCIÓN AGRESIVO

### **FASE 1 (Semanas 1-3): CRÍTICO EU - 100% Certificable**

**ISO 42001 (8 prompts)** - 3 semanas paralelo
- Equipo A: Prompts A.1.1-A.1.4 (9 días)
- Equipo B: Prompts A.1.5-A.1.8 (9 días)

**Resultado:** ISO 42001 **100%** ✅ - Auditable

---

### **FASE 2 (Semanas 4-6): GOVERNANCE BOARD-LEVEL**

**ISO 38507 (10 prompts)** - 3 semanas paralelo
- Equipo A: Prompts A.2.1-A.2.5 (10 días)
- Equipo B: Prompts A.2.6-A.2.10 (10 días)

**Resultado:** ISO 38507 **98%** ✅ - Enterprise/Public ready

---

### **FASE 3 (Semanas 7-9): OECD + GDPR Perfection**

**OECD + GDPR (10 prompts)** - 2 semanas paralelo
- Equipo A: OECD (5 prompts) - 7 días
- Equipo B: GDPR (5 prompts) - 8 días

**Resultado:** OECD **100%**, GDPR **98%** ✅

---

### **FASE 4 (Semanas 10-12): SECURITY/PRIVACY Hardening**

**ISO 27001 + 27701 (10 prompts)** - 3 semanas paralelo
- Equipo A: ISO 27001 (6 prompts) - 12 días
- Equipo B: ISO 27701 (4 prompts) - 8 días

**Resultado:** ISO 27001 **95%**, ISO 27701 **95%** ✅

---

### **FASE 5 (OPCIONAL - Semanas 13-14): UK Market**

**ICO UK (9 prompts)** - 2 semanas (solo si venta UK)

**Resultado:** ICO UK **98%** ✅

---

## ✅ RESULTADO FINAL

**Timeline:** 12-14 semanas (3-3.5 meses) desarrollo agresivo  
**Teams:** 2 equipos paralelos  
**Output:** **100% Multi-Framework Compliance**

| Framework | Antes | Después | Certificable |
|-----------|-------|---------|--------------|
| EU AI Act | 95% | **100%** ✅ | Sí |
| ISO 42001 | 92% | **100%** ✅ | Sí |
| ISO 38507 | 75% | **98%** ✅ | Sí |
| OECD Principles | 90% | **100%** ✅ | N/A (no certificable) |
| OECD Accountability | 95% | **100%** ✅ | N/A |
| GDPR | 90% | **98%** ✅ | Sí (via ISO 27701) |
| ISO 27001 | 85% | **95%** ✅ | Sí |
| ISO 27701 | 88% | **95%** ✅ | Sí |
| ICO UK | 0% | **98%** ✅ | N/A (guidance) |

**PROMEDIO MULTI-FRAMEWORK:** **98%** ✅

---

## 🎯 PRÓXIMOS PASOS INMEDIATOS

1. **AHORA:** Revisar este documento prompts
2. **HOY:** Priorizar grupos (A > B > C?)
3. **MAÑANA (post-lanzamiento):** Kickoff Fase 1 (ISO 42001)
4. **12 semanas:** CodeflowX = Best-in-class multi-framework compliance

---

**¿Empezamos con Grupo A (ISO 42001 + ISO 38507) inmediatamente post-lanzamiento?** 🚀

