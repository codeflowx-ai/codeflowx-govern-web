# GUÍA EJECUCIÓN PROMPTS PENDIENTES
## Plan Priorizado Implementación ~122 Prompts

**Fecha:** 5 Noviembre 2025  
**Estado Actual:** 28/150 prompts (19%)  
**Objetivo:** Roadmap ejecutar 122 prompts pendientes  
**Timeline:** Q4 2024 - Q2 2025

---

## 🎯 ESTRATEGIA EJECUCIÓN

### **Principio:** Valor incremental por sprint

**Priorización basada en:**
1. 🔴 **Compliance crítico** (deadlines legales)
2. 🟡 **Valor comercial** (demos, ventas)
3. 🟢 **Nice-to-have** (mejoras)

---

## 📅 FASE 1: CRÍTICO COMPLIANCE (Semanas 1-4)

### **SPRINT 1 (Semana 1) - GPAI Compliance 🔴**

**Objetivo:** Art. 51-55 GPAI (deadline 2 Ago 2025)

**PROMPTS_06 - MLOps Adapters Fine-tuning:**

- [ ] **PROMPT 2** - ModelAdaptationBusinessService (2-3 días)
  - Service tracking fine-tuning, LoRA, merge
  - Lineage model chains
  - **Esfuerzo:** 2-3 días (1 chat)
  - **Valor:** 🔴 CRÍTICO - GPAI compliance

- [ ] **PROMPT 3** - BPMN adapter-creation-approval (1 día)
  - Workflow aprobación adapters
  - **Esfuerzo:** 1 día (1 chat)

- [ ] **PROMPT 4** - BPMN finetuning-approval-v1 (1 día)
  - Workflow aprobación fine-tuning
  - **Esfuerzo:** 1 día (1 chat)

- [ ] **PROMPT 5** - Python validators lineage (2 días)
  - Validators model lineage chains
  - **Esfuerzo:** 2 días (1 chat)

- [ ] **PROMPT 6** - UI lineage trees (2 días)
  - Visualización árbol lineage
  - **Esfuerzo:** 2 días (1 chat)

**Total Sprint 1:** 5 prompts, 8-9 días esfuerzo (con 3-4 chats paralelos = 3 días reales)

**Resultado:** GPAI compliance Art. 51-55 completo ✅

---

### **SPRINT 2 (Semana 2) - GDPR Automation 🔴**

**Objetivo:** Automatizar GDPR Art. 15-22 completamente

**PROMPTS_08 - Java Entities GDPR:**

- [ ] **PROMPT 14** - GDPRDataProcessing (1.5 días)
  - Entity registro actividades tratamiento (Art. 30)
  - Tabla: GDPDATAPROCESSING
  - **Esfuerzo:** 1.5 días

- [ ] **PROMPT 15** - GDPRLegalBasis (1 día)
  - Entity legal basis tracking (Art. 6)
  - Tabla: GDPLEGALBASIS

- [ ] **PROMPT 17** - GDPRDataBreaches (1.5 días)
  - Entity data breaches (Art. 33-34)
  - Workflow 72h notification
  - Tabla: GDPDATABREACHES

- [ ] **PROMPT 18** - GDPRConsentRecords (1 día)
  - Entity consent tracking (Art. 7)
  - Tabla: GDPCONSENTRECORDS

- [ ] **PROMPT 19** - GDPRDataTransfers (1.5 días)
  - Entity international transfers (Art. 44-46)
  - SCCs tracking
  - Tabla: GDPDATATRANSFERS

**PROMPTS_09 - BPMN GDPR Workflows:**

- [ ] **PROMPT 9** - data_subject_rights_fulfillment (2 días)
  - Workflow automation derechos GDPR
  - 30 días deadline automation
  - **Prioridad:** 🔴 CRÍTICA

- [ ] **PROMPT 11** - data_breach_notification_workflow (1 día)
  - Workflow 72h breach notification
  - Authority notification automation

- [ ] **PROMPT 12** - international_transfer_approval (1 día)
  - Workflow approval transfers fuera EEE
  - SCCs validation

