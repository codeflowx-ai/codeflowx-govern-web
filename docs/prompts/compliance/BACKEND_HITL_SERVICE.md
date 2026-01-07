# 🏗️ MICROSERVICIO BACKEND - HITL SUPERVISION SERVICE

**Módulo:** Compliance - HITL Supervision (Art. 14 EU AI Act)
**Nombre del Microservicio:** `codeflowx-governance-hitl-service`
**Puerto:** `8091` (configurable)
**Tecnología:** Spring Boot WebFlux (Reactivo)
**Fecha:** Diciembre 2025

---

## 📋 RESUMEN

Este documento describe la implementación del microservicio backend para el módulo de HITL Supervision, siguiendo la arquitectura reactiva establecida y la plantilla de referencia `codeflowx-governance-classification-service`.

---

## 🏗️ ESTRUCTURA DEL MICROSERVICIO

```
codeflowx-governance-hitl-service/
├── pom.xml                                    # Dependencias: WebFlux, Business, Repository, DTOs
├── src/main/
│   ├── java/com/codeflowx/govern/hitl/
│   │   ├── HitlServiceApplication.java        # App principal con @SpringBootApplication
│   │   ├── controller/
│   │   │   └── HitlController.java            # REST Controller reactivo (Mono<ResponseEntity<T>>)
│   │   ├── service/
│   │   │   └── HitlAIService.java             # Servicios adicionales (IA, integraciones) - opcional
│   │   ├── config/
│   │   │   └── WebClientConfig.java           # Configuración de WebClient - opcional
│   │   └── exception/
│   │       └── GlobalExceptionHandler.java    # Manejo global de excepciones
│   └── resources/
│       └── application.yml                    # Configuración (WebFlux, JPA, Actuator)
└── README.md                                  # Documentación del microservicio
```

---

## 📦 DEPENDENCIAS (pom.xml)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.codeflowx</groupId>
        <artifactId>nocode.service</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>codeflowx-governance-hitl-service</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>CodeflowX Governance HITL Service</name>
    <description>Microservicio reactivo para HITL Supervision (Art. 14 EU AI Act)</description>

    <dependencies>
        <!-- WebFlux (NO spring-boot-starter-web) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>

        <!-- JPA -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <!-- Servicios de Negocio -->
        <dependency>
            <groupId>com.codeflowx.govern</groupId>
            <artifactId>codeflowx.govern.business</artifactId>
        </dependency>

        <!-- Repositorios -->
        <dependency>
            <groupId>com.codeflowx.govern</groupId>
            <artifactId>codeflowx.govern.repository</artifactId>
        </dependency>

        <!-- DTOs -->
        <dependency>
            <groupId>com.codeflowx.govern</groupId>
            <artifactId>codeflowx.govern.nocode.dtos</artifactId>
        </dependency>

        <!-- Entidades -->
        <dependency>
            <groupId>com.codeflowx.govern</groupId>
            <artifactId>nocode.service.entitys</artifactId>
        </dependency>

        <!-- OpenAPI (WebFlux) -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webflux-ui</artifactId>
        </dependency>

        <!-- Actuator -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- PostgreSQL (runtime) -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## 🚀 APPLICATION PRINCIPAL

**Archivo:** `src/main/java/com/codeflowx/govern/hitl/HitlServiceApplication.java`

```java
package com.codeflowx.govern.hitl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
    "com.codeflowx.govern.hitl",
    "com.codeflowx.govern.business",
    "com.codeflowx.govern.repository"
})
@EnableJpaRepositories(basePackages = "com.codeflowx.govern.repository")
@EntityScan(basePackages = "com.codeflowx.govern.entity")
public class HitlServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(HitlServiceApplication.class, args);
    }
}
```

---

## 🎮 CONTROLLER REACTIVO

**Archivo:** `src/main/java/com/codeflowx/govern/hitl/controller/HitlController.java`

