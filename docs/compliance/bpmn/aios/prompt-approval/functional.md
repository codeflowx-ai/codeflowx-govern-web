# Prompt Approval v1 – Guía Funcional

## Objetivo
Evaluar y aprobar prompts (RAG/LLM) antes de su publicación, garantizando seguridad, ética y cumplimiento de políticas internas (mitigación de prompt injection, PII leakage, bias).

## Roles
- **Prompt Engineer / Owner**: propone el prompt y responde observaciones.
- **Security / Trust & Safety**: revisa inyecciones, fugas, contenido sensible.
- **Compliance/Ethics**: valida alineamiento con políticas, lenguaje inclusivo.
- **Human Reviewer (HITL)**: decide sobre prompts en zona gris.

## Flujo
1. **Start**: se registra `promptId`, `promptText`, `intent`.
2. **Service Task – Prompt Safety Check**: analiza:
   - Inyección de instrucciones.
   - Fugas de secretos/PII.
   - Bias/toxicidad.
3. **Business Rule Task – Prompt Scoring**:
   - `AUTO_APPROVE`
   - `HITL`
   - `AUTO_REJECT`
4. **User Task – Human Review** (solo si `HITL`): el revisor aprueba o rechaza.
5. **Service Tasks finales**:
   - `AutoApprovePrompt` → publica en el catálogo de prompts/marketplace.
   - `RejectPrompt` → notifica al owner.

## Variables
- `promptId`, `promptText`, `language`, `useCase`.
- `safetyFindings`, `toxicityScore`, `jailbreakRisk`, `piiLeakage`.
- `decision`, `justification`, `requiresHumanReview`, `ownerNotification`.

## SLA
- Evaluación automática: < 10 min.
- Revisión HUM: 24 h.
- Prompts críticos (high-risk domains) requieren revisión adicional por Compliance.

## Consideraciones
- Debe integrarse con el Marketplace para publicar/retirar prompts.
- Registrar todas las decisiones y evidencias en `ImmutableLog`.


