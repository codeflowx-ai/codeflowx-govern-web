# QA-002: Lineage Multi-Dominio, Metadata-Riesgo, Drift Continuo
## Pregunta de Experta Gobierno Datos #2 - 3 Preguntas Avanzadas

**Fecha:** 5 Noviembre 2025  
**Experta:** Experta Gobierno Datos #2 (LinkedIn)  
**Canal:** LinkedIn (comentario post CodeflowX lanzamiento)  
**Área:** Gobierno Datos + Data Lineage + Continuous Governance  
**Nivel urgencia:** 🟡 Media (lead comercial potencial)  
**Estado:** ✅ Respondido (estrategia SIN detalles técnicos)

---

## 📋 CONTEXTO PREGUNTA

**Situación:**
Comentario LinkedIn post lanzamiento. Experta gobierno datos reconoce calidad iniciativa y hace 3 preguntas muy técnicas sobre arquitectura governance.

**Perfil experta:**
- **Rol:** Responsable Gobierno Datos
- **Expertise:** Data Governance, Data Lineage, Data Quality
- **Nivel técnico:** 🔥 Muy Alto
- **Objetivo:** Validar técnica + Posible lead comercial / colaboración

**Tono comentario:** Profesional, respetuoso, genuinamente interesado

---

## ❓ PREGUNTA(S) TEXTUAL

### **Pregunta 1: Trazabilidad y linaje multi-dominio**

```
¿Cómo gestiona CodeflowX la trazabilidad y el linaje de los datos que alimentan los modelos, especialmente cuando provienen de múltiples dominios o sistemas externos?
```

**Desglose técnico:**
- Qué pregunta realmente: Arquitectura data lineage para datos multi-origen (CRM, APIs, Data Lakes, etc.)
- Por qué pregunta esto: Validar si es linaje superficial o profundo multi-capa
- Nivel detalle esperado: Arquitectónico (sin exigir código)

---

### **Pregunta 2: Vinculación metadatos calidad ↔ clasificación riesgo**

```
¿Existe un marco para vincular los metadatos de calidad, origen y propósito de uso de los datos con la clasificación de riesgo establecida por el AI Act?
```

**Desglose técnico:**
- Qué pregunta realmente: ¿Existe integración automática metadata calidad → riesgo AI Act?
- Por qué pregunta esto: Validar si clasificación riesgo considera calidad datos
- Nivel detalle esperado: Conceptual-arquitectónico (marco, no código)

---

### **Pregunta 3: Gobernanza continua post-despliegue (drift + ético)**

```
Y, más allá del cumplimiento normativo, ¿cómo se asegura la gobernanza continua de los modelos en producción, considerando la deriva de datos (data drift) y el monitoreo ético post-despliegue?
```

**Desglose técnico:**
- Qué pregunta realmente: Monitoring 24/7 producción (drift + ethical)
- Por qué pregunta esto: Validar si governance es solo pre-deployment o también post
- Nivel detalle esperado: Funcional (qué se monitoriza, no cómo implementado)

---

## ✅ VERIFICACIÓN COBERTURA REAL

### **Pregunta 1: Trazabilidad y linaje multi-dominio**

**¿Lo tenemos implementado?**
- ✅ SÍ - 100%

**Cobertura estimada:**
- 100%

**Evidencia implementación:**
- **Tabla:** `TRNEXPERIMENTLINEAGE`
- **Campos clave:** 
  - `TRNDATASETS` (JSONB) — datasets origen multi-dominio
  - `TRNPROVENANCEGRAPH` (JSONB) — grafo completo provenance
  - `TRNDATASOURCESCOMPLIANT` (BOOLEAN) — compliance tracking
- **Vista SQL:** `v_model_lineage_tree` (vista recursiva para árbol linaje completo)
- **Archivo:** `sql-scripts/patches/08_view_model_lineage_tree.sql`
- **Funcionalidad:** Tracking granular origen (CRM, APIs, Data Lakes, Kafka, S3), bloqueo automático si fuente no compliant

**Gaps identificados:**
- Ninguno

---

### **Pregunta 2: Vinculación metadatos calidad ↔ clasificación riesgo**

**¿Lo tenemos implementado?**
- ✅ SÍ - 100%

**Cobertura estimada:**
- 100%

