package com.codeflowx.govern.viewmodel.incident;
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
 * ViewModel para documentar detalles de incidente grave
 * User Task BPMN: documentIncidentDetails (Art. 73)
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class DocumentIncidentDetailsViewModel extends BaseFront<DocumentIncidentDetailsViewModel>{

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

    @Override
    public void setBeans(Object bean) {}

    private String taskId;
    private String severity = "";
    private Integer affectedUsersCount = 0;

    // Formulario documentación
    private String incidentTitle = "";
    private String detailedDescription = "";
    private String impactAssessment = "";
    private String initialActions = "";
    private String potentialRisks = "";

    @Init
    public void init(@QueryParam("taskId") String taskId) {
        this.taskId = taskId;
        loadTaskData();
    }

    private void loadTaskData() {
        try {
            if (taskService != null && taskId != null) {
                log.info("Loading incident details for taskId: {}", taskId);
            }
        } catch (Exception e) {
            log.error("Error loading task data", e);
        }
    }

    @Command
    @NotifyChange("*")
    public void submitDocumentation() {
        log.info("Submitting incident documentation - taskId: {}", taskId);

        if (incidentTitle.trim().isEmpty() || detailedDescription.trim().isEmpty()) {
            Messagebox.show("Please fill all required fields", "Validation", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        try {
            if (taskService != null && taskId != null) {
                java.util.Map<String, Object> variables = new java.util.HashMap<>();
                variables.put("incidentTitle", incidentTitle);
                variables.put("detailedDescription", detailedDescription);
                variables.put("impactAssessment", impactAssessment);
                variables.put("initialActions", initialActions);
                variables.put("potentialRisks", potentialRisks);
                variables.put("documentedBy", ctxBean.getUser().getUsuname());
                variables.put("documentationDate", System.currentTimeMillis());

                taskService.complete(taskId, variables);

                Messagebox.show("Incident documented successfully", "Success", Messagebox.OK, Messagebox.INFORMATION);
            }
        } catch (Exception e) {
            log.error("Error completing task", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
}
