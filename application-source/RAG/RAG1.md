Te ayudo a implementar un sistema RAG (Retrieval Augmented Generation) con Spring Boot y PGVector. Este sistema permitirá indexar documentos y realizar búsquedas semánticas para enriquecer las respuestas del LLM.

Primero, las dependencias Maven necesarias (pom.xml):

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.1</version>
    </parent>

    <groupId>com.suinsit.leka</groupId>
    <artifactId>rag-service</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <java.version>17</java.version>
        <spring-cloud.version>2023.0.0</spring-cloud.version>
        <openai.version>0.12.0</openai.version>
        <pgvector.version>0.1.3</pgvector.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>com.pgvector</groupId>
            <artifactId>pgvector</artifactId>
            <version>${pgvector.version}</version>
        </dependency>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
        </dependency>
        <dependency>
            <groupId>com.theokanning.openai-gpt3-java</groupId>
            <artifactId>service</artifactId>
            <version>${openai.version}</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```

Configuración de la base de datos (application.yml):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/leka_rag
    username: postgres
    password: secret
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

openai:
  api-key: ${OPENAI_API_KEY}
  embedding-model: text-embedding-ada-002
  completion-model: gpt-4-turbo-preview
```

Entidad para almacenar documentos y embeddings:

```java
package com.suinsit.leka.rag.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Data
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "embedding", columnDefinition = "vector(1536)")
    private float[] embedding;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

Repositorio para búsquedas vectoriales:

```java
package com.suinsit.leka.rag.repository;

import com.suinsit.leka.rag.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    @Query(value = "SELECT d.*, (d.embedding <=> :queryEmbedding) as distance " +
            "FROM documents d " +
            "ORDER BY distance ASC " +
            "LIMIT :limit", nativeQuery = true)
    List<Document> findSimilarDocuments(@Param("queryEmbedding") float[] queryEmbedding, 
                                      @Param("limit") int limit);
}
```

Servicio para gestionar RAG:

```java
package com.suinsit.leka.rag.service;

import com.suinsit.leka.rag.model.Document;
import com.suinsit.leka.rag.repository.DocumentRepository;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.embedding.EmbeddingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RagService {
    private final DocumentRepository documentRepository;
    private final OpenAiService openAiService;

    @Value("${openai.embedding-model}")
    private String embeddingModel;

    public Mono<Document> indexDocument(String title, String content) {
        return Mono.fromCallable(() -> {
            // Generar embedding para el contenido
            var embeddingRequest = EmbeddingRequest.builder()
                    .model(embeddingModel)
                    .input(List.of(content))
                    .build();
            
            var embedding = openAiService.createEmbeddings(embeddingRequest)
                    .getData().get(0).getEmbedding()
                    .stream()
                    .map(Double::floatValue)
                    .toArray(float[]::new);

            // Crear y guardar documento
            var document = new Document();
            document.setTitle(title);
            document.setContent(content);
            document.setEmbedding(embedding);
            
            return documentRepository.save(document);
        }).subscribeOn(reactor.core.scheduler.Schedulers.boundedElastic());
    }

    public Mono<List<Document>> searchSimilarDocuments(String query, int limit) {
        return Mono.fromCallable(() -> {
            // Generar embedding para la consulta
            var embeddingRequest = EmbeddingRequest.builder()
                    .model(embeddingModel)
                    .input(List.of(query))
                    .build();
            
            var queryEmbedding = openAiService.createEmbeddings(embeddingRequest)
                    .getData().get(0).getEmbedding()
                    .stream()
                    .map(Double::floatValue)
                    .toArray(float[]::new);

            // Buscar documentos similares
            return documentRepository.findSimilarDocuments(queryEmbedding, limit);
        }).subscribeOn(reactor.core.scheduler.Schedulers.boundedElastic());
    }
}
```

Controlador REST:

```java
package com.suinsit.leka.rag.controller;

import com.suinsit.leka.rag.model.Document;
import com.suinsit.leka.rag.service.RagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/rag")
@RequiredArgsConstructor
public class RagController {
    private final RagService ragService;

    @PostMapping("/documents")
    public Mono<Document> indexDocument(@RequestParam String title, 
                                      @RequestParam String content) {
        return ragService.indexDocument(title, content);
    }

    @GetMapping("/search")
    public Mono<List<Document>> searchDocuments(@RequestParam String query, 
                                              @RequestParam(defaultValue = "5") int limit) {
        return ragService.searchSimilarDocuments(query, limit);
    }
}
```

Script SQL para crear la extensión pgvector:

```sql
CREATE EXTENSION IF NOT EXISTS vector;

-- Crear índice para búsqueda por similitud
CREATE INDEX ON documents USING ivfflat (embedding vector_cosine_ops) WITH (lists = 100);
```

Este código implementa:
1. Indexación de documentos con generación de embeddings usando OpenAI
2. Búsqueda semántica usando PGVector
3. API REST reactiva con WebFlux
4. Persistencia con JPA/Hibernate

Para usar el servicio:

1. Indexar un documento:
```bash
curl -X POST "http://localhost:8080/api/rag/documents?title=Example&content=This is a sample document"
```

2. Buscar documentos similares:
```bash
curl "http://localhost:8080/api/rag/search?query=sample document&limit=5"
```

Consideraciones adicionales:
- Implementar cache con Redis para embeddings frecuentes
- Añadir autenticación y autorización
- Implementar rate limiting
- Añadir métricas Prometheus
- Configurar trazabilidad con Jaeger
- Implementar circuit breakers para llamadas a OpenAI

Este es un punto de partida que puedes expandir según tus necesidades específicas.
