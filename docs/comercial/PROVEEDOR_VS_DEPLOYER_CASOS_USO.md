# PROVEEDOR VS DEPLOYER - CASOS DE USO EU AI ACT
## Guía Práctica Timeline y Argumentación Comercial

**Fecha:** Noviembre 2025  
**Versión:** 1.0  
**Objetivo:** Clarificar obligaciones y timeline según rol (proveedor/deployer) con casos de uso reales para argumentación comercial

---

## 📖 DEFINICIONES CLAVE

### **PROVEEDOR (Provider) - Art. 3.3**

> **Definición:** Persona física o jurídica, autoridad pública, agencia u otro organismo que **desarrolla** un sistema de IA o que **encarga su desarrollo** con vistas a **comercializarlo** o ponerlo en servicio con su propio nombre o marca, ya sea a título oneroso o gratuito.

**Características Proveedor:**
```
✅ Desarrolla el sistema IA (interno o contratado)
✅ Lo comercializa con su nombre/marca
✅ Lo pone en el mercado/servicio
✅ Tiene control sobre diseño/funcionalidad
```

**Ejemplos Proveedor:**
- OpenAI (proveedor GPT-4)
- Google (proveedor Vertex AI)
- Salesforce (proveedor Einstein AI)
- Startup IA que vende SaaS de predicción fraude

---

### **DEPLOYER (Usuario/Desplegador) - Art. 3.4**

> **Definición:** Persona física o jurídica, autoridad pública, agencia u otro organismo que, bajo su autoridad, **usa** un sistema de IA, salvo que el sistema se utilice en el curso de una actividad personal no profesional.

**Características Deployer:**
```
✅ Usa sistema IA de terceros (API, producto)
✅ O usa sistema IA propio internamente
✅ Bajo su autoridad/responsabilidad
✅ En contexto profesional
```

**Ejemplos Deployer:**
- Banco que usa GPT-4 API para scoring crediticio
- Hospital que usa software diagnóstico IA
- Empresa RR.HH. que usa sistema IA selección candidatos
- Ayuntamiento que usa IA reconocimiento facial vigilancia

---

### **PROVEEDOR + DEPLOYER (Doble Rol)**

**Situación:** Empresa desarrolla sistema IA Y lo usa internamente en contexto alto riesgo.

**Ejemplo:**
```
Amazon:
→ PROVEEDOR: Desarrolla AWS Rekognition (vende a terceros)
→ DEPLOYER: Usa Rekognition internamente en almacenes

Obligaciones: AMBOS roles (Art. 16 proveedor + Art. 26 deployer)
```

---

## 📅 TIMELINE ENTRADA EN VIGOR POR ROL

### **TABLA RESUMEN DEADLINES PROVEEDOR vs DEPLOYER**

| Fecha | Proveedor | Deployer | Tipo Sistema |
|-------|-----------|----------|--------------|
| **1 Ago 2024** | ✅ Ley en vigor | ✅ Ley en vigor | General |
| **2 Feb 2025** | 🔴 Prohibido comercializar sistemas Art. 5 | 🔴 Prohibido usar sistemas Art. 5 | Prohibidos |
| **2 Ago 2025** | 🔴 GPAI riesgo sistémico (Art. 51-56) | N/A | GPAI ≥10^25 FLOPs |
| **2 Ago 2026** | 🔴 Sistemas alto riesgo (Art. 9-15, 16-21) | 🔴 Sistemas alto riesgo (Art. 26-29) | Alto riesgo Anexo III |
| **2 Ago 2026** | 🟡 Sistemas riesgo limitado (Art. 50) | 🟡 Transparencia (Art. 50) | Chatbots, deepfakes |
| **2 Ago 2027** | 🔴 Productos regulados CE marking | 🟢 Uso productos conformes | Alto riesgo + CE |
| **2 Ago 2030** | 🟡 Sistemas legacy pre-2026 | 🟡 Sistemas legacy pre-2026 | Transición final |

---

## 🎯 CASOS DE USO POR SECTOR - PROVEEDOR VS DEPLOYER

---

## CASO 1: SECTOR BANCA - SCORING CREDITICIO

### **Escenario A: Banco Desarrolla Sistema IA Propio**

**Empresa:** Banco Santander  
**Sistema:** Motor scoring crediticio IA (desarrollo interno)  
**Rol:** **PROVEEDOR** (desarrolla) + **DEPLOYER** (usa internamente)

#### **Clasificación Riesgo:**
- ✅ **Alto riesgo** (Anexo III.5.a - scoring crediticio acceso crédito)

#### **Obligaciones PROVEEDOR (Art. 16-21):**

| Obligación | Artículo | Deadline | Esfuerzo Sin CodeflowX | Con CodeflowX |
|-----------|----------|----------|----------------------|---------------|
| Sistema gestión riesgo | Art. 9 | 2 Ago 2026 | 8-12 semanas | 2 semanas |
| Data governance | Art. 10 | 2 Ago 2026 | 8-10 semanas | 1-2 semanas |
| Documentación técnica Anexo IV | Art. 11 | 2 Ago 2026 | 6-8 semanas | 3-5 días |
| Logging inmutable | Art. 12 | 2 Ago 2026 | 4-6 semanas | 1 semana |
| Transparencia + instrucciones | Art. 13 | 2 Ago 2026 | 3-4 semanas | 2-3 días |
| Supervisión humana | Art. 14 | 2 Ago 2026 | 4-6 semanas | 1 semana |
| Accuracy + robustez | Art. 15 | 2 Ago 2026 | 6-8 semanas | 2 semanas |
| Sistema gestión calidad | Art. 17 | 2 Ago 2026 | 8-12 semanas | 2 semanas |
| Evaluación conformidad | Art. 43 + Anexo VI | 2 Ago 2026 | 12-16 semanas | 2-4 semanas |
| Declaración UE | Anexo V | 2 Ago 2026 | 2-3 semanas | 2-3 días |
| Registro Art. 71 DB | Art. 71 | 2 Ago 2026 | 2 semanas | 1 día |

