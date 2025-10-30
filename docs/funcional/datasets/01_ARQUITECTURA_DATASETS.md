# 📦 ARQUITECTURA DE DATASETS - CODEFLOWX GOVERN

**Fecha:** Octubre 30, 2025  
**Versión:** 1.0  
**Propósito:** Definir la arquitectura completa de gestión de datasets: dominios, entrenamiento, evaluación, RAG y su integración con leka-server

---

## 🎯 VISIÓN GENERAL

Los datasets son el núcleo del conocimiento y entrenamiento de agentes, modelos y sistemas RAG. Esta arquitectura separa claramente:

- **Gestión de metadatos, gobierno y auditoría** (Java + PostgreSQL)
- **Procesamiento, embeddings y vectorización** (leka-server + Python)

### Documentos Relacionados

Este documento es la **arquitectura general de datasets** y su integración con leka-server. Para detalles de implementación específica:

- **Domain Ingestion (Implementado):** `docs/funcional/domain-ingestion/01_IMPLEMENTACION_DOMAIN_INGESTION.md`
  - Entidades JPA: Domain, DomainDataSource, IngestionJob, DomainSector
  - ViewModels: 8 ViewModels completamente implementados
  - Pantallas ZUL: 8 pantallas (Dashboard, Wizard, Gestión, Monitor, 4× Configure Sources)
  - Estado: ✅ Completado Fase 1 (metadatos y UI)
  - **Pendiente:** Integración con leka-server para procesamiento real

- **RAG Systems:** `docs/funcional/rag/09_INTEGRACION_LEKA_SERVER.md`
  - Arquitectura de sistemas RAG
  - Integración con Qdrant/Cohere/Pinecone

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

**2. Domain (Dominios de Negocio - Implementado)**
```sql
CREATE TABLE DIN_DOMAIN (
    IDXDOMAIN BIGSERIAL PRIMARY KEY,
    
    -- Identificación
    DINDOMAINNAME VARCHAR(200) NOT NULL,
    DINDOMAINDESCRIPTION VARCHAR(1000),
    DINDOMAINICON VARCHAR(50),  -- Emoji o icono
    DINDOMAINCOLOR VARCHAR(50),  -- Color para UI
    DININDUSTRY VARCHAR(200),  -- Industria
    DINBUSINESSAREA VARCHAR(200),  -- Área de negocio
    
    -- Estado y Progreso
    DINSTATUS VARCHAR(50),  -- active, inactive, error, draft
    DINPROGRESS INTEGER,  -- % de progreso 0-100
    DINTOTALDOCUMENTS INTEGER,  -- Total documentos
    DINTOTALWEBPAGES INTEGER,  -- Total páginas web
    DINTOTALAPIS INTEGER,  -- Total APIs
    DINVERSION VARCHAR(50),
    
    -- Compliance y Regulación
    DINREGULATORYFRAMEWORK VARCHAR(500),  -- GDPR, HIPAA, SOC2, etc.
    DINDATARETENTIONYEARS INTEGER,
    DINPRIVACYLEVEL VARCHAR(50),  -- public, internal, confidential, restricted
    DINCOMPLIANCESTATUS VARCHAR(50),  -- compliant, non_compliant, under_review
    DINQUALITYSCORE INTEGER,  -- 0-100
    
    -- Configuración RAG
    DINRAGENABLED BOOLEAN DEFAULT false,
    DINRAGDOCUMENTCOUNT INTEGER,
    DINRAGLASTINDEXED TIMESTAMP,
    DINRAGSEARCHQUERIES INTEGER,
    
    -- Configuración Training
    DINTRAININGENABLED BOOLEAN DEFAULT false,
    DINTRAININGCONTENTCOUNT INTEGER,
    DINTRAININGLASTGENERATED TIMESTAMP,
    DINTRAININGRELEVANCESCORE INTEGER,
    
    -- Auto Ingestion
    DINAUTOINGESTION BOOLEAN DEFAULT false,
    DINQUALITYTHRESHOLD INTEGER,  -- Umbral calidad 0-100
    DINRETENTIONDAYS INTEGER,
    DINCOMPLIANCECHECKS BOOLEAN DEFAULT true,
    
    -- Auditoría
    DINCREATEDAT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    DINUPDATEDAT TIMESTAMP,
    DINLASTUPDATE TIMESTAMP,
    DINCREATEDBY VARCHAR(200),
    DINUPDATEDBY VARCHAR(200)
);
```

