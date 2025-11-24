# Model Approval v1 – Guía Técnica

## Artefactos
- **BPMN**: `processes/aios/model-approval-v1.bpmn`
- **Delegates**:
  - `ModelValidationDelegate`
  - `BiasDetectionDelegate`
  - `ComplianceCheckDelegate`
  - `ModelEvaluationDelegate` / `StoreEvaluationDelegate`
  - `MarkModelProductionDelegate`
  - `MarkModelConditionalDelegate`
  - `RejectModelDelegate`
- **Reglas**: `rules/model/model-approval-scoring.drl` (sesión `model-approval-session`)
- **Fact**: `ModelApprovalFact`
- **Formularios**: `ml_engineer_review.zul`, `governance_review.zul`

## Variables
- `modelId`, `versionId`, `targetEnvironment`.
- `performanceScore`, `biasScore`, `complianceScore`, `driftRisk`.
- `mlEngineerApproval`, `governanceApproval`.
- `finalDecision`, `confidenceLevel`, `requiresMonitoring`, `justification`.
- `immutableLogId`, `deploymentTicketId`.

## Flujo técnico
1. Delegados de evaluación escriben en las variables de score.
2. `ModelValidationDelegate` puede orquestar pipelines (e.g. `leka-model-evaluation`).
3. `BiasDetectionDelegate`/`ComplianceCheckDelegate` reutilizan servicios existentes (pendiente agregar cliente único).
4. `Calculate` + Drools:
   - `ModelApprovalFact` se crea en `ModelEvaluationDelegate` (o `CalculateModelScoreDelegate` si se separa).
   - `DroolsRulesService.executeModelApprovalRules` determina `finalDecision`.
5. Dependiendo del resultado:
   - `MarkModelProductionDelegate` actualiza `ModelRegistry` y dispara `deployment-automation`.
   - `MarkModelConditionalDelegate` marca `requiresMonitoring=true` y registra condiciones.
   - `RejectModelDelegate` cierra la solicitud.

## Integraciones
- `ModelRegistry` (persistencia).
- `DeploymentAutomation` BPMN (call activity o REST).
- `ImmutableLogService`.
- `NotificationService`.

## Pendientes
- Refactorizar para consumir los nuevos servicios de AI‑OS Runtime (telemetría, policy binding).
- Agregar métricas de explainability y fairness específicas por dominio.
- Escribir pruebas unitarias/integrales (Flowable) que cubran los tres escenarios de decisión.


