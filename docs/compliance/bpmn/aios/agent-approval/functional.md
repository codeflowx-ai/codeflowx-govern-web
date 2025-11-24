# Agent Approval v1 – Guía Funcional

## Objetivo
Garantizar que cualquier agente de IA que vaya a operar en producción cumpla con los requisitos de riesgo, cumplimiento normativo y ética establecidos por AI‑OS y por el EU AI Act (Art. 9, 13 y 14).

## Roles
- **Product Owner / Solicitan­te**: inicia la solicitud con la información del agente.
- **Equipo de Riesgos**: ejecuta la evaluación técnica de amenazas/impactos.
- **Equipo de Cumplimiento**: verifica alineación con políticas y normativas.
- **Comité Ético / Human‑in‑the‑Loop**: revisa casos sensibles.

## Flujo resumido
1. **Inicio**: se registra el agente (`agentId`, `agentName`, `owner`, `useCase`).
2. **Gateway paralelo**:
   - `Risk Assessment` → calcula `riskScore`, `riskCategory`.
   - `Compliance Check` → valida reglas, datos, políticas.
   - `Ethics Review` → analiza sesgos y controles de supervisión humana.
3. **Join** de las tres evaluaciones.
4. **Business Rule Task (Agent Scoring)**: Drools determina una de tres salidas:
   - `AUTO_APPROVE`
   - `HITL_REQUIRED`
   - `AUTO_REJECT`
5. **User Task – Human Review** (solo si `HITL_REQUIRED`): el comité aprueba o rechaza.
6. **Service Tasks finales**:
   - `AutoApproveAgent` → marca el agente como aprobado y registra en `ImmutableLog`.
   - `AutoRejectAgent` → notifica y cierra la solicitud.

## Variables clave
- Entradas: `agentId`, `agentName`, `owner`, `capabilities`, `sensitiveData`.
- Resultados de evaluación: `riskScore`, `complianceScore`, `ethicsScore`, `riskCategory`.
- Salidas: `decision` (`AUTO_APPROVE`, `HITL_REQUIRED`, `AUTO_REJECT`), `confidenceLevel`, `justification`, `requiresHumanReview`.

## SLA y tiempos
- Evaluaciones automáticas: < 30 minutos.
- Revisión HUM (HITL): máximo 48 h.
- Publicación del agente aprobado: al finalizar el proceso.

## Integraciones
- `leka-bias-detection` y `leka-llm-evaluation` para validaciones técnicas.
- `AIOS API` para registrar agentes y sus capacidades.
- `ImmutableLog` para trazabilidad completa.

## Puntos de atención / próximos pasos
- Actualizar este flujo con los nuevos servicios de AI‑OS Runtime (telemetría, policy enforcement).
- Incluir validaciones de “Prompt Injection / Jailbreak” cuando el agente interactúe con LLMs externos.


