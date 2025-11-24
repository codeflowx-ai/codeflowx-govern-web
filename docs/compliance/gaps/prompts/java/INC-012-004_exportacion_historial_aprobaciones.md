# PROMPT: INC-012-004 - Exportación de Historial de Aprobaciones para Auditores

**Incidencia:** INC-012-004  
**Prioridad:** 🟡 MEDIA  
**Artículo EU AI Act:** Art. 12 EU AI Act  
**Esfuerzo Estimado:** 5 días  
**Tipo:** Java + Backend

---

## CONTEXTO

No existe capacidad de exportar historial completo de aprobaciones y despliegues en formato adecuado para auditores externos. Los auditores necesitan acceso a historial de aprobaciones con timestamps, decisiones, justificaciones, métricas de evaluación y registros inmutables.

**Ubicación Actual:**
- Entidad: `ModelApproval` (tabla `MODMODELAPPROVALS`)
- Entidad: `ImmutableLog` (tabla `IMLIMMUTABLELOGS`)
- Entidad: `DeploymentLog` (tabla `SRVDEPLOYMENTLOGS`)
- Historial BPMN: Tablas Flowable (`ACT_HI_PROCINST`, `ACT_HI_TASKINST`, etc.)

**Problema:**
- Dificultad para auditorías externas
- Falta de evidencia exportable para cumplimiento
- No hay formato estandarizado para auditores

---

## REQUISITOS

1. **Crear endpoint REST** para exportar historial de aprobaciones
2. **Soportar múltiples formatos:** PDF, JSON, CSV
3. **Incluir información completa:**
   - Lista de aprobaciones con detalles
   - Métricas de evaluación
   - Referencias a ImmutableLog
   - Historial BPMN
4. **Permitir filtrado** por fecha, modelo, estado, etc.
5. **Generar reporte ejecutivo** para auditores

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear Controller: `ApprovalExportController.java`

**Ubicación:** `com.codeflowx.govern.controller.compliance.ApprovalExportController`

```java
package com.codeflowx.govern.controller.compliance;

import com.codeflowx.govern.dto.compliance.ApprovalExportRequest;
import com.codeflowx.govern.service.compliance.ApprovalExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller para exportar historial de aprobaciones
 * Requisito: INC-012-004 - Art. 12 EU AI Act
 */
@RestController
@RequestMapping("/api/compliance/approvals")
public class ApprovalExportController {
    
    @Autowired
    private ApprovalExportService approvalExportService;
    
    /**
     * Exporta historial de aprobaciones en formato especificado
     * 
     * @param format Formato de exportación: PDF, JSON, CSV
     * @param startDate Fecha inicio (opcional)
     * @param endDate Fecha fin (opcional)
     * @param modelId ID del modelo (opcional)
     * @param status Estado de aprobación (opcional)
     * @return Archivo exportado
     */
    @GetMapping(value = "/export", produces = {
        MediaType.APPLICATION_PDF_VALUE,
        MediaType.APPLICATION_JSON_VALUE,
        "text/csv"
    })
    public ResponseEntity<byte[]> exportApprovals(
            @RequestParam(defaultValue = "PDF") String format,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) Long modelId,
            @RequestParam(required = false) String status) {
        
        ApprovalExportRequest request = new ApprovalExportRequest();
        request.setFormat(format.toUpperCase());
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setModelId(modelId);
        request.setStatus(status);
        
        byte[] exportData = approvalExportService.exportApprovals(request);
        
        HttpHeaders headers = new HttpHeaders();
        String contentType;
        String filename;
        
        switch (format.toUpperCase()) {
            case "PDF":
                contentType = MediaType.APPLICATION_PDF_VALUE;
                filename = "approvals_export_" + System.currentTimeMillis() + ".pdf";
                break;
            case "JSON":
                contentType = MediaType.APPLICATION_JSON_VALUE;
                filename = "approvals_export_" + System.currentTimeMillis() + ".json";
                break;
            case "CSV":
                contentType = "text/csv";
                filename = "approvals_export_" + System.currentTimeMillis() + ".csv";
                break;
            default:
                return ResponseEntity.badRequest().build();
        }
        
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentLength(exportData.length);
        
        return new ResponseEntity<>(exportData, headers, HttpStatus.OK);
    }
    
    /**
     * Obtiene lista de aprobaciones para preview
     */
    @GetMapping("/list")
    public ResponseEntity<List<ApprovalExportDTO>> listApprovals(
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) Long modelId,
            @RequestParam(required = false) String status) {
        
        ApprovalExportRequest request = new ApprovalExportRequest();
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setModelId(modelId);
        request.setStatus(status);
        
        List<ApprovalExportDTO> approvals = approvalExportService.listApprovals(request);
        return ResponseEntity.ok(approvals);
    }
}
```

