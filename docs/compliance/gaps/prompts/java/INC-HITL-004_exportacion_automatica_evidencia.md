# PROMPT: INC-HITL-004 - Exportación Automática de Evidencia para Auditorías

**Incidencia:** INC-HITL-004  
**Prioridad:** 🟠 HIGH  
**Artículo EU AI Act:** Art. 14.4 (Transparency)  
**Esfuerzo Estimado:** 5-7 días  
**Tipo:** Java - Backend + Frontend  
**Estado:** ✅ COMPLETADO

---

## DESCRIPCIÓN

No existe funcionalidad para exportar automáticamente evidencia de aprobaciones humanas en formatos estándar para auditorías externas (PDF, XML, JSON estructurado). Los datos almacenados en JSONB requieren parseo manual.

---

## CONTEXTO

**Ubicación Actual:**
- Datos almacenados en JSONB requieren parseo manual
- No hay endpoints REST para exportación de evidencia
- No hay generación automática de reportes de auditoría
- Consultas SQL deben ejecutarse manualmente

**Entidades Afectadas:**
- `AgentApproval` (Tabla: `AGTAGENTAPPROVALS`)
- `ModelApproval` (Tabla: `MODMODELAPPROVALS`)
- `PromptApproval` (Tabla: `PRMPROMPTAPPROVALS`)
- `HitlAuditChange` (Tabla: `GOVAUDITHITLCHANGES`)

**Referencias:**
- `/docs/compliance/auditoria/AUDITORIA_SUPERVISION_HUMANA_HITL.md`
- `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md`

---

## REQUISITOS

### 1. Endpoint REST para Exportación

- Exportar en formatos PDF, XML, JSON estructurado
- Filtrado por período, tipo, estado, aprobador
- Incluir toda la información de aprobación

### 2. Generación de Reportes Estructurados

- Reportes con metadatos de auditoría (hash, firma, timestamp)
- Formato estándar para auditorías externas

### 3. UI para Exportación

- Botones de exportación en frontend
- Selección de formato y filtros

---

## IMPLEMENTACIÓN REQUERIDA

### 1. DTO de Exportación

**Archivo:** `codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/hitl/ApprovalExportDto.java`

```java
package com.codeflowx.govern.nocode.dtos.hitl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalExportDto {
    private Long approvalId;
    private String entityType; // AGENT, MODEL, PROMPT
    private String approvalType;
    private String approvalStatus;
    private Timestamp createdAt;
    private Timestamp approvedAt;
    private String createdBy;
    private String approverName;
    private String approverRole;
    private String requestReason;
    private Map<String, Object> requestDetails;
    private Map<String, Object> riskAssessment;
    private Map<String, Object> complianceCheck;
    private Map<String, Object> technicalReview;
    private Map<String, Object> ethicalReview;
    private String approvalNotes;
    private String rejectionReason;
    private String sourceIp;
    private String userAgent;
    private String exportHash; // Hash SHA-256 del registro completo
    private Timestamp exportTimestamp;
}
```

### 2. Servicio de Exportación

**Archivo:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/hitl/ApprovalExportService.java`

```java
package com.codeflowx.govern.service.hitl;

