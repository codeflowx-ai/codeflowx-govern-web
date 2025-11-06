# CODEFLOWX - PLATAFORMA DE GOBIERNO IA
## Documento Ejecutivo Comercial

**Versión:** 1.0  
**Fecha:** Noviembre 2025  
**Audiencia:** Agencias, Consultores, Software Houses  
**Propósito:** Presentación ejecutiva producto

---

## 🎯 QUÉ ES CODEFLOWX

**CodeflowX** es la **plataforma de gobierno, evaluación y monitorización de sistemas IA** que garantiza compliance EU AI Act automático para empresas que desarrollan soluciones IA.

### **En Una Frase:**

> **"CodeflowX transforma 16 meses de compliance manual en 3 meses automatizados, garantizando que tus chatbots, agentes y RAG cumplan EU AI Act antes del deadline Agosto 2026."**

### **Problema que Resuelve:**

```
❌ ANTES (Sin CodeflowX):
- 16 meses compliance manual (consultores 150.000€)
- Documentación Anexo IV: 6-8 semanas manual
- Bias detection: análisis ad-hoc no sistemático
- Explicabilidad: inexistente o manual
- Supervisión humana: sin evidencia documentada
- Monitorización: sin drift detection automatizado
- Audit trail: inexistente
→ Resultado: IMPOSIBLE cumplir Agosto 2026

✅ DESPUÉS (Con CodeflowX):
- 3 meses compliance automatizado (24.000€/año)
- Documentación Anexo IV: 3-5 días automatizada
- Bias detection: 20+ métricas fairness automáticas
- Explicabilidad: SHAP/LIME integrado
- Supervisión humana: HITL workflows documentados
- Monitorización: drift detection 24/7
- Audit trail: logging inmutable completo
→ Resultado: COMPLIANCE GARANTIZADO Agosto 2026
```

---

## 🏗️ ARQUITECTURA PLATAFORMA

### **3 Capas de Gobierno:**

```
┌──────────────────────────────────────────────────────────┐
│ CAPA 1: ORQUESTACIÓN (BPMN 2.0)                         │
│ • 22 Procesos Governance Certificables                  │
│ • Workflow aprobación modelos                           │
│ • Supervisión humana documentada                        │
│ • Auditable por reguladores                             │
└──────────────────────────────────────────────────────────┘
                         ↓
┌──────────────────────────────────────────────────────────┐
│ CAPA 2: MOTOR DE REGLAS (Drools)                        │
│ • 120+ Reglas de negocio compliance                     │
│ • Políticas as code                                      │
│ • Modificables sin redeploy                             │
│ • Versionadas y auditables                              │
└──────────────────────────────────────────────────────────┘
                         ↓
┌──────────────────────────────────────────────────────────┐
│ CAPA 3: MICROSERVICIOS ANÁLISIS IA                      │
│ • 10+ microservicios Python especializados              │
│ • Análisis automático ML/LLM                            │
│ • Detección proactiva riesgos                           │
│ • Integración APIs comerciales/open source             │
└──────────────────────────────────────────────────────────┘
```

### **Stack Tecnológico:**

| Componente | Tecnología | Propósito |
|------------|-----------|-----------|
| **Backend** | Spring Boot 3.x | API REST + workflows |
| **Workflows** | Camunda BPMN 2.0 | Procesos governance |
| **Reglas** | Drools | Políticas as code |
| **Microservicios** | FastAPI (Python) | Análisis IA especializado |
| **Base Datos** | PostgreSQL + TimescaleDB + pgvector | ACID + time-series + embeddings |
| **Vector DB** | Qdrant | RAG + similarity search |
| **Object Storage** | MinIO (S3-compatible) | Modelos, datasets, docs |
| **Log Analytics** | OpenSearch + Dashboards | Logs inmutables + análisis |
| **Cache** | Redis | Performance |
| **Message Broker** | RabbitMQ | Eventos asíncronos |
| **LLMs Locales** | Ollama (LLaMA, Mistral) | Sin coste APIs |
| **Orquestación** | Kubernetes + Helm | Producción enterprise |

---

## 🚀 FUNCIONALIDADES CLAVE

### **1. GOBIERNO DE DATOS (Art. 10)**

#### **1.1. Detección Sesgos Avanzada**

