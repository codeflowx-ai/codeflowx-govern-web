# REFERENCIAS A MICROSERVICIOS PYTHON EN DOCUMENTOS DE INCIDENCIAS

**Fecha:** Diciembre 2025  
**Objetivo:** Consolidar todas las referencias a microservicios Python en documentos de incidencias

---

## RESUMEN EJECUTIVO

**Total Referencias Encontradas:** 50+ referencias  
**Microservicios Mencionados:** 10 microservicios Python  
**Documentos de Incidencias Revisados:** 12 documentos

---

## PARTE 1: INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md

### Referencias Directas:

1. **INC-004** (Línea 188):
   ```java
   // 4. Validar con NLP (opcional - usar leka-llm-evaluation)
   // JustificationQualityResult quality = llmEvaluationService.evaluateJustificationQuality(justification);
   ```
   - **Microservicio:** `leka-llm-evaluation` (8002)
   - **Uso:** Validación de calidad de justificación con NLP

2. **INC-007** (Línea 328):
   ```
   ✅ **Microservicio Python `leka-fria-generator`:**
   ```
   - **Microservicio:** `leka-fria-generator` (8012)
   - **Uso:** Generación de FRIA y validación cruzada

3. **INC-011** (Línea 663):
   ```java
   // Verificar que leka-bias-detection está configurado y activo
   ```
   - **Microservicio:** `leka-bias-detection-service` (8001)
   - **Uso:** Verificación de configuración

---

## PARTE 2: INCIDENCIAS_005_EVALUACION_RAG.md

### Referencias Directas:

1. **INC-005-002** (Líneas 30, 56-71):
   - **Microservicio:** `leka-llm-evaluation` (Puerto 8002)
   - **Archivos mencionados:**
     - `services/selfcheck_service.py`
     - `services/factscore_service.py`
     - `services/entailment_service.py`
     - `services/hallucination_aggregator.py`
     - `main.py`
     - `models/response_models.py`
   - **Endpoints:**
     - `POST /api/hallucination-detection/detect`
     - `POST /api/hallucination-detection/validate-human`
   - **Swagger UI:** `http://localhost:8002/api/docs`
   - **Referencia:** `IMPLEMENTACION_INC-005-002_INC-010-012.md` en `leka-llm-evaluation`

2. **INC-005-003** (Líneas 86, 116-131):
   - **Microservicio:** `leka-adversarial-robustness` (Puerto 8007)
   - **Archivos mencionados:**
     - `services/policy_scoring_service.py`
   - **Endpoints:**
     - `POST /api/policy-validation/score`
   - **Swagger UI:** `http://localhost:8007/docs`
   - **Referencia:** `MEJORAS_INCIDENCIAS_005.md` sección INC-005-003

3. **INC-005-010** (Línea 244):
   ```
   - Migrado de `RestTemplate` a `RAGEvaluationClient` para integración con microservicio RAG (puerto 8004)
   ```
   - **Microservicio:** `leka-rag-evaluation` (8004)
   - **Uso:** Integración con cliente Java

4. **Resumen de Estado** (Líneas 348-349):
   - INC-005-002: ✅ **RESUELTO** - Servicios avanzados en `leka-llm-evaluation`
   - INC-005-003: ✅ **RESUELTO** - PolicyScoringService en `leka-adversarial-robustness`

5. **Integración** (Línea 399):
   ```
   - **Ubicación:** `leka-rag-evaluation/postman/RAG_Evaluation_Service.postman_collection.json`
   ```
   - **Microservicio:** `leka-rag-evaluation` (8004)

---

## PARTE 3: INCIDENCIAS_RECOMENDACIONES_EVALUACION_DATASETS.md

### Referencias Directas:

1. **INC-001-DS** (Líneas 50-51):
   ```
   - Changelog: `leka-bias-detection-service/CHANGELOG_INCIDENCIAS.md`
   - Implementación: `leka-bias-detection-service/utils/file_validation.py`
   ```
   - **Microservicio:** `leka-bias-detection-service` (8001)

2. **INC-002-DS** (Líneas 94-95):
   ```
   - Changelog: `leka-bias-detection-service/CHANGELOG_INCIDENCIAS.md`
   - Implementación: `leka-bias-detection-service/services/streaming_data_quality_service.py`
   ```
   - **Microservicio:** `leka-bias-detection-service` (8001)

3. **INC-004-DS** (Líneas 173-174):
   ```
   - Changelog: `leka-bias-detection-service/CHANGELOG_INCIDENCIAS.md`
   - Implementación: `leka-bias-detection-service/utils/adaptive_timeout.py`, `utils/progress_tracker.py`
   ```
   - **Microservicio:** `leka-bias-detection-service` (8001)

4. **INC-011** (Línea 412):
   ```
   ✅ Servicio `RecommendationGenerator` implementado en `leka-model-wrapper`
   ```
   - **Microservicio:** `leka-model-wrapper` (8006)

