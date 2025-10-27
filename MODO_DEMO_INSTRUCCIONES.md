# Modo DEMO - Instrucciones de Acceso

## Acceso a la Bandeja de Tareas en Modo MOCK

### URL Principal:

```
/workflow/task-inbox.zul?mock=true
```

### ¿Qué Verás?

Al acceder con `mock=true`, la bandeja mostrará **24 tareas simuladas**, una para cada pantalla de workflow:

| # | Tarea | Pantalla | Prioridad |
|---|-------|----------|-----------|
| 1 | Aprobar Agente IA - Cliente Banco Nacional | AgentApprovalHumanOverride | HIGH |
| 2 | Responder Alerta Crítica - Sistema Producción | AlertResponse | CRITICAL |
| 3 | Plan Mitigación Sesgo - Modelo HR | BiasMitigationPlan | HIGH |
| 4 | Revisar Sesgo Detectado - Modelo Scoring Crédito | BiasReview | CRITICAL |
| 5 | Decisión Urgente Sesgo - Sistema Activo | BiasUrgentDecision | CRITICAL |
| 6 | Decisión Revisión Compliance - EU AI Act | ComplianceReviewDecision | MEDIUM |
| 7 | Compliance Review - Sistema RAG Legal | ComplianceReview | HIGH |
| 8 | Recordatorio Dataset - Training Sentiment | DatasetReviewReminder | MEDIUM |
| 9 | Analizar Drift - Modelo Recomendaciones | DriftAnalysis | CRITICAL |
| 10 | Decisión Drift - Modelo Predicción Ventas | DriftReviewDecision | HIGH |
| 11 | Ethics Committee - Chatbot Médica | EthicsCommitteeReview | CRITICAL |
| 12 | Plan Mitigación Ética - Sistema Automático | EthicsMitigationPlan | HIGH |
| 13 | Recordatorio Ethics - Revisión Pendiente | EthicsReviewReminder | MEDIUM |
| 14 | Solicitud Ethics Review - IA RRHH | EthicsReviewRequest | HIGH |
| 15 | Recordatorio SLA HITL - Aprobación Pendiente | HitlSlaReminder | HIGH |
| 16 | Evaluar LLM - GPT-4o Soporte | LlmEvaluationReview | MEDIUM |
| 17 | Aprobar Modelo - Predicción Fraude v4.2 | ModelApprovalHumanOverride | HIGH |
| 18 | Recordatorio Aprobación Modelo - Churn | ModelApprovalReminder | MEDIUM |
| 19 | Revisar Evaluación Modelo - Sentimientos | ModelEvaluationReview | MEDIUM |
| 20 | Intervención Performance - API Inference | PerformanceIntervention | CRITICAL |
| 21 | Decisión Performance - Endpoint Batch | PerformanceReviewDecision | HIGH |
| 22 | Revisión Humana Prompt - Marketing | PromptHumanReview | MEDIUM |
| 23 | Evaluar RAG - Sistema Documentación | RagEvaluationReview | MEDIUM |

## Cómo Usar

### 1. Acceder a la Bandeja MOCK

```
http://localhost:8080/workflow/task-inbox.zul?mock=true
```

### 2. Hacer Click en Cualquier Tarea

Cada tarea tiene su URL ya configurada con `mock=true`, por ejemplo:

```
/workflow/agent-approval-override.zul?taskId=mock-1&mock=true
/workflow/bias-review.zul?taskId=mock-4&mock=true
/workflow/prompt-human-review.zul?taskId=mock-23&mock=true
```

### 3. Navegar entre Pantallas

Todas las pantallas en modo MOCK:
- Mostrarán datos simulados realistas
- No guardarán cambios en la BD
- Permitirán simular todas las acciones
- Tendrán un indicador visual "MODO DEMO"

### 4. Volver a la Bandeja

Desde cualquier pantalla, cancelar o completar te devuelve a:
```
/workflow/task-inbox.zul?mock=true
```

## Datos Simulados en Cada Pantalla

### AgentApprovalHumanOverride (mock-1)
- **Agente:** Cliente Banco Nacional v2.3
- **Risk Level:** HIGH
- **Compliance Score:** 85%
- **Bias Score:** 92%
- **Decision:** Pendiente de aprobación

### BiasReview (mock-4)
- **Modelo:** Scoring Crédito v3.2
- **Tipo Sesgo:** DEMOGRAPHIC
- **Grupos Afectados:** Género femenino (-5%), Edad 18-25 (-8%)
- **Severidad:** HIGH
- **Confidence:** 87.5%

### PromptHumanReview (mock-23)
- **Prompt:** Marketing Black Friday 2025
- **Safety Score:** 95/100
- **Compliance Score:** 88/100
- **Jailbreak:** No detectado
- **Injection:** No detectado

