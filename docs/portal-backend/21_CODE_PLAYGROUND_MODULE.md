# Módulo de Code Playground

## Descripción General

El **Code Playground Module** es un entorno interactivo para probar, experimentar y validar código en múltiples lenguajes y tecnologías. Proporciona un sandbox seguro para desarrollo, testing y aprendizaje.

## Arquitectura del Sistema

### Componentes Principales

1. **Code Execution Engine** - Motor de ejecución de código
2. **Language Runtimes** - Entornos de ejecución para diferentes lenguajes
3. **Sandbox Environment** - Entorno aislado y seguro
4. **Code Validation** - Validación de sintaxis y seguridad
5. **Result Management** - Gestión de resultados y logs

### Tecnologías Soportadas

- **JavaScript/Node.js** - Runtime completo con npm
- **Python** - Con pip y librerías estándar
- **Java** - Compilación y ejecución JVM
- **SQL** - Ejecución de consultas en base de datos
- **Shell Scripts** - Comandos bash limitados
- **Docker** - Contenedores aislados

## Entidades del Sistema

### 1. Sesiones de Playground

```java
@Entity
@Table(name = "plg_playground_sessions")
public class PlaygroundSession {
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
    @Column(name = "programming_language", nullable = false)
    private ProgrammingLanguage programmingLanguage;
    
    @Column(name = "session_config", columnDefinition = "JSONB")
    private String sessionConfig;
    
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

public enum ProgrammingLanguage {
    JAVASCRIPT, PYTHON, JAVA, SQL, BASH, DOCKER
}

public enum SessionStatus {
    ACTIVE, PAUSED, EXPIRED, TERMINATED
}
```

### 2. Ejecuciones de Código

```java
@Entity
@Table(name = "plg_code_executions")
public class CodeExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "session_id", nullable = false)
    private Long sessionId;
    
    @Column(name = "code_snippet", columnDefinition = "TEXT")
    private String codeSnippet;
    
    @Column(name = "input_data", columnDefinition = "JSONB")
    private String inputData;
    
    @Column(name = "execution_result", columnDefinition = "TEXT")
    private String executionResult;
    
    @Column(name = "error_message")
    private String errorMessage;
    
    @Column(name = "execution_time_ms")
    private Long executionTimeMs;
    
    @Column(name = "memory_usage_mb")
    private Long memoryUsageMb;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ExecutionStatus status;
    
    @Column(name = "started_at")
    private LocalDateTime startedAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Getters y Setters
}

public enum ExecutionStatus {
    PENDING, RUNNING, COMPLETED, FAILED, TIMEOUT, MEMORY_LIMIT_EXCEEDED
}
```

### 3. Archivos del Playground

```java
@Entity
@Table(name = "plg_playground_files")
public class PlaygroundFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "session_id", nullable = false)
    private Long sessionId;
    
    @Column(name = "file_name", nullable = false)
    private String fileName;
    
    @Column(name = "file_path")
    private String filePath;
    
    @Column(name = "file_content", columnDefinition = "TEXT")
    private String fileContent;
    
    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;
    
    @Column(name = "mime_type")
    private String mimeType;
    
    @Column(name = "is_directory")
    private Boolean isDirectory;
    
    @Column(name = "parent_file_id")
    private Long parentFileId;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Getters y Setters
}
```

### 4. Configuraciones de Lenguaje

```java
@Entity
@Table(name = "plg_language_configs")
public class LanguageConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "programming_language", nullable = false, unique = true)
    private ProgrammingLanguage programmingLanguage;
    
    @Column(name = "version")
    private String version;
    
    @Column(name = "runtime_config", columnDefinition = "JSONB")
    private String runtimeConfig;
    
    @Column(name = "allowed_libraries", columnDefinition = "JSONB")
    private String allowedLibraries;
    
    @Column(name = "security_restrictions", columnDefinition = "JSONB")
    private String securityRestrictions;
    
    @Column(name = "timeout_seconds")
    private Integer timeoutSeconds;
    
    @Column(name = "memory_limit_mb")
    private Integer memoryLimitMb;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Getters y Setters
}
```

### 5. Logs de Ejecución