**3. DomainDataSource (Fuentes de Datos - Implementado)**
```sql
CREATE TABLE DIN_DATA_SOURCE (
    IDXDATASOURCE BIGSERIAL PRIMARY KEY,
    IDXDOMAIN BIGINT NOT NULL REFERENCES DIN_DOMAIN(IDXDOMAIN),
    
    -- Identificación
    DINDSNAME VARCHAR(300) NOT NULL,
    DINDSDESCRIPTION VARCHAR(1000),
    DINDSTYPE VARCHAR(50) NOT NULL,  -- database, api, file, web
    DINDSSTATUS VARCHAR(50),  -- active, inactive, error
    
    -- Database específico
    DINDSCONNECTIONSTRING VARCHAR(500),
    DINDSDATABASETYPE VARCHAR(50),  -- postgresql, mysql, mongodb, oracle, sqlserver
    DINDSTABLES TEXT,  -- JSON array de tablas
    
    -- API específico
    DINDSENDPOINT VARCHAR(500),
    DINDSAUTHTYPE VARCHAR(50),  -- bearer, api_key, oauth2
    DINDSAPIKEY VARCHAR(500),
    DINDSTOKEN VARCHAR(1000),
    DINDSPARAMETERS TEXT,  -- JSON de parámetros
    
    -- Web Scraping específico
    DINDSURL VARCHAR(500),
    DINDSSELECTORS TEXT,  -- JSON de selectores CSS/XPath
    DINDSMAXPAGES INTEGER,
    DINDSDELAY INTEGER,  -- Delay en ms entre requests
    
    -- File/Document específico
    DINDSFILEPATH VARCHAR(500),
    DINDSFILETYPE VARCHAR(50),
    DINDSSUPPORTEDFORMATS TEXT,  -- JSON array de formatos
    
    -- Opciones de Procesamiento
    DINDSEXTRACTTABLES BOOLEAN DEFAULT false,
    DINDSEXTRACTNUMBERS BOOLEAN DEFAULT false,
    DINDSEXTRACTENTITIES BOOLEAN DEFAULT false,
    DINDSQUALITYTHRESHOLD INTEGER,
    DINDSENABLED BOOLEAN DEFAULT true,
    
    -- Metadata y Sincronización
    DINDSLASTSYNC TIMESTAMP,
    DINDSRECORDCOUNT INTEGER,
    DINDSCONFIGURATION TEXT,  -- JSON configuración adicional
    DINDSMETADATA TEXT,  -- JSON metadata
    
    -- Auditoría
    DINDSCREATEDAT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    DINDSUPDATEDAT TIMESTAMP
);
```

**4. IngestionJob (Jobs de Ingesta - Implementado)**
```sql
CREATE TABLE DIN_INGESTION_JOB (
    IDXINGESTIONJOB BIGSERIAL PRIMARY KEY,
    IDXDOMAIN BIGINT NOT NULL REFERENCES DIN_DOMAIN(IDXDOMAIN),
    IDXDOMAINSECTOR BIGINT REFERENCES DIN_SECTOR(IDXDOMAINSECTOR),
    
    -- Identificación
    DINJOBNAME VARCHAR(300) NOT NULL,
    DINJOBTYPE VARCHAR(50) NOT NULL,  -- document_upload, web_scraping, api, database
    DINSTATUS VARCHAR(50) NOT NULL,  -- pending, running, completed, failed, paused, queued
    
    -- Progreso
    DINPROGRESS INTEGER DEFAULT 0,  -- % 0-100
    DINTOTALRECORDS INTEGER,
    DINPROCESSEDRECORDS INTEGER DEFAULT 0,
    DINFAILEDRECORDS INTEGER DEFAULT 0,
    
    -- Tiempos
    DINSTARTEDAT TIMESTAMP,
    DINCOMPLETEDAT TIMESTAMP,
    DINESTIMATEDCOMPLETION TIMESTAMP,
    
    -- Métricas
    DINPROCESSINGTIME INTEGER,  -- Segundos
    DINSUCCESSRATE DOUBLE PRECISION,
    DINQUALITYSCORE INTEGER,
    
    -- Web Scraping específico
    DINSCRAPEDPAGES INTEGER,
    DINTOTALPAGES INTEGER,
    
    -- Configuración y errores
    DINCONFIGURATION TEXT,  -- JSON
    DINMETADATA TEXT,  -- JSON
    DINERRORMESSAGE VARCHAR(2000),
    DINDATASETPATH VARCHAR(500),  -- Path al dataset procesado
    
    -- Auditoría
    DINCREATEDAT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    DINUPDATEDAT TIMESTAMP
);
```

