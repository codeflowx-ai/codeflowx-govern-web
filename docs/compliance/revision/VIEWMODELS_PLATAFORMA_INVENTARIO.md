# INVENTARIO VIEWMODELS PLATAFORMA

**Fecha:** 25 de noviembre de 2025
**Objetivo:** Inventario completo de ViewModels en `com.codeflowx.platform.viewmodel` y `com.codeflowx.platform.viewmodels`

---

## 📊 RESUMEN EJECUTIVO

| Carpeta | Total ViewModels | Relacionados con Demo |
|---------|------------------|----------------------|
| **governance/** | 38 | 5 |
| **compliance/** | 2 | 2 |
| **projects/** | 48 | 1 |
| **monitoring/** | 15 | 3 |
| **models/** | ? | 1 |
| **agents/** | ? | 0 |
| **rag/** | ? | 0 |
| **prompts/** | ? | 0 |
| **evaluation/** | ? | 0 |
| **training/** | ? | 0 |
| **serving/** | ? | 0 |
| **analytics/** | ? | 0 |
| **providers/** | ? | 0 |
| **infrastructure/** | ? | 0 |
| **dashboard/** | ? | 0 |
| **core/** | ? | 0 |
| **notifications/** | ? | 0 |
| **datasources/** | ? | 0 |
| **domainingestion/** | ? | 0 |
| **artefacto/** | ? | 0 |
| **playground/** | ? | 0 |
| **TOTAL** | **103+** | **12+** |

---

## 📋 VIEWMODELS POR CARPETA

### 1. GOVERNANCE (38 ViewModels)

#### Dashboard y Resumen:
- `GovernanceDashboardSummaryOverviewViewModel` ✅ **USADO EN DEMO**
- `GovernanceOverviewOverviewViewModel`
- `GovernanceKpisExecutiveOverviewViewModel`
- `GovernanceMetricsSummaryOverviewViewModel`

#### Métricas:
- `GovernanceMetricOverviewViewModel`
- `GovernanceMetricDetailViewModel`
- `SecurityMetricOverviewViewModel`
- `SecurityMetricDetailViewModel`

#### Políticas:
- `PolicyOverviewViewModel`
- `PolicyDetailViewModel`
- `PolicyRuleOverviewViewModel`
- `PolicyRuleDetailViewModel`
- `PolicyEvaluationOverviewViewModel`
- `PolicyEvaluationDetailViewModel`
- `PolicyEvaluationTrendsOverviewViewModel`
- `PolicyValidationConfigOverviewViewModel`
- `PolicyValidationConfigDetailViewModel`
- `PolicyChecklistItemOverviewViewModel`
- `PolicyChecklistItemDetailViewModel`
- `PolicyViolationOverviewViewModel`
- `PolicyViolationDetailViewModel`
- `PolicyEffectivenessReportOverviewViewModel`
- `PolicyAuditLogOverviewViewModel` ✅ **RELACIONADO CON TRAZABILIDAD**
- `PolicyAuditLogDetailViewModel` ✅ **RELACIONADO CON TRAZABILIDAD**

#### Compliance:
- `ComplianceAssessmentOverviewViewModel` ✅ **RELACIONADO CON DEMO**
- `ComplianceAssessmentDetailViewModel` ✅ **RELACIONADO CON DEMO**
- `ComplianceByFrameworkOverviewViewModel`
- `ComplianceRequirementOverviewViewModel`
- `ComplianceRequirementDetailViewModel`
- `ComplianceFindingOverviewViewModel`
- `ComplianceFindingDetailViewModel`
- `ComplianceGapsAnalysisOverviewViewModel`

#### Riesgos:
- `RiskAssessmentMatrixOverviewViewModel` ✅ **RELACIONADO CON FRIA**
- `SecurityThreatOverviewViewModel`
- `SecurityThreatDetailViewModel`
- `SecurityPolicyOverviewViewModel`
- `SecurityPolicyDetailViewModel`

#### Auditoría:
- `GovernanceAuditTrailDetailedOverviewViewModel` ✅ **RELACIONADO CON TRAZABILIDAD**
- `ViolationHeatmapOverviewViewModel`
- `AutoApprovalAnalyticsOverviewViewModel`

---

### 2. COMPLIANCE (2 ViewModels)

- `ComplianceAssessmentViewModel` ✅ **RELACIONADO CON DEMO**
- `FriaAssessmentViewModel` ✅ **RELACIONADO CON DEMO** (diferente de `FriaWizardViewModel`)

**Nota:** `FriaAssessmentViewModel` es diferente de `FriaWizardViewModel`:
- `FriaWizardViewModel` está en `com.codeflowx.govern.viewmodel.compliance` (wizard de 6 pasos)
- `FriaAssessmentViewModel` está en `com.codeflowx.platform.viewmodel.compliance` (gestión de evaluaciones)

---

### 3. PROJECTS (48 ViewModels)

#### Principales:
- `ProjectOverviewViewModel`
- `ProjectDetailViewModel` ✅ **RELACIONADO CON DEMO** (ficha de sistema)
- `ProjectAIInventoryViewModel` ✅ **USADO EN DEMO** (pero está en `com.codeflowx.govern.viewmodel.projects`)

#### Otros:
- `ProjectArtifactOverviewViewModel`
- `ProjectArtifactDetailViewModel`
- `ProjectDocumentOverviewViewModel`
- `ProjectDocumentDetailViewModel`
- `ProjectMemberOverviewViewModel`
- `ProjectMemberDetailViewModel`
- `ProjectTaskOverviewViewModel`
- `ProjectTaskDetailViewModel`
- `ProjectVersionOverviewViewModel`
- `ProjectVersionDetailViewModel`
- `ProjectRequirementOverviewViewModel`
- `ProjectRequirementDetailViewModel`
- `ProjectDomainOverviewViewModel`
- `ProjectDomainDetailViewModel`
- `ProjectStackOverviewViewModel`
- `ProjectStackDetailViewModel`
- `ProjectTechnologyOverviewViewModel`
- `ProjectTechnologyDetailViewModel`
- `ProjectLicenseOverviewViewModel`
- `ProjectLicenseDetailViewModel`
- `ProjectTokenOverviewViewModel`
- `ProjectTokenDetailViewModel`
- `ProjectBillingDetailOverviewViewModel`
- `ProjectBillingDetailDetailViewModel`
- `ProjectBillingStatusOverviewViewModel`
- `ProjectCostEstimatorOverviewViewModel`
- `ProjectCostEstimatorDetailViewModel`
- `ProjectResourceConsumptionOverviewViewModel`
- `ProjectResourceConsumptionDetailViewModel`
- `ProjectResourceAllocationOverviewViewModel`
- `ProjectTimeTrackingOverviewViewModel`
- `ProjectTimeTrackingDetailViewModel`
- `ProjectROIOverviewViewModel`
- `ProjectROIDetailViewModel`
- `ProjectRoiAnalysisOverviewViewModel`
- `ProjectFinancialSummaryOverviewViewModel`
- `ProjectCostBreakdownOverviewViewModel`
- `ProjectPortfolioDashboardOverviewViewModel`
- `ProjectTimelineGanttOverviewViewModel`
- `ProjectInvoiceOverviewViewModel`
- `ProjectInvoiceDetailViewModel`
- `ClientProfitabilityAnalysisOverviewViewModel`
- `InvoiceAgingReportOverviewViewModel`

---

### 4. MONITORING (15 ViewModels)

#### Alertas:
- `MonitoringAlertOverviewViewModel` ✅ **USADO EN DEMO**
- `MonitoringAlertDetailViewModel` ✅ **USADO EN DEMO**
- `SystemAlertOverviewViewModel` ✅ **RELACIONADO CON DEMO**
- `SystemAlertDetailViewModel` ✅ **RELACIONADO CON DEMO**
- `AlertSummaryOverviewViewModel` ✅ **RELACIONADO CON DEMO**

#### Dashboard:
- `MonitoringDashboardOverviewViewModel` ✅ **RELACIONADO CON DEMO**

#### Métricas:
- `MonitoringMetricOverviewViewModel`
- `MonitoringMetricDetailViewModel`
- `SystemMetricOverviewViewModel`
- `SystemMetricDetailViewModel`
- `MetricsVisualizationOverviewViewModel`

#### Salud del Sistema:
- `SystemHealthOverviewViewModel`
- `SystemHealthDetailViewModel`

#### Auditoría:
- `AuditLogOverviewViewModel` ✅ **RELACIONADO CON TRAZABILIDAD**
- `AuditLogDetailViewModel` ✅ **RELACIONADO CON TRAZABILIDAD**

#### Otros:
- `AnomalyHeatmapOverviewViewModel`

---

## 🔍 VIEWMODELS RELACIONADOS CON DEMO

### Dashboard General:
1. ✅ `DashboardViewModel` (`com.codeflowx.govern.viewmodel.DashboardViewModel`)
2. ✅ `GovernanceDashboardSummaryOverviewViewModel` (`com.codeflowx.platform.viewmodel.governance.GovernanceDashboardSummaryOverviewViewModel`)

### Inventario Sistemas IA:
1. ✅ `ProjectAIInventoryViewModel` (`com.codeflowx.govern.viewmodel.projects.ProjectAIInventoryViewModel`)

### Ficha Sistema IA:
1. ✅ `ProjectDetailViewModel` (`com.codeflowx.platform.viewmodel.projects.ProjectDetailViewModel`)
2. ✅ `ModelsDetailViewModel` (en `com.codeflowx.govern.viewmodel.models`)

### Clasificación AI Act:
1. ✅ `HighRiskClassifierViewModel` (`com.codeflowx.govern.viewmodel.compliance.HighRiskClassifierViewModel`)

### Evaluación Riesgos / FRIA:
1. ✅ `FriaWizardViewModel` (`com.codeflowx.govern.viewmodel.compliance.FriaWizardViewModel`)
2. ⚠️ `FriaAssessmentViewModel` (`com.codeflowx.platform.viewmodel.compliance.FriaAssessmentViewModel`) - Gestión de evaluaciones

### Bandeja de Tareas:
1. ✅ `TaskInboxViewModel` (`com.codeflowx.govern.workflow.viewmodels.TaskInboxViewModel`)

### Supervisión Humana (HITL):
1. ⚠️ `AgentApprovalHumanOverrideViewModel` (`com.codeflowx.govern.workflow.viewmodels.AgentApprovalHumanOverrideViewModel`) - Para agentes
2. ⚠️ `PromptHumanReviewViewModel` (`com.codeflowx.govern.workflow.viewmodels.PromptHumanReviewViewModel`) - Para prompts
3. ⚠️ `HitlSlaReminderViewModel` (`com.codeflowx.govern.workflow.viewmodels.HitlSlaReminderViewModel`) - Recordatorios
4. 🔴 **FALTA:** `HitlSupervisionViewModel` - Dashboard consolidado de HITL

### Trazabilidad y Evidencias:
1. ✅ `PolicyAuditLogOverviewViewModel` (`com.codeflowx.platform.viewmodel.governance.PolicyAuditLogOverviewViewModel`)
2. ✅ `PolicyAuditLogDetailViewModel` (`com.codeflowx.platform.viewmodel.governance.PolicyAuditLogDetailViewModel`)
3. ✅ `AuditLogOverviewViewModel` (`com.codeflowx.platform.viewmodel.monitoring.AuditLogOverviewViewModel`)
4. ✅ `AuditLogDetailViewModel` (`com.codeflowx.platform.viewmodel.monitoring.AuditLogDetailViewModel`)
5. ✅ `GovernanceAuditTrailDetailedOverviewViewModel` (`com.codeflowx.platform.viewmodel.governance.GovernanceAuditTrailDetailedOverviewViewModel`)
6. ✅ `AgentDecisionsLogViewModel` (`com.codeflowx.govern.viewmodel.agents.AgentDecisionsLogViewModel`)
7. 🔴 **FALTA:** `TraceabilityEvidenceViewModel` - Vista consolidada de trazabilidad

### Alertas e Incidentes:
1. ✅ `MonitoringAlertOverviewViewModel` (`com.codeflowx.platform.viewmodel.monitoring.MonitoringAlertOverviewViewModel`)
2. ✅ `MonitoringAlertDetailViewModel` (`com.codeflowx.platform.viewmodel.monitoring.MonitoringAlertDetailViewModel`)
3. ✅ `SystemAlertOverviewViewModel` (`com.codeflowx.platform.viewmodel.monitoring.SystemAlertOverviewViewModel`)
4. ✅ `AlertSummaryOverviewViewModel` (`com.codeflowx.platform.viewmodel.monitoring.AlertSummaryOverviewViewModel`)

### Documentación / Reporte:
1. ⚠️ `AIActDocumentationGeneratorViewModel` (`com.codeflowx.platform.viewmodel.compliance.AIActDocumentationGeneratorViewModel`) - Generador
2. ⚠️ `ConformityDeclarationManagerViewModel` (`com.codeflowx.govern.viewmodel.compliance.ConformityDeclarationManagerViewModel`) - Declaraciones
3. 🔴 **FALTA:** `ComplianceReportViewModel` - Reporte consolidado para demo

---

## 📋 ZULs ENCONTRADOS

### Dashboard:
- ✅ `console/dashboard.zul` - `DashboardViewModel`
- ✅ `console/platform/governance/dashboard/summary.zul` - `GovernanceDashboardSummaryOverviewViewModel`
- ✅ `console/platform/governance/dashboard/overview.zul`

### Inventario:
- ✅ `console/platform/projects/project-ai-inventory.zul` - `ProjectAIInventoryViewModel`

### Compliance:
- ✅ `console/gobierno/compliance/high-risk-classifier.zul` - `HighRiskClassifierViewModel`
- ✅ `console/gobierno/compliance/fria-wizard.zul` - `FriaWizardViewModel`
- ✅ `console/gobierno/compliance/ai-act-documentation-generator.zul`
- ✅ `console/gobierno/compliance/conformity-declaration-manager.zul`
- ✅ `console/gobierno/compliance/conformity-review-form.zul`
- ✅ `console/gobierno/compliance/eu-registration-status.zul`
- ✅ `console/gobierno/compliance/sector-dashboard.zul`

### BPMN / Tareas:
- ✅ `console/bpmn/task-inbox.zul` - `TaskInboxViewModel`
- ✅ `console/bpmn/agent-approval-human-override-form.zul`
- ✅ `console/bpmn/prompt-human-review-form.zul`
- ✅ `console/bpmn/hitl-sla-reminder-form.zul`

### Monitoring:
- ✅ `console/platform/monitoring/dashboard/page.zul`
- ✅ `console/platform/monitoring/dashboard/overview.zul`
- ✅ `console/platform/monitoring/dashboard/summary.zul`
- ✅ `console/platform/monitoring/alerts/overview.zul` - `MonitoringAlertOverviewViewModel`
- ✅ `console/platform/monitoring/alerts/detail.zul` - `MonitoringAlertDetailViewModel`
- ✅ `console/platform/monitoring/alerts/system-overview.zul` - `SystemAlertOverviewViewModel`
- ✅ `console/platform/monitoring/alerts/system-detail.zul` - `SystemAlertDetailViewModel`
- ✅ `console/platform/monitoring/alerts/summary.zul` - `AlertSummaryOverviewViewModel`

### Governance:
- ✅ `console/gobierno/governance/governance-overview.zul`
- ✅ `console/gobierno/governance/governance-detail.zul`
- ✅ `console/gobierno/governance/ai_objectives_management.zul`
- ✅ `console/gobierno/governance/ai_competence_management.zul`

### Projects:
- ✅ `console/gobierno/projects/projects-dashboard.zul`
- ✅ `console/gobierno/projects/projects-overview.zul`
- ✅ `console/gobierno/projects/projects-detail.zul`

---

## 🎯 RECOMENDACIONES PARA DEMO

### Pantallas Listas (6):
1. ✅ **Dashboard General** - Usar `console/dashboard.zul` (más completo con tareas)
2. ✅ **Inventario Sistemas IA** - Usar `console/platform/projects/project-ai-inventory.zul`
3. ✅ **Ficha Sistema IA** - Usar `ProjectDetailViewModel` o `ModelsDetailViewModel`
4. ✅ **Clasificación AI Act** - Usar `console/gobierno/compliance/high-risk-classifier.zul`
5. ✅ **Evaluación Riesgos / FRIA** - Usar `console/gobierno/compliance/fria-wizard.zul`
6. ✅ **Bandeja de Tareas** - Usar `console/bpmn/task-inbox.zul`

### Pantallas a Crear (4):
1. 🔴 **Supervisión Humana (HITL)** - Consolidar ViewModels existentes en un dashboard
2. 🔴 **Trazabilidad y Evidencias** - Consolidar ViewModels de auditoría existentes
3. 🔴 **Documentación / Reporte** - Crear reporte consolidado
4. 🔴 **Pantalla Final** - Pantalla estática simple

---

**Última actualización:** 25 de noviembre de 2025
