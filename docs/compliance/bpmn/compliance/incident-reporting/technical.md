# Incident Reporting & Resolution – Guía Técnica

## Artefactos
- **BPMN**: `processes/compliance/incident-reporting-process.bpmn20.xml`
- **Delegates** (paquete `workflow.delegates.incident`):
  - `ClassifyIncidentSeverityDelegate`
  - `NotifyMarketSurveillanceAuthorityDelegate`
  - `NotifyAffectedUsersDelegate`
  - `ExecuteRCADelegate`
  - `CreateCorrectiveActionTasksDelegate`
  - `CloseIncidentDelegate`
  - `EscalateIncidentDelegate`
- **ViewModels**: `DocumentIncidentDetails`, `RootCauseAnalysis`, `DefineCorrectiveActions`, `VerifyIncidentResolution`.
- **Entidades**: `IncidentReport`, `CorrectiveAction`.

## Variables
- Entrada: `incidentId`, `source`, `detectedAt`, `initialSeverity`.
- Salida/intermedias:
  - `severity`, `isSerious`, `affectedUsersCount`.
  - `authorityNotificationId`, `authorityNotificationTimestamp`.
  - `rcaReportUrl`.
  - `correctiveActions` (lista de DTOs con responsable/fecha).
  - `resolutionStatus`, `escalationCount`.

## Integraciones
- **Authority API**: `NotifyMarketSurveillanceAuthorityDelegate` envía payload Art. 73 (HTTP POST).
- **NotificationService**: notificar usuarios si hay impacto GDPR.
- **RCA microservice**: `ExecuteRCADelegate` llama micro Python con logs/metrics.
- **Task Service / Jira**: `CreateCorrectiveActionTasksDelegate`.

## Lógica delegates
- `ClassifyIncidentSeverityDelegate`: aplica heurística (usuarios afectados, impacto derechos fundamentales, data breach) y setea `severity` (`LOW/MEDIUM/HIGH/CRITICAL`) + `isSerious`.
- `ExecuteRCADelegate`: obtiene informe automático, guarda URL en storage.
- `CreateCorrectiveActionTasksDelegate`: crea sub-tareas multi-instancia.
- `CloseIncidentDelegate`: actualiza `IncidentReport.status = RESOLVED`, envía notificación final.
- `EscalateIncidentDelegate`: incrementa `escalationCount`, notifica management.

## SLA / Validaciones
- Gateway `Serious?` determina flujo de notificación inmediata (Art. 73).
- Timer `Follow-up` envía recordatorio cada 48h hasta que `correctiveActions[*].status == DONE`.

## Testing
- Escenarios:
  1. `isSerious=true` → verifica paralelismo (authority + users + documentation).
  2. Corrección exitosa → `CloseIncident`.
  3. Resolución fallida → `Escalate` y loop.
- Tests unitarios para cada delegate con mocks de servicios externos.

## Pendientes
- Implementar cliente oficial EU Market Surveillance API (actualmente stub).
- Registrar todos los eventos en `ImmutableLog`.

