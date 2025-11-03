# PROMPTS - BPMN WORKFLOWS EU AI ACT
## Modificación de BPMNs Existentes y Creación de Nuevos Procesos

**Equipo:** BPMN + Delegates Team  
**Fecha:** 15 de noviembre de 2025  
**Objetivo:** Extender BPMNs existentes y crear nuevos workflows para EU AI Act compliance  
**Esfuerzo Estimado:** 8-10 días (con 2-3 chats en paralelo)

---

## 🏗️ ARQUITECTURA BPMN ACTUAL

**Procesos BPMN Existentes:** 17 procesos
```
APROBACIÓN (3):
✅ agent-approval-v1.bpmn
✅ model-approval-v1.bpmn
✅ prompt-approval-v1.bpmn

DETECCIÓN (4):
✅ bias-detection-v1.bpmn
✅ drift-detection-v1.bpmn
✅ performance-degradation-v1.bpmn
✅ alert-response-v1.bpmn

EVALUACIÓN (3):
✅ llm-evaluation-v1.bpmn
✅ rag-evaluation-v1.bpmn
✅ model-evaluation-v1.bpmn

AUTOMATIZACIÓN (2):
✅ deployment-automation-v1.bpmn
✅ incident-response-rca-v1.bpmn

GOVERNANCE (5):
✅ compliance-monitoring-v1.bpmn
✅ ethics-review-v1.bpmn
✅ risk-assessment-v1.bpmn
✅ + 2 más
```

**Componentes BPMN:**
- **Service Tasks:** Java Delegates (65 existentes)
- **User Tasks:** ViewModels ZUL (25 existentes)
- **Gateways:** Exclusive, Parallel, Inclusive
- **Timers:** Start events, intermediate events

---

## 📋 GRUPOS DE PROMPTS

### **GRUPO A: MODIFICAR BPMNs EXISTENTES (3-4 días)**

---

### **PROMPT A.1 - Extender compliance-monitoring-v1.bpmn**

**Contexto:**
BPMN existente que ejecuta cada 24h para monitoreo compliance.

**Nuevas Actividades EU AI Act:**

**Art. 19 - Verificación logs inmutables:**
1. Service Task: `verifyLogIntegrity` - Verificar hash chain logs
2. Gateway: `logIntegrityGateway` - Si hay tampering → alerta crítica

**Art. 15.4/15.5 - Evaluaciones adversariales programadas:**
3. Service Task: `runAdversarialEvaluation` - Ejecutar tests adversariales
4. Service Task: `runFeedbackLoopAnalysis` - Detectar feedback loop bias

**Art. 72 - Post-market monitoring:**
5. Service Task: `checkPostMarketMetrics` - Verificar métricas producción

**Prompt Específico:**

