## Módulo de Project Management - Portal Backend

## Descripción General

El módulo de Project Management es el núcleo central del sistema que gestiona proyectos de desarrollo de IA. Los proyectos controlan licencias, acceso a recursos, tecnologías asociadas, stacks arquitectónicos y endpoints de serving. Es un módulo crítico que afecta a todos los demás módulos funcionales del sistema.

## Arquitectura del Sistema

### Concepto Clave

Los proyectos de IA son el núcleo del negocio y controlan:

- **Licencias del sistema** (archivos encriptados para self-hosting)
- **Acceso a recursos** (modelos, APIs, RAG, generación de apps)
- **Tokens de usuario** para diferentes servicios
- **Integración con IDEs** y herramientas externas
- **Asociación con tecnologías** y stacks arquitectónicos
- **Endpoints de serving** asociados al proyecto

### Flujo de Acceso

```plaintext
Usuario → Login → JWT Principal
    ↓
Verificar Tokens Específicos:
    ↓
├── AI Development Token → Acceso a modelos, playgrounds
├── Code Development Token → Acceso a repositorios, code tools
├── Project Access Token → Acceso a proyectos específicos
└── IDE Integration Token → Acceso a plugins externos
    ↓
Validar Acceso al Proyecto:
    ↓
├── Verificar licencia (archivo encriptado)
├── Verificar límites de uso
├── Verificar tecnologías asociadas
└── Verificar endpoints de serving
```

## Entidades del Sistema

### 1\. Project

```java
@Entity
@Table(name = "prj_projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "project_code")
    private String projectCode; // Código único del proyecto

    @Column(name = "project_type")
    @Enumerated(EnumType.STRING)
    private ProjectType projectType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @Column(name = "owner_id")
    private Long ownerId; // Referencia a cor_users.id

    @Column(name = "client_id")
    private Long clientId; // Referencia a prj_clients.id

    @Column(name = "department_id")
    private Long departmentId; // Referencia a cor_departments.id

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "budget")
    private BigDecimal budget;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relaciones
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<projecttechnology> technologies = new ArrayList&lt;&gt;();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<projectstack> stacks = new ArrayList&lt;&gt;();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<projectendpoint> endpoints = new ArrayList&lt;&gt;();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<projectlicense> licenses = new ArrayList&lt;&gt;();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<userprojecttoken> userTokens = new ArrayList&lt;&gt;();
}

public enum ProjectType {
    AI_DEVELOPMENT,      // Desarrollo de modelos de IA
    RAG_APPLICATION,     // Aplicación RAG
    APP_GENERATION,      // Generación de aplicaciones
    MODEL_SERVING,       // Servicio de modelos
    RESEARCH,            // Proyecto de investigación
    PROOF_OF_CONCEPT,   // POC
    PRODUCTION,          // Aplicación en producción
    DEMO                // Demostración
}

public enum ProjectStatus {
    PLANNING,           // En planificación
    ACTIVE,             // Activo
    ON_HOLD,            // En pausa
    COMPLETED,          // Completado
    CANCELLED,          // Cancelado
    ARCHIVED            // Archivado
}
```

### 2\. ProjectTechnology

```java
@Entity
@Table(name = "prj_project_technologies")
public class ProjectTechnology {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "technology_id", nullable = false)
    private Technology technology; // Referencia a la entidad Technology del módulo Technology Management

    @Column(name = "version")
    private String version;

    @Column(name = "is_required")
    private Boolean isRequired = true;

    @Column(name = "usage_type")
    @Enumerated(EnumType.STRING)
    private TechnologyUsageType usageType;

    @Column(name = "configuration")
    private String configuration; // JSON configuration específica del proyecto

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum TechnologyUsageType {
    PRIMARY,            // Tecnología principal del proyecto
    DEPENDENCY,         // Dependencia requerida
    OPTIONAL,           // Tecnología opcional
    ALTERNATIVE,        // Alternativa disponible
    LEGACY             // Tecnología heredada
}
```

### 3\. ProjectStack

```java
@Entity
@Table(name = "prj_project_stacks")
public class ProjectStack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "stack_id", nullable = false)
    private TechnologyStack stack; // Referencia a la entidad TechnologyStack del módulo Technology Management

    @Column(name = "stack_version")
    private String stackVersion;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "configuration")
    private String configuration; // JSON stack configuration específica del proyecto

    @Column(name = "deployment_config")
    private String deploymentConfig; // JSON deployment config específica del proyecto

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

### 4\. ProjectMember

```java
@Entity
@Table(name = "prj_project_members")
public class ProjectMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private ProjectRole role;

    @Column(name = "permissions")
    private String permissions; // JSON array de permisos

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @Column(name = "left_at")
    private LocalDateTime leftAt;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum ProjectRole {
    OWNER,              // Propietario del proyecto
    PROJECT_MANAGER,    // Gestor del proyecto
    LEAD_DEVELOPER,     // Desarrollador líder
    DEVELOPER,          // Desarrollador
    AI_ENGINEER,        // Ingeniero de IA
    DEVOPS_ENGINEER,    // Ingeniero DevOps
    DATA_SCIENTIST,     // Científico de datos
    TESTER,             // Tester
    VIEWER              // Solo lectura
}
```

### 5\. ProjectLicense

```java
@Entity
@Table(name = "prj_project_licenses")
public class ProjectLicense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "license_type")
    @Enumerated(EnumType.STRING)
    private LicenseType licenseType;

    @Column(name = "license_file")
    private byte[] licenseFile; // Archivo encriptado

    @Column(name = "license_hash")
    private String licenseHash; // Hash del archivo para verificación

    @Column(name = "encryption_key_id")
    private String encryptionKeyId; // ID de la clave de encriptación

    @Column(name = "max_users")
    private Integer maxUsers;

    @Column(name = "max_ai_models")
    private Integer maxAIModels;

    @Column(name = "max_rag_documents")
    private Integer maxRAGDocuments;

    @Column(name = "max_api_calls_per_month")
    private Long maxAPICallsPerMonth;

    @Column(name = "max_storage_gb")
    private Integer maxStorageGB;

    @Column(name = "features_enabled")
    private String featuresEnabled; // JSON array de características

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum LicenseType {
    FREE,               // Licencia gratuita
    BASIC,              // Licencia básica
    PROFESSIONAL,       // Licencia profesional
    ENTERPRISE,         // Licencia empresarial
    CUSTOM              // Licencia personalizada
}
```

### 6\. ProjectToken

```java
@Entity
@Table(name = "prj_project_tokens")
public class ProjectToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "token_type")
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Column(name = "token_value")
    private String tokenValue; // Encriptado

    @Column(name = "scopes")
    private String scopes; // JSON array de permisos

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "last_used")
    private LocalDateTime lastUsed;

    @Column(name = "usage_count")
    private Long usageCount = 0L;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum TokenType {
    AI_DEVELOPMENT,      // Acceso a modelos y playgrounds de IA
    CODE_DEVELOPMENT,    // Acceso a repositorios y herramientas de código
    PROJECT_ACCESS,      // Acceso a proyectos específicos
    IDE_INTEGRATION,     // Para plugins de IDEs externos
    API_ACCESS,          // Acceso a APIs del sistema
    RAG_ACCESS,          // Acceso a documentos RAG
    MODEL_TRAINING,      // Acceso a entrenamiento de modelos
    APP_GENERATION,      // Acceso a generación de aplicaciones
    SERVING_ACCESS,      // Acceso a endpoints de serving
    TECHNOLOGY_ACCESS    // Acceso a tecnologías del proyecto
}
```

### 7\. Client

```java
@Entity
@Table(name = "prj_clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "industry")
    private String industry;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<project> projects = new ArrayList&lt;&gt;();
}
```

### 8\. ProjectRequirement

```java
@Entity
@Table(name = "prj_project_requirements")
public class ProjectRequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "requirement_code")
    private String requirementCode; // Código único del requisito

    @Column(name = "requirement_type")
    @Enumerated(EnumType.STRING)
    private RequirementType requirementType;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private RequirementPriority priority;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RequirementStatus status;

    @Column(name = "acceptance_criteria")
    private String acceptanceCriteria; // JSON con criterios de aceptación

    @Column(name = "business_value")
    private Integer businessValue; // 1-10

    @Column(name = "story_points")
    private Integer storyPoints;

    @Column(name = "assigned_to")
    private Long assignedTo; // Referencia a cor_users.id

    @Column(name = "estimated_hours")
    private BigDecimal estimatedHours;

    @Column(name = "actual_hours")
    private BigDecimal actualHours;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum RequirementType {
    FUNCTIONAL,         // Requisito funcional
    NON_FUNCTIONAL,     // Requisito no funcional
    BUSINESS,           // Requisito de negocio
    USER_STORY,         // Historia de usuario
    EPIC,               // Épica
    FEATURE,            // Característica
    BUG,                // Error
    IMPROVEMENT         // Mejora
}

public enum RequirementPriority {
    CRITICAL,           // Crítico
    HIGH,               // Alto
    MEDIUM,             // Medio
    LOW,                // Bajo
    OPTIONAL            // Opcional
}

public enum RequirementStatus {
    DRAFT,              // Borrador
    REVIEW,             // En revisión
    APPROVED,           // Aprobado
    IN_PROGRESS,        // En progreso
    TESTING,            // En pruebas
    COMPLETED,          // Completado
    REJECTED,           // Rechazado
    CANCELLED           // Cancelado
}
```

### 9\. ProjectDomain

```java
@Entity
@Table(name = "prj_project_domains")
public class ProjectDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "domain_id")
    private Long domainId; // Referencia a dmn_domains.id

    @Column(name = "domain_role")
    @Enumerated(EnumType.STRING)
    private DomainRole domainRole;

    @Column(name = "usage_intensity")
    @Enumerated(EnumType.STRING)
    private UsageIntensity usageIntensity;

    @Column(name = "data_requirements")
    private String dataRequirements; // JSON con requisitos de datos

    @Column(name = "compliance_requirements")
    private String complianceRequirements; // JSON con requisitos de cumplimiento

    @Column(name = "rag_integration")
    private Boolean ragIntegration = false;

    @Column(name = "rag_configuration")
    private String ragConfiguration; // JSON con configuración RAG

    @Column(name = "data_retention_policy")
    private String dataRetentionPolicy; // JSON con política de retención

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum DomainRole {
    PRIMARY,            // Dominio principal del proyecto
    SECONDARY,          // Dominio secundario
    REFERENCE,          // Dominio de referencia
    COMPLIANCE,         // Dominio para cumplimiento
    TRAINING,           // Dominio para entrenamiento
    VALIDATION          // Dominio para validación
}

public enum UsageIntensity {
    HIGH,               // Uso intensivo
    MEDIUM,             // Uso moderado
    LOW,                // Uso bajo
    OCCASIONAL,         // Uso ocasional
    REFERENCE_ONLY      // Solo referencia
}
```

### 10\. ProjectVersion

```java
@Entity
@Table(name = "prj_project_versions")
public class ProjectVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "version_number")
    private String versionNumber; // SemVer: 1.0.0

    @Column(name = "version_type")
    @Enumerated(EnumType.STRING)
    private VersionType versionType;

    @Column(name = "release_notes")
    private String releaseNotes;

    @Column(name = "changelog")
    private String changelog; // JSON con cambios detallados

    @Column(name = "deployment_status")
    @Enumerated(EnumType.STRING)
    private DeploymentStatus deploymentStatus;

    @Column(name = "deployed_at")
    private LocalDateTime deployedAt;

    @Column(name = "deployed_by")
    private Long deployedBy; // Referencia a cor_users.id

    @Column(name = "rollback_available")
    private Boolean rollbackAvailable = false;

    @Column(name = "rollback_instructions")
    private String rollbackInstructions;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum VersionType {
    MAJOR,              // Cambio mayor (breaking changes)
    MINOR,              // Nueva funcionalidad
    PATCH,              // Corrección de errores
    PRE_RELEASE,        // Pre-release
    RELEASE_CANDIDATE   // Candidato a release
}

public enum DeploymentStatus {
    PENDING,            // Pendiente de despliegue
    IN_PROGRESS,        // En proceso de despliegue
    DEPLOYED,           // Desplegado
    FAILED,             // Falló el despliegue
    ROLLED_BACK,        // Revertido
    ARCHIVED            // Archivado
}
```

### 11\. ProjectDocument

```java
@Entity
@Table(name = "prj_project_documents")
public class ProjectDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "document_type")
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content; // Contenido del documento

    @Column(name = "content_hash")
    private String contentHash; // Hash del contenido para RAG

    @Column(name = "file_path")
    private String filePath; // Ruta al archivo si existe

    @Column(name = "file_size")
    private Long fileSize; // Tamaño en bytes

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "version")
    private String version;

    @Column(name = "author_id")
    private Long authorId; // Referencia a cor_users.id

    @Column(name = "reviewer_id")
    private Long reviewerId; // Referencia a cor_users.id

    @Column(name = "review_status")
    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;

    @Column(name = "review_notes")
    private String reviewNotes;

    @Column(name = "is_rag_indexed")
    private Boolean isRagIndexed = false;

    @Column(name = "rag_indexed_at")
    private LocalDateTime ragIndexedAt;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum DocumentType {
    TECHNICAL_SPEC,     // Especificación técnica
    FUNCTIONAL_SPEC,    // Especificación funcional
    REQUIREMENTS,       // Documento de requisitos
    ARCHITECTURE,       // Documento de arquitectura
    API_DOCS,           // Documentación de API
    USER_MANUAL,        // Manual de usuario
    DEPLOYMENT_GUIDE,   // Guía de despliegue
    TEST_PLAN,          // Plan de pruebas
    CHANGELOG,          // Registro de cambios
    MEETING_NOTES,      // Notas de reunión
    DECISION_LOG        // Registro de decisiones
}

public enum ReviewStatus {
    DRAFT,              // Borrador
    IN_REVIEW,          // En revisión
    APPROVED,           // Aprobado
    REJECTED,           // Rechazado
    NEEDS_REVISION      // Necesita revisión
}
```

### 12\. ProjectTimeTracking

```java
@Entity
@Table(name = "prj_project_time_tracking")
public class ProjectTimeTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "user_id")
    private Long userId; // Referencia a cor_users.id

    @Column(name = "requirement_id")
    private Long requirementId; // Referencia a prj_project_requirements.id

    @Column(name = "activity_type")
    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

    @Column(name = "description")
    private String description;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "duration_hours")
    private BigDecimal durationHours;

    @Column(name = "hourly_rate")
    private BigDecimal hourlyRate;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Column(name = "is_billable")
    private Boolean isBillable = true;

    @Column(name = "billing_status")
    @Enumerated(EnumType.STRING)
    private BillingStatus billingStatus;

    @Column(name = "notes")
    private String notes;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum ActivityType {
    DEVELOPMENT,         // Desarrollo
    TESTING,             // Pruebas
    DESIGN,              // Diseño
    ANALYSIS,            // Análisis
    MEETING,             // Reunión
    DOCUMENTATION,       // Documentación
    DEPLOYMENT,          // Despliegue
    SUPPORT,             // Soporte
    TRAINING,            // Entrenamiento
    RESEARCH             // Investigación
}

public enum BillingStatus {
    PENDING,             // Pendiente de facturación
    BILLED,              // Facturado
    PAID,                // Pagado
    WRITTEN_OFF,         // Cancelado
    DISPUTED             // Disputado
}
```

### 13\. ProjectTask

```java
@Entity
@Table(name = "prj_project_tasks")
public class ProjectTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "requirement_id")
    private Long requirementId; // Referencia a prj_project_requirements.id

    @Column(name = "parent_task_id")
    private Long parentTaskId; // Para tareas subtareas

    @Column(name = "task_code")
    private String taskCode; // Código único de la tarea

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "task_type")
    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(name = "assigned_to")
    private Long assignedTo; // Referencia a cor_users.id

    @Column(name = "reporter_id")
    private Long reporterId; // Referencia a cor_users.id

    @Column(name = "estimated_hours")
    private BigDecimal estimatedHours;

    @Column(name = "actual_hours")
    private BigDecimal actualHours;

    @Column(name = "story_points")
    private Integer storyPoints;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "completed_date")
    private LocalDate completedDate;

    @Column(name = "progress_percentage")
    private Integer progressPercentage; // 0-100

    @Column(name = "dependencies")
    private String dependencies; // JSON con IDs de tareas dependientes

    @Column(name = "tags")
    private String tags; // JSON con tags de la tarea

    @Column(name = "attachments")
    private String attachments; // JSON con archivos adjuntos

    @Column(name = "comments")
    private String comments; // JSON con comentarios

    @Column(name = "time_logs")
    private String timeLogs; // JSON con registros de tiempo

    @Column(name = "is_blocked")
    private Boolean isBlocked = false;

    @Column(name = "block_reason")
    private String blockReason;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum TaskType {
    FEATURE,            // Nueva funcionalidad
    BUG,                // Corrección de error
    IMPROVEMENT,        // Mejora
    TASK,               // Tarea general
    STORY,              // Historia de usuario
    EPIC,               // Épica
    SUBTASK,            // Subtarea
    RESEARCH,           // Investigación
    DOCUMENTATION,      // Documentación
    TESTING,            // Pruebas
    DEPLOYMENT,         // Despliegue
    SUPPORT             // Soporte
}

public enum TaskPriority {
    CRITICAL,           // Crítico
    HIGH,               // Alto
    MEDIUM,             // Medio
    LOW,                // Bajo
    OPTIONAL            // Opcional
}

public enum TaskStatus {
    BACKLOG,            // En backlog
    TO_DO,              // Por hacer
    IN_PROGRESS,        // En progreso
    IN_REVIEW,          // En revisión
    TESTING,            // En pruebas
    DONE,               // Completada
    CANCELLED,          // Cancelada
    BLOCKED,            // Bloqueada
    ON_HOLD             // En espera
}
```

### 14\. ProjectTaskComment

```java
@Entity
@Table(name = "prj_project_task_comments")
public class ProjectTaskComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_id")
    private Long taskId; // Referencia a prj_project_tasks.id

    @Column(name = "user_id")
    private Long userId; // Referencia a cor_users.id

    @Column(name = "comment")
    private String comment;

    @Column(name = "comment_type")
    @Enumerated(EnumType.STRING)
    private CommentType commentType;

    @Column(name = "is_internal")
    private Boolean isInternal = false; // Comentario interno del equipo

    @Column(name = "mentions")
    private String mentions; // JSON con usuarios mencionados

    @Column(name = "attachments")
    private String attachments; // JSON con archivos adjuntos

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum CommentType {
    GENERAL,            // Comentario general
    STATUS_UPDATE,      // Actualización de estado
    BLOCK_REASON,       // Razón de bloqueo
    SOLUTION,           // Solución propuesta
    QUESTION,           // Pregunta
    FEEDBACK,           // Feedback
    APPROVAL,           // Aprobación
    REJECTION           // Rechazo
}
```

### 15\. ProjectTaskTimeLog

```java
@Entity
@Table(name = "prj_project_task_time_logs")
public class ProjectTaskTimeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_id")
    private Long taskId; // Referencia a prj_project_tasks.id

    @Column(name = "user_id")
    private Long userId; // Referencia a cor_users.id

    @Column(name = "activity_type")
    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

    @Column(name = "description")
    private String description;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "duration_hours")
    private BigDecimal durationHours;

    @Column(name = "is_billable")
    private Boolean isBillable = true;

    @Column(name = "notes")
    private String notes;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

## API Endpoints

### Project Management

