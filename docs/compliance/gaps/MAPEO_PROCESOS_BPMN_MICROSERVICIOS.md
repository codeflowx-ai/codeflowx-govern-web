# MAPEO: PROCESOS BPMN → MICROSERVICIOS PYTHON

**Fecha:** Diciembre 2025  
**Objetivo:** Identificar qué procesos BPMN llaman directamente a los microservicios Python según auditoría e incidencias

---

## 📊 RESUMEN EJECUTIVO

**Total Procesos BPMN Identificados:** 20 procesos  
**Procesos que llaman Microservicios Python:** 18 procesos (90%)  
**Procesos sin llamadas directas a Python:** 2 procesos (10%)

---

## PARTE 1: PROCESOS BPMN DEFINIDOS EN AUDITORÍA

### 1.1 Procesos de Catalogación y Clasificación

#### **high_risk_compliance_workflow** (BPMN)
**Documento:** `AUDITORIA_CATALOGACION_CLASIFICACION_IA.md`  
**Ubicación:** Proceso de clasificación de alto riesgo

**Microservicios Python Llamados:**
- ❌ **Ninguno directamente** (proceso Java/ViewModel)

**Nota:** Este proceso es principalmente Java/ViewModel, pero puede generar tareas que luego llaman a microservicios Python para validaciones técnicas.

---

### 1.2 Procesos de FRIA y Evaluaciones Técnicas

#### **fria-process.bpmn20.xml** (BPMN)
**Documento:** `AUDITORIA_FRIA_EVALUACIONES_TECNICAS.md`  
**Ubicación:** `docs/compliance/bpmn/compliance/fria/`  
**Referencia:** `docs/compliance/bpmn/compliance/fria/technical.md`

**Microservicios Python Llamados:**
1. ✅ **leka-fria-generator** (8012)
   - `POST /api/fria/generate-assessment` → Genera documento FRIA
   - `POST /api/fria/cross-validate` → Valida consistencia FRIA vs métricas técnicas

2. ✅ **leka-bias-detection-service** (8001)
   - Obtiene métricas de sesgo y calidad de datos
   - Integrado en `CrossValidateFriaDelegate` (INC-007)

3. ✅ **leka-llm-evaluation** (8002)
   - Obtiene métricas de performance del modelo
   - Integrado en `CrossValidateFriaDelegate` (INC-007)

4. ✅ **leka-adversarial-robustness** (8007)
   - Obtiene métricas de robustez adversarial
   - Integrado en `CrossValidateFriaDelegate` (INC-007)

**Delegates Java:**
- `GenerateFriaDocumentDelegate` → Llama `leka-fria-generator`
- `CrossValidateFriaDelegate` → Llama múltiples microservicios (INC-007)
- `AnalyzeFundamentalRightsDelegate` → Puede usar `leka-llm-evaluation`

**Referencia Incidencias:** INC-007 (Validación cruzada FRIA)

---

### 1.3 Procesos de Evaluación de Datasets

#### **dataset-quality-v1.bpmn** (BPMN)
**Documento:** `AUDITORIA_EVALUACION_DATASETS.md`, `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md`  
**Ubicación:** `docs/compliance/bpmn/aios/dataset-quality/`

**Microservicios Python Llamados:**
1. ✅ **leka-bias-detection-service** (8001)
   - `POST /api/tabular/analyze-bias` → Análisis de sesgo
   - `POST /api/tabular/detect-drift` → Detección de drift
   - `POST /api/tabular/evaluate-privacy` → Evaluación de privacidad
   - `POST /api/tabular/evaluate-data-quality` → Calidad de datos

2. ✅ **leka-rag-evaluation** (8004)
   - `POST /api/rag/evaluate-document-quality` → Si aplica para datasets RAG

3. ✅ **leka-model-wrapper** (8006)
   - `POST /api/dataset-quality/generate-recommendations` → Recomendaciones automáticas (INC-011)

**Delegates Java:**
- `DatasetQualityEvaluationDelegate` → Llama `leka-bias-detection-service`
- `GenerateRecommendationsDelegate` → Llama `leka-model-wrapper` (INC-011)

**Referencia Incidencias:** INC-011 (Recomendaciones automáticas)

---

