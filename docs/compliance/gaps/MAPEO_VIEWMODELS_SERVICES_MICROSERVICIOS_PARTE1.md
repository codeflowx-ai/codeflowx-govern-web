# MAPEO: VIEWMODELS Y SERVICES JAVA → MICROSERVICIOS PYTHON

**Fecha:** Diciembre 2025  
**Objetivo:** Identificar qué ViewModels y Services Java llaman directamente a los microservicios Python según auditoría, incidencias y código fuente

**Nota:** Este documento se divide en partes debido al gran volumen de información.

---

## 📊 RESUMEN EJECUTIVO (Preliminar)

**Total ViewModels identificados:** 91+  
**Total Services identificados:** 154+  
**ViewModels que llaman microservicios Python:** En análisis  
**Services que llaman microservicios Python:** En análisis

---

## PARTE 1: VIEWMODELS DE COMPLIANCE

### 1.1 HighRiskClassifierViewModel.java

**Ubicación:** `src/main/java/com/codeflowx/govern/viewmodel/compliance/HighRiskClassifierViewModel.java`

**Microservicios Python Llamados:**
- ❌ **Ninguno directamente** (actualmente)

**Nota según código:**
- Línea 394: `// TODO: Llamar a microservicio Python leka-prompt-governance o nuevo micro classification`
- **Estado:** Pendiente de implementación según INC-002 (Sugerencia IA)

**Referencia Incidencias:**
- INC-001: Validación Coherencia Modelo-Dataset (✅ Implementado - no requiere microservicio)
- INC-002: Validación Confianza Sugerencia IA (⚠️ Pendiente - requiere `leka-prompt-governance` o nuevo micro)
- INC-003: Validación Documentación Técnica (✅ Implementado - no requiere microservicio)
- INC-004: Validación Calidad Justificación (✅ Implementado - no requiere microservicio)
- INC-005: Validación Sistemas Prohibidos (✅ Implementado - no requiere microservicio)