```plaintext
GET    /api/v1/projects
GET    /api/v1/projects/{id}
POST   /api/v1/projects
PUT    /api/v1/projects/{id}
DELETE /api/v1/projects/{id}
GET    /api/v1/projects/user/{userId}
GET    /api/v1/projects/client/{clientId}
GET    /api/v1/projects/department/{departmentId}
POST   /api/v1/projects/{id}/activate
POST   /api/v1/projects/{id}/deactivate
```

### Project Technologies

```plaintext
GET    /api/v1/projects/{id}/technologies
GET    /api/v1/projects/{id}/technologies/{techId}
POST   /api/v1/projects/{id}/technologies
PUT    /api/v1/projects/{id}/technologies/{techId}
DELETE /api/v1/projects/{id}/technologies/{techId}
GET    /api/v1/projects/{id}/technologies/search
```

### Project Stacks

```plaintext
GET    /api/v1/projects/{id}/stacks
GET    /api/v1/projects/{id}/stacks/{stackId}
POST   /api/v1/projects/{id}/stacks
PUT    /api/v1/projects/{id}/stacks/{stackId}
DELETE /api/v1/projects/{id}/stacks/{stackId}
POST   /api/v1/projects/{id}/stacks/{stackId}/deploy
```

### Project Members

```plaintext
GET    /api/v1/projects/{id}/members
GET    /api/v1/projects/{id}/members/{memberId}
POST   /api/v1/projects/{id}/members
PUT    /api/v1/projects/{id}/members/{memberId}
DELETE /api/v1/projects/{id}/members/{memberId}
POST   /api/v1/projects/{id}/members/{memberId}/activate
POST   /api/v1/projects/{id}/members/{memberId}/deactivate
```

### Project Licenses

```plaintext
GET    /api/v1/projects/{id}/licenses
GET    /api/v1/projects/{id}/licenses/{licenseId}
POST   /api/v1/projects/{id}/licenses
PUT    /api/v1/projects/{id}/licenses/{licenseId}
DELETE /api/v1/projects/{id}/licenses/{licenseId}
POST   /api/v1/projects/{id}/licenses/{licenseId}/validate
POST   /api/v1/projects/{id}/licenses/{licenseId}/renew
```

### Project Tokens

```plaintext
GET    /api/v1/projects/{id}/tokens
GET    /api/v1/projects/{id}/tokens/{tokenId}
POST   /api/v1/projects/{id}/tokens
PUT    /api/v1/projects/{id}/tokens/{tokenId}
DELETE /api/v1/projects/{id}/tokens/{tokenId}
POST   /api/v1/projects/{id}/tokens/{tokenId}/regenerate
POST   /api/v1/projects/{id}/tokens/{tokenId}/revoke
```

### Project Requirements

```plaintext
GET    /api/v1/projects/{id}/requirements
GET    /api/v1/projects/{id}/requirements/{reqId}
POST   /api/v1/projects/{id}/requirements
PUT    /api/v1/projects/{id}/requirements/{reqId}
DELETE /api/v1/projects/{id}/requirements/{reqId}
GET    /api/v1/projects/{id}/requirements/search
POST   /api/v1/projects/{id}/requirements/{reqId}/assign
POST   /api/v1/projects/{id}/requirements/{reqId}/approve
POST   /api/v1/projects/{id}/requirements/{reqId}/reject
```

### Project Domains

```plaintext
GET    /api/v1/projects/{id}/domains
GET    /api/v1/projects/{id}/domains/{domainId}
POST   /api/v1/projects/{id}/domains
PUT    /api/v1/projects/{id}/domains/{domainId}
DELETE /api/v1/projects/{id}/domains/{domainId}
GET    /api/v1/projects/{id}/domains/rag-enabled
POST   /api/v1/projects/{id}/domains/{domainId}/rag-configure
POST   /api/v1/projects/{id}/domains/{domainId}/compliance-check
```

### Project Versions

```plaintext
GET    /api/v1/projects/{id}/versions
GET    /api/v1/projects/{id}/versions/{versionId}
POST   /api/v1/projects/{id}/versions
PUT    /api/v1/projects/{id}/versions/{versionId}
DELETE /api/v1/projects/{id}/versions/{versionId}
POST   /api/v1/projects/{id}/versions/{versionId}/deploy
POST   /api/v1/projects/{id}/versions/{versionId}/rollback
GET    /api/v1/projects/{id}/versions/latest
GET    /api/v1/projects/{id}/versions/stable
```

### Project Documents

```plaintext
GET    /api/v1/projects/{id}/documents
GET    /api/v1/projects/{id}/documents/{docId}
POST   /api/v1/projects/{id}/documents
PUT    /api/v1/projects/{id}/documents/{docId}
DELETE /api/v1/projects/{id}/documents/{docId}
GET    /api/v1/projects/{id}/documents/search
POST   /api/v1/projects/{id}/documents/{docId}/review
POST   /api/v1/projects/{id}/documents/{docId}/approve
POST   /api/v1/projects/{id}/documents/{docId}/rag-index
GET    /api/v1/projects/{id}/documents/rag-search
```

### Project Time Tracking

```plaintext
GET    /api/v1/projects/{id}/time-tracking
GET    /api/v1/projects/{id}/time-tracking/{trackingId}
POST   /api/v1/projects/{id}/time-tracking
PUT    /api/v1/projects/{id}/time-tracking/{trackingId}
DELETE /api/v1/projects/{id}/time-tracking/{trackingId}
GET    /api/v1/projects/{id}/time-tracking/user/{userId}
GET    /api/v1/projects/{id}/time-tracking/requirement/{reqId}
GET    /api/v1/projects/{id}/time-tracking/summary
POST   /api/v1/projects/{id}/time-tracking/bulk-import
GET    /api/v1/projects/{id}/time-tracking/reports
```

### AI-Powered Project Generation

```plaintext
POST   /api/v1/projects/{id}/generate-requirements
POST   /api/v1/projects/{id}/generate-architecture
POST   /api/v1/projects/{id}/generate-documentation
POST   /api/v1/projects/{id}/generate-test-cases
POST   /api/v1/projects/{id}/generate-deployment-config
POST   /api/v1/projects/{id}/generate-api-specs
POST   /api/v1/projects/{id}/generate-user-stories
POST   /api/v1/projects/{id}/generate-acceptance-criteria
```

### RAG Integration

```plaintext
GET    /api/v1/projects/{id}/rag/search
GET    /api/v1/projects/{id}/rag/suggestions
GET    /api/v1/projects/{id}/rag/context
POST   /api/v1/projects/{id}/rag/query
POST   /api/v1/projects/{id}/rag/feedback
GET    /api/v1/projects/{id}/rag/history
POST   /api/v1/projects/{id}/rag/reindex
```

### Project Task Management

```plaintext
GET    /api/v1/projects/{id}/tasks
GET    /api/v1/projects/{id}/tasks/{taskId}
POST   /api/v1/projects/{id}/tasks
PUT    /api/v1/projects/{id}/tasks/{taskId}
DELETE /api/v1/projects/{id}/tasks/{taskId}
GET    /api/v1/projects/{id}/tasks/search
GET    /api/v1/projects/{id}/tasks/assigned/{userId}
GET    /api/v1/projects/{id}/tasks/status/{status}
GET    /api/v1/projects/{id}/tasks/priority/{priority}
POST   /api/v1/projects/{id}/tasks/{taskId}/assign
POST   /api/v1/projects/{id}/tasks/{taskId}/status
POST   /api/v1/projects/{id}/tasks/{taskId}/priority
POST   /api/v1/projects/{id}/tasks/{taskId}/progress
POST   /api/v1/projects/{id}/tasks/{taskId}/block
POST   /api/v1/projects/{id}/tasks/{taskId}/unblock
POST   /api/v1/projects/{id}/tasks/{taskId}/dependencies
GET    /api/v1/projects/{id}/tasks/backlog
GET    /api/v1/projects/{id}/tasks/sprint
GET    /api/v1/projects/{id}/tasks/burndown
GET    /api/v1/projects/{id}/tasks/velocity
```

### Project Task Comments

```plaintext
GET    /api/v1/projects/{id}/tasks/{taskId}/comments
GET    /api/v1/projects/{id}/tasks/{taskId}/comments/{commentId}
POST   /api/v1/projects/{id}/tasks/{taskId}/comments
PUT    /api/v1/projects/{id}/tasks/{taskId}/comments/{commentId}
DELETE /api/v1/projects/{id}/tasks/{taskId}/comments/{commentId}
POST   /api/v1/projects/{id}/tasks/{taskId}/comments/{commentId}/mentions
```

### Project Task Time Logs

```plaintext
GET    /api/v1/projects/{id}/tasks/{taskId}/time-logs
GET    /api/v1/projects/{id}/tasks/{taskId}/time-logs/{logId}
POST   /api/v1/projects/{id}/tasks/{taskId}/time-logs
PUT    /api/v1/projects/{id}/tasks/{taskId}/time-logs/{logId}
DELETE /api/v1/projects/{id}/tasks/{taskId}/time-logs/{logId}
GET    /api/v1/projects/{id}/tasks/{taskId}/time-summary
POST   /api/v1/projects/{id}/tasks/{taskId}/time-logs/bulk
```

### Project Task Analytics

```plaintext
GET    /api/v1/projects/{id}/tasks/analytics/overview
GET    /api/v1/projects/{id}/tasks/analytics/velocity
GET    /api/v1/projects/{id}/tasks/analytics/burndown
GET    /api/v1/projects/{id}/tasks/analytics/cycle-time
GET    /api/v1/projects/{id}/tasks/analytics/lead-time
GET    /api/v1/projects/{id}/tasks/analytics/team-performance
GET    /api/v1/projects/{id}/tasks/analytics/priority-distribution
GET    /api/v1/projects/{id}/tasks/analytics/status-distribution
GET    /api/v1/projects/{id}/tasks/analytics/assignee-workload
GET    /api/v1/projects/{id}/tasks/analytics/blocked-tasks
```

### Client Management

```plaintext
GET    /api/v1/clients
GET    /api/v1/clients/{id}
POST   /api/v1/clients
PUT    /api/v1/clients/{id}
DELETE /api/v1/clients/{id}
GET    /api/v1/clients/{id}/projects
```

## Scripts de Base de Datos

### Script de Creación de Tablas

```plaintext
-- Tabla de clientes
CREATE TABLE prj_clients (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    company_name VARCHAR(255),
    contact_email VARCHAR(255),
    contact_phone VARCHAR(50),
    industry VARCHAR(100),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla prj_clients
COMMENT ON TABLE prj_clients IS 'Tabla que almacena la información de los clientes de la organización';
COMMENT ON COLUMN prj_clients.id IS 'Identificador único del cliente';
COMMENT ON COLUMN prj_clients.name IS 'Nombre del cliente';
COMMENT ON COLUMN prj_clients.description IS 'Descripción del cliente';
COMMENT ON COLUMN prj_clients.company_name IS 'Nombre de la empresa del cliente';
COMMENT ON COLUMN prj_clients.contact_email IS 'Email de contacto del cliente';
COMMENT ON COLUMN prj_clients.contact_phone IS 'Teléfono de contacto del cliente';
COMMENT ON COLUMN prj_clients.industry IS 'Industria del cliente';
COMMENT ON COLUMN prj_clients.is_active IS 'Indica si el cliente está activo';
COMMENT ON COLUMN prj_clients.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN prj_clients.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de proyectos
CREATE TABLE prj_projects (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    project_code VARCHAR(100) UNIQUE NOT NULL,
    project_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) DEFAULT 'PLANNING',
    owner_id BIGINT REFERENCES cor_users(id),
    client_id BIGINT REFERENCES prj_clients(id),
    department_id BIGINT REFERENCES cor_departments(id),
    start_date DATE,
    end_date DATE,
    budget DECIMAL(15,2),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla prj_projects
COMMENT ON TABLE prj_projects IS 'Tabla principal que almacena la información de los proyectos de IA';
COMMENT ON COLUMN prj_projects.id IS 'Identificador único del proyecto';
COMMENT ON COLUMN prj_projects.name IS 'Nombre del proyecto';
COMMENT ON COLUMN prj_projects.description IS 'Descripción detallada del proyecto';
COMMENT ON COLUMN prj_projects.project_code IS 'Código único del proyecto para identificación interna';
COMMENT ON COLUMN prj_projects.project_type IS 'Tipo de proyecto (AI_DEVELOPMENT, RAG_APPLICATION, etc.)';
COMMENT ON COLUMN prj_projects.status IS 'Estado actual del proyecto';
COMMENT ON COLUMN prj_projects.owner_id IS 'ID del usuario propietario del proyecto';
COMMENT ON COLUMN prj_projects.client_id IS 'ID del cliente asociado al proyecto';
COMMENT ON COLUMN prj_projects.department_id IS 'ID del departamento responsable del proyecto';
COMMENT ON COLUMN prj_projects.start_date IS 'Fecha de inicio del proyecto';
COMMENT ON COLUMN prj_projects.end_date IS 'Fecha de finalización del proyecto';
COMMENT ON COLUMN prj_projects.budget IS 'Presupuesto asignado al proyecto';
COMMENT ON COLUMN prj_projects.is_active IS 'Indica si el proyecto está activo';
COMMENT ON COLUMN prj_projects.created_at IS 'Fecha y hora de creación del proyecto';
COMMENT ON COLUMN prj_projects.updated_at IS 'Fecha y hora de la última actualización del proyecto';

-- Tabla de tecnologías del proyecto
CREATE TABLE prj_project_technologies (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT REFERENCES prj_projects(id) ON DELETE CASCADE,
    technology_id BIGINT REFERENCES technologies(id),
    version VARCHAR(50),
    is_required BOOLEAN DEFAULT true,
    usage_type VARCHAR(50) NOT NULL,
    configuration TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla prj_project_technologies
COMMENT ON TABLE prj_project_technologies IS 'Tabla que relaciona proyectos con tecnologías específicas';
COMMENT ON COLUMN prj_project_technologies.id IS 'Identificador único de la relación proyecto-tecnología';
COMMENT ON COLUMN prj_project_technologies.project_id IS 'ID del proyecto';
COMMENT ON COLUMN prj_project_technologies.technology_id IS 'ID de la tecnología';
COMMENT ON COLUMN prj_project_technologies.version IS 'Versión de la tecnología utilizada';
COMMENT ON COLUMN prj_project_technologies.is_required IS 'Indica si la tecnología es requerida para el proyecto';
COMMENT ON COLUMN prj_project_technologies.usage_type IS 'Tipo de uso de la tecnología en el proyecto';
COMMENT ON COLUMN prj_project_technologies.configuration IS 'Configuración específica de la tecnología en formato JSON';
COMMENT ON COLUMN prj_project_technologies.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN prj_project_technologies.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de stacks del proyecto
CREATE TABLE prj_project_stacks (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT REFERENCES prj_projects(id) ON DELETE CASCADE,
    stack_id BIGINT REFERENCES technology_stacks(id),
    stack_version VARCHAR(50),
    is_active BOOLEAN DEFAULT true,
    configuration TEXT,
    deployment_config TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla prj_project_stacks
COMMENT ON TABLE prj_project_stacks IS 'Tabla que relaciona proyectos con stacks tecnológicos';
COMMENT ON COLUMN prj_project_stacks.id IS 'Identificador único de la relación proyecto-stack';
COMMENT ON COLUMN prj_project_stacks.project_id IS 'ID del proyecto';
COMMENT ON COLUMN prj_project_stacks.stack_id IS 'ID del stack tecnológico';
COMMENT ON COLUMN prj_project_stacks.stack_version IS 'Versión del stack utilizada';
COMMENT ON COLUMN prj_project_stacks.is_active IS 'Indica si el stack está activo en el proyecto';
COMMENT ON COLUMN prj_project_stacks.configuration IS 'Configuración del stack en formato JSON';
COMMENT ON COLUMN prj_project_stacks.deployment_config IS 'Configuración de despliegue del stack en formato JSON';
COMMENT ON COLUMN prj_project_stacks.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN prj_project_stacks.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de miembros del proyecto
CREATE TABLE prj_project_members (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT REFERENCES prj_projects(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES cor_users(id),
    role VARCHAR(50) NOT NULL,
    permissions TEXT,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    left_at TIMESTAMP,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla prj_project_members
COMMENT ON TABLE prj_project_members IS 'Tabla que gestiona los miembros y roles de los proyectos';
COMMENT ON COLUMN prj_project_members.id IS 'Identificador único del miembro del proyecto';
COMMENT ON COLUMN prj_project_members.project_id IS 'ID del proyecto';
COMMENT ON COLUMN prj_project_members.user_id IS 'ID del usuario miembro';
COMMENT ON COLUMN prj_project_members.role IS 'Rol del usuario en el proyecto';
COMMENT ON COLUMN prj_project_members.permissions IS 'Permisos específicos del usuario en formato JSON';
COMMENT ON COLUMN prj_project_members.joined_at IS 'Fecha y hora de incorporación al proyecto';
COMMENT ON COLUMN prj_project_members.left_at IS 'Fecha y hora de salida del proyecto';
COMMENT ON COLUMN prj_project_members.is_active IS 'Indica si el miembro está activo en el proyecto';
COMMENT ON COLUMN prj_project_members.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN prj_project_members.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de licencias del proyecto
CREATE TABLE prj_project_licenses (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT REFERENCES prj_projects(id) ON DELETE CASCADE,
    license_type VARCHAR(50) NOT NULL,
    license_file BYTEA NOT NULL,
    license_hash VARCHAR(64) NOT NULL,
    encryption_key_id VARCHAR(100) NOT NULL,
    max_users INTEGER,
    max_ai_models INTEGER,
    max_rag_documents INTEGER,
    max_api_calls_per_month BIGINT,
    max_storage_gb INTEGER,
    features_enabled TEXT,
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla prj_project_licenses
COMMENT ON TABLE prj_project_licenses IS 'Tabla que almacena las licencias encriptadas de los proyectos';
COMMENT ON COLUMN prj_project_licenses.id IS 'Identificador único de la licencia';
COMMENT ON COLUMN prj_project_licenses.project_id IS 'ID del proyecto asociado';
COMMENT ON COLUMN prj_project_licenses.license_type IS 'Tipo de licencia (FREE, BASIC, PROFESSIONAL, etc.)';
COMMENT ON COLUMN prj_project_licenses.license_file IS 'Archivo de licencia encriptado';
COMMENT ON COLUMN prj_project_licenses.license_hash IS 'Hash del archivo de licencia para verificación';
COMMENT ON COLUMN prj_project_licenses.encryption_key_id IS 'ID de la clave de encriptación utilizada';
COMMENT ON COLUMN prj_project_licenses.max_users IS 'Número máximo de usuarios permitidos';
COMMENT ON COLUMN prj_project_licenses.max_ai_models IS 'Número máximo de modelos de IA permitidos';
COMMENT ON COLUMN prj_project_licenses.max_rag_documents IS 'Número máximo de documentos RAG permitidos';
COMMENT ON COLUMN prj_project_licenses.max_api_calls_per_month IS 'Número máximo de llamadas API por mes';
COMMENT ON COLUMN prj_project_licenses.max_storage_gb IS 'Almacenamiento máximo en GB';
COMMENT ON COLUMN prj_project_licenses.features_enabled IS 'Características habilitadas en formato JSON';
COMMENT ON COLUMN prj_project_licenses.issued_at IS 'Fecha y hora de emisión de la licencia';
COMMENT ON COLUMN prj_project_licenses.expires_at IS 'Fecha y hora de expiración de la licencia';
COMMENT ON COLUMN prj_project_licenses.is_active IS 'Indica si la licencia está activa';
COMMENT ON COLUMN prj_project_licenses.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN prj_project_licenses.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de tokens del proyecto
CREATE TABLE prj_project_tokens (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT REFERENCES prj_projects(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES cor_users(id),
    token_type VARCHAR(50) NOT NULL,
    token_value TEXT NOT NULL,
    scopes TEXT,
    expires_at TIMESTAMP,
    is_active BOOLEAN DEFAULT true,
    last_used TIMESTAMP,
    usage_count BIGINT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla prj_project_tokens
COMMENT ON TABLE prj_project_tokens IS 'Tabla que gestiona los tokens de acceso específicos de los proyectos';
COMMENT ON COLUMN prj_project_tokens.id IS 'Identificador único del token';
COMMENT ON COLUMN prj_project_tokens.project_id IS 'ID del proyecto asociado';
COMMENT ON COLUMN prj_project_tokens.user_id IS 'ID del usuario propietario del token';
COMMENT ON COLUMN prj_project_tokens.token_type IS 'Tipo de token (AI_DEVELOPMENT, CODE_DEVELOPMENT, etc.)';
COMMENT ON COLUMN prj_project_tokens.token_value IS 'Valor del token encriptado';
COMMENT ON COLUMN prj_project_tokens.scopes IS 'Alcance de permisos del token en formato JSON';
COMMENT ON COLUMN prj_project_tokens.expires_at IS 'Fecha y hora de expiración del token';
COMMENT ON COLUMN prj_project_tokens.is_active IS 'Indica si el token está activo';
COMMENT ON COLUMN prj_project_tokens.last_used IS 'Fecha y hora del último uso del token';
COMMENT ON COLUMN prj_project_tokens.usage_count IS 'Contador de usos del token';
COMMENT ON COLUMN prj_project_tokens.created_at IS 'Fecha y hora de creación del token';
COMMENT ON COLUMN prj_project_tokens.updated_at IS 'Fecha y hora de la última actualización del token';

-- Índices para optimización
CREATE INDEX idx_prj_projects_code ON prj_projects(project_code);
CREATE INDEX idx_prj_projects_type ON prj_projects(project_type);
CREATE INDEX idx_prj_projects_status ON prj_projects(status);
CREATE INDEX idx_prj_projects_owner_id ON prj_projects(owner_id);
CREATE INDEX idx_prj_projects_client_id ON prj_projects(client_id);
CREATE INDEX idx_prj_projects_department_id ON prj_projects(department_id);
CREATE INDEX idx_prj_projects_active ON prj_projects(is_active);
CREATE INDEX idx_prj_project_technologies_project_id ON prj_project_technologies(project_id);
CREATE INDEX idx_prj_project_technologies_technology_id ON prj_project_technologies(technology_id);
CREATE INDEX idx_prj_project_stacks_project_id ON prj_project_stacks(project_id);
CREATE INDEX idx_prj_project_stacks_stack_id ON prj_project_stacks(stack_id);
CREATE INDEX idx_prj_project_members_project_id ON prj_project_members(project_id);
CREATE INDEX idx_prj_project_members_user_id ON prj_project_members(user_id);
CREATE INDEX idx_prj_project_members_role ON prj_project_members(role);
CREATE INDEX idx_prj_project_licenses_project_id ON prj_project_licenses(project_id);
CREATE INDEX idx_prj_project_licenses_type ON prj_project_licenses(license_type);
CREATE INDEX idx_prj_project_licenses_active ON prj_project_licenses(is_active);
CREATE INDEX idx_prj_project_tokens_project_id ON prj_project_tokens(project_id);
CREATE INDEX idx_prj_project_tokens_user_id ON prj_project_tokens(user_id);
CREATE INDEX idx_prj_project_tokens_type ON prj_project_tokens(token_type);
CREATE INDEX idx_prj_project_tokens_active ON prj_project_tokens(is_active);
CREATE INDEX idx_prj_clients_name ON prj_clients(name);
CREATE INDEX idx_prj_clients_active ON prj_clients(is_active);
```