**Microservicio:** `leka-bias-detection-service`

```
✅ 20+ métricas fairness:
   • Disparate Impact Ratio
   • Statistical Parity Difference
   • Equal Opportunity Difference
   • Predictive Equality
   • Calibration by group

✅ Análisis interseccionalidad:
   • Género x Edad
   • Etnia x Código postal
   • Combinaciones múltiples

✅ Detección data poisoning:
   • Tests estadísticos (Kolmogorov-Smirnov, Chi-Squared)
   • Outliers extremos
   • Anomalías distribuciones
```

**Valor Agencias/Consultores:**
> *"Tu chatbot RR.HH. screening candidatos puede tener sesgo género sin saberlo. CodeflowX detecta automáticamente con 20+ métricas y genera informe compliance Art. 10."*

---

#### **1.2. Validación Calidad Datos**

**Microservicio:** `leka-bias-detection-service`

```
✅ Integridad dataset:
   • Missing values analysis
   • Duplicados detection
   • Outliers extremos
   • Schema validation

✅ Data quality score:
   • Completeness (% valores completos)
   • Uniqueness (% duplicados)
   • Validity (tipos datos correctos)
   • Consistency (coherencia cross-tables)
```

**Valor Agencias/Consultores:**
> *"Antes de entrenar modelo scoring crediticio, valida calidad datos automáticamente. Art. 10.2 exige datasets representativos y sin errores."*

---

### **2. EVALUACIÓN MODELOS/LLMs (Art. 15)**

#### **2.1. Evaluación LLMs Comprehensive**

**Microservicio:** `leka-llm-evaluation`

```
✅ Métricas rendimiento:
   • Accuracy, Precision, Recall, F1
   • Perplexity (calidad generación)
   • BLEU, ROUGE (traducción, resumen)
   • Coherence, Relevance (respuestas)

✅ Frameworks integrados:
   • DeepEval (testing LLMs)
   • RAGAS (evaluación RAG)
   • MLflow (tracking experimentos)

✅ Evaluación compliance:
   • Toxicity detection (respuestas ofensivas)
   • PII leakage (filtra datos personales)
   • Hallucination detection (inventa info)
   • Bias in responses (respuestas sesgadas)
```

**Valor Agencias/Consultores:**
> *"Tu chatbot atención cliente puede generar respuestas tóxicas. CodeflowX evalúa automáticamente toxicity, PII leakage, hallucinations antes de producción."*

---

#### **2.2. Adversarial Robustness Testing**

**Microservicio:** `leka-adversarial-robustness`

```
✅ Tests adversariales:
   • Evasion attacks (engañar modelo)
   • Poisoning attacks (envenenar datos)
   • Model inversion (extraer datos entrenamiento)
   • Backdoor attacks (puertas traseras)

✅ Jailbreak detection (LLMs):
   • Prompt injection attempts
   • System prompt leaks
   • Adversarial prompts
   • Security testing
```

**Valor Agencias/Consultores:**
> *"Art. 15.5 exige adversarial robustness testing. CodeflowX automatiza tests adversariales (evasion, poisoning, jailbreak) y genera informe compliance."*

---

### **3. EXPLICABILIDAD (Art. 13)**

#### **3.1. Explicabilidad Modelos ML**

**Microservicio:** `leka-llm-evaluation` + integración SHAP/LIME

```
✅ Feature importance:
   • SHAP (SHapley Additive exPlanations)
   • LIME (Local Interpretable Model-agnostic Explanations)
   • Permutation importance

✅ Explicaciones nivel instancia:
   • "Por qué candidato X rechazado"
   • "Por qué préstamo Y denegado"
   • Feature contributions visualizadas

✅ Explicaciones nivel global:
   • Features más importantes modelo
   • Patrones decisión detectados
   • Relaciones variables explicadas
```

**Valor Agencias/Consultores:**
> *"Cliente rechazado por tu sistema scoring tiene derecho saber por qué (Art. 13). CodeflowX genera explicaciones automáticas con SHAP/LIME."*

---

### **4. GOBIERNO PROMPTS/RAG (Art. 10 + Art. 13)**

#### **4.1. Governance Prompts**

**Microservicio:** `leka-prompt-governance`

