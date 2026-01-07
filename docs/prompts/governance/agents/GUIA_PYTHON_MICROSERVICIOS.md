# 🐍 GUÍA PYTHON PARA MICROSERVICIOS DE EVALUACIÓN

**Versión:** 1.0
**Fecha:** Enero 2025
**Audiencia:** Desarrolladores Python, AI Engineers, Agentes de IA
**Proyecto:** CodeFlowX AI Governance Platform

---

## 🎯 PROPÓSITO

Esta guía define cómo los microservicios Python de evaluación (sesgos, LLM, RAG, agentes, etc.) deben aplicar algoritmos configurados o crear nuevos microservicios cuando sea necesario. Los microservicios deben ser flexibles y permitir la configuración de diferentes algoritmos según el contexto.

**IMPORTANTE:** Los microservicios Python **NO tienen acceso a bases de datos**. Toda la información necesaria (datos, configuración, algoritmo) debe venir en el request del endpoint. El backend Java es responsable de consultar la base de datos y enviar toda la información necesaria.

---

## 📊 ARQUITECTURA DE MICROSERVICIOS

### Estructura General

```
Backend Java (Business Service)
    ↓ Consulta configuración desde PostgreSQL
    ↓ Prepara request con TODA la información necesaria
    ↓
Cliente Java (AIGovernanceClient)
    ↓ HTTP Request con datos completos
    ↓
Microservicio Python (FastAPI)
    ↓ Recibe TODA la información en el request
    ↓ Aplica algoritmo especificado
    ↓
Resultado JSON
    ↓
Backend Java (persiste resultados en PostgreSQL)
```

**IMPORTANTE:** Los microservicios Python **NO tienen acceso a bases de datos**. Toda la información necesaria debe venir en el request del endpoint.

### Microservicios Disponibles

| Microservicio | Puerto | Cliente Java | Endpoints Base |
|---------------|--------|-------------|----------------|
| Bias Detection | 8001 | `BiasDetectionClient` | `/api/bias-analysis/**` |
| LLM Evaluation | 8002 | `LLMEvaluationClient` | `/api/llm/**` |
| Prompt Governance | 8003 | `PromptGovernanceClient` | `/api/prompt/**` |
| RAG Evaluation | 8004 | `RAGEvaluationClient` | `/api/rag/**` |
| Agent Monitoring | 8005 | `AgentMonitoringClient` | `/api/agent/**` |
| Model Wrapper | 8006 | `ModelWrapperClient` | `/api/models/**` |
| AI Interpreter | 8011 | `AIInterpreterClient` | `/api/interpret/**` |

---

## 🔧 CONFIGURACIÓN DE ALGORITMOS

### 1. Algoritmos Configurables

Cada microservicio debe soportar múltiples algoritmos configurables. **Toda la configuración debe venir en el request** del endpoint. El backend Java consulta la configuración desde PostgreSQL y la incluye en el request.

**Fuentes de configuración (en orden de prioridad):**

1. **Parámetro en el request (máxima prioridad):**
   ```python
   @app.post("/api/bias-analysis/analyze")
   async def analyze_bias(
       request: BiasAnalysisRequest,  # Contiene algorithm y toda la configuración
       algorithm: Optional[str] = None  # Opcional, override del request
   ):
       # El algoritmo viene en el request o como parámetro
       algorithm_to_use = algorithm or request.algorithm or request.config.get("algorithm")
   ```

2. **Variables de entorno (solo como fallback):**
   ```python
   DEFAULT_ALGORITHM = os.getenv("BIAS_DETECTION_ALGORITHM", "statistical_parity")
   # Solo usar si no viene en el request
   ```

**IMPORTANTE:** Los microservicios Python **NO consultan bases de datos**. El backend Java:
- Consulta la configuración desde PostgreSQL
- Incluye el algoritmo y configuración en el request
- El microservicio Python solo procesa lo que recibe

### 2. Registro de Algoritmos

Cada microservicio debe mantener un registro de algoritmos disponibles:

```python
AVAILABLE_ALGORITHMS = {
    "bias-detection": {
        "statistical_parity": StatisticalParityAlgorithm,
        "equalized_odds": EqualizedOddsAlgorithm,
        "demographic_parity": DemographicParityAlgorithm,
        "calibrated_equality": CalibratedEqualityAlgorithm,
        "individual_fairness": IndividualFairnessAlgorithm
    },
    "llm-evaluation": {
        "hallucination_detection": HallucinationDetectionAlgorithm,
        "toxicity_detection": ToxicityDetectionAlgorithm,
        "quality_scoring": QualityScoringAlgorithm,
        "bias_text_detection": BiasTextDetectionAlgorithm
    },
    "agent-monitoring": {
        "execution_analysis": ExecutionAnalysisAlgorithm,
        "reliability_evaluation": ReliabilityEvaluationAlgorithm,
        "cost_analysis": CostAnalysisAlgorithm,
        "loop_detection": LoopDetectionAlgorithm
    }
}
```

---

## 📝 IMPLEMENTACIÓN POR MICROSERVICIO

### 1. Bias Detection (Puerto 8001)

**Cliente Java:** `BiasDetectionClient`

**Algoritmos Disponibles:**

1. **Statistical Parity:**
   ```python
   class StatisticalParityAlgorithm:
       def analyze(self, data: pd.DataFrame, protected_attribute: str, target: str):
           # Calcular diferencia estadística entre grupos
           return {
               "statistical_parity_difference": float,
               "classification": str,  # LOW, MODERATE, HIGH
               "recommendations": List[str]
           }
   ```

2. **Equalized Odds:**
   ```python
   class EqualizedOddsAlgorithm:
       def analyze(self, data: pd.DataFrame, protected_attribute: str, target: str):
           # Calcular igualdad de oportunidades
           return {
               "equalized_odds_difference": float,
               "true_positive_rate_difference": float,
               "false_positive_rate_difference": float
           }
   ```

3. **Demographic Parity:**
   ```python
   class DemographicParityAlgorithm:
       def analyze(self, data: pd.DataFrame, protected_attribute: str, target: str):
           # Calcular paridad demográfica
           return {
               "demographic_parity_difference": float,
               "group_statistics": Dict[str, Dict]
           }
   ```

**Endpoint Principal:**
```python
@app.post("/api/bias-analysis/analyze")
async def analyze_bias(
    request: BiasAnalysisRequest,
    algorithm: Optional[str] = None
):
    """
    Analiza sesgo en datos o modelo.

    IMPORTANTE: Toda la información necesaria debe venir en el request.
    El backend Java consulta la configuración desde PostgreSQL y la incluye aquí.

    Args:
        request: BiasAnalysisRequest con:
            - data: Datos a analizar (DataFrame serializado o datos estructurados)
            - protected_attribute: Atributo protegido
            - target: Variable objetivo
            - algorithm: Algoritmo a usar (viene del backend Java)
            - config: Configuración adicional del algoritmo (viene del backend Java)
        algorithm: Algoritmo a usar (opcional, override del request)

    Returns:
        BiasAnalysisResponse con métricas de sesgo
    """
    # 1. Determinar algoritmo a usar (prioridad: parámetro > request > default)
    algorithm_name = (
        algorithm
        or request.algorithm
        or request.config.get("algorithm") if request.config else None
        or DEFAULT_ALGORITHM
    )

    # 2. Validar que el algoritmo existe
    if algorithm_name not in AVAILABLE_ALGORITHMS["bias-detection"]:
        raise ValueError(f"Algorithm {algorithm_name} not available")

    # 3. Instanciar algoritmo
    algorithm_class = AVAILABLE_ALGORITHMS["bias-detection"][algorithm_name]
    algorithm_instance = algorithm_class()

    # 4. Aplicar algoritmo con configuración del request
    algorithm_config = request.config or {}
    result = algorithm_instance.analyze(
        data=request.data,
        protected_attribute=request.protected_attribute,
        target=request.target,
        config=algorithm_config  # Configuración específica del algoritmo
    )

    # 5. Retornar respuesta
    return BiasAnalysisResponse(
        algorithm_used=algorithm_name,
        metrics=result,
        timestamp=datetime.now()
    )
```

