package com.codeflowx.govern.viewmodel.models;

import java.util.HashMap;
import java.util.Map;

import org.enartframework.web.zk.page.MasterPage;
import org.flowable.engine.TaskService;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para user task Review and Approve Adapter
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ReviewAdapterApprovalViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;

    @WireVariable
    private TaskService taskService;

    private String taskId;
    private String comparisonReport;
    private String reviewerComments;

    @Init
    public void init(@QueryParam("taskId") String taskId) {
        this.taskId = taskId;
    }

    @Command
    public void approve() {
        completeWithDecision("APPROVE");
    }

    @Command
    public void requestModification() {
        completeWithDecision("MODIFY");
    }

    @Command
    public void reject() {
        completeWithDecision("REJECT");
    }

    private void completeWithDecision(String decision) {
        if (taskId == null) {
            Messagebox.show("Task ID no disponible.", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        Map<String, Object> variables = new HashMap<>();
        variables.put("approvalDecision", decision);
        variables.put("reviewerComments", reviewerComments);
        variables.put("comparisonReport", comparisonReport);
        completeTask(variables);
        Messagebox.show("Decisión registrada: " + decision, "Información", Messagebox.OK, Messagebox.INFORMATION);
    }

    private void completeTask(Map<String, Object> variables) {
        if (taskService != null) {
            taskService.complete(taskId, variables);
        } else {
            log.warn("TaskService no disponible, completando ReviewAdapterApproval en MOCK.");
        }
    }
}
