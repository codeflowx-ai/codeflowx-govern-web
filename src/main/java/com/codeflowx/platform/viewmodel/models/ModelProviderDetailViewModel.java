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
import com.codeflowx.govern.entity.models.ModelProvider;
import com.codeflowx.govern.entity.models.ModelCatalog;
import com.codeflowx.govern.entity.models.ProviderCredential;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de ModelProvider
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ModelProviderDetailViewModel extends MasterPage {
    
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
    private Long idxmodelprovider;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private ModelProvider currentModelProvider;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    private String originalModname = null;
    private String originalModdisplayname = null;
    private String originalModcontactemail = null;
    
    // ========== Listas para combos (FK) ==========
    private List<String> availableModprovidertypes = new ArrayList<>();
    private List<String> availableModstatuss = new ArrayList<>();
    private List<String> availableModauthtypes = new ArrayList<>();
    
    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    
    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    private List<ModelCatalog> submodcatalog = new ArrayList<>();
    private List<ProviderCredential> submodprovidercredentials = new ArrayList<>();
    private boolean submodcatalogLoaded = false;
    private boolean submodprovidercredentialsLoaded = false;
    
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
            idxmodelprovider = Long.valueOf(String.valueOf(dataParam));
        }
        
        log.info("Inicializando ModelProviderDetailViewModel - mode: {}, idxmodelprovider: {}", mode, idxmodelprovider);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxmodelprovider != null) {
            loadItem(idxmodelprovider);
        } else {
            log.error("Modo inválido o falta idxmodelprovider");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/models/models-overview.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentModelProvider, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentModelProvider = new ModelProvider();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadModprovidertypes();
        loadModstatuss();
        loadModauthtypes();
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            // findById siempre recibe Long id (el PK)
            currentModelProvider = businessService.findById(ModelProvider.class, id);
            
            if (currentModelProvider == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/models/models-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentModelProvider.getModname();
        loadModprovidertypes();
        loadModstatuss();
        loadModauthtypes();
            
            // Cargar tags/roles existentes desde JSON
            
            // Guardar valores originales para validación de unicidad
            originalModname = currentModelProvider.getModname();
            originalModdisplayname = currentModelProvider.getModdisplayname();
            originalModcontactemail = currentModelProvider.getModcontactemail();
            
            // Auditar carga de registro
            logActivity("CONSULTA", "MODPROVIDERS", id, "Consulta: " + currentModelProvider.getModname());
            
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
            
            boolean isNew = currentModelProvider.getIdxmodelprovider() == null;
            
            if (isNew) {
                businessService.save(currentModelProvider);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "MODPROVIDERS", currentModelProvider.getIdxmodelprovider(), 
                    "Creado: " + currentModelProvider.getModname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                businessService.update(currentModelProvider);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "MODPROVIDERS", currentModelProvider.getIdxmodelprovider(), 
                    "Actualizado: " + currentModelProvider.getModname());
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
        
        if (currentModelProvider.getModname() == null || currentModelProvider.getModname().trim().isEmpty()) {
            errors.append("- Name\n");
        }
        if (currentModelProvider.getModname() != null && currentModelProvider.getModname().length() > 100) {
            errors.append("- Name no puede exceder 100 caracteres\n");
        }
        if (currentModelProvider.getModprovidertype() == null || currentModelProvider.getModprovidertype().trim().isEmpty()) {
            errors.append("- Provider Type\n");
        }
        if (currentModelProvider.getModstatus() == null || currentModelProvider.getModstatus().trim().isEmpty()) {
            errors.append("- Status\n");
        }
        if (currentModelProvider.getModcreatedby() == null || currentModelProvider.getModcreatedby().trim().isEmpty()) {
            errors.append("- Created By\n");
        }
        if (currentModelProvider.getModcreatedby() != null && currentModelProvider.getModcreatedby().length() > 255) {
            errors.append("- Created By no puede exceder 255 caracteres\n");
        }
        if (currentModelProvider.getModcreatedat() == null) {
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
        params.put("dataParam", idxmodelprovider);
        params.put("action", Action.LOAD);
        appendPage("plataforma/models/models-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    private void loadModprovidertypes() {
        // TODO: Cargar valores desde configuración o BD
        availableModprovidertypes.add("OPTION_1");
        availableModprovidertypes.add("OPTION_2");
        availableModprovidertypes.add("OPTION_3");
    }
    
    private void loadModstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableModstatuss.add("OPTION_1");
        availableModstatuss.add("OPTION_2");
        availableModstatuss.add("OPTION_3");
    }
    
    private void loadModauthtypes() {
        // TODO: Cargar valores desde configuración o BD
        availableModauthtypes.add("OPTION_1");
        availableModauthtypes.add("OPTION_2");
        availableModauthtypes.add("OPTION_3");
    }
    
    private void loadSubmodcatalog() {
        try {
            if (currentModelProvider != null && currentModelProvider.getIdxmodelprovider() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "provider");
                criteria.setValues(new Object[]{currentModelProvider.getIdxmodelprovider()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ModelCatalog> result = businessService.findAllEntity(ModelCatalog.class, collectionParams, criterias);
                submodcatalog = result != null ? result.getContent() : new ArrayList<>();
                submodcatalogLoaded = true;
                log.debug("Cargados {} submodcatalog", submodcatalog.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar submodcatalog", e);
            submodcatalog = new ArrayList<>();
        }
    }
    
    private void loadSubmodprovidercredentials() {
        try {
            if (currentModelProvider != null && currentModelProvider.getIdxmodelprovider() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "provider");
                criteria.setValues(new Object[]{currentModelProvider.getIdxmodelprovider()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ProviderCredential> result = businessService.findAllEntity(ProviderCredential.class, collectionParams, criterias);
                submodprovidercredentials = result != null ? result.getContent() : new ArrayList<>();
                submodprovidercredentialsLoaded = true;
                log.debug("Cargados {} submodprovidercredentials", submodprovidercredentials.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar submodprovidercredentials", e);
            submodprovidercredentials = new ArrayList<>();
        }
    }
    
    @Command
    @NotifyChange("submodcatalog")
    public void onSelectSubmodcatalogTab() {
        if (!submodcatalogLoaded) {
            loadSubmodcatalog();
        }
    }
    
    @Command
    @NotifyChange("submodprovidercredentials")
    public void onSelectSubmodprovidercredentialsTab() {
        if (!submodprovidercredentialsLoaded) {
            loadSubmodprovidercredentials();
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
            currentModelProvider = null;
            
            // Limpiar listas de FK
            
            // Limpiar listas de LIST_STRING
            if (availableModprovidertypes != null) {
                availableModprovidertypes.clear();
                availableModprovidertypes = null;
            }
            if (availableModstatuss != null) {
                availableModstatuss.clear();
                availableModstatuss = null;
            }
            if (availableModauthtypes != null) {
                availableModauthtypes.clear();
                availableModauthtypes = null;
            }
            
            // Limpiar colecciones @OneToMany
            if (submodcatalog != null) {
                submodcatalog.clear();
                submodcatalog = null;
            }
            submodcatalogLoaded = false;
            if (submodprovidercredentials != null) {
                submodprovidercredentials.clear();
                submodprovidercredentials = null;
            }
            submodprovidercredentialsLoaded = false;
            
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