```
Necesito EXTENDER el proceso BPMN compliance-monitoring-v1.bpmn existente con nuevas actividades de verificación según EU AI Act Art. 19, 15, y 72.

PROCESO ACTUAL:
- Archivo: compliance-monitoring-v1.bpmn (ubicar en proyecto)
- Frecuencia: Timer cada 24h
- Service Tasks actuales: executeComplianceCheck, createAlerts, etc.
- User Tasks actuales: reviewIssues, createIncident, resolveIncident

NUEVAS ACTIVIDADES A AÑADIR:

1. Service Task: verifyLogIntegrity
   - ID: verifyLogIntegrity
   - Nombre: Verificar Integridad Logs Inmutables
   - Delegate: VerifyLogIntegrityDelegate.java (CREAR)
   - Input: projectId (desde process variables)
   - Output:
     - logIntegrityValid (Boolean)
     - tamperingDetected (Boolean)
     - corruptedLogsCount (Integer)
   - Ubicación en flujo: Después de executeComplianceCheck, en paralelo

2. Exclusive Gateway: logIntegrityGateway
   - ID: logIntegrityGateway
   - Condition 1: ${tamperingDetected == true} → createCriticalAlert
   - Condition 2: ${tamperingDetected == false} → continuar flujo normal
   
3. Service Task: createCriticalAlert (si tampering)
   - ID: createCriticalTamperingAlert
   - Nombre: Crear Alerta Crítica Tampering
   - Delegate: CreateTamperingAlertDelegate.java (CREAR)
   
4. Service Task: runAdversarialEvaluation
   - ID: runAdversarialEvaluation
   - Nombre: Ejecutar Evaluaciones Adversariales
   - Delegate: RunAdversarialEvaluationDelegate.java (CREAR)
   - Input: modelId
   - Output:
     - adversarialRobustnessScore (BigDecimal)
     - vulnerabilitiesFound (Integer)
   - Ubicación: Paralelo a verifyLogIntegrity

5. Service Task: runFeedbackLoopAnalysis
   - ID: runFeedbackLoopAnalysis
   - Nombre: Analizar Feedback Loop Bias
   - Delegate: RunFeedbackLoopAnalysisDelegate.java (CREAR)
   - Input: modelId, timePeriod (últimos 30 días)
   - Output:
     - feedbackLoopDetected (Boolean)
     - biasAmplificationFactor (BigDecimal)

6. Service Task: checkPostMarketMetrics
   - ID: checkPostMarketMetrics
   - Nombre: Verificar Métricas Post-Market
   - Delegate: CheckPostMarketMetricsDelegate.java (CREAR)
   - Input: projectId
   - Output:
     - driftDetected (Boolean)
     - performanceDegradation (Boolean)
     - userSatisfactionDrop (Boolean)

FLUJO MODIFICADO:

[Start Timer (24h)]
    ↓
[executeComplianceCheck] (existente)
    ↓
[Parallel Gateway] (NUEVO)
    ├─→ [verifyLogIntegrity] (NUEVO)
    │       ↓
    │   [logIntegrityGateway] (NUEVO)
    │       ├─ tampering → [createCriticalTamperingAlert] → [Join]
    │       └─ OK → [Join]
    │
    ├─→ [runAdversarialEvaluation] (NUEVO)
    │       ↓
    │   [Join]
    │
    ├─→ [runFeedbackLoopAnalysis] (NUEVO)
    │       ↓
    │   [Join]
    │
    └─→ [checkPostMarketMetrics] (NUEVO)
            ↓
        [Join]
    ↓
[Join Gateway]
    ↓
[nonComplianceGateway] (existente)
    ↓
... resto flujo existente ...

DELEGATES A CREAR:

1. VerifyLogIntegrityDelegate.java
2. CreateTamperingAlertDelegate.java
3. RunAdversarialEvaluationDelegate.java
4. RunFeedbackLoopAnalysisDelegate.java
5. CheckPostMarketMetricsDelegate.java

Cada delegate:
- Package: com.codeflowx.govern.workflow.delegates.compliance
- Implements: JavaDelegate (Camunda)
- Llama servicios Java correspondientes
- Llama micros Python si necesario
- Guarda resultados en process variables

INSTRUCCIONES:
1. Abrir BPMN existente con Camunda Modeler
2. Añadir nuevas actividades en paralelo (no romper flujo actual)
3. Configurar delegates
4. Crear clases delegate Java
5. Actualizar process variables document
6. NO romper User Tasks existentes
7. Mantener backward compatibility
```

**Artículos Cubiertos:** Art. 19, Art. 15.4, Art. 15.5, Art. 72  
**Esfuerzo:** 2-3 días  
**Prioridad:** 🔴 Crítica

---

### **GRUPO B: NUEVOS BPMNs (5-6 días)**

---

### **PROMPT B.1 - NUEVO BPMN: conformity-assessment-process.bpmn**

**Objetivo:** Proceso de evaluación de conformidad según Art. 43 y Anexo VI (4 pasos).

**Prompt Específico:**

