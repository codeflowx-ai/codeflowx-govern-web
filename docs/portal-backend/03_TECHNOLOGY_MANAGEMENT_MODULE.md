# 03. Módulo de Technology Management - Portal Backend

## Descripción General

El módulo de Technology Management gestiona el catálogo de tecnologías, frameworks, librerías y arquitecturas utilizadas en la plataforma. Incluye funcionalidades de descubrimiento, evaluación, marketplace, gestión de repositorios, **integración con leka-server para webscraping**, **gestión de documentación RAG**, **prompts para entrenamiento y ejecución**, y **requisitos de infraestructura**. Se integra con sistemas de análisis de dependencias, evaluación de tecnologías, módulo de entrenamiento y servidor RAG.

## Características Principales

- **Gestión Completa de Tecnologías**: Catálogo con versiones, dependencias y metadatos
- **Technology Stacks**: Creación y gestión de stacks con requisitos de infraestructura
- **Integración Leka-Server**: Webscraping automático para actualización de información
- **Gestión RAG**: Documentación para servidor RAG y formación
- **Prompts de Entrenamiento**: Gestión de prompts para entrenamiento y ejecución
- **Requisitos de Infraestructura**: Especificaciones para despliegue y evaluación
- **Relación con Proyectos**: Mapeo de tecnologías utilizadas en proyectos
- **Integración Training**: Conexión con módulo de entrenamiento y datasets

## Entidades del Sistema

### 1. Technology (Tecnología)

```java
@Entity
@Table(name = "tch_technologies")
public class Technology {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private TechnologyCategory category;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TechnologyType type;

    @Column(name = "version")
    private String version;

    @Column(name = "latest_version")
    private String latestVersion;

    @Column(name = "homepage_url")
    private String homepageUrl;

    @Column(name = "repository_url")
    private String repositoryUrl;

    @Column(name = "documentation_url")
    private String documentationUrl;

    @Column(name = "license")
    private String license;

    @Column(name = "programming_language")
    private String programmingLanguage;

    @Column(name = "tags")
    private String tags; // JSON array of tags

    @Column(name = "metadata")
    private String metadata; // JSON metadata

    @Column(name = "rating")
    private Double rating;

    @Column(name = "download_count")
    private Long downloadCount = 0L;

    @Column(name = "star_count")
    private Long starCount = 0L;

    @Column(name = "fork_count")
    private Long forkCount = 0L;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "technology", cascade = CascadeType.ALL)
    private List<TechnologyVersion> versions = new ArrayList<>();

    @OneToMany(mappedBy = "technology", cascade = CascadeType.ALL)
    private List<TechnologyReview> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "technology", cascade = CascadeType.ALL)
    private List<TechnologyDependency> dependencies = new ArrayList<>();

    @OneToMany(mappedBy = "technology", cascade = CascadeType.ALL)
    private List<TechnologyDocumentation> documentation = new ArrayList<>();

    @OneToMany(mappedBy = "technology", cascade = CascadeType.ALL)
    private List<TechnologyPrompt> prompts = new ArrayList<>();

    @OneToMany(mappedBy = "technology", cascade = CascadeType.ALL)
    private List<TechnologyInfrastructure> infrastructure = new ArrayList<>();
}

public enum TechnologyCategory {
    MACHINE_LEARNING, DEEP_LEARNING, DATA_PROCESSING, WEB_FRAMEWORK,
    DATABASE, CLOUD_INFRASTRUCTURE, DEVOPS, MONITORING, SECURITY,
    FRONTEND, BACKEND, MOBILE, EMBEDDED, BLOCKCHAIN, IOT, AI_TOOLS,
    ML_FRAMEWORKS, DATA_VISUALIZATION, API_TOOLS, TESTING_TOOLS
}

public enum TechnologyType {
    FRAMEWORK, LIBRARY, TOOL, PLATFORM, SERVICE, LANGUAGE, DATABASE_ENGINE,
    OPERATING_SYSTEM, CONTAINER, ORCHESTRATOR, MONITORING_TOOL, SECURITY_TOOL,
    AI_MODEL, DATASET, API_GATEWAY, MESSAGE_QUEUE, CACHE_SYSTEM
}
```

### 2. Technology Version (Versión de Tecnología)

```java
@Entity
@Table(name = "tch_technology_versions")
public class TechnologyVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "technology_id", nullable = false)
    private Long technologyId;

    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "changelog")
    private String changelog;

    @Column(name = "download_url")
    private String downloadUrl;

    @Column(name = "checksum")
    private String checksum;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "is_stable")
    private Boolean isStable = false;

    @Column(name = "is_lts")
    private Boolean isLts = false; // Long Term Support

    @Column(name = "end_of_life")
    private LocalDate endOfLife;

    @Column(name = "security_vulnerabilities")
    private Integer securityVulnerabilities = 0;

    @Column(name = "compatibility_notes")
    private String compatibilityNotes; // JSON con notas de compatibilidad

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "technology_id", insertable = false, updatable = false)
    private Technology technology;
}
```

### 3. Technology Review (Reseña de Tecnología)

```java
@Entity
@Table(name = "tch_technology_reviews")
public class TechnologyReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "technology_id", nullable = false)
    private Long technologyId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "rating", nullable = false)
    private Integer rating; // 1-5 stars

    @Column(name = "review")
    private String review;

    @Column(name = "pros")
    private String pros; // JSON array

    @Column(name = "cons")
    private String cons; // JSON array

    @Column(name = "use_case")
    private String useCase;

    @Column(name = "experience_level")
    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;

    @Column(name = "project_size")
    @Enumerated(EnumType.STRING)
    private ProjectSize projectSize;

    @Column(name = "project_context")
    private String projectContext; // Contexto del proyecto donde se usó

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "technology_id", insertable = false, updatable = false)
    private Technology technology;
}

public enum ExperienceLevel {
    BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
}

public enum ProjectSize {
    SMALL, MEDIUM, LARGE, ENTERPRISE
}
```

### 4. Technology Dependency (Dependencia de Tecnología)

```java
@Entity
@Table(name = "tch_technology_dependencies")
public class TechnologyDependency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "technology_id", nullable = false)
    private Long technologyId;

    @Column(name = "dependency_technology_id", nullable = false)
    private Long dependencyTechnologyId;

    @Column(name = "dependency_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DependencyType dependencyType;

    @Column(name = "version_constraint")
    private String versionConstraint; // e.g., ">=1.0.0,<2.0.0"

    @Column(name = "is_optional")
    private Boolean isOptional = false;

    @Column(name = "dependency_notes")
    private String dependencyNotes; // Notas sobre la dependencia

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "technology_id", insertable = false, updatable = false)
    private Technology technology;

    @ManyToOne
    @JoinColumn(name = "dependency_technology_id", insertable = false, updatable = false)
    private Technology dependencyTechnology;
}

public enum DependencyType {
    REQUIRES, RECOMMENDS, SUGGESTS, ENHANCES, BREAKS, REPLACES, PROVIDES,
    CONFLICTS, OPTIONAL, DEVELOPMENT, TESTING, BUILD_TOOL
}
```

### 5. Technology Stack (Stack de Tecnologías)

```java
@Entity
@Table(name = "tch_technology_stacks")
public class TechnologyStack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private StackCategory category;

    @Column(name = "use_case")
    private String useCase;

    @Column(name = "complexity", nullable = false)
    @Enumerated(EnumType.STRING)
    private StackComplexity complexity;

    @Column(name = "maturity", nullable = false)
    @Enumerated(EnumType.STRING)
    private StackMaturity maturity;

    @Column(name = "tags")
    private String tags; // JSON array

    @Column(name = "is_public")
    private Boolean isPublic = true;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "usage_count")
    private Long usageCount = 0L;

    @Column(name = "deployment_notes")
    private String deploymentNotes; // Notas sobre despliegue

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "stack", cascade = CascadeType.ALL)
    private List<StackTechnology> technologies = new ArrayList<>();

    @OneToMany(mappedBy = "stack", cascade = CascadeType.ALL)
    private List<StackInfrastructure> infrastructure = new ArrayList<>();

    @OneToMany(mappedBy = "stack", cascade = CascadeType.ALL)
    private List<StackPrompt> prompts = new ArrayList<>();

    @OneToMany(mappedBy = "stack", cascade = CascadeType.ALL)
    private List<StackDocumentation> documentation = new ArrayList<>();
}

public enum StackCategory {
    FULL_STACK, FRONTEND, BACKEND, DATA_SCIENCE, MACHINE_LEARNING,
    DEVOPS, MOBILE, EMBEDDED, CLOUD_NATIVE, MICROSERVICES, AI_PIPELINE,
    DATA_PIPELINE, MONITORING_STACK, SECURITY_STACK, TESTING_STACK
}

public enum StackComplexity {
    SIMPLE, MODERATE, COMPLEX, ENTERPRISE
}

public enum StackMaturity {
    EXPERIMENTAL, BETA, STABLE, PRODUCTION_READY, ENTERPRISE_GRADE
}
```

### 6. Stack Technology (Tecnología en Stack)

```java
@Entity
@Table(name = "tch_stack_technologies")
public class StackTechnology {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stack_id", nullable = false)
    private Long stackId;

    @Column(name = "technology_id", nullable = false)
    private Long technologyId;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private TechnologyRole role;

    @Column(name = "version_constraint")
    private String versionConstraint;

    @Column(name = "order_index")
    private Integer orderIndex = 0;

    @Column(name = "is_required")
    private Boolean isRequired = true;

    @Column(name = "configuration")
    private String configuration; // JSON con configuración específica

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "stack_id", insertable = false, updatable = false)
    private TechnologyStack stack;

    @ManyToOne
    @JoinColumn(name = "technology_id", insertable = false, updatable = false)
    private Technology technology;
}

public enum TechnologyRole {
    CORE, SUPPORTING, OPTIONAL, ALTERNATIVE, DEPRECATED, MONITORING,
    SECURITY, TESTING, BUILD_TOOL, DEPLOYMENT_TOOL, BACKUP_TOOL
}
```

### 7. Technology Evaluation (Evaluación de Tecnología)

```java
@Entity
@Table(name = "tch_technology_evaluations")
public class TechnologyEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "technology_id", nullable = false)
    private Long technologyId;

    @Column(name = "evaluator_id", nullable = false)
    private Long evaluatorId;

    @Column(name = "evaluation_date", nullable = false)
    private LocalDate evaluationDate;

    @Column(name = "overall_score")
    private Double overallScore;

    @Column(name = "performance_score")
    private Double performanceScore;

    @Column(name = "security_score")
    private Double securityScore;

    @Column(name = "maintainability_score")
    private Double maintainabilityScore;

    @Column(name = "community_score")
    private Double communityScore;

    @Column(name = "documentation_score")
    private Double documentationScore;

    @Column(name = "maturity_score")
    private Double maturityScore;

    @Column(name = "learning_curve_score")
    private Double learningCurveScore; // Facilidad de aprendizaje

    @Column(name = "ecosystem_score")
    private Double ecosystemScore; // Calidad del ecosistema

    @Column(name = "recommendation", nullable = false)
    @Enumerated(EnumType.STRING)
    private Recommendation recommendation;

    @Column(name = "notes")
    private String notes;

    @Column(name = "evaluation_context")
    private String evaluationContext; // Contexto de la evaluación

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "technology_id", insertable = false, updatable = false)
    private Technology technology;
}

public enum Recommendation {
    STRONGLY_RECOMMEND, RECOMMEND, CONSIDER, AVOID, NOT_EVALUATED
}
```

### 8. Technology Documentation (Documentación de Tecnología)

```java
@Entity
@Table(name = "tch_technology_documentation")
public class TechnologyDocumentation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "technology_id", nullable = false)
    private Long technologyId;

    @Column(name = "documentation_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DocumentationType documentationType;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content; // Contenido de la documentación

    @Column(name = "file_url")
    private String fileUrl; // URL del archivo si está almacenado externamente

    @Column(name = "file_type")
    private String fileType; // PDF, DOCX, MD, HTML, etc.

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "language")
    private String language = "en"; // Idioma de la documentación

    @Column(name = "version")
    private String version; // Versión de la documentación

    @Column(name = "is_rag_ready")
    private Boolean isRagReady = false; // Preparado para servidor RAG

    @Column(name = "rag_metadata")
    private String ragMetadata; // JSON con metadatos para RAG

    @Column(name = "training_relevance")
    private Integer trainingRelevance; // 1-10 relevancia para formación

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "technology_id", insertable = false, updatable = false)
    private Technology technology;
}

public enum DocumentationType {
    USER_GUIDE, API_REFERENCE, TUTORIAL, BEST_PRACTICES, EXAMPLES,
    TROUBLESHOOTING, ARCHITECTURE, DEPLOYMENT, SECURITY, PERFORMANCE,
    INTEGRATION, MIGRATION, CHANGELOG, CONTRIBUTING, LICENSE
}
```

### 9. Technology Prompt (Prompt de Tecnología)

```java
@Entity
@Table(name = "tch_technology_prompts")
public class TechnologyPrompt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "technology_id", nullable = false)
    private Long technologyId;

    @Column(name = "prompt_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PromptType promptType;

    @Column(name = "prompt_name", nullable = false)
    private String promptName;

    @Column(name = "prompt_content", nullable = false)
    private String promptContent; // Contenido del prompt

    @Column(name = "prompt_description")
    private String promptDescription;

    @Column(name = "prompt_category")
    private String promptCategory; // Categoría del prompt

    @Column(name = "prompt_tags")
    private String promptTags; // JSON array de tags

    @Column(name = "input_variables")
    private String inputVariables; // JSON con variables de entrada

    @Column(name = "output_format")
    private String outputFormat; // Formato esperado de salida

    @Column(name = "usage_examples")
    private String usageExamples; // JSON con ejemplos de uso

    @Column(name = "is_training_prompt")
    private Boolean isTrainingPrompt = false; // Para entrenamiento

    @Column(name = "is_execution_prompt")
    private Boolean isExecutionPrompt = false; // Para ejecución

    @Column(name = "prompt_version")
    private String promptVersion = "1.0.0";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "technology_id", insertable = false, updatable = false)
    private Technology technology;
}

public enum PromptType {
    TRAINING, EXECUTION, TESTING, DEBUGGING, OPTIMIZATION, DOCUMENTATION,
    CODE_GENERATION, ANALYSIS, DEPLOYMENT, MONITORING, SECURITY
}
```

