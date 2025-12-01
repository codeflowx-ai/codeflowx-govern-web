# MetricThresholdService

**Ubicación:** `com.codeflowx.govern.business.governance.MetricThresholdService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Gestiona umbrales configurables de métricas de governance para activar alertas cuando los valores exceden límites.

---

## 🎯 Responsabilidades

- Configurar umbrales (MIN, MAX, WARNING, CRITICAL)
- Consultar umbrales configurados
- Evaluar si valores actuales exceden umbrales
- Listar métricas con umbrales configurados

---

## 📚 API Pública

### `configureThreshold(Long metricId, String thresholdType, BigDecimal thresholdValue)`

Configura umbral para una métrica.

**Parámetros:**
- `metricId`: ID de la métrica
- `thresholdType`: "MIN", "MAX", "WARNING", "CRITICAL"
- `thresholdValue`: Valor del umbral (puede ser null para desactivar)

---

### `getThresholds(Long metricId)`

Obtiene todos los umbrales configurados.

**Retorna:** `Map<String, BigDecimal>` con MIN, MAX, WARNING, CRITICAL

---

### `evaluateThresholds(Long metricId, BigDecimal currentValue)`

Evalúa si un valor excede umbrales.

**Retorna:** `Map<String, String>` con estado de cada umbral:
- "OK", "WARNING", "CRITICAL", "NOT_CONFIGURED"

**Ejemplo:**
```java
Map<String, String> results = thresholdService.evaluateThresholds(metricId, currentValue);
if ("CRITICAL".equals(results.get("WARNING"))) {
    // Activar alerta crítica
}
```

---

## 📖 Referencias

- **Prompt:** INC-010
- **ViewModels:** AnalyticsMetricViewModel, AnalyticsOverviewViewModel, MonitoringDashboardViewModel

---

**Última actualización:** 25 de noviembre de 2025
