# Módulo de AI Playground

## Descripción General

El **AI Playground Module** es un entorno interactivo para experimentar, probar y validar modelos de IA, agentes, y sistemas de machine learning. Proporciona herramientas para testing, fine-tuning y evaluación de modelos en un entorno controlado.

## Arquitectura del Sistema

### Componentes Principales

1. **Model Testing Engine** - Motor de testing de modelos
2. **Agent Simulation** - Simulación de agentes de IA
3. **Fine-tuning Environment** - Entorno para fine-tuning
4. **Performance Metrics** - Métricas de rendimiento
5. **Dataset Management** - Gestión de datasets de prueba

### Tecnologías Soportadas

- **LLMs**: GPT, Claude, LLaMA, Mistral
- **Embeddings**: OpenAI, Cohere, Sentence-Transformers
- **Vector Databases**: Pinecone, Weaviate, Qdrant
- **RAG Systems**: LangChain, LlamaIndex
- **Fine-tuning**: LoRA, QLoRA, PEFT
- **Evaluation**: MLflow, CodeflowX

## Entidades del Sistema

### 1. Sesiones de AI Playground

```java
@Entity
@Table(name = "aip_ai_playground_sessions")
public class AIPlaygroundSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "session_name", nullable = false)
    private String sessionName;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "session_type", nullable = false)
    private SessionType sessionType;
    
    @Column(name = "model_config", columnDefinition = "JSONB")
    private String modelConfig;
    
    @Column(name = "dataset_config", columnDefinition = "JSONB")
    private String datasetConfig;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SessionStatus status;
    
    @Column(name = "started_at")
    private LocalDateTime startedAt;
    
    @Column(name = "last_activity")
    private LocalDateTime lastActivity;
    
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Getters y Setters
}

public enum SessionType {
    MODEL_TESTING, AGENT_SIMULATION, FINE_TUNING, RAG_TESTING, 
    EMBEDDING_TESTING, EVALUATION, COMPARISON
}

public enum SessionStatus {
    ACTIVE, PAUSED, EXPIRED, TERMINATED, FAILED
}
```

### 2. Tests de Modelos

```java
@Entity
@Table(name = "aip_model_tests")
public class ModelTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "session_id", nullable = false)
    private Long sessionId;
    
    @Column(name = "test_name", nullable = false)
    private String testName;
    
    @Column(name = "model_id")
    private Long modelId;
    
    @Column(name = "test_type", nullable = false)
    private String testType;
    
    @Column(name = "input_data", columnDefinition = "JSONB")
    private String inputData;
    
    @Column(name = "expected_output")
    private String expectedOutput;
    
    @Column(name = "actual_output", columnDefinition = "TEXT")
    private String actualOutput;
    
    @Column(name = "performance_metrics", columnDefinition = "JSONB")
    private String performanceMetrics;
    
    @Column(name = "execution_time_ms")
    private Long executionTimeMs;
    
    @Column(name = "tokens_used")
    private Integer tokensUsed;
    
    @Column(name = "cost_usd")
    private BigDecimal costUsd;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TestStatus status;
    
    @Column(name = "started_at")
    private LocalDateTime startedAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Getters y Setters
}

public enum TestStatus {
    PENDING, RUNNING, COMPLETED, FAILED, TIMEOUT
}
```

### 3. Datasets de Prueba

```java
@Entity
@Table(name = "aip_test_datasets")
public class TestDataset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "dataset_name", nullable = false)
    private String datasetName;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "dataset_type", nullable = false)
    private String datasetType;
    
    @Column(name = "data_schema", columnDefinition = "JSONB")
    private String dataSchema;
    
    @Column(name = "sample_data", columnDefinition = "JSONB")
    private String sampleData;
    
    @Column(name = "total_records")
    private Integer totalRecords;
    
    @Column(name = "file_size_mb")
    private Long fileSizeMb;
    
    @Column(name = "file_path")
    private String filePath;
    
    @Column(name = "is_public")
    private Boolean isPublic;
    
    @Column(name = "created_by")
    private Long createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Getters y Setters
}
```

### 4. Configuraciones de Modelos

