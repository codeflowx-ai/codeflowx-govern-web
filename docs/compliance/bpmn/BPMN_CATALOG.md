# BPMN Catalog – CodeflowX Govern (Nov 2025)

## Alcance y criterios

- Cobertura de todos los procesos BPMN presentes en `codeflowx.govern.workflow.lib/src/main/resources/processes` (excluyendo `education/`).
- Fuentes analizadas: `PROMPTS_04`, `PROMPTS_06`, `PROMPTS_09`, `PROMPT_001/002/003`, `PROMPTS_ISO_GDPR_*`, `MANUAL_DESARROLLADOR_PROCESOS_BPMN.md`, `TRACKING_PROMPTS_IMPLEMENTACION.md`, matrices de compliance y plan de governance.
- Objetivo: visibilidad funcional (responsabilidad, normativa, delegados/artefactos) y detección de brechas por falta de documentación, alineación AI‑OS o gaps operativos.
- Estado se clasifica como:
  - `Documentado`: existe prompt/manual con flujo detallado y variables.
  - `Parcial`: descripción resumida pero falta especificación completa o evidencias de implementación.
  - `Sin ficha`: no se encontró documentación funcional.

## Organización de carpetas BPMN

- `processes/aios/`: Flujos AI‑OS (onboarding, approvals, runtime, marketplace, evaluaciones).
- `processes/compliance/`: Procesos regulatorios EU AI Act / GDPR (conformidad, FRIA, incidentes, consentimientos).
- `processes/audit/`: Workflows ISO 42001 / ISO 38507 (auditorías, reviews, acciones correctivas).
- `processes/metrics/`: Monitoreo operacional (bias, drift, performance, alertas, dataset quality).
- `processes/education/`: Sector Education / FaaS (se mantiene sin cambios).

## 1. Procesos de aprobación, consentimiento y conformidad

