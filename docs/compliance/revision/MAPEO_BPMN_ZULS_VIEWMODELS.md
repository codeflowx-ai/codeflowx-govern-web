# MAPEO BPMN ZULs - VIEWMODELS

**Fecha:** 25 de noviembre de 2025  
**Objetivo:** Mapear todos los ZULs de BPMN con sus ViewModels correspondientes en `com.codeflowx.govern.workflow.viewmodels`

---

## 📊 RESUMEN EJECUTIVO

| Categoría | Total ZULs | ViewModels | Estado |
|-----------|------------|------------|--------|
| **HITL / Supervisión Humana** | 4 | 4 | ✅ Completo |
| **Aprobaciones** | 8 | 8 | ✅ Completo |
| **Evaluaciones** | 6 | 6 | ✅ Completo |
| **Compliance** | 4 | 4 | ✅ Completo |
| **Gestión de Riesgos** | 5 | 5 | ✅ Completo |
| **Auditoría** | 3 | 3 | ✅ Completo |
| **Gestión de Proyectos** | 8 | 8 | ✅ Completo |
| **Incidentes** | 3 | 3 | ✅ Completo |
| **Bandeja de Tareas** | 1 | 1 | ✅ Completo |
| **TOTAL** | **44** | **44** | **100%** |

---

## 📋 MAPEO COMPLETO ZUL → VIEWMODEL

### 🟦 HITL / SUPERVISIÓN HUMANA (4 ZULs)

| ZUL | ViewModel | Ubicación ViewModel | Estado |
|-----|-----------|---------------------|--------|
| `agent-approval-human-override-form.zul` | `AgentApprovalHumanOverrideViewModel` | `com.codeflowx.govern.workflow.viewmodels.AgentApprovalHumanOverrideViewModel` | ✅ |
| `model-approval-human-override-form.zul` | `ModelApprovalHumanOverrideViewModel` | `com.codeflowx.govern.workflow.viewmodels.ModelApprovalHumanOverrideViewModel` | ✅ |
| `prompt-human-review-form.zul` | `PromptHumanReviewViewModel` | `com.codeflowx.govern.workflow.viewmodels.PromptHumanReviewViewModel` | ✅ |
| `hitl-sla-reminder-form.zul` | `HitlSlaReminderViewModel` | `com.codeflowx.govern.workflow.viewmodels.HitlSlaReminderViewModel` | ✅ |

**Relación con Demo:**
- ✅ Estos 4 ViewModels pueden usarse para la pantalla **Supervisión Humana (HITL)**
- ⚠️ Falta dashboard consolidado que los agrupe

---

### ✅ APROBACIONES (8 ZULs)

| ZUL | ViewModel | Ubicación ViewModel | Estado |
|-----|-----------|---------------------|--------|
| `prompt-approval-request-form.zul` | `PromptApprovalRequestViewModel` | `com.codeflowx.govern.workflow.viewmodels.PromptApprovalRequestViewModel` | ✅ |
| `ai-decommission-approval-form.zul` | `AIDecommissionApprovalViewModel` | `com.codeflowx.govern.workflow.viewmodels.AIDecommissionApprovalViewModel` | ✅ |
| `competence-gap-approval-form.zul` | `CompetenceGapApprovalViewModel` | `com.codeflowx.govern.workflow.viewmodels.CompetenceGapApprovalViewModel` | ✅ |
| `corrective-action-approval-form.zul` | `CorrectiveActionApprovalViewModel` | `com.codeflowx.govern.workflow.viewmodels.CorrectiveActionApprovalViewModel` | ✅ |
| `approve-conformity-assessment-form.zul` | `ApproveConformityAssessmentViewModel` | `com.codeflowx.govern.workflow.viewmodels.ApproveConformityAssessmentViewModel` | ✅ |
| `deployer-approval-form.zul` | `DeployerApprovalViewModel` | `com.codeflowx.govern.workflow.viewmodels.DeployerApprovalViewModel` | ✅ |
| `board-decision-paper-form.zul` | `BoardDecisionPaperViewModel` | `com.codeflowx.govern.workflow.viewmodels.BoardDecisionPaperViewModel` | ✅ |
| `management-review-form.zul` | `ManagementReviewViewModel` | `com.codeflowx.govern.workflow.viewmodels.ManagementReviewViewModel` | ✅ |

