# GUÍA AGENTE 9 - EVIDENCE ENGINE API REST

**Agente:** Backend Senior - Microservicios Spring Boot
**Equipo:** Equipo 9 - Evidence Engine
**Duración:** 16 horas
**Objetivo:** Crear microservicio Spring Boot (evidence-engine) que exponga API REST pública para evidencias inmutables, reutilizando ImmutableLoggingBusinessService como librería

---

## 📋 TAREA ASIGNADA

### **Prompt 1 — Governance Core → Evidence Engine API REST + estándar de metadatos**

**Estado:** 🔴 PENDIENTE

**Objetivo:** Crear un microservicio Spring Boot (evidence-engine) que exponga una API REST pública para evidencias inmutables, reutilizando ImmutableLoggingBusinessService como librería. El BusinessService se mantiene como librería compartida; el microservicio lo usa internamente y expone endpoints adicionales para SDKs y partners.

**Requisito Legal:** Art.19 AI Act — logs inmutables y verificables.

**Esfuerzo Estimado:** 16 horas

---

## 📚 DOCUMENTOS DE REFERENCIA

### Prompt Principal:
- **`/docs/compliance/EVOLUCION_AIOS_OVERLAY.md`** - Sección "Prompt 1 — Governance Core → Evidence Engine API REST"

### Documentación General:
- **Arquitectura EnArt:** `/docs/compliance/PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md`
- **Microservicios Spring Boot:** `/docs/compliance/PROMPTS_06_MICROSERVICIO_DOCUMENTACION_JAVA.md`
- **Seguimiento:** `/docs/compliance/gaps/SEGUIMIENTO_INCIDENCIAS.md`

### Referencias Técnicas:
- **ImmutableLoggingBusinessService:** `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/logging/ImmutableLoggingBusinessService.java`
- **API Existente:** `/eclipse-workspace/nocode.service/codeflowx-aios-api/src/main/resources/openapi/aios-api.yaml`
- **Endpoints Existentes:** `/api/v1/aios/audit/logs` (GET/POST)
- **Tabla IMLIMMUTABLELOGS:** Ver schema en base de datos
- **BusinessService Base:** `/eclipse-workspace/nocode.service/codeflowx.nocode.persist/src/main/java/codeflowx/nocode/persist/BusinessService.java`

---

## 🏗️ ARQUITECTURA - CONVENCIONES OBLIGATORIAS

### **Microservicio Spring Boot:**
- **Nombre:** `codeflowx-governance-evidence-engine`
- **Puerto:** 8086
- **Ubicación:** `/eclipse-workspace/nocode.service/codeflowx-governance-evidence-engine/`
- **Framework:** Spring Boot 3.2.5 (servlet - síncrono)
- **Stack:** Servlet (no reactivo) porque `ImmutableLoggingBusinessService` es síncrono
- **Dependencias:**
  - `codeflowx.govern.business` (para ImmutableLoggingBusinessService)
  - Spring Web MVC (servlet), OpenAPI/Swagger, Actuator

### **⚠️ Decisión Arquitectónica: Servlet vs Reactivo**
- **Usar Servlet (`spring-boot-starter-web`)** porque:
  - `ImmutableLoggingBusinessService` usa `BusinessService` que es síncrono (bloqueante)
  - Patrón consistente con `codeflowx-aios-api` y `codeflowx-governance-api`
  - Operaciones de base de datos son bloqueantes (JPA/Hibernate)
  - No requiere alta concurrencia no bloqueante para este caso de uso

### **Estructura del Módulo:**
```
codeflowx-governance-evidence-engine/
├── pom.xml
├── src/main/java/com/codeflowx/governance/evidence/
│   ├── EvidenceEngineApplication.java
│   ├── controller/
│   │   └── EvidenceController.java
│   ├── service/
│   │   ├── EvidenceService.java
│   │   └── impl/
│   │       └── EvidenceServiceImpl.java
│   ├── dto/
│   │   ├── CreateEvidenceRequest.java
│   │   ├── VerifyEvidenceRequest.java
│   │   ├── EvidenceResponse.java
│   │   └── EvidenceChainResponse.java
│   └── config/
│       ├── EvidenceEngineProperties.java
│       └── OpenApiConfig.java
├── src/main/resources/
│   ├── application.yml
│   └── openapi/
│       └── evidence-engine-api.yaml
└── src/test/java/
    ├── EvidenceControllerTest.java
    ├── EvidenceServiceTest.java
    └── EvidenceIntegrationTest.java
```

---

## 📁 ESTRUCTURA DE DIRECTORIOS

### **Microservicio:**
```
/eclipse-workspace/nocode.service/codeflowx-governance-evidence-engine/
├── pom.xml                          ← Crear nuevo módulo Maven
├── src/main/java/.../EvidenceEngineApplication.java
├── src/main/java/.../controller/EvidenceController.java
├── src/main/java/.../service/EvidenceService.java
└── src/main/resources/application.yml
```

