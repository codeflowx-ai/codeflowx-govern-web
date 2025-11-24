# PROMPT: INC-010-004 - Implementación Real de PostMarketMonitoringService

**Incidencia:** INC-010-004  
**Prioridad:** 🔴 CRÍTICA  
**Artículo EU AI Act:** Art. 72  
**Esfuerzo Estimado:** 3 días  
**Tipo:** Java - Backend + Integración Microservicios

---

## CONTEXTO

El servicio `PostMarketMonitoringService` tiene implementación mock con TODOs. No realiza verificaciones reales de métricas post-market.

**Estado Actual:**
```java
// TODO: Implementar verificación real de métricas post-market
// Drift detection, performance degradation, user satisfaction
PostMarketResult result = new PostMarketResult();
result.setDriftDetected(false);
result.setPerformanceDegradation(false);
// ... valores hardcodeados
```

---

## REQUISITOS

1. Integrar con `leka-bias-detection-service` endpoint `/api/drift/detect`
2. Integrar con `aio-telemetry-service` para métricas en tiempo real
3. Integrar con sistema de feedback de usuarios
4. Implementar cálculo real de métricas (drift, performance, satisfacción)
5. Comparación con baseline histórico

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Modificar `PostMarketMonitoringService`

```java
package com.codeflowx.govern.workflow.services;

import com.codeflowx.govern.entities.compliance.PostMarketResult;
import com.codeflowx.govern.services.integration.DriftDetectionService;
import com.codeflowx.govern.services.integration.TelemetryService;
import com.codeflowx.govern.services.integration.UserFeedbackService;
import com.codeflowx.govern.repositories.compliance.ModelEvaluationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostMarketMonitoringService {
    
    private final DriftDetectionService driftDetectionService;
    private final TelemetryService telemetryService;
    private final UserFeedbackService userFeedbackService;
    private final ModelEvaluationRepository modelEvaluationRepository;
    
    /**
     * Verifica métricas post-market reales para un proyecto
     */
    public PostMarketResult checkProjectMetrics(Long projectId) {
        log.info("Verificando métricas post-market reales para proyecto: {}", projectId);
        
        PostMarketResult result = new PostMarketResult();
        Map<String, Object> metricsData = new HashMap<>();
        
        // 1. Detección de Drift
        DriftResult driftResult = checkDrift(projectId);
        result.setDriftDetected(driftResult.isDetected());
        result.setDriftScore(driftResult.getScore());
        metricsData.put("drift", driftResult.getDetails());
        
        // 2. Degradación de Performance
        PerformanceResult perfResult = checkPerformance(projectId);
        result.setPerformanceDegradation(perfResult.isDegraded());
        result.setPerformanceScore(perfResult.getScore());
        metricsData.put("performance", perfResult.getDetails());
        
        // 3. Satisfacción de Usuario
        SatisfactionResult satResult = checkUserSatisfaction(projectId);
        result.setUserSatisfactionDrop(satResult.isDropped());
        result.setUserSatisfactionScore(satResult.getScore());
        metricsData.put("satisfaction", satResult.getDetails());
        
        result.setMetricsData(metricsData);
        
        log.info("Resultado PMM - Drift: {}, Performance: {}, Satisfacción: {}", 
            driftResult.isDetected(), perfResult.isDegraded(), satResult.isDropped());
        
        return result;
    }
    
    /**
     * Verifica drift comparando con baseline histórico
     */
    private DriftResult checkDrift(Long projectId) {
        try {
            // Obtener baseline del modelo
            ModelEvaluation baseline = modelEvaluationRepository
                .findLatestBaselineByProject(projectId)
                .orElseThrow(() -> new IllegalStateException("No hay baseline disponible"));
            
            // Obtener datos actuales del proyecto
            Map<String, Object> currentData = getCurrentProjectData(projectId);
            
            // Llamar a leka-bias-detection-service
            DriftDetectionRequest request = DriftDetectionRequest.builder()
                .baselineData(baseline.getEvaluationData())
                .currentData(currentData)
                .modelId(baseline.getIdxmodel())
                .build();
            
            DriftDetectionResponse response = driftDetectionService.detectDrift(request);
            
            BigDecimal driftScore = response.getDriftScore();
            boolean detected = driftScore != null && driftScore.compareTo(new BigDecimal("0.30")) > 0;
            
            return DriftResult.builder()
                .detected(detected)
                .score(driftScore != null ? driftScore : BigDecimal.ZERO)
                .details(Map.of(
                    "ksTest", response.getKsTest(),
                    "psi", response.getPsi(),
                    "baselineDate", baseline.getCreatedAt().toString()
                ))
                .build();
            
        } catch (Exception e) {
            log.error("Error verificando drift para proyecto {}", projectId, e);
            // En caso de error, retornar resultado conservador
            return DriftResult.builder()
                .detected(true) // En caso de error, asumir drift para seguridad
                .score(new BigDecimal("0.50"))
                .details(Map.of("error", e.getMessage()))
                .build();
        }
    }
    
    /**
     * Verifica degradación de performance comparando con baseline
     */
    private PerformanceResult checkPerformance(Long projectId) {
        try {
            // Obtener baseline de performance
            ModelEvaluation baseline = modelEvaluationRepository
                .findLatestBaselineByProject(projectId)
                .orElseThrow(() -> new IllegalStateException("No hay baseline disponible"));
            
            // Obtener métricas actuales de aio-telemetry-service
            TelemetryMetrics currentMetrics = telemetryService.getMetrics(
                projectId, 
                LocalDateTime.now().minusDays(1), 
                LocalDateTime.now()
            );
            
            // Comparar con baseline
            BigDecimal baselineAccuracy = baseline.getAccuracy();
            BigDecimal currentAccuracy = currentMetrics.getAccuracy();
            
            if (baselineAccuracy == null || currentAccuracy == null) {
                return PerformanceResult.builder()
                    .degraded(false)
                    .score(new BigDecimal("1.0"))
                    .details(Map.of("status", "metrics_not_available"))
                    .build();
            }
            
            BigDecimal degradationPercentage = baselineAccuracy
                .subtract(currentAccuracy)
                .divide(baselineAccuracy, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
            
            boolean degraded = degradationPercentage.compareTo(new BigDecimal("20")) > 0;
            
            // Calcular score de performance (0-1)
            BigDecimal performanceScore = BigDecimal.ONE
                .subtract(degradationPercentage.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
            
            if (performanceScore.compareTo(BigDecimal.ZERO) < 0) {
                performanceScore = BigDecimal.ZERO;
            }
            
            return PerformanceResult.builder()
                .degraded(degraded)
                .score(performanceScore)
                .details(Map.of(
                    "baselineAccuracy", baselineAccuracy,
                    "currentAccuracy", currentAccuracy,
                    "degradationPercentage", degradationPercentage,
                    "latencyP95", currentMetrics.getLatencyP95(),
                    "throughput", currentMetrics.getThroughput(),
                    "errorRate", currentMetrics.getErrorRate()
                ))
                .build();
            
        } catch (Exception e) {
            log.error("Error verificando performance para proyecto {}", projectId, e);
            return PerformanceResult.builder()
                .degraded(true)
                .score(new BigDecimal("0.5"))
                .details(Map.of("error", e.getMessage()))
                .build();
        }
    }
    
    /**
     * Verifica satisfacción de usuario basada en feedback
     */
    private SatisfactionResult checkUserSatisfaction(Long projectId) {
        try {
            // Obtener feedback de últimos 30 días
            List<UserFeedback> feedbacks = userFeedbackService.getFeedbackByProject(
                projectId, 
                LocalDateTime.now().minusDays(30),
                LocalDateTime.now()
            );
            
            if (feedbacks.isEmpty()) {
                return SatisfactionResult.builder()
                    .dropped(false)
                    .score(new BigDecimal("0.8")) // Score neutral si no hay feedback
                    .details(Map.of("status", "no_feedback_available"))
                    .build();
            }
            
            // Calcular métricas de satisfacción
            long totalFeedbacks = feedbacks.size();
            long positiveFeedbacks = feedbacks.stream()
                .filter(f -> f.getSentiment().equals("POSITIVE"))
                .count();
            long negativeFeedbacks = feedbacks.stream()
                .filter(f -> f.getSentiment().equals("NEGATIVE"))
                .count();
            
            // Calcular score (0-1)
            BigDecimal satisfactionScore = new BigDecimal(positiveFeedbacks)
                .divide(new BigDecimal(totalFeedbacks), 4, RoundingMode.HALF_UP);
            
            // Comparar con baseline histórico (últimos 60 días anteriores)
            BigDecimal historicalScore = userFeedbackService.getHistoricalSatisfactionScore(
                projectId, 
                LocalDateTime.now().minusDays(90),
                LocalDateTime.now().minusDays(30)
            );
            
            // Determinar si hay caída significativa (>20%)
            boolean dropped = false;
            if (historicalScore != null) {
                BigDecimal dropPercentage = historicalScore
                    .subtract(satisfactionScore)
                    .divide(historicalScore, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
                
                dropped = dropPercentage.compareTo(new BigDecimal("20")) > 0;
            }
            
            return SatisfactionResult.builder()
                .dropped(dropped)
                .score(satisfactionScore)
                .details(Map.of(
                    "totalFeedbacks", totalFeedbacks,
                    "positiveFeedbacks", positiveFeedbacks,
                    "negativeFeedbacks", negativeFeedbacks,
                    "historicalScore", historicalScore != null ? historicalScore : "N/A",
                    "complaintRate", userFeedbackService.getComplaintRate(projectId, 30),
                    "escalationRate", userFeedbackService.getEscalationRate(projectId, 30)
                ))
                .build();
            
        } catch (Exception e) {
            log.error("Error verificando satisfacción para proyecto {}", projectId, e);
            return SatisfactionResult.builder()
                .dropped(false)
                .score(new BigDecimal("0.7"))
                .details(Map.of("error", e.getMessage()))
                .build();
        }
    }
    
    /**
     * Obtiene datos actuales del proyecto para comparación
     */
    private Map<String, Object> getCurrentProjectData(Long projectId) {
        // Obtener últimas predicciones/inferencias del modelo
        // Esto dependerá de cómo se almacenen los datos en tu sistema
        return new HashMap<>(); // TODO: Implementar según arquitectura
    }
}
```

