# Bias Detection v1 – Guía Técnica

## Artefactos
- **BPMN**: `processes/metrics/bias-detection-v1.bpmn`
- **Delegates**:
  - `PrepareDemographicDataDelegate`
  - `ExecuteBiasDetectionDelegate`
  - `NotifyBiasAnalysisDelegate`
  - `RejectBiasedModelDelegate`
  - `AutoApproveNoBiasDelegate`
- **Reglas**: `rules/bias/bias-detection-rules.drl` (`ruleflow-group: bias-detection`).
- **Fact**: `BiasDetectionFact`.

## Variables
- Entrada: `modelId`, `datasetId`, `baselineMetrics`.
- Salida:
  - `biasMetrics` (Map)
  - `biasDecision`
  - `requiresHumanReview`
  - `notificationIds`

## Integraciones
- **leka-bias-detection-service**: API `/api/tabular/analyze-bias`.
- **NotificationService**: alertas email/chat.
- **ModelRegistry**: etiquetar modelos con estado fairness.

## Delegates
- `PrepareDemographicDataDelegate`: obtiene cohorts y atributos protegidos.
- `ExecuteBiasDetectionDelegate`: invoca micro y persiste resultados.
- `NotifyBiasAnalysisDelegate`: envía resumen + enlace al reporte.
- `RejectBiasedModelDelegate`: marca modelo como `BLOCKED` en registry.

## Testing
- Unit tests con mock del micro `leka-bias-detection`.
- Flowable test para rutas `AUTO_NOTIFY`, `ESCALATE`.

## Pendientes
- Agregar métricas específicas por sector (umbral configurable).
- Integrar con `ai-component-onboarding` para fallback automático.

