# CASOS DE USO AI ACT - DESARROLLO, AGENCIAS Y CONSULTORES
## Guía Comercial para Empresas que Desarrollan Soluciones IA para Terceros

**Fecha:** Noviembre 2025  
**Versión:** 1.0  
**Audiencia:** Empresas desarrollo software, agencias marketing, consultores, integradores  
**Objetivo:** Clarificar obligaciones AI Act para quien desarrolla chatbots, agentes, RAG, asistentes usando modelos comerciales/open source

---

## 🎯 PERFIL TARGET: ¿A QUIÉN VA DIRIGIDO?

### **Empresas Target:**

```
✅ Software houses que desarrollan soluciones IA
✅ Agencias marketing digital con servicios IA
✅ Consultores/freelancers desarrollo chatbots
✅ Integradores que implementan IA para clientes
✅ Startups IA B2B (SaaS chatbots, agentes, RAG)
✅ Agencias conversacionales (WhatsApp, Telegram bots)
✅ System integrators con práctica IA
```

### **Tecnologías que Usan:**

**Modelos Comerciales API:**
- OpenAI (GPT-4, GPT-3.5)
- Anthropic (Claude)
- Google (Gemini)
- Cohere
- Azure OpenAI Service

**Modelos Open Source:**
- LLaMA 2/3 (Meta)
- Mistral 7B/8x7B
- Falcon
- BLOOM
- Vicuna, Alpaca

**Frameworks/Herramientas:**
- LangChain, LlamaIndex
- Pinecone, Qdrant, Weaviate (vector DBs)
- Streamlit, Gradio (UIs)
- FastAPI, Flask (APIs)

### **Soluciones que Desarrollan:**

```
🤖 Chatbots atención cliente
🧠 Agentes IA autónomos
📚 RAG (Retrieval Augmented Generation)
🛍️ Asistentes compras/ventas
📊 Analizadores feedback/sentimiento
📞 Bots WhatsApp/Telegram
🎓 Asistentes educativos
💼 Asistentes internos empresas
```

---

## ⚠️ MENSAJE CLAVE PARA ESTE TARGET

```
┌─────────────────────────────────────────────────────────────────┐
│                                                                 │
│  "Aunque uses GPT-4 API o LLaMA open source, si desarrollas   │
│   y vendes/entregas solución IA a cliente, TÚ eres PROVEEDOR" │
│                                                                 │
│   → Tienes obligaciones Art. 16-21 (no solo tu cliente)       │
│   → Deadline: 2 Agosto 2026 (9 meses)                         │
│   → Multas: hasta 35M€ o 7% facturación                       │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### **Error Común:**

```
❌ PENSAMIENTO ERRÓNEO:
"Uso GPT-4 API, OpenAI cumple AI Act, yo no tengo obligaciones."

✅ REALIDAD:
"Desarrollas sistema IA (chatbot) con GPT-4 como componente.
Lo entregas a cliente final. TÚ eres PROVEEDOR del sistema chatbot.
Tienes obligaciones Art. 16-21 si sistema es alto riesgo."
```

---

## 📊 MATRIZ DE CLASIFICACIÓN RÁPIDA

| Caso Uso | ¿Alto Riesgo? | Proveedor | Deadline | Criticidad |
|----------|---------------|-----------|----------|------------|
| **Chatbot ecommerce recomendador** | ⚠️ Depende | Agencia | 2 Ago 2026 | 🟡 MEDIA |
| **Chatbot atención cliente FAQ** | ❌ NO (riesgo limitado) | Agencia | 2 Ago 2026 | 🟢 BAJA |
| **Agente IA scoring crediticio** | ✅ SÍ (Anexo III.5.a) | Agencia | 2 Ago 2026 | 🔴 CRÍTICA |
| **RAG documentación interna** | ❌ NO (mínimo) | Consultor | N/A | 🟢 BAJA |
| **Chatbot screening RR.HH.** | ✅ SÍ (Anexo III.4.a) | Software house | 2 Ago 2026 | 🔴 CRÍTICA |
| **Asistente compras ecommerce** | ⚠️ Depende | Agencia | 2 Ago 2026 | 🟡 MEDIA |
| **Bot WhatsApp soporte** | ❌ NO (riesgo limitado) | Consultor | 2 Ago 2026 | 🟢 BAJA |
| **Agente IA detección fraude** | ✅ SÍ (Anexo III.5.a) | Software house | 2 Ago 2026 | 🔴 CRÍTICA |
| **RAG legal bufete** | ✅ SÍ (Anexo III.8) | Consultor | 2 Ago 2026 | 🔴 CRÍTICA |
| **Chatbot educativo tutorías** | ✅ SÍ (Anexo III.3) | Startup | 2 Ago 2026 | 🔴 CRÍTICA |

---

## 🤖 CASO 1: CHATBOT ECOMMERCE ATENCIÓN CLIENTE (FAQ)

### **Descripción:**

**Cliente:** Tienda online moda  
**Proveedor:** Agencia marketing "Digital Boost"  
**Solución:** Chatbot web atención cliente 24/7  
**Tecnología:** GPT-3.5 API + LangChain + Pinecone (FAQ vectorizadas)  
**Funcionalidad:**
- Responde preguntas frecuentes (envíos, devoluciones, tallas)
- Búsqueda productos catálogo
- Estado pedidos (consulta, no modificación)

### **Clasificación Riesgo:**

❌ **NO Alto Riesgo** → Riesgo limitado (Art. 50 transparencia)

**Razón:** Chatbot FAQ no toma decisiones significativas. Solo informa. No afecta Anexo III categorías.

### **Obligaciones PROVEEDOR (Agencia):**

| Obligación | Artículo | Descripción |
|-----------|----------|-------------|
| **Transparencia chatbot** | Art. 50.1 | Usuario debe saber que interactúa con IA |
| **Identificación generación IA** | Art. 50.2 | Si chatbot genera contenido, marcarlo como IA |
| **Documentación básica** | Buena práctica | Manual uso cliente |

**Esfuerzo:** 1-2 días (añadir disclaimer "Este es un asistente IA")

### **Obligaciones DEPLOYER (Tienda online):**

| Obligación | Artículo | Descripción |
|-----------|----------|-------------|
| **Transparencia usuarios** | Art. 50.1 | "Estás chateando con asistente IA" |
| **Supervisión contenido** | Buena práctica | Revisar respuestas chatbot periódicamente |

### **Argumentación Comercial:**

```
MENSAJE AGENCIA:
"Tu chatbot FAQ ecommerce es RIESGO LIMITADO (Art. 50).
No es alto riesgo → compliance SENCILLO.

Obligaciones:
✅ Transparencia: añadir disclaimer 'Asistente IA'
✅ Documentación: manual uso cliente

Esfuerzo: 1-2 días (trivial)
Coste: <500€

CodeflowX valor:
- Template disclaimers Art. 50 (varios idiomas)
- Checklist compliance riesgo limitado
- Documentación automatizada

