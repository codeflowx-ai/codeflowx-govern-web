# CODEFLOWX · GOBERNANZA SIN MIGRACIONES
## Integración nativa con agentes existentes (n8n, ChatGPT, Claude, Databricks)

**Fecha:** Noviembre 2025  
**Uso:** Equipo comercial / partners técnicos  
**Objetivo:** Explicar por qué la API pública de CodeflowX + pipeline de gobierno ofrece una ventaja competitiva única frente a plataformas que obligan a migrar agentes.

---

## 1. Resumen ejecutivo
- **Problema:** el 80% de los clientes enterprise ya tienen agentes en producción (n8n, ChatGPT API, Claude, Databricks) y no quieren migrarlos.
- **Solución CodeflowX:** capa de gobierno que ingiere cada interacción (REST/MCP/SDK) y aplica compliance EU AI Act sin tocar la infraestructura existente.
- **Resultado:** despliegues en días (no meses), ROI inmediato y gobierno transversal sobre cualquier modelo (propio o comercial).

---

## 2. Cómo funciona (arquitectura)
```
Agente existente (n8n, chatbot, Claude API)
          │  POST /governance/events
          ▼
┌──────────────────────────────┐
│ codeflowx-governance-api     │  (REST + Webhooks + MCP)
│  • Valida + persiste evento  │
│  • Publica en RabbitMQ       │
└──────────────┬───────────────┘
               │
         RabbitMQ `governance.events`
               │
┌──────────────▼───────────────┐
│ Micros IA (Python)           │
│  • Evaluación LLM            │
│  • Bias, Toxicidad, DPIA     │
│  • RAG Quality, FRIA         │
└──────────────┬───────────────┘
               │
         RabbitMQ `governance.decisions`
               │
┌──────────────▼───────────────┐
│ API Java – Webhooks / SDK    │
│  • Devuelve alertas          │
│  • Actualiza Databricks ML   │
│  • Genera Anexo IV/V/VI      │
└──────────────────────────────┘
```

---

## 3. Conectores disponibles

| Integración | Cómo se conecta | Qué aporta | Estado |
|-------------|-----------------|------------|--------|
| **n8n / Zapier / Make** | HTTP node → `POST /api/v1/governance/events` | Gobierno sin rehacer workflow | Listo (REST + webhooks) |
| **ChatGPT / Claude / Gemini API** | SDK CodeflowX (Java/Python/TS) envuelve llamada | Registro completo, tokens, contexto | Listo (SDK generado OpenAPI) |
| **Databricks MLflow** | Conector PROMPTS_12 sincroniza modelos y deployments | Bloqueo despliegue si no hay aprobación CodeflowX | En curso (Prompts 12) |
| **Snowflake / Lakehouse** | Metadata + linaje via connectors | FRIA + Art. 10 (datasets) | En curso |
| **VSCode / Open Interpreter (MCP)** | Adaptador MCP Java (`/mcp`) | Proyectos internos piden evaluaciones sin REST | Diseño Grupo D PROMPTS_05 |

---

## 4. Ventajas clave frente a la competencia

| Reto del cliente | Plataformas tradicionales | CodeflowX |
|------------------|---------------------------|-----------|
| Mantener agentes en n8n/ChatGPT | Obligan a migrar al builder propietario | Plug & play: endpoint REST + SDK |
| Cumplir EU AI Act Art. 10-15 | Requieren consultoría manual | Pipeline automático (evaluaciones, FRIA, anexos) |
| Gobierno multi-modelo (OpenAI, Anthropic, Mistral) | Suelen soportar 1-2 proveedores | Agnóstico (via `leka-llm-interpreter`) |
| Auditoría Art. 71 (Databricks / Snowflake) | No cubren, piden export manual | Conectores PROMPTS_12 → registros automáticos |
| Tiempo de despliegue | 3-6 meses | 2-3 semanas (sin migraciones) |

---

## 5. Casos de uso

1. **Banco con agentes de atención (n8n + ChatGPT):**
   - Añade nodo HTTP final que envía evento a CodeflowX.
   - Recibe alertas en ServiceNow si hay desviaciones o incidentes (webhook).
   - Documentación Art. 13 y FRIA listas para auditoría.

2. **Consultora que usa Databricks para entrenar modelos:**
   - CodeflowX sincroniza MLflow y sólo permite despliegue si la evaluación LLM/bias es positiva.
   - Reportes Anexo IV y evidencia Art. 43 disponibles en PDF.

3. **Admin pública con bots en ChatGPT Team:**
   - Exporta conversaciones y las sube a `POST /projects/{id}/evaluate` para trazabilidad oficial.
   - Webhook notifica a equipo legal cuando se detectan datos personales sensibles.

---

## 6. Mensajes comerciales
- **“No reescribas tus agentes, gobierna lo que ya tienes.”**
- **“CodeflowX se sienta encima de n8n, ChatGPT o Databricks. Añade cumplimiento EU AI Act sin cambiar tu stack.”**
- **“Conectamos, evaluamos, auditamos y devolvemos veredictos en tiempo real.”**
- **“Somos la única plataforma que combina conectores Databricks/Snowflake + pipeline EU AI Act + webhooks para n8n.”**

---

## 7. Datos de referencia
- 65% de nuestros prospectos usan n8n o Zapier para orquestar agentes.
- Databricks + Snowflake concentran >40% de pipelines ML enterprise (fuente: IDC 2025).
- EU AI Act (Art. 71) exige registro detallado → CodeflowX genera bundle automático desde la API.
- Competidores (Credo AI, Holistic AI) requieren migraciones o consultoría intensiva.

---

## 8. Pricing & upsell
- **Entry:** Cloud Compliance 149 €/mes (1 proyecto = 2 bots + 2 agentes + 2 RAG) para clientes que sólo necesitan trazabilidad básica.
- **Mid-market:** Cloud Governance 699 €/mes (3 proyectos) ideal para agencias que ya operan varios agentes.
- **Enterprise:** Cloud Enterprise 1.199 €/mes (5 proyectos multi-tenant) + conectores Databricks/Snowflake (PROMPTS_12) + módulo inferencia gestionado.
- **Add-ons:** proyecto extra (120 €/mes), clientes OEM extra (150 €/mes por bloque), activación módulo inferencia (90 €/mes + compute real).

---

## 9. Próximos pasos comerciales
1. Incluir slide “Governance Overlay” en las demos.
2. Ofrecer PoC express: integrar un flujo n8n en <48 h.
3. Paquete conjunto con Databricks → se sincroniza MLflow + pipeline gobierno.
4. Preparar testimoniales (consultora + banco) destacando que no migraron infraestructura.

---

## 📚 Referencias
- PROMPTS_05 Grupo D – Microservicio `codeflowx-governance-api`
- PROMPTS_12 – Conectores Databricks, Snowflake, Azure ML, SageMaker
- PROMPTS_14 – Tablas `govgovernanceevents`, `govwebhooksubscriptions`, `govgovernanceresults`
- Documento comercial `COMPARATIVA_PLATAFORMAS_GOBIERNO_IA_NOV2025`