### 10. Technology Infrastructure (Infraestructura de Tecnología)

```java
@Entity
@Table(name = "tch_technology_infrastructure")
public class TechnologyInfrastructure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "technology_id", nullable = false)
    private Long technologyId;

    @Column(name = "infrastructure_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private InfrastructureType infrastructureType;

    @Column(name = "requirements")
    private String requirements; // JSON con requisitos de infraestructura

    @Column(name = "min_cpu_cores")
    private Integer minCpuCores;

    @Column(name = "recommended_cpu_cores")
    private Integer recommendedCpuCores;

    @Column(name = "min_ram_gb")
    private Integer minRamGB;

    @Column(name = "recommended_ram_gb")
    private Integer recommendedRamGB;

    @Column(name = "min_storage_gb")
    private Integer minStorageGB;

    @Column(name = "recommended_storage_gb")
    private Integer recommendedStorageGB;

    @Column(name = "gpu_requirements")
    private String gpuRequirements; // JSON con requisitos de GPU

    @Column(name = "network_requirements")
    private String networkRequirements; // JSON con requisitos de red

    @Column(name = "operating_system")
    private String operatingSystem; // Sistemas operativos soportados

    @Column(name = "container_support")
    private Boolean containerSupport = false;

    @Column(name = "cloud_providers")
    private String cloudProviders; // JSON con proveedores de nube soportados

    @Column(name = "deployment_notes")
    private String deploymentNotes; // Notas sobre despliegue

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "technology_id", insertable = false, updatable = false)
    private Technology technology;
}

public enum InfrastructureType {
    COMPUTE, STORAGE, NETWORK, SECURITY, MONITORING, BACKUP, DISASTER_RECOVERY,
    LOAD_BALANCING, CACHING, MESSAGE_QUEUE, DATABASE, CONTAINER, ORCHESTRATION
}
```

### 11. Stack Infrastructure (Infraestructura del Stack)

```java
@Entity
@Table(name = "tch_stack_infrastructure")
public class StackInfrastructure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stack_id", nullable = false)
    private Long stackId;

    @Column(name = "infrastructure_component", nullable = false)
    private String infrastructureComponent; // Nombre del componente

    @Column(name = "component_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private InfrastructureType componentType;

    @Column(name = "requirements")
    private String requirements; // JSON con requisitos específicos

    @Column(name = "deployment_order")
    private Integer deploymentOrder; // Orden de despliegue

    @Column(name = "is_required")
    private Boolean isRequired = true;

    @Column(name = "scaling_rules")
    private String scalingRules; // JSON con reglas de escalado

    @Column(name = "monitoring_requirements")
    private String monitoringRequirements; // JSON con requisitos de monitoreo

    @Column(name = "security_requirements")
    private String securityRequirements; // JSON con requisitos de seguridad

    @Column(name = "backup_requirements")
    private String backupRequirements; // JSON con requisitos de backup

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "stack_id", insertable = false, updatable = false)
    private TechnologyStack stack;
}
```

### 12. Stack Prompt (Prompt del Stack)

```java
@Entity
@Table(name = "tch_stack_prompts")
public class StackPrompt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stack_id", nullable = false)
    private Long stackId;

    @Column(name = "prompt_name", nullable = false)
    private String promptName;

    @Column(name = "prompt_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PromptType promptType;

    @Column(name = "prompt_content", nullable = false)
    private String promptContent;

    @Column(name = "prompt_description")
    private String promptDescription;

    @Column(name = "target_technology_id")
    private Long targetTechnologyId; // Tecnología específica del stack

    @Column(name = "execution_order")
    private Integer executionOrder; // Orden de ejecución

    @Column(name = "input_mapping")
    private String inputMapping; // JSON con mapeo de entradas

    @Column(name = "output_mapping")
    private String outputMapping; // JSON con mapeo de salidas

    @Column(name = "validation_rules")
    private String validationRules; // JSON con reglas de validación

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "stack_id", insertable = false, updatable = false)
    private TechnologyStack stack;

    @ManyToOne
    @JoinColumn(name = "target_technology_id", insertable = false, updatable = false)
    private Technology targetTechnology;
}
```

### 13. Stack Documentation (Documentación del Stack)

```java
@Entity
@Table(name = "tch_stack_documentation")
public class StackDocumentation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stack_id", nullable = false)
    private Long stackId;

    @Column(name = "documentation_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DocumentationType documentationType;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "is_rag_ready")
    private Boolean isRagReady = false;

    @Column(name = "rag_metadata")
    private String ragMetadata;

    @Column(name = "training_relevance")
    private Integer trainingRelevance;

    @Column(name = "deployment_section")
    private String deploymentSection; // Sección específica de despliegue

    @Column(name = "troubleshooting_section")
    private String troubleshootingSection; // Sección de solución de problemas

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "stack_id", insertable = false, updatable = false)
    private TechnologyStack stack;
}
```

### 14. Web Scraping Job (Trabajo de Web Scraping)

```java
@Entity
@Table(name = "tch_webscraping_jobs")
public class WebScrapingJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "technology_id")
    private Long technologyId;

    @Column(name = "source_url", nullable = false)
    private String sourceUrl;

    @Column(name = "job_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ScrapingJobType jobType;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ScrapingJobStatus status = ScrapingJobStatus.PENDING;

    @Column(name = "configuration")
    private String configuration; // JSON scraping config

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "result")
    private String result; // JSON scraping result

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "retry_count")
    private Integer retryCount = 0;

    @Column(name = "lek_server_job_id")
    private String lekServerJobId; // ID del trabajo en leka-server

    @Column(name = "lek_server_status")
    private String lekServerStatus; // Estado del trabajo en leka-server

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "technology_id", insertable = false, updatable = false)
    private Technology technology;
}

public enum ScrapingJobType {
    VERSION_UPDATE, SECURITY_VULNERABILITY, DOWNLOAD_STATS, COMMUNITY_METRICS,
    DOCUMENTATION_UPDATE, LICENSE_UPDATE, DEPENDENCY_UPDATE, PRICING_UPDATE,
    COMPATIBILITY_UPDATE, PERFORMANCE_METRICS
}

public enum ScrapingJobStatus {
    PENDING, RUNNING, COMPLETED, FAILED, CANCELLED, RETRYING, QUEUED_IN_LEKA
}
```

### 15. Project Technology Usage (Uso de Tecnología en Proyectos)

```java
@Entity
@Table(name = "tch_project_technology_usage")
public class ProjectTechnologyUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false)
    private Long projectId; // Referencia a prj_projects.id

    @Column(name = "technology_id", nullable = false)
    private Long technologyId;

    @Column(name = "usage_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UsageType usageType;

    @Column(name = "version_used")
    private String versionUsed;

    @Column(name = "usage_start_date")
    private LocalDate usageStartDate;

    @Column(name = "usage_end_date")
    private LocalDate usageEndDate;

    @Column(name = "usage_intensity")
    @Enumerated(EnumType.STRING)
    private UsageIntensity usageIntensity;

    @Column(name = "usage_notes")
    private String usageNotes;

    @Column(name = "performance_rating")
    private Integer performanceRating; // 1-5 rating

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "technology_id", insertable = false, updatable = false)
    private Technology technology;
}

public enum UsageType {
    PRIMARY, SECONDARY, DEVELOPMENT, TESTING, MONITORING, SECURITY, BACKUP
}

public enum UsageIntensity {
    LOW, MEDIUM, HIGH, CRITICAL
}
```

## API Endpoints

### Technology Management

```plaintext
GET    /api/v1/technologies
GET    /api/v1/technologies/{id}
POST   /api/v1/technologies
PUT    /api/v1/technologies/{id}
DELETE /api/v1/technologies/{id}
```

### Technology Version Management

```plaintext
GET    /api/v1/technologies/{id}/versions
GET    /api/v1/technologies/{id}/versions/{versionId}
POST   /api/v1/technologies/{id}/versions
PUT    /api/v1/technologies/{id}/versions/{versionId}
DELETE /api/v1/technologies/{id}/versions/{versionId}
```

### Technology Review Management

```plaintext
GET    /api/v1/technologies/{id}/reviews
GET    /api/v1/technologies/{id}/reviews/{reviewId}
POST   /api/v1/technologies/{id}/reviews
PUT    /api/v1/technologies/{id}/reviews/{reviewId}
DELETE /api/v1/technologies/{id}/reviews/{reviewId}
```

### Technology Dependency Management

```plaintext
GET    /api/v1/technologies/{id}/dependencies
GET    /api/v1/technologies/{id}/dependencies/{dependencyId}
POST   /api/v1/technologies/{id}/dependencies
PUT    /api/v1/technologies/{id}/dependencies/{dependencyId}
DELETE /api/v1/technologies/{id}/dependencies/{dependencyId}
```

### Technology Stack Management

```plaintext
GET    /api/v1/stacks
GET    /api/v1/stacks/{id}
POST   /api/v1/stacks
PUT    /api/v1/stacks/{id}
DELETE /api/v1/stacks/{id}
```

### Stack Technology Management

```plaintext
GET    /api/v1/stacks/{id}/technologies
GET    /api/v1/stacks/{id}/technologies/{techId}
POST   /api/v1/stacks/{id}/technologies
PUT    /api/v1/stacks/{id}/technologies/{techId}
DELETE /api/v1/stacks/{id}/technologies/{techId}
```

### Technology Evaluation Management

```plaintext
GET    /api/v1/technologies/{id}/evaluations
GET    /api/v1/technologies/{id}/evaluations/{evaluationId}
POST   /api/v1/technologies/{id}/evaluations
PUT    /api/v1/technologies/{id}/evaluations/{evaluationId}
DELETE /api/v1/technologies/{id}/evaluations/{evaluationId}
```

### Technology Documentation Management

```plaintext
GET    /api/v1/technologies/{id}/documentation
GET    /api/v1/technologies/{id}/documentation/{docId}
POST   /api/v1/technologies/{id}/documentation
PUT    /api/v1/technologies/{id}/documentation/{docId}
DELETE /api/v1/technologies/{id}/documentation/{docId}
```

### Technology Prompt Management

```plaintext
GET    /api/v1/technologies/{id}/prompts
GET    /api/v1/technologies/{id}/prompts/{promptId}
POST   /api/v1/technologies/{id}/prompts
PUT    /api/v1/technologies/{id}/prompts/{promptId}
DELETE /api/v1/technologies/{id}/prompts/{promptId}
```

### Technology Infrastructure Management

```plaintext
GET    /api/v1/technologies/{id}/infrastructure
GET    /api/v1/technologies/{id}/infrastructure/{infraId}
POST   /api/v1/technologies/{id}/infrastructure
PUT    /api/v1/technologies/{id}/infrastructure/{infraId}
DELETE /api/v1/technologies/{id}/infrastructure/{infraId}
```

### Stack Infrastructure Management

```plaintext
GET    /api/v1/stacks/{id}/infrastructure
GET    /api/v1/stacks/{id}/infrastructure/{infraId}
POST   /api/v1/stacks/{id}/infrastructure
PUT    /api/v1/stacks/{id}/infrastructure/{infraId}
DELETE /api/v1/stacks/{id}/infrastructure/{infraId}
```

### Stack Prompt Management

```plaintext
GET    /api/v1/stacks/{id}/prompts
GET    /api/v1/stacks/{id}/prompts/{promptId}
POST   /api/v1/stacks/{id}/prompts
PUT    /api/v1/stacks/{id}/prompts/{promptId}
DELETE /api/v1/stacks/{id}/prompts/{promptId}
```

### Stack Documentation Management

```plaintext
GET    /api/v1/stacks/{id}/documentation
GET    /api/v1/stacks/{id}/documentation/{docId}
POST   /api/v1/stacks/{id}/documentation
PUT    /api/v1/stacks/{id}/documentation/{docId}
DELETE /api/v1/stacks/{id}/documentation/{docId}
```

### Web Scraping Management

```plaintext
GET    /api/v1/webscraping/jobs
GET    /api/v1/webscraping/jobs/{jobId}
POST   /api/v1/webscraping/jobs
PUT    /api/v1/webscraping/jobs/{jobId}
DELETE /api/v1/webscraping/jobs/{jobId}
POST   /api/v1/webscraping/jobs/{jobId}/retry
POST   /api/v1/webscraping/jobs/{jobId}/cancel
```

### Project Technology Usage Management

```plaintext
GET    /api/v1/projects/{projectId}/technologies
GET    /api/v1/projects/{projectId}/technologies/{techId}
POST   /api/v1/projects/{projectId}/technologies
PUT    /api/v1/projects/{projectId}/technologies/{techId}
DELETE /api/v1/projects/{projectId}/technologies/{techId}
```

### Technology Discovery and Search

```plaintext
GET    /api/v1/technologies/search
GET    /api/v1/technologies/recommendations
GET    /api/v1/technologies/trending
GET    /api/v1/technologies/categories/{category}
GET    /api/v1/technologies/types/{type}
```

### Stack Discovery and Search

```plaintext
GET    /api/v1/stacks/search
GET    /api/v1/stacks/recommendations
GET    /api/v1/stacks/categories/{category}
GET    /api/v1/stacks/complexity/{complexity}
GET    /api/v1/stacks/maturity/{maturity}
```

### RAG Integration

