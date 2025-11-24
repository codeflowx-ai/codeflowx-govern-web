# EU Database Registration Process – Guía Funcional

## Objetivo
Gestionar el registro de sistemas AI en la Base de Datos de la UE (Art. 49 y Anexo VIII), incluyendo validaciones de datos y envío a la API oficial/mocks.

## Roles
- **Compliance Officer**: inicia registro y valida formularios.
- **Legal**: revisa información de secciones sensibles (ej. Section C).
- **Technical Writer**: carga documentación de soporte.

## Flujo
1. **Start Event** – se dispara cuando:
   - `Project.isHighRisk && conformityAssessed == true`, o
   - Evaluación concluye “NOT high-risk” (Art. 6.3).
2. **Determine Registration Type** (`registrationType = SECTION_A/B/C/MULTIPLE`).
3. **Gateway Type?**
   - Deriva a user task que se ajusta a los campos de la sección correspondiente:
     - Section A (high-risk).
     - Section B (evaluación no-alto riesgo).
     - Section C (deployer autoridad pública).
4. **Validate Registration Data** (service).
5. **Gateway Valid?**
   - NO → User Task `Fix Validation Errors`.
   - YES → `Generate Registration Package` (JSON + PDF + anexos).
6. **User Task `Review Registration Package`**.
7. **Gateway Submit?**
   - YES → `Submit to EU Database API`.
   - NO → End “Registration Cancelled”.
8. **Gateway Submission Success?**
   - YES → `Update Project`.
   - NO → `Log Error & Retry` → User Task `Manual Resolution`.

## Variables
- `registrationType`, `validationErrors`, `registrationPackageUrl`.
- `submissionSuccess`, `euRegistrationId`, `submissionDate`.
- `retryCount`, `manualResolutionNotes`.

## SLA
- Preparación/validación datos: 3 días.
- Revisión legal (si Section C): +2 días.
- Retries API: 3 intentos automáticos antes de intervención manual.

## Evidencias
- Registro en `Project.euRegistrationId`, estado `REGISTERED`.
- Paquete generado (JSON + PDF) almacenado en repositorio evidencias.

