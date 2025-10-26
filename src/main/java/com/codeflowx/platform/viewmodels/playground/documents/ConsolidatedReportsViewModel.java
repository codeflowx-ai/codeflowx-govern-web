package com.codeflowx.platform.viewmodels.playground.documents;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

/**
 * ViewModel para Reportes Consolidados
 * Muestra reportes de múltiples documentos procesados en batch
 * Endpoint: GET /api/v1/document/jobs/{job_id}/result
 */
@Slf4j
@VariableResolver(DelegatingVariableResolver.class)
public class ConsolidatedReportsViewModel {

    @WireVariable
    private RestTemplate restTemplate;

    private static final String DOCUMENTS_SERVICE_URL = "http://localhost:8004";

    @Getter
    @Setter
    private String jobId;

    @Getter
    @Setter
    private String reportTitle;

    @Getter
    @Setter
    private Date generationDate;

    @Getter
    @Setter
    private int documentsProcessed = 0;

    @Getter
    @Setter
    private double totalProcessingTime = 0.0;

    @Getter
    @Setter
    private String executiveSummary;

    @Getter
    private List<DocumentAnalysis> documentAnalyses = new ArrayList<>();

    @Getter
    @Setter
    private boolean loading = false;

    @Getter
    @Setter
    private String errorMessage = "";

    @Init
    public void init(@QueryParam("jobId") String jobId) {
        this.jobId = jobId;
        log.info("✅ Consolidated Reports ViewModel initialized for job: {}", jobId);
        if (jobId != null && !jobId.isEmpty()) {
            loadReport();
        }
    }

    /**
     * Carga el reporte consolidado del job
     */
    @Command
    @NotifyChange("*")
    public void loadReport() {
        loading = true;
        errorMessage = "";

        try {
            String url = DOCUMENTS_SERVICE_URL + "/api/v1/document/jobs/" + jobId + "/result";
            
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> body = response.getBody();
                
                reportTitle = (String) body.getOrDefault("title", "Consolidated Report");
                documentsProcessed = ((Number) body.getOrDefault("documents_processed", 0)).intValue();
                totalProcessingTime = ((Number) body.getOrDefault("total_processing_time", 0.0)).doubleValue();
                executiveSummary = (String) body.get("executive_summary");
                generationDate = new Date();

                log.info("✅ Report loaded: {}", reportTitle);
            }

        } catch (Exception e) {
            log.error("❌ Error loading report", e);
            errorMessage = "Error al cargar reporte: " + e.getMessage();
        } finally {
            loading = false;
        }
    }

    /**
     * Exporta el reporte como PDF
     */
    @Command
    public void exportAsPdf() {
        log.info("📤 Exporting report as PDF: {}", jobId);
        // TODO: Implementar exportación PDF
    }

    /**
     * Exporta el reporte como Word
     */
    @Command
    public void exportAsWord() {
        log.info("📤 Exporting report as Word: {}", jobId);
        // TODO: Implementar exportación Word
    }

    @Getter
    @Setter
    public static class DocumentAnalysis {
        private String documentId;
        private String filename;
        private String summary;
        private Map<String, Object> metrics;
    }
}