## Scripts SQL

### 1. Script DROP (Eliminar tablas existentes)

```sql
-- Eliminar triggers primero
DROP TRIGGER IF EXISTS project_audit_trigger ON prj_projects;
DROP TRIGGER IF EXISTS project_role_audit_trigger ON prj_project_roles;
DROP TRIGGER IF EXISTS project_member_audit_trigger ON prj_project_members;
DROP TRIGGER IF EXISTS project_token_audit_trigger ON prj_project_tokens;
DROP TRIGGER IF EXISTS project_technology_audit_trigger ON prj_project_technologies;
DROP TRIGGER IF EXISTS project_requirement_audit_trigger ON prj_project_requirements;
DROP TRIGGER IF EXISTS project_domain_audit_trigger ON prj_project_domains;
DROP TRIGGER IF EXISTS project_version_audit_trigger ON prj_project_versions;
DROP TRIGGER IF EXISTS project_document_audit_trigger ON prj_project_documents;
DROP TRIGGER IF EXISTS project_time_tracking_audit_trigger ON prj_project_time_tracking;
DROP TRIGGER IF EXISTS project_task_audit_trigger ON prj_project_tasks;
DROP TRIGGER IF EXISTS project_task_comment_audit_trigger ON prj_project_task_comments;
DROP TRIGGER IF EXISTS project_task_time_log_audit_trigger ON prj_project_task_time_logs;

-- Eliminar índices
DROP INDEX IF EXISTS idx_prj_projects_owner_id;
DROP INDEX IF EXISTS idx_prj_projects_client_id;
DROP INDEX IF EXISTS idx_prj_projects_department_id;
DROP INDEX IF EXISTS idx_prj_projects_status;
DROP INDEX IF EXISTS idx_prj_projects_type;
DROP INDEX IF EXISTS idx_prj_projects_code;

DROP INDEX IF EXISTS idx_prj_project_roles_project_id;
DROP INDEX IF EXISTS idx_prj_project_roles_user_id;
DROP INDEX IF EXISTS idx_prj_project_roles_role;

DROP INDEX IF EXISTS idx_prj_project_members_project_id;
DROP INDEX IF EXISTS idx_prj_project_members_user_id;
DROP INDEX IF EXISTS idx_prj_project_members_role;

DROP INDEX IF EXISTS idx_prj_project_tokens_project_id;
DROP INDEX IF EXISTS idx_prj_project_tokens_user_id;
DROP INDEX IF EXISTS idx_prj_project_tokens_type;

DROP INDEX IF EXISTS idx_prj_project_technologies_project_id;
DROP INDEX IF EXISTS idx_prj_project_technologies_technology_id;

DROP INDEX IF EXISTS idx_prj_project_requirements_project_id;
DROP INDEX IF EXISTS idx_prj_project_requirements_type;
DROP INDEX IF EXISTS idx_prj_project_requirements_priority;
DROP INDEX IF EXISTS idx_prj_project_requirements_status;
DROP INDEX IF EXISTS idx_prj_project_requirements_assigned_to;

DROP INDEX IF EXISTS idx_prj_project_domains_project_id;
DROP INDEX IF EXISTS idx_prj_project_domains_domain_id;
DROP INDEX IF EXISTS idx_prj_project_domains_role;

DROP INDEX IF EXISTS idx_prj_project_versions_project_id;
DROP INDEX IF EXISTS idx_prj_project_versions_version_number;
DROP INDEX IF EXISTS idx_prj_project_versions_type;
DROP INDEX IF EXISTS idx_prj_project_versions_deployment_status;

DROP INDEX IF EXISTS idx_prj_project_documents_project_id;
DROP INDEX IF EXISTS idx_prj_project_documents_type;
DROP INDEX IF EXISTS idx_prj_project_documents_author_id;
DROP INDEX IF EXISTS idx_prj_project_documents_review_status;

DROP INDEX IF EXISTS idx_prj_project_time_tracking_project_id;
DROP INDEX IF EXISTS idx_prj_project_time_tracking_user_id;
DROP INDEX IF EXISTS idx_prj_project_time_tracking_requirement_id;
DROP INDEX IF EXISTS idx_prj_project_time_tracking_activity_type;

DROP INDEX IF EXISTS idx_prj_project_tasks_project_id;
DROP INDEX IF EXISTS idx_prj_project_tasks_requirement_id;
DROP INDEX IF EXISTS idx_prj_project_tasks_assigned_to;
DROP INDEX IF EXISTS idx_prj_project_tasks_status;
DROP INDEX IF EXISTS idx_prj_project_tasks_priority;
DROP INDEX IF EXISTS idx_prj_project_tasks_type;

DROP INDEX IF EXISTS idx_prj_project_task_comments_task_id;
DROP INDEX IF EXISTS idx_prj_project_task_comments_user_id;
DROP INDEX IF EXISTS idx_prj_project_task_comments_type;

DROP INDEX IF EXISTS idx_prj_project_task_time_logs_task_id;
DROP INDEX IF EXISTS idx_prj_project_task_time_logs_user_id;
DROP INDEX IF EXISTS idx_prj_project_task_time_logs_activity_type;

-- Eliminar tablas en orden de dependencias
DROP TABLE IF EXISTS prj_project_task_time_logs;
DROP TABLE IF EXISTS prj_project_task_comments;
DROP TABLE IF EXISTS prj_project_tasks;
DROP TABLE IF EXISTS prj_project_time_tracking;
DROP TABLE IF EXISTS prj_project_documents;
DROP TABLE IF EXISTS prj_project_versions;
DROP TABLE IF EXISTS prj_project_domains;
DROP TABLE IF EXISTS prj_project_requirements;
DROP TABLE IF EXISTS prj_project_technologies;
DROP TABLE IF EXISTS prj_project_tokens;
DROP TABLE IF EXISTS prj_project_members;
DROP TABLE IF EXISTS prj_project_roles;
DROP TABLE IF EXISTS prj_projects;
DROP TABLE IF EXISTS prj_clients;

-- Eliminar secuencias
DROP SEQUENCE IF EXISTS seq_prj_projects;
DROP SEQUENCE IF EXISTS seq_prj_clients;
DROP SEQUENCE IF EXISTS seq_prj_project_roles;
DROP SEQUENCE IF EXISTS seq_prj_project_members;
DROP SEQUENCE IF EXISTS seq_prj_project_tokens;
DROP SEQUENCE IF EXISTS seq_prj_project_technologies;
DROP SEQUENCE IF EXISTS seq_prj_project_requirements;
DROP SEQUENCE IF EXISTS seq_prj_project_domains;
DROP SEQUENCE IF EXISTS seq_prj_project_versions;
DROP SEQUENCE IF EXISTS seq_prj_project_documents;
DROP SEQUENCE IF EXISTS seq_prj_project_time_tracking;
DROP SEQUENCE IF EXISTS seq_prj_project_tasks;
DROP SEQUENCE IF EXISTS seq_prj_project_task_comments;
DROP SEQUENCE IF EXISTS seq_prj_project_task_time_logs;

-- Eliminar funciones
DROP FUNCTION IF EXISTS fn_prj_project_audit_log();
DROP FUNCTION IF EXISTS fn_prj_project_role_audit_log();
DROP FUNCTION IF EXISTS fn_prj_project_member_audit_log();
DROP FUNCTION IF EXISTS fn_prj_project_token_audit_log();
DROP FUNCTION IF EXISTS fn_prj_project_technology_audit_log();
DROP FUNCTION IF EXISTS fn_prj_project_requirement_audit_log();
DROP FUNCTION IF EXISTS fn_prj_project_domain_audit_log();
DROP FUNCTION IF EXISTS fn_prj_project_version_audit_log();
DROP FUNCTION IF EXISTS fn_prj_project_document_audit_log();
DROP FUNCTION IF EXISTS fn_prj_project_time_tracking_audit_log();
DROP FUNCTION IF EXISTS fn_prj_project_task_audit_log();
DROP FUNCTION IF EXISTS fn_prj_project_task_comment_audit_log();
DROP FUNCTION IF EXISTS fn_prj_project_task_time_log_audit_log();

-- Eliminar tipos enum
DROP TYPE IF EXISTS project_type;
DROP TYPE IF EXISTS project_status;
DROP TYPE IF EXISTS project_role_type;
DROP TYPE IF EXISTS token_type;
DROP TYPE IF EXISTS requirement_type;
DROP TYPE IF EXISTS requirement_priority;
DROP TYPE IF EXISTS requirement_status;
DROP TYPE IF EXISTS domain_role;
DROP TYPE IF EXISTS usage_intensity;
DROP TYPE IF EXISTS version_type;
DROP TYPE IF EXISTS deployment_status;
DROP TYPE IF EXISTS document_type;
DROP TYPE IF EXISTS review_status;
DROP TYPE IF EXISTS activity_type;
DROP TYPE IF EXISTS billing_status;
DROP TYPE IF EXISTS task_type;
DROP TYPE IF EXISTS task_priority;
DROP TYPE IF EXISTS task_status;
DROP TYPE IF EXISTS comment_type;
```

### 2. Script CREATE TABLE (Crear todas las tablas)

