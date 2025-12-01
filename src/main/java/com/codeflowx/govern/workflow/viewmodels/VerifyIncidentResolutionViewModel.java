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
 * ViewModel: Verify Incident Resolution
 *
 * BPMN Process: incident-reporting-process
 * User Task: verifyIncidentResolution
 * Candidate Groups: compliance-officers
 *
 * Funcionalidad:
 * - Verificar que todas las acciones correctivas se completaron
 * - Confirmar que el incidente está resuelto
 * - Escalar si no está resuelto
 *
 * Input Variables:
 * - incidentTitle, correctiveActionsCount
 *
 * Output Variables:
 * - incidentResolved (Boolean), verificationNotes, resolutionEvidence
 * - verifiedBy, verificationDate
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class VerifyIncidentResolutionViewModel extends BaseFront<VerifyIncidentResolutionViewModel> {

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
    private Integer correctiveActionsCount = 0;

    // Verification fields
    private Boolean issueResolved = false;
    private Boolean noRecurrence = false;
    private String verificationNotes = "";
    private String resolutionEvidence = "";

    // Mock mode
    private boolean mockMode = false;

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();

        log.info("🚀 Inicializando VerifyIncidentResolutionViewModel");

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
            Object countObj = variables.get("correctiveActionsCount");
            if (countObj != null) {
                correctiveActionsCount = countObj instanceof Integer ? (Integer) countObj :
                                        countObj instanceof Long ? ((Long) countObj).intValue() : 0;
            }

            log.info("✅ Variables cargadas - Incident: {}, Actions: {}", incidentTitle, correctiveActionsCount);

        } catch (Exception e) {
            log.error("❌ Error inicializando: {}", e.getMessage(), e);
            Messagebox.show("Error cargando datos: " + e.getMessage(), "Error",
                            Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Confirmar resolución y cerrar incidente
     */
    @Command
    @NotifyChange("*")
    public void confirmResolution() {
        log.info("User decision: Confirm Resolution - taskId: {}", taskId);

        if (!issueResolved || !noRecurrence) {
            Messagebox.show("Please confirm both checkboxes to verify resolution",
                          "Validation", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        try {
            if (mockMode) {
                log.info("🎭 Mock mode: Simulando confirmResolution");
                Messagebox.show("✅ [DEMO] Incident marked as RESOLVED and will be closed",
                    "Demo Mode", Messagebox.OK, Messagebox.INFORMATION,
                    event -> Executions.getCurrent().sendRedirect("/console/bpmn/task-inbox.zul?mock=true"));
                return;
            }

            if (taskService != null && taskId != null) {
                Map<String, Object> variables = new HashMap<>();
                variables.put("incidentResolved", true);
                variables.put("verificationNotes", verificationNotes);
                variables.put("resolutionEvidence", resolutionEvidence);
                variables.put("verifiedBy", getCurrentUsername());
                variables.put("verificationDate", System.currentTimeMillis());

                taskService.complete(taskId, variables);

                log.info("✅ Tarea completada exitosamente");

                Messagebox.show("Incident marked as RESOLVED and will be closed",
                              "Success", Messagebox.OK, Messagebox.INFORMATION,
                              event -> Executions.getCurrent().sendRedirect("/console/bpmn/task-inbox.zul"));
            }
        } catch (Exception e) {
            log.error("❌ Error completando tarea: {}", e.getMessage(), e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Escalar incidente (no resuelto)
     */
    @Command
    @NotifyChange("*")
    public void escalateIncident() {
        log.info("User decision: Escalate - taskId: {}", taskId);

        Messagebox.show("This will escalate the incident and create a new corrective action cycle. Continue?",
                       "Confirm Escalation",
                       Messagebox.YES | Messagebox.NO,
                       Messagebox.QUESTION,
                       event -> {
                           if (event.getName().equals("onYes")) {
                               try {
                                   if (mockMode) {
                                       log.info("🎭 Mock mode: Simulando escalateIncident");
                                       Messagebox.show("✅ [DEMO] Incident ESCALATED - new corrective actions required",
                                           "Demo Mode", Messagebox.OK, Messagebox.WARNING,
                                           e -> Executions.getCurrent().sendRedirect("/console/bpmn/task-inbox.zul?mock=true"));
                                       return;
                                   }

                                   if (taskService != null && taskId != null) {
                                       Map<String, Object> variables = new HashMap<>();
                                       variables.put("incidentResolved", false);
                                       variables.put("verificationNotes", verificationNotes);
                                       variables.put("escalatedBy", getCurrentUsername());

                                       taskService.complete(taskId, variables);

                                       log.info("✅ Tarea completada - Incidente escalado");

                                       Messagebox.show("Incident ESCALATED - new corrective actions required",
                                                     "Info", Messagebox.OK, Messagebox.WARNING,
                                                     e -> Executions.getCurrent().sendRedirect("/console/bpmn/task-inbox.zul"));
                                   }
                               } catch (Exception e) {
                                   log.error("❌ Error completando tarea: {}", e.getMessage(), e);
                                   Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
                               }
                           }
                       });
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
        this.taskId = "mock-verify-resolution-task-001";
        this.processInstanceId = "mock-process-incident-001";
        this.incidentTitle = "System Performance Degradation - Critical";
        this.correctiveActionsCount = 3;
        this.verificationNotes = "All corrective actions completed successfully";
        this.resolutionEvidence = "Performance metrics back to normal, no recurrence detected";

        log.info("🎭 Mock data loaded for Verify Incident Resolution");
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

    @org.zkoss.bind.annotation.Destroy
    public void destroy() {
        taskService = null;
    }
}