**TOTAL:** 63-95 semanas (15-23 meses) | Con CodeflowX: 11-15 semanas (2.5-3.5 meses)

#### **Obligaciones DEPLOYER (Art. 26-29):**

| Obligación | Artículo | Deadline | Esfuerzo Sin CodeflowX | Con CodeflowX |
|-----------|----------|----------|----------------------|---------------|
| Usar conforme instrucciones | Art. 26.1 | 2 Ago 2026 | Operativo | Automatizado |
| Supervisión humana efectiva | Art. 26.5 | 2 Ago 2026 | 4 semanas | 1 semana |
| Monitorización operación | Art. 26.2 | 2 Ago 2026 | Continuo | Automatizado |
| Logging uso | Art. 26.3 | 2 Ago 2026 | 2 semanas | Incluido |
| Reporting incidentes | Art. 26.8 | 2 Ago 2026 | Ad-hoc | Automatizado |
| DPIA (si autoridad pública) | Art. 27 | 2 Ago 2026 | 6-8 semanas | 1 semana |

**TOTAL:** 12-14 semanas | Con CodeflowX: 2 semanas (automatizado continuo)

#### **Argumentación Comercial:**

```
MENSAJE:
"Como banco que desarrolla Y usa scoring crediticio IA, tienes DOBLE rol:
- PROVEEDOR: Art. 9-21 (desarrollo conforme)
- DEPLOYER: Art. 26-29 (uso responsable)

Sin CodeflowX: 77-109 semanas (18-25 meses) → IMPOSIBLE cumplir Agosto 2026
Con CodeflowX: 13-17 semanas (3-4 meses) → COMPLIANCE GARANTIZADO

Deadline: 2 Agosto 2026 (9 meses) → ACTUAR YA

Ahorro: 180.000 - 250.000€ en consultores + multas evitadas (hasta 35M€)"
```

---

### **Escenario B: Banco Usa API Terceros para Scoring**

**Empresa:** Banco BBVA  
**Sistema:** Usa API "FraudDetect AI" de vendor externo para scoring fraude  
**Rol:** **DEPLOYER SOLO** (no desarrolla, solo usa)

#### **Clasificación Riesgo:**
- ✅ **Alto riesgo** (Anexo III.5.a - scoring crediticio acceso crédito)

#### **Obligaciones DEPLOYER (Art. 26-29):**

**CRÍTICO:** BBVA NO tiene obligaciones PROVEEDOR (las tiene el vendor), PERO SÍ tiene obligaciones DEPLOYER completas.

| Obligación | Artículo | Deadline | Descripción |
|-----------|----------|----------|-------------|
| Verificar conformidad proveedor | Art. 26.10 | 2 Ago 2026 | Pedir Declaración UE + documentación |
| Usar conforme instrucciones | Art. 26.1 | 2 Ago 2026 | Seguir manual uso proveedor |
| Supervisión humana efectiva | Art. 26.5 | 2 Ago 2026 | Human-in-the-loop decisiones scoring |
| Monitorización continua | Art. 26.2 | 2 Ago 2026 | Detectar anomalías/drift |
| Input data quality | Art. 26.3 | 2 Ago 2026 | Datos entrada calidad suficiente |
| Logging uso | Art. 26.3 | 2 Ago 2026 | Logs uso interno |
| Reporting incidentes graves | Art. 26.8 | 2 Ago 2026 | Notificar proveedor + autoridad |
| DPIA (si aplica) | Art. 27 | 2 Ago 2026 | Evaluación impacto derechos fundamentales |
| Registro interno | Art. 26.11 | 2 Ago 2026 | Registro uso sistemas alto riesgo |

**Esfuerzo:** 12-14 semanas | Con CodeflowX: 2 semanas setup + monitorización automatizada

#### **Argumentación Comercial:**

```
MENSAJE:
"Aunque uses API terceros (FraudDetect AI), NO te libra de obligaciones.
Como DEPLOYER de sistema alto riesgo, debes cumplir Art. 26-29:

✅ Verificar que proveedor tenga Declaración UE + Anexo IV
✅ Supervisión humana efectiva (no decisión 100% automatizada)
✅ Monitorización continua performance
✅ Logging uso interno
✅ Reporting incidentes graves

CodeflowX te ayuda:
- Verificación automática conformidad proveedor (check Declaración UE)
- HITL workflows supervisión humana
- Monitorización drift/anomalías automática
- Logging inmutable uso
- Incident management integrado

Deadline: 2 Agosto 2026 (9 meses)
Esfuerzo: 2 semanas setup + automatizado vs 12-14 semanas manual

Multas deployer: hasta 15M€ o 3% facturación global"
```

