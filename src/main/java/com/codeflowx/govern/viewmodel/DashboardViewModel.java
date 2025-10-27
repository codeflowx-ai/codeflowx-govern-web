package com.codeflowx.govern.viewmodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.MasterPage;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.codeflowx.govern.workflow.services.TaskManagementService;
import com.codeflowx.govern.workflow.services.TaskManagementService.TaskDTO;

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel: Dashboard Principal
 * 
 * Muestra:
 * - Resumen de tareas pendientes
 * - Tareas urgentes (CRITICAL)
 * - Métricas de workflow
 * - Acceso rápido a funcionalidades
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class DashboardViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;

    // ========== Servicios y contexto ==========
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
    
    // ========== Servicios Flowable (Opcional MOCK) ==========
    @WireVariable
    private TaskManagementService taskManagementService;

    @WireVariable
    private TaskService taskService;

    @WireVariable
    private RuntimeService runtimeService;
    
    // ========== Modo MOCK ==========
    @Getter
    private boolean mockMode = false;

    // ========== Datos Dashboard ==========
    @Getter
    private int totalTasks = 0;
    
    @Getter
    private int assignedToMeCount = 0;
    
    @Getter
    private int criticalTasksCount = 0;
    
    @Getter
    private int groupTasksCount = 0;
    
    @Getter
    private List<TaskDTO> urgentTasks = new ArrayList<>();
    
    @Getter
    private String currentUsername;
    
    // Métricas generales
    @Getter
    private int totalComplianceChecks = 0;
    
    @Getter
    private int activeModels = 0;
    
    @Getter
    private int activeAgents = 0;
    
    @Getter
    private int pendingApprovals = 0;
    
    // Títulos y descripciones dinámicos
    @Getter
    private String subtitle = "";
    
    @Getter
    private String stat1Title = "";
    
    @Getter
    private String stat1Description = "";
    
    @Getter
    private String stat2Title = "";
    
    @Getter
    private String stat2Description = "";
    
    @Getter
    private String stat3Title = "";
    
    @Getter
    private String stat3Description = "";
    
    @Getter
    private String stat4Title = "";
    
    @Getter
    private String stat4Description = "";

    // ========== Inicialización ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        // Detectar modo MOCK
        String mockParam = Executions.getCurrent().getParameter("mock");
        mockMode = "true".equalsIgnoreCase(mockParam);
        
        // Obtener usuario logado
        currentUsername = (String) Sessions.getCurrent().getAttribute("username");
        if (currentUsername == null) {
            currentUsername = mockMode ? "demo.user" : "admin";
        }

        log.info("📊 Inicializando Dashboard - Usuario: {} - MOCK: {}", currentUsername, mockMode);
        
        // Cargar datos
        loadDashboardData();
    }

    /**
     * Cargar datos del dashboard
     */
    @Command
    @NotifyChange({"totalTasks", "assignedToMeCount", "criticalTasksCount", "groupTasksCount", 
                   "urgentTasks", "totalComplianceChecks", "activeModels", "activeAgents", "pendingApprovals",
                   "subtitle", "stat1Title", "stat1Description", "stat2Title", "stat2Description",
                   "stat3Title", "stat3Description", "stat4Title", "stat4Description"})
    public void loadDashboardData() {
        try {
            // Auto-detectar MOCK mode si Flowable no está disponible
            if (mockMode || taskManagementService == null) {
                if (taskManagementService == null) {
                    log.info("🎭 Flowable no disponible - activando MOCK mode");
                    mockMode = true;
                }
                loadMockDashboardData();
                return;
            }
            
            // Cargar tareas reales
            List<TaskDTO> allTasks = taskManagementService.getPendingTasksForUser(currentUsername);
            
            totalTasks = allTasks.size();
            assignedToMeCount = (int) allTasks.stream()
                .filter(t -> currentUsername.equals(t.getAssignee()))
                .count();
            
            criticalTasksCount = (int) allTasks.stream()
                .filter(t -> t.getPriority() != null && t.getPriority() >= 90)
                .count();
            
            groupTasksCount = totalTasks - assignedToMeCount;
            
            // Tareas urgentes (top 5 CRITICAL)
            urgentTasks = allTasks.stream()
                .filter(t -> t.getPriority() != null && t.getPriority() >= 90)
                .limit(5)
                .collect(Collectors.toList());
            
            // Métricas adicionales
            totalComplianceChecks = 42;
            activeModels = 8;
            activeAgents = 3;
            pendingApprovals = 12;
            
            // Textos
            subtitle = "Resumen de tu sistema de gobierno IA";
            stat1Title = "Total Tareas Pendientes";
            stat1Description = totalTasks + " tareas en el sistema";
            stat2Title = "Asignadas a Mí";
            stat2Description = assignedToMeCount + " tareas personales";
            stat3Title = "Tareas Críticas";
            stat3Description = criticalTasksCount + " requieren atención urgente";
            stat4Title = "De Mis Roles";
            stat4Description = groupTasksCount + " disponibles para mis roles";
            
            log.info("✅ Dashboard cargado: {} tareas, {} urgentes", totalTasks, criticalTasksCount);
            
        } catch (Exception e) {
            log.error("❌ Error cargando dashboard: {}", e.getMessage(), e);
            loadMockDashboardData();
        }
    }
    
    /**
     * Datos MOCK para dashboard
     */
    private void loadMockDashboardData() {
        log.info("🎭 Cargando datos MOCK para dashboard");
        
        // Tareas MOCK (simuladas)
        totalTasks = 24;
        assignedToMeCount = 10;
        criticalTasksCount = 6;
        groupTasksCount = 14;
        
        // Crear tareas urgentes demo
        urgentTasks = new ArrayList<>();
        
        String[] userNames = {"maria.garcia", "carlos.rodriguez", "ana.martinez"};
        
        long now = System.currentTimeMillis();
        Date dueDateSoon = new Date(now + 2 * 3600000); // +2 horas
        
        // Tarea 1: Critical
        TaskDTO task1 = createMockTask("critical-1", "bias-detection-v1", 
            "Decisión Urgente Sesgo - Sistema Activo",
            "Sesgo crítico en producción requiere decisión inmediata",
            userNames[0], 100, Arrays.asList("AdminAI", "Compliance"), dueDateSoon);
        
        // Tarea 2: Critical
        TaskDTO task2 = createMockTask("critical-2", "drift-detection-v1",
            "Analizar Drift - Modelo Recomendaciones",
            "Investigadr causa raíz de drift detectado (-12% accuracy)",
            userNames[1], 95, Arrays.asList("DataEngineer", "MLOps"), dueDateSoon);
        
        // Tarea 3: Critical
        TaskDTO task3 = createMockTask("critical-3", "critical-alert-response",
            "Responder Alerta Crítica - Sistema Producción",
            "Alerta crítica detectada en sistema de inferencia",
            userNames[2], 90, Arrays.asList("MLOps"), dueDateSoon);
        
        urgentTasks.add(task1);
        urgentTasks.add(task2);
        urgentTasks.add(task3);
        
        // Métricas demo
        totalComplianceChecks = 42;
        activeModels = 8;
        activeAgents = 3;
        pendingApprovals = 12;
        
        // Textos
        subtitle = "Resumen de tu sistema de gobierno IA";
        stat1Title = "Total Tareas Pendientes";
        stat1Description = totalTasks + " tareas en el sistema";
        stat2Title = "Asignadas a Mí";
        stat2Description = assignedToMeCount + " tareas personales";
        stat3Title = "Tareas Críticas";
        stat3Description = criticalTasksCount + " requieren atención urgente";
        stat4Title = "De Mis Roles";
        stat4Description = groupTasksCount + " disponibles para mis roles";
        
        log.info("✅ Dashboard MOCK cargado");
    }
    
    /**
     * Crear tarea MOCK
     */
    private TaskDTO createMockTask(String id, String processDefId, String name, String description,
                                   String assignee, Integer priority, List<String> candidateGroups, Date dueDate) {
        TaskDTO task = new TaskDTO();
        task.setId(id);
        task.setProcessDefinitionId(processDefId);
        task.setName(name);
        task.setDescription(description);
        task.setAssignee(assignee);
        task.setPriority(priority);
        task.setCreateTime(new Date());
        task.setDueDate(dueDate);
        task.setCandidateGroups(candidateGroups);
        
        Map<String, Object> vars = new HashMap<>();
        vars.put("mockMode", true);
        vars.put("priority", priority);
        task.setProcessVariables(vars);
        
        return task;
    }
    
    /**
     * Convertir prioridad a texto
     */
    public String getPriorityText(Integer priority) {
        if (priority == null) return "MEDIUM";
        if (priority >= 90) return "HIGH";
        if (priority >= 50) return "MEDIUM";
        return "LOW";
    }
    
    /**
     * Obtener estilo de prioridad
     */
    public String getPriorityStyle(Integer priority) {
        if (priority == null) priority = 50;
        if (priority >= 90) return "font-weight: bold; color: #dc3545;";
        if (priority >= 50) return "font-weight: 500; color: #ffc107;";
        return "color: #6c757d;";
    }
    
    /**
     * Obtener badge de prioridad
     */
    public String getPriorityBadgeClass(Integer priority) {
        if (priority == null) priority = 50;
        if (priority >= 90) return "badge bg-danger";
        if (priority >= 50) return "badge bg-warning";
        return "badge bg-secondary";
    }
    
    /**
     * Abrir bandeja de tareas
     */
    @Command
    public void openTaskInbox() {
        Executions.sendRedirect("/console/bpmn/task-inbox.zul?mock=" + mockMode);
    }
    
    /**
     * Abrir tarea específica
     */
    @Command
    public void openTask(@org.zkoss.bind.annotation.BindingParam("task") TaskDTO task) {
        if (task.getFormKey() != null && !task.getFormKey().isEmpty()) {
            String zulPath = "/console/" + task.getFormKey() + "?taskId=" + task.getId() + "&mock=true";
            Executions.createComponents(zulPath, null, null);
        }
    }

    // ===============================================
    // HELPERS
    // ===============================================
    
    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }
    
    public String formatCandidateGroups(List<String> groups) {
        if (groups == null || groups.isEmpty()) {
            return "N/A";
        }
        return String.join(", ", groups);
    }
    
    public boolean hasUrgentTasks() {
        return criticalTasksCount > 0;
    }
    
    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }
    
    @org.zkoss.bind.annotation.Destroy
    public void destroy() {
        if (urgentTasks != null) { urgentTasks.clear(); urgentTasks = null; }
        businessService = null;
        taskService = null;
    }
}

