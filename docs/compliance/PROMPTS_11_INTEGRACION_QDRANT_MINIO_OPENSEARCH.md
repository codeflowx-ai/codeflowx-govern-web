# PROMPTS_11: INTEGRACIÓN QDRANT + MINIO + OPENSEARCH
## Setup e Integración Componentes Especializados

**Fecha:** 5 Noviembre 2025  
**Propósito:** Integrar Qdrant (vectores), MinIO (files), OpenSearch (logs) con stack existente  
**Arquitectura:** Componentes especializados enterprise  
**Prioridad:** 🟡 ALTA (necesario RAG potente + compliance robusto)

---

## 🎯 VISIÓN ARQUITECTURA

```
Backend Java (Spring Boot)
    ↓
PostgreSQL/TimescaleDB  (Relacional + Time-series)
    +
Qdrant  (Embeddings + RAG + Reranking)
    +
MinIO  (Documentos + Datasets + Modelos)
    +
OpenSearch  (Logs + Auditoría + Full-text)
```

**Total prompts:** 15 prompts
- **Qdrant:** 5 prompts (setup, collections, integration, RAG, reranking)
- **MinIO:** 5 prompts (setup, buckets, integration, upload/download, lifecycle)
- **OpenSearch:** 5 prompts (setup, indices, integration, logs, dashboards)

---

## 📦 GRUPO A: QDRANT (EMBEDDINGS + RAG)

### **PROMPT 1: Setup Qdrant + Collections Básicas**

**Objetivo:** Instalar Qdrant y crear collections principales para RAG

**Arquitectura:**
```
Qdrant Server (Docker/K8s)
    ↓
Collections:
- prompts (embeddings prompts governance)
- knowledge_base_docs (documentos KB RAG)
- compliance_regulations (regulaciones AI Act, GDPR, ISOs)
- evaluations (embeddings evaluaciones similarity)
```

**Crear:**

1. **Docker Compose Qdrant:**

```yaml
# docker/qdrant/docker-compose.yml
version: '3.8'
services:
  qdrant:
    image: qdrant/qdrant:v1.7.0
    container_name: codeflowx-qdrant
    restart: unless-stopped
    ports:
      - "6333:6333"  # HTTP API
      - "6334:6334"  # gRPC
    volumes:
      - ./qdrant_storage:/qdrant/storage
      - ./qdrant_snapshots:/qdrant/snapshots
    environment:
      - QDRANT__SERVICE__GRPC_PORT=6334
      - QDRANT__SERVICE__HTTP_PORT=6333
      - QDRANT__LOG_LEVEL=INFO
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:6333/health"]
      interval: 30s
      timeout: 10s
      retries: 3
    networks:
      - codeflowx-network

networks:
  codeflowx-network:
    external: true
```

2. **Script inicialización collections:**

```python
# scripts/init_qdrant_collections.py
from qdrant_client import QdrantClient
from qdrant_client.models import VectorParams, Distance, HnswConfigDiff

def init_qdrant_collections():
    """Inicializar collections Qdrant para CodeflowX"""
    
    client = QdrantClient(url="http://localhost:6333")
    
    collections = [
        {
            "name": "prompts",
            "vector_size": 1536,  # OpenAI ada-002
            "distance": Distance.COSINE,
            "hnsw_config": HnswConfigDiff(m=16, ef_construct=100),
            "description": "Embeddings prompts governance"
        },
        {
            "name": "knowledge_base_docs",
            "vector_size": 1536,
            "distance": Distance.COSINE,
            "hnsw_config": HnswConfigDiff(m=32, ef_construct=200),  # Más accuracy RAG
            "description": "Documentos KB chunkeados para RAG"
        },
        {
            "name": "compliance_regulations",
            "vector_size": 1536,
            "distance": Distance.COSINE,
            "hnsw_config": HnswConfigDiff(m=32, ef_construct=200),
            "description": "Regulaciones AI Act, GDPR, ISOs para RAG compliance"
        },
        {
            "name": "evaluations",
            "vector_size": 1536,
            "distance": Distance.COSINE,
            "hnsw_config": HnswConfigDiff(m=16, ef_construct=100),
            "description": "Embeddings evaluaciones para similarity search"
        }
    ]
    
    for collection in collections:
        try:
            # Check si existe
            existing = client.get_collections()
            if collection["name"] not in [c.name for c in existing.collections]:
                client.create_collection(
                    collection_name=collection["name"],
                    vectors_config=VectorParams(
                        size=collection["vector_size"],
                        distance=collection["distance"]
                    ),
                    hnsw_config=collection["hnsw_config"]
                )
                print(f"✅ Collection '{collection['name']}' created")
            else:
                print(f"⚠️  Collection '{collection['name']}' already exists")
        except Exception as e:
            print(f"❌ Error creating collection '{collection['name']}': {e}")
    
    print("\n🎉 Qdrant collections initialized!")

if __name__ == "__main__":
    init_qdrant_collections()
```

3. **Configuración application.properties:**

```properties
# config/application-qdrant.properties

# Qdrant configuration
qdrant.url=http://localhost:6333
qdrant.grpc.url=http://localhost:6334
qdrant.api.key=${QDRANT_API_KEY:}
qdrant.timeout=30000
qdrant.connection.pool.size=50
qdrant.max.retries=3

# Collections
qdrant.collection.prompts=prompts
qdrant.collection.knowledge.base=knowledge_base_docs
qdrant.collection.compliance=compliance_regulations
qdrant.collection.evaluations=evaluations
```

