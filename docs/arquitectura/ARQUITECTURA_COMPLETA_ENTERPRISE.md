# 🏗️ ARQUITECTURA ENTERPRISE COMPLETA - CODEFLOWX
## Stack definitivo: Componentes especializados para cada función

**Fecha:** 5 Noviembre 2025  
**Visión:** RAG potente + Reranking + Multi-almacenamiento  
**Decisión:** Qdrant + MinIO + OpenSearch + PostgreSQL/TimescaleDB

---

## 🎯 RESUMEN EJECUTIVO

**Stack enterprise CodeflowX:**

```
┌─────────────────────────────────────────────────────────────┐
│                    BACKEND JAVA (Spring Boot)                │
│                    API Layer + Business Logic                │
└────────┬──────────┬──────────┬──────────┬──────────┬────────┘
         │          │          │          │          │
         │          │          │          │          │
    ┌────▼────┐ ┌──▼──────┐ ┌─▼─────┐ ┌─▼──────┐ ┌─▼──────┐
    │PostgreSQL│ │ Qdrant │ │ MinIO │ │OpenSearch│ │Python  │
    │TimescaleDB│ │(Vector)│ │(Files)│ │ (Logs) │ │Micros  │
    │ pgvector │ │        │ │       │ │        │ │        │
    └──────────┘ └────────┘ └───────┘ └────────┘ └────────┘
```

**Principio:** **Especialización > Todo-en-uno**

Cada componente hace **una cosa muy bien**:
- ✅ **PostgreSQL/TimescaleDB:** Datos relacionales + time-series
- ✅ **Qdrant:** Embeddings vectoriales + RAG potente + Reranking
- ✅ **MinIO:** Object storage (documentos, archivos, modelos)
- ✅ **OpenSearch:** Logs, auditoría, full-text search
- ✅ **pgvector:** Opcional (casos específicos ACID)

---

## ✅ TU VISIÓN ES CORRECTA

**Por qué tu plan (Qdrant + MinIO + OpenSearch) es óptimo:**

### **1. RAG Potente + Reranking necesita Qdrant**

**Caso real CodeflowX:**

```python
# RAG complejo para compliance queries
query = "¿Qué requisitos Art. 10 AI Act para data governance?"

# PASO 1: Hybrid search (vector + keyword)
results_hybrid = qdrant_client.search(
    collection_name="compliance_docs",
    query_vector=embedding,
    query="Art. 10 data governance",  # Keyword search
    fusion=models.Fusion.RRF  # Reciprocal Rank Fusion
)

# PASO 2: Reranking con modelo especializado
results_reranked = reranker_model.rerank(
    query=query,
    documents=[r.payload["content"] for r in results_hybrid],
    top_k=5
)

# PASO 3: Filtros complejos
results_filtered = qdrant_client.search(
    collection_name="compliance_docs",
    query_vector=embedding,
    query_filter=models.Filter(
        must=[
            models.FieldCondition(
                key="article",
                match=models.MatchAny(any=["Art. 10", "Art. 15"])
            ),
            models.FieldCondition(
                key="framework",
                match=models.MatchValue(value="AI_ACT")
            ),
            models.FieldCondition(
                key="language",
                match=models.MatchValue(value="es")
            )
        ],
        should=[
            models.FieldCondition(
                key="importance",
                match=models.MatchValue(value="high")
            )
        ]
    ),
    score_threshold=0.75,
    limit=10
)
```

**Con pgvector:** Esto sería **muy complejo o imposible** (no hay hybrid search nativo, no hay reranking, filtros limitados).

**Con Qdrant:** **Nativo y optimizado** para esto.

---

### **2. MinIO para documentos/archivos es estándar enterprise**

**Casos uso CodeflowX:**

