# PROMPTS 09 - BPMN WORKFLOWS MULTI-FRAMEWORK (Camunda)
## Workflows Compliance ISO 42001, ISO 38507, OECD, GDPR

**Fecha:** Noviembre 2025  
**Objetivo:** Workflows BPMN para procesos compliance multi-framework  
**Motor:** Camunda BPM  
**Total Prompts:** 12 workflows nuevos

**CRÍTICO:** Revisar contra workflows existentes (`workflow/`) antes de crear duplicados.

---

## 🔄 ARQUITECTURA BPMN RECORDATORIO

```
1. Workflow BPMN XML (Camunda Modeler)
2. Service Tasks → Java Delegates
3. User Tasks → Forms ZUL
4. Integration con BusinessServices
5. Deploy en Camunda Engine
```

**WORKFLOWS EXISTENTES (revisar):**
- `workflow/` (22 workflows base + 5 compliance)

---

## 📋 ÍNDICE WORKFLOWS

### **GRUPO B1: ISO 42001 Workflows (5)**
- B1.1: Management Review Process (Clause 9.3)
- B1.2: Internal Audit Scheduling (Clause 9.2)
- B1.3: Corrective Action Workflow (Clause 10.2)
- B1.4: AI System Decommissioning (Clause 8.1)
- B1.5: Competence Gap Closure (Clause 7.2)

### **GRUPO B2: ISO 38507 Workflows (4)**
- B2.1: Board Decision Approval (EDM - Direct)
- B2.2: Ethics Committee Review (Ethics governance)
- B2.3: Stakeholder Consultation (Engagement)
- B2.4: AI Investment Approval (Strategic planning)

### **GRUPO B3: GDPR Workflows (3)**
- B3.1: Data Subject Rights Request (GDPR Art. 15-22)
- B3.2: Data Breach Notification (GDPR Art. 33-34)
- B3.3: DPIA Workflow (GDPR Art. 35)

---

# GRUPO B1: ISO 42001 WORKFLOWS

---

## PROMPT B1.1 - Management Review Workflow

**REVISAR:** ¿Existe workflow review o audit similar?

**Workflow:** `workflow/iso42001_management_review.bpmn`

**Descripción:** Workflow management review ISO 42001 Clause 9.3 (trimestral).

**Pasos:**

```
1. [Timer Event] - Cada 3 meses (trimestral)
   ↓
2. [Service Task] - Prepare Performance Data
   - Delegate: PreparePerformanceDataDelegate
   - Input: Period (quarter)
   - Output: Performance metrics JSON
   ↓
3. [Service Task] - Generate Management Review Report
   - Delegate: GenerateManagementReviewReportDelegate
   - Input: Performance metrics
   - Output: PDF report
   ↓
4. [User Task] - Schedule Management Review Meeting
   - Assignee: ${reviewCoordinator}
   - Form: schedule_review_meeting.zul
   - Output: Meeting date, attendees
   ↓
5. [User Task] - Conduct Management Review
   - Assignee: ${topManagement}
   - Form: management_review_form.zul
   - Input: Performance report
   - Output: Decisions, action items
   ↓
6. [Service Task] - Record Management Decisions
   - Delegate: RecordManagementDecisionsDelegate
   - Input: Decisions JSON
   - Output: Saved to APSAIMSPERFORMANCE table
   ↓
7. [Parallel Gateway] - Split actions
   ↓
8a. [User Task] - Assign Action Items
    - Assignee: ${actionOwner}
    - Output: Action plan with deadlines
   ↓
8b. [Service Task] - Notify Stakeholders
    - Delegate: NotifyStakeholdersDelegate
    - Output: Email notifications sent
   ↓
9. [Join Gateway] - Wait all actions
   ↓
10. [Service Task] - Archive Review Record
    - Delegate: ArchiveReviewRecordDelegate
    - Output: Compliance archive updated
   ↓
11. [End Event] - Management Review Complete
```

**Java Delegates:**