```java
@Entity
@Table(name = "aip_model_configs")
public class ModelConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "config_name", nullable = false)
    private String configName;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "model_provider", nullable = false)
    private String modelProvider;
    
    @Column(name = "model_name", nullable = false)
    private String modelName;
    
    @Column(name = "model_version")
    private String modelVersion;
    
    @Column(name = "parameters", columnDefinition = "JSONB")
    private String parameters;
    
    @Column(name = "api_config", columnDefinition = "JSONB")
    private String apiConfig;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    
    @Column(name = "created_by")
    private Long createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Getters y Setters
}
```

### 5. Métricas de Rendimiento

```java
@Entity
@Table(name = "aip_performance_metrics")
public class PerformanceMetrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "test_id", nullable = false)
    private Long testId;
    
    @Column(name = "metric_name", nullable = false)
    private String metricName;
    
    @Column(name = "metric_value", nullable = false)
    private Double metricValue;
    
    @Column(name = "metric_unit")
    private String metricUnit;
    
    @Column(name = "threshold_value")
    private Double thresholdValue;
    
    @Column(name = "is_threshold_met")
    private Boolean isThresholdMet;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Getters y Setters
}
```

## Servicios del Sistema

### 1. Servicio de AI Playground

```java
@Service
@Transactional
public class AIPlaygroundService {
    
    @Autowired
    private AIPlaygroundSessionRepository sessionRepository;
    
    @Autowired
    private ModelTestRepository testRepository;
    
    @Autowired
    private TestDatasetRepository datasetRepository;
    
    @Autowired
    private ModelConfigRepository configRepository;
    
    @Autowired
    private LekaServerIntegrationService lekaService;
    
    /**
     * Crea una nueva sesión de AI Playground
     */
    public AIPlaygroundSession createSession(SessionCreationRequest request) {
        // Validar límites del usuario
        validateUserLimits(request.getUserId());
        
        // Crear sesión
        AIPlaygroundSession session = new AIPlaygroundSession();
        session.setSessionName(request.getSessionName());
        session.setDescription(request.getDescription());
        session.setUserId(request.getUserId());
        session.setSessionType(request.getSessionType());
        session.setModelConfig(request.getModelConfig());
        session.setDatasetConfig(request.getDatasetConfig());
        session.setStatus(SessionStatus.ACTIVE);
        session.setStartedAt(LocalDateTime.now());
        session.setLastActivity(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusHours(48));
        session.setCreatedAt(LocalDateTime.now());
        
        session = sessionRepository.save(session);
        
        // Inicializar entorno según el tipo de sesión
        initializeSessionEnvironment(session);
        
        return session;
    }
    
    /**
     * Ejecuta un test de modelo
     */
    public ModelTest executeModelTest(Long sessionId, ModelTestRequest request) {
        AIPlaygroundSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Sesión no encontrada"));
        
        // Validar sesión activa
        if (session.getStatus() != SessionStatus.ACTIVE) {
            throw new InvalidSessionException("Sesión no está activa");
        }
        
        // Crear test
        ModelTest test = new ModelTest();
        test.setSessionId(sessionId);
        test.setTestName(request.getTestName());
        test.setModelId(request.getModelId());
        test.setTestType(request.getTestType());
        test.setInputData(request.getInputData());
        test.setExpectedOutput(request.getExpectedOutput());
        test.setStatus(TestStatus.PENDING);
        test.setStartedAt(LocalDateTime.now());
        test.setCreatedAt(LocalDateTime.now());
        
        test = testRepository.save(test);
        
        // Ejecutar test de forma asíncrona
        CompletableFuture.runAsync(() -> executeModelTestAsync(test));
        
        return test;
    }
    
    /**
     * Ejecuta test de modelo de forma asíncrona
     */
    private void executeModelTestAsync(ModelTest test) {
        try {
            test.setStatus(TestStatus.RUNNING);
            testRepository.save(test);
            
            long startTime = System.currentTimeMillis();
            
            // Ejecutar test usando Leka Server
            ModelTestResult result = lekaService.executeModelTest(test);
            
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Actualizar test con resultados
            test.setActualOutput(result.getOutput());
            test.setPerformanceMetrics(result.getMetrics());
            test.setExecutionTimeMs(executionTime);
            test.setTokensUsed(result.getTokensUsed());
            test.setCostUsd(result.getCost());
            test.setStatus(TestStatus.COMPLETED);
            test.setCompletedAt(LocalDateTime.now());
            
            testRepository.save(test);
            
            // Guardar métricas de rendimiento
            savePerformanceMetrics(test, result.getMetrics());
            
        } catch (Exception e) {
            test.setStatus(TestStatus.FAILED);
            test.setCompletedAt(LocalDateTime.now());
            testRepository.save(test);
        }
    }
}
```

