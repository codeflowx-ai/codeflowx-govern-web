package com.codeflowx.govern.workflow.delegates;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.codeflowx.govern.entity.evaluation.LlmEvaluation;
import com.fasterxml.jackson.databind.ObjectMapper;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component("executeLlmEvaluationDelegate")
public class ExecuteLlmEvaluationDelegate implements JavaDelegate {

    @Autowired
    @Qualifier("evaluationClient")
    private WebClient evaluationClient;

    @Autowired
    private BusinessService businessService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void execute(DelegateExecution execution) {
        Long evaluationId = (Long) execution.getVariable("evaluation_id");
        Integer testCases = (Integer) execution.getVariable("test_cases");
        
        log.info("🔄 Ejecutando evaluación LLM: evaluationId={}, testCases={}", evaluationId, testCases);

        try {
            // Actualizar estado
            LlmEvaluation evaluation = businessService.findById(LlmEvaluation.class, evaluationId);
            evaluation.setEvalstatus("IN_PROGRESS");
            evaluation.setEvalstartedat(Timestamp.from(Instant.now()));
            businessService.save(evaluation);

            // Preparar request para servidor Python
            Map<String, Object> request = Map.of(
                "model_id", execution.getVariable("model_id"),
                "model_endpoint", execution.getVariable("model_endpoint"),
                "evaluation_type", "text_generation",
                "dataset", execution.getVariable("dataset"),
                "metrics", execution.getVariable("metrics"),
                "test_cases", testCases,
                "async", testCases > 100  // >100 casos = asíncrono
            );

            // Llamar a servidor Python
            Mono<Map> responseMono = evaluationClient.post()
                .uri("/api/v1/evaluation/llm/run")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofMinutes(10));

            Map<String, Object> response = responseMono.block();

            if (testCases > 100) {
                // ASÍNCRONO: Guardar task_id y esperar RabbitMQ
                String taskId = (String) response.get("task_id");
                execution.setVariable("task_id", taskId);
                execution.setVariable("is_async", true);

                evaluation.setEvaltestcases(objectMapper.writeValueAsString(Map.of("task_id", taskId)));
                businessService.save(evaluation);

                log.info("⏳ Evaluación ASÍNCRONA iniciada: taskId={}", taskId);

            } else {
                // SÍNCRONO: Procesar resultados inmediatamente
                execution.setVariable("is_async", false);
                execution.setVariable("evaluation_results", response);

                saveResults(evaluation, response);

                log.info("✅ Evaluación SÍNCRONA completada");
            }

        } catch (Exception e) {
            log.error("❌ Error ejecutando evaluación LLM", e);
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }

    private void saveResults(LlmEvaluation evaluation, Map<String, Object> response) throws Exception {
        evaluation.setEvalstatus("COMPLETED");
        evaluation.setEvalcompletedat(Timestamp.from(Instant.now()));
        evaluation.setEvalresults(objectMapper.writeValueAsString(response.get("metrics")));
        evaluation.setEvalaggregatedscores(objectMapper.writeValueAsString(Map.of(
            "overall_score", response.get("overall_score")
        )));

        Double executionTime = (Double) response.get("execution_time");
        evaluation.setEvaldurationseconds(executionTime.longValue());

        businessService.save(evaluation);
    }
}


