# BPMN Audit & Documentation Status (Nov 2025)

> Propósito: Inventariar todos los procesos BPMN, verificar su documentación funcional/técnica y señalar los gaps pendientes antes de la auditoría.

## Leyenda
- **Funcional**: documento orientado a usuarios de negocio/compliance.
- **Técnico**: guía para developers o agentes IA (delegates, reglas, integraciones).
- **Estado**: ✅ completo · ⚠️ parcial · ⛔ pendiente.

## 1. AI‑OS / Runtime (`processes/aios/`)

| Proceso | BPMN | Funcional | Técnico | Estado | Gaps |
| --- | --- | --- | --- | --- | --- |
| agent-approval-v1 | `aios/agent-approval-v1.bpmn` | `docs/MANUAL_DESARROLLADOR_PROCESOS_BPMN.md` §1 | `docs/MANUAL_DESARROLLADOR_PROCESOS_BPMN.md` §1 | ⚠️ | Falta guía dedicada + actualización AI‑OS. |
| model-approval-v1 | `aios/model-approval-v1.bpmn` | Manual §2 | Manual §2 | ⚠️ | No enlaza nuevos servicios runtime. |
| prompt-approval-v1 | `aios/prompt-approval-v1.bpmn` | Manual §3 | Manual §3 | ⚠️ | Requiere guía propia + integraciones marketplace. |
| adapter-creation-approval-v1 | `.bpmn20.xml` | `PROMPTS_06_MLOPS...` | (sin guía) | ⚠️ | Crear guía técnica. |
| finetuning-approval-v1 | `.bpmn20.xml` | `PROMPTS_06...` | (sin guía) | ⚠️ | Documentar restricciones y delegates. |
| deployment-automation-v1 | `aios/deployment-automation-v1.bpmn` | Manual §11 | Manual §11 | ⚠️ | Delegates AI‑OS pendientes. |
| model-retraining-orchestration-v1 | `aios/model-retraining-orchestration-v1.bpmn` | Manual §17 | Manual §17 | ⚠️ | Especificación detallada pendiente. |
| model-evaluation-v1 | `aios/model-evaluation-v1.bpmn` | Manual §10 | Manual §10 | ⚠️ | Falta guía dedicada + telemetría. |
| llm-evaluation-v1 | `aios/llm-evaluation-v1.bpmn` | Manual §8 | Manual §8 | ⚠️ | Sin doc específica. |
| rag-evaluation-v1 | `aios/rag-evaluation-v1.bpmn` | Manual §9 | Manual §9 | ⚠️ | Variables/métricas incompletas. |
| ai-component-onboarding-v1 | `aios/ai-component-onboarding-v1.bpmn` | `docs/compliance/bpmn/aios/ai-component-onboarding/functional.md` | `docs/compliance/bpmn/aios/ai-component-onboarding/technical.md` | ✅ | Implementar clientes micros (@todo). |
| ai-runtime-health-v1 | `aios/ai-runtime-health-v1.bpmn` | `docs/compliance/bpmn/aios/ai-runtime-health/functional.md` | `docs/compliance/bpmn/aios/ai-runtime-health/technical.md` | ✅ | Conectar a `aio-telemetry-service`. |
| ai-marketplace-publish-v1 | `aios/ai-marketplace-publish-v1.bpmn` | `docs/compliance/bpmn/aios/ai-marketplace-publish/functional.md` | `docs/compliance/bpmn/aios/ai-marketplace-publish/technical.md` | ✅ | Notificaciones owner pendientes. |
| ai-policy-review-v1 | `aios/ai-policy-review-v1.bpmn` | `docs/compliance/bpmn/aios/ai-policy-review/functional.md` | `docs/compliance/bpmn/aios/ai-policy-review/technical.md` | ✅ | Cliente `aio-policy-service` en curso. |
| external-model-approval-v1 | `aios/external-model-approval-v1.bpmn` | `docs/compliance/bpmn/aios/external-model-approval/functional.md` | `docs/compliance/bpmn/aios/external-model-approval/technical.md` | ✅ | Cliente `leka-risk-evaluator` pendiente. |

## 2. Compliance / EU AI Act (`processes/compliance/`)

| Proceso | BPMN | Funcional | Técnico | Estado | Gaps |
| --- | --- | --- | --- | --- | --- |
| compliance-monitoring-v1 | `compliance/compliance-monitoring-v1.bpmn` | `docs/compliance/bpmn/compliance/compliance-monitoring/functional.md` | `docs/compliance/bpmn/compliance/compliance-monitoring/technical.md` | ✅ | Integraciones con micros (log integrity, adversarial) pendientes de clientes. |
| conformity-assessment-process | `.bpmn20.xml` | `docs/compliance/bpmn/compliance/conformity-assessment/functional.md` | `docs/compliance/bpmn/compliance/conformity-assessment/technical.md` | ✅ | Alinear con notified body + enlazar entidades `ComplianceAssessment`. |
| internal-conformity-assessment-v1 | `.bpmn` | `docs/compliance/bpmn/compliance/internal-conformity/functional.md` | `docs/compliance/bpmn/compliance/internal-conformity/technical.md` | ✅ | Consolidar decisiones duplicadas con proceso externo. |
| consent-management-v1 | `.bpmn` | `docs/compliance/bpmn/compliance/consent-management/functional.md` | `docs/compliance/bpmn/compliance/consent-management/technical.md` | ✅ | Implementar API revocación + clientes notificación. |
| incident-reporting-process | `.bpmn20.xml` | `docs/compliance/bpmn/compliance/incident-reporting/functional.md` | `docs/compliance/bpmn/compliance/incident-reporting/technical.md` | ✅ | Cliente autoridad Art. 73 y orquestación con RCA micro. |
| fria-process | `.bpmn20.xml` | `docs/compliance/bpmn/compliance/fria/functional.md` | `docs/compliance/bpmn/compliance/fria/technical.md` | ✅ | Manejo de errores notificación autoridad / versionamiento FRIA. |
| eu-database-registration-process | `.bpmn20.xml` | `docs/compliance/bpmn/compliance/eu-database-registration/functional.md` | `docs/compliance/bpmn/compliance/eu-database-registration/technical.md` | ✅ | Pendiente cliente oficial API UE. |
| risk-assessment-v1 | `.bpmn` | `docs/compliance/bpmn/compliance/risk-assessment/functional.md` | `docs/compliance/bpmn/compliance/risk-assessment/technical.md` | ✅ | Explorar reglas adicionales y dashboards riesgo. |
| ethics-review-v1 | `.bpmn` | `docs/compliance/bpmn/compliance/ethics-review/functional.md` | `docs/compliance/bpmn/compliance/ethics-review/technical.md` | ✅ | Integrar con Policy Review para follow-up automático. |

