# MAPEO: VIEWMODELS Y SERVICES JAVA → MICROSERVICIOS PYTHON (PARTE 2)

**Fecha:** Diciembre 2025  
**Continuación de:** `MAPEO_VIEWMODELS_SERVICES_MICROSERVICIOS_PARTE1.md`

---

## PARTE 4: CLIENTES JAVA DISPONIBLES (DETALLE COMPLETO)

### 4.1 AIGovernanceClient (Factoría de Clientes)

**Ubicación:** `codeflowx.govern.nocode.client/src/main/java/com/codeflowx/governance/client/AIGovernanceClient.java`

**Gateway URL:**
- K8s: `http://api-leka-govern:8000`
- Local: `http://localhost:8000`

**Clientes proporcionados:**

1. ✅ **llmEvaluation()** → `LLMEvaluationClient`
   - **Microservicio:** leka-llm-evaluation (8002)
   - **Endpoints Base:** `/api/llm/*`

2. ✅ **promptGovernance()** → `PromptGovernanceClient`
   - **Microservicio:** leka-prompt-governance (8003)
   - **Endpoints Base:** `/api/prompt/*`

3. ✅ **ragEvaluation()** → `RAGEvaluationClient`
   - **Microservicio:** leka-rag-evaluation (8004)
   - **Endpoints Base:** `/api/rag/*`

4. ✅ **agentMonitoring()** → `AgentMonitoringClient`
   - **Microservicio:** leka-agent-monitoring (8005)
   - **Endpoints Base:** `/api/agent/*`

5. ✅ **modelWrapper()** → `ModelWrapperClient`
   - **Microservicio:** leka-model-wrapper (8006)
   - **Endpoints Base:** `/api/models/*`

6. ✅ **aiInterpreter()** → `AIInterpreterClient`
   - **Microservicio:** leka-ai-interpreter (8011)
   - **Endpoints Base:** `/api/interpret/*`

7. ✅ **friaGenerator()** → `FRIAGeneratorClient`
   - **Microservicio:** leka-fria-generator (8012)
   - **Endpoints Base:** `/api/fria/*`

8. ✅ **biasDetection()** → `BiasDetectionClient`
   - **Microservicio:** leka-bias-detection-service (8001)
   - **Endpoints Base:** `/api/**`

9. ✅ **adversarialRobustness()** → `AdversarialRobustnessClient`
   - **Microservicio:** leka-adversarial-robustness (8007)
   - **Endpoints Base:** `/api/adversarial/**` y `/api/policy-validation/**`

10. ✅ **conformityAssessment()** → `ConformityAssessmentClient`
    - **Microservicio:** leka-conformity-assessment (8009)
    - **Endpoints Base:** `/api/conformity/**`

11. ✅ **euDeclarationGenerator()** → `EUDeclarationGeneratorClient`
    - **Microservicio:** leka-eu-declaration-generator (8010)
    - **Endpoints Base:** `/api/declaration/**`

12. ✅ **copyrightCompliance()** → `CopyrightComplianceClient`
    - **Microservicio:** leka-copyright-compliance (8013)
    - **Endpoints Base:** `/api/copyright/**`

**Nota:** AIGovernanceClient proporciona acceso a TODOS los microservicios Python de compliance a través de clientes especializados.

---

## PARTE 5: VIEWMODELS QUE DEBERÍAN USAR MICROSERVICIOS

### 5.1 ViewModels de Prompts

#### **PromptsDetailViewModel.java**
**Ubicación:** `src/main/java/com/codeflowx/govern/viewmodel/prompts/PromptsDetailViewModel.java`

**Microservicios que debería llamar:**
1. ⚠️ **leka-prompt-governance** (8003)
   - **Uso potencial:** Validación de seguridad, efectividad, detección PII
   - **Métodos relevantes:** `validatePrompt()`, `calculateEffectiveness()`
   - **Estado:** ❌ No usa microservicio actualmente