---

## CASO 2: SECTOR SALUD - DIAGNÓSTICO MÉDICO IA

### **Escenario A: Philips (Proveedor Software Diagnóstico)**

**Empresa:** Philips Healthcare  
**Sistema:** Software IA diagnóstico cáncer mama (radiología)  
**Rol:** **PROVEEDOR** (desarrolla + comercializa)

#### **Clasificación Riesgo:**
- ✅ **Alto riesgo** (Anexo III.1.a - salud/seguridad personas)
- ✅ **Producto regulado** (MDR 2017/745 Medical Device Regulation)

#### **Obligaciones PROVEEDOR (Art. 16-21 + MDR):**

**CRÍTICO:** Doble regulación AI Act + MDR → CE marking obligatorio

| Obligación | Regulación | Deadline | Complejidad |
|-----------|------------|----------|-------------|
| Evaluación conformidad Anexo VI | AI Act | 2 Ago 2027 | 🔴 ALTA (notified body) |
| Documentación técnica Anexo IV | AI Act | 2 Ago 2027 | 🔴 ALTA |
| Sistema gestión calidad ISO 13485 | MDR | 2 Ago 2027 | 🔴 ALTA |
| Clinical evaluation | MDR | 2 Ago 2027 | 🔴 ALTA |
| Post-market surveillance | MDR + AI Act | 2 Ago 2027 | 🔴 ALTA |
| CE marking | AI Act + MDR | 2 Ago 2027 | 🔴 CRÍTICA |
| Registro EUDAMED | MDR | 2 Ago 2027 | 🟡 MEDIA |
| Registro Art. 71 DB | AI Act | 2 Ago 2027 | 🟡 MEDIA |

**Deadline especial:** 2 Agosto 2027 (productos regulados CE tienen 1 año extra)

**Esfuerzo:** 80-120 semanas (18-24 meses) | Con CodeflowX: 20-30 semanas (5-7 meses)

#### **Argumentación Comercial:**

```
MENSAJE:
"Como fabricante dispositivo médico con IA, tienes DOBLE regulación:
- AI Act (sistema alto riesgo salud)
- MDR 2017/745 (dispositivo médico)

CE marking obligatorio = Evaluación conformidad notified body + ISO 13485

Deadline: 2 Agosto 2027 (20 meses)
Complejidad: ALTÍSIMA (doble compliance)

CodeflowX ayuda en parte AI Act:
✅ Documentación técnica Anexo IV automatizada
✅ Post-market surveillance IA integrado
✅ Logging + monitorización Art. 12 + 72
✅ Export Art. 71 DB formato estándar

PERO: Necesitas consultores MDR + notified body (no reemplazable).

Valor CodeflowX: Reduce 50-60% esfuerzo parte AI Act (20-30 semanas ahorro)
Ahorro: 60.000 - 90.000€ en consultores AI Act"
```

---

### **Escenario B: Hospital Usa Software Diagnóstico (Deployer)**

**Empresa:** Hospital Universitario Madrid  
**Sistema:** Usa software Philips IA diagnóstico (comprado con CE marking)  
**Rol:** **DEPLOYER**

#### **Clasificación Riesgo:**
- ✅ **Alto riesgo** (Anexo III.1.a - salud)

#### **Obligaciones DEPLOYER (Art. 26-29):**

| Obligación | Artículo | Deadline | Descripción |
|-----------|----------|----------|-------------|
| Verificar CE marking | Art. 26.10 | 2 Ago 2026 | Comprobar Declaración UE + certificado |
| Supervisión humana médica | Art. 26.5 | 2 Ago 2026 | Médico revisa diagnóstico IA SIEMPRE |
| Condiciones uso adecuadas | Art. 26.1 | 2 Ago 2026 | Seguir instrucciones fabricante (IFU) |
| Monitorización performance | Art. 26.2 | 2 Ago 2026 | Detectar si empeora precisión |
| Input data quality | Art. 26.3 | 2 Ago 2026 | Imágenes radiológicas calidad suficiente |
| Logging uso | Art. 26.3 | 2 Ago 2026 | Registro uso diagnósticos IA |
| Reporting incidentes graves | Art. 26.8 | 2 Ago 2026 | Notificar error diagnóstico grave |
| DPIA/FRIA (autoridad pública) | Art. 27 | 2 Ago 2026 | Evaluación impacto derechos fundamentales |

**Esfuerzo:** 10-12 semanas | Con CodeflowX: 1-2 semanas setup + automatizado

#### **Argumentación Comercial:**

```
MENSAJE:
"Como hospital usando software diagnóstico IA (aunque tenga CE marking), 
tienes obligaciones DEPLOYER Art. 26-29:

✅ Verificar CE marking válido (Philips debe proporcionar)
✅ Supervisión humana SIEMPRE (médico revisa diagnóstico IA)
✅ Monitorización performance (detectar si IA empeora)
✅ Logging uso + reporting incidentes graves
✅ FRIA (Art. 27) porque eres autoridad pública

CodeflowX para hospitales:
- Dashboard monitorización performance IA en tiempo real
- HITL workflow médico (supervisión humana documentada)
- Logging automático uso sistemas IA
- FRIA generator automatizado (Art. 27)
- Incident management + reporting automatizado

Deadline: 2 Agosto 2026 (9 meses)
Esfuerzo: 1-2 semanas setup vs 10-12 semanas manual
Ahorro: 30.000 - 40.000€ + compliance garantizado

Multas: hasta 15M€ si incidente grave sin reporting"
```

