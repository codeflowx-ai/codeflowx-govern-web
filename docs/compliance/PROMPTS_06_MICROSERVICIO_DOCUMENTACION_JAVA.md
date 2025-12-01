# PROMPT - MICROSERVICIO GENERACIÓN DOCUMENTOS (JAVA)

## Crear Microservicio Spring Boot para Generación de Documentos PDF

**Equipo:** Backend Java Team
**Fecha:** Diciembre 2025
**Objetivo:** Crear microservicio Spring Boot para generación de documentos PDF (reportes, Anexo IV, justificaciones) usando iText + FreeMarker
**Esfuerzo Estimado:** 1-2 días

---

## 🎯 CONTEXTO Y REQUISITOS

### **Propósito:**
Crear un microservicio Spring Boot dedicado para la generación de documentos PDF técnicos y de compliance, incluyendo:
- **Reportes Post-Market Surveillance** (INC-010-002)
- **Documentación Técnica Anexo IV** (INC-010-001)
- **Justificaciones y reportes ejecutivos**
- **Reportes de evaluación y métricas**

### **Tecnologías:**
- **Framework:** Spring Boot 3.2.5
- **Java:** 17
- **PDF:** iText 8.0.2 (o Apache PDFBox como alternativa)
- **Templates:** FreeMarker 2.3.32
- **Formato:** PDF, HTML (opcional), JSON (metadatos)

### **Arquitectura:**
- **Microservicio STATELESS** (sin estado)
- **REST API** para invocación desde otros servicios Java
- **Templates externos** (NO hardcoded) en `src/main/resources/templates/`
- **Integración:** Llamado desde `BusinessServices` en `codeflowx.govern.business`

---

## 📦 ESTRUCTURA DEL MÓDULO

### **1. Ubicación y Nombre:**

**Módulo:** `codeflowx-governance-documentation`
**Ubicación:** `/mnt/c/Users/ManuelGonzalez/eclipse-workspace/nocode.service/codeflowx-governance-documentation/`

### **2. Estructura de Directorios:**

```
codeflowx-governance-documentation/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── codeflowx/
│   │   │           └── governance/
│   │   │               └── documentation/
│   │   │                   ├── DocumentationApplication.java
│   │   │                   ├── config/
│   │   │                   │   ├── FreeMarkerConfig.java
│   │   │                   │   └── DocumentationProperties.java
│   │   │                   ├── controller/
│   │   │                   │   └── DocumentationController.java
│   │   │                   ├── service/
│   │   │                   │   ├── DocumentGenerationService.java
│   │   │                   │   ├── PdfGenerationService.java
│   │   │                   │   └── TemplateService.java
│   │   │                   ├── dto/
│   │   │                   │   ├── GenerateReportRequest.java
│   │   │                   │   ├── GenerateAnnexIvRequest.java
│   │   │                   │   └── DocumentResponse.java
│   │   │                   ├── exception/
│   │   │                   │   ├── DocumentGenerationException.java
│   │   │                   │   └── TemplateNotFoundException.java
│   │   │                   └── util/
│   │   │                       └── DocumentUtils.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── templates/
│   │           ├── reports/
│   │           │   ├── pmm_surveillance_report.ftl
│   │           │   ├── compliance_executive_report.ftl
│   │           │   └── evaluation_report.ftl
│   │           └── annex-iv/
│   │               └── technical_documentation.ftl
│   └── test/
│       └── java/
│           └── com/codeflowx/governance/documentation/
│               └── service/
│                   └── DocumentGenerationServiceTest.java
```

---

## 🔧 IMPLEMENTACIÓN PASO A PASO

### **PASO 1: Crear pom.xml**

