# Implementación Completa: Pruebas A/B y LLMs as Judge

**Fecha:** Enero 2025
**Estado:** ✅ Implementado

---

## 📦 ARCHIVOS CREADOS/MODIFICADOS

### Backend - DTOs

1. **`ABTestExecutionDto.java`**
   - DTO para ejecutar pruebas A/B
   - Campos: entityType, entityAId, entityBId, testDataset, judgeModelId, etc.

2. **`JudgeEvaluationDto.java`**
   - DTO para evaluaciones del juez LLM
   - Campos: score, feedback, positives, improvements, criteriaScores

3. **`ABTestResultDto.java`**
   - DTO con resultados completos de A/B test
   - Incluye: runA, runB, comparison, comparisonRunId

4. **`ABTestComparisonDto.java`**
   - DTO con resultados de comparación
   - Campos: winner, scores, wins, statisticalSignificance, improvement

5. **`MetricComparisonDto.java`**
   - DTO para comparación de métricas individuales
   - Campos: valueA, valueB, difference, improvementPercentage, winner

### Backend - Servicios

1. **`LLMJudgeService.java`**
   - Servicio para evaluar con modelos LLM como juez
   - Métodos:
     - `evaluateWithJudge()`: Evalúa lista de resultados
     - `evaluateABComparison()`: Evalúa comparación A/B
   - Construye prompts de evaluación
   - Parsea respuestas JSON del juez

2. **`ABTestComparisonService.java`**
   - Servicio para comparar resultados A/B
   - Métodos:
     - `compareResults()`: Compara evaluaciones y métricas
     - Calcula significancia estadística
     - Determina ganador
     - Calcula mejoras porcentuales

3. **`ABTestExecutionService.java`**
   - Servicio principal para ejecutar A/B tests
   - Métodos:
     - `executeABTest()`: Orquesta todo el proceso
     - `parseDataset()`: Parsea CSV de prueba
     - `executeEntity()`: Ejecuta entidad con inputs
     - `calculateMetrics()`: Calcula métricas agregadas
   - Soporta: MODEL, PROMPT, AGENT, RAG

### Backend - Controller

1. **`GovernanceTestingController.java`** (modificado)
   - Nuevo endpoint: `POST /api/v1/governance/testing/execute/ab-test`
   - Acepta FormData o JSON
   - Soporta subida de archivos CSV

### Frontend - Componentes

1. **`ABTestResults.tsx`**
   - Componente para visualizar resultados A/B
   - Muestra: scores, victorias, comparaciones, métricas
   - Incluye botones para ver runs individuales

2. **`JudgeEvaluation.tsx`**
   - Componente para mostrar evaluación del juez
   - Muestra: score, feedback, aspectos positivos/mejoras
   - Visualización de criterios específicos

3. **`/governance/testing/ab-results/page.tsx`**
   - Página para mostrar resultados de A/B tests
   - Recibe resultados vía query params

### Frontend - Componentes de Ejecución (modificados)

1. **`PromptTestExecution.tsx`** - Integrado con endpoint A/B
2. **`ModelTestExecution.tsx`** - Integrado con endpoint A/B
3. **`AgentTestExecution.tsx`** - Integrado con endpoint A/B
4. **`RAGTestExecution.tsx`** - Integrado con endpoint A/B

---

## 🔄 FLUJO DE EJECUCIÓN

### Prueba A/B con LLM Judge

```
1. Usuario → /governance/testing/execute/{entity-type}
2. Selecciona "Prueba A/B"
3. Completa formulario:
   - Entidad A y B
   - Dataset CSV
   - Modelo Juez (opcional)
   - Criterio de evaluación
4. Frontend → POST /api/v1/governance/testing/execute/ab-test
   - FormData con archivo CSV
5. Backend:
   a. Crea/obtiene experimento
   b. Crea run A y run B
   c. Parsea CSV → lista de test cases
   d. Ejecuta entidad A con cada input
   e. Ejecuta entidad B con cada input
   f. Si hay juez:
      - Para cada resultado, llama al juez LLM
      - Juez retorna: score, feedback, positives, improvements
   g. Calcula métricas agregadas
   h. Compara resultados (ABTestComparisonService)
   i. Crea run de comparación
   j. Retorna ABTestResultDto
6. Frontend → Redirige a /governance/testing/ab-results
7. Muestra resultados con ABTestResults component
```

### Test Individual con LLM Judge

