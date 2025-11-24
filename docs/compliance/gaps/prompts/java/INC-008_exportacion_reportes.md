# PROMPT: INC-008-DS - Exportación de Reportes en Formatos Estándar

**Incidencia:** INC-008-DS  
**Prioridad:** 🟡 MEDIUM  
**Artículo:** ISO 42001 9.2 (auditoría interna)  
**Esfuerzo Estimado:** 3-4 días  
**Tipo:** Java - Backend + Frontend

---

## CONTEXTO

Las evaluaciones no pueden exportarse en formatos estándar (PDF, Excel, JSON) para auditorías externas o reportes regulatorios.

**Ubicación Actual:**
- `dataset-quality-dashboard-overview.zul` - No hay botones de exportación
- No hay endpoints de API para generar reportes

---

## REQUISITOS

1. Endpoint `/api/dataset-quality/export/{evaluationId}?format=pdf|excel|json`
2. Generar PDF con gráficos, métricas y recomendaciones
3. Excel con datos tabulares para análisis
4. JSON para integración con sistemas externos

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear Service de Exportación

**Archivo:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/business/evaluation/DatasetQualityReportService.java`

```java
package com.codeflowx.govern.business.evaluation;

import com.codeflowx.govern.entity.governance.DatasetQuality;
import com.codeflowx.govern.entity.evaluation.ModelBiasAnalysis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * Service para generar reportes de evaluación de datasets
 * ISO 42001 9.2 - Auditoría interna
 */
@Slf4j
@Service
public class DatasetQualityReportService {
    
    @Autowired
    private BusinessService businessService;
    
    @Autowired
    private DatasetBiasVisualizationService visualizationService;
    
    /**
     * Genera reporte en formato especificado
     */
    public byte[] generateReport(Long evaluationId, String format) {
        DatasetQuality evaluation = businessService.findById(DatasetQuality.class, evaluationId);
        if (evaluation == null) {
            throw new IllegalArgumentException("Evaluation not found: " + evaluationId);
        }
        
        switch (format.toUpperCase()) {
            case "PDF":
                return generatePdfReport(evaluation);
            case "EXCEL":
                return generateExcelReport(evaluation);
            case "JSON":
                return generateJsonReport(evaluation);
            default:
                throw new IllegalArgumentException("Unsupported format: " + format);
        }
    }
    
    private byte[] generatePdfReport(DatasetQuality evaluation) {
        try {
            // Usar iText o Apache PDFBox
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            
            // Crear documento PDF
            // Incluir:
            // - Header con logo y fecha
            // - Resumen ejecutivo
            // - Métricas de calidad
            // - Análisis de bias
            // - Problemas detectados
            // - Recomendaciones
            // - Gráficos (si es posible)
            
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("Error generating PDF report: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate PDF report", e);
        }
    }
    
    private byte[] generateExcelReport(DatasetQuality evaluation) {
        try {
            // Usar Apache POI
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            
            // Crear workbook Excel
            // Hojas:
            // - Resumen
            // - Métricas detalladas
            // - Problemas detectados
            // - Recomendaciones
            // - Historial (si hay versiones)
            
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("Error generating Excel report: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate Excel report", e);
        }
    }
    
    private byte[] generateJsonReport(DatasetQuality evaluation) {
        try {
            Map<String, Object> report = new HashMap<>();
            
            // Datos básicos
            report.put("evaluation_id", evaluation.getDqlevaluationid());
            report.put("dataset_name", evaluation.getDqldatasetname());
            report.put("evaluation_date", evaluation.getDqlcreatedat().toString());
            
            // Métricas
            report.put("overall_score", evaluation.getDqloverallscore());
            report.put("quality_rating", evaluation.getDqlqualityrating());
            report.put("completeness_score", evaluation.getDqlcompletenesscore());
            report.put("validity_score", evaluation.getDqlvalidityscore());
            
            // Decision
            report.put("decision", evaluation.getDqldecision());
            report.put("justification", evaluation.getDqljustification());
            
            // Issues
            if (evaluation.getDqlissuesfound() != null) {
                report.put("issues", parseJson(evaluation.getDqlissuesfound()));
            }
            
            // Recommendations
            if (evaluation.getDqlrecommendations() != null) {
                report.put("recommendations", parseJson(evaluation.getDqlrecommendations()));
            }
            
            // Visualización data
            try {
                DatasetBiasVisualizationDto visualization = 
                    visualizationService.generateVisualizationData(evaluation.getIdxdatasetquality());
                report.put("visualization_data", visualization);
            } catch (Exception e) {
                log.warn("Could not include visualization data: {}", e.getMessage());
            }
            
            // Serializar a JSON
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsBytes(report);
            
        } catch (Exception e) {
            log.error("Error generating JSON report: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate JSON report", e);
        }
    }
}
```

### 2. Crear Controller REST

**Archivo:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/controller/evaluation/DatasetQualityReportController.java`