```python
# Documentos técnicos (Anexo IV)
minio_client.fput_object(
    bucket_name="technical-docs",
    object_name=f"models/{model_id}/anexo_iv.pdf",
    file_path="/tmp/anexo_iv.pdf",
    metadata={
        "model_id": model_id,
        "document_type": "ANEXO_IV",
        "ai_act_article": "Art. 11",
        "created_at": datetime.now().isoformat()
    }
)

# Datasets entrenamiento (Art. 10)
minio_client.fput_object(
    bucket_name="datasets",
    object_name=f"training/{dataset_id}/data.parquet",
    file_path="/tmp/dataset.parquet",
    metadata={
        "dataset_id": dataset_id,
        "quality_score": 0.95,
        "gdpr_compliant": True
    }
)

# Modelos fine-tuned
minio_client.fput_object(
    bucket_name="models",
    object_name=f"finetuned/{model_id}/model.safetensors",
    file_path="/tmp/model.safetensors",
    metadata={
        "base_model": "llama-3-8b",
        "adaptation_type": "LORA",
        "gpai_compliant": True
    }
)

# Documentos KB (RAG)
minio_client.fput_object(
    bucket_name="knowledge-base",
    object_name=f"docs/{doc_id}/document.pdf",
    file_path="/tmp/doc.pdf"
)
```

**Por qué MinIO vs PostgreSQL bytea:**
- ✅ **Archivos grandes:** GB de modelos (PostgreSQL <1GB práctico)
- ✅ **Streaming eficiente:** Direct download sin cargar memoria
- ✅ **S3 compatible:** Fácil migración cloud
- ✅ **Versioning:** Control versiones archivos
- ✅ **Lifecycle policies:** Archivado automático
- ✅ **Multi-tenant:** Buckets por cliente

---

### **3. OpenSearch para logs/auditoría es estándar compliance**

**Casos uso CodeflowX:**

```python
# Logs inmutables Art. 19 (AI Act)
opensearch_client.index(
    index="audit-logs-2025-11",
    body={
        "timestamp": datetime.now().isoformat(),
        "event_type": "MODEL_DEPLOYMENT",
        "user_id": 123,
        "model_id": 456,
        "risk_level": "HIGH_RISK",
        "approval_status": "APPROVED",
        "approver_id": 789,
        "hash_chain": "sha256:...",  # Immutable chain
        "metadata": {
            "project": "Credit Scoring",
            "environment": "PRODUCTION"
        }
    }
)

# Búsqueda auditoría (Art. 72 post-market monitoring)
results = opensearch_client.search(
    index="audit-logs-*",
    body={
        "query": {
            "bool": {
                "must": [
                    {"term": {"model_id": 456}},
                    {"range": {"timestamp": {
                        "gte": "2025-01-01",
                        "lte": "2025-12-31"
                    }}}
                ]
            }
        },
        "aggs": {
            "events_by_type": {
                "terms": {"field": "event_type"}
            }
        }
    }
)

# Full-text search logs
results = opensearch_client.search(
    index="inference-logs-*",
    body={
        "query": {
            "match": {
                "error_message": "bias detected demographic parity"
            }
        }
    }
)
```

**Por qué OpenSearch vs PostgreSQL:**
- ✅ **Full-text search avanzado:** Mejor que PostgreSQL tsvector
- ✅ **Agregaciones complejas:** Dashboards compliance
- ✅ **Tiempo real:** Ingestión masiva logs (millones/día)
- ✅ **Escalabilidad:** Sharding automático
- ✅ **Kibana/Dashboards:** Visualización out-of-box
- ✅ **Retention policies:** Archivado automático logs antiguos

---

## 🏗️ ARQUITECTURA DEFINITIVA

### **Diagrama completo:**

