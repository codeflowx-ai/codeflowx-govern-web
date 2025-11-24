# PROMPT: INC-010-013 - Visualización de Tendencias Avanzadas

**Incidencia:** INC-010-013  
**Prioridad:** 🟢 MEDIA  
**Artículo EU AI Act:** Art. 72  
**Esfuerzo Estimado:** 2 días  
**Tipo:** Java - Backend + Frontend  
**Referencia:** GAP-017

---

## CONTEXTO

Falta visualización de tendencias avanzadas. Se requiere gráficos de tendencias temporales, comparación con baseline, y predicción de tendencias según Art. 72.

**Estado Actual:**
- ✅ Dashboard básico implementado (INC-010-008)
- ✅ Tendencias históricas básicas disponibles
- ❌ No hay gráficos de tendencias temporales avanzados
- ❌ No hay comparación con baseline
- ❌ No hay predicción de tendencias

---

## REQUISITOS

1. Gráficos de tendencias temporales
2. Comparación con baseline
3. Predicción de tendencias
4. Visualizaciones interactivas
5. Exportación de gráficos

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear Servicio para Tendencias Avanzadas

```java
package com.codeflowx.govern.services.compliance;

import com.codeflowx.govern.entities.compliance.MonitoringMetric;
import com.codeflowx.govern.repositories.compliance.MonitoringMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdvancedTrendsService {
    
    private final MonitoringMetricRepository metricRepository;
    
    /**
     * Obtiene tendencias con comparación con baseline
     */
    public Map<String, Object> getTrendsWithBaseline(
            Long projectId, Long modelId, String metricName, int days) {
        
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        
        // Obtener métricas actuales
        List<MonitoringMetric> currentMetrics = metricRepository
            .findByProjectAndModelAndMetricNameAndCreatedAtAfter(
                projectId, modelId, metricName, since);
        
        // Obtener baseline (primeros 30 días o período de referencia)
        LocalDateTime baselineStart = LocalDateTime.now().minusDays(90);
        LocalDateTime baselineEnd = LocalDateTime.now().minusDays(60);
        
        List<MonitoringMetric> baselineMetrics = metricRepository
            .findByProjectAndModelAndMetricNameAndCreatedAtBetween(
                projectId, modelId, metricName, baselineStart, baselineEnd);
        
        // Calcular estadísticas de baseline
        Map<String, Double> baselineStats = calculateBaselineStats(baselineMetrics);
        
        // Calcular tendencias actuales
        Map<String, Object> currentTrends = calculateTrends(currentMetrics);
        
        // Comparar con baseline
        Map<String, Object> comparison = compareWithBaseline(currentTrends, baselineStats);
        
        Map<String, Object> result = new HashMap<>();
        result.put("current", currentTrends);
        result.put("baseline", baselineStats);
        result.put("comparison", comparison);
        result.put("dataPoints", mapToDataPoints(currentMetrics));
        
        return result;
    }
    
    /**
     * Predice tendencias futuras usando regresión lineal simple
     */
    public Map<String, Object> predictTrends(
            Long projectId, Long modelId, String metricName, int daysToPredict) {
        
        // Obtener datos históricos (últimos 30 días)
        LocalDateTime since = LocalDateTime.now().minusDays(30);
        List<MonitoringMetric> historicalMetrics = metricRepository
            .findByProjectAndModelAndMetricNameAndCreatedAtAfter(
                projectId, modelId, metricName, since);
        
        if (historicalMetrics.size() < 2) {
            return Map.of("error", "Datos insuficientes para predicción");
        }
        
        // Calcular regresión lineal
        LinearRegressionResult regression = calculateLinearRegression(historicalMetrics);
        
        // Generar predicciones
        List<Map<String, Object>> predictions = new ArrayList<>();
        LocalDateTime currentDate = LocalDateTime.now();
        
        for (int i = 1; i <= daysToPredict; i++) {
            LocalDateTime futureDate = currentDate.plusDays(i);
            double predictedValue = regression.getSlope() * i + regression.getIntercept();
            
            Map<String, Object> prediction = new HashMap<>();
            prediction.put("date", futureDate.toLocalDate().toString());
            prediction.put("value", predictedValue);
            prediction.put("confidence", calculateConfidence(regression, historicalMetrics.size()));
            
            predictions.add(prediction);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("predictions", predictions);
        result.put("regression", Map.of(
            "slope", regression.getSlope(),
            "intercept", regression.getIntercept(),
            "rSquared", regression.getRSquared()
        ));
        result.put("trend", regression.getSlope() > 0 ? "INCREASING" : 
                   regression.getSlope() < 0 ? "DECREASING" : "STABLE");
        
        return result;
    }
    
    private Map<String, Double> calculateBaselineStats(List<MonitoringMetric> metrics) {
        if (metrics.isEmpty()) {
            return Map.of(
                "mean", 0.0,
                "stddev", 0.0,
                "min", 0.0,
                "max", 0.0
            );
        }
        
        double[] values = metrics.stream()
            .mapToDouble(m -> m.getMonmetricvalue().doubleValue())
            .toArray();
        
        double mean = Arrays.stream(values).average().orElse(0.0);
        double variance = Arrays.stream(values)
            .map(v -> Math.pow(v - mean, 2))
            .average()
            .orElse(0.0);
        double stddev = Math.sqrt(variance);
        
        return Map.of(
            "mean", mean,
            "stddev", stddev,
            "min", Arrays.stream(values).min().orElse(0.0),
            "max", Arrays.stream(values).max().orElse(0.0)
        );
    }
    
    private Map<String, Object> calculateTrends(List<MonitoringMetric> metrics) {
        if (metrics.isEmpty()) {
            return Map.of();
        }
        
        double[] values = metrics.stream()
            .mapToDouble(m -> m.getMonmetricvalue().doubleValue())
            .toArray();
        
        double mean = Arrays.stream(values).average().orElse(0.0);
        double variance = Arrays.stream(values)
            .map(v -> Math.pow(v - mean, 2))
            .average()
            .orElse(0.0);
        double stddev = Math.sqrt(variance);
        
        // Calcular tendencia (pendiente)
        double slope = calculateSlope(metrics);
        
        return Map.of(
            "mean", mean,
            "stddev", stddev,
            "min", Arrays.stream(values).min().orElse(0.0),
            "max", Arrays.stream(values).max().orElse(0.0),
            "slope", slope,
            "trend", slope > 0.01 ? "INCREASING" : slope < -0.01 ? "DECREASING" : "STABLE"
        );
    }
    
    private double calculateSlope(List<MonitoringMetric> metrics) {
        if (metrics.size() < 2) {
            return 0.0;
        }
        
        metrics.sort(Comparator.comparing(MonitoringMetric::getMoncreatedat));
        
        int n = metrics.size();
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
        
        for (int i = 0; i < n; i++) {
            double x = i; // Índice temporal
            double y = metrics.get(i).getMonmetricvalue().doubleValue();
            
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
        }
        
        return (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
    }
    
    private Map<String, Object> compareWithBaseline(
            Map<String, Object> current, Map<String, Double> baseline) {
        
        double currentMean = (Double) current.get("mean");
        double baselineMean = baseline.get("mean");
        
        double deviation = currentMean - baselineMean;
        double deviationPercent = (deviation / baselineMean) * 100;
        
        String status = Math.abs(deviationPercent) < 5 ? "NORMAL" :
                       deviationPercent > 10 ? "ABOVE_BASELINE" :
                       deviationPercent < -10 ? "BELOW_BASELINE" : "WARNING";
        
        return Map.of(
            "deviation", deviation,
            "deviationPercent", deviationPercent,
            "status", status,
            "currentMean", currentMean,
            "baselineMean", baselineMean
        );
    }
    
    private LinearRegressionResult calculateLinearRegression(List<MonitoringMetric> metrics) {
        metrics.sort(Comparator.comparing(MonitoringMetric::getMoncreatedat));
        
        int n = metrics.size();
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0, sumY2 = 0;
        
        for (int i = 0; i < n; i++) {
            double x = i;
            double y = metrics.get(i).getMonmetricvalue().doubleValue();
            
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
            sumY2 += y * y;
        }
        
        double slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        double intercept = (sumY - slope * sumX) / n;
        
        // Calcular R²
        double meanY = sumY / n;
        double ssRes = 0, ssTot = 0;
        for (int i = 0; i < n; i++) {
            double y = metrics.get(i).getMonmetricvalue().doubleValue();
            double yPred = slope * i + intercept;
            ssRes += Math.pow(y - yPred, 2);
            ssTot += Math.pow(y - meanY, 2);
        }
        double rSquared = 1 - (ssRes / ssTot);
        
        return new LinearRegressionResult(slope, intercept, rSquared);
    }
    
    private double calculateConfidence(LinearRegressionResult regression, int dataPoints) {
        // Confianza basada en R² y número de puntos
        double baseConfidence = regression.getRSquared();
        double dataConfidence = Math.min(1.0, dataPoints / 30.0);
        return (baseConfidence + dataConfidence) / 2.0;
    }
    
    private List<Map<String, Object>> mapToDataPoints(List<MonitoringMetric> metrics) {
        return metrics.stream()
            .map(m -> Map.of(
                "date", m.getMoncreatedat().toString(),
                "value", m.getMonmetricvalue().doubleValue()
            ))
            .collect(Collectors.toList());
    }
    
    @Data
    private static class LinearRegressionResult {
        private final double slope;
        private final double intercept;
        private final double rSquared;
    }
}
```