```
✅ Detección PII en prompts:
   • Microsoft Presidio integration
   • Detección 50+ tipos PII (email, teléfono, DNI, etc.)
   • Pseudonimización automática
   • Redaction configurable

✅ Versionado prompts:
   • Git-like versioning
   • Diff prompts versions
   • Rollback capability
   • Audit trail changes

✅ Template management:
   • Prompt templates biblioteca
   • Variables parametrizables
   • Testing A/B prompts
```

**Valor Agencias/Consultores:**
> *"Tu RAG documentación interna puede exponer PII sin saberlo. CodeflowX detecta automáticamente con Presidio y pseudonimiza antes de enviar a LLM."*

---

#### **4.2. Evaluación RAG**

**Microservicio:** `leka-rag-evaluation`

```
✅ Métricas RAG específicas:
   • Context Precision (relevancia chunks)
   • Context Recall (cobertura respuesta)
   • Faithfulness (fidelidad a docs)
   • Answer Relevance (respuesta útil)

✅ Framework RAGAS:
   • Evaluación end-to-end RAG
   • Métricas calidad retrieval
   • Métricas calidad generation
   • Comparativa configuraciones RAG

✅ Chunk quality analysis:
   • Tamaño óptimo chunks
   • Overlap analysis
   • Embedding quality
   • Retrieval performance
```

**Valor Agencias/Consultores:**
> *"Tu RAG legal bufete puede recuperar chunks irrelevantes. CodeflowX evalúa automáticamente con RAGAS (context precision, faithfulness, answer relevance)."*

---

### **5. MONITORIZACIÓN POST-DESPLIEGUE (Art. 72)**

#### **5.1. Drift Detection**

**Microservicio:** `leka-agent-monitoring`

```
✅ Data drift:
   • Statistical tests (KS, Chi-Squared)
   • Feature distribution changes
   • Covariate shift detection
   • Alertas automáticas

✅ Concept drift:
   • Performance degradation
   • Accuracy drop detection
   • Prediction distribution changes
   • Retraining triggers

✅ Model drift:
   • Model behavior changes
   • Output distribution shift
   • Calibration degradation
```

**Valor Agencias/Consultores:**
> *"Tu chatbot scoring crediticio puede degradarse con tiempo. CodeflowX detecta automáticamente drift (datos, concepto, modelo) y alerta para reentrenamiento."*

---

#### **5.2. Incident Management**

**Microservicio:** `leka-agent-monitoring`

```
✅ Detección incidentes automática:
   • Performance drops >10%
   • Bias spikes detected
   • Error rate increases
   • Timeout/latency issues

✅ Root cause analysis:
   • LLM-powered RCA
   • Análisis logs automático
   • Correlación eventos
   • Recomendaciones mitigación

✅ Reporting Art. 73:
   • Formato compliance Art. 73 (incidentes graves)
   • Notificación autoridad automatizada
   • Timeline incidente documentado
   • Corrective actions tracking
```

**Valor Agencias/Consultores:**
> *"Art. 73 exige reportar incidentes graves. CodeflowX detecta automáticamente, analiza root cause con LLM y genera reporte formato compliance."*

---

### **6. DOCUMENTACIÓN COMPLIANCE (Anexo IV)**

#### **6.1. Documentación Técnica Automatizada**

**Microservicio:** `leka-technical-documentation-generator`

```
✅ Anexo IV completo:
   • Propósito sistema (intended use)
   • Datos entrenamiento (Art. 10)
   • Arquitectura modelo
   • Métricas rendimiento (Art. 15)
   • Medidas mitigación riesgos (Art. 9)
   • Instrucciones uso (Art. 13)
   • Supervisión humana (Art. 14)

✅ Formatos:
   • PDF professional
   • HTML interactivo
   • Markdown exportable
   • JSON structured

✅ Idiomas:
   • Español, Inglés, Francés, Alemán
   • Traducción automática
   • Templates adaptables
```

**Valor Agencias/Consultores:**
> *"Documentación Anexo IV manualmente toma 6-8 semanas. CodeflowX genera automáticamente en 3-5 días (100% campos Anexo IV cumplimentados)."*

---

#### **6.2. Declaración UE Conformidad**

**Microservicio:** `leka-eu-declaration-generator`

