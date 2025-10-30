# 📦 ARQUITECTURA DE DATASETS - CODEFLOWX GOVERN

**Fecha:** Octubre 30, 2025  
**Versión:** 1.0  
**Propósito:** Definir la arquitectura completa de gestión de datasets: dominios, entrenamiento, evaluación, RAG y su integración con leka-server

---

## 🎯 VISIÓN GENERAL

Los datasets son el núcleo del conocimiento y entrenamiento de agentes, modelos y sistemas RAG. Esta arquitectura separa claramente:

- **Gestión de metadatos, gobierno y auditoría** (Java + PostgreSQL)
- **Procesamiento, embeddings y vectorización** (leka-server + Python)

---

## 📊 TIPOS DE DATASETS

### 1. Domain Datasets (Dominios de Negocio)
Datasets que definen el conocimiento de un dominio específico (finanzas, salud, legal, etc.)

**Propósito:**
- Entrenar agentes en dominios específicos
- Alimentar sistemas RAG con conocimiento especializado
- Benchmark de competencia en el dominio

**Entidades:**
- `AgentDomain` (tabla principal del dominio)
- `DomainDataset` (datasets asociados al dominio)

### 2. Training Datasets (Entrenamiento)
Datasets para fine-tuning de modelos y entrenamiento de agentes

**Propósito:**
- Fine-tuning de LLMs
- Entrenamiento supervisado de agentes
- Transfer learning

**Entidades:**
- `TrainingDataset`
- `TrainingDataSource`

### 3. Evaluation Datasets (Evaluación)
Datasets para evaluar performance, sesgos y compliance

**Propósito:**
- Evaluación de modelos (accuracy, F1, BLEU, etc.)
- Detección de sesgos
- Validación de compliance

**Entidades:**
- `EvaluationDataset`
- `EvaluationMetrics`

### 4. RAG Datasets (Knowledge Base)
Datasets para sistemas RAG de búsqueda y recuperación

**Propósito:**
- Búsqueda semántica
- Question Answering
- Document retrieval

**Entidades:**
- `RagDataSource`
- `RagDocument`

---

## 🏗️ ARQUITECTURA DE SEPARACIÓN

### Capa de Gestión, Gobierno y Compliance (Java + PostgreSQL)

**Responsabilidad:** Metadatos, catálogo, permisos, auditoría, compliance, costes

#### Entidades JPA (PostgreSQL)

**1. AgentDomain (Dominios de Agentes)**
```sql
CREATE TABLE AGTAGENTDOMAINS (
    IDXAGENTDOMAIN BIGSERIAL PRIMARY KEY,
    AGTDOMAINNAME VARCHAR(255) NOT NULL,
    AGTDOMAINCODE VARCHAR(100) NOT NULL UNIQUE,
    AGTDESCRIPTION TEXT,
    AGTCATEGORY VARCHAR(100) NOT NULL,
    AGTSUBCATEGORY VARCHAR(100),
    AGTSTATUS TEXT[] NOT NULL,
    AGTPRIORITY TEXT[],
    
    -- Conocimiento del dominio (referencias, NO embeddings)
    AGTREQUIREMENTS JSONB,
    AGTSKILLS JSONB,
    AGTKNOWLEDGEAREAS JSONB,
    AGTTOOLS JSONB,
    AGTDATASETS JSONB,  -- Referencias a datasets, paths, IDs
    
    -- Métricas y benchmarks (metadata)
    AGTMETRICS JSONB,
    AGTBENCHMARKS JSONB,
    AGTSTANDARDS JSONB,
    
    -- Compliance y regulación
    AGTREGULATIONS JSONB,
    AGTETHICALGUIDELINES JSONB,
    AGTQUALITYSTANDARDS JSONB,
    
    -- Evaluación y certificación
    AGTPERFORMANCECRITERIA JSONB,
    AGTCERTIFICATIONSTANDARDS JSONB,
    AGTVALIDATIONMETHODS JSONB,
    AGTTESTINGFRAMEWORKS JSONB,
    
    -- Aprendizaje
    AGTCOMPETENCYLEVELS JSONB,
    AGTLEARNINGPATHS JSONB,
    AGTRESOURCES JSONB,
    
    -- Auditoría
    AGTVERSION VARCHAR(50),
    AGTMATURITYLEVEL TEXT[],
    AGTADOPTIONRATE DECIMAL,
    AGTSUCCESSRATE DECIMAL,
    AGTCREATEDBY VARCHAR(255) NOT NULL,
    AGTUPDATEDBY VARCHAR(255),
    AGTCREATEDAT TIMESTAMP NOT NULL,
    AGTUPDATEDAT TIMESTAMP
);
```

