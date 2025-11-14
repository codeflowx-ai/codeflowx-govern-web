package com.codeflowx.govern.workflow.viewmodels;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.MasterPage;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class CorrectiveActionAssessmentViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;

    private static final List<String> SEVERITY_OPTIONS = Arrays.asList("CRITICAL", "MAJOR", "MINOR");

    @WireVariable
    private BusinessService businessService;

    @WireVariable
    public Environment environment;

    @WireVariable("context")
    protected GenericApplicationContext contexto;

    @WireVariable("ctxBean")
    protected Context ctxBean;

    @WireVariable("APPLICATION_DS")
    protected DataSource ds;

    @WireVariable
    private TaskService taskService;

    @WireVariable
    private RuntimeService runtimeService;

    private String taskId;
    private String processInstanceId;

    private Long nonConformityId;
    private String nonConformityClause;
    private String description;
    private String severity = "MAJOR";
    private Date detectionDate = new Date();

    protected void initDao() {
        if (businessService == null && environment != null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }

    @Override
    public void setBeans(Object bean) {
        // No-op
    }

    @Init
    public void initView(@QueryParam("taskId") String taskId) {
        this.taskId = taskId;
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        loadTaskContext();
    }

    private void loadTaskContext() {
        try {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null) {
                Messagebox.show("Tarea no disponible", "Error", Messagebox.OK, Messagebox.ERROR);
                Executions.sendRedirect("/plataforma/workflow/task-inbox.zul");
                return;
            }
            this.processInstanceId = task.getProcessInstanceId();
            Object clause = runtimeService.getVariable(processInstanceId, "nonConformityClause");
            if (clause instanceof String clauseStr) {
                this.nonConformityClause = clauseStr;
            }
            Object ncId = runtimeService.getVariable(processInstanceId, "nonConformityId");
            if (ncId instanceof Number num) {
                this.nonConformityId = num.longValue();
            }
        } catch (Exception e) {
            log.error("Error cargando datos de la no conformidad", e);
        }
    }

    @Command
    @NotifyChange("*")
    public void doSubmit() {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("nonConformityId", nonConformityId);
            variables.put("nonConformityClause", nonConformityClause);
            variables.put("nonConformityDescription", description);
            variables.put("nonConformitySeverity", severity);
            variables.put("nonConformityDetectionDate", detectionDate);
            variables.put("assessedBy", ctxBean.getUser().getUsuname());
            taskService.complete(taskId, variables);
            Executions.sendRedirect("/plataforma/workflow/task-inbox.zul");
        } catch (Exception e) {
            log.error("Error registrando evaluación", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    public void doCancel() {
        Executions.sendRedirect("/plataforma/workflow/task-inbox.zul");
    }

    public List<String> getSeverityOptions() {
        return SEVERITY_OPTIONS;
    }
}


