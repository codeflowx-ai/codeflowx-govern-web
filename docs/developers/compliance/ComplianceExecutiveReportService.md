# ComplianceExecutiveReportService

**Ubicación:** `com.codeflowx.govern.business.compliance.ComplianceExecutiveReportService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Genera reportes ejecutivos consolidados que agregan información de múltiples fuentes de compliance para proporcionar una visión integral del estado de compliance de la organización.

---

## 🎯 Responsabilidades

- Generar reportes ejecutivos (FULL, EXECUTIVE, TECHNICAL, AUDIT)
- Consolidar métricas de múltiples fuentes
- Generar conclusiones y recomendaciones
- Opcionalmente generar PDF mediante microservicio Python

---

## 📚 API Pública

### `generateExecutiveReport(String reportType, Timestamp startDate, Timestamp endDate)`

Genera reporte ejecutivo consolidado.

**Parámetros:**
- `reportType`: "FULL", "EXECUTIVE", "TECHNICAL", "AUDIT"
- `startDate`, `endDate`: Período del reporte (pueden ser null)

**Retorna:** `Map<String, Object>` con:
- `metrics`: Métricas consolidadas
- `executiveSummary`: Resumen ejecutivo
- `assessments`, `fria`, `euRegistrations`: Resúmenes por categoría
- `conclusions`, `recommendations`: Listas de conclusiones y recomendaciones
- `pdfGenerated`: Boolean si se generó PDF
- `pdfSize`: Tamaño del PDF en bytes

**Configuración:**
- `compliance.report.service.url`: URL del microservicio de generación de PDF (opcional)

**Ejemplo:**
```java
Timestamp startDate = Timestamp.valueOf("2025-01-01 00:00:00");
Timestamp endDate = new Timestamp(System.currentTimeMillis());
Map<String, Object> report = reportService.generateExecutiveReport("EXECUTIVE", startDate, endDate);
```

---

## 📖 Referencias

- **Prompt:** INC-024
- **ViewModels:** ComplianceReportViewModel, AnalyticsReportViewModel, GovernanceDashboardViewModel

---

**Última actualización:** 25 de noviembre de 2025
