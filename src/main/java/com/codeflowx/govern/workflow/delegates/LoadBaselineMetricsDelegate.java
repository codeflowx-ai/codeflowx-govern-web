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
 * Delegate: Load Baseline Metrics
 * 
 * Carga las métricas baseline (referencia) de un modelo
 * para comparar con métricas actuales.
 * 
 * Proceso: 05_DRIFT_DETECTION
 * JPA: PerformanceMetrics (prefijo PRF)
 */
@Slf4j
@Component("loadBaselineMetricsDelegate")
public class LoadBaselineMetricsDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            log.info("📥 Loading Baseline Metrics...");

            Long modelId = (Long) execution.getVariable("modelId");

            // Cargar métricas baseline (primeras 100 mediciones) - SQL nativo
            String sql = "SELECT * FROM PRFPERFORMANCEMETRICS WHERE PRFENTITYTYPE = :entityType AND PRFENTITYID = :entityId ORDER BY PRFMEASUREDAT ASC LIMIT 100";
            
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("entityType", "MODEL");
            params.put("entityId", modelId);
            
            List<PerformanceMetrics> baselineMetrics = businessService.findByParams(PerformanceMetrics.class, sql, params);

            if (baselineMetrics.isEmpty()) {
                log.warn("⚠️ No baseline metrics found for model ID: {}", modelId);
                execution.setVariable("baselineAvailable", false);
                return;
            }

            // Calcular estadísticas baseline
            double baselineLatency = baselineMetrics.stream()
                .filter(m -> m.getPrflatencyms() != null)
                .mapToDouble(PerformanceMetrics::getPrflatencyms)
                .average()
                .orElse(0.0);
            
            double baselineThroughput = baselineMetrics.stream()
                .filter(m -> m.getPrfthroughput() != null)
                .mapToDouble(PerformanceMetrics::getPrfthroughput)
                .average()
                .orElse(0.0);

            execution.setVariable("baselineAvailable", true);
            execution.setVariable("baselineLatency", baselineLatency);
            execution.setVariable("baselineThroughput", baselineThroughput);
            execution.setVariable("baselineSampleSize", baselineMetrics.size());

            log.info("✅ Baseline metrics loaded:");
            log.info("   Latency: {:.0f}ms", baselineLatency);
            log.info("   Throughput: {:.0f} req/s", baselineThroughput);
            log.info("   Sample size: {} measurements", baselineMetrics.size());

        } catch (Exception e) {
            log.error("❌ Error loading baseline metrics: {}", e.getMessage(), e);
            execution.setVariable("baselineLoadError", e.getMessage());
            execution.setVariable("baselineAvailable", false);
            throw new RuntimeException("Failed to load baseline metrics: " + e.getMessage(), e);
        }
    }
}
