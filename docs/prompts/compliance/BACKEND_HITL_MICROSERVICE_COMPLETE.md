# 🚀 MICROSERVICIO COMPLETO - HITL SUPERVISION SERVICE

**Archivos completos del microservicio para copiar directamente**

---

## 📦 1. pom.xml

**Ubicación:** `nocode.service/codeflowx-governance-hitl-service/pom.xml`

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

## 🚀 2. Application Principal

**Ubicación:** `nocode.service/codeflowx-governance-hitl-service/src/main/java/com/codeflowx/govern/hitl/HitlServiceApplication.java`

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

## 🎮 3. Controller Reactivo

**Ubicación:** `nocode.service/codeflowx-governance-hitl-service/src/main/java/com/codeflowx/govern/hitl/controller/HitlController.java`

```java
package com.codeflowx.govern.hitl.controller;

import com.codeflowx.govern.business.compliance.HitlSupervisionBusinessService;
import com.codeflowx.govern.nocode.dtos.compliance.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hitl")
@RequiredArgsConstructor
@Slf4j
public class HitlController {

    private final HitlSupervisionBusinessService businessService;

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
            .map(ResponseEntity::ok)
            .onErrorResume(error -> {
                log.error("Error updating HITL configuration", error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }
}
```

---

## ⚙️ 4. application.yml

**Ubicación:** `nocode.service/codeflowx-governance-hitl-service/src/main/resources/application.yml`

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

## 📝 5. README.md del Microservicio

**Ubicación:** `nocode.service/codeflowx-governance-hitl-service/README.md`

```markdown
# HITL Supervision Service

Microservicio reactivo para HITL Supervision según Art. 14 EU AI Act.

## Endpoints

- `GET /api/v1/hitl/dashboard` - Dashboard con métricas
- `GET /api/v1/hitl/interventions` - Intervenciones con filtros
- `POST /api/v1/hitl/interventions` - Registrar decisión
- `GET /api/v1/hitl/config` - Configuración
- `POST /api/v1/hitl/config` - Actualizar configuración

## Swagger UI

http://localhost:8091/swagger-ui.html

## Puerto

8091 (configurable mediante SERVER_PORT)
```

---

## ✅ PASOS PARA IMPLEMENTAR

1. **Crear estructura de directorios** en `nocode.service/codeflowx-governance-hitl-service/`
2. **Copiar archivos** según las ubicaciones indicadas
3. **Agregar al pom.xml padre** en `nocode.service/pom.xml`:
   ```xml
   <modules>
     <!-- ... otros módulos ... -->
     <module>codeflowx-governance-hitl-service</module>
   </modules>
   ```
4. **Crear Business Service** copiando desde `BACKEND_HITL_BUSINESS_SERVICE.java`
5. **Crear Repositorios** copiando desde `BACKEND_HITL_REPOSITORIES.java`
6. **Crear DTOs** copiando desde `BACKEND_HITL_DTOS.java`
7. **Compilar y ejecutar** el microservicio
8. **Probar endpoints** con Swagger UI

---

**Listo para implementación completa**