```
Necesito CREAR proceso BPMN nuevo conformity-assessment-process.bpmn para evaluación de conformidad según Anexo VI del EU AI Act.

ESPECIFICACIONES:
- Archivo: conformity-assessment-process.bpmn20.xml
- ID Proceso: conformity_assessment_process
- Nombre: Conformity Assessment Process (Annex VI)
- Categoría: Compliance

FLUJO PROCESO (Anexo VI - 4 pasos):

[Start Event: Manual]
    ↓
[User Task: Initiate Assessment]
    - Formulario: Seleccionar proyecto, tipo assessment (self/notified_body)
    - Roles: compliance-officers
    - ViewModel: InitiateConformityAssessmentViewModel.java + initiate_conformity_assessment.zul
    - Output: projectId, assessmentType
    ↓
[Service Task: Step 2 - Verify QMS Compliance (Art. 17)]
    - Delegate: VerifyQmsComplianceDelegate.java
    - Llama: QualityManagementSystemService.calculateQmsComplianceScore(projectId)
    - Output:
      - qmsCompliant (Boolean)
      - qmsScore (BigDecimal)
      - qmsGaps (List<String>)
    ↓
[Exclusive Gateway: QMS Compliant?]
    ├─ NO → [User Task: Review QMS Gaps]
    │           - Mostrar gaps detectados
    │           - Opciones: Fix and retry, Cancel assessment
    │           └─ → [End: Assessment Failed]
    │
    └─ YES → continuar
    ↓
[Service Task: Step 3 - Review Technical Documentation (Annex IV)]
    - Delegate: ReviewTechnicalDocumentationDelegate.java
    - Llama: TechnicalDocumentationService.validateAnnexIV(projectId)
    - Llama micro Python: leka-technical-documentation-generator/validate
    - Output:
      - docComplete (Boolean)
      - docScore (BigDecimal)
      - docGaps (List<String>)
    ↓
[Exclusive Gateway: Doc Complete?]
    ├─ NO (score < 0.90) → [User Task: Complete Documentation]
    │                         └─ Loop back o End
    └─ YES → continuar
    ↓
[Service Task: Step 4 - Verify Process Consistency]
    - Delegate: VerifyProcessConsistencyDelegate.java
    - Verifica: Design process coherente, Post-market monitoring plan OK
    - Output:
      - processConsistent (Boolean)
      - consistencyScore (BigDecimal)
    ↓
[Exclusive Gateway: All Steps Pass?]
    ├─ NO → [User Task: Final Review] → Decision (retry/cancel)
    └─ YES → continuar
    ↓
[Service Task: Generate Conformity Report]
    - Delegate: GenerateConformityReportDelegate.java
    - Genera: Reporte PDF conformidad
    - Output: reportUrl, overallScore
    ↓
[User Task: Approve Conformity Assessment]
    - Review final con score agregado
    - Roles: compliance-officers, management
    - ViewModel: ApproveConformityAssessmentViewModel.java
    - Decision: Approve / Reject
    ↓
[Exclusive Gateway: Approved?]
    ├─ YES → [Service Task: Mark as Conformity Assessed]
    │           - Update Project.conformityAssessed = true
    │           - Generate EU Declaration (trigger otro BPMN)
    │           └─ [End: Assessment Complete]
    │
    └─ NO → [Service Task: Document Rejection]
            └─ [End: Assessment Rejected]

DELEGATES A CREAR:
1. VerifyQmsComplianceDelegate.java
2. ReviewTechnicalDocumentationDelegate.java
3. VerifyProcessConsistencyDelegate.java
4. GenerateConformityReportDelegate.java
5. MarkAsConformityAssessedDelegate.java
6. DocumentRejectionDelegate.java

USER TASKS VIEWMODELS:
1. InitiateConformityAssessmentViewModel.java + .zul
2. ReviewQmsGapsViewModel.java + .zul
3. CompleteDocumentationViewModel.java + .zul
4. FinalReviewViewModel.java + .zul
5. ApproveConformityAssessmentViewModel.java + .zul

PROCESS VARIABLES:
- projectId (Long)
- assessmentType (String: SELF, NOTIFIED_BODY)
- qmsCompliant (Boolean)
- qmsScore (BigDecimal)
- qmsGaps (List<String>)
- docComplete (Boolean)
- docScore (BigDecimal)
- processConsistent (Boolean)
- overallScore (BigDecimal)
- approved (Boolean)

PATRÓN A SEGUIR:
- Revisar agent-approval-v1.bpmn como ejemplo
- Misma estructura delegates
- Misma nomenclatura
- Mismo manejo de variables
- Mismo error handling
```

