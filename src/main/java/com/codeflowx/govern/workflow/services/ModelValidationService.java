package com.codeflowx.govern.workflow.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio para validar performance de modelos ML
 */
@Slf4j
@Service
public class ModelValidationService {

    @Autowired
    private ModelInferenceService inferenceService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Valida performance de un modelo contra thresholds
     */
    public ValidationResult validatePerformance(Long modelId, Long versionId, Map<String, Double> thresholds) {
        log.info("Validando performance: modelId={}, versionId={}", modelId, versionId);

        try {
            // 1. Ejecutar inferencia en dataset de validación
            ModelInferenceService.InferenceResult inferenceResult = 
                    inferenceService.runBatchInference(modelId, versionId, "validation-dataset");

            // 2. Calcular métricas de performance
            PerformanceMetrics metrics = calculateMetrics(inferenceResult);

            // 3. Validar contra thresholds
            boolean passed = validateAgainstThresholds(metrics, thresholds);

            // 4. Generar recomendaciones si no pasó
            List<String> recommendations = passed ? new ArrayList<>() : generateRecommendations(metrics, thresholds);

            ValidationResult result = ValidationResult.builder()
                    .modelId(modelId)
                    .versionId(versionId)
                    .passed(passed)
                    .metrics(metrics.toMap())
                    .thresholds(thresholds)
                    .recommendations(recommendations)
                    .timestamp(System.currentTimeMillis())
                    .build();

            log.info("Validation completada: passed={}", passed);
            return result;

        } catch (Exception e) {
            log.error("Error validando performance", e);
            throw new RuntimeException("Error en validation: " + e.getMessage(), e);
        }
    }

    private PerformanceMetrics calculateMetrics(ModelInferenceService.InferenceResult inferenceResult) {
        List<ModelInferenceService.Prediction> predictions = inferenceResult.getPredictions();

        // Calcular métricas estándar
        double accuracy = calculateAccuracy(predictions);
        double precision = calculatePrecision(predictions);
        double recall = calculateRecall(predictions);
        double f1Score = 2 * (precision * recall) / (precision + recall);
        double latency = calculateAverageLatency(inferenceResult);

        return PerformanceMetrics.builder()
                .accuracy(accuracy)
                .precision(precision)
                .recall(recall)
                .f1Score(f1Score)
                .latency(latency)
                .sampleSize(predictions.size())
                .build();
    }

    private double calculateAccuracy(List<ModelInferenceService.Prediction> predictions) {
        long correct = predictions.stream()
                .filter(p -> p.getPrediction().equals(p.getGroundTruth()))
                .count();
        return (double) correct / predictions.size();
    }

    private double calculatePrecision(List<ModelInferenceService.Prediction> predictions) {
        // TODO: Implementar para clasificación multi-clase
        return 0.92; // Simulado
    }

    private double calculateRecall(List<ModelInferenceService.Prediction> predictions) {
        // TODO: Implementar para clasificación multi-clase
        return 0.89; // Simulado
    }

    private double calculateAverageLatency(ModelInferenceService.InferenceResult result) {
        return result.getDurationMs() / (double) result.getPredictions().size();
    }

    private boolean validateAgainstThresholds(PerformanceMetrics metrics, Map<String, Double> thresholds) {
        if (thresholds == null || thresholds.isEmpty()) {
            return true; // Sin thresholds, siempre pasa
        }

        boolean passed = true;

        if (thresholds.containsKey("accuracy") && metrics.getAccuracy() < thresholds.get("accuracy")) {
            passed = false;
        }
        if (thresholds.containsKey("precision") && metrics.getPrecision() < thresholds.get("precision")) {
            passed = false;
        }
        if (thresholds.containsKey("recall") && metrics.getRecall() < thresholds.get("recall")) {
            passed = false;
        }
        if (thresholds.containsKey("f1Score") && metrics.getF1Score() < thresholds.get("f1Score")) {
            passed = false;
        }
        if (thresholds.containsKey("latency") && metrics.getLatency() > thresholds.get("latency")) {
            passed = false;
        }

        return passed;
    }

    private List<String> generateRecommendations(PerformanceMetrics metrics, Map<String, Double> thresholds) {
        List<String> recommendations = new ArrayList<>();

        if (thresholds.containsKey("accuracy") && metrics.getAccuracy() < thresholds.get("accuracy")) {
            recommendations.add("Accuracy por debajo del threshold: " + 
                              String.format("%.2f < %.2f", metrics.getAccuracy(), thresholds.get("accuracy")));
            recommendations.add("Considerar: aumentar datos de entrenamiento, ajustar hiperparámetros");
        }

        if (thresholds.containsKey("latency") && metrics.getLatency() > thresholds.get("latency")) {
            recommendations.add("Latencia por encima del threshold: " + 
                              String.format("%.2fms > %.2fms", metrics.getLatency(), thresholds.get("latency")));
            recommendations.add("Considerar: optimización de modelo, quantization, caching");
        }

        return recommendations;
    }

    // DTOs

    @Data
    @Builder
    public static class ValidationResult {
        private Long modelId;
        private Long versionId;
        private Boolean passed;
        private Map<String, Double> metrics;
        private Map<String, Double> thresholds;
        private List<String> recommendations;
        private Long timestamp;

        public String toJson() {
            try {
                return new ObjectMapper().writeValueAsString(this);
            } catch (JsonProcessingException e) {
                return "{}";
            }
        }
    }

    @Data
    @Builder
    public static class PerformanceMetrics {
        private Double accuracy;
        private Double precision;
        private Double recall;
        private Double f1Score;
        private Double latency;
        private Integer sampleSize;

        public Map<String, Double> toMap() {
            Map<String, Double> map = new HashMap<>();
            map.put("accuracy", accuracy);
            map.put("precision", precision);
            map.put("recall", recall);
            map.put("f1Score", f1Score);
            map.put("latency", latency);
            return map;
        }
    }
}

