# DISTRIBUCIÓN DE INCIDENCIAS PENDIENTES - AGENTES

**Fecha:** 25 de noviembre de 2025
**Total Incidencias Pendientes:** ~41-43
**Agentes Existentes:** 5
**Agentes Adicionales Creados:** 3
**Total Agentes:** 8

---

## 📊 RESUMEN DE DISTRIBUCIÓN

| Agente | Incidencias Asignadas | Prioridad | Esfuerzo Estimado | Tipo Principal |
|--------|----------------------|-----------|------------------|----------------|
| **Agente 1** | 5 | Críticas + Altas | ~12h | Validaciones |
| **Agente 2** | 5 | Altas + Medias | ~10h | Python/ML |
| **Agente 3** | 5 | Críticas + Altas | ~10h | Integraciones |
| **Agente 4** | 6 | Altas + Medias | ~12h | PMM + Dashboards |
| **Agente 5** | 4 | Medias + Bajas | ~8h | BPMN + Workflows |
| **Agente 6** | 5 | Medias + Bajas | ~10h | Frontend + UI |
| **Agente 7** | 5 | Medias + Bajas | ~8h | Optimización + DBA |
| **Agente 8** | 4 | Bajas | ~6h | Funcionalidades Opcionales |
| **TOTAL** | **39** | - | **~76h** | - |

---

## 👥 ASIGNACIÓN POR AGENTE

### **AGENTE 1 - Validaciones Críticas y Altas**

**Especialidad:** Backend Senior - Validaciones y Compliance
**Incidencias Asignadas:** 5
**Esfuerzo Estimado:** ~12 horas

#### **Incidencias:**

1. **INC-007: Validación cruzada FRIA vs métricas** 🔴 CRÍTICA
   - **Esfuerzo:** 2h
   - **Tipo:** Integración Python
   - **Prompt:** `/docs/compliance/gaps/prompts/java/INC-007_benchmarks_industria.md`
   - **Notas:** Requiere microservicio Python

2. **INC-005-DS: Validación integridad datasets** 🟡 ALTA
   - **Esfuerzo:** 2h
   - **Tipo:** Java + Python
   - **Notas:** Hash SHA-256

3. **INC-002: Validación confianza sugerencia IA** 🟡 MEDIA
   - **Esfuerzo:** 1h
   - **Tipo:** Backend
   - **Notas:** Aumentar umbral a 0.85

4. **INC-004: Validación calidad justificación** 🟡 MEDIA
   - **Esfuerzo:** 2h
   - **Tipo:** Backend + NLP
   - **Notas:** NLP para detectar genéricas

5. **INC-008: Validación fórmula cálculo riesgo** 🟡 MEDIA
   - **Esfuerzo:** 1h
   - **Tipo:** Backend
   - **Notas:** Documentar y testear

**Total:** 5 incidencias, ~8 horas

---

### **AGENTE 2 - Python/ML y RAG**

**Especialidad:** Backend Mid - Python Microservicios y ML
**Incidencias Asignadas:** 5
**Esfuerzo Estimado:** ~10 horas

#### **Incidencias:**

1. **INC-005-004: Métricas RAG estandarizadas** 🟡 ALTA
   - **Esfuerzo:** 3h
   - **Tipo:** Python Microservicio
   - **Notas:** RAGAS, ARES, benchmarking BEIR/MTEB

2. **INC-005-005: Evaluación sesgo embeddings** 🟡 ALTA
   - **Esfuerzo:** 2h
   - **Tipo:** Python Microservicio
   - **Notas:** WEAT, debiasing, reportes

3. **INC-005-006: Detección post-generación grounding** 🟡 ALTA
   - **Esfuerzo:** 2h
   - **Tipo:** Python Microservicio
   - **Notas:** Validación pre-generación, score confianza

4. **INC-005-007: Evaluación calidad chunks** 🟢 MEDIA
   - **Esfuerzo:** 2h
   - **Tipo:** Python Microservicio
   - **Notas:** Coherencia, completitud, rupturas semánticas

