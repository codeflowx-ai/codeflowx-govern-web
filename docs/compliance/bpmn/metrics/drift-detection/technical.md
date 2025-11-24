# Drift Detection v1 – Guía Técnica

## Artefactos
- **BPMN**: `processes/metrics/drift-detection-v1.bpmn`
- **Delegates**:
  - `LoadBaselineMetricsDelegate`
  - `CaptureCurrentMetricsDelegate`
  - `AnalyzeDriftDelegate`
  - `CreateDriftAlertDelegate`
  - `NotifyDriftDelegate`
- **Reglas**: `rules/drift/drift-detection-rules.drl`
- **Fact**: `DriftDetectionFact`

## Variables
- Entrada: `modelId`, `baselineTimestamp`.
- Salida:
  - `baselineMetrics`, `currentMetrics`
  - `driftIndicators` (PSI, KS, etc.)
  - `driftLevel`
  - `alertId`

## Integraciones
- **Telemetry/Data Lake**: obtiene métricas actuales.
- **ModelRegistry**: actualiza estado `needsRetraining`.
- **NotificationService**: envía alertas condicionales.

## Detalle delegates
- `LoadBaselineMetricsDelegate`: consulta base de datos/time-series para snapshot histórico.
- `CaptureCurrentMetricsDelegate`: recolecta métricas recientes.
- `AnalyzeDriftDelegate`: calcula indicadores y llena `DriftDetectionFact`.
- `CreateDriftAlertDelegate`: crea alerta/ticket cuando `driftLevel != LOW`.

## Testing
- Unit tests con datos sintéticos para cada nivel.
- Flowable test para verificar gateway y alertas generadas.

## Pendientes
- Integrar con `model-retraining-orchestration` para lanzar retraining automático.