```plaintext
GET    /api/v1/rag/technologies/{id}/documentation
GET    /api/v1/rag/stacks/{id}/documentation
POST   /api/v1/rag/technologies/{id}/documentation/process
POST   /api/v1/rag/stacks/{id}/documentation/process
```

### Training Integration

```plaintext
GET    /api/v1/training/technologies/{id}/prompts
GET    /api/v1/training/stacks/{id}/prompts
GET    /api/v1/training/technologies/{id}/documentation
GET    /api/v1/training/stacks/{id}/documentation
```

## Scripts de Base de Datos

```plaintext
-- Tabla de tecnologías
CREATE TABLE tch_technologies (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    version VARCHAR(50),
    latest_version VARCHAR(50),
    homepage_url VARCHAR(500),
    repository_url VARCHAR(500),
    documentation_url VARCHAR(500),
    license VARCHAR(100),
    programming_language VARCHAR(100),
    tags TEXT,
    metadata TEXT,
    rating DECIMAL(3,2),
    download_count BIGINT DEFAULT 0,
    star_count BIGINT DEFAULT 0,
    fork_count BIGINT DEFAULT 0,
    last_updated TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de versiones de tecnologías
CREATE TABLE tch_technology_versions (
    id BIGSERIAL PRIMARY KEY,
    technology_id BIGINT REFERENCES tch_technologies(id) ON DELETE CASCADE,
    version VARCHAR(50) NOT NULL,
    release_date DATE,
    changelog TEXT,
    download_url VARCHAR(500),
    checksum VARCHAR(64),
    file_size_bytes BIGINT,
    is_stable BOOLEAN DEFAULT false,
    is_lts BOOLEAN DEFAULT false,
    end_of_life DATE,
    security_vulnerabilities INTEGER DEFAULT 0,
    compatibility_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de reseñas de tecnologías
CREATE TABLE tch_technology_reviews (
    id BIGSERIAL PRIMARY KEY,
    technology_id BIGINT REFERENCES tch_technologies(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    rating INTEGER CHECK (rating &gt;= 1 AND rating &lt;= 5),
    review TEXT,
    pros TEXT,
    cons TEXT,
    use_case TEXT,
    experience_level VARCHAR(50),
    project_size VARCHAR(50),
    project_context TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de dependencias de tecnologías
CREATE TABLE tch_technology_dependencies (
    id BIGSERIAL PRIMARY KEY,
    technology_id BIGINT REFERENCES tch_technologies(id) ON DELETE CASCADE,
    dependency_technology_id BIGINT REFERENCES tch_technologies(id) ON DELETE CASCADE,
    dependency_type VARCHAR(50) NOT NULL,
    version_constraint VARCHAR(100),
    is_optional BOOLEAN DEFAULT false,
    dependency_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de stacks de tecnologías
CREATE TABLE tch_technology_stacks (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    category VARCHAR(50) NOT NULL,
    use_case TEXT,
    complexity VARCHAR(50),
    maturity VARCHAR(50),
    tags TEXT,
    is_public BOOLEAN DEFAULT true,
    rating DECIMAL(3,2),
    usage_count BIGINT DEFAULT 0,
    deployment_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de tecnologías en stacks
CREATE TABLE tch_stack_technologies (
    id BIGSERIAL PRIMARY KEY,
    stack_id BIGINT REFERENCES tch_technology_stacks(id) ON DELETE CASCADE,
    technology_id BIGINT REFERENCES tch_technologies(id) ON DELETE CASCADE,
    role VARCHAR(50) NOT NULL,
    version_constraint VARCHAR(100),
    order_index INTEGER DEFAULT 0,
    is_required BOOLEAN DEFAULT true,
    configuration TEXT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de evaluaciones de tecnologías
CREATE TABLE tch_technology_evaluations (
    id BIGSERIAL PRIMARY KEY,
    technology_id BIGINT REFERENCES tch_technologies(id) ON DELETE CASCADE,
    evaluator_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    evaluation_date DATE NOT NULL,
    overall_score DECIMAL(3,2),
    performance_score DECIMAL(3,2),
    security_score DECIMAL(3,2),
    maintainability_score DECIMAL(3,2),
    community_score DECIMAL(3,2),
    documentation_score DECIMAL(3,2),
    maturity_score DECIMAL(3,2),
    learning_curve_score DECIMAL(3,2),
    ecosystem_score DECIMAL(3,2),
    recommendation VARCHAR(50),
    notes TEXT,
    evaluation_context TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de trabajos de web scraping
CREATE TABLE tch_webscraping_jobs (
    id BIGSERIAL PRIMARY KEY,
    technology_id BIGINT REFERENCES tch_technologies(id) ON DELETE CASCADE,
    source_url VARCHAR(500) NOT NULL,
    job_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    configuration TEXT,
    scheduled_at TIMESTAMP,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    result TEXT,
    error_message TEXT,
    retry_count INTEGER DEFAULT 0,
    lek_server_job_id VARCHAR(255),
    lek_server_status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de documentación de tecnologías
CREATE TABLE tch_technology_documentation (
    id BIGSERIAL PRIMARY KEY,
    technology_id BIGINT REFERENCES tch_technologies(id) ON DELETE CASCADE,
    documentation_type VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    file_url VARCHAR(500),
    file_type VARCHAR(50),
    file_size_bytes BIGINT,
    language VARCHAR(10),
    version VARCHAR(50),
    is_rag_ready BOOLEAN DEFAULT false,
    rag_metadata TEXT,
    training_relevance INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de prompts de tecnologías
CREATE TABLE tch_technology_prompts (
    id BIGSERIAL PRIMARY KEY,
    technology_id BIGINT REFERENCES tch_technologies(id) ON DELETE CASCADE,
    prompt_type VARCHAR(50) NOT NULL,
    prompt_name VARCHAR(255) NOT NULL,
    prompt_content TEXT NOT NULL,
    prompt_description TEXT,
    prompt_category VARCHAR(100),
    prompt_tags TEXT,
    input_variables TEXT,
    output_format TEXT,
    usage_examples TEXT,
    is_training_prompt BOOLEAN DEFAULT false,
    is_execution_prompt BOOLEAN DEFAULT false,
    prompt_version VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de infraestructura de tecnologías
CREATE TABLE tch_technology_infrastructure (
    id BIGSERIAL PRIMARY KEY,
    technology_id BIGINT REFERENCES tch_technologies(id) ON DELETE CASCADE,
    infrastructure_type VARCHAR(100) NOT NULL,
    requirements TEXT,
    min_cpu_cores INTEGER,
    recommended_cpu_cores INTEGER,
    min_ram_gb INTEGER,
    recommended_ram_gb INTEGER,
    min_storage_gb INTEGER,
    recommended_storage_gb INTEGER,
    gpu_requirements TEXT,
    network_requirements TEXT,
    operating_system VARCHAR(255),
    container_support BOOLEAN DEFAULT false,
    cloud_providers TEXT,
    deployment_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de stacks de infraestructura
CREATE TABLE tch_stack_infrastructure (
    id BIGSERIAL PRIMARY KEY,
    stack_id BIGINT REFERENCES tch_technology_stacks(id) ON DELETE CASCADE,
    infrastructure_component VARCHAR(255) NOT NULL,
    component_type VARCHAR(100) NOT NULL,
    requirements TEXT,
    deployment_order INTEGER,
    is_required BOOLEAN DEFAULT true,
    scaling_rules TEXT,
    monitoring_requirements TEXT,
    security_requirements TEXT,
    backup_requirements TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de documentación de stacks
CREATE TABLE tch_stack_documentation (
    id BIGSERIAL PRIMARY KEY,
    stack_id BIGINT REFERENCES tch_technology_stacks(id) ON DELETE CASCADE,
    documentation_type VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    file_url VARCHAR(500),
    file_type VARCHAR(50),
    is_rag_ready BOOLEAN DEFAULT false,
    rag_metadata TEXT,
    training_relevance INTEGER CHECK (training_relevance >= 1 AND training_relevance <= 10),
    deployment_section TEXT,
    troubleshooting_section TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de prompts de stacks
CREATE TABLE tch_stack_prompts (
    id BIGSERIAL PRIMARY KEY,
    stack_id BIGINT REFERENCES tch_technology_stacks(id) ON DELETE CASCADE,
    prompt_name VARCHAR(255) NOT NULL,
    prompt_type VARCHAR(50) NOT NULL,
    prompt_content TEXT NOT NULL,
    prompt_description TEXT,
    target_technology_id BIGINT REFERENCES tch_technologies(id),
    execution_order INTEGER,
    input_mapping TEXT,
    output_mapping TEXT,
    validation_rules TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de uso de tecnologías en proyectos
CREATE TABLE tch_project_technology_usage (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT REFERENCES prj_projects(id) ON DELETE CASCADE,
    technology_id BIGINT REFERENCES tch_technologies(id) ON DELETE CASCADE,
    usage_type VARCHAR(50) NOT NULL,
    version_used VARCHAR(50),
    usage_start_date DATE,
    usage_end_date DATE,
    usage_intensity VARCHAR(50),
    usage_notes TEXT,
    performance_rating INTEGER CHECK (performance_rating &gt;= 1 AND performance_rating &lt;= 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para optimización
CREATE INDEX idx_technologies_category ON tch_technologies(category);
CREATE INDEX idx_technologies_type ON tch_technologies(type);
CREATE INDEX idx_technologies_programming_language ON tch_technologies(programming_language);
CREATE INDEX idx_technologies_rating ON tch_technologies(rating);
CREATE INDEX idx_technologies_created_at ON tch_technologies(created_at);
CREATE INDEX idx_technology_versions_technology_id ON tch_technology_versions(technology_id);
CREATE INDEX idx_technology_versions_version ON tch_technology_versions(version);
CREATE INDEX idx_technology_reviews_technology_id ON tch_technology_reviews(technology_id);
CREATE INDEX idx_technology_reviews_user_id ON tch_technology_reviews(user_id);
CREATE INDEX idx_technology_reviews_rating ON tch_technology_reviews(rating);
CREATE INDEX idx_technology_dependencies_technology_id ON tch_technology_dependencies(technology_id);
CREATE INDEX idx_technology_dependencies_dependency_id ON tch_technology_dependencies(dependency_technology_id);
CREATE INDEX idx_technology_stacks_category ON tch_technology_stacks(category);
CREATE INDEX idx_technology_stacks_user_id ON tch_technology_stacks(user_id);
CREATE INDEX idx_technology_stacks_rating ON tch_technology_stacks(rating);
CREATE INDEX idx_stack_technologies_stack_id ON tch_stack_technologies(stack_id);
CREATE INDEX idx_stack_technologies_technology_id ON tch_stack_technologies(technology_id);
CREATE INDEX idx_technology_evaluations_technology_id ON tch_technology_evaluations(technology_id);
CREATE INDEX idx_technology_evaluations_evaluator_id ON tch_technology_evaluations(evaluator_id);
CREATE INDEX idx_webscraping_jobs_technology_id ON tch_webscraping_jobs(technology_id);
CREATE INDEX idx_webscraping_jobs_status ON tch_webscraping_jobs(status);
CREATE INDEX idx_webscraping_jobs_scheduled_at ON tch_webscraping_jobs(scheduled_at);
CREATE INDEX idx_technology_documentation_documentation_type ON tch_technology_documentation(documentation_type);
CREATE INDEX idx_technology_documentation_language ON tch_technology_documentation(language);
CREATE INDEX idx_technology_documentation_version ON tch_technology_documentation(version);
CREATE INDEX idx_technology_documentation_is_rag_ready ON tch_technology_documentation(is_rag_ready);
CREATE INDEX idx_technology_prompts_prompt_type ON tch_technology_prompts(prompt_type);
CREATE INDEX idx_technology_prompts_is_training_prompt ON tch_technology_prompts(is_training_prompt);
CREATE INDEX idx_technology_prompts_is_execution_prompt ON tch_technology_prompts(is_execution_prompt);
CREATE INDEX idx_technology_infrastructure_infrastructure_type ON tch_technology_infrastructure(infrastructure_type);
CREATE INDEX idx_stack_infrastructure_infrastructure_component ON tch_stack_infrastructure(infrastructure_component);
CREATE INDEX idx_stack_infrastructure_component_type ON tch_stack_infrastructure(component_type);
CREATE INDEX idx_stack_prompts_prompt_name ON tch_stack_prompts(prompt_name);
CREATE INDEX idx_stack_prompts_prompt_type ON tch_stack_prompts(prompt_type);
CREATE INDEX idx_stack_prompts_target_technology_id ON tch_stack_prompts(target_technology_id);
CREATE INDEX idx_project_technology_usage_usage_type ON tch_project_technology_usage(usage_type);
CREATE INDEX idx_project_technology_usage_usage_intensity ON tch_project_technology_usage(usage_intensity);
```

## Servicios del Sistema

### 1. Technology Service

