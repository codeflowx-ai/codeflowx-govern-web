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
import com.codeflowx.govern.entity.agents.AgentWorkflow;
import com.codeflowx.govern.entity.agents.AgentWorkflowExecution;
import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.framework.validators.UniqueValidator;
import codeflowx.nocode.persist.BusinessService;
import com.codeflowx.govern.service.agents.AgentWorkflowService;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de AgentWorkflow
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class AgentWorkflowDetailViewModel extends BaseFront<AgentWorkflowDetailViewModel> {
    
    @WireVariable
    private BusinessService businessService;
    
    @WireVariable
    private AgentWorkflowService agentWorkflowService;
    
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
    private Long idxagentworkflow;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private AgentWorkflow currentAgentWorkflow;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    private String originalAgtname = null;
    
    // ========== Listas para combos (FK) ==========
    private List<String> availableAgtworkflowtypes = new ArrayList<>();
    private List<String> availableAgtstatuss = new ArrayList<>();
    
    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    private List<String> selectedAgtagentsequence = new ArrayList<>();
    private String newAgtagentsequenc = "";
    
    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    private List<AgentWorkflowExecution> subagtworkflowexecutions = new ArrayList<>();
    private boolean subagtworkflowexecutionsLoaded = false;
    
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
            idxagentworkflow = Long.valueOf(String.valueOf(dataParam));
        }
        
        log.info("Inicializando AgentWorkflowDetailViewModel - mode: {}, idxagentworkflow: {}", mode, idxagentworkflow);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxagentworkflow != null) {
            loadItem(idxagentworkflow);
        } else {
            log.error("Modo inválido o falta idxagentworkflow");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentAgentWorkflow, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentAgentWorkflow = new AgentWorkflow();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadAgtworkflowtypes();
        loadAgtstatuss();
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            // findById siempre recibe Long id (el PK)
            currentAgentWorkflow = agentWorkflowService.findById(id);
            
            if (currentAgentWorkflow == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentAgentWorkflow.getAgtname();
        loadAgtworkflowtypes();
        loadAgtstatuss();
            
            // Cargar tags/roles existentes desde JSON
            selectedAgtagentsequence = convertJsonToList(currentAgentWorkflow.getAgtagentsequence());
            
            // Guardar valores originales para validación de unicidad
            originalAgtname = currentAgentWorkflow.getAgtname();
            
            // Auditar carga de registro
            logActivity("CONSULTA", "AGTAGENTWORKFLOWS", id, "Consulta: " + currentAgentWorkflow.getAgtname());
            
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
            
            boolean isNew = currentAgentWorkflow.getIdxagentworkflow() == null;
            
            if (isNew) {
                currentAgentWorkflow = agentWorkflowService.create(currentAgentWorkflow);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "AGTAGENTWORKFLOWS", currentAgentWorkflow.getIdxagentworkflow(), 
                    "Creado: " + currentAgentWorkflow.getAgtname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentAgentWorkflow = agentWorkflowService.update(currentAgentWorkflow);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "AGTAGENTWORKFLOWS", currentAgentWorkflow.getIdxagentworkflow(), 
                    "Actualizado: " + currentAgentWorkflow.getAgtname());
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
        
        if (currentAgentWorkflow.getAgtname() == null || currentAgentWorkflow.getAgtname().trim().isEmpty()) {
            errors.append("- Name\n");
        }
        if (currentAgentWorkflow.getAgtname() != null && currentAgentWorkflow.getAgtname().length() > 255) {
            errors.append("- Name no puede exceder 255 caracteres\n");
        }
        if (currentAgentWorkflow.getAgtworkflowtype() == null || currentAgentWorkflow.getAgtworkflowtype().trim().isEmpty()) {
            errors.append("- Workflow Type\n");
        }
        if (currentAgentWorkflow.getAgtstatus() == null || currentAgentWorkflow.getAgtstatus().trim().isEmpty()) {
            errors.append("- Status\n");
        }
        if (currentAgentWorkflow.getAgtdefinition() == null || currentAgentWorkflow.getAgtdefinition().trim().isEmpty()) {
            errors.append("- Definition\n");
        }
        if (currentAgentWorkflow.getAgtcreatedby() == null || currentAgentWorkflow.getAgtcreatedby().trim().isEmpty()) {
            errors.append("- Created By\n");
        }
        if (currentAgentWorkflow.getAgtcreatedby() != null && currentAgentWorkflow.getAgtcreatedby().length() > 255) {
            errors.append("- Created By no puede exceder 255 caracteres\n");
        }
        if (currentAgentWorkflow.getAgtcreatedat() == null) {
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
        params.put("dataParam", idxagentworkflow);
        params.put("action", Action.LOAD);
        appendPage("plataforma/agents/agents-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    private void loadAgtworkflowtypes() {
        // TODO: Cargar valores desde configuración o BD
        availableAgtworkflowtypes.add("OPTION_1");
        availableAgtworkflowtypes.add("OPTION_2");
        availableAgtworkflowtypes.add("OPTION_3");
    }
    
    private void loadAgtstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableAgtstatuss.add("OPTION_1");
        availableAgtstatuss.add("OPTION_2");
        availableAgtstatuss.add("OPTION_3");
    }
    
    @Command
    @NotifyChange("{'selectedAgtagentsequence', 'currentAgentWorkflow'}")
    public void addAgtagentsequenc() {
        if (newAgtagentsequenc != null && !newAgtagentsequenc.trim().isEmpty() && !selectedAgtagentsequence.contains(newAgtagentsequenc.trim())) {
            selectedAgtagentsequence.add(newAgtagentsequenc.trim());
            newAgtagentsequenc = "";
            // Convertir lista a JSON y actualizar en currentAgentWorkflow
            currentAgentWorkflow.setAgtagentsequence(convertListToJson(selectedAgtagentsequence));
        }
    }
    
    @Command
    @NotifyChange("{'selectedAgtagentsequence', 'currentAgentWorkflow'}")
    public void removeAgtagentsequenc(@BindingParam("tag") String tag) {
        selectedAgtagentsequence.remove(tag);
        // Convertir lista a JSON y actualizar en currentAgentWorkflow
        currentAgentWorkflow.setAgtagentsequence(convertListToJson(selectedAgtagentsequence));
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
    
    private void loadSubagtworkflowexecutions() {
        try {
            if (currentAgentWorkflow != null && currentAgentWorkflow.getIdxagentworkflow() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "workflow");
                criteria.setValues(new Object[]{currentAgentWorkflow.getIdxagentworkflow()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<AgentWorkflowExecution> result = businessService.findAllEntity(AgentWorkflowExecution.class, collectionParams, criterias);
                subagtworkflowexecutions = result != null ? result.getContent() : new ArrayList<>();
                subagtworkflowexecutionsLoaded = true;
                log.debug("Cargados {} subagtworkflowexecutions", subagtworkflowexecutions.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subagtworkflowexecutions", e);
            subagtworkflowexecutions = new ArrayList<>();
        }
    }
    
    @Command
    @NotifyChange("subagtworkflowexecutions")
    public void onSelectSubagtworkflowexecutionsTab() {
        if (!subagtworkflowexecutionsLoaded) {
            loadSubagtworkflowexecutions();
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
    
    /**
     * Libera recursos y limpia referencias para ayudar al GC
     * Se llama automáticamente cuando el ViewModel se destruye
     */
    @Destroy
    public void destroy() {
        log.debug("[Destroy] Liberando recursos del ViewModel {}", this.getClass().getSimpleName());
        
        try {
            // Limpiar entidad actual
            currentAgentWorkflow = null;
            
            // Limpiar listas de FK
            
            // Limpiar listas de LIST_STRING
            if (availableAgtworkflowtypes != null) {
                availableAgtworkflowtypes.clear();
                availableAgtworkflowtypes = null;
            }
            if (availableAgtstatuss != null) {
                availableAgtstatuss.clear();
                availableAgtstatuss = null;
            }
            
            // Limpiar colecciones @OneToMany
            if (subagtworkflowexecutions != null) {
                subagtworkflowexecutions.clear();
                subagtworkflowexecutions = null;
            }
            subagtworkflowexecutionsLoaded = false;
            
            // Limpiar tags/roles JSONB
            if (selectedAgtagentsequence != null) {
                selectedAgtagentsequence.clear();
                selectedAgtagentsequence = null;
            }
            newAgtagentsequenc = null;
            
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