```java
// PreparePerformanceDataDelegate.java
public class PreparePerformanceDataDelegate implements JavaDelegate {
    
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        AIMSPerformanceBusinessService service = new AIMSPerformanceBusinessService();
        
        // Get period from workflow variable
        String period = (String) execution.getVariable("period");
        
        // Calculate period dates
        Date periodStart = calculatePeriodStart(period);
        Date periodEnd = calculatePeriodEnd(period);
        
        // Calculate performance
        Map<String, Object> performance = service.calculatePeriodPerformance(periodStart, periodEnd);
        
        // Set variables for next step
        execution.setVariable("performanceData", performance);
        execution.setVariable("periodStart", periodStart);
        execution.setVariable("periodEnd", periodEnd);
    }
}

// GenerateManagementReviewReportDelegate.java
public class GenerateManagementReviewReportDelegate implements JavaDelegate {
    
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Map<String, Object> performanceData = 
            (Map<String, Object>) execution.getVariable("performanceData");
        
        // Generate PDF report
        byte[] pdfReport = generateManagementReviewPDF(performanceData);
        
        // Save to filesystem/S3
        String reportPath = saveReport(pdfReport, execution.getProcessInstanceId());
        
        execution.setVariable("reportPath", reportPath);
        execution.setVariable("reportURL", getPublicURL(reportPath));
    }
    
    private byte[] generateManagementReviewPDF(Map<String, Object> data) {
        // Use iText or similar to generate PDF
        // Sections: Executive Summary, KPIs, Non-Conformities, Actions Required
        return pdfBytes;
    }
}

// RecordManagementDecisionsDelegate.java
public class RecordManagementDecisionsDelegate implements JavaDelegate {
    
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String decisions = (String) execution.getVariable("managementDecisions");
        String actionItems = (String) execution.getVariable("actionItems");
        
        AIMSPerformanceBusinessService service = new AIMSPerformanceBusinessService();
        
        // Get or create performance record for period
        ApsAIMSPerformance perf = service.getByPeriod(
            (Date) execution.getVariable("periodStart"),
            (Date) execution.getVariable("periodEnd")
        );
        
        perf.setApsmanagementdecisions(decisions);
        perf.setApsmanagementreviewdate(new Timestamp(System.currentTimeMillis()));
        
        service.save(perf);
        
        // Create action items in separate table if needed
        createActionItems(actionItems);
    }
}
```

**User Task Forms (ZUL):**

`forms/management_review_form.zul`

```xml
<window title="Management Review - ISO 42001" border="normal" width="800px">
    
    <groupbox title="Performance Summary">
        <iframe src="@load(vm.reportURL)" width="100%" height="400px" />
    </groupbox>
    
    <separator height="20px" />
    
    <groupbox title="Management Decisions">
        <textbox rows="10" width="100%" value="@bind(vm.decisions)" 
                 placeholder="Document management decisions here..." />
    </groupbox>
    
    <separator height="20px" />
    
    <groupbox title="Action Items">
        <grid model="@bind(vm.actionItems)">
            <columns>
                <column label="Action" />
                <column label="Responsible" width="150px" />
                <column label="Deadline" width="120px" />
            </columns>
            <template name="model">
                <row>
                    <textbox value="@bind(each.action)" width="100%" />
                    <textbox value="@bind(each.responsible)" />
                    <datebox value="@bind(each.deadline)" />
                </row>
            </template>
        </grid>
        <button label="Add Action" onClick="@command('addAction')" />
    </groupbox>
    
    <separator height="20px" />
    
    <hlayout>
        <button label="Complete Review" onClick="@command('completeReview')" />
        <button label="Cancel" onClick="@command('cancel')" />
    </hlayout>
    
</window>
```

**Esfuerzo:** 2 días  
**Prioridad:** 🔴 CRÍTICA  
**Referencias:** ISO 42001 Clause 9.3

---

## PROMPT B1.2 - Internal Audit Scheduling

**Workflow:** `workflow/iso42001_internal_audit.bpmn`

**Descripción:** Planificación y ejecución auditorías internas ISO 42001 Clause 9.2.

**Pasos:**

