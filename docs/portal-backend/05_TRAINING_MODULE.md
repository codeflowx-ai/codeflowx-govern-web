## Módulo de Training - Portal Backend

## Descripción General

El módulo de Training gestiona todo el proceso de entrenamiento de modelos de IA, incluyendo jobs, recursos, costes, datasets, experimentos y monitorización. Proporciona una API completa para la orquestación del entrenamiento.

## Entidades del Sistema

### 1\. TrainingJob

```java
@Entity
@Table(name = "training_jobs")
public class TrainingJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Column(name = "job_type")
    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Column(name = "model_type")
    private String modelType;

    @Column(name = "dataset_id")
    private Long datasetId;

    @Column(name = "hyperparameters")
    private String hyperparameters; // JSON

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "estimated_duration_hours")
    private Double estimatedDurationHours;

    @Column(name = "actual_duration_hours")
    private Double actualDurationHours;

    @Column(name = "cost_usd")
    private BigDecimal costUsd;

    @Column(name = "resource_config")
    private String resourceConfig; // JSON

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "trainingJob", cascade = CascadeType.ALL)
    private List<traininglog> logs = new ArrayList&lt;&gt;();

    @OneToMany(mappedBy = "trainingJob", cascade = CascadeType.ALL)
    private List<trainingmetric> metrics = new ArrayList&lt;&gt;();

    @OneToMany(mappedBy = "trainingJob", cascade = CascadeType.ALL)
    private List<trainingartifact> artifacts = new ArrayList&lt;&gt;();
}

public enum JobStatus {
    PENDING, QUEUED, RUNNING, COMPLETED, FAILED, CANCELLED, PAUSED
}

public enum JobType {
    FINE_TUNING, PRETRAINING, TRANSFER_LEARNING, HYPERPARAMETER_TUNING
}
```

### 2\. Dataset

```java
@Entity
@Table(name = "datasets")
public class Dataset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "dataset_type")
    @Enumerated(EnumType.STRING)
    private DatasetType datasetType;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "record_count")
    private Long recordCount;

    @Column(name = "format")
    private String format; // CSV, JSON, PARQUET, etc.

    @Column(name = "schema")
    private String schema; // JSON schema

    @Column(name = "metadata")
    private String metadata; // JSON metadata

    @Column(name = "is_processed")
    private Boolean isProcessed = false;

    @Column(name = "processing_status")
    @Enumerated(EnumType.STRING)
    private ProcessingStatus processingStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "dataset", cascade = CascadeType.ALL)
    private List<trainingjob> trainingJobs = new ArrayList&lt;&gt;();
}

public enum DatasetType {
    TEXT, IMAGE, AUDIO, VIDEO, TABULAR, MULTIMODAL
}

public enum ProcessingStatus {
    PENDING, PROCESSING, COMPLETED, FAILED
}
```

### 3\. Experiment

```java
@Entity
@Table(name = "experiments")
public class Experiment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "user_id")
    private Long userId;

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
    private List<trainingjob> trainingJobs = new ArrayList&lt;&gt;();
}

public enum ExperimentType {
    HYPERPARAMETER_OPTIMIZATION, ARCHITECTURE_SEARCH, FEATURE_ENGINEERING,
    MODEL_COMPARISON, AB_TESTING
}

public enum ExperimentStatus {
    PLANNED, RUNNING, COMPLETED, FAILED, CANCELLED
}
```

### 4\. TrainingResource

```java
@Entity
@Table(name = "training_resources")
public class TrainingResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "resource_type")
    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    @Column(name = "specifications")
    private String specifications; // JSON specs

    @Column(name = "cost_per_hour_usd")
    private BigDecimal costPerHourUsd;

    @Column(name = "availability")
    @Enumerated(EnumType.STRING)
    private ResourceAvailability availability;

    @Column(name = "current_utilization")
    private Double currentUtilization;

    @Column(name = "max_utilization")
    private Double maxUtilization;

    @Column(name = "location")
    private String location;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum ResourceType {
    GPU, CPU, MEMORY, STORAGE, NETWORK
}

public enum ResourceAvailability {
    AVAILABLE, IN_USE, MAINTENANCE, OFFLINE
}
```

### 5\. TrainingCost

```java
@Entity
@Table(name = "training_costs")
public class TrainingCost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_job_id")
    private Long trainingJobId;

    @Column(name = "resource_id")
    private Long resourceId;

    @Column(name = "cost_type")
    @Enumerated(EnumType.STRING)
    private CostType costType;

    @Column(name = "amount_usd")
    private BigDecimal amountUsd;

    @Column(name = "duration_hours")
    private Double durationHours;

    @Column(name = "rate_per_hour_usd")
    private BigDecimal ratePerHourUsd;

    @Column(name = "description")
    private String description;

    @Column(name = "billing_period")
    private String billingPeriod; // hourly, daily, monthly

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

public enum CostType {
    COMPUTE, STORAGE, NETWORK, SOFTWARE, LICENSING, SUPPORT
}
```

### 6\. TrainingMetric

```java
@Entity
@Table(name = "training_metrics")
public class TrainingMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_job_id")
    private Long trainingJobId;

    @Column(name = "metric_name")
    private String metricName;

    @Column(name = "metric_value")
    private Double metricValue;

    @Column(name = "metric_unit")
    private String metricUnit;

    @Column(name = "epoch")
    private Integer epoch;

    @Column(name = "step")
    private Long step;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "metadata")
    private String metadata; // JSON additional info
}
```

### 7\. TrainingLog

```java
@Entity
@Table(name = "training_logs")
public class TrainingLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_job_id")
    private Long trainingJobId;

    @Column(name = "log_level")
    @Enumerated(EnumType.STRING)
    private LogLevel logLevel;

    @Column(name = "message")
    private String message;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "source")
    private String source; // component that generated the log

    @Column(name = "metadata")
    private String metadata; // JSON additional context
}

public enum LogLevel {
    DEBUG, INFO, WARNING, ERROR, CRITICAL
}
```

### 8\. TrainingArtifact

```java
@Entity
@Table(name = "training_artifacts")
public class TrainingArtifact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_job_id")
    private Long trainingJobId;

    @Column(name = "artifact_type")
    @Enumerated(EnumType.STRING)
    private ArtifactType artifactType;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "checksum")
    private String checksum;

    @Column(name = "metadata")
    private String metadata; // JSON metadata

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

public enum ArtifactType {
    MODEL, CHECKPOINT, LOGS, METRICS, CONFIG, DATASET, REPORT
}
```

### 7\. TrainingInfrastructure

