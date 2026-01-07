# Módulo de Plugins - Portal Backend

## Descripción General

El módulo de Plugins gestiona la extensibilidad de la plataforma mediante un sistema de plugins dinámicos. Incluye funcionalidades de instalación, configuración, gestión de dependencias, versionado y ejecución segura de plugins. Se integra con sistemas de gestión de paquetes y proporciona un entorno de ejecución aislado para plugins de terceros.

## Entidades del Sistema

### 1. Plugin

```java
@Entity
@Table(name = "plugins")
public class Plugin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column
    private String description;
    
    @Column(name = "plugin_type")
    @Enumerated(EnumType.STRING)
    private PluginType pluginType;
    
    @Column(name = "version")
    private String version;
    
    @Column(name = "author")
    private String author;
    
    @Column(name = "vendor")
    private String vendor;
    
    @Column(name = "license")
    private String license;
    
    @Column(name = "homepage_url")
    private String homepageUrl;
    
    @Column(name = "repository_url")
    private String repositoryUrl;
    
    @Column(name = "documentation_url")
    private String documentationUrl;
    
    @Column(name = "plugin_file_path")
    private String pluginFilePath;
    
    @Column(name = "plugin_file_size_bytes")
    private Long pluginFileSizeBytes;
    
    @Column(name = "checksum")
    private String checksum;
    
    @Column(name = "dependencies")
    private String dependencies; // JSON dependencies
    
    @Column(name = "configuration_schema")
    private String configurationSchema; // JSON schema for config
    
    @Column(name = "capabilities")
    private String capabilities; // JSON plugin capabilities
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PluginStatus status;
    
    @Column(name = "is_active")
    private Boolean isActive;
    
    @Column(name = "installed_at")
    private LocalDateTime installedAt;
    
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "plugin", cascade = CascadeType.ALL)
    private List<PluginConfiguration> configurations = new ArrayList<>();
    
    @OneToMany(mappedBy = "plugin", cascade = CascadeType.ALL)
    private List<PluginExecution> executions = new ArrayList<>();
    
    @OneToMany(mappedBy = "plugin", cascade = CascadeType.ALL)
    private List<PluginLog> logs = new ArrayList<>();
}

public enum PluginType {
    INTEGRATION, TRANSFORMATION, VISUALIZATION, ANALYSIS, WORKFLOW, CUSTOM
}

public enum PluginStatus {
    INSTALLED, CONFIGURED, ACTIVE, INACTIVE, ERROR, UPDATING, UNINSTALLING
}
```

### 2. PluginConfiguration

```java
@Entity
@Table(name = "plugin_configurations")
public class PluginConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "plugin_id")
    private Long pluginId;
    
    @Column(name = "config_key")
    private String configKey;
    
    @Column(name = "config_value")
    private String configValue;
    
    @Column(name = "config_type")
    private String configType; // string, integer, boolean, json, etc.
    
    @Column(name = "is_required")
    private Boolean isRequired;
    
    @Column(name = "default_value")
    private String defaultValue;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "validation_rules")
    private String validationRules; // JSON validation rules
    
    @Column(name = "is_encrypted")
    private Boolean isEncrypted;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

### 3. PluginExecution

```java
@Entity
@Table(name = "plugin_executions")
public class PluginExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "plugin_id")
    private Long pluginId;
    
    @Column(name = "execution_id")
    private String executionId; // UUID for tracking
    
    @Column(name = "execution_type")
    @Enumerated(EnumType.STRING)
    private ExecutionType executionType;
    
    @Column(name = "input_data")
    private String inputData; // JSON input data
    
    @Column(name = "output_data")
    private String outputData; // JSON output data
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ExecutionStatus status;
    
    @Column(name = "started_at")
    private LocalDateTime startedAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @Column(name = "duration_ms")
    private Long durationMs;
    
    @Column(name = "error_message")
    private String errorMessage;
    
    @Column(name = "execution_context")
    private String executionContext; // JSON execution context
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

public enum ExecutionType {
    SYNC, ASYNC, SCHEDULED, EVENT_DRIVEN, MANUAL
}

