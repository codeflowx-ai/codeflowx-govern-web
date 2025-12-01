package com.codeflowx.platform.viewmodels.playground.documents;
import com.codeflowx.framework.zkoss.BaseFront;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

/**
 * ViewModel para Búsqueda Avanzada de Documentos
 * Permite búsqueda por contenido y metadatos con filtros combinados
 * Endpoint: GET /api/v1/document/search
 */
@Slf4j
@VariableResolver(DelegatingVariableResolver.class)
public class DocumentSearchViewModel extends BaseFront<DocumentSearchViewModel> {

    @WireVariable
    private RestTemplate restTemplate;

    private static final String DOCUMENTS_SERVICE_URL = "http://localhost:8004";

    @Getter
    @Setter
    private String searchQuery = "";

    @Getter
    @Setter
    private String filterFileType = "";

    @Getter
    @Setter
    private Date filterDateFrom;

    @Getter
    @Setter
    private Date filterDateTo;

    @Getter
    private List<SearchResult> searchResults = new ArrayList<>();

    @Getter
    private List<String> searchHistory = new ArrayList<>();

    @Getter
    @Setter
    private boolean loading = false;

    @Getter
    @Setter
    private String errorMessage = "";

    @Init
    public void init() {
        log.info("✅ Document Search ViewModel initialized");
    }

    /**
     * Ejecuta la búsqueda de documentos
     */
    @Command
    @NotifyChange({"searchResults", "searchHistory", "loading", "errorMessage"})
    public void executeSearch() {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            return;
        }

        loading = true;
        errorMessage = "";

        try {
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(DOCUMENTS_SERVICE_URL + "/api/v1/document/search")
                    .queryParam("query", searchQuery)
                    .queryParam("limit", 100);

            if (filterFileType != null && !filterFileType.isEmpty()) {
                builder.queryParam("file_types", filterFileType);
            }

            String url = builder.toUriString();
            
            log.info("📤 Searching documents: {}", searchQuery);
            
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> body = response.getBody();
                List<Map<String, Object>> results = (List<Map<String, Object>>) body.get("results");
                
                searchResults.clear();
                for (Map<String, Object> result : results) {
                    SearchResult sr = new SearchResult();
                    sr.setDocumentId((String) result.get("document_id"));
                    sr.setFilename((String) result.get("filename"));
                    sr.setSnippet((String) result.get("snippet"));
                    sr.setRelevance(((Number) result.get("relevance")).doubleValue());
                    searchResults.add(sr);
                }

                // Agregar a historial
                if (!searchHistory.contains(searchQuery)) {
                    searchHistory.add(0, searchQuery);
                    if (searchHistory.size() > 10) {
                        searchHistory = new ArrayList<>(searchHistory.subList(0, 10));
                    }
                }

                log.info("✅ Found {} results", searchResults.size());
            }

        } catch (Exception e) {
            log.error("❌ Error searching documents", e);
            errorMessage = "Error en búsqueda: " + e.getMessage();
        } finally {
            loading = false;
        }
    }

    @Getter
    @Setter
    public static class SearchResult {
        private String documentId;
        private String filename;
        private String snippet;
        private double relevance;
    }
}

