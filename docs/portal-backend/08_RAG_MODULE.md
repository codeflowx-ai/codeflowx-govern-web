# Módulo RAG (Retrieval Augmented Generation) - Portal Backend

## Descripción General

El módulo RAG proporciona capacidades avanzadas de **Recuperación Aumentada de Información** para la generación de texto, utilizando PostgreSQL con pgvector para embeddings, integración con leka-server para procesamiento de documentos, y el sistema unificado de conversaciones del módulo TMP.

**IMPORTANTE**: Este módulo NO duplica funcionalidades de domain-ingestion. En su lugar, **consume y procesa** documentos, fuentes de datos y web scraping del módulo domain-ingestion para generar chunks, embeddings y capacidades de búsqueda semántica.

### Características Principales

- **Procesamiento de Contenido**: Consume documentos de domain-ingestion para chunking inteligente
- **Sistema de Chunks Avanzado**: Chunking semántico, overlap inteligente, metadata rica
- **Embeddings Vectoriales**: pgvector para búsqueda semántica eficiente
- **Búsqueda Híbrida**: Combinación de búsqueda vectorial y textual
- **Integración con TMP**: Usa el sistema unificado de conversaciones
- **Auto-clasificación**: Clasificación automática de chunks usando LLMs
- **Leka-Server Integration**: Procesamiento de documentos externo
- **Consumo de Domain-Ingestion**: Procesa documentos, web scraping y fuentes de datos existentes
- **Asignación de Modelos**: Cada proyecto RAG se asigna a modelos específicos (embedding, reranker, LLM)
- **Ejecución en Chatbots**: Configuración específica para ejecución por parte de chatbots
- **Gestión de Modelos**: Catálogo de modelos disponibles (locales y externos) con métricas de rendimiento

## Arquitectura del Sistema

```plaintext
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Portal API    │    │   RAG Service   │
│                 │◄──►│                 │◄──►│                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │                       │
                                ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   PostgreSQL    │    │   Leka Server   │    │   TMP Module    │
│   + pgvector    │◄──►│   Document      │◄──►│   Conversations │
│   (Embeddings)  │    │   Processing    │    │   & Templates   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │                       │
                                ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Domain        │    │   RabbitMQ      │    │   Prometheus    │
│   Ingestion     │◄──►│   Messaging     │    │   Metrics       │
│   (Documents,   │    │                 │    │                 │
│   WebScraping,  │    │                 │    │                 │
│   DataSources)  │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### Flujo de Procesamiento RAG

1. **Contenido disponible** → Documentos, web scraping y fuentes de domain-ingestion
2. **RAG Project creado** → Asociado a contenido específico de domain-ingestion
3. **Asignación de Modelos** → Se asignan modelos específicos (embedding, reranker, LLM)
4. **Procesamiento** → Se envía contenido a leka-server para chunking inteligente
5. **Leka-server procesa** → Chunking semántico, extracción de metadata
6. **Chunks retornan** → Con embeddings y metadata enriquecida
7. **Generación de Embeddings** → Usando el modelo de embedding asignado
8. **Almacenamiento** → En PostgreSQL con pgvector para búsqueda
9. **Búsqueda** → Vectorial + textual híbrida con reranking si está configurado
10. **Generación** → Usando el modelo LLM asignado y el sistema TMP para conversaciones
11. **Ejecución en Chatbots** → Los chatbots usan la configuración específica del proyecto RAG

## Entidades del Sistema

### 1. RAGProject

```java
@Entity
@Table(name = "rag_projects")
public class RAGProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column(name = "description")
    private String description;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "project_id")
    private Long projectId; // Referencia al proyecto principal

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "rag_settings")
    private String ragSettings; // JSON configuration específica de RAG

    @Column(name = "vector_config")
    private String vectorConfig; // pgvector configuration

    @Column(name = "chunking_config")
    private String chunkingConfig; // JSON chunking configuration

    @Column(name = "embedding_model")
    private String embeddingModel;

    @Column(name = "leka_server_config")
    private String lekaServerConfig; // JSON leka-server configuration

    // **ASIGNACIÓN DE MODELOS PARA CHATBOTS**
    @Column(name = "embedding_model_id")
    private Long embeddingModelId; // Referencia al modelo de embedding asignado

    @Column(name = "reranker_model_id")
    private Long rerankerModelId; // Referencia al modelo de reranker asignado

    @Column(name = "llm_model_id")
    private Long llmModelId; // Referencia al modelo LLM asignado para generación

    @Column(name = "model_configuration")
    private String modelConfiguration; // JSON configuración específica de modelos

    @Column(name = "chatbot_execution_config")
    private String chatbotExecutionConfig; // JSON configuración para ejecución en chatbots

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relaciones con Domain-Ingestion
    @OneToMany(mappedBy = "ragProject", cascade = CascadeType.ALL)
    private List<RAGDocumentAssignment> documentAssignments = new ArrayList<>();

    @OneToMany(mappedBy = "ragProject", cascade = CascadeType.ALL)
    private List<RAGWebScrapingAssignment> webScrapingAssignments = new ArrayList<>();

    @OneToMany(mappedBy = "ragProject", cascade = CascadeType.ALL)
    private List<RAGDataSourceAssignment> dataSourceAssignments = new ArrayList<>();
}
```

### 2. RAGModel (Modelos para RAG)

```java
@Entity
@Table(name = "rag_models")
public class RAGModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @Column(name = "model_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ModelType modelType;

    @Column(name = "model_provider")
    private String modelProvider; // OpenAI, Anthropic, HuggingFace, Local, etc.

    @Column(name = "model_version")
    private String modelVersion;

    @Column(name = "model_endpoint")
    private String modelEndpoint; // URL del modelo si es externo

    @Column(name = "model_configuration")
    private String modelConfiguration; // JSON configuración específica del modelo

    @Column(name = "is_local")
    private Boolean isLocal = false; // Si es un modelo local o externo

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "performance_metrics")
    private String performanceMetrics; // JSON métricas de rendimiento

    @Column(name = "cost_per_token")
    private Double costPerToken; // Costo por token para modelos externos

    @Column(name = "max_tokens")
    private Integer maxTokens; // Máximo de tokens soportados

    @Column(name = "context_window")
    private Integer contextWindow; // Ventana de contexto del modelo

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum ModelType {
    EMBEDDING,    // Modelos para generar embeddings (text-embedding-ada-002, etc.)
    RERANKER,     // Modelos para reranking de resultados (cohere-rerank, etc.)
    LLM,          // Modelos de lenguaje para generación (GPT-4, Claude, Llama, etc.)
    CHATBOT       // Modelos específicos para chatbots
}
```

### 3. RAGDocumentAssignment (Asignación de Documentos)

```java
@Entity
@Table(name = "rag_document_assignments")
public class RAGDocumentAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rag_project_id", nullable = false)
    private Long ragProjectId;

    @Column(name = "domain_document_id", nullable = false)
    private Long domainDocumentId; // Referencia a dmn_domain_documents

    @Column(name = "processing_status")
    @Enumerated(EnumType.STRING)
    private ProcessingStatus processingStatus = ProcessingStatus.PENDING;

    @Column(name = "leka_job_id")
    private String lekaJobId; // ID del job en leka-server

    @Column(name = "processing_metadata")
    private String processingMetadata; // JSON metadata del procesamiento

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @ManyToOne
    @JoinColumn(name = "rag_project_id", insertable = false, updatable = false)
    private RAGProject ragProject;
}