```
✅ Anexo V automático:
   • Identificación proveedor
   • Identificación sistema IA
   • Referencia estándares aplicados
   • Procedimiento evaluación conformidad
   • Declaración firmable digitalmente

✅ Formato oficial:
   • Template oficial Comisión Europea
   • Campos obligatorios validados
   • Firma digital integrada
   • PDF/A long-term preservation
```

**Valor Agencias/Consultores:**
> *"Declaración UE conformidad (Anexo V) es obligatoria. CodeflowX genera automáticamente con template oficial y valida campos obligatorios."*

---

### **7. EVALUACIÓN CONFORMIDAD (Anexo VI)**

#### **7.1. Assessment Automatizado**

**Microservicio:** `leka-conformity-assessment`

```
✅ Procedimientos Anexo VI:
   • Internal control (Anexo VI.1)
   • Quality management (Anexo VI.2)
   • Checklist compliance automático
   • Gap analysis

✅ Evidencias generadas:
   • Test reports
   • Audit trails
   • Documentación técnica
   • Declaración conformidad

✅ Scoring compliance:
   • % cumplimiento por artículo
   • Gaps identificados
   • Acciones correctivas sugeridas
   • Roadmap certificación
```

**Valor Agencias/Consultores:**
> *"Evaluación conformidad (Anexo VI) manualmente toma 12-16 semanas. CodeflowX automatiza checklist, identifica gaps y genera evidencias en 2-4 semanas."*

---

### **8. SUPERVISIÓN HUMANA (Art. 14)**

#### **8.1. HITL Workflows**

**Componente:** BPMN workflows + Backend

```
✅ Human-in-the-loop documentado:
   • Workflow aprobación decisiones críticas
   • Escalación automática casos dudosos
   • Revisión humana obligatoria configurable
   • Audit trail completo decisiones

✅ Circuit breakers:
   • Kill-switch manual inmediato
   • Stop automático umbrales
   • Pause system capability
   • Rollback último estado estable

✅ Evidencia supervisión:
   • Timestamp aprobaciones
   • Usuario aprobador registrado
   • Razones decisión documentadas
   • Trazabilidad completa
```

**Valor Agencias/Consultores:**
> *"Art. 14 exige supervisión humana efectiva. CodeflowX provee workflows HITL documentados + kill-switch + audit trail completo (evidencia compliance)."*

---

### **9. LOGGING INMUTABLE (Art. 12)**

#### **9.1. Audit Trail Completo**

**Componente:** PostgreSQL + hash chain + OpenSearch

```
✅ Logs inmutables:
   • Hash chain (cada log firmado)
   • Tamper-proof (detección modificación)
   • Timestamp confiable
   • Append-only storage

✅ Información registrada:
   • Input data (pseudonimizado)
   • Output decisiones
   • Modelo versión usado
   • Usuario responsable
   • Timestamp exacto
   • Contexto ejecución

✅ Retención configurable:
   • Default: 10 años (Art. 12.1)
   • Compression automática
   • Archival cold storage
   • Compliance GDPR Art. 17 (derecho olvido)
```

**Valor Agencias/Consultores:**
> *"Art. 12 exige logs automáticos mínimo 6 meses (alto riesgo). CodeflowX provee logging inmutable con hash chain (tamper-proof) + retención 10 años."*

---

### **10. REGISTRO ART. 71 BASE DE DATOS UE**

#### **10.1. Export Art. 71 Automático**

**Microservicio:** Backend + export service

```
✅ Anexo VIII formato:
   • Campos obligatorios 100% cumplimentados
   • Formato JSON/XML según especificación
   • Validación schema oficial
   • Export 1-click

✅ Información incluida:
   • Identificación proveedor/deployer
   • Sistema IA descripción
   • Categoría alto riesgo (Anexo III)
   • Estado sistema (desarrollo/producción)
   • Certificados/declaraciones
   • Eventos graves reportados

✅ Integración futura:
   • API conexión directa DB UE
   • Notificación automática cambios
   • Sincronización bidireccional
```

**Valor Agencias/Consultores:**
> *"Art. 71 exige registro sistemas alto riesgo en base datos UE. CodeflowX genera export Anexo VIII formato oficial (JSON/XML) 1-click."*

