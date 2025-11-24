# Guía Funcional y Técnica de Procesos BPMN – CodeflowX Govern

> **Fecha:** 15 Nov 2025  
> **Ámbito:** Todos los procesos en `codeflowx.govern.workflow.lib/src/main/resources/processes` (excluyendo `education/`).  
> **Objetivo:** Proveer a negocio y desarrollo una descripción funcional y la vista técnica (tareas, delegates, reglas, formularios, variables) de cada flujo.

## Convenciones

- **Tareas de servicio** mapean a delegates Spring (`@Component`) empaquetados en el módulo `codeflowx.govern.workflow.lib`.
- **Business Rule Tasks** usan reglas Drools (`rules/**/*.drl`); se indica archivo asociado.
- **User Tasks** referencian ViewModels ZUL u otros formularios; si no existe evidencia se marca **TODO**.
- **Variables** listan entradas/salidas críticas. Las claves en cursiva requieren confirmación/implementación.

---

## 1. Procesos de aprobación

### 1.1 `agent-approval-v1.bpmn`

- **Propósito:** Validar agentes IA antes de habilitarlos en producción.
- **Trigger:** Solicitud de aprobación (`agentId`, `agentName`, `approverEmail`).
- **Actores:** Equipo de gobierno (HITL), sistema de reglas.
- **Flujo funcional:**
  1. Start Event → se inyectan datos del agente.
  2. Parallel Gateway divide en tres validaciones: Risk Assessment, Compliance Check, Ethics Review.
  3. Join Gateway espera resultados.
  4. Business Rule Task `agent-scoring` calcula decisión.
  5. Exclusive Gateway decide: `AUTO_APPROVE`, `HITL_REQUIRED`, `AUTO_REJECT`.
  6. En caso HITL, User Task `Agent Manual Review`.
  7. Service Task final según decisión (auto approve/reject).
- **Mapa técnico:**

| Paso | Tipo | Delegate / Form / Regla | Notas |
| --- | --- | --- | --- |
| Risk Assessment | Service | `AIRiskAssessmentDelegate` | Obtiene métricas de riesgo operativo. |
| Compliance Check | Service | `AIComplianceCheckDelegate` | Verifica políticas AI‑OS y anexos EU AI Act. |
| Ethics Review | Service | `AIEthicalReviewDelegate` | Llama repositorio de decisiones éticas. |
| Business Rule | Rule Task | `rules/agent-scoring.drl` | Pondera `riskScore`, `complianceScore`, `ethicsScore`. |
| HITL Review | User Task | `agent-approval-human-override-form.zul` (`AgentApprovalWorkflowViewModel`) | SLA 2 días. |
| Auto Approve | Service | `AutoApproveAgentDelegate` | Marca agente aprobado y registra en `ImmutableLog`. |
| Auto Reject | Service | `AutoRejectAgentDelegate` | Genera evidencia de rechazo. |

- **Variables clave:** `agentId`, `agentName`, `riskScore`, `complianceScore`, `ethicsScore`, `decision`, `humanReviewer`, `immutableLogId`.

### 1.2 `model-approval-v1.bpmn`

- **Propósito:** Aprobar modelos ML antes de su despliegue.
- **Trigger:** ML Engineer inicia solicitud con `modelId`, `versionId`.
- **Actores:** ML Engineer, Governance team.
- **Flujo:** Solicitud → validaciones paralelas (Performance, Bias Detection, Compliance) → Join → User Tasks (ML Engineer Review, Governance Review) → Business Rule Task (`model-approval-scoring.drl`) → Gateway (Approved / Conditional / Rejected) → Service Tasks (`MarkModelProductionDelegate`, `MarkModelConditionalDelegate`, `RejectModelDelegate`).
- **Variables:** `modelId`, `versionId`, `approvalType`, `performanceScore`, `biasScore`, `complianceScore`, `mlEngineerApproval`, `governanceApproval`, `finalDecision`.

### 1.3 `prompt-approval-v1.bpmn`