public enum ProcessingStatus {
    PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED
}
```

### 3. RAGWebScrapingAssignment (Asignación de Web Scraping)

```java
@Entity
@Table(name = "rag_webscraping_assignments")
public class RAGWebScrapingAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rag_project_id", nullable = false)
    private Long ragProjectId;

    @Column(name = "webscraping_job_id", nullable = false)
    private Long webscrapingJobId; // Referencia a dmn_webscraping_jobs

    @Column(name = "processing_status")
    @Enumerated(EnumType.STRING)
    private ProcessingStatus processingStatus = ProcessingStatus.PENDING;

    @Column(name = "leka_job_id")
    private String lekaJobId; // ID del job en leka-server

    @Column(name = "processing_metadata")
    private String processingMetadata; // JSON metadata del procesamiento

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @ManyToOne
    @JoinColumn(name = "rag_project_id", insertable = false, updatable = false)
    private RAGProject ragProject;
}
```

### 4. RAGDataSourceAssignment (Asignación de Fuentes de Datos)

```java
@Entity
@Table(name = "rag_datasource_assignments")
public class RAGDataSourceAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rag_project_id", nullable = false)
    private Long ragProjectId;

    @Column(name = "data_source_id", nullable = false)
    private Long dataSourceId; // Referencia a dmn_data_sources

    @Column(name = "processing_status")
    @Enumerated(EnumType.STRING)
    private ProcessingStatus processingStatus = ProcessingStatus.PENDING;

    @Column(name = "leka_job_id")
    private String lekaJobId; // ID del job en leka-server

    @Column(name = "processing_metadata")
    private String processingMetadata; // JSON metadata del procesamiento

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @ManyToOne
    @JoinColumn(name = "rag_project_id", insertable = false, updatable = false)
    private RAGProject ragProject;
}
```

### 5. DocumentChunk

```java
@Entity
@Table(name = "rag_document_chunks")
public class DocumentChunk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rag_project_id", nullable = false)
    private Long ragProjectId;

    @Column(name = "assignment_id", nullable = false)
    private Long assignmentId; // Referencia a la asignación (document, webscraping, o datasource)

    @Column(name = "assignment_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AssignmentType assignmentType; // DOCUMENT, WEBSCRAPING, DATASOURCE

    @Column(name = "chunk_index")
    private Integer chunkIndex;

    @Column(name = "chunk_text", columnDefinition = "TEXT")
    private String chunkText;

    @Column(name = "chunk_size")
    private Integer chunkSize;

    @Column(name = "start_position")
    private Integer startPosition;

    @Column(name = "end_position")
    private Integer endPosition;

    @Column(name = "overlap_with_previous")
    private Integer overlapWithPrevious;

    @Column(name = "overlap_with_next")
    private Integer overlapWithNext;

    // Metadata enriquecida por leka-server
    @Column(name = "semantic_score")
    private Double semanticScore;

    @Column(name = "keywords")
    private String keywords; // JSON array de keywords extraídos

    @Column(name = "entities")
    private String entities; // JSON array de entidades nombradas

    @Column(name = "summary")
    private String summary; // Resumen del chunk

    @Column(name = "chunk_type")
    @Enumerated(EnumType.STRING)
    private ChunkType chunkType; // Tipo semántico del chunk

    @Column(name = "confidence_score")
    private Double confidenceScore; // Confianza en la clasificación

    // **FUNCIONALIDAD AVANZADA: Tags automáticos**
    @Column(name = "auto_generated_tags")
    private String autoGeneratedTags; // JSON array de tags generados automáticamente

    @Column(name = "custom_tags")
    private String customTags; // JSON array de tags personalizados del usuario

    @Column(name = "tag_generation_metadata")
    private String tagGenerationMetadata; // JSON metadata del proceso de generación de tags

    @Column(name = "leka_metadata")
    private String lekaMetadata; // JSON metadata completa de leka-server

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

public enum AssignmentType {
    DOCUMENT, WEBSCRAPING, DATASOURCE
}

public enum ChunkType {
    INTRODUCTION, MAIN_CONTENT, CONCLUSION, TABLE, FIGURE,
    CODE_BLOCK, QUOTE, FOOTNOTE, HEADER, PARAGRAPH, CUSTOM
}
```

### 4. Embedding

```java
@Entity
@Table(name = "rag_embeddings")
public class Embedding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rag_project_id")
    private Long ragProjectId;

    @Column(name = "document_chunk_id")
    private Long documentChunkId;

    @Column(name = "embedding_model")
    private String embeddingModel;

    @Column(name = "embedding_version")
    private String embeddingVersion;

    @Column(name = "embedding_vector", columnDefinition = "vector(1536)")
    private String embeddingVector; // pgvector column

    @Column(name = "vector_dimension")
    private Integer vectorDimension;

    @Column(name = "similarity_score")
    private Double similarityScore;

    @Column(name = "embedding_metadata")
    private String embeddingMetadata; // JSON metadata del embedding

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
```

### 5. RAGSearch

```java
@Entity
@Table(name = "rag_searches")
public class RAGSearch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rag_project_id")
    private Long ragProjectId;

    @Column(name = "search_query")
    private String searchQuery;

    @Column(name = "search_type")
    @Enumerated(EnumType.STRING)
    private SearchType searchType;

    @Column(name = "search_results")
    private String searchResults; // JSON array de resultados

    @Column(name = "search_metadata")
    private String searchMetadata; // JSON metadata de la búsqueda

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "conversation_id")
    private String conversationId; // Referencia al módulo TMP

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

public enum SearchType {
    VECTOR_SEMANTIC, TEXTUAL_KEYWORD, HYBRID, FILTERED, CUSTOM
}
```

### 6. LekaServerJob

```java
@Entity
@Table(name = "rag_leka_server_jobs")
public class LekaServerJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rag_project_id")
    private Long ragProjectId;

    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "leka_job_id")
    private String lekaJobId; // ID único del job en leka-server

    @Column(name = "job_type")
    @Enumerated(EnumType.STRING)
    private LekaJobType jobType;

    @Column(name = "job_status")
    @Enumerated(EnumType.STRING)
    private LekaJobStatus jobStatus;

    @Column(name = "job_config")
    private String jobConfig; // JSON configuration del job

    @Column(name = "job_result")
    private String jobResult; // JSON resultado del job

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum LekaJobType {
    DOCUMENT_CHUNKING, EMBEDDING_GENERATION, METADATA_EXTRACTION,
    CONTENT_ANALYSIS, AUTO_CLASSIFICATION, CUSTOM
}

public enum LekaJobStatus {
    PENDING, RUNNING, COMPLETED, FAILED, CANCELLED, TIMEOUT
}
```

## Scripts SQL

### DROP TABLES

```sql
-- Eliminar tablas en orden inverso (por dependencias)
DROP TABLE IF EXISTS rag_leka_server_jobs CASCADE;
DROP TABLE IF EXISTS rag_searches CASCADE;
DROP TABLE IF EXISTS rag_embeddings CASCADE;
DROP TABLE IF EXISTS rag_document_chunks CASCADE;
DROP TABLE IF EXISTS rag_datasource_assignments CASCADE;
DROP TABLE IF EXISTS rag_webscraping_assignments CASCADE;
DROP TABLE IF EXISTS rag_document_assignments CASCADE;

DROP TABLE IF EXISTS rag_projects CASCADE;
DROP TABLE IF EXISTS rag_models CASCADE;