### 2. Servicio de Testing de Modelos

```java
@Service
public class ModelTestingService {
    
    @Autowired
    private ModelConfigRepository configRepository;
    
    @Autowired
    private TestDatasetRepository datasetRepository;
    
    /**
     * Ejecuta diferentes tipos de tests
     */
    public ModelTestResult executeTest(ModelTest test, String testType) {
        switch (testType) {
            case "TEXT_GENERATION":
                return executeTextGenerationTest(test);
            case "QUESTION_ANSWERING":
                return executeQuestionAnsweringTest(test);
            case "SUMMARIZATION":
                return executeSummarizationTest(test);
            case "TRANSLATION":
                return executeTranslationTest(test);
            case "SENTIMENT_ANALYSIS":
                return executeSentimentAnalysisTest(test);
            case "CODE_GENERATION":
                return executeCodeGenerationTest(test);
            default:
                throw new UnsupportedTestTypeException("Tipo de test no soportado");
        }
    }
    
    /**
     * Ejecuta test de generación de texto
     */
    private ModelTestResult executeTextGenerationTest(ModelTest test) {
        try {
            // Preparar prompt
            String prompt = buildTextGenerationPrompt(test.getInputData());
            
            // Ejecutar modelo
            ModelResponse response = executeModel(test.getModelId(), prompt);
            
            // Calcular métricas
            Map<String, Object> metrics = calculateTextGenerationMetrics(
                test.getExpectedOutput(), response.getOutput());
            
            return new ModelTestResult(response.getOutput(), metrics, 
                response.getTokensUsed(), response.getCost());
            
        } catch (Exception e) {
            throw new TestExecutionException("Error ejecutando test de generación de texto", e);
        }
    }
    
    /**
     * Ejecuta test de pregunta-respuesta
     */
    private ModelTestResult executeQuestionAnsweringTest(ModelTest test) {
        try {
            // Preparar contexto y pregunta
            String context = extractContext(test.getInputData());
            String question = extractQuestion(test.getInputData());
            
            // Construir prompt
            String prompt = buildQAPrompt(context, question);
            
            // Ejecutar modelo
            ModelResponse response = executeModel(test.getModelId(), prompt);
            
            // Calcular métricas de precisión
            Map<String, Object> metrics = calculateQAMetrics(
                test.getExpectedOutput(), response.getOutput());
            
            return new ModelTestResult(response.getOutput(), metrics, 
                response.getTokensUsed(), response.getCost());
            
        } catch (Exception e) {
            throw new TestExecutionException("Error ejecutando test de pregunta-respuesta", e);
        }
    }
}
```

## API Endpoints

### 1. Gestión de Sesiones

```java
@RestController
@RequestMapping("/api/v1/ai-playground/sessions")
public class AIPlaygroundSessionController {
    
    @Autowired
    private AIPlaygroundService playgroundService;
    
    /**
     * Crear nueva sesión
     */
    @PostMapping
    public ResponseEntity<AIPlaygroundSession> createSession(@RequestBody SessionCreationRequest request) {
        AIPlaygroundSession session = playgroundService.createSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(session);
    }
    
    /**
     * Obtener sesión por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<AIPlaygroundSession> getSession(@PathVariable Long id) {
        AIPlaygroundSession session = playgroundService.getSessionById(id);
        return ResponseEntity.ok(session);
    }
    
    /**
     * Listar sesiones del usuario
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<AIPlaygroundSession>> getUserSessions(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Page<AIPlaygroundSession> sessions = playgroundService.getUserSessions(userId, page, size);
        return ResponseEntity.ok(sessions);
    }
    
    /**
     * Pausar sesión
     */
    @PostMapping("/{id}/pause")
    public ResponseEntity<Void> pauseSession(@PathVariable Long id) {
        playgroundService.pauseSession(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Reanudar sesión
     */
    @PostMapping("/{id}/resume")
    public ResponseEntity<Void> resumeSession(@PathVariable Long id) {
        playgroundService.resumeSession(id);
        return ResponseEntity.ok().build();
    }
}
```

### 2. Testing de Modelos

