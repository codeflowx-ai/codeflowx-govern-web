# ISO 38507 Board Decision Workflow – Guía Funcional

## Objetivo
Dar cumplimiento a los requisitos de gobernanza de ISO 38507 para decisiones estratégicas relacionadas con IA (evaluación, aprobación y seguimiento por el consejo).

## Roles
- **Proposal Owner**: presenta iniciativa (inversión, política, riesgo).
- **Board Secretary**: coordina agenda/documentación.
- **Board Members**: revisan y aprueban/rechazan.
- **Implementation Owner**: ejecuta decisión aprobada.

## Flujo
1. **Start** – propuesta ingresada (`proposalId`, `type`, `summary`).
2. **User Task – Prepare Board Paper**: reúne análisis estratégico, riesgos, KPIs.
3. **Service Task – Calculate Strategic Alignment Score**.
4. **User Task – Board Review (Evaluate)**: miembros revisan y piden info.
5. **Gateway – Decision?**
   - `REQUEST_MORE_INFO` → loop a Prepare Paper.
   - `REJECT` → notifica y finaliza.
   - `APPROVE` → continúa.
6. **User Task – Board Decides (Direct)**: registro formal de votación.
7. **Service Task – Record Board Decision**: persiste en tablas `BDCBOARDDECISION` / `BRDBOARDREPORT`.
8. **User Task – Implement Decision**: asigna responsables y plan de acción.
9. **Service Task – Monitor Implementation**: seguimiento (KPIs ROI, riesgos).
10. **End**.

## Variables
- `proposalId`, `proposalType`, `strategicAlignmentScore`.
- `boardDecision` (`APPROVE`, `REJECT`, `REQUEST_MORE_INFO`).
- `actionPlan`, `implementationStatus`.

## SLA
- Preparación Board Paper: ≤ 10 días.
- Respuesta a “request more info”: ≤ 5 días.
- Seguimiento post decisión: reportes mensuales.

## Evidencias
- Board paper y actas.
- Registro en tablas `BDCBOARDDECISION`, `BRDBOARDREPORT`.
- Logs en `ImmutableLog`.

