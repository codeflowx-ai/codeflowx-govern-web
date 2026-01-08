# Módulo de Roadmap - Portal Backend

## Descripción General

El módulo de Roadmap gestiona la planificación estratégica del producto, incluyendo features, releases, milestones y feedback de usuarios. Es fundamental para la gestión del producto y la comunicación con stakeholders.

## Arquitectura del Sistema

### Concepto Clave

El sistema de roadmap proporciona:

- **Gestión de features** y funcionalidades del producto
- **Planificación de releases** y versiones
- **Seguimiento de milestones** y objetivos
- **Feedback de usuarios** y priorización
- **Análisis de mercado** y competencia
- **Roadmaps visuales** para diferentes audiencias
- **Integración con proyectos** y recursos

### Flujo de Roadmap

```
Ideas de Usuario → Feature Request → Análisis → Priorización
    ↓
Planificación de Release → Desarrollo → Testing → Deploy
    ↓
Feedback y Métricas → Iteración → Nuevo Ciclo
```

## Entidades del Sistema

### 1. Feature

```java
@Entity
@Table(name = "features")
public class Feature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "feature_code")
    private String featureCode; // Código único del feature

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private FeatureCategory category;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private FeaturePriority priority;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private FeatureStatus status;

    @Column(name = "effort_estimate")
    private String effortEstimate; // Story points o días

    @Column(name = "business_value")
    private Integer businessValue; // 1-10 escala

    @Column(name = "user_impact")
    private Integer userImpact; // 1-10 escala

    @Column(name = "technical_complexity")
    private Integer technicalComplexity; // 1-10 escala

    @Column(name = "dependencies")
    private String dependencies; // JSON array de feature IDs

    @Column(name = "acceptance_criteria")
    private String acceptanceCriteria; // JSON array

    @Column(name = "assigned_team")
    private String assignedTeam;

    @Column(name = "assigned_developer")
    private Long assignedDeveloper; // Referencia a users.id

    @Column(name = "target_release_id")
    private Long targetReleaseId; // Referencia a releases.id

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "estimated_completion")
    private LocalDate estimatedCompletion;

    @Column(name = "actual_completion")
    private LocalDate actualCompletion;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relaciones
    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL)
    private List<FeatureFeedback> feedback = new ArrayList<>();

    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL)
    private List<FeatureRequirement> requirements = new ArrayList<>();
}

public enum FeatureCategory {
    USER_INTERFACE,      // Mejoras de UI/UX
    FUNCTIONALITY,       // Nueva funcionalidad
    PERFORMANCE,         // Optimizaciones
    SECURITY,            // Mejoras de seguridad
    INTEGRATION,         // Integraciones externas
    INFRASTRUCTURE,      // Infraestructura
    ANALYTICS,           // Analytics y reportes
    MOBILE,              // Funcionalidades móviles
    API,                 // APIs y endpoints
    AUTOMATION           // Automatización
}

public enum FeaturePriority {
    LOW,                 // Baja prioridad
    MEDIUM,              // Prioridad media
    HIGH,                // Alta prioridad
    CRITICAL,            // Crítica
    BLOCKER              // Bloqueante
}

public enum FeatureStatus {
    IDEA,                // Solo una idea
    RESEARCH,            // En investigación
    DESIGN,              // En diseño
    DEVELOPMENT,         // En desarrollo
    TESTING,             // En testing
    REVIEW,              // En revisión
    READY,               // Listo para deploy
    DEPLOYED,            // Desplegado
    CANCELLED,           // Cancelado
    ARCHIVED             // Archivado
}
```

### 2. Release

