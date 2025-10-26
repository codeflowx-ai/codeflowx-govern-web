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
 * Delegate para verificación de compliance con IA
 * Verifica AI Act, GDPR, DSA automáticamente
 */
@Slf4j
@Component("aiComplianceCheckDelegate")
public class AIComplianceCheckDelegate implements JavaDelegate {

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

        log.info("🤖 AI Compliance check: entityType={}, ID={}", entityType, agentId);

        try {
            // 1. Cargar datos
            Agent agent = businessService.findById(Agent.class, agentId);
            
            // 2. Preparar system_info
            Map<String, Object> systemInfo = new HashMap<>();
            systemInfo.put("name", agent.getAgtname());
            systemInfo.put("description", agent.getAgtdescription());
            systemInfo.put("type", agent.getAgttype());
            systemInfo.put("metadata", agent.getAgtmetadata());  // ✅ CORRECTO
            systemInfo.put("capabilities", agent.getAgtcapabilities());  // ✅ CORRECTO
            systemInfo.put("configuration", agent.getAgtconfiguration());  // ✅ CORRECTO

            // 3. Llamar a Agente IA
            Map<String, Object> complianceResult = aiAgentService.checkCompliance(
                entityType,
                agentId,
                systemInfo
            );
            
            Integer complianceScore = (Integer) complianceResult.get("compliance_score");
            Boolean compliant = (Boolean) complianceResult.get("compliant");
            
            // 4. Actualizar BBDD
            if (approvalId != null) {
                AgentApproval approval = businessService.findById(AgentApproval.class, approvalId);
                if (approval != null) {
                    approval.setAgtcompliancecheck(toJson(complianceResult));
                    approval.setAgtupdatedat(new Timestamp(System.currentTimeMillis()));
                    businessService.save(approval);
                }
            }

            // 5. Evidencia
            // 6. Variables BPMN
            execution.setVariable("complianceScore", complianceScore != null ? complianceScore : 60);
            execution.setVariable("compliant", Boolean.TRUE.equals(compliant));

            log.info("✅ Compliance check completado - Score: {}, Compliant: {}", complianceScore, compliant);

        } catch (Exception e) {
            log.error("❌ Error en compliance check: {}", e.getMessage());
            execution.setVariable("complianceScore", 60);
            execution.setVariable("compliant", false);
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

