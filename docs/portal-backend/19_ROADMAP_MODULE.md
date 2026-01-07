# Módulo de Roadmap de Plataforma - Portal Backend

## Descripción General

El módulo de **Roadmap de Plataforma** proporciona una vista simple y clara de la evolución de CodeflowX. Permite a los usuarios ver qué funcionalidades están en desarrollo, cuáles están planificadas y cuáles ya están disponibles. Es un roadmap público y transparente que mantiene informados a todos los usuarios sobre el futuro de la plataforma.

## Arquitectura del Sistema

### **Principios de Diseño**

- **Simplicidad**: Roadmap fácil de entender para cualquier usuario
- **Transparencia**: Información pública sobre el estado de desarrollo
- **Actualización Continua**: Roadmap siempre actualizado con el progreso real
- **Categorización Clara**: Funcionalidades agrupadas por tipo y prioridad
- **Timeline Visual**: Línea de tiempo clara para cada funcionalidad
- **Feedback de Usuarios**: Sistema para que los usuarios voten por funcionalidades

### **Flujo de Trabajo**

1. **Planificación**: Equipo define funcionalidades para próximas versiones
2. **Publicación**: Roadmap se actualiza con nuevas funcionalidades
3. **Desarrollo**: Equipo trabaja en las funcionalidades planificadas
4. **Actualización**: Estado se actualiza según el progreso real
5. **Lanzamiento**: Funcionalidades se marcan como completadas
6. **Feedback**: Usuarios votan y comentan sobre funcionalidades

## Entidades del Sistema

### 1. **PlatformFeature**

```java
@Entity
@Table(name = "rdm_platform_features")
public class PlatformFeature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "feature_name")
    private String featureName;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private FeatureCategory category;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private FeaturePriority priority;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private FeatureStatus status;

    @Column(name = "target_version")
    private String targetVersion; // v1.2.0, v2.0.0, etc.

    @Column(name = "target_quarter")
    private String targetQuarter; // Q1 2025, Q2 2025, etc.

    @Column(name = "progress_percentage")
    private Integer progressPercentage; // 0-100

    @Column(name = "user_votes")
    private Integer userVotes = 0;

    @Column(name = "is_public")
    private Boolean isPublic = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum FeatureCategory {
    CORE_PLATFORM, AI_AGENTS, NOTEBOOKS, DESIGN_TOOLS,
    INTEGRATIONS, SECURITY, PERFORMANCE, USER_EXPERIENCE
}

public enum FeaturePriority {
    LOW, MEDIUM, HIGH, CRITICAL
}

public enum FeatureStatus {
    PLANNED, IN_DEVELOPMENT, IN_TESTING, READY_FOR_RELEASE,
    RELEASED, ON_HOLD, CANCELLED
}
```

### 2. **PlatformRelease**

```java
@Entity
@Table(name = "rdm_platform_releases")
public class PlatformRelease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "version")
    private String version;

    @Column(name = "release_name")
    private String releaseName;

    @Column(name = "description")
    private String description;

    @Column(name = "release_type")
    @Enumerated(EnumType.STRING)
    private ReleaseType releaseType;

    @Column(name = "planned_date")
    private LocalDate plannedDate;

    @Column(name = "actual_date")
    private LocalDate actualDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ReleaseStatus status;

    @Column(name = "highlights")
    private String highlights; // JSON array de funcionalidades destacadas

    @Column(name = "breaking_changes")
    private String breakingChanges; // JSON array de cambios breaking

    @Column(name = "deprecations")
    private String deprecations; // JSON array de funcionalidades deprecadas

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum ReleaseType {
    MAJOR, MINOR, PATCH, HOTFIX
}

public enum ReleaseStatus {
    PLANNED, IN_DEVELOPMENT, IN_TESTING, READY_FOR_RELEASE, RELEASED
}
```

### 3. **UserVote**