```
┌────────────────────────────────────────────────────────────────┐
│                     CAPA APLICACIÓN                             │
│                                                                 │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐         │
│  │ Backend Java │  │  ZKoss UI    │  │Python FastAPI│         │
│  │ Spring Boot  │  │  (Internal)  │  │ Microservices│         │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘         │
│         │                 │                  │                  │
└─────────┼─────────────────┼──────────────────┼─────────────────┘
          │                 │                  │
          └─────────────────┴──────────────────┘
                            │
          ┌─────────────────┼──────────────────┐
          │                 │                  │
          ▼                 ▼                  ▼
┌─────────────────┐  ┌──────────────┐  ┌──────────────┐
│   PostgreSQL    │  │   Qdrant     │  │    MinIO     │
│  + TimescaleDB  │  │  (Vectors)   │  │   (Files)    │
│  + pgvector     │  │              │  │              │
├─────────────────┤  ├──────────────┤  ├──────────────┤
│ • Relacional    │  │ • Embeddings │  │ • Documentos │
│ • Time-series   │  │ • RAG        │  │ • Datasets   │
│ • Metadata      │  │ • Reranking  │  │ • Modelos    │
│ • Governance    │  │ • Hybrid     │  │ • Archivos   │
│ • Compliance    │  │   search     │  │ • Versioning │
└─────────────────┘  └──────────────┘  └──────────────┘
          │                 │                  │
          ▼                 ▼                  ▼
┌─────────────────────────────────────────────────────┐
│                   OpenSearch                        │
│                   (Logs + Search)                   │
├─────────────────────────────────────────────────────┤
│ • Audit logs (Art. 19 immutable)                    │
│ • Inference logs                                    │
│ • Application logs                                  │
│ • Full-text search                                  │
│ • Compliance dashboards                             │
└─────────────────────────────────────────────────────┘
```

---

## 📊 DISTRIBUCIÓN DATOS POR COMPONENTE

### **PostgreSQL + TimescaleDB:**

**Qué almacena:**
- ✅ **Metadata todo:** Models, Projects, Users, Evaluations, etc.
- ✅ **Governance:** ComplianceAssessments, EthicalReviews, Approvals
- ✅ **Configuración:** Policies, Rules, Workflows
- ✅ **Relaciones:** FK, constraints, transactions ACID
- ✅ **Time-series metadata:** MONITORINGMETRICS (comprimido 10x)

**Tamaño proyectado año 5:** 50-100 GB (con TimescaleDB compression)

**No almacena:**
- ❌ Embeddings (→ Qdrant)
- ❌ Archivos grandes (→ MinIO)
- ❌ Logs masivos (→ OpenSearch)

---

### **Qdrant:**

**Qué almacena:**
- ✅ **Embeddings prompts:** OpenAI ada-002 (1536 dims)
- ✅ **Embeddings documentos KB:** Para RAG
- ✅ **Embeddings chunks:** Documentación técnica
- ✅ **Embeddings evaluaciones:** Similarity search evals
- ✅ **Metadata mínimo:** Refs PostgreSQL (IDs)

**Tamaño proyectado año 5:**
- 5.75M embeddings × 1536 dims × 4 bytes = ~35 GB (sin compresión)
- Con quantization: ~18 GB (50% reducción)

**Collections:**
```python
# 1. prompts
qdrant_client.create_collection(
    collection_name="prompts",
    vectors_config=VectorParams(size=1536, distance=Distance.COSINE)
)

# 2. knowledge_base_docs
qdrant_client.create_collection(
    collection_name="knowledge_base_docs",
    vectors_config=VectorParams(size=1536, distance=Distance.COSINE)
)

# 3. compliance_regulations
qdrant_client.create_collection(
    collection_name="compliance_regulations",
    vectors_config=VectorParams(size=1536, distance=Distance.COSINE),
    hnsw_config=models.HnswConfigDiff(
        m=32,  # Más conexiones = más accuracy RAG compliance
        ef_construct=200
    )
)

# 4. evaluations
qdrant_client.create_collection(
    collection_name="evaluations",
    vectors_config=VectorParams(size=1536, distance=Distance.COSINE)
)
```

---

### **MinIO:**

**Qué almacena:**
- ✅ **Documentos técnicos:** PDFs Anexo IV (Art. 11)
- ✅ **Datasets entrenamiento:** Parquet, CSV (Art. 10)
- ✅ **Modelos fine-tuned:** Safetensors, GGUF
- ✅ **Documentos KB:** PDFs, Word, etc. (antes chunking)
- ✅ **Artefactos evaluación:** Resultados tests
- ✅ **Backups:** Snapshots PostgreSQL, Qdrant

