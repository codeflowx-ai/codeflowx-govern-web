package com.codeflowx.govern.viewmodel.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.enartframework.web.annotation.Action;
import org.enartframework.web.zk.page.MasterPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.framework.validators.UniqueValidator;
import com.codeflowx.govern.entity.evaluation.ModelBiasAnalysis;
import com.codeflowx.govern.entity.evaluation.ModelPerformance;
import com.codeflowx.govern.entity.functions.models.CalculateDrift;
import com.codeflowx.govern.entity.functions.models.ValidateModel;
import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.entity.models.ModelArtifact;
import com.codeflowx.govern.entity.models.ModelProvider;
import com.codeflowx.govern.entity.models.ModelVersion;
import com.codeflowx.govern.entity.procedures.models.CreateModelVersion;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de modelo AI/ML
 * 
 * Responsabilidades:
 * - Creación de nuevos modelos
 * - Edición de modelos existentes
 * - Operaciones de negocio: validateModel(), calculateDrift()
 * - Información descendente: versiones, artifacts, métricas
 * - Estadísticas y contadores específicos del modelo
 * - Gestión de relaciones (provider, versiones, performance, etc.)
 * 
 * NO incluye:
 * - Búsqueda/listado (ver ModelsOverviewViewModel)
 * - Métricas generales del conjunto (ver ModelsOverviewViewModel)
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class ModelsDetailViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    private static final String IDDESKTOP = "contenedor";
    // ========== Servicios y contexto Spring ==========
    @WireVariable
    private BusinessService businessService;
    
    @Autowired
    protected IEntityLocal dao;
    
    @WireVariable
    public Environment environment;
    
    @WireVariable("context")
    protected GenericApplicationContext contexto;
    
    @WireVariable("ctxBean")
    protected Context ctxBean;
    
    
    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }
    
    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
        
    }
    // ==== validadores ===
    UniqueValidator unique ;
    
    // ========== Modo de operación ==========
    private String mode; // "create" o "edit"
    private Long modelId;
    private boolean editing = false; // true = edición, false = creación
    private String pageTitle = "Detalle del Modelo";
    
    // ========== Datos del modelo ==========
    private Model currentModel;
    private List<ModelProvider> availableProviders = new ArrayList<>();
    
    // ========== Información descendente ==========
    private List<ModelVersion> modelVersions = new ArrayList<>();
    private List<ModelArtifact> modelArtifacts = new ArrayList<>();
    private List<ModelPerformance> performanceMetrics = new ArrayList<>();
    private List<ModelBiasAnalysis> biasAnalysis = new ArrayList<>();
    
    // ========== Estadísticas específicas del modelo ==========
    private Long totalVersions = 0L;
    private Long totalArtifacts = 0L;
    private Long totalInferences = 0L;
    private BigDecimal currentAccuracy = BigDecimal.ZERO;
    private BigDecimal currentBiasScore = BigDecimal.ZERO;
    private BigDecimal currentDriftScore = BigDecimal.ZERO;
    private String lastValidationStatus = "NOT_VALIDATED";
    private Timestamp lastValidationDate;
    
    // ========== Inicialización ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        // Obtener parámetros de navegación
        mode = (String) super.action.name(); //   Executions.getCurrent().getParameter("mode");
       // String modelIdStr = Executions.getCurrent().getParameter("modelId");
        
        if (dataParam != null) {
            modelId = Long.parseLong(String.valueOf(dataParam));
        }
        
        log.info("Inicializando ModelsDetailViewModel - mode: {}, modelId: {}", mode, modelId);
        
        loadAvailableProviders();
        
        if ("NEW".equals(mode)) {
            initNewModel();
        } else if ("LOAD".equals(mode) && modelId != null) {
            loadModel(modelId);
        } else {
            log.error("Modo inválido o falta modelId");
            Executions.sendRedirect("/models/models-overview.zul");
        }
        
        // validador 
        unique = new UniqueValidator(currentModel, businessService);
    }
    
    /**
     * Inicializa un nuevo modelo con valores por defecto
     */
    private void initNewModel() {
        log.debug("Inicializando nuevo modelo");
        currentModel = new Model();
        currentModel.setModcreatedat(new Timestamp(System.currentTimeMillis()));
        currentModel.setModcreatedby("system"); // TODO: Usuario actual
        currentModel.setModstatus("DEVELOPMENT");
        currentModel.setModapprovalstatus("PENDING_APPROVAL");
        
        editing = false;
        pageTitle = "Registrar Nuevo Modelo";
    }
    
    /**
     * Carga modelo existente desde BD
     */
    private void loadModel(Long id) {
        try {
            log.debug("Cargando modelo ID={}", id);
            
            currentModel = businessService.findById(Model.class, id);
            
            if (currentModel == null) {
                log.error("Modelo no encontrado: ID={}", id);
                Messagebox.show(Labels.getLabel("models.error.notfound"), Labels.getLabel("models.error.title"), 
                    Messagebox.OK, Messagebox.ERROR);
                Executions.sendRedirect("/models/models-overview.zul");
                return;
            }
            
            log.info("Modelo cargado: {}", currentModel.getModname());
            
            editing = true;
            pageTitle = "Editar Modelo: " + currentModel.getModname();
            
            // Cargar información descendente
            loadModelVersions();
            loadModelArtifacts();
            loadPerformanceMetrics();
            loadBiasAnalysis();
            loadModelStatistics();
            
        } catch (Exception e) {
            log.error("Error al cargar modelo ID={}", id, e);
            Messagebox.show(Labels.getLabel("models.error.load") + ": " + e.getMessage(), 
                Labels.getLabel("models.error.title"), Messagebox.OK, Messagebox.ERROR);
            Executions.sendRedirect("/models/models-overview.zul");
        }
    }
    
    /**
     * Carga proveedores disponibles para combos
     */
    private void loadAvailableProviders() {
        try {
            PageParams params = PageParams.builder()
                .maxRows(100)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            Map<String, Object> filters = new HashMap<>();
            filters.put("modstatus", "ACTIVE");
            
            PageResult<ModelProvider> result = businessService.findAllEntity(
                ModelProvider.class,
                params,
                filters
            );
            
            if (result != null && result.getContent() != null) {
                availableProviders = result.getContent();
                log.info("Cargados {} proveedores", availableProviders.size());
            }
            
        } catch (Exception e) {
            log.error("Error al cargar proveedores", e);
            availableProviders = new ArrayList<>();
        }
    }
    
    /**
     * Carga estadísticas específicas del modelo desde contadores reales
     */
    private void loadModelStatistics() {
        try {
            log.debug("Cargando estadísticas del modelo ID={}", currentModel.getIdxmodel());
            
            // Contar versiones
            totalVersions = (long) modelVersions.size();
            
            // Contar artifacts
            totalArtifacts = (long) modelArtifacts.size();
            
            // Calcular métricas de performance desde datos reales
            if (!performanceMetrics.isEmpty()) {
                // Obtener última métrica de accuracy
                performanceMetrics.stream()
                    .filter(p -> "ACCURACY".equals(p.getModmetricname()))
                    .findFirst()
                    .ifPresent(p -> currentAccuracy = p.getModmetricvalue());
                
                // El total de inferencias se puede obtener de otra fuente o vista
                totalInferences = 0L; // TODO: Obtener de vista o cálculo específico
            } else {
                currentAccuracy = BigDecimal.ZERO;
                totalInferences = 0L;
            }
            
            // Calcular bias score desde análisis de sesgo
            if (!biasAnalysis.isEmpty()) {
                // Usar el campo modbiasscore directamente y promediar
                currentBiasScore = biasAnalysis.stream()
                    .filter(b -> b.getModbiasscore() != null)
                    .map(ModelBiasAnalysis::getModbiasscore)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(biasAnalysis.size()), 2, RoundingMode.HALF_UP);
            } else {
                currentBiasScore = BigDecimal.ZERO;
            }
            
            log.info("Estadísticas cargadas - Versiones: {}, Artifacts: {}, Inferencias: {}", 
                totalVersions, totalArtifacts, totalInferences);
            
        } catch (Exception e) {
            log.error("Error al cargar estadísticas", e);
            totalVersions = 0L;
            totalArtifacts = 0L;
            totalInferences = 0L;
        }
    }
    
    // ========== Comandos CRUD ==========
    
    @Command
    @NotifyChange("*")
    public void saveModel() {
        try {
            log.info("Guardando modelo: {}", currentModel.getModname());
            
            // Validaciones
            if (currentModel.getModname() == null || currentModel.getModname().trim().isEmpty()) {
                Messagebox.show(Labels.getLabel("models.validation.required.name"), 
                    Labels.getLabel("models.validation.title"), Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            if (currentModel.getModtype() == null || currentModel.getModtype().trim().isEmpty()) {
                Messagebox.show(Labels.getLabel("models.validation.required.type"), 
                    Labels.getLabel("models.validation.title"), Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            if (currentModel.getModversion() == null || currentModel.getModversion().trim().isEmpty()) {
                Messagebox.show(Labels.getLabel("models.validation.required.version"), 
                    Labels.getLabel("models.validation.title"), Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            // Guardar
            if (currentModel.getIdxmodel() == null) {
                // Nuevo
                businessService.save(currentModel);
                log.info("Modelo creado: ID={}, nombre={}", 
                    currentModel.getIdxmodel(), currentModel.getModname());
                Messagebox.show(Labels.getLabel("models.success.created"), 
                    Labels.getLabel("models.success.title"), Messagebox.OK, Messagebox.INFORMATION);
            } else {
                // Actualizar
                currentModel.setModupdatedat(new Timestamp(System.currentTimeMillis()));
                currentModel.setModupdatedby("system"); // TODO: Usuario actual
                businessService.update(currentModel);
                log.info("Modelo actualizado: ID={}, nombre={}", 
                    currentModel.getIdxmodel(), currentModel.getModname());
                Messagebox.show(Labels.getLabel("models.success.updated"), 
                    Labels.getLabel("models.success.title"), Messagebox.OK, Messagebox.INFORMATION);
            }
            
            // Volver al listado
            Executions.sendRedirect("/models/models-overview.zul");
            
        } catch (Exception e) {
            log.error("Error al guardar modelo", e);
            Messagebox.show(Labels.getLabel("models.error.save") + ": " + e.getMessage(), 
                Labels.getLabel("models.error.title"), Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    public void cancelEdit() {
    	Map<String, Object> params = new HashMap<>();
        params.put("dataParam", modelId);
        params.put("action", Action.LOAD);
        appendPage("gobierno/models/models-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    // ========== Operaciones de negocio ==========
    
    @Command
    @NotifyChange({"lastValidationStatus", "lastValidationDate", "currentModel"})
    public void validateModel() {
        if (currentModel == null || currentModel.getIdxmodel() == null) {
            Messagebox.show(Labels.getLabel("models.validation.required.name"), 
                Labels.getLabel("models.warning.title"), Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        
        log.info("Ejecutando validación de modelo ID={}", currentModel.getIdxmodel());
        try {
            ValidateModel function = new ValidateModel();
            function.setPModelId(currentModel.getIdxmodel());
            
            function = businessService.callFuction(function);
            
            if (function != null && function.getOResult() != null) {
                Gson gson = new Gson();
                JsonObject jsonObj = gson.fromJson(function.getOResult().toString(), JsonObject.class);
                
                boolean success = jsonObj.get("success").getAsBoolean();
                double score = jsonObj.get("score").getAsDouble();
                
                JsonObject checksObj = jsonObj.getAsJsonObject("checks");
                boolean hasName = checksObj.get("has_name").getAsBoolean();
                boolean hasProvider = checksObj.get("has_provider").getAsBoolean();
                boolean hasValidStatus = checksObj.get("has_valid_status").getAsBoolean();
                boolean hasEndpoint = checksObj.get("has_endpoint").getAsBoolean();
                
                String level = score < 50 ? "CRITICAL" : (score < 80 ? "WARNING" : "SUCCESS");
                boolean allValid = hasName && hasProvider && hasValidStatus && hasEndpoint;
                
                lastValidationStatus = allValid ? "PASSED" : "FAILED";
                lastValidationDate = new Timestamp(System.currentTimeMillis());
                
                log.info("Validación completada - Score: {}, Level: {}", score, level);
                
                String message = String.format(
                    "Validación completada\n\n" +
                    "Score: %.1f/100\n" +
                    "Nivel: %s\n" +
                    "Success: %s\n\n" +
                    "Checks:\n" +
                    "✓ Nombre: %s\n" +
                    "✓ Proveedor: %s\n" +
                    "✓ Estado: %s\n" +
                    "✓ Endpoint: %s",
                    score, level, success ? "Sí" : "No",
                    hasName ? "OK" : "FAIL",
                    hasProvider ? "OK" : "FAIL",
                    hasValidStatus ? "OK" : "FAIL",
                    hasEndpoint ? "OK" : "FAIL"
                );
                
                Messagebox.show(message, Labels.getLabel("models.operation.validation.title"), 
                    Messagebox.OK, 
                    allValid ? Messagebox.INFORMATION : Messagebox.EXCLAMATION);
            }
                
        } catch (Exception e) {
            log.error("Error al validar modelo", e);
            lastValidationStatus = "ERROR";
            Messagebox.show(Labels.getLabel("models.error.validation") + ": " + e.getMessage(), 
                Labels.getLabel("models.error.title"), Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange({"currentDriftScore", "currentModel"})
    public void calculateDrift() {
        if (currentModel == null || currentModel.getIdxmodel() == null) {
            Messagebox.show(Labels.getLabel("models.validation.required.name"), 
                Labels.getLabel("models.warning.title"), Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        
        log.info("Calculando drift para modelo ID={}", currentModel.getIdxmodel());
        try {
            CalculateDrift function = new CalculateDrift();
            function.setPModelId(currentModel.getIdxmodel());
            
            function = businessService.callFuction(function);
            
            if (function != null && function.getOResult() != null) {
                Gson gson = new Gson();
                JsonObject jsonObj = gson.fromJson(function.getOResult().toString(), JsonObject.class);
                
                double driftScore = jsonObj.get("drift_score").getAsDouble();
                
                JsonObject metricsObj = jsonObj.getAsJsonObject("metrics");
                int totalCalls = metricsObj.get("total_calls").getAsInt();
                int errorCalls = metricsObj.get("error_calls").getAsInt();
                double avgLatency = metricsObj.get("avg_latency").getAsDouble();
                double avgCost = metricsObj.get("avg_cost").getAsDouble();
                
                String level = driftScore > 80 ? "CRITICAL" : (driftScore > 50 ? "WARNING" : "NORMAL");
                double errorRate = totalCalls > 0 ? (errorCalls * 100.0 / totalCalls) : 0.0;
                boolean hasSignificantDrift = driftScore > 50;
                
                currentDriftScore = BigDecimal.valueOf(driftScore);
                
                log.info("Drift calculado - Score: {}, Level: {}", driftScore, level);
                
                String message = String.format(
                    "Cálculo de Drift completado\n\n" +
                    "Drift Score: %.1f\n" +
                    "Nivel: %s\n\n" +
                    "Métricas:\n" +
                    "• Total Calls: %d\n" +
                    "• Error Calls: %d\n" +
                    "• Tasa de Error: %.2f%%\n" +
                    "• Latencia Promedio: %.2f ms\n" +
                    "• Costo Promedio: $%.4f",
                    driftScore, level, totalCalls, errorCalls,
                    errorRate, avgLatency, avgCost
                );
                
                String icon = hasSignificantDrift ? Messagebox.EXCLAMATION : Messagebox.INFORMATION;
                Messagebox.show(message, Labels.getLabel("models.operation.drift.title"), Messagebox.OK, icon);
            }
                
        } catch (Exception e) {
            log.error("Error al calcular drift", e);
            Messagebox.show(Labels.getLabel("models.error.drift") + ": " + e.getMessage(), 
                Labels.getLabel("models.error.title"), Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    // ========== Información descendente ==========
    
    /**
     * Carga versiones del modelo desde BD
     */
    @Command
    @NotifyChange({"modelVersions", "totalVersions"})
    public void loadModelVersions() {
        try {
            log.debug("Cargando versiones del modelo ID={}", currentModel.getIdxmodel());
            
            PageParams params = PageParams.builder()
                .maxRows(100)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            Map<String, Object> filters = new HashMap<>();
            filters.put("idmodmodels0", currentModel.getIdxmodel());
            
            PageResult<ModelVersion> result = businessService.findAllEntity(
                ModelVersion.class,
                params,
                filters
            );
            
            if (result != null && result.getContent() != null) {
                modelVersions = result.getContent();
                totalVersions = (long) modelVersions.size();
                log.info("Cargadas {} versiones del modelo", totalVersions);
            } else {
                modelVersions = new ArrayList<>();
                totalVersions = 0L;
            }
            
        } catch (Exception e) {
            log.error("Error al cargar versiones del modelo", e);
            modelVersions = new ArrayList<>();
            totalVersions = 0L;
        }
    }
    
    /**
     * Carga artifacts del modelo desde BD
     */
    @Command
    @NotifyChange({"modelArtifacts", "totalArtifacts"})
    public void loadModelArtifacts() {
        try {
            log.debug("Cargando artifacts del modelo ID={}", currentModel.getIdxmodel());
            
            PageParams params = PageParams.builder()
                .maxRows(100)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            Map<String, Object> filters = new HashMap<>();
            filters.put("idmodmodels0", currentModel.getIdxmodel());
            
            PageResult<ModelArtifact> result = businessService.findAllEntity(
                ModelArtifact.class,
                params,
                filters
            );
            
            if (result != null && result.getContent() != null) {
                modelArtifacts = result.getContent();
                totalArtifacts = (long) modelArtifacts.size();
                log.info("Cargados {} artifacts del modelo", totalArtifacts);
            } else {
                modelArtifacts = new ArrayList<>();
                totalArtifacts = 0L;
            }
            
        } catch (Exception e) {
            log.error("Error al cargar artifacts del modelo", e);
            modelArtifacts = new ArrayList<>();
            totalArtifacts = 0L;
        }
    }
    
    /**
     * Carga métricas de performance desde BD
     */
    @Command
    @NotifyChange({"performanceMetrics", "currentAccuracy"})
    public void loadPerformanceMetrics() {
        try {
            log.debug("Cargando métricas de performance del modelo ID={}", currentModel.getIdxmodel());
            
            PageParams params = PageParams.builder()
                .maxRows(50)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            Map<String, Object> filters = new HashMap<>();
            filters.put("idmodmodels0", currentModel.getIdxmodel());
            
            PageResult<ModelPerformance> result = businessService.findAllEntity(
                ModelPerformance.class,
                params,
                filters
            );
            
            if (result != null && result.getContent() != null) {
                performanceMetrics = result.getContent();
                log.info("Cargadas {} métricas de performance", performanceMetrics.size());
                
                // Actualizar accuracy actual
                performanceMetrics.stream()
                    .filter(p -> "ACCURACY".equals(p.getModmetricname()))
                    .findFirst()
                    .ifPresent(p -> currentAccuracy = p.getModmetricvalue());
            } else {
                performanceMetrics = new ArrayList<>();
            }
            
        } catch (Exception e) {
            log.error("Error al cargar métricas de performance", e);
            performanceMetrics = new ArrayList<>();
        }
    }
    
    /**
     * Carga análisis de sesgo desde BD
     */
    @Command
    @NotifyChange({"biasAnalysis", "currentBiasScore"})
    public void loadBiasAnalysis() {
        try {
            log.debug("Cargando análisis de sesgo del modelo ID={}", currentModel.getIdxmodel());
            
            PageParams params = PageParams.builder()
                .maxRows(50)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            Map<String, Object> filters = new HashMap<>();
            filters.put("idmodmodels0", currentModel.getIdxmodel());
            
            PageResult<ModelBiasAnalysis> result = businessService.findAllEntity(
                ModelBiasAnalysis.class,
                params,
                filters
            );
            
            if (result != null && result.getContent() != null) {
                biasAnalysis = result.getContent();
                log.info("Cargados {} análisis de sesgo", biasAnalysis.size());
            } else {
                biasAnalysis = new ArrayList<>();
            }
            
        } catch (Exception e) {
            log.error("Error al cargar análisis de sesgo", e);
            biasAnalysis = new ArrayList<>();
        }
    }
    
    /**
     * Crea una nueva versión del modelo usando stored procedure
     */
    @Command
    @NotifyChange("*")
    public void createNewVersion() {
        try {
            log.info("Creando nueva versión del modelo ID={}", currentModel.getIdxmodel());
            
            CreateModelVersion procedure = new CreateModelVersion();
            procedure.setPInputParam(currentModel.getIdxmodel());
            
            procedure = businessService.callProcedure(procedure);
            
            if (procedure.getOSuccess() != null && procedure.getOSuccess()) {
                log.info("Nueva versión creada: ID={}", procedure.getOResult());
                
                // Recargar versiones
                loadModelVersions();
                
                Messagebox.show(
                    Labels.getLabel("models.success.version.created") + "\nID: " + procedure.getOResult(),
                    Labels.getLabel("models.success.title"),
                    Messagebox.OK,
                    Messagebox.INFORMATION
                );
            } else {
                log.warn("No se pudo crear la versión");
                Messagebox.show(
                    Labels.getLabel("models.warning.version.failed"),
                    Labels.getLabel("models.warning.title"),
                    Messagebox.OK,
                    Messagebox.EXCLAMATION
                );
            }
            
        } catch (Exception e) {
            log.error("Error al crear nueva versión", e);
            Messagebox.show(
                Labels.getLabel("models.error.version") + ": " + e.getMessage(),
                Labels.getLabel("models.error.title"),
                Messagebox.OK,
                Messagebox.ERROR
            );
        }
    }
    
    /**
     * Libera recursos y limpia referencias para ayudar al GC
     * Se llama automáticamente cuando el ViewModel se destruye
     */
    @Destroy
    public void destroy() {
        log.debug("[Destroy] Liberando recursos del ViewModel {}", this.getClass().getSimpleName());
        
        try {
            // Limpiar modelo actual
            currentModel = null;
            
            // Limpiar listas de FK
            if (availableProviders != null) {
                availableProviders.clear();
                availableProviders = null;
            }
            
            // Limpiar listas descendentes
            if (modelVersions != null) {
                modelVersions.clear();
                modelVersions = null;
            }
            if (modelArtifacts != null) {
                modelArtifacts.clear();
                modelArtifacts = null;
            }
            if (performanceMetrics != null) {
                performanceMetrics.clear();
                performanceMetrics = null;
            }
            if (biasAnalysis != null) {
                biasAnalysis.clear();
                biasAnalysis = null;
            }
            
            // Limpiar BusinessService
            businessService = null;
            
            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }
}