```java
@Entity
@Table(name = "rdm_user_votes")
public class UserVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "feature_id")
    private Long featureId;

    @Column(name = "vote_type")
    @Enumerated(EnumType.STRING)
    private VoteType voteType;

    @Column(name = "comment")
    private String comment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

public enum VoteType {
    UP_VOTE, DOWN_VOTE, NEUTRAL
}
```

## Servicios del Sistema

### **PlatformRoadmapService**

```java
@Service
public class PlatformRoadmapService {

    @Autowired
    private PlatformFeatureRepository featureRepository;

    @Autowired
    private PlatformReleaseRepository releaseRepository;

    @Autowired
    private UserVoteRepository voteRepository;

    public List<PlatformFeature> getPublicRoadmap() {
        return featureRepository.findByIsPublicTrueOrderByPriorityDescTargetQuarterAsc();
    }

    public List<PlatformFeature> getFeaturesByCategory(FeatureCategory category) {
        return featureRepository.findByCategoryAndIsPublicTrueOrderByPriorityDesc(category);
    }

    public List<PlatformFeature> getFeaturesByStatus(FeatureStatus status) {
        return featureRepository.findByStatusAndIsPublicTrueOrderByTargetQuarterAsc(status);
    }

    public List<PlatformFeature> getFeaturesByQuarter(String quarter) {
        return featureRepository.findByTargetQuarterAndIsPublicTrueOrderByPriorityDesc(quarter);
    }

    public PlatformFeature addFeature(CreateFeatureRequest request) {
        PlatformFeature feature = new PlatformFeature();
        feature.setFeatureName(request.getFeatureName());
        feature.setDescription(request.getDescription());
        feature.setCategory(request.getCategory());
        feature.setPriority(request.getPriority());
        feature.setStatus(FeatureStatus.PLANNED);
        feature.setTargetVersion(request.getTargetVersion());
        feature.setTargetQuarter(request.getTargetQuarter());
        feature.setProgressPercentage(0);
        feature.setCreatedAt(LocalDateTime.now());

        return featureRepository.save(feature);
    }

    public void updateFeatureProgress(Long featureId, Integer progressPercentage) {
        PlatformFeature feature = featureRepository.findById(featureId)
            .orElseThrow(() -> new FeatureNotFoundException(featureId));

        feature.setProgressPercentage(progressPercentage);

        // Actualizar estado basado en progreso
        if (progressPercentage >= 100) {
            feature.setStatus(FeatureStatus.READY_FOR_RELEASE);
        } else if (progressPercentage > 0) {
            feature.setStatus(FeatureStatus.IN_DEVELOPMENT);
        }

        feature.setUpdatedAt(LocalDateTime.now());
        featureRepository.save(feature);
    }

    public void voteForFeature(Long featureId, Long userId, VoteType voteType, String comment) {
        // Verificar si el usuario ya votó
        Optional<UserVote> existingVote = voteRepository.findByUserIdAndFeatureId(userId, featureId);

        if (existingVote.isPresent()) {
            // Actualizar voto existente
            UserVote vote = existingVote.get();
            vote.setVoteType(voteType);
            vote.setComment(comment);
            voteRepository.save(vote);
        } else {
            // Crear nuevo voto
            UserVote vote = new UserVote();
            vote.setUserId(userId);
            vote.setFeatureId(featureId);
            vote.setVoteType(voteType);
            vote.setComment(comment);
            vote.setCreatedAt(LocalDateTime.now());
            voteRepository.save(vote);
        }

        // Actualizar contador de votos del feature
        updateFeatureVoteCount(featureId);
    }

    private void updateFeatureVoteCount(Long featureId) {
        List<UserVote> votes = voteRepository.findByFeatureId(featureId);
        int upVotes = (int) votes.stream().filter(v -> v.getVoteType() == VoteType.UP_VOTE).count();

        PlatformFeature feature = featureRepository.findById(featureId)
            .orElseThrow(() -> new FeatureNotFoundException(featureId));
        feature.setUserVotes(upVotes);
        featureRepository.save(feature);
    }

    public List<PlatformRelease> getUpcomingReleases() {
        return releaseRepository.findByStatusInOrderByPlannedDateAsc(
            Arrays.asList(ReleaseStatus.PLANNED, ReleaseStatus.IN_DEVELOPMENT, ReleaseStatus.IN_TESTING)
        );
    }

    public List<PlatformRelease> getRecentReleases() {
        return releaseRepository.findByStatusOrderByActualDateDesc(ReleaseStatus.RELEASED);
    }
}
```

