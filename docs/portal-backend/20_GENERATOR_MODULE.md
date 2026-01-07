# Módulo de Generación Automática de Código

## Descripción General

El **Generator Module** es un sistema inteligente para la generación automática de código basado en especificaciones de diseño, modelos de datos y patrones arquitectónicos. Utiliza agentes especializados para generar código en múltiples tecnologías y frameworks.

## Arquitectura del Sistema

### Componentes Principales

1. **Code Generation Engine** - Motor principal de generación
2. **Technology Adapters** - Adaptadores para diferentes tecnologías
3. **Template Engine** - Sistema de plantillas inteligentes
4. **Agent Orchestration** - Orquestación de agentes especializados
5. **Quality Assurance** - Validación y testing automático del código

### Tecnologías Soportadas

- **Frontend**: React, Vue.js, Angular, Next.js
- **Backend**: Spring Boot, FastAPI, Express.js, Django
- **Database**: PostgreSQL, MySQL, MongoDB, Redis
- **Mobile**: React Native, Flutter, iOS (Swift), Android (Kotlin)
- **DevOps**: Docker, Kubernetes, Terraform, GitHub Actions

## Entidades del Sistema

### 1. Generación de Código

```java
@Entity
@Table(name = "gen_code_generation_jobs")
public class CodeGenerationJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_name", nullable = false)
    private String jobName;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private GenerationStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_technology", nullable = false)
    private TargetTechnology targetTechnology;

    @Column(name = "source_design_id")
    private Long sourceDesignId;

    @Column(name = "source_module")
    private String sourceModule;

    @Column(name = "configuration", columnDefinition = "JSONB")
    private String configuration;

    @Column(name = "generated_files", columnDefinition = "JSONB")
    private String generatedFiles;

    @Column(name = "error_log")
    private String errorLog;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Getters y Setters
}

public enum GenerationStatus {
    PENDING, IN_PROGRESS, COMPLETED, FAILED, CANCELLED
}

public enum TargetTechnology {
    REACT, VUE, ANGULAR, NEXT_JS, SPRING_BOOT, FASTAPI,
    EXPRESS_JS, DJANGO, POSTGRESQL, MYSQL, MONGODB,
    REACT_NATIVE, FLUTTER, IOS, ANDROID, DOCKER,
    KUBERNETES, TERRAFORM, GITHUB_ACTIONS
}
```

### 2. Plantillas de Generación

```java
@Entity
@Table(name = "gen_code_templates")
public class CodeTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_name", nullable = false, unique = true)
    private String templateName;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "technology", nullable = false)
    private TargetTechnology technology;

    @Column(name = "template_type", nullable = false)
    private String templateType;

    @Column(name = "template_content", columnDefinition = "TEXT")
    private String templateContent;

    @Column(name = "variables", columnDefinition = "JSONB")
    private String variables;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "version")
    private String version;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Getters y Setters
}
```

### 3. Configuraciones de Generación

```java
@Entity
@Table(name = "gen_generation_configs")
public class GenerationConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "config_name", nullable = false)
    private String configName;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "technology", nullable = false)
    private TargetTechnology technology;

    @Column(name = "config_type", nullable = false)
    private String configType;

    @Column(name = "settings", columnDefinition = "JSONB")
    private String settings;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Getters y Setters
}
```

### 4. Historial de Generaciones

```java
@Entity
@Table(name = "gen_generation_history")
public class GenerationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_id")
    private Long jobId;

    @Column(name = "generation_type", nullable = false)
    private String generationType;

    @Column(name = "source_entity")
    private String sourceEntity;

    @Column(name = "source_id")
    private Long sourceId;

    @Column(name = "target_technology", nullable = false)
    private String targetTechnology;

    @Column(name = "generated_files_count")
    private Integer generatedFilesCount;

    @Column(name = "total_lines_of_code")
    private Integer totalLinesOfCode;

    @Column(name = "generation_time_seconds")
    private Integer generationTimeSeconds;

    @Column(name = "success_rate")
    private Double successRate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Getters y Setters
}
```

### 5. Agentes de Generación