---

## 🎯 VENTAJAS PARA AGENCIAS/CONSULTORES

### **1. DIFERENCIADOR COMPETITIVO**

```
SIN CodeflowX:
- Cliente pregunta: "¿Tu chatbot cumple AI Act?"
- Tu respuesta: "Eh... estamos mirándolo..."
- Resultado: PIERDES DEAL vs competencia compliance

CON CodeflowX:
- Cliente pregunta: "¿Tu chatbot cumple AI Act?"
- Tu respuesta: "SÍ, certificado CodeflowX. Aquí documentación Anexo IV + Declaración UE."
- Resultado: GANAS DEAL + precio premium +15%
```

**First-Mover Advantage:**
- 95% agencias NO tienen compliance → Oportunidad ENORME
- Tus primeros 6 meses = ventaja 6 meses adelante competencia
- En RFPs, compliance será MANDATORIO desde Junio 2026
- Quien tenga compliance ready = gana automáticamente

---

### **2. REDUCCIÓN RIESGO LEGAL**

```
RIESGO SIN CodeflowX:
- Cliente final demandado por discriminación (chatbot RR.HH.)
- Cliente demanda A TI como proveedor
- Sin evidencia compliance → pérdida demanda
- Daños: 50.000 - 200.000€ + reputación

PROTECCIÓN CON CodeflowX:
- Bias detection 20+ métricas (evidencia no discriminación)
- Explicabilidad SHAP/LIME (razones decisión)
- Audit trail completo (trazabilidad)
- Documentación Anexo IV (compliance documentado)
→ Defensa legal sólida + demostración diligencia debida
```

**Multas Evitadas:**
- Sin compliance Agosto 2026 = prohibición vender + multas hasta 35M€
- Con CodeflowX = compliance garantizado + tranquilidad

---

### **3. AUMENTO PRECIO/MARGEN**

```
PRECIO SIN Compliance:
Chatbot RR.HH. screening: 15.000€
Margen: 30% = 4.500€

PRECIO CON Compliance CodeflowX:
Chatbot RR.HH. screening + compliance: 18.000€ (+20%)
Coste CodeflowX: 2.000€/mes = 24.000€/año (5 clientes)
Coste por cliente: 4.800€/año = 400€/mes
Margen adicional: +3.000€ - 400€/mes x duración contrato

CÁLCULO:
Contrato 12 meses:
- Precio adicional: +3.000€
- Coste CodeflowX: -4.800€
- Margen neto: -1.800€ (año 1)

Contrato 24 meses:
- Precio adicional: +6.000€
- Coste CodeflowX: -9.600€ (pero amortizado 5 clientes)
- Margen neto: POSITIVO si >2 clientes simultáneos

ADEMÁS:
- Retención cliente: compliance = contrato renovado
- Upsell: cliente añade nuevo sistema → upgrade tier
- Referencia: cliente satisfecho → nuevos clientes
```

**ROI Agencia:**
```
Inversión: 24.000€/año licencia CodeflowX Enterprise
Beneficios:
- 5 clientes x +3.000€ precio = +15.000€/año
- 0 demandas evitadas x 50.000€ = potencialmente +50.000€
- 3 deals ganados vs competencia x 15.000€ = +45.000€
- Reputación: compliance certified = inestimable

ROI: (15.000 + 45.000 - 24.000) / 24.000 = 150% ROI
```

---

### **4. VELOCIDAD TIME-TO-MARKET**

```
DESARROLLO SIN CodeflowX:
1. Desarrollar chatbot: 4 semanas
2. Compliance manual:
   - Documentación Anexo IV: 6-8 semanas
   - Bias detection: 2 semanas (si sabes hacerlo)
   - Explicabilidad: 3 semanas
   - Logging: 2 semanas
   - Evaluación conformidad: 12-16 semanas
TOTAL: 29-35 semanas (7-9 meses)

DESARROLLO CON CodeflowX:
1. Desarrollar chatbot: 4 semanas
2. Integrar CodeflowX: 1 semana
3. Compliance automatizado: 2 semanas (validación)
TOTAL: 7 semanas (1.5 meses)

AHORRO: 22-28 semanas (5.5-7 meses)
```