### 1.4 Procesos de Evaluación RAG

#### **rag-evaluation-v1.bpmn** (BPMN)
**Documento:** `INCIDENCIAS_005_EVALUACION_RAG.md`  
**Ubicación:** `codeflowx.govern.workflow.lib/src/main/resources/processes/aios/rag-evaluation-v1.bpmn`

**Microservicios Python Llamados:**
1. ✅ **leka-rag-evaluation** (8004)
   - `POST /api/rag/evaluate-retrieval` → Evaluación de recuperación
   - `POST /api/rag/evaluate-answer` → Evaluación de respuesta
   - `POST /api/rag/evaluate-context-relevance` → Relevancia de contexto
   - `POST /api/rag/evaluate-full-pipeline` → Pipeline completo
   - `POST /api/rag/evaluate-document-quality` → Calidad de documentos
   - `POST /api/rag/evaluate-conversation-context` → Contexto multi-turn
   - `POST /api/rag/evaluate-citations` → Precisión de citas
   - `POST /api/rag/evaluate-index-quality` → Calidad de índice

**Delegates Java:**
- `RAGEvaluationDelegate` → Llama `leka-rag-evaluation` (todos los endpoints)

**Referencia Incidencias:** INC-005-002, INC-005-003, INC-005-004, INC-005-006, INC-005-007, INC-005-010

---

### 1.5 Procesos de Post-Market Monitoring

#### **compliance-monitoring-v1.bpmn** (BPMN)
**Documento:** `AUDITORIA_010_POST_MARKET_MONITORING.md`  
**Ubicación:** `docs/compliance/bpmn/compliance/compliance-monitoring/`

**Microservicios Python Llamados:**
1. ✅ **leka-llm-evaluation** (8002)
   - `POST /api/llm/evaluate-toxicity` → Verificación de toxicidad
   - `POST /api/llm/evaluate-bias-text` → Verificación de sesgo

2. ✅ **leka-prompt-governance** (8003)
   - `POST /api/prompt/evaluate-safety` → Verificación de seguridad

3. ✅ **leka-bias-detection-service** (8001)
   - `POST /api/tabular/analyze-bias` → Detección de sesgo

**Delegates Java:**
- `CheckPostMarketMetricsDelegate` → Llama múltiples microservicios
- `ExecuteComplianceCheckDelegate` → Llama `leka-llm-evaluation` y `leka-prompt-governance`

---

#### **incident-reporting-process.bpmn** (BPMN)
**Documento:** `AUDITORIA_010_POST_MARKET_MONITORING.md`  
**Ubicación:** Proceso de reporte de incidentes

**Microservicios Python Llamados:**
1. ✅ **leka-agent-monitoring** (8005)
   - `POST /api/agent/analyze-execution` → Análisis de ejecución
   - `POST /api/agent/evaluate-reliability` → Evaluación de confiabilidad

2. ✅ **leka-llm-evaluation** (8002)
   - `POST /api/llm/evaluate-hallucination` → Detección de alucinaciones (INC-005-002)

**Delegates Java:**
- `ExecuteRCADelegate` → Llama microservicios para análisis automático de RCA

**Referencia Incidencias:** INC-005-002 (Detección de alucinaciones)

---

## PARTE 2: PROCESOS BPMN DEL INVENTARIO GENERAL

### 2.1 Módulo Governance (10 procesos)

#### **1. compliance-monitoring-v1.bpmn**
**Microservicios Python:**
- ✅ **leka-llm-evaluation** (8002)
  - `/api/llm/evaluate-toxicity`
  - `/api/llm/evaluate-bias-text`
  - `/api/llm/evaluate-quality`
- ✅ **leka-prompt-governance** (8003)
  - `/api/prompt/evaluate-safety`
- ✅ **leka-bias-detection-service** (8001)
  - `/api/tabular/analyze-bias`

#### **2. risk-assessment-v1.bpmn**
**Microservicios Python:**
- ✅ **leka-model-wrapper** (8006)
  - `/api/models/benchmark`
- ✅ **leka-prompt-governance** (8003)
  - `/api/prompt/evaluate-safety`
- ✅ **leka-agent-monitoring** (8005)
  - `/api/agent/analyze-safety-violations`

