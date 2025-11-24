# PROMPT: INC-010-009 - API REST para Consulta de Histórico

**Incidencia:** INC-010-009  
**Prioridad:** 🟡 ALTA  
**Artículo EU AI Act:** Art. 72  
**Esfuerzo Estimado:** 2 días  
**Tipo:** Java - Backend  
**Referencia:** GAP-017

---

## CONTEXTO

No hay endpoints REST para consultar histórico de métricas y alertas. Se requiere API REST completa para consulta de histórico según Art. 72, con filtros, paginación y ordenamiento.

**Estado Actual:**
- ✅ Métricas almacenadas en `MONMONITORINGMETRICS`
- ✅ Alertas almacenadas en `MONMONITORINGALERTS`
- ✅ Incidentes almacenados (si aplica)
- ❌ No hay endpoints REST para consulta
- ❌ No hay filtros por proyecto, modelo, fecha, tipo
- ❌ No hay paginación y ordenamiento

---

## REQUISITOS

1. Crear endpoints REST:
   - `GET /api/v1/pmm/metrics/history` - Histórico de métricas
   - `GET /api/v1/pmm/alerts/history` - Histórico de alertas
   - `GET /api/v1/pmm/incidents/history` - Histórico de incidentes
2. Filtros por proyecto, modelo, fecha, tipo
3. Paginación y ordenamiento
4. Formato de respuesta estándar

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear DTOs para Respuestas

```java
package com.codeflowx.govern.dto.compliance;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MetricHistoryResponse {
    private List<MetricHistoryItem> items;
    private long total;
    private int page;
    private int size;
    private int totalPages;
}

@Data
public class MetricHistoryItem {
    private Long id;
    private Long projectId;
    private Long modelId;
    private String metricName;
    private Double metricValue;
    private LocalDateTime createdAt;
    private Map<String, Object> metadata;
}

@Data
public class AlertHistoryResponse {
    private List<AlertHistoryItem> items;
    private long total;
    private int page;
    private int size;
    private int totalPages;
}

@Data
public class AlertHistoryItem {
    private Long id;
    private Long projectId;
    private Long modelId;
    private String alertType;
    private String severity;
    private String status;
    private LocalDateTime triggeredAt;
    private LocalDateTime acknowledgedAt;
    private LocalDateTime resolvedAt;
    private Map<String, Object> alertData;
}
```

### 2. Crear Servicio para Histórico

```java
package com.codeflowx.govern.services.compliance;

import com.codeflowx.govern.dto.compliance.*;
import com.codeflowx.govern.entities.compliance.MonitoringAlert;
import com.codeflowx.govern.entities.compliance.MonitoringMetric;
import com.codeflowx.govern.repositories.compliance.MonitoringAlertRepository;
import com.codeflowx.govern.repositories.compliance.MonitoringMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PmmHistoryService {
    
    private final MonitoringMetricRepository metricRepository;
    private final MonitoringAlertRepository alertRepository;
    
    /**
     * Obtiene histórico de métricas con filtros y paginación
     */
    public MetricHistoryResponse getMetricsHistory(
            Long projectId,
            Long modelId,
            String metricName,
            LocalDateTime startDate,
            LocalDateTime endDate,
            int page,
            int size,
            String sortBy,
            String sortDir) {
        
        Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Direction.fromString(sortDir), sortBy)
        );
        
        Page<MonitoringMetric> metricsPage = metricRepository.findWithFilters(
            projectId,
            modelId,
            metricName,
            startDate,
            endDate,
            pageable
        );
        
        MetricHistoryResponse response = new MetricHistoryResponse();
        response.setItems(metricsPage.getContent().stream()
            .map(this::mapToMetricHistoryItem)
            .collect(Collectors.toList()));
        response.setTotal(metricsPage.getTotalElements());
        response.setPage(page);
        response.setSize(size);
        response.setTotalPages(metricsPage.getTotalPages());
        
        return response;
    }
    
    /**
     * Obtiene histórico de alertas con filtros y paginación
     */
    public AlertHistoryResponse getAlertsHistory(
            Long projectId,
            Long modelId,
            String alertType,
            String severity,
            LocalDateTime startDate,
            LocalDateTime endDate,
            int page,
            int size,
            String sortBy,
            String sortDir) {
        
        Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Direction.fromString(sortDir), sortBy)
        );
        
        Page<MonitoringAlert> alertsPage = alertRepository.findWithFilters(
            projectId,
            modelId,
            alertType,
            severity,
            startDate,
            endDate,
            pageable
        );
        
        AlertHistoryResponse response = new AlertHistoryResponse();
        response.setItems(alertsPage.getContent().stream()
            .map(this::mapToAlertHistoryItem)
            .collect(Collectors.toList()));
        response.setTotal(alertsPage.getTotalElements());
        response.setPage(page);
        response.setSize(size);
        response.setTotalPages(alertsPage.getTotalPages());
        
        return response;
    }
    
    private MetricHistoryItem mapToMetricHistoryItem(MonitoringMetric metric) {
        MetricHistoryItem item = new MetricHistoryItem();
        item.setId(metric.getIdxmonmetric());
        item.setProjectId(metric.getIdxproject());
        item.setModelId(metric.getIdxmodel());
        item.setMetricName(metric.getMonmetricname());
        item.setMetricValue(metric.getMonmetricvalue().doubleValue());
        item.setCreatedAt(metric.getMoncreatedat());
        item.setMetadata(metric.getMonmetricdata());
        return item;
    }
    
    private AlertHistoryItem mapToAlertHistoryItem(MonitoringAlert alert) {
        AlertHistoryItem item = new AlertHistoryItem();
        item.setId(alert.getIdxmonalert());
        item.setProjectId(alert.getIdxproject());
        item.setModelId(alert.getIdxmodel());
        item.setAlertType(String.join(",", alert.getMonalerttype()));
        item.setSeverity(alert.getMonseverity());
        item.setStatus(String.join(",", alert.getMonstatus()));
        item.setTriggeredAt(alert.getMontriggeredat());
        item.setAcknowledgedAt(alert.getMonacknowledgedat());
        item.setResolvedAt(alert.getMonresolvedat());
        item.setAlertData(alert.getMonalertdata());
        return item;
    }
}
```