**Verificar:**
- Docker Compose up correcto
- Collections creadas (curl http://localhost:6333/collections)
- Health check OK

---

### **PROMPT 2: Cliente Java Qdrant + Service Layer**

**Objetivo:** Cliente Java para interactuar con Qdrant desde backend Spring Boot

**Crear:**

1. **Dependency Maven:**

```xml
<!-- pom.xml -->
<dependency>
    <groupId>io.qdrant</groupId>
    <artifactId>client</artifactId>
    <version>1.7.0</version>
</dependency>
```

2. **QdrantConfig:**

```java
// config/QdrantConfig.java
package com.codeflowx.govern.config;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QdrantConfig {
    
    @Value("${qdrant.url}")
    private String qdrantUrl;
    
    @Value("${qdrant.grpc.url}")
    private String qdrantGrpcUrl;
    
    @Value("${qdrant.api.key:}")
    private String apiKey;
    
    @Bean
    public QdrantClient qdrantClient() {
        QdrantGrpcClient.Builder builder = QdrantGrpcClient.newBuilder(
            qdrantGrpcUrl.replace("http://", ""),
            6334,  // gRPC port
            false   // TLS disabled local
        );
        
        if (apiKey != null && !apiKey.isEmpty()) {
            builder.withApiKey(apiKey);
        }
        
        return new QdrantClient(builder.build());
    }
}
```

3. **QdrantService:**

```java
// service/vector/QdrantService.java
package com.codeflowx.govern.service.vector;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Points.*;
import io.qdrant.client.grpc.Collections.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QdrantService {
    
    @Autowired
    private QdrantClient qdrantClient;
    
    @Value("${qdrant.collection.prompts}")
    private String promptsCollection;
    
    @Value("${qdrant.collection.knowledge.base}")
    private String knowledgeBaseCollection;
    
    @Value("${qdrant.collection.compliance}")
    private String complianceCollection;
    
    /**
     * Insert vector en collection
     */
    public String insertVector(String collectionName, float[] vector, Map<String, Object> payload) {
        try {
            String pointId = UUID.randomUUID().toString();
            
            PointStruct point = PointStruct.newBuilder()
                .setId(PointId.newBuilder().setUuid(pointId).build())
                .setVectors(Vectors.newBuilder().setVector(
                    Vector.newBuilder().addAllData(
                        Arrays.stream(vector).boxed().collect(Collectors.toList())
                    ).build()
                ).build())
                .putAllPayload(convertPayload(payload))
                .build();
            
            qdrantClient.upsertAsync(
                collectionName,
                List.of(point)
            ).get();
            
            log.info("✅ Vector inserted in collection '{}': {}", collectionName, pointId);
            return pointId;
            
        } catch (Exception e) {
            log.error("❌ Error inserting vector in Qdrant: {}", e.getMessage());
            throw new RuntimeException("Error inserting vector", e);
        }
    }
    
    /**
     * Search similar vectors
     */
    public List<ScoredPoint> searchSimilar(
        String collectionName,
        float[] queryVector,
        int limit,
        Float scoreThreshold
    ) {
        try {
            SearchPoints.Builder searchBuilder = SearchPoints.newBuilder()
                .setCollectionName(collectionName)
                .addAllVector(Arrays.stream(queryVector).boxed().collect(Collectors.toList()))
                .setLimit(limit)
                .setWithPayload(WithPayloadSelector.newBuilder().setEnable(true).build());
            
            if (scoreThreshold != null) {
                searchBuilder.setScoreThreshold(scoreThreshold);
            }
            
            List<ScoredPoint> results = qdrantClient.searchAsync(
                searchBuilder.build()
            ).get();
            
            log.info("✅ Search executed in collection '{}': {} results", 
                     collectionName, results.size());
            return results;
            
        } catch (Exception e) {
            log.error("❌ Error searching in Qdrant: {}", e.getMessage());
            throw new RuntimeException("Error searching vectors", e);
        }
    }
    
    /**
     * Search con filtros
     */
    public List<ScoredPoint> searchWithFilters(
        String collectionName,
        float[] queryVector,
        Filter filter,
        int limit
    ) {
        try {
            SearchPoints searchRequest = SearchPoints.newBuilder()
                .setCollectionName(collectionName)
                .addAllVector(Arrays.stream(queryVector).boxed().collect(Collectors.toList()))
                .setFilter(filter)
                .setLimit(limit)
                .setWithPayload(WithPayloadSelector.newBuilder().setEnable(true).build())
                .build();
            
            List<ScoredPoint> results = qdrantClient.searchAsync(searchRequest).get();
            
            log.info("✅ Filtered search executed: {} results", results.size());
            return results;
            
        } catch (Exception e) {
            log.error("❌ Error filtered search in Qdrant: {}", e.getMessage());
            throw new RuntimeException("Error filtered search", e);
        }
    }
    
    /**
     * Delete vector
     */
    public void deleteVector(String collectionName, String pointId) {
        try {
            qdrantClient.deleteAsync(
                collectionName,
                PointsSelector.newBuilder()
                    .setPoints(
                        PointsIdsList.newBuilder()
                            .addIds(PointId.newBuilder().setUuid(pointId).build())
                            .build()
                    ).build()
            ).get();
            
            log.info("✅ Vector deleted from collection '{}': {}", collectionName, pointId);
            
        } catch (Exception e) {
            log.error("❌ Error deleting vector from Qdrant: {}", e.getMessage());
            throw new RuntimeException("Error deleting vector", e);
        }
    }
    
    private Map<String, Value> convertPayload(Map<String, Object> payload) {
        // Convert Java Map to Qdrant Value map
        // Implementation details...
        return new HashMap<>();
    }
}
```

**Verificar:**
- QdrantClient bean creado
- Conexión a Qdrant OK
- Insert/search/delete funciona

---

### **PROMPT 3: Microservicio Python RAG con Qdrant**

**Objetivo:** Microservicio FastAPI para RAG usando Qdrant (chunking, embedding, search, reranking)

**Crear:**

```python
# leka-rag-service/main.py
from fastapi import FastAPI, HTTPException, Depends
from pydantic import BaseModel
from typing import List, Optional, Dict, Any
from qdrant_client import QdrantClient
from qdrant_client.models import PointStruct, Filter, FieldCondition, MatchValue, SearchRequest
import openai
import tiktoken
import structlog

logger = structlog.get_logger(__name__)

app = FastAPI(title="CodeflowX RAG Service", version="1.0.0")

# Qdrant client
qdrant_client = QdrantClient(url="http://qdrant:6333")

# OpenAI client
openai.api_key = os.getenv("OPENAI_API_KEY")

# Models
class DocumentChunkRequest(BaseModel):
    document_id: str
    document_text: str
    metadata: Dict[str, Any]
    chunk_size: int = 1000
    chunk_overlap: int = 200

class RAGQueryRequest(BaseModel):
    query: str
    collection_name: str
    filters: Optional[Dict[str, Any]] = None
    top_k: int = 5
    rerank: bool = True
    score_threshold: float = 0.7

class RAGQueryResponse(BaseModel):
    answer: str
    sources: List[Dict[str, Any]]
    total_sources: int
    query_embedding_time_ms: float
    search_time_ms: float
    llm_generation_time_ms: float

# Service
class RAGService:
    def __init__(self, qdrant_client: QdrantClient):
        self.qdrant = qdrant_client
        self.tokenizer = tiktoken.get_encoding("cl100k_base")
    
    def chunk_text(
        self, 
        text: str, 
        chunk_size: int = 1000, 
        chunk_overlap: int = 200
    ) -> List[str]:
        """Chunk text con overlap para context preservation"""
        
        tokens = self.tokenizer.encode(text)
        chunks = []
        
        start = 0
        while start < len(tokens):
            end = start + chunk_size
            chunk_tokens = tokens[start:end]
            chunk_text = self.tokenizer.decode(chunk_tokens)
            chunks.append(chunk_text)
            start = end - chunk_overlap  # Overlap
        
        logger.info(f"Text chunked into {len(chunks)} chunks")
        return chunks
    
    def embed_text(self, text: str) -> List[float]:
        """Generate embedding usando OpenAI"""
        
        response = openai.embeddings.create(
            model="text-embedding-ada-002",
            input=text
        )
        return response.data[0].embedding
    
    def embed_batch(self, texts: List[str]) -> List[List[float]]:
        """Batch embeddings (más eficiente)"""
        
        response = openai.embeddings.create(
            model="text-embedding-ada-002",
            input=texts
        )
        return [data.embedding for data in response.data]
    
    async def process_document(
        self,
        document_id: str,
        document_text: str,
        metadata: Dict[str, Any],
        collection_name: str,
        chunk_size: int = 1000
    ) -> Dict[str, Any]:
        """Pipeline completo: chunk → embed → insert Qdrant"""
        
        # 1. Chunk
        chunks = self.chunk_text(document_text, chunk_size)
        
        # 2. Embed batch
        embeddings = self.embed_batch(chunks)
        
        # 3. Prepare points
        points = []
        for i, (chunk, embedding) in enumerate(zip(chunks, embeddings)):
            point = PointStruct(
                id=f"{document_id}_chunk_{i}",
                vector=embedding,
                payload={
                    "document_id": document_id,
                    "chunk_index": i,
                    "chunk_text": chunk,
                    **metadata
                }
            )
            points.append(point)
        
        # 4. Insert Qdrant
        self.qdrant.upsert(
            collection_name=collection_name,
            points=points
        )
        
        logger.info(
            f"Document processed",
            document_id=document_id,
            chunks_created=len(chunks),
            collection=collection_name
        )
        
        return {
            "document_id": document_id,
            "chunks_created": len(chunks),
            "collection": collection_name
        }
    
    async def query_rag(
        self,
        query: str,
        collection_name: str,
        filters: Optional[Dict[str, Any]] = None,
        top_k: int = 5,
        rerank: bool = True,
        score_threshold: float = 0.7
    ) -> RAGQueryResponse:
        """RAG query con optional reranking"""
        
        import time
        
        # 1. Embed query
        t0 = time.time()
        query_embedding = self.embed_text(query)
        embedding_time = (time.time() - t0) * 1000
        
        # 2. Build Qdrant filter
        qdrant_filter = None
        if filters:
            must_conditions = []
            for key, value in filters.items():
                must_conditions.append(
                    FieldCondition(
                        key=key,
                        match=MatchValue(value=value)
                    )
                )
            qdrant_filter = Filter(must=must_conditions)
        
        # 3. Search Qdrant
        t1 = time.time()
        search_results = self.qdrant.search(
            collection_name=collection_name,
            query_vector=query_embedding,
            query_filter=qdrant_filter,
            limit=top_k * 2 if rerank else top_k,  # More candidates if reranking
            score_threshold=score_threshold
        )
        search_time = (time.time() - t1) * 1000
        
        # 4. Rerank (optional)
        if rerank and len(search_results) > top_k:
            search_results = self._rerank(query, search_results, top_k)
        
        # 5. Build context
        context_chunks = [r.payload["chunk_text"] for r in search_results[:top_k]]
        context = "\n\n---\n\n".join(context_chunks)
        
        # 6. Generate answer
        t2 = time.time()
        answer = openai.chat.completions.create(
            model="gpt-4",
            messages=[
                {
                    "role": "system",
                    "content": f"Eres un experto en compliance AI Act y GDPR. "
                               f"Responde basándote SOLO en el contexto proporcionado.\n\n"
                               f"Contexto:\n{context}"
                },
                {
                    "role": "user",
                    "content": query
                }
            ],
            temperature=0.3,
            max_tokens=500
        )
        llm_time = (time.time() - t2) * 1000
        
        # 7. Format sources
        sources = [
            {
                "document_id": r.payload.get("document_id"),
                "chunk_index": r.payload.get("chunk_index"),
                "chunk_text": r.payload.get("chunk_text"),
                "score": r.score,
                "metadata": {k: v for k, v in r.payload.items() 
                            if k not in ["chunk_text", "document_id", "chunk_index"]}
            }
            for r in search_results[:top_k]
        ]
        
        return RAGQueryResponse(
            answer=answer.choices[0].message.content,
            sources=sources,
            total_sources=len(search_results),
            query_embedding_time_ms=embedding_time,
            search_time_ms=search_time,
            llm_generation_time_ms=llm_time
        )
    
    def _rerank(self, query: str, results: List, top_k: int) -> List:
        """Rerank usando cross-encoder (mejora relevancia 30-50%)"""
        
        # TODO: Implement con modelo reranking (sentence-transformers/cross-encoder)
        # Por ahora retorna top results por score
        return sorted(results, key=lambda x: x.score, reverse=True)[:top_k]

# Initialize service
rag_service = RAGService(qdrant_client)

# Endpoints
@app.post("/api/rag/process-document")
async def process_document_endpoint(request: DocumentChunkRequest):
    """Procesar documento: chunk + embed + insert Qdrant"""
    try:
        result = await rag_service.process_document(
            document_id=request.document_id,
            document_text=request.document_text,
            metadata=request.metadata,
            collection_name="knowledge_base_docs",
            chunk_size=request.chunk_size
        )
        return result
    except Exception as e:
        logger.error(f"Error processing document: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/rag/query", response_model=RAGQueryResponse)
async def rag_query_endpoint(request: RAGQueryRequest):
    """RAG query con reranking opcional"""
    try:
        result = await rag_service.query_rag(
            query=request.query,
            collection_name=request.collection_name,
            filters=request.filters,
            top_k=request.top_k,
            rerank=request.rerank,
            score_threshold=request.score_threshold
        )
        return result
    except Exception as e:
        logger.error(f"Error RAG query: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/health")
async def health_check():
    return {"status": "healthy", "service": "rag-service"}
```

**Verificar:**
- Microservicio FastAPI funciona
- Process document OK (chunk + embed + insert)
- RAG query OK (embed query + search + LLM answer)

---

### **PROMPT 4: Integración Qdrant con Backend Java (Prompts)**

**Objetivo:** Integrar Qdrant para embeddings de prompts desde backend Java

**Flujo:**
```
User crea Prompt (Backend Java)
    ↓
Insert PostgreSQL (metadata)
    ↓
Generate embedding (OpenAI API)
    ↓
Insert Qdrant (vector + payload con prompt_id ref)
    ↓
Guardar qdrant_point_id en PostgreSQL
```

**Crear:**

```java
// service/PromptEmbeddingService.java
package com.codeflowx.govern.service.embedding;

import com.codeflowx.govern.entity.prompts.Prompt;
import com.codeflowx.govern.service.vector.QdrantService;
import com.codeflowx.govern.service.external.OpenAIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class PromptEmbeddingService {
    
    @Autowired
    private QdrantService qdrantService;
    
    @Autowired
    private OpenAIService openAIService;
    
    @Value("${qdrant.collection.prompts}")
    private String promptsCollection;
    
    /**
     * Generate + store embedding para prompt
     */
    @Transactional
    public void generateAndStoreEmbedding(Prompt prompt) {
        try {
            log.info("Generating embedding for prompt: {}", prompt.getIdxprompt());
            
            // 1. Generate embedding (OpenAI)
            float[] embedding = openAIService.generateEmbedding(prompt.getPrmpromptText());
            
            // 2. Prepare payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("prompt_id", prompt.getIdxprompt());
            payload.put("prompt_text", prompt.getPrmpromptText());
            payload.put("project_id", prompt.getPrmprojectId());
            payload.put("user_id", prompt.getPrmuserId());
            payload.put("created_at", prompt.getPrmcreatedAt().toString());
            
            // 3. Insert Qdrant
            String pointId = qdrantService.insertVector(
                promptsCollection,
                embedding,
                payload
            );
            
            // 4. Save qdrant_point_id en PostgreSQL
            prompt.setPrmqdrantPointId(pointId);
            // Auto-saved por @Transactional
            
            log.info("✅ Embedding stored for prompt {} with point_id {}", 
                     prompt.getIdxprompt(), pointId);
            
        } catch (Exception e) {
            log.error("❌ Error generating embedding for prompt {}: {}", 
                      prompt.getIdxprompt(), e.getMessage());
            throw new RuntimeException("Error generating embedding", e);
        }
    }
    
    /**
     * Search similar prompts
     */
    public List<PromptSimilarityResult> searchSimilarPrompts(
        String queryText,
        Long projectId,
        int limit
    ) {
        try {
            // 1. Generate query embedding
            float[] queryEmbedding = openAIService.generateEmbedding(queryText);
            
            // 2. Build filter (project_id)
            Filter filter = Filter.newBuilder()
                .addMust(Condition.newBuilder()
                    .setField(FieldCondition.newBuilder()
                        .setKey("project_id")
                        .setMatch(Match.newBuilder()
                            .setValue(Value.newBuilder().setIntegerValue(projectId).build())
                            .build())
                        .build())
                    .build())
                .build();
            
            // 3. Search Qdrant
            List<ScoredPoint> results = qdrantService.searchWithFilters(
                promptsCollection,
                queryEmbedding,
                filter,
                limit
            );
            
            // 4. Convert to DTO
            return results.stream()
                .map(r -> new PromptSimilarityResult(
                    r.getPayloadOrThrow("prompt_id").getIntegerValue(),
                    r.getPayloadOrThrow("prompt_text").getStringValue(),
                    r.getScore()
                ))
                .collect(Collectors.toList());
            
        } catch (Exception e) {
            log.error("❌ Error searching similar prompts: {}", e.getMessage());
            throw new RuntimeException("Error searching prompts", e);
        }
    }
}
```

**Modificar entity Prompt:**

```java
// entity/prompts/Prompt.java
@Entity
@Table(name = "PROMPTS")
public class Prompt {
    // ... campos existentes ...
    
    @Column(name = "PRMQDRANT_POINT_ID", length = 100)
    private String prmqdrantPointId;  // Ref Qdrant
    
    // Getters/setters...
}
```

**Crear migration SQL:**

```sql
-- V1.XX__add_qdrant_point_id_prompts.sql
ALTER TABLE PROMPTS ADD COLUMN PRMQDRANT_POINT_ID VARCHAR(100);
CREATE INDEX idx_prompts_qdrant_point_id ON PROMPTS(PRMQDRANT_POINT_ID);
```

**Verificar:**
- Prompt creado → embedding generado automáticamente
- qdrant_point_id guardado en PostgreSQL
- Search similar prompts funciona

---

### **PROMPT 5: Reranking Avanzado para RAG**

**Objetivo:** Implementar reranking con modelo cross-encoder para mejorar relevancia RAG 30-50%

**Crear:**

```python
# leka-rag-service/services/reranker_service.py
from sentence_transformers import CrossEncoder
from typing import List, Dict, Any
import structlog

logger = structlog.get_logger(__name__)

class RerankerService:
    """Reranking con cross-encoder para mejorar relevancia RAG"""
    
    def __init__(self, model_name: str = "cross-encoder/ms-marco-MiniLM-L-6-v2"):
        self.model = CrossEncoder(model_name)
        logger.info(f"Reranker model loaded: {model_name}")
    
    def rerank(
        self,
        query: str,
        documents: List[Dict[str, Any]],
        top_k: int = 5,
        score_key: str = "chunk_text"
    ) -> List[Dict[str, Any]]:
        """
        Rerank documents usando cross-encoder
        
        Args:
            query: Query original
            documents: Documentos candidatos (de Qdrant)
            top_k: Top N documentos retornar
            score_key: Key en dict con texto documento
            
        Returns:
            Documentos rerankeados por relevancia
        """
        
        # 1. Prepare pairs (query, document)
        pairs = [(query, doc[score_key]) for doc in documents]
        
        # 2. Compute cross-encoder scores
        scores = self.model.predict(pairs)
        
        # 3. Add rerank scores to documents
        for doc, score in zip(documents, scores):
            doc["rerank_score"] = float(score)
            doc["original_score"] = doc.get("score", 0.0)  # Qdrant similarity score
        
        # 4. Sort by rerank score
        reranked = sorted(documents, key=lambda x: x["rerank_score"], reverse=True)
        
        logger.info(
            f"Reranked {len(documents)} documents to top {top_k}",
            original_top_score=documents[0].get("score"),
            reranked_top_score=reranked[0]["rerank_score"]
        )
        
        return reranked[:top_k]
```

**Actualizar RAGService para usar reranker:**

```python
# leka-rag-service/main.py (actualizar)
from services.reranker_service import RerankerService

class RAGService:
    def __init__(self, qdrant_client: QdrantClient):
        self.qdrant = qdrant_client
        self.tokenizer = tiktoken.get_encoding("cl100k_base")
        self.reranker = RerankerService()  # Initialize reranker
    
    def _rerank(self, query: str, results: List, top_k: int) -> List:
        """Rerank usando cross-encoder"""
        
        # Convert Qdrant ScoredPoint to dict
        documents = [
            {
                "chunk_text": r.payload["chunk_text"],
                "document_id": r.payload.get("document_id"),
                "score": r.score,
                "payload": r.payload
            }
            for r in results
        ]
        
        # Rerank
        reranked_docs = self.reranker.rerank(query, documents, top_k)
        
        # Convert back to ScoredPoint format
        # (keep original structure but reordered)
        reranked_results = []
        for doc in reranked_docs:
            # Find original ScoredPoint
            original = next(r for r in results 
                           if r.payload["chunk_text"] == doc["chunk_text"])
            reranked_results.append(original)
        
        return reranked_results
```

**Verificar:**
- Reranker model loaded
- Reranking mejora relevancia (comparar con/sin reranking)
- Performance acceptable (<500ms reranking)

---

## 📦 GRUPO B: MINIO (OBJECT STORAGE)

### **PROMPT 6: Setup MinIO + Buckets**

**Objetivo:** Instalar MinIO y crear buckets para documentos, datasets, modelos

**Crear:**

```yaml
# docker/minio/docker-compose.yml
version: '3.8'
services:
  minio:
    image: minio/minio:RELEASE.2024-01-01T00-00-00Z
    container_name: codeflowx-minio
    restart: unless-stopped
    ports:
      - "9000:9000"  # API
      - "9001:9001"  # Console
    volumes:
      - ./minio_data:/data
    environment:
      - MINIO_ROOT_USER=codeflowx_admin
      - MINIO_ROOT_PASSWORD=${MINIO_ROOT_PASSWORD}
      - MINIO_BROWSER_REDIRECT_URL=http://localhost:9001
    command: server /data --console-address ":9001"
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:9000/minio/health/live"]
      interval: 30s
      timeout: 10s
      retries: 3
    networks:
      - codeflowx-network

networks:
  codeflowx-network:
    external: true
```

**Script inicialización buckets:**

```python
# scripts/init_minio_buckets.py
from minio import Minio
from minio.error import S3Error
import os

def init_minio_buckets():
    """Inicializar buckets MinIO para CodeflowX"""
    
    # MinIO client
    client = Minio(
        "localhost:9000",
        access_key=os.getenv("MINIO_ACCESS_KEY", "codeflowx_admin"),
        secret_key=os.getenv("MINIO_SECRET_KEY"),
        secure=False  # HTTP local
    )
    
    buckets = [
        {
            "name": "technical-docs",
            "description": "Documentos técnicos Anexo IV (Art. 11)"
        },
        {
            "name": "datasets",
            "description": "Datasets entrenamiento (Art. 10)"
        },
        {
            "name": "models",
            "description": "Modelos fine-tuned (safetensors, GGUF)"
        },
        {
            "name": "knowledge-base",
            "description": "Documentos KB RAG (antes chunking)"
        },
        {
            "name": "evaluation-results",
            "description": "Artefactos evaluaciones"
        },
        {
            "name": "backups",
            "description": "Backups PostgreSQL, Qdrant snapshots"
        },
        {
            "name": "client-documents",
            "description": "Documentos específicos clientes"
        }
    ]
    
    for bucket in buckets:
        try:
            # Check si existe
            if not client.bucket_exists(bucket["name"]):
                client.make_bucket(bucket["name"])
                print(f"✅ Bucket '{bucket['name']}' created")
                
                # Set lifecycle policy (optional)
                # client.set_bucket_lifecycle(bucket["name"], lifecycle_config)
            else:
                print(f"⚠️  Bucket '{bucket['name']}' already exists")
                
        except S3Error as e:
            print(f"❌ Error creating bucket '{bucket['name']}': {e}")
    
    print("\n🎉 MinIO buckets initialized!")

if __name__ == "__main__":
    init_minio_buckets()
```

**Verificar:**
- MinIO console accesible (http://localhost:9001)
- Buckets creados
- Upload/download test file funciona

---

### **PROMPT 7: Cliente Java MinIO + Service Layer**

**Objetivo:** Cliente Java para interactuar con MinIO desde backend Spring Boot

**Crear:**

```xml
<!-- pom.xml -->
<dependency>
    <groupId>io.minio</groupId>
    <artifactId>minio</artifactId>
    <version>8.5.7</version>
</dependency>
```

```java
// config/MinIOConfig.java
package com.codeflowx.govern.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinIOConfig {
    
    @Value("${minio.url}")
    private String minioUrl;
    
    @Value("${minio.access.key}")
    private String accessKey;
    
    @Value("${minio.secret.key}")
    private String secretKey;
    
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
            .endpoint(minioUrl)
            .credentials(accessKey, secretKey)
            .build();
    }
}
```

```java
// service/storage/MinIOService.java
package com.codeflowx.govern.service.storage;

import io.minio.*;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;

@Slf4j
@Service
public class MinIOService {
    
    @Autowired
    private MinioClient minioClient;
    
    /**
     * Upload file to MinIO
     */
    public String uploadFile(
        String bucketName,
        String objectName,
        InputStream inputStream,
        long size,
        String contentType,
        Map<String, String> metadata
    ) {
        try {
            // Ensure bucket exists
            boolean found = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucketName).build()
            );
            if (!found) {
                minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(bucketName).build()
                );
            }
            
            // Upload
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputStream, size, -1)
                    .contentType(contentType)
                    .userMetadata(metadata)
                    .build()
            );
            
            log.info("✅ File uploaded to MinIO: {}/{}", bucketName, objectName);
            
            return String.format("%s/%s", bucketName, objectName);
            
        } catch (Exception e) {
            log.error("❌ Error uploading file to MinIO: {}", e.getMessage());
            throw new RuntimeException("Error uploading file", e);
        }
    }
    
    /**
     * Upload MultipartFile
     */
    public String uploadMultipartFile(
        String bucketName,
        String objectName,
        MultipartFile file,
        Map<String, String> metadata
    ) {
        try {
            return uploadFile(
                bucketName,
                objectName,
                file.getInputStream(),
                file.getSize(),
                file.getContentType(),
                metadata
            );
        } catch (Exception e) {
            throw new RuntimeException("Error uploading multipart file", e);
        }
    }
    
    /**
     * Download file from MinIO
     */
    public InputStream downloadFile(String bucketName, String objectName) {
        try {
            return minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
        } catch (Exception e) {
            log.error("❌ Error downloading file from MinIO: {}", e.getMessage());
            throw new RuntimeException("Error downloading file", e);
        }
    }
    
    /**
     * Get file URL (presigned)
     */
    public String getPresignedUrl(
        String bucketName,
        String objectName,
        int expirySeconds
    ) {
        try {
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(expirySeconds)
                    .build()
            );
        } catch (Exception e) {
            log.error("❌ Error getting presigned URL: {}", e.getMessage());
            throw new RuntimeException("Error getting presigned URL", e);
        }
    }
    
    /**
     * Delete file
     */
    public void deleteFile(String bucketName, String objectName) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
            log.info("✅ File deleted from MinIO: {}/{}", bucketName, objectName);
        } catch (Exception e) {
            log.error("❌ Error deleting file from MinIO: {}", e.getMessage());
            throw new RuntimeException("Error deleting file", e);
        }
    }
}
```

**Verificar:**
- MinIO client bean creado
- Upload file funciona
- Download file funciona
- Presigned URL genera correctamente

---

### **PROMPT 8: Integración MinIO con Documentos Técnicos (Anexo IV)**

**Objetivo:** Almacenar documentos técnicos Anexo IV en MinIO

**Modificar entity TechnicalDocumentation:**

```java
// entity/compliance/TechnicalDocumentation.java
@Entity
@Table(name = "TECDOCUMENTATION")
public class TechnicalDocumentation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXTECDOCUMENTATION")
    private Long idxtecdocumentation;
    
    @Column(name = "TECMODEL_ID")
    private Long tecmodelId;
    
    @Column(name = "TECDOCUMENT_TYPE", length = 50)
    private String tecdocumentType;  // ANEXO_IV, INSTRUCTIONS_FOR_USE, etc.
    
    @Column(name = "TECMINIO_BUCKET", length = 100)
    private String tecminioBucket;
    
    @Column(name = "TECMINIO_OBJECT_NAME", length = 500)
    private String tecminioObjectName;
    
    @Column(name = "TECFILE_SIZE_BYTES")
    private Long tecfileSizeBytes;
    
    @Column(name = "TECCONTENT_TYPE", length = 100)
    private String teccontentType;
    
    @Column(name = "TECCREATED_AT")
    private Timestamp teccreatedAt;
    
    // Getters/setters...
}
```

**Service para generar Anexo IV + upload MinIO:**

```java
// service/compliance/TechnicalDocumentationService.java
@Service
public class TechnicalDocumentationService {
    
    @Autowired
    private MinIOService minioService;
    
    @Autowired
    private TechnicalDocumentationRepository tecDocRepository;
    
    /**
     * Generate Anexo IV PDF + upload MinIO
     */
    @Transactional
    public TechnicalDocumentation generateAndUploadAnexoIV(Long modelId) {
        try {
            // 1. Generate PDF (usando lib PDF generation)
            byte[] pdfBytes = generateAnexoIVPDF(modelId);
            
            // 2. Prepare metadata
            Map<String, String> metadata = new HashMap<>();
            metadata.put("model_id", modelId.toString());
            metadata.put("document_type", "ANEXO_IV");
            metadata.put("ai_act_article", "Art. 11");
            metadata.put("generated_at", LocalDateTime.now().toString());
            
            // 3. Upload MinIO
            String objectName = String.format("models/%d/anexo_iv_%d.pdf", 
                                              modelId, System.currentTimeMillis());
            
            String minioPath = minioService.uploadFile(
                "technical-docs",
                objectName,
                new ByteArrayInputStream(pdfBytes),
                pdfBytes.length,
                "application/pdf",
                metadata
            );
            
            // 4. Save metadata PostgreSQL
            TechnicalDocumentation tecDoc = new TechnicalDocumentation();
            tecDoc.setTecmodelId(modelId);
            tecDoc.setTecdocumentType("ANEXO_IV");
            tecDoc.setTecminioBucket("technical-docs");
            tecDoc.setTecminioObjectName(objectName);
            tecDoc.setTecfileSizeBytes((long) pdfBytes.length);
            tecDoc.setTeccontentType("application/pdf");
            tecDoc.setTeccreatedAt(new Timestamp(System.currentTimeMillis()));
            
            tecDocRepository.save(tecDoc);
            
            log.info("✅ Anexo IV generated and uploaded for model {}", modelId);
            
            return tecDoc;
            
        } catch (Exception e) {
            log.error("❌ Error generating Anexo IV for model {}: {}", modelId, e.getMessage());
            throw new RuntimeException("Error generating Anexo IV", e);
        }
    }
    
    private byte[] generateAnexoIVPDF(Long modelId) {
        // TODO: Implement PDF generation
        // (usar lib como iText, Apache PDFBox, etc.)
        return new byte[0];
    }
}
```

**Verificar:**
- Anexo IV PDF generado
- Upload MinIO correcto
- Metadata guardado PostgreSQL
- Download PDF funciona

---

### **PROMPT 9: Pipeline RAG: MinIO → Chunking → Qdrant**

**Objetivo:** Pipeline completo: Upload PDF MinIO → Extract text → Chunk → Embed → Qdrant

**Crear:**

```python
# leka-rag-service/services/document_processing_service.py
from fastapi import UploadFile
from minio import Minio
import PyPDF2
import io
from typing import Dict, Any
import structlog

logger = structlog.get_logger(__name__)

class DocumentProcessingService:
    """Pipeline completo: MinIO → Extract → Chunk → Embed → Qdrant"""
    
    def __init__(self, minio_client: Minio, qdrant_client, rag_service):
        self.minio = minio_client
        self.qdrant = qdrant_client
        self.rag = rag_service
    
    async def process_pdf_document(
        self,
        file: UploadFile,
        document_id: str,
        metadata: Dict[str, Any],
        bucket_name: str = "knowledge-base",
        collection_name: str = "knowledge_base_docs"
    ) -> Dict[str, Any]:
        """
        Pipeline completo PDF:
        1. Upload MinIO
        2. Extract text PDF
        3. Chunk text
        4. Generate embeddings
        5. Insert Qdrant
        """
        
        # 1. Upload MinIO
        object_name = f"docs/{document_id}/{file.filename}"
        
        file_bytes = await file.read()
        self.minio.put_object(
            bucket_name=bucket_name,
            object_name=object_name,
            data=io.BytesIO(file_bytes),
            length=len(file_bytes),
            content_type=file.content_type,
            metadata=metadata
        )
        
        logger.info(f"File uploaded to MinIO: {bucket_name}/{object_name}")
        
        # 2. Extract text PDF
        pdf_text = self._extract_text_from_pdf(io.BytesIO(file_bytes))
        
        logger.info(f"Text extracted from PDF: {len(pdf_text)} characters")
        
        # 3. Chunk + Embed + Insert Qdrant (usa RAG service)
        metadata_with_minio = {
            **metadata,
            "minio_bucket": bucket_name,
            "minio_object_name": object_name,
            "filename": file.filename
        }
        
        qdrant_result = await self.rag.process_document(
            document_id=document_id,
            document_text=pdf_text,
            metadata=metadata_with_minio,
            collection_name=collection_name
        )
        
        return {
            "document_id": document_id,
            "minio_path": f"{bucket_name}/{object_name}",
            "chunks_created": qdrant_result["chunks_created"],
            "text_length": len(pdf_text),
            "collection": collection_name
        }
    
    def _extract_text_from_pdf(self, pdf_bytes: io.BytesIO) -> str:
        """Extract text from PDF usando PyPDF2"""
        
        try:
            pdf_reader = PyPDF2.PdfReader(pdf_bytes)
            text = ""
            
            for page in pdf_reader.pages:
                text += page.extract_text() + "\n\n"
            
            return text.strip()
            
        except Exception as e:
            logger.error(f"Error extracting text from PDF: {e}")
            raise RuntimeError(f"Error extracting PDF text: {e}")
```

**Endpoint FastAPI:**

```python
# leka-rag-service/main.py (añadir)
from services.document_processing_service import DocumentProcessingService

# Initialize
minio_client = Minio(
    "minio:9000",
    access_key=os.getenv("MINIO_ACCESS_KEY"),
    secret_key=os.getenv("MINIO_SECRET_KEY"),
    secure=False
)

doc_processing_service = DocumentProcessingService(
    minio_client, 
    qdrant_client, 
    rag_service
)

@app.post("/api/documents/process-pdf")
async def process_pdf_document_endpoint(
    file: UploadFile,
    document_id: str,
    project_id: int,
    document_type: str
):
    """Pipeline completo: Upload PDF → Extract → Chunk → Embed → Qdrant"""
    
    try:
        metadata = {
            "project_id": project_id,
            "document_type": document_type
        }
        
        result = await doc_processing_service.process_pdf_document(
            file=file,
            document_id=document_id,
            metadata=metadata
        )
        
        return result
        
    except Exception as e:
        logger.error(f"Error processing PDF: {e}")
        raise HTTPException(status_code=500, detail=str(e))
```

**Verificar:**
- Upload PDF → MinIO OK
- Extract text PDF OK
- Chunking + embedding + Qdrant OK
- Pipeline completo end-to-end funciona

---

### **PROMPT 10: Lifecycle Policies MinIO (Archivado Automático)**

**Objetivo:** Configurar lifecycle policies para archivar/delete automático objetos antiguos

**Crear:**

```python
# scripts/configure_minio_lifecycle.py
from minio import Minio
from minio.lifecycleconfig import LifecycleConfig, Rule, Expiration, Transition
from datetime import timedelta

def configure_lifecycle_policies():
    """Configurar lifecycle policies MinIO"""
    
    client = Minio(
        "localhost:9000",
        access_key=os.getenv("MINIO_ACCESS_KEY"),
        secret_key=os.getenv("MINIO_SECRET_KEY"),
        secure=False
    )
    
    # Policy 1: Delete inference logs >12 meses (bucket evaluation-results)
    lifecycle_evaluation = LifecycleConfig(
        [
            Rule(
                rule_id="delete-old-evaluations",
                status="Enabled",
                expiration=Expiration(days=365),  # 12 meses
                rule_filter=None
            )
        ]
    )
    
    client.set_bucket_lifecycle("evaluation-results", lifecycle_evaluation)
    print("✅ Lifecycle policy configured for evaluation-results (delete >12 months)")
    
    # Policy 2: Transition datasets >6 meses a tier frío (si MinIO tiene tiering)
    # (MinIO tiering requiere configuración adicional)
    
    # Policy 3: Delete backups >2 años
    lifecycle_backups = LifecycleConfig(
        [
            Rule(
                rule_id="delete-old-backups",
                status="Enabled",
                expiration=Expiration(days=730),  # 2 años
                rule_filter=None
            )
        ]
    )
    
    client.set_bucket_lifecycle("backups", lifecycle_backups)
    print("✅ Lifecycle policy configured for backups (delete >2 years)")
    
    print("\n🎉 MinIO lifecycle policies configured!")

if __name__ == "__main__":
    configure_lifecycle_policies()
```

**Verificar:**
- Lifecycle policies configuradas
- Objetos antiguos deleted automáticamente (esperar o test con objetos test)

---

## 📦 GRUPO C: OPENSEARCH (LOGS + AUDITORÍA)

### **PROMPT 11: Setup OpenSearch + Índices Básicos**

**Objetivo:** Instalar OpenSearch y crear índices para logs audit, inference, application

**Crear:**

```yaml
# docker/opensearch/docker-compose.yml
version: '3.8'
services:
  opensearch:
    image: opensearchproject/opensearch:2.11.0
    container_name: codeflowx-opensearch
    restart: unless-stopped
    ports:
      - "9200:9200"  # HTTP API
      - "9600:9600"  # Performance Analyzer
    volumes:
      - ./opensearch_data:/usr/share/opensearch/data
    environment:
      - discovery.type=single-node
      - OPENSEARCH_JAVA_OPTS=-Xms2g -Xmx2g
      - DISABLE_SECURITY_PLUGIN=true  # Disable security para desarrollo local
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:9200/_cluster/health"]
      interval: 30s
      timeout: 10s
      retries: 3
    networks:
      - codeflowx-network
  
  opensearch-dashboards:
    image: opensearchproject/opensearch-dashboards:2.11.0
    container_name: codeflowx-opensearch-dashboards
    restart: unless-stopped
    ports:
      - "5601:5601"
    environment:
      - OPENSEARCH_HOSTS=["http://opensearch:9200"]
      - DISABLE_SECURITY_DASHBOARDS_PLUGIN=true
    depends_on:
      - opensearch
    networks:
      - codeflowx-network

networks:
  codeflowx-network:
    external: true
```

**Script inicialización índices:**

```python
# scripts/init_opensearch_indices.py
from opensearchpy import OpenSearch
from datetime import datetime

def init_opensearch_indices():
    """Inicializar índices OpenSearch para CodeflowX"""
    
    client = OpenSearch(
        hosts=["http://localhost:9200"],
        use_ssl=False,
        verify_certs=False
    )
    
    indices = [
        {
            "name": "audit-logs",
            "settings": {
                "number_of_shards": 2,
                "number_of_replicas": 1
            },
            "mappings": {
                "properties": {
                    "timestamp": {"type": "date"},
                    "event_type": {"type": "keyword"},
                    "user_id": {"type": "long"},
                    "model_id": {"type": "long"},
                    "risk_level": {"type": "keyword"},
                    "approval_status": {"type": "keyword"},
                    "approver_id": {"type": "long"},
                    "hash_chain": {"type": "keyword"},
                    "metadata": {"type": "object", "enabled": False}
                }
            }
        },
        {
            "name": "inference-logs",
            "settings": {
                "number_of_shards": 3,
                "number_of_replicas": 1
            },
            "mappings": {
                "properties": {
                    "timestamp": {"type": "date"},
                    "model_id": {"type": "long"},
                    "inference_id": {"type": "keyword"},
                    "latency_ms": {"type": "float"},
                    "tokens_input": {"type": "integer"},
                    "tokens_output": {"type": "integer"},
                    "cost_usd": {"type": "float"},
                    "error": {"type": "text"}
                }
            }
        },
        {
            "name": "application-logs",
            "settings": {
                "number_of_shards": 1,
                "number_of_replicas": 1
            },
            "mappings": {
                "properties": {
                    "timestamp": {"type": "date"},
                    "level": {"type": "keyword"},
                    "logger": {"type": "keyword"},
                    "message": {"type": "text"},
                    "exception": {"type": "text"}
                }
            }
        }
    ]
    
    for index in indices:
        try:
            if not client.indices.exists(index=index["name"]):
                client.indices.create(
                    index=index["name"],
                    body={
                        "settings": index["settings"],
                        "mappings": index["mappings"]
                    }
                )
                print(f"✅ Index '{index['name']}' created")
            else:
                print(f"⚠️  Index '{index['name']}' already exists")
        except Exception as e:
            print(f"❌ Error creating index '{index['name']}': {e}")
    
    print("\n🎉 OpenSearch indices initialized!")

if __name__ == "__main__":
    init_opensearch_indices()
```

**Verificar:**
- OpenSearch accesible (curl http://localhost:9200)
- OpenSearch Dashboards accesible (http://localhost:5601)
- Índices creados

---

### **PROMPT 12: Cliente Java OpenSearch + Service Layer**

**Objetivo:** Cliente Java para enviar logs a OpenSearch desde backend Spring Boot

**Crear:**

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.opensearch.client</groupId>
    <artifactId>opensearch-rest-high-level-client</artifactId>
    <version>2.11.0</version>
</dependency>
```

```java
// config/OpenSearchConfig.java
package com.codeflowx.govern.config;

import org.apache.http.HttpHost;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenSearchConfig {
    
    @Value("${opensearch.host}")
    private String opensearchHost;
    
    @Value("${opensearch.port}")
    private int opensearchPort;
    
    @Bean
    public RestHighLevelClient openSearchClient() {
        return new RestHighLevelClient(
            RestClient.builder(
                new HttpHost(opensearchHost, opensearchPort, "http")
            )
        );
    }
}
```

```java
// service/logging/OpenSearchLogService.java
package com.codeflowx.govern.service.logging;

import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class OpenSearchLogService {
    
    @Autowired
    private RestHighLevelClient openSearchClient;
    
    /**
     * Log audit event (Art. 19 immutable)
     */
    public void logAuditEvent(
        String eventType,
        Long userId,
        Long modelId,
        String riskLevel,
        String approvalStatus,
        Long approverId,
        String hashChain,
        Map<String, Object> metadata
    ) {
        try {
            Map<String, Object> document = new HashMap<>();
            document.put("timestamp", LocalDateTime.now().toString());
            document.put("event_type", eventType);
            document.put("user_id", userId);
            document.put("model_id", modelId);
            document.put("risk_level", riskLevel);
            document.put("approval_status", approvalStatus);
            document.put("approver_id", approverId);
            document.put("hash_chain", hashChain);
            document.put("metadata", metadata);
            
            String indexName = "audit-logs-" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            
            IndexRequest request = new IndexRequest(indexName)
                .source(document);
            
            IndexResponse response = openSearchClient.index(request, RequestOptions.DEFAULT);
            
            log.debug("✅ Audit event logged to OpenSearch: {}", response.getId());
            
        } catch (Exception e) {
            log.error("❌ Error logging audit event to OpenSearch: {}", e.getMessage());
            // No throw exception (no queremos que falle transacción por log)
        }
    }
    
    /**
     * Log inference
     */
    public void logInference(
        Long modelId,
        String inferenceId,
        float latencyMs,
        int tokensInput,
        int tokensOutput,
        float costUsd,
        String error
    ) {
        try {
            Map<String, Object> document = new HashMap<>();
            document.put("timestamp", LocalDateTime.now().toString());
            document.put("model_id", modelId);
            document.put("inference_id", inferenceId);
            document.put("latency_ms", latencyMs);
            document.put("tokens_input", tokensInput);
            document.put("tokens_output", tokensOutput);
            document.put("cost_usd", costUsd);
            if (error != null) {
                document.put("error", error);
            }
            
            String indexName = "inference-logs-" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            
            IndexRequest request = new IndexRequest(indexName)
                .source(document);
            
            openSearchClient.index(request, RequestOptions.DEFAULT);
            
        } catch (Exception e) {
            log.error("❌ Error logging inference to OpenSearch: {}", e.getMessage());
        }
    }
}
```

**Integrar con BPMN delegates:**

```java
// workflow/delegates/LogAuditEventDelegate.java
@Component("logAuditEventDelegate")
public class LogAuditEventDelegate implements JavaDelegate {
    
    @Autowired
    private OpenSearchLogService openSearchLogService;
    
    @Override
    public void execute(DelegateExecution execution) {
        // Extract variables
        String eventType = (String) execution.getVariable("eventType");
        Long userId = (Long) execution.getVariable("userId");
        Long modelId = (Long) execution.getVariable("modelId");
        // ... etc
        
        // Log to OpenSearch (inmutable Art. 19)
        openSearchLogService.logAuditEvent(
            eventType, userId, modelId, 
            riskLevel, approvalStatus, approverId,
            hashChain, metadata
        );
    }
}
```

**Verificar:**
- OpenSearch client bean creado
- Log audit event funciona
- Log inference funciona
- Logs aparecen en OpenSearch Dashboards

---

### **PROMPT 13: Microservicio Python OpenSearch (Analytics + Dashboards)**

**Objetivo:** Microservicio FastAPI para queries analytics OpenSearch (dashboards compliance)

**Crear:**

```python
# leka-analytics-service/main.py
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from opensearchpy import OpenSearch
from typing import List, Dict, Any, Optional
from datetime import datetime, timedelta
import structlog

logger = structlog.get_logger(__name__)

app = FastAPI(title="CodeflowX Analytics Service", version="1.0.0")

# OpenSearch client
opensearch_client = OpenSearch(
    hosts=["http://opensearch:9200"],
    use_ssl=False,
    verify_certs=False
)

class ComplianceMetricsRequest(BaseModel):
    start_date: str
    end_date: str
    model_id: Optional[int] = None
    project_id: Optional[int] = None

class InferenceAnalyticsRequest(BaseModel):
    model_id: int
    days: int = 7

# Service
class AnalyticsService:
    def __init__(self, opensearch_client):
        self.opensearch = opensearch_client
    
    def get_compliance_metrics(
        self, 
        start_date: str, 
        end_date: str,
        model_id: Optional[int] = None
    ) -> Dict[str, Any]:
        """Get compliance metrics from audit logs"""
        
        # Build query
        must_clauses = [
            {"range": {"timestamp": {"gte": start_date, "lte": end_date}}}
        ]
        
        if model_id:
            must_clauses.append({"term": {"model_id": model_id}})
        
        query = {
            "size": 0,  # No docs, solo aggregations
            "query": {
                "bool": {"must": must_clauses}
            },
            "aggs": {
                "events_by_type": {
                    "terms": {"field": "event_type", "size": 20}
                },
                "approvals_by_status": {
                    "terms": {"field": "approval_status", "size": 10}
                },
                "models_by_risk": {
                    "terms": {"field": "risk_level", "size": 5}
                },
                "events_over_time": {
                    "date_histogram": {
                        "field": "timestamp",
                        "calendar_interval": "day"
                    }
                }
            }
        }
        
        # Execute query
        response = self.opensearch.search(
            index="audit-logs-*",
            body=query
        )
        
        # Parse aggregations
        aggs = response["aggregations"]
        
        return {
            "total_events": response["hits"]["total"]["value"],
            "events_by_type": [
                {"type": b["key"], "count": b["doc_count"]}
                for b in aggs["events_by_type"]["buckets"]
            ],
            "approvals_by_status": [
                {"status": b["key"], "count": b["doc_count"]}
                for b in aggs["approvals_by_status"]["buckets"]
            ],
            "models_by_risk": [
                {"risk_level": b["key"], "count": b["doc_count"]}
                for b in aggs["models_by_risk"]["buckets"]
            ],
            "events_over_time": [
                {
                    "date": b["key_as_string"],
                    "count": b["doc_count"]
                }
                for b in aggs["events_over_time"]["buckets"]
            ]
        }
    
    def get_inference_analytics(
        self, 
        model_id: int, 
        days: int = 7
    ) -> Dict[str, Any]:
        """Get inference analytics (latency, cost, errors)"""
        
        start_date = (datetime.now() - timedelta(days=days)).isoformat()
        
        query = {
            "size": 0,
            "query": {
                "bool": {
                    "must": [
                        {"term": {"model_id": model_id}},
                        {"range": {"timestamp": {"gte": start_date}}}
                    ]
                }
            },
            "aggs": {
                "avg_latency": {"avg": {"field": "latency_ms"}},
                "p95_latency": {"percentiles": {"field": "latency_ms", "percents": [95]}},
                "total_cost": {"sum": {"field": "cost_usd"}},
                "total_tokens": {
                    "sum": {"script": "doc['tokens_input'].value + doc['tokens_output'].value"}
                },
                "error_rate": {
                    "filter": {"exists": {"field": "error"}}
                },
                "inferences_over_time": {
                    "date_histogram": {
                        "field": "timestamp",
                        "calendar_interval": "hour"
                    }
                }
            }
        }
        
        response = self.opensearch.search(
            index="inference-logs-*",
            body=query
        )
        
        aggs = response["aggregations"]
        total = response["hits"]["total"]["value"]
        
        return {
            "model_id": model_id,
            "days": days,
            "total_inferences": total,
            "avg_latency_ms": aggs["avg_latency"]["value"],
            "p95_latency_ms": aggs["p95_latency"]["values"]["95.0"],
            "total_cost_usd": aggs["total_cost"]["value"],
            "total_tokens": aggs["total_tokens"]["value"],
            "error_count": aggs["error_rate"]["doc_count"],
            "error_rate_pct": (aggs["error_rate"]["doc_count"] / total * 100) if total > 0 else 0,
            "inferences_over_time": [
                {
                    "timestamp": b["key_as_string"],
                    "count": b["doc_count"]
                }
                for b in aggs["inferences_over_time"]["buckets"]
            ]
        }

# Initialize service
analytics_service = AnalyticsService(opensearch_client)

# Endpoints
@app.post("/api/analytics/compliance-metrics")
async def get_compliance_metrics_endpoint(request: ComplianceMetricsRequest):
    """Get compliance metrics from audit logs"""
    try:
        result = analytics_service.get_compliance_metrics(
            start_date=request.start_date,
            end_date=request.end_date,
            model_id=request.model_id
        )
        return result
    except Exception as e:
        logger.error(f"Error getting compliance metrics: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/analytics/inference-analytics")
async def get_inference_analytics_endpoint(request: InferenceAnalyticsRequest):
    """Get inference analytics for model"""
    try:
        result = analytics_service.get_inference_analytics(
            model_id=request.model_id,
            days=request.days
        )
        return result
    except Exception as e:
        logger.error(f"Error getting inference analytics: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/health")
async def health_check():
    return {"status": "healthy", "service": "analytics-service"}
```

**Verificar:**
- Analytics service funciona
- Compliance metrics query OK
- Inference analytics query OK
- Aggregations correctas

---

### **PROMPT 14: Index Lifecycle Management (ILM) OpenSearch**

**Objetivo:** Configurar ILM para rotation/deletion automática índices logs

**Crear:**

```python
# scripts/configure_opensearch_ilm.py
from opensearchpy import OpenSearch

def configure_ilm_policies():
    """Configurar Index Lifecycle Management OpenSearch"""
    
    client = OpenSearch(
        hosts=["http://localhost:9200"],
        use_ssl=False,
        verify_certs=False
    )
    
    # Policy 1: audit-logs (retention 10 años)
    audit_logs_policy = {
        "policy": {
            "description": "Audit logs retention policy (10 years)",
            "default_state": "hot",
            "states": [
                {
                    "name": "hot",
                    "actions": [],
                    "transitions": [
                        {
                            "state_name": "warm",
                            "conditions": {"min_index_age": "30d"}
                        }
                    ]
                },
                {
                    "name": "warm",
                    "actions": [
                        {"read_only": {}}
                    ],
                    "transitions": [
                        {
                            "state_name": "cold",
                            "conditions": {"min_index_age": "365d"}
                        }
                    ]
                },
                {
                    "name": "cold",
                    "actions": [],
                    "transitions": [
                        {
                            "state_name": "delete",
                            "conditions": {"min_index_age": "3650d"}  # 10 años
                        }
                    ]
                },
                {
                    "name": "delete",
                    "actions": [
                        {"delete": {}}
                    ]
                }
            ]
        }
    }
    
    client.transport.perform_request(
        "PUT",
        "/_plugins/_ism/policies/audit-logs-policy",
        body=audit_logs_policy
    )
    print("✅ ILM policy configured for audit-logs (10 years retention)")
    
    # Policy 2: inference-logs (retention 12 meses)
    inference_logs_policy = {
        "policy": {
            "description": "Inference logs retention policy (12 months)",
            "default_state": "hot",
            "states": [
                {
                    "name": "hot",
                    "actions": [],
                    "transitions": [
                        {
                            "state_name": "delete",
                            "conditions": {"min_index_age": "365d"}  # 12 meses
                        }
                    ]
                },
                {
                    "name": "delete",
                    "actions": [
                        {"delete": {}}
                    ]
                }
            ]
        }
    }
    
    client.transport.perform_request(
        "PUT",
        "/_plugins/_ism/policies/inference-logs-policy",
        body=inference_logs_policy
    )
    print("✅ ILM policy configured for inference-logs (12 months retention)")
    
    # Apply policies a índices
    client.transport.perform_request(
        "POST",
        "/audit-logs-*/_settings",
        body={"index.plugins.index_state_management.policy_id": "audit-logs-policy"}
    )
    
    client.transport.perform_request(
        "POST",
        "/inference-logs-*/_settings",
        body={"index.plugins.index_state_management.policy_id": "inference-logs-policy"}
    )
    
    print("\n🎉 OpenSearch ILM policies configured!")

if __name__ == "__main__":
    configure_ilm_policies()
```

**Verificar:**
- ILM policies creadas
- Policies aplicadas a índices
- Retention automática funciona (test con índices test)

---

### **PROMPT 15: OpenSearch Dashboards Compliance (Kibana-like)**

**Objetivo:** Crear dashboards pre-configurados compliance en OpenSearch Dashboards

**Crear visualizaciones (JSON export OpenSearch Dashboards):**

```json
// dashboards/compliance_overview.ndjson
{
  "type": "dashboard",
  "id": "compliance-overview",
  "attributes": {
    "title": "CodeflowX Compliance Overview",
    "description": "Dashboard compliance EU AI Act + GDPR",
    "panelsJSON": "[...]",
    "visualizations": [
      {
        "title": "Events by Type",
        "type": "pie",
        "index": "audit-logs-*",
        "aggregation": "terms:event_type"
      },
      {
        "title": "Approvals by Status",
        "type": "bar",
        "index": "audit-logs-*",
        "aggregation": "terms:approval_status"
      },
      {
        "title": "Models by Risk Level",
        "type": "pie",
        "index": "audit-logs-*",
        "aggregation": "terms:risk_level"
      },
      {
        "title": "Events Over Time",
        "type": "line",
        "index": "audit-logs-*",
        "aggregation": "date_histogram:timestamp"
      },
      {
        "title": "Inference Latency P95",
        "type": "line",
        "index": "inference-logs-*",
        "aggregation": "percentiles:latency_ms:95"
      },
      {
        "title": "Total Cost",
        "type": "metric",
        "index": "inference-logs-*",
        "aggregation": "sum:cost_usd"
      }
    ]
  }
}
```

**Script import dashboards:**

```python
# scripts/import_opensearch_dashboards.py
from opensearchpy import OpenSearch
import json

def import_dashboards():
    """Import pre-configured dashboards to OpenSearch"""
    
    client = OpenSearch(
        hosts=["http://localhost:9200"],
        use_ssl=False,
        verify_certs=False
    )
    
    # Load dashboard JSON
    with open("dashboards/compliance_overview.ndjson", "r") as f:
        dashboard_json = json.load(f)
    
    # Import dashboard
    client.transport.perform_request(
        "POST",
        "/.kibana/_doc/dashboard:compliance-overview",
        body=dashboard_json
    )
    
    print("✅ Dashboard 'Compliance Overview' imported")
    print("   Access: http://localhost:5601/app/dashboards#/view/compliance-overview")
    
    print("\n🎉 OpenSearch Dashboards configured!")

if __name__ == "__main__":
    import_dashboards()
```

**Verificar:**
- Dashboard importado
- Visualizaciones muestran datos
- Accesible desde OpenSearch Dashboards UI

---

## ✅ RESUMEN PROMPTS_11

**Total prompts:** 15
- **Qdrant (5):** Setup, cliente Java, RAG microservice, integración prompts, reranking
- **MinIO (5):** Setup, cliente Java, docs técnicos, pipeline RAG, lifecycle
- **OpenSearch (5):** Setup, cliente Java, analytics microservice, ILM, dashboards

**Arquitectura resultante:**
```
PostgreSQL/TimescaleDB  (Metadata relacional + time-series)
    +
Qdrant  (5.75M embeddings + RAG hybrid search + reranking)
    +
MinIO  (4 TB documentos + datasets + modelos + 7 buckets)
    +
OpenSearch  (650 GB logs comprimidos + audit immutable + dashboards)
```

**Beneficios:**
- ✅ RAG potente (hybrid search + reranking = 30-50% mejor relevancia)
- ✅ Escalabilidad (cada componente optimizado su función)
- ✅ Compliance robusto (logs inmutables OpenSearch Art. 19)
- ✅ Multi-tenant (collections Qdrant, buckets MinIO, índices OpenSearch)
- ✅ Lifecycle automático (MinIO + OpenSearch ILM)

---

**Última actualización:** 5 Noviembre 2025  
**Prioridad:** 🟡 ALTA  
**Estimación:** 10-15 días implementación completa (con 3-4 chats paralelos)  
**Siguiente:** Ejecutar prompts 1-15 en orden
