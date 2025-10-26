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
 * Delegate: Manual Approve LLM Evaluation
 * Procesa aprobación manual de evaluación LLM
 * Proceso: 12_LLM_EVALUATION
 */
@Slf4j
@Component("manualApproveLlmEvaluationDelegate")
public class ManualApproveLlmEvaluationDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            log.info("✅ Processing Manual LLM Evaluation Approval");
            
            Long evaluationId = (Long) execution.getVariable("evaluationId");
            String approverNotes = (String) execution.getVariable("approverNotes");
            String approvedBy = (String) execution.getVariable("approvedBy");
            
            if (evaluationId != null) {
                LlmEvaluation eval = businessService.findById(LlmEvaluation.class, evaluationId);
                if (eval != null) {
                    eval.setEvalstatus("APPROVED");
                    eval.setEvalupdatedat(new Timestamp(System.currentTimeMillis()));
                    
                    // Construir descripción con información del aprobador
                    String description = String.format("✅ Aprobación manual por %s", 
                        approvedBy != null ? approvedBy : "MANUAL_REVIEWER");
                    if (approverNotes != null && !approverNotes.isEmpty()) {
                        description += " - Notas: " + approverNotes;
                    }
                    eval.setEvaldescription(description);
                    
                    businessService.save(eval);
                    
                    log.info("✅ LLM Evaluation manually approved. ID: {}", evaluationId);
                }
            }
            
            execution.setVariable("manualApprovalCompleted", true);
            
        } catch (Exception e) {
            log.error("❌ Error in manual approval: {}", e.getMessage(), e);
            throw new RuntimeException("Manual approval failed: " + e.getMessage(), e);
        }
    }
}