**Documentos:**
- 📄 Auditoría: `AUDITORIA_CATALOGACION_CLASIFICACION_IA.md`
- 📋 Incidencias: `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-001, INC-002, INC-003, INC-004, INC-005)

---

### 1.2 FriaWizardViewModel.java

**Ubicación:** `src/main/java/com/codeflowx/govern/viewmodel/compliance/FriaWizardViewModel.java`

**Microservicios Python Llamados:**
1. ✅ **leka-fria-generator** (8012)
   - **Endpoint:** `POST /api/fria/generate-assessment`
   - **Uso:** Generación de documento FRIA completo
   - **Endpoint:** `POST /api/fria/cross-validate` (INC-007)
   - **Uso:** Validación cruzada FRIA vs métricas técnicas

2. ✅ **leka-bias-detection-service** (8001)
   - **Uso:** Integrado en validación cruzada (INC-007)
   - **Llamado desde:** Delegate `CrossValidateFriaDelegate`

3. ✅ **leka-llm-evaluation** (8002)
   - **Uso:** Integrado en validación cruzada (INC-007)
   - **Llamado desde:** Delegate `CrossValidateFriaDelegate`

4. ✅ **leka-adversarial-robustness** (8007)
   - **Uso:** Integrado en validación cruzada (INC-007)
   - **Llamado desde:** Delegate `CrossValidateFriaDelegate`

**Referencia Incidencias:**
- INC-007: Validación Cruzada FRIA vs Métricas Técnicas (✅ Implementado)
- INC-009: Persistencia Estado Árbol Riesgo (✅ Implementado - no requiere microservicio)
- INC-016: Exportación Árbol Riesgo (✅ Implementado - no requiere microservicio)
- INC-021: Versionado FRIA (✅ Implementado - no requiere microservicio)
- INC-023: Validación Calidad FRIA (✅ Implementado - no requiere microservicio)

**Documentos:**
- 📄 Auditoría: `AUDITORIA_FRIA_EVALUACIONES_TECNICAS.md`
- 📋 Incidencias: `INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md` (INC-007, INC-009, INC-016, INC-021, INC-023)
- 📄 BPMN: `docs/compliance/bpmn/compliance/fria/technical.md`

**Nota:** Las llamadas a microservicios se realizan principalmente a través de Delegates BPMN, no directamente desde el ViewModel.

---

### 1.3 Otros ViewModels de Compliance

**ViewModels identificados pero sin llamadas directas a microservicios Python:**

1. **ConformityDeclarationManagerViewModel.java**
   - **Microservicios:** Potencialmente `leka-eu-declaration-generator` (8010)
   - **Estado:** Por verificar

2. **InitiateConformityAssessmentViewModel.java**
   - **Microservicios:** Potencialmente `leka-conformity-assessment` (8009)
   - **Estado:** Por verificar

3. **CompleteDocumentationViewModel.java**
   - **Microservicios:** Potencialmente `leka-technical-documentation-generator` (8008)
   - **Estado:** Por verificar

4. **AIActDocumentationGeneratorViewModel.java**
   - **Microservicios:** Potencialmente `leka-technical-documentation-generator` (8008)
   - **Estado:** Por verificar

**Nota:** Estos ViewModels requieren revisión de código fuente para confirmar llamadas a microservicios.

---

## PARTE 2: SERVICES DE WORKFLOW (BPMN Delegates)

### 2.1 RagEvaluationService.java

**Ubicación:** `codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/services/RagEvaluationService.java`

**Microservicios Python Llamados:**
1. ✅ **leka-rag-evaluation** (8004)
   - **Cliente:** `AIGovernanceClient.ragEvaluation()` → `RAGEvaluationClient`
   - **Endpoints utilizados:**
     - `POST /api/rag/evaluate-retrieval` → `evaluateRetrieval()`
     - `POST /api/rag/evaluate-answer` → `evaluateAnswer()`
     - `POST /api/rag/evaluate-context-relevance` → `evaluateContextRelevance()`
     - `POST /api/rag/evaluate-full-pipeline` → `evaluateFullPipeline()`
     - `POST /api/rag/evaluate-document-quality` → `evaluateDocumentQuality()`
     - `POST /api/rag/evaluate-conversation-context` → `evaluateConversationContext()`
     - `POST /api/rag/evaluate-citations` → `evaluateCitations()`
     - `POST /api/rag/evaluate-index-quality` → `evaluateIndexQuality()`

**Código relevante:**
```java
@Autowired(required = false)
private AIGovernanceClient aiGovernanceClient;

// Uso:
RAGRetrievalResponse retrievalResponse = aiGovernanceClient.ragEvaluation()
    .evaluateRetrieval(retrievalRequest);
```

**Referencia Incidencias:**
- INC-005-002: Detección de alucinaciones (✅ Implementado)
- INC-005-003: Validación proactiva políticas (✅ Implementado)
- INC-005-004: Métricas RAG estandarizadas (✅ Implementado)
- INC-005-006: Prevención grounding (✅ Implementado)
- INC-005-007: Calidad chunks (✅ Implementado)
- INC-005-010: Proceso BPMN completo (✅ Implementado)

**Documentos:**
- 📋 Incidencias: `INCIDENCIAS_005_EVALUACION_RAG.md`
- 📄 BPMN: `rag-evaluation-v1.bpmn`

---

### 2.2 PostMarketMonitoringService.java

**Ubicación:** `codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/services/PostMarketMonitoringService.java`

**Microservicios Python Llamados:**
1. ✅ **leka-llm-evaluation** (8002)
   - **Uso:** Evaluación de toxicidad y sesgo en post-market monitoring
   - **Llamado desde:** Delegate `CheckPostMarketMetricsDelegate`

2. ✅ **leka-prompt-governance** (8003)
   - **Uso:** Verificación de seguridad de prompts
   - **Llamado desde:** Delegate `CheckPostMarketMetricsDelegate`

3. ✅ **leka-bias-detection-service** (8001)
   - **Uso:** Detección de sesgo en datos
   - **Llamado desde:** Delegate `CheckPostMarketMetricsDelegate`

**Referencia Incidencias:**
- INC-010: Post-Market Monitoring (✅ Implementado)

**Documentos:**
- 📄 Auditoría: `AUDITORIA_010_POST_MARKET_MONITORING.md`
- 📄 BPMN: `compliance-monitoring-v1.bpmn`

---

### 2.3 ExecuteLlmEvaluationDelegate.java

**Ubicación:** `codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/delegates/ExecuteLlmEvaluationDelegate.java`

**Microservicios Python Llamados:**
1. ✅ **leka-llm-evaluation** (8002)
   - **Endpoint:** `POST /api/v1/evaluation/llm/run`
   - **Método:** WebClient (reactive)
   - **Uso:** Ejecución de evaluaciones LLM desde procesos BPMN

**Código relevante:**
```java
@Autowired
@Qualifier("evaluationClient")
private WebClient evaluationClient;