```sql
-- Crear tipos enum
CREATE TYPE project_type AS ENUM ('AI_DEVELOPMENT', 'CODE_DEVELOPMENT', 'RESEARCH', 'PROOF_OF_CONCEPT', 'PRODUCTION', 'MAINTENANCE');
CREATE TYPE project_status AS ENUM ('PLANNING', 'IN_PROGRESS', 'ON_HOLD', 'COMPLETED', 'CANCELLED', 'ARCHIVED');
CREATE TYPE project_role_type AS ENUM ('OWNER', 'ADMIN', 'DEVELOPER', 'TESTER', 'REVIEWER', 'VIEWER');
CREATE TYPE token_type AS ENUM ('AI_DEVELOPMENT', 'CODE_DEVELOPMENT', 'PROJECT_ACCESS', 'IDE_INTEGRATION');
CREATE TYPE requirement_type AS ENUM ('FUNCTIONAL', 'NON_FUNCTIONAL', 'BUSINESS', 'USER_STORY', 'EPIC', 'FEATURE', 'BUG', 'IMPROVEMENT');
CREATE TYPE requirement_priority AS ENUM ('CRITICAL', 'HIGH', 'MEDIUM', 'LOW', 'OPTIONAL');
CREATE TYPE requirement_status AS ENUM ('DRAFT', 'REVIEW', 'APPROVED', 'IN_PROGRESS', 'TESTING', 'COMPLETED', 'REJECTED', 'CANCELLED');
CREATE TYPE domain_role AS ENUM ('PRIMARY', 'SECONDARY', 'REFERENCE', 'COMPLIANCE', 'TRAINING', 'VALIDATION');
CREATE TYPE usage_intensity AS ENUM ('HIGH', 'MEDIUM', 'LOW', 'OCCASIONAL', 'REFERENCE_ONLY');
CREATE TYPE version_type AS ENUM ('MAJOR', 'MINOR', 'PATCH', 'PRE_RELEASE', 'RELEASE_CANDIDATE');
CREATE TYPE deployment_status AS ENUM ('PENDING', 'IN_PROGRESS', 'DEPLOYED', 'FAILED', 'ROLLED_BACK', 'ARCHIVED');
CREATE TYPE document_type AS ENUM ('TECHNICAL_SPEC', 'FUNCTIONAL_SPEC', 'REQUIREMENTS', 'ARCHITECTURE', 'API_DOCS', 'USER_MANUAL', 'DEPLOYMENT_GUIDE', 'TEST_PLAN', 'CHANGELOG', 'MEETING_NOTES', 'DECISION_LOG');
CREATE TYPE review_status AS ENUM ('DRAFT', 'IN_REVIEW', 'APPROVED', 'REJECTED', 'NEEDS_REVISION');
CREATE TYPE activity_type AS ENUM ('DEVELOPMENT', 'TESTING', 'DESIGN', 'ANALYSIS', 'MEETING', 'DOCUMENTATION', 'DEPLOYMENT', 'SUPPORT', 'TRAINING', 'RESEARCH');
CREATE TYPE billing_status AS ENUM ('PENDING', 'BILLED', 'PAID', 'WRITTEN_OFF', 'DISPUTED');
CREATE TYPE task_type AS ENUM ('FEATURE', 'BUG', 'IMPROVEMENT', 'TASK', 'STORY', 'EPIC', 'SUBTASK', 'RESEARCH', 'DOCUMENTATION', 'TESTING', 'DEPLOYMENT', 'SUPPORT');
CREATE TYPE task_priority AS ENUM ('CRITICAL', 'HIGH', 'MEDIUM', 'LOW', 'OPTIONAL');
CREATE TYPE task_status AS ENUM ('BACKLOG', 'TO_DO', 'IN_PROGRESS', 'IN_REVIEW', 'TESTING', 'DONE', 'CANCELLED', 'BLOCKED', 'ON_HOLD');
CREATE TYPE comment_type AS ENUM ('GENERAL', 'STATUS_UPDATE', 'BLOCK_REASON', 'SOLUTION', 'QUESTION', 'FEEDBACK', 'APPROVAL', 'REJECTION');

-- Crear secuencias
CREATE SEQUENCE seq_prj_projects START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_prj_clients START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_prj_project_roles START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_prj_project_members START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_prj_project_tokens START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_prj_project_technologies START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_prj_project_requirements START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_prj_project_domains START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_prj_project_versions START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_prj_project_documents START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_prj_project_time_tracking START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_prj_project_tasks START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_prj_project_task_comments START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_prj_project_task_time_logs START WITH 1 INCREMENT BY 1;

-- Tabla de clientes
CREATE TABLE prj_clients (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    contact_email VARCHAR(255),
    contact_phone VARCHAR(50),
    address TEXT,
    industry VARCHAR(100),
    company_size VARCHAR(50),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE prj_clients IS 'Clientes del sistema de proyectos';
COMMENT ON COLUMN prj_clients.id IS 'Identificador único del cliente';
COMMENT ON COLUMN prj_clients.name IS 'Nombre del cliente';
COMMENT ON COLUMN prj_clients.description IS 'Descripción del cliente';
COMMENT ON COLUMN prj_clients.contact_email IS 'Email de contacto del cliente';
COMMENT ON COLUMN prj_clients.contact_phone IS 'Teléfono de contacto del cliente';
COMMENT ON COLUMN prj_clients.address IS 'Dirección del cliente';
COMMENT ON COLUMN prj_clients.industry IS 'Industria del cliente';
COMMENT ON COLUMN prj_clients.company_size IS 'Tamaño de la empresa del cliente';
COMMENT ON COLUMN prj_clients.is_active IS 'Indica si el cliente está activo';
COMMENT ON COLUMN prj_clients.created_at IS 'Fecha de creación del cliente';
COMMENT ON COLUMN prj_clients.updated_at IS 'Fecha de última actualización del cliente';

-- Tabla de proyectos
CREATE TABLE prj_projects (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    project_code VARCHAR(50) UNIQUE NOT NULL,
    project_type project_type NOT NULL,
    status project_status DEFAULT 'PLANNING',
    owner_id BIGINT NOT NULL REFERENCES cor_users(id),
    client_id BIGINT REFERENCES prj_clients(id),
    department_id BIGINT REFERENCES cor_departments(id),
    start_date DATE,
    end_date DATE,
    budget DECIMAL(15,2),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE prj_projects IS 'Proyectos del sistema';
COMMENT ON COLUMN prj_projects.id IS 'Identificador único del proyecto';
COMMENT ON COLUMN prj_projects.name IS 'Nombre del proyecto';
COMMENT ON COLUMN prj_projects.description IS 'Descripción del proyecto';
COMMENT ON COLUMN prj_projects.project_code IS 'Código único del proyecto';
COMMENT ON COLUMN prj_projects.project_type IS 'Tipo de proyecto';
COMMENT ON COLUMN prj_projects.status IS 'Estado actual del proyecto';
COMMENT ON COLUMN prj_projects.owner_id IS 'ID del propietario del proyecto';
COMMENT ON COLUMN prj_projects.client_id IS 'ID del cliente asociado';
COMMENT ON COLUMN prj_projects.department_id IS 'ID del departamento asociado';
COMMENT ON COLUMN prj_projects.start_date IS 'Fecha de inicio del proyecto';
COMMENT ON COLUMN prj_projects.end_date IS 'Fecha de finalización del proyecto';
COMMENT ON COLUMN prj_projects.budget IS 'Presupuesto del proyecto';
COMMENT ON COLUMN prj_projects.is_active IS 'Indica si el proyecto está activo';
COMMENT ON COLUMN prj_projects.created_at IS 'Fecha de creación del proyecto';
COMMENT ON COLUMN prj_projects.updated_at IS 'Fecha de última actualización del proyecto';

-- Tabla de roles de proyecto
CREATE TABLE prj_project_roles (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL REFERENCES prj_projects(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES cor_users(id),
    role project_role_type NOT NULL,
    permissions JSONB,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE prj_project_roles IS 'Roles de usuarios en proyectos';
COMMENT ON COLUMN prj_project_roles.id IS 'Identificador único del rol de proyecto';
COMMENT ON COLUMN prj_project_roles.project_id IS 'ID del proyecto';
COMMENT ON COLUMN prj_project_roles.user_id IS 'ID del usuario';
COMMENT ON COLUMN prj_project_roles.role IS 'Tipo de rol en el proyecto';
COMMENT ON COLUMN prj_project_roles.permissions IS 'Permisos específicos del rol en formato JSON';
COMMENT ON COLUMN prj_project_roles.is_active IS 'Indica si el rol está activo';
COMMENT ON COLUMN prj_project_roles.created_at IS 'Fecha de creación del rol';
COMMENT ON COLUMN prj_project_roles.updated_at IS 'Fecha de última actualización del rol';

-- Tabla de miembros del proyecto
CREATE TABLE prj_project_members (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL REFERENCES prj_projects(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES cor_users(id),
    role project_role_type NOT NULL,
    join_date DATE DEFAULT CURRENT_DATE,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE prj_project_members IS 'Miembros de proyectos';
COMMENT ON COLUMN prj_project_members.id IS 'Identificador único del miembro';
COMMENT ON COLUMN prj_project_members.project_id IS 'ID del proyecto';
COMMENT ON COLUMN prj_project_members.user_id IS 'ID del usuario miembro';
COMMENT ON COLUMN prj_project_members.role IS 'Rol del miembro en el proyecto';
COMMENT ON COLUMN prj_project_members.join_date IS 'Fecha de ingreso al proyecto';
COMMENT ON COLUMN prj_project_members.is_active IS 'Indica si el miembro está activo';
COMMENT ON COLUMN prj_project_members.created_at IS 'Fecha de creación del registro';
COMMENT ON COLUMN prj_project_members.updated_at IS 'Fecha de última actualización del registro';

-- Tabla de tokens de proyecto
CREATE TABLE prj_project_tokens (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL REFERENCES prj_projects(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES cor_users(id),
    token_type token_type NOT NULL,
    token_value TEXT NOT NULL,
    expires_at TIMESTAMP,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE prj_project_tokens IS 'Tokens de acceso a proyectos';
COMMENT ON COLUMN prj_project_tokens.id IS 'Identificador único del token';
COMMENT ON COLUMN prj_project_tokens.project_id IS 'ID del proyecto';
COMMENT ON COLUMN prj_project_tokens.user_id IS 'ID del usuario propietario del token';
COMMENT ON COLUMN prj_project_tokens.token_type IS 'Tipo de token';
COMMENT ON COLUMN prj_project_tokens.token_value IS 'Valor del token encriptado';
COMMENT ON COLUMN prj_project_tokens.expires_at IS 'Fecha de expiración del token';
COMMENT ON COLUMN prj_project_tokens.is_active IS 'Indica si el token está activo';
COMMENT ON COLUMN prj_project_tokens.created_at IS 'Fecha de creación del token';
COMMENT ON COLUMN prj_project_tokens.updated_at IS 'Fecha de última actualización del token';

-- Tabla de tecnologías del proyecto
CREATE TABLE prj_project_technologies (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL REFERENCES prj_projects(id) ON DELETE CASCADE,
    technology_id BIGINT NOT NULL REFERENCES tch_technologies(id),
    version VARCHAR(50),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE prj_project_technologies IS 'Tecnologías asociadas a proyectos';
COMMENT ON COLUMN prj_project_technologies.id IS 'Identificador único de la tecnología del proyecto';
COMMENT ON COLUMN prj_project_technologies.project_id IS 'ID del proyecto';
COMMENT ON COLUMN prj_project_technologies.technology_id IS 'ID de la tecnología';
COMMENT ON COLUMN prj_project_technologies.version IS 'Versión de la tecnología utilizada';
COMMENT ON COLUMN prj_project_technologies.is_active IS 'Indica si la tecnología está activa en el proyecto';
COMMENT ON COLUMN prj_project_technologies.created_at IS 'Fecha de asociación de la tecnología';
COMMENT ON COLUMN prj_project_technologies.updated_at IS 'Fecha de última actualización de la asociación';

-- Tabla de requisitos del proyecto
CREATE TABLE prj_project_requirements (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL REFERENCES prj_projects(id) ON DELETE CASCADE,
    requirement_code VARCHAR(50) NOT NULL,
    requirement_type requirement_type NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    priority requirement_priority NOT NULL,
    status requirement_status DEFAULT 'DRAFT',
    acceptance_criteria TEXT,
    business_value INTEGER CHECK (business_value >= 1 AND business_value <= 10),
    story_points INTEGER,
    assigned_to BIGINT REFERENCES cor_users(id),
    estimated_hours DECIMAL(8,2),
    actual_hours DECIMAL(8,2),
    due_date DATE,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE prj_project_requirements IS 'Requisitos de proyectos para generación automática de aplicaciones';
COMMENT ON COLUMN prj_project_requirements.id IS 'Identificador único del requisito';
COMMENT ON COLUMN prj_project_requirements.project_id IS 'ID del proyecto';
COMMENT ON COLUMN prj_project_requirements.requirement_code IS 'Código único del requisito';
COMMENT ON COLUMN prj_project_requirements.requirement_type IS 'Tipo de requisito';
COMMENT ON COLUMN prj_project_requirements.title IS 'Título del requisito';
COMMENT ON COLUMN prj_project_requirements.description IS 'Descripción del requisito';
COMMENT ON COLUMN prj_project_requirements.priority IS 'Prioridad del requisito';
COMMENT ON COLUMN prj_project_requirements.status IS 'Estado actual del requisito';
COMMENT ON COLUMN prj_project_requirements.acceptance_criteria IS 'Criterios de aceptación en formato JSON';
COMMENT ON COLUMN prj_project_requirements.business_value IS 'Valor de negocio del 1 al 10';
COMMENT ON COLUMN prj_project_requirements.story_points IS 'Puntos de historia para estimación';
COMMENT ON COLUMN prj_project_requirements.assigned_to IS 'ID del usuario asignado';
COMMENT ON COLUMN prj_project_requirements.estimated_hours IS 'Horas estimadas para completar';
COMMENT ON COLUMN prj_project_requirements.actual_hours IS 'Horas reales utilizadas';
COMMENT ON COLUMN prj_project_requirements.due_date IS 'Fecha de vencimiento del requisito';
COMMENT ON COLUMN prj_project_requirements.is_active IS 'Indica si el requisito está activo';
COMMENT ON COLUMN prj_project_requirements.created_at IS 'Fecha de creación del requisito';
COMMENT ON COLUMN prj_project_requirements.updated_at IS 'Fecha de última actualización del requisito';

-- Tabla de dominios del proyecto
CREATE TABLE prj_project_domains (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL REFERENCES prj_projects(id) ON DELETE CASCADE,
    domain_id BIGINT NOT NULL REFERENCES dmn_domains(id),
    domain_role domain_role NOT NULL,
    usage_intensity usage_intensity NOT NULL,
    data_requirements TEXT,
    compliance_requirements TEXT,
    rag_integration BOOLEAN DEFAULT false,
    rag_configuration TEXT,
    data_retention_policy TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE prj_project_domains IS 'Dominios asociados a proyectos para ingesta de datos';
COMMENT ON COLUMN prj_project_domains.id IS 'Identificador único del dominio del proyecto';
COMMENT ON COLUMN prj_project_domains.project_id IS 'ID del proyecto';
COMMENT ON COLUMN prj_project_domains.domain_id IS 'ID del dominio';
COMMENT ON COLUMN prj_project_domains.domain_role IS 'Rol del dominio en el proyecto';
COMMENT ON COLUMN prj_project_domains.usage_intensity IS 'Intensidad de uso del dominio';
COMMENT ON COLUMN prj_project_domains.data_requirements IS 'Requisitos de datos en formato JSON';
COMMENT ON COLUMN prj_project_domains.compliance_requirements IS 'Requisitos de cumplimiento en formato JSON';
COMMENT ON COLUMN prj_project_domains.rag_integration IS 'Indica si el dominio está integrado con RAG';
COMMENT ON COLUMN prj_project_domains.rag_configuration IS 'Configuración RAG en formato JSON';
COMMENT ON COLUMN prj_project_domains.data_retention_policy IS 'Política de retención de datos en formato JSON';
COMMENT ON COLUMN prj_project_domains.is_active IS 'Indica si el dominio está activo en el proyecto';
COMMENT ON COLUMN prj_project_domains.created_at IS 'Fecha de asociación del dominio';
COMMENT ON COLUMN prj_project_domains.updated_at IS 'Fecha de última actualización de la asociación';

-- Tabla de versiones del proyecto
CREATE TABLE prj_project_versions (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL REFERENCES prj_projects(id) ON DELETE CASCADE,
    version_number VARCHAR(20) NOT NULL,
    version_type version_type NOT NULL,
    release_notes TEXT,
    changelog TEXT,
    deployment_status deployment_status DEFAULT 'PENDING',
    deployed_at TIMESTAMP,
    deployed_by BIGINT REFERENCES cor_users(id),
    rollback_available BOOLEAN DEFAULT false,
    rollback_instructions TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE prj_project_versions IS 'Versiones y releases de proyectos';
COMMENT ON COLUMN prj_project_versions.id IS 'Identificador único de la versión';
COMMENT ON COLUMN prj_project_versions.project_id IS 'ID del proyecto';
COMMENT ON COLUMN prj_project_versions.version_number IS 'Número de versión (SemVer)';
COMMENT ON COLUMN prj_project_versions.version_type IS 'Tipo de versión';
COMMENT ON COLUMN prj_project_versions.release_notes IS 'Notas de la release';
COMMENT ON COLUMN prj_project_versions.changelog IS 'Registro de cambios en formato JSON';
COMMENT ON COLUMN prj_project_versions.deployment_status IS 'Estado del despliegue';
COMMENT ON COLUMN prj_project_versions.deployed_at IS 'Fecha de despliegue';
COMMENT ON COLUMN prj_project_versions.deployed_by IS 'ID del usuario que realizó el despliegue';
COMMENT ON COLUMN prj_project_versions.rollback_available IS 'Indica si está disponible el rollback';
COMMENT ON COLUMN prj_project_versions.rollback_instructions IS 'Instrucciones para realizar rollback';
COMMENT ON COLUMN prj_project_versions.is_active IS 'Indica si la versión está activa';
COMMENT ON COLUMN prj_project_versions.created_at IS 'Fecha de creación de la versión';
COMMENT ON COLUMN prj_project_versions.updated_at IS 'Fecha de última actualización de la versión';

-- Tabla de documentos del proyecto
CREATE TABLE prj_project_documents (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL REFERENCES prj_projects(id) ON DELETE CASCADE,
    document_type document_type NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    content_hash VARCHAR(64),
    file_path VARCHAR(500),
    file_size BIGINT,
    mime_type VARCHAR(100),
    version VARCHAR(20),
    author_id BIGINT NOT NULL REFERENCES cor_users(id),
    reviewer_id BIGINT REFERENCES cor_users(id),
    review_status review_status DEFAULT 'DRAFT',
    review_notes TEXT,
    is_rag_indexed BOOLEAN DEFAULT false,
    rag_indexed_at TIMESTAMP,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE prj_project_documents IS 'Documentos técnicos y funcionales de proyectos integrados con RAG';
COMMENT ON COLUMN prj_project_documents.id IS 'Identificador único del documento';
COMMENT ON COLUMN prj_project_documents.project_id IS 'ID del proyecto';
COMMENT ON COLUMN prj_project_documents.document_type IS 'Tipo de documento';
COMMENT ON COLUMN prj_project_documents.title IS 'Título del documento';
COMMENT ON COLUMN prj_project_documents.content IS 'Contenido del documento';
COMMENT ON COLUMN prj_project_documents.content_hash IS 'Hash del contenido para RAG';
COMMENT ON COLUMN prj_project_documents.file_path IS 'Ruta al archivo si existe';
COMMENT ON COLUMN prj_project_documents.file_size IS 'Tamaño del archivo en bytes';
COMMENT ON COLUMN prj_project_documents.mime_type IS 'Tipo MIME del archivo';
COMMENT ON COLUMN prj_project_documents.version IS 'Versión del documento';
COMMENT ON COLUMN prj_project_documents.author_id IS 'ID del autor del documento';
COMMENT ON COLUMN prj_project_documents.reviewer_id IS 'ID del revisor del documento';
COMMENT ON COLUMN prj_project_documents.review_status IS 'Estado de revisión del documento';
COMMENT ON COLUMN prj_project_documents.review_notes IS 'Notas de revisión';
COMMENT ON COLUMN prj_project_documents.is_rag_indexed IS 'Indica si el documento está indexado en RAG';
COMMENT ON COLUMN prj_project_documents.rag_indexed_at IS 'Fecha de indexación en RAG';
COMMENT ON COLUMN prj_project_documents.is_active IS 'Indica si el documento está activo';
COMMENT ON COLUMN prj_project_documents.created_at IS 'Fecha de creación del documento';
COMMENT ON COLUMN prj_project_documents.updated_at IS 'Fecha de última actualización del documento';

-- Tabla de seguimiento de tiempo del proyecto
CREATE TABLE prj_project_time_tracking (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL REFERENCES prj_projects(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES cor_users(id),
    requirement_id BIGINT REFERENCES prj_project_requirements(id),
    activity_type activity_type NOT NULL,
    description TEXT,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    duration_hours DECIMAL(8,2),
    hourly_rate DECIMAL(10,2),
    total_cost DECIMAL(15,2),
    is_billable BOOLEAN DEFAULT true,
    billing_status billing_status DEFAULT 'PENDING',
    notes TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE prj_project_time_tracking IS 'Seguimiento de tiempo y costes de proyectos';
COMMENT ON COLUMN prj_project_time_tracking.id IS 'Identificador único del registro de tiempo';
COMMENT ON COLUMN prj_project_time_tracking.project_id IS 'ID del proyecto';
COMMENT ON COLUMN prj_project_time_tracking.user_id IS 'ID del usuario';
COMMENT ON COLUMN prj_project_time_tracking.requirement_id IS 'ID del requisito asociado';
COMMENT ON COLUMN prj_project_time_tracking.activity_type IS 'Tipo de actividad realizada';
COMMENT ON COLUMN prj_project_time_tracking.description IS 'Descripción de la actividad';
COMMENT ON COLUMN prj_project_time_tracking.start_time IS 'Hora de inicio de la actividad';
COMMENT ON COLUMN prj_project_time_tracking.end_time IS 'Hora de finalización de la actividad';
COMMENT ON COLUMN prj_project_time_tracking.duration_hours IS 'Duración en horas de la actividad';
COMMENT ON COLUMN prj_project_time_tracking.hourly_rate IS 'Tarifa por hora del usuario';
COMMENT ON COLUMN prj_project_time_tracking.total_cost IS 'Coste total de la actividad';
COMMENT ON COLUMN prj_project_time_tracking.is_billable IS 'Indica si la actividad es facturable';
COMMENT ON COLUMN prj_project_time_tracking.billing_status IS 'Estado de facturación';
COMMENT ON COLUMN prj_project_time_tracking.notes IS 'Notas adicionales';
COMMENT ON COLUMN prj_project_time_tracking.is_active IS 'Indica si el registro está activo';
COMMENT ON COLUMN prj_project_time_tracking.created_at IS 'Fecha de creación del registro';
COMMENT ON COLUMN prj_project_time_tracking.updated_at IS 'Fecha de última actualización del registro';

-- Tabla de tareas del proyecto
CREATE TABLE prj_project_tasks (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL REFERENCES prj_projects(id) ON DELETE CASCADE,
    requirement_id BIGINT REFERENCES prj_project_requirements(id),
    parent_task_id BIGINT REFERENCES prj_project_tasks(id),
    task_code VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    task_type task_type NOT NULL,
    priority task_priority NOT NULL,
    status task_status DEFAULT 'BACKLOG',
    assigned_to BIGINT REFERENCES cor_users(id),
    reporter_id BIGINT NOT NULL REFERENCES cor_users(id),
    estimated_hours DECIMAL(8,2),
    actual_hours DECIMAL(8,2),
    story_points INTEGER,
    due_date DATE,
    start_date DATE,
    completed_date DATE,
    progress_percentage INTEGER CHECK (progress_percentage >= 0 AND progress_percentage <= 100),
    dependencies TEXT,
    tags TEXT,
    attachments TEXT,
    comments TEXT,
    time_logs TEXT,
    is_blocked BOOLEAN DEFAULT false,
    block_reason TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE prj_project_tasks IS 'Tareas de proyectos para gestión de desarrollo';
COMMENT ON COLUMN prj_project_tasks.id IS 'Identificador único de la tarea';
COMMENT ON COLUMN prj_project_tasks.project_id IS 'ID del proyecto';
COMMENT ON COLUMN prj_project_tasks.requirement_id IS 'ID del requisito asociado';
COMMENT ON COLUMN prj_project_tasks.parent_task_id IS 'ID de la tarea padre (para subtareas)';
COMMENT ON COLUMN prj_project_tasks.task_code IS 'Código único de la tarea';
COMMENT ON COLUMN prj_project_tasks.title IS 'Título de la tarea';
COMMENT ON COLUMN prj_project_tasks.description IS 'Descripción de la tarea';
COMMENT ON COLUMN prj_project_tasks.task_type IS 'Tipo de tarea';
COMMENT ON COLUMN prj_project_tasks.priority IS 'Prioridad de la tarea';
COMMENT ON COLUMN prj_project_tasks.status IS 'Estado actual de la tarea';
COMMENT ON COLUMN prj_project_tasks.assigned_to IS 'ID del usuario asignado';
COMMENT ON COLUMN prj_project_tasks.reporter_id IS 'ID del usuario que reportó la tarea';
COMMENT ON COLUMN prj_project_tasks.estimated_hours IS 'Horas estimadas para completar';
COMMENT ON COLUMN prj_project_tasks.actual_hours IS 'Horas reales utilizadas';
COMMENT ON COLUMN prj_project_tasks.story_points IS 'Puntos de historia para estimación';
COMMENT ON COLUMN prj_project_tasks.due_date IS 'Fecha de vencimiento de la tarea';
COMMENT ON COLUMN prj_project_tasks.start_date IS 'Fecha de inicio de la tarea';
COMMENT ON COLUMN prj_project_tasks.completed_date IS 'Fecha de finalización de la tarea';
COMMENT ON COLUMN prj_project_tasks.progress_percentage IS 'Porcentaje de progreso (0-100)';
COMMENT ON COLUMN prj_project_tasks.dependencies IS 'IDs de tareas dependientes en formato JSON';
COMMENT ON COLUMN prj_project_tasks.tags IS 'Tags de la tarea en formato JSON';
COMMENT ON COLUMN prj_project_tasks.attachments IS 'Archivos adjuntos en formato JSON';
COMMENT ON COLUMN prj_project_tasks.comments IS 'Comentarios de la tarea en formato JSON';
COMMENT ON COLUMN prj_project_tasks.time_logs IS 'Registros de tiempo en formato JSON';
COMMENT ON COLUMN prj_project_tasks.is_blocked IS 'Indica si la tarea está bloqueada';
COMMENT ON COLUMN prj_project_tasks.block_reason IS 'Razón del bloqueo de la tarea';
COMMENT ON COLUMN prj_project_tasks.is_active IS 'Indica si la tarea está activa';
COMMENT ON COLUMN prj_project_tasks.created_at IS 'Fecha de creación de la tarea';
COMMENT ON COLUMN prj_project_tasks.updated_at IS 'Fecha de última actualización de la tarea';

-- Tabla de comentarios de tareas
CREATE TABLE prj_project_task_comments (
    id BIGSERIAL PRIMARY KEY,
    task_id BIGINT NOT NULL REFERENCES prj_project_tasks(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES cor_users(id),
    comment TEXT NOT NULL,
    comment_type comment_type DEFAULT 'GENERAL',
    is_internal BOOLEAN DEFAULT false,
    mentions TEXT,
    attachments TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE prj_project_task_comments IS 'Comentarios de tareas de proyectos';
COMMENT ON COLUMN prj_project_task_comments.id IS 'Identificador único del comentario';
COMMENT ON COLUMN prj_project_task_comments.task_id IS 'ID de la tarea';
COMMENT ON COLUMN prj_project_task_comments.user_id IS 'ID del usuario que hizo el comentario';
COMMENT ON COLUMN prj_project_task_comments.comment IS 'Contenido del comentario';
COMMENT ON COLUMN prj_project_task_comments.comment_type IS 'Tipo de comentario';
COMMENT ON COLUMN prj_project_task_comments.is_internal IS 'Indica si es un comentario interno del equipo';
COMMENT ON COLUMN prj_project_task_comments.mentions IS 'Usuarios mencionados en formato JSON';
COMMENT ON COLUMN prj_project_task_comments.attachments IS 'Archivos adjuntos en formato JSON';
COMMENT ON COLUMN prj_project_task_comments.is_active IS 'Indica si el comentario está activo';
COMMENT ON COLUMN prj_project_task_comments.created_at IS 'Fecha de creación del comentario';
COMMENT ON COLUMN prj_project_task_comments.updated_at IS 'Fecha de última actualización del comentario';

-- Tabla de registros de tiempo de tareas
CREATE TABLE prj_project_task_time_logs (
    id BIGSERIAL PRIMARY KEY,
    task_id BIGINT NOT NULL REFERENCES prj_project_tasks(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES cor_users(id),
    activity_type activity_type NOT NULL,
    description TEXT,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    duration_hours DECIMAL(8,2),
    is_billable BOOLEAN DEFAULT true,
    notes TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE prj_project_task_time_logs IS 'Registros de tiempo específicos de tareas';
COMMENT ON COLUMN prj_project_task_time_logs.id IS 'Identificador único del registro de tiempo';
COMMENT ON COLUMN prj_project_task_time_logs.task_id IS 'ID de la tarea';
COMMENT ON COLUMN prj_project_task_time_logs.user_id IS 'ID del usuario';
COMMENT ON COLUMN prj_project_task_time_logs.activity_type IS 'Tipo de actividad realizada';
COMMENT ON COLUMN prj_project_task_time_logs.description IS 'Descripción de la actividad';
COMMENT ON COLUMN prj_project_task_time_logs.start_time IS 'Hora de inicio de la actividad';
COMMENT ON COLUMN prj_project_task_time_logs.end_time IS 'Hora de finalización de la actividad';
COMMENT ON COLUMN prj_project_task_time_logs.duration_hours IS 'Duración en horas de la actividad';
COMMENT ON COLUMN prj_project_task_time_logs.is_billable IS 'Indica si la actividad es facturable';
COMMENT ON COLUMN prj_project_task_time_logs.notes IS 'Notas adicionales';
COMMENT ON COLUMN prj_project_task_time_logs.is_active IS 'Indica si el registro está activo';
COMMENT ON COLUMN prj_project_task_time_logs.created_at IS 'Fecha de creación del registro';
COMMENT ON COLUMN prj_project_task_time_logs.updated_at IS 'Fecha de última actualización del registro';
```

