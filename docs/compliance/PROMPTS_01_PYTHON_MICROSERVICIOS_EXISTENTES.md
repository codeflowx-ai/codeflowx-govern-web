# PROMPTS - MODIFICACIÓN MICROSERVICIOS PYTHON EXISTENTES
## EU AI ACT COMPLIANCE - Extensión de Funcionalidades

**Equipo:** Python Team - Microservicios Existentes  
**Fecha:** 15 de noviembre de 2025  
**Objetivo:** Extender 9 microservicios Python existentes con funcionalidades de cumplimiento EU AI Act  
**Esfuerzo Estimado:** 12-15 días (con 3-4 chats en paralelo)

---

## 🏗️ ARQUITECTURA ACTUAL

**Microservicios Python Existentes:**
```
✅ leka-bias-detection-service (puerto 8001)
✅ leka-llm-evaluation (puerto 8002)
✅ leka-prompt-governance (puerto 8003)
✅ leka-rag-evaluation (puerto 8004)
✅ leka-agent-monitoring (puerto 8005)
✅ leka-model-wrapper (puerto 8006)
✅ leka-server-serving-wrapper (puerto ?)
✅ leka-llm-interpreter (puerto 8011)
✅ leka-orchestrator (puerto 8000) - Spring Cloud Gateway Java
```

**Principio:** Microservicios STATELESS (sin BD propia), solo análisis y retorno JSON.

---

## 📋 GRUPOS DE PROMPTS

### **GRUPO A: DETECCIÓN ADVERSARIAL Y ROBUSTEZ (3-4 días)**

---

### **PROMPT A.1 - Extensión leka-bias-detection-service**

**Contexto:**
Microservicio existente en `/mnt/c/Users/ManuelGonzalez/git/leka-bias-detection-service` que analiza datasets tabulares para detección de sesgos.

**Nuevas Funcionalidades EU AI Act:**

**Art. 15.5 - Adversarial Robustness:**
1. **Detección Data Poisoning** (envenenamiento datos entrenamiento)
2. **Detección estadística de anomalías** en datasets
3. **Validación integridad datasets** antes de entrenamiento

**Art. 10.2.f - Detección sesgos ampliados:**
4. **Análisis sesgos protegidos** (género, edad, etnia, religión, orientación sexual)
5. **Detección sesgos geográficos/culturales**
6. **Interseccionalidad** (sesgos combinados)

**Prompt Específico:**

```
Necesito extender el microservicio leka-bias-detection-service con nuevas funcionalidades de EU AI Act Art. 15.5 y Art. 10.

MICROSERVICIO ACTUAL:
- Ubicación: /mnt/c/Users/ManuelGonzalez/git/leka-bias-detection-service
- Puerto: 8001
- Framework: FastAPI
- Funcionalidad actual: Detección sesgos básicos en datos tabulares

NUEVOS ENDPOINTS A CREAR:

1. POST /api/bias/detect-data-poisoning
   Input: {
     "dataset_csv": "base64_encoded_csv",
     "baseline_stats": {...},  // opcional - para comparar
     "thresholds": {...}  // umbrales detección
   }
   Output: {
     "poisoning_detected": bool,
     "anomalies": [
       {"column": "age", "anomaly_score": 0.87, "reason": "Distribución estadística atípica"},
       {"column": "income", "anomaly_score": 0.92, "reason": "Outliers extremos (3.2% vs esperado 0.1%)"}
     ],
     "statistical_tests": {
       "kolmogorov_smirnov": {...},
       "chi_squared": {...}
     },
     "recommendation": "REVISAR - 2 columnas con anomalías significativas"
   }
   
   Análisis:
   - Tests estadísticos: Kolmogorov-Smirnov, Chi-Squared, Z-scores
   - Detección outliers extremos (IQR, Z-score > 3)
   - Comparación con distribuciones esperadas
   - Detección duplicados sospechosos
   - Análisis correlaciones inesperadas

2. POST /api/bias/detect-protected-attributes
   Input: {
     "dataset_csv": "base64_encoded_csv",
     "model_predictions": [...],  // opcional - si ya existe modelo
     "protected_attributes": ["gender", "age", "ethnicity"],
     "target_variable": "approved"
   }
   Output: {
     "bias_metrics": {
       "gender": {
         "disparate_impact_ratio": 0.72,  // < 0.8 = posible discriminación
         "statistical_parity_difference": 0.18,
         "equal_opportunity_difference": 0.14,
         "predictive_equality_difference": 0.11,
         "severity": "HIGH"
       },
       "age": {
         "disparate_impact_ratio": 0.88,
         "severity": "MEDIUM"
       }
     },
     "intersectionality_analysis": {
       "gender_x_age": {
         "most_affected_group": "female_50+",
         "approval_rate": 0.43,
         "avg_approval_rate": 0.72,
         "impact": -0.29
       }
     },
     "recommendation": "CRÍTICO - Sesgo de género detectado (DI ratio 0.72)"
   }

3. POST /api/bias/validate-dataset-integrity
   Input: {
     "dataset_csv": "base64_encoded_csv",
     "expected_schema": {...},
     "data_quality_rules": {...}
   }
   Output: {
     "integrity_score": 0.94,
     "issues": [
       {"type": "missing_values", "column": "income", "count": 45, "percentage": 0.9%},
       {"type": "duplicates", "count": 23, "percentage": 0.46%},
       {"type": "outliers", "column": "age", "count": 12, "values": [150, 145, ...]}
     ],
     "data_quality_score": 0.91,
     "recommendation": "ACEPTABLE - Issues menores (< 1% datos afectados)"
   }

REQUISITOS TÉCNICOS:
- Usar librerías: scipy, scikit-learn, pandas, numpy
- Métricas fairness: aif360 (AI Fairness 360 - IBM)
- Tests estadísticos robustos
- Umbrales configurables
- Logging estructurado (structlog)
- Métricas Prometheus
- Documentación OpenAPI automática

NO CREAR:
- Base de datos (stateless)
- Autenticación (manejada por gateway)
- UI (solo API)

MANTENER:
- Estructura actual del microservicio
- Endpoints existentes
- Configuración Docker/K8s existente
```