Mono<Map> responseMono = evaluationClient.post()
    .uri("/api/v1/evaluation/llm/run")
    .bodyValue(request)
    .retrieve()
    .bodyToMono(Map.class)
    .timeout(Duration.ofMinutes(10));
```

**Referencia BPMN:**
- Proceso: `llm-evaluation-v1.bpmn`

---

### 2.4 DataProfilingDelegate.java

**Ubicación:** `codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/delegates/dataset/DataProfilingDelegate.java`

**Microservicios Python Llamados:**
1. ✅ **leka-server-serving-evaluation** (puerto ?)
   - **Endpoint:** `POST /api/v1/evaluation/dataset/quality`
   - **Método:** RestTemplate
   - **Uso:** Evaluación de calidad de datasets

**Código relevante:**
```java
String url = evaluationServiceUrl + "/api/v1/evaluation/dataset/quality";
ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
```

**Nota:** Este microservicio (`leka-server-serving-evaluation`) no está en los prompts de compliance pero es usado por delegates.

**Referencia BPMN:**
- Proceso: `dataset-quality-v1.bpmn`

---

## PARTE 3: BUSINESS SERVICES

### 3.1 ModelAdaptationBusinessService.java

**Ubicación:** `src/main/java/com/codeflowx/govern/business/models/ModelAdaptationBusinessService.java`

**Microservicios Python Llamados:**
1. ✅ **leka-model-wrapper** (8006)
   - **Endpoint:** `POST /api/model/recommend-adaptation`
   - **Método:** RestTemplate
   - **Uso:** Recomendación de estrategia de adaptación de modelos

**Código relevante:**
```java
@Value("${leka.model.wrapper.url:http://localhost:8006}")
private String modelWrapperUrl;