**5. DatasetConsumption (Consumo y Costes)**
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

**6. DatasetAuditLog (Auditoría de Datasets)**
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

---

## 📚 REFERENCIAS A DOCUMENTACIÓN EXISTENTE

- **Implementación Domain Ingestion:** `docs/funcional/domain-ingestion/01_IMPLEMENTACION_DOMAIN_INGESTION.md`
  - Entidades implementadas: Domain, DomainDataSource, IngestionJob, DomainSector
  - ViewModels: DomainManagementViewModel, DomainIngestionWizardViewModel, Configure*, JobMonitorViewModel
  - Pantallas ZUL ya creadas (8 pantallas)
  - Scripts SQL: domain_ingestion.sql

- **Integración RAG:** `docs/funcional/rag/09_INTEGRACION_LEKA_SERVER.md`
  - Arquitectura RAG Systems
  - Endpoints leka-server para RAG
  - ViewModels de RAG

---

## 📁 ESTRUCTURA DE DATOS EN JAVA (Metadatos)

### Domain campos clave ya implementados

Campos de configuración RAG en tabla `DIN_DOMAIN`:
- `dinragenabled`: BOOLEAN - Habilita indexación RAG
- `dinragdocumentcount`: INTEGER - Documentos indexados
- `dinraglastindexed`: TIMESTAMP - Última indexación
- `dinragsearchqueries`: INTEGER - Total queries RAG

Campos de configuración Training:
- `dintrainingenabled`: BOOLEAN - Habilita entrenamiento
- `dintrainingcontentcount`: INTEGER - Contenido de training
- `dintraininglastgenerated`: TIMESTAMP - Último training
- `dintrainingrelevancescore`: INTEGER - Score de relevancia

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

> **Estado actual:** ✅ Implementado Fase 1 (metadatos y UI). Pendiente: integración con leka-server.

**DomainManagementViewModel** (✅ Implementado)
- `loadDomains()` → **DB** (BusinessService.findAllEntity con Criterias)
- `searchDomains()` → **DB** (filtros por nombre, industria, status)
- `createDomain()`, `editDomain()`, `deleteDomain()` → **DB** con confirmación
- **Pendiente:** `loadDomainMetrics()` → **API** (GET /domains/{id}/coverage para métricas de conocimiento)

**DomainIngestionWizardViewModel** (✅ Implementado)
- Wizard 6 pasos con validación por paso
- `selectTemplate()` → Auto-configura industria, privacy, regulatory según template (Fintech, Healthcare, E-commerce)
- `toggleDataSource()` → Selección múltiple (database, api, file, web)
- `finishDomain()` → **DB** (crea Domain en estado "draft")
- **Pendiente:** Si `dinragenabled=true` → lanzar **API**: `POST /domains/{domainId}/ingest-all`

**ConfigureDocumentsViewModel** (✅ Implementado)
- `loadDataSources()` → **DB** (lista DomainDataSource con type='file')
- `saveDataSource()` → **DB** (guarda config: filePath, fileType, supportedFormats, extractTables/Numbers/Entities)
- `deleteDataSource()` → **DB** con confirmación
- **Pendiente:** `uploadDocument()` → Sube a S3/FS + **API**: `POST /domains/{domainId}/datasets/{datasetId}/ingest`
- **Pendiente:** `validateDocument()` → **API**: `POST /datasets/validate`

**ConfigureApiViewModel** (✅ Implementado)
- `loadApiSources()` → **DB** (lista DomainDataSource con type='api')
- `saveApiSource()` → **DB** (endpoint, authType, apiKey, token, parameters JSON)
- **Pendiente:** `testConnection()` → **API**: `GET /datasources/test-connection`
- **Pendiente:** `scheduleIngestion()` → **API**: `POST /jobs/schedule` (cron expression)

