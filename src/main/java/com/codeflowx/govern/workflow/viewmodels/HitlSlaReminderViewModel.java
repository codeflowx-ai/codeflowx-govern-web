package com.codeflowx.govern.workflow.viewmodels;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.admin.Ssoractividad;

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel: HITL SLA Reminder
 * 
 * Proceso BPMN: 01_AGENT_APPROVAL (agent-approval-v1)
 * User Task: hitlReminder (disparado por Timer Boundary 24h)
 * Candidate Groups: governance-leads
 * 
 * Funcionalidad:
 * La revisión humana (hitlTask) lleva más de 24h sin completarse.
 * Se notifica a governance-leads para escalar o tomar acción.
 * 
 * Input Variables:
 * - agentName: String
 * - hitlTaskCreatedAt: Timestamp
 * 
 * Output Variables:
 * - sla_action: 'escalate', 'extend', 'complete_now'
 * - sla_notes: String
 * - sla_handler: String
 * - sla_timestamp: Timestamp
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class HitlSlaReminderViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;

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

    @WireVariable
    private TaskService taskService;

    @WireVariable
    private RuntimeService runtimeService;

    private String taskId;
    private String processInstanceId;
    private String agentName;
    private String elapsedTime;
    private String slaAction;
    private String slaNotes;
    
    // Mock mode
    private boolean mockMode = false;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        Map<String, String[]> params = Executions.getCurrent().getParameterMap();
        
        // Detectar mock mode
        if (params.containsKey("mock") && "true".equals(params.get("mock")[0])) {
            this.mockMode = true;
            loadMockData();
            log.info("🎭 Mock mode activado");
            return;
        }
        
        if (params.containsKey("taskId")) {
            this.taskId = params.get("taskId")[0];
            loadTaskData();
        }
    }
    
   
    
    private void setBeans() {
        // Beans ya inyectados por @WireVariable
    }

    private void loadTaskData() {
        try {
            Map<String, Object> variables = taskService.getVariables(taskId);
            agentName = variables.get("agentName") != null ? variables.get("agentName").toString() : "N/A";

            // Calcular tiempo transcurrido
            Timestamp taskCreated = (Timestamp) variables.get("hitlTaskCreatedAt");
            if (taskCreated != null) {
                long diffMillis = System.currentTimeMillis() - taskCreated.getTime();
                long hours = TimeUnit.MILLISECONDS.toHours(diffMillis);
                elapsedTime = hours + " horas";
            } else {
                elapsedTime = "Más de 24 horas";
            }

            log.info("✅ SLA Reminder cargado - Agent: {}, Elapsed: {}", agentName, elapsedTime);

        } catch (Exception e) {
            log.error("❌ Error inicializando: {}", e.getMessage(), e);
        }
    }

    @Command
    public void confirm() {
        try {
            if (slaAction == null || slaAction.isEmpty()) {
                Messagebox.show("Debe seleccionar una acción", "Advertencia", 
                                Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }

            if (mockMode) {
                log.info("🎭 Mock mode: Simulando confirm - action={}", slaAction);
                logActivity("MOCK_CONFIRM_SLA", "HitlSlaReminder", null, "Simulación de acción SLA: " + slaAction);
                Messagebox.show("✅ [DEMO] Acción registrada: " + getSlaActionLabel(slaAction), "Demo Mode", 
                    Messagebox.OK, Messagebox.INFORMATION,
                    event -> Executions.getCurrent().sendRedirect("/plataforma/workflow/my-tasks.zul?mock=true"));
                return;
            }

            String username = super.getUser() != null ? super.getUser().getUsername() : "SYSTEM";
            
            Map<String, Object> outputVariables = new HashMap<>();
            outputVariables.put("sla_action", slaAction);
            outputVariables.put("sla_notes", slaNotes);
            outputVariables.put("sla_handler", username);
            outputVariables.put("sla_timestamp", new Timestamp(System.currentTimeMillis()));

            taskService.complete(taskId, outputVariables);
            
            logActivity("CONFIRM_SLA", "HitlSlaReminder", null, "Acción SLA: " + slaAction);

            Messagebox.show(
                "Acción registrada: " + getSlaActionLabel(slaAction), 
                "Éxito", 
                Messagebox.OK, 
                Messagebox.INFORMATION,
                event -> Executions.getCurrent().sendRedirect("/plataforma/workflow/my-tasks.zul")
            );

        } catch (Exception e) {
            log.error("❌ Error completando SLA reminder: {}", e.getMessage(), e);
            Messagebox.show("Error: " + e.getMessage(), "Error", 
                            Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    public void cancel() {
        String redirect = mockMode ? "/plataforma/workflow/my-tasks.zul?mock=true" : "/plataforma/workflow/my-tasks.zul";
        Executions.getCurrent().sendRedirect(redirect);
    }

    private String getSlaActionLabel(String action) {
        switch (action) {
            case "escalate": return "Escalado a superior";
            case "extend": return "Plazo extendido 24h";
            case "complete_now": return "Asumir tarea ahora";
            default: return action;
        }
    }
    
    /**
     * Mock data para demos
     */
    private void loadMockData() {
        this.taskId = "mock-hitl-sla-task-001";
        this.processInstanceId = "mock-process-hitl-sla-001";
        this.agentName = "Customer-Support-Bot-v2";
        this.elapsedTime = "28 horas";
        
        log.info("🎭 Mock data loaded for HITL SLA Reminder");
    }
    
    /**
     * Registra actividad del usuario
     */
    private void logActivity(String action, String model, Long pk, String mensaje) {
        try {
            Ssoractividad activityLog = new Ssoractividad();
            activityLog.setUsername(getUser().getUsername());
            activityLog.setAccion(action);
            activityLog.setAlta(new Timestamp(System.currentTimeMillis()));
            activityLog.setModulo(model);
            activityLog.setIdtupla(pk != null ? pk.intValue() : 0);
            activityLog.setAplicacion(ctxBean.getApplicationName());
            activityLog.setValuetupla(mensaje);
            businessService.save(activityLog);
        } catch (Exception e) {
            log.error("Error al auditar acción: {} en módulo: {}", action, model, e);
        }
    }
    
    @Destroy
    public void destroy() {
        businessService = null;
        taskService = null;
    }
}