```java
@Service
@Transactional
public class TechnologyService {

    @Autowired
    private TechnologyRepository technologyRepository;

    @Autowired
    private TechnologyVersionRepository versionRepository;

    @Autowired
    private TechnologyReviewRepository reviewRepository;

    @Autowired
    private TechnologyDependencyRepository dependencyRepository;

    @Autowired
    private TechnologyDocumentationRepository documentationRepository;

    @Autowired
    private TechnologyPromptRepository promptRepository;

    @Autowired
    private TechnologyInfrastructureRepository infrastructureRepository;

    @Autowired
    private ProjectTechnologyUsageRepository projectUsageRepository;

    @Autowired
    private AuditService auditService;

    @Autowired
    private TechnologyManagementMetrics metrics;

    public Technology createTechnology(Technology technology) {
        Timer.Sample timer = metrics.startTechnologyCreationTimer();
        try {
            technology.setCreatedAt(LocalDateTime.now());
            technology.setUpdatedAt(LocalDateTime.now());
            Technology saved = technologyRepository.save(technology);

            auditService.logAction("TECHNOLOGY_CREATED", "Technology created: " + technology.getName());
            metrics.incrementTechnologiesCreated();

            return saved;
        } finally {
            timer.stop(metrics.getTechnologyCreationTimer());
        }
    }

    public Technology updateTechnology(Long id, Technology technology) {
        Technology existing = technologyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Technology not found"));

        existing.setName(technology.getName());
        existing.setDescription(technology.getDescription());
        existing.setCategory(technology.getCategory());
        existing.setType(technology.getType());
        existing.setLatestVersion(technology.getLatestVersion());
        existing.setHomepageUrl(technology.getHomepageUrl());
        existing.setRepositoryUrl(technology.getRepositoryUrl());
        existing.setDocumentationUrl(technology.getDocumentationUrl());
        existing.setLicense(technology.getLicense());
        existing.setProgrammingLanguage(technology.getProgrammingLanguage());
        existing.setTags(technology.getTags());
        existing.setMetadata(technology.getMetadata());
        existing.setUpdatedAt(LocalDateTime.now());

        Technology updated = technologyRepository.save(existing);

        auditService.logAction("TECHNOLOGY_UPDATED", "Technology updated: " + existing.getName());

        return updated;
    }

    public void deleteTechnology(Long id) {
        Technology technology = technologyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Technology not found"));

        technologyRepository.delete(technology);

        auditService.logAction("TECHNOLOGY_DELETED", "Technology deleted: " + technology.getName());
    }

    public List<Technology> searchTechnologies(String query, TechnologyCategory category, TechnologyType type) {
        return technologyRepository.searchTechnologies(query, category, type);
    }

    public List<Technology> getRecommendations(Long userId, String useCase) {
        return technologyRepository.getRecommendations(userId, useCase);
    }

    public List<Technology> getTrendingTechnologies() {
        return technologyRepository.getTrendingTechnologies();
    }

    public List<ProjectTechnologyUsage> getProjectTechnologyUsage(Long projectId) {
        return projectUsageRepository.findByProjectId(projectId);
    }

    public List<Technology> getTechnologiesByProject(Long projectId) {
        return technologyRepository.findTechnologiesByProject(projectId);
    }
}
```

### 2. Technology Stack Service

```java
@Service
@Transactional
public class TechnologyStackService {

    @Autowired
    private TechnologyStackRepository stackRepository;

    @Autowired
    private StackTechnologyRepository stackTechnologyRepository;

    @Autowired
    private StackInfrastructureRepository stackInfrastructureRepository;

    @Autowired
    private StackPromptRepository stackPromptRepository;

    @Autowired
    private StackDocumentationRepository stackDocumentationRepository;

    @Autowired
    private AuditService auditService;

    @Autowired
    private TechnologyManagementMetrics metrics;

    public TechnologyStack createStack(TechnologyStack stack) {
        Timer.Sample timer = metrics.startStackCreationTimer();
        try {
            stack.setCreatedAt(LocalDateTime.now());
            stack.setUpdatedAt(LocalDateTime.now());
            TechnologyStack saved = stackRepository.save(stack);

            auditService.logAction("STACK_CREATED", "Technology stack created: " + stack.getName());
            metrics.incrementStacksCreated();

            return saved;
        } finally {
            timer.stop(metrics.getStackCreationTimer());
        }
    }

    public TechnologyStack updateStack(Long id, TechnologyStack stack) {
        TechnologyStack existing = stackRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Technology stack not found"));

        existing.setName(stack.getName());
        existing.setDescription(stack.getDescription());
        existing.setCategory(stack.getCategory());
        existing.setUseCase(stack.getUseCase());
        existing.setComplexity(stack.getComplexity());
        existing.setMaturity(stack.getMaturity());
        existing.setTags(stack.getTags());
        existing.setIsPublic(stack.getIsPublic());
        existing.setDeploymentNotes(stack.getDeploymentNotes());
        existing.setUpdatedAt(LocalDateTime.now());

        TechnologyStack updated = stackRepository.save(existing);

        auditService.logAction("STACK_UPDATED", "Technology stack updated: " + existing.getName());

        return updated;
    }

    public void deleteStack(Long id) {
        TechnologyStack stack = stackRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Technology stack not found"));

        stackRepository.delete(stack);

        auditService.logAction("STACK_DELETED", "Technology stack deleted: " + stack.getName());
    }

    public List<TechnologyStack> searchStacks(String query, StackCategory category, StackComplexity complexity) {
        return stackRepository.searchStacks(query, category, complexity);
    }

    public List<TechnologyStack> getRecommendations(Long userId, String useCase) {
        return stackRepository.getRecommendations(userId, useCase);
    }

    public List<TechnologyStack> getStacksByTechnology(Long technologyId) {
        return stackRepository.findStacksByTechnology(technologyId);
    }

    public List<TechnologyStack> getStacksByProject(Long projectId) {
        return stackRepository.findStacksByProject(projectId);
    }
}
```

### 3. Web Scraping Service

```java
@Service
@Transactional
public class WebScrapingService {

    @Autowired
    private WebScrapingJobRepository jobRepository;

    @Autowired
    private TechnologyRepository technologyRepository;

    @Autowired
    private LekServerClient lekServerClient;

    @Autowired
    private AuditService auditService;

    @Autowired
    private TechnologyManagementMetrics metrics;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Scheduled(fixedRate = 300000) // Cada 5 minutos
    public void processWebScrapingJobs() {
        List<WebScrapingJob> pendingJobs = jobRepository.findByStatus(ScrapingJobStatus.PENDING);

        for (WebScrapingJob job : pendingJobs) {
            try {
                processJob(job);
            } catch (Exception e) {
                handleJobError(job, e);
            }
        }
    }

    private void processJob(WebScrapingJob job) {
        job.setStatus(ScrapingJobStatus.RUNNING);
        job.setStartedAt(LocalDateTime.now());
        jobRepository.save(job);

        try {
            // Enviar trabajo a leka-server
            String lekServerJobId = lekServerClient.submitScrapingJob(
                job.getSourceUrl(),
                job.getJobType(),
                job.getConfiguration()
            );

            job.setLekServerJobId(lekServerJobId);
            job.setLekServerStatus("SUBMITTED");
            job.setStatus(ScrapingJobStatus.QUEUED_IN_LEKA);
            jobRepository.save(job);

            auditService.logAction("SCRAPING_JOB_SUBMITTED",
                "Web scraping job submitted to leka-server: " + job.getId());

        } catch (Exception e) {
            throw new RuntimeException("Failed to submit job to leka-server", e);
        }
    }

    @RabbitListener(queues = "technology.webscraping.result.queue")
    public void handleScrapingResult(ScrapingResult result) {
        WebScrapingJob job = jobRepository.findByLekServerJobId(result.getJobId())
            .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (result.isSuccess()) {
            job.setStatus(ScrapingJobStatus.COMPLETED);
            job.setResult(result.getData());
            job.setCompletedAt(LocalDateTime.now());

            // Procesar resultado y actualizar tecnología
            processScrapingResult(job, result);

        } else {
            job.setStatus(ScrapingJobStatus.FAILED);
            job.setErrorMessage(result.getErrorMessage());
            job.setCompletedAt(LocalDateTime.now());
        }

        jobRepository.save(job);
        metrics.incrementWebScrapingJobs();

        auditService.logAction("SCRAPING_JOB_COMPLETED",
            "Web scraping job completed: " + job.getId());
    }

    private void processScrapingResult(WebScrapingJob job, ScrapingResult result) {
        Technology technology = technologyRepository.findById(job.getTechnologyId())
            .orElse(null);

        if (technology != null) {
            switch (job.getJobType()) {
                case VERSION_UPDATE:
                    updateTechnologyVersion(technology, result.getData());
                    break;
                case SECURITY_VULNERABILITY:
                    updateSecurityInfo(technology, result.getData());
                    break;
                case DOWNLOAD_STATS:
                    updateDownloadStats(technology, result.getData());
                    break;
                case COMMUNITY_METRICS:
                    updateCommunityMetrics(technology, result.getData());
                    break;
                case DOCUMENTATION_UPDATE:
                    updateDocumentationInfo(technology, result.getData());
                    break;
                default:
                    // Otros tipos de actualización
                    break;
            }

            technology.setLastUpdated(LocalDateTime.now());
            technologyRepository.save(technology);
        }
    }

    private void updateTechnologyVersion(Technology technology, String data) {
        // Lógica para actualizar versión de tecnología
        // Parsear JSON y actualizar campos relevantes
    }

    private void updateSecurityInfo(Technology technology, String data) {
        // Lógica para actualizar información de seguridad
    }

    private void updateDownloadStats(Technology technology, String data) {
        // Lógica para actualizar estadísticas de descarga
    }

    private void updateCommunityMetrics(Technology technology, String data) {
        // Lógica para actualizar métricas de comunidad
    }

    private void updateDocumentationInfo(Technology technology, String data) {
        // Lógica para actualizar información de documentación
    }

    private void handleJobError(WebScrapingJob job, Exception e) {
        job.setStatus(ScrapingJobStatus.FAILED);
        job.setErrorMessage(e.getMessage());
        job.setCompletedAt(LocalDateTime.now());

        if (job.getRetryCount() < 3) {
            job.setRetryCount(job.getRetryCount() + 1);
            job.setStatus(ScrapingJobStatus.RETRYING);
            job.setScheduledAt(LocalDateTime.now().plusMinutes(5));
        }

        jobRepository.save(job);

        auditService.logAction("SCRAPING_JOB_ERROR",
            "Web scraping job error: " + job.getId() + " - " + e.getMessage());
    }

    public WebScrapingJob scheduleJob(WebScrapingJob job) {
        job.setStatus(ScrapingJobStatus.PENDING);
        job.setCreatedAt(LocalDateTime.now());
        job.setScheduledAt(LocalDateTime.now());

        WebScrapingJob saved = jobRepository.save(job);

        auditService.logAction("SCRAPING_JOB_SCHEDULED",
            "Web scraping job scheduled: " + saved.getId());

        return saved;
    }

    public void retryJob(Long jobId) {
        WebScrapingJob job = jobRepository.findById(jobId)
            .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        job.setStatus(ScrapingJobStatus.PENDING);
        job.setScheduledAt(LocalDateTime.now());
        job.setRetryCount(0);
        jobRepository.save(job);

        auditService.logAction("SCRAPING_JOB_RETRY",
            "Web scraping job retry: " + job.getId());
    }

    public void cancelJob(Long jobId) {
        WebScrapingJob job = jobRepository.findById(jobId)
            .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        job.setStatus(ScrapingJobStatus.CANCELLED);
        job.setCompletedAt(LocalDateTime.now());
        jobRepository.save(job);

        // Cancelar en leka-server si es necesario
        if (job.getLekServerJobId() != null) {
            try {
                lekServerClient.cancelJob(job.getLekServerJobId());
            } catch (Exception e) {
                // Log error pero no fallar
            }
        }

        auditService.logAction("SCRAPING_JOB_CANCELLED",
            "Web scraping job cancelled: " + job.getId());
    }
}
```

### 4. Lek-Server Client

```java
@Component
public class LekServerClient {

    @Value("${lek-server.base-url}")
    private String baseUrl;

    @Value("${lek-server.api-key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    public String submitScrapingJob(String sourceUrl, ScrapingJobType jobType, String configuration) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        ScrapingJobRequest request = new ScrapingJobRequest();
        request.setSourceUrl(sourceUrl);
        request.setJobType(jobType.name());
        request.setConfiguration(configuration);

        HttpEntity<ScrapingJobRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ScrapingJobResponse> response = restTemplate.postForEntity(
            baseUrl + "/api/v1/scraping/jobs",
            entity,
            ScrapingJobResponse.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().getJobId();
        } else {
            throw new RuntimeException("Failed to submit job to leka-server");
        }
    }

    public void cancelJob(String jobId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(
            baseUrl + "/api/v1/scraping/jobs/" + jobId + "/cancel",
            HttpMethod.POST,
            entity,
            Void.class
        );
    }

    public ScrapingJobStatus getJobStatus(String jobId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<ScrapingJobStatusResponse> response = restTemplate.exchange(
            baseUrl + "/api/v1/scraping/jobs/" + jobId + "/status",
            HttpMethod.GET,
            entity,
            ScrapingJobStatusResponse.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return ScrapingJobStatus.valueOf(response.getBody().getStatus());
        } else {
            throw new RuntimeException("Failed to get job status from leka-server");
        }
    }
}

public class ScrapingJobRequest {
    private String sourceUrl;
    private String jobType;
    private String configuration;

    // Getters y setters
}

public class ScrapingJobResponse {
    private String jobId;
    private String status;

    // Getters y setters
}

public class ScrapingJobStatusResponse {
    private String status;

    // Getters y setters
}
```

### 5. RAG Integration Service

