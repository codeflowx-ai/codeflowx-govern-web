package com.codeflowx.govern.workflow.delegates.dataset;

import com.codeflowx.govern.workflow.drools.facts.DatasetQualityFact;
import com.codeflowx.govern.workflow.services.DroolsRulesService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Delegate: Calculate Dataset Score (con Drools)
 * 
 * Ejecuta reglas Drools para determinar la decisión final sobre el dataset.
 * 
 * Drools Rules File: dataset-quality-scoring.drl
 * 
 * Responsabilidades:
 * - Crear DatasetQualityFact con todas las métricas
 * - Ejecutar reglas Drools
 * - Obtener decisión y justificación
 * - Guardar resultado en variables BPMN
 */
@Slf4j
@Component("calculateDatasetScoreDelegate")
public class CalculateDatasetScoreDelegate implements JavaDelegate {

    @Autowired
    private DroolsRulesService droolsService;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            log.info("🎯 Calculating Dataset Score with Drools...");

            // 1. Crear Fact con todas las métricas
            DatasetQualityFact fact = new DatasetQualityFact();

            // Identificación
            fact.setDatasetId((String) execution.getVariable("datasetId"));
            fact.setDatasetName((String) execution.getVariable("datasetName"));
            fact.setDatasetVersion((String) execution.getVariable("datasetVersion"));
            fact.setEvaluatorId((String) execution.getVariable("evaluatorId"));

            // Quality scores
            fact.setOverallQualityScore(getDoubleVar(execution, "qualityScore", 0.0));
            fact.setCompletenessScore(getDoubleVar(execution, "completenessScore", 0.0));
            fact.setValidityScore(getDoubleVar(execution, "validityScore", 0.0));
            fact.setUniquenessScore(getDoubleVar(execution, "uniquenessScore", 0.0));
            fact.setConsistencyScore(getDoubleVar(execution, "consistencyScore", 1.0));
            fact.setAccuracyScore(getDoubleVar(execution, "accuracyScore", 0.0));

            // Bias/Fairness
            fact.setBiasScore(getDoubleVar(execution, "biasScore", 0.0));
            fact.setFairnessScore(getDoubleVar(execution, "fairnessScore", 0.0));

            // PII/Compliance
            fact.setPiiDetected(getBooleanVar(execution, "piiDetected", false));
            fact.setPiiCount(getIntegerVar(execution, "piiCount", 0));
            fact.setGdprCompliant(getBooleanVar(execution, "gdprCompliant", true));

            // Estadísticas
            fact.setTotalRecords(getIntegerVar(execution, "totalRecords", 0));
            fact.setDuplicateRate(getDoubleVar(execution, "duplicateRate", 0.0));
            fact.setNullRate(getDoubleVar(execution, "nullRate", 0.0));
            fact.setOutlierPercentage(getDoubleVar(execution, "outlierPercentage", 0.0));

            log.info("📊 Dataset Metrics Summary:");
            log.info("   - Quality Score: {}", fact.getOverallQualityScore());
            log.info("   - Completeness: {}", fact.getCompletenessScore());
            log.info("   - Validity: {}", fact.getValidityScore());
            log.info("   - Bias Score: {}", fact.getBiasScore());
            log.info("   - PII Detected: {}", fact.isPiiDetected());
            log.info("   - GDPR Compliant: {}", fact.isGdprCompliant());

            // 2. Ejecutar reglas Drools
            droolsService.executeDatasetQualityRules(fact);

            // 3. Obtener resultados de Drools
            String decision = fact.getDecision();
            Boolean requiresReview = fact.isRequiresReview();
            String justification = fact.getJustification();
            String riskLevel = fact.getRiskLevel();

            log.info("✅ Drools Decision: {}", decision);
            log.info("   - Requires Review: {}", requiresReview);
            log.info("   - Risk Level: {}", riskLevel);
            log.info("   - Justification: {}", justification);

            // 4. Guardar en variables BPMN
            execution.setVariable("decision", decision);
            execution.setVariable("requiresReview", requiresReview);
            execution.setVariable("justification", justification);
            execution.setVariable("riskLevel", riskLevel);

            // Para gateways
            execution.setVariable("isApproved", "APPROVED".equals(decision));
            execution.setVariable("isRejected", "REJECTED".equals(decision));
            execution.setVariable("needsReview", requiresReview);

            log.info("✅ CalculateDatasetScoreDelegate completed successfully");

        } catch (Exception e) {
            log.error("❌ Error in CalculateDatasetScoreDelegate", e);
            // Fallback: requiere revisión humana
            execution.setVariable("decision", "REVIEW_REQUIRED");
            execution.setVariable("requiresReview", true);
            execution.setVariable("justification", "Error in scoring: " + e.getMessage());
            throw new RuntimeException("Dataset scoring failed: " + e.getMessage(), e);
        }
    }

    // Helper methods
    private Double getDoubleVar(DelegateExecution execution, String varName) {
        Object value = execution.getVariable(varName);
        if (value == null) return 0.0;
        if (value instanceof Double) return (Double) value;
        if (value instanceof Number) return ((Number) value).doubleValue();
        return Double.parseDouble(value.toString());
    }

    private Double getDoubleVar(DelegateExecution execution, String varName, Double defaultValue) {
        Object value = execution.getVariable(varName);
        if (value == null) return defaultValue;
        return getDoubleVar(execution, varName);
    }

    private Integer getIntegerVar(DelegateExecution execution, String varName, Integer defaultValue) {
        Object value = execution.getVariable(varName);
        if (value == null) return defaultValue;
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Number) return ((Number) value).intValue();
        return Integer.parseInt(value.toString());
    }

    private Long getLongVar(DelegateExecution execution, String varName, Long defaultValue) {
        Object value = execution.getVariable(varName);
        if (value == null) return defaultValue;
        if (value instanceof Long) return (Long) value;
        if (value instanceof Number) return ((Number) value).longValue();
        return Long.parseLong(value.toString());
    }

    private Boolean getBooleanVar(DelegateExecution execution, String varName, Boolean defaultValue) {
        Object value = execution.getVariable(varName);
        if (value == null) return defaultValue;
        if (value instanceof Boolean) return (Boolean) value;
        return Boolean.parseBoolean(value.toString());
    }

    private String getStringVar(DelegateExecution execution, String varName, String defaultValue) {
        Object value = execution.getVariable(varName);
        if (value == null) return defaultValue;
        return value.toString();
    }
}

