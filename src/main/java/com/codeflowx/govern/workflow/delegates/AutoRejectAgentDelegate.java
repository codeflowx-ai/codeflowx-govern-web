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
 * Delegate para auto-rechazar agente basado en evaluaciones IA
 * Se ejecuta cuando minScore < 70 (baja confianza/alto riesgo)
 */
@Slf4j
@Component("autoRejectAgentDelegate")
public class AutoRejectAgentDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        Long agentId = (Long) execution.getVariable("agentId");
        Long approvalId = (Long) execution.getVariable("approvalId");
        Integer minScore = (Integer) execution.getVariable("minScore");
        String riskJustification = (String) execution.getVariable("riskJustification");

        log.info("🤖 Auto-rechazando agente ID: {} (AI minScore: {})", agentId, minScore);

        try {
            // 1. Actualizar Agent a REJECTED
            Agent agent = businessService.findById(Agent.class, agentId);
            if (agent != null) {
                agent.setAgtstatus("REJECTED");
                agent.setAgtupdatedat(new Timestamp(System.currentTimeMillis()));
                businessService.save(agent);
            }

            // 2. Actualizar AgentApproval con razón del rechazo
            if (approvalId != null) {
                AgentApproval approval = businessService.findById(AgentApproval.class, approvalId);
                if (approval != null) {
                    approval.setAgtapprovalstatus("REJECTED");
                    approval.setAgtrejectionreason("Auto-rejected by AI due to low confidence score (" + minScore + "/100). " +
                                                   "Issues detected in risk, compliance, or ethical evaluation. " +
                                                   "AI Justification: " + (riskJustification != null ? riskJustification : "See detailed assessments."));
                    approval.setAgtupdatedat(new Timestamp(System.currentTimeMillis()));
                    businessService.save(approval);
                }
            }

            // 3. Variables del proceso
            execution.setVariable("finalDecision", "REJECTED");
            execution.setVariable("approvalMethod", "AI_AUTO_REJECT");
            execution.setVariable("rejectedAt", System.currentTimeMillis());

            log.info("✅ Agente auto-rechazado exitosamente");

        } catch (DaoException e) {
            log.error("❌ Error de BBDD auto-rechazando agente", e);
            throw new RuntimeException("Error de BBDD: " + e.getMessage(), e);
        }
    }
}

