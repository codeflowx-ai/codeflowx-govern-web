# Risk Assessment v1 – Guía Funcional

## Objetivo
Dar cumplimiento al Art. 9 (Risk Management System) e ISO 42001 6.1 mediante una evaluación periódica de riesgos de cada componente AI, con decisión HITL sobre la aceptación del riesgo residual.

## Roles
- **Risk Manager**: planifica evaluaciones.
- **Engineering Lead**: aporta inputs técnicos (métricas, mitigaciones).
- **Executive Sponsor**: acepta o rechaza riesgo residual.

## Flujo
1. **Start Event** (manual o scheduler).
2. **RiskAssessmentDelegate**: calcula `riskScore`, `riskCategory`, sugiere mitigaciones.
3. **ScheduleReviewDelegate**: programa reunión o deadline para aceptación.
4. **User Task “Accept Residual Risk”**:
   - Puede aceptar, rechazar o solicitar nuevas mitigaciones.
5. **End Event**:
   - Si se acepta → se registra `residualRiskAccepted=true`.
   - Si se rechaza → se dispara plan de mitigación (loop opcional).

## Variables
- `riskScore`, `riskCategory`.
- `recommendedMitigations`.
- `residualRiskAccepted`, `acceptedBy`, `acceptedAt`.

## SLA
- Evaluación técnica: 3 días.
- Aceptación residual: 5 días tras la evaluación.
- Reevaluación cada trimestre o ante incidentes.

## Evidencias
- Registro en `RiskRegister`.
- Entrada en `ImmutableLog` con la decisión final.