**Tamaño proyectado año 5:**
- Documentos: 100K docs × 5 MB avg = 500 GB
- Datasets: 1,000 datasets × 1 GB avg = 1 TB
- Modelos: 500 modelos × 5 GB avg = 2.5 TB
- **Total:** ~4 TB

**Buckets:**
```python
buckets = [
    "technical-docs",      # Anexo IV, instrucciones uso
    "datasets",            # Training data (Art. 10)
    "models",              # Fine-tuned models
    "knowledge-base",      # RAG source docs
    "evaluation-results",  # Test artifacts
    "backups",             # DB snapshots
    "client-documents"     # Cliente-specific docs
]
```

---

### **OpenSearch:**

**Qué almacena:**
- ✅ **Audit logs:** Inmutables Art. 19 (hash chain)
- ✅ **Inference logs:** Millones/día (time-series)
- ✅ **Application logs:** Errores, warnings, info
- ✅ **Security logs:** Accesos, intentos no autorizados
- ✅ **Performance logs:** Métricas latencia, throughput

**Tamaño proyectado año 5:**
- Inference logs: 5B logs × 1 KB avg = 5 TB (sin compresión)
- Con compression: ~500 GB
- Audit logs: ~100 GB
- App logs: ~50 GB
- **Total:** ~650 GB (comprimido)

**Índices:**
```python
indices = [
    "audit-logs-YYYY-MM",          # Monthly rotation
    "inference-logs-YYYY-MM-DD",   # Daily rotation
    "application-logs-YYYY-MM",    # Monthly
    "security-logs-YYYY-MM",       # Monthly
    "performance-metrics-YYYY-MM"  # Monthly
]
```

**Retention:**
- Audit logs: 10 años (compliance)
- Inference logs: 12 meses (después → MinIO archive)
- App logs: 3 meses
- Security logs: 2 años

---

## 🎯 PGVECTOR: OPCIONAL PARA CASOS ESPECÍFICOS

**Ahora que tienes Qdrant, pgvector es opcional.**

**Cuándo usar pgvector (coexiste con Qdrant):**

### **Caso 1: Embeddings con transacciones ACID críticas**

```python
# Caso: Prompt + embedding DEBEN insertarse juntos o ninguno
# (Si falla embedding, rollback prompt también)

@transaction
def create_prompt_with_embedding(prompt_text, project_id):
    # 1. Insert prompt (PostgreSQL)
    prompt = Prompt(
        prompt_text=prompt_text,
        project_id=project_id
    )
    db.add(prompt)
    db.flush()  # Get ID
    
    # 2. Generate embedding
    embedding = openai.embed(prompt_text)
    
    # 3. Insert embedding (pgvector - MISMA transaction)
    prompt.embedding = embedding
    
    # 4. Commit (ambos o ninguno)
    db.commit()  # ACID garantizado

# Con Qdrant: No puedes rollback Qdrant si falla PostgreSQL
```

**Usar pgvector si:** Integridad transaccional crítica (raro).

---

### **Caso 2: Embeddings pequeños con joins frecuentes**

```sql
-- Query común: Prompts similares + metadata proyecto + usuario (1 query SQL)
SELECT p.prompt_text,
       pr.project_name,
       u.user_name,
       1 - (p.embedding <=> $1) AS similarity
FROM prompts p
JOIN projects pr ON p.project_id = pr.id
JOIN users u ON p.user_id = u.id
WHERE pr.status = 'ACTIVE'
  AND u.role = 'ADMIN'
  AND 1 - (p.embedding <=> $1) > 0.8
ORDER BY p.embedding <=> $1
LIMIT 10;

-- Con Qdrant: 2 queries + merge manual
```

**Usar pgvector si:** Joins complejos frecuentes + <100K embeddings.

---

### **Caso 3: Embeddings auxiliares (no RAG principal)**

Ejemplos:
- Embeddings usuarios (similarity usuarios)
- Embeddings proyectos (similarity proyectos)
- Embeddings tags/categorías

