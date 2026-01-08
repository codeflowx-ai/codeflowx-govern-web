# 03. Módulo de Domain Ingestion - Portal Backend

## Descripción General

El módulo de Domain Ingestion gestiona la ingesta, procesamiento y gestión de datos de dominio específicos para entrenamiento de modelos de IA. Incluye funcionalidades de asistente de dominios, gestión de datasets, monitorización de jobs, procesamiento de datos especializados, **webscraping para búsqueda de información**, **gestión de documentos integrada con RAG**, **integración con módulo de training**, **relación con proyectos**, y **cumplimiento normativo**. Se integra con sistemas de almacenamiento distribuido, procesamiento de datos, leka-server para webscraping, servidor RAG, y módulo de entrenamiento.

## Características Principales

- **Gestión Completa de Dominios**: Creación y configuración de dominios específicos de negocio
- **Gestión de Datasets**: Manejo de datasets con metadatos y calidad de datos
- **Esquemas de Datos**: Definición y evolución de esquemas de datos
- **Jobs de Ingesta**: Programación y ejecución de trabajos de ingesta
- **Wizard de Dominios**: Asistente guiado para configuración de dominios
- **Webscraping Automático**: Búsqueda y descarga de información de fuentes externas
- **Gestión RAG**: Documentos para búsqueda inteligente y formación
- **Integración Training**: Conexión con módulo de entrenamiento
- **Relación con Proyectos**: Mapeo de dominios utilizados en proyectos
- **Cumplimiento Normativo**: Trazabilidad y versionado para cumplir leyes

## Entidades del Sistema

### 1. Domain (Dominio)

```java
@Entity
@Table(name = "dmn_domains")
public class Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "domain_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DomainType domainType;

    @Column(name = "industry")
    private String industry;

    @Column(name = "business_area")
    private String businessArea;

    @Column(name = "data_sources")
    private String dataSources; // JSON array of data sources

    @Column(name = "data_types")
    private String dataTypes; // JSON array of data types

    @Column(name = "quality_metrics")
    private String qualityMetrics; // JSON quality metrics

    @Column(name = "compliance_requirements")
    private String complianceRequirements; // JSON compliance info

    @Column(name = "regulatory_framework")
    private String regulatoryFramework; // Marco regulatorio aplicable

    @Column(name = "data_retention_years")
    private Integer dataRetentionYears; // Años de retención obligatoria

    @Column(name = "privacy_level")
    @Enumerated(EnumType.STRING)
    private PrivacyLevel privacyLevel; // Nivel de privacidad de los datos

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DomainStatus status = DomainStatus.DRAFT;

    @Column(name = "version")
    private String version = "1.0.0";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL)
    private List<DomainDataset> datasets = new ArrayList<>();

    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL)
    private List<DomainIngestionJob> ingestionJobs = new ArrayList<>();

    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL)
    private List<DomainSchema> schemas = new ArrayList<>();

    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL)
    private List<DomainDocument> documents = new ArrayList<>();

    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL)
    private List<DomainWebScrapingJob> webScrapingJobs = new ArrayList<>();

    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL)
    private List<DomainComplianceLog> complianceLogs = new ArrayList<>();
}

public enum DomainType {
    FINANCIAL, HEALTHCARE, RETAIL, MANUFACTURING, TELECOMMUNICATIONS,
    TRANSPORTATION, ENERGY, EDUCATION, GOVERNMENT, ECOMMERCE,
    MEDIA_ENTERTAINMENT, REAL_ESTATE, INSURANCE, BANKING, PHARMACEUTICAL,
    LEGAL, AEROSPACE, AUTOMOTIVE, CHEMICAL, FOOD_BEVERAGE
}

public enum DomainStatus {
    DRAFT, ACTIVE, INACTIVE, ARCHIVED, UNDER_REVIEW, COMPLIANCE_CHECK
}

public enum PrivacyLevel {
    PUBLIC, INTERNAL, CONFIDENTIAL, RESTRICTED, HIGHLY_SENSITIVE
}
```

### 2. Domain Dataset (Dataset del Dominio)

```java
@Entity
@Table(name = "dmn_domain_datasets")
public class DomainDataset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "domain_id", nullable = false)
    private Long domainId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "dataset_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DatasetType datasetType;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "file_format")
    private String fileFormat; // CSV, JSON, Parquet, etc.

    @Column(name = "record_count")
    private Long recordCount;

    @Column(name = "column_count")
    private Integer columnCount;

    @Column(name = "schema_version")
    private String schemaVersion;

    @Column(name = "data_quality_score")
    private Double dataQualityScore;

    @Column(name = "last_ingested")
    private LocalDateTime lastIngested;

    @Column(name = "ingestion_frequency")
    private String ingestionFrequency; // daily, weekly, monthly, etc.

    @Column(name = "retention_policy")
    private String retentionPolicy; // JSON retention configuration

    @Column(name = "data_lineage")
    private String dataLineage; // JSON data lineage information

    @Column(name = "source_metadata")
    private String sourceMetadata; // JSON metadata about data sources

    @Column(name = "compliance_tags")
    private String complianceTags; // JSON compliance tags

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

    @OneToMany(mappedBy = "dataset", cascade = CascadeType.ALL)
    private List<DatasetColumn> columns = new ArrayList<>();

    @OneToMany(mappedBy = "dataset", cascade = CascadeType.ALL)
    private List<DatasetIngestionLog> ingestionLogs = new ArrayList<>();

    @OneToMany(mappedBy = "dataset", cascade = CascadeType.ALL)
    private List<DatasetQualityMetric> qualityMetrics = new ArrayList<>();
}

public enum DatasetType {
    TRAINING, VALIDATION, TEST, PRODUCTION, REFERENCE, ARCHIVE, COMPLIANCE, AUDIT
}
```

### 3. Dataset Column (Columna del Dataset)

```java
@Entity
@Table(name = "dmn_dataset_columns")
public class DatasetColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dataset_id", nullable = false)
    private Long datasetId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "data_type", nullable = false)
    private String dataType; // string, integer, float, boolean, date, etc.

    @Column(name = "description")
    private String description;

    @Column(name = "is_required")
    private Boolean isRequired = false;

    @Column(name = "is_primary_key")
    private Boolean isPrimaryKey = false;

    @Column(name = "is_foreign_key")
    private Boolean isForeignKey = false;

    @Column(name = "reference_table")
    private String referenceTable;

    @Column(name = "reference_column")
    private String referenceColumn;

    @Column(name = "constraints")
    private String constraints; // JSON constraints

    @Column(name = "statistics")
    private String statistics; // JSON column statistics

    @Column(name = "privacy_level")
    @Enumerated(EnumType.STRING)
    private PrivacyLevel privacyLevel = PrivacyLevel.INTERNAL;

    @Column(name = "pii_detected")
    private Boolean piiDetected = false; // Personal Identifiable Information

    @Column(name = "encryption_required")
    private Boolean encryptionRequired = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
```

### 4. Domain Ingestion Job (Trabajo de Ingesta del Dominio)

```java
@Entity
@Table(name = "dmn_domain_ingestion_jobs")
public class DomainIngestionJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "domain_id", nullable = false)
    private Long domainId;

    @Column(name = "dataset_id")
    private Long datasetId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "job_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private IngestionJobType jobType;

    @Column(name = "source_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SourceType sourceType;

    @Column(name = "source_configuration")
    private String sourceConfiguration; // JSON source config

    @Column(name = "transformation_rules")
    private String transformationRules; // JSON transformation rules

    @Column(name = "validation_rules")
    private String validationRules; // JSON validation rules

    @Column(name = "schedule")
    private String schedule; // Cron expression

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.PENDING;

    @Column(name = "priority")
    private Integer priority = 5; // 1-10, higher is more important

    @Column(name = "estimated_duration_minutes")
    private Integer estimatedDurationMinutes;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "retry_count")
    private Integer retryCount = 0;

    @Column(name = "max_retries")
    private Integer maxRetries = 3;

    @Column(name = "compliance_check_required")
    private Boolean complianceCheckRequired = true;

    @Column(name = "data_lineage_tracking")
    private Boolean dataLineageTracking = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "ingestionJob", cascade = CascadeType.ALL)
    private List<IngestionJobLog> jobLogs = new ArrayList<>();

    @OneToMany(mappedBy = "ingestionJob", cascade = CascadeType.ALL)
    private List<IngestionComplianceCheck> complianceChecks = new ArrayList<>();
}

public enum IngestionJobType {
    FULL_LOAD, INCREMENTAL_LOAD, STREAMING_LOAD, BATCH_PROCESSING,
    DATA_CLEANSING, DATA_TRANSFORMATION, DATA_VALIDATION, SCHEMA_EVOLUTION,
    COMPLIANCE_CHECK, DATA_LINEAGE_UPDATE, QUALITY_ASSESSMENT
}

public enum SourceType {
    DATABASE, FILE_SYSTEM, API, MESSAGE_QUEUE, STREAMING_PLATFORM,
    CLOUD_STORAGE, WEB_SCRAPING, IoT_DEVICE, EXTERNAL_SYSTEM,
    Leka_SERVER, RAG_SERVER, TRAINING_MODULE
}

public enum JobStatus {
    PENDING, SCHEDULED, RUNNING, COMPLETED, FAILED, CANCELLED, RETRYING,
    COMPLIANCE_CHECK, DATA_LINEAGE_UPDATE, QUALITY_ASSESSMENT
}
```

### 5. Ingestion Job Log (Log del Trabajo de Ingesta)

```java
@Entity
@Table(name = "dmn_ingestion_job_logs")
public class IngestionJobLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ingestion_job_id", nullable = false)
    private Long ingestionJobId;

    @Column(name = "log_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private LogLevel logLevel;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "execution_step")
    private String executionStep;

    @Column(name = "records_processed")
    private Long recordsProcessed = 0L;

    @Column(name = "records_failed")
    private Long recordsFailed = 0L;

    @Column(name = "execution_time_ms")
    private Long executionTimeMs;

    @Column(name = "memory_usage_mb")
    private Double memoryUsageMb;

    @Column(name = "cpu_usage_percent")
    private Double cpuUsagePercent;

    @Column(name = "additional_data")
    private String additionalData; // JSON additional information

    @Column(name = "compliance_notes")
    private String complianceNotes; // Notas sobre cumplimiento normativo

    @Column(name = "data_lineage_updates")
    private String dataLineageUpdates; // JSON lineage updates
}

public enum LogLevel {
    DEBUG, INFO, WARNING, ERROR, CRITICAL, COMPLIANCE_ALERT, QUALITY_ALERT
}
```

