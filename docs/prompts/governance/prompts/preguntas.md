# Preguntas sobre Evaluación y Gobierno de Prompts

## 1. ¿Cómo evaluamos un prompt?

El sistema implementa **dos tipos de evaluación** de prompts:

### A. Análisis de Cumplimiento (Compliance Analysis)
**Endpoint:** `POST /api/v1/prompts/{promptId}/analyze`

**Proceso:**
1. **Análisis de Seguridad** (`PromptGovernanceClient.evaluateSafety`):
   - Detecta vulnerabilidades de prompt injection
   - Detecta intentos de jailbreak
   - Evalúa el nivel de seguridad del prompt
   - Retorna: `isSafe`, `safetyScore`, `severity`, `risksDetected`, `recommendations`

2. **Análisis de Efectividad** (`PromptGovernanceClient.evaluateEffectiveness`):
   - Evalúa claridad del prompt
   - Evalúa especificidad del prompt
   - Calcula score de efectividad
   - Retorna: `effectivenessScore`, `clarity`, `specificity`, `grade`, `suggestions`

3. **Creación de Validación:**
   - Tipo: `COMPLIANCE`
   - Score calculado combinando seguridad y efectividad
   - Issues encontrados contados automáticamente
   - Detalles almacenados en `prmvalidationdetails` (JSON estructurado)

**Resultado:** Se crea un registro `PromptValidation` con tipo `COMPLIANCE` que contiene:
- `prmvalidationscore`: Score numérico (0-100)
- `prmvalidationdetails`: Detalles del análisis en formato texto estructurado
- `prmissuesfound`: Número de problemas detectados
- `prmvalidationresult`: Estado del análisis (PENDING, PASSED, FAILED)

### B. Evaluación de Rendimiento (Performance Evaluation)
**Endpoint:** `POST /api/v1/prompts/{promptId}/evaluate`

**Proceso:**
1. **Evaluación de Calidad** (`LLMEvaluationClient.evaluateQuality`):
   - Detecta alucinaciones en respuestas
   - Detecta toxicidad
   - Detecta sesgos (bias)
   - Retorna métricas de calidad

2. **Evaluación de Eficiencia de Costos** (`LLMEvaluationClient.evaluateCostEfficiency`):
   - Calcula tokens consumidos (input/output)
   - Estima latencia de respuesta
   - Calcula costos operativos
   - Retorna: `tokensUsed`, `latency`, `estimatedCost`

3. **Creación de Validación:**
   - Tipo: `PERFORMANCE`
   - Score calculado combinando calidad y eficiencia
   - Issues encontrados contados automáticamente
   - Detalles almacenados en `prmvalidationdetails`

**Resultado:** Se crea un registro `PromptValidation` con tipo `PERFORMANCE` que contiene métricas de rendimiento.

**Nota:** Ambos procesos utilizan **Resilience4j** con:
- Circuit Breaker para tolerancia a fallos
- Retry automático en caso de errores transitorios
- Time Limiter para evitar timeouts prolongados
- Fallback methods si el servicio de Python no está disponible

**Capacidades Adicionales Disponibles (no implementadas en el módulo actual):**

El `PromptGovernanceClient` y `LLMEvaluationClient` ofrecen funcionalidades adicionales que pueden integrarse:

- **Comparación de Versiones:** `PromptGovernanceClient.compareVersions()` - Compara dos versiones de un prompt
- **A/B Testing:** `LLMEvaluationClient.abTestPrompts()` - Prueba dos prompts diferentes
- **Benchmarking:** `LLMEvaluationClient.benchmarkEvaluations()` - Compara rendimiento entre múltiples modelos
- **Análisis de Costos:** `PromptGovernanceClient.analyzeCost()` - Analiza costos detallados
- **Validación de Templates:** `PromptGovernanceClient.validateTemplate()` - Valida templates con variables
- **Optimización de Context Window:** `PromptGovernanceClient.optimizeContextWindow()` - Optimiza el uso del contexto

---

## 2. ¿Contra qué modelo podemos probarlo?

### Modelos Asociados al Prompt

Los prompts pueden estar **asociados a múltiples modelos** mediante la relación `PromptModel`:

**Endpoints disponibles:**
- `POST /api/v1/prompts/{promptId}/models/{modelId}` - Asociar modelo
- `DELETE /api/v1/prompts/{promptId}/models/{modelId}` - Desasociar modelo
- `GET /api/v1/prompts/{promptId}/models` - Listar modelos asociados

