package com.codeflowx.govern.workflow.viewmodels;

import java.sql.Timestamp;
import java.util.HashMap;
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
import org.zkoss.bind.annotation.Destroy;
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
 * ViewModel: RAG Evaluation Review
 * 
 * Proceso BPMN: 06_RAG_EVALUATION (rag-evaluation-v1)
 * User Task: ragReviewTask
 * Candidate Groups: ai-governance-team, data-engineers
 * 
 * Funcionalidad:
 * Permite revisar los resultados de evaluación de un sistema RAG (Retrieval-Augmented Generation).
 * Evalúa contexto, relevancia, precisión de recuperación y calidad de generación.
 * 
 * Input Variables:
 * - rag_evaluation_id: Long
 * - rag_system_name: String
 * - overall_score: Double
 * 
 * Output Variables:
 * - decision: "approve" | "reject" | "retest"
 * - notes: String
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class RagEvaluationReviewViewModel extends MasterPage {

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

    private String taskId;
    private String processInstanceId;
    private String decision;
    private String notes;
    
    // Mock mode
    private boolean mockMode = false;
    
    // Mock data
    private String ragSystemName;
    private Long ragEvaluationId;
    private Double overallScore;
    private Double contextRelevance;
    private Double answerAccuracy;
    private Double retrievalPrecision;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        Map<String, String[]> params = Executions.getCurrent().getParameterMap();
        
        // Detectar mock mode
        if (params.containsKey("mock") && "true".equals(params.get("mock")[0])) {
            this.mockMode = true;
            loadMockData();
            log.info("🎭 Mock mode activado");
            return;
        }
        
        if (params.containsKey("taskId")) {
            this.taskId = params.get("taskId")[0];
            loadTaskData();
        }
    }

    private void loadTaskData() {
        try {
            org.flowable.task.api.Task task = taskService.createTaskQuery()
                .taskId(taskId).singleResult();
            this.processInstanceId = task.getProcessInstanceId();
            
            Map<String, Object> variables = runtimeService.getVariables(processInstanceId);
            this.ragSystemName = (String) variables.get("rag_system_name");
            this.ragEvaluationId = (Long) variables.get("rag_evaluation_id");
            
            log.info("✅ Task data loaded: taskId={}", taskId);
        } catch (Exception e) {
            log.error("❌ Error cargando task data", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", 
                Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"*"})
    public void doSubmit() {
        try {
            if (mockMode) {
                log.info("🎭 Mock mode: Simulando submit - decision={}", decision);
                logActivity("MOCK_SUBMIT_RAG_EVAL", "RagEvaluation", ragEvaluationId, "Simulación de decisión: " + decision);
                Messagebox.show("✅ [DEMO] Decisión registrada exitosamente", "Demo Mode", 
                    Messagebox.OK, Messagebox.INFORMATION,
                    event -> Executions.getCurrent().sendRedirect("/plataforma/workflow/my-tasks.zul?mock=true"));
                return;
            }
            
            Map<String, Object> taskVariables = new HashMap<>();
            taskVariables.put("decision", decision);
            taskVariables.put("notes", notes);
            
            taskService.complete(taskId, taskVariables);
            
            logActivity("SUBMIT_RAG_EVAL", "RagEvaluation", ragEvaluationId, "Decisión: " + decision);
            
            Messagebox.show("Decision submitted", "Success", 
                Messagebox.OK, Messagebox.INFORMATION,
                event -> Executions.getCurrent().sendRedirect("/plataforma/workflow/my-tasks.zul"));
                
        } catch (Exception e) {
            log.error("❌ Error submitting", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", 
                Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    public void doCancel() {
        String redirect = mockMode ? "/plataforma/workflow/my-tasks.zul?mock=true" : "/plataforma/workflow/my-tasks.zul";
        Executions.getCurrent().sendRedirect(redirect);
    }
    
    /**
     * Mock data para demos
     */
    private void loadMockData() {
        this.taskId = "mock-rag-eval-task-001";
        this.processInstanceId = "mock-process-rag-eval-001";
        this.ragSystemName = "Legal-Document-RAG";
        this.ragEvaluationId = 2001L;
        this.overallScore = 0.88;
        this.contextRelevance = 0.92;
        this.answerAccuracy = 0.85;
        this.retrievalPrecision = 0.87;
        
        log.info("🎭 Mock data loaded for RAG Evaluation Review");
    }
    
    /**
     * Registra actividad del usuario
     */
    private void logActivity(String action, String model, Long pk, String mensaje) {
        try {
            Ssoractividad activityLog = new Ssoractividad();
            activityLog.setUsername(getUser().getUsername());
            activityLog.setAccion(action);
            activityLog.setAlta(new Timestamp(System.currentTimeMillis()));
            activityLog.setModulo(model);
            activityLog.setIdtupla(pk != null ? pk.intValue() : 0);
            activityLog.setAplicacion(ctxBean.getApplicationName());
            activityLog.setValuetupla(mensaje);
            businessService.save(activityLog);
        } catch (Exception e) {
            log.error("Error al auditar acción: {} en módulo: {}", action, model, e);
        }
    }
    
    @Destroy
    public void destroy() {
        businessService = null;
        taskService = null;
    }
}