```java
@Entity
@Table(name = "trn_training_infrastructure")
public class TrainingInfrastructure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_job_id")
    private Long trainingJobId;

    @Column(name = "infrastructure_type")
    @Enumerated(EnumType.STRING)
    private InfrastructureType infrastructureType;

    @Column(name = "provider")
    private String provider; // AWS, GCP, Azure, OVH, RunPod, Vast.ai, Self-hosted

    @Column(name = "instance_type")
    private String instanceType; // g4dn.xlarge, n1-standard-4, etc.

    @Column(name = "gpu_type")
    private String gpuType; // nvidia-tesla-v100, nvidia-a100, etc.

    @Column(name = "gpu_count")
    private Integer gpuCount;

    @Column(name = "cpu_cores")
    private Integer cpuCores;

    @Column(name = "memory_gb")
    private Integer memoryGb;

    @Column(name = "storage_gb")
    private Integer storageGb;

    @Column(name = "instance_id")
    private String instanceId; // ID de la instancia en el proveedor

    @Column(name = "instance_status")
    @Enumerated(EnumType.STRING)
    private InstanceStatus instanceStatus;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "stopped_at")
    private LocalDateTime stoppedAt;

    @Column(name = "cost_per_hour")
    private BigDecimal costPerHour;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum InfrastructureType {
    CLOUD_INSTANCE,      // Instancia cloud tradicional
    SERVERLESS,           // Función serverless
    KUBERNETES_POD,      // Pod de Kubernetes
    DOCKER_CONTAINER,    // Contenedor Docker
    BARE_METAL,          // Servidor físico
    EDGE_DEVICE          // Dispositivo edge
}

public enum InstanceStatus {
    STARTING,            // Iniciando
    RUNNING,             // Ejecutándose
    STOPPING,            // Deteniéndose
    STOPPED,             // Detenida
    TERMINATED,          // Terminada
    ERROR                // Error
}
```

### 8\. TrainingPrompt

```java
@Entity
@Table(name = "trn_training_prompts")
public class TrainingPrompt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_job_id")
    private Long trainingJobId;

    @Column(name = "prompt_type")
    @Enumerated(EnumType.STRING)
    private PromptType promptType;

    @Column(name = "prompt_text")
    private String promptText;

    @Column(name = "system_prompt")
    private String systemPrompt;

    @Column(name = "few_shot_examples")
    private String fewShotExamples; // JSON con ejemplos few-shot

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "max_tokens")
    private Integer maxTokens;

    @Column(name = "top_p")
    private Double topP;

    @Column(name = "frequency_penalty")
    private Double frequencyPenalty;

    @Column(name = "presence_penalty")
    private Double presencePenalty;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum PromptType {
    SYSTEM_PROMPT,       // Prompt del sistema
    USER_PROMPT,         // Prompt del usuario
    FEW_SHOT,            // Ejemplos few-shot
    INSTRUCTION,         // Instrucciones de entrenamiento
    EVALUATION,          // Prompt para evaluación
    TESTING              // Prompt para testing
}
```

### 9\. TrainingQuantization

```java
@Entity
@Table(name = "trn_training_quantization")
public class TrainingQuantization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_job_id")
    private Long trainingJobId;

    @Column(name = "quantization_type")
    @Enumerated(EnumType.STRING)
    private QuantizationType quantizationType;

    @Column(name = "bits")
    private Integer bits; // 4, 8, 16, 32

    @Column(name = "group_size")
    private Integer groupSize;

    @Column(name = "act_order")
    private Boolean actOrder;

    @Column(name = "damp_percent")
    private Double dampPercent;

    @Column(name = "desc_act")
    private Boolean descAct;

    @Column(name = "static_groups")
    private Boolean staticGroups;

    @Column(name = "sym")
    private Boolean sym;

    @Column(name = "true_sequential")
    private Boolean trueSequential;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "model_file")
    private String modelFile;

    @Column(name = "quantized_file")
    private String quantizedFile;

    @Column(name = "compression_ratio")
    private Double compressionRatio;

    @Column(name = "accuracy_loss")
    private Double accuracyLoss;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum QuantizationType {
    GPTQ,                // GPTQ quantization
    AWQ,                 // AWQ quantization
    GGUF,                // GGUF quantization
    INT8,                // INT8 quantization
    INT4,                // INT4 quantization
    FP16,                // FP16 quantization
    BF16                 // BF16 quantization
}
```

### 10\. TrainingExecution

```java
@Entity
@Table(name = "trn_training_executions")
public class TrainingExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_job_id")
    private Long trainingJobId;

    @Column(name = "execution_number")
    private Integer executionNumber; // Número de ejecución (1, 2, 3...)

    @Column(name = "execution_type")
    @Enumerated(EnumType.STRING)
    private ExecutionType executionType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ExecutionStatus status;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "duration_hours")
    private Double durationHours;

    @Column(name = "cost_usd")
    private BigDecimal costUsd;

    @Column(name = "hyperparameters")
    private String hyperparameters; // JSON con hiperparámetros específicos

    @Column(name = "dataset_version")
    private String datasetVersion;

    @Column(name = "model_version")
    private String modelVersion;

    @Column(name = "checkpoint_path")
    private String checkpointPath;

    @Column(name = "final_model_path")
    private String finalModelPath;

    @Column(name = "metrics_summary")
    private String metricsSummary; // JSON con resumen de métricas

    @Column(name = "error_log")
    private String errorLog;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum ExecutionType {
    INITIAL,             // Ejecución inicial
    RETRAIN,             // Re-entrenamiento
    HYPERPARAMETER_TUNE, // Ajuste de hiperparámetros
    DATASET_UPDATE,      // Actualización de dataset
    MODEL_UPDATE,        // Actualización de modelo base
    QUANTIZATION,        // Cuantización
    EVALUATION           // Evaluación
}

public enum ExecutionStatus {
    PENDING,             // Pendiente
    RUNNING,             // Ejecutándose
    COMPLETED,           // Completada
    FAILED,              // Falló
    CANCELLED,           // Cancelada
    PAUSED               // Pausada
}
```

### 11\. DatasetSource

