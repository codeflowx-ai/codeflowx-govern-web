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
 * ViewModel: Performance Intervention (Manual)
 * 
 * Proceso BPMN: 11_PERFORMANCE_DEGRADATION (performance-degradation-v1)
 * User Task: performanceInterventionTask
 * Candidate Groups: ml-ops, platform-engineers
 * 
 * Funcionalidad:
 * Intervención manual cuando se detecta degradación de performance.
 * Permite decidir acciones correctivas: reboot, scale, retrain, rollback, etc.
 * 
 * Input Variables:
 * - model_name: String
 * - avgLatency: Double
 * - avgThroughput: Double
 * - avgErrorRate: Double
 * - severity: String
 * 
 * Output Variables:
 * - decision: "reboot" | "scale_up" | "retrain" | "rollback" | "monitor"
 * - notes: String
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class PerformanceInterventionViewModel extends MasterPage {

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
    private String decision;
    private String notes;
    
    // Mock mode
    private boolean mockMode = false;
    
    // Mock data
    private String modelName;
    private Double avgLatency;
    private Double avgThroughput;
    private Double avgErrorRate;
    private String severity;

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
            org.flowable.task.api.Task task = taskService.createTaskQuery()
                .taskId(taskId).singleResult();
            this.processInstanceId = task.getProcessInstanceId();
            
            Map<String, Object> variables = runtimeService.getVariables(processInstanceId);
            this.modelName = (String) variables.get("model_name");
            this.avgLatency = (Double) variables.get("avgLatency");
            this.avgThroughput = (Double) variables.get("avgThroughput");
            this.avgErrorRate = (Double) variables.get("avgErrorRate");
            this.severity = (String) variables.get("severity");
            
            log.info("✅ Task data loaded: taskId={}", taskId);
        } catch (Exception e) {
            log.error("❌ Error cargando task data", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", 
                Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"*"})
    public void doSubmit() {
        try {
            if (mockMode) {
                log.info("🎭 Mock mode: Simulando submit - decision={}", decision);
                logActivity("MOCK_SUBMIT_PERF_INTERVENTION", "PerformanceIntervention", null, "Simulación de decisión: " + decision);
                Messagebox.show("✅ [DEMO] Decisión registrada exitosamente", "Demo Mode", 
                    Messagebox.OK, Messagebox.INFORMATION,
                    event -> Executions.getCurrent().sendRedirect("/plataforma/workflow/my-tasks.zul?mock=true"));
                return;
            }
            
            Map<String, Object> taskVariables = new HashMap<>();
            taskVariables.put("decision", decision);
            taskVariables.put("notes", notes);
            
            taskService.complete(taskId, taskVariables);
            
            logActivity("SUBMIT_PERF_INTERVENTION", "PerformanceIntervention", null, "Decisión: " + decision);
            
            Messagebox.show("Decision submitted", "Success", 
                Messagebox.OK, Messagebox.INFORMATION,
                event -> Executions.getCurrent().sendRedirect("/plataforma/workflow/my-tasks.zul"));
                
        } catch (Exception e) {
            log.error("❌ Error submitting", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", 
                Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    public void doCancel() {
        String redirect = mockMode ? "/plataforma/workflow/my-tasks.zul?mock=true" : "/plataforma/workflow/my-tasks.zul";
        Executions.getCurrent().sendRedirect(redirect);
    }
    
    /**
     * Mock data para demos
     */
    private void loadMockData() {
        this.taskId = "mock-perf-intervention-task-001";
        this.processInstanceId = "mock-process-perf-intervention-001";
        this.modelName = "Recommendation-Engine-v4";
        this.avgLatency = 850.0;
        this.avgThroughput = 45.0;
        this.avgErrorRate = 3.2;
        this.severity = "HIGH";
        
        log.info("🎭 Mock data loaded for Performance Intervention");
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
