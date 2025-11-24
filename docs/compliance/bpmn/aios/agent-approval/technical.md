# Agent Approval v1 – Guía Técnica

## Artefactos
- **BPMN**: `codeflowx.govern.workflow.lib/src/main/resources/processes/aios/agent-approval-v1.bpmn`
- **Delegates principales** (`com.codeflowx.govern.workflow.delegates`):
  - `AIRiskAssessmentDelegate`
  - `AIComplianceCheckDelegate`
  - `AIEthicalReviewDelegate`
  - `CalculateAgentScoreDelegate`
  - `AutoApproveAgentDelegate`
  - `AutoRejectAgentDelegate`
  - `AcceptResidualRiskDelegate` (cuando aplica)
- **Reglas Drools**: `rules/agent/agent-scoring.drl` (sesión `agent-approval-session`)
- **Fact**: `AgentApprovalFact`

## Variables de proceso
- `agentId`, `agentName`, `owner`, `useCase`, `sensitivityLevel`.
- `riskScore`, `complianceScore`, `ethicsScore`, `riskCategory`.
- `decision`, `requiresHumanReview`, `confidenceLevel`, `justification`.
- `immutableLogId`, `humanReviewer`, `approvalArtifacts`.

## Integraciones clave
- **Servicios técnicos**:
  - `RiskEngine` (vía `AIRiskAssessmentDelegate`)
  - `leka-bias-detection-service` y `leka-llm-evaluation` (invocados desde las evaluaciones técnicas)
  - `ImmutableLogService`
  - `AIOS Core API` para registrar el estado final del agente
- **Fuentes de datos**: `AgentRepository`, `PolicyBindingService`, `ConfigService` (para umbrales)

## Flujo técnico
1. `AIRiskAssessmentDelegate` y `AIComplianceCheckDelegate` se ejecutan en paralelo; ambos escriben en `riskScore`, `riskCategory`, `complianceScore`.
2. `AIEthicalReviewDelegate` hace llamadas a servicios externos (pendiente de consolidar cliente) y deja `ethicsScore`.
3. `CalculateAgentScoreDelegate` arma `AgentApprovalFact` y ejecuta Drools (`executeAgentApprovalRules`) para obtener `decision`.
4. Si `decision == HITL_REQUIRED`, se abre `agent-approval-human-override-form.zul`.
5. `AutoApproveAgentDelegate` y `AutoRejectAgentDelegate` actualizan el estado en base de datos y registran el evento.

## Consideraciones pendientes
- Refactorizar el BPMN y los delegates para que consuman el nuevo `AioPolicyEnforcementService`.
- Exponer métricas y logs estructurados (score breakdown, tiempos de cada evaluación).
- Añadir pruebas automatizadas:
  - Test de unidad para `CalculateAgentScoreDelegate` (incluyendo casos de riesgo crítico).
  - Test Flowable que cubra `AUTO_APPROVE`, `HITL_REQUIRED` y `AUTO_REJECT`.


