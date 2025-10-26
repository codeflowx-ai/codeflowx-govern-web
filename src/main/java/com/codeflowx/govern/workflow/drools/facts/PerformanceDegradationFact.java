package com.codeflowx.govern.workflow.drools.facts;

import java.io.Serializable;

/**
 * Fact para el proceso de degradación de performance (performance-degradation-v1)
 * 
 * INPUT VARIABLES:
 * - latencyMs: Latencia en milisegundos
 * - errorRate: Tasa de error (0-100)
 * - throughput: Throughput (requests/sec)
 * - cpuUsage: Uso de CPU (0-100)
 * - memoryUsage: Uso de memoria (0-100)
 * 
 * OUTPUT VARIABLES:
 * - decision: NO_DEGRADATION, DEGRADATION_DETECTED, CRITICAL_DEGRADATION
 * - severity: LOW, MEDIUM, HIGH, CRITICAL
 * - confidenceLevel: Nivel de confianza (HIGH, MEDIUM, LOW)
 * - justification: Justificación de la decisión
 * - canAutoscale: Si puede hacer autoscaling
 * - recommendedAction: Acción recomendada
 */
public class PerformanceDegradationFact implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // INPUT VARIABLES
    private Double latencyMs;
    private Double errorRate;
    private Double throughput;
    private Double cpuUsage;
    private Double memoryUsage;
    
    // OUTPUT VARIABLES
    private String decision;
    private String severity;
    private String confidenceLevel;
    private String justification;
    private Boolean canAutoscale;
    private String recommendedAction;
    
    // CONSTRUCTORS
    public PerformanceDegradationFact() {}
    
    public PerformanceDegradationFact(Double latencyMs, Double errorRate, Double throughput, 
                                     Double cpuUsage, Double memoryUsage) {
        this.latencyMs = latencyMs;
        this.errorRate = errorRate;
        this.throughput = throughput;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
    }
    
    // GETTERS AND SETTERS
    public Double getLatencyMs() {
        return latencyMs;
    }
    
    public void setLatencyMs(Double latencyMs) {
        this.latencyMs = latencyMs;
    }
    
    public Double getErrorRate() {
        return errorRate;
    }
    
    public void setErrorRate(Double errorRate) {
        this.errorRate = errorRate;
    }
    
    public Double getThroughput() {
        return throughput;
    }
    
    public void setThroughput(Double throughput) {
        this.throughput = throughput;
    }
    
    public Double getCpuUsage() {
        return cpuUsage;
    }
    
    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }
    
    public Double getMemoryUsage() {
        return memoryUsage;
    }
    
    public void setMemoryUsage(Double memoryUsage) {
        this.memoryUsage = memoryUsage;
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
    
    public Boolean getCanAutoscale() {
        return canAutoscale;
    }
    
    public void setCanAutoscale(Boolean canAutoscale) {
        this.canAutoscale = canAutoscale;
    }
    
    public String getRecommendedAction() {
        return recommendedAction;
    }
    
    public void setRecommendedAction(String recommendedAction) {
        this.recommendedAction = recommendedAction;
    }
    
    @Override
    public String toString() {
        return "PerformanceDegradationFact{" +
                "latencyMs=" + latencyMs +
                ", errorRate=" + errorRate +
                ", throughput=" + throughput +
                ", cpuUsage=" + cpuUsage +
                ", memoryUsage=" + memoryUsage +
                ", decision='" + decision + '\'' +
                ", severity='" + severity + '\'' +
                ", canAutoscale=" + canAutoscale +
                '}';
    }
}
