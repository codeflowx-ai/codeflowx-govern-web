package com.codeflowx.govern.workflow.viewmodels;
import com.codeflowx.framework.zkoss.BaseFront;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
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
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.admin.Ssoractividad;

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel: Drift Review Decision (Timer-triggered)
 * 
 * BPMN Process: drift-detection-v1
 * User Task: driftReviewDecisionTask
 * Candidate Groups: ml-engineers, governance-admins
 * Triggered by: Timer Boundary Event (PT48H - después de 48 horas)
 * 
 * Funcionalidad:
 * - Mostrar estado actual del drift después de 48h de monitoreo
 * - Visualizar si el drift persiste, empeoró o mejoró
 * - Decidir acción definitiva: RETRAIN, WAIT_MORE_DATA, ACCEPT_DRIFT
 * - Registrar justificación de la decisión
 * - Disparar proceso de retraining si se decide
 * 
 * Input Variables (desde proceso BPMN):
 * - modelId: Long - ID del modelo
 * - modelName: String - Nombre del modelo
 * - driftScore: Double - Score actual del drift
 * - driftThreshold: Double - Umbral configurado
 * - driftType: String - Tipo de drift
 * - severity: String - Severidad
 * - driftRecommendations: String - Recomendaciones del análisis previo
 * - waitHours: Integer - Horas que lleva esperando (48)
 * 
 * Output Variables (al completar task):
 * - review_decision: String - 'retrain', 'wait_more_data', 'accept_drift'
 * - drift_review_justification: String - Justificación de la decisión
 * - retrain_immediately: Boolean - Si iniciar retraining inmediato
 * - wait_additional_days: Integer - Días adicionales a esperar (si aplica)
 * - reviewed_by: String - Username
 * - review_timestamp: Timestamp
 * 
 * Modo MOCK:
 * - URL: /workflow/drift-review-decision.zul?taskId=mock-10&mock=true
 * - Datos simulados: Modelo predicción ventas, drift persistente, decisión pendiente
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class DriftReviewDecisionViewModel extends BaseFront<DriftReviewDecisionViewModel>{

    private static final long serialVersionUID = 1L;

    // ========== Servicios y contexto Spring ==========
    @WireVariable
    private BusinessService businessService;
    
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
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }
    
    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }

    // ========== Servicios Flowable ==========
    @WireVariable
    private TaskService taskService;

    @WireVariable
    private RuntimeService runtimeService;

    // ========== Modo MOCK ==========
    @Getter
    private boolean mockMode = false;

    // ========== Datos ==========
    private String taskId;
    private String processInstanceId;
    private String modelName;
    private Double driftScore;
    private Double driftThreshold;
    private String driftType;
    private String severity;
    private String driftRecommendations;
    private String selectedDecision;
    private String justification;

    // ========== Inicialización ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        // Detectar modo MOCK
     // Detectar mock mode desde parámetros URL
        if(System.getenv("MOCK_MODE")!=null) {
        	mockMode = Boolean.parseBoolean(System.getenv("MOCK_MODE").toString());
        }
       
       
        
        log.info("🚀 Inicializando DriftReviewDecisionViewModel - MOCK MODE: {}", mockMode);
        
        if (mockMode) {
            loadMockData();
        } else {
            loadRealData();
        }
    }
    
    private void loadMockData() {
        log.info("🎭 Cargando datos MOCK para Drift Review Decision...");
        
        taskId = Executions.getCurrent().getParameter("taskId");
        processInstanceId = "mock-process-10";
        
        modelName = "Modelo_Prediccion_Ventas_v3.1";
        driftScore = 0.28;
        driftThreshold = 0.15;
        driftType = "CONCEPT_DRIFT";
        severity = "HIGH";
        driftRecommendations = "Reentrenamiento recomendado con datos de últimos 6 meses. " +
                              "El modelo no captura nuevos patrones de comportamiento post-pandemia.";
        
        log.info("✅ Datos MOCK cargados - Han pasado 48h desde detección inicial");
    }
    
    private void loadRealData() {
        log.info("💼 Cargando datos reales desde Flowable...");
        
        try {
            taskId = Executions.getCurrent().getParameter("taskId");
            if (taskId == null) {
                Messagebox.show("Error: Task ID no encontrado", "Error", Messagebox.OK, Messagebox.ERROR);
                return;
            }

            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null) {
                Messagebox.show("Error: Tarea no encontrada", "Error", Messagebox.OK, Messagebox.ERROR);
                return;
            }

            processInstanceId = task.getProcessInstanceId();

            // Cargar variables del drift
            modelName = (String) runtimeService.getVariable(processInstanceId, "modelName");
            driftScore = (Double) runtimeService.getVariable(processInstanceId, "driftScore");
            driftThreshold = (Double) runtimeService.getVariable(processInstanceId, "driftThreshold");
            driftType = (String) runtimeService.getVariable(processInstanceId, "driftType");
            severity = (String) runtimeService.getVariable(processInstanceId, "severity");
            driftRecommendations = (String) runtimeService.getVariable(processInstanceId, "driftRecommendations");

            log.info("✅ Drift Review Decision cargado - Model: {} | Score: {}", modelName, driftScore);

        } catch (Exception e) {
            log.error("❌ Error inicializando formulario", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    // ========== Comandos ==========
    
    @Command
    @NotifyChange("selectedDecision")
    public void selectDecision(String decision) {
        this.selectedDecision = decision;
        log.info("📋 Decisión seleccionada: {}", decision);
    }

    @Command
    public void confirmDecision() {
        if (mockMode) {
            log.info("🎭 MOCK: Simulando decisión de drift...");
            String message = "✅ DEMO: Decisión confirmada\n\n" +
                           "Modelo: " + modelName + "\n" +
                           "Decisión: " + selectedDecision + "\n\n" +
                           getConfirmationMessage() + "\n\n" +
                           "(Modo MOCK - No se ejecutó acción real)";
            Messagebox.show(message, "Demo - Decisión Confirmada", 
                Messagebox.OK, Messagebox.INFORMATION,
                event -> Executions.sendRedirect("/workflow/task-inbox.zul?mock=true"));
            return;
        }
        
        try {
            if (selectedDecision == null || justification == null || justification.isEmpty()) {
                Messagebox.show("Por favor, selecciona una decisión y proporciona justificación", 
                               "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }

            // Guardar variables en proceso
            runtimeService.setVariable(processInstanceId, "reviewDecision", selectedDecision);
            runtimeService.setVariable(processInstanceId, "driftReviewJustification", justification);

            // Completar tarea
            taskService.complete(taskId);
            
            logActivity("DECISION", "DRIFT_REVIEW", Long.parseLong(taskId), 
                       "Decisión drift: " + modelName + " - " + selectedDecision);

            log.info("✅ Drift Review completado - Decisión: {}", selectedDecision);

            String message = getConfirmationMessage();
            Messagebox.show(message, "Decisión Confirmada", Messagebox.OK, Messagebox.INFORMATION,
                event -> Executions.sendRedirect("/console/govern/governance-reports.zul"));

        } catch (Exception e) {
            log.error("❌ Error confirmando decisión", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    public void cancel() {
        String redirectUrl = mockMode ? "/workflow/task-inbox.zul?mock=true" : "/console/govern/governance-reports.zul";
        Executions.sendRedirect(redirectUrl);
    }

    private String getConfirmationMessage() {
        switch (selectedDecision) {
            case "RETRAIN":
                return "🔄 Se iniciará el reentrenamiento del modelo.\n" +
                       "El proceso de training se disparará automáticamente.";
            case "WAIT_MORE_DATA":
                return "⏳ Se esperarán 7 días más para acumular más datos.\n" +
                       "Se continuará monitoreando el drift.";
            case "ACCEPT_DRIFT":
                return "✅ El drift ha sido aceptado.\n" +
                       "Se documentará en el historial pero no se tomará acción correctiva.";
            default:
                return "Decisión registrada";
        }
    }
    

    
    @org.zkoss.bind.annotation.Destroy
    public void destroy() {
        businessService = null;
        taskService = null;
        runtimeService = null;
    }
}
