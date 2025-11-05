# 🚨 GUÍA SIN NDA: QUÉ DECIR vs QUÉ NO DECIR
## Protección IP + Generación Leads

**Fecha:** 5 Noviembre 2025  
**Propósito:** Guía para responder expertos sin revelar IP crítica  
**Principio:** **Balance Credibilidad ↔ Protección IP**

---

## 🎯 OBJETIVO RESPUESTAS SIN NDA

**Lo que buscamos:**
1. ✅ Demostrar competencia técnica (para generar credibilidad)
2. ✅ Responder suficientemente bien (para mostrar expertise)
3. ✅ Proteger IP crítica (sin revelar arquitectura interna)
4. ✅ Generar lead comercial (invitar a conversación con NDA)
5. ✅ No dar consultoría gratis (no resolver problemas técnicos del experto)

**Lo que NO queremos:**
- ❌ Sonar evasivos/marketing (pierde credibilidad)
- ❌ Revelar código/arquitectura específica (pierde IP)
- ❌ Dar tanto detalle que no necesiten reunión (pierde lead)
- ❌ Parecer arrogantes o defensivos (pierde oportunidad)

---

## ❌ NUNCA DAR SIN NDA/MARCO COMERCIAL

### **1. CÓDIGO ESPECÍFICO**

**❌ NO DAR:**
- Nombres tablas SQL exactas
  - Ejemplo BAD: "Tabla `TRNEXPERIMENTLINEAGE`"
  - Ejemplo GOOD: "Base datos relacional con tracking provenance"
  
- Nombres columnas/campos específicos
  - Ejemplo BAD: "`TRNDATASETS` (JSONB), `TRNPROVENANCEGRAPH`"
  - Ejemplo GOOD: "Metadatos origen y transformaciones"
  
- Código SQL/Python completo
  - Ejemplo BAD: `CREATE TABLE TRNEXPERIMENTLINEAGE (...)`
  - Ejemplo GOOD: "Queries SQL para linaje recursivo"
  
- Nombres funciones/clases específicos
  - Ejemplo BAD: "`fn_get_risk_level_classification`"
  - Ejemplo GOOD: "Función automática clasificación riesgo"
  
- Nombres microservicios internos
  - Ejemplo BAD: "`leka-model-wrapper`, `leka-llm-evaluation`"
  - Ejemplo GOOD: "Microservicios especializados evaluación"
  
- Endpoints API exactos
  - Ejemplo BAD: `POST /api/model/detect-inference-drift`
  - Ejemplo GOOD: "API drift detection"
  
- Nombres workflows BPMN específicos
  - Ejemplo BAD: "`drift-detection-process`"
  - Ejemplo GOOD: "Workflow automatizado drift detection"

---

### **2. ARQUITECTURA ESPECÍFICA**

**❌ NO DAR:**
- Diagramas arquitectura interna completos
- Distribución microservicios específica
  - Ejemplo BAD: "20+ microservicios Python, 8 FastAPI ports 8000-8007"
  - Ejemplo GOOD: "Arquitectura microservicios especializada"
  
- Infraestructura K8s específica
  - Ejemplo BAD: "3 namespaces: govern-core, govern-ml, govern-bpmn"
  - Ejemplo GOOD: "Kubernetes con aislamiento por componentes"
  
- Esquemas base datos completos
  - Ejemplo BAD: "42 tablas governance, 18 training, 12 compliance"
  - Ejemplo GOOD: "Base datos normalizada multi-módulo"
  
- Algoritmos propietarios
  - Ejemplo BAD: Código algoritmo clasificación riesgo propietario
  - Ejemplo GOOD: "Algoritmo multi-factor clasificación riesgo"
  
- Paths internos exactos
  - Ejemplo BAD: "`leka-model-wrapper/services/drift_detection_service.py`"
  - Ejemplo GOOD: "Servicio drift detection implementado"

---

### **3. MÉTRICAS/NÚMEROS ESPECÍFICOS**

