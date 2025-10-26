package com.codeflowx.govern.workflow.delegates;

import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.workflow.services.RagEvaluationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("ragEvaluationDelegate")
public class RagEvaluationDelegate implements JavaDelegate {

    @Autowired
    private RagEvaluationService ragEvaluationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void execute(DelegateExecution execution) {
        Long ragId = (Long) execution.getVariable("ragId");
        String evaluationType = execution.getVariable("evaluationType") != null ? 
                               (String) execution.getVariable("evaluationType") : "SCHEDULED";

        log.info("Ejecutando evaluación de RAG ID: {}", ragId);

        try {
            Map<String, Object> result = ragEvaluationService.evaluateRag(ragId, evaluationType);
            
            Double overallScore = (Double) result.get("overallScore");
            Boolean belowThreshold = (Boolean) result.get("belowThreshold");

            execution.setVariable("overallScore", overallScore != null ? overallScore : 0.0);
            execution.setVariable("belowThreshold", Boolean.TRUE.equals(belowThreshold));
            execution.setVariable("evaluationResultJson", objectMapper.writeValueAsString(result));
            execution.setVariable("evaluationTimestamp", System.currentTimeMillis());

            log.info("Evaluación RAG completada - Score: {}, Below Threshold: {}", overallScore, belowThreshold);

        } catch (Exception e) {
            log.error("Error en evaluación RAG", e);
            execution.setVariable("error", e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }
}