**Artículos Cubiertos:** Art. 10.2.f, Art. 10.2.g, Art. 15.5  
**Esfuerzo:** 2-3 días  
**Prioridad:** 🔴 Crítica

---

### **PROMPT A.2 - Extensión leka-llm-evaluation**

**Contexto:**
Microservicio existente en `/mnt/c/Users/ManuelGonzalez/git/leka-llm-evaluation` para evaluación de LLMs con DeepEval.

**Nuevas Funcionalidades EU AI Act:**

**Art. 15.4 - Feedback Loop Bias Detection:**
1. **Detección amplificación sesgos** en sistemas de aprendizaje continuo
2. **Análisis correlación** output → input en bucles retroalimentación
3. **Drift detection** en distribuciones output

**Art. 15.5 - Adversarial Examples Detection:**
4. **Detección ejemplos adversarios** (inputs diseñados para error)
5. **Prompt injection detection** (avanzado)
6. **Jailbreak detection**

**Prompt Específico:**

```
Necesito extender el microservicio leka-llm-evaluation con detección de feedback loop bias y adversarial examples según EU AI Act Art. 15.4 y 15.5.

MICROSERVICIO ACTUAL:
- Ubicación: /mnt/c/Users/ManuelGonzalez/git/leka-llm-evaluation
- Puerto: 8002
- Framework: FastAPI + DeepEval
- Funcionalidad actual: Evaluación LLMs (hallucination, toxicity, bias, quality)

NUEVOS ENDPOINTS A CREAR:

1. POST /api/llm/detect-feedback-loop-bias
   Input: {
     "model_outputs_time_series": [
       {"timestamp": "2025-11-01T10:00:00", "output": "...", "input": "..."},
       {"timestamp": "2025-11-01T11:00:00", "output": "...", "input": "..."},
       // Serie temporal de inputs/outputs
     ],
     "window_size": 100,  // análisis ventana deslizante
     "bias_metrics": ["sentiment", "toxicity", "topic_distribution"]
   }
   Output: {
     "feedback_loop_detected": bool,
     "bias_amplification": {
       "sentiment": {
         "initial_bias": 0.12,  // Tiempo T0
         "current_bias": 0.34,  // Tiempo TN
         "amplification_factor": 2.83,  // 283% amplificación
         "trend": "INCREASING",
         "severity": "HIGH"
       },
       "topic_distribution": {
         "shift_detected": true,
         "topics_over_represented": ["topic_A", "topic_B"],
         "entropy_change": -0.23  // Pérdida diversidad
       }
     },
     "correlation_analysis": {
       "output_to_input_lag_1": 0.67,  // Correlación fuerte
       "output_to_input_lag_5": 0.45
     },
     "recommendation": "CRÍTICO - Sesgo amplificándose 283% en 30 días. Requiere intervención."
   }
   
   Análisis:
   - Análisis serie temporal (pandas, statsmodels)
   - Detección drift en distribuciones (KL divergence, Jensen-Shannon)
   - Correlación cruzada output(t) → input(t+k)
   - Análisis entropía y diversidad
   - Trend analysis (regresión, ARIMA si procede)

2. POST /api/llm/detect-adversarial-examples
   Input: {
     "model_name": "gpt-4-turbo",
     "test_inputs": [...],  // Lista de inputs a testear
     "expected_behavior": "refuse_harmful_requests",
     "adversarial_techniques": ["prompt_injection", "jailbreak", "role_play", "encoding"]
   }
   Output: {
     "adversarial_vulnerability_score": 0.12,  // 0-1, menor = más robusto
     "successful_attacks": [
       {
         "input": "Ignore previous instructions...",
         "technique": "prompt_injection",
         "model_response": "...",
         "bypassed_safety": true,
         "severity": "HIGH"
       }
     ],
     "robustness_by_technique": {
       "prompt_injection": {"tested": 25, "successful": 3, "success_rate": 0.12},
       "jailbreak": {"tested": 30, "successful": 1, "success_rate": 0.03},
       "role_play": {"tested": 20, "successful": 0, "success_rate": 0.0},
       "encoding": {"tested": 15, "successful": 2, "success_rate": 0.13}
     },
     "overall_robustness": 0.93,  // 93% resistente
     "recommendation": "ACEPTABLE - 93% robustez, mejorar detección encoding"
   }
   
   Análisis:
   - 100+ patrones adversariales conocidos
   - Técnicas: prompt injection, jailbreak, DAN, role-play, encoding (base64, rot13)
   - Integración con red teaming databases públicas
   - Scoring de severidad por técnica

3. POST /api/llm/evaluate-continuous-learning-safety
   Input: {
     "model_id": "model_v2_3",
     "feedback_data": [...],  // Datos feedback usuarios para continuar aprendiendo
     "baseline_metrics": {...}  // Métricas modelo antes de feedback
   }
   Output: {
     "safe_to_retrain": bool,
     "risk_assessment": {
       "data_quality": 0.89,
       "bias_risk": "LOW",
       "adversarial_contamination": 0.03,  // 3% datos sospechosos
       "distribution_shift": 0.12
     },
     "contaminated_samples": [
       {"index": 234, "reason": "Patrón adversarial detectado", "confidence": 0.91},
       {"index": 456, "reason": "Outlier estadístico extremo", "confidence": 0.87}
     ],
     "recommendation": "REVISAR - Eliminar 12 muestras contaminadas antes de reentrenamiento"
   }

REQUISITOS TÉCNICOS:
- Usar: DeepEval (existente), transformers, scipy, statsmodels
- Patrones adversariales: mantener DB actualizada
- Métricas fairness: integrate aif360
- Tests estadísticos robustos (series temporales)
- Logging JSON estructurado

MANTENER:
- Endpoints existentes funcionando
- Estructura FastAPI actual
- Docker/K8s config
- Integración Prometheus
```