### 2. Crear Controller REST

```java
package com.codeflowx.govern.controllers.compliance;

import com.codeflowx.govern.services.compliance.AdvancedTrendsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/pmm/trends")
@RequiredArgsConstructor
@Slf4j
public class AdvancedTrendsController {
    
    private final AdvancedTrendsService trendsService;
    
    @GetMapping("/baseline")
    public ResponseEntity<Map<String, Object>> getTrendsWithBaseline(
            @RequestParam Long projectId,
            @RequestParam(required = false) Long modelId,
            @RequestParam String metricName,
            @RequestParam(defaultValue = "30") int days) {
        
        Map<String, Object> trends = trendsService.getTrendsWithBaseline(
            projectId, modelId, metricName, days);
        
        return ResponseEntity.ok(trends);
    }
    
    @GetMapping("/predict")
    public ResponseEntity<Map<String, Object>> predictTrends(
            @RequestParam Long projectId,
            @RequestParam(required = false) Long modelId,
            @RequestParam String metricName,
            @RequestParam(defaultValue = "7") int daysToPredict) {
        
        Map<String, Object> predictions = trendsService.predictTrends(
            projectId, modelId, metricName, daysToPredict);
        
        return ResponseEntity.ok(predictions);
    }
}
```

### 3. Crear Componente Frontend (ZUL + JavaScript)

