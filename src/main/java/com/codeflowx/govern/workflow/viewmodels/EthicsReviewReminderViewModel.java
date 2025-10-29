package com.codeflowx.govern.workflow.viewmodels;

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
 * ViewModel: Ethics Review Reminder
 * 
 * BPMN Process: ethics-review-v1
 * User Task: ethicsReviewReminderTask
 * Candidate Groups: ethics-committee
 * Triggered by: Timer Boundary Event (PT72H - después de 72 horas)
 * 
 * Funcionalidad:
 * - Recordatorio de revisión ética pendiente después de 72h
 * - Mostrar estado actual de la revisión
 * - Decidir: PROCEED_REVIEW (continuar), POSTPONE (posponer), CANCEL (cancelar)
 * - Justificar decisión de postponer o cancelar
 * 
 * Input Variables (desde proceso BPMN):
 * - systemId: Long
 * - systemName: String
 * - pendingSinceDays: Integer - Días pendiente
 * - reviewPriority: String
 * 
 * Output Variables (al completar task):
 * - reminder_decision: String - 'proceed', 'postpone', 'cancel'
 * - reminder_notes: String
 * - postpone_days: Integer (si aplica)
 * - decided_by: String
 * - decision_time: Timestamp
 * 
 * Modo MOCK:
 * - URL: /workflow/ethics-review-reminder.zul?taskId=mock-13&mock=true
 * - Datos simulados: Revisión ética pendiente 3 días, prioridad media
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class EthicsReviewReminderViewModel extends MasterPage {

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

    @Getter
    private boolean mockMode = false;

    private String taskId;
    private String processInstanceId;
    private String systemName;
    private Integer pendingSinceDays;
    private String reviewPriority;
    private String selectedDecision;
    private String notes;
    private Integer postponeDays = 7;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
     // Detectar mock mode desde parámetros URL
        if(System.getenv("MOCK_MODE")!=null) {
        	mockMode = Boolean.parseBoolean(System.getenv("MOCK_MODE").toString());
        }
       
        
        
        log.info("🚀 Inicializando EthicsReviewReminderViewModel - MOCK MODE: {}", mockMode);
        
        if (mockMode) {
            loadMockData();
        } else {
            loadRealData();
        }
    }
    
    private void loadMockData() {
        taskId = Executions.getCurrent().getParameter("taskId");
        systemName = "Sistema IA Evaluación Desempeño";
        pendingSinceDays = 3;
        reviewPriority = "MEDIUM";
        log.info("✅ Datos MOCK cargados - Reminder de revisión ética");
    }
    
    private void loadRealData() {
        try {
            taskId = Executions.getCurrent().getParameter("taskId");
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            processInstanceId = task.getProcessInstanceId();
            systemName = (String) runtimeService.getVariable(processInstanceId, "systemName");
            pendingSinceDays = (Integer) runtimeService.getVariable(processInstanceId, "pendingSinceDays");
            reviewPriority = (String) runtimeService.getVariable(processInstanceId, "reviewPriority");
        } catch (Exception e) {
            log.error("Error cargando datos", e);
        }
    }

    @Command
    public void confirm() {
        if (mockMode) {
            Messagebox.show("✅ DEMO: Decisión confirmada\n\nSistema: " + systemName + "\nDecisión: " + selectedDecision + "\n\n(Modo MOCK)", 
                "Demo", Messagebox.OK, Messagebox.INFORMATION,
                e -> Executions.sendRedirect("/workflow/task-inbox.zul?mock=true"));
            return;
        }
        
        runtimeService.setVariable(processInstanceId, "reminder_decision", selectedDecision);
        runtimeService.setVariable(processInstanceId, "reminder_notes", notes);
        if ("POSTPONE".equals(selectedDecision)) {
            runtimeService.setVariable(processInstanceId, "postpone_days", postponeDays);
        }
        taskService.complete(taskId);
        logActivity("REMINDER", "ETHETHICSREVIEWS", Long.parseLong(taskId), "Ethics reminder: " + selectedDecision);
        Messagebox.show("Decisión confirmada", "OK", Messagebox.OK, Messagebox.INFORMATION,
            e -> Executions.sendRedirect("/console/govern/ethics-dashboard.zul"));
    }

    @Command
    public void cancel() {
        Executions.sendRedirect(mockMode ? "/workflow/task-inbox.zul?mock=true" : "/console/govern/ethics-dashboard.zul");
    }
    
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
        businessService = null;
        taskService = null;
        runtimeService = null;
    }
}
