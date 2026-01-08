## 🧾 Auditoría — Agente de Procesos BPMN (Módulo de Models)

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Alcance:** Auditoría documental (basada en guías/estado del módulo)

---

## 🎯 Objetivo

Auditar el “agente de procesos BPMN” aplicado al módulo **Models**, para asegurar que:

- El workflow **`model-approval-v1`** existe, está versionado y deployable.
- Las **variables de proceso** y **form keys** están alineadas con backend y frontend.
- Se cumple el gobierno **HITL** (grupos, SLAs, trazabilidad de decisión humana).
- Delegates, reglas Drools y tareas automáticas están definidas y con manejo de errores.
- El disparo del proceso desde backend es **determinista**, auditable y no hardcodeado (más allá del process key).

---

## 📚 Artefactos revisados (fuente)

- `MODEL_APPROVAL_WORKFLOW_STATUS.md`
- `ESTADO_ACTUAL_MODULO.md`
- `ESTADO_IMPLEMENTACION_BACKEND.md`
- `GUIA_TIPOS_TAREAS_FLOWABLE.md`
- `GUIA_FUNCIONAL_MODELOS.md`

---

## ✅ Proceso BPMN requerido

### Proceso principal

- **Aprobación de modelos**: `model-approval-v1`
  - Ubicación esperada (según documentación): `codeflowx.govern.workflow.lib/src/main/resources/processes/aios/model-approval-v1.bpmn`
  - Disparo esperado: al crear modelo (backend) y/o por endpoint explícito (si existe)

---

## 🧩 Variables, tareas, formularios y reglas (lo verificable)

### Inputs esperados (Submit Task)

- `modelId`, `versionId`
- `approvalType`: `NEW_MODEL`, `VERSION_UPDATE`, `REDEPLOYMENT`
- `targetEnvironment`: `STAGING`, `PRODUCTION`
- `businessJustification`

### Outputs esperados (automatización + HITL)

- Scores:
  - `performanceScore` (0-100)
  - `biasScore` (0-100)
  - `complianceScore` (0-100)
- Revisión ML:
  - `mlApproval`: `APPROVED`, `REJECTED`, `NEEDS_CHANGES`
  - `reviewNotes`
- Revisión Governance:
  - `governanceApproval`: `APPROVED`, `REJECTED`, `CONDITIONAL`
  - `riskAssessment`: `LOW`, `MEDIUM`, `HIGH`
- Decisión Drools:
  - `finalDecision`: `APPROVED`, `CONDITIONAL_APPROVAL`, `REJECTED`
  - `minScore`, `confidenceLevel`, `justification`, `requiresMonitoring`

### Formularios (FormKey ↔ UI)

Según documentación, deben existir y mapearse correctamente:

- `model-approval-request` (Submit)
- `model-ml-review` (ML Engineer Review)
- `model-governance-review` (Governance Review)
- `model-approval-reminder` (SLA Reminder)

**Puntos a auditar:**

- [ ] Los `formKey` del BPMN corresponden a rutas/UI reales (sin “zul legacy” sin mapping).
- [ ] La UI carga variables del workflow y las renderiza (scores, decisiones previas).
- [ ] Los formularios completan tareas vía API de tasks (complete) con variables correctas.

### Delegates / Service Tasks (automatización)

Delegates mencionados a verificar (o equivalentes):

- `ModelValidationDelegate`
- `BiasDetectionDelegate`
- `ComplianceCheckDelegate`
- `MarkModelProductionDelegate`
- `MarkModelConditionalDelegate`
- `RejectModelDelegate`

**Puntos a auditar:**

- [ ] Todos los delegates existen, están registrados y manejan timeouts/reintentos.
- [ ] Manejo de errores: boundary events y defaults conservadores (si aplica) documentados.
- [ ] Persistencia: los delegates actualizan entidades (`ModelApproval`, `Model`) de forma transaccional y auditable.

### Drools (BusinessRuleTask)

- Reglas: `model-approval-scoring.drl` (según documentación)
- Fact: `ModelApprovalFact`

**Puntos a auditar:**

- [ ] KieBase/KieSession configuradas y cargan reglas en runtime.
- [ ] Variables del proceso se mapean correctamente al Fact (inputs/outputs).
- [ ] Reglas contemplan diferencias STAGING vs PRODUCTION (umbral, rigor).

---

## 👥 HITL: grupos, SLAs y trazabilidad

### Candidate groups esperados

- Submit: `ml-engineers`
- ML Review: `ml-engineers`, `senior-ml-engineers`
- Governance Review: `governance-admins`
- Reminder: `governance-leads`

### SLA esperado

- Governance Review: **3 días** (Timer Boundary + Reminder)

**Puntos a auditar:**

- [ ] Los grupos existen en IAM y no quedan tareas huérfanas.
- [ ] La decisión humana queda auditada (actor, timestamp, comentario si aplica).
- [ ] SLA no cancela tareas; dispara recordatorio y queda registro del evento.

---

## 🔁 Integración backend ↔ BPMN

**Puntos a auditar:**

- [ ] El backend dispara `model-approval-v1` con variables mínimas (modelId, modelName, modelType, version, etc.).
- [ ] El backend persiste `workflowInstanceId` y expone consulta de estado (approval/workflowStatus).
- [ ] No hay disparos duplicados por reintentos (idempotencia por modelId/versionId).

---

## ⚠️ Riesgos / hallazgos típicos (a vigilar)

- **FormKey legacy**: referencias `.zul` sin mapping real a UI actual.
- **Divergencia de variables**: DTOs/campos backend vs variables BPMN.
- **HITL sin IAM**: candidate groups no existen o cambian de nombre.
- **Reglas Drools invisibles**: reglas no versionadas/gestionadas como artefacto gobernado.

---

## 📌 Entregables esperados del agente (salida)

1. Inventario del BPMN `model-approval-v1` (tareas, gateways, eventos, versiones).
2. Matriz “tarea → variables → delegate/form → endpoint”.
3. Checklist HITL (grupos/SLA/auditoría) con brechas.
4. Recomendaciones de hardening: errores, idempotencia, trazabilidad, versionado.

---

## 📌 Referencias internas

- `MODEL_APPROVAL_WORKFLOW_STATUS.md`
- `GUIA_TIPOS_TAREAS_FLOWABLE.md`
- `ESTADO_ACTUAL_MODULO.md`
