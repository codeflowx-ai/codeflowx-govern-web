package com.codeflowx.govern.workflow.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio para detectar sesgos en modelos ML usando inferencia + análisis estadístico
 */
@Slf4j
@Service
public class BiasDetectionService {

    @Autowired
    private ModelInferenceService inferenceService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Detecta sesgos en un modelo ML
     */
    public BiasDetectionResult detectModelBias(Long modelId) {
        log.info("Detectando sesgos para model: {}", modelId);

        try {
            // 1. Ejecutar inferencia en dataset con demographics
            ModelInferenceService.InferenceResult inferenceResult = 
                    inferenceService.runBatchInference(modelId, null, "bias-detection-dataset");

            // 2. Calcular fairness metrics
            FairnessMetrics fairness = calculateFairnessMetrics(inferenceResult);

            // 3. Detectar sesgos
            boolean hasBias = detectBias(fairness);
            int biasScore = calculateBiasScore(fairness);

            // 4. Generar recomendaciones si hay sesgo
            List<String> recommendations = hasBias ? generateBiasRecommendations(fairness) : new ArrayList<>();

            BiasDetectionResult result = BiasDetectionResult.builder()
                    .modelId(modelId)
                    .hasBias(hasBias)
                    .biasScore(biasScore)
                    .fairnessMetrics(fairness)
                    .recommendations(recommendations)
                    .timestamp(System.currentTimeMillis())
                    .build();

            log.info("Bias detection completado: modelId={}, hasBias={}, score={}",
                     modelId, hasBias, biasScore);

            return result;

        } catch (Exception e) {
            log.error("Error en bias detection para model: " + modelId, e);
            throw new RuntimeException("Error detectando sesgos: " + e.getMessage(), e);
        }
    }

    private FairnessMetrics calculateFairnessMetrics(ModelInferenceService.InferenceResult inferenceResult) {
        // Calcular métricas de fairness estándar
        // Demographic Parity, Equal Opportunity, Disparate Impact

        List<ModelInferenceService.Prediction> predictions = inferenceResult.getPredictions();

        // Simular cálculo (en producción, usar librería de fairness)
        double demographicParity = calculateDemographicParity(predictions);
        double equalOpportunity = calculateEqualOpportunity(predictions);
        double disparateImpact = calculateDisparateImpact(predictions);
        double equalizedOdds = calculateEqualizedOdds(predictions);

        return FairnessMetrics.builder()
                .demographicParity(demographicParity)
                .equalOpportunity(equalOpportunity)
                .disparateImpact(disparateImpact)
                .equalizedOdds(equalizedOdds)
                .sampleSize(predictions.size())
                .build();
    }

    private double calculateDemographicParity(List<ModelInferenceService.Prediction> predictions) {
        // TODO: Implementar cálculo real
        // Por ahora retorna valor simulado
        return 0.85;
    }

    private double calculateEqualOpportunity(List<ModelInferenceService.Prediction> predictions) {
        return 0.90;
    }

    private double calculateDisparateImpact(List<ModelInferenceService.Prediction> predictions) {
        return 0.82;
    }

    private double calculateEqualizedOdds(List<ModelInferenceService.Prediction> predictions) {
        return 0.88;
    }

    private boolean detectBias(FairnessMetrics fairness) {
        // Thresholds estándar de fairness
        double DEMOGRAPHIC_PARITY_THRESHOLD = 0.80;
        double DISPARATE_IMPACT_THRESHOLD = 0.80;

        return fairness.getDemographicParity() < DEMOGRAPHIC_PARITY_THRESHOLD ||
               fairness.getDisparateImpact() < DISPARATE_IMPACT_THRESHOLD;
    }

    private int calculateBiasScore(FairnessMetrics fairness) {
        // Score invertido: más bajo = más sesgo
        double avgFairness = (fairness.getDemographicParity() +
                              fairness.getEqualOpportunity() +
                              fairness.getDisparateImpact() +
                              fairness.getEqualizedOdds()) / 4.0;

        return (int) Math.round((1.0 - avgFairness) * 100); // 0-100, mayor = más sesgo
    }

    private List<String> generateBiasRecommendations(FairnessMetrics fairness) {
        List<String> recommendations = new ArrayList<>();

        if (fairness.getDemographicParity() < 0.80) {
            recommendations.add("Balancear dataset para mejorar demographic parity");
            recommendations.add("Considerar técnicas de re-weighting durante entrenamiento");
        }

        if (fairness.getDisparateImpact() < 0.80) {
            recommendations.add("Implementar post-processing para reducir disparate impact");
            recommendations.add("Revisar features que puedan estar correlacionadas con grupos protegidos");
        }

        if (fairness.getEqualOpportunity() < 0.85) {
            recommendations.add("Ajustar thresholds de decisión por grupo demográfico");
        }

        return recommendations;
    }

    // DTOs

    @Data
    @Builder
    public static class BiasDetectionResult {
        private Long modelId;
        private Boolean hasBias;
        private Integer biasScore; // 0-100 (mayor = más sesgo)
        private FairnessMetrics fairnessMetrics;
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
    public static class FairnessMetrics {
        private Double demographicParity; // 0-1 (1 = perfecto)
        private Double equalOpportunity; // 0-1
        private Double disparateImpact; // 0-1
        private Double equalizedOdds; // 0-1
        private Integer sampleSize;
    }
}

