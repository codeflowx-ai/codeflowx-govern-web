# AI Runtime Health – Guía Técnica

## Artefactos
- **BPMN**: `processes/aios/ai-runtime-health-v1.bpmn`
- **Reglas**: `rules/aios/runtime/ai-runtime-health.drl`
- **Fact**: `RuntimeHealthFact`

## Delegates
| Delegate | Clase | Descripción |
| --- | --- | --- |
| `aioTelemetryCollectorDelegate` | `aios.runtime.AioTelemetryCollectorDelegate` | Obtiene métricas (latencia, error rate, drift). `// @todo` cliente `aio-telemetry-service`. |
| `runtimeKpiEvaluatorDelegate` | Calcula `runtimeCompositeScore`. |
| `runtimeHealthRulesDelegate` | Construye `RuntimeHealthFact`, ejecuta reglas via `DroolsRulesService.executeRuntimeHealthRules`. |
| `updateRuntimeDashboardDelegate` | Marca actualización en dashboards. |
| `createRuntimeWarningTicketDelegate` | Genera ticket para estado `YELLOW`. |
| `runtimeIncidentTriggerDelegate` | Señala incidente crítico (`RED`). |

## Reglas Drools
1. **Runtime Healthy** (`GREEN`): KPIs dentro de umbrales.
2. **Runtime Warning** (`YELLOW`): valores intermedios (latencia 200‑399ms, error rate 1‑5%, drift 0.2‑0.39).
3. **Runtime Critical** (`RED`): fallback para el resto.

## Variables relevantes
- Entrada: `runtimeId`, `latencyMs`, `errorRate`, `driftIndicator`, `complianceScore`.
- Salida: `healthState`, `healthJustification`, `runtimeWarningTicketId`, `runtimeIncidentTriggered`.

## Integraciones pendientes
- `aio-telemetry-service` (REST/gRPC) para obtener métricas reales.
- Webhook con sistema de tickets (ServiceNow/Jira) para registrar `YELLOW`.
- Enlace con BPMN `incident-reporting-process` vía evento/message.

## Consideraciones
- Asegurar que `DroolsRulesService` cuenta con sesión `ai-runtime-health-session`.
- Se recomienda registrar snapshot en base de datos para auditoría (Art. 72 EU AI Act).