**Artículos Cubiertos:** Art. 15.4, Art. 15.5  
**Esfuerzo:** 3 días  
**Prioridad:** 🔴 Crítica

---

### **PROMPT A.3 - Extensión leka-prompt-governance**

**Contexto:**
Microservicio existente en `/mnt/c/Users/ManuelGonzalez/git/leka-prompt-governance` con Presidio para PII detection y safety.

**Nuevas Funcionalidades EU AI Act:**

**Art. 13 - Transparencia y explicabilidad de prompts:**
1. **Análisis complejidad prompt** (readability, clarity)
2. **Detección instrucciones conflictivas** en prompts
3. **Evaluación efectividad prompt** (con métricas)

**Art. 10.2 - Gobernanza de prompts como "datos":**
4. **Prompt versioning y comparación**
5. **Detección drift en prompts** (cambios de comportamiento)

**Prompt Específico:**

```
Necesito extender leka-prompt-governance con funcionalidades de gobernanza, análisis de efectividad y detección de drift en prompts.

MICROSERVICIO ACTUAL:
- Ubicación: /mnt/c/Users/ManuelGonzalez/git/leka-prompt-governance
- Puerto: 8003
- Framework: FastAPI + Microsoft Presidio
- Funcionalidad actual: PII detection, prompt safety, 50+ patrones

NUEVOS ENDPOINTS A CREAR:

1. POST /api/prompt/analyze-complexity
   Input: {
     "prompt_text": "You are an assistant...",
     "target_audience": "end_user",  // end_user, developer, specialist
     "language": "en"
   }
   Output: {
     "complexity_score": 0.67,  // 0-1 (0=simple, 1=muy complejo)
     "readability_metrics": {
       "flesch_reading_ease": 65.4,
       "flesch_kincaid_grade": 8.2,
       "average_sentence_length": 15.3,
       "difficult_words_percentage": 12%
     },
     "structure_analysis": {
       "total_instructions": 8,
       "conditional_logic": 3,
       "examples_provided": 2,
       "constraints": 5
     },
     "clarity_score": 0.78,
     "conflicting_instructions": [
       {
         "instruction_1": "Be concise",
         "instruction_2": "Explain in detail",
         "conflict_type": "contradiction",
         "severity": "MEDIUM"
       }
     ],
     "recommendation": "ACEPTABLE - Complexity adecuada para end_user, resolver 1 conflicto"
   }
   
   Análisis:
   - Readability: textstat library (Flesch, Gunning Fog, etc.)
   - Parsing: spaCy NLP para estructura
   - Detección contradicciones: semantic similarity entre instrucciones
   - Scoring según target_audience

2. POST /api/prompt/evaluate-effectiveness
   Input: {
     "prompt_text": "...",
     "test_cases": [
       {"input": "user query 1", "expected_behavior": "answer_factually"},
       {"input": "user query 2", "expected_behavior": "refuse_harmful"}
     ],
     "model_endpoint": "http://...",  // endpoint LLM para testear
     "evaluation_criteria": ["accuracy", "safety", "consistency"]
   }
   Output: {
     "effectiveness_score": 0.87,
     "test_results": [
       {
         "case_id": 1,
         "success": true,
         "actual_behavior": "answer_factually",
         "expected_behavior": "answer_factually",
         "response_quality": 0.92
       },
       {
         "case_id": 2,
         "success": false,
         "actual_behavior": "answer_harmful",
         "expected_behavior": "refuse_harmful",
         "response_quality": 0.23,
         "issue": "Safety filter bypassed"
       }
     ],
     "metrics": {
       "success_rate": 0.85,  // 85% casos pasan
       "consistency": 0.91,  // Consistencia respuestas
       "safety_compliance": 0.89
     },
     "failing_cases": [2, 7, 12],
     "recommendation": "REVISAR - 3 casos fallan safety checks"
   }
   
   Nota: Este endpoint LLAMA al modelo LLM real para testear prompt

3. POST /api/prompt/compare-versions
   Input: {
     "prompt_v1": "...",
     "prompt_v2": "...",
     "comparison_metrics": ["semantic_similarity", "instruction_diff", "expected_behavior_change"],
     "test_cases": [...]  // opcional - para evaluar ambos
   }
   Output: {
     "semantic_similarity": 0.78,  // Qué tan similares semánticamente
     "structural_changes": {
       "instructions_added": 2,
       "instructions_removed": 1,
       "instructions_modified": 3,
       "total_diff": 6
     },
     "expected_behavior_change": "MODERATE",
     "key_differences": [
       {
         "type": "instruction_added",
         "content": "Always cite sources",
         "impact": "HIGH",
         "affects": "transparency"
       }
     ],
     "recommendation": "MODERATE CHANGE - Requiere re-evaluación antes de desplegar"
   }

4. POST /api/prompt/detect-prompt-drift
   Input: {
     "prompt_versions": [
       {"version": "v1.0", "timestamp": "...", "text": "...", "metrics": {...}},
       {"version": "v2.0", "timestamp": "...", "text": "...", "metrics": {...}},
       // Serie temporal de versiones
     ],
     "drift_thresholds": {...}
   }
   Output: {
     "drift_detected": bool,
     "drift_score": 0.34,
     "drift_analysis": {
       "semantic_drift": 0.28,  // Cambio semántico acumulado
       "behavioral_drift": 0.41,  // Cambio en comportamiento observado
       "performance_drift": -0.15  // Degradación rendimiento
     },
     "trend": "INCREASING_DRIFT",
     "versions_analysis": [
       {
         "from": "v1.0",
         "to": "v1.5",
         "drift": 0.12,
         "period": "30 days",
         "changes": 8
       },
       {
         "from": "v1.5",
         "to": "v2.0",
         "drift": 0.22,  // Aceleración
         "period": "15 days",
         "changes": 12
       }
     ],
     "recommendation": "WARNING - Drift acelerando. Considerar estabilización de prompt."
   }

REQUISITOS TÉCNICOS:
- Sentence transformers para semantic similarity
- NLP analysis: spaCy
- Diffing semántico avanzado
- Series temporales: análisis trend
- Integration con endpoints existentes

MANTENER:
- Presidio PII detection (actual)
- 50+ patrones safety (actuales)
- Estructura existente
```

