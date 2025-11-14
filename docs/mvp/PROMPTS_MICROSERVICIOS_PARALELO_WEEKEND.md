# 🚀 PROMPTS MICROSERVICIOS - TRABAJO PARALELO WEEKEND

**Objetivo:** 7 microservicios Python + Orchestrator funcionando  
**Timeline:** Viernes noche → Martes listo  
**Estrategia:** 7 chats IA trabajando en paralelo = 7 developers  

---

## 🏗️ ARQUITECTURA DE MICROSERVICIOS

### **Principio de diseño:**

```
MICROSERVICIOS PYTHON (Stateless):
├─ Solo ejecutan análisis/procesamiento
├─ Input: Request con datos (CSV, JSON, texto, etc.)
├─ Output: JSON con resultados
├─ NO persisten (sin DB, sin tablas)
├─ Framework: FastAPI (ligero, rápido)
├─ Logging: sí, persistencia: NO

BACKEND JAVA (Stateful):
├─ Llama micros Python vía REST
├─ Recibe JSON response
├─ Persiste en PostgreSQL (170 tablas JPA ya existentes)
├─ Gestiona workflow, audit trail, usuarios
├─ UI: 500 pantallas ZKoss

ORCHESTRATOR (API Gateway):
├─ Router de requests
├─ Load balancing
├─ Health checks
├─ Rate limiting
├─ NO lógica de negocio
```

---

## 📋 MICROSERVICIOS A CREAR (7 Chats)

```
✅ MICRO 1: leka-tabular-analysis (YA EXISTE - puerto 8001)
🔧 MICRO 2: leka-llm-evaluation (puerto 8002) - Chat 1
🔧 MICRO 3: leka-prompt-governance (puerto 8003) - Chat 2
🔧 MICRO 4: leka-rag-evaluation (puerto 8004) - Chat 3
🔧 MICRO 5: leka-agent-monitoring (puerto 8005) - Chat 4
🔧 MICRO 6: leka-model-wrapper (puerto 8006) - Chat 5
🔧 MICRO 7: leka-orchestrator (puerto 8000) - Spring Cloud Gateway - Chat 6
🔧 MICRO 8: AI Interpreter (puerto 8011) - Mistral 7B - Chat 7
🔧 MICRO 9: Cliente Java Unified - Chat 8
```

---

## 💬 CHAT 1: MICRO LLM EVALUATION (Puerto 8002)

### **PROMPT ESPECÍFICO:**

```
Necesito crear microservicio Python STATELESS para evaluación de LLMs.

CONTEXTO:
- Framework: FastAPI
- Puerto: 8002
- Arquitectura: Microservicio stateless (NO base datos)
- Input: Request JSON con parámetros
- Output: JSON con resultados análisis
- Integración: LangChain, LangSmith, DeepEval (frameworks ya existentes)

RESPONSABILIDAD ÚNICA:
Evaluar outputs de LLMs (hallucinations, toxicity, bias, quality).
NO persiste nada, solo analiza y retorna JSON.

═══════════════════════════════════════════
ENDPOINTS A CREAR:
═══════════════════════════════════════════

1. POST /api/llm/evaluate-hallucination
   
   Input:
   {
     "model_id": "gpt-4",
     "prompt": "Explain quantum computing",
     "response": "LLM generated text...",
     "reference_context": "Ground truth text..." (opcional)
   }
   
   Processing:
   - Detectar hallucinations usando DeepEval o custom
   - Factual consistency check
   - Groundedness score
   
   Output:
   {
     "hallucination_detected": true,
     "confidence": 0.85,
     "score": 45,  // 0-100
     "severity": "MODERATE",
     "details": ["Claim X not supported by context"],
     "recommendations": "Verify facts before deployment"
   }

2. POST /api/llm/evaluate-toxicity
   
   Input:
   {
     "text": "LLM output to analyze",
     "model_id": "gpt-4"
   }
   
   Processing:
   - Toxicity detection
   - Hate speech
   - Offensive content
   - Using: Detoxify library o similar
   
   Output:
   {
     "is_toxic": false,
     "toxicity_score": 0.05,
     "categories": {
       "hate_speech": 0.01,
       "profanity": 0.02,
       "threat": 0.00
     },
     "severity": "SAFE",
     "flagged_content": []
   }

3. POST /api/llm/evaluate-bias-text
   
   Input:
   {
     "text": "LLM output",
     "bias_types": ["gender", "race", "religion"]
   }
   
   Processing:
   - Detectar bias en lenguaje
   - Gendered language
   - Stereotypes
   - Cultural bias
   
   Output:
   {
     "bias_detected": true,
     "bias_types": ["gender"],
     "score": 65,  // 0-100, lower = more biased
     "examples": ["Uses 'he' exclusively for doctors"],
     "severity": "MODERATE",
     "recommendations": "Use gender-neutral language"
   }

4. POST /api/llm/evaluate-quality
   
   Input:
   {
     "prompt": "User question",
     "response": "LLM answer",
     "criteria": ["relevance", "coherence", "completeness"]
   }
   
   Processing:
   - Relevance to prompt
   - Coherence of answer
   - Completeness
   - Using: GPT-4-as-judge o DeepEval
   
   Output:
   {
     "quality_score": 85,
     "relevance": 90,
     "coherence": 85,
     "completeness": 80,
     "overall_grade": "GOOD",
     "feedback": "Answer is relevant and complete..."
   }

5. POST /api/llm/batch-evaluate
   
   Input:
   {
     "evaluations": [
       {"type": "hallucination", "data": {...}},
       {"type": "toxicity", "data": {...}},
       {"type": "bias", "data": {...}}
     ]
   }
   
   Output:
   {
     "results": [
       {"type": "hallucination", "result": {...}},
       {"type": "toxicity", "result": {...}},
       ...
     ],
     "overall_score": 75,
     "critical_issues": 0
   }

6. GET /health
   Health check

═══════════════════════════════════════════
FRAMEWORKS A USAR:
═══════════════════════════════════════════

✅ FastAPI (web framework)
✅ DeepEval (LLM evaluation framework)
✅ LangChain (si ayuda con evaluations)
✅ Detoxify (toxicity detection)
✅ transformers + sentiment models (bias detection)
✅ Custom logic donde necesario

NO USAR:
❌ Base de datos (SQLAlchemy, etc.)
❌ ORM
❌ Persistencia de ningún tipo

═══════════════════════════════════════════
ESTRUCTURA PROYECTO:
═══════════════════════════════════════════

leka-llm-evaluation/
├── main.py                    # FastAPI app
├── services/
│   ├── hallucination_service.py
│   ├── toxicity_service.py
│   ├── bias_text_service.py
│   └── quality_service.py
├── models/
│   └── response_models.py     # Pydantic models (NO DB)
├── requirements.txt
├── Dockerfile
├── README.md
└── tests/
    └── test_endpoints.py

═══════════════════════════════════════════
EJEMPLO CÓDIGO:
═══════════════════════════════════════════

# main.py
from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI(title="LLM Evaluation Service", version="1.0.0")

class HallucinationRequest(BaseModel):
    model_id: str
    prompt: str
    response: str
    reference_context: str = None

class HallucinationResponse(BaseModel):
    hallucination_detected: bool
    confidence: float
    score: int
    severity: str
    details: list
    recommendations: str

@app.post("/api/llm/evaluate-hallucination", response_model=HallucinationResponse)
async def evaluate_hallucination(request: HallucinationRequest):
    # Usar DeepEval o custom logic
    # NO guardar en DB
    # Solo procesar y retornar
    
    result = hallucination_service.detect(
        prompt=request.prompt,
        response=request.response,
        context=request.reference_context
    )
    
    return HallucinationResponse(**result)

# services/hallucination_service.py
def detect(prompt: str, response: str, context: str = None):
    # Implementar detección con DeepEval
    # Retornar dict con resultados
    # NO persistir
    
    return {
        "hallucination_detected": True,
        "confidence": 0.85,
        "score": 45,
        "severity": "MODERATE",
        "details": ["Claim not supported"],
        "recommendations": "Verify facts"
    }

═══════════════════════════════════════════
DELIVERABLES:
═══════════════════════════════════════════

Por favor genera:
1. main.py completo con 6 endpoints
2. Services (hallucination, toxicity, bias, quality)
3. Pydantic models (Request/Response)
4. requirements.txt con dependencias
5. Dockerfile
6. README.md con:
   - Instalación
   - Ejemplos curl
   - Docker run
7. Tests básicos

IMPORTANTE:
✅ Stateless (NO database)
✅ Solo lógica análisis
✅ Retornar JSON
✅ Usar frameworks existentes (DeepEval, Detoxify, etc.)
❌ NO persistencia
❌ NO ORM
❌ NO SQLAlchemy

Usa FastAPI, Pydantic, DeepEval, buenas prácticas Python.
```

