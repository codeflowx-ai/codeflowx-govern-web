package com.codeflowx.govern.workflow.viewmodels;

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
 * Plan de mitigación de sesgos
 * Proceso: 08_BIAS_DETECTION
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class BiasMitigationPlanViewModel extends MasterPage {

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
    private String decision;
    private String notes;
    
    private boolean mockMode = false;

    // ========== Inicialización ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        log.info("🚀 Inicializando BiasMitigationPlanViewModel");
        
        Map<String, String[]> params = Executions.getCurrent().getParameterMap();
        
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
    
    private void loadMockData() {
        this.taskId = "mock-bias-mitigation-001";
        this.processInstanceId = "mock-bias-process-001";
        log.info("🎭 Mock data loaded for Bias Mitigation Plan");
    }

    private void loadTaskData() {
        try {
            org.flowable.task.api.Task task = taskService.createTaskQuery()
                .taskId(taskId).singleResult();
            this.processInstanceId = task.getProcessInstanceId();
            
            Map<String, Object> variables = runtimeService.getVariables(processInstanceId);
            // TODO: Cargar variables específicas
            
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
                log.info("🎭 Mock mode: Simulando doSubmit - decision={}", decision);
                logActivity("MOCK_BIAS_MITIGATION", "BiasMitigation", null, "Simulación: " + decision);
                Messagebox.show("✅ [DEMO] Decisión registrada: " + decision, "Demo Mode", 
                    Messagebox.OK, Messagebox.INFORMATION,
                    event -> Executions.getCurrent().sendRedirect("/plataforma/workflow/my-tasks.zul?mock=true"));
                return;
            }
            
            Map<String, Object> taskVariables = Map.of(
                "decision", decision,
                "notes", notes
            );
            
            taskService.complete(taskId, taskVariables);
            
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
    }
}
