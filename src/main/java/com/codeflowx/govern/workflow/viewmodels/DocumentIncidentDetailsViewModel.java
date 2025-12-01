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
 * ViewModel: Document Incident Details
 *
 * BPMN Process: incident-reporting-process
 * User Task: documentIncidentDetails
 * Candidate Groups: compliance-officers, tech-team
 *
 * Funcionalidad:
 * - Documentar detalles de incidente grave (Art. 73)
 * - Completar información requerida para notificación a autoridades
 * - Guardar documentación en variables del proceso
 *
 * Input Variables:
 * - severity, affectedUsersCount (desde ClassifyIncidentSeverityDelegate)
 *
 * Output Variables:
 * - incidentTitle, detailedDescription, impactAssessment
 * - initialActions, potentialRisks
 * - documentedBy, documentationDate
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class DocumentIncidentDetailsViewModel extends BaseFront<DocumentIncidentDetailsViewModel> {

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
    private String severity = "";
    private Integer affectedUsersCount = 0;

    // Formulario documentación
    private String incidentTitle = "";
    private String detailedDescription = "";
    private String impactAssessment = "";
    private String initialActions = "";
    private String potentialRisks = "";

    // Mock mode
    private boolean mockMode = false;

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();

        log.info("🚀 Inicializando DocumentIncidentDetailsViewModel");

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

            severity = getStringVariable(variables, "severity");
            affectedUsersCount = getIntVariable(variables, "affectedUsersCount");
            incidentTitle = getStringVariable(variables, "incidentTitle");

            log.info("✅ Variables cargadas - Severity: {}, Affected Users: {}", severity, affectedUsersCount);

        } catch (Exception e) {
            log.error("❌ Error inicializando: {}", e.getMessage(), e);
            Messagebox.show("Error cargando datos: " + e.getMessage(), "Error",
                            Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Enviar documentación y completar tarea
     */
    @Command
    @NotifyChange("*")
    public void submitDocumentation() {
        log.info("Submitting incident documentation - taskId: {}", taskId);

        if (incidentTitle.trim().isEmpty() || detailedDescription.trim().isEmpty()) {
            Messagebox.show("Please fill all required fields (Title and Description)",
                          "Validation", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        try {
            if (mockMode) {
                log.info("🎭 Mock mode: Simulando submitDocumentation");
                Messagebox.show("✅ [DEMO] Incident documentation submitted successfully",
                    "Demo Mode", Messagebox.OK, Messagebox.INFORMATION,
                    event -> Executions.getCurrent().sendRedirect("/console/bpmn/task-inbox.zul?mock=true"));
                return;
            }

            if (taskService != null && taskId != null) {
                Map<String, Object> variables = new HashMap<>();
                variables.put("incidentTitle", incidentTitle);
                variables.put("detailedDescription", detailedDescription);
                variables.put("impactAssessment", impactAssessment);
                variables.put("initialActions", initialActions);
                variables.put("potentialRisks", potentialRisks);
                variables.put("documentedBy", getCurrentUsername());
                variables.put("documentationDate", System.currentTimeMillis());

                taskService.complete(taskId, variables);

                log.info("✅ Tarea completada exitosamente");

                Messagebox.show("Incident documented successfully", "Success",
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
        this.taskId = "mock-incident-document-task-001";
        this.processInstanceId = "mock-process-incident-001";
        this.severity = "CRITICAL";
        this.affectedUsersCount = 150;
        this.incidentTitle = "System Performance Degradation - Critical";
        this.detailedDescription = "Mock incident description for demo purposes";
        this.impactAssessment = "High impact on user experience";
        this.initialActions = "Immediate containment actions taken";
        this.potentialRisks = "Risk of data loss if not addressed";

        log.info("🎭 Mock data loaded for Document Incident Details");
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

    private Integer getIntVariable(Map<String, Object> vars, String key) {
        Object value = vars.get(key);
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Long) return ((Long) value).intValue();
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    @org.zkoss.bind.annotation.Destroy
    public void destroy() {
        taskService = null;
    }
}