5. **INC-010-012: Análisis de Sentimiento en Feedback** 🟢 MEDIA
   - **Esfuerzo:** 1h
   - **Tipo:** Python Microservicio
   - **Prompt:** `/docs/compliance/gaps/prompts/python/INC-010-012_analisis_sentimiento.md`

**Total:** 5 incidencias, ~10 horas

---

### **AGENTE 3 - Integraciones y APIs**

**Especialidad:** Backend Senior - Integraciones Externas
**Incidencias Asignadas:** 5
**Esfuerzo Estimado:** ~10 horas

#### **Incidencias:**

1. **INC-020: Integración APIs autoridades** 🔴 CRÍTICA
   - **Esfuerzo:** 3h + TBD
   - **Tipo:** Integración Externa
   - **Prompt:** `/docs/compliance/gaps/prompts/java/INC-020_integracion_apis.md`
   - **Notas:** Preparar para API oficial, mock si no disponible

2. **INC-010-010: Integración con Sistema de Feedback** 🟡 ALTA
   - **Esfuerzo:** 2h
   - **Tipo:** Backend + Python
   - **Prompt:** `/docs/compliance/gaps/prompts/java/INC-010-010_integracion_feedback.md`

3. **INC-010-015: Integración con Sistemas Externos** 🟢 MEDIA
   - **Esfuerzo:** 2h
   - **Tipo:** Backend + Python
   - **Prompt:** `/docs/compliance/gaps/prompts/java/INC-010-015_integracion_sistemas_externos.md`
   - **Notas:** Conectores Azure ML, SageMaker

4. **INC-015: Verificación términos OpenAI** 🟡 MEDIA
   - **Esfuerzo:** 1h
   - **Tipo:** Backend
   - **Notas:** Validar compliance legal

5. **INC-021: Versionado FRIA** 🟡 MEDIA
   - **Esfuerzo:** 2h
   - **Tipo:** Backend
   - **Notas:** Historial de versiones

**Total:** 5 incidencias, ~10 horas

---

### **AGENTE 4 - PMM y Dashboards**

**Especialidad:** Backend Senior - PMM y Visualizaciones
**Incidencias Asignadas:** 6
**Esfuerzo Estimado:** ~12 horas

#### **Incidencias:**

1. **INC-010-004: Implementación Real PostMarketMonitoringService** 🔴 CRÍTICA
   - **Esfuerzo:** 2h
   - **Tipo:** Backend
   - **Prompt:** `/docs/compliance/gaps/prompts/java/INC-010-004_implementacion_real_pmm_service.md`
   - **Notas:** Eliminar mocks, integrar servicios reales

2. **INC-010-008: Dashboard de Supervisión Continua** 🟡 ALTA
   - **Esfuerzo:** 3h
   - **Tipo:** Backend + Frontend
   - **Prompt:** `/docs/compliance/gaps/prompts/java/INC-010-008_dashboard_supervision.md`
   - **Notas:** Dashboard con métricas en tiempo real

3. **INC-010-009: API REST para Consulta de Histórico** 🟡 ALTA
   - **Esfuerzo:** 2h
   - **Tipo:** Backend
   - **Prompt:** `/docs/compliance/gaps/prompts/java/INC-010-009_api_rest_historico.md`
   - **Notas:** Endpoints con filtros y paginación

4. **INC-010-013: Visualización de Tendencias Avanzadas** 🟢 MEDIA
   - **Esfuerzo:** 3h
   - **Tipo:** Backend + Frontend
   - **Prompt:** `/docs/compliance/gaps/prompts/java/INC-010-013_visualizacion_tendencias.md`
   - **Notas:** Gráficos con baseline y predicciones

5. **INC-010-014: Configuración de Frecuencias por Proyecto** 🟢 MEDIA
   - **Esfuerzo:** 1h
   - **Tipo:** Backend
   - **Prompt:** `/docs/compliance/gaps/prompts/java/INC-010-014_configuracion_frecuencias.md`

6. **INC-017: Dashboard consolidado compliance** 🟡 MEDIA
   - **Esfuerzo:** 3h
   - **Tipo:** Backend + Frontend
   - **Notas:** Nueva pantalla ZUL