---

### 📊 EVALUACIONES (6 ZULs)

| ZUL | ViewModel | Ubicación ViewModel | Estado |
|-----|-----------|---------------------|--------|
| `llm-evaluation-review-form.zul` | `LlmEvaluationReviewViewModel` | `com.codeflowx.govern.workflow.viewmodels.LlmEvaluationReviewViewModel` | ✅ |
| `rag-evaluation-review-form.zul` | `RagEvaluationReviewViewModel` | `com.codeflowx.govern.workflow.viewmodels.RagEvaluationReviewViewModel` | ✅ |
| `model-evaluation-review-form.zul` | `ModelEvaluationReviewViewModel` | `com.codeflowx.govern.workflow.viewmodels.ModelEvaluationReviewViewModel` | ✅ |
| `competence-gap-assessment-form.zul` | `CompetenceGapAssessmentViewModel` | `com.codeflowx.govern.workflow.viewmodels.CompetenceGapAssessmentViewModel` | ✅ |
| `corrective-action-assessment-form.zul` | `CorrectiveActionAssessmentViewModel` | `com.codeflowx.govern.workflow.viewmodels.CorrectiveActionAssessmentViewModel` | ✅ |
| `performance-review-decision-form.zul` | `PerformanceReviewDecisionViewModel` | `com.codeflowx.govern.workflow.viewmodels.PerformanceReviewDecisionViewModel` | ✅ |

---

### 🛡️ COMPLIANCE (4 ZULs)

| ZUL | ViewModel | Ubicación ViewModel | Estado |
|-----|-----------|---------------------|--------|
| `compliance-review-form.zul` | `ComplianceReviewViewModel` | `com.codeflowx.govern.workflow.viewmodels.ComplianceReviewViewModel` | ✅ |
| `compliance-review-decision-form.zul` | `ComplianceReviewDecisionViewModel` | `com.codeflowx.govern.workflow.viewmodels.ComplianceReviewDecisionViewModel` | ✅ |
| `review-qms-gaps-form.zul` | `ReviewQmsGapsViewModel` | `com.codeflowx.govern.workflow.viewmodels.ReviewQmsGapsViewModel` | ✅ |
| `complete-documentation-form.zul` | `CompleteDocumentationViewModel` | `com.codeflowx.govern.workflow.viewmodels.CompleteDocumentationViewModel` | ✅ |

---

### ⚠️ GESTIÓN DE RIESGOS (5 ZULs)

| ZUL | ViewModel | Ubicación ViewModel | Estado |
|-----|-----------|---------------------|--------|
| `bias-review-form.zul` | `BiasReviewViewModel` | `com.codeflowx.govern.workflow.viewmodels.BiasReviewViewModel` | ✅ |
| `bias-mitigation-plan-form.zul` | `BiasMitigationPlanViewModel` | `com.codeflowx.govern.workflow.viewmodels.BiasMitigationPlanViewModel` | ✅ |
| `bias-urgent-decision-form.zul` | `BiasUrgentDecisionViewModel` | `com.codeflowx.govern.workflow.viewmodels.BiasUrgentDecisionViewModel` | ✅ |
| `drift-analysis-form.zul` | `DriftAnalysisViewModel` | `com.codeflowx.govern.workflow.viewmodels.DriftAnalysisViewModel` | ✅ |
| `drift-review-decision-form.zul` | `DriftReviewDecisionViewModel` | `com.codeflowx.govern.workflow.viewmodels.DriftReviewDecisionViewModel` | ✅ |

---

### 🔍 AUDITORÍA (3 ZULs)

