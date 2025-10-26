package com.codeflowx.govern.workflow.drools.facts;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

/**
 * Drools Fact: Model Approval
 * 
 * Fact object usado por BusinessRuleTask en proceso model-approval-v1.
 * 
 * Input (desde validaciones automatizadas):
 * - performanceScore: Score de rendimiento (0-100)
 * - biasScore: Score de detección de bias (0-100)
 * - complianceScore: Score de compliance (0-100)
 * 
 * Input (desde revisiones humanas):
 * - mlEngineerApproval: APPROVED, REJECTED, NEEDS_CHANGES
 * - governanceApproval: APPROVED, REJECTED, CONDITIONAL
 * - targetEnvironment: STAGING, PRODUCTION
 * 
 * Output (calculado por Drools):
 * - finalDecision: APPROVED, REJECTED, CONDITIONAL_APPROVAL
 * - minScore: Score mínimo alcanzado
 * - confidenceLevel: Nivel de confianza (0.0-1.0)
 * - justification: Explicación de la decisión
 * - requiresMonitoring: true/false (si aprobado con condiciones)
 * 
 * DRL: model-approval-scoring.drl
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelApprovalFact implements Serializable {

    private static final long serialVersionUID = 1L;

    // ===== INPUT: Validaciones Automatizadas =====
    
    /**
     * Performance Score (0-100)
     * Calculado por ModelValidationDelegate
     */
    private Integer performanceScore;
    
    /**
     * Bias Score (0-100, donde 100 = sin bias)
     * Calculado por BiasDetectionDelegate
     */
    private Integer biasScore;
    
    /**
     * Compliance Score (0-100)
     * Calculado por ComplianceCheckDelegate
     */
    private Integer complianceScore;
    
    // ===== INPUT: Revisiones Humanas =====
    
    /**
     * ML Engineer Approval: APPROVED, REJECTED, NEEDS_CHANGES
     */
    private String mlEngineerApproval;
    
    /**
     * Governance Approval: APPROVED, REJECTED, CONDITIONAL
     */
    private String governanceApproval;
    
    /**
     * Target Environment: STAGING, PRODUCTION
     */
    private String targetEnvironment;
    
    // ===== OUTPUT: Decisión Final (Drools) =====
    
    /**
     * Decisión final:
     * - APPROVED: Aprobación completa
     * - REJECTED: Rechazo
     * - CONDITIONAL_APPROVAL: Aprobado con monitorización adicional
     */
    private String finalDecision;
    
    /**
     * Score mínimo de las 3 validaciones
     */
    private Integer minScore;
    
    /**
     * Nivel de confianza de la decisión (0.0 - 1.0)
     */
    private Double confidenceLevel;
    
    /**
     * Justificación de la decisión
     */
    private String justification;
    
    /**
     * Requiere monitorización adicional (para aprobaciones condicionales)
     */
    private Boolean requiresMonitoring;
    
    // ===== MÉTODOS DE UTILIDAD =====
    
    /**
     * Calcula el score mínimo de las 3 validaciones
     */
    public Integer calculateMinScore() {
        int min = performanceScore;
        if (biasScore < min) min = biasScore;
        if (complianceScore < min) min = complianceScore;
        return min;
    }
    
    /**
     * Calcula el score promedio
     */
    public Double calculateAvgScore() {
        return (performanceScore + biasScore + complianceScore) / 3.0;
    }
    
    /**
     * Verifica si todos los scores son válidos
     */
    public boolean allScoresAvailable() {
        return performanceScore != null && biasScore != null && complianceScore != null;
    }
    
    /**
     * Verifica si ambas revisiones humanas aprobaron
     */
    public boolean bothReviewsApproved() {
        return "APPROVED".equals(mlEngineerApproval) && 
               "APPROVED".equals(governanceApproval);
    }
    
    /**
     * Verifica si es deployment a PRODUCTION
     */
    public boolean isProductionDeployment() {
        return "PRODUCTION".equals(targetEnvironment);
    }
}

