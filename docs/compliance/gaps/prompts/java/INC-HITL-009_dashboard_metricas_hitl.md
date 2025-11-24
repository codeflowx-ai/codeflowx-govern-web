# PROMPT: INC-HITL-009 - Dashboard de Métricas de HITL

**Incidencia:** INC-HITL-009  
**Prioridad:** 🟡 MEDIUM  
**Artículo EU AI Act:** Art. 14.4 (Transparency)  
**Esfuerzo Estimado:** 5-7 días  
**Tipo:** Java - Backend + Frontend  
**Estado:** ✅ COMPLETADO

---

## DESCRIPCIÓN

No existe dashboard o vista consolidada de métricas de HITL (tiempos de aprobación, tasas de aprobación/rechazo, distribución por tipo, etc.), lo que limita la visibilidad operativa. Se requiere crear dashboard en frontend con métricas clave.

---

## REQUISITOS

1. Crear dashboard en frontend con métricas clave de HITL
2. Implementar gráficos de tendencias (tiempo de aprobación, tasas)
3. Configurar alertas para métricas anómalas (ej: tiempo de aprobación > threshold)
4. Incluir comparativas por período, tipo, aprobador

---

## IMPLEMENTACIÓN REQUERIDA

### 1. DTO de Métricas

**Archivo:** `codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/hitl/HitlMetricsDto.java`

```java
package com.codeflowx.govern.nocode.dtos.hitl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HitlMetricsDto {
    private long totalApprovals;
    private long pendingApprovals;
    private long approvedCount;
    private long rejectedCount;
    private double averageApprovalTimeHours;
    private double medianApprovalTimeHours;
    private Map<String, Long> approvalsByType;
    private Map<String, Long> approvalsByStatus;
    private Map<String, Double> averageTimeByType;
    private Map<String, Long> approvalsByApprover;
}
```

### 2. Servicio de Métricas

**Archivo:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/hitl/HitlMetricsService.java`

```java
package com.codeflowx.govern.service.hitl;

import com.codeflowx.govern.nocode.dtos.hitl.HitlMetricsDto;
import com.codeflowx.govern.repository.agents.AgentApprovalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HitlMetricsService {
    
    @Autowired
    private AgentApprovalRepository agentApprovalRepository;
    
    @Transactional(readOnly = true)
    public HitlMetricsDto getMetrics(Timestamp startDate, Timestamp endDate) {
        HitlMetricsDto metrics = HitlMetricsDto.builder()
            .totalApprovals(agentApprovalRepository.countByDateRange(startDate, endDate))
            .pendingApprovals(agentApprovalRepository.countByStatus("PENDING"))
            .approvedCount(agentApprovalRepository.countByStatus("APPROVED"))
            .rejectedCount(agentApprovalRepository.countByStatus("REJECTED"))
            .averageApprovalTimeHours(calculateAverageApprovalTime(startDate, endDate))
            .approvalsByType(getApprovalsByType(startDate, endDate))
            .approvalsByStatus(getApprovalsByStatus(startDate, endDate))
            .averageTimeByType(getAverageTimeByType(startDate, endDate))
            .approvalsByApprover(getApprovalsByApprover(startDate, endDate))
            .build();
        
        return metrics;
    }
    
    private double calculateAverageApprovalTime(Timestamp start, Timestamp end) {
        // Query para calcular tiempo promedio de aprobación
        return agentApprovalRepository.calculateAverageApprovalTime(start, end);
    }
    
    private Map<String, Long> getApprovalsByType(Timestamp start, Timestamp end) {
        return agentApprovalRepository.countByTypeAndDateRange(start, end)
            .stream()
            .collect(Collectors.toMap(
                obj -> (String) obj[0],
                obj -> (Long) obj[1]
            ));
    }
    
    private Map<String, Long> getApprovalsByStatus(Timestamp start, Timestamp end) {
        return agentApprovalRepository.countByStatusAndDateRange(start, end)
            .stream()
            .collect(Collectors.toMap(
                obj -> (String) obj[0],
                obj -> (Long) obj[1]
            ));
    }
    
    private Map<String, Double> getAverageTimeByType(Timestamp start, Timestamp end) {
        return agentApprovalRepository.calculateAverageTimeByType(start, end)
            .stream()
            .collect(Collectors.toMap(
                obj -> (String) obj[0],
                obj -> ((Number) obj[1]).doubleValue()
            ));
    }
    
    private Map<String, Long> getApprovalsByApprover(Timestamp start, Timestamp end) {
        return agentApprovalRepository.countByApproverAndDateRange(start, end)
            .stream()
            .collect(Collectors.toMap(
                obj -> (String) obj[0],
                obj -> (Long) obj[1]
            ));
    }
}
```

### 3. Controller REST

**Archivo:** `codeflowx.govern.controllers/src/main/java/com/codeflowx/govern/controller/hitl/HitlMetricsController.java`

```java
package com.codeflowx.govern.controller.hitl;

import com.codeflowx.govern.nocode.dtos.hitl.HitlMetricsDto;
import com.codeflowx.govern.service.hitl.HitlMetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/governance/hitl/metrics")
public class HitlMetricsController {
    
    @Autowired
    private HitlMetricsService metricsService;
    
    @GetMapping
    public ResponseEntity<HitlMetricsDto> getMetrics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        Timestamp start = startDate != null ? Timestamp.valueOf(startDate) : null;
        Timestamp end = endDate != null ? Timestamp.valueOf(endDate) : null;
        
        HitlMetricsDto metrics = metricsService.getMetrics(start, end);
        return ResponseEntity.ok(metrics);
    }
}
```

### 4. Queries en Repository

**Archivo:** Modificar `AgentApprovalRepository.java`

```java
@Query("SELECT AVG(EXTRACT(EPOCH FROM (a.agtapprovedat - a.agtcreatedat)) / 3600.0) " +
       "FROM AgentApproval a WHERE a.agtapprovedat IS NOT NULL " +
       "AND (:startDate IS NULL OR a.agtcreatedat >= :startDate) " +
       "AND (:endDate IS NULL OR a.agtcreatedat <= :endDate)")
Double calculateAverageApprovalTime(@Param("startDate") Timestamp startDate, 
                                    @Param("endDate") Timestamp endDate);

@Query("SELECT a.agtapprovaltype, COUNT(a) " +
       "FROM AgentApproval a WHERE " +
       "(:startDate IS NULL OR a.agtcreatedat >= :startDate) " +
       "AND (:endDate IS NULL OR a.agtcreatedat <= :endDate) " +
       "GROUP BY a.agtapprovaltype")
List<Object[]> countByTypeAndDateRange(@Param("startDate") Timestamp startDate,
                                       @Param("endDate") Timestamp endDate);
```

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md#inc-hitl-009`
- **EU AI Act Art. 14.4:** Transparency

---

**Estado:** ✅ COMPLETADO  
**Esfuerzo Estimado:** 5-7 días  
**Responsable:** Frontend Team + Backend Team  
**Fecha Límite:** 2 meses