```java
@RestController
@RequestMapping("/api/v1/ai-playground/tests")
public class ModelTestController {
    
    @Autowired
    private AIPlaygroundService playgroundService;
    
    /**
     * Ejecutar test de modelo
     */
    @PostMapping("/sessions/{sessionId}/execute")
    public ResponseEntity<ModelTest> executeModelTest(
            @PathVariable Long sessionId,
            @RequestBody ModelTestRequest request) {
        
        ModelTest test = playgroundService.executeModelTest(sessionId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(test);
    }
    
    /**
     * Obtener resultado de test
     */
    @GetMapping("/{id}")
    public ResponseEntity<ModelTest> getTest(@PathVariable Long id) {
        ModelTest test = playgroundService.getTestById(id);
        return ResponseEntity.ok(test);
    }
    
    /**
     * Listar tests de una sesión
     */
    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<Page<ModelTest>> getSessionTests(
            @PathVariable Long sessionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Page<ModelTest> tests = playgroundService.getSessionTests(sessionId, page, size);
        return ResponseEntity.ok(tests);
    }
    
    /**
     * Comparar resultados de tests
     */
    @PostMapping("/compare")
    public ResponseEntity<TestComparisonResult> compareTests(@RequestBody TestComparisonRequest request) {
        TestComparisonResult result = playgroundService.compareTests(request);
        return ResponseEntity.ok(result);
    }
}
```

### 3. Gestión de Datasets

```java
@RestController
@RequestMapping("/api/v1/ai-playground/datasets")
public class TestDatasetController {
    
    @Autowired
    private TestDatasetService datasetService;
    
    /**
     * Crear dataset
     */
    @PostMapping
    public ResponseEntity<TestDataset> createDataset(@RequestBody DatasetCreationRequest request) {
        TestDataset dataset = datasetService.createDataset(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dataset);
    }
    
    /**
     * Subir archivo de dataset
     */
    @PostMapping("/{id}/upload")
    public ResponseEntity<Void> uploadDatasetFile(@PathVariable Long id, 
                                                @RequestParam("file") MultipartFile file) {
        datasetService.uploadDatasetFile(id, file);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Listar datasets
     */
    @GetMapping
    public ResponseEntity<Page<TestDataset>> listDatasets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String datasetType) {
        
        Page<TestDataset> datasets = datasetService.listDatasets(page, size, datasetType);
        return ResponseEntity.ok(datasets);
    }
    
    /**
     * Descargar dataset
     */
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadDataset(@PathVariable Long id) {
        Resource resource = datasetService.downloadDataset(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
```

## Configuración de Aplicación

### application.yml

```yaml
# Configuración del Módulo de AI Playground
ai-playground:
  # Configuración de sesiones
  sessions:
    max-active-sessions-per-user: 3
    session-timeout-hours: 48
    max-session-size-mb: 500
  
  # Configuración de testing
  testing:
    max-test-time-seconds: 600
    max-concurrent-tests: 5
    default-temperature: 0.7
    default-max-tokens: 1000
  
  # Configuración de modelos
  models:
    default-provider: openai
    supported-providers: [openai, anthropic, cohere, local]
    rate-limit-per-minute: 60
  
  # Configuración de Leka Server
  leka-server:
    base-url: ${LEKA_SERVER_URL:http://localhost:8000}
    timeout-seconds: 120
    retry-attempts: 3
  
  # Configuración de almacenamiento
  storage:
    dataset-path: ${DATASET_PATH:/tmp/ai-playground-datasets}
    max-dataset-size-mb: 1024
    cleanup-enabled: true
    cleanup-interval-hours: 48

# Configuración de Prometheus
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
    tags:
      application: codeflowx-ai-playground
      module: ai-playground
```

## Métricas de Prometheus

### Métricas Personalizadas

