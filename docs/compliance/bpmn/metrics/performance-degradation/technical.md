# Performance Degradation v1 – Guía Técnica

## Artefactos
- **BPMN**: `processes/metrics/performance-degradation-v1.bpmn`
- **Delegates**:
  - `LoadBaselinePerformanceDelegate`
  - `CheckCurrentPerformanceDelegate`
  - `ComparePerformanceDelegate`
  - `TriggerRetrainingDelegate`
  - `SavePerformanceMetricsDelegate`
- **Reglas**: `rules/performance/performance-degradation-rules.drl`
- **Fact**: `PerformanceDegradationFact`.

## Variables
- `modelId`, `environment`.
- `baselineMetrics` (Map), `currentMetrics`.
- `comparisonResult`, `pValue`.
- `degradationDetected`, `recommendedAction`.

## Integraciones
- **Telemetry/Monitoring APIs** para métricas online.
- **ModelRegistry**: actualizar estado `needsRetraining`.
- **Deployment Automation BPMN**: se puede encadenar tras `TriggerRetraining`.

## Delegates
- `LoadBaselinePerformanceDelegate`: lee snapshot guardado (BD/time-series).
- `CheckCurrentPerformanceDelegate`: recoge métricas recientes.
- `ComparePerformanceDelegate`: calcula p-value y setea `degradationDetected`.
- `TriggerRetrainingDelegate`: dispara `model-retraining-orchestration` (Flowable call activity o REST).
- `SavePerformanceMetricsDelegate`: almacena resultados para auditoría.

## Testing
- Unit tests con datos sintéticos (sin degradación vs. degradación).
- Flowable tests para garantizar que se dispara retraining solo cuando aplica.

## Pendientes
- Agregar thresholds configurables por tipo de modelo.
- Registrar métricas en observabilidad (Prometheus).