**Extracción de Modelo desde Parámetros:**

El sistema extrae el modelo objetivo desde el campo `prmparameters` del prompt:
- Formato esperado: `"model=gpt-4, temperature=0.7, max_tokens=1000"`
- Si no se especifica, usa `"gpt-4"` como default
- El modelo extraído se usa en:
  - Análisis de seguridad (`modelTarget`)
  - Evaluación de eficiencia de costos (`modelId`)

**Limitación Actual:**

⚠️ **La evaluación de rendimiento actualmente usa el prompt como respuesta** (`response = prompt.getPrmcontent()`), lo que es una limitación. Para una evaluación real, se necesitaría:
1. Ejecutar el prompt contra el modelo asociado
2. Obtener la respuesta real del modelo
3. Evaluar esa respuesta (no el prompt)

**Recomendación:** Implementar un endpoint de prueba que:
- Ejecute el prompt contra un modelo específico
- Capture la respuesta real
- Evalúe esa respuesta con `LLMEvaluationClient.evaluateQuality`

---

## 3. ¿Cómo validamos las respuestas esperadas?

### Estado Actual

**❌ No está implementado actualmente** un mecanismo para validar respuestas esperadas contra respuestas reales.

### Lo que existe:

1. **Validaciones Manuales:**
   - El usuario puede crear validaciones manuales mediante `POST /api/v1/prompts/{promptId}/validations`
   - Tipo: `CUSTOM` o `SYSTEM`
   - El usuario debe proporcionar manualmente:
     - `prmvalidationresult`: Resultado (PASSED, FAILED, PENDING)
     - `prmvalidationscore`: Score manual
     - `prmvalidationdetails`: Detalles de la validación

2. **Validaciones Automáticas (Análisis y Evaluación):**
   - `analyzePrompt`: Crea validación tipo `COMPLIANCE`
   - `evaluatePrompt`: Crea validación tipo `PERFORMANCE`
   - Pero **no comparan respuestas esperadas vs reales**

### Lo que falta implementar:

**Sistema de Validación de Respuestas Esperadas:**

1. **Estructura de Datos:**
   - Agregar campo `prmexpectedresponse` a la entidad `Prompt` (respuesta esperada)
   - O crear tabla `PromptTestCases` con:
     - `testCaseId`
     - `promptId`
     - `input`: Input de prueba
     - `expectedResponse`: Respuesta esperada
     - `actualResponse`: Respuesta real (después de ejecución)
     - `validationStatus`: PASSED, FAILED, PENDING

2. **Endpoint de Prueba:**
   ```
   POST /api/v1/prompts/{promptId}/test
   Body: {
     "modelId": "gpt-4",
     "input": "test input",
     "expectedResponse": "expected output"
   }
   ```

3. **Proceso de Validación:**
   - Ejecutar prompt contra modelo
   - Obtener respuesta real
   - Comparar respuesta real vs esperada (usando métricas de similitud semántica)
   - Crear validación tipo `TEST` con resultado

4. **Métricas de Comparación:**
   - Similitud semántica (cosine similarity de embeddings)
   - Exactitud (exact match)
   - BLEU score
   - ROUGE score
   - Evaluación con LLM (usar `LLMEvaluationClient.evaluateQuality`)

---

## 4. ¿Es factible que hagamos pruebas en gobierno?

### ✅ Sí, es factible y recomendable

**Ventajas del módulo de gobierno para pruebas:**

1. **Infraestructura Existente:**
   - ✅ Sistema de validaciones ya implementado
   - ✅ Integración con `AIGovernanceClient` (Python)
   - ✅ Circuit Breaker y Retry para resiliencia
   - ✅ Trazabilidad completa (auditoría)

2. **Cumplimiento Normativo:**
   - ✅ Registro de todas las pruebas (EU AI Act Art. 19, 27)
   - ✅ Versionado de prompts (ISO 42001 8.2)
   - ✅ Historial de validaciones
   - ✅ Asociación con proyectos (contexto normativo)

3. **Funcionalidades que se pueden aprovechar:**
   - ✅ Análisis de cumplimiento antes de pruebas
   - ✅ Evaluación de rendimiento después de pruebas
   - ✅ Gestión de relaciones (modelos, agents, RAG)
   - ✅ Workflow de aprobación (integración BPMN)

