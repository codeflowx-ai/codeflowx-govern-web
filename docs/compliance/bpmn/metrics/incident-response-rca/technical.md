# Incident Response RCA v1 – Guía Técnica

## Artefactos
- **BPMN**: `processes/metrics/incident-response-rca-v1.bpmn`
- **Delegates**:
  - `AIImpactAssessmentDelegate`
  - `GenerateMitigationRecommendationsDelegate`
- **Integración**: tareas humanas (aprobación mitigaciones) y enlace con BPMN `incident-reporting-process`.

## Variables
- Entrada: `incidentId`.
- Salida:
  - `impactAssessment` (Map)
  - `mitigationRecommendations` (List<Map>)
  - `rcaReportUrl`
  - `rcaApproved`

## Integraciones
- **RCA Microservice**: analiza logs/metrics (Python).
- **Document Storage**: guarda reporte PDF/JSON.
- **Incident Service**: actualiza `IncidentReport` con resultados.

## Consideraciones
- `GenerateMitigationRecommendationsDelegate` debe mapear cada recomendación a responsables y prioridad.
- Puede usarse timer/human task para validar recomendaciones antes de cerrar.

## Testing
- Mocks del micro RCA.
- Flowable test para verificar que `incidentId` se propaga y se actualizan variables post-RCA.

## Pendientes
- Añadir delegate para “Approve Mitigations”.
- Integrar con tablero de acciones para seguimiento.