**Evidencia implementación:**
- **Tabla calidad:** `DATASETQUALITY`
  - `DQICOMPLETENESS`, `DQICONSISTENCY`, `DQIACCURACY`
  - `DQIPIIFOUND`, `DQIBIASDETECTED`, `DQIGDPRCOMPLIANT`
- **Función SQL:** `fn_get_risk_level_classification`
  - Input: model_id, use_case, sector
  - Output: RISK_LEVEL (escalado automático si calidad <80% o PII)
  - Archivo: `sql-scripts/functions/governance/fn_get_risk_level_classification.sql`
- **Vista dashboard:** `v_models_risk_quality` (matriz Risk-Quality)
- **Workflow BPMN:** Bloquea aprobación si HIGH_RISK + calidad insuficiente

**Gaps identificados:**
- Ninguno

---

### **Pregunta 3: Gobernanza continua post-despliegue (drift + ético)**

**¿Lo tenemos implementado?**
- ✅ SÍ - 100%

**Cobertura estimada:**
- 100%

**Evidencia implementación:**

**Drift Detection:**
- **Microservicio:** `leka-model-wrapper` (Python FastAPI)
- **Clase:** `DriftDetectionService`
- **Archivo:** `leka-model-wrapper/services/drift_detection_service.py`
- **Métodos:** `_detect_input_drift`, `_detect_output_drift`, `_detect_performance_drift`
- **Endpoint:** `/api/model/detect-inference-drift`
- **BPMN workflow:** `drift-detection-process` (ejecución cada 1 hora automática)
- **Tabla tracking:** `DRIFTDETECTION`
- **Archivo BPMN doc:** `docs/bpmn/processes/09_DRIFT_DETECTION_PROCESS.md`

**Ethical Monitoring:**
- **Tabla:** `ETHICALREVIEWS`
- **Campos:** 6 scores (transparency, fairness, accountability, privacy, safety, societal_impact)
- **Workflow:** `ethical_review_post_deployment` (cada 3 meses)
- **Escalado:** Automático a Ethics Committee si score <7.0 o degradación >10%

**Dashboard unificado:**
- **Vista:** `v_production_models_governance`
- **Status:** HEALTHY, ACTION_REQUIRED, ETHICAL_REVIEW_NEEDED

**Gaps identificados:**
- Ninguno

---

## 💬 RESPUESTA PREPARADA

### **Nivel NDA:** ❌ SIN NDA (LinkedIn público, primera interacción)

### **Estrategia respuesta:**

**🚨 DECISIÓN CRÍTICA:**
- **NO dar detalles técnicos específicos** (nombres tablas, código, funciones)
- **SÍ demostrar competencia técnica** (conceptos, capacidades, beneficios)
- **Objetivo:** Generar lead comercial / Invitar a conversación con NDA

**Razones:**
1. Proteger IP (no revelar arquitectura sin marco comercial)
2. Generar lead (ella debe contactar para saber más)
3. Balance credibilidad vs protección

---

### **RESPUESTA DADA (textual - LinkedIn):**

```markdown
Gracias por tus palabras y por una reflexión tan precisa.

Efectivamente, uno de los retos más complejos del AI Governance es conectar los pilares de datos, modelos y decisiones de negocio bajo un marco común.

En CodeflowX lo abordamos de forma integral:

🔹 La trazabilidad y linaje se gestionan desde la propia capa de gobierno, vinculando cada dataset, modelo y métrica a su contexto de uso.

🔹 Cada flujo de datos incorpora metadatos de origen, propósito y calidad, que se asocian automáticamente a la clasificación de riesgo definida en el AI Act.

🔹 Y la gobernanza continua se mantiene mediante monitorización activa de drift, métricas éticas y auditorías programadas dentro del ciclo de vida del modelo.

En resumen, buscamos que la gobernanza de datos y la de IA no sean silos, sino partes de un mismo sistema de responsabilidad digital.

Encantado de profundizarlo contigo — seguro que tenemos mucho en común en este enfoque.
```

**Análisis respuesta:**
- ✅ Demuestra competencia técnica (vocabulario correcto)
- ✅ Responde las 3 preguntas conceptualmente
- ✅ NO revela detalles técnicos específicos (nombres tablas, código)
- ✅ Invita a conversación privada ("Encantado de profundizarlo")
- ✅ Tono profesional, respetuoso, colaborativo

