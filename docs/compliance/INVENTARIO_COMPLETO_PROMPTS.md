# 📋 INVENTARIO COMPLETO PROMPTS - CODEFLOWX
## Vista Consolidada Todos los Documentos PROMPTS

**Fecha:** 5 Noviembre 2025  
**Total documentos:** 11  
**Total prompts:** 165  
**Implementados:** 28 (17%)  
**Pendientes:** 137 (83%)

---

## 📊 TABLA RESUMEN

| # | Documento | Objetivo | Total | ✅ | ⏳ | % | Prioridad |
|---|-----------|----------|-------|----|----|---|-----------|
| **01** | PROMPTS_01_PYTHON_MICROSERVICIOS_EXISTENTES | Extensiones micros Python existentes | 10 | 6 | 4 | 60% | 🟡 ALTA |
| **02** | PROMPTS_02_PYTHON_MICROSERVICIOS_NUEVOS | Nuevos microservicios Python (docs, explainability, etc.) | 8 | 1 | 7 | 13% | 🔴 CRÍTICA |
| **03** | PROMPTS_03_JAVA_BACKEND_EXISTENTE | Extensiones backend Java existente (COMPLETADO) | 7 | 7 | 0 | **100%** | ✅ DONE |
| **04** | PROMPTS_04_BPMN_WORKFLOWS_BASICOS | Workflows BPMN básicos compliance | 15 | 3 | 12 | 20% | 🔴 CRÍTICA |
| **05** | PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS | Nuevas entidades Java compliance | 12 | 0 | 12 | 0% | 🟡 ALTA |
| **06** | PROMPTS_06_MLOPS_ADAPTERS_FINETUNING | GPAI compliance (Art. 51-55) adapters/fine-tuning | 6 | 0 | 6 | 0% | 🔴 CRÍTICA |
| **07** | PROMPTS_07_MULTI_FRAMEWORK_100_PERCENT | Multi-framework master (ISO, OECD, GDPR, ICO) | 47 | 0 | 47 | 0% | 🟡 ALTA |
| **08** | PROMPTS_08_JAVA_MULTI_FRAMEWORK | Entities Java multi-framework (22 entidades) | 22 | 1 | 21 | 5% | 🟡 ALTA |
| **09** | PROMPTS_09_BPMN_MULTI_FRAMEWORK | Workflows BPMN multi-framework (12 workflows) | 12 | 0 | 12 | 0% | 🟡 ALTA |
| **10** | PROMPTS_10_PYTHON_MULTI_FRAMEWORK_CONSOLIDADO | Python microservicios consolidado | 13 | 0 | 13 | 0% | 🟡 ALTA |
| **11** | PROMPTS_11_INTEGRACION_QDRANT_MINIO_OPENSEARCH | Infraestructura enterprise (Qdrant, MinIO, OpenSearch) | 15 | 0 | 15 | 0% | 🟡 ALTA |
| | **TOTAL** | | **165** | **28** | **137** | **17%** | |

---

## 🎯 DISTRIBUCIÓN POR TECNOLOGÍA

### **JAVA (Backend Spring Boot):**

| Documento | Tipo | Total | ✅ | ⏳ |
|-----------|------|-------|----|----|
| PROMPTS_03 | Extensiones existente | 7 | 7 | 0 |
| PROMPTS_05 | Nuevas entidades | 12 | 0 | 12 |
| PROMPTS_08 | Entities multi-framework | 22 | 1 | 21 |
| **Total Java** | | **41** | **8** | **33** |

**% Java:** 20% (8/41)

---

### **PYTHON (Microservicios FastAPI):**

| Documento | Tipo | Total | ✅ | ⏳ |
|-----------|------|-------|----|----|
| PROMPTS_01 | Extensiones existentes | 10 | 6 | 4 |
| PROMPTS_02 | Nuevos microservicios | 8 | 1 | 7 |
| PROMPTS_10 | Consolidado multi-framework | 13 | 0 | 13 |
| PROMPTS_11 (Grupo A) | RAG Qdrant | 5 | 0 | 5 |
| PROMPTS_11 (Grupo C) | Analytics OpenSearch | 1 | 0 | 1 |
| **Total Python** | | **37** | **7** | **30** |

