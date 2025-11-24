package com.codeflowx.platform.viewmodel.datasources;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.entity.datasources.DataSourceDocument;
import com.codeflowx.govern.service.datasources.DataSourceDocumentService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import codeflowx.nocode.persist.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para gestión de Documentos de Data Sources
 */
@Slf4j
@Getter
@Setter
@Init(superclass = true)
@VariableResolver(DelegatingVariableResolver.class)
public class DataSourceDocumentViewModel extends BaseFront<DataSourceDocumentViewModel> {

    private static final long serialVersionUID = 1L;

    @Override
    public void setBeans(Object bean) {}

    @WireVariable
    private DataSourceDocumentService dataSourceDocumentService;

    // ========== Datos del formulario ==========
    private String docDataSourceName;
    private String docName;
    private String docDescription;
    private String docType = "PDF";
    private boolean extractText = true;
    private boolean extractMetadata = true;
    private boolean generateEmbeddings = false;
    private boolean indexForSearch = false;

    // ========== Archivos seleccionados ==========
    private List<UploadedFile> selectedFiles = new ArrayList<>();

    // ========== Processing Status ==========
    private boolean processingStatusVisible = false;
    private String processingStatusType = "info";
    private String processingStatusIcon = "info-circle";
    private String processingStatusTitle = "";
    private String processingStatusMessage = "";
    private boolean processingProgressVisible = false;
    private int processingProgress = 0;