**Artículos Cubiertos:** Art. 43, Anexo VI  
**Esfuerzo:** 3 días  
**Prioridad:** 🔴 Crítica

---

### **PROMPT B.2 - NUEVO BPMN: incident-reporting-process.bpmn**

**Objetivo:** Proceso notificación incidentes graves y malfuncionamientos según Art. 20, Art. 62, Art. 73.

**Prompt Específico:**

```
Necesito CREAR proceso BPMN incident-reporting-process.bpmn para gestión y notificación de incidentes según EU AI Act Art. 20, 62, 73.

ESPECIFICACIONES:
- Archivo: incident-reporting-process.bpmn20.xml
- ID: incident_reporting_process
- Nombre: Incident Reporting & Resolution Process
- Trigger: Manual (usuario reporta) o Automático (sistema detecta)

FLUJO PROCESO:

[Start Event: Incident Detected]
    - Puede ser:
      a) Signal from monitoring system
      b) Manual report by user
    ↓
[Service Task: Classify Incident Severity]
    - Delegate: ClassifyIncidentSeverityDelegate.java
    - Análisis automático:
      - Affected users count
      - Impact on fundamental rights
      - System criticality
      - Data breach involved
    - Output:
      - severity (LOW, MEDIUM, HIGH, CRITICAL)
      - isSerious (Boolean) - según Art. 73 definición
      - affectedUsersCount (Integer)
    ↓
[Exclusive Gateway: Serious Incident? (Art. 73)]
    ├─ YES (serious) → [Parallel Gateway]
    │                     ├─→ [Service Task: Notify Authority Immediately]
    │                     │     - Delegate: NotifyMarketSurveillanceAuthorityDelegate.java
    │                     │     - Plazo: Inmediato (Art. 73.1)
    │                     │     - Contenido: Descripción incidente, riesgos, medidas
    │                     │
    │                     ├─→ [Service Task: Notify Affected Users]
    │                     │     - Si GDPR breach también
    │                     │
    │                     └─→ [User Task: Document Incident Details]
    │                           └─→ [Join]
    │
    └─ NO (normal) → [User Task: Categorize and Document]
    ↓
[User Task: Root Cause Analysis]
    - Roles: tech-team, compliance-officers
    - ViewModel: RootCauseAnalysisViewModel.java
    - Formulario:
      - What happened?
      - Why happened (root cause)?
      - Contributing factors?
      - Timeline of events?
    ↓
[Service Task: Call RCA Microservice]
    - Delegate: ExecuteRCADelegate.java
    - Llama micro Python para análisis automático (logs, metrics)
    - Output: RCA report automated
    ↓
[User Task: Define Corrective Actions (Art. 20)]
    - Roles: tech-lead, compliance
    - ViewModel: DefineCorrectiveActionsViewModel.java
    - Formulario:
      - Immediate actions (containment)
      - Short-term actions (fix)
      - Long-term actions (prevent recurrence)
      - Responsible person per action
      - Due dates
    ↓
[Service Task: Create Corrective Action Tasks]
    - Delegate: CreateCorrectiveActionTasksDelegate.java
    - Crea tareas en sistema para cada acción
    - Asigna responsables
    ↓
[Parallel Gateway: Execute Actions]
    ├─→ [Multi-Instance: Execute Corrective Actions]
    │     - Por cada acción → sub-task
    │     - Collection: ${correctiveActions}
    │
    └─→ [Timer: Follow-up Reminder]
          - Reminder cada 48h hasta resolución
    ↓
[Join: All Actions Complete]
    ↓
[User Task: Verify Resolution]
    - Roles: compliance-officers
    - ViewModel: VerifyIncidentResolutionViewModel.java
    - Validar: Issue resolved, no recurrence
    ↓
[Exclusive Gateway: Resolved?]
    ├─ YES → [Service Task: Close Incident]
    │           - Update incident status: RESOLVED
    │           - Notify stakeholders
    │           - Update compliance dashboard
    │           └─→ [End: Incident Resolved]
    │
    └─ NO → [Service Task: Escalate]
            - Notify management
            - Create new corrective action cycle
            └─→ Loop back to Define Corrective Actions

DELEGATES A CREAR:
1. ClassifyIncidentSeverityDelegate.java
2. NotifyMarketSurveillanceAuthorityDelegate.java
3. NotifyAffectedUsersDelegate.java
4. ExecuteRCADelegate.java
5. CreateCorrectiveActionTasksDelegate.java
6. CloseIncidentDelegate.java
7. EscalateIncidentDelegate.java

USER TASKS + VIEWMODELS:
1. DocumentIncidentDetailsViewModel.java + .zul
2. RootCauseAnalysisViewModel.java + .zul
3. DefineCorrectiveActionsViewModel.java + .zul
4. VerifyIncidentResolutionViewModel.java + .zul

ENTIDAD JPA:
Usar o crear:
- IncidentReport.java (tabla INRINCIDENTREPORTS)
- CorrectiveAction.java (tabla CRAORRECTIVEACTIONS)

IMPORTANTE:
- Notificación autoridades < 24h para incidentes graves (Art. 73.1)
- Documentación completa incidente (Art. 20)
- Tracking corrective actions hasta resolución
```