```java
@Entity
@Table(name = "plg_execution_logs")
public class ExecutionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "execution_id", nullable = false)
    private Long executionId;
    
    @Column(name = "log_level", nullable = false)
    private String logLevel;
    
    @Column(name = "message", columnDefinition = "TEXT")
    private String message;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    @Column(name = "source")
    private String source;
    
    @Column(name = "metadata", columnDefinition = "JSONB")
    private String metadata;
    
    // Getters y Setters
}
```

## Servicios del Sistema

### 1. Servicio de Playground

```java
@Service
@Transactional
public class PlaygroundService {
    
    @Autowired
    private PlaygroundSessionRepository sessionRepository;
    
    @Autowired
    private CodeExecutionRepository executionRepository;
    
    @Autowired
    private PlaygroundFileRepository fileRepository;
    
    @Autowired
    private CodeExecutionEngine executionEngine;
    
    /**
     * Crea una nueva sesión de playground
     */
    public PlaygroundSession createSession(SessionCreationRequest request) {
        // Validar límites del usuario
        validateUserLimits(request.getUserId());
        
        // Crear sesión
        PlaygroundSession session = new PlaygroundSession();
        session.setSessionName(request.getSessionName());
        session.setDescription(request.getDescription());
        session.setUserId(request.getUserId());
        session.setProgrammingLanguage(request.getProgrammingLanguage());
        session.setSessionConfig(request.getSessionConfig());
        session.setStatus(SessionStatus.ACTIVE);
        session.setStartedAt(LocalDateTime.now());
        session.setLastActivity(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusHours(24));
        session.setCreatedAt(LocalDateTime.now());
        
        session = sessionRepository.save(session);
        
        // Inicializar archivos del playground
        initializePlaygroundFiles(session);
        
        return session;
    }
    
    /**
     * Ejecuta código en una sesión
     */
    public CodeExecution executeCode(Long sessionId, CodeExecutionRequest request) {
        PlaygroundSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Sesión no encontrada"));
        
        // Validar sesión activa
        if (session.getStatus() != SessionStatus.ACTIVE) {
            throw new InvalidSessionException("Sesión no está activa");
        }
        
        // Crear ejecución
        CodeExecution execution = new CodeExecution();
        execution.setSessionId(sessionId);
        execution.setCodeSnippet(request.getCodeSnippet());
        execution.setInputData(request.getInputData());
        execution.setStatus(ExecutionStatus.PENDING);
        execution.setStartedAt(LocalDateTime.now());
        execution.setCreatedAt(LocalDateTime.now());
        
        execution = executionRepository.save(execution);
        
        // Ejecutar código de forma asíncrona
        CompletableFuture.runAsync(() -> executeCodeAsync(execution));
        
        return execution;
    }
    
    /**
     * Ejecuta código de forma asíncrona
     */
    private void executeCodeAsync(CodeExecution execution) {
        try {
            execution.setStatus(ExecutionStatus.RUNNING);
            executionRepository.save(execution);
            
            long startTime = System.currentTimeMillis();
            
            // Ejecutar código usando el motor de ejecución
            ExecutionResult result = executionEngine.execute(execution);
            
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Actualizar ejecución con resultados
            execution.setExecutionResult(result.getOutput());
            execution.setErrorMessage(result.getError());
            execution.setExecutionTimeMs(executionTime);
            execution.setMemoryUsageMb(result.getMemoryUsage());
            execution.setStatus(ExecutionStatus.COMPLETED);
            execution.setCompletedAt(LocalDateTime.now());
            
            executionRepository.save(execution);
            
        } catch (Exception e) {
            execution.setStatus(ExecutionStatus.FAILED);
            execution.setErrorMessage(e.getMessage());
            execution.setCompletedAt(LocalDateTime.now());
            executionRepository.save(execution);
        }
    }
}
```

### 2. Motor de Ejecución de Código