---

## CASO 3: SECTOR RR.HH. - SELECCIÓN CANDIDATOS IA

### **Escenario A: LinkedIn/HireVue (Proveedor Plataforma Reclutamiento)**

**Empresa:** HireVue (vendor SaaS selección candidatos IA)  
**Sistema:** Plataforma IA análisis video entrevistas + scoring candidatos  
**Rol:** **PROVEEDOR**

#### **Clasificación Riesgo:**
- ✅ **Alto riesgo** (Anexo III.4.a - empleo selección candidatos)

#### **Obligaciones PROVEEDOR (Art. 16-21):**

| Obligación | Deadline | Crítico |
|-----------|----------|---------|
| Sistema gestión riesgo (Art. 9) | 2 Ago 2026 | 🔴 |
| Data governance + bias detection (Art. 10) | 2 Ago 2026 | 🔴 CRÍTICA |
| Documentación técnica Anexo IV | 2 Ago 2026 | 🔴 |
| Logging inmutable (Art. 12) | 2 Ago 2026 | 🔴 |
| Transparencia candidatos (Art. 13) | 2 Ago 2026 | 🔴 CRÍTICA |
| Supervisión humana (Art. 14) | 2 Ago 2026 | 🔴 CRÍTICA |
| Accuracy + fairness (Art. 15) | 2 Ago 2026 | 🔴 CRÍTICA |
| Evaluación conformidad (Anexo VI) | 2 Ago 2026 | 🔴 |
| Declaración UE (Anexo V) | 2 Ago 2026 | 🔴 |
| Registro Art. 71 DB | 2 Ago 2026 | 🔴 |

**CRITICIDAD ESPECIAL:**
- Sesgo demográfico (género, edad, etnia) → Art. 10.2 compliance CRÍTICO
- Transparencia candidatos → Deben saber que IA decide
- GDPR Art. 22 → Decisiones automatizadas

**Esfuerzo:** 50-70 semanas | Con CodeflowX: 10-14 semanas

#### **Argumentación Comercial:**

```
MENSAJE:
"Como proveedor plataforma reclutamiento IA, tienes uno de los casos 
más SENSIBLES del AI Act (Anexo III.4.a empleo):

RIESGOS CRÍTICOS:
🔴 Sesgo demográfico → Discriminación ilegal
🔴 Transparencia candidatos → Deben saber que IA decide
🔴 GDPR Art. 22 → Derecho no decisión 100% automatizada
🔴 Multas: hasta 35M€ + escándalo reputacional

CodeflowX ESENCIAL para RR.HH. IA:
✅ leka-bias-detection: 20+ métricas fairness (género, edad, etnia)
✅ Documentación Anexo IV: explica cómo sistema evita sesgo
✅ Transparencia: genera notificaciones candidatos Art. 13
✅ HITL workflows: supervisión humana documentada
✅ Audit trail completo: evidencia no discriminación

Deadline: 2 Agosto 2026 (9 meses)
Esfuerzo: 10-14 semanas con CodeflowX vs 50-70 semanas manual
Ahorro: 150.000 - 210.000€

SIN COMPLIANCE = PROHIBICIÓN OPERAR + MULTAS + DEMANDAS"
```

---

### **Escenario B: Empresa Usa Plataforma RR.HH. IA (Deployer)**

**Empresa:** Telefónica (usa HireVue para seleccionar candidatos)  
**Sistema:** Plataforma HireVue IA análisis entrevistas  
**Rol:** **DEPLOYER**

#### **Clasificación Riesgo:**
- ✅ **Alto riesgo** (Anexo III.4.a - empleo)

#### **Obligaciones DEPLOYER (Art. 26-29):**

| Obligación | Artículo | Deadline | Descripción |
|-----------|----------|----------|-------------|
| Verificar conformidad HireVue | Art. 26.10 | 2 Ago 2026 | Pedir Declaración UE + Anexo IV |
| Supervisión humana RR.HH. | Art. 26.5 | 2 Ago 2026 | Reclutador humano SIEMPRE decide final |
| Informar candidatos | Art. 26.7 | 2 Ago 2026 | "Sistema IA analiza tu entrevista" |
| Monitorización bias | Art. 26.2 | 2 Ago 2026 | Detectar sesgo en scoring |
| Logging uso | Art. 26.3 | 2 Ago 2026 | Registro candidatos evaluados IA |
| Reporting incidentes | Art. 26.8 | 2 Ago 2026 | Notificar sesgo detectado |
| DPIA (si autoridad pública) | Art. 27 | 2 Ago 2026 | Solo sector público |

**Esfuerzo:** 8-10 semanas | Con CodeflowX: 1-2 semanas setup + automatizado

#### **Argumentación Comercial:**