import com.codeflowx.govern.entity.agents.AgentApproval;
import com.codeflowx.govern.entity.models.ModelApproval;
import com.codeflowx.govern.entity.prompts.PromptApproval;
import com.codeflowx.govern.nocode.dtos.hitl.ApprovalExportDto;
import com.codeflowx.govern.repository.agents.AgentApprovalRepository;
import com.codeflowx.govern.repository.models.ModelApprovalRepository;
import com.codeflowx.govern.repository.prompts.PromptApprovalRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApprovalExportService {
    
    @Autowired
    private AgentApprovalRepository agentApprovalRepository;
    
    @Autowired
    private ModelApprovalRepository modelApprovalRepository;
    
    @Autowired
    private PromptApprovalRepository promptApprovalRepository;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Exportar aprobaciones en formato JSON
     */
    @Transactional(readOnly = true)
    public List<ApprovalExportDto> exportApprovalsAsJson(
            String entityType, 
            Timestamp startDate, 
            Timestamp endDate,
            String approvalStatus,
            String approverName) {
        
        List<ApprovalExportDto> exports = new ArrayList<>();
        
        if (entityType == null || "AGENT".equals(entityType)) {
            exports.addAll(exportAgentApprovals(startDate, endDate, approvalStatus, approverName));
        }
        if (entityType == null || "MODEL".equals(entityType)) {
            exports.addAll(exportModelApprovals(startDate, endDate, approvalStatus, approverName));
        }
        if (entityType == null || "PROMPT".equals(entityType)) {
            exports.addAll(exportPromptApprovals(startDate, endDate, approvalStatus, approverName));
        }
        
        // Calcular hash para cada registro
        exports.forEach(this::calculateHash);
        
        return exports;
    }
    
    private List<ApprovalExportDto> exportAgentApprovals(
            Timestamp startDate, Timestamp endDate, String status, String approver) {
        
        List<AgentApproval> approvals = agentApprovalRepository.findByFilters(
            startDate, endDate, status, approver);
        
        return approvals.stream()
            .map(this::mapToExportDto)
            .collect(Collectors.toList());
    }
    
    private List<ApprovalExportDto> exportModelApprovals(
            Timestamp startDate, Timestamp endDate, String status, String approver) {
        
        List<ModelApproval> approvals = modelApprovalRepository.findByFilters(
            startDate, endDate, status, approver);
        
        return approvals.stream()
            .map(this::mapToExportDto)
            .collect(Collectors.toList());
    }
    
    private List<ApprovalExportDto> exportPromptApprovals(
            Timestamp startDate, Timestamp endDate, String status, String approver) {
        
        List<PromptApproval> approvals = promptApprovalRepository.findByFilters(
            startDate, endDate, status, approver);
        
        return approvals.stream()
            .map(this::mapToExportDto)
            .collect(Collectors.toList());
    }
    
    private ApprovalExportDto mapToExportDto(AgentApproval approval) {
        ApprovalExportDto dto = ApprovalExportDto.builder()
            .approvalId(approval.getIdxagentapproval())
            .entityType("AGENT")
            .approvalType(approval.getAgtapprovaltype())
            .approvalStatus(approval.getAgtapprovalstatus())
            .createdAt(approval.getAgtcreatedat())
            .approvedAt(approval.getAgtapprovedat())
            .createdBy(approval.getAgtcreatedby())
            .approverName(approval.getAgtapprovername())
            .approverRole(approval.getAgtapproverrole())
            .requestReason(approval.getAgtrequestreason())
            .approvalNotes(approval.getAgtapprovalnotes())
            .rejectionReason(approval.getAgtrejectionreason())
            .exportTimestamp(new Timestamp(System.currentTimeMillis()))
            .build();
        
        // Parsear JSONB
        if (approval.getAgtrequestdetails() != null) {
            dto.setRequestDetails(parseJsonb(approval.getAgtrequestdetails()));
        }
        if (approval.getAgtriskassessment() != null) {
            dto.setRiskAssessment(parseJsonb(approval.getAgtriskassessment()));
        }
        if (approval.getAgtcompliancecheck() != null) {
            dto.setComplianceCheck(parseJsonb(approval.getAgtcompliancecheck()));
        }
        if (approval.getAgttechnicalreview() != null) {
            dto.setTechnicalReview(parseJsonb(approval.getAgttechnicalreview()));
        }
        if (approval.getAgtethicalreview() != null) {
            dto.setEthicalReview(parseJsonb(approval.getAgtethicalreview()));
        }
        
        return dto;
    }
    
    private ApprovalExportDto mapToExportDto(ModelApproval approval) {
        // Similar a AgentApproval pero con campos MOD*
        ApprovalExportDto dto = ApprovalExportDto.builder()
            .approvalId(approval.getIdxmodelapproval())
            .entityType("MODEL")
            .approvalType(approval.getModapprovaltype())
            .approvalStatus(approval.getModapprovalstatus())
            .createdAt(approval.getModcreatedat())
            .approvedAt(approval.getModapprovedat())
            .createdBy(approval.getModcreatedby())
            .approverName(approval.getModapprovername())
            .approverRole(approval.getModapproverrole())
            .requestReason(approval.getModrequestreason())
            .approvalNotes(approval.getModapprovalnotes())
            .rejectionReason(approval.getModrejectionreason())
            .exportTimestamp(new Timestamp(System.currentTimeMillis()))
            .build();
        
        // Parsear JSONB similar
        return dto;
    }
    
    private ApprovalExportDto mapToExportDto(PromptApproval approval) {
        // Similar pero con campos PRM*
        ApprovalExportDto dto = ApprovalExportDto.builder()
            .approvalId(approval.getIdxpromptapproval())
            .entityType("PROMPT")
            .approvalType(approval.getPrmapprovaltype())
            .approvalStatus(approval.getPrmapprovalstatus())
            .createdAt(approval.getPrmcreatedat())
            .approvedAt(approval.getPrmapprovedat())
            .createdBy(approval.getPrmcreatedby())
            .approverName(approval.getPrmapprovername())
            .approverRole(approval.getPrmapproverrole())
            .requestReason(approval.getPrmrequestreason())
            .approvalNotes(approval.getPrmapprovalnotes())
            .rejectionReason(approval.getPrmrejectionreason())
            .exportTimestamp(new Timestamp(System.currentTimeMillis()))
            .build();
        
        // Parsear JSONB similar
        return dto;
    }
    
    private Map<String, Object> parseJsonb(String jsonb) {
        try {
            return objectMapper.readValue(jsonb, Map.class);
        } catch (Exception e) {
            return Map.of("error", "Error parsing JSONB: " + e.getMessage());
        }
    }
    
    private void calculateHash(ApprovalExportDto dto) {
        try {
            String json = objectMapper.writeValueAsString(dto);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(json.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            dto.setExportHash(hexString.toString());
        } catch (Exception e) {
            dto.setExportHash("ERROR: " + e.getMessage());
        }
    }
    
    /**
     * Exportar en formato XML
     */
    public String exportApprovalsAsXml(
            String entityType, 
            Timestamp startDate, 
            Timestamp endDate,
            String approvalStatus,
            String approverName) {
        
        List<ApprovalExportDto> exports = exportApprovalsAsJson(
            entityType, startDate, endDate, approvalStatus, approverName);
        
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<ApprovalExports>\n");
        xml.append("  <ExportMetadata>\n");
        xml.append("    <ExportDate>").append(LocalDateTime.now()).append("</ExportDate>\n");
        xml.append("    <TotalRecords>").append(exports.size()).append("</TotalRecords>\n");
        xml.append("  </ExportMetadata>\n");
        xml.append("  <Approvals>\n");
        
        for (ApprovalExportDto dto : exports) {
            xml.append("    <Approval>\n");
            xml.append("      <ApprovalId>").append(dto.getApprovalId()).append("</ApprovalId>\n");
            xml.append("      <EntityType>").append(dto.getEntityType()).append("</EntityType>\n");
            xml.append("      <ApprovalType>").append(dto.getApprovalType()).append("</ApprovalType>\n");
            xml.append("      <ApprovalStatus>").append(dto.getApprovalStatus()).append("</ApprovalStatus>\n");
            xml.append("      <CreatedAt>").append(dto.getCreatedAt()).append("</CreatedAt>\n");
            xml.append("      <ApprovedAt>").append(dto.getApprovedAt()).append("</ApprovedAt>\n");
            xml.append("      <CreatedBy>").append(dto.getCreatedBy()).append("</CreatedBy>\n");
            xml.append("      <ApproverName>").append(dto.getApproverName()).append("</ApproverName>\n");
            xml.append("      <ApproverRole>").append(dto.getApproverRole()).append("</ApproverRole>\n");
            xml.append("      <RequestReason>").append(escapeXml(dto.getRequestReason())).append("</RequestReason>\n");
            xml.append("      <ApprovalNotes>").append(escapeXml(dto.getApprovalNotes())).append("</ApprovalNotes>\n");
            xml.append("      <ExportHash>").append(dto.getExportHash()).append("</ExportHash>\n");
            xml.append("      <ExportTimestamp>").append(dto.getExportTimestamp()).append("</ExportTimestamp>\n");
            xml.append("    </Approval>\n");
        }
        
        xml.append("  </Approvals>\n");
        xml.append("</ApprovalExports>");
        
        return xml.toString();
    }
    
    private String escapeXml(String str) {
        if (str == null) return "";
        return str.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&apos;");
    }
    
    /**
     * Exportar en formato PDF (requiere biblioteca como iText o Apache PDFBox)
     */
    public byte[] exportApprovalsAsPdf(
            String entityType, 
            Timestamp startDate, 
            Timestamp endDate,
            String approvalStatus,
            String approverName) {
        
        // TODO: Implementar generación de PDF
        // Usar iText o Apache PDFBox
        // Incluir metadatos, tabla de aprobaciones, hash de integridad
        return new byte[0];
    }
}
```

### 3. Controller REST

**Archivo:** `codeflowx.govern.controllers/src/main/java/com/codeflowx/govern/controller/hitl/ApprovalExportController.java`

```java
package com.codeflowx.govern.controller.hitl;

import com.codeflowx.govern.nocode.dtos.hitl.ApprovalExportDto;
import com.codeflowx.govern.service.hitl.ApprovalExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/governance/approvals/export")
public class ApprovalExportController {
    
    @Autowired
    private ApprovalExportService exportService;
    
    /**
     * Exportar aprobaciones en formato JSON
     */
    @GetMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ApprovalExportDto>> exportAsJson(
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String approvalStatus,
            @RequestParam(required = false) String approverName) {
        
        Timestamp start = startDate != null ? Timestamp.valueOf(startDate) : null;
        Timestamp end = endDate != null ? Timestamp.valueOf(endDate) : null;
        
        List<ApprovalExportDto> exports = exportService.exportApprovalsAsJson(
            entityType, start, end, approvalStatus, approverName);
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, 
                "attachment; filename=approvals_export_" + LocalDateTime.now() + ".json")
            .body(exports);
    }
    
    /**
     * Exportar aprobaciones en formato XML
     */
    @GetMapping(value = "/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> exportAsXml(
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String approvalStatus,
            @RequestParam(required = false) String approverName) {
        
        Timestamp start = startDate != null ? Timestamp.valueOf(startDate) : null;
        Timestamp end = endDate != null ? Timestamp.valueOf(endDate) : null;
        
        String xml = exportService.exportApprovalsAsXml(
            entityType, start, end, approvalStatus, approverName);
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, 
                "attachment; filename=approvals_export_" + LocalDateTime.now() + ".xml")
            .body(xml);
    }
    
    /**
     * Exportar aprobaciones en formato PDF
     */
    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportAsPdf(
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String approvalStatus,
            @RequestParam(required = false) String approverName) {
        
        Timestamp start = startDate != null ? Timestamp.valueOf(startDate) : null;
        Timestamp end = endDate != null ? Timestamp.valueOf(endDate) : null;
        
        byte[] pdf = exportService.exportApprovalsAsPdf(
            entityType, start, end, approvalStatus, approverName);
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, 
                "attachment; filename=approvals_export_" + LocalDateTime.now() + ".pdf")
            .body(pdf);
    }
}
```

### 4. Métodos de Filtrado en Repositories

**Archivo:** Modificar `AgentApprovalRepository.java`

```java
@Query("SELECT a FROM AgentApproval a WHERE " +
       "(:startDate IS NULL OR a.agtcreatedat >= :startDate) AND " +
       "(:endDate IS NULL OR a.agtcreatedat <= :endDate) AND " +
       "(:status IS NULL OR a.agtapprovalstatus = :status) AND " +
       "(:approver IS NULL OR a.agtapprovername = :approver) " +
       "ORDER BY a.agtcreatedat DESC")