Precio licencia: 1.200€/año (básica)
ROI: Tranquilidad compliance + professional image"
```

---

## 🛍️ CASO 2: ASISTENTE VIRTUAL COMPRAS PERSONALIZADO

### **Descripción:**

**Cliente:** Amazon España / El Corte Inglés  
**Proveedor:** Software house "SmartRetail AI"  
**Solución:** Asistente IA recomendación productos personalizada  
**Tecnología:** GPT-4 API + sistema recomendación + histórico compras  
**Funcionalidad:**
- Recomienda productos basado en perfil usuario
- Sugiere compras complementarias
- Personaliza ofertas/descuentos
- "Comprar por ti" (con autorización)

### **Clasificación Riesgo:**

⚠️ **DEPENDE CONTEXTO:**

**Escenario A:** Solo recomendación (usuario decide)  
→ ❌ NO alto riesgo (riesgo limitado Art. 50)

**Escenario B:** Decisión automática scoring usuario para acceso ofertas exclusivas  
→ ✅ Alto riesgo (Anexo III.5.a evaluación crediticia)

**Escenario C:** Sistema afecta acceso servicios esenciales  
→ ✅ Alto riesgo (Anexo III.5.a)

### **Asumimos Escenario A (más común): NO alto riesgo**

### **Obligaciones PROVEEDOR (Software house):**

| Obligación | Artículo | Descripción |
|-----------|----------|-------------|
| **Transparencia recomendaciones IA** | Art. 50.1 | "Recomendaciones generadas por IA" |
| **Explicabilidad básica** | Buena práctica | "Por qué te recomendamos esto" |
| **No perfilado discriminatorio** | GDPR Art. 22 | No decisiones automatizadas discriminatorias |
| **Documentación uso** | Buena práctica | Manual cliente |

**Esfuerzo:** 2-3 días

### **Si fuera Escenario B/C (Alto Riesgo):**

| Obligación | Artículo | Esfuerzo |
|-----------|----------|----------|
| Sistema gestión riesgo | Art. 9 | 2 semanas |
| Data governance + bias detection | Art. 10 | 1-2 semanas |
| Documentación Anexo IV | Art. 11 | 3-5 días |
| Logging inmutable | Art. 12 | 1 semana |
| Transparencia + explicabilidad | Art. 13 | 2-3 días |
| Supervisión humana | Art. 14 | 1 semana |
| Evaluación conformidad | Anexo VI | 2-4 semanas |

**Esfuerzo:** 11-15 semanas

### **Argumentación Comercial:**

```
MENSAJE SOFTWARE HOUSE:
"Tu asistente compras personalizado clasificación CRÍTICA:

¿SOLO RECOMIENDA (usuario decide final)?
→ Riesgo limitado (Art. 50) → compliance 2-3 días

¿DECIDE AUTOMÁTICAMENTE acceso ofertas/crédito?
→ Alto riesgo (Anexo III.5.a) → compliance 11-15 semanas

EVALÚA CON CLIENTE FINAL:
- ¿Asistente solo sugiere o ejecuta compras automáticamente?
- ¿Scoring usuario para ofertas exclusivas?
- ¿Afecta acceso servicios/crédito?

Si ALTO RIESGO:
CodeflowX esencial:
✅ Bias detection (no discriminación ofertas)
✅ Documentación Anexo IV automatizada
✅ Explicabilidad recomendaciones
✅ Logging inmutable decisiones

Esfuerzo: 11-15 semanas con CodeflowX vs 50-70 sem manual
Ahorro: 120.000€
Deadline: 2 Agosto 2026 (9 meses)"
```

---

## 💼 CASO 3: AGENTE IA SCREENING INICIAL RR.HH.

### **Descripción:**

**Cliente:** Consultora RR.HH. "TalentHub"  
**Proveedor:** Software house "HireSmart AI"  
**Solución:** Agente IA análisis CVs + screening inicial candidatos  
**Tecnología:** GPT-4 API + LangChain + análisis estructurado CVs  
**Funcionalidad:**
- Analiza CV candidato vs requisitos puesto
- Scoring idoneidad candidato (0-100)
- Genera shortlist candidatos recomendados
- Preguntas automatizadas screening inicial

### **Clasificación Riesgo:**

✅ **ALTO RIESGO** (Anexo III.4.a - selección/reclutamiento trabajadores)

**Criticidad:** 🔴 **CRÍTICA** (sesgo demográfico, discriminación)

### **Obligaciones PROVEEDOR (Software house):**

| Obligación | Artículo | Deadline | Esfuerzo Manual | Con CodeflowX |
|-----------|----------|----------|----------------|---------------|
| Sistema gestión riesgo | Art. 9 | 2 Ago 2026 | 8-12 semanas | 2 semanas |
| Data governance + bias detection | Art. 10 | 2 Ago 2026 | 8-10 semanas | 1-2 semanas |
| Documentación técnica Anexo IV | Art. 11 | 2 Ago 2026 | 6-8 semanas | 3-5 días |
| Logging inmutable | Art. 12 | 2 Ago 2026 | 4-6 semanas | 1 semana |
| Transparencia candidatos | Art. 13 | 2 Ago 2026 | 3-4 semanas | 2-3 días |
| Supervisión humana | Art. 14 | 2 Ago 2026 | 4-6 semanas | 1 semana |
| Accuracy + fairness | Art. 15 | 2 Ago 2026 | 6-8 semanas | 2 semanas |
| Sistema gestión calidad | Art. 17 | 2 Ago 2026 | 8-12 semanas | 2 semanas |
| Evaluación conformidad | Anexo VI | 2 Ago 2026 | 12-16 semanas | 2-4 semanas |
| Declaración UE | Anexo V | 2 Ago 2026 | 2-3 semanas | 2-3 días |
| Registro Art. 71 DB | Art. 71 | 2 Ago 2026 | 2 semanas | 1 día |

**TOTAL:** 63-95 semanas (15-23 meses) | Con CodeflowX: 11-15 semanas (2.5-3.5 meses)

### **CRITICIDAD ESPECIAL RR.HH.:**

```
🔴 Sesgo demográfico → Discriminación ilegal (género, edad, etnia, origen)
🔴 GDPR Art. 22 → Derecho no decisión 100% automatizada
🔴 Transparencia candidatos → Deben saber que IA analiza CV
🔴 Explicabilidad → Candidato rechazado puede pedir razones
🔴 Supervisión humana → Reclutador SIEMPRE decide final
```

### **Obligaciones DEPLOYER (Consultora RR.HH.):**

| Obligación | Artículo | Descripción |
|-----------|----------|-------------|
| Verificar conformidad proveedor | Art. 26.10 | Pedir Declaración UE + Anexo IV a HireSmart |
| Supervisión humana efectiva | Art. 26.5 | Reclutador revisa SIEMPRE antes descartar candidato |
| Informar candidatos | Art. 26.7 | "Sistema IA analiza tu CV" |
| Monitorización bias | Art. 26.2 | Detectar sesgo género/edad en scoring |

### **Argumentación Comercial:**

```
MENSAJE SOFTWARE HOUSE:
"Tu agente IA screening RR.HH. = caso MÁS SENSIBLE AI Act.

RIESGOS CRÍTICOS:
🔴 Sesgo demográfico → Discriminación ilegal
🔴 Demandas candidatos rechazados
🔴 Multas: hasta 35M€
🔴 Escándalo reputacional (prensa: 'IA discrimina mujeres')

SIN COMPLIANCE:
- Cliente puede DEMANDARTE si candidato denuncia discriminación
- TÚ respondes como PROVEEDOR (Art. 16-21)
- Cliente responde como DEPLOYER (Art. 26-29)
- Cadena responsabilidad COMPLETA

CodeflowX ESENCIAL:
✅ leka-bias-detection: 20+ métricas fairness (género, edad, etnia)
✅ Documentación Anexo IV: explica mitigación sesgo
✅ Explicabilidad: genera razones scoring candidato
✅ Transparencia: plantillas notificación candidatos Art. 13
✅ HITL workflows: supervisión humana documentada
✅ Audit trail completo: evidencia no discriminación

Deadline: 2 Agosto 2026 (9 meses)
Esfuerzo: 11-15 semanas con CodeflowX vs 63-95 semanas manual
Ahorro: 150.000 - 210.000€

SIN COMPLIANCE = PROHIBICIÓN VENDER + MULTAS + DEMANDAS