```java
@Entity
@Table(name = "trn_dataset_sources")
public class DatasetSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dataset_id")
    private Long datasetId;

    @Column(name = "source_type")
    @Enumerated(EnumType.STRING)
    private SourceType sourceType;

    @Column(name = "source_name")
    private String sourceName; // HuggingFace, Kaggle, API, Database, Web Scraping

    @Column(name = "source_url")
    private String sourceUrl;

    @Column(name = "source_config")
    private String sourceConfig; // JSON con configuración específica

    @Column(name = "api_key")
    private String apiKey; // Encriptado

    @Column(name = "cron_schedule")
    private String cronSchedule; // Para actualizaciones automáticas

    @Column(name = "last_sync_at")
    private LocalDateTime lastSyncAt;

    @Column(name = "sync_status")
    @Enumerated(EnumType.STRING)
    private SyncStatus syncStatus;

    @Column(name = "sync_error")
    private String syncError;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum SourceType {
    HUGGINGFACE,         // HuggingFace Hub
    KAGGLE,              // Kaggle Datasets
    API,                 // API externa
    DATABASE,            // Base de datos
    WEB_SCRAPING,        // Web scraping
    FILE_UPLOAD,         // Carga de archivo
    MANUAL_CREATION      // Creación manual
}

public enum SyncStatus {
    PENDING,             // Pendiente de sincronización
    SYNCING,             // Sincronizando
    COMPLETED,           // Sincronización completada
    FAILED,              // Sincronización falló
    SCHEDULED            // Programada
}
```

### 12\. TrainingGovernance

```java
@Entity
@Table(name = "trn_training_governance")
public class TrainingGovernance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_job_id")
    private Long trainingJobId;

    @Column(name = "governance_type")
    @Enumerated(EnumType.STRING)
    private GovernanceType governanceType;

    @Column(name = "compliance_status")
    @Enumerated(EnumType.STRING)
    private ComplianceStatus complianceStatus;

    @Column(name = "data_privacy_check")
    private Boolean dataPrivacyCheck;

    @Column(name = "bias_detection")
    private Boolean biasDetection;

    @Column(name = "bias_score")
    private Double biasScore;

    @Column(name = "fairness_metrics")
    private String fairnessMetrics; // JSON con métricas de justicia

    @Column(name = "explainability_check")
    private Boolean explainabilityCheck;

    @Column(name = "robustness_test")
    private Boolean robustnessTest;

    @Column(name = "adversarial_test")
    private Boolean adversarialTest;

    @Column(name = "security_scan")
    private Boolean securityScan;

    @Column(name = "licensing_check")
    private Boolean licensingCheck;

    @Column(name = "license_type")
    private String licenseType;

    @Column(name = "approval_required")
    private Boolean approvalRequired;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "governance_notes")
    private String governanceNotes;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum GovernanceType {
    DATA_PRIVACY,        // Privacidad de datos
    BIAS_FAIRNESS,       // Sesgo y justicia
    EXPLAINABILITY,      // Explicabilidad
    ROBUSTNESS,          // Robustez
    SECURITY,            // Seguridad
    LICENSING,           // Licencias
    COMPLIANCE           // Cumplimiento general
}

public enum ComplianceStatus {
    PENDING,             // Pendiente de revisión
    IN_REVIEW,           // En revisión
    APPROVED,            // Aprobado
    REJECTED,            // Rechazado
    CONDITIONAL,         // Aprobado con condiciones
    EXEMPT               // Exento
}
```

## API Endpoints

### Dashboard Overview

```plaintext
GET /api/v1/training/dashboard/overview
GET /api/v1/training/dashboard/overview/{projectId}
```

**Response Schema:**

```plaintext
{
  "success": true,
  "metrics": {
    "total_jobs": 150,
    "running_jobs": 12,
    "completed_jobs": 120,
    "failed_jobs": 8,
    "queued_jobs": 10,
    "success_rate": 93.75,
    "avg_training_time_hours": 4.5,
    "total_cost_usd": 1250.75,
    "cost_this_month_usd": 450.25,
    "resources_utilized": 85.5,
    "models_trained": 45,
    "datasets_processed": 28
  },
  "recent_activity": [
    {
      "id": "job_123",
      "type": "job",
      "status": "completed",
      "name": "BERT Fine-tuning",
      "created_at": "2024-01-15T10:30:00Z"
    }
  ],
  "cost_trends": [
    {
      "date": "2024-01-15",
      "cost_usd": 45.50,
      "jobs_count": 3
    }
  ],
  "last_updated": "2024-01-15T15:45:00Z"
}
```

### Job Management

```plaintext
GET    /api/v1/training/jobs
GET    /api/v1/training/jobs/{id}
POST   /api/v1/training/jobs
PUT    /api/v1/training/jobs/{id}
DELETE /api/v1/training/jobs/{id}
POST   /api/v1/training/jobs/{id}/start
POST   /api/v1/training/jobs/{id}/stop
POST   /api/v1/training/jobs/{id}/pause
POST   /api/v1/training/jobs/{id}/resume
GET    /api/v1/training/jobs/{id}/logs
GET    /api/v1/training/jobs/{id}/metrics
GET    /api/v1/training/jobs/{id}/artifacts
```

### Dataset Management

```plaintext
GET    /api/v1/training/datasets
GET    /api/v1/training/datasets/{id}
POST   /api/v1/training/datasets
PUT    /api/v1/training/datasets/{id}
DELETE /api/v1/training/datasets/{id}
POST   /api/v1/training/datasets/{id}/upload
POST   /api/v1/training/datasets/{id}/process
GET    /api/v1/training/datasets/{id}/preview
GET    /api/v1/training/datasets/{id}/schema
```

### Experiment Management

```plaintext
GET    /api/v1/training/experiments
GET    /api/v1/training/experiments/{id}
POST   /api/v1/training/experiments
PUT    /api/v1/training/experiments/{id}
DELETE /api/v1/training/experiments/{id}
POST   /api/v1/training/experiments/{id}/start
POST   /api/v1/training/experiments/{id}/complete
GET    /api/v1/training/experiments/{id}/results
```

### Resource Management

```plaintext
GET    /api/v1/training/resources
GET    /api/v1/training/resources/{id}
POST   /api/v1/training/resources
PUT    /api/v1/training/resources/{id}
DELETE /api/v1/training/resources/{id}
GET    /api/v1/training/resources/available
GET    /api/v1/training/resources/utilization
```

### Cost Management

```plaintext
GET    /api/v1/training/costs
GET    /api/v1/training/costs/job/{jobId}
GET    /api/v1/training/costs/project/{projectId}
GET    /api/v1/training/costs/user/{userId}
GET    /api/v1/training/costs/date-range
GET    /api/v1/training/costs/breakdown
```

### Infrastructure Management

```plaintext
GET    /api/v1/training/infrastructure
GET    /api/v1/training/infrastructure/{id}
POST   /api/v1/training/infrastructure
PUT    /api/v1/training/infrastructure/{id}
DELETE /api/v1/training/infrastructure/{id}
POST   /api/v1/training/infrastructure/{id}/start
POST   /api/v1/training/infrastructure/{id}/stop
POST   /api/v1/training/infrastructure/{id}/terminate
GET    /api/v1/training/infrastructure/providers
GET    /api/v1/training/infrastructure/costs
```

### Prompt Management

