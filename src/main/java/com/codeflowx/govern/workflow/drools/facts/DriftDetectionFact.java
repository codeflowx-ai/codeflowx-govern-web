package com.codeflowx.govern.workflow.drools.facts;

import java.io.Serializable;

/**
 * Fact para el proceso de detección de drift (drift-detection-v1)
 * 
 * INPUT VARIABLES:
 * - driftScore: Puntuación de drift (0-100)
 * - driftType: Tipo de drift (DATA, CONCEPT, PREDICTION)
 * - timeWindow: Ventana temporal (horas)
 * - sampleSize: Tamaño de muestra
 * 
 * OUTPUT VARIABLES:
 * - decision: NO_DRIFT, DRIFT_DETECTED, CRITICAL_DRIFT
 * - severity: LOW, MEDIUM, HIGH, CRITICAL
 * - confidenceLevel: Nivel de confianza (HIGH, MEDIUM, LOW)
 * - justification: Justificación de la decisión
 * - requiresRetraining: Si requiere reentrenamiento
 * - recommendedAction: Acción recomendada
 */
public class DriftDetectionFact implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // INPUT VARIABLES
    private Double driftScore;
    private String driftType;
    private Integer timeWindow;
    private Long sampleSize;
    
    // OUTPUT VARIABLES
    private String decision;
    private String severity;
    private String confidenceLevel;
    private String justification;
    private Boolean requiresRetraining;
    private String recommendedAction;
    
    // CONSTRUCTORS
    public DriftDetectionFact() {}
    
    public DriftDetectionFact(Double driftScore, String driftType, Integer timeWindow, Long sampleSize) {
        this.driftScore = driftScore;
        this.driftType = driftType;
        this.timeWindow = timeWindow;
        this.sampleSize = sampleSize;
    }
    
    // GETTERS AND SETTERS
    public Double getDriftScore() {
        return driftScore;
    }
    
    public void setDriftScore(Double driftScore) {
        this.driftScore = driftScore;
    }
    
    public String getDriftType() {
        return driftType;
    }
    
    public void setDriftType(String driftType) {
        this.driftType = driftType;
    }
    
    public Integer getTimeWindow() {
        return timeWindow;
    }
    
    public void setTimeWindow(Integer timeWindow) {
        this.timeWindow = timeWindow;
    }
    
    public Long getSampleSize() {
        return sampleSize;
    }
    
    public void setSampleSize(Long sampleSize) {
        this.sampleSize = sampleSize;
    }
    
    public String getDecision() {
        return decision;
    }
    
    public void setDecision(String decision) {
        this.decision = decision;
    }
    
    public String getSeverity() {
        return severity;
    }
    
    public void setSeverity(String severity) {
        this.severity = severity;
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
    
    public Boolean getRequiresRetraining() {
        return requiresRetraining;
    }
    
    public void setRequiresRetraining(Boolean requiresRetraining) {
        this.requiresRetraining = requiresRetraining;
    }
    
    public String getRecommendedAction() {
        return recommendedAction;
    }
    
    public void setRecommendedAction(String recommendedAction) {
        this.recommendedAction = recommendedAction;
    }
    
    @Override
    public String toString() {
        return "DriftDetectionFact{" +
                "driftScore=" + driftScore +
                ", driftType='" + driftType + '\'' +
                ", timeWindow=" + timeWindow +
                ", decision='" + decision + '\'' +
                ", severity='" + severity + '\'' +
                ", requiresRetraining=" + requiresRetraining +
                '}';
    }
}