Precio licencia CodeflowX: 24.000€/año (5 clientes)
ROI: Se paga solo con 1 contrato evitado de demanda (50.000€+)"
```

---

## 📚 CASO 4: RAG DOCUMENTACIÓN INTERNA EMPRESA

### **Descripción:**

**Cliente:** Empresa consultoría "Deloitte España"  
**Proveedor:** Consultor freelance "Juan IA Solutions"  
**Solución:** RAG (Retrieval Augmented Generation) base conocimiento interna  
**Tecnología:** LLaMA 2 70B (local) + Qdrant + LangChain  
**Funcionalidad:**
- Empleados consultan documentación interna (políticas, procedimientos)
- RAG responde basado en docs empresariales
- NO toma decisiones, solo informa
- Uso INTERNO empleados

### **Clasificación Riesgo:**

❌ **NO Alto Riesgo** → Riesgo mínimo (uso interno, no decisiones)

**Razón:** Sistema interno informativo. No afecta derechos fundamentales. No decisiones automatizadas sobre personas.

### **Obligaciones PROVEEDOR (Consultor):**

| Obligación | Descripción |
|-----------|-------------|
| **Documentación básica** | Manual uso + arquitectura sistema |
| **GDPR compliance** | Si docs contienen datos personales |
| **Transparencia empleados** | "Respuestas generadas por IA RAG" |

**Esfuerzo:** 1-2 días (documentación básica)

### **Obligaciones DEPLOYER (Empresa):**

| Obligación | Descripción |
|-----------|-------------|
| **Supervisión contenido** | Revisar respuestas RAG periódicamente |
| **GDPR datos empleados** | Si RAG procesa datos personales |

### **Argumentación Comercial:**

```
MENSAJE CONSULTOR:
"Tu RAG documentación interna = RIESGO MÍNIMO.
No es alto riesgo → compliance BÁSICO.

Obligaciones:
✅ Documentación técnica básica (manual uso)
✅ Transparencia: 'Respuestas IA RAG'
✅ GDPR: si docs tienen datos personales

Esfuerzo: 1-2 días
Coste: <1.000€

CodeflowX valor LIMITADO (no alto riesgo):
- Puede usar plantillas documentación
- GDPR compliance checker (PII en docs)

Recomendación: Compliance manual suficiente.
CodeflowX útil si escalas a múltiples clientes RAG."
```

---

## 🏦 CASO 5: CHATBOT SCORING CREDITICIO MICROFINANZAS

### **Descripción:**

**Cliente:** Fintech microcréditos "QuickLoan"  
**Proveedor:** Startup "FinAI Solutions"  
**Solución:** Chatbot conversacional evaluación riesgo crediticio  
**Tecnología:** GPT-4 API + modelo scoring interno + datos bancarios  
**Funcionalidad:**
- Cliente solicita préstamo via chatbot
- Chatbot hace preguntas financieras
- Sistema scoring evalúa riesgo crediticio
- Aprueba/rechaza microcrédito automáticamente (hasta 5.000€)

### **Clasificación Riesgo:**

✅ **ALTO RIESGO** (Anexo III.5.a - scoring crediticio acceso crédito)

**Criticidad:** 🔴 **CRÍTICA** (decisión financiera, afecta derechos consumidores)

### **Obligaciones PROVEEDOR (Startup):**

**Iguales a CASO 3 (RR.HH.):** Art. 9-15, 16-21, Anexo IV-V-VI, Art. 71

**TOTAL:** 63-95 semanas | Con CodeflowX: 11-15 semanas

### **CRITICIDAD ESPECIAL FINANCIERA:**

```
🔴 Explicabilidad → Cliente rechazado derecho saber por qué
🔴 No discriminación → No sesgo género, edad, origen, código postal
🔴 GDPR Art. 22 → Derecho oposición decisión automatizada
🔴 Supervisión humana → Oficial crédito revisa rechazos/aprobaciones
🔴 Accuracy crítico → Error scoring = pérdidas financieras
```

### **Argumentación Comercial:**

```
MENSAJE STARTUP:
"Tu chatbot scoring crediticio = ALTO RIESGO CRÍTICO.

RIESGOS:
🔴 Discriminación ilegal (código postal, género, edad)
🔴 Demandas consumidores rechazados
🔴 Multas Banco de España + AI Act (hasta 35M€)
🔴 Pérdidas financieras (scoring incorrecto)

CodeflowX ESENCIAL sector financiero:
✅ Bias detection: fairness metrics scoring
✅ Explicabilidad: genera razones rechazo/aprobación
✅ Documentación Anexo IV: justifica modelo scoring
✅ Logging inmutable: evidencia decisiones
✅ HITL workflows: oficial crédito supervisa
✅ Accuracy tracking: monitorización precisión scoring

Deadline: 2 Agosto 2026 (9 meses)
Esfuerzo: 11-15 semanas con CodeflowX vs 63-95 sem manual
Ahorro: 150.000€

Precio licencia: 24.000€/año
ROI: 1 demanda evitada = 50.000€+
     1 auditoría exitosa = reputación + no multas"
```

---

## 📱 CASO 6: BOT WHATSAPP SOPORTE TÉCNICO

### **Descripción:**

**Cliente:** Empresa telecomunicaciones "Movistar"  
**Proveedor:** Agencia conversacional "ChatBotPro"  
**Solución:** Bot WhatsApp soporte técnico 24/7  
**Tecnología:** Claude API + Twilio WhatsApp API + base conocimiento  
**Funcionalidad:**
- Cliente reporta incidencia técnica (internet, móvil)
- Bot diagnostica problema común
- Sugiere soluciones (reiniciar router, APN, etc.)
- Escala a humano si no resuelve

### **Clasificación Riesgo:**

❌ **NO Alto Riesgo** → Riesgo limitado (Art. 50)

**Razón:** Bot soporte técnico no toma decisiones sobre personas. Solo asiste técnicamente.

### **Obligaciones PROVEEDOR (Agencia):**

| Obligación | Artículo | Esfuerzo |
|-----------|----------|----------|
| Transparencia chatbot | Art. 50.1 | 1 día |
| Identificación IA | Art. 50.2 | 1 día |
| Documentación uso | Buena práctica | 1-2 días |

**TOTAL:** 2-3 días

### **Argumentación Comercial:**

```
MENSAJE AGENCIA:
"Tu bot WhatsApp soporte = RIESGO LIMITADO.
Compliance SENCILLO (Art. 50).

Obligaciones:
✅ Transparencia: 'Hola, soy asistente IA Movistar'
✅ Identificación: usuario sabe que es IA

Esfuerzo: 2-3 días
Coste: <500€

CodeflowX valor:
- Templates disclaimers WhatsApp (varios idiomas)
- Checklist compliance bots conversacionales

Precio: 1.200€/año licencia básica
Valor: Profesionaliza tu oferta + compliance garantizado"
```

---

## 🎓 CASO 7: CHATBOT EDUCATIVO TUTORÍAS PERSONALIZADAS

### **Descripción:**

**Cliente:** Universidad online "UNED"  
**Proveedor:** Startup EdTech "SmartTutor AI"  
**Solución:** Chatbot tutor IA personalizado por alumno  
**Tecnología:** GPT-4 API + progreso estudiante + curriculum  
**Funcionalidad:**
- Tutorías personalizadas según nivel alumno
- Recomienda recursos estudio
- Evalúa ejercicios alumno
- Sugiere camino formativo (optativas, especializaciones)

### **Clasificación Riesgo:**

⚠️ **DEPENDE CONTEXTO:**

**Escenario A:** Solo tutorías informativas (alumno decide estudios)  
→ ❌ NO alto riesgo

**Escenario B:** Decisiones automáticas acceso cursos/titulaciones  
→ ✅ Alto riesgo (Anexo III.3 - acceso educación/formación)

**Asumimos Escenario B: ALTO RIESGO**

### **Obligaciones PROVEEDOR (Startup EdTech):**

**Iguales a CASO 3 (RR.HH.):** Art. 9-15, 16-21, Anexo IV-V-VI, Art. 71

**TOTAL:** 63-95 semanas | Con CodeflowX: 11-15 semanas

### **CRITICIDAD ESPECIAL EDUCATIVA:**

```
🔴 No discriminación → No sesgo nivel socioeconómico, origen
🔴 Transparencia estudiantes → Deben saber decisión IA
🔴 Explicabilidad → Estudiante rechazado derecho saber por qué
🔴 Supervisión humana → Profesor revisa decisiones acceso
🔴 Accuracy → Error puede afectar futuro estudiante
```

### **Argumentación Comercial:**

```
MENSAJE STARTUP EDTECH:
"Tu chatbot educativo si DECIDE acceso cursos = ALTO RIESGO.