**Schema del Request (ejemplo):**
```python
class BiasAnalysisRequest(BaseModel):
    """Request para análisis de sesgo - TODA la información viene aquí"""
    data: Union[pd.DataFrame, Dict, List[Dict]]  # Datos completos a analizar
    protected_attribute: str  # Atributo protegido
    target: str  # Variable objetivo
    algorithm: Optional[str] = None  # Algoritmo especificado por backend Java
    config: Optional[Dict] = None  # Configuración del algoritmo desde BD
    context: Optional[str] = None  # Contexto (agent-evaluation, etc.)
    metadata: Optional[Dict] = None  # Metadatos adicionales
```

---

### 2. LLM Evaluation (Puerto 8002)

**Cliente Java:** `LLMEvaluationClient`

**Algoritmos Disponibles:**

1. **Hallucination Detection:**
   ```python
   class HallucinationDetectionAlgorithm:
       def evaluate(self, prompt: str, response: str, reference_context: str = None):
           # Detectar alucinaciones usando múltiples métodos
           return {
               "hallucination_detected": bool,
               "score": float,  # 0-1, donde 1 es más alucinación
               "confidence": float,
               "evidence": List[str]
           }
   ```

2. **Toxicity Detection:**
   ```python
   class ToxicityDetectionAlgorithm:
       def evaluate(self, text: str):
           # Detectar toxicidad usando modelos pre-entrenados
           return {
               "toxicity_detected": bool,
               "toxicity_score": float,  # 0-1
               "categories": Dict[str, float],  # hate, threat, etc.
               "severity": str  # LOW, MODERATE, HIGH, SEVERE
           }
   ```

3. **Quality Scoring:**
   ```python
   class QualityScoringAlgorithm:
       def evaluate(self, prompt: str, response: str, context: Dict = None):
           # Evaluar calidad general de la respuesta
           return {
               "overall_score": float,  # 0-1
               "coherence": float,
               "relevance": float,
               "fluency": float,
               "completeness": float
           }
   ```

**Endpoint Principal:**
```python
@app.post("/api/llm/evaluate-hallucination")
async def evaluate_hallucination(
    request: HallucinationRequest,
    algorithm: Optional[str] = None
):
    """
    Evalúa alucinaciones en respuesta de LLM.

    IMPORTANTE: Toda la información viene en el request.
    El backend Java incluye el algoritmo y configuración.

    Args:
        request: HallucinationRequest con:
            - prompt: Prompt original
            - response: Respuesta del LLM
            - reference_context: Contexto de referencia (opcional)
            - algorithm: Algoritmo a usar (viene del backend Java)
            - config: Configuración del algoritmo (viene del backend Java)
        algorithm: Algoritmo a usar (opcional, override del request)

    Returns:
        HallucinationResponse con resultados
    """
    # Determinar algoritmo (prioridad: parámetro > request > default)
    algorithm_name = (
        algorithm
        or request.algorithm
        or request.config.get("algorithm") if request.config else None
        or DEFAULT_ALGORITHM
    )

    if algorithm_name not in AVAILABLE_ALGORITHMS["llm-evaluation"]:
        raise ValueError(f"Algorithm {algorithm_name} not available")

    algorithm_class = AVAILABLE_ALGORITHMS["llm-evaluation"][algorithm_name]
    algorithm_instance = algorithm_class()

    # Aplicar algoritmo con configuración del request
    algorithm_config = request.config or {}
    result = algorithm_instance.evaluate(
        prompt=request.prompt,
        response=request.response,
        reference_context=request.reference_context,
        config=algorithm_config
    )

    return HallucinationResponse(
        algorithm_used=algorithm_name,
        hallucination_detected=result["hallucination_detected"],
        score=result["score"],
        confidence=result.get("confidence", 0.0),
        evidence=result.get("evidence", [])
    )
```

---

### 3. Agent Monitoring (Puerto 8005)

**Cliente Java:** `AgentMonitoringClient`

**Algoritmos Disponibles:**

1. **Execution Analysis:**
   ```python
   class ExecutionAnalysisAlgorithm:
       def analyze(self, execution_trace: List[Dict], final_output: str, expected_output: str = None):
           # Analizar ejecución del agente
           return {
               "success_rate": float,
               "efficiency_score": float,
               "steps_analyzed": int,
               "issues_detected": List[Dict],
               "recommendations": List[str]
           }
   ```

