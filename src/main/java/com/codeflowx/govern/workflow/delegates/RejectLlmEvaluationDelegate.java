package com.codeflowx.govern.workflow.delegates;

import java.sql.Timestamp;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.evaluation.LlmEvaluation;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate: Reject LLM Evaluation
 * Rechaza evaluación LLM
 * Proceso: 12_LLM_EVALUATION
 */
@Slf4j
@Component("rejectLlmEvaluationDelegate")
public class RejectLlmEvaluationDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            log.info("❌ Rejecting LLM Evaluation");
            
            Long evaluationId = (Long) execution.getVariable("evaluationId");
            String rejectionReason = (String) execution.getVariable("rejectionReason");
            String rejectedBy = (String) execution.getVariable("rejectedBy");
            
            if (evaluationId != null) {
                LlmEvaluation eval = businessService.findById(LlmEvaluation.class, evaluationId);
                if (eval != null) {
                    eval.setEvalstatus("REJECTED");
                    eval.setEvalupdatedat(new Timestamp(System.currentTimeMillis()));
                    
                    // Construir descripción con información del rechazo
                    String description = String.format("❌ Rechazado por %s", 
                        rejectedBy != null ? rejectedBy : "REVIEWER");
                    if (rejectionReason != null && !rejectionReason.isEmpty()) {
                        description += " - Motivo: " + rejectionReason;
                    }
                    eval.setEvaldescription(description);
                    
                    businessService.save(eval);
                    
                    log.info("✅ LLM Evaluation rejected. ID: {}", evaluationId);
                }
            }
            
            execution.setVariable("evaluationRejected", true);
            
        } catch (Exception e) {
            log.error("❌ Error rejecting evaluation: {}", e.getMessage(), e);
            throw new RuntimeException("Rejection failed: " + e.getMessage(), e);
        }
    }
}
