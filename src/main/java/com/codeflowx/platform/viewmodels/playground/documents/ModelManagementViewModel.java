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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

/**
 * ViewModel para Gestión de Modelos de IA
 * PANTALLA CRÍTICA: Debe ejecutarse ANTES de usar procesamiento de documentos
 * Endpoint: GET/POST /api/v1/models/*
 */
@Slf4j
@VariableResolver(DelegatingVariableResolver.class)
public class ModelManagementViewModel extends BaseFront<ModelManagementViewModel> {

    @WireVariable
    private RestTemplate restTemplate;

    private static final String DOCUMENTS_SERVICE_URL = "http://localhost:8004";

    // Estado del sistema
    @Getter
    @Setter
    private boolean modelServiceInitialized = false;

    @Getter
    @Setter
    private boolean modelLoaderAvailable = false;

    @Getter
    @Setter
    private boolean gpuAvailable = false;

    @Getter
    @Setter
    private String systemStatus = "UNKNOWN";

    @Getter
    private List<String> loadedModels = new ArrayList<>();

    @Getter
    @Setter
    private int modelCacheSize = 0;

    @Getter
    @Setter
    private String cacheDirectory = "";

    // Modelos disponibles por categoría
    @Getter
    private Map<String, List<ModelInfo>> modelsByCategory = new LinkedHashMap<>();

    // Modelo seleccionado para acciones
    @Getter
    @Setter
    private ModelInfo selectedModel;

    // Logs del sistema
    @Getter
    private List<LogEntry> systemLogs = new ArrayList<>();

    // Estado de carga
    @Getter
    @Setter
    private boolean loading = false;

    @Getter
    @Setter
    private String errorMessage = "";

    // Formulario de registro de modelo
    @Getter
    @Setter
    private String newModelName = "";

    @Getter
    @Setter
    private String newModelProvider = "huggingface";

    @Getter
    @Setter
    private String newModelTask = "document-extraction";

    @Init
    public void init() {
        log.info("✅ Model Management ViewModel initialized");
        checkSystemStatus();
        loadAvailableModels();
    }