**Artículos Cubiertos:** Art. 20, Art. 62, Art. 73  
**Esfuerzo:** 3 días  
**Prioridad:** 🔴 Crítica

---

### **PROMPT B.3 - NUEVO BPMN: fria-process.bpmn**

**Objetivo:** Proceso FRIA (Fundamental Rights Impact Assessment) para responsables del despliegue según Art. 27.

**Prompt Específico:**

```
Necesito CREAR proceso BPMN fria-process.bpmn para Evaluación de Impacto en Derechos Fundamentales según Art. 27 EU AI Act.

ESPECIFICACIONES:
- Archivo: fria-process.bpmn20.xml
- ID: fria_process
- Nombre: FRIA Process (Art. 27)
- Trigger: Manual (antes de despliegue sistema alto riesgo)

FLUJO PROCESO:

[Start Event: Initiate FRIA]
    ↓
[User Task: FRIA Wizard Step 1-6]
    - Usar FriaWizardViewModel.java (del prompt Java)
    - 6 pasos wizard (elementos Art. 27.1 a-f)
    - Output: friaDataComplete (Boolean)
    ↓
[Service Task: Generate FRIA Document]
    - Delegate: GenerateFriaDocumentDelegate.java
    - Llama micro Python: leka-fria-generator
    - Output:
      - friaDocumentUrl (String)
      - complianceScore (BigDecimal)
      - art27Compliant (Boolean)
    ↓
[Exclusive Gateway: FRIA Complete?]
    ├─ NO (score < 0.90) → [User Task: Complete Missing Elements]
    │                         └─→ Loop back
    └─ YES → continuar
    ↓
[Service Task: Analyze Fundamental Rights Impact]
    - Delegate: AnalyzeFundamentalRightsDelegate.java
    - Identifica artículos Charter UE afectados
    - Output:
      - charterArticlesAffected (List<String>)
      - impactSeverity (HIGH, MEDIUM, LOW)
    ↓
[Exclusive Gateway: High Impact?]
    ├─ YES → [User Task: Enhanced Review]
    │           - Roles: ethics-committee, legal
    │           - Review detallado impacto
    │           - Decision: Approve / Reject / Modify System
    │           ↓
    │       [Exclusive Gateway: Review Decision?]
    │           ├─ APPROVE → continuar
    │           ├─ REJECT → [End: Deployment Blocked]
    │           └─ MODIFY → [User Task: Define Modifications] → Loop
    │
    └─ NO/MEDIUM → continuar
    ↓
[User Task: Deployer Approval]
    - Roles: deployer, deployment-manager
    - Review FRIA final
    - Confirmar medidas mitigación
    - Decision: Proceed / Cancel
    ↓
[Exclusive Gateway: Proceed?]
    ├─ YES → [Parallel Gateway]
    │           ├─→ [Service Task: Notify Market Surveillance Authority]
    │           │     - Delegate: NotifyAuthorityFriaDelegate.java
    │           │     - Submit FRIA summary (Art. 27.3)
    │           │     - Output: notificationId, notificationDate
    │           │
    │           ├─→ [Service Task: Register FRIA in Database]
    │           │     - Save FriaAssessment entity
    │           │     - Link to Project
    │           │
    │           └─→ [Service Task: Update Project Status]
    │                 - Project.friaCompleted = true
    │                 - Project.readyForDeployment = true
    │           ↓
    │       [Join]
    │           ↓
    │       [End: FRIA Completed & Notified]
    │
    └─ NO → [Service Task: Cancel Deployment]
            └─→ [End: Deployment Cancelled]

DELEGATES A CREAR:
1. GenerateFriaDocumentDelegate.java
2. AnalyzeFundamentalRightsDelegate.java
3. NotifyAuthorityFriaDelegate.java
4. RegisterFriaDelegate.java
5. UpdateProjectFriaStatusDelegate.java

USER TASKS + VIEWMODELS:
1. InitiateFriaViewModel.java + .zul
2. CompleteMissingElementsViewModel.java + .zul
3. EnhancedReviewViewModel.java + .zul
4. DefineModificationsViewModel.java + .zul
5. DeployerApprovalViewModel.java + .zul

ENTIDAD JPA:
FriaAssessment.java (nueva)
- Tabla: FRIAFUNDAMENTALRIGHTSASSESSMENTS
- Prefijo: FRIA
- Campos:
  - IDXFRIAASSESSMENT (PK)
  - IDXPROJECT (FK)
  - FRIADOCUMENTURL (VARCHAR)
  - FRIACOMPLIANCESCORE (NUMERIC 3,2)
  - FRIART27COMPLIANT (BOOLEAN)
  - FRIACHARTER ARTICLESAFFECTED (JSONB)
  - FRIAIMPACTSEVERITY (VARCHAR)
  - FRIANOTIFICATIONID (VARCHAR)
  - FRIANOTIFICATIONDATE (TIMESTAMP)
  - FRIAAPPROVED (BOOLEAN)

IMPORTANTE:
- FRIA obligatoria para:
  - Organismos públicos (todos sistemas alto riesgo)
  - Entidades privadas servicios públicos (todos sistemas alto riesgo)
  - Todos deployers de Anexo III.5.b y III.5.c
- Notificación autoridad obligatoria (Art. 27.3)
- Integración con DPIA si aplica (Art. 27.4)
```

