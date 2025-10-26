package com.codeflowx.govern.workflow.drools.facts;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Fact object para evaluación de RAG (Retrieval-Augmented Generation)
 * Utilizado en el proceso de evaluación de calidad de respuestas RAG
 */
public class RagEvaluationFact {
    
    // Identificadores
    private String ragId;
    private String modelId;
    private String sessionId;
    
    // Métricas de evaluación
    private double relevanceScore;        // Relevancia de la respuesta (0-1)
    private double accuracyScore;         // Precisión de la respuesta (0-1)
    private double completenessScore;     // Completitud de la respuesta (0-1)
    private double coherenceScore;        // Coherencia de la respuesta (0-1)
    private double factualConsistencyScore; // Consistencia factual (0-1)
    
    // Métricas de rendimiento
    private long responseTimeMs;          // Tiempo de respuesta en ms
    private int retrievedDocumentsCount;  // Número de documentos recuperados
    private double retrievalPrecision;    // Precisión de recuperación (0-1)
    private double retrievalRecall;       // Recall de recuperación (0-1)
    
    // Evaluación de contexto
    private double contextRelevanceScore; // Relevancia del contexto (0-1)
    private double contextCoverageScore;  // Cobertura del contexto (0-1)
    private List<String> missingContexts; // Contextos faltantes
    
    // Evaluación de generación
    private double fluencyScore;          // Fluidez del texto (0-1)
    private double originalityScore;      // Originalidad de la respuesta (0-1)
    private int hallucinationCount;       // Número de alucinaciones detectadas
    private List<String> hallucinationTypes; // Tipos de alucinaciones
    
    // Puntuación final calculada
    private double overallScore;          // Puntuación general (0-1)
    private String qualityGrade;          // Grado de calidad (A, B, C, D, F)
    private boolean belowThreshold;       // Si está por debajo del umbral
    
    // Configuración de umbrales
    private double relevanceThreshold;    // Umbral de relevancia (default: 0.7)
    private double accuracyThreshold;     // Umbral de precisión (default: 0.8)
    private double overallThreshold;      // Umbral general (default: 0.75)
    
    // Metadatos
    private LocalDateTime evaluationDate;
    private String evaluatorId;
    private String evaluationMethod;      // "automatic", "human", "hybrid"
    
    // Resultados de la evaluación
    private List<String> recommendations; // Recomendaciones de mejora
    private String riskLevel;             // "LOW", "MEDIUM", "HIGH", "CRITICAL"
    private boolean requiresReview;       // Si requiere revisión manual
    
    // Constructores
    public RagEvaluationFact() {
        this.relevanceThreshold = 0.7;
        this.accuracyThreshold = 0.8;
        this.overallThreshold = 0.75;
        this.evaluationDate = LocalDateTime.now();
        this.evaluationMethod = "automatic";
        this.riskLevel = "LOW";
        this.requiresReview = false;
    }
    
    public RagEvaluationFact(String ragId, String modelId) {
        this();
        this.ragId = ragId;
        this.modelId = modelId;
    }
    
    // Métodos de cálculo
    public void calculateOverallScore() {
        // Cálculo ponderado de la puntuación general
        this.overallScore = (
            relevanceScore * 0.25 +
            accuracyScore * 0.25 +
            completenessScore * 0.20 +
            coherenceScore * 0.15 +
            factualConsistencyScore * 0.15
        );
        
        // Ajuste por rendimiento
        if (responseTimeMs > 5000) { // Más de 5 segundos
            this.overallScore *= 0.9;
        }
        
        // Ajuste por alucinaciones
        if (hallucinationCount > 0) {
            this.overallScore *= (1.0 - (hallucinationCount * 0.1));
        }
    }
    
    public void determineQualityGrade() {
        if (overallScore >= 0.9) {
            this.qualityGrade = "A";
        } else if (overallScore >= 0.8) {
            this.qualityGrade = "B";
        } else if (overallScore >= 0.7) {
            this.qualityGrade = "C";
        } else if (overallScore >= 0.6) {
            this.qualityGrade = "D";
        } else {
            this.qualityGrade = "F";
        }
    }
    
    public void checkThresholds() {
        this.belowThreshold = (
            relevanceScore < relevanceThreshold ||
            accuracyScore < accuracyThreshold ||
            overallScore < overallThreshold
        );
    }
    
    public void determineRiskLevel() {
        if (overallScore < 0.5 || hallucinationCount > 3) {
            this.riskLevel = "CRITICAL";
        } else if (overallScore < 0.6 || hallucinationCount > 1) {
            this.riskLevel = "HIGH";
        } else if (overallScore < 0.7 || hallucinationCount > 0) {
            this.riskLevel = "MEDIUM";
        } else {
            this.riskLevel = "LOW";
        }
    }
    
    // Getters y Setters
    public String getRagId() { return ragId; }
    public void setRagId(String ragId) { this.ragId = ragId; }
    