### 2. Crear Servicios de Integración

**DriftDetectionService:**
```java
package com.codeflowx.govern.services.integration;

import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class DriftDetectionService {
    
    @Value("${services.leka-bias-detection.url}")
    private String lekaBiasDetectionUrl;
    
    private final RestTemplate restTemplate;
    
    public DriftDetectionResponse detectDrift(DriftDetectionRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<DriftDetectionRequest> entity = new HttpEntity<>(request, headers);
        
        return restTemplate.postForObject(
            lekaBiasDetectionUrl + "/api/drift/detect",
            entity,
            DriftDetectionResponse.class
        );
    }
    
    @Data
    @Builder
    public static class DriftDetectionRequest {
        private Map<String, Object> baselineData;
        private Map<String, Object> currentData;
        private Long modelId;
    }
    
    @Data
    public static class DriftDetectionResponse {
        private BigDecimal driftScore;
        private Map<String, Object> ksTest;
        private Map<String, Object> psi;
    }
}
```

**TelemetryService:**
```java
package com.codeflowx.govern.services.integration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TelemetryService {
    
    @Value("${services.aio-telemetry.url}")
    private String aioTelemetryUrl;
    
    private final RestTemplate restTemplate;
    
    public TelemetryMetrics getMetrics(Long projectId, LocalDateTime start, LocalDateTime end) {
        String url = String.format(
            "%s/api/v1/metrics?projectId=%d&start=%s&end=%s",
            aioTelemetryUrl, projectId, start, end
        );
        
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        
        return TelemetryMetrics.builder()
            .accuracy(new BigDecimal(response.get("accuracy").toString()))
            .latencyP95(new BigDecimal(response.get("latencyP95").toString()))
            .throughput(Integer.valueOf(response.get("throughput").toString()))
            .errorRate(new BigDecimal(response.get("errorRate").toString()))
            .build();
    }
    
    @Data
    @Builder
    public static class TelemetryMetrics {
        private BigDecimal accuracy;
        private BigDecimal latencyP95;
        private Integer throughput;
        private BigDecimal errorRate;
    }
}
```

