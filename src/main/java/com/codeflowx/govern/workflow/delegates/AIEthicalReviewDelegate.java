package com.codeflowx.govern.workflow.delegates;

import java.sql.Timestamp;
import java.util.HashMap;
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
 * Delegate para revisión ética con IA
 * Evalúa principios éticos automáticamente
 */
@Slf4j
@Component("aiEthicalReviewDelegate")
public class AIEthicalReviewDelegate implements JavaDelegate {

    @Autowired
    private AIAgentService aiAgentService;

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        Long agentId = (Long) execution.getVariable("agentId");
        Long approvalId = (Long) execution.getVariable("approvalId");
        String entityType = (String) execution.getVariable("entityType");
        if (entityType == null) entityType = "AGENT";

        log.info("🤖 AI Ethical review: entityType={}, ID={}", entityType, agentId);

        try {
            // 1. Cargar datos
            Agent agent = businessService.findById(Agent.class, agentId);
            
            // 2. Preparar system_info
            Map<String, Object> systemInfo = new HashMap<>();
            systemInfo.put("name", agent.getAgtname());
            systemInfo.put("description", agent.getAgtdescription());
            systemInfo.put("metadata", agent.getAgtmetadata());  // ✅ CORRECTO
            systemInfo.put("capabilities", agent.getAgtcapabilities());  // ✅ CORRECTO
            systemInfo.put("configuration", agent.getAgtconfiguration());  // ✅ CORRECTO
            systemInfo.put("status", agent.getAgtstatus());  // ✅ CORRECTO

            // 3. Llamar a Agente IA
            Map<String, Object> ethicsResult = aiAgentService.reviewEthics(
                entityType,
                agentId,
                systemInfo
            );
            
            Integer ethicsScore = (Integer) ethicsResult.get("ethics_score");
            
            // 4. Actualizar BBDD
            if (approvalId != null) {
                AgentApproval approval = businessService.findById(AgentApproval.class, approvalId);
                if (approval != null) {
                    approval.setAgtethicalreview(toJson(ethicsResult));
                    approval.setAgtupdatedat(new Timestamp(System.currentTimeMillis()));
                    businessService.save(approval);
                }
            }

            // 5. Evidencia
            // 6. Variables BPMN
            execution.setVariable("ethicsScore", ethicsScore != null ? ethicsScore : 70);

            log.info("✅ Ethical review completado - Score: {}", ethicsScore);

        } catch (Exception e) {
            log.error("❌ Error en ethical review: {}", e.getMessage());
            execution.setVariable("ethicsScore", 70);
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }


    private String toJson(Map<String, Object> data) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(data);
        } catch (Exception e) {
            return "{}";
        }
    }
}

