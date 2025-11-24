# ISO 42001 Competence Gap Closure – Guía Técnica

## Artefactos
- **BPMN**: `processes/audit/iso42001-competence-gap-v1.bpmn`
- **Delegates** (a definir):
  - `AssessCompetenceGapDelegate`
  - `RecommendTrainingPlanDelegate`
  - `UpdateCompetenceGapStatusDelegate`
- **Formularios**: `competence_gap_assessment.zul`, `training_plan_approval.zul`, `competence_verification.zul`.
- **Entidad**: `CompetenceGap` (`CGAGAPS`).

## Variables
- Entrada: `competenceGapId`, `employeeId`, `role`.
- Intermedias:
  - `gapDetails` (map), `trainingPlan`.
  - `responsibleManager`, `dueDate`.
- Salida:
  - `planStatus`, `completionRate`, `verificationScore`.

## Integraciones
- **HR/Learning Service**: catálogo de cursos y asignaciones.
- **NotificationService**: avisos a participantes y managers.
- **ImmutableLog**: registro de creación/cierre.

## Comportamiento
- `AssessCompetenceGapDelegate`: valida datos iniciales, calcula `criticality`.
- `RecommendTrainingPlanDelegate`: consulta catálogo, genera plan (lista de training items con duración, proveedor).
- `UpdateCompetenceGapStatusDelegate`: actualiza `status` (`OPEN`, `IN_PROGRESS`, `CLOSED`).

## Testing
- Unit tests para delegates (mocks de HR service).
- Flowable test: path donde gap se cierra vs. loop por verificación fallida.

## Pendientes
- Definir reglas (Drools) para priorizar planes según criticidad.
- Añadir métricas (tiempo medio de cierre).

