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
 * Delegate: Reject Biased Model
 * Rechaza modelo por detección de sesgo
 * Proceso: 08_BIAS_DETECTION
 * JPA: ModelApproval (prefijo MOD)
 */
@Slf4j
@Component("rejectBiasedModelDelegate")
public class RejectBiasedModelDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            log.info("❌ Rejecting Biased Model");
            
            Long modelApprovalId = (Long) execution.getVariable("modelApprovalId");
            String rejectionReason = (String) execution.getVariable("mitigationRecommendations");
            
            if (modelApprovalId != null) {
                ModelApproval ma = businessService.findById(ModelApproval.class, modelApprovalId);
                if (ma != null) {
                    // Actualizar estado (usando propiedades CORRECTAS con prefijo MOD)
                    ma.setModapprovalstatus("REJECTED");  // ✅ CORRECTO (no mastatus)
                    ma.setModrejectionreason("Bias detected: " + rejectionReason);  // ✅ CORRECTO
                    ma.setModupdatedat(new Timestamp(System.currentTimeMillis()));  // ✅ CORRECTO
                    businessService.save(ma);
                    log.info("✅ Model approval rejected due to bias. ID: {}", modelApprovalId);
                }
            }
            
            execution.setVariable("modelRejected", true);
            
        } catch (Exception e) {
            log.error("❌ Error rejecting biased model: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to reject model: " + e.getMessage(), e);
        }
    }
}
