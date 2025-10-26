package com.codeflowx.govern.workflow.viewmodels;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.MasterPage;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
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
 * ViewModel: Model Approval Reminder (SLA Timer)
 * 
 * Proceso BPMN: 02_MODEL_APPROVAL (model-approval-v1)
 * User Task: modelApprovalReminder (disparado por Timer Boundary 48h)
 * Candidate Groups: governance-leads, ml-ops
 * 
 * Funcionalidad:
 * La aprobación de modelo lleva más de 48h sin completarse.
 * Se notifica para escalar o tomar acción sobre el SLA.
 * 
 * Input Variables:
 * - modelName: String
 * - approvalCreatedAt: Timestamp
 * 
 * Output Variables:
 * - slaDecision: "escalate" | "extend" | "approve_now" | "reject_now"
 * - slaReminderNotes: String
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class ModelApprovalReminderViewModel extends MasterPage {

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
    private String modelName;
    private String selectedDecision;
    private String notes;
    
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
        
        taskId = Executions.getCurrent().getParameter("taskId");
        if (taskId != null) {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            processInstanceId = task.getProcessInstanceId();
            modelName = (String) runtimeService.getVariable(processInstanceId, "modelName");
        }
    }
    
    
    
    private void setBeans() {
        // Beans ya inyectados por @WireVariable
    }

    @Command
    public void confirm() {
        try {
            if (mockMode) {
                log.info("🎭 Mock mode: Simulando confirm - decision={}", selectedDecision);
                logActivity("MOCK_CONFIRM_MODEL_SLA", "ModelApprovalReminder", null, "Simulación de decisión SLA: " + selectedDecision);
                Messagebox.show("✅ [DEMO] Decisión SLA confirmada", "Demo Mode", 
                    Messagebox.OK, Messagebox.INFORMATION,
                    event -> Executions.getCurrent().sendRedirect("/plataforma/workflow/my-tasks.zul?mock=true"));
                return;
            }
            
            runtimeService.setVariable(processInstanceId, "slaDecision", selectedDecision);
            runtimeService.setVariable(processInstanceId, "slaReminderNotes", notes);
            taskService.complete(taskId);
            
            logActivity("CONFIRM_MODEL_SLA", "ModelApprovalReminder", null, "Decisión SLA: " + selectedDecision);
            
            Messagebox.show("Decisión SLA confirmada", "OK", Messagebox.OK, Messagebox.INFORMATION,
                e -> Executions.getCurrent().sendRedirect("/plataforma/workflow/my-tasks.zul"));
        } catch (Exception e) {
            log.error("Error confirmando", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    public void cancel() {
        String redirect = mockMode ? "/plataforma/workflow/my-tasks.zul?mock=true" : "/plataforma/workflow/my-tasks.zul";
        Executions.getCurrent().sendRedirect(redirect);
    }
    
    /**
     * Mock data para demos
     */
    private void loadMockData() {
        this.taskId = "mock-model-approval-reminder-task-001";
        this.processInstanceId = "mock-process-model-approval-reminder-001";
        this.modelName = "Credit-Risk-ML-Model-v3";
        
        log.info("🎭 Mock data loaded for Model Approval Reminder");
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
