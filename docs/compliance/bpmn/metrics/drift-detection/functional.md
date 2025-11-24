# Drift Detection v1 – Guía Funcional

## Objetivo
Detectar desviaciones de datos o concepto en modelos desplegados, disparando alertas y acciones preventivas (Art. 15, 72 EU AI Act).

## Roles
- **MLOps**: configura y supervisa ejecuciones.
- **Model Owner**: revisa alertas y decide acciones (retraining, recalibración).
- **Compliance/Quality**: monitoriza indicadores de degradación.

## Flujo
1. **Start** (timer o evento).
2. **Load Baseline Metrics**: obtiene métricas históricas de referencia.
3. **Capture Current Metrics**: recoge métricas recientes (latencia, distribuciones).
4. **Analyze Drift**: calcula PSI, KS, KL divergence.
5. **Business Rule Task**: clasifica `driftLevel` (`LOW`, `MEDIUM`, `CRITICAL`).
6. **Gateway**:
   - `LOW` → log/archivo.
   - `MEDIUM` → notificación y creación de tarea.
   - `CRITICAL` → crea alerta y propone retraining.
7. **End**.

## Variables
- `modelId`, `baselineTimestamp`.
- `baselineMetrics`, `currentMetrics`.
- `driftLevel`, `driftIndicators`.
- `alertId`, `recommendedAction`.

## SLA
- Alerta crítica debe revisarse en < 12h.
- Si se recomienda retraining debe planificarse en < 5 días.

## Evidencias
- Reporte comparativo (PDF/JSON).
- Registro en dashboard de monitoreo.

