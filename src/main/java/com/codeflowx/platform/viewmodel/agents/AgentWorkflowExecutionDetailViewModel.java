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
import com.codeflowx.govern.entity.agents.AgentWorkflowExecution;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de AgentWorkflowExecution
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class AgentWorkflowExecutionDetailViewModel extends MasterPage {
    
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
    private Long idxworkflowexecution;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private AgentWorkflowExecution currentAgentWorkflowExecution;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    
    // ========== Listas para combos (FK) ==========
    private List<String> availableAgtstatuss = new ArrayList<>();
    
    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    private List<String> selectedAgtagentexecutions = new ArrayList<>();
    private String newAgtagentexecution = "";
    
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
            idxworkflowexecution = Long.valueOf(String.valueOf(dataParam));
        }
        
        log.info("Inicializando AgentWorkflowExecutionDetailViewModel - mode: {}, idxworkflowexecution: {}", mode, idxworkflowexecution);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxworkflowexecution != null) {
            loadItem(idxworkflowexecution);
        } else {
            log.error("Modo inválido o falta idxworkflowexecution");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("gobierno/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentAgentWorkflowExecution, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentAgentWorkflowExecution = new AgentWorkflowExecution();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadAgtstatuss();
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            // findById siempre recibe Long id (el PK)
            currentAgentWorkflowExecution = businessService.findById(AgentWorkflowExecution.class, id);
            
            if (currentAgentWorkflowExecution == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentAgentWorkflowExecution.getIdxworkflowexecution();
        loadAgtstatuss();
            
            // Cargar tags/roles existentes desde JSON
            selectedAgtagentexecutions = convertJsonToList(currentAgentWorkflowExecution.getAgtagentexecutions());
            
            // Guardar valores originales para validación de unicidad
            
            // Auditar carga de registro
            logActivity("CONSULTA", "AGTWORKFLOWEXECUTIONS", id, "Consulta: " + currentAgentWorkflowExecution.getIdxworkflowexecution());
            
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
            
            boolean isNew = currentAgentWorkflowExecution.getIdxworkflowexecution() == null;
            
            if (isNew) {
                businessService.save(currentAgentWorkflowExecution);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "AGTWORKFLOWEXECUTIONS", currentAgentWorkflowExecution.getIdxworkflowexecution(), 
                    "Creado: " + currentAgentWorkflowExecution.getIdxworkflowexecution());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                businessService.update(currentAgentWorkflowExecution);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "AGTWORKFLOWEXECUTIONS", currentAgentWorkflowExecution.getIdxworkflowexecution(), 
                    "Actualizado: " + currentAgentWorkflowExecution.getIdxworkflowexecution());
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
        
        if (currentAgentWorkflowExecution.getAgtexecutionid() == null || currentAgentWorkflowExecution.getAgtexecutionid().trim().isEmpty()) {
            errors.append("- Execution Id\n");
        }
        if (currentAgentWorkflowExecution.getAgtexecutionid() != null && currentAgentWorkflowExecution.getAgtexecutionid().length() > 100) {
            errors.append("- Execution Id no puede exceder 100 caracteres\n");
        }
        if (currentAgentWorkflowExecution.getAgtstatus() == null || currentAgentWorkflowExecution.getAgtstatus().trim().isEmpty()) {
            errors.append("- Status\n");
        }
        if (currentAgentWorkflowExecution.getAgtstartedat() == null) {
            errors.append("- Startedat\n");
        }
        if (currentAgentWorkflowExecution.getAgtcreatedby() == null || currentAgentWorkflowExecution.getAgtcreatedby().trim().isEmpty()) {
            errors.append("- Created By\n");
        }
        if (currentAgentWorkflowExecution.getAgtcreatedby() != null && currentAgentWorkflowExecution.getAgtcreatedby().length() > 255) {
            errors.append("- Created By no puede exceder 255 caracteres\n");
        }
        if (currentAgentWorkflowExecution.getAgtcreatedat() == null) {
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
        params.put("dataParam", idxworkflowexecution);
        params.put("action", Action.LOAD);
        appendPage("plataforma/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    private void loadAgtstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableAgtstatuss.add("OPTION_1");
        availableAgtstatuss.add("OPTION_2");
        availableAgtstatuss.add("OPTION_3");
    }
    
    @Command
    @NotifyChange("{'selectedAgtagentexecutions', 'currentAgentWorkflowExecution'}")
    public void addAgtagentexecution() {
        if (newAgtagentexecution != null && !newAgtagentexecution.trim().isEmpty() && !selectedAgtagentexecutions.contains(newAgtagentexecution.trim())) {
            selectedAgtagentexecutions.add(newAgtagentexecution.trim());
            newAgtagentexecution = "";
            // Convertir lista a JSON y actualizar en currentAgentWorkflowExecution
            currentAgentWorkflowExecution.setAgtagentexecutions(convertListToJson(selectedAgtagentexecutions));
        }
    }
    
    @Command
    @NotifyChange("{'selectedAgtagentexecutions', 'currentAgentWorkflowExecution'}")
    public void removeAgtagentexecution(@BindingParam("tag") String tag) {
        selectedAgtagentexecutions.remove(tag);
        // Convertir lista a JSON y actualizar en currentAgentWorkflowExecution
        currentAgentWorkflowExecution.setAgtagentexecutions(convertListToJson(selectedAgtagentexecutions));
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
            currentAgentWorkflowExecution = null;
            
            // Limpiar listas de FK
            
            // Limpiar listas de LIST_STRING
            if (availableAgtstatuss != null) {
                availableAgtstatuss.clear();
                availableAgtstatuss = null;
            }
            
            // Limpiar colecciones @OneToMany
            
            // Limpiar tags/roles JSONB
            if (selectedAgtagentexecutions != null) {
                selectedAgtagentexecutions.clear();
                selectedAgtagentexecutions = null;
            }
            newAgtagentexecution = null;
            
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
