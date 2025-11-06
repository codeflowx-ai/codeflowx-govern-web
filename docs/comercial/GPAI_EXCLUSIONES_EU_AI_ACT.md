# GPAI - EXCLUSIONES Y EXCEPCIONES EU AI ACT
## Guía Técnica para Equipo Comercial

**Fecha:** Noviembre 2025  
**Versión:** 1.0  
**Objetivo:** Clarificar qué modelos están excluidos/incluidos en obligaciones GPAI (General-Purpose AI)

---

## 📖 DEFINICIÓN GPAI (Art. 3.44 + 3.45)

### **¿Qué es un modelo GPAI?**

> **"Modelo de IA de propósito general"**: modelo de IA, incluido cuando se entrena con autoaprendizaje o aprendizaje autosupervisado, que presenta una generalidad significativa y es capaz de realizar competentemente una amplia gama de tareas distintivas, independientemente de la forma en que se comercialice, y que puede integrarse en diversos sistemas o aplicaciones posteriores, excepto los modelos de IA que se utilizan antes de su puesta en el mercado para investigación, desarrollo o creación de prototipos.

### **Características Clave GPAI:**

1. **Generalidad significativa** → Puede hacer múltiples tareas diferentes
2. **Amplia gama de tareas** → No especializado en una sola cosa
3. **Integrable en sistemas posteriores** → Puede usarse como base para aplicaciones
4. **Comercializado o accesible** → Puesto en el mercado o servicio

---

## ✅ MODELOS EXCLUIDOS DE GPAI

### **1. MODELOS DE INVESTIGACIÓN Y PROTOTIPADO (Art. 3.44 excepción)**

**Descripción:**
Modelos utilizados **ANTES** de su puesta en el mercado para:
- Investigación científica
- Desarrollo interno
- Creación de prototipos (PoC)

**Condiciones de Exclusión:**
```
✅ NO comercializado
✅ NO disponible públicamente (ni siquiera como API)
✅ NO integrado en productos/servicios de terceros
✅ Uso EXCLUSIVO interno R&D
```

**Ejemplos Excluidos:**
- Modelo experimental universidad (paper académico)
- Prototipo interno startup (antes de MVP)
- Modelo R&D empresa (sin release externo)

**⚠️ PIERDE EXCLUSIÓN cuando:**
- Se lanza API pública (aunque sea beta)
- Se integra en producto comercial
- Se licencia a terceros
- Se publica como open-source con uso comercial

**Aplicabilidad CodeflowX:**
> **"Si tu modelo GPAI está en R&D, NO necesitas cumplir Art. 51-53 todavía. CodeflowX puede ayudarte a prepararte ANTES del release comercial."**

---

### **2. MODELOS DE TAREA ESPECÍFICA (NO GPAI por definición)**

**Descripción:**
Modelos entrenados/fine-tuned para **UNA** tarea específica sin generalidad significativa.

**Criterios de Exclusión:**
```
✅ Diseñado para UNA tarea concreta
✅ NO puede realizar tareas diversas sin reentrenamiento
✅ Arquitectura/tamaño no permite generalidad
✅ Fine-tuning sobre dominio muy específico
```

**Ejemplos Excluidos (NO son GPAI):**
| Modelo | Tarea Específica | ¿Por qué NO es GPAI? |
|--------|------------------|---------------------|
| **Clasificador spam emails** | Clasificación binaria | Solo spam/no-spam, no generalizable |
| **Detector fraude bancario** | Clasificación transacciones | Solo dominio financiero específico |
| **OCR facturas** | Extracción datos facturas | Solo OCR estructurado, no NLP general |
| **Predictor churn** | Predicción probabilidad abandono | Solo dominio específico empresa |
| **Recommendation engine Netflix** | Recomendación películas | Solo recomendación catálogo específico |
| **Chatbot FAQ soporte** | Respuestas predefinidas | Solo FAQs específicas, no conversacional general |

