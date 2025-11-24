# Consent Management v1 – Guía Funcional

## Objetivo
Gestionar el ciclo de vida del consentimiento (GDPR Art. 7 y 8) para usuarios/estudiantes, asegurando registros, validación, notificación y expiración periódica.

## Actores
- **Data Privacy Officer (DPO)**: supervisa el proceso.
- **Customer Support / Admissions**: inicia solicitudes o gestiona revocaciones.
- **Sistemas externos**: portales/webhooks que capturan consent.

## Flujo
1. **Start** → `recordConsentRequest` (registra solicitud con `dataSubjectId`, `context`, `channel`).
2. **validateConsent**: comprueba edad mínima, pares padre/tutor, scopes solicitados.
3. **Gateway** Valid?  
   - YES → `storeConsent` (persistencia) + `notifyDataSubject`.  
   - NO → `rejectRequest`.
4. **Timer Event (cada 30 días)** → `checkExpiration`:
   - Si expira → `expireConsent` + notificación.
5. **Revocaciones**: se disparan vía mensaje externo, reusan `withdrawConsent`.

## Variables
- Entrada: `consentId`, `dataSubjectId`, `legalBasis`, `channel`, `expiryDate`.
- Salida: `consentStatus` (`ACTIVE`, `REJECTED`, `WITHDRAWN`, `EXPIRED`), `consentRecordUrl`.

## SLA
- Validación automática inmediata.
- Notificación al interesado < 24h.
- Revalidación/renovación al menos cada 12 meses (política corporativa).

## Evidencias
- Registro en `ConsentRecord` (tabla `CONCONSENTRECORDS`).
- Hash en `ImmutableLog` con metadata del consentimiento.

