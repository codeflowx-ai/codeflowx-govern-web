package com.codeflowx.govern.workflow.drools.facts;

import java.io.Serializable;

/**
 * Fact para el proceso de evaluación de LLM (llm-evaluation-v1)
 * 
 * INPUT VARIABLES:
 * - overallScore: Puntuación general (0-100)
 * - accuracyScore: Precisión (0-100)
 * - toxicityScore: Toxicidad (0-100, menor es mejor)
 * - hallucinationScore: Alucinaciones (0-100, menor es mejor)
 * - biasScore: Sesgo (0-100, menor es mejor)
 * 
 * OUTPUT VARIABLES:
 * - decision: PASSED, REVIEW_REQUIRED, FAILED
 * - confidenceLevel: Nivel de confianza (HIGH, MEDIUM, LOW)
 * - justification: Justificación de la decisión
 * - requiresReview: Si requiere revisión humana
 */
public class LlmEvaluationFact implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // INPUT VARIABLES
    private Double overallScore;
    private Double accuracyScore;
    private Double toxicityScore;
    private Double hallucinationScore;
    private Double biasScore;
    
    // OUTPUT VARIABLES
    private String decision;
    private String confidenceLevel;
    private String justification;
    private Boolean requiresReview;
    
    // CONSTRUCTORS
    public LlmEvaluationFact() {}
    
    public LlmEvaluationFact(Double overallScore, Double accuracyScore, Double toxicityScore,
                           Double hallucinationScore, Double biasScore) {
        this.overallScore = overallScore;
        this.accuracyScore = accuracyScore;
        this.toxicityScore = toxicityScore;
        this.hallucinationScore = hallucinationScore;
        this.biasScore = biasScore;
    }
    
    // GETTERS AND SETTERS
    public Double getOverallScore() {
        return overallScore;
    }
    
    public void setOverallScore(Double overallScore) {
        this.overallScore = overallScore;
    }
    
    public Double getAccuracyScore() {
        return accuracyScore;
    }
    
    public void setAccuracyScore(Double accuracyScore) {
        this.accuracyScore = accuracyScore;
    }
    
    public Double getToxicityScore() {
        return toxicityScore;
    }
    
    public void setToxicityScore(Double toxicityScore) {
        this.toxicityScore = toxicityScore;
    }
    
    public Double getHallucinationScore() {
        return hallucinationScore;
    }
    
    public void setHallucinationScore(Double hallucinationScore) {
        this.hallucinationScore = hallucinationScore;
    }
    
    public Double getBiasScore() {
        return biasScore;
    }
    
    public void setBiasScore(Double biasScore) {
        this.biasScore = biasScore;
    }
    
    public String getDecision() {
        return decision;
    }
    
    public void setDecision(String decision) {
        this.decision = decision;
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
    
    public Boolean getRequiresReview() {
        return requiresReview;
    }
    
    public void setRequiresReview(Boolean requiresReview) {
        this.requiresReview = requiresReview;
    }
    
    @Override
    public String toString() {
        return "LlmEvaluationFact{" +
                "overallScore=" + overallScore +
                ", accuracyScore=" + accuracyScore +
                ", toxicityScore=" + toxicityScore +
                ", hallucinationScore=" + hallucinationScore +
                ", biasScore=" + biasScore +
                ", decision='" + decision + '\'' +
                ", requiresReview=" + requiresReview +
                '}';
    }
}
