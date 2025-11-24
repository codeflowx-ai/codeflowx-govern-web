# Internal Conformity Assessment v1 – Guía Técnica

## Artefactos
- **BPMN**: `processes/compliance/internal-conformity-assessment-v1.bpmn`
- **Delegates**: `verifyRiskManagement`, `verifyDataGovernance`, `verifyDocumentation`, `verifyRecordKeeping`, `verifyTransparency`, `verifyHumanOversight`, `verifyAccuracy`, `generateDeclaration`, `issueDeclaration`.
- **Reglas**: `internal-conformity-score.drl` (ruleflow group `internal-conformity`).

## Variables técnicas
- Entrada: `assessmentId`, `entityType`, `entityId`.
- Flags: `artXCompliant (Boolean)`, `artXScore (BigDecimal)` si aplica.
- Salidas: `overallScore`, `compliancePassed`, `declarationId`, `declarationUrl`.

## Lógica
- Cada delegate invoca el servicio correspondiente del dominio `nocode.service`:
  - `VerifyRiskManagementDelegate` -> `RiskManagementService`.
  - `VerifyDataGovernanceDelegate` -> `DataGovernanceService` (usa `leka-bias-detection`).
  - `VerifyDocumentationDelegate` -> `TechnicalDocumentationService`.
  - etc.
- `generateDeclaration` construye PDF y sube a storage; `issueDeclaration` almacena referencia en `ComplianceAssessment`.

## Drools
- Fact: `ConformityAssessmentFact` (ver `drools/facts`).
- Reglas:
  - `PassWithHighScore`: `overallScore >= 0.9`.
  - `ConditionalPass`: `overallScore 0.75-0.89` → `compliancePassed=false`.
  - `Fail`: `overallScore < 0.75`.

## Integraciones
- `ImmutableLog`: log por artículo.
- `NotificationService`: avisos al board cuando se requiere `humanReview`.

## Testing
- Escenarios unitarios para cada delegate (mock services).
- Test BPMN Flowable que asegure rutas PASS/FAIL.

## Pendientes
- Sincronizar esta autoevaluación con proceso `conformity-assessment-process`.
- Añadir métricas (time per step) a observabilidad.

