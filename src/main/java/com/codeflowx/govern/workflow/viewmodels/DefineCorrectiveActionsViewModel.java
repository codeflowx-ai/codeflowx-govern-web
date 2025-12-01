package com.codeflowx.govern.workflow.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel: Define Corrective Actions
 *
 * BPMN Process: incident-reporting-process
 * User Task: defineCorrectiveActions (Art. 20)
 * Candidate Groups: tech-lead, compliance
 *
 * Funcionalidad:
 * - Definir acciones correctivas (inmediatas, corto plazo, largo plazo)
 * - Asignar responsables por acción
 * - Completar tarea con lista de acciones
 *
 * Input Variables:
 * - incidentTitle, rootCause (desde RootCauseAnalysis)
 *
 * Output Variables:
 * - correctiveActions (List<String>), correctiveActionsCount
 * - definedBy
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class DefineCorrectiveActionsViewModel extends BaseFront<DefineCorrectiveActionsViewModel> {

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
    private String incidentTitle = "";
    private String rootCause = "";

    // Corrective Actions
    private List<CorrectiveAction> correctiveActions = new ArrayList<>();
    private String newActionDescription = "";
    private String newActionType = "SHORT_TERM";
    private String newActionResponsible = "";

    // Mock mode
    private boolean mockMode = false;

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();

        log.info("🚀 Inicializando DefineCorrectiveActionsViewModel");

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

            incidentTitle = getStringVariable(variables, "incidentTitle");
            rootCause = getStringVariable(variables, "whyHappened");

            log.info("✅ Variables cargadas - Incident: {}", incidentTitle);

        } catch (Exception e) {
            log.error("❌ Error inicializando: {}", e.getMessage(), e);
            Messagebox.show("Error cargando datos: " + e.getMessage(), "Error",
                            Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Añadir acción correctiva
     */
    @Command
    @NotifyChange("*")
    public void addAction() {
        if (newActionDescription.trim().isEmpty()) {
            Messagebox.show("Please enter action description", "Validation",
                          Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        CorrectiveAction action = new CorrectiveAction();
        action.setDescription(newActionDescription);
        action.setType(newActionType);
        action.setResponsible(newActionResponsible);

        correctiveActions.add(action);

        // Clear form
        newActionDescription = "";
        newActionResponsible = "";

        log.info("✅ Acción añadida - Total: {}", correctiveActions.size());
    }

    /**
     * Eliminar acción correctiva
     */
    @Command
    @NotifyChange("*")
    public void removeAction(@org.zkoss.bind.annotation.BindingParam("action") CorrectiveAction action) {
        correctiveActions.remove(action);
        log.info("✅ Acción eliminada - Total: {}", correctiveActions.size());
    }

    /**
     * Enviar acciones correctivas y completar tarea
     */
    @Command
    @NotifyChange("*")
    public void submitActions() {
        log.info("Submitting corrective actions - taskId: {}, count: {}", taskId, correctiveActions.size());

        if (correctiveActions.isEmpty()) {
            Messagebox.show("Please define at least one corrective action",
                          "Validation", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        try {
            if (mockMode) {
                log.info("🎭 Mock mode: Simulando submitActions");
                Messagebox.show("✅ [DEMO] Corrective actions defined successfully",
                    "Demo Mode", Messagebox.OK, Messagebox.INFORMATION,
                    event -> Executions.getCurrent().sendRedirect("/console/bpmn/task-inbox.zul?mock=true"));
                return;
            }

            if (taskService != null && taskId != null) {
                // Convertir a List<String> para BPMN
                List<String> actionDescriptions = new ArrayList<>();
                for (CorrectiveAction action : correctiveActions) {
                    actionDescriptions.add(action.getType() + ": " + action.getDescription() +
                                         " (Responsible: " + action.getResponsible() + ")");
                }

                Map<String, Object> variables = new HashMap<>();
                variables.put("correctiveActions", actionDescriptions);
                variables.put("correctiveActionsCount", correctiveActions.size());
                variables.put("definedBy", getCurrentUsername());

                taskService.complete(taskId, variables);

                log.info("✅ Tarea completada exitosamente");

                Messagebox.show("Corrective actions defined successfully", "Success",
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
        this.taskId = "mock-corrective-actions-task-001";
        this.processInstanceId = "mock-process-incident-001";
        this.incidentTitle = "System Performance Degradation - Critical";
        this.rootCause = "Memory leak in service layer";

        CorrectiveAction action1 = new CorrectiveAction();
        action1.setType("IMMEDIATE");
        action1.setDescription("Restart affected services");
        action1.setResponsible("DevOps Team");
        correctiveActions.add(action1);

        CorrectiveAction action2 = new CorrectiveAction();
        action2.setType("SHORT_TERM");
        action2.setDescription("Fix memory leak in service layer");
        action2.setResponsible("Backend Team");
        correctiveActions.add(action2);

        log.info("🎭 Mock data loaded for Define Corrective Actions");
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

    private String getStringVariable(Map<String, Object> vars, String key) {
        Object value = vars.get(key);
        return value != null ? value.toString() : "";
    }

    @Data
    public static class CorrectiveAction {
        private String description;
        private String type; // IMMEDIATE, SHORT_TERM, LONG_TERM
        private String responsible;
    }

    @org.zkoss.bind.annotation.Destroy
    public void destroy() {
        taskService = null;
    }
}
