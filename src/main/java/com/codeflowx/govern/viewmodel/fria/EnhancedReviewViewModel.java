package com.codeflowx.govern.viewmodel.fria;

import java.util.ArrayList;
import java.util.List;

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
 * ViewModel para enhanced review de FRIA de alto impacto
 * User Task BPMN: enhancedReview (Art. 27)
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class EnhancedReviewViewModel extends MasterPage {

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
    private Long projectId;
    private String impactSeverity = "";
    private List<String> charterArticles = new ArrayList<>();
    private String reviewDecision = "APPROVE";
    private String reviewComments = "";
    
    @Init
    public void init(@QueryParam("taskId") String taskId) {
        this.taskId = taskId;
        loadTaskData();
    }
    
    private void loadTaskData() {
        try {
            if (taskService != null && taskId != null) {
                log.info("Loading enhanced review data for taskId: {}", taskId);
            }
        } catch (Exception e) {
            log.error("Error loading task data", e);
        }
    }

    @Command
    @NotifyChange("*")
    public void approve() {
        log.info("Enhanced review decision: APPROVE - taskId: {}", taskId);
        completeTask("APPROVE");
    }

    @Command
    @NotifyChange("*")
    public void reject() {
        log.info("Enhanced review decision: REJECT - taskId: {}", taskId);
        Messagebox.show("This will BLOCK deployment. Continue?", "Confirm", 
                       Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                       event -> {
                           if (event.getName().equals("onYes")) {
                               completeTask("REJECT");
                           }
                       });
    }

    @Command
    @NotifyChange("*")
    public void modify() {
        log.info("Enhanced review decision: MODIFY - taskId: {}", taskId);
        completeTask("MODIFY");
    }
    
    private void completeTask(String decision) {
        try {
            if (taskService != null && taskId != null) {
                java.util.Map<String, Object> variables = new java.util.HashMap<>();
                variables.put("reviewDecision", decision);
                variables.put("reviewComments", reviewComments);
                variables.put("reviewedBy", ctxBean.getUser().getUsuname());
                
                taskService.complete(taskId, variables);
                
                Messagebox.show("Enhanced review decision: " + decision, "Success", Messagebox.OK, Messagebox.INFORMATION);
            }
        } catch (Exception e) {
            log.error("Error completing task", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
}

