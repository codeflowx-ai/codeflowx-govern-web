package com.codeflowx.govern.viewmodel.compliance;
import com.codeflowx.framework.zkoss.BaseFront;

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
import com.codeflowx.govern.service.models.ModelService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para revisar gaps de QMS (Quality Management System)
 * User Task BPMN: reviewQmsGaps
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class ReviewQmsGapsViewModel extends BaseFront<ReviewQmsGapsViewModel>{

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
    public void setBeans(Object bean) {
        // Not needed
    }

    // Task info
    private String taskId;
    private Long projectId;
    private String projectName;
    
    // QMS info
    private Double qmsScore = 0.0;
    private List<String> qmsGaps = new ArrayList<>();
    private String decision = "RETRY"; // RETRY o CANCEL
    
    @Init
    public void init(@QueryParam("taskId") String taskId) {
        this.taskId = taskId;
        loadTaskData();
    }
    
    private void loadTaskData() {
        try {
            if (taskService != null && taskId != null) {
                // Cargar variables del proceso
                // qmsGaps, qmsScore, projectId, etc.
                log.info("Loading task data for taskId: {}", taskId);
            }
        } catch (Exception e) {
            log.error("Error loading task data", e);
        }
    }

    @Command
    @NotifyChange("*")
    public void fixAndRetry() {
        log.info("User decision: Fix and Retry - taskId: {}", taskId);
        
        try {
            if (taskService != null && taskId != null) {
                // Completar tarea con decision RETRY
                java.util.Map<String, Object> variables = new java.util.HashMap<>();
                variables.put("qmsGapsDecision", "RETRY");
                
                taskService.complete(taskId, variables);
                
                Messagebox.show("QMS gaps will be reviewed again", "Info", Messagebox.OK, Messagebox.INFORMATION);
            }
        } catch (Exception e) {
            log.error("Error completing task", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("*")
    public void cancelAssessment() {
        log.info("User decision: Cancel Assessment - taskId: {}", taskId);
        
        try {
            if (taskService != null && taskId != null) {
                // Completar tarea con decision CANCEL
                java.util.Map<String, Object> variables = new java.util.HashMap<>();
                variables.put("qmsGapsDecision", "CANCEL");
                
                taskService.complete(taskId, variables);
                
                Messagebox.show("Conformity assessment cancelled", "Info", Messagebox.OK, Messagebox.INFORMATION);
            }
        } catch (Exception e) {
            log.error("Error completing task", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
}

