## 02\. Módulo de Model Management - Portal Backend

## Descripción General

El módulo de Model Management gestiona todo el ciclo de vida de los modelos de IA, incluyendo versionado, experimentos, artefactos, despliegue, monitorización y **integración con marketplaces externos**. Se integra con MLflow para el tracking de experimentos, con el sistema de auditoría del portal y con **lek-server** para búsqueda y descarga automática de modelos de múltiples proveedores (+3,000,000 modelos).

## Características Principales

- **Gestión Completa de Modelos**: Creación, versionado, experimentos y despliegue
- **Integración con Marketplaces**: Búsqueda automática en +7 proveedores principales
- **Gestión de Proveedores**: OpenAI, Anthropic, HuggingFace, CodeflowX, Self-hosting
- **Sistema de Costes**: Control de precios por tokens, imágenes y llamadas
- **Cumplimiento Normativo**: IA Act Europea, GDPR, certificaciones
- **Recursos de Hardware**: Requisitos mínimos y recomendados
- **Métricas Avanzadas**: Consumo, rendimiento y calidad
- **Integración MLflow**: Tracking completo de experimentos

## Entidades del Sistema

### 1\. AI Provider (Proveedor de IA)

```java
@Entity
@Table(name = "mdl_ai_providers")
public class AIProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider_name", nullable = false, unique = true)
    private String providerName; // OpenAI, Anthropic, HuggingFace, CodeflowX, Self-hosting

    @Column(name = "provider_type")
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Column(name = "api_base_url")
    private String apiBaseUrl;

    @Column(name = "service_endpoint")
    private String serviceEndpoint; // URL del servicio CodeflowX que gestiona el proveedor

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "rate_limit_per_minute")
    private Integer rateLimitPerMinute;

    @Column(name = "max_concurrent_requests")
    private Integer maxConcurrentRequests;

    @Column(name = "supported_model_types")
    private String supportedModelTypes; // JSON array de tipos soportados

    @Column(name = "provider_metadata")
    private String providerMetadata; // JSON con información adicional del proveedor

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
    private List<providertoken> tokens = new ArrayList&lt;&gt;();

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
    private List<model> models = new ArrayList&lt;&gt;();
}

public enum ProviderType {
    OPENAI, ANTHROPIC, HUGGINGFACE, CODEFLOWX, SELF_HOSTING, CIVITAI, GOOGLE_AI,
    TORCH_HUB, NVIDIA_NGC, INTEL_OPENVINO, ONNX_MODEL_ZOO
}
```

### 2\. Provider Token (Token del Proveedor)

```java
@Entity
@Table(name = "mdl_provider_tokens")
public class ProviderToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider_id", nullable = false)
    private Long providerId;

    @Column(name = "token_name", nullable = false)
    private String tokenName;

    @Column(name = "token_value", nullable = false)
    private String tokenValue; // Token encriptado

    @Column(name = "encryption_key_id")
    private String encryptionKeyId;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "usage_limit")
    private Long usageLimit; // Límite de uso del token

    @Column(name = "current_usage")
    private Long currentUsage = 0L;

    @Column(name = "created_by")
    private Long createdBy; // Referencia a cor_users.id

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "provider_id", insertable = false, updatable = false)
    private AIProvider provider;
}
```

### 3\. Model (Modelo de IA)

```java
@Entity
@Table(name = "mdl_models")
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @Column(name = "model_description")
    private String modelDescription;

    @Column(name = "project_id")
    private Long projectId; // Referencia a prj_projects.id

    @Column(name = "user_id")
    private Long userId; // Referencia a cor_users.id

    @Column(name = "provider_id")
    private Long providerId; // Referencia a mdl_ai_providers.id

    @Column(name = "model_type")
    @Enumerated(EnumType.STRING)
    private ModelType modelType;

    @Column(name = "model_category")
    @Enumerated(EnumType.STRING)
    private ModelCategory modelCategory;

    @Column(name = "framework")
    private String framework; // TensorFlow, PyTorch, Scikit-learn, etc.

    @Column(name = "version")
    private String version;

    @Column(name = "is_adapter")
    private Boolean isAdapter = false;

    @Column(name = "is_fine_tuned")
    private Boolean isFineTuned = false;

    @Column(name = "is_codeflowx_trained")
    private Boolean isCodeflowXTrained = false;

    @Column(name = "is_marketplace_model")
    private Boolean isMarketplaceModel = false;

    @Column(name = "marketplace_source")
    private String marketplaceSource; // CivitAI, HuggingFace, etc.

    @Column(name = "marketplace_model_id")
    private String marketplaceModelId; // ID del modelo en el marketplace

    @Column(name = "mlflow_run_id")
    private String mlflowRunId;

    @Column(name = "mlflow_model_uri")
    private String mlflowModelUri;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ModelStatus status;

    @Column(name = "tags")
    private String tags; // JSON array of tags

    @Column(name = "metadata")
    private String metadata; // JSON metadata

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
    private List<modelversion> versions = new ArrayList&lt;&gt;();

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
    private List<modelartifact> artifacts = new ArrayList&lt;&gt;();

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
    private List<modeldeployment> deployments = new ArrayList&lt;&gt;();

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
    private List<modelcost> costs = new ArrayList&lt;&gt;();

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
    private List<modelhardwarerequirement> hardwareRequirements = new ArrayList&lt;&gt;();

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
    private List<modelcompliance> compliance = new ArrayList&lt;&gt;();

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
    private List<modelusagestatistics> usageStatistics = new ArrayList&lt;&gt;();
}

public enum ModelType {
    MULTIMODAL, TEXT, AUDIO, VISION, EMBEDDING, GENERATIVE, DISCRIMINATIVE,
    CLASSIFICATION, REGRESSION, CLUSTERING, DEEP_LEARNING, NLP, COMPUTER_VISION,
    RECOMMENDATION, ANOMALY_DETECTION, TIME_SERIES, REINFORCEMENT_LEARNING
}

public enum ModelCategory {
    LANGUAGE_MODEL, VISION_MODEL, AUDIO_MODEL, MULTIMODAL_MODEL, EMBEDDING_MODEL,
    CLASSIFICATION_MODEL, REGRESSION_MODEL, GENERATIVE_MODEL, DISCRIMINATIVE_MODEL,
    REINFORCEMENT_MODEL, TRANSFORMER_MODEL, CNN_MODEL, RNN_MODEL, GAN_MODEL
}

public enum ModelStatus {
    DRAFT, TRAINING, EVALUATING, APPROVED, REJECTED, DEPRECATED, ARCHIVED,
    DOWNLOADING, PROCESSING, READY, ERROR
}
```

### 4\. Model Cost (Costes del Modelo)

```java
@Entity
@Table(name = "mdl_model_costs")
public class ModelCost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_id", nullable = false)
    private Long modelId;

    @Column(name = "cost_type")
    @Enumerated(EnumType.STRING)
    private CostType costType;

    @Column(name = "input_cost_per_token")
    private BigDecimal inputCostPerToken; // Coste por token de entrada

    @Column(name = "output_cost_per_token")
    private BigDecimal outputCostPerToken; // Coste por token de salida

    @Column(name = "cost_per_image")
    private BigDecimal costPerImage; // Coste por imagen (para modelos de visión)

    @Column(name = "cost_per_audio_minute")
    private BigDecimal costPerAudioMinute; // Coste por minuto de audio

    @Column(name = "cost_per_api_call")
    private BigDecimal costPerApiCall; // Coste por llamada API

    @Column(name = "currency")
    private String currency = "USD";

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "model_id", insertable = false, updatable = false)
    private Model model;
}

public enum CostType {
    INPUT_TOKENS, OUTPUT_TOKENS, IMAGES, AUDIO, API_CALLS, STORAGE, TRAINING, INFERENCE
}
```

### 5\. Model Pricing (Precios de Venta)

```java
@Entity
@Table(name = "mdl_model_pricing")
public class ModelPricing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_id", nullable = false)
    private Long modelId;

    @Column(name = "pricing_tier")
    @Enumerated(EnumType.STRING)
    private PricingTier pricingTier;

    @Column(name = "price_per_token")
    private BigDecimal pricePerToken;

    @Column(name = "price_per_image")
    private BigDecimal pricePerImage;

    @Column(name = "price_per_api_call")
    private BigDecimal pricePerApiCall;

    @Column(name = "monthly_subscription_price")
    private BigDecimal monthlySubscriptionPrice;

    @Column(name = "annual_subscription_price")
    private BigDecimal annualSubscriptionPrice;

    @Column(name = "currency")
    private String currency = "USD";

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "model_id", insertable = false, updatable = false)
    private Model model;
}

public enum PricingTier {
    FREE, BASIC, PROFESSIONAL, ENTERPRISE, CUSTOM
}
```

### 6\. Model Hardware Requirement (Requisitos de Hardware)

```java
@Entity
@Table(name = "mdl_hardware_requirements")
public class ModelHardwareRequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_id", nullable = false)
    private Long modelId;

    @Column(name = "requirement_type")
    @Enumerated(EnumType.STRING)
    private RequirementType requirementType;

    @Column(name = "min_cpu_cores")
    private Integer minCpuCores;

    @Column(name = "recommended_cpu_cores")
    private Integer recommendedCpuCores;

    @Column(name = "min_ram_gb")
    private Integer minRamGB;

    @Column(name = "recommended_ram_gb")
    private Integer recommendedRamGB;

    @Column(name = "min_gpu_memory_gb")
    private Integer minGpuMemoryGB;

    @Column(name = "recommended_gpu_memory_gb")
    private Integer recommendedGpuMemoryGB;

    @Column(name = "gpu_type")
    private String gpuType; // RTX 4090, A100, etc.

    @Column(name = "min_storage_gb")
    private Integer minStorageGB;

    @Column(name = "recommended_storage_gb")
    private Integer recommendedStorageGB;

    @Column(name = "network_bandwidth_mbps")
    private Integer networkBandwidthMbps;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "model_id", insertable = false, updatable = false)
    private Model model;
}

public enum RequirementType {
    MINIMUM, RECOMMENDED, OPTIMAL
}
```

### 7\. Model Compliance (Cumplimiento Normativo)

```java
@Entity
@Table(name = "mdl_model_compliance")
public class ModelCompliance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_id", nullable = false)
    private Long modelId;

    @Column(name = "compliance_type")
    @Enumerated(EnumType.STRING)
    private ComplianceType complianceType;

    @Column(name = "compliance_status")
    @Enumerated(EnumType.STRING)
    private ComplianceStatus complianceStatus;

    @Column(name = "certification_date")
    private LocalDateTime certificationDate;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "certification_body")
    private String certificationBody;

    @Column(name = "certification_id")
    private String certificationId;

    @Column(name = "compliance_details")
    private String complianceDetails; // JSON con detalles del cumplimiento

    @Column(name = "audit_report_url")
    private String auditReportUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "model_id", insertable = false, updatable = false)
    private Model model;
}

public enum ComplianceType {
    AI_ACT_EUROPEAN, GDPR, HIPAA, SOC2, ISO27001, NIST, FAIR_CREDIT,
    CONSUMER_PROTECTION, PRIVACY_SHIELD, LOCAL_REGULATIONS
}

public enum ComplianceStatus {
    NOT_ASSESSED, UNDER_REVIEW, COMPLIANT, NON_COMPLIANT, CONDITIONALLY_COMPLIANT,
    EXPIRED, REVOKED, PENDING_UPDATE
}
```

### 8\. Model Usage Statistics (Estadísticas de Uso)

```java
@Entity
@Table(name = "mdl_usage_statistics")
public class ModelUsageStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_id", nullable = false)
    private Long modelId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "total_requests")
    private Long totalRequests = 0L;

    @Column(name = "successful_requests")
    private Long successfulRequests = 0L;

    @Column(name = "failed_requests")
    private Long failedRequests = 0L;

    @Column(name = "total_tokens_processed")
    private Long totalTokensProcessed = 0L;

    @Column(name = "total_images_processed")
    private Long totalImagesProcessed = 0L;

    @Column(name = "total_audio_minutes")
    private Long totalAudioMinutes = 0L;

    @Column(name = "average_response_time_ms")
    private Double averageResponseTimeMs;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Column(name = "total_revenue")
    private BigDecimal totalRevenue;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "model_id", insertable = false, updatable = false)
    private Model model;
}
```

### 9\. Marketplace Model (Modelo del Marketplace)

```java
@Entity
@Table(name = "mdl_marketplace_models")
public class MarketplaceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "marketplace_source", nullable = false)
    private String marketplaceSource; // CivitAI, HuggingFace, etc.

    @Column(name = "marketplace_model_id", nullable = false)
    private String marketplaceModelId;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "model_description")
    private String modelDescription;

    @Column(name = "model_type")
    private String modelType;

    @Column(name = "framework")
    private String framework;

    @Column(name = "download_url")
    private String downloadUrl;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "checksum")
    private String checksum;

    @Column(name = "tags")
    private String tags; // JSON array de tags del marketplace

    @Column(name = "rating")
    private Double rating;

    @Column(name = "download_count")
    private Long downloadCount;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "is_downloaded")
    private Boolean isDownloaded = false;

    @Column(name = "downloaded_at")
    private LocalDateTime downloadedAt;

    @Column(name = "local_model_id")
    private Long localModelId; // Referencia a mdl_models.id si se descargó

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

### 10\. Model Download (Descarga de Modelos)

```java
@Entity
@Table(name = "mdl_model_downloads")
public class ModelDownload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "marketplace_model_id", nullable = false)
    private Long marketplaceModelId;

    @Column(name = "user_id", nullable = false)
    private Long userId; // Referencia a cor_users.id

    @Column(name = "download_status")
    @Enumerated(EnumType.STRING)
    private DownloadStatus downloadStatus;

    @Column(name = "download_progress")
    private Integer downloadProgress; // 0-100%

    @Column(name = "download_started_at")
    private LocalDateTime downloadStartedAt;

    @Column(name = "download_completed_at")
    private LocalDateTime downloadCompletedAt;

    @Column(name = "download_duration_seconds")
    private Long downloadDurationSeconds;

    @Column(name = "file_path")
    private String filePath; // Ruta local del archivo descargado

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "marketplace_model_id", insertable = false, updatable = false)
    private MarketplaceModel marketplaceModel;
}

public enum DownloadStatus {
    PENDING, DOWNLOADING, COMPLETED, FAILED, CANCELLED, PROCESSING
}
```

### 11\. Model Version (Versión del Modelo)

```java
@Entity
@Table(name = "mdl_model_versions")
public class ModelVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_id", nullable = false)
    private Long modelId;

    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "description")
    private String description;

    @Column(name = "mlflow_version_id")
    private String mlflowVersionId;

    @Column(name = "stage")
    @Enumerated(EnumType.STRING)
    private ModelStage stage;

    @Column(name = "metrics")
    private String metrics; // JSON metrics

    @Column(name = "parameters")
    private String parameters; // JSON parameters

    @Column(name = "signature")
    private String signature; // JSON model signature

    @Column(name = "input_example")
    private String inputExample; // JSON input example

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "transitioned_at")
    private LocalDateTime transitionedAt;

    @OneToMany(mappedBy = "modelVersion", cascade = CascadeType.ALL)
    private List<modelartifact> artifacts = new ArrayList&lt;&gt;();
}

public enum ModelStage {
    NONE, STAGING, PRODUCTION, ARCHIVED
}
```

### 12\. Model Artifact (Artefacto del Modelo)

```java
@Entity
@Table(name = "mdl_model_artifacts")
public class ModelArtifact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_id", nullable = false)
    private Long modelId;

    @Column(name = "model_version_id")
    private Long modelVersionId;

    @Column(name = "artifact_type")
    @Enumerated(EnumType.STRING)
    private ArtifactType artifactType;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "checksum")
    private String checksum;

    @Column(name = "mlflow_artifact_uri")
    private String mlflowArtifactUri;

    @Column(name = "metadata")
    private String metadata; // JSON metadata

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

public enum ArtifactType {
    MODEL_FILE, CONFIG_FILE, PREPROCESSOR, FEATURE_EXTRACTOR, EVALUATION_RESULTS,
    TRAINING_LOGS, VISUALIZATIONS, DOCUMENTATION, TEST_DATA, SAMPLE_OUTPUTS
}
```

### 13\. Model Experiment (Experimento del Modelo)

```java
@Entity
@Table(name = "mdl_model_experiments")
public class ModelExperiment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_id", nullable = false)
    private Long modelId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "mlflow_experiment_id")
    private String mlflowExperimentId;

    @Column(name = "experiment_type")
    @Enumerated(EnumType.STRING)
    private ExperimentType experimentType;

    @Column(name = "objective")
    private String objective;

    @Column(name = "hypothesis")
    private String hypothesis;

    @Column(name = "methodology")
    private String methodology;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ExperimentStatus status;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "results")
    private String results; // JSON results

    @Column(name = "conclusions")
    private String conclusions;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "experiment", cascade = CascadeType.ALL)
    private List<experimentrun> runs = new ArrayList&lt;&gt;();
}

public enum ExperimentType {
    HYPERPARAMETER_OPTIMIZATION, ARCHITECTURE_SEARCH, FEATURE_ENGINEERING,
    MODEL_COMPARISON, AB_TESTING, TRANSFER_LEARNING, FEW_SHOT_LEARNING
}

