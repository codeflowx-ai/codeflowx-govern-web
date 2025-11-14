package com.codeflowx.govern.viewmodel.fria;

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
 * ViewModel para aprobación final del deployer tras FRIA
 * User Task BPMN: deployerApproval (Art. 27)
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class DeployerApprovalViewModel extends MasterPage {

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
    private String friaDocumentUrl = "";
    private String deploymentDecisionNotes = "";
    
    @Init
    public void init(@QueryParam("taskId") String taskId) {
        this.taskId = taskId;
        loadTaskData();
    }
    
    private void loadTaskData() {
        try {
            if (taskService != null && taskId != null) {
                log.info("Loading deployer approval data for taskId: {}", taskId);
            }
        } catch (Exception e) {
            log.error("Error loading task data", e);
        }
    }

    @Command
    @NotifyChange("*")
    public void proceedWithDeployment() {
        log.info("Deployer decision: PROCEED - taskId: {}", taskId);
        
        try {
            if (taskService != null && taskId != null) {
                java.util.Map<String, Object> variables = new java.util.HashMap<>();
                variables.put("proceedWithDeployment", true);
                variables.put("deploymentDecisionNotes", deploymentDecisionNotes);
                variables.put("approvedByDeployer", ctxBean.getUser().getUsuname());
                
                taskService.complete(taskId, variables);
                
                Messagebox.show("FRIA approved - Proceeding with deployment registration", 
                              "Success", Messagebox.OK, Messagebox.INFORMATION);
            }
        } catch (Exception e) {
            log.error("Error completing task", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("*")
    public void cancelDeployment() {
        log.info("Deployer decision: CANCEL - taskId: {}", taskId);
        
        Messagebox.show("Are you sure you want to CANCEL deployment?", "Confirm", 
                       Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                       event -> {
                           if (event.getName().equals("onYes")) {
                               try {
                                   if (taskService != null && taskId != null) {
                                       java.util.Map<String, Object> variables = new java.util.HashMap<>();
                                       variables.put("proceedWithDeployment", false);
                                       variables.put("deploymentDecisionNotes", deploymentDecisionNotes);
                                       variables.put("cancelledByDeployer", ctxBean.getUser().getUsuname());
                                       
                                       taskService.complete(taskId, variables);
                                       
                                       Messagebox.show("Deployment CANCELLED", "Info", Messagebox.OK, Messagebox.INFORMATION);
                                   }
                               } catch (Exception e) {
                                   log.error("Error completing task", e);
                                   Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
                               }
                           }
                       });
    }
}