5. **INC-012** (Líneas 443, 469):
   ```
   ✅ Conectores DVC y Git LFS implementados en `leka-bias-detection-service`:
   ```
   - **Microservicio:** `leka-bias-detection-service` (8001)
   ```
   - 🔄 Integración en `leka-server-model-loader` para datasets de entrenamiento
   ```
   - **Microservicio:** `leka-server-model-loader` (no especificado en compliance)

---

## PARTE 4: AUDITORIA_EVALUACION_DATASETS.md

### Referencias Directas:

1. **Microservicio Principal** (Línea 13):
   ```
   **Microservicio Principal:** `leka-bias-detection-service` (Puerto 8001)
   ```

2. **Límites de Archivo** (Líneas 17-18):
   ```
   | **Archivo CSV** | **1024 MB (1 GB)** | ✅ SÍ | `leka-bias-detection-service/utils/file_validation.py` ⭐ ACTUALIZADO |
   | **Timeout** | **Adaptativo** | ✅ SÍ | `leka-bias-detection-service/utils/adaptive_timeout.py` ⭐ ACTUALIZADO |
   ```

3. **Archivos Específicos** (Líneas 24, 89, 124, 210):
   - `leka-bias-detection-service/utils/file_validation.py`
   - `leka-bias-detection-service` (múltiples referencias)
   - `leka-llm-evaluation` (Línea 210)

4. **Microservicios Involucrados** (Líneas 276-280):
   ```
   | Microservicio | Puerto | Responsabilidad |
   | **leka-bias-detection-service** | 8001 | Data quality, bias, drift, duplicates, outliers |
   | **leka-llm-evaluation** | 8002 | Toxicidad, safety, PII leakage |
   | **leka-prompt-governance** | 8003 | PII detection, prompt safety |
   | **leka-rag-evaluation** | 8004 | RAG quality (si aplica) |
   ```

5. **Service Tasks BPMN** (Líneas 302-308):
   - Quality Evaluation → `leka-bias-detection-service`
   - Bias Detection → `leka-bias-detection-service`
   - Drift Detection → `leka-bias-detection-service`
   - Label Leakage Detection → `leka-bias-detection-service`

6. **Adversarial Robustness** (Línea 799):
   ```
   | **15.3** - Resiliencia ante ataques | ✅ Tests adversariales (si aplica) | `leka-adversarial-robustness` |
   ```
   - **Microservicio:** `leka-adversarial-robustness` (8007)

7. **Pipeline** (Líneas 984, 992, 1027, 1043, 1051, 1059, 1062, 1067):
   ```
   ✅ **Pipeline:** Microservicios Python + Workflow BPMN + Delegates Java
   - Microservicios Python: `bias-detection-service/`
   - `POST /api/dataset-quality/generate-recommendations` en `leka-model-wrapper` (puerto 8006)
   - Ver detalles en: `leka-bias-detection-service/CHANGELOG_INCIDENCIAS.md`
   - Ver detalles en: `leka-model-wrapper/AUDITORIA_INC-011.md`
   ```

---

## PARTE 5: AUDITORIA_010_POST_MARKET_MONITORING.md

### Referencias Directas:

1. **Microservicios Evaluados** (Líneas 44-45):
   ```
   - `leka-bias-detection-service` (Detección de sesgo)
   - `leka-adversarial-service` (Evaluación adversarial)
   ```
   - **Microservicios:** `leka-bias-detection-service` (8001), `leka-adversarial-service` (posiblemente 8007)

2. **Integración Drift** (Línea 87):
   ```
   - Integrar con `leka-bias-detection-service` endpoint `/api/drift/detect`
   ```
   - **Microservicio:** `leka-bias-detection-service` (8001)

3. **Análisis de Sentimiento** (Líneas 141, 160, 260-261, 593):
   ```
   **Microservicio:** `leka-llm-evaluation` (Puerto 8002)
   - Servicio disponible en `leka-llm-evaluation`: `POST /api/sentiment/analyze`
   ```
   - **Microservicio:** `leka-llm-evaluation` (8002)
   - **Endpoint:** `POST /api/sentiment/analyze`

4. **Integraciones Pendientes** (Líneas 518, 548, 582):
   ```
   ⚠️ **PARCIAL:** Integraciones con microservicios pendientes
   ⚠️ **PARCIAL:** Integraciones con microservicios (mock/TODO)
   4. Completar integraciones reales con microservicios (eliminar mocks)
   ```

---

## PARTE 6: AUDITORIA_005_EVALUACION_RAG.md

### Referencias Directas:

1. **Actualización** (Línea 5):
   ```
   **Última Actualización:** 2025-01-XX (INC-005-002 mejorado con servicios avanzados en leka-llm-evaluation)
   ```

2. **Modelos Especializados** (Líneas 216, 223, 238):
   ```
   - **Modelos Especializados Avanzados:** Implementación completa en microservicio `leka-llm-evaluation`:
   - **Endpoints API REST:** `POST /api/hallucination-detection/detect` y `/validate-human` disponibles en puerto 8002
   - ✅ Validación humana sistemática: Implementada en `leka-llm-evaluation`
   ```
   - **Microservicio:** `leka-llm-evaluation` (8002)