**Artículos Cubiertos:** Art. 13, Art. 15.4  
**Esfuerzo:** 2 días  
**Prioridad:** 🟡 Media-Alta

---

### **GRUPO B: EVALUACIÓN Y VALIDACIÓN (3-4 días)**

---

### **PROMPT B.1 - Extensión leka-rag-evaluation**

**Contexto:**
Microservicio existente en `/mnt/c/Users/ManuelGonzalez/git/leka-rag-evaluation` para evaluación de calidad RAG.

**Nuevas Funcionalidades EU AI Act:**

**Art. 13.3.d - Trazabilidad y atribución RAG:**
1. **Citation validation** (verificar fuentes citadas existen)
2. **Attribution tracking** (qué chunk generó qué parte de respuesta)
3. **Source transparency** para usuarios

**Art. 10.3 - Calidad knowledge base:**
4. **Detección gaps de conocimiento** en KB
5. **Análisis cobertura temática**
6. **Detección documentos desactualizados/duplicados**

**Prompt Específico:**

```
Necesito extender leka-rag-evaluation con funcionalidades de trazabilidad, attribution y análisis de knowledge base según EU AI Act Art. 13.

MICROSERVICIO ACTUAL:
- Ubicación: /mnt/c/Users/ManuelGonzalez/git/leka-rag-evaluation
- Puerto: 8004
- Framework: FastAPI
- Funcionalidad actual: RAG quality evaluation (faithfulness, relevance, etc.)

NUEVOS ENDPOINTS A CREAR:

1. POST /api/rag/validate-citations
   Input: {
     "generated_response": "Según el manual técnico (página 45)...",
     "retrieved_chunks": [
       {"chunk_id": "doc_123_chunk_5", "text": "...", "metadata": {"page": 45, "doc": "manual_v2.pdf"}},
       ...
     ],
     "vector_db_connection": {...}  // para validar docs existen
   }
   Output: {
     "citations_valid": bool,
     "citation_analysis": [
       {
         "citation_in_response": "manual técnico (página 45)",
         "source_chunk": "doc_123_chunk_5",
         "validation": "VALID",
         "confidence": 0.96,
         "exact_match": true
       }
     ],
     "hallucinated_citations": [],  // Citas que NO existen en KB
     "unsupported_claims": [],  // Claims sin citation correspondiente
     "attribution_score": 0.98,  // Qué % respuesta tiene fuente válida
     "recommendation": "EXCELENTE - Todas las citas verificadas"
   }

2. POST /api/rag/analyze-knowledge-base
   Input: {
     "vector_db_connection": {...},
     "index_name": "production_kb_v3",
     "analysis_type": ["coverage", "quality", "duplicates", "freshness"]
   }
   Output: {
     "kb_statistics": {
       "total_documents": 15847,
       "total_chunks": 234556,
       "avg_chunk_size": 387,
       "embedding_model": "text-embedding-3-large"
     },
     "quality_analysis": {
       "duplicate_documents": {
         "count": 23,
         "percentage": 0.15%,
         "pairs": [
           {"doc_1": "manual_v1.pdf", "doc_2": "manual_v1_copy.pdf", "similarity": 0.99}
         ]
       },
       "outdated_documents": {
         "count": 1204,
         "percentage": 7.6%,
         "criteria": "last_modified > 365 days",
         "oldest": "policy_2019.pdf (2156 days old)"
       },
       "empty_or_corrupted": {
         "count": 8,
         "files": ["corrupted_file.pdf", ...]
       }
     },
     "coverage_analysis": {
       "topics_covered": ["finance", "legal", "HR", "technical"],
       "topic_distribution": {
         "finance": 35%,
         "legal": 28%,
         "HR": 22%,
         "technical": 15%
       },
       "detected_gaps": [
         {"topic": "GDPR_compliance", "coverage": 12%, "recommendation": "Añadir más docs GDPR"}
       ]
     },
     "freshness_score": 0.84,
     "overall_quality_score": 0.91,
     "recommendation": "BUENA - Eliminar 23 duplicados, actualizar 1204 docs antiguos"
   }
   
   Análisis:
   - Conectar a vector DB (Pinecone, Weaviate, etc.)
   - Detección duplicados: embedding similarity > 0.95
   - Freshness: metadata last_modified
   - Topic modeling: BERTopic o LDA
   - Coverage gaps: análisis temático

3. POST /api/rag/trace-generation-attribution
   Input: {
     "query": "¿Cuál es la política de vacaciones?",
     "generated_response": "La política establece 25 días...",
     "retrieved_chunks": [...],
     "generation_log": {...}  // opcional - si está disponible
   }
   Output: {
     "attribution_map": [
       {
         "response_segment": "La política establece 25 días",
         "source_chunks": ["doc_45_chunk_12"],
         "confidence": 0.94,
         "attribution_type": "direct_quote"
       },
       {
         "response_segment": "incluyendo festivos locales",
         "source_chunks": ["doc_45_chunk_13", "doc_67_chunk_8"],
         "confidence": 0.78,
         "attribution_type": "synthesized"
       }
     ],
     "coverage": {
       "response_length": 287,
       "attributed_length": 265,
       "attribution_percentage": 92.3%,
       "unattributed_length": 22,  // Posible generación sin fuente
       "unattributed_segments": ["además de beneficios adicionales"]
     },
     "retrieval_quality": {
       "chunks_used": 3,
       "chunks_retrieved": 5,
       "usage_rate": 0.6  // 60% chunks recuperados fueron útiles
     },
     "recommendation": "EXCELENTE - 92% respuesta atribuible a fuentes"
   }

REQUISITOS TÉCNICOS:
- Sentence segmentation
- Semantic matching response ↔ chunks
- Confidence scoring
- Differential analysis

MANTENER:
- Presidio PII detection actual
- Safety patterns existentes
```