---

## 💬 CHAT 2: MICRO PROMPT GOVERNANCE (Puerto 8003)

### **PROMPT ESPECÍFICO:**

```
Necesito crear microservicio Python STATELESS para governance de prompts.

CONTEXTO:
- Framework: FastAPI
- Puerto: 8003
- Stateless: NO base datos, solo análisis
- Input: Prompt text
- Output: JSON con evaluación/score

RESPONSABILIDAD ÚNICA:
Evaluar seguridad, efectividad y calidad de prompts.
NO persiste, solo analiza.

═══════════════════════════════════════════
ENDPOINTS A CREAR:
═══════════════════════════════════════════

1. POST /api/prompt/evaluate-safety
   
   Input:
   {
     "prompt": "Prompt text to evaluate",
     "model_target": "gpt-4",
     "use_case": "customer_service"
   }
   
   Processing:
   - Prompt injection detection
   - Jailbreak attempt detection
   - PII leakage risk
   - Unsafe instructions
   
   Output:
   {
     "is_safe": true,
     "safety_score": 85,
     "risks_detected": [],
     "severity": "LOW",
     "recommendations": "Safe to use"
   }

2. POST /api/prompt/evaluate-effectiveness
   
   Input:
   {
     "prompt": "Prompt text",
     "expected_output_type": "json" | "text" | "code",
     "sample_responses": ["response1", "response2", ...]  // opcional
   }
   
   Processing:
   - Clarity score
   - Specificity score
   - Structure quality
   - Expected output likelihood
   
   Output:
   {
     "effectiveness_score": 78,
     "clarity": 80,
     "specificity": 75,
     "structure_quality": 80,
     "grade": "GOOD",
     "suggestions": [
       "Add output format example",
       "Specify constraints explicitly"
     ]
   }

3. POST /api/prompt/detect-pii-leakage
   
   Input:
   {
     "prompt": "Prompt with potential PII"
   }
   
   Processing:
   - Detectar emails, phones, names
   - Credit cards, SSN
   - Addresses, etc.
   
   Output:
   {
     "pii_detected": true,
     "pii_types": ["email", "phone"],
     "locations": [
       {"type": "email", "value": "user@example.com", "position": 45}
     ],
     "risk_level": "MEDIUM",
     "recommendations": "Remove or mask PII before sending to LLM"
   }

4. POST /api/prompt/compare-versions
   
   Input:
   {
     "prompt_v1": "Original prompt",
     "prompt_v2": "Modified prompt",
     "test_cases": ["case1", "case2", ...]  // opcional
   }
   
   Processing:
   - Similarity score
   - Structural changes
   - Safety comparison
   - Effectiveness comparison
   
   Output:
   {
     "similarity": 0.75,
     "improvements": ["v2 more specific", "v2 adds constraints"],
     "regressions": [],
     "recommendation": "USE_V2",
     "confidence": 0.90
   }

5. POST /api/prompt/analyze-cost
   
   Input:
   {
     "prompt": "Prompt text",
     "model": "gpt-4",
     "expected_responses_per_day": 1000
   }
   
   Processing:
   - Token counting
   - Cost estimation
   - Optimization suggestions
   
   Output:
   {
     "prompt_tokens": 245,
     "estimated_response_tokens": 500,
     "cost_per_call": 0.015,  // USD
     "cost_per_day": 15.0,
     "cost_per_month": 450.0,
     "optimization_potential": "20% reduction possible",
     "suggestions": ["Remove redundant phrases", "Use system message"]
   }

6. GET /health

═══════════════════════════════════════════
FRAMEWORKS A USAR:
═══════════════════════════════════════════

✅ FastAPI
✅ LangKit (prompt analysis)
✅ tiktoken (token counting)
✅ transformers + NER models (PII detection)
✅ Custom regex patterns (safety checks)
✅ Presidio (PII detection alternativa)

NO USAR:
❌ SQLAlchemy, databases
❌ Persistencia

═══════════════════════════════════════════
ESTRUCTURA:
═══════════════════════════════════════════

leka-prompt-governance/
├── main.py
├── services/
│   ├── safety_service.py
│   ├── effectiveness_service.py
│   ├── pii_detection_service.py
│   └── cost_analysis_service.py
├── models/
│   └── schemas.py  # Pydantic models
├── requirements.txt
├── Dockerfile
└── README.md

Por favor genera:
1. main.py con 6 endpoints
2. Todos los services
3. Pydantic schemas
4. requirements.txt
5. Dockerfile
6. README con ejemplos curl

CRÍTICO:
✅ Stateless (NO DB)
✅ Solo análisis
✅ Retornar JSON
❌ NO persistencia
```

---

## 💬 CHAT 3: MICRO RAG EVALUATION (Puerto 8004)

### **PROMPT ESPECÍFICO:**