2. **Reliability Evaluation:**
   ```python
   class ReliabilityEvaluationAlgorithm:
       def evaluate(self, executions: List[Dict]):
           # Evaluar confiabilidad basada en múltiples ejecuciones
           return {
               "reliability_score": float,  # 0-1
               "consistency_score": float,
               "failure_rate": float,
               "average_success_rate": float
           }
   ```

3. **Cost Analysis:**
   ```python
   class CostAnalysisAlgorithm:
       def analyze(self, execution_trace: List[Dict], cost_per_token: Dict[str, float]):
           # Analizar costos de ejecución
           return {
               "total_cost": float,
               "cost_breakdown": Dict[str, float],
               "tokens_used": Dict[str, int],
               "cost_per_step": List[Dict]
           }
   ```

4. **Loop Detection:**
   ```python
   class LoopDetectionAlgorithm:
       def detect(self, execution_trace: List[Dict], max_iterations: int = 10):
           # Detectar loops infinitos
           return {
               "loop_detected": bool,
               "loop_start_step": int,
               "loop_length": int,
               "severity": str  # LOW, MODERATE, HIGH
           }
   ```

**Endpoint Principal:**
```python
@app.post("/api/agent/analyze-execution")
async def analyze_execution(
    request: AgentExecutionRequest,
    algorithm: Optional[str] = None
):
    """
    Analiza ejecución individual de agente.

    Args:
        request: AgentExecutionRequest con execution_trace y final_output
        algorithm: Algoritmo a usar (opcional)

    Returns:
        AgentExecutionResponse con análisis
    """
    algorithm_name = algorithm or request.algorithm or get_default_algorithm("agent-monitoring", "execution")

    if algorithm_name not in AVAILABLE_ALGORITHMS["agent-monitoring"]:
        raise ValueError(f"Algorithm {algorithm_name} not available")

    algorithm_class = AVAILABLE_ALGORITHMS["agent-monitoring"][algorithm_name]
    algorithm_instance = algorithm_class()

    result = algorithm_instance.analyze(
        execution_trace=request.execution_trace,
        final_output=request.final_output,
        expected_output=request.expected_output
    )

    return AgentExecutionResponse(
        agent_id=request.agent_id,
        algorithm_used=algorithm_name,
        success_rate=result.get("success_rate", 0.0),
        efficiency_score=result.get("efficiency_score", 0.0),
        issues_detected=result.get("issues_detected", []),
        recommendations=result.get("recommendations", [])
    )
```

---

## 🆕 CREACIÓN DE NUEVOS MICROSERVICIOS

### Cuándo Crear un Nuevo Microservicio

Crear un nuevo microservicio cuando:

1. **Nuevo dominio funcional:**
   - No existe microservicio que cubra el dominio
   - El dominio requiere algoritmos específicos
   - El dominio tiene requisitos de escalabilidad diferentes

2. **Separación de responsabilidades:**
   - El nuevo servicio tiene responsabilidades claramente distintas
   - No encaja bien en servicios existentes

3. **Requisitos técnicos diferentes:**
   - Requiere librerías o frameworks diferentes
   - Requiere recursos computacionales diferentes (GPU, etc.)

### Template de Nuevo Microservicio

