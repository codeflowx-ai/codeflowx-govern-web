# Model Approval v1 – Guía Funcional

## Objetivo
Aprobar modelos de Machine Learning/ML Ops antes de su despliegue en entornos de producción, verificando performance, sesgos, cumplimiento regulatorio y validación humana (Art. 10, 13, 15 EU AI Act e ISO 42001 8.2).

## Actores
- **ML Engineer**: responsable de la solicitud y de la evidencia técnica.
- **Data Science Lead**: supervisa resultados y firma técnica.
- **Equipo de Cumplimiento**: revisa políticas, privacidad y explicabilidad.
- **Comité de Gobierno**: decide aprobaciones condicionadas o rechazos.

## Flujo funcional
1. **Inicio**: se crea la solicitud con `modelId`, `version`, `tipo de despliegue`.
2. **Evaluaciones paralelas**:
   - `Model Validation` (performance + robustez).
   - `Bias Detection`.
   - `Compliance Check` (datos, licencias, GDPR).
3. **Join** → se realiza `ML Engineer Review`.
4. **Governance Review** (User Task) con SLA estándar (3 días).
5. **Business Rule Task** (`model-approval-scoring.drl`):
   - `APPROVED`: cumple todos los criterios.
   - `CONDITIONAL_APPROVAL`: requiere monitoreo/acciones extra.
   - `REJECTED`.
6. **Service Tasks finales**:
   - `MarkModelProduction` / `MarkModelConditional`.
   - `RejectModel` cuando aplique.
   - Notificaciones a los equipos y registro en `ImmutableLog`.

## Variables
- Entradas: `modelId`, `versionId`, `trainingDataset`, `targetEnvironment`.
- Métricas: `performanceScore`, `biasScore`, `complianceScore`, `driftRisk`.
- Salida: `finalDecision`, `confidenceLevel`, `requiresMonitoring`, `justification`, `mlEngineerApproval`, `governanceApproval`.

## SLA orientativos
- Evaluaciones automáticas: < 60 min.
- Revisión ML Engineer: 24 h.
- Revisión Governance: 72 h.
- Publicación en producción: inmediatamente después de `MarkModelProduction`.

## Interacciones externas
- `leka-model-evaluation`, `leka-bias-detection`, `leka-llm-evaluation`.
- Sistemas de versionado de modelos (MLflow, SageMaker, etc.).
- `AIOS Runtime` para generar políticas y telemetría.

## Próximos pasos
- Integrar con el nuevo `AioDeploymentService` para disparar despliegues automáticos post‑aprobación.
- Incorporar verificación de “post-market monitoring plan” y requisitos de explicabilidad.


