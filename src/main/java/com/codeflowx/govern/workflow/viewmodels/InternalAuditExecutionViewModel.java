package com.codeflowx.govern.workflow.viewmodels;

import java.util.ArrayList;
import java.util.Calendar;
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
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class InternalAuditExecutionViewModel extends MasterPage {

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
    private List<AuditItem> items = new ArrayList<>();

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
        loadChecklist();
    }

    private void loadChecklist() {
        try {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null) {
                Messagebox.show("Tarea no disponible", "Error", Messagebox.OK, Messagebox.ERROR);
                Executions.sendRedirect("/plataforma/workflow/task-inbox.zul");
                return;
            }
            this.processInstanceId = task.getProcessInstanceId();
            List<Map<String, Object>> checklist = (List<Map<String, Object>>) runtimeService.getVariable(processInstanceId, "auditChecklist");
            if (checklist == null) {
                checklist = List.of();
            }
            for (Map<String, Object> entry : checklist) {
                AuditItem item = new AuditItem();
                item.setClause((String) entry.get("clause"));
                item.setTitle((String) entry.get("title"));
                item.setDescription((String) entry.get("description"));
                item.setCompliant(true);
                item.setSeverity("MINOR");
                item.setDueDate(defaultDueDate());
                items.add(item);
            }
        } catch (Exception e) {
            log.error("Error cargando checklist de auditoría", e);
            Messagebox.show("Error cargando checklist", "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("items")
    public void markNonCompliant(AuditItem item) {
        item.setCompliant(false);
    }

    @Command
    public void doCancel() {
        Executions.sendRedirect("/plataforma/workflow/task-inbox.zul");
    }

    @Command
    public void doSubmit() {
        try {
            List<Map<String, Object>> responses = new ArrayList<>();
            for (AuditItem item : items) {
                Map<String, Object> response = new HashMap<>();
                response.put("clause", item.getClause());
                response.put("description", item.getDescription());
                response.put("compliant", item.isCompliant());
                response.put("severity", item.getSeverity());
                response.put("evidence", item.getEvidence());
                response.put("owner", item.getOwner());
                response.put("dueDate", item.getDueDate());
                responses.add(response);
            }

            Map<String, Object> variables = new HashMap<>();
            variables.put("auditResponses", responses);
            variables.put("auditConductedBy", ctxBean.getUser().getUsuname());
            variables.put("auditConductedAt", new Date());

            taskService.complete(taskId, variables);
            Messagebox.show("Auditoría registrada correctamente", "Éxito",
                    Messagebox.OK, Messagebox.INFORMATION,
                    event -> Executions.sendRedirect("/plataforma/workflow/task-inbox.zul"));
        } catch (Exception e) {
            log.error("Error registrando auditoría", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    private Date defaultDueDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        return calendar.getTime();
    }

    @Data
    public static class AuditItem {
        private String clause;
        private String title;
        private String description;
        private boolean compliant;
        private String severity;
        private String evidence;
        private String owner;
        private Date dueDate;
    }
}