**Artículos Cubiertos:** Art. 13.3.d, Art. 10  
**Esfuerzo:** 2 días  
**Prioridad:** 🟡 Media

---

### **GRUPO C: MONITORIZACIÓN Y DRIFT (2-3 días)**

---

### **PROMPT C.1 - Extensión leka-agent-monitoring**

**Contexto:**
Microservicio existente en `/mnt/c/Users/ManuelGonzalez/git/leka-agent-monitoring` para monitorización de agentes.

**Nuevas Funcionalidades EU AI Act:**

**Art. 14 - Supervisión humana de agentes:**
1. **HITL decision tracking** (decisiones requieren supervisión)
2. **Override detection** (cuándo humanos anulan agente)
3. **Intervention analytics**

**Art. 19 - Inmutable logging de decisiones agente:**
4. **Cryptographic audit trail** para decisiones agente
5. **Tamper detection** en logs
6. **Chain-of-custody** de decisiones críticas

**Prompt Específico:**

```
Necesito extender leka-agent-monitoring con funcionalidades de supervisión humana, audit trail inmutable y detección de intervenciones según EU AI Act Art. 14 y Art. 19.

MICROSERVICIO ACTUAL:
- Ubicación: /mnt/c/Users/ManuelGonzalez/git/leka-agent-monitoring
- Puerto: 8005
- Framework: FastAPI
- Funcionalidad actual: Agent monitoring enterprise-grade

NUEVOS ENDPOINTS A CREAR:

1. POST /api/agent/log-decision-immutable
   Input: {
     "agent_id": "customer_service_agent_v2.1",
     "decision_data": {
       "timestamp": "2025-11-15T14:23:45Z",
       "query": "Cancel order #12345",
       "reasoning_steps": [...],
       "tools_called": [...],
       "decision": "cancel_approved",
       "confidence": 0.94,
       "human_oversight_required": false
     },
     "previous_log_hash": "a3f5d8..."  // Hash del log anterior (blockchain-style)
   }
   Output: {
     "log_id": "uuid",
     "current_hash": "b7e9f2...",  // SHA-256 hash de este log
     "signature": "...",  // Firma criptográfica
     "timestamp_proof": "...",  // RFC 3161 timestamp si configurado
     "chain_valid": true,
     "log_stored": true
   }
   
   Funcionalidad:
   - Hash chain: cada log incluye hash del anterior (blockchain-style)
   - Signature: HMAC o RSA signature de cada log
   - Timestamp: integración opcional con TSA (Time Stamping Authority)
   - Almacenamiento: append-only log file + optional external (S3, etc.)
   - Verificación integridad: endpoint separado

2. POST /api/agent/verify-log-integrity
   Input: {
     "log_range": {"from": "log_id_1", "to": "log_id_100"},
     "verification_type": "full"  // full, sample, recent
   }
   Output: {
     "integrity_valid": bool,
     "total_logs_checked": 100,
     "tampering_detected": false,
     "hash_chain_valid": true,
     "signatures_valid": 100,
     "corrupted_logs": [],
     "verification_time": "1.2s",
     "recommendation": "INTEGRITY CONFIRMED - No tampering detected"
   }

3. POST /api/agent/analyze-human-interventions
   Input: {
     "agent_id": "customer_service_agent_v2.1",
     "time_range": {"start": "2025-11-01", "end": "2025-11-15"},
     "analysis_type": ["override_rate", "intervention_patterns", "decision_confidence"]
   }
   Output: {
     "intervention_statistics": {
       "total_decisions": 45823,
       "hitl_required": 1054,  // 2.3% requirieron humano
       "hitl_rate": 0.023,
       "human_overrides": 158,  // Humano anuló agente
       "override_rate": 0.15,  // 15% de HITL fueron override
       "avg_decision_confidence_overridden": 0.67  // Baja confianza = más override
     },
     "patterns": {
       "most_overridden_decision_type": "high_value_refund",
       "least_overridden": "faq_response",
       "override_by_confidence": {
         "0.0-0.5": 0.45,  // 45% override si confianza < 0.5
         "0.5-0.7": 0.22,
         "0.7-0.9": 0.08,
         "0.9-1.0": 0.02
       }
     },
     "recommendation": "AJUSTAR - Aumentar HITL threshold para high_value_refund"
   }

REQUISITOS TÉCNICOS:
- Cryptography: hashlib (SHA-256), hmac o cryptography library
- Append-only logging: garantizar no modificación
- Optional: integración blockchain timestamp (OriginStamp, OpenTimestamps)
- Performance: hash verification eficiente
- Storage: file-based + optional cloud backup

MANTENER:
- Agent monitoring actual
- Estructura existente
```