```
Necesito crear microservicio Python STATELESS para evaluación de sistemas RAG.

CONTEXTO:
- Framework: FastAPI
- Puerto: 8004
- Stateless: NO DB, solo análisis
- Integración: RAGAS framework, TruLens

RESPONSABILIDAD:
Evaluar calidad de sistemas RAG (retrieval, answer, hallucination).

═══════════════════════════════════════════
ENDPOINTS:
═══════════════════════════════════════════

1. POST /api/rag/evaluate-retrieval
   
   Input:
   {
     "query": "User question",
     "retrieved_documents": [
       {"doc_id": "1", "content": "...", "score": 0.9},
       {"doc_id": "2", "content": "...", "score": 0.7}
     ],
     "relevant_doc_ids": ["1", "3"]  // ground truth opcional
   }
   
   Output:
   {
     "retrieval_quality": 85,
     "precision": 0.5,  // 1/2 retrieved es relevant
     "recall": 0.33,    // 1/3 relevant fue retrieved
     "mrr": 1.0,        // Mean Reciprocal Rank
     "ndcg": 0.87,      // Normalized Discounted Cumulative Gain
     "grade": "GOOD",
     "recommendations": "Consider increasing k to improve recall"
   }

2. POST /api/rag/evaluate-answer
   
   Input:
   {
     "query": "User question",
     "answer": "RAG generated answer",
     "retrieved_context": ["doc1 content", "doc2 content"],
     "ground_truth": "Expected answer" (opcional)
   }
   
   Output:
   {
     "answer_quality": 78,
     "groundedness": 0.90,  // Answer supported by context
     "relevance": 0.85,     // Answer relevant to query
     "correctness": 0.75,   // vs. ground truth si existe
     "hallucination_score": 0.10,  // Low = good
     "grade": "GOOD",
     "issues": ["Minor detail not in context"],
     "recommendations": "Answer quality acceptable"
   }

3. POST /api/rag/evaluate-context-relevance
   
   Input:
   {
     "query": "User question",
     "contexts": ["context1", "context2", "context3"]
   }
   
   Output:
   {
     "context_relevance": 0.82,
     "per_context_scores": [0.95, 0.80, 0.70],
     "irrelevant_contexts": [2],  // index del contexto no relevante
     "recommendations": "Remove context 2 to reduce noise"
   }

4. POST /api/rag/evaluate-full-pipeline
   
   Input:
   {
     "query": "User question",
     "retrieved_docs": [...],
     "generated_answer": "RAG answer",
     "ground_truth": "Expected" (opcional)
   }
   
   Output:
   {
     "overall_score": 82,
     "retrieval_score": 85,
     "answer_score": 80,
     "faithfulness": 0.90,
     "answer_relevancy": 0.85,
     "context_precision": 0.87,
     "grade": "GOOD",
     "bottlenecks": ["Answer generation could be more precise"],
     "recommendations": "Overall pipeline performing well"
   }

5. GET /health

═══════════════════════════════════════════
FRAMEWORKS:
═══════════════════════════════════════════

✅ FastAPI
✅ RAGAS (RAG Assessment framework)
✅ LangChain (utilities)
✅ sentence-transformers (embeddings para similarity)
✅ Custom metrics

NO:
❌ Base de datos

Por favor genera microservicio completo stateless.
```

---

## 💬 CHAT 4: MICRO AGENT MONITORING (Puerto 8005)

### **PROMPT ESPECÍFICO:**

```
Microservicio STATELESS para monitoreo de agentes IA.

CONTEXTO:
- FastAPI, puerto 8005
- Stateless (NO DB)
- Analiza comportamiento agentes multi-step

═══════════════════════════════════════════
ENDPOINTS:
═══════════════════════════════════════════

1. POST /api/agent/analyze-execution
   
   Input:
   {
     "agent_id": "customer-support-agent",
     "execution_trace": [
       {"step": 1, "action": "search_kb", "result": "success", "duration_ms": 450},
       {"step": 2, "action": "call_llm", "result": "success", "duration_ms": 2300},
       {"step": 3, "action": "format_response", "result": "success", "duration_ms": 50}
     ],
     "final_output": "Agent response",
     "expected_output": "Expected response" (opcional)
   }
   
   Output:
   {
     "success": true,
     "total_steps": 3,
     "successful_steps": 3,
     "success_rate": 1.0,
     "total_duration_ms": 2800,
     "efficiency_score": 85,
     "tool_usage": {
       "search_kb": 1,
       "call_llm": 1,
       "format_response": 1
     },
     "bottlenecks": ["call_llm slow (2.3s)"],
     "recommendations": "Consider caching LLM calls"
   }

2. POST /api/agent/evaluate-reliability
   
   Input:
   {
     "agent_executions": [
       {"execution_id": "1", "success": true, "duration": 2500},
       {"execution_id": "2", "success": false, "error": "timeout"},
       ...
     ]
   }
   
   Output:
   {
     "reliability_score": 92,
     "success_rate": 0.92,
     "avg_duration_ms": 2650,
     "failure_rate": 0.08,
     "common_errors": ["timeout (5%)", "tool_not_found (3%)"],
     "grade": "EXCELLENT",
     "recommendations": "Increase timeout threshold"
   }

3. POST /api/agent/analyze-cost
   
   Input:
   {
     "agent_id": "support-agent",
     "execution_trace": [...],  // con info de llamadas LLM, tools
     "pricing": {
       "gpt4_per_1k_tokens": 0.03,
       "tool_call_cost": 0.001
     }
   }
   
   Output:
   {
     "total_cost": 0.045,
     "llm_cost": 0.042,
     "tool_cost": 0.003,
     "tokens_used": 1400,
     "cost_breakdown": {...},
     "optimization_potential": "15% reducible",
     "suggestions": ["Cache common queries", "Use GPT-3.5 for simple tasks"]
   }

4. POST /api/agent/detect-loops
   
   Input:
   {
     "execution_trace": [
       {"step": 1, "action": "search"},
       {"step": 2, "action": "call_llm"},
       {"step": 3, "action": "search"},  // repeated
       {"step": 4, "action": "call_llm"},  // repeated
       ...
     ]
   }
   
   Output:
   {
     "loop_detected": true,
     "loop_pattern": "search → call_llm",
     "repetitions": 3,
     "severity": "MODERATE",
     "recommendations": "Add max_iterations limit"
   }

5. GET /health

Por favor genera microservicio completo stateless con LangChain utilities.
```

---

## 💬 CHAT 5: MICRO MODEL WRAPPER (Puerto 8006)

### **PROMPT ESPECÍFICO:**

