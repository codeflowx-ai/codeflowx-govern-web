# Prompt Approval v1 – Guía Técnica

## Artefactos
- **BPMN**: `processes/aios/prompt-approval-v1.bpmn`
- **Delegates**:
  - `PromptSafetyDelegate`
  - `AutoApprovePromptDelegate`
  - `RejectEthicsDelegate`
- **Reglas**: `rules/prompt/prompt-governance.drl` (sesión `prompt-approval-session`)
- **Fact**: `PromptApprovalFact`
- **Form**: `prompt_approval_human_override.zul`

## Variables
- `promptId`, `promptText`, `language`, `intent`.
- `toxicityScore`, `jailbreakRisk`, `piiLeakageScore`, `biasFindings`.
- `decision`, `requiresHumanReview`, `justification`, `ownerEmail`.
- `marketplaceEntryId`, `immutableLogId`.

## Integraciones
- **leka-llm-evaluation** / `prompt-safety` microservices.
- **Content Moderation APIs** (toxicity, hate speech, PII).
- **Prompt Marketplace / Catalog Service**.
- **ImmutableLogService` y `NotificationService`.

## Flujo técnico
1. `PromptSafetyDelegate` invoca todos los análisis y llena el `PromptApprovalFact`.
2. Drools decide la acción en base a umbrales configurables.
3. Si `requiresHumanReview`, se abre el formulario ZUL para justificar la decisión.
4. `AutoApprovePromptDelegate` publica el prompt en el marketplace y actualiza `promptStatus=APPROVED`.
5. `RejectEthicsDelegate` marca `promptStatus=REJECTED`, registra motivos y notifica.

## Pendientes
- Crear cliente consolidado para todos los análisis de seguridad (actualmente se manejan en el delegate).
- Añadir métricas y logging estructurado por tipo de riesgo.
- Escribir tests unitarios + Flowable (aprobado, rechazo, HITL).