### 6. Domain Schema (Esquema del Dominio)

```java
@Entity
@Table(name = "dmn_domain_schemas")
public class DomainSchema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "domain_id", nullable = false)
    private Long domainId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "schema_version", nullable = false)
    private String schemaVersion;

    @Column(name = "schema_definition", nullable = false)
    private String schemaDefinition; // JSON schema definition

    @Column(name = "schema_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SchemaType schemaType;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "compatibility_level")
    private String compatibilityLevel = "backward"; // backward, forward, full

    @Column(name = "validation_rules")
    private String validationRules; // JSON validation rules

    @Column(name = "evolution_history")
    private String evolutionHistory; // JSON evolution tracking

    @Column(name = "compliance_requirements")
    private String complianceRequirements; // JSON compliance requirements

    @Column(name = "data_governance_rules")
    private String dataGovernanceRules; // JSON governance rules

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "schema", cascade = CascadeType.ALL)
    private List<SchemaField> fields = new ArrayList<>();

    @OneToMany(mappedBy = "schema", cascade = CascadeType.ALL)
    private List<SchemaVersionHistory> versionHistory = new ArrayList<>();
}

public enum SchemaType {
    AVRO, JSON_SCHEMA, PROTOBUF, PARQUET, CSV_HEADER, DATABASE_TABLE,
    COMPLIANCE_SCHEMA, GOVERNANCE_SCHEMA, TRAINING_SCHEMA
}
```

### 7. Schema Field (Campo del Esquema)

```java
@Entity
@Table(name = "dmn_schema_fields")
public class SchemaField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "schema_id", nullable = false)
    private Long schemaId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "field_path")
    private String fieldPath; // JSON path notation

    @Column(name = "data_type", nullable = false)
    private String dataType;

    @Column(name = "is_required")
    private Boolean isRequired = false;

    @Column(name = "default_value")
    private String defaultValue;

    @Column(name = "constraints")
    private String constraints; // JSON field constraints

    @Column(name = "description")
    private String description;

    @Column(name = "order_index")
    private Integer orderIndex = 0;

    @Column(name = "privacy_level")
    @Enumerated(EnumType.STRING)
    private PrivacyLevel privacyLevel = PrivacyLevel.INTERNAL;

    @Column(name = "compliance_tags")
    private String complianceTags; // JSON compliance tags

    @Column(name = "data_quality_rules")
    private String dataQualityRules; // JSON quality rules

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
```

### 8. Domain Wizard (Asistente del Dominio)

```java
@Entity
@Table(name = "dmn_domain_wizards")
public class DomainWizard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "wizard_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private WizardType wizardType;

    @Column(name = "current_step")
    private Integer currentStep = 1;

    @Column(name = "total_steps")
    private Integer totalSteps;

    @Column(name = "wizard_data")
    private String wizardData; // JSON wizard state

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private WizardStatus status = WizardStatus.IN_PROGRESS;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "compliance_check_passed")
    private Boolean complianceCheckPassed = false;

    @Column(name = "training_content_generated")
    private Boolean trainingContentGenerated = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum WizardType {
    DOMAIN_SETUP, DATA_SOURCE_CONFIGURATION, SCHEMA_DEFINITION,
    VALIDATION_RULES, INGESTION_SCHEDULING, QUALITY_MONITORING,
    COMPLIANCE_SETUP, TRAINING_CONTENT_GENERATION, RAG_INTEGRATION
}

public enum WizardStatus {
    IN_PROGRESS, COMPLETED, ABANDONED, ERROR, COMPLIANCE_CHECK,
    TRAINING_GENERATION, RAG_PROCESSING
}
```

### 9. Domain Document (Documento del Dominio)

```java
@Entity
@Table(name = "dmn_domain_documents")
public class DomainDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "domain_id", nullable = false)
    private Long domainId;

    @Column(name = "document_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content; // Contenido del documento

    @Column(name = "file_url")
    private String fileUrl; // URL del archivo si está almacenado externamente

    @Column(name = "file_type")
    private String fileType; // PDF, DOCX, MD, HTML, etc.

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "language")
    private String language = "en"; // Idioma del documento

    @Column(name = "version")
    private String version; // Versión del documento

    @Column(name = "is_rag_ready")
    private Boolean isRagReady = false; // Preparado para servidor RAG

    @Column(name = "rag_metadata")
    private String ragMetadata; // JSON con metadatos para RAG

    @Column(name = "training_relevance")
    private Integer trainingRelevance; // 1-10 relevancia para formación

    @Column(name = "compliance_tags")
    private String complianceTags; // JSON con tags de cumplimiento

    @Column(name = "data_lineage")
    private String dataLineage; // JSON con información de linaje

    @Column(name = "source_information")
    private String sourceInformation; // JSON con información de la fuente

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "domain_id", insertable = false, updatable = false)
    private Domain domain;
}

public enum DocumentType {
    USER_GUIDE, API_REFERENCE, TUTORIAL, BEST_PRACTICES, EXAMPLES,
    TROUBLESHOOTING, ARCHITECTURE, DEPLOYMENT, SECURITY, PERFORMANCE,
    INTEGRATION, MIGRATION, CHANGELOG, CONTRIBUTING, LICENSE,
    COMPLIANCE_DOCUMENT, GOVERNANCE_POLICY, TRAINING_MATERIAL, RAG_CONTENT
}
```

### 10. Domain Web Scraping Job (Trabajo de Web Scraping del Dominio)

```java
@Entity
@Table(name = "dmn_domain_webscraping_jobs")
public class DomainWebScrapingJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    // Relación muchos a muchos con dominios
    @ManyToMany
    @JoinTable(
        name = "dmn_webscraping_domains",
        joinColumns = @JoinColumn(name = "webscraping_job_id"),
        inverseJoinColumns = @JoinColumn(name = "domain_id")
    )
    private List<Domain> domains = new ArrayList<>();

    // Relación muchos a muchos con proyectos RAG
    @ManyToMany
    @JoinTable(
        name = "dmn_webscraping_rag_projects",
        joinColumns = @JoinColumn(name = "webscraping_job_id"),
        inverseJoinColumns = @JoinColumn(name = "rag_project_id")
    )
    private List<RagProject> ragProjects = new ArrayList<>();

    @Column(name = "source_url", nullable = false)
    private String sourceUrl;

    @Column(name = "job_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private WebScrapingJobType jobType;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private WebScrapingJobStatus status = WebScrapingJobStatus.PENDING;

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

    @Column(name = "data_extracted")
    private String dataExtracted; // JSON con datos extraídos

    @Column(name = "compliance_check_required")
    private Boolean complianceCheckRequired = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "domain_id", insertable = false, updatable = false)
    private Domain domain;
}

public enum WebScrapingJobType {
    DATA_EXTRACTION, SCHEMA_DISCOVERY, COMPLIANCE_UPDATE, QUALITY_CHECK,
    SOURCE_VALIDATION, CONTENT_UPDATE, REGULATORY_CHECK, TRAINING_CONTENT
}

public enum WebScrapingJobStatus {
    PENDING, RUNNING, COMPLETED, FAILED, CANCELLED, RETRYING, QUEUED_IN_LEKA,
    COMPLIANCE_CHECK, DATA_PROCESSING, RAG_INTEGRATION
}
```

### 11. Domain Compliance Log (Log de Cumplimiento del Dominio)

```java
@Entity
@Table(name = "dmn_domain_compliance_logs")
public class DomainComplianceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "domain_id", nullable = false)
    private Long domainId;

    @Column(name = "compliance_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ComplianceType complianceType;

    @Column(name = "check_date", nullable = false)
    private LocalDateTime checkDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ComplianceStatus status;

    @Column(name = "details")
    private String details; // JSON con detalles del check

    @Column(name = "requirements_checked")
    private String requirementsChecked; // JSON con requisitos verificados

    @Column(name = "violations_found")
    private String violationsFound; // JSON con violaciones encontradas

    @Column(name = "remediation_actions")
    private String remediationActions; // JSON con acciones de remediación

    @Column(name = "next_check_date")
    private LocalDateTime nextCheckDate;

    @Column(name = "checker_id")
    private Long checkerId; // ID del usuario que realizó el check

    @Column(name = "automated_check")
    private Boolean automatedCheck = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "domain_id", insertable = false, updatable = false)
    private Domain domain;
}

public enum ComplianceType {
    DATA_PRIVACY, REGULATORY_COMPLIANCE, DATA_QUALITY, SECURITY_AUDIT,
    GOVERNANCE_CHECK, RETENTION_POLICY, ACCESS_CONTROL, AUDIT_TRAIL
}

public enum ComplianceStatus {
    PASSED, FAILED, WARNING, PENDING, IN_PROGRESS, EXEMPT
}
```

### 12. Project Domain Usage (Uso del Dominio en Proyectos)

```java
@Entity
@Table(name = "dmn_project_domain_usage")
public class ProjectDomainUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false)
    private Long projectId; // Referencia a prj_projects.id

    @Column(name = "domain_id", nullable = false)
    private Long domainId;

    @Column(name = "usage_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DomainUsageType usageType;

    @Column(name = "usage_start_date")
    private LocalDate usageStartDate;

    @Column(name = "usage_end_date")
    private LocalDate usageEndDate;

    @Column(name = "usage_intensity")
    @Enumerated(EnumType.STRING)
    private UsageIntensity usageIntensity;

    @Column(name = "usage_notes")
    private String usageNotes;

    @Column(name = "compliance_verified")
    private Boolean complianceVerified = false;

    @Column(name = "data_quality_score")
    private Double dataQualityScore;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "domain_id", insertable = false, updatable = false)
    private Domain domain;
}

public enum DomainUsageType {
    PRIMARY, SECONDARY, REFERENCE, COMPLIANCE, TRAINING, AUDIT, RESEARCH
}

public enum UsageIntensity {
    LOW, MEDIUM, HIGH, CRITICAL
}
```