```java
@Entity
@Table(name = "gen_generation_agents")
public class GenerationAgent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agent_name", nullable = false, unique = true)
    private String agentName;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialization", nullable = false)
    private AgentSpecialization specialization;

    @Column(name = "technology_stack", columnDefinition = "JSONB")
    private String technologyStack;

    @Column(name = "capabilities", columnDefinition = "JSONB")
    private String capabilities;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "version")
    private String version;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Getters y Setters
}

public enum AgentSpecialization {
    FRONTEND_UI, BACKEND_API, DATABASE_SCHEMA, MOBILE_APP,
    DEVOPS_INFRASTRUCTURE, TESTING_FRAMEWORK, DOCUMENTATION,
    SECURITY_LAYER, PERFORMANCE_OPTIMIZATION, ACCESSIBILITY
}
```

## Servicios del Sistema

### 1. Servicio de Generación de Código

```java
@Service
@Transactional
public class CodeGenerationService {

    @Autowired
    private CodeGenerationJobRepository jobRepository;

    @Autowired
    private CodeTemplateRepository templateRepository;

    @Autowired
    private GenerationConfigRepository configRepository;

    @Autowired
    private AgentOrchestrationService agentService;

    /**
     * Inicia un trabajo de generación de código
     */
    public CodeGenerationJob startGeneration(GenerationRequest request) {
        // Validación de la solicitud
        validateGenerationRequest(request);

        // Crear trabajo de generación
        CodeGenerationJob job = createGenerationJob(request);

        // Iniciar generación asíncrona
        CompletableFuture.runAsync(() -> executeGeneration(job));

        return job;
    }

    /**
     * Ejecuta la generación de código
     */
    private void executeGeneration(CodeGenerationJob job) {
        try {
            job.setStatus(GenerationStatus.IN_PROGRESS);
            job.setStartedAt(LocalDateTime.now());
            jobRepository.save(job);

            // Obtener plantillas y configuración
            CodeTemplate template = getTemplateForTechnology(job.getTargetTechnology());
            GenerationConfig config = getConfigForTechnology(job.getTargetTechnology());

            // Orquestar agentes para la generación
            GenerationResult result = agentService.orchestrateGeneration(job, template, config);

            // Procesar resultado
            processGenerationResult(job, result);

        } catch (Exception e) {
            handleGenerationError(job, e);
        }
    }

    /**
     * Procesa el resultado de la generación
     */
    private void processGenerationResult(CodeGenerationJob job, GenerationResult result) {
        job.setStatus(GenerationStatus.COMPLETED);
        job.setCompletedAt(LocalDateTime.now());
        job.setGeneratedFiles(result.getGeneratedFiles());
        jobRepository.save(job);

        // Registrar en historial
        saveGenerationHistory(job, result);
    }

    /**
     * Maneja errores durante la generación
     */
    private void handleGenerationError(CodeGenerationJob job, Exception e) {
        job.setStatus(GenerationStatus.FAILED);
        job.setErrorLog(e.getMessage());
        job.setCompletedAt(LocalDateTime.now());
        jobRepository.save(job);
    }
}
```

### 2. Servicio de Orquestación de Agentes

```java
@Service
public class AgentOrchestrationService {

    @Autowired
    private GenerationAgentRepository agentRepository;

    @Autowired
    private LekaServerIntegrationService lekaService;

    /**
     * Orquesta la generación usando múltiples agentes
     */
    public GenerationResult orchestrateGeneration(CodeGenerationJob job,
                                                CodeTemplate template,
                                                GenerationConfig config) {

        // Determinar agentes necesarios
        List<GenerationAgent> requiredAgents = determineRequiredAgents(job, template);

        // Crear pipeline de generación
        GenerationPipeline pipeline = createGenerationPipeline(job, requiredAgents);

        // Ejecutar pipeline
        return executeGenerationPipeline(pipeline);
    }

    /**
     * Determina los agentes necesarios para la generación
     */
    private List<GenerationAgent> determineRequiredAgents(CodeGenerationJob job,
                                                         CodeTemplate template) {
        List<GenerationAgent> agents = new ArrayList<>();

        // Agente principal según la tecnología
        agents.add(getPrimaryAgent(job.getTargetTechnology()));

        // Agentes especializados según el tipo de generación
        if (template.getTemplateType().contains("UI")) {
            agents.add(getAgentBySpecialization(AgentSpecialization.FRONTEND_UI));
        }

        if (template.getTemplateType().contains("API")) {
            agents.add(getAgentBySpecialization(AgentSpecialization.BACKEND_API));
        }

        if (template.getTemplateType().contains("DATABASE")) {
            agents.add(getAgentBySpecialization(AgentSpecialization.DATABASE_SCHEMA));
        }

        return agents;
    }

    /**
     * Ejecuta el pipeline de generación
     */
    private GenerationResult executeGenerationPipeline(GenerationPipeline pipeline) {
        GenerationResult result = new GenerationResult();

        for (GenerationStep step : pipeline.getSteps()) {
            try {
                // Ejecutar paso usando Leka Server
                StepResult stepResult = lekaService.executeGenerationStep(step);
                result.addStepResult(stepResult);

                if (!stepResult.isSuccess()) {
                    throw new GenerationException("Error en paso: " + step.getStepName());
                }

            } catch (Exception e) {
                throw new GenerationException("Error ejecutando pipeline", e);
            }
        }

        return result;
    }
}
```