```
MENSAJE:
"Aunque uses HireVue (vendor externo), TÚ eres responsable como DEPLOYER 
ante candidatos rechazados que demanden por discriminación.

OBLIGACIONES DEPLOYER Art. 26-29:
✅ Verificar que HireVue tenga Declaración UE conforme
✅ Supervisión humana: reclutador SIEMPRE decide final (no 100% automatizado)
✅ Informar candidatos: "IA analiza entrevista" (Art. 26.7)
✅ Monitorización bias: detectar si scoring sesgado por género/edad
✅ Logging + reporting incidentes

RIESGO LEGAL:
- Candidato rechazado demanda por discriminación
- Empresa debe demostrar: supervisión humana + no sesgo + transparencia
- SIN evidencia = pérdida demanda + multas AI Act

CodeflowX para RR.HH. deployer:
✅ Dashboard monitorización bias tiempo real
✅ HITL workflow reclutador (evidencia supervisión humana)
✅ Notificaciones automáticas candidatos (Art. 26.7)
✅ Logging inmutable uso IA (evidencia auditoría)
✅ Incident reporting automatizado

Deadline: 2 Agosto 2026 (9 meses)
Esfuerzo: 1-2 semanas setup vs 8-10 semanas manual
Ahorro: 24.000 - 30.000€ + protección legal

DEMANDA PERDIDA = 50.000 - 200.000€ + reputación dañada"
```

---

## CASO 4: SECTOR PÚBLICO - RECONOCIMIENTO FACIAL VIGILANCIA

### **Escenario A: Ayuntamiento Madrid (Deployer Sistema Vigilancia)**

**Empresa:** Ayuntamiento de Madrid  
**Sistema:** Cámaras reconocimiento facial en estaciones metro (vendor Hikvision)  
**Rol:** **DEPLOYER** (autoridad pública)

#### **Clasificación Riesgo:**
- ⚠️ **DEPENDE contexto:**
  - Identificación remota biométrica tiempo real espacio público → **PROHIBIDO** (Art. 5.1.h) con excepciones estrictas
  - Identificación post-evento (no tiempo real) → **Alto riesgo** (Anexo III.1.a + Recital 34)

**CRÍTICO:** Art. 5 prohíbe RBI (Remote Biometric Identification) tiempo real salvo excepciones:
- Búsqueda víctimas secuestro
- Prevención amenaza terrorista inminente
- Localización sospechosos delitos graves (Anexo II)

#### **Si RBI tiempo real (prohibido salvo excepción):**

| Requisito | Artículo | Descripción |
|-----------|----------|-------------|
| Autorización judicial previa | Art. 5.1.h.ii | Juez debe autorizar CADA uso |
| Limitado en tiempo/lugar | Art. 5.1.h.i | No vigilancia masiva permanente |
| Notificación autoridad supervisora | Art. 5.1.h.iii | AI Office + autoridad datos |
| Logging completo | Art. 5.1.h + Art. 26 | Registro TODOS los usos |
| Supervisión humana estricta | Art. 5.1.h + Art. 26 | Oficial humano SIEMPRE revisa |

#### **Si identificación post-evento (alto riesgo):**

| Obligación | Artículo | Deadline | Descripción |
|-----------|----------|----------|-------------|
| FRIA obligatorio | Art. 27 | 2 Ago 2026 | Fundamental Rights Impact Assessment |
| Verificar conformidad vendor | Art. 26.10 | 2 Ago 2026 | Hikvision debe tener Declaración UE |
| Supervisión humana | Art. 26.5 | 2 Ago 2026 | Oficial humano verifica identificación |
| Informar personas afectadas | Art. 26.7 + GDPR | 2 Ago 2026 | Carteles "vigilancia IA reconocimiento facial" |
| Logging inmutable | Art. 26.3 | 2 Ago 2026 | Registro TODOS los usos |
| DPIA GDPR | GDPR Art. 35 | 2 Ago 2026 | Data Protection Impact Assessment |
| Consulta DPO | GDPR Art. 35 | 2 Ago 2026 | Data Protection Officer |

**Esfuerzo:** 16-20 semanas | Con CodeflowX: 3-4 semanas

#### **Argumentación Comercial:**

```
MENSAJE:
"Reconocimiento facial vigilancia = caso MÁS SENSIBLE AI Act:

ESCENARIO 1: RBI tiempo real (vigilancia continua)
→ PROHIBIDO Art. 5.1.h salvo excepciones terrorismo/secuestro/delitos graves
→ Requiere autorización judicial CADA uso
→ Multas: hasta 35M€ o 7% presupuesto
→ Escándalo reputacional ENORME

ESCENARIO 2: Identificación post-evento (revisar grabaciones)
→ Alto riesgo (Anexo III.1.a)
→ FRIA obligatorio Art. 27 (autoridad pública)
→ DPIA GDPR obligatorio
→ Transparencia ciudadanos (carteles informativos)

CodeflowX para sector público vigilancia:
✅ FRIA generator automatizado (Art. 27)
✅ Logging inmutable uso RBI (evidencia autorización judicial)
✅ HITL workflow oficial seguridad (supervisión humana documentada)
✅ DPIA GDPR integrado
✅ Dashboard transparencia ciudadanos (uso sistemas IA público)
✅ Incident reporting automatizado

Deadline: 2 Agosto 2026 (9 meses)
Esfuerzo: 3-4 semanas con CodeflowX vs 16-20 semanas manual
Ahorro: 48.000 - 60.000€

RIESGO REPUTACIONAL:
- Escándalo vigilancia masiva ilegal
- Demandas ciudadanos LOPD
- Multas 35M€ + prohibición uso

RECOMENDACIÓN: Evaluación legal exhaustiva ANTES despliegue."
```

---

## CASO 5: SECTOR SEGUROS - TARIFICACIÓN IA

