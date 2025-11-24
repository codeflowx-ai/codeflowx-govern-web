# AI Policy Review – Guía Funcional

## Objetivo
Revisar periódicamente las políticas aplicadas a componentes AI‑OS, evaluar evidencia runtime y ejecutar acciones de remediación o suspensión según el resultado.

## Roles
- **Policy Committee**: atiende decisiones `HITL`.
- **AIOS Admin**: ejecuta acciones correctivas y actualiza dashboard.
- **Compliance Lead**: supervisa historial y reportes.

## Flujo funcional
1. **Disparo**: scheduler mensual o evento de cambio en `AioPolicyBinding`.
2. **Carga de políticas activas** y evidencias runtime (logs, auditorías, violaciones).
3. **Evaluaciones en paralelo**:
   - Regla de cumplimiento (Drools) genera `COMPLIANT`, `HITL` o `NON_COMPLIANT`.
   - Actualización del dashboard de políticas.
4. **Ruta según decisión**:
   - `COMPLIANT`: se registra en `ImmutableLog`.
   - `HITL`: se abre tarea para el comité (puede aprobar, pedir cambios o convertir a remediación).
   - `NON_COMPLIANT`: se genera plan de remediación y opcionalmente se dispara suspensión.
5. **Cierre**: variables `policyDecision`, `policyJustification`, `remediationPlan` quedan disponibles para reporting.

## Entradas
- `policyBindingId`, `activePolicies`, `policyComplianceScore`, `openViolations`, `daysSinceLastAudit`.

## Salidas
- `policyDecision`, `requiresRemediation`, `policyJustification`.
- `policyDashboardUpdated`, `policySuspensionTriggered`.

## SLA
- Tareas HITL: resolución en ≤ 5 días.
- Remediación: plan debe definirse con deadline <= 14 días según Art. 9 EU AI Act.