**Referencia Incidencias:**
- INC-001: Límite tamaño archivo (✅ Implementado en microservicio)
- INC-002: Streaming datasets (✅ Implementado en microservicio)
- INC-004: Timeout adaptativo (✅ Implementado en microservicio)

**Recomendación:** Integrar `AIGovernanceClient.promptGovernance()` para validaciones.

---

#### **PromptApprovalWorkflowViewModel.java**
**Ubicación:** `src/main/java/com/codeflowx/govern/viewmodel/prompts/PromptApprovalWorkflowViewModel.java`

**Microservicios que debería llamar:**
1. ⚠️ **leka-prompt-governance** (8003)
   - **Uso potencial:** Evaluación de seguridad y efectividad en workflow de aprobación
   - **Estado:** ❌ Por verificar

**Referencia BPMN:**
- Proceso: `prompt-approval-v1.bpmn`

---

### 5.2 ViewModels de RAG

#### **RagSystemsDetailViewModel.java**
**Ubicación:** `src/main/java/com/codeflowx/govern/viewmodel/rag/RagSystemsDetailViewModel.java`

**Microservicios que debería llamar:**
1. ⚠️ **leka-rag-evaluation** (8004)
   - **Uso potencial:** Evaluación de calidad del sistema RAG
   - **Métodos relevantes:** `calculateIndexHealth()`
   - **Estado:** ❌ No usa microservicio actualmente

**Referencia Incidencias:**
- INC-005-002: Detección de alucinaciones (✅ Implementado en microservicio)
- INC-005-003: Validación proactiva políticas (✅ Implementado en microservicio)
- INC-005-004: Métricas RAG estandarizadas (✅ Implementado en microservicio)
- INC-005-006: Prevención grounding (✅ Implementado en microservicio)
- INC-005-007: Calidad chunks (✅ Implementado en microservicio)

**Recomendación:** Integrar `AIGovernanceClient.ragEvaluation()` para evaluaciones.

---

### 5.3 ViewModels de Modelos

#### **ModelsDetailViewModel.java**
**Ubicación:** `src/main/java/com/codeflowx/govern/viewmodel/models/ModelsDetailViewModel.java`

**Microservicios que debería llamar:**
1. ⚠️ **leka-model-wrapper** (8006)
   - **Uso potencial:** Benchmarking, comparación de modelos
   - **Métodos relevantes:** `validateModel()`, `calculateDrift()`
   - **Estado:** ❌ No usa microservicio actualmente

2. ⚠️ **leka-bias-detection-service** (8001)
   - **Uso potencial:** Análisis de sesgo del modelo
   - **Estado:** ❌ No usa microservicio actualmente

3. ⚠️ **leka-llm-evaluation** (8002)
   - **Uso potencial:** Evaluación de calidad del modelo LLM
   - **Estado:** ❌ No usa microservicio actualmente

**Referencia Incidencias:**
- INC-011: Recomendaciones automáticas (✅ Implementado en microservicio)

**Recomendación:** Integrar múltiples clientes según tipo de modelo.

---

#### **ModelApprovalWorkflowViewModel.java**
**Ubicación:** `src/main/java/com/codeflowx/govern/viewmodel/models/ModelApprovalWorkflowViewModel.java`

**Microservicios que debería llamar:**
1. ⚠️ **leka-model-wrapper** (8006)
   - **Uso potencial:** Validación de modelo en workflow de aprobación
   - **Estado:** ❌ Por verificar

2. ⚠️ **leka-bias-detection-service** (8001)
   - **Uso potencial:** Análisis de sesgo en aprobación
   - **Estado:** ❌ Por verificar

**Referencia BPMN:**
- Proceso: `model-evaluation-v1.bpmn`

---

### 5.4 ViewModels de Compliance (Documentación)

#### **CompleteDocumentationViewModel.java**
**Ubicación:** `src/main/java/com/codeflowx/govern/viewmodel/compliance/CompleteDocumentationViewModel.java`

**Microservicios que debería llamar:**
1. ⚠️ **leka-technical-documentation-generator** (8008)
   - **Uso potencial:** Generación automática de documentación técnica (Anexo IV)
   - **Estado:** ❌ Por verificar

