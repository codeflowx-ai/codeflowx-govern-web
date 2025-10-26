package com.codeflowx.govern.workflow.delegates;

import java.sql.Timestamp;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.agents.AgentAlert;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate: Update Alert Status
 * Actualiza el estado de una alerta después de procesarla
 * Proceso: 10_ALERT_RESPONSE
 */
@Slf4j
@Component("updateAlertStatusDelegate")
public class UpdateAlertStatusDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            log.info("🔄 Updating Alert Status");
            
            Long alertId = (Long) execution.getVariable("alertId");
            String newStatus = (String) execution.getVariable("alertStatus");
            
            if (alertId != null) {
                AgentAlert alert = businessService.findById(AgentAlert.class, alertId);
                if (alert != null) {
                    alert.setAgtstatus(newStatus != null ? newStatus : "RESOLVED");
                    alert.setAgtresolvedat(new Timestamp(System.currentTimeMillis()));
                    businessService.save(alert);
                    
                    log.info("✅ Alert status updated. ID: {} | Status: {}", alertId, newStatus);
                }
            }
            
            execution.setVariable("alertStatusUpdated", true);
            
        } catch (Exception e) {
            log.error("❌ Error updating alert status: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update alert status: " + e.getMessage(), e);
        }
    }
}