**⚠️ PERO SÍ son GPAI:**
| Modelo | ¿Por qué SÍ es GPAI? |
|--------|---------------------|
| **GPT-4, Claude, Gemini** | Capacidad NLP general (traducción, resumen, código, análisis, etc.) |
| **BERT/RoBERTa base** | Embeddings generalizables múltiples tareas NLP |
| **LLaMA, Mistral** | Modelos fundacionales multiuso |
| **CLIP, DALL-E** | Multimodal imagen-texto con capacidad general |
| **Whisper** | Transcripción multiidioma con capacidad general |

**Aplicabilidad CodeflowX:**
> **"Si tu modelo solo hace predicción churn o clasificación spam, NO es GPAI → Solo aplica Art. 9-15 si es alto riesgo (Anexo III). CodeflowX igualmente te ayuda con compliance alto riesgo."**

---

### **3. MODELOS OPEN SOURCE CON LICENCIA LIBRE (Art. 53.1.c + Recital 110)**

**⚠️ IMPORTANTE:** Open source NO excluye automáticamente de GPAI, pero **reduce obligaciones**.

**Condiciones para Obligaciones Reducidas:**

```
✅ Publicado bajo licencia open source (MIT, Apache 2.0, GPL, etc.)
✅ Pesos del modelo disponibles públicamente
✅ Código entrenamiento disponible
✅ Documentación suficiente sobre datos entrenamiento
✅ NO riesgo sistémico (<10^25 FLOPs o evaluación favorable)
```

**Obligaciones Reducidas (Art. 53.1.c):**

| Obligación | Open Source GPAI | GPAI Propietario |
|-----------|------------------|------------------|
| **Documentación técnica** | ✅ Sí (simplificada) | ✅ Sí (completa) |
| **Política respeto copyright** | ✅ Sí | ✅ Sí |
| **Resumen datos entrenamiento** | ✅ Sí (público) | ✅ Sí |
| **Evaluación riesgo sistémico** | ⚠️ Solo si >10^25 FLOPs | ⚠️ Solo si >10^25 FLOPs |
| **Cumplimiento Código de Conducta** | ✅ Voluntario | ✅ Voluntario |

**Ejemplos Open Source GPAI:**
- **LLaMA (Meta)** → Open weights, obligaciones reducidas
- **Mistral** → Open source, documentación pública
- **Falcon** → Open source, compliance transparente
- **BLOOM** → Open source, datos entrenamiento documentados

**⚠️ ATENCIÓN:** Open source con riesgo sistémico (>10^25 FLOPs) SÍ tiene obligaciones completas Art. 51.

**Aplicabilidad CodeflowX:**
> **"Si tu modelo es open source <10^25 FLOPs, obligaciones GPAI son menores. CodeflowX puede generar documentación Art. 53.1 automáticamente (copyright, datos, modelo card)."**

---

### **4. MODELOS POR DEBAJO DEL UMBRAL DE RIESGO SISTÉMICO (<10^25 FLOPs)**

**Descripción:**
Modelos GPAI que NO alcanzan el umbral de riesgo sistémico tienen obligaciones **reducidas**.

**Umbral Riesgo Sistémico (Art. 51.1):**

```
🔴 RIESGO SISTÉMICO: ≥ 10^25 FLOPs de cómputo entrenamiento
🟢 NO RIESGO SISTÉMICO: < 10^25 FLOPs
```

**¿Cuánto es 10^25 FLOPs?**

| Modelo | FLOPs Entrenamiento | ¿Riesgo Sistémico? |
|--------|---------------------|-------------------|
| **GPT-3 (175B)** | ~3.14 × 10^23 | ❌ NO (debajo umbral) |
| **GPT-4** | ~2 × 10^25 (estimado) | ✅ SÍ (sobre umbral) |
| **LLaMA-2 70B** | ~1.7 × 10^24 | ❌ NO |
| **Claude 3 Opus** | ~10^25 (estimado) | ⚠️ Probablemente SÍ |
| **Gemini Ultra** | >10^25 (estimado) | ✅ SÍ |
| **BERT-base** | ~10^18 | ❌ NO (muy debajo) |
| **Mistral 7B** | ~10^23 | ❌ NO |

**Obligaciones por Categoría:**

#### **A. GPAI SIN Riesgo Sistémico (<10^25 FLOPs) - Art. 53**

