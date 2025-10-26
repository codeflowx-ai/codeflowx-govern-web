package com.codeflowx.govern.workflow.delegates;

import java.sql.Timestamp;

import org.enartframework.orm.exception.DaoException;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.agents.AgentApproval;
import com.codeflowx.govern.workflow.services.RiskAssessmentService;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate para ejecutar Risk Assessment usando BusinessService y JPAs
 */
@Slf4j
@Component("riskAssessmentDelegate")
public class RiskAssessmentDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private RiskAssessmentService riskAssessmentService;

    @Override
    public void execute(DelegateExecution execution) {
        String processInstanceId = execution.getProcessInstanceId();
        Long agentId = (Long) execution.getVariable("agentId");

        log.info("Ejecutando RiskAssessmentDelegate: processId={}, agentId={}",
                 processInstanceId, agentId);

        try {
            // 1. Ejecutar risk assessment usando LLM
            RiskAssessmentService.RiskAssessmentResult result = 
                    riskAssessmentService.assessAgentRisk(agentId);

            // 2. Actualizar JPA AgentApproval
            Long approvalId = (Long) execution.getVariable("approvalId");
            if (approvalId != null) {
                AgentApproval approval = businessService.findById(AgentApproval.class, approvalId);
                if (approval != null) {
                    approval.setAgtriskassessment(result.toJson());
                    approval.setAgtupdatedat(new Timestamp(System.currentTimeMillis()));
                    businessService.save(approval);
                }
            }

            // 3. Guardar resultados en variables del proceso
            execution.setVariable("riskAssessmentResult", result.toJson());
            execution.setVariable("riskLevel", result.getRiskLevel());
            execution.setVariable("riskScore", result.getRiskScore());

            log.info("RiskAssessmentDelegate completado: riskLevel={}, score={}",
                     result.getRiskLevel(), result.getRiskScore());

        } catch (DaoException e) {
            log.error("Error de BBDD en RiskAssessmentDelegate", e);
            execution.setVariable("riskAssessmentError", e.getMessage());
            execution.setVariable("riskLevel", "ERROR");
            throw new RuntimeException("Error de BBDD: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error en RiskAssessmentDelegate", e);
            execution.setVariable("riskAssessmentError", e.getMessage());
            execution.setVariable("riskLevel", "ERROR");
            throw new RuntimeException("Error en risk assessment: " + e.getMessage(), e);
        }
    }
}

