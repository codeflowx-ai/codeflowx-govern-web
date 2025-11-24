# Model Evaluation v1 – Guía Técnica

## Artefactos
- **BPMN**: `processes/aios/model-evaluation-v1.bpmn`
- **Delegates**:
  - `ModelEvaluationDelegate`
  - `StoreEvaluationDelegate`
  - (Opcional) `NotifyEvaluationResultDelegate`
- **Reglas**: `rules/aios/evaluation/model-eval.drl` (por completar)
- **Fact**: `EvaluationScoreFact`

## Variables
- `modelId`, `versionId`, `datasetId`, `evaluationProfile`.
- `metrics`: `accuracy`, `precision`, `recall`, `latency`, `throughput`.
- `evaluationResult` (`PASS`/`FAIL`), `justification`, `reportUrl`.

## Integraciones
- **leka-model-evaluation** (API).
- **Artifact Store** (para guardar métricas, gráficos, logs).
- **ModelRegistry** (para adjuntar los resultados).
- **NotificationService**.

## Flujo técnico
1. `ModelEvaluationDelegate` prepara el payload y llama al servicio de evaluación (sincronamente o asíncrono).
2. Los resultados se escriben en `metrics` + `evaluationStatus`.
3. Reglas (o lógica en `StoreEvaluationDelegate`) determinan `PASS` vs `FAIL`.
4. `StoreEvaluationDelegate` persiste en base de datos, sube el reporte y deja referencia en `evaluationReportUrl`.
5. Si el resultado es `PASS`, se puede notificar a `model-approval`; si es `FAIL`, se envía a los owners.

## Pendientes
- Formalizar `EvaluationScoreFact` y las reglas de umbrales.
- Añadir soporte para comparaciones contra baseline.
- Crear tests (mock de `leka-model-evaluation`) para ambos caminos.


