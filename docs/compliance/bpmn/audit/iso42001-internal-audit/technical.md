# ISO 42001 Internal Audit – Guía Técnica

## Artefactos
- **BPMN**: `processes/audit/iso42001-internal-audit-v1.bpmn`
- **Delegates**:
  - `GenerateAuditChecklistDelegate`
  - `CalculateComplianceScoreDelegate`
  - `RecordAuditResultsDelegate`
- **Forms**: `audit_scheduling_form.zul`, `audit_execution_form.zul`.

## Variables
- `auditId`, `auditScope`, `auditPeriod`.
- `auditChecklist` (collection de preguntas).
- `auditFindings` (List<AuditFindingDTO>).
- `complianceScore`.

## Integraciones
- **ChecklistService**: genera preguntas basadas en cláusulas.
- **AuditResultService**: guarda resultados y enlaza a NC.
- **NotificationService**: envía recordatorios a auditores y dueños de procesos.

## Detalle delegates
- `GenerateAuditChecklistDelegate`: usa catálogo ISO para generar `auditChecklist`.
- `CalculateComplianceScoreDelegate`: aplica ponderaciones; puede invocar Drools si es necesario.
- `RecordAuditResultsDelegate`: persiste en BD y dispara BPMN `iso42001-corrective-action` cuando existen hallazgos.

## Testing
- Unit tests para checklist y scoring.
- Flowable test para verificar ramificaciones (con y sin NCs).