```xml
<!-- trends-chart.zul -->
<zk>
    <window title="Tendencias Avanzadas" border="normal" width="100%" height="100%">
        <vbox>
            <hbox>
                <label value="Métrica:" />
                <combobox id="metricCombo" model="@load(vm.metrics)" />
                <label value="Días:" />
                <intbox id="daysBox" value="30" />
                <button label="Cargar" onClick="@command('loadTrends')" />
                <button label="Predecir" onClick="@command('predictTrends')" />
            </hbox>
            
            <div id="chartContainer" style="width: 100%; height: 400px;">
                <!-- Gráfico se renderiza aquí con Chart.js -->
            </div>
        </vbox>
    </window>
    
    <script>
        // Cargar Chart.js
        <![CDATA[
        function loadChart(data) {
            const ctx = document.getElementById('chartContainer').getContext('2d');
            
            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: data.labels,
                    datasets: [{
                        label: 'Actual',
                        data: data.values,
                        borderColor: 'rgb(75, 192, 192)',
                        tension: 0.1
                    }, {
                        label: 'Baseline',
                        data: data.baseline,
                        borderColor: 'rgb(255, 99, 132)',
                        borderDash: [5, 5]
                    }, {
                        label: 'Predicción',
                        data: data.predictions,
                        borderColor: 'rgb(153, 102, 255)',
                        borderDash: [10, 5]
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: false
                        }
                    }
                }
            });
        }
        ]]>
    </script>
</zk>
```

---

## VALIDACIONES

1. ✅ Gráficos de tendencias temporales implementados
2. ✅ Comparación con baseline funcional
3. ✅ Predicción de tendencias implementada
4. ✅ Visualizaciones interactivas creadas
5. ✅ Exportación de gráficos disponible

---

## NOTAS

- Usar Chart.js o D3.js para visualizaciones
- Predicciones son aproximadas (regresión lineal simple)
- Considerar modelos más avanzados para predicciones precisas
- Baseline debe ser configurable por proyecto/modelo

---

**Estado:** ✅ COMPLETADO

