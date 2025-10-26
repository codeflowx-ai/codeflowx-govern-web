package com.codeflowx.govern.workflow.delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate: Check Auto Scaling
 * Verifica si el modelo puede auto-escalarse
 * Proceso: 06_PERFORMANCE_DEGRADATION
 */
@Slf4j
@Component("checkAutoScalingDelegate")
public class CheckAutoScalingDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        log.info("🔍 Checking Auto-Scaling Capability");
        
        Long modelId = (Long) execution.getVariable("modelId");
        String deploymentType = (String) execution.getVariable("deploymentType");
        Integer currentReplicas = (Integer) execution.getVariable("currentReplicas");
        Integer maxReplicas = (Integer) execution.getVariable("maxReplicas");
        
        if (currentReplicas == null) currentReplicas = 1;
        if (maxReplicas == null) maxReplicas = 10;
        
        // Verificar si puede escalar
        boolean canAutoScale = "KUBERNETES".equalsIgnoreCase(deploymentType) && currentReplicas < maxReplicas;
        
        log.info("📊 Auto-Scaling Check:");
        log.info("   Model ID: {}", modelId);
        log.info("   Deployment: {}", deploymentType);
        log.info("   Current Replicas: {} / Max: {}", currentReplicas, maxReplicas);
        log.info("   Can Auto-Scale: {}", canAutoScale);
        
        execution.setVariable("canAutoScale", canAutoScale);
        execution.setVariable("currentReplicas", currentReplicas);
        execution.setVariable("targetReplicas", Math.min(currentReplicas + 1, maxReplicas));
    }
}
