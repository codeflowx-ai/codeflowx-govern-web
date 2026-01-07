## 🧾 Auditoría — Agente de Procesos BPMN (Módulo de Agentes)

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Alcance:** Auditoría documental (basada en guías del módulo)

---

## 🎯 Objetivo

Auditar el “agente de procesos BPMN” como responsabilidad funcional para asegurar que:

- Los **procesos BPMN requeridos** existen, están versionados y ubicados donde indica la guía.
- Los **disparadores (APIs/eventos)** y las **variables de proceso** están alineados con el backend.
- Se cumple el gobierno **HITL** (tareas humanas, grupos, SLAs) y el ciclo de vida (aprobación, certificación, retiro, etc.).
- La selección de proceso por agente sea **configurable** (riesgo/contexto) y trazable.

---

## 📚 Artefactos revisados (fuente)

- `GUIA_BPMN_AGENTES.md`
- `GUIA_CONFIGURACION_PROCESOS_AGENTES.md`
- `GUIA_DESARROLLO_BACKEND_AGENTES.md`
- `GUIA_FUNCIONAL_AGENTES.md`
- `ANALISIS_COMPARATIVO_GOVERNABILIDAD.md` (brechas/recomendaciones sobre configuración explícita)

---

## ✅ Procesos BPMN requeridos (según guía BPMN)

La guía BPMN define procesos que el sistema debe **verificar** y (si aplica) **crear automáticamente**.

### Procesos y ubicación esperada

- **Aprobación de agentes**: `agent-approval-v1`
  - Ubicación: `data/bpmn-processes/aios/agent-approval-v1.bpmn`
  - Disparador: `POST /api/v1/agents/approval`
  - Incluye **UserTask** HITL con **SLA 24h** y grupos candidatos (ej: `ml-engineers`, `governance-admins`)

- **Certificación**: `agent-certification-v1`
  - Ubicación: `data/bpmn-processes/aios/agent-certification-v1.bpmn` (crear si no existe)
  - Disparador: `POST /api/v1/agents/registry/{id}/certification`
  - Incluye **UserTask** con **SLA 48h** (ej: `compliance-officers`, `governance-managers`)

- **Retiro**: `agent-retirement-v1`
  - Ubicación: `data/bpmn-processes/aios/agent-retirement-v1.bpmn` (crear si no existe)
  - Disparador: `POST /api/v1/agents/registry/{id}/retirement`
  - Incluye **UserTask** con **SLA 72h** (ej: `governance-managers`, `project-managers`)

- **Política de gobierno**: `agent-governance-policy-v1`
  - Ubicación: `data/bpmn-processes/aios/agent-governance-policy-v1.bpmn` (crear si no existe)
  - Disparador: `POST /api/governance/agents/governance`

- **Revisión HITL de decisiones**: `agent-decision-review-v1`
- **Aprobación HITL de reversiones**: `agent-rollback-approval-v1`

> Nota: esta auditoría se limita a lo explicitado por las guías; la guía funcional puede implicar más procesos/variantes.

---

## 🧩 Variables, tareas y delegados (lo verificable)

### Aprobación (`agent-approval-v1`)

La guía especifica:

- Variables del proceso (ej: `approvalId`, `agentUuid`, `approvalType`, `approverEmail`, etc.).
- Tareas: ServiceTasks (risk/compliance/ethics), BusinessRuleTask (Drools), gateway de decisión y UserTask HITL.
- Delegados Java a verificar (ejemplos):
  - `AIRiskAssessmentDelegate`, `AIComplianceCheckDelegate`, `AIEthicalReviewDelegate`
  - `AutoApproveAgentDelegate`, `AutoRejectAgentDelegate`

**Puntos a auditar:**

- [ ] Variables del proceso coinciden con DTO/campos reales del backend.
- [ ] Existen los delegados y están registrados.
- [ ] Reglas Drools referenciadas están disponibles/configuradas.
- [ ] SLA y grupos candidatos están configurados como exige la guía.

---

## ⚙️ Configuración dinámica por agente (según guía de configuración)

La guía de configuración propone un modelo para seleccionar procesos por:

- Tipo de agente, riesgo, contexto, condiciones (JSONB), prioridad.

### Tablas documentadas (configuración)

- `cor_agent_process_config`
- `cor_agent_rule_config`
- `cor_agent_algorithm_config`

Todas con:

- **PK autonumérica** `BIGSERIAL PRIMARY KEY`
- `agent_uuid` como vínculo lógico a `AGTAGENTS.agtuuid`
- Índices por `agent_uuid`, tipo, `is_active`

**Puntos a auditar:**

- [ ] La selección de `process_key` se resuelve de forma determinista (priority + conditions + fallback).
- [ ] Se evita duplicidad/conflictos (constraint unique definida).
- [ ] Trazabilidad: quién cambió configuración y cuándo (created/updated/by).

---

## 🔁 Integración con backend / runtime

La guía de configuración describe la integración con runtime:

- Servicio de selección de proceso (`getProcessKey(...)`) + lanzamiento de proceso (`launchProcess(...)`).

**Puntos a auditar:**

- [ ] El runtime usa configuración por agente (y no hardcode).
- [ ] El “fallback” a proceso por defecto es explícito y auditable.
- [ ] Se registran los eventos de inicio/fin/decisión del proceso para auditoría.

---

## ⚠️ Riesgos / hallazgos (basados en documentación)

- **Brecha histórica reportada**: falta claridad de “qué proceso se lanza por agente” sin configuración explícita (aparece como mejora en análisis comparativo). La guía de configuración propone cubrirlo con `cor_agent_process_config`.
- **Riesgo de divergencia**: si BPMN templates se “auto-crean”, se debe controlar versionado y gobernanza del artefacto BPMN (quién lo generó, cuándo, base template).
- **Riesgo HITL**: SLAs y grupos candidatos deben alinearse con IAM/roles reales; si no, quedan tareas huérfanas.

---

## ✅ Checklist de auditoría (accionable)

### Artefactos BPMN

- [ ] Existen todos los BPMN requeridos en las rutas indicadas.
- [ ] Están versionados (v1, v2…) con política clara (compatibilidad).
- [ ] Validación BPMN: deployable en motor y sin errores.

### Contratos (API ↔ BPMN)

- [ ] Los endpoints disparadores existen y registran `processInstanceId`.
- [ ] Variables de proceso alineadas con DTOs/entidades reales.
- [ ] Manejo de errores: qué pasa si falla un delegate/regla.

### HITL

- [ ] Candidate groups y SLAs configurados como dicta la guía.
- [ ] Registro/auditoría de decisiones humanas (actor, timestamp, comentario obligatorio si aplica).

### Configuración por agente

- [ ] Tablas `cor_*` implementadas/operativas (si el objetivo es selección dinámica).
- [ ] UI/API de configuración (checklist de la guía de configuración).
- [ ] Trazabilidad de cambios en configuración (auditoría).

---

## 📌 Referencias internas

- `GUIA_BPMN_AGENTES.md` (procesos requeridos, tareas, variables, SLAs)
- `GUIA_CONFIGURACION_PROCESOS_AGENTES.md` (tablas `cor_*`, selección y runtime)
- `ANALISIS_COMPARATIVO_GOVERNABILIDAD.md` (áreas críticas: configuración explícita de procesos)
