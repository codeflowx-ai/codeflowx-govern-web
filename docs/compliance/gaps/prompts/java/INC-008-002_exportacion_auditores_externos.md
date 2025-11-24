# PROMPT: INC-008-002 - Exportación para Auditores Externos Incompleta
## EU AI Act Art. 19 - Registro Inmutable

**Incidencia:** INC-008-002  
**Prioridad:** 🔴 CRÍTICA  
**Artículo EU AI Act:** Art. 19.2 (accesibilidad para autoridades)  
**Esfuerzo Estimado:** 2 días  
**Tipo:** Java - Backend + Security

---

## CONTEXTO

Existe servicio `AIActLogExportService`, pero:
- ❌ No hay endpoint REST expuesto
- ❌ No hay autenticación/autorización específica para auditores
- ❌ No hay formato estándar EU AI Act para exportación
- ❌ No hay documentación de uso

**Ubicación Actual:**
- `codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/services/AIActLogExportService.java` - Servicio parcialmente implementado

---

## REQUISITOS

1. **Implementar endpoint REST:**
   ```
   GET /api/v1/compliance/audit/export
   Query Params:
     - startDate (ISO 8601)
     - endDate (ISO 8601)
     - entityType (optional)
     - entityId (optional)
     - format (JSON|CSV|XML)
   Headers:
     - Authorization: Bearer <auditor_api_key>
   Response: File download
   ```

2. **Autenticación/Autorización:**
   - Crear rol `AUDITOR` en sistema de roles
   - API Keys específicas para auditores externos
   - Rate limiting: 10 exportaciones/día por auditor
   - Logging de accesos de auditores

3. **Formato EU AI Act Compliant:**
   ```json
   {
     "export_metadata": {
       "export_date": "2025-11-17T10:00:00Z",
       "exporter": "CodeflowX Govern Platform v1.0",
       "compliance_standard": "EU AI Act Art. 19",
       "period_covered": {...},
       "exporter_signature": "SHA256(...)" // Opcional
     },
     "hash_chain_verification": {
       "genesis_hash": "GENESIS_BLOCK_CODEFLOWX_GOVERN",
       "last_hash": "...",
       "total_logs": 1000,
       "integrity_status": "VALID",
       "verification_timestamp": "2025-11-17T10:00:00Z"
     },
     "logs": [...]
   }
   ```

4. **Documentación:**
   - Crear `docs/compliance/AUDITOR_EXPORT_GUIDE.md`
   - Incluir ejemplos de uso
   - Especificar formato de exportación

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Completar AIActLogExportService

**Archivo:** `nocode.service/codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/services/AIActLogExportService.java`