public enum ExecutionStatus {
    PENDING, RUNNING, COMPLETED, FAILED, CANCELLED, TIMEOUT
}
```

### 4. PluginLog

```java
@Entity
@Table(name = "plugin_logs")
public class PluginLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "plugin_id")
    private Long pluginId;
    
    @Column(name = "execution_id")
    private Long executionId;
    
    @Column(name = "log_level")
    @Enumerated(EnumType.STRING)
    private LogLevel logLevel;
    
    @Column(name = "log_message")
    private String logMessage;
    
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    
    @Column(name = "source")
    private String source; // plugin, runtime, system
    
    @Column(name = "additional_data")
    private String additionalData; // JSON additional context
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

public enum LogLevel {
    DEBUG, INFO, WARNING, ERROR, CRITICAL
}
```

### 5. PluginRegistry

```java
@Entity
@Table(name = "plugin_registry")
public class PluginRegistry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "plugin_name")
    private String pluginName;
    
    @Column(name = "registry_url")
    private String registryUrl;
    
    @Column(name = "latest_version")
    private String latestVersion;
    
    @Column(name = "available_versions")
    private String availableVersions; // JSON array of versions
    
    @Column(name = "download_count")
    private Long downloadCount;
    
    @Column(name = "rating")
    private Double rating;
    
    @Column(name = "tags")
    private String tags; // JSON array of tags
    
    @Column(name = "last_checked")
    private LocalDateTime lastChecked;
    
    @Column(name = "is_available")
    private Boolean isAvailable;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

### 6. PluginDependency

```java
@Entity
@Table(name = "plugin_dependencies")
public class PluginDependency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "plugin_id")
    private Long pluginId;
    
    @Column(name = "dependency_name")
    private String dependencyName;
    
    @Column(name = "dependency_type")
    @Enumerated(EnumType.STRING)
    private DependencyType dependencyType;
    
    @Column(name = "version_constraint")
    private String versionConstraint; // e.g., ">=1.0.0,<2.0.0"
    
    @Column(name = "is_optional")
    private Boolean isOptional;
    
    @Column(name = "is_resolved")
    private Boolean isResolved;
    
    @Column(name = "resolved_version")
    private String resolvedVersion;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

public enum DependencyType {
    PLUGIN, LIBRARY, SERVICE, DATABASE, EXTERNAL_API
}
```

## API Endpoints

### Plugin Management

```
GET    /api/v1/plugins
GET    /api/v1/plugins/{id}
POST   /api/v1/plugins
PUT    /api/v1/plugins/{id}
DELETE /api/v1/plugins/{id}
POST   /api/v1/plugins/{id}/install
POST   /api/v1/plugins/{id}/uninstall
POST   /api/v1/plugins/{id}/activate
POST   /api/v1/plugins/{id}/deactivate
POST   /api/v1/plugins/{id}/update
```

### Plugin Configuration

```
GET    /api/v1/plugins/{id}/configurations
GET    /api/v1/plugins/{id}/configurations/{configId}
POST   /api/v1/plugins/{id}/configurations
PUT    /api/v1/plugins/{id}/configurations/{configId}
DELETE /api/v1/plugins/{id}/configurations/{configId}
POST   /api/v1/plugins/{id}/configurations/validate
POST   /api/v1/plugins/{id}/configurations/apply
```

### Plugin Execution

```
GET    /api/v1/plugins/{id}/executions
GET    /api/v1/plugins/{id}/executions/{executionId}
POST   /api/v1/plugins/{id}/execute
POST   /api/v1/plugins/{id}/execute/async
POST   /api/v1/plugins/{id}/execute/scheduled
GET    /api/v1/plugins/{id}/executions/status/{status}
```

### Plugin Registry

```
GET    /api/v1/plugin-registry
GET    /api/v1/plugin-registry/search
GET    /api/v1/plugin-registry/categories
GET    /api/v1/plugin-registry/popular
GET    /api/v1/plugin-registry/recent
POST   /api/v1/plugin-registry/check-updates
```

### Plugin Monitoring

```
GET    /api/v1/plugins/{id}/logs
GET    /api/v1/plugins/{id}/logs/search
GET    /api/v1/plugins/{id}/metrics
GET    /api/v1/plugins/{id}/health
GET    /api/v1/plugins/monitoring/overview
GET    /api/v1/plugins/monitoring/performance
```

