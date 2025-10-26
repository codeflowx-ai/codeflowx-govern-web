package com.codeflowx.govern.workflow.drools.facts;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Fact object para respuesta automática a incidentes ML
 * AI-powered Root Cause Analysis y auto-remediation
 */
public class IncidentResponseFact {
    
    // Identificadores
    private String incidentId;
    private String modelId;
    private String serviceId;
    private String environment;          // "PRODUCTION", "STAGING", "DEV"
    
    // Clasificación del incidente
    private String incidentType;         // "PERFORMANCE", "ERROR", "DRIFT", "BIAS", "AVAILABILITY"
    private String severity;             // "P1_CRITICAL", "P2_HIGH", "P3_MEDIUM", "P4_LOW"
    private String impact;               // "CUSTOMER_FACING", "INTERNAL", "MONITORING_ONLY"
    
    // Métricas del incidente
    private double errorRate;            // 0-1
    private double latencyP95;           // ms
    private double latencyIncrease;      // Porcentaje vs baseline
    private int affectedRequests;
    private double throughputDrop;       // Porcentaje
    
    // Contexto temporal
    private LocalDateTime incidentStartTime;
    private LocalDateTime detectionTime;
    private int minutesSinceStart;
    private String timeOfDay;            // "PEAK", "OFF_PEAK", "NIGHT"
    
    // Análisis de causa raíz (RCA)
    private List<String> suspectedCauses; // Causas sospechosas
    private String primaryCause;          // Causa principal identificada
    private double causeConfidence;       // 0-1 confianza en RCA
    private boolean isKnownIssue;        // Si es un problema conocido
    private String similarIncidentId;    // ID de incidente similar previo
    
    // Correlación con cambios
    private boolean recentDeployment;    // Deployment en últimas 24h
    private boolean recentConfigChange;  // Cambio de config en últimas 24h
    private boolean infrastructureEvent; // Evento de infraestructura
    private String lastChangeId;         // ID del último cambio
    
    // Decisión de respuesta
    private String responseAction;       // "AUTO_FIX", "ROLLBACK", "SCALE", "HITL_REQUIRED", "ESCALATE"
    private boolean canAutoRemediate;
    private String remediationStrategy;  // Estrategia específica de fix
    private int estimatedRecoveryTime;   // Minutos
    
    // Auto-remediation
    private List<String> automatedActions; // Acciones a ejecutar
    private boolean requiresApproval;
    private int confidenceScore;         // 0-100
    
    // Governance y compliance
    private boolean requiresIncidentReport;
    private boolean requiresPostMortem;
    private List<String> notificationGroups;
    
    // Métricas de resolución
    private int mttr;                    // Mean Time To Recovery (minutos)
    private boolean wasAutoResolved;
    
    // Metadatos
    private LocalDateTime evaluationTime;
    private String evaluatorVersion;     // Versión del sistema RCA
    
    // Resultados
    private List<String> recommendations;
    private String justification;
    private Map<String, Object> diagnosticData;
    
    public IncidentResponseFact() {
        this.evaluationTime = LocalDateTime.now();
        this.evaluatorVersion = "1.0";
        this.canAutoRemediate = false;
        this.requiresApproval = true;
        this.confidenceScore = 0;
    }
    
    public IncidentResponseFact(String incidentId, String modelId) {
        this();
        this.incidentId = incidentId;
        this.modelId = modelId;
    }
    
    // Métodos de cálculo
    public void calculateMinutesSinceStart() {
        if (incidentStartTime != null) {
            this.minutesSinceStart = (int) java.time.temporal.ChronoUnit.MINUTES.between(
                incidentStartTime, LocalDateTime.now()
            );
        }
    }
    
    public void determineSeverity() {
        if (errorRate > 0.5 || affectedRequests > 10000 || environment.equals("PRODUCTION")) {
            this.severity = "P1_CRITICAL";
        } else if (errorRate > 0.2 || affectedRequests > 1000) {
            this.severity = "P2_HIGH";
        } else if (errorRate > 0.05 || affectedRequests > 100) {
            this.severity = "P3_MEDIUM";
        } else {
            this.severity = "P4_LOW";
        }
    }
    
