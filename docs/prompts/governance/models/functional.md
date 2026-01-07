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

## Formularios (User Tasks)

- **Submit Model for Approval** (`plataforma/workflow/model-approval-request-form.zul`)
  - Formulario para solicitar aprobación del modelo
  - Inputs: `modelId`, `versionId`, `approvalType` (NEW_MODEL, VERSION_UPDATE, REDEPLOYMENT), `targetEnvironment` (STAGING, PRODUCTION), `businessJustification`
  - Estado: ✅ **CREADO** - `app/(app)/bpmn/forms/model-approval-request/page.tsx`
  - Pendiente: Traducciones i18n

- **ML Engineer Review** (`plataforma/workflow/model-ml-review-form.zul`)
  - Revisión técnica del modelo por ML Engineer
  - Muestra métricas: `performanceScore`, `biasScore`, `complianceScore`
  - Outputs: `mlApproval` (APPROVED, REJECTED, NEEDS_CHANGES), `reviewNotes`
  - Estado: ✅ **CREADO** - `app/(app)/bpmn/forms/model-ml-review/page.tsx`
  - Pendiente: Traducciones i18n, carga de datos del workflow

- **Governance Review** (`plataforma/workflow/model-governance-review-form.zul`)
  - Revisión de governance (ética, riesgos, compliance)
  - Muestra métricas y decisión previa del ML Engineer
  - Outputs: `governanceApproval` (APPROVED, REJECTED, CONDITIONAL), `riskAssessment` (LOW, MEDIUM, HIGH), `reviewNotes`
  - Estado: ✅ **CREADO** - `app/(app)/bpmn/forms/model-governance-review/page.tsx`
  - Pendiente: Traducciones i18n, carga de datos del workflow

- **SLA Reminder** (`bpmn/model-approval-reminder-form.zul`)
  - Recordatorio cuando se excede el SLA de 3 días en Governance Review
  - Estado: ✅ **EXISTE** - `app/(app)/bpmn/forms/model-approval-reminder/page.tsx`

## Integración con Backend

El workflow se dispara automáticamente al crear un nuevo modelo mediante `ModelBusinessService.createModel()`:

- **Archivo:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/models/ModelBusinessService.java`
- **Proceso BPMN:** `model-approval-v1`
- **Variables iniciales:**
  - `modelId`, `modelName`, `modelType`, `modelVersion`, `modelDescription`, `status`, `createdBy`, `createdAt`

## Próximos pasos
- ✅ Formularios creados (pendiente traducciones i18n)
- ⚠️ Configurar mapeo de formKeys BPMN a rutas Next.js
- ⚠️ Implementar carga de datos del workflow en formularios
- ⚠️ Integrar con el nuevo `AioDeploymentService` para disparar despliegues automáticos post‑aprobación.
- ⚠️ Incorporar verificación de "post-market monitoring plan" y requisitos de explicabilidad.
