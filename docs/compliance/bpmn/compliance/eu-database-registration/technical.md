# EU Database Registration Process – Guía Técnica

## Artefactos
- **BPMN**: `processes/compliance/eu-database-registration-process.bpmn20.xml`
- **Delegates**:
  - `DetermineRegistrationTypeDelegate`
  - `ValidateRegistrationDataDelegate`
  - `GenerateRegistrationPackageDelegate`
  - `SubmitToEuDatabaseDelegate`
  - `UpdateProjectRegistrationDelegate`
  - `LogRegistrationErrorDelegate`
- **ViewModels**: `EuRegistrationFormViewModel`, `FixValidationErrors`, `ReviewRegistrationPackage`, `ManualResolution`.

## Variables
- `projectId`, `registrationType`, `registrationPayload`.
- `validationPassed`, `validationErrors`.
- `registrationPackageUrl`, `submissionSuccess`, `euRegistrationId`, `submissionDate`.
- `retryCount`, `manualResolutionNotes`.

## Integraciones
- **Project Service**: obtener datos del proyecto (sector, notified body, FRIA).
- **EU Database API** (mock por ahora) – `SubmitToEuDatabaseDelegate`.
- **Storage** (S3/MinIO) para paquete (ZIP con JSON + PDF + anexos).
- **NotificationService**: avisar a compliance/legal cuando se requiere `ManualResolution`.

## Consideraciones técnicas
- `DetermineRegistrationTypeDelegate`: aplica reglas Art. 49 (Section A: high risk, Section B: not-high-risk; Section C: deployer público).
- `ValidateRegistrationDataDelegate`: valida obligatoriedad y formatos (Bean Validation + listas Anexo VIII).
- `SubmitToEuDatabaseDelegate`: 
  - Retries automáticos (backoff).  
  - Marca `submissionSuccess` y `euRegistrationId` si la API responde 2xx.  
  - En falla → set `retryCount` y route a `LogRegistrationErrorDelegate`.
- `UpdateProjectRegistrationDelegate`: persiste `Project.euRegistrationStatus`.

## Testing
- Mock API para éxito, error temporal (retry) y error permanente (ManualResolution).
- Validar que `registrationPackageUrl` se genera y guarda en storage.

## Pendientes
- Implementar cliente oficial cuando la Comisión publique especificación.
- Asegurar cifrado/mascaramiento de campos sensibles antes de enviar al API.