| Proceso (archivo) | Responsabilidad funcional | Cobertura normativa / framework | Fuente(s) | Integración AI‑OS / Entidades | Estado / Gaps |
| --- | --- | --- | --- | --- | --- |
| `agent-approval-v1.bpmn` | Orquestar validaciones de riesgo, compliance y ética para agentes; gateway auto/aprobación HITL. | EU AI Act Art. 9, 14; políticas internas AI‑OS. | `docs/compliance/bpmn/aios/agent-approval/functional.md`<br>`docs/compliance/bpmn/aios/agent-approval/technical.md` | `AioComponent`, `AioPolicyBinding`, `ImmutableLog`. | Documentado; falta integrar los nuevos servicios de Runtime y capturar evidencias de ejecución. |
| `model-approval-v1.bpmn` | Aprobar modelos ML (performance, bias, compliance) antes de producción. | EU AI Act Art. 15, 10; ISO 42001 8.2. | `docs/compliance/bpmn/aios/model-approval/functional.md`<br>`docs/compliance/bpmn/aios/model-approval/technical.md` | `AioComponent`, `ComplianceAssessment`, `ModelRegistry`. | Documentado; pendiente integrar con `AioDeploymentService` y pruebas end-to-end. |
| `prompt-approval-v1.bpmn` | Validar prompts RAG/LLM (safety, políticas). | EU AI Act Art. 9, 14, 52. | `docs/compliance/bpmn/aios/prompt-approval/functional.md`<br>`docs/compliance/bpmn/aios/prompt-approval/technical.md` | `AioPolicyBinding`, `PromptCatalog`, `ImmutableLog`. | Documentado; falta consolidar clientes de seguridad y conexión directa con el marketplace. |
| `adapter-creation-approval-v1.bpmn20.xml` | Evaluar solicitudes de adapters (riesgo, reutilización). | EU AI Act Art. 53, Art. 10. | `docs/compliance/bpmn/aios/adapter-creation-approval/functional.md`<br>`docs/compliance/bpmn/aios/adapter-creation-approval/technical.md` | `AdapterRegistry`, `AioServiceBinding`. | Documentado; requiere implementar reglas Drools y validar el flujo en runtime. |
| `finetuning-approval-v1.bpmn20.xml` | Controlar peticiones de fine-tuning, priorizar adapters, requerir evidencias. | EU AI Act Art. 53 + políticas internas. | `docs/compliance/bpmn/aios/finetuning-approval/functional.md`<br>`docs/compliance/bpmn/aios/finetuning-approval/technical.md` | `FineTuningRequest`, `ImmutableLog`, `leka-model-wrapper`. | Documentado; faltan delegates definitivos y checklist AI‑OS. |
| `consent-management-v1.bpmn` | Gestionar ciclo de vida de consentimientos (registro, validación, notificación, expiración). | GDPR Art. 7 & 8. | `docs/compliance/bpmn/compliance/consent-management/functional.md`<br>`docs/compliance/bpmn/compliance/consent-management/technical.md` | `ConsentRecord` (tabla ENART `CONCONSENTRECORDS`), `ImmutableLog`. | Documentado; validar API de revocación y sincronización con micro de consents. |
| `internal-conformity-assessment-v1.bpmn` | Evaluación interna Art. 43 (Annex VI) con checks por artículos 9‑15 y declaración. | EU AI Act Art. 43 / Annex VI. | `docs/compliance/bpmn/compliance/internal-conformity/functional.md`<br>`docs/compliance/bpmn/compliance/internal-conformity/technical.md` | `ComplianceAssessment`, `AioComponent`, `ImmutableLog`. | Documentado; falta orquestarlo desde el micro `workflow.engine`. |
| `conformity-assessment-process.bpmn20.xml` | Proceso completo Annex VI con user tasks (Initiate, Review gaps, Final approval). | EU AI Act Art. 43 / Annex VI. | `docs/compliance/bpmn/compliance/conformity-assessment/functional.md`<br>`docs/compliance/bpmn/compliance/conformity-assessment/technical.md` | `ComplianceAssessment`, `EuDeclaration`, `ImmutableLog`. | Documentado; falta validar despliegue end-to-end y trazabilidad con `workflow.engine`. |
| `fria-process.bpmn20.xml` | Fundamental Rights Impact Assessment: wizard, análisis, revisión ética y notificación autoridad. | EU AI Act Art. 27. | `docs/compliance/bpmn/compliance/fria/functional.md`<br>`docs/compliance/bpmn/compliance/fria/technical.md` | `FriaAssessment`, `Project`, `ImmutableLog`. | Documentado; pendiente automatizar notificación a autoridad y versionado del reporte. |
| `incident-reporting-process.bpmn20.xml` | Gestión completa de incidentes (clasificar, notificar autoridades/usuarios, RCA, acciones correctivas). | EU AI Act Art. 20, 62, 73. | `docs/compliance/bpmn/compliance/incident-reporting/functional.md`<br>`docs/compliance/bpmn/compliance/incident-reporting/technical.md` | `IncidentReport`, `CorrectiveAction`, `ImmutableLog`. | Documentado; falta probar integración con RCA y sistemas externos de notificación. |
| `eu-database-registration-process.bpmn20.xml` | Determinar tipo de registro (Secciones A/B/C), validar datos y enviar a API UE. | EU AI Act Art. 49 & Annex VIII. | `docs/compliance/bpmn/compliance/eu-database-registration/functional.md`<br>`docs/compliance/bpmn/compliance/eu-database-registration/technical.md` | `EuRegistration`, `Project`, `ImmutableLog`. | Documentado; requiere cliente oficial hacia la API UE y manejo de reintentos en producción. |
| `ai-component-onboarding-v1.bpmn` | Automatizar onboarding de componentes AI‑OS desde solicitud hasta activación. | EU AI Act Art. 9-15 (verificaciones) + políticas internas AI‑OS. | `docs/compliance/bpmn/aios/ai-component-onboarding/functional.md`<br>`docs/compliance/bpmn/aios/ai-component-onboarding/technical.md` | `AioComponent`, `AioPolicyBinding`, `ImmutableLog`. | Documentado; pendiente validar delegados y clientes externos (`leka-*`). |
| `ai-marketplace-publish-v1.bpmn` | Publicar componentes en marketplace con scoring y revisiones legales. | EU AI Act Art. 52, comercio justo IA. | `docs/compliance/bpmn/aios/ai-marketplace-publish/functional.md`<br>`docs/compliance/bpmn/aios/ai-marketplace-publish/technical.md` | `MarketplacePublication`, `AioComponent`, `ImmutableLog`. | Documentado; falta implementación de notificaciones/tickets reales. |
| `external-model-approval-v1.bpmn` | Aprobar modelos externos con clasificación de riesgo y FRIA si aplica. | EU AI Act Art. 43, 54 (modelos terceros). | `docs/compliance/bpmn/aios/external-model-approval/functional.md`<br>`docs/compliance/bpmn/aios/external-model-approval/technical.md` | `ExternalModel`, `FriaAssessment`, `ImmutableLog`. | Documentado; clientes (`leka-risk-evaluator`, autoridad) en desarrollo. |

