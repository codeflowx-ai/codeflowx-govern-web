# LLM Evaluation v1 – Guía Técnica

## Artefactos
- **BPMN**: `processes/aios/llm-evaluation-v1.bpmn`
- **Delegates**:
  - `PrepareLlmEvaluationDelegate`
  - `ExecuteLlmEvaluationDelegate`
  - `SaveLlmEvaluationResultsDelegate`
  - `AutoApproveLlmEvaluationDelegate`
  - `ManualApproveLlmEvaluationDelegate`
  - `RejectLlmEvaluationDelegate`
  - `NotifyLlmEvaluationDelegate`
- **Reglas**: `rules/aios/evaluation/llm-evaluation.drl`

## Variables
- `llmId`, `provider`, `region`, `evaluationProfile`.
- `benchmarkResults`, `safetyFindings`, `latencyMetrics`, `costPerToken`.
- `decision`, `justification`, `requiresHumanReview`.
- `reportUrl`, `immutableLogId`.

## Integraciones
- **leka-llm-evaluation** micro (ejecuta benchmarks y tests de seguridad).
- **External LLM providers** (OpenAI, Anthropic, etc.) – se requiere credencial segura.
- **Storage** para reportes (S3/MinIO).
- **Notification/Issue tracking** para alertar a los stakeholders.

## Flujo técnico
1. `PrepareLlmEvaluationDelegate` construye el plan (prompts, datasets, límites).
2. `ExecuteLlmEvaluationDelegate` orquesta las llamadas al micro evaluator / providers.
3. Resultados se empaquetan y pasan a `SaveLlmEvaluationResultsDelegate`.
4. Reglas determinan si se aprueba automáticamente o requiere HITL.
5. Delegates finales actualizan catálogos y notifican.

## Pendientes
- Alinear métricas con `AIOS_API_OVERVIEW` (capabilities, safety tiers).
- Añadir soporte para evaluación continua (periódica) con `ai-runtime-health`.
- Crear tests automatizados (mock de `leka-llm-evaluation`).


