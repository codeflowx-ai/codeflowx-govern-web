package com.codeflowx.platform.viewmodel.agents;

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
import com.codeflowx.govern.entity.agents.AgentTool;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de AgentTool
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class AgentToolDetailViewModel extends MasterPage {
    
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
    private Long idxagenttool;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private AgentTool currentAgentTool;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    private String originalAgttoolname = null;
    
    // ========== Listas para combos (FK) ==========
    private List<String> availableAgttooltypes = new ArrayList<>();
    private List<String> availableAgtstatuss = new ArrayList<>();
    private List<String> availableAgtauthenticationtypes = new ArrayList<>();
    private List<String> availableAgtprioritys = new ArrayList<>();
    private List<String> availableAgtlicensetypes = new ArrayList<>();
    
    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    
    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        // Obtener parámetros de navegación
        mode = (String) super.action.name();
        
        // dataParam siempre contiene el ID (PK de tipo Long)
        if (dataParam != null) {
            idxagenttool = Long.valueOf(String.valueOf(dataParam));
        }
        
        log.info("Inicializando AgentToolDetailViewModel - mode: {}, idxagenttool: {}", mode, idxagenttool);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxagenttool != null) {
            loadItem(idxagenttool);
        } else {
            log.error("Modo inválido o falta idxagenttool");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("gobierno/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentAgentTool, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentAgentTool = new AgentTool();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadAgttooltypes();
        loadAgtstatuss();
        loadAgtauthenticationtypes();
        loadAgtprioritys();
        loadAgtlicensetypes();
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            // findById siempre recibe Long id (el PK)
            currentAgentTool = businessService.findById(AgentTool.class, id);
            
            if (currentAgentTool == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentAgentTool.getAgttoolname();
        loadAgttooltypes();
        loadAgtstatuss();
        loadAgtauthenticationtypes();
        loadAgtprioritys();
        loadAgtlicensetypes();
            
            // Cargar tags/roles existentes desde JSON
            
            // Guardar valores originales para validación de unicidad
            originalAgttoolname = currentAgentTool.getAgttoolname();
            
            // Auditar carga de registro
            logActivity("CONSULTA", "AGTAGENTTOOLS", id, "Consulta: " + currentAgentTool.getAgttoolname());
            
        } catch (Exception e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
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
            
            boolean isNew = currentAgentTool.getIdxagenttool() == null;
            
            if (isNew) {
                businessService.save(currentAgentTool);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "AGTAGENTTOOLS", currentAgentTool.getIdxagenttool(), 
                    "Creado: " + currentAgentTool.getAgttoolname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                businessService.update(currentAgentTool);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "AGTAGENTTOOLS", currentAgentTool.getIdxagenttool(), 
                    "Actualizado: " + currentAgentTool.getAgttoolname());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }
            
            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
            
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
        
        if (currentAgentTool.getAgttoolname() == null || currentAgentTool.getAgttoolname().trim().isEmpty()) {
            errors.append("- Tool Name\n");
        }
        if (currentAgentTool.getAgttoolname() != null && currentAgentTool.getAgttoolname().length() > 255) {
            errors.append("- Tool Name no puede exceder 255 caracteres\n");
        }
        if (currentAgentTool.getAgttooltype() == null || currentAgentTool.getAgttooltype().trim().isEmpty()) {
            errors.append("- Tool Type\n");
        }
        if (currentAgentTool.getAgtcategory() == null || currentAgentTool.getAgtcategory().trim().isEmpty()) {
            errors.append("- Category\n");
        }
        if (currentAgentTool.getAgtcategory() != null && currentAgentTool.getAgtcategory().length() > 100) {
            errors.append("- Category no puede exceder 100 caracteres\n");
        }
        if (currentAgentTool.getAgtstatus() == null || currentAgentTool.getAgtstatus().trim().isEmpty()) {
            errors.append("- Status\n");
        }
        if (currentAgentTool.getAgtcreatedby() == null || currentAgentTool.getAgtcreatedby().trim().isEmpty()) {
            errors.append("- Created By\n");
        }
        if (currentAgentTool.getAgtcreatedby() != null && currentAgentTool.getAgtcreatedby().length() > 255) {
            errors.append("- Created By no puede exceder 255 caracteres\n");
        }
        if (currentAgentTool.getAgtcreatedat() == null) {
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
        params.put("dataParam", idxagenttool);
        params.put("action", Action.LOAD);
        appendPage("plataforma/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    private void loadAgttooltypes() {
        // TODO: Cargar valores desde configuración o BD
        availableAgttooltypes.add("OPTION_1");
        availableAgttooltypes.add("OPTION_2");
        availableAgttooltypes.add("OPTION_3");
    }
    
    private void loadAgtstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableAgtstatuss.add("OPTION_1");
        availableAgtstatuss.add("OPTION_2");
        availableAgtstatuss.add("OPTION_3");
    }
    
    private void loadAgtauthenticationtypes() {
        // TODO: Cargar valores desde configuración o BD
        availableAgtauthenticationtypes.add("OPTION_1");
        availableAgtauthenticationtypes.add("OPTION_2");
        availableAgtauthenticationtypes.add("OPTION_3");
    }
    
    private void loadAgtprioritys() {
        // TODO: Cargar valores desde configuración o BD
        availableAgtprioritys.add("OPTION_1");
        availableAgtprioritys.add("OPTION_2");
        availableAgtprioritys.add("OPTION_3");
    }
    
    private void loadAgtlicensetypes() {
        // TODO: Cargar valores desde configuración o BD
        availableAgtlicensetypes.add("OPTION_1");
        availableAgtlicensetypes.add("OPTION_2");
        availableAgtlicensetypes.add("OPTION_3");
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
            currentAgentTool = null;
            
            // Limpiar listas de FK
            
            // Limpiar listas de LIST_STRING
            if (availableAgttooltypes != null) {
                availableAgttooltypes.clear();
                availableAgttooltypes = null;
            }
            if (availableAgtstatuss != null) {
                availableAgtstatuss.clear();
                availableAgtstatuss = null;
            }
            if (availableAgtauthenticationtypes != null) {
                availableAgtauthenticationtypes.clear();
                availableAgtauthenticationtypes = null;
            }
            if (availableAgtprioritys != null) {
                availableAgtprioritys.clear();
                availableAgtprioritys = null;
            }
            if (availableAgtlicensetypes != null) {
                availableAgtlicensetypes.clear();
                availableAgtlicensetypes = null;
            }
            
            // Limpiar colecciones @OneToMany
            
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
