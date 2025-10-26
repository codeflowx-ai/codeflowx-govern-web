package com.codeflowx.govern.workflow.delegates;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.evaluation.EvaluationMetric;
import com.codeflowx.govern.entity.evaluation.LlmEvaluation;
import com.fasterxml.jackson.databind.ObjectMapper;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("saveLlmEvaluationResultsDelegate")
public class SaveLlmEvaluationResultsDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void execute(DelegateExecution execution) {
        Long evaluationId = (Long) execution.getVariable("evaluation_id");
        Map<String, Object> results = (Map) execution.getVariable("evaluation_results");

        log.info("💾 Guardando resultados LLM: evaluationId={}", evaluationId);

        try {
            // 1. Actualizar evaluación principal
            LlmEvaluation evaluation = businessService.findById(LlmEvaluation.class, evaluationId);
            evaluation.setEvalstatus("COMPLETED");
            evaluation.setEvalresults(objectMapper.writeValueAsString(results.get("metrics")));
            evaluation.setEvalaggregatedscores(objectMapper.writeValueAsString(results.get("aggregated_scores")));
            evaluation.setEvalcompletedat(Timestamp.from(Instant.now()));

            businessService.save(evaluation);

            // 2. Guardar métricas individuales
            Map<String, Object> metrics = (Map) results.get("metrics");
            for (Map.Entry<String, Object> entry : metrics.entrySet()) {
                EvaluationMetric metric = new EvaluationMetric();
                metric.setMetricname(entry.getKey());
                metric.setMetricvalue(BigDecimal.valueOf(((Number) entry.getValue()).doubleValue()));
                metric.setMetrictype("LLM");
                metric.setThresholdvalue(BigDecimal.valueOf(0.80));
                metric.setPassed(((Number) entry.getValue()).doubleValue() >= 0.80);
                metric.setCreatedat(Timestamp.from(Instant.now()));

                businessService.save(metric);
            }

            // 3. Calcular score general
            Map<String, Object> aggregated = (Map) results.get("aggregated_scores");
            Double overallScore = ((Number) aggregated.get("overall_score")).doubleValue();
            execution.setVariable("overall_score", overallScore);

            log.info("✅ Resultados guardados: score={}", overallScore);

        } catch (Exception e) {
            log.error("❌ Error guardando resultados", e);
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }
}


