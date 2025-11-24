# Alert Response v1 – Guía Técnica

## Artefactos
- **BPMN**: `processes/metrics/alert-response-v1.bpmn`
- **Delegates**:
  - `ClassifyAlertDelegate`
  - `LogLowAlertDelegate`
  - `NotifyHighAlertDelegate`
  - `AutoEscalateCriticalDelegate`
  - `UpdateAlertStatusDelegate`
- **Reglas**: `rules/alert/alert-classification.drl`
- **Fact**: `AlertClassificationFact`.

## Variables
- Entrada: `alertId`, `alertType`, `severity`, `source`.
- Salida:
  - `classification`
  - `ticketId`
  - `escalationNotified`
  - `alertStatus`

## Integraciones
- **AlertCenter DB**: almacenar/actualizar estado.
- **ITSM / Ticketing**: creación de tickets (ServiceNow/Jira).
- **Incident BPMN**: `AutoEscalateCriticalDelegate` dispara mensaje/signal a `incident-reporting-process`.

## Consideraciones
- Reglas Drools determinan `classification` en base a tipo, criticidad y contexto.
- `UpdateAlertStatusDelegate` marca `CLOSED`, `IN_PROGRESS` o `ESCALATED`.

## Testing
- Unit tests para reglas (alertas por tipo).
- Flowable test que valide rutas para cada severidad.

