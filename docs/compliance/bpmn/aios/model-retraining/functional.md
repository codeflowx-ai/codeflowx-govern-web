# Model Retraining Orchestration v1 – Guía Funcional

## Objetivo
Orquestar el proceso de re‑entrenamiento de modelos cuando se detecta degradación, drift u otras condiciones de negocio, asegurando aprobación y despliegue controlado.

## Roles
- **MLOps**: dispara el proceso (manualmente o automáticamente).
- **Data Science Team**: ejecuta y valida el nuevo modelo.
- **Governance Board**: aprueba su promoción.

## Flujo
1. **Start** – trigger desde `drift/performance` o manual.
2. **Service Task – Trigger Retraining**: inicia pipeline de entrenamiento con los datos aprobados.
3. **Service Task – Monitor Retraining Job**: espera resultado (éxito/fallo).
4. **Gateway – Job Success?**
   - YES → `Validate Retrained Model`.
   - NO → `RollbackModel` / notificar.
5. **Service Task – Validate Retrained Model**: corre suite de evaluación.
6. **Gateway – Validation Pass?**
   - YES → `Update Model Registry` y (opcional) dispara `deployment-automation`.
   - NO → `RollbackModel` / `Notify Failure`.
7. **End**.

## Variables
- `retrainingJobId`, `datasetVersion`, `hyperparameters`.
- `retrainingStatus`, `validationMetrics`.
- `promotionDecision`, `previousVersionId`.

## SLA
- Entrenamiento automático: depende del tipo de modelo (definir por familia).
- Validación: < 24 h.
- Promoción: sujeta a `model-approval` si hay cambios mayores.

## Consideraciones
- Debe tener trazabilidad de datos usados y parámetros (para auditoría).
- Posibilidad de ejecutar en entornos híbridos (on‑prem + cloud).


