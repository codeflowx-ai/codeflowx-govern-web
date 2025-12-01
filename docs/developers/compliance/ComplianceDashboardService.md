# ComplianceDashboardService

**Ubicación:** `com.codeflowx.govern.business.compliance.ComplianceDashboardService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Proporciona métricas consolidadas de compliance agregando información de múltiples fuentes (assessments, FRIA, registros EU) para dashboards ejecutivos.

---

## 🎯 Responsabilidades

- Calcular métricas agregadas (totales, completados, scores promedio)
- Calcular tasas de finalización y aprobación
- Obtener métricas desglosadas por sector

---

## 📚 API Pública

### `getComplianceMetrics()`

Obtiene métricas consolidadas de compliance.

**Retorna:** `Map<String, Object>` con:
- `totalAssessments`, `completedAssessments`
- `totalFria`, `approvedFria`
- `totalEuRegistrations`, `submittedEuRegistrations`
- `averageComplianceScore`
- `completionRate`, `friaApprovalRate`, `euSubmissionRate`

**Ejemplo:**
```java
Map<String, Object> metrics = complianceDashboardService.getComplianceMetrics();
Long total = (Long) metrics.get("totalAssessments");
Double completionRate = (Double) metrics.get("completionRate");
```

---

### `getComplianceMetricsBySector()`

Obtiene métricas desglosadas por sector (placeholder - requiere implementación).

---

## 📖 Referencias

- **Prompt:** INC-017
- **ViewModels:** GovernanceDashboardViewModel, ComplianceAiActViewModel, SectorDashboardViewModel

---

**Última actualización:** 25 de noviembre de 2025
