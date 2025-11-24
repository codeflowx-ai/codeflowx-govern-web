# ISO 42001 Management Review – Guía Técnica

## Artefactos
- **BPMN**: `processes/audit/iso42001-management-review-v1.bpmn`
- **Delegates**:
  - `PreparePerformanceDataDelegate`
  - `GenerateManagementReviewReportDelegate`
  - `RecordManagementDecisionsDelegate`
  - `NotifyStakeholdersDelegate`
  - `ArchiveReviewRecordDelegate`
- **ViewModels**: `schedule_review_meeting.zul`, `management_review_form.zul`.

## Variables
- `period`, `periodStart`, `periodEnd`.
- `performanceData` (Map).
- `reportUrl`, `reportPath`.
- `managementDecisions`, `actionItems`.
- `meetingDate`, `attendees`.

## Integraciones
- **AIMSPerformanceBusinessService**: cálculo y persistencia de datos.
- **Report generator** (iText/PDF) para `GenerateManagementReviewReportDelegate`.
- **NotificationService**: alertar stakeholders.
- **ImmutableLog**: guardar decisiones clave.

## Consideraciones
- Timer configurado para ejecutarse cada trimestre.
- `RecordManagementDecisionsDelegate` asegura que se crean acciones en tabla separada si es necesario.
- `ArchiveReviewRecordDelegate` mueve reportes a storage y marca `complianceArchiveUpdated`.

## Testing
- Mock `AIMSPerformanceBusinessService` para validar cálculo de periodos.
- Verificar que los caminos paralelos se completan antes de archivar.


