package com.codeflowx.govern.workflow.delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate: Log Low Alert
 * Registra alertas de severidad baja
 * Proceso: 10_ALERT_RESPONSE
 */
@Slf4j
@Component("logLowAlertDelegate")
public class LogLowAlertDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        log.info("📝 Logging Low Priority Alert");
        
        String alertType = (String) execution.getVariable("alertType");
        String severity = (String) execution.getVariable("severity");
        String message = (String) execution.getVariable("alertMessage");
        
        log.info("ℹ️ LOW ALERT:");
        log.info("   Type: {} | Severity: {}", alertType, severity);
        log.info("   Message: {}", message);
        
        execution.setVariable("lowAlertLogged", true);
    }
}