#### **3. ethics-review-v1.bpmn**
**Microservicios Python:**
- ✅ **leka-llm-evaluation** (8002)
  - `/api/llm/evaluate-bias-text`
  - `/api/llm/evaluate-toxicity`
- ✅ **leka-prompt-governance** (8003)
  - `/api/prompt/evaluate-safety`
  - `/api/prompt/detect-pii-leakage`
- ✅ **leka-bias-detection-service** (8001)
  - `/api/tabular/analyze-bias`

#### **4. bias-detection-v1.bpmn**
**Microservicios Python:**
- ✅ **leka-bias-detection-service** (8001)
  - `/api/tabular/analyze-bias`
  - `/api/tabular/detect-drift`
  - `/api/tabular/explain-predictions`
  - `/api/tabular/evaluate-privacy`
  - `/api/tabular/test-robustness`
  - `/api/tabular/evaluate-data-quality`
  - `/api/tabular/detect-label-leakage`
  - `/api/tabular/benchmark-fairness`
- ✅ **leka-llm-evaluation** (8002)
  - `/api/llm/evaluate-bias-text`

#### **5. dataset-quality-v1.bpmn**
**Microservicios Python:**
- ✅ **leka-bias-detection-service** (8001)
  - `/api/tabular/evaluate-data-quality`
  - `/api/tabular/analyze-bias`
  - `/api/tabular/detect-drift`
- ✅ **leka-model-wrapper** (8006)
  - `/api/dataset-quality/generate-recommendations` (INC-011)

#### **6. performance-degradation-v1.bpmn**
**Microservicios Python:**
- ✅ **leka-agent-monitoring** (8005)
  - `/api/agent/analyze-execution`
  - `/api/agent/evaluate-reliability`
- ✅ **leka-model-wrapper** (8006)
  - `/api/models/benchmark`

#### **7. alert-response-v1.bpmn**
**Microservicios Python:**
- ✅ **leka-agent-monitoring** (8005)
  - `/api/agent/analyze-safety-violations`
  - `/api/agent/detect-loops`

#### **8. incident-response-rca-v1.bpmn**
**Microservicios Python:**
- ✅ **leka-agent-monitoring** (8005)
  - `/api/agent/analyze-execution`
  - `/api/agent/evaluate-reliability`
- ✅ **leka-llm-evaluation** (8002)
  - `/api/llm/evaluate-hallucination` (INC-005-002)

**Nota:** También puede usar `leka-ai-interpreter` (8011) para análisis de causa raíz, pero este no está en los prompts de compliance.

#### **9. deployment-automation-v1.bpmn**
**Microservicios Python:**
- ✅ **leka-model-wrapper** (8006)
  - `/api/models/benchmark`
  - `/api/models/estimate-cost`
- ✅ **leka-agent-monitoring** (8005)
  - `/api/agent/benchmark-agent-performance`

#### **10. model-retraining-orchestration-v1.bpmn**
**Microservicios Python:**
- ✅ **leka-model-wrapper** (8006)
  - `/api/models/benchmark`
  - `/api/models/ab-test`
  - `/api/models/smart-route`

---

### 2.2 Módulo Evaluation (3 procesos)

#### **11. llm-evaluation-v1.bpmn**
**Microservicios Python:**
- ✅ **leka-llm-evaluation** (8002)
  - `/api/llm/evaluate-hallucination`
  - `/api/llm/evaluate-toxicity`
  - `/api/llm/evaluate-bias-text`
  - `/api/llm/evaluate-quality`
  - `/api/llm/evaluate-prompt-injection`
  - `/api/llm/evaluate-instruction-following`
  - `/api/llm/evaluate-consistency`
  - `/api/llm/evaluate-factual-grounding`
  - `/api/llm/evaluate-cost-efficiency`
  - `/api/llm/benchmark-evaluations`
  - `/api/llm/ab-test-prompts`

#### **12. model-evaluation-v1.bpmn**
**Microservicios Python:**
- ✅ **leka-bias-detection-service** (8001)
  - `/api/tabular/analyze-bias`
  - `/api/tabular/detect-drift`
  - `/api/tabular/explain-predictions`
  - `/api/tabular/evaluate-privacy`
  - `/api/tabular/test-robustness`