**ConfigureDatabaseViewModel** (✅ Implementado)
- `loadDatabaseSources()` → **DB** (lista DomainDataSource con type='database')
- `saveDatabaseSource()` → **DB** (connectionString, databaseType, tables JSON)
- **Pendiente:** `testConnection()` → **API**: `POST /datasources/test-connection`
- **Pendiente:** `extractData()` → **API**: `POST /domains/{domainId}/datasources/{datasourceId}/extract-and-ingest`

**ConfigureWebScrapingViewModel** (✅ Implementado)
- `loadWebSources()` → **DB** (lista DomainDataSource con type='web')
- `saveWebSource()` → **DB** (url, selectors JSON, maxPages, delay)
- **Pendiente:** `testScraping()` → **API**: `POST /datasources/test-scraping` (preview de 1 página)
- **Pendiente:** `startScraping()` → **API**: `POST /domains/{domainId}/datasources/{datasourceId}/scrape-and-ingest`

**JobMonitorViewModel** (✅ Implementado)
- `loadJobs()` → **DB** (BusinessService.findAllEntity con filtros por dominio/tipo/estado)
- `pauseJob()`, `resumeJob()`, `cancelJob()` → **DB** (cambio de estado en IngestionJob)
- `refreshJobs()` → **DB** (reload cada N segundos)
- **Pendiente:** Integrar con **API** para jobs reales de leka-server:
  - `loadJobs()` → **API**: `GET /jobs?domainId=&type=&status=`
  - `viewJobLogs()` → **API**: `GET /jobs/{taskId}/logs`
  - `pauseJob()`, `cancelJob()` → **API**: `PUT /jobs/{taskId}/pause`, `DELETE /jobs/{taskId}`

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

### ✅ Fase 1: Gestión de Metadatos (COMPLETADO)

- [x] Entidades JPA: Domain, DomainDataSource, IngestionJob, DomainSector
- [x] Scripts SQL: domain_ingestion.sql con 4 tablas
- [x] ViewModels: 8 ViewModels con patrón BaseFront
- [x] Pantallas ZUL: Dashboard, Wizard, Gestión, Monitor, 4× Configure Sources
- [x] Validaciones y auditoría (logActivity)
- [x] CRUD completo de dominios y fuentes de datos

**Resultado:** Sistema funcional para gestionar metadatos de dominios y configurar fuentes. Datos se guardan en PostgreSQL. UI operativa.

---

### 🔄 Fase 2: Integración con leka-server (PENDIENTE)

#### 2.1 Infraestructura
- [ ] Crear LekaServerClient (Feign/WebClient con timeouts, retries, circuit breaker)
- [ ] Definir DTOs (IngestRequestDto, ProgressDto, CoverageDto, JobDto, etc.)
- [ ] Configurar JWT propagation y X-Request-Id generation
- [ ] Health check endpoint (GET /health) y validación de disponibilidad
- [ ] Configuración en application.yml (url, timeouts, retries)

#### 2.2 Nuevas Entidades y Extensiones DB
- [ ] Extender Domain con campos: dinragindexstatus, dinraglastindexedat, dinragtaskid
- [ ] Crear tabla DatasetConsumption (consumo por dataset/periodo)
- [ ] Crear tabla DatasetAuditLog (auditoría específica de datasets)
- [ ] Opcional: tabla DatasetJob (si jobs se gestionan también en Java, no solo en leka)

#### 2.3 ViewModels - Domain Ingestion (Integración API)
- [ ] **DomainManagementViewModel:**
  - Añadir `loadDomainCoverage()` → GET /domains/{id}/coverage
  - Añadir comando `indexDomain()` → POST /domains/{id}/ingest-all
  - Mostrar métricas de cobertura en UI (topics, keywords, coverage%)

- [ ] **DomainIngestionWizardViewModel:**
  - En `finishDomain()`, si `dinragenabled=true`:
    - POST /domains/{id}/ingest-all → obtener taskId
    - Guardar taskId en Domain.dinragtaskid
    - Redirigir a JobMonitor con taskId destacado