## Scripts de Base de Datos

```sql
-- Tabla de plugins
CREATE TABLE plugins (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    plugin_type VARCHAR(50) NOT NULL,
    version VARCHAR(50) NOT NULL,
    author VARCHAR(255),
    vendor VARCHAR(255),
    license VARCHAR(100),
    homepage_url VARCHAR(500),
    repository_url VARCHAR(500),
    documentation_url VARCHAR(500),
    plugin_file_path VARCHAR(500),
    plugin_file_size_bytes BIGINT,
    checksum VARCHAR(64),
    dependencies TEXT,
    configuration_schema TEXT,
    capabilities TEXT,
    status VARCHAR(50) DEFAULT 'INSTALLED',
    is_active BOOLEAN DEFAULT false,
    installed_at TIMESTAMP,
    last_updated TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de configuraciones de plugins
CREATE TABLE plugin_configurations (
    id BIGSERIAL PRIMARY KEY,
    plugin_id BIGINT REFERENCES plugins(id) ON DELETE CASCADE,
    config_key VARCHAR(255) NOT NULL,
    config_value TEXT,
    config_type VARCHAR(50) NOT NULL,
    is_required BOOLEAN DEFAULT false,
    default_value TEXT,
    description TEXT,
    validation_rules TEXT,
    is_encrypted BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de ejecuciones de plugins
CREATE TABLE plugin_executions (
    id BIGSERIAL PRIMARY KEY,
    plugin_id BIGINT REFERENCES plugins(id) ON DELETE CASCADE,
    execution_id VARCHAR(100) NOT NULL,
    execution_type VARCHAR(50) NOT NULL,
    input_data TEXT,
    output_data TEXT,
    status VARCHAR(50) DEFAULT 'PENDING',
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    duration_ms BIGINT,
    error_message TEXT,
    execution_context TEXT,
    user_id BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de logs de plugins
CREATE TABLE plugin_logs (
    id BIGSERIAL PRIMARY KEY,
    plugin_id BIGINT REFERENCES plugins(id) ON DELETE CASCADE,
    execution_id BIGINT REFERENCES plugin_executions(id) ON DELETE CASCADE,
    log_level VARCHAR(20) NOT NULL,
    log_message TEXT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    source VARCHAR(100),
    additional_data TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de registro de plugins
CREATE TABLE plugin_registry (
    id BIGSERIAL PRIMARY KEY,
    plugin_name VARCHAR(255) NOT NULL,
    registry_url VARCHAR(500),
    latest_version VARCHAR(50),
    available_versions TEXT,
    download_count BIGINT DEFAULT 0,
    rating DECIMAL(3,2),
    tags TEXT,
    last_checked TIMESTAMP,
    is_available BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de dependencias de plugins
CREATE TABLE plugin_dependencies (
    id BIGSERIAL PRIMARY KEY,
    plugin_id BIGINT REFERENCES plugins(id) ON DELETE CASCADE,
    dependency_name VARCHAR(255) NOT NULL,
    dependency_type VARCHAR(50) NOT NULL,
    version_constraint VARCHAR(100),
    is_optional BOOLEAN DEFAULT false,
    is_resolved BOOLEAN DEFAULT false,
    resolved_version VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para optimización
CREATE INDEX idx_plugins_type ON plugins(plugin_type);
CREATE INDEX idx_plugins_status ON plugins(status);
CREATE INDEX idx_plugins_active ON plugins(is_active);
CREATE INDEX idx_plugins_name_version ON plugins(name, version);
CREATE INDEX idx_plugin_configurations_plugin_id ON plugin_configurations(plugin_id);
CREATE INDEX idx_plugin_configurations_key ON plugin_configurations(config_key);
CREATE INDEX idx_plugin_executions_plugin_id ON plugin_executions(plugin_id);
CREATE INDEX idx_plugin_executions_status ON plugin_executions(status);
CREATE INDEX idx_plugin_executions_type ON plugin_executions(execution_type);
CREATE INDEX idx_plugin_executions_user_id ON plugin_executions(user_id);
CREATE INDEX idx_plugin_logs_plugin_id ON plugin_logs(plugin_id);
CREATE INDEX idx_plugin_logs_execution_id ON plugin_logs(execution_id);
CREATE INDEX idx_plugin_logs_timestamp ON plugin_logs(timestamp);
CREATE INDEX idx_plugin_logs_level ON plugin_logs(log_level);
CREATE INDEX idx_plugin_registry_name ON plugin_registry(plugin_name);
CREATE INDEX idx_plugin_registry_available ON plugin_registry(is_available);
CREATE INDEX idx_plugin_dependencies_plugin_id ON plugin_dependencies(plugin_id);
CREATE INDEX idx_plugin_dependencies_name ON plugin_dependencies(dependency_name);
CREATE INDEX idx_plugin_dependencies_resolved ON plugin_dependencies(is_resolved);
```

