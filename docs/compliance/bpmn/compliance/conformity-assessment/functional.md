# Conformity Assessment Process – Guía Funcional

## Objetivo
Ejecutar la evaluación de conformidad según Art. 43 / Anexo VI del EU AI Act antes de publicar un sistema de alto riesgo (aplica a proveedores y, en ciertos casos, deployers).

## Roles
- **Compliance Officer**: inicia y gestiona el assessment.
- **QMS Lead**: responde a gaps de calidad.
- **Technical Documentation Owner**: carga evidencias Anexo IV.
- **Management**: aprueba o rechaza el resultado final.

## Flujo
1. **Initiate Assessment (User Task)**: se selecciona proyecto y tipo (`SELF`, `NOTIFIED_BODY`).
2. **Step 2 – Verify QMS Compliance**: calcula `qmsScore` y `qmsGaps`.
3. **Gateway QMS**:
   - NO → tarea `Review QMS Gaps`, posibilidad de cancelar o reintentar.
   - SÍ → continuar.
4. **Step 3 – Review Technical Documentation**: integra Anexo IV (incluye micro Python de doc técnica).
5. **Gateway Doc**:
   - NO → `Complete Documentation` (loop).
   - SÍ → continuar.
6. **Step 4 – Verify Process Consistency**: valida diseño, post-market plan.
7. **Gateway All Steps Pass?** → `Final Review` o `Generate Conformity Report`.
8. **Generate Conformity Report**: produce PDF + `overallScore`.
9. **Approve Conformity Assessment (User Task)**: decisión management/compliance.
10. **Gateway Approved?**:
    - YES → `MarkAsConformityAssessed`, opcional disparar EU declaration.
    - NO → `DocumentRejection`.

## Variables principales
- `projectId`, `assessmentType`.
- `qmsCompliant`, `qmsScore`, `qmsGaps`.
- `docComplete`, `docScore`, `docGaps`.
- `processConsistent`.
- `overallScore`, `approved`.
- `reportUrl`.

## SLA
- Cada paso técnico: ≤ 5 días.
- Aprobación final: ≤ 3 días tras recibir reporte.

## Entradas / evidencias
- QMS results (Art. 17).
- Annex IV technical documentation.
- Process consistency artefacts (design + post-market monitoring plan).

