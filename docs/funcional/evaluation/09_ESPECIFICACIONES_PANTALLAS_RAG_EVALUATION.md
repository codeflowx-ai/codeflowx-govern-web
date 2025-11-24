# 📋 ESPECIFICACIONES DE PANTALLAS ZKOSS - RAG EVALUATION

**Fecha:** Diciembre 2024  
**Versión:** 1.0  
**Propósito:** Especificaciones completas de pantallas ZKoss para evaluación RAG  
**Referencia:** Cliente Java `RAGEvaluationClient` con 24 endpoints

---

## 📊 RESUMEN EJECUTIVO

Este documento especifica las pantallas ZKoss necesarias para la evaluación de sistemas RAG, basadas en el cliente Java `RAGEvaluationClient` que proporciona acceso a todos los endpoints del microservicio RAG Evaluation (puerto 8004).

**Pantallas Requeridas:** 8 pantallas principales + 3 pantallas BPMN  
**Cliente Java:** `RAGEvaluationClient` (24 endpoints)  
**Documentación Cliente:** `codeflowx.govern.nocode.client/RAG_CLIENT_USAGE.md`

---

## 🎯 PANTALLAS PRINCIPALES

### 1. RAG Evaluation - Overview (Listado)

**Ubicación:** `src/main/webapp/console/platform/evaluation/rag-evaluation/overview.zul`

**Propósito:** Listar todas las evaluaciones RAG realizadas con filtros y búsqueda.

**Funcionalidades:**
- ✅ Listar evaluaciones RAG con paginación
- ✅ Filtros: fecha, score, risk level, cliente
- ✅ Búsqueda por query, evaluation ID
- ✅ Ordenamiento por fecha, score, risk level
- ✅ Acciones: Ver detalle, Nueva evaluación, Exportar

**Campos de Tabla:**
- Evaluation ID
- Query
- Overall Score (con indicador visual de color)
- Risk Level (LOW/MEDIUM/HIGH/CRITICAL con badges)
- Faithfulness
- Answer Relevancy
- Context Precision
- Fecha de evaluación
- Cliente
- Acciones (Ver, Editar, Eliminar)

**ViewModel:** `RAGEvaluationOverviewViewModel.java`

**Endpoints Cliente:**
- No requiere llamada directa (usa servicio que persiste en BD)

---

### 2. RAG Evaluation - Detail (Crear/Editar)

**Ubicación:** `src/main/webapp/console/platform/evaluation/rag-evaluation/page.zul`

**Propósito:** Crear o editar una evaluación RAG completa del pipeline.

**Funcionalidades:**
- ✅ Formulario para evaluación completa del pipeline
- ✅ Campos: Query, Documentos Recuperados, Respuesta Generada, Ground Truth
- ✅ Agregar/Eliminar documentos recuperados
- ✅ Ejecutar evaluación en tiempo real
- ✅ Visualizar resultados: scores, métricas, recomendaciones
- ✅ Guardar evaluación en base de datos

**Secciones del Formulario:**

#### Sección 1: Datos de Entrada
- **Query:** Textbox multilínea (obligatorio)
- **Documentos Recuperados:** Lista dinámica con:
  - Doc ID (textbox)
  - Contenido (textbox multilínea)
  - Score (numberbox, 0.0-1.0)
  - Metadata (mapa de propiedades)
  - Botón "Agregar Documento"
  - Botón "Eliminar" por documento
- **Respuesta Generada:** Textbox multilínea (obligatorio)
- **Ground Truth:** Textbox multilínea (opcional)

#### Sección 2: Resultados de Evaluación
- **Overall Score:** Label con indicador de color (0-100)
- **Retrieval Score:** Label (0-100)
- **Answer Score:** Label (0-100)
- **Faithfulness:** Label (0.0-1.0) con barra de progreso
- **Answer Relevancy:** Label (0.0-1.0) con barra de progreso
- **Context Precision:** Label (0.0-1.0) con barra de progreso
- **Grade:** Label (A/B/C/D/F)
- **Risk Level:** Badge con color (LOW/MEDIUM/HIGH/CRITICAL)
- **Bottlenecks:** Lista de problemas detectados
- **Recommendations:** Textarea con recomendaciones

**Botones:**
- "Evaluar Pipeline Completo" - Ejecuta evaluación
- "Validar Políticas" - Valida políticas antes de generar
- "Guardar Evaluación" - Persiste en BD
- "Limpiar Formulario" - Resetea formulario
- "Cancelar" - Regresa a overview

**ViewModel:** `RAGEvaluationViewModel.java`