**Total:** 6 incidencias, ~14 horas

---

### **AGENTE 5 - BPMN y Workflows**

**Especialidad:** Backend Mid - BPMN y Automatización
**Incidencias Asignadas:** 4
**Esfuerzo Estimado:** ~8 horas

#### **Incidencias:**

1. **INC-010-007: Implementación de Informes Automáticos** 🟡 ALTA
   - **Esfuerzo:** 2h
   - **Tipo:** BPMN + Backend
   - **Prompt:** `/docs/compliance/gaps/prompts/bpmn/INC-010-007_informes_automaticos.md`
   - **Notas:** Timers BPMN para generación automática

2. **INC-005-009: Proceso mejora continua** 🟢 MEDIA
   - **Esfuerzo:** 3h
   - **Tipo:** Python + BPMN
   - **Notas:** A/B testing, feedback, auto-update

3. **INC-006: Verificación automática integridad logs** 🟡 MEDIA
   - **Esfuerzo:** 2h
   - **Tipo:** Backend + BPMN
   - **Notas:** Job diario programado

4. **INC-009-DS: Notificaciones automáticas** 🟡 MEDIA
   - **Esfuerzo:** 1h
   - **Tipo:** Java/BPMN
   - **Notas:** Email, Slack

**Total:** 4 incidencias, ~8 horas

---

### **AGENTE 6 - Frontend y Visualizaciones** ⭐ NUEVO

**Especialidad:** Frontend + Backend - UI y Visualizaciones
**Incidencias Asignadas:** 5
**Esfuerzo Estimado:** ~10 horas

#### **Incidencias:**

1. **INC-003-DS: Visualizaciones gráficas bias** 🟡 ALTA
   - **Esfuerzo:** 3h
   - **Tipo:** Java/Frontend
   - **Notas:** Histogramas, box plots

2. **INC-008-DS: Exportación reportes** 🟡 MEDIA
   - **Esfuerzo:** 2h
   - **Tipo:** Backend+Frontend
   - **Notas:** PDF, Excel, JSON

3. **INC-018: Búsqueda avanzada logs** 🟡 MEDIA
   - **Esfuerzo:** 2h
   - **Tipo:** Backend + Frontend
   - **Notas:** Filtros múltiples

4. **INC-016: Exportación árbol riesgo** 🟢 BAJA
   - **Esfuerzo:** 2h
   - **Tipo:** Frontend
   - **Notas:** PDF, PNG, JSON

5. **INC-024: Reporte ejecutivo consolidado** 🟢 BAJA
   - **Esfuerzo:** 2h
   - **Tipo:** Backend+Frontend
   - **Notas:** PDF ejecutivo

**Total:** 5 incidencias, ~11 horas

---

### **AGENTE 7 - Optimización y DBA** ⭐ NUEVO

**Especialidad:** Backend + DBA - Optimización y Performance
**Incidencias Asignadas:** 5
**Esfuerzo Estimado:** ~8 horas

#### **Incidencias:**

1. **INC-010-011: Optimización de Consultas** 🟡 ALTA
   - **Esfuerzo:** 2h
   - **Tipo:** DBA
   - **Prompt:** `/docs/compliance/gaps/prompts/dba/INC-010-011_optimizacion_consultas.md`
   - **Notas:** Vistas materializadas TimescaleDB

2. **INC-007-DS: Benchmarks industria** 🟡 MEDIA
   - **Esfuerzo:** 2h
   - **Tipo:** Data Science + Backend
   - **Notas:** Tabla benchmarks por sector

3. **INC-010: Umbrales configurables métricas** 🟡 MEDIA
   - **Esfuerzo:** 1h
   - **Tipo:** Backend
   - **Notas:** Tabla configuración

4. **INC-014: Retención histórica evaluaciones** 🟡 MEDIA
   - **Esfuerzo:** 1h
   - **Tipo:** Backend + DBA
   - **Notas:** Tabla historial

5. **INC-009-005: Optimización queries** 🟢 BAJA
   - **Esfuerzo:** 2h
   - **Tipo:** DBA Team
   - **Notas:** Índices GIN + aggregates