public enum ExperimentStatus {
    PLANNED, RUNNING, COMPLETED, FAILED, CANCELLED, PAUSED
}
```

### 14\. Experiment Run (Ejecución del Experimento)

```java
@Entity
@Table(name = "mdl_experiment_runs")
public class ExperimentRun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "experiment_id", nullable = false)
    private Long experimentId;

    @Column(name = "mlflow_run_id")
    private String mlflowRunId;

    @Column(name = "run_name")
    private String runName;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RunStatus status;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(name = "duration_seconds")
    private Long durationSeconds;

    @Column(name = "parameters")
    private String parameters; // JSON parameters

    @Column(name = "metrics")
    private String metrics; // JSON metrics

    @Column(name = "tags")
    private String tags; // JSON tags

    @Column(name = "artifacts")
    private String artifacts; // JSON artifact paths

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum RunStatus {
    RUNNING, SCHEDULED, FINISHED, FAILED, KILLED
}
```

### 15\. Model Deployment (Despliegue del Modelo)

```java
@Entity
@Table(name = "mdl_model_deployments")
public class ModelDeployment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_id", nullable = false)
    private Long modelId;

    @Column(name = "model_version_id")
    private Long modelVersionId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "deployment_type")
    @Enumerated(EnumType.STRING)
    private DeploymentType deploymentType;

    @Column(name = "environment")
    private String environment; // dev, staging, prod

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private DeploymentStatus status;

    @Column(name = "endpoint_url")
    private String endpointUrl;

    @Column(name = "health_check_url")
    private String healthCheckUrl;

    @Column(name = "configuration")
    private String configuration; // JSON deployment config

    @Column(name = "resources")
    private String resources; // JSON resource requirements

    @Column(name = "scaling_config")
    private String scalingConfig; // JSON scaling configuration

    @Column(name = "deployed_at")
    private LocalDateTime deployedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "deployment", cascade = CascadeType.ALL)
    private List<deploymentmetric> metrics = new ArrayList&lt;&gt;();
}

public enum DeploymentType {
    REST_API, GRPC, BATCH_INFERENCE, STREAMING, EDGE, CONTAINER, SERVERLESS
}

public enum DeploymentStatus {
    PENDING, DEPLOYING, RUNNING, FAILED, STOPPED, SCALING, UPDATING
}
```

### 16\. Deployment Metric (Métrica del Despliegue)

```java
@Entity
@Table(name = "mdl_deployment_metrics")
public class DeploymentMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deployment_id", nullable = false)
    private Long deploymentId;

    @Column(name = "metric_name", nullable = false)
    private String metricName;

    @Column(name = "metric_value")
    private Double metricValue;

    @Column(name = "metric_unit")
    private String metricUnit;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "labels")
    private String labels; // JSON labels

    @Column(name = "metadata")
    private String metadata; // JSON additional info
}
```

## Sistema de Búsqueda y Marketplace

### Marketplace Search Service

```java
@Service
public class MarketplaceSearchService {

    @Autowired
    private LekServerClient lekServerClient;

    @Autowired
    private MarketplaceModelRepository marketplaceModelRepository;

    @Autowired
    private ModelDownloadRepository modelDownloadRepository;

    @Autowired
    private AuditService auditService;

    /**
     * Búsqueda de modelos en múltiples marketplaces
     */
    public SearchResult searchModels(MarketplaceSearchRequest request) {
        try {
            // Búsqueda a través de lek-server
            SearchResult result = lekServerClient.searchModels(request);

            // Guardar resultados en base de datos local
            saveSearchResults(result.getModels());

            // Registrar auditoría
            auditService.logAction(
                getCurrentUsername(),
                "MARKETPLACE_SEARCH",
                "MARKETPLACE_SEARCH",
                request.getQuery()
            );

            return result;
        } catch (Exception e) {
            throw new MarketplaceSearchException("Error searching models: " + e.getMessage(), e);
        }
    }

    /**
     * Descarga automática de un modelo del marketplace
     */
    public ModelDownload downloadModel(Long marketplaceModelId, Long userId) {
        try {
            MarketplaceModel marketplaceModel = marketplaceModelRepository.findById(marketplaceModelId)
                .orElseThrow(() -&gt; new MarketplaceModelNotFoundException("Marketplace model not found"));

            // Crear registro de descarga
            ModelDownload download = new ModelDownload();
            download.setMarketplaceModelId(marketplaceModelId);
            download.setUserId(userId);
            download.setDownloadStatus(DownloadStatus.PENDING);
            download.setDownloadStartedAt(LocalDateTime.now());
            download.setCreatedAt(LocalDateTime.now());

            ModelDownload savedDownload = modelDownloadRepository.save(download);

            // Iniciar descarga asíncrona
            CompletableFuture.runAsync(() -&gt; {
                performModelDownload(savedDownload.getId(), marketplaceModel);
            });

            return savedDownload;
        } catch (Exception e) {
            throw new ModelDownloadException("Error starting download: " + e.getMessage(), e);
        }
    }

    /**
     * Descarga automática del modelo
     */
    private void performModelDownload(Long downloadId, MarketplaceModel marketplaceModel) {
        try {
            ModelDownload download = modelDownloadRepository.findById(downloadId)
                .orElseThrow(() -&gt; new ModelDownloadNotFoundException("Download not found"));

            // Actualizar estado a descargando
            download.setDownloadStatus(DownloadStatus.DOWNLOADING);
            download.setDownloadProgress(0);
            modelDownloadRepository.save(download);

            // Descargar modelo a través de lek-server
            String localFilePath = lekServerClient.downloadModel(
                marketplaceModel.getMarketplaceSource(),
                marketplaceModel.getMarketplaceModelId(),
                download.getUserId()
            );

            // Actualizar estado a completado
            download.setDownloadStatus(DownloadStatus.COMPLETED);
            download.setDownloadProgress(100);
            download.setDownloadCompletedAt(LocalDateTime.now());
            download.setFilePath(localFilePath);
            download.setDownloadDurationSeconds(
                Duration.between(download.getDownloadStartedAt(), LocalDateTime.now()).getSeconds()
            );
            modelDownloadRepository.save(download);

            // Marcar modelo del marketplace como descargado
            marketplaceModel.setIsDownloaded(true);
            marketplaceModel.setDownloadedAt(LocalDateTime.now());
            marketplaceModelRepository.save(marketplaceModel);

        } catch (Exception e) {
            // Actualizar estado a fallido
            ModelDownload download = modelDownloadRepository.findById(downloadId).orElse(null);
            if (download != null) {
                download.setDownloadStatus(DownloadStatus.FAILED);
                download.setErrorMessage(e.getMessage());
                modelDownloadRepository.save(download);
            }
        }
    }

    /**
     * Obtener proveedores disponibles
     */
    public List<providerinfo> getAvailableProviders() {
        return Arrays.asList(
            new ProviderInfo("CivitAI", "+1,500,000", "Stable Diffusion, Midjourney, Audio, Video, 3D"),
            new ProviderInfo("HuggingFace", "+1,900,000", "BERT, GPT, T5, Stable Diffusion"),
            new ProviderInfo("Google AI", "+100,000", "Gemini, PaLM, TensorFlow Hub"),
            new ProviderInfo("Torch Hub", "+50,000", "Computer Vision, NLP, Audio"),
            new ProviderInfo("NVIDIA NGC", "+25,000", "GPU-optimized, TensorRT ready"),
            new ProviderInfo("Intel OpenVINO", "+15,000", "Intel CPU/GPU/XPU, Core Ultra NPU"),
            new ProviderInfo("ONNX Model Zoo", "+10,000", "Multi-framework, ONNX optimized")
        );
    }

    private String getCurrentUsername() {
        return "current_user"; // Placeholder
    }
}

@Data
@AllArgsConstructor
public class ProviderInfo {
    private String name;
    private String modelCount;
    private String characteristics;
}

@Data
public class MarketplaceSearchRequest {
    private String query;
    private List<string> providers;
    private List<string> modelTypes;
    private List<string> frameworks;
    private String sortBy; // rating, downloads, date
    private String sortOrder; // asc, desc
    private Integer page;
    private Integer pageSize;
}

@Data
public class SearchResult {
    private List<marketplacemodel> models;
    private Integer totalCount;
    private Integer page;
    private Integer pageSize;
    private Integer totalPages;
}
```

## API Endpoints

### AI Provider Management

```plaintext
GET    /api/v1/providers
GET    /api/v1/providers/{id}
POST   /api/v1/providers
PUT    /api/v1/providers/{id}
DELETE /api/v1/providers/{id}
GET    /api/v1/providers/{id}/tokens
POST   /api/v1/providers/{id}/tokens
PUT    /api/v1/providers/{id}/tokens/{tokenId}
DELETE /api/v1/providers/{id}/tokens/{tokenId}
GET    /api/v1/providers/{id}/models
```

### Provider Token Management

```plaintext
GET    /api/v1/tokens
GET    /api/v1/tokens/{id}
POST   /api/v1/tokens
PUT    /api/v1/tokens/{id}
DELETE /api/v1/tokens/{id}
POST   /api/v1/tokens/{id}/validate
POST   /api/v1/tokens/{id}/rotate
```

### Model Management

```plaintext
GET    /api/v1/models
GET    /api/v1/models/{id}
POST   /api/v1/models
PUT    /api/v1/models/{id}
DELETE /api/v1/models/{id}
GET    /api/v1/models/{id}/versions
GET    /api/v1/models/{id}/artifacts
GET    /api/v1/models/{id}/deployments
GET    /api/v1/models/{id}/costs
GET    /api/v1/models/{id}/pricing
GET    /api/v1/models/{id}/hardware-requirements
GET    /api/v1/models/{id}/compliance
GET    /api/v1/models/{id}/usage-statistics
POST   /api/v1/models/{id}/register
POST   /api/v1/models/{id}/approve
POST   /api/v1/models/{id}/reject
```

### Model Costs and Pricing

```plaintext
GET    /api/v1/models/{id}/costs
POST   /api/v1/models/{id}/costs
PUT    /api/v1/models/{id}/costs/{costId}
DELETE /api/v1/models/{id}/costs/{costId}

GET    /api/v1/models/{id}/pricing
POST   /api/v1/models/{id}/pricing
PUT    /api/v1/models/{id}/pricing/{pricingId}
DELETE /api/v1/models/{id}/pricing/{pricingId}
```

### Model Hardware Requirements

```plaintext
GET    /api/v1/models/{id}/hardware-requirements
POST   /api/v1/models/{id}/hardware-requirements
PUT    /api/v1/models/{id}/hardware-requirements/{reqId}
DELETE /api/v1/models/{id}/hardware-requirements/{reqId}
```

### Model Compliance

```plaintext
GET    /api/v1/models/{id}/compliance
POST   /api/v1/models/{id}/compliance
PUT    /api/v1/models/{id}/compliance/{complianceId}
DELETE /api/v1/models/{id}/compliance/{complianceId}
POST   /api/v1/models/{id}/compliance/{complianceId}/assess
```

### Model Usage Statistics

```plaintext
GET    /api/v1/models/{id}/usage-statistics
GET    /api/v1/models/{id}/usage-statistics/date-range
GET    /api/v1/models/{id}/usage-statistics/export
```

### Model Versions

```plaintext
GET    /api/v1/models/{id}/versions
GET    /api/v1/models/{id}/versions/{versionId}
POST   /api/v1/models/{id}/versions
PUT    /api/v1/models/{id}/versions/{versionId}
DELETE /api/v1/models/{id}/versions/{versionId}
POST   /api/v1/models/{id}/versions/{versionId}/transition
GET    /api/v1/models/{id}/versions/{versionId}/artifacts
```

### Model Experiments

```plaintext
GET    /api/v1/models/{id}/experiments
GET    /api/v1/models/{id}/experiments/{experimentId}
POST   /api/v1/models/{id}/experiments
PUT    /api/v1/models/{id}/experiments/{experimentId}
DELETE /api/v1/models/{id}/experiments/{experimentId}
POST   /api/v1/models/{id}/experiments/{experimentId}/start
POST   /api/v1/models/{id}/experiments/{experimentId}/complete
GET    /api/v1/models/{id}/experiments/{experimentId}/runs
```

### Model Deployments

```plaintext
GET    /api/v1/models/{id}/deployments
GET    /api/v1/models/{id}/deployments/{deploymentId}
POST   /api/v1/models/{id}/deployments
PUT    /api/v1/models/{id}/deployments/{deploymentId}
DELETE /api/v1/models/{id}/deployments/{deploymentId}
POST   /api/v1/models/{id}/deployments/{deploymentId}/start
POST   /api/v1/models/{id}/deployments/{deploymentId}/stop
POST   /api/v1/models/{id}/deployments/{deploymentId}/scale
GET    /api/v1/models/{id}/deployments/{deploymentId}/metrics
```

### Marketplace Search and Download

```plaintext
GET    /api/v1/marketplace/search
GET    /api/v1/marketplace/providers
GET    /api/v1/marketplace/models
GET    /api/v1/marketplace/models/{id}
POST   /api/v1/marketplace/models/{id}/download
GET    /api/v1/marketplace/downloads
GET    /api/v1/marketplace/downloads/{downloadId}
POST   /api/v1/marketplace/downloads/{downloadId}/cancel
```

### Model Search and Discovery

```plaintext
GET    /api/v1/models/search
GET    /api/v1/models/tags
GET    /api/v1/models/frameworks
GET    /api/v1/models/types
GET    /api/v1/models/categories
POST   /api/v1/models/compare
GET    /api/v1/models/recommendations
GET    /api/v1/models/trending
```

## Integración con MLflow

### MLflowService

```java
@Service
public class MLflowService {

    @Autowired
    private MLflowClient mlflowClient;

    @Value("${mlflow.tracking-uri}")
    private String trackingUri;

    @Value("${mlflow.experiment-name}")
    private String experimentName;

    @Autowired
    private AuditService auditService;

    public String createExperiment(String name, String description) {
        try {
            CreateExperiment createExperiment = CreateExperiment.newBuilder()
                .name(name)
                .build();

            CreateExperiment.Response response = mlflowClient.createExperiment(createExperiment);
            String experimentId = response.getExperimentId();

            // Registrar auditoría
            auditService.logAction(
                getCurrentUsername(),
                "CREATE_MLFLOW_EXPERIMENT",
                "MLFLOW_EXPERIMENT",
                experimentId
            );

            return experimentId;
        } catch (Exception e) {
            throw new MLflowException("Failed to create experiment: " + e.getMessage(), e);
        }
    }

    public String createRun(String experimentId, String runName) {
        try {
            CreateRun createRun = CreateRun.newBuilder()
                .experimentId(experimentId)
                .runName(runName)
                .build();

            CreateRun.Response response = mlflowClient.createRun(createRun);
            String runId = response.getRun().getRunId();

            // Registrar auditoría
            auditService.logAction(
                getCurrentUsername(),
                "CREATE_MLFLOW_RUN",
                "MLFLOW_RUN",
                runId
            );

            return runId;
        } catch (Exception e) {
            throw new MLflowException("Failed to create run: " + e.getMessage(), e);
        }
    }

    public void logParameters(String runId, Map<string, string=""> parameters) {
        try {
            for (Map.Entry<string, string=""> entry : parameters.entrySet()) {
                LogParam logParam = LogParam.newBuilder()
                    .runId(runId)
                    .key(entry.getKey())
                    .value(entry.getValue())
                    .build();

                mlflowClient.logParam(logParam);
            }
        } catch (Exception e) {
            throw new MLflowException("Failed to log parameters: " + e.getMessage(), e);
        }
    }

    public void logMetrics(String runId, Map<string, double=""> metrics) {
        try {
            for (Map.Entry<string, double=""> entry : metrics.entrySet()) {
                LogMetric logMetric = LogMetric.newBuilder()
                    .runId(runId)
                    .key(entry.getKey())
                    .value(entry.getValue())
                    .build();

                mlflowClient.logMetric(logMetric);
            }
        } catch (Exception e) {
            throw new MLflowException("Failed to log metrics: " + e.getMessage(), e);
        }
    }

    public void logArtifact(String runId, String artifactPath, File artifact) {
        try {
            mlflowClient.logArtifact(runId, artifact, artifactPath);
        } catch (Exception e) {
            throw new MLflowException("Failed to log artifact: " + e.getMessage(), e);
        }
    }

    public void registerModel(String runId, String modelName, String modelPath) {
        try {
            ModelVersion modelVersion = mlflowClient.createModelVersion(
                modelName, modelPath, runId
            );

            // Registrar auditoría
            auditService.logAction(
                getCurrentUsername(),
                "REGISTER_MLFLOW_MODEL",
                "MLFLOW_MODEL",
                modelVersion.getVersion()
            );
        } catch (Exception e) {
            throw new MLflowException("Failed to register model: " + e.getMessage(), e);
        }
    }

    public void transitionModelStage(String modelName, String version, String stage) {
        try {
            mlflowClient.transitionModelVersionStage(modelName, version, stage);

            // Registrar auditoría
            auditService.logAction(
                getCurrentUsername(),
                "TRANSITION_MODEL_STAGE",
                "MLFLOW_MODEL",
                modelName + ":" + version + " -&gt; " + stage
            );
        } catch (Exception e) {
            throw new MLflowException("Failed to transition model stage: " + e.getMessage(), e);
        }
    }

    private String getCurrentUsername() {
        return "current_user"; // Placeholder
    }
}
```

## Servicios de Model Management

### ModelService

```java
@Service
@Transactional
public class ModelService {

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private MLflowService mlflowService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private ModelManagementMetrics metrics;

