package com.codeflowx.platform.viewmodels.playground.documents;

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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ViewModel para Pantalla de Lista de Documentos
 * Muestra todos los documentos procesados con filtros y paginación
 * Endpoint: GET /api/v1/document/list
 */
@Slf4j
@VariableResolver(DelegatingVariableResolver.class)
public class DocumentListViewModel {

    @WireVariable
    private RestTemplate restTemplate;

    private static final String DOCUMENTS_SERVICE_URL = "http://localhost:8004";
    private static final int PAGE_SIZE = 50;

    // Lista de documentos
    @Getter
    private List<Document> documents = new ArrayList<>();

    @Getter
    private List<Document> filteredDocuments = new ArrayList<>();

    @Getter
    @Setter
    private Document selectedDocument;

    // Filtros
    @Getter
    @Setter
    private String searchQuery = "";

    @Getter
    @Setter
    private boolean filterPdf = true;

    @Getter
    @Setter
    private boolean filterDocx = true;

    @Getter
    @Setter
    private boolean filterPptx = true;

    @Getter
    @Setter
    private boolean filterXlsx = true;

    @Getter
    @Setter
    private boolean filterTxt = true;

    @Getter
    @Setter
    private String filterStatus = "ALL";

    @Getter
    @Setter
    private Date filterDateFrom;

    @Getter
    @Setter
    private Date filterDateTo;

    // Paginación
    @Getter
    @Setter
    private int currentPage = 0;

    @Getter
    @Setter
    private int totalPages = 0;

    // Estado
    @Getter
    @Setter
    private boolean loading = false;

    @Getter
    @Setter
    private String errorMessage = "";

    // Ordenamiento
    @Getter
    @Setter
    private String sortField = "date";

    @Getter
    @Setter
    private boolean sortAscending = false;

    @Init
    public void init() {
        log.info("✅ Document List ViewModel initialized");
        loadDocuments();
    }