| ZUL | ViewModel | Ubicación ViewModel | Estado |
|-----|-----------|---------------------|--------|
| `internal-audit-scheduling-form.zul` | `InternalAuditSchedulingViewModel` | `com.codeflowx.govern.workflow.viewmodels.InternalAuditSchedulingViewModel` | ✅ |
| `internal-audit-execution-form.zul` | `InternalAuditExecutionViewModel` | `com.codeflowx.govern.workflow.viewmodels.InternalAuditExecutionViewModel` | ✅ |
| `management-review-schedule-form.zul` | `ManagementReviewScheduleViewModel` | `com.codeflowx.govern.workflow.viewmodels.ManagementReviewScheduleViewModel` | ✅ |

---

### 📋 GESTIÓN DE PROYECTOS / FRIA (8 ZULs)

| ZUL | ViewModel | Ubicación ViewModel | Estado |
|-----|-----------|---------------------|--------|
| `complete-missing-elements-form.zul` | `CompleteMissingElementsViewModel` | `com.codeflowx.govern.workflow.viewmodels.CompleteMissingElementsViewModel` | ✅ |
| `define-modifications-form.zul` | `DefineModificationsViewModel` | `com.codeflowx.govern.workflow.viewmodels.DefineModificationsViewModel` | ✅ |
| `enhanced-review-form.zul` | `EnhancedReviewViewModel` | `com.codeflowx.govern.workflow.viewmodels.EnhancedReviewViewModel` | ✅ |
| `final-review-form.zul` | `FinalReviewViewModel` | `com.codeflowx.govern.workflow.viewmodels.FinalReviewViewModel` | ✅ |
| `eu-registration-form.zul` | `EURegistrationFormViewModel` | `com.codeflowx.govern.workflow.viewmodels.EURegistrationFormViewModel` | ✅ |
| `review-registration-package-form.zul` | `ReviewRegistrationPackageViewModel` | `com.codeflowx.govern.workflow.viewmodels.ReviewRegistrationPackageViewModel` | ✅ |
| `fix-validation-errors-form.zul` | `FixValidationErrorsViewModel` | `com.codeflowx.govern.workflow.viewmodels.FixValidationErrorsViewModel` | ✅ |
| `manual-resolution-form.zul` | `ManualResolutionViewModel` | `com.codeflowx.govern.workflow.viewmodels.ManualResolutionViewModel` | ✅ |

---

### 🚨 INCIDENTES (3 ZULs)

| ZUL | ViewModel | Ubicación ViewModel | Estado |
|-----|-----------|---------------------|--------|
| `document-incident-details-form.zul` | `DocumentIncidentDetailsViewModel` | `com.codeflowx.govern.workflow.viewmodels.DocumentIncidentDetailsViewModel` | ✅ |
| `verify-incident-resolution-form.zul` | `VerifyIncidentResolutionViewModel` | `com.codeflowx.govern.workflow.viewmodels.VerifyIncidentResolutionViewModel` | ✅ |
| `root-cause-analysis-form.zul` | `RootCauseAnalysisViewModel` | `com.codeflowx.govern.workflow.viewmodels.RootCauseAnalysisViewModel` | ✅ |

---

### 🔄 ACCIONES CORRECTIVAS (4 ZULs)

| ZUL | ViewModel | Ubicación ViewModel | Estado |
|-----|-----------|---------------------|--------|
| `define-corrective-actions-form.zul` | `DefineCorrectiveActionsViewModel` | `com.codeflowx.govern.workflow.viewmodels.DefineCorrectiveActionsViewModel` | ✅ |
| `corrective-action-implementation-form.zul` | `CorrectiveActionImplementationViewModel` | `com.codeflowx.govern.workflow.viewmodels.CorrectiveActionImplementationViewModel` | ✅ |
| `corrective-action-effectiveness-form.zul` | `CorrectiveActionEffectivenessViewModel` | `com.codeflowx.govern.workflow.viewmodels.CorrectiveActionEffectivenessViewModel` | ✅ |
| `management-review-actions-form.zul` | `ManagementReviewActionsViewModel` | `com.codeflowx.govern.workflow.viewmodels.ManagementReviewActionsViewModel` | ✅ |

---

### 🏥 ÉTICA (3 ZULs)