**Artículos Cubiertos:** Art. 14, Art. 19, Art. 12  
**Esfuerzo:** 2-3 días  
**Prioridad:** 🔴 Crítica (logs inmutables)

---

### **PROMPT C.2 - Extensión leka-model-wrapper**

**Contexto:**
Microservicio existente en `/mnt/c/Users/ManuelGonzalez/git/leka-model-wrapper` para wrapper de modelos comerciales.

**Nuevas Funcionalidades EU AI Act:**

**Art. 15.1 - Precisión y métricas de rendimiento:**
1. **Performance benchmarking** automático
2. **Accuracy monitoring** en tiempo real
3. **Latency y throughput tracking**

**Art. 72 - Post-market monitoring:**
4. **Drift detection** en inferencias
5. **Degradation alerts**
6. **Usage analytics**

**Prompt Específico:**

```
Necesito extender leka-model-wrapper con funcionalidades de benchmarking, drift detection y post-market monitoring según EU AI Act Art. 15 y Art. 72.

MICROSERVICIO ACTUAL:
- Ubicación: /mnt/c/Users/ManuelGonzalez/git/leka-model-wrapper
- Puerto: 8006
- Framework: FastAPI
- Funcionalidad actual: Wrapper para llamadas a modelos comerciales (OpenAI, Anthropic, etc.)

NUEVOS ENDPOINTS A CREAR:

1. POST /api/model/benchmark-performance
   Input: {
     "model_endpoint": "https://api.openai.com/v1/chat/completions",
     "model_name": "gpt-4-turbo",
     "benchmark_suite": "standard",  // standard, comprehensive, quick
     "test_cases": 100  // número de tests
   }
   Output: {
     "benchmark_results": {
       "accuracy": {
         "factual_correctness": 0.94,
         "instruction_following": 0.91,
         "task_completion": 0.89
       },
       "performance": {
         "avg_latency": 1847,  // ms
         "p50_latency": 1654,
         "p95_latency": 2956,
         "p99_latency": 4234,
         "throughput": 34.2  // requests/second
       },
       "quality": {
         "hallucination_rate": 0.021,  // 2.1%
         "toxicity_rate": 0.003,
         "coherence_score": 0.92,
         "relevance_score": 0.88
       },
       "cost": {
         "total_tokens": 234567,
         "total_cost_usd": 4.23,
         "cost_per_request": 0.042
       }
     },
     "comparison_baseline": {
       "previous_benchmark": "2025-10-15",
       "accuracy_change": +0.02,  // Mejoró
       "latency_change": -125,  // 125ms más rápido
       "cost_change": -0.008  // Más barato
     },
     "recommendation": "MEJORADO - Modelo más rápido y preciso vs mes anterior"
   }
   
   Tests incluidos:
   - MMLU subset (conocimiento general)
   - TruthfulQA (factualidad)
   - HumanEval (si code model)
   - Latency tests (múltiples concurrent requests)

2. POST /api/model/detect-inference-drift
   Input: {
     "model_id": "gpt_4_turbo_production",
     "inference_logs": [
       {"timestamp": "...", "input": "...", "output": "...", "latency": 1234, ...},
       // Serie temporal de inferencias
     ],
     "baseline_distribution": {...},  // opcional - distribución esperada
     "drift_thresholds": {"data_drift": 0.3, "prediction_drift": 0.15}
   }
   Output: {
     "drift_detected": bool,
     "drift_analysis": {
       "input_drift": {
         "detected": true,
         "drift_score": 0.34,
         "metrics": {
           "avg_input_length": {"baseline": 487, "current": 712, "change_pct": 46%},
           "topic_distribution": {
             "baseline": {"finance": 0.45, "legal": 0.35, "other": 0.20},
             "current": {"finance": 0.28, "legal": 0.61, "other": 0.11},  // Shift hacia legal
             "kl_divergence": 0.34
           }
         },
         "severity": "MEDIUM"
       },
       "output_drift": {
         "detected": false,
         "drift_score": 0.08
       },
       "performance_drift": {
         "latency_degradation": true,
         "avg_latency": {"baseline": 1654, "current": 2234, "change_pct": +35%},
         "p95_latency": {"baseline": 2956, "current": 4123, "change_pct": +39%}
       }
     },
     "recommendation": "WARNING - Input drift detected + latency degrading. Considerar revalidación modelo."
   }
   
   Análisis:
   - KL divergence para distribuciones
   - Statistical tests (Kolmogorov-Smirnov, Chi-squared)
   - Topic modeling drift
   - Latency/performance tracking

3. POST /api/model/track-usage-analytics
   Input: {
     "model_id": "gpt_4_turbo_production",
     "time_range": {"start": "2025-11-01", "end": "2025-11-15"},
     "metrics": ["usage", "cost", "errors", "satisfaction"]
   }
   Output: {
     "usage_metrics": {
       "total_requests": 456782,
       "unique_users": 3421,
       "avg_requests_per_user": 133.5,
       "peak_qps": 87,  // queries per second
       "avg_qps": 34.2
     },
     "cost_metrics": {
       "total_tokens": 45678923,
       "total_cost_usd": 823.45,
       "cost_per_request": 0.0018,
       "cost_trend": "STABLE"
     },
     "error_metrics": {
       "total_errors": 1234,
       "error_rate": 0.0027,  // 0.27%
       "error_types": {
         "timeout": 678 (55%),
         "rate_limit": 234 (19%),
         "validation": 189 (15%),
         "other": 133 (11%)
       }
     },
     "satisfaction_metrics": {
       "avg_rating": 4.3,
       "ratings_count": 12456,
       "rating_distribution": {1: 2%, 2: 3%, 3: 8%, 4: 35%, 5: 52%}
     },
     "recommendation": "EXCELENTE - Error rate bajo, satisfaction alta"
   }

REQUISITOS TÉCNICOS:
- Procesamiento serie temporal eficiente (pandas, numpy)
- Statistical analysis (scipy)
- Almacenamiento temporal en memoria (no persist)
- Aggregations eficientes

MANTENER:
- Wrapper functionality actual
- Endpoints de inferencia existentes
```