    // ========== Lista de Documentos ==========
    private List<DataSourceDocument> documentsList = new ArrayList<>();
    private PageParams pageParams;
    private PageResult<DataSourceDocument> pageResult;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);

        pageParams = PageParams.builder()
            .maxRows(20)
            .pageActual(1)
            .rowActual(0)
            .build();

        loadDocuments();
    }

    @Command
    @NotifyChange("*")
    public void loadDocuments() {
        try {
            pageResult = dataSourceDocumentService.findAll(pageParams);

            if (pageResult != null && pageResult.getContent() != null) {
                documentsList = pageResult.getContent();

                // Auditar búsqueda
                logActivity("BUSCAR", "DATASOURCEDOCUMENTS", null,
                    "Búsqueda: " + documentsList.size() + " documentos");
            } else {
                documentsList = new ArrayList<>();
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar documentos", e);
            Messagebox.show("Error al cargar documentos: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("*")
    public void uploadDocuments() {
        // TODO: Implementar upload de documentos
        log.info("Upload documents");
    }

    @Command
    @NotifyChange("*")
    public void selectFiles() {
        // TODO: Abrir selector de archivos
        log.info("Select files");
    }

    @Command
    @NotifyChange("*")
    public void removeFile(@BindingParam("file") UploadedFile file) {
        selectedFiles.remove(file);
    }

    @Command
    @NotifyChange("*")
    public void processUpload() {
        if (selectedFiles.isEmpty()) {
            Messagebox.show("No hay archivos seleccionados", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        try {
            processingStatusVisible = true;
            processingProgressVisible = true;
            processingStatusType = "info";
            processingStatusIcon = "spinner";
            processingStatusTitle = "Procesando documentos";
            processingStatusMessage = "Subiendo y procesando " + selectedFiles.size() + " archivo(s)...";
            processingProgress = 0;

            // TODO: Implementar integración con leka-server para procesamiento de documentos

            // Simular procesamiento
            for (UploadedFile file : selectedFiles) {
                DataSourceDocument doc = new DataSourceDocument();
                doc.setDocname(file.getName());
                doc.setDoctype(docType);
                doc.setDocfilesize((long) file.getSize());
                doc.setDocstatus("UPLOADED");
                doc.setDocprocessingstatus("PENDING");
                doc.setDocuploadedat(new Timestamp(System.currentTimeMillis()));
                doc.setDoccreatedat(new Timestamp(System.currentTimeMillis()));

                dataSourceDocumentService.create(doc);

                // Auditar creación
                logActivity("CREAR", "DATASOURCEDOCUMENTS", doc.getIdxdatasourcedocument(),
                    "Documento subido: " + doc.getDocname());
            }

            processingStatusType = "success";
            processingStatusIcon = "check-circle";
            processingStatusTitle = "Procesamiento completado";
            processingStatusMessage = selectedFiles.size() + " archivo(s) procesados exitosamente";
            processingProgress = 100;

            selectedFiles.clear();
            loadDocuments();

        } catch (Exception e) {
            log.error("Error al procesar upload", e);
            processingStatusType = "danger";
            processingStatusIcon = "times-circle";
            processingStatusTitle = "Error";
            processingStatusMessage = e.getMessage();
        }
    }

    @Command
    @NotifyChange("*")
    public void previewDocuments() {
        Messagebox.show("Funcionalidad de preview en desarrollo", "Info", Messagebox.OK, Messagebox.INFORMATION);
    }

    @Command
    @NotifyChange("*")
    public void validateDocuments() {
        Messagebox.show("Funcionalidad de validación en desarrollo", "Info", Messagebox.OK, Messagebox.INFORMATION);
    }

    @Command
    @NotifyChange("*")
    public void clearSelection() {
        selectedFiles.clear();
        processingStatusVisible = false;
    }

    @Command
    @NotifyChange("*")
    public void refreshDocuments() {
        loadDocuments();
    }

    @Command
    public void viewDocument(@BindingParam("doc") DataSourceDocument doc) {
        log.info("Ver documento: {}", doc.getDocname());
    }

    @Command
    public void downloadDocument(@BindingParam("doc") DataSourceDocument doc) {
        log.info("Descargar documento: {}", doc.getDocname());
    }

    @Command
    @NotifyChange("*")
    public void reprocessDocument(@BindingParam("doc") DataSourceDocument doc) {
        try {
            // TODO: Integración con leka-server para reprocesar
            Messagebox.show("Reprocesando documento: " + doc.getDocname(), "Info", Messagebox.OK, Messagebox.INFORMATION);
        } catch (Exception e) {
            log.error("Error al reprocesar documento", e);
        }
    }

    @Command
    @NotifyChange("*")
    public void deleteDocument(@BindingParam("doc") DataSourceDocument doc) {
        Messagebox.show("¿Está seguro de eliminar el documento: " + doc.getDocname() + "?",
            "Confirmar", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                    try {
                        dataSourceDocumentService.deleteById(doc.getIdxdatasourcedocument());

                        // Auditar eliminación
                        logActivity("ELIMINAR", "DATASOURCEDOCUMENTS", doc.getIdxdatasourcedocument(),
                            "Documento eliminado: " + doc.getDocname());

                        loadDocuments();
                        Messagebox.show("Documento eliminado exitosamente",
                            "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                    } catch (GovernanceServiceException e) {
                        log.error("Error al eliminar documento", e);
                        Messagebox.show("Error al eliminar documento: " + e.getMessage(),
                            "Error", Messagebox.OK, Messagebox.ERROR);
                    }
                }
            });
    }

    @Command
    public void exportDocuments() {
        log.info("Exportar documentos");
    }

    // ========== Métodos auxiliares ==========

    public String getDocStatusColor(String status) {
        if (status == null) return "secondary";
        switch (status) {
            case "UPLOADED": return "info";
            case "PROCESSING": return "warning";
            case "PROCESSED": return "success";
            case "ERROR": return "danger";
            default: return "secondary";
        }
    }

    public String getProcessingStatusColor(String status) {
        if (status == null) return "secondary";
        switch (status) {
            case "PENDING": return "secondary";
            case "IN_PROGRESS": return "warning";
            case "COMPLETED": return "success";
            case "FAILED": return "danger";
            default: return "secondary";
        }
    }

    public String formatFileSize(Long size) {
        if (size == null) return "-";
        if (size < 1024) return size + " B";
        if (size < 1024 * 1024) return String.format("%.2f KB", size / 1024.0);
        return String.format("%.2f MB", size / (1024.0 * 1024.0));
    }

    public String formatDate(Timestamp timestamp) {
        if (timestamp == null) return "-";
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(timestamp);
    }

    @Destroy
    public void destroy() {
        if (documentsList != null) {
            documentsList.clear();
            documentsList = null;
        }
        if (selectedFiles != null) {
            selectedFiles.clear();
            selectedFiles = null;
        }
        pageResult = null;
        pageParams = null;
        dataSourceDocumentService = null;
    }

    // Inner class para archivos subidos
    @Getter
    @Setter
    @AllArgsConstructor
    public static class UploadedFile {
        private String name;
        private String type;
        private int size;
    }
}