### EthicsCommitteeReview (mock-11)
- **Sistema:** Chatbot Atención Médica
- **Tipo:** MEDICAL_AI
- **Impacto:** 10,000 pacientes/mes
- **Riesgos:** Privacidad datos médicos, sesgo diagnóstico
- **Recomendaciones:** Auditoría médica externa

### PerformanceIntervention (mock-20)
- **Endpoint:** /api/v1/inference
- **Baseline:** 300ms
- **Current:** 1200ms
- **Degradación:** +300%
- **Acción:** INMEDIATA

## Características del Modo MOCK

### Ventajas:
- ✅ **Sin dependencias:** No requiere Flowable, BD, servicios externos
- ✅ **Datos consistentes:** Siempre los mismos datos para demos
- ✅ **Sin side effects:** No modifica datos reales
- ✅ **Rápido:** Sin latencia de servicios
- ✅ **Reproducible:** Ideal para grabar videos
- ✅ **Completo:** Todas las 24 pantallas accesibles

### Indicadores Visuales:
- 🎭 Banner "MODO DEMO" en cada pantalla
- 🎭 Botones con etiqueta "(DEMO)"
- 🎭 Color diferenciado (warning/amarillo)
- 🎭 Logs con prefijo [MOCK]

## Ejemplo de Uso para Video/Demo

### Escenario 1: Aprobación de Agente con Sesgo

1. Acceder a bandeja: `/workflow/task-inbox.zul?mock=true`
2. Click en tarea #1: "Aprobar Agente IA"
3. Revisar datos simulados
4. Ver que tiene sesgo detectado
5. Click en tarea #3: "Plan Mitigación Sesgo"
6. Crear plan de mitigación
7. Volver y aprobar agente

### Escenario 2: Workflow Completo de Ethics

1. Acceder a bandeja MOCK
2. Tarea #14: "Solicitud Ethics Review"
3. Tarea #11: "Ethics Committee Review"
4. Tarea #12: "Plan Mitigación Ética"
5. Tarea #13: "Recordatorio" (si aplica)

### Escenario 3: Performance Degradation

1. Tarea #20: "Intervención Performance" (CRITICAL)
2. Ver métricas degradadas
3. Tarea #21: "Decisión Performance"
4. Tomar acción correctiva

## Activar/Desactivar Modo MOCK

### Activar:
```
?mock=true
```

### Desactivar (modo normal):
```
Sin parámetro o ?mock=false
```

## URLs Directas a Pantallas (con MOCK)

```
# Bandeja principal
/workflow/task-inbox.zul?mock=true

# Agentes
/workflow/agent-approval-override.zul?taskId=mock-1&mock=true

# Sesgo
/workflow/bias-review.zul?taskId=mock-4&mock=true
/workflow/bias-mitigation-plan.zul?taskId=mock-3&mock=true
/workflow/bias-urgent-decision.zul?taskId=mock-5&mock=true

# Compliance
/workflow/compliance-review.zul?taskId=mock-7&mock=true
/workflow/compliance-review-decision.zul?taskId=mock-6&mock=true

# Drift
/workflow/drift-analysis.zul?taskId=mock-9&mock=true
/workflow/drift-review-decision.zul?taskId=mock-10&mock=true

# Ethics
/workflow/ethics-committee-review.zul?taskId=mock-11&mock=true
/workflow/ethics-mitigation-plan.zul?taskId=mock-12&mock=true
/workflow/ethics-review-reminder.zul?taskId=mock-13&mock=true
/workflow/ethics-review-request.zul?taskId=mock-14&mock=true

# Models
/workflow/model-approval-override.zul?taskId=mock-17&mock=true
/workflow/model-approval-reminder.zul?taskId=mock-18&mock=true
/workflow/model-evaluation-review.zul?taskId=mock-19&mock=true

# Prompts
/workflow/prompt-human-review.zul?taskId=mock-23&mock=true

# Performance
/workflow/performance-intervention.zul?taskId=mock-20&mock=true
/workflow/performance-review-decision.zul?taskId=mock-21&mock=true

# Otros
/workflow/dataset-review-reminder.zul?taskId=mock-8&mock=true
/workflow/llm-evaluation-review.zul?taskId=mock-16&mock=true
/workflow/rag-evaluation-review.zul?taskId=mock-24&mock=true
/workflow/alert-response.zul?taskId=mock-2&mock=true
/workflow/hitl-sla-reminder.zul?taskId=mock-15&mock=true
```

---

## 🎬 Listo para Grabar Videos y Hacer Demos

Con este setup puedes:
1. Mostrar todas las pantallas del workflow
2. Simular flujos completos end-to-end
3. Grabar videos sin dependencias técnicas
4. Hacer presentaciones a clientes
5. Verificar diseño y UX de todas las pantallas

**Acceso rápido:** `/workflow/task-inbox.zul?mock=true`

