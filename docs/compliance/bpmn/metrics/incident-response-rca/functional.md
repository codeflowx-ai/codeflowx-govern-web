# Incident Response RCA v1 – Guía Funcional

## Objetivo
Estandarizar el análisis de causa raíz (Root Cause Analysis) para incidentes detectados en operación, generando recomendaciones y acciones preventivas.

## Roles
- **Incident Analyst**: ejecuta el RCA.
- **Tech Lead**: valida recomendaciones.
- **Compliance**: asegura trazabilidad y cierre.

## Flujo
1. **Start** – recibe `incidentId`.
2. **AI Impact Assessment** (service): evalúa impacto del incidente sobre KPIs, usuarios, riesgos.
3. **Generate Mitigation Recommendations** (service): produce lista de acciones.
4. **Gateway**:
   - `Mitigations Approved?` (user task si se requiere) → si no, ajustar/iterar.
   - Si sí → se integran con `incident-reporting-process` (acciones correctoras).
5. **End** – RCA completado y adjuntado al incidente.

## Variables
- `incidentId`, `impactReport`, `mitigationRecommendations`.
- `approvalStatus`, `rcaCompleted`.

## SLA
- RCA inicial: ≤ 48h desde detección.
- Recomendaciones deben incluir responsables y tiempos.

## Evidencias
- Informe RCA laeg para auditoría.
- Registro en `IncidentReport.rcaReportUrl`.