```
Microservicio STATELESS wrapper para modelos comerciales (OpenAI, Anthropic, etc.).

CONTEXTO:
- FastAPI, puerto 8006
- LangChain integration
- Wrapper unificado sobre múltiples providers
- Stateless: NO DB

RESPONSABILIDAD:
Interfaz unificada para llamar modelos de diferentes providers.
Tracking de uso (retornar en response, NO guardar).

═══════════════════════════════════════════
ENDPOINTS:
═══════════════════════════════════════════

1. POST /api/models/invoke
   
   Input:
   {
     "provider": "openai" | "anthropic" | "cohere" | "groq",
     "model": "gpt-4" | "claude-3-opus" | etc.,
     "messages": [
       {"role": "system", "content": "You are..."},
       {"role": "user", "content": "Question"}
     ],
     "temperature": 0.7,
     "max_tokens": 1000
   }
   
   Output:
   {
     "response": "Model generated text",
     "provider": "openai",
     "model": "gpt-4",
     "usage": {
       "prompt_tokens": 245,
       "completion_tokens": 456,
       "total_tokens": 701
     },
     "cost": 0.021,  // USD calculated
     "latency_ms": 2340,
     "metadata": {
       "finish_reason": "stop",
       "model_version": "gpt-4-0613"
     }
   }

2. POST /api/models/batch-invoke
   
   Input:
   {
     "requests": [
       {"provider": "openai", "model": "gpt-4", "messages": [...]},
       {"provider": "anthropic", "model": "claude-3", "messages": [...]}
     ]
   }
   
   Output:
   {
     "results": [
       {"request_id": 0, "response": "...", "cost": 0.02},
       {"request_id": 1, "response": "...", "cost": 0.03}
     ],
     "total_cost": 0.05,
     "total_latency_ms": 5600
   }

3. POST /api/models/compare-responses
   
   Input:
   {
     "prompt": "Same prompt for all",
     "models": [
       {"provider": "openai", "model": "gpt-4"},
       {"provider": "anthropic", "model": "claude-3-opus"}
     ]
   }
   
   Output:
   {
     "responses": [
       {"model": "gpt-4", "response": "...", "cost": 0.02, "latency": 2300},
       {"model": "claude-3-opus", "response": "...", "cost": 0.03, "latency": 1800}
     ],
     "comparison": {
       "fastest": "claude-3-opus",
       "cheapest": "gpt-4",
       "response_similarity": 0.85
     }
   }

4. GET /api/models/list-available
   
   Output:
   {
     "providers": [
       {
         "name": "openai",
         "models": ["gpt-4", "gpt-3.5-turbo", "gpt-4-turbo"],
         "status": "available"
       },
       {
         "name": "anthropic",
         "models": ["claude-3-opus", "claude-3-sonnet"],
         "status": "available"
       }
     ]
   }

5. GET /health

═══════════════════════════════════════════
FRAMEWORKS:
═══════════════════════════════════════════

✅ FastAPI
✅ LangChain (ChatOpenAI, ChatAnthropic, etc.)
✅ tiktoken (token counting)
✅ openai, anthropic, cohere SDKs

Por favor genera microservicio stateless con LangChain wrappers.
```

---

## 💬 CHAT 6: ORCHESTRATOR - SPRING CLOUD GATEWAY (Puerto 8000)

### **PROMPT ESPECÍFICO:**

```
Necesito crear API Gateway con Spring Cloud Gateway para orquestar microservicios Python.

CONTEXTO:
- Framework: Spring Cloud Gateway
- Puerto: 8000
- Lenguaje: Java/Spring Boot
- Responsabilidad: Routing, load balancing, circuit breaker
- Microservicios backend: 6+ servicios Python FastAPI

MICROSERVICIOS PYTHON BACKEND:
- leka-tabular-analysis: localhost:8001
- leka-llm-evaluation: localhost:8002
- leka-prompt-governance: localhost:8003
- leka-rag-evaluation: localhost:8004
- leka-agent-monitoring: localhost:8005
- leka-model-wrapper: localhost:8006
- leka-ai-interpreter: localhost:8011

═══════════════════════════════════════════
ROUTING CONFIGURATION (application.yml):
═══════════════════════════════════════════

spring:
  cloud:
    gateway:
      routes:
        # Tabular Analysis
        - id: tabular-analysis
          uri: http://localhost:8001
          predicates:
            - Path=/api/tabular/**
          filters:
            - name: CircuitBreaker
              args:
                name: tabularCircuitBreaker
                fallbackUri: forward:/fallback/tabular
            - name: Retry
              args:
                retries: 2
                statuses: BAD_GATEWAY
        
        # LLM Evaluation
        - id: llm-evaluation
          uri: http://localhost:8002
          predicates:
            - Path=/api/llm/**
          filters:
            - CircuitBreaker
            - Retry
        
        # Prompt Governance
        - id: prompt-governance
          uri: http://localhost:8003
          predicates:
            - Path=/api/prompt/**
          filters:
            - CircuitBreaker
            - Retry
        
        # RAG Evaluation
        - id: rag-evaluation
          uri: http://localhost:8004
          predicates:
            - Path=/api/rag/**
          filters:
            - CircuitBreaker
            - Retry
        
        # Agent Monitoring
        - id: agent-monitoring
          uri: http://localhost:8005
          predicates:
            - Path=/api/agent/**
          filters:
            - CircuitBreaker
            - Retry
        
        # Model Wrapper
        - id: model-wrapper
          uri: http://localhost:8006
          predicates:
            - Path=/api/models/**
          filters:
            - CircuitBreaker
            - Retry
        
        # AI Interpreter
        - id: ai-interpreter
          uri: http://localhost:8011
          predicates:
            - Path=/api/interpret/**
          filters:
            - CircuitBreaker
            - Retry

═══════════════════════════════════════════
CONTROLADOR HEALTH CHECK:
═══════════════════════════════════════════

@RestController
@RequestMapping("/api/v1")
public class GatewayHealthController {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @GetMapping("/services/status")
    public ResponseEntity<ServicesStatusResponse> getServicesStatus() {
        List<ServiceStatus> services = Arrays.asList(
            checkService("tabular-analysis", "http://localhost:8001/health"),
            checkService("llm-evaluation", "http://localhost:8002/health"),
            checkService("prompt-governance", "http://localhost:8003/health"),
            checkService("rag-evaluation", "http://localhost:8004/health"),
            checkService("agent-monitoring", "http://localhost:8005/health"),
            checkService("model-wrapper", "http://localhost:8006/health"),
            checkService("ai-interpreter", "http://localhost:8011/health")
        );
        
        ServicesStatusResponse response = ServicesStatusResponse.builder()
            .services(services)
            .overallStatus(calculateOverallStatus(services))
            .build();
        
        return ResponseEntity.ok(response);
    }
    
    private ServiceStatus checkService(String name, String healthUrl) {
        try {
            long start = System.currentTimeMillis();
            ResponseEntity<String> response = restTemplate.getForEntity(healthUrl, String.class);
            long latency = System.currentTimeMillis() - start;
            
            return ServiceStatus.builder()
                .name(name)
                .url(healthUrl.replace("/health", ""))
                .status(response.getStatusCode() == HttpStatus.OK ? "healthy" : "unhealthy")
                .latencyMs(latency)
                .build();
        } catch (Exception e) {
            return ServiceStatus.builder()
                .name(name)
                .url(healthUrl.replace("/health", ""))
                .status("down")
                .latencyMs(null)
                .error(e.getMessage())
                .build();
        }
    }
}

═══════════════════════════════════════════
GLOBAL FILTERS:
═══════════════════════════════════════════

@Component
public class LoggingGlobalFilter implements GlobalFilter {
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Request: {} {}", 
            exchange.getRequest().getMethod(),
            exchange.getRequest().getPath());
        
        long startTime = System.currentTimeMillis();
        
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long duration = System.currentTimeMillis() - startTime;
            log.info("Response: {} ms", duration);
        }));
    }
}

═══════════════════════════════════════════
CIRCUIT BREAKER CONFIGURATION:
═══════════════════════════════════════════

@Configuration
public class CircuitBreakerConfiguration {
    
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
            .circuitBreakerConfig(CircuitBreakerConfig.custom()
                .slidingWindowSize(10)
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(30))
                .build())
            .timeLimiterConfig(TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(30))
                .build())
            .build());
    }
}

═══════════════════════════════════════════
ESTRUCTURA PROYECTO:
═══════════════════════════════════════════

leka-gateway/
├── src/main/java/com/codeflowx/gateway/
│   ├── GatewayApplication.java
│   ├── config/
│   │   ├── GatewayConfiguration.java
│   │   └── CircuitBreakerConfiguration.java
│   ├── controller/
│   │   └── GatewayHealthController.java
│   ├── filter/
│   │   └── LoggingGlobalFilter.java
│   ├── model/
│   │   ├── ServiceStatus.java
│   │   └── ServicesStatusResponse.java
│   └── fallback/
│       └── FallbackController.java
├── src/main/resources/
│   ├── application.yml
│   └── application-prod.yml
├── pom.xml
├── Dockerfile
└── README.md

═══════════════════════════════════════════
POM.XML DEPENDENCIES:
═══════════════════════════════════════════

<dependencies>
    <!-- Spring Cloud Gateway -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>
    
    <!-- Circuit Breaker -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-circuitbreaker-reactor-resilience4j</artifactId>
    </dependency>
    
    <!-- Actuator for health checks -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
</dependencies>

Por favor genera:
1. Spring Cloud Gateway completo
2. Routes configuration (application.yml)
3. Health check controller
4. Circuit breaker config
5. Global filters (logging, CORS)
6. Fallback controllers
7. pom.xml
8. Dockerfile
9. README.md con ejemplos

IMPORTANTE:
✅ Spring Cloud Gateway (NO Zuul legacy)
✅ Reactive (WebFlux)
✅ Circuit breaker (Resilience4j)
✅ Health checks
✅ Retry logic
❌ NO business logic
❌ NO persistencia
```

