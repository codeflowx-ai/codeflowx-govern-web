package com.codeflowx.govern.viewmodel.incident;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para definir acciones correctivas
 * User Task BPMN: defineCorrectiveActions (Art. 20)
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class DefineCorrectiveActionsViewModel extends MasterPage {

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
    private String rootCause = "";
    
    // Corrective Actions
    private List<CorrectiveAction> correctiveActions = new ArrayList<>();
    private String newActionDescription = "";
    private String newActionType = "SHORT_TERM";
    private String newActionResponsible = "";
    
    @Init
    public void init(@QueryParam("taskId") String taskId) {
        this.taskId = taskId;
        loadTaskData();
    }
    
    private void loadTaskData() {
        try {
            if (taskService != null && taskId != null) {
                log.info("Loading corrective actions for taskId: {}", taskId);
            }
        } catch (Exception e) {
            log.error("Error loading task data", e);
        }
    }

    @Command
    @NotifyChange("*")
    public void addAction() {
        if (newActionDescription.trim().isEmpty()) {
            Messagebox.show("Please enter action description", "Validation", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        
        CorrectiveAction action = new CorrectiveAction();
        action.setDescription(newActionDescription);
        action.setType(newActionType);
        action.setResponsible(newActionResponsible);
        
        correctiveActions.add(action);
        
        // Clear form
        newActionDescription = "";
        newActionResponsible = "";
    }

    @Command
    @NotifyChange("*")
    public void removeAction(CorrectiveAction action) {
        correctiveActions.remove(action);
    }

    @Command
    @NotifyChange("*")
    public void submitActions() {
        log.info("Submitting corrective actions - taskId: {}, count: {}", taskId, correctiveActions.size());
        
        if (correctiveActions.isEmpty()) {
            Messagebox.show("Please define at least one corrective action", 
                          "Validation", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        
        try {
            if (taskService != null && taskId != null) {
                // Convertir a List<String> para BPMN
                List<String> actionDescriptions = new ArrayList<>();
                for (CorrectiveAction action : correctiveActions) {
                    actionDescriptions.add(action.getType() + ": " + action.getDescription() + 
                                         " (Responsible: " + action.getResponsible() + ")");
                }
                
                Map<String, Object> variables = new HashMap<>();
                variables.put("correctiveActions", actionDescriptions);
                variables.put("correctiveActionsCount", correctiveActions.size());
                variables.put("definedBy", ctxBean.getUser().getUsuname());
                
                taskService.complete(taskId, variables);
                
                Messagebox.show("Corrective actions defined successfully", "Success", Messagebox.OK, Messagebox.INFORMATION);
            }
        } catch (Exception e) {
            log.error("Error completing task", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Data
    public static class CorrectiveAction {
        private String description;
        private String type; // IMMEDIATE, SHORT_TERM, LONG_TERM
        private String responsible;
    }
}