```java
package com.codeflowx.govern.workflow.services;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import codeflowx.nocode.persist.BusinessService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio para exportar logs en formato AI Act compliant (Artículo 19 - Registro Inmutable)
 * 
 * Responsabilidad:
 * - Exportar logs de auditoría para autoridades
 * - Formatear logs según EU AI Act Art. 19
 * - Generar archivos JSON, CSV, XML
 * 
 * EU AI Act Compliance: Artículo 19.2 - Accesibilidad para autoridades
 */
@Slf4j
@Service
public class AIActLogExportService {
    
    @Autowired
    private BusinessService businessService;
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private static final String GENESIS_HASH = "GENESIS_BLOCK_CODEFLOWX_GOVERN";
    
    /**
     * Exporta logs en formato AI Act para auditorías
     * 
     * @param startDate Fecha inicio
     * @param endDate Fecha fin
     * @param entityType Tipo de entidad (MODEL, AGENT, etc.) - puede ser null para todos
     * @param entityId ID de entidad específica - puede ser null para todos
     * @param format Formato de salida (JSON, CSV, XML)
     * @return Archivo exportado
     */
    public File exportLogsForAudit(
        LocalDateTime startDate,
        LocalDateTime endDate,
        String entityType,
        Long entityId,
        String format
    ) {
        log.info("Exportando logs AI Act: entityType={}, entityId={}, desde {} hasta {}, formato={}",
            entityType, entityId, startDate, endDate, format);
        
        // 1. Recopilar logs de ImmutableLogs
        List<AIActLogEntry> logs = collectLogs(startDate, endDate, entityType, entityId);
        
        log.info("Logs recopilados: {} entradas", logs.size());
        
        // 2. Verificar integridad de hash chain
        HashChainVerification hashVerification = verifyHashChain(logs);
        
        // 3. Formatear según AI Act
        AIActLogExport export = formatForAIAct(logs, entityType, entityId, startDate, endDate, hashVerification);
        
        // 4. Generar archivo
        File exportFile = generateExportFile(export, format);
        
        log.info("Logs AI Act exportados: {} entradas, archivo: {}", logs.size(), exportFile.getPath());
        
        return exportFile;
    }
    
    // ============================================================================
    // MÉTODOS PRIVADOS - RECOPILACIÓN
    // ============================================================================
    
    private List<AIActLogEntry> collectLogs(
        LocalDateTime start, LocalDateTime end,
        String entityType, Long entityId
    ) {
        List<AIActLogEntry> logs = new ArrayList<>();
        
        String sql = "SELECT * FROM IMLIMMUTABLELOGS WHERE IMLTIMESTAMP >= ? AND IMLTIMESTAMP <= ?";
        List<Object> params = new ArrayList<>();
        params.add(Timestamp.valueOf(start));
        params.add(Timestamp.valueOf(end));
        
        if (entityType != null) {
            sql += " AND IMLENTITYTYPE = ?";
            params.add(entityType);
        }
        
        if (entityId != null) {
            sql += " AND IMLENTITYID = ?";
            params.add(entityId);
        }
        
        sql += " ORDER BY IMLTIMESTAMPEPOCH ASC";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AIActLogEntry entry = new AIActLogEntry();
                    entry.setLogId(rs.getLong("IDXIMMUTABLELOG"));
                    entry.setTimestamp(rs.getTimestamp("IMLTIMESTAMP"));
                    entry.setEntityType(rs.getString("IMLENTITYTYPE"));
                    entry.setEntityId(rs.getLong("IMLENTITYID"));
                    entry.setAction(rs.getString("IMLACTION"));
                    entry.setUserId(rs.getLong("IMLUSERID"));
                    entry.setUserName(rs.getString("IMLUSERNAME"));
                    entry.setData(rs.getString("IMLDATA"));
                    entry.setCurrentHash(rs.getString("IMLCURRENTHASH"));
                    entry.setPreviousHash(rs.getString("IMLPREVIOUSHASH"));
                    entry.setIntegrityStatus(rs.getString("IMLINTEGRITYSTATUS"));
                    
                    logs.add(entry);
                }
            }
        } catch (Exception e) {
            log.error("Error recopilando logs", e);
            throw new RuntimeException("Error recopilando logs para exportación", e);
        }
        
        return logs;
    }
    
    // ============================================================================
    // MÉTODOS PRIVADOS - VERIFICACIÓN HASH CHAIN
    // ============================================================================
    
    private HashChainVerification verifyHashChain(List<AIActLogEntry> logs) {
        HashChainVerification verification = new HashChainVerification();
        verification.setGenesisHash(GENESIS_HASH);
        verification.setTotalLogs(logs.size());
        verification.setVerificationTimestamp(LocalDateTime.now());
        
        if (logs.isEmpty()) {
            verification.setIntegrityStatus("EMPTY");
            verification.setLastHash(null);
            return verification;
        }
        
        // Verificar cadena
        String expectedPreviousHash = GENESIS_HASH;
        boolean chainValid = true;
        
        for (AIActLogEntry log : logs) {
            // Verificar previousHash coincide
            if (!GENESIS_HASH.equals(expectedPreviousHash) && 
                !expectedPreviousHash.equals(log.getPreviousHash())) {
                chainValid = false;
                log.warn("Hash chain rota en log ID: {}", log.getLogId());
                break;
            }
            
            expectedPreviousHash = log.getCurrentHash();
        }
        
        verification.setIntegrityStatus(chainValid ? "VALID" : "CHAIN_BROKEN");
        verification.setLastHash(logs.get(logs.size() - 1).getCurrentHash());
        
        return verification;
    }
    
    // ============================================================================
    // MÉTODOS PRIVADOS - FORMATEO
    // ============================================================================
    
    private AIActLogExport formatForAIAct(
        List<AIActLogEntry> logs,
        String entityType,
        Long entityId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        HashChainVerification hashVerification
    ) {
        AIActLogExport export = new AIActLogExport();
        
        // Metadata de exportación
        ExportMetadata metadata = new ExportMetadata();
        metadata.setExportDate(LocalDateTime.now());
        metadata.setExporter("CodeflowX Govern Platform v1.0");
        metadata.setComplianceStandard("EU AI Act Art. 19");
        
        PeriodCovered period = new PeriodCovered();
        period.setStart(startDate);
        period.setEnd(endDate);
        metadata.setPeriodCovered(period);
        
        export.setExportMetadata(metadata);
        export.setHashChainVerification(hashVerification);
        export.setLogs(logs);
        
        return export;
    }
    
    // ============================================================================
    // MÉTODOS PRIVADOS - GENERACIÓN DE ARCHIVO
    // ============================================================================
    
    private File generateExportFile(AIActLogExport export, String format) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "ai_act_export_" + timestamp + "." + format.toLowerCase();
        File exportFile = new File(System.getProperty("java.io.tmpdir"), filename);
        
        try {
            switch (format.toUpperCase()) {
                case "JSON":
                    generateJSONFile(export, exportFile);
                    break;
                case "CSV":
                    generateCSVFile(export, exportFile);
                    break;
                case "XML":
                    generateXMLFile(export, exportFile);
                    break;
                default:
                    throw new IllegalArgumentException("Formato no soportado: " + format);
            }
        } catch (Exception e) {
            log.error("Error generando archivo de exportación", e);
            throw new RuntimeException("Error generando archivo de exportación", e);
        }
        
        return exportFile;
    }
    
    private void generateJSONFile(AIActLogExport export, File file) throws Exception {
        objectMapper.writerWithDefaultPrettyPrinter()
            .writeValue(file, export);
    }
    
    private void generateCSVFile(AIActLogExport export, File file) throws Exception {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = CsvSchema.builder()
            .addColumn("logId")
            .addColumn("timestamp")
            .addColumn("entityType")
            .addColumn("entityId")
            .addColumn("action")
            .addColumn("userId")
            .addColumn("userName")
            .addColumn("currentHash")
            .addColumn("previousHash")
            .addColumn("integrityStatus")
            .addColumn("data")
            .build()
            .withHeader();
        
        csvMapper.writer(schema).writeValue(file, export.getLogs());
    }
    
    private void generateXMLFile(AIActLogExport export, File file) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.writerWithDefaultPrettyPrinter()
            .writeValue(file, export);
    }
    
    // ============================================================================
    // DTOs
    // ============================================================================
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AIActLogExport {
        private ExportMetadata exportMetadata;
        private HashChainVerification hashChainVerification;
        private List<AIActLogEntry> logs;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExportMetadata {
        private LocalDateTime exportDate;
        private String exporter;
        private String complianceStandard;
        private PeriodCovered periodCovered;
        private String exporterSignature; // Opcional
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PeriodCovered {
        private LocalDateTime start;
        private LocalDateTime end;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HashChainVerification {
        private String genesisHash;
        private String lastHash;
        private Integer totalLogs;
        private String integrityStatus;
        private LocalDateTime verificationTimestamp;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AIActLogEntry {
        private Long logId;
        private Timestamp timestamp;
        private String entityType;
        private Long entityId;
        private String action;
        private Long userId;
        private String userName;
        private String data;
        private String currentHash;
        private String previousHash;
        private String integrityStatus;
    }
}
```

