# COMPARATIVA PRECIOS COMPETENCIA IA – NOVIEMBRE 2025
## Contexto España / Europa

**Objetivo:** Justificar internamente la actualización de tarifas CodeflowX frente a precios de mercado de soluciones parciales (inferencia, agentes, consultoría) y suites de IA.

**Fecha:** Noviembre 2025  
**Fuentes:** Cloudiax, AI Works, Vidiv, Crata AI, MakeIA Solutions, IDC/BCG, Kit Digital, etc. Enlaces al final.

---

## 1. Referencias públicas precios (mensuales)

| Oferta | Tipo solución | Qué incluye | Precio público |
|--------|---------------|-------------|----------------|
| **Cloudiax – Llama 3.1 8B** | Inferencia LLM gestionada | Hosting modelo único | **999 €/mes** [Cloudiax](https://www.cloudiax.com/es/precios/) |
| **Cloudiax – Llama 3.3 70B** | Inferencia LLM gestionada | Hosting modelo grande | **3.499 €/mes** [Cloudiax](https://www.cloudiax.com/es/precios/) |
| **AI Works – Plan Profesional GPT-4o** | Acceso LLM + tokens + reporting | 30 M tokens/mes + soporte | **1.475 $/mes** (~1.380 €/mes) [AI Works](https://aiworks.es/soluciones.html) |
| **AI Works – Plan Crecimiento** | Acceso LLM | 15 M tokens/mes | **975 $/mes** (~910 €/mes) |
| **Vidiv – Agente voz avanzado** | Agente IA voz multilenguaje | Instalación 1.5k–5k € + NLU | **500–1.500 €/mes** + consumo [Vidiv](https://vidiv.com/2025/05/28/agentes-de-ia-por-voz-para-atencion-comercial-que-son-cuales-son-sus-beneficios-y-cuanto-cuestan/) |
| **Señor IA – Solución IA empresa** | Suite IA corporativa | 230 €/mes + 36 €/usuario | Ejemplo: empresa 10 usuarios → **590 €/mes** [Señor IA](https://xn--seoria-xwa.com/precios/) |
| **Consultoría IA (MakeIA)** | Servicios consultoría | 150 €/hora | Proyecto medio 80 h → **12.000 €** (one-shot) [MakeIA](https://makeiasolutions.com/plan-de-precios/) |
| **Crata AI – Quickstarter** | Programa adopción IA | Estrategia + roadmap | **5.900 €** (one-shot) [Crata AI](https://www.crata-ai.com/es/plans) |

**Observaciones:**
- Estos precios cubren **solo un componente** (inferencia, tokens, agente voz, consultoría inicial). No incluyen compliance EU AI Act ni gobierno completo.
- Clientes ya pagan >1.000 €/mes por hosting de un modelo o por acceso mensual a GPT-4o.
- Agentes específicos (voz) están en 500–1.500 €/mes sin compliance ni governance.

---

## 2. Benchmark corporativo / on-prem (competidores enterprise)

| Proveedor | Tipo oferta | Pricing público/referenciado | Comentarios |
|-----------|-------------|------------------------------|-------------|
| **IBM watsonx.governance** | Licencia enterprise + servicios | Packs desde ~180 k€/año (50k predicciones) + servicios | Requiere stack IBM/OpenShift; despliegue 6-9 meses; fuerte lock-in. |
| **Microsoft Azure AI Studio + Responsible AI** | Add-ons cloud + consumo | Pago por uso Azure + paquetes enterprise (NDA) | Solo clientes Azure; gobierno ligado a Purview/Synapse; sin controles RAG externos. |
| **DataRobot AI Trust Platform** | Plataforma MLOps + governance | Contratos Enterprise >150 k€/año (NDA) | Gobierno ligado al stack DataRobot; alto coste de consultoría. |
| **SAS Viya AI Governance** | Licencia perpetua + mantenimiento | Licencias desde 120 k€ + 20% soporte | Foco banca/sector público; adopción lenta, heavy consulting. |
| **Palantir AIP** | Plataforma integral IA/gobierno | Acuerdos multi-anuales >1 M€/año | Control extremo pero coste prohibitivo y dependencia Palantir. |

**Insight:** CodeflowX (24–48 k€/año self-host) entrega cumplimiento EU AI Act completo con despliegues 4-6x más rápidos y coste 4-10x inferior.

---

## 3. Benchmark sector público (España / UE)

| Proveedor | Oferta | Estimación precio | Comentarios |
|-----------|--------|-------------------|-------------|
| **Indra Minsait Trust AI** | Plataforma gobierno IA sector público | Proyectos 200–400 k€ (implantación + licencia) | Enfoque llave en mano; despliegue ≥9 meses; fuerte componente servicios. |
| **Atos Eviden AI Ethics** | Framework governance + consultoría | Contratos 150–300 k€/año | Dependencia servicios profesionales; sin SaaS modular. |
| **Accenture AI Compliance Factory** | Servicios compliance + plataforma | 200 k€ setup + 15 k€/mes operación | Metodología + servicios; coste recurrente elevado. |
| **Palantir AIP (sector público)** | Gobierno + data platform | +1 M€/año (defensa/seguridad) | Solo grandes administraciones; alto lock-in. |
| **Microsoft AI Governance sector público** | Azure + soluciones partner | Consumo Azure + consultoría 100–250 k€ | Requiere migrar a Azure; sin controles EU AI Act específicos. |

**Insight:** Nuestra gama Public (12–60 k€/año) se sitúa por debajo de los umbrales de licitación simplificada (<200 k€) y permite PoC en <6 semanas.

---

## 4. Benchmark frente a CodeflowX (nueva tarifa)

| Oferta CodeflowX | Precio | Cobertura | Diferenciador vs mercado |
|------------------|--------|-----------|-------------------------|
| **Cloud Compliance** | 149 €/mes | Cumplimiento básico (Art. 50, documentación Anexo IV básica) para **1 proyecto** (hasta 2 chatbots + 2 agentes + 2 RAG) | Entry-level competitivo; similar a SaaS básicos pero añade compliance EU AI Act. |
| **Cloud Governance** | **699 €/mes** | Cumplimiento + gobierno + evaluación + monitorización para **3 proyectos** (hasta 6 chatbots + 6 agentes + 6 RAG en total) | Precio comparable a hosting LLMs. Entrega suite completa vs soluciones parciales. |
| **Cloud Enterprise (5 clientes)** | **1.199 €/mes** | Multi-tenant marca blanca + suite completa para **5 proyectos** (uno por cliente externo; cada proyecto con 2/2/2 activos) | Menor que coste inferencia 70B (3.499 €/mes) o consultoría repetida. Justificado por valor multi-cliente. |
| **Corporate Standard (self-host)** | 24 k€/año | Suite completa on-prem | Alineado con licencias enterprise (>=20 k€/año). |
| **Corporate Enterprise** | 48 k€/año | Suite completa HA + roadmap | Comparables a proyectos >50 k€. |
| **Public Local** | 12 k€/año | Suite completa cloud dedicado | Adecuado a presupuestos públicos (incluye FRIA, reporting). |

**Conclusión:** con la subida, nuestras tarifas se mantienen por debajo de servicios parciales pero reflejan el valor de entregar la plataforma integral (cumplimiento + gobierno + evaluación + monitorización), sin incurrir en la percepción de “producto barato”.

---

## 5. ROI comparativo

| Concepto | Competencia (ejemplos) | Coste | Equivalencia CodeflowX |
|----------|------------------------|-------|------------------------|
| Hosting LLM (Llama 70B) | 3.499 €/mes | Solo inferencia | Cloud Governance (699 €/mes) con 3 proyectos (p.ej. chatbot soporte + agente interno + RAG compliance) + cumplimiento + monitorización. Ahorro 80 % + más valor. |
| Acceso GPT-4o + tokens | 1.380 €/mes | Solo uso modelo | Cloud Enterprise (1.199 €/mes) multi-tenant (5 proyectos/clients, cada uno con 2/2/2 activos) + compliance. Menos coste, más servicio. |
| Agente voz avanzado | 500–1.500 €/mes | Solo canal voz | Cloud Governance entrega múltiples canales + gobierno completo para 3 proyectos. |
| Consultoría compliance | 12.000 € proyecto | One-shot, sin plataforma | Cloud Governance 699 €/mes = 8.388 €/año; incluye herramienta viva y capacidad de hasta 3 proyectos simultáneos. |

**Mensaje comercial:** “Lo que pagas hoy solo por inferencia o consultoría, con CodeflowX obtienes gobernanza integral + cumplimiento EU AI Act + monitorización continua”.

---

## 6. Programas de ayuda / contexto gasto

- **Kit Digital** (España) → ayudas 25.000–29.000 € para medianas empresas (Red.es). [Kit Digital](https://cincodias.elpais.com/extras/2024-12-12/kit-digital-ofrece-ayudas-de-25000-o-29000-euros-a-las-medianas-empresas.html)
- **Gasto IA generativa en Europa** → >30.000 M€ en 2027 (IDC/BCG). [CIO](https://www.cio.com/article/2093027/el-gasto-europeo-en-soluciones-de-ia-generativa-superara-los-30-000-millones-en-2027.html)
- **Proyectos IA en España** → coste medio 8.000–25.000 € (q2bstudio). [Q2B Studio](https://www.q2bstudio.com/nuestro-blog/34881/)

**Traducción:** hay presupuesto asignado para IA. Nuestra tarifa ajustada aprovecha la disposición de pago y se situa dentro de rangos asumibles por ayudas públicas.

---

## 7. Posicionamiento recomendado

| Segmento | Competencia típica | Precio mercado | Tarifa CodeflowX | Mensaje clave |
|----------|--------------------|----------------|------------------|---------------|
| Microagencias | SaaS básicos (Señor IA, Vidiv) | 300–800 €/mes | 149 € (cumplimiento 1 proyecto) / 699 € (gobierno 3 proyectos) | “Por el precio de un agente parcial, gobiernas 2 chatbots + 2 agentes + 2 RAG con compliance garantizado.” |
| Mid-market | Hosting LLMs, consultoras | 1.000–3.500 €/mes | 699–1.199 €/mes | “Menos coste que solo inferencia grande. Incluye gobierno 3-5 proyectos, monitorización y compliance EU AI Act.” |
| Enterprise | Consultoras (proyectos >50 k€) | 12–150 k€/proyecto | 24–48 k€/año | “Coste anual competitivo vs proyectos puntuales. Plataforma viva + soporte.” |
| Sector público | Suites específicas | 15–80 k€/año | 12–60 k€/año | “Cumple ENS + EU AI Act en un único producto. Descuento early adopter.” |

---

## 8. Recomendaciones comerciales

1. **Justificar precio** con comparativa directa (tablas anteriores) en pitches.
2. **Enfatizar ROI**: compliance + gobierno evita proyectos de 12k€ cada vez + multas/escándalos.
3. **Ofrecer bundles** (Cloud Governance + activación módulo inferencia 90 €/mes + compute GPU repercutido) para quien quiera todo cerrado.
4. **Actualizar pricing deck** con estos datos y referencias.
5. **Revisar anual** según feedback clientes y evolución mercado.

---

## 📚 Fuentes

- Cloudiax: https://www.cloudiax.com/es/precios/
- AI Works: https://aiworks.es/soluciones.html
- Vidiv: https://vidiv.com/2025/05/28/agentes-de-ia-por-voz-para-atencion-comercial-que-son-cuales-son-sus-beneficios-y-cuanto-cuestan/
- Señor IA: https://xn--seoria-xwa.com/precios/
- MakeIA Solutions: https://makeiasolutions.com/plan-de-precios/
- Crata AI: https://www.crata-ai.com/es/plans
- Q2B Studio: https://www.q2bstudio.com/nuestro-blog/34881/
- Kit Digital (Red.es): https://cincodias.elpais.com/extras/2024-12-12/kit-digital-ofrece-ayudas-de-25000-o-29000-euros-a-las-medianas-empresas.html
- CIO.com: https://www.cio.com/article/2093027/el-gasto-europeo-en-soluciones-de-ia-generativa-superara-los-30-000-millones-en-2027.html
- IBM watsonx.governance (Partner Briefing Oct-2025)
- Microsoft Azure Responsible AI (Enterprise Pricing Guide v2.5)
- DataRobot AI Trust Platform (Forrester TEI 2024)
- SAS Viya AI Governance (Partner Rate Card 2025)
- Palantir AIP contratos UE (Diario Oficial UE 2024-2025)
- Indra Minsait licitaciones IA ética (Plataforma Contratación Estado 2024)
- Atos Eviden AI Ethics brochure (Sep-2025)
- Accenture AI Compliance Factory whitepaper (Q3 2025)
- Microsoft Sector Público AI Governance (Azure Public Sector playbook 2025)

---

**Documento preparado por:** Equipo Strategy & Pricing  
**Última actualización:** Noviembre 2025  
**Confidencialidad:** Uso interno únicamente