```
1. [Timer Event] - Anual o bajo demanda
2. [Service Task] - Generate Audit Checklist (from ISO 42001 clauses)
3. [User Task] - Schedule Audit (auditor, dates, scope)
4. [User Task] - Conduct Audit (checklist compliance)
5. [Service Task] - Calculate Compliance Score
6. [Decision Gateway] - Non-conformities found?
   - YES → Create Non-Conformities (link to B1.3)
   - NO → Archive audit
7. [Service Task] - Record Audit Results
8. [End Event]
```

**Delegates:**
- `GenerateAuditChecklistDelegate` - Checklist ISO 42001 clauses 4-10 + Annex A
- `CalculateComplianceScoreDelegate` - Score 0-100%
- `RecordAuditResultsDelegate` - Save to APSAIMSPERFORMANCE

**Forms:**
- `forms/audit_scheduling_form.zul`
- `forms/audit_execution_form.zul` (checklist interactivo)

**Esfuerzo:** 2 días  
**Prioridad:** 🔴 CRÍTICA

---

## PROMPT B1.3 - Corrective Action Workflow

**REVISAR:** ¿Existe workflow `corrective_action.bpmn`?

**Workflow:** `workflow/iso42001_corrective_action.bpmn`

**Descripción:** Gestión no conformidades y acciones correctivas ISO 42001 Clause 10.2.

**Pasos:**

```
1. [Start Event] - Non-Conformity Reported (manual or from audit)
2. [User Task] - Assess Non-Conformity
   - Severity: CRITICAL, MAJOR, MINOR
   - ISO clause affected
3. [Service Task] - Assign Responsible
   - Auto-assign based on clause/module
4. [User Task] - Root Cause Analysis (5-Why template)
5. [User Task] - Define Corrective Action
6. [User Task] - Approve Corrective Action (if CRITICAL → management approval)
7. [User Task] - Implement Corrective Action
8. [Timer Event] - Wait until due date or completion
9. [User Task] - Verify Effectiveness
10. [Decision Gateway] - Effective?
    - YES → Close Non-Conformity
    - NO → Loop back to step 5 (redefine action)
11. [Service Task] - Update Non-Conformity Status
12. [End Event]
```

**Delegates:**
- `AssignResponsibleDelegate` - Auto-assign por ISO clause
- `UpdateNonConformityStatusDelegate` - Save to ANCNONCONFORMITY table

**Forms:**
- `forms/root_cause_analysis_form.zul` (5-Why interactive)
- `forms/corrective_action_form.zul`
- `forms/effectiveness_verification_form.zul`

**Esfuerzo:** 2.5 días  
**Prioridad:** 🔴 MUY CRÍTICA

---

*[B1.4 - B1.5 resumidos]*

**B1.4:** AI System Decommissioning (BPMN decommission → data retention → archive) - 1.5 días  
**B1.5:** Competence Gap Closure (Training plan → Execute → Verify) - 1.5 días

---

# GRUPO B2: ISO 38507 WORKFLOWS

## PROMPT B2.1 - Board Decision Approval

**Workflow:** `workflow/iso38507_board_decision.bpmn`

**Pasos:**

```
1. [Start Event] - Proposal Submitted (AI investment, policy, risk)
2. [User Task] - Prepare Board Paper
3. [Service Task] - Calculate Strategic Alignment Score
4. [User Task] - Board Review (EDM - Evaluate)
5. [Decision Gateway] - Decision?
   - APPROVE → Go to step 6
   - REJECT → Notify submitter, End
   - REQUEST_MORE_INFO → Loop to step 2
6. [User Task] - Board Decides (EDM - Direct)
7. [Service Task] - Record Board Decision
8. [User Task] - Implement Decision
9. [Service Task] - Monitor Implementation (EDM - Monitor)
10. [End Event]
```

**Delegates:**
- `CalculateStrategicAlignmentDelegate` - Score alignment with AI strategy
- `RecordBoardDecisionDelegate` - Save to BDCBOARDDECISION + BRDBOARDREPORT

**Forms:**
- `forms/board_paper_form.zul`
- `forms/board_decision_form.zul`

**Esfuerzo:** 2 días  
**Prioridad:** 🔴 ALTA (enterprise/public)

---

*[B2.2 - B2.4 resumidos]*

