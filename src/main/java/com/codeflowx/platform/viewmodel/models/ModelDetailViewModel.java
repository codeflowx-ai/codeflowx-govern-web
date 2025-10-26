package com.codeflowx.platform.viewmodel.models;

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
public class ModelDetailViewModel extends MasterPage {
    
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
        
        // Obtener parámetros de navegación
        mode = (String) super.action.name();
        
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
            appendPage("gobierno/models/models-overview.zul", page.getFellow(IDDESKTOP), params);
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
     * @param mensaje  -- mensaje aclaratorio, ejemplo ha creado el modelo XXXX
     * @throws DaoException
     * @throws UiException
     */
    private void logActivity(String action, String model, Long pk, String mensaje) throws DaoException, UiException {
        try {
            Ssoractividad log = new Ssoractividad();
            log.setUsername(getUser().getUsername());
            log.setAccion(action);
            log.setAlta(new java.sql.Timestamp(System.currentTimeMillis()));
            log.setModulo(model);
            log.setIdtupla(pk != null ? pk.intValue() : 0);
            log.setAplicacion(ctxBean.getApplicationName());
            log.setValuetupla(mensaje);
            businessService.save(log);
        } catch (Exception e) {
            log.error("Error al auditar acción: {} en módulo: {}", action, model, e);
            // No lanzar excepción para que no interrumpa el flujo normal
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
}
