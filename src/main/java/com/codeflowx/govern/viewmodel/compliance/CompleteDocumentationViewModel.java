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
 * ViewModel para completar documentación técnica (Anexo IV)
 * User Task BPMN: completeDocumentation
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class CompleteDocumentationViewModel extends BaseFront<CompleteDocumentationViewModel>{

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
    
    // Documentation info
    private Double docScore = 0.0;
    private List<String> docGaps = new ArrayList<>();
    private String decision = "RETRY";
    private String notes = "";
    
    @Init
    public void init(@QueryParam("taskId") String taskId) {
        this.taskId = taskId;
        loadTaskData();
    }
    
    private void loadTaskData() {
        try {
            if (taskService != null && taskId != null) {
                log.info("Loading task data for taskId: {}", taskId);
                // Cargar docGaps, docScore del proceso
            }
        } catch (Exception e) {
            log.error("Error loading task data", e);
        }
    }

    @Command
    @NotifyChange("*")
    public void retryWithUpdates() {
        log.info("User decision: Retry with updates - taskId: {}", taskId);
        
        try {
            if (taskService != null && taskId != null) {
                java.util.Map<String, Object> variables = new java.util.HashMap<>();
                variables.put("docCompletionDecision", "RETRY");
                variables.put("docCompletionNotes", notes);
                
                taskService.complete(taskId, variables);
                
                Messagebox.show("Documentation will be reviewed again", "Info", Messagebox.OK, Messagebox.INFORMATION);
            }
        } catch (Exception e) {
            log.error("Error completing task", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("*")
    public void cancelAssessment() {
        log.info("User decision: Cancel - taskId: {}", taskId);
        
        try {
            if (taskService != null && taskId != null) {
                java.util.Map<String, Object> variables = new java.util.HashMap<>();
                variables.put("docCompletionDecision", "CANCEL");
                
                taskService.complete(taskId, variables);
                
                Messagebox.show("Assessment cancelled", "Info", Messagebox.OK, Messagebox.INFORMATION);
            }
        } catch (Exception e) {
            log.error("Error completing task", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
}