    public Model createModel(ModelDto modelDto) {
        Timer.Sample timer = metrics.startModelCreationTimer();

        try {
            // Validar datos del modelo
            validateModelData(modelDto);

            // Crear modelo
            Model model = new Model();
            model.setModelName(modelDto.getModelName());
            model.setModelDescription(modelDto.getModelDescription());
            model.setProjectId(modelDto.getProjectId());
            model.setUserId(getCurrentUserId());
            model.setProviderId(modelDto.getProviderId());
            model.setModelType(modelDto.getModelType());
            model.setModelCategory(modelDto.getModelCategory());
            model.setFramework(modelDto.getFramework());
            model.setVersion("1.0.0");
            model.setIsAdapter(modelDto.getIsAdapter());
            model.setIsFineTuned(modelDto.getIsFineTuned());
            model.setIsCodeflowXTrained(modelDto.getIsCodeflowXTrained());
            model.setIsMarketplaceModel(modelDto.getIsMarketplaceModel());
            model.setMarketplaceSource(modelDto.getMarketplaceSource());
            model.setMarketplaceModelId(modelDto.getMarketplaceModelId());
            model.setStatus(ModelStatus.DRAFT);
            model.setTags(modelDto.getTags());
            model.setMetadata(modelDto.getMetadata());
            model.setCreatedAt(LocalDateTime.now());

            Model savedModel = modelRepository.save(model);

            // Incrementar métricas
            metrics.incrementModelsCreated();

            // Registrar auditoría
            auditService.logAction(
                getCurrentUsername(),
                "CREATE_MODEL",
                "MODEL",
                savedModel.getId().toString()
            );

            return savedModel;
        } finally {
            timer.stop();
        }
    }

    public ModelVersion createModelVersion(Long modelId, ModelVersionDto versionDto) {
        Model model = modelRepository.findById(modelId)
            .orElseThrow(() -&gt; new ModelNotFoundException("Model not found"));

        // Crear versión del modelo
        ModelVersion version = new ModelVersion();
        version.setModelId(modelId);
        version.setVersion(versionDto.getVersion());
        version.setDescription(versionDto.getDescription());
        version.setStage(ModelStage.NONE);
        version.setParameters(versionDto.getParameters());
        version.setCreatedAt(LocalDateTime.now());

        ModelVersion savedVersion = modelVersionRepository.save(version);

        // Incrementar métricas
        metrics.incrementVersionsCreated();

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "CREATE_MODEL_VERSION",
            "MODEL_VERSION",
            savedVersion.getId().toString()
        );

        return savedVersion;
    }

    public ModelExperiment createExperiment(Long modelId, ModelExperimentDto experimentDto) {
        Model model = modelRepository.findById(modelId)
            .orElseThrow(() -&gt; new ModelNotFoundException("Model not found"));

        // Crear experimento en MLflow
        String mlflowExperimentId = mlflowService.createExperiment(
            experimentDto.getName(),
            experimentDto.getDescription()
        );

        // Crear experimento en base de datos
        ModelExperiment experiment = new ModelExperiment();
        experiment.setModelId(modelId);
        experiment.setName(experimentDto.getName());
        experiment.setDescription(experimentDto.getDescription());
        experiment.setMlflowExperimentId(mlflowExperimentId);
        experiment.setExperimentType(experimentDto.getExperimentType());
        experiment.setObjective(experimentDto.getObjective());
        experiment.setHypothesis(experimentDto.getHypothesis());
        experiment.setMethodology(experimentDto.getMethodology());
        experiment.setStatus(ExperimentStatus.PLANNED);
        experiment.setCreatedAt(LocalDateTime.now());

        ModelExperiment savedExperiment = experimentRepository.save(experiment);

        // Incrementar métricas
        metrics.incrementExperimentsCreated();

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "CREATE_MODEL_EXPERIMENT",
            "MODEL_EXPERIMENT",
            savedExperiment.getId().toString()
        );

        return savedExperiment;
    }

    private void validateModelData(ModelDto modelDto) {
        if (modelDto.getModelName() == null || modelDto.getModelName().trim().isEmpty()) {
            throw new ValidationException("Model name is required");
        }

        if (modelDto.getModelType() == null) {
            throw new ValidationException("Model type is required");
        }

        if (modelDto.getFramework() == null || modelDto.getFramework().trim().isEmpty()) {
            throw new ValidationException("Framework is required");
        }
    }

    private Long getCurrentUserId() {
        return 1L; // Placeholder
    }

    private String getCurrentUsername() {
        return "current_user"; // Placeholder
    }
}
```

### AIProviderService

```java
@Service
@Transactional
public class AIProviderService {

    @Autowired
    private AIProviderRepository providerRepository;

    @Autowired
    private ProviderTokenRepository tokenRepository;

    @Autowired
    private AuditService auditService;

    public AIProvider createProvider(AIProviderDto providerDto) {
        // Validar datos del proveedor
        validateProviderData(providerDto);

        // Crear proveedor
        AIProvider provider = new AIProvider();
        provider.setProviderName(providerDto.getProviderName());
        provider.setProviderType(providerDto.getProviderType());
        provider.setApiBaseUrl(providerDto.getApiBaseUrl());
        provider.setServiceEndpoint(providerDto.getServiceEndpoint());
        provider.setRateLimitPerMinute(providerDto.getRateLimitPerMinute());
        provider.setMaxConcurrentRequests(providerDto.getMaxConcurrentRequests());
        provider.setSupportedModelTypes(providerDto.getSupportedModelTypes());
        provider.setProviderMetadata(providerDto.getProviderMetadata());
        provider.setIsActive(true);
        provider.setCreatedAt(LocalDateTime.now());

        AIProvider savedProvider = providerRepository.save(provider);

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "CREATE_AI_PROVIDER",
            "AI_PROVIDER",
            savedProvider.getId().toString()
        );

        return savedProvider;
    }

    public ProviderToken createToken(Long providerId, ProviderTokenDto tokenDto) {
        // Validar que el proveedor existe
        AIProvider provider = providerRepository.findById(providerId)
            .orElseThrow(() -&gt; new AIProviderNotFoundException("Provider not found"));

        // Crear token
        ProviderToken token = new ProviderToken();
        token.setProviderId(providerId);
        token.setTokenName(tokenDto.getTokenName());
        token.setTokenValue(encryptToken(tokenDto.getTokenValue()));
        token.setEncryptionKeyId(tokenDto.getEncryptionKeyId());
        token.setExpiresAt(tokenDto.getExpiresAt());
        token.setUsageLimit(tokenDto.getUsageLimit());
        token.setIsActive(true);
        token.setCreatedBy(getCurrentUserId());
        token.setCreatedAt(LocalDateTime.now());

        ProviderToken savedToken = tokenRepository.save(token);

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "CREATE_PROVIDER_TOKEN",
            "PROVIDER_TOKEN",
            savedToken.getId().toString()
        );

        return savedToken;
    }

    private void validateProviderData(AIProviderDto providerDto) {
        if (providerDto.getProviderName() == null || providerDto.getProviderName().trim().isEmpty()) {
            throw new ValidationException("Provider name is required");
        }

        if (providerDto.getProviderType() == null) {
            throw new ValidationException("Provider type is required");
        }
    }

    private String encryptToken(String tokenValue) {
        // Implementar encriptación del token
        return "encrypted_" + tokenValue; // Placeholder
    }

    private Long getCurrentUserId() {
        return 1L; // Placeholder
    }

    private String getCurrentUsername() {
        return "current_user"; // Placeholder
    }
}
```

## Monitoreo y Métricas

### Prometheus Metrics

```java
@Component
public class ModelManagementMetrics {

    @Autowired
    private MeterRegistry meterRegistry;

    private final Counter modelsCreatedCounter;
    private final Counter versionsCreatedCounter;
    private final Counter experimentsCreatedCounter;
    private final Counter deploymentsCreatedCounter;
    private final Counter providersCreatedCounter;
    private final Counter tokensCreatedCounter;
    private final Counter marketplaceSearchesCounter;
    private final Counter modelDownloadsCounter;
    private final Timer modelCreationTimer;
    private final Timer experimentExecutionTimer;
    private final Timer marketplaceSearchTimer;
    private final Timer modelDownloadTimer;

    public ModelManagementMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.modelsCreatedCounter = Counter.builder("model.management.models.created")
            .description("Total models created")
            .register(meterRegistry);

        this.versionsCreatedCounter = Counter.builder("model.management.versions.created")
            .description("Total model versions created")
            .register(meterRegistry);

        this.experimentsCreatedCounter = Counter.builder("model.management.experiments.created")
            .description("Total experiments created")
            .register(meterRegistry);

        this.deploymentsCreatedCounter = Counter.builder("model.management.deployments.created")
            .description("Total deployments created")
            .register(meterRegistry);

        this.providersCreatedCounter = Counter.builder("model.management.providers.created")
            .description("Total AI providers created")
            .register(meterRegistry);

        this.tokensCreatedCounter = Counter.builder("model.management.tokens.created")
            .description("Total provider tokens created")
            .register(meterRegistry);

        this.marketplaceSearchesCounter = Counter.builder("model.management.marketplace.searches")
            .description("Total marketplace searches performed")
            .register(meterRegistry);

        this.modelDownloadsCounter = Counter.builder("model.management.marketplace.downloads")
            .description("Total models downloaded from marketplace")
            .register(meterRegistry);

        this.modelCreationTimer = Timer.builder("model.management.model.creation.time")
            .description("Model creation time")
            .register(meterRegistry);

        this.experimentExecutionTimer = Timer.builder("model.management.experiment.execution.time")
            .description("Experiment execution time")
            .register(meterRegistry);

        this.marketplaceSearchTimer = Timer.builder("model.management.marketplace.search.time")
            .description("Marketplace search time")
            .register(meterRegistry);