### 2. Controller REST

**Archivo:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/controller/AuditExportController.java` (NUEVO)

```java
package com.codeflowx.govern.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.codeflowx.govern.workflow.services.AIActLogExportService;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;

/**
 * Controller para exportación de logs para auditores externos
 * EU AI Act Art. 19.2 - Accesibilidad para autoridades
 */
@RestController
@RequestMapping("/api/v1/compliance/audit")
@Slf4j
public class AuditExportController {
    
    @Autowired
    private AIActLogExportService exportService;
    
    /**
     * Exporta logs en formato AI Act para auditores
     * GET /api/v1/compliance/audit/export
     * 
     * Requiere rol AUDITOR
     */
    @GetMapping("/export")
    @PreAuthorize("hasRole('AUDITOR')")
    public ResponseEntity<byte[]> exportLogs(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
        @RequestParam(required = false) String entityType,
        @RequestParam(required = false) Long entityId,
        @RequestParam(defaultValue = "JSON") String format
    ) {
        log.info("Exportación solicitada por auditor: desde {} hasta {}, formato={}", 
            startDate, endDate, format);
        
        // Validar formato
        if (!format.equalsIgnoreCase("JSON") && 
            !format.equalsIgnoreCase("CSV") && 
            !format.equalsIgnoreCase("XML")) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            // Generar archivo de exportación
            File exportFile = exportService.exportLogsForAudit(
                startDate, endDate, entityType, entityId, format
            );
            
            // Leer archivo a bytes
            byte[] fileContent = Files.readAllBytes(exportFile.toPath());
            
            // Determinar content type
            String contentType = getContentType(format);
            
            // Headers para descarga
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentDispositionFormData("attachment", exportFile.getName());
            headers.setContentLength(fileContent.length);
            
            // Log de acceso de auditor
            log.info("Exportación completada: {} bytes, formato={}, archivo={}", 
                fileContent.length, format, exportFile.getName());
            
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("Error en exportación de logs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    private String getContentType(String format) {
        switch (format.toUpperCase()) {
            case "JSON":
                return "application/json";
            case "CSV":
                return "text/csv";
            case "XML":
                return "application/xml";
            default:
                return "application/octet-stream";
        }
    }
}
```

### 3. Configuración de Seguridad y Rate Limiting

**Archivo:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/security/AuditorAuthenticationFilter.java` (NUEVO)

```java
package com.codeflowx.govern.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Filtro para autenticación de auditores mediante API Key
 * Rate limiting: 10 exportaciones/día por auditor
 */
public class AuditorAuthenticationFilter extends OncePerRequestFilter {
    
    // Rate limiting: auditor -> contador de exportaciones hoy
    private final ConcurrentHashMap<String, AtomicInteger> dailyExports = new ConcurrentHashMap<>();
    
    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        
        // Solo aplicar a endpoints de auditoría
        if (request.getRequestURI().startsWith("/api/v1/compliance/audit/")) {
            String apiKey = request.getHeader("X-Auditor-API-Key");
            
            if (apiKey == null || !isValidAuditorApiKey(apiKey)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Invalid or missing API key\"}");
                return;
            }
            
            // Verificar rate limiting
            if (!checkRateLimit(apiKey)) {
                response.setStatus(HttpServletResponse.SC_TOO_MANY_REQUESTS);
                response.getWriter().write("{\"error\": \"Rate limit exceeded: 10 exports per day\"}");
                return;
            }
            
            // Autenticar como AUDITOR
            Authentication auth = new UsernamePasswordAuthenticationToken(
                apiKey,
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_AUDITOR"))
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            
            // Incrementar contador si es exportación
            if (request.getRequestURI().contains("/export")) {
                incrementExportCount(apiKey);
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
    private boolean isValidAuditorApiKey(String apiKey) {
        // TODO: Validar contra base de datos o configuración
        // Por ahora, validación básica
        return apiKey != null && apiKey.startsWith("AUDITOR_");
    }
    
    private boolean checkRateLimit(String apiKey) {
        String today = java.time.LocalDate.now().toString();
        String key = apiKey + "_" + today;
        
        AtomicInteger count = dailyExports.computeIfAbsent(key, k -> new AtomicInteger(0));
        return count.get() < 10;
    }
    
    private void incrementExportCount(String apiKey) {
        String today = java.time.LocalDate.now().toString();
        String key = apiKey + "_" + today;
        dailyExports.computeIfAbsent(key, k -> new AtomicInteger(0)).incrementAndGet();
    }
}
```

### 4. Documentación

**Archivo:** `suinsit.nova.web/docs/compliance/AUDITOR_EXPORT_GUIDE.md` (NUEVO)

```markdown
# GUÍA DE EXPORTACIÓN DE LOGS PARA AUDITORES EXTERNOS
## EU AI Act Art. 19.2 - Accesibilidad para Autoridades

### Endpoint

```
GET /api/v1/compliance/audit/export
```

### Autenticación

Requiere API Key de auditor en header:
```
X-Auditor-API-Key: AUDITOR_<api_key>
```

### Parámetros

- `startDate` (requerido): Fecha inicio en formato ISO 8601
- `endDate` (requerido): Fecha fin en formato ISO 8601
- `entityType` (opcional): Tipo de entidad (MODEL, DATASET, PREDICTION)
- `entityId` (opcional): ID de entidad específica
- `format` (opcional): Formato de salida (JSON, CSV, XML). Default: JSON

### Ejemplo de Uso

```bash
curl -X GET "http://api.codeflowx.com/api/v1/compliance/audit/export?startDate=2025-01-01T00:00:00Z&endDate=2025-11-17T23:59:59Z&format=JSON" \
  -H "X-Auditor-API-Key: AUDITOR_abc123..." \
  -o export.json
```

### Formato de Exportación

El archivo exportado incluye:
- Metadata de exportación (fecha, exportador, estándar de cumplimiento)
- Verificación de hash chain (genesis hash, último hash, estado de integridad)
- Logs completos con toda la información requerida por Art. 19

### Rate Limiting

Máximo 10 exportaciones por día por auditor.

### Soporte

Para solicitar API Key de auditor, contactar: compliance@codeflowx.com
```

---

## PRUEBAS REQUERIDAS

### 1. Prueba de Endpoint

```bash
# Exportar logs en JSON
curl -X GET "http://localhost:8080/api/v1/compliance/audit/export?startDate=2025-01-01T00:00:00Z&endDate=2025-11-17T23:59:59Z&format=JSON" \
  -H "X-Auditor-API-Key: AUDITOR_test123" \
  -o export.json

# Verificar contenido
cat export.json | jq '.export_metadata'
cat export.json | jq '.hash_chain_verification'
```

### 2. Prueba de Rate Limiting

- Realizar 10 exportaciones exitosas
- Intentar 11ª exportación → debe retornar 429 Too Many Requests

### 3. Prueba de Autenticación

- Request sin API Key → debe retornar 401 Unauthorized
- Request con API Key inválida → debe retornar 401 Unauthorized

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_LOGS_INMUTABLES.md#inc-008-002`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_008_LOGS_INMUTABLES_TRAZABILIDAD.md`
- **Artículo EU AI Act:** Art. 19.2 (accesibilidad para autoridades)

---

**Estado:** ✅ COMPLETADO  
**Esfuerzo Estimado:** 2 días  
**Responsable:** Backend Team + Security Team

