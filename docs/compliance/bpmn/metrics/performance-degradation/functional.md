# Performance Degradation v1 – Guía Funcional

## Objetivo
Detectar degradaciones significativas en el rendimiento de un modelo (accuracy, precision, tiempos) y decidir si se requiere retraining o intervención humana.

## Roles
- **MLOps**: ejecuta monitoreo y revisa alertas.
- **Model Owner**: valida métricas y planifica retraining.
- **Product Stakeholders**: reciben notificaciones de impacto.

## Flujo
1. **Start** (scheduler).
2. **Load Baseline Performance**.
3. **Check Current Metrics** (usa telemetría/monitoring).
4. **Compare Performance**: analiza variación, estadísticos (p-value).
5. **Business Rule Task**: clasifica `performanceStatus` (`DEGRADATION`, `OK`).
6. **Gateway**:
   - `DEGRADATION` → `Trigger Retraining` + notificación.
   - `OK` → archivar/fin.
7. **End**.

## Variables
- `modelId`, `environment`.
- `baselineMetrics`, `currentMetrics`.
- `pValue`, `performanceStatus`.
- `retrainingTriggered`.

## SLA
- Model owner responde a `DEGRADATION` en < 24h.
- Retraining debe iniciarse en ≤ 5 días si impacto alto.

## Evidencias
- Reporte comparativo.
- Registro en dashboard de rendimiento.

