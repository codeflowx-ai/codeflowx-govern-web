# Compliance Monitoring v1 – Guía Técnica

## Artefactos
- **BPMN**: `processes/compliance/compliance-monitoring-v1.bpmn`
- **Delegates nuevos** (módulo `codeflowx.govern.workflow.lib`):
  - `VerifyLogIntegrityDelegate`
  - `CreateTamperingAlertDelegate`
  - `RunAdversarialEvaluationDelegate`
  - `RunFeedbackLoopAnalysisDelegate`
  - `CheckPostMarketMetricsDelegate`
- **Reglas**: usa Drools existentes para clasificación de alertas (`rules/alert/**`).

## Diseño
1. Se ejecuta `executeComplianceCheck` legacy.
2. Gateway paralelo dispara cuatro service tasks nuevos.
3. Las variables se consolidan antes de `nonComplianceGateway`.
4. `CreateTamperingAlertDelegate` conecta con gestor de incidentes y marca `alertSeverity = CRITICAL`.

## Detalle de delegates
| Delegate | Resumen | Integraciones |
| --- | --- | --- |
| `VerifyLogIntegrityDelegate` | Calcula hashes y compara contra `ImmutableLog`/Timescale. | `// @todo client immutable-log-service`. |
| `RunAdversarialEvaluationDelegate` | Orquesta ataques FGSM / prompt injection. | `leka-llm-evaluation`, `leka-adversarial-service` (**pendiente**). |
| `RunFeedbackLoopAnalysisDelegate` | Analiza métricas de retroalimentación y bias amplification. | `leka-bias-detection-service`. |
| `CheckPostMarketMetricsDelegate` | Consulta telemetría (drift, degradación, UX). | `aio-telemetry-service`. |
| `CreateTamperingAlertDelegate` | Genera alerta crítica y enlaza `incident-reporting-process`. | Sistema de alertas + Flowable signal. |

## Variables
- `logIntegrityValid`, `tamperingDetected`, `corruptedLogsCount`.
- `adversarialRobustnessScore`, `vulnerabilitiesFound`.
- `feedbackLoopDetected`, `biasAmplificationFactor`.
- `driftDetected`, `performanceDegradation`, `userSatisfactionDrop`.

## Dependencias
- `ImmutableLog` para auditoría.
- Microservicios `leka-*` (bias, llm eval) – clientes aún marcados con `@todo`.
- `aio-telemetry-service`.

## Testing
- Requiere `@SpringBootTest` que mockee microservicios y verifique:
  - Flujos paralelos se completan.
  - Variables esperadas presentes.
  - Signal de tampering dispara Flowable Event.

## Pendientes
- Implementar clientes HTTP/gRPC reales.
- Añadir métricas Prometheus (latencia de cada delegate).
- Documentar mapping a `COMPLIANCE_LOGS` en BD.

