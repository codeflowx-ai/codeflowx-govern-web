# ISO 42001 Internal Audit – Guía Funcional

## Objetivo
Gestionar la planificación y ejecución de auditorías internas (cláusula 9.2) del Sistema de Gestión de IA.

## Roles
- **Audit Coordinator**: programa auditoría y asigna auditores.
- **Auditor**: completa checklist y documenta hallazgos.
- **Process Owners**: atienden no conformidades derivadas.

## Flujo
1. **Timer/Event** → inicia auditoría (anual o por demanda).
2. **Generate Audit Checklist**: arma lista basada en cláusulas y alcance seleccionado.
3. **User Task – Schedule Audit**: define fechas, auditor, alcance.
4. **User Task – Conduct Audit**: auditor llena checklist, marca evidencias/fallos.
5. **Service Task – Calculate Compliance Score**.
6. **Gateway – Non conformities?**
   - YES → crea registro de NC (disparando BPMN Corrective Action).
   - NO → continúa.
7. **Record Audit Results** → persiste en repositorio.
8. **End**.

## Variables
- `auditId`, `auditScope`, `auditChecklist`.
- `auditFindings`, `nonConformities`.
- `complianceScore`.

## SLA
- Programación: ≥ 2 semanas antes de ejecución.
- Entrega de informe: ≤ 7 días después de la auditoría.
- Seguimiento NC: enlazado al BPMN Corrective Action (cláusula 10.2).

## Evidencias
- Checklist completado.
- Informe final y score.
- Registro en `ApsAimsPerformance` / módulo de auditorías.