---

## 💬 CHAT 7: AI INTERPRETER - MISTRAL 7B (Puerto 8011)

### **PROMPT ESPECÍFICO:**

```
Necesito microservicio Python STATELESS para interpretar resultados técnicos usando LLM local (Mistral 7B).

CONTEXTO:
- Framework: FastAPI
- Puerto: 8011
- LLM Local: Mistral 7B Instruct
- Stateless: NO DB, solo interpretación en tiempo real
- Input: JSON técnico + pregunta usuario
- Output: Explicación en lenguaje natural

RESPONSABILIDAD ÚNICA:
Convertir resultados técnicos de análisis ML en explicaciones comprensibles para usuarios no técnicos.
DIFERENCIADOR CLAVE: UX superior, GDPR Art. 22 compliant (derecho a explicación).

═══════════════════════════════════════════
CASOS DE USO PRINCIPALES:
═══════════════════════════════════════════

Ver documento: LLM_VALUE_PROPOSITION.md

1. Explicar rechazo/decisión modelo
2. Interpretar drift para stakeholders
3. Responder preguntas usuarios sobre decisiones IA
4. Generar reportes ejecutivos automáticos
5. Análisis de causa raíz explicado
6. Chat interactivo sobre resultados governance

═══════════════════════════════════════════
ENDPOINTS A CREAR:
═══════════════════════════════════════════

1. POST /api/interpret/explain-result
2. POST /api/interpret/answer-question  
3. POST /api/interpret/generate-executive-summary
4. POST /api/interpret/root-cause-analysis
5. POST /api/interpret/chat
6. GET /health

(Ver prompt detallado que te di arriba con todos los inputs/outputs)

═══════════════════════════════════════════
INTEGRACIÓN MODELO LOCAL:
═══════════════════════════════════════════

MODELO RECOMENDADO: microsoft/Phi-3-mini-4k-instruct ⭐ MVP

RAZÓN:
✅ Ultraligero: 3.8B parámetros
✅ Funciona en CPU (sin GPU necesaria)
✅ RAM: Solo 4-5GB
✅ Sorprendentemente capaz para su tamaño
✅ Contexto: 4K tokens (suficiente para explicaciones)
✅ Español e inglés aceptables
✅ Gratis, open-source (MIT)

CARGAR AL INICIAR SERVICIO (CPU mode):
```python
from transformers import AutoTokenizer, AutoModelForCausalLM
import torch

# Global - cargar UNA VEZ (CPU mode para MVP)
tokenizer = AutoTokenizer.from_pretrained("microsoft/Phi-3-mini-4k-instruct", trust_remote_code=True)
model = AutoModelForCausalLM.from_pretrained(
    "microsoft/Phi-3-mini-4k-instruct",
    torch_dtype=torch.float32,  # float32 para CPU
    device_map="cpu",  # Forzar CPU
    trust_remote_code=True
)

def generate_explanation(prompt: str) -> str:
    inputs = tokenizer(prompt, return_tensors="pt")
    outputs = model.generate(
        **inputs,
        max_new_tokens=300,
        temperature=0.3,  # Bajo para explicaciones factuales
        do_sample=True,
        top_p=0.9
    )
    return tokenizer.decode(outputs[0], skip_special_tokens=True)
```

HARDWARE REQUERIDO (MVP):
- CPU: 4-8 cores
- RAM: 6GB mínimo (4GB modelo + 2GB overhead)
- Storage: 10GB
- Performance: 5-10 seg por explicación (aceptable para MVP)

UPGRADE PATH (Post-MVP):
- GPU: Cambiar a Mistral 7B (1-3 seg)
- Cost: €300/mes GPU cloud
- RAM: 16GB GPU

═══════════════════════════════════════════
PROMPT TEMPLATES (Ejemplos):
═══════════════════════════════════════════

TEMPLATE EXPLICACIÓN BIAS:
```
Eres experto en AI governance que explica resultados técnicos de manera clara.

ANÁLISIS TÉCNICO:
Demographic Parity Difference: 0.15
Classification: MODERATE
Equal Opportunity: 0.12

CONTEXTO:
Modelo: fraud-detection-v1
Industria: Finanzas
Uso: Credit scoring

Explica en español, lenguaje simple, 150 palabras máximo.
Incluye: qué significa, implicaciones, recomendaciones.

EXPLICACIÓN:
```

TEMPLATE ROOT CAUSE:
```
Analiza causa raíz de drift detectado.

PROBLEMA: Drift en feature 'income' (HIGH severity)
MÉTRICA: KS statistic = 0.45
PERÍODO: Q4 2024

CONTEXTO NEGOCIO:
- Inflación 2024: +8%
- Nuevo sistema reporting: Oct 2024

Identifica 2-3 causas más probables.
Prioriza por likelihood.
Sugiere acciones específicas.