| ZUL | ViewModel | Ubicación ViewModel | Estado |
|-----|-----------|---------------------|--------|
| `ethics-review-request-form.zul` | `EthicsReviewRequestViewModel` | `com.codeflowx.govern.workflow.viewmodels.EthicsReviewRequestViewModel` | ✅ |
| `ethics-review-reminder-form.zul` | `EthicsReviewReminderViewModel` | `com.codeflowx.govern.workflow.viewmodels.EthicsReviewReminderViewModel` | ✅ |
| `ethics-committee-review-form.zul` | `EthicsCommitteeReviewViewModel` | `com.codeflowx.govern.workflow.viewmodels.EthicsCommitteeReviewViewModel` | ✅ |
| `ethics-mitigation-plan-form.zul` | `EthicsMitigationPlanViewModel` | `com.codeflowx.govern.workflow.viewmodels.EthicsMitigationPlanViewModel` | ✅ |

---

### 📚 COMPETENCIA (5 ZULs)

| ZUL | ViewModel | Ubicación ViewModel | Estado |
|-----|-----------|---------------------|--------|
| `competence-gap-assessment-form.zul` | `CompetenceGapAssessmentViewModel` | `com.codeflowx.govern.workflow.viewmodels.CompetenceGapAssessmentViewModel` | ✅ |
| `competence-gap-approval-form.zul` | `CompetenceGapApprovalViewModel` | `com.codeflowx.govern.workflow.viewmodels.CompetenceGapApprovalViewModel` | ✅ |
| `competence-gap-implementation-form.zul` | `CompetenceGapImplementationViewModel` | `com.codeflowx.govern.workflow.viewmodels.CompetenceGapImplementationViewModel` | ✅ |
| `competence-gap-additional-actions-form.zul` | `CompetenceGapAdditionalActionsViewModel` | `com.codeflowx.govern.workflow.viewmodels.CompetenceGapAdditionalActionsViewModel` | ✅ |
| `competence-gap-rework-form.zul` | `CompetenceGapReworkViewModel` | `com.codeflowx.govern.workflow.viewmodels.CompetenceGapReworkViewModel` | ✅ |

---

### 🔄 DESMANTELAMIENTO (4 ZULs)

| ZUL | ViewModel | Ubicación ViewModel | Estado |
|-----|-----------|---------------------|--------|
| `ai-decommission-request-form.zul` | `AIDecommissionRequestViewModel` | `com.codeflowx.govern.workflow.viewmodels.AIDecommissionRequestViewModel` | ✅ |
| `ai-decommission-approval-form.zul` | `AIDecommissionApprovalViewModel` | `com.codeflowx.govern.workflow.viewmodels.AIDecommissionApprovalViewModel` | ✅ |
| `ai-decommission-verification-form.zul` | `AIDecommissionVerificationViewModel` | `com.codeflowx.govern.workflow.viewmodels.AIDecommissionVerificationViewModel` | ✅ |
| `ai-decommission-retention-form.zul` | `AIDecommissionRetentionViewModel` | `com.codeflowx.govern.workflow.viewmodels.AIDecommissionRetentionViewModel` | ✅ |

---

### 🔔 RECORDATORIOS (3 ZULs)

| ZUL | ViewModel | Ubicación ViewModel | Estado |
|-----|-----------|---------------------|--------|
| `model-approval-reminder-form.zul` | `ModelApprovalReminderViewModel` | `com.codeflowx.govern.workflow.viewmodels.ModelApprovalReminderViewModel` | ✅ |
| `dataset-review-reminder-form.zul` | `DatasetReviewReminderViewModel` | `com.codeflowx.govern.workflow.viewmodels.DatasetReviewReminderViewModel` | ✅ |
| `hitl-sla-reminder-form.zul` | `HitlSlaReminderViewModel` | `com.codeflowx.govern.workflow.viewmodels.HitlSlaReminderViewModel` | ✅ |

---

### 🟦 BANDEJA DE TAREAS (1 ZUL)

| ZUL | ViewModel | Ubicación ViewModel | Estado |
|-----|-----------|---------------------|--------|
| `task-inbox.zul` | `TaskInboxViewModel` | `com.codeflowx.govern.workflow.viewmodels.TaskInboxViewModel` | ✅ |