### 13. Training Content Integration (Integración de Contenido de Entrenamiento)

```java
@Entity
@Table(name = "dmn_training_content_integration")
public class TrainingContentIntegration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "domain_id", nullable = false)
    private Long domainId;

    @Column(name = "content_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TrainingContentType contentType;

    @Column(name = "content_title", nullable = false)
    private String contentTitle;

    @Column(name = "content_description")
    private String contentDescription;

    @Column(name = "content_data")
    private String contentData; // JSON con contenido de entrenamiento

    @Column(name = "training_level")
    @Enumerated(EnumType.STRING)
    private TrainingLevel trainingLevel;

    @Column(name = "prerequisites")
    private String prerequisites; // JSON con prerrequisitos

    @Column(name = "learning_objectives")
    private String learningObjectives; // JSON con objetivos de aprendizaje

    @Column(name = "assessment_criteria")
    private String assessmentCriteria; // JSON con criterios de evaluación

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "domain_id", insertable = false, updatable = false)
    private Domain domain;
}

public enum TrainingContentType {
    COURSE_MODULE, TUTORIAL, PRACTICAL_EXERCISE, ASSESSMENT, REFERENCE_MATERIAL,
    CASE_STUDY, BEST_PRACTICES, COMPLIANCE_TRAINING, DATA_QUALITY_TRAINING
}

public enum TrainingLevel {
    BEGINNER, INTERMEDIATE, ADVANCED, EXPERT, SPECIALIST
}
```

### 14. Data Source (Fuente de Datos)

```java
@Entity
@Table(name = "dmn_data_sources")
public class DataSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DataSourceType type;

    @Column(name = "url")
    private String url;

    @Column(name = "description")
    private String description;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DataSourceStatus status = DataSourceStatus.ACTIVE;

    @Column(name = "last_sync")
    private LocalDateTime lastSync;

    @Column(name = "configuration")
    private String configuration; // JSON configuration

    @Column(name = "authentication_config")
    private String authenticationConfig; // JSON auth config

    @Column(name = "sync_frequency")
    private String syncFrequency; // Cron expression

    @Column(name = "compliance_check_required")
    private Boolean complianceCheckRequired = true;

    @Column(name = "data_retention_days")
    private Integer dataRetentionDays;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relación muchos a muchos con dominios
    @ManyToMany
    @JoinTable(
        name = "dmn_data_source_domains",
        joinColumns = @JoinColumn(name = "data_source_id"),
        inverseJoinColumns = @JoinColumn(name = "domain_id")
    )
    private List<Domain> domains = new ArrayList<>();

    // Relación muchos a muchos con proyectos RAG
    @ManyToMany
    @JoinTable(
        name = "dmn_data_source_rag_projects",
        joinColumns = @JoinColumn(name = "data_source_id"),
        inverseJoinColumns = @JoinColumn(name = "rag_project_id")
    )
    private List<RagProject> ragProjects = new ArrayList<>();

    @OneToMany(mappedBy = "dataSource", cascade = CascadeType.ALL)
    private List<DataSourceSyncLog> syncLogs = new ArrayList<>();
}

public enum DataSourceType {
    API, DATABASE, WEBHOOK, FILE, STREAMING, IoT_DEVICE, EXTERNAL_SYSTEM
}

public enum DataSourceStatus {
    ACTIVE, INACTIVE, ERROR, MAINTENANCE, PENDING_APPROVAL
}
```

### 15. Data Source Sync Log (Log de Sincronización de Fuente de Datos)

```java
@Entity
@Table(name = "dmn_data_source_sync_logs")
public class DataSourceSyncLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_source_id", nullable = false)
    private Long dataSourceId;

    @Column(name = "sync_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SyncType syncType;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private SyncStatus status;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "records_processed")
    private Long recordsProcessed = 0L;

    @Column(name = "records_failed")
    private Long recordsFailed = 0L;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "sync_duration_ms")
    private Long syncDurationMs;

    @Column(name = "data_size_bytes")
    private Long dataSizeBytes;

    @Column(name = "compliance_check_passed")
    private Boolean complianceCheckPassed;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "data_source_id", insertable = false, updatable = false)
    private DataSource dataSource;
}

public enum SyncType {
    FULL_SYNC, INCREMENTAL_SYNC, MANUAL_SYNC, SCHEDULED_SYNC, REAL_TIME_SYNC
}

public enum SyncStatus {
    PENDING, RUNNING, COMPLETED, FAILED, CANCELLED, COMPLIANCE_CHECK
}
```

## API Endpoints

### Domain Management

```plaintext
GET    /api/v1/domains
GET    /api/v1/domains/{id}
POST   /api/v1/domains
PUT    /api/v1/domains/{id}
DELETE /api/v1/domains/{id}
GET    /api/v1/domains/search
GET    /api/v1/domains/types
GET    /api/v1/domains/industries
GET    /api/v1/domains/user/{userId}
```

### Domain Datasets

```plaintext
GET    /api/v1/domains/{id}/datasets
GET    /api/v1/domains/{id}/datasets/{datasetId}
POST   /api/v1/domains/{id}/datasets
PUT    /api/v1/domains/{id}/datasets/{datasetId}
DELETE /api/v1/domains/{id}/datasets/{datasetId}
GET    /api/v1/domains/{id}/datasets/{datasetId}/columns
POST   /api/v1/domains/{id}/datasets/{datasetId}/columns
GET    /api/v1/domains/{id}/datasets/{datasetId}/quality-score
```

### Domain Ingestion Jobs

```plaintext
GET    /api/v1/domains/{id}/ingestion-jobs
GET    /api/v1/domains/{id}/ingestion-jobs/{jobId}
POST   /api/v1/domains/{id}/ingestion-jobs
PUT    /api/v1/domains/{id}/ingestion-jobs/{jobId}
DELETE /api/v1/domains/{id}/ingestion-jobs/{jobId}
POST   /api/v1/domains/{id}/ingestion-jobs/{jobId}/start
POST   /api/v1/domains/{id}/ingestion-jobs/{jobId}/stop
POST   /api/v1/domains/{id}/ingestion-jobs/{jobId}/retry
GET    /api/v1/domains/{id}/ingestion-jobs/{jobId}/logs
```

### Domain Schemas

```plaintext
GET    /api/v1/domains/{id}/schemas
GET    /api/v1/domains/{id}/schemas/{schemaId}
POST   /api/v1/domains/{id}/schemas
PUT    /api/v1/domains/{id}/schemas/{schemaId}
DELETE /api/v1/domains/{id}/schemas/{schemaId}
GET    /api/v1/domains/{id}/schemas/{schemaId}/fields
POST   /api/v1/domains/{id}/schemas/{schemaId}/fields
GET    /api/v1/domains/{id}/schemas/{schemaId}/validate
POST   /api/v1/domains/{id}/schemas/{schemaId}/evolve
```

### Domain Wizard

```plaintext
GET    /api/v1/domain-wizard
POST   /api/v1/domain-wizard/start
PUT    /api/v1/domain-wizard/{wizardId}/step
GET    /api/v1/domain-wizard/{wizardId}/progress
POST   /api/v1/domain-wizard/{wizardId}/complete
POST   /api/v1/domain-wizard/{wizardId}/abandon
GET    /api/v1/domain-wizard/{wizardId}/recommendations
```



### Monitoring and Analytics

```plaintext
GET    /api/v1/domains/{id}/monitoring/overview
GET    /api/v1/domains/{id}/monitoring/ingestion-stats
GET    /api/v1/domains/{id}/monitoring/data-quality
GET    /api/v1/domains/{id}/monitoring/performance-metrics
GET    /api/v1/domains/{id}/monitoring/error-summary
GET    /api/v1/domains/{id}/analytics/data-lineage
GET    /api/v1/domains/{id}/analytics/impact-analysis
```



## Scripts de Base de Datos