## API Endpoints

### **Roadmap Público**

```java
@RestController
@RequestMapping("/api/v1/platform/roadmap")
public class PlatformRoadmapController {

    @Autowired
    private PlatformRoadmapService roadmapService;

    @GetMapping
    public ResponseEntity<List<PlatformFeature>> getPublicRoadmap() {
        List<PlatformFeature> features = roadmapService.getPublicRoadmap();
        return ResponseEntity.ok(features);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<PlatformFeature>> getFeaturesByCategory(@PathVariable FeatureCategory category) {
        List<PlatformFeature> features = roadmapService.getFeaturesByCategory(category);
        return ResponseEntity.ok(features);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PlatformFeature>> getFeaturesByStatus(@PathVariable FeatureStatus status) {
        List<PlatformFeature> features = roadmapService.getFeaturesByStatus(status);
        return ResponseEntity.ok(features);
    }

    @GetMapping("/quarter/{quarter}")
    public ResponseEntity<List<PlatformFeature>> getFeaturesByQuarter(@PathVariable String quarter) {
        List<PlatformFeature> features = roadmapService.getFeaturesByQuarter(quarter);
        return ResponseEntity.ok(features);
    }

    @GetMapping("/releases/upcoming")
    public ResponseEntity<List<PlatformRelease>> getUpcomingReleases() {
        List<PlatformRelease> releases = roadmapService.getUpcomingReleases();
        return ResponseEntity.ok(releases);
    }

    @GetMapping("/releases/recent")
    public ResponseEntity<List<PlatformRelease>> getRecentReleases() {
        List<PlatformRelease> releases = roadmapService.getRecentReleases();
        return ResponseEntity.ok(releases);
    }
}
```

### **Sistema de Votos**

```java
@RestController
@RequestMapping("/api/v1/platform/roadmap/votes")
public class UserVoteController {

    @Autowired
    private PlatformRoadmapService roadmapService;

    @PostMapping("/{featureId}")
    public ResponseEntity<Void> voteForFeature(@PathVariable Long featureId,
                                              @RequestBody VoteRequest request) {
        roadmapService.voteForFeature(featureId, getCurrentUserId(), request.getVoteType(), request.getComment());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{featureId}/stats")
    public ResponseEntity<VoteStats> getFeatureVoteStats(@PathVariable Long featureId) {
        VoteStats stats = roadmapService.getFeatureVoteStats(featureId);
        return ResponseEntity.ok(stats);
    }
}
```

## Configuración de Aplicación

### **application-platform-roadmap.yml**

```yaml
platform-roadmap:
  # Configuración del roadmap
  roadmap:
    auto-progress-updates: true
    progress-update-interval: 1h
    public-visibility: true
    user-voting: true

  # Configuración de releases
  releases:
    auto-versioning: true
    changelog-generation: true
    breaking-changes-tracking: true
    deprecation-notifications: true

  # Configuración de votos
  voting:
    max-votes-per-user: 100
    vote-validation: true
    comment-moderation: true

  # Configuración de notificaciones
  notifications:
    feature-updates: true
    release-announcements: true
    progress-notifications: true
    user-vote-updates: true
```

## Métricas Prometheus

