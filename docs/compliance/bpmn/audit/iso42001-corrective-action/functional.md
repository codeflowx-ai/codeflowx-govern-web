# ISO 42001 Corrective Action Workflow – Guía Funcional

## Objetivo
Gestionar de extremo a extremo las no conformidades detectadas en auditorías, monitoreo u otros procesos, cumpliendo la cláusula 10.2 (Acciones correctivas) de ISO 42001.

## Roles
- **NC Owner**: registra la no conformidad y provee contexto.
- **Responsible Assignee**: implementa la acción definida.
- **Approver (Management/Compliance)**: valida acciones críticas.
- **Verifier**: comprueba efectividad antes de cerrar.

## Flujo
1. **Start** – se registra la NC (manual o trigger desde auditoría).
2. **User Task – Assess Non-Conformity**: clasifica severidad (`CRITICAL`, `MAJOR`, `MINOR`), cláusula afectada, descripción.
3. **Service Task – Assign Responsible**: propone dueño basado en cláusula/módulo.
4. **User Task – Root Cause Analysis** (plantilla 5-Why).
5. **User Task – Define Corrective Action**: describe acciones, recursos, fechas.
6. **User Task – Approve Corrective Action** (si severidad crítica → management).
7. **User Task – Implement Corrective Action**: seguimiento por responsable.
8. **Timer Event** – controla fecha comprometida.
9. **User Task – Verify Effectiveness**: valida resultados, añade evidencias.
10. **Gateway Effective?**
    - YES → `Update Non-Conformity Status` a `CLOSED`.
    - NO → loop para redefinir acción.
11. **End**.

## Variables principales
- `nonConformityId`, `severity`, `clause`.
- `rootCause`, `correctiveActionPlan`.
- `responsible`, `dueDate`.
- `verificationResult`, `status`.

## SLA
- Definir acción: ≤ 5 días desde la detección.
- Implementación: según severidad (Máx. 30 días para CRITICAL).
- Verificación: 5 días después de implementación.

## Evidencias
- Registro en tabla `ANCNONCONFORMITY`.
- Documentos adjuntos en Root Cause/Verification.
- Logs en `ImmutableLog`.
# ISO 42001 Corrective Action – Guía Funcional

## Objetivo
Gestionar no conformidades y acciones correctivas conforme a la cláusula 10.2 de ISO 42001, asegurando seguimiento hasta verificar su eficacia.

## Roles
- **NC Owner**: registra la no conformidad y coordina seguimiento.
- **Responsible Engineer**: ejecuta acciones definidas.
- **Quality Manager**: valida eficacia antes de cerrar.

## Flujo
1. **Start** – recibido desde auditoría, monitoreo o reporte manual.
2. **User Task – Assess Non-Conformity**: determina severidad, cláusulas afectadas.
3. **Assign Responsible** (service): autoasigna en función del módulo afectado.
4. **User Task – Root Cause Analysis** (5-Why).
5. **User Task – Define Corrective Action** (plan detallado, due dates).
6. **User Task – Approve Corrective Action** (si severidad crítica requiere management).
7. **User Task – Implement Corrective Action**.
8. **Timer – Due Date** (espera).
9. **User Task – Verify Effectiveness**.
10. **Gateway Effective?**
    - YES → `Update Non-Conformity Status` → End.
    - NO → loop a `Define Corrective Action`.

## Variables
- `nonConformityId`, `severity`, `isoClause`.
- `rootCause`, `correctiveActions`.
- `dueDate`, `actionStatus`.
- `effectiveness`, `closureComments`.

## SLA
- Plan acción: ≤ 5 días tras identificar NC.
- Implementación: según criticidad (configurable).
- Verificación: 10 días tras completar implementación.

## Evidencias
- Registro en tabla `ANCNONCONFORMITY`.
- Documentación RCA adjunta.
- Planes y verificaciones firmados digitalmente.