```java
@Entity
@Table(name = "releases")
public class Release {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "version")
    private String version; // SemVer format

    @Column(name = "release_type")
    @Enumerated(EnumType.STRING)
    private ReleaseType type;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ReleaseStatus status;

    @Column(name = "target_date")
    private LocalDate targetDate;

    @Column(name = "actual_date")
    private LocalDate actualDate;

    @Column(name = "freeze_date")
    private LocalDate freezeDate; // Feature freeze

    @Column(name = "release_notes")
    private String releaseNotes;

    @Column(name = "changelog")
    private String changelog; // JSON con cambios

    @Column(name = "is_major")
    private Boolean isMajor = false;

    @Column(name = "is_breaking")
    private Boolean isBreaking = false;

    @Column(name = "created_by")
    private Long createdBy; // Referencia a users.id

    @Column(name = "approved_by")
    private Long approvedBy; // Referencia a users.id

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relaciones
    @OneToMany(mappedBy = "release", cascade = CascadeType.ALL)
    private List<ReleaseFeature> features = new ArrayList<>();
}

public enum ReleaseType {
    MAJOR,               // Versión mayor (breaking changes)
    MINOR,               // Versión menor (nuevas features)
    PATCH,               // Parche (bug fixes)
    HOTFIX,              // Hotfix crítico
    ALPHA,               // Alpha testing
    BETA,                // Beta testing
    RC                   // Release candidate
}

public enum ReleaseStatus {
    PLANNING,            // En planificación
    DEVELOPMENT,         // En desarrollo
    TESTING,             // En testing
    FREEZE,              // Feature freeze
    STAGING,             // En staging
    READY,               // Listo para deploy
    DEPLOYED,            // Desplegado
    CANCELLED            // Cancelado
}
```

### 3. Milestone

```java
@Entity
@Table(name = "milestones")
public class Milestone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "milestone_type")
    @Enumerated(EnumType.STRING)
    private MilestoneType type;

    @Column(name = "target_date")
    private LocalDate targetDate;

    @Column(name = "actual_date")
    private LocalDate actualDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MilestoneStatus status;

    @Column(name = "progress_percentage")
    private Integer progressPercentage = 0;

    @Column(name = "dependencies")
    private String dependencies; // JSON array de milestone IDs

    @Column(name = "success_criteria")
    private String successCriteria; // JSON array

    @Column(name = "assigned_owner")
    private Long assignedOwner; // Referencia a users.id

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum MilestoneType {
    SPRINT,              // Sprint de desarrollo
    QUARTER,             // Objetivo trimestral
    PROJECT,             // Hito de proyecto
    BUSINESS,            // Hito de negocio
    TECHNICAL,           // Hito técnico
    COMPLIANCE,          // Hito de cumplimiento
    MARKETING            // Hito de marketing
}

public enum MilestoneStatus {
    NOT_STARTED,         // No iniciado
    IN_PROGRESS,         // En progreso
    COMPLETED,           // Completado
    DELAYED,             // Retrasado
    CANCELLED            // Cancelado
}
```

### 4. FeatureFeedback

```java
@Entity
@Table(name = "feature_feedback")
public class FeatureFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "feature_id")
    private Long featureId; // Referencia a features.id

    @Column(name = "user_id")
    private Long userId; // Referencia a users.id

    @Column(name = "feedback_type")
    @Enumerated(EnumType.STRING)
    private FeedbackType type;

    @Column(name = "rating")
    private Integer rating; // 1-5 escala

    @Column
    private String comment;

    @Column(name = "use_case")
    private String useCase; // Caso de uso del usuario

    @Column(name = "business_impact")
    private String businessImpact; // Impacto en el negocio

    @Column(name = "is_urgent")
    private Boolean isUrgent = false;

    @Column(name = "is_feature_request")
    private Boolean isFeatureRequest = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum FeedbackType {
    BUG_REPORT,          // Reporte de bug
    FEATURE_REQUEST,     // Solicitud de feature
    IMPROVEMENT,         // Sugerencia de mejora
    COMPLIMENT,          // Comentario positivo
    COMPLAINT,           // Queja
    QUESTION,            // Pregunta
    GENERAL              // Comentario general
}
```

### 5. MarketAnalysis