Obligaciones Art. 9-15, 16-21 + Anexo IV-VI.

CodeflowX sector educativo:
✅ Bias detection: fairness nivel socioeconómico
✅ Explicabilidad: razones decisión acceso
✅ Transparencia: notificaciones estudiantes
✅ HITL workflows: profesor supervisa decisiones

Deadline: 2 Agosto 2026 (9 meses)
Esfuerzo: 11-15 semanas con CodeflowX
Ahorro: 150.000€

Precio: 24.000€/año
ROI: Evita demandas estudiantes + cumple compliance"
```

---

## 🏥 CASO 8: CHATBOT TRIAJE SÍNTOMAS SALUD

### **Descripción:**

**Cliente:** App salud "Doctoralia"  
**Proveedor:** Startup HealthTech "MedAI"  
**Solución:** Chatbot triaje inicial síntomas paciente  
**Tecnología:** GPT-4 API fine-tuned + base conocimiento médico  
**Funcionalidad:**
- Paciente describe síntomas
- Chatbot hace preguntas diagnóstico
- Clasifica urgencia: emergencia / médico 24h / no urgente
- Recomienda especialidad médica

### **Clasificación Riesgo:**

✅ **ALTO RIESGO** (Anexo III.1.a - salud/seguridad personas)

**Criticidad:** 🔴 **CRÍTICA** (decisión salud, puede afectar vida)

### **Obligaciones PROVEEDOR (Startup HealthTech):**

**Iguales a CASO 3 + requisitos médicos adicionales**

**ADICIONAL:**
```
🔴 Accuracy CRÍTICO → Error clasificación urgencia = muerte
🔴 Supervisión médica → Médico SIEMPRE revisa triaje IA
🔴 Disclaimers legales → "No sustituye diagnóstico médico"
🔴 Responsabilidad civil → Seguro RC médica
🔴 GDPR datos salud → Categoría especial Art. 9
```

**TOTAL:** 70-100 semanas | Con CodeflowX: 14-18 semanas

### **Argumentación Comercial:**

```
MENSAJE STARTUP HEALTHTECH:
"Tu chatbot triaje salud = ALTO RIESGO CRÍTICO.

RIESGOS EXTREMOS:
🔴 Error clasificación urgencia → Muerte paciente
🔴 Demandas negligencia médica → Millones €
🔴 Responsabilidad penal si muerte
🔴 Multas AI Act 35M€ + GDPR datos salud

REQUISITOS ESPECIALES SALUD:
✅ Accuracy >99% (life-critical)
✅ Supervisión médico 100% casos
✅ Disclaimers legales reforzados
✅ Seguro RC médica específico
✅ GDPR datos salud categoría especial

CodeflowX + Consultoría médica legal:
✅ Accuracy tracking crítico
✅ HITL workflows médico obligatorio
✅ Logging inmutable decisiones triaje
✅ Documentación Anexo IV medical-grade
✅ GDPR datos salud compliance

Deadline: 2 Agosto 2026 (9 meses)
Esfuerzo: 14-18 semanas con CodeflowX
Ahorro: 180.000€

Precio: 36.000€/año (sector salud)
ROI: Evita demanda negligencia (>500.000€) + compliance"
```

---

## 📊 CASO 9: AGENTE IA ANÁLISIS FEEDBACK CLIENTES

### **Descripción:**

**Cliente:** Cadena hoteles "NH Hotel Group"  
**Proveedor:** Agencia "Marketing Intelligence AI"  
**Solución:** Agente IA análisis reviews clientes + sentiment  
**Tecnología:** Mistral 7B (local) + análisis sentiment + categorización  
**Funcionalidad:**
- Procesa reviews TripAdvisor, Booking, Google
- Sentiment analysis (positivo/negativo/neutro)
- Categorización temas (limpieza, atención, ubicación)
- Dashboard insights gerencia
- NO decisiones automáticas sobre empleados/clientes

### **Clasificación Riesgo:**

❌ **NO Alto Riesgo** → Riesgo mínimo (análisis interno, no decisiones)

**Razón:** Sistema analítico interno. No toma decisiones sobre personas. Solo insights.

### **Obligaciones PROVEEDOR (Agencia):**

| Obligación | Descripción | Esfuerzo |
|-----------|-------------|----------|
| Documentación básica | Manual uso + arquitectura | 1-2 días |
| GDPR compliance | Reviews pueden contener datos personales | 1 día |
| Transparencia interna | "Insights generados por IA" | 1 día |

**TOTAL:** 2-3 días

### **Argumentación Comercial:**

```
MENSAJE AGENCIA:
"Tu agente análisis feedback = RIESGO MÍNIMO.
No alto riesgo → compliance BÁSICO.

Obligaciones:
✅ Documentación técnica básica
✅ GDPR: reviews pueden tener datos personales (nombres)
✅ Transparencia: gerencia sabe que insights son IA

Esfuerzo: 2-3 días
Coste: <1.000€

CodeflowX valor LIMITADO:
- GDPR PII scanner (detectar nombres en reviews)
- Templates documentación

Recomendación: Compliance manual suficiente.
CodeflowX útil si escalas a múltiples clientes."
```

---

## 💳 CASO 10: RECOMENDADOR PRODUCTOS PERSONALIZADO ECOMMERCE

### **Descripción:**

**Cliente:** Marketplace "AliExpress España"  
**Proveedor:** Startup "RecoAI"  
**Solución:** Sistema recomendación productos personalizado  
**Tecnología:** LLaMA 2 70B fine-tuned + collaborative filtering + histórico compras  
**Funcionalidad:**
- Recomienda productos basado en comportamiento usuario
- Personaliza homepage por usuario
- Emails marketing personalizados productos
- Ofertas flash personalizadas

### **Clasificación Riesgo:**

⚠️ **DEPENDE:**

**Escenario A:** Solo recomendación (usuario decide, acceso igual todos)  
→ ❌ NO alto riesgo (riesgo limitado)

**Escenario B:** Scoring usuario determina acceso ofertas exclusivas/crédito compra  
→ ✅ Alto riesgo (Anexo III.5.a)

**Asumimos Escenario A: NO ALTO RIESGO**

### **Obligaciones PROVEEDOR (Startup):**

| Obligación | Artículo | Esfuerzo |
|-----------|----------|----------|
| Transparencia recomendaciones IA | Art. 50.1 + GDPR | 1 día |
| Explicabilidad básica | Buena práctica | 1-2 días |
| No perfilado discriminatorio | GDPR Art. 22 | Diseño sistema |
| Documentación uso | Buena práctica | 1 día |

**TOTAL:** 2-4 días

### **Argumentación Comercial:**

```
MENSAJE STARTUP:
"Tu recomendador productos personalizado = RIESGO LIMITADO.
(Si solo recomienda, no decide acceso/crédito)

Obligaciones:
✅ Transparencia: 'Recomendaciones personalizadas IA'
✅ Explicabilidad: 'Por qué te recomendamos esto'
✅ GDPR: no perfilado discriminatorio

Esfuerzo: 2-4 días
Coste: <1.500€

CodeflowX valor:
- Templates transparencia recomendadores
- GDPR perfilado compliance
- Explicabilidad recomendaciones