## Servicios de Plugins

### PluginService

```java
@Service
@Transactional
public class PluginService {
    
    @Autowired
    private PluginRepository pluginRepository;
    
    @Autowired
    private PluginRegistryService registryService;
    
    @Autowired
    private PluginExecutionService executionService;
    
    @Autowired
    private PluginSecurityService securityService;
    
    @Autowired
    private AuditService auditService;
    
    public Plugin installPlugin(PluginInstallDto installDto) {
        // Validar datos de instalación
        validateInstallData(installDto);
        
        // Verificar seguridad del plugin
        securityService.validatePluginSecurity(installDto.getPluginFilePath());
        
        // Crear plugin
        Plugin plugin = new Plugin();
        plugin.setName(installDto.getName());
        plugin.setDescription(installDto.getDescription());
        plugin.setPluginType(installDto.getPluginType());
        plugin.setVersion(installDto.getVersion());
        plugin.setAuthor(installDto.getAuthor());
        plugin.setVendor(installDto.getVendor());
        plugin.setLicense(installDto.getLicense());
        plugin.setHomepageUrl(installDto.getHomepageUrl());
        plugin.setRepositoryUrl(installDto.getRepositoryUrl());
        plugin.setDocumentationUrl(installDto.getDocumentationUrl());
        plugin.setPluginFilePath(installDto.getPluginFilePath());
        plugin.setPluginFileSizeBytes(installDto.getPluginFileSizeBytes());
        plugin.setChecksum(installDto.getChecksum());
        plugin.setDependencies(installDto.getDependencies());
        plugin.setConfigurationSchema(installDto.getConfigurationSchema());
        plugin.setCapabilities(installDto.getCapabilities());
        plugin.setStatus(PluginStatus.INSTALLED);
        plugin.setIsActive(false);
        plugin.setInstalledAt(LocalDateTime.now());
        plugin.setCreatedAt(LocalDateTime.now());
        
        Plugin savedPlugin = pluginRepository.save(plugin);
        
        // Resolver dependencias
        resolveDependencies(savedPlugin);
        
        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "INSTALL_PLUGIN",
            "PLUGIN",
            savedPlugin.getId().toString()
        );
        
        return savedPlugin;
    }
    
    public Plugin activatePlugin(Long pluginId) {
        Plugin plugin = pluginRepository.findById(pluginId)
            .orElseThrow(() -> new PluginNotFoundException("Plugin not found"));
        
        // Validar que el plugin esté instalado
        if (plugin.getStatus() != PluginStatus.INSTALLED) {
            throw new InvalidPluginStateException("Plugin must be installed to activate");
        }
        
        // Verificar dependencias resueltas
        if (!areDependenciesResolved(pluginId)) {
            throw new DependencyException("Plugin dependencies not resolved");
        }
        
        // Activar plugin
        plugin.setStatus(PluginStatus.ACTIVE);
        plugin.setIsActive(true);
        plugin.setUpdatedAt(LocalDateTime.now());
        
        Plugin activePlugin = pluginRepository.save(plugin);
        
        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "ACTIVATE_PLUGIN",
            "PLUGIN",
            pluginId.toString()
        );
        
        return activePlugin;
    }
    
    public PluginExecution executePlugin(Long pluginId, PluginExecutionDto executionDto) {
        Plugin plugin = pluginRepository.findById(pluginId)
            .orElseThrow(() -> new PluginNotFoundException("Plugin not found"));
        
        // Validar que el plugin esté activo
        if (!plugin.getIsActive()) {
            throw new InvalidPluginStateException("Plugin must be active to execute");
        }
        
        // Crear ejecución
        PluginExecution execution = new PluginExecution();
        execution.setPluginId(pluginId);
        execution.setExecutionId(UUID.randomUUID().toString());
        execution.setExecutionType(executionDto.getExecutionType());
        execution.setInputData(executionDto.getInputData());
        execution.setStatus(ExecutionStatus.PENDING);
        execution.setUserId(getCurrentUserId());
        execution.setCreatedAt(LocalDateTime.now());
        
        PluginExecution savedExecution = executionService.save(execution);
        
        // Ejecutar plugin según el tipo
        switch (executionDto.getExecutionType()) {
            case SYNC:
                executePluginSync(savedExecution);
                break;
            case ASYNC:
                executePluginAsync(savedExecution);
                break;
            case SCHEDULED:
                schedulePluginExecution(savedExecution, executionDto.getScheduleTime());
                break;
            default:
                throw new UnsupportedExecutionTypeException("Unsupported execution type: " + executionDto.getExecutionType());
        }
        
        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "EXECUTE_PLUGIN",
            "PLUGIN_EXECUTION",
            savedExecution.getId().toString()
        );
        
        return savedExecution;
    }
    
    private void resolveDependencies(Plugin plugin) {
        if (plugin.getDependencies() != null) {
            // Parsear dependencias JSON y resolverlas
            // Implementar lógica de resolución
        }
    }
    
    private boolean areDependenciesResolved(Long pluginId) {
        // Verificar que todas las dependencias estén resueltas
        return true; // Placeholder
    }
    
    private void executePluginSync(PluginExecution execution) {
        try {
            execution.setStatus(ExecutionStatus.RUNNING);
            execution.setStartedAt(LocalDateTime.now());
            executionService.save(execution);
            
            // Ejecutar plugin de forma síncrona
            // Implementar lógica de ejecución
            
            execution.setStatus(ExecutionStatus.COMPLETED);
            execution.setCompletedAt(LocalDateTime.now());
            execution.setDurationMs(calculateDuration(execution.getStartedAt()));
            executionService.save(execution);
            
        } catch (Exception e) {
            execution.setStatus(ExecutionStatus.FAILED);
            execution.setErrorMessage(e.getMessage());
            execution.setCompletedAt(LocalDateTime.now());
            executionService.save(execution);
        }
    }
    
    private void executePluginAsync(PluginExecution execution) {
        // Ejecutar plugin de forma asíncrona
        CompletableFuture.runAsync(() -> {
            try {
                execution.setStatus(ExecutionStatus.RUNNING);
                execution.setStartedAt(LocalDateTime.now());
                executionService.save(execution);
                
                // Implementar lógica de ejecución asíncrona
                
                execution.setStatus(ExecutionStatus.COMPLETED);
                execution.setCompletedAt(LocalDateTime.now());
                execution.setDurationMs(calculateDuration(execution.getStartedAt()));
                executionService.save(execution);
                
            } catch (Exception e) {
                execution.setStatus(ExecutionStatus.FAILED);
                execution.setErrorMessage(e.getMessage());
                execution.setCompletedAt(LocalDateTime.now());
                executionService.save(execution);
            }
        });
    }
    
    private void schedulePluginExecution(PluginExecution execution, LocalDateTime scheduleTime) {
        // Programar ejecución del plugin
        // Implementar lógica de scheduling
    }
    
    private Long calculateDuration(LocalDateTime startedAt) {
        if (startedAt == null) {
            return 0L;
        }
        
        Duration duration = Duration.between(startedAt, LocalDateTime.now());
        return duration.toMillis();
    }
    
    private void validateInstallData(PluginInstallDto installDto) {
        if (installDto.getName() == null || installDto.getName().trim().isEmpty()) {
            throw new ValidationException("Plugin name is required");
        }
        
        if (installDto.getPluginType() == null) {
            throw new ValidationException("Plugin type is required");
        }
        
        if (installDto.getVersion() == null || installDto.getVersion().trim().isEmpty()) {
            throw new ValidationException("Plugin version is required");
        }
        
        if (installDto.getPluginFilePath() == null || installDto.getPluginFilePath().trim().isEmpty()) {
            throw new ValidationException("Plugin file path is required");
        }
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
public class PluginMetrics {
    
    @Autowired
    private MeterRegistry meterRegistry;
    
    private final Counter pluginsInstalledCounter;
    private final Counter pluginsActivatedCounter;
    private final Counter pluginsDeactivatedCounter;
    private final Counter pluginExecutionsCounter;
    private final Counter pluginExecutionFailuresCounter;
    private final Timer pluginExecutionTimer;
    private final Timer pluginInstallationTimer;
    
    public PluginMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        
        this.pluginsInstalledCounter = Counter.builder("plugins.installed")
            .description("Total plugins installed")
            .register(meterRegistry);
        
        this.pluginsActivatedCounter = Counter.builder("plugins.activated")
            .description("Total plugins activated")
            .register(meterRegistry);
        
        this.pluginsDeactivatedCounter = Counter.builder("plugins.deactivated")
            .description("Total plugins deactivated")
            .register(meterRegistry);
        
        this.pluginExecutionsCounter = Counter.builder("plugins.executions")
            .description("Total plugin executions")
            .register(meterRegistry);
        
        this.pluginExecutionFailuresCounter = Counter.builder("plugins.executions.failed")
            .description("Total plugin execution failures")
            .register(meterRegistry);
        
        this.pluginExecutionTimer = Timer.builder("plugins.execution.time")
            .description("Plugin execution time")
            .register(meterRegistry);
        
        this.pluginInstallationTimer = Timer.builder("plugins.installation.time")
            .description("Plugin installation time")
            .register(meterRegistry);
    }
    
    public void incrementPluginsInstalled() {
        pluginsInstalledCounter.increment();
    }
    
    public void incrementPluginsActivated() {
        pluginsActivatedCounter.increment();
    }
    
    public void incrementPluginsDeactivated() {
        pluginsDeactivatedCounter.increment();
    }
    
    public void incrementPluginExecutions() {
        pluginExecutionsCounter.increment();
    }
    
    public void incrementPluginExecutionFailures() {
        pluginExecutionFailuresCounter.increment();
    }
    
    public Timer.Sample startPluginExecutionTimer() {
        return Timer.start(meterRegistry);
    }
    
    public Timer.Sample startPluginInstallationTimer() {
        return Timer.start(meterRegistry);
    }
}
```