### **Escenario A: Mapfre (Proveedor + Deployer)**

**Empresa:** Mapfre Seguros  
**Sistema:** Motor IA tarificación seguros salud/vida  
**Rol:** **PROVEEDOR** (desarrolla) + **DEPLOYER** (usa internamente)

#### **Clasificación Riesgo:**
- ✅ **Alto riesgo** (Anexo III.5.b - evaluación y fijación primas seguros vida/salud)

#### **Obligaciones PROVEEDOR + DEPLOYER (Art. 16-21 + 26-29):**

**Obligaciones clave:**

| Obligación | Artículo | Deadline | Criticidad |
|-----------|----------|----------|------------|
| Data governance + no discriminación | Art. 10 | 2 Ago 2026 | 🔴 CRÍTICA |
| Explicabilidad decisión tarificación | Art. 13 | 2 Ago 2026 | 🔴 CRÍTICA |
| Supervisión humana decisiones | Art. 14 + 26.5 | 2 Ago 2026 | 🔴 CRÍTICA |
| Accuracy + robustez actuarial | Art. 15 | 2 Ago 2026 | 🔴 |
| Documentación técnica Anexo IV | Art. 11 | 2 Ago 2026 | 🔴 |
| Evaluación conformidad | Anexo VI | 2 Ago 2026 | 🔴 |
| Logging inmutable | Art. 12 + 26.3 | 2 Ago 2026 | 🔴 |
| Transparencia asegurado | Art. 13 + 26.7 | 2 Ago 2026 | 🔴 CRÍTICA |

**CRITICIDAD ESPECIAL:**
- No discriminación (género, edad, salud) → Art. 10.2
- Explicabilidad → Asegurado tiene derecho entender por qué prima X
- GDPR Art. 22 → Decisiones automatizadas

**Esfuerzo:** 60-80 semanas | Con CodeflowX: 12-16 semanas

#### **Argumentación Comercial:**

```
MENSAJE:
"Tarificación seguros IA = caso sensible (Anexo III.5.b):

RIESGOS CRÍTICOS:
🔴 Discriminación ilegal (género, salud, edad)
🔴 Explicabilidad → Asegurado debe entender tarifa
🔴 GDPR Art. 22 → Derecho oposición decisión automatizada
🔴 Multas: hasta 35M€ + demandas consumidores

CodeflowX para seguros:
✅ Bias detection: fairness metrics seguros (protected attributes)
✅ Explainability: genera explicaciones tarificación Art. 13
✅ HITL workflows: actuario supervisa tarifas extremas
✅ Documentación Anexo IV: justifica modelo actuarial
✅ Logging inmutable: evidencia supervisión humana
✅ Audit trail: demuestra no discriminación

Deadline: 2 Agosto 2026 (9 meses)
Esfuerzo: 12-16 semanas con CodeflowX vs 60-80 semanas manual
Ahorro: 180.000 - 240.000€

RIESGO LEGAL:
- Demanda consumidor discriminación
- Sanción DPA GDPR Art. 22
- Multas AI Act 35M€
- Escándalo reputacional (prensa)"
```

---

## CASO 6: PROVEEDOR GPAI (OpenAI, Anthropic)

### **Escenario: OpenAI GPT-4 (Proveedor GPAI Riesgo Sistémico)**

**Empresa:** OpenAI  
**Sistema:** GPT-4 (modelo GPAI riesgo sistémico ≥10^25 FLOPs)  
**Rol:** **PROVEEDOR GPAI**

#### **Clasificación:**
- ✅ **GPAI con riesgo sistémico** (Art. 51-56)

#### **Obligaciones PROVEEDOR GPAI (Art. 51-56):**

| Obligación | Artículo | Deadline | Descripción |
|-----------|----------|----------|-------------|
| Evaluación + mitigación riesgos sistémicos | Art. 51.1.a | 2 Ago 2025 ⏰ | Evaluar riesgos ciberseguridad, salud, etc. |
| Adversarial testing | Art. 51.1.b | 2 Ago 2025 ⏰ | Test robustez adversarial |
| Evaluación + mitigación graves | Art. 51.1.c | 2 Ago 2025 ⏰ | Riesgos graves para salud/derechos |
| Seguimiento incidentes graves | Art. 51.1.d | 2 Ago 2025 ⏰ | Tracking + reporting incidentes |
| Ciberseguridad modelo + infra | Art. 51.1.e | 2 Ago 2025 ⏰ | Protección leak pesos modelo |
| Eficiencia energética | Art. 51.1.f | 2 Ago 2025 ⏰ | Report CO2, energy, water |
| Notificación AI Office | Art. 52.1 | 2 Ago 2025 ⏰ | Notificar riesgo sistémico |
| Documentación técnica | Art. 53.1.a | 2 Ago 2025 ⏰ | Modelo card completo |
| Política respeto copyright | Art. 53.1.b | 2 Ago 2025 ⏰ | Copyright training data |
| Resumen público datos | Art. 53.1.d | 2 Ago 2025 ⏰ | Dataset público resumido |
| Cumplimiento Código Conducta | Art. 56 | 2 Ago 2025 ⏰ | Código conducta voluntario |

**Deadline:** 2 Agosto 2025 (⏰ **URGENTE - 9 MESES YA PASADOS**)

**Esfuerzo:** 100-150 semanas | Con CodeflowX: 20-30 semanas