### 3. Script CREATE INDEX (Crear índices para optimización)

```sql
-- Índices para prj_clients
CREATE INDEX idx_prj_clients_name ON prj_clients(name);
CREATE INDEX idx_prj_clients_industry ON prj_clients(industry);
CREATE INDEX idx_prj_clients_active ON prj_clients(is_active);
CREATE INDEX idx_prj_clients_created_at ON prj_clients(created_at);

-- Índices para prj_projects
CREATE INDEX idx_prj_projects_owner_id ON prj_projects(owner_id);
CREATE INDEX idx_prj_projects_client_id ON prj_projects(client_id);
CREATE INDEX idx_prj_projects_department_id ON prj_projects(department_id);
CREATE INDEX idx_prj_projects_status ON prj_projects(status);
CREATE INDEX idx_prj_projects_type ON prj_projects(project_type);
CREATE INDEX idx_prj_projects_code ON prj_projects(project_code);
CREATE INDEX idx_prj_projects_start_date ON prj_projects(start_date);
CREATE INDEX idx_prj_projects_end_date ON prj_projects(end_date);
CREATE INDEX idx_prj_projects_active ON prj_projects(is_active);
CREATE INDEX idx_prj_projects_created_at ON prj_projects(created_at);

-- Índices para prj_project_roles
CREATE INDEX idx_prj_project_roles_project_id ON prj_project_roles(project_id);
CREATE INDEX idx_prj_project_roles_user_id ON prj_project_roles(user_id);
CREATE INDEX idx_prj_project_roles_role ON prj_project_roles(role);
CREATE INDEX idx_prj_project_roles_active ON prj_project_roles(is_active);

-- Índices para prj_project_members
CREATE INDEX idx_prj_project_members_project_id ON prj_project_members(project_id);
CREATE INDEX idx_prj_project_members_user_id ON prj_project_members(user_id);
CREATE INDEX idx_prj_project_members_role ON prj_project_members(role);
CREATE INDEX idx_prj_project_members_join_date ON prj_project_members(join_date);
CREATE INDEX idx_prj_project_members_active ON prj_project_members(is_active);

-- Índices para prj_project_tokens
CREATE INDEX idx_prj_project_tokens_project_id ON prj_project_tokens(project_id);
CREATE INDEX idx_prj_project_tokens_user_id ON prj_project_tokens(user_id);
CREATE INDEX idx_prj_project_tokens_type ON prj_project_tokens(token_type);
CREATE INDEX idx_prj_project_tokens_expires_at ON prj_project_tokens(expires_at);
CREATE INDEX idx_prj_project_tokens_active ON prj_project_tokens(is_active);

-- Índices para prj_project_technologies
CREATE INDEX idx_prj_project_technologies_project_id ON prj_project_technologies(project_id);
CREATE INDEX idx_prj_project_technologies_technology_id ON prj_project_technologies(technology_id);
CREATE INDEX idx_prj_project_technologies_version ON prj_project_technologies(version);
CREATE INDEX idx_prj_project_technologies_active ON prj_project_technologies(is_active);

-- Índices para prj_project_requirements
CREATE INDEX idx_prj_project_requirements_project_id ON prj_project_requirements(project_id);
CREATE INDEX idx_prj_project_requirements_type ON prj_project_requirements(requirement_type);
CREATE INDEX idx_prj_project_requirements_priority ON prj_project_requirements(priority);
CREATE INDEX idx_prj_project_requirements_status ON prj_project_requirements(status);
CREATE INDEX idx_prj_project_requirements_assigned_to ON prj_project_requirements(assigned_to);
CREATE INDEX idx_prj_project_requirements_due_date ON prj_project_requirements(due_date);
CREATE INDEX idx_prj_project_requirements_code ON prj_project_requirements(requirement_code);
CREATE INDEX idx_prj_project_requirements_active ON prj_project_requirements(is_active);
CREATE INDEX idx_prj_project_requirements_created_at ON prj_project_requirements(created_at);

-- Índices para prj_project_domains
CREATE INDEX idx_prj_project_domains_project_id ON prj_project_domains(project_id);
CREATE INDEX idx_prj_project_domains_domain_id ON prj_project_domains(domain_id);
CREATE INDEX idx_prj_project_domains_role ON prj_project_domains(domain_role);
CREATE INDEX idx_prj_project_domains_usage_intensity ON prj_project_domains(usage_intensity);
CREATE INDEX idx_prj_project_domains_rag_integration ON prj_project_domains(rag_integration);
CREATE INDEX idx_prj_project_domains_active ON prj_project_domains(is_active);

-- Índices para prj_project_versions
CREATE INDEX idx_prj_project_versions_project_id ON prj_project_versions(project_id);
CREATE INDEX idx_prj_project_versions_version_number ON prj_project_versions(version_number);
CREATE INDEX idx_prj_project_versions_type ON prj_project_versions(version_type);
CREATE INDEX idx_prj_project_versions_deployment_status ON prj_project_versions(deployment_status);
CREATE INDEX idx_prj_project_versions_deployed_at ON prj_project_versions(deployed_at);
CREATE INDEX idx_prj_project_versions_deployed_by ON prj_project_versions(deployed_by);
CREATE INDEX idx_prj_project_versions_active ON prj_project_versions(is_active);
CREATE INDEX idx_prj_project_versions_created_at ON prj_project_versions(created_at);

-- Índices para prj_project_documents
CREATE INDEX idx_prj_project_documents_project_id ON prj_project_documents(project_id);
CREATE INDEX idx_prj_project_documents_type ON prj_project_documents(document_type);
CREATE INDEX idx_prj_project_documents_author_id ON prj_project_documents(author_id);
CREATE INDEX idx_prj_project_documents_reviewer_id ON prj_project_documents(reviewer_id);
CREATE INDEX idx_prj_project_documents_review_status ON prj_project_documents(review_status);
CREATE INDEX idx_prj_project_documents_rag_indexed ON prj_project_documents(is_rag_indexed);
CREATE INDEX idx_prj_project_documents_version ON prj_project_documents(version);
CREATE INDEX idx_prj_project_documents_active ON prj_project_documents(is_active);
CREATE INDEX idx_prj_project_documents_created_at ON prj_project_documents(created_at);

-- Índices para prj_project_time_tracking
CREATE INDEX idx_prj_project_time_tracking_project_id ON prj_project_time_tracking(project_id);
CREATE INDEX idx_prj_project_time_tracking_user_id ON prj_project_time_tracking(user_id);
CREATE INDEX idx_prj_project_time_tracking_requirement_id ON prj_project_time_tracking(requirement_id);
CREATE INDEX idx_prj_project_time_tracking_activity_type ON prj_project_time_tracking(activity_type);
CREATE INDEX idx_prj_project_time_tracking_start_time ON prj_project_time_tracking(start_time);
CREATE INDEX idx_prj_project_time_tracking_end_time ON prj_project_time_tracking(end_time);
CREATE INDEX idx_prj_project_time_tracking_billing_status ON prj_project_time_tracking(billing_status);
CREATE INDEX idx_prj_project_time_tracking_billable ON prj_project_time_tracking(is_billable);
CREATE INDEX idx_prj_project_time_tracking_active ON prj_project_time_tracking(is_active);
CREATE INDEX idx_prj_project_time_tracking_created_at ON prj_project_time_tracking(created_at);

-- Índices para prj_project_tasks
CREATE INDEX idx_prj_project_tasks_project_id ON prj_project_tasks(project_id);
CREATE INDEX idx_prj_project_tasks_requirement_id ON prj_project_tasks(requirement_id);
CREATE INDEX idx_prj_project_tasks_parent_task_id ON prj_project_tasks(parent_task_id);
CREATE INDEX idx_prj_project_tasks_assigned_to ON prj_project_tasks(assigned_to);
CREATE INDEX idx_prj_project_tasks_reporter_id ON prj_project_tasks(reporter_id);
CREATE INDEX idx_prj_project_tasks_status ON prj_project_tasks(status);
CREATE INDEX idx_prj_project_tasks_priority ON prj_project_tasks(priority);
CREATE INDEX idx_prj_project_tasks_type ON prj_project_tasks(task_type);
CREATE INDEX idx_prj_project_tasks_due_date ON prj_project_tasks(due_date);
CREATE INDEX idx_prj_project_tasks_start_date ON prj_project_tasks(start_date);
CREATE INDEX idx_prj_project_tasks_completed_date ON prj_project_tasks(completed_date);
CREATE INDEX idx_prj_project_tasks_progress_percentage ON prj_project_tasks(progress_percentage);
CREATE INDEX idx_prj_project_tasks_blocked ON prj_project_tasks(is_blocked);
CREATE INDEX idx_prj_project_tasks_code ON prj_project_tasks(task_code);
CREATE INDEX idx_prj_project_tasks_active ON prj_project_tasks(is_active);
CREATE INDEX idx_prj_project_tasks_created_at ON prj_project_tasks(created_at);

-- Índices para prj_project_task_comments
CREATE INDEX idx_prj_project_task_comments_task_id ON prj_project_task_comments(task_id);
CREATE INDEX idx_prj_project_task_comments_user_id ON prj_project_task_comments(user_id);
CREATE INDEX idx_prj_project_task_comments_type ON prj_project_task_comments(comment_type);
CREATE INDEX idx_prj_project_task_comments_internal ON prj_project_task_comments(is_internal);
CREATE INDEX idx_prj_project_task_comments_active ON prj_project_task_comments(is_active);
CREATE INDEX idx_prj_project_task_comments_created_at ON prj_project_task_comments(created_at);

-- Índices para prj_project_task_time_logs
CREATE INDEX idx_prj_project_task_time_logs_task_id ON prj_project_task_time_logs(task_id);
CREATE INDEX idx_prj_project_task_time_logs_user_id ON prj_project_task_time_logs(user_id);
CREATE INDEX idx_prj_project_task_time_logs_activity_type ON prj_project_task_time_logs(activity_type);
CREATE INDEX idx_prj_project_task_time_logs_start_time ON prj_project_task_time_logs(start_time);
CREATE INDEX idx_prj_project_task_time_logs_end_time ON prj_project_task_time_logs(end_time);
CREATE INDEX idx_prj_project_task_time_logs_billable ON prj_project_task_time_logs(is_billable);
CREATE INDEX idx_prj_project_task_time_logs_active ON prj_project_task_time_logs(is_active);
CREATE INDEX idx_prj_project_task_time_logs_created_at ON prj_project_task_time_logs(created_at);

-- Índices compuestos para consultas frecuentes
CREATE INDEX idx_prj_projects_owner_status ON prj_projects(owner_id, status);
CREATE INDEX idx_prj_projects_client_status ON prj_projects(client_id, status);
CREATE INDEX idx_prj_projects_department_status ON prj_projects(department_id, status);
CREATE INDEX idx_prj_projects_type_status ON prj_projects(project_type, status);

CREATE INDEX idx_prj_project_tasks_project_status ON prj_project_tasks(project_id, status);
CREATE INDEX idx_prj_project_tasks_project_priority ON prj_project_tasks(project_id, priority);
CREATE INDEX idx_prj_project_tasks_project_assigned ON prj_project_tasks(project_id, assigned_to);
CREATE INDEX idx_prj_project_tasks_requirement_status ON prj_project_tasks(requirement_id, status);

CREATE INDEX idx_prj_project_requirements_project_priority ON prj_project_requirements(project_id, priority);
CREATE INDEX idx_prj_project_requirements_project_status ON prj_project_requirements(project_id, status);
CREATE INDEX idx_prj_project_requirements_assigned_status ON prj_project_requirements(assigned_to, status);

CREATE INDEX idx_prj_project_time_tracking_project_user ON prj_project_time_tracking(project_id, user_id);
CREATE INDEX idx_prj_project_time_tracking_project_activity ON prj_project_time_tracking(project_id, activity_type);
CREATE INDEX idx_prj_project_time_tracking_user_date ON prj_project_time_tracking(user_id, start_time);

CREATE INDEX idx_prj_project_documents_project_type ON prj_project_documents(project_id, document_type);
CREATE INDEX idx_prj_project_documents_project_status ON prj_project_documents(project_id, review_status);
CREATE INDEX idx_prj_project_documents_author_status ON prj_project_documents(author_id, review_status);
```

### 4. Script de Inicialización (Datos Demo)

