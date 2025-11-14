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
 * ViewModel para user task: Justify Why NOT Adapter
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class JustifyFineTuningViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;

    @WireVariable
    private TaskService taskService;

    private String taskId;
    private String justification;
    private boolean co2Concern;
    private boolean performanceCritical;
    private boolean architectureChange;

    @Init
    public void init(@QueryParam("taskId") String taskId) {
        this.taskId = taskId;
    }

    @Command
    @NotifyChange("*")
    public void submitJustification() {
        if (taskId == null) {
            Messagebox.show("Task ID no disponible.", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        if (justification == null || justification.trim().length() < 100) {
            Messagebox.show("La justificación debe tener al menos 100 caracteres.", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        Map<String, Object> variables = new HashMap<>();
        variables.put("justificationText", justification);
        variables.put("co2Concern", co2Concern);
        variables.put("performanceCritical", performanceCritical);
        variables.put("architectureChange", architectureChange);
        completeTask(variables);
        Messagebox.show("Justificación enviada.", "Información", Messagebox.OK, Messagebox.INFORMATION);
    }

    private void completeTask(Map<String, Object> variables) {
        if (taskService != null) {
            taskService.complete(taskId, variables);
        } else {
            log.warn("TaskService no disponible, completando JustifyFineTuning en MOCK.");
        }
    }
}