```java
@Service
public class RagIntegrationService {

    @Autowired
    private TechnologyDocumentationRepository techDocRepository;

    @Autowired
    private StackDocumentationRepository stackDocRepository;

    @Autowired
    private RagServerClient ragServerClient;

    @Autowired
    private AuditService auditService;

    public void processTechnologyDocumentation(Long technologyId) {
        List<TechnologyDocumentation> docs = techDocRepository.findByTechnologyId(technologyId);

        for (TechnologyDocumentation doc : docs) {
            if (doc.getIsRagReady()) {
                try {
                    // Enviar documentación al servidor RAG
                    ragServerClient.processDocumentation(
                        "technology",
                        technologyId.toString(),
                        doc.getTitle(),
                        doc.getContent(),
                        doc.getRagMetadata()
                    );

                    auditService.logAction("RAG_DOCUMENTATION_PROCESSED",
                        "Technology documentation processed for RAG: " + doc.getId());

                } catch (Exception e) {
                    auditService.logAction("RAG_DOCUMENTATION_ERROR",
                        "Failed to process technology documentation for RAG: " + doc.getId());
                }
            }
        }
    }

    public void processStackDocumentation(Long stackId) {
        List<StackDocumentation> docs = stackDocRepository.findByStackId(stackId);

        for (StackDocumentation doc : docs) {
            if (doc.getIsRagReady()) {
                try {
                    // Enviar documentación del stack al servidor RAG
                    ragServerClient.processDocumentation(
                        "stack",
                        stackId.toString(),
                        doc.getTitle(),
                        doc.getContent(),
                        doc.getRagMetadata()
                    );

                    auditService.logAction("RAG_STACK_DOCUMENTATION_PROCESSED",
                        "Stack documentation processed for RAG: " + doc.getId());

                } catch (Exception e) {
                    auditService.logAction("RAG_STACK_DOCUMENTATION_ERROR",
                        "Failed to process stack documentation for RAG: " + doc.getId());
                }
            }
        }
    }

    public List<String> searchRagDocumentation(String query, String context) {
        try {
            return ragServerClient.searchDocumentation(query, context);
        } catch (Exception e) {
            auditService.logAction("RAG_SEARCH_ERROR",
                "Failed to search RAG documentation: " + query);
            return new ArrayList<>();
        }
    }
}

@Component
public class RagServerClient {

    @Value("${rag-server.base-url}")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    public void processDocumentation(String type, String id, String title, String content, String metadata) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RagDocumentRequest request = new RagDocumentRequest();
        request.setType(type);
        request.setId(id);
        request.setTitle(title);
        request.setContent(content);
        request.setMetadata(metadata);

        HttpEntity<RagDocumentRequest> entity = new HttpEntity<>(request, headers);

        restTemplate.postForEntity(
            baseUrl + "/api/v1/documents",
            entity,
            Void.class
        );
    }

    public List<String> searchDocumentation(String query, String context) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RagSearchRequest request = new RagSearchRequest();
        request.setQuery(query);
        request.setContext(context);

        HttpEntity<RagSearchRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<RagSearchResponse> response = restTemplate.postForEntity(
            baseUrl + "/api/v1/search",
            entity,
            RagSearchResponse.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().getResults();
        } else {
            return new ArrayList<>();
        }
    }
}

public class RagDocumentRequest {
    private String type;
    private String id;
    private String title;
    private String content;
    private String metadata;

    // Getters y setters
}

public class RagSearchRequest {
    private String query;
    private String context;

    // Getters y setters
}

public class RagSearchResponse {
    private List<String> results;

    // Getters y setters
}
```

### 6. Training Integration Service

```java
@Service
public class TrainingIntegrationService {

    @Autowired
    private TechnologyPromptRepository techPromptRepository;

    @Autowired
    private StackPromptRepository stackPromptRepository;

    @Autowired
    private TechnologyDocumentationRepository techDocRepository;

    @Autowired
    private StackDocumentationRepository stackDocRepository;

    @Autowired
    private TrainingModuleClient trainingModuleClient;

    @Autowired
    private AuditService auditService;

    public List<TechnologyPrompt> getTrainingPrompts(Long technologyId) {
        return techPromptRepository.findByTechnologyIdAndIsTrainingPromptTrue(technologyId);
    }

    public List<StackPrompt> getStackTrainingPrompts(Long stackId) {
        return stackPromptRepository.findByStackIdAndIsTrainingPromptTrue(stackId);
    }

    public List<TechnologyDocumentation> getTrainingDocumentation(Long technologyId) {
        return techDocRepository.findByTechnologyIdAndTrainingRelevanceGreaterThan(technologyId, 5);
    }

    public List<StackDocumentation> getStackTrainingDocumentation(Long stackId) {
        return stackDocRepository.findByStackIdAndTrainingRelevanceGreaterThan(stackId, 5);
    }

    public void syncTrainingContent(Long technologyId) {
        try {
            List<TechnologyPrompt> prompts = getTrainingPrompts(technologyId);
            List<TechnologyDocumentation> docs = getTrainingDocumentation(technologyId);

            // Sincronizar con el módulo de entrenamiento
            trainingModuleClient.syncTechnologyTrainingContent(technologyId, prompts, docs);

            auditService.logAction("TRAINING_CONTENT_SYNCED",
                "Technology training content synced: " + technologyId);

        } catch (Exception e) {
            auditService.logAction("TRAINING_CONTENT_SYNC_ERROR",
                "Failed to sync technology training content: " + technologyId);
        }
    }

    public void syncStackTrainingContent(Long stackId) {
        try {
            List<StackPrompt> prompts = getStackTrainingPrompts(stackId);
            List<StackDocumentation> docs = getStackTrainingDocumentation(stackId);

            // Sincronizar con el módulo de entrenamiento
            trainingModuleClient.syncStackTrainingContent(stackId, prompts, docs);

            auditService.logAction("STACK_TRAINING_CONTENT_SYNCED",
                "Stack training content synced: " + stackId);

        } catch (Exception e) {
            auditService.logAction("STACK_TRAINING_CONTENT_SYNC_ERROR",
                "Failed to sync stack training content: " + stackId);
        }
    }
}

@Component
public class TrainingModuleClient {

    @Value("${training-module.base-url}")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    public void syncTechnologyTrainingContent(Long technologyId, List<TechnologyPrompt> prompts,
                                           List<TechnologyDocumentation> docs) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        TechnologyTrainingSyncRequest request = new TechnologyTrainingSyncRequest();
        request.setTechnologyId(technologyId);
        request.setPrompts(prompts);
        request.setDocumentation(docs);

        HttpEntity<TechnologyTrainingSyncRequest> entity = new HttpEntity<>(request, headers);

        restTemplate.postForEntity(
            baseUrl + "/api/v1/sync/technology-training",
            entity,
            Void.class
        );
    }

    public void syncStackTrainingContent(Long stackId, List<StackPrompt> prompts,
                                       List<StackDocumentation> docs) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        StackTrainingSyncRequest request = new StackTrainingSyncRequest();
        request.setStackId(stackId);
        request.setPrompts(prompts);
        request.setDocumentation(docs);

        HttpEntity<StackTrainingSyncRequest> entity = new HttpEntity<>(request, headers);

        restTemplate.postForEntity(
            baseUrl + "/api/v1/sync/stack-training",
            entity,
            Void.class
        );
    }
}

public class TechnologyTrainingSyncRequest {
    private Long technologyId;
    private List<TechnologyPrompt> prompts;
    private List<TechnologyDocumentation> documentation;

    // Getters y setters
}

public class StackTrainingSyncRequest {
    private Long stackId;
    private List<StackPrompt> prompts;
    private List<StackDocumentation> documentation;

    // Getters y setters
}
```

### 7. Technology Management Metrics

```java
@Component
public class TechnologyManagementMetrics {

    private final MeterRegistry meterRegistry;

    private final Counter technologiesCreatedCounter;
    private final Counter stacksCreatedCounter;
    private final Counter evaluationsCreatedCounter;
    private final Counter reviewsCreatedCounter;
    private final Counter webScrapingJobsCounter;

    private final Timer technologyCreationTimer;
    private final Timer stackCreationTimer;
    private final Timer evaluationCreationTimer;
    private final Timer reviewCreationTimer;
    private final Timer webScrapingJobTimer;

    public TechnologyManagementMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.technologiesCreatedCounter = Counter.builder("technology.management.technologies.created")
            .description("Number of technologies created")
            .register(meterRegistry);

        this.stacksCreatedCounter = Counter.builder("technology.management.stacks.created")
            .description("Number of technology stacks created")
            .register(meterRegistry);

        this.evaluationsCreatedCounter = Counter.builder("technology.management.evaluations.created")
            .description("Number of technology evaluations created")
            .register(meterRegistry);

        this.reviewsCreatedCounter = Counter.builder("technology.management.reviews.created")
            .description("Number of technology reviews created")
            .register(meterRegistry);

        this.webScrapingJobsCounter = Counter.builder("technology.management.webscraping.jobs")
            .description("Number of web scraping jobs processed")
            .register(meterRegistry);

        this.technologyCreationTimer = Timer.builder("technology.management.technology.creation.time")
            .description("Time taken to create a technology")
            .register(meterRegistry);

        this.stackCreationTimer = Timer.builder("technology.management.stack.creation.time")
            .description("Time taken to create a technology stack")
            .register(meterRegistry);

        this.evaluationCreationTimer = Timer.builder("technology.management.evaluation.creation.time")
            .description("Time taken to create a technology evaluation")
            .register(meterRegistry);

        this.reviewCreationTimer = Timer.builder("technology.management.review.creation.time")
            .description("Time taken to create a technology review")
            .register(meterRegistry);

        this.webScrapingJobTimer = Timer.builder("technology.management.webscraping.job.time")
            .description("Time taken to process a web scraping job")
            .register(meterRegistry);
    }

    public void incrementTechnologiesCreated() {
        technologiesCreatedCounter.increment();
    }

    public void incrementStacksCreated() {
        stacksCreatedCounter.increment();
    }

    public void incrementEvaluationsCreated() {
        evaluationsCreatedCounter.increment();
    }

    public void incrementReviewsCreated() {
        reviewsCreatedCounter.increment();
    }

    public void incrementWebScrapingJobs() {
        webScrapingJobsCounter.increment();
    }

    public Timer.Sample startTechnologyCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startStackCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startEvaluationCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startReviewCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startWebScrapingJobTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer getTechnologyCreationTimer() {
        return technologyCreationTimer;
    }

    public Timer getStackCreationTimer() {
        return stackCreationTimer;
    }

    public Timer getEvaluationCreationTimer() {
        return evaluationCreationTimer;
    }

    public Timer getReviewCreationTimer() {
        return reviewCreationTimer;
    }

    public Timer getWebScrapingJobTimer() {
        return webScrapingJobTimer;
    }
}
```

## Monitoreo y Métricas

### Prometheus Metrics

```java
@Component
public class TechnologyManagementMetrics {

    @Autowired
    private MeterRegistry meterRegistry;

    private final Counter technologiesCreatedCounter;
    private final Counter stacksCreatedCounter;
    private final Counter evaluationsCreatedCounter;
    private final Counter reviewsCreatedCounter;
    private final Counter webScrapingJobsCounter;
    private final Timer technologyCreationTimer;
    private final Timer stackCreationTimer;

    public TechnologyManagementMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.technologiesCreatedCounter = Counter.builder("technology.management.technologies.created")
            .description("Total technologies created")
            .register(meterRegistry);

        this.stacksCreatedCounter = Counter.builder("technology.management.stacks.created")
            .description("Total technology stacks created")
            .register(meterRegistry);

        this.evaluationsCreatedCounter = Counter.builder("technology.management.evaluations.created")
            .description("Total technology evaluations created")
            .register(meterRegistry);

        this.reviewsCreatedCounter = Counter.builder("technology.management.reviews.created")
            .description("Total technology reviews created")
            .register(meterRegistry);

        this.webScrapingJobsCounter = Counter.builder("technology.management.webscraping.jobs")
            .description("Total web scraping jobs processed")
            .register(meterRegistry);

        this.technologyCreationTimer = Timer.builder("technology.management.technology.creation.time")
            .description("Technology creation time")
            .register(meterRegistry);

        this.stackCreationTimer = Timer.builder("technology.management.stack.creation.time")
            .description("Technology stack creation time")
            .register(meterRegistry);
    }

    public void incrementTechnologiesCreated() {
        technologiesCreatedCounter.increment();
    }

    public void incrementStacksCreated() {
        stacksCreatedCounter.increment();
    }

    public void incrementEvaluationsCreated() {
        evaluationsCreatedCounter.increment();
    }

    public void incrementReviewsCreated() {
        reviewsCreatedCounter.increment();
    }

    public void incrementWebScrapingJobs() {
        webScrapingJobsCounter.increment();
    }

    public Timer.Sample startTechnologyCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startStackCreationTimer() {
        return Timer.start(meterRegistry);
    }
}
```

## Configuración de Aplicación

### application-technology-management.yml

```plaintext
technology-management:
  # Configuración de tecnologías
  technologies:
    default-version: "1.0.0"
    supported-categories: ["machine_learning", "deep_learning", "data_processing", "web_framework"]
    supported-types: ["framework", "library", "tool", "platform", "service"]
    max-tags: 10
    auto-versioning: true

  # Configuración de stacks
  stacks:
    max-technologies-per-stack: 20
    public-by-default: true
    auto-rating-calculation: true

  # Configuración de evaluaciones
  evaluations:
    min-score: 0.0
    max-score: 10.0
    required-fields: ["overall_score", "performance_score", "security_score"]
    auto-rating-update: true

  # Configuración de web scraping
  webscraping:
    queue:
      name: "technology.webscraping.queue"
      result-queue-name: "technology.webscraping.result.queue"
    retry:
      max-attempts: 3
      delay-minutes: 5
    scheduling:
      version-update-interval-hours: 24
      security-check-interval-hours: 12
      stats-update-interval-hours: 6

  # Configuración de marketplace
  marketplace:
    featured-technologies-count: 10
    trending-technologies-count: 20
    recommendation-algorithm: "collaborative_filtering"
    user-feedback-weight: 0.7
    community-rating-weight: 0.3

  # Configuración de leka-server
  leka-server:
    base-url: "https://leka-server.codeflowx.com"
    api-key: "${LEKA_SERVER_API_KEY}"
    timeout-seconds: 30
    max-concurrent-jobs: 10

  # Configuración de RAG
  rag:
    server:
      base-url: "https://rag-server.codeflowx.com"
      api-key: "${RAG_SERVER_API_KEY}"
      timeout-seconds: 60
    document-processing:
      max-file-size-mb: 50
      supported-formats: ["pdf", "docx", "md", "html", "txt"]
      auto-processing: true

  # Configuración de training
  training:
    module:
      base-url: "https://training-module.codeflowx.com"
      api-key: "${TRAINING_MODULE_API_KEY}"
      timeout-seconds: 30
    content-sync:
      auto-sync: true
      sync-interval-hours: 6
      min-relevance-score: 5

  # Configuración de prompts
  prompts:
    max-content-length: 10000
    supported-types: ["training", "execution", "testing", "debugging"]
    versioning:
      auto-version: true
      version-format: "semantic"

  # Configuración de infraestructura
  infrastructure:
    requirements:
      max-cpu-cores: 128
      max-ram-gb: 2048
      max-storage-gb: 100000
    gpu:
      supported-types: ["nvidia", "amd", "intel"]
      max-gpu-memory-gb: 80
    cloud:
      supported-providers: ["aws", "azure", "gcp", "digitalocean"]
      auto-detection: true

  # Configuración de monitoreo
  monitoring:
    metrics:
      enabled: true
      collection-interval-seconds: 30
    alerts:
      enabled: true
      webscraping-failure-threshold: 3
      evaluation-score-threshold: 5.0
```

