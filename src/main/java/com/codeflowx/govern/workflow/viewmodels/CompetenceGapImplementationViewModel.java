package com.codeflowx.govern.workflow.viewmodels;
import com.codeflowx.framework.zkoss.BaseFront;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.suinsit.Context;
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
public class CompetenceGapImplementationViewModel extends BaseFront<CompetenceGapImplementationViewModel>{

    private static final long serialVersionUID = 1L;

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

    private Date startDate = new Date();
    private Date completionDate;
    private String trainingProvider;
    private String participants;
    private Double preAssessmentScore;
    private Double postAssessmentScore;
    private String executionNotes;

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
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task != null) {
            this.processInstanceId = task.getProcessInstanceId();
        }
    }

    @Command
    @NotifyChange("*")
    public void doSubmit() {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("trainingStartDate", startDate);
            variables.put("trainingCompletionDate", completionDate);
            variables.put("trainingProvider", trainingProvider);
            variables.put("trainingParticipants", participants);
            variables.put("preTrainingAssessment", preAssessmentScore);
            variables.put("postTrainingAssessment", postAssessmentScore);
            variables.put("trainingExecutionNotes", executionNotes);
            variables.put("trainingExecutedBy", ctxBean.getUser().getUsuname());
            taskService.complete(taskId, variables);
            Executions.sendRedirect("/plataforma/workflow/task-inbox.zul");
        } catch (Exception e) {
            log.error("Error registrando ejecución capacitación", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    public void doCancel() {
        Executions.sendRedirect("/plataforma/workflow/task-inbox.zul");
    }
}