```python
"""
Template para nuevo microservicio de evaluación
"""
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import Optional, List, Dict
import os
from datetime import datetime

app = FastAPI(
    title="New Evaluation Service",
    description="Microservicio para evaluación de X",
    version="1.0.0"
)

# Configuración
DEFAULT_ALGORITHM = os.getenv("NEW_SERVICE_ALGORITHM", "default_algorithm")
GATEWAY_URL = os.getenv("GATEWAY_URL", "http://api-leka-govern:8000")

# Algoritmos disponibles
AVAILABLE_ALGORITHMS = {
    "default_algorithm": DefaultAlgorithm,
    "advanced_algorithm": AdvancedAlgorithm
}

# Modelos de datos
class EvaluationRequest(BaseModel):
    """Request para evaluación"""
    data: Dict
    algorithm: Optional[str] = None
    context: Optional[Dict] = None

class EvaluationResponse(BaseModel):
    """Response de evaluación"""
    algorithm_used: str
    result: Dict
    timestamp: datetime
    confidence: Optional[float] = None

# Endpoints
@app.get("/health")
async def health():
    """Health check"""
    return {"status": "healthy", "service": "new-evaluation-service"}

@app.post("/api/new-service/evaluate")
async def evaluate(
    request: EvaluationRequest,
    algorithm: Optional[str] = None
):
    """
    Endpoint principal de evaluación

    Args:
        request: EvaluationRequest con datos
        algorithm: Algoritmo a usar (opcional)

    Returns:
        EvaluationResponse con resultados
    """
    try:
        # 1. Determinar algoritmo
        algorithm_name = algorithm or request.algorithm or get_default_algorithm("new-service")

        # 2. Validar algoritmo
        if algorithm_name not in AVAILABLE_ALGORITHMS:
            raise ValueError(f"Algorithm {algorithm_name} not available")

        # 3. Instanciar y ejecutar algoritmo
        algorithm_class = AVAILABLE_ALGORITHMS[algorithm_name]
        algorithm_instance = algorithm_class()
        result = algorithm_instance.evaluate(request.data, request.context)

        # 4. Retornar respuesta
        return EvaluationResponse(
            algorithm_used=algorithm_name,
            result=result,
            timestamp=datetime.now(),
            confidence=result.get("confidence")
        )

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

def get_default_algorithm(service: str) -> str:
    """
    Obtiene algoritmo por defecto desde variables de entorno.

    NOTA: NO se consulta base de datos. El backend Java consulta la BD
    y envía el algoritmo en el request. Esta función solo es fallback.
    """
    return DEFAULT_ALGORITHM

# Clases de algoritmos
class DefaultAlgorithm:
    def evaluate(self, data: Dict, context: Dict = None) -> Dict:
        """Implementación del algoritmo por defecto"""
        # Implementar lógica
        return {"result": "default"}

class AdvancedAlgorithm:
    def evaluate(self, data: Dict, context: Dict = None) -> Dict:
        """Implementación del algoritmo avanzado"""
        # Implementar lógica
        return {"result": "advanced"}
```

### Pasos para Crear un Nuevo Microservicio

1. **Crear estructura del proyecto:**
   ```
   new-evaluation-service/
   ├── app/
   │   ├── __init__.py
   │   ├── main.py
   │   ├── algorithms/
   │   │   ├── __init__.py
   │   │   └── default_algorithm.py
   │   ├── models/
   │   │   ├── __init__.py
   │   │   └── request_response.py
   │   └── config/
   │       └── settings.py
   ├── requirements.txt
   ├── Dockerfile
   └── README.md
   ```

2. **Configurar Docker:**
   ```dockerfile
   FROM python:3.11-slim

   WORKDIR /app

   COPY requirements.txt .
   RUN pip install --no-cache-dir -r requirements.txt

   COPY app/ ./app/

   CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "800X"]
   ```

3. **Registrar en Gateway:**
   - Agregar ruta en Spring Cloud Gateway
   - Configurar puerto y nombre del servicio
   - Configurar health check

4. **Crear Cliente Java:**
   - Crear `NewEvaluationClient` en `codeflowx.govern.nocode.client`
   - Agregar métodos al `AIGovernanceClient`
   - Crear DTOs para request/response

5. **Documentar:**
   - Crear guía de uso similar a las existentes
   - Documentar algoritmos disponibles
   - Documentar configuración

---

## 🔄 INTEGRACIÓN CON BACKEND JAVA

### Flujo de Configuración

**IMPORTANTE:** Los microservicios Python **NO acceden a bases de datos**. El flujo es:

1. **Backend Java consulta configuración:**
   ```java
   // En AgentBusinessService o similar
   AlgorithmConfig config = algorithmConfigRepository.findByServiceAndContext(
       "bias-detection",
       "agent-evaluation"
   );
   ```

2. **Backend Java prepara request:**
   ```java
   BiasAnalysisRequest request = BiasAnalysisRequest.builder()
       .data(data)  // Datos completos
       .protectedAttribute("gender")
       .target("approved")
       .algorithm(config.getAlgorithmName())  // Algoritmo desde BD
       .config(config.getConfiguration())  // Configuración desde BD
       .context("agent-evaluation")
       .build();
   ```