**2. DomainDataset (Datasets de Dominio)**
```sql
CREATE TABLE DOMDOMAINDATASETS (
    IDXDOMAINDATASET BIGSERIAL PRIMARY KEY,
    IDDOMAINS0 BIGINT REFERENCES AGTAGENTDOMAINS(IDXAGENTDOMAIN),  -- FK al dominio
    
    -- Identificación
    DOMNAME VARCHAR(255) NOT NULL,
    DOMDESCRIPTION TEXT,
    DOMDATASETTYPE VARCHAR(50) NOT NULL,  -- TRAINING, EVALUATION, RAG, BENCHMARK
    
    -- Almacenamiento (referencias, NO datos reales)
    DOMFILEPATH VARCHAR(500),  -- Path en S3/FS
    DOMFILEFORMAT VARCHAR(50),  -- CSV, JSON, Parquet, PDF, etc.
    DOMFILESIZEBYTES BIGINT,
    DOMRECORDCOUNT BIGINT,
    DOMCOLUMNCOUNT INTEGER,
    DOMSCHEMAVERSION VARCHAR(50),
    
    -- Calidad y compliance (metadata)
    DOMDATAQUALITYSCORE DECIMAL(5,2),
    DOMCOMPLIANCETAGS JSONB,
    DOMRETENTIONPOLICY JSONB,
    DOMDATALINEAGE JSONB,  -- De dónde viene, transformaciones
    DOMSOURCEMETADATA JSONB,
    
    -- Configuración RAG
    DOMISRAGREADY BOOLEAN DEFAULT false,  -- Listo para indexar en leka-server
    DOMRAGMETADATA JSONB,  -- Estrategia chunking, embedding model, etc.
    DOMTRAININGRELEVANCE INTEGER,  -- 1-10 relevancia para entrenamiento
    
    -- Sincronización
    DOMLASTINGESTED TIMESTAMP,
    DOMINGESTIONFREQUENCY VARCHAR(50),  -- DAILY, WEEKLY, MONTHLY, ON_DEMAND
    DOMINGESTIONSTATUS VARCHAR(50),  -- PENDING, IN_PROGRESS, COMPLETED, FAILED
    DOMLASTERROR TEXT,
    
    -- Auditoría
    DOMCREATEDBY VARCHAR(255) NOT NULL,
    DOMUPDATEDBY VARCHAR(255),
    DOMCREATEDAT TIMESTAMP NOT NULL,
    DOMUPDATEDAT TIMESTAMP
);
```