- **Propósito:** Controlar la publicación de prompts LLM/RAG.
- **Flujo:** Start → Service Task `PromptSafetyDelegate` (evalúa PI, jailbreak, PII) → Business Rule Task `prompt-governance.drl` → Gateway `AUTO_APPROVE | HITL | AUTO_REJECT` → Service Tasks (`AutoApprovePromptDelegate`, `RejectEthicsDelegate`) o User Task `PromptManualReview` (form `prompt-approval-human-override.zul`).
- **Variables:** `promptId`, `promptText`, `safetyFindings`, `decision`.

### 1.4 `adapter-creation-approval-v1.bpmn20.xml`

- **Propósito:** Evaluar solicitudes de adapters frente a políticas de reuse.
- **Fuente:** `PROMPTS_06_MLOPS_ADAPTERS_FINETUNING.md`.
- **Flujo principal:** Start → `ValidateAdapterRequestDelegate` → `RiskScoringDelegate` → Gateway (High risk → HITL, else → Auto) → `RegisterAdapterDelegate`.
- **Reglas:** `adapter-approval-rules.drl` (pendiente confirmación).
- **Variables:** `adapterId`, `owner`, `riskScore`, `reusePlan`.

### 1.5 `finetuning-approval-v1.bpmn20.xml`

- **Propósito:** Controlar peticiones de fine-tuning completo.
- **Puntos clave:** obliga a comprobar disponibilidad de adapters, exige evaluación de cumplimiento Art. 53.
- **Delegates previstos:** `EvaluateFineTuningRiskDelegate`, `CheckAdapterAlternativesDelegate`, `EscalateFineTuningRequestDelegate`.
- **Reglas:** `finetuning-approval-rules.drl`.

### 1.6 `consent-management-v1.bpmn`

- **Propósito:** Gestionar consentimientos GDPR (registro, validación, revocación).
- **Flujo:** Start → `recordConsentRequest` (Service) → `validateConsent` (Service) → Gateway (válido / no válido) → `storeConsent` o `rejectRequest` → `notifyDataSubject` → Timer `CheckExpiration` (cada 30 días) → `expireConsent`.
- **Delegates:** `RecordConsentDelegate`, `ValidateConsentDelegate`, `StoreConsentDelegate`, `RejectConsentDelegate`, `NotifyDataSubjectDelegate`, `ExpireConsentDelegate`.
- **Variables:** `consentId`, `dataSubjectId`, `legalBasis`, `status`, `expiresAt`.

### 1.7 `internal-conformity-assessment-v1.bpmn`

- **Propósito:** Autoevaluación interna Annex VI (Art. 43).
- **Flujo clave:** Start → Service Tasks por artículo (`verifyRiskManagement`, `verifyDataGovernance`, `verifyDocumentation`, `verifyRecordKeeping`, `verifyTransparency`, `verifyHumanOversight`, `verifyAccuracy`) → Business Rule `calculateComplianceScore` → Gateway (>=90% → generar declaración, <90% → Human Review) → User Task `humanReview` → Gateway → `generateDeclaration` → `issueDeclaration`.
- **Delegates:** `VerifyRiskManagementDelegate`, `VerifyDataGovernanceDelegate`, `VerifyDocumentationDelegate`, `VerifyRecordKeepingDelegate`, `VerifyTransparencyDelegate`, `VerifyHumanOversightDelegate`, `VerifyAccuracyDelegate`, `GenerateConformityDeclarationDelegate`, `IssueDeclarationDelegate`.
- **Reglas:** `internal-conformity-score.drl`.
- **Variables:** `assessmentId`, `entityType`, `art9Compliant`…`art15Compliant`, `overallScore`, `compliancePassed`, `declarationId`.

### 1.8 `conformity-assessment-process.bpmn20.xml`

