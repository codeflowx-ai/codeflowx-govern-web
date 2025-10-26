package com.codeflowx.govern.workflow.delegates;

import java.util.List;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codeflowx.govern.entity.monitoring.PerformanceMetrics;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate: Capture Current Metrics
 * 
 * Captura las métricas actuales de un modelo en producción
 * para comparar con baseline y detectar drift/degradación.
 * 
 * Proceso: 05_DRIFT_DETECTION
 * JPA: PerformanceMetrics (prefijo PRF)
 */
@Slf4j
@Component("captureCurrentMetricsDelegate")
public class CaptureCurrentMetricsDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            log.info("📸 Capturing Current Metrics...");

            Long modelId = (Long) execution.getVariable("modelId");

            // Capturar métricas recientes (últimas 50 mediciones) - SQL nativo
            String sql = "SELECT * FROM PRFPERFORMANCEMETRICS WHERE PRFENTITYTYPE = :entityType AND PRFENTITYID = :entityId ORDER BY PRFMEASUREDAT DESC LIMIT 50";
            
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("entityType", "MODEL");
            params.put("entityId", modelId);
            
            List<PerformanceMetrics> currentMetrics = businessService.findByParams(PerformanceMetrics.class, sql, params);
            
            if (currentMetrics.isEmpty()) {
                log.warn("⚠️ No current metrics found for model ID: {}", modelId);
                execution.setVariable("currentMetricsAvailable", false);
                return;
            }

            // Calcular estadísticas actuales
            double currentLatency = currentMetrics.stream()
                .filter(m -> m.getPrflatencyms() != null)
                .mapToDouble(PerformanceMetrics::getPrflatencyms)
                .average()
                .orElse(0.0);
            
            double currentThroughput = currentMetrics.stream()
                .filter(m -> m.getPrfthroughput() != null)
                .mapToDouble(PerformanceMetrics::getPrfthroughput)
                .average()
                .orElse(0.0);

            double currentErrorRate = currentMetrics.stream()
                .filter(m -> m.getPrferrorrate() != null)
                .mapToDouble(PerformanceMetrics::getPrferrorrate)
                .average()
                .orElse(0.0);

            execution.setVariable("currentMetricsAvailable", true);
            execution.setVariable("currentLatency", currentLatency);
            execution.setVariable("currentThroughput", currentThroughput);
            execution.setVariable("currentErrorRate", currentErrorRate);
            execution.setVariable("currentSampleSize", currentMetrics.size());

            log.info("✅ Current metrics captured:");
            log.info("   Latency: {:.0f}ms", currentLatency);
            log.info("   Throughput: {:.0f} req/s", currentThroughput);
            log.info("   Error rate: {:.2f}%", currentErrorRate * 100);
            log.info("   Sample size: {} measurements", currentMetrics.size());

        } catch (Exception e) {
            log.error("❌ Error capturing current metrics: {}", e.getMessage(), e);
            execution.setVariable("currentMetricsCaptureError", e.getMessage());
            execution.setVariable("currentMetricsAvailable", false);
            throw new RuntimeException("Failed to capture current metrics: " + e.getMessage(), e);
        }
    }
}