```sql
-- Insertar clientes demo
INSERT INTO prj_clients (name, description, contact_email, contact_phone, address, industry, company_size) VALUES
('TechCorp Solutions', 'Empresa de soluciones tecnológicas para el sector financiero', 'contact@techcorp.com', '+34 91 123 4567', 'Calle Mayor 123, Madrid', 'Tecnología', '500-1000 empleados'),
('DataFlow Analytics', 'Especialistas en análisis de datos y business intelligence', 'info@dataflow.com', '+34 93 987 6543', 'Avenida Diagonal 456, Barcelona', 'Consultoría', '100-500 empleados'),
('AI Innovations Lab', 'Laboratorio de innovación en inteligencia artificial', 'hello@ailab.com', '+34 94 555 1234', 'Plaza Circular 789, Bilbao', 'Investigación', '50-100 empleados'),
('CloudTech Systems', 'Proveedor de soluciones cloud y DevOps', 'sales@cloudtech.com', '+34 95 777 8888', 'Calle Real 321, Sevilla', 'Tecnología', '200-500 empleados'),
('SmartCity Solutions', 'Soluciones inteligentes para ciudades del futuro', 'info@smartcity.com', '+34 96 111 2222', 'Paseo Marítimo 654, Valencia', 'Infraestructura', '100-200 empleados');

-- Insertar proyectos demo
INSERT INTO prj_projects (name, description, project_code, project_type, status, owner_id, client_id, department_id, start_date, end_date, budget) VALUES
('Sistema de Análisis de Riesgos Financieros', 'Sistema de IA para análisis y predicción de riesgos en operaciones financieras', 'PRJ-001', 'AI_DEVELOPMENT', 'IN_PROGRESS',
 (SELECT id FROM cor_users WHERE username = 'admin'),
 (SELECT id FROM prj_clients WHERE name = 'TechCorp Solutions'),
 (SELECT id FROM cor_departments WHERE name = 'Desarrollo'),
 '2024-01-15', '2024-06-30', 150000.00),

('Plataforma de Business Intelligence', 'Plataforma de análisis de datos con dashboards interactivos y reportes automáticos', 'PRJ-002', 'CODE_DEVELOPMENT', 'PLANNING',
 (SELECT id FROM cor_users WHERE username = 'manager'),
 (SELECT id FROM prj_clients WHERE name = 'DataFlow Analytics'),
 (SELECT id FROM cor_departments WHERE name = 'Análisis'),
 '2024-03-01', '2024-08-31', 200000.00),

('Motor de Recomendaciones IA', 'Sistema de recomendaciones basado en machine learning para e-commerce', 'PRJ-003', 'AI_DEVELOPMENT', 'IN_PROGRESS',
 (SELECT id FROM cor_users WHERE username = 'developer'),
 (SELECT id FROM prj_clients WHERE name = 'AI Innovations Lab'),
 (SELECT id FROM cor_departments WHERE name = 'Investigación'),
 '2024-02-01', '2024-07-31', 120000.00),

('Migración a Infraestructura Cloud', 'Migración completa de sistemas legacy a arquitectura cloud nativa', 'PRJ-004', 'PRODUCTION', 'IN_PROGRESS',
 (SELECT id FROM cor_users WHERE username = 'admin'),
 (SELECT id FROM prj_clients WHERE name = 'CloudTech Systems'),
 (SELECT id FROM cor_departments WHERE name = 'Infraestructura'),
 '2024-01-01', '2024-12-31', 300000.00),

('Sistema de Gestión de Tráfico Inteligente', 'Sistema de IA para optimización de semáforos y gestión de tráfico en tiempo real', 'PRJ-005', 'AI_DEVELOPMENT', 'PLANNING',
 (SELECT id FROM cor_users WHERE username = 'manager'),
 (SELECT id FROM prj_clients WHERE name = 'SmartCity Solutions'),
 (SELECT id FROM cor_departments WHERE name = 'Desarrollo'),
 '2024-04-01', '2024-11-30', 250000.00);

-- Insertar roles de proyecto demo
INSERT INTO prj_project_roles (project_id, user_id, role, permissions) VALUES
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-001'), (SELECT id FROM cor_users WHERE username = 'admin'), 'OWNER', '{"all": true}'),
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-001'), (SELECT id FROM cor_users WHERE username = 'manager'), 'ADMIN', '{"read": true, "write": true, "delete": true}'),
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-001'), (SELECT id FROM cor_users WHERE username = 'developer'), 'DEVELOPER', '{"read": true, "write": true}'),
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-001'), (SELECT id FROM cor_users WHERE username = 'tester'), 'TESTER', '{"read": true, "write": true}'),

((SELECT id FROM prj_projects WHERE project_code = 'PRJ-002'), (SELECT id FROM cor_users WHERE username = 'manager'), 'OWNER', '{"all": true}'),
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-002'), (SELECT id FROM cor_users WHERE username = 'developer'), 'DEVELOPER', '{"read": true, "write": true}'),
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-002'), (SELECT id FROM cor_users WHERE username = 'analyst'), 'REVIEWER', '{"read": true, "write": true}'),

((SELECT id FROM prj_projects WHERE project_code = 'PRJ-003'), (SELECT id FROM cor_users WHERE username = 'developer'), 'OWNER', '{"all": true}'),
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-003'), (SELECT id FROM cor_users WHERE username = 'researcher'), 'DEVELOPER', '{"read": true, "write": true}'),
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-003'), (SELECT id FROM cor_users WHERE username = 'tester'), 'TESTER', '{"read": true, "write": true}');

-- Insertar miembros de proyecto demo
INSERT INTO prj_project_members (project_id, user_id, role, join_date) VALUES
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-001'), (SELECT id FROM cor_users WHERE username = 'admin'), 'OWNER', '2024-01-15'),
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-001'), (SELECT id FROM cor_users WHERE username = 'manager'), 'ADMIN', '2024-01-16'),
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-001'), (SELECT id FROM cor_users WHERE username = 'developer'), 'DEVELOPER', '2024-01-17'),
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-001'), (SELECT id FROM cor_users WHERE username = 'tester'), 'TESTER', '2024-01-18'),

((SELECT id FROM prj_projects WHERE project_code = 'PRJ-002'), (SELECT id FROM cor_users WHERE username = 'manager'), 'OWNER', '2024-03-01'),
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-002'), (SELECT id FROM cor_users WHERE username = 'developer'), 'DEVELOPER', '2024-03-02'),
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-002'), (SELECT id FROM cor_users WHERE username = 'analyst'), 'REVIEWER', '2024-03-03'),

((SELECT id FROM prj_projects WHERE project_code = 'PRJ-003'), (SELECT id FROM cor_users WHERE username = 'developer'), 'OWNER', '2024-02-01'),
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-003'), (SELECT id FROM cor_users WHERE username = 'researcher'), 'DEVELOPER', '2024-02-02'),
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-003'), (SELECT id FROM cor_users WHERE username = 'tester'), 'TESTER', '2024-02-03');

-- Insertar tokens de proyecto demo
INSERT INTO prj_project_tokens (project_id, user_id, token_type, token_value, expires_at) VALUES
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-001'), (SELECT id FROM cor_users WHERE username = 'admin'), 'AI_DEVELOPMENT', 'encrypted_token_prj001_ai_dev_2024', '2025-01-15 00:00:00'),
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-001'), (SELECT id FROM cor_users WHERE username = 'developer'), 'CODE_DEVELOPMENT', 'encrypted_token_prj001_code_dev_2024', '2025-01-15 00:00:00'),
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-002'), (SELECT id FROM cor_users WHERE username = 'manager'), 'CODE_DEVELOPMENT', 'encrypted_token_prj002_code_dev_2024', '2025-03-01 00:00:00'),
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-003'), (SELECT id FROM cor_users WHERE username = 'developer'), 'AI_DEVELOPMENT', 'encrypted_token_prj003_ai_dev_2024', '2025-02-01 00:00:00');

-- Insertar requisitos de proyecto demo
INSERT INTO prj_project_requirements (project_id, requirement_code, requirement_type, title, description, priority, status, acceptance_criteria, business_value, story_points, assigned_to, estimated_hours, due_date) VALUES
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-001'), 'REQ-001-001', 'FUNCTIONAL', 'Análisis de Datos Financieros', 'Sistema debe analizar datos financieros en tiempo real y generar alertas de riesgo', 'HIGH', 'IN_PROGRESS',
 '{"criteria": ["Procesar datos en tiempo real", "Generar alertas automáticas", "Mostrar métricas de riesgo"], "success_metrics": ["Latencia < 100ms", "Precisión > 95%"]}', 9, 8,
 (SELECT id FROM cor_users WHERE username = 'developer'), 40.0, '2024-04-15'),

((SELECT id FROM prj_projects WHERE project_code = 'PRJ-001'), 'REQ-001-002', 'NON_FUNCTIONAL', 'Rendimiento del Sistema', 'El sistema debe manejar 1000 transacciones por segundo', 'CRITICAL', 'APPROVED',
 '{"criteria": ["Throughput > 1000 TPS", "Response time < 200ms", "Uptime > 99.9%"], "performance_tests": ["Load testing", "Stress testing"]}', 10, 5,
 (SELECT id FROM cor_users WHERE username = 'developer'), 24.0, '2024-04-30'),

((SELECT id FROM prj_projects WHERE project_code = 'PRJ-002'), 'REQ-002-001', 'USER_STORY', 'Dashboard Interactivo', 'Como usuario, quiero ver un dashboard interactivo con métricas clave del negocio', 'HIGH', 'DRAFT',
 '{"criteria": ["Visualización de KPIs", "Filtros dinámicos", "Exportación de datos"], "user_acceptance": ["Usuario puede navegar intuitivamente", "Datos se actualizan en tiempo real"]}', 8, 13,
 (SELECT id FROM cor_users WHERE username = 'developer'), 60.0, '2024-05-15'),

((SELECT id FROM prj_projects WHERE project_code = 'PRJ-003'), 'REQ-003-001', 'FEATURE', 'Algoritmo de Recomendaciones', 'Implementar algoritmo de machine learning para recomendaciones personalizadas', 'HIGH', 'IN_PROGRESS',
 '{"criteria": ["Precisión > 90%", "Personalización por usuario", "Aprendizaje continuo"], "ml_metrics": ["Precision", "Recall", "F1-Score"]}', 9, 21,
 (SELECT id FROM cor_users WHERE username = 'researcher'), 80.0, '2024-05-30');

-- Insertar tareas de proyecto demo
INSERT INTO prj_project_tasks (project_id, requirement_id, task_code, title, description, task_type, priority, status, assigned_to, reporter_id, estimated_hours, story_points, due_date, progress_percentage) VALUES
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-001'),
 (SELECT id FROM prj_project_requirements WHERE requirement_code = 'REQ-001-001'),
 'TASK-001-001', 'Implementar API de Análisis', 'Crear endpoints REST para análisis de datos financieros', 'FEATURE', 'HIGH', 'IN_PROGRESS',
 (SELECT id FROM cor_users WHERE username = 'developer'), (SELECT id FROM cor_users WHERE username = 'admin'), 16.0, 2, '2024-04-10', 75),

((SELECT id FROM prj_projects WHERE project_code = 'PRJ-001'),
 (SELECT id FROM prj_project_requirements WHERE requirement_code = 'REQ-001-001'),
 'TASK-001-002', 'Crear Modelo de IA', 'Desarrollar modelo de machine learning para detección de riesgos', 'FEATURE', 'CRITICAL', 'TO_DO',
 (SELECT id FROM cor_users WHERE username = 'developer'), (SELECT id FROM cor_users WHERE username = 'admin'), 24.0, 3, '2024-04-20', 0),

((SELECT id FROM prj_projects WHERE project_code = 'PRJ-001'),
 (SELECT id FROM prj_project_requirements WHERE requirement_code = 'REQ-001-002'),
 'TASK-001-003', 'Optimización de Rendimiento', 'Optimizar consultas de base de datos y caché', 'IMPROVEMENT', 'HIGH', 'BACKLOG',
 (SELECT id FROM cor_users WHERE username = 'developer'), (SELECT id FROM cor_users WHERE username = 'manager'), 12.0, 2, '2024-04-25', 0),

((SELECT id FROM prj_projects WHERE project_code = 'PRJ-002'),
 (SELECT id FROM prj_project_requirements WHERE requirement_code = 'REQ-002-001'),
 'TASK-002-001', 'Diseño de UI/UX', 'Crear mockups y prototipos del dashboard', 'DESIGN', 'MEDIUM', 'TO_DO',
 (SELECT id FROM cor_users WHERE username = 'developer'), (SELECT id FROM cor_users WHERE username = 'manager'), 20.0, 3, '2024-05-05', 0),

((SELECT id FROM prj_projects WHERE project_code = 'PRJ-003'),
 (SELECT id FROM prj_project_requirements WHERE requirement_code = 'REQ-003-001'),
 'TASK-003-001', 'Investigación de Algoritmos', 'Investigar algoritmos de recomendación más efectivos', 'RESEARCH', 'HIGH', 'IN_PROGRESS',
 (SELECT id FROM cor_users WHERE username = 'researcher'), (SELECT id FROM cor_users WHERE username = 'developer'), 16.0, 2, '2024-04-15', 60);

-- Insertar comentarios de tareas demo
INSERT INTO prj_project_task_comments (task_id, user_id, comment, comment_type, is_internal) VALUES
((SELECT id FROM prj_project_tasks WHERE task_code = 'TASK-001-001'), (SELECT id FROM cor_users WHERE username = 'developer'), 'API implementada con endpoints básicos. Pendiente validación de datos.', 'STATUS_UPDATE', false),
((SELECT id FROM prj_project_tasks WHERE task_code = 'TASK-001-001'), (SELECT id FROM cor_users WHERE username = 'admin'), 'Excelente progreso. ¿Cuándo estará lista para testing?', 'FEEDBACK', false),
((SELECT id FROM prj_project_tasks WHERE task_code = 'TASK-001-002'), (SELECT id FROM cor_users WHERE username = 'developer'), 'Necesito acceso a los datasets de entrenamiento para comenzar.', 'QUESTION', false),
((SELECT id FROM prj_project_tasks WHERE task_code = 'TASK-002-001'), (SELECT id FROM cor_users WHERE username = 'manager'), 'Prioridad alta para el dashboard. Es clave para la presentación al cliente.', 'STATUS_UPDATE', true);

-- Insertar registros de tiempo demo
INSERT INTO prj_project_time_tracking (project_id, user_id, requirement_id, activity_type, description, start_time, end_time, duration_hours, hourly_rate, total_cost, is_billable) VALUES
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-001'), (SELECT id FROM cor_users WHERE username = 'developer'),
 (SELECT id FROM prj_project_requirements WHERE requirement_code = 'REQ-001-001'),
 'DEVELOPMENT', 'Implementación de endpoints de análisis', '2024-04-01 09:00:00', '2024-04-01 17:00:00', 8.0, 45.00, 360.00, true),

((SELECT id FROM prj_projects WHERE project_code = 'PRJ-001'), (SELECT id FROM cor_users WHERE username = 'developer'),
 (SELECT id FROM prj_project_requirements WHERE requirement_code = 'REQ-001-001'),
 'TESTING', 'Testing de endpoints implementados', '2024-04-02 09:00:00', '2024-04-02 12:00:00', 3.0, 45.00, 135.00, true),

((SELECT id FROM prj_projects WHERE project_code = 'PRJ-003'), (SELECT id FROM cor_users WHERE username = 'researcher'),
 (SELECT id FROM prj_project_requirements WHERE requirement_code = 'REQ-003-001'),
 'RESEARCH', 'Investigación de algoritmos de recomendación', '2024-04-01 10:00:00', '2024-04-01 18:00:00', 8.0, 50.00, 400.00, true),

((SELECT id FROM prj_projects WHERE project_code = 'PRJ-003'), (SELECT id FROM cor_users WHERE username = 'researcher'),
 (SELECT id FROM prj_project_requirements WHERE requirement_code = 'REQ-003-001'),
 'ANALYSIS', 'Análisis de papers científicos sobre recomendaciones', '2024-04-02 09:00:00', '2024-04-02 15:00:00', 6.0, 50.00, 300.00, true);

-- Insertar documentos de proyecto demo
INSERT INTO prj_project_documents (project_id, document_type, title, content, content_hash, version, author_id, review_status) VALUES
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-001'), 'TECHNICAL_SPEC', 'Especificación Técnica del Sistema de Análisis de Riesgos',
 'Este documento describe la arquitectura técnica del sistema de análisis de riesgos financieros...', 'hash_tech_spec_prj001_v1', '1.0',
 (SELECT id FROM cor_users WHERE username = 'admin'), 'APPROVED'),

((SELECT id FROM prj_projects WHERE project_code = 'PRJ-001'), 'API_DOCS', 'Documentación de la API de Análisis',
 'Documentación completa de los endpoints REST para análisis de datos financieros...', 'hash_api_docs_prj001_v1', '1.0',
 (SELECT id FROM cor_users WHERE username = 'developer'), 'IN_REVIEW'),

((SELECT id FROM prj_projects WHERE project_code = 'PRJ-002'), 'FUNCTIONAL_SPEC', 'Especificación Funcional del Dashboard',
 'Especificación detallada de las funcionalidades del dashboard de business intelligence...', 'hash_func_spec_prj002_v1', '1.0',
 (SELECT id FROM cor_users WHERE username = 'manager'), 'DRAFT'),

((SELECT id FROM prj_projects WHERE project_code = 'PRJ-003'), 'ARCHITECTURE', 'Arquitectura del Sistema de Recomendaciones',
 'Documento de arquitectura del motor de recomendaciones basado en machine learning...', 'hash_arch_prj003_v1', '1.0',
 (SELECT id FROM cor_users WHERE username = 'developer'), 'APPROVED');

-- Insertar versiones de proyecto demo
INSERT INTO prj_project_versions (project_id, version_number, version_type, release_notes, changelog, deployment_status) VALUES
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-001'), '1.0.0', 'MAJOR', 'Primera versión del sistema de análisis de riesgos',
 '{"features": ["API de análisis básico", "Modelo de IA inicial", "Dashboard de métricas"], "fixes": [], "breaking_changes": []}', 'DEPLOYED'),

((SELECT id FROM prj_projects WHERE project_code = 'PRJ-001'), '1.1.0', 'MINOR', 'Mejoras en rendimiento y nuevas métricas',
 '{"features": ["Optimización de consultas", "Nuevas métricas de riesgo", "Mejoras en UI"], "fixes": ["Bug en cálculo de métricas"], "breaking_changes": []}', 'PENDING'),

((SELECT id FROM prj_projects WHERE project_code = 'PRJ-003'), '0.1.0', 'PRE_RELEASE', 'Prototipo inicial del motor de recomendaciones',
 '{"features": ["Algoritmo básico de recomendaciones", "API de testing", "Métricas de precisión"], "fixes": [], "breaking_changes": []}', 'DEPLOYED');

-- Insertar dominios de proyecto demo
INSERT INTO prj_project_domains (project_id, domain_id, domain_role, usage_intensity, rag_integration, data_requirements) VALUES
((SELECT id FROM prj_projects WHERE project_code = 'PRJ-001'),
 (SELECT id FROM dmn_domains WHERE name = 'Finanzas'), 'PRIMARY', 'HIGH', true,
 '{"data_types": ["transacciones", "balances", "ratios"], "update_frequency": "real_time", "retention_period": "7_years"}'),

((SELECT id FROM prj_projects WHERE project_code = 'PRJ-002'),
 (SELECT id FROM dmn_domains WHERE name = 'Ventas'), 'PRIMARY', 'MEDIUM', true,
 '{"data_types": ["ventas", "clientes", "productos"], "update_frequency": "daily", "retention_period": "3_years"}'),

((SELECT id FROM prj_projects WHERE project_code = 'PRJ-003'),
 (SELECT id FROM dmn_domains WHERE name = 'E-commerce'), 'PRIMARY', 'HIGH', true,
 '{"data_types": ["comportamiento_usuario", "historial_compras", "productos"], "update_frequency": "real_time", "retention_period": "2_years"}');
```

## Servicios de Project Management

### ProjectService

```java
@Service
@Transactional
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectTechnologyService technologyService;

    @Autowired
    private ProjectStackService stackService;

    @Autowired
    private ProjectMemberService memberService;

    @Autowired
    private ProjectLicenseService licenseService;

    @Autowired
    private ProjectTokenService tokenService;

    @Autowired
    private AuditService auditService;

    public Project createProject(ProjectDto projectDto) {
        // Validar datos del proyecto
        validateProjectData(projectDto);

        // Generar código único del proyecto
        String projectCode = generateUniqueProjectCode(projectDto.getName());

        // Crear proyecto
        Project project = new Project();
        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());
        project.setProjectCode(projectCode);
        project.setProjectType(projectDto.getProjectType());
        project.setStatus(ProjectStatus.PLANNING);
        project.setOwnerId(getCurrentUserId());
        project.setClientId(projectDto.getClientId());
        project.setDepartmentId(projectDto.getDepartmentId());
        project.setStartDate(projectDto.getStartDate());
        project.setEndDate(projectDto.getEndDate());
        project.setBudget(projectDto.getBudget());
        project.setIsActive(true);
        project.setCreatedAt(LocalDateTime.now());

        Project savedProject = projectRepository.save(project);

        // Asignar tecnologías si se especifican
        if (projectDto.getTechnologies() != null) {
            technologyService.assignTechnologiesToProject(savedProject.getId(), projectDto.getTechnologies());
        }

        // Asignar stacks si se especifican
        if (projectDto.getStacks() != null) {
            stackService.assignStacksToProject(savedProject.getId(), projectDto.getStacks());
        }

        // Crear licencia por defecto
        licenseService.createDefaultLicense(savedProject.getId());

        // Generar tokens del proyecto
        tokenService.generateProjectTokens(savedProject.getId());

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "CREATE_PROJECT",
            "PROJECT",
            savedProject.getId().toString()
        );

        return savedProject;
    }

    public ProjectTechnology addTechnologyToProject(Long projectId, ProjectTechnologyDto techDto) {
        // Validar que el proyecto existe
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -&gt; new ProjectNotFoundException("Project not found"));

        // Añadir tecnología al proyecto
        ProjectTechnology projectTech = technologyService.addTechnology(projectId, techDto);

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "ADD_TECHNOLOGY_TO_PROJECT",
            "PROJECT_TECHNOLOGY",
            projectTech.getId().toString()
        );

        return projectTech;
    }

    public ProjectStack addStackToProject(Long projectId, ProjectStackDto stackDto) {
        // Validar que el proyecto existe
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -&gt; new ProjectNotFoundException("Project not found"));

        // Añadir stack al proyecto
        ProjectStack projectStack = stackService.addStack(projectId, stackDto);

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "ADD_STACK_TO_PROJECT",
            "PROJECT_STACK",
            projectStack.getId().toString()
        );

        return projectStack;
    }

    public ProjectMember addMemberToProject(Long projectId, ProjectMemberDto memberDto) {
        // Validar que el proyecto existe
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -&gt; new ProjectNotFoundException("Project not found"));

        // Validar permisos para añadir miembros
        validateMemberManagementPermissions(projectId);

        // Añadir miembro al proyecto
        ProjectMember member = memberService.addMember(projectId, memberDto);

        // Generar tokens específicos para el nuevo miembro
        tokenService.generateUserProjectTokens(projectId, member.getUserId());

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "ADD_MEMBER_TO_PROJECT",
            "PROJECT_MEMBER",
            member.getId().toString()
        );

        return member;
    }

    private void validateProjectData(ProjectDto projectDto) {
        if (projectDto.getName() == null || projectDto.getName().trim().isEmpty()) {
            throw new ValidationException("Project name is required");
        }

        if (projectDto.getProjectType() == null) {
            throw new ValidationException("Project type is required");
        }

        if (projectDto.getClientId() == null) {
            throw new ValidationException("Client ID is required");
        }

        if (projectDto.getDepartmentId() == null) {
            throw new ValidationException("Department ID is required");
        }
    }

    private String generateUniqueProjectCode(String projectName) {
        // Generar código único basado en el nombre del proyecto
        String baseCode = projectName.toUpperCase().replaceAll("[^A-Z0-9]", "");
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(8);
        return baseCode + "_" + timestamp;
    }

    private void validateMemberManagementPermissions(Long projectId) {
        // Validar que el usuario actual tiene permisos para gestionar miembros
        // Implementar lógica de validación
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

### Prometheus Metrics

```java
@Component
public class ProjectMetrics {

