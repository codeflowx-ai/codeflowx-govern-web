# AI Policy Review – Guía Técnica

## Artefactos
- **BPMN**: `processes/aios/ai-policy-review-v1.bpmn`
- **Reglas**: `rules/aios/policy/ai-policy-review.drl`
- **Fact**: `PolicyReviewFact`

## Delegates clave
| Delegate | Descripción |
| --- | --- |
| `loadPoliciesDelegate` | Inicializa `activePolicies`. |
| `evaluatePolicyEvidenceDelegate` | Obtiene KPIs de cumplimiento (`// @todo` cliente `aio-policy-service`). |
| `updatePolicyDashboardDelegate` | Marca actualización de dashboard. |
| `policyReviewRulesDelegate` | Ejecuta Drools y setea `policyDecision`. |
| `logPolicyComplianceDelegate` | Persiste resultados `COMPLIANT`. |
| `policyRemediationDelegate` | Genera `remediationPlan`. |
| `triggerPolicySuspensionDelegate` | Activa suspensión ante fallos críticos. |

## Variables
- In: `policyBindingId`, `policyComplianceScore`, `openViolations`, `daysSinceLastAudit`, `criticalFailure`.
- Out: `policyDecision`, `requiresRemediation`, `policyJustification`, `policySuspensionTriggered`.

## Reglas
1. **Compliant**: score ≥90, sin violaciones, auditoría reciente.
2. **HITL**: score 75‑89 o violaciones leves.
3. **Non-Compliant**: resto → obliga remediación y posible suspensión.

## Integraciones pendientes
- Servicio de políticas (`aio-policy-service`).
- Notificaciones al comité (correo/chatops) cuando se crea tarea HITL.

## Consideraciones
- Sesión Drools `ai-policy-review-session`.
- Para nuevas métricas (ej. `driftAlerts`), ampliar `PolicyReviewFact` y reglas asociadas.