---

### **ALTERNATIVA CON NDA/MARCO COMERCIAL:**

Si hubiera NDA firmado o reunión comercial acordada, respuesta sería:

```markdown
Gracias por tu interés técnico. Dado que hemos establecido marco de colaboración, puedo compartir detalles específicos:

🔹 **Trazabilidad multi-dominio:**
   - Tabla TRNEXPERIMENTLINEAGE con provenance graph (JSONB)
   - Tracking granular origen: CRM (PostgreSQL), APIs externas (REST), Data Lakes (S3), Streams (Kafka)
   - Vista SQL recursiva v_model_lineage_tree (detecta ciclos)
   - Bloqueo automático si fuente no compliant (campo TRNDATASOURCESCOMPLIANT)
   - Ejemplo: Modelo credit scoring 4 dominios — sistema detectó dataset social media sin consent y bloqueó

🔹 **Metadatos ↔ Riesgo:**
   - Tabla DATASETQUALITY: completeness, consistency, accuracy, PII (Presidio), sesgo
   - Función SQL fn_get_risk_level_classification:
     • Calidad <80% → Escalado automático HIGH_RISK
     • PII detectado → HIGH_RISK
     • Sesgo detectado → HIGH_RISK
   - Vista dashboard v_models_risk_quality (matriz Risk-Quality)
   - Workflow BPMN bloquea si HIGH_RISK + baja calidad

🔹 **Gobernanza continua:**
   - DriftDetectionService (Python FastAPI, leka-model-wrapper/services/drift_detection_service.py)
   - 3 tipos drift: input (KL divergence), output, performance
   - BPMN drift-detection-process (ejecución cada 1 hora)
   - Tabla DRIFTDETECTION tracking completo
   - ETHICALREVIEWS POST_DEPLOYMENT cada 3 meses (6 scores éticos)
   - Dashboard v_production_models_governance (HEALTHY/ACTION_REQUIRED/ETHICAL_REVIEW_NEEDED)

Adjunto documento técnico completo con arquitectura detallada, código SQL/Python y casos reales.
```

---

## 📊 ANÁLISIS POST-RESPUESTA

**Feedback recibido:**
- ⏳ Pendiente (respondido hace pocas horas)

**Lead generado:**
- ⏳ En espera

**Próximo paso:**
- Esperar respuesta experta
- Si responde positivamente → Ofrecer reunión técnica con NDA
- Si no responde en 7 días → Mensaje privado ofreciendo demo

**Aprendizajes:**
- ✅ **ESTRATEGIA CORRECTA:** No dar detalles técnicos sin NDA
- ✅ Balance perfecto credibilidad vs protección IP
- ✅ Respuesta suficientemente técnica para demostrar competencia
- ✅ Invitación clara a profundizar con marco comercial

---

## 🔗 DOCUMENTOS RELACIONADOS

- **Respuesta técnica completa (CON detalles):** [`RESPUESTA_EXPERTA_GOBIERNO_DATOS_2.md`](../lanzamiento/RESPUESTA_EXPERTA_GOBIERNO_DATOS_2.md) (62 KB) — SOLO para uso con NDA
- **Versión LinkedIn corta:** [`RESPUESTA_LINKEDIN_EXPERTA_GOBIERNO_DATOS_2.txt`](../lanzamiento/RESPUESTA_LINKEDIN_EXPERTA_GOBIERNO_DATOS_2.txt) — NO USAR sin NDA

---

## 📝 NOTAS ADICIONALES

**Lección crítica aprendida:**

1. **SIN NDA → Respuesta conceptual** (como la dada en LinkedIn)
2. **CON NDA → Respuesta técnica detallada** (código, arquitectura, métricas)

**Documentos técnicos completos creados:**
- `RESPUESTA_EXPERTA_GOBIERNO_DATOS_2.md` (62 KB, 30 páginas con código SQL/Python)
- `RESPUESTA_LINKEDIN_EXPERTA_GOBIERNO_DATOS_2.txt` (3000 caracteres técnicos)

**⚠️ IMPORTANTE:** Estos documentos técnicos completos son **SOLO para uso interno o con NDA firmado**. No publicar en LinkedIn público.

---

**Creado:** 5 Noviembre 2025  
**Última actualización:** 5 Noviembre 2025  
**Owner:** CTO
