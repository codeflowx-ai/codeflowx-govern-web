# PROMPT: INC-010-002 - Generación Automática de Post-Market Surveillance Report

**Incidencia:** INC-010-002  
**Prioridad:** 🔴 CRÍTICA (Certification Blocker)  
**Artículo EU AI Act:** Art. 72  
**Esfuerzo Estimado:** 4 días  
**Tipo:** Java - Backend + Reporting  
**Referencia:** GAP-017

---

## CONTEXTO

No existe generación automática de informes de vigilancia poscomercialización según Art. 72. Los informes son requeridos para demostrar supervisión continua a las autoridades.

**Estado Actual:**
- ✅ Métricas almacenadas en `MONMONITORINGMETRICS`
- ✅ Alertas almacenadas en `MONMONITORINGALERTS`
- ❌ No hay generación automática de informes
- ❌ No hay template de informe

---

## REQUISITOS

1. Crear entidad `PostMarketSurveillanceReport` con tabla `PMSPOSTMARKETSURVEILLANCEREPORTS`
2. Crear servicio `PostMarketSurveillanceReportService` con generación automática
3. Template de informe con secciones requeridas según Art. 72
4. Generación de PDF
5. Integración con proceso BPMN (timer automático)
6. Almacenamiento en storage (MinIO/S3)
7. Endpoints REST para consulta y descarga

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear Entidad JPA `PostMarketSurveillanceReport`

**Tabla SQL:**
```sql
CREATE TABLE PMSPOSTMARKETSURVEILLANCEREPORTS (
    iduuid UUID UNIQUE,
    IDXPMSREPORT BIGSERIAL PRIMARY KEY,
    IDXPROJECT BIGINT NOT NULL,
    IDXMODEL BIGINT,
    PMSREPORTTYPE VARCHAR(50) NOT NULL, -- DAILY, WEEKLY, MONTHLY, AD_HOC
    PMSREPORTDATE DATE NOT NULL,
    PMSREPORTDATA JSONB NOT NULL, -- Contenido del informe
    PMSREPORTPDF BYTEA, -- PDF generado
    PMSREPORTPDFPATH VARCHAR(500), -- Path en storage (MinIO/S3)
    PMSSTATUS TEXT[] NOT NULL, -- DRAFT, GENERATED, APPROVED
    PMSCREATEDAT TIMESTAMP NOT NULL,
    PMSUPDATEDAT TIMESTAMP,
    CONSTRAINT FK_PMS_PROJECT FOREIGN KEY (IDXPROJECT) REFERENCES PRJPROJECTS(IDXPROJECT),
    CONSTRAINT FK_PMS_MODEL FOREIGN KEY (IDXMODEL) REFERENCES MODMODELS(IDXMODEL)
);

CREATE INDEX IDX_PMS_PROJECT ON PMSPOSTMARKETSURVEILLANCEREPORTS(IDXPROJECT);
CREATE INDEX IDX_PMS_MODEL ON PMSPOSTMARKETSURVEILLANCEREPORTS(IDXMODEL);
CREATE INDEX IDX_PMS_DATE ON PMSPOSTMARKETSURVEILLANCEREPORTS(PMSREPORTDATE);
CREATE INDEX IDX_PMS_TYPE ON PMSPOSTMARKETSURVEILLANCEREPORTS(PMSREPORTTYPE);
CREATE INDEX IDX_PMS_STATUS ON PMSPOSTMARKETSURVEILLANCEREPORTS USING GIN(PMSSTATUS);
```