```java
@Entity
@Table(name = "market_analysis")
public class MarketAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(name = "analysis_date")
    private LocalDate analysisDate;

    @Column(name = "market_segment")
    private String marketSegment;

    @Column(name = "competitor_analysis")
    private String competitorAnalysis; // JSON con análisis

    @Column(name = "market_trends")
    private String marketTrends; // JSON con tendencias

    @Column(name = "opportunities")
    private String opportunities; // JSON con oportunidades

    @Column(name = "threats")
    private String threats; // JSON con amenazas

    @Column(name = "recommendations")
    private String recommendations; // JSON con recomendaciones

    @Column(name = "created_by")
    private Long createdBy; // Referencia a users.id

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

## API Endpoints

### Feature Management

```
GET    /api/v1/features
GET    /api/v1/features/{id}
POST   /api/v1/features
PUT    /api/v1/features/{id}
DELETE /api/v1/features/{id}
GET    /api/v1/features/category/{category}
GET    /api/v1/features/priority/{priority}
GET    /api/v1/features/status/{status}
GET    /api/v1/features/release/{releaseId}
POST   /api/v1/features/{id}/assign
POST   /api/v1/features/{id}/update-status
```

### Release Management

```
GET    /api/v1/releases
GET    /api/v1/releases/{id}
POST   /api/v1/releases
PUT    /api/v1/releases/{id}
DELETE /api/v1/releases/{id}
GET    /api/v1/releases/type/{type}
GET    /api/v1/releases/status/{status}
POST   /api/v1/releases/{id}/approve
POST   /api/v1/releases/{id}/deploy
GET    /api/v1/releases/roadmap
```

### Milestone Management

```
GET    /api/v1/milestones
GET    /api/v1/milestones/{id}
POST   /api/v1/milestones
PUT    /api/v1/milestones/{id}
DELETE /api/v1/milestones/{id}
GET    /api/v1/milestones/type/{type}
GET    /api/v1/milestones/status/{status}
POST   /api/v1/milestones/{id}/update-progress
```

### Feedback Management

```
GET    /api/v1/feature-feedback
GET    /api/v1/feature-feedback/{id}
POST   /api/v1/feature-feedback
PUT    /api/v1/feature-feedback/{id}
DELETE /api/v1/feature-feedback/{id}
GET    /api/v1/feature-feedback/feature/{featureId}
GET    /api/v1/feature-feedback/user/{userId}
GET    /api/v1/feature-feedback/type/{type}
```

### Market Analysis

```
GET    /api/v1/market-analysis
GET    /api/v1/market-analysis/{id}
POST   /api/v1/market-analysis
PUT    /api/v1/market-analysis/{id}
DELETE /api/v1/market-analysis/{id}
GET    /api/v1/market-analysis/segment/{segment}
GET    /api/v1/market-analysis/date-range
```

### Roadmap Views

```
GET    /api/v1/roadmap/overview
GET    /api/v1/roadmap/quarterly
GET    /api/v1/roadmap/yearly
GET    /api/v1/roadmap/team/{teamId}
GET    /api/v1/roadmap/stakeholder
GET    /api/v1/roadmap/development
```

## Scripts de Base de Datos

### Script de Creación de Tablas

```sql
-- Tabla de features
CREATE TABLE features (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    feature_code VARCHAR(100) UNIQUE NOT NULL,
    category VARCHAR(30) NOT NULL,
    priority VARCHAR(20) DEFAULT 'MEDIUM',
    status VARCHAR(20) DEFAULT 'IDEA',
    effort_estimate VARCHAR(50),
    business_value INTEGER CHECK (business_value >= 1 AND business_value <= 10),
    user_impact INTEGER CHECK (user_impact >= 1 AND user_impact <= 10),
    technical_complexity INTEGER CHECK (technical_complexity >= 1 AND technical_complexity <= 10),
    dependencies TEXT, -- JSON array de feature IDs
    acceptance_criteria TEXT, -- JSON array
    assigned_team VARCHAR(100),
    assigned_developer BIGINT REFERENCES users(id),
    target_release_id BIGINT,
    start_date DATE,
    estimated_completion DATE,
    actual_completion DATE,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla features
COMMENT ON TABLE features IS 'Tabla que almacena las features y funcionalidades del producto';
COMMENT ON COLUMN features.id IS 'Identificador único de la feature';
COMMENT ON COLUMN features.name IS 'Nombre de la feature';
COMMENT ON COLUMN features.description IS 'Descripción detallada de la feature';
COMMENT ON COLUMN features.feature_code IS 'Código único de la feature';
COMMENT ON COLUMN features.category IS 'Categoría de la feature (UI, FUNCTIONALITY, PERFORMANCE, etc.)';
COMMENT ON COLUMN features.priority IS 'Prioridad de la feature (LOW, MEDIUM, HIGH, CRITICAL, BLOCKER)';
COMMENT ON COLUMN features.status IS 'Estado actual de la feature (IDEA, RESEARCH, DEVELOPMENT, etc.)';
COMMENT ON COLUMN features.effort_estimate IS 'Estimación de esfuerzo en story points o días';
COMMENT ON COLUMN features.business_value IS 'Valor de negocio en escala 1-10';
COMMENT ON COLUMN features.user_impact IS 'Impacto en el usuario en escala 1-10';
COMMENT ON COLUMN features.technical_complexity IS 'Complejidad técnica en escala 1-10';
COMMENT ON COLUMN features.dependencies IS 'Dependencias con otras features en formato JSON';
COMMENT ON COLUMN features.acceptance_criteria IS 'Criterios de aceptación en formato JSON';
COMMENT ON COLUMN features.assigned_team IS 'Equipo asignado a la feature';
COMMENT ON COLUMN features.assigned_developer IS 'ID del desarrollador asignado';
COMMENT ON COLUMN features.target_release_id IS 'ID de la release objetivo';
COMMENT ON COLUMN features.start_date IS 'Fecha de inicio del desarrollo';
COMMENT ON COLUMN features.estimated_completion IS 'Fecha estimada de finalización';
COMMENT ON COLUMN features.actual_completion IS 'Fecha real de finalización';
COMMENT ON COLUMN features.is_active IS 'Indica si la feature está activa';
COMMENT ON COLUMN features.created_at IS 'Fecha y hora de creación';
COMMENT ON COLUMN features.updated_at IS 'Fecha y hora de la última actualización';

-- Tabla de releases
CREATE TABLE releases (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    version VARCHAR(50) NOT NULL,
    release_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) DEFAULT 'PLANNING',
    target_date DATE,
    actual_date DATE,
    freeze_date DATE,
    release_notes TEXT,
    changelog TEXT, -- JSON con cambios
    is_major BOOLEAN DEFAULT false,
    is_breaking BOOLEAN DEFAULT false,
    created_by BIGINT REFERENCES users(id),
    approved_by BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla releases
COMMENT ON TABLE releases IS 'Tabla que almacena las releases y versiones del producto';
COMMENT ON COLUMN releases.id IS 'Identificador único de la release';
COMMENT ON COLUMN releases.name IS 'Nombre de la release';
COMMENT ON COLUMN releases.description IS 'Descripción de la release';
COMMENT ON COLUMN releases.version IS 'Versión semántica (SemVer)';
COMMENT ON COLUMN releases.release_type IS 'Tipo de release (MAJOR, MINOR, PATCH, etc.)';
COMMENT ON COLUMN releases.status IS 'Estado de la release (PLANNING, DEVELOPMENT, TESTING, etc.)';
COMMENT ON COLUMN releases.target_date IS 'Fecha objetivo de la release';
COMMENT ON COLUMN releases.actual_date IS 'Fecha real de la release';
COMMENT ON COLUMN releases.freeze_date IS 'Fecha de congelación de features';
COMMENT ON COLUMN releases.release_notes IS 'Notas de la release';
COMMENT ON COLUMN releases.changelog IS 'Changelog en formato JSON';
COMMENT ON COLUMN releases.is_major IS 'Indica si es una release mayor';
COMMENT ON COLUMN releases.is_breaking IS 'Indica si tiene cambios breaking';
COMMENT ON COLUMN releases.created_by IS 'ID del usuario que creó la release';
COMMENT ON COLUMN releases.approved_by IS 'ID del usuario que aprobó la release';
COMMENT ON COLUMN releases.created_at IS 'Fecha y hora de creación';
COMMENT ON COLUMN releases.updated_at IS 'Fecha y hora de la última actualización';

-- Tabla de milestones
CREATE TABLE milestones (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    milestone_type VARCHAR(20) NOT NULL,
    target_date DATE,
    actual_date DATE,
    status VARCHAR(20) DEFAULT 'NOT_STARTED',
    progress_percentage INTEGER DEFAULT 0 CHECK (progress_percentage >= 0 AND progress_percentage <= 100),
    dependencies TEXT, -- JSON array de milestone IDs
    success_criteria TEXT, -- JSON array
    assigned_owner BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla milestones
COMMENT ON TABLE milestones IS 'Tabla que almacena los hitos y objetivos del producto';
COMMENT ON COLUMN milestones.id IS 'Identificador único del milestone';
COMMENT ON COLUMN milestones.name IS 'Nombre del milestone';
COMMENT ON COLUMN milestones.description IS 'Descripción del milestone';
COMMENT ON COLUMN milestones.milestone_type IS 'Tipo de milestone (SPRINT, QUARTER, PROJECT, etc.)';
COMMENT ON COLUMN milestones.target_date IS 'Fecha objetivo del milestone';
COMMENT ON COLUMN milestones.actual_date IS 'Fecha real del milestone';
COMMENT ON COLUMN milestones.status IS 'Estado del milestone (NOT_STARTED, IN_PROGRESS, COMPLETED, etc.)';
COMMENT ON COLUMN milestones.progress_percentage IS 'Porcentaje de progreso (0-100)';
COMMENT ON COLUMN milestones.dependencies IS 'Dependencias con otros milestones en formato JSON';
COMMENT ON COLUMN milestones.success_criteria IS 'Criterios de éxito en formato JSON';
COMMENT ON COLUMN milestones.assigned_owner IS 'ID del usuario responsable del milestone';
COMMENT ON COLUMN milestones.created_at IS 'Fecha y hora de creación';
COMMENT ON COLUMN milestones.updated_at IS 'Fecha y hora de la última actualización';

-- Tabla de feedback de features
CREATE TABLE feature_feedback (
    id BIGSERIAL PRIMARY KEY,
    feature_id BIGINT REFERENCES features(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id),
    feedback_type VARCHAR(20) NOT NULL,
    rating INTEGER CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    use_case TEXT,
    business_impact TEXT,
    is_urgent BOOLEAN DEFAULT false,
    is_feature_request BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla feature_feedback
COMMENT ON TABLE feature_feedback IS 'Tabla que almacena el feedback de los usuarios sobre las features';
COMMENT ON COLUMN feature_feedback.id IS 'Identificador único del feedback';
COMMENT ON COLUMN feature_feedback.feature_id IS 'ID de la feature relacionada';
COMMENT ON COLUMN feature_feedback.user_id IS 'ID del usuario que proporcionó el feedback';
COMMENT ON COLUMN feature_feedback.feedback_type IS 'Tipo de feedback (BUG_REPORT, FEATURE_REQUEST, etc.)';
COMMENT ON COLUMN feature_feedback.rating IS 'Rating del usuario en escala 1-5';
COMMENT ON COLUMN feature_feedback.comment IS 'Comentario del usuario';
COMMENT ON COLUMN feature_feedback.use_case IS 'Caso de uso del usuario';
COMMENT ON COLUMN feature_feedback.business_impact IS 'Impacto en el negocio';
COMMENT ON COLUMN feature_feedback.is_urgent IS 'Indica si el feedback es urgente';
COMMENT ON COLUMN feature_feedback.is_feature_request IS 'Indica si es una solicitud de feature';
COMMENT ON COLUMN feature_feedback.created_at IS 'Fecha y hora de creación';
COMMENT ON COLUMN feature_feedback.updated_at IS 'Fecha y hora de la última actualización';

-- Tabla de análisis de mercado
CREATE TABLE market_analysis (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    analysis_date DATE,
    market_segment VARCHAR(100),
    competitor_analysis TEXT, -- JSON con análisis
    market_trends TEXT, -- JSON con tendencias
    opportunities TEXT, -- JSON con oportunidades
    threats TEXT, -- JSON con amenazas
    recommendations TEXT, -- JSON con recomendaciones
    created_by BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla market_analysis
COMMENT ON TABLE market_analysis IS 'Tabla que almacena los análisis de mercado y competencia';
COMMENT ON COLUMN market_analysis.id IS 'Identificador único del análisis';
COMMENT ON COLUMN market_analysis.title IS 'Título del análisis';
COMMENT ON COLUMN market_analysis.description IS 'Descripción del análisis';
COMMENT ON COLUMN market_analysis.analysis_date IS 'Fecha del análisis';
COMMENT ON COLUMN market_analysis.market_segment IS 'Segmento de mercado analizado';
COMMENT ON COLUMN market_analysis.competitor_analysis IS 'Análisis de competidores en formato JSON';
COMMENT ON COLUMN market_analysis.market_trends IS 'Tendencias de mercado en formato JSON';
COMMENT ON COLUMN market_analysis.opportunities IS 'Oportunidades identificadas en formato JSON';
COMMENT ON COLUMN market_analysis.threats IS 'Amenazas identificadas en formato JSON';
COMMENT ON COLUMN market_analysis.recommendations IS 'Recomendaciones en formato JSON';
COMMENT ON COLUMN market_analysis.created_by IS 'ID del usuario que creó el análisis';
COMMENT ON COLUMN market_analysis.created_at IS 'Fecha y hora de creación';
COMMENT ON COLUMN market_analysis.updated_at IS 'Fecha y hora de la última actualización';

-- Tabla de relación release-feature
CREATE TABLE release_features (
    release_id BIGINT REFERENCES releases(id) ON DELETE CASCADE,
    feature_id BIGINT REFERENCES features(id) ON DELETE CASCADE,
    added_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    removed_date TIMESTAMP,
    is_active BOOLEAN DEFAULT true,
    PRIMARY KEY (release_id, feature_id)
);

-- Comentarios de la tabla release_features
COMMENT ON TABLE release_features IS 'Tabla de relación entre releases y features';
COMMENT ON COLUMN release_features.release_id IS 'ID de la release';
COMMENT ON COLUMN release_features.feature_id IS 'ID de la feature';
COMMENT ON COLUMN release_features.added_date IS 'Fecha de adición a la release';
COMMENT ON COLUMN release_features.removed_date IS 'Fecha de remoción de la release';
COMMENT ON COLUMN release_features.is_active IS 'Indica si la relación está activa';

-- Índices para optimización
CREATE INDEX idx_features_code ON features(feature_code);
CREATE INDEX idx_features_category ON features(category);
CREATE INDEX idx_features_priority ON features(priority);
CREATE INDEX idx_features_status ON features(status);
CREATE INDEX idx_features_release ON features(target_release_id);
CREATE INDEX idx_features_developer ON features(assigned_developer);
CREATE INDEX idx_features_active ON features(is_active);
CREATE INDEX idx_releases_version ON releases(version);
CREATE INDEX idx_releases_type ON releases(release_type);
CREATE INDEX idx_releases_status ON releases(status);
CREATE INDEX idx_releases_target_date ON releases(target_date);
CREATE INDEX idx_milestones_type ON milestones(milestone_type);
CREATE INDEX idx_milestones_status ON milestones(status);
CREATE INDEX idx_milestones_target_date ON milestones(target_date);
CREATE INDEX idx_feedback_feature ON feature_feedback(feature_id);
CREATE INDEX idx_feedback_user ON feature_feedback(user_id);
CREATE INDEX idx_feedback_type ON feature_feedback(feedback_type);
CREATE INDEX idx_market_analysis_segment ON market_analysis(market_segment);
CREATE INDEX idx_market_analysis_date ON market_analysis(analysis_date);
CREATE INDEX idx_release_features_release ON release_features(release_id);
CREATE INDEX idx_release_features_feature ON release_features(feature_id);
```

## Servicios de Roadmap

### RoadmapService

```java
@Service
@Transactional
public class RoadmapService {

    @Autowired
    private FeatureRepository featureRepository;

    @Autowired
    private ReleaseRepository releaseRepository;

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private FeatureFeedbackRepository feedbackRepository;

    @Autowired
    private MarketAnalysisRepository marketAnalysisRepository;

    @Autowired
    private AuditService auditService;

    public Feature createFeature(FeatureDto featureDto) {
        // Validar datos de la feature
        validateFeatureData(featureDto);

        // Generar código único de la feature
        String featureCode = generateUniqueFeatureCode(featureDto.getName());

        // Crear feature
        Feature feature = new Feature();
        feature.setName(featureDto.getName());
        feature.setDescription(featureDto.getDescription());
        feature.setFeatureCode(featureCode);
        feature.setCategory(featureDto.getCategory());
        feature.setPriority(featureDto.getPriority());
        feature.setStatus(FeatureStatus.IDEA);
        feature.setEffortEstimate(featureDto.getEffortEstimate());
        feature.setBusinessValue(featureDto.getBusinessValue());
        feature.setUserImpact(featureDto.getUserImpact());
        feature.setTechnicalComplexity(featureDto.getTechnicalComplexity());
        feature.setDependencies(featureDto.getDependencies());
        feature.setAcceptanceCriteria(featureDto.getAcceptanceCriteria());
        feature.setAssignedTeam(featureDto.getAssignedTeam());
        feature.setAssignedDeveloper(featureDto.getAssignedDeveloper());
        feature.setTargetReleaseId(featureDto.getTargetReleaseId());
        feature.setStartDate(featureDto.getStartDate());
        feature.setEstimatedCompletion(featureDto.getEstimatedCompletion());
        feature.setIsActive(true);
        feature.setCreatedAt(LocalDateTime.now());

        Feature savedFeature = featureRepository.save(feature);

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "CREATE_FEATURE",
            "FEATURE",
            savedFeature.getId().toString()
        );

        return savedFeature;
    }

    public Release createRelease(ReleaseDto releaseDto) {
        // Validar datos de la release
        validateReleaseData(releaseDto);

        // Crear release
        Release release = new Release();
        release.setName(releaseDto.getName());
        release.setDescription(releaseDto.getDescription());
        release.setVersion(releaseDto.getVersion());
        release.setType(releaseDto.getType());
        release.setStatus(ReleaseStatus.PLANNING);
        release.setTargetDate(releaseDto.getTargetDate());
        release.setFreezeDate(releaseDto.getFreezeDate());
        release.setReleaseNotes(releaseDto.getReleaseNotes());
        release.setIsMajor(releaseDto.getIsMajor());
        release.setIsBreaking(releaseDto.getIsBreaking());
        release.setCreatedBy(getCurrentUserId());
        release.setCreatedAt(LocalDateTime.now());

        Release savedRelease = releaseRepository.save(release);

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "CREATE_RELEASE",
            "RELEASE",
            savedRelease.getId().toString()
        );

        return savedRelease;
    }

    public RoadmapOverview getRoadmapOverview() {
        // Obtener features por estado
        Map<FeatureStatus, Long> featuresByStatus = featureRepository.countByStatus();

        // Obtener releases próximas
        List<Release> upcomingReleases = releaseRepository.findUpcomingReleases();

        // Obtener milestones activos
        List<Milestone> activeMilestones = milestoneRepository.findActiveMilestones();

        // Calcular métricas
        RoadmapMetrics metrics = calculateRoadmapMetrics();

        return new RoadmapOverview(featuresByStatus, upcomingReleases, activeMilestones, metrics);
    }

    public List<Feature> getFeaturesForRelease(Long releaseId) {
        return featureRepository.findByTargetReleaseId(releaseId);
    }

    public void updateFeatureStatus(Long featureId, FeatureStatus newStatus) {
        Feature feature = featureRepository.findById(featureId)
            .orElseThrow(() -> new FeatureNotFoundException("Feature not found"));

        feature.setStatus(newStatus);
        feature.setUpdatedAt(LocalDateTime.now());

        if (newStatus == FeatureStatus.COMPLETED) {
            feature.setActualCompletion(LocalDate.now());
        }

        featureRepository.save(feature);

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "UPDATE_FEATURE_STATUS",
            "FEATURE",
            featureId.toString()
        );
    }

    private void validateFeatureData(FeatureDto featureDto) {
        if (featureDto.getName() == null || featureDto.getName().trim().isEmpty()) {
            throw new ValidationException("Feature name is required");
        }

        if (featureDto.getCategory() == null) {
            throw new ValidationException("Feature category is required");
        }

        if (featureDto.getPriority() == null) {
            throw new ValidationException("Feature priority is required");
        }
    }

    private void validateReleaseData(ReleaseDto releaseDto) {
        if (releaseDto.getName() == null || releaseDto.getName().trim().isEmpty()) {
            throw new ValidationException("Release name is required");
        }

        if (releaseDto.getVersion() == null || releaseDto.getVersion().trim().isEmpty()) {
            throw new ValidationException("Release version is required");
        }

        if (releaseDto.getType() == null) {
            throw new ValidationException("Release type is required");
        }
    }

    private String generateUniqueFeatureCode(String featureName) {
        // Generar código único basado en el nombre de la feature
        String baseCode = featureName.toUpperCase().replaceAll("[^A-Z0-9]", "");
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(8);
        return "FEAT_" + baseCode + "_" + timestamp;
    }

    private Long getCurrentUserId() {
        // Obtener ID del usuario actual
        return 1L; // Placeholder
    }

    private String getCurrentUsername() {
        // Obtener username del usuario actual
        return "current_user"; // Placeholder
    }

    private RoadmapMetrics calculateRoadmapMetrics() {
        // Calcular métricas del roadmap
        long totalFeatures = featureRepository.count();
        long completedFeatures = featureRepository.countByStatus(FeatureStatus.COMPLETED);
        long inProgressFeatures = featureRepository.countByStatus(FeatureStatus.DEVELOPMENT);
        long plannedFeatures = featureRepository.countByStatus(FeatureStatus.PLANNING);

        return new RoadmapMetrics(totalFeatures, completedFeatures, inProgressFeatures, plannedFeatures);
    }
}
```

## Monitoreo y Métricas

### Prometheus Metrics

```java
@Component
public class RoadmapMetrics {

    @Autowired
    private MeterRegistry meterRegistry;

    private final Counter featuresCreatedCounter;
    private final Counter releasesCreatedCounter;
    private final Counter milestonesCompletedCounter;
    private final Counter feedbackSubmittedCounter;
    private final Timer featureCreationTimer;
    private final Timer releasePlanningTimer;

    public RoadmapMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.featuresCreatedCounter = Counter.builder("roadmap.features.created")
            .description("Total features created")
            .register(meterRegistry);

        this.releasesCreatedCounter = Counter.builder("roadmap.releases.created")
            .description("Total releases created")
            .register(meterRegistry);

        this.milestonesCompletedCounter = Counter.builder("roadmap.milestones.completed")
            .description("Total milestones completed")
            .register(meterRegistry);

        this.feedbackSubmittedCounter = Counter.builder("roadmap.feedback.submitted")
            .description("Total feedback submitted")
            .register(meterRegistry);

        this.featureCreationTimer = Timer.builder("roadmap.feature.creation.time")
            .description("Feature creation time")
            .register(meterRegistry);

        this.releasePlanningTimer = Timer.builder("roadmap.release.planning.time")
            .description("Release planning time")
            .register(meterRegistry);
    }

    public void incrementFeaturesCreated() {
        featuresCreatedCounter.increment();
    }

    public void incrementReleasesCreated() {
        releasesCreatedCounter.increment();
    }

    public void incrementMilestonesCompleted() {
        milestonesCompletedCounter.increment();
    }

    public void incrementFeedbackSubmitted() {
        feedbackSubmittedCounter.increment();
    }

    public Timer.Sample startFeatureCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startReleasePlanningTimer() {
        return Timer.start(meterRegistry);
    }
}
```

## Configuración de Aplicación

### application-roadmap.yml

```yaml
roadmap:
  # Configuración de features
  features:
    auto-code-generation: true
    code-prefix: "FEAT"
    max-name-length: 255
    default-status: "IDEA"
    auto-priority-calculation: true

  # Configuración de releases
  releases:
    version-validation: true
    semantic-versioning: true
    auto-changelog: true
    release-notes-template: true
    approval-workflow: true

  # Configuración de milestones
  milestones:
    auto-progress-calculation: true
    dependency-validation: true
    notification-on-completion: true
    max-dependencies: 10

  # Configuración de feedback
  feedback:
    auto-categorization: true
    sentiment-analysis: true
    priority-suggestions: true
    duplicate-detection: true
    max-comment-length: 1000

  # Configuración de roadmap
  roadmap:
    views:
      - "overview"
      - "quarterly"
      - "yearly"
      - "team"
      - "stakeholder"
      - "development"
    export-formats:
      - "pdf"
      - "excel"
      - "json"
      - "csv"
    update-frequency: "daily"
    cache-enabled: true
    cache-ttl-seconds: 3600
```

## Conclusión

El módulo de Roadmap proporciona un sistema completo de planificación estratégica del producto, incluyendo:

- **Gestión de Features**: Creación, priorización y seguimiento de funcionalidades
- **Planificación de Releases**: Gestión de versiones y ciclos de desarrollo
- **Seguimiento de Milestones**: Hitos y objetivos del producto
- **Feedback de Usuarios**: Recopilación y análisis de comentarios
- **Análisis de Mercado**: Investigación de competencia y tendencias
- **Roadmaps Visuales**: Diferentes vistas para diferentes audiencias
- **Métricas y KPIs**: Seguimiento del progreso del producto
- **Integración con Proyectos**: Conexión con el sistema de gestión de proyectos

El sistema está diseñado para ser flexible y escalable, permitiendo la adaptación a diferentes metodologías de desarrollo y necesidades de planificación del producto.