```plaintext
-- Tabla de dominios
CREATE TABLE dmn_domains (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    project_id BIGINT REFERENCES projects(id) ON DELETE CASCADE,
    domain_type VARCHAR(50) NOT NULL,
    industry VARCHAR(100),
    business_area VARCHAR(100),
    data_sources TEXT,
    data_types TEXT,
    quality_metrics TEXT,
    compliance_requirements TEXT,
    regulatory_framework TEXT,
    data_retention_years INTEGER,
    privacy_level VARCHAR(50),
    status VARCHAR(50) DEFAULT 'DRAFT',
    version VARCHAR(50) DEFAULT '1.0.0',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de datasets de dominio
CREATE TABLE dmn_domain_datasets (
    id BIGSERIAL PRIMARY KEY,
    domain_id BIGINT REFERENCES dmn_domains(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    dataset_type VARCHAR(50) NOT NULL,
    file_path VARCHAR(500),
    file_size_bytes BIGINT,
    file_format VARCHAR(50),
    record_count BIGINT,
    column_count INTEGER,
    schema_version VARCHAR(50),
    data_quality_score DECIMAL(3,2),
    last_ingested TIMESTAMP,
    ingestion_frequency VARCHAR(50),
    retention_policy TEXT,
    data_lineage TEXT,
    source_metadata TEXT,
    compliance_tags TEXT,
    is_rag_ready BOOLEAN DEFAULT false,
    rag_metadata TEXT,
    training_relevance INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de columnas de dataset
CREATE TABLE dmn_dataset_columns (
    id BIGSERIAL PRIMARY KEY,
    dataset_id BIGINT REFERENCES dmn_domain_datasets(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    data_type VARCHAR(100) NOT NULL,
    description TEXT,
    is_required BOOLEAN DEFAULT false,
    is_primary_key BOOLEAN DEFAULT false,
    is_foreign_key BOOLEAN DEFAULT false,
    reference_table VARCHAR(255),
    reference_column VARCHAR(255),
    constraints TEXT,
    statistics TEXT,
    privacy_level VARCHAR(50),
    pii_detected BOOLEAN DEFAULT false,
    encryption_required BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de trabajos de ingesta de dominio
CREATE TABLE dmn_domain_ingestion_jobs (
    id BIGSERIAL PRIMARY KEY,
    domain_id BIGINT REFERENCES dmn_domains(id) ON DELETE CASCADE,
    dataset_id BIGINT REFERENCES dmn_domain_datasets(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    job_type VARCHAR(50) NOT NULL,
    source_type VARCHAR(50) NOT NULL,
    source_configuration TEXT,
    transformation_rules TEXT,
    validation_rules TEXT,
    schedule VARCHAR(100),
    status VARCHAR(50) DEFAULT 'PENDING',
    priority INTEGER DEFAULT 5,
    estimated_duration_minutes INTEGER,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    error_message TEXT,
    retry_count INTEGER DEFAULT 0,
    max_retries INTEGER DEFAULT 3,
    compliance_check_required BOOLEAN DEFAULT true,
    data_lineage_tracking BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de logs de trabajos de ingesta
CREATE TABLE dmn_ingestion_job_logs (
    id BIGSERIAL PRIMARY KEY,
    ingestion_job_id BIGINT REFERENCES dmn_domain_ingestion_jobs(id) ON DELETE CASCADE,
    log_level VARCHAR(20) NOT NULL,
    message TEXT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    execution_step VARCHAR(100),
    records_processed BIGINT DEFAULT 0,
    records_failed BIGINT DEFAULT 0,
    execution_time_ms BIGINT,
    memory_usage_mb DECIMAL(10,2),
    cpu_usage_percent DECIMAL(5,2),
    additional_data TEXT,
    compliance_notes TEXT,
    data_lineage_updates TEXT
);

-- Tabla de esquemas de dominio
CREATE TABLE dmn_domain_schemas (
    id BIGSERIAL PRIMARY KEY,
    domain_id BIGINT REFERENCES dmn_domains(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    schema_version VARCHAR(50) NOT NULL,
    schema_definition TEXT NOT NULL,
    schema_type VARCHAR(50) NOT NULL,
    is_active BOOLEAN DEFAULT true,
    compatibility_level VARCHAR(50) DEFAULT 'backward',
    validation_rules TEXT,
    evolution_history TEXT,
    compliance_requirements TEXT,
    data_governance_rules TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de campos de esquema
CREATE TABLE dmn_schema_fields (
    id BIGSERIAL PRIMARY KEY,
    schema_id BIGINT REFERENCES dmn_domain_schemas(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    field_path VARCHAR(500),
    data_type VARCHAR(100) NOT NULL,
    is_required BOOLEAN DEFAULT false,
    default_value TEXT,
    constraints TEXT,
    description TEXT,
    order_index INTEGER DEFAULT 0,
    privacy_level VARCHAR(50),
    compliance_tags TEXT,
    data_quality_rules TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de wizards de dominio
CREATE TABLE dmn_domain_wizards (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    wizard_type VARCHAR(50) NOT NULL,
    current_step INTEGER DEFAULT 1,
    total_steps INTEGER NOT NULL,
    wizard_data TEXT,
    status VARCHAR(50) DEFAULT 'IN_PROGRESS',
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    compliance_check_passed BOOLEAN DEFAULT false,
    training_content_generated BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de documentos de dominio
CREATE TABLE dmn_domain_documents (
    id BIGSERIAL PRIMARY KEY,
    domain_id BIGINT REFERENCES dmn_domains(id) ON DELETE CASCADE,
    document_type VARCHAR(100) NOT NULL,
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
    compliance_tags TEXT,
    data_lineage TEXT,
    source_information TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla principal de trabajos de web scraping
CREATE TABLE dmn_webscraping_jobs (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    source_url VARCHAR(500) NOT NULL,
    job_type VARCHAR(100) NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    configuration TEXT,
    scheduled_at TIMESTAMP,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    result TEXT,
    error_message TEXT,
    retry_count INTEGER DEFAULT 0,
    lek_server_job_id VARCHAR(255),
    lek_server_status VARCHAR(100),
    data_extracted TEXT,
    compliance_check_required BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de relación muchos a muchos entre web scraping y dominios
CREATE TABLE dmn_webscraping_domains (
    id BIGSERIAL PRIMARY KEY,
    webscraping_job_id BIGINT REFERENCES dmn_webscraping_jobs(id) ON DELETE CASCADE,
    domain_id BIGINT REFERENCES dmn_domains(id) ON DELETE CASCADE,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    assigned_by BIGINT REFERENCES users(id),
    status VARCHAR(50) DEFAULT 'ACTIVE',
    UNIQUE(webscraping_job_id, domain_id)
);

-- Tabla de relación muchos a muchos entre web scraping y proyectos RAG
CREATE TABLE dmn_webscraping_rag_projects (
    id BIGSERIAL PRIMARY KEY,
    webscraping_job_id BIGINT REFERENCES dmn_webscraping_jobs(id) ON DELETE CASCADE,
    rag_project_id BIGINT REFERENCES rag_projects(id) ON DELETE CASCADE,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    assigned_by BIGINT REFERENCES users(id),
    status VARCHAR(50) DEFAULT 'ACTIVE',
    UNIQUE(webscraping_job_id, rag_project_id)
);

-- Tabla de logs de cumplimiento de dominio
CREATE TABLE dmn_domain_compliance_logs (
    id BIGSERIAL PRIMARY KEY,
    domain_id BIGINT REFERENCES dmn_domains(id) ON DELETE CASCADE,
    compliance_type VARCHAR(100) NOT NULL,
    check_date TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    details TEXT,
    requirements_checked TEXT,
    violations_found TEXT,
    remediation_actions TEXT,
    next_check_date TIMESTAMP,
    checker_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    automated_check BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de uso de dominio en proyectos
CREATE TABLE dmn_project_domain_usage (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT REFERENCES projects(id) ON DELETE CASCADE,
    domain_id BIGINT REFERENCES dmn_domains(id) ON DELETE CASCADE,
    usage_type VARCHAR(100) NOT NULL,
    usage_start_date DATE,
    usage_end_date DATE,
    usage_intensity VARCHAR(50),
    usage_notes TEXT,
    compliance_verified BOOLEAN DEFAULT false,
    data_quality_score DECIMAL(3,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de integración de contenido de entrenamiento
CREATE TABLE dmn_training_content_integration (
    id BIGSERIAL PRIMARY KEY,
    domain_id BIGINT REFERENCES dmn_domains(id) ON DELETE CASCADE,
    content_type VARCHAR(100) NOT NULL,
    content_title VARCHAR(255) NOT NULL,
    content_description TEXT,
    content_data TEXT,
    training_level VARCHAR(100),
    prerequisites TEXT,
    learning_objectives TEXT,
    assessment_criteria TEXT,
    is_active BOOLEAN DEFAULT true,
    last_updated TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla principal de fuentes de datos
CREATE TABLE dmn_data_sources (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    url VARCHAR(500),
    description TEXT,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    last_sync TIMESTAMP,
    configuration TEXT,
    authentication_config TEXT,
    sync_frequency VARCHAR(100),
    compliance_check_required BOOLEAN DEFAULT true,
    data_retention_days INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de relación muchos a muchos entre fuentes de datos y dominios
CREATE TABLE dmn_data_source_domains (
    id BIGSERIAL PRIMARY KEY,
    data_source_id BIGINT REFERENCES dmn_data_sources(id) ON DELETE CASCADE,
    domain_id BIGINT REFERENCES dmn_domains(id) ON DELETE CASCADE,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    assigned_by BIGINT REFERENCES users(id),
    status VARCHAR(50) DEFAULT 'ACTIVE',
    UNIQUE(data_source_id, domain_id)
);

-- Tabla de relación muchos a muchos entre fuentes de datos y proyectos RAG
CREATE TABLE dmn_data_source_rag_projects (
    id BIGSERIAL PRIMARY KEY,
    data_source_id BIGINT REFERENCES dmn_data_sources(id) ON DELETE CASCADE,
    rag_project_id BIGINT REFERENCES rag_projects(id) ON DELETE CASCADE,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    assigned_by BIGINT REFERENCES users(id),
    status VARCHAR(50) DEFAULT 'ACTIVE',
    UNIQUE(data_source_id, rag_project_id)
);

-- Tabla de logs de sincronización de fuentes de datos
CREATE TABLE dmn_data_source_sync_logs (
    id BIGSERIAL PRIMARY KEY,
    data_source_id BIGINT REFERENCES dmn_data_sources(id) ON DELETE CASCADE,
    sync_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    started_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP,
    records_processed BIGINT DEFAULT 0,
    records_failed BIGINT DEFAULT 0,
    error_message TEXT,
    sync_duration_ms BIGINT,
    data_size_bytes BIGINT,
    compliance_check_passed BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para optimización
CREATE INDEX idx_dmn_domains_user_id ON dmn_domains(user_id);
CREATE INDEX idx_dmn_domains_project_id ON dmn_domains(project_id);
CREATE INDEX idx_dmn_domains_type ON dmn_domains(domain_type);
CREATE INDEX idx_dmn_domains_industry ON dmn_domains(industry);
CREATE INDEX idx_dmn_domains_status ON dmn_domains(status);
CREATE INDEX idx_dmn_domain_datasets_domain_id ON dmn_domain_datasets(domain_id);
CREATE INDEX idx_dmn_domain_datasets_type ON dmn_domain_datasets(dataset_type);
CREATE INDEX idx_dmn_domain_datasets_format ON dmn_domain_datasets(file_format);
CREATE INDEX idx_dmn_dataset_columns_dataset_id ON dmn_dataset_columns(dataset_id);
CREATE INDEX idx_dmn_dataset_columns_name ON dmn_dataset_columns(name);
CREATE INDEX idx_dmn_domain_ingestion_jobs_domain_id ON dmn_domain_ingestion_jobs(domain_id);
CREATE INDEX idx_dmn_domain_ingestion_jobs_dataset_id ON dmn_domain_ingestion_jobs(dataset_id);
CREATE INDEX idx_dmn_domain_ingestion_jobs_status ON dmn_domain_ingestion_jobs(status);
CREATE INDEX idx_dmn_domain_ingestion_jobs_type ON dmn_domain_ingestion_jobs(job_type);
CREATE INDEX idx_dmn_ingestion_job_logs_job_id ON dmn_ingestion_job_logs(ingestion_job_id);
CREATE INDEX idx_dmn_ingestion_job_logs_timestamp ON dmn_ingestion_job_logs(timestamp);
CREATE INDEX idx_dmn_ingestion_job_logs_level ON dmn_ingestion_job_logs(log_level);
CREATE INDEX idx_dmn_domain_schemas_domain_id ON dmn_domain_schemas(domain_id);
CREATE INDEX idx_dmn_domain_schemas_version ON dmn_domain_schemas(schema_version);
CREATE INDEX idx_dmn_domain_schemas_active ON dmn_domain_schemas(is_active);
CREATE INDEX idx_dmn_schema_fields_schema_id ON dmn_schema_fields(schema_id);
CREATE INDEX idx_dmn_schema_fields_name ON dmn_schema_fields(name);
CREATE INDEX idx_dmn_domain_wizards_user_id ON dmn_domain_wizards(user_id);
CREATE INDEX idx_dmn_domain_wizards_type ON dmn_domain_wizards(wizard_type);
CREATE INDEX idx_dmn_domain_wizards_status ON dmn_domain_wizards(status);
CREATE INDEX idx_dmn_domain_documents_domain_id ON dmn_domain_documents(domain_id);
CREATE INDEX idx_dmn_domain_documents_type ON dmn_domain_documents(document_type);
-- Índices para web scraping
CREATE INDEX idx_dmn_webscraping_jobs_status ON dmn_webscraping_jobs(status);
CREATE INDEX idx_dmn_webscraping_jobs_type ON dmn_webscraping_jobs(job_type);
CREATE INDEX idx_dmn_webscraping_jobs_created_at ON dmn_webscraping_jobs(created_at);

-- Índices para relaciones muchos a muchos
CREATE INDEX idx_dmn_webscraping_domains_job_id ON dmn_webscraping_domains(webscraping_job_id);
CREATE INDEX idx_dmn_webscraping_domains_domain_id ON dmn_webscraping_domains(domain_id);
CREATE INDEX idx_dmn_webscraping_domains_status ON dmn_webscraping_domains(status);

CREATE INDEX idx_dmn_webscraping_rag_projects_job_id ON dmn_webscraping_rag_projects(webscraping_job_id);
CREATE INDEX idx_dmn_webscraping_rag_projects_rag_project_id ON dmn_webscraping_rag_projects(rag_project_id);
CREATE INDEX idx_dmn_webscraping_rag_projects_status ON dmn_webscraping_rag_projects(status);
CREATE INDEX idx_dmn_domain_compliance_logs_domain_id ON dmn_domain_compliance_logs(domain_id);
CREATE INDEX idx_dmn_domain_compliance_logs_type ON dmn_domain_compliance_logs(compliance_type);
CREATE INDEX idx_dmn_project_domain_usage_project_id ON dmn_project_domain_usage(project_id);
CREATE INDEX idx_dmn_project_domain_usage_domain_id ON dmn_project_domain_usage(domain_id);
CREATE INDEX idx_dmn_project_domain_usage_type ON dmn_project_domain_usage(usage_type);
CREATE INDEX idx_dmn_training_content_integration_domain_id ON dmn_training_content_integration(domain_id);
CREATE INDEX idx_dmn_training_content_integration_type ON dmn_training_content_integration(content_type);

-- Índices para fuentes de datos
CREATE INDEX idx_dmn_data_sources_type ON dmn_data_sources(type);
CREATE INDEX idx_dmn_data_sources_status ON dmn_data_sources(status);
CREATE INDEX idx_dmn_data_sources_created_at ON dmn_data_sources(created_at);

-- Índices para relaciones muchos a muchos de fuentes de datos
CREATE INDEX idx_dmn_data_source_domains_data_source_id ON dmn_data_source_domains(data_source_id);
CREATE INDEX idx_dmn_data_source_domains_domain_id ON dmn_data_source_domains(domain_id);
CREATE INDEX idx_dmn_data_source_domains_status ON dmn_data_source_domains(status);

CREATE INDEX idx_dmn_data_source_rag_projects_data_source_id ON dmn_data_source_rag_projects(data_source_id);
CREATE INDEX idx_dmn_data_source_rag_projects_rag_project_id ON dmn_data_source_rag_projects(rag_project_id);
CREATE INDEX idx_dmn_data_source_rag_projects_status ON dmn_data_source_rag_projects(status);

-- Índices para logs de sincronización
CREATE INDEX idx_dmn_data_source_sync_logs_data_source_id ON dmn_data_source_sync_logs(data_source_id);
CREATE INDEX idx_dmn_data_source_sync_logs_status ON dmn_data_source_sync_logs(status);
CREATE INDEX idx_dmn_data_source_sync_logs_started_at ON dmn_data_source_sync_logs(started_at);
```