- **Propósito:** Proceso completo Annex VI con intervención HITL.
- **Flujo (ver PROMPT B.1):** User Task `Initiate Assessment` → Service Task `Verify QMS Compliance` → Gateway (QMS pass?) → User/User tasks `Review QMS Gaps` → Service Task `Review Technical Documentation` → Gateway → Service Task `Verify Process Consistency` → Gateway `All Steps Pass?` → Service Task `Generate Conformity Report` → User Task `Approve Conformity Assessment` → Gateway `Approved?` → `MarkAsConformityAssessedDelegate` o `DocumentRejectionDelegate`.
- **User Forms:** `InitiateConformityAssessmentViewModel`, `ReviewQmsGapsViewModel`, `CompleteDocumentationViewModel`, `FinalReviewViewModel`, `ApproveConformityAssessmentViewModel`.
- **Delegates:** `VerifyQmsComplianceDelegate`, `ReviewTechnicalDocumentationDelegate`, `VerifyProcessConsistencyDelegate`, `GenerateConformityReportDelegate`, `MarkAsConformityAssessedDelegate`, `DocumentRejectionDelegate`.
- **Variables:** `projectId`, `assessmentType`, `qmsCompliant`, `qmsScore`, `qmsGaps`, `docComplete`, `docScore`, `processConsistent`, `overallScore`, `approved`.

### 1.9 `fria-process.bpmn20.xml`

- **Propósito:** Fundamental Rights Impact Assessment (Art. 27).
- **Flujo:** Start → User Task `FRIA Wizard` → Service Task `Generate FRIA Document` → Gateway (score >=0.9?) → User Task `Complete Missing Elements` (loop) → Service Task `Analyze Fundamental Rights Impact` → Gateway `High Impact?` → User Task `Enhanced Review` + sub-decisions → User Task `Deployer Approval` → Gateway `Proceed?` → Parallel Gateway (Notificar autoridad, Registrar FRIA, Actualizar Proyecto) → Join → End; o Service Task `Cancel Deployment`.
- **Delegates:** `GenerateFriaDocumentDelegate`, `AnalyzeFundamentalRightsDelegate`, `NotifyAuthorityFriaDelegate`, `RegisterFriaDelegate`, `UpdateProjectFriaStatusDelegate`.
- **Variables:** `friaId`, `projectId`, `friaDocumentUrl`, `complianceScore`, `art27Compliant`, `charterArticlesAffected`, `impactSeverity`, `notificationId`.

### 1.10 `incident-reporting-process.bpmn20.xml`

- **Propósito:** Gestión integral de incidentes según Art. 20/62/73.
- **Flujo resumido:** Start (manual o señal) → Service `ClassifyIncidentSeverityDelegate` → Gateway `Serious Incident?` → Parallel (Notificar Autoridad, Notificar Usuarios, User Task `Document Incident Details`) → User Task `Root Cause Analysis` → Service Task `ExecuteRCADelegate` → User Task `Define Corrective Actions` → Service `CreateCorrectiveActionTasksDelegate` → Parallel (Multi-instance `Execute Corrective Actions`, Timer follow-up) → Join → User Task `Verify Resolution` → Gateway `Resolved?` → Service `CloseIncidentDelegate` o `EscalateIncidentDelegate`.
- **User Forms:** `DocumentIncidentDetailsViewModel`, `RootCauseAnalysisViewModel`, `DefineCorrectiveActionsViewModel`, `VerifyIncidentResolutionViewModel`.
- **Variables:** `incidentId`, `severity`, `isSerious`, `affectedUsersCount`, `rcaReportUrl`, `correctiveActions`, `resolutionStatus`.

### 1.11 `eu-database-registration-process.bpmn20.xml`

- **Propósito:** Registrar sistemas en la base de datos UE (Art. 49).
- **Flujo:** Start (cuando `Project.isHighRisk && conformityAssessed` o evaluación not high-risk) → Service `DetermineRegistrationTypeDelegate` → Gateway (Sección A/B/C/Multiple) → User Task(s) `EuRegistrationFormViewModel` / `FixValidationErrorsViewModel` → Service `ValidateRegistrationDataDelegate` → Gateway (válido?) → Service `GenerateRegistrationPackageDelegate` → User Task `ReviewRegistrationPackageViewModel` → Gateway `Submit?` → Service `SubmitToEuDatabaseDelegate` → Gateway `Submission Success?` → `UpdateProjectRegistrationDelegate` o `LogRegistrationErrorDelegate` + User Task `ManualResolutionViewModel`.
- **Variables:** `projectId`, `registrationType`, `validationErrors`, `registrationPackageUrl`, `submissionSuccess`, `euRegistrationId`.

