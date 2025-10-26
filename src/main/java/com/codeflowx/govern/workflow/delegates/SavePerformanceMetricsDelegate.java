package com.codeflowx.govern.workflow.delegates;

import java.sql.Timestamp;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.monitoring.PerformanceMetrics;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate: Save Performance Metrics
 * 
 * Persiste las métricas de performance capturadas durante
 * el análisis de degradación.
 * 
 * Proceso: 06_PERFORMANCE_DEGRADATION
 * JPA: PerformanceMetrics (prefijo PRF)
 */
@Slf4j
@Component("savePerformanceMetricsDelegate")
public class SavePerformanceMetricsDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            log.info("💾 Saving Performance Metrics...");

            Long modelId = (Long) execution.getVariable("modelId");
            Double avgLatency = (Double) execution.getVariable("avgLatency");
            Double avgThroughput = (Double) execution.getVariable("avgThroughput");
            Double avgErrorRate = (Double) execution.getVariable("avgErrorRate");
            Boolean performanceDegradation = (Boolean) execution.getVariable("performanceDegradation");
            String severity = (String) execution.getVariable("severity");
            String createdBy = (String) execution.getVariable("initiator");

            // Crear registro de métricas (usando propiedades CORRECTAS con prefijo PRF)
            PerformanceMetrics pm = new PerformanceMetrics();
            pm.setPrfentitytype("MODEL");  // ✅ CORRECTO (no pmmodelid)
            pm.setPrfentityid(modelId);    // ✅ CORRECTO
            pm.setPrflatencyms(avgLatency != null ? avgLatency : 0.0);  // ✅ CORRECTO (no pmlatency)
            pm.setPrfthroughput(avgThroughput != null ? avgThroughput : 0.0);  // ✅ CORRECTO
            pm.setPrferrorrate(avgErrorRate != null ? avgErrorRate : 0.0);  // ✅ CORRECTO
            pm.setPrfdegradationdetected(performanceDegradation != null ? performanceDegradation : false);  // ✅ CORRECTO
            pm.setPrfstatus(severity != null ? severity : "NORMAL");  // ✅ CORRECTO (no pmseverity)
            pm.setPrfmeasuredat(new Timestamp(System.currentTimeMillis()));  // ✅ CORRECTO (no pmtimestamp)
            pm.setPrfcreatedat(new Timestamp(System.currentTimeMillis()));  // ✅ CORRECTO
            pm.setPrfcreatedby(createdBy);  // ✅ CORRECTO

            businessService.save(pm);
            
            execution.setVariable("performanceMetricsId", pm.getIdxperformancemetric());

            log.info("✅ Performance metrics saved. ID: {}", pm.getIdxperformancemetric());
            log.info("   Degradation: {} | Status: {}", pm.getPrfdegradationdetected(), pm.getPrfstatus());

        } catch (Exception e) {
            log.error("❌ Error saving performance metrics: {}", e.getMessage(), e);
            execution.setVariable("saveMetricsError", e.getMessage());
            throw new RuntimeException("Failed to save performance metrics: " + e.getMessage(), e);
        }
    }
}
