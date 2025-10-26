package com.codeflowx.govern.workflow.delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.workflow.services.NotificationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("notifyLlmEvaluationDelegate")
public class NotifyLlmEvaluationDelegate implements JavaDelegate {

    @Autowired
    private NotificationService notificationService;

    @Override
    public void execute(DelegateExecution execution) {
        String modelName = (String) execution.getVariable("model_name");
        Double overallScore = (Double) execution.getVariable("overall_score");
        Long evaluationId = (Long) execution.getVariable("evaluationId");

        String status = overallScore >= 0.80 ? "APPROVED" : "REQUIRES_ATTENTION";

        log.info("📧 Notificando resultado evaluación LLM: modelo={}, score={}", modelName, overallScore);

        try {
            // Usar método existente de NotificationService
            notificationService.notifyApproval(
                "LLM_EVALUATION",
                evaluationId,
                execution.getProcessInstanceId()
            );

            log.info("✅ Notificación enviada - Modelo: {}, Score: {}, Status: {}", 
                     modelName, overallScore, status);

        } catch (Exception e) {
            log.error("❌ Error enviando notificaciones", e);
            // No fallar el proceso por error en notificación
        }
    }
}