**❌ NO DAR:**
- Métricas internas exactas
  - Ejemplo BAD: "100+ métricas: hallucination_rate, toxicity_score, bias_demographic_parity"
  - Ejemplo GOOD: "Amplio conjunto métricas evaluación (hallucination, toxicity, bias, etc.)"
  
- Tiempos ejecución exactos
  - Ejemplo BAD: "Drift detection cada 1 hora, ethical review cada 3 meses"
  - Ejemplo GOOD: "Drift detection periódico, ethical review programado"
  
- Porcentajes implementación internos
  - Ejemplo BAD: "Modelo emotion recognition 78.3% calidad → CRITICAL"
  - Ejemplo GOOD: "Modelos baja calidad → Escalado automático riesgo"
  
- Líneas código
  - Ejemplo BAD: "150,000 líneas Java, 80,000 Python"
  - Ejemplo GOOD: "Plataforma enterprise full-stack"
  
- Tamaño base datos
  - Ejemplo BAD: "Schema 3.5 GB, 85 tablas, 42 vistas SQL"
  - Ejemplo GOOD: "Base datos enterprise robusta"
  
- Números exactos workflows
  - Ejemplo BAD: "22 workflows BPMN automatizados"
  - Ejemplo GOOD: "Múltiples workflows automatizados"

---

### **4. CASOS CLIENTES**

**❌ NO DAR:**
- Nombres clientes reales
- Casos uso específicos con datos reales
- Métricas rendimiento clientes
- Sectores clientes específicos (sin permiso)
- Screenshots dashboards clientes

---

### **5. ROADMAP/GAPS INTERNOS**

**❌ NO DAR:**
- Funcionalidades pendientes específicas
  - Ejemplo BAD: "EU Database API pendiente Q1 2025"
  - Ejemplo GOOD: "Integración continua con APIs externas"
  
- Timelines roadmap interno
- Prioridades backlog
- Bugs conocidos
- Limitaciones técnicas específicas

---

## ✅ SÍ DAR SIN NDA (GENERA CREDIBILIDAD)

### **1. CONCEPTOS TÉCNICOS GENERALES**

**✅ SÍ DAR:**
- Conceptos estándar industria
  - ✅ "Provenance graph" (concepto estándar)
  - ✅ "Data lineage tracking"
  - ✅ "Metadata governance"
  - ✅ "Risk-quality matrix"
  - ✅ "Drift detection"
  - ✅ "Ethical monitoring"
  - ✅ "Multi-layer governance"
  - ✅ "Continuous monitoring"
  - ✅ "Post-market surveillance"
  
- Vocabulario técnico correcto
  - ✅ "KL divergence" (método estadístico estándar)
  - ✅ "Kolmogorov-Smirnov test"
  - ✅ "JSONB" (tipo dato PostgreSQL estándar)
  - ✅ "Recursive SQL views"
  - ✅ "Anomaly detection"

---

### **2. TECNOLOGÍAS ESTÁNDAR**

**✅ SÍ DAR:**
- Tecnologías open source/públicas
  - ✅ PostgreSQL (base datos relacional)
  - ✅ Python (lenguaje microservicios)
  - ✅ FastAPI (framework Python)
  - ✅ SQL (queries)
  - ✅ BPMN (workflows estándar)
  - ✅ Kubernetes (orquestación)
  - ✅ Docker (contenedores)
  - ✅ OpenSearch (logs/search)
  - ✅ Prometheus/Grafana (monitoring)
  - ✅ RabbitMQ/Kafka (mensajería)

**❌ NO especificar:**
- Versiones exactas (PostgreSQL 14.2 vs "PostgreSQL")
- Configuraciones específicas
- Integraciones propietarias

---

### **3. APROXIMACIONES ARQUITECTÓNICAS**

**✅ SÍ DAR:**
- Conceptos arquitectónicos generales
  - ✅ "Multi-layer lineage tracking"
  - ✅ "Automated risk escalation"
  - ✅ "Continuous monitoring 24/7"
  - ✅ "Self-hosted architecture"
  - ✅ "Microservices-based"
  - ✅ "Event-driven workflows"
  - ✅ "API-first design"
  - ✅ "Immutable logging"
  - ✅ "Multi-tenant isolation"
  