## Scripts SQL

### Script de Eliminación (DROP)

```sql
-- Eliminar funciones, triggers e índices primero
DROP FUNCTION IF EXISTS update_technology_rating() CASCADE;
DROP FUNCTION IF EXISTS update_stack_rating() CASCADE;
DROP FUNCTION IF EXISTS update_technology_last_updated() CASCADE;
DROP FUNCTION IF EXISTS update_stack_usage_count() CASCADE;

-- Eliminar triggers
DROP TRIGGER IF EXISTS technology_rating_trigger ON tch_technology_reviews;
DROP TRIGGER IF EXISTS stack_rating_trigger ON tch_technology_evaluations;
DROP TRIGGER IF EXISTS technology_updated_trigger ON tch_technologies;
DROP TRIGGER IF EXISTS stack_usage_trigger ON tch_project_technology_usage;

-- Eliminar índices
DROP INDEX IF EXISTS idx_tch_technologies_name;
DROP INDEX IF EXISTS idx_tch_technologies_category;
DROP INDEX IF EXISTS idx_tch_technologies_type;
DROP INDEX IF EXISTS idx_tch_technologies_rating;
DROP INDEX IF EXISTS idx_tch_technology_versions_technology_id;
DROP INDEX IF EXISTS idx_tch_technology_versions_version;
DROP INDEX IF EXISTS idx_tch_technology_reviews_technology_id;
DROP INDEX IF EXISTS idx_tch_technology_reviews_user_id;
DROP INDEX IF EXISTS idx_tch_technology_reviews_rating;
DROP INDEX IF EXISTS idx_tch_technology_dependencies_technology_id;
DROP INDEX IF EXISTS idx_tch_technology_dependencies_dependency_id;
DROP INDEX IF EXISTS idx_tch_technology_stacks_name;
DROP INDEX IF EXISTS idx_tch_technology_stacks_category;
DROP INDEX IF EXISTS idx_tch_technology_stacks_user_id;
DROP INDEX IF EXISTS idx_tch_stack_technologies_stack_id;
DROP INDEX IF EXISTS idx_tch_stack_technologies_technology_id;
DROP INDEX IF EXISTS idx_tch_technology_evaluations_technology_id;
DROP INDEX IF EXISTS idx_tch_technology_evaluations_evaluator_id;
DROP INDEX IF EXISTS idx_tch_technology_documentation_technology_id;
DROP INDEX IF EXISTS idx_tch_technology_documentation_type;
DROP INDEX IF EXISTS idx_tch_technology_prompts_technology_id;
DROP INDEX IF EXISTS idx_tch_technology_prompts_type;
DROP INDEX IF EXISTS idx_tch_technology_infrastructure_technology_id;
DROP INDEX IF EXISTS idx_tch_stack_infrastructure_stack_id;
DROP INDEX IF EXISTS idx_tch_stack_prompts_stack_id;
DROP INDEX IF EXISTS idx_tch_stack_documentation_stack_id;
DROP INDEX IF EXISTS idx_tch_webscraping_jobs_technology_id;
DROP INDEX IF EXISTS idx_tch_webscraping_jobs_status;
DROP INDEX IF EXISTS idx_tch_webscraping_jobs_type;
DROP INDEX IF EXISTS idx_tch_project_technology_usage_project_id;
DROP INDEX IF EXISTS idx_tch_project_technology_usage_technology_id;

-- Eliminar tablas en orden de dependencia
DROP TABLE IF EXISTS tch_project_technology_usage;
DROP TABLE IF EXISTS tch_webscraping_jobs;
DROP TABLE IF EXISTS tch_stack_documentation;
DROP TABLE IF EXISTS tch_stack_prompts;
DROP TABLE IF EXISTS tch_stack_infrastructure;
DROP TABLE IF EXISTS tch_technology_infrastructure;
DROP TABLE IF EXISTS tch_technology_prompts;
DROP TABLE IF EXISTS tch_technology_documentation;
DROP TABLE IF EXISTS tch_technology_evaluations;
DROP TABLE IF EXISTS tch_stack_technologies;
DROP TABLE IF EXISTS tch_technology_stacks;
DROP TABLE IF EXISTS tch_technology_dependencies;
DROP TABLE IF EXISTS tch_technology_reviews;
DROP TABLE IF EXISTS tch_technology_versions;
DROP TABLE IF EXISTS tch_technologies;

-- Eliminar secuencias
DROP SEQUENCE IF EXISTS seq_tch_technologies;
DROP SEQUENCE IF EXISTS seq_tch_technology_versions;
DROP SEQUENCE IF EXISTS seq_tch_technology_reviews;
DROP SEQUENCE IF EXISTS seq_tch_technology_dependencies;
DROP SEQUENCE IF EXISTS seq_tch_technology_stacks;
DROP SEQUENCE IF EXISTS seq_tch_stack_technologies;
DROP SEQUENCE IF EXISTS seq_tch_technology_evaluations;
DROP SEQUENCE IF EXISTS seq_tch_technology_documentation;
DROP SEQUENCE IF EXISTS seq_tch_technology_prompts;
DROP SEQUENCE IF EXISTS seq_tch_technology_infrastructure;
DROP SEQUENCE IF EXISTS seq_tch_stack_infrastructure;
DROP SEQUENCE IF EXISTS seq_tch_stack_prompts;
DROP SEQUENCE IF EXISTS seq_tch_stack_documentation;
DROP SEQUENCE IF EXISTS seq_tch_webscraping_jobs;
DROP SEQUENCE IF EXISTS seq_tch_project_technology_usage;
```

### Script de Creación de Tablas

```sql
-- Tabla de tecnologías
CREATE TABLE tch_technologies (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    version VARCHAR(50),
    latest_version VARCHAR(50),
    homepage_url VARCHAR(500),
    repository_url VARCHAR(500),
    documentation_url VARCHAR(500),
    license VARCHAR(100),
    programming_language VARCHAR(100),
    tags TEXT,
    metadata TEXT,
    rating DECIMAL(3,2),
    download_count BIGINT DEFAULT 0,
    star_count BIGINT DEFAULT 0,
    fork_count BIGINT DEFAULT 0,
    last_updated TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de versiones de tecnologías
CREATE TABLE tch_technology_versions (
    id BIGSERIAL PRIMARY KEY,
    technology_id BIGINT REFERENCES tch_technologies(id) ON DELETE CASCADE,
    version VARCHAR(50) NOT NULL,
    release_date DATE,
    changelog TEXT,
    download_url VARCHAR(500),
    checksum VARCHAR(64),
    file_size_bytes BIGINT,
    is_stable BOOLEAN DEFAULT false,
    is_lts BOOLEAN DEFAULT false,
    end_of_life DATE,
    security_vulnerabilities INTEGER DEFAULT 0,
    compatibility_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de reseñas de tecnologías
CREATE TABLE tch_technology_reviews (
    id BIGSERIAL PRIMARY KEY,
    technology_id BIGINT REFERENCES tch_technologies(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES cor_users(id) ON DELETE CASCADE,
    rating INTEGER CHECK (rating >= 1 AND rating <= 5),
    review TEXT,
    pros TEXT,
    cons TEXT,
    use_case TEXT,
    experience_level VARCHAR(50),
    project_size VARCHAR(50),
    project_context TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de dependencias de tecnologías
CREATE TABLE tch_technology_dependencies (
    id BIGSERIAL PRIMARY KEY,
    technology_id BIGINT REFERENCES tch_technologies(id) ON DELETE CASCADE,
    dependency_technology_id BIGINT REFERENCES tch_technologies(id) ON DELETE CASCADE,
    dependency_type VARCHAR(50) NOT NULL,
    version_constraint VARCHAR(100),
    is_optional BOOLEAN DEFAULT false,
    dependency_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de stacks de tecnologías
CREATE TABLE tch_technology_stacks (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    user_id BIGINT REFERENCES cor_users(id) ON DELETE CASCADE,
    category VARCHAR(50) NOT NULL,
    use_case TEXT,
    complexity VARCHAR(50),
    maturity VARCHAR(50),
    tags TEXT,
    is_public BOOLEAN DEFAULT true,
    rating DECIMAL(3,2),
    usage_count BIGINT DEFAULT 0,
    deployment_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de tecnologías en stacks
CREATE TABLE tch_stack_technologies (
    id BIGSERIAL PRIMARY KEY,
    stack_id BIGINT REFERENCES tch_technology_stacks(id) ON DELETE CASCADE,
    technology_id BIGINT REFERENCES tch_technologies(id) ON DELETE CASCADE,
    role VARCHAR(50) NOT NULL,
    version_constraint VARCHAR(100),
    order_index INTEGER DEFAULT 0,
    is_required BOOLEAN DEFAULT true,
    configuration TEXT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de evaluaciones de tecnologías
CREATE TABLE tch_technology_evaluations (
    id BIGSERIAL PRIMARY KEY,
    technology_id BIGINT REFERENCES tch_technologies(id) ON DELETE CASCADE,
    evaluator_id BIGINT REFERENCES cor_users(id) ON DELETE CASCADE,
    evaluation_date DATE NOT NULL,
    overall_score DECIMAL(3,2),
    performance_score DECIMAL(3,2),
    security_score DECIMAL(3,2),
    maintainability_score DECIMAL(3,2),
    community_score DECIMAL(3,2),
    documentation_score DECIMAL(3,2),
    maturity_score DECIMAL(3,2),
    learning_curve_score DECIMAL(3,2),
    ecosystem_score DECIMAL(3,2),
    recommendation VARCHAR(50),
    notes TEXT,
    evaluation_context TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de trabajos de web scraping
CREATE TABLE tch_webscraping_jobs (
    id BIGSERIAL PRIMARY KEY,
    technology_id BIGINT REFERENCES tch_technologies(id) ON DELETE CASCADE,
    source_url VARCHAR(500) NOT NULL,
    job_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    configuration TEXT,
    scheduled_at TIMESTAMP,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    result TEXT,
    error_message TEXT,
    retry_count INTEGER DEFAULT 0,
    lek_server_job_id VARCHAR(255),
    lek_server_status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de documentación de tecnologías
CREATE TABLE tch_technology_documentation (
    id BIGSERIAL PRIMARY KEY,
    technology_id BIGINT REFERENCES tch_technologies(id) ON DELETE CASCADE,
    documentation_type VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    file_url VARCHAR(500),
    file_type VARCHAR(50),
    file_size_bytes BIGINT,
    language VARCHAR(10),
    version VARCHAR(50),
    is_rag_ready BOOLEAN DEFAULT false,
    rag_metadata TEXT,
    training_relevance INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de prompts de tecnologías
CREATE TABLE tch_technology_prompts (
    id BIGSERIAL PRIMARY KEY,
    technology_id BIGINT REFERENCES tch_technologies(id) ON DELETE CASCADE,
    prompt_type VARCHAR(50) NOT NULL,
    prompt_name VARCHAR(255) NOT NULL,
    prompt_content TEXT NOT NULL,
    prompt_description TEXT,
    prompt_category VARCHAR(100),
    prompt_tags TEXT,
    input_variables TEXT,
    output_format TEXT,
    usage_examples TEXT,
    is_training_prompt BOOLEAN DEFAULT false,
    is_execution_prompt BOOLEAN DEFAULT false,
    prompt_version VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de infraestructura de tecnologías
CREATE TABLE tch_technology_infrastructure (
    id BIGSERIAL PRIMARY KEY,
    technology_id BIGINT REFERENCES tch_technologies(id) ON DELETE CASCADE,
    infrastructure_type VARCHAR(100) NOT NULL,
    requirements TEXT,
    min_cpu_cores INTEGER,
    recommended_cpu_cores INTEGER,
    min_ram_gb INTEGER,
    recommended_ram_gb INTEGER,
    min_storage_gb INTEGER,
    recommended_storage_gb INTEGER,
    gpu_requirements TEXT,
    network_requirements TEXT,
    operating_system VARCHAR(255),
    container_support BOOLEAN DEFAULT false,
    cloud_providers TEXT,
    deployment_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de stacks de infraestructura
CREATE TABLE tch_stack_infrastructure (
    id BIGSERIAL PRIMARY KEY,
    stack_id BIGINT REFERENCES tch_technology_stacks(id) ON DELETE CASCADE,
    infrastructure_component VARCHAR(255) NOT NULL,
    component_type VARCHAR(100) NOT NULL,
    requirements TEXT,
    deployment_order INTEGER,
    is_required BOOLEAN DEFAULT true,
    scaling_rules TEXT,
    monitoring_requirements TEXT,
    security_requirements TEXT,
    backup_requirements TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de documentación de stacks
CREATE TABLE tch_stack_documentation (
    id BIGSERIAL PRIMARY KEY,
    stack_id BIGINT REFERENCES tch_technology_stacks(id) ON DELETE CASCADE,
    documentation_type VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    file_url VARCHAR(500),
    file_type VARCHAR(50),
    is_rag_ready BOOLEAN DEFAULT false,
    rag_metadata TEXT,
    training_relevance INTEGER CHECK (training_relevance >= 1 AND training_relevance <= 10),
    deployment_section TEXT,
    troubleshooting_section TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de prompts de stacks
CREATE TABLE tch_stack_prompts (
    id BIGSERIAL PRIMARY KEY,
    stack_id BIGINT REFERENCES tch_technology_stacks(id) ON DELETE CASCADE,
    prompt_name VARCHAR(255) NOT NULL,
    prompt_type VARCHAR(50) NOT NULL,
    prompt_content TEXT NOT NULL,
    prompt_description TEXT,
    target_technology_id BIGINT REFERENCES tch_technologies(id),
    execution_order INTEGER,
    input_mapping TEXT,
    output_mapping TEXT,
    validation_rules TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de uso de tecnologías en proyectos
CREATE TABLE tch_project_technology_usage (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT REFERENCES prj_projects(id) ON DELETE CASCADE,
    technology_id BIGINT REFERENCES tch_technologies(id) ON DELETE CASCADE,
    usage_type VARCHAR(50) NOT NULL,
    version_used VARCHAR(50),
    usage_start_date DATE,
    usage_end_date DATE,
    usage_intensity VARCHAR(50),
    usage_notes TEXT,
    performance_rating INTEGER CHECK (performance_rating &gt;= 1 AND performance_rating &lt;= 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Script de Creación de Índices

```sql
-- Índices para tecnologías
CREATE INDEX idx_tch_technologies_name ON tch_technologies(name);
CREATE INDEX idx_tch_technologies_category ON tch_technologies(category);
CREATE INDEX idx_tch_technologies_type ON tch_technologies(type);
CREATE INDEX idx_tch_technologies_rating ON tch_technologies(rating);
CREATE INDEX idx_tch_technologies_created_at ON tch_technologies(created_at);

