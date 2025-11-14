package com.codeflowx.govern.workflow.viewmodels;

import java.time.LocalDate;
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
public class ManagementReviewActionsViewModel extends MasterPage {

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

    private List<ActionItem> actions = new ArrayList<>();
    private String newActionDescription = "";
    private String newActionOwner = "";
    private LocalDate newActionDueDate = LocalDate.now().plusWeeks(4);

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
    }

    @Command
    @NotifyChange({"actions", "newActionDescription", "newActionOwner", "newActionDueDate"})
    public void addAction() {
        if (newActionDescription == null || newActionDescription.isBlank()) {
            Messagebox.show("Debe describir la acción correctiva", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        ActionItem item = new ActionItem();
        item.setDescription(newActionDescription);
        item.setOwner(newActionOwner);
        item.setDueDate(newActionDueDate);
        actions.add(item);

        newActionDescription = "";
        newActionOwner = "";
        newActionDueDate = LocalDate.now().plusWeeks(4);
    }

    @Command
    @NotifyChange("actions")
    public void removeAction(ActionItem item) {
        actions.remove(item);
    }

    @Command
    public void doCancel() {
        Executions.sendRedirect("/plataforma/workflow/task-inbox.zul");
    }

    @Command
    public void doSubmit() {
        if (actions.isEmpty()) {
            Messagebox.show("Debe definir al menos una acción correctiva", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        try {
            List<Map<String, Object>> serialized = new ArrayList<>();
            for (ActionItem action : actions) {
                Map<String, Object> item = new HashMap<>();
                item.put("description", action.getDescription());
                item.put("owner", action.getOwner());
                item.put("dueDate", action.getDueDate());
                serialized.add(item);
            }

            Map<String, Object> variables = new HashMap<>();
            variables.put("actionItems", serialized);
            variables.put("actionItemsCount", serialized.size());
            variables.put("actionPlanOwner", ctxBean.getUser().getUsuname());

            taskService.complete(taskId, variables);
            Messagebox.show("Plan de acciones registrado", "Éxito",
                    Messagebox.OK, Messagebox.INFORMATION,
                    event -> Executions.sendRedirect("/plataforma/workflow/task-inbox.zul"));
        } catch (Exception e) {
            log.error("Error registrando acciones", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Data
    public static class ActionItem {
        private String description;
        private String owner;
        private LocalDate dueDate;
    }
}