**Endpoints Cliente:**
- `evaluateFullPipeline(RAGFullPipelineRequest)` - Evaluación completa
- `validatePolicies(RAGPolicyValidationRequest)` - Validación de políticas

**Ejemplo de Código:**
```java
@Command
@NotifyChange({"evaluationResult", "overallScore", "riskLevel", "grade", "loading"})
public void evaluateFullPipeline() {
    // Ver RAG_CLIENT_USAGE.md para implementación completa
}
```

---

### 3. RAG Retrieval Evaluation

**Ubicación:** `src/main/webapp/console/platform/evaluation/rag-evaluation/retrieval.zul`

**Propósito:** Evaluar específicamente la calidad del retrieval.

**Funcionalidades:**
- ✅ Formulario para evaluación de retrieval
- ✅ Campos: Query, Documentos Recuperados, Documentos Relevantes
- ✅ Visualizar métricas: Precision, Recall, F1, NDCG
- ✅ Gráfico de distribución de scores

**Campos:**
- **Query:** Textbox multilínea
- **Documentos Recuperados:** Lista con doc_id, content, score
- **Documentos Relevantes:** Lista de IDs de documentos relevantes (ground truth)

**Resultados:**
- Precision (0.0-1.0)
- Recall (0.0-1.0)
- F1 Score (0.0-1.0)
- NDCG (0.0-1.0)
- Retrieval Quality (Map con análisis detallado)

**ViewModel:** `RAGRetrievalEvaluationViewModel.java`

**Endpoints Cliente:**
- `evaluateRetrieval(RAGRetrievalRequest)`

---

### 4. RAG Answer Evaluation

**Ubicación:** `src/main/webapp/console/platform/evaluation/rag-evaluation/answer.zul`

**Propósito:** Evaluar específicamente la calidad de una respuesta generada.

**Funcionalidades:**
- ✅ Formulario para evaluación de respuesta
- ✅ Campos: Query, Respuesta, Contexto Recuperado, Ground Truth
- ✅ Visualizar métricas: Answer Quality, Groundedness, Relevance, Correctness
- ✅ Detección de alucinaciones con score y segmentos específicos

**Campos:**
- **Query:** Textbox multilínea
- **Answer:** Textbox multilínea
- **Retrieved Context:** Lista de contextos recuperados
- **Ground Truth:** Textbox multilínea (opcional)

**Resultados:**
- Answer Quality (0-100)
- Groundedness (0.0-1.0)
- Relevance (0.0-1.0)
- Correctness (0.0-1.0)
- Hallucination Score (0.0-1.0) con indicador de riesgo
- Grade (A/B/C/D/F)
- Issues: Lista de problemas detectados
- Recommendations: Textarea

**ViewModel:** `RAGAnswerEvaluationViewModel.java`

**Endpoints Cliente:**
- `evaluateAnswer(RAGAnswerRequest)`

---

### 5. RAG Policy Validation

**Ubicación:** `src/main/webapp/console/platform/evaluation/rag-evaluation/policy-validation.zul`

**Propósito:** Validar políticas del cliente antes de generar respuesta (EU AI Act Art. 10).

**Funcionalidades:**
- ✅ Formulario para validación proactiva de políticas
- ✅ Campos: Query, Chunks Recuperados, Políticas del Cliente
- ✅ Visualizar: Validation Passed, Alignment Score, Filtered Chunks
- ✅ Recomendaciones de ajuste

**Campos:**
- **Query:** Textbox multilínea
- **Retrieved Chunks:** Lista de chunks con contenido
- **Client Policies:** Mapa de políticas (language, topics, tone, ethics)
- **Validate Before Generation:** Checkbox

**Resultados:**
- Validation Passed (Boolean con badge)
- Alignment Score (0-100)
- Should Proceed (Boolean)
- Filtered Chunks Count (Integer)
- Query Validation (Map con detalles)
- Chunks Validation (Map con detalles)
- Recommendation (String)

**ViewModel:** `RAGPolicyValidationViewModel.java`

**Endpoints Cliente:**
- `validatePolicies(RAGPolicyValidationRequest)`

---

### 6. RAG Benchmarking

**Ubicación:** `src/main/webapp/console/platform/evaluation/rag-evaluation/benchmarking.zul`

**Propósito:** Ejecutar benchmarking del sistema RAG con datasets estándar.

**Funcionalidades:**
- ✅ Formulario para configuración de benchmark
- ✅ Selección de dataset: custom, cohere, trec, msmarco
- ✅ Configuración del sistema RAG
- ✅ Queries personalizadas
- ✅ Opciones: Habilitar datasets externos, Conexión RAG real
- ✅ Visualizar resultados: métricas agregadas, comparación

