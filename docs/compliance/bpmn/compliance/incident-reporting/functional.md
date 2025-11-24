# Incident Reporting & Resolution Process – Guía Funcional

## Objetivo
Cubrir la gestión completa de incidentes graves/malfuncionamientos (Art. 20, 62 y 73 EU AI Act), desde detección hasta cierre, incluyendo notificación a autoridades y usuarios afectados.

## Roles
- **Monitoring / SRE**: inicia el proceso automáticamente.
- **Compliance Officer**: coordina notificacione y RCA.
- **Tech Lead**: define y ejecuta acciones correctoras.
- **Market Surveillance Liaison**: envía reportes Art. 73.

## Flujo
1. **Start Event**: señal de monitoreo o reporte manual.
2. **Classify Incident Severity** (service task): calcula `severity`, `isSerious`.
3. **Gateway Serious Incident?**
   - YES → Parallel:
     - `Notify Market Surveillance Authority`.
     - `Notify Affected Users`.
     - User Task `Document Incident Details`.
   - NO → User Task `Categorize and Document`.
4. **User Task – Root Cause Analysis**.
5. **Service Task – Execute RCA** (micro Python).
6. **User Task – Define Corrective Actions**.
7. **Service Task – Create Corrective Action Tasks**.
8. **Parallel**:
   - Multi-instance `Execute Corrective Actions`.
   - Timer `Follow-up Reminder`.
9. **Join** → User Task `Verify Resolution`.
10. **Gateway Resolved?**
    - YES → `Close Incident`.
    - NO → `Escalate` (loop a Corrective Actions).

## Variables
- `incidentId`, `severity`, `isSerious`, `affectedUsersCount`.
- `rcaReportUrl`, `correctiveActions`.
- `resolutionStatus`, `authorityNotificationId`.

## SLA
- Notificación autoridad (serious): < 24h (Art. 73.1).
- RCA inicial: 48h.
- Corrective actions: según criticidad (definidas en la tarea).

## Entregables
- Informe para autoridad.
- RCA documentado + acciones con responsables/fechas.
- Registro en dashboard de incidentes.