Precio: 1.200€/año básica
ROI: Profesionaliza oferta + GDPR compliance"
```

---

## ⚖️ CASO 11: RAG LEGAL BUFETE ABOGADOS

### **Descripción:**

**Cliente:** Bufete abogados "Garrigues"  
**Proveedor:** Legal Tech startup "LawAI"  
**Solución:** RAG base conocimiento legal + jurisprudencia  
**Tecnología:** GPT-4 API + Pinecone + base datos jurisprudencia  
**Funcionalidad:**
- Abogados consultan jurisprudencia/leyes
- RAG responde basado en docs legales
- Sugiere argumentos legales casos similares
- Genera borradores escritos legales

### **Clasificación Riesgo:**

⚠️ **DEPENDE:**

**Escenario A:** Asistente interno abogados (humano decide siempre)  
→ ❌ NO alto riesgo (herramienta profesional)

**Escenario B:** Sistema decide automáticamente elegibilidad ayuda legal gratuita  
→ ✅ Alto riesgo (Anexo III.8 - acceso asistencia legal)

**Escenario C:** Sistema genera documentos legales usados directamente (contratos, testamentos)  
→ ✅ Alto riesgo (Anexo III.8)

**Asumimos Escenario A: NO ALTO RIESGO**

### **Obligaciones PROVEEDOR (Startup LegalTech):**

| Obligación | Descripción | Esfuerzo |
|-----------|-------------|----------|
| Documentación técnica | Manual uso + limitaciones | 2 días |
| Disclaimers legales | "No sustituye asesoría legal humana" | 1 día |
| Accuracy verificación | Respuestas RAG verificables con fuentes | Diseño |
| GDPR datos clientes | Si RAG accede casos con datos personales | 1 día |

**TOTAL:** 3-4 días

### **Si fuera Escenario B/C (Alto Riesgo):**

**Obligaciones completas Art. 9-21:** 63-95 semanas | Con CodeflowX: 11-15 semanas

### **Argumentación Comercial:**

```
MENSAJE STARTUP LEGALTECH:
"Tu RAG legal clasificación CRÍTICA depende uso:

¿ASISTENTE INTERNO abogados (humano decide)?
→ NO alto riesgo → compliance 3-4 días

¿DECIDE AUTOMÁTICAMENTE elegibilidad ayuda legal?
→ Alto riesgo (Anexo III.8) → compliance 11-15 semanas

EVALÚA CON BUFETE:
- ¿Abogado SIEMPRE revisa antes usar respuesta RAG?
- ¿Sistema genera docs usados directamente sin revisión?
- ¿Decide acceso ayuda legal automáticamente?

Si ALTO RIESGO:
CodeflowX sector legal:
✅ Documentación Anexo IV legal-grade
✅ Logging inmutable consultas/respuestas
✅ Explicabilidad: fuentes respuesta RAG
✅ Accuracy tracking: respuestas correctas vs incorrectas
✅ HITL workflows: abogado supervisa

Deadline: 2 Agosto 2026
Esfuerzo: 11-15 semanas con CodeflowX
Ahorro: 150.000€
Precio: 24.000€/año"
```

---

## 🚗 CASO 12: CHATBOT RESERVAS CITAS/SERVICIOS

### **Descripción:**

**Cliente:** Taller mecánico cadena "Norauto"  
**Proveedor:** Agencia "AutoBot Solutions"  
**Solución:** Chatbot reserva citas taller  
**Tecnología:** GPT-3.5 API + sistema gestión citas  
**Funcionalidad:**
- Cliente solicita cita taller
- Chatbot pregunta matrícula, servicio, disponibilidad
- Reserva cita automáticamente
- Envía confirmación SMS/email

### **Clasificación Riesgo:**

❌ **NO Alto Riesgo** → Riesgo limitado (Art. 50)

**Razón:** Reserva citas no afecta derechos fundamentales. Transacción simple.

### **Obligaciones PROVEEDOR (Agencia):**

| Obligación | Artículo | Esfuerzo |
|-----------|----------|----------|
| Transparencia chatbot | Art. 50.1 | 1 día |
| Documentación uso | Buena práctica | 1 día |

**TOTAL:** 1-2 días

### **Argumentación Comercial:**

```
MENSAJE AGENCIA:
"Tu chatbot reservas = RIESGO LIMITADO.
Compliance SENCILLO (Art. 50).

Obligaciones:
✅ Transparencia: 'Asistente IA Norauto'

Esfuerzo: 1-2 días
Coste: <500€

CodeflowX valor:
- Templates disclaimers chatbots reservas
- Checklist compliance bots transaccionales

Precio: 1.200€/año básica
Valor: Profesionaliza oferta compliance"
```

---

## 🏢 CASO 13: ASISTENTE INTERNO EMPLEADOS (FAQ EMPRESA)

### **Descripción:**

**Cliente:** Corporación "Telefónica"  
**Proveedor:** Consultor "Enterprise AI Solutions"  
**Solución:** Asistente IA interno empleados FAQ  
**Tecnología:** LLaMA 2 70B (on-premise) + Qdrant + docs corporativas  
**Funcionalidad:**
- Empleados consultan políticas empresa (vacaciones, gastos, beneficios)
- Asistente responde basado en docs RR.HH.
- NO toma decisiones sobre empleados
- Uso EXCLUSIVO interno

### **Clasificación Riesgo:**

❌ **NO Alto Riesgo** → Riesgo mínimo (interno, informativo)

### **Obligaciones PROVEEDOR (Consultor):**

| Obligación | Esfuerzo |
|-----------|----------|
| Documentación técnica | 1-2 días |
| GDPR datos empleados | 1 día |
| Transparencia interna | 1 día |

**TOTAL:** 2-3 días

### **Argumentación Comercial:**

```
MENSAJE CONSULTOR:
"Tu asistente interno FAQ = RIESGO MÍNIMO.
Compliance BÁSICO.

Obligaciones:
✅ Documentación técnica
✅ GDPR si procesa datos empleados
✅ Transparencia: 'Respuestas IA'

Esfuerzo: 2-3 días
Coste: <1.000€

CodeflowX valor LIMITADO (no alto riesgo).
Compliance manual suficiente."
```

---

## 🎯 CASO 14: AGENTE IA VENTAS CONVERSACIONAL

### **Descripción:**

**Cliente:** Concesionario coches "AutoFácil"  
**Proveedor:** Startup "SalesAI Pro"  
**Solución:** Agente IA ventas conversacional lead scoring  
**Tecnología:** Claude API + CRM integración + lead scoring  
**Funcionalidad:**
- Conversa con potencial cliente web/WhatsApp
- Pregunta necesidades/presupuesto
- Recomienda modelos coches
- Lead scoring (frío/tibio/caliente)
- Deriva a vendedor humano según scoring

### **Clasificación Riesgo:**

❌ **NO Alto Riesgo** → Riesgo limitado (Art. 50)

**Razón:** Ventas B2C no afecta Anexo III. Lead scoring interno no decisión sobre acceso servicios esenciales.

### **Obligaciones PROVEEDOR (Startup):**

| Obligación | Artículo | Esfuerzo |
|-----------|----------|----------|
| Transparencia chatbot | Art. 50.1 | 1 día |
| GDPR datos clientes | RGPD Art. 6 | 1 día |
| Documentación uso | Buena práctica | 1 día |

**TOTAL:** 2-3 días

### **Argumentación Comercial:**

```
MENSAJE STARTUP:
"Tu agente ventas conversacional = RIESGO LIMITADO.
Compliance SENCILLO.

Obligaciones:
✅ Transparencia: 'Asistente IA AutoFácil'
✅ GDPR: consentimiento datos cliente

Esfuerzo: 2-3 días
Coste: <1.000€

CodeflowX valor:
- Templates transparencia agentes ventas
- GDPR consent management

