package com.codeflowx.platform.viewmodel.models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.enartframework.suinsit.Context;
import javax.sql.DataSource;
import org.enartframework.nocode.dao.IEntityLocal;
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
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import com.codeflowx.govern.entity.models.ModelVersion;
import com.codeflowx.govern.entity.models.ModelArtifact;
import com.codeflowx.govern.entity.models.ModelStageTransition;
import com.codeflowx.govern.entity.models.ModelValidation;
import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.framework.validators.UniqueValidator;
import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import org.enartframework.orm.exception.DaoException;
import org.zkoss.zk.ui.UiException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de ModelVersion
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ModelVersionDetailViewModel extends MasterPage {
    
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
    private Long idxmodelversion;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private ModelVersion currentModelVersion;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    
    // ========== Listas para combos (FK) ==========
    private List<String> availableModstatuss = new ArrayList<>();
    
    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    private List<String> selectedModtags = new ArrayList<>();
    private String newModtag = "";
    
    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    private List<ModelArtifact> submodmodelartifacts = new ArrayList<>();
    private List<ModelStageTransition> submodmodelstagetransitions = new ArrayList<>();
    private List<ModelValidation> submodmodelvalidations = new ArrayList<>();
    private boolean submodmodelartifactsLoaded = false;
    private boolean submodmodelstagetransitionsLoaded = false;
    private boolean submodmodelvalidationsLoaded = false;
    
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
            idxmodelversion = Long.valueOf(String.valueOf(dataParam));
        }
        
        log.info("Inicializando ModelVersionDetailViewModel - mode: {}, idxmodelversion: {}", mode, idxmodelversion);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxmodelversion != null) {
            loadItem(idxmodelversion);
        } else {
            log.error("Modo inválido o falta idxmodelversion");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/models/models-overview.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentModelVersion, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentModelVersion = new ModelVersion();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadModstatuss();
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            // findById siempre recibe Long id (el PK)
            currentModelVersion = businessService.findById(ModelVersion.class, id);
            
            if (currentModelVersion == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/models/models-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentModelVersion.getModdescription();
        loadModstatuss();
            
            // Cargar tags/roles existentes desde JSON
            selectedModtags = convertJsonToList(currentModelVersion.getModtags());
            
            // Guardar valores originales para validación de unicidad
            
            // Auditar carga de registro
            logActivity("CONSULTA", "MODMODELVERSIONS", id, "Consulta: " + currentModelVersion.getModdescription());
            
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
            
            boolean isNew = currentModelVersion.getIdxmodelversion() == null;
            
            if (isNew) {
                businessService.save(currentModelVersion);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "MODMODELVERSIONS", currentModelVersion.getIdxmodelversion(), 
                    "Creado: " + currentModelVersion.getModdescription());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                businessService.update(currentModelVersion);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "MODMODELVERSIONS", currentModelVersion.getIdxmodelversion(), 
                    "Actualizado: " + currentModelVersion.getModdescription());
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
        
        if (currentModelVersion.getModversion() == null || currentModelVersion.getModversion().trim().isEmpty()) {
            errors.append("- Version\n");
        }
        if (currentModelVersion.getModversion() != null && currentModelVersion.getModversion().length() > 50) {
            errors.append("- Version no puede exceder 50 caracteres\n");
        }
        if (currentModelVersion.getModstatus() == null || currentModelVersion.getModstatus().trim().isEmpty()) {
            errors.append("- Status\n");
        }
        if (currentModelVersion.getModcreatedby() == null || currentModelVersion.getModcreatedby().trim().isEmpty()) {
            errors.append("- Created By\n");
        }
        if (currentModelVersion.getModcreatedby() != null && currentModelVersion.getModcreatedby().length() > 255) {
            errors.append("- Created By no puede exceder 255 caracteres\n");
        }
        if (currentModelVersion.getModcreatedat() == null) {
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
        params.put("dataParam", idxmodelversion);
        params.put("action", Action.LOAD);
        appendPage("plataforma/models/models-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    private void loadModstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableModstatuss.add("OPTION_1");
        availableModstatuss.add("OPTION_2");
        availableModstatuss.add("OPTION_3");
    }
    
    @Command
    @NotifyChange("{'selectedModtags', 'currentModelVersion'}")
    public void addModtag() {
        if (newModtag != null && !newModtag.trim().isEmpty() && !selectedModtags.contains(newModtag.trim())) {
            selectedModtags.add(newModtag.trim());
            newModtag = "";
            // Convertir lista a JSON y actualizar en currentModelVersion
            currentModelVersion.setModtags(convertListToJson(selectedModtags));
        }
    }
    
    @Command
    @NotifyChange("{'selectedModtags', 'currentModelVersion'}")
    public void removeModtag(@BindingParam("tag") String tag) {
        selectedModtags.remove(tag);
        // Convertir lista a JSON y actualizar en currentModelVersion
        currentModelVersion.setModtags(convertListToJson(selectedModtags));
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
            if (currentModelVersion != null && currentModelVersion.getIdxmodelversion() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "version");
                criteria.setValues(new Object[]{currentModelVersion.getIdxmodelversion()});
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
    
    private void loadSubmodmodelstagetransitions() {
        try {
            if (currentModelVersion != null && currentModelVersion.getIdxmodelversion() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "version");
                criteria.setValues(new Object[]{currentModelVersion.getIdxmodelversion()});
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
            if (currentModelVersion != null && currentModelVersion.getIdxmodelversion() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "version");
                criteria.setValues(new Object[]{currentModelVersion.getIdxmodelversion()});
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
    
    @Command
    @NotifyChange("submodmodelartifacts")
    public void onSelectSubmodmodelartifactsTab() {
        if (!submodmodelartifactsLoaded) {
            loadSubmodmodelartifacts();
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
            currentModelVersion = null;
            
            // Limpiar listas de FK
            
            // Limpiar listas de LIST_STRING
            if (availableModstatuss != null) {
                availableModstatuss.clear();
                availableModstatuss = null;
            }
            
            // Limpiar colecciones @OneToMany
            if (submodmodelartifacts != null) {
                submodmodelartifacts.clear();
                submodmodelartifacts = null;
            }
            submodmodelartifactsLoaded = false;
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