### 2. Crear Service: `ApprovalExportService.java`

**Ubicación:** `com.codeflowx.govern.service.compliance.ApprovalExportService`

```java
package com.codeflowx.govern.service.compliance;

import com.codeflowx.govern.dto.compliance.ApprovalExportRequest;
import com.codeflowx.govern.dto.compliance.ApprovalExportDTO;
import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.entity.models.ModelApproval;
import com.codeflowx.govern.entity.logging.ImmutableLog;
import com.codeflowx.govern.service.BusinessService;
import com.codeflowx.govern.service.flowable.FlowableHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para exportar historial de aprobaciones
 * Requisito: INC-012-004 - Art. 12 EU AI Act
 */
@Service
@Transactional(readOnly = true)
public class ApprovalExportService {
    
    @Autowired
    private BusinessService businessService;
    
    @Autowired
    private FlowableHistoryService flowableHistoryService;
    
    @Autowired
    private ApprovalExportPdfGenerator pdfGenerator;
    
    @Autowired
    private ApprovalExportJsonGenerator jsonGenerator;
    
    @Autowired
    private ApprovalExportCsvGenerator csvGenerator;
    
    /**
     * Exporta aprobaciones según formato solicitado
     */
    public byte[] exportApprovals(ApprovalExportRequest request) {
        List<ApprovalExportDTO> approvals = listApprovals(request);
        
        switch (request.getFormat()) {
            case "PDF":
                return pdfGenerator.generate(approvals, request);
            case "JSON":
                return jsonGenerator.generate(approvals, request);
            case "CSV":
                return csvGenerator.generate(approvals, request);
            default:
                throw new IllegalArgumentException("Formato no soportado: " + request.getFormat());
        }
    }
    
    /**
     * Lista aprobaciones según filtros
     */
    public List<ApprovalExportDTO> listApprovals(ApprovalExportRequest request) {
        String query = buildQuery(request);
        List<Object[]> results = businessService.findBySQL(Object[].class, query);
        
        return results.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Construye query SQL según filtros
     */
    private String buildQuery(ApprovalExportRequest request) {
        StringBuilder sql = new StringBuilder(
            "SELECT " +
            "  a.IDXAPPROVAL, " +
            "  a.IDXMODEL, " +
            "  m.MODNAME, " +
            "  a.MODAPPROVALSTATUS, " +
            "  a.MODAPPROVEDAT, " +
            "  a.MODAPPROVERID, " +
            "  a.MODAPPROVERNAME, " +
            "  a.MODAPPROVERROLE, " +
            "  a.MODAPPROVALNOTES, " +
            "  a.MODPERFORMANCEVALIDATION, " +
            "  a.MODBIASDETECTION, " +
            "  a.MODCOMPLIANCECHECK, " +
            "  a.MODDRIFTRISK " +
            "FROM MODMODELAPPROVALS a " +
            "INNER JOIN MODMODELS m ON a.IDXMODEL = m.IDXMODEL " +
            "WHERE 1=1"
        );
        
        List<Object> params = new ArrayList<>();
        
        if (request.getStartDate() != null) {
            sql.append(" AND a.MODAPPROVEDAT >= ?");
            params.add(request.getStartDate());
        }
        
        if (request.getEndDate() != null) {
            sql.append(" AND a.MODAPPROVEDAT <= ?");
            params.add(request.getEndDate());
        }
        
        if (request.getModelId() != null) {
            sql.append(" AND a.IDXMODEL = ?");
            params.add(request.getModelId());
        }
        
        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            sql.append(" AND a.MODAPPROVALSTATUS = ?");
            params.add(request.getStatus());
        }
        
        sql.append(" ORDER BY a.MODAPPROVEDAT DESC");
        
        return sql.toString();
    }
    
    /**
     * Mapea resultado SQL a DTO
     */
    private ApprovalExportDTO mapToDTO(Object[] row) {
        ApprovalExportDTO dto = new ApprovalExportDTO();
        dto.setApprovalId(((Number) row[0]).longValue());
        dto.setModelId(((Number) row[1]).longValue());
        dto.setModelName((String) row[2]);
        dto.setStatus((String) row[3]);
        dto.setApprovedAt((java.sql.Timestamp) row[4]);
        dto.setApproverId((String) row[5]);
        dto.setApproverName((String) row[6]);
        dto.setApproverRole((String) row[7]);
        dto.setNotes((String) row[8]);
        
        // Parsear métricas JSONB
        dto.setPerformanceValidation((String) row[9]);
        dto.setBiasDetection((String) row[10]);
        dto.setComplianceCheck((String) row[11]);
        dto.setDriftRisk((Double) row[12]);
        
        // Obtener referencias a ImmutableLog
        dto.setImmutableLogReferences(getImmutableLogReferences(dto.getApprovalId()));
        
        // Obtener historial BPMN
        dto.setBpmnHistory(flowableHistoryService.getProcessHistory(dto.getApprovalId()));
        
        return dto;
    }
    
    /**
     * Obtiene referencias a ImmutableLog
     */
    private List<String> getImmutableLogReferences(Long approvalId) {
        String query = "SELECT IMLCURRENTHASH FROM IMLIMMUTABLELOGS " +
                      "WHERE IMLENTITYTYPE = 'MODEL_APPROVAL' AND IMLENTITYID = ? " +
                      "ORDER BY IMLTIMESTAMPEPOCH DESC";
        List<String> hashes = businessService.findBySQL(String.class, query, approvalId);
        return hashes;
    }
}
```

