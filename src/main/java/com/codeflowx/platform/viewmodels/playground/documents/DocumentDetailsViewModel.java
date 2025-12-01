package com.codeflowx.platform.viewmodels.playground.documents;
import com.codeflowx.framework.zkoss.BaseFront;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

/**
 * ViewModel para Pantalla de Detalles de Documento
 * Muestra información completa de un documento procesado con pestañas
 * Endpoints: GET /api/v1/document/{document_id} y /structure
 */
@Slf4j
@VariableResolver(DelegatingVariableResolver.class)
public class DocumentDetailsViewModel extends BaseFront<DocumentDetailsViewModel> {

    @WireVariable
    private RestTemplate restTemplate;

    private static final String DOCUMENTS_SERVICE_URL = "http://localhost:8004";

    @Getter
    @Setter
    private String documentId;

    @Getter
    @Setter
    private String filename;

    @Getter
    @Setter
    private String fileType;

    @Getter
    @Setter
    private long fileSize;

    @Getter
    @Setter
    private String status;

    @Getter
    @Setter
    private Date processingDate;

    @Getter
    @Setter
    private String extractedText;

    @Getter
    private List<TableData> extractedTables = new ArrayList<>();

    @Getter
    private List<ImageData> extractedImages = new ArrayList<>();

    @Getter
    private Map<String, Object> metadata = new HashMap<>();

    @Getter
    private Map<String, Object> structure = new HashMap<>();

    @Getter
    @Setter
    private boolean loading = false;

    @Getter
    @Setter
    private String errorMessage = "";

    @Getter
    @Setter
    private double confidenceScore = 0.0;

    @Init
    public void init(@QueryParam("documentId") String documentId) {
        this.documentId = documentId;
        log.info("✅ Document Details ViewModel initialized for: {}", documentId);
        if (documentId != null && !documentId.isEmpty()) {
            loadDocumentDetails();
            loadDocumentStructure();
        }
    }

    /**
     * Carga los detalles del documento
     */
    @Command
    @NotifyChange("*")
    public void loadDocumentDetails() {
        loading = true;
        errorMessage = "";

        try {
            String url = DOCUMENTS_SERVICE_URL + "/api/v1/document/" + documentId;
            
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> body = response.getBody();
                
                // Metadata
                Map<String, Object> meta = (Map<String, Object>) body.get("metadata");
                filename = (String) meta.get("filename");
                fileType = (String) meta.get("file_type");
                fileSize = ((Number) meta.get("file_size")).longValue();
                
                // Content
                Map<String, Object> content = (Map<String, Object>) body.get("content");
                extractedText = (String) content.get("text");
                confidenceScore = ((Number) content.getOrDefault("confidence_score", 0.0)).doubleValue();
                
                // Tables
                List<Map<String, Object>> tables = (List<Map<String, Object>>) content.get("tables");
                if (tables != null) {
                    extractedTables.clear();
                    for (Map<String, Object> table : tables) {
                        TableData td = new TableData();
                        td.setIndex(extractedTables.size() + 1);
                        td.setData(table);
                        extractedTables.add(td);
                    }
                }

                log.info("✅ Document details loaded: {}", filename);
            }

        } catch (Exception e) {
            log.error("❌ Error loading document details", e);
            errorMessage = "Error al cargar documento: " + e.getMessage();
        } finally {
            loading = false;
        }
    }

    /**
     * Carga la estructura del documento
     */
    @Command
    @NotifyChange("structure")
    public void loadDocumentStructure() {
        try {
            String url = DOCUMENTS_SERVICE_URL + "/api/v1/document/" + documentId + "/structure";
            
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                structure = response.getBody();
                log.info("✅ Document structure loaded");
            }

        } catch (Exception e) {
            log.error("❌ Error loading document structure", e);
        }
    }

    /**
     * Exporta el documento como TXT
     */
    @Command
    public void exportAsTxt() {
        // TODO: Implementar exportación
        Messagebox.show("Exportar como TXT: " + filename, "Export", Messagebox.OK, Messagebox.INFORMATION);
    }

    /**
     * Exporta el documento como JSON
     */
    @Command
    public void exportAsJson() {
        // TODO: Implementar exportación
        Messagebox.show("Exportar como JSON: " + filename, "Export", Messagebox.OK, Messagebox.INFORMATION);
    }

    @Getter
    @Setter
    public static class TableData {
        private int index;
        private Map<String, Object> data;
    }

    @Getter
    @Setter
    public static class ImageData {
        private String url;
        private String description;
        private Map<String, Object> metadata;
    }
}

