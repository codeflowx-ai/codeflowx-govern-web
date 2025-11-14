# COMPARATIVA PLATAFORMAS GOBIERNO IA · NOVIEMBRE 2025

## 1. Resumen ejecutivo

**Objetivo:** identificar posicionamiento competitivo frente a plataformas de gobierno de IA, soluciones no-code para agentes y proveedores de inferencia/modelos, con foco en pricing y cobertura funcional.

**Metodología:** revisión de fichas públicas y pricing (noviembre 2025) de los principales proveedores. Referencias incluidas al final.

---

## 2. Plataformas de gobierno, evaluación y monitorización

| Proveedor | Enfoque principal | Módulos clave | Modelo de pricing público | Observaciones |
|-----------|------------------|---------------|---------------------------|---------------|
| **CodeflowX** | Gobierno integral EU AI Act + monitoreo multi-entorno | Policy-as-code, evaluación LLM/RAG, Monitorización drift, auditoría Art. 71 | Planes Cloud 149–1.199 €/mes; self-host 24–48 k€/año (proyectos 2/2/2) | Diferencial en cumplimiento EU AI Act + infraestructura propia (inferencia/RAG). [CodeflowX](https://codeflowx.com/precios/) |
| **Credo AI** | Governanza y compliance para grandes corporaciones | AI registry, risk scoring, policy workflows, reporting regulator | Pricing a medida (enterprise, regulado) | Se posiciona como "AI Governance Platform" con foco banca/seguros. [Credo AI](https://www.credo.ai/platform) |
| **Holistic AI** | Gestión de riesgo y auditoría de IA | Evaluación riesgo, fairness, impacto derechos | Precio bajo NDA (paquetes audit + plataforma) | Fuerte en auditorías externas y consultoría asociada. [Holistic AI](https://www.holisticai.com/platform) |
| **Fiddler AI** | Observabilidad y explicabilidad de modelos | Monitorización drift, bias dashboards, explainable AI | Plan Enterprise (pricing on request) | Más orientado a MLOps clásico (tabulares). [Fiddler AI](https://www.fiddler.ai/product) |
| **Arthur AI** | Evaluación y monitoreo LLM + ML | Test de calidad, evaluación LLM, bias, drift | Arthur Shield (LLM) y Observability (ML) enterprise | Enfatiza controles LLM y métricas RAG. [Arthur AI](https://www.arthur.ai/platform) |
| **Lakera Guard** | Seguridad LLM y protección prompts | Detección prompt injection, filtrado contenido | Plan Free; Pro ± 199 CHF/mes; Enterprise a medida | Cobertura específica seguridad LLM, no gobierno completo. [Lakera](https://www.lakera.ai/guard) |
| **Weights & Biases** | MLOps experimentación + observabilidad | Tracking experimentos, evaluaciones, monitoreo | Teams desde 49 $/usuario/mes; Enterprise a medida | No cubre compliance; sí experiment tracking y eval. [Weights & Biases](https://wandb.ai/site/pricing) |

### 2.1. Proveedores corporate / on-premises (licencia anual)

| Proveedor | Oferta | Implementación | Pricing de referencia | Comentarios |
|-----------|--------|----------------|----------------------|-------------|
| **IBM watsonx.governance** | Governance sobre watsonx + modelos externos (lineage, bias, explicabilidad) | IBM Cloud Pak for Data, OpenShift, on-prem | Licencias a partir de ~180 k€/año (packs 50.000 predicciones) | Fuerte en regulado; requiere ecosistema IBM y servicios profesionales. |
| **DataRobot AI Trust Platform** | Risk management, documentación regulatoria, monitorización | Kubernetes/self-host + SaaS híbrido | Enterprise >150 k€/año (solo bajo NDA) | Incluye automatización ML; gobierno ligado a stack DataRobot. |
| **SAS Viya AI Governance** | Gestión ciclo de vida modelos, auditoría, reporting | SAS Viya (on-prem/Cloud) | Licencia perpetua + mantenimiento (desde 120 k€) | Foco financiero/público; heavy consulting. |
| **Microsoft Azure AI Studio + Responsible AI** | Responsible AI dashboard, content safety, policy packs | Azure nativo (Synapse, Purview, AI Studio) | Pago por uso Azure + addons Enterprise (NDA) | Solo para clientes cloud Azure; no cubre RAG fuera ecosistema. |
| **Palantir AIP (Artificial Intelligence Platform)** | Control extremo a extremo (data lineage, AI policy enforcement) | Implementación conjunta Palantir (cloud privado/soberano) | Contratos multi-anuales >1 M€/año | Governance profundo pero acoplado a plataforma Palantir. |

**Notas corporate:**
- Ninguno publica precios masivos; todos operan via NDA y proyectos de servicios.
- Requieren ecosistemas cerrados (IBM, SAS, Palantir), elevando coste total y lock-in.
- Puntos débiles: despliegue lento (6-12 meses) y foco en modelos tabulares clásicos más que en LLM/RAG.

**Conclusiones:**
- Plataformas de gobierno orientadas a enterprise venden contratos a medida y dependen de servicios profesionales; pocas comunican precios base.
- Proveedores corporate elevan el ticket (>120 k€/año) y obligan a adopción del stack propietario → oportunidad para CodeflowX con licencias self-host (24–48 k€/año) y despliegues rápidos.
- CodeflowX combina pricing transparente (SaaS escalonado + self-host) con compliance EU AI Act y módulos de inferencia propios → ventaja en negociación con agencias/consultoras y medianas empresas.
- Competidores como Lakera/Weights & Biases se centran en segmentos concretos (seguridad LLM o experiment tracking), no en gobierno completo.

---

## 3. Plataformas no-code para crear agentes/chatbots (precios públicos)

| Proveedor | Enfoque | Plan base (mensual) | Límite relevante | Observaciones |
|-----------|---------|---------------------|------------------|---------------|
| **Botpress** | Creación de chatbots/agents con IA generativa | Growth 99 USD/mes (fact. anual) | 10.000 mensajes/mes, 3 miembros equipo | Incluye handoff, analytics, connectors. [Botpress](https://botpress.com/pricing) |
| **Voiceflow** | Diseñar agentes conversacionales omnicanal | Pro 50 USD/editor/mes (fact. anual) | 500 ejecuciones prototipo/mes | Fuerte en colaboración diseño conversación. [Voiceflow](https://www.voiceflow.com/pricing) |
| **Landbot** | Automatización chat + formularios conversacionales | Pro 80 €/mes (fact. anual) | 5.000 conversaciones/mes | Integra web, WhatsApp, CRM. [Landbot](https://landbot.io/pricing) |
| **Manychat** | Automatización marketing conversacional | Pro desde 15 USD/mes | 500 contactos activos (escala por bloque) | Centrado en WhatsApp/Instagram DMs. [Manychat](https://manychat.com/pricing) |
| **Stack AI** | Agentes no-code usando LLMs | Builder 99 USD/mes | 3 agentes activos, 25k mensajes | Incluye workflows RAG básicos. [Stack AI](https://www.stack-ai.com/pricing) |
| **Flowise Cloud** | Orquestración visual sobre LangChain | Starter 49 USD/mes | 2 instancias, 50k ejecuciones | Buena para prototipos RAG sin código. [Flowise](https://flowiseai.com/pricing) |

**Insights:**
- Ticket medio 49–99 USD/mes → refuerza la necesidad de upsell a gobierno/compliance (nuestro Cloud Governance 699 €/mes = 7× ticket no-code).
- Limitaciones fuertes en mensajes/contactos → las agencias que escalan rápidamente necesitan gobernanza externa (CodeflowX) para control multi-proyecto.

---

## 4. Proveedores de inferencia y modelos (2025)

| Proveedor | Modelos destacados | Pricing de referencia | Observaciones |
|-----------|--------------------|-----------------------|---------------|
| **OpenAI** | GPT-4o, GPT-4o mini, GPT-4.1, Embeddings `text-embedding-3` | GPT-4o: 5 USD / 1M tokens entrada, 15 USD / 1M tokens salida; GPT-4o mini: 0,15 / 0,60 USD | API global, requiere BYO compliance. [OpenAI](https://openai.com/api/pricing/) |
| **Anthropic** | Claude 3.5 Sonnet, Claude 3.5 Haiku | 3 USD / 1M tokens entrada, 15 USD / 1M tokens salida (Sonnet) | Integrado en AWS Bedrock/Azure; política uso responsable estricta. [Anthropic](https://www.anthropic.com/pricing) |
| **Google Vertex (Gemini)** | Gemini 1.5 Pro, Gemini 1.5 Flash | 3,50 USD / 1M tokens entrada, 10 USD / 1M tokens salida (Gemini 1.5 Pro) | Incluye control de seguridad y grounding. [Google Vertex AI](https://cloud.google.com/vertex-ai/pricing) |
| **AWS Bedrock** | Claude 3, Llama 3, Mistral, Amazon Titan | Claude 3 Sonnet: 0,003 USD entrada / 0,015 USD salida por 1K tokens (us-east-1) | Facturación consolidada AWS, integración IAM/S3. [Amazon Bedrock](https://aws.amazon.com/bedrock/pricing/) |
| **Mistral AI** | Mistral Large, Mistral Small, Codestral | Mistral Large: 8 USD / 1M tokens, Small: 2 USD / 1M tokens | API europea, soporte self-host vía `mistral-inference`. [Mistral](https://mistral.ai/pricing/) |
| **Together AI** | Mixtral, Llama Guard, Qwen | Ej. `mixtral-8x7b` 0,60 USD / 1M tokens entrada, 0,60 USD salida | Ofrece inferencia multi-cloud + finetuning. [Together AI](https://www.together.ai/pricing) |
| **Cohere** | Command R+, Embed v3 | Command R+: 3 USD / 1M tokens entrada, 15 USD salida | Orientado a enterprise y seguridad datos. [Cohere](https://cohere.com/pricing) |

**Observaciones:**
- Precios convergen en rangos 3–8 USD / 1M tokens entrada para modelos avanzados → refuerza propuesta de ahorro CodeflowX al optimizar uso (routing, fallback, caching) desde `leka-llm-interpreter`.
- Muchos proveedores ofrecen integración con AWS/Azure/GCP → es crítico mantener conectores listos (PROMPTS_11).

---

## 5. Implicaciones para CodeflowX

1. **Diferenciación:** Somos de los pocos con pricing transparente y foco en compliance EU AI Act + infraestructura propia. Usar comparativa en pitches enterprise.
2. **Upsell:** Clientes de plataformas no-code (~99 USD/mes) representan pipeline ideal para migrar a Gobierno 699 €/mes cuando gestionan múltiples agentes.
3. **Alianzas:** Mantener conectores nativos con OpenAI, Anthropic, Gemini, Mistral y Together AI acelera integraciones (continuar PROMPTS_11/12).
4. **Mensaje comercial:** “CodeflowX = capa de gobierno + monitoreo sobre cualquiera de estas herramientas (no-code, inferencia).”

---

## 📚 Fuentes

- CodeflowX: https://codeflowx.com/precios/
- Credo AI: https://www.credo.ai/platform
- Holistic AI: https://www.holisticai.com/platform
- Fiddler AI: https://www.fiddler.ai/product
- Arthur AI: https://www.arthur.ai/platform
- Lakera Guard: https://www.lakera.ai/guard
- Weights & Biases: https://wandb.ai/site/pricing
- Botpress: https://botpress.com/pricing
- Voiceflow: https://www.voiceflow.com/pricing
- Landbot: https://landbot.io/pricing
- Manychat: https://manychat.com/pricing
- Stack AI: https://www.stack-ai.com/pricing
- Flowise Cloud: https://flowiseai.com/pricing
- OpenAI API Pricing: https://openai.com/api/pricing/
- Anthropic Pricing: https://www.anthropic.com/pricing
- Google Vertex AI Pricing: https://cloud.google.com/vertex-ai/pricing
- Amazon Bedrock Pricing: https://aws.amazon.com/bedrock/pricing/
- Mistral AI Pricing: https://mistral.ai/pricing/
- Together AI Pricing: https://www.together.ai/pricing
- Cohere Pricing: https://cohere.com/pricing