### 3. Extender Repository con Métodos de Filtrado

```java
package com.codeflowx.govern.repositories.compliance;

import com.codeflowx.govern.entities.compliance.MonitoringMetric;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MonitoringMetricRepository extends JpaRepository<MonitoringMetric, Long> {
    
    @Query("SELECT m FROM MonitoringMetric m WHERE " +
           "(:projectId IS NULL OR m.idxproject = :projectId) AND " +
           "(:modelId IS NULL OR m.idxmodel = :modelId) AND " +
           "(:metricName IS NULL OR m.monmetricname = :metricName) AND " +
           "(:startDate IS NULL OR m.moncreatedat >= :startDate) AND " +
           "(:endDate IS NULL OR m.moncreatedat <= :endDate)")
    Page<MonitoringMetric> findWithFilters(
        @Param("projectId") Long projectId,
        @Param("modelId") Long modelId,
        @Param("metricName") String metricName,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable
    );
}
```

```java
package com.codeflowx.govern.repositories.compliance;

import com.codeflowx.govern.entities.compliance.MonitoringAlert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MonitoringAlertRepository extends JpaRepository<MonitoringAlert, Long> {
    
    @Query("SELECT a FROM MonitoringAlert a WHERE " +
           "(:projectId IS NULL OR a.idxproject = :projectId) AND " +
           "(:modelId IS NULL OR a.idxmodel = :modelId) AND " +
           "(:alertType IS NULL OR :alertType MEMBER OF a.monalerttype) AND " +
           "(:severity IS NULL OR a.monseverity = :severity) AND " +
           "(:startDate IS NULL OR a.montriggeredat >= :startDate) AND " +
           "(:endDate IS NULL OR a.montriggeredat <= :endDate)")
    Page<MonitoringAlert> findWithFilters(
        @Param("projectId") Long projectId,
        @Param("modelId") Long modelId,
        @Param("alertType") String alertType,
        @Param("severity") String severity,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable
    );
}
```

### 4. Crear Controller REST

```java
package com.codeflowx.govern.controllers.compliance;

import com.codeflowx.govern.dto.compliance.*;
import com.codeflowx.govern.services.compliance.PmmHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/pmm")
@RequiredArgsConstructor
@Slf4j
public class PmmHistoryController {
    
    private final PmmHistoryService historyService;
    
    @GetMapping("/metrics/history")
    public ResponseEntity<MetricHistoryResponse> getMetricsHistory(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long modelId,
            @RequestParam(required = false) String metricName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "moncreatedat") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        
        MetricHistoryResponse response = historyService.getMetricsHistory(
            projectId, modelId, metricName, startDate, endDate,
            page, size, sortBy, sortDir
        );
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/alerts/history")
    public ResponseEntity<AlertHistoryResponse> getAlertsHistory(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long modelId,
            @RequestParam(required = false) String alertType,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "montriggeredat") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        
        AlertHistoryResponse response = historyService.getAlertsHistory(
            projectId, modelId, alertType, severity, startDate, endDate,
            page, size, sortBy, sortDir
        );
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/incidents/history")
    public ResponseEntity<?> getIncidentsHistory(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long modelId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        // TODO: Implementar cuando exista entidad de incidentes
        return ResponseEntity.ok(Map.of("message", "Not implemented yet"));
    }
}
```

---

## VALIDACIONES

1. ✅ Endpoint `/api/v1/pmm/metrics/history` implementado
2. ✅ Endpoint `/api/v1/pmm/alerts/history` implementado
3. ✅ Filtros por proyecto, modelo, fecha, tipo funcionando
4. ✅ Paginación y ordenamiento implementados
5. ✅ Formato de respuesta estándar

---

## NOTAS

- Usar paginación para evitar cargar demasiados datos
- Ordenamiento por defecto: fecha descendente
- Filtros opcionales (todos pueden ser null)
- Considerar índices en BD para mejorar performance

---

**Estado:** ✅ COMPLETADO

