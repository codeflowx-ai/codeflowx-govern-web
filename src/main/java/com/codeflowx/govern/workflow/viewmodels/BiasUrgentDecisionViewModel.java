package com.codeflowx.govern.workflow.viewmodels;

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

@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class BiasUrgentDecisionViewModel extends MasterPage {

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
    private TaskService taskService;
    
    @WireVariable
    private RuntimeService runtimeService;
    
    // ========== Datos ==========
    private String taskId;
    private String processInstanceId;
    private String modelName;
    private Double disparateImpact;
    private String selectedDecision;
    private String justification;
    
    private boolean mockMode = false;

    // ========== Inicialización ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        log.info("🚀 Inicializando BiasUrgentDecisionViewModel");
        
        java.util.Map<String, String[]> params = Executions.getCurrent().getParameterMap();
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
        
        taskId = Executions.getCurrent().getParameter("taskId");
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        processInstanceId = task.getProcessInstanceId();
        modelName = (String) runtimeService.getVariable(processInstanceId, "modelName");
        disparateImpact = (Double) runtimeService.getVariable(processInstanceId, "disparateImpact");
    }
    
    private void loadMockData() {
        this.taskId = "mock-bias-urgent-001";
        this.processInstanceId = "mock-bias-urgent-process-001";
        this.modelName = "Credit-Risk-Predictor-v2";
        this.disparateImpact = 0.72;
        log.info("🎭 Mock data loaded for Bias Urgent Decision");
    }

    @Command
    public void confirm() {
        if (mockMode) {
            log.info("🎭 Mock mode: Simulando confirm - decision={}", selectedDecision);
            logActivity("MOCK_BIAS_URGENT", "BiasUrgent", null, "Simulación: " + selectedDecision);
            Messagebox.show("✅ [DEMO] Decisión urgente confirmada: " + selectedDecision, "Demo Mode", 
                Messagebox.OK, Messagebox.INFORMATION,
                e -> Executions.sendRedirect("/plataforma/workflow/my-tasks.zul?mock=true"));
            return;
        }
        
        runtimeService.setVariable(processInstanceId, "urgentDecision", selectedDecision);
        runtimeService.setVariable(processInstanceId, "biasUrgentJustification", justification);
        taskService.complete(taskId);
        Messagebox.show("Decisión urgente confirmada: " + selectedDecision, "OK", Messagebox.OK, Messagebox.INFORMATION,
            e -> Executions.sendRedirect("/governance/reports/effectiveness.zul"));
    }

    @Command
    public void cancel() {
        String redirect = mockMode ? "/plataforma/workflow/my-tasks.zul?mock=true" : "/governance/reports/effectiveness.zul";
        Executions.sendRedirect(redirect);
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
    }
}