    /**
     * Verifica el estado del sistema y modelos
     */
    @Command
    @NotifyChange({"modelServiceInitialized", "modelLoaderAvailable", "gpuAvailable", 
                   "systemStatus", "loadedModels", "modelCacheSize", "cacheDirectory"})
    public void checkSystemStatus() {
        loading = true;
        errorMessage = "";

        try {
            String url = DOCUMENTS_SERVICE_URL + "/api/v1/models/status";
            
            log.info("📤 Checking model service status...");
            
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> body = response.getBody();
                
                modelServiceInitialized = (Boolean) body.getOrDefault("initialized", false);
                modelLoaderAvailable = (Boolean) body.getOrDefault("model_loader_available", false);
                gpuAvailable = (Boolean) body.getOrDefault("gpu_available", false);
                systemStatus = (String) body.getOrDefault("status", "UNKNOWN");
                modelCacheSize = ((Number) body.getOrDefault("model_cache_size", 0)).intValue();
                cacheDirectory = (String) body.getOrDefault("cache_directory", "");
                
                List<String> models = (List<String>) body.get("loaded_models");
                if (models != null) {
                    loadedModels = new ArrayList<>(models);
                }

                log.info("✅ System status retrieved: {}", systemStatus);
                addLog("INFO", "System status checked successfully", "Status: " + systemStatus);
            }

        } catch (Exception e) {
            log.error("❌ Error checking system status", e);
            errorMessage = "Error al verificar estado del sistema: " + e.getMessage();
            systemStatus = "ERROR";
            addLog("ERROR", "Failed to check system status", e.getMessage());
        } finally {
            loading = false;
        }
    }

    /**
     * Carga la lista de modelos disponibles
     */
    @Command
    @NotifyChange("modelsByCategory")
    public void loadAvailableModels() {
        try {
            String url = DOCUMENTS_SERVICE_URL + "/api/v1/models/available";
            
            log.info("📤 Loading available models...");
            
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> body = response.getBody();
                modelsByCategory.clear();

                // Parsear modelos por categoría
                Map<String, List<Map<String, Object>>> categoriesMap = 
                    (Map<String, List<Map<String, Object>>>) body.get("models");

                if (categoriesMap != null) {
                    for (Map.Entry<String, List<Map<String, Object>>> entry : categoriesMap.entrySet()) {
                        String category = entry.getKey();
                        List<ModelInfo> models = new ArrayList<>();

                        for (Map<String, Object> modelMap : entry.getValue()) {
                            ModelInfo model = new ModelInfo();
                            model.setName((String) modelMap.get("name"));
                            model.setProvider((String) modelMap.get("provider"));
                            model.setTask((String) modelMap.get("task"));
                            model.setStatus(loadedModels.contains(model.getName()) ? "LOADED" : "AVAILABLE");
                            
                            // Información adicional si está disponible
                            if (modelMap.containsKey("size")) {
                                model.setSize((String) modelMap.get("size"));
                            }
                            if (modelMap.containsKey("description")) {
                                model.setDescription((String) modelMap.get("description"));
                            }

                            models.add(model);
                        }

                        modelsByCategory.put(category, models);
                    }
                }

                log.info("✅ Loaded {} model categories", modelsByCategory.size());
                addLog("INFO", "Available models loaded", modelsByCategory.size() + " categories");
            }

        } catch (Exception e) {
            log.error("❌ Error loading available models", e);
            addLog("ERROR", "Failed to load available models", e.getMessage());
        }
    }

    /**
     * Registra un nuevo modelo en el sistema
     */
    @Command
    @NotifyChange({"newModelName", "newModelProvider", "newModelTask", "modelsByCategory"})
    public void registerModel() {
        if (newModelName == null || newModelName.trim().isEmpty()) {
            Messagebox.show("El nombre del modelo es requerido", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        loading = true;

        try {
            String url = DOCUMENTS_SERVICE_URL + "/api/v1/models/register";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("model_name", newModelName);
            body.add("provider", newModelProvider);
            body.add("task", newModelTask);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            log.info("📤 Registering model: {}", newModelName);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> result = response.getBody();
                String status = (String) result.get("status");
                
                log.info("✅ Model registered: {} - Status: {}", newModelName, status);
                addLog("SUCCESS", "Model registered", newModelName + " - " + status);
                
                Messagebox.show("Modelo registrado correctamente: " + newModelName, 
                                "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                
                // Limpiar formulario
                newModelName = "";
                newModelProvider = "huggingface";
                newModelTask = "document-extraction";
                
                // Recargar lista
                loadAvailableModels();
            }

        } catch (Exception e) {
            log.error("❌ Error registering model", e);
            addLog("ERROR", "Failed to register model", e.getMessage());
            Messagebox.show("Error al registrar modelo: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        } finally {
            loading = false;
        }
    }

    /**
     * Carga un modelo específico en memoria
     */
    @Command
    @NotifyChange({"loadedModels", "modelsByCategory"})
    public void loadModel(@BindingParam("model") ModelInfo model) {
        if (model == null) {
            Messagebox.show("Seleccione un modelo", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        loading = true;

        try {
            String url = DOCUMENTS_SERVICE_URL + "/api/v1/models/load";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("model_name", model.getName());
            body.add("task", model.getTask());

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            log.info("📤 Loading model: {}", model.getName());

            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> result = response.getBody();
                String status = (String) result.get("status");
                
                log.info("✅ Model loaded: {} - Status: {}", model.getName(), status);
                addLog("SUCCESS", "Model loaded", model.getName() + " - " + status);
                
                Messagebox.show("Modelo cargado correctamente: " + model.getName(), 
                                "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                
                // Actualizar estado del sistema
                checkSystemStatus();
                loadAvailableModels();
            }

        } catch (Exception e) {
            log.error("❌ Error loading model", e);
            addLog("ERROR", "Failed to load model", e.getMessage());
            Messagebox.show("Error al cargar modelo: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        } finally {
            loading = false;
        }
    }

    /**
     * Verifica la salud del servicio
     */
    @Command
    @NotifyChange("*")
    public void checkHealth() {
        try {
            String url = DOCUMENTS_SERVICE_URL + "/health";
            
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> health = response.getBody();
                String status = (String) health.get("status");
                
                log.info("✅ Health check: {}", status);
                addLog("INFO", "Health check successful", "Status: " + status);
                
                Messagebox.show("Servicio operativo: " + status, "Health Check", Messagebox.OK, Messagebox.INFORMATION);
            }

        } catch (Exception e) {
            log.error("❌ Health check failed", e);
            addLog("ERROR", "Health check failed", e.getMessage());
            Messagebox.show("Error en health check: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Limpia los logs
     */
    @Command
    @NotifyChange("systemLogs")
    public void clearLogs() {
        systemLogs.clear();
        log.info("🗑️ Logs cleared");
    }

    /**
     * Refresca todo el estado
     */
    @Command
    @NotifyChange("*")
    public void refreshAll() {
        checkSystemStatus();
        loadAvailableModels();
    }

    /**
     * Agrega una entrada al log
     */
    private void addLog(String level, String message, String details) {
        LogEntry entry = new LogEntry();
        entry.setTimestamp(new Date());
        entry.setLevel(level);
        entry.setMessage(message);
        entry.setDetails(details);
        
        systemLogs.add(0, entry); // Agregar al inicio
        
        // Mantener solo los últimos 100 logs
        if (systemLogs.size() > 100) {
            systemLogs = new ArrayList<>(systemLogs.subList(0, 100));
        }
    }

    /**
     * Clase para representar información de un modelo
     */
    @Getter
    @Setter
    public static class ModelInfo {
        private String name;
        private String provider;
        private String task;
        private String status; // AVAILABLE, LOADED, ERROR
        private String size;
        private String description;
    }

    /**
     * Clase para representar una entrada de log
     */
    @Getter
    @Setter
    public static class LogEntry {
        private Date timestamp;
        private String level; // INFO, SUCCESS, ERROR, WARNING
        private String message;
        private String details;
    }
}

