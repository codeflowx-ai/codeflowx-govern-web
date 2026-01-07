# Guía: Pruebas A/B y LLMs as Judge con Sistema de Experimentos y Runs

**Fecha:** Enero 2025
**Objetivo:** Documentar cómo ejecutar pruebas A/B y usar LLMs como juez para evaluar Prompts, Modelos, Agentes y RAG usando el sistema de experimentos y runs.

---

## 📋 ÍNDICE

1. [Conceptos Generales](#conceptos-generales)
2. [Arquitectura de Pruebas A/B](#arquitectura-de-pruebas-ab)
3. [Arquitectura LLMs as Judge](#arquitectura-llms-as-judge)
4. [Flujos de Ejecución por Entidad](#flujos-de-ejecución-por-entidad)
5. [Implementación Backend](#implementación-backend)
6. [Implementación Frontend](#implementación-frontend)
7. [Ejemplos Prácticos](#ejemplos-prácticos)

---

## 🎯 CONCEPTOS GENERALES

### Pruebas A/B

Las pruebas A/B comparan dos versiones de una entidad (Prompt, Modelo, Agente, RAG) usando el mismo conjunto de datos de prueba para determinar cuál versión tiene mejor rendimiento.

**Características:**
- Se ejecutan dos runs en paralelo o secuencialmente
- Ambos runs usan el mismo dataset de prueba
- Los resultados se comparan usando métricas específicas
- Se crea un run "parent" que agrupa ambos runs

### LLMs as Judge

Un modelo LLM actúa como juez para evaluar la calidad de las respuestas/salidas de otra entidad.

**Características:**
- Se usa un modelo LLM (ej: GPT-4, Claude) como evaluador
- El juez evalúa según criterios específicos (calidad, precisión, cumplimiento, etc.)
- Genera scores y feedback estructurado
- Puede usarse en pruebas A/B para comparar objetivamente

---

## 🏗️ ARQUITECTURA DE PRUEBAS A/B

### Estructura de Datos

```json
{
  "experiment": {
    "name": "A/B Test: Prompt v1 vs v2",
    "entityType": "PROMPT",
    "description": "Comparación de dos versiones de prompt"
  },
  "runA": {
    "name": "Prompt A - v1.0",
    "entityId": 123,
    "parameters": { "version": "1.0" },
    "metrics": { "accuracy": 0.85, "latency": 1.2 }
  },
  "runB": {
    "name": "Prompt B - v2.0",
    "entityId": 124,
    "parameters": { "version": "2.0" },
    "metrics": { "accuracy": 0.92, "latency": 1.5 }
  },
  "comparison": {
    "winner": "runB",
    "improvement": {
      "accuracy": "+8.2%",
      "latency": "+25%"
    },
    "statisticalSignificance": 0.95
  }
}
```

### Flujo de Ejecución A/B

```
1. Usuario selecciona tipo de test A/B
2. Selecciona entidad A y entidad B
3. Sube dataset de prueba (CSV)
4. Sistema crea:
   - 1 Experiment (contenedor)
   - 2 Runs (uno para A, uno para B)
   - 1 Run de comparación (opcional)
5. Ejecuta ambos runs en paralelo
6. Compara resultados
7. Genera reporte de comparación
```

---

## 🎓 ARQUITECTURA LLMs AS JUDGE

### Estructura de Datos

```json
{
  "run": {
    "name": "Prompt Test with GPT-4 Judge",
    "entityType": "PROMPT",
    "entityId": 123,
    "parameters": {
      "judgeModelId": 456,
      "judgeModelName": "GPT-4",
      "judgeCriteria": "quality",
      "evaluationPrompt": "Evalúa la calidad de esta respuesta..."
    },
    "metrics": {
      "judgeScore": 0.87,
      "judgeFeedback": "La respuesta es clara y precisa...",
      "criteriaScores": {
        "clarity": 0.9,
        "accuracy": 0.85,
        "completeness": 0.88
      }
    }
  }
}
```

### Flujo de Ejecución LLM Judge

```
1. Usuario selecciona entidad a evaluar
2. Selecciona modelo juez (LLM)
3. Configura criterios de evaluación
4. Sube dataset de prueba (CSV con inputs)
5. Sistema:
   - Ejecuta la entidad con cada input
   - Para cada respuesta, envía al juez:
     * Input original
     * Respuesta generada
     * Criterios de evaluación
   - El juez genera score y feedback
6. Agrega métricas al run
7. Genera reporte con scores y feedback
```

---

## 🔄 FLUJOS DE EJECUCIÓN POR ENTIDAD

### 1. PROMPTS - Prueba A/B con LLM Judge

**Caso de Uso:** Comparar dos prompts diferentes para la misma tarea.

**Flujo:**

```typescript
// 1. Crear experimento
POST /api/v1/governance/experiments
{
  "govExperimentName": "A/B Test: Customer Service Prompts",
  "govExperimentEntityType": "PROMPT",
  "govExperimentDescription": "Comparar prompt v1 vs v2 para atención al cliente"
}

// 2. Crear Run A
POST /api/v1/governance/experiments/{experimentId}/runs
{
  "govRunName": "Prompt A - v1.0",
  "govRunType": "AB_TEST",
  "govRunEntityType": "PROMPT",
  "govRunEntityId": 123,
  "govRunParameters": {
    "version": "1.0",
    "temperature": 0.7,
    "maxTokens": 500
  }
}

// 3. Crear Run B
POST /api/v1/governance/experiments/{experimentId}/runs
{
  "govRunName": "Prompt B - v2.0",
  "govRunType": "AB_TEST",
  "govRunEntityType": "PROMPT",
  "govRunEntityId": 124,
  "govRunParameters": {
    "version": "2.0",
    "temperature": 0.7,
    "maxTokens": 500
  }
}

// 4. Ejecutar ambos runs con LLM Judge
POST /api/v1/governance/testing/execute/ab-test
{
  "experimentId": 1,
  "runAId": 10,
  "runBId": 11,
  "testDataset": "base64_csv_data",
  "judgeModelId": 456, // GPT-4
  "judgeCriteria": "quality",
  "evaluationPrompt": "Evalúa la calidad de estas respuestas de atención al cliente..."
}

// 5. El backend ejecuta:
//    - Para cada input en el dataset:
//      a. Ejecuta Prompt A → respuestaA
//      b. Ejecuta Prompt B → respuestaB
//      c. Envía ambas al juez: "Compara respuestaA vs respuestaB"
//      d. Juez retorna: { winner: "A"|"B"|"TIE", scoreA: 0.85, scoreB: 0.92, feedback: "..." }
//    - Agrega métricas a cada run
//    - Crea run de comparación

// 6. Obtener resultados
GET /api/v1/governance/runs/compare?runIds=10,11
```

**Métricas Generadas:**

```json
{
  "runA": {
    "metrics": {
      "averageJudgeScore": 0.82,
      "winRate": 0.35,
      "averageLatency": 1.2,
      "totalEvaluations": 100
    }
  },
  "runB": {
    "metrics": {
      "averageJudgeScore": 0.89,
      "winRate": 0.65,
      "averageLatency": 1.3,
      "totalEvaluations": 100
    }
  },
  "comparison": {
    "winner": "runB",
    "statisticalSignificance": 0.95,
    "improvement": {
      "judgeScore": "+8.5%",
      "winRate": "+30%"
    }
  }
}
```

### 2. MODELOS - Prueba A/B

**Caso de Uso:** Comparar dos modelos en el mismo dataset.

**Flujo:**

```typescript
// 1. Crear experimento
POST /api/v1/governance/experiments
{
  "govExperimentName": "A/B Test: GPT-3.5 vs GPT-4",
  "govExperimentEntityType": "MODEL"
}

// 2. Ejecutar A/B test
POST /api/v1/governance/testing/execute/ab-test
{
  "experimentId": 2,
  "entityType": "MODEL",
  "entityAId": 789, // GPT-3.5
  "entityBId": 790, // GPT-4
  "testDataset": "base64_csv_data",
  "evaluationMetrics": ["accuracy", "latency", "cost"]
}

// 3. El backend:
//    - Ejecuta modelo A en cada fila del dataset
//    - Ejecuta modelo B en cada fila del dataset
//    - Calcula métricas para cada modelo
//    - Compara resultados
```

**Métricas Generadas:**

```json
{
  "runA": {
    "metrics": {
      "accuracy": 0.87,
      "precision": 0.85,
      "recall": 0.89,
      "f1Score": 0.87,
      "averageLatency": 0.8,
      "totalCost": 0.05
    }
  },
  "runB": {
    "metrics": {
      "accuracy": 0.94,
      "precision": 0.92,
      "recall": 0.95,
      "f1Score": 0.93,
      "averageLatency": 1.2,
      "totalCost": 0.15
    }
  }
}
```

### 3. AGENTES - Prueba A/B con LLM Judge

**Caso de Uso:** Comparar dos agentes en escenarios de uso real.

**Flujo:**

```typescript
// 1. Crear experimento
POST /api/v1/governance/experiments
{
  "govExperimentName": "A/B Test: Agent v1 vs v2",
  "govExperimentEntityType": "AGENT"
}

// 2. Ejecutar con LLM Judge
POST /api/v1/governance/testing/execute/ab-test
{
  "experimentId": 3,
  "entityType": "AGENT",
  "entityAId": 111,
  "entityBId": 112,
  "testScenarios": "base64_csv_data", // Escenarios de prueba
  "judgeModelId": 456,
  "judgeCriteria": "behavior_quality",
  "evaluationPrompt": "Evalúa el comportamiento del agente en este escenario..."
}

// 3. El backend:
//    - Para cada escenario:
//      a. Ejecuta Agente A → accionesA, respuestasA
//      b. Ejecuta Agente B → accionesB, respuestasB
//      c. Envía al juez: escenario + accionesA + accionesB
//      d. Juez evalúa: calidad, cumplimiento, eficiencia
```

### 4. RAG - Prueba A/B con LLM Judge

**Caso de Uso:** Comparar dos configuraciones RAG (diferentes retrievers, chunk sizes, etc.).

**Flujo:**

```typescript
// 1. Crear experimento
POST /api/v1/governance/experiments
{
  "govExperimentName": "A/B Test: RAG Config A vs B",
  "govExperimentEntityType": "RAG"
}

// 2. Ejecutar con LLM Judge
POST /api/v1/governance/testing/execute/ab-test
{
  "experimentId": 4,
  "entityType": "RAG",
  "entityAId": 201, // RAG con chunk size 500
  "entityBId": 202, // RAG con chunk size 1000
  "testQueries": "base64_csv_data",
  "judgeModelId": 456,
  "judgeCriteria": "accuracy",
  "evaluationPrompt": "Evalúa la precisión y relevancia de esta respuesta RAG..."
}

// 3. El backend:
//    - Para cada query:
//      a. RAG A: retrieve + generate → respuestaA
//      b. RAG B: retrieve + generate → respuestaB
//      c. Juez evalúa: precisión, relevancia, completitud
//      d. También mide: retrieval accuracy, latency
```

---

## 🔧 IMPLEMENTACIÓN BACKEND

### 1. Endpoint para Ejecutar A/B Test

```java
@PostMapping("/api/v1/governance/testing/execute/ab-test")
@Operation(summary = "Ejecutar prueba A/B entre dos entidades")
public Mono<ResponseEntity<ABTestResultDto>> executeABTest(
    @RequestBody ABTestExecutionDto dto,
    Principal principal) {

    return Mono.fromCallable(() -> {
        // 1. Validar entidades
        validateEntities(dto.getEntityType(), dto.getEntityAId(), dto.getEntityBId());

        // 2. Crear o obtener experimento
        GovernanceExperiment experiment = getOrCreateExperiment(dto);

        // 3. Crear runs A y B
        GovernanceRun runA = createRun(experiment, dto.getEntityAId(), "A");
        GovernanceRun runB = createRun(experiment, dto.getEntityBId(), "B");

        // 4. Procesar dataset
        List<TestCase> testCases = parseDataset(dto.getTestDataset());

        // 5. Ejecutar tests en paralelo
        List<TestResult> resultsA = executeEntity(runA, testCases);
        List<TestResult> resultsB = executeEntity(runB, testCases);

        // 6. Si hay juez, evaluar con LLM
        if (dto.getJudgeModelId() != null) {
            resultsA = evaluateWithJudge(resultsA, dto.getJudgeModelId(), dto.getJudgeCriteria());
            resultsB = evaluateWithJudge(resultsB, dto.getJudgeModelId(), dto.getJudgeCriteria());
        }

        // 7. Calcular métricas
        Map<String, Object> metricsA = calculateMetrics(resultsA);
        Map<String, Object> metricsB = calculateMetrics(resultsB);

        // 8. Actualizar runs con métricas
        updateRunMetrics(runA, metricsA);
        updateRunMetrics(runB, metricsB);

        // 9. Comparar resultados
        ABTestComparison comparison = compareResults(resultsA, resultsB);

        // 10. Crear run de comparación (opcional)
        GovernanceRun comparisonRun = createComparisonRun(experiment, runA, runB, comparison);

        return new ABTestResultDto(runA, runB, comparison, comparisonRun);
    })
    .subscribeOn(Schedulers.boundedElastic())
    .map(ResponseEntity::ok);
}
```

### 2. Servicio para Evaluar con LLM Judge

```java
@Service
public class LLMJudgeService {

    @Autowired
    private AIGovernanceClient aiGovernanceClient;

    public List<JudgeEvaluation> evaluateWithJudge(
            List<TestResult> results,
            Long judgeModelId,
            String criteria) {

        Model judgeModel = modelRepository.findById(judgeModelId)
            .orElseThrow(() -> new IllegalArgumentException("Judge model not found"));

        List<JudgeEvaluation> evaluations = new ArrayList<>();

        for (TestResult result : results) {
            // Construir prompt de evaluación
            String evaluationPrompt = buildEvaluationPrompt(
                result.getInput(),
                result.getOutput(),
                criteria
            );

            // Llamar al modelo juez
            String judgeResponse = aiGovernanceClient.models()
                .generateText(judgeModel, evaluationPrompt);

            // Parsear respuesta del juez
            JudgeEvaluation evaluation = parseJudgeResponse(judgeResponse);
            evaluations.add(evaluation);
        }

        return evaluations;
    }

    private String buildEvaluationPrompt(String input, String output, String criteria) {
        return String.format(
            "Eres un evaluador experto. Evalúa la siguiente respuesta según el criterio: %s\n\n" +
            "Input: %s\n" +
            "Output: %s\n\n" +
            "Proporciona:\n" +
            "1. Score (0-1)\n" +
            "2. Feedback detallado\n" +
            "3. Aspectos positivos\n" +
            "4. Aspectos a mejorar\n\n" +
            "Formato JSON: {\"score\": 0.85, \"feedback\": \"...\", \"positives\": [...], \"improvements\": [...]}",
            criteria, input, output
        );
    }

    private JudgeEvaluation parseJudgeResponse(String response) {
        // Parsear JSON de respuesta del juez
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(response, JudgeEvaluation.class);
        } catch (Exception e) {
            log.error("Error parsing judge response", e);
            throw new RuntimeException("Invalid judge response format", e);
        }
    }
}
```

### 3. Servicio para Comparar Resultados A/B

```java
@Service
public class ABTestComparisonService {

    public ABTestComparison compareResults(
            List<TestResult> resultsA,
            List<TestResult> resultsB) {

        // Calcular métricas agregadas
        double avgScoreA = resultsA.stream()
            .mapToDouble(r -> r.getJudgeScore())
            .average()
            .orElse(0.0);

        double avgScoreB = resultsB.stream()
            .mapToDouble(r -> r.getJudgeScore())
            .average()
            .orElse(0.0);

        // Contar victorias
        long winsA = resultsA.stream()
            .filter(r -> r.getJudgeScore() > getCorrespondingScore(r, resultsB))
            .count();

        long winsB = resultsB.stream()
            .filter(r -> r.getJudgeScore() > getCorrespondingScore(r, resultsA))
            .count();

        // Calcular significancia estadística
        double statisticalSignificance = calculateStatisticalSignificance(
            resultsA, resultsB
        );

        // Determinar ganador
        String winner = avgScoreB > avgScoreA ? "B" : "A";
        if (Math.abs(avgScoreB - avgScoreA) < 0.01) {
            winner = "TIE";
        }

        return ABTestComparison.builder()
            .winner(winner)
            .averageScoreA(avgScoreA)
            .averageScoreB(avgScoreB)
            .winsA(winsA)
            .winsB(winsB)
            .statisticalSignificance(statisticalSignificance)
            .improvement(calculateImprovement(avgScoreA, avgScoreB))
            .build();
    }
}
```

---

## 🎨 IMPLEMENTACIÓN FRONTEND

### Componente de Ejecución A/B

El frontend ya tiene los formularios básicos. Necesitamos mejorar la visualización de resultados:

```typescript
// components/ABTestResults.tsx
export function ABTestResults({ runA, runB, comparison }) {
  return (
    <div className="space-y-6">
      {/* Métricas Comparativas */}
      <Card>
        <CardHeader>
          <CardTitle>Comparación de Resultados</CardTitle>
        </CardHeader>
        <CardBody>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <h3>Run A</h3>
              <MetricDisplay metrics={runA.metrics} />
            </div>
            <div>
              <h3>Run B</h3>
              <MetricDisplay metrics={runB.metrics} />
            </div>
          </div>

          {/* Ganador */}
          <div className="mt-4 p-4 bg-green-50 rounded">
            <p className="font-bold">Ganador: {comparison.winner}</p>
            <p>Mejora: {comparison.improvement}</p>
            <p>Significancia estadística: {comparison.statisticalSignificance}</p>
          </div>
        </CardBody>
      </Card>

      {/* Gráficos Comparativos */}
      <Card>
        <CardHeader>
          <CardTitle>Visualización Comparativa</CardTitle>
        </CardHeader>
        <CardBody>
          <ComparisonChart runA={runA} runB={runB} />
        </CardBody>
      </Card>
    </div>
  );
}
```

---

## 📝 EJEMPLOS PRÁCTICOS

### Ejemplo 1: A/B Test de Prompts con GPT-4 como Juez

**Escenario:** Comparar dos prompts para generación de resúmenes.

**Dataset de prueba (CSV):**
```csv
input,expected_output
"Artículo sobre IA...", "Resumen esperado..."
"Documento técnico...", "Resumen esperado..."
```

**Ejecución:**
1. Usuario va a `/governance/testing/execute/prompt`
2. Selecciona "Prueba A/B"
3. Ingresa:
   - Prompt A ID: 123
   - Prompt B ID: 124
   - Modelo Juez: GPT-4 (ID: 456)
   - Criterio: "quality"
   - Sube CSV con artículos
4. Sistema ejecuta y genera comparación

### Ejemplo 2: A/B Test de Modelos

**Escenario:** Comparar GPT-3.5 vs GPT-4 en tarea de clasificación.

**Ejecución:**
1. Usuario va a `/governance/testing/execute/model`
2. Selecciona "Prueba A/B"
3. Ingresa:
   - Modelo A: GPT-3.5 (ID: 789)
   - Modelo B: GPT-4 (ID: 790)
   - Sube CSV con datos de prueba
4. Sistema ejecuta ambos modelos y compara métricas

### Ejemplo 3: RAG con LLM Judge

**Escenario:** Evaluar calidad de respuestas RAG usando Claude como juez.

**Ejecución:**
1. Usuario va a `/governance/testing/execute/rag`
2. Selecciona "Test de Precisión"
3. Ingresa:
   - RAG ID: 201
   - Modelo Juez: Claude (ID: 457)
   - Criterio: "accuracy"
   - Sube CSV con queries
4. Sistema ejecuta RAG y juez evalúa cada respuesta

---

## 🔗 INTEGRACIÓN CON MICROSERVICIOS

### Servicio de Evaluación con LLM Judge

**Nuevo microservicio Python:** `llm-judge-service`

```python
# llm-judge-service/main.py
from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()

class JudgeRequest(BaseModel):
    judge_model_id: str
    input_text: str
    output_text: str
    criteria: str
    evaluation_prompt: str

class JudgeResponse(BaseModel):
    score: float
    feedback: str
    positives: list[str]
    improvements: list[str]

@app.post("/evaluate")
async def evaluate_with_judge(request: JudgeRequest):
    # Llamar al modelo juez (OpenAI, Anthropic, etc.)
    response = await call_llm(
        model_id=request.judge_model_id,
        prompt=build_prompt(request)
    )

    # Parsear respuesta
    evaluation = parse_evaluation(response)

    return JudgeResponse(**evaluation)
```

---

## 📊 MÉTRICAS Y REPORTES

### Métricas Generadas

**Para Pruebas A/B:**
- Win rate (porcentaje de victorias)
- Average score difference
- Statistical significance
- Latency comparison
- Cost comparison

**Para LLM Judge:**
- Average judge score
- Score distribution
- Feedback summary
- Criteria-specific scores

### Visualizaciones

- Gráficos de barras comparativos
- Distribución de scores
- Heatmaps de criterios
- Timeline de ejecución

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### Backend
- [ ] Crear endpoint `/api/v1/governance/testing/execute/ab-test`
- [ ] Crear servicio `LLMJudgeService`
- [ ] Crear servicio `ABTestComparisonService`
- [ ] Integrar con microservicio de LLM Judge
- [ ] Actualizar `GovernanceRunBusinessService` para soportar runs A/B

### Frontend
- [ ] Mejorar formularios de A/B test
- [ ] Crear componente `ABTestResults`
- [ ] Crear componente `JudgeEvaluation`
- [ ] Agregar visualizaciones comparativas

### Testing
- [ ] Tests unitarios para comparación A/B
- [ ] Tests de integración con LLM Judge
- [ ] Tests end-to-end del flujo completo

---

## 🚀 PRÓXIMOS PASOS

1. **Implementar microservicio LLM Judge** (Python)
2. **Crear endpoints backend** para ejecución A/B
3. **Mejorar UI** para visualización de resultados
4. **Agregar soporte para batch evaluation** (evaluar múltiples casos en paralelo)
5. **Implementar caching** de evaluaciones del juez para optimizar costos

---

**Nota:** Este documento es una guía de implementación. Los detalles técnicos pueden variar según las decisiones de arquitectura específicas del proyecto.
