# ISO 42001 Corrective Action Workflow – Guía Técnica

## Artefactos
- **BPMN**: `processes/audit/iso42001-corrective-action-v1.bpmn`
- **Delegates**:
  - `AssignResponsibleDelegate`
  - `UpdateNonConformityStatusDelegate`
- **Forms**:
  - `root_cause_analysis_form.zul`
  - `corrective_action_form.zul`
  - `effectiveness_verification_form.zul`

## Variables
- Entrada: `nonConformityId`, `reportedBy`, `severity`, `clause`.
- Intermedias:
  - `responsibleUser`, `dueDate`.
  - `rootCauseAnalysis`.
  - `correctiveActionPlan`.
- Salida:
  - `ncStatus`, `verificationResult`, `closureEvidence`.

## Integraciones
- **NonConformityService**: CRUD sobre `ANCNONCONFORMITY`.
- **TaskService/Jira**: opcional para asignar tareas a responsables.
- **NotificationService**: recordatorios de due date.
- **ImmutableLog**: registro de cada cambio de estado.

## Detalle delegates
- `AssignResponsibleDelegate`: obtiene responsable según cláusula/módulo (map configurable) y actualiza NC.
- `UpdateNonConformityStatusDelegate`: marca `status=OPEN/IN_PROGRESS/CLOSED`, persiste fecha y comentarios.

## Eventos
- Timer intermedio ligado a `dueDate` → dispara recordatorio y, si expira, alerta a management.

## Testing
- Unit tests para `AssignResponsibleDelegate` (mapping correcto).
- Flowable test: escenarios de aprobación, loop por verificación fallida, cierre exitoso.
# ISO 42001 Corrective Action – Guía Técnica

## Artefactos
- **BPMN**: `processes/audit/iso42001-corrective-action-v1.bpmn`
- **Delegates**:
  - `AssignResponsibleDelegate`
  - `UpdateNonConformityStatusDelegate`
- **Forms**: `root_cause_analysis_form.zul`, `corrective_action_form.zul`, `effectiveness_verification_form.zul`.

## Variables
- `nonConformityId`, `severity`, `isoClause`.
- `assignedUser`, `actionPlan`, `dueDate`.
- `implementationStatus`, `effectiveness`.

## Integraciones
- **NonConformityService** (tabla `ANCNONCONFORMITY`).
- **Task Service / Ticketing** para acciones.
- **NotificationService** para recordatorios (Timer).

## Lógica
- `AssignResponsibleDelegate`: consulta catálogo (cláusula → equipo) y set `assignedUser`.
- `UpdateNonConformityStatusDelegate`: actualiza status (`OPEN`, `IN_PROGRESS`, `CLOSED`, `REOPENED`).
- Timer intermedio dispara recordatorios si `implementationStatus != COMPLETED`.

## Testing
- Unit tests con mocks del servicio NC.
- Flowable test: verificar loop cuando `effectiveness=false`.

