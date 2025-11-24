# Risk Assessment v1 – Guía Técnica

## Artefactos
- **BPMN**: `processes/compliance/risk-assessment-v1.bpmn`
- **Delegates**:
  - `RiskAssessmentDelegate`
  - `ScheduleReviewDelegate`
  - `AcceptResidualRiskDelegate`
- **Entidades**: `RiskAssessment`, `RiskRegister`, `ImmutableLog`.

## Variables
- `componentId`, `assessmentId`.
- `riskScore` (0‑100), `riskCategory` (`LOW`, `MEDIUM`, `HIGH`, `CRITICAL`).
- `recommendedMitigations` (List<String>).
- `reviewDate`, `reviewOwner`.
- `residualRiskAccepted`, `acceptedBy`, `acceptedAt`.

## Lógica
- `RiskAssessmentDelegate`: 
  - Recolecta métricas operativas (incidentes, drift, compliance).
  - Llama servicios internos (`RiskService`) para puntuar.
  - Asigna `riskCategory` usando thresholds.
- `ScheduleReviewDelegate`: 
  - Calcula fecha objetivo (ej. ahora + 5 días).
  - Crea invitación/recordatorio (puede integrarse con calendar API).
- `AcceptResidualRiskDelegate`: 
  - Actualiza tabla `RiskRegister`.
  - Set `requiresMitigation` y `residualRiskAccepted`.

## Integraciones
- `RiskService` (dominio `nocode.service`).
- `NotificationService` para agendar review.
- `ImmutableLogService` para registrar decisión.

## Testing
- Unit tests para `RiskAssessmentDelegate` (scores y categorías).
- Test BPMN con Flowable: rutas “Accepted” vs “Rejected”.

## Pendientes
- Añadir BusinessRuleTask opcional con Drools para evaluar combos de métricas.
- Automatizar export de resultados hacia dashboards de riesgos.