```
✅ Documentación técnica (Art. 53.1.a)
✅ Política respeto copyright datos entrenamiento (Art. 53.1.b)
✅ Resumen público datos entrenamiento (Art. 53.1.d)
✅ Cumplimiento Código de Conducta (Art. 56) - VOLUNTARIO
❌ NO evaluación modelos/riesgos sistémicos
❌ NO adversarial testing obligatorio
❌ NO seguimiento incidentes graves
❌ NO ciberseguridad reforzada
```

**Esfuerzo Compliance:** BAJO (2-4 semanas con CodeflowX)

#### **B. GPAI CON Riesgo Sistémico (≥10^25 FLOPs) - Art. 51**

```
✅ TODAS las de Art. 53 (documentación, copyright, datos)
✅ Evaluación modelo + mitigación riesgos sistémicos (Art. 51.1.a)
✅ Adversarial testing (Art. 51.1.b)
✅ Evaluación + mitigación riesgos sistémicos graves (Art. 51.1.c)
✅ Seguimiento incidentes graves + reporting (Art. 51.1.d)
✅ Ciberseguridad protección modelo + infraestructura (Art. 51.1.e)
✅ Eficiencia energética reportada (Art. 51.1.f)
✅ Notificación a AI Office si riesgo sistémico (Art. 52.1)
```

**Esfuerzo Compliance:** ALTO (8-12 semanas con CodeflowX)

**Aplicabilidad CodeflowX:**
> **"¿Tu modelo <10^25 FLOPs? Obligaciones GPAI son menores (Art. 53 solo). CodeflowX genera documentación + copyright + datos automáticamente. ¿Tu modelo ≥10^25 FLOPs? Necesitas Art. 51 completo → CodeflowX incluye adversarial testing + incident tracking."**

---

### **5. MODELOS GPAI USADOS EXCLUSIVAMENTE EN SISTEMAS NO ALTO RIESGO**

**⚠️ ATENCIÓN:** Esto NO excluye de obligaciones GPAI, pero reduce urgencia.

**Escenario:**
```
Empresa X usa GPT-4 API (GPAI riesgo sistémico) para:
- Chatbot atención cliente (riesgo limitado Art. 50)
- Generación marketing content (riesgo mínimo)
- Asistente interno empleados (riesgo mínimo)

→ Empresa X NO es proveedor GPAI (OpenAI lo es)
→ Empresa X cumple Art. 50 (transparencia) para chatbot
→ Empresa X NO tiene obligaciones Art. 51 (las tiene OpenAI)
```

**Regla:**
```
Obligaciones GPAI (Art. 51-53):
→ Recaen sobre PROVEEDOR del modelo GPAI
→ NO sobre DEPLOYER que usa API modelo terceros

Obligaciones sistemas alto riesgo (Art. 9-15):
→ Recaen sobre DEPLOYER del sistema IA
→ Incluido si usa modelo GPAI tercero como componente
```

**Ejemplos:**

| Caso | Rol | Obligaciones |
|------|-----|--------------|
| **OpenAI publica GPT-4** | Proveedor GPAI | Art. 51-53 (GPAI riesgo sistémico) |
| **Empresa X usa GPT-4 API para contratación** | Deployer alto riesgo | Art. 9-15 (sistema alto riesgo) + Art. 50 (transparencia) |
| **Empresa Y usa GPT-4 para chatbot FAQ** | Deployer riesgo limitado | Art. 50 (transparencia chatbot) |
| **Universidad Z entrena LLaMA-3 clon** | Proveedor GPAI | Art. 53 (si <10^25) o Art. 51 (si ≥10^25) |

**Aplicabilidad CodeflowX:**
> **"¿Usas modelo GPAI tercero (GPT-4, Claude, etc.) en sistema alto riesgo? CodeflowX te ayuda con Art. 9-15 (tu responsabilidad como deployer). Las obligaciones GPAI Art. 51-53 las cumple el proveedor (OpenAI, Anthropic)."**

---

## 🚫 MODELOS QUE **NO** PUEDEN EXCLUIRSE DE GPAI

### **Características que SIEMPRE clasifican como GPAI:**