**Campos:**
- **System Config:** Mapa de configuración del sistema
- **Benchmark Dataset:** Combobox (custom, cohere, trec, msmarco)
- **Custom Queries:** Lista de queries personalizadas
- **Enable External Datasets:** Checkbox
- **RAG Connection:** Sección colapsable con:
  - RAG Service URL
  - Timeout
  - API Key (opcional)
  - Headers adicionales (opcional)

**Resultados:**
- Benchmark Dataset (String)
- System Config (String)
- Results (Map con métricas agregadas)
- Metadata (Map con información del benchmark)

**ViewModel:** `RAGBenchmarkingViewModel.java`

**Endpoints Cliente:**
- `benchmarkSystem(RAGBenchmarkRequest)`

---

### 7. RAG Human Validation Queue

**Ubicación:** `src/main/webapp/console/platform/evaluation/rag-evaluation/human-validation-queue.zul`

**Propósito:** Gestionar cola de validaciones humanas de alucinaciones (Plan Resolución 3%).

**Funcionalidades:**
- ✅ Listar validaciones pendientes en cola
- ✅ Filtros: priority, status, fecha
- ✅ Ver detalle de validación
- ✅ Enviar validación humana
- ✅ Estadísticas de validación

**Campos de Tabla:**
- Validation ID
- Evaluation ID
- Query
- Answer
- Hallucination Score
- Priority (HIGH/MEDIUM/LOW)
- Queue Position
- Estimated Wait Time
- Status
- Fecha de encolado
- Acciones (Ver, Validar)

**Sección de Estadísticas:**
- Total Validated
- Confirmed Hallucinations
- False Positives
- Precision
- Pending in Queue
- Pending by Priority (Map)

**ViewModel:** `RAGHumanValidationQueueViewModel.java`

**Endpoints Cliente:**
- `getValidationQueue()` - Obtener cola completa
- `getValidationQueue(Integer limit, String priority)` - Con filtros
- `getValidationById(String evaluationId)` - Detalle de validación
- `submitValidation(RAGHumanValidationRequest)` - Enviar validación
- `getValidationStatistics()` - Estadísticas

---

### 8. RAG Human Validation Detail

**Ubicación:** `src/main/webapp/console/platform/evaluation/rag-evaluation/human-validation-detail.zul`

**Propósito:** Formulario para validación humana de una alucinación específica.

**Funcionalidades:**
- ✅ Mostrar detalles de evaluación
- ✅ Query y respuesta generada
- ✅ Análisis de alucinación con segmentos específicos
- ✅ Formulario de validación: Is Hallucination, Confidence, Notes
- ✅ Enviar validación

**Campos de Visualización (Read-only):**
- Evaluation ID
- Query
- Answer
- Hallucination Analysis (Map con score, severity, detected_hallucinations)

**Campos de Validación:**
- **Is Hallucination:** Checkbox
- **Confidence:** Numberbox (0.0-1.0)
- **Notes:** Textarea multilínea

**Botones:**
- "Enviar Validación" - Submite validación
- "Cancelar" - Regresa a cola

**ViewModel:** `RAGHumanValidationDetailViewModel.java`

**Endpoints Cliente:**
- `getValidationById(String evaluationId)` - Obtener detalle
- `submitValidation(RAGHumanValidationRequest)` - Enviar validación

---

## 🔄 PANTALLAS BPMN

### 9. RAG Evaluation Review Form (BPMN)

**Ubicación:** `src/main/webapp/console/bpmn/rag-evaluation-review-form.zul`

**Propósito:** Formulario de revisión manual en proceso BPMN `rag-evaluation-v1.bpmn`.

**Funcionalidades:**
- ✅ Mostrar resultados de evaluación RAG
- ✅ Campos de revisión: Aprobar, Rechazar, Requerir Cambios
- ✅ Comentarios del revisor
- ✅ Visualizar métricas y nivel de riesgo

**Campos de Visualización:**
- Process Instance ID
- Evaluation Score
- Risk Level
- Faithfulness
- Answer Relevancy
- Context Precision
- Grade
- Bottlenecks
- Recommendations

**Campos de Revisión:**
- **Decisión:** Radiogroup (Aprobar, Rechazar, Requerir Cambios)
- **Comentarios:** Textarea multilínea

**Botones:**
- "Aprobar" - Completa tarea con aprobación
- "Rechazar" - Completa tarea con rechazo
- "Requerir Cambios" - Completa tarea con cambios requeridos

**ViewModel:** `RAGEvaluationReviewFormViewModel.java`

