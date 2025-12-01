package com.codeflowx.govern.viewmodel.euregistration;
import com.codeflowx.framework.zkoss.BaseFront;

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
 * ViewModel para formulario de registro EU Database
 * User Task BPMN: fillRegistrationForm (Art. 49, Anexo VIII)
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class EuRegistrationFormViewModel extends BaseFront<EuRegistrationFormViewModel>{

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
    private String registrationType = "";
    
    // Form fields (simplified - Anexo VIII has 13 fields for Section A)
    private String providerName = "";
    private String providerAddress = "";
    private String systemName = "";
    private String intendedPurpose = "";
    private String highriskCategory = "";
    private String conformityBody = "";
    
    @Init
    public void init(@QueryParam("taskId") String taskId) {
        this.taskId = taskId;
        loadTaskData();
    }
    
    private void loadTaskData() {
        try {
            if (taskService != null && taskId != null) {
                log.info("Loading EU registration form for taskId: {}", taskId);
            }
        } catch (Exception e) {
            log.error("Error loading task data", e);
        }
    }

    @Command
    @NotifyChange("*")
    public void submitForm() {
        log.info("Submitting EU registration form - taskId: {}", taskId);
        
        if (providerName.trim().isEmpty() || systemName.trim().isEmpty()) {
            Messagebox.show("Please complete all required fields", 
                          "Validation", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        
        try {
            if (taskService != null && taskId != null) {
                java.util.Map<String, Object> variables = new java.util.HashMap<>();
                variables.put("providerName", providerName);
                variables.put("providerAddress", providerAddress);
                variables.put("systemName", systemName);
                variables.put("intendedPurpose", intendedPurpose);
                variables.put("highriskCategory", highriskCategory);
                variables.put("conformityBody", conformityBody);
                variables.put("filledBy", ctxBean.getUser().getUsuname());
                
                taskService.complete(taskId, variables);
                
                Messagebox.show("Registration form submitted for validation", "Success", Messagebox.OK, Messagebox.INFORMATION);
            }
        } catch (Exception e) {
            log.error("Error completing task", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
}

