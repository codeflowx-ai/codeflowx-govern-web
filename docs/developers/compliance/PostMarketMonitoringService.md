# PostMarketMonitoringService

**Ubicación:** `com.codeflowx.govern.business.compliance.PostMarketMonitoringService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Gestiona la vigilancia post-comercialización (PMM) de sistemas de IA de alto riesgo según Art. 72 del EU AI Act.

---

## 🎯 Responsabilidades

- Crear planes de monitoreo con frecuencias configurables
- Ejecutar monitoreos programados
- Verificar incidentes y métricas de performance
- Calcular próximas fechas de monitoreo

---

## 📚 API Pública

### `createMonitoringPlan(Long projectId, String monitoringFrequency)`

Crea plan de PMM para un proyecto.

**Parámetros:**
- `projectId`: ID del proyecto
- `monitoringFrequency`: "DAILY", "WEEKLY", "MONTHLY"

**Retorna:** `PostMarketMonitoring`

**Ejemplo:**
```java
PostMarketMonitoring pmm = pmmService.createMonitoringPlan(projectId, "MONTHLY");
```

---

### `executeMonitoring(Long pmmId)`

Ejecuta monitoreo post-mercado.

**Retorna:** `Map<String, Object>` con:
- `incidentCount`: Número de incidentes detectados
- `performanceMetrics`: Métricas de performance
- `monitoringDate`: Fecha del monitoreo
- `nextMonitoringDate`: Próxima fecha programada

**Ejemplo:**
```java
Map<String, Object> results = pmmService.executeMonitoring(pmmId);
Long incidents = (Long) results.get("incidentCount");
```

---

### `getActiveMonitoringPlans()`

Obtiene planes de PMM activos ordenados por próxima fecha.

**Retorna:** `List<PostMarketMonitoring>`

---

## 📖 Referencias

- **Art. 72 EU AI Act:** Post-Market Monitoring
- **Prompts:** INC-010-004, INC-010-001, INC-010-002, INC-010-003, INC-010-005

---

**Última actualización:** 25 de noviembre de 2025