ANÁLISIS:
```

═══════════════════════════════════════════
FRAMEWORKS A USAR:
═══════════════════════════════════════════

✅ FastAPI
✅ transformers (Hugging Face)
✅ torch (PyTorch)
✅ Jinja2 (templates)
✅ pydantic (models)

NO:
❌ LLMs comerciales (OpenAI/Anthropic) - SOLO local
❌ Base datos
❌ LangChain (opcional, si ayuda con templates)

═══════════════════════════════════════════
OPTIMIZACIONES CRÍTICAS:
═══════════════════════════════════════════

✅ Cargar modelo AL INICIAR servicio (no por request)
✅ Caché en memoria para preguntas similares
✅ Batch processing si múltiples requests
✅ Temperature 0.3 (factuales, no creatividad)
✅ max_tokens optimizado por endpoint
✅ Timeout 30 segundos
✅ Fallback: Si falla LLM, respuesta template simple

═══════════════════════════════════════════
ESTRUCTURA PROYECTO:
═══════════════════════════════════════════

leka-ai-interpreter/
├── main.py
├── services/
│   ├── llm_service.py           # Wrapper Mistral
│   ├── prompt_builder.py        # Construir prompts
│   ├── response_parser.py       # Parsear responses
│   └── cache_service.py         # Cache en memoria
├── templates/
│   ├── bias_explanation.jinja2
│   ├── drift_explanation.jinja2
│   ├── quality_explanation.jinja2
│   ├── executive_summary.jinja2
│   └── root_cause.jinja2
├── models/
│   └── schemas.py               # Pydantic models
├── requirements.txt
├── Dockerfile
└── README.md

═══════════════════════════════════════════
DOCKERFILE (GPU Optimizado):
═══════════════════════════════════════════

FROM python:3.11-slim

RUN apt-get update && apt-get install -y \
    git \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Descargar modelo en build (caché Docker layer) - Phi-3 Mini CPU
RUN pip install --no-cache-dir transformers torch
RUN python3 -c "from transformers import AutoTokenizer, AutoModelForCausalLM; \
    print('Downloading Phi-3 Mini...'); \
    AutoTokenizer.from_pretrained('microsoft/Phi-3-mini-4k-instruct', trust_remote_code=True); \
    AutoModelForCausalLM.from_pretrained('microsoft/Phi-3-mini-4k-instruct', \
        torch_dtype='float32', trust_remote_code=True); \
    print('Model cached successfully')"

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 8011

# CPU mode (sin GPU)
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8011"]

═══════════════════════════════════════════
REQUIREMENTS.TXT:
═══════════════════════════════════════════

fastapi==0.104.1
uvicorn==0.24.0
pydantic==2.5.0
transformers==4.36.0
torch==2.1.0
accelerate==0.25.0
sentencepiece==0.1.99
protobuf==4.25.1
jinja2==3.1.2

═══════════════════════════════════════════
DELIVERABLES:
═══════════════════════════════════════════

Por favor genera:
1. main.py con 6 endpoints completos
2. llm_service.py (wrapper Mistral con caché)
3. prompt_builder.py (templates Jinja2 para cada caso)
4. response_parser.py (extraer info limpia de LLM)
5. cache_service.py (caché en memoria)
6. Pydantic schemas (todos los Request/Response)
7. requirements.txt
8. Dockerfile optimizado GPU
9. README.md con:
   - Hardware requirements
   - Instalación modelo
   - Ejemplos curl
   - Tips optimización GPU
   - Fallback sin GPU (CPU mode)

IMPORTANTE:
✅ Modelo cargado UNA VEZ al inicio
✅ Caché respuestas similares (evitar re-inferencia)
✅ Temperature BAJA (0.3) para factuales
✅ Timeout 30s
✅ Fallback automático si LLM falla
✅ Logging detallado
❌ NO persistencia
❌ NO LLMs comerciales

Usa FastAPI, transformers, Mistral 7B Instruct, buenas prácticas Python.
```

---

## 💬 CHAT 8: CLIENTE JAVA UNIFICADO

### **PROMPT ESPECÍFICO:**

```
Cliente REST Java para consumir Gateway Spring Cloud desplegado en Kubernetes.

CONTEXTO:
- Spring Boot RestTemplate
- Habla con Gateway Spring Cloud (NO con micros individuales)
- Abstrae complejidad microservicios
- Package: com.codeflowx.platform.integration.ml
- Gateway K8s: `api-leka-govern` (puerto 8000)

═══════════════════════════════════════════
GATEWAY KUBERNETES:
═══════════════════════════════════════════

GATEWAY DESPLEGADO EN K8S:
- Nombre servicio: `api-leka-govern`
- Puerto: 8000
- Mismo namespace: `http://api-leka-govern:8000`
- Namespace diferente: `http://api-leka-govern.codeflowx-demo.svc.cluster.local:8000`

URL BASE CONFIGURABLE:
- Mismo namespace (recomendado si mismo K8s): `http://api-leka-govern:8000`
- Namespace diferente: `http://api-leka-govern.codeflowx-demo.svc.cluster.local:8000`
- Local/desarrollo: `http://localhost:8000`

═══════════════════════════════════════════
CLASE PRINCIPAL:
═══════════════════════════════════════════

public class AIGovernanceClient {
    
    private final RestTemplate restTemplate;
    private final String gatewayUrl;  // Configurable: K8s service URL o localhost
    
    // Métodos para TODOS los tipos de evaluación
    
    // Tabular analysis
    public BiasAnalysisResponse analyzeBias(BiasRequest request);
    public DriftDetectionResponse detectDrift(DriftRequest request);
    public DataQualityResponse validateQuality(DataQualityRequest request);
    
    // LLM evaluation
    public HallucinationResponse evaluateLLMHallucination(LLMRequest request);
    public ToxicityResponse evaluateLLMToxicity(ToxicityRequest request);
    public LLMQualityResponse evaluateLLMQuality(LLMQualityRequest request);
    
    // Prompt governance
    public PromptSafetyResponse evaluatePromptSafety(PromptRequest request);
    public PromptEffectivenessResponse evaluatePromptEffectiveness(PromptRequest request);
    
    // RAG evaluation
    public RAGRetrievalResponse evaluateRAGRetrieval(RAGRetrievalRequest request);
    public RAGAnswerResponse evaluateRAGAnswer(RAGAnswerRequest request);
    
    // Agent monitoring
    public AgentExecutionResponse analyzeAgentExecution(AgentExecutionRequest request);
    public AgentReliabilityResponse evaluateAgentReliability(AgentReliabilityRequest request);
    
    // Model wrapper
    public ModelInvokeResponse invokeModel(ModelInvokeRequest request);
    
    // AI Interpreter (NUEVO - diferenciador)
    public ExplanationResponse explainResult(ExplainRequest request);
    public AnswerResponse answerQuestion(QuestionRequest request);
    public ExecutiveSummaryResponse generateSummary(SummaryRequest request);
    
    // Health
    public ServicesStatusResponse getServicesStatus();
}

═══════════════════════════════════════════
IMPLEMENTACIÓN MÉTODOS:
═══════════════════════════════════════════

Todos los métodos siguen mismo patrón:
1. Construyen request HTTP
2. Llaman Gateway: POST http://localhost:8000/api/[modulo]/[endpoint]
3. Parsean response JSON
4. Manejo errores (timeout, circuit breaker open, etc.)

Ejemplo:
```java
public BiasAnalysisResponse analyzeBias(BiasRequest request) {
    String url = gatewayUrl + "/api/tabular/bias-analysis/analyze";
    
    try {
        ResponseEntity<BiasAnalysisResponse> response = restTemplate.postForEntity(
            url,
            request,
            BiasAnalysisResponse.class
        );
        return response.getBody();
    } catch (HttpClientErrorException e) {
        throw new AIGovernanceException("Error analyzing bias", e);
    } catch (ResourceAccessException e) {
        throw new ServiceUnavailableException("Gateway unavailable", e);
    }
}
```

═══════════════════════════════════════════
EXCEPTION HANDLING:
═══════════════════════════════════════════

public class AIGovernanceException extends RuntimeException {
    private final int statusCode;
    private final String service;
}

public class ServiceUnavailableException extends AIGovernanceException {
    // 503 - Servicio no disponible
}

public class CircuitBreakerOpenException extends AIGovernanceException {
    // Circuit breaker abierto
}

═══════════════════════════════════════════
CONFIGURATION:
═══════════════════════════════════════════