### 3. Servicio de Plantillas

```java
@Service
public class CodeTemplateService {

    @Autowired
    private CodeTemplateRepository templateRepository;

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * Crea una nueva plantilla de código
     */
    public CodeTemplate createTemplate(TemplateCreationRequest request) {
        // Validar plantilla
        validateTemplate(request);

        // Crear plantilla
        CodeTemplate template = new CodeTemplate();
        template.setTemplateName(request.getTemplateName());
        template.setDescription(request.getDescription());
        template.setTechnology(request.getTechnology());
        template.setTemplateType(request.getTemplateType());
        template.setTemplateContent(request.getTemplateContent());
        template.setVariables(request.getVariables());
        template.setIsActive(true);
        template.setVersion("1.0.0");
        template.setCreatedAt(LocalDateTime.now());

        return templateRepository.save(template);
    }

    /**
     * Renderiza una plantilla con variables
     */
    public String renderTemplate(CodeTemplate template, Map<String, Object> variables) {
        try {
            return templateEngine.render(template.getTemplateContent(), variables);
        } catch (Exception e) {
            throw new TemplateRenderingException("Error renderizando plantilla", e);
        }
    }

    /**
     * Valida una plantilla antes de crearla
     */
    private void validateTemplate(TemplateCreationRequest request) {
        if (templateRepository.existsByTemplateName(request.getTemplateName())) {
            throw new TemplateAlreadyExistsException("Plantilla ya existe: " + request.getTemplateName());
        }

        // Validar sintaxis de la plantilla
        validateTemplateSyntax(request.getTemplateContent());

        // Validar variables
        validateTemplateVariables(request.getVariables());
    }
}
```

## API Endpoints

### 1. Gestión de Trabajos de Generación

```java
@RestController
@RequestMapping("/api/v1/generator/jobs")
public class CodeGenerationJobController {

    @Autowired
    private CodeGenerationService generationService;

    /**
     * Crear nuevo trabajo de generación
     */
    @PostMapping
    public ResponseEntity<CodeGenerationJob> createGenerationJob(@RequestBody GenerationRequest request) {
        CodeGenerationJob job = generationService.startGeneration(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(job);
    }

    /**
     * Obtener trabajo por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CodeGenerationJob> getGenerationJob(@PathVariable Long id) {
        CodeGenerationJob job = generationService.getJobById(id);
        return ResponseEntity.ok(job);
    }

    /**
     * Listar trabajos de generación
     */
    @GetMapping
    public ResponseEntity<Page<CodeGenerationJob>> listGenerationJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) GenerationStatus status,
            @RequestParam(required = false) TargetTechnology technology) {

        Page<CodeGenerationJob> jobs = generationService.listJobs(page, size, status, technology);
        return ResponseEntity.ok(jobs);
    }

    /**
     * Cancelar trabajo de generación
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelGenerationJob(@PathVariable Long id) {
        generationService.cancelJob(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Descargar código generado
     */
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadGeneratedCode(@PathVariable Long id) {
        Resource resource = generationService.downloadGeneratedCode(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"generated-code.zip\"")
                .body(resource);
    }
}
```

### 2. Gestión de Plantillas

