package com.codeflowx.govern.workflow.drools.facts;

import lombok.Data;

/**
 * Fact para Drools - Alert Classification
 */
@Data
public class AlertClassificationFact {
    
    // Alert data
    private String alertType;  // DRIFT_DETECTED, PERFORMANCE_DEGRADATION, COMPLIANCE_VIOLATION, etc.
    private String alertCategory;  // MODEL_MONITORING, INFRASTRUCTURE, COMPLIANCE, SECURITY
    private String description;
    
    // Metrics (opcional, según tipo)
    private Double metricValue;
    private Double metricThreshold;
    private Double degradationPercentage;
    
    // Metadata
    private String modelName;
    private String serviceName;
    private Boolean isProduction;
    private Integer affectedUsers;
    
    // Resultado calculado por Drools
    private String severity;  // LOW, MEDIUM, HIGH, CRITICAL
    private String priority;  // P1, P2, P3, P4
    private Integer slaMinutes;  // SLA en minutos
    private String escalationChannel;  // SLACK, EMAIL, PAGERDUTY
    private String assignedTeam;  // ml-engineers, mlops-engineers, governance-admins
    private Boolean requiresImmediateAction;
}


