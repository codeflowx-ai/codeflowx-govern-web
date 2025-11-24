# Internal Conformity Assessment v1 – Guía Funcional

## Objetivo
Permitir a los equipos internos ejecutar la autoevaluación completa del Art. 43/Annex VI sin intervención de un notified body, asegurando que el sistema AI cumple todos los controles (Art. 9–15).

## Roles
- **Internal Compliance**: coordina el assessment.
- **Engineering Leads**: aportan evidencias por cada artículo.
- **Governance Board**: decide aprobación y emite declaración.

## Flujo
1. **Start** → `startConformityAssessment`.
2. **verifyRiskManagement** (Art. 9) → define `art9Compliant`.
3. **verifyDataGovernance** (Art. 10).
4. **verifyDocumentation** (Art. 11).
5. **verifyRecordKeeping** (Art. 12).
6. **verifyTransparency** (Art. 13).
7. **verifyHumanOversight** (Art. 14).
8. **verifyAccuracy** (Art. 15).
9. **calculateComplianceScore** (Business Rule Task) → `overallScore`.
10. **Gateway**:
    - `>=90%` → `generateDeclaration`.
    - `<90%` → `humanReview`.
11. **humanReview**: tarea para compliance board (puede reintentar o cancelar).
12. **generateDeclaration** + `issueDeclaration`.
13. **End** → `assessmentCompleted`.

## Variables
- Por artículo: `art9Compliant` … `art15Compliant`.
- `overallScore`, `compliancePassed`, `declarationId`.

## SLA
- Fase de evidencias: 10 días.
- Revisión humana: 3 días.

## Notas funcionales
- Si se rechaza, se debe documentar motivo y plan de remediación.
- Los resultados alimentan el proceso `conformity-assessment-process` cuando se requiere notified body.