Precio: 1.200€/año básica"
```

---

## 🔍 CASO 15: AGENTE IA DETECCIÓN FRAUDE ECOMMERCE

### **Descripción:**

**Cliente:** Ecommerce "Zara.com"  
**Proveedor:** Software house "FraudGuard AI"  
**Solución:** Agente IA detección fraude transacciones tiempo real  
**Tecnología:** Mistral 8x7B + modelo ML scoring fraude + datos transaccionales  
**Funcionalidad:**
- Analiza transacción tiempo real
- Scoring riesgo fraude (0-100)
- Bloquea transacción si score >90 (alto riesgo fraude)
- Deriva a revisión humana si score 70-90

### **Clasificación Riesgo:**

✅ **ALTO RIESGO** (Anexo III.5.a - scoring afecta acceso servicios financieros)

**Criticidad:** 🔴 **CRÍTICA** (bloqueo transacción = afecta consumidor)

### **Obligaciones PROVEEDOR (Software house):**

**Iguales a CASO 3 (RR.HH.):** Art. 9-21, Anexo IV-VI, Art. 71

**TOTAL:** 63-95 semanas | Con CodeflowX: 11-15 semanas

### **CRITICIDAD ESPECIAL FRAUDE:**

```
🔴 False positives → Cliente legítimo bloqueado (pérdida venta)
🔴 False negatives → Fraude no detectado (pérdida financiera)
🔴 Explicabilidad → Cliente bloqueado derecho saber por qué
🔴 Supervisión humana → Analista fraude revisa bloqueos
🔴 Accuracy crítico → Balance falsos positivos/negativos
```

### **Argumentación Comercial:**

```
MENSAJE SOFTWARE HOUSE:
"Tu agente detección fraude = ALTO RIESGO CRÍTICO.

RIESGOS:
🔴 False positives → Clientes legítimos bloqueados (demandas)
🔴 False negatives → Fraudes no detectados (pérdidas €€€)
🔴 Multas AI Act: hasta 35M€
🔴 Reputación: cliente bloqueado injustamente = viral redes

CodeflowX sector fraude:
✅ Accuracy tracking: false positives/negatives
✅ Explicabilidad: razones bloqueo transacción
✅ Logging inmutable: evidencia decisiones
✅ HITL workflows: analista revisa bloqueos
✅ Bias detection: no sesgo código postal/género
✅ Documentación Anexo IV: justifica modelo fraude

Deadline: 2 Agosto 2026 (9 meses)
Esfuerzo: 11-15 semanas con CodeflowX vs 63-95 sem manual
Ahorro: 150.000€

Precio: 24.000€/año
ROI: 1 demanda cliente evitada = 30.000€+
     Mejora accuracy = reducción pérdidas fraude"
```

---

## 📊 TABLA RESUMEN: 15 CASOS DE USO

| # | Caso Uso | ¿Alto Riesgo? | Anexo III | Esfuerzo Manual | Con CodeflowX | Ahorro € | Precio CodeflowX |
|---|----------|---------------|-----------|----------------|---------------|----------|-----------------|
| 1 | Chatbot ecommerce FAQ | ❌ NO | - | 1-2 días | - | - | 1.200€/año básica |
| 2 | Asistente compras personalizado | ⚠️ Depende | III.5.a | 2-3 días / 50-70 sem | 11-15 sem | 0 / 120.000€ | 1.200€ / 24.000€ |
| 3 | Agente screening RR.HH. | ✅ SÍ | III.4.a | 63-95 sem | 11-15 sem | 150.000€ | 24.000€/año |
| 4 | RAG docs interna | ❌ NO | - | 1-2 días | - | - | N/A (manual ok) |
| 5 | Chatbot scoring crediticio | ✅ SÍ | III.5.a | 63-95 sem | 11-15 sem | 150.000€ | 24.000€/año |
| 6 | Bot WhatsApp soporte | ❌ NO | - | 2-3 días | - | - | 1.200€/año básica |
| 7 | Chatbot educativo tutorías | ⚠️ Depende | III.3 | 3-4 días / 63-95 sem | 11-15 sem | 0 / 150.000€ | 1.200€ / 24.000€ |
| 8 | Chatbot triaje salud | ✅ SÍ | III.1.a | 70-100 sem | 14-18 sem | 180.000€ | 36.000€/año |
| 9 | Agente análisis feedback | ❌ NO | - | 2-3 días | - | - | N/A (manual ok) |
| 10 | Recomendador productos | ⚠️ Depende | III.5.a | 2-4 días / 50-70 sem | 11-15 sem | 0 / 120.000€ | 1.200€ / 24.000€ |
| 11 | RAG legal bufete | ⚠️ Depende | III.8 | 3-4 días / 63-95 sem | 11-15 sem | 0 / 150.000€ | 1.200€ / 24.000€ |
| 12 | Chatbot reservas citas | ❌ NO | - | 1-2 días | - | - | 1.200€/año básica |
| 13 | Asistente interno FAQ | ❌ NO | - | 2-3 días | - | - | N/A (manual ok) |
| 14 | Agente ventas conversacional | ❌ NO | - | 2-3 días | - | - | 1.200€/año básica |
| 15 | Agente detección fraude | ✅ SÍ | III.5.a | 63-95 sem | 11-15 sem | 150.000€ | 24.000€/año |

---

## 💰 MODELOS PRICING CODEFLOWX PARA ESTE MERCADO

### **Tier 1: BÁSICA (Riesgo Limitado)**

**Precio:** 1.200€/año (100€/mes)

**Para:**
- Chatbots FAQ ecommerce
- Bots WhatsApp soporte
- Chatbots reservas
- Agentes ventas conversacionales

**Incluye:**
- Templates transparencia Art. 50
- Checklist compliance riesgo limitado
- Documentación básica
- 1 proyecto

---

### **Tier 2: PROFESIONAL (Alto Riesgo Básico)**

**Precio:** 12.000€/año (1.000€/mes)

**Para:**
- Startups con 1-2 sistemas alto riesgo
- Consultores con pocos clientes alto riesgo

**Incluye:**
- Documentación Anexo IV automatizada (1 sistema)
- Bias detection básico
- Logging inmutable
- Evaluación conformidad asistida
- Soporte técnico email

---

### **Tier 3: ENTERPRISE (Alto Riesgo Múltiple)**

**Precio:** 24.000€/año (2.000€/mes)

**Para:**
- Software houses con múltiples clientes alto riesgo
- Agencias con portafolio sistemas IA

**Incluye:**
- Documentación Anexo IV automatizada (hasta 5 sistemas)
- Bias detection avanzado (20+ métricas)
- Explicabilidad (SHAP/LIME)
- HITL workflows
- Evaluación conformidad + Declaración UE
- Export Art. 71 DB
- Soporte técnico prioritario

---

### **Tier 4: SECTORES CRÍTICOS (Salud, Legal, Financiero)**

**Precio:** 36.000€/año (3.000€/mes)

**Para:**
- HealthTech con sistemas salud
- LegalTech con sistemas legales
- FinTech con scoring/fraude

**Incluye:**
- Todo Tier 3 +
- Accuracy tracking crítico
- Compliance sectorial (GDPR salud, regulación financiera)
- Consultoría legal incluida (4h/mes)
- Auditoría compliance incluida (1x año)
- Soporte 24/7

---

## 📋 CHECKLIST DISCOVERY: IDENTIFICAR OBLIGACIONES CLIENTE

Use esta checklist en discovery call con agencias/consultores:

```
┌─────────────────────────────────────────────────────────────────┐
│ CHECKLIST CLASIFICACIÓN SOLUCIÓN IA                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│ 1. ¿Qué solución IA desarrollas para cliente?                  │
│    [ ] Chatbot/asistente                                        │
│    [ ] Agente IA autónomo                                       │
│    [ ] RAG (documentación)                                      │
│    [ ] Sistema recomendación                                    │
│    [ ] Detección fraude/anomalías                               │
│    [ ] Scoring (crédito/riesgo/candidatos)                      │
│    [ ] Análisis/insights (sentiment, feedback)                  │
│    [ ] Otro: _________                                          │
│                                                                 │
│ 2. ¿Tecnología usada?                                          │
│    [ ] API comercial (GPT-4, Claude, Gemini)                    │
│    [ ] Open source (LLaMA, Mistral, Falcon)                     │
│    [ ] Modelo propio fine-tuned                                 │
│    [ ] Híbrido                                                  │
│                                                                 │
│ 3. ¿Sector cliente final?                                      │
│    [ ] Ecommerce/retail                                         │
│    [ ] Banca/finanzas/seguros → 🔴 CRÍTICO                      │
│    [ ] RR.HH./empleo → 🔴 CRÍTICO                               │
│    [ ] Salud/medicina → 🔴 CRÍTICO                              │
│    [ ] Educación → 🔴 CRÍTICO                                   │
│    [ ] Legal → 🔴 CRÍTICO                                       │
│    [ ] Marketing/ventas                                         │
│    [ ] Soporte/atención cliente                                 │
│    [ ] Sector público → 🔴 CRÍTICO (FRIA)                       │
│    [ ] Otro: _________                                          │
│                                                                 │
│ 4. ¿Sistema toma DECISIONES automáticas sobre personas?        │
│    [ ] SÍ, rechaza/aprueba candidatos empleo → ALTO RIESGO     │
│    [ ] SÍ, rechaza/aprueba crédito → ALTO RIESGO               │
│    [ ] SÍ, decide acceso educación → ALTO RIESGO               │
│    [ ] SÍ, decide acceso servicios esenciales → ALTO RIESGO    │
│    [ ] NO, solo recomienda (humano decide) → Riesgo limitado   │
│    [ ] NO, solo informa → Riesgo mínimo                         │
│                                                                 │
│ 5. ¿Supervisión humana efectiva?                               │
│    [ ] SÍ, humano SIEMPRE revisa antes ejecutar                │
│    [ ] NO, decisión 100% automatizada → ALTO RIESGO            │
│    [ ] PARCIAL, humano revisa solo casos dudosos               │
│                                                                 │
│ 6. ¿Deadline cliente final?                                    │
│    [ ] Antes Agosto 2026 → URGENCIA MÁXIMA                     │
│    [ ] Después Agosto 2026 → Menor urgencia                     │
│    [ ] No sabe → Educar sobre deadline                          │
│                                                                 │
│ 7. ¿Cliente final sabe obligaciones AI Act?                    │
│    [ ] SÍ, informado                                            │
│    [ ] NO, desconoce → OPORTUNIDAD EDUCACIÓN                    │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 📧 EMAIL TEMPLATES POR ESCENARIO