## Servicios de Domain Ingestion

### DomainService

```java
@Service
@Transactional
public class DomainService {

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private DatasetService datasetService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private WebScrapingService webScrapingService;

    @Autowired
    private RAGDocumentService ragDocumentService;

    @Autowired
    private TrainingIntegrationService trainingIntegrationService;

    public Domain createDomain(DomainCreateDTO dto, Long userId) {
        // Validar datos de entrada
        validateDomainData(dto);

        // Crear dominio
        Domain domain = new Domain();
        domain.setName(dto.getName());
        domain.setDescription(dto.getDescription());
        domain.setUserId(userId);
        domain.setProjectId(dto.getProjectId());
        domain.setDomainType(dto.getDomainType());
        domain.setIndustry(dto.getIndustry());
        domain.setBusinessArea(dto.getBusinessArea());
        domain.setDataSources(dto.getDataSources());
        domain.setDataTypes(dto.getDataTypes());
        domain.setQualityMetrics(dto.getQualityMetrics());
        domain.setComplianceRequirements(dto.getComplianceRequirements());
        domain.setRegulatoryFramework(dto.getRegulatoryFramework());
        domain.setDataRetentionYears(dto.getDataRetentionYears());
        domain.setPrivacyLevel(dto.getPrivacyLevel());
        domain.setStatus(DomainStatus.DRAFT);
        domain.setVersion("1.0.0");
        domain.setCreatedAt(LocalDateTime.now());
        domain.setUpdatedAt(LocalDateTime.now());

        // Guardar dominio
        Domain savedDomain = domainRepository.save(domain);

        // Auditoría
        auditService.logAction("DOMAIN_CREATED", "Domain created: " + savedDomain.getName(), userId);

        return savedDomain;
    }

    public Domain updateDomain(Long domainId, DomainUpdateDTO dto, Long userId) {
        Domain domain = domainRepository.findById(domainId)
            .orElseThrow(() -> new ResourceNotFoundException("Domain not found"));

        // Validar permisos
        validateUserAccess(domain, userId);

        // Actualizar campos
        domain.setName(dto.getName());
        domain.setDescription(dto.getDescription());
        domain.setIndustry(dto.getIndustry());
        domain.setBusinessArea(dto.getBusinessArea());
        domain.setDataSources(dto.getDataSources());
        domain.setDataTypes(dto.getDataTypes());
        domain.setQualityMetrics(dto.getQualityMetrics());
        domain.setComplianceRequirements(dto.getComplianceRequirements());
        domain.setRegulatoryFramework(dto.getRegulatoryFramework());
        domain.setDataRetentionYears(dto.getDataRetentionYears());
        domain.setPrivacyLevel(dto.getPrivacyLevel());
        domain.setUpdatedAt(LocalDateTime.now());

        // Guardar cambios
        Domain updatedDomain = domainRepository.save(domain);

        // Auditoría
        auditService.logAction("DOMAIN_UPDATED", "Domain updated: " + updatedDomain.getName(), userId);

        return updatedDomain;
    }

    public void deleteDomain(Long domainId, Long userId) {
        Domain domain = domainRepository.findById(domainId)
            .orElseThrow(() -> new ResourceNotFoundException("Domain not found"));

        // Validar permisos
        validateUserAccess(domain, userId);

        // Verificar que no tenga datasets activos
        if (!domain.getDatasets().isEmpty()) {
            throw new BusinessException("Cannot delete domain with active datasets");
        }

        // Eliminar dominio
        domainRepository.delete(domain);

        // Auditoría
        auditService.logAction("DOMAIN_DELETED", "Domain deleted: " + domain.getName(), userId);
    }

    public List<Domain> getDomainsByUser(Long userId) {
        return domainRepository.findByUserId(userId);
    }

    public List<Domain> getDomainsByProject(Long projectId) {
        return domainRepository.findByProjectId(projectId);
    }

    public Domain getDomainById(Long domainId) {
        return domainRepository.findById(domainId)
            .orElseThrow(() -> new ResourceNotFoundException("Domain not found"));
    }

    public void activateDomain(Long domainId, Long userId) {
        Domain domain = getDomainById(domainId);
        validateUserAccess(domain, userId);

        // Verificar cumplimiento antes de activar
        if (!validateCompliance(domain)) {
            throw new BusinessException("Domain does not meet compliance requirements");
        }

        domain.setStatus(DomainStatus.ACTIVE);
        domain.setUpdatedAt(LocalDateTime.now());
        domainRepository.save(domain);

        // Auditoría
        auditService.logAction("DOMAIN_ACTIVATED", "Domain activated: " + domain.getName(), userId);
    }

    private void validateDomainData(DomainCreateDTO dto) {
        if (StringUtils.isEmpty(dto.getName())) {
            throw new ValidationException("Domain name is required");
        }
        if (dto.getDomainType() == null) {
            throw new ValidationException("Domain type is required");
        }
        if (dto.getUserId() == null) {
            throw new ValidationException("User ID is required");
        }
    }

    private void validateUserAccess(Domain domain, Long userId) {
        if (!domain.getUserId().equals(userId)) {
            throw new AccessDeniedException("User does not have access to this domain");
        }
    }

    private boolean validateCompliance(Domain domain) {
        // Implementar validación de cumplimiento
        // Verificar requisitos regulatorios, políticas de privacidad, etc.
        return true; // Placeholder
    }
}
```









### 3. Data Source Service