## 2. Procesos de riesgo, monitoreo y lifecycle (core AI governance)

| Proceso | Responsabilidad | Cobertura | Fuente(s) | Integración AI‑OS / Entidades | Estado / Gaps |
| --- | --- | --- | --- | --- | --- |
| `compliance-monitoring-v1.bpmn` | Monitor diario, ahora extendido con verificación de logs, evaluaciones adversariales y métricas post-market. | EU AI Act Art. 15, 19, 72. | `docs/compliance/bpmn/compliance/compliance-monitoring/functional.md`<br>`docs/compliance/bpmn/compliance/compliance-monitoring/technical.md` | `AioTelemetry`, `ImmutableLog`, `ComplianceDashboard`. | Documentado; pendiente validar despliegue extendido y clientes de adversarial/log integrity. |
| `ethics-review-v1.bpmn` | Revisión ética HITL. | Art. 9, 14; ISO 42001 clause 4.2. | `docs/compliance/bpmn/compliance/ethics-review/functional.md`<br>`docs/compliance/bpmn/compliance/ethics-review/technical.md` | `EthicsReview`, `AioPolicyBinding`. | Documentado; falta enlazar con flujos de Policy Review para cierres automáticos. |
| `risk-assessment-v1.bpmn` | Evaluar riesgos, programar revisión, aceptar riesgo residual. | EU AI Act Art. 9; ISO 42001 6.1. | `docs/compliance/bpmn/compliance/risk-assessment/functional.md`<br>`docs/compliance/bpmn/compliance/risk-assessment/technical.md` | `RiskRegister`, `AioComponent`, `ImmutableLog`. | Documentado; pendiente instrumentar métricas y SLAs en runtime. |
| `bias-detection-v1.bpmn` | Pipeline periódico con preparación de datos demográficos y llamada a `leka-bias-detection`. | EU AI Act Art. 10, 15. | `docs/compliance/bpmn/metrics/bias-detection/functional.md`<br>`docs/compliance/bpmn/metrics/bias-detection/technical.md` | `leka-bias-detection-service`, `DatasetRegistry`, `ImmutableLog`. | Documentado; falta desplegar cliente Python definitivo. |
| `drift-detection-v1.bpmn` | Comparar baseline vs métricas actuales y alertar. | Art. 15 (robustness), Art. 72. | `docs/compliance/bpmn/metrics/drift-detection/functional.md`<br>`docs/compliance/bpmn/metrics/drift-detection/technical.md` | `AioTelemetryService`, `leka-bias-detection` (drift endpoint), `ImmutableLog`. | Documentado; definir thresholds por sector y path automático a retraining. |
| `performance-degradation-v1.bpmn` | Detectar degradación estadística y disparar retraining. | Art. 15, Annex IX tests. | `docs/compliance/bpmn/metrics/performance-degradation/functional.md`<br>`docs/compliance/bpmn/metrics/performance-degradation/technical.md` | `AioTelemetryService`, `ModelRegistry`, `ImmutableLog`. | Documentado; falta integración automática con `model-retraining`. |
| `alert-response-v1.bpmn` | Clasificar alertas y escalar según severidad. | ISO 42001 8.8, EU AI Act Art. 72. | `docs/compliance/bpmn/metrics/alert-response/functional.md`<br>`docs/compliance/bpmn/metrics/alert-response/technical.md` | `AlertCenter`, `TaskInbox`, `ImmutableLog`. | Documentado; falta enlazar con ITSM real y checklists. |
| `incident-response-rca-v1.bpmn` | RCA automática tras incidentes, generar mitigaciones. | Art. 20, 72. | `docs/compliance/bpmn/metrics/incident-response-rca/functional.md`<br>`docs/compliance/bpmn/metrics/incident-response-rca/technical.md` | `IncidentReport`, `leka-incident-rca` (python micro), `ImmutableLog`. | Documentado; falta definir handoff automático desde `incident-reporting`. |
| `dataset-quality-v1.bpmn` | Validar datasets antes de entrenar (pass/fail). | EU AI Act Art. 10 (data governance). | `docs/compliance/bpmn/metrics/dataset-quality/functional.md`<br>`docs/compliance/bpmn/metrics/dataset-quality/technical.md` | `DatasetRegistry`, `leka-bias-detection-service`, `ImmutableLog`. | Documentado; validar integración con catálogo de datasets y issue tracker. |
| `model-retraining-orchestration-v1.bpmn` | Programar y validar re-entrenamientos tras degradación. | Art. 54 (post-market), ISO 42001 lifecycle. | `docs/compliance/bpmn/aios/model-retraining/functional.md`<br>`docs/compliance/bpmn/aios/model-retraining/technical.md` | `AioDeploymentService`, `ModelRegistry`, `ImmutableLog`. | Documentado; requiere SLA formales y handshake con deployment automation. |
| `deployment-automation-v1.bpmn` | Automatizar despliegues, autoscaling y rollback. | PROMPT_002 runtime; ISO 42001 8.4. | `docs/compliance/bpmn/aios/deployment-automation/functional.md`<br>`docs/compliance/bpmn/aios/deployment-automation/technical.md` | `AioDeploymentService`, `KubernetesAdapter`, `ImmutableLog`. | Documentado; pendiente construir los servicios runtime (`AioDeploymentService`). |
| `model-evaluation-v1.bpmn` | Evaluar modelos tradicionales. | Art. 15, Annex VII. | `docs/compliance/bpmn/aios/model-evaluation/functional.md`<br>`docs/compliance/bpmn/aios/model-evaluation/technical.md` | `ModelRegistry`, `leka-model-evaluation`, `ImmutableLog`. | Documentado; falta integrar telemetría AI‑OS y reporting automático. |
| `llm-evaluation-v1.bpmn` | Evaluar LLMs, aplicar reglas/hitl. | Art. 15, 52. | `docs/compliance/bpmn/aios/llm-evaluation/functional.md`<br>`docs/compliance/bpmn/aios/llm-evaluation/technical.md` | `leka-llm-evaluation`, `ImmutableLog`, `AioPolicyBinding`. | Documentado; pendiente validar clientes externos y guardar métricas en `ImmutableLog`. |
| `rag-evaluation-v1.bpmn` | Evaluar sistemas RAG (latencia, precisión, grounding). | Art. 15, Annex IX. | `docs/compliance/bpmn/aios/rag-evaluation/functional.md`<br>`docs/compliance/bpmn/aios/rag-evaluation/technical.md` | `RagRegistry`, `leka-rag-evaluation`, `ImmutableLog`. | Documentado; falta definir métricas por vertical y enlazar con approvals. |
| `ai-runtime-health-v1.bpmn` | Monitorizar salud runtime y disparar tickets/incidentes. | EU AI Act Art. 72 (post-market), ISO 42001 9.1. | `docs/compliance/bpmn/aios/ai-runtime-health/functional.md`<br>`docs/compliance/bpmn/aios/ai-runtime-health/technical.md` | `AioTelemetry`, `IncidentReport`, `ImmutableLog`. | Documentado; requiere pruebas de integración y clientes `aio-telemetry`. |
| `ai-policy-review-v1.bpmn` | Revisar políticas AI‑OS y aplicar remediaciones/suspensiones. | EU AI Act Art. 9-15 (governance continuo). | `docs/compliance/bpmn/aios/ai-policy-review/functional.md`<br>`docs/compliance/bpmn/aios/ai-policy-review/technical.md` | `AioPolicyBinding`, `ImmutableLog`, `PolicyDashboard`. | Documentado; pendiente conectar con `aio-policy-service` y flujos de suspensión.