**Entidad Java:**
```java
package com.codeflowx.govern.entities.compliance;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "PMSPOSTMARKETSURVEILLANCEREPORTS")
@Data
public class PostMarketSurveillanceReport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXPMSREPORT")
    private Long idxpmsreport;
    
    @Column(name = "iduuid", unique = true, nullable = false)
    private UUID iduuid = UUID.randomUUID();
    
    @Column(name = "IDXPROJECT", nullable = false)
    private Long idxproject;
    
    @Column(name = "IDXMODEL")
    private Long idxmodel;
    
    @Column(name = "PMSREPORTTYPE", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ReportType pmsreporttype;
    
    @Column(name = "PMSREPORTDATE", nullable = false)
    private LocalDate pmsreportdate;
    
    @Column(name = "PMSREPORTDATA", nullable = false, columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> pmsreportdata; // Contenido completo del informe
    
    @Lob
    @Column(name = "PMSREPORTPDF", columnDefinition = "bytea")
    private byte[] pmsreportpdf;
    
    @Column(name = "PMSREPORTPDFPATH", length = 500)
    private String pmsreportpdfpath; // Path en storage
    
    @Column(name = "PMSSTATUS", nullable = false, columnDefinition = "text[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private Set<String> pmsstatus; // DRAFT, GENERATED, APPROVED
    
    @Column(name = "PMSCREATEDAT", nullable = false)
    private LocalDateTime pmscreatedat = LocalDateTime.now();
    
    @Column(name = "PMSUPDATEDAT")
    private LocalDateTime pmsupdatedat;
    
    public enum ReportType {
        DAILY, WEEKLY, MONTHLY, AD_HOC
    }
}
```

### 2. Crear Servicio `PostMarketSurveillanceReportService`