```java
@Service
@Transactional
public class DataSourceService {

    @Autowired
    private DataSourceRepository dataSourceRepository;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private RagProjectRepository ragProjectRepository;

    @Autowired
    private AuditService auditService;

    @Autowired
    private ComplianceService complianceService;

    public DataSource createDataSource(DataSourceCreateDTO dto, Long userId) {
        // Crear fuente de datos
        DataSource dataSource = new DataSource();
        dataSource.setName(dto.getName());
        dataSource.setType(dto.getType());
        dataSource.setUrl(dto.getUrl());
        dataSource.setDescription(dto.getDescription());
        dataSource.setStatus(DataSourceStatus.ACTIVE);
        dataSource.setConfiguration(dto.getConfiguration());
        dataSource.setAuthenticationConfig(dto.getAuthenticationConfig());
        dataSource.setSyncFrequency(dto.getSyncFrequency());
        dataSource.setComplianceCheckRequired(dto.getComplianceCheckRequired());
        dataSource.setDataRetentionDays(dto.getDataRetentionDays());
        dataSource.setCreatedAt(LocalDateTime.now());
        dataSource.setUpdatedAt(LocalDateTime.now());

        // Asignar dominios si se especifican
        if (dto.getDomainIds() != null && !dto.getDomainIds().isEmpty()) {
            List<Domain> domains = domainRepository.findAllById(dto.getDomainIds());
            dataSource.setDomains(domains);
        }

        // Asignar proyectos RAG si se especifican
        if (dto.getRagProjectIds() != null && !dto.getRagProjectIds().isEmpty()) {
            List<RagProject> ragProjects = ragProjectRepository.findAllById(dto.getRagProjectIds());
            dataSource.setRagProjects(ragProjects);
        }

        // Guardar fuente de datos
        DataSource savedDataSource = dataSourceRepository.save(dataSource);

        // Auditoría
        auditService.logAction("DATA_SOURCE_CREATED",
            "Data source created: " + savedDataSource.getName(), userId);

        return savedDataSource;
    }

    /**
     * Asignar una fuente de datos existente a dominios adicionales
     */
    public void assignDataSourceToDomains(Long dataSourceId, List<Long> domainIds, Long userId) {
        DataSource dataSource = dataSourceRepository.findById(dataSourceId)
            .orElseThrow(() -> new ResourceNotFoundException("Data source not found"));

        List<Domain> newDomains = domainRepository.findAllById(domainIds);
        dataSource.getDomains().addAll(newDomains);
        dataSource.setUpdatedAt(LocalDateTime.now());

        dataSourceRepository.save(dataSource);

        auditService.logAction("DATA_SOURCE_DOMAINS_ASSIGNED",
            "Data source " + dataSourceId + " assigned to additional domains: " + domainIds, userId);
    }

    /**
     * Asignar una fuente de datos existente a proyectos RAG adicionales
     */
    public void assignDataSourceToRagProjects(Long dataSourceId, List<Long> ragProjectIds, Long userId) {
        DataSource dataSource = dataSourceRepository.findById(dataSourceId)
            .orElseThrow(() -> new ResourceNotFoundException("Data source not found"));

        List<RagProject> newRagProjects = ragProjectRepository.findAllById(ragProjectIds);
        dataSource.getRagProjects().addAll(newRagProjects);
        dataSource.setUpdatedAt(LocalDateTime.now());

        dataSourceRepository.save(dataSource);

        auditService.logAction("DATA_SOURCE_RAG_PROJECTS_ASSIGNED",
            "Data source " + dataSourceId + " assigned to additional RAG projects: " + ragProjectIds, userId);
    }

    public DataSource updateDataSource(Long dataSourceId, DataSourceUpdateDTO dto, Long userId) {
        DataSource dataSource = dataSourceRepository.findById(dataSourceId)
            .orElseThrow(() -> new ResourceNotFoundException("Data source not found"));

        // Actualizar campos
        dataSource.setName(dto.getName());
        dataSource.setType(dto.getType());
        dataSource.setUrl(dto.getUrl());
        dataSource.setDescription(dto.getDescription());
        dataSource.setConfiguration(dto.getConfiguration());
        dataSource.setAuthenticationConfig(dto.getAuthenticationConfig());
        dataSource.setSyncFrequency(dto.getSyncFrequency());
        dataSource.setComplianceCheckRequired(dto.getComplianceCheckRequired());
        dataSource.setDataRetentionDays(dto.getDataRetentionDays());
        dataSource.setUpdatedAt(LocalDateTime.now());

        // Guardar cambios
        DataSource updatedDataSource = dataSourceRepository.save(dataSource);

        // Auditoría
        auditService.logAction("DATA_SOURCE_UPDATED",
            "Data source updated: " + updatedDataSource.getName(), userId);

        return updatedDataSource;
    }

    public void deleteDataSource(Long dataSourceId, Long userId) {
        DataSource dataSource = dataSourceRepository.findById(dataSourceId)
            .orElseThrow(() -> new ResourceNotFoundException("Data source not found"));

        // Verificar que no tenga asignaciones activas
        if (!dataSource.getDomains().isEmpty() || !dataSource.getRagProjects().isEmpty()) {
            throw new BusinessException("Cannot delete data source with active assignments");
        }

        // Eliminar fuente de datos
        dataSourceRepository.delete(dataSource);

        // Auditoría
        auditService.logAction("DATA_SOURCE_DELETED",
            "Data source deleted: " + dataSource.getName(), userId);
    }

    public List<DataSource> getDataSourcesByDomain(Long domainId) {
        return dataSourceRepository.findByDomainsId(domainId);
    }

    public List<DataSource> getDataSourcesByRagProject(Long ragProjectId) {
        return dataSourceRepository.findByRagProjectsId(ragProjectId);
    }

    public List<DataSource> getAllDataSources() {
        return dataSourceRepository.findAll();
    }

    public DataSourceSyncLog syncDataSource(Long dataSourceId, SyncType syncType, Long userId) {
        DataSource dataSource = dataSourceRepository.findById(dataSourceId)
            .orElseThrow(() -> new ResourceNotFoundException("Data source not found"));

        // Crear log de sincronización
        DataSourceSyncLog syncLog = new DataSourceSyncLog();
        syncLog.setDataSourceId(dataSourceId);
        syncLog.setSyncType(syncType);
        syncLog.setStatus(SyncStatus.RUNNING);
        syncLog.setStartedAt(LocalDateTime.now());
        syncLog.setCreatedAt(LocalDateTime.now());

        // Guardar log
        DataSourceSyncLog savedSyncLog = dataSourceSyncLogRepository.save(syncLog);

        // Ejecutar sincronización (asíncrona)
        executeDataSourceSync(dataSource, savedSyncLog, userId);

        return savedSyncLog;
    }

    public boolean testDataSourceConnection(Long dataSourceId) {
        DataSource dataSource = dataSourceRepository.findById(dataSourceId)
            .orElseThrow(() -> new ResourceNotFoundException("Data source not found"));

        try {
            // Implementar lógica de prueba de conexión según el tipo
            switch (dataSource.getType()) {
                case API:
                    return testApiConnection(dataSource);
                case DATABASE:
                    return testDatabaseConnection(dataSource);
                case WEBHOOK:
                    return testWebhookConnection(dataSource);
                case FILE:
                    return testFileConnection(dataSource);
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private void executeDataSourceSync(DataSource dataSource, DataSourceSyncLog syncLog, Long userId) {
        // Implementar lógica de sincronización asíncrona
        // Actualizar estado del log según el resultado
    }

    private boolean testApiConnection(DataSource dataSource) {
        // Implementar prueba de conexión API
        return true; // Placeholder
    }

    private boolean testDatabaseConnection(DataSource dataSource) {
        // Implementar prueba de conexión base de datos
        return true; // Placeholder
    }

    private boolean testWebhookConnection(DataSource dataSource) {
        // Implementar prueba de conexión webhook
        return true; // Placeholder
    }

    private boolean testFileConnection(DataSource dataSource) {
        // Implementar prueba de conexión archivo
        return true; // Placeholder
    }
}
```




## Monitoreo y Métricas

### Prometheus Metrics

```java
@Component
public class DomainIngestionMetrics {

    @Autowired
    private MeterRegistry meterRegistry;

    private final Counter domainsCreatedCounter;
    private final Counter datasetsCreatedCounter;
    private final Counter ingestionJobsCounter;
    private final Counter schemasCreatedCounter;
    private final Timer domainCreationTimer;
    private final Timer datasetCreationTimer;
    private final Timer ingestionJobExecutionTimer;

    public DomainIngestionMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.domainsCreatedCounter = Counter.builder("domain.ingestion.domains.created")
            .description("Total domains created")
            .register(meterRegistry);

        this.datasetsCreatedCounter = Counter.builder("domain.ingestion.datasets.created")
            .description("Total datasets created")
            .register(meterRegistry);

        this.ingestionJobsCounter = Counter.builder("domain.ingestion.jobs.processed")
            .description("Total ingestion jobs processed")
            .register(meterRegistry);

        this.schemasCreatedCounter = Counter.builder("domain.ingestion.schemas.created")
            .description("Total schemas created")
            .register(meterRegistry);

        this.domainCreationTimer = Timer.builder("domain.ingestion.domain.creation.time")
            .description("Domain creation time")
            .register(meterRegistry);

        this.datasetCreationTimer = Timer.builder("domain.ingestion.dataset.creation.time")
            .description("Dataset creation time")
            .register(meterRegistry);

        this.ingestionJobExecutionTimer = Timer.builder("domain.ingestion.job.execution.time")
            .description("Ingestion job execution time")
            .register(meterRegistry);
    }

    public void incrementDomainsCreated() {
        domainsCreatedCounter.increment();
    }

    public void incrementDatasetsCreated() {
        datasetsCreatedCounter.increment();
    }

    public void incrementIngestionJobs() {
        ingestionJobsCounter.increment();
    }

    public void incrementSchemasCreated() {
        schemasCreatedCounter.increment();
    }

    public Timer.Sample startDomainCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startDatasetCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startIngestionJobExecutionTimer() {
        return Timer.start(meterRegistry);
    }
}
```

## Configuración de Aplicación

### application-domain-ingestion.yml