```java
@Service
public class CodeExecutionEngine {
    
    @Autowired
    private LanguageConfigRepository configRepository;
    
    @Autowired
    private DockerService dockerService;
    
    /**
     * Ejecuta código según el lenguaje
     */
    public ExecutionResult execute(CodeExecution execution) {
        PlaygroundSession session = getSession(execution.getSessionId());
        LanguageConfig config = getLanguageConfig(session.getProgrammingLanguage());
        
        // Validar restricciones de seguridad
        validateSecurityRestrictions(execution.getCodeSnippet(), config);
        
        // Ejecutar según el lenguaje
        switch (session.getProgrammingLanguage()) {
            case JAVASCRIPT:
                return executeJavaScript(execution, config);
            case PYTHON:
                return executePython(execution, config);
            case JAVA:
                return executeJava(execution, config);
            case SQL:
                return executeSQL(execution, config);
            case BASH:
                return executeBash(execution, config);
            case DOCKER:
                return executeDocker(execution, config);
            default:
                throw new UnsupportedLanguageException("Lenguaje no soportado");
        }
    }
    
    /**
     * Ejecuta código JavaScript
     */
    private ExecutionResult executeJavaScript(CodeExecution execution, LanguageConfig config) {
        try {
            // Crear contenedor Node.js aislado
            String containerId = dockerService.createContainer("node:18-alpine", config);
            
            // Escribir código en el contenedor
            dockerService.writeFile(containerId, "/app/code.js", execution.getCodeSnippet());
            
            // Ejecutar código
            DockerExecutionResult result = dockerService.executeCommand(containerId, 
                "node /app/code.js", config.getTimeoutSeconds());
            
            // Limpiar contenedor
            dockerService.removeContainer(containerId);
            
            return new ExecutionResult(result.getOutput(), result.getError(), result.getMemoryUsage());
            
        } catch (Exception e) {
            throw new ExecutionException("Error ejecutando JavaScript", e);
        }
    }
    
    /**
     * Ejecuta código Python
     */
    private ExecutionResult executePython(CodeExecution execution, LanguageConfig config) {
        try {
            // Crear contenedor Python aislado
            String containerId = dockerService.createContainer("python:3.11-alpine", config);
            
            // Escribir código en el contenedor
            dockerService.writeFile(containerId, "/app/code.py", execution.getCodeSnippet());
            
            // Instalar librerías permitidas
            if (config.getAllowedLibraries() != null) {
                installAllowedLibraries(containerId, config.getAllowedLibraries());
            }
            
            // Ejecutar código
            DockerExecutionResult result = dockerService.executeCommand(containerId, 
                "python /app/code.py", config.getTimeoutSeconds());
            
            // Limpiar contenedor
            dockerService.removeContainer(containerId);
            
            return new ExecutionResult(result.getOutput(), result.getError(), result.getMemoryUsage());
            
        } catch (Exception e) {
            throw new ExecutionException("Error ejecutando Python", e);
        }
    }
}
```

## API Endpoints

### 1. Gestión de Sesiones

```java
@RestController
@RequestMapping("/api/v1/playground/sessions")
public class PlaygroundSessionController {
    
    @Autowired
    private PlaygroundService playgroundService;
    
    /**
     * Crear nueva sesión
     */
    @PostMapping
    public ResponseEntity<PlaygroundSession> createSession(@RequestBody SessionCreationRequest request) {
        PlaygroundSession session = playgroundService.createSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(session);
    }
    
    /**
     * Obtener sesión por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlaygroundSession> getSession(@PathVariable Long id) {
        PlaygroundSession session = playgroundService.getSessionById(id);
        return ResponseEntity.ok(session);
    }
    
    /**
     * Listar sesiones del usuario
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<PlaygroundSession>> getUserSessions(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Page<PlaygroundSession> sessions = playgroundService.getUserSessions(userId, page, size);
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
    
    /**
     * Terminar sesión
     */
    @PostMapping("/{id}/terminate")
    public ResponseEntity<Void> terminateSession(@PathVariable Long id) {
        playgroundService.terminateSession(id);
        return ResponseEntity.ok().build();
    }
}
```

### 2. Ejecución de Código