**% Python:** 19% (7/37)

---

### **BPMN (Workflows Camunda/Motor propio):**

| Documento | Tipo | Total | ✅ | ⏳ |
|-----------|------|-------|----|----|
| PROMPTS_04 | Workflows básicos | 15 | 3 | 12 |
| PROMPTS_09 | Workflows multi-framework | 12 | 0 | 12 |
| **Total BPMN** | | **27** | **3** | **24** |

**% BPMN:** 11% (3/27)

---

### **INFRAESTRUCTURA (Docker, Config, Scripts):**

| Documento | Tipo | Total | ✅ | ⏳ |
|-----------|------|-------|----|----|
| PROMPTS_11 (Grupos A,B,C) | Qdrant, MinIO, OpenSearch | 15 | 0 | 15 |
| **Total Infra** | | **15** | **0** | **15** |

**% Infra:** 0% (0/15)

---

### **CROSS-FUNCTIONAL (Políticas, Docs, Audits):**

| Documento | Tipo | Total | ✅ | ⏳ |
|-----------|------|-------|----|----|
| PROMPTS_07 | Multi-framework master | 47 | 0 | 47 |
| **Total Cross** | | **47** | **0** | **47** |

**% Cross:** 0% (0/47)

---

## 🔥 TOP 30 PROMPTS CRÍTICOS (Por Impacto)

### **COMPLIANCE LEGAL (Deadlines AI Act):**

| # | Prompt | Documento | Deadline | Prioridad |
|---|--------|-----------|----------|-----------|
| 1 | ModelAdaptationBusinessService | PROMPTS_06 #2 | 2 Ago 2025 | 🔴 GPAI |
| 2 | adapter-creation-approval BPMN | PROMPTS_06 #3 | 2 Ago 2025 | 🔴 GPAI |
| 3 | finetuning-approval-v1 BPMN | PROMPTS_06 #4 | 2 Ago 2025 | 🔴 GPAI |
| 4 | leka-server-documents | PROMPTS_02 #1 | 2 Ago 2026 | 🔴 Art. 11 |
| 5 | serious_incident_reporting BPMN | PROMPTS_04 #6 | 2 Ago 2026 | 🔴 Art. 73 |
| 6 | conformity_assessment_process BPMN | PROMPTS_04 #4 | 2 Ago 2026 | 🔴 Art. 43 |

---

### **INFRAESTRUCTURA RAG ENTERPRISE:**

| # | Prompt | Documento | Valor | Prioridad |
|---|--------|-----------|-------|-----------|
| 7 | Setup Qdrant + Collections | PROMPTS_11 #1 | RAG potente | 🟡 ALTA |
| 8 | Microservicio RAG Python | PROMPTS_11 #3 | RAG completo | 🔴 CRÍTICA |
| 9 | Pipeline RAG MinIO → Qdrant | PROMPTS_11 #9 | Pipeline e2e | 🔴 CRÍTICA |
| 10 | Reranking avanzado | PROMPTS_11 #5 | +30-50% accuracy | 🟡 ALTA |

---

### **COMPLIANCE GDPR:**

| # | Prompt | Documento | Art. GDPR | Prioridad |
|---|--------|-----------|-----------|-----------|
| 11 | data_subject_rights_fulfillment BPMN | PROMPTS_09 #9 | Art. 15-22 | 🔴 CRÍTICA |
| 12 | GDPRDataProcessing entity | PROMPTS_08 #14 | Art. 30 | 🔴 CRÍTICA |
| 13 | GDPRDataBreaches entity | PROMPTS_08 #17 | Art. 33-34 | 🔴 CRÍTICA |
| 14 | data_breach_notification BPMN | PROMPTS_09 #11 | 72h deadline | 🔴 CRÍTICA |

---

### **LOGS INMUTABLES (Art. 19):**