- Patterns arquitectónicos estándar
  - ✅ "Hexagonal architecture" (patrón)
  - ✅ "CQRS pattern" (si aplica)
  - ✅ "Event sourcing" (si aplica)

**❌ NO especificar:**
- Cómo implementas específicamente esos patterns
- Detalles internos arquitectura

---

### **4. CAPACIDADES FUNCIONALES**

**✅ SÍ DAR:**
- Qué hace el sistema (no cómo lo hace)
  - ✅ "Bloqueo automático si fuente no compliant"
  - ✅ "Escalado automático riesgo si calidad baja"
  - ✅ "Detección drift automática"
  - ✅ "Alertas + HITL si severity alta"
  - ✅ "Dashboard unificado governance"
  - ✅ "Tracking completo linaje datos"
  - ✅ "Vinculación automática metadata-riesgo"
  - ✅ "Monitoreo ético post-despliegue"
  
- Beneficios funcionales
  - ✅ "Reduce tiempo compliance 80%"
  - ✅ "Automatiza workflows manuales"
  - ✅ "Detecta sesgos pre-deployment"
  - ✅ "Cumple Art. XX AI Act"

**❌ NO especificar:**
- Cómo implementas esas capacidades internamente
- Código que hace funcionar eso

---

### **5. BENEFICIOS NEGOCIO**

**✅ SÍ DAR:**
- Valor business claro
  - ✅ "No silos entre gobierno datos y gobierno IA"
  - ✅ "Trazabilidad end-to-end"
  - ✅ "Compliance by design"
  - ✅ "Automatización workflows"
  - ✅ "Reduce riesgo regulatorio"
  - ✅ "Acelera time-to-market"
  - ✅ "Soberanía datos europea"

---

### **6. FRAMEWORKS/ESTÁNDARES**

**✅ SÍ DAR:**
- Normativa pública
  - ✅ EU AI Act (Art. 10, 15, 19, 27, 72, etc.)
  - ✅ GDPR (Art. 15-22, 35, etc.)
  - ✅ ISO 42001, ISO 27001, ISO 38507
  - ✅ OECD AI Principles
  - ✅ IEEE Ethically Aligned Design
  - ✅ ICO UK Guidance
  
- Anexos AI Act públicos
  - ✅ Anexo III (use cases alto riesgo)
  - ✅ Anexo IV (documentación técnica)
  - ✅ Anexo VIII (EU Database campos)

**❌ NO especificar:**
- Cómo implementas específicamente cada artículo
- Código compliance específico

---

### **7. EJEMPLOS GENÉRICOS**

**✅ SÍ DAR:**
- Casos uso genéricos (sin datos reales)
  - ✅ "Modelo credit scoring con datos múltiples dominios"
  - ✅ "Sistema detecta dataset no compliant y bloquea"
  - ✅ "Drift detection en producción con alertas"
  - ✅ "Modelo emotion recognition con calidad insuficiente rechazado"
  
- Sectores generales
  - ✅ "Banca, salud, sector público"
  - ✅ "Sectores regulados"
  
- Use cases estándar
  - ✅ "Credit scoring, chatbots, recommendation engines"

**❌ NO especificar:**
- Nombres clientes reales
- Métricas específicas ("78.3% calidad")
- Timelines específicos clientes

---

## 📝 TEMPLATES RESPUESTA

### **TEMPLATE 1: RESPUESTA CONCEPTUAL (SIN NDA)**

```markdown
Gracias por tu pregunta técnica.

En CodeflowX abordamos [TEMA] de forma integral:

🔹 [CAPACIDAD 1]: Implementada mediante [TECNOLOGÍA ESTÁNDAR] con [APROXIMACIÓN ARQUITECTÓNICA]. [CAPACIDAD FUNCIONAL].

🔹 [CAPACIDAD 2]: [ENFOQUE TÉCNICO GENÉRICO]. [BENEFICIO NEGOCIO].

🔹 [CAPACIDAD 3]: [TECNOLOGÍA ESTÁNDAR] con [CAPACIDAD FUNCIONAL]. [RESULTADO FUNCIONAL].

Ejemplo genérico: [CASO USO SIN DATOS ESPECÍFICOS] — el sistema [RESULTADO FUNCIONAL].

Si deseas profundizar técnicamente (arquitectura detallada, código, casos reales), encantado de continuar conversación con marco de colaboración.
```

