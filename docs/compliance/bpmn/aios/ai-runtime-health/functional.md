# AI Runtime Health – Guía Funcional

## Objetivo
Supervisar de forma continua la salud operativa de los runtimes AI‑OS, detectar degradaciones y disparar acciones (tickets o incidentes) antes de que impacten a usuarios finales.

## Roles
- **Site Reliability / MLOps**: atiende tickets `YELLOW`.
- **Incident Manager**: gestiona escalaciones `RED`.
- **Compliance**: monitorea justificaciones y dashboards.

## Flujo funcional
1. **Disparador**: Timer cada 15 min o evento de alerta inyecta `runtimeId`.
2. **Colecta de telemetría**: se toma un snapshot (latencia, error rate, drift, compliance).
3. **Evaluación KPI**: se calcula un “composite score” con peso a latencia/errores/compliance.
4. **Acciones paralelas**:
   - Determinar estado (`GREEN`, `YELLOW`, `RED`).
   - Actualizar dashboard de salud.
5. **Decisión final**:
   - `GREEN`: solo log.
   - `YELLOW`: se crea ticket de warning para SRE.
   - `RED`: se dispara el `runtimeIncidentTrigger` que encadena el BPMN de incidentes.

## Entradas clave
- `runtimeId`, `latencyMs`, `errorRate`, `driftIndicator`, `complianceScore`.

## Salidas clave
- `healthState`, `healthJustification`.
- `runtimeWarningTicketId` (si YELLOW).
- `runtimeIncidentTriggered` (si RED).

## SLA / Notificaciones
- Tickets `YELLOW`: respuesta < 1h.
- Incidentes `RED`: se activa cadena EU AI Act (Art. 72) en < 30 min.