| # | Prompt | Documento | Art. AI Act | Prioridad |
|---|--------|-----------|-------------|-----------|
| 15 | Setup OpenSearch + Índices | PROMPTS_11 #11 | Art. 19 | 🔴 CRÍTICA |
| 16 | Cliente Java OpenSearch LogService | PROMPTS_11 #12 | Art. 19 | 🔴 CRÍTICA |
| 17 | ILM OpenSearch retention | PROMPTS_11 #14 | Art. 19 + GDPR | 🔴 CRÍTICA |

---

### **ISO 42001 BASE:**

| # | Prompt | Documento | ISO Clause | Prioridad |
|---|--------|-----------|------------|-----------|
| 18 | AIMPolicies entity | PROMPTS_08 #1 | 5.2 | 🔴 CRÍTICA |
| 19 | AIMObjectives entity | PROMPTS_08 #2 | 6.2 | 🔴 CRÍTICA |
| 20 | AIMSystemInventory entity | PROMPTS_08 #5 | 8.1 | 🔴 CRÍTICA |
| 21 | ai_policy_approval_workflow BPMN | PROMPTS_09 #1 | 5.2 | 🟡 ALTA |
| 22 | internal_audit_workflow BPMN | PROMPTS_09 #4 | 9.2 | 🔴 CRÍTICA |

---

### **DOCS TÉCNICOS (Art. 11):**

| # | Prompt | Documento | Valor | Prioridad |
|---|--------|-----------|-------|-----------|
| 23 | Docs Técnicos Anexo IV MinIO | PROMPTS_11 #8 | Art. 11 | 🔴 CRÍTICA |
| 24 | TechnicalDocumentation entity | PROMPTS_05 #B.4 | Art. 11 | 🟡 ALTA |

---

### **EVALUACIÓN AVANZADA:**

| # | Prompt | Documento | Valor | Prioridad |
|---|--------|-----------|-------|-----------|
| 25 | leka-adversarial-robustness | PROMPTS_02 #4 | Art. 15.5 | 🟡 ALTA |
| 26 | leka-explainability | PROMPTS_02 #6 | Art. 22 GDPR | 🟡 ALTA |
| 27 | leka-data-lineage | PROMPTS_02 #5 | Art. 10 | 🟡 ALTA |

---

### **POST-MARKET MONITORING:**

| # | Prompt | Documento | Art. AI Act | Prioridad |
|---|--------|-----------|-------------|-----------|
| 28 | PostMarketMonitoring entity | PROMPTS_05 #B.5 | Art. 72 | 🟡 ALTA |
| 29 | post_market_monitoring_workflow BPMN | PROMPTS_04 #5 | Art. 72 | 🟡 ALTA |
| 30 | SeriousIncident entity | PROMPTS_05 #B.6 | Art. 73 | 🔴 CRÍTICA |

---

## 📅 ROADMAP EJECUCIÓN RECOMENDADO

### **Q4 2025 (Nov-Dic):**

**Mes 1 (Noviembre):**
- ✅ Sprint 1: GPAI compliance (PROMPTS_06 - 6 prompts)
- ✅ Sprint 2: GDPR automation (PROMPTS_08/09 - 8 prompts)

**Mes 2 (Diciembre):**
- ✅ Sprint 3: Docs técnicas (PROMPTS_02 - 3 prompts)
- ✅ Sprint 4: ISO 42001 base (PROMPTS_07/08/09 - 9 prompts)
- ✅ Sprint 4-bis: Infraestructura RAG (PROMPTS_11 - 6 prompts)

**Total Q4:** 32 prompts → 28+32 = 60 prompts (36%)

---

### **Q1 2026 (Ene-Mar):**

**Mes 3 (Enero):**
- ✅ Sprint 5: Workflows automáticos (PROMPTS_04/05 - 7 prompts)
- ✅ Sprint 6: Cybersecurity (PROMPTS_02/10 - 4 prompts)