### **Template 1: Agencia Riesgo Limitado (Chatbots FAQ)**

**Asunto:** [AGENCIA] - Compliance AI Act Chatbots (2 días esfuerzo)

```
Hola [NOMBRE],

Como agencia que desarrolla chatbots ecommerce/soporte, tus obligaciones 
AI Act son SENCILLAS (riesgo limitado Art. 50):

OBLIGACIONES:
✅ Transparencia: usuario sabe que es IA
✅ Identificación: "Asistente IA [Marca]"

Esfuerzo: 2-3 días (añadir disclaimers)
Coste compliance: <500€

CodeflowX Básica (1.200€/año):
✅ Templates disclaimers Art. 50 (varios idiomas)
✅ Checklist compliance chatbots
✅ Profesionaliza tu oferta

ROI: Clientes valoran compliance + image profesional

¿Demo 15 min?
[LINK]

Saludos,
[TU NOMBRE]
```

---

### **Template 2: Software House Alto Riesgo (RR.HH., Scoring)**

**Asunto:** [EMPRESA] - URGENTE: Compliance AI Act Alto Riesgo (9 meses)

```
Hola [NOMBRE],

Vi que desarrolláis [SISTEMA IA RR.HH./SCORING] para clientes.

⚠️ CLASIFICACIÓN: ALTO RIESGO (Anexo III.4.a / III.5.a)
⚠️ DEADLINE: 2 Agosto 2026 (solo 9 meses)
⚠️ MULTAS: hasta 35M€

OBLIGACIONES PROVEEDOR (Art. 16-21):
✅ Documentación Anexo IV completa
✅ Bias detection (género, edad, etnia)
✅ Explicabilidad decisiones
✅ Logging inmutable
✅ Evaluación conformidad + Declaración UE
✅ Registro Art. 71 DB

Sin CodeflowX: 63-95 semanas (15-23 meses) → IMPOSIBLE cumplir
Con CodeflowX: 11-15 semanas (2.5-3.5 meses) → COMPLIANCE GARANTIZADO

Ahorro: 150.000€
Precio: 24.000€/año (hasta 5 sistemas)

RIESGOS SIN COMPLIANCE:
🔴 Prohibición vender sistemas
🔴 Multas 35M€
🔴 Demandas clientes afectados (discriminación)
🔴 Pérdida reputación

¿30 min demo urgente esta semana?
[LINK]

Saludos,
[TU NOMBRE]

P.D. 9 meses = muy justo. Actuar YA.
```

---

### **Template 3: Startup HealthTech/LegalTech/FinTech**

**Asunto:** [STARTUP] - Compliance AI Act Sectores Críticos

```
Hola [NOMBRE],

Vuestro sistema [SALUD/LEGAL/FINANCIERO] = ALTO RIESGO CRÍTICO.

SECTORES REGULADOS:
🔴 Salud → Anexo III.1.a (+ GDPR datos salud)
🔴 Legal → Anexo III.8 (+ responsabilidad profesional)
🔴 Financiero → Anexo III.5.a (+ regulación bancaria)

Compliance COMPLEJO:
- AI Act Art. 9-21 (alto riesgo)
- GDPR categoría especial (salud)
- Regulación sectorial adicional
- Accuracy CRÍTICO (life-critical / money-critical)

CodeflowX Sectores Críticos (36.000€/año):
✅ Todo compliance AI Act automatizado
✅ GDPR datos salud/financieros
✅ Accuracy tracking crítico
✅ Consultoría legal incluida (4h/mes)
✅ Auditoría compliance incluida (1x año)
✅ Soporte 24/7

Deadline: 2 Agosto 2026 (9 meses)
Esfuerzo: 14-18 semanas con CodeflowX vs 70-100 sem manual
Ahorro: 180.000€

RIESGO EXTREMO:
- Error sistema salud → Muerte paciente
- Error sistema financiero → Pérdidas millonarias
- Error sistema legal → Negligencia profesional
- Demandas: >500.000€

¿Reunión CEO + CTO esta semana?
[LINK]

Saludos,
[TU NOMBRE]
```

---

## 🎯 ARGUMENTOS COMERCIALES POR OBJECIÓN

### **Objeción 1: "Uso GPT-4 API, OpenAI cumple AI Act"**

**Respuesta:**
```
"Entiendo la confusión, pero:

OpenAI cumple obligaciones como PROVEEDOR del modelo GPT-4 (Art. 51-56).

TÚ eres PROVEEDOR del sistema chatbot/agente que desarrollas con GPT-4.

→ Obligaciones DIFERENTES:
   - OpenAI: Art. 51-56 (modelo GPAI)
   - TÚ: Art. 16-21 (sistema alto riesgo si aplica)

Ejemplo:
- OpenAI responsable: GPT-4 modelo seguro/robusto
- TÚ responsable: chatbot RR.HH. no discrimina, tiene supervisión humana,
  documentación Anexo IV, etc.

→ Si candidato demanda por discriminación, demanda a TU CLIENTE (deployer)
  y a TI (proveedor chatbot), NO a OpenAI."
```

---

### **Objeción 2: "Somos pequeños, no tenemos presupuesto"**

