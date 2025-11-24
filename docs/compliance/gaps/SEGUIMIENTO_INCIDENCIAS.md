# SEGUIMIENTO DE INCIDENCIAS - ESTADO DE IMPLEMENTACIÓN

**Última Actualización:** Diciembre 2025  
**Responsable:** CodeflowX Compliance Team

---

## LEYENDA DE ESTADOS

- 🔴 **PENDIENTE**: No iniciado
- 🟡 **EN PROGRESO**: En desarrollo
- 🟢 **COMPLETADO**: Implementado y probado
- ⚠️ **BLOQUEADO**: Esperando dependencias
- ✅ **VERIFICADO**: Revisado y aprobado

---

## INCIDENCIAS CRÍTICAS (🔴)

| ID | Descripción | Artículo | Prioridad | Estado | Asignado | Fecha Inicio | Fecha Fin | Notas |
|----|-------------|----------|-----------|--------|----------|--------------|-----------|-------|
| INC-001 | Validación coherencia modelo-dataset | Art. 10, 11 | 🔴 CRÍTICA | 🟢 COMPLETADO | Backend | 2025-12-19 | 2025-12-19 | Implementado en HighRiskClassifierViewModel |
| **INC-001-DS** | **Límite tamaño archivo insuficiente** | **Art. 10.1.a** | **🔴 CRÍTICA** | **🟢 COMPLETADO** | **MLOps** | **2025-11** | **2025-11** | **✅ Python: límite a 1GB configurable + validación memoria** |
| **INC-002-DS** | **Falta streaming datasets grandes** | **Art. 10.1.a** | **🔴 CRÍTICA** | **🟢 COMPLETADO** | **MLOps** | **2025-11** | **2025-11** | **✅ Python: streaming automático por chunks implementado** |
| INC-003 | Validación documentación técnica completa | Art. 11, Anexo IV | 🔴 CRÍTICA | 🟢 COMPLETADO | Backend | 2025-12-19 | 2025-12-19 | Implementado en HighRiskClassifierViewModel |
| INC-005 | Validación sistemas prohibidos Art. 5 | Art. 5, Anexo II | 🔴 CRÍTICA | 🔴 PENDIENTE | - | - | - | Checklist automático requerido |
| **INC-005-DS** | **Validación integridad datasets** | **ISO 42001 8.2.2** | **🟠 HIGH** | **🔴 PENDIENTE** | **Backend** | **-** | **-** | **Java+Python: hash SHA-256** |
| INC-007 | Validación cruzada FRIA vs métricas | Art. 27 | 🔴 CRÍTICA | 🔴 PENDIENTE | - | - | - | Requiere microservicio Python |
| INC-011 | Checklist completo validación modelos | Art. 11, 15 | 🔴 CRÍTICA | 🔴 PENDIENTE | - | - | - | Validación por tipo de modelo |
| **INC-011-01** | **Control acceso basado en roles** | **Art. 12, 19, ISO 27001** | **🔴 CRÍTICA** | **🟢 COMPLETADO** | **Backend + DBA** | **2025-12** | **2025-12** | **Scripts SQL creados, prompts listos** |
| **INC-011-02** | **Versionado explícito datasets** | **Art. 10, 11, GDPR 30** | **🔴 CRÍTICA** | **🟢 COMPLETADO** | **Backend + DBA** | **2025-12** | **2025-12** | **Scripts SQL creados, prompts listos** |
| INC-012 | Alertas automáticas tampering | Art. 19 | 🔴 CRÍTICA | 🔴 PENDIENTE | - | - | - | Job programado + notificaciones |
| INC-013 | Validación medidas mitigación implementadas | Art. 27.1.f | 🔴 CRÍTICA | 🔴 PENDIENTE | - | - | - | Verificar implementación real |
| INC-020 | Integración APIs autoridades | Art. 27.3, 49 | 🔴 CRÍTICA | 🔴 PENDIENTE | - | - | - | Preparar para API oficial |
| ~~**INC-005-001**~~ | ~~**Almacenamiento inmutable verdadero**~~ | **Art. 17** | **🔴 CRÍTICA** | **✅ RESUELTO** | **Backend** | **2025-01-27** | **2025-01-27** | **✅ Usa tabla general IMLIMMUTABLELOGS. Ver AUDITORIA_005_EVALUACION_RAG.md sección 6.2** |
| **INC-005-002** | **Detección insuficiente alucinaciones** | **Art. 13** | **🔴 CRÍTICA** | **🔴 PENDIENTE** | **ML/AI** | **-** | **-** | **Python: SelfCheckGPT, FactScore, Entailment** |
| **INC-005-003** | **Validación reactiva políticas cliente** | **Art. 15** | **🔴 CRÍTICA** | **🟢 COMPLETADO** | **Backend** | **2025-01-27** | **2025-01-27** | **Java: Entidad, Repositorio, Servicio creados. Pendiente: integración Python para scoring** |

