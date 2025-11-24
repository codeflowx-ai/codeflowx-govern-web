# FRIA Process – Guía Técnica

## Artefactos
- **BPMN**: `processes/compliance/fria-process.bpmn20.xml`
- **Delegates**:
  - `GenerateFriaDocumentDelegate`
  - `AnalyzeFundamentalRightsDelegate`
  - `NotifyAuthorityFriaDelegate`
  - `RegisterFriaDelegate`
  - `UpdateProjectFriaStatusDelegate`
- **ViewModels**: `InitiateFria`, `CompleteMissingElements`, `EnhancedReview`, `DefineModifications`, `DeployerApproval`.
- **Entidades**: `FriaAssessment` (`FRIAFUNDAMENTALRIGHTSASSESSMENTS`).

## Variables
- Entrada: `projectId`, `componentId`, `initiator`.
- Salidas:
  - `friaDocumentUrl`, `complianceScore`, `art27Compliant`.
  - `charterArticlesAffected` (JSONB), `impactSeverity`.
  - `notificationId`, `notificationDate`.
  - `friaApproved`, `projectStatus`.

## Integraciones
- **leka-fria-generator** (Python) → genera documento y score.
  - `POST /api/fria/generate-assessment` → genera FRIA documental
  - `POST /api/fria/cross-validate` 🆕 → valida consistencia FRIA vs métricas técnicas
- **leka-bias-detection-service** → obtiene métricas de sesgo y calidad de datos
- **leka-llm-evaluation** → obtiene métricas de performance del modelo
- **leka-adversarial-robustness** → obtiene métricas de robustez adversarial
- **Authority API** (Art. 27.3) → `NotifyAuthorityFriaDelegate`.
- **Govern Project Service** → `RegisterFriaDelegate`, `UpdateProjectFriaStatusDelegate`.

## Lógica
- `GenerateFriaDocumentDelegate`:
  - Construye payload con respuestas del wizard.
  - Llama micro Python (REST) → recibe URL + score.
  - Guarda en storage (S3/MinIO).
- `CrossValidateFriaDelegate` 🆕 (INC-007):
  - Construye payload con datos FRIA del wizard.
  - Llama `POST /api/fria/cross-validate` del microservicio Python.
  - Obtiene métricas técnicas automáticamente de microservicios:
    - `leka-bias-detection-service` → bias_score, dataset_quality
    - `leka-llm-evaluation` → model_performance
    - `leka-adversarial-robustness` → adversarial_robustness
  - Compara riesgos declarados vs métricas reales.
  - Detecta inconsistencias (BIAS_NOT_DECLARED, MITIGATION_NOT_IMPLEMENTED, etc.).
  - Calcula `consistencyScore` (0.0 - 1.0).
  - Si `consistencyScore < 0.70`, requiere justificación del usuario.
  - Guarda resultado en `FriaAssessment.friaCrossValidationResult`.
- `AnalyzeFundamentalRightsDelegate`: aplica heurísticas + catálogos de Charter (puede usar `leka-llm-evaluation` + `leka-risk-engine`).
- `NotifyAuthorityFriaDelegate`: formatea JSON con summary FRIA y lo envía al endpoint configurado.
- `RegisterFriaDelegate`: persiste `FriaAssessment` y vincula `Project`.
- `UpdateProjectFriaStatusDelegate`: actualiza flags `friaCompleted`, `readyForDeployment`.

## Validaciones
- `complianceScore >= 0.90` para considerarse completo.
- `consistencyScore >= 0.70` 🆕 para considerar FRIA consistente con métricas técnicas.
- Si `consistencyScore < 0.70`, requiere justificación del usuario antes de continuar.
- Si `impactSeverity == HIGH`, branch a `EnhancedReview`.
- En caso de `Modify`, se captura plan de mitigación y se repite wizard.

## Testing
- Unit tests para delegates de integración (mock servicios externos).
- Escenario Flowable:
  - Case 1: FRIA completada sin alto impacto (flujo directo).
  - Case 2: Alto impacto → Enhanced Review → Proceed.
  - Case 3: Rechazo → Cancel Deployment.

## Pendientes
- Implementar manejo de errores y reintentos para notificación autoridad.
- Añadir registro de versiones FRIA (control de cambios).