**Artículos Cubiertos:** Art. 27 (completo), integración Art. 26  
**Esfuerzo:** 3 días  
**Prioridad:** 🔴 Crítica

---

### **PROMPT B.4 - NUEVO BPMN: eu-database-registration-process.bpmn**

**Objetivo:** Proceso registro en Base de Datos UE según Art. 49 (3 secciones).

**Prompt Específico:**

```
Necesito CREAR proceso BPMN eu-database-registration-process.bpmn para registro en Base de Datos UE según Art. 49 y Anexo VIII.

ESPECIFICACIONES:
- Archivo: eu-database-registration-process.bpmn20.xml
- ID: eu_database_registration_process
- Nombre: EU Database Registration (Art. 49)

FLUJO PROCESO:

[Start Event: Registration Required]
    - Triggered cuando:
      a) Project.isHighRisk == true y conformityAssessed == true
      b) Project evaluado como NOT high-risk (Art. 6.3)
    ↓
[Service Task: Determine Registration Type]
    - Delegate: DetermineRegistrationTypeDelegate.java
    - Logic:
      - Si isHighRisk && annexIIICategory → Section A (Art. 49.1)
      - Si NOT high-risk evaluation → Section B (Art. 49.2)
      - Si deployer public authority → Section C también (Art. 49.3)
    - Output:
      - registrationType (SECTION_A, SECTION_B, SECTION_C, MULTIPLE)
    ↓
[Exclusive Gateway: Registration Type?]
    ├─ SECTION_A → [User Task: Complete Section A Form (13 fields)]
    ├─ SECTION_B → [User Task: Complete Section B Form (9 fields)]
    ├─ SECTION_C → [User Task: Complete Section C Form (5 fields)]
    └─ MULTIPLE → [Parallel: Multiple Forms]
    ↓
[User Task: Fill Registration Form]
    - ViewModel: EuRegistrationFormViewModel.java
    - Formulario adaptado según section (A, B, o C)
    - Campos Anexo VIII verificados
    - Auto-population desde Project/Model data
    ↓
[Service Task: Validate Registration Data]
    - Delegate: ValidateRegistrationDataDelegate.java
    - Verifica: 100% campos obligatorios completos
    - Formato correcto
    - Output:
      - validationPassed (Boolean)
      - validationErrors (List<String>)
    ↓
[Exclusive Gateway: Valid?]
    ├─ NO → [User Task: Fix Validation Errors] → Loop back
    └─ YES → continuar
    ↓
[Service Task: Generate Registration Package]
    - Delegate: GenerateRegistrationPackageDelegate.java
    - Prepara:
      - Registration JSON (Anexo VIII format)
      - EU Declaration PDF
      - Technical Doc summary
      - Certificates (si hay)
    - Output: registrationPackageUrl
    ↓
[User Task: Review Registration Package]
    - Roles: compliance-officers, legal
    - Review final antes de submit
    - Decision: Submit / Modify / Cancel
    ↓
[Exclusive Gateway: Submit?]
    ├─ YES → [Service Task: Submit to EU Database API]
    │           - Delegate: SubmitToEuDatabaseDelegate.java
    │           - POST a API oficial UE
    │           - Output:
    │             - submissionSuccess (Boolean)
    │             - euRegistrationId (String)
    │             - submissionDate (Timestamp)
    │           ↓
    │       [Exclusive Gateway: Submission Success?]
    │           ├─ YES → [Service Task: Update Project]
    │           │           - Project.euRegistrationId = value
    │           │           - Project.euRegistrationStatus = REGISTERED
    │           │           └─→ [End: Registration Complete]
    │           │
    │           └─ NO → [Service Task: Log Error & Retry]
    │                   - Retry logic (max 3 attempts)
    │                   - Si falla 3 veces → Manual intervention
    │                   └─→ [User Task: Manual Resolution]
    │
    └─ NO → [End: Registration Cancelled]

CASOS ESPECIALES:

Sensitive Systems (Anexo III.1, III.6, III.7):
- Law enforcement, Migration, Biometrics
- Registro en sección SEGURA no pública
- Solo campos limitados (Art. 49.4)
- Acceso restringido

Infrastructure Critical (Anexo III.2):
- Registro NACIONAL (no EU database)
- Art. 49.5

DELEGATES A CREAR:
1. DetermineRegistrationTypeDelegate.java
2. ValidateRegistrationDataDelegate.java
3. GenerateRegistrationPackageDelegate.java
4. SubmitToEuDatabaseDelegate.java
5. UpdateProjectRegistrationDelegate.java
6. LogRegistrationErrorDelegate.java

USER TASKS + VIEWMODELS:
1. EuRegistrationFormViewModel.java (multi-form adaptive)
2. FixValidationErrorsViewModel.java
3. ReviewRegistrationPackageViewModel.java
4. ManualResolutionViewModel.java

IMPORTANTE:
- API UE pendiente (usar mock endpoint para testing)
- Preparar infrastructure para integración rápida
- Retry logic robusto
- Logging completo de submission attempts
- Error handling para timeouts, rate limits, etc.
```

