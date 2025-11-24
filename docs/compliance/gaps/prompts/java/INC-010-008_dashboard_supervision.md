# PROMPT: INC-010-008 - Dashboard de Supervisión Continua

**Incidencia:** INC-010-008  
**Prioridad:** 🟡 ALTA  
**Artículo EU AI Act:** Art. 72  
**Esfuerzo Estimado:** 3 días  
**Tipo:** Java - Backend + Frontend  
**Referencia:** GAP-017

---

## CONTEXTO

Dashboard mencionado en documentación pero requiere verificación de implementación UI. Se necesita un dashboard completo para visualizar métricas en tiempo real, tendencias históricas, alertas activas y estado de proyectos/modelos según Art. 72.

**Estado Actual:**
- ✅ Dashboard Compliance mencionado en documentación funcional
- ✅ Métricas almacenadas en `MONMONITORINGMETRICS`
- ✅ Alertas almacenadas en `MONMONITORINGALERTS`
- ❌ Falta verificación de implementación UI
- ❌ Falta visualización de tendencias históricas
- ❌ Falta exportación automática de reportes

---

## REQUISITOS

1. Verificar si existe dashboard implementado
2. Si no existe, crear dashboard con:
   - Métricas en tiempo real
   - Tendencias históricas
   - Alertas activas
   - Estado de proyectos/modelos
3. Exportación CSV/PDF
4. Filtros por proyecto, modelo, fecha
5. Gráficos y visualizaciones

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear Servicio para Dashboard

```java
package com.codeflowx.govern.services.compliance;

import com.codeflowx.govern.entities.compliance.MonitoringAlert;
import com.codeflowx.govern.entities.compliance.MonitoringMetric;
import com.codeflowx.govern.repositories.compliance.MonitoringAlertRepository;
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
public class PmmDashboardService {
    
    private final MonitoringMetricRepository metricRepository;
    private final MonitoringAlertRepository alertRepository;
    
    /**
     * Obtiene métricas en tiempo real para dashboard
     */
    public Map<String, Object> getRealTimeMetrics(Long projectId, Long modelId) {
        Map<String, Object> metrics = new HashMap<>();
        
        // Obtener últimas métricas (últimas 24 horas)
        LocalDateTime since = LocalDateTime.now().minusHours(24);
        
        List<MonitoringMetric> recentMetrics = metricRepository
            .findByProjectAndModelAndCreatedAtAfter(projectId, modelId, since);
        
        // Agrupar por tipo de métrica
        Map<String, List<MonitoringMetric>> metricsByType = recentMetrics.stream()
            .collect(Collectors.groupingBy(MonitoringMetric::getMonmetricname));
        
        // Calcular promedios y tendencias
        metricsByType.forEach((metricName, metricList) -> {
            Map<String, Object> metricData = new HashMap<>();
            
            double average = metricList.stream()
                .mapToDouble(m -> m.getMonmetricvalue().doubleValue())
                .average()
                .orElse(0.0);
            
            double latest = metricList.stream()
                .max(Comparator.comparing(MonitoringMetric::getMoncreatedat))
                .map(m -> m.getMonmetricvalue().doubleValue())
                .orElse(0.0);
            
            metricData.put("average", average);
            metricData.put("latest", latest);
            metricData.put("trend", calculateTrend(metricList));
            metricData.put("count", metricList.size());
            
            metrics.put(metricName, metricData);
        });
        
        return metrics;
    }
    
    /**
     * Obtiene tendencias históricas
     */
    public Map<String, List<Map<String, Object>>> getHistoricalTrends(
            Long projectId, Long modelId, int days) {
        
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        
        List<MonitoringMetric> metrics = metricRepository
            .findByProjectAndModelAndCreatedAtAfter(projectId, modelId, since);
        
        // Agrupar por día y tipo de métrica
        Map<String, List<Map<String, Object>>> trends = new HashMap<>();
        
        metrics.stream()
            .collect(Collectors.groupingBy(
                m -> m.getMonmetricname(),
                Collectors.groupingBy(m -> m.getMoncreatedat().toLocalDate())
            ))
            .forEach((metricName, dailyMetrics) -> {
                List<Map<String, Object>> dailyData = dailyMetrics.entrySet().stream()
                    .map(entry -> {
                        Map<String, Object> dayData = new HashMap<>();
                        dayData.put("date", entry.getKey().toString());
                        
                        double avg = entry.getValue().stream()
                            .mapToDouble(m -> m.getMonmetricvalue().doubleValue())
                            .average()
                            .orElse(0.0);
                        
                        dayData.put("value", avg);
                        return dayData;
                    })
                    .sorted(Comparator.comparing(m -> (String) m.get("date")))
                    .collect(Collectors.toList());
                
                trends.put(metricName, dailyData);
            });
        
        return trends;
    }
    
    /**
     * Obtiene alertas activas
     */
    public List<Map<String, Object>> getActiveAlerts(Long projectId, Long modelId) {
        List<MonitoringAlert> alerts = alertRepository
            .findByProjectAndModelAndStatusContaining(projectId, modelId, "ACTIVE");
        
        return alerts.stream()
            .map(this::mapAlertToDisplay)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtiene estado de proyectos/modelos
     */
    public Map<String, Object> getProjectModelStatus(Long projectId, Long modelId) {
        Map<String, Object> status = new HashMap<>();
        
        // Obtener última verificación PMM
        // Obtener plan PMM activo
        // Obtener última alerta
        // Calcular score de cumplimiento
        
        return status;
    }
    
    /**
     * Calcula tendencia (UP, DOWN, STABLE)
     */
    private String calculateTrend(List<MonitoringMetric> metrics) {
        if (metrics.size() < 2) {
            return "STABLE";
        }
        
        metrics.sort(Comparator.comparing(MonitoringMetric::getMoncreatedat));
        
        double first = metrics.get(0).getMonmetricvalue().doubleValue();
        double last = metrics.get(metrics.size() - 1).getMonmetricvalue().doubleValue();
        
        double change = ((last - first) / first) * 100;
        
        if (change > 5) {
            return "UP";
        } else if (change < -5) {
            return "DOWN";
        } else {
            return "STABLE";
        }
    }
    
    private Map<String, Object> mapAlertToDisplay(MonitoringAlert alert) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", alert.getIdxmonalert());
        map.put("type", alert.getMonalerttype());
        map.put("severity", alert.getMonseverity());
        map.put("status", alert.getMonstatus());
        map.put("triggeredAt", alert.getMontriggeredat());
        map.put("description", alert.getMonalertdata());
        return map;
    }
}
```

