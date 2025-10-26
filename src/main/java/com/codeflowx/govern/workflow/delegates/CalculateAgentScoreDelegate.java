package com.codeflowx.govern.workflow.delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.workflow.drools.facts.AgentApprovalFact;
import com.codeflowx.govern.workflow.services.DroolsRulesService;

import lombok.extern.slf4j.Slf4j;

/**
 * Delegate para calcular confidence score usando Drools
 * Reemplaza el scriptTask hardcoded
 */
@Slf4j
@Component("calculateAgentScoreDelegate")
public class CalculateAgentScoreDelegate implements JavaDelegate {

    @Autowired
    private DroolsRulesService droolsService;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            log.info("🎯 [CalculateAgentScore] Iniciando cálculo con Drools");
            
            // 1. Obtener scores de las evaluaciones previas
            Integer riskScore = (Integer) execution.getVariable("riskScore");
            Integer complianceScore = (Integer) execution.getVariable("complianceScore");
            Integer ethicsScore = (Integer) execution.getVariable("ethicsScore");
            
            // Validaciones
            if (riskScore == null || complianceScore == null || ethicsScore == null) {
                throw new IllegalStateException("Missing required scores for agent approval");
            }
            
            log.info("📊 Scores obtenidos - Risk: {}, Compliance: {}, Ethics: {}", 
                    riskScore, complianceScore, ethicsScore);
            
            // 2. Crear Fact para Drools
            AgentApprovalFact fact = new AgentApprovalFact();
            fact.setRiskScore(riskScore);
            fact.setComplianceScore(complianceScore);
            fact.setEthicsScore(ethicsScore);
            
            Boolean compliant = (Boolean) execution.getVariable("compliant");
            fact.setCompliant(compliant != null ? compliant : true);
            
            // 3. Ejecutar Drools Rules Engine
            log.info("🔧 Ejecutando Drools Rules Engine...");
            droolsService.executeAgentApprovalRules(fact);
            
            // 4. Obtener resultados de Drools
            String decision = fact.getDecision();
            Integer minScore = fact.getMinScore();
            Double confidenceLevel = fact.getConfidenceLevel();
            String justification = fact.getJustification();
            
            // Derivar requiresHumanReview de la decisión
            Boolean requiresHumanReview = "HITL_REQUIRED".equals(decision);
            
            log.info("✅ Drools decision: {}", decision);
            log.info("📊 MinScore: {}, ConfidenceLevel: {}", minScore, confidenceLevel);
            log.info("👤 RequiresHumanReview: {}", requiresHumanReview);
            log.info("📝 Justification: {}", justification);
            
            // 5. Guardar resultados en variables de proceso
            execution.setVariable("decision", decision);
            execution.setVariable("minScore", minScore);
            execution.setVariable("confidenceLevel", confidenceLevel);
            execution.setVariable("requiresHumanReview", requiresHumanReview);
            execution.setVariable("justification", justification);
            
            // Para compatibilidad con gateways
            execution.setVariable("humanApprovalRequired", requiresHumanReview);
            
            log.info("✅ [CalculateAgentScore] Completado - Decision: {}", decision);
            
        } catch (Exception e) {
            log.error("❌ Error en CalculateAgentScoreDelegate: {}", e.getMessage(), e);
            throw new RuntimeException("Error calculating agent score with Drools: " + e.getMessage(), e);
        }
    }
}