**Ventaja Competitiva:**
- Delivers más rápidos = más clientes atendidos
- Cliente urgente = puedes entregar en 2 meses vs competencia 9 meses
- First-to-market = capturas oportunidades antes

---

### **5. ESCALABILIDAD NEGOCIO**

```
SIN CodeflowX (Manual):
Cliente 1: 16 meses compliance → 1 cliente/año
Cliente 2: 16 meses compliance → 1 cliente/año
Cliente 3: 16 meses compliance → 1 cliente/año
TOTAL: 3 clientes en 3 años (no escalable)

CON CodeflowX (Automatizado):
Cliente 1: 3 meses compliance → 4 clientes/año
Cliente 2: 3 meses compliance → 4 clientes/año
Cliente 3: 3 meses compliance → 4 clientes/año
Cliente 4: 3 meses compliance → 4 clientes/año
TOTAL: 16 clientes en 1 año (5x escalabilidad)
```

**Crecimiento Agencia:**
- Mismo equipo = 5x más clientes atendidos
- Revenue: 3 clientes x 15k€ = 45k€ → 16 clientes x 15k€ = 240k€
- Crecimiento: +433% revenue con misma plantilla

---

### **6. PROFESIONALIZACIÓN OFERTA**

```
OFERTA SIN CodeflowX:
"Desarrollamos tu chatbot en 4 semanas."
- Cliente: "¿Cumple AI Act?"
- Tú: "Eh... estamos mirando eso..."
- Impresión: AMATEUR

OFERTA CON CodeflowX:
"Desarrollamos tu chatbot compliance-ready en 7 semanas, incluyendo:
✅ Documentación Anexo IV
✅ Bias detection 20+ métricas
✅ Explicabilidad SHAP/LIME
✅ Logging inmutable Art. 12
✅ Evaluación conformidad Anexo VI
✅ Declaración UE conformidad Anexo V
✅ Export Art. 71 DB UE"
- Cliente: "WOW, son profesionales serios"
- Impresión: ENTERPRISE-GRADE
```

**Imagen Marca:**
- Compliance = profesionalidad
- Certificación = confianza
- Documentación = seriedad
- Auditable = tranquilidad cliente

---

### **7. TRANQUILIDAD OPERATIVA**

```
SIN CodeflowX:
- Cliente llama: "Candidato demanda discriminación, necesito evidencias"
- Tú: "Eh... no tenemos logs de esa decisión..."
- Resultado: PÁNICO + demanda perdida

CON CodeflowX:
- Cliente llama: "Candidato demanda discriminación, necesito evidencias"
- Tú: "Aquí audit trail completo:
  * Timestamp decisión
  * Input datos candidato (pseudonimizado)
  * Modelo versión usado
  * Scoring decisión
  * Bias metrics (DI ratio 0.89 OK)
  * Explicación SHAP (razones rechazo)
  * Supervisión humana (aprobado por Juan el 15/11/2025 10:23am)"
- Resultado: TRANQUILIDAD + demanda desestimada
```

**Dormir Tranquilo:**
- Monitorización 24/7 automática (drift, bias, incidents)
- Alertas proactivas (antes que cliente se queje)
- Evidencia compliance siempre disponible
- Soporte técnico CodeflowX (no estás solo)

---

## 💰 MODELOS PRICING AGENCIAS/CONSULTORES

### **Tier 1: BÁSICA - 1.200€/año**

**Target:** Chatbots riesgo limitado (FAQ, soporte, reservas)

**Incluye:**
- Templates transparencia Art. 50
- Checklist compliance riesgo limitado
- Documentación básica
- 1 proyecto

**Ideal para:**
- Chatbot ecommerce FAQ
- Bot WhatsApp soporte
- Chatbot reservas citas
- Agente ventas conversacional (no scoring)

---

### **Tier 2: PROFESIONAL - 12.000€/año**

**Target:** 1-2 sistemas alto riesgo pequeños

**Incluye:**
- Documentación Anexo IV automatizada (1 sistema)
- Bias detection básico (10 métricas)
- Logging inmutable
- Evaluación conformidad asistida
- Soporte email

**Ideal para:**
- Consultor freelance con 1-2 clientes alto riesgo
- Startup con MVP alto riesgo

---