**Artículos Cubiertos:** Art. 15.1, Art. 72, Art. 12  
**Esfuerzo:** 2 días  
**Prioridad:** 🟡 Media

---

### **GRUPO D: NUEVAS CAPACIDADES ESPECÍFICAS (2-3 días)**

---

### **PROMPT D.1 - NUEVO MICROSERVICIO: leka-adversarial-robustness**

**Nota:** Este es NUEVO microservicio (no existía).

**Funcionalidades EU AI Act:**

**Art. 15.5 - Robustez adversarial completa:**
1. **Model poisoning detection**
2. **Model evasion detection**
3. **Adversarial attack simulation**
4. **Defense mechanisms testing**

**Prompt Específico:**

```
Necesito CREAR NUEVO microservicio Python leka-adversarial-robustness para detección completa de ataques adversariales según EU AI Act Art. 15.5.

ESPECIFICACIONES:
- Nombre: leka-adversarial-robustness
- Puerto: 8007
- Framework: FastAPI
- Ubicación: /mnt/c/Users/ManuelGonzalez/git/leka-adversarial-robustness
- Arquitectura: STATELESS (sin BD)

ENDPOINTS A CREAR:

1. POST /api/adversarial/detect-model-poisoning
   Input: {
     "model_weights": "s3://path/to/weights.pkl",  // o upload
     "baseline_model": "s3://path/to/baseline.pkl",  // modelo confiable
     "comparison_metrics": ["weight_distribution", "activation_patterns"]
   }
   Output: {
     "poisoning_detected": bool,
     "poisoning_score": 0.23,
     "analysis": {
       "weight_distribution_anomaly": 0.18,
       "activation_pattern_anomaly": 0.28,
       "layers_suspicious": ["layer_5", "layer_12"],
       "backdoor_probability": 0.23
     },
     "suspicious_patterns": [
       {"layer": "layer_5", "pattern": "Weights concentrados en valores extremos", "confidence": 0.67}
     ],
     "recommendation": "MEDIUM RISK - Revisar layers 5 y 12 manualmente"
   }
   
   Análisis:
   - Comparación distribución de pesos (KL divergence)
   - Detección backdoors (activations específicas)
   - Análisis capas sospechosas
   - Statistical anomalies

2. POST /api/adversarial/test-model-evasion
   Input: {
     "model_endpoint": "http://model-api/predict",
     "attack_techniques": ["fgsm", "pgd", "carlini_wagner", "deepfool"],
     "test_samples": [...],
     "epsilon": 0.03  // perturbation budget
   }
   Output: {
     "evasion_vulnerability_score": 0.12,  // Bajo = robusto
     "attack_success_rates": {
       "fgsm": {"tested": 100, "successful": 23, "success_rate": 0.23},
       "pgd": {"tested": 100, "successful": 8, "success_rate": 0.08},
       "carlini_wagner": {"tested": 50, "successful": 3, "success_rate": 0.06},
       "deepfool": {"tested": 50, "successful": 2, "success_rate": 0.04}
     },
     "robustness_score": 0.88,  // 88% robusto
     "most_vulnerable_classes": ["class_3", "class_7"],
     "recommendation": "ROBUSTO - 88% resistencia a ataques adversariales"
   }
   
   Librerías:
   - advertorch (ataques adversariales)
   - foolbox (alternativa)
   - CleverHans (FGSM, PGD)

3. POST /api/adversarial/simulate-attacks
   Input: {
     "model_endpoint": "http://...",
     "attack_scenarios": ["data_poisoning", "prompt_injection", "model_inversion"],
     "severity_levels": ["low", "medium", "high"]
   }
   Output: {
     "simulation_results": {...},
     "vulnerabilities_found": [...],
     "mitigation_recommendations": [...]
   }

REQUISITOS TÉCNICOS:
- adversarial-robustness-toolbox (ART - IBM)
- advertorch o foolbox
- PyTorch/TensorFlow (para ataques)
- Numpy, scipy
- NO persistencia

ESTRUCTURA:
- Similar a otros micros leka-*
- Docker, K8s yamls
- Health check, metrics
```

