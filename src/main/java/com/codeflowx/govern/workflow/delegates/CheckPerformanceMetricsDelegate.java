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
 * Delegate: Check Performance Metrics
 * 
 * Verifica las métricas de performance de un modelo en producción
 * y detecta degradación comparando con baseline.
 * 
 * Proceso: 06_PERFORMANCE_DEGRADATION
 * 
 * JPA: PerformanceMetrics (tabla PRFPERFORMANCEMETRICS)
 * Propiedades: prfentitytype, prfentityid, prflatencyms, prfthroughput...
 */
@Slf4j
@Component("checkPerformanceMetricsDelegate")
public class CheckPerformanceMetricsDelegate implements JavaDelegate {

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            log.info("📊 Checking Performance Metrics...");

            // 1. Obtener variables del proceso
            Long modelId = (Long) execution.getVariable("modelId");
            Double latencyThreshold = (Double) execution.getVariable("latencyThreshold");
            Double throughputThreshold = (Double) execution.getVariable("throughputThreshold");
            
            if (latencyThreshold == null) latencyThreshold = 500.0; // ms
            if (throughputThreshold == null) throughputThreshold = 100.0; // req/s

            // 2. Obtener métricas recientes del modelo - SQL nativo
            String sql = "SELECT * FROM PRFPERFORMANCEMETRICS WHERE PRFENTITYTYPE = :entityType AND PRFENTITYID = :entityId ORDER BY PRFMEASUREDAT DESC LIMIT 10";
            
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("entityType", "MODEL");
            params.put("entityId", modelId);
            
            List<PerformanceMetrics> metrics = businessService.findByParams(PerformanceMetrics.class, sql, params);

            if (metrics.isEmpty()) {
                log.warn("⚠️ No performance metrics found for model ID: {}", modelId);
                execution.setVariable("performanceDegradation", false);
                execution.setVariable("noMetricsAvailable", true);
                return;
            }

            // 3. Calcular métricas promedio (últimas 10 mediciones)
            double avgLatency = metrics.stream()
                .filter(m -> m.getPrflatencyms() != null)
                .mapToDouble(PerformanceMetrics::getPrflatencyms)  // ✅ CORRECTO (no getPmlatency)
                .average()
                .orElse(0.0);
            
            double avgThroughput = metrics.stream()
                .filter(m -> m.getPrfthroughput() != null)
                .mapToDouble(PerformanceMetrics::getPrfthroughput)  // ✅ CORRECTO (no getPmthroughput)
                .average()
                .orElse(0.0);
            
            double avgErrorRate = metrics.stream()
                .filter(m -> m.getPrferrorrate() != null)
                .mapToDouble(PerformanceMetrics::getPrferrorrate)  // ✅ CORRECTO (no getPmerrorrate)
                .average()
                .orElse(0.0);

            // 4. Detectar degradación
            boolean latencyDegradation = avgLatency > latencyThreshold;
            boolean throughputDegradation = avgThroughput < throughputThreshold;
            boolean errorRateDegradation = avgErrorRate > 0.05; // 5%
            boolean performanceDegradation = latencyDegradation || throughputDegradation || errorRateDegradation;

            // 5. Calcular severidad
            String severity = "LOW";
            if (latencyDegradation && avgLatency > (latencyThreshold * 2)) {
                severity = "CRITICAL";
            } else if (errorRateDegradation && avgErrorRate > 0.10) {
                severity = "HIGH";
            } else if (performanceDegradation) {
                severity = "MEDIUM";
            }

            // 6. Guardar variables en proceso BPMN
            execution.setVariable("performanceDegradation", performanceDegradation);
            execution.setVariable("avgLatency", avgLatency);
            execution.setVariable("avgThroughput", avgThroughput);
            execution.setVariable("avgErrorRate", avgErrorRate);
            execution.setVariable("latencyDegradation", latencyDegradation);
            execution.setVariable("throughputDegradation", throughputDegradation);
            execution.setVariable("errorRateDegradation", errorRateDegradation);
            execution.setVariable("severity", severity);

            log.info("✅ Performance check completed:");
            log.info("   Latency: {:.0f}ms (threshold: {:.0f}ms)", avgLatency, latencyThreshold);
            log.info("   Throughput: {:.0f} req/s (threshold: {:.0f} req/s)", avgThroughput, throughputThreshold);
            log.info("   Error Rate: {:.2f}%", avgErrorRate * 100);
            log.info("   Degradation: {} | Severity: {}", performanceDegradation, severity);

        } catch (Exception e) {
            log.error("❌ Error checking performance metrics: {}", e.getMessage(), e);
            execution.setVariable("performanceCheckError", e.getMessage());
            execution.setVariable("performanceDegradation", true); // Por seguridad, asumimos degradación
            throw new RuntimeException("Performance check failed: " + e.getMessage(), e);
        }
    }
}
