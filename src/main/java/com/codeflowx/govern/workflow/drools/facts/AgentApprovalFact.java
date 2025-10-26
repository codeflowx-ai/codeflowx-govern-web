package com.codeflowx.govern.workflow.drools.facts;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

/**
 * Drools Fact: Agent Approval
 * 
 * Fact object usado por BusinessRuleTask en proceso agent-approval-v3.
 * 
 * Input (desde delegates):
 * - riskScore, complianceScore, ethicsScore
 * - compliant
 * 
 * Output (calculado por Drools):
 * - decision: AUTO_APPROVE, HITL_REQUIRED, AUTO_REJECT
 * - minScore: Score mínimo alcanzado
 * - confidenceLevel: Nivel de confianza (0.0-1.0)
 * - justification: Explicación de la decisión
 * 
 * DRL: agent-scoring.drl
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentApprovalFact implements Serializable {

    private static final long serialVersionUID = 1L;

    // ===== INPUT (desde Process Variables) =====
    
    /**
     * Risk Score (0-100)
     * Calculado por AIRiskAssessmentDelegate
     */
    private Integer riskScore;
    
    /**
     * Compliance Score (0-100)
     * Calculado por AIComplianceCheckDelegate
     */
    private Integer complianceScore;
    
    /**
     * Ethics Score (0-100)
     * Calculado por AIEthicalReviewDelegate
     */
    private Integer ethicsScore;
    
    /**
     * Cumple con compliance (true/false)
     * Calculado por AIComplianceCheckDelegate
     */
    private Boolean compliant;
    
    // ===== OUTPUT (calculado por Drools) =====
    
    /**
     * Decisión final:
     * - AUTO_APPROVE: Aprobación automática
     * - HITL_REQUIRED: Requiere revisión humana
     * - AUTO_REJECT: Rechazo automático
     */
    private String decision;
    
    /**
     * Score mínimo de los 3 evaluados
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
    
    // ===== MÉTODOS DE UTILIDAD =====
    
    /**
     * Calcula el score mínimo de los 3
     */
    public Integer calculateMinScore() {
        int min = riskScore;
        if (complianceScore < min) min = complianceScore;
        if (ethicsScore < min) min = ethicsScore;
        return min;
    }
    
    /**
     * Calcula el score promedio
     */
    public Double calculateAvgScore() {
        return (riskScore + complianceScore + ethicsScore) / 3.0;
    }
    
    /**
     * Verifica si todos los scores son válidos (no null)
     */
    public boolean allScoresAvailable() {
        return riskScore != null && complianceScore != null && ethicsScore != null;
    }
}