---

## INCIDENCIAS MEDIAS (🟡)

| ID | Descripción | Artículo | Prioridad | Estado | Asignado | Fecha Inicio | Fecha Fin | Notas |
|----|-------------|----------|-----------|--------|----------|--------------|-----------|-------|
| INC-002 | Validación confianza sugerencia IA | Art. 6 | 🟡 MEDIA | 🔴 PENDIENTE | - | - | - | Aumentar umbral a 0.85 |
| **INC-003-DS** | **Visualizaciones gráficas bias** | **Art. 10.2** | **🟠 HIGH** | **🔴 PENDIENTE** | **Frontend** | **-** | **-** | **Java/Frontend: histogramas, box plots** |
| **INC-004-DS** | **Timeout adaptativo** | **Art. 10.1.a** | **🟠 HIGH** | **🟢 COMPLETADO** | **MLOps** | **2025-11** | **2025-11** | **✅ Python: timeout dinámico según tamaño implementado** |
| INC-004 | Validación calidad justificación | Art. 6 | 🟡 MEDIA | 🔴 PENDIENTE | - | - | - | NLP para detectar genéricas |
| INC-006 | Verificación automática integridad logs | Art. 19 | 🟡 MEDIA | 🔴 PENDIENTE | - | - | - | Job diario programado |
| **INC-006-DS** | **Métricas confianza estadística** | **Art. 15.1** | **🟡 MEDIUM** | **🟢 COMPLETADO** | **Data Science** | **2025-01-XX** | **2025-01-XX** | **✅ Python: intervalos confianza bootstrap + p-values implementados** |
| **INC-007-DS** | **Benchmarks industria** | **Art. 10.1.b** | **🟡 MEDIUM** | **🔴 PENDIENTE** | **Data Science** | **-** | **-** | **Java: tabla benchmarks por sector** |
| **INC-008-DS** | **Exportación reportes** | **ISO 42001 9.2** | **🟡 MEDIUM** | **🔴 PENDIENTE** | **Backend+Frontend** | **-** | **-** | **Java: PDF, Excel, JSON** |
| **INC-009-DS** | **Notificaciones automáticas** | **Art. 72** | **🟡 MEDIUM** | **🔴 PENDIENTE** | **DevOps** | **-** | **-** | **Java/BPMN: email, Slack** |
| INC-008 | Validación fórmula cálculo riesgo | Art. 27 | 🟡 MEDIA | 🔴 PENDIENTE | - | - | - | Documentar y testear |
| INC-010 | Umbrales configurables métricas | Art. 10, 15 | 🟡 MEDIA | 🔴 PENDIENTE | - | - | - | Tabla configuración |
| INC-014 | Retención histórica evaluaciones | Art. 12, 18 | 🟡 MEDIA | 🔴 PENDIENTE | - | - | - | Tabla historial |
| INC-015 | Verificación términos OpenAI | Art. 10 | 🟡 MEDIA | 🔴 PENDIENTE | - | - | - | Validar compliance legal |
| INC-017 | Dashboard consolidado compliance | Múltiples | 🟡 MEDIA | 🔴 PENDIENTE | - | - | - | Nueva pantalla ZUL |
| INC-018 | Búsqueda avanzada logs | Art. 19 | 🟡 MEDIA | 🔴 PENDIENTE | - | - | - | Filtros múltiples |
| INC-021 | Versionado FRIA | Art. 27 | 🟡 MEDIA | 🔴 PENDIENTE | - | - | - | Historial de versiones |
| **INC-011-03** | **Versionado explícito evaluaciones** | **Art. 12, 11** | **🟡 ALTA** | **🟢 COMPLETADO** | **Backend** | **2025-12** | **2025-12** | **Scripts SQL creados, prompts listos** |
| **INC-011-04** | **Validación formato SemVer** | **Art. 11** | **🟡 ALTA** | **🟢 COMPLETADO** | **Backend + DBA** | **2025-12** | **2025-12** | **Scripts SQL creados, prompts listos** |
| INC-023 | Validación calidad FRIA | Art. 27 | 🟡 MEDIA | 🔴 PENDIENTE | - | - | - | NLP para calidad |
| **INC-005-004** | **Falta métricas RAG estandarizadas** | **Art. 10, 13** | **🟡 ALTA** | **🔴 PENDIENTE** | **ML/AI** | **-** | **-** | **Python: RAGAS, ARES, benchmarking BEIR/MTEB** |
| **INC-005-005** | **No evaluación sesgo embeddings** | **Art. 10** | **🟡 ALTA** | **🔴 PENDIENTE** | **ML/AI+Ética** | **-** | **-** | **Python: WEAT, debiasing, reportes** |
| **INC-005-006** | **Detección post-generación grounding** | **Art. 13** | **🟡 ALTA** | **🔴 PENDIENTE** | **ML/AI** | **-** | **-** | **Python: validación pre-generación, score confianza** |