**Usar pgvector si:** Embeddings auxiliares, no performance crítica.

---

**RECOMENDACIÓN FINAL pgvector:**

```
Qdrant: RAG principal (99% casos)
    ↓
pgvector: Casos específicos ACID o joins (1% casos)
```

No necesitas migrar todo a pgvector. Conviven perfectamente.

---

## 💰 COSTES INFRAESTRUCTURA COMPLETA

### **Año 1 (Startup):**

| Componente | Cloud (AWS/Azure) | Self-hosted |
|-----------|-------------------|-------------|
| **PostgreSQL/TimescaleDB** | $800/mes (db.r6g.2xlarge) | $5K one-time + $150/mes |
| **Qdrant** | $400/mes (r6g.xlarge) | $3K one-time + $100/mes |
| **MinIO** | $200/mes (500 GB S3) | $2K one-time + $50/mes |
| **OpenSearch** | $600/mes (3 nodes) | $6K one-time + $200/mes |
| **Python micros** | $400/mes (K8s) | Incluido servers |
| **Total** | **$2,400/mes** | **$16K one-time + $500/mes** |

---

### **Año 5 (Enterprise):**

| Componente | Cloud | Self-hosted |
|-----------|-------|-------------|
| **PostgreSQL/TimescaleDB** | $2,000/mes (master + 2 replicas) | $20K + $600/mes |
| **Qdrant** | $1,200/mes (cluster 3 nodes) | $12K + $400/mes |
| **MinIO** | $800/mes (4 TB) | $8K + $200/mes |
| **OpenSearch** | $2,000/mes (6 nodes) | $24K + $800/mes |
| **Python micros** | $1,000/mes (K8s scaled) | Incluido |
| **Total** | **$7,000/mes** | **$64K one-time + $2,000/mes** |

**ROI self-hosted año 5:** $7K × 60 meses - $64K = $356K ahorro

---

## 🚀 PLAN IMPLEMENTACIÓN

### **FASE 1: Q4 2025 (Setup inicial)**

**1. PostgreSQL/TimescaleDB (ya tienes):**
- ✅ Configurar hypertables
- ✅ Compression policies
- ✅ Retention policies

**2. Qdrant (nuevo):**
```bash
# Docker Compose
version: '3.8'
services:
  qdrant:
    image: qdrant/qdrant:v1.7.0
    ports:
      - "6333:6333"
      - "6334:6334"
    volumes:
      - ./qdrant_storage:/qdrant/storage
    environment:
      - QDRANT__SERVICE__GRPC_PORT=6334
```

**3. MinIO (nuevo):**
```bash
# Docker Compose
  minio:
    image: minio/minio:latest
    ports:
      - "9000:9000"
      - "9001:9001"
    volumes:
      - ./minio_data:/data
    environment:
      - MINIO_ROOT_USER=admin
      - MINIO_ROOT_PASSWORD=secure_password
    command: server /data --console-address ":9001"
```

**4. OpenSearch (nuevo):**
```bash
# Docker Compose
  opensearch:
    image: opensearchproject/opensearch:2.11.0
    ports:
      - "9200:9200"
      - "9600:9600"
    volumes:
      - ./opensearch_data:/usr/share/opensearch/data
    environment:
      - discovery.type=single-node
      - OPENSEARCH_JAVA_OPTS=-Xms2g -Xmx2g
```

---

### **FASE 2: Q1 2026 (Microservicios integración)**

**Microservicio RAG (Python FastAPI):**