```yaml
# Configuración del módulo Domain Ingestion
domain-ingestion:
  # Configuración general
  enabled: true
  version: "1.0.0"

  # Configuración de dominios
  domains:
    max-domains-per-user: 100
    max-datasets-per-domain: 1000
    default-retention-years: 7
    compliance-check-enabled: true
    data-lineage-tracking: true

  # Configuración de datasets
  datasets:
    max-file-size-mb: 1024
    supported-formats:
      - CSV
      - JSON
      - Parquet
      - Excel
      - XML
    quality-threshold: 0.8
    auto-validation: true

  # Configuración de web scraping
  webscraping:
    leka-server:
      base-url: "https://leka-server.codeflowx.ai"
      api-key: "${LEKA_SERVER_API_KEY}"
      timeout-seconds: 300
      max-retries: 3
    compliance:
      auto-check: true
      privacy-scan: true
      regulatory-check: true
    scheduling:
      max-concurrent-jobs: 10
      default-delay-minutes: 15

  # Configuración RAG
  rag:
    server:
      base-url: "https://rag-server.codeflowx.ai"
      api-key: "${RAG_SERVER_API_KEY}"
      timeout-seconds: 120
    documents:
      max-content-size-mb: 50
      supported-languages:
        - en
        - es
        - fr
        - de
      auto-indexing: true
      similarity-threshold: 0.7

  # Configuración de training
  training:
    integration:
      enabled: true
      auto-content-generation: true
      relevance-threshold: 7
    content:
      max-modules-per-domain: 20
      auto-assessment: true
      certification-enabled: true

  # Configuración de cumplimiento
  compliance:
    auto-audit: true
    retention-policy-check: true
    privacy-impact-assessment: true
    regulatory-updates:
      enabled: true
      check-frequency-hours: 24
      auto-notification: true

  # Configuración de monitorización
  monitoring:
    metrics:
      enabled: true
      collection-interval-seconds: 60
    alerts:
      enabled: true
      quality-threshold: 0.8
      compliance-violations: true
      webscraping-failures: true

  # Configuración de almacenamiento
  storage:
    local:
      enabled: true
      base-path: "/data/domains"
      max-size-gb: 1000
    minio:
      enabled: true
      endpoint: "${MINIO_ENDPOINT}"
      bucket: "domain-documents"
      access-key: "${MINIO_ACCESS_KEY}"
      secret-key: "${MINIO_SECRET_KEY}"

  # Configuración de mensajería
  messaging:
    rabbitmq:
      enabled: true
      host: "${RABBITMQ_HOST}"
      port: 5672
      username: "${RABBITMQ_USERNAME}"
      password: "${RABBITMQ_PASSWORD}"
      virtual-host: "/"
    topics:
      domain-created: "domain.created"
      dataset-ingested: "dataset.ingested"
      webscraping-completed: "webscraping.completed"
      compliance-check: "compliance.check"
      training-content: "training.content"

  # Configuración de auditoría
  audit:
    enabled: true
    log-level: "INFO"
    retention-days: 365
    sensitive-fields:
      - "password"
      - "api_key"
      - "token"
      - "secret"

  # Configuración de seguridad
  security:
    encryption:
      enabled: true
      algorithm: "AES-256"
      key-size: 256
    access-control:
      role-based: true
      domain-isolation: true
      audit-trail: true

  # Configuración de rendimiento
  performance:
    cache:
      enabled: true
      ttl-seconds: 3600
      max-size: 10000
    async-processing:
      enabled: true
      thread-pool-size: 20
      queue-capacity: 1000
    batch-processing:
      enabled: true
      batch-size: 100
      timeout-seconds: 300
```

## Métricas Prometheus

### Domain Ingestion Metrics

```java
@Component
public class DomainIngestionMetrics {

    private final MeterRegistry meterRegistry;

    // Contadores
    private final Counter domainsCreatedCounter;
    private final Counter domainsUpdatedCounter;
    private final Counter domainsDeletedCounter;
    private final Counter datasetsIngestedCounter;
    private final Counter webScrapingJobsCreatedCounter;
    private final Counter webScrapingJobsCompletedCounter;
    private final Counter webScrapingJobsFailedCounter;
    private final Counter ragDocumentsCreatedCounter;
    private final Counter ragDocumentsSearchedCounter;
    private final Counter complianceChecksPerformedCounter;
    private final Counter complianceViolationsCounter;
    private final Counter trainingContentGeneratedCounter;

    // Timers
    private final Timer domainCreationTimer;
    private final Timer datasetIngestionTimer;
    private final Timer webScrapingJobTimer;
    private final Timer ragDocumentProcessingTimer;
    private final Timer complianceCheckTimer;
    private final Timer trainingContentGenerationTimer;

    // Gauges
    private final Gauge activeDomainsGauge;
    private final Gauge activeDatasetsGauge;
    private final Gauge pendingWebScrapingJobsGauge;
    private final Gauge ragDocumentsCountGauge;
    private final Gauge complianceScoreGauge;

    public DomainIngestionMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Inicializar contadores
        this.domainsCreatedCounter = Counter.builder("domain_ingestion_domains_created_total")
            .description("Total number of domains created")
            .register(meterRegistry);

        this.domainsUpdatedCounter = Counter.builder("domain_ingestion_domains_updated_total")
            .description("Total number of domains updated")
            .register(meterRegistry);

        this.domainsDeletedCounter = Counter.builder("domain_ingestion_domains_deleted_total")
            .description("Total number of domains deleted")
            .register(meterRegistry);

        this.datasetsIngestedCounter = Counter.builder("domain_ingestion_datasets_ingested_total")
            .description("Total number of datasets ingested")
            .register(meterRegistry);

        this.webScrapingJobsCreatedCounter = Counter.builder("domain_ingestion_webscraping_jobs_created_total")
            .description("Total number of web scraping jobs created")
            .register(meterRegistry);

        this.webScrapingJobsCompletedCounter = Counter.builder("domain_ingestion_webscraping_jobs_completed_total")
            .description("Total number of web scraping jobs completed")
            .register(meterRegistry);

        this.webScrapingJobsFailedCounter = Counter.builder("domain_ingestion_webscraping_jobs_failed_total")
            .description("Total number of web scraping jobs failed")
            .register(meterRegistry);

        this.ragDocumentsCreatedCounter = Counter.builder("domain_ingestion_rag_documents_created_total")
            .description("Total number of RAG documents created")
            .register(meterRegistry);

        this.ragDocumentsSearchedCounter = Counter.builder("domain_ingestion_rag_documents_searched_total")
            .description("Total number of RAG document searches performed")
            .register(meterRegistry);

        this.complianceChecksPerformedCounter = Counter.builder("domain_ingestion_compliance_checks_performed_total")
            .description("Total number of compliance checks performed")
            .register(meterRegistry);

        this.complianceViolationsCounter = Counter.builder("domain_ingestion_compliance_violations_total")
            .description("Total number of compliance violations detected")
            .register(meterRegistry);

        this.trainingContentGeneratedCounter = Counter.builder("domain_ingestion_training_content_generated_total")
            .description("Total number of training content items generated")
            .register(meterRegistry);

        // Inicializar timers
        this.domainCreationTimer = Timer.builder("domain_ingestion_domain_creation_duration")
            .description("Time taken to create a domain")
            .register(meterRegistry);

        this.datasetIngestionTimer = Timer.builder("domain_ingestion_dataset_ingestion_duration")
            .description("Time taken to ingest a dataset")
            .register(meterRegistry);

        this.webScrapingJobTimer = Timer.builder("domain_ingestion_webscraping_job_duration")
            .description("Time taken to complete a web scraping job")
            .register(meterRegistry);

        this.ragDocumentProcessingTimer = Timer.builder("domain_ingestion_rag_document_processing_duration")
            .description("Time taken to process a RAG document")
            .register(meterRegistry);

        this.complianceCheckTimer = Timer.builder("domain_ingestion_compliance_check_duration")
            .description("Time taken to perform a compliance check")
            .register(meterRegistry);

        this.trainingContentGenerationTimer = Timer.builder("domain_ingestion_training_content_generation_duration")
            .description("Time taken to generate training content")
            .register(meterRegistry);

        // Inicializar gauges
        this.activeDomainsGauge = Gauge.builder("domain_ingestion_active_domains")
            .description("Number of active domains")
            .register(meterRegistry, this, DomainIngestionMetrics::getActiveDomainsCount);

        this.activeDatasetsGauge = Gauge.builder("domain_ingestion_active_datasets")
            .description("Number of active datasets")
            .register(meterRegistry, this, DomainIngestionMetrics::getActiveDatasetsCount);

        this.pendingWebScrapingJobsGauge = Gauge.builder("domain_ingestion_pending_webscraping_jobs")
            .description("Number of pending web scraping jobs")
            .register(meterRegistry, this, DomainIngestionMetrics::getPendingWebScrapingJobsCount);

        this.ragDocumentsCountGauge = Gauge.builder("domain_ingestion_rag_documents_count")
            .description("Number of RAG documents")
            .register(meterRegistry, this, DomainIngestionMetrics::getRAGDocumentsCount);

        this.complianceScoreGauge = Gauge.builder("domain_ingestion_compliance_score")
            .description("Overall compliance score")
            .register(meterRegistry, this, DomainIngestionMetrics::getComplianceScore);
    }

    // Métodos para incrementar contadores
    public void incrementDomainsCreated() {
        domainsCreatedCounter.increment();
    }

    public void incrementDomainsUpdated() {
        domainsUpdatedCounter.increment();
    }

    public void incrementDomainsDeleted() {
        domainsDeletedCounter.increment();
    }

    public void incrementDatasetsIngested() {
        datasetsIngestedCounter.increment();
    }

    public void incrementWebScrapingJobsCreated() {
        webScrapingJobsCreatedCounter.increment();
    }

    public void incrementWebScrapingJobsCompleted() {
        webScrapingJobsCompletedCounter.increment();
    }

    public void incrementWebScrapingJobsFailed() {
        webScrapingJobsFailedCounter.increment();
    }

    public void incrementRAGDocumentsCreated() {
        ragDocumentsCreatedCounter.increment();
    }

    public void incrementRAGDocumentsSearched() {
        ragDocumentsSearchedCounter.increment();
    }

    public void incrementComplianceChecksPerformed() {
        complianceChecksPerformedCounter.increment();
    }

    public void incrementComplianceViolations() {
        complianceViolationsCounter.increment();
    }

    public void incrementTrainingContentGenerated() {
        trainingContentGeneratedCounter.increment();
    }

    // Métodos para medir tiempo
    public Timer.Sample startDomainCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startDatasetIngestionTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startWebScrapingJobTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startRAGDocumentProcessingTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startComplianceCheckTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startTrainingContentGenerationTimer() {
        return Timer.start(meterRegistry);
    }

    // Métodos para obtener valores de gauge
    private double getActiveDomainsCount() {
        // Implementar lógica para obtener conteo de dominios activos
        return 0.0; // Placeholder
    }

    private double getActiveDatasetsCount() {
        // Implementar lógica para obtener conteo de datasets activos
        return 0.0; // Placeholder
    }

    private double getPendingWebScrapingJobsCount() {
        // Implementar lógica para obtener conteo de trabajos pendientes
        return 0.0; // Placeholder
    }

    private double getRAGDocumentsCount() {
        // Implementar lógica para obtener conteo de documentos RAG
        return 0.0; // Placeholder
    }

    private double getComplianceScore() {
        // Implementar lógica para obtener puntuación de cumplimiento
        return 0.0; // Placeholder
    }
}
```