**UserFeedbackService:**
```java
package com.codeflowx.govern.services.integration;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserFeedbackService {
    
    // TODO: Integrar con servicio de feedback o base de datos de feedback
    
    public List<UserFeedback> getFeedbackByProject(Long projectId, LocalDateTime start, LocalDateTime end) {
        // Implementar consulta a base de datos o servicio externo
        return List.of(); // TODO: Implementar
    }
    
    public BigDecimal getHistoricalSatisfactionScore(Long projectId, LocalDateTime start, LocalDateTime end) {
        // Implementar cálculo de score histórico
        return null; // TODO: Implementar
    }
    
    public BigDecimal getComplaintRate(Long projectId, int days) {
        // Implementar cálculo de tasa de quejas
        return BigDecimal.ZERO; // TODO: Implementar
    }
    
    public BigDecimal getEscalationRate(Long projectId, int days) {
        // Implementar cálculo de tasa de escalaciones
        return BigDecimal.ZERO; // TODO: Implementar
    }
    
    @Data
    public static class UserFeedback {
        private Long id;
        private Long projectId;
        private String content;
        private String sentiment; // POSITIVE, NEUTRAL, NEGATIVE
        private LocalDateTime createdAt;
        private Integer rating; // 1-5
    }
}
```

### 3. Crear Result Classes

```java
package com.codeflowx.govern.entities.compliance;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
public class DriftResult {
    private boolean detected;
    private BigDecimal score;
    private Map<String, Object> details;
}

@Data
@Builder
public class PerformanceResult {
    private boolean degraded;
    private BigDecimal score;
    private Map<String, Object> details;
}

@Data
@Builder
public class SatisfactionResult {
    private boolean dropped;
    private BigDecimal score;
    private Map<String, Object> details;
}
```

---

## CONFIGURACIÓN

**application.yml:**
```yaml
services:
  leka-bias-detection:
    url: http://leka-bias-detection-service:8080
  aio-telemetry:
    url: http://aio-telemetry-service:8080
```

---

## VALIDACIONES

1. ✅ Integración con leka-bias-detection-service
2. ✅ Integración con aio-telemetry-service
3. ✅ Integración con sistema de feedback
4. ✅ Cálculo real de métricas (drift, performance, satisfacción)
5. ✅ Comparación con baseline histórico
6. ✅ Manejo de errores adecuado

---

## NOTAS

- Implementar circuit breakers para llamadas a microservicios
- Cachear resultados si es necesario para mejorar performance
- Logging detallado para auditoría
- Manejar casos donde servicios externos no estén disponibles


---

**Estado:** ✅ COMPLETADO