### 2. Crear Controller REST

```java
package com.codeflowx.govern.controllers.compliance;

import com.codeflowx.govern.services.compliance.PmmDashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/pmm/dashboard")
@RequiredArgsConstructor
@Slf4j
public class PmmDashboardController {
    
    private final PmmDashboardService dashboardService;
    
    @GetMapping("/metrics/realtime")
    public ResponseEntity<Map<String, Object>> getRealTimeMetrics(
            @RequestParam Long projectId,
            @RequestParam(required = false) Long modelId) {
        
        Map<String, Object> metrics = dashboardService.getRealTimeMetrics(projectId, modelId);
        return ResponseEntity.ok(metrics);
    }
    
    @GetMapping("/trends")
    public ResponseEntity<Map<String, Object>> getHistoricalTrends(
            @RequestParam Long projectId,
            @RequestParam(required = false) Long modelId,
            @RequestParam(defaultValue = "30") int days) {
        
        Map<String, Object> trends = dashboardService.getHistoricalTrends(projectId, modelId, days);
        return ResponseEntity.ok(trends);
    }
    
    @GetMapping("/alerts/active")
    public ResponseEntity<Map<String, Object>> getActiveAlerts(
            @RequestParam Long projectId,
            @RequestParam(required = false) Long modelId) {
        
        Map<String, Object> alerts = Map.of("alerts", dashboardService.getActiveAlerts(projectId, modelId));
        return ResponseEntity.ok(alerts);
    }
    
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getProjectModelStatus(
            @RequestParam Long projectId,
            @RequestParam(required = false) Long modelId) {
        
        Map<String, Object> status = dashboardService.getProjectModelStatus(projectId, modelId);
        return ResponseEntity.ok(status);
    }
}
```

### 3. Crear ViewModel para Dashboard

```java
package com.codeflowx.govern.viewmodels.compliance;

import com.codeflowx.govern.services.compliance.PmmDashboardService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Slf4j
public class PmmDashboardViewModel {
    
    @WireVariable
    private PmmDashboardService dashboardService;
    
    private Map<String, Object> realTimeMetrics;
    private Map<String, List<Map<String, Object>>> historicalTrends;
    private List<Map<String, Object>> activeAlerts;
    private Map<String, Object> projectStatus;
    
    private Long projectId;
    private Long modelId;
    private int daysRange = 30;
    
    @Init
    public void init(@ContextParam(ContextType.VIEW) org.zkoss.zk.ui.Component view) {
        loadDashboard();
    }
    
    @Command
    public void loadDashboard() {
        if (projectId != null) {
            realTimeMetrics = dashboardService.getRealTimeMetrics(projectId, modelId);
            historicalTrends = dashboardService.getHistoricalTrends(projectId, modelId, daysRange);
            activeAlerts = dashboardService.getActiveAlerts(projectId, modelId);
            projectStatus = dashboardService.getProjectModelStatus(projectId, modelId);
        }
    }
    
    @Command
    public void exportToCsv() {
        // Implementar exportación CSV
    }
    
    @Command
    public void exportToPdf() {
        // Implementar exportación PDF
    }
    
    @Command
    public void refresh() {
        loadDashboard();
    }
}
```

### 4. Crear Servicio de Exportación

```java
package com.codeflowx.govern.services.compliance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardExportService {
    
    /**
     * Exporta dashboard a CSV
     */
    public byte[] exportToCsv(Map<String, Object> dashboardData) {
        // Implementar exportación CSV
        return new byte[0]; // TODO
    }
    
    /**
     * Exporta dashboard a PDF
     */
    public byte[] exportToPdf(Map<String, Object> dashboardData) {
        // Implementar exportación PDF con gráficos
        return new byte[0]; // TODO
    }
}
```

---

## VALIDACIONES

1. ✅ Dashboard implementado con métricas en tiempo real
2. ✅ Tendencias históricas visualizadas
3. ✅ Alertas activas mostradas
4. ✅ Estado de proyectos/modelos visible
5. ✅ Exportación CSV/PDF funcional
6. ✅ Filtros por proyecto, modelo, fecha implementados

---

## NOTAS

- Usar librerías de gráficos (Chart.js, D3.js, etc.)
- Dashboard debe ser responsive
- Exportación debe incluir gráficos y tablas
- Actualización automática cada X minutos (configurable)

---

**Estado:** ✅ COMPLETADO

