# ¿ME AFECTA EL EU AI ACT? - FAQ TÉCNICO
## Respuestas Directas: Adapters, Fine-tuning, RAG, Modelos Open Source

**Fecha:** Noviembre 2025  
**Propósito:** Aclarar cuándo y cómo te afecta el AI Act según tu caso de uso específico  
**Para:** CTOs, desarrolladores, consultoras tech, agencias que usan IA

---

## 🎯 TABLA RÁPIDA - TU CASO + OBLIGACIONES + FECHA LÍMITE

| Tu Caso de Uso | Proveedor GPAI (Art. 53) | Proveedor Sistema IA (Art. 6-27) | Fecha Límite Cumplimiento | CodeflowX Cubre |
|----------------|--------------------------|----------------------------------|---------------------------|-----------------|
| **Uso GPT-4/Claude via API** | ❌ | ✅ (si alto riesgo según Anexo III) | 2 agosto 2027 (si alto riesgo)<br>2 agosto 2026 (general) | ✅ Art. 6-27 |
| **Uso Llama/Mistral sin modificar** | ❌ | ✅ (si alto riesgo según Anexo III) | 2 agosto 2027 (si alto riesgo)<br>2 agosto 2026 (general) | ✅ Art. 6-27 |
| **Fine-tuning Llama/Mistral completo** | ✅ | ✅ (si alto riesgo) | **2 agosto 2025** (GPAI)<br>2 agosto 2027 (alto riesgo) | ✅ Art. 53 + Art. 6-27 |
| **Adapters (LoRA/QLoRA) sobre modelo base** | ✅ | ✅ (si alto riesgo) | **2 agosto 2025** (GPAI)<br>2 agosto 2027 (alto riesgo) | ✅ Art. 53 + Art. 6-27 |
| **Merge de modelos (ej: Llama+Mistral)** | ✅ | ✅ (si alto riesgo) | **2 agosto 2025** (GPAI)<br>2 agosto 2027 (alto riesgo) | ✅ Art. 53 + Art. 6-27 |
| **RAG con docs propios + GPT-4** | ❌ | ✅ (si alto riesgo según uso) | 2 agosto 2027 (si alto riesgo)<br>2 agosto 2026 (general) | ✅ Art. 6-27 |
| **Chatbot web atención cliente (cualquier modelo)** | ❌ | ✅ (Art. 52 mínimo - transparencia) | 2 agosto 2026 (general) | ✅ Art. 52 + logs |
| **Cuantizar modelo para distribuir** | ✅ | ✅ | **2 agosto 2025** (GPAI)<br>2 agosto 2026 (general) | ✅ Art. 53 + Art. 6-27 |
| **Agentes IA (AutoGPT, CrewAI, etc.)** | ❌ | ✅ (Art. 14 supervisión humana) | 2 agosto 2027 (si alto riesgo)<br>2 agosto 2026 (general) | ✅ Art. 6-27 + HITL |
| **Screening CVs con IA (cualquier modelo)** | ❌ | ✅ **ALTO RIESGO** (Anexo III.4) | **2 agosto 2027** | ✅ Art. 6-27 completo |
| **Scoring crediticio con ML** | ❌ | ✅ **ALTO RIESGO** (Anexo III.5.b) | **2 agosto 2027** | ✅ Art. 6-27 completo |

**FECHAS CLAVE:**
- **2 agosto 2025:** GPAI (modelos modificados, adapters, merge) - **9 MESES** ⚠️
- **2 agosto 2026:** Aplicación general - **21 MESES**
- **2 agosto 2027:** Sistemas alto riesgo (Anexo III) - **32 MESES**

---

## 📋 FAQ DETALLADO - RESPUESTAS DIRECTAS

### **P1: ¿Si uso adapters (LoRA) soy proveedor GPAI?**

✅ **SÍ, probablemente eres proveedor GPAI.**

**Razonamiento legal:**
- Adapters modifican el comportamiento del modelo base
- Aunque no cambies los pesos originales, SÍ cambias la salida del modelo
- Art. 3(63): Modificación sustancial = cambio que requiere nueva evaluación

**Obligaciones:**
- **Art. 53:** Documentación técnica (Anexo XI), política copyright (Anexo XII), transparencia datos entrenamiento
- **Art. 6-27:** Si tu sistema es alto riesgo (ej: RRHH, banca, salud)

**Fecha límite:** **2 agosto 2025** (GPAI) - **Solo 9 meses**