        this.modelDownloadTimer = Timer.builder("model.management.marketplace.download.time")
            .description("Model download time")
            .register(meterRegistry);
    }

    public void incrementModelsCreated() {
        modelsCreatedCounter.increment();
    }

    public void incrementVersionsCreated() {
        versionsCreatedCounter.increment();
    }

    public void incrementExperimentsCreated() {
        experimentsCreatedCounter.increment();
    }

    public void incrementDeploymentsCreated() {
        deploymentsCreatedCounter.increment();
    }

    public void incrementProvidersCreated() {
        providersCreatedCounter.increment();
    }

    public void incrementTokensCreated() {
        tokensCreatedCounter.increment();
    }

    public void incrementMarketplaceSearches() {
        marketplaceSearchesCounter.increment();
    }

    public void incrementModelDownloads() {
        modelDownloadsCounter.increment();
    }

    public Timer.Sample startModelCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startExperimentExecutionTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startMarketplaceSearchTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startModelDownloadTimer() {
        return Timer.start(meterRegistry);
    }
}
```

## Configuración de Aplicación

### application-model-management.yml

```plaintext
model-management:
  # Configuración de MLflow
  mlflow:
    tracking-uri: ${MLFLOW_TRACKING_URI:http://localhost:5000}
    experiment-name: ${MLFLOW_EXPERIMENT_NAME:codeflowx-models}
    model-registry-uri: ${MLFLOW_MODEL_REGISTRY_URI:http://localhost:5000}
    artifact-root: ${MLFLOW_ARTIFACT_ROOT:./mlruns}

  # Configuración de modelos
  models:
    default-version: "1.0.0"
    supported-frameworks:
      [
        "tensorflow",
        "pytorch",
        "scikit-learn",
        "xgboost",
        "lightgbm",
        "transformers",
      ]
    supported-types:
      [
        "multimodal",
        "text",
        "audio",
        "vision",
        "embedding",
        "generative",
        "discriminative",
      ]
    supported-categories:
      [
        "language_model",
        "vision_model",
        "audio_model",
        "multimodal_model",
        "embedding_model",
      ]
    max-file-size-mb: 1024
    auto-versioning: true
    auto-compliance-check: true

  # Configuración de proveedores de IA
  ai-providers:
    encryption-algorithm: "AES-256-GCM"
    key-rotation-days: 90
    token-validation-interval: 60
    max-tokens-per-provider: 10
    supported-providers:
      ["openai", "anthropic", "huggingface", "codeflowx", "self_hosting"]

  # Configuración de marketplaces
  marketplaces:
    lek-server-url: ${LEK_SERVER_URL:http://localhost:8080}
    search-timeout-seconds: 30
    download-timeout-minutes: 60
    max-concurrent-downloads: 5
    auto-cleanup-downloads: true
    download-retention-days: 7

  # Configuración de costes y precios
  pricing:
    default-currency: "USD"
    cost-tracking-enabled: true
    revenue-tracking-enabled: true
    auto-cost-calculation: true
    price-update-frequency: "daily"

  # Configuración de hardware
  hardware:
    auto-detection: true
    resource-monitoring: true
    performance-tracking: true
    scaling-recommendations: true

  # Configuración de cumplimiento
  compliance:
    auto-assessment: true
    certification-tracking: true
    audit-logging: true
    regulatory-updates: true

  # Configuración de experimentos
  experiments:
    max-duration-days: 30
    auto-cleanup: true
    retention-days: 365
    max-concurrent-runs: 10
    resource-limits:
      max-cpu-cores: 32
      max-ram-gb: 128
      max-gpu-count: 8

  # Configuración de despliegues
  deployments:
    default-environment: "dev"
    supported-types:
      [
        "rest_api",
        "grpc",
        "batch_inference",
        "streaming",
        "edge",
        "container",
        "serverless",
      ]
    auto-scaling: true
    health-check-interval: 30
    max-instances: 10
    min-instances: 1
    resource-limits:
      max-cpu-cores: 16
      max-ram-gb: 64
      max-gpu-count: 4

  # Configuración de métricas y monitoreo
  monitoring:
    prometheus-enabled: true
    grafana-dashboards: true
    alerting-enabled: true
    log-aggregation: true
    performance-tracking: true
    usage-analytics: true

  # Configuración de seguridad
  security:
    token-encryption: true
    audit-logging: true
    access-control: true
    data-privacy: true
    compliance-reporting: true
```

## Scripts de Base de Datos

### 1\. Script de Limpieza (DROP de Tablas, Índices, etc.)

```plaintext
-- =====================================================
-- SCRIPT DE LIMPIEZA - EJECUTAR PRIMERO
-- =====================================================

-- Eliminar funciones y procedimientos almacenados
DROP FUNCTION IF EXISTS update_updated_at_column() CASCADE;
DROP FUNCTION IF EXISTS log_model_action(text, text, text, text) CASCADE;
DROP FUNCTION IF EXISTS get_model_permissions(bigint) CASCADE;
DROP FUNCTION IF EXISTS check_model_access(bigint, text) CASCADE;

-- Eliminar triggers
DROP TRIGGER IF EXISTS update_mdl_models_updated_at ON mdl_models CASCADE;
DROP TRIGGER IF EXISTS update_mdl_model_versions_updated_at ON mdl_model_versions CASCADE;
DROP TRIGGER IF EXISTS update_mdl_model_experiments_updated_at ON mdl_model_experiments CASCADE;
DROP TRIGGER IF EXISTS update_mdl_experiment_runs_updated_at ON mdl_experiment_runs CASCADE;
DROP TRIGGER IF EXISTS update_mdl_model_deployments_updated_at ON mdl_model_deployments CASCADE;
DROP TRIGGER IF EXISTS update_mdl_ai_providers_updated_at ON mdl_ai_providers CASCADE;

-- Eliminar índices
DROP INDEX IF EXISTS idx_mdl_models_project_id CASCADE;
DROP INDEX IF EXISTS idx_mdl_models_user_id CASCADE;
DROP INDEX IF EXISTS idx_mdl_models_provider_id CASCADE;
DROP INDEX IF EXISTS idx_mdl_models_type CASCADE;
DROP INDEX IF EXISTS idx_mdl_models_category CASCADE;
DROP INDEX IF EXISTS idx_mdl_models_framework CASCADE;
DROP INDEX IF EXISTS idx_mdl_models_status CASCADE;
DROP INDEX IF EXISTS idx_mdl_models_marketplace CASCADE;

-- Eliminar tablas en orden correcto (dependencias)
DROP TABLE IF EXISTS mdl_deployment_metrics CASCADE;
DROP TABLE IF EXISTS mdl_model_deployments CASCADE;
DROP TABLE IF EXISTS mdl_model_artifacts CASCADE;
DROP TABLE IF EXISTS mdl_model_versions CASCADE;
DROP TABLE IF EXISTS mdl_experiment_runs CASCADE;
DROP TABLE IF EXISTS mdl_model_experiments CASCADE;
DROP TABLE IF EXISTS mdl_model_usage_statistics CASCADE;
DROP TABLE IF EXISTS mdl_model_compliance CASCADE;
DROP TABLE IF EXISTS mdl_hardware_requirements CASCADE;
DROP TABLE IF EXISTS mdl_model_pricing CASCADE;
DROP TABLE IF EXISTS mdl_model_costs CASCADE;
DROP TABLE IF EXISTS mdl_model_downloads CASCADE;
DROP TABLE IF EXISTS mdl_marketplace_models CASCADE;
DROP TABLE IF EXISTS mdl_provider_tokens CASCADE;
DROP TABLE IF EXISTS mdl_models CASCADE;
DROP TABLE IF EXISTS mdl_ai_providers CASCADE;

-- Eliminar secuencias
DROP SEQUENCE IF EXISTS mdl_models_id_seq CASCADE;
DROP SEQUENCE IF EXISTS mdl_model_versions_id_seq CASCADE;
DROP SEQUENCE IF EXISTS mdl_model_artifacts_id_seq CASCADE;
DROP SEQUENCE IF EXISTS mdl_model_experiments_id_seq CASCADE;
DROP SEQUENCE IF EXISTS mdl_experiment_runs_id_seq CASCADE;
DROP SEQUENCE IF EXISTS mdl_model_deployments_id_seq CASCADE;
DROP SEQUENCE IF EXISTS mdl_deployment_metrics_id_seq CASCADE;
DROP SEQUENCE IF EXISTS mdl_ai_providers_id_seq CASCADE;
DROP SEQUENCE IF EXISTS mdl_provider_tokens_id_seq CASCADE;
DROP SEQUENCE IF EXISTS mdl_model_costs_id_seq CASCADE;
DROP SEQUENCE IF EXISTS mdl_model_pricing_id_seq CASCADE;
DROP SEQUENCE IF EXISTS mdl_hardware_requirements_id_seq CASCADE;
DROP SEQUENCE IF EXISTS mdl_model_compliance_id_seq CASCADE;
DROP SEQUENCE IF EXISTS mdl_usage_statistics_id_seq CASCADE;
DROP SEQUENCE IF EXISTS mdl_marketplace_models_id_seq CASCADE;
DROP SEQUENCE IF EXISTS mdl_model_downloads_id_seq CASCADE;
```

### 2\. Script de Creación de Tablas

```plaintext
-- =====================================================
-- SCRIPT DE CREACIÓN DE TABLAS - EJECUTAR SEGUNDO
-- =====================================================

-- Extensión para UUIDs (opcional)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Tabla de proveedores de IA (MODEL MANAGEMENT)
CREATE TABLE mdl_ai_providers (
    id BIGSERIAL PRIMARY KEY,
    provider_name VARCHAR(100) NOT NULL UNIQUE,
    provider_type VARCHAR(50) NOT NULL,
    api_base_url VARCHAR(500),
    service_endpoint VARCHAR(500),
    is_active BOOLEAN DEFAULT true,
    rate_limit_per_minute INTEGER,
    max_concurrent_requests INTEGER,
    supported_model_types TEXT,
    provider_metadata TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla mdl_ai_providers
COMMENT ON TABLE mdl_ai_providers IS 'Tabla que almacena los proveedores de servicios de IA';
COMMENT ON COLUMN mdl_ai_providers.id IS 'Identificador único del proveedor';
COMMENT ON COLUMN mdl_ai_providers.provider_name IS 'Nombre del proveedor (OpenAI, Anthropic, etc.)';
COMMENT ON COLUMN mdl_ai_providers.provider_type IS 'Tipo de proveedor (OPENAI, ANTHROPIC, HUGGINGFACE, etc.)';
COMMENT ON COLUMN mdl_ai_providers.api_base_url IS 'URL base de la API del proveedor';
COMMENT ON COLUMN mdl_ai_providers.service_endpoint IS 'Endpoint del servicio CodeflowX que gestiona el proveedor';
COMMENT ON COLUMN mdl_ai_providers.is_active IS 'Indica si el proveedor está activo';
COMMENT ON COLUMN mdl_ai_providers.rate_limit_per_minute IS 'Límite de rate por minuto del proveedor';
COMMENT ON COLUMN mdl_ai_providers.max_concurrent_requests IS 'Máximo de requests concurrentes permitidos';
COMMENT ON COLUMN mdl_ai_providers.supported_model_types IS 'Tipos de modelos soportados en formato JSON';
COMMENT ON COLUMN mdl_ai_providers.provider_metadata IS 'Metadatos adicionales del proveedor en formato JSON';
COMMENT ON COLUMN mdl_ai_providers.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN mdl_ai_providers.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de tokens de proveedores (MODEL MANAGEMENT)
CREATE TABLE mdl_provider_tokens (
    id BIGSERIAL PRIMARY KEY,
    provider_id BIGINT REFERENCES mdl_ai_providers(id) ON DELETE CASCADE,
    token_name VARCHAR(100) NOT NULL,
    token_value TEXT NOT NULL,
    encryption_key_id VARCHAR(100),
    is_active BOOLEAN DEFAULT true,
    expires_at TIMESTAMP,
    usage_limit BIGINT,
    current_usage BIGINT DEFAULT 0,
    created_by BIGINT REFERENCES cor_users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla mdl_provider_tokens
COMMENT ON TABLE mdl_provider_tokens IS 'Tabla que almacena los tokens de autenticación de los proveedores';
COMMENT ON COLUMN mdl_provider_tokens.id IS 'Identificador único del token';
COMMENT ON COLUMN mdl_provider_tokens.provider_id IS 'ID del proveedor al que pertenece el token';
COMMENT ON COLUMN mdl_provider_tokens.token_name IS 'Nombre descriptivo del token';
COMMENT ON COLUMN mdl_provider_tokens.token_value IS 'Valor del token (encriptado)';
COMMENT ON COLUMN mdl_provider_tokens.encryption_key_id IS 'ID de la clave de encriptación utilizada';
COMMENT ON COLUMN mdl_provider_tokens.is_active IS 'Indica si el token está activo';
COMMENT ON COLUMN mdl_provider_tokens.expires_at IS 'Fecha y hora de expiración del token';
COMMENT ON COLUMN mdl_provider_tokens.usage_limit IS 'Límite de uso del token';
COMMENT ON COLUMN mdl_provider_tokens.current_usage IS 'Uso actual del token';
COMMENT ON COLUMN mdl_provider_tokens.created_by IS 'ID del usuario que creó el token';
COMMENT ON COLUMN mdl_provider_tokens.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN mdl_provider_tokens.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de modelos (MODEL MANAGEMENT)
CREATE TABLE mdl_models (
    id BIGSERIAL PRIMARY KEY,
    model_name VARCHAR(255) NOT NULL,
    model_description TEXT,
    project_id BIGINT REFERENCES prj_projects(id),
    user_id BIGINT REFERENCES cor_users(id),
    provider_id BIGINT REFERENCES mdl_ai_providers(id),
    model_type VARCHAR(50) NOT NULL,
    model_category VARCHAR(50),
    framework VARCHAR(100),
    version VARCHAR(50),
    is_adapter BOOLEAN DEFAULT false,
    is_fine_tuned BOOLEAN DEFAULT false,
    is_codeflowx_trained BOOLEAN DEFAULT false,
    is_marketplace_model BOOLEAN DEFAULT false,
    marketplace_source VARCHAR(100),
    marketplace_model_id VARCHAR(100),
    mlflow_run_id VARCHAR(100),
    mlflow_model_uri VARCHAR(500),
    status VARCHAR(50) DEFAULT 'DRAFT',
    tags TEXT,
    metadata TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    registered_at TIMESTAMP
);

-- Comentarios de la tabla mdl_models
COMMENT ON TABLE mdl_models IS 'Tabla principal que almacena la información de los modelos de IA';
COMMENT ON COLUMN mdl_models.id IS 'Identificador único del modelo';
COMMENT ON COLUMN mdl_models.model_name IS 'Nombre del modelo';
COMMENT ON COLUMN mdl_models.model_description IS 'Descripción del modelo';
COMMENT ON COLUMN mdl_models.project_id IS 'ID del proyecto al que pertenece el modelo';
COMMENT ON COLUMN mdl_models.user_id IS 'ID del usuario propietario del modelo';
COMMENT ON COLUMN mdl_models.provider_id IS 'ID del proveedor de IA del modelo';
COMMENT ON COLUMN mdl_models.model_type IS 'Tipo del modelo (MULTIMODAL, TEXT, AUDIO, VISION, etc.)';
COMMENT ON COLUMN mdl_models.model_category IS 'Categoría del modelo (LANGUAGE_MODEL, VISION_MODEL, etc.)';
COMMENT ON COLUMN mdl_models.framework IS 'Framework utilizado (TensorFlow, PyTorch, etc.)';
COMMENT ON COLUMN mdl_models.version IS 'Versión del modelo';
COMMENT ON COLUMN mdl_models.is_adapter IS 'Indica si es un modelo adaptador';
COMMENT ON COLUMN mdl_models.is_fine_tuned IS 'Indica si es un modelo fine-tuned';
COMMENT ON COLUMN mdl_models.is_codeflowx_trained IS 'Indica si fue entrenado por CodeflowX';
COMMENT ON COLUMN mdl_models.is_marketplace_model IS 'Indica si es un modelo del marketplace';
COMMENT ON COLUMN mdl_models.marketplace_source IS 'Fuente del marketplace (CivitAI, HuggingFace, etc.)';
COMMENT ON COLUMN mdl_models.marketplace_model_id IS 'ID del modelo en el marketplace';
COMMENT ON COLUMN mdl_models.mlflow_run_id IS 'ID de la ejecución en MLflow';
COMMENT ON COLUMN mdl_models.mlflow_model_uri IS 'URI del modelo en MLflow';
COMMENT ON COLUMN mdl_models.status IS 'Estado del modelo (DRAFT, TRAINING, APPROVED, etc.)';
COMMENT ON COLUMN mdl_models.tags IS 'Tags del modelo en formato JSON';
COMMENT ON COLUMN mdl_models.metadata IS 'Metadatos adicionales del modelo en formato JSON';
COMMENT ON COLUMN mdl_models.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN mdl_models.updated_at IS 'Fecha y hora de la última actualización del registro';
COMMENT ON COLUMN mdl_models.registered_at IS 'Fecha y hora de registro del modelo';

-- Tabla de costes del modelo (MODEL MANAGEMENT)
CREATE TABLE mdl_model_costs (
    id BIGSERIAL PRIMARY KEY,
    model_id BIGINT REFERENCES mdl_models(id) ON DELETE CASCADE,
    cost_type VARCHAR(50) NOT NULL,
    input_cost_per_token DECIMAL(15,8),
    output_cost_per_token DECIMAL(15,8),
    cost_per_image DECIMAL(15,8),
    cost_per_audio_minute DECIMAL(15,8),
    cost_per_api_call DECIMAL(15,8),
    currency VARCHAR(10) DEFAULT 'USD',
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla mdl_model_costs
COMMENT ON TABLE mdl_model_costs IS 'Tabla que almacena los costes operativos de los modelos';
COMMENT ON COLUMN mdl_model_costs.id IS 'Identificador único del coste';
COMMENT ON COLUMN mdl_model_costs.model_id IS 'ID del modelo al que pertenece el coste';
COMMENT ON COLUMN mdl_model_costs.cost_type IS 'Tipo de coste (INPUT_TOKENS, OUTPUT_TOKENS, IMAGES, etc.)';
COMMENT ON COLUMN mdl_model_costs.input_cost_per_token IS 'Coste por token de entrada';
COMMENT ON COLUMN mdl_model_costs.output_cost_per_token IS 'Coste por token de salida';
COMMENT ON COLUMN mdl_model_costs.cost_per_image IS 'Coste por imagen procesada';
COMMENT ON COLUMN mdl_model_costs.cost_per_audio_minute IS 'Coste por minuto de audio procesado';
COMMENT ON COLUMN mdl_model_costs.cost_per_api_call IS 'Coste por llamada API';
COMMENT ON COLUMN mdl_model_costs.currency IS 'Moneda del coste';
COMMENT ON COLUMN mdl_model_costs.is_active IS 'Indica si el coste está activo';
COMMENT ON COLUMN mdl_model_costs.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN mdl_model_costs.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de precios del modelo (MODEL MANAGEMENT)
CREATE TABLE mdl_model_pricing (
    id BIGSERIAL PRIMARY KEY,
    model_id BIGINT REFERENCES mdl_models(id) ON DELETE CASCADE,
    pricing_tier VARCHAR(50) NOT NULL,
    price_per_token DECIMAL(15,8),
    price_per_image DECIMAL(15,8),
    price_per_api_call DECIMAL(15,8),
    monthly_subscription_price DECIMAL(15,2),
    annual_subscription_price DECIMAL(15,2),
    currency VARCHAR(10) DEFAULT 'USD',
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla mdl_model_pricing
COMMENT ON TABLE mdl_model_pricing IS 'Tabla que almacena los precios de venta de los modelos';
COMMENT ON COLUMN mdl_model_pricing.id IS 'Identificador único del precio';
COMMENT ON COLUMN mdl_model_pricing.model_id IS 'ID del modelo al que pertenece el precio';
COMMENT ON COLUMN mdl_model_pricing.pricing_tier IS 'Nivel de precios (FREE, BASIC, PROFESSIONAL, etc.)';
COMMENT ON COLUMN mdl_model_pricing.price_per_token IS 'Precio por token';
COMMENT ON COLUMN mdl_model_pricing.price_per_image IS 'Precio por imagen';
COMMENT ON COLUMN mdl_model_pricing.price_per_api_call IS 'Precio por llamada API';
COMMENT ON COLUMN mdl_model_pricing.monthly_subscription_price IS 'Precio de suscripción mensual';
COMMENT ON COLUMN mdl_model_pricing.annual_subscription_price IS 'Precio de suscripción anual';
COMMENT ON COLUMN mdl_model_pricing.currency IS 'Moneda del precio';
COMMENT ON COLUMN mdl_model_pricing.is_active IS 'Indica si el precio está activo';
COMMENT ON COLUMN mdl_model_pricing.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN mdl_model_pricing.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de requisitos de hardware (MODEL MANAGEMENT)
CREATE TABLE mdl_hardware_requirements (
    id BIGSERIAL PRIMARY KEY,
    model_id BIGINT REFERENCES mdl_models(id) ON DELETE CASCADE,
    requirement_type VARCHAR(50) NOT NULL,
    min_cpu_cores INTEGER,
    recommended_cpu_cores INTEGER,
    min_ram_gb INTEGER,
    recommended_ram_gb INTEGER,
    min_gpu_memory_gb INTEGER,
    recommended_gpu_memory_gb INTEGER,
    gpu_type VARCHAR(100),
    min_storage_gb INTEGER,
    recommended_storage_gb INTEGER,
    network_bandwidth_mbps INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla mdl_hardware_requirements
COMMENT ON TABLE mdl_hardware_requirements IS 'Tabla que almacena los requisitos de hardware para ejecutar los modelos';
COMMENT ON COLUMN mdl_hardware_requirements.id IS 'Identificador único del requisito';
COMMENT ON COLUMN mdl_hardware_requirements.model_id IS 'ID del modelo al que pertenece el requisito';
COMMENT ON COLUMN mdl_hardware_requirements.requirement_type IS 'Tipo de requisito (MINIMUM, RECOMMENDED, OPTIMAL)';
COMMENT ON COLUMN mdl_hardware_requirements.min_cpu_cores IS 'Número mínimo de núcleos CPU';
COMMENT ON COLUMN mdl_hardware_requirements.recommended_cpu_cores IS 'Número recomendado de núcleos CPU';
COMMENT ON COLUMN mdl_hardware_requirements.min_ram_gb IS 'Memoria RAM mínima en GB';
COMMENT ON COLUMN mdl_hardware_requirements.recommended_ram_gb IS 'Memoria RAM recomendada en GB';
COMMENT ON COLUMN mdl_hardware_requirements.min_gpu_memory_gb IS 'Memoria GPU mínima en GB';
COMMENT ON COLUMN mdl_hardware_requirements.recommended_gpu_memory_gb IS 'Memoria GPU recomendada en GB';
COMMENT ON COLUMN mdl_hardware_requirements.gpu_type IS 'Tipo de GPU recomendada';
COMMENT ON COLUMN mdl_hardware_requirements.min_storage_gb IS 'Almacenamiento mínimo en GB';
COMMENT ON COLUMN mdl_hardware_requirements.recommended_storage_gb IS 'Almacenamiento recomendado en GB';
COMMENT ON COLUMN mdl_hardware_requirements.network_bandwidth_mbps IS 'Ancho de banda de red en Mbps';
COMMENT ON COLUMN mdl_hardware_requirements.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN mdl_hardware_requirements.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de cumplimiento normativo (MODEL MANAGEMENT)
CREATE TABLE mdl_model_compliance (
    id BIGSERIAL PRIMARY KEY,
    model_id BIGINT REFERENCES mdl_models(id) ON DELETE CASCADE,
    compliance_type VARCHAR(50) NOT NULL,
    compliance_status VARCHAR(50) DEFAULT 'NOT_ASSESSED',
    certification_date TIMESTAMP,
    expiry_date TIMESTAMP,
    certification_body VARCHAR(200),
    certification_id VARCHAR(100),
    compliance_details TEXT,
    audit_report_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla mdl_model_compliance
COMMENT ON TABLE mdl_model_compliance IS 'Tabla que almacena el cumplimiento normativo de los modelos';
COMMENT ON COLUMN mdl_model_compliance.id IS 'Identificador único del cumplimiento';
COMMENT ON COLUMN mdl_model_compliance.model_id IS 'ID del modelo al que pertenece el cumplimiento';
COMMENT ON COLUMN mdl_model_compliance.compliance_type IS 'Tipo de cumplimiento (AI_ACT_EUROPEAN, GDPR, HIPAA, etc.)';
COMMENT ON COLUMN mdl_model_compliance.compliance_status IS 'Estado del cumplimiento (NOT_ASSESSED, COMPLIANT, NON_COMPLIANT, etc.)';
COMMENT ON COLUMN mdl_model_compliance.certification_date IS 'Fecha de certificación';
COMMENT ON COLUMN mdl_model_compliance.expiry_date IS 'Fecha de expiración de la certificación';
COMMENT ON COLUMN mdl_model_compliance.certification_body IS 'Organismo de certificación';
COMMENT ON COLUMN mdl_model_compliance.certification_id IS 'ID de la certificación';
COMMENT ON COLUMN mdl_model_compliance.compliance_details IS 'Detalles del cumplimiento en formato JSON';
COMMENT ON COLUMN mdl_model_compliance.audit_report_url IS 'URL del reporte de auditoría';
COMMENT ON COLUMN mdl_model_compliance.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN mdl_model_compliance.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de estadísticas de uso (MODEL MANAGEMENT)
CREATE TABLE mdl_usage_statistics (
    id BIGSERIAL PRIMARY KEY,
    model_id BIGINT REFERENCES mdl_models(id) ON DELETE CASCADE,
    date DATE NOT NULL,
    total_requests BIGINT DEFAULT 0,
    successful_requests BIGINT DEFAULT 0,
    failed_requests BIGINT DEFAULT 0,
    total_tokens_processed BIGINT DEFAULT 0,
    total_images_processed BIGINT DEFAULT 0,
    total_audio_minutes BIGINT DEFAULT 0,
    average_response_time_ms DOUBLE PRECISION,
    total_cost DECIMAL(15,2),
    total_revenue DECIMAL(15,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla mdl_usage_statistics
COMMENT ON TABLE mdl_usage_statistics IS 'Tabla que almacena las estadísticas de uso diario de los modelos';
COMMENT ON COLUMN mdl_usage_statistics.id IS 'Identificador único de la estadística';
COMMENT ON COLUMN mdl_usage_statistics.model_id IS 'ID del modelo al que pertenece la estadística';
COMMENT ON COLUMN mdl_usage_statistics.date IS 'Fecha de la estadística';
COMMENT ON COLUMN mdl_usage_statistics.total_requests IS 'Total de requests procesados';
COMMENT ON COLUMN mdl_usage_statistics.successful_requests IS 'Total de requests exitosos';
COMMENT ON COLUMN mdl_usage_statistics.failed_requests IS 'Total de requests fallidos';
COMMENT ON COLUMN mdl_usage_statistics.total_tokens_processed IS 'Total de tokens procesados';
COMMENT ON COLUMN mdl_usage_statistics.total_images_processed IS 'Total de imágenes procesadas';
COMMENT ON COLUMN mdl_usage_statistics.total_audio_minutes IS 'Total de minutos de audio procesados';
COMMENT ON COLUMN mdl_usage_statistics.average_response_time_ms IS 'Tiempo promedio de respuesta en milisegundos';
COMMENT ON COLUMN mdl_usage_statistics.total_cost IS 'Coste total del día';
COMMENT ON COLUMN mdl_usage_statistics.total_revenue IS 'Ingresos totales del día';
COMMENT ON COLUMN mdl_usage_statistics.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN mdl_usage_statistics.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de modelos del marketplace (MODEL MANAGEMENT)
CREATE TABLE mdl_marketplace_models (
    id BIGSERIAL PRIMARY KEY,
    marketplace_source VARCHAR(100) NOT NULL,
    marketplace_model_id VARCHAR(100) NOT NULL,
    model_name VARCHAR(255),
    model_description TEXT,
    model_type VARCHAR(100),
    framework VARCHAR(100),
    download_url VARCHAR(500),
    file_size_bytes BIGINT,
    checksum VARCHAR(64),
    tags TEXT,
    rating DOUBLE PRECISION,
    download_count BIGINT,
    last_updated TIMESTAMP,
    is_downloaded BOOLEAN DEFAULT false,
    downloaded_at TIMESTAMP,
    local_model_id BIGINT REFERENCES mdl_models(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla mdl_marketplace_models
COMMENT ON TABLE mdl_marketplace_models IS 'Tabla que almacena los modelos disponibles en marketplaces externos';
COMMENT ON COLUMN mdl_marketplace_models.id IS 'Identificador único del modelo del marketplace';
COMMENT ON COLUMN mdl_marketplace_models.marketplace_source IS 'Fuente del marketplace (CivitAI, HuggingFace, etc.)';
COMMENT ON COLUMN mdl_marketplace_models.marketplace_model_id IS 'ID del modelo en el marketplace';
COMMENT ON COLUMN mdl_marketplace_models.model_name IS 'Nombre del modelo';
COMMENT ON COLUMN mdl_marketplace_models.model_description IS 'Descripción del modelo';
COMMENT ON COLUMN mdl_marketplace_models.model_type IS 'Tipo del modelo';
COMMENT ON COLUMN mdl_marketplace_models.framework IS 'Framework del modelo';
COMMENT ON COLUMN mdl_marketplace_models.download_url IS 'URL de descarga del modelo';
COMMENT ON COLUMN mdl_marketplace_models.file_size_bytes IS 'Tamaño del archivo en bytes';
COMMENT ON COLUMN mdl_marketplace_models.checksum IS 'Checksum del archivo';
COMMENT ON COLUMN mdl_marketplace_models.tags IS 'Tags del modelo en formato JSON';
COMMENT ON COLUMN mdl_marketplace_models.rating IS 'Rating del modelo en el marketplace';
COMMENT ON COLUMN mdl_marketplace_models.download_count IS 'Número de descargas en el marketplace';
COMMENT ON COLUMN mdl_marketplace_models.last_updated IS 'Última actualización en el marketplace';
COMMENT ON COLUMN mdl_marketplace_models.is_downloaded IS 'Indica si el modelo ha sido descargado';
COMMENT ON COLUMN mdl_marketplace_models.downloaded_at IS 'Fecha y hora de descarga';
COMMENT ON COLUMN mdl_marketplace_models.local_model_id IS 'ID del modelo local si se descargó';
COMMENT ON COLUMN mdl_marketplace_models.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN mdl_marketplace_models.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de descargas de modelos (MODEL MANAGEMENT)
CREATE TABLE mdl_model_downloads (
    id BIGSERIAL PRIMARY KEY,
    marketplace_model_id BIGINT REFERENCES mdl_marketplace_models(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES cor_users(id),
    download_status VARCHAR(50) DEFAULT 'PENDING',
    download_progress INTEGER DEFAULT 0,
    download_started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    download_completed_at TIMESTAMP,
    download_duration_seconds BIGINT,
    file_path VARCHAR(500),
    error_message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla mdl_model_downloads
COMMENT ON TABLE mdl_model_downloads IS 'Tabla que registra las descargas de modelos del marketplace';
COMMENT ON COLUMN mdl_model_downloads.id IS 'Identificador único de la descarga';
COMMENT ON COLUMN mdl_model_downloads.marketplace_model_id IS 'ID del modelo del marketplace';
COMMENT ON COLUMN mdl_model_downloads.user_id IS 'ID del usuario que realizó la descarga';
COMMENT ON COLUMN mdl_model_downloads.download_status IS 'Estado de la descarga (PENDING, DOWNLOADING, COMPLETED, etc.)';
COMMENT ON COLUMN mdl_model_downloads.download_progress IS 'Progreso de la descarga (0-100%)';
COMMENT ON COLUMN mdl_model_downloads.download_started_at IS 'Fecha y hora de inicio de la descarga';
COMMENT ON COLUMN mdl_model_downloads.download_completed_at IS 'Fecha y hora de finalización de la descarga';
COMMENT ON COLUMN mdl_model_downloads.download_duration_seconds IS 'Duración de la descarga en segundos';
COMMENT ON COLUMN mdl_model_downloads.file_path IS 'Ruta local del archivo descargado';
COMMENT ON COLUMN mdl_model_downloads.error_message IS 'Mensaje de error si la descarga falló';
COMMENT ON COLUMN mdl_model_downloads.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN mdl_model_downloads.updated_at IS 'Fecha y hora de la última actualización del registro';
```

### 4\. Script de Inicialización con Datos de Demo

```plaintext
-- =====================================================
-- INICIALIZACIÓN DE PROVEEDORES DE IA
-- =====================================================

-- Insertar proveedores de IA principales
INSERT INTO mdl_ai_providers (
    provider_name, provider_type, api_base_url, service_endpoint,
    rate_limit_per_minute, max_concurrent_requests, supported_model_types,
    provider_metadata, is_active
) VALUES
('OpenAI', 'OPENAI', 'https://api.openai.com/v1', 'https://codeflowx.company.com/providers/openai',
 3000, 100, '["text", "vision", "audio", "multimodal"]',
 '{"api_version": "v1", "models": ["gpt-4", "gpt-3.5-turbo", "dall-e-3", "whisper"], "features": ["chat", "completions", "embeddings", "fine-tuning"]}',
 true),

('Anthropic', 'ANTHROPIC', 'https://api.anthropic.com', 'https://codeflowx.company.com/providers/anthropic',
 2500, 80, '["text", "multimodal"]',
 '{"api_version": "v1", "models": ["claude-3-opus", "claude-3-sonnet", "claude-3-haiku"], "features": ["messages", "completions", "tools"]}',
 true),

('HuggingFace', 'HUGGINGFACE', 'https://api-inference.huggingface.co', 'https://codeflowx.company.com/providers/huggingface',
 5000, 200, '["text", "vision", "audio", "multimodal", "embedding"]',
 '{"api_version": "v1", "models": ["bert", "gpt2", "t5", "stable-diffusion"], "features": ["inference", "embeddings", "text-generation", "image-generation"]}',
 true),

('CodeflowX', 'CODEFLOWX', 'https://api.codeflowx.company.com', 'https://codeflowx.company.com/providers/internal',
 10000, 500, '["text", "vision", "audio", "multimodal", "embedding", "generative", "discriminative"]',
 '{"api_version": "v2", "models": ["codeflowx-llm", "codeflowx-vision", "codeflowx-audio"], "features": ["custom-training", "fine-tuning", "adapter-support"]}',
 true),

('Self-Hosting', 'SELF_HOSTING', 'https://internal.company.com/ai', 'https://codeflowx.company.com/providers/self-hosting',
 2000, 50, '["text", "vision", "audio", "multimodal"]',
 '{"api_version": "v1", "models": ["internal-llm", "internal-vision"], "features": ["local-inference", "custom-models", "data-privacy"]}',
 true),

('CivitAI', 'CIVITAI', 'https://civitai.com/api/v1', 'https://codeflowx.company.com/providers/civitai',
 1000, 30, '["vision", "audio", "video", "3d"]',
 '{"api_version": "v1", "models": ["stable-diffusion", "midjourney-style", "audio-models"], "features": ["image-generation", "audio-generation", "nsfw-filtering"]}',
 true),

('Google AI', 'GOOGLE_AI', 'https://generativelanguage.googleapis.com', 'https://codeflowx.company.com/providers/google-ai',
 1500, 60, '["text", "multimodal", "embedding"]',
 '{"api_version": "v1", "models": ["gemini-pro", "gemini-pro-vision", "embedding-001"], "features": ["chat", "completions", "embeddings", "safety"]}',
 true);

-- =====================================================
-- INICIALIZACIÓN DE TOKENS DE PROVEEDORES
-- =====================================================

-- Insertar tokens de demo para los proveedores
INSERT INTO mdl_provider_tokens (
    provider_id, token_name, token_value, encryption_key_id,
    expires_at, usage_limit, created_by
) VALUES
((SELECT id FROM mdl_ai_providers WHERE provider_name = 'OpenAI'),
 'OpenAI Production Token', 'encrypted_sk-proj-demo123456789', 'key_openai_prod_001',
 CURRENT_TIMESTAMP + INTERVAL '1 year', 1000000,
 (SELECT id FROM cor_users WHERE username = 'admin')),

((SELECT id FROM mdl_ai_providers WHERE provider_name = 'Anthropic'),
 'Anthropic Production Token', 'encrypted_sk-ant-demo123456789', 'key_anthropic_prod_001',
 CURRENT_TIMESTAMP + INTERVAL '1 year', 500000,
 (SELECT id FROM cor_users WHERE username = 'admin')),

((SELECT id FROM mdl_ai_providers WHERE provider_name = 'HuggingFace'),
 'HuggingFace Production Token', 'encrypted_hf_demo123456789', 'key_huggingface_prod_001',
 CURRENT_TIMESTAMP + INTERVAL '1 year', 2000000,
 (SELECT id FROM cor_users WHERE username = 'admin')),

((SELECT id FROM mdl_ai_providers WHERE provider_name = 'CodeflowX'),
 'CodeflowX Internal Token', 'encrypted_cfx_demo123456789', 'key_codeflowx_internal_001',
 CURRENT_TIMESTAMP + INTERVAL '5 years', 10000000,
 (SELECT id FROM cor_users WHERE username = 'admin')),

((SELECT id FROM mdl_ai_providers WHERE provider_name = 'Self-Hosting'),
 'Self-Hosting Internal Token', 'encrypted_self_demo123456789', 'key_self_hosting_001',
 CURRENT_TIMESTAMP + INTERVAL '10 years', 5000000,
 (SELECT id FROM cor_users WHERE username = 'admin'));

-- =====================================================
-- INICIALIZACIÓN DE MODELOS DE DEMO
-- =====================================================

-- Insertar modelos de demo
INSERT INTO mdl_models (
    model_name, model_description, project_id, user_id, provider_id,
    model_type, model_category, framework, version, is_adapter, is_fine_tuned,
    is_codeflowx_trained, is_marketplace_model, status, tags, metadata
) VALUES
('GPT-4 Turbo', 'Modelo de lenguaje avanzado de OpenAI para tareas de texto y conversación',
 (SELECT id FROM prj_projects WHERE name = 'AI Assistant Project'),
 (SELECT id FROM cor_users WHERE username = 'ai'),
 (SELECT id FROM mdl_ai_providers WHERE provider_name = 'OpenAI'),
 'TEXT', 'LANGUAGE_MODEL', 'OpenAI', 'gpt-4-turbo-preview', false, false, false, false,
 'APPROVED', '["llm", "text-generation", "conversation", "openai"]',
 '{"capabilities": ["text-generation", "code-generation", "reasoning"], "max_tokens": 128000, "training_data": "Until April 2023"}'),

('Claude-3 Sonnet', 'Modelo multimodal de Anthropic para análisis de texto e imágenes',
 (SELECT id FROM prj_projects WHERE name = 'Multimodal Analysis Project'),
 (SELECT id FROM cor_users WHERE username = 'ai'),
 (SELECT id FROM mdl_ai_providers WHERE provider_name = 'Anthropic'),
 'MULTIMODAL', 'LANGUAGE_MODEL', 'Anthropic', 'claude-3-sonnet-20240229', false, false, false, false,
 'APPROVED', '["llm", "multimodal", "analysis", "anthropic"]',
 '{"capabilities": ["text-analysis", "image-analysis", "reasoning"], "max_tokens": 200000, "training_data": "Until August 2023"}'),

('CodeflowX Custom LLM', 'Modelo de lenguaje personalizado entrenado por CodeflowX para análisis de código',
 (SELECT id FROM prj_projects WHERE name = 'Code Analysis Project'),
 (SELECT id FROM cor_users WHERE username = 'developer'),
 (SELECT id FROM mdl_ai_providers WHERE provider_name = 'CodeflowX'),
 'TEXT', 'LANGUAGE_MODEL', 'CodeflowX', '1.0.0', false, false, true, false,
 'APPROVED', '["llm", "code-analysis", "custom", "codeflowx"]',
 '{"capabilities": ["code-analysis", "bug-detection", "code-generation"], "max_tokens": 50000, "training_data": "Codebase internal"}'),

('Stable Diffusion XL', 'Modelo de generación de imágenes de CivitAI para creación de contenido visual',
 (SELECT id FROM prj_projects WHERE name = 'Image Generation Project'),
 (SELECT id FROM cor_users WHERE username = 'ai'),
 (SELECT id FROM mdl_ai_providers WHERE provider_name = 'CivitAI'),
 'VISION', 'GENERATIVE_MODEL', 'Stability AI', '1.0', false, false, false, true,
 'APPROVED', '["image-generation", "stable-diffusion", "civitai", "vision"]',
 '{"capabilities": ["image-generation", "image-editing", "style-transfer"], "resolution": "1024x1024", "training_data": "LAION-5B"}'),

('BERT Fine-tuned', 'Modelo BERT fine-tuned para clasificación de documentos empresariales',
 (SELECT id FROM prj_projects WHERE name = 'Document Classification Project'),
 (SELECT id FROM cor_users WHERE username = 'business'),
 (SELECT id FROM mdl_ai_providers WHERE provider_name = 'HuggingFace'),
 'TEXT', 'CLASSIFICATION_MODEL', 'HuggingFace', '1.0.0', false, true, false, false,
 'APPROVED', '["bert", "classification", "fine-tuned", "huggingface"]',
 '{"capabilities": ["document-classification", "sentiment-analysis", "named-entity-recognition"], "max_tokens": 512, "training_data": "Custom business documents"}');

-- =====================================================
-- INICIALIZACIÓN DE COSTES DE MODELOS
-- =====================================================

-- Insertar costes para los modelos
INSERT INTO mdl_model_costs (
    model_id, cost_type, input_cost_per_token, output_cost_per_token,
    cost_per_image, cost_per_api_call, currency
) VALUES
((SELECT id FROM mdl_models WHERE model_name = 'GPT-4 Turbo'),
 'INPUT_TOKENS', 0.00003, 0.00006, NULL, 0.001, 'USD'),

((SELECT id FROM mdl_models WHERE model_name = 'Claude-3 Sonnet'),
 'INPUT_TOKENS', 0.000015, 0.000075, NULL, 0.001, 'USD'),

((SELECT id FROM mdl_models WHERE model_name = 'CodeflowX Custom LLM'),
 'INPUT_TOKENS', 0.00001, 0.00002, NULL, 0.0005, 'USD'),

((SELECT id FROM mdl_models WHERE model_name = 'Stable Diffusion XL'),
 'IMAGES', NULL, NULL, 0.05, 0.01, 'USD'),

((SELECT id FROM mdl_models WHERE model_name = 'BERT Fine-tuned'),
 'INPUT_TOKENS', 0.000005, 0.00001, NULL, 0.0002, 'USD');

-- =====================================================
-- INICIALIZACIÓN DE PRECIOS DE MODELOS
-- =====================================================

-- Insertar precios de venta para los modelos
INSERT INTO mdl_model_pricing (
    model_id, pricing_tier, price_per_token, price_per_image,
    price_per_api_call, monthly_subscription_price, annual_subscription_price, currency
) VALUES
((SELECT id FROM mdl_models WHERE model_name = 'GPT-4 Turbo'),
 'PROFESSIONAL', 0.00005, NULL, 0.002, 99.99, 999.99, 'USD'),

((SELECT id FROM mdl_models WHERE model_name = 'Claude-3 Sonnet'),
 'PROFESSIONAL', 0.00003, NULL, 0.002, 79.99, 799.99, 'USD'),

((SELECT id FROM mdl_models WHERE model_name = 'CodeflowX Custom LLM'),
 'ENTERPRISE', 0.00002, NULL, 0.001, 199.99, 1999.99, 'USD'),

((SELECT id FROM mdl_models WHERE model_name = 'Stable Diffusion XL'),
 'BASIC', NULL, 0.10, 0.02, 29.99, 299.99, 'USD'),

((SELECT id FROM mdl_models WHERE model_name = 'BERT Fine-tuned'),
 'PROFESSIONAL', 0.00001, NULL, 0.0005, 49.99, 499.99, 'USD');

-- =====================================================
-- INICIALIZACIÓN DE REQUISITOS DE HARDWARE
-- =====================================================

-- Insertar requisitos mínimos y recomendados para los modelos
INSERT INTO mdl_hardware_requirements (
    model_id, requirement_type, min_cpu_cores, recommended_cpu_cores,
    min_ram_gb, recommended_ram_gb, min_gpu_memory_gb, recommended_gpu_memory_gb,
    gpu_type, min_storage_gb, recommended_storage_gb
) VALUES
-- GPT-4 Turbo (API-based, no requiere hardware local)
((SELECT id FROM mdl_models WHERE model_name = 'GPT-4 Turbo'),
 'MINIMUM', 1, 2, 2, 4, 0, 0, NULL, 1, 2),

-- Claude-3 Sonnet (API-based, no requiere hardware local)
((SELECT id FROM mdl_models WHERE model_name = 'Claude-3 Sonnet'),
 'MINIMUM', 1, 2, 2, 4, 0, 0, NULL, 1, 2),

-- CodeflowX Custom LLM (requiere hardware local)
((SELECT id FROM mdl_models WHERE model_name = 'CodeflowX Custom LLM'),
 'MINIMUM', 4, 8, 8, 16, 4, 8, 'RTX 3060', 10, 20),
((SELECT id FROM mdl_models WHERE model_name = 'CodeflowX Custom LLM'),
 'RECOMMENDED', 8, 16, 16, 32, 8, 16, 'RTX 4090', 20, 50),

-- Stable Diffusion XL (requiere GPU)
((SELECT id FROM mdl_models WHERE model_name = 'Stable Diffusion XL'),
 'MINIMUM', 4, 8, 8, 16, 6, 8, 'RTX 3060', 15, 30),
((SELECT id FROM mdl_models WHERE model_name = 'Stable Diffusion XL'),
 'RECOMMENDED', 8, 16, 16, 32, 8, 12, 'RTX 4090', 30, 60),

-- BERT Fine-tuned (CPU-based)
((SELECT id FROM mdl_models WHERE model_name = 'BERT Fine-tuned'),
 'MINIMUM', 2, 4, 4, 8, 0, 0, NULL, 5, 10),
((SELECT id FROM mdl_models WHERE model_name = 'BERT Fine-tuned'),
 'RECOMMENDED', 4, 8, 8, 16, 0, 0, NULL, 10, 20);

-- =====================================================
-- INICIALIZACIÓN DE CUMPLIMIENTO NORMATIVO
-- =====================================================

-- Insertar información de cumplimiento para los modelos
INSERT INTO mdl_model_compliance (
    model_id, compliance_type, compliance_status, certification_date,
    expiry_date, certification_body, compliance_details
) VALUES
((SELECT id FROM mdl_models WHERE model_name = 'GPT-4 Turbo'),
 'GDPR', 'COMPLIANT', CURRENT_TIMESTAMP - INTERVAL '6 months',
 CURRENT_TIMESTAMP + INTERVAL '6 months', 'OpenAI Compliance Team',
 '{"data_processing": "EU data centers", "data_retention": "30 days", "user_rights": "fully_supported"}'),

((SELECT id FROM mdl_models WHERE model_name = 'Claude-3 Sonnet'),
 'AI_ACT_EUROPEAN', 'UNDER_REVIEW', NULL, NULL, 'Anthropic Compliance Team',
 '{"ai_risk_assessment": "in_progress", "transparency": "high", "human_oversight": "implemented"}'),

((SELECT id FROM mdl_models WHERE model_name = 'CodeflowX Custom LLM'),
 'SOC2', 'COMPLIANT', CURRENT_TIMESTAMP - INTERVAL '3 months',
 CURRENT_TIMESTAMP + INTERVAL '9 months', 'Internal Audit Team',
 '{"security_controls": "implemented", "access_management": "role_based", "data_protection": "encrypted"}'),

((SELECT id FROM mdl_models WHERE model_name = 'Stable Diffusion XL'),
 'GDPR', 'COMPLIANT', CURRENT_TIMESTAMP - INTERVAL '1 month',
 CURRENT_TIMESTAMP + INTERVAL '11 months', 'CivitAI Compliance Team',
 '{"content_filtering": "active", "nsfw_detection": "enabled", "user_consent": "required"}'),

((SELECT id FROM mdl_models WHERE model_name = 'BERT Fine-tuned'),
 'HIPAA', 'UNDER_REVIEW', NULL, NULL, 'Internal Compliance Team',
 '{"phi_handling": "encrypted", "audit_logging": "enabled", "access_controls": "strict"}');

-- =====================================================
-- INICIALIZACIÓN DE ESTADÍSTICAS DE USO
-- =====================================================

-- Insertar estadísticas de uso de demo para los últimos 7 días
INSERT INTO mdl_usage_statistics (
    model_id, date, total_requests, successful_requests, failed_requests,
    total_tokens_processed, average_response_time_ms, total_cost, total_revenue
) VALUES
-- GPT-4 Turbo - últimos 7 días
((SELECT id FROM mdl_models WHERE model_name = 'GPT-4 Turbo'),
 CURRENT_DATE - INTERVAL '6 days', 1250, 1200, 50, 150000, 2500, 4.50, 6.25),
((SELECT id FROM mdl_models WHERE model_name = 'GPT-4 Turbo'),
 CURRENT_DATE - INTERVAL '5 days', 1380, 1350, 30, 165000, 2300, 4.95, 6.90),
((SELECT id FROM mdl_models WHERE model_name = 'GPT-4 Turbo'),
 CURRENT_DATE - INTERVAL '4 days', 1420, 1400, 20, 170000, 2200, 5.10, 7.10),
((SELECT id FROM mdl_models WHERE model_name = 'GPT-4 Turbo'),
 CURRENT_DATE - INTERVAL '3 days', 1560, 1530, 30, 185000, 2100, 5.55, 7.80),
((SELECT id FROM mdl_models WHERE model_name = 'GPT-4 Turbo'),
 CURRENT_DATE - INTERVAL '2 days', 1680, 1650, 30, 200000, 2000, 6.00, 8.40),
((SELECT id FROM mdl_models WHERE model_name = 'GPT-4 Turbo'),
 CURRENT_DATE - INTERVAL '1 day', 1750, 1720, 30, 210000, 1900, 6.30, 8.75),
((SELECT id FROM mdl_models WHERE model_name = 'GPT-4 Turbo'),
 CURRENT_DATE, 1820, 1800, 20, 220000, 1800, 6.60, 9.10),

-- Claude-3 Sonnet - últimos 7 días
((SELECT id FROM mdl_models WHERE model_name = 'Claude-3 Sonnet'),
 CURRENT_DATE - INTERVAL '6 days', 890, 870, 20, 120000, 1800, 2.25, 3.12),
((SELECT id FROM mdl_models WHERE model_name = 'Claude-3 Sonnet'),
 CURRENT_DATE - INTERVAL '5 days', 920, 900, 20, 125000, 1750, 2.35, 3.22),
((SELECT id FROM mdl_models WHERE model_name = 'Claude-3 Sonnet'),
 CURRENT_DATE - INTERVAL '4 days', 950, 930, 20, 130000, 1700, 2.45, 3.32),
((SELECT id FROM mdl_models WHERE model_name = 'Claude-3 Sonnet'),
 CURRENT_DATE - INTERVAL '3 days', 980, 960, 20, 135000, 1650, 2.55, 3.42),
((SELECT id FROM mdl_models WHERE model_name = 'Claude-3 Sonnet'),
 CURRENT_DATE - INTERVAL '2 days', 1010, 990, 20, 140000, 1600, 2.65, 3.52),
((SELECT id FROM mdl_models WHERE model_name = 'Claude-3 Sonnet'),
 CURRENT_DATE - INTERVAL '1 day', 1040, 1020, 20, 145000, 1550, 2.75, 3.62),
((SELECT id FROM mdl_models WHERE model_name = 'Claude-3 Sonnet'),
 CURRENT_DATE, 1070, 1050, 20, 150000, 1500, 2.85, 3.72),

-- Stable Diffusion XL - últimos 7 días
((SELECT id FROM mdl_models WHERE model_name = 'Stable Diffusion XL'),
 CURRENT_DATE - INTERVAL '6 days', 450, 440, 10, 0, 8000, 22.00, 44.00),
((SELECT id FROM mdl_models WHERE model_name = 'Stable Diffusion XL'),
 CURRENT_DATE - INTERVAL '5 days', 480, 470, 10, 0, 7800, 23.50, 47.00),
((SELECT id FROM mdl_models WHERE model_name = 'Stable Diffusion XL'),
 CURRENT_DATE - INTERVAL '4 days', 510, 500, 10, 0, 7600, 25.00, 50.00),
((SELECT id FROM mdl_models WHERE model_name = 'Stable Diffusion XL'),
 CURRENT_DATE - INTERVAL '3 days', 540, 530, 10, 0, 7400, 26.50, 53.00),
((SELECT id FROM mdl_models WHERE model_name = 'Stable Diffusion XL'),
 CURRENT_DATE - INTERVAL '2 days', 570, 560, 10, 0, 7200, 28.00, 56.00),
((SELECT id FROM mdl_models WHERE model_name = 'Stable Diffusion XL'),
 CURRENT_DATE - INTERVAL '1 day', 600, 590, 10, 0, 7000, 29.50, 59.00),
((SELECT id FROM mdl_models WHERE model_name = 'Stable Diffusion XL'),
 CURRENT_DATE, 630, 620, 10, 0, 6800, 31.00, 62.00);

-- =====================================================
-- INICIALIZACIÓN DE MODELOS DEL MARKETPLACE
-- =====================================================

-- Insertar modelos del marketplace de demo
INSERT INTO mdl_marketplace_models (
    marketplace_source, marketplace_model_id, model_name, model_description,
    model_type, framework, download_url, file_size_bytes, tags, rating, download_count
) VALUES
('CivitAI', 'civitai_001', 'Realistic Vision V5.1', 'Modelo de generación de imágenes realistas de alta calidad',
 'vision', 'Stable Diffusion', 'https://civitai.com/models/4201/realistic-vision-v51', 2048576000,
 '["realistic", "portrait", "photography", "high-quality"]', 4.8, 150000),

('HuggingFace', 'hf_001', 'Llama-2-7b-chat-hf', 'Modelo de lenguaje Llama 2 de 7B parámetros optimizado para chat',
 'text', 'Transformers', 'https://huggingface.co/meta-llama/Llama-2-7b-chat-hf', 1394038784,
 '["llm", "chat", "llama", "7b"]', 4.9, 250000),

('Google AI', 'google_001', 'Gemini Pro Vision', 'Modelo multimodal de Google para análisis de texto e imágenes',
 'multimodal', 'Google AI', 'https://ai.google.dev/models/gemini', 0,
 '["multimodal", "vision", "text", "google"]', 4.7, 100000),

('NVIDIA NGC', 'ngc_001', 'NeMo-1.3B', 'Modelo de lenguaje eficiente de NVIDIA para tareas de NLP',
 'text', 'PyTorch', 'https://catalog.ngc.nvidia.com/orgs/nvidia/teams/nemo/models/nemo_gpt3_1_3b', 2684354560,
 '["llm", "nemo", "nvidia", "efficient"]', 4.6, 75000),

('Intel OpenVINO', 'intel_001', 'BERT-Base-Uncased', 'Modelo BERT optimizado para Intel CPUs y GPUs',
 'text', 'OpenVINO', 'https://github.com/openvinotoolkit/openvino_model_zoo', 440401920,
 '["bert", "nlp", "intel", "optimized"]', 4.5, 50000);

-- =====================================================
-- INICIALIZACIÓN DE VERSIONES DE MODELOS
-- =====================================================

-- Insertar versiones para los modelos
INSERT INTO mdl_model_versions (
    model_id, version, description, stage, parameters, signature, created_at
) VALUES
((SELECT id FROM mdl_models WHERE model_name = 'GPT-4 Turbo'),
 'gpt-4-turbo-preview', 'Versión más reciente de GPT-4 con capacidades mejoradas', 'PRODUCTION',
 '{"temperature": 0.7, "max_tokens": 128000, "top_p": 1.0}',
 '{"input": "text", "output": "text", "max_length": 128000}', CURRENT_TIMESTAMP),

((SELECT id FROM mdl_models WHERE model_name = 'Claude-3 Sonnet'),
 'claude-3-sonnet-20240229', 'Versión estable de Claude 3 Sonnet', 'PRODUCTION',
 '{"temperature": 0.5, "max_tokens": 200000, "top_p": 0.9}',
 '{"input": ["text", "image"], "output": "text", "max_length": 200000}', CURRENT_TIMESTAMP),

((SELECT id FROM mdl_models WHERE model_name = 'CodeflowX Custom LLM'),
 '1.0.0', 'Primera versión del modelo personalizado de CodeflowX', 'PRODUCTION',
 '{"temperature": 0.3, "max_tokens": 50000, "top_p": 0.8}',
 '{"input": "text", "output": "text", "max_length": 50000}', CURRENT_TIMESTAMP),

((SELECT id FROM mdl_models WHERE model_name = 'Stable Diffusion XL'),
 '1.0', 'Versión estable de Stable Diffusion XL', 'PRODUCTION',
 '{"steps": 50, "cfg_scale": 7.5, "width": 1024, "height": 1024}',
 '{"input": "text", "output": "image", "resolution": "1024x1024"}', CURRENT_TIMESTAMP),

((SELECT id FROM mdl_models WHERE model_name = 'BERT Fine-tuned'),
 '1.0.0', 'Versión fine-tuned de BERT para clasificación de documentos', 'PRODUCTION',
 '{"max_length": 512, "batch_size": 16, "learning_rate": 2e-5}',
 '{"input": "text", "output": "classification", "max_length": 512}', CURRENT_TIMESTAMP);

-- =====================================================
-- INICIALIZACIÓN DE EXPERIMENTOS DE MODELOS
-- =====================================================

-- Insertar experimentos para los modelos
INSERT INTO mdl_model_experiments (
    model_id, name, description, experiment_type, objective, hypothesis, methodology, status, started_at
) VALUES
((SELECT id FROM mdl_models WHERE model_name = 'CodeflowX Custom LLM'),
 'Fine-tuning para Análisis de Código', 'Experimento para mejorar la capacidad de análisis de código del modelo',
 'FINE_TUNING', 'Mejorar la precisión en detección de bugs y generación de código',
 'El fine-tuning con datos específicos de código mejorará el rendimiento en tareas de programación',
 'Fine-tuning con dataset de código Python y Java, evaluación con métricas de precisión y recall',
 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '30 days'),

((SELECT id FROM mdl_models WHERE model_name = 'BERT Fine-tuned'),
 'Fine-tuning para Clasificación de Documentos', 'Experimento para adaptar BERT a documentos empresariales',
 'FINE_TUNING', 'Mejorar la clasificación de documentos empresariales',
 'El fine-tuning con documentos del dominio empresarial mejorará la precisión de clasificación',
 'Fine-tuning con dataset de documentos empresariales, validación cruzada, evaluación con F1-score',
 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '15 days'),

((SELECT id FROM mdl_models WHERE model_name = 'Stable Diffusion XL'),
 'Optimización de Parámetros', 'Experimento para encontrar los mejores parámetros de generación',
 'HYPERPARAMETER_OPTIMIZATION', 'Optimizar parámetros para mejor calidad de imagen',
 'La optimización de steps, cfg_scale y otros parámetros mejorará la calidad visual',
 'Grid search de parámetros, evaluación subjetiva de calidad, métricas de diversidad',
 'RUNNING', CURRENT_TIMESTAMP - INTERVAL '7 days');

-- =====================================================
-- INICIALIZACIÓN DE DESPLIEGUES DE MODELOS
-- =====================================================

-- Insertar despliegues para los modelos
INSERT INTO mdl_model_deployments (
    model_id, model_version_id, name, description, deployment_type, environment, status, endpoint_url
) VALUES
((SELECT id FROM mdl_models WHERE model_name = 'CodeflowX Custom LLM'),
 (SELECT id FROM mdl_model_versions WHERE model_id = (SELECT id FROM mdl_models WHERE model_name = 'CodeflowX Custom LLM')),
 'CodeflowX LLM Production', 'Despliegue en producción del modelo personalizado de CodeflowX',
 'REST_API', 'prod', 'RUNNING', 'https://api.codeflowx.company.com/v1/models/custom-llm'),

((SELECT id FROM mdl_models WHERE model_name = 'BERT Fine-tuned'),
 (SELECT id FROM mdl_model_versions WHERE model_id = (SELECT id FROM mdl_models WHERE model_name = 'BERT Fine-tuned')),
 'BERT Document Classifier', 'Despliegue del clasificador de documentos BERT fine-tuned',
 'REST_API', 'staging', 'RUNNING', 'https://staging.codeflowx.company.com/v1/models/bert-classifier'),

((SELECT id FROM mdl_models WHERE model_name = 'Stable Diffusion XL'),
 (SELECT id FROM mdl_model_versions WHERE model_id = (SELECT id FROM mdl_models WHERE model_name = 'Stable Diffusion XL')),
 'SDXL Image Generator', 'Despliegue del generador de imágenes Stable Diffusion XL',
 'REST_API', 'dev', 'RUNNING', 'https://dev.codeflowx.company.com/v1/models/sdxl-generator');

-- =====================================================
-- INICIALIZACIÓN DE MÉTRICAS DE DESPLIEGUE
-- =====================================================

-- Insertar métricas de despliegue de demo
INSERT INTO mdl_deployment_metrics (
    deployment_id, metric_name, metric_value, metric_unit, timestamp, labels
) VALUES
-- Métricas para CodeflowX LLM Production
((SELECT id FROM mdl_model_deployments WHERE name = 'CodeflowX LLM Production'),
 'requests_per_second', 45.2, 'requests/sec', CURRENT_TIMESTAMP - INTERVAL '1 hour', '{"environment": "prod"}'),
((SELECT id FROM mdl_model_deployments WHERE name = 'CodeflowX LLM Production'),
 'average_response_time', 1250, 'ms', CURRENT_TIMESTAMP - INTERVAL '1 hour', '{"environment": "prod"}'),
((SELECT id FROM mdl_model_deployments WHERE name = 'CodeflowX LLM Production'),
 'error_rate', 0.02, 'percent', CURRENT_TIMESTAMP - INTERVAL '1 hour', '{"environment": "prod"}'),
((SELECT id FROM mdl_model_deployments WHERE name = 'CodeflowX LLM Production'),
 'cpu_usage', 65.8, 'percent', CURRENT_TIMESTAMP - INTERVAL '1 hour', '{"environment": "prod"}'),
((SELECT id FROM mdl_model_deployments WHERE name = 'CodeflowX LLM Production'),
 'memory_usage', 78.3, 'percent', CURRENT_TIMESTAMP - INTERVAL '1 hour', '{"environment": "prod"}'),

-- Métricas para BERT Document Classifier
((SELECT id FROM mdl_model_deployments WHERE name = 'BERT Document Classifier'),
 'requests_per_second', 120.5, 'requests/sec', CURRENT_TIMESTAMP - INTERVAL '1 hour', '{"environment": "staging"}'),
((SELECT id FROM mdl_model_deployments WHERE name = 'BERT Document Classifier'),
 'average_response_time', 450, 'ms', CURRENT_TIMESTAMP - INTERVAL '1 hour', '{"environment": "staging"}'),
((SELECT id FROM mdl_model_deployments WHERE name = 'BERT Document Classifier'),
 'error_rate', 0.01, 'percent', CURRENT_TIMESTAMP - INTERVAL '1 hour', '{"environment": "staging"}'),
((SELECT id FROM mdl_model_deployments WHERE name = 'BERT Document Classifier'),
 'cpu_usage', 45.2, 'percent', CURRENT_TIMESTAMP - INTERVAL '1 hour', '{"environment": "staging"}'),
((SELECT id FROM mdl_model_deployments WHERE name = 'BERT Document Classifier'),
 'memory_usage', 52.7, 'percent', CURRENT_TIMESTAMP - INTERVAL '1 hour', '{"environment": "staging"}'),

-- Métricas para SDXL Image Generator
((SELECT id FROM mdl_model_deployments WHERE name = 'SDXL Image Generator'),
 'requests_per_second', 15.8, 'requests/sec', CURRENT_TIMESTAMP - INTERVAL '1 hour', '{"environment": "dev"}'),
((SELECT id FROM mdl_model_deployments WHERE name = 'SDXL Image Generator'),
 'average_response_time', 8500, 'ms', CURRENT_TIMESTAMP - INTERVAL '1 hour', '{"environment": "dev"}'),
((SELECT id FROM mdl_model_deployments WHERE name = 'SDXL Image Generator'),
 'error_rate', 0.05, 'percent', CURRENT_TIMESTAMP - INTERVAL '1 hour', '{"environment": "dev"}'),
((SELECT id FROM mdl_model_deployments WHERE name = 'SDXL Image Generator'),
 'gpu_usage', 85.3, 'percent', CURRENT_TIMESTAMP - INTERVAL '1 hour', '{"environment": "dev"}'),
((SELECT id FROM mdl_model_deployments WHERE name = 'SDXL Image Generator'),
 'memory_usage', 92.1, 'percent', CURRENT_TIMESTAMP - INTERVAL '1 hour', '{"environment": "dev"}');
```

## Pendiente

## Conclusión

El módulo de Model Management proporciona una gestión completa del ciclo de vida de modelos de IA, incluyendo:

- **Gestión de Modelos**: Creación, versionado y registro de modelos
- **Experimentos**: Diseño, ejecución y tracking de experimentos con MLflow
- **Artefactos**: Gestión de archivos y artefactos del modelo
- **Despliegue**: Configuración y monitorización de despliegues
- **Integración MLflow**: Tracking completo de experimentos y versionado
- **Métricas y Monitoreo**: Captura de métricas de rendimiento y uso

El sistema está diseñado para ser escalable, auditable y fácilmente integrable con MLflow y otros módulos del portal.

## Notas de Implementación

### 1\. **Sistema de Búsqueda de Marketplace**: Integrado con lek-server para búsqueda automática en +7 proveedores principales con +3,000,000 modelos

### 2\. **Gestión de Proveedores**: Sistema completo de proveedores de IA con gestión de tokens encriptados y rate limiting

### 3\. **Sistema de Costes**: Control granular de costes por tokens, imágenes, audio y llamadas API con moneda configurable

### 4\. **Precios de Venta**: Múltiples niveles de precios (FREE, BASIC, PROFESSIONAL, ENTERPRISE, CUSTOM) con suscripciones

### 5\. **Requisitos de Hardware**: Especificaciones mínimas y recomendadas para CPU, RAM, GPU y almacenamiento

### 6\. **Cumplimiento Normativo**: Soporte para IA Act Europea, GDPR, HIPAA, SOC2, ISO27001 y otras normativas

### 7\. **Estadísticas de Uso**: Métricas detalladas de consumo, rendimiento y facturación con análisis temporal

### 8\. **Integración MLflow**: Tracking completo de experimentos, versionado y artefactos con auditoría

### 9\. **Descarga Automática**: Sistema asíncrono de descarga de modelos del marketplace con progreso y manejo de errores

### 10\. **Normalización Completa**: Todas las tablas siguen las reglas 3NF, BCNF con prefijos `mdl_` y PKs autonuméricas

## Uso del Script

### 1\. **Ejecutar en orden**:

- Primero el script de limpieza (DROP)
- Luego el script de creación de tablas
- Finalmente el script de índices

### 2\. **Configurar lek-server**: Asegurar que el servidor lek-server esté disponible en la URL configurada

### 3\. **Configurar proveedores**: Crear los proveedores de IA necesarios antes de crear modelos

### 4\. **Verificar integración**: Probar la búsqueda de marketplace y descarga de modelos

## Verificación de Datos

```plaintext
-- Verificar proveedores creados
SELECT 'Proveedores de IA:' as info, COUNT(*) as total FROM mdl_ai_providers;

-- Verificar tokens creados
SELECT 'Tokens de proveedores:' as info, COUNT(*) as total FROM mdl_provider_tokens;

-- Verificar modelos creados
SELECT 'Modelos:' as info, COUNT(*) as total FROM mdl_models;

-- Verificar costes de modelos
SELECT 'Costes de modelos:' as info, COUNT(*) as total FROM mdl_model_costs;

-- Verificar precios de modelos
SELECT 'Precios de modelos:' as info, COUNT(*) as total FROM mdl_model_pricing;

-- Verificar requisitos de hardware
SELECT 'Requisitos de hardware:' as info, COUNT(*) as total FROM mdl_hardware_requirements;

-- Verificar cumplimiento normativo
SELECT 'Cumplimiento normativo:' as info, COUNT(*) as total FROM mdl_model_compliance;

-- Verificar estadísticas de uso
SELECT 'Estadísticas de uso:' as info, COUNT(*) as total FROM mdl_usage_statistics;

-- Verificar modelos del marketplace
SELECT 'Modelos del marketplace:' as info, COUNT(*) as total FROM mdl_marketplace_models;

-- Verificar descargas de modelos
SELECT 'Descargas de modelos:' as info, COUNT(*) as total FROM mdl_model_downloads;

-- Verificar versiones de modelos
SELECT 'Versiones de modelos:' as info, COUNT(*) as total FROM mdl_model_versions;

-- Verificar artefactos de modelos
SELECT 'Artefactos de modelos:' as info, COUNT(*) as total FROM mdl_model_artifacts;

-- Verificar experimentos de modelos
SELECT 'Experimentos de modelos:' as info, COUNT(*) as total FROM mdl_model_experiments;

-- Verificar ejecuciones de experimentos
SELECT 'Ejecuciones de experimentos:' as info, COUNT(*) as total FROM mdl_experiment_runs;

-- Verificar despliegues de modelos
SELECT 'Despliegues de modelos:' as info, COUNT(*) as total FROM mdl_model_deployments;

-- Verificar métricas de despliegue
SELECT 'Métricas de despliegue:' as info, COUNT(*) as total FROM mdl_deployment_metrics;
```

## Conclusión

El módulo de Model Management proporciona una gestión completa del ciclo de vida de modelos de IA, incluyendo:

- **Gestión de Proveedores**: Sistema completo de proveedores de IA con gestión de tokens encriptados
- **Gestión de Modelos**: Creación, versionado, categorización y metadata completa de modelos
- **Sistema de Costes y Precios**: Control granular de costes operativos y precios de venta
- **Requisitos de Hardware**: Especificaciones técnicas para ejecución y escalado
- **Cumplimiento Normativo**: Soporte para normativas internacionales y certificaciones
- **Estadísticas y Métricas**: Análisis completo de uso, rendimiento y facturación
- **Integración con Marketplace**: Búsqueda automática en +7 proveedores con +3,000,000 modelos
- **Descarga Automática**: Sistema asíncrono de descarga con progreso y manejo de errores
- **Integración MLflow**: Tracking completo de experimentos y versionado
- **Monitoreo Prometheus**: Métricas avanzadas para auditoría y rendimiento

El sistema está completamente normalizado siguiendo las reglas de 3NF, BCNF, con prefijos de módulos `mdl_`, PKs autonuméricas y arquitectura hexagonal aplicando principios SOLID y KISS. La integración con lek-server permite acceso a una base de datos masiva de modelos de IA de múltiples marketplaces, proporcionando una experiencia unificada para la gestión y descubrimiento de modelos.

CREATE INDEX idx_mdl_deployment_metrics_name ON mdl_deployment_metrics(metric_name);

## Training Hub - Centro de Entrenamiento Unificado

### Descripción del Training Hub

El Training Hub es un módulo unificado que centraliza la gestión de:

- **Adapters**: Modelos especializados para tecnologías, dominios de negocio y casos genéricos
- **Fine-tuning**: Ejecuciones de entrenamiento con métricas, costes y artefactos
- **Artefactos**: Archivos generados durante el entrenamiento (checkpoints, modelos, logs)
- **Datasets**: Conjuntos de datos utilizados para el entrenamiento

### Entidades del Training Hub

#### 1. Training Adapter (Adaptador de Entrenamiento)

```java
@Entity
@Table(name = "mdl_training_adapters")
public class TrainingAdapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "adapter_name", nullable = false)
    private String adapterName;

    @Column(name = "adapter_kind", nullable = false)
    @Enumerated(EnumType.STRING)
    private AdapterKind adapterKind; // TECHNOLOGY, DOMAIN, GENERIC

    @Column(name = "base_model_id", nullable = false)
    private Long baseModelId; // Referencia a mdl_models.id

    @Column(name = "target_domain")
    private String targetDomain; // JavaScript, ES, General, etc.

    @Column(name = "training_status")
    @Enumerated(EnumType.STRING)
    private TrainingStatus trainingStatus;

    @Column(name = "training_config")
    private String trainingConfig; // JSON con configuración de entrenamiento

    @Column(name = "performance_metrics")
    private String performanceMetrics; // JSON con métricas de rendimiento

    @Column(name = "created_by")
    private Long createdBy; // Referencia a cor_users.id

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "base_model_id", insertable = false, updatable = false)
    private Model baseModel;

    @OneToMany(mappedBy = "adapter", cascade = CascadeType.ALL)
    private List<TrainingRun> trainingRuns = new ArrayList<>();

    @OneToMany(mappedBy = "adapter", cascade = CascadeType.ALL)
    private List<TrainingArtifact> artifacts = new ArrayList<>();
}

public enum AdapterKind {
    TECHNOLOGY, DOMAIN, GENERIC
}

public enum TrainingStatus {
    PENDING, TRAINING, EVALUATING, READY, FAILED, ARCHIVED
}
```

#### 2. Training Run (Ejecución de Entrenamiento)

```java
@Entity
@Table(name = "mdl_training_runs")
public class TrainingRun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "adapter_id")
    private Long adapterId; // Referencia a mdl_training_adapters.id

    @Column(name = "run_name", nullable = false)
    private String runName;

    @Column(name = "base_model_id", nullable = false)
    private Long baseModelId; // Referencia a mdl_models.id

    @Column(name = "provider_id")
    private Long providerId; // Referencia a mdl_ai_providers.id

    @Column(name = "training_status")
    @Enumerated(EnumType.STRING)
    private RunStatus runStatus;

    @Column(name = "mlflow_run_id")
    private String mlflowRunId;

    @Column(name = "training_metrics")
    private String trainingMetrics; // JSON con métricas de entrenamiento

    @Column(name = "cost_usd")
    private BigDecimal costUSD;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "adapter_id", insertable = false, updatable = false)
    private TrainingAdapter adapter;

    @OneToMany(mappedBy = "trainingRun", cascade = CascadeType.ALL)
    private List<TrainingArtifact> artifacts = new ArrayList<>();
}
```

#### 3. Training Artifact (Artefacto de Entrenamiento)

```java
@Entity
@Table(name = "mdl_training_artifacts")
public class TrainingArtifact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "artifact_name", nullable = false)
    private String artifactName;

    @Column(name = "artifact_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ArtifactType artifactType;

    @Column(name = "origin_type")
    @Enumerated(EnumType.STRING)
    private OriginType originType; // ADAPTER, FINETUNE

    @Column(name = "linked_id")
    private Long linkedId; // ID del adapter o run que lo generó

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "checksum")
    private String checksum;

    @Column(name = "mlflow_artifact_uri")
    private String mlflowArtifactUri;

    @Column(name = "metadata")
    private String metadata; // JSON con metadatos adicionales

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Long createdBy; // Referencia a cor_users.id
}

public enum OriginType {
    ADAPTER, FINETUNE
}
```

#### 4. Training Dataset (Dataset de Entrenamiento)

```java
@Entity
@Table(name = "mdl_training_datasets")
public class TrainingDataset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dataset_name", nullable = false)
    private String datasetName;

    @Column(name = "dataset_description")
    private String datasetDescription;

    @Column(name = "dataset_type")
    @Enumerated(EnumType.STRING)
    private DatasetType datasetType;

    @Column(name = "data_source")
    private String dataSource; // URL o ruta del dataset

    @Column(name = "data_format")
    private String dataFormat; // JSON, CSV, Parquet, etc.

    @Column(name = "total_samples")
    private Long totalSamples;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "checksum")
    private String checksum;

    @Column(name = "version")
    private String version;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy; // Referencia a cor_users.id
}

public enum DatasetType {
    TEXT, IMAGE, AUDIO, MULTIMODAL, CODE, TABULAR
}
```

### APIs del Training Hub

#### Training Adapter Management

```plaintext
GET    /api/v1/training/adapters
GET    /api/v1/training/adapters/{id}
POST   /api/v1/training/adapters
PUT    /api/v1/training/adapters/{id}
DELETE /api/v1/training/adapters/{id}
POST   /api/v1/training/adapters/{id}/start-training
POST   /api/v1/training/adapters/{id}/stop-training
GET    /api/v1/training/adapters/{id}/runs
GET    /api/v1/training/adapters/{id}/artifacts
POST   /api/v1/training/adapters/{id}/promote
POST   /api/v1/training/adapters/{id}/archive
```

#### Training Run Management

```plaintext
GET    /api/v1/training/runs
GET    /api/v1/training/runs/{id}
POST   /api/v1/training/runs
PUT    /api/v1/training/runs/{id}
DELETE /api/v1/training/runs/{id}
POST   /api/v1/training/runs/{id}/start
POST   /api/v1/training/runs/{id}/stop
GET    /api/v1/training/runs/{id}/metrics
GET    /api/v1/training/runs/{id}/artifacts
GET    /api/v1/training/runs/{id}/logs
```

#### Training Artifact Management

```plaintext
GET    /api/v1/training/artifacts
GET    /api/v1/training/artifacts/{id}
POST   /api/v1/training/artifacts
PUT    /api/v1/training/artifacts/{id}
DELETE /api/v1/training/artifacts/{id}
GET    /api/v1/training/artifacts/{id}/download
POST   /api/v1/training/artifacts/{id}/validate
GET    /api/v1/training/artifacts/{id}/metadata
```

#### Training Dataset Management

```plaintext
GET    /api/v1/training/datasets
GET    /api/v1/training/datasets/{id}
POST   /api/v1/training/datasets
PUT    /api/v1/training/datasets/{id}
DELETE /api/v1/training/datasets/{id}
POST   /api/v1/training/datasets/{id}/upload
GET    /api/v1/training/datasets/{id}/download
POST   /api/v1/training/datasets/{id}/validate
GET    /api/v1/training/datasets/{id}/preview
```

### Integración con Fuentes Externas

#### 1. MLflow Integration Service

```java
@Service
public class MLflowTrainingService {

    @Autowired
    private MLflowClient mlflowClient;

    @Autowired
    private TrainingRunRepository trainingRunRepository;

    @Autowired
    private TrainingArtifactRepository artifactRepository;

    /**
     * Crear experimento de entrenamiento en MLflow
     */
    public String createTrainingExperiment(String name, String description) {
        try {
            CreateExperiment createExperiment = CreateExperiment.newBuilder()
                .name(name)
                .build();

            CreateExperiment.Response response = mlflowClient.createExperiment(createExperiment);
            return response.getExperimentId();
        } catch (Exception e) {
            throw new MLflowException("Failed to create training experiment: " + e.getMessage(), e);
        }
    }

    /**
     * Iniciar run de entrenamiento en MLflow
     */
    public String startTrainingRun(String experimentId, String runName, Map<String, String> tags) {
        try {
            CreateRun createRun = CreateRun.newBuilder()
                .experimentId(experimentId)
                .runName(runName)
                .tags(tags)
                .build();

            CreateRun.Response response = mlflowClient.createRun(createRun);
            return response.getRun().getRunId();
        } catch (Exception e) {
            throw new MLflowException("Failed to start training run: " + e.getMessage(), e);
        }
    }

    /**
     * Log de métricas de entrenamiento
     */
    public void logTrainingMetrics(String runId, Map<String, Double> metrics) {
        try {
            for (Map.Entry<String, Double> entry : metrics.entrySet()) {
                LogMetric logMetric = LogMetric.newBuilder()
                    .runId(runId)
                    .key(entry.getKey())
                    .value(entry.getValue())
                    .build();

                mlflowClient.logMetric(logMetric);
            }
        } catch (Exception e) {
            throw new MLflowException("Failed to log training metrics: " + e.getMessage(), e);
        }
    }

    /**
     * Log de artefactos de entrenamiento
     */
    public void logTrainingArtifact(String runId, String artifactPath, File artifact) {
        try {
            mlflowClient.logArtifact(runId, artifact, artifactPath);
        } catch (Exception e) {
            throw new MLflowException("Failed to log training artifact: " + e.getMessage(), e);
        }
    }
}
```

#### 2. External Provider Integration Service

```java
@Service
public class ExternalProviderTrainingService {

    @Autowired
    private AIProviderRepository providerRepository;

    @Autowired
    private TrainingRunRepository trainingRunRepository;

    /**
     * Iniciar entrenamiento en proveedor externo (AWS, OVH, etc.)
     */
    public TrainingRun startExternalTraining(TrainingRunRequest request) {
        try {
            AIProvider provider = providerRepository.findById(request.getProviderId())
                .orElseThrow(() -> new ProviderNotFoundException("Provider not found"));

            // Crear registro de entrenamiento
            TrainingRun trainingRun = new TrainingRun();
            trainingRun.setRunName(request.getRunName());
            trainingRun.setBaseModelId(request.getBaseModelId());
            trainingRun.setProviderId(request.getProviderId());
            trainingRun.setRunStatus(RunStatus.PENDING);
            trainingRun.setStartedAt(LocalDateTime.now());
            trainingRun.setCreatedAt(LocalDateTime.now());

            TrainingRun savedRun = trainingRunRepository.save(trainingRun);

            // Iniciar entrenamiento asíncrono en el proveedor
            CompletableFuture.runAsync(() -> {
                executeExternalTraining(savedRun.getId(), request, provider);
            });

            return savedRun;
        } catch (Exception e) {
            throw new TrainingException("Failed to start external training: " + e.getMessage(), e);
        }
    }

    /**
     * Ejecutar entrenamiento en proveedor externo
     */
    private void executeExternalTraining(Long runId, TrainingRunRequest request, AIProvider provider) {
        try {
            TrainingRun trainingRun = trainingRunRepository.findById(runId)
                .orElseThrow(() -> new TrainingRunNotFoundException("Training run not found"));

            // Actualizar estado a entrenando
            trainingRun.setRunStatus(RunStatus.RUNNING);
            trainingRunRepository.save(trainingRun);

            // Lógica específica del proveedor
            switch (provider.getProviderType()) {
                case AWS:
                    executeAWSTraining(trainingRun, request);
                    break;
                case OVH:
                    executeOVHTraining(trainingRun, request);
                    break;
                case GOOGLE_CLOUD:
                    executeGoogleCloudTraining(trainingRun, request);
                    break;
                default:
                    throw new UnsupportedProviderException("Provider not supported: " + provider.getProviderType());
            }

        } catch (Exception e) {
            // Actualizar estado a fallido
            TrainingRun trainingRun = trainingRunRepository.findById(runId).orElse(null);
            if (trainingRun != null) {
                trainingRun.setRunStatus(RunStatus.FAILED);
                trainingRunRepository.save(trainingRun);
            }
        }
    }

    private void executeAWSTraining(TrainingRun trainingRun, TrainingRunRequest request) {
        // Implementación específica para AWS
        // - Crear instancia EC2 con GPU
        // - Configurar SageMaker o ECS
        // - Ejecutar script de entrenamiento
        // - Monitorizar progreso
    }

    private void executeOVHTraining(TrainingRun trainingRun, TrainingRunRequest request) {
        // Implementación específica para OVH
        // - Crear instancia con GPU
        // - Configurar Docker/container
        // - Ejecutar entrenamiento
        // - Monitorizar progreso
    }
}
```

#### 3. Training Analytics Service

```java
@Service
public class TrainingAnalyticsService {

    @Autowired
    private TrainingRunRepository trainingRunRepository;

    @Autowired
    private TrainingAdapterRepository adapterRepository;

    @Autowired
    private TrainingArtifactRepository artifactRepository;

    /**
     * Obtener métricas generales del Training Hub
     */
    public TrainingHubMetrics getTrainingHubMetrics() {
        try {
            long totalAdapters = adapterRepository.count();
            long totalRuns = trainingRunRepository.count();
            long totalArtifacts = artifactRepository.count();

            // Calcular almacenamiento total
            long totalStorageBytes = artifactRepository.findAll().stream()
                .mapToLong(artifact -> artifact.getFileSizeBytes() != null ? artifact.getFileSizeBytes() : 0)
                .sum();

            // Calcular costes totales
            BigDecimal totalCosts = trainingRunRepository.findAll().stream()
                .map(run -> run.getCostUSD() != null ? run.getCostUSD() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            return TrainingHubMetrics.builder()
                .totalAdapters(totalAdapters)
                .totalRuns(totalRuns)
                .totalArtifacts(totalArtifacts)
                .totalStorageGB(String.format("%.1f GB", totalStorageBytes / (1024.0 * 1024.0 * 1024.0)))
                .totalCostsUSD(totalCosts)
                .build();

        } catch (Exception e) {
            throw new AnalyticsException("Failed to get training hub metrics: " + e.getMessage(), e);
        }
    }

    /**
     * Obtener distribución de adapters por tipo
     */
    public Map<AdapterKind, Long> getAdapterDistribution() {
        try {
            return adapterRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                    TrainingAdapter::getAdapterKind,
                    Collectors.counting()
                ));
        } catch (Exception e) {
            throw new AnalyticsException("Failed to get adapter distribution: " + e.getMessage(), e);
        }
    }

    /**
     * Obtener métricas de rendimiento de entrenamiento
     */
    public TrainingPerformanceMetrics getTrainingPerformanceMetrics() {
        try {
            List<TrainingRun> completedRuns = trainingRunRepository.findByRunStatus(RunStatus.FINISHED);

            if (completedRuns.isEmpty()) {
                return TrainingPerformanceMetrics.builder()
                    .averageDurationMinutes(0)
                    .averageCostUSD(BigDecimal.ZERO)
                    .successRate(0.0)
                    .build();
            }

            double averageDuration = completedRuns.stream()
                .mapToInt(run -> run.getDurationMinutes() != null ? run.getDurationMinutes() : 0)
                .average()
                .orElse(0.0);

            BigDecimal averageCost = completedRuns.stream()
                .map(run -> run.getCostUSD() != null ? run.getCostUSD() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(completedRuns.size()), 2, RoundingMode.HALF_UP);

            long successfulRuns = completedRuns.stream()
                .filter(run -> run.getRunStatus() == RunStatus.FINISHED)
                .count();

            double successRate = (double) successfulRuns / completedRuns.size() * 100;

            return TrainingPerformanceMetrics.builder()
                .averageDurationMinutes((int) Math.round(averageDuration))
                .averageCostUSD(averageCost)
                .successRate(successRate)
                .build();

        } catch (Exception e) {
            throw new AnalyticsException("Failed to get training performance metrics: " + e.getMessage(), e);
        }
    }
}

@Data
@Builder
public class TrainingHubMetrics {
    private long totalAdapters;
    private long totalRuns;
    private long totalArtifacts;
    private String totalStorageGB;
    private BigDecimal totalCostsUSD;
}

@Data
@Builder
public class TrainingPerformanceMetrics {
    private int averageDurationMinutes;
    private BigDecimal averageCostUSD;
    private double successRate;
}
```

### Scripts de Base de Datos para Training Hub

#### Creación de Tablas del Training Hub

```sql
-- =====================================================
-- TABLAS DEL TRAINING HUB
-- =====================================================

-- Tabla de adaptadores de entrenamiento
CREATE TABLE mdl_training_adapters (
    id BIGSERIAL PRIMARY KEY,
    adapter_name VARCHAR(255) NOT NULL,
    adapter_kind VARCHAR(50) NOT NULL,
    base_model_id BIGINT REFERENCES mdl_models(id) ON DELETE CASCADE,
    target_domain VARCHAR(100),
    training_status VARCHAR(50) DEFAULT 'PENDING',
    training_config TEXT,
    performance_metrics TEXT,
    created_by BIGINT REFERENCES cor_users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de ejecuciones de entrenamiento
CREATE TABLE mdl_training_runs (
    id BIGSERIAL PRIMARY KEY,
    adapter_id BIGINT REFERENCES mdl_training_adapters(id) ON DELETE CASCADE,
    run_name VARCHAR(255) NOT NULL,
    base_model_id BIGINT REFERENCES mdl_models(id) ON DELETE CASCADE,
    provider_id BIGINT REFERENCES mdl_ai_providers(id),
    training_status VARCHAR(50) DEFAULT 'PENDING',
    mlflow_run_id VARCHAR(100),
    training_metrics TEXT,
    cost_usd DECIMAL(15,2),
    duration_minutes INTEGER,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de artefactos de entrenamiento
CREATE TABLE mdl_training_artifacts (
    id BIGSERIAL PRIMARY KEY,
    artifact_name VARCHAR(255) NOT NULL,
    artifact_type VARCHAR(50) NOT NULL,
    origin_type VARCHAR(50) NOT NULL,
    linked_id BIGINT,
    file_path VARCHAR(500),
    file_size_bytes BIGINT,
    checksum VARCHAR(64),
    mlflow_artifact_uri VARCHAR(500),
    metadata TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT REFERENCES cor_users(id)
);

-- Tabla de datasets de entrenamiento
CREATE TABLE mdl_training_datasets (
    id BIGSERIAL PRIMARY KEY,
    dataset_name VARCHAR(255) NOT NULL,
    dataset_description TEXT,
    dataset_type VARCHAR(50),
    data_source VARCHAR(500),
    data_format VARCHAR(50),
    total_samples BIGINT,
    file_size_bytes BIGINT,
    checksum VARCHAR(64),
    version VARCHAR(50),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT REFERENCES cor_users(id)
);

-- Índices para optimización
CREATE INDEX idx_training_adapters_base_model ON mdl_training_adapters(base_model_id);
CREATE INDEX idx_training_adapters_kind ON mdl_training_adapters(adapter_kind);
CREATE INDEX idx_training_adapters_status ON mdl_training_adapters(training_status);

CREATE INDEX idx_training_runs_adapter ON mdl_training_runs(adapter_id);
CREATE INDEX idx_training_runs_base_model ON mdl_training_runs(base_model_id);
CREATE INDEX idx_training_runs_provider ON mdl_training_runs(provider_id);
CREATE INDEX idx_training_runs_status ON mdl_training_runs(training_status);

CREATE INDEX idx_training_artifacts_origin ON mdl_training_artifacts(origin_type, linked_id);
CREATE INDEX idx_training_artifacts_type ON mdl_training_artifacts(artifact_type);

CREATE INDEX idx_training_datasets_type ON mdl_training_datasets(dataset_type);
CREATE INDEX idx_training_datasets_active ON mdl_training_datasets(is_active);
```

### Configuración de Aplicación para Training Hub

```yaml
# application-training-hub.yml
training-hub:
  # Configuración de MLflow
  mlflow:
    tracking-uri: ${MLFLOW_TRACKING_URI:http://localhost:5000}
    experiment-name: ${MLFLOW_EXPERIMENT_NAME:codeflowx-training}
    artifact-root: ${MLFLOW_ARTIFACT_ROOT:./mlruns}

  # Configuración de proveedores externos
  external-providers:
    aws:
      region: ${AWS_REGION:us-east-1}
      instance-types:
        - "g4dn.xlarge"
        - "g4dn.2xlarge"
        - "p3.2xlarge"
      max-instances: 5
      spot-instances: true

    ovh:
      region: ${OVH_REGION:GRA}
      instance-types:
        - "gpu-1"
        - "gpu-2"
        - "gpu-3"
      max-instances: 3

    google-cloud:
      region: ${GCP_REGION:us-central1}
      instance-types:
        - "n1-standard-4"
        - "n1-standard-8"
        - "n1-standard-16"
      max-instances: 4

  # Configuración de entrenamiento
  training:
    max-concurrent-runs: 10
    default-timeout-hours: 24
    auto-cleanup-artifacts: true
    artifact-retention-days: 30
    cost-tracking-enabled: true
    performance-monitoring: true

  # Configuración de datasets
  datasets:
    max-file-size-gb: 10
    supported-formats: ["json", "csv", "parquet", "h5", "pkl"]
    auto-validation: true
    versioning-enabled: true
    backup-enabled: true

  # Configuración de analytics
  analytics:
    metrics-collection-interval: 60
    performance-tracking: true
    cost-analysis: true
    trend-analysis: true
    alerting-enabled: true
```

## Conclusión del Training Hub

El Training Hub proporciona una plataforma unificada para la gestión completa del ciclo de vida de entrenamiento de modelos de IA, incluyendo:

- **Gestión de Adapters**: Creación y gestión de adaptadores especializados para diferentes dominios
- **Fine-tuning Unificado**: Ejecución y monitorización de entrenamientos en múltiples proveedores
- **Gestión de Artefactos**: Control centralizado de todos los archivos generados durante el entrenamiento
- **Integración Multi-proveedor**: Soporte para AWS, OVH, Google Cloud y otros proveedores
- **Analytics Avanzados**: Métricas de rendimiento, costes y análisis de tendencias
- **Integración MLflow**: Tracking completo de experimentos y versionado
- **Gestión de Datasets**: Control de versiones y validación automática de datos de entrenamiento

El sistema está diseñado para ser escalable, auditable y fácilmente integrable con múltiples fuentes de datos externas, proporcionando una experiencia unificada para el entrenamiento de modelos de IA.