### Lo que se necesita agregar:

1. **Endpoint de Ejecución de Pruebas:**
   ```
   POST /api/v1/prompts/{promptId}/test
   ```
   - Ejecutar prompt contra modelo
   - Capturar respuesta real
   - Comparar con respuesta esperada
   - Crear validación tipo `TEST`

2. **Gestión de Test Cases:**
   - CRUD de test cases por prompt
   - Ejecución en batch
   - Reportes de resultados

3. **Integración con Modelos:**
   - Usar modelos asociados al prompt
   - O permitir seleccionar modelo para prueba
   - Ejecutar contra múltiples modelos (A/B testing)

4. **Métricas de Pruebas:**
   - Tasa de éxito (pass rate)
   - Score promedio
   - Tendencias temporales
   - Comparación entre versiones

**Recomendación:** Implementar el sistema de pruebas como una extensión del módulo de gobierno, aprovechando la infraestructura existente.

---

## 5. ¿Está completo para gobierno de IA el módulo de prompt?

### Estado Actual: ⚠️ **90% Completo**

### ✅ Lo que está implementado:

1. **CRUD Completo:**
   - ✅ Registro de prompts
   - ✅ Edición de prompts
   - ✅ Listado y búsqueda
   - ✅ Eliminación (soft delete)

2. **Versionado:**
   - ✅ Versionado semántico automático (MAJOR.MINOR.PATCH)
   - ✅ Historial de versiones
   - ✅ Comparación de versiones

3. **Análisis de Cumplimiento:**
   - ✅ Análisis de seguridad (prompt injection, jailbreak)
   - ✅ Análisis de efectividad (claridad, especificidad)
   - ✅ Detección de sesgos
   - ✅ Integración con `PromptGovernanceClient`

4. **Evaluación de Rendimiento:**
   - ✅ Evaluación de calidad (hallucinaciones, toxicidad, bias)
   - ✅ Evaluación de eficiencia (tokens, latencia, costos)
   - ✅ Integración con `LLMEvaluationClient`

5. **Gestión de Relaciones:**
   - ✅ Asociación con proyectos
   - ✅ Asociación con agents
   - ✅ Asociación con models
   - ✅ Asociación con RAG systems

6. **Validaciones:**
   - ✅ Creación manual de validaciones
   - ✅ Listado de validaciones
   - ✅ Validaciones automáticas (análisis y evaluación)

7. **Frontend:**
   - ✅ Pantallas principales (listado, detalle, registro)
   - ✅ Gestión de relaciones (tabs separados)
   - ✅ Modales para asociar/desasociar
   - ✅ Botones para análisis de cumplimiento (`handleAnalyze`)
   - ✅ Botones para evaluación de rendimiento (`handleEvaluate`)
   - ❌ **Pantalla de ejecución de tests/pruebas** (no existe)
   - ❌ **Tab o sección para probar prompt contra modelo real** (no existe)
   - ❌ **Interfaz para ver respuesta del modelo** (no existe)
   - ❌ **Gestión de test cases en UI** (no existe)

8. **Resiliencia:**
   - ✅ Circuit Breaker
   - ✅ Retry automático
   - ✅ Time Limiter
   - ✅ Fallback methods

### ❌ Lo que falta implementar:

1. **Sistema de Pruebas (Testing):**
   - ❌ **Pantalla de ejecución de tests en frontend** (no existe)
   - ❌ Ejecución de prompts contra modelos reales
   - ❌ Captura de respuestas reales del modelo
   - ❌ Visualización de respuesta del modelo en UI
   - ❌ Comparación con respuestas esperadas
   - ❌ Gestión de test cases en frontend
   - ❌ Reportes de pruebas
   - ❌ Tab "Pruebas" o "Tests" en la página de detalle del prompt

   **Nota:** Los botones "Analizar Cumplimiento" y "Evaluar Rendimiento" existen, pero solo crean validaciones. **No ejecutan el prompt contra un modelo real ni muestran la respuesta**.

2. **Validación de Respuestas Esperadas:**
   - ❌ Campo para respuesta esperada
   - ❌ Comparación automática
   - ❌ Métricas de similitud