### 1.12 `ai-component-onboarding-v1.bpmn`

- **Propósito:** Automatizar onboarding de componentes AI‑OS desde la solicitud inicial hasta la activación.
- **Flujo:** Start → `ValidateComponentMetadataDelegate` → Gateway (metadata válida) → User Task `AioComponentMetadataFixViewModel` (loop) → `ComponentSecurityComplianceDelegate` (**@todo implementar clientes `leka-bias-detection-service` y `leka-llm-evaluation`)** → Parallel Gateway (registrar ficha, immutable log, adjuntar políticas via `AttachDefaultPoliciesDelegate`) → Business Rule `ai-component-onboarding.drl` → Gateway (APPROVE/CONDITIONAL/REJECT) → `ActivateComponentDelegate` / User Task `AioComponentConditionalApprovalViewModel` / `RejectComponentDelegate`.
- **Variables:** `componentId`, `workspaceId`, `componentType`, `securityScore`, `complianceScore`, `policiesAttached`, `decision`.

### 1.13 `ai-marketplace-publish-v1.bpmn`

- **Propósito:** Publicar componentes en el marketplace controlando evidencias, scoring y revisiones legales.
- **Flujo:** Start → `VerifyCertificationArtifactsDelegate` → Gateway (artifacts completos) → User Task `MarketplaceEvidenceUploadViewModel` → `MarketplaceScoringDelegate` → Business Rule `marketplace-scoring.drl` → Gateway (`PUBLIC`, `RESTRICTED`, `DENIED`) → `PublishComponentMarketplaceDelegate` / User Task `MarketplaceLegalReviewViewModel` / `NotifyMarketplaceDenialDelegate`.
- **Variables:** `componentId`, `marketplaceTier`, `certificationSet`, `marketplaceScore`, `publicationDecision`.

### 1.14 `external-model-approval-v1.bpmn`

- **Propósito:** Evaluar y aprobar modelos externos integrados en AI‑OS.
- **Flujo:** Start → `ExternalModelRiskClassificationDelegate` (**@todo implementar cliente `leka-risk-evaluator`**) → Gateway (High risk) → `LaunchFriaForExternalModelDelegate` (si aplica) → `ValidateExternalDocumentationDelegate` → Business Rule `external-model-approval.drl` → Gateway (`APPROVE`, `CONDITIONAL`, `REJECT`) → `RegisterExternalModelDelegate` / User Task `ExternalModelHitlReviewViewModel` / `NotifyExternalVendorDelegate`.
- **Variables:** `externalModelId`, `vendorName`, `riskLevel`, `documentationScore`, `externalDecision`.

---

## 2. Procesos de riesgo, monitoreo y evaluación

### 2.1 `bias-detection-v1.bpmn`

- **Propósito:** Ejecutar detección de sesgo periódica sobre modelos/datasets.
- **Flujo:** Timer/Event Start → `PrepareDemographicDataDelegate` → `ExecuteBiasDetectionDelegate` (llama `leka-bias-detection-service`) → Business Rule `bias-detection-rules.drl` → Gateway `AUTO_NOTIFY | ESCALATE | IGNORE` → Service tasks `NotifyBiasAnalysisDelegate`, `RejectBiasedModelDelegate`, `AutoApproveNoBiasDelegate`.
- **Variables:** `modelId`, `datasetId`, `biasMetrics`, `decision`.

### 2.2 `drift-detection-v1.bpmn`

- **Propósito:** Detectar concept/data drift.
- **Flujo:** Start (`modelId`, `baselineTimestamp`) → `LoadBaselineMetricsDelegate` → `CaptureCurrentMetricsDelegate` → `AnalyzeDriftDelegate` → Rule Task `drift-detection-rules.drl` → Gateway `LOW | MEDIUM | CRITICAL` → `CreateDriftAlertDelegate` + `NotifyDriftDelegate`.
- **Variables:** `ksPvalue`, `populationStabilityIndex`, `driftLevel`.