    public String getModelId() { return modelId; }
    public void setModelId(String modelId) { this.modelId = modelId; }
    
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    
    public double getRelevanceScore() { return relevanceScore; }
    public void setRelevanceScore(double relevanceScore) { this.relevanceScore = relevanceScore; }
    
    public double getAccuracyScore() { return accuracyScore; }
    public void setAccuracyScore(double accuracyScore) { this.accuracyScore = accuracyScore; }
    
    public double getCompletenessScore() { return completenessScore; }
    public void setCompletenessScore(double completenessScore) { this.completenessScore = completenessScore; }
    
    public double getCoherenceScore() { return coherenceScore; }
    public void setCoherenceScore(double coherenceScore) { this.coherenceScore = coherenceScore; }
    
    public double getFactualConsistencyScore() { return factualConsistencyScore; }
    public void setFactualConsistencyScore(double factualConsistencyScore) { this.factualConsistencyScore = factualConsistencyScore; }
    
    public long getResponseTimeMs() { return responseTimeMs; }
    public void setResponseTimeMs(long responseTimeMs) { this.responseTimeMs = responseTimeMs; }
    
    public int getRetrievedDocumentsCount() { return retrievedDocumentsCount; }
    public void setRetrievedDocumentsCount(int retrievedDocumentsCount) { this.retrievedDocumentsCount = retrievedDocumentsCount; }
    
    public double getRetrievalPrecision() { return retrievalPrecision; }
    public void setRetrievalPrecision(double retrievalPrecision) { this.retrievalPrecision = retrievalPrecision; }
    
    public double getRetrievalRecall() { return retrievalRecall; }
    public void setRetrievalRecall(double retrievalRecall) { this.retrievalRecall = retrievalRecall; }
    
    public double getContextRelevanceScore() { return contextRelevanceScore; }
    public void setContextRelevanceScore(double contextRelevanceScore) { this.contextRelevanceScore = contextRelevanceScore; }
    
    public double getContextCoverageScore() { return contextCoverageScore; }
    public void setContextCoverageScore(double contextCoverageScore) { this.contextCoverageScore = contextCoverageScore; }
    
    public List<String> getMissingContexts() { return missingContexts; }
    public void setMissingContexts(List<String> missingContexts) { this.missingContexts = missingContexts; }
    
    public double getFluencyScore() { return fluencyScore; }
    public void setFluencyScore(double fluencyScore) { this.fluencyScore = fluencyScore; }
    
    public double getOriginalityScore() { return originalityScore; }
    public void setOriginalityScore(double originalityScore) { this.originalityScore = originalityScore; }
    
    public int getHallucinationCount() { return hallucinationCount; }
    public void setHallucinationCount(int hallucinationCount) { this.hallucinationCount = hallucinationCount; }
    
    public List<String> getHallucinationTypes() { return hallucinationTypes; }
    public void setHallucinationTypes(List<String> hallucinationTypes) { this.hallucinationTypes = hallucinationTypes; }
    
    public double getOverallScore() { return overallScore; }
    public void setOverallScore(double overallScore) { this.overallScore = overallScore; }
    
    public String getQualityGrade() { return qualityGrade; }
    public void setQualityGrade(String qualityGrade) { this.qualityGrade = qualityGrade; }
    
    public boolean isBelowThreshold() { return belowThreshold; }
    public void setBelowThreshold(boolean belowThreshold) { this.belowThreshold = belowThreshold; }
    
    public double getRelevanceThreshold() { return relevanceThreshold; }
    public void setRelevanceThreshold(double relevanceThreshold) { this.relevanceThreshold = relevanceThreshold; }
    
    public double getAccuracyThreshold() { return accuracyThreshold; }
    public void setAccuracyThreshold(double accuracyThreshold) { this.accuracyThreshold = accuracyThreshold; }
    
    public double getOverallThreshold() { return overallThreshold; }
    public void setOverallThreshold(double overallThreshold) { this.overallThreshold = overallThreshold; }
    
    public LocalDateTime getEvaluationDate() { return evaluationDate; }
    public void setEvaluationDate(LocalDateTime evaluationDate) { this.evaluationDate = evaluationDate; }
    
    public String getEvaluatorId() { return evaluatorId; }
    public void setEvaluatorId(String evaluatorId) { this.evaluatorId = evaluatorId; }
    
    public String getEvaluationMethod() { return evaluationMethod; }
    public void setEvaluationMethod(String evaluationMethod) { this.evaluationMethod = evaluationMethod; }
    
    public List<String> getRecommendations() { return recommendations; }
    public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }
    
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    
    public boolean isRequiresReview() { return requiresReview; }
    public void setRequiresReview(boolean requiresReview) { this.requiresReview = requiresReview; }
    
    @Override
    public String toString() {
        return "RagEvaluationFact{" +
                "ragId='" + ragId + '\'' +
                ", modelId='" + modelId + '\'' +
                ", overallScore=" + overallScore +
                ", qualityGrade='" + qualityGrade + '\'' +
                ", riskLevel='" + riskLevel + '\'' +
                ", belowThreshold=" + belowThreshold +
                '}';
    }
}