3. **Monitoreo Post-Mercado:**
   - ✅ **Implementado mediante módulo de telemetría (OpenTelemetry)**
   - ✅ Los microservicios Python tienen soporte nativo para OpenTelemetry
   - ✅ Métricas Prometheus disponibles (`GET /metrics` en cada microservicio)
   - ✅ Trazabilidad completa de ejecuciones
   - ⚠️ Dashboard de métricas en tiempo real (pendiente de integración en frontend)

4. **Workflow de Aprobación:**
   - ⚠️ Integración BPMN mencionada pero no verificada
   - ❌ Disparo automático de workflow
   - ❌ Estados de aprobación en UI

5. **Métricas Avanzadas:**
   - ✅ **A/B testing entre prompts** (`LLMEvaluationClient.abTestPrompts()`)
   - ✅ **Benchmarking de evaluaciones** (`LLMEvaluationClient.benchmarkEvaluations()`)
   - ✅ **Comparación de versiones** (`PromptGovernanceClient.compareVersions()`)
   - ⚠️ Tendencias temporales (pendiente de visualización en frontend)

6. **Documentación Técnica:**
   - ⚠️ Guía funcional existe
   - ❌ Guía técnica para desarrolladores (parcial)
   - ❌ API documentation completa

### 📊 Resumen de Completitud:

| Área | Estado | Completitud |
|------|--------|-------------|
| CRUD | ✅ | 100% |
| Versionado | ✅ | 100% |
| Análisis Cumplimiento | ✅ | 100% |
| Evaluación Rendimiento | ⚠️ | 80% (falta ejecución real) |
| Gestión Relaciones | ✅ | 100% |
| Validaciones | ⚠️ | 70% (falta validación de respuestas) |
| Pruebas | ❌ | 0% |
| Monitoreo | ✅ | 90% (telemetría implementada, falta dashboard) |
| Workflow Aprobación | ⚠️ | 50% (backend, falta UI) |
| Frontend | ⚠️ | 85% (falta pantalla de tests/pruebas) |

### 🎯 Recomendaciones para Completar:

1. **Prioridad Alta:**
   - Implementar sistema de pruebas (ejecución real contra modelos)
   - Agregar validación de respuestas esperadas
   - Verificar y completar workflow de aprobación

2. **Prioridad Media:**
   - Integrar dashboard de métricas en frontend (telemetría ya disponible)
   - Agregar visualización de tendencias temporales
   - Completar documentación técnica

3. **Prioridad Baja:**
   - A/B testing entre prompts
   - Dashboard de métricas en tiempo real
   - Optimizaciones de rendimiento

**Conclusión:** El módulo está **suficientemente completo para gobierno básico y avanzado de IA**. Las principales áreas pendientes son:

### Frontend (Crítico):
- ❌ **Pantalla de ejecución de tests/pruebas** - No existe interfaz para ejecutar el prompt contra un modelo real
- ❌ **Tab "Pruebas" o "Tests"** - Falta sección en la página de detalle para probar el prompt
- ❌ **Visualización de respuestas** - No se puede ver la respuesta real del modelo en la UI
- ❌ **Gestión de test cases** - No hay interfaz para crear/gestionar casos de prueba

### Backend:
- ❌ Sistema de pruebas con ejecución real contra modelos (usando `ModelWrapperClient`)
- ❌ Validación de respuestas esperadas vs reales
- ❌ Endpoint para ejecutar prompt y obtener respuesta real

### Visualización:
- ⚠️ Dashboard de visualización de métricas de telemetría (telemetría existe, falta dashboard)

**Nota importante:** Los botones "Analizar Cumplimiento" y "Evaluar Rendimiento" en el frontend solo crean validaciones en el backend. **No ejecutan el prompt contra un modelo real ni muestran la respuesta del modelo al usuario**.

---

## 7. ¿Qué falta implementar en el frontend para pruebas?

### Pantalla de Ejecución de Tests (No Existe)

**Ubicación sugerida:** Nueva pestaña "Pruebas" o "Tests" en `/governance/prompts/[id]`

**Funcionalidades requeridas:**

1. **Selector de Modelo:**
   - Dropdown con modelos asociados al prompt
   - O selector de cualquier modelo disponible
   - Mostrar información del modelo (proveedor, versión, costos)

2. **Input de Prueba:**
   - Campo de texto para input del usuario (si el prompt requiere variables)
   - Editor para modificar el prompt antes de ejecutar (opcional)
   - Botón "Ejecutar Prueba"

