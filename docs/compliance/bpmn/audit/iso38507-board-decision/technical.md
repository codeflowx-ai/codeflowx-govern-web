# ISO 38507 Board Decision Workflow – Guía Técnica

## Artefactos
- **BPMN**: `processes/audit/iso38507-board-decision-v1.bpmn`
- **Delegates**:
  - `CalculateStrategicAlignmentDelegate`
  - `RecordBoardDecisionDelegate`
  - `MonitorImplementationDelegate` (a definir)
- **Forms**:
  - `board_paper_form.zul`
  - `board_decision_form.zul`
  - `implementation_plan_form.zul`

## Variables
- Entrada: `proposalId`, `proposalType`, `owner`.
- Intermedias:
  - `strategicAlignmentScore`
  - `boardComments`
  - `actionItems`
- Salida:
  - `boardDecision`
  - `decisionReportUrl`
  - `implementationStatus`

## Integraciones
- **StrategyService**: datos para alignment score.
- **BoardDecisionService**: persiste en `BDCBOARDDECISION`, `BRDBOARDREPORT`.
- **NotificationService**: envia actas a stakeholders.
- **ImmutableLog**: registro auditable.

## Comportamiento delegates
- `CalculateStrategicAlignmentDelegate`: utiliza KPIs (AI strategy, compliance, ROI).
- `RecordBoardDecisionDelegate`: guarda acta, miembros presentes, resultado de votación.
- `MonitorImplementationDelegate`: consulta KPIs post decisión y actualiza `implementationStatus`.

## Testing
- Unit tests para delegates (mocks de Strategy/Board services).
- Flowable test: escenarios (aprobado, rechazo, request more info).