### **BusinessService (USAR, NO MODIFICAR):**
```
/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/logging/
└── ImmutableLoggingBusinessService.java  ← USAR como dependencia
```

### **API Existente (EXTENDER si necesario):**
```
/eclipse-workspace/nocode.service/codeflowx-aios-api/
└── src/main/resources/openapi/aios-api.yaml  ← Revisar endpoints existentes
```

---

## 🔧 IMPLEMENTACIÓN

### **Fase 1: Crear Módulo Maven**

#### **1.1. Crear pom.xml:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>codeflowx.govern</groupId>
        <artifactId>nocode.service</artifactId>
        <version>1.0.0</version>
    </parent>

    <artifactId>codeflowx-governance-evidence-engine</artifactId>
    <name>CodeflowX Governance Evidence Engine</name>
    <description>Microservicio Spring Boot para API REST de evidencias inmutables (EU AI Act Art. 19)</description>
    <packaging>jar</packaging>

    <properties>
        <java.version>17</java.version>
        <spring-boot.version>3.2.5</spring-boot.version>
        <springdoc.version>2.5.0</springdoc.version>
        <logstash.encoder.version>7.4</logstash.encoder.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <!-- Spring Boot Web (SERVLET - síncrono para BusinessService) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Spring Boot Validation -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- Spring Boot Actuator (health checks) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- BusinessService (librería compartida) -->
        <dependency>
            <groupId>codeflowx.govern</groupId>
            <artifactId>codeflowx.govern.business</artifactId>
            <version>1.0.0</version>
        </dependency>

        <!-- OpenAPI/Swagger (WebMVC para servlet) -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>${springdoc.version}</version>
        </dependency>

        <!-- MinIO para attachments -->
        <dependency>
            <groupId>io.minio</groupId>
            <artifactId>minio</artifactId>
            <version>8.5.7</version>
        </dependency>

        <!-- Logging -->
        <dependency>
            <groupId>net.logstash.logback</groupId>
            <artifactId>logstash-logback-encoder</artifactId>
            <version>${logstash.encoder.version}</version>
        </dependency>

        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- Tests -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>${spring-boot.version}</version>
            </plugin>
        </plugins>
    </build>
</project>
```

#### **1.2. Agregar módulo al pom.xml padre:**
```xml
<!-- En /eclipse-workspace/nocode.service/pom.xml -->
<modules>
    <!-- ... módulos existentes ... -->
    <module>codeflowx-governance-evidence-engine</module>
</modules>
```

### **Fase 2: Implementar Endpoints REST**

#### **2.1. EvidenceController.java:**
```java
package com.codeflowx.governance.evidence.controller;

import com.codeflowx.governance.evidence.dto.*;
import com.codeflowx.governance.evidence.service.EvidenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/evidence")
@Tag(name = "Evidence Engine", description = "API REST para evidencias inmutables (EU AI Act Art. 19)")
@RequiredArgsConstructor
public class EvidenceController {

    private final EvidenceService evidenceService;