#### **Argumentación Comercial:**

```
MENSAJE:
"Como proveedor GPAI riesgo sistémico (GPT-4 ≥10^25 FLOPs), tienes 
obligaciones MÁS ESTRICTAS que sistemas alto riesgo normales:

DEADLINE: 2 Agosto 2025 (YA VIGENTE)
→ OpenAI/Anthropic/Google DEBEN cumplir YA

Obligaciones Art. 51-56:
🔴 Adversarial testing obligatorio (evasion, poisoning, jailbreak)
🔴 Evaluación riesgos sistémicos (ciberseguridad, salud, derechos)
🔴 Seguimiento incidentes graves + reporting
🔴 Ciberseguridad leak pesos modelo
🔴 Eficiencia energética (CO2, energy, water)
🔴 Notificación AI Office

CodeflowX para proveedores GPAI:
✅ leka-adversarial-robustness: testing automatizado
✅ leka-agent-monitoring: seguimiento incidentes
✅ leka-sustainability-metrics: CO2/energy/water
✅ Export Art. 52.1 notificación AI Office
✅ Modelo card generator (Art. 53.1.a)
✅ Copyright policy generator (Art. 53.1.b)

Esfuerzo: 20-30 semanas con CodeflowX vs 100-150 semanas manual
Ahorro: 300.000 - 450.000€

Multas: hasta 15M€ o 3% facturación global
Riesgo reputacional: ENORME (OpenAI, Google, Anthropic)"
```

---

## 📊 TABLA RESUMEN: DEADLINE POR ROL Y SECTOR

| Sector | Rol | Sistema | Riesgo | Deadline | Urgencia |
|--------|-----|---------|--------|----------|----------|
| **Banca** | Proveedor+Deployer | Scoring crediticio | Alto | 2 Ago 2026 | 🔴 9 meses |
| **Banca** | Deployer | API fraude tercero | Alto | 2 Ago 2026 | 🔴 9 meses |
| **Salud** | Proveedor | Software diagnóstico | Alto + CE | 2 Ago 2027 | 🟡 20 meses |
| **Salud** | Deployer | Usa software diagnóstico | Alto | 2 Ago 2026 | 🔴 9 meses |
| **RR.HH.** | Proveedor | Plataforma reclutamiento | Alto | 2 Ago 2026 | 🔴 9 meses |
| **RR.HH.** | Deployer | Usa plataforma reclutamiento | Alto | 2 Ago 2026 | 🔴 9 meses |
| **Público** | Deployer | Reconocimiento facial | Prohibido/Alto | 2 Ago 2026 | 🔴 CRÍTICO |
| **Seguros** | Proveedor+Deployer | Tarificación IA | Alto | 2 Ago 2026 | 🔴 9 meses |
| **GPAI** | Proveedor | GPT-4, Gemini Ultra | Riesgo sistémico | 2 Ago 2025 | 🔴 YA VIGENTE |

---

## 💰 ROI CODEFLOWX POR ROL

### **PROVEEDOR Sistema Alto Riesgo:**

```
Sin CodeflowX: 63-95 semanas + 192.500€
Con CodeflowX: 11-15 semanas + 42.000€
Ahorro: 150.500€ + 52-80 semanas (82%)
```

### **DEPLOYER Sistema Alto Riesgo:**

```
Sin CodeflowX: 12-14 semanas + 36.000€
Con CodeflowX: 2 semanas + 12.000€
Ahorro: 24.000€ + 10-12 semanas (83%)
```

### **PROVEEDOR GPAI Riesgo Sistémico:**

```
Sin CodeflowX: 100-150 semanas + 450.000€
Con CodeflowX: 20-30 semanas + 90.000€
Ahorro: 360.000€ + 70-120 semanas (80%)
```

---

## 📋 CHECKLIST DISCOVERY: IDENTIFICAR ROL CLIENTE

Use esta checklist en la primera llamada para identificar rol y obligaciones:

```
┌─────────────────────────────────────────────────────────────────┐
│ CHECKLIST IDENTIFICACIÓN ROL                                    │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│ 1. ¿Cliente DESARROLLA sistema IA?                             │
│    [ ] SÍ → PROVEEDOR (Art. 16-21)                             │
│    [ ] NO → Ir pregunta 2                                      │
│                                                                 │
│ 2. ¿Cliente USA sistema IA terceros (API, producto)?          │
│    [ ] SÍ → DEPLOYER (Art. 26-29)                              │
│    [ ] NO → No aplica AI Act                                   │
│                                                                 │
│ 3. ¿Cliente desarrolla Y usa internamente?                    │
│    [ ] SÍ → PROVEEDOR + DEPLOYER (Art. 16-21 + 26-29)         │
│                                                                 │
│ 4. ¿Sistema es alto riesgo (Anexo III)?                       │
│    [ ] Empleo/RR.HH. → SÍ (Anexo III.4)                        │
│    [ ] Banca/crédito/seguros → SÍ (Anexo III.5)                │
│    [ ] Salud/seguridad → SÍ (Anexo III.1-2)                    │
│    [ ] Educación → SÍ (Anexo III.3)                            │
│    [ ] Justicia/migración → SÍ (Anexo III.6-7-8)               │
│    [ ] Infraestructuras críticas → SÍ (Anexo III.2)            │
│    [ ] Otro → Evaluar caso                                     │
│                                                                 │
│ 5. ¿Cliente es autoridad pública?                             │
│    [ ] SÍ → FRIA obligatorio (Art. 27)                         │
│    [ ] NO → FRIA voluntario                                    │
│                                                                 │
│ 6. ¿Sistema es GPAI ≥10^25 FLOPs?                             │
│    [ ] SÍ → Obligaciones Art. 51-56 (deadline Ago 2025 ⏰)     │
│    [ ] NO → Solo Art. 9-15 o 26-29                             │
│                                                                 │
│ 7. ¿Sistema es producto regulado (MDR, IVD, etc.)?            │
│    [ ] SÍ → CE marking + deadline Ago 2027 (1 año extra)      │
│    [ ] NO → Deadline Ago 2026                                  │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 📧 EMAIL TEMPLATE POR ROL

### **Template 1: Cliente PROVEEDOR Sistema Alto Riesgo**

**Asunto:** [EMPRESA] - Compliance AI Act Agosto 2026 (Proveedor)

```
Hola [NOMBRE],