**CodeflowX resuelve:**
- Governance adapters (tracking versiones, evaluación calidad)
- Auto-generación Anexo XI/XII
- Documentación datos entrenamiento
- Evaluación post-modificación

---

### **P2: Recomendáis usar adapters vs modelos genéricos?**

✅ **SÍ, TOTALMENTE RECOMENDADO (pero con governance).**

**Ventajas adapters:**
- 10-100x más barato que fine-tuning completo
- Mejor precisión en tu dominio específico
- Menos sesgos (entrenas con TUS datos)
- Más control sobre comportamiento
- Menor riesgo de "olvido catastrófico"
- Sostenibilidad (menos CO2)

**Pero:**
- Necesitas governance desde día 1 (Art. 53)
- Documentación obligatoria (Anexo XI/XII)
- Evaluación continua

**Ejemplo real:**
- Llama-3-8B genérico → 60% precisión en tu caso legal
- Llama-3-8B + LoRA entrenado con tus docs legales → 89% precisión
- **Ganancia:** 29 puntos + cumplimiento normativo con CodeflowX

**Mensaje:** Adapters son el futuro (eficiencia), pero necesitas plataforma que los gobierne.

---

### **P3: ¿Si uso GPT-4 de OpenAI me afecta el AI Act?**

✅ **SÍ, TE AFECTA (aunque no seas proveedor GPAI).**

**Tu rol:**
- OpenAI = Proveedor GPAI (Art. 53) → OpenAI cumple Anexo XI/XII
- **Tú = Proveedor sistema de IA** (Art. 6-27)

**Ejemplo chatbot RRHH con GPT-4:**
- Sistema alto riesgo (Anexo III.4 - Empleo)
- **TÚ debes cumplir:**
  - Art. 9: Gestión riesgos (evaluar sesgos, precisión)
  - Art. 10: Gobernanza datos (CVs usados, calidad, bias)
  - Art. 11 + Anexo IV: Documentación técnica
  - Art. 12: Mantenimiento registros
  - Art. 13: Transparencia (candidatos saben cómo funciona)
  - Art. 14: Supervisión humana (HITL)
  - Art. 15: Robustez, ciberseguridad
  - Art. 19: Logs inmutables
  - Art. 27: FRIA (si organismo público)
  - Art. 49: Registro BBDD UE

**Respuesta corta:** Usar GPT-4 NO te exime. Tú eres proveedor del sistema.

**Fecha límite:** 2 agosto 2027 (alto riesgo)

---

### **P4: ¿Modelos open source (Llama, Mistral) tienen menos obligaciones?**

❌ **NO, AL CONTRARIO - MÁS RESPONSABILIDAD.**

**Comparación:**

| Aspecto | GPT-4 (OpenAI) | Llama (Meta open source) |
|---------|----------------|--------------------------|
| **Si usas sin modificar** | Tú: Art. 6-27<br>OpenAI: Art. 53 | Tú: Art. 6-27<br>Meta: Art. 53 |
| **Si haces fine-tuning** | Tú: Art. 53 + Art. 6-27<br>OpenAI: Art. 53 | Tú: Art. 53 + Art. 6-27<br>Meta: Art. 53 |
| **Soporte cumplimiento** | OpenAI puede ayudar | Meta NO da soporte individual |
| **Responsabilidad** | Compartida con OpenAI | 100% tuya |
| **Documentación modelo** | OpenAI tiene Anexo XI | Tú debes crear Anexo XI |

**Mensaje:** Open source = libertad, pero también responsabilidad. Necesitas governance desde día 1.

---

### **P5: ¿RAG me hace proveedor GPAI?**

❌ **NO, RAG NO es GPAI.**  
✅ **SÍ eres proveedor de SISTEMA DE IA.**

**Por qué:**
- RAG NO modifica el modelo (solo cambia input/contexto)
- Solo usas modelo GPAI de terceros
- Pero creas **sistema de IA nuevo**

**Obligaciones:**
- **NO Art. 53** (GPAI)
- **SÍ Art. 6-27** (si alto riesgo según uso)

**Ejemplo RAG con docs médicos:**
- Sistema alto riesgo (Anexo III.5.c - Salud)
- Debes cumplir Art. 9-27
- **Fecha límite:** 2 agosto 2027

**CodeflowX gobierna tu RAG:**
- Calidad knowledge base (Art. 10 - datos)
- Chunking strategies
- Retrieval quality (precisión Art. 15)
- Citations (transparencia Art. 13)
- Bias en documentos fuente (Art. 10)
- Drift detection (Art. 72)