- [ ] **ConfigureDocumentsViewModel:**
  - Añadir `uploadDocument()` → Upload a S3/FS + POST /domains/{domainId}/datasources/{datasourceId}/ingest
  - Añadir `validateDocument()` → POST /datasets/validate (formato, schema)
  - Mostrar preview de documento antes de ingestar

- [ ] **ConfigureApiViewModel:**
  - Añadir `testConnection()` → GET /datasources/test-connection
  - Añadir `scheduleIngestion()` → POST /jobs/schedule (cron)
  - Mostrar resultado de test en modal

- [ ] **ConfigureDatabaseViewModel:**
  - Añadir `testConnection()` → POST /datasources/test-connection
  - Añadir `extractData()` → POST /domains/{domainId}/datasources/{id}/extract-and-ingest
  - Mostrar preview de primeras filas

- [ ] **ConfigureWebScrapingViewModel:**
  - Añadir `testScraping()` → POST /datasources/test-scraping (preview 1 página)
  - Añadir `startScraping()` → POST /domains/{domainId}/datasources/{id}/scrape-and-ingest
  - Mostrar HTML preview

- [ ] **JobMonitorViewModel:**
  - Mixto: cargar jobs de DB + jobs de leka-server (API)
  - GET /jobs?domainId=&type=&status= para jobs de leka
  - GET /jobs/{taskId}/logs para ver logs en tiempo real
  - PUT /jobs/{taskId}/pause, DELETE /jobs/{taskId} para control desde leka

#### 2.4 ViewModels - RAG (Integración API)
- [ ] RagDataSourceViewModel: POST /rag/datasources/{id}/ingest
- [ ] DocumentCoverageAnalysisOverviewViewModel: GET /rag/datasources/coverage-analysis
- [ ] EmbeddingGenerationProgressOverviewViewModel: GET /rag/datasources/embedding-progress
- [ ] DatasourceStatisticsOverviewViewModel: GET /rag/datasources/statistics
- [ ] ChunkDistributionStatsOverviewViewModel: GET /rag/quality/chunk-distribution

#### 2.5 Gobierno y Compliance
- [ ] RagBiasDetectionViewModel: POST /datasets/{id}/bias-detection
- [ ] RagComplianceViewModel: GET /datasets/{id}/compliance-check
- [ ] Workflows BPMN: validación de bias/compliance antes de aprobar

#### 2.6 Auditoría y Consumo
- [ ] Persistir en DatasetAuditLog tras cada comando (con taskId si aplica)
- [ ] Polling/webhook para actualizar ingestionStatus (PENDING → IN_PROGRESS → COMPLETED/FAILED)
- [ ] Cron job para consolidar consumo: GET /consumption?startDate=&endDate= → guardar en DatasetConsumption
- [ ] Dashboard de costes por dataset/dominio/proyecto/tenant

#### 2.7 Testing
- [ ] Unit tests de LekaServerClient (mocks, WireMock)
- [ ] Integration tests con leka-server en staging
- [ ] End-to-end: crear dominio → configurar fuente → ingestar → verificar índice en Qdrant
- [ ] Performance tests (ingesta de 10k+ documentos, medir throughput)
- [ ] Resilience tests (timeout, circuit breaker, retry)

---

## 🚀 DECISIONES TÉCNICAS PENDIENTES

### Procesamiento
1. **Estrategia de chunking por defecto:** ¿Semantic (spacy/langchain) o Fixed-size (512 tokens)?
2. **Embedding model por defecto:** ¿OpenAI text-embedding-3-small, Azure, o Cohere embed-multilingual-v3.0?
3. **Vector backend por defecto:** ¿Qdrant self-hosted, Cohere (managed) o Pinecone cloud?

### Operaciones
4. **Política de retención de vectores:** ¿Eliminar automáticamente tras X meses o manual?
5. **Sincronización de estado:** ¿Webhook de leka → Java (push) o polling cada N segundos (pull)?
6. **Jobs de ingesta:** ¿Gestionar en leka-server (Celery) o en Java (BPMN/Quartz)?

### Seguridad y Costes
7. **Rate limiting:** ¿Por tenant, por proyecto, por usuario o combinado?
8. **Costes:** ¿Mostrar en tiempo real (dashboard) o consolidado diario/mensual (reporting)?
9. **Encriptación:** ¿Vectores encriptados at-rest en Qdrant o solo en tránsito?