**Total Sprint 2:** 8 prompts, 10-11 días esfuerzo (con 4-5 chats paralelos = 3 días reales)

**Resultado:** GDPR automation completa + Respuesta experto gobierno datos sólida ✅

---

### **SPRINT 3 (Semana 3) - Documentación Técnica 🔴**

**Objetivo:** Anexo IV auto-generation + Instructions for Use

**PROMPTS_02 - Python Microservicios Nuevos:**

- [ ] **PROMPT 1** - leka-server-documents (3-4 días)
  - **Endpoints:**
    - `/api/docs/generate-technical-doc` (Anexo IV)
    - `/api/docs/generate-instructions-for-use` (Art. 13)
    - `/api/docs/generate-eu-declaration` (Art. 47, Anexo V)
  - **Prioridad:** 🔴 CRÍTICA
  - **Esfuerzo:** 3-4 días (1 chat)
  - **Valor:** Responde experto pregunta "Audit pack Anexo IV"

**PROMPTS_05 - Java Entities Compliance:**

- [ ] **PROMPT B.4** - TechnicalDocumentation entity (1.5 días)
  - Tabla: TECDOCUMENTACION
  - Storage docs técnicos
  - **Esfuerzo:** 1.5 días

- [ ] **PROMPT B.3** - ConformityDeclaration entity (1 día)
  - Tabla: CONDECLARACIONES
  - EU Declaration Art. 47

**Total Sprint 3:** 3 prompts, 5-6 días esfuerzo (con 2 chats paralelos = 3 días reales)

**Resultado:** Anexo IV auto-generation + Instructions for Use ✅

---

### **SPRINT 4 (Semana 4) - ISO 42001 Foundation 🔴**

**Objetivo:** ISO 42001 base (necesario certificación Q2 2025)

**PROMPTS_07 - Multi-Framework (Grupo A):**

- [ ] **A.1.1** - AI Policy Framework document (1 día)
  - Documento formal política IA
  - Aprobación top management
  - **Prioridad:** 🔴 CRÍTICA ISO 42001

- [ ] **A.1.5** - AI System Inventory (2 días)
  - Entity: AIMSYSTEMINVENTORY
  - Registry completo sistemas IA
  - **Prioridad:** 🔴 CRÍTICA

- [ ] **A.1.7** - Internal Audit Program (2 días)
  - Entity: AIMINTERNALAUDITS
  - **Prioridad:** 🔴 CRÍTICA

**PROMPTS_08 - Java Entities ISO 42001:**

- [ ] **PROMPT 1** - AIMPolicies (1.5 días)
  - Tabla: AIMPOLICIES
  - Prefijo: AIP

- [ ] **PROMPT 2** - AIMObjectives (1.5 días)
  - Tabla: AIMOBJECTIVES
  - Prefijo: AIO

- [ ] **PROMPT 5** - AIMSystemInventory (1.5 días)
- [ ] **PROMPT 7** - AIMInternalAudits (1.5 días)

**PROMPTS_09 - BPMN ISO 42001:**

- [ ] **PROMPT 1** - ai_policy_approval_workflow (1 día)
- [ ] **PROMPT 4** - internal_audit_workflow (1.5 días)

**Total Sprint 4:** 9 prompts, 12-13 días esfuerzo (con 5-6 chats paralelos = 3 días reales)

**Resultado:** ISO 42001 base para certificación ✅

---

## 📅 FASE 2: VALOR COMERCIAL (Semanas 5-8)

### **SPRINT 5 (Semana 5) - Workflows Automáticos 🟡**

**Objetivo:** Automatizar procesos compliance más demandados

**PROMPTS_04 - BPMN Workflows Básicos:**

- [ ] **PROMPT 4** - conformity_assessment_process (2 días)
  - Art. 43 conformity
  - Anexo VII procedures
  - **Prioridad:** 🟡 ALTA

- [ ] **PROMPT 5** - post_market_monitoring_workflow (2 días)
  - Art. 72 monitoring continuo