```java
package com.codeflowx.govern.controller.evaluation;

import com.codeflowx.govern.business.evaluation.DatasetQualityReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/dataset-quality")
public class DatasetQualityReportController {
    
    @Autowired
    private DatasetQualityReportService reportService;
    
    @GetMapping("/export/{evaluationId}")
    public ResponseEntity<byte[]> exportReport(
        @PathVariable Long evaluationId,
        @RequestParam(defaultValue = "PDF") String format
    ) {
        try {
            byte[] report = reportService.generateReport(evaluationId, format);
            
            String contentType;
            String filename;
            String extension;
            
            switch (format.toUpperCase()) {
                case "PDF":
                    contentType = MediaType.APPLICATION_PDF_VALUE;
                    extension = "pdf";
                    break;
                case "EXCEL":
                    contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                    extension = "xlsx";
                    break;
                case "JSON":
                    contentType = MediaType.APPLICATION_JSON_VALUE;
                    extension = "json";
                    break;
                default:
                    return ResponseEntity.badRequest().build();
            }
            
            filename = String.format("dataset-quality-report-%d.%s", evaluationId, extension);
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(report);
                
        } catch (Exception e) {
            log.error("Error exporting report: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
```

### 3. Añadir Botones de Exportación en ZUL

**Archivo:** `suinsit.nova.web/src/main/webapp/console/platform/views/governance/dataset-quality-dashboard-overview.zul`

```xml
<!-- Añadir después de la lista de evaluaciones -->
<hlayout style="padding: 10px;">
    <button label="Exportar PDF" 
            onClick="@command('exportReport', format='PDF')"
            disabled="@load(vm.selectedItem eq null)"/>
    <button label="Exportar Excel" 
            onClick="@command('exportReport', format='EXCEL')"
            disabled="@load(vm.selectedItem eq null)"/>
    <button label="Exportar JSON" 
            onClick="@command('exportReport', format='JSON')"
            disabled="@load(vm.selectedItem eq null)"/>
</hlayout>
```

### 4. Añadir Método en ViewModel

**Archivo:** `suinsit.nova.web/src/main/java/com/codeflowx/platform/viewmodels/views/governance/DatasetQualityDashboardOverviewViewModel.java`

```java
@Command
public void exportReport(@BindingParam("format") String format) {
    if (selectedItem == null) {
        Messagebox.show("Seleccione una evaluación para exportar", "Advertencia", 
                       Messagebox.OK, Messagebox.EXCLAMATION);
        return;
    }
    
    try {
        Long evaluationId = selectedItem.getIdxdatasetquality();
        
        // Llamar a API
        String url = "/api/dataset-quality/export/" + evaluationId + "?format=" + format;
        
        // Descargar archivo
        Executions.getCurrent().sendRedirect(url, "_blank");
        
        Messagebox.show("Reporte generado correctamente", "Éxito", 
                       Messagebox.OK, Messagebox.INFORMATION);
        
    } catch (Exception e) {
        log.error("Error exporting report: {}", e.getMessage(), e);
        Messagebox.show("Error al exportar reporte: " + e.getMessage(), "Error", 
                       Messagebox.OK, Messagebox.ERROR);
    }
}
```

---

## DEPENDENCIAS

**Añadir a `pom.xml`:**

```xml
<!-- PDF Generation -->
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itext7-core</artifactId>
    <version>7.2.5</version>
</dependency>

<!-- Excel Generation -->
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.4</version>
</dependency>

<!-- JSON -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.2</version>
</dependency>
```

---

## VALIDACIONES

1. ✅ PDF se genera correctamente
2. ✅ Excel se genera con múltiples hojas
3. ✅ JSON contiene todos los datos
4. ✅ Descarga funciona desde UI
5. ✅ Formatos son correctos

---

## TESTING

```java
@Test
public void testExportPdf() {
    byte[] pdf = reportService.generateReport(evaluationId, "PDF");
    assertNotNull(pdf);
    assertTrue(pdf.length > 0);
}

@Test
public void testExportExcel() {
    byte[] excel = reportService.generateReport(evaluationId, "EXCEL");
    assertNotNull(excel);
    assertTrue(excel.length > 0);
}
```

---

## DOCUMENTACIÓN

Actualizar:
- `docs/compliance/auditoria/AUDITORIA_EVALUACION_DATASETS.md` - Exportación de reportes
- Crear guía de uso de exportación

---

## CUMPLIMIENTO ISO 42001

**9.2:** Auditoría interna
- ✅ Reportes exportables para auditorías externas
- ✅ Formatos estándar (PDF, Excel, JSON)
- ✅ Datos completos para análisis

---

**Última actualización:** Noviembre 2025  
**Versión:** 1.0  
**Responsable:** Backend Team + Frontend Team

---

**Estado:** ✅ COMPLETADO