```plaintext
GET    /api/v1/training/prompts
GET    /api/v1/training/prompts/{id}
POST   /api/v1/training/prompts
PUT    /api/v1/training/prompts/{id}
DELETE /api/v1/training/prompts/{id}
GET    /api/v1/training/prompts/type/{promptType}
POST   /api/v1/training/prompts/{id}/test
POST   /api/v1/training/prompts/{id}/validate
```

### Quantization Management

```plaintext
GET    /api/v1/training/quantization
GET    /api/v1/training/quantization/{id}
POST   /api/v1/training/quantization
PUT    /api/v1/training/quantization/{id}
DELETE /api/v1/training/quantization/{id}
POST   /api/v1/training/quantization/{id}/execute
GET    /api/v1/training/quantization/types
GET    /api/v1/training/quantization/compression-analysis
```

### Execution Management

```plaintext
GET    /api/v1/training/executions
GET    /api/v1/training/executions/{id}
POST   /api/v1/training/executions
PUT    /api/v1/training/executions/{id}
DELETE /api/v1/training/executions/{id}
GET    /api/v1/training/executions/job/{jobId}
GET    /api/v1/training/executions/type/{executionType}
POST   /api/v1/training/executions/{id}/retry
POST   /api/v1/training/executions/{id}/cancel
GET    /api/v1/training/executions/history/{jobId}
```

### Dataset Source Management

```plaintext
GET    /api/v1/training/dataset-sources
GET    /api/v1/training/dataset-sources/{id}
POST   /api/v1/training/dataset-sources
PUT    /api/v1/training/dataset-sources/{id}
DELETE /api/v1/training/dataset-sources/{id}
POST   /api/v1/training/dataset-sources/{id}/sync
GET    /api/v1/training/dataset-sources/sync-status
POST   /api/v1/training/dataset-sources/{id}/schedule
GET    /api/v1/training/dataset-sources/providers
GET    /api/v1/training/dataset-sources/search
```

### Governance Management

```plaintext
GET    /api/v1/training/governance
GET    /api/v1/training/governance/{id}
POST   /api/v1/training/governance
PUT    /api/v1/training/governance/{id}
DELETE /api/v1/training/governance/{id}
POST   /api/v1/training/governance/{id}/approve
POST   /api/v1/training/governance/{id}/reject
GET    /api/v1/training/governance/compliance-status
GET    /api/v1/training/governance/bias-analysis
GET    /api/v1/training/governance/fairness-metrics
```

### Advanced Training Features

```plaintext
POST   /api/v1/training/auto-hyperparameter-tuning
POST   /api/v1/training/distributed-training
POST   /api/v1/training/federated-learning
POST   /api/v1/training/active-learning
POST   /api/v1/training/curriculum-learning
GET    /api/v1/training/optimization-suggestions
POST   /api/v1/training/early-stopping
POST   /api/v1/training/checkpoint-restore
```

### Integration Endpoints

```plaintext
POST   /api/v1/training/integrate/huggingface
POST   /api/v1/training/integrate/kaggle
POST   /api/v1/training/integrate/mlflow
POST   /api/v1/training/integrate/kubernetes
POST   /api/v1/training/integrate/docker
GET    /api/v1/training/integration/status
POST   /api/v1/training/integration/test
```

### Monitoring and Analytics

```plaintext
GET    /api/v1/training/analytics/overview
GET    /api/v1/training/analytics/costs
GET    /api/v1/training/analytics/performance
GET    /api/v1/training/analytics/resource-utilization
GET    /api/v1/training/analytics/success-rate
GET    /api/v1/training/analytics/training-time
GET    /api/v1/training/analytics/model-quality
GET    /api/v1/training/analytics/dataset-usage
```

## Scripts de Base de Datos

```plaintext
-- Tabla de jobs de entrenamiento
CREATE TABLE training_jobs (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    project_id BIGINT REFERENCES projects(id),
    user_id BIGINT REFERENCES users(id),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    job_type VARCHAR(50) NOT NULL,
    model_type VARCHAR(100),
    dataset_id BIGINT REFERENCES datasets(id),
    hyperparameters TEXT,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    estimated_duration_hours DECIMAL(10,2),
    actual_duration_hours DECIMAL(10,2),
    cost_usd DECIMAL(10,2),
    resource_config TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de datasets
CREATE TABLE datasets (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    project_id BIGINT REFERENCES projects(id),
    user_id BIGINT REFERENCES users(id),
    dataset_type VARCHAR(50) NOT NULL,
    file_path VARCHAR(500),
    file_size_bytes BIGINT,
    record_count BIGINT,
    format VARCHAR(50),
    schema TEXT,
    metadata TEXT,
    is_processed BOOLEAN DEFAULT false,
    processing_status VARCHAR(50) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de experimentos
CREATE TABLE experiments (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    project_id BIGINT REFERENCES projects(id),
    user_id BIGINT REFERENCES users(id),
    experiment_type VARCHAR(50) NOT NULL,
    objective TEXT,
    hypothesis TEXT,
    methodology TEXT,
    status VARCHAR(50) DEFAULT 'PLANNED',
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    results TEXT,
    conclusions TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de recursos de entrenamiento
CREATE TABLE training_resources (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    resource_type VARCHAR(50) NOT NULL,
    specifications TEXT,
    cost_per_hour_usd DECIMAL(10,2),
    availability VARCHAR(50) DEFAULT 'AVAILABLE',
    current_utilization DECIMAL(5,2),
    max_utilization DECIMAL(5,2),
    location VARCHAR(100),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de costes de entrenamiento
CREATE TABLE training_costs (
    id BIGSERIAL PRIMARY KEY,
    training_job_id BIGINT REFERENCES training_jobs(id),
    resource_id BIGINT REFERENCES training_resources(id),
    cost_type VARCHAR(50) NOT NULL,
    amount_usd DECIMAL(10,2) NOT NULL,
    duration_hours DECIMAL(10,2),
    rate_per_hour_usd DECIMAL(10,2),
    description TEXT,
    billing_period VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de métricas de entrenamiento
CREATE TABLE training_metrics (
    id BIGSERIAL PRIMARY KEY,
    training_job_id BIGINT REFERENCES training_jobs(id),
    metric_name VARCHAR(100) NOT NULL,
    metric_value DECIMAL(15,6),
    metric_unit VARCHAR(50),
    epoch INTEGER,
    step BIGINT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    metadata TEXT
);

-- Tabla de logs de entrenamiento
CREATE TABLE training_logs (
    id BIGSERIAL PRIMARY KEY,
    training_job_id BIGINT REFERENCES training_jobs(id),
    log_level VARCHAR(20) NOT NULL,
    message TEXT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    source VARCHAR(100),
    metadata TEXT
);

-- Tabla de artefactos de entrenamiento
CREATE TABLE training_artifacts (
    id BIGSERIAL PRIMARY KEY,
    training_job_id BIGINT REFERENCES training_jobs(id),
    artifact_type VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    file_path VARCHAR(500),
    file_size_bytes BIGINT,
    checksum VARCHAR(64),
    metadata TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para optimización
CREATE INDEX idx_training_jobs_project_id ON training_jobs(project_id);
CREATE INDEX idx_training_jobs_user_id ON training_jobs(user_id);
CREATE INDEX idx_training_jobs_status ON training_jobs(status);
CREATE INDEX idx_training_jobs_created_at ON training_jobs(created_at);
CREATE INDEX idx_datasets_project_id ON datasets(project_id);
CREATE INDEX idx_datasets_user_id ON datasets(user_id);
CREATE INDEX idx_datasets_type ON datasets(dataset_type);
CREATE INDEX idx_experiments_project_id ON experiments(project_id);
CREATE INDEX idx_experiments_status ON experiments(status);
CREATE INDEX idx_training_costs_job_id ON training_costs(training_job_id);
CREATE INDEX idx_training_metrics_job_id ON training_metrics(training_job_id);
CREATE INDEX idx_training_logs_job_id ON training_logs(training_job_id);
CREATE INDEX idx_training_logs_timestamp ON training_logs(timestamp);
CREATE INDEX idx_training_artifacts_job_id ON training_artifacts(training_job_id);
```