```java
@RestController
@RequestMapping("/api/v1/playground/executions")
public class CodeExecutionController {
    
    @Autowired
    private PlaygroundService playgroundService;
    
    /**
     * Ejecutar código
     */
    @PostMapping("/sessions/{sessionId}/execute")
    public ResponseEntity<CodeExecution> executeCode(
            @PathVariable Long sessionId,
            @RequestBody CodeExecutionRequest request) {
        
        CodeExecution execution = playgroundService.executeCode(sessionId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(execution);
    }
    
    /**
     * Obtener resultado de ejecución
     */
    @GetMapping("/{id}")
    public ResponseEntity<CodeExecution> getExecution(@PathVariable Long id) {
        CodeExecution execution = playgroundService.getExecutionById(id);
        return ResponseEntity.ok(execution);
    }
    
    /**
     * Listar ejecuciones de una sesión
     */
    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<Page<CodeExecution>> getSessionExecutions(
            @PathVariable Long sessionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Page<CodeExecution> executions = playgroundService.getSessionExecutions(sessionId, page, size);
        return ResponseEntity.ok(executions);
    }
    
    /**
     * Cancelar ejecución en progreso
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelExecution(@PathVariable Long id) {
        playgroundService.cancelExecution(id);
        return ResponseEntity.ok().build();
    }
}
```

### 3. Gestión de Archivos

```java
@RestController
@RequestMapping("/api/v1/playground/files")
public class PlaygroundFileController {
    
    @Autowired
    private PlaygroundFileService fileService;
    
    /**
     * Crear archivo
     */
    @PostMapping("/sessions/{sessionId}")
    public ResponseEntity<PlaygroundFile> createFile(
            @PathVariable Long sessionId,
            @RequestBody FileCreationRequest request) {
        
        PlaygroundFile file = fileService.createFile(sessionId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(file);
    }
    
    /**
     * Actualizar archivo
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlaygroundFile> updateFile(
            @PathVariable Long id,
            @RequestBody FileUpdateRequest request) {
        
        PlaygroundFile file = fileService.updateFile(id, request);
        return ResponseEntity.ok(file);
    }
    
    /**
     * Listar archivos de una sesión
     */
    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<List<PlaygroundFile>> getSessionFiles(@PathVariable Long sessionId) {
        List<PlaygroundFile> files = fileService.getSessionFiles(sessionId);
        return ResponseEntity.ok(files);
    }
    
    /**
     * Descargar archivo
     */
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        Resource resource = fileService.downloadFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
```

## Configuración de Aplicación

### application.yml

```yaml
# Configuración del Módulo de Code Playground
playground:
  # Configuración de sesiones
  sessions:
    max-active-sessions-per-user: 5
    session-timeout-hours: 24
    max-session-size-mb: 100
  
  # Configuración de ejecución
  execution:
    max-execution-time-seconds: 300
    max-memory-usage-mb: 512
    max-concurrent-executions: 10
  
  # Configuración de Docker
  docker:
    enabled: true
    registry-url: ${DOCKER_REGISTRY_URL:}
    base-images:
      javascript: node:18-alpine
      python: python:3.11-alpine
      java: openjdk:17-alpine
      sql: postgres:15-alpine
  
  # Configuración de seguridad
  security:
    allowed-file-extensions: [".js", ".py", ".java", ".sql", ".sh", ".yml", ".yaml"]
    blocked-commands: ["rm", "del", "format", "shutdown"]
    max-file-size-kb: 1024

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
      application: codeflowx-playground
      module: code-playground
```

## Métricas de Prometheus

### Métricas Personalizadas