- [ ] **PROMPT 6** - serious_incident_reporting (1.5 días)
  - Art. 73 incidents (15 días deadline)
  - **Prioridad:** 🔴 CRÍTICA

- [ ] **PROMPT 8** - model_update_approval (1 día)
  - Workflow cambios modelo

- [ ] **PROMPT 10** - data_quality_validation (1.5 días)
  - Art. 10 data governance

**PROMPTS_05 - Java Entities Compliance:**

- [ ] **PROMPT B.5** - PostMarketMonitoring (1.5 días)
  - Tabla: PMMPOSTMARKETMONITORING
  - Art. 72 + Anexo IX

- [ ] **PROMPT B.6** - SeriousIncident (1.5 días)
  - Tabla: SINSERIOUSINIDENTS
  - Art. 73

**Total Sprint 5:** 7 prompts, 10-11 días esfuerzo (con 4 chats paralelos = 3 días reales)

**Resultado:** Workflows críticos automatizados ✅

---

### **SPRINT 6 (Semana 6) - Python Adversarial & Security 🟡**

**Objetivo:** Art. 15 cybersecurity completo

**PROMPTS_02 - Python Nuevos:**

- [ ] **PROMPT 4** - leka-adversarial-robustness (3 días)
  - Adversarial testing Art. 15.5
  - Attack simulation
  - **Prioridad:** 🟡 ALTA
  - **Esfuerzo:** 3 días (1 chat)

**PROMPTS_10 - Python Extensiones:**

- [ ] **PROMPT 9** - leka-adversarial-testing extensiones (2 días)
  - Integración con evaluaciones existentes

**PROMPTS_08 - Java Entities Security:**

- [ ] **PROMPT 20** - ISECSecurityIncidents (1.5 días)
  - Tabla: ISECSECURITYINCIDENTS
  - ISO 27001 incidents

- [ ] **PROMPT 21** - ISECVulnerabilities (1.5 días)
  - Tabla: ISECVULNERABILITIES
  - Vulnerability tracking

**Total Sprint 6:** 4 prompts, 8 días esfuerzo (con 3 chats paralelos = 3 días reales)

**Resultado:** Cybersecurity Art. 15 completo ✅

---

### **SPRINT 7 (Semana 7) - Explainability & Transparency 🟡**

**Objetivo:** Art. 13, Art. 22 GDPR explainability

**PROMPTS_02 - Python Nuevos:**

- [ ] **PROMPT 6** - leka-explainability (3 días)
  - SHAP/LIME integration
  - Explanation generation Art. 22
  - Human-friendly explanations
  - **Esfuerzo:** 3 días

**PROMPTS_01 - Python Extensiones:**

- [ ] Governance features leka-llm-evaluation (2 días)
  - 5 endpoints governance adicionales
  - Instruction following, consistency, etc.

**Total Sprint 7:** 2 prompts, 5 días esfuerzo (con 2 chats paralelos = 3 días reales)

**Resultado:** Explainability completa ✅

---

### **SPRINT 8 (Semana 8) - Data Lineage & Quality 🟡**

**Objetivo:** Art. 10 data governance completo

**PROMPTS_02 - Python Nuevos:**

- [ ] **PROMPT 5** - leka-data-lineage (3 días)
  - Data lineage tracking
  - Dataset provenance
  - **Esfuerzo:** 3 días

**PROMPTS_10 - Python Extensiones:**

- [ ] **PROMPT 8** - leka-data-lineage-tracker (2 días)
  - Extensiones lineage

**Total Sprint 8:** 2 prompts, 5 días esfuerzo (con 2 chats paralelos = 3 días reales)

**Resultado:** Data governance Art. 10 completo ✅

---

## 📅 FASE 3: COMPLETITUD (Semanas 9-16)

### **SPRINT 9-10 (Semanas 9-10) - ISO 38507 Governance**

**PROMPTS_07 Grupo B (10 prompts) + PROMPTS_08 (4 entities):**