## Configuración de Servicios

### TrainingService

```java
@Service
@Transactional
public class TrainingService {

    @Autowired
    private TrainingJobRepository trainingJobRepository;

    @Autowired
    private DatasetRepository datasetRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private CostService costService;

    @Autowired
    private AuditService auditService;

    public TrainingJob createTrainingJob(TrainingJobDto jobDto) {
        // Validar recursos disponibles
        validateResources(jobDto.getResourceConfig());

        // Crear job
        TrainingJob job = new TrainingJob();
        job.setName(jobDto.getName());
        job.setDescription(jobDto.getDescription());
        job.setProjectId(jobDto.getProjectId());
        job.setUserId(getCurrentUserId());
        job.setStatus(JobStatus.PENDING);
        job.setJobType(jobDto.getJobType());
        job.setModelType(jobDto.getModelType());
        job.setDatasetId(jobDto.getDatasetId());
        job.setHyperparameters(jobDto.getHyperparameters());
        job.setResourceConfig(jobDto.getResourceConfig());
        job.setCreatedAt(LocalDateTime.now());

        TrainingJob savedJob = trainingJobRepository.save(job);

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "CREATE_TRAINING_JOB",
            "TRAINING_JOB",
            savedJob.getId().toString()
        );

        return savedJob;
    }

    public TrainingJob startTrainingJob(Long jobId) {
        TrainingJob job = trainingJobRepository.findById(jobId)
            .orElseThrow(() -&gt; new TrainingJobNotFoundException("Job not found"));

        // Validar que el job esté en estado válido
        if (job.getStatus() != JobStatus.PENDING &amp;&amp; job.getStatus() != JobStatus.PAUSED) {
            throw new InvalidJobStateException("Job cannot be started from current state");
        }

        // Asignar recursos
        assignResources(job);

        // Actualizar estado
        job.setStatus(JobStatus.RUNNING);
        job.setStartedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());

        TrainingJob updatedJob = trainingJobRepository.save(job);

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "START_TRAINING_JOB",
            "TRAINING_JOB",
            updatedJob.getId().toString()
        );

        return updatedJob;
    }

    private void validateResources(String resourceConfig) {
        // Implementar validación de recursos
    }

    private void assignResources(TrainingJob job) {
        // Implementar asignación de recursos
    }

    private Long getCurrentUserId() {
        // Obtener ID del usuario actual
        return 1L; // Placeholder
    }

    private String getCurrentUsername() {
        // Obtener username del usuario actual
        return "current_user"; // Placeholder
    }
}
```

## Monitoreo y Métricas

### Health Checks

```java
@Component
public class TrainingHealthIndicator implements HealthIndicator {

    @Autowired
    private TrainingJobRepository trainingJobRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    public Health health() {
        try {
            long runningJobs = trainingJobRepository.countByStatus(JobStatus.RUNNING);
            long failedJobs = trainingJobRepository.countByStatus(JobStatus.FAILED);
            long availableResources = resourceRepository.countByAvailability(ResourceAvailability.AVAILABLE);

            Health.Builder builder = Health.up()
                .withDetail("running_jobs", runningJobs)
                .withDetail("failed_jobs", failedJobs)
                .withDetail("available_resources", availableResources);

            if (failedJobs &gt; 0) {
                builder.withDetail("status", "Warning: Failed jobs detected");
            }

            return builder.build();
        } catch (Exception e) {
            return Health.down()
                .withDetail("error", e.getMessage())
                .build();
        }
    }
}
```

### Métricas Personalizadas