```java
package com.codeflowx.govern.services.compliance;

import com.codeflowx.govern.entities.compliance.PostMarketSurveillanceReport;
import com.codeflowx.govern.repositories.compliance.PostMarketSurveillanceReportRepository;
import com.codeflowx.govern.repositories.compliance.MonitoringAlertRepository;
import com.codeflowx.govern.repositories.compliance.MonitoringMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostMarketSurveillanceReportService {
    
    private final PostMarketSurveillanceReportRepository repository;
    private final MonitoringAlertRepository alertRepository;
    private final MonitoringMetricRepository metricRepository;
    private final ReportPdfGenerator pdfGenerator;
    private final StorageService storageService;
    
    /**
     * Genera un informe de vigilancia automático
     */
    @Transactional
    public PostMarketSurveillanceReport generateReport(
            Long projectId, 
            Long modelId, 
            PostMarketSurveillanceReport.ReportType reportType,
            LocalDate reportDate) {
        
        log.info("Generando informe {} para proyecto {} y modelo {}", reportType, projectId, modelId);
        
        PostMarketSurveillanceReport report = new PostMarketSurveillanceReport();
        report.setIdxproject(projectId);
        report.setIdxmodel(modelId);
        report.setPmsreporttype(reportType);
        report.setPmsreportdate(reportDate != null ? reportDate : LocalDate.now());
        report.setPmsstatus(Set.of("DRAFT"));
        report.setPmscreatedat(LocalDateTime.now());
        
        // Generar contenido del informe
        Map<String, Object> reportData = generateReportContent(projectId, modelId, reportType, report.getPmsreportdate());
        report.setPmsreportdata(reportData);
        
        // Generar PDF
        byte[] pdfBytes = pdfGenerator.generatePdf(reportData, reportType);
        report.setPmsreportpdf(pdfBytes);
        
        // Guardar PDF en storage
        String pdfPath = storageService.saveReportPdf(projectId, report.getIdxpmsreport(), pdfBytes);
        report.setPmsreportpdfpath(pdfPath);
        
        report.setPmsstatus(Set.of("GENERATED"));
        report.setPmsupdatedat(LocalDateTime.now());
        
        return repository.save(report);
    }
    
    /**
     * Genera el contenido del informe según Art. 72
     */
    private Map<String, Object> generateReportContent(
            Long projectId, 
            Long modelId, 
            PostMarketSurveillanceReport.ReportType reportType,
            LocalDate reportDate) {
        
        Map<String, Object> content = new HashMap<>();
        
        // 1. Resumen ejecutivo
        content.put("executiveSummary", generateExecutiveSummary(projectId, modelId, reportDate));
        
        // 2. Métricas monitoreadas
        content.put("metrics", generateMetricsSection(projectId, modelId, reportDate, reportType));
        
        // 3. Alertas generadas
        content.put("alerts", generateAlertsSection(projectId, modelId, reportDate, reportType));
        
        // 4. Incidentes detectados
        content.put("incidents", generateIncidentsSection(projectId, modelId, reportDate, reportType));
        
        // 5. Tendencias y análisis
        content.put("trends", generateTrendsSection(projectId, modelId, reportDate, reportType));
        
        // 6. Acciones correctivas
        content.put("correctiveActions", generateCorrectiveActionsSection(projectId, modelId, reportDate, reportType));
        
        // Metadata
        content.put("reportDate", reportDate.toString());
        content.put("reportType", reportType.name());
        content.put("generatedAt", LocalDateTime.now().toString());
        
        return content;
    }
    
    private Map<String, Object> generateExecutiveSummary(Long projectId, Long modelId, LocalDate reportDate) {
        Map<String, Object> summary = new HashMap<>();
        // Implementar lógica de resumen
        return summary;
    }
    
    private List<Map<String, Object>> generateMetricsSection(
            Long projectId, Long modelId, LocalDate reportDate, PostMarketSurveillanceReport.ReportType reportType) {
        // Consultar métricas del período
        return metricRepository.findByProjectAndDateRange(projectId, modelId, 
            getStartDate(reportDate, reportType), reportDate);
    }
    
    private List<Map<String, Object>> generateAlertsSection(
            Long projectId, Long modelId, LocalDate reportDate, PostMarketSurveillanceReport.ReportType reportType) {
        // Consultar alertas del período
        return alertRepository.findByProjectAndDateRange(projectId, modelId, 
            getStartDate(reportDate, reportType), reportDate);
    }
    
    private List<Map<String, Object>> generateIncidentsSection(
            Long projectId, Long modelId, LocalDate reportDate, PostMarketSurveillanceReport.ReportType reportType) {
        // Consultar incidentes graves del período
        return Collections.emptyList(); // Implementar
    }
    
    private Map<String, Object> generateTrendsSection(
            Long projectId, Long modelId, LocalDate reportDate, PostMarketSurveillanceReport.ReportType reportType) {
        // Análisis de tendencias
        return new HashMap<>();
    }
    
    private List<Map<String, Object>> generateCorrectiveActionsSection(
            Long projectId, Long modelId, LocalDate reportDate, PostMarketSurveillanceReport.ReportType reportType) {
        // Acciones correctivas iniciadas/completadas
        return Collections.emptyList(); // Implementar
    }
    
    private LocalDate getStartDate(LocalDate endDate, PostMarketSurveillanceReport.ReportType reportType) {
        return switch (reportType) {
            case DAILY -> endDate;
            case WEEKLY -> endDate.minusWeeks(1);
            case MONTHLY -> endDate.minusMonths(1);
            case AD_HOC -> endDate.minusMonths(1); // Por defecto último mes
        };
    }
    
    /**
     * Obtiene un informe por ID
     */
    public Optional<PostMarketSurveillanceReport> getReport(Long reportId) {
        return repository.findById(reportId);
    }
    
    /**
     * Lista informes por proyecto
     */
    public List<PostMarketSurveillanceReport> getReportsByProject(Long projectId) {
        return repository.findByIdxprojectOrderByPmsreportdateDesc(projectId);
    }
    
    /**
     * Obtiene PDF del informe
     */
    public byte[] getReportPdf(Long reportId) {
        PostMarketSurveillanceReport report = repository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("Informe no encontrado"));
        
        if (report.getPmsreportpdf() != null) {
            return report.getPmsreportpdf();
        }
        
        // Si no está en BD, obtener de storage
        if (report.getPmsreportpdfpath() != null) {
            return storageService.getReportPdf(report.getPmsreportpdfpath());
        }
        
        throw new IllegalStateException("PDF del informe no disponible");
    }
}
```

### 3. Crear Interface para Generación de PDF

```java
package com.codeflowx.govern.services.compliance;

import com.codeflowx.govern.entities.compliance.PostMarketSurveillanceReport;

import java.util.Map;

public interface ReportPdfGenerator {
    byte[] generatePdf(Map<String, Object> reportData, PostMarketSurveillanceReport.ReportType reportType);
}
```