    @Autowired
    private MeterRegistry meterRegistry;

    private final Counter projectsCreatedCounter;
    private final Counter projectsActivatedCounter;
    private final Counter projectsCompletedCounter;
    private final Counter technologiesAddedCounter;
    private final Counter stacksAddedCounter;
    private final Counter membersAddedCounter;
    private final Timer projectCreationTimer;
    private final Timer technologyAssignmentTimer;

    public ProjectMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.projectsCreatedCounter = Counter.builder("projects.created")
            .description("Total projects created")
            .register(meterRegistry);

        this.projectsActivatedCounter = Counter.builder("projects.activated")
            .description("Total projects activated")
            .register(meterRegistry);

        this.projectsCompletedCounter = Counter.builder("projects.completed")
            .description("Total projects completed")
            .register(meterRegistry);

        this.technologiesAddedCounter = Counter.builder("projects.technologies.added")
            .description("Total technologies added to projects")
            .register(meterRegistry);

        this.stacksAddedCounter = Counter.builder("projects.stacks.added")
            .description("Total stacks added to projects")
            .register(meterRegistry);

        this.membersAddedCounter = Counter.builder("projects.members.added")
            .description("Total members added to projects")
            .register(meterRegistry);

        this.projectCreationTimer = Timer.builder("projects.creation.time")
            .description("Project creation time")
            .register(meterRegistry);

        this.technologyAssignmentTimer = Timer.builder("projects.technology.assignment.time")
            .description("Technology assignment time")
            .register(meterRegistry);
    }

    public void incrementProjectsCreated() {
        projectsCreatedCounter.increment();
    }

    public void incrementProjectsActivated() {
        projectsActivatedCounter.increment();
    }

    public void incrementProjectsCompleted() {
        projectsCompletedCounter.increment();
    }

    public void incrementTechnologiesAdded() {
        technologiesAddedCounter.increment();
    }

    public void incrementStacksAdded() {
        stacksAddedCounter.increment();
    }

    public void incrementMembersAdded() {
        membersAddedCounter.increment();
    }

    public Timer.Sample startProjectCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startTechnologyAssignmentTimer() {
        return Timer.start(meterRegistry);
    }
}
```

## Configuración de Aplicación

### application-project-management.yml

```yaml
project-management:
  # Configuración de proyectos
  projects:
    auto-code-generation: true
    code-prefix: "PRJ"
    max-name-length: 255
    default-status: "PLANNING"
    auto-owner-assignment: true
    max-projects-per-user: 50
    max-budget: 1000000.00

  # Configuración de requisitos
  requirements:
    auto-code-generation: true
    code-prefix: "REQ"
    max-title-length: 255
    default-status: "DRAFT"
    auto-assignment: false
    max-requirements-per-project: 200
    story-points-scale: [1, 2, 3, 5, 8, 13, 21, 34]
    business-value-scale: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

  # Configuración de tareas
  tasks:
    auto-code-generation: true
    code-prefix: "TASK"
    max-title-length: 255
    default-status: "BACKLOG"
    auto-assignment: false
    max-tasks-per-project: 1000
    max-subtasks-per-task: 10
    progress-tracking: true
    time-tracking: true
    story-points-scale: [1, 2, 3, 5, 8, 13, 21, 34]
    max-dependencies-per-task: 20

  # Configuración de dominios
  domains:
    max-domains-per-project: 50
    rag-integration-enabled: true
    auto-compliance-check: true
    data-retention-policies:
      default: "7_years"
      financial: "10_years"
      healthcare: "25_years"
      legal: "permanent"

  # Configuración de versiones
  versions:
    semver-enforcement: true
    auto-versioning: false
    max-versions-per-project: 100
    deployment-approval-required: true
    rollback-enabled: true
    changelog-required: true
    release-notes-required: true

  # Configuración de documentos
  documents:
    max-file-size-mb: 50
    supported-formats: ["pdf", "docx", "txt", "md", "html"]
    rag-indexing-enabled: true
    auto-review-assignment: true
    version-control: true
    max-versions-per-document: 10
    content-hash-algorithm: "SHA-256"

  # Configuración de seguimiento de tiempo
  time-tracking:
    enabled: true
    auto-calculation: true
    max-hours-per-day: 24
    max-hours-per-week: 168
    overtime-tracking: true
    billable-hours-tracking: true
    hourly-rate-required: true
    max-hourly-rate: 1000.00

  # Configuración de tecnologías
  technologies:
    auto-version-detection: true
    dependency-resolution: true
    conflict-detection: true
    max-technologies-per-project: 50
    auto-deployment-config: true

  # Configuración de stacks
  stacks:
    auto-deployment-config: true
    environment-isolation: true
    resource-allocation: true
    scaling-configuration: true

  # Configuración de licencias
  licenses:
    encryption-algorithm: "AES-256-GCM"
    key-rotation-days: 90
    auto-validation: true
    offline-validation: true
    max-file-size-mb: 10

  # Configuración de tokens
  tokens:
    default-expiration-days: 365
    auto-rotation: true
    scope-validation: true
    usage-tracking: true
    max-tokens-per-user: 10

  # Configuración de miembros
  members:
    role-inheritance: true
    permission-cascade: true
    auto-notification: true
    max-members-per-project: 100

  # Configuración de auditoría
  audit:
    log-all-actions: true
    sensitive-data-masking: true
    retention-days: 2555
    export-capability: true

  # Configuración de RAG
  rag:
    enabled: true
    auto-indexing: false
    content-processing: true
    similarity-threshold: 0.8
    max-results: 50
    cache-enabled: true
    cache-ttl-minutes: 60

  # Configuración de generación automática
  auto-generation:
    requirements:
      enabled: true
      ai-model: "gpt-4"
      max-tokens: 2000
      temperature: 0.7

    architecture:
      enabled: true
      ai-model: "gpt-4"
      max-tokens: 3000
      temperature: 0.5

    documentation:
      enabled: true
      ai-model: "gpt-4"
      max-tokens: 2500
      temperature: 0.6

    test-cases:
      enabled: true
      ai-model: "gpt-4"
      max-tokens: 1500
      temperature: 0.8

  # Configuración de notificaciones
  notifications:
    task-assignments: true
    deadline-reminders: true
    status-changes: true
    progress-updates: true
    time-tracking-alerts: true
    document-reviews: true
    version-deployments: true

  # Configuración de reportes
  reports:
    time-tracking-summary: true
    project-progress: true
    task-burndown: true
    team-performance: true
    cost-analysis: true
    requirement-coverage: true
    domain-usage: true

  # Configuración de integración
  integration:
    ide-plugins: true
    webhooks: true
    api-rate-limiting: true
    max-requests-per-minute: 1000
    webhook-timeout-seconds: 30
    retry-attempts: 3
```

## Pendiente

hay que incorporar una gestion de requisitos y casos de uso para que nuestro modulo de NLP pueda generar la aplicacion. los diferentes dominos que se incluyen en el proyecto, las normativas a aplicar al proyecto , un cntrol de versiones del proyecto, relase y gestion del cambio , documentos tecnicos , funcionales , requistos …. integrados con el rag para realizar busquedas y consultas , tambien incluir una parte de imputacion de horas y cost

## Conclusión

El módulo de Project Management proporciona un sistema completo de gestión de proyectos de IA, incluyendo:

- **Gestión de Proyectos**: Creación, configuración y gestión del ciclo de vida de proyectos
- **Tecnologías Asociadas**: Gestión de tecnologías específicas por proyecto
- **Stacks Arquitectónicos**: Configuración de stacks tecnológicos y arquitecturas
- **Gestión de Miembros**: Control de acceso y roles de usuarios en proyectos
- **Licencias Encriptadas**: Sistema de licencias seguro para self-hosting
- **Tokens de Acceso**: Gestión de tokens específicos para diferentes servicios
- **Integración con IDEs**: Conexión con herramientas de desarrollo externas
- **Endpoints de Serving**: Asociación de endpoints con proyectos específicos

El sistema está diseñado para ser el núcleo central que controla el acceso a todos los recursos del portal, proporcionando un control granular de licencias, permisos y recursos por proyecto.

## Servicios Java para Nuevas Entidades

### 1. ProjectRequirementService

```java
@Service
@Transactional
public class ProjectRequirementService {

    @Autowired
    private ProjectRequirementRepository requirementRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AuditService auditService;

    public ProjectRequirement createRequirement(Long projectId, ProjectRequirementDto dto) {
        // Validar que el proyecto existe
        Project project = projectService.getProjectById(projectId);
        if (project == null) {
            throw new ResourceNotFoundException("Proyecto no encontrado");
        }

        // Generar código único del requisito
        String requirementCode = generateRequirementCode(projectId);

        ProjectRequirement requirement = new ProjectRequirement();
        requirement.setProjectId(projectId);
        requirement.setRequirementCode(requirementCode);
        requirement.setRequirementType(dto.getRequirementType());
        requirement.setTitle(dto.getTitle());
        requirement.setDescription(dto.getDescription());
        requirement.setPriority(dto.getPriority());
        requirement.setStatus(RequirementStatus.DRAFT);
        requirement.setAcceptanceCriteria(dto.getAcceptanceCriteria());
        requirement.setBusinessValue(dto.getBusinessValue());
        requirement.setStoryPoints(dto.getStoryPoints());
        requirement.setAssignedTo(dto.getAssignedTo());
        requirement.setEstimatedHours(dto.getEstimatedHours());
        requirement.setDueDate(dto.getDueDate());

        ProjectRequirement saved = requirementRepository.save(requirement);

        // Auditoría
        auditService.logAction("CREATE_REQUIREMENT", "PROJECT_REQUIREMENT",
            saved.getId(), "Requisito creado: " + dto.getTitle());

        return saved;
    }

    public ProjectRequirement updateRequirement(Long projectId, Long requirementId, ProjectRequirementDto dto) {
        ProjectRequirement requirement = getRequirementById(projectId, requirementId);

        requirement.setTitle(dto.getTitle());
        requirement.setDescription(dto.getDescription());
        requirement.setPriority(dto.getPriority());
        requirement.setAcceptanceCriteria(dto.getAcceptanceCriteria());
        requirement.setBusinessValue(dto.getBusinessValue());
        requirement.setStoryPoints(dto.getStoryPoints());
        requirement.setAssignedTo(dto.getAssignedTo());
        requirement.setEstimatedHours(dto.getEstimatedHours());
        requirement.setDueDate(dto.getDueDate());

        ProjectRequirement updated = requirementRepository.save(requirement);

        // Auditoría
        auditService.logAction("UPDATE_REQUIREMENT", "PROJECT_REQUIREMENT",
            updated.getId(), "Requisito actualizado: " + dto.getTitle());

        return updated;
    }

    public void deleteRequirement(Long projectId, Long requirementId) {
        ProjectRequirement requirement = getRequirementById(projectId, requirementId);

        requirementRepository.delete(requirement);

        // Auditoría
        auditService.logAction("DELETE_REQUIREMENT", "PROJECT_REQUIREMENT",
            requirementId, "Requisito eliminado: " + requirement.getTitle());
    }

    public ProjectRequirement assignRequirement(Long projectId, Long requirementId, Long userId) {
        ProjectRequirement requirement = getRequirementById(projectId, requirementId);
        requirement.setAssignedTo(userId);

        ProjectRequirement updated = requirementRepository.save(requirement);

        // Auditoría
        auditService.logAction("ASSIGN_REQUIREMENT", "PROJECT_REQUIREMENT",
            requirementId, "Requisito asignado a usuario: " + userId);

        return updated;
    }

    public ProjectRequirement updateRequirementStatus(Long projectId, Long requirementId, RequirementStatus status) {
        ProjectRequirement requirement = getRequirementById(projectId, requirementId);
        requirement.setStatus(status);

        if (status == RequirementStatus.COMPLETED) {
            requirement.setCompletedDate(LocalDate.now());
        }

        ProjectRequirement updated = requirementRepository.save(requirement);

        // Auditoría
        auditService.logAction("UPDATE_REQUIREMENT_STATUS", "PROJECT_REQUIREMENT",
            requirementId, "Estado actualizado a: " + status);

        return updated;
    }

    public List<ProjectRequirement> getRequirementsByProject(Long projectId) {
        return requirementRepository.findByProjectIdAndIsActiveTrue(projectId);
    }

    public List<ProjectRequirement> getRequirementsByStatus(Long projectId, RequirementStatus status) {
        return requirementRepository.findByProjectIdAndStatusAndIsActiveTrue(projectId, status);
    }

    public List<ProjectRequirement> getRequirementsByPriority(Long projectId, RequirementPriority priority) {
        return requirementRepository.findByProjectIdAndPriorityAndIsActiveTrue(projectId, priority);
    }

    public List<ProjectRequirement> getRequirementsByAssignee(Long projectId, Long userId) {
        return requirementRepository.findByProjectIdAndAssignedToAndIsActiveTrue(projectId, userId);
    }

    public List<ProjectRequirement> searchRequirements(Long projectId, String searchTerm) {
        return requirementRepository.searchByProjectAndTerm(projectId, searchTerm);
    }

    private String generateRequirementCode(Long projectId) {
        Project project = projectService.getProjectById(projectId);
        String projectCode = project.getProjectCode().replace("PRJ-", "");

        // Contar requisitos existentes para este proyecto
        long count = requirementRepository.countByProjectId(projectId);

        return String.format("REQ-%s-%03d", projectCode, count + 1);
    }

    private ProjectRequirement getRequirementById(Long projectId, Long requirementId) {
        return requirementRepository.findByProjectIdAndIdAndIsActiveTrue(projectId, requirementId)
            .orElseThrow(() -> new ResourceNotFoundException("Requisito no encontrado"));
    }
}
```

### 2. ProjectTaskService

```java
@Service
@Transactional
public class ProjectTaskService {

    @Autowired
    private ProjectTaskRepository taskRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AuditService auditService;

    public ProjectTask createTask(Long projectId, ProjectTaskDto dto) {
        // Validar que el proyecto existe
        Project project = projectService.getProjectById(projectId);
        if (project == null) {
            throw new ResourceNotFoundException("Proyecto no encontrado");
        }

        // Generar código único de la tarea
        String taskCode = generateTaskCode(projectId);

        ProjectTask task = new ProjectTask();
        task.setProjectId(projectId);
        task.setRequirementId(dto.getRequirementId());
        task.setParentTaskId(dto.getParentTaskId());
        task.setTaskCode(taskCode);
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setTaskType(dto.getTaskType());
        task.setPriority(dto.getPriority());
        task.setStatus(TaskStatus.BACKLOG);
        task.setAssignedTo(dto.getAssignedTo());
        task.setReporterId(dto.getReporterId());
        task.setEstimatedHours(dto.getEstimatedHours());
        task.setStoryPoints(dto.getStoryPoints());
        task.setDueDate(dto.getDueDate());
        task.setProgressPercentage(0);
        task.setDependencies(dto.getDependencies());
        task.setTags(dto.getTags());
        task.setAttachments(dto.getAttachments());

        ProjectTask saved = taskRepository.save(task);

        // Auditoría
        auditService.logAction("CREATE_TASK", "PROJECT_TASK",
            saved.getId(), "Tarea creada: " + dto.getTitle());

        return saved;
    }

    public ProjectTask updateTask(Long projectId, Long taskId, ProjectTaskDto dto) {
        ProjectTask task = getTaskById(projectId, taskId);

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setTaskType(dto.getTaskType());
        task.setPriority(dto.getPriority());
        task.setAssignedTo(dto.getAssignedTo());
        task.setEstimatedHours(dto.getEstimatedHours());
        task.setStoryPoints(dto.getStoryPoints());
        task.setDueDate(dto.getDueDate());
        task.setDependencies(dto.getDependencies());
        task.setTags(dto.getTags());
        task.setAttachments(dto.getAttachments());

        ProjectTask updated = taskRepository.save(task);

        // Auditoría
        auditService.logAction("UPDATE_TASK", "PROJECT_TASK",
            taskId, "Tarea actualizada: " + dto.getTitle());

        return updated;
    }

    public ProjectTask assignTask(Long projectId, Long taskId, Long userId) {
        ProjectTask task = getTaskById(projectId, taskId);
        task.setAssignedTo(userId);

        ProjectTask updated = taskRepository.save(task);

        // Auditoría
        auditService.logAction("ASSIGN_TASK", "PROJECT_TASK",
            taskId, "Tarea asignada a usuario: " + userId);

        return updated;
    }

    public ProjectTask updateTaskStatus(Long projectId, Long taskId, TaskStatus status) {
        ProjectTask task = getTaskById(projectId, taskId);
        task.setStatus(status);

        if (status == TaskStatus.DONE) {
            task.setCompletedDate(LocalDate.now());
            task.setProgressPercentage(100);
        } else if (status == TaskStatus.IN_PROGRESS) {
            task.setStartDate(LocalDate.now());
        }

        ProjectTask updated = taskRepository.save(task);

        // Auditoría
        auditService.logAction("UPDATE_TASK_STATUS", "PROJECT_TASK",
            taskId, "Estado de tarea actualizado a: " + status);

        return updated;
    }

    public ProjectTask updateTaskProgress(Long projectId, Long taskId, Integer progressPercentage) {
        ProjectTask task = getTaskById(projectId, taskId);

        if (progressPercentage < 0 || progressPercentage > 100) {
            throw new IllegalArgumentException("El progreso debe estar entre 0 y 100");
        }

        task.setProgressPercentage(progressPercentage);

        if (progressPercentage == 100) {
            task.setStatus(TaskStatus.DONE);
            task.setCompletedDate(LocalDate.now());
        } else if (progressPercentage > 0 && task.getStatus() == TaskStatus.BACKLOG) {
            task.setStatus(TaskStatus.IN_PROGRESS);
            if (task.getStartDate() == null) {
                task.setStartDate(LocalDate.now());
            }
        }

        ProjectTask updated = taskRepository.save(task);

        // Auditoría
        auditService.logAction("UPDATE_TASK_PROGRESS", "PROJECT_TASK",
            taskId, "Progreso de tarea actualizado a: " + progressPercentage + "%");

        return updated;
    }