**Integración BPMN:**
- User Task: `manualReview` en `rag-evaluation-v1.bpmn`
- Form Key: `rag-review-form`

---

### 10. RAG Alert Form (BPMN)

**Ubicación:** `src/main/webapp/console/bpmn/rag-alert-form.zul`

**Propósito:** Formulario para crear alerta cuando evaluación RAG tiene riesgo alto/crítico.

**Funcionalidades:**
- ✅ Mostrar evaluación con riesgo alto/crítico
- ✅ Campos de alerta: Tipo, Severidad, Mensaje
- ✅ Asignar responsable
- ✅ Crear alerta

**ViewModel:** `RAGAlertFormViewModel.java`

**Integración BPMN:**
- Service Task: `createAlert` en `rag-evaluation-v1.bpmn`

---

### 11. RAG Evaluation Timeout Form (BPMN)

**Ubicación:** `src/main/webapp/console/bpmn/rag-evaluation-timeout-form.zul`

**Propósito:** Formulario para manejar timeout en evaluación RAG.

**Funcionalidades:**
- ✅ Mostrar información de timeout
- ✅ Opciones: Reintentar, Cancelar, Escalar
- ✅ Comentarios

**ViewModel:** `RAGEvaluationTimeoutFormViewModel.java`

**Integración BPMN:**
- Service Task: `handleTimeout` en `rag-evaluation-v1.bpmn`
- Boundary Event: `timeoutEvent` (30 minutos)

---

## 📐 ESPECIFICACIONES TÉCNICAS

### Estructura de Directorios

```
src/main/webapp/console/platform/evaluation/rag-evaluation/
├── overview.zul                    # Listado de evaluaciones
├── page.zul                        # Crear/Editar evaluación completa
├── retrieval.zul                   # Evaluación de retrieval
├── answer.zul                      # Evaluación de respuesta
├── policy-validation.zul           # Validación de políticas
├── benchmarking.zul                # Benchmarking de sistema
├── human-validation-queue.zul      # Cola de validaciones humanas
└── human-validation-detail.zul    # Detalle de validación humana

src/main/webapp/console/bpmn/
├── rag-evaluation-review-form.zul  # Revisión manual BPMN
├── rag-alert-form.zul              # Alerta de riesgo
└── rag-evaluation-timeout-form.zul # Manejo de timeout
```

### ViewModels Requeridos

```
src/main/java/com/codeflowx/governance/ui/rag/
├── RAGEvaluationOverviewViewModel.java
├── RAGEvaluationViewModel.java
├── RAGRetrievalEvaluationViewModel.java
├── RAGAnswerEvaluationViewModel.java
├── RAGPolicyValidationViewModel.java
├── RAGBenchmarkingViewModel.java
├── RAGHumanValidationQueueViewModel.java
├── RAGHumanValidationDetailViewModel.java
├── RAGEvaluationReviewFormViewModel.java
├── RAGAlertFormViewModel.java
└── RAGEvaluationTimeoutFormViewModel.java
```

### Servicios Requeridos

```
src/main/java/com/codeflowx/govern/service/rag/
├── RAGEvaluationService.java       # Servicio principal con persistencia
└── RAGPolicyValidationService.java # Servicio de validación de políticas
```

---

## 🎨 COMPONENTES UI REUTILIZABLES

### 1. Score Indicator

**Componente:** `rag-score-indicator.zul`

**Propósito:** Mostrar score con indicador visual de color.

**Props:**
- `score`: Double (0-100)
- `label`: String
- `showProgressBar`: Boolean

**Colores:**
- Verde: >= 80
- Amarillo: 60-79
- Naranja: 40-59
- Rojo: < 40

### 2. Risk Level Badge

**Componente:** `rag-risk-badge.zul`

**Propósito:** Mostrar nivel de riesgo con badge de color.

**Props:**
- `riskLevel`: String (LOW/MEDIUM/HIGH/CRITICAL)

**Colores:**
- LOW: Verde
- MEDIUM: Amarillo
- HIGH: Naranja
- CRITICAL: Rojo

### 3. Metric Progress Bar

**Componente:** `rag-metric-progress.zul`

**Propósito:** Mostrar métrica (0.0-1.0) con barra de progreso.

**Props:**
- `value`: Double (0.0-1.0)
- `label`: String
- `showValue`: Boolean

### 4. Retrieved Documents List

**Componente:** `rag-retrieved-docs-list.zul`

**Propósito:** Lista editable de documentos recuperados.

**Props:**
- `documents`: List<RetrievedDocumentItem>
- `editable`: Boolean
- `onAdd`: Command
- `onRemove`: Command