---

## INCIDENCIAS AUDITORÍA 009 - MONITORIZACIÓN TIEMPO REAL

| ID | Descripción | Artículo | Prioridad | Estado | Asignado | Fecha Inicio | Fecha Fin | Notas |
|----|-------------|----------|-----------|--------|----------|--------------|-----------|-------|
| INC-009-001 | Documentación formal capacidad | Art. 12, 15 | 🟡 MEDIA | 🔴 PENDIENTE | SRE Team | - | - | Tests de carga |
| INC-009-002 | Throttling y backpressure | Art. 12 | 🟡 MEDIA | 🟢 COMPLETADO | Backend | 2025-11-17 | 2025-11-17 | Código aplicado |
| INC-009-003 | Análisis asíncrono | Art. 12 | 🟡 MEDIA | 🟢 COMPLETADO | Backend | 2025-11-17 | 2025-11-17 | Código aplicado |
| INC-009-004 | Métricas de incidentes | Art. 15 | 🟡 MEDIA | 🟢 COMPLETADO | Backend + Analytics | 2025-11-17 | 2025-11-17 | Entidad y servicio creados |
| INC-009-005 | Optimización queries | Art. 12 | 🟢 BAJA | 🔴 PENDIENTE | DBA Team | - | - | Índices GIN + aggregates |
| INC-009-006 | Health indicator | Art. 12 | 🟢 BAJA | 🟢 COMPLETADO | SRE + Backend | 2025-11-17 | 2025-11-17 | HealthIndicator creado |

---

## INCIDENCIAS BAJAS (🟢)

| ID | Descripción | Artículo | Prioridad | Estado | Asignado | Fecha Inicio | Fecha Fin | Notas |
|----|-------------|----------|-----------|--------|----------|--------------|-----------|-------|
| INC-009 | Persistencia estado árbol riesgo | Art. 27 | 🟢 BAJA | 🔴 PENDIENTE | - | - | - | Auto-guardado |
| **INC-010-DS** | **Historial versiones evaluaciones** | **ISO 42001 8.2.2** | **🟢 LOW** | **🔴 PENDIENTE** | **Backend** | **-** | **-** | **Java: versionado de evaluaciones** |
| **INC-011-DS** | **Recomendaciones automáticas** | **Art. 10.2** | **🟢 LOW** | **🔴 PENDIENTE** | **Data Science** | **-** | **-** | **Python: generación automática** |
| **INC-012-DS** | **Integración DVC/Git LFS** | **ISO 42001 8.2.2** | **🟢 LOW** | **🟢 COMPLETADO** | **MLOps** | **2025-01-XX** | **2025-01-XX** | **✅ Python: conectores DVC/Git LFS implementados en leka-bias-detection-service** |
| INC-016 | Exportación árbol riesgo | Art. 27 | 🟢 BAJA | 🔴 PENDIENTE | - | - | - | PDF, PNG, JSON |
| INC-019 | Notificaciones vencimientos | Art. 27, 49 | 🟢 BAJA | 🔴 PENDIENTE | - | - | - | Job programado |
| INC-022 | Caché evaluaciones técnicas | Art. 15 | 🟢 BAJA | 🔴 PENDIENTE | - | - | - | Performance |
| INC-024 | Reporte ejecutivo consolidado | Múltiples | 🟢 BAJA | 🔴 PENDIENTE | - | - | - | PDF ejecutivo |
| **INC-005-007** | **Falta evaluación calidad chunks** | **Art. 13** | **🟢 MEDIA** | **🔴 PENDIENTE** | **ML/AI** | **-** | **-** | **Python: coherencia, completitud, rupturas semánticas** |
| ~~**INC-005-008**~~ | ~~**Falta validación ética y valores**~~ | **Art. 15** | **🟢 MEDIA** | **✅ RESUELTO** | **Backend** | **2025-01-27** | **2025-01-27** | **✅ Usa entidad general EthicsReview (ETHETHICSREVIEWS). Ver COMPARACION_ETICA_RAG.md** |
| **INC-005-009** | **Falta proceso mejora continua** | **Art. 13** | **🟢 MEDIA** | **🔴 PENDIENTE** | **ML Ops** | **-** | **-** | **Python+BPMN: A/B testing, feedback, auto-update** |

