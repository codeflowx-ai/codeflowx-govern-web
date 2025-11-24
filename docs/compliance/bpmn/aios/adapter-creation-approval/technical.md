# Adapter Creation Approval v1 – Guía Técnica

## Artefactos
- **BPMN**: `processes/aios/adapter-creation-approval-v1.bpmn20.xml`
- **Delegates** (a implementar/reforzar):
  - `ValidateAdapterRequestDelegate`
  - `AdapterRiskScoringDelegate`
  - `SuggestReuseDelegate`
  - `ApproveAdapterDelegate`
  - `RejectAdapterDelegate`
- **Reglas**: `rules/aios/adapter/adapter-approval-rules.drl` (pendiente de crear)
- **Fact**: `AdapterApprovalFact`

## Variables
- `adapterId`, `modelId`, `baseModelType`, `useCase`.
- `existingAdapters` (lista).
- `riskScore`, `riskCategory`, `reuseCandidate`.
- `decision`, `justification`, `requiresHumanReview`.

## Integraciones
- **Adapter Catalog Service**: para consultar/adicionar adapters existentes.
- **RiskService**: calcula `riskScore`, `dataSensitivity`.
- **PolicyBindingService**: asegura que el adapter hereda políticas del modelo base.
- **ImmutableLogService** y `NotificationService`.

## Flujo técnico
1. `ValidateAdapterRequestDelegate` verifica inputs obligatorios y políticas.
2. `AdapterRiskScoringDelegate` calcula puntajes y escribe `riskScore`, `riskCategory`.
3. `SuggestReuseDelegate` revisa catálogo y setea `reuseCandidate`.
4. Reglas Drools (pendientes) determinan `decision`.
5. `ApproveAdapterDelegate` crea el adapter en el catálogo y genera credenciales/pipelines.
6. `RejectAdapterDelegate` documenta el motivo y notifica a los interesados.

## Pendientes
- Implementar `adapter-approval-rules.drl`.
- Añadir pruebas unitarias para los delegates y un Flowable test covering approve/reject/reuse.
- Integrar con `finetuning-approval` para compartir lógica de priorización de adapters.


