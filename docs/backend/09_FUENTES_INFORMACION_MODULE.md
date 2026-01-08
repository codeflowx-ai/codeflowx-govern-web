# 09. FUENTES DE INFORMACIÓN MODULE

## 1. OVERVIEW

El módulo de **Fuentes de Información** centraliza la gestión de todas las fuentes de datos utilizadas por el sistema RAG, Domain Ingestion, Technology y Model Training. Este módulo elimina duplicaciones y proporciona una interfaz unificada para la gestión de APIs, bases de datos, documentos y web scraping.

### 1.1 Objetivos

- **Centralización**: Unificar la gestión de fuentes de datos
- **Eliminación de Duplicaciones**: Evitar redundancia entre módulos
- **Escalabilidad**: Soporte para múltiples dominios y proyectos RAG
- **Flexibilidad**: Asignación many-to-many entre fuentes, dominios y proyectos

### 1.2 Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│                    FUENTES DE INFORMACIÓN                  │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │     APIs    │  │   DATABASE  │  │ DOCUMENTOS  │        │
│  │             │  │             │  │             │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │WEB SCRAPING │  │   DOMINIOS  │  │PROYECTOS RAG│        │
│  │             │  │             │  │             │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
└─────────────────────────────────────────────────────────────┘
```

## 2. ENTIDADES PRINCIPALES

### 2.1 API Endpoints (cor_api_endpoints)

```java
@Entity
@Table(name = "cor_api_endpoints")
public class ApiEndpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "api_id")
    private Long apiId;

    @Column(name = "api_name", nullable = false, length = 255)
    private String apiName;

    @Column(name = "api_description", columnDefinition = "TEXT")
    private String apiDescription;

    @Column(name = "api_url", nullable = false, length = 500)
    private String apiUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "api_type", nullable = false)
    private ApiType apiType; // RAG_QUERY, EMBEDDING, RERANKING, LLM_INFERENCE, SEARCH, CHAT

    @Enumerated(EnumType.STRING)
    @Column(name = "http_method", nullable = false)
    private HttpMethod httpMethod; // GET, POST, PUT, DELETE

    @Column(name = "api_version", length = 50)
    private String apiVersion;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private ApiProvider provider; // OPENAI, ANTHROPIC, COHERE, HUGGINGFACE, CUSTOM

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApiStatus status; // ACTIVE, INACTIVE, ERROR, MAINTENANCE

    @Column(name = "authentication_type", length = 100)
    private String authenticationType;

    @Column(name = "api_key", length = 500)
    private String apiKey;

    @Column(name = "rate_limit_per_minute")
    private Integer rateLimitPerMinute;

    @Column(name = "timeout_seconds")
    private Integer timeoutSeconds;

    @Column(name = "retry_attempts")
    private Integer retryAttempts;

    @Column(name = "cost_per_request", precision = 10, scale = 4)
    private BigDecimal costPerRequest;

    @Column(name = "total_requests")
    private Long totalRequests = 0L;

    @Column(name = "successful_requests")
    private Long successfulRequests = 0L;

    @Column(name = "failed_requests")
    private Long failedRequests = 0L;

    @Column(name = "avg_response_time_ms")
    private Double avgResponseTimeMs;

    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relaciones Many-to-Many
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "cor_api_domain_assignments",
        joinColumns = @JoinColumn(name = "api_id"),
        inverseJoinColumns = @JoinColumn(name = "domain_id")
    )
    private Set<Domain> assignedDomains = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "cor_api_rag_project_assignments",
        joinColumns = @JoinColumn(name = "api_id"),
        inverseJoinColumns = @JoinColumn(name = "rag_project_id")
    )
    private Set<RagProject> assignedRagProjects = new HashSet<>();
}
```

### 2.2 Database Connections (cor_database_connections)

```java
@Entity
@Table(name = "cor_database_connections")
public class DatabaseConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "db_id")
    private Long dbId;

    @Column(name = "db_name", nullable = false, length = 255)
    private String dbName;

    @Column(name = "db_description", columnDefinition = "TEXT")
    private String dbDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "db_type", nullable = false)
    private DatabaseType dbType; // POSTGRESQL, MYSQL, MONGODB, ELASTICSEARCH, VECTOR_DB, REDIS

    @Column(name = "host", nullable = false, length = 255)
    private String host;

    @Column(name = "port")
    private Integer port;

    @Column(name = "database_name", length = 255)
    private String databaseName;

    @Column(name = "username", length = 255)
    private String username;

    @Column(name = "password", length = 500)
    private String password;

    @Column(name = "connection_string", length = 1000)
    private String connectionString;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DatabaseStatus status; // ACTIVE, INACTIVE, ERROR, MAINTENANCE

    @Column(name = "max_connections")
    private Integer maxConnections;

    @Column(name = "connection_timeout_seconds")
    private Integer connectionTimeoutSeconds;

    @Column(name = "query_timeout_seconds")
    private Integer queryTimeoutSeconds;

    @Column(name = "total_queries")
    private Long totalQueries = 0L;

    @Column(name = "successful_queries")
    private Long successfulQueries = 0L;

    @Column(name = "failed_queries")
    private Long failedQueries = 0L;

    @Column(name = "avg_query_time_ms")
    private Double avgQueryTimeMs;

    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relaciones Many-to-Many
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "cor_database_domain_assignments",
        joinColumns = @JoinColumn(name = "db_id"),
        inverseJoinColumns = @JoinColumn(name = "domain_id")
    )
    private Set<Domain> assignedDomains = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "cor_database_rag_project_assignments",
        joinColumns = @JoinColumn(name = "db_id"),
        inverseJoinColumns = @JoinColumn(name = "rag_project_id")
    )
    private Set<RagProject> assignedRagProjects = new HashSet<>();
}
```

### 2.3 Document Batches (cor_document_batches)

```java
@Entity
@Table(name = "cor_document_batches")
public class DocumentBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_id")
    private Long batchId;

    @Column(name = "batch_name", nullable = false, length = 255)
    private String batchName;

    @Column(name = "batch_description", columnDefinition = "TEXT")
    private String batchDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BatchStatus status; // UPLOADING, PROCESSING, COMPLETED, FAILED, PAUSED

    @Column(name = "total_documents")
    private Integer totalDocuments = 0;

    @Column(name = "processed_documents")
    private Integer processedDocuments = 0;

    @Column(name = "failed_documents")
    private Integer failedDocuments = 0;

    @Column(name = "total_chunks")
    private Integer totalChunks = 0;

    @Column(name = "total_size_bytes")
    private Long totalSizeBytes = 0L;

    @Column(name = "chunk_size")
    private Integer chunkSize = 1000;

    @Column(name = "chunk_overlap")
    private Integer chunkOverlap = 200;

    @Column(name = "processing_started_at")
    private LocalDateTime processingStartedAt;

    @Column(name = "processing_completed_at")
    private LocalDateTime processingCompletedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relaciones Many-to-Many
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "cor_document_domain_assignments",
        joinColumns = @JoinColumn(name = "batch_id"),
        inverseJoinColumns = @JoinColumn(name = "domain_id")
    )
    private Set<Domain> assignedDomains = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "cor_document_rag_project_assignments",
        joinColumns = @JoinColumn(name = "batch_id"),
        inverseJoinColumns = @JoinColumn(name = "rag_project_id")
    )
    private Set<RagProject> assignedRagProjects = new HashSet<>();

    // Relación One-to-Many con documentos
    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Document> documents = new HashSet<>();
}
```

### 2.4 Web Scraping Jobs (cor_web_scraping_jobs)

```java
@Entity
@Table(name = "cor_web_scraping_jobs")
public class WebScrapingJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long jobId;

    @Column(name = "job_name", nullable = false, length = 255)
    private String jobName;

    @Column(name = "job_description", columnDefinition = "TEXT")
    private String jobDescription;

    @Column(name = "target_url", nullable = false, length = 1000)
    private String targetUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private JobStatus status; // RUNNING, COMPLETED, FAILED, PAUSED

    @Column(name = "progress_percentage")
    private Integer progressPercentage = 0;

    @Column(name = "total_pages")
    private Integer totalPages = 0;

    @Column(name = "pages_scraped")
    private Integer pagesScraped = 0;

    @Column(name = "data_extracted")
    private Integer dataExtracted = 0;

    @Column(name = "errors_count")
    private Integer errorsCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private JobPriority priority; // LOW, MEDIUM, HIGH

    @Column(name = "schedule_cron", length = 100)
    private String scheduleCron;

    @Column(name = "last_run_at")
    private LocalDateTime lastRunAt;

    @Column(name = "next_run_at")
    private LocalDateTime nextRunAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relaciones Many-to-Many
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "cor_scraping_domain_assignments",
        joinColumns = @JoinColumn(name = "job_id"),
        inverseJoinColumns = @JoinColumn(name = "domain_id")
    )
    private Set<Domain> assignedDomains = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "cor_scraping_rag_project_assignments",
        joinColumns = @JoinColumn(name = "job_id"),
        inverseJoinColumns = @JoinColumn(name = "rag_project_id")
    )
    private Set<RagProject> assignedRagProjects = new HashSet<>();
}
```

## 3. TABLAS RELACIONALES MANY-TO-MANY

### 3.1 API Domain Assignments (cor_api_domain_assignments)

```sql
CREATE TABLE cor_api_domain_assignments (
    api_id BIGINT NOT NULL,
    domain_id BIGINT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    assigned_by VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (api_id, domain_id),
    FOREIGN KEY (api_id) REFERENCES cor_api_endpoints(api_id) ON DELETE CASCADE,
    FOREIGN KEY (domain_id) REFERENCES cor_domains(domain_id) ON DELETE CASCADE
);
```

### 3.2 API RAG Project Assignments (cor_api_rag_project_assignments)

```sql
CREATE TABLE cor_api_rag_project_assignments (
    api_id BIGINT NOT NULL,
    rag_project_id BIGINT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    assigned_by VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (api_id, rag_project_id),
    FOREIGN KEY (api_id) REFERENCES cor_api_endpoints(api_id) ON DELETE CASCADE,
    FOREIGN KEY (rag_project_id) REFERENCES rag_projects(rag_project_id) ON DELETE CASCADE
);
```

### 3.3 Database Domain Assignments (cor_database_domain_assignments)

```sql
CREATE TABLE cor_database_domain_assignments (
    db_id BIGINT NOT NULL,
    domain_id BIGINT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    assigned_by VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (db_id, domain_id),
    FOREIGN KEY (db_id) REFERENCES cor_database_connections(db_id) ON DELETE CASCADE,
    FOREIGN KEY (domain_id) REFERENCES cor_domains(domain_id) ON DELETE CASCADE
);
```

### 3.4 Database RAG Project Assignments (cor_database_rag_project_assignments)

```sql
CREATE TABLE cor_database_rag_project_assignments (
    db_id BIGINT NOT NULL,
    rag_project_id BIGINT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    assigned_by VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (db_id, rag_project_id),
    FOREIGN KEY (db_id) REFERENCES cor_database_connections(db_id) ON DELETE CASCADE,
    FOREIGN KEY (rag_project_id) REFERENCES rag_projects(rag_project_id) ON DELETE CASCADE
);
```

### 3.5 Document Domain Assignments (cor_document_domain_assignments)

```sql
CREATE TABLE cor_document_domain_assignments (
    batch_id BIGINT NOT NULL,
    domain_id BIGINT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    assigned_by VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (batch_id, domain_id),
    FOREIGN KEY (batch_id) REFERENCES cor_document_batches(batch_id) ON DELETE CASCADE,
    FOREIGN KEY (domain_id) REFERENCES cor_domains(domain_id) ON DELETE CASCADE
);
```

### 3.6 Document RAG Project Assignments (cor_document_rag_project_assignments)

```sql
CREATE TABLE cor_document_rag_project_assignments (
    batch_id BIGINT NOT NULL,
    rag_project_id BIGINT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    assigned_by VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (batch_id, rag_project_id),
    FOREIGN KEY (batch_id) REFERENCES cor_document_batches(batch_id) ON DELETE CASCADE,
    FOREIGN KEY (rag_project_id) REFERENCES rag_projects(rag_project_id) ON DELETE CASCADE
);
```

### 3.7 Scraping Domain Assignments (cor_scraping_domain_assignments)

```sql
CREATE TABLE cor_scraping_domain_assignments (
    job_id BIGINT NOT NULL,
    domain_id BIGINT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    assigned_by VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (job_id, domain_id),
    FOREIGN KEY (job_id) REFERENCES cor_web_scraping_jobs(job_id) ON DELETE CASCADE,
    FOREIGN KEY (domain_id) REFERENCES cor_domains(domain_id) ON DELETE CASCADE
);
```

### 3.8 Scraping RAG Project Assignments (cor_scraping_rag_project_assignments)

```sql
CREATE TABLE cor_scraping_rag_project_assignments (
    job_id BIGINT NOT NULL,
    rag_project_id BIGINT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    assigned_by VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (job_id, rag_project_id),
    FOREIGN KEY (job_id) REFERENCES cor_web_scraping_jobs(job_id) ON DELETE CASCADE,
    FOREIGN KEY (rag_project_id) REFERENCES rag_projects(rag_project_id) ON DELETE CASCADE
);
```

## 4. ENUMS Y TIPOS

### 4.1 API Types

```java
public enum ApiType {
    RAG_QUERY("RAG Query"),
    EMBEDDING("Embedding"),
    RERANKING("Reranking"),
    LLM_INFERENCE("LLM Inference"),
    SEARCH("Search"),
    CHAT("Chat");
}
```

### 4.2 Database Types

```java
public enum DatabaseType {
    POSTGRESQL("PostgreSQL"),
    MYSQL("MySQL"),
    MONGODB("MongoDB"),
    ELASTICSEARCH("Elasticsearch"),
    VECTOR_DB("Vector Database"),
    REDIS("Redis");
}
```

### 4.3 Status Enums

```java
public enum ApiStatus {
    ACTIVE, INACTIVE, ERROR, MAINTENANCE
}

