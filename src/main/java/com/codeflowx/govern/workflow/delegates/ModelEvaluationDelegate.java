package com.codeflowx.govern.workflow.delegates;

import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.workflow.services.ModelEvaluationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Delegate para ejecutar evaluación de modelos LLM
 */
@Slf4j
@Component("modelEvaluationDelegate")
public class ModelEvaluationDelegate implements JavaDelegate {

    @Autowired
    private ModelEvaluationService modelEvaluationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void execute(DelegateExecution execution) {
        Long modelId = (Long) execution.getVariable("modelId");
        String evaluationType = execution.getVariable("evaluationType") != null ? 
                               (String) execution.getVariable("evaluationType") : "SCHEDULED";

        log.info("Ejecutando evaluación de modelo ID: {}, tipo: {}", modelId, evaluationType);

        try {
            // Ejecutar evaluación
            Map<String, Object> evaluationResult = modelEvaluationService.evaluateModel(modelId, evaluationType);
            
            // Extraer resultados
            Double performanceScore = (Double) evaluationResult.get("performanceScore");
            Double accuracyScore = (Double) evaluationResult.get("accuracyScore");
            Double f1Score = (Double) evaluationResult.get("f1Score");
            Boolean driftDetected = (Boolean) evaluationResult.get("driftDetected");
            Boolean belowThreshold = (Boolean) evaluationResult.get("belowThreshold");

            // Guardar en variables del proceso
            execution.setVariable("performanceScore", performanceScore != null ? performanceScore : 0.0);
            execution.setVariable("accuracyScore", accuracyScore != null ? accuracyScore : 0.0);
            execution.setVariable("f1Score", f1Score != null ? f1Score : 0.0);
            execution.setVariable("driftDetected", Boolean.TRUE.equals(driftDetected));
            execution.setVariable("belowThreshold", Boolean.TRUE.equals(belowThreshold));
            execution.setVariable("evaluationResultJson", objectMapper.writeValueAsString(evaluationResult));
            execution.setVariable("evaluationTimestamp", System.currentTimeMillis());

            log.info("Evaluación completada - Performance: {}, Drift: {}, Below Threshold: {}",
                     performanceScore, driftDetected, belowThreshold);

        } catch (Exception e) {
            log.error("Error ejecutando evaluación de modelo", e);
            execution.setVariable("error", e.getMessage());
            execution.setVariable("belowThreshold", false);
            throw new RuntimeException("Error en evaluación: " + e.getMessage(), e);
        }
    }
}