**Referencia:**
- Art. 11 + Anexo IV EU AI Act

---

#### **AIActDocumentationGeneratorViewModel.java**
**Ubicación:** `src/main/java/com/codeflowx/govern/viewmodel/compliance/AIActDocumentationGeneratorViewModel.java`

**Microservicios que debería llamar:**
1. ⚠️ **leka-technical-documentation-generator** (8008)
   - **Uso potencial:** Generación de documentación AI Act
   - **Estado:** ❌ Por verificar

---

#### **InitiateConformityAssessmentViewModel.java**
**Ubicación:** `src/main/java/com/codeflowx/govern/viewmodel/compliance/InitiateConformityAssessmentViewModel.java`

**Microservicios que debería llamar:**
1. ⚠️ **leka-conformity-assessment** (8009)
   - **Uso potencial:** Iniciar evaluación de conformidad (Anexo VI)
   - **Estado:** ❌ Por verificar

**Referencia:**
- Anexo VI EU AI Act

---

#### **ConformityDeclarationManagerViewModel.java**
**Ubicación:** `src/main/java/com/codeflowx/govern/viewmodel/compliance/ConformityDeclarationManagerViewModel.java`

**Microservicios que debería llamar:**
1. ⚠️ **leka-eu-declaration-generator** (8010)
   - **Uso potencial:** Generación de declaración UE de conformidad (Anexo V)
   - **Estado:** ❌ Por verificar

**Referencia:**
- Anexo V EU AI Act

---

## PARTE 6: SERVICES ADICIONALES

### 6.1 Services de Evaluación

#### **RagEvaluationService.java** (Ya documentado en Parte 1)
**Ubicación:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/rag/RagEvaluationService.java`

**Estado:** ✅ Ya documentado en Parte 1

---

#### **PolicyEvaluationService.java**
**Ubicación:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/governance/PolicyEvaluationService.java`

**Microservicios que podría llamar:**
1. ⚠️ **leka-prompt-governance** (8003)
   - **Uso potencial:** Validación de políticas de prompts
   - **Estado:** ❌ Por verificar

---

### 6.2 Services de Modelos

#### **ModelBiasAnalysisService.java**
**Ubicación:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/evaluation/ModelBiasAnalysisService.java`

**Microservicios que debería llamar:**
1. ⚠️ **leka-bias-detection-service** (8001)
   - **Uso potencial:** Análisis de sesgo de modelos
   - **Estado:** ❌ Por verificar

**Referencia:**
- Art. 10 EU AI Act (Gobernanza de datos y sesgos)

---

#### **ModelPerformanceService.java**
**Ubicación:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/evaluation/ModelPerformanceService.java`

**Microservicios que debería llamar:**
1. ⚠️ **leka-model-wrapper** (8006)
   - **Uso potencial:** Benchmarking y evaluación de performance
   - **Estado:** ❌ Por verificar

2. ⚠️ **leka-llm-evaluation** (8002)
   - **Uso potencial:** Evaluación de calidad LLM
   - **Estado:** ❌ Por verificar

---

### 6.3 Services de Datasets