### 2.3 `performance-degradation-v1.bpmn`

- **Propósito:** Detectar degradación de performance y disparar retraining.
- **Flujo:** Start → `LoadBaselinePerformanceDelegate` → `CheckCurrentPerformanceDelegate` → `ComparePerformanceDelegate` → Rule Task `performance-degradation-rules.drl` → Gateway `DEGRADATION | OK` → `TriggerRetrainingDelegate` / `ArchiveEvaluationDelegate`.
- **Variables:** `baselineMetrics`, `currentMetrics`, `pValue`, `degradationDetected`.

### 2.4 `alert-response-v1.bpmn`

- **Prop:** Clasificar alertas de monitoreo/aplicaciones.
- **Flujo:** Start (`alertId`, `alertType`, `severity`) → `ClassifyAlertDelegate` (Drools `alert-routing-rules.drl`) → Gateway `LOW/MEDIUM/HIGH/CRITICAL` → Service tasks `LogLowAlertDelegate`, `NotifyHighAlertDelegate`, `AutoEscalateCriticalDelegate` → `UpdateAlertStatusDelegate`.

### 2.5 `incident-response-rca-v1.bpmn`

- **Propósito:** Analizar incidentes detectados (post `incident-reporting` o alertas).
- **Flujo:** Start (`incidentId`) → `AIImpactAssessmentDelegate` → `GenerateMitigationRecommendationsDelegate` → Gateway (Mitigations approved?) → End / escalate.

### 2.6 `dataset-quality-v1.bpmn`

- **Propósito:** Verificar calidad de datasets (Art. 10).
- **Flujo:** Start → `DatasetQualityCheckDelegate` (llama `leka-bias-detection /evaluate-data-quality`) → Gateway `PASS | FAIL` → `PublishDatasetBadgeDelegate` o `CreateDataIssueDelegate`.
- **Variables:** `datasetId`, `qualityScore`, `issues`.
- **Estado:** Documentado en manual; falta detalle de formularios → **TODO** generar prompt estilo `PROMPTS_04`.

### 2.7 `model-evaluation-v1.bpmn`

- **Propósito:** Evaluar modelos (tests técnicos).
- **Flujo:** Start → `ModelEvaluationDelegate` → `StoreEvaluationDelegate` → Rule Task `model-evaluation-rules.drl` → Gateway `APPROVED | REJECTED`.

### 2.8 `llm-evaluation-v1.bpmn`

- **Propósito:** Validar LLMs (robustez, jailbreak, toxicity).
- **Delegates:** `PrepareLlmEvaluationDelegate`, `ExecuteLlmEvaluationDelegate`, `AutoApproveLlmEvaluationDelegate`, `ManualApproveLlmEvaluationDelegate`, `RejectLlmEvaluationDelegate`, `SaveLlmEvaluationResultsDelegate`, `NotifyLlmEvaluationDelegate`.
- **Variables:** `llmId`, `testSuiteId`, `toxicityScore`, `promptInjectionScore`, `decision`.

### 2.9 `rag-evaluation-v1.bpmn`

- **Propósito:** Evaluar sistemas RAG.
- **Delegates:** `RagEvaluationDelegate`, `StoreRagEvaluationDelegate`, `CreateRagAlertDelegate`.
- **Variables:** `ragSystemId`, `groundingScore`, `latencyMs`.

### 2.10 `model-retraining-orchestration-v1.bpmn`

- **Propósito:** Orquestar reentrenamientos tras degradación/drift.
- **Flujo esperado:** Start (`modelId`) → `TriggerRetrainingDelegate` → `MonitorRetrainingJobDelegate` → Gateway (Job success?) → `ValidateRetrainedModelDelegate` → `UpdateModelRegistryDelegate` → End; en fallo → `RollbackModelDelegate`.
- **Estado:** Documentado en manual pero sin prompt detallado; requiere confirmación de delegates (marcados con **TODO**).