```java
@Component
public class TrainingMetrics {

    private final MeterRegistry meterRegistry;

    // Contadores para jobs de entrenamiento
    private final Counter jobsCreatedCounter;
    private final Counter jobsStartedCounter;
    private final Counter jobsCompletedCounter;
    private final Counter jobsFailedCounter;
    private final Counter jobsCancelledCounter;

    // Contadores para infraestructura
    private final Counter infrastructureStartedCounter;
    private final Counter infrastructureStoppedCounter;
    private final Counter infrastructureErrorsCounter;
    private final Counter cloudInstancesCounter;
    private final Counter serverlessExecutionsCounter;

    // Contadores para prompts
    private final Counter promptsCreatedCounter;
    private final Counter promptsValidatedCounter;
    private final Counter promptsOptimizedCounter;
    private final Counter fewShotExamplesCounter;

    // Contadores para cuantización
    private final Counter quantizationExecutedCounter;
    private final Counter quantizationSuccessCounter;
    private final Counter quantizationFailedCounter;
    private final Counter compressionRatioGauge;

    // Contadores para ejecuciones
    private final Counter executionsStartedCounter;
    private final Counter executionsCompletedCounter;
    private final Counter executionsFailedCounter;
    private final Counter retryAttemptsCounter;
    private final Counter checkpointSavesCounter;

    // Contadores para fuentes de datos
    private final Counter datasetSourcesSyncCounter;
    private final Counter datasetSourcesErrorCounter;
    private final Counter huggingfaceDownloadsCounter;
    private final Counter kaggleDownloadsCounter;
    private final Counter webScrapingJobsCounter;

    // Contadores para gobierno
    private final Counter governanceChecksCounter;
    private final Counter complianceApprovalsCounter;
    private final Counter biasDetectionsCounter;
    private final Counter fairnessViolationsCounter;
    private final Counter securityScansCounter;

    // Timers para operaciones
    private final Timer jobExecutionTimer;
    private final Timer infrastructureProvisioningTimer;
    private final Timer quantizationExecutionTimer;
    private final Timer datasetSyncTimer;
    private final Timer governanceCheckTimer;

    // Gauges para métricas en tiempo real
    private final Gauge activeJobsGauge;
    private final Gauge runningInfrastructureGauge;
    private final Gauge totalCostsGauge;
    private final Gauge resourceUtilizationGauge;
    private final Gauge datasetSizeGauge;

    public TrainingMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Inicializar contadores
        this.jobsCreatedCounter = Counter.builder("training.jobs.created")
            .description("Número de jobs de entrenamiento creados")
            .register(meterRegistry);

        this.jobsStartedCounter = Counter.builder("training.jobs.started")
            .description("Número de jobs de entrenamiento iniciados")
            .register(meterRegistry);

        this.jobsCompletedCounter = Counter.builder("training.jobs.completed")
            .description("Número de jobs de entrenamiento completados")
            .register(meterRegistry);

        this.jobsFailedCounter = Counter.builder("training.jobs.failed")
            .description("Número de jobs de entrenamiento fallidos")
            .register(meterRegistry);

        this.jobsCancelledCounter = Counter.builder("training.jobs.cancelled")
            .description("Número de jobs de entrenamiento cancelados")
            .register(meterRegistry);

        this.infrastructureStartedCounter = Counter.builder("training.infrastructure.started")
            .description("Número de instancias de infraestructura iniciadas")
            .register(meterRegistry);

        this.infrastructureStoppedCounter = Counter.builder("training.infrastructure.stopped")
            .description("Número de instancias de infraestructura detenidas")
            .register(meterRegistry);

        this.infrastructureErrorsCounter = Counter.builder("training.infrastructure.errors")
            .description("Número de errores de infraestructura")
            .register(meterRegistry);

        this.cloudInstancesCounter = Counter.builder("training.infrastructure.cloud_instances")
            .description("Número de instancias cloud utilizadas")
            .register(meterRegistry);

        this.serverlessExecutionsCounter = Counter.builder("training.infrastructure.serverless")
            .description("Número de ejecuciones serverless")
            .register(meterRegistry);

        this.promptsCreatedCounter = Counter.builder("training.prompts.created")
            .description("Número de prompts creados")
            .register(meterRegistry);

        this.promptsValidatedCounter = Counter.builder("training.prompts.validated")
            .description("Número de prompts validados")
            .register(meterRegistry);

        this.promptsOptimizedCounter = Counter.builder("training.prompts.optimized")
            .description("Número de prompts optimizados")
            .register(meterRegistry);

        this.fewShotExamplesCounter = Counter.builder("training.prompts.few_shot_examples")
            .description("Número de ejemplos few-shot utilizados")
            .register(meterRegistry);

        this.quantizationExecutedCounter = Counter.builder("training.quantization.executed")
            .description("Número de cuantizaciones ejecutadas")
            .register(meterRegistry);

        this.quantizationSuccessCounter = Counter.builder("training.quantization.success")
            .description("Número de cuantizaciones exitosas")
            .register(meterRegistry);

        this.quantizationFailedCounter = Counter.builder("training.quantization.failed")
            .description("Número de cuantizaciones fallidas")
            .register(meterRegistry);

        this.executionsStartedCounter = Counter.builder("training.executions.started")
            .description("Número de ejecuciones iniciadas")
            .register(meterRegistry);

        this.executionsCompletedCounter = Counter.builder("training.executions.completed")
            .description("Número de ejecuciones completadas")
            .register(meterRegistry);

        this.executionsFailedCounter = Counter.builder("training.executions.failed")
            .description("Número de ejecuciones fallidas")
            .register(meterRegistry);

        this.retryAttemptsCounter = Counter.builder("training.executions.retry_attempts")
            .description("Número de intentos de reintento")
            .register(meterRegistry);

        this.checkpointSavesCounter = Counter.builder("training.executions.checkpoint_saves")
            .description("Número de checkpoints guardados")
            .register(meterRegistry);

        this.datasetSourcesSyncCounter = Counter.builder("training.dataset_sources.sync")
            .description("Número de sincronizaciones de fuentes de datos")
            .register(meterRegistry);

        this.datasetSourcesErrorCounter = Counter.builder("training.dataset_sources.errors")
            .description("Número de errores en fuentes de datos")
            .register(meterRegistry);

        this.huggingfaceDownloadsCounter = Counter.builder("training.dataset_sources.huggingface_downloads")
            .description("Número de descargas de HuggingFace")
            .register(meterRegistry);

        this.kaggleDownloadsCounter = Counter.builder("training.dataset_sources.kaggle_downloads")
            .description("Número de descargas de Kaggle")
            .register(meterRegistry);

        this.webScrapingJobsCounter = Counter.builder("training.dataset_sources.web_scraping_jobs")
            .description("Número de trabajos de web scraping")
            .register(meterRegistry);

        this.governanceChecksCounter = Counter.builder("training.governance.checks")
            .description("Número de verificaciones de gobierno")
            .register(meterRegistry);

        this.complianceApprovalsCounter = Counter.builder("training.governance.compliance_approvals")
            .description("Número de aprobaciones de cumplimiento")
            .register(meterRegistry);

        this.biasDetectionsCounter = Counter.builder("training.governance.bias_detections")
            .description("Número de detecciones de sesgo")
            .register(meterRegistry);

        this.fairnessViolationsCounter = Counter.builder("training.governance.fairness_violations")
            .description("Número de violaciones de justicia")
            .register(meterRegistry);

        this.securityScansCounter = Counter.builder("training.governance.security_scans")
            .description("Número de escaneos de seguridad")
            .register(meterRegistry);

        // Inicializar timers
        this.jobExecutionTimer = Timer.builder("training.jobs.execution.time")
            .description("Tiempo de ejecución de jobs")
            .register(meterRegistry);

        this.infrastructureProvisioningTimer = Timer.builder("training.infrastructure.provisioning.time")
            .description("Tiempo de aprovisionamiento de infraestructura")
            .register(meterRegistry);

        this.quantizationExecutionTimer = Timer.builder("training.quantization.execution.time")
            .description("Tiempo de ejecución de cuantización")
            .register(meterRegistry);

        this.datasetSyncTimer = Timer.builder("training.dataset_sources.sync.time")
            .description("Tiempo de sincronización de fuentes de datos")
            .register(meterRegistry);

        this.governanceCheckTimer = Timer.builder("training.governance.check.time")
            .description("Tiempo de verificación de gobierno")
            .register(meterRegistry);

        // Inicializar gauges
        this.activeJobsGauge = Gauge.builder("training.jobs.active")
            .description("Número de jobs activos")
            .register(meterRegistry, this, TrainingMetrics::getActiveJobsCount);

        this.runningInfrastructureGauge = Gauge.builder("training.infrastructure.running")
            .description("Número de instancias de infraestructura ejecutándose")
            .register(meterRegistry, this, TrainingMetrics::getRunningInfrastructureCount);

        this.totalCostsGauge = Gauge.builder("training.costs.total")
            .description("Costes totales de entrenamiento")
            .register(meterRegistry, this, TrainingMetrics::getTotalCosts);

        this.resourceUtilizationGauge = Gauge.builder("training.resources.utilization")
            .description("Porcentaje de utilización de recursos")
            .register(meterRegistry, this, TrainingMetrics::getResourceUtilization);

        this.datasetSizeGauge = Gauge.builder("training.datasets.total_size")
            .description("Tamaño total de datasets")
            .register(meterRegistry, this, TrainingMetrics::getTotalDatasetSize);
    }

    // Métodos para incrementar contadores
    public void incrementJobsCreated() {
        jobsCreatedCounter.increment();
    }

    public void incrementJobsStarted() {
        jobsStartedCounter.increment();
    }

    public void incrementJobsCompleted() {
        jobsCompletedCounter.increment();
    }

    public void incrementJobsFailed() {
        jobsFailedCounter.increment();
    }

    public void incrementJobsCancelled() {
        jobsCancelledCounter.increment();
    }

    public void incrementInfrastructureStarted() {
        infrastructureStartedCounter.increment();
    }

    public void incrementInfrastructureStopped() {
        infrastructureStoppedCounter.increment();
    }

    public void incrementInfrastructureErrors() {
        infrastructureErrorsCounter.increment();
    }

    public void incrementCloudInstances() {
        cloudInstancesCounter.increment();
    }

    public void incrementServerlessExecutions() {
        serverlessExecutionsCounter.increment();
    }

    public void incrementPromptsCreated() {
        promptsCreatedCounter.increment();
    }

    public void incrementPromptsValidated() {
        promptsValidatedCounter.increment();
    }

    public void incrementPromptsOptimized() {
        promptsOptimizedCounter.increment();
    }

    public void incrementFewShotExamples() {
        fewShotExamplesCounter.increment();
    }

    public void incrementQuantizationExecuted() {
        quantizationExecutedCounter.increment();
    }

    public void incrementQuantizationSuccess() {
        quantizationSuccessCounter.increment();
    }

    public void incrementQuantizationFailed() {
        quantizationFailedCounter.increment();
    }

    public void incrementExecutionsStarted() {
        executionsStartedCounter.increment();
    }

    public void incrementExecutionsCompleted() {
        executionsCompletedCounter.increment();
    }

    public void incrementExecutionsFailed() {
        executionsFailedCounter.increment();
    }

    public void incrementRetryAttempts() {
        retryAttemptsCounter.increment();
    }

    public void incrementCheckpointSaves() {
        checkpointSavesCounter.increment();
    }

    public void incrementDatasetSourcesSync() {
        datasetSourcesSyncCounter.increment();
    }

    public void incrementDatasetSourcesError() {
        datasetSourcesErrorCounter.increment();
    }

    public void incrementHuggingfaceDownloads() {
        huggingfaceDownloadsCounter.increment();
    }

    public void incrementKaggleDownloads() {
        kaggleDownloadsCounter.increment();
    }

    public void incrementWebScrapingJobs() {
        webScrapingJobsCounter.increment();
    }

    public void incrementGovernanceChecks() {
        governanceChecksCounter.increment();
    }

    public void incrementComplianceApprovals() {
        complianceApprovalsCounter.increment();
    }

    public void incrementBiasDetections() {
        biasDetectionsCounter.increment();
    }

    public void incrementFairnessViolations() {
        fairnessViolationsCounter.increment();
    }

    public void incrementSecurityScans() {
        securityScansCounter.increment();
    }

    // Métodos para iniciar timers
    public Timer.Sample startJobExecutionTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startInfrastructureProvisioningTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startQuantizationExecutionTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startDatasetSyncTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startGovernanceCheckTimer() {
        return Timer.start(meterRegistry);
    }

    // Métodos para obtener valores de gauges (implementar con repositorios)
    private double getActiveJobsCount() {
        // Implementar con TrainingJobRepository.countByStatusIn
        return 0.0;
    }

    private double getRunningInfrastructureCount() {
        // Implementar con TrainingInfrastructureRepository.countByInstanceStatus
        return 0.0;
    }

    private double getTotalCosts() {
        // Implementar con TrainingJobRepository.sumCostUsd
        return 0.0;
    }

    private double getResourceUtilization() {
        // Implementar cálculo de utilización de recursos
        return 75.5;
    }

    private double getTotalDatasetSize() {
        // Implementar con DatasetRepository.sumFileSize
        return 0.0;
    }
}
```