```java
@RestController
@RequestMapping("/api/v1/generator/templates")
public class CodeTemplateController {

    @Autowired
    private CodeTemplateService templateService;

    /**
     * Crear nueva plantilla
     */
    @PostMapping
    public ResponseEntity<CodeTemplate> createTemplate(@RequestBody TemplateCreationRequest request) {
        CodeTemplate template = templateService.createTemplate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(template);
    }

    /**
     * Actualizar plantilla
     */
    @PutMapping("/{id}")
    public ResponseEntity<CodeTemplate> updateTemplate(@PathVariable Long id,
                                                    @RequestBody TemplateUpdateRequest request) {
        CodeTemplate template = templateService.updateTemplate(id, request);
        return ResponseEntity.ok(template);
    }

    /**
     * Listar plantillas
     */
    @GetMapping
    public ResponseEntity<Page<CodeTemplate>> listTemplates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) TargetTechnology technology,
            @RequestParam(required = false) String templateType) {

        Page<CodeTemplate> templates = templateService.listTemplates(page, size, technology, templateType);
        return ResponseEntity.ok(templates);
    }

    /**
     * Renderizar plantilla con variables
     */
    @PostMapping("/{id}/render")
    public ResponseEntity<TemplateRenderResponse> renderTemplate(@PathVariable Long id,
                                                              @RequestBody Map<String, Object> variables) {
        String renderedContent = templateService.renderTemplate(id, variables);
        TemplateRenderResponse response = new TemplateRenderResponse(renderedContent);
        return ResponseEntity.ok(response);
    }
}
```

### 3. Gestión de Configuraciones

```java
@RestController
@RequestMapping("/api/v1/generator/configs")
public class GenerationConfigController {

    @Autowired
    private GenerationConfigService configService;

    /**
     * Crear nueva configuración
     */
    @PostMapping
    public ResponseEntity<GenerationConfig> createConfig(@RequestBody ConfigCreationRequest request) {
        GenerationConfig config = configService.createConfig(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(config);
    }

    /**
     * Obtener configuración por tecnología
     */
    @GetMapping("/technology/{technology}")
    public ResponseEntity<GenerationConfig> getConfigByTechnology(@PathVariable TargetTechnology technology) {
        GenerationConfig config = configService.getDefaultConfigForTechnology(technology);
        return ResponseEntity.ok(config);
    }

    /**
     * Listar configuraciones
     */
    @GetMapping
    public ResponseEntity<Page<GenerationConfig>> listConfigs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) TargetTechnology technology) {

        Page<GenerationConfig> configs = configService.listConfigs(page, size, technology);
        return ResponseEntity.ok(configs);
    }
}
```

## Configuración de Aplicación

### application.yml

```yaml
# Configuración del Módulo de Generación
generator:
  # Configuración de agentes
  agents:
    max-concurrent-generations: 5
    timeout-seconds: 300
    retry-attempts: 3

  # Configuración de plantillas
  templates:
    cache-enabled: true
    cache-ttl-seconds: 3600
    validation-enabled: true

  # Configuración de Leka Server
  leka-server:
    base-url: ${LEKA_SERVER_URL:http://localhost:8000}
    timeout-seconds: 60
    retry-attempts: 3

  # Configuración de almacenamiento
  storage:
    generated-code-path: ${GENERATED_CODE_PATH:/tmp/generated-code}
    max-storage-size-mb: 1024
    cleanup-enabled: true
    cleanup-interval-hours: 24

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
      application: codeflowx-generator
      module: generator
```

## Métricas de Prometheus

### Métricas Personalizadas

```java
@Component
public class GeneratorMetrics {

    private final Counter generationJobsTotal;
    private final Counter generationJobsSuccessful;
    private final Counter generationJobsFailed;
    private final Histogram generationTimeSeconds;
    private final Gauge activeGenerations;
    private final Counter generatedLinesOfCode;

    public GeneratorMetrics(MeterRegistry meterRegistry) {
        this.generationJobsTotal = Counter.builder("generator_jobs_total")
                .description("Total de trabajos de generación iniciados")
                .tag("module", "generator")
                .register(meterRegistry);

        this.generationJobsSuccessful = Counter.builder("generator_jobs_successful")
                .description("Trabajos de generación exitosos")
                .tag("module", "generator")
                .register(meterRegistry);

        this.generationJobsFailed = Counter.builder("generator_jobs_failed")
                .description("Trabajos de generación fallidos")
                .tag("module", "generator")
                .register(meterRegistry);

        this.generationTimeSeconds = Histogram.builder("generator_time_seconds")
                .description("Tiempo de generación en segundos")
                .tag("module", "generator")
                .buckets(10, 30, 60, 120, 300, 600)
                .register(meterRegistry);

        this.activeGenerations = Gauge.builder("generator_active_generations")
                .description("Generaciones activas actualmente")
                .tag("module", "generator")
                .register(meterRegistry);

        this.generatedLinesOfCode = Counter.builder("generator_lines_of_code_total")
                .description("Total de líneas de código generadas")
                .tag("module", "generator")
                .register(meterRegistry);
    }

    public void recordGenerationJobStarted() {
        generationJobsTotal.increment();
        activeGenerations.increment();
    }

    public void recordGenerationJobCompleted(long generationTimeSeconds, int linesOfCode) {
        this.generationTimeSeconds.record(generationTimeSeconds);
        this.generatedLinesOfCode.increment(linesOfCode);
        this.generationJobsSuccessful.increment();
        activeGenerations.decrement();
    }

    public void recordGenerationJobFailed() {
        this.generationJobsFailed.increment();
        activeGenerations.decrement();
    }
}
```

## Integración con Leka Server

### Servicio de Integración

```java
@Service
public class LekaServerIntegrationService {

    @Value("${generator.leka-server.base-url}")
    private String lekaServerUrl;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Ejecuta un paso de generación en Leka Server
     */
    public StepResult executeGenerationStep(GenerationStep step) {
        try {
            String url = lekaServerUrl + "/api/v1/generation/execute";

            GenerationStepRequest request = new GenerationStepRequest();
            request.setStepName(step.getStepName());
            request.setStepType(step.getStepType());
            request.setInputData(step.getInputData());
            request.setConfiguration(step.getConfiguration());

            ResponseEntity<StepResult> response = restTemplate.postForEntity(url, request, StepResult.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new LekaServerException("Error en Leka Server: " + response.getStatusCode());
            }

        } catch (Exception e) {
            throw new LekaServerException("Error comunicándose con Leka Server", e);
        }
    }

    /**
     * Obtiene el estado de un agente en Leka Server
     */
    public AgentStatus getAgentStatus(String agentName) {
        try {
            String url = lekaServerUrl + "/api/v1/agents/" + agentName + "/status";
            ResponseEntity<AgentStatus> response = restTemplate.getForEntity(url, AgentStatus.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new LekaServerException("Error obteniendo estado del agente");
            }

        } catch (Exception e) {
            throw new LekaServerException("Error comunicándose con Leka Server", e);
        }
    }
}
```

## Casos de Uso

### 1. Generación de Aplicación React

```json
{
  "jobName": "Portal Frontend React",
  "description": "Generación de aplicación React para portal de administración",
  "targetTechnology": "REACT",
  "sourceDesignId": 123,
  "sourceModule": "DESIGN_MODELING",
  "configuration": {
    "useTypeScript": true,
    "useTailwindCSS": true,
    "useReactQuery": true,
    "useZustand": true,
    "includeTesting": true,
    "includeStorybook": false
  }
}
```

### 2. Generación de API Spring Boot

```json
{
  "jobName": "User Management API",
  "description": "Generación de API REST para gestión de usuarios",
  "targetTechnology": "SPRING_BOOT",
  "sourceDesignId": 456,
  "sourceModule": "DESIGN_MODELING",
  "configuration": {
    "useSpringSecurity": true,
    "useJPA": true,
    "useSwagger": true,
    "includeTesting": true,
    "includeDocker": true,
    "databaseType": "POSTGRESQL"
  }
}
```

### 3. Generación de Base de Datos

```json
{
  "jobName": "E-commerce Database",
  "description": "Generación de esquema de base de datos para e-commerce",
  "targetTechnology": "POSTGRESQL",
  "sourceDesignId": 789,
  "sourceModule": "DESIGN_MODELING",
  "configuration": {
    "useMigrations": true,
    "includeIndexes": true,
    "includeTriggers": false,
    "includeStoredProcedures": true,
    "namingConvention": "SNAKE_CASE"
  }
}
```

## Conclusión

El **Generator Module** proporciona un sistema completo y automatizado para la generación de código en múltiples tecnologías, utilizando:

- **Agentes especializados** para diferentes tipos de generación
- **Plantillas inteligentes** con variables dinámicas
- **Configuraciones flexibles** por tecnología
- **Integración con Leka Server** para ejecución de agentes
- **Métricas y monitoreo** completo del proceso
- **API REST** para gestión y control
- **Sistema de colas** para generaciones asíncronas

El sistema está diseñado para ser extensible, permitiendo la adición de nuevas tecnologías, plantillas y agentes especializados según las necesidades del proyecto.