## 3. Procesos regulatorios adicionales

| Proceso | Responsabilidad | Cobertura | Fuente(s) | Integración AI‑OS / Entidades | Estado / Gaps |
| --- | --- | --- | --- | --- | --- |
| `iso42001-management-review-v1.bpmn` | Revisiones trimestrales (preparar datos, reunión, decisiones, acciones). | ISO 42001 Clause 9.3. | `docs/compliance/bpmn/audit/iso42001-management-review/functional.md`<br>`docs/compliance/bpmn/audit/iso42001-management-review/technical.md` | `ApsAimsPerformance`, `ImmutableLog`. | Documentado; asegurar integración con fuentes AIMS y evidencias en `workflow.engine`. |
| `iso42001-internal-audit-v1.bpmn` | Planificar y ejecutar auditorías internas. | ISO 42001 Clause 9.2. | `docs/compliance/bpmn/audit/iso42001-internal-audit/functional.md`<br>`docs/compliance/bpmn/audit/iso42001-internal-audit/technical.md` | `AuditRecord`, `ImmutableLog`. | Documentado; falta automatizar generación/almacenamiento de checklists. |
| `iso42001-corrective-action-v1.bpmn` | Gestionar no conformidades y acciones. | ISO 42001 Clause 10.2. | `docs/compliance/bpmn/audit/iso42001-corrective-action/functional.md`<br>`docs/compliance/bpmn/audit/iso42001-corrective-action/technical.md` | `AncNonConformity`, `CorrectiveAction`, `ImmutableLog`. | Documentado; integrar con Task Service/Issue tracker para seguimiento. |
| `iso42001-ai-decommissioning-v1.bpmn` | Desmantelar sistemas IA (data retention → archive). | ISO 42001 Clause 8.1. | `docs/compliance/bpmn/audit/iso42001-ai-decommissioning/functional.md`<br>`docs/compliance/bpmn/audit/iso42001-ai-decommissioning/technical.md` | `AioComponent`, `DataRetentionPlan`, `ImmutableLog`. | Documentado; requiere implementar delegates reales para freeze/archive/retention. |
| `iso42001-competence-gap-v1.bpmn` | Cerrar brechas de competencias (plan, ejecución, verificación). | ISO 42001 Clause 7.2. | `docs/compliance/bpmn/audit/iso42001-competence-gap/functional.md`<br>`docs/compliance/bpmn/audit/iso42001-competence-gap/technical.md` | `CompetenceGap`, `TrainingPlan`, `ImmutableLog`. | Documentado; falta integrar con sistemas HR y definir delegates concretos. |
| `iso38507-board-decision-v1.bpmn` | Aprobación decisiones de junta (prepare → evaluar → registrar → monitorear). | ISO 38507 (EDM). | `docs/compliance/bpmn/audit/iso38507-board-decision/functional.md`<br>`docs/compliance/bpmn/audit/iso38507-board-decision/technical.md` | `BDCBoardDecision`, `BRDBoardReport`, `ImmutableLog`. | Documentado; pendiente conectar con BoardDecisionService.