## Configuración de Aplicación

### application-training.yml

```yaml
training:
  # Configuración de recursos
  resources:
    default-gpu: "nvidia-tesla-v100"
    default-cpu: "8"
    default-memory: "32GB"
    default-storage: "100GB"
    max-gpu-count: 8
    max-cpu-cores: 64
    max-memory-gb: 512
    max-storage-gb: 2000

  # Configuración de infraestructura
  infrastructure:
    providers:
      - name: "AWS"
        enabled: true
        regions: ["us-east-1", "us-west-2", "eu-west-1"]
        instance-types: ["g4dn.xlarge", "g4dn.2xlarge", "g4dn.4xlarge"]
        gpu-types: ["nvidia-tesla-t4", "nvidia-tesla-v100"]

      - name: "GCP"
        enabled: true
        regions: ["us-central1", "us-west1", "europe-west1"]
        instance-types: ["n1-standard-4", "n1-standard-8", "n1-standard-16"]
        gpu-types: ["nvidia-tesla-t4", "nvidia-tesla-v100", "nvidia-a100"]

      - name: "Azure"
        enabled: true
        regions: ["eastus", "westus2", "westeurope"]
        instance-types: ["Standard_NC6s_v3", "Standard_NC12s_v3"]
        gpu-types: ["nvidia-tesla-v100"]

      - name: "OVH"
        enabled: true
        regions: ["GRA", "SBG", "BHS"]
        instance-types: ["g2-15", "g2-30", "g2-60"]
        gpu-types: ["nvidia-tesla-v100"]

      - name: "RunPod"
        enabled: true
        regions: ["US-East", "US-West", "EU-West"]
        instance-types: ["RTX 3090", "RTX 4090", "A100"]
        gpu-types: ["nvidia-rtx-3090", "nvidia-rtx-4090", "nvidia-a100"]

      - name: "Vast.ai"
        enabled: true
        regions: ["US", "EU", "Asia"]
        instance-types: ["RTX 3090", "RTX 4090", "A100"]
        gpu-types: ["nvidia-rtx-3090", "nvidia-rtx-4090", "nvidia-a100"]

      - name: "Self-hosted"
        enabled: true
        regions: ["local", "on-premise"]
        instance-types: ["custom"]
        gpu-types: ["nvidia-tesla-v100", "nvidia-a100", "nvidia-h100"]

  # Configuración de costes
  costs:
    gpu-rates:
      nvidia-tesla-v100: 2.48
      nvidia-tesla-t4: 0.35
      nvidia-a100: 3.26
      nvidia-h100: 8.50
      nvidia-rtx-3090: 0.60
      nvidia-rtx-4090: 0.80
    cpu-rate-per-core: 0.05
    memory-rate-per-gb: 0.01
    storage-rate-per-gb: 0.0001
    network-rate-per-gb: 0.09
    markup-percentage: 15.0

  # Configuración de jobs
  jobs:
    max-concurrent: 10
    max-duration-hours: 168 # 1 week
    auto-cleanup-days: 30
    checkpoint-interval-minutes: 30
    early-stopping-patience: 5
    max-retries: 3

  # Configuración de datasets
  datasets:
    max-file-size-mb: 10240 # 10GB
    supported-formats: ["csv", "json", "parquet", "h5", "pkl", "txt", "md"]
    auto-processing: true
    versioning-enabled: true
    max-versions-per-dataset: 10
    auto-backup: true

  # Configuración de fuentes de datos
  dataset-sources:
    huggingface:
      enabled: true
      api-base-url: "https://huggingface.co/api"
      max-download-size-gb: 100
      auto-sync: false
      sync-interval-hours: 24

    kaggle:
      enabled: true
      api-base-url: "https://www.kaggle.com/api/v1"
      max-download-size-gb: 50
      auto-sync: false
      sync-interval-hours: 48

    web-scraping:
      enabled: true
      max-pages-per-scrape: 1000
      rate-limit-requests-per-minute: 60
      user-agent: "CodeFlowX-Training-Bot/1.0"

    database:
      enabled: true
      max-query-timeout-seconds: 300
      max-result-rows: 1000000

  # Configuración de prompts
  prompts:
    max-prompt-length: 10000
    max-few-shot-examples: 50
    default-temperature: 0.7
    default-max-tokens: 2048
    default-top-p: 0.9
    validation-enabled: true
    auto-optimization: false

  # Configuración de cuantización
  quantization:
    enabled: true
    supported-types: ["GPTQ", "AWQ", "GGUF", "INT8", "INT4", "FP16", "BF16"]
    max-compression-ratio: 0.25
    min-accuracy-threshold: 0.95
    auto-optimization: true
    hardware-acceleration: true

  # Configuración de ejecuciones
  executions:
    max-executions-per-job: 100
    auto-versioning: true
    checkpoint-retention-days: 90
    model-retention-days: 365
    metrics-retention-days: 2555

  # Configuración de experimentos
  experiments:
    max-duration-days: 30
    auto-archival: true
    retention-days: 365
    mlflow-integration: true
    tensorboard-integration: true
    wandb-integration: false

  # Configuración de gobierno
  governance:
    enabled: true
    auto-compliance-check: true
    bias-detection-threshold: 0.1
    fairness-metrics-required: true
    explainability-required: true
    security-scan-enabled: true
    licensing-check-enabled: true
    approval-workflow: true

  # Configuración de integración
  integration:
    mlflow:
      enabled: true
      tracking-uri: "http://localhost:5000"
      registry-uri: "http://localhost:5000"
      experiment-tracking: true
      model-registry: true

    kubernetes:
      enabled: true
      namespace: "training"
      service-account: "training-sa"
      resource-quotas: true
      auto-scaling: true

    docker:
      enabled: true
      registry: "localhost:5000"
      auto-build: true
      multi-stage-builds: true

    huggingface:
      enabled: true
      token-encryption: true
      model-caching: true
      auto-upload: false

    kaggle:
      enabled: true
      api-key-encryption: true
      dataset-caching: true

  # Configuración de monitorización
  monitoring:
    prometheus-enabled: true
    grafana-enabled: true
    alerting-enabled: true
    log-aggregation: true
    performance-profiling: true
    resource-tracking: true
    cost-alerts:
      threshold-usd: 100.0
      notification-email: true
      notification-slack: false

  # Configuración de notificaciones
  notifications:
    job-completion: true
    job-failure: true
    cost-threshold: true
    resource-utilization: true
    governance-approval: true
    dataset-sync: true
    channels:
      - email
      - slack
      - webhook

  # Configuración de seguridad
  security:
    api-key-encryption: true
    data-encryption-at-rest: true
    data-encryption-in-transit: true
    network-isolation: true
    access-control: true
    audit-logging: true
    compliance-reporting: true
```