**Artículos Cubiertos:** Art. 49, Anexo VIII (3 secciones)  
**Esfuerzo:** 2-3 días  
**Prioridad:** 🔴 Crítica

---

## 📊 RESUMEN PROMPTS - BPMN WORKFLOWS

| Prompt | BPMN | Tipo | Funcionalidad | Esfuerzo | Prioridad |
|--------|------|------|---------------|----------|-----------|
| **A.1** | compliance-monitoring-v1 | Modificar Existente | Añadir verificación logs, adversarial, metrics | 2-3 días | 🔴 Crítica |
| **B.1** | conformity-assessment-process | Crear Nuevo | Anexo VI - 4 pasos evaluación conformidad | 3 días | 🔴 Crítica |
| **B.2** | incident-reporting-process | Crear Nuevo | Art. 20, 62, 73 - Incidentes y acciones correctoras | 3 días | 🔴 Crítica |
| **B.3** | eu-database-registration-process | Crear Nuevo | Art. 49 - Registro BBDD UE (3 secciones) | 2-3 días | 🔴 Crítica |

**TOTAL ESFUERZO:** 10-12 días  
**CON 2 CHATS PARALELOS:** 5-6 días reales  
**CON 3 CHATS PARALELOS:** 3-4 días reales

---

## 🎯 DISTRIBUCIÓN TRABAJO PARALELO

