package com.codeflowx.govern.workflow.viewmodels;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.flowable.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.framework.zkoss.BaseFront;

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel: Execute Corrective Action
 *
 * BPMN Process: incident-reporting-process
 * User Task: executeCorrectiveAction (MULTI-INSTANCE)
 * Candidate Groups: tech-team
 *
 * Funcionalidad:
 * - Ejecutar una acción correctiva específica (multi-instance)
 * - Registrar progreso y evidencia de ejecución
 * - Marcar acción como completada
 *
 * Input Variables:
 * - correctiveActionIndex (número de instancia)
 * - correctiveActionDescription (desde DefineCorrectiveActions)
 *
 * Output Variables:
 * - actionCompleted (Boolean), actionEvidence, actionNotes
 * - executedBy, executionDate
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class ExecuteCorrectiveActionViewModel extends BaseFront<ExecuteCorrectiveActionViewModel> {

    private static final long serialVersionUID = 1L;

    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }

    // ========== Servicios Flowable ==========
    @WireVariable
    private TaskService taskService;

    @Autowired
    protected IEntityLocal dao;

    @WireVariable
    public Environment environment;

    @WireVariable("context")
    protected GenericApplicationContext contexto;

    @WireVariable("ctxBean")
    protected Context ctxBean;

    @WireVariable("APPLICATION_DS")
    protected DataSource ds;

    protected void initDao() {
        // Inicialización si es necesaria
    }

    // ========== Datos ==========
    private String taskId;
    private String processInstanceId;
    private Integer correctiveActionIndex = 0;
    private String correctiveActionDescription = "";
    private String actionType = "";

    // Formulario ejecución
    private Boolean actionCompleted = false;
    private String actionEvidence = "";
    private String actionNotes = "";

    // Mock mode
    private boolean mockMode = false;

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();

        log.info("🚀 Inicializando ExecuteCorrectiveActionViewModel");

        // Detectar mock mode desde parámetros URL o variable de entorno
        String mockParam = Executions.getCurrent().getParameter("mock");
        if (mockParam != null && ("true".equalsIgnoreCase(mockParam) || "1".equals(mockParam))) {
            mockMode = true;
        } else if (System.getenv("MOCK_MODE") != null) {
            mockMode = Boolean.parseBoolean(System.getenv("MOCK_MODE").toString());
        }

        if (mockMode) {
            this.mockMode = true;
            loadMockData();
            log.info("🎭 Mock mode activado");
            return;
        }

        try {
            // Obtener taskId de la sesión (pasado por TaskInboxViewModel)
            taskId = (String) Sessions.getCurrent().getAttribute("taskId");
            processInstanceId = (String) Sessions.getCurrent().getAttribute("processInstanceId");

            if (taskId == null) {
                log.error("❌ No se encontró taskId en sesión");
                Messagebox.show("Error: No se puede cargar la tarea", "Error",
                                Messagebox.OK, Messagebox.ERROR);
                return;
            }

            log.info("📋 Cargando tarea: {}", taskId);

            // Cargar variables del proceso
            Map<String, Object> variables = taskService.getVariables(taskId);

            // Obtener información de la acción correctiva desde variables de proceso
            // En multi-instance, cada instancia tiene su propio índice
            Object indexObj = variables.get("loopCounter");
            if (indexObj != null) {
                correctiveActionIndex = indexObj instanceof Integer ? (Integer) indexObj :
                                      indexObj instanceof Long ? ((Long) indexObj).intValue() : 0;
            }

            // Obtener lista de acciones correctivas y seleccionar la correspondiente
            Object actionsObj = variables.get("correctiveActions");
            if (actionsObj instanceof java.util.List) {
                @SuppressWarnings("unchecked")
                java.util.List<String> actions = (java.util.List<String>) actionsObj;
                if (correctiveActionIndex >= 0 && correctiveActionIndex < actions.size()) {
                    String actionStr = actions.get(correctiveActionIndex);
                    correctiveActionDescription = actionStr;
                    // Extraer tipo si está en el formato "TYPE: description"
                    if (actionStr.contains(":")) {
                        String[] parts = actionStr.split(":", 2);
                        actionType = parts[0].trim();
                        correctiveActionDescription = parts.length > 1 ? parts[1].trim() : actionStr;
                    }
                }
            }

            log.info("✅ Variables cargadas - Action Index: {}, Description: {}",
                    correctiveActionIndex, correctiveActionDescription);

        } catch (Exception e) {
            log.error("❌ Error inicializando: {}", e.getMessage(), e);
            Messagebox.show("Error cargando datos: " + e.getMessage(), "Error",
                            Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Completar acción correctiva
     */
    @Command
    @NotifyChange("*")
    public void completeAction() {
        log.info("Completing corrective action - taskId: {}, index: {}", taskId, correctiveActionIndex);

        if (!actionCompleted) {
            Messagebox.show("Please mark the action as completed",
                          "Validation", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        if (actionEvidence.trim().isEmpty()) {
            Messagebox.show("Please provide evidence of action execution",
                          "Validation", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        try {
            if (mockMode) {
                log.info("🎭 Mock mode: Simulando completeAction");
                Messagebox.show("✅ [DEMO] Corrective action completed successfully",
                    "Demo Mode", Messagebox.OK, Messagebox.INFORMATION,
                    event -> Executions.getCurrent().sendRedirect("/console/bpmn/task-inbox.zul?mock=true"));
                return;
            }

            if (taskService != null && taskId != null) {
                Map<String, Object> variables = new HashMap<>();
                variables.put("actionCompleted", true);
                variables.put("actionEvidence", actionEvidence);
                variables.put("actionNotes", actionNotes);
                variables.put("executedBy", getCurrentUsername());
                variables.put("executionDate", System.currentTimeMillis());

                taskService.complete(taskId, variables);

                log.info("✅ Tarea completada exitosamente");

                Messagebox.show("Corrective action completed successfully", "Success",
                              Messagebox.OK, Messagebox.INFORMATION,
                              event -> Executions.getCurrent().sendRedirect("/console/bpmn/task-inbox.zul"));
            }
        } catch (Exception e) {
            log.error("❌ Error completando tarea: {}", e.getMessage(), e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Cancelar y volver a bandeja
     */
    @Command
    public void cancel() {
        String redirect = mockMode ? "/console/bpmn/task-inbox.zul?mock=true" : "/console/bpmn/task-inbox.zul";
        Executions.getCurrent().sendRedirect(redirect);
    }

    /**
     * Mock data para demos
     */
    private void loadMockData() {
        this.taskId = "mock-execute-action-task-001";
        this.processInstanceId = "mock-process-incident-001";
        this.correctiveActionIndex = 1;
        this.actionType = "SHORT_TERM";
        this.correctiveActionDescription = "Fix memory leak in service layer";
        this.actionEvidence = "Code review completed, fix deployed to staging";
        this.actionNotes = "Fix tested and verified";

        log.info("🎭 Mock data loaded for Execute Corrective Action");
    }

    // ===============================================
    // PRIVATE HELPERS
    // ===============================================

    private String getCurrentUsername() {
        if (ctxBean != null && ctxBean.getUser() != null) {
            return ctxBean.getUser().getUsuname();
        }
        String username = (String) Sessions.getCurrent().getAttribute("username");
        return username != null ? username : "unknown";
    }

    @org.zkoss.bind.annotation.Destroy
    public void destroy() {
        taskService = null;
    }
}
