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
 * ViewModel para Dashboard de Estadísticas de Documentos
 * Muestra métricas generales y gráficos de tendencias
 * Endpoints: GET /api/v1/document/statistics/overview y /health
 */
@Slf4j
@VariableResolver(DelegatingVariableResolver.class)
public class StatisticsDashboardViewModel {

    @WireVariable
    private RestTemplate restTemplate;

    private static final String DOCUMENTS_SERVICE_URL = "http://localhost:8004";

    // KPIs
    @Getter
    @Setter
    private long totalDocuments = 0;

    @Getter
    @Setter
    private long documentsToday = 0;

    @Getter
    @Setter
    private double successRate = 0.0;

    @Getter
    @Setter
    private double avgProcessingTime = 0.0;

    @Getter
    private Map<String, Long> documentsByType = new LinkedHashMap<>();

    @Getter
    private List<ProcessingStat> processingStats = new ArrayList<>();

    @Getter
    @Setter
    private boolean loading = false;

    @Getter
    @Setter
    private String errorMessage = "";

    @Getter
    @Setter
    private String systemHealth = "UNKNOWN";

    @Init
    public void init() {
        log.info("✅ Statistics Dashboard ViewModel initialized");
        loadStatistics();
        checkHealth();
    }

    /**
     * Carga las estadísticas generales
     */
    @Command
    @NotifyChange("*")
    public void loadStatistics() {
        loading = true;
        errorMessage = "";

        try {
            String url = DOCUMENTS_SERVICE_URL + "/api/v1/document/statistics/overview";
            
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> body = response.getBody();
                
                totalDocuments = ((Number) body.getOrDefault("total_documents", 0)).longValue();
                documentsToday = ((Number) body.getOrDefault("documents_today", 0)).longValue();
                successRate = ((Number) body.getOrDefault("success_rate", 0.0)).doubleValue();
                avgProcessingTime = ((Number) body.getOrDefault("avg_processing_time", 0.0)).doubleValue();
                
                // Documents by type
                Map<String, Object> byType = (Map<String, Object>) body.get("by_type");
                if (byType != null) {
                    documentsByType.clear();
                    byType.forEach((k, v) -> documentsByType.put(k, ((Number) v).longValue()));
                }

                log.info("✅ Statistics loaded: {} total documents", totalDocuments);
            }

        } catch (Exception e) {
            log.error("❌ Error loading statistics", e);
            errorMessage = "Error al cargar estadísticas: " + e.getMessage();
        } finally {
            loading = false;
        }
    }

    /**
     * Verifica la salud del sistema
     */
    @Command
    @NotifyChange("systemHealth")
    public void checkHealth() {
        try {
            String url = DOCUMENTS_SERVICE_URL + "/health";
            
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> health = response.getBody();
                systemHealth = (String) health.getOrDefault("status", "UNKNOWN");
                log.info("✅ Health check: {}", systemHealth);
            }

        } catch (Exception e) {
            log.error("❌ Health check failed", e);
            systemHealth = "ERROR";
        }
    }

    @Getter
    @Setter
    public static class ProcessingStat {
        private String period;
        private long count;
        private double avgTime;
    }
}