```
❌ Modelo multiuso (NLP general, visión general, multimodal)
❌ Capacidad realizar tareas diversas sin reentrenamiento
❌ Arquitectura transformer grande (>1B parámetros típicamente)
❌ Entrenado datasets generales (Common Crawl, Wikipedia, etc.)
❌ Comercializado como "foundation model" o "base model"
❌ API pública multi-tenant
❌ Fine-tunable por terceros para múltiples tareas
```

### **Ejemplos que SÍ son GPAI (sin excepción):**

| Modelo | Proveedor | ¿Por qué es GPAI? | Riesgo Sistémico |
|--------|-----------|-------------------|------------------|
| **GPT-4, GPT-4 Turbo** | OpenAI | NLP general, multiuso | ✅ SÍ (≥10^25) |
| **Claude 3 (Opus, Sonnet, Haiku)** | Anthropic | NLP general, multiuso | ⚠️ Opus probablemente SÍ |
| **Gemini Ultra, Pro, Nano** | Google | Multimodal general | ⚠️ Ultra probablemente SÍ |
| **LLaMA 2, LLaMA 3** | Meta | NLP general, open weights | ❌ NO (<10^25) |
| **Mistral Large, 8x7B** | Mistral | NLP general, multiuso | ❌ NO (<10^25) |
| **DALL-E 3** | OpenAI | Generación imagen general | ⚠️ Depende FLOPs |
| **Stable Diffusion XL** | Stability AI | Generación imagen general | ❌ NO (<10^25) |
| **Whisper** | OpenAI | Transcripción multiidioma | ❌ NO (<10^25) |
| **CLIP** | OpenAI | Embeddings multimodal | ❌ NO (<10^25) |

---

## 📊 ÁRBOL DE DECISIÓN: ¿MI MODELO ES GPAI?

```
┌─────────────────────────────────────────────────────────────────┐
│ ¿Tu modelo está en R&D/prototipo SIN release comercial?        │
└────────────────┬────────────────────────────────────────────────┘
                 │
        SÍ ──────┤────── NO
                 │
            EXCLUIDO      ┌────────────────────────────────────────┐
            (Art 3.44)    │ ¿Realiza UNA sola tarea específica?   │
                          └──────┬─────────────────────────────────┘
                                 │
                        SÍ ──────┤────── NO
                                 │
                          NO ES GPAI     ┌───────────────────────────┐
                          (Tarea         │ ¿Capacidad multiuso      │
                          específica)    │ (generalidad)?            │
                                        └──┬────────────────────────┘
                                           │
                                  SÍ ──────┤────── NO
                                           │
                                    ES GPAI        NO ES GPAI
                                           │
                                    ┌──────┴──────┐
                                    │             │
                            ¿≥10^25 FLOPs?    <10^25 FLOPs
                                    │             │
                         ┌──────────┴────┐       │
                         SÍ              NO       │
                         │               │        │
                 RIESGO SISTÉMICO  NO RIESGO     │
                 (Art. 51-56)      SISTÉMICO     │
                 Obligaciones      (Art. 53-56)   │
                 COMPLETAS         Obligaciones   │
                                  REDUCIDAS       │
                                                  │
                                    ┌─────────────┴─────────────┐
                                    │ ¿Open source con          │
                                    │ licencia libre?           │
                                    └────┬──────────────────────┘
                                         │
                                SÍ ──────┤────── NO
                                         │
                              Obligaciones    Obligaciones
                              SIMPLIFICADAS   ESTÁNDAR
                              (Art. 53.1.c)   (Art. 53)
```

---

## 💼 ARGUMENTOS COMERCIALES POR ESCENARIO

### **Escenario 1: Cliente usa API modelo GPAI tercero (GPT-4, Claude)**

**Situación:**
```
Cliente X: "Usamos GPT-4 para sistema contratación RR.HH."
→ Sistema alto riesgo (Anexo III.4.a empleo)
→ PERO usan API OpenAI (no entrenan modelo propio)
```

