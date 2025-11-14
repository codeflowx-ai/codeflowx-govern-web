package com.codeflowx.govern.viewmodel.incident;

import javax.sql.DataSource;

import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.MasterPage;
import org.flowable.engine.TaskService;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para Root Cause Analysis de incidente
 * User Task BPMN: rootCauseAnalysis (Art. 20)
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class RootCauseAnalysisViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    
    @WireVariable
    private BusinessService businessService;
    
    @WireVariable
    private TaskService taskService;
    
    @WireVariable
    public Environment environment;
    
    @WireVariable("context")
    protected GenericApplicationContext contexto;
    
    @WireVariable("ctxBean")
    protected Context ctxBean;
    
    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }
    
    @Override
    public void setBeans(Object bean) {}

    private String taskId;
    private String incidentTitle = "";
    
    // RCA Fields
    private String whatHappened = "";
    private String whyHappened = "";
    private String contributingFactors = "";
    private String timeline = "";
    private String automatedRcaReport = "";
    
    @Init
    public void init(@QueryParam("taskId") String taskId) {
        this.taskId = taskId;
        loadTaskData();
    }
    
    private void loadTaskData() {
        try {
            if (taskService != null && taskId != null) {
                log.info("Loading RCA data for taskId: {}", taskId);
                // Cargar automatedRcaReport si existe del delegate
            }
        } catch (Exception e) {
            log.error("Error loading task data", e);
        }
    }

    @Command
    @NotifyChange("*")
    public void submitRCA() {
        log.info("Submitting RCA - taskId: {}", taskId);
        
        if (whatHappened.trim().isEmpty() || whyHappened.trim().isEmpty()) {
            Messagebox.show("Please complete What Happened and Why Happened fields", 
                          "Validation", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        
        try {
            if (taskService != null && taskId != null) {
                java.util.Map<String, Object> variables = new java.util.HashMap<>();
                variables.put("whatHappened", whatHappened);
                variables.put("whyHappened", whyHappened);
                variables.put("contributingFactors", contributingFactors);
                variables.put("timeline", timeline);
                variables.put("rcaCompletedBy", ctxBean.getUser().getUsuname());
                variables.put("rcaCompletionDate", System.currentTimeMillis());
                
                taskService.complete(taskId, variables);
                
                Messagebox.show("Root Cause Analysis completed", "Success", Messagebox.OK, Messagebox.INFORMATION);
            }
        } catch (Exception e) {
            log.error("Error completing task", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
}