**Respuesta:**
```
"Entiendo perfectamente. Veamos coste compliance:

OPCIÓN A: Sin CodeflowX (manual)
- Consultores externos: 150.000€
- Tiempo: 63-95 semanas (16-23 meses)
- Resultado: NO llegas a Agosto 2026
- Riesgo: Multas 35M€ + prohibición vender

OPCIÓN B: Con CodeflowX
- Licencia: 24.000€/año (2.000€/mes)
- Tiempo: 11-15 semanas (3 meses)
- Resultado: Compliance garantizado Agosto 2026
- Riesgo: Mitigado

ROI:
- Ahorro: 126.000€ (150k - 24k)
- Payback: 1 contrato cliente evitado demanda (>50.000€)
- Valor: Diferenciador competitivo en RFPs

Financiación:
- Pago mensual: 2.000€/mes (vs consultor 12.000€/mes)
- Repercutir coste a clientes: +5% precio soluciones IA
  (cliente valora compliance → paga más)

Alternativa: NO cumplir = prohibición vender Agosto 2026
→ Perder TODOS los clientes vs pagar 24k€/año"
```

---

### **Objeción 3: "Mis clientes no piden compliance aún"**

**Respuesta:**
```
"Totalmente cierto... HOY no piden. Pero:

TIMELINE:
- Hoy (Nov 2025): Clientes no piden compliance
- Marzo 2026: Clientes empiezan a preguntar
- Junio 2026: Clientes EXIGEN compliance (deadline cercano)
- Agosto 2026: OBLIGATORIO → Clientes no pueden usar sin compliance

¿Qué pasa si esperas a Marzo 2026?
- Compliance toma 11-15 semanas con CodeflowX
- Marzo + 15 semanas = Junio/Julio 2026
- Muy justo para deadline Agosto 2026
- STRESS MÁXIMO

¿Qué pasa si actúas AHORA (Nov 2025)?
- Nov + 15 semanas = Febrero 2026
- 6 MESES BUFFER antes deadline
- TRANQUILIDAD
- FIRST-MOVER advantage:
  * Diferenciador en RFPs (compliance ready)
  * Clientes te eligen vs competencia sin compliance
  * Precio premium (compliance tiene valor)

ADEMÁS:
- Competencia tardará en reaccionar
- Tú ya compliance ready → ganas todos RFPs
- En 6 meses, clientes SÍ pedirán compliance
- Estarás 6 meses adelante

First-mover wins."
```

---

### **Objeción 4: "Es solo un chatbot simple, no creo que aplique"**

**Respuesta:**
```
"Entiendo que parezca simple, pero clasificación depende de:

NO importa:
❌ Complejidad técnica chatbot
❌ Si usa GPT-4 o modelo simple
❌ Tamaño equipo desarrollo

SÍ importa:
✅ ¿Qué DECISIONES toma chatbot?
✅ ¿AFECTA derechos fundamentales personas?
✅ ¿Sector cliente (RR.HH., crédito, salud, educación)?

Ejemplos:
- Chatbot FAQ ecommerce → Riesgo limitado (Art. 50) → SENCILLO
- Chatbot screening RR.HH. → Alto riesgo (Anexo III.4.a) → CRÍTICO

Diferencia:
- FAQ: solo informa → no decisiones → compliance 2 días
- RR.HH.: rechaza candidatos → decisión empleo → compliance 11-15 sem

¿Tu chatbot DECIDE sobre acceso empleo/crédito/educación/salud?
→ SÍ = Alto riesgo → CodeflowX esencial
→ NO = Riesgo limitado → Compliance básico ok

Evaluemos juntos en demo 15 min."
```

---

## 📊 COMPARATIVA MERCADOS: ENTERPRISE vs AGENCIAS/CONSULTORES

| Criterio | Enterprise (Banca, Salud) | Agencias/Consultores |
|----------|--------------------------|---------------------|
| **Tamaño** | >500 empleados | 5-50 empleados |
| **Presupuesto** | 100.000 - 500.000€ | 10.000 - 50.000€ |
| **Sistemas IA** | 5-20 sistemas | 1-10 clientes |
| **Conocimiento AI Act** | Alto (legal/compliance) | Bajo/medio |
| **Urgencia** | Alta (regulatory) | Media (clientes no piden aún) |
| **Ciclo venta** | 6-12 meses | 1-3 meses |
| **Precio target** | 50.000 - 200.000€ | 1.200 - 36.000€ |
| **Argumentos clave** | Risk mitigation, multas, auditoría | ROI, diferenciador, first-mover |
| **Objeciones** | "Ya tenemos consultores" | "No tenemos presupuesto" |
| **Tamaño mercado** | 500 empresas España | 10.000+ empresas España |

---

## 🎯 ESTRATEGIA GO-TO-MARKET AGENCIAS/CONSULTORES

### **Fase 1: Awareness (Nov 2025 - Ene 2026)**

**Objetivo:** Educar mercado sobre obligaciones AI Act

**Tácticas:**
- Webinar "AI Act para Agencias/Consultores que Desarrollan IA"
- Whitepaper "15 Casos Uso AI Act: ¿Tu Solución es Alto Riesgo?"
- LinkedIn ads targeting: desarrolladores IA, CTOs agencias
- Partners: asociaciones desarrollo software, clusters IA

---

### **Fase 2: Consideration (Feb 2026 - Abr 2026)**

**Objetivo:** Generar leads cualificados

**Tácticas:**
- Calculadora ROI online "¿Cuánto te cuesta compliance AI Act?"
- Checklist interactivo "¿Mi solución es alto riesgo?"
- Casos de éxito: agencia compliance ready gana RFP
- Free trial 14 días (catalogar 1 sistema IA)

---

### **Fase 3: Decision (May 2026 - Jul 2026)**

**Objetivo:** Cerrar ventas ANTES deadline Agosto 2026

**Tácticas:**
- Urgencia: "Solo 3 meses hasta deadline Agosto 2026"
- Descuento early bird: 20% descuento si contratan antes Jun 2026
- Bundling: Licencia + consultoría onboarding 3.000€
- Garantía: "Compliance ready Agosto 2026 o devolución 100%"

---

### **Fase 4: Retention (Ago 2026+)**

**Objetivo:** Renovaciones + upsell múltiples sistemas

**Tácticas:**
- Success stories: "Agencia X pasó auditoría AI Act sin incidencias"
- Upsell: cliente añade nuevo sistema IA → upgrade tier
- Referral program: recomienda CodeflowX → 20% descuento renovación
- Community: foro agencias/consultores compliance AI Act

---

## 📍 CONCLUSIÓN COMERCIAL

### **MERCADO AGENCIAS/CONSULTORES:**

```
✅ TAMAÑO: 10.000+ empresas España desarrollando soluciones IA
✅ OPORTUNIDAD: Mayoría desconoce obligaciones AI Act
✅ URGENCIA: Deadline Agosto 2026 (9 meses)
✅ TICKET MEDIO: 1.200€ - 36.000€/año según criticidad
✅ CICLO VENTA: 1-3 meses (más rápido que enterprise)
✅ ESCALABILIDAD: Alta (mismo producto, múltiples clientes)
```

### **MENSAJE CLAVE:**

> **"Si desarrollas y entregas chatbot/agente/RAG a cliente, TÚ eres PROVEEDOR. Tienes obligaciones AI Act. Deadline Agosto 2026 (9 meses). Sin compliance = prohibición vender + multas 35M€. CodeflowX te hace compliance ready en 11-15 semanas vs 63-95 semanas manual. Ahorro 150.000€. First-mover advantage."**

### **CALL TO ACTION:**

> **"Demo 15 min → Evaluamos si tu solución es alto riesgo → Calculamos ROI personalizado → Trial gratuito 14 días."**

---

**Documento preparado por:** Equipo CodeflowX  
**Última actualización:** Noviembre 2025  
**Versión:** 1.0  
**Confidencialidad:** Uso interno equipo comercial