**Argumento CodeflowX:**
```
✅ "No tienes obligaciones GPAI Art. 51-53 (las tiene OpenAI)."
✅ "SÍ tienes obligaciones sistema alto riesgo Art. 9-15."
✅ "CodeflowX cubre Art. 9-15 completo: logging, supervisión humana, 
    transparencia, documentación técnica, evaluación conformidad."
✅ "PLUS: Art. 50 transparencia (chatbot debe declarar que es IA)."
```

**Objeción típica:** *"Pero OpenAI ya cumple AI Act..."*

**Respuesta:**
```
"OpenAI cumple obligaciones como PROVEEDOR del modelo GPAI (Art. 51-53).
TÚ eres DEPLOYER del sistema alto riesgo contratación (Art. 9-15).
→ Son obligaciones DIFERENTES.
→ Tú respondes ante auditoría/autoridad por el sistema contratación, 
   no OpenAI.
→ CodeflowX documenta TU sistema (Anexo IV), TU supervisión humana 
   (Art. 14), TUS logs (Art. 12)."
```

---

### **Escenario 2: Cliente entrena modelo GPAI propio (<10^25 FLOPs)**

**Situación:**
```
Cliente Y: "Hemos fine-tuned LLaMA-2 70B en nuestros datos internos 
            para asistente legal."
→ Fine-tuning ~10^23 FLOPs (debajo umbral riesgo sistémico)
→ Modelo GPAI (capacidad NLP general)
→ NO riesgo sistémico
```

**Argumento CodeflowX:**
```
✅ "Tu modelo ES GPAI (Art. 3.44) → obligaciones Art. 53."
✅ "NO riesgo sistémico (<10^25) → obligaciones REDUCIDAS (no Art. 51)."
✅ "CodeflowX genera automáticamente:
    - Documentación técnica modelo (Art. 53.1.a)
    - Política copyright datos entrenamiento (Art. 53.1.b)
    - Resumen público datos entrenamiento (Art. 53.1.d)"
✅ "Esfuerzo: 2-4 semanas vs 12-16 semanas manual."
✅ "Si luego despliegas asistente legal como sistema alto riesgo 
    (Anexo III.8 legal), CodeflowX también cubre Art. 9-15."
```

---

### **Escenario 3: Cliente entrena modelo GPAI grande (≥10^25 FLOPs)**

**Situación:**
```
Cliente Z: "Estamos entrenando modelo LLM 200B parámetros desde cero."
→ Entrenamiento ~5 × 10^25 FLOPs (SOBRE umbral riesgo sistémico)
→ Modelo GPAI con riesgo sistémico
```

**Argumento CodeflowX:**
```
🔴 "Tu modelo ES GPAI con RIESGO SISTÉMICO (Art. 51-56)."
🔴 "Obligaciones CRÍTICAS + compliance COMPLEJO:
    - Evaluación riesgos sistémicos (Art. 51.1.a)
    - Adversarial testing obligatorio (Art. 51.1.b)
    - Seguimiento incidentes graves (Art. 51.1.d)
    - Ciberseguridad reforzada (Art. 51.1.e)
    - Eficiencia energética (Art. 51.1.f)
    - Notificación AI Office (Art. 52.1)"
✅ "CodeflowX incluye:
    - leka-adversarial-robustness (adversarial testing automatizado)
    - leka-agent-monitoring (seguimiento incidentes)
    - leka-sustainability-metrics (CO2, energy, water)
    - Export Art. 52.1 notificación AI Office (formato estándar)"
✅ "Esfuerzo: 8-12 semanas con CodeflowX vs 24-36 semanas manual."
💰 "ROI crítico: multas riesgo sistémico hasta 15M€ o 3% facturación global."
```

---

### **Escenario 4: Cliente modelo tarea específica (NO GPAI)**

**Situación:**
```
Cliente W: "Tenemos modelo ML que predice churn clientes B2B."
→ Predicción probabilidad abandono (regresión logística/XGBoost)
→ Tarea ESPECÍFICA (no generalizable)
→ NO es GPAI
```