### Escalabilidad
10. **Colecciones Qdrant:** ¿Una colección por tenant o una global con payload filters?
11. **Sharding:** ¿Particionar índices grandes por fecha/categoría o monolítico?
12. **Caché de búsquedas:** ¿Redis para queries frecuentes o siempre fresh from Qdrant?

---

## 📚 REFERENCIAS

- **Implementación Domain Ingestion:** `docs/funcional/domain-ingestion/01_IMPLEMENTACION_DOMAIN_INGESTION.md` ✅ Completado
- **Integración RAG:** `docs/funcional/rag/09_INTEGRACION_LEKA_SERVER.md`
- **Entidades JPA:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/domainingestion/`
- **ViewModels:** `src/main/java/com/codeflowx/platform/viewmodel/domainingestion/`
- **Pantallas ZUL:** `src/main/webapp/console/platform/domain-ingestion/`
- **Scripts SQL:** `nocode.service.entitys/src/main/resources/sql-scripts/domain_ingestion.sql`
- **Leka-server:** `leka-server-documents/`, `leka-server-serving-evaluation/`

---

## 📊 RESUMEN EJECUTIVO

### Estado Actual (Octubre 2025)

**✅ Completado (Fase 1):**
- Gestión de metadatos de dominios (Domain, DomainDataSource, IngestionJob)
- UI completa: 8 pantallas ZUL operativas
- ViewModels con patrón BaseFront: 8 ViewModels
- CRUD, validaciones, auditoría básica (Ssoractividad)
- Wizard de creación de dominios con templates
- Configuración de fuentes de datos (API, Database, Web, Documents)
- Monitor de jobs (estados en DB)

**🔄 Pendiente (Fase 2):**
- Integración con leka-server para procesamiento real
- Chunking, embeddings, indexación vectorial
- Métricas técnicas de coverage, quality, bias
- Jobs asíncronos con progreso en tiempo real
- Consolidación de consumo/costes
- Testing end-to-end con Qdrant/Cohere/Pinecone

### Arquitectura Final (Objetivo)

```
┌─────────────────────────────────────────────────────────┐
│                  USUARIOS (Portal ZK)                    │
└──────────────────┬──────────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────────┐
│         GESTIÓN (Java + PostgreSQL)                      │
│  - Metadatos de dominios, datasets, fuentes             │
│  - Compliance, permisos, auditoría                       │
│  - Configuración de procesamiento                        │
│  - ViewModels: domainingestion/*, agents/*, rag/*        │
│  - Entidades: Domain, DomainDataSource, IngestionJob     │
└──────────────────┬──────────────────────────────────────┘
                   │ HTTP/gRPC (LekaServerClient)
                   │ JWT + X-Request-Id
┌──────────────────▼──────────────────────────────────────┐
│         EJECUCIÓN (leka-server + Python)                 │
│  - Chunking (estrategias configurables)                  │
│  - Embeddings (OpenAI/Azure/Cohere/Local)                │
│  - Indexación (Qdrant/Cohere/Pinecone)                   │
│  - Búsqueda semántica (retrieval + reranking)            │
│  - Métricas técnicas (coverage, quality, bias)           │
│  - Jobs asíncronos (Celery + Redis)                      │
└──────────────────┬──────────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────────┐
│    PERSISTENCIA VECTORIAL (Multi-backend)                │
│  - Qdrant (colecciones + payload filters)                │
│  - Cohere (índices + rerank)                             │
│  - Pinecone (namespaces + metadata filters)              │
└──────────────────────────────────────────────────────────┘
```

### Próximos Pasos Inmediatos

1. **Definir OpenAPI de leka-server** (endpoints de datasets, jobs, búsqueda)
2. **Generar LekaServerClient** (OpenAPI Generator + Maven)
3. **Implementar Fase 2** (otro chat AI con este documento como guía)
4. **Testing en staging** (Qdrant + datasets de prueba)
5. **Despliegue progresivo** (dominios piloto → producción)

---

**Documento creado:** 2025-10-30  
**Versión:** 1.0  
**Estado:** Arquitectura Definida + Fase 1 Implementada ✅  
**Próximo:** Fase 2 - Integración leka-server 🔄

