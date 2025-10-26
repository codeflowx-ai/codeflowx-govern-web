package com.codeflowx.govern.workflow.delegates;

import java.sql.Timestamp;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.models.ModelApproval;
import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate: Auto Approve No Bias
 * Auto-aprueba modelo si no se detectó sesgo
 * Proceso: bias-detection-v1
 */
@Slf4j
@Component("autoApproveNoBiasDelegate")
public class AutoApproveNoBiasDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            log.info("✅ Auto-Approving Model (No Bias Detected)");
            
            Long modelApprovalId = (Long) execution.getVariable("modelApprovalId");
            Double overallFairness = (Double) execution.getVariable("overallFairness");
            
            if (modelApprovalId != null) {
                ModelApproval ma = businessService.findById(ModelApproval.class, modelApprovalId);
                if (ma != null) {
                    ma.setModapprovalstatus("APPROVED");
                    ma.setModapprovalnotes("Auto-approved: No bias detected. Fairness score: " + 
                                          String.format("%.2f", overallFairness != null ? overallFairness : 1.0));
                    ma.setModapprovedat(new Timestamp(System.currentTimeMillis()));
                    ma.setModapproverid("SYSTEM_AI");
                    ma.setModapprovername("Automated AI Governance System");
                    ma.setModapproverrole("SYSTEM");
                    businessService.save(ma);
                    
                    log.info("✅ Model auto-approved. ID: {}", modelApprovalId);
                }
            }
            
            execution.setVariable("modelApproved", true);
            execution.setVariable("autoApprovalReason", "NO_BIAS_DETECTED");
            
        } catch (Exception e) {
            log.error("❌ Error auto-approving model: {}", e.getMessage(), e);
            throw new RuntimeException("Auto-approval failed: " + e.getMessage(), e);
        }
    }
}