String endpoint = modelWrapperUrl + "/api/model/recommend-adaptation";
ResponseEntity<AdaptationRecommendation> response = restTemplate.postForEntity(
    endpoint,
    request,
    AdaptationRecommendation.class
);
```

**Referencia:**
- Art. 51-55 EU AI Act - GPAI Downstream Providers

---

## RESUMEN PARCIAL (Parte 1)

### ViewModels que llaman microservicios Python:
1. ✅ **FriaWizardViewModel** → `leka-fria-generator`, `leka-bias-detection-service`, `leka-llm-evaluation`, `leka-adversarial-robustness` (vía Delegates)

### Services que llaman microservicios Python:
1. ✅ **RagEvaluationService** → `leka-rag-evaluation` (vía AIGovernanceClient)
2. ✅ **PostMarketMonitoringService** → `leka-llm-evaluation`, `leka-prompt-governance`, `leka-bias-detection-service` (vía Delegates)
3. ✅ **ExecuteLlmEvaluationDelegate** → `leka-llm-evaluation` (WebClient)
4. ✅ **DataProfilingDelegate** → `leka-server-serving-evaluation` (RestTemplate)

### Business Services que llaman microservicios Python:
1. ✅ **ModelAdaptationBusinessService** → `leka-model-wrapper` (RestTemplate)

---

## PARTE 2: DELEGATES BPMN (Workflow Services)

### 2.5 Otros Delegates que llaman microservicios Python

Según `delegate-microservice-inventory.md`, los siguientes delegates llaman microservicios Python:

#### **AnalyzeDriftDelegate**
**Microservicios Python:**
1. ✅ **leka-server-serving-evaluation** (puerto ?)
   - **Endpoint:** `POST /api/v1/evaluation/drift/detect`
   - **Método:** RestTemplate
   - **Uso:** Detección de data/concept drift

**Nota:** Este microservicio no está en los prompts de compliance pero es usado por delegates.

---

#### **ValidateComplianceDelegate**
**Microservicios Python:**
1. ✅ **leka-server-serving-evaluation** (puerto ?)
   - **Endpoint:** `POST /api/v1/evaluation/dataset/pii`
   - **Método:** RestTemplate
   - **Uso:** Detección de PII en datasets (Presidio)

**Nota:** Endpoints diferentes a `LLMEvaluationClient` (`/api/llm/*`).

---

### 2.6 Delegates que NO usan microservicios Python (pero usan otros servicios)

#### **AIComplianceCheckDelegate**
- **Microservicio:** `leka-server-governance` (NO es microservicio Python de compliance)
- **Endpoint:** `POST /api/v1/governance/compliance-check`
- **Método:** WebClient (AIAgentService)

#### **AIEthicalReviewDelegate**
- **Microservicio:** `leka-server-governance` (NO es microservicio Python de compliance)
- **Endpoint:** `POST /api/v1/governance/ethical-review`
- **Método:** WebClient (AIAgentService)

#### **AIRiskAssessmentDelegate**
- **Microservicio:** `leka-server-governance` (NO es microservicio Python de compliance)
- **Endpoint:** `POST /api/v1/governance/risk-assessment`
- **Método:** WebClient (AIAgentService)

#### **ComplianceCheckDelegate**
- **Microservicio:** `codeflowx-inference` (NO es microservicio Python de compliance)
- **Endpoint:** `POST /api/rag/query`, `POST /chat/completions`
- **Método:** ComplianceCheckService con ModelInferenceService

---

## PARTE 3: CLIENTES JAVA DISPONIBLES

### 3.1 Clientes en `codeflowx.govern.nocode.client`

Estos clientes están disponibles para usar en ViewModels y Services:

1. ✅ **AIGovernanceClient** (Factoría de clientes)
   - **Gateway URL:** `http://api-leka-govern:8000` (K8s) o `http://localhost:8000` (local)
   - **Proporciona acceso a:**
     - `llmEvaluation()` → LLMEvaluationClient
     - `ragEvaluation()` → RAGEvaluationClient
     - `promptGovernance()` → PromptGovernanceClient
     - `agentMonitoring()` → AgentMonitoringClient
     - `modelWrapper()` → ModelWrapperClient
     - `aiInterpreter()` → AIInterpreterClient

2. ✅ **LLMEvaluationClient**
   - **Microservicio:** leka-llm-evaluation (puerto 8002)
   - **Endpoints Base:** `/api/llm/*`
   - **Estado:** ✅ Disponible

3. ✅ **PromptGovernanceClient**
   - **Microservicio:** leka-prompt-governance (puerto 8003)
   - **Endpoints Base:** `/api/prompt/*`
   - **Estado:** ✅ Disponible

4. ✅ **RAGEvaluationClient**
   - **Microservicio:** leka-rag-evaluation (puerto 8004)
   - **Endpoints Base:** `/api/rag/*`
   - **Estado:** ✅ Disponible

5. ✅ **AgentMonitoringClient**
   - **Microservicio:** leka-agent-monitoring (puerto 8005)
   - **Endpoints Base:** `/api/agent/*`
   - **Estado:** ✅ Disponible

6. ✅ **ModelWrapperClient**
   - **Microservicio:** leka-model-wrapper (puerto 8006)
   - **Endpoints Base:** `/api/models/*`
   - **Estado:** ✅ Disponible

7. ✅ **AIInterpreterClient**
   - **Microservicio:** leka-ai-interpreter (puerto 8011)
   - **Endpoints Base:** `/api/interpret/*`
   - **Estado:** ✅ Disponible

### 3.2 Microservicios SIN Cliente Java (pero usados por Delegates)

1. ⚠️ **leka-server-serving-evaluation** (puerto ?)
   - **Endpoints:** `/api/v1/evaluation/*`
   - **Uso:** Delegates usan RestTemplate directamente
   - **Nota:** No está en los prompts de compliance

2. ⚠️ **leka-fria-generator** (8012)
   - **Estado:** Cliente Java no identificado
   - **Uso:** Llamado desde Delegates BPMN (CrossValidateFriaDelegate)

3. ⚠️ **leka-bias-detection-service** (8001)
   - **Estado:** Cliente Java no identificado
   - **Uso:** Llamado desde Delegates BPMN (CrossValidateFriaDelegate)

4. ⚠️ **leka-adversarial-robustness** (8007)
   - **Estado:** Cliente Java no identificado
   - **Uso:** Llamado desde Delegates BPMN (CrossValidateFriaDelegate)

5. ⚠️ **leka-technical-documentation-generator** (8008)
   - **Estado:** Cliente Java no identificado
   - **Uso:** Potencialmente desde ViewModels de documentación

6. ⚠️ **leka-conformity-assessment** (8009)
   - **Estado:** Cliente Java no identificado
   - **Uso:** Potencialmente desde ViewModels de conformidad

7. ⚠️ **leka-eu-declaration-generator** (8010)
   - **Estado:** Cliente Java no identificado
   - **Uso:** Potencialmente desde ViewModels de declaración UE

8. ⚠️ **leka-copyright-compliance** (8013)
   - **Estado:** Cliente Java no identificado
   - **Uso:** Potencialmente desde ViewModels de copyright

---

## RESUMEN COMPLETO (Partes 1 y 2)

### ViewModels que llaman microservicios Python:
1. ✅ **FriaWizardViewModel** → `leka-fria-generator`, `leka-bias-detection-service`, `leka-llm-evaluation`, `leka-adversarial-robustness` (vía Delegates)

### Services que llaman microservicios Python:
1. ✅ **RagEvaluationService** → `leka-rag-evaluation` (vía AIGovernanceClient)
2. ✅ **PostMarketMonitoringService** → `leka-llm-evaluation`, `leka-prompt-governance`, `leka-bias-detection-service` (vía Delegates)
3. ✅ **ModelAdaptationBusinessService** → `leka-model-wrapper` (RestTemplate)

### Delegates que llaman microservicios Python:
1. ✅ **ExecuteLlmEvaluationDelegate** → `leka-llm-evaluation` (WebClient)
2. ✅ **DataProfilingDelegate** → `leka-server-serving-evaluation` (RestTemplate)
3. ✅ **AnalyzeDriftDelegate** → `leka-server-serving-evaluation` (RestTemplate)
4. ✅ **ValidateComplianceDelegate** → `leka-server-serving-evaluation` (RestTemplate)
5. ✅ **CrossValidateFriaDelegate** → `leka-fria-generator`, `leka-bias-detection-service`, `leka-llm-evaluation`, `leka-adversarial-robustness` (RestTemplate/WebClient)

---

## GAPS IDENTIFICADOS

### Microservicios sin Cliente Java:
1. ⚠️ **leka-fria-generator** (8012) - Usado por Delegates pero sin cliente formal
2. ⚠️ **leka-bias-detection-service** (8001) - Usado por Delegates pero sin cliente formal
3. ⚠️ **leka-adversarial-robustness** (8007) - Usado por Delegates pero sin cliente formal
4. ⚠️ **leka-technical-documentation-generator** (8008) - Sin cliente identificado
5. ⚠️ **leka-conformity-assessment** (8009) - Sin cliente identificado
6. ⚠️ **leka-eu-declaration-generator** (8010) - Sin cliente identificado
7. ⚠️ **leka-copyright-compliance** (8013) - Sin cliente identificado

### ViewModels que deberían llamar microservicios pero no lo hacen:
1. ⚠️ **HighRiskClassifierViewModel** - TODO pendiente para INC-002
2. ⚠️ **ConformityDeclarationManagerViewModel** - Potencialmente `leka-eu-declaration-generator`
3. ⚠️ **InitiateConformityAssessmentViewModel** - Potencialmente `leka-conformity-assessment`
4. ⚠️ **CompleteDocumentationViewModel** - Potencialmente `leka-technical-documentation-generator`
5. ⚠️ **AIActDocumentationGeneratorViewModel** - Potencialmente `leka-technical-documentation-generator`

---

**Continuará en Parte 3 (otros ViewModels y Services)...**

**Última actualización:** Diciembre 2025

