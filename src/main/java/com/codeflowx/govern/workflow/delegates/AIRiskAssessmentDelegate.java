package com.codeflowx.govern.workflow.delegates;

import java.sql.Timestamp;
import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.agents.Agent;
import com.codeflowx.govern.entity.agents.AgentApproval;
import com.codeflowx.govern.entity.monitoring.AuditLog;
import com.codeflowx.govern.workflow.services.AIAgentService;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate para ejecutar risk assessment con IA
 * Integrado con microservicio leka-server-governance (80% automatización)
 */
@Slf4j
@Component("aiRiskAssessmentDelegate")
public class AIRiskAssessmentDelegate implements JavaDelegate {

    @Autowired
    private AIAgentService aiAgentService;

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution)  {
        String processInstanceId = execution.getProcessInstanceId();
        Long agentId = (Long) execution.getVariable("agentId");
        Long approvalId = (Long) execution.getVariable("approvalId");

        log.info("🤖 Ejecutando AI risk assessment: processId={}, agentId={}", processInstanceId, agentId);

        try {
            // 1. Cargar datos desde BBDD
            Agent agent = businessService.findById(Agent.class, agentId);
            if (agent == null) {
                throw new RuntimeException("Agente no encontrado: " + agentId);
            }

            // 2. Llamar a Agente IA (microservicio governance)
            Map<String, Object> riskResult = aiAgentService.assessRisk(
                "AGENT",
                agentId,
                agent.getAgtname(),
                agent.getAgtdescription(),
                agent.getAgtmetadata(),  // ✅ CORRECTO (JSONB con metadata)
                agent.getAgtcapabilities(),  // ✅ CORRECTO (JSONB con capabilities)
                agent.getAgtconfiguration()  // ✅ CORRECTO (JSONB con configuration)
            );
            
            // 3. Extraer resultados
            Integer riskScore = (Integer) riskResult.get("risk_score");
            String riskCategory = (String) riskResult.get("risk_category");
            
            // 4. Actualizar AgentApproval con resultado AI
            if (approvalId != null) {
                AgentApproval approval = businessService.findById(AgentApproval.class, approvalId);
                if (approval != null) {
                    approval.setAgtriskassessment(toJson(riskResult));
                    approval.setAgtupdatedat(new Timestamp(System.currentTimeMillis()));
                    businessService.save(approval);
                }
            }

            // 5. Variables del proceso BPMN  
            execution.setVariable("riskScore", riskScore != null ? riskScore : 75);
            execution.setVariable("riskCategory", riskCategory);
            execution.setVariable("riskJustification", riskResult.get("justification"));

            log.info("✅ AI risk assessment completado - Score: {}, Category: {}", riskScore, riskCategory);

        } catch (Exception e) {
            log.error("❌ Error en risk assessment: {}", e.getMessage());
            execution.setVariable("riskScore", 75); // Conservador
            execution.setVariable("error", e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }

    // Método saveEvidence() eliminado - AuditLog no tiene las propiedades usadas
    // La trazabilidad se guarda en agtriskassessment (JSONB) dentro de AgentApproval

    private String toJson(Map<String, Object> data) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(data);
        } catch (Exception e) {
            return "{}";
        }
    }
}

