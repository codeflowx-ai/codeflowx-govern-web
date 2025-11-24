# Ethics Review v1 – Guía Técnica

## Artefactos
- **BPMN**: `processes/compliance/ethics-review-v1.bpmn`
- **Delegates**:
  - `AIEthicalReviewDelegate`
  - `SaveEthicsEvidenceDelegate`
  - `RejectEthicsDelegate`
- **Form**: `ethics_review_form.zul` (o equivalente en UI actual).
- **Entidad**: `EthicsReview` (tabla `ETCREVIEWS`).

## Variables
- Entrada: `componentId`, `componentType`, `requester`.
- Salida:
  - `ethicsScore`
  - `ethicsFindings`
  - `ethicsEvidenceUrl`
  - `ethicsDecision`
  - `ethicsComments`

## Lógica
- `AIEthicalReviewDelegate`: orquesta evaluaciones (fairness, transparency, human oversight). Puede llamar:
  - `leka-bias-detection` (fairness metrics)
  - `leka-ai-interpreter` (explicabilidad)
  - `leka-compliance-checks` (policy alignment)
- `SaveEthicsEvidenceDelegate`: sube resultado a storage y crea registro `EthicsReview`.
- `RejectEthicsDelegate`: marca decisión, genera plan de acción y envía notificaciones.

## Integraciones
- `ImmutableLog` para evidencias.
- `NotificationService` para informar a dueños ante rechazo.
- `GovernComponentService` para actualizar estado.

## Testing
- Unit tests: validar parseo de metrics y persistencia en `EthicsReview`.
- Flowable tests: rutas APPROVED vs REJECTED.

## Pendientes
- Incorporar BusinessRuleTask para evaluar `ethicsScore` automáticamente.
- Añadir enlace a AI Policy Review cuando se rechaza (trigger remediation).

