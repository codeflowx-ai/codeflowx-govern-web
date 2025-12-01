package com.codeflowx.platform.viewmodel.models;
import com.codeflowx.framework.zkoss.BaseFront;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.orm.exception.DaoException;
import org.enartframework.suinsit.Context;
import org.enartframework.web.annotation.Action;
import org.enartframework.web.zk.page.MasterPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.framework.validators.UniqueValidator;
import com.codeflowx.govern.entity.evaluation.ModelBiasAnalysis;
import com.codeflowx.govern.entity.evaluation.ModelPerformance;
import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.entity.models.ModelApproval;
import com.codeflowx.govern.entity.models.ModelArtifact;
import com.codeflowx.govern.entity.models.ModelDependency;
import com.codeflowx.govern.entity.models.ModelProvider;
import com.codeflowx.govern.entity.models.ModelStageTransition;
import com.codeflowx.govern.entity.models.ModelValidation;
import com.codeflowx.govern.entity.models.ModelVersion;
import com.codeflowx.govern.entity.serving.ModelDeployment;
import com.codeflowx.govern.entity.serving.ModelMetrics;
import com.codeflowx.admin.Ssoractividad;

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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de Model
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ModelDetailViewModel extends BaseFront<ModelDetailViewModel>{
    
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
    
    @WireVariable("APPLICATION_DS")
    protected DataSource ds;
    
    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }
    
    @Override
    public void setBeans(Object bean) {
        // Auto-generated method stub
    }
    
    private static final long serialVersionUID = 1L;
    private static final String IDDESKTOP = "contenedor";
    
    // ========== Modo de operación ==========
    private String mode;
    private Long idxmodel;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private Model currentModel;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    private String originalModname = null;
    
    // ========== Listas para combos (FK) ==========
    private List<ModelProvider> availableModelProviders = new ArrayList<>();
    private List<String> availableModtypes = new ArrayList<>();
    private List<String> availableModframeworks = new ArrayList<>();
    private List<String> availableModstatuss = new ArrayList<>();
    private List<String> availableModapprovalstatuss = new ArrayList<>();
    
    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    private List<String> selectedModtags = new ArrayList<>();
    private String newModtag = "";
    
    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    private List<ModelArtifact> submodmodelartifacts = new ArrayList<>();
    private List<ModelBiasAnalysis> submodmodelbiasanalyses = new ArrayList<>();
    private List<ModelDependency> submodmodeldependencies = new ArrayList<>();
    private List<ModelDependency> submodmodeldependenciesByTargetmodel = new ArrayList<>();
    private List<ModelPerformance> submodmodelperformances = new ArrayList<>();
    private List<ModelStageTransition> submodmodelstagetransitions = new ArrayList<>();
    private List<ModelValidation> submodmodelvalidations = new ArrayList<>();
    private List<ModelVersion> submodmodelversions = new ArrayList<>();
    private List<ModelDeployment> subsrvdeployments = new ArrayList<>();
    private List<ModelMetrics> subsrvmodelmetrics = new ArrayList<>();
    private List<ModelVersion> subsrvmodelversions = new ArrayList<>();
    private boolean submodmodelartifactsLoaded = false;
    private boolean submodmodelbiasanalysesLoaded = false;
    private boolean submodmodeldependenciesLoaded = false;
    private boolean submodmodeldependenciesByTargetmodelLoaded = false;
    private boolean submodmodelperformancesLoaded = false;
    private boolean submodmodelstagetransitionsLoaded = false;
    private boolean submodmodelvalidationsLoaded = false;
    private boolean submodmodelversionsLoaded = false;
    private boolean subsrvdeploymentsLoaded = false;
    private boolean subsrvmodelmetricsLoaded = false;
    private boolean subsrvmodelversionsLoaded = false;
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        // Obtener parámetros de navegación - con protección para action null

        
        if (super.action != null) {

        
            mode = super.action.name();

        
        } else {

        
            mode = (dataParam != null) ? "LOAD" : "NEW";

        
            log.warn("Action es null, infiriendo modo: {}", mode);

        
        }
        
        // dataParam siempre contiene el ID (PK de tipo Long)
        if (dataParam != null) {
            idxmodel = Long.valueOf(String.valueOf(dataParam));
        }
        
        log.info("Inicializando ModelDetailViewModel - mode: {}, idxmodel: {}", mode, idxmodel);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxmodel != null) {
            loadItem(idxmodel);
        } else {
            log.error("Modo inválido o falta idxmodel");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/models/models-overview.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentModel, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentModel = new Model();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadModelProviders();
        loadModtypes();
        loadModframeworks();
        loadModstatuss();
        loadModapprovalstatuss();
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            // findById siempre recibe Long id (el PK)
            currentModel = businessService.findById(Model.class, id);
            
            if (currentModel == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/models/models-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentModel.getModname();
        loadModelProviders();
        loadModtypes();
        loadModframeworks();
        loadModstatuss();
        loadModapprovalstatuss();
            
            // Cargar tags/roles existentes desde JSON
            selectedModtags = convertJsonToList(currentModel.getModtags());
            
            // Guardar valores originales para validación de unicidad
            originalModname = currentModel.getModname();
            
            // Auditar carga de registro
            logActivity("CONSULTA", "MODMODELS", id, "Consulta: " + currentModel.getModname());
            
        } catch (Exception e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/models/models-overview.zul", page.getFellow(IDDESKTOP), params);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void saveItem() {
        try {
            log.info("Guardando registro");
            
            // Validar campos obligatorios
            if (!validateRequiredFields()) {
                return;
            }
            
            boolean isNew = currentModel.getIdxmodel() == null;
            
            if (isNew) {
                businessService.save(currentModel);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "MODMODELS", currentModel.getIdxmodel(), 
                    "Creado: " + currentModel.getModname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                businessService.update(currentModel);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "MODMODELS", currentModel.getIdxmodel(), 
                    "Actualizado: " + currentModel.getModname());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }
            
            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/models/models-overview.zul", page.getFellow(IDDESKTOP), params);
            
        } catch (Exception e) {
            log.error("Error al guardar", e);
            Messagebox.show("Error al guardar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    /**
     * Valida que todos los campos obligatorios estén completos
     * @return true si la validación es exitosa
     */
    private boolean validateRequiredFields() {
        StringBuilder errors = new StringBuilder();
        
        if (currentModel.getModname() == null || currentModel.getModname().trim().isEmpty()) {
            errors.append("- Name\n");
        }
        if (currentModel.getModname() != null && currentModel.getModname().length() > 255) {
            errors.append("- Name no puede exceder 255 caracteres\n");
        }
        if (currentModel.getModtype() == null || currentModel.getModtype().trim().isEmpty()) {
            errors.append("- Type\n");
        }
        if (currentModel.getModversion() == null || currentModel.getModversion().trim().isEmpty()) {
            errors.append("- Version\n");
        }
        if (currentModel.getModversion() != null && currentModel.getModversion().length() > 50) {
            errors.append("- Version no puede exceder 50 caracteres\n");
        }
        if (currentModel.getModstatus() == null || currentModel.getModstatus().trim().isEmpty()) {
            errors.append("- Status\n");
        }
        if (currentModel.getModcreatedby() == null || currentModel.getModcreatedby().trim().isEmpty()) {
            errors.append("- Created By\n");
        }
        if (currentModel.getModcreatedby() != null && currentModel.getModcreatedby().length() > 255) {
            errors.append("- Created By no puede exceder 255 caracteres\n");
        }
        if (currentModel.getModcreatedat() == null) {
            errors.append("- Created At\n");
        }
        
        if (errors.length() > 0) {
            Messagebox.show("Por favor complete los siguientes campos:\n" + errors.toString(),
                "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return false;
        }
        
        return true;
    }
    
    @Command
    public void cancelEdit() {
        log.debug("Cancelando edición");
        Map<String, Object> params = new HashMap<>();
        params.put("dataParam", idxmodel);
        params.put("action", Action.LOAD);
        appendPage("plataforma/models/models-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    /**
     * Envía el modelo a aprobación
     * Cambia el estado del modelo a IN_REVIEW y crea un registro de aprobación
     */
    @Command
    @NotifyChange("*")
    public void submitForApproval() {
        try {
            if (currentModel == null || currentModel.getIdxmodel() == null) {
                Messagebox.show("No hay modelo para enviar a aprobación",
                    "Error", Messagebox.OK, Messagebox.ERROR);
                return;
            }
            
            // Validar que el modelo esté en estado DRAFT
            if (!"DRAFT".equals(currentModel.getModstatus())) {
                Messagebox.show("Solo los modelos en estado DRAFT pueden enviarse a aprobación",
                    "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            // Validar que el usuario actual sea el owner
            if (!getUser().getUsername().equals(currentModel.getModcreatedby())) {
                Messagebox.show("Solo el owner del modelo puede enviarlo a aprobación",
                    "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            log.info("Enviando modelo a aprobación: ID={}, Name={}", 
                currentModel.getIdxmodel(), currentModel.getModname());
            
            // Confirmar acción
            Messagebox.show("¿Está seguro de enviar este modelo a aprobación?",
                "Confirmar",
                Messagebox.YES | Messagebox.NO,
                Messagebox.QUESTION,
                event -> {
                    if (Messagebox.ON_YES.equals(event.getName())) {
                        try {
                            // Cambiar estado del modelo
                            currentModel.setModstatus("IN_REVIEW");
                            currentModel.setModapprovalstatus("PENDING");
                            currentModel.setModupdatedby(getUser().getUsername());
                            currentModel.setModupdatedat(new Timestamp(System.currentTimeMillis()));
                            
                            businessService.update(currentModel);
                            
                            // Crear registro de aprobación
                            com.codeflowx.govern.entity.models.ModelApproval approval = 
                                new com.codeflowx.govern.entity.models.ModelApproval();
                            approval.setModel(currentModel);
                            approval.setModapprovaltype("NEW_MODEL");
                            approval.setModapprovalstatus("UNDER_REVIEW");
                            approval.setModtargetenvironment("PRODUCTION");
                            approval.setModrequestreason("Solicitud de aprobación para nuevo modelo: " + currentModel.getModname());
                            approval.setModcreatedby(getUser().getUsername());
                            approval.setModcreatedat(new Timestamp(System.currentTimeMillis()));
                            
                            businessService.save(approval);
                            
                            log.info("Modelo enviado a aprobación exitosamente");
                            
                            // Auditar acción
                            logActivity("ENVIO_APROBACION", "MODMODELS", currentModel.getIdxmodel(),
                                "Modelo enviado a aprobación: " + currentModel.getModname());
                            
                            Messagebox.show("Modelo enviado a aprobación exitosamente",
                                "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                            
                            // Recargar datos
                            loadItem(currentModel.getIdxmodel());
                            
                        } catch (Exception e) {
                            log.error("Error al enviar a aprobación", e);
                            Messagebox.show("Error al enviar a aprobación: " + e.getMessage(),
                                "Error", Messagebox.OK, Messagebox.ERROR);
                        }
                    }
                });
            
        } catch (Exception e) {
            log.error("Error al preparar envío a aprobación", e);
            Messagebox.show("Error: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    /**
     * Verifica si el botón "Enviar a Aprobación" debe estar visible
     * @return true si el modelo está en DRAFT y el usuario actual es el owner
     */
    public boolean isSubmitForApprovalVisible() {
        if (currentModel == null) return false;
        
        boolean isDraft = "DRAFT".equals(currentModel.getModstatus());
        boolean isOwner = getUser() != null && 
                         getUser().getUsername() != null && 
                         getUser().getUsername().equals(currentModel.getModcreatedby());
        
        return isDraft && isOwner;
    }
    
    private void loadModelProviders() {
        try {
            PageParams fkParams = PageParams.builder()
                .maxRows(1000)
                .pageActual(1)
                .rowActual(0)
                .build();
            PageResult<ModelProvider> result = businessService.findAllEntity(ModelProvider.class, fkParams, new Criterias());
            availableModelProviders = result != null ? result.getContent() : new ArrayList<>();
        } catch (Exception e) {
            log.error("Error al cargar modelProviders", e);
            availableModelProviders = new ArrayList<>();
        }
    }
    
    private void loadModtypes() {
        // TODO: Cargar valores desde configuración o BD
        availableModtypes.add("OPTION_1");
        availableModtypes.add("OPTION_2");
        availableModtypes.add("OPTION_3");
    }
    
    private void loadModframeworks() {
        // TODO: Cargar valores desde configuración o BD
        availableModframeworks.add("OPTION_1");
        availableModframeworks.add("OPTION_2");
        availableModframeworks.add("OPTION_3");
    }
    
    private void loadModstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableModstatuss.add("OPTION_1");
        availableModstatuss.add("OPTION_2");
        availableModstatuss.add("OPTION_3");
    }
    
    private void loadModapprovalstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableModapprovalstatuss.add("OPTION_1");
        availableModapprovalstatuss.add("OPTION_2");
        availableModapprovalstatuss.add("OPTION_3");
    }
    
    @Command
    @NotifyChange("{'selectedModtags', 'currentModel'}")
    public void addModtag() {
        if (newModtag != null && !newModtag.trim().isEmpty() && !selectedModtags.contains(newModtag.trim())) {
            selectedModtags.add(newModtag.trim());
            newModtag = "";
            // Convertir lista a JSON y actualizar en currentModel
            currentModel.setModtags(convertListToJson(selectedModtags));
        }
    }
    
    @Command
    @NotifyChange("{'selectedModtags', 'currentModel'}")
    public void removeModtag(@BindingParam("tag") String tag) {
        selectedModtags.remove(tag);
        // Convertir lista a JSON y actualizar en currentModel
        currentModel.setModtags(convertListToJson(selectedModtags));
    }
    
    private String convertListToJson(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) json.append(",");
            json.append("\"").append(list.get(i)).append("\"");
        }
        json.append("]");
        return json.toString();
    }
    
    private List<String> convertJsonToList(String json) {
        List<String> result = new ArrayList<>();
        if (json == null || json.trim().isEmpty() || json.equals("[]")) {
            return result;
        }
        // Simplificación: parseo básico de JSON array de strings
        String cleaned = json.replace("[", "").replace("]", "").replace("\"", "");
        if (!cleaned.isEmpty()) {
            for (String item : cleaned.split(",")) {
                result.add(item.trim());
            }
        }
        return result;
    }
    
    private void loadSubmodmodelartifacts() {
        try {
            if (currentModel != null && currentModel.getIdxmodel() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "model");
                criteria.setValues(new Object[]{currentModel.getIdxmodel()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ModelArtifact> result = businessService.findAllEntity(ModelArtifact.class, collectionParams, criterias);
                submodmodelartifacts = result != null ? result.getContent() : new ArrayList<>();
                submodmodelartifactsLoaded = true;
                log.debug("Cargados {} submodmodelartifacts", submodmodelartifacts.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar submodmodelartifacts", e);
            submodmodelartifacts = new ArrayList<>();
        }
    }
    
    private void loadSubmodmodelbiasanalyses() {
        try {
            if (currentModel != null && currentModel.getIdxmodel() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "model");
                criteria.setValues(new Object[]{currentModel.getIdxmodel()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ModelBiasAnalysis> result = businessService.findAllEntity(ModelBiasAnalysis.class, collectionParams, criterias);
                submodmodelbiasanalyses = result != null ? result.getContent() : new ArrayList<>();
                submodmodelbiasanalysesLoaded = true;
                log.debug("Cargados {} submodmodelbiasanalyses", submodmodelbiasanalyses.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar submodmodelbiasanalyses", e);
            submodmodelbiasanalyses = new ArrayList<>();
        }
    }
    
    private void loadSubmodmodeldependencies() {
        try {
            if (currentModel != null && currentModel.getIdxmodel() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "sourceModel");
                criteria.setValues(new Object[]{currentModel.getIdxmodel()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ModelDependency> result = businessService.findAllEntity(ModelDependency.class, collectionParams, criterias);
                submodmodeldependencies = result != null ? result.getContent() : new ArrayList<>();
                submodmodeldependenciesLoaded = true;
                log.debug("Cargados {} submodmodeldependencies", submodmodeldependencies.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar submodmodeldependencies", e);
            submodmodeldependencies = new ArrayList<>();
        }
    }
    
    private void loadSubmodmodeldependenciesByTargetmodel() {
        try {
            if (currentModel != null && currentModel.getIdxmodel() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "targetModel");
                criteria.setValues(new Object[]{currentModel.getIdxmodel()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ModelDependency> result = businessService.findAllEntity(ModelDependency.class, collectionParams, criterias);
                submodmodeldependenciesByTargetmodel = result != null ? result.getContent() : new ArrayList<>();
                submodmodeldependenciesByTargetmodelLoaded = true;
                log.debug("Cargados {} submodmodeldependenciesByTargetmodel", submodmodeldependenciesByTargetmodel.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar submodmodeldependenciesByTargetmodel", e);
            submodmodeldependenciesByTargetmodel = new ArrayList<>();
        }
    }
    
    private void loadSubmodmodelperformances() {
        try {
            if (currentModel != null && currentModel.getIdxmodel() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "model");
                criteria.setValues(new Object[]{currentModel.getIdxmodel()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ModelPerformance> result = businessService.findAllEntity(ModelPerformance.class, collectionParams, criterias);
                submodmodelperformances = result != null ? result.getContent() : new ArrayList<>();
                submodmodelperformancesLoaded = true;
                log.debug("Cargados {} submodmodelperformances", submodmodelperformances.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar submodmodelperformances", e);
            submodmodelperformances = new ArrayList<>();
        }
    }
    
    private void loadSubmodmodelstagetransitions() {
        try {
            if (currentModel != null && currentModel.getIdxmodel() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "model");
                criteria.setValues(new Object[]{currentModel.getIdxmodel()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ModelStageTransition> result = businessService.findAllEntity(ModelStageTransition.class, collectionParams, criterias);
                submodmodelstagetransitions = result != null ? result.getContent() : new ArrayList<>();
                submodmodelstagetransitionsLoaded = true;
                log.debug("Cargados {} submodmodelstagetransitions", submodmodelstagetransitions.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar submodmodelstagetransitions", e);
            submodmodelstagetransitions = new ArrayList<>();
        }
    }
    
    private void loadSubmodmodelvalidations() {
        try {
            if (currentModel != null && currentModel.getIdxmodel() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "model");
                criteria.setValues(new Object[]{currentModel.getIdxmodel()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ModelValidation> result = businessService.findAllEntity(ModelValidation.class, collectionParams, criterias);
                submodmodelvalidations = result != null ? result.getContent() : new ArrayList<>();
                submodmodelvalidationsLoaded = true;
                log.debug("Cargados {} submodmodelvalidations", submodmodelvalidations.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar submodmodelvalidations", e);
            submodmodelvalidations = new ArrayList<>();
        }
    }
    
    private void loadSubmodmodelversions() {
        try {
            if (currentModel != null && currentModel.getIdxmodel() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "model");
                criteria.setValues(new Object[]{currentModel.getIdxmodel()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ModelVersion> result = businessService.findAllEntity(ModelVersion.class, collectionParams, criterias);
                submodmodelversions = result != null ? result.getContent() : new ArrayList<>();
                submodmodelversionsLoaded = true;
                log.debug("Cargados {} submodmodelversions", submodmodelversions.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar submodmodelversions", e);
            submodmodelversions = new ArrayList<>();
        }
    }
    
    private void loadSubsrvdeployments() {
        try {
            if (currentModel != null && currentModel.getIdxmodel() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "model");
                criteria.setValues(new Object[]{currentModel.getIdxmodel()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ModelDeployment> result = businessService.findAllEntity(ModelDeployment.class, collectionParams, criterias);
                subsrvdeployments = result != null ? result.getContent() : new ArrayList<>();
                subsrvdeploymentsLoaded = true;
                log.debug("Cargados {} subsrvdeployments", subsrvdeployments.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subsrvdeployments", e);
            subsrvdeployments = new ArrayList<>();
        }
    }
    
    private void loadSubsrvmodelmetrics() {
        try {
            if (currentModel != null && currentModel.getIdxmodel() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "model");
                criteria.setValues(new Object[]{currentModel.getIdxmodel()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ModelMetrics> result = businessService.findAllEntity(ModelMetrics.class, collectionParams, criterias);
                subsrvmodelmetrics = result != null ? result.getContent() : new ArrayList<>();
                subsrvmodelmetricsLoaded = true;
                log.debug("Cargados {} subsrvmodelmetrics", subsrvmodelmetrics.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subsrvmodelmetrics", e);
            subsrvmodelmetrics = new ArrayList<>();
        }
    }
    
    private void loadSubsrvmodelversions() {
        try {
            if (currentModel != null && currentModel.getIdxmodel() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "model");
                criteria.setValues(new Object[]{currentModel.getIdxmodel()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ModelVersion> result = businessService.findAllEntity(ModelVersion.class, collectionParams, criterias);
                subsrvmodelversions = result != null ? result.getContent() : new ArrayList<>();
                subsrvmodelversionsLoaded = true;
                log.debug("Cargados {} subsrvmodelversions", subsrvmodelversions.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subsrvmodelversions", e);
            subsrvmodelversions = new ArrayList<>();
        }
    }
    
    @Command
    @NotifyChange("submodmodelartifacts")
    public void onSelectSubmodmodelartifactsTab() {
        if (!submodmodelartifactsLoaded) {
            loadSubmodmodelartifacts();
        }
    }
    
    @Command
    @NotifyChange("submodmodelbiasanalyses")
    public void onSelectSubmodmodelbiasanalysesTab() {
        if (!submodmodelbiasanalysesLoaded) {
            loadSubmodmodelbiasanalyses();
        }
    }
    
    @Command
    @NotifyChange("submodmodeldependencies")
    public void onSelectSubmodmodeldependenciesTab() {
        if (!submodmodeldependenciesLoaded) {
            loadSubmodmodeldependencies();
        }
    }
    
    @Command
    @NotifyChange("submodmodeldependenciesByTargetmodel")
    public void onSelectSubmodmodeldependenciesByTargetmodelTab() {
        if (!submodmodeldependenciesByTargetmodelLoaded) {
            loadSubmodmodeldependenciesByTargetmodel();
        }
    }
    
    @Command
    @NotifyChange("submodmodelperformances")
    public void onSelectSubmodmodelperformancesTab() {
        if (!submodmodelperformancesLoaded) {
            loadSubmodmodelperformances();
        }
    }
    
    @Command
    @NotifyChange("submodmodelstagetransitions")
    public void onSelectSubmodmodelstagetransitionsTab() {
        if (!submodmodelstagetransitionsLoaded) {
            loadSubmodmodelstagetransitions();
        }
    }
    
    @Command
    @NotifyChange("submodmodelvalidations")
    public void onSelectSubmodmodelvalidationsTab() {
        if (!submodmodelvalidationsLoaded) {
            loadSubmodmodelvalidations();
        }
    }
    
    @Command
    @NotifyChange("submodmodelversions")
    public void onSelectSubmodmodelversionsTab() {
        if (!submodmodelversionsLoaded) {
            loadSubmodmodelversions();
        }
    }
    
    @Command
    @NotifyChange("subsrvdeployments")
    public void onSelectSubsrvdeploymentsTab() {
        if (!subsrvdeploymentsLoaded) {
            loadSubsrvdeployments();
        }
    }
    
    @Command
    @NotifyChange("subsrvmodelmetrics")
    public void onSelectSubsrvmodelmetricsTab() {
        if (!subsrvmodelmetricsLoaded) {
            loadSubsrvmodelmetrics();
        }
    }
    
    @Command
    @NotifyChange("subsrvmodelversions")
    public void onSelectSubsrvmodelversionsTab() {
        if (!subsrvmodelversionsLoaded) {
            loadSubsrvmodelversions();
        }
    }
    
    /**
     * audita las acciones de un usuario
     * @param action - buscar, edicion ,borrar,creacion ...
     * @param model - nombre del modulo/tabla
     * @param pk  - clave primaria del registro
     * @param mensaje  -- mensaje aclaratorio, ejemplo ha creado el modelo XXXXzar excepción para que no interrumpa el flujo normal
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
            // Limpiar entidad actual
            currentModel = null;
            
            // Limpiar listas de FK
            if (availableModelProviders != null) {
                availableModelProviders.clear();
                availableModelProviders = null;
            }
            
            // Limpiar listas de LIST_STRING
            if (availableModtypes != null) {
                availableModtypes.clear();
                availableModtypes = null;
            }
            if (availableModframeworks != null) {
                availableModframeworks.clear();
                availableModframeworks = null;
            }
            if (availableModstatuss != null) {
                availableModstatuss.clear();
                availableModstatuss = null;
            }
            if (availableModapprovalstatuss != null) {
                availableModapprovalstatuss.clear();
                availableModapprovalstatuss = null;
            }
            
            // Limpiar colecciones @OneToMany
            if (submodmodelartifacts != null) {
                submodmodelartifacts.clear();
                submodmodelartifacts = null;
            }
            submodmodelartifactsLoaded = false;
            if (submodmodelbiasanalyses != null) {
                submodmodelbiasanalyses.clear();
                submodmodelbiasanalyses = null;
            }
            submodmodelbiasanalysesLoaded = false;
            if (submodmodeldependencies != null) {
                submodmodeldependencies.clear();
                submodmodeldependencies = null;
            }
            submodmodeldependenciesLoaded = false;
            if (submodmodeldependenciesByTargetmodel != null) {
                submodmodeldependenciesByTargetmodel.clear();
                submodmodeldependenciesByTargetmodel = null;
            }
            submodmodeldependenciesByTargetmodelLoaded = false;
            if (submodmodelperformances != null) {
                submodmodelperformances.clear();
                submodmodelperformances = null;
            }
            submodmodelperformancesLoaded = false;
            if (submodmodelstagetransitions != null) {
                submodmodelstagetransitions.clear();
                submodmodelstagetransitions = null;
            }
            submodmodelstagetransitionsLoaded = false;
            if (submodmodelvalidations != null) {
                submodmodelvalidations.clear();
                submodmodelvalidations = null;
            }
            submodmodelvalidationsLoaded = false;
            if (submodmodelversions != null) {
                submodmodelversions.clear();
                submodmodelversions = null;
            }
            submodmodelversionsLoaded = false;
            if (subsrvdeployments != null) {
                subsrvdeployments.clear();
                subsrvdeployments = null;
            }
            subsrvdeploymentsLoaded = false;
            if (subsrvmodelmetrics != null) {
                subsrvmodelmetrics.clear();
                subsrvmodelmetrics = null;
            }
            subsrvmodelmetricsLoaded = false;
            if (subsrvmodelversions != null) {
                subsrvmodelversions.clear();
                subsrvmodelversions = null;
            }
            subsrvmodelversionsLoaded = false;
            
            // Limpiar tags/roles JSONB
            if (selectedModtags != null) {
                selectedModtags.clear();
                selectedModtags = null;
            }
            newModtag = null;
            
            // Limpiar validadores
            unique = null;
            
            // Limpiar BusinessService
            businessService = null;
            
            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }
    
    // ========== COMPLIANCE CHECKLIST ==========
    
    /**
     * Obtiene el checklist de compliance EU AI Act
     */
    public List<com.codeflowx.platform.dto.ComplianceCheck> getComplianceChecklist() {
        List<com.codeflowx.platform.dto.ComplianceCheck> checklist = new ArrayList<>();
        
        if (currentModel == null) {
            return checklist;
        }
        
        // 1. Risk Classification documentada
        checklist.add(com.codeflowx.platform.dto.ComplianceCheck.builder()
            .id("risk_classification")
            .name("Risk Classification")
            .description("Clasificación de riesgo del modelo documentada")
            .passed(currentModel.getModrisklevel() != null && !currentModel.getModrisklevel().isEmpty())
            .completedDate(currentModel.getModcreatedat() != null ? currentModel.getModcreatedat().toString() : "")
            .priority("REQUIRED")
            .build());
        
        // 2. Dataset Quality validado
        boolean hasDataset = false; // TODO: Check if dataset is associated
        checklist.add(com.codeflowx.platform.dto.ComplianceCheck.builder()
            .id("dataset_quality")
            .name("Dataset Quality")
            .description("Calidad del dataset validada")
            .passed(hasDataset)
            .priority("REQUIRED")
            .build());
        
        // 3. Bias Analysis realizado
        boolean hasBiasAnalysis = currentModel.getSubmodmodelbiasanalyses() != null && 
                                  !currentModel.getSubmodmodelbiasanalyses().isEmpty();
        String biasAnalysisDate = "";
        if (hasBiasAnalysis && !currentModel.getSubmodmodelbiasanalyses().isEmpty()) {
            ModelBiasAnalysis lastAnalysis = currentModel.getSubmodmodelbiasanalyses().get(0);
            biasAnalysisDate = lastAnalysis.getModanalysisdate() != null ? 
                             lastAnalysis.getModanalysisdate().toString() : "";
        }
        checklist.add(com.codeflowx.platform.dto.ComplianceCheck.builder()
            .id("bias_analysis")
            .name("Bias Analysis")
            .description("Análisis de sesgo realizado")
            .passed(hasBiasAnalysis)
            .completedDate(biasAnalysisDate)
            .priority("REQUIRED")
            .build());
        
        // 4. Performance Metrics documentadas
        boolean hasPerformance = currentModel.getModperformancemetrics() != null && 
                                !currentModel.getModperformancemetrics().isEmpty();
        checklist.add(com.codeflowx.platform.dto.ComplianceCheck.builder()
            .id("performance_metrics")
            .name("Performance Metrics")
            .description("Métricas de rendimiento documentadas")
            .passed(hasPerformance)
            .completedDate(hasPerformance ? currentModel.getModcreatedat().toString() : "")
            .priority("REQUIRED")
            .build());
        
        // 5. Modelo aprobado
        boolean isApproved = "APPROVED".equals(currentModel.getModstatus());
        checklist.add(com.codeflowx.platform.dto.ComplianceCheck.builder()
            .id("model_approved")
            .name("Model Approved")
            .description("Modelo aprobado por Governance")
            .passed(isApproved)
            .completedDate(currentModel.getModapprovedat() != null ? currentModel.getModapprovedat().toString() : "")
            .priority("REQUIRED")
            .build());
        
        // 6. Audit Trail disponible
        boolean hasAuditTrail = true; // Always true if model exists
        checklist.add(com.codeflowx.platform.dto.ComplianceCheck.builder()
            .id("audit_trail")
            .name("Audit Trail")
            .description("Trazabilidad de cambios disponible")
            .passed(hasAuditTrail)
            .completedDate(currentModel.getModcreatedat() != null ? currentModel.getModcreatedat().toString() : "")
            .priority("REQUIRED")
            .build());
        
        return checklist;
    }
    
    /**
     * Obtiene el score de compliance (X/6)
     */
    public String getComplianceScore() {
        List<com.codeflowx.platform.dto.ComplianceCheck> checklist = getComplianceChecklist();
        long passed = checklist.stream().filter(com.codeflowx.platform.dto.ComplianceCheck::isPassed).count();
        return passed + "/" + checklist.size();
    }
    
    /**
     * Obtiene el porcentaje de compliance
     */
    public int getCompliancePercentage() {
        List<com.codeflowx.platform.dto.ComplianceCheck> checklist = getComplianceChecklist();
        if (checklist.isEmpty()) return 0;
        long passed = checklist.stream().filter(com.codeflowx.platform.dto.ComplianceCheck::isPassed).count();
        return (int) ((passed * 100) / checklist.size());
    }
    
    /**
     * Obtiene el estado de compliance
     */
    public String getComplianceStatus() {
        List<com.codeflowx.platform.dto.ComplianceCheck> checklist = getComplianceChecklist();
        long passed = checklist.stream().filter(com.codeflowx.platform.dto.ComplianceCheck::isPassed).count();
        
        if (passed == checklist.size()) {
            return "COMPLIANT";
        } else if (passed >= 4) {
            return "PARTIAL";
        } else {
            return "NON_COMPLIANT";
        }
    }
    
    /**
     * Obtiene el label del estado de compliance
     */
    public String getComplianceStatusLabel() {
        String status = getComplianceStatus();
        switch (status) {
            case "COMPLIANT":
                return "✓ COMPLIANT";
            case "PARTIAL":
                return "⚠ PARTIAL COMPLIANCE";
            default:
                return "✗ NON-COMPLIANT";
        }
    }
    
    /**
     * Obtiene los análisis de sesgo recientes (últimos 3)
     */
    public List<ModelBiasAnalysis> getRecentBiasAnalyses() {
        if (currentModel == null || currentModel.getSubmodmodelbiasanalyses() == null) {
            return new ArrayList<>();
        }
        
        List<ModelBiasAnalysis> analyses = currentModel.getSubmodmodelbiasanalyses();
        return analyses.size() > 3 ? analyses.subList(0, 3) : analyses;
    }
    
    // ========== COMANDOS ADICIONALES ==========
    
    /**
     * Comando para editar el modelo
     */
    @Command
    public void editItem() {
        Map<String, Object> params = new HashMap<>();
        params.put("action", Action.LOAD);
        params.put("dataParam", currentModel.getIdxmodel());
        appendPage("plataforma/models/create/page.zul", page.getFellow(IDDESKTOP), params);
    }
    
    /**
     * Comando para analizar sesgo
     */
    @Command
    public void analyzeBias() {
        Map<String, Object> params = new HashMap<>();
        params.put("modelId", currentModel.getIdxmodel());
        appendPage("plataforma/models/bias-analysis/overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    /**
     * Comando para enviar a aprobación
     */
    @Command
    @NotifyChange("currentModel")
    public void submitForApproval() {
        try {
            if (currentModel == null || !"DRAFT".equals(currentModel.getModstatus())) {
                Messagebox.show("Solo los modelos en estado DRAFT pueden enviarse a aprobación", "Error", 
                              Messagebox.OK, Messagebox.ERROR);
                return;
            }
            
            // TODO: Crear registro en ModelApproval
            
            // Cambiar estado a IN_REVIEW
            currentModel.setModstatus("IN_REVIEW");
            businessService.save(currentModel);
            
            Messagebox.show("Modelo enviado a aprobación correctamente", "Éxito", 
                          Messagebox.OK, Messagebox.INFORMATION);
            
            logActivity("SUBMIT_APPROVAL", "Modelo enviado a aprobación");
            
        } catch (Exception e) {
            log.error("Error al enviar modelo a aprobación: {}", e.getMessage(), e);
            Messagebox.show("Error al enviar a aprobación: " + e.getMessage(), "Error", 
                          Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    /**
     * Comando para ver audit trail
     */
    @Command
    public void viewAuditTrail() {
        Map<String, Object> params = new HashMap<>();
        params.put("modelId", currentModel.getIdxmodel());
        params.put("entity", "Model");
        appendPage("plataforma/governance/audit/trail.zul", page.getFellow(IDDESKTOP), params);
    }
}