    /**
     * Carga la lista de documentos desde el microservicio
     */
    @Command
    @NotifyChange({"documents", "filteredDocuments", "loading", "errorMessage", "totalPages"})
    public void loadDocuments() {
        loading = true;
        errorMessage = "";

        try {
            // Construir URL con parámetros
            StringBuilder urlBuilder = new StringBuilder(DOCUMENTS_SERVICE_URL + "/api/v1/document/list");
            urlBuilder.append("?limit=").append(PAGE_SIZE);
            urlBuilder.append("&offset=").append(currentPage * PAGE_SIZE);

            // Agregar filtros de tipo
            List<String> fileTypes = new ArrayList<>();
            if (filterPdf) fileTypes.add("pdf");
            if (filterDocx) fileTypes.add("docx");
            if (filterPptx) fileTypes.add("pptx");
            if (filterXlsx) fileTypes.add("xlsx");
            if (filterTxt) fileTypes.add("txt");

            if (!fileTypes.isEmpty()) {
                urlBuilder.append("&file_types=").append(String.join(",", fileTypes));
            }

            // Agregar filtro de estado
            if (!"ALL".equals(filterStatus)) {
                urlBuilder.append("&status=").append(filterStatus.toLowerCase());
            }

            String url = urlBuilder.toString();

            log.info("📤 Fetching documents from: {}", url);

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> body = response.getBody();
                List<Map<String, Object>> docs = (List<Map<String, Object>>) body.get("documents");
                Integer total = (Integer) body.get("total");

                documents.clear();
                for (Map<String, Object> docMap : docs) {
                    Document doc = new Document();
                    doc.setDocumentId((String) docMap.get("document_id"));
                    doc.setFilename((String) docMap.get("filename"));
                    doc.setFileType((String) docMap.get("file_type"));
                    doc.setFileSize(((Number) docMap.get("file_size")).longValue());
                    doc.setStatus((String) docMap.get("status"));
                    doc.setProcessingDate((String) docMap.get("processing_date"));
                    doc.setPageCount((Integer) docMap.get("pages_count"));

                    documents.add(doc);
                }

                totalPages = (int) Math.ceil(total.doubleValue() / PAGE_SIZE);
                
                applyFiltersAndSort();

                log.info("✅ Loaded {} documents (total: {})", documents.size(), total);
            }

        } catch (Exception e) {
            log.error("❌ Error loading documents", e);
            errorMessage = "Error al cargar documentos: " + e.getMessage();
        } finally {
            loading = false;
        }
    }

    /**
     * Aplica filtros locales y ordenamiento
     */
    @Command
    @NotifyChange("filteredDocuments")
    public void applyFiltersAndSort() {
        filteredDocuments = new ArrayList<>(documents);

        // Aplicar búsqueda por texto
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            String query = searchQuery.toLowerCase();
            filteredDocuments = filteredDocuments.stream()
                    .filter(doc -> doc.getFilename().toLowerCase().contains(query))
                    .collect(Collectors.toList());
        }

        // Aplicar ordenamiento
        Comparator<Document> comparator = getComparator();
        filteredDocuments.sort(comparator);

        log.info("📊 Applied filters: {} documents", filteredDocuments.size());
    }

    /**
     * Obtiene el comparador según el campo de ordenamiento
     */
    private Comparator<Document> getComparator() {
        Comparator<Document> comparator;

        switch (sortField) {
            case "name":
                comparator = Comparator.comparing(Document::getFilename);
                break;
            case "size":
                comparator = Comparator.comparing(Document::getFileSize);
                break;
            case "type":
                comparator = Comparator.comparing(Document::getFileType);
                break;
            case "date":
            default:
                comparator = Comparator.comparing(Document::getProcessingDate).reversed();
                break;
        }

        if (!sortAscending && !"date".equals(sortField)) {
            comparator = comparator.reversed();
        }

        return comparator;
    }

    /**
     * Cambia el ordenamiento
     */
    @Command
    @NotifyChange("filteredDocuments")
    public void changeSort(@BindingParam("field") String field) {
        if (sortField.equals(field)) {
            sortAscending = !sortAscending;
        } else {
            sortField = field;
            sortAscending = true;
        }
        applyFiltersAndSort();
    }

    /**
     * Búsqueda de documentos
     */
    @Command
    @NotifyChange("filteredDocuments")
    public void search() {
        applyFiltersAndSort();
    }

    /**
     * Navega a la página siguiente
     */
    @Command
    @NotifyChange({"currentPage", "documents", "filteredDocuments"})
    public void nextPage() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            loadDocuments();
        }
    }

    /**
     * Navega a la página anterior
     */
    @Command
    @NotifyChange({"currentPage", "documents", "filteredDocuments"})
    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            loadDocuments();
        }
    }

    /**
     * Elimina un documento
     */
    @Command
    @NotifyChange({"documents", "filteredDocuments", "selectedDocument"})
    public void deleteDocument(@BindingParam("document") Document document) {
        Messagebox.show("¿Está seguro de eliminar este documento?", "Confirmar", 
                Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
                event -> {
                    if (Messagebox.ON_OK.equals(event.getName())) {
                        performDelete(document);
                    }
                });
    }

    /**
     * Ejecuta la eliminación del documento
     */
    private void performDelete(Document document) {
        try {
            String url = DOCUMENTS_SERVICE_URL + "/api/v1/document/" + document.getDocumentId();
            
            restTemplate.delete(url);
            
            documents.remove(document);
            applyFiltersAndSort();
            
            log.info("✅ Document deleted: {}", document.getDocumentId());
            Messagebox.show("Documento eliminado correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);

        } catch (Exception e) {
            log.error("❌ Error deleting document", e);
            Messagebox.show("Error al eliminar documento: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Limpia todos los filtros
     */
    @Command
    @NotifyChange({"searchQuery", "filterStatus", "filterDateFrom", "filterDateTo", 
                   "filterPdf", "filterDocx", "filterPptx", "filterXlsx", "filterTxt"})
    public void clearFilters() {
        searchQuery = "";
        filterStatus = "ALL";
        filterDateFrom = null;
        filterDateTo = null;
        filterPdf = true;
        filterDocx = true;
        filterPptx = true;
        filterXlsx = true;
        filterTxt = true;
        loadDocuments();
    }

    /**
     * Formatea el tamaño del archivo
     */
    public String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }

    /**
     * Clase para representar un documento
     */
    @Getter
    @Setter
    public static class Document {
        private String documentId;
        private String filename;
        private String fileType;
        private long fileSize;
        private String status;
        private String processingDate;
        private Integer pageCount;
    }
}