3. **Plan de Resolución** (Línea 487):
   ```
   **Plan detallado:** Ver `leka-rag-evaluation/PLAN_RESOLUCION_3_PORCIENTO.md`
   ```
   - **Microservicio:** `leka-rag-evaluation` (8004)

4. **Cliente Java** (Líneas 502, 519, 535, 552, 554, 559):
   ```
   Se ha implementado un cliente Java completo (`RAGEvaluationClient`) que proporciona acceso a todos los 24 endpoints del microservicio RAG Evaluation:
   - **Ubicación:** `leka-rag-evaluation/postman/RAG_Evaluation_Service.postman_collection.json`
   - ✅ **Todos los 24 endpoints** del microservicio incluidos
   ```
   - **Microservicio:** `leka-rag-evaluation` (8004)

---

## PARTE 7: AUDITORIA_FRIA_EVALUACIONES_TECNICAS.md

### Referencias Directas:

1. **Auditoría de Sesgos** (Línea 181):
   ```json
   "description": "Auditoría periódica de sesgos con leka-bias-detection-service",
   ```
   - **Microservicio:** `leka-bias-detection-service` (8001)

---

## PARTE 8: INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md

### Referencias Directas:

1. **Integración Drift** (Línea 219):
   ```
   1. **Integrar con `leka-bias-detection-service`:**
   ```

2. **Análisis de Sentimiento** (Líneas 432, 459-470):
   ```
   **Microservicio:** `leka-llm-evaluation` (Puerto 8002)
   - Archivos en leka-llm-evaluation:
   - Microservicio stateless listo para consumo desde backend Java
   - Endpoints documentados en Swagger UI: `http://localhost:8002/api/docs`
   - Ver `IMPLEMENTACION_INC-005-002_INC-010-012.md` en `leka-llm-evaluation` para detalles completos
   ```
   - **Microservicio:** `leka-llm-evaluation` (8002)

---

## RESUMEN POR MICROSERVICIO

### leka-bias-detection-service (8001)
**Referencias:** 20+  
**Documentos:** 
- INCIDENCIAS_RECOMENDACIONES_EVALUACION_DATASETS.md
- AUDITORIA_EVALUACION_DATASETS.md
- AUDITORIA_010_POST_MARKET_MONITORING.md
- AUDITORIA_FRIA_EVALUACIONES_TECNICAS.md
- INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md

**Incidencias:**
- INC-001-DS, INC-002-DS, INC-004-DS, INC-011, INC-012

---

### leka-llm-evaluation (8002)
**Referencias:** 10+  
**Documentos:**
- INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md
- INCIDENCIAS_005_EVALUACION_RAG.md
- AUDITORIA_EVALUACION_DATASETS.md
- AUDITORIA_005_EVALUACION_RAG.md
- AUDITORIA_010_POST_MARKET_MONITORING.md
- INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md

**Incidencias:**
- INC-004, INC-005-002, INC-010-012

---

### leka-rag-evaluation (8004)
**Referencias:** 5+  
**Documentos:**
- INCIDENCIAS_005_EVALUACION_RAG.md
- AUDITORIA_EVALUACION_DATASETS.md
- AUDITORIA_005_EVALUACION_RAG.md

**Incidencias:**
- INC-005-010

---

### leka-adversarial-robustness (8007)
**Referencias:** 3+  
**Documentos:**
- INCIDENCIAS_005_EVALUACION_RAG.md
- AUDITORIA_EVALUACION_DATASETS.md
- AUDITORIA_010_POST_MARKET_MONITORING.md

**Incidencias:**
- INC-005-003

---

### leka-fria-generator (8012)
**Referencias:** 1  
**Documentos:**
- INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md

**Incidencias:**
- INC-007

---

### leka-model-wrapper (8006)
**Referencias:** 3+  
**Documentos:**
- INCIDENCIAS_RECOMENDACIONES_EVALUACION_DATASETS.md
- AUDITORIA_EVALUACION_DATASETS.md

**Incidencias:**
- INC-011

---

### leka-prompt-governance (8003)
**Referencias:** 1  
**Documentos:**
- AUDITORIA_EVALUACION_DATASETS.md

**Incidencias:**
- Mencionado en tabla de microservicios involucrados

---

## CONCLUSIÓN

### Microservicios más referenciados:
1. **leka-bias-detection-service** (8001) - 20+ referencias
2. **leka-llm-evaluation** (8002) - 10+ referencias
3. **leka-rag-evaluation** (8004) - 5+ referencias
4. **leka-adversarial-robustness** (8007) - 3+ referencias
5. **leka-model-wrapper** (8006) - 3+ referencias

### Microservicios menos referenciados:
- **leka-fria-generator** (8012) - 1 referencia
- **leka-prompt-governance** (8003) - 1 referencia

### Microservicios sin referencias en incidencias:
- **leka-agent-monitoring** (8005)
- **leka-technical-documentation-generator** (8008)
- **leka-conformity-assessment** (8009)
- **leka-eu-declaration-generator** (8010)
- **leka-copyright-compliance** (8013)

---

**Última actualización:** Diciembre 2025

