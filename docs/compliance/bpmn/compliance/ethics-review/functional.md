# Ethics Review v1 – Guía Funcional

## Objetivo
Realizar revisiones éticas obligatorias (políticas internas e ISO 42001 4.2) para agentes/modelos/pipelines antes de moverlos a producción.

## Roles
- **Ethics Committee**: conduce la revisión, decide aprobación o rechazo.
- **AI Governance Analyst**: prepara evidencias, adjunta reportes.
- **Product Owner**: responde a observaciones.

## Flujo
1. **Start** → `AI Ethical Review` (service task) compila evidencias (score fairness, explicabilidad, transparencia).
2. **Save Ethics Evidence**: adjunta resultados (PDF, KPIs).
3. **User Task “Ethics Review”**:
   - Visualiza evidencias y llena formulario (decisión + comentarios).
4. **Gateway**:
   - `APPROVED` → `Log Approval` → End.
   - `REJECTED` → `RejectEthicsDelegate` genera plan de mitigación y notifica.

## Variables
- `ethicsScore`, `ethicsFindings`.
- `ethicsDecision` (`APPROVED`, `REJECTED`, `HITL`).
- `ethicsComments`.
- `ethicsEvidenceUrl`.

## SLA
- Revisión ética: ≤ 5 días.
- Rechazos → plan de acción en ≤ 10 días.

## Evidencias
- Registro en `EthicsReview` entidad BD.
- Documentos guardados en repositorio de compliance.