### **CHAT BPMN-1 - Modificación + Incident:**
- Prompt A.1 (compliance-monitoring extensión)
- Prompt B.2 (incident-reporting-process NUEVO)
- **Esfuerzo:** 5-6 días

### **CHAT BPMN-2 - Assessment + Registration:**
- Prompt B.1 (conformity-assessment-process NUEVO)
- Prompt B.3 (eu-database-registration NUEVO)
- **Esfuerzo:** 5-6 días

**Timeline con 2 chats:** 5-6 días reales

---

## 📋 PATRÓN ESTÁNDAR BPMN

**Estructura obligatoria cada BPMN:**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
             xmlns:camunda="http://camunda.org/schema/1.0/bpmn">
    
    <process id="proceso_id" name="Proceso Nombre" isExecutable="true">
        
        <!-- Start Event -->
        <startEvent id="start" name="Start"/>
        
        <!-- Service Task con Delegate -->
        <serviceTask id="serviceTask1" name="Service Task Name"
                     camunda:delegateExpression="${delegateBean}"/>
        
        <!-- User Task con Form Key -->
        <userTask id="userTask1" name="User Task Name"
                  camunda:formKey="embedded:app:forms/form_name.html"
                  camunda:assignee="${assignee}"
                  camunda:candidateGroups="compliance-officers"/>
        
        <!-- Exclusive Gateway -->
        <exclusiveGateway id="gateway1" name="Decision?"/>
        
        <!-- Sequence Flows con condiciones -->
        <sequenceFlow id="flow1" sourceRef="gateway1" targetRef="task2">
            <conditionExpression>${condition == true}</conditionExpression>
        </sequenceFlow>
        
        <!-- End Event -->
        <endEvent id="end" name="End"/>
        
    </process>
</definitions>
```

**Delegates Pattern:**

```java
@Component("delegateBean")
public class MyDelegate implements JavaDelegate {
    
    @Autowired
    private SomeService someService;
    
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // Obtener variables
        Long projectId = (Long) execution.getVariable("projectId");
        
        // Ejecutar lógica
        ResultDTO result = someService.doSomething(projectId);
        
        // Guardar output
        execution.setVariable("resultData", result);
        execution.setVariable("success", true);
        
        // Logging
        log.info("Delegate executed for project: {}", projectId);
    }
}
```

---

## ⚠️ CONSIDERACIONES

### **Integración con Micros Python:**

```java
// En delegates, llamar micros Python
@Component
public class CallPythonMicroDelegate implements JavaDelegate {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${leka.micro.url:http://localhost:8002}")
    private String microUrl;
    
    @Override
    public void execute(DelegateExecution execution) {
        RequestDTO request = buildRequest(execution);
        
        ResponseEntity<ResponseDTO> response = restTemplate.postForEntity(
            microUrl + "/api/endpoint",
            request,
            ResponseDTO.class
        );
        
        execution.setVariable("microResult", response.getBody());
    }
}
```

### **Error Handling:**

```java
try {
    // Lógica delegate
} catch (Exception e) {
    log.error("Error in delegate: {}", e.getMessage());
    execution.setVariable("error", e.getMessage());
    execution.setVariable("success", false);
    // NO lanzar excepción - dejar que BPMN maneje con gateway
}
```

### **User Tasks Forms:**

```
Opciones:
1. Embedded forms (HTML)
2. External task (ZUL con ViewModel)
3. Form key apuntando a ZUL

Recomendado: External task con ZUL (consistencia con UI actual)
```

---

**Fin Documento 4 de 5**