- [ ] B.1.1 - AI Strategy Alignment (2 días)
- [ ] B.1.3 - Board-Level Reporting (2 días)
- [ ] B.1.5 - RACI Matrix (1.5 días)
- [ ] B.1.4 - Evaluate-Direct-Monitor Dashboard (2 días)
- [ ] Otros 6 prompts ISO 38507

**Esfuerzo:** 14 prompts, ~18 días (con 6 chats paralelos = 6 días reales)

---

### **SPRINT 11-12 (Semanas 11-12) - ISO 42001 Completitud**

**PROMPTS_07 Grupo A restantes (6 prompts) + PROMPTS_08 (5 entities):**

- [ ] A.1.2 - AI Objectives (ya listado)
- [ ] A.1.3 - Competence Management
- [ ] A.1.4 - Documented Information
- [ ] A.1.6 - Change Management
- [ ] A.1.8 - Continual Improvement
- [ ] PROMPTS_08: Entities 3, 4, 6, 8, 9

**Esfuerzo:** 11 prompts, ~14 días (con 5 chats paralelos = 6 días reales)

---

### **SPRINT 13-14 (Semanas 13-14) - BPMN Automation Completo**

**PROMPTS_09 workflows restantes (9 workflows):**

- [ ] PROMPT 2 - ai_objectives_planning_workflow
- [ ] PROMPT 3 - competence_assessment_workflow
- [ ] PROMPT 5 - management_review_workflow
- [ ] PROMPT 6 - corrective_action_workflow
- [ ] PROMPT 7 - strategy_alignment_workflow
- [ ] PROMPT 8 - board_reporting_workflow
- [ ] Otros 3 workflows

**Esfuerzo:** 9 prompts, ~12 días (con 4 chats paralelos = 6 días reales)

---

### **SPRINT 15-16 (Semanas 15-16) - Python Enhancements**

**PROMPTS_10 microservicios restantes (13 extensiones):**

- [ ] PROMPT 1-7: Extensiones microservicios existentes
- [ ] PROMPT 8-13: Nuevos microservicios (GDPR, ISO reporter, dashboard)

**Esfuerzo:** 13 prompts, ~20 días (con 6 chats paralelos = 6 días reales)

---

## 📅 FASE 4: OPCIONAL/POLISH (Semanas 17-20)

### **SPRINT 17-18 - ICO UK (Solo si clientes UK)**

**PROMPTS_07 Grupo F (9 prompts):**
- [ ] ICO UK specific features
- **Esfuerzo:** 9 prompts, ~12 días (con 4 chats = 6 días reales)
- **Prioridad:** 🟢 BAJA (solo si ventas UK)

### **SPRINT 19-20 - Polish & Testing**
- Testing E2E completo
- Performance optimization
- UI/UX improvements
- Documentación usuario final

---

## 🎯 CALENDARIO EJECUTIVO

```
NOVIEMBRE 2025:
├─ Semana 1 (4-8 Nov):   Sprint 1 - GPAI ✅
├─ Semana 2 (11-15 Nov): Sprint 2 - GDPR Automation ✅
├─ Semana 3 (18-22 Nov): Sprint 3 - Documentación Técnica ✅
└─ Semana 4 (25-29 Nov): Sprint 4 - ISO 42001 Base ✅

DICIEMBRE 2025:
├─ Semana 1-2: Sprint 5 - Workflows ✅
├─ Semana 3-4: Sprint 6 - Security ✅

ENERO 2026:
├─ Semana 1-2: Sprint 7 - Explainability ✅
├─ Semana 3-4: Sprint 8 - Data Lineage ✅

FEBRERO 2026:
├─ Semana 1-2: Sprint 9-10 - ISO 38507 ✅
├─ Semana 3-4: Sprint 11-12 - ISO 42001 Completitud ✅

MARZO 2026:
├─ Semana 1-2: Sprint 13-14 - BPMN Completo ✅
├─ Semana 3-4: Sprint 15-16 - Python Enhancements ✅

ABRIL 2026:
└─ Semana 1-4: Sprint 17-20 - ICO UK + Polish (opcional)
```

