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
import com.codeflowx.govern.entity.models.ModelCatalog;
import com.codeflowx.govern.entity.models.ModelCapability;
import com.codeflowx.govern.entity.models.ModelEndpoint;
import com.codeflowx.govern.entity.models.ModelUsage;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de ModelCatalog
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ModelCatalogDetailViewModel extends MasterPage {
    
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
    private Long idxmodelcatalog;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private ModelCatalog currentModelCatalog;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    private String originalModmodelname = null;
    private String originalModdisplayname = null;
    
    // ========== Listas para combos (FK) ==========
    private List<String> availableModstatuss = new ArrayList<>();
    private List<String> availableModlicensetypes = new ArrayList<>();
    
    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    
    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    private List<ModelCapability> submodcapabilities = new ArrayList<>();
    private List<ModelEndpoint> submodendpoints = new ArrayList<>();
    private List<ModelUsage> submodusage = new ArrayList<>();
    private boolean submodcapabilitiesLoaded = false;
    private boolean submodendpointsLoaded = false;
    private boolean submodusageLoaded = false;
    
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
            idxmodelcatalog = Long.valueOf(String.valueOf(dataParam));
        }
        
        log.info("Inicializando ModelCatalogDetailViewModel - mode: {}, idxmodelcatalog: {}", mode, idxmodelcatalog);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxmodelcatalog != null) {
            loadItem(idxmodelcatalog);
        } else {
            log.error("Modo inválido o falta idxmodelcatalog");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/models/models-overview.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentModelCatalog, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentModelCatalog = new ModelCatalog();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadModstatuss();
        loadModlicensetypes();
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            // findById siempre recibe Long id (el PK)
            currentModelCatalog = businessService.findById(ModelCatalog.class, id);
            
            if (currentModelCatalog == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/models/models-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentModelCatalog.getModmodelname();
        loadModstatuss();
        loadModlicensetypes();
            
            // Cargar tags/roles existentes desde JSON
            
            // Guardar valores originales para validación de unicidad
            originalModmodelname = currentModelCatalog.getModmodelname();
            originalModdisplayname = currentModelCatalog.getModdisplayname();
            
            // Auditar carga de registro
            logActivity("CONSULTA", "MODCATALOG", id, "Consulta: " + currentModelCatalog.getModmodelname());
            
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
            
            boolean isNew = currentModelCatalog.getIdxmodelcatalog() == null;
            
            if (isNew) {
                businessService.save(currentModelCatalog);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "MODCATALOG", currentModelCatalog.getIdxmodelcatalog(), 
                    "Creado: " + currentModelCatalog.getModmodelname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                businessService.update(currentModelCatalog);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "MODCATALOG", currentModelCatalog.getIdxmodelcatalog(), 
                    "Actualizado: " + currentModelCatalog.getModmodelname());
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
        
        if (currentModelCatalog.getModmodelname() == null || currentModelCatalog.getModmodelname().trim().isEmpty()) {
            errors.append("- Model Name\n");
        }
        if (currentModelCatalog.getModmodelname() != null && currentModelCatalog.getModmodelname().length() > 255) {
            errors.append("- Model Name no puede exceder 255 caracteres\n");
        }
        if (currentModelCatalog.getModmodelid() == null || currentModelCatalog.getModmodelid().trim().isEmpty()) {
            errors.append("- Model Id\n");
        }
        if (currentModelCatalog.getModmodelid() != null && currentModelCatalog.getModmodelid().length() > 255) {
            errors.append("- Model Id no puede exceder 255 caracteres\n");
        }
        if (currentModelCatalog.getModstatus() == null || currentModelCatalog.getModstatus().trim().isEmpty()) {
            errors.append("- Status\n");
        }
        if (currentModelCatalog.getModcreatedby() == null || currentModelCatalog.getModcreatedby().trim().isEmpty()) {
            errors.append("- Created By\n");
        }
        if (currentModelCatalog.getModcreatedby() != null && currentModelCatalog.getModcreatedby().length() > 255) {
            errors.append("- Created By no puede exceder 255 caracteres\n");
        }
        if (currentModelCatalog.getModcreatedat() == null) {
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
        params.put("dataParam", idxmodelcatalog);
        params.put("action", Action.LOAD);
        appendPage("plataforma/models/models-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    private void loadModstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableModstatuss.add("OPTION_1");
        availableModstatuss.add("OPTION_2");
        availableModstatuss.add("OPTION_3");
    }
    
    private void loadModlicensetypes() {
        // TODO: Cargar valores desde configuración o BD
        availableModlicensetypes.add("OPTION_1");
        availableModlicensetypes.add("OPTION_2");
        availableModlicensetypes.add("OPTION_3");
    }
    
    private void loadSubmodcapabilities() {
        try {
            if (currentModelCatalog != null && currentModelCatalog.getIdxmodelcatalog() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "modelCatalog");
                criteria.setValues(new Object[]{currentModelCatalog.getIdxmodelcatalog()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ModelCapability> result = businessService.findAllEntity(ModelCapability.class, collectionParams, criterias);
                submodcapabilities = result != null ? result.getContent() : new ArrayList<>();
                submodcapabilitiesLoaded = true;
                log.debug("Cargados {} submodcapabilities", submodcapabilities.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar submodcapabilities", e);
            submodcapabilities = new ArrayList<>();
        }
    }
    
    private void loadSubmodendpoints() {
        try {
            if (currentModelCatalog != null && currentModelCatalog.getIdxmodelcatalog() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "modelCatalog");
                criteria.setValues(new Object[]{currentModelCatalog.getIdxmodelcatalog()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ModelEndpoint> result = businessService.findAllEntity(ModelEndpoint.class, collectionParams, criterias);
                submodendpoints = result != null ? result.getContent() : new ArrayList<>();
                submodendpointsLoaded = true;
                log.debug("Cargados {} submodendpoints", submodendpoints.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar submodendpoints", e);
            submodendpoints = new ArrayList<>();
        }
    }
    
    private void loadSubmodusage() {
        try {
            if (currentModelCatalog != null && currentModelCatalog.getIdxmodelcatalog() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "modelCatalog");
                criteria.setValues(new Object[]{currentModelCatalog.getIdxmodelcatalog()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ModelUsage> result = businessService.findAllEntity(ModelUsage.class, collectionParams, criterias);
                submodusage = result != null ? result.getContent() : new ArrayList<>();
                submodusageLoaded = true;
                log.debug("Cargados {} submodusage", submodusage.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar submodusage", e);
            submodusage = new ArrayList<>();
        }
    }
    
    @Command
    @NotifyChange("submodcapabilities")
    public void onSelectSubmodcapabilitiesTab() {
        if (!submodcapabilitiesLoaded) {
            loadSubmodcapabilities();
        }
    }
    
    @Command
    @NotifyChange("submodendpoints")
    public void onSelectSubmodendpointsTab() {
        if (!submodendpointsLoaded) {
            loadSubmodendpoints();
        }
    }
    
    @Command
    @NotifyChange("submodusage")
    public void onSelectSubmodusageTab() {
        if (!submodusageLoaded) {
            loadSubmodusage();
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
            currentModelCatalog = null;
            
            // Limpiar listas de FK
            
            // Limpiar listas de LIST_STRING
            if (availableModstatuss != null) {
                availableModstatuss.clear();
                availableModstatuss = null;
            }
            if (availableModlicensetypes != null) {
                availableModlicensetypes.clear();
                availableModlicensetypes = null;
            }
            
            // Limpiar colecciones @OneToMany
            if (submodcapabilities != null) {
                submodcapabilities.clear();
                submodcapabilities = null;
            }
            submodcapabilitiesLoaded = false;
            if (submodendpoints != null) {
                submodendpoints.clear();
                submodendpoints = null;
            }
            submodendpointsLoaded = false;
            if (submodusage != null) {
                submodusage.clear();
                submodusage = null;
            }
            submodusageLoaded = false;
            
            // Limpiar tags/roles JSONB
            
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
