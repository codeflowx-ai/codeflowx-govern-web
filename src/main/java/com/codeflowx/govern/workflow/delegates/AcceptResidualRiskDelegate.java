package com.codeflowx.govern.workflow.delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate: Accept Residual Risk
 * Acepta riesgo residual después de análisis
 * Proceso: MODEL_APPROVAL
 */
@Slf4j
@Component("acceptResidualRiskDelegate")
public class AcceptResidualRiskDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        log.info("⚠️ Accepting Residual Risk");
        
        Integer riskScore = (Integer) execution.getVariable("riskScore");
        String riskCategory = (String) execution.getVariable("riskCategory");
        String acceptedBy = (String) execution.getVariable("approvedBy");
        
        log.info("✅ Residual Risk Accepted:");
        log.info("   Score: {} | Category: {}", riskScore, riskCategory);
        log.info("   Accepted by: {}", acceptedBy);
        
        execution.setVariable("residualRiskAccepted", true);
        execution.setVariable("requiresMitigation", false);
    }
}
