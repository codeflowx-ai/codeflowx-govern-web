package com.codeflowx.govern.workflow.viewmodels;
import com.codeflowx.framework.zkoss.BaseFront;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.suinsit.Context;
import org.flowable.engine.TaskService;
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
public class InternalAuditSchedulingViewModel extends BaseFront<InternalAuditSchedulingViewModel>{

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

    private String taskId;
    private Date auditDate = defaultDate();
    private String auditScope = "";
    private String auditor = "";
    private Long projectId;

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
    public void initView(@QueryParam("taskId") String taskId,
            @QueryParam("projectId") Long projectIdParam) {
        this.taskId = taskId;
        this.projectId = projectIdParam;
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
    }

    @Command
    @NotifyChange("*")
    public void doSubmit() {
        if (auditScope == null || auditScope.isBlank()) {
            Messagebox.show("Debe definir el alcance de la auditoría", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        if (auditor == null || auditor.isBlank()) {
            Messagebox.show("Debe asignar un auditor responsable", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        Map<String, Object> variables = new HashMap<>();
        variables.put("auditScheduledDate", auditDate);
        variables.put("auditScope", auditScope);
        variables.put("auditAuditor", auditor);
        variables.put("projectId", projectId);
        variables.put("scheduledBy", ctxBean.getUser().getUsuname());
        taskService.complete(taskId, variables);
        Executions.sendRedirect("/plataforma/workflow/task-inbox.zul");
    }

    @Command
    public void doCancel() {
        Executions.sendRedirect("/plataforma/workflow/task-inbox.zul");
    }

    private Date defaultDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 14);
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}