**Ubicación:** `codeflowx-governance-documentation/pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>codeflowx.govern</groupId>
        <artifactId>nocode.service</artifactId>
        <version>1.0.0</version>
    </parent>

    <artifactId>codeflowx-governance-documentation</artifactId>
    <name>CodeflowX Governance Documentation Service</name>
    <description>Spring Boot microservice for generating PDF documents (reports, Annex IV, justifications) using iText and FreeMarker</description>
    <packaging>jar</packaging>

    <properties>
        <java.version>17</java.version>
        <spring-boot.version>3.2.5</spring-boot.version>
        <springdoc.version>2.5.0</springdoc.version>
        <itext.version>8.0.2</itext.version>
        <itext.html2pdf.version>5.0.2</itext.html2pdf.version>
        <freemarker.version>2.3.32</freemarker.version>
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
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <!-- FreeMarker para templates -->
        <dependency>
            <groupId>org.freemarker</groupId>
            <artifactId>freemarker</artifactId>
            <version>${freemarker.version}</version>
        </dependency>

        <!-- iText para generación PDF -->
        <dependency>
            <groupId>com.itextpdf</groupId>
            <artifactId>itext7-core</artifactId>
            <version>${itext.version}</version>
        </dependency>
        <dependency>
            <groupId>com.itextpdf</groupId>
            <artifactId>html2pdf</artifactId>
            <version>${itext.html2pdf.version}</version>
        </dependency>

        <!-- OpenAPI/Swagger -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>${springdoc.version}</version>
        </dependency>

        <!-- Logging -->
        <dependency>
            <groupId>net.logstash.logback</groupId>
            <artifactId>logstash-logback-encoder</artifactId>
            <version>${logstash.encoder.version}</version>
        </dependency>

        <!-- DTOs compartidos (si es necesario) -->
        <dependency>
            <groupId>com.codeflowx.govern</groupId>
            <artifactId>codeflowx.govern.nocode.dtos</artifactId>
            <version>1.0.0</version>
        </dependency>

        <!-- Testing -->
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

### **PASO 2: Añadir módulo al pom.xml padre**

**Ubicación:** `/mnt/c/Users/ManuelGonzalez/eclipse-workspace/nocode.service/pom.xml`

Añadir en la sección `<modules>`:

```xml
<module>codeflowx-governance-documentation</module>
```

### **PASO 3: Clase Principal (Application.java)**

**Ubicación:** `src/main/java/com/codeflowx/governance/documentation/DocumentationApplication.java`

```java
package com.codeflowx.governance.documentation;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = "com.codeflowx.governance.documentation")
@EnableConfigurationProperties(DocumentationProperties.class)
@OpenAPIDefinition(
        info = @Info(
                title = "CodeflowX Governance Documentation Service",
                version = "1.0",
                description = "REST API for generating PDF documents (reports, Annex IV, justifications) using iText and FreeMarker",
                contact = @Contact(name = "CodeflowX", email = "governance@codeflowx.ai")
        ),
        tags = {
                @Tag(name = "Document Generation", description = "Operations for generating PDF documents"),
                @Tag(name = "Reports", description = "Post-Market Surveillance and compliance reports"),
                @Tag(name = "Annex IV", description = "Technical documentation generation (EU AI Act Annex IV)")
        }
)
public class DocumentationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocumentationApplication.class, args);
    }
}
```

### **PASO 4: Configuración FreeMarker**

**Ubicación:** `src/main/java/com/codeflowx/governance/documentation/config/FreeMarkerConfig.java`

```java
package com.codeflowx.governance.documentation.config;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.nio.charset.StandardCharsets;

@org.springframework.context.annotation.Configuration
public class FreeMarkerConfig {

    @Bean
    @Primary
    public Configuration freeMarkerConfiguration() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);

        // Cargar templates desde classpath
        TemplateLoader templateLoader = new ClassTemplateLoader(
            FreeMarkerConfig.class,
            "/templates"
        );
        cfg.setTemplateLoader(templateLoader);

        // Configuración
        cfg.setDefaultEncoding(StandardCharsets.UTF_8.name());
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);

        return cfg;
    }
}
```

### **PASO 5: Properties de Configuración**

**Ubicación:** `src/main/java/com/codeflowx/governance/documentation/config/DocumentationProperties.java`

```java
package com.codeflowx.governance.documentation.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "documentation")
public class DocumentationProperties {

