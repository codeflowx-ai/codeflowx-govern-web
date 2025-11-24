# RESUMEN - PROMPTS EVALUACIÓN DE DATASETS

**Fecha Creación:** Noviembre 2025  
**Total Prompts Creados:** 12  
**Estado:** ✅ Todos los prompts creados

---

## PROMPTS CREADOS POR PRIORIDAD

### 🔴 CRÍTICAS (2 prompts)

| ID | Archivo | Tipo | Esfuerzo | Responsable |
|----|---------|------|----------|-------------|
| **INC-001-DS** | `prompts/python/INC-001_limite_tamaño_archivo.md` | Python | 2-3 días | MLOps |
| **INC-002-DS** | `prompts/python/INC-002_streaming_datasets_grandes.md` | Python | 5-7 días | MLOps |

**Descripción:**
- **INC-001:** Aumentar límite de 100MB a 500MB-1GB configurable, validación de memoria
- **INC-002:** Implementar procesamiento por chunks/streaming para datasets grandes

---

### 🟠 ALTAS (3 prompts)

| ID | Archivo | Tipo | Esfuerzo | Responsable |
|----|---------|------|----------|-------------|
| **INC-003-DS** | `prompts/java/INC-003_visualizaciones_graficas_bias.md` | Java+Frontend | 3-4 días | Frontend |
| **INC-004-DS** | `prompts/python/INC-004_timeout_adaptativo.md` | Python | 2-3 días | MLOps |
| **INC-005-DS** | `prompts/java/INC-005_validacion_integridad_datasets.md` | Java+Python | 1-2 días | Backend |

**Descripción:**
- **INC-003:** Visualizaciones gráficas (histogramas, box plots, heatmaps, tendencias)
- **INC-004:** Timeout adaptativo basado en tamaño de dataset, retry con backoff
- **INC-005:** Validación de integridad con hash SHA-256

---

### 🟡 MEDIAS (4 prompts)

| ID | Archivo | Tipo | Esfuerzo | Responsable |
|----|---------|------|----------|-------------|
| **INC-006-DS** | `prompts/python/INC-006_metricas_confianza_estadistica.md` | Python+Java | 3-4 días | Data Science |
| **INC-007-DS** | `prompts/java/INC-007_benchmarks_industria.md` | Java | 4-5 días | Data Science+Compliance |
| **INC-008-DS** | `prompts/java/INC-008_exportacion_reportes.md` | Java+Frontend | 3-4 días | Backend+Frontend |
| **INC-009-DS** | `prompts/java/INC-009_notificaciones_automaticas.md` | Java+BPMN | 2-3 días | DevOps |

**Descripción:**
- **INC-006:** Intervalos de confianza y p-values para métricas
- **INC-007:** Comparación con benchmarks de industria por sector
- **INC-008:** Exportación de reportes en PDF, Excel, JSON
- **INC-009:** Notificaciones automáticas (email, Slack) para hallazgos críticos

---

### 🟢 BAJAS (3 prompts)

| ID | Archivo | Tipo | Esfuerzo | Responsable |
|----|---------|------|----------|-------------|
| **INC-010-DS** | `prompts/java/INC-010_historial_versiones_evaluaciones.md` | Java | 2-3 días | Backend |
| **INC-011-DS** | `prompts/python/INC-011_recomendaciones_automaticas.md` | Python | 4-5 días | Data Science |
| **INC-012-DS** | `prompts/python/INC-012_integracion_dvc_git_lfs.md` | Python | 5-7 días | MLOps |

**Descripción:**
- **INC-010:** Versionado de evaluaciones con historial
- **INC-011:** Generación automática de recomendaciones accionables
- **INC-012:** Integración con DVC y Git LFS para importar datasets

---

## DISTRIBUCIÓN POR TIPO DE AGENTE

### Python (Microservicios) - 6 prompts
- INC-001-DS: Límite tamaño
- INC-002-DS: Streaming
- INC-004-DS: Timeout adaptativo
- INC-006-DS: Métricas confianza
- INC-011-DS: Recomendaciones automáticas
- INC-012-DS: Integración DVC/Git LFS

### Java (Backend) - 5 prompts
- INC-003-DS: Visualizaciones (requiere Frontend también)
- INC-005-DS: Validación integridad (requiere Python también)
- INC-007-DS: Benchmarks industria
- INC-008-DS: Exportación reportes (requiere Frontend también)
- INC-009-DS: Notificaciones automáticas
- INC-010-DS: Historial versiones