**Argumento CodeflowX:**
```
✅ "Tu modelo NO es GPAI (tarea específica predicción churn)."
✅ "¿Es alto riesgo? Depende:
    - Si predicción churn afecta empleo/contratos → Anexo III.4 → SÍ
    - Si solo marketing interno → NO alto riesgo"
✅ "Si NO alto riesgo: compliance mínimo (solo transparencia Art. 50 si aplica)."
✅ "Si SÍ alto riesgo: necesitas Art. 9-15 (sin Art. 51-53 GPAI)."
✅ "CodeflowX igualmente útil:
    - Catalogación + clasificación riesgo (FASE 1)
    - Documentación técnica si alto riesgo (Anexo IV)
    - Bias detection, explainability, logging (Art. 10, 13, 12)"
```

---

## 📋 CHECKLIST COMERCIAL: CLASIFICACIÓN GPAI CLIENTE

Use esta checklist en discovery call para determinar obligaciones GPAI:

```
┌─────────────────────────────────────────────────────────────────┐
│ CHECKLIST CLASIFICACIÓN GPAI                                    │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│ 1. ¿Cliente entrena modelo propio o usa API tercero?          │
│    [ ] Entrena propio → Ir pregunta 2                          │
│    [ ] Usa API tercero (GPT-4, Claude, etc.) → NO obligaciones GPAI │
│                                                                 │
│ 2. ¿Modelo está en R&D/prototipo SIN release comercial?       │
│    [ ] SÍ → EXCLUIDO GPAI (Art. 3.44)                          │
│    [ ] NO → Ir pregunta 3                                      │
│                                                                 │
│ 3. ¿Modelo puede realizar MÚLTIPLES tareas diversas?          │
│    [ ] SÍ (NLP general, visión general, etc.) → ES GPAI, ir 4 │
│    [ ] NO (solo clasificación spam, churn, etc.) → NO GPAI    │
│                                                                 │
│ 4. ¿Cómputo entrenamiento ≥ 10^25 FLOPs?                      │
│    [ ] SÍ → GPAI RIESGO SISTÉMICO (Art. 51-56)                │
│    [ ] NO → GPAI SIN riesgo sistémico (Art. 53-56)            │
│    [ ] NO SÉ → Estimar por parámetros:                         │
│        - <10B parámetros → probablemente NO                    │
│        - 10-100B parámetros → depende datos/epochs             │
│        - >100B parámetros → probablemente SÍ                   │
│                                                                 │
│ 5. ¿Modelo open source con licencia libre?                    │
│    [ ] SÍ → Obligaciones SIMPLIFICADAS (Art. 53.1.c)          │
│    [ ] NO → Obligaciones ESTÁNDAR                              │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🎯 PREGUNTAS FRECUENTES COMERCIALES

### **1. "¿Si uso GPT-4 API, tengo obligaciones GPAI?"**

**NO.** Obligaciones GPAI Art. 51-53 las tiene OpenAI (proveedor). Tú tienes obligaciones como **deployer** del sistema que usa GPT-4:
- Si sistema alto riesgo → Art. 9-15
- Si chatbot → Art. 50 (transparencia)
- Si otro uso → depende clasificación riesgo

---

### **2. "¿LLaMA-2 fine-tuned es GPAI?"**

**SÍ**, porque LLaMA-2 base tiene capacidad NLP general. Fine-tuning NO elimina clasificación GPAI. Obligaciones:
- Si fine-tuning <10^25 FLOPs → Art. 53 (reducidas)
- Si fine-tuning ≥10^25 FLOPs → Art. 51-56 (completas)

**PERO** si fine-tuning extremo convierte modelo en tarea única específica → podría argumentarse NO GPAI (depende caso).

---

### **3. "¿BERT/RoBERTa es GPAI?"**

**SÍ**, BERT/RoBERTa son GPAI porque generan embeddings generalizables para múltiples tareas NLP. Obligaciones:
- <10^25 FLOPs → Art. 53 (casi todos casos)
- Open source (MIT license) → obligaciones simplificadas Art. 53.1.c

---

### **4. "¿Modelo clasificación fraude bancario es GPAI?"**

**NO.** Clasificación fraude es tarea específica. NO tiene generalidad significativa. **PERO**:
- Fraude bancario → Anexo III.5.a (scoring crediticio) → Alto riesgo
- Necesitas Art. 9-15 (sin Art. 51-53)

---

### **5. "¿Cuándo empieza obligatoriedad GPAI?"**

| Fecha | Obligación GPAI |
|-------|----------------|
| **2 Agosto 2025** | GPAI riesgo sistémico (≥10^25 FLOPs) → Art. 51-56 |
| **2 Agosto 2026** | GPAI sin riesgo sistémico (<10^25 FLOPs) → Art. 53-56 |

**YA VIGENTE (desde Ago 2025):** Riesgo sistémico  
**PRÓXIMO (Ago 2026):** GPAI general

---

## 📊 TABLA RESUMEN: OBLIGACIONES POR TIPO MODELO

| Tipo Modelo | Ejemplo | GPAI | Obligaciones | Deadline | CodeflowX Coverage |
|-------------|---------|------|--------------|----------|-------------------|
| **API tercero** | GPT-4 API | N/A (proveedor lo es) | Art. 9-15 si alto riesgo | Ago 2026 | ✅ 100% |
| **GPAI riesgo sistémico** | GPT-4 propio | ✅ SÍ | Art. 51-56 completo | Ago 2025 ⏰ | ✅ 100% |
| **GPAI sin riesgo sistémico** | LLaMA-2 70B fine-tuned | ✅ SÍ | Art. 53-56 reducido | Ago 2026 | ✅ 100% |
| **GPAI open source** | Mistral 7B publicado | ✅ SÍ | Art. 53-56 simplificado | Ago 2026 | ✅ 100% |
| **Tarea específica alto riesgo** | Scoring crediticio | ❌ NO | Art. 9-15 | Ago 2026 | ✅ 100% |
| **Tarea específica NO alto riesgo** | Predictor stock interno | ❌ NO | Mínimo (voluntario) | N/A | ✅ 100% |
| **R&D sin release** | Prototipo universidad | ❌ EXCLUIDO | Ninguna (hasta release) | N/A | ✅ Preparación |

---

## 💰 ARGUMENTACIÓN ROI POR CATEGORÍA GPAI

### **Cliente GPAI Riesgo Sistémico (≥10^25 FLOPs):**

```
SIN CODEFLOWX:
- Adversarial testing manual: 8-12 semanas + 60.000€ consultores
- Evaluación riesgos sistémicos: 6-8 semanas + 45.000€ consultores
- Ciberseguridad compliance: 4-6 semanas + 30.000€ auditoría
- Seguimiento incidentes: 15.000€/año operativo
─────────────────────────────
TOTAL: 24-26 semanas + 135.000€ inicial + 15k€/año