3. **Backend Java llama al microservicio:**
   ```java
   BiasAnalysisResponse response = aiGovernanceClient.biasDetection()
       .analyzeBias(request);
   ```

4. **Microservicio Python procesa:**
   - Recibe TODO en el request
   - Aplica el algoritmo especificado
   - Retorna resultado

5. **Backend Java persiste resultado:**
   ```java
   // Persiste en PostgreSQL
   biasResultRepository.save(toEntity(response));
   ```

### Tabla de Configuración (solo en Backend Java)

La tabla `cor_algorithm_config` existe solo en PostgreSQL y es consultada por el backend Java:

```sql
CREATE TABLE cor_algorithm_config (
    id BIGSERIAL PRIMARY KEY,
    service VARCHAR(100) NOT NULL,  -- bias-detection, llm-evaluation, etc.
    context VARCHAR(100),            -- agent-evaluation, model-evaluation, etc.
    algorithm_name VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT true,
    priority INTEGER DEFAULT 0,      -- Mayor prioridad = más preferido
    configuration JSONB,             -- Configuración específica del algoritmo
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_algorithm_config_service ON cor_algorithm_config(service, context, is_active);
```

**El microservicio Python NO consulta esta tabla.** El backend Java la consulta y envía la información en el request.

---

## 📋 CHECKLIST DE IMPLEMENTACIÓN

### Para Cada Microservicio Existente

- [ ] Soporta múltiples algoritmos configurables
- [ ] Recibe algoritmo y configuración en el request (NO consulta BD)
- [ ] Permite override del algoritmo como parámetro del endpoint
- [ ] Tiene fallback a algoritmo por defecto (solo desde env vars)
- [ ] Documenta algoritmos disponibles
- [ ] Valida que el algoritmo existe antes de usarlo
- [ ] Retorna información del algoritmo usado en la respuesta
- [ ] Maneja errores de algoritmo gracefully
- [ ] **NO tiene acceso a bases de datos**
- [ ] **Recibe TODA la información necesaria en el request**

### Para Nuevos Microservicios

- [ ] Sigue el template proporcionado
- [ ] Implementa al menos un algoritmo
- [ ] **NO incluye acceso a bases de datos**
- [ ] **Define schema completo del request con TODA la información necesaria**
- [ ] Configura health checks
- [ ] Registra en Spring Cloud Gateway
- [ ] Crea cliente Java correspondiente
- [ ] Documenta en guía de uso
- [ ] Backend Java configura algoritmo en base de datos y lo envía en el request

---

## 🚨 MEJORES PRÁCTICAS

1. **Siempre validar algoritmo:**
   - Verificar que el algoritmo existe antes de instanciarlo
   - Retornar error claro si el algoritmo no está disponible

2. **Configuración flexible:**
   - **Recibir configuración completa en el request** (backend Java la consulta desde BD)
   - Usar variables de entorno solo como fallback último recurso
   - Permitir override del algoritmo como parámetro del endpoint

3. **Logging detallado:**
   - Registrar algoritmo usado
   - Registrar tiempo de ejecución
   - Registrar errores con contexto

4. **Manejo de errores:**
   - Capturar excepciones de algoritmos
   - Retornar errores HTTP apropiados
   - No exponer detalles internos

5. **Performance:**
   - Cachear instancias de algoritmos cuando sea posible
   - Usar async/await para operaciones I/O
   - **NO hacer consultas a base de datos** (todo viene en el request)

---

## 📚 REFERENCIAS

- **Cliente Java:** `codeflowx.govern.nocode.client`
- **Endpoints Mapping:** `ENDPOINTS_MAPPING.md`
- **Guías de Uso:** `*_CLIENT_GUIDE.md`
- **FastAPI Documentation:** https://fastapi.tiangolo.com/
- **Spring Cloud Gateway:** https://spring.io/projects/spring-cloud-gateway

---

**Última Actualización:** Enero 2025
**Versión:** 1.0
**Estado:** ✅ Operativo