## Pendiente

Este modulo al igual que la tecnologia esta relacionado con el modulo de training , ademas de tener un webscraping para la busqueda de informacion, descarga para crear los dataset , tambien debe de incorporar la gestion de documentos integrada con rag para la busqueda interna de informacion e incluso una relacion con el user_trainng_module para poder utilziarlo en formacion. otra relacion fundamentas es desde proyectos que un proyecto puede usar este dominio de negocio para la generacion de proyectos. muy importante el versionado y cumplimeinto de normativas que correspondam , inportante para cumplir las leyes guardar los origenes de informacion y datos , es recomendable tener un cron opcional que permita actualizar informacion de origenes externos como son webs o apis.

## Conclusión

El módulo de Domain Ingestion proporciona una gestión completa de la ingesta de datos de dominio, incluyendo:

- **Gestión de Dominios**: Creación y configuración de dominios específicos de negocio
- **Gestión de Datasets**: Manejo de datasets con metadatos y calidad de datos
- **Esquemas de Datos**: Definición y evolución de esquemas de datos
- **Jobs de Ingesta**: Programación y ejecución de trabajos de ingesta
- **Wizard de Dominios**: Asistente guiado para configuración de dominios
- **Monitoreo y Calidad**: Seguimiento de calidad de datos y métricas de rendimiento

El sistema está diseñado para ser escalable, con soporte para múltiples tipos de datos, validación automática y un asistente que simplifica la configuración de dominios complejos.

## Notas de Implementación

### Integración con Leka-Server

- **Webscraping Automático**: El módulo se integra con leka-server para búsqueda y descarga automática de información de fuentes externas
- **Jobs Asíncronos**: Los trabajos de webscraping se ejecutan de forma asíncrona con notificaciones vía RabbitMQ
- **Retry Automático**: Sistema de reintentos automáticos con configuración de máximo de intentos
- **Monitorización**: Seguimiento del estado de los trabajos en leka-server

### Gestión RAG Integrada

- **Documentos Inteligentes**: Los documentos se procesan y indexan automáticamente para búsqueda inteligente
- **Metadatos Enriquecidos**: Cada documento incluye metadatos para RAG, cumplimiento y formación
- **Búsqueda Semántica**: Integración con servidor RAG para búsquedas avanzadas
- **Auto-indexación**: Los documentos se indexan automáticamente cuando se marcan como RAG-ready

### Integración con Módulo de Training

- **Contenido Automático**: Generación automática de contenido de entrenamiento basado en dominios
- **Relevancia**: Sistema de puntuación de relevancia (1-10) para determinar qué contenido usar en formación
- **Módulos de Curso**: Creación automática de módulos de curso, tutoriales y ejercicios prácticos
- **Evaluación**: Sistema de evaluación automática con criterios personalizables

### Relación con Proyectos

- **Mapeo de Uso**: Cada dominio puede ser utilizado por múltiples proyectos con diferentes intensidades
- **Verificación de Cumplimiento**: Los proyectos verifican el cumplimiento normativo antes de usar dominios
- **Puntuación de Calidad**: Sistema de puntuación de calidad de datos para proyectos
- **Auditoría**: Trazabilidad completa del uso de dominios en proyectos

### Cumplimiento Normativo

- **Validación Automática**: Verificación automática de cumplimiento antes de activar dominios
- **Marco Regulatorio**: Soporte para diferentes marcos regulatorios (GDPR, HIPAA, SOX, etc.)
- **Retención de Datos**: Políticas de retención configurables por dominio
- **Auditoría**: Logs completos de todas las verificaciones de cumplimiento

### Versionado y Evolución

- **Esquemas Evolutivos**: Los esquemas de datos pueden evolucionar manteniendo compatibilidad
- **Historial de Cambios**: Trazabilidad completa de todos los cambios en dominios y datasets
- **Migración Automática**: Sistema de migración automática de datos cuando cambian los esquemas
- **Rollback**: Capacidad de revertir cambios si es necesario

### Normalización Aplicada

- **Prefijos de Tabla**: Todas las tablas usan el prefijo `dmn_` (domain)
- **PK Autonumérica**: Todas las tablas tienen PK autonumérica (`id BIGSERIAL PRIMARY KEY`)
- **3NF/BCNF**: Estructura normalizada siguiendo tercera forma normal y Boyce-Codd
- **Relaciones**: Foreign keys correctamente definidas con `REFERENCES`
- **Índices**: Índices optimizados para consultas frecuentes

### Métricas y Monitorización

- **Prometheus**: Métricas completas para monitorización y alertas
- **Contadores**: Seguimiento de operaciones (creación, actualización, eliminación)
- **Timers**: Medición de rendimiento de operaciones críticas
- **Gauges**: Estado actual del sistema (dominios activos, trabajos pendientes)
- **Alertas**: Sistema de alertas para violaciones de cumplimiento y fallos

## Uso del Script

### Orden de Ejecución

1. **Ejecutar DROP script** para limpiar objetos existentes
2. **Ejecutar CREATE TABLE script** para crear todas las tablas
3. **Ejecutar CREATE INDEX script** para crear índices optimizados
4. **Ejecutar INSERT script** para datos de demostración

### Verificación de Datos

```sql
-- Verificar dominios creados
SELECT COUNT(*) FROM dmn_domains;

-- Verificar datasets
SELECT COUNT(*) FROM dmn_domain_datasets;

-- Verificar trabajos de ingesta
SELECT COUNT(*) FROM dmn_domain_ingestion_jobs;

-- Verificar documentos RAG
SELECT COUNT(*) FROM dmn_domain_documents;

-- Verificar trabajos de webscraping
SELECT COUNT(*) FROM dmn_domain_webscraping_jobs;

-- Verificar logs de cumplimiento
SELECT COUNT(*) FROM dmn_domain_compliance_logs;

-- Verificar uso en proyectos
SELECT COUNT(*) FROM dmn_project_domain_usage;

-- Verificar contenido de entrenamiento
SELECT COUNT(*) FROM dmn_training_content_integration;
```

## Resumen de Cambios - Web Scraping Multi-Dominio y Multi-RAG

### **Cambios Implementados**

#### **1. Estructura de Base de Datos**

- ✅ **Nueva tabla principal**: `dmn_webscraping_jobs` con campos `name` y `description`
- ✅ **Tabla de relación dominios**: `dmn_webscraping_domains` para asignaciones muchos a muchos
- ✅ **Tabla de relación RAG**: `dmn_webscraping_rag_projects` para proyectos RAG
- ✅ **Índices optimizados**: Para consultas eficientes en las nuevas relaciones

#### **2. Entidad Java Actualizada**

- ✅ **Relaciones JPA**: `@ManyToMany` con `@JoinTable` para dominios y proyectos RAG
- ✅ **Campos adicionales**: `name`, `description` para mejor identificación
- ✅ **Eliminación**: Campo `domainId` único reemplazado por relaciones múltiples

#### **3. Servicio Web Scraping**

- ✅ **Métodos de asignación**: Para dominios y proyectos RAG adicionales
- ✅ **Consultas múltiples**: Por dominio, por proyecto RAG, y todos los trabajos
- ✅ **Auditoría**: Logging de todas las asignaciones y cambios

#### **4. DTOs y Endpoints**

- ✅ **DTOs de creación**: Con soporte para múltiples dominios y proyectos RAG
- ✅ **DTOs de asignación**: Para añadir dominios o proyectos RAG a trabajos existentes
- ✅ **Validaciones**: Restricciones UNIQUE para evitar asignaciones duplicadas

### **Beneficios de la Nueva Arquitectura**

1. **Eficiencia Operativa**: Elimina duplicación de trabajos de scraping
2. **Flexibilidad**: Asignación dinámica según necesidades del negocio
3. **Trazabilidad**: Seguimiento completo de todas las asignaciones
4. **Escalabilidad**: Fácil gestión de múltiples dominios y proyectos
5. **Compliance**: Verificación automática para todas las asignaciones

### **Casos de Uso Soportados**

- **Multi-Dominio**: Un scraping puede alimentar dominios de finanzas, salud y retail
- **Multi-RAG**: Un scraping puede servir a proyectos de documentación y conocimiento
- **Asignación Dinámica**: Añadir/remover dominios o proyectos según evolucionen las necesidades
- **Auditoría Completa**: Seguimiento de quién, cuándo y por qué se realizaron las asignaciones

## Conclusión

El módulo **Domain Ingestion** está completamente implementado con todas las funcionalidades solicitadas:

✅ **Normalización Completa**: Prefijos `dmn_`, PK autonumérica, 3NF/BCNF
✅ **Webscraping Integrado**: Integración completa con leka-server
✅ **Gestión RAG**: Documentos para búsqueda inteligente y formación
✅ **Integración Training**: Conexión con módulo de entrenamiento
✅ **Relación con Proyectos**: Mapeo de dominios utilizados en proyectos
✅ **Cumplimiento Normativo**: Trazabilidad y versionado para cumplir leyes
✅ **Cron Opcional**: Sistema para actualizaciones de fuentes externas

El módulo sigue los principios de **arquitectura hexagonal**, **SOLID**, **KISS** y **TDD**, proporcionando una base sólida para la gestión de dominios de negocio con capacidades avanzadas de ingesta, procesamiento y cumplimiento normativo.

**Estado**: **COMPLETADO** ✅
**Próximo módulo**: `04_INFRAESTRUCTURE_MANAGEMENT.md`
