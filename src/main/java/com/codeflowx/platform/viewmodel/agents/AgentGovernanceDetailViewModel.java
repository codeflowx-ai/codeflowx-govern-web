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
import com.codeflowx.govern.entity.agents.AgentGovernance;
import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.framework.validators.UniqueValidator;
import codeflowx.nocode.persist.BusinessService;
import com.codeflowx.govern.service.agents.AgentGovernanceService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;
import com.codeflowx.framework.zkoss.BaseFront;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de AgentGovernance
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class AgentGovernanceDetailViewModel extends BaseFront<AgentGovernanceDetailViewModel> {
    
    @WireVariable
    private BusinessService businessService;
    
    @WireVariable
    private AgentGovernanceService agentGovernanceService;
    
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
    private Long idxagentgovernance;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private AgentGovernance currentAgentGovernance;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    private String originalAgtpolicyname = null;
    
    // ========== Listas para combos (FK) ==========
    private List<String> availableAgtpolicytypes = new ArrayList<>();
    private List<String> availableAgtenforcementlevels = new ArrayList<>();
    private List<String> availableAgtstatuss = new ArrayList<>();
    private List<String> availableAgtprioritys = new ArrayList<>();
    
    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    
    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        // Obtener parámetros de navegación - con protección para action null

        
        if (super.action != null) {

        
            mode = super.action.name();

        
        } else {

        
            // Si action es null, intentar determinar el modo por el contexto

        
            mode = (dataParam != null) ? "LOAD" : "NEW";

        
            log.warn("Action es null, infiriendo modo: {}", mode);

        
        }
        
        // dataParam siempre contiene el ID (PK de tipo Long)
        if (dataParam != null) {
            idxagentgovernance = Long.valueOf(String.valueOf(dataParam));
        }
        
        log.info("Inicializando AgentGovernanceDetailViewModel - mode: {}, idxagentgovernance: {}", mode, idxagentgovernance);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxagentgovernance != null) {
            loadItem(idxagentgovernance);
        } else {
            log.error("Modo inválido o falta idxagentgovernance");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentAgentGovernance, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentAgentGovernance = new AgentGovernance();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadAgtpolicytypes();
        loadAgtenforcementlevels();
        loadAgtstatuss();
        loadAgtprioritys();
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            // findById siempre recibe Long id (el PK)
            currentAgentGovernance = agentGovernanceService.findById(id);
            
            if (currentAgentGovernance == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentAgentGovernance.getAgtpolicyname();
        loadAgtpolicytypes();
        loadAgtenforcementlevels();
        loadAgtstatuss();
        loadAgtprioritys();
            
            // Cargar tags/roles existentes desde JSON
            
            // Guardar valores originales para validación de unicidad
            originalAgtpolicyname = currentAgentGovernance.getAgtpolicyname();
            
            // Auditar carga de registro
            logActivity("CONSULTA", "AGTAGENTGOVERNANCE", id, "Consulta: " + currentAgentGovernance.getAgtpolicyname());
            
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
            
            boolean isNew = currentAgentGovernance.getIdxagentgovernance() == null;
            
            if (isNew) {
                currentAgentGovernance = agentGovernanceService.create(currentAgentGovernance);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "AGTAGENTGOVERNANCE", currentAgentGovernance.getIdxagentgovernance(), 
                    "Creado: " + currentAgentGovernance.getAgtpolicyname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentAgentGovernance = agentGovernanceService.update(currentAgentGovernance);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "AGTAGENTGOVERNANCE", currentAgentGovernance.getIdxagentgovernance(), 
                    "Actualizado: " + currentAgentGovernance.getAgtpolicyname());
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
        
        if (currentAgentGovernance.getAgtpolicytype() == null || currentAgentGovernance.getAgtpolicytype().trim().isEmpty()) {
            errors.append("- Policytype\n");
        }
        if (currentAgentGovernance.getAgtpolicyname() == null || currentAgentGovernance.getAgtpolicyname().trim().isEmpty()) {
            errors.append("- Policyname\n");
        }
        if (currentAgentGovernance.getAgtpolicyname() != null && currentAgentGovernance.getAgtpolicyname().length() > 255) {
            errors.append("- Policyname no puede exceder 255 caracteres\n");
        }
        if (currentAgentGovernance.getAgtpolicycontent() == null || currentAgentGovernance.getAgtpolicycontent().trim().isEmpty()) {
            errors.append("- Policycontent\n");
        }
        if (currentAgentGovernance.getAgtenforcementlevel() == null || currentAgentGovernance.getAgtenforcementlevel().trim().isEmpty()) {
            errors.append("- Enforcementlevel\n");
        }
        if (currentAgentGovernance.getAgtstatus() == null || currentAgentGovernance.getAgtstatus().trim().isEmpty()) {
            errors.append("- Status\n");
        }
        if (currentAgentGovernance.getAgteffectivefrom() == null) {
            errors.append("- Effectivefrom\n");
        }
        if (currentAgentGovernance.getAgtcreatedby() == null || currentAgentGovernance.getAgtcreatedby().trim().isEmpty()) {
            errors.append("- Created By\n");
        }
        if (currentAgentGovernance.getAgtcreatedby() != null && currentAgentGovernance.getAgtcreatedby().length() > 255) {
            errors.append("- Created By no puede exceder 255 caracteres\n");
        }
        if (currentAgentGovernance.getAgtcreatedat() == null) {
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
        params.put("dataParam", idxagentgovernance);
        params.put("action", Action.LOAD);
        appendPage("plataforma/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    private void loadAgtpolicytypes() {
        // TODO: Cargar valores desde configuración o BD
        availableAgtpolicytypes.add("OPTION_1");
        availableAgtpolicytypes.add("OPTION_2");
        availableAgtpolicytypes.add("OPTION_3");
    }
    
    private void loadAgtenforcementlevels() {
        // TODO: Cargar valores desde configuración o BD
        availableAgtenforcementlevels.add("OPTION_1");
        availableAgtenforcementlevels.add("OPTION_2");
        availableAgtenforcementlevels.add("OPTION_3");
    }
    
    private void loadAgtstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableAgtstatuss.add("OPTION_1");
        availableAgtstatuss.add("OPTION_2");
        availableAgtstatuss.add("OPTION_3");
    }
    
    private void loadAgtprioritys() {
        // TODO: Cargar valores desde configuración o BD
        availableAgtprioritys.add("OPTION_1");
        availableAgtprioritys.add("OPTION_2");
        availableAgtprioritys.add("OPTION_3");
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
    
    /**
     * Libera recursos y limpia referencias para ayudar al GC
     * Se llama automáticamente cuando el ViewModel se destruye
     */
    @Destroy
    public void destroy() {
        log.debug("[Destroy] Liberando recursos del ViewModel {}", this.getClass().getSimpleName());
        
        try {
            // Limpiar entidad actual
            currentAgentGovernance = null;
            
            // Limpiar listas de FK
            
            // Limpiar listas de LIST_STRING
            if (availableAgtpolicytypes != null) {
                availableAgtpolicytypes.clear();
                availableAgtpolicytypes = null;
            }
            if (availableAgtenforcementlevels != null) {
                availableAgtenforcementlevels.clear();
                availableAgtenforcementlevels = null;
            }
            if (availableAgtstatuss != null) {
                availableAgtstatuss.clear();
                availableAgtstatuss = null;
            }
            if (availableAgtprioritys != null) {
                availableAgtprioritys.clear();
                availableAgtprioritys = null;
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
