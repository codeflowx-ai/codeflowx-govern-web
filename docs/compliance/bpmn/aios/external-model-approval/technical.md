# External Model Approval – Guía Técnica

## Artefactos
- **BPMN**: `processes/aios/external-model-approval-v1.bpmn`
- **Reglas**: `rules/aios/external/external-model-approval.drl`
- **Fact**: `ExternalModelApprovalFact`

## Delegates
| Delegate | Función |
| --- | --- |
| `externalModelRiskClassificationDelegate` | Obtiene `riskLevel`. `// @todo` cliente `leka-risk-evaluator`. |
| `launchFriaForExternalModelDelegate` | Marca `friaRequired`, dispara FRIA BPMN. |
| `validateExternalDocumentationDelegate` | Calcula `documentationScore`. |
| `externalModelApprovalRulesDelegate` | Ejecuta reglas y devuelve `externalDecision`. |
| `registerExternalModelDelegate` | Actualiza catálogo al aprobar. |
| `notifyExternalVendorDelegate` | `// @todo` enviar correo o webhook a proveedor. |

## Variables
- In: `externalModelId`, `vendorName`, `riskLevel`, `documentationScore`, `friaRequired`, `friaCompleted`.
- Out: `externalDecision`, `externalDecisionJustification`, `externalModelStatus`, `externalModelRegisteredAt`.

## Reglas
1. **Approve**: riesgo distinto de crítico + score ≥85 + FRIA (si requerida) completada.
2. **Conditional**: score ≥70 → requiere revisión humana.
3. **Reject**: resto.

## Integraciones pendientes
- `leka-risk-evaluator` (API) para scoring de riesgo.
- Webhook con `fria-process` (ya se lanza via delegate pero se puede reforzar con eventos).
- Notificación al proveedor (email/API).

## Notas técnicas
- Sesión Drools: `external-model-approval-session`.
- El delegate de FRIA puede ampliarse para esperar respuesta asincrónica del BPMN FRIA antes de continuar.