#### **DatasetQualityService.java**
**Ubicación:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/governance/DatasetQualityService.java`

**Microservicios que debería llamar:**
1. ⚠️ **leka-bias-detection-service** (8001)
   - **Uso potencial:** Evaluación de calidad y sesgo de datasets
   - **Estado:** ❌ Por verificar

**Referencia:**
- Art. 10 EU AI Act
- Proceso BPMN: `dataset-quality-v1.bpmn`

---

## PARTE 7: DELEGATES ADICIONALES (del inventario)

### 7.1 Delegates que usan microservicios Python (pero con RestTemplate directo)

#### **DetectDatasetBiasDelegate**
**Microservicios Python:**
1. ✅ **leka-server-serving-evaluation** (puerto ?)
   - **Endpoint:** `POST /api/v1/evaluation/dataset/bias`
   - **Método:** RestTemplate
   - **Uso:** Análisis de atributos protegidos y fairness

**Nota:** Endpoints diferentes a `LLMEvaluationClient` (`/api/llm/*`).

---

#### **ExecuteBiasDetectionDelegate**
**Microservicios Python:**
1. ✅ **leka-server-serving-evaluation** (puerto ?)
   - **Endpoint:** `POST /api/v1/evaluation/bias/detect`
   - **Método:** RestTemplate
   - **Uso:** Análisis de fairness con metadatos de modelo y dataset

**Nota:** Endpoints diferentes a `LLMEvaluationClient` (`/api/llm/*`).

---

#### **EstimateAdapterCostDelegate**
**Microservicios Python:**
1. ⚠️ **leka-model-wrapper** (8006)
   - **Endpoint:** `POST /api/model/estimate-adaptation-cost`
   - **Método:** RestTemplate
   - **Problema:** Endpoint diferente a `ModelWrapperClient` (`/api/models/*` vs `/api/model/*`)
   - **Uso:** Cálculo de coste, CO₂ y tiempo para LoRA/adapter

**Recomendación:** Migrar a usar `ModelWrapperClient` o adaptar endpoint.

---

#### **EvaluateContentBiasDelegate**
**Microservicios Python:**
1. ✅ **leka-bias-detection-service** (8001)
   - **Endpoint:** `POST /api/v1/detect`
   - **Base URL:** `http://leka-bias-detection-service:8080` (fija)
   - **Método:** RestTemplate
   - **Uso:** Evaluación de sesgo en contenido educativo

**Nota:** No existe cliente Java para este servicio, pero `AIGovernanceClient.biasDetection()` está disponible.

---

#### **ReviewTechnicalDocumentationDelegate**
**Microservicios Python:**
1. ⚠️ **technical-documentation-service** (puerto ?)
   - **Endpoint:** `POST /validate`
   - **Base URL:** `http://localhost:8002/api/technical-documentation` (default)
   - **Método:** RestTemplate
   - **Uso:** Validación de anexos AI Act (Annex IV)

**Nota:** Podría ser `leka-technical-documentation-generator` (8008), pero endpoint diferente.

---

### 7.2 Delegates que NO usan microservicios Python de compliance

#### **ModelEvaluationDelegate**
- **Microservicio:** `codeflowx-llm` (NO es microservicio Python de compliance)
- **Endpoints:** `/model-evaluation/performance`, `/model-evaluation/quality`, `/model-evaluation/drift`
- **Recomendación:** Migrar a usar `LLMEvaluationClient` o `ModelWrapperClient` si el gateway puede enrutar

#### **PromptSafetyDelegate**
- **Microservicio:** `codeflowx-llm` (NO es microservicio Python de compliance)
- **Endpoints:** `/prompt-safety/jailbreak`, `/prompt-safety/malicious-content`, `/prompt-safety/toxicity`
- **Recomendación:** Migrar a usar `PromptGovernanceClient` si el gateway puede enrutar

#### **RagEvaluationDelegate**
- **Microservicio:** `codeflowx-llm` (NO es microservicio Python de compliance)
- **Endpoints:** `/rag-evaluation/retrieval`, `/rag-evaluation/generation`, `/rag-evaluation/hallucinations`
- **Recomendación:** Migrar a usar `RAGEvaluationClient` si el gateway puede enrutar

**Nota:** Estos delegates usan servicios legacy que deberían migrarse a los nuevos microservicios Python.

---

## PARTE 8: RESUMEN CONSOLIDADO

### ViewModels que SÍ llaman microservicios Python:
1. ✅ **FriaWizardViewModel** → 4 microservicios (vía Delegates)

### ViewModels que DEBERÍAN llamar microservicios Python (10):
1. ⚠️ **HighRiskClassifierViewModel** → `leka-prompt-governance` (INC-002 pendiente)
2. ⚠️ **PromptsDetailViewModel** → `leka-prompt-governance`
3. ⚠️ **PromptApprovalWorkflowViewModel** → `leka-prompt-governance`
4. ⚠️ **RagSystemsDetailViewModel** → `leka-rag-evaluation`
5. ⚠️ **ModelsDetailViewModel** → `leka-model-wrapper`, `leka-bias-detection-service`, `leka-llm-evaluation`
6. ⚠️ **ModelApprovalWorkflowViewModel** → `leka-model-wrapper`, `leka-bias-detection-service`
7. ⚠️ **CompleteDocumentationViewModel** → `leka-technical-documentation-generator`
8. ⚠️ **AIActDocumentationGeneratorViewModel** → `leka-technical-documentation-generator`
9. ⚠️ **InitiateConformityAssessmentViewModel** → `leka-conformity-assessment`
10. ⚠️ **ConformityDeclarationManagerViewModel** → `leka-eu-declaration-generator`

### Services que SÍ llaman microservicios Python:
1. ✅ **RagEvaluationService** → `leka-rag-evaluation` (vía AIGovernanceClient)
2. ✅ **PostMarketMonitoringService** → 3 microservicios (vía Delegates)
3. ✅ **ModelAdaptationBusinessService** → `leka-model-wrapper` (RestTemplate)

### Services que DEBERÍAN llamar microservicios Python (4):
1. ⚠️ **PolicyEvaluationService** → `leka-prompt-governance`
2. ⚠️ **ModelBiasAnalysisService** → `leka-bias-detection-service`
3. ⚠️ **ModelPerformanceService** → `leka-model-wrapper`, `leka-llm-evaluation`
4. ⚠️ **DatasetQualityService** → `leka-bias-detection-service`

### Delegates que SÍ llaman microservicios Python (10):
1. ✅ **ExecuteLlmEvaluationDelegate** → `leka-llm-evaluation`
2. ✅ **DataProfilingDelegate** → `leka-server-serving-evaluation`
3. ✅ **AnalyzeDriftDelegate** → `leka-server-serving-evaluation`
4. ✅ **ValidateComplianceDelegate** → `leka-server-serving-evaluation`
5. ✅ **CrossValidateFriaDelegate** → 4 microservicios
6. ✅ **DetectDatasetBiasDelegate** → `leka-server-serving-evaluation`
7. ✅ **ExecuteBiasDetectionDelegate** → `leka-server-serving-evaluation`
8. ✅ **EstimateAdapterCostDelegate** → `leka-model-wrapper`
9. ✅ **EvaluateContentBiasDelegate** → `leka-bias-detection-service`
10. ✅ **ReviewTechnicalDocumentationDelegate** → `technical-documentation-service` (posiblemente 8008)

### Delegates que DEBERÍAN migrar a microservicios Python (3):
1. ⚠️ **ModelEvaluationDelegate** → Migrar a `LLMEvaluationClient` o `ModelWrapperClient`
2. ⚠️ **PromptSafetyDelegate** → Migrar a `PromptGovernanceClient`
3. ⚠️ **RagEvaluationDelegate** → Migrar a `RAGEvaluationClient`

---

## GAPS Y RECOMENDACIONES

### GAP 1: ViewModels sin integración
**10 ViewModels** identificados que deberían usar microservicios Python pero no lo hacen.

**Prioridad:** 🔴 CRÍTICA para ViewModels de compliance (documentación, conformidad, declaración UE)

### GAP 2: Services sin integración
**4 Services** identificados que deberían usar microservicios Python pero no lo hacen.

**Prioridad:** 🟡 MEDIA

### GAP 3: Delegates usando RestTemplate directo
**10 Delegates** usan RestTemplate directamente en lugar de clientes Java formales.

**Recomendación:** Migrar a usar `AIGovernanceClient` y sus clientes especializados.

### GAP 4: Delegates usando servicios legacy
**3 Delegates** usan servicios legacy (`codeflowx-llm`) que deberían migrarse a microservicios Python.

**Recomendación:** Migrar endpoints a usar clientes Java formales.

---

**Última actualización:** Diciembre 2025

