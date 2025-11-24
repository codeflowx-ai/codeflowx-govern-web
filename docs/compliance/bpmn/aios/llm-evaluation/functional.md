# LLM Evaluation v1 – Guía Funcional

## Objetivo
Evaluar modelos de lenguaje grandes (LLM propietarios o de terceros) antes de habilitarlos en AI‑OS, midiendo capacidades, seguridad y cumplimiento (prompt injection, toxicidad, factualidad).

## Roles
- **LLM Engineer**: lanza la evaluación y revisa resultados.
- **Security / Trust & Safety**: revisan métricas de seguridad.
- **Product Owner**: valida la adecuación al caso de uso.

## Flujo
1. **Start** – se especifica `llmId`, `provider`, `modelo base`, `config`.
2. **Prepare Evaluation**: selecciona dataset de pruebas, prompts, escenarios.
3. **Execute Evaluation**:
   - Benchmarks (MMLU, TruthfulQA, etc.).
   - Tests de seguridad (prompt injection, jailbreak, PII).
   - Métricas de coste/latencia.
4. **Store Results** – persiste métricas y reportes.
5. **Business Rule Task** – decide `APPROVE`, `HITL`, `REJECT`.
6. **User Task (opcional)** – revisión humana en casos borderline.
7. **End** – notificar al solicitante y, si se aprueba, habilitar el LLM para `agent/prompt`.

## Variables
- `llmId`, `provider`, `region`, `capabilities`.
- `benchmarkScores`, `securityFindings`, `latencyStats`, `costEstimate`.
- `decision`, `justification`, `requiresHumanReview`.

## SLA
- Evaluación automática puede tardar horas dependiendo del tamaño del suite.
- Revisión humana: 48 h.

## Consideraciones
- Si el LLM es externo (API), se deben almacenar los resultados y configuración para auditoría.
- Las conclusiones alimentan `external-model-approval` y `prompt-approval`.