### Frontend - 2 prompts (compartidos)
- INC-003-DS: Visualizaciones gráficas
- INC-008-DS: Botones de exportación

### BPMN - 1 prompt (compartido)
- INC-009-DS: Workflow de notificaciones

---

## ESFUERZO TOTAL ESTIMADO

| Prioridad | Prompts | Esfuerzo Total |
|-----------|---------|----------------|
| 🔴 Críticas | 2 | 7-10 días |
| 🟠 Altas | 3 | 6-9 días |
| 🟡 Medias | 4 | 12-16 días |
| 🟢 Bajas | 3 | 11-15 días |
| **TOTAL** | **12** | **36-50 días** |

---

## ESTRUCTURA DE ARCHIVOS CREADOS

```
gaps/prompts/
├── python/
│   ├── INC-001_limite_tamaño_archivo.md ✅
│   ├── INC-002_streaming_datasets_grandes.md ✅
│   ├── INC-004_timeout_adaptativo.md ✅
│   ├── INC-006_metricas_confianza_estadistica.md ✅
│   ├── INC-011_recomendaciones_automaticas.md ✅
│   └── INC-012_integracion_dvc_git_lfs.md ✅
└── java/
    ├── INC-003_visualizaciones_graficas_bias.md ✅
    ├── INC-005_validacion_integridad_datasets.md ✅
    ├── INC-007_benchmarks_industria.md ✅
    ├── INC-008_exportacion_reportes.md ✅
    ├── INC-009_notificaciones_automaticas.md ✅
    └── INC-010_historial_versiones_evaluaciones.md ✅
```

---

## PRÓXIMOS PASOS

### Fase 1: Críticas (Q1 2026)
1. ✅ INC-001-DS: Aumentar límite tamaño (MLOps)
2. ✅ INC-002-DS: Implementar streaming (MLOps)

### Fase 2: Altas (Q2 2026)
3. ✅ INC-003-DS: Visualizaciones gráficas (Frontend)
4. ✅ INC-004-DS: Timeout adaptativo (MLOps)
5. ✅ INC-005-DS: Validación integridad (Backend)

### Fase 3: Medias (Q3 2026)
6. ✅ INC-006-DS: Métricas confianza (Data Science)
7. ✅ INC-007-DS: Benchmarks industria (Data Science)
8. ✅ INC-008-DS: Exportación reportes (Backend+Frontend)
9. ✅ INC-009-DS: Notificaciones automáticas (DevOps)

### Fase 4: Bajas (Q4 2026)
10. ✅ INC-010-DS: Historial versiones (Backend)
11. ✅ INC-011-DS: Recomendaciones automáticas (Data Science)
12. ✅ INC-012-DS: Integración DVC/Git LFS (MLOps)

---

## CUMPLIMIENTO NORMATIVO

### EU AI Act
- ✅ **Art. 10.1.a:** Datos representativos (INC-001, INC-002, INC-004)
- ✅ **Art. 10.1.b:** Sesgos mínimos (INC-007)
- ✅ **Art. 10.2:** Transparencia y mitigación (INC-003, INC-011)
- ✅ **Art. 15.1:** Precisión (INC-006)
- ✅ **Art. 72:** Incident reporting (INC-009)

### ISO 42001
- ✅ **8.2.2:** Data governance (INC-005, INC-010, INC-012)
- ✅ **9.2:** Auditoría interna (INC-008)

---

## NOTAS IMPORTANTES

1. **Dependencias entre prompts:**
   - INC-002 (streaming) puede requerir INC-001 (límite tamaño) primero
   - INC-006 (confianza) mejora INC-007 (benchmarks)
   - INC-008 (exportación) requiere INC-003 (visualizaciones) para gráficos en PDF

2. **Integraciones externas:**
   - INC-009 requiere configuración SMTP/Slack
   - INC-012 requiere acceso a repositorios DVC/Git LFS

3. **Testing:**
   - Todos los prompts incluyen sección de testing
   - Validaciones específicas por incidencia

---

**Última actualización:** Noviembre 2025  
**Versión:** 1.0  
**Mantenedor:** CodeflowX Compliance Team