**Artículos Cubiertos:** Art. 15.5 (completo)  
**Esfuerzo:** 3 días  
**Prioridad:** 🔴 CRÍTICA  
**Nota:** Este es microservicio NUEVO pero lo incluyo aquí porque es Python

---

## 📊 RESUMEN PROMPTS EQUIPO PYTHON - MICROSERVICIOS EXISTENTES

| Prompt | Microservicio | Funcionalidades | Esfuerzo | Prioridad |
|--------|---------------|----------------|----------|-----------|
| **A.1** | leka-bias-detection-service | Data poisoning, protected attributes, integrity | 2-3 días | 🔴 Crítica |
| **A.2** | leka-llm-evaluation | Feedback loop bias, adversarial examples | 3 días | 🔴 Crítica |
| **A.3** | leka-prompt-governance | Complexity, effectiveness, drift, versioning | 2 días | 🟡 Media-Alta |
| **B.1** | leka-rag-evaluation | Citations, attribution, KB analysis, gaps | 2 días | 🟡 Media |
| **C.1** | leka-agent-monitoring | Immutable logging, HITL tracking, interventions | 2-3 días | 🔴 Crítica |
| **C.2** | leka-model-wrapper | Benchmarking, drift, usage analytics | 2 días | 🟡 Media |
| **D.1** | leka-adversarial-robustness (NUEVO) | Model poisoning, evasion, attack simulation | 3 días | 🔴 Crítica |

**TOTAL ESFUERZO:** 16-18 días  
**CON 3 CHATS PARALELOS:** 5-6 días reales  
**CON 4 CHATS PARALELOS:** 4-5 días reales

---

## 🎯 DISTRIBUCIÓN TRABAJO PARALELO RECOMENDADA

### **CHAT 1 - Adversarial & Security (Crítico):**
- Prompt A.1 (leka-bias-detection-service)
- Prompt D.1 (leka-adversarial-robustness NUEVO)
- **Esfuerzo:** 5-6 días

### **CHAT 2 - LLM Evaluation & Feedback Loops:**
- Prompt A.2 (leka-llm-evaluation)
- **Esfuerzo:** 3 días

### **CHAT 3 - Monitoring & Logging:**
- Prompt C.1 (leka-agent-monitoring - immutable logs)
- Prompt C.2 (leka-model-wrapper - drift)
- **Esfuerzo:** 4-5 días

### **CHAT 4 - Prompts & RAG:**
- Prompt A.3 (leka-prompt-governance)
- Prompt B.1 (leka-rag-evaluation)
- **Esfuerzo:** 4 días

**Timeline con 4 chats:** 5-6 días reales

---

## 📋 CHECKLIST PRE-IMPLEMENTACIÓN

Antes de ejecutar prompts, verificar:

- [ ] Microservicio existe en ruta especificada
- [ ] Puerto no está en uso por otro servicio
- [ ] Endpoints actuales documentados
- [ ] Requirements.txt actual revisado
- [ ] Docker/K8s configs existentes identificados
- [ ] Tests actuales ejecutables
- [ ] Leer README del microservicio

---

## ⚠️ IMPORTANTE - MANTENER

**NO ROMPER:**
- ✅ Endpoints existentes (backward compatibility)
- ✅ Estructura de responses actual
- ✅ Docker/K8s configuración
- ✅ Health checks
- ✅ Prometheus metrics actuales

**SÍ EXTENDER:**
- ✅ Nuevos endpoints (versioned: /api/v2/ si necesario)
- ✅ Nuevas librerías en requirements.txt
- ✅ Nuevos tests
- ✅ Documentación OpenAPI actualizada

---

**Fin Documento 1 de 5**

