# Dataset Quality v1 – Guía Funcional

## Objetivo
Garantizar que los datasets utilizados para entrenar o probar modelos AI cumplen los requisitos de calidad, integridad y gobernanza (Art. 10 EU AI Act).

## Roles
- **Data Steward**: coordina evaluación y recibe resultados.
- **ML Engineer**: corrige issues detectados.
- **Compliance**: audita informes de calidad.

## Flujo
1. **Start** – evaluación se dispara manualmente o por scheduler.
2. **Dataset Quality Check** (service):
   - Evalúa completitud, consistencia, duplicados, outliers, leakage.
3. **Gateway PASS / FAIL**:
   - `PASS` → marca dataset como listo y publica badge.
   - `FAIL` → crea issue/ticket y notifica al Data Steward.
4. **End**.

## Variables
- `datasetId`, `datasetVersion`.
- `qualityScore`, `issuesDetected`.
- `qualityStatus` (`PASS`, `FAIL`).
- `badgeUrl`, `issueTicketId`.

## SLA
- Datasets críticos deben reevaluarse al menos trimestralmente.
- Issues deben resolverse antes de usar dataset en entrenamiento/producción.

## Evidencias
- Reporte generado por `leka-bias-detection` (endpoints de data quality).
- Badge / certificado en catálogo de datasets.