```java
package com.codeflowx.govern.hitl.controller;

import com.codeflowx.govern.business.compliance.HitlSupervisionService;
import com.codeflowx.govern.nocode.dtos.compliance.HitlDashboardDto;
import com.codeflowx.govern.nocode.dtos.compliance.HitlInterventionDto;
import com.codeflowx.govern.nocode.dtos.compliance.HitlDecisionDto;
import com.codeflowx.govern.nocode.dtos.compliance.HitlDecisionRequest;
import com.codeflowx.govern.nocode.dtos.compliance.HitlSupervisionConfigDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/hitl")
@RequiredArgsConstructor
@Slf4j
public class HitlController {

    private final HitlSupervisionService businessService;

    /**
     * GET /api/v1/hitl/dashboard
     * Obtiene los datos del dashboard de HITL Supervision
     */
    @GetMapping("/dashboard")
    public Mono<ResponseEntity<HitlDashboardDto>> getDashboard(
            @RequestParam(required = false) Long projectId) {
        return Mono.fromCallable(() -> businessService.getDashboard(projectId))
            .subscribeOn(Schedulers.boundedElastic())
            .map(ResponseEntity::ok)
            .onErrorResume(error -> {
                log.error("Error retrieving HITL dashboard", error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    /**
     * GET /api/v1/hitl/interventions
     * Obtiene intervenciones HITL con filtros opcionales
     */
    @GetMapping("/interventions")
    public Mono<ResponseEntity<List<HitlInterventionDto>>> getInterventions(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type) {
        return Mono.fromCallable(() -> businessService.getInterventions(projectId, status, type))
            .subscribeOn(Schedulers.boundedElastic())
            .map(interventions -> interventions.stream()
                .map(this::toInterventionDto)
                .collect(Collectors.toList()))
            .map(ResponseEntity::ok)
            .onErrorResume(error -> {
                log.error("Error retrieving HITL interventions", error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    /**
     * POST /api/v1/hitl/interventions
     * Registra una decisión humana para una intervención HITL
     */
    @PostMapping("/interventions")
    public Mono<ResponseEntity<HitlDecisionDto>> recordDecision(
            @RequestBody HitlDecisionRequest request) {
        return Mono.fromCallable(() -> businessService.recordDecision(request))
            .subscribeOn(Schedulers.boundedElastic())
            .map(this::toDecisionDto)
            .map(ResponseEntity::ok)
            .onErrorResume(error -> {
                log.error("Error recording HITL decision", error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    /**
     * GET /api/v1/hitl/config
     * Obtiene la configuración de supervisión HITL
     */
    @GetMapping("/config")
    public Mono<ResponseEntity<List<HitlSupervisionConfigDto>>> getConfig(
            @RequestParam(required = false) Long projectId) {
        return Mono.fromCallable(() -> businessService.getSupervisionConfig(projectId))
            .subscribeOn(Schedulers.boundedElastic())
            .map(configs -> configs.stream()
                .map(this::toConfigDto)
                .collect(Collectors.toList()))
            .map(ResponseEntity::ok)
            .onErrorResume(error -> {
                log.error("Error retrieving HITL configuration", error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    /**
     * POST /api/v1/hitl/config
     * Actualiza la configuración de supervisión HITL
     */
    @PostMapping("/config")
    public Mono<ResponseEntity<HitlSupervisionConfigDto>> updateConfig(
            @RequestBody HitlSupervisionConfigDto configDto) {
        return Mono.fromCallable(() -> businessService.updateSupervisionConfig(configDto))
            .subscribeOn(Schedulers.boundedElastic())
            .map(this::toConfigDto)
            .map(ResponseEntity::ok)
            .onErrorResume(error -> {
                log.error("Error updating HITL configuration", error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    // Métodos de conversión Entity -> DTO
    private HitlInterventionDto toInterventionDto(Object entity) {
        // Implementar conversión
        return null;
    }

    private HitlDecisionDto toDecisionDto(Object entity) {
        // Implementar conversión
        return null;
    }

    private HitlSupervisionConfigDto toConfigDto(Object entity) {
        // Implementar conversión
        return null;
    }
}
```