Como PROVEEDOR de [SISTEMA IA], tienes obligaciones críticas EU AI Act 
deadline 2 Agosto 2026 (solo 9 meses):

OBLIGACIONES PROVEEDOR (Art. 16-21):
✅ Sistema gestión riesgo (Art. 9)
✅ Data governance + bias detection (Art. 10)
✅ Documentación técnica Anexo IV (Art. 11)
✅ Logging inmutable (Art. 12)
✅ Transparencia + instrucciones (Art. 13)
✅ Supervisión humana (Art. 14)
✅ Accuracy + robustez (Art. 15)
✅ Sistema gestión calidad (Art. 17)
✅ Evaluación conformidad (Anexo VI)
✅ Declaración UE (Anexo V)
✅ Registro Art. 71 DB

Sin CodeflowX: 63-95 semanas (15-23 meses) → IMPOSIBLE cumplir
Con CodeflowX: 11-15 semanas (2.5-3.5 meses) → COMPLIANCE GARANTIZADO

Ahorro: 150.500€ + 52-80 semanas
Multas evitadas: hasta 35M€ o 7% facturación

¿15 min demo esta semana?
[LINK calendly]

Saludos,
[TU NOMBRE]
```

---

### **Template 2: Cliente DEPLOYER Sistema Alto Riesgo**

**Asunto:** [EMPRESA] - Obligaciones Deployer AI Act (uso IA terceros)

```
Hola [NOMBRE],

Aunque uses sistema IA de terceros (API [VENDOR]), tienes obligaciones 
como DEPLOYER EU AI Act deadline 2 Agosto 2026:

OBLIGACIONES DEPLOYER (Art. 26-29):
✅ Verificar conformidad proveedor (Declaración UE)
✅ Supervisión humana efectiva
✅ Monitorización continua performance
✅ Logging uso interno
✅ Reporting incidentes graves

RIESGO:
→ Multas hasta 15M€ si no cumples Art. 26-29
→ Demandas usuarios afectados (si RR.HH., seguros, crédito)
→ Responsabilidad legal aunque uses API tercero

CodeflowX para deployers:
✅ Verificación automática conformidad proveedor
✅ HITL workflows supervisión humana
✅ Monitorización drift/anomalías
✅ Logging inmutable + incident management

Esfuerzo: 2 semanas setup vs 12-14 semanas manual
Ahorro: 24.000€ + protección legal

¿Demo 30 min?
[LINK calendly]

Saludos,
[TU NOMBRE]
```

---

## 🎯 CONCLUSIÓN COMERCIAL

### **MENSAJES CLAVE POR ROL:**

#### **Para PROVEEDORES:**
```
"Como PROVEEDOR, tienes obligaciones MÁS COMPLEJAS (Art. 16-21):
- Documentación técnica Anexo IV
- Evaluación conformidad + Declaración UE
- Sistema gestión calidad
- Registro Art. 71 DB

Esfuerzo: 63-95 semanas manual vs 11-15 semanas con CodeflowX
Ahorro: 150.500€ (82%)
Deadline: 2 Agosto 2026 (9 meses) → ACTUAR YA"
```

#### **Para DEPLOYERS:**
```
"Como DEPLOYER, aunque uses sistema terceros, TÚ eres responsable 
ante usuarios afectados (Art. 26-29):
- Supervisión humana efectiva
- Monitorización continua
- Reporting incidentes

Esfuerzo: 12-14 semanas manual vs 2 semanas con CodeflowX
Ahorro: 24.000€ (83%)
Deadline: 2 Agosto 2026 (9 meses) → ACTUAR YA"
```

#### **Para PROVEEDOR + DEPLOYER:**
```
"Doble rol = DOBLE obligaciones (Art. 16-21 + 26-29):
- PROVEEDOR: compliance desarrollo (Anexo IV, evaluación, etc.)
- DEPLOYER: compliance uso (supervisión, monitorización, etc.)

Esfuerzo: 75-109 semanas manual vs 13-17 semanas con CodeflowX
Ahorro: 180.000€ (82%)
Deadline: 2 Agosto 2026 (9 meses) → URGENCIA MÁXIMA"
```

---

**Documento preparado por:** Equipo CodeflowX  
**Última actualización:** Noviembre 2025  
**Versión:** 1.0  
**Confidencialidad:** Uso interno equipo comercial