**Ejemplo real aplicado:**

```markdown
Gracias por tu pregunta técnica.

En CodeflowX abordamos el linaje de datos de forma integral:

🔹 Trazabilidad multi-dominio: Implementada mediante base datos relacional con tracking provenance. Soporte CRM, APIs externas, Data Lakes, Streams. Bloqueo automático si fuente no compliant.

🔹 Metadatos ↔ Riesgo: Función automática clasificación riesgo que escala según calidad datos. Dashboard con matriz Risk-Quality. Workflow BPMN bloquea si riesgo alto + baja calidad.

🔹 Gobernanza continua: Servicio drift detection (Python) con ejecución periódica. Monitoreo ético programado. Dashboard unificado con status governance.

Ejemplo genérico: Modelo credit scoring con datos múltiples dominios — sistema detectó dataset no compliant y bloqueó automáticamente.

Si deseas profundizar técnicamente (arquitectura detallada, código, casos reales), encantado de continuar conversación con marco de colaboración.
```

---

### **TEMPLATE 2: RESPUESTA TÉCNICA DETALLADA (CON NDA)**

```markdown
Gracias por tu interés técnico. Dado que hemos establecido [NDA/Marco de colaboración], puedo compartir detalles específicos:

🔹 [PREGUNTA 1]:
   - **Implementación:** Tabla `[NOMBRE_TABLA]` con campos `[CAMPOS_ESPECÍFICOS]`
   - **Código:** [SNIPPET SQL/PYTHON]
   - **Métricas:** [NÚMEROS ESPECÍFICOS]
   - **Caso real cliente:** [DATOS REALES CON PERMISO]
   - **Ubicación:** `[PATH_ARCHIVO]`

🔹 [PREGUNTA 2]:
   - **Arquitectura:** [DIAGRAMA ESPECÍFICO]
   - **Microservicio:** `[NOMBRE_ESPECÍFICO]` (Puerto [XXXX])
   - **Endpoint:** `POST [URL_ESPECÍFICA]`
   - **Función:** `[NOMBRE_FUNCIÓN]` ([PARÁMETROS])
   - **Tiempos:** [NÚMEROS ESPECÍFICOS] (cada X horas/días)

Adjunto documento técnico completo con [ARQUITECTURA DETALLADA, CÓDIGO COMPLETO, CASOS REALES].
```

---

## 🎯 DECISIÓN: ¿DAR DETALLE TÉCNICO O NO?

**Preguntas para hacerte:**

1. **¿Hay NDA firmado?**
   - ❌ NO → Respuesta conceptual (TEMPLATE 1)
   - ✅ SÍ → Respuesta técnica detallada (TEMPLATE 2)

2. **¿Hay reunión comercial acordada?**
   - ❌ NO → Respuesta conceptual
   - ✅ SÍ → Respuesta técnica (pre-NDA pero con compromiso)

3. **¿Hay interés comercial claro?**
   - ❌ NO (validador/curioso) → Respuesta breve conceptual
   - ✅ SÍ (lead potencial) → Respuesta conceptual + invitación

4. **¿Experto puede ser competidor?**
   - ⚠️ DUDA → Respuesta muy conceptual + verificar identidad
   - ✅ NO → Respuesta conceptual estándar

5. **¿Pregunta es pública (LinkedIn, Twitter)?**
   - ✅ SÍ → SIEMPRE respuesta conceptual (otros leen)
   - ❌ NO (email privado, llamada) → Puede ser más técnico (pero sin código)

---

## 🚨 CASOS ESPECIALES

### **Caso 1: Experto muy técnico insiste en detalles**

**Estrategia:**
1. Reconocer la pregunta técnica
2. Demostrar que entiendes perfectamente la pregunta
3. Ofrecer reunión técnica con NDA para profundizar