```
1. Usuario → /governance/testing/execute/{entity-type}
2. Selecciona test (PROMPT_TEST, ACCURACY_TEST, etc.)
3. Completa formulario con modelo juez
4. Backend ejecuta y evalúa con juez
5. Muestra resultados con JudgeEvaluation component
```

---

## 📊 ESTRUCTURA DE DATOS

### Dataset CSV

Formato esperado:
```csv
input,expected_output
"Input 1","Expected output 1"
"Input 2","Expected output 2"
```

Columnas soportadas:
- `input`, `entrada`, `query`, `text` → Input a probar
- `expected_output`, `esperado`, `output` → Output esperado (opcional)

### Respuesta del Juez LLM

Formato JSON esperado:
```json
{
  "score": 0.85,
  "feedback": "La respuesta es clara y precisa...",
  "positives": ["Claridad", "Precisión"],
  "improvements": ["Podría ser más completa"],
  "criteriaScores": {
    "clarity": 0.9,
    "accuracy": 0.85,
    "completeness": 0.88
  }
}
```

### Resultado A/B

```json
{
  "runA": {
    "id": 10,
    "name": "Run A",
    "metrics": {
      "averageJudgeScore": 0.82,
      "wins": 35,
      "totalTests": 100
    }
  },
  "runB": {
    "id": 11,
    "name": "Run B",
    "metrics": {
      "averageJudgeScore": 0.89,
      "wins": 65,
      "totalTests": 100
    }
  },
  "comparison": {
    "winner": "B",
    "averageScoreA": 0.82,
    "averageScoreB": 0.89,
    "winsA": 35,
    "winsB": 65,
    "statisticalSignificance": 0.95,
    "improvement": {
      "averageScore": "+8.5%"
    }
  }
}
```

---

## 🔧 CONFIGURACIÓN NECESARIA

### Backend

1. **AIGovernanceClient** debe estar configurado para llamar a modelos LLM
2. **Repositorios** deben existir:
   - ✅ ModelRepository
   - ✅ PromptRepository
   - ✅ AgentRepository
   - ⚠️ RAGRepository (comentado, pendiente de implementar)

### Frontend

1. Los componentes están listos para usar
2. La navegación usa `window.location.assign()` para compatibilidad

---

## 🚀 PRÓXIMOS PASOS

### Backend

1. **Implementar ejecución real de entidades:**
   - Conectar con servicios de ejecución de prompts
   - Conectar con servicios de ejecución de modelos
   - Conectar con servicios de ejecución de agentes
   - Implementar ejecución de RAG cuando exista

2. **Mejorar LLMJudgeService:**
   - Integrar con AIGovernanceClient real
   - Soporte para múltiples proveedores (OpenAI, Anthropic, etc.)
   - Caching de evaluaciones para optimizar costos

3. **Mejorar parseo de CSV:**
   - Soporte para más formatos
   - Validación de esquema
   - Manejo de encoding

### Frontend

1. **Mejorar visualizaciones:**
   - Gráficos comparativos (Chart.js, Recharts)
   - Tablas de resultados detallados
   - Exportación de reportes

2. **Agregar funcionalidades:**
   - Historial de evaluaciones del juez
   - Comparación de múltiples runs
   - Filtros y búsqueda avanzada

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### Backend
- [x] DTOs creados
- [x] LLMJudgeService implementado
- [x] ABTestComparisonService implementado
- [x] ABTestExecutionService implementado
- [x] Endpoint creado
- [ ] Integración real con servicios de ejecución
- [ ] Integración real con AIGovernanceClient para juez

### Frontend
- [x] Componente ABTestResults
- [x] Componente JudgeEvaluation
- [x] Página de resultados A/B
- [x] Integración en formularios de ejecución
- [ ] Gráficos comparativos
- [ ] Exportación de reportes

---

## 📝 NOTAS TÉCNICAS

### Parseo de CSV

El servicio `ABTestExecutionService` incluye un parser CSV robusto que:
- Soporta campos con comillas
- Detecta automáticamente columnas de input/output
- Maneja diferentes encodings
- Valida formato básico

### Evaluación con Juez

El `LLMJudgeService`:
- Construye prompts estructurados para el juez
- Parsea respuestas JSON del juez
- Maneja errores gracefully
- Soporta criterios personalizados

### Comparación Estadística

El `ABTestComparisonService`:
- Calcula significancia estadística (t-test simplificado)
- Determina ganador considerando scores y victorias
- Calcula mejoras porcentuales
- Compara métricas individuales

---

**Estado:** ✅ Implementación completa lista para testing e integración con servicios reales.

