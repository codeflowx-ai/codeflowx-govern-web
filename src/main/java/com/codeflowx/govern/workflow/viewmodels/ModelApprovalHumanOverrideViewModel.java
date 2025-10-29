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
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
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
 * ViewModel: Model Approval - Human Override Review
 * 
 * BPMN Process: model-approval-v1
 * User Task: humanReviewTask
 * Candidate Groups: ml-engineers, governance-admins
 * 
 * Funcionalidad:
 * - Mostrar métricas de evaluación del modelo (performance, bias, compliance)
 * - Visualizar decisión recomendada de Drools
 * - Permitir override manual de la decisión automática
 * - Aprobar o rechazar el modelo para producción
 * - Registrar justificación de override si difiere de Drools
 * 
 * Input Variables (desde proceso BPMN):
 * - modelId: Long - ID del modelo
 * - modelName: String - Nombre del modelo
 * - modelVersion: String - Versión
 * - performanceScore: Double - Score de performance (0-100)
 * - biasScore: Double - Score de sesgo (0-100)
 * - complianceScore: Double - Score de compliance (0-100)
 * - droolsDecision: String - Decisión recomendada ('approve'/'reject')
 * - confidenceLevel: Double - Confianza de Drools
 * - justification: String - Justificación de Drools
 * - minPerformanceThreshold: Double - Umbral mínimo
 * 
 * Output Variables (al completar task):
 * - human_decision: String - 'approve' o 'reject'
 * - human_justification: String - Justificación del revisor
 * - override_occurred: Boolean - Si difiere de Drools
 * - override_reason: String - Razón del override
 * - approved_by: String - Username
 * - approval_date: Timestamp
 * 
 * Modo MOCK:
 * - URL: /workflow/model-approval-override.zul?taskId=mock-17&mock=true
 * - Datos simulados: Modelo fraude v4.2, performance 94%, bias 89%, Drools: approve
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class ModelApprovalHumanOverrideViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;

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

    @WireVariable
    private TaskService taskService;

    @WireVariable
    private RuntimeService runtimeService;

    @Getter
    private boolean mockMode = false;

    private String taskId;
    private String processInstanceId;
    private String modelName;
    private String modelVersion;
    private Double performanceScore;
    private Double biasScore;
    private Double complianceScore;
    private String droolsDecision;
    private Double confidenceLevel;
    private String droolsJustification;
    private List<String> performanceMetrics = new ArrayList<>();
    private String selectedDecision;
    private String humanJustification;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
     // Detectar mock mode desde parámetros URL
        if(System.getenv("MOCK_MODE")!=null) {
        	mockMode = Boolean.parseBoolean(System.getenv("MOCK_MODE").toString());
        }
       
        
        
        log.info("🚀 Inicializando ModelApprovalHumanOverrideViewModel - MOCK MODE: {}", mockMode);
        
        if (mockMode) {
            loadMockData();
        } else {
            loadRealData();
        }
    }
    
    private void loadMockData() {
        log.info("🎭 Cargando datos MOCK para Model Approval...");
        
        taskId = Executions.getCurrent().getParameter("taskId");
        processInstanceId = "mock-process-17";
        
        modelName = "Modelo_Prediccion_Fraude_v4.2";
        modelVersion = "4.2.0";
        performanceScore = 94.5;
        biasScore = 89.0;
        complianceScore = 92.0;
        droolsDecision = "APPROVE";
        confidenceLevel = 0.92;
        droolsJustification = "Modelo cumple todos los umbrales: Performance 94.5% (min 85%), " +
                             "Bias 89% (min 80%), Compliance 92% (min 85%). Recomendación: APROBAR.";
        
        performanceMetrics.add("Accuracy: 94.5%");
        performanceMetrics.add("Precision: 93.2%");
        performanceMetrics.add("Recall: 91.8%");
        performanceMetrics.add("F1-Score: 92.5%");
        performanceMetrics.add("AUC-ROC: 0.96");
        
        log.info("✅ Datos MOCK cargados - Modelo fraude con alta performance");
    }
    
    private void loadRealData() {
        try {
            taskId = Executions.getCurrent().getParameter("taskId");
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            processInstanceId = task.getProcessInstanceId();
            
            Map<String, Object> vars = runtimeService.getVariables(processInstanceId);
            modelName = (String) vars.get("modelName");
            modelVersion = (String) vars.get("modelVersion");
            performanceScore = (Double) vars.get("performanceScore");
            biasScore = (Double) vars.get("biasScore");
            complianceScore = (Double) vars.get("complianceScore");
            droolsDecision = (String) vars.get("droolsDecision");
            confidenceLevel = (Double) vars.get("confidenceLevel");
            droolsJustification = (String) vars.get("justification");
        } catch (Exception e) {
            log.error("Error cargando datos", e);
        }
    }

    @Command
    public void approve() {
        if (mockMode) {
            Messagebox.show("✅ DEMO: Modelo APROBADO\n\nModelo: " + modelName + "\nVersión: " + modelVersion + "\nPerformance: " + performanceScore + "%\n\n(Modo MOCK)", 
                "Demo - Modelo Aprobado", Messagebox.OK, Messagebox.INFORMATION,
                e -> Executions.sendRedirect("/workflow/task-inbox.zul?mock=true"));
            return;
        }
        
        selectedDecision = "APPROVE";
        confirmDecision();
    }

    @Command
    public void reject() {
        if (mockMode) {
            Messagebox.show("❌ DEMO: Modelo RECHAZADO\n\nModelo: " + modelName + "\n\n(Modo MOCK)", 
                "Demo - Modelo Rechazado", Messagebox.OK, Messagebox.INFORMATION,
                e -> Executions.sendRedirect("/workflow/task-inbox.zul?mock=true"));
            return;
        }
        
        selectedDecision = "REJECT";
        confirmDecision();
    }
    
    private void confirmDecision() {
        try {
            Map<String, Object> taskVars = new HashMap<>();
            taskVars.put("human_decision", selectedDecision);
            taskVars.put("human_justification", humanJustification);
            taskVars.put("override_occurred", !selectedDecision.equalsIgnoreCase(droolsDecision));
            taskVars.put("approved_by", getUser().getUsername());
            taskVars.put("approval_date", new java.sql.Timestamp(System.currentTimeMillis()));

            taskService.complete(taskId, taskVars);
            
            logActivity("APROBACION_MODELO", "MODMODELAPPROVALS", Long.parseLong(taskId), 
                       "Modelo " + selectedDecision + ": " + modelName);

            Messagebox.show("Decisión registrada", "Éxito", Messagebox.OK, Messagebox.INFORMATION);

        } catch (Exception e) {
            log.error("Error confirmando decisión", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    public void cancel() {
        Executions.sendRedirect(mockMode ? "/workflow/task-inbox.zul?mock=true" : "/console/govern/models-dashboard.zul");
    }
    
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
        if (performanceMetrics != null) { performanceMetrics.clear(); performanceMetrics = null; }
        businessService = null;
        taskService = null;
        runtimeService = null;
    }
}
