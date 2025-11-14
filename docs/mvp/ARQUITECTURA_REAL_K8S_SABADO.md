# 🏗️ ARQUITECTURA REAL KUBERNETES - SÁBADO 1 NOV 2025

**Estado:** Todos los microservicios desplegados en K8s  
**Pendiente:** Cliente Java unificado  
**Fecha:** Sábado 1 Noviembre 2025

---

## 📊 MICROSERVICIOS DESPLEGADOS EN K8S

### **Basado en READMEs leídos:**

```
✅ leka-bias-detection-service (8001) - Análisis tabular
✅ leka-llm-evaluation (8002) - LLM evaluation con DeepEval
✅ leka-prompt-governance (8003) - Prompt safety con Presidio
✅ leka-rag-evaluation (8004) - RAG quality evaluation
✅ leka-agent-monitoring (8005) - Agent monitoring enterprise-grade
✅ leka-model-wrapper (8006) - Wrapper modelos comerciales
✅ leka-server-serving-wrapper (?) - Serving LLM, embeddings, audio, imagen, vector stores

PENDIENTE:
✅ Spring Cloud Gateway (8000) - DESPLEGADO como `api-leka-govern`
   - Mismo namespace: `api-leka-govern:8000`
   - Namespace diferente: `api-leka-govern.codeflowx-demo.svc.cluster.local:8000`
🔧 AI Interpreter Phi-3 Mini CPU (8011) - ¿Ya existe o falta?
🔧 Cliente Java Unified - FALTA (tarea hoy)
```

---

## 📋 CARACTERÍSTICAS IMPLEMENTADAS (Por README)

### **leka-llm-evaluation (8002):**
```
VERSIÓN: 2.0.0 - "Nivel 11/10"

FRAMEWORKS AVANZADOS:
✅ DeepEval (framework profesional LLM evaluation)
✅ sentence-transformers (embeddings semánticos)
✅ Detoxify (modelo pre-entrenado toxicity)
✅ Prometheus (métricas)
✅ Structlog (logging estructurado JSON)

ENDPOINTS:
- POST /api/llm/evaluate-hallucination
- POST /api/llm/evaluate-toxicity
- POST /api/llm/evaluate-bias-text
- POST /api/llm/evaluate-quality
- POST /api/llm/batch-evaluate
- GET /health

STATELESS: ✅ Solo analiza, retorna JSON
```

### **leka-prompt-governance (8003):**
```
FRAMEWORKS ENTERPRISE:
✅ Microsoft Presidio (PII detection enterprise-grade)
✅ spaCy NER (reconocimiento entidades)
✅ sentence-transformers (análisis semántico)
✅ structlog (logging)
✅ Prometheus (métricas)
✅ 50+ patrones seguridad 2024-2025

ENDPOINTS:
- POST /api/prompt/evaluate-safety
- POST /api/prompt/evaluate-effectiveness
- POST /api/prompt/detect-pii-leakage
- POST /api/prompt/compare-versions
- POST /api/prompt/analyze-cost
- GET /health
- GET /metrics

STATELESS: ✅
```

### **leka-rag-evaluation (8004):**
```
FRAMEWORKS:
✅ RAGAS (RAG Assessment)
✅ sentence-transformers
✅ scikit-learn

ENDPOINTS:
- POST /api/rag/evaluate-retrieval
- POST /api/rag/evaluate-answer
- POST /api/rag/evaluate-context-relevance
- POST /api/rag/evaluate-full-pipeline
- GET /health

MÉTRICAS:
- Precision, Recall, MRR, NDCG
- Groundedness, Relevance, Correctness
- Hallucination score
- Faithfulness, Context precision

STATELESS: ✅
```