    @PostMapping("/create")
    @Operation(summary = "Crear evidencia", description = "Crea una evidencia inmutable usando ImmutableLoggingBusinessService")
    public ResponseEntity<EvidenceResponse> createEvidence(@RequestBody CreateEvidenceRequest request) {
        EvidenceResponse response = evidenceService.createEvidence(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/verify/{logUuid}")
    @Operation(summary = "Verificar integridad", description = "Verifica la integridad de una evidencia usando hash chain")
    public ResponseEntity<VerifyEvidenceResponse> verifyEvidence(@PathVariable String logUuid) {
        VerifyEvidenceResponse response = evidenceService.verifyEvidence(logUuid);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/query")
    @Operation(summary = "Consultar evidencias", description = "Consulta evidencias con filtros avanzados")
    public ResponseEntity<List<EvidenceResponse>> queryEvidence(
            @RequestParam(required = false) String componentUuid,
            @RequestParam(required = false) String operation,
            @RequestParam(required = false) String since,
            @RequestParam(required = false) String until) {
        List<EvidenceResponse> response = evidenceService.queryEvidence(componentUuid, operation, since, until);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/export")
    @Operation(summary = "Exportar evidencias", description = "Exporta evidencias en formato PDF o hash chain")
    public ResponseEntity<ExportEvidenceResponse> exportEvidence(@RequestBody ExportEvidenceRequest request) {
        ExportEvidenceResponse response = evidenceService.exportEvidence(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/chain/{componentUuid}")
    @Operation(summary = "Obtener cadena de hashes", description = "Obtiene la cadena completa de hashes para un componente")
    public ResponseEntity<EvidenceChainResponse> getHashChain(@PathVariable String componentUuid) {
        EvidenceChainResponse response = evidenceService.getHashChain(componentUuid);
        return ResponseEntity.ok(response);
    }
}
```

#### **2.2. EvidenceService.java (delegar a ImmutableLoggingBusinessService):**
```java
package com.codeflowx.governance.evidence.service;

import com.codeflowx.govern.business.logging.ImmutableLoggingBusinessService;
import com.codeflowx.governance.evidence.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EvidenceServiceImpl implements EvidenceService {

    private final ImmutableLoggingBusinessService immutableLoggingBusinessService;

    @Override
    public EvidenceResponse createEvidence(CreateEvidenceRequest request) {
        log.debug("Creating evidence for component: {}", request.getComponentUuid());

        // Delegar a ImmutableLoggingBusinessService
        // Usar: immutableLoggingBusinessService.createLog(...)

        // Implementar según prompt
    }

    @Override
    public VerifyEvidenceResponse verifyEvidence(String logUuid) {
        log.debug("Verifying evidence: {}", logUuid);

        // Delegar a ImmutableLoggingBusinessService
        // Usar: immutableLoggingBusinessService.verifyIntegrity(...)

        // Implementar según prompt
    }

    // ... otros métodos
}
```

### **Fase 3: Extender ImmutableLoggingBusinessService (si necesario)**

#### **3.1. Métodos adicionales a crear:**
- `exportEvidence(UUID logUuid, ExportFormat format)` - Exportar evidencia
- `getHashChain(String componentUuid)` - Obtener cadena de hashes
- `verifyChain(String componentUuid)` - Verificar cadena completa

**Ubicación:** `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/logging/ImmutableLoggingBusinessService.java`

**IMPORTANTE:** Solo agregar métodos nuevos, NO modificar métodos existentes.

### **Fase 4: Tests**

#### **4.1. Unit Tests:**
- `EvidenceServiceTest.java` - Tests unitarios del servicio
- Mock de `ImmutableLoggingBusinessService`

#### **4.2. Integration Tests:**
- `EvidenceIntegrationTest.java` - Tests de integración con base de datos
- Usar `@SpringBootTest` con base de datos H2 en memoria

#### **4.3. Contract Tests:**
- Tests de contrato para validar OpenAPI spec
- Validar que los endpoints cumplen con el contrato

### **Fase 5: Documentación**

#### **5.1. OpenAPI Spec:**
- Crear `evidence-engine-api.yaml` en `src/main/resources/openapi/`
- Incluir todos los endpoints con ejemplos

#### **5.2. Ejemplos SDK:**
- Ejemplo Java usando `codeflowx.govern.nocode.client`
- Ejemplo Python usando requests/httpx

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### **Antes de Empezar:**
- [ ] Leer prompt completo en `EVOLUCION_AIOS_OVERLAY.md`
- [ ] Revisar `ImmutableLoggingBusinessService` existente
- [ ] Revisar endpoints existentes en `codeflowx-aios-api`
- [ ] Verificar schema de tabla `IMLIMMUTABLELOGS`

### **Fase 1: Módulo Maven**
- [ ] Crear `pom.xml` con dependencias correctas
- [ ] Agregar módulo al `pom.xml` padre
- [ ] Compilar sin errores: `mvn clean install -DskipTests`

### **Fase 2: Endpoints REST**
- [ ] Crear `EvidenceController` con todos los endpoints
- [ ] Crear `EvidenceService` que delega a `ImmutableLoggingBusinessService`
- [ ] Crear DTOs (Request/Response)
- [ ] Configurar OpenAPI/Swagger

### **Fase 3: Extender BusinessService**
- [ ] Agregar métodos adicionales si son necesarios
- [ ] NO modificar métodos existentes
- [ ] Compilar sin errores

### **Fase 4: Tests**
- [ ] Unit tests para `EvidenceService`
- [ ] Integration tests con base de datos
- [ ] Contract tests para OpenAPI
- [ ] Todos los tests pasan

### **Fase 5: Documentación**
- [ ] OpenAPI spec completo
- [ ] Ejemplos SDK Java
- [ ] Ejemplos SDK Python
- [ ] README del microservicio

### **Fase 6: Configuración**
- [ ] `application.yml` con puerto 8086
- [ ] Configuración de retención (mínimo 6 meses)
- [ ] Integración con MinIO para attachments
- [ ] Health checks

### **Después de Implementación:**
- [ ] Compilar sin errores: `mvn clean install`
- [ ] Microservicio inicia correctamente en puerto 8086
- [ ] Todos los endpoints responden correctamente
- [ ] Tests pasan (unit + integration + contract)
- [ ] Documentación OpenAPI generada
- [ ] Actualizar `SEGUIMIENTO_INCIDENCIAS.md` si aplica
- [ ] Commit con mensaje: `[AGENTE-9] Evidence Engine API REST`

---

## 📝 REGISTRO DE ARCHIVOS CREADOS

**Estado:** 🔴 PENDIENTE

**Archivos a Crear:**
- [ ] `codeflowx-governance-evidence-engine/pom.xml`
- [ ] `codeflowx-governance-evidence-engine/src/main/java/.../EvidenceEngineApplication.java`
- [ ] `codeflowx-governance-evidence-engine/src/main/java/.../controller/EvidenceController.java`
- [ ] `codeflowx-governance-evidence-engine/src/main/java/.../service/EvidenceService.java`
- [ ] `codeflowx-governance-evidence-engine/src/main/java/.../service/impl/EvidenceServiceImpl.java`
- [ ] `codeflowx-governance-evidence-engine/src/main/java/.../dto/CreateEvidenceRequest.java`
- [ ] `codeflowx-governance-evidence-engine/src/main/java/.../dto/EvidenceResponse.java`
- [ ] `codeflowx-governance-evidence-engine/src/main/java/.../dto/VerifyEvidenceResponse.java`
- [ ] `codeflowx-governance-evidence-engine/src/main/java/.../dto/ExportEvidenceRequest.java`
- [ ] `codeflowx-governance-evidence-engine/src/main/java/.../dto/ExportEvidenceResponse.java`
- [ ] `codeflowx-governance-evidence-engine/src/main/java/.../dto/EvidenceChainResponse.java`
- [ ] `codeflowx-governance-evidence-engine/src/main/resources/application.yml`
- [ ] `codeflowx-governance-evidence-engine/src/main/resources/openapi/evidence-engine-api.yaml`
- [ ] `codeflowx-governance-evidence-engine/src/test/java/.../EvidenceServiceTest.java`
- [ ] `codeflowx-governance-evidence-engine/src/test/java/.../EvidenceIntegrationTest.java`

**Archivos a Modificar:**
- [ ] `nocode.service/pom.xml` - Agregar módulo
- [ ] `codeflowx.govern.business/.../ImmutableLoggingBusinessService.java` - Agregar métodos adicionales si necesario

**Fecha Inicio:** [FECHA]
**Fecha Finalización:** [FECHA]

---

## 🚨 GESTIÓN DE BLOQUEOS

### **Si encuentras bloqueos:**

1. **ImmutableLoggingBusinessService no tiene método necesario:**
   - Agregar método nuevo (NO modificar existentes)
   - Seguir misma estructura y convenciones

2. **Endpoints ya existen en codeflowx-aios-api:**
   - Revisar si se pueden extender
   - O crear adapters que deleguen al BusinessService

3. **Error de compilación:**
   - Verificar que `codeflowx.govern.business` está compilado
   - Verificar dependencias en `pom.xml`

4. **Duda sobre implementación:**
   - Revisar prompt completo en `EVOLUCION_AIOS_OVERLAY.md`
   - Revisar `ImmutableLoggingBusinessService` como referencia
   - Documentar la duda en reporte

---

## 📊 REPORTE DE PROGRESO (Cada 2 horas)

```
AGENTE 9 - REPORTE HORA [X]
==========================
Fase Actual: [Fase 1/2/3/4/5/6]
Progreso: [%]

Tareas Completadas:
- [Lista de tareas]

Tareas En Progreso:
- [Tarea]: [Progreso %] - [Bloqueos si hay]

Archivos Creados/Modificados:
- [Lista de archivos]

Bloqueos/Problemas:
- [Lista de bloqueos]

Tiempo Estimado Restante:
- [Horas estimadas]
```

---

## 📚 REFERENCIAS RÁPIDAS

### **Comandos Útiles:**
```bash
# Compilar módulo business primero
cd /mnt/c/Users/ManuelGonzalez/eclipse-workspace/nocode.service/codeflowx.govern.business
mvn clean install -DskipTests

# Compilar nuevo microservicio
cd /mnt/c/Users/ManuelGonzalez/eclipse-workspace/nocode.service/codeflowx-governance-evidence-engine
mvn clean install -DskipTests

# Ejecutar microservicio
mvn spring-boot:run

# Verificar puerto 8086
curl http://localhost:8086/actuator/health
```

### **Rutas Importantes:**
- **Prompt:** `/git/suinsit.nova.web/docs/compliance/EVOLUCION_AIOS_OVERLAY.md`
- **BusinessService:** `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/logging/ImmutableLoggingBusinessService.java`
- **API Existente:** `/eclipse-workspace/nocode.service/codeflowx-aios-api/src/main/resources/openapi/aios-api.yaml`
- **Microservicio:** `/eclipse-workspace/nocode.service/codeflowx-governance-evidence-engine/`

---

**Última Actualización:** [FECHA]
**Próxima Revisión:** Al completar cada fase
