# HallucinationDetectionService

**Ubicación:** `com.codeflowx.govern.business.rag.HallucinationDetectionService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Gestiona la detección de alucinaciones en respuestas generadas por RAG, cumpliendo con el Art. 13 del EU AI Act que requiere robustez, seguridad y precisión.

---

## 🎯 Responsabilidades

- Detectar alucinaciones usando microservicio Python especializado (LLM Evaluation)
- Validar precisión factual de respuestas RAG
- Fallback a validación básica Java si el microservicio no está disponible
- Integración con múltiples métodos de detección (SelfCheckGPT, FactScore, Entailment)

---

## 🔌 Integración con Microservicio

**Microservicio:** LLM Evaluation (Puerto 8002)
**Endpoint:** `POST /api/hallucination-detection/detect`
**Cliente Java:** `AIGovernanceClient.llmEvaluation().detectAdvancedHallucination()`

**Métodos de Detección:**
- **SelfCheckGPT:** Auto-verificación mediante generación múltiple
- **FactScore:** Validación de hechos específicos contra contexto
- **Entailment:** Verificación de coherencia lógica (NLI)

---

## 📚 API Pública

### `detectHallucinations(String text, List<String> context, String query)`

Detecta alucinaciones en texto generado por RAG.

**Parámetros:**
- `text`: Texto de la respuesta generada
- `context`: Chunks de contexto usados para generar respuesta
- `query`: Query original (opcional)

**Retorna:** `HallucinationDetectionResult` con:
- `isHallucination`: Boolean - Si se detectó alucinación
- `overallScore`: Double (0.0-1.0) - Score agregado (mayor = más alucinaciones)
- `confidence`: Double (0.0-1.0) - Confianza de la detección
- `requiresHumanReview`: Boolean - Si requiere revisión humana
- `riskLevel`: String - "LOW", "MEDIUM", "HIGH", "CRITICAL"
- `methodScores`: Lista de scores por método
- `detectedSentences`: Frases detectadas como problemáticas
- `recommendations`: Recomendaciones para mejorar
- `fallbackMode`: Boolean - Si se usó modo fallback

**Ejemplo:**
```java
@WireVariable
private HallucinationDetectionService hallucinationDetectionService;

List<String> contextChunks = Arrays.asList("Chunk 1...", "Chunk 2...");
HallucinationDetectionResult result = hallucinationDetectionService.detectHallucinations(
    responseText, contextChunks, query
);

if (result.isHallucination()) {
    log.warn("Alucinación detectada: score={}, risk={}",
        result.getOverallScore(), result.getRiskLevel());
}
```

---

### `validateFactualAccuracy(String text, List<String> context)`

Valida precisión factual de respuesta contra contexto.

**Parámetros:**
- `text`: Texto de la respuesta
- `context`: Chunks de contexto

**Retorna:** `FactualAccuracyResult` con:
- `isAccurate`: Boolean - Si la respuesta es precisa
- `accuracyScore`: Double (0.0-1.0) - Score de precisión
- `confidence`: Double (0.0-1.0) - Confianza de la validación
- `requiresReview`: Boolean - Si requiere revisión

**Ejemplo:**
```java
FactualAccuracyResult accuracy = hallucinationDetectionService.validateFactualAccuracy(
    responseText, contextChunks
);

if (!accuracy.isAccurate()) {
    log.warn("Precisión factual baja: score={}", accuracy.getAccuracyScore());
}
```

---

## 🔄 Modo Fallback

Si el microservicio Python no está disponible, el servicio usa un fallback básico que:
- Retorna score neutro (0.5)
- Siempre requiere revisión humana
- Genera recomendaciones básicas
- Marca `fallbackMode = true`

**Log:**
```
WARN: Hallucination detection fallback mode: Python service not available. Human review required.
```

---

## 📖 Referencias

- **Art. 13 EU AI Act:** Robustez, Seguridad y Precisión
- **Prompt:** INC-005-002
- **Microservicio:** LLM Evaluation (puerto 8002)
- **Cliente Java:** `LLMEvaluationClient.detectAdvancedHallucination()`
- **ViewModels:** RAGEvaluationViewModel, RAGAnswerViewModel

---

**Última actualización:** 25 de noviembre de 2025
