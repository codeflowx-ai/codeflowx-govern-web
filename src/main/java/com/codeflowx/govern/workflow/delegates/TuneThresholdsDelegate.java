package com.codeflowx.govern.workflow.delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * Delegate: Tune Thresholds
 * Ajusta umbrales de performance basado en métricas
 * Proceso: 06_PERFORMANCE_DEGRADATION
 */
@Slf4j
@Component("tuneThresholdsDelegate")
public class TuneThresholdsDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        log.info("🎛️ Tuning Performance Thresholds");
        
        Double currentLatency = (Double) execution.getVariable("avgLatency");
        Double currentThroughput = (Double) execution.getVariable("avgThroughput");
        
        // Ajustar umbrales (10% de margen)
        Double newLatencyThreshold = currentLatency != null ? currentLatency * 1.10 : 500.0;
        Double newThroughputThreshold = currentThroughput != null ? currentThroughput * 0.90 : 100.0;
        
        log.info("📊 New Thresholds:");
        log.info("   Latency: {:.0f}ms (was: {:.0f}ms)", newLatencyThreshold, currentLatency);
        log.info("   Throughput: {:.0f} req/s", newThroughputThreshold);
        
        execution.setVariable("latencyThreshold", newLatencyThreshold);
        execution.setVariable("throughputThreshold", newThroughputThreshold);
        execution.setVariable("thresholdsTuned", true);
    }
}