## Pendiente

los entrenamientos se realizan asignando un servidor cloud, serveless.. de la infraestructura y los costes de uso se imputan al entrenamiento, los datasets se peuden crear o descaragr de un proveedor externo como HF mediante leka-server, hay que incluir un buscado interno y externo . el trainig no neesariamente implica dominio o tecnologia, se puede suar para crear modelos propios o hacer finetunning sobre otros , tambien hay que tener una gestion de prompts, cuantificacion… el entramineto del modelos esta relacionado con gobierno, evaluacion de modelos y registro del modelo o adapter en el model management., el entremaineto tambien puede estar asigndo a un proyecto y un mismo entrenamineto puede ser ejecutado n veces hay que mantenr un historico de ejecuciones y versiones por cuestion de gestion de datos y dataset, el web scraping, llamaas a api o base de datos para crear los dataset tambien deben de poderse programar mediante un cron.

## Conclusión

El módulo de Training proporciona una gestión completa del ciclo de vida de entrenamiento de modelos, incluyendo:

- **Gestión de Jobs**: Creación, ejecución, monitorización y control de jobs de entrenamiento
- **Gestión de Datasets**: Carga, procesamiento y versionado de datasets
- **Gestión de Experimentos**: Diseño, ejecución y análisis de experimentos
- **Gestión de Recursos**: Asignación y monitorización de recursos computacionales
- **Control de Costes**: Seguimiento detallado de costes por job y recurso
- **Métricas y Logs**: Captura completa de métricas y logs de entrenamiento
- **Artefactos**: Gestión de modelos, checkpoints y otros artefactos generados

El sistema está diseñado para ser escalable, auditable y fácilmente integrable con otros módulos del portal.
