package com.codeflowx.govern.workflow.drools.facts;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Fact object para decisión de reentrenamiento de modelos ML
 * Utilizado en el proceso de reentrenamiento automático con governance
 */
public class ModelRetrainingFact {
    
    // Identificadores
    private String modelId;
    private String modelName;
    private String modelVersion;
    private String triggerReason;        // "DRIFT", "DEGRADATION", "SCHEDULED", "MANUAL"
    
    // Métricas actuales del modelo
    private double currentAccuracy;
    private double currentPrecision;
    private double currentRecall;
    private double currentF1Score;
    private double driftScore;           // 0-1, 0=sin drift
    private double performanceDegradation; // Porcentaje de degradación
    
    // Comparación con baseline
    private double baselineAccuracy;
    private double accuracyDrop;         // Diferencia vs baseline
    private double performanceRatio;     // Current/Baseline
    
    // Métricas de datos
    private int trainingDataSize;
    private int newDataAvailable;
    private double dataQualityScore;     // 0-1
    private boolean dataSchemaChanged;
    private double dataDriftScore;       // 0-1
    
    // Métricas de recursos
    private double estimatedTrainingTime; // En horas
    private double estimatedCost;         // En USD
    private String availableInfrastructure; // "GPU", "CPU", "SPOT", "ON_DEMAND"
    private boolean spotInstancesAvailable;
    
    // Configuración de reentrenamiento
    private String retrainingStrategy;   // "INCREMENTAL", "FULL", "TRANSFER_LEARNING"
    private boolean useAutomatedHPO;     // Hyperparameter optimization
    private int maxTrainingEpochs;
    private double earlyStoppingThreshold;
    
    // Decisión calculada
    private String decision;             // "INCREMENTAL_RETRAIN", "FULL_RETRAIN", "SKIP", "HITL_REQUIRED"
    private int priorityScore;           // 1-10 (10=urgente)
    private String urgencyLevel;         // "LOW", "MEDIUM", "HIGH", "CRITICAL"
    
    // A/B Testing
    private boolean enableABTesting;
    private double abTestingTrafficSplit; // Porcentaje de tráfico para challenger
    private int abTestingDurationDays;
    
    // Governance
    private boolean requiresApproval;
    private List<String> approvalGroups;
    private String riskLevel;            // "LOW", "MEDIUM", "HIGH", "CRITICAL"
    
    // Metadatos
    private LocalDateTime evaluationDate;
    private LocalDateTime lastRetrainingDate;
    private int daysSinceLastRetrain;
    private int retrainingCount;         // Número de veces reentrenado
    
    // Resultados
    private List<String> recommendations;
    private String justification;
    private Map<String, Object> retrainingConfig;
    
    // Constructores
    public ModelRetrainingFact() {
        this.evaluationDate = LocalDateTime.now();
        this.enableABTesting = true;
        this.abTestingTrafficSplit = 10.0;
        this.abTestingDurationDays = 3;
        this.useAutomatedHPO = false;
        this.maxTrainingEpochs = 100;
        this.earlyStoppingThreshold = 0.001;
    }
    
    public ModelRetrainingFact(String modelId, String modelName) {
        this();
        this.modelId = modelId;
        this.modelName = modelName;
    }
    
    // Métodos de cálculo
    public void calculatePerformanceRatio() {
        if (baselineAccuracy > 0) {
            this.performanceRatio = currentAccuracy / baselineAccuracy;
            this.accuracyDrop = baselineAccuracy - currentAccuracy;
        }
    }
    
    public void calculateDaysSinceLastRetrain() {
        if (lastRetrainingDate != null) {
            this.daysSinceLastRetrain = (int) java.time.temporal.ChronoUnit.DAYS.between(
                lastRetrainingDate, LocalDateTime.now()
            );
        }
    }
    
    public void determineUrgencyLevel() {
        if (performanceDegradation > 30 || driftScore > 0.8 || currentAccuracy < 0.6) {
            this.urgencyLevel = "CRITICAL";
            this.priorityScore = 10;
        } else if (performanceDegradation > 20 || driftScore > 0.6 || currentAccuracy < 0.7) {
            this.urgencyLevel = "HIGH";
            this.priorityScore = 8;
        } else if (performanceDegradation > 10 || driftScore > 0.4 || currentAccuracy < 0.8) {
            this.urgencyLevel = "MEDIUM";
            this.priorityScore = 5;
        } else {
            this.urgencyLevel = "LOW";
            this.priorityScore = 2;
        }
    }
    
