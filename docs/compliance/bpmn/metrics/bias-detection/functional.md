# Bias Detection v1 – Guía Funcional

## Objetivo
Ejecutar evaluaciones periódicas de sesgo en modelos y datasets, detectando disparidades y generando acciones de mitigación (Art. 10 y 15 EU AI Act).

## Roles
- **Model Owner**: recibe resultados y define acciones.
- **Compliance/Data Ethics**: revisa alertas y aprueba mitigaciones.
- **MLOps**: programa y monitorea ejecuciones.

## Flujo
1. **Start** (timer diario o evento).
2. **Prepare Demographic Data**: compila atributos protegidos y cohortes.
3. **Execute Bias Detection**: corre métricas (disparate impact, equal opportunity, etc.) usando micro `leka-bias-detection`.
4. **Business Rule Task**: clasifica resultado (`AUTO_NOTIFY`, `ESCALATE`, `IGNORE`) según umbrales.
5. **Gateway**:
   - `AUTO_NOTIFY` → notifica owner (sesgo leve).
   - `ESCALATE` → genera tarea/alerta crítica y potencial bloqueo.
   - `IGNORE` → sin acción (sin sesgo significativo).
6. **End**.

## Variables
- `modelId`, `datasetId`.
- `biasMetrics` (mapa de resultados).
- `biasDecision` (`NO_BIAS`, `WARNING`, `CRITICAL`).
- `biasJustification`, `ownerNotified`.

## SLA
- Model owner debe revisar alertas críticas en < 24h.
- Registro de acciones correctivas enlaza con `incident-response-rca` si procede.

## Evidencias
- Reporte generado por `leka-bias-detection`.
- Registro en `ImmutableLog` y dashboard de fairness.

