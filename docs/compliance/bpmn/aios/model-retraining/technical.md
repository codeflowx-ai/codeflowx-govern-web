# Model Retraining Orchestration v1 – Guía Técnica

## Artefactos
- **BPMN**: `processes/aios/model-retraining-orchestration-v1.bpmn`
- **Delegates** (a reforzar):
  - `TriggerRetrainingDelegate`
  - `MonitorRetrainingJobDelegate`
  - `ValidateRetrainedModelDelegate`
  - `UpdateModelRegistryDelegate`
  - `RollbackModelDelegate`

## Variables
- `modelId`, `previousVersionId`, `datasetVersion`.
- `retrainingJobId`, `retrainingStatus`.
- `validationMetrics`, `validationResult`.
- `promotionDecision`, `deploymentStrategy`.

## Integraciones
- **Training Platform** (SageMaker, Vertex, Ray, etc.) para lanzar y monitorear jobs.
- **ModelRegistry**: registrar la nueva versión y su metadata.
- **DeploymentAutomation**: se puede invocar al final en caso de éxito.
- **Telemetry/Drift services**: aportan la condición de disparo.

## Flujo técnico
1. `TriggerRetrainingDelegate` crea el job con parámetros y guarda `retrainingJobId`.
2. `MonitorRetrainingJobDelegate` consulta el estado hasta completarse o fallar.
3. En éxito → `ValidateRetrainedModelDelegate` llama a `leka-model-evaluation` y `leka-bias-detection`.
4. Si la validación es OK → `UpdateModelRegistryDelegate` crea versión y registra vínculos con datasets/parámetros.
5. Si falla → `RollbackModelDelegate` deja constancia y notifica.

## Pendientes
- Implementar delegates reales (actualmente placeholders en muchos repos).
- Añadir un `BusinessRuleTask` para decidir automáticamente si se promociona (basado en delta de métricas).
- Escribir tests integrados con un mock de la plataforma de entrenamiento.