    public ProjectTask blockTask(Long projectId, Long taskId, String blockReason) {
        ProjectTask task = getTaskById(projectId, taskId);
        task.setIsBlocked(true);
        task.setBlockReason(blockReason);
        task.setStatus(TaskStatus.BLOCKED);

        ProjectTask updated = taskRepository.save(task);

        // Auditoría
        auditService.logAction("BLOCK_TASK", "PROJECT_TASK",
            taskId, "Tarea bloqueada: " + blockReason);

        return updated;
    }

    public ProjectTask unblockTask(Long projectId, Long taskId) {
        ProjectTask task = getTaskById(projectId, taskId);
        task.setIsBlocked(false);
        task.setBlockReason(null);
        task.setStatus(TaskStatus.TO_DO);

        ProjectTask updated = taskRepository.save(task);

        // Auditoría
        auditService.logAction("UNBLOCK_TASK", "PROJECT_TASK",
            taskId, "Tarea desbloqueada");

        return updated;
    }

    public List<ProjectTask> getTasksByProject(Long projectId) {
        return taskRepository.findByProjectIdAndIsActiveTrue(projectId);
    }

    public List<ProjectTask> getTasksByStatus(Long projectId, TaskStatus status) {
        return taskRepository.findByProjectIdAndStatusAndIsActiveTrue(projectId, status);
    }

    public List<ProjectTask> getTasksByPriority(Long projectId, TaskPriority priority) {
        return taskRepository.findByProjectIdAndPriorityAndIsActiveTrue(projectId, priority);
    }

    public List<ProjectTask> getTasksByAssignee(Long projectId, Long userId) {
        return taskRepository.findByProjectIdAndAssignedToAndIsActiveTrue(projectId, userId);
    }

    public List<ProjectTask> getTasksByRequirement(Long projectId, Long requirementId) {
        return taskRepository.findByProjectIdAndRequirementIdAndIsActiveTrue(projectId, requirementId);
    }

    public List<ProjectTask> getBacklogTasks(Long projectId) {
        return taskRepository.findByProjectIdAndStatusAndIsActiveTrue(projectId, TaskStatus.BACKLOG);
    }

    public List<ProjectTask> getSprintTasks(Long projectId) {
        return taskRepository.findByProjectIdAndStatusInAndIsActiveTrue(projectId,
            Arrays.asList(TaskStatus.TO_DO, TaskStatus.IN_PROGRESS, TaskStatus.IN_REVIEW, TaskStatus.TESTING));
    }

    public List<ProjectTask> getBlockedTasks(Long projectId) {
        return taskRepository.findByProjectIdAndIsBlockedTrueAndIsActiveTrue(projectId);
    }

    public TaskAnalytics getTaskAnalytics(Long projectId) {
        List<ProjectTask> tasks = getTasksByProject(projectId);

        long totalTasks = tasks.size();
        long completedTasks = tasks.stream().filter(t -> t.getStatus() == TaskStatus.DONE).count();
        long blockedTasks = tasks.stream().filter(ProjectTask::getIsBlocked).count();

        BigDecimal totalEstimatedHours = tasks.stream()
            .map(ProjectTask::getEstimatedHours)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalActualHours = tasks.stream()
            .map(ProjectTask::getActualHours)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new TaskAnalytics(totalTasks, completedTasks, blockedTasks, totalEstimatedHours, totalActualHours);
    }

    private String generateTaskCode(Long projectId) {
        Project project = projectService.getProjectById(projectId);
        String projectCode = project.getProjectCode().replace("PRJ-", "");

        // Contar tareas existentes para este proyecto
        long count = taskRepository.countByProjectId(projectId);

        return String.format("TASK-%s-%03d", projectCode, count + 1);
    }

    private ProjectTask getTaskById(Long projectId, Long taskId) {
        return taskRepository.findByProjectIdAndIdAndIsActiveTrue(projectId, taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada"));
    }
}
```

## Métricas Prometheus

### ProjectManagementMetrics

```java
@Component
public class ProjectManagementMetrics {

    private final MeterRegistry meterRegistry;

    // Contadores para proyectos
    private final Counter projectsCreatedCounter;
    private final Counter projectsUpdatedCounter;
    private final Counter projectsDeletedCounter;
    private final Counter projectsCompletedCounter;

    // Contadores para requisitos
    private final Counter requirementsCreatedCounter;
    private final Counter requirementsUpdatedCounter;
    private final Counter requirementsDeletedCounter;
    private final Counter requirementsCompletedCounter;
    private final Counter requirementsApprovedCounter;
    private final Counter requirementsRejectedCounter;

    // Contadores para tareas
    private final Counter tasksCreatedCounter;
    private final Counter tasksUpdatedCounter;
    private final Counter tasksDeletedCounter;
    private final Counter tasksCompletedCounter;
    private final Counter tasksAssignedCounter;
    private final Counter tasksBlockedCounter;
    private final Counter tasksUnblockedCounter;

    // Contadores para dominios
    private final Counter domainsAssociatedCounter;
    private final Counter domainsRemovedCounter;
    private final Counter ragIntegrationsCounter;

    // Contadores para versiones
    private final Counter versionsCreatedCounter;
    private final Counter versionsDeployedCounter;
    private final Counter versionsRolledBackCounter;
    private final Counter deploymentsFailedCounter;

    // Contadores para documentos
    private final Counter documentsCreatedCounter;
    private final Counter documentsUpdatedCounter;
    private final Counter documentsDeletedCounter;
    private final Counter documentsReviewedCounter;
    private final Counter documentsApprovedCounter;
    private final Counter documentsRejectedCounter;
    private final Counter ragIndexedDocumentsCounter;

    // Contadores para seguimiento de tiempo
    private final Counter timeEntriesCreatedCounter;
    private final Counter timeEntriesUpdatedCounter;
    private final Counter timeEntriesDeletedCounter;
    private final Counter billableHoursCounter;
    private final Counter nonBillableHoursCounter;

    // Timers para operaciones
    private final Timer projectCreationTimer;
    private final Timer requirementCreationTimer;
    private final Timer taskCreationTimer;
    private final Timer versionDeploymentTimer;
    private final Timer documentReviewTimer;
    private final Timer ragIndexingTimer;
    private final Timer timeTrackingTimer;

    // Gauges para métricas en tiempo real
    private final Gauge activeProjectsGauge;
    private final Gauge activeRequirementsGauge;
    private final Gauge activeTasksGauge;
    private final Gauge blockedTasksGauge;
    private final Gauge overdueTasksGauge;
    private final Gauge ragIndexedDocumentsGauge;
    private final Gauge totalProjectHoursGauge;

    public ProjectManagementMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Inicializar contadores
        this.projectsCreatedCounter = Counter.builder("projects.created")
            .description("Número de proyectos creados")
            .register(meterRegistry);

        this.projectsUpdatedCounter = Counter.builder("projects.updated")
            .description("Número de proyectos actualizados")
            .register(meterRegistry);

        this.projectsDeletedCounter = Counter.builder("projects.deleted")
            .description("Número de proyectos eliminados")
            .register(meterRegistry);

        this.projectsCompletedCounter = Counter.builder("projects.completed")
            .description("Número de proyectos completados")
            .register(meterRegistry);

        this.requirementsCreatedCounter = Counter.builder("requirements.created")
            .description("Número de requisitos creados")
            .register(meterRegistry);

        this.requirementsUpdatedCounter = Counter.builder("requirements.updated")
            .description("Número de requisitos actualizados")
            .register(meterRegistry);

        this.requirementsDeletedCounter = Counter.builder("requirements.deleted")
            .description("Número de requisitos eliminados")
            .register(meterRegistry);

        this.requirementsCompletedCounter = Counter.builder("requirements.completed")
            .description("Número de requisitos completados")
            .register(meterRegistry);

        this.requirementsApprovedCounter = Counter.builder("requirements.approved")
            .description("Número de requisitos aprobados")
            .register(meterRegistry);

        this.requirementsRejectedCounter = Counter.builder("requirements.rejected")
            .description("Número de requisitos rechazados")
            .register(meterRegistry);

        this.tasksCreatedCounter = Counter.builder("tasks.created")
            .description("Número de tareas creadas")
            .register(meterRegistry);

        this.tasksUpdatedCounter = Counter.builder("tasks.updated")
            .description("Número de tareas actualizadas")
            .register(meterRegistry);

        this.tasksDeletedCounter = Counter.builder("tasks.deleted")
            .description("Número de tareas eliminadas")
            .register(meterRegistry);

        this.tasksCompletedCounter = Counter.builder("tasks.completed")
            .description("Número de tareas completadas")
            .register(meterRegistry);

        this.tasksAssignedCounter = Counter.builder("tasks.assigned")
            .description("Número de tareas asignadas")
            .register(meterRegistry);

        this.tasksBlockedCounter = Counter.builder("tasks.blocked")
            .description("Número de tareas bloqueadas")
            .register(meterRegistry);

        this.tasksUnblockedCounter = Counter.builder("tasks.unblocked")
            .description("Número de tareas desbloqueadas")
            .register(meterRegistry);

        this.domainsAssociatedCounter = Counter.builder("domains.associated")
            .description("Número de dominios asociados a proyectos")
            .register(meterRegistry);

        this.domainsRemovedCounter = Counter.builder("domains.removed")
            .description("Número de dominios removidos de proyectos")
            .register(meterRegistry);

        this.ragIntegrationsCounter = Counter.builder("rag.integrations")
            .description("Número de integraciones RAG configuradas")
            .register(meterRegistry);

        this.versionsCreatedCounter = Counter.builder("versions.created")
            .description("Número de versiones creadas")
            .register(meterRegistry);

        this.versionsDeployedCounter = Counter.builder("versions.deployed")
            .description("Número de versiones desplegadas")
            .register(meterRegistry);

        this.versionsRolledBackCounter = Counter.builder("versions.rolled_back")
            .description("Número de versiones revertidas")
            .register(meterRegistry);

        this.deploymentsFailedCounter = Counter.builder("deployments.failed")
            .description("Número de despliegues fallidos")
            .register(meterRegistry);

        this.documentsCreatedCounter = Counter.builder("documents.created")
            .description("Número de documentos creados")
            .register(meterRegistry);

        this.documentsUpdatedCounter = Counter.builder("documents.updated")
            .description("Número de documentos actualizados")
            .register(meterRegistry);

        this.documentsDeletedCounter = Counter.builder("documents.deleted")
            .description("Número de documentos eliminados")
            .register(meterRegistry);

        this.documentsReviewedCounter = Counter.builder("documents.reviewed")
            .description("Número de documentos revisados")
            .register(meterRegistry);

        this.documentsApprovedCounter = Counter.builder("documents.approved")
            .description("Número de documentos aprobados")
            .register(meterRegistry);

        this.documentsRejectedCounter = Counter.builder("documents.rejected")
            .description("Número de documentos rechazados")
            .register(meterRegistry);

        this.ragIndexedDocumentsCounter = Counter.builder("rag.documents_indexed")
            .description("Número de documentos indexados en RAG")
            .register(meterRegistry);

        this.timeEntriesCreatedCounter = Counter.builder("time_entries.created")
            .description("Número de entradas de tiempo creadas")
            .register(meterRegistry);

        this.timeEntriesUpdatedCounter = Counter.builder("time_entries.updated")
            .description("Número de entradas de tiempo actualizadas")
            .register(meterRegistry);

        this.timeEntriesDeletedCounter = Counter.builder("time_entries.deleted")
            .description("Número de entradas de tiempo eliminadas")
            .register(meterRegistry);

        this.billableHoursCounter = Counter.builder("time.billable_hours")
            .description("Horas facturables registradas")
            .register(meterRegistry);

        this.nonBillableHoursCounter = Counter.builder("time.non_billable_hours")
            .description("Horas no facturables registradas")
            .register(meterRegistry);

        // Inicializar timers
        this.projectCreationTimer = Timer.builder("projects.creation.time")
            .description("Tiempo de creación de proyectos")
            .register(meterRegistry);

        this.requirementCreationTimer = Timer.builder("requirements.creation.time")
            .description("Tiempo de creación de requisitos")
            .register(meterRegistry);

        this.taskCreationTimer = Timer.builder("tasks.creation.time")
            .description("Tiempo de creación de tareas")
            .register(meterRegistry);

        this.versionDeploymentTimer = Timer.builder("versions.deployment.time")
            .description("Tiempo de despliegue de versiones")
            .register(meterRegistry);

        this.documentReviewTimer = Timer.builder("documents.review.time")
            .description("Tiempo de revisión de documentos")
            .register(meterRegistry);

        this.ragIndexingTimer = Timer.builder("rag.indexing.time")
            .description("Tiempo de indexación en RAG")
            .register(meterRegistry);

        this.timeTrackingTimer = Timer.builder("time.tracking.time")
            .description("Tiempo de registro de tiempo")
            .register(meterRegistry);

        // Inicializar gauges
        this.activeProjectsGauge = Gauge.builder("projects.active")
            .description("Número de proyectos activos")
            .register(meterRegistry, this, ProjectManagementMetrics::getActiveProjectsCount);

        this.activeRequirementsGauge = Gauge.builder("requirements.active")
            .description("Número de requisitos activos")
            .register(meterRegistry, this, ProjectManagementMetrics::getActiveRequirementsCount);

        this.activeTasksGauge = Gauge.builder("tasks.active")
            .description("Número de tareas activas")
            .register(meterRegistry, this, ProjectManagementMetrics::getActiveTasksCount);

        this.blockedTasksGauge = Gauge.builder("tasks.blocked")
            .description("Número de tareas bloqueadas")
            .register(meterRegistry, this, ProjectManagementMetrics::getBlockedTasksCount);

        this.overdueTasksGauge = Gauge.builder("tasks.overdue")
            .description("Número de tareas vencidas")
            .register(meterRegistry, this, ProjectManagementMetrics::getOverdueTasksCount);

        this.ragIndexedDocumentsGauge = Gauge.builder("rag.documents_indexed_count")
            .description("Número total de documentos indexados en RAG")
            .register(meterRegistry, this, ProjectManagementMetrics::getRagIndexedDocumentsCount);

        this.totalProjectHoursGauge = Gauge.builder("time.total_hours")
            .description("Total de horas registradas en proyectos")
            .register(meterRegistry, this, ProjectManagementMetrics::getTotalProjectHours);
    }

    // Métodos para incrementar contadores
    public void incrementProjectsCreated() {
        projectsCreatedCounter.increment();
    }

    public void incrementProjectsUpdated() {
        projectsUpdatedCounter.increment();
    }

    public void incrementProjectsDeleted() {
        projectsDeletedCounter.increment();
    }

    public void incrementProjectsCompleted() {
        projectsCompletedCounter.increment();
    }

    public void incrementRequirementsCreated() {
        requirementsCreatedCounter.increment();
    }

    public void incrementRequirementsUpdated() {
        requirementsUpdatedCounter.increment();
    }

    public void incrementRequirementsDeleted() {
        requirementsDeletedCounter.increment();
    }

    public void incrementRequirementsCompleted() {
        requirementsCompletedCounter.increment();
    }

    public void incrementRequirementsApproved() {
        requirementsApprovedCounter.increment();
    }

    public void incrementRequirementsRejected() {
        requirementsRejectedCounter.increment();
    }

    public void incrementTasksCreated() {
        tasksCreatedCounter.increment();
    }

    public void incrementTasksUpdated() {
        tasksUpdatedCounter.increment();
    }

    public void incrementTasksDeleted() {
        tasksDeletedCounter.increment();
    }

    public void incrementTasksCompleted() {
        tasksCompletedCounter.increment();
    }

    public void incrementTasksAssigned() {
        tasksAssignedCounter.increment();
    }

    public void incrementTasksBlocked() {
        tasksBlockedCounter.increment();
    }

    public void incrementTasksUnblocked() {
        tasksUnblockedCounter.increment();
    }

    public void incrementDomainsAssociated() {
        domainsAssociatedCounter.increment();
    }

    public void incrementDomainsRemoved() {
        domainsRemovedCounter.increment();
    }

    public void incrementRagIntegrations() {
        ragIntegrationsCounter.increment();
    }

    public void incrementVersionsCreated() {
        versionsCreatedCounter.increment();
    }

    public void incrementVersionsDeployed() {
        versionsDeployedCounter.increment();
    }

    public void incrementVersionsRolledBack() {
        versionsRolledBackCounter.increment();
    }

    public void incrementDeploymentsFailed() {
        deploymentsFailedCounter.increment();
    }

    public void incrementDocumentsCreated() {
        documentsCreatedCounter.increment();
    }

    public void incrementDocumentsUpdated() {
        documentsUpdatedCounter.increment();
    }

    public void incrementDocumentsDeleted() {
        documentsDeletedCounter.increment();
    }

    public void incrementDocumentsReviewed() {
        documentsReviewedCounter.increment();
    }

    public void incrementDocumentsApproved() {
        documentsApprovedCounter.increment();
    }

    public void incrementDocumentsRejected() {
        documentsRejectedCounter.increment();
    }

    public void incrementRagIndexedDocuments() {
        ragIndexedDocumentsCounter.increment();
    }

    public void incrementTimeEntriesCreated() {
        timeEntriesCreatedCounter.increment();
    }

    public void incrementTimeEntriesUpdated() {
        timeEntriesUpdatedCounter.increment();
    }

    public void incrementTimeEntriesDeleted() {
        timeEntriesDeletedCounter.increment();
    }

    public void incrementBillableHours(BigDecimal hours) {
        billableHoursCounter.increment(hours.doubleValue());
    }

    public void incrementNonBillableHours(BigDecimal hours) {
        nonBillableHoursCounter.increment(hours.doubleValue());
    }

    // Métodos para iniciar timers
    public Timer.Sample startProjectCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startRequirementCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startTaskCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startVersionDeploymentTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startDocumentReviewTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startRagIndexingTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startTimeTrackingTimer() {
        return Timer.start(meterRegistry);
    }

    // Métodos para obtener valores de gauges (implementar con repositorios)
    private double getActiveProjectsCount() {
        // Implementar con ProjectRepository.countByStatusAndIsActiveTrue(ProjectStatus.IN_PROGRESS)
        return 0.0;
    }

    private double getActiveRequirementsCount() {
        // Implementar con ProjectRequirementRepository.countByStatusInAndIsActiveTrue
        return 0.0;
    }

    private double getActiveTasksCount() {
        // Implementar con ProjectTaskRepository.countByStatusInAndIsActiveTrue
        return 0.0;
    }

    private double getBlockedTasksCount() {
        // Implementar con ProjectTaskRepository.countByIsBlockedTrueAndIsActiveTrue
        return 0.0;
    }

    private double getOverdueTasksCount() {
        // Implementar con ProjectTaskRepository.countByDueDateBeforeAndStatusNotAndIsActiveTrue
        return 0.0;
    }

    private double getRagIndexedDocumentsCount() {
        // Implementar con ProjectDocumentRepository.countByIsRagIndexedTrueAndIsActiveTrue
        return 0.0;
    }

    private double getTotalProjectHours() {
        // Implementar con ProjectTimeTrackingRepository.sumDurationHoursByProjectId
        return 0.0;
    }
}
```