```java
@Component
public class PlatformRoadmapMetrics {

    private final Counter featuresCreatedCounter;
    private final Counter featuresReleasedCounter;
    private final Counter userVotesCounter;
    private final Gauge activeFeaturesGauge;
    private final Gauge averageProgressGauge;

    public PlatformRoadmapMetrics(MeterRegistry meterRegistry) {
        this.featuresCreatedCounter = Counter.builder("platform.roadmap.features.created")
            .description("Total platform features created")
            .register(meterRegistry);

        this.featuresReleasedCounter = Counter.builder("platform.roadmap.features.released")
            .description("Total platform features released")
            .register(meterRegistry);

        this.userVotesCounter = Counter.builder("platform.roadmap.user.votes")
            .description("Total user votes on features")
            .register(meterRegistry);

        this.activeFeaturesGauge = Gauge.builder("platform.roadmap.features.active")
            .description("Number of active features in development")
            .register(meterRegistry);

        this.averageProgressGauge = Gauge.builder("platform.roadmap.progress.average")
            .description("Average progress of all features")
            .register(meterRegistry);
    }

    public void incrementFeaturesCreated() {
        featuresCreatedCounter.increment();
    }

    public void incrementFeaturesReleased() {
        featuresReleasedCounter.increment();
    }

    public void incrementUserVotes() {
        userVotesCounter.increment();
    }

    public void setActiveFeatures(int count) {
        activeFeaturesGauge.set(count);
    }

    public void setAverageProgress(double percentage) {
        averageProgressGauge.set(percentage);
    }
}
```

## Ejemplo de Roadmap Público

### **Q1 2025 - Funcionalidades Principales**

```json
{
  "quarter": "Q1 2025",
  "features": [
    {
      "name": "Sistema de Agentes IA",
      "description": "Creación y gestión de agentes de IA reutilizables",
      "category": "AI_AGENTS",
      "priority": "CRITICAL",
      "status": "IN_DEVELOPMENT",
      "progress": 75,
      "targetVersion": "v1.5.0",
      "userVotes": 156
    },
    {
      "name": "Notebooks Interactivos",
      "description": "Entorno de desarrollo con celdas de código Python",
      "category": "NOTEBOOKS",
      "priority": "HIGH",
      "status": "IN_DEVELOPMENT",
      "progress": 60,
      "targetVersion": "v1.5.0",
      "userVotes": 89
    },
    {
      "name": "Diseñador de Pantallas",
      "description": "Herramienta visual para diseño de interfaces",
      "category": "DESIGN_TOOLS",
      "priority": "MEDIUM",
      "status": "PLANNED",
      "progress": 0,
      "targetVersion": "v1.6.0",
      "userVotes": 45
    }
  ]
}
```

### **Q2 2025 - Mejoras y Expansión**

```json
{
  "quarter": "Q2 2025",
  "features": [
    {
      "name": "Marketplace de Agentes",
      "description": "Plataforma para compartir y descubrir agentes IA",
      "category": "AI_AGENTS",
      "priority": "HIGH",
      "status": "PLANNED",
      "progress": 0,
      "targetVersion": "v1.7.0",
      "userVotes": 234
    },
    {
      "name": "Integración Multi-Cloud",
      "description": "Soporte para AWS, Azure, GCP y otros proveedores",
      "category": "INTEGRATIONS",
      "priority": "MEDIUM",
      "status": "PLANNED",
      "progress": 0,
      "targetVersion": "v1.7.0",
      "userVotes": 67
    }
  ]
}
```

## Beneficios del Sistema

- **Transparencia**: Los usuarios ven exactamente qué se está desarrollando
- **Simplicidad**: Roadmap fácil de entender y navegar
- **Participación**: Sistema de votos para que los usuarios influyan en prioridades
- **Planificación**: Visión clara del futuro de la plataforma
- **Comunicación**: Información actualizada sobre el estado de desarrollo
- **Feedback**: Los usuarios pueden comentar y votar por funcionalidades
- **Confianza**: Los usuarios saben qué esperar en próximas versiones
- **Colaboración**: Sistema abierto para contribuciones y sugerencias

## Conclusión

El módulo de Roadmap de Plataforma proporciona una vista simple y transparente de la evolución de CodeflowX. Al mostrar claramente qué funcionalidades están en desarrollo, planificadas o ya disponibles, mantiene informados a todos los usuarios sobre el futuro de la plataforma. El sistema de votos permite a los usuarios participar activamente en la priorización de funcionalidades, creando una comunidad comprometida y colaborativa.