-- Índices para versiones
CREATE INDEX idx_tch_technology_versions_technology_id ON tch_technology_versions(technology_id);
CREATE INDEX idx_tch_technology_versions_version ON tch_technology_versions(version);
CREATE INDEX idx_tch_technology_versions_release_date ON tch_technology_versions(release_date);

-- Índices para reseñas
CREATE INDEX idx_tch_technology_reviews_technology_id ON tch_technology_reviews(technology_id);
CREATE INDEX idx_tch_technology_reviews_user_id ON tch_technology_reviews(user_id);
CREATE INDEX idx_tch_technology_reviews_rating ON tch_technology_reviews(rating);
CREATE INDEX idx_tch_technology_reviews_created_at ON tch_technology_reviews(created_at);

-- Índices para dependencias
CREATE INDEX idx_tch_technology_dependencies_technology_id ON tch_technology_dependencies(technology_id);
CREATE INDEX idx_tch_technology_dependencies_dependency_id ON tch_technology_dependencies(dependency_technology_id);
CREATE INDEX idx_tch_technology_dependencies_type ON tch_technology_dependencies(dependency_type);

-- Índices para stacks
CREATE INDEX idx_tch_technology_stacks_name ON tch_technology_stacks(name);
CREATE INDEX idx_tch_technology_stacks_category ON tch_technology_stacks(category);
CREATE INDEX idx_tch_technology_stacks_user_id ON tch_technology_stacks(user_id);
CREATE INDEX idx_tch_technology_stacks_rating ON tch_technology_stacks(rating);
CREATE INDEX idx_tch_technology_stacks_created_at ON tch_technology_stacks(created_at);

-- Índices para tecnologías en stacks
CREATE INDEX idx_tch_stack_technologies_stack_id ON tch_stack_technologies(stack_id);
CREATE INDEX idx_tch_stack_technologies_technology_id ON tch_stack_technologies(technology_id);
CREATE INDEX idx_tch_stack_technologies_role ON tch_stack_technologies(role);
CREATE INDEX idx_tch_stack_technologies_order ON tch_stack_technologies(order_index);

-- Índices para evaluaciones
CREATE INDEX idx_tch_technology_evaluations_technology_id ON tch_technology_evaluations(technology_id);
CREATE INDEX idx_tch_technology_evaluations_evaluator_id ON tch_technology_evaluations(evaluator_id);
CREATE INDEX idx_tch_technology_evaluations_date ON tch_technology_evaluations(evaluation_date);
CREATE INDEX idx_tch_technology_evaluations_score ON tch_technology_evaluations(overall_score);

-- Índices para documentación
CREATE INDEX idx_tch_technology_documentation_technology_id ON tch_technology_documentation(technology_id);
CREATE INDEX idx_tch_technology_documentation_type ON tch_technology_documentation(documentation_type);
CREATE INDEX idx_tch_technology_documentation_rag_ready ON tch_technology_documentation(is_rag_ready);
CREATE INDEX idx_tch_technology_documentation_training ON tch_technology_documentation(training_relevance);

-- Índices para prompts
CREATE INDEX idx_tch_technology_prompts_technology_id ON tch_technology_prompts(technology_id);
CREATE INDEX idx_tch_technology_prompts_type ON tch_technology_prompts(prompt_type);
CREATE INDEX idx_tch_technology_prompts_training ON tch_technology_prompts(is_training_prompt);
CREATE INDEX idx_tch_technology_prompts_execution ON tch_technology_prompts(is_execution_prompt);

-- Índices para infraestructura
CREATE INDEX idx_tch_technology_infrastructure_technology_id ON tch_technology_infrastructure(technology_id);
CREATE INDEX idx_tch_technology_infrastructure_type ON tch_technology_infrastructure(infrastructure_type);

-- Índices para infraestructura de stacks
CREATE INDEX idx_tch_stack_infrastructure_stack_id ON tch_stack_infrastructure(stack_id);
CREATE INDEX idx_tch_stack_infrastructure_type ON tch_stack_infrastructure(component_type);

-- Índices para prompts de stacks
CREATE INDEX idx_tch_stack_prompts_stack_id ON tch_stack_prompts(stack_id);
CREATE INDEX idx_tch_stack_prompts_type ON tch_stack_prompts(prompt_type);
CREATE INDEX idx_tch_stack_prompts_target_technology_id ON tch_stack_prompts(target_technology_id);

-- Índices para documentación de stacks
CREATE INDEX idx_tch_stack_documentation_stack_id ON tch_stack_documentation(stack_id);
CREATE INDEX idx_tch_stack_documentation_type ON tch_stack_documentation(documentation_type);
CREATE INDEX idx_tch_stack_documentation_rag ON tch_stack_documentation(is_rag_ready);

-- Índices para web scraping
CREATE INDEX idx_tch_webscraping_jobs_technology_id ON tch_webscraping_jobs(technology_id);
CREATE INDEX idx_tch_webscraping_jobs_status ON tch_webscraping_jobs(status);
CREATE INDEX idx_tch_webscraping_jobs_type ON tch_webscraping_jobs(job_type);
CREATE INDEX idx_tch_webscraping_jobs_scheduled ON tch_webscraping_jobs(scheduled_at);
CREATE INDEX idx_tch_webscraping_jobs_lek ON tch_webscraping_jobs(lek_server_job_id);

-- Índices para uso en proyectos
CREATE INDEX idx_tch_project_technology_usage_project_id ON tch_project_technology_usage(project_id);
CREATE INDEX idx_tch_project_technology_usage_technology_id ON tch_project_technology_usage(technology_id);
CREATE INDEX idx_tch_project_technology_usage_type ON tch_project_technology_usage(usage_type);
CREATE INDEX idx_tch_project_technology_usage_start_date ON tch_project_technology_usage(usage_start_date);
```

### Script de Datos de Demostración

```sql
-- Insertar tecnologías de demostración
INSERT INTO tch_technologies (name, description, category, type, version, latest_version, homepage_url, repository_url, documentation_url, license, programming_language, tags, rating, download_count, star_count, fork_count) VALUES
('TensorFlow', 'Biblioteca de código abierto para machine learning y deep learning', 'MACHINE_LEARNING', 'FRAMEWORK', '2.15.0', '2.15.0', 'https://tensorflow.org', 'https://github.com/tensorflow/tensorflow', 'https://tensorflow.org/docs', 'Apache 2.0', 'Python', '["AI", "ML", "Deep Learning", "Neural Networks"]', 4.8, 15000000, 180000, 85000),
('PyTorch', 'Framework de machine learning para Python con soporte para GPU', 'MACHINE_LEARNING', 'FRAMEWORK', '2.1.0', '2.1.0', 'https://pytorch.org', 'https://github.com/pytorch/pytorch', 'https://pytorch.org/docs', 'BSD', 'Python', '["AI", "ML", "Deep Learning", "Research"]', 4.7, 12000000, 75000, 45000),
('React', 'Biblioteca de JavaScript para construir interfaces de usuario', 'FRONTEND', 'LIBRARY', '18.2.0', '18.2.0', 'https://reactjs.org', 'https://github.com/facebook/react', 'https://reactjs.org/docs', 'MIT', 'JavaScript', '["UI", "Frontend", "Component-Based", "Virtual DOM"]', 4.9, 25000000, 210000, 45000),
('Spring Boot', 'Framework para crear aplicaciones Spring independientes', 'BACKEND', 'FRAMEWORK', '3.2.0', '3.2.0', 'https://spring.io/projects/spring-boot', 'https://github.com/spring-projects/spring-boot', 'https://docs.spring.io/spring-boot', 'Apache 2.0', 'Java', '["Backend", "Microservices", "Enterprise", "Java"]', 4.6, 8000000, 68000, 38000),
('Docker', 'Plataforma para desarrollar, enviar y ejecutar aplicaciones en contenedores', 'DEVOPS', 'PLATFORM', '24.0.0', '24.0.0', 'https://docker.com', 'https://github.com/docker/docker-ce', 'https://docs.docker.com', 'Apache 2.0', 'Go', '["Containerization", "DevOps", "Deployment", "Microservices"]', 4.8, 20000000, 65000, 25000),
('PostgreSQL', 'Sistema de gestión de bases de datos objeto relacional', 'DATABASE', 'DATABASE_ENGINE', '15.5', '15.5', 'https://postgresql.org', 'https://github.com/postgres/postgres', 'https://www.postgresql.org/docs', 'PostgreSQL', 'C', '["Database", "SQL", "ACID", "Open Source"]', 4.7, 18000000, 12000, 8000),
('Kubernetes', 'Plataforma de orquestación de contenedores de código abierto', 'DEVOPS', 'ORCHESTRATOR', '1.28.0', '1.28.0', 'https://kubernetes.io', 'https://github.com/kubernetes/kubernetes', 'https://kubernetes.io/docs', 'Apache 2.0', 'Go', '["Container Orchestration", "DevOps", "Microservices", "Cloud Native"]', 4.6, 15000000, 98000, 55000),
('Vue.js', 'Framework progresivo de JavaScript para construir interfaces de usuario', 'FRONTEND', 'FRAMEWORK', '3.3.0', '3.3.0', 'https://vuejs.org', 'https://github.com/vuejs/vue', 'https://vuejs.org/guide', 'MIT', 'JavaScript', '["UI", "Frontend", "Progressive", "Component-Based"]', 4.5, 12000000, 35000, 18000),
('FastAPI', 'Framework web moderno y rápido para Python', 'BACKEND', 'FRAMEWORK', '0.104.0', '0.104.0', 'https://fastapi.tiangolo.com', 'https://github.com/tiangolo/fastapi', 'https://fastapi.tiangolo.com', 'MIT', 'Python', '["API", "Backend", "Fast", "Async"]', 4.8, 8000000, 68000, 28000),
('Redis', 'Sistema de almacenamiento en memoria de estructura de datos', 'DATABASE', 'CACHE_SYSTEM', '7.2.0', '7.2.0', 'https://redis.io', 'https://github.com/redis/redis', 'https://redis.io/documentation', 'BSD', 'C', '["Cache", "In-Memory", "Key-Value", "High Performance"]', 4.6, 16000000, 58000, 22000);

-- Insertar versiones de tecnologías
INSERT INTO tch_technology_versions (technology_id, version, release_date, changelog, is_stable, is_lts) VALUES
(1, '2.15.0', '2023-11-15', 'Mejoras de rendimiento y nuevas APIs', true, false),
(1, '2.14.0', '2023-08-15', 'Correcciones de seguridad y optimizaciones', true, true),
(2, '2.1.0', '2023-10-15', 'Soporte para nuevas arquitecturas de GPU', true, false),
(2, '2.0.0', '2023-03-15', 'Major release con cambios significativos', true, true),
(3, '18.2.0', '2022-06-14', 'Mejoras en el renderizado concurrente', true, false),
(4, '3.2.0', '2023-11-23', 'Soporte para Java 21 y mejoras de rendimiento', true, false);

-- Insertar reseñas de tecnologías
INSERT INTO tch_technology_reviews (technology_id, user_id, rating, review, pros, cons, use_case, experience_level, project_size) VALUES
(1, 1, 5, 'Excelente framework para deep learning', '["Fácil de usar", "Buena documentación", "Comunidad activa"]', '["Curva de aprendizaje inicial", "Requiere GPU para mejor rendimiento"]', 'Proyecto de reconocimiento de imágenes', 'INTERMEDIATE', 'MEDIUM'),
(2, 2, 4, 'Muy bueno para investigación y prototipado', '["Flexible", "Pythonic", "Buen debugging"]', '["Menos maduro que TensorFlow", "Documentación menos completa"]', 'Investigación en NLP', 'ADVANCED', 'SMALL'),
(3, 3, 5, 'La mejor opción para frontend moderno', '["Componentes reutilizables", "Virtual DOM eficiente", "Ecosistema rico"]', '["Curva de aprendizaje", "Configuración inicial compleja"]', 'Aplicación web empresarial', 'EXPERT', 'LARGE'),
(4, 4, 4, 'Framework robusto para aplicaciones Java', '["Enterprise-ready", "Spring ecosystem", "Buena documentación"]', '["Overhead de configuración", "Curva de aprendizaje"]', 'API REST empresarial', 'INTERMEDIATE', 'MEDIUM');