    public void determineRetrainingStrategy() {
        if (dataSchemaChanged || performanceDegradation > 40) {
            this.retrainingStrategy = "FULL";
        } else if (newDataAvailable > trainingDataSize * 0.1 && dataDriftScore < 0.3) {
            this.retrainingStrategy = "INCREMENTAL";
        } else if (performanceDegradation > 20) {
            this.retrainingStrategy = "TRANSFER_LEARNING";
        } else {
            this.retrainingStrategy = "INCREMENTAL";
        }
    }
    
    public void makeDecision() {
        if (performanceDegradation > 30 || driftScore > 0.7) {
            this.decision = "FULL_RETRAIN";
            this.requiresApproval = false; // Auto-approve por criticidad
            this.justification = "Performance crítico o drift alto - reentrenamiento completo urgente";
        } else if (performanceDegradation > 15 || driftScore > 0.5) {
            this.decision = "INCREMENTAL_RETRAIN";
            this.requiresApproval = false;
            this.justification = "Degradación moderada - reentrenamiento incremental";
        } else if (performanceDegradation > 10 || daysSinceLastRetrain > 90) {
            this.decision = "INCREMENTAL_RETRAIN";
            this.requiresApproval = true; // Requiere aprobación si no es urgente
            this.justification = "Degradación leve o >90 días sin reentrenar";
        } else if (performanceDegradation < 5 && driftScore < 0.2) {
            this.decision = "SKIP";
            this.requiresApproval = false;
            this.justification = "Performance estable - no requiere reentrenamiento";
        } else {
            this.decision = "HITL_REQUIRED";
            this.requiresApproval = true;
            this.justification = "Caso ambiguo - requiere revisión humana";
        }
    }
    
    // Getters y Setters
    public String getModelId() { return modelId; }
    public void setModelId(String modelId) { this.modelId = modelId; }
    
    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    
    public String getModelVersion() { return modelVersion; }
    public void setModelVersion(String modelVersion) { this.modelVersion = modelVersion; }
    
    public String getTriggerReason() { return triggerReason; }
    public void setTriggerReason(String triggerReason) { this.triggerReason = triggerReason; }
    
    public double getCurrentAccuracy() { return currentAccuracy; }
    public void setCurrentAccuracy(double currentAccuracy) { this.currentAccuracy = currentAccuracy; }
    
    public double getCurrentPrecision() { return currentPrecision; }
    public void setCurrentPrecision(double currentPrecision) { this.currentPrecision = currentPrecision; }
    
    public double getCurrentRecall() { return currentRecall; }
    public void setCurrentRecall(double currentRecall) { this.currentRecall = currentRecall; }
    
    public double getCurrentF1Score() { return currentF1Score; }
    public void setCurrentF1Score(double currentF1Score) { this.currentF1Score = currentF1Score; }
    
    public double getDriftScore() { return driftScore; }
    public void setDriftScore(double driftScore) { this.driftScore = driftScore; }
    
    public double getPerformanceDegradation() { return performanceDegradation; }
    public void setPerformanceDegradation(double performanceDegradation) { this.performanceDegradation = performanceDegradation; }
    
    public double getBaselineAccuracy() { return baselineAccuracy; }
    public void setBaselineAccuracy(double baselineAccuracy) { this.baselineAccuracy = baselineAccuracy; }
    
    public double getAccuracyDrop() { return accuracyDrop; }
    public void setAccuracyDrop(double accuracyDrop) { this.accuracyDrop = accuracyDrop; }
    
    public double getPerformanceRatio() { return performanceRatio; }
    public void setPerformanceRatio(double performanceRatio) { this.performanceRatio = performanceRatio; }
    
    public int getTrainingDataSize() { return trainingDataSize; }
    public void setTrainingDataSize(int trainingDataSize) { this.trainingDataSize = trainingDataSize; }
    
    public int getNewDataAvailable() { return newDataAvailable; }
    public void setNewDataAvailable(int newDataAvailable) { this.newDataAvailable = newDataAvailable; }
    
    public double getDataQualityScore() { return dataQualityScore; }
    public void setDataQualityScore(double dataQualityScore) { this.dataQualityScore = dataQualityScore; }
    
    public boolean isDataSchemaChanged() { return dataSchemaChanged; }
    public void setDataSchemaChanged(boolean dataSchemaChanged) { this.dataSchemaChanged = dataSchemaChanged; }
    
    public double getDataDriftScore() { return dataDriftScore; }
    public void setDataDriftScore(double dataDriftScore) { this.dataDriftScore = dataDriftScore; }
    