// application.properties
# Gateway K8s (mismo namespace) - RECOMENDADO producción
ai.governance.gateway.url=http://api-leka-govern:8000

# Gateway K8s (namespace diferente)
# ai.governance.gateway.url=http://api-leka-govern.codeflowx-demo.svc.cluster.local:8000

# Gateway local/desarrollo
# ai.governance.gateway.url=http://localhost:8000

ai.governance.timeout=30000
ai.governance.retry.enabled=true
ai.governance.retry.max-attempts=2

@Configuration
public class AIGovernanceClientConfiguration {
    
    @Value("${ai.governance.gateway.url}")
    private String gatewayUrl;
    
    @Value("${ai.governance.timeout}")
    private int timeout;
    
    @Bean
    public AIGovernanceClient aiGovernanceClient() {
        RestTemplate restTemplate = new RestTemplate();
        
        // Timeout
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(timeout);
        factory.setReadTimeout(timeout);
        restTemplate.setRequestFactory(factory);
        
        // Error handler
        restTemplate.setErrorHandler(new AIGovernanceErrorHandler());
        
        return new AIGovernanceClient(restTemplate, gatewayUrl);
    }
}

═══════════════════════════════════════════
DELIVERABLES:
═══════════════════════════════════════════

Por favor genera:
1. AIGovernanceClient.java completo
2. Todos los DTOs (Request/Response para cada módulo)
3. Exception classes
4. Configuration class
5. Error handler
6. application.properties ejemplo
7. Tests unitarios (mock Gateway)
8. README.md con ejemplos de uso

IMPORTANTE:
✅ Hablar solo con Gateway Spring Cloud (servicio K8s `api-leka-govern:8000`)
✅ URL configurable vía properties (mismo namespace, namespace diferente, o localhost)
✅ Todos los módulos cubiertos (tabular, LLM, prompt, RAG, agent, models, interpreter)
✅ Exception handling robusto
✅ Timeout y retry configurables
✅ Detectar automáticamente si está en K8s o local (opcional: auto-discovery)
❌ NO llamar micros directamente
❌ NO persistencia (eso lo hace BusinessService)

NOTA KUBERNETES:
- En producción K8s usar: `http://api-leka-govern:8000` (mismo namespace)
- Si cliente Java está en namespace diferente: usar FQDN completo
- Para desarrollo local: `http://localhost:8000`
```

---

## ☸️ KUBERNETES DEPLOYMENT

### **Manifests para cada microservicio:**

```yaml
# NOTA: Cada Chat debe generar su Dockerfile
# Kubernetes yamls se crean después de tener Dockerfiles

DEPLOYMENT ORDEN:
1. Cada micro genera Dockerfile
2. Build images: docker build -t leka-[micro]:latest .
3. Push a registry (opcional): docker push registry/leka-[micro]:latest
4. Kubectl apply deployments

EJEMPLO deployment.yaml (por micro):
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: leka-llm-evaluation
spec:
  replicas: 2
  selector:
    matchLabels:
      app: leka-llm-evaluation
  template:
    metadata:
      labels:
        app: leka-llm-evaluation
    spec:
      containers:
      - name: llm-evaluation
        image: leka-llm-evaluation:latest
        ports:
        - containerPort: 8002
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "1Gi"
            cpu: "1000m"
---
apiVersion: v1
kind: Service
metadata:
  name: leka-llm-evaluation
spec:
  selector:
    app: leka-llm-evaluation
  ports:
  - port: 8002
    targetPort: 8002
```

PARA GATEWAY (Spring Cloud Gateway):
- ServiceType: LoadBalancer o Ingress
- Puerto expuesto: 8000

PARA AI INTERPRETER (CPU - MVP):
- Resources:
  * requests: memory 4Gi, cpu 2
  * limits: memory 6Gi, cpu 4
- Performance: 5-10 seg/explicación (aceptable MVP)
- Upgrade: GPU T4 post-MVP (1-3 seg)

---

## ⏰ TIMELINE REALISTA WEEKEND

```
VIERNES NOCHE (20:00-24:00):
├─ Lanzar 6 chats (Micros 2-7)
├─ Copiar prompts específicos
└─ Monitorear generación código (4h)

SÁBADO (09:00-24:00):
├─ 09:00-13:00: Revisar código generado
├─ 14:00-19:00: Crear Dockerfiles para cada micro
├─ 20:00-24:00: Testing individual (cada micro solo)
└─ Meta: 6 micros funcionando independientes

DOMINGO (09:00-24:00):
├─ 09:00-13:00: Integrar Orchestrator
├─ 14:00-18:00: Kubernetes deployment
├─ 19:00-22:00: Testing integrado end-to-end
├─ 23:00-24:00: Cliente Java + docs
└─ Meta: TODO integrado funcionando

LUNES-MARTES:
├─ Lunes: Lanzamiento marketing
├─ Martes: Demos funcionando con arquitectura completa
└─ Meta: Producto COMPLETO operativo
```

---

## 🎯 VENTAJA DE ESTE APPROACH

```
ANTES (mi error):
└─ Pensaba en monolito
└─ 1 chat haciendo todo
└─ 2-3 semanas

AHORA (tu approach):
└─ 6 micros independientes
└─ 6 chats en paralelo
└─ 3 días (viernes-domingo)

RESULTADO:
✅ 6x más rápido (paralelización)
✅ Código modular (microservicios)
✅ Escalable (cada micro scale independiente)
✅ Mantenible (aislar problemas)
```

---

## ✅ CREO LOS PROMPTS COMPLETOS AHORA

Generando documento con:
- 6 prompts microservicios Python
- 1 prompt Orchestrator
- 1 prompt Cliente Java
- Kubernetes manifests
- Plan integración weekend

---

## 📊 RESUMEN ARQUITECTURA COMPLETA

### **Stack completo después del weekend:**

```
CAPA 1: CLIENTES
├─ Java Backend (suinsit.nova.web) → Llama Orchestrator
├─ Python clients externos → Llama Orchestrator
├─ MCP Agents → Llama Orchestrator
└─ Direct REST → Llama Orchestrator

CAPA 2: ORCHESTRATOR (8000)
└─ API Gateway unificado
└─ Routing inteligente

CAPA 3: MICROSERVICIOS PYTHON (Stateless)
├─ 8001: Tabular Analysis (✅ YA EXISTE)
├─ 8002: LLM Evaluation (🔧 Chat 1)
├─ 8003: Prompt Governance (🔧 Chat 2)
├─ 8004: RAG Evaluation (🔧 Chat 3)
├─ 8005: Agent Monitoring (🔧 Chat 4)
└─ 8006: Model Wrapper (🔧 Chat 5)

CAPA 4: BACKEND JAVA (Stateful)
└─ PostgreSQL 170 tablas
└─ Persiste resultados de micros
└─ BusinessService + JPA