3. **Visualización de Respuesta:**
   - Área para mostrar la respuesta del modelo
   - Información de la ejecución:
     - Tokens consumidos (input/output)
     - Latencia de respuesta
     - Costo estimado
     - Timestamp de ejecución

4. **Comparación con Respuesta Esperada:**
   - Campo para ingresar respuesta esperada
   - Botón "Comparar"
   - Métricas de similitud:
     - Score de similitud semántica
     - Exact match (sí/no)
     - Diferencias destacadas

5. **Gestión de Test Cases:**
   - Lista de test cases guardados
   - Botón "Guardar como Test Case"
   - Ejecutar test cases en batch
   - Ver historial de ejecuciones

6. **Métricas de Pruebas:**
   - Tasa de éxito (pass rate)
   - Score promedio
   - Gráfico de tendencias
   - Comparación entre versiones del prompt

**Ejemplo de estructura:**

```
/governance/prompts/[id]
├── Tab: Información General
├── Tab: Versiones
├── Tab: Validaciones
├── Tab: Proyecto
├── Tab: Agents
├── Tab: Models
├── Tab: RAG Systems
└── Tab: Pruebas (NUEVO) ⭐
    ├── Sección: Ejecutar Prueba
    │   ├── Selector de modelo
    │   ├── Input (si aplica)
    │   ├── Botón "Ejecutar"
    │   └── Respuesta del modelo
    ├── Sección: Comparar con Esperado
    │   ├── Campo respuesta esperada
    │   ├── Botón "Comparar"
    │   └── Métricas de similitud
    └── Sección: Test Cases
        ├── Lista de test cases
        ├── Botón "Nuevo Test Case"
        └── Ejecutar batch
```

**Backend necesario:**

1. **Endpoint para ejecutar prompt:**
   ```
   POST /api/prompts/{promptId}/test
   Body: {
     "modelId": "gpt-4",
     "input": "test input" (opcional),
     "expectedResponse": "expected output" (opcional)
   }
   Response: {
     "response": "model response",
     "tokensUsed": { "input": 100, "output": 50 },
     "latency": 1.2,
     "cost": 0.001,
     "similarityScore": 0.95 (si hay expectedResponse)
   }
   ```

2. **Endpoint para gestionar test cases:**
   ```
   GET /api/prompts/{promptId}/test-cases
   POST /api/prompts/{promptId}/test-cases
   DELETE /api/prompts/{promptId}/test-cases/{testCaseId}
   POST /api/prompts/{promptId}/test-cases/batch-execute
   ```

**Integración con clientes existentes:**

- Usar `ModelWrapperClient.invoke()` para ejecutar el prompt contra el modelo
- Usar `LLMEvaluationClient.evaluateQuality()` para comparar respuestas
- Usar `LLMEvaluationClient.evaluateConsistency()` para evaluar consistencia

---

## 6. Clientes de Python Disponibles para Prompts

### Clientes Principales

El módulo de prompts utiliza los siguientes clientes Java que se comunican con microservicios Python:

#### 1. **PromptGovernanceClient** (Puerto 8003)

**Funcionalidades:**
- ✅ `evaluateSafety()` - Evaluación de seguridad (injection, jailbreak, PII leakage)
- ✅ `evaluateEffectiveness()` - Evaluación de efectividad (claridad, especificidad, estructura)
- ✅ `detectPiiLeakage()` - Detección de información personal identificable
- ✅ `compareVersions()` - Comparación de versiones de prompts
- ✅ `analyzeCost()` - Análisis de costos (tokens, modelo, volumen)
- ✅ `validateTemplate()` - Validación de templates con variables
- ✅ `optimizeContextWindow()` - Optimización de context window
- ✅ `evaluateFewShotExamples()` - Evaluación de ejemplos few-shot
- ✅ `validateOutputFormat()` - Validación de formato de salida
- ✅ `health()` - Health check
- ✅ `metrics()` - Métricas Prometheus

**Uso en el módulo:**
- Utilizado en `analyzePrompt()` para análisis de cumplimiento
- Integrado con `PromptBusinessService`

#### 2. **LLMEvaluationClient** (Puerto 8002)