public enum DatabaseStatus {
    ACTIVE, INACTIVE, ERROR, MAINTENANCE
}

public enum BatchStatus {
    UPLOADING, PROCESSING, COMPLETED, FAILED, PAUSED
}

public enum JobStatus {
    RUNNING, COMPLETED, FAILED, PAUSED
}
```

## 5. SERVICIOS Y REPOSITORIOS

### 5.1 ApiEndpointService

```java
@Service
@Transactional
public class ApiEndpointService {

    @Autowired
    private ApiEndpointRepository apiEndpointRepository;

    public List<ApiEndpoint> getAllApiEndpoints() {
        return apiEndpointRepository.findAll();
    }

    public ApiEndpoint getApiEndpointById(Long id) {
        return apiEndpointRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("API Endpoint not found"));
    }

    public ApiEndpoint createApiEndpoint(ApiEndpoint apiEndpoint) {
        apiEndpoint.setCreatedAt(LocalDateTime.now());
        return apiEndpointRepository.save(apiEndpoint);
    }

    public ApiEndpoint updateApiEndpoint(Long id, ApiEndpoint apiEndpointDetails) {
        ApiEndpoint apiEndpoint = getApiEndpointById(id);
        // Update fields
        apiEndpoint.setUpdatedAt(LocalDateTime.now());
        return apiEndpointRepository.save(apiEndpoint);
    }

    public void deleteApiEndpoint(Long id) {
        ApiEndpoint apiEndpoint = getApiEndpointById(id);
        apiEndpointRepository.delete(apiEndpoint);
    }

    public List<ApiEndpoint> getApiEndpointsByDomain(Long domainId) {
        return apiEndpointRepository.findByAssignedDomains_DomainId(domainId);
    }

    public List<ApiEndpoint> getApiEndpointsByRagProject(Long ragProjectId) {
        return apiEndpointRepository.findByAssignedRagProjects_RagProjectId(ragProjectId);
    }

    public ApiEndpoint assignToDomain(Long apiId, Long domainId) {
        // Implementation for domain assignment
    }

    public ApiEndpoint assignToRagProject(Long apiId, Long ragProjectId) {
        // Implementation for RAG project assignment
    }
}
```

### 5.2 DatabaseConnectionService

```java
@Service
@Transactional
public class DatabaseConnectionService {

