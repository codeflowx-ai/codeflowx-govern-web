package com.codeflowx.govern.workflow.viewmodels;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.govern.entity.evaluation.LlmEvaluation;
import com.fasterxml.jackson.databind.ObjectMapper;

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel: LLM Evaluation Review
 * 
 * Proceso BPMN: 04_LLM_EVALUATION (llm-evaluation-v1)
 * User Task: llmReviewTask
 * Candidate Groups: ai-governance-team, ml-engineers
 * 
 * Funcionalidad:
 * Permite revisar los resultados de evaluación de un LLM (accuracy, toxicity, bias, etc.)
 * y decidir si aprobar o rechazar el modelo según los resultados.
 * 
 * Input Variables:
 * - evaluation_id: Long
 * - model_name: String
 * - overall_score: Double
 * 
 * Output Variables:
 * - action: "approve" | "reject"
 * - justification: String
 * - reviewed_by: String
 * - reviewed_at: Timestamp
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class LlmEvaluationReviewViewModel extends MasterPage {

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

    // Datos de la evaluación
    private String modelName;
    private Long evaluationId;
    private Double overallScore;
    private Double threshold = 0.80;
    private String gap;

    // Métricas
    private List<MetricRow> metrics = new ArrayList<>();
    private Integer totalTestCases;
    private Integer passedTestCases;
    private Integer failedTestCases;
    private String recommendations;

    // Decisión humana
    private String action;
    private String justification;

    private String taskId;
    private String processInstanceId;
    
    // Mock mode
    private boolean mockMode = false;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        Map<String, String[]> params = Executions.getCurrent().getParameterMap();
        
        // Detectar mock mode
     // Detectar mock mode desde parámetros URL
        if(System.getenv("MOCK_MODE")!=null) {
        	mockMode = Boolean.parseBoolean(System.getenv("MOCK_MODE").toString());
        }
       
        
        
        if (params.containsKey("taskId")) {
            this.taskId = params.get("taskId")[0];
            loadTaskData();
        } else {
            Messagebox.show("Error: No se proporcionó taskId", "Error", 
                Messagebox.OK, Messagebox.ERROR);
        }
    }

    private void loadTaskData() {
        try {
            // Cargar task de Flowable
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            this.processInstanceId = task.getProcessInstanceId();

            // Cargar variables
            Map<String, Object> variables = runtimeService.getVariables(processInstanceId);
            this.evaluationId = (Long) variables.get("evaluation_id");
            this.modelName = (String) variables.get("model_name");
            this.overallScore = (Double) variables.get("overall_score");

            // Calcular gap
            this.gap = String.format("-%.2f", (threshold - overallScore) * 100) + "%";

            // Cargar evaluación desde BBDD
            LlmEvaluation evaluation = businessService.findById(LlmEvaluation.class, evaluationId);

            // Parsear métricas
            if (evaluation.getEvalresults() != null) {
                Map<String, Object> results = parseJson(evaluation.getEvalresults());
                parseMetrics(results);
            }

            // Recomendaciones
            this.recommendations = generateRecommendations();

            log.info("✅ Task data loaded: evaluationId={}, score={}", evaluationId, overallScore);

        } catch (Exception e) {
            log.error("❌ Error cargando task data", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", 
                Messagebox.OK, Messagebox.ERROR);
        }
    }

    private void parseMetrics(Map<String, Object> results) {
        for (Map.Entry<String, Object> entry : results.entrySet()) {
            Double value = ((Number) entry.getValue()).doubleValue();
            boolean passed = value >= 0.80;

            metrics.add(new MetricRow(
                entry.getKey(),
                String.format("%.2f", value),
                "0.80",
                passed
            ));

            if (passed) {
                passedTestCases = (passedTestCases == null ? 0 : passedTestCases) + 1;
            } else {
                failedTestCases = (failedTestCases == null ? 0 : failedTestCases) + 1;
            }
        }

        totalTestCases = metrics.size();
    }

    private String generateRecommendations() {
        List<String> recs = new ArrayList<>();
        
        if (overallScore < 0.70) {
            recs.add("🔴 CRITICAL: Score is significantly below threshold");
            recs.add("Recommended action: REJECT or major model revision");
        } else if (overallScore < 0.80) {
            recs.add("⚠️ WARNING: Score is below threshold but close");
            recs.add("Consider: Re-run with adjusted parameters, or approve with mitigation plan");
        }

        // Métricas específicas con problemas
        List<String> failedMetrics = metrics.stream()
            .filter(m -> !m.passed)
            .map(m -> m.name)
            .collect(Collectors.toList());

        if (!failedMetrics.isEmpty()) {
            recs.add("");
            recs.add("Failed metrics: " + String.join(", ", failedMetrics));
        }

        return String.join("\n", recs);
    }

    @Command
    public void doSubmit() {
        if (action == null || justification == null || justification.trim().length() < 20) {
            Messagebox.show("Por favor completa todos los campos", "Validación", 
                Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        try {
            if (mockMode) {
                log.info("🎭 Mock mode: Simulando submit - action={}", action);
                logActivity("MOCK_SUBMIT_LLM_EVAL", "LlmEvaluation", evaluationId, "Simulación de decisión: " + action);
                Messagebox.show("✅ [DEMO] Decisión registrada exitosamente", "Demo Mode", 
                    Messagebox.OK, Messagebox.INFORMATION,
                    event -> Executions.getCurrent().sendRedirect("/plataforma/workflow/my-tasks.zul?mock=true"));
                return;
            }
            
            String username = getUser() != null ? getUser().getUsername() : "SYSTEM";
            
            // Completar tarea de Flowable
            Map<String, Object> taskVariables = new HashMap<>();
            taskVariables.put("action", action);
            taskVariables.put("justification", justification);
            taskVariables.put("reviewed_by", username);
            taskVariables.put("reviewed_at", new Timestamp(System.currentTimeMillis()).toString());

            taskService.complete(taskId, taskVariables);
            
            logActivity("SUBMIT_LLM_EVAL", "LlmEvaluation", evaluationId, "Decisión: " + action);

            Messagebox.show(
                "Decision submitted successfully",
                "Success",
                Messagebox.OK,
                Messagebox.INFORMATION,
                event -> Executions.getCurrent().sendRedirect("/plataforma/workflow/my-tasks.zul")
            );

        } catch (Exception e) {
            log.error("❌ Error submitting decision", e);
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
        this.taskId = "mock-llm-eval-task-001";
        this.processInstanceId = "mock-process-llm-eval-001";
        this.modelName = "GPT-4-Medical-Assistant";
        this.evaluationId = 1001L;
        this.overallScore = 0.76;
        this.gap = String.format("-%.2f%%", (threshold - overallScore) * 100);
        
        // Mock metrics
        metrics.add(new MetricRow("Accuracy", "0.82", "0.80", true));
        metrics.add(new MetricRow("Toxicity", "0.95", "0.80", true));
        metrics.add(new MetricRow("Bias", "0.72", "0.80", false));
        metrics.add(new MetricRow("Coherence", "0.85", "0.80", true));
        metrics.add(new MetricRow("Relevance", "0.68", "0.80", false));
        
        this.totalTestCases = 5;
        this.passedTestCases = 3;
        this.failedTestCases = 2;
        
        this.recommendations = "⚠️ WARNING: Score is below threshold but close\n" +
                              "Consider: Re-run with adjusted parameters, or approve with mitigation plan\n\n" +
                              "Failed metrics: Bias, Relevance";
        
        log.info("🎭 Mock data loaded for LLM Evaluation Review");
    }

    private Map<String, Object> parseJson(String json) {
        try {
            return new ObjectMapper().readValue(json, Map.class);
        } catch (Exception e) {
            return Map.of();
        }
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
    
    @Getter
    @Setter
    public static class MetricRow {
        private String name;
        private String value;
        private String threshold;
        private boolean passed;

        public MetricRow(String name, String value, String threshold, boolean passed) {
            this.name = name;
            this.value = value;
            this.threshold = threshold;
            this.passed = passed;
        }
    }
}