## 3. Audit / ISO (`processes/audit/`)

| Proceso | Documentación | Estado | Gaps |
| --- | --- | --- | --- |
| iso42001-management-review-v1 | Func: `docs/compliance/bpmn/audit/iso42001-management-review/functional.md`<br>Tech: `docs/compliance/bpmn/audit/iso42001-management-review/technical.md` | ✅ | Mantener integración con reportes AIMS. |
| iso42001-internal-audit-v1 | Func: `docs/compliance/bpmn/audit/iso42001-internal-audit/functional.md`<br>Tech: `docs/compliance/bpmn/audit/iso42001-internal-audit/technical.md` | ✅ | Checklist dinámico según alcance (por implementar). |
| iso42001-corrective-action-v1 | Func: `docs/compliance/bpmn/audit/iso42001-corrective-action/functional.md`<br>Tech: `docs/compliance/bpmn/audit/iso42001-corrective-action/technical.md` | ✅ | Integrar loops con Task Service. |
| iso42001-competence-gap-v1 | Func: `docs/compliance/bpmn/audit/iso42001-competence-gap/functional.md`<br>Tech: `docs/compliance/bpmn/audit/iso42001-competence-gap/technical.md` | ✅ | Definir delegates reales (HR service). |
| iso42001-ai-decommissioning-v1 | Func: `docs/compliance/bpmn/audit/iso42001-ai-decommissioning/functional.md`<br>Tech: `docs/compliance/bpmn/audit/iso42001-ai-decommissioning/technical.md` | ✅ | Implementar servicios Freeze/Archive/Data Retention. |
| iso38507-board-decision-v1 | Func: `docs/compliance/bpmn/audit/iso38507-board-decision/functional.md`<br>Tech: `docs/compliance/bpmn/audit/iso38507-board-decision/technical.md` | ✅ | Conectar con BoardDecisionService real. |

## 4. Metrics / Monitoring (`processes/metrics/`)

| Proceso | Documentación | Estado | Gaps |
| --- | --- | --- | --- |
| bias-detection-v1 | Func: `docs/compliance/bpmn/metrics/bias-detection/functional.md`<br>Tech: `docs/compliance/bpmn/metrics/bias-detection/technical.md` | ✅ | Implementar cliente `leka-bias-detection`. |
| drift-detection-v1 | Func: `docs/compliance/bpmn/metrics/drift-detection/functional.md`<br>Tech: `docs/compliance/bpmn/metrics/drift-detection/technical.md` | ✅ | Enlazar con retraining automático. |
| performance-degradation-v1 | Func: `docs/compliance/bpmn/metrics/performance-degradation/functional.md`<br>Tech: `docs/compliance/bpmn/metrics/performance-degradation/technical.md` | ✅ | Configurar thresholds por modelo. |
| alert-response-v1 | Func: `docs/compliance/bpmn/metrics/alert-response/functional.md`<br>Tech: `docs/compliance/bpmn/metrics/alert-response/technical.md` | ✅ | Conectar con ITSM real. |
| incident-response-rca-v1 | Func: `docs/compliance/bpmn/metrics/incident-response-rca/functional.md`<br>Tech: `docs/compliance/bpmn/metrics/incident-response-rca/technical.md` | ✅ | Añadir aprobación mitigaciones. |
| dataset-quality-v1 | Func: `docs/compliance/bpmn/metrics/dataset-quality/functional.md`<br>Tech: `docs/compliance/bpmn/metrics/dataset-quality/technical.md` | ✅ | Integrar catálogo datasets y issue tracker. |

## 5. Próximos pasos recomendados
1. **Verificación de despliegue real**: ejecutar `codeflowx.govern.workflow.engine` tras la reestructura siguiendo `docs/compliance/bpmn/BPMN_RUNTIME_VALIDATION_PLAN.md` y documentar evidencias.
2. **Actualizar users/prompts**: revisar manuales (`PROMPTS_*`, `MANUAL_DESARROLLADOR_*`, catálogos) para que referencien las nuevas rutas de BPMN y guías en `docs/compliance/...`.
3. **Completar guías AI‑OS pendientes** (agent/model/prompt approvals, deployment automation, retraining, evaluations) con el mismo formato funcional/técnico.
4. **Implementar clientes y tests** para los delegates que hoy tienen `@todo` (leka services, aio-telemetry, policy service, risk evaluator, ITSM, etc.) y registrar evidencia en este documento cuando estén listos.