```python
# services/rag_service.py
class EnterpriseRAGService:
    def __init__(self, qdrant_client, minio_client, opensearch_client, db):
        self.qdrant = qdrant_client
        self.minio = minio_client
        self.opensearch = opensearch_client
        self.db = db
    
    async def process_document(self, file_path, metadata):
        """Pipeline completo: MinIO → chunking → embedding → Qdrant"""
        
        # 1. Upload a MinIO
        object_name = f"kb_docs/{metadata['project_id']}/{file_path.name}"
        self.minio.fput_object(
            bucket_name="knowledge-base",
            object_name=object_name,
            file_path=file_path
        )
        
        # 2. Extract text + chunking
        chunks = await self.chunk_document(file_path)
        
        # 3. Generate embeddings (batch)
        embeddings = await self.embed_batch([c.text for c in chunks])
        
        # 4. Insert Qdrant
        points = [
            models.PointStruct(
                id=str(uuid.uuid4()),
                vector=emb,
                payload={
                    "chunk_text": chunk.text,
                    "document_id": metadata["document_id"],
                    "project_id": metadata["project_id"],
                    "minio_path": object_name,
                    "page_number": chunk.page
                }
            ) for chunk, emb in zip(chunks, embeddings)
        ]
        
        self.qdrant.upsert(
            collection_name="knowledge_base_docs",
            points=points
        )
        
        # 5. Log OpenSearch
        await self.opensearch.index(
            index="document-processing-logs",
            body={
                "timestamp": datetime.now(),
                "document_id": metadata["document_id"],
                "chunks_created": len(chunks),
                "minio_path": object_name,
                "status": "SUCCESS"
            }
        )
        
        return {"chunks_created": len(chunks), "minio_path": object_name}
    
    async def query_rag(self, user_query, project_id, rerank=True):
        """RAG query con reranking"""
        
        # 1. Generate query embedding
        query_embedding = await self.embed(user_query)
        
        # 2. Hybrid search Qdrant
        results = self.qdrant.search(
            collection_name="knowledge_base_docs",
            query_vector=query_embedding,
            query=user_query,  # Keyword search
            query_filter=models.Filter(
                must=[
                    models.FieldCondition(
                        key="project_id",
                        match=models.MatchValue(value=project_id)
                    )
                ]
            ),
            fusion=models.Fusion.RRF,  # Reciprocal Rank Fusion
            limit=20
        )
        
        # 3. Reranking (opcional pero recomendado)
        if rerank:
            results = await self.rerank(user_query, results, top_k=5)
        
        # 4. Build context
        context = "\n\n".join([r.payload["chunk_text"] for r in results[:5]])
        
        # 5. Generate answer (LLM)
        answer = await self.llm.chat_completion(
            messages=[
                {"role": "system", "content": f"Context:\n{context}"},
                {"role": "user", "content": user_query}
            ]
        )
        
        # 6. Log inference OpenSearch
        await self.opensearch.index(
            index="rag-queries",
            body={
                "timestamp": datetime.now(),
                "query": user_query,
                "project_id": project_id,
                "num_sources": len(results),
                "reranked": rerank,
                "answer_length": len(answer)
            }
        )
        
        return {
            "answer": answer,
            "sources": results[:5],
            "total_sources": len(results)
        }
```

---

## ✅ CONCLUSIÓN ACTUALIZADA

**Tu plan original era correcto:** Qdrant + MinIO + OpenSearch para arquitectura enterprise robusta.

**Arquitectura final:**
- ✅ **PostgreSQL/TimescaleDB:** Datos relacionales + time-series (50-100 GB)
- ✅ **Qdrant:** RAG potente + reranking + embeddings (18-35 GB)
- ✅ **MinIO:** Documentos + datasets + modelos (4 TB)
- ✅ **OpenSearch:** Logs + auditoría + full-text (650 GB)
- ⚠️ **pgvector:** Opcional (casos ACID específicos)

**Principio:** Especialización > Todo-en-uno

**Coste año 5:** $7K/mes cloud o $2K/mes self-hosted

**Ventajas:**
- ✅ RAG profesional (hybrid search, reranking)
- ✅ Escalabilidad probada (cada componente optimizado)
- ✅ Compliance robusto (logs inmutables OpenSearch)
- ✅ Multi-tenant fácil (buckets MinIO, collections Qdrant)

**¿Empezamos con setup Qdrant + MinIO + OpenSearch?** 🚀

---

**Última actualización:** 5 Noviembre 2025  
**Decisión:** Arquitectura enterprise con componentes especializados  
**Próximo paso:** Docker Compose multi-componente