    public double getEstimatedTrainingTime() { return estimatedTrainingTime; }
    public void setEstimatedTrainingTime(double estimatedTrainingTime) { this.estimatedTrainingTime = estimatedTrainingTime; }
    
    public double getEstimatedCost() { return estimatedCost; }
    public void setEstimatedCost(double estimatedCost) { this.estimatedCost = estimatedCost; }
    
    public String getAvailableInfrastructure() { return availableInfrastructure; }
    public void setAvailableInfrastructure(String availableInfrastructure) { this.availableInfrastructure = availableInfrastructure; }
    
    public boolean isSpotInstancesAvailable() { return spotInstancesAvailable; }
    public void setSpotInstancesAvailable(boolean spotInstancesAvailable) { this.spotInstancesAvailable = spotInstancesAvailable; }
    
    public String getRetrainingStrategy() { return retrainingStrategy; }
    public void setRetrainingStrategy(String retrainingStrategy) { this.retrainingStrategy = retrainingStrategy; }
    
    public boolean isUseAutomatedHPO() { return useAutomatedHPO; }
    public void setUseAutomatedHPO(boolean useAutomatedHPO) { this.useAutomatedHPO = useAutomatedHPO; }
    
    public int getMaxTrainingEpochs() { return maxTrainingEpochs; }
    public void setMaxTrainingEpochs(int maxTrainingEpochs) { this.maxTrainingEpochs = maxTrainingEpochs; }
    
    public double getEarlyStoppingThreshold() { return earlyStoppingThreshold; }
    public void setEarlyStoppingThreshold(double earlyStoppingThreshold) { this.earlyStoppingThreshold = earlyStoppingThreshold; }
    
    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
    
    public int getPriorityScore() { return priorityScore; }
    public void setPriorityScore(int priorityScore) { this.priorityScore = priorityScore; }
    
    public String getUrgencyLevel() { return urgencyLevel; }
    public void setUrgencyLevel(String urgencyLevel) { this.urgencyLevel = urgencyLevel; }
    
    public boolean isEnableABTesting() { return enableABTesting; }
    public void setEnableABTesting(boolean enableABTesting) { this.enableABTesting = enableABTesting; }
    
    public double getAbTestingTrafficSplit() { return abTestingTrafficSplit; }
    public void setAbTestingTrafficSplit(double abTestingTrafficSplit) { this.abTestingTrafficSplit = abTestingTrafficSplit; }
    
    public int getAbTestingDurationDays() { return abTestingDurationDays; }
    public void setAbTestingDurationDays(int abTestingDurationDays) { this.abTestingDurationDays = abTestingDurationDays; }
    
    public boolean isRequiresApproval() { return requiresApproval; }
    public void setRequiresApproval(boolean requiresApproval) { this.requiresApproval = requiresApproval; }
    
    public List<String> getApprovalGroups() { return approvalGroups; }
    public void setApprovalGroups(List<String> approvalGroups) { this.approvalGroups = approvalGroups; }
    
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    
    public LocalDateTime getEvaluationDate() { return evaluationDate; }
    public void setEvaluationDate(LocalDateTime evaluationDate) { this.evaluationDate = evaluationDate; }
    
    public LocalDateTime getLastRetrainingDate() { return lastRetrainingDate; }
    public void setLastRetrainingDate(LocalDateTime lastRetrainingDate) { this.lastRetrainingDate = lastRetrainingDate; }
    
    public int getDaysSinceLastRetrain() { return daysSinceLastRetrain; }
    public void setDaysSinceLastRetrain(int daysSinceLastRetrain) { this.daysSinceLastRetrain = daysSinceLastRetrain; }
    
    public int getRetrainingCount() { return retrainingCount; }
    public void setRetrainingCount(int retrainingCount) { this.retrainingCount = retrainingCount; }
    
    public List<String> getRecommendations() { return recommendations; }
    public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }
    
    public String getJustification() { return justification; }
    public void setJustification(String justification) { this.justification = justification; }
    
    public Map<String, Object> getRetrainingConfig() { return retrainingConfig; }
    public void setRetrainingConfig(Map<String, Object> retrainingConfig) { this.retrainingConfig = retrainingConfig; }
    
    @Override
    public String toString() {
        return "ModelRetrainingFact{" +
                "modelId='" + modelId + '\'' +
                ", modelName='" + modelName + '\'' +
                ", decision='" + decision + '\'' +
                ", urgencyLevel='" + urgencyLevel + '\'' +
                ", retrainingStrategy='" + retrainingStrategy + '\'' +
                ", performanceDegradation=" + performanceDegradation +
                ", driftScore=" + driftScore +
                '}';
    }
}
