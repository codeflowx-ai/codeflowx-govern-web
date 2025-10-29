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
import org.zkoss.bind.annotation.NotifyChange;
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
 * ViewModel: Performance Review Decision (URGENTE - Timer 30min)
 * 
 * Proceso BPMN: 11_PERFORMANCE_DEGRADATION (performance-degradation-v1)
 * User Task: performanceReviewDecisionTask
 * Candidate Groups: ml-ops, platform-admins
 * 
 * Funcionalidad:
 * Decisión urgente cuando se detecta degradación crítica de performance.
 * Timer de 30 minutos para tomar acción inmediata.
 * 
 * Input Variables:
 * - modelName: String
 * - avgLatency, avgThroughput, avgErrorRate: Double
 * - severity: String
 * 
 * Output Variables:
 * - reviewDecision: "emergency_rollback" | "scale_now" | "monitor" | "accept_risk"
 * - performanceReviewJustification: String
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class PerformanceReviewDecisionViewModel extends MasterPage {

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
    private Double avgLatency;
    private Double avgThroughput;
    private Double avgErrorRate;
    private String severity;
    private String selectedDecision;
    private String justification;
    
    // Mock mode
    private boolean mockMode = false;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        Map<String, String[]> params = Executions.getCurrent().getParameterMap();
        
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
            taskId = Executions.getCurrent().getParameter("taskId");
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            processInstanceId = task.getProcessInstanceId();

            modelName = (String) runtimeService.getVariable(processInstanceId, "modelName");
            avgLatency = (Double) runtimeService.getVariable(processInstanceId, "avgLatency");
            avgThroughput = (Double) runtimeService.getVariable(processInstanceId, "avgThroughput");
            avgErrorRate = (Double) runtimeService.getVariable(processInstanceId, "avgErrorRate");
            severity = (String) runtimeService.getVariable(processInstanceId, "severity");

            log.info("🚨 Performance Review - Model: {} | Latency: {}ms", modelName, avgLatency);
        } catch (Exception e) {
            log.error("Error init", e);
        }
    }

    @Command
    @NotifyChange("selectedDecision")
    public void selectDecision(String decision) {
        this.selectedDecision = decision;
    }

    @Command
    public void confirmDecision() {
        try {
            if (mockMode) {
                log.info("🎭 Mock mode: Simulando confirmDecision - decision={}", selectedDecision);
                logActivity("MOCK_CONFIRM_PERF_DECISION", "PerformanceReview", null, "Simulación de decisión: " + selectedDecision);
                Messagebox.show("✅ [DEMO] Decisión confirmada: " + selectedDecision, "Demo Mode", 
                    Messagebox.OK, Messagebox.INFORMATION,
                    event -> Executions.getCurrent().sendRedirect("/plataforma/workflow/my-tasks.zul?mock=true"));
                return;
            }
            
            runtimeService.setVariable(processInstanceId, "reviewDecision", selectedDecision);
            runtimeService.setVariable(processInstanceId, "performanceReviewJustification", justification);
            taskService.complete(taskId);
            
            logActivity("CONFIRM_PERF_DECISION", "PerformanceReview", null, "Decisión: " + selectedDecision);

            log.info("✅ Decisión urgente confirmada: {}", selectedDecision);

            Messagebox.show("Decisión confirmada: " + selectedDecision, "Confirmado", Messagebox.OK, Messagebox.INFORMATION,
                event -> Executions.getCurrent().sendRedirect("/plataforma/workflow/my-tasks.zul"));
        } catch (Exception e) {
            log.error("Error", e);
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
        this.taskId = "mock-perf-review-decision-task-001";
        this.processInstanceId = "mock-process-perf-review-decision-001";
        this.modelName = "Payment-Fraud-Detector-v1";
        this.avgLatency = 1200.0;
        this.avgThroughput = 38.0;
        this.avgErrorRate = 5.8;
        this.severity = "CRITICAL";
        
        log.info("🎭 Mock data loaded for Performance Review Decision");
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