### 2.11 `deployment-automation-v1.bpmn`

- **Propósito:** Automatizar despliegues con autoscaling/rollback.
- **Delegates:** `CheckAutoScalingDelegate`, `DeployModelDelegate`, `AutoScaleDeploymentDelegate`, `RollbackModelDelegate`.
- **Notas:** Debe integrarse con `AioDeploymentService` (`PROMPT_002`).

### 2.12 `compliance-monitoring-v1.bpmn` (extendido)

- **Propósito:** Monitoreo continuo de cumplimiento (Art. 19, 15, 72).
- **Nuevas tareas (Prompt A.1):** `verifyLogIntegrity` (`VerifyLogIntegrityDelegate`), `logIntegrityGateway`, `createCriticalTamperingAlert`, `runAdversarialEvaluation`, `runFeedbackLoopAnalysis`, `checkPostMarketMetrics`. Todas corren en paralelo tras `executeComplianceCheck`.
- **Variables añadidas:** `logIntegrityValid`, `tamperingDetected`, `corruptedLogsCount`, `adversarialRobustnessScore`, `vulnerabilitiesFound`, `feedbackLoopDetected`, `biasAmplificationFactor`, `driftDetected`, `performanceDegradation`, `userSatisfactionDrop`.

### 2.13 `ethics-review-v1.bpmn`

- **Propósito:** Garantizar evaluación ética independiente.
- **Delegates:** `AIEthicalReviewDelegate`, `SaveEthicsEvidenceDelegate`, `RejectEthicsDelegate`.
- **User Task:** `EthicsReviewForm.zul` (**confirmar**).

### 2.14 `risk-assessment-v1.bpmn`

- **Propósito:** Ejecutar evaluación de riesgos (Art. 9).
- **Flujo:** Start → `RiskAssessmentDelegate` → `ScheduleReviewDelegate` → User Task `Accept Residual Risk` → End.
- **Reglas:** `risk-mitigation-rules.drl`.

### 2.15 `ai-runtime-health-v1.bpmn`

- **Propósito:** Supervisar salud runtime y activar alertas/tickets en base a KPIs.
- **Flujo:** Start (timer/alert) → `AioTelemetryCollectorDelegate` (**@todo client aio-telemetry-service**) → `RuntimeKpiEvaluatorDelegate` → Parallel Gateway (Business Rule `runtime-health.drl`, `UpdateRuntimeDashboardDelegate`) → Join → Gateway (`GREEN`, `YELLOW`, `RED`) → End / `CreateRuntimeWarningTicketDelegate` / `RuntimeIncidentTriggerDelegate`.
- **Variables:** `runtimeId`, `snapshotId`, `latencyMs`, `errorRate`, `healthState`.

### 2.16 `ai-policy-review-v1.bpmn`

- **Propósito:** Revisar bindings de políticas AI‑OS y aplicar remediaciones/suspensiones.
- **Flujo:** Start → `LoadPoliciesDelegate` → `EvaluatePolicyEvidenceDelegate` (**@todo client aio-policy-service**) → Parallel (Business Rule `policy-review.drl`, `UpdatePolicyDashboardDelegate`) → Join → Gateway (`COMPLIANT`, `HITL`, `NON_COMPLIANT`) → `LogPolicyComplianceDelegate` / User Task `PolicyCommitteeReviewViewModel` / `PolicyRemediationDelegate` → `TriggerPolicySuspensionDelegate`.
- **Variables:** `policyBindingId`, `evidenceSet`, `complianceScore`, `policyDecision`, `remediationPlan`.

---

## 3. Procesos ISO 42001 / ISO 38507

### 3.1 `iso42001-management-review-v1.bpmn`

- **Propósito:** Revisión trimestral Clause 9.3.
- **Flujo:** Timer (cada 3 meses) → `PreparePerformanceDataDelegate` → `GenerateManagementReviewReportDelegate` → User Task `Schedule Management Review` → User Task `Conduct Management Review` → `RecordManagementDecisionsDelegate` → Parallel (User Task `Assign Action Items`, Service `NotifyStakeholdersDelegate`) → Join → `ArchiveReviewRecordDelegate`.
- **Variables:** `period`, `performanceData`, `managementDecisions`, `actionItems`.