---

## ⚙️ CONFIGURACIÓN (application.yml)

**Archivo:** `src/main/resources/application.yml`

```yaml
spring:
  application:
    name: governance-hitl-service
  webflux:
    base-path: /
  jpa:
    hibernate:
      ddl-auto: none
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
  datasource:
    url: ${DATABASE_URL:jdbc:postgresql://localhost:5432/codeflowx}
    username: ${DATABASE_USERNAME:codeflowx}
    password: ${DATABASE_PASSWORD:codeflowx}
    driver-class-name: org.postgresql.Driver

server:
  port: ${SERVER_PORT:8091}

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always

springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
```

---

## 📊 SERVICIOS DE NEGOCIO REQUERIDOS

**Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/`

### HitlSupervisionService

```java
package com.codeflowx.govern.business.compliance;

import com.codeflowx.govern.entity.compliance.HitlSupervision;
import com.codeflowx.govern.entity.compliance.HitlDecision;
import com.codeflowx.govern.repository.compliance.HitlSupervisionRepository;
import com.codeflowx.govern.repository.compliance.HitlDecisionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HitlSupervisionService {

    private final HitlSupervisionRepository supervisionRepository;
    private final HitlDecisionRepository decisionRepository;

    public HitlDashboardDto getDashboard(Long projectId) {
        log.info("Retrieving HITL dashboard for projectId: {}", projectId);
        // Implementar lógica de negocio
        return null;
    }

    public List<HitlIntervention> getInterventions(Long projectId, String status, String type) {
        log.info("Retrieving HITL interventions - projectId: {}, status: {}, type: {}",
            projectId, status, type);
        // Implementar lógica de negocio
        return null;
    }

    public HitlDecision recordDecision(HitlDecisionRequest request) {
        log.info("Recording HITL decision for interventionId: {}", request.getInterventionId());
        // Implementar lógica de negocio
        return null;
    }

    public List<HitlSupervision> getSupervisionConfig(Long projectId) {
        log.info("Retrieving HITL supervision config for projectId: {}", projectId);
        // Implementar lógica de negocio
        return null;
    }

    public HitlSupervision updateSupervisionConfig(HitlSupervisionConfigDto configDto) {
        log.info("Updating HITL supervision config for type: {}", configDto.getType());
        // Implementar lógica de negocio
        return null;
    }
}
```

---

## 🗄️ REPOSITORIOS REQUERIDOS

**Ubicación:** `codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/compliance/`

### HitlSupervisionRepository

```java
package com.codeflowx.govern.repository.compliance;

import com.codeflowx.govern.entity.compliance.HitlSupervision;
import com.codeflowx.govern.repository.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HitlSupervisionRepository extends GenericRepository<HitlSupervision, Long> {

    @Query("SELECT h FROM HitlSupervision h WHERE h.idxproject = :projectId")
    List<HitlSupervision> findByProjectId(@Param("projectId") Long projectId);
}
```

### HitlDecisionRepository

```java
package com.codeflowx.govern.repository.compliance;

import com.codeflowx.govern.entity.compliance.HitlDecision;
import com.codeflowx.govern.repository.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HitlDecisionRepository extends GenericRepository<HitlDecision, Long> {

    @Query("SELECT d FROM HitlDecision d WHERE d.idxhitlsupervision = :supervisionId")
    List<HitlDecision> findBySupervisionId(@Param("supervisionId") Long supervisionId);

    @Query("SELECT d FROM HitlDecision d WHERE d.hitlentitytype = :entityType")
    List<HitlDecision> findByEntityType(@Param("entityType") String entityType);
}
```

---

## 📝 DTOs REQUERIDOS

**Ubicación:** `codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/`

### HitlDashboardDto

```java
package com.codeflowx.govern.nocode.dtos.compliance;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class HitlDashboardDto {
    private HitlMetricsDto metrics;
    private List<HitlInterventionDto> pendingInterventions;
    private List<HitlDecisionDto> recentDecisions;
    private List<HitlSupervisionConfigDto> supervisionConfig;
}
```

### HitlMetricsDto

```java
package com.codeflowx.govern.nocode.dtos.compliance;