**Funcionalidades:**
- ✅ `evaluateHallucination()` - Detecta alucinaciones en respuestas
- ✅ `evaluateToxicity()` - Evalúa toxicidad en texto
- ✅ `evaluateBiasText()` - Detecta sesgos en texto
- ✅ `evaluateQuality()` - Evalúa calidad general de respuesta
- ✅ `evaluatePromptInjection()` - Detecta intentos de prompt injection
- ✅ `evaluateInstructionFollowing()` - Evalúa seguimiento de instrucciones
- ✅ `evaluateConsistency()` - Evalúa consistencia entre respuestas
- ✅ `evaluateFactualGrounding()` - Evalúa fundamentación fáctica
- ✅ `evaluateCostEfficiency()` - Analiza eficiencia de costos
- ✅ `benchmarkEvaluations()` - Benchmarking de múltiples modelos
- ✅ `abTestPrompts()` - **A/B testing de prompts** ⭐
- ✅ `batchEvaluate()` - Evaluación en lote
- ✅ `health()` - Health check
- ✅ `metrics()` - Métricas Prometheus

**Uso en el módulo:**
- Utilizado en `evaluatePrompt()` para evaluación de rendimiento
- Integrado con `PromptBusinessService`

#### 3. **AgentMonitoringClient** (Puerto 8005)

**Funcionalidades:**
- ✅ `analyzeExecution()` - Análisis de ejecución individual
- ✅ `evaluateReliability()` - Evaluación de confiabilidad
- ✅ `analyzeCost()` - Análisis de costos
- ✅ `detectLoops()` - Detección de loops infinitos
- ✅ `analyzeMultiAgentOrchestration()` - Análisis de orquestación multi-agente
- ✅ `evaluateToolUsage()` - Evaluación de uso de herramientas
- ✅ `analyzeSafetyViolations()` - Análisis de violaciones de seguridad
- ✅ `benchmarkAgentPerformance()` - Benchmarking de performance
- ✅ `health()` - Health check

**Uso en el módulo:**
- Disponible para monitoreo de agents asociados a prompts
- Puede utilizarse para análisis de ejecuciones de prompts en agents

#### 4. **ModelWrapperClient** (Puerto 8006)

**Funcionalidades:**
- ✅ `invoke()` - Invocación de modelos
- ✅ `stream()` - Streaming de respuestas
- ✅ `batchInvoke()` - Invocación en lote
- ✅ `health()` - Health check

**Uso potencial:**
- Puede utilizarse para ejecutar prompts contra modelos reales
- Útil para implementar sistema de pruebas

### Telemetría y Monitoreo

**OpenTelemetry:**
- ✅ Todos los microservicios Python tienen soporte nativo para OpenTelemetry
- ✅ Trazabilidad completa de ejecuciones
- ✅ Métricas Prometheus disponibles en cada microservicio (`GET /metrics`)
- ✅ Integración con módulo de telemetría del sistema

**Métricas Disponibles:**
- Latencia de ejecución
- Tasa de errores
- Throughput
- Uso de recursos
- Costos por operación

### Configuración

Los clientes se configuran mediante `AIGovernanceClient` (factoría):

```java
@Autowired
private AIGovernanceClient governance;

// Obtener clientes especializados
PromptGovernanceClient promptClient = governance.promptGovernance();
LLMEvaluationClient llmClient = governance.llmEvaluation();
AgentMonitoringClient agentClient = governance.agentMonitoring();
ModelWrapperClient modelClient = governance.modelWrapper();
```

**Configuración en `application.yml`:**
```yaml
codeflowx:
  leka:
    governance:
      enabled: true
      gateway:
        url: http://api-leka-govern:8000  # K8s
        # url: http://localhost:8000      # Local
      timeout: 30000
      webclient:
        max-in-memory-size: 10485760
```

### Documentación Completa

Todas las guías de uso están disponibles en:
- `codeflowx.govern.nocode.client/PROMPT_GOVERNANCE_CLIENT_GUIDE.md`
- `codeflowx.govern.nocode.client/LLMEVALUATION_CLIENT_GUIDE.md`
- `codeflowx.govern.nocode.client/AGENT_MONITORING_CLIENT_GUIDE.md`
- `codeflowx.govern.nocode.client/MODEL_WRAPPER_CLIENT_GUIDE.md`
- `codeflowx.govern.nocode.client/GUIA_USO_CLIENTE.md` (guía general)
