# ISO 42001 Competence Gap Closure – Guía Funcional

## Objetivo
Dar cumplimiento a la cláusula 7.2 (Competencia) asegurando que las brechas de habilidades identificadas en personal clave se documentan, planifican y verifican.

## Roles
- **HR / L&D Lead**: identifica brechas y define plan de formación.
- **Manager**: aprueba y supervisa la ejecución.
- **Participant**: completa las actividades asignadas.

## Flujo propuesto
1. **Start** – registro de brecha (fuente: evaluación, auditoría, etc.).
2. **User Task – Assess Competence Gap**: define competencias requeridas, nivel actual, criticidad.
3. **Service Task – Recommend Training Plan**: sugiere cursos/recursos (puede consultar catálogo).
4. **User Task – Approve Training Plan** (manager).
5. **User Task – Execute Training Plan** (multi-instancia por actividad).
6. **Timer / Reminder**: seguimiento de progreso.
7. **User Task – Verify Competence Closure**: evaluación post-formación.
8. **Gateway**:
   - `Closed` → actualizar registro y finalizar.
   - `Not closed` → ajustar plan (loop).
9. **End**.

## Variables
- `competenceGapId`, `competenceRequired`, `currentLevel`, `targetLevel`.
- `trainingPlan` (lista de actividades).
- `planStatus`, `completionRate`.
- `verificationResult`, `nextReviewDate`.

## SLA
- Diseño del plan: ≤ 5 días.
- Ejecución: dependiente de plan (definir fecha objetivo).
- Verificación: dentro de 10 días tras concluir actividades.

## Evidencias
- Registro en tabla `CompetenceGap`.
- Certificados/documentos de formación adjuntos.