### 3.2 `iso42001-internal-audit-v1.bpmn`

- **Flujo:** Timer (anual) → `GenerateAuditChecklistDelegate` → User Task `Schedule Audit` → User Task `Conduct Audit` → `CalculateComplianceScoreDelegate` → Gateway `Non-conformities?` → `RecordAuditResultsDelegate`.
- **Variables:** `auditId`, `checklist`, `auditFindings`.

### 3.3 `iso42001-corrective-action-v1.bpmn`

- **Flujo:** Start (NC report) → User Task `Assess Non-Conformity` → `AssignResponsibleDelegate` → User Task `Root Cause Analysis` → User Task `Define Corrective Action` → User Task `Approve Corrective Action` → User Task `Implement Corrective Action` → Timer (due date) → User Task `Verify Effectiveness` → Gateway (Effective?) → `UpdateNonConformityStatusDelegate`.

### 3.4 `iso42001-ai-decommissioning-v1.bpmn`

- **Estado:** Solo resumen en `PROMPTS_09`. Se requiere especificar tareas (data retention, archive, audit trail) y delegates → **TODO** crear prompt detallado.

### 3.5 `iso42001-competence-gap-v1.bpmn`

- **Estado:** Resumen (plan de training → ejecutar → verificar). Falta documentación completa → **TODO**.

### 3.6 `iso38507-board-decision-v1.bpmn`

- **Flujo:** Start (proposal) → User Task `Prepare Board Paper` → `CalculateStrategicAlignmentDelegate` → User Task `Board Review` → Gateway (Approve / Reject / Request Info) → User Task `Board Decision` → `RecordBoardDecisionDelegate` → User Task `Implement Decision` → `MonitorImplementationDelegate`.
- **Reglas:** `board-decision-rules.drl`.

---

## 4. Procesos regulatorios adicionales

### 4.1 `conformity-assessment-process.bpmn20.xml`

- (Documentado en sección 1.8; se mantiene referencia cruzada para Annex VI).

### 4.2 `incident-reporting-process.bpmn20.xml`

- (Documentado en sección 1.10).

### 4.3 `eu-database-registration-process.bpmn20.xml`

- (Documentado en sección 1.11).

---

## 5. Procesos pendientes de creación

| Proceso | Fuente | Requerimientos técnicos/responsables |
| --- | --- | --- |
| `iso42001-ai-decommissioning-v1.bpmn` | `PROMPTS_09_BPMN_MULTI_FRAMEWORK.md` | Definir tasks para retirada controlada, archivado y retención (delegates `PlanDecommissionDelegate`, `ArchiveAssetsDelegate`). |
| `iso42001-competence-gap-v1.bpmn` | `PROMPTS_09_BPMN_MULTI_FRAMEWORK.md` | Diseñar flujo training plan → ejecución → verificación, con `CompetencePlanDelegate` y formularios HITL. |

---

## 6. Checklist de mantenimiento

1. **Validación cruzada:** comparar este documento con `docs/compliance/bpmn/BPMN_CATALOG.md` en cada sprint para mantener consistencia.
2. **Integración AI‑OS:** documentar para cada proceso la escritura en `ImmutableLog`, entidades relacionadas (`AioComponent`, `ComplianceAssessment`, etc.).
3. **Reglas Drools:** asegurar que cada Business Rule Task referenciada tiene archivo `.drl` en `rules/` y pruebas unitarias.
4. **Delegates:** confirmar que todos los delegates mencionados existen en `codeflowx.govern.workflow.lib` y se referencian por `camunda:delegateExpression`.
5. **Formularios/User Tasks:** registrar la vista ZUL o formulario utilizado; si está pendiente, marcar **TODO** con responsable.

> **Próximo paso:** Completar los detalles faltantes marcados como **TODO** (especialmente procesos ISO 8.1/7.2 y orquestaciones AI‑OS) y versionar esta guía junto con los BPMN correspondientes.


