# ISO 42001 Management Review – Guía Funcional

## Objetivo
Cumplir con la cláusula 9.3 de ISO 42001 realizando revisiones trimestrales de resultados, decisiones y acciones sobre el Sistema de Gestión de IA.

## Roles
- **Review Coordinator**: agenda la reunión y recopila información.
- **Top Management**: lidera la revisión, define decisiones.
- **Action Owners**: ejecutan las acciones posteriores.

## Flujo
1. **Timer** (cada 3 meses).
2. **Prepare Performance Data**: calcula KPIs del periodo anterior.
3. **Generate Management Review Report**: arma informe consolidado (PDF/URL).
4. **User Task – Schedule Meeting**: define fecha, asistentes.
5. **User Task – Conduct Management Review**: se documentan decisiones y acciones.
6. **Record Management Decisions**: persiste decisiones en el sistema de rendimiento.
7. **Parallel**:
   - User Task `Assign Action Items`.
   - Service Task `Notify Stakeholders`.
8. **Join** → `Archive Review Record`.
9. **End**.

## Variables
- `period`, `performanceData`, `reportUrl`.
- `managementDecisions`, `actionItems`.
- `meetingDate`, `attendees`.

## SLA
- Preparación datos: 5 días antes de la reunión.
- Acciones asignadas: dentro de 2 días tras la reunión.
- Archivo y reportes: 24h posteriores.

## Evidencias
- Informe PDF enlazado.
- Registro en `ApsAimsPerformance`.
- Listado de acciones con responsables y fechas objetivo.