## 4. Estado de cobertura y brechas

### Procesos existentes sin ficha funcional localizada

No hay procesos pendientes de ficha: todos los BPMN inventariados cuentan ahora con guía funcional y técnica en `docs/compliance/**`. Se mantiene monitoreo para nuevas incorporaciones o cambios futuros.

### Procesos requeridos por AI‑OS pero ausentes en `processes/`

Actualmente no hay procesos pendientes; todos los workflows planificados están creados en `processes/` (será reevaluado cada sprint).

### Gaps transversales detectados

1. **Validación runtime pendiente**: falta levantar `codeflowx.govern.workflow.engine` con todos los BPMN reubicados para certificar carga de delegates/reglas y adjuntar evidencias en `BPMN_AUDIT_STATUS.md`.
2. **Clientes externos (`@todo`)**: los delegates AI‑OS todavía no consumen los microservicios Python (`leka-*`, `aio-telemetry`, `aio-policy-service`, ITSM). Se requiere implementar los clients o documentar mock/stub operativo.
3. **Pruebas automatizadas**: no existen suites unitarias/integración que ejerciten las nuevas reglas Drools y delegates. Debe añadirse cobertura y publicar resultados.
4. **Referencias cruzadas**: manuales históricos (`PROMPTS_04/06/09`, `MANUAL_DESARROLLADOR_PROCESOS_BPMN.md`, docs de studio) aún apuntan a rutas antiguas; hay que actualizar enlaces a las nuevas subcarpetas y guías.
5. **Evidencias ENART**: se debe adjuntar checklist de cumplimiento (nombres, entidades, logging) por proceso para cerrar la auditoría y asegurar trazabilidad ImmutableLog/AI‑OS en ejecución.

## 5. Próximos pasos sugeridos

1. **Ejecución controlada del engine**: iniciar `codeflowx.govern.workflow.engine` con la librería actual y capturar logs/screenshots para dejar evidencia en `BPMN_AUDIT_STATUS.md`.
2. **Clientes externos**: diseñar/implementar los clientes Python/REST marcados con `@todo` en los delegates (bias detection, telemetry, policy, marketplace, ITSM) y documentar su estado.
3. **Pruebas automatizadas**: agregar unit/integration tests para delegates y reglas Drools; registrar los resultados en el repositorio/guides.
4. **Actualizar cross references**: sincronizar `PROMPTS_04/06/09`, `PROMPTS_14`, `MANUAL_DESARROLLADOR_PROCESOS_BPMN.md` y documentación de studio para que apunten a `docs/compliance/**` y a las nuevas rutas BPMN.
5. **Checklist ENART y matrices RACI**: complementar cada ficha con evidencias de naming, entidades, ImmutableLog y responsables/SLAs para cerrar la auditoría.

> Nota: Los procesos bajo `processes/education/` se omiten deliberadamente; serán abordados en el proyecto FaaS sectorial (51 sectores) según lo indicado por Operaciones.