- ✅ **leka-model-wrapper** (8006)
  - `/api/models/benchmark`

#### **13. rag-evaluation-v1.bpmn**
**Microservicios Python:**
- ✅ **leka-rag-evaluation** (8004)
  - Todos los endpoints (ver sección 1.4)

---

### 2.3 Módulo Prompts (1 proceso)

#### **14. prompt-approval-v1.bpmn**
**Microservicios Python:**
- ✅ **leka-prompt-governance** (8003)
  - `/api/prompt/evaluate-safety`
  - `/api/prompt/evaluate-effectiveness`
  - `/api/prompt/detect-pii-leakage`
  - `/api/prompt/validate-template`
  - `/api/prompt/optimize-context-window`
  - `/api/prompt/evaluate-few-shot-examples`
  - `/api/prompt/validate-output-format`
  - `/api/prompt/analyze-cost`

**Referencia Incidencias:** INC-001 (Límite tamaño archivo), INC-002 (Streaming datasets), INC-004 (Timeout adaptativo)

---

### 2.4 Módulo Agents (2 procesos)

#### **15. agent-approval-v1.bpmn**
**Microservicios Python:**
- ✅ **leka-agent-monitoring** (8005)
  - `/api/agent/analyze-execution`
  - `/api/agent/evaluate-reliability`
  - `/api/agent/analyze-cost`
  - `/api/agent/detect-loops`
  - `/api/agent/analyze-multi-agent-orchestration`
  - `/api/agent/evaluate-tool-usage`
  - `/api/agent/analyze-safety-violations`
  - `/api/agent/benchmark-agent-performance`
- ✅ **leka-prompt-governance** (8003)
  - `/api/prompt/evaluate-safety`
- ✅ **leka-llm-evaluation** (8002)
  - `/api/llm/evaluate-toxicity`
  - `/api/llm/evaluate-bias-text`

**Referencia Incidencias:** INC-005-009 (Mejora continua)

#### **16. agent-approval-v2.bpmn**
**Microservicios Python:**
- ✅ **leka-agent-monitoring** (8005)
  - Mismos endpoints que agent-approval-v1.bpmn

---

## PARTE 3: RESUMEN POR MICROSERVICIO

### Microservicios Python Llamados por Procesos BPMN

#### **leka-bias-detection-service** (8001)
**Procesos que lo llaman:**
1. compliance-monitoring-v1.bpmn
2. ethics-review-v1.bpmn
3. bias-detection-v1.bpmn
4. dataset-quality-v1.bpmn
5. model-evaluation-v1.bpmn
6. fria-process.bpmn20.xml (CrossValidateFriaDelegate)

**Total:** 6 procesos

---

#### **leka-llm-evaluation** (8002)
**Procesos que lo llaman:**
1. compliance-monitoring-v1.bpmn
2. ethics-review-v1.bpmn
3. bias-detection-v1.bpmn
4. incident-response-rca-v1.bpmn
5. llm-evaluation-v1.bpmn
6. agent-approval-v1.bpmn
7. agent-approval-v2.bpmn
8. fria-process.bpmn20.xml (CrossValidateFriaDelegate)

**Total:** 8 procesos

---

#### **leka-prompt-governance** (8003)
**Procesos que lo llaman:**
1. compliance-monitoring-v1.bpmn
2. risk-assessment-v1.bpmn
3. ethics-review-v1.bpmn
4. prompt-approval-v1.bpmn
5. agent-approval-v1.bpmn
6. agent-approval-v2.bpmn

**Total:** 6 procesos

---

#### **leka-rag-evaluation** (8004)
**Procesos que lo llaman:**
1. rag-evaluation-v1.bpmn
2. dataset-quality-v1.bpmn (si aplica)

**Total:** 2 procesos

---

#### **leka-agent-monitoring** (8005)
**Procesos que lo llaman:**
1. risk-assessment-v1.bpmn
2. performance-degradation-v1.bpmn
3. alert-response-v1.bpmn
4. incident-response-rca-v1.bpmn
5. deployment-automation-v1.bpmn
6. agent-approval-v1.bpmn
7. agent-approval-v2.bpmn

**Total:** 7 procesos

---

