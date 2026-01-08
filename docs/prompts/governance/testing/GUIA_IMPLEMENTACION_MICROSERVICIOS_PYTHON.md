# Guía de Implementación: Microservicios Python para Testing

**Versión:** 1.0
**Fecha:** Enero 2025
**Audiencia:** Backend Developers, Python Engineers, DevOps Teams

---

## 📋 ÍNDICE

1. [Introducción](#introducción)
2. [Arquitectura General](#arquitectura-general)
3. [Microservicios Requeridos](#microservicios-requeridos)
4. [Microservicio 1: Testing Execution Service](#microservicio-1-testing-execution-service)
5. [Microservicio 2: LLM Judge Service](#microservicio-2-llm-judge-service)
6. [Microservicio 3: AB Test Comparison Service](#microservicio-3-ab-test-comparison-service)
7. [Cliente Java](#cliente-java)
8. [Despliegue y Configuración](#despliegue-y-configuración)
9. [Testing y Validación](#testing-y-validación)
10. [Mejores Prácticas](#mejores-prácticas)

---

## 🎯 INTRODUCCIÓN

Esta guía documenta la implementación de los microservicios Python necesarios para el módulo de Testing y Validación de CodeFlowX Governance. Estos microservicios se integran con el backend Java mediante el API Gateway y proporcionan funcionalidades de ejecución de pruebas, evaluación con LLMs como juez, y comparación de resultados A/B.

### Objetivos

- Implementar microservicios Python siguiendo el patrón arquitectónico existente
- Integrar con el cliente Java `AIGovernanceClient`
- Proporcionar endpoints REST para ejecución de pruebas
- Soportar pruebas A/B y evaluación con LLMs como juez
- Mantener consistencia con microservicios existentes

### Microservicios Existentes de Referencia

| Microservicio | Puerto | Endpoints Base | Cliente Java |
|--------------|--------|---------------|-------------|
| LLM Evaluation | 8002 | `/api/llm/**` | `LLMEvaluationClient` |
| Prompt Governance | 8003 | `/api/prompt/**` | `PromptGovernanceClient` |
| RAG Evaluation | 8004 | `/api/rag/**` | `RAGEvaluationClient` |
| Agent Monitoring | 8005 | `/api/agent/**` | `AgentMonitoringClient` |
| Model Wrapper | 8006 | `/api/models/**` | `ModelWrapperClient` |
| AI Interpreter | 8011 | `/api/interpret/**` | `AIInterpreterClient` |

---

## 🏗️ ARQUITECTURA GENERAL

### Diagrama de Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│                    Backend Java (Spring Boot)                 │
│  ┌────────────────────────────────────────────────────────┐  │
│  │  GovernanceTestingController                            │  │
│  │  - ABTestExecutionService                               │  │
│  │  - LLMJudgeService                                      │  │
│  │  - ABTestComparisonService                              │  │
│  └──────────────┬─────────────────────────────────────────┘  │
│                 │                                              │
│                 │ AIGovernanceClient (WebClient)              │
│                 │                                              │
└─────────────────┼─────────────────────────────────────────────┘
                  │
                  │ HTTP REST
                  │
┌─────────────────▼─────────────────────────────────────────────┐
│              API Gateway (Spring Cloud Gateway)               │
│                    Puerto: 8000                               │
│  ┌────────────────────────────────────────────────────────┐  │
│  │  Routing Rules:                                         │  │
│  │  /api/testing/** → Testing Execution Service (8012)    │  │
│  │  /api/judge/** → LLM Judge Service (8013)              │  │
│  │  /api/abtest/** → AB Test Comparison Service (8014)    │  │
│  └────────────────────────────────────────────────────────┘  │
└─────────────────┬─────────────────────────────────────────────┘
                  │
        ┌─────────┼─────────┐
        │         │         │
┌───────▼───┐ ┌───▼────┐ ┌──▼──────────┐
│ Testing   │ │ LLM    │ │ AB Test     │
│ Execution │ │ Judge  │ │ Comparison  │
│ Service   │ │ Service│ │ Service     │
│ (8012)    │ │ (8013) │ │ (8014)      │
└───────────┘ └────────┘ └─────────────┘
```

### Flujo de Datos

1. **Frontend/Backend Java** → Llama a `AIGovernanceClient`
2. **AIGovernanceClient** → Envía request HTTP al API Gateway (puerto 8000)
3. **API Gateway** → Enruta al microservicio Python correspondiente
4. **Microservicio Python** → Procesa request y retorna JSON
5. **AIGovernanceClient** → Deserializa JSON a objetos Java tipados
6. **Backend Java** → Retorna resultado al frontend

---

## 📦 MICROSERVICIOS REQUERIDOS

### Resumen de Microservicios

| Microservicio | Puerto | Responsabilidad | Endpoints Principales |
|--------------|--------|----------------|---------------------|
| **Testing Execution** | 8012 | Ejecutar entidades (prompts, modelos, agentes, RAG) con datasets | `/api/testing/execute/**` |
| **LLM Judge** | 8013 | Evaluar resultados usando LLMs como juez | `/api/judge/evaluate/**` |
| **AB Test Comparison** | 8014 | Comparar resultados de pruebas A/B | `/api/abtest/compare/**` |

---

## 🔧 MICROSERVICIO 1: TESTING EXECUTION SERVICE

### Descripción

Microservicio responsable de ejecutar entidades de IA (Prompts, Modelos, Agentes, RAG) con datasets de prueba. Procesa archivos CSV, ejecuta cada caso de prueba, y retorna resultados estructurados.

### Tecnologías

- **Framework:** FastAPI (Python 3.11+)
- **Puerto:** 8012
- **Base Path:** `/api/testing`
- **Base de Datos:** No requiere (stateless)

### Estructura del Proyecto

```
testing-execution-service/
├── app/
│   ├── __init__.py
│   ├── main.py                 # FastAPI app
│   ├── config.py               # Configuración
│   ├── models/
│   │   ├── __init__.py
│   │   ├── request.py          # Request models
│   │   └── response.py         # Response models
│   ├── services/
│   │   ├── __init__.py
│   │   ├── prompt_executor.py  # Ejecutar prompts
│   │   ├── model_executor.py   # Ejecutar modelos
│   │   ├── agent_executor.py   # Ejecutar agentes
│   │   ├── rag_executor.py     # Ejecutar RAG
│   │   └── csv_parser.py       # Parsear CSV
│   ├── routers/
│   │   ├── __init__.py
│   │   └── execution.py       # Endpoints de ejecución
│   └── utils/
│       ├── __init__.py
│       └── validators.py      # Validadores
├── tests/
│   ├── __init__.py
│   ├── test_execution.py
│   └── test_csv_parser.py
├── requirements.txt
├── Dockerfile
└── README.md
```

### Endpoints

#### 1. Ejecutar Prompt

```http
POST /api/testing/execute/prompt
Content-Type: application/json
```

**Request:**
```json
{
  "promptId": 123,
  "testCases": [
    {
      "input": "¿Qué es la IA?",
      "expectedOutput": null
    },
    {
      "input": "Explica machine learning",
      "expectedOutput": "Machine Learning es..."
    }
  ],
  "parameters": {
    "temperature": 0.7,
    "maxTokens": 500
  },
  "modelId": 456
}
```

**Response:**
```json
{
  "executionId": "exec_123456",
  "results": [
    {
      "input": "¿Qué es la IA?",
      "output": "La Inteligencia Artificial es...",
      "executionTime": 1.23,
      "tokensUsed": 150,
      "success": true
    },
    {
      "input": "Explica machine learning",
      "output": "Machine Learning es...",
      "executionTime": 1.45,
      "tokensUsed": 180,
      "success": true
    }
  ],
  "summary": {
    "totalTests": 2,
    "successfulTests": 2,
    "failedTests": 0,
    "averageExecutionTime": 1.34,
    "totalTokensUsed": 330
  }
}
```

#### 2. Ejecutar Modelo

```http
POST /api/testing/execute/model
Content-Type: application/json
```

**Request:**
```json
{
  "modelId": 789,
  "testCases": [
    {
      "input": "Clasifica este texto: 'Me encanta este producto'",
      "expectedOutput": "positive"
    }
  ],
  "parameters": {
    "temperature": 0.0,
    "topP": 0.9
  }
}
```

**Response:** Similar a ejecutar prompt

#### 3. Ejecutar Agente

```http
POST /api/testing/execute/agent
Content-Type: application/json
```

**Request:**
```json
{
  "agentId": 321,
  "testCases": [
    {
      "input": "Responde: ¿Cuál es el clima en Madrid?",
      "expectedOutput": null
    }
  ],
  "parameters": {
    "maxIterations": 5,
    "timeout": 30
  }
}
```

**Response:** Similar a ejecutar prompt

#### 4. Ejecutar RAG

```http
POST /api/testing/execute/rag
Content-Type: application/json
```

**Request:**
```json
{
  "ragId": 654,
  "testCases": [
    {
      "input": "¿Qué documentos mencionan machine learning?",
      "expectedOutput": null
    }
  ],
  "parameters": {
    "topK": 5,
    "similarityThreshold": 0.7
  }
}
```

**Response:** Similar a ejecutar prompt

#### 5. Parsear CSV

```http
POST /api/testing/parse-csv
Content-Type: multipart/form-data
```

**Request:**
```
file: <archivo.csv>
```

**Response:**
```json
{
  "testCases": [
    {
      "input": "¿Qué es la IA?",
      "expectedOutput": "La Inteligencia Artificial es..."
    }
  ],
  "totalRows": 100,
  "columns": ["input", "expected_output"]
}
```

### Implementación de Servicios

#### `prompt_executor.py`

```python
from typing import List, Dict, Any
import asyncio
from app.models.request import PromptExecutionRequest, TestCase
from app.models.response import ExecutionResult, ExecutionSummary

class PromptExecutor:
    def __init__(self, model_service_client):
        self.model_service = model_service_client

    async def execute_prompt(
        self,
        request: PromptExecutionRequest
    ) -> Dict[str, Any]:
        """
        Ejecuta un prompt con múltiples casos de prueba.
        """
        results = []

        for test_case in request.testCases:
            try:
                # Llamar al servicio de modelos para ejecutar el prompt
                response = await self.model_service.generate_text(
                    model_id=request.modelId,
                    prompt_id=request.promptId,
                    input_text=test_case.input,
                    parameters=request.parameters
                )

                result = ExecutionResult(
                    input=test_case.input,
                    output=response.text,
                    executionTime=response.execution_time,
                    tokensUsed=response.tokens_used,
                    success=True
                )
                results.append(result)

            except Exception as e:
                result = ExecutionResult(
                    input=test_case.input,
                    output=f"ERROR: {str(e)}",
                    executionTime=0.0,
                    tokensUsed=0,
                    success=False
                )
                results.append(result)

        # Calcular resumen
        summary = self._calculate_summary(results)

        return {
            "executionId": f"exec_{hash(str(request))}",
            "results": [r.dict() for r in results],
            "summary": summary.dict()
        }

    def _calculate_summary(self, results: List[ExecutionResult]) -> ExecutionSummary:
        successful = [r for r in results if r.success]
        return ExecutionSummary(
            totalTests=len(results),
            successfulTests=len(successful),
            failedTests=len(results) - len(successful),
            averageExecutionTime=sum(r.executionTime for r in successful) / len(successful) if successful else 0.0,
            totalTokensUsed=sum(r.tokensUsed for r in successful)
        )
```

#### `csv_parser.py`

```python
import csv
import io
from typing import List, Dict, Any
from app.models.request import TestCase

class CSVParser:
    def parse_csv(self, csv_content: str) -> List[TestCase]:
        """
        Parsea contenido CSV y retorna lista de TestCase.
        """
        test_cases = []

        # Detectar encoding
        try:
            csv_content = csv_content.encode('utf-8').decode('utf-8')
        except:
            csv_content = csv_content.encode('latin-1').decode('latin-1')

        # Parsear CSV
        reader = csv.DictReader(io.StringIO(csv_content))

        # Detectar columnas de input/output
        input_col = None
        output_col = None

        for col in reader.fieldnames:
            col_lower = col.lower()
            if 'input' in col_lower or 'entrada' in col_lower or 'query' in col_lower:
                input_col = col
            if 'output' in col_lower or 'esperado' in col_lower or 'expected' in col_lower:
                output_col = col

        if not input_col:
            raise ValueError("CSV debe tener una columna 'input' o 'entrada'")

        # Crear test cases
        for row in reader:
            test_case = TestCase(
                input=row[input_col].strip(),
                expectedOutput=row[output_col].strip() if output_col and row[output_col] else None
            )
            test_cases.append(test_case)

        return test_cases
```

### Integración con Servicios Existentes

El servicio debe integrarse con:
- **Model Wrapper Service** (puerto 8006) - Para ejecutar modelos
- **Prompt Governance Service** (puerto 8003) - Para obtener prompts
- **Agent Monitoring Service** (puerto 8005) - Para ejecutar agentes
- **RAG Evaluation Service** (puerto 8004) - Para ejecutar RAG

---

## 🎓 MICROSERVICIO 2: LLM JUDGE SERVICE

### Descripción

Microservicio responsable de evaluar resultados de pruebas usando modelos LLM como juez. Evalúa calidad, precisión, cumplimiento y otros criterios personalizados.

### Tecnologías

- **Framework:** FastAPI (Python 3.11+)
- **Puerto:** 8013
- **Base Path:** `/api/judge`
- **Dependencias:** Puede reutilizar lógica de `LLMEvaluationClient`

### Estructura del Proyecto

```
llm-judge-service/
├── app/
│   ├── __init__.py
│   ├── main.py
│   ├── config.py
│   ├── models/
│   │   ├── __init__.py
│   │   ├── request.py
│   │   └── response.py
│   ├── services/
│   │   ├── __init__.py
│   │   ├── judge_evaluator.py    # Evaluador principal
│   │   ├── prompt_builder.py     # Construir prompts de evaluación
│   │   └── response_parser.py   # Parsear respuestas del juez
│   ├── routers/
│   │   ├── __init__.py
│   │   └── judge.py
│   └── utils/
│       └── validators.py
├── tests/
├── requirements.txt
├── Dockerfile
└── README.md
```

### Endpoints

#### 1. Evaluar Resultado Individual

```http
POST /api/judge/evaluate
Content-Type: application/json
```

**Request:**
```json
{
  "input": "¿Qué es la IA?",
  "output": "La Inteligencia Artificial es una rama de la informática...",
  "judgeModelId": 456,
  "criteria": "quality",
  "evaluationPrompt": null,
  "referenceContext": null
}
```

**Response:**
```json
{
  "score": 0.85,
  "feedback": "La respuesta es clara y precisa, pero podría ser más completa en algunos aspectos técnicos.",
  "positives": [
    "Claridad en la definición",
    "Precisión técnica"
  ],
  "improvements": [
    "Podría incluir ejemplos prácticos",
    "Falta mencionar aplicaciones actuales"
  ],
  "criteriaScores": {
    "clarity": 0.9,
    "accuracy": 0.85,
    "completeness": 0.8
  },
  "input": "¿Qué es la IA?",
  "output": "La Inteligencia Artificial es una rama de la informática..."
}
```

#### 2. Evaluar Lote de Resultados

```http
POST /api/judge/evaluate-batch
Content-Type: application/json
```

**Request:**
```json
{
  "results": [
    {
      "input": "¿Qué es la IA?",
      "output": "La Inteligencia Artificial es..."
    },
    {
      "input": "Explica machine learning",
      "output": "Machine Learning es..."
    }
  ],
  "judgeModelId": 456,
  "criteria": "quality",
  "evaluationPrompt": null
}
```

**Response:**
```json
{
  "evaluations": [
    {
      "score": 0.85,
      "feedback": "...",
      "positives": [...],
      "improvements": [...]
    },
    {
      "score": 0.92,
      "feedback": "...",
      "positives": [...],
      "improvements": [...]
    }
  ],
  "summary": {
    "totalEvaluations": 2,
    "averageScore": 0.885,
    "minScore": 0.85,
    "maxScore": 0.92
  }
}
```

#### 3. Evaluar Comparación A/B

```http
POST /api/judge/evaluate-ab
Content-Type: application/json
```

**Request:**
```json
{
  "input": "¿Qué es la IA?",
  "outputA": "La Inteligencia Artificial es...",
  "outputB": "IA es una tecnología que permite...",
  "judgeModelId": 456,
  "criteria": "quality",
  "evaluationPrompt": null
}
```

**Response:**
```json
{
  "winner": "B",
  "scoreA": 0.85,
  "scoreB": 0.92,
  "feedback": "Ambas respuestas son correctas, pero la respuesta B es más concisa y directa.",
  "positivesA": ["Claridad técnica"],
  "positivesB": ["Concisión", "Claridad"],
  "improvementsA": ["Podría ser más concisa"],
  "improvementsB": ["Podría incluir más detalles técnicos"]
}
```

### Implementación

#### `judge_evaluator.py`

```python
from typing import List, Dict, Any, Optional
from app.models.request import JudgeEvaluationRequest, JudgeBatchRequest, JudgeABRequest
from app.models.response import JudgeEvaluation, JudgeBatchResponse, JudgeABResponse
from app.services.prompt_builder import PromptBuilder
from app.services.response_parser import ResponseParser

class JudgeEvaluator:
    def __init__(self, model_service_client):
        self.model_service = model_service_client
        self.prompt_builder = PromptBuilder()
        self.response_parser = ResponseParser()

    async def evaluate(
        self,
        request: JudgeEvaluationRequest
    ) -> JudgeEvaluation:
        """
        Evalúa un resultado individual con un LLM juez.
        """
        # Construir prompt de evaluación
        prompt = self.prompt_builder.build_evaluation_prompt(
            input_text=request.input,
            output_text=request.output,
            criteria=request.criteria,
            custom_prompt=request.evaluationPrompt,
            reference_context=request.referenceContext
        )

        # Llamar al modelo juez
        response = await self.model_service.generate_text(
            model_id=request.judgeModelId,
            prompt_text=prompt,
            parameters={
                "temperature": 0.3,  # Baja temperatura para evaluación consistente
                "maxTokens": 1000
            }
        )

        # Parsear respuesta del juez
        evaluation = self.response_parser.parse_evaluation_response(
            response.text,
            request.input,
            request.output
        )

        return evaluation

    async def evaluate_batch(
        self,
        request: JudgeBatchRequest
    ) -> JudgeBatchResponse:
        """
        Evalúa un lote de resultados.
        """
        evaluations = []

        for result in request.results:
            eval_request = JudgeEvaluationRequest(
                input=result.input,
                output=result.output,
                judgeModelId=request.judgeModelId,
                criteria=request.criteria,
                evaluationPrompt=request.evaluationPrompt
            )

            evaluation = await self.evaluate(eval_request)
            evaluations.append(evaluation)

        # Calcular resumen
        scores = [e.score for e in evaluations if e.score is not None]
        summary = {
            "totalEvaluations": len(evaluations),
            "averageScore": sum(scores) / len(scores) if scores else 0.0,
            "minScore": min(scores) if scores else 0.0,
            "maxScore": max(scores) if scores else 0.0
        }

        return JudgeBatchResponse(
            evaluations=[e.dict() for e in evaluations],
            summary=summary
        )

    async def evaluate_ab(
        self,
        request: JudgeABRequest
    ) -> JudgeABResponse:
        """
        Evalúa comparación A/B.
        """
        # Construir prompt de comparación
        prompt = self.prompt_builder.build_ab_evaluation_prompt(
            input_text=request.input,
            output_a=request.outputA,
            output_b=request.outputB,
            criteria=request.criteria,
            custom_prompt=request.evaluationPrompt
        )

        # Llamar al modelo juez
        response = await self.model_service.generate_text(
            model_id=request.judgeModelId,
            prompt_text=prompt,
            parameters={
                "temperature": 0.3,
                "maxTokens": 1500
            }
        )

        # Parsear respuesta
        ab_evaluation = self.response_parser.parse_ab_response(
            response.text,
            request.input,
            request.outputA,
            request.outputB
        )

        return ab_evaluation
```

#### `prompt_builder.py`

```python
from typing import Optional

class PromptBuilder:
    def build_evaluation_prompt(
        self,
        input_text: str,
        output_text: str,
        criteria: str,
        custom_prompt: Optional[str] = None,
        reference_context: Optional[str] = None
    ) -> str:
        """
        Construye prompt de evaluación para el juez LLM.
        """
        if custom_prompt:
            return custom_prompt.replace("{input}", input_text)\
                               .replace("{output}", output_text)\
                               .replace("{criteria}", criteria)

        prompt = f"""Eres un evaluador experto. Evalúa la siguiente respuesta según el criterio: {criteria}

Input: {input_text}
Output: {output_text}
"""

        if reference_context:
            prompt += f"\nContexto de referencia: {reference_context}\n"

        prompt += """
Proporciona tu evaluación en formato JSON con la siguiente estructura:
{
  "score": 0.85,
  "feedback": "Feedback detallado...",
  "positives": ["aspecto positivo 1", "aspecto positivo 2"],
  "improvements": ["aspecto a mejorar 1", "aspecto a mejorar 2"],
  "criteriaScores": {
    "clarity": 0.9,
    "accuracy": 0.85,
    "completeness": 0.88
  }
}

El score debe ser un número entre 0.0 y 1.0.
"""
        return prompt

    def build_ab_evaluation_prompt(
        self,
        input_text: str,
        output_a: str,
        output_b: str,
        criteria: str,
        custom_prompt: Optional[str] = None
    ) -> str:
        """
        Construye prompt de evaluación A/B.
        """
        if custom_prompt:
            return custom_prompt.replace("{input}", input_text)\
                               .replace("{outputA}", output_a)\
                               .replace("{outputB}", output_b)\
                               .replace("{criteria}", criteria)

        prompt = f"""Eres un evaluador experto. Compara las siguientes dos respuestas según el criterio: {criteria}

Input: {input_text}
Output A: {output_a}
Output B: {output_b}

Proporciona tu evaluación en formato JSON:
{{
  "winner": "A" o "B" o "TIE",
  "scoreA": 0.85,
  "scoreB": 0.92,
  "feedback": "Explicación de la comparación...",
  "positivesA": ["aspectos positivos de A"],
  "positivesB": ["aspectos positivos de B"],
  "improvementsA": ["mejoras para A"],
  "improvementsB": ["mejoras para B"]
}}
"""
        return prompt
```

#### `response_parser.py`

```python
import json
from typing import Dict, Any, Optional
from app.models.response import JudgeEvaluation, JudgeABResponse

class ResponseParser:
    def parse_evaluation_response(
        self,
        response_text: str,
        input_text: str,
        output_text: str
    ) -> JudgeEvaluation:
        """
        Parsea respuesta JSON del juez LLM.
        """
        try:
            # Intentar extraer JSON del texto (puede tener texto antes/después)
            json_start = response_text.find('{')
            json_end = response_text.rfind('}') + 1

            if json_start == -1 or json_end == 0:
                raise ValueError("No se encontró JSON en la respuesta")

            json_text = response_text[json_start:json_end]
            data = json.loads(json_text)

            return JudgeEvaluation(
                score=float(data.get("score", 0.0)),
                feedback=data.get("feedback", ""),
                positives=data.get("positives", []),
                improvements=data.get("improvements", []),
                criteriaScores=data.get("criteriaScores", {}),
                input=input_text,
                output=output_text
            )
        except Exception as e:
            # Retornar evaluación de error
            return JudgeEvaluation(
                score=0.0,
                feedback=f"Error parsing judge response: {str(e)}",
                positives=[],
                improvements=[],
                criteriaScores={},
                input=input_text,
                output=output_text
            )

    def parse_ab_response(
        self,
        response_text: str,
        input_text: str,
        output_a: str,
        output_b: str
    ) -> JudgeABResponse:
        """
        Parsea respuesta de comparación A/B.
        """
        try:
            json_start = response_text.find('{')
            json_end = response_text.rfind('}') + 1

            if json_start == -1 or json_end == 0:
                raise ValueError("No se encontró JSON en la respuesta")

            json_text = response_text[json_start:json_end]
            data = json.loads(json_text)

            return JudgeABResponse(
                winner=data.get("winner", "TIE"),
                scoreA=float(data.get("scoreA", 0.0)),
                scoreB=float(data.get("scoreB", 0.0)),
                feedback=data.get("feedback", ""),
                positivesA=data.get("positivesA", []),
                positivesB=data.get("positivesB", []),
                improvementsA=data.get("improvementsA", []),
                improvementsB=data.get("improvementsB", [])
            )
        except Exception as e:
            # Retornar respuesta de error
            return JudgeABResponse(
                winner="TIE",
                scoreA=0.0,
                scoreB=0.0,
                feedback=f"Error parsing judge response: {str(e)}",
                positivesA=[],
                positivesB=[],
                improvementsA=[],
                improvementsB=[]
            )
```

---

## 📊 MICROSERVICIO 3: AB TEST COMPARISON SERVICE

### Descripción

Microservicio responsable de comparar resultados de pruebas A/B, calcular métricas comparativas, significancia estadística y determinar ganadores.

### Tecnologías

- **Framework:** FastAPI (Python 3.11+)
- **Puerto:** 8014
- **Base Path:** `/api/abtest`
- **Librerías:** NumPy, SciPy (para estadísticas)

### Estructura del Proyecto

```
abtest-comparison-service/
├── app/
│   ├── __init__.py
│   ├── main.py
│   ├── config.py
│   ├── models/
│   │   ├── __init__.py
│   │   ├── request.py
│   │   └── response.py
│   ├── services/
│   │   ├── __init__.py
│   │   ├── comparison_engine.py    # Motor de comparación
│   │   ├── statistics.py          # Cálculos estadísticos
│   │   └── metrics_calculator.py  # Calculadora de métricas
│   ├── routers/
│   │   ├── __init__.py
│   │   └── comparison.py
│   └── utils/
│       └── validators.py
├── tests/
├── requirements.txt
├── Dockerfile
└── README.md
```

### Endpoints

#### 1. Comparar Resultados A/B

```http
POST /api/abtest/compare
Content-Type: application/json
```

**Request:**
```json
{
  "evaluationsA": [
    {
      "score": 0.85,
      "feedback": "...",
      "input": "¿Qué es la IA?",
      "output": "..."
    },
    {
      "score": 0.82,
      "feedback": "...",
      "input": "Explica ML",
      "output": "..."
    }
  ],
  "evaluationsB": [
    {
      "score": 0.92,
      "feedback": "...",
      "input": "¿Qué es la IA?",
      "output": "..."
    },
    {
      "score": 0.89,
      "feedback": "...",
      "input": "Explica ML",
      "output": "..."
    }
  ],
  "metricsA": {
    "totalTests": 100,
    "successfulTests": 98,
    "averageExecutionTime": 1.23,
    "totalTokensUsed": 15000
  },
  "metricsB": {
    "totalTests": 100,
    "successfulTests": 99,
    "averageExecutionTime": 1.45,
    "totalTokensUsed": 16000
  }
}
```

**Response:**
```json
{
  "winner": "B",
  "averageScoreA": 0.835,
  "averageScoreB": 0.905,
  "winsA": 35,
  "winsB": 65,
  "ties": 0,
  "statisticalSignificance": 0.95,
  "improvement": {
    "averageScore": "+8.4%",
    "successfulTests": "+1.0%",
    "averageExecutionTime": "+17.9%"
  },
  "metricComparisons": {
    "averageScore": {
      "metricName": "averageScore",
      "valueA": 0.835,
      "valueB": 0.905,
      "difference": 0.07,
      "improvementPercentage": "+8.4%",
      "winner": "B"
    },
    "successfulTests": {
      "metricName": "successfulTests",
      "valueA": 98,
      "valueB": 99,
      "difference": 1,
      "improvementPercentage": "+1.0%",
      "winner": "B"
    }
  }
}
```

### Implementación

#### `comparison_engine.py`

```python
from typing import List, Dict, Any
from app.models.request import ABComparisonRequest
from app.models.response import ABComparisonResponse, MetricComparison
from app.services.statistics import StatisticsCalculator
from app.services.metrics_calculator import MetricsCalculator

class ComparisonEngine:
    def __init__(self):
        self.stats_calculator = StatisticsCalculator()
        self.metrics_calculator = MetricsCalculator()

    def compare(
        self,
        request: ABComparisonRequest
    ) -> ABComparisonResponse:
        """
        Compara resultados A/B y determina ganador.
        """
        # Calcular scores promedio
        scores_a = [e.score for e in request.evaluationsA if e.score is not None]
        scores_b = [e.score for e in request.evaluationsB if e.score is not None]

        avg_score_a = sum(scores_a) / len(scores_a) if scores_a else 0.0
        avg_score_b = sum(scores_b) / len(scores_b) if scores_b else 0.0

        # Contar victorias
        wins_a, wins_b, ties = self._count_wins(
            request.evaluationsA,
            request.evaluationsB
        )

        # Determinar ganador
        winner = self._determine_winner(
            avg_score_a, avg_score_b,
            wins_a, wins_b
        )

        # Calcular significancia estadística
        statistical_significance = self.stats_calculator.calculate_significance(
            scores_a, scores_b
        )

        # Calcular mejoras
        improvement = self.metrics_calculator.calculate_improvement(
            request.metricsA,
            request.metricsB
        )

        # Comparar métricas individuales
        metric_comparisons = self.metrics_calculator.compare_metrics(
            request.metricsA,
            request.metricsB
        )

        return ABComparisonResponse(
            winner=winner,
            averageScoreA=avg_score_a,
            averageScoreB=avg_score_b,
            winsA=wins_a,
            winsB=wins_b,
            ties=ties,
            statisticalSignificance=statistical_significance,
            improvement=improvement,
            metricComparisons=metric_comparisons
        )

    def _count_wins(
        self,
        evaluations_a: List,
        evaluations_b: List
    ) -> tuple:
        """
        Cuenta victorias, derrotas y empates.
        """
        wins_a = 0
        wins_b = 0
        ties = 0

        min_size = min(len(evaluations_a), len(evaluations_b))

        for i in range(min_size):
            score_a = evaluations_a[i].score
            score_b = evaluations_b[i].score

            if score_a is None or score_b is None:
                continue

            diff = abs(score_a - score_b)
            if diff < 0.01:  # Empate si diferencia < 0.01
                ties += 1
            elif score_a > score_b:
                wins_a += 1
            else:
                wins_b += 1

        return wins_a, wins_b, ties

    def _determine_winner(
        self,
        avg_score_a: float,
        avg_score_b: float,
        wins_a: int,
        wins_b: int
    ) -> str:
        """
        Determina ganador considerando scores y victorias.
        """
        score_diff = abs(avg_score_a - avg_score_b)
        win_diff = abs(wins_a - wins_b)

        if score_diff < 0.01 and win_diff < 3:
            return "TIE"

        # Ponderar: 70% score promedio, 30% número de victorias
        total_wins = wins_a + wins_b
        if total_wins == 0:
            return "TIE"

        weighted_score_a = (avg_score_a * 0.7) + ((wins_a / total_wins) * 0.3)
        weighted_score_b = (avg_score_b * 0.7) + ((wins_b / total_wins) * 0.3)

        if abs(weighted_score_a - weighted_score_b) < 0.01:
            return "TIE"

        return "B" if weighted_score_b > weighted_score_a else "A"
```

#### `statistics.py`

```python
from typing import List
import numpy as np
from scipy import stats

class StatisticsCalculator:
    def calculate_significance(
        self,
        scores_a: List[float],
        scores_b: List[float]
    ) -> float:
        """
        Calcula significancia estadística usando t-test.
        """
        if len(scores_a) < 2 or len(scores_b) < 2:
            return 0.0

        try:
            # Convertir a arrays numpy
            arr_a = np.array(scores_a)
            arr_b = np.array(scores_b)

            # Realizar t-test
            t_stat, p_value = stats.ttest_ind(arr_a, arr_b)

            # Retornar nivel de confianza (1 - p_value)
            return max(0.0, min(1.0, 1.0 - p_value))
        except Exception:
            return 0.0
```

#### `metrics_calculator.py`

```python
from typing import Dict, Any, List
from app.models.response import MetricComparison

class MetricsCalculator:
    def calculate_improvement(
        self,
        metrics_a: Dict[str, Any],
        metrics_b: Dict[str, Any]
    ) -> Dict[str, str]:
        """
        Calcula mejoras porcentuales entre métricas.
        """
        improvement = {}

        for key in metrics_a:
            if key not in metrics_b:
                continue

            try:
                value_a = float(metrics_a[key])
                value_b = float(metrics_b[key])

                if value_a == 0:
                    continue

                improvement_pct = ((value_b - value_a) / value_a) * 100
                improvement[key] = f"{improvement_pct:+.2f}%"
            except (ValueError, TypeError):
                continue

        return improvement

    def compare_metrics(
        self,
        metrics_a: Dict[str, Any],
        metrics_b: Dict[str, Any]
    ) -> Dict[str, MetricComparison]:
        """
        Compara métricas individuales.
        """
        comparisons = {}

        for key in metrics_a:
            if key not in metrics_b:
                continue

            try:
                value_a = float(metrics_a[key])
                value_b = float(metrics_b[key])
                diff = value_b - value_a

                improvement_pct = (diff / value_a * 100) if value_a != 0 else 0.0
                winner = "TIE" if abs(diff) < 0.01 else ("B" if value_b > value_a else "A")

                comparisons[key] = MetricComparison(
                    metricName=key,
                    valueA=value_a,
                    valueB=value_b,
                    difference=abs(diff),
                    improvementPercentage=f"{improvement_pct:+.2f}%",
                    winner=winner
                )
            except (ValueError, TypeError):
                continue

        return comparisons
```

---

## ☕ CLIENTE JAVA

### Estructura del Cliente

Siguiendo el patrón existente, crear en `codeflowx.govern.nocode.client`:

```
src/main/java/com/codeflowx/governance/client/
├── testing/
│   ├── TestingExecutionClient.java
│   ├── LLMJudgeClient.java
│   └── ABTestComparisonClient.java
└── model/
    ├── testing/
    │   ├── request/
    │   │   ├── PromptExecutionRequest.java
    │   │   ├── ModelExecutionRequest.java
    │   │   ├── AgentExecutionRequest.java
    │   │   ├── RAGExecutionRequest.java
    │   │   └── JudgeEvaluationRequest.java
    │   └── response/
    │       ├── ExecutionResult.java
    │       ├── JudgeEvaluation.java
    │       └── ABComparisonResponse.java
```

### Ejemplo de Cliente

#### `TestingExecutionClient.java`

```java
package com.codeflowx.governance.client.testing;

import com.codeflowx.governance.client.model.testing.request.PromptExecutionRequest;
import com.codeflowx.governance.client.model.testing.response.ExecutionResponse;
import reactor.core.publisher.Mono;

public interface TestingExecutionClient {
    Mono<ExecutionResponse> executePrompt(PromptExecutionRequest request);
    Mono<ExecutionResponse> executeModel(ModelExecutionRequest request);
    Mono<ExecutionResponse> executeAgent(AgentExecutionRequest request);
    Mono<ExecutionResponse> executeRAG(RAGExecutionRequest request);
    Mono<List<TestCase>> parseCSV(MultipartFile file);
}
```

#### Integración en `AIGovernanceClient.java`

```java
public class AIGovernanceClient {
    // ... clientes existentes ...

    private TestingExecutionClient testingExecutionClient;
    private LLMJudgeClient llmJudgeClient;
    private ABTestComparisonClient abTestComparisonClient;

    public TestingExecutionClient testingExecution() {
        if (testingExecutionClient == null) {
            testingExecutionClient = new TestingExecutionClientImpl(webClient, baseUrl);
        }
        return testingExecutionClient;
    }

    public LLMJudgeClient judge() {
        if (llmJudgeClient == null) {
            llmJudgeClient = new LLMJudgeClientImpl(webClient, baseUrl);
        }
        return llmJudgeClient;
    }

    public ABTestComparisonClient abTest() {
        if (abTestComparisonClient == null) {
            abTestComparisonClient = new ABTestComparisonClientImpl(webClient, baseUrl);
        }
        return abTestComparisonClient;
    }
}
```

### Uso en Backend Java

```java
@Autowired
private AIGovernanceClient governance;

// Ejecutar prompt
PromptExecutionRequest request = PromptExecutionRequest.builder()
    .promptId(123L)
    .modelId(456L)
    .testCases(testCases)
    .build();

ExecutionResponse response = governance.testingExecution()
    .executePrompt(request)
    .block();

// Evaluar con juez
JudgeEvaluationRequest judgeRequest = JudgeEvaluationRequest.builder()
    .input("¿Qué es la IA?")
    .output("La IA es...")
    .judgeModelId(789L)
    .criteria("quality")
    .build();

JudgeEvaluation evaluation = governance.judge()
    .evaluate(judgeRequest)
    .block();
```

---

## 🚀 DESPLIEGUE Y CONFIGURACIÓN

### Dockerfile Ejemplo

```dockerfile
FROM python:3.11-slim

WORKDIR /app

# Instalar dependencias
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copiar código
COPY app/ ./app/

# Exponer puerto
EXPOSE 8012

# Comando
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8012"]
```

### Kubernetes Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: testing-execution-service
  namespace: codeflowx-governance
spec:
  replicas: 2
  selector:
    matchLabels:
      app: testing-execution-service
  template:
    metadata:
      labels:
        app: testing-execution-service
    spec:
      containers:
      - name: testing-execution
        image: codeflowx/testing-execution-service:1.0.0
        ports:
        - containerPort: 8012
        env:
        - name: MODEL_SERVICE_URL
          value: "http://model-wrapper-service:8006"
        - name: PROMPT_SERVICE_URL
          value: "http://prompt-governance-service:8003"
        resources:
          requests:
            memory: "512Mi"
            cpu: "250m"
          limits:
            memory: "1Gi"
            cpu: "500m"
---
apiVersion: v1
kind: Service
metadata:
  name: testing-execution-service
  namespace: codeflowx-governance
spec:
  selector:
    app: testing-execution-service
  ports:
  - port: 8012
    targetPort: 8012
```

### Configuración API Gateway

```yaml
spring:
  cloud:
    gateway:
      routes:
      - id: testing-execution
        uri: http://testing-execution-service:8012
        predicates:
        - Path=/api/testing/**
        filters:
        - StripPrefix=1

      - id: llm-judge
        uri: http://llm-judge-service:8013
        predicates:
        - Path=/api/judge/**
        filters:
        - StripPrefix=1

      - id: abtest-comparison
        uri: http://abtest-comparison-service:8014
        predicates:
        - Path=/api/abtest/**
        filters:
        - StripPrefix=1
```

---

## 🧪 TESTING Y VALIDACIÓN

### Tests Unitarios

```python
import pytest
from app.services.prompt_executor import PromptExecutor

def test_execute_prompt():
    executor = PromptExecutor(mock_model_service)
    request = PromptExecutionRequest(
        promptId=123,
        modelId=456,
        testCases=[TestCase(input="Test", expectedOutput=None)],
        parameters={}
    )

    result = await executor.execute_prompt(request)

    assert result["executionId"] is not None
    assert len(result["results"]) == 1
    assert result["summary"]["totalTests"] == 1
```

### Tests de Integración

```python
from fastapi.testclient import TestClient
from app.main import app

client = TestClient(app)

def test_execute_prompt_endpoint():
    response = client.post(
        "/api/testing/execute/prompt",
        json={
            "promptId": 123,
            "modelId": 456,
            "testCases": [
                {"input": "Test", "expectedOutput": None}
            ]
        }
    )

    assert response.status_code == 200
    assert "executionId" in response.json()
```

---

## ✅ MEJORES PRÁCTICAS

### 1. Manejo de Errores

- Usar excepciones específicas por tipo de error
- Retornar códigos HTTP apropiados
- Incluir mensajes de error descriptivos
- Logging detallado de errores

### 2. Performance

- Usar async/await para operaciones I/O
- Implementar timeouts configurables
- Cachear resultados cuando sea apropiado
- Procesar lotes en paralelo cuando sea posible

### 3. Seguridad

- Validar todos los inputs
- Sanitizar datos antes de procesar
- Usar autenticación/autorización si es necesario
- No exponer información sensible en logs

### 4. Monitoreo

- Health checks (`/health`)
- Métricas Prometheus (`/metrics`)
- Logging estructurado (JSON)
- Tracing distribuido (opcional)

### 5. Documentación

- OpenAPI/Swagger automático
- README con ejemplos
- Comentarios en código
- Guías de uso

---

## 📝 CHECKLIST DE IMPLEMENTACIÓN

### Testing Execution Service
- [ ] Estructura del proyecto creada
- [ ] Endpoints implementados
- [ ] Integración con servicios existentes
- [ ] Parser CSV implementado
- [ ] Tests unitarios
- [ ] Tests de integración
- [ ] Dockerfile y deployment
- [ ] Documentación

### LLM Judge Service
- [ ] Estructura del proyecto creada
- [ ] Endpoints implementados
- [ ] Prompt builder implementado
- [ ] Response parser implementado
- [ ] Tests unitarios
- [ ] Tests de integración
- [ ] Dockerfile y deployment
- [ ] Documentación

### AB Test Comparison Service
- [ ] Estructura del proyecto creada
- [ ] Endpoints implementados
- [ ] Motor de comparación implementado
- [ ] Cálculos estadísticos
- [ ] Tests unitarios
- [ ] Tests de integración
- [ ] Dockerfile y deployment
- [ ] Documentación

### Cliente Java
- [ ] Interfaces de clientes creadas
- [ ] Implementaciones creadas
- [ ] Integración en AIGovernanceClient
- [ ] DTOs de request/response
- [ ] Tests unitarios
- [ ] Documentación

### Despliegue
- [ ] Dockerfiles creados
- [ ] Kubernetes deployments
- [ ] Configuración API Gateway
- [ ] Health checks
- [ ] Monitoreo configurado

---

**Última Actualización:** Enero 2025
**Versión:** 1.0
**Estado:** 📋 Guía de Implementación