**Mes 4 (Febrero):**
- ✅ Sprint 7: Explainability (PROMPTS_02/01 - 2 prompts)
- ✅ Sprint 8: Data lineage (PROMPTS_02/10 - 2 prompts)
- ✅ Sprint 9: RAG completo (PROMPTS_11 - 9 prompts restantes)

**Mes 5 (Marzo):**
- ✅ Sprint 10: ISO 38507 (PROMPTS_07/08 - 14 prompts)

**Total Q1:** 38 prompts → 60+38 = 98 prompts (59%)

---

### **Q2 2026 (Abr-Jun):**

**Mes 6 (Abril):**
- ✅ Sprint 11-12: ISO 42001 completitud (PROMPTS_07/08 - 11 prompts)

**Mes 7 (Mayo):**
- ✅ Sprint 13-14: BPMN completo (PROMPTS_09 - 9 prompts)

**Mes 8 (Junio):**
- ✅ Sprint 15-16: Python enhancements (PROMPTS_10 - 13 prompts)

**Total Q2:** 33 prompts → 98+33 = 131 prompts (79%)

---

### **Q3 2026 (Jul-Sep) - OPCIONAL:**

**Mes 9-10:**
- ✅ Sprint 17-18: OECD + ICO UK (PROMPTS_07 restantes - 20 prompts)

**Mes 11:**
- ✅ Sprint 19: Polish + testing

**Total Q3:** 20 prompts → 131+20 = 151 prompts (91%)

---

### **Q4 2026 (Oct-Dic) - COMPLETITUD:**

**Mes 12:**
- ✅ Sprint 20: Prompts finales + testing E2E

**Total Q4:** 14 prompts → 165 prompts (100%)

---

## 🔥 DEADLINES LEGALES vs PROMPTS

| Deadline Legal | Requisito AI Act | Prompts Necesarios | Estado | Riesgo |
|----------------|------------------|-------------------|--------|--------|
| **2 Feb 2025** | Art. 5 Prohibidas | PROMPTS_03 | ✅ Listo | ✅ OK |
| **2 Ago 2025** | Art. 51-55 GPAI | PROMPTS_06 (6) | ⏳ Sprint 1 | ⚠️ 9 meses |
| **2 Ago 2026** | Art. 6-29 Alto Riesgo | PROMPTS_03 ✅ + PROMPTS_04/05 (27) | ⚠️ 70% listo | ⚠️ 21 meses |
| **2 Ago 2027** | Productos Anexo I CE | PROMPTS_05 compliance | ⏳ Q2 2026 | ✅ 33 meses |

**URGENTE:** PROMPTS_06 (GPAI) debe completarse **ANTES 2 Ago 2025** = **9 meses disponibles**

---

## 📊 DISTRIBUCIÓN POR PRIORIDAD

### **🔴 CRÍTICOS (31 prompts):**

- PROMPTS_02: leka-server-documents (Anexo IV)
- PROMPTS_04: serious_incident_reporting, conformity_assessment
- PROMPTS_06: GPAI compliance completo (6 prompts)
- PROMPTS_08: GDPRDataProcessing, GDPRDataBreaches, AIMPolicies, AIMSystemInventory (4)
- PROMPTS_09: data_subject_rights, internal_audit, data_breach_notification (3)
- PROMPTS_11: RAG Python, Pipeline RAG, OpenSearch setup, LogService Art. 19, ILM (6)

**Timeline críticos:** Completar Q4 2025 - Q1 2026 (6 meses)

---

### **🟡 ALTOS (86 prompts):**

- PROMPTS_01: Extensiones microservicios (4 restantes)
- PROMPTS_05: Entities compliance (12)
- PROMPTS_07: Multi-framework (47)
- PROMPTS_08: Entities multi-framework (17 restantes)
- PROMPTS_09: Workflows multi-framework (9 restantes)
- PROMPTS_11: Setup Qdrant/MinIO, clientes Java (7)

**Timeline altos:** Q1 2026 - Q2 2026 (6 meses)

---

### **🟢 MEDIOS (20 prompts):**