### **Tier 3: ENTERPRISE - 24.000€/año** ⭐ **MÁS POPULAR**

**Target:** Agencias/software houses con múltiples clientes

**Incluye:**
- Documentación Anexo IV automatizada (hasta 5 sistemas)
- Bias detection avanzado (20+ métricas)
- Explicabilidad (SHAP/LIME)
- HITL workflows
- Drift detection 24/7
- Evaluación conformidad + Declaración UE
- Export Art. 71 DB
- Incident management
- Soporte técnico prioritario
- Onboarding 1 semana incluido

**Ideal para:**
- Agencias con 3-10 clientes alto riesgo/año
- Software houses con portafolio sistemas IA
- Integradores con práctica IA

**ROI:**
```
Inversión: 24.000€/año
Por cliente:
- Precio adicional compliance: +3.000€
- 8 clientes/año x 3.000€ = 24.000€
→ Break-even 8 clientes
→ Cliente 9+ = profit puro
```

---

### **Tier 4: SECTORES CRÍTICOS - 36.000€/año**

**Target:** HealthTech, LegalTech, FinTech

**Incluye:**
- Todo Tier 3 +
- Accuracy tracking crítico (life-critical/money-critical)
- Compliance sectorial (GDPR salud, regulación financiera)
- Consultoría legal incluida (4h/mes)
- Auditoría compliance incluida (1x año)
- Soporte 24/7

**Ideal para:**
- Startups HealthTech (triaje salud, diagnóstico)
- LegalTech (RAG legal con decisiones automáticas)
- FinTech (scoring crediticio, detección fraude)

---

## 📊 COMPARATIVA COMPETENCIA

| Característica | CodeflowX | Weights & Biases | MLflow | Azure ML Governance | AWS SageMaker Governance |
|----------------|-----------|------------------|--------|---------------------|-------------------------|
| **Bias detection** | ✅ 20+ métricas | ⚠️ Básico | ❌ No | ⚠️ Básico | ⚠️ Básico |
| **Explicabilidad** | ✅ SHAP/LIME integrado | ❌ No | ❌ No | ⚠️ Manual | ⚠️ Manual |
| **Documentación Anexo IV** | ✅ Automatizada | ❌ No | ❌ No | ❌ No | ❌ No |
| **Declaración UE Anexo V** | ✅ Automatizada | ❌ No | ❌ No | ❌ No | ❌ No |
| **Export Art. 71 DB** | ✅ 1-click | ❌ No | ❌ No | ❌ No | ❌ No |
| **HITL workflows** | ✅ BPMN integrado | ❌ No | ❌ No | ⚠️ Manual | ⚠️ Manual |
| **Drift detection** | ✅ Automático 24/7 | ✅ Sí | ⚠️ Básico | ✅ Sí | ✅ Sí |
| **Logging inmutable** | ✅ Hash chain | ❌ No | ⚠️ Básico | ⚠️ Básico | ⚠️ Básico |
| **Self-hosted** | ✅ Sí | ❌ No (solo cloud) | ✅ Sí | ❌ No | ❌ No |
| **EU AI Act focus** | ✅ 100% diseñado | ❌ No | ❌ No | ⚠️ Parcial | ⚠️ Parcial |
| **Pricing agencias** | ✅ 1.2k€ - 36k€ | 💰 >100k€/año | ✅ Open source (self-manage) | 💰 >50k€/año | 💰 >50k€/año |

### **Posicionamiento Único CodeflowX:**

```
✅ ÚNICO con documentación Anexo IV automatizada
✅ ÚNICO con Declaración UE Anexo V automatizada
✅ ÚNICO con export Art. 71 DB UE 1-click
✅ ÚNICO con HITL workflows BPMN integrados
✅ ÚNICO con logging inmutable hash chain
✅ ÚNICO diseñado 100% para EU AI Act
✅ ÚNICO con pricing accesible agencias/consultores (1.2k€ vs 100k€)
```

---

## 🚀 PROCESO ONBOARDING (1 SEMANA)

### **Día 1: Kickoff + Instalación**

```
☑️ Reunión kickoff equipo (1h)
☑️ Instalación CodeflowX (Docker Compose / Kubernetes)
☑️ Configuración infraestructura básica
☑️ Acceso credenciales
```

### **Día 2-3: Integración Sistema IA**

