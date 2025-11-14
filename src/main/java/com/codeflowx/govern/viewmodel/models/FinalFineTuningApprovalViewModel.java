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
 * ViewModel para user task: Final Confirmation (Senior Approval)
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class FinalFineTuningApprovalViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;

    @WireVariable
    private TaskService taskService;

    private String taskId;
    private String justificationSummary;
    private String comparisonSummary;
    private boolean adapterEvaluated;
    private boolean costAcknowledged;
    private boolean co2Acknowledged;
    private String reviewerNotes;

    @Init
    public void init(@QueryParam("taskId") String taskId) {
        this.taskId = taskId;
    }

    @Command
    public void approveFineTuning() {
        if (!canApprove()) {
            Messagebox.show("Debes confirmar todas las casillas antes de aprobar.", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        completeWithDecision("APPROVE_FINETUNING");
    }

    @Command
    public void switchToAdapter() {
        completeWithDecision("SWITCH_ADAPTER");
    }

    @Command
    public void rejectFineTuning() {
        completeWithDecision("REJECT");
    }

    private boolean canApprove() {
        return adapterEvaluated && costAcknowledged && co2Acknowledged;
    }

    private void completeWithDecision(String decision) {
        if (taskId == null) {
            Messagebox.show("Task ID no disponible.", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        Map<String, Object> variables = new HashMap<>();
        variables.put("finalDecision", decision);
        variables.put("seniorNotes", reviewerNotes);
        variables.put("adapterEvaluated", adapterEvaluated);
        variables.put("costAcknowledged", costAcknowledged);
        variables.put("co2Acknowledged", co2Acknowledged);
        completeTask(variables);
        Messagebox.show("Decisión registrada: " + decision, "Información", Messagebox.OK, Messagebox.INFORMATION);
    }

    private void completeTask(Map<String, Object> variables) {
        if (taskService != null) {
            taskService.complete(taskId, variables);
        } else {
            log.warn("TaskService no disponible, completando FinalFineTuningApproval en MOCK.");
        }
    }
}

