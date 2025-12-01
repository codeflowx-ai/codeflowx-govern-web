package com.codeflowx.govern.viewmodel.models;
import com.codeflowx.framework.zkoss.BaseFront;

import java.util.HashMap;
import java.util.Map;

import org.enartframework.web.zk.page.MasterPage;
import org.flowable.engine.TaskService;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para user task: Define Adapter Parameters (adapter-creation-approval-v1)
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class DefineAdapterParametersViewModel extends BaseFront<DefineAdapterParametersViewModel>{

    private static final long serialVersionUID = 1L;

    @WireVariable
    private TaskService taskService;

    @WireVariable
    public Environment environment;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String taskId;
    private Long baseModelId;
    private String adapterName;
    private String adapterType = "ADAPTER_LORA";
    private Integer rank = 16;
    private Integer alpha = 32;
    private String targetModules = "q_proj,v_proj";
    private Double dropout = 0.1;
    private String trainingDataset;
    private String createdBy;
    private String modificationsDocUrl;

    @Init
    public void init(@QueryParam("taskId") String taskId) {
        this.taskId = taskId;
    }

    @Command
    @NotifyChange("*")
    public void submit() {
        if (taskId == null) {
            Messagebox.show("Task ID no disponible.", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("baseModelId", baseModelId);
            variables.put("adapterType", adapterType);
            variables.put("adapterName", adapterName);
            variables.put("adapterRank", rank);
            variables.put("adapterAlpha", alpha);
            variables.put("adapterTargetModules", targetModules);
            variables.put("adapterDropout", dropout);
            variables.put("adapterTrainingDataset", trainingDataset);
            variables.put("createdBy", createdBy);
            variables.put("modificationsDocUrl", modificationsDocUrl);

            Map<String, Object> config = new HashMap<>();
            config.put("adapterName", adapterName);
            config.put("adapterType", adapterType);
            config.put("rank", rank);
            config.put("alpha", alpha);
            config.put("targetModules", targetModules);
            config.put("dropout", dropout);
            config.put("trainingDataset", trainingDataset);
            config.put("modificationsDocUrl", modificationsDocUrl);
            variables.put("adapterConfigJson", objectMapper.writeValueAsString(config));

            completeTask(variables);
            Messagebox.show("Parámetros registrados.", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
        } catch (Exception e) {
            log.error("Error completando tarea", e);
            Messagebox.show("Error registrando parámetros: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    private void completeTask(Map<String, Object> variables) {
        if (taskService != null) {
            taskService.complete(taskId, variables);
        } else {
            log.warn("TaskService no disponible, completando en modo MOCK. vars={}", variables);
        }
    }
}

