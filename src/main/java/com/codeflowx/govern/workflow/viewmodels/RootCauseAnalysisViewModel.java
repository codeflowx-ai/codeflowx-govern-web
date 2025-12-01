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
 * ViewModel: Root Cause Analysis
 *
 * BPMN Process: incident-reporting-process
 * User Task: rootCauseAnalysis (Art. 20)
 * Candidate Groups: tech-team, compliance-officers
 *
 * Funcionalidad:
 * - Realizar análisis de causa raíz del incidente
 * - Completar campos: What Happened, Why Happened, Contributing Factors, Timeline
 * - Mostrar reporte automático de RCA si está disponible
 *
 * Input Variables:
 * - incidentTitle, automatedRcaReport (desde ExecuteRCADelegate)
 *
 * Output Variables:
 * - whatHappened, whyHappened, contributingFactors, timeline
 * - rcaCompletedBy, rcaCompletionDate
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class RootCauseAnalysisViewModel extends BaseFront<RootCauseAnalysisViewModel> {

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
    private String automatedRcaReport = "";

    // RCA Fields
    private String whatHappened = "";
    private String whyHappened = "";
    private String contributingFactors = "";
    private String timeline = "";

    // Mock mode
    private boolean mockMode = false;

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();

        log.info("🚀 Inicializando RootCauseAnalysisViewModel");

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
            automatedRcaReport = getStringVariable(variables, "automatedRcaReport");

            log.info("✅ Variables cargadas - Incident: {}", incidentTitle);

        } catch (Exception e) {
            log.error("❌ Error inicializando: {}", e.getMessage(), e);
            Messagebox.show("Error cargando datos: " + e.getMessage(), "Error",
                            Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Enviar RCA y completar tarea
     */
    @Command
    @NotifyChange("*")
    public void submitRCA() {
        log.info("Submitting RCA - taskId: {}", taskId);

        if (whatHappened.trim().isEmpty() || whyHappened.trim().isEmpty()) {
            Messagebox.show("Please complete 'What Happened' and 'Why Happened' fields",
                          "Validation", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        try {
            if (mockMode) {
                log.info("🎭 Mock mode: Simulando submitRCA");
                Messagebox.show("✅ [DEMO] Root Cause Analysis completed successfully",
                    "Demo Mode", Messagebox.OK, Messagebox.INFORMATION,
                    event -> Executions.getCurrent().sendRedirect("/console/bpmn/task-inbox.zul?mock=true"));
                return;
            }

            if (taskService != null && taskId != null) {
                Map<String, Object> variables = new HashMap<>();
                variables.put("whatHappened", whatHappened);
                variables.put("whyHappened", whyHappened);
                variables.put("contributingFactors", contributingFactors);
                variables.put("timeline", timeline);
                variables.put("rcaCompletedBy", getCurrentUsername());
                variables.put("rcaCompletionDate", System.currentTimeMillis());

                taskService.complete(taskId, variables);

                log.info("✅ Tarea completada exitosamente");

                Messagebox.show("Root Cause Analysis completed", "Success",
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
        this.taskId = "mock-rca-task-001";
        this.processInstanceId = "mock-process-incident-001";
        this.incidentTitle = "System Performance Degradation - Critical";
        this.automatedRcaReport = "Automated RCA analysis detected: Memory leak in service layer";
        this.whatHappened = "System performance degraded significantly over 2 hours";
        this.whyHappened = "Memory leak in service layer caused by unclosed database connections";
        this.contributingFactors = "High load, insufficient monitoring";
        this.timeline = "T-120min: Normal operation\nT-60min: First signs of degradation\nT-30min: Performance drop\nT0: Incident occurred";

        log.info("🎭 Mock data loaded for Root Cause Analysis");
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