---

## MÉTRICAS DE PROGRESO

**Total Incidencias:** 49 (24 originales + 12 evaluación datasets + 9 evaluación RAG + 4 versionado)  
**Completadas:** 12 (24%) - Incluye 2 resueltas por reutilización (INC-005-001, INC-005-008) + 3 nuevas (INC-001-DS, INC-002-DS, INC-004-DS)  
**En Progreso:** 0 (0%)  
**Pendientes:** 37 (76%)

**Por Prioridad:**
- Críticas: 7/15 (47%) - Incluye 4 de datasets (INC-001-DS✅, INC-002-DS✅) + 2 de RAG (INC-005-001✅, INC-005-003) + 2 de versionado (INC-011-01, INC-011-02)
- Altas: 3/8 (38%) - Incluye 4 de datasets (INC-004-DS✅) + 3 de RAG + 2 de versionado (INC-011-03, INC-011-04)
- Medias: 2/17 (12%) - Incluye 4 de datasets + 1 de RAG (INC-005-008✅)
- Bajas: 0/9 (0%) - Incluye 3 de datasets

---

## PRÓXIMOS PASOS

1. ✅ Revisar y priorizar incidencias críticas
2. 🔄 Asignar responsables
3. 🔄 Crear tareas en sistema de gestión
4. 🔄 Iniciar implementación Fase 1 (Críticas)

---

## INCIDENCIAS AUDITORÍA 010 - POST MARKET MONITORING

Ver documento detallado: `SEGUIMIENTO_INCIDENCIAS_010_PMM.md`

**Resumen:**
- Total incidencias: 15
- Prompts creados: 15/15 (100%) ✅
- Prompts Java: 12/12 (100%) ✅
- Estado: Todos los prompts listos para implementación

---

## INCIDENCIAS AUDITORÍA 011 - VERSIONADO Y CONTROL DE CAMBIOS

Ver documentos:
- Auditoría: `/docs/compliance/auditoria/AUDITORIA_011_VERSIONADO_CONTROL_CAMBIOS.md`
- Incidencias: `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_VERSIONADO.md`
- Resumen Prompts: `/docs/compliance/gaps/RESUMEN_PROMPTS_VERSIONADO.md`

**Resumen:**
- Total incidencias: 4
- Prompts creados: 4/4 (100%) ✅
- Scripts SQL creados: 4/4 (100%) ✅
- Estado: Scripts SQL implementados, entidades Java según especificaciones en prompts

**Archivos SQL creados:**
- `versionado_control_acceso_roles.sql` - Tablas CORROLES, CORUSERROLES, VERSVERSIONAPPROVALS
- `versionado_triggers_prevent_update.sql` - Triggers para bloquear updates en versiones aprobadas
- `versionado_datasets.sql` - Tabla DATADATASETVERSIONS
- `versionado_evaluaciones.sql` - Campos de versionado en MODEVALUATIONS
- `validacion_semver_constraints.sql` - Constraints SemVer y migración de datos

**Prompts Java:**
- `INC-011-01_control_acceso_roles.md` ✅
- `INC-011-02_versionado_explicito_datasets.md` ✅
- `INC-011-03_versionado_explicito_evaluaciones.md` ✅
- `INC-011-04_validacion_semver.md` ✅

---

## NOTAS GENERALES

- Todas las incidencias tienen prompts detallados en `/prompts/`
- Verificar compliance con EU AI Act después de cada corrección
- Documentar cambios en código y pruebas realizadas
- Actualizar este documento al completar cada incidencia
- Incidencias INC-010 tienen seguimiento separado en `SEGUIMIENTO_INCIDENCIAS_010_PMM.md`