---

## 📋 FLUJOS DE USUARIO

### Flujo 1: Evaluación Completa del Pipeline

1. Usuario navega a "RAG Evaluation > Nueva Evaluación"
2. Completa formulario: Query, Documentos, Respuesta, Ground Truth
3. Click en "Evaluar Pipeline Completo"
4. Sistema muestra resultados: Scores, Métricas, Risk Level
5. Usuario puede "Guardar Evaluación" o "Limpiar"

### Flujo 2: Validación de Políticas

1. Usuario navega a "RAG Evaluation > Validar Políticas"
2. Completa formulario: Query, Chunks, Políticas
3. Click en "Validar Políticas"
4. Sistema muestra: Validation Passed, Alignment Score, Recomendaciones
5. Si no pasa validación, usuario ajusta chunks o políticas

### Flujo 3: Validación Humana de Alucinaciones

1. Sistema detecta alucinación (score > 0.5)
2. Automáticamente encola para validación humana
3. Usuario navega a "RAG Evaluation > Cola de Validaciones"
4. Selecciona validación pendiente
5. Revisa query, respuesta y análisis de alucinación
6. Completa formulario: Is Hallucination, Confidence, Notes
7. Click en "Enviar Validación"
8. Sistema actualiza estadísticas y aprende del feedback

### Flujo 4: Benchmarking

1. Usuario navega a "RAG Evaluation > Benchmarking"
2. Configura: System Config, Dataset, Custom Queries
3. Opcionalmente habilita datasets externos o conexión RAG real
4. Click en "Ejecutar Benchmark"
5. Sistema muestra resultados: Métricas agregadas, Comparación

---

## 🔗 INTEGRACIÓN CON CLIENTE JAVA

### Inyección del Cliente

Todos los ViewModels deben inyectar el servicio que usa el cliente:

```java
@WireVariable
private RAGEvaluationService ragEvaluationService;
```

El servicio internamente usa:

```java
@Autowired
private AIGovernanceClient aiGovernanceClient;
```

### Ejemplo de Llamada

```java
// En ViewModel
RAGFullPipelineRequest request = RAGFullPipelineRequest.builder()
    .query(query)
    .retrievedDocs(convertToRetrievedDocuments(retrievedDocs))
    .generatedAnswer(generatedAnswer)
    .groundTruth(groundTruth)
    .build();

RAGFullPipelineResponse response = ragEvaluationService
    .evaluateAndSave(request, clientId);
```

---

## 📚 REFERENCIAS

- **Cliente Java:** `codeflowx.govern.nocode.client/RAGEvaluationClient.java`
- **Guía de Uso:** `codeflowx.govern.nocode.client/RAG_CLIENT_USAGE.md`
- **Documentación Funcional:** `docs/funcional/evaluation/`
- **Proceso BPMN:** `codeflowx.govern.workflow.lib/src/main/resources/processes/aios/rag-evaluation-v1.bpmn`
- **Auditoría RAG:** `docs/compliance/auditoria/AUDITORIA_005_EVALUACION_RAG.md`

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### Pantallas Principales
- [ ] `overview.zul` - Listado de evaluaciones
- [ ] `page.zul` - Crear/Editar evaluación completa
- [ ] `retrieval.zul` - Evaluación de retrieval
- [ ] `answer.zul` - Evaluación de respuesta
- [ ] `policy-validation.zul` - Validación de políticas
- [ ] `benchmarking.zul` - Benchmarking
- [ ] `human-validation-queue.zul` - Cola de validaciones
- [ ] `human-validation-detail.zul` - Detalle de validación

### Pantallas BPMN
- [ ] `rag-evaluation-review-form.zul` - Revisión manual
- [ ] `rag-alert-form.zul` - Alerta de riesgo
- [ ] `rag-evaluation-timeout-form.zul` - Manejo de timeout

### ViewModels
- [ ] Todos los ViewModels implementados
- [ ] Integración con `RAGEvaluationService`
- [ ] Manejo de errores implementado
- [ ] Validación de formularios

### Componentes Reutilizables
- [ ] `rag-score-indicator.zul`
- [ ] `rag-risk-badge.zul`
- [ ] `rag-metric-progress.zul`
- [ ] `rag-retrieved-docs-list.zul`

### Servicios
- [ ] `RAGEvaluationService` con persistencia
- [ ] `RAGPolicyValidationService` con persistencia
- [ ] Integración con `RAGEvaluationClient`

---

**Versión:** 1.0  
**Última actualización:** Diciembre 2024  
**Autor:** CodeFlowX Governance Team