---

## 🔥 TOP 20 PROMPTS CRÍTICOS (Orden Ejecución)

**Ejecutar en este orden para máximo impacto:**

| # | Prompt | Documento | Días | Prioridad | Valor |
|---|--------|-----------|------|-----------|-------|
| 1 | ModelAdaptationBusinessService | PROMPTS_06 #2 | 2-3 | 🔴 | GPAI compliance |
| 2 | leka-server-documents | PROMPTS_02 #1 | 3-4 | 🔴 | Anexo IV generation |
| 3 | GDPRDataProcessing | PROMPTS_08 #14 | 1.5 | 🔴 | GDPR Art. 30 |
| 4 | data_subject_rights_fulfillment | PROMPTS_09 #9 | 2 | 🔴 | GDPR automation |
| 5 | AIMPolicies | PROMPTS_08 #1 | 1.5 | 🔴 | ISO 42001 Clause 5.2 |
| 6 | AIMSystemInventory | PROMPTS_08 #5 | 1.5 | 🔴 | ISO 42001 Clause 8.1 |
| 7 | serious_incident_reporting | PROMPTS_04 #6 | 1.5 | 🔴 | Art. 73 (15 días) |
| 8 | GDPRDataBreaches | PROMPTS_08 #17 | 1.5 | 🔴 | GDPR 72h notification |
| 9 | internal_audit_workflow | PROMPTS_09 #4 | 1.5 | 🔴 | ISO 42001 audits |
| 10 | dpia_assessment_process | PROMPTS_09 #10 | 2 | 🟡 | GDPR Art. 35 formal |
| 11 | leka-adversarial-robustness | PROMPTS_02 #4 | 3 | 🟡 | Art. 15.5 security |
| 12 | conformity_assessment_process | PROMPTS_04 #4 | 2 | 🟡 | Art. 43 conformity |
| 13 | GDPRDataTransfers | PROMPTS_08 #19 | 1.5 | 🟡 | Transfers tracking |
| 14 | PostMarketMonitoring | PROMPTS_05 #B.5 | 1.5 | 🟡 | Art. 72 monitoring |
| 15 | leka-explainability | PROMPTS_02 #6 | 3 | 🟡 | Art. 22 GDPR |
| 16 | GOVBoardReports | PROMPTS_08 #11 | 1.5 | 🟡 | ISO 38507 reporting |
| 17 | leka-data-lineage | PROMPTS_02 #5 | 3 | 🟡 | Data provenance |
| 18 | AIMInternalAudits | PROMPTS_08 #7 | 1.5 | 🟡 | ISO 42001 audits |
| 19 | data_breach_notification | PROMPTS_09 #11 | 1 | 🟡 | GDPR 72h workflow |
| 20 | TechnicalDocumentation | PROMPTS_05 #B.4 | 1.5 | 🟡 | Art. 11 storage |

**Total Top 20:** ~38 días esfuerzo → **8-10 días reales** (chats paralelos)

---

## 📋 CÓMO USAR ESTE DOCUMENTO

### **Paso 1: Selecciona Sprint**
Decide qué sprint ejecutar (recomendación: orden propuesto)

### **Paso 2: Abre documento PROMPTS correspondiente**
Ejemplo: Sprint 1 → Abre `PROMPTS_06_MLOPS_ADAPTERS_FINETUNING.md`

### **Paso 3: Copia prompt específico**
Ejemplo: PROMPT 2 - ModelAdaptationBusinessService (líneas X-Y)

### **Paso 4: Ejecuta en chat**
Pega prompt en chat especializado (Java/Python/BPMN)

### **Paso 5: Marca completado**
Vuelve a este documento y marca [x] prompt ejecutado

### **Paso 6: Actualiza README**
Actualiza % en `README.md` compliance

---

## 🔄 PARALELIZACIÓN CHATS

**Máxima eficiencia:** 4-6 chats paralelos

**Distribución típica sprint:**

