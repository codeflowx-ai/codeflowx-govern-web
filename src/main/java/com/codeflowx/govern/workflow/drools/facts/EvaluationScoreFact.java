package com.codeflowx.govern.workflow.drools.facts;

import lombok.Data;

/**
 * Fact para Drools - LLM/RAG/Model Evaluation Scoring
 */
@Data
public class EvaluationScoreFact {
    
    // Scores
    private Double overallScore;
    private Double threshold;
    private Integer testCasesPassed;
    private Integer testCasesTotal;
    private Double passRate;
    
    // Metadata
    private String evaluationType;  // LLM, RAG, MODEL
    private String modelName;
    private String dataset;
    
    // Resultado calculado por Drools
    private String decision;  // AUTO_APPROVE, REQUIRES_REVIEW, AUTO_REJECT
    private Boolean meetsThreshold;
    private String recommendation;
    private String actionRequired;
}