-- Insertar dependencias de tecnologías
INSERT INTO tch_technology_dependencies (technology_id, dependency_technology_id, dependency_type, version_constraint, is_optional) VALUES
(1, 6, 'REQUIRES', '>=13.0', false),
(2, 6, 'REQUIRES', '>=3.7', false),
(3, 6, 'REQUIRES', '>=14.0', false),
(4, 6, 'REQUIRES', '>=11.0', false),
(1, 10, 'RECOMMENDS', '>=6.0', true),
(2, 10, 'RECOMMENDS', '>=6.0', true);

-- Insertar stacks de tecnologías
INSERT INTO tch_technology_stacks (name, description, user_id, category, use_case, complexity, maturity, tags, rating) VALUES
('AI Development Stack', 'Stack completo para desarrollo de aplicaciones de IA', 1, 'MACHINE_LEARNING', 'Desarrollo de modelos de ML', 'COMPLEX', 'PRODUCTION_READY', '["AI", "ML", "Python", "GPU"]', 4.7),
('Full Stack Web Development', 'Stack moderno para aplicaciones web completas', 2, 'FULL_STACK', 'Aplicaciones web empresariales', 'MODERATE', 'PRODUCTION_READY', '["Web", "JavaScript", "Java", "Database"]', 4.5),
('Microservices Architecture', 'Stack para arquitectura de microservicios', 3, 'MICROSERVICES', 'Sistemas distribuidos', 'COMPLEX', 'ENTERPRISE_GRADE', '["Microservices", "Container", "Orchestration", "Monitoring"]', 4.6),
('Data Science Pipeline', 'Stack para pipelines de datos y análisis', 4, 'DATA_PIPELINE', 'Análisis de datos y ML', 'MODERATE', 'STABLE', '["Data", "Python", "ML", "Visualization"]', 4.4);

-- Insertar tecnologías en stacks
INSERT INTO tch_stack_technologies (stack_id, technology_id, role, version_constraint, order_index, is_required) VALUES
(1, 1, 'CORE', '>=2.14.0', 1, true),
(1, 2, 'SUPPORTING', '>=2.0.0', 2, true),
(1, 6, 'CORE', '>=13.0', 3, true),
(1, 10, 'SUPPORTING', '>=6.0', 4, false),
(2, 3, 'CORE', '>=18.0.0', 1, true),
(2, 4, 'CORE', '>=3.0.0', 2, true),
(2, 6, 'CORE', '>=13.0', 3, true),
(2, 10, 'SUPPORTING', '>=6.0', 4, false),
(3, 5, 'CORE', '>=24.0.0', 1, true),
(3, 7, 'CORE', '>=1.25.0', 2, true),
(3, 6, 'CORE', '>=13.0', 3, true),
(3, 10, 'SUPPORTING', '>=6.0', 4, false);

-- Insertar evaluaciones de tecnologías
INSERT INTO tch_technology_evaluations (technology_id, evaluator_id, evaluation_date, overall_score, performance_score, security_score, maintainability_score, community_score, documentation_score, maturity_score, learning_curve_score, ecosystem_score, recommendation) VALUES
(1, 1, '2023-12-01', 4.8, 4.9, 4.7, 4.8, 4.9, 4.8, 4.9, 4.5, 4.9, 'STRONGLY_RECOMMEND'),
(2, 2, '2023-12-01', 4.7, 4.8, 4.6, 4.7, 4.8, 4.6, 4.7, 4.8, 4.7, 'RECOMMEND'),
(3, 3, '2023-12-01', 4.9, 4.8, 4.7, 4.9, 4.9, 4.9, 4.8, 4.6, 4.9, 'STRONGLY_RECOMMEND'),
(4, 4, '2023-12-01', 4.6, 4.7, 4.8, 4.6, 4.7, 4.8, 4.7, 4.5, 4.7, 'RECOMMEND');

-- Insertar documentación de tecnologías
INSERT INTO tch_technology_documentation (technology_id, documentation_type, title, content, is_rag_ready, training_relevance) VALUES
(1, 'USER_GUIDE', 'Getting Started with TensorFlow', 'Guía completa para comenzar con TensorFlow...', true, 9),
(1, 'API_REFERENCE', 'TensorFlow API Reference', 'Referencia completa de la API de TensorFlow...', true, 8),
(2, 'TUTORIAL', 'PyTorch Tutorial for Beginners', 'Tutorial paso a paso para principiantes...', true, 9),
(3, 'BEST_PRACTICES', 'React Best Practices', 'Mejores prácticas para desarrollo con React...', true, 8);

-- Insertar prompts de tecnologías
INSERT INTO tch_technology_prompts (technology_id, prompt_type, prompt_name, prompt_content, is_training_prompt, is_execution_prompt) VALUES
(1, 'TRAINING', 'TensorFlow Model Training', 'Prompt para entrenar modelos con TensorFlow...', true, false),
(1, 'EXECUTION', 'TensorFlow Model Inference', 'Prompt para ejecutar inferencia con TensorFlow...', false, true),
(2, 'TRAINING', 'PyTorch Training Setup', 'Prompt para configurar entrenamiento con PyTorch...', true, false),
(3, 'TRAINING', 'React Component Creation', 'Prompt para crear componentes React...', true, false);

-- Insertar infraestructura de tecnologías
INSERT INTO tch_technology_infrastructure (technology_id, infrastructure_type, min_cpu_cores, recommended_cpu_cores, min_ram_gb, recommended_ram_gb, min_storage_gb, recommended_storage_gb, container_support) VALUES
(1, 'COMPUTE', 4, 8, 8, 16, 20, 50, true),
(2, 'COMPUTE', 2, 4, 4, 8, 10, 25, true),
(3, 'COMPUTE', 2, 4, 4, 8, 5, 15, true),
(4, 'COMPUTE', 4, 8, 8, 16, 20, 50, true),
(5, 'CONTAINER', 2, 4, 4, 8, 10, 25, true),
(6, 'DATABASE', 2, 4, 4, 8, 20, 100, true);

-- Insertar infraestructura de stacks
INSERT INTO tch_stack_infrastructure (stack_id, infrastructure_component, component_type, requirements, deployment_order, is_required) VALUES
(1, 'GPU Cluster', 'COMPUTE', '{"gpu_type": "NVIDIA V100", "gpu_count": 4, "gpu_memory": "32GB"}', 1, true),
(1, 'High Memory Nodes', 'COMPUTE', '{"ram_gb": 128, "cpu_cores": 32}', 2, true),
(2, 'Load Balancer', 'LOAD_BALANCING', '{"algorithm": "round_robin", "health_check": true}', 1, true),
(2, 'Application Servers', 'COMPUTE', '{"ram_gb": 16, "cpu_cores": 8}', 2, true),
(3, 'Container Registry', 'STORAGE', '{"storage_gb": 1000, "replication": true}', 1, true),
(3, 'Monitoring Stack', 'MONITORING', '{"prometheus": true, "grafana": true}', 3, false);

-- Insertar prompts de stacks
INSERT INTO tch_stack_prompts (stack_id, prompt_name, prompt_type, prompt_content, target_technology_id, execution_order) VALUES
(1, 'Setup GPU Environment', 'TRAINING', 'Prompt para configurar entorno GPU...', 1, 1),
(1, 'Train ML Model', 'EXECUTION', 'Prompt para entrenar modelo ML...', 1, 2),
(2, 'Deploy Frontend', 'EXECUTION', 'Prompt para desplegar frontend...', 3, 1),
(2, 'Deploy Backend', 'EXECUTION', 'Prompt para desplegar backend...', 4, 2);

-- Insertar documentación de stacks
INSERT INTO tch_stack_documentation (stack_id, documentation_type, title, content, is_rag_ready, training_relevance, deployment_section, troubleshooting_section) VALUES
(1, 'DEPLOYMENT', 'AI Stack Deployment Guide', 'Guía completa de despliegue...', true, 9, 'Sección detallada de despliegue...', 'Solución de problemas comunes...'),
(2, 'ARCHITECTURE', 'Full Stack Architecture', 'Arquitectura del stack completo...', true, 8, 'Guía de despliegue...', 'Troubleshooting...'),
(3, 'DEPLOYMENT', 'Microservices Deployment', 'Despliegue de microservicios...', true, 7, 'Pasos de despliegue...', 'Problemas y soluciones...');

-- Insertar trabajos de web scraping
INSERT INTO tch_webscraping_jobs (technology_id, source_url, job_type, status, scheduled_at) VALUES
(1, 'https://pypi.org/project/tensorflow/', 'VERSION_UPDATE', 'PENDING', CURRENT_TIMESTAMP),
(2, 'https://pypi.org/project/torch/', 'VERSION_UPDATE', 'PENDING', CURRENT_TIMESTAMP),
(3, 'https://www.npmjs.com/package/react', 'VERSION_UPDATE', 'PENDING', CURRENT_TIMESTAMP),
(4, 'https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-parent', 'VERSION_UPDATE', 'PENDING', CURRENT_TIMESTAMP);

-- Insertar uso de tecnologías en proyectos
INSERT INTO tch_project_technology_usage (project_id, technology_id, usage_type, version_used, usage_start_date, usage_intensity, performance_rating) VALUES
(1, 1, 'PRIMARY', '2.15.0', '2023-01-01', 'HIGH', 5),
(1, 6, 'CORE', '15.5', '2023-01-01', 'HIGH', 5),
(2, 3, 'PRIMARY', '18.2.0', '2023-02-01', 'HIGH', 5),
(2, 4, 'PRIMARY', '3.2.0', '2023-02-01', 'HIGH', 4),
(2, 6, 'CORE', '15.5', '2023-02-01', 'HIGH', 5),
(3, 5, 'PRIMARY', '24.0.0', '2023-03-01', 'HIGH', 5),
(3, 7, 'PRIMARY', '1.28.0', '2023-03-01', 'HIGH', 4);
```

## Notas de Implementación

### **🔗 Integración con Leka-Server**

- El módulo de webscraping se conecta con leka-server para realizar búsquedas automáticas
- Leka-server devuelve resultados para almacenamiento en la base de datos
- Sistema de colas para procesamiento asíncrono de trabajos de scraping

### **🔗 Relación con Datasets y Entrenamiento**

- Las tecnologías y stacks están relacionadas con los datasets del módulo de entrenamiento
- Integración completa con el módulo de training para gestionar tecnologías utilizadas
- Sincronización automática de contenido de entrenamiento

### **📚 Gestión de Documentación para RAG**

- Sistema completo de gestión de documentación para el servidor RAG
- Documentación utilizable para formación y consultas inteligentes
- Metadatos RAG para optimización de búsquedas

### **📊 Consulta de Proyectos por Tecnologías**

- Mapeo completo entre proyectos y tecnologías utilizadas
- Consultas por tecnologías específicas o stacks tecnológicos
- Métricas de uso y rendimiento por proyecto

### **🏗️ Documentación de Requisitos de Infraestructura**

- Especificaciones detalladas de requisitos de infraestructura en stacks
- Evaluación automática de despliegue basada en requisitos
- Soporte para GPU, CPU, RAM, almacenamiento y red

### **🤖 Prompts para Entrenamiento y Ejecución**

- Sistema completo de gestión de prompts para entrenamiento
- Prompts para ejecución y despliegue
- Versionado y categorización de prompts

### **🔧 Normalización Aplicada**

- Prefijo `tch_` aplicado a todas las tablas del módulo
- PKs autonuméricas (`id BIGSERIAL PRIMARY KEY`) en todas las tablas
- 3NF y BCNF aplicados para optimización de consultas
- Índices optimizados para rendimiento

### **📈 Métricas y Monitoreo**

- Sistema completo de métricas con Micrometer/Prometheus
- Monitoreo de operaciones CRUD y webscraping
- Alertas configurables para fallos y umbrales

## Uso del Script

### **Orden de Ejecución:**

1. **Script de Eliminación**: Ejecutar primero para limpiar objetos existentes
2. **Script de Creación**: Crear todas las tablas con estructura normalizada
3. **Script de Índices**: Crear índices para optimización de consultas
4. **Script de Datos**: Insertar datos de demostración para testing

### **Verificación de Datos:**

```sql
-- Verificar tecnologías creadas
SELECT COUNT(*) FROM tch_technologies;

-- Verificar stacks creados
SELECT COUNT(*) FROM tch_technology_stacks;

-- Verificar documentación RAG-ready
SELECT COUNT(*) FROM tch_technology_documentation WHERE is_rag_ready = true;

-- Verificar prompts de entrenamiento
SELECT COUNT(*) FROM tch_technology_prompts WHERE is_training_prompt = true;

-- Verificar trabajos de webscraping
SELECT COUNT(*) FROM tch_webscraping_jobs;
```

## Conclusión

El módulo de Technology Management proporciona una gestión completa del catálogo de tecnologías, incluyendo:

- **Gestión de Tecnologías**: Catálogo completo con versiones, dependencias y metadatos
- **Technology Stacks**: Creación y gestión de stacks con requisitos de infraestructura
- **Integración Leka-Server**: Webscraping automático para actualización de información
- **Gestión RAG**: Documentación para servidor RAG y formación
- **Prompts de Entrenamiento**: Gestión de prompts para entrenamiento y ejecución
- **Requisitos de Infraestructura**: Especificaciones para despliegue y evaluación
- **Relación con Proyectos**: Mapeo de tecnologías utilizadas en proyectos
- **Integración Training**: Conexión con módulo de entrenamiento y datasets

El sistema está diseñado para ser escalable, con actualizaciones automáticas mediante webscraping integrado con leka-server, gestión completa de documentación RAG, prompts para entrenamiento y ejecución, requisitos de infraestructura detallados, y un sistema de evaluación colaborativo que ayuda a los usuarios a tomar decisiones informadas sobre tecnologías.

**Todas las funcionalidades pendientes han sido implementadas y el módulo está completamente normalizado según las reglas establecidas.**
