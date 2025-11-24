# Alert Response v1 – Guía Funcional

## Objetivo
Clasificar y gestionar alertas operativas generadas por el monitoreo AI‑OS, asegurando respuesta adecuada según severidad.

## Roles
- **SRE / Monitoring**: recibe y atiende alertas LOW/MEDIUM.
- **Incident Manager**: gestiona alertas HIGH/CRITICAL.
- **Compliance**: audita historial de alertas.

## Flujo
1. **Start** – evento con `alertId`, `alertType`, `severity`.
2. **Classify Alert** (Drools) → determina acción recomendada.
3. **Gateway** con rutas:
   - `LOW` → `LogLowAlert`.
   - `MEDIUM` → `NotifyHighAlert` (correo / chat).
   - `HIGH` → `NotifyHighAlert` + crear ticket.
   - `CRITICAL` → `AutoEscalateCritical` (dispara incident BPMN).
4. **Update Alert Status** → cierra/bloquea según resultado.
5. **End**.

## Variables
- `alertId`, `alertType`, `source`.
- `severity`, `classification`.
- `ticketId`, `escalation`.

## SLA
- MEDIUM: respuesta < 2h.
- HIGH: respuesta < 30 min.
- CRITICAL: activar `incident-reporting` inmediatamente.

## Evidencias
- Log en `AlertCenter`.
- Tickets generados en sistema ITSM.

