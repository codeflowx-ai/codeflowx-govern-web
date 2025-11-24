package com.codeflowx.govern.viewmodel.agents;

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
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.govern.entity.agents.Agent;
import com.codeflowx.govern.service.governance.PolicyService;
// AgentTask no existe, usar AgentWorkflow
import com.codeflowx.govern.entity.agents.AgentWorkflow;
import com.codeflowx.govern.entity.governance.PolicyAuditLog;
import com.codeflowx.govern.service.agents.AgentWorkflowService;
import com.codeflowx.govern.service.agents.AgentService;
import com.codeflowx.govern.service.governance.PolicyAuditLogService;

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
 * ViewModel para el log de decisiones de agentes con traceability completo
 * Muestra histórico de tareas, workflows ejecutados y decisiones tomadas por agentes AI
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class AgentDecisionsLogViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    
    // ========== Servicios y contexto Spring ==========
    @WireVariable
    private PolicyService policyService;
    @WireVariable
    private PolicyAuditLogService policyAuditLogService;
    @WireVariable
    private AgentService agentService;
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
    
    
    protected void initDao() {
        // Ya no es necesario inicializar BusinessService manualmente
        // El Service se inyecta automáticamente mediante @WireVariable
    }
    }
    
    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }

    // ========== Paginación ==========
    private PageParams pageParams;
    
    // ========== Filtros ==========
    private Long filterAgentId;
    private String filterStatus;
    private Timestamp filterStartDate;
    private Timestamp filterEndDate;
    
    // ========== Datos ==========
    private List<Agent> availableAgents = new ArrayList<>();
    private List<AgentWorkflow> tasks = new ArrayList<>();
    private List<AgentWorkflow> workflows = new ArrayList<>();
    private List<PolicyAuditLog> auditLogs = new ArrayList<>();
    
    // ========== KPIs ==========
    private Long totalTasks = 0L;
    private Long completedTasks = 0L;
    private Long failedTasks = 0L;
    private Long totalWorkflows = 0L;
    private Long activeWorkflows = 0L;
    private BigDecimal successRate = BigDecimal.ZERO;
    
    // ========== Detalle de tarea seleccionada ==========
    private AgentWorkflow selectedTask;
    private String taskDecisionDetails = "";

    // ========== Inicialización ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        initializePageParams();
        
        log.info("Inicializando AgentDecisionsLogViewModel");
        
        loadAvailableAgents();
        loadTasks();
        loadWorkflows();
        loadAuditLogs();
        calculateKPIs();
    }
    
    private void initializePageParams() {
        pageParams = PageParams.builder()
                .maxRows(50)
                .pageActual(1)
                .rowActual(0)
                .ascending(false)
                .sortField("createdat")
                .build();
    }

    // ========== Carga de Datos ==========
    
    private void loadAvailableAgents() {
        try {
            log.debug("Cargando agentes disponibles");
            
            // TABLE - usar findAllEntity()
            PageParams pageParams = PageParams.builder()
                .maxRows(100)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            PageResult<Agent> result = agentService.findAll(pageParams, new Criterias()
            );
            
            if (result != null && result.getContent() != null) {
                availableAgents = result.getContent();
                log.info("Cargados {} agentes", availableAgents.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar agentes", e);
        }
    }
    
    private void loadTasks() {
        try {
            log.debug("Cargando tareas de agentes");
            
            // TABLE - usar findAllEntity() con filtros
            PageParams pageParams = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            Criterias criterias = new Criterias();
            if (filterAgentId != null) {
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "fkidxagent");
                criteria.setValueEnd(filterAgentId);
                criterias.addCriteria(criteria);
            }
            if (filterStatus != null && !filterStatus.isEmpty()) {
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "agtstatus");
                criteria.setValueEnd(filterStatus);
                criterias.addCriteria(criteria);
            }
            
            PageResult<AgentWorkflow> result = agentWorkflowService.findAll(pageParams, criterias
            );
            
            if (result != null && result.getContent() != null) {
                tasks = result.getContent();
                log.info("Cargadas {} tareas", tasks.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar tareas", e);
        }
    }
    
    private void loadWorkflows() {
        try {
            log.debug("Cargando workflows de agentes");
            
            // TABLE - usar findAllEntity() con filtros
            PageParams pageParams = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            Criterias criterias = new Criterias();
            if (filterAgentId != null) {
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "fkidxagent");
                criteria.setValueEnd(filterAgentId);
                criterias.addCriteria(criteria);
            }
            
            PageResult<AgentWorkflow> result = agentWorkflowService.findAll(pageParams, criterias
            );
            
            if (result != null && result.getContent() != null) {
                workflows = result.getContent();
                log.info("Cargados {} workflows", workflows.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar workflows", e);
        }
    }
    
    private void loadAuditLogs() {
        try {
            log.debug("Cargando audit logs relacionados");
            
            // TABLE - usar findAllEntity()
            PageParams pageParams = PageParams.builder()
                .maxRows(20)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            PageResult<PolicyAuditLog> result = policyAuditLogService.findAll(pageParams, new Criterias()
            );
            
            if (result != null && result.getContent() != null) {
                auditLogs = result.getContent();
                log.info("Cargados {} audit logs", auditLogs.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar audit logs", e);
        }
    }
    
    private void calculateKPIs() {
        try {
            log.debug("Calculando KPIs de agent decisions");
            
            totalTasks = (long) tasks.size();
            
            completedTasks = tasks.stream()
                .filter(t -> "COMPLETED".equals(t.getAgtstatus()))
                .count();
            
            failedTasks = tasks.stream()
                .filter(t -> "FAILED".equals(t.getAgtstatus()))
                .count();
            
            totalWorkflows = (long) workflows.size();
            
            activeWorkflows = workflows.stream()
                .filter(w -> "RUNNING".equals(w.getAgtstatus()))
                .count();
            
            if (totalTasks > 0) {
                successRate = BigDecimal.valueOf(completedTasks)
                    .divide(BigDecimal.valueOf(totalTasks), 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            }
            
            log.info("KPIs calculados - Tasks: {}, Completed: {}, Success rate: {}%", 
                totalTasks, completedTasks, successRate);
        } catch (Exception e) {
            log.error("Error al calcular KPIs", e);
        }
    }

    // ========== Comandos ==========
    
    @Command
    @NotifyChange("*")
    public void refreshData() {
        log.info("Refrescando log de decisiones");
        try {
            loadTasks();
            loadWorkflows();
            loadAuditLogs();
            calculateKPIs();
            
            Messagebox.show("Datos actualizados correctamente", "Éxito", 
                Messagebox.OK, Messagebox.INFORMATION);
        } catch (Exception e) {
            log.error("Error al refrescar datos", e);
            Messagebox.show("Error al refrescar: " + e.getMessage(), "Error", 
                Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange({"tasks", "totalTasks"})
    public void applyFilters() {
        log.info("Aplicando filtros - Agent: {}, Status: {}", filterAgentId, filterStatus);
        loadTasks();
        calculateKPIs();
    }
    
    @Command
    @NotifyChange({"filterAgentId", "filterStatus", "tasks"})
    public void clearFilters() {
        log.info("Limpiando filtros");
        filterAgentId = null;
        filterStatus = null;
        filterStartDate = null;
        filterEndDate = null;
        loadTasks();
        calculateKPIs();
    }
    
    @Command
    @NotifyChange({"selectedTask", "taskDecisionDetails"})
    public void viewTaskDetails(@org.zkoss.bind.annotation.BindingParam("task") AgentWorkflow task) {
        log.info("Visualizando detalles de tarea: {}", task.getIdxagentworkflow());
        selectedTask = task;
        taskDecisionDetails = task.getAgtdescription() != null ? task.getAgtdescription() : "Sin detalles disponibles";
    }

    // ========== Auditoría ==========
    
    /**
     * Registra la actividad del usuario en el sistema de auditoría
     */
    private void logActivity(String action, String model, Long pk, String mensaje) {
        try {
            Ssoractividad activityLog = new Ssoractividad();
            activityLog.setUsername(getUser().getUsername());
            activityLog.setAccion(action);
            activityLog.setAlta(new java.sql.Timestamp(System.currentTimeMillis()));
            activityLog.setModulo(model);
            activityLog.setIdtupla(pk != null ? pk.intValue() : 0);
            activityLog.setAplicacion(ctxBean.getApplicationName());
            activityLog.setValuetupla(mensaje);
            activityLog = policyService.create(activityLog);
        } catch (Exception e) {
            log.error("Error al auditar acción: {} en módulo: {}", action, model, e);
        }
    }
    
    // ========== Cleanup ==========
    
    @Destroy
    public void destroy() {
        log.debug("[Destroy] Liberando recursos del ViewModel {}", this.getClass().getSimpleName());
        try {
            if (availableAgents != null) {
                availableAgents.clear();
                availableAgents = null;
            }
            if (tasks != null) {
                tasks.clear();
                tasks = null;
            }
            if (workflows != null) {
                workflows.clear();
                workflows = null;
            }
            if (auditLogs != null) {
                auditLogs.clear();
                auditLogs = null;
            }
            
            selectedTask = null;
            pageParams = null;
            policyService = null;
            policyAuditLogService = null;
            agentService = null;
            agentWorkflowService = null;
            
            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }
}


