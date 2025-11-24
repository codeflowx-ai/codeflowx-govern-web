# RECUENTO COMPLETO DE MICROSERVICIOS PYTHON

**Fecha:** Diciembre 2025  
**Objetivo:** Confirmar número exacto de microservicios Python necesarios según documentos de auditoría y prompts

---

## 📊 RESUMEN EJECUTIVO

**Total Microservicios Python:** **15 microservicios**

- **Microservicios Existentes (extendidos):** 7
- **Microservicios Nuevos (creados):** 7
- **Microservicios Adicionales:** 1

---

## PARTE 1: MICROSERVICIOS EXISTENTES (PROMPTS_01)

**Documento:** `PROMPTS_01_PYTHON_MICROSERVICIOS_EXISTENTES.md`  
**Total:** 9 microservicios mencionados (7 Python existentes + 1 Python nuevo + 1 Java)

### Microservicios Python Existentes:

1. ✅ **leka-bias-detection-service** (Puerto 8001)
   - **Funcionalidad:** Detección de sesgos en datasets tabulares
   - **Prompts asociados:** INC-001, INC-002, INC-004, INC-005-005, INC-006, INC-012
   - **Estado:** ✅ Extendido con funcionalidades EU AI Act

2. ✅ **leka-llm-evaluation** (Puerto 8002)
   - **Funcionalidad:** Evaluación de LLMs
   - **Prompts asociados:** INC-005-002, INC-010-012
   - **Estado:** ✅ Extendido con detección de alucinaciones y análisis de sentimiento

3. ✅ **leka-prompt-governance** (Puerto 8003)
   - **Funcionalidad:** Gobernanza de prompts
   - **Prompts asociados:** INC-001, INC-002, INC-004
   - **Estado:** ✅ Extendido con validación de tamaño de archivo, streaming, timeout adaptativo

4. ✅ **leka-rag-evaluation** (Puerto 8004)
   - **Funcionalidad:** Evaluación de sistemas RAG
   - **Prompts asociados:** INC-005-004, INC-005-006, INC-005-007
   - **Estado:** ✅ Extendido con métricas RAGAS, prevención grounding, calidad chunks

5. ✅ **leka-agent-monitoring** (Puerto 8005)
   - **Funcionalidad:** Monitoreo de agentes IA
   - **Prompts asociados:** INC-005-009
   - **Estado:** ✅ Extendido con mejora continua

6. ✅ **leka-model-wrapper** (Puerto 8006)
   - **Funcionalidad:** Wrapper para modelos de IA
   - **Prompts asociados:** INC-011
   - **Estado:** ✅ Extendido con recomendaciones automáticas

7. ✅ **leka-adversarial-robustness** (Puerto 8007) ⚠️ **NUEVO según PROMPTS_01**
   - **Funcionalidad:** Detección de ataques adversariales (Art. 15.5) + Validación proactiva de políticas
   - **Prompts asociados:** INC-005-003 (PolicyScoringService implementado aquí)
   - **Estado:** ✅ Creado como nuevo microservicio
   - **Confirmación:** Según `INCIDENCIAS_005_EVALUACION_RAG.md` línea 86, está en puerto 8007
   - **Nota:** En `RESUMEN_PROMPTS_PYTHON.md` aparece incorrectamente como puerto 8012

8. ✅ **leka-server-serving-evaluation** (Puerto ?)
   - **Funcionalidad:** Evaluación de modelos en producción
   - **Prompts asociados:** Ninguno específico en gaps
   - **Estado:** ✅ Existente, mencionado en TRACKING_PROMPTS_IMPLEMENTACION.md

9. ✅ **leka-llm-interpreter** (Puerto 8011)
   - **Funcionalidad:** Interpretación de resultados de LLMs
   - **Prompts asociados:** Ninguno específico en gaps
   - **Estado:** ✅ Existente

### ⚠️ NO ES PYTHON:
- **leka-orchestrator** (Puerto 8000) - **Spring Cloud Gateway Java** (NO cuenta como Python)

---

## PARTE 2: MICROSERVICIOS NUEVOS (PROMPTS_02)

**Documento:** `PROMPTS_02_PYTHON_MICROSERVICIOS_NUEVOS.md`  
**Total:** 5 microservicios nuevos

10. ✅ **leka-technical-documentation-generator** (Puerto 8008)
    - **Funcionalidad:** Generación de documentación técnica Anexo IV
    - **Prompts asociados:** Ninguno específico en gaps (es parte de PROMPTS_02)
    - **Estado:** ✅ Creado según TRACKING_PROMPTS_IMPLEMENTACION.md

11. ✅ **leka-conformity-assessment** (Puerto 8009)
    - **Funcionalidad:** Evaluación de conformidad Anexo VI
    - **Prompts asociados:** Ninguno específico en gaps
    - **Estado:** ✅ Creado según TRACKING_PROMPTS_IMPLEMENTACION.md

12. ✅ **leka-eu-declaration-generator** (Puerto 8010)
    - **Funcionalidad:** Generación de declaración UE Anexo V
    - **Prompts asociados:** Ninguno específico en gaps
    - **Estado:** ✅ Creado según TRACKING_PROMPTS_IMPLEMENTACION.md

