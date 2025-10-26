package com.codeflowx.govern.workflow.delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate: Notify Bias Analysis
 * Notifica a stakeholders sobre resultados de análisis de sesgo
 * Proceso: 08_BIAS_DETECTION
 */
@Slf4j
@Component("notifyBiasAnalysisDelegate")
public class NotifyBiasAnalysisDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        log.info("📧 Notifying Bias Analysis Results");
        
        Boolean biasDetected = (Boolean) execution.getVariable("biasDetected");
        String modelName = (String) execution.getVariable("modelName");
        Double overallFairness = (Double) execution.getVariable("overallFairness");
        
        // TODO: Integrar con sistema de notificaciones (email/Slack/Teams)
        log.info("   Model: {} | Bias Detected: {} | Fairness Score: {:.2f}", 
                 modelName, biasDetected, overallFairness != null ? overallFairness : 0.0);
        
        execution.setVariable("biasNotificationSent", true);
    }
}
