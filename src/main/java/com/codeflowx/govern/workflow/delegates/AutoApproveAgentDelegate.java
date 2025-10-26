package com.codeflowx.govern.workflow.delegates;

import java.sql.Timestamp;

import org.enartframework.orm.exception.DaoException;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.agents.Agent;
import com.codeflowx.govern.entity.agents.AgentApproval;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate para auto-aprobar agente basado en evaluaciones IA
 * Se ejecuta cuando minScore >= 90 (alta confianza)
 */
@Slf4j
@Component("autoApproveAgentDelegate")
public class AutoApproveAgentDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        Long agentId = (Long) execution.getVariable("agentId");
        Long approvalId = (Long) execution.getVariable("approvalId");
        Integer minScore = (Integer) execution.getVariable("minScore");

        log.info("🤖 Auto-aprobando agente ID: {} (AI minScore: {})", agentId, minScore);

        try {
            // 1. Actualizar Agent a APPROVED
            Agent agent = businessService.findById(Agent.class, agentId);
            if (agent != null) {
                agent.setAgtstatus("APPROVED");
                agent.setAgtupdatedat(new Timestamp(System.currentTimeMillis()));
                businessService.save(agent);
            }

            // 2. Actualizar AgentApproval
            if (approvalId != null) {
                AgentApproval approval = businessService.findById(AgentApproval.class, approvalId);
                if (approval != null) {
                    approval.setAgtapprovalstatus("APPROVED");
                    approval.setAgtapproverid("SYSTEM_AI");
                    approval.setAgtapprovername("AI Auto-Approval (High Confidence)");
                    approval.setAgtapprovalnotes("Auto-approved by AI. Min score: " + minScore + "/100");
                    approval.setAgtapprovedat(new Timestamp(System.currentTimeMillis()));
                    approval.setAgtupdatedat(new Timestamp(System.currentTimeMillis()));
                    businessService.save(approval);
                }
            }

            // 3. Variables del proceso
            execution.setVariable("finalDecision", "APPROVED");
            execution.setVariable("approvalMethod", "AI_AUTO_APPROVE");
            execution.setVariable("approvedAt", System.currentTimeMillis());

            log.info("✅ Agente auto-aprobado exitosamente");

        } catch (DaoException e) {
            log.error("❌ Error de BBDD auto-aprobando agente", e);
            throw new RuntimeException("Error de BBDD: " + e.getMessage(), e);
        }
    }
}