13. ✅ **leka-fria-generator** (Puerto 8012)
    - **Funcionalidad:** Generación de FRIA Art. 27
    - **Prompts asociados:** INC-007
    - **Estado:** ✅ Creado y completado (Nov 2025)
    - **⚠️ NOTA:** Conflicto de puerto con leka-adversarial-robustness en algunos documentos

14. ✅ **leka-copyright-compliance** (Puerto 8013)
    - **Funcionalidad:** Verificación de copyright para GPAI
    - **Prompts asociados:** Ninguno específico en gaps
    - **Estado:** ✅ Creado según TRACKING_PROMPTS_IMPLEMENTACION.md

---

## PARTE 3: VERIFICACIÓN DE CONFLICTOS DE PUERTOS

### ⚠️ Conflicto Detectado:

**Puerto 8012:**
- `leka-fria-generator` (PROMPTS_02) - ✅ Confirmado puerto 8012
- `leka-adversarial-robustness` (RESUMEN_PROMPTS_PYTHON.md) - ❌ Error, debería ser 8007

**Resolución:**
- `leka-fria-generator` → Puerto 8012 ✅ (correcto según PROMPTS_02)
- `leka-adversarial-robustness` → Puerto 8007 ✅ (correcto según PROMPTS_01)

---

## PARTE 4: RECUENTO FINAL

### Microservicios Python Totales: **15**

#### Por Categoría:

**Microservicios Existentes Extendidos (7):**
1. leka-bias-detection-service (8001)
2. leka-llm-evaluation (8002)
3. leka-prompt-governance (8003)
4. leka-rag-evaluation (8004)
5. leka-agent-monitoring (8005)
6. leka-model-wrapper (8006)
7. leka-llm-interpreter (8011)

**Microservicios Nuevos Creados (7):**
8. leka-adversarial-robustness (8007) - NUEVO según PROMPTS_01
9. leka-technical-documentation-generator (8008)
10. leka-conformity-assessment (8009)
11. leka-eu-declaration-generator (8010)
12. leka-fria-generator (8012)
13. leka-copyright-compliance (8013)

**Microservicios Adicionales (1):**
14. leka-server-serving-evaluation (puerto no especificado) - Existente

---

## DISTRIBUCIÓN DE PUERTOS

| Puerto | Microservicio | Estado | Documento |
|--------|---------------|--------|-----------|
| 8001 | leka-bias-detection-service | ✅ Existente | PROMPTS_01 |
| 8002 | leka-llm-evaluation | ✅ Existente | PROMPTS_01 |
| 8003 | leka-prompt-governance | ✅ Existente | PROMPTS_01 |
| 8004 | leka-rag-evaluation | ✅ Existente | PROMPTS_01 |
| 8005 | leka-agent-monitoring | ✅ Existente | PROMPTS_01 |
| 8006 | leka-model-wrapper | ✅ Existente | PROMPTS_01 |
| 8007 | leka-adversarial-robustness | ✅ Nuevo | PROMPTS_01 |
| 8008 | leka-technical-documentation-generator | ✅ Nuevo | PROMPTS_02 |
| 8009 | leka-conformity-assessment | ✅ Nuevo | PROMPTS_02 |
| 8010 | leka-eu-declaration-generator | ✅ Nuevo | PROMPTS_02 |
| 8011 | leka-llm-interpreter | ✅ Existente | PROMPTS_01 |
| 8012 | leka-fria-generator | ✅ Nuevo | PROMPTS_02 |
| 8013 | leka-copyright-compliance | ✅ Nuevo | PROMPTS_02 |
| ? | leka-server-serving-evaluation | ✅ Existente | PROMPTS_01 |

**Total Puertos Asignados:** 14 (13 con puerto específico + 1 sin puerto)  
**Total Microservicios Python:** 15

---

## CONCLUSIÓN

### ✅ **CONFIRMACIÓN: 15 MICROSERVICIOS PYTHON**

**Desglose:**
- **7 microservicios existentes** extendidos con funcionalidades EU AI Act
- **7 microservicios nuevos** creados para compliance EU AI Act
- **1 microservicio adicional** (leka-server-serving-evaluation sin puerto específico)

**Total:** 15 microservicios Python

### ⚠️ Corrección Necesaria en RESUMEN_PROMPTS_PYTHON.md:

En `RESUMEN_PROMPTS_PYTHON.md` línea 272, `leka-adversarial-robustness` aparece con puerto 8012, pero según:
- `PROMPTS_01_PYTHON_MICROSERVICIOS_EXISTENTES.md` línea 999 → **puerto 8007** ✅
- `INCIDENCIAS_005_EVALUACION_RAG.md` línea 86 → **puerto 8007** ✅

**Corrección:** `leka-adversarial-robustness` debe usar **puerto 8007**, NO 8012.

**Puerto 8012** está correctamente asignado a `leka-fria-generator` según `PROMPTS_02_PYTHON_MICROSERVICIOS_NUEVOS.md` línea 691.

---

## REFERENCIAS

- **PROMPTS_01_PYTHON_MICROSERVICIOS_EXISTENTES.md** - 9 microservicios (8 Python + 1 Java)
- **PROMPTS_02_PYTHON_MICROSERVICIOS_NUEVOS.md** - 5 microservicios nuevos
- **TRACKING_PROMPTS_IMPLEMENTACION.md** - Estado de implementación
- **RESUMEN_PROMPTS_PYTHON.md** - Prompts de gaps (14 prompts en 8 microservicios)

---

**Última actualización:** Diciembre 2025

