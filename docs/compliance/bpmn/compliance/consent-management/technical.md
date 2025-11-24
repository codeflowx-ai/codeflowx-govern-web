# Consent Management v1 – Guía Técnica

## Artefactos
- **BPMN**: `processes/compliance/consent-management-v1.bpmn`
- **Delegates**:
  - `RecordConsentDelegate`
  - `ValidateConsentDelegate`
  - `StoreConsentDelegate`
  - `RejectConsentDelegate`
  - `NotifyDataSubjectDelegate`
  - `ExpireConsentDelegate`
  - `WithdrawConsentDelegate` (si se implementa mensaje externo)
- **Entidades**: `ConsentRecord` (`CONCONSENTRECORDS`), con campos `CONSSTATUS`, `CONSLEGALBASIS`, timestamps etc.

## Variables
- `consentId: Long`
- `dataSubjectId: Long`
- `legalBasis: String`
- `consentStatus: String`
- `consentExpiresAt`
- `contactChannel`

## Flujo técnico
1. `RecordConsentDelegate`: crea registro preliminar (estado `PENDING`).
2. `ValidateConsentDelegate`: aplica reglas (edad mínima, guardian, scopes).
3. `StoreConsentDelegate`: persiste y genera hash `ImmutableLog`.
4. `NotifyDataSubjectDelegate`: envía email/SMS (usar `NotificationService`).
5. `Timer` → `ExpireConsentDelegate`: cambia estado a `EXPIRED`, registra log.

## Integraciones
- **NotificationService** (email/SMS).  
- **CRM / Portal** para solicitudes (REST endpoint `POST /api/consents` que dispara BPMN).  
- **ImmutableLogService** para trazabilidad.

## Validaciones
- Menores de 16 → requiere `guardianConsent`.
- `legalBasis` debe pertenecer a enumeración (`CONSENT`, `LEGIT_INTEREST`, etc.).
- Logs enmascaran PII (hash + metadata).

## Testing
- Unit tests sobre `ConsentManagementService`.
- Flowable test que simule: aprobación, rechazo, expiración, revocación.

## Pendientes
- API REST en micro `workflow.engine` para iniciar proceso.
- Endpoint para revocaciones (mensaje BPMN).