**Total:** 5 incidencias, ~8 horas

---

### **AGENTE 8 - Funcionalidades Opcionales** ⭐ NUEVO

**Especialidad:** Backend - Funcionalidades Complementarias
**Incidencias Asignadas:** 4
**Esfuerzo Estimado:** ~6 horas

#### **Incidencias:**

1. **INC-009: Persistencia estado árbol riesgo** 🟢 BAJA
   - **Esfuerzo:** 1h
   - **Tipo:** Backend
   - **Notas:** Auto-guardado

2. **INC-019: Notificaciones vencimientos** 🟢 BAJA
   - **Esfuerzo:** 1h
   - **Tipo:** Backend
   - **Notas:** Job programado

3. **INC-022: Caché evaluaciones técnicas** 🟢 BAJA
   - **Esfuerzo:** 2h
   - **Tipo:** Backend
   - **Notas:** Performance

4. **INC-010-DS: Historial versiones evaluaciones** 🟢 BAJA
   - **Esfuerzo:** 1h
   - **Tipo:** Backend
   - **Notas:** Versionado de evaluaciones

5. **INC-009-001: Documentación formal capacidad** 🟡 MEDIA
   - **Esfuerzo:** 1h
   - **Tipo:** SRE Team
   - **Notas:** Tests de carga

**Total:** 5 incidencias, ~6 horas

---

## 📋 RESUMEN POR PRIORIDAD

### **Incidencias Críticas (🔴):**
- **Agente 1:** INC-007
- **Agente 3:** INC-020
- **Agente 4:** INC-010-004
- **Total:** 3 incidencias críticas pendientes

### **Incidencias Altas (🟡):**
- **Agente 1:** INC-005-DS
- **Agente 2:** INC-005-004, INC-005-005, INC-005-006
- **Agente 3:** INC-010-010
- **Agente 4:** INC-010-008, INC-010-009
- **Agente 5:** INC-010-007
- **Agente 6:** INC-003-DS
- **Agente 7:** INC-010-011
- **Total:** 10 incidencias altas

### **Incidencias Medias (🟡):**
- **Agente 1:** INC-002, INC-004, INC-008
- **Agente 2:** INC-005-007, INC-010-012
- **Agente 3:** INC-010-015, INC-015, INC-021
- **Agente 4:** INC-010-013, INC-010-014, INC-017
- **Agente 5:** INC-005-009, INC-006, INC-009-DS
- **Agente 6:** INC-008-DS, INC-018
- **Agente 7:** INC-007-DS, INC-010, INC-014
- **Agente 8:** INC-009-001
- **Total:** 18 incidencias medias

### **Incidencias Bajas (🟢):**
- **Agente 6:** INC-016, INC-024
- **Agente 7:** INC-009-005
- **Agente 8:** INC-009, INC-019, INC-022, INC-010-DS
- **Total:** 7 incidencias bajas

---

## 📝 NOTAS IMPORTANTES

### **Dependencias:**
- **INC-007** (Agente 1) requiere microservicio Python
- **INC-020** (Agente 3) puede estar bloqueado si API oficial no disponible
- **INC-010-004** (Agente 4) ya tiene servicio, solo necesita verificación

### **Microservicios Python Requeridos:**
- **Agente 2:** 5 microservicios Python (RAG, embeddings, chunks, sentimiento)
- **Agente 1:** 1 microservicio Python (FRIA vs métricas)

### **BPMN Requeridos:**
- **Agente 5:** 2 procesos BPMN (informes automáticos, mejora continua)

### **Frontend Requerido:**
- **Agente 4:** 2 dashboards (supervisión continua, tendencias)
- **Agente 6:** 5 pantallas/visualizaciones

---

## ✅ PRÓXIMOS PASOS

1. **Crear documentos de guía** para Agentes 6, 7 y 8
2. **Actualizar documentos existentes** de Agentes 1-5 con nuevas incidencias
3. **Verificar prompts** disponibles para todas las incidencias
4. **Crear prompts faltantes** si es necesario
5. **Iniciar trabajo** según prioridad

---

**Última Actualización:** 25 de noviembre de 2025
