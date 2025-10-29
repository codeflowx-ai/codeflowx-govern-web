package com.codeflowx.govern.workflow.viewmodels;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.flowable.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.framework.zkoss.BaseFront;

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel: Agent Approval - Human Override Review
 * 
 * BPMN Process: agent-approval-v1
 * User Task: hitlTask
 * Candidate Groups: governance-admins
 * 
 * Funcionalidad:
 * - Mostrar scores de evaluaciones (risk, compliance, ethics)
 * - Mostrar decisión de Drools
 * - Permitir al usuario aprobar o rechazar
 * - Completar User Task con decisión
 * 
 * Input Variables:
 * - agentId, agentName
 * - riskScore, complianceScore, ethicsScore
 * - minScore, confidenceLevel, justification
 * 
 * Output Variables:
 * - human_decision: 'approve' o 'reject'
 * - human_notes: String
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class AgentApprovalHumanOverrideViewModel extends BaseFront<AgentApprovalHumanOverrideViewModel> {
   private static final long serialVersionUID = 1L;
    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }

    // ========== Servicios Flowable ==========
    @WireVariable
    private TaskService taskService;

    // ========== Datos ==========
    private String taskId;
    private String processInstanceId;
    private Long agentId;
    private String agentName;
    private Integer riskScore;
    private Integer complianceScore;
    private Integer ethicsScore;
    private Integer minScore;
    private Double confidenceLevel;
    private String justification;
    private String humanDecision;  // 'approve' o 'reject'
    private String humanNotes;
    
    // Mock mode
    private boolean mockMode = false;

    // ========== Inicialización ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        log.info("🚀 Inicializando AgentApprovalHumanOverrideViewModel");
        
        // Detectar mock mode desde parámetros URL
        if(System.getenv("MOCK_MODE")!=null) {
        	mockMode = Boolean.parseBoolean(System.getenv("MOCK_MODE").toString());
        }
       
        if (mockMode) {
            this.mockMode = true;
            loadMockData();
            log.info("🎭 Mock mode activado");
            return;
        }

        try {
            // Obtener taskId de la sesión (pasado por TaskInboxViewModel)
            taskId = (String) Sessions.getCurrent().getAttribute("taskId");
            processInstanceId = (String) Sessions.getCurrent().getAttribute("processInstanceId");

            if (taskId == null) {
                log.error("❌ No se encontró taskId en sesión");
                Messagebox.show("Error: No se puede cargar la tarea", "Error", 
                                Messagebox.OK, Messagebox.ERROR);
                return;
            }

            log.info("📋 Cargando tarea: {}", taskId);

            // Cargar variables del proceso
            Map<String, Object> variables = taskService.getVariables(taskId);

            agentId = getLongVariable(variables, "agentId");
            agentName = getStringVariable(variables, "agentName");
            riskScore = getIntVariable(variables, "riskScore");
            complianceScore = getIntVariable(variables, "complianceScore");
            ethicsScore = getIntVariable(variables, "ethicsScore");
            minScore = getIntVariable(variables, "minScore");
            confidenceLevel = getDoubleVariable(variables, "confidenceLevel");
            justification = getStringVariable(variables, "justification");

            log.info("✅ Variables cargadas - Agent: {}, MinScore: {}", agentName, minScore);

        } catch (Exception e) {
            log.error("❌ Error inicializando: {}", e.getMessage(), e);
            Messagebox.show("Error cargando datos: " + e.getMessage(), "Error", 
                            Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Confirmar decisión y completar tarea
     */
    @Command
    public void confirm() {
        try {
            if (humanDecision == null || humanDecision.isEmpty()) {
                Messagebox.show("Debe seleccionar una decisión (Aprobar/Rechazar)", "Advertencia", 
                                Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }

            if (humanNotes == null || humanNotes.trim().isEmpty()) {
                Messagebox.show("Debe proporcionar notas justificando su decisión", "Advertencia", 
                                Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            if (mockMode) {
                log.info("🎭 Mock mode: Simulando confirm - decision={}", humanDecision);
                logActivity("MOCK_CONFIRM_AGENT_APPROVAL", "AgentApproval", agentId, "Simulación de decisión: " + humanDecision);
                Messagebox.show("✅ [DEMO] Decisión registrada: " + (humanDecision.equals("approve") ? "APROBADO" : "RECHAZADO"), 
                    "Demo Mode", Messagebox.OK, Messagebox.INFORMATION,
                    event -> Executions.getCurrent().sendRedirect("/plataforma/workflow/my-tasks.zul?mock=true"));
                return;
            }

            log.info("✅ Completando tarea {} con decisión: {}", taskId, humanDecision);

            // Setear variables de salida
            Map<String, Object> outputVariables = new HashMap<>();
            outputVariables.put("human_decision", humanDecision);
            outputVariables.put("human_notes", humanNotes);
            outputVariables.put("human_reviewer", getCurrentUsername());
            outputVariables.put("human_review_timestamp", new java.sql.Timestamp(System.currentTimeMillis()));

            // Completar tarea
            taskService.complete(taskId, outputVariables);

            log.info("✅ Tarea completada exitosamente");

            // Mostrar confirmación y cerrar ventana
            Messagebox.show(
                "Decisión registrada exitosamente: " + (humanDecision.equals("approve") ? "APROBADO" : "RECHAZADO"), 
                "Éxito", 
                Messagebox.OK, 
                Messagebox.INFORMATION,
                event -> {
                    // Cerrar ventana
                    Executions.getCurrent().sendRedirect("/console/bpmn/task-inbox.zul");
                }
            );

        } catch (Exception e) {
            log.error("❌ Error completando tarea: {}", e.getMessage(), e);
            Messagebox.show("Error: " + e.getMessage(), "Error", 
                            Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Cancelar y volver a bandeja
     */
    @Command
    public void cancel() {
        String redirect = mockMode ? "/plataforma/workflow/my-tasks.zul?mock=true" : "/console/bpmn/task-inbox.zul";
        Executions.getCurrent().sendRedirect(redirect);
    }
    
    /**
     * Mock data para demos
     */
    private void loadMockData() {
        this.taskId = "mock-agent-approval-task-001";
        this.processInstanceId = "mock-process-agent-approval-001";
        this.agentId = 101L;
        this.agentName = "Customer-Support-Bot-v3";
        this.riskScore = 72;
        this.complianceScore = 88;
        this.ethicsScore = 81;
        this.minScore = 75;
        this.confidenceLevel = 0.85;
        this.justification = "Agent requires human review due to moderate risk score below governance threshold";
        
        log.info("🎭 Mock data loaded for Agent Approval Human Override");
    }

    /**
     * Obtener clase CSS según score
     */
    public String getScoreClass(Integer score) {
        if (score == null) return "";
        if (score >= 85) return "score-high";
        if (score >= 60) return "score-medium";
        return "score-low";
    }

    // ===============================================
    // PRIVATE HELPERS
    // ===============================================

    private String getCurrentUsername() {
        String username = (String) Sessions.getCurrent().getAttribute("username");
        return username != null ? username : "unknown";
    }

    private Long getLongVariable(Map<String, Object> vars, String key) {
        Object value = vars.get(key);
        if (value instanceof Long) return (Long) value;
        if (value instanceof Integer) return ((Integer) value).longValue();
        return null;
    }

    private String getStringVariable(Map<String, Object> vars, String key) {
        Object value = vars.get(key);
        return value != null ? value.toString() : "";
    }

    private Integer getIntVariable(Map<String, Object> vars, String key) {
        Object value = vars.get(key);
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Long) return ((Long) value).intValue();
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private Double getDoubleVariable(Map<String, Object> vars, String key) {
        Object value = vars.get(key);
        if (value instanceof Double) return (Double) value;
        if (value instanceof Float) return ((Float) value).doubleValue();
        if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
   
    
    @org.zkoss.bind.annotation.Destroy
    public void destroy() {
        businessService = null;
        taskService = null;
    }
}