DEPLOYMENT:
└─ Kubernetes (cada micro independiente)
└─ Spring Cloud Gateway como entry point
```

---

## 🎯 CHECKLIST EJECUCIÓN WEEKEND

### **VIERNES NOCHE (4 horas):**

```
☐ 20:00 - Abrir 8 chats IA:
   Chat 1: Claude.ai → Micro LLM Evaluation (8002)
   Chat 2: ChatGPT → Micro Prompt Governance (8003)
   Chat 3: Claude.ai → Micro RAG Evaluation (8004)
   Chat 4: ChatGPT → Micro Agent Monitoring (8005)
   Chat 5: Claude.ai → Micro Model Wrapper (8006)
   Chat 6: ChatGPT → Spring Cloud Gateway (8000)
   Chat 7: Claude.ai → AI Interpreter Mistral 7B (8011) ⭐
   Chat 8: ChatGPT → Cliente Java Unified

☐ 20:15 - Copiar prompts de este documento a cada chat

PRIORIDAD (si no tienes 8 chats disponibles):
   1º Gateway (Chat 6) - Sin esto nada funciona
   2º LLM Eval (Chat 1) - Core value
   3º Prompt Gov (Chat 2) - Core value
   4º AI Interpreter (Chat 7) - Diferenciador UX ⭐
   5º Model Wrapper (Chat 5) - Para invocar modelos
   6º RAG Eval (Chat 3)
   7º Agent Monitor (Chat 4)
   8º Cliente Java (Chat 8) - Puede ser después

☐ 20:30-23:30 - Monitorear generación código:
   └─ Revisar cada 30 min
   └─ Responder preguntas de los chats
   └─ Descargar código cuando esté listo

☐ 23:30-24:00 - Organizar código:
   /microservices-generated/
   ├── leka-llm-evaluation/
   ├── leka-prompt-governance/
   ├── leka-rag-evaluation/
   ├── leka-agent-monitoring/
   ├── leka-model-wrapper/
   └── leka-orchestrator/

☐ Git commit: "feat: 6 microservicios generados por IA"
```

### **SÁBADO (12-14 horas):**

```
☐ 09:00-13:00 - Revisar y ajustar código:
   ├─ Verificar cada micro compila sin errores
   ├─ requirements.txt correctos
   ├─ Dockerfiles válidos
   └─ Corregir imports/dependencias

☐ 14:00-19:00 - Testing individual:
   ├─ Lanzar cada micro uno por uno
   ├─ Probar endpoints con curl
   ├─ Verificar responses correctos
   └─ Anotar errores para fixes

☐ 20:00-24:00 - Fixes iterativos:
   ├─ Distribuir errores entre chats
   ├─ Chats generan fixes
   ├─ Aplicar fixes
   └─ Relanzar hasta 6/6 funcionando

META SÁBADO: 6 micros funcionando independientes
```

### **DOMINGO (12-14 horas):**

```
☐ 09:00-13:00 - Orchestrator integration:
   ├─ Configurar routing
   ├─ Health checks
   ├─ Testing routing correcto
   └─ Verificar: Request → Orchestrator → Micro → Response

☐ 14:00-18:00 - Kubernetes deployment:
   ├─ Verificar Dockerfiles de cada micro
   ├─ kubectl apply -f k8s/ (manifests básicos)
   ├─ Verificar pods running: kubectl get pods
   ├─ Testing end-to-end
   └─ http://localhost:8000/api/v1/services/status ✅

☐ 19:00-22:00 - Cliente Java:
   ├─ Integrar Chat 7 (Cliente Java)
   ├─ AIGovernanceClient.java completo
   ├─ Testing desde Java backend
   └─ Llamadas funcionando

☐ 23:00-24:00 - Documentación rápida:
   ├─ README.md por microservicio
   ├─ ARCHITECTURE.md general
   ├─ Kubernetes manifests básicos
   └─ Ejemplos curl

META DOMINGO: Arquitectura completa integrada
```

---

## 🎬 LANZAMIENTO LUNES - MENSAJE ACTUALIZADO

### **Post LinkedIn con arquitectura completa:**

```
🚀 CodeFlowX: La Plataforma COMPLETA de AI Governance

12 meses construyendo en silencio.
Hoy lo lanzamos.

No es un módulo. No es un nicho.
Es GOVERNANCE COMPLETO end-to-end.

🔥 LO QUE LANZAMOS HOY:

ARQUITECTURA MICROSERVICIOS:
✅ 6 servicios Python independientes
✅ 1 Orchestrator unificado
✅ APIs REST + MCP native
✅ Escalable, modular, production-ready

CAPACIDADES:

📊 Modelos ML Clásicos:
• Bias Analysis (3 métricas)
• Drift Detection (5 tests)
• Data Quality (8 validaciones)
• Explainability (SHAP/LIME)
• Privacy (k-anonymity, l-diversity)
• Robustness, Performance, Uncertainty

🤖 LLMs & Generative AI:
• Hallucination detection
• Toxicity & bias en texto
• Quality evaluation
• Cost tracking

✍️ Prompt Governance:
• Safety checks (injection, jailbreak)
• PII detection
• Effectiveness scoring
• Versioning & A/B testing

🔍 RAG Systems:
• Retrieval quality
• Answer accuracy
• Groundedness
• Full pipeline evaluation

🎯 AI Agents:
• Execution analysis
• Reliability scoring
• Cost tracking
• Loop detection

🌐 Model Wrapper:
• OpenAI, Anthropic, Cohere, Groq
• Unified interface
• Multi-provider comparison

📋 COMPLIANCE:
✅ EU AI Act (Art. 10, 11, 13, 15, 61)
✅ GDPR (Art. 5, 22, 25, 32)
✅ Audit trail completo
✅ Model Cards automáticos

🎁 EARLY ACCESS (15 clientes):
€35K año 1 (50% OFF vs €70K regular)
Plataforma COMPLETA desde día 1

Cierra: 15 Noviembre

📹 DEMO: [link]
📄 DOCS: www.codeflowx.com

No es un paper. No es un concepto.
Es SOFTWARE funcionando HOY.

¿Quién quiere ser early adopter? 👇

#AIGovernance #MCP #LLM #MLOps #EUAIAct
```

---

## 📝 ORDEN DE LECTURA DE PROMPTS

```
1. Lee este documento completo (30 min)
2. Abre 6 chats IA
3. Copia prompts en orden:
   - Chat 1: Prompt CHAT 1
   - Chat 2: Prompt CHAT 2
   - Chat 3: Prompt CHAT 3
   - Chat 4: Prompt CHAT 4
   - Chat 5: Prompt CHAT 5
   - Chat 6: Prompt CHAT 6
4. Envía todos a la vez
5. Monitorea respuestas
6. Descarga código cuando esté listo
```

---

## ✅ CRITERIOS DONE

```
DOMINGO NOCHE:
☐ 7 servicios corriendo en Docker
☐ Orchestrator routing correcto
☐ Cliente Java funcionando
☐ Testing end-to-end pasando
☐ Documentación básica lista

LUNES:
☐ Post LinkedIn publicado
☐ Arquitectura completa funcionando
☐ Demos agendables para semana próxima

RESULTADO:
└─ Plataforma COMPLETA (no MVP limitado)
└─ 6 microservicios operativos
└─ Competidor: Aún escribiendo paper
└─ Tú: Producto COMPLETO funcionando
```

---

**Última actualización:** 1 Noviembre 2025, 23:00  
**Status:** 📝 PROMPTS LISTOS - LANZAR CHATS AHORA  
**Timeline:** Viernes 23:00 → Martes 12:00 (TODO integrado)