    private String templatesPath = "templates";
    private int maxReportSizeMb = 50;
    private boolean enableCaching = true;
    private int cacheSize = 100;

    // Getters y Setters
    public String getTemplatesPath() {
        return templatesPath;
    }

    public void setTemplatesPath(String templatesPath) {
        this.templatesPath = templatesPath;
    }

    public int getMaxReportSizeMb() {
        return maxReportSizeMb;
    }

    public void setMaxReportSizeMb(int maxReportSizeMb) {
        this.maxReportSizeMb = maxReportSizeMb;
    }

    public boolean isEnableCaching() {
        return enableCaching;
    }

    public void setEnableCaching(boolean enableCaching) {
        this.enableCaching = enableCaching;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }
}
```

### **PASO 6: application.yml**

**Ubicación:** `src/main/resources/application.yml`

```yaml
spring:
  application:
    name: codeflowx-governance-documentation
  servlet:
    multipart:
      max-file-size: 50MB
      max-request-size: 50MB

server:
  port: 8085

documentation:
  templates-path: templates
  max-report-size-mb: 50
  enable-caching: true
  cache-size: 100

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
    enabled: true

logging:
  level:
    com.codeflowx.governance.documentation: INFO
    freemarker: WARN
```

### **PASO 7: DTOs**

#### **GenerateReportRequest.java**

**Ubicación:** `src/main/java/com/codeflowx/governance/documentation/dto/GenerateReportRequest.java`

```java
package com.codeflowx.governance.documentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public class GenerateReportRequest {

    @NotBlank(message = "Template name is required")
    private String templateName;

    @NotNull(message = "Report data is required")
    private Map<String, Object> reportData;

    private String outputFormat = "pdf"; // pdf, html, json

    private String fileName;

    // Getters y Setters
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Map<String, Object> getReportData() {
        return reportData;
    }

    public void setReportData(Map<String, Object> reportData) {
        this.reportData = reportData;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
```

#### **GenerateAnnexIvRequest.java**

**Ubicación:** `src/main/java/com/codeflowx/governance/documentation/dto/GenerateAnnexIvRequest.java`

```java
package com.codeflowx.governance.documentation.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

public class GenerateAnnexIvRequest {

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotNull(message = "Annex IV data is required")
    private Map<String, Object> annexIvData;

    private String outputFormat = "pdf";

    // Getters y Setters
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Map<String, Object> getAnnexIvData() {
        return annexIvData;
    }

    public void setAnnexIvData(Map<String, Object> annexIvData) {
        this.annexIvData = annexIvData;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }
}
```

#### **DocumentResponse.java**

**Ubicación:** `src/main/java/com/codeflowx/governance/documentation/dto/DocumentResponse.java`

```java
package com.codeflowx.governance.documentation.dto;

import java.time.LocalDateTime;

public class DocumentResponse {

    private byte[] documentBytes;
    private String fileName;
    private String contentType;
    private long sizeBytes;
    private LocalDateTime generatedAt;

    // Getters y Setters
    public byte[] getDocumentBytes() {
        return documentBytes;
    }

    public void setDocumentBytes(byte[] documentBytes) {
        this.documentBytes = documentBytes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
}
```

### **PASO 8: Servicios**

#### **DocumentGenerationService.java**

**Ubicación:** `src/main/java/com/codeflowx/governance/documentation/service/DocumentGenerationService.java`

```java
package com.codeflowx.governance.documentation.service;

import com.codeflowx.governance.documentation.dto.DocumentResponse;
import com.codeflowx.governance.documentation.dto.GenerateAnnexIvRequest;
import com.codeflowx.governance.documentation.dto.GenerateReportRequest;
import com.codeflowx.governance.documentation.exception.DocumentGenerationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentGenerationService {

    private final PdfGenerationService pdfGenerationService;
    private final TemplateService templateService;

    /**
     * Genera un reporte PDF usando template FreeMarker
     */
    public DocumentResponse generateReport(GenerateReportRequest request) {
        try {
            log.info("Generating report with template: {}", request.getTemplateName());

            // 1. Renderizar template FreeMarker a HTML
            String htmlContent = templateService.renderTemplate(
                request.getTemplateName(),
                request.getReportData()
            );

            // 2. Convertir HTML a PDF
            byte[] pdfBytes = pdfGenerationService.htmlToPdf(htmlContent);

            // 3. Crear respuesta
            DocumentResponse response = new DocumentResponse();
            response.setDocumentBytes(pdfBytes);
            response.setFileName(request.getFileName() != null
                ? request.getFileName()
                : generateFileName(request.getTemplateName()));
            response.setContentType("application/pdf");
            response.setSizeBytes(pdfBytes.length);
            response.setGeneratedAt(LocalDateTime.now());

            log.info("Report generated successfully: {} ({} bytes)",
                response.getFileName(), response.getSizeBytes());

            return response;

        } catch (Exception e) {
            log.error("Error generating report with template: {}",
                request.getTemplateName(), e);
            throw new DocumentGenerationException(
                "Error generating report: " + e.getMessage(), e);
        }
    }

    /**
     * Genera documentación técnica Anexo IV
     */
    public DocumentResponse generateAnnexIv(GenerateAnnexIvRequest request) {
        try {
            log.info("Generating Annex IV documentation for project: {}",
                request.getProjectId());

            // Renderizar template Anexo IV
            String htmlContent = templateService.renderTemplate(
                "annex-iv/technical_documentation.ftl",
                request.getAnnexIvData()
            );

            // Convertir a PDF
            byte[] pdfBytes = pdfGenerationService.htmlToPdf(htmlContent);

            DocumentResponse response = new DocumentResponse();
            response.setDocumentBytes(pdfBytes);
            response.setFileName("annex-iv-project-" + request.getProjectId() + ".pdf");
            response.setContentType("application/pdf");
            response.setSizeBytes(pdfBytes.length);
            response.setGeneratedAt(LocalDateTime.now());

            log.info("Annex IV documentation generated successfully for project: {}",
                request.getProjectId());

            return response;

        } catch (Exception e) {
            log.error("Error generating Annex IV for project: {}",
                request.getProjectId(), e);
            throw new DocumentGenerationException(
                "Error generating Annex IV: " + e.getMessage(), e);
        }
    }

    private String generateFileName(String templateName) {
        String baseName = templateName.replace(".ftl", "")
                                     .replace("/", "_");
        return baseName + "_" + System.currentTimeMillis() + ".pdf";
    }
}
```

#### **PdfGenerationService.java**

**Ubicación:** `src/main/java/com/codeflowx/governance/documentation/service/PdfGenerationService.java`

```java
package com.codeflowx.governance.documentation.service;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class PdfGenerationService {

    /**
     * Convierte HTML a PDF usando iText
     */
    public byte[] htmlToPdf(String htmlContent) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PdfWriter writer = new PdfWriter(baos);
             PdfDocument pdfDoc = new PdfDocument(writer)) {

            // Convertir HTML a PDF
            HtmlConverter.convertToPdf(htmlContent, pdfDoc);

            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Error converting HTML to PDF", e);
            throw new IOException("Error converting HTML to PDF: " + e.getMessage(), e);
        }
    }
}
```

#### **TemplateService.java**

**Ubicación:** `src/main/java/com/codeflowx/governance/documentation/service/TemplateService.java`

```java
package com.codeflowx.governance.documentation.service;

import com.codeflowx.governance.documentation.exception.TemplateNotFoundException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateService {

    private final Configuration freeMarkerConfig;

    /**
     * Renderiza un template FreeMarker con los datos proporcionados
     */
    public String renderTemplate(String templateName, Map<String, Object> data)
            throws IOException, TemplateException {

        try {
            // Asegurar extensión .ftl
            if (!templateName.endsWith(".ftl")) {
                templateName = templateName + ".ftl";
            }

            log.debug("Rendering template: {} with data keys: {}",
                templateName, data != null ? data.keySet() : "null");

            // Cargar template
            Template template = freeMarkerConfig.getTemplate(templateName);

            // Renderizar
            StringWriter writer = new StringWriter();
            template.process(data, writer);

            return writer.toString();

        } catch (IOException e) {
            log.error("Template not found or error reading: {}", templateName, e);
            throw new TemplateNotFoundException(
                "Template not found: " + templateName, e);
        } catch (TemplateException e) {
            log.error("Error processing template: {}", templateName, e);
            throw new TemplateException(
                "Error processing template: " + templateName, e);
        }
    }
}
```

### **PASO 9: Controlador REST**

**Ubicación:** `src/main/java/com/codeflowx/governance/documentation/controller/DocumentationController.java`

```java
package com.codeflowx.governance.documentation.controller;

import com.codeflowx.governance.documentation.dto.DocumentResponse;
import com.codeflowx.governance.documentation.dto.GenerateAnnexIvRequest;
import com.codeflowx.governance.documentation.dto.GenerateReportRequest;
import com.codeflowx.governance.documentation.service.DocumentGenerationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documentation")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Document Generation", description = "API for generating PDF documents")
public class DocumentationController {