```java
@Component
public class AIPlaygroundMetrics {
    
    private final Counter sessionsCreatedTotal;
    private final Counter modelTestsTotal;
    private final Counter modelTestsSuccessful;
    private final Counter modelTestsFailed;
    private final Histogram testExecutionTimeSeconds;
    private final Gauge activeSessions;
    private final Gauge activeTests;
    private final Counter totalTokensUsed;
    private final Counter totalCostUsd;
    
    public AIPlaygroundMetrics(MeterRegistry meterRegistry) {
        this.sessionsCreatedTotal = Counter.builder("ai_playground_sessions_created_total")
                .description("Total de sesiones de AI playground creadas")
                .tag("module", "ai-playground")
                .register(meterRegistry);
        
        this.modelTestsTotal = Counter.builder("ai_playground_model_tests_total")
                .description("Total de tests de modelos ejecutados")
                .tag("module", "ai-playground")
                .register(meterRegistry);
        
        this.modelTestsSuccessful = Counter.builder("ai_playground_model_tests_successful")
                .description("Tests de modelos exitosos")
                .tag("module", "ai-playground")
                .register(meterRegistry);
        
        this.modelTestsFailed = Counter.builder("ai_playground_model_tests_failed")
                .description("Tests de modelos fallidos")
                .tag("module", "ai-playground")
                .register(meterRegistry);
        
        this.testExecutionTimeSeconds = Histogram.builder("ai_playground_test_execution_time_seconds")
                .description("Tiempo de ejecución de tests en segundos")
                .tag("module", "ai-playground")
                .buckets(10, 30, 60, 120, 300, 600)
                .register(meterRegistry);
        
        this.activeSessions = Gauge.builder("ai_playground_active_sessions")
                .description("Sesiones activas actualmente")
                .tag("module", "ai-playground")
                .register(meterRegistry);
        
        this.activeTests = Gauge.builder("ai_playground_active_tests")
                .description("Tests activos actualmente")
                .tag("module", "ai-playground")
                .register(meterRegistry);
        
        this.totalTokensUsed = Counter.builder("ai_playground_total_tokens_used")
                .description("Total de tokens utilizados")
                .tag("module", "ai-playground")
                .register(meterRegistry);
        
        this.totalCostUsd = Counter.builder("ai_playground_total_cost_usd")
                .description("Costo total en USD")
                .tag("module", "ai-playground")
                .register(meterRegistry);
    }
    
    public void recordSessionCreated() {
        sessionsCreatedTotal.increment();
        activeSessions.increment();
    }
    
    public void recordSessionTerminated() {
        activeSessions.decrement();
    }
    
    public void recordModelTestStarted() {
        modelTestsTotal.increment();
        activeTests.increment();
    }
    
    public void recordModelTestCompleted(long executionTimeSeconds, boolean success, 
                                       int tokensUsed, BigDecimal cost) {
        this.testExecutionTimeSeconds.record(executionTimeSeconds);
        activeTests.decrement();
        
        if (success) {
            modelTestsSuccessful.increment();
        } else {
            modelTestsFailed.increment();
        }
        
        totalTokensUsed.increment(tokensUsed);
        totalCostUsd.increment(cost.doubleValue());
    }
}
```

## Casos de Uso

### 1. Testing de Generación de Texto

```json
{
  "testName": "Generación de Descripción de Producto",
  "testType": "TEXT_GENERATION",
  "inputData": {
    "product": "Smartphone Android",
    "features": ["5G", "128GB", "Triple Camera", "5000mAh Battery"],
    "targetAudience": "Profesionales jóvenes"
  },
  "expectedOutput": "Descripción profesional y atractiva del producto",
  "modelId": 1
}
```

### 2. Testing de RAG System

```json
{
  "testName": "RAG - Consulta sobre Documentación Técnica",
  "testType": "QUESTION_ANSWERING",
  "inputData": {
    "context": "Documentación técnica de CodeflowX",
    "question": "¿Cómo configurar un agente de IA?",
    "expectedSections": ["configuración", "agentes", "IA"]
  },
  "expectedOutput": "Respuesta basada en la documentación técnica",
  "modelId": 2
}
```

### 3. Comparación de Modelos

```json
{
  "comparisonName": "Comparación GPT-4 vs Claude-3",
  "models": [1, 2],
  "testDataset": 5,
  "metrics": ["accuracy", "response_time", "cost", "quality_score"],
  "testTypes": ["TEXT_GENERATION", "QUESTION_ANSWERING", "SUMMARIZATION"]
}
```

## Conclusión

El **AI Playground Module** proporciona un entorno completo para:

- **Testing de modelos** de IA en diferentes escenarios
- **Evaluación de rendimiento** con métricas detalladas
- **Comparación de modelos** para selección óptima
- **Fine-tuning** y optimización de parámetros
- **Testing de sistemas RAG** y embeddings
- **Validación de agentes** de IA
- **Gestión de datasets** de prueba
- **Métricas de costos** y uso de tokens

El sistema está diseñado para integrarse con Leka Server y proporcionar un entorno seguro para experimentación con IA, manteniendo control sobre costos y recursos utilizados.