    @Autowired
    private DatabaseConnectionRepository databaseConnectionRepository;

    public List<DatabaseConnection> getAllDatabaseConnections() {
        return databaseConnectionRepository.findAll();
    }

    public DatabaseConnection getDatabaseConnectionById(Long id) {
        return databaseConnectionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Database Connection not found"));
    }

    public DatabaseConnection createDatabaseConnection(DatabaseConnection dbConnection) {
        dbConnection.setCreatedAt(LocalDateTime.now());
        return databaseConnectionRepository.save(dbConnection);
    }

    public DatabaseConnection updateDatabaseConnection(Long id, DatabaseConnection dbConnectionDetails) {
        DatabaseConnection dbConnection = getDatabaseConnectionById(id);
        // Update fields
        dbConnection.setUpdatedAt(LocalDateTime.now());
        return databaseConnectionRepository.save(dbConnection);
    }

    public void deleteDatabaseConnection(Long id) {
        DatabaseConnection dbConnection = getDatabaseConnectionById(id);
        databaseConnectionRepository.delete(dbConnection);
    }

    public List<DatabaseConnection> getDatabaseConnectionsByDomain(Long domainId) {
        return databaseConnectionRepository.findByAssignedDomains_DomainId(domainId);
    }

    public List<DatabaseConnection> getDatabaseConnectionsByRagProject(Long ragProjectId) {
        return databaseConnectionRepository.findByAssignedRagProjects_RagProjectId(ragProjectId);
    }