import lombok.Data;

@Data
public class HitlMetricsDto {
    private Double averageResponseTime;
    private Double approvalRate;
    private Double slaCompliance;
    private Integer pendingInterventions;
    private Integer totalInterventions;
    private Map<String, Integer> interventionsByType;
    private Map<String, Integer> interventionsByStatus;
}
```

### HitlInterventionDto

```java
package com.codeflowx.govern.nocode.dtos.compliance;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HitlInterventionDto {
    private Long id;
    private String type;
    private String entityType;
    private Long entityId;
    private String entityName;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime slaDeadline;
    private Integer slaHours;
    private Double timeRemaining;
    private String urgency;
}
```

### HitlDecisionDto

```java
package com.codeflowx.govern.nocode.dtos.compliance;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HitlDecisionDto {
    private Long id;
    private String type;
    private String entityType;
    private Long entityId;
    private String entityName;
    private String decision;
    private String decisionReason;
    private Double responseTime;
    private LocalDateTime decisionDate;
    private String userId;
}
```

### HitlDecisionRequest

```java
package com.codeflowx.govern.nocode.dtos.compliance;

import lombok.Data;

@Data
public class HitlDecisionRequest {
    private Long interventionId;
    private String decision;
    private String reason;
    private String userId;
}
```

### HitlSupervisionConfigDto

```java
package com.codeflowx.govern.nocode.dtos.compliance;

import lombok.Data;
import java.util.List;

@Data
public class HitlSupervisionConfigDto {
    private String type;
    private Boolean enabled;
    private Integer slaHours;
    private List<String> requiredRoles;
    private Boolean autoEscalation;
    private Integer escalationHours;
}
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### Estructura del Microservicio
- [ ] Crear directorio `codeflowx-governance-hitl-service/`
- [ ] Crear `pom.xml` con dependencias correctas
- [ ] Crear `HitlServiceApplication.java`
- [ ] Crear `HitlController.java` con todos los endpoints
- [ ] Crear `application.yml` con configuración
- [ ] Agregar módulo al `pom.xml` padre

### Servicios de Negocio
- [ ] Verificar/Crear `HitlSupervisionService` en `codeflowx.govern.business`
- [ ] Implementar métodos de negocio requeridos
- [ ] Agregar logging apropiado

### Repositorios
- [ ] Verificar/Crear `HitlSupervisionRepository`
- [ ] Verificar/Crear `HitlDecisionRepository`
- [ ] Agregar queries personalizadas si es necesario

### DTOs
- [ ] Verificar/Crear DTOs en `codeflowx.govern.nocode.dtos`
- [ ] Implementar conversiones Entity -> DTO

### Entidades JPA
- [ ] Verificar entidades `HitlSupervision` y `HitlDecision`
- [ ] Verificar tablas en base de datos (GOVHITLSUPERVISIONS, GOVHITLDECISIONS)

### Testing
- [ ] Probar endpoints con Swagger UI
- [ ] Probar integración con frontend
- [ ] Verificar manejo de errores

---

## 🔗 REFERENCIAS

- **Arquitectura Frontend:** `docs/ARQUITECTURA_FRONTEND.md`
- **Integración Backend:** `docs/prompts/compliance/INTEGRACION_BACKEND_HITL.md`
- **Plantilla de Referencia:** `nocode.service/codeflowx-governance-classification-service/`
- **Prompt Implementación:** `docs/prompts/compliance/PROMPT_COMPLIANCE_HITL.md`

---

**Última actualización:** Diciembre 2025
**Estado:** Documentación completa - Pendiente implementación