**Funcionalidad:**
- Lista todas las tareas BPMN pendientes
- Filtros por proceso, prioridad, estado
- Acceso a formularios ZUL individuales de cada tarea
- Asignación de tareas
- Claim/Release de tareas

**Relación con Demo:**
- ✅ **OBLIGATORIA** para la demo
- Muestra todas las tareas de los procesos BPMN
- Permite acceder a los formularios individuales

---

## 🎯 VIEWMODELS RELACIONADOS CON DEMO

### Supervisión Humana (HITL):
1. ✅ `AgentApprovalHumanOverrideViewModel` - Aprobación de agentes
2. ✅ `ModelApprovalHumanOverrideViewModel` - Aprobación de modelos
3. ✅ `PromptHumanReviewViewModel` - Revisión de prompts
4. ✅ `HitlSlaReminderViewModel` - Recordatorios SLA

**Acceso desde Bandeja de Tareas:**
- Todas estas tareas aparecen en `task-inbox.zul`
- Al hacer clic, se abre el formulario ZUL correspondiente

**Para Dashboard Consolidado:**
- Crear `HitlSupervisionViewModel` que consolide estas 4 vistas
- Mostrar estadísticas de supervisión humana
- Mostrar registro de intervenciones
- Mostrar auditoría de decisiones

---

### Trazabilidad y Evidencias:
Los ViewModels de BPMN no están directamente relacionados con trazabilidad, pero las tareas BPMN generan logs que pueden usarse para trazabilidad.

**ViewModels Relacionados (en otras carpetas):**
- `AgentDecisionsLogViewModel` - Log de decisiones de agentes
- `AuditLogOverviewViewModel` - Logs de auditoría
- `PolicyAuditLogOverviewViewModel` - Logs de políticas

---

### Alertas e Incidentes:
1. ✅ `DocumentIncidentDetailsViewModel` - Documentar incidentes
2. ✅ `VerifyIncidentResolutionViewModel` - Verificar resolución
3. ✅ `RootCauseAnalysisViewModel` - Análisis de causa raíz
4. ✅ `AlertResponseViewModel` - Respuesta a alertas

**Acceso desde Bandeja de Tareas:**
- Estas tareas aparecen en `task-inbox.zul` cuando hay incidentes

---

## 📊 RESUMEN PARA DEMO

### ✅ Completamente Implementado:
- **Bandeja de Tareas:** `task-inbox.zul` + `TaskInboxViewModel` ✅
- **Formularios HITL:** 4 ZULs + 4 ViewModels ✅
- **Formularios de Incidentes:** 3 ZULs + 3 ViewModels ✅

### ⚠️ Parcialmente Implementado:
- **Supervisión Humana Dashboard:** Existen 4 ViewModels individuales, falta dashboard consolidado
- **Trazabilidad:** Los ViewModels de BPMN generan logs, pero falta vista consolidada

### 🔴 Pendiente:
- Dashboard consolidado de Supervisión Humana
- Vista consolidada de Trazabilidad y Evidencias

---

## 🔗 INTEGRACIÓN CON BANDEJA DE TAREAS

**Flujo de Trabajo:**
1. Usuario abre `task-inbox.zul`
2. Ve lista de tareas pendientes (incluye tareas HITL, incidentes, etc.)
3. Hace clic en una tarea
4. Se abre el formulario ZUL correspondiente (ej: `agent-approval-human-override-form.zul`)
5. Usuario completa el formulario
6. Se completa la tarea BPMN
7. El proceso continúa

**Para Demo:**
- ✅ Mostrar `task-inbox.zul` con tareas de ejemplo
- ✅ Hacer clic en una tarea HITL para mostrar formulario
- ✅ Hacer clic en una tarea de incidente para mostrar formulario
- ✅ Demostrar que las tareas están conectadas con procesos BPMN reales

---

**Última actualización:** 25 de noviembre de 2025  
**Total ZULs BPMN:** 44  
**Total ViewModels Workflow:** 44  
**Cobertura:** 100%