CON CODEFLOWX:
- Adversarial testing automatizado: 2 semanas + incluido
- Evaluación riesgos sistémicos: 1 semana + incluido
- Ciberseguridad: compliance automatizado + incluido
- Seguimiento incidentes: automatizado + incluido
─────────────────────────────
TOTAL: 3 semanas + 36.000€/año licencia

AHORRO: 99.000€ + 21 semanas (81% tiempo)
```

### **Cliente GPAI Sin Riesgo Sistémico (<10^25 FLOPs):**

```
SIN CODEFLOWX:
- Documentación técnica modelo: 3-4 semanas + 18.000€
- Política copyright datos: 2 semanas + 9.000€
- Resumen datos entrenamiento: 1 semana + 4.500€
─────────────────────────────
TOTAL: 6-7 semanas + 31.500€

CON CODEFLOWX:
- Documentación automática: 3 días + incluido
- Política copyright: 1 día + incluido
- Resumen datos: 1 día + incluido
─────────────────────────────
TOTAL: 5 días + 12.000€/año licencia

AHORRO: 19.500€ + 5.5 semanas (87% tiempo)
```

---

## 📞 CONTACTO Y RECURSOS

Para más información sobre clasificación GPAI y obligaciones específicas:

- **Equipo Legal:** legal@codeflowx.ai
- **Documentación técnica:** docs.codeflowx.ai/gpai-compliance
- **Demo personalizada:** [LINK calendly]

---

**Documento preparado por:** Equipo CodeflowX  
**Última actualización:** Noviembre 2025  
**Versión:** 1.0  
**Confidencialidad:** Uso interno equipo comercial


