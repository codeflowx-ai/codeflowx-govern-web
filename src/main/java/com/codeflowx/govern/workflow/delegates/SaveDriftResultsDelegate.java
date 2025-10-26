package com.codeflowx.govern.workflow.delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.monitoring.DriftDetection;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate: Save Drift Results
 * Persiste los resultados finales de análisis de drift
 * Proceso: 05_DRIFT_DETECTION
 * JPA: DriftDetection (prefijo DRF)
 */
@Slf4j
@Component("saveDriftResultsDelegate")
public class SaveDriftResultsDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            log.info("💾 Saving Drift Results");
            
            Long driftDetectionId = (Long) execution.getVariable("driftDetectionId");
            
            if (driftDetectionId != null) {
                DriftDetection dd = businessService.findById(DriftDetection.class, driftDetectionId);
                if (dd != null) {
                    // Actualizar estado final (usando propiedades CORRECTAS con prefijo DRF)
                    dd.setDrfstatus("COMPLETED");  // ✅ CORRECTO (no ddstatus)
                    businessService.save(dd);
                    log.info("✅ Drift results saved. ID: {}", driftDetectionId);
                }
            }
            
            execution.setVariable("driftResultsSaved", true);
            
        } catch (Exception e) {
            log.error("❌ Error saving drift results: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save results: " + e.getMessage(), e);
        }
    }
}