- PROMPTS_02: Microservicios nice-to-have
- PROMPTS_10: Python enhancements
- PROMPTS_11: Dashboards OpenSearch, Lifecycle MinIO

**Timeline medios:** Q3 2026 - Q4 2026 (6 meses)

---

## 📂 ESTRUCTURA CARPETA PROMPTS

```
docs/compliance/
├── PROMPTS_01_PYTHON_MICROSERVICIOS_EXISTENTES.md        (10 prompts, 60%)
├── PROMPTS_02_PYTHON_MICROSERVICIOS_NUEVOS.md            (8 prompts, 13%)
├── PROMPTS_03_JAVA_BACKEND_EXISTENTE.md                  (7 prompts, 100%) ✅
├── PROMPTS_04_BPMN_WORKFLOWS_BASICOS.md                  (15 prompts, 20%)
├── PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md         (12 prompts, 0%)
├── PROMPTS_06_MLOPS_ADAPTERS_FINETUNING.md               (6 prompts, 0%)
├── PROMPTS_07_MULTI_FRAMEWORK_100_PERCENT.md             (47 prompts, 0%)
├── PROMPTS_08_JAVA_MULTI_FRAMEWORK.md                    (22 prompts, 5%)
├── PROMPTS_09_BPMN_MULTI_FRAMEWORK.md                    (12 prompts, 0%)
├── PROMPTS_10_PYTHON_MULTI_FRAMEWORK_CONSOLIDADO.md      (13 prompts, 0%)
├── PROMPTS_11_INTEGRACION_QDRANT_MINIO_OPENSEARCH.md     (15 prompts, 0%) ⭐
│
├── TRACKING_PROMPTS_IMPLEMENTACION.md                    ← Tracking detallado
├── GUIA_EJECUCION_PROMPTS_PENDIENTES.md                  ← Roadmap sprints
└── INVENTARIO_COMPLETO_PROMPTS.md                        ← Este documento
```

---

## 🎯 MÉTRICAS EJECUCIÓN

### **Por Sprint:**

| Sprint | Semana | Prompts | Días Esfuerzo | Días Reales (paralelo) | Acumulado |
|--------|--------|---------|---------------|------------------------|-----------|
| Sprint 1 | Sem 1 | 6 | 8-9 | 3 | 34 (21%) |
| Sprint 2 | Sem 2 | 8 | 10-11 | 3 | 42 (25%) |
| Sprint 3 | Sem 3 | 3 | 5-6 | 3 | 45 (27%) |
| Sprint 4 | Sem 4 | 9 | 12-13 | 3 | 54 (33%) |
| Sprint 4-bis | Sem 4.5 | 6 | 6-7 | 2-3 | 60 (36%) |
| Sprint 5 | Sem 5 | 7 | 10-11 | 3 | 67 (41%) |
| Sprint 6 | Sem 6 | 4 | 8 | 3 | 71 (43%) |
| Sprint 7 | Sem 7 | 2 | 5 | 3 | 73 (44%) |
| Sprint 8 | Sem 8 | 2 | 5 | 3 | 75 (45%) |
| Sprint 9 | Sem 9 | 9 | 12-14 | 4 | 84 (51%) |
| Sprint 10-16 | Sem 10-20 | 81 | ~90 | ~30 | 165 (100%) |

**Timeline total:** 20 semanas (5 meses) con ejecución agresiva

---

## 📋 CÓMO USAR ESTE INVENTARIO

### **Paso 1: Selecciona qué ejecutar**

**Por deadline:**
- Deadline 2 Ago 2025 → PROMPTS_06 (GPAI)
- Deadline 2 Ago 2026 → PROMPTS_02 (docs), PROMPTS_04 (workflows), PROMPTS_05

**Por valor comercial:**
- RAG potente → PROMPTS_11 (Qdrant)
- GDPR automation → PROMPTS_08/09 (GDPR)
- ISO 42001 certificación → PROMPTS_07/08/09 (ISO)

