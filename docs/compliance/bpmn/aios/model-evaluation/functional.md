# Model Evaluation v1 – Guía Funcional

## Objetivo
Estandarizar la evaluación de modelos (tanto de entrenamiento como de retraining) antes de pasar a la fase de aprobación o despliegue, asegurando calidad técnica y cumplimiento de métricas.

## Roles
- **Data Scientist / ML Engineer**: ejecuta la evaluación.
- **Model Reviewer**: valida resultados (puede ser el mismo owner o un revisor independiente).
- **Governance**: consulta los resultados en dashboards.

## Flujo
1. **Inicio**: se envía la solicitud con `modelId`, `dataset`, `hiperparámetros`.
2. **Service Task – Model Evaluation**: ejecuta suite automática (métricas, curvas, robustez).
3. **Service Task – Store Evaluation Results**: persiste métricas y artefactos (confusion matrix, etc.).
4. **Business Rule Task – Evaluation Decision**: compara contra umbrales y decide `PASS/FAIL`.
5. **Final**:
   - `PASS` → habilita el modelo para `model-approval`.
   - `FAIL` → notifica y cierra la solicitud.

## Variables
- `modelId`, `datasetId`, `evaluationConfig`.
- `accuracy`, `precision`, `recall`, `latency`, `resourceUsage`.
- `evaluationOutcome` (`PASS`/`FAIL`), `findings`, `evaluationReportUrl`.

## SLA
- Job de evaluación: depende del tipo de modelo (target < 2 h).
- Notificación al owner en < 15 min después de la finalización.

## Consideraciones
- Esta evaluación se puede ejecutar en distintos entornos (lab, staging).
- Debe integrarse con `llm-evaluation` y `rag-evaluation` para casos específicos.