---

### **P6: ¿Merge de modelos (ej: Llama+Mistral)?**

✅ **SÍ, eres proveedor GPAI nuevo.**

**Por qué:**
- Creas modelo nuevo con capacidades diferentes
- Merge = modificación sustancial
- Debes evaluar capacidades del modelo resultante

**Obligaciones:**
- **Art. 53 completo:**
  - Anexo XI: Documentación técnica del modelo merged
  - Anexo XII: Política copyright (modelos originales)
  - Transparencia datos (si re-entrenaste)
  - Evaluación capacidades
  - Si >10^25 FLOPS → Art. 55 (riesgo sistémico)

**Fecha límite:** **2 agosto 2025** - **9 MESES** ⚠️

**CodeflowX cubre:**
- Tracking merge (modelos origen, pesos, método)
- Evaluación automática capacidades merged model
- Generación Anexo XI/XII
- Comparación pre/post merge

---

### **P7: ¿Cuantizar modelos (GGUF, 4-bit)?**

⚠️ **DEPENDE de qué hagas.**

**Caso A - Cuantizas para uso interno:**
- NO eres proveedor GPAI
- Solo deployer/usuario

**Caso B - Cuantizas y distribuyes modelo:**
- **SÍ eres proveedor GPAI**
- Modificación + distribución = proveedor
- Art. 53 aplica
- **Fecha límite:** 2 agosto 2025

**Caso C - Cuantizas y vendes sistema basado en eso:**
- Proveedor sistema IA (Art. 6-27)
- Probablemente NO proveedor GPAI (cuantización ≠ cambio capacidades sustancial)

---

### **P8: ¿Agentes de IA (AutoGPT, CrewAI, LangGraph)?**

✅ **SÍ, eres proveedor sistema IA (NO GPAI).**

**Obligaciones específicas:**
- **Art. 14:** Supervisión humana (HITL) **CRÍTICO**
  - Agentes toman decisiones → Humano debe poder intervenir
  - Stop button
  - Revisión decisiones antes de ejecutar acciones críticas
  
- **Art. 13:** Transparencia decisiones
  - Explicar POR QUÉ el agente tomó esa decisión
  - Qué tools usó
  - Qué datos consultó

- **Art. 19:** Logs inmutables
  - Todas las decisiones del agente
  - Acciones ejecutadas
  - Intervenciones humanas

**Si agente es alto riesgo (ej: agente que aprueba créditos):**
- **Fecha límite:** 2 agosto 2027
- Art. 6-27 completo

**CodeflowX gobierna agentes:**
- HITL integrado (stop, approve, reject)
- Explicabilidad decisiones (AI Interpreter)
- Logs inmutables automáticos
- Monitoring autonomía (drift detection)

---

### **P9: ¿Sistemas NO alto riesgo tienen obligaciones?**

✅ **SÍ, mínimo Art. 52 (transparencia).**

**Obligaciones mínimas CUALQUIER sistema IA:**

**Art. 52 - Transparencia:**
- Si chatbot → Usuario debe saber que habla con IA, no humano
- Si contenido generado por IA → Debe estar identificado
- Si deep fake → Obligatorio indicar que es manipulado

**Fecha límite:** 2 agosto 2026 (aplicación general)

**Ejemplo chatbot web ecommerce:**
- NO alto riesgo (solo vende productos)
- Pero **SÍ Art. 52** → "Estás hablando con un asistente virtual IA"
- **SÍ logs** (buena práctica para RGPD)

---

### **P10: ¿Soy "proveedor" o "deployer/usuario"?**

**PROVEEDOR (Obligaciones completas Art. 16-27):**
- Desarrollas sistema IA (aunque uses modelos terceros)
- Modificas sistema IA existente sustancialmente
- Pones tu nombre/marca en el sistema
- Lo vendes/distribuyes a clientes

**Ejemplos proveedores:**
- Consultora que crea chatbot GPT-4 para banco → **PROVEEDOR**
- Startup que hace RAG con Llama para SaaS → **PROVEEDOR**
- Agencia que crea asistente IA para cliente → **PROVEEDOR**

**DEPLOYER/Usuario (Obligaciones menores Art. 26):**
- Usas sistema IA de terceros SIN modificar
- Lo usas para operaciones internas

**Ejemplos deployers:**
- Empresa que compra Microsoft Copilot y lo usa tal cual → **DEPLOYER**
- Empresa que usa ChatGPT Enterprise sin customizar → **DEPLOYER**

