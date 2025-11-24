package com.codeflowx.govern.viewmodel.compliance;

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
import com.codeflowx.govern.service.models.ModelService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para aprobar evaluación de conformidad final
 * User Task BPMN: approveConformityAssessment
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class ApproveConformityAssessmentViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    
    @WireVariable
    private ModelService modelService;
    
    @WireVariable
    private TaskService taskService;
    
    @WireVariable
    public Environment environment;
    
    @WireVariable("context")
    protected GenericApplicationContext contexto;
    
    @WireVariable("ctxBean")
    protected Context ctxBean;
    
    protected void initDao() {
        // Ya no es necesario inicializar BusinessService manualmente
        // El Service se inyecta automáticamente mediante @WireVariable
    }
    }
    
    @Override
    public void setBeans(Object bean) {}

    private String taskId;
    private Long projectId;
    private String projectName;
    private Double overallScore = 0.0;
    private String reportUrl = "";
    private String approvalComments = "";
    
    @Init
    public void init(@QueryParam("taskId") String taskId) {
        this.taskId = taskId;
        loadTaskData();
    }
    
    private void loadTaskData() {
        try {
            if (taskService != null && taskId != null) {
                log.info("Loading approval data for taskId: {}", taskId);
            }
        } catch (Exception e) {
            log.error("Error loading task data", e);
        }
    }

    @Command
    @NotifyChange("*")
    public void approve() {
        log.info("User decision: Approve - taskId: {}", taskId);
        
        try {
            if (taskService != null && taskId != null) {
                java.util.Map<String, Object> variables = new java.util.HashMap<>();
                variables.put("approved", true);
                variables.put("approvalComments", approvalComments);
                variables.put("approvedBy", ctxBean.getUser().getUsuname());
                variables.put("approvalDate", System.currentTimeMillis());
                
                taskService.complete(taskId, variables);
                
                Messagebox.show("Conformity assessment APPROVED - Project marked as conformity assessed", 
                              "Success", Messagebox.OK, Messagebox.INFORMATION);
            }
        } catch (Exception e) {
            log.error("Error completing task", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("*")
    public void reject() {
        log.info("User decision: Reject - taskId: {}", taskId);
        
        Messagebox.show("Are you sure you want to REJECT this conformity assessment?",
                       "Confirm Rejection", 
                       Messagebox.YES | Messagebox.NO, 
                       Messagebox.QUESTION,
                       event -> {
                           if (event.getName().equals("onYes")) {
                               try {
                                   if (taskService != null && taskId != null) {
                                       java.util.Map<String, Object> variables = new java.util.HashMap<>();
                                       variables.put("approved", false);
                                       variables.put("approvalComments", approvalComments);
                                       variables.put("rejectedBy", ctxBean.getUser().getUsuname());
                                       
                                       taskService.complete(taskId, variables);
                                       
                                       Messagebox.show("Conformity assessment REJECTED", 
                                                     "Info", Messagebox.OK, Messagebox.INFORMATION);
                                   }
                               } catch (Exception e) {
                                   log.error("Error completing task", e);
                                   Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
                               }
                           }
                       });
    }
}

