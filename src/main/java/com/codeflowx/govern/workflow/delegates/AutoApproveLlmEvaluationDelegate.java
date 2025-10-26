package com.codeflowx.govern.workflow.delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.evaluation.LlmEvaluation;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("autoApproveLlmEvaluationDelegate")
public class AutoApproveLlmEvaluationDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        Long evaluationId = (Long) execution.getVariable("evaluation_id");
        Double overallScore = (Double) execution.getVariable("overall_score");

        log.info("✅ Auto-aprobando evaluación LLM: evaluationId={}, score={}", evaluationId, overallScore);

        try {
            LlmEvaluation evaluation = businessService.findById(LlmEvaluation.class, evaluationId);
            evaluation.setEvaldescription(String.format(
                "✅ Auto-aprobado - Score: %.2f (>= 80%%)", overallScore));
            evaluation.setEvalupdatedat(java.sql.Timestamp.from(java.time.Instant.now()));

            businessService.save(evaluation);

            execution.setVariable("auto_approved", true);
            log.info("✅ Evaluación {} auto-aprobada", evaluationId);

        } catch (Exception e) {
            log.error("❌ Error auto-aprobando evaluación", e);
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }
}