**3. DatasetConsumption (Consumo y Costes)**
```sql
CREATE TABLE DATASETCONSUMPTION (
    IDXDATASETCONSUMPTION BIGSERIAL PRIMARY KEY,
    IDDATASET0 BIGINT,  -- FK genérica (puede ser DomainDataset, TrainingDataset, etc.)
    DATASETTYPE VARCHAR(50),  -- DOMAIN, TRAINING, EVALUATION, RAG
    
    -- Periodo
    PERIOD_START TIMESTAMP NOT NULL,
    PERIOD_END TIMESTAMP NOT NULL,
    
    -- Consumo
    TOKENS_USED BIGINT,  -- Tokens de embeddings
    REQUESTS_COUNT BIGINT,  -- Requests de ingesta/búsqueda
    STORAGE_BYTES BIGINT,  -- Espacio usado (vectores + chunks)
    COST_USD DECIMAL(12,4),  -- Coste total
    PROVIDER VARCHAR(100),  -- openai, azure, cohere, local
    
    -- Metadata
    DETAILS JSONB,
    CREATEDAT TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**4. DatasetAuditLog (Auditoría de Datasets)**
```sql
CREATE TABLE DATASETAUDITLOG (
    IDXDATASETAUDITLOG BIGSERIAL PRIMARY KEY,
    
    -- Actor
    ACTOR_USERNAME VARCHAR(255) NOT NULL,
    ACTOR_ROLE VARCHAR(100),
    
    -- Acción
    ACTION VARCHAR(100) NOT NULL,  -- CREATE, UPDATE, DELETE, INGEST, SEARCH, EXPORT
    ENTITY_TYPE VARCHAR(100),  -- DOMAIN_DATASET, RAG_DATASET, TRAINING_DATASET
    ENTITY_ID BIGINT,
    ENTITY_NAME VARCHAR(500),
    
    -- Detalles
    DETAILS JSONB,  -- Cambios realizados, taskId de leka, etc.
    IP_ADDRESS VARCHAR(50),
    USER_AGENT TEXT,
    
    -- Timestamp
    CREATEDAT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Índices
    INDEX idx_actor (ACTOR_USERNAME),
    INDEX idx_action (ACTION),
    INDEX idx_entity (ENTITY_TYPE, ENTITY_ID),
    INDEX idx_timestamp (CREATEDAT)
);
```

#### Funciones de Gestión (Java)

**CRUD y Catálogo:**
- Alta/edición/baja de dominios (AgentDomain)
- Registro de datasets (DomainDataset, TrainingDataset, EvaluationDataset)
- Vinculación dataset ↔ dominio ↔ proyecto ↔ tenant
- Versionado de datasets (schemas, formatos)

**Compliance y Gobierno:**
- Etiquetado de compliance (GDPR, HIPAA, PCI-DSS, etc.)
- Políticas de retención y lineage
- Validación de calidad de datos (quality score)
- Aprobación de datasets (workflows BPMN)

**Configuración para Ejecución:**
- Flags: `isRagReady`, `trainingRelevance`, `ingestionFrequency`
- Metadata: `ragMetadata` (chunking strategy, embedding model), `sourceMetadata`
- Estados: `ingestionStatus` (PENDING | IN_PROGRESS | COMPLETED | FAILED)

**Auditoría y Consumo:**
- Registro de acciones (quién creó/modificó/ingestó/buscó en qué dataset)
- Consolidación de consumo (tokens, requests, storage, cost) por periodo
- Reporting de uso por proyecto/tenant/dominio

**ViewModels involucrados:**
- `platform/viewmodel/domainingestion/*` (DomainManagementViewModel, DomainIngestionWizardViewModel, Configure*, JobMonitorViewModel)
- `platform/viewmodel/agents/AgentDomainOverviewViewModel`, `AgentDomainDetailViewModel`
- `platform/viewmodel/training/DatasetSourceOverviewViewModel`, `DatasetSourceDetailViewModel`
- `platform/viewmodel/projects/ProjectDomainOverviewViewModel`, `ProjectDomainDetailViewModel`

---

### Capa de Ejecución (leka-server + Python)

**Responsabilidad:** Procesamiento real de datasets, chunking, embeddings, indexación vectorial

#### Funciones de Ejecución

**Ingesta y Procesamiento:**
- Parseo de archivos (CSV, JSON, Parquet, PDF, DOCX, TXT, XML, etc.)
- Chunking estratégico:
  - Fixed-size (tokens, caracteres)
  - Semantic (párrafos, secciones, temas)
  - Sliding-window (overlap configurable)
  - Document-structure-aware (headers, bullets, tablas)
- Generación de embeddings batch:
  - OpenAI (text-embedding-3-small/large)
  - Azure OpenAI
  - Cohere (embed-english-v3.0, embed-multilingual-v3.0)
  - Local (Sentence-Transformers, Ollama)
- Upsert a índices vectoriales (Qdrant/Cohere/Pinecone)

**Indexación Multi-Backend:**
- Qdrant: colecciones por `{tenantId}_{domainId}` o `{systemId}_{datasetId}`
- Cohere: índices con nombre único por tenant/dataset
- Pinecone: índices compartidos con namespaces por tenant

**Payload vectorial común:**
```json
{
  "tenantId": "tenant-123",
  "projectId": "proj-456",
  "domainId": "dom-789",
  "datasetId": "ds-101",
  "systemId": "rag-sys-202",  // Si es RAG
  "docId": "doc-12345",
  "chunkId": "chunk-001",
  "section": "Introduction",
  "metadata": {
    "title": "Financial Regulations 2024",
    "author": "SEC",
    "date": "2024-01-15",
    "tags": ["finance", "compliance"],
    "complianceLevel": "public"
  },
  "embeddingsVersion": "openai-3-small-2024",
  "createdAt": "2024-10-30T10:00:00Z"
}
```

**Evaluación de Calidad:**
- Data profiling (distribución de chunks, longitudes, densidad semántica)
- Bias detection (análisis estadístico de sesgo en embeddings)
- Coverage analysis (cobertura de temas/keywords del dominio)
- Consistency checks (duplicados, coherencia semántica)

**Métricas Técnicas:**
- Embedding generation progress (% completado, chunks/sec, ETA)
- Chunk distribution stats (histograma de tamaños, overlap)
- Document coverage (% documentos procesados vs totales)
- Index health (latencia, disponibilidad, tamaño en MB/GB)
- Search analytics (queries, hit ratio, top keywords)

---

## 🔄 FLUJOS DE TRABAJO

### Flujo 1: Creación de Domain Dataset

**Paso 1: Gestión (Java)**
1. Usuario crea/edita Domain (DomainManagementViewModel)
2. Configura fuentes de datos (ConfigureDocuments/Api/Database/WebScraping)
3. Sube/referencia archivos de dataset
4. Sistema crea `DomainDataset` en PostgreSQL:
   - Metadatos: name, description, datasetType, filePath, fileFormat
   - Compliance: complianceTags, retentionPolicy, dataLineage
   - Config: isRagReady=true, ragMetadata={chunkStrategy, embeddingModel}
   - Estado: ingestionStatus=PENDING
5. Guarda en DB y muestra confirmación

**Paso 2: Ingesta (Java → leka-server)**
1. Usuario hace click en "Ingestar Dataset"
2. Java valida: isRagReady=true, filePath existe, compliance OK
3. Java lanza: `POST /domains/{domainId}/datasets/{datasetId}/ingest`
   - Headers: JWT, X-Request-Id
   - Body: {filePath, chunkStrategy, embeddingModel, vectorBackend, payloadFilters}
4. Leka responde: `{taskId, status: "ACCEPTED", estimatedTime}`
5. Java actualiza: ingestionStatus=IN_PROGRESS, muestra toast con taskId

**Paso 3: Procesamiento (leka-server)**
1. Leka lee archivo desde S3/FS (usando filePath)
2. Parsea según fileFormat (CSV→pandas, PDF→pypdf, etc.)
3. Chunking según estrategia configurada
4. Genera embeddings batch (rate limit aware)
5. Upsert a Qdrant/Cohere/Pinecone con payload completo
6. Calcula métricas: docCount, chunkCount, avgChunkSize, coverage
7. Al terminar: callback/webhook a Java o espera polling

**Paso 4: Sincronización (leka-server → Java)**
1. Leka notifica completado (webhook o polling desde Java)
2. Java actualiza DomainDataset:
   - ingestionStatus=COMPLETED
   - lastIngested=now()
   - recordCount={chunks indexados}
   - dataQualityScore={score calculado por leka}
3. Java guarda en DatasetConsumption: tokens, requests, storage, cost
4. Java audita en DatasetAuditLog: acción=INGEST, entityId, taskId, duration

**Paso 5: Visualización (Java)**
1. Usuario ve en UI: estado COMPLETED, métricas actualizadas
2. Panels de monitoreo refrescan: embedding progress, coverage, health
3. Métricas técnicas consultadas desde leka: GET /domains/{id}/coverage, GET /domains/{id}/quality

---

### Flujo 2: Búsqueda en Dataset RAG

**Paso 1: Búsqueda (Java)**
1. Usuario escribe query en pantalla de agente/modelo
2. Java audita: acción=SEARCH, query, user

**Paso 2: Ejecución (leka-server)**
1. Java llama: `POST /rag/search`
   - Body: {query, systemId, domainId, topK, filters: {projectId, tenantId, complianceLevel}}
2. Leka ejecuta:
   - Embedding del query
   - Búsqueda vectorial con payload filters
   - Opcional: reranking (Cohere, cross-encoder)
3. Leka responde: `{results: [{id, text, score, metadata}], latency, cost}`

**Paso 3: Registro (Java)**
1. Java muestra resultados en UI
2. Java guarda en DatasetConsumption: tokens (query embedding), cost
3. Java audita en DatasetAuditLog: detalles de búsqueda, resultados count

---

### Flujo 3: Evaluación de Bias en Dataset

**Paso 1: Configuración (Java)**
1. Usuario configura umbrales de bias (RagBiasDetectionViewModel)
2. Guarda configuración en DB: thresholds, rules, complianceFramework

**Paso 2: Ejecución (leka-server)**
1. Java lanza: `POST /datasets/{datasetId}/bias-detection`
2. Leka ejecuta análisis:
   - Distribución de embeddings (PCA, t-SNE)
   - Detección de clusters semánticos sesgados
   - Análisis estadístico (disparate impact, fairness metrics)
3. Leka responde: `{biasScore, clusters, recommendations, taskId}`

**Paso 3: Decisión (Java)**
1. Java muestra resultados en UI
2. Si biasScore > threshold: alerta, workflow de revisión (BPMN)
3. Java guarda decisión de gobierno en DB
4. Java audita acción y guarda consumo

---

## 📁 ESTRUCTURA DE DATOS EN JAVA (Metadatos)

### AgentDomain.agtdatasets (JSONB)
```json
{
  "datasets": [
    {
      "datasetId": 123,
      "name": "Financial Regulations 2024",
      "type": "TRAINING",
      "filePath": "s3://datasets/finance/regulations-2024.csv",
      "recordCount": 15000,
      "isRagReady": true,
      "ingestionStatus": "COMPLETED",
      "lastIngested": "2024-10-29T10:00:00Z"
    },
    {
      "datasetId": 124,
      "name": "Banking Terminology",
      "type": "RAG",
      "filePath": "s3://datasets/finance/terminology.json",
      "recordCount": 8500,
      "isRagReady": true,
      "ingestionStatus": "IN_PROGRESS"
    }
  ],
  "totalDatasets": 2,
  "totalRecords": 23500,
  "lastSync": "2024-10-30T08:00:00Z"
}
```

### DomainDataset.ragMetadata (JSONB)
```json
{
  "vectorBackend": "qdrant",
  "collectionName": "tenant-123_domain-789_dataset-101",
  "embeddingModel": "text-embedding-3-small",
  "embeddingDimensions": 1536,
  "chunkStrategy": "semantic",
  "chunkSize": 512,
  "chunkOverlap": 128,
  "indexedAt": "2024-10-29T10:30:00Z",
  "vectorCount": 12500,
  "indexSizeMB": 245.7
}
```

### DomainDataset.complianceTags (JSONB)
```json
{
  "frameworks": ["GDPR", "SOC2"],
  "dataClassification": "internal",
  "piiDetected": false,
  "retentionYears": 7,
  "encryptionRequired": true,
  "accessRestrictions": ["finance_team", "compliance_officers"],
  "auditFrequency": "quarterly"
}
```

---

## 🔌 ENDPOINTS LEKA-SERVER (Datasets)

### Domain Datasets

**Ingesta:**
- `POST /domains/{domainId}/datasets/{datasetId}/ingest`
  - Request: `{filePath, fileFormat, chunkStrategy, embeddingModel, vectorBackend, payloadFilters}`
  - Response: `{taskId, status, estimatedTime, message}`

- `POST /domains/{domainId}/ingest-all`
  - Ingesta todos los datasets del dominio marcados como isRagReady=true
  - Response: `{taskIds[], totalDatasets, estimatedTime}`

**Progreso y Estado:**
- `GET /domains/{domainId}/datasets/{datasetId}/progress`
  - Response: `{status, progress%, chunksProcessed, chunksTotal, ETA, currentSpeed}`

- `GET /jobs/{taskId}`
  - Response: `{taskId, status, progress%, logs[], startedAt, completedAt, error}`

**Métricas:**
- `GET /domains/{domainId}/coverage`
  - Response: `{documentsTotal, documentsIndexed, chunksTotal, topics[], keywords[], coverage%}`

- `GET /domains/{domainId}/quality`
  - Response: `{dataQualityScore, biasScore, consistencyScore, recommendations[], issues[]}`

- `GET /datasets/{datasetId}/statistics`
  - Response: `{recordCount, chunkCount, avgChunkSize, chunkDistribution[], embeddingsVersion}`

**Bias y Compliance:**
- `POST /datasets/{datasetId}/bias-detection`
  - Request: `{thresholds, framework, analysisType}`
  - Response: `{taskId, biasScore, clusters[], disparateImpact, recommendations[]}`

- `GET /datasets/{datasetId}/compliance-check`
  - Response: `{compliant, violations[], risks[], remediation[]}`

**Búsqueda y Retrieval:**
- `POST /rag/search`
  - Request: `{query, systemId, domainId, datasetId, topK, filters: {tenantId, projectId, complianceLevel}}`
  - Response: `{results: [{id, text, score, metadata}], latency, tokensUsed, cost}`

**Administración:**
- `POST /datasets/{datasetId}/rebuild-index`
  - Reindex completo (por cambio de embedding model o estrategia)
  - Response: `{taskId, vectorsToDelete, vectorsToCreate}`

- `DELETE /datasets/{datasetId}/vectors`
  - Elimina vectores del dataset (soft delete con flag en payload)
  - Response: `{deletedCount, indexSizeReduced}`

---

## 🗺️ INTEGRACIÓN POR MÓDULO

### Módulo: Domain Ingestion (platform/viewmodel/domainingestion)

**DomainManagementViewModel:**
- `loadDomains()` → **DB** (BusinessService.findAllEntity)
- `loadDomainMetrics()` → **API** (GET /domains/{id}/coverage para cada dominio)
- `createDomain()`, `updateDomain()`, `deleteDomain()` → **DB**

**DomainIngestionWizardViewModel:**
- `finishDomain()` → **DB** (crea Domain + DomainDatasets)
- Si `ragEnabled=true` y datasets con `isRagReady=true`:
  - Lanza **API**: `POST /domains/{domainId}/ingest-all`
  - Guarda taskIds en tabla de Jobs o en memoria
  - Redirige a JobMonitorViewModel con taskIds

**ConfigureDocumentsViewModel:**
- `uploadDocument()` → Sube a S3/FS, guarda path en **DB** (DomainDataset.filePath)
- `validateDocument()` → **API**: `POST /datasets/validate` (formato, schema, calidad)
- `ingestDocument()` → **API**: `POST /domains/{domainId}/datasets/{datasetId}/ingest`

**ConfigureApiViewModel:**
- `testConnection()` → Guarda config en **DB** (connectionConfig JSONB)
- `scheduleIngestion()` → Guarda frecuencia en **DB** + crea job en BPMN o **API** (scheduling en leka)

**ConfigureDatabaseViewModel:**
- Similar a ConfigureApi: config en **DB**, extracción/ingesta en **API**

**ConfigureWebScrapingViewModel:**
- Config de scraping en **DB**, ejecución en **API** (POST /datasets/scrape + /ingest)

**JobMonitorViewModel:**
- `loadJobs()` → Mixto:
  - Jobs BPMN (workflows de aprobación) → **DB** (historicProcessInstance)
  - Jobs de ingesta/procesamiento → **API** (GET /jobs?type=ingestion&domainId=)
- `viewJobDetails()` → **API** (GET /jobs/{taskId} con logs y progreso)
- `cancelJob()` → **API** (DELETE /jobs/{taskId})

---

### Módulo: Agents - Agent Domains (platform/viewmodel/agents)

**AgentDomainOverviewViewModel:**
- `loadAgentDomains()` → **DB** (BusinessService.findAllEntity)
- `loadDomainCoverage()` → **API** (GET /domains/{id}/coverage para métricas de conocimiento)

**AgentDomainDetailViewModel:**
- `loadAgentDomain()`, `saveAgentDomain()` → **DB**
- `loadDomainDatasets()` → **DB** (lista de DomainDatasets del dominio)
- `ingestAllDatasets()` → **API** (POST /domains/{domainId}/ingest-all)
- `viewDatasetProgress()` → **API** (GET /domains/{domainId}/datasets/{datasetId}/progress)

---

### Módulo: Training (platform/viewmodel/training)

**DatasetSourceOverviewViewModel:**
- `loadDatasetSources()` → **DB** (metadatos de TrainingDatasets)
- `loadIngestionStats()` → **API** (GET /training/datasets/statistics)

**DatasetSourceDetailViewModel:**
- CRUD de TrainingDataset → **DB**
- `uploadTrainingData()` → S3/FS + **DB** (path)
- `ingestForTraining()` → **API** (POST /training/datasets/{id}/ingest)
- `validateDataset()` → **API** (POST /training/datasets/{id}/validate: format, schema, quality)

---

### Módulo: RAG (platform/viewmodel/rag)

**RagDataSourceViewModel:**
- CRUD de RagDataSource → **DB** (metadata: nombre, tipo, conexión)
- `syncDataSource()` → **API** (POST /rag/datasources/{id}/sync: pull de datos externos)
- `ingestToIndex()` → **API** (POST /rag/systems/{systemId}/datasources/{datasourceId}/ingest)

**DocumentCoverageAnalysisOverviewViewModel:**
- `loadCoverageAnalysis()` → **API** (GET /rag/datasources/coverage-analysis?systemId=&datasourceId=)

**EmbeddingGenerationProgressOverviewViewModel:**
- `loadProgress()` → **API** (GET /rag/datasources/embedding-progress?systemId=)

**DatasourceStatisticsOverviewViewModel:**
- `loadStatistics()` → **API** (GET /rag/datasources/statistics?systemId=)

**ChunkDistributionStatsOverviewViewModel:**
- `loadChunkDistribution()` → **API** (GET /rag/quality/chunk-distribution?systemId=&datasourceId=)

---

## 🔐 GOBIERNO Y SEGURIDAD

### Permisos y ACL

**Gestión (Java):**
- Tabla: `DatasetPolicy` (id, datasetId, subjectType, subjectId, actions[], scope)
- Configuración de quién puede: ver/editar/ingestar/buscar/exportar datasets
- Validación en ViewModels antes de llamar a leka-server

**Ejecución (leka-server):**
- JWT con scopes: `dataset:read`, `dataset:ingest`, `dataset:search`, `dataset:admin`
- Payload filters en Qdrant/Pinecone: filtrar por tenantId, projectId, complianceLevel
- Rate limiting por tenant/proyecto

### Compliance

**Gestión (Java):**
- Etiquetado de datasets (complianceTags: GDPR, HIPAA, PCI-DSS, SOC2)
- Políticas de retención (retentionPolicy: años, delete after, archive)
- Data lineage (origen, transformaciones, destiny)
- Validación pre-ingesta (compliance OK antes de enviar a leka)

**Ejecución (leka-server):**
- PII detection (antes de embeddings)
- Encryption at rest (vectores + payloads)
- Audit logs de queries (almacenados y enviados a Java para consolidación)

---

## 📊 MÉTRICAS Y CONSUMO

### DatasetConsumption (Tabla Java)

Consolidación de métricas de consumo por dataset y periodo:

```sql
-- Ejemplo de registro
INSERT INTO DATASETCONSUMPTION (
    IDDATASET0, DATASETTYPE, PERIOD_START, PERIOD_END,
    TOKENS_USED, REQUESTS_COUNT, STORAGE_BYTES, COST_USD, PROVIDER
) VALUES (
    123, 'DOMAIN', '2024-10-01', '2024-10-31',
    1500000, 450, 2147483648, 45.50, 'openai'
);
```

**Métricas reportadas por leka-server:**
- Tokens usados (embeddings generation + search queries)
- Requests count (ingest, search, rebuild)
- Storage bytes (tamaño de índice vectorial)
- Cost estimado (por provider: OpenAI, Azure, Cohere, Pinecone)

**Consolidación:**
- Java consulta: `GET /datasets/{id}/consumption?startDate=&endDate=`
- Java persiste en DatasetConsumption para reporting y billing
- Dashboards de costes por proyecto/tenant/dominio

---

## 🛠️ IMPLEMENTACIÓN TÉCNICA

### RagApiClient (Java - WebClient/Feign)

Interfaz sugerida:

```java
@FeignClient(name = "leka-server", url = "${leka.server.url}")
public interface LekaServerClient {
    
    // Datasets - Ingesta
    @PostMapping("/domains/{domainId}/datasets/{datasetId}/ingest")
    IngestResponseDto ingestDomainDataset(
        @PathVariable Long domainId,
        @PathVariable Long datasetId,
        @RequestBody IngestRequestDto request,
        @RequestHeader("Authorization") String jwt,
        @RequestHeader("X-Request-Id") String requestId
    );
    
    @PostMapping("/domains/{domainId}/ingest-all")
    IngestAllResponseDto ingestAllDomainDatasets(
        @PathVariable Long domainId,
        @RequestHeader("Authorization") String jwt,
        @RequestHeader("X-Request-Id") String requestId
    );
    
    // Progress y Jobs
    @GetMapping("/domains/{domainId}/datasets/{datasetId}/progress")
    ProgressDto getDatasetProgress(
        @PathVariable Long domainId,
        @PathVariable Long datasetId,
        @RequestHeader("Authorization") String jwt
    );
    
    @GetMapping("/jobs/{taskId}")
    JobDto getJobStatus(
        @PathVariable String taskId,
        @RequestHeader("Authorization") String jwt
    );
    
    // Métricas
    @GetMapping("/domains/{domainId}/coverage")
    CoverageDto getDomainCoverage(
        @PathVariable Long domainId,
        @RequestHeader("Authorization") String jwt
    );
    
    @GetMapping("/domains/{domainId}/quality")
    QualityDto getDomainQuality(
        @PathVariable Long domainId,
        @RequestHeader("Authorization") String jwt
    );
    
    @GetMapping("/datasets/{datasetId}/statistics")
    StatisticsDto getDatasetStatistics(
        @PathVariable Long datasetId,
        @RequestHeader("Authorization") String jwt
    );
    
    // Bias y Compliance
    @PostMapping("/datasets/{datasetId}/bias-detection")
    BiasDetectionDto detectBias(
        @PathVariable Long datasetId,
        @RequestBody BiasDetectionRequestDto request,
        @RequestHeader("Authorization") String jwt
    );
    
    @GetMapping("/datasets/{datasetId}/compliance-check")
    ComplianceDto checkCompliance(
        @PathVariable Long datasetId,
        @RequestHeader("Authorization") String jwt
    );
    
    // Búsqueda RAG
    @PostMapping("/rag/search")
    SearchResponseDto search(
        @RequestBody SearchRequestDto request,
        @RequestHeader("Authorization") String jwt
    );
    
    // Administración
    @PostMapping("/datasets/{datasetId}/rebuild-index")
    RebuildResponseDto rebuildIndex(
        @PathVariable Long datasetId,
        @RequestBody RebuildRequestDto request,
        @RequestHeader("Authorization") String jwt
    );
    
    @DeleteMapping("/datasets/{datasetId}/vectors")
    DeleteResponseDto deleteVectors(
        @PathVariable Long datasetId,
        @RequestHeader("Authorization") String jwt
    );
}
```

### Configuración (application.yml)

```yaml
leka:
  server:
    url: ${LEKA_SERVER_URL:http://localhost:8001}
    timeout:
      connect: 2000  # 2s
      read: 15000    # 15s
    retry:
      maxAttempts: 3
      backoff: 1000  # 1s exponential
    circuitBreaker:
      failureThreshold: 5
      timeout: 30000  # 30s
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### Fase 1: Infraestructura
- [ ] Crear LekaServerClient (Feign con timeouts, retries, circuit breaker)
- [ ] Definir DTOs (IngestRequestDto, ProgressDto, CoverageDto, etc.)
- [ ] Configurar JWT propagation y X-Request-Id
- [ ] Health check endpoint (GET /health) y validación pre-ingesta

### Fase 2: Entidades y DB
- [ ] Verificar/crear tabla DomainDataset con campos isRagReady, ragMetadata, ingestionStatus
- [ ] Crear tabla DatasetConsumption
- [ ] Crear tabla DatasetAuditLog
- [ ] Migración de datos existentes si aplica

### Fase 3: ViewModels - Domain Ingestion
- [ ] DomainManagementViewModel: integrar GET /domains/{id}/coverage
- [ ] DomainIngestionWizardViewModel: integrar POST /domains/{id}/ingest-all
- [ ] ConfigureDocumentsViewModel: integrar POST /datasets/{id}/ingest
- [ ] JobMonitorViewModel: integrar GET /jobs?type=ingestion

### Fase 4: ViewModels - RAG
- [ ] RagDataSourceViewModel: integrar POST /rag/datasources/{id}/ingest
- [ ] DocumentCoverageAnalysisOverviewViewModel: GET /rag/datasources/coverage-analysis
- [ ] EmbeddingGenerationProgressOverviewViewModel: GET /rag/datasources/embedding-progress
- [ ] DatasourceStatisticsOverviewViewModel: GET /rag/datasources/statistics
- [ ] ChunkDistributionStatsOverviewViewModel: GET /rag/quality/chunk-distribution

### Fase 5: Gobierno
- [ ] RagBiasDetectionViewModel: integrar POST /datasets/{id}/bias-detection
- [ ] RagComplianceViewModel: integrar GET /datasets/{id}/compliance-check
- [ ] Actualizar workflows BPMN para incluir validación de bias/compliance

### Fase 6: Auditoría y Consumo
- [ ] Persistir en DatasetAuditLog tras cada comando exitoso
- [ ] Polling/webhook para actualizar ingestionStatus
- [ ] Consolidar consumo en DatasetConsumption (cron job o event-driven)
- [ ] Dashboard de costes por dataset/dominio/proyecto

### Fase 7: Testing
- [ ] Unit tests de LekaServerClient (mocks)
- [ ] Integration tests con leka-server en staging
- [ ] End-to-end: crear dominio → subir dataset → ingestar → buscar → verificar resultados
- [ ] Performance tests (ingesta de 10k+ documentos)

---

## 🚀 DECISIONES PENDIENTES

1. **Estrategia de chunking por defecto:** ¿Semantic (spacy/langchain) o Fixed-size (512 tokens)?
2. **Embedding model por defecto:** ¿OpenAI text-embedding-3-small o Azure?
3. **Vector backend por defecto:** ¿Qdrant self-hosted o Pinecone cloud?
4. **Política de retención:** ¿Eliminar vectores automáticamente tras X meses o manual?
5. **Notificaciones:** ¿Webhook de leka → Java o polling cada N segundos?
6. **Rate limiting:** ¿Por tenant, por proyecto o por usuario?
7. **Costes:** ¿Mostrar en tiempo real o consolidado diario/mensual?

---

## 📚 REFERENCIAS

- Documento RAG: `docs/funcional/rag/09_INTEGRACION_LEKA_SERVER.md`
- Entidades JPA: `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/`
- ViewModels: `src/main/java/com/codeflowx/platform/viewmodel/domainingestion/`
- Leka-server docs: `leka-server-documents/`, `leka-server-serving-evaluation/`

---

**Próximos pasos:** Validar con el equipo y empezar implementación por fases.

