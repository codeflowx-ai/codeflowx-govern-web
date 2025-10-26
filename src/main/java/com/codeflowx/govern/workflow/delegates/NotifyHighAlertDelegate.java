package com.codeflowx.govern.workflow.delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate: Notify High Alert
 * Notifica alertas de severidad alta
 * Proceso: 10_ALERT_RESPONSE
 */
@Slf4j
@Component("notifyHighAlertDelegate")
public class NotifyHighAlertDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        log.info("🚨 Notifying High Priority Alert");
        
        String alertType = (String) execution.getVariable("alertType");
        String severity = (String) execution.getVariable("severity");
        String message = (String) execution.getVariable("alertMessage");
        
        log.warn("⚠️ HIGH ALERT:");
        log.warn("   Type: {} | Severity: {}", alertType, severity);
        log.warn("   Message: {}", message);
        
        // TODO: Integrar con sistema de notificaciones urgentes (PagerDuty/OpsGenie)
        
        execution.setVariable("highAlertNotificationSent", true);
    }
}