```java
@Component
public class PlaygroundMetrics {
    
    private final Counter sessionsCreatedTotal;
    private final Counter codeExecutionsTotal;
    private final Counter codeExecutionsSuccessful;
    private final Counter codeExecutionsFailed;
    private final Histogram executionTimeSeconds;
    private final Gauge activeSessions;
    private final Gauge activeExecutions;
    
    public PlaygroundMetrics(MeterRegistry meterRegistry) {
        this.sessionsCreatedTotal = Counter.builder("playground_sessions_created_total")
                .description("Total de sesiones de playground creadas")
                .tag("module", "code-playground")
                .register(meterRegistry);
        
        this.codeExecutionsTotal = Counter.builder("playground_code_executions_total")
                .description("Total de ejecuciones de código")
                .tag("module", "code-playground")
                .register(meterRegistry);
        
        this.codeExecutionsSuccessful = Counter.builder("playground_code_executions_successful")
                .description("Ejecuciones de código exitosas")
                .tag("module", "code-playground")
                .register(meterRegistry);
        
        this.codeExecutionsFailed = Counter.builder("playground_code_executions_failed")
                .description("Ejecuciones de código fallidas")
                .tag("module", "code-playground")
                .register(meterRegistry);
        
        this.executionTimeSeconds = Histogram.builder("playground_execution_time_seconds")
                .description("Tiempo de ejecución en segundos")
                .tag("module", "code-playground")
                .buckets(1, 5, 15, 30, 60, 120, 300)
                .register(meterRegistry);
        
        this.activeSessions = Gauge.builder("playground_active_sessions")
                .description("Sesiones activas actualmente")
                .tag("module", "code-playground")
                .register(meterRegistry);
        
        this.activeExecutions = Gauge.builder("playground_active_executions")
                .description("Ejecuciones activas actualmente")
                .tag("module", "code-playground")
                .register(meterRegistry);
    }
    
    public void recordSessionCreated() {
        sessionsCreatedTotal.increment();
        activeSessions.increment();
    }
    
    public void recordSessionTerminated() {
        activeSessions.decrement();
    }
    
    public void recordCodeExecutionStarted() {
        codeExecutionsTotal.increment();
        activeExecutions.increment();
    }
    
    public void recordCodeExecutionCompleted(long executionTimeSeconds, boolean success) {
        this.executionTimeSeconds.record(executionTimeSeconds);
        activeExecutions.decrement();
        
        if (success) {
            codeExecutionsSuccessful.increment();
        } else {
            codeExecutionsFailed.increment();
        }
    }
}
```

## Casos de Uso

### 1. Testing de Algoritmos

```javascript
// Ejemplo de testing de algoritmo de ordenamiento
function quickSort(arr) {
    if (arr.length <= 1) return arr;
    
    const pivot = arr[Math.floor(arr.length / 2)];
    const left = arr.filter(x => x < pivot);
    const middle = arr.filter(x => x === pivot);
    const right = arr.filter(x => x > pivot);
    
    return [...quickSort(left), ...middle, ...quickSort(right)];
}

// Test
const testArray = [3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5];
console.log('Original:', testArray);
console.log('Ordenado:', quickSort(testArray));
```

### 2. Experimentación con APIs

```python
import requests
import json

# Test de API REST
def test_api():
    try:
        response = requests.get('https://jsonplaceholder.typicode.com/posts/1')
        if response.status_code == 200:
            data = response.json()
            print(f"Título: {data['title']}")
            print(f"Usuario ID: {data['userId']}")
            return data
        else:
            print(f"Error: {response.status_code}")
            return None
    except Exception as e:
        print(f"Excepción: {e}")
        return None

test_api()
```

### 3. Consultas SQL

```sql
-- Ejemplo de consulta compleja
WITH user_stats AS (
    SELECT 
        u.department_id,
        d.name as department_name,
        COUNT(*) as user_count,
        AVG(u.salary) as avg_salary
    FROM users u
    JOIN departments d ON u.department_id = d.id
    WHERE u.is_active = true
    GROUP BY u.department_id, d.name
)
SELECT 
    department_name,
    user_count,
    ROUND(avg_salary, 2) as avg_salary,
    CASE 
        WHEN avg_salary > 50000 THEN 'High'
        WHEN avg_salary > 30000 THEN 'Medium'
        ELSE 'Low'
    END as salary_category
FROM user_stats
ORDER BY avg_salary DESC;
```

## Conclusión

El **Code Playground Module** proporciona un entorno seguro y aislado para:

- **Testing de código** en múltiples lenguajes
- **Experimentación** con APIs y librerías
- **Aprendizaje** de nuevas tecnologías
- **Validación** de algoritmos y lógica de negocio
- **Prototipado rápido** de soluciones
- **Colaboración** en tiempo real entre desarrolladores

El sistema está diseñado con seguridad como prioridad, utilizando contenedores Docker aislados y restricciones de acceso para garantizar un entorno de desarrollo seguro.
