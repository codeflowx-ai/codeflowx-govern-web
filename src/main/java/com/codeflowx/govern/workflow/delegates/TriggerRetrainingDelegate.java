package com.codeflowx.govern.workflow.delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate: Trigger Retraining
 * Dispara proceso de reentrenamiento de modelo
 * Proceso: 05_DRIFT_DETECTION
 */
@Slf4j
@Component("triggerRetrainingDelegate")
public class TriggerRetrainingDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        log.info("🔄 Triggering Model Retraining");
        
        Long modelId = (Long) execution.getVariable("modelId");
        String driftType = (String) execution.getVariable("driftType");
        String recommendations = (String) execution.getVariable("driftRecommendations");
        
        log.info("🚀 RETRAINING TRIGGERED:");
        log.info("   Model ID: {}", modelId);
        log.info("   Reason: Drift detected ({})", driftType);
        log.info("   Recommendations: {}", recommendations);
        
        // TODO: Integrar con pipeline de training (Kubeflow/MLflow/AirFlow)
        
        execution.setVariable("retrainingTriggered", true);
        execution.setVariable("retrainingStatus", "QUEUED");
    }
}