### 3. Crear DTO: `ApprovalExportDTO.java`

**Ubicación:** `com.codeflowx.govern.dto.compliance.ApprovalExportDTO`

```java
package com.codeflowx.govern.dto.compliance;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * DTO para exportación de aprobaciones
 * Requisito: INC-012-004
 */
public class ApprovalExportDTO {
    private Long approvalId;
    private Long modelId;
    private String modelName;
    private String status;
    private Timestamp approvedAt;
    private String approverId;
    private String approverName;
    private String approverRole;
    private String notes;
    private String performanceValidation;
    private String biasDetection;
    private String complianceCheck;
    private Double driftRisk;
    private List<String> immutableLogReferences;
    private Map<String, Object> bpmnHistory;
    
    // Getters y Setters
    // ...
}
```

### 4. Crear Generador PDF: `ApprovalExportPdfGenerator.java`

**Ubicación:** `com.codeflowx.govern.service.compliance.ApprovalExportPdfGenerator`

```java
package com.codeflowx.govern.service.compliance;

import com.codeflowx.govern.dto.compliance.ApprovalExportDTO;
import com.codeflowx.govern.dto.compliance.ApprovalExportRequest;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Generador de PDF para exportación de aprobaciones
 * Requisito: INC-012-004
 */
@Component
public class ApprovalExportPdfGenerator {
    
    public byte[] generate(List<ApprovalExportDTO> approvals, ApprovalExportRequest request) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try (PdfWriter writer = new PdfWriter(baos);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {
            
            // Título
            document.add(new Paragraph("Historial de Aprobaciones - Reporte de Auditoría")
                .setFontSize(18)
                .setBold());
            
            // Información de filtros
            document.add(new Paragraph("Período: " + 
                (request.getStartDate() != null ? request.getStartDate().toString() : "Desde inicio") +
                " - " +
                (request.getEndDate() != null ? request.getEndDate().toString() : "Hasta ahora")));
            
            // Tabla de aprobaciones
            Table table = new Table(8);
            table.addHeaderCell("ID Aprobación");
            table.addHeaderCell("Modelo");
            table.addHeaderCell("Estado");
            table.addHeaderCell("Fecha Aprobación");
            table.addHeaderCell("Aprobador");
            table.addHeaderCell("Rol");
            table.addHeaderCell("Performance Score");
            table.addHeaderCell("Bias Score");
            
            for (ApprovalExportDTO approval : approvals) {
                table.addCell(approval.getApprovalId().toString());
                table.addCell(approval.getModelName());
                table.addCell(approval.getStatus());
                table.addCell(approval.getApprovedAt() != null ? 
                    approval.getApprovedAt().toString() : "N/A");
                table.addCell(approval.getApproverName() != null ? 
                    approval.getApproverName() : "N/A");
                table.addCell(approval.getApproverRole() != null ? 
                    approval.getApproverRole() : "N/A");
                table.addCell(extractScore(approval.getPerformanceValidation()));
                table.addCell(extractScore(approval.getBiasDetection()));
            }
            
            document.add(table);
            
            // Referencias a ImmutableLog
            document.add(new Paragraph("\nReferencias a Registros Inmutables:")
                .setBold());
            for (ApprovalExportDTO approval : approvals) {
                if (approval.getImmutableLogReferences() != null && 
                    !approval.getImmutableLogReferences().isEmpty()) {
                    document.add(new Paragraph("Aprobación " + approval.getApprovalId() + ":"));
                    for (String hash : approval.getImmutableLogReferences()) {
                        document.add(new Paragraph("  - " + hash));
                    }
                }
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF", e);
        }
        
        return baos.toByteArray();
    }
    
    private String extractScore(String jsonb) {
        // Extraer score de JSONB
        // TODO: Implementar parsing JSON
        return "N/A";
    }
}
```