### **leka-agent-monitoring (8005):**
```
VERSIÓN: "Enterprise 11/10"

CARACTERÍSTICAS ENTERPRISE:
✅ Structured Logging (structlog JSON)
✅ Prometheus Metrics
✅ Request ID Tracking
✅ Health Checks avanzados (ready, live, startup) - K8s ready
✅ Rate Limiting configurable
✅ Security Headers automáticos
✅ CORS configurable
✅ Error Handling centralizado
✅ Middleware pipeline eficiente

ENDPOINTS:
- POST /api/agent/analyze-execution
- POST /api/agent/evaluate-reliability
- POST /api/agent/analyze-cost
- POST /api/agent/detect-loops
- GET /health (+ /health/ready, /health/live, /health/startup)
- GET /metrics

QUALITY:
✅ Code quality: black, ruff, mypy
✅ Testing: pytest
✅ CI/CD: GitHub Actions ready
✅ Makefile con comandos útiles

STATELESS: ✅
```

### **leka-model-wrapper (8006):**
```
CARACTERÍSTICAS:
✅ Unified interface para múltiples providers
✅ API Keys en HEADERS (no env vars) - Como modelos comerciales
✅ Cost tracking automático
✅ Latency monitoring
✅ Batch processing
✅ Model comparison
✅ Circuit breakers
✅ Rate limiting
✅ Caching inteligente (TTL + LRU)
✅ OpenTelemetry + Prometheus

PROVIDERS SOPORTADOS:
- OpenAI (GPT-4, GPT-3.5, etc.)
- Anthropic (Claude 3)
- Cohere
- Groq

ENDPOINTS:
- POST /api/models/invoke
- POST /api/models/batch-invoke
- POST /api/models/compare-responses
- GET /api/models/list-available
- GET /health
- GET /metrics

AUTENTICACIÓN:
- Authorization: Bearer sk-...
- X-API-Key: sk-...
- X-OpenAI-API-Key: sk-...
- X-Anthropic-API-Key: sk-ant-...

STATELESS: ✅
```

### **leka-server-serving-wrapper (Puerto ?):**
```
MEGA SERVICIO UNIFICADO:

CAPACIDADES:
✅ LLM Chat (vía LangChain)
✅ Embeddings (múltiples providers)
✅ Audio (transcripción, clasificación, generación)
✅ Imagen (captioning, clasificación, análisis)
✅ Bases datos vectoriales (ChromaDB, Pinecone, Weaviate, Qdrant)
✅ REST API + gRPC
✅ Pool de modelos
✅ Métricas avanzadas

ENDPOINTS:
- POST /api/v1/chat/chat
- POST /api/v1/chat/simple
- POST /api/v1/generate
- POST /api/v1/embeddings/encode
- POST /api/v1/embeddings/similarity
- GET /management/models/list
- GET /management/providers/list
- GET /management/monitoring/status
- GET /management/system/info

PROVIDERS:
- OpenAI, Anthropic, Google, Cohere, Groq
- Together, Ollama
- Multimodal providers

VECTOR STORES:
- ChromaDB, Pinecone, Weaviate, Qdrant

PUERTO: ¿8007 o diferente?
```

---

## ✅ GATEWAY DESPLEGADO

**Spring Cloud Gateway:**
- **Nombre servicio K8s:** `api-leka-govern`
- **Puerto:** 8000
- **Mismo namespace:** `http://api-leka-govern:8000`
- **Namespace diferente:** `http://api-leka-govern.codeflowx-demo.svc.cluster.local:8000`
- **Estado:** ✅ Desplegado

---

## ❓ PREGUNTAS RESTANTES

1. **¿Qué puerto tiene leka-server-serving-wrapper?**
2. **¿AI Interpreter (Phi-3 Mini CPU) ya está o falta?**

---

## 🎯 PENDIENTE: CLIENTE JAVA UNIFICADO

**Cliente Java debe hablar con:**
- ✅ Spring Cloud Gateway: `api-leka-govern:8000` (mismo namespace)
- ✅ O: `api-leka-govern.codeflowx-demo.svc.cluster.local:8000` (namespace diferente)

**URLs base configurables:**
- Mismo namespace: `http://api-leka-govern:8000`
- Namespace diferente: `http://api-leka-govern.codeflowx-demo.svc.cluster.local:8000`

**Actualizando prompt del Cliente Java con esta configuración...**