    public void determineResponseAction() {
        if (isKnownIssue && causeConfidence > 0.8) {
            this.responseAction = "AUTO_FIX";
            this.canAutoRemediate = true;
            this.requiresApproval = false;
        } else if (recentDeployment && causeConfidence > 0.7) {
            this.responseAction = "ROLLBACK";
            this.canAutoRemediate = true;
            this.requiresApproval = severity.equals("P1_CRITICAL") ? false : true;
        } else if (throughputDrop > 30) {
            this.responseAction = "SCALE";
            this.canAutoRemediate = true;
            this.requiresApproval = false;
        } else if (severity.equals("P1_CRITICAL")) {
            this.responseAction = "ESCALATE";
            this.requiresApproval = false;
        } else {
            this.responseAction = "HITL_REQUIRED";
            this.requiresApproval = true;
        }
    }
    
    // Getters y Setters (simplificados)
    public String getIncidentId() { return incidentId; }
    public void setIncidentId(String incidentId) { this.incidentId = incidentId; }
    
    public String getModelId() { return modelId; }
    public void setModelId(String modelId) { this.modelId = modelId; }
    
    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }
    
    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }
    
    public String getIncidentType() { return incidentType; }
    public void setIncidentType(String incidentType) { this.incidentType = incidentType; }
    
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    
    public String getImpact() { return impact; }
    public void setImpact(String impact) { this.impact = impact; }
    
    public double getErrorRate() { return errorRate; }
    public void setErrorRate(double errorRate) { this.errorRate = errorRate; }
    
    public double getLatencyP95() { return latencyP95; }
    public void setLatencyP95(double latencyP95) { this.latencyP95 = latencyP95; }
    
    public double getLatencyIncrease() { return latencyIncrease; }
    public void setLatencyIncrease(double latencyIncrease) { this.latencyIncrease = latencyIncrease; }
    
    public int getAffectedRequests() { return affectedRequests; }
    public void setAffectedRequests(int affectedRequests) { this.affectedRequests = affectedRequests; }
    
    public double getThroughputDrop() { return throughputDrop; }
    public void setThroughputDrop(double throughputDrop) { this.throughputDrop = throughputDrop; }
    
    public LocalDateTime getIncidentStartTime() { return incidentStartTime; }
    public void setIncidentStartTime(LocalDateTime incidentStartTime) { this.incidentStartTime = incidentStartTime; }
    
    public LocalDateTime getDetectionTime() { return detectionTime; }
    public void setDetectionTime(LocalDateTime detectionTime) { this.detectionTime = detectionTime; }
    
    public int getMinutesSinceStart() { return minutesSinceStart; }
    public void setMinutesSinceStart(int minutesSinceStart) { this.minutesSinceStart = minutesSinceStart; }
    
    public String getTimeOfDay() { return timeOfDay; }
    public void setTimeOfDay(String timeOfDay) { this.timeOfDay = timeOfDay; }
    
    public List<String> getSuspectedCauses() { return suspectedCauses; }
    public void setSuspectedCauses(List<String> suspectedCauses) { this.suspectedCauses = suspectedCauses; }
    
    public String getPrimaryCause() { return primaryCause; }
    public void setPrimaryCause(String primaryCause) { this.primaryCause = primaryCause; }
    
    public double getCauseConfidence() { return causeConfidence; }
    public void setCauseConfidence(double causeConfidence) { this.causeConfidence = causeConfidence; }
    
    public boolean isKnownIssue() { return isKnownIssue; }
    public void setKnownIssue(boolean knownIssue) { isKnownIssue = knownIssue; }
    
    public String getSimilarIncidentId() { return similarIncidentId; }
    public void setSimilarIncidentId(String similarIncidentId) { this.similarIncidentId = similarIncidentId; }
    
    public boolean isRecentDeployment() { return recentDeployment; }
    public void setRecentDeployment(boolean recentDeployment) { this.recentDeployment = recentDeployment; }
    
    public boolean isRecentConfigChange() { return recentConfigChange; }
    public void setRecentConfigChange(boolean recentConfigChange) { this.recentConfigChange = recentConfigChange; }
    
    public boolean isInfrastructureEvent() { return infrastructureEvent; }
    public void setInfrastructureEvent(boolean infrastructureEvent) { this.infrastructureEvent = infrastructureEvent; }
    
    public String getLastChangeId() { return lastChangeId; }
    public void setLastChangeId(String lastChangeId) { this.lastChangeId = lastChangeId; }
    
    public String getResponseAction() { return responseAction; }
    public void setResponseAction(String responseAction) { this.responseAction = responseAction; }
    
    public boolean isCanAutoRemediate() { return canAutoRemediate; }
    public void setCanAutoRemediate(boolean canAutoRemediate) { this.canAutoRemediate = canAutoRemediate; }
    
    public String getRemediationStrategy() { return remediationStrategy; }
    public void setRemediationStrategy(String remediationStrategy) { this.remediationStrategy = remediationStrategy; }
    
    public int getEstimatedRecoveryTime() { return estimatedRecoveryTime; }
    public void setEstimatedRecoveryTime(int estimatedRecoveryTime) { this.estimatedRecoveryTime = estimatedRecoveryTime; }
    
    public List<String> getAutomatedActions() { return automatedActions; }
    public void setAutomatedActions(List<String> automatedActions) { this.automatedActions = automatedActions; }
    
    public boolean isRequiresApproval() { return requiresApproval; }
    public void setRequiresApproval(boolean requiresApproval) { this.requiresApproval = requiresApproval; }
    
    public int getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(int confidenceScore) { this.confidenceScore = confidenceScore; }
    
    public boolean isRequiresIncidentReport() { return requiresIncidentReport; }
    public void setRequiresIncidentReport(boolean requiresIncidentReport) { this.requiresIncidentReport = requiresIncidentReport; }
    
    public boolean isRequiresPostMortem() { return requiresPostMortem; }
    public void setRequiresPostMortem(boolean requiresPostMortem) { this.requiresPostMortem = requiresPostMortem; }
    
    public List<String> getNotificationGroups() { return notificationGroups; }
    public void setNotificationGroups(List<String> notificationGroups) { this.notificationGroups = notificationGroups; }
    
    public int getMttr() { return mttr; }
    public void setMttr(int mttr) { this.mttr = mttr; }
    
    public boolean isWasAutoResolved() { return wasAutoResolved; }
    public void setWasAutoResolved(boolean wasAutoResolved) { this.wasAutoResolved = wasAutoResolved; }
    
    public LocalDateTime getEvaluationTime() { return evaluationTime; }
    public void setEvaluationTime(LocalDateTime evaluationTime) { this.evaluationTime = evaluationTime; }
    
    public String getEvaluatorVersion() { return evaluatorVersion; }
    public void setEvaluatorVersion(String evaluatorVersion) { this.evaluatorVersion = evaluatorVersion; }
    
    public List<String> getRecommendations() { return recommendations; }
    public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }
    
    public String getJustification() { return justification; }
    public void setJustification(String justification) { this.justification = justification; }
    
    public Map<String, Object> getDiagnosticData() { return diagnosticData; }
    public void setDiagnosticData(Map<String, Object> diagnosticData) { this.diagnosticData = diagnosticData; }
    
    @Override
    public String toString() {
        return "IncidentResponseFact{" +
                "incidentId='" + incidentId + '\'' +
                ", severity='" + severity + '\'' +
                ", responseAction='" + responseAction + '\'' +
                ", canAutoRemediate=" + canAutoRemediate +
                ", primaryCause='" + primaryCause + '\'' +
                '}';
    }
}