## Configuración de Aplicación

### application-plugins.yml

```yaml
plugins:
  # Configuración de instalación
  installation:
    auto-dependency-resolution: true
    security-validation: true
    sandbox-execution: true
    max-file-size-mb: 100
    allowed-file-types: ["jar", "zip", "tar.gz"]
  
  # Configuración de ejecución
  execution:
    timeout-seconds: 300
    max-memory-mb: 1024
    max-cpu-percent: 80
    sandbox-enabled: true
    logging-enabled: true
  
  # Configuración de seguridad
  security:
    signature-validation: true
    permission-checking: true
    resource-isolation: true
    network-restrictions: true
  
  # Configuración de registro
  registry:
    auto-update-check: true
    update-check-interval-hours: 24
    trusted-sources: ["official", "verified", "community"]
    rating-threshold: 3.0
  
  # Configuración de monitorización
  monitoring:
    execution-tracking: true
    performance-monitoring: true
    error-tracking: true
    usage-analytics: true
```

## Conclusión

El módulo de Plugins proporciona un sistema completo de extensibilidad para la plataforma, incluyendo:

- **Gestión de Plugins**: Instalación, activación y gestión del ciclo de vida de plugins
- **Sistema de Dependencias**: Resolución automática y gestión de dependencias entre plugins
- **Ejecución Segura**: Entorno de ejecución aislado y sandbox para plugins
- **Configuración Dinámica**: Esquemas de configuración y validación automática
- **Monitorización**: Seguimiento de ejecuciones, rendimiento y errores
- **Registro de Plugins**: Catálogo centralizado y gestión de versiones

El sistema está diseñado para ser seguro, escalable y fácil de usar, proporcionando una base sólida para la extensibilidad de la plataforma mediante plugins de terceros.
