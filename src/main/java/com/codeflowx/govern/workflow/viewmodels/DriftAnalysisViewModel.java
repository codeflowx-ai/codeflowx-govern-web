package com.codeflowx.govern.workflow.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.MasterPage;
import org.flowable.engine.RuntimeService;
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
 * ViewModel: Drift Detection - Root Cause Analysis
 * 
 * BPMN Process: drift-detection-v1
 * User Task: driftAnalysisTask
 * Candidate Groups: data-scientists, ml-engineers
 * 
 * Funcionalidad:
 * - Mostrar métricas de drift detectado (tipo, score, severidad)
 * - Visualizar comparación baseline vs actual
 * - Analizar posibles causas raíz del drift
 * - Recomendar acciones correctivas
 * - Decidir siguiente paso: RETRAIN, INVESTIGATE_MORE, ACCEPT_DRIFT
 * 
 * Input Variables (desde proceso BPMN):
 * - modelId: Long - ID del modelo con drift
 * - modelName: String - Nombre del modelo
 * - driftScore: Double - Puntuación del drift (0-1)
 * - driftThreshold: Double - Umbral configurado
 * - driftType: String - Tipo (DATA_DRIFT, CONCEPT_DRIFT, etc.)
 * - severity: String - Severidad (LOW, MEDIUM, HIGH, CRITICAL)
 * - baselineMetrics: Map - Métricas baseline
 * - currentMetrics: Map - Métricas actuales
 * 
 * Output Variables (al completar task):
 * - drift_decision: String - 'retrain', 'investigate_more', 'accept_drift'
 * - root_cause_analysis: String - Análisis de causa raíz
 * - recommended_actions: List<String> - Acciones recomendadas
 * - analysis_notes: String - Notas del analista
 * - analyzed_by: String - Username del analista
 * - analysis_date: Timestamp
 * 
 * Modo MOCK:
 * - URL: /workflow/drift-analysis.zul?taskId=mock-9&mock=true
 * - Datos simulados: Modelo recomendaciones, drift -12% accuracy, data drift
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class DriftAnalysisViewModel extends MasterPage {

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
    private List<String> rootCauses = new ArrayList<>();
    private List<String> recommendations = new ArrayList<>();
    private String decision;
    private String notes;

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
       
       
        
        log.info("🚀 Inicializando DriftAnalysisViewModel - MOCK MODE: {}", mockMode);
        
        if (mockMode) {
            loadMockData();
        } else {
            loadRealData();
        }
    }
    
    private void loadMockData() {
        log.info("🎭 Cargando datos MOCK para Drift Analysis...");
        
        taskId = Executions.getCurrent().getParameter("taskId");
        processInstanceId = "mock-process-9";
        
        // Datos del modelo con drift
        modelName = "Modelo_Recomendaciones_v2.8";
        driftScore = 0.32;
        driftThreshold = 0.15;
        driftType = "DATA_DRIFT";
        severity = "CRITICAL";
        
        // Causas raíz identificadas
        rootCauses.add("Cambio en distribución de datos de entrada (+45% nuevos usuarios Gen-Z)");
        rootCauses.add("Tendencias estacionales no capturadas en training (Black Friday)");
        rootCauses.add("Categorías de productos nuevas no vistas en entrenamiento");
        
        // Recomendaciones
        recommendations.add("🔄 Reentrenamiento con datos de últimos 3 meses");
        recommendations.add("📊 Agregar features de estacionalidad");
        recommendations.add("🎯 Incrementar dataset con nuevas categorías");
        recommendations.add("⚡ Implementar retraining automático quincenal");
        
        log.info("✅ Datos MOCK cargados - Modelo: {}, Drift: {}%", modelName, (int)(driftScore * 100));
    }
    
    private void loadRealData() {
        log.info("💼 Cargando datos reales desde Flowable...");
        
        try {
            taskId = Executions.getCurrent().getParameter("taskId");
            if (taskId == null) {
                Messagebox.show("Error: Task ID no encontrado", "Error", Messagebox.OK, Messagebox.ERROR);
                return;
            }

            org.flowable.task.api.Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null) {
                Messagebox.show("Error: Tarea no encontrada", "Error", Messagebox.OK, Messagebox.ERROR);
                return;
            }

            processInstanceId = task.getProcessInstanceId();
            
            // Cargar variables del proceso
            Map<String, Object> vars = runtimeService.getVariables(processInstanceId);
            modelName = (String) vars.get("modelName");
            driftScore = (Double) vars.get("driftScore");
            driftThreshold = (Double) vars.get("driftThreshold");
            driftType = (String) vars.get("driftType");
            severity = (String) vars.get("severity");
            
            log.info("✅ Datos reales cargados: taskId={}", taskId);
            
        } catch (Exception e) {
            log.error("❌ Error cargando datos", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    // ========== Comandos ==========
    
    @Command
    @NotifyChange({"*"})
    public void doSubmit() {
        if (mockMode) {
            log.info("🎭 MOCK: Simulando envío de análisis de drift...");
            Messagebox.show(
                "✅ DEMO: Análisis de drift enviado\n\n" +
                "Modelo: " + modelName + "\n" +
                "Drift: " + (int)(driftScore * 100) + "%\n" +
                "Decisión: " + decision + "\n\n" +
                "(Modo MOCK - No se guardó en BD)", 
                "Demo - Análisis Completado", 
                Messagebox.OK, Messagebox.INFORMATION,
                event -> Executions.sendRedirect("/workflow/task-inbox.zul?mock=true"));
            return;
        }
        
        try {
            Map<String, Object> taskVariables = new HashMap<>();
            taskVariables.put("drift_decision", decision);
            taskVariables.put("root_cause_analysis", String.join("; ", rootCauses));
            taskVariables.put("recommended_actions", recommendations);
            taskVariables.put("analysis_notes", notes);
            taskVariables.put("analyzed_by", getUser().getUsername());
            taskVariables.put("analysis_date", new java.sql.Timestamp(System.currentTimeMillis()));
            
            taskService.complete(taskId, taskVariables);
            
            logActivity("ANALISIS", "DRIFT_DETECTION", Long.parseLong(taskId), 
                       "Análisis drift: " + modelName + " - Decisión: " + decision);
            
            Messagebox.show("Análisis completado correctamente", "Éxito", 
                Messagebox.OK, Messagebox.INFORMATION,
                event -> Executions.sendRedirect("/plataforma/workflow/my-tasks.zul"));
                
        } catch (Exception e) {
            log.error("❌ Error enviando análisis", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    public void doCancel() {
        String redirectUrl = mockMode ? "/workflow/task-inbox.zul?mock=true" : "/plataforma/workflow/my-tasks.zul";
        Executions.sendRedirect(redirectUrl);
    }
    
    // ========== Auditoría ==========
    
    private void logActivity(String action, String model, Long pk, String mensaje) {
        try {
            Ssoractividad activityLog = new Ssoractividad();
            activityLog.setUsername(getUser().getUsername());
            activityLog.setAccion(action);
            activityLog.setAlta(new java.sql.Timestamp(System.currentTimeMillis()));
            activityLog.setModulo(model);
            activityLog.setIdtupla(pk != null ? pk.intValue() : 0);
            activityLog.setAplicacion(ctxBean.getApplicationName());
            activityLog.setValuetupla(mensaje);
            businessService.save(activityLog);
        } catch (Exception e) {
            log.error("Error al auditar acción: {} en módulo: {}", action, model, e);
        }
    }
    
    @org.zkoss.bind.annotation.Destroy
    public void destroy() {
        if (rootCauses != null) { rootCauses.clear(); rootCauses = null; }
        if (recommendations != null) { recommendations.clear(); recommendations = null; }
        businessService = null;
        taskService = null;
        runtimeService = null;
    }
}
