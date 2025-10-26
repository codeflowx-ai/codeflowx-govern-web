package com.codeflowx.govern.workflow.delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate: Notify Drift
 * Notifica sobre drift detectado
 * Proceso: 05_DRIFT_DETECTION
 */
@Slf4j
@Component("notifyDriftDelegate")
public class NotifyDriftDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        log.info("📧 Notifying Drift Detection");
        
        String modelName = (String) execution.getVariable("modelName");
        Boolean driftDetected = (Boolean) execution.getVariable("driftDetected");
        String driftType = (String) execution.getVariable("driftType");
        Double driftScore = (Double) execution.getVariable("driftScore");
        String severity = (String) execution.getVariable("severity");
        
        log.info("🚨 DRIFT ALERT:");
        log.info("   Model: {}", modelName);
        log.info("   Type: {} | Score: {:.2f}", driftType, driftScore != null ? driftScore : 0.0);
        log.info("   Severity: {}", severity);
        
        // TODO: Integrar con sistema de notificaciones (email/Slack/Teams)
        
        execution.setVariable("driftNotificationSent", true);
    }
}