```

### CREATE TABLES

```sql
-- Tabla de modelos RAG
CREATE TABLE rag_models (
    id BIGSERIAL PRIMARY KEY,
    model_name VARCHAR(255) NOT NULL,
    model_type VARCHAR(50) NOT NULL,
    model_provider VARCHAR(100),
    model_version VARCHAR(50),
    model_endpoint VARCHAR(500),
    model_configuration TEXT,
    is_local BOOLEAN DEFAULT false,
    is_active BOOLEAN DEFAULT true,
    performance_metrics TEXT,
    cost_per_token DECIMAL(10,8),
    max_tokens INTEGER,
    context_window INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



-- Tabla de proyectos RAG
CREATE TABLE rag_projects (
    id BIGSERIAL PRIMARY KEY,
    project_name VARCHAR(255) NOT NULL,
    description TEXT,
    owner_id BIGINT REFERENCES cor_users(id),
    project_id BIGINT REFERENCES prj_projects(id),
    is_active BOOLEAN DEFAULT true,
    rag_settings TEXT, -- JSON configuration específica de RAG
    vector_config TEXT, -- pgvector configuration
    chunking_config TEXT, -- JSON chunking configuration
    embedding_model VARCHAR(255),
    leka_server_config TEXT, -- JSON leka-server configuration
    -- Asignación de modelos para chatbots
    embedding_model_id BIGINT REFERENCES rag_models(id),
    reranker_model_id BIGINT REFERENCES rag_models(id),
    llm_model_id BIGINT REFERENCES rag_models(id),
    model_configuration TEXT, -- JSON configuración específica de modelos
    chatbot_execution_config TEXT, -- JSON configuración para ejecución en chatbots
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de asignaciones de documentos
CREATE TABLE rag_document_assignments (
    id BIGSERIAL PRIMARY KEY,
    rag_project_id BIGINT REFERENCES rag_projects(id) ON DELETE CASCADE,
    domain_document_id BIGINT REFERENCES dmn_domain_documents(id) ON DELETE CASCADE,
    processing_status VARCHAR(50) DEFAULT 'PENDING',
    leka_job_id VARCHAR(255),
    processing_metadata TEXT,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMP
);

-- Tabla de asignaciones de web scraping
CREATE TABLE rag_webscraping_assignments (
    id BIGSERIAL PRIMARY KEY,
    rag_project_id BIGINT REFERENCES rag_projects(id) ON DELETE CASCADE,
    webscraping_job_id BIGINT REFERENCES dmn_webscraping_jobs(id) ON DELETE CASCADE,
    processing_status VARCHAR(50) DEFAULT 'PENDING',
    leka_job_id VARCHAR(255),
    processing_metadata TEXT,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMP
);

-- Tabla de asignaciones de fuentes de datos
CREATE TABLE rag_datasource_assignments (
    id BIGSERIAL PRIMARY KEY,
    rag_project_id BIGINT REFERENCES rag_projects(id) ON DELETE CASCADE,
    data_source_id BIGINT REFERENCES dmn_data_sources(id) ON DELETE CASCADE,
    processing_status VARCHAR(50) DEFAULT 'PENDING',
    leka_job_id VARCHAR(255),
    processing_metadata TEXT,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMP
);



-- Tabla de chunks de documentos
CREATE TABLE rag_document_chunks (
    id BIGSERIAL PRIMARY KEY,
    rag_project_id BIGINT REFERENCES rag_projects(id) ON DELETE CASCADE,
    assignment_id BIGINT NOT NULL,
    assignment_type VARCHAR(50) NOT NULL, -- DOCUMENT, WEBSCRAPING, DATASOURCE
    chunk_index INTEGER,
    chunk_text TEXT NOT NULL,
    chunk_size INTEGER,
    start_position INTEGER,
    end_position INTEGER,
    overlap_with_previous INTEGER DEFAULT 0,
    overlap_with_next INTEGER DEFAULT 0,
    semantic_score DECIMAL(5,4),
    keywords TEXT, -- JSON array de keywords
    entities TEXT, -- JSON array de entidades
    summary TEXT,
    chunk_type VARCHAR(50),
    confidence_score DECIMAL(5,4),
    -- Funcionalidad avanzada: Tags automáticos
    auto_generated_tags TEXT, -- JSON array de tags generados automáticamente
    custom_tags TEXT, -- JSON array de tags personalizados del usuario
    tag_generation_metadata TEXT, -- JSON metadata del proceso de generación de tags
    leka_metadata TEXT, -- JSON metadata completa de leka-server
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de embeddings
CREATE TABLE rag_embeddings (
    id BIGSERIAL PRIMARY KEY,
    rag_project_id BIGINT REFERENCES rag_projects(id) ON DELETE CASCADE,
    document_chunk_id BIGINT REFERENCES rag_document_chunks(id) ON DELETE CASCADE,
    embedding_model VARCHAR(255) NOT NULL,
    embedding_version VARCHAR(50),
    embedding_vector vector(1536), -- pgvector column
    vector_dimension INTEGER,
    similarity_score DECIMAL(5,4),
    embedding_metadata TEXT, -- JSON metadata del embedding
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de búsquedas RAG
CREATE TABLE rag_searches (
    id BIGSERIAL PRIMARY KEY,
    rag_project_id BIGINT REFERENCES rag_projects(id) ON DELETE CASCADE,
    search_query TEXT NOT NULL,
    search_type VARCHAR(50) NOT NULL,
    search_results TEXT, -- JSON array de resultados
    search_metadata TEXT, -- JSON metadata de la búsqueda
    user_id BIGINT REFERENCES cor_users(id),
    conversation_id VARCHAR(255), -- Referencia al módulo TMP
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de jobs de leka-server
CREATE TABLE rag_leka_server_jobs (
    id BIGSERIAL PRIMARY KEY,
    rag_project_id BIGINT REFERENCES rag_projects(id) ON DELETE CASCADE,
    document_id BIGINT REFERENCES rag_documents(id) ON DELETE CASCADE,
    leka_job_id VARCHAR(255) UNIQUE NOT NULL,
    job_type VARCHAR(50) NOT NULL,
    job_status VARCHAR(50) DEFAULT 'PENDING',
    job_config TEXT, -- JSON configuration del job
    job_result TEXT, -- JSON resultado del job
    error_message TEXT,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### CREATE INDEXES

```sql
-- Índices para optimización
CREATE INDEX idx_rag_projects_owner ON rag_projects(owner_id);
CREATE INDEX idx_rag_projects_project ON rag_projects(project_id);
CREATE INDEX idx_rag_projects_active ON rag_projects(is_active);



-- Índices para asignaciones
CREATE INDEX idx_rag_document_assignments_project ON rag_document_assignments(rag_project_id);
CREATE INDEX idx_rag_document_assignments_document ON rag_document_assignments(domain_document_id);
CREATE INDEX idx_rag_document_assignments_status ON rag_document_assignments(processing_status);

CREATE INDEX idx_rag_webscraping_assignments_project ON rag_webscraping_assignments(rag_project_id);
CREATE INDEX idx_rag_webscraping_assignments_job ON rag_webscraping_assignments(webscraping_job_id);
CREATE INDEX idx_rag_webscraping_assignments_status ON rag_webscraping_assignments(processing_status);

CREATE INDEX idx_rag_datasource_assignments_project ON rag_datasource_assignments(rag_project_id);
CREATE INDEX idx_rag_datasource_assignments_source ON rag_datasource_assignments(data_source_id);
CREATE INDEX idx_rag_datasource_assignments_status ON rag_datasource_assignments(processing_status);



CREATE INDEX idx_rag_document_chunks_project ON rag_document_chunks(rag_project_id);
CREATE INDEX idx_rag_document_chunks_assignment ON rag_document_chunks(assignment_id);
CREATE INDEX idx_rag_document_chunks_type ON rag_document_chunks(assignment_type);
CREATE INDEX idx_rag_document_chunks_index ON rag_document_chunks(chunk_index);

CREATE INDEX idx_rag_embeddings_project ON rag_embeddings(rag_project_id);
CREATE INDEX idx_rag_embeddings_chunk ON rag_embeddings(document_chunk_id);
CREATE INDEX idx_rag_embeddings_model ON rag_embeddings(embedding_model);

CREATE INDEX idx_rag_searches_project ON rag_searches(rag_project_id);
CREATE INDEX idx_rag_searches_type ON rag_searches(search_type);
CREATE INDEX idx_rag_searches_conversation ON rag_searches(conversation_id);

CREATE INDEX idx_rag_leka_server_jobs_project ON rag_leka_server_jobs(rag_project_id);
CREATE INDEX idx_rag_leka_server_jobs_document ON rag_leka_server_jobs(document_id);
CREATE INDEX idx_rag_leka_server_jobs_status ON rag_leka_server_jobs(job_status);
CREATE INDEX idx_rag_leka_server_jobs_leka_id ON rag_leka_server_jobs(leka_job_id);

-- Índices vectoriales para pgvector
CREATE INDEX idx_rag_embeddings_vector ON rag_embeddings USING ivfflat (embedding_vector vector_cosine_ops);
```

### INSERT DEMO DATA

```sql
-- Insertar modelos RAG de demostración
INSERT INTO rag_models (model_name, model_type, model_provider, model_version, model_endpoint, is_local, is_active, cost_per_token, max_tokens, context_window) VALUES
('text-embedding-ada-002', 'EMBEDDING', 'OpenAI', '2.0', 'https://api.openai.com/v1/embeddings', false, true, 0.0001, 8191, 8191),
('cohere-rerank-multilingual-v2.0', 'RERANKER', 'Cohere', '2.0', 'https://api.cohere.ai/v1/rerank', false, true, 0.001, 1000, 1000),
('gpt-4-turbo', 'LLM', 'OpenAI', '4.0', 'https://api.openai.com/v1/chat/completions', false, true, 0.01, 128000, 128000),
('llama-2-70b-chat', 'LLM', 'Local', '2.0', 'http://localhost:11434', true, true, 0.0, 4096, 4096),
('claude-3-opus', 'LLM', 'Anthropic', '3.0', 'https://api.anthropic.com/v1/messages', false, true, 0.015, 200000, 200000);

-- Insertar proyectos RAG de demostración
INSERT INTO rag_projects (project_name, description, owner_id, project_id, rag_settings, vector_config, chunking_config, embedding_model, leka_server_config, embedding_model_id, reranker_model_id, llm_model_id, model_configuration, chatbot_execution_config, is_active) VALUES
('DemoRAGProject', 'Proyecto RAG de demostración para testing', 1, 1,
 '{"max_chunk_size": 1000, "chunk_overlap": 200, "semantic_chunking": true}',
 '{"vector_dimension": 1536, "index_type": "ivfflat", "distance_metric": "cosine"}',
 '{"chunking_strategy": "semantic", "min_chunk_size": 100, "max_chunk_size": 1000, "overlap_percentage": 20}',
 'text-embedding-ada-002',
 '{"endpoint": "http://localhost:8002", "api_key": "demo_key", "timeout": 30000}',
 1, 2, 3, -- IDs de los modelos asignados
 '{"temperature": 0.7, "max_tokens": 1000, "top_p": 0.9}',
 '{"chatbot_id": "demo_chatbot", "response_style": "professional", "include_sources": true}',
 true),

('Ecommerce Knowledge Base', 'Base de conocimiento para chatbot de ecommerce con productos y políticas', 1, 2,
 '{"max_chunk_size": 800, "chunk_overlap": 150, "semantic_chunking": true, "product_focus": true}',
 '{"vector_dimension": 1536, "index_type": "ivfflat", "distance_metric": "cosine"}',
 '{"chunking_strategy": "semantic", "min_chunk_size": 80, "max_chunk_size": 800, "overlap_percentage": 18}',
 'text-embedding-ada-002',
 '{"endpoint": "http://localhost:8002", "api_key": "ecommerce_key", "timeout": 30000}',
 1, 2, 4, -- Llama local para ecommerce
 '{"temperature": 0.5, "max_tokens": 800, "top_p": 0.8}',
 '{"chatbot_id": "ecommerce_chatbot", "response_style": "friendly", "include_sources": true, "product_recommendations": true}',
 true),

('Legal Document Assistant', 'Asistente legal para análisis de documentos y consultas jurídicas', 2, 3,
 '{"max_chunk_size": 1200, "chunk_overlap": 250, "semantic_chunking": true, "legal_focus": true}',
 '{"vector_dimension": 1536, "index_type": "ivfflat", "distance_metric": "cosine"}',
 '{"chunking_strategy": "semantic", "min_chunk_size": 120, "max_chunk_size": 1200, "overlap_percentage": 20}',
 'text-embedding-ada-002',
 '{"endpoint": "http://localhost:8002", "api_key": "legal_key", "timeout": 45000}',
 1, 2, 5, -- Claude para legal
 '{"temperature": 0.3, "max_tokens": 1500, "top_p": 0.7}',
 '{"chatbot_id": "legal_chatbot", "response_style": "formal", "include_sources": true, "legal_citations": true}',
 true),

('Technical Support RAG', 'Sistema RAG para soporte técnico con documentación y troubleshooting', 1, 4,
 '{"max_chunk_size": 600, "chunk_overlap": 100, "semantic_chunking": true, "technical_focus": true}',
 '{"vector_dimension": 1536, "index_type": "ivfflat", "distance_metric": "cosine"}',
 '{"chunking_strategy": "semantic", "min_chunk_size": 60, "max_chunk_size": 600, "overlap_percentage": 16}',
 'text-embedding-ada-002',
 '{"endpoint": "http://localhost:8002", "api_key": "support_key", "timeout": 30000}',
 1, 2, 3, -- GPT-4 para soporte técnico
 '{"temperature": 0.4, "max_tokens": 1200, "top_p": 0.8}',
 '{"chatbot_id": "support_chatbot", "response_style": "helpful", "include_sources": true, "troubleshooting_steps": true}',
 true);

-- Insertar asignaciones de demostración
INSERT INTO rag_document_assignments (rag_project_id, domain_document_id, processing_status, leka_job_id, processing_metadata) VALUES
(1, 1, 'COMPLETED', 'leka_job_001', '{"chunks_generated": 5, "processing_time_ms": 2500}'),
(2, 2, 'COMPLETED', 'leka_job_004', '{"chunks_generated": 8, "processing_time_ms": 3200}'),
(3, 3, 'COMPLETED', 'leka_job_005', '{"chunks_generated": 12, "processing_time_ms": 4500}'),
(4, 4, 'COMPLETED', 'leka_job_006', '{"chunks_generated": 6, "processing_time_ms": 2800}');

INSERT INTO rag_webscraping_assignments (rag_project_id, webscraping_job_id, processing_status, leka_job_id, processing_metadata) VALUES
(1, 1, 'COMPLETED', 'leka_job_002', '{"pages_scraped": 10, "data_extracted": true}'),
(2, 2, 'COMPLETED', 'leka_job_007', '{"pages_scraped": 15, "data_extracted": true, "product_data": true}'),
(3, 3, 'COMPLETED', 'leka_job_008', '{"pages_scraped": 8, "data_extracted": true, "legal_documents": true}'),
(4, 4, 'COMPLETED', 'leka_job_009', '{"pages_scraped": 20, "data_extracted": true, "technical_docs": true}');

INSERT INTO rag_datasource_assignments (rag_project_id, data_source_id, processing_status, leka_job_id, processing_metadata) VALUES
(1, 1, 'COMPLETED', 'leka_job_003', '{"records_processed": 100, "sync_successful": true}'),
(2, 2, 'COMPLETED', 'leka_job_010', '{"records_processed": 250, "sync_successful": true, "product_catalog": true}'),
(3, 3, 'COMPLETED', 'leka_job_011', '{"records_processed": 80, "sync_successful": true, "legal_database": true}'),
(4, 4, 'COMPLETED', 'leka_job_012', '{"records_processed": 150, "sync_successful": true, "knowledge_base": true}');

-- Insertar chunks de demostración
INSERT INTO rag_document_chunks (rag_project_id, assignment_id, assignment_type, chunk_index, chunk_text, chunk_size, start_position, end_position, overlap_with_previous, overlap_with_next, semantic_score, keywords, entities, summary, chunk_type, confidence_score, auto_generated_tags, custom_tags, tag_generation_metadata, leka_metadata) VALUES
-- Proyecto 1: DemoRAGProject
(1, 1, 'DOCUMENT', 1, 'Este es el primer chunk del documento de demostración. Contiene información introductoria sobre el tema principal.', 120, 0, 120, 0, 20, 0.85, '["introducción", "tema", "principal"]', '["documento", "demostración"]', 'Chunk introductorio del documento', 'INTRODUCTION', 0.92, '["intro", "overview", "basics"]', '["important", "demo"]', '{"model": "gpt-4", "confidence": 0.95, "generated_at": "2024-01-01T10:00:00Z"}', '{"semantic_analysis": {"topic": "introducción", "sentiment": "neutral"}, "extracted_entities": ["documento", "demostración"], "confidence": 0.92}'),
(1, 1, 'DOCUMENT', 2, 'El segundo chunk continúa con más detalles sobre el tema. Incluye ejemplos y explicaciones adicionales.', 130, 100, 230, 20, 15, 0.78, '["detalles", "ejemplos", "explicaciones"]', '["tema", "ejemplos"]', 'Chunk con detalles y ejemplos', 'MAIN_CONTENT', 0.89, '["details", "examples", "explanations"]', '["technical", "advanced"]', '{"model": "gpt-4", "confidence": 0.88, "generated_at": "2024-01-01T10:01:00Z"}', '{"semantic_analysis": {"topic": "desarrollo", "sentiment": "informative"}, "extracted_entities": ["tema", "ejemplos"], "confidence": 0.89}'),

-- Proyecto 2: Ecommerce Knowledge Base
(2, 2, 'DOCUMENT', 1, 'Política de devoluciones: Los clientes pueden devolver productos dentro de 30 días desde la compra. El producto debe estar en condiciones originales.', 140, 0, 140, 0, 25, 0.91, '["devoluciones", "política", "30 días"]', '["cliente", "producto", "compra"]', 'Política de devoluciones', 'POLICY', 0.94, '["returns", "policy", "customer"]', '["important", "ecommerce"]', '{"model": "gpt-4", "confidence": 0.96, "generated_at": "2024-01-01T11:00:00Z"}', '{"semantic_analysis": {"topic": "políticas", "sentiment": "neutral"}, "extracted_entities": ["cliente", "producto"], "confidence": 0.94}'),
(2, 2, 'DOCUMENT', 2, 'Guía de productos: Nuestro catálogo incluye más de 10,000 productos en categorías como electrónicos, hogar, moda y deportes.', 135, 115, 250, 25, 20, 0.87, '["catálogo", "productos", "categorías"]', '["electrónicos", "hogar", "moda"]', 'Descripción del catálogo', 'PRODUCT_INFO', 0.91, '["catalog", "products", "categories"]', '["business", "inventory"]', '{"model": "gpt-4", "confidence": 0.93, "generated_at": "2024-01-01T11:01:00Z"}', '{"semantic_analysis": {"topic": "productos", "sentiment": "positive"}, "extracted_entities": ["electrónicos", "hogar"], "confidence": 0.91}'),

-- Proyecto 3: Legal Document Assistant
(3, 3, 'DOCUMENT', 1, 'Artículo 15: El derecho a la protección de datos personales es fundamental. Toda persona tiene derecho a conocer, actualizar y rectificar sus datos.', 145, 0, 145, 0, 30, 0.93, '["protección", "datos", "personales", "derecho"]', '["persona", "datos", "rectificar"]', 'Derecho a protección de datos', 'LEGAL_ARTICLE', 0.96, '["privacy", "data_protection", "rights"]', '["legal", "gdpr"]', '{"model": "gpt-4", "confidence": 0.97, "generated_at": "2024-01-01T12:00:00Z"}', '{"semantic_analysis": {"topic": "derechos", "sentiment": "formal"}, "extracted_entities": ["persona", "datos"], "confidence": 0.96}'),
(3, 3, 'DOCUMENT', 2, 'Responsabilidades del responsable del tratamiento: Implementar medidas técnicas y organizativas apropiadas para garantizar la seguridad de los datos.', 150, 115, 265, 30, 25, 0.89, '["responsabilidades", "medidas", "seguridad", "datos"]', '["responsable", "tratamiento", "medidas"]', 'Responsabilidades del responsable', 'LEGAL_OBLIGATION', 0.92, '["responsibilities", "security", "compliance"]', '["legal", "obligation"]', '{"model": "gpt-4", "confidence": 0.94, "generated_at": "2024-01-01T12:01:00Z"}', '{"semantic_analysis": {"topic": "obligaciones", "sentiment": "formal"}, "extracted_entities": ["responsable", "medidas"], "confidence": 0.92}'),

-- Proyecto 4: Technical Support RAG
(4, 4, 'DOCUMENT', 1, 'Solución de problemas de conexión: Verificar configuración de red, reiniciar router, comprobar cables de conexión y actualizar drivers de red.', 160, 0, 160, 0, 20, 0.86, '["conexión", "red", "router", "drivers"]', '["configuración", "cables", "drivers"]', 'Troubleshooting de conexión', 'TROUBLESHOOTING', 0.88, '["network", "troubleshooting", "connection"]', '["technical", "support"]', '{"model": "gpt-4", "confidence": 0.90, "generated_at": "2024-01-01T13:00:00Z"}', '{"semantic_analysis": {"topic": "soporte", "sentiment": "helpful"}, "extracted_entities": ["configuración", "cables"], "confidence": 0.88}'),
(4, 4, 'DOCUMENT', 2, 'Configuración de firewall: Permitir puertos 80, 443, 8080. Configurar excepciones para aplicaciones específicas y verificar reglas de entrada y salida.', 155, 140, 295, 20, 15, 0.84, '["firewall", "puertos", "excepciones", "reglas"]', '["puertos", "aplicaciones", "reglas"]', 'Configuración de firewall', 'CONFIGURATION', 0.87, '["firewall", "ports", "configuration"]', '["security", "network"]', '{"model": "gpt-4", "confidence": 0.89, "generated_at": "2024-01-01T13:01:00Z"}', '{"semantic_analysis": {"topic": "configuración", "sentiment": "technical"}, "extracted_entities": ["puertos", "aplicaciones"], "confidence": 0.87}');

-- Insertar embeddings de demostración
INSERT INTO rag_embeddings (rag_project_id, document_chunk_id, embedding_model, embedding_version, vector_dimension, similarity_score, embedding_metadata) VALUES
-- Proyecto 1: DemoRAGProject
(1, 1, 'text-embedding-ada-002', '1.0.0', 1536, 0.85, '{"model_version": "1.0.0", "generation_timestamp": "2024-01-01T00:00:00Z", "vector_quality": "high"}'),
(1, 2, 'text-embedding-ada-002', '1.0.0', 1536, 0.78, '{"model_version": "1.0.0", "generation_timestamp": "2024-01-01T00:00:00Z", "vector_quality": "high"}'),
-- Proyecto 2: Ecommerce Knowledge Base
(2, 3, 'text-embedding-ada-002', '1.0.0', 1536, 0.91, '{"model_version": "1.0.0", "generation_timestamp": "2024-01-01T01:00:00Z", "vector_quality": "high", "ecommerce_optimized": true}'),
(2, 4, 'text-embedding-ada-002', '1.0.0', 1536, 0.87, '{"model_version": "1.0.0", "generation_timestamp": "2024-01-01T01:00:00Z", "vector_quality": "high", "ecommerce_optimized": true}'),
-- Proyecto 3: Legal Document Assistant
(3, 5, 'text-embedding-ada-002', '1.0.0', 1536, 0.93, '{"model_version": "1.0.0", "generation_timestamp": "2024-01-01T02:00:00Z", "vector_quality": "high", "legal_optimized": true}'),
(3, 6, 'text-embedding-ada-002', '1.0.0', 1536, 0.89, '{"model_version": "1.0.0", "generation_timestamp": "2024-01-01T02:00:00Z", "vector_quality": "high", "legal_optimized": true}'),
-- Proyecto 4: Technical Support RAG
(4, 7, 'text-embedding-ada-002', '1.0.0', 1536, 0.86, '{"model_version": "1.0.0", "generation_timestamp": "2024-01-01T03:00:00Z", "vector_quality": "high", "technical_optimized": true}'),
(4, 8, 'text-embedding-ada-002', '1.0.0', 1536, 0.84, '{"model_version": "1.0.0", "generation_timestamp": "2024-01-01T03:00:00Z", "vector_quality": "high", "technical_optimized": true}');

-- Insertar job de leka-server de demostración
INSERT INTO rag_leka_server_jobs (rag_project_id, document_id, leka_job_id, job_type, job_status, job_config, job_result, started_at, completed_at) VALUES
(1, 1, 'leka_job_001', 'DOCUMENT_CHUNKING', 'COMPLETED', '{"chunking_strategy": "semantic", "min_chunk_size": 100, "max_chunk_size": 1000}', '{"chunks_generated": 2, "processing_time_ms": 1500, "quality_score": 0.89}', '2024-01-01 10:00:00', '2024-01-01 10:00:01');
```

## API Endpoints

### RAG Models

```plaintext
GET    /api/v1/rag/models
GET    /api/v1/rag/models/{id}
POST   /api/v1/rag/models
PUT    /api/v1/rag/models/{id}
DELETE /api/v1/rag/models/{id}
GET    /api/v1/rag/models/type/{type}
GET    /api/v1/rag/models/provider/{provider}
POST   /api/v1/rag/models/{id}/test
```

### RAG Projects

```plaintext
GET    /api/v1/rag/projects
GET    /api/v1/rag/projects/{id}
POST   /api/v1/rag/projects
PUT    /api/v1/rag/projects/{id}
DELETE /api/v1/rag/projects/{id}
GET    /api/v1/rag/projects/user/{userId}
GET    /api/v1/rag/projects/search
POST   /api/v1/rag/projects/{id}/activate
POST   /api/v1/rag/projects/{id}/deactivate
POST   /api/v1/rag/projects/{id}/assign-models
GET    /api/v1/rag/projects/{id}/models
```

### RAG Assignments

```plaintext
POST   /api/v1/rag/projects/{id}/assign-documents
POST   /api/v1/rag/projects/{id}/assign-webscraping
POST   /api/v1/rag/projects/{id}/assign-datasources
POST   /api/v1/rag/projects/{id}/assign-apis
POST   /api/v1/rag/projects/{id}/assign-databases
GET    /api/v1/rag/projects/{id}/assignments
GET    /api/v1/rag/projects/{id}/assignments/documents
GET    /api/v1/rag/projects/{id}/assignments/webscraping
GET    /api/v1/rag/projects/{id}/assignments/datasources
GET    /api/v1/rag/projects/{id}/assignments/apis
GET    /api/v1/rag/projects/{id}/assignments/databases
DELETE /api/v1/rag/projects/{id}/assignments/{assignmentId}
```

### RAG API Endpoints

```plaintext
GET    /api/v1/rag/api-endpoints
GET    /api/v1/rag/api-endpoints/{id}
POST   /api/v1/rag/api-endpoints
PUT    /api/v1/rag/api-endpoints/{id}
DELETE /api/v1/rag/api-endpoints/{id}
GET    /api/v1/rag/api-endpoints/type/{type}
GET    /api/v1/rag/api-endpoints/provider/{provider}
GET    /api/v1/rag/api-endpoints/status/{status}
POST   /api/v1/rag/api-endpoints/{id}/test
POST   /api/v1/rag/api-endpoints/{id}/toggle-status
GET    /api/v1/rag/api-endpoints/{id}/usage-stats
GET    /api/v1/rag/api-endpoints/{id}/assignments
```

### RAG Database Connections

```plaintext
GET    /api/v1/rag/database-connections
GET    /api/v1/rag/database-connections/{id}
POST   /api/v1/rag/database-connections
PUT    /api/v1/rag/database-connections/{id}
DELETE /api/v1/rag/database-connections/{id}
GET    /api/v1/rag/database-connections/type/{type}
GET    /api/v1/rag/database-connections/status/{status}
POST   /api/v1/rag/database-connections/{id}/test
POST   /api/v1/rag/database-connections/{id}/toggle-status
GET    /api/v1/rag/database-connections/{id}/usage-stats
GET    /api/v1/rag/database-connections/{id}/assignments
```

### Documents

```plaintext
GET    /api/v1/rag/projects/{projectId}/documents
GET    /api/v1/rag/projects/{projectId}/documents/{id}
POST   /api/v1/rag/projects/{projectId}/documents
PUT    /api/v1/rag/projects/{projectId}/documents/{id}
DELETE /api/v1/rag/projects/{projectId}/documents/{id}
POST   /api/v1/rag/projects/{projectId}/documents/upload
POST   /api/v1/rag/projects/{projectId}/documents/batch-upload
GET    /api/v1/rag/projects/{projectId}/documents/search
GET    /api/v1/rag/projects/{projectId}/documents/status/{status}
POST   /api/v1/rag/projects/{projectId}/documents/{id}/process
POST   /api/v1/rag/projects/{projectId}/documents/{id}/reprocess
```

### Document Chunks

```plaintext
GET    /api/v1/rag/documents/{documentId}/chunks
GET    /api/v1/rag/documents/{documentId}/chunks/{id}
POST   /api/v1/rag/documents/{documentId}/chunks
PUT    /api/v1/rag/documents/{documentId}/chunks/{id}
DELETE /api/v1/rag/documents/{documentId}/chunks/{id}
GET    /api/v1/rag/documents/{documentId}/chunks/search
POST   /api/v1/rag/documents/{documentId}/chunks/optimize
GET    /api/v1/rag/documents/{documentId}/chunks/semantic-analysis
```

### Embeddings

```plaintext
GET    /api/v1/rag/projects/{projectId}/embeddings
GET    /api/v1/rag/projects/{projectId}/embeddings/{id}
POST   /api/v1/rag/projects/{projectId}/embeddings
PUT    /api/v1/rag/projects/{projectId}/embeddings/{id}
DELETE /api/v1/rag/projects/{projectId}/embeddings/{id}
POST   /api/v1/rag/projects/{projectId}/embeddings/generate
POST   /api/v1/rag/projects/{projectId}/embeddings/batch-generate
GET    /api/v1/rag/projects/{projectId}/embeddings/search
POST   /api/v1/rag/projects/{projectId}/embeddings/similarity-search
```

### RAG Search

```plaintext
POST   /api/v1/rag/projects/{projectId}/search
POST   /api/v1/rag/projects/{projectId}/search/semantic
POST   /api/v1/rag/projects/{projectId}/search/hybrid
POST   /api/v1/rag/projects/{projectId}/search/filtered
GET    /api/v1/rag/projects/{projectId}/search/history
GET    /api/v1/rag/projects/{projectId}/search/analytics
```

### Leka Server Integration

```plaintext
POST   /api/v1/rag/projects/{projectId}/leka-server/jobs
GET    /api/v1/rag/projects/{projectId}/leka-server/jobs
GET    /api/v1/rag/projects/{projectId}/leka-server/jobs/{id}
POST   /api/v1/rag/projects/{projectId}/leka-server/jobs/{id}/cancel
GET    /api/v1/rag/projects/{projectId}/leka-server/jobs/status/{status}
POST   /api/v1/rag/projects/{projectId}/leka-server/test-connection
```

### Advanced RAG Features

```plaintext
POST   /api/v1/rag/projects/{projectId}/semantic-search
POST   /api/v1/rag/projects/{projectId}/hybrid-search
POST   /api/v1/rag/projects/{projectId}/context-aware-generation
POST   /api/v1/rag/projects/{projectId}/knowledge-graph-query
POST   /api/v1/rag/projects/{projectId}/document-summarization
POST   /api/v1/rag/projects/{projectId}/question-answering
```

### RAG Analytics and Monitoring

```plaintext
GET    /api/v1/rag/projects/{projectId}/analytics/overview
GET    /api/v1/rag/projects/{projectId}/analytics/usage
GET    /api/v1/rag/projects/{projectId}/analytics/performance
GET    /api/v1/rag/projects/{projectId}/analytics/quality
GET    /api/v1/rag/projects/{projectId}/analytics/costs
GET    /api/v1/rag/projects/{projectId}/analytics/search-analytics
GET    /api/v1/rag/projects/{projectId}/analytics/document-insights
```

## Servicios Java

### RAGApiEndpointService

```java
@Service
public class RAGApiEndpointService {

    @Autowired
    private RAGApiEndpointRepository apiEndpointRepository;

    @Autowired
    private RAGApiAssignmentRepository apiAssignmentRepository;

    public RAGApiEndpoint createApiEndpoint(RAGApiEndpointCreateRequest request) {
        RAGApiEndpoint endpoint = new RAGApiEndpoint();
        endpoint.setName(request.getName());
        endpoint.setDescription(request.getDescription());
        endpoint.setUrl(request.getUrl());
        endpoint.setMethod(request.getMethod());
        endpoint.setType(request.getType());
        endpoint.setProvider(request.getProvider());
        endpoint.setVersion(request.getVersion());
        endpoint.setRateLimit(request.getRateLimit());
        endpoint.setCostPerRequest(request.getCostPerRequest());
        endpoint.setConfiguration(request.getConfiguration());
        endpoint.setMonitoring(request.getMonitoring());
        endpoint.setCreatedAt(LocalDateTime.now());
        endpoint.setUpdatedAt(LocalDateTime.now());

        return apiEndpointRepository.save(endpoint);
    }

    public RAGApiEndpoint updateApiEndpoint(Long id, RAGApiEndpointUpdateRequest request) {
        RAGApiEndpoint endpoint = apiEndpointRepository.findById(id)
            .orElseThrow(() -> new ApiEndpointNotFoundException("API Endpoint not found"));

        endpoint.setName(request.getName());
        endpoint.setDescription(request.getDescription());
        endpoint.setUrl(request.getUrl());
        endpoint.setMethod(request.getMethod());
        endpoint.setType(request.getType());
        endpoint.setProvider(request.getProvider());
        endpoint.setVersion(request.getVersion());
        endpoint.setRateLimit(request.getRateLimit());
        endpoint.setCostPerRequest(request.getCostPerRequest());
        endpoint.setConfiguration(request.getConfiguration());
        endpoint.setMonitoring(request.getMonitoring());
        endpoint.setUpdatedAt(LocalDateTime.now());

        return apiEndpointRepository.save(endpoint);
    }

    public void deleteApiEndpoint(Long id) {
        RAGApiEndpoint endpoint = apiEndpointRepository.findById(id)
            .orElseThrow(() -> new ApiEndpointNotFoundException("API Endpoint not found"));

        // Verificar si hay asignaciones activas
        List<RAGApiAssignment> assignments = apiAssignmentRepository.findByApiEndpointId(id);
        if (!assignments.isEmpty()) {
            throw new ApiEndpointInUseException("Cannot delete API endpoint with active assignments");
        }

        apiEndpointRepository.delete(endpoint);
    }

    public RAGApiEndpoint testApiEndpoint(Long id) {
        RAGApiEndpoint endpoint = apiEndpointRepository.findById(id)
            .orElseThrow(() -> new ApiEndpointNotFoundException("API Endpoint not found"));

        try {
            // Realizar test de conectividad
            ApiTestResult result = performApiTest(endpoint);

            // Actualizar métricas
            endpoint.setResponseTime(result.getResponseTime());
            endpoint.setSuccessRate(result.getSuccessRate());
            endpoint.setLastUsed(LocalDateTime.now());
            endpoint.setStatus(result.isSuccess() ? ApiStatus.ACTIVE : ApiStatus.ERROR);
            endpoint.setUpdatedAt(LocalDateTime.now());

            return apiEndpointRepository.save(endpoint);
        } catch (Exception e) {
            endpoint.setStatus(ApiStatus.ERROR);
            endpoint.setUpdatedAt(LocalDateTime.now());
            apiEndpointRepository.save(endpoint);
            throw new ApiTestException("API test failed: " + e.getMessage());
        }
    }

    public RAGApiEndpoint toggleApiEndpointStatus(Long id) {
        RAGApiEndpoint endpoint = apiEndpointRepository.findById(id)
            .orElseThrow(() -> new ApiEndpointNotFoundException("API Endpoint not found"));

        endpoint.setStatus(endpoint.getStatus() == ApiStatus.ACTIVE ? ApiStatus.INACTIVE : ApiStatus.ACTIVE);
        endpoint.setUpdatedAt(LocalDateTime.now());

        return apiEndpointRepository.save(endpoint);
    }

    public List<RAGApiAssignment> getApiEndpointAssignments(Long id) {
        return apiAssignmentRepository.findByApiEndpointId(id);
    }

    public ApiUsageStats getApiEndpointUsageStats(Long id) {
        RAGApiEndpoint endpoint = apiEndpointRepository.findById(id)
            .orElseThrow(() -> new ApiEndpointNotFoundException("API Endpoint not found"));

        List<RAGApiAssignment> assignments = apiAssignmentRepository.findByApiEndpointId(id);

        return ApiUsageStats.builder()
            .totalAssignments(assignments.size())
            .activeAssignments(assignments.stream()
                .filter(a -> a.getProcessingStatus() == ProcessingStatus.ACTIVE)
                .count())
            .totalRequests(endpoint.getTotalRequests())
            .averageResponseTime(endpoint.getResponseTime())
            .successRate(endpoint.getSuccessRate())
            .lastUsed(endpoint.getLastUsed())
            .build();
    }

    private ApiTestResult performApiTest(RAGApiEndpoint endpoint) {
        // Implementar test de API según el tipo y configuración
        // Retornar resultado con métricas
        return new ApiTestResult();
    }
}
```

### RAGDatabaseConnectionService

```java
@Service
public class RAGDatabaseConnectionService {

    @Autowired
    private RAGDatabaseConnectionRepository databaseConnectionRepository;

    @Autowired
    private RAGDatabaseAssignmentRepository databaseAssignmentRepository;

    public RAGDatabaseConnection createDatabaseConnection(RAGDatabaseConnectionCreateRequest request) {
        RAGDatabaseConnection connection = new RAGDatabaseConnection();
        connection.setName(request.getName());
        connection.setDescription(request.getDescription());
        connection.setConnectionType(request.getConnectionType());
        connection.setHost(request.getHost());
        connection.setPort(request.getPort());
        connection.setDatabaseName(request.getDatabaseName());
        connection.setUsername(request.getUsername());
        connection.setPasswordEncrypted(encryptPassword(request.getPassword()));
        connection.setConnectionString(request.getConnectionString());
        connection.setIsSsl(request.getIsSsl());
        connection.setConnectionPoolSize(request.getConnectionPoolSize());
        connection.setTimeout(request.getTimeout());
        connection.setMaxConnections(request.getMaxConnections());
        connection.setConfiguration(request.getConfiguration());
        connection.setMonitoring(request.getMonitoring());
        connection.setCreatedAt(LocalDateTime.now());
        connection.setUpdatedAt(LocalDateTime.now());

        return databaseConnectionRepository.save(connection);
    }

    public RAGDatabaseConnection updateDatabaseConnection(Long id, RAGDatabaseConnectionUpdateRequest request) {
        RAGDatabaseConnection connection = databaseConnectionRepository.findById(id)
            .orElseThrow(() -> new DatabaseConnectionNotFoundException("Database Connection not found"));

        connection.setName(request.getName());
        connection.setDescription(request.getDescription());
        connection.setConnectionType(request.getConnectionType());
        connection.setHost(request.getHost());
        connection.setPort(request.getPort());
        connection.setDatabaseName(request.getDatabaseName());
        connection.setUsername(request.getUsername());
        if (request.getPassword() != null) {
            connection.setPasswordEncrypted(encryptPassword(request.getPassword()));
        }
        connection.setConnectionString(request.getConnectionString());
        connection.setIsSsl(request.getIsSsl());
        connection.setConnectionPoolSize(request.getConnectionPoolSize());
        connection.setTimeout(request.getTimeout());
        connection.setMaxConnections(request.getMaxConnections());
        connection.setConfiguration(request.getConfiguration());
        connection.setMonitoring(request.getMonitoring());
        connection.setUpdatedAt(LocalDateTime.now());

        return databaseConnectionRepository.save(connection);
    }

    public void deleteDatabaseConnection(Long id) {
        RAGDatabaseConnection connection = databaseConnectionRepository.findById(id)
            .orElseThrow(() -> new DatabaseConnectionNotFoundException("Database Connection not found"));

        // Verificar si hay asignaciones activas
        List<RAGDatabaseAssignment> assignments = databaseAssignmentRepository.findByDatabaseConnectionId(id);
        if (!assignments.isEmpty()) {
            throw new DatabaseConnectionInUseException("Cannot delete database connection with active assignments");
        }

        databaseConnectionRepository.delete(connection);
    }

    public RAGDatabaseConnection testDatabaseConnection(Long id) {
        RAGDatabaseConnection connection = databaseConnectionRepository.findById(id)
            .orElseThrow(() -> new DatabaseConnectionNotFoundException("Database Connection not found"));

        try {
            // Realizar test de conectividad
            DatabaseTestResult result = performDatabaseTest(connection);

            // Actualizar estado
            connection.setStatus(result.isSuccess() ? ConnectionStatus.ACTIVE : ConnectionStatus.ERROR);
            connection.setLastTested(LocalDateTime.now());
            connection.setUpdatedAt(LocalDateTime.now());

            return databaseConnectionRepository.save(connection);
        } catch (Exception e) {
            connection.setStatus(ConnectionStatus.ERROR);
            connection.setUpdatedAt(LocalDateTime.now());
            databaseConnectionRepository.save(connection);
            throw new DatabaseTestException("Database test failed: " + e.getMessage());
        }
    }

    public RAGDatabaseConnection toggleDatabaseConnectionStatus(Long id) {
        RAGDatabaseConnection connection = databaseConnectionRepository.findById(id)
            .orElseThrow(() -> new DatabaseConnectionNotFoundException("Database Connection not found"));

        connection.setStatus(connection.getStatus() == ConnectionStatus.ACTIVE ? ConnectionStatus.INACTIVE : ConnectionStatus.ACTIVE);
        connection.setUpdatedAt(LocalDateTime.now());

        return databaseConnectionRepository.save(connection);
    }

    public List<RAGDatabaseAssignment> getDatabaseConnectionAssignments(Long id) {
        return databaseAssignmentRepository.findByDatabaseConnectionId(id);
    }

    public DatabaseUsageStats getDatabaseConnectionUsageStats(Long id) {
        RAGDatabaseConnection connection = databaseConnectionRepository.findById(id)
            .orElseThrow(() -> new DatabaseConnectionNotFoundException("Database Connection not found"));

        List<RAGDatabaseAssignment> assignments = databaseAssignmentRepository.findByDatabaseConnectionId(id);

        return DatabaseUsageStats.builder()
            .totalAssignments(assignments.size())
            .activeAssignments(assignments.stream()
                .filter(a -> a.getProcessingStatus() == ProcessingStatus.ACTIVE)
                .count())
            .lastTested(connection.getLastTested())
            .connectionPoolSize(connection.getConnectionPoolSize())
            .maxConnections(connection.getMaxConnections())
            .build();
    }

    private String encryptPassword(String password) {
        // Implementar encriptación de contraseña
        return password; // Placeholder
    }

    private DatabaseTestResult performDatabaseTest(RAGDatabaseConnection connection) {
        // Implementar test de base de datos según el tipo
        // Retornar resultado con métricas
        return new DatabaseTestResult();
    }
}
```

### RAGService

```java
@Service
public class RAGService {

    @Autowired
    private RAGProjectRepository projectRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private LekaServerService lekaServerService;

    @Autowired
    private EmbeddingService embeddingService;

    public RAGProject createRAGProject(RAGProjectCreateRequest request) {
        RAGProject project = new RAGProject();
        project.setProjectName(request.getProjectName());
        project.setDescription(request.getDescription());
        project.setOwnerId(getCurrentUserId());
        project.setProjectId(request.getProjectId());
        project.setRagSettings(request.getRagSettings());
        project.setVectorConfig(request.getVectorConfig());
        project.setChunkingConfig(request.getChunkingConfig());
        project.setEmbeddingModel(request.getEmbeddingModel());
        project.setLekaServerConfig(request.getLekaServerConfig());

        return projectRepository.save(project);
    }

    public Document processDocument(Long projectId, MultipartFile file) {
        RAGProject project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RAGProjectNotFoundException("Project not found"));

        // Subir archivo a MinIO
        String filePath = uploadToMinIO(file);

        // Crear documento
        Document document = new Document();
        document.setRagProjectId(projectId);
        document.setDocumentName(file.getOriginalFilename());
        document.setDocumentType(determineDocumentType(file));
        document.setFilePath(filePath);
        document.setFileSize(file.getSize());
        document.setMimeType(file.getContentType());
        document.setProcessingStatus(ProcessingStatus.PENDING);

        Document savedDocument = documentRepository.save(document);

        // Enviar a leka-server para procesamiento
        String lekaJobId = lekaServerService.submitDocumentProcessing(
            project.getLekaServerConfig(),
            savedDocument.getId(),
            filePath
        );

        savedDocument.setLekaJobId(lekaJobId);
        return documentRepository.save(savedDocument);
    }

    public List<DocumentChunk> getDocumentChunks(Long documentId) {
        return documentChunkRepository.findByDocumentIdOrderByChunkIndex(documentId);
    }

    public List<DocumentChunk> searchChunks(Long projectId, String query, SearchType searchType) {
        if (searchType == SearchType.VECTOR_SEMANTIC || searchType == SearchType.HYBRID) {
            return performVectorSearch(projectId, query);
        } else {
            return performTextualSearch(projectId, query);
        }
    }

    private List<DocumentChunk> performVectorSearch(Long projectId, String query) {
        // Generar embedding de la query
        String queryEmbedding = embeddingService.generateEmbedding(query);

        // Buscar chunks similares usando pgvector
        return embeddingRepository.findSimilarChunks(projectId, queryEmbedding, 10);
    }
}
```

### LekaServerService

```java
@Service
public class LekaServerService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LekaServerJobRepository jobRepository;

    public String submitDocumentProcessing(String lekaConfig, Long documentId, String filePath) {
        LekaServerConfig config = parseLekaConfig(lekaConfig);

        // Crear job en leka-server
        LekaJobRequest request = new LekaJobRequest();
        request.setDocumentId(documentId);
        request.setFilePath(filePath);
        request.setJobType("DOCUMENT_CHUNKING");
        request.setConfig(createChunkingConfig());

        ResponseEntity<LekaJobResponse> response = restTemplate.postForEntity(
            config.getEndpoint() + "/api/v1/jobs",
            request,
            LekaJobResponse.class
        );

        String lekaJobId = response.getBody().getJobId();

        // Registrar job localmente
        LekaServerJob job = new LekaServerJob();
        job.setDocumentId(documentId);
        job.setLekaJobId(lekaJobId);
        job.setJobType(LekaJobType.DOCUMENT_CHUNKING);
        job.setJobStatus(LekaJobStatus.PENDING);
        job.setJobConfig(createChunkingConfig());

        jobRepository.save(job);

        return lekaJobId;
    }

    public void processLekaJobResult(String lekaJobId, LekaJobResult result) {
        LekaServerJob job = jobRepository.findByLekaJobId(lekaJobId)
            .orElseThrow(() -> new JobNotFoundException("Job not found"));

        job.setJobStatus(LekaJobStatus.COMPLETED);
        job.setJobResult(result.toJson());
        job.setCompletedAt(LocalDateTime.now());

        jobRepository.save(job);

        // Procesar chunks retornados por leka-server
        processChunksFromLekaServer(job.getDocumentId(), result.getChunks());
    }

    private void processChunksFromLekaServer(Long documentId, List<LekaChunk> lekaChunks) {
        for (LekaChunk lekaChunk : lekaChunks) {
            DocumentChunk chunk = new DocumentChunk();
            chunk.setDocumentId(documentId);
            chunk.setChunkIndex(lekaChunk.getIndex());
            chunk.setChunkText(lekaChunk.getText());
            chunk.setChunkSize(lekaChunk.getText().length());
            chunk.setStartPosition(lekaChunk.getStartPosition());
            chunk.setEndPosition(lekaChunk.getEndPosition());
            chunk.setSemanticScore(lekaChunk.getSemanticScore());
            chunk.setKeywords(lekaChunk.getKeywords().toJson());
            chunk.setEntities(lekaChunk.getEntities().toJson());
            chunk.setSummary(lekaChunk.getSummary());
            chunk.setChunkType(lekaChunk.getChunkType());
            chunk.setConfidenceScore(lekaChunk.getConfidenceScore());
            chunk.setLekaMetadata(lekaChunk.getMetadata().toJson());

            documentChunkRepository.save(chunk);
        }
    }
}
```

## Métricas y Monitoreo

### Prometheus Metrics

```java
@Component
public class RAGMetrics {

    private final Counter documentsProcessedCounter;
    private final Counter chunksGeneratedCounter;
    private final Counter embeddingsGeneratedCounter;
    private final Counter searchesPerformedCounter;
    private final Timer documentProcessingTimer;
    private final Timer chunkingTimer;
    private final Timer embeddingGenerationTimer;
    private final Timer searchTimer;

    public RAGMetrics(MeterRegistry meterRegistry) {
        this.documentsProcessedCounter = Counter.builder("rag_documents_processed_total")
            .description("Total documents processed")
            .register(meterRegistry);

        this.chunksGeneratedCounter = Counter.builder("rag_chunks_generated_total")
            .description("Total chunks generated")
            .register(meterRegistry);

        this.embeddingsGeneratedCounter = Counter.builder("rag_embeddings_generated_total")
            .description("Total embeddings generated")
            .register(meterRegistry);

        this.searchesPerformedCounter = Counter.builder("rag_searches_performed_total")
            .description("Total searches performed")
            .register(meterRegistry);

        this.documentProcessingTimer = Timer.builder("rag_document_processing_duration_seconds")
            .description("Document processing duration")
            .register(meterRegistry);

        this.chunkingTimer = Timer.builder("rag_chunking_duration_seconds")
            .description("Chunking duration")
            .register(meterRegistry);

        this.embeddingGenerationTimer = Timer.builder("rag_embedding_generation_duration_seconds")
            .description("Embedding generation duration")
            .register(meterRegistry);

        this.searchTimer = Timer.builder("rag_search_duration_seconds")
            .description("Search duration")
            .register(meterRegistry);
    }

    public void incrementDocumentsProcessed() {
        documentsProcessedCounter.increment();
    }

    public void incrementChunksGenerated() {
        chunksGeneratedCounter.increment();
    }

    public void incrementEmbeddingsGenerated() {
        embeddingsGeneratedCounter.increment();
    }

    public void incrementSearchesPerformed() {
        searchesPerformedCounter.increment();
    }

    public Timer.Sample startDocumentProcessingTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startChunkingTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startEmbeddingGenerationTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startSearchTimer() {
        return Timer.start(meterRegistry);
    }
}
```

## Configuración de Aplicación

### application-rag.yml

```yaml
rag:
  projects:
    default-chunk-size: 1000
    default-chunk-overlap: 200
    semantic-chunking: true
    auto-embedding: true

  documents:
    supported-formats:
      ["pdf", "docx", "txt", "html", "markdown", "excel", "csv"]
    max-file-size: 100MB
    auto-processing: true
    quality-check: true

  chunking:
    strategy: semantic
    min-chunk-size: 100
    max-chunk-size: 1000
    overlap-percentage: 20
    semantic-analysis: true
    auto-classification: true

  embeddings:
    default-model: text-embedding-ada-002
    vector-dimension: 1536
    index-type: ivfflat
    distance-metric: cosine
    similarity-threshold: 0.7

  leka-server:
    endpoint: ${LEKA_SERVER_ENDPOINT:http://localhost:8002}
    api-key: ${LEKA_SERVER_API_KEY:}
    timeout: ${LEKA_SERVER_TIMEOUT:30000}
    retry-attempts: ${LEKA_SERVER_RETRY_ATTEMPTS:3}
    retry-delay: ${LEKA_SERVER_RETRY_DELAY:1000}

  search:
    default-limit: 10
    hybrid-search: true
    semantic-search: true
    filter-search: true
    result-ranking: semantic

  monitoring:
    real-time-metrics: true
    search-analytics: true
    document-insights: true
    performance-tracking: true

pgvector:
  enabled: true
  dimension: 1536
  index-type: ivfflat
  distance-metric: cosine
  similarity-threshold: 0.7

minio:
  endpoint: ${MINIO_ENDPOINT:localhost:9000}
  access-key: ${MINIO_ACCESS_KEY:minioadmin}
  secret-key: ${MINIO_SECRET_KEY:minioadmin}
  bucket-name: ${MINIO_BUCKET:codeflowx-rag}
  region: ${MINIO_REGION:us-east-1}
  secure: ${MINIO_SECURE:false}
```

## Características Innovadoras

### 1. **Integración con Leka-Server**

- **Procesamiento Externo**: Documentos se envían a leka-server para chunking inteligente
- **Metadata Enriquecida**: Leka-server retorna chunks con metadata semántica rica
- **Auto-clasificación**: Clasificación automática de chunks usando LLMs
- **Chunking Semántico**: División inteligente basada en significado, no solo longitud

### 2. **Sistema de Chunks Avanzado**

- **Chunking Semántico**: Los chunks se dividen por significado, no por caracteres
- **Overlap Inteligente**: Overlap basado en contexto semántico
- **Metadata Rica**: Keywords, entidades, resúmenes, tipos de chunk
- **Confianza**: Score de confianza en la clasificación automática

### 3. **Búsqueda Híbrida**

- **Vectorial Semántica**: Búsqueda por similitud de embeddings
- **Textual Keyword**: Búsqueda tradicional por palabras clave
- **Híbrida**: Combinación de ambos métodos para mejores resultados
- **Filtrado**: Filtros por tipo de chunk, metadata, etc.

### 4. **Integración con TMP**

- **Conversaciones Unificadas**: Usa el sistema de conversaciones del módulo TMP
- **Sin Duplicación**: No hay entidades de chat duplicadas
- **Búsqueda Contextual**: Las búsquedas se asocian a conversaciones específicas
- **Historial**: Seguimiento de búsquedas por conversación

### 5. **Auto-clasificación de Chunks**

- **Clasificación Inteligente**: LLMs clasifican chunks automáticamente
- **Tipos Semánticos**: INTRODUCTION, MAIN_CONTENT, CONCLUSION, etc.
- **Tags Personalizables**: Usuarios pueden definir tags específicos
- **Fácil Remoción**: Tags se pueden eliminar fácilmente

## Conclusión

El módulo RAG actualizado proporciona:

- **Funcionalidad RAG Pura**: Se enfoca exclusivamente en RAG y búsqueda vectorial
- **Integración Leka-Server**: Procesamiento externo de documentos con chunking inteligente
- **Sistema de Chunks Avanzado**: Chunking semántico con metadata rica
- **Búsqueda Híbrida**: Vectorial + textual para mejores resultados
- **Integración TMP**: Usa el sistema unificado de conversaciones
- **Auto-clasificación**: Clasificación automática de chunks usando LLMs

Este módulo elimina la duplicación de funcionalidad de chat y se enfoca en su responsabilidad principal: proporcionar capacidades avanzadas de RAG con integración inteligente con leka-server.
