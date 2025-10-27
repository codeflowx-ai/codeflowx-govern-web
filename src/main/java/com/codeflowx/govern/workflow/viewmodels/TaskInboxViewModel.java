package com.codeflowx.govern.workflow.viewmodels;

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
import org.zkoss.bind.annotation.BindingParam;
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
import org.zkoss.zul.Messagebox;

import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.govern.workflow.services.TaskManagementService;
import com.codeflowx.govern.workflow.services.TaskManagementService.TaskDTO;
import com.codeflowx.govern.workflow.services.TaskManagementService.UserDTO;

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel: Bandeja de Tareas BPMN
 * 
 * Funcionalidades:
 * - Ver tareas pendientes del usuario logado
 * - Filtrar por proceso, prioridad, estado
 * - Asignar tarea a usuario específico
 * - Claim/Release tareas
 * - Abrir formulario ZUL de la tarea
 * 
 * Integración:
 * - Ssousuario (usuario logado)
 * - Ssorol (roles del usuario)
 * - Flowable TaskService
 * - TaskManagementService
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class TaskInboxViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;

    // ========== Servicios y contexto Spring ==========
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
        // TODO Auto-generated method stub
    }

    // ========== Servicios Flowable ==========
    @WireVariable
    private TaskManagementService taskManagementService;

    @WireVariable
    private TaskService taskService;

    @WireVariable
    private RuntimeService runtimeService;

    // ========== Modo MOCK ==========
    @Getter
    private boolean mockMode = false;

    // ========== Datos ==========
    @Getter
    private List<TaskDTO> allTasks = new ArrayList<>();

    @Getter
    private List<TaskDTO> filteredTasks = new ArrayList<>();

    @Getter @Setter
    private TaskDTO selectedTask;

    // Filtros
    @Getter
    private List<String> processDefinitions = new ArrayList<>();

    @Getter
    private List<String> priorities = Arrays.asList("Todas", "Alta (90-100)", "Media (50-89)", "Baja (0-49)");

    @Getter
    private List<String> taskStates = Arrays.asList("Todos", "Asignadas a Mí", "De Mis Roles", "Sin Asignar");

    @Getter @Setter
    private String selectedProcess = "Todos";

    @Getter @Setter
    private String selectedPriority = "Todas";

    @Getter @Setter
    private String selectedState = "Todos";

    // Usuario logado
    @Getter
    private String currentUsername;

    // Estadísticas
    @Getter
    private int totalTasks = 0;

    @Getter
    private int assignedToMeCount = 0;

    @Getter
    private int groupTasksCount = 0;

    // Dialog de asignación
    @Getter @Setter
    private boolean showAssignDialog = false;

    @Getter @Setter
    private TaskDTO taskToAssign;

    @Getter
    private List<UserDTO> availableUsers = new ArrayList<>();

    @Getter @Setter
    private UserDTO selectedUserToAssign;

    // ========== Inicialización ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        // Detectar modo MOCK desde parámetro URL
        String mockParam = Executions.getCurrent().getParameter("mock");
        mockMode = "true".equalsIgnoreCase(mockParam);
        
        log.info("🚀 Inicializando TaskInboxViewModel - MOCK MODE: {}", mockMode);

        // Obtener usuario logado de sesión ZKoss
        currentUsername = (String) Sessions.getCurrent().getAttribute("username");
        if (currentUsername == null) {
            currentUsername = mockMode ? "demo.user" : "admin";
        }

        log.info("📋 Usuario logado: {}", currentUsername);

        // Cargar tareas
        if (mockMode) {
            loadMockTasks();
        } else {
            loadTasks();
        }
    }

    /**
     * Cargar tareas pendientes del usuario
     */
    @Command
    @NotifyChange({"allTasks", "filteredTasks", "totalTasks", "assignedToMeCount", "groupTasksCount", "processDefinitions"})
    public void loadTasks() {
        if (mockMode) {
            loadMockTasks();
            return;
        }
        
        try {
            log.info("📥 Cargando tareas para usuario: {}", currentUsername);

            // Obtener tareas del service
            allTasks = taskManagementService.getPendingTasksForUser(currentUsername);
            filteredTasks = new ArrayList<>(allTasks);

            // Calcular estadísticas
            totalTasks = allTasks.size();
            assignedToMeCount = (int) allTasks.stream()
                .filter(t -> currentUsername.equals(t.getAssignee()))
                .count();
            groupTasksCount = totalTasks - assignedToMeCount;

            // Extraer procesos únicos para filtro
            processDefinitions = new ArrayList<>();
            processDefinitions.add("Todos");
            processDefinitions.addAll(
                allTasks.stream()
                    .map(TaskDTO::getProcessDefinitionId)
                    .map(this::getProcessName)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList())
            );

            log.info("✅ Cargadas {} tareas ({} asignadas, {} de grupos)", 
                     totalTasks, assignedToMeCount, groupTasksCount);

        } catch (Exception e) {
            log.error("❌ Error cargando tareas: {}", e.getMessage(), e);
            Messagebox.show("Error cargando tareas: " + e.getMessage(), "Error", 
                            Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    /**
     * Cargar tareas MOCK para demo/presentación
     * Una tarea por cada pantalla de workflow para poder acceder y verificar diseño
     */
    private void loadMockTasks() {
        log.info("🎭 Cargando tareas MOCK para demo - UNA POR CADA PANTALLA WORKFLOW...");
        
        allTasks = new ArrayList<>();
        
        // ========== TAREAS MOCK - UNA POR CADA PANTALLA ==========
        
        // 1. AgentApprovalHumanOverrideViewModel
        allTasks.add(createMockTask("mock-1", "agent-approval-v1", "Aprobar Agente IA - Cliente Banco Nacional", 
            "Agente conversacional para atención bancaria. Requiere revisión humana.", "demo.user", "HIGH", "PENDING",
            "/workflow/agent-approval-override.zul?taskId=mock-1&mock=true"));
            
        // 2. AlertResponseViewModel
        allTasks.add(createMockTask("mock-2", "critical-alert-response", "Responder Alerta Crítica - Sistema Producción", 
            "Alerta crítica detectada en sistema de inferencia", "demo.user", "CRITICAL", "PENDING",
            "/workflow/alert-response.zul?taskId=mock-2&mock=true"));
            
        // 3. BiasMitigationPlanViewModel
        allTasks.add(createMockTask("mock-3", "bias-detection-v1", "Plan Mitigación Sesgo - Modelo HR", 
            "Crear plan para mitigar sesgo detectado en sistema RRHH", "demo.user", "HIGH", "PENDING",
            "/workflow/bias-mitigation-plan.zul?taskId=mock-3&mock=true"));
            
        // 4. BiasReviewViewModel
        allTasks.add(createMockTask("mock-4", "bias-detection-v1", "Revisar Sesgo Detectado - Modelo Scoring Crédito", 
            "Sesgo demográfico: género y edad en aprobaciones", null, "CRITICAL", "PENDING",
            "/workflow/bias-review.zul?taskId=mock-4&mock=true"));
            
        // 5. BiasUrgentDecisionViewModel
        allTasks.add(createMockTask("mock-5", "bias-detection-v1", "Decisión Urgente Sesgo - Sistema Activo", 
            "Sesgo crítico en producción requiere decisión inmediata", "demo.user", "CRITICAL", "PENDING",
            "/workflow/bias-urgent-decision.zul?taskId=mock-5&mock=true"));
            
        // 6. ComplianceReviewDecisionViewModel
        allTasks.add(createMockTask("mock-6", "compliance-monitoring-v1", "Decisión Revisión Compliance - EU AI Act", 
            "Decidir acción tras timer de 7 días en compliance", "demo.user", "MEDIUM", "PENDING",
            "/workflow/compliance-review-decision.zul?taskId=mock-6&mock=true"));
            
        // 7. ComplianceReviewViewModel
        allTasks.add(createMockTask("mock-7", "compliance-monitoring-v1", "Compliance Review - Sistema RAG Legal", 
            "Revisión de hallazgos de compliance", null, "HIGH", "PENDING",
            "/workflow/compliance-review.zul?taskId=mock-7&mock=true"));
            
        // 8. DatasetReviewReminderViewModel
        allTasks.add(createMockTask("mock-8", "dataset-quality-v1", "Recordatorio Dataset - Training Sentiment Analysis", 
            "Revisar calidad de dataset tras reminder", "demo.user", "MEDIUM", "PENDING",
            "/workflow/dataset-review-reminder.zul?taskId=mock-8&mock=true"));
            
        // 9. DriftAnalysisViewModel
        allTasks.add(createMockTask("mock-9", "drift-detection-v1", "Analizar Drift - Modelo Recomendaciones", 
            "Investigar causa raíz de drift detectado (-12% accuracy)", "demo.user", "CRITICAL", "PENDING",
            "/workflow/drift-analysis.zul?taskId=mock-9&mock=true"));
            
        // 10. DriftReviewDecisionViewModel
        allTasks.add(createMockTask("mock-10", "drift-detection-v1", "Decisión Drift - Modelo Predicción Ventas", 
            "Decidir acción correctiva para drift", null, "HIGH", "PENDING",
            "/workflow/drift-review-decision.zul?taskId=mock-10&mock=true"));
            
        // 11. EthicsCommitteeReviewViewModel
        allTasks.add(createMockTask("mock-11", "ethics-review-v1", "Ethics Committee - Chatbot Atención Médica", 
            "Revisión del comité de ética para sistema médico", "demo.user", "CRITICAL", "PENDING",
            "/workflow/ethics-committee-review.zul?taskId=mock-11&mock=true"));
            
        // 12. EthicsMitigationPlanViewModel
        allTasks.add(createMockTask("mock-12", "ethics-review-v1", "Plan Mitigación Ética - Sistema Automático", 
            "Crear plan para mitigar problemas éticos detectados", "demo.user", "HIGH", "PENDING",
            "/workflow/ethics-mitigation-plan.zul?taskId=mock-12&mock=true"));
            
        // 13. EthicsReviewReminderViewModel
        allTasks.add(createMockTask("mock-13", "ethics-review-v1", "Recordatorio Ethics - Revisión Pendiente", 
            "Reminder de revisión ética pendiente", null, "MEDIUM", "PENDING",
            "/workflow/ethics-review-reminder.zul?taskId=mock-13&mock=true"));
            
        // 14. EthicsReviewRequestViewModel
        allTasks.add(createMockTask("mock-14", "ethics-review-v1", "Solicitud Ethics Review - IA Recursos Humanos", 
            "Solicitar revisión ética para sistema de selección", "demo.user", "HIGH", "PENDING",
            "/workflow/ethics-review-request.zul?taskId=mock-14&mock=true"));
            
        // 15. HitlSlaReminderViewModel
        allTasks.add(createMockTask("mock-15", "agent-approval-v1", "Recordatorio SLA HITL - Aprobación Pendiente", 
            "SLA de revisión humana próximo a vencer", "demo.user", "HIGH", "PENDING",
            "/workflow/hitl-sla-reminder.zul?taskId=mock-15&mock=true"));
            
        // 16. LlmEvaluationReviewViewModel
        allTasks.add(createMockTask("mock-16", "llm-evaluation-v1", "Evaluar LLM - GPT-4o para Soporte Cliente", 
            "Revisión de calidad de respuestas del LLM", null, "MEDIUM", "PENDING",
            "/workflow/llm-evaluation-review.zul?taskId=mock-16&mock=true"));
            
        // 17. ModelApprovalHumanOverrideViewModel
        allTasks.add(createMockTask("mock-17", "model-approval-v1", "Aprobar Modelo - Predicción Fraude v4.2", 
            "Modelo de ML para detección de fraude en pagos", "demo.user", "HIGH", "PENDING",
            "/workflow/model-approval-override.zul?taskId=mock-17&mock=true"));
            
        // 18. ModelApprovalReminderViewModel
        allTasks.add(createMockTask("mock-18", "model-approval-v1", "Recordatorio Aprobación Modelo - Churn Prediction", 
            "Reminder de aprobación de modelo pendiente", "demo.user", "MEDIUM", "PENDING",
            "/workflow/model-approval-reminder.zul?taskId=mock-18&mock=true"));
            
        // 19. ModelEvaluationReviewViewModel
        allTasks.add(createMockTask("mock-19", "model-evaluation-v1", "Revisar Evaluación Modelo - Clasificador Sentimientos", 
            "Revisar resultados de evaluación automática", null, "MEDIUM", "PENDING",
            "/workflow/model-evaluation-review.zul?taskId=mock-19&mock=true"));
            
        // 20. PerformanceInterventionViewModel
        allTasks.add(createMockTask("mock-20", "performance-degradation-v1", "Intervención Performance - API Inference", 
            "Latencia crítica: 300ms → 1200ms. Acción inmediata requerida", "demo.user", "CRITICAL", "PENDING",
            "/workflow/performance-intervention.zul?taskId=mock-20&mock=true"));
            
        // 21. PerformanceReviewDecisionViewModel
        allTasks.add(createMockTask("mock-21", "performance-degradation-v1", "Decisión Performance - Endpoint Batch", 
            "Decidir acción para degradación de performance", "demo.user", "HIGH", "PENDING",
            "/workflow/performance-review-decision.zul?taskId=mock-21&mock=true"));
            
        // 22. PromptApprovalRequestViewModel (solicitud)
        // Esta es pantalla de inicio de proceso, no user task, se accede diferente
        
        // 23. PromptHumanReviewViewModel
        allTasks.add(createMockTask("mock-23", "prompt-approval-process", "Revisión Humana Prompt - Marketing Black Friday", 
            "Prompt para campaña marketing requiere revisión", "demo.user", "MEDIUM", "PENDING",
            "/workflow/prompt-human-review.zul?taskId=mock-23&mock=true"));
            
        // 24. RagEvaluationReviewViewModel
        allTasks.add(createMockTask("mock-24", "rag-evaluation-v1", "Evaluar RAG - Sistema Documentación Técnica", 
            "Revisión de calidad de retrieval y generación", null, "MEDIUM", "PENDING",
            "/workflow/rag-evaluation-review.zul?taskId=mock-24&mock=true"));
        
        filteredTasks = new ArrayList<>(allTasks);
        
        // Calcular estadísticas
        totalTasks = allTasks.size();
        assignedToMeCount = (int) allTasks.stream()
            .filter(t -> "demo.user".equals(t.getAssignee()))
            .count();
        groupTasksCount = totalTasks - assignedToMeCount;
        
        // Procesos únicos
        processDefinitions = new ArrayList<>();
        processDefinitions.add("Todos");
        processDefinitions.addAll(allTasks.stream()
            .map(TaskDTO::getProcessDefinitionId)
            .map(this::getProcessName)
            .distinct()
            .sorted()
            .collect(Collectors.toList()));
        
        log.info("✅ Cargadas {} tareas MOCK ({} asignadas, {} de grupos)", 
                 totalTasks, assignedToMeCount, groupTasksCount);
        log.info("🎬 MODO DEMO ACTIVADO: Todas las tareas llevan parámetro mock=true en sus URLs");
    }
    
    /**
     * Crear tarea MOCK para demo
     */
    private TaskDTO createMockTask(String id, String processDefId, String name, String description, 
                                    String assignee, String priorityStr, String status, String formKey) {
        TaskDTO task = new TaskDTO();
        task.setId(id);
        task.setProcessDefinitionId(processDefId);
        task.setName(name);
        task.setDescription(description);
        task.setAssignee(assignee);
        
        // Convertir String a Integer para priority
        Integer priority = convertPriorityToInt(priorityStr);
        task.setPriority(priority);
        
        task.setCreateTime(new java.util.Date(System.currentTimeMillis() - (long)(Math.random() * 86400000))); // Random en último 24h
        task.setFormKey(formKey);
        
        // Variables del proceso simuladas
        Map<String, Object> vars = new HashMap<>();
        vars.put("mockMode", true);
        vars.put("priority", priority);
        vars.put("status", status);
        task.setProcessVariables(vars);
        
        return task;
    }
    
    /**
     * Convierte String de prioridad a Integer
     * Soporta: "CRITICAL", "HIGH", "MEDIUM", "LOW"
     */
    private Integer convertPriorityToInt(String priorityStr) {
        if (priorityStr == null) return 50;
        
        switch (priorityStr.toUpperCase()) {
            case "CRITICAL": return 100;
            case "HIGH": return 90;
            case "MEDIUM": return 50;
            case "LOW": return 10;
            default:
                try {
                    return Integer.parseInt(priorityStr);
                } catch (NumberFormatException e) {
                    return 50;
                }
        }
    }

    /**
     * Filtrar tareas
     */
    @Command
    @NotifyChange("filteredTasks")
    public void filterTasks() {
        filteredTasks = allTasks.stream()
            .filter(this::matchesFilters)
            .collect(Collectors.toList());

        log.info("🔍 Filtradas {} tareas de {} totales", filteredTasks.size(), allTasks.size());
    }

    /**
     * Limpiar filtros
     */
    @Command
    @NotifyChange({"filteredTasks", "selectedProcess", "selectedPriority", "selectedState"})
    public void clearFilters() {
        selectedProcess = "Todos";
        selectedPriority = "Todas";
        selectedState = "Todos";
        filteredTasks = new ArrayList<>(allTasks);
    }

    /**
     * Refrescar tareas
     */
    @Command
    public void refreshTasks() {
        loadTasks();
        Messagebox.show("Tareas actualizadas", "Info", Messagebox.OK, Messagebox.INFORMATION);
    }

    /**
     * Claim (reclamar) tarea
     */
    @Command
    public void claimTask(@BindingParam("task") TaskDTO task) {
        try {
            log.info("👤 Reclamando tarea: {} por {}", task.getId(), currentUsername);

            boolean success = taskManagementService.claimTask(task.getId(), currentUsername);

            if (success) {
                Messagebox.show("Tarea reclamada exitosamente", "Éxito", 
                                Messagebox.OK, Messagebox.INFORMATION);
                loadTasks();
            } else {
                Messagebox.show("Error reclamando tarea", "Error", 
                                Messagebox.OK, Messagebox.ERROR);
            }

        } catch (Exception e) {
            log.error("❌ Error reclamando tarea: {}", e.getMessage(), e);
            Messagebox.show("Error: " + e.getMessage(), "Error", 
                            Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Liberar tarea
     */
    @Command
    public void releaseTask(@BindingParam("task") TaskDTO task) {
        try {
            log.info("🔓 Liberando tarea: {}", task.getId());

            boolean success = taskManagementService.releaseTask(task.getId());

            if (success) {
                Messagebox.show("Tarea liberada exitosamente", "Éxito", 
                                Messagebox.OK, Messagebox.INFORMATION);
                loadTasks();
            } else {
                Messagebox.show("Error liberando tarea", "Error", 
                                Messagebox.OK, Messagebox.ERROR);
            }

        } catch (Exception e) {
            log.error("❌ Error liberando tarea: {}", e.getMessage(), e);
            Messagebox.show("Error: " + e.getMessage(), "Error", 
                            Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Mostrar dialog de asignación
     */
    @Command
    @NotifyChange({"showAssignDialog", "taskToAssign", "availableUsers", "selectedUserToAssign"})
    public void showAssignDialog(@BindingParam("task") TaskDTO task) {
        try {
            log.info("📝 Mostrando dialog de asignación para tarea: {}", task.getId());

            taskToAssign = task;
            availableUsers = taskManagementService.getUsersForTask(task.getId());
            selectedUserToAssign = null;
            showAssignDialog = true;

            log.info("✅ Usuarios disponibles para tarea: {}", availableUsers.size());

        } catch (Exception e) {
            log.error("❌ Error mostrando dialog: {}", e.getMessage(), e);
            Messagebox.show("Error: " + e.getMessage(), "Error", 
                            Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Cerrar dialog de asignación
     */
    @Command
    @NotifyChange({"showAssignDialog", "taskToAssign", "availableUsers", "selectedUserToAssign"})
    public void closeAssignDialog() {
        showAssignDialog = false;
        taskToAssign = null;
        availableUsers.clear();
        selectedUserToAssign = null;
    }

    /**
     * Confirmar asignación
     */
    @Command
    @NotifyChange({"showAssignDialog", "taskToAssign", "availableUsers"})
    public void confirmAssign() {
        try {
            if (selectedUserToAssign == null) {
                Messagebox.show("Debe seleccionar un usuario", "Advertencia", 
                                Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }

            log.info("✅ Asignando tarea {} a usuario {}", 
                     taskToAssign.getId(), selectedUserToAssign.getUsername());

            boolean success = taskManagementService.assignTaskToUser(
                taskToAssign.getId(), 
                selectedUserToAssign.getUsername()
            );

            if (success) {
                Messagebox.show(
                    "Tarea asignada exitosamente a " + selectedUserToAssign.getFullname(), 
                    "Éxito", 
                    Messagebox.OK, 
                    Messagebox.INFORMATION
                );
                closeAssignDialog();
                loadTasks();
            } else {
                Messagebox.show("Error asignando tarea", "Error", 
                                Messagebox.OK, Messagebox.ERROR);
            }

        } catch (Exception e) {
            log.error("❌ Error confirmando asignación: {}", e.getMessage(), e);
            Messagebox.show("Error: " + e.getMessage(), "Error", 
                            Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Abrir formulario ZUL de la tarea
     */
    @Command
    public void openTaskForm(@BindingParam("task") TaskDTO task) {
        try {
            if (task.getFormKey() == null || task.getFormKey().isEmpty()) {
                Messagebox.show("Esta tarea no tiene formulario asociado", "Advertencia", 
                                Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }

            log.info("📄 Abriendo formulario ZUL: {}", task.getFormKey());

            // Pasar variables a la sesión para que el formulario las pueda leer
            Sessions.getCurrent().setAttribute("taskId", task.getId());
            Sessions.getCurrent().setAttribute("processInstanceId", task.getProcessInstanceId());

            // Abrir formulario ZUL en nueva ventana/tab
            String zulPath = "/console/" + task.getFormKey();
            Executions.createComponents(zulPath, null, null);

        } catch (Exception e) {
            log.error("❌ Error abriendo formulario: {}", e.getMessage(), e);
            Messagebox.show("Error abriendo formulario: " + e.getMessage(), "Error", 
                            Messagebox.OK, Messagebox.ERROR);
        }
    }

    // ===============================================
    // HELPERS
    // ===============================================

    private boolean matchesFilters(TaskDTO task) {
        // Filtro por proceso
        if (!"Todos".equals(selectedProcess)) {
            String processName = getProcessName(task.getProcessDefinitionId());
            if (!selectedProcess.equals(processName)) {
                return false;
            }
        }

        // Filtro por prioridad
        if (!"Todas".equals(selectedPriority)) {
            int priority = task.getPriority() != null ? task.getPriority() : 50;
            if ("Alta (90-100)".equals(selectedPriority) && priority < 90) return false;
            if ("Media (50-89)".equals(selectedPriority) && (priority < 50 || priority >= 90)) return false;
            if ("Baja (0-49)".equals(selectedPriority) && priority >= 50) return false;
        }

        // Filtro por estado
        if (!"Todos".equals(selectedState)) {
            if ("Asignadas a Mí".equals(selectedState) && !currentUsername.equals(task.getAssignee())) {
                return false;
            }
            if ("De Mis Roles".equals(selectedState) && currentUsername.equals(task.getAssignee())) {
                return false;
            }
            if ("Sin Asignar".equals(selectedState) && task.getAssignee() != null) {
                return false;
            }
        }

        return true;
    }

    public String getTaskRowClass(TaskDTO task) {
        int priority = task.getPriority() != null ? task.getPriority() : 50;
        if (priority >= 90) return "task-row-high";
        if (priority >= 50) return "task-row-medium";
        return "task-row-low";
    }

    public String getPriorityClass(Integer priority) {
        if (priority == null) priority = 50;
        if (priority >= 90) return "priority-high";
        if (priority >= 50) return "priority-medium";
        return "priority-low";
    }

    public String getProcessName(String processDefinitionId) {
        if (processDefinitionId == null) return "N/A";
        
        // Extraer nombre del proceso del ID
        // Formato: "agent-approval-process-v3:1:12345"
        String[] parts = processDefinitionId.split(":");
        if (parts.length > 0) {
            String key = parts[0];
            // Convertir kebab-case a Title Case
            return Arrays.stream(key.split("-"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
        }
        return processDefinitionId;
    }

    public String formatCandidateGroups(List<String> groups) {
        if (groups == null || groups.isEmpty()) {
            return "N/A";
        }
        return String.join(", ", groups);
    }

    public String getDueDateStyle(Date dueDate) {
        if (dueDate == null) return "";
        
        long now = System.currentTimeMillis();
        long due = dueDate.getTime();
        long diff = due - now;
        
        // Rojo si ya pasó o falta menos de 2 horas
        if (diff < 0 || diff < 2 * 60 * 60 * 1000) {
            return "color: red; font-weight: bold;";
        }
        // Naranja si falta menos de 24 horas
        if (diff < 24 * 60 * 60 * 1000) {
            return "color: orange; font-weight: bold;";
        }
        return "";
    }

    public boolean isAssignedToMe(TaskDTO task) {
        return currentUsername.equals(task.getAssignee());
    }

    public String getTaskVariables(TaskDTO task) {
        if (task == null) return "";
        
        try {
            Map<String, Object> variables = runtimeService.getVariables(task.getProcessInstanceId());
            if (variables.isEmpty()) {
                return "<i>No hay variables</i>";
            }

            StringBuilder html = new StringBuilder("<ul style='margin:0; padding-left:20px;'>");
            variables.entrySet().stream()
                .limit(10) // Mostrar solo las primeras 10
                .forEach(entry -> {
                    String value = entry.getValue() != null ? entry.getValue().toString() : "null";
                    if (value.length() > 50) value = value.substring(0, 50) + "...";
                    html.append("<li><b>").append(entry.getKey()).append(":</b> ")
                        .append(value).append("</li>");
                });
            html.append("</ul>");

            return html.toString();

        } catch (Exception e) {
            return "<i>Error obteniendo variables</i>";
        }
    }
    
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
            businessService.save(activityLog);
        } catch (Exception e) {
            log.error("Error al auditar acción: {} en módulo: {}", action, model, e);
        }
    }
    
    @org.zkoss.bind.annotation.Destroy
    public void destroy() {
        if (allTasks != null) { allTasks.clear(); allTasks = null; }
        if (filteredTasks != null) { filteredTasks.clear(); filteredTasks = null; }
        if (availableUsers != null) { availableUsers.clear(); availableUsers = null; }
        businessService = null;
        taskService = null;
    }
}

