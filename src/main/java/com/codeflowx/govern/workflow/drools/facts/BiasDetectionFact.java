package com.codeflowx.govern.workflow.drools.facts;

import java.io.Serializable;

/**
 * Fact para el proceso de detección de bias (bias-detection-v1)
 * 
 * INPUT VARIABLES:
 * - demographicBiasScore: Puntuación de bias demográfico (0-100)
 * - geographicBiasScore: Puntuación de bias geográfico (0-100)  
 * - temporalBiasScore: Puntuación de bias temporal (0-100)
 * - modelType: Tipo de modelo (CLASSIFICATION, REGRESSION, LLM, etc.)
 * - datasetSize: Tamaño del dataset
 * - sensitiveAttributes: Atributos sensibles detectados
 * 
 * OUTPUT VARIABLES:
 * - decision: NO_BIAS, BIAS_DETECTED, CRITICAL_BIAS
 * - overallBiasScore: Puntuación general de bias (0-100)
 * - confidenceLevel: Nivel de confianza (HIGH, MEDIUM, LOW)
 * - justification: Justificación de la decisión
 * - requiresMitigation: Si requiere plan de mitigación
 * - riskLevel: Nivel de riesgo (LOW, MEDIUM, HIGH, CRITICAL)
 */
public class BiasDetectionFact implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // INPUT VARIABLES
    private Double demographicBiasScore;
    private Double geographicBiasScore;
    private Double temporalBiasScore;
    private String modelType;
    private Long datasetSize;
    private String sensitiveAttributes;
    
    // OUTPUT VARIABLES
    private String decision;
    private Double overallBiasScore;
    private String confidenceLevel;
    private String justification;
    private Boolean requiresMitigation;
    private String riskLevel;
    
    // CONSTRUCTORS
    public BiasDetectionFact() {}
    
    public BiasDetectionFact(Double demographicBiasScore, Double geographicBiasScore, 
                           Double temporalBiasScore, String modelType, Long datasetSize) {
        this.demographicBiasScore = demographicBiasScore;
        this.geographicBiasScore = geographicBiasScore;
        this.temporalBiasScore = temporalBiasScore;
        this.modelType = modelType;
        this.datasetSize = datasetSize;
    }
    
    // GETTERS AND SETTERS
    public Double getDemographicBiasScore() {
        return demographicBiasScore;
    }
    
    public void setDemographicBiasScore(Double demographicBiasScore) {
        this.demographicBiasScore = demographicBiasScore;
    }
    
    public Double getGeographicBiasScore() {
        return geographicBiasScore;
    }
    
    public void setGeographicBiasScore(Double geographicBiasScore) {
        this.geographicBiasScore = geographicBiasScore;
    }
    
    public Double getTemporalBiasScore() {
        return temporalBiasScore;
    }
    
    public void setTemporalBiasScore(Double temporalBiasScore) {
        this.temporalBiasScore = temporalBiasScore;
    }
    
    public String getModelType() {
        return modelType;
    }
    
    public void setModelType(String modelType) {
        this.modelType = modelType;
    }
    
    public Long getDatasetSize() {
        return datasetSize;
    }
    
    public void setDatasetSize(Long datasetSize) {
        this.datasetSize = datasetSize;
    }
    
    public String getSensitiveAttributes() {
        return sensitiveAttributes;
    }
    
    public void setSensitiveAttributes(String sensitiveAttributes) {
        this.sensitiveAttributes = sensitiveAttributes;
    }
    
    public String getDecision() {
        return decision;
    }
    
    public void setDecision(String decision) {
        this.decision = decision;
    }
    
    public Double getOverallBiasScore() {
        return overallBiasScore;
    }
    
    public void setOverallBiasScore(Double overallBiasScore) {
        this.overallBiasScore = overallBiasScore;
    }
    
    public String getConfidenceLevel() {
        return confidenceLevel;
    }
    
    public void setConfidenceLevel(String confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }
    
    public String getJustification() {
        return justification;
    }
    
    public void setJustification(String justification) {
        this.justification = justification;
    }
    
    public Boolean getRequiresMitigation() {
        return requiresMitigation;
    }
    
    public void setRequiresMitigation(Boolean requiresMitigation) {
        this.requiresMitigation = requiresMitigation;
    }
    
    public String getRiskLevel() {
        return riskLevel;
    }
    
    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }
    
    @Override
    public String toString() {
        return "BiasDetectionFact{" +
                "demographicBiasScore=" + demographicBiasScore +
                ", geographicBiasScore=" + geographicBiasScore +
                ", temporalBiasScore=" + temporalBiasScore +
                ", modelType='" + modelType + '\'' +
                ", datasetSize=" + datasetSize +
                ", decision='" + decision + '\'' +
                ", overallBiasScore=" + overallBiasScore +
                ", confidenceLevel='" + confidenceLevel + '\'' +
                ", riskLevel='" + riskLevel + '\'' +
                '}';
    }
}