**Implementación con iText o Apache PDFBox:**
```java
package com.codeflowx.govern.services.compliance.impl;

import com.codeflowx.govern.services.compliance.ReportPdfGenerator;
import com.codeflowx.govern.entities.compliance.PostMarketSurveillanceReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class ITextPdfGenerator implements ReportPdfGenerator {
    
    @Override
    public byte[] generatePdf(Map<String, Object> reportData, PostMarketSurveillanceReport.ReportType reportType) {
        // Implementar generación de PDF con iText
        // Incluir todas las secciones del informe
        // Formato profesional con logo, headers, footers
        // Tablas y gráficos si es necesario
        return new byte[0]; // TODO: Implementar
    }
}
```

### 4. Crear Repository

```java
package com.codeflowx.govern.repositories.compliance;

import com.codeflowx.govern.entities.compliance.PostMarketSurveillanceReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostMarketSurveillanceReportRepository extends JpaRepository<PostMarketSurveillanceReport, Long> {
    
    List<PostMarketSurveillanceReport> findByIdxprojectOrderByPmsreportdateDesc(Long projectId);
    
    List<PostMarketSurveillanceReport> findByIdxprojectAndPmsreporttypeAndPmsreportdate(
        Long projectId, PostMarketSurveillanceReport.ReportType reportType, LocalDate reportDate);
}
```

### 5. Crear Controller REST

```java
package com.codeflowx.govern.controllers.compliance;

import com.codeflowx.govern.entities.compliance.PostMarketSurveillanceReport;
import com.codeflowx.govern.services.compliance.PostMarketSurveillanceReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pmm/reports")
@RequiredArgsConstructor
@Slf4j
public class PostMarketSurveillanceReportController {
    
    private final PostMarketSurveillanceReportService reportService;
    
    @GetMapping
    public ResponseEntity<List<PostMarketSurveillanceReport>> getReports(
            @RequestParam Long projectId,
            @RequestParam(required = false) Long modelId) {
        
        List<PostMarketSurveillanceReport> reports = reportService.getReportsByProject(projectId);
        return ResponseEntity.ok(reports);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PostMarketSurveillanceReport> getReport(@PathVariable Long id) {
        return reportService.getReport(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> getReportPdf(@PathVariable Long id) {
        try {
            byte[] pdf = reportService.getReportPdf(id);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "report-" + id + ".pdf");
            
            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error obteniendo PDF del informe", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/generate")
    public ResponseEntity<PostMarketSurveillanceReport> generateReport(
            @RequestParam Long projectId,
            @RequestParam(required = false) Long modelId,
            @RequestParam PostMarketSurveillanceReport.ReportType reportType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportDate) {
        
        PostMarketSurveillanceReport report = reportService.generateReport(
            projectId, modelId, reportType, reportDate);
        
        return ResponseEntity.ok(report);
    }
}
```

---

## INTEGRACIÓN CON BPMN

Crear timer en proceso BPMN `compliance-monitoring-v1.bpmn`:

```xml
<timerEventDefinition>
  <timeCycle>0 0 1 * * ?</timeCycle> <!-- Diario a las 00:00 -->
</timerEventDefinition>
```

Service Task para generación automática:
```java
@Service
public class GeneratePmmReportDelegate implements JavaDelegate {
    
    @Autowired
    private PostMarketSurveillanceReportService reportService;
    
    @Override
    public void execute(DelegateExecution execution) {
        Long projectId = (Long) execution.getVariable("projectId");
        Long modelId = (Long) execution.getVariable("modelId");
        
        reportService.generateReport(projectId, modelId, 
            PostMarketSurveillanceReport.ReportType.DAILY, LocalDate.now());
    }
}
```

---

## VALIDACIONES

1. ✅ Entidad JPA creada
2. ✅ Servicio con generación automática
3. ✅ Template de informe con todas las secciones Art. 72
4. ✅ Generación de PDF funcional
5. ✅ Almacenamiento en storage
6. ✅ Endpoints REST implementados
7. ✅ Integración con BPMN (timer automático)

---

## NOTAS

- Usar prefijo `PMS` para tabla
- PK autonumérica `IDXPMSREPORT`
- Tercera forma normal
- KISS principle
- Integrar con MinIO/S3 para almacenamiento de PDFs grandes


---

**Estado:** ✅ COMPLETADO