**Por tecnología:**
- Backend Java → PROMPTS_05, PROMPTS_08
- Python FastAPI → PROMPTS_01, PROMPTS_02, PROMPTS_10, PROMPTS_11
- BPMN workflows → PROMPTS_04, PROMPTS_09

---

### **Paso 2: Abre documento PROMPTS correspondiente**

Ejemplo: Ejecutar Sprint 1 (GPAI)
```bash
# Abre:
docs/compliance/PROMPTS_06_MLOPS_ADAPTERS_FINETUNING.md

# Busca:
PROMPT 2 - ModelAdaptationBusinessService

# Copia prompt completo
```

---

### **Paso 3: Ejecuta en chat especializado**

**Chats especializados:**
- **Chat Java:** PROMPTS_05, PROMPTS_08
- **Chat Python:** PROMPTS_01, PROMPTS_02, PROMPTS_10, PROMPTS_11 (Python)
- **Chat BPMN:** PROMPTS_04, PROMPTS_09
- **Chat DevOps:** PROMPTS_11 (setup infraestructura)

---

### **Paso 4: Marca completado**

```bash
# En TRACKING_PROMPTS_IMPLEMENTACION.md:
- [x] PROMPT 2 - ModelAdaptationBusinessService  ← Marca con x

# Actualiza % en README.md
```

---

## 🚀 SIGUIENTE ACCIÓN INMEDIATA

**Para empezar AHORA:**

**Opción A: GPAI Compliance (deadline 2 Ago 2025)** 🔴
```bash
1. Abre: PROMPTS_06_MLOPS_ADAPTERS_FINETUNING.md
2. Ejecuta: PROMPT 2 (ModelAdaptationBusinessService)
3. Tiempo: 2-3 días
4. Resultado: GPAI tracking adapters/fine-tuning
```

**Opción B: Infraestructura RAG (valor comercial)** 🟡
```bash
1. Abre: PROMPTS_11_INTEGRACION_QDRANT_MINIO_OPENSEARCH.md
2. Ejecuta: PROMPT 1, 6, 11 (setup infra - paralelo)
3. Tiempo: 1-2 días
4. Resultado: Qdrant + MinIO + OpenSearch funcionando
```

**Opción C: GDPR Automation (valor comercial)** 🔴
```bash
1. Abre: PROMPTS_08_JAVA_MULTI_FRAMEWORK.md
2. Ejecuta: PROMPT 14 (GDPRDataProcessing)
3. Tiempo: 1.5 días
4. Resultado: GDPR Art. 30 automatizado
```

---

## 📊 ESTADO ACTUAL VISUAL

```
┌────────────────────────────────────────────────────────┐
│              INVENTARIO PROMPTS CODEFLOWX               │
├────────────────────────────────────────────────────────┤
│                                                         │
│  Total documentos:   11                                 │
│  Total prompts:      165                                │
│                                                         │
│  ✅ Implementados:   28  (17%)  ████░░░░░░░░░░░░░░░░  │
│  ⏳ Pendientes:      137 (83%)                          │
│                                                         │
│  🔴 Críticos:        31  (19%)  (GPAI, GDPR, Art. 19)  │
│  🟡 Altos:           86  (52%)  (ISO, workflows)        │
│  🟢 Medios:          20  (12%)  (enhancements)          │
│                                                         │
│  Timeline estimado:  5-8 meses (con ejecución agresiva) │
│                                                         │
└────────────────────────────────────────────────────────┘
```

---

## ✅ DOCUMENTOS TRACKING

**Sistema tracking completo:**

1. **`INVENTARIO_COMPLETO_PROMPTS.md`** ← Este documento (vista consolidada)
2. **`TRACKING_PROMPTS_IMPLEMENTACION.md`** (checklist detallado por prompt)
3. **`GUIA_EJECUCION_PROMPTS_PENDIENTES.md`** (roadmap sprints priorizados)
4. **`README.md`** (estado general + % implementación)

---

**Última actualización:** 5 Noviembre 2025  
**Próxima revisión:** Semanal (actualizar % completados)  
**Uso:** Vista ejecutiva rápida inventario completo
