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
 * Delegate: Save Ethics Evidence
 * Guarda evidencia de revisión ética
 * Proceso: 09_ETHICS_REVIEW
 */
@Slf4j
@Component("saveEthicsEvidenceDelegate")
public class SaveEthicsEvidenceDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            log.info("💾 Saving Ethics Evidence");
            
            Long ethicsReviewId = (Long) execution.getVariable("ethicsReviewId");
            String evidence = (String) execution.getVariable("ethicsEvidence");
            
            if (ethicsReviewId != null) {
                EthicsReview er = businessService.findById(EthicsReview.class, ethicsReviewId);
                if (er != null) {
                    // Guardar evidencia en evidence path/metadata
                    if (evidence != null && !evidence.isEmpty()) {
                        er.setEthevidencepath(evidence);
                    }
                    er.setEthupdatedat(new Timestamp(System.currentTimeMillis()));
                    businessService.save(er);
                    
                    log.info("✅ Ethics evidence saved. ID: {}", ethicsReviewId);
                }
            }
            
            execution.setVariable("evidenceSaved", true);
            
        } catch (Exception e) {
            log.error("❌ Error saving evidence: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save evidence: " + e.getMessage(), e);
        }
    }
}
