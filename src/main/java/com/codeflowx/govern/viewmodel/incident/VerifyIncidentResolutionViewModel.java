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
import com.codeflowx.govern.service.models.ModelService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para verificar resolución de incidente
 * User Task BPMN: verifyIncidentResolution
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class VerifyIncidentResolutionViewModel extends MasterPage {

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
    private String incidentTitle = "";
    private Integer correctiveActionsCount = 0;
    
    // Verification fields
    private Boolean issueResolved = false;
    private Boolean noRecurrence = false;
    private String verificationNotes = "";
    private String resolutionEvidence = "";
    
    @Init
    public void init(@QueryParam("taskId") String taskId) {
        this.taskId = taskId;
        loadTaskData();
    }
    
    private void loadTaskData() {
        try {
            if (taskService != null && taskId != null) {
                log.info("Loading verification data for taskId: {}", taskId);
            }
        } catch (Exception e) {
            log.error("Error loading task data", e);
        }
    }

    @Command
    @NotifyChange("*")
    public void confirmResolution() {
        log.info("User decision: Confirm Resolution - taskId: {}", taskId);
        
        if (!issueResolved || !noRecurrence) {
            Messagebox.show("Please confirm both checkboxes to verify resolution", 
                          "Validation", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        
        try {
            if (taskService != null && taskId != null) {
                java.util.Map<String, Object> variables = new java.util.HashMap<>();
                variables.put("incidentResolved", true);
                variables.put("verificationNotes", verificationNotes);
                variables.put("resolutionEvidence", resolutionEvidence);
                variables.put("verifiedBy", ctxBean.getUser().getUsuname());
                variables.put("verificationDate", System.currentTimeMillis());
                
                taskService.complete(taskId, variables);
                
                Messagebox.show("Incident marked as RESOLVED and will be closed", 
                              "Success", Messagebox.OK, Messagebox.INFORMATION);
            }
        } catch (Exception e) {
            log.error("Error completing task", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("*")
    public void escalateIncident() {
        log.info("User decision: Escalate - taskId: {}", taskId);
        
        Messagebox.show("This will escalate the incident and create a new corrective action cycle. Continue?",
                       "Confirm Escalation", 
                       Messagebox.YES | Messagebox.NO, 
                       Messagebox.QUESTION,
                       event -> {
                           if (event.getName().equals("onYes")) {
                               try {
                                   if (taskService != null && taskId != null) {
                                       java.util.Map<String, Object> variables = new java.util.HashMap<>();
                                       variables.put("incidentResolved", false);
                                       variables.put("verificationNotes", verificationNotes);
                                       variables.put("escalatedBy", ctxBean.getUser().getUsuname());
                                       
                                       taskService.complete(taskId, variables);
                                       
                                       Messagebox.show("Incident ESCALATED - new corrective actions required", 
                                                     "Info", Messagebox.OK, Messagebox.WARNING);
                                   }
                               } catch (Exception e) {
                                   log.error("Error completing task", e);
                                   Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
                               }
                           }
                       });
    }
}