    public boolean testConnection(Long dbId) {
        // Implementation for connection testing
    }
}
```

## 6. CONTROLADORES REST

### 6.1 ApiEndpointController

```java
@RestController
@RequestMapping("/api/data-sources/apis")
@CrossOrigin(origins = "*")
public class ApiEndpointController {

    @Autowired
    private ApiEndpointService apiEndpointService;

    @GetMapping
    public ResponseEntity<List<ApiEndpoint>> getAllApiEndpoints() {
        List<ApiEndpoint> apiEndpoints = apiEndpointService.getAllApiEndpoints();
        return ResponseEntity.ok(apiEndpoints);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiEndpoint> getApiEndpointById(@PathVariable Long id) {
        ApiEndpoint apiEndpoint = apiEndpointService.getApiEndpointById(id);
        return ResponseEntity.ok(apiEndpoint);
    }

    @PostMapping
    public ResponseEntity<ApiEndpoint> createApiEndpoint(@RequestBody ApiEndpoint apiEndpoint) {
        ApiEndpoint createdApiEndpoint = apiEndpointService.createApiEndpoint(apiEndpoint);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdApiEndpoint);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiEndpoint> updateApiEndpoint(@PathVariable Long id, @RequestBody ApiEndpoint apiEndpointDetails) {
        ApiEndpoint updatedApiEndpoint = apiEndpointService.updateApiEndpoint(id, apiEndpointDetails);
        return ResponseEntity.ok(updatedApiEndpoint);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApiEndpoint(@PathVariable Long id) {
        apiEndpointService.deleteApiEndpoint(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/test")
    public ResponseEntity<Map<String, Object>> testApiEndpoint(@PathVariable Long id) {
        // Implementation for API testing
    }

    @PostMapping("/{id}/assign/domain/{domainId}")
    public ResponseEntity<ApiEndpoint> assignToDomain(@PathVariable Long id, @PathVariable Long domainId) {
        ApiEndpoint apiEndpoint = apiEndpointService.assignToDomain(id, domainId);
        return ResponseEntity.ok(apiEndpoint);
    }

    @PostMapping("/{id}/assign/rag-project/{ragProjectId}")
    public ResponseEntity<ApiEndpoint> assignToRagProject(@PathVariable Long id, @PathVariable Long ragProjectId) {
        ApiEndpoint apiEndpoint = apiEndpointService.assignToRagProject(id, ragProjectId);
        return ResponseEntity.ok(apiEndpoint);
    }
}
```

### 6.2 DatabaseConnectionController

```java
@RestController
@RequestMapping("/api/data-sources/databases")
@CrossOrigin(origins = "*")
public class DatabaseConnectionController {

    @Autowired
    private DatabaseConnectionService databaseConnectionService;

    @GetMapping
    public ResponseEntity<List<DatabaseConnection>> getAllDatabaseConnections() {
        List<DatabaseConnection> dbConnections = databaseConnectionService.getAllDatabaseConnections();
        return ResponseEntity.ok(dbConnections);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatabaseConnection> getDatabaseConnectionById(@PathVariable Long id) {
        DatabaseConnection dbConnection = databaseConnectionService.getDatabaseConnectionById(id);
        return ResponseEntity.ok(dbConnection);
    }

    @PostMapping
    public ResponseEntity<DatabaseConnection> createDatabaseConnection(@RequestBody DatabaseConnection dbConnection) {
        DatabaseConnection createdDbConnection = databaseConnectionService.createDatabaseConnection(dbConnection);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDbConnection);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DatabaseConnection> updateDatabaseConnection(@PathVariable Long id, @RequestBody DatabaseConnection dbConnectionDetails) {
        DatabaseConnection updatedDbConnection = databaseConnectionService.updateDatabaseConnection(id, dbConnectionDetails);
        return ResponseEntity.ok(updatedDbConnection);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDatabaseConnection(@PathVariable Long id) {
        databaseConnectionService.deleteDatabaseConnection(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/test")
    public ResponseEntity<Map<String, Object>> testDatabaseConnection(@PathVariable Long id) {
        boolean isConnected = databaseConnectionService.testConnection(id);
        Map<String, Object> response = new HashMap<>();
        response.put("connected", isConnected);
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }
}
```

## 7. VISTAS Y PROCEDIMIENTOS

### 7.1 Vista de Estadísticas de APIs

```sql
CREATE VIEW v_api_statistics AS
SELECT
    api_id,
    api_name,
    api_type,
    provider,
    status,
    total_requests,
    successful_requests,
    failed_requests,
    CASE
        WHEN total_requests > 0 THEN (successful_requests::FLOAT / total_requests::FLOAT) * 100
        ELSE 0
    END as success_rate,
    avg_response_time_ms,
    cost_per_request,
    (total_requests * cost_per_request) as total_cost,
    last_used_at,
    created_at
FROM cor_api_endpoints;
```

### 7.2 Vista de Estadísticas de Bases de Datos

```sql
CREATE VIEW v_database_statistics AS
SELECT
    db_id,
    db_name,
    db_type,
    status,
    total_queries,
    successful_queries,
    failed_queries,
    CASE
        WHEN total_queries > 0 THEN (successful_queries::FLOAT / total_queries::FLOAT) * 100
        ELSE 0
    END as success_rate,
    avg_query_time_ms,
    last_used_at,
    created_at
FROM cor_database_connections;
```

### 7.3 Procedimiento para Actualizar Estadísticas de API

```sql
CREATE OR REPLACE PROCEDURE sp_update_api_statistics(
    p_api_id BIGINT,
    p_request_successful BOOLEAN,
    p_response_time_ms DOUBLE PRECISION
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE cor_api_endpoints
    SET
        total_requests = total_requests + 1,
        successful_requests = CASE
            WHEN p_request_successful THEN successful_requests + 1
            ELSE successful_requests
        END,
        failed_requests = CASE
            WHEN NOT p_request_successful THEN failed_requests + 1
            ELSE failed_requests
        END,
        avg_response_time_ms = CASE
            WHEN total_requests = 0 THEN p_response_time_ms
            ELSE (avg_response_time_ms * (total_requests - 1) + p_response_time_ms) / total_requests
        END,
        last_used_at = CURRENT_TIMESTAMP,
        updated_at = CURRENT_TIMESTAMP
    WHERE api_id = p_api_id;
END;
$$;
```

### 7.4 Procedimiento para Actualizar Estadísticas de Base de Datos

```sql
CREATE OR REPLACE PROCEDURE sp_update_database_statistics(
    p_db_id BIGINT,
    p_query_successful BOOLEAN,
    p_query_time_ms DOUBLE PRECISION
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE cor_database_connections
    SET
        total_queries = total_queries + 1,
        successful_queries = CASE
            WHEN p_query_successful THEN successful_queries + 1
            ELSE successful_queries
        END,
        failed_queries = CASE
            WHEN NOT p_query_successful THEN failed_queries + 1
            ELSE failed_queries
        END,
        avg_query_time_ms = CASE
            WHEN total_queries = 0 THEN p_query_time_ms
            ELSE (avg_query_time_ms * (total_queries - 1) + p_query_time_ms) / total_queries
        END,
        last_used_at = CURRENT_TIMESTAMP,
        updated_at = CURRENT_TIMESTAMP
    WHERE db_id = p_db_id;
END;
$$;
```

## 8. INTEGRACIÓN CON OTROS MÓDULOS

### 8.1 Integración con RAG Module

- Las APIs se asignan a proyectos RAG para embedding, reranking y LLM inference
- Las bases de datos se utilizan para almacenar vectores y metadatos
- Los documentos se procesan y chunking para RAG
- Web scraping proporciona datos en tiempo real para RAG

### 8.2 Integración con Domain Ingestion

- Todas las fuentes se pueden asignar a múltiples dominios
- Proporciona datos estructurados y no estructurados
- Soporte para múltiples formatos y protocolos

### 8.3 Integración con Technology Module

- APIs para integración con sistemas externos
- Bases de datos para almacenamiento de configuraciones
- Documentos para documentación técnica

### 8.4 Integración con Model Training

- APIs para acceso a modelos pre-entrenados
- Bases de datos para datasets de entrenamiento
- Documentos como corpus de entrenamiento

## 9. SEGURIDAD Y COMPLIANCE

### 9.1 Autenticación y Autorización

- API keys encriptadas
- Credenciales de base de datos seguras
- Control de acceso basado en roles
- Auditoría de accesos

### 9.2 Cumplimiento Normativo

- GDPR compliance para datos personales
- IA Act compliance para sistemas de IA
- Logging y trazabilidad completa
- Retención de datos configurable

## 10. MONITOREO Y MÉTRICAS

### 10.1 Métricas de APIs

- Tasa de éxito/fallo
- Tiempo de respuesta promedio
- Uso de rate limits
- Costos por request

### 10.2 Métricas de Bases de Datos

- Conexiones activas
- Tiempo de query promedio
- Uso de recursos
- Disponibilidad

### 10.3 Métricas de Documentos

- Progreso de procesamiento
- Tamaño de chunks
- Calidad de extracción
- Tiempo de procesamiento

### 10.4 Métricas de Web Scraping

- Páginas procesadas
- Datos extraídos
- Errores de scraping
- Cumplimiento de rate limits

## 11. CONFIGURACIÓN Y DESPLIEGUE

### 11.1 Variables de Entorno

```bash
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=fuentes_informacion
DB_USERNAME=admin
DB_PASSWORD=secure_password

# API Configuration
API_RATE_LIMIT=1000
API_TIMEOUT=30
API_RETRY_ATTEMPTS=3

# Security
JWT_SECRET=your_jwt_secret
ENCRYPTION_KEY=your_encryption_key
```

### 11.2 Docker Compose

```yaml
version: "3.8"
services:
  fuentes-informacion-db:
    image: postgres:15
    environment:
      POSTGRES_DB: fuentes_informacion
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: secure_password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  fuentes-informacion-api:
    build: .
    ports:
      - "8080:8080"
    environment:
      - DB_HOST=fuentes-informacion-db
      - DB_PORT=5432
      - DB_NAME=fuentes_informacion
    depends_on:
      - fuentes-informacion-db

volumes:
  postgres_data:
```

## 12. TESTING

### 12.1 Unit Tests

```java
@ExtendWith(MockitoExtension.class)
class ApiEndpointServiceTest {

    @Mock
    private ApiEndpointRepository apiEndpointRepository;

    @InjectMocks
    private ApiEndpointService apiEndpointService;

    @Test
    void testCreateApiEndpoint() {
        // Test implementation
    }

    @Test
    void testGetApiEndpointById() {
        // Test implementation
    }
}
```

### 12.2 Integration Tests

```java
@SpringBootTest
@AutoConfigureTestDatabase
class ApiEndpointControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreateApiEndpoint() {
        // Integration test implementation
    }
}
```

## 13. DOCUMENTACIÓN DE API

### 13.1 OpenAPI Specification

```yaml
openapi: 3.0.0
info:
  title: Fuentes de Información API
  version: 1.0.0
  description: API para gestión centralizada de fuentes de datos

paths:
  /api/data-sources/apis:
    get:
      summary: Obtener todas las APIs
      responses:
        "200":
          description: Lista de APIs
    post:
      summary: Crear nueva API
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: "#/components/schemas/ApiEndpoint"
```

Este documento proporciona una visión completa del módulo de Fuentes de Información, centralizando la gestión de todas las fuentes de datos y eliminando duplicaciones entre módulos.