**REGLA GENERAL:** Si desarrollas para clientes → **PROVEEDOR** (mayoría de casos).

---

## 🚨 MENSAJES CLAVE COMERCIALES

### **Para CTOs Consultoras Tech:**

> "¿Recomendáis a clientes usar adapters (LoRA) sobre Llama?  
> ✅ SÍ, pero sabiendo que:
> 1. Son proveedores GPAI (Art. 53) desde 2 agosto 2025 (9 meses)
> 2. Necesitan governance adapters desde día 1
> 3. CodeflowX es la ÚNICA plataforma que gobierna esto"

---

### **Para Agencias Marketing:**

> "¿Tu cliente usa chatbot con GPT-4 en su web?  
> ✅ Te afecta:
> - Art. 52: Transparencia (usuario sabe que es IA)
> - Art. 19: Logs conversaciones (auditable)
> - Si vende productos financieros → Alto riesgo (Art. 6-27)
> 
> CodeflowX automatiza compliance, tú solo vendes."

---

### **Para Abogados/Compliance:**

> "Cliente hace fine-tuning de Llama con docs legales:  
> 1. Proveedor GPAI (Art. 53) → Deadline 2 agosto 2025
> 2. Sistema alto riesgo legal (Anexo III.8.a) → Deadline 2 agosto 2027
> 3. **Doble obligación, doble riesgo**
> 
> CodeflowX cubre ambas (único en mercado)."

---

## 📊 PRIORIZACIÓN POR URGENCIA

### **URGENTE (2 agosto 2025 - 9 meses):**

Si haces **CUALQUIERA** de esto:
- ✅ Fine-tuning modelos
- ✅ Adapters (LoRA, QLoRA, etc.)
- ✅ Merge de modelos
- ✅ Distribución modelos cuantizados

→ **ERES PROVEEDOR GPAI** → Art. 53 + Anexo XI + Anexo XII

---

### **IMPORTANTE (2 agosto 2026 - 21 meses):**

Aplicación general:
- ✅ Chatbots (Art. 52 transparencia mínimo)
- ✅ Sistemas IA no alto riesgo
- ✅ Contenido generado por IA

---

### **CRÍTICO SECTORES REGULADOS (2 agosto 2027 - 32 meses):**

Sistemas alto riesgo (Anexo III):
- ✅ RRHH (screening CVs, evaluación desempeño)
- ✅ Financiero (scoring crediticio, seguros)
- ✅ Salud (diagnóstico, análisis imágenes)
- ✅ Legal (sistemas decisión judicial)
- ✅ Público (administración, servicios esenciales)

→ **Art. 6-27 COMPLETO**

---

## 💡 DIFERENCIADOR CODEFLOWX

**Competencia (herramientas observabilidad):**
- ❌ NO gobierna adapters
- ❌ NO gobierna fine-tuning
- ❌ NO gobierna merge modelos
- ❌ NO genera Anexo XI/XII
- ❌ NO tracking copyright compliance

**CodeflowX (governance completo):**
- ✅ Gobierna adapters (versiones, evaluación, Anexo XI)
- ✅ Gobierna fine-tuning (datos, evaluación, Anexo XI)
- ✅ Gobierna merge (tracking origen, capacidades, Anexo XI)
- ✅ Auto-genera Anexo XI/XII (documentación técnica)
- ✅ Política copyright automatizada (Anexo XII)
- ✅ Governance modelos + prompts + agentes + RAG + datos

**ÚNICO en mercado con governance completo GPAI (Art. 53) + Sistemas Alto Riesgo (Art. 6-27).**

---

## 📞 PRÓXIMOS PASOS

**¿Tienes dudas sobre tu caso específico?**

1. **Demo técnica 30 min:** Analizamos tu stack, te decimos qué obligaciones tienes
2. **Evaluación compliance:** Checklist personalizado según tu caso
3. **Roadmap implementación:** Plan paso a paso para cumplir deadlines

**Contacto:**  
Email: tech@codeflowx.com  
Calendly: [link]

---

**CodeflowX - De España para Europa**  
**La única plataforma que gobierna adapters, fine-tuning, merge, RAG, agentes y prompts**  
**Compliance completo GPAI (Art. 53) + Alto Riesgo (Art. 6-27)**

---

*Documento actualizado: Noviembre 2025*  
*Fechas según Artículo 113 del Reglamento (UE) 2024/1689*

