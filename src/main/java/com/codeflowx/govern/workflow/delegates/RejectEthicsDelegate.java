package com.codeflowx.govern.workflow.delegates;

import java.sql.Timestamp;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.governance.EthicsReview;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate: Reject Ethics
 * Rechaza revisión ética
 * Proceso: 09_ETHICS_REVIEW
 */
@Slf4j
@Component("rejectEthicsDelegate")
public class RejectEthicsDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            log.info("❌ Rejecting Ethics Review");
            
            Long ethicsReviewId = (Long) execution.getVariable("ethicsReviewId");
            String rejectionReason = (String) execution.getVariable("rejectionReason");
            
            if (ethicsReviewId != null) {
                EthicsReview er = businessService.findById(EthicsReview.class, ethicsReviewId);
                if (er != null) {
                    er.setEthstatus("REJECTED");
                    er.setEthupdatedat(new Timestamp(System.currentTimeMillis()));
                    
                    // Guardar motivo del rechazo en committee notes
                    String notes = "❌ Revisión ética rechazada";
                    if (rejectionReason != null && !rejectionReason.isEmpty()) {
                        notes += " - Motivo: " + rejectionReason;
                    }
                    er.setEthcommitteenotes(notes);
                    er.setEthcommitteedecision("REJECTED");
                    
                    businessService.save(er);
                    
                    log.info("✅ Ethics review rejected. ID: {}", ethicsReviewId);
                }
            }
            
            execution.setVariable("ethicsRejected", true);
            
        } catch (Exception e) {
            log.error("❌ Error rejecting ethics: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to reject ethics: " + e.getMessage(), e);
        }
    }
}