List<AgentApproval> findByFilters(
    @Param("startDate") Timestamp startDate,
    @Param("endDate") Timestamp endDate,
    @Param("status") String status,
    @Param("approver") String approver);
```

---

## TESTING

### 1. Test de Exportación JSON

```java
@Test
public void testExportApprovalsAsJson_DeberiaRetornarListaCompleta() {
    // Given
    Timestamp start = Timestamp.valueOf(LocalDateTime.now().minusDays(30));
    Timestamp end = Timestamp.valueOf(LocalDateTime.now());
    
    // When
    List<ApprovalExportDto> exports = exportService.exportApprovalsAsJson(
        null, start, end, null, null);
    
    // Then
    assertFalse(exports.isEmpty());
    exports.forEach(dto -> {
        assertNotNull(dto.getExportHash());
        assertNotNull(dto.getExportTimestamp());
    });
}
```

---

## MÉTRICAS DE ÉXITO

- **100%** de evidencia exportable en < 5 minutos
- **3 formatos** disponibles (JSON, XML, PDF)
- **100%** de registros con hash de integridad

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md#inc-hitl-004`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_SUPERVISION_HUMANA_HITL.md`
- **EU AI Act Art. 14.4:** Transparency

---

**Estado:** ✅ COMPLETADO  
**Esfuerzo Estimado:** 5-7 días  
**Responsable:** Backend Team + Frontend Team  
**Fecha Límite:** 1 mes