### 5. Crear Generadores JSON y CSV

Similar a PDF generator, crear:
- `ApprovalExportJsonGenerator.java`
- `ApprovalExportCsvGenerator.java`

---

## VALIDACIONES ADICIONALES RECOMENDADAS

1. **Añadir autenticación y autorización** (solo auditores pueden exportar)
2. **Limitar tamaño de exportación** (paginación para grandes volúmenes)
3. **Añadir watermark** en PDFs para trazabilidad
4. **Registrar exportaciones** en ImmutableLog

---

## PRUEBAS REQUERIDAS

1. **Test 1:** Exportar en formato PDF → Debe generar PDF válido
2. **Test 2:** Exportar en formato JSON → Debe generar JSON válido
3. **Test 3:** Exportar en formato CSV → Debe generar CSV válido
4. **Test 4:** Filtrar por fecha → Debe retornar solo aprobaciones en rango
5. **Test 5:** Filtrar por modelo → Debe retornar solo aprobaciones del modelo
6. **Test 6:** Verificar que incluye referencias a ImmutableLog
7. **Test 7:** Verificar que incluye historial BPMN

---

## REFERENCIAS

- **Art. 12 EU AI Act:** Registro y Trazabilidad
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_012_PUESTA_PRODUCCION.md#inc-012-004`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_012_PUESTA_PRODUCCION.md`

---

## NOTAS DE IMPLEMENTACIÓN

- Usar biblioteca iText o Apache PDFBox para generación PDF
- Considerar usar Jackson para generación JSON
- Añadir paginación para grandes volúmenes de datos
- Considerar caché de exportaciones frecuentes
- Añadir logs de auditoría para cada exportación

---

**Estado:** ✅ COMPLETADO