**Ejemplo:**
```markdown
Tu pregunta sobre [TEMA] es muy precisa y técnica.

Entiendo que buscas validar [LO QUE BUSCA VALIDAR]. Tenemos implementación completa de [CAPACIDAD], con [APROXIMACIÓN].

Por políticas protección IP, detalles específicos de implementación (código, arquitectura interna, algoritmos) los compartimos con NDA o marco comercial.

Encantado de agendar demo técnica donde puedo mostrarte en vivo:
- [CAPACIDAD 1 en acción]
- [CAPACIDAD 2 funcionando]
- [CAPACIDAD 3 con casos reales]

¿Te interesa una sesión técnica?
```

---

### **Caso 2: "¿Puedes compartir código de ejemplo?"**

**❌ NO DAR:**
- Código producción real
- Funciones propietarias completas
- Queries SQL con nombres tablas reales

**✅ SÍ DAR:**
- Pseudocódigo conceptual
- Ejemplo simplificado genérico

**Ejemplo:**
```markdown
Por protección IP, código específico lo comparto con NDA.

Puedo darte un ejemplo conceptual simplificado:

```pseudocode
function classify_risk(model, dataset_quality):
    if dataset_quality < threshold:
        risk_level = escalate_risk(risk_level)
    if use_case in high_risk_annexIII:
        risk_level = "HIGH_RISK"
    return risk_level
```

En demo técnica puedo mostrarte implementación real funcionando.
```

---

### **Caso 3: "¿Qué métricas exactas usáis?"**

**❌ NO DAR:**
- Lista completa métricas propietarias (100+)
- Nombres específicos internos
- Fórmulas propietarias

**✅ SÍ DAR:**
- Categorías métricas
- Frameworks estándar
- Ejemplos públicos

**Ejemplo:**
```markdown
Usamos métricas estándar industria + propietarias:

**Categorías:**
- LLM: Hallucination, toxicity, bias, coherence, relevance
- RAG: Retrieval precision, answer groundedness, context relevance
- Vision: Object detection accuracy, fairness demographics
- Security: Adversarial robustness, PII leakage, prompt injection

**Frameworks:**
- OECD AI Principles
- ISO 42001 metrics
- EU AI Act Art. 15 requirements

Métricas propietarias específicas las comparto en demo técnica con NDA.
```

---

## 📊 RESUMEN VISUAL

```
┌──────────────────────────────────────────────────────┐
│               SIN NDA / PÚBLICO                       │
├──────────────────────────────────────────────────────┤
│ ✅ Conceptos técnicos generales                       │
│ ✅ Tecnologías estándar (PostgreSQL, Python, etc.)    │
│ ✅ Aproximaciones arquitectónicas                     │
│ ✅ Capacidades funcionales                            │
│ ✅ Beneficios negocio                                 │
│ ✅ Frameworks/Estándares públicos                     │
│ ✅ Ejemplos genéricos sin datos reales                │
│                                                       │
│ ❌ Nombres tablas/funciones específicas                │
│ ❌ Código SQL/Python completo                         │
│ ❌ Arquitectura interna detallada                     │
│ ❌ Métricas/números específicos                       │
│ ❌ Casos clientes reales                              │
│ ❌ Roadmap/gaps internos                              │
└──────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────┐
│            CON NDA / MARCO COMERCIAL                  │
├──────────────────────────────────────────────────────┤
│ ✅ Todo lo anterior +                                 │
│ ✅ Nombres tablas/columnas específicas                │
│ ✅ Código SQL/Python completo                         │
│ ✅ Diagramas arquitectura interna                     │
│ ✅ Endpoints API exactos                              │
│ ✅ Métricas propietarias específicas                  │
│ ✅ Casos reales clientes (con permiso)                │
│ ✅ Configuraciones específicas                        │
│ ✅ Algoritmos propietarios                            │
└──────────────────────────────────────────────────────┘
```

---

**Última actualización:** 5 Noviembre 2025  
**Revisión:** Aprobada CTO  
**Uso:** Obligatorio para respuestas públicas o sin NDA