**Sprint ejemplo (Semana 2 - GDPR):**
```
Chat 1 (Java Entities):    PROMPTS_08 #14, #15, #19
Chat 2 (Java Entities):    PROMPTS_08 #17, #18
Chat 3 (BPMN):             PROMPTS_09 #9
Chat 4 (BPMN):             PROMPTS_09 #11, #12
Chat 5 (Testing):          Testing entities creadas
Chat 6 (Docs):             Documentación cambios
```

**Resultado:** 8 prompts en 3 días reales (vs 10 días secuencial)

---

## 📊 MÉTRICAS TRACKING

**Actualiza semanalmente:**

| Métrica | Actual | Target Final |
|---------|--------|--------------|
| **Prompts totales** | 150 | 150 |
| **Implementados** | 28 (19%) | 150 (100%) |
| **Java Entities** | 8 | 42 |
| **Python Microservicios** | 8 | 21 |
| **BPMN Workflows** | 3 | 27 |
| **Semanas estimadas** | - | 16-20 semanas |
| **Con paralelización** | - | 8-10 semanas |

---

## 🎯 QUICK WINS (Máximo impacto, mínimo esfuerzo)

**Si tienes 1 semana:**
1. PROMPTS_06 GPAI (5 prompts) → GPAI compliance ✅
2. GDPRDataSubjectRequests workflow (PROMPTS_09 #9) → GDPR rights automation ✅

**Si tienes 2 semanas:**
+ leka-server-documents (PROMPTS_02 #1) → Anexo IV generation ✅
+ GDPRDataProcessing (PROMPTS_08 #14) → GDPR Art. 30 ✅

**Si tienes 1 mes (4 semanas):**
+ Ejecuta Sprints 1-4 completos
+ Resultado: GPAI + GDPR + Docs + ISO 42001 base ✅
+ **Compliance sube de 19% → 45%**

---

## 🚨 DEADLINES LEGALES vs IMPLEMENTACIÓN

| Deadline Legal | Requisito | Prompts Necesarios | Estado |
|----------------|-----------|-------------------|--------|
| **2 Feb 2025** | Art. 5 Prohibidas | ✅ PROMPTS_03 cubre | ✅ Listo |
| **2 Ago 2025** | Art. 51-55 GPAI | PROMPTS_06 (6) | ⏳ Sprint 1 |
| **2 Ago 2026** | Art. 6-29 Alto Riesgo | PROMPTS_03 ✅ + PROMPTS_04/05 | ⚠️ 70% listo |
| **2 Ago 2027** | Productos Anexo I CE | PROMPTS_05 compliance | ⏳ Sprints 5-8 |

**URGENTE:** Sprint 1 (GPAI) debe completarse **ANTES 2 Ago 2025** = 9 meses

---

## 📞 RECOMENDACIÓN EJECUCIÓN

### **Plan Agresivo (4 meses):**
- Sprints 1-16 en 16 semanas (4 meses)
- 4-6 chats paralelos constantes
- **Resultado:** 100% compliance Febrero 2026

### **Plan Moderado (6 meses):**
- Sprints 1-16 en 24 semanas (6 meses)
- 3-4 chats paralelos
- **Resultado:** 100% compliance Abril 2026

### **Plan Conservador (8 meses):**
- Sprints 1-16 en 32 semanas (8 meses)
- 2-3 chats paralelos
- **Resultado:** 100% compliance Junio 2026

---

## ✅ SIGUIENTE ACCIÓN INMEDIATA

**AHORA MISMO:**

1. ✅ Abre `PROMPTS_06_MLOPS_ADAPTERS_FINETUNING.md`
2. ✅ Busca **PROMPT 2** (ModelAdaptationBusinessService)
3. ✅ Copia prompt completo
4. ✅ Abre nuevo chat Java
5. ✅ Pega prompt y ejecuta
6. ✅ Cuando complete → Marca [x] en este documento
7. ✅ Repite con PROMPT 3, 4, 5, 6

**Tiempo:** Sprint 1 completo en 3 días (chats paralelos)

---

**¿Empezamos con Sprint 1 (GPAI) ahora?** 🚀