```
☑️ Integrar chatbot/agente/RAG con CodeflowX APIs
☑️ Configurar logging automático
☑️ Setup bias detection endpoints
☑️ Configurar evaluación modelo
```

### **Día 4: Configuración Workflows**

```
☑️ Setup HITL workflows aprobación
☑️ Configurar umbrales alertas (drift, bias)
☑️ Setup circuit breakers / kill-switch
☑️ Configurar políticas compliance
```

### **Día 5: Documentación + Testing**

```
☑️ Generar documentación Anexo IV (primera versión)
☑️ Testing bias detection
☑️ Testing explicabilidad
☑️ Validación audit trail
```

### **Día 6-7: Training + Go-Live**

```
☑️ Training equipo técnico (2h)
☑️ Training equipo negocio (1h)
☑️ Revisión documentación compliance
☑️ Go-live producción
```

**Post-Onboarding:**
- Soporte técnico continuo (email/chat)
- Actualizaciones automáticas plataforma
- Nuevas funcionalidades incluidas
- Community access (foro agencias/consultores)

---

## 📞 CONTACTO Y PRÓXIMOS PASOS

### **Opción 1: Demo 30 Minutos**

**Contenido demo:**
- Walkthrough plataforma CodeflowX
- Demo en vivo: generar documentación Anexo IV
- Demo en vivo: bias detection 20+ métricas
- Demo en vivo: explicabilidad SHAP
- Q&A

**Agenda:** [LINK CALENDLY]

---

### **Opción 2: Free Trial 14 Días**

**Incluye:**
- Acceso completo plataforma (Docker Compose local)
- Catalogar 1 sistema IA gratis
- Generar documentación Anexo IV
- Testing bias detection
- Soporte técnico trial

**Registro:** [LINK TRIAL]

---

### **Opción 3: Propuesta Comercial Personalizada**

**Proceso:**
1. Reunión discovery 30 min (conocer tus sistemas IA)
2. Análisis compliance gaps
3. Calculadora ROI personalizada
4. Propuesta comercial escrita (precio, timeline, deliverables)

**Contacto:** sales@codeflowx.ai

---

## 📚 RECURSOS ADICIONALES

### **Documentación Técnica:**
- Guía instalación: docs.codeflowx.ai/install
- API reference: docs.codeflowx.ai/api
- Tutoriales: docs.codeflowx.ai/tutorials
- FAQ: docs.codeflowx.ai/faq

### **Documentos Comerciales:**
- EU AI Act Timeline + Fases CodeflowX
- GPAI Exclusiones EU AI Act
- Proveedor vs Deployer Casos Uso
- 15 Casos Uso Agencias/Consultores ← Este documento

### **Casos de Éxito:**
- Agencia "Digital Boost": Compliance 5 clientes en 3 meses
- Software house "SmartRetail": Gana RFP vs competencia (compliance)
- Startup "FinAI Solutions": ISO 42001 certified en 6 meses

---

## 🎯 MENSAJE FINAL

### **Para Agencias/Consultores:**

> **"CodeflowX no es un gasto, es una inversión que se paga sola. Con 8 clientes/año (+3.000€ cada uno por compliance), recuperas los 24.000€ de licencia. A partir del cliente 9, es profit puro. Además, evitas demandas (50.000€+), ganas deals vs competencia y escalas tu negocio 5x. ¿Empezamos con una demo 30 min?"**

### **Diferenciador Clave:**

> **"Aunque uses GPT-4 API, si desarrollas y entregas chatbot/agente/RAG a cliente, TÚ eres PROVEEDOR. Tienes obligaciones AI Act Art. 16-21. Deadline Agosto 2026 (9 meses). Sin compliance = prohibición vender + multas 35M€. CodeflowX te hace compliance-ready en 3 meses vs 16 meses manual. First-mover wins."**

---

**Documento preparado por:** Equipo CodeflowX  
**Última actualización:** Noviembre 2025  
**Versión:** 1.0  
**Confidencialidad:** Público (uso comercial)

**Contacto:**  
📧 sales@codeflowx.ai  
🌐 www.codeflowx.ai  
📞 +34 XXX XXX XXX  
📅 [Agenda Demo](calendly.com/codeflowx)


