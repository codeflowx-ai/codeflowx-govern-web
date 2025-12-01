package com.codeflowx.platform.viewmodels.playground.documents;
import com.codeflowx.framework.zkoss.BaseFront;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.zkoss.bind.annotation.*;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

/**
 * ViewModel para Pantalla de Carga de Documentos
 * Permite subir documentos individuales o múltiples para procesamiento
 * Endpoint: POST /api/v1/document/extract o POST /api/v1/document/batch-extract
 */
@Slf4j
@VariableResolver(DelegatingVariableResolver.class)
public class DocumentUploadViewModel extends BaseFront<DocumentUploadViewModel> {

    @WireVariable
    private RestTemplate restTemplate;

    // URL base del microservicio
    private static final String DOCUMENTS_SERVICE_URL = "http://localhost:8004";
    private static final int MAX_FILES = 20;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "docx", "doc", "pptx", "ppt", "xlsx", "xls", "txt", "md");

    // Lista de archivos seleccionados
    @Getter
    private List<DocumentFile> selectedFiles = new ArrayList<>();

    // Opciones de procesamiento
    @Getter
    @Setter
    private boolean preserveFormat = true;

    @Getter
    @Setter
    private boolean extractTables = true;

    @Getter
    @Setter
    private boolean extractImages = true;

    @Getter
    @Setter
    private String consolidationStrategy = "concat";

    // Estado de procesamiento
    @Getter
    @Setter
    private boolean processing = false;

    @Getter
    @Setter
    private int uploadProgress = 0;

    @Getter
    @Setter
    private String statusMessage = "";

    @Getter
    @Setter
    private String jobId = null;

    @Init
    public void init() {
        log.info("✅ Document Upload ViewModel initialized");
    }

    /**
     * Maneja la carga de archivos (múltiples)
     */
    @Command
    @NotifyChange({"selectedFiles", "statusMessage"})
    public void onFileUpload(@BindingParam("event") UploadEvent event) {
        try {
            Media[] medias = event.getMedias();
            
            if (medias == null || medias.length == 0) {
                Messagebox.show("No se seleccionaron archivos", "Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }

            // Validar límite de archivos
            if (selectedFiles.size() + medias.length > MAX_FILES) {
                Messagebox.show("Máximo " + MAX_FILES + " archivos permitidos", "Error", Messagebox.OK, Messagebox.ERROR);
                return;
            }

            for (Media media : medias) {
                // Validar extensión
                String filename = media.getName();
                String extension = getFileExtension(filename).toLowerCase();
                
                if (!ALLOWED_EXTENSIONS.contains(extension)) {
                    log.warn("⚠️ Archivo rechazado (extensión no válida): {}", filename);
                    continue;
                }

                // Validar tamaño (max 50MB por archivo)
                if (media.getByteData().length > 50 * 1024 * 1024) {
                    Messagebox.show("El archivo " + filename + " excede el tamaño máximo de 50MB", 
                                    "Error", Messagebox.OK, Messagebox.ERROR);
                    continue;
                }

                DocumentFile docFile = new DocumentFile();
                docFile.setFilename(filename);
                docFile.setFileType(extension);
                docFile.setFileSize(media.getByteData().length);
                docFile.setContent(media.getByteData());
                docFile.setStatus("Pendiente");

                selectedFiles.add(docFile);
                log.info("✅ Archivo agregado: {} ({} bytes)", filename, docFile.getFileSize());
            }

            statusMessage = selectedFiles.size() + " archivo(s) seleccionado(s)";

        } catch (Exception e) {
            log.error("❌ Error al cargar archivos", e);
            Messagebox.show("Error al cargar archivos: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Procesa los documentos (individual o batch)
     */
    @Command
    @NotifyChange({"processing", "uploadProgress", "statusMessage", "jobId"})
    public void processDocuments() {
        if (selectedFiles.isEmpty()) {
            Messagebox.show("Seleccione al menos un archivo", "Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        processing = true;
        uploadProgress = 0;
        statusMessage = "Procesando documentos...";

        try {
            if (selectedFiles.size() == 1) {
                // Procesamiento individual
                processSingleDocument();
            } else {
                // Procesamiento batch
                processBatchDocuments();
            }

        } catch (Exception e) {
            log.error("❌ Error al procesar documentos", e);
            statusMessage = "Error: " + e.getMessage();
            processing = false;
            Messagebox.show("Error al procesar documentos: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Procesa un solo documento
     */
    private void processSingleDocument() {
        try {
            DocumentFile file = selectedFiles.get(0);
            
            String url = DOCUMENTS_SERVICE_URL + "/api/v1/document/extract";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            
            // Archivo
            ByteArrayResource fileResource = new ByteArrayResource(file.getContent()) {
                @Override
                public String getFilename() {
                    return file.getFilename();
                }
            };
            body.add("file", fileResource);

            // Opciones
            body.add("preserve_format", preserveFormat);
            body.add("extract_tables", extractTables);
            body.add("extract_images", extractImages);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            log.info("📤 Enviando documento: {}", file.getFilename());
            uploadProgress = 50;

            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

            uploadProgress = 100;
            
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> result = response.getBody();
                String documentId = (String) result.get("document_id");
                
                file.setStatus("Completado");
                file.setDocumentId(documentId);
                
                statusMessage = "✅ Documento procesado correctamente";
                processing = false;
                
                log.info("✅ Documento procesado: {} → ID: {}", file.getFilename(), documentId);
                
                Messagebox.show("Documento procesado correctamente\nID: " + documentId, 
                                "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }

        } catch (Exception e) {
            log.error("❌ Error en procesamiento individual", e);
            throw new RuntimeException("Error al procesar documento: " + e.getMessage());
        }
    }

    /**
     * Procesa múltiples documentos en batch
     */
    private void processBatchDocuments() {
        try {
            String url = DOCUMENTS_SERVICE_URL + "/api/v1/document/batch-extract";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            // Archivos
            for (DocumentFile file : selectedFiles) {
                ByteArrayResource fileResource = new ByteArrayResource(file.getContent()) {
                    @Override
                    public String getFilename() {
                        return file.getFilename();
                    }
                };
                body.add("files", fileResource);
            }

            // Opciones
            body.add("preserve_format", preserveFormat);
            body.add("extract_tables", extractTables);
            body.add("extract_images", extractImages);
            body.add("consolidation_strategy", consolidationStrategy);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            log.info("📤 Enviando batch de {} documentos", selectedFiles.size());
            uploadProgress = 30;

            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

            uploadProgress = 100;

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> result = response.getBody();
                jobId = (String) result.get("job_id");
                
                statusMessage = "✅ Job creado: " + jobId;
                processing = false;
                
                log.info("✅ Batch job creado: {}", jobId);
                
                Messagebox.show("Procesamiento iniciado\nJob ID: " + jobId + "\n\nPuede ver el progreso en la pantalla de estado", 
                                "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }

        } catch (Exception e) {
            log.error("❌ Error en procesamiento batch", e);
            throw new RuntimeException("Error al procesar batch: " + e.getMessage());
        }
    }

    /**
     * Elimina un archivo de la lista
     */
    @Command
    @NotifyChange({"selectedFiles", "statusMessage"})
    public void removeFile(@BindingParam("file") DocumentFile file) {
        selectedFiles.remove(file);
        statusMessage = selectedFiles.size() + " archivo(s) seleccionado(s)";
        log.info("🗑️ Archivo eliminado: {}", file.getFilename());
    }

    /**
     * Limpia la lista de archivos
     */
    @Command
    @NotifyChange({"selectedFiles", "statusMessage", "processing", "uploadProgress", "jobId"})
    public void clearFiles() {
        selectedFiles.clear();
        statusMessage = "";
        processing = false;
        uploadProgress = 0;
        jobId = null;
        log.info("🗑️ Lista de archivos limpiada");
    }

    /**
     * Obtiene la extensión del archivo
     */
    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);
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
     * Clase interna para representar un archivo
     */
    @Getter
    @Setter
    public static class DocumentFile {
        private String filename;
        private String fileType;
        private long fileSize;
        private byte[] content;
        private String status;
        private String documentId;
    }
}