#### **leka-model-wrapper** (8006)
**Procesos que lo llaman:**
1. risk-assessment-v1.bpmn
2. dataset-quality-v1.bpmn (INC-011)
3. performance-degradation-v1.bpmn
4. deployment-automation-v1.bpmn
5. model-retraining-orchestration-v1.bpmn
6. model-evaluation-v1.bpmn

**Total:** 6 procesos

---

#### **leka-adversarial-robustness** (8007)
**Procesos que lo llaman:**
1. fria-process.bpmn20.xml (CrossValidateFriaDelegate)

**Total:** 1 proceso

**Nota:** Este microservicio es nuevo y solo se integra en el proceso FRIA para validación cruzada (INC-007).

---

#### **leka-fria-generator** (8012)
**Procesos que lo llaman:**
1. fria-process.bpmn20.xml (GenerateFriaDocumentDelegate, CrossValidateFriaDelegate)

**Total:** 1 proceso

**Nota:** Este microservicio es específico para el proceso FRIA.

---

#### **leka-technical-documentation-generator** (8008)
**Procesos que lo llaman:**
- ❌ **Ningún proceso BPMN identificado**

**Nota:** Este microservicio puede ser llamado desde ViewModels Java directamente, no desde procesos BPMN.

---

#### **leka-conformity-assessment** (8009)
**Procesos que lo llaman:**
- ❌ **Ningún proceso BPMN identificado**

**Nota:** Este microservicio puede ser llamado desde ViewModels Java directamente, no desde procesos BPMN.

---

#### **leka-eu-declaration-generator** (8010)
**Procesos que lo llaman:**
- ❌ **Ningún proceso BPMN identificado**

**Nota:** Este microservicio puede ser llamado desde ViewModels Java directamente, no desde procesos BPMN.

---

#### **leka-copyright-compliance** (8013)
**Procesos que lo llaman:**
- ❌ **Ningún proceso BPMN identificado**

**Nota:** Este microservicio puede ser llamado desde ViewModels Java directamente, no desde procesos BPMN.

---

## PARTE 4: RESUMEN ESTADÍSTICO

### Procesos BPMN por Microservicio

| Microservicio | Puerto | Procesos BPMN | % Cobertura |
|---------------|--------|---------------|-------------|
| leka-llm-evaluation | 8002 | 8 | 40% |
| leka-agent-monitoring | 8005 | 7 | 35% |
| leka-bias-detection-service | 8001 | 6 | 30% |
| leka-prompt-governance | 8003 | 6 | 30% |
| leka-model-wrapper | 8006 | 6 | 30% |
| leka-rag-evaluation | 8004 | 2 | 10% |
| leka-fria-generator | 8012 | 1 | 5% |
| leka-adversarial-robustness | 8007 | 1 | 5% |
| leka-technical-documentation-generator | 8008 | 0 | 0% |
| leka-conformity-assessment | 8009 | 0 | 0% |
| leka-eu-declaration-generator | 8010 | 0 | 0% |
| leka-copyright-compliance | 8013 | 0 | 0% |

**Total Procesos BPMN:** 20  
**Procesos con llamadas a Python:** 18 (90%)  
**Procesos sin llamadas a Python:** 2 (10%)

---

## CONCLUSIÓN

### Microservicios Críticos para BPMN:
1. **leka-llm-evaluation** (8002) - 8 procesos
2. **leka-agent-monitoring** (8005) - 7 procesos
3. **leka-bias-detection-service** (8001) - 6 procesos
4. **leka-prompt-governance** (8003) - 6 procesos
5. **leka-model-wrapper** (8006) - 6 procesos

### Microservicios Especializados:
- **leka-rag-evaluation** (8004) - Solo para evaluación RAG
- **leka-fria-generator** (8012) - Solo para proceso FRIA
- **leka-adversarial-robustness** (8007) - Solo para validación FRIA

### Microservicios sin Integración BPMN:
- **leka-technical-documentation-generator** (8008)
- **leka-conformity-assessment** (8009)
- **leka-eu-declaration-generator** (8010)
- **leka-copyright-compliance** (8013)

**Nota:** Estos últimos 4 microservicios pueden ser llamados directamente desde ViewModels Java o servicios backend, no necesariamente desde procesos BPMN.

---

**Última actualización:** Diciembre 2025