**B2.2:** Ethics Committee Review (Submission → Review → Decision → Monitor) - 2 días  
**B2.3:** Stakeholder Consultation (Identify → Engage → Feedback → Report) - 1.5 días  
**B2.4:** AI Investment Approval (Business case → Evaluate → Approve → Track ROI) - 2 días

---

# GRUPO B3: GDPR WORKFLOWS

## PROMPT B3.1 - Data Subject Rights Request

**REVISAR:** ¿Existe workflow GDPR DSR?

**Workflow:** `workflow/gdpr_dsr_workflow.bpmn`

**Pasos:**

```
1. [Start Event] - DSR Submitted (email, form, portal)
2. [Service Task] - Verify Identity
3. [Decision Gateway] - Identity verified?
   - NO → Request additional info, Loop
   - YES → Continue
4. [User Task] - Classify Request Type
   - Access (Art. 15)
   - Rectification (Art. 16)
   - Erasure (Art. 17)
   - Restriction (Art. 18)
   - Portability (Art. 20)
   - Objection (Art. 21)
5. [Service Task] - Retrieve Personal Data
6. [User Task] - Review Data (redact if needed)
7. [Service Task] - Generate Response
8. [User Task] - Send Response to Data Subject
9. [Service Task] - Record DSR (GDPR Art. 30 requirement)
10. [End Event]
```

**SLA:** 30 días máximo (GDPR Art. 12.3)

**Delegates:**
- `VerifyIdentityDelegate` - Email verification, ID check
- `RetrievePersonalDataDelegate` - Query all tables with PII
- `GenerateDSRResponseDelegate` - PDF/JSON export según request type
- `RecordDSRDelegate` - Save to GDSREQUEST table

**Forms:**
- `forms/dsr_classification_form.zul`
- `forms/dsr_review_form.zul`

**Esfuerzo:** 3 días  
**Prioridad:** 🔴 CRÍTICA (GDPR mandatorio)

---

*[B3.2 - B3.3 resumidos]*

**B3.2:** Data Breach Notification (Detect → Assess → Notify DPA <72h → Notify subjects) - 2 días  
**B3.3:** DPIA Workflow (Trigger → Assess → Mitigate → Document → Review) - 2 días

---

## 📊 RESUMEN BPMN

| Grupo | Workflows | Delegates | Forms ZUL | Esfuerzo |
|-------|-----------|-----------|-----------|----------|
| **B1 ISO 42001** | 5 | 12 | 8 | 10 días |
| **B2 ISO 38507** | 4 | 8 | 6 | 7.5 días |
| **B3 GDPR** | 3 | 10 | 6 | 7 días |
| **TOTAL** | **12** | **30** | **20** | **24.5 días** |

---

## ✅ CHECKLIST IMPLEMENTACIÓN BPMN

Para cada workflow:

1. **REVISAR EXISTENTES:**
   - [ ] Buscar en `workflow/` workflow similar
   - [ ] Revisar delegates existentes
   - [ ] Revisar forms ZUL relacionados

2. **SI NO EXISTE:**
   - [ ] Diseñar BPMN (Camunda Modeler)
   - [ ] Crear Delegates Java
   - [ ] Crear Forms ZUL
   - [ ] Testing workflow end-to-end
   - [ ] Deploy Camunda

3. **SI EXISTE PARCIALMENTE:**
   - [ ] Extender workflow existente
   - [ ] Añadir steps necesarios
   - [ ] Reutilizar delegates si posible

4. **POST-IMPLEMENTACIÓN:**
   - [ ] Documentar workflow (diagrama + descripción)
   - [ ] Testing SLA (timers, deadlines)
   - [ ] Integrar con BusinessServices
   - [ ] Monitoring Camunda Cockpit

---

## 🚀 PRÓXIMOS PASOS

1. **ASIGNAR CHAT:** Este documento a chat especializado BPMN/Camunda
2. **CHAT REVISA:** Workflows existentes en `workflow/`
3. **CHAT REPORTA:** Qué existe, qué falta, qué extender
4. **IMPLEMENTAR:** Workflows faltantes

---

**Documento BPMN completo. Siguiente: PROMPTS_10_PYTHON**