    private final DocumentGenerationService documentGenerationService;

    @PostMapping("/generate-report")
    @Operation(summary = "Generate PDF report",
               description = "Generates a PDF report using a FreeMarker template")
    public ResponseEntity<byte[]> generateReport(
            @Valid @RequestBody GenerateReportRequest request) {

        log.info("Received request to generate report: {}", request.getTemplateName());

        DocumentResponse response = documentGenerationService.generateReport(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", response.getFileName());
        headers.setContentLength(response.getSizeBytes());

        return new ResponseEntity<>(response.getDocumentBytes(), headers, HttpStatus.OK);
    }

    @PostMapping("/generate-annex-iv")
    @Operation(summary = "Generate Annex IV technical documentation",
               description = "Generates EU AI Act Annex IV technical documentation PDF")
    public ResponseEntity<byte[]> generateAnnexIv(
            @Valid @RequestBody GenerateAnnexIvRequest request) {

        log.info("Received request to generate Annex IV for project: {}",
            request.getProjectId());

        DocumentResponse response = documentGenerationService.generateAnnexIv(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", response.getFileName());
        headers.setContentLength(response.getSizeBytes());

        return new ResponseEntity<>(response.getDocumentBytes(), headers, HttpStatus.OK);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Documentation service is running");
    }
}
```

### **PASO 10: Excepciones**

#### **DocumentGenerationException.java**

**Ubicación:** `src/main/java/com/codeflowx/governance/documentation/exception/DocumentGenerationException.java`

```java
package com.codeflowx.governance.documentation.exception;

public class DocumentGenerationException extends RuntimeException {

    public DocumentGenerationException(String message) {
        super(message);
    }

    public DocumentGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

#### **TemplateNotFoundException.java**

**Ubicación:** `src/main/java/com/codeflowx/governance/documentation/exception/TemplateNotFoundException.java`

```java
package com.codeflowx.governance.documentation.exception;

public class TemplateNotFoundException extends RuntimeException {

    public TemplateNotFoundException(String message) {
        super(message);
    }

    public TemplateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

### **PASO 11: Template de Ejemplo (FreeMarker)**

**Ubicación:** `src/main/resources/templates/reports/pmm_surveillance_report.ftl`

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Post-Market Surveillance Report</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            color: #333;
        }
        h1 {
            color: #2c3e50;
            border-bottom: 3px solid #3498db;
            padding-bottom: 10px;
        }
        h2 {
            color: #34495e;
            margin-top: 30px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: left;
        }
        th {
            background-color: #3498db;
            color: white;
        }
        .metric {
            background-color: #ecf0f1;
            padding: 10px;
            margin: 10px 0;
            border-left: 4px solid #3498db;
        }
    </style>
</head>
<body>
    <h1>Post-Market Surveillance Report</h1>

    <div class="metric">
        <strong>Report Period:</strong> ${reportData.reportPeriod!""}<br>
        <strong>Project ID:</strong> ${reportData.projectId!""}<br>
        <strong>Generated At:</strong> ${reportData.generatedAt!""}
    </div>

    <h2>Executive Summary</h2>
    <p>${reportData.executiveSummary!""}</p>

    <h2>Performance Metrics</h2>
    <table>
        <tr>
            <th>Metric</th>
            <th>Value</th>
            <th>Threshold</th>
            <th>Status</th>
        </tr>
        <#if reportData.metrics??>
            <#list reportData.metrics as metric>
            <tr>
                <td>${metric.name!""}</td>
                <td>${metric.value!""}</td>
                <td>${metric.threshold!""}</td>
                <td>${metric.status!""}</td>
            </tr>
            </#list>
        </#if>
    </table>

    <h2>Incidents</h2>
    <#if reportData.incidents?? && (reportData.incidents?size > 0)>
        <#list reportData.incidents as incident>
        <div class="metric">
            <strong>Incident ID:</strong> ${incident.id!""}<br>
            <strong>Severity:</strong> ${incident.severity!""}<br>
            <strong>Description:</strong> ${incident.description!""}
        </div>
        </#list>
    <#else>
        <p>No incidents reported during this period.</p>
    </#if>

    <h2>Recommendations</h2>
    <#if reportData.recommendations??>
        <ul>
            <#list reportData.recommendations as recommendation>
            <li>${recommendation!""}</li>
            </#list>
        </ul>
    </#if>
</body>
</html>
```

---

## 🔗 INTEGRACIÓN CON BUSINESS SERVICES

### **Ejemplo de uso desde BusinessService:**

```java
@Service
@RequiredArgsConstructor
public class PostMarketSurveillanceReportService {

    @Value("${documentation.service.url:http://localhost:8085}")
    private String documentationServiceUrl;

    private final RestTemplate restTemplate;

    public byte[] generateSurveillanceReport(Long projectId) {
        // 1. Preparar datos desde BD
        Map<String, Object> reportData = prepareReportData(projectId);

        // 2. Llamar microservicio de documentación
        GenerateReportRequest request = new GenerateReportRequest();
        request.setTemplateName("reports/pmm_surveillance_report");
        request.setReportData(reportData);
        request.setOutputFormat("pdf");
        request.setFileName("pmm_report_" + projectId + ".pdf");

        ResponseEntity<byte[]> response = restTemplate.postForEntity(
            documentationServiceUrl + "/api/documentation/generate-report",
            request,
            byte[].class
        );

        // 3. Retornar PDF generado
        return response.getBody();
    }
}
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

- [ ] Crear módulo `codeflowx-governance-documentation` en `nocode.service`
- [ ] Añadir módulo al `pom.xml` padre
- [ ] Crear `pom.xml` con dependencias (iText, FreeMarker, Spring Boot)
- [ ] Crear `DocumentationApplication.java`
- [ ] Crear `FreeMarkerConfig.java`
- [ ] Crear `DocumentationProperties.java`
- [ ] Crear `application.yml`
- [ ] Crear DTOs (`GenerateReportRequest`, `GenerateAnnexIvRequest`, `DocumentResponse`)
- [ ] Crear servicios (`DocumentGenerationService`, `PdfGenerationService`, `TemplateService`)
- [ ] Crear `DocumentationController.java` con endpoints REST
- [ ] Crear excepciones personalizadas
- [ ] Crear templates FreeMarker de ejemplo
- [ ] Compilar sin errores
- [ ] Probar endpoints con Postman/Swagger
- [ ] Documentar en `/docs/developers/compliance/`

---

## 📚 REFERENCIAS

- **iText Documentation:** https://itextpdf.com/
- **FreeMarker Documentation:** https://freemarker.apache.org/
- **Spring Boot 3.2.5:** https://spring.io/projects/spring-boot
- **Estructura similar:** `codeflowx-governance-api` (referencia)

---

## 🚀 DESPLIEGUE

El microservicio se despliega como JAR independiente:

```bash
java -jar codeflowx-governance-documentation-1.0.0.jar
```

**Puerto por defecto:** 8085
**Swagger UI:** http://localhost:8085/swagger-ui.html
**Health Check:** http://localhost:8085/api/documentation/health
