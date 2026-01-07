## 🧾 Auditoría — Agente de Telemetría (Módulo de Agentes)

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Alcance:** Auditoría documental (basada en guías del módulo)

---

## 🎯 Objetivo

Auditar el “agente de telemetría” como responsabilidad de plataforma para asegurar que:

- El **ingreso de eventos** (schema + validación) es consistente y resistente.
- La **persistencia** escala (≈10M eventos/día) y respeta el diseño de **BD separada**.
- La telemetría habilita **gobernanza**: reglas (Drools), soporte HITL, auditoría y métricas.
- Existe **correlación** con telemetría a nivel SO (Prometheus) para cumplimiento y security auditing.

---

## 📚 Artefactos revisados (fuente)

- `GUIA_TELEMETRIA_AGENTES.md`
- `MEJORAS_PENDIENTES_TELEMETRIA.md`
- `ANALISIS_COMPARATIVO_GOVERNABILIDAD.md` (telemetría multi-capa y gaps)

---

## 🧱 Arquitectura objetivo (según documentación)

### Flujo general

- Runtime del agente emite **eventos JSON**.
- Backend Java:
  1) valida schema,
  2) ejecuta reglas Drools,
  3) decide necesidad HITL,
  4) llama LLMs si aplica,
  5) persiste en **BD de telemetría**.

### Separación de bases de datos

- **BD de negocio (PostgreSQL)**: gestión del ciclo de vida (agentes, aprobaciones, certificaciones, etc.).
- **BD de telemetría (time-series / analytics)**: eventos masivos (`agent_interactions`, `agent_decisions`, `agent_alerts`, etc.).
- Relación **lógica** por `agent_uuid` (sin FK física).
- Sincronización selectiva a negocio solo para eventos relevantes (decisiones HITL, alertas críticas, etc.).

---

## 📋 Contrato de eventos (schema) — lo documentado

La guía define al menos:

- `AgentInteraction` (interacción)
- `AgentDecision` (decisión)
- `AgentAlert` (alerta)

Campos recurrentes:

- `eventType`, `timestamp`
- `agentUuid`, `agentName`
- “metrics” (duración, tokens, coste, modelo, provider)
- “context” (environment, deploymentId, version, etc.)

**Puntos a auditar:**

- [ ] Versionado de schema (compatibilidad hacia atrás).
- [ ] Validación con JSON Schema en ingestión (fail-fast + errores claros).
- [ ] Idempotencia por `eventId` (evitar duplicados en reintentos/batch).

---

## 📥 Endpoints de recepción (lo documentado)

La guía de telemetría incluye:

- `POST /api/v1/telemetry/agents/events`
- `POST /api/v1/telemetry/agents/events/batch`

**Puntos a auditar:**

- [ ] Límite de tamaño/peso por evento y por batch.
- [ ] Backpressure/cola (si hay picos).
- [ ] Rate limiting y autenticación (evitar abuso).
- [ ] Métricas del propio pipeline de ingestión (latencia, cola, tasa de error).

---

## 🧠 Reglas y decisión HITL (lo documentado)

La guía contempla:

- Ejecución de **Drools**.
- Posible uso de **LLMs** para razonamiento (p. ej. “¿requiere revisión humana?”).
- Creación de tareas HITL cuando corresponde y sincronización selectiva a BD de negocio.

**Puntos a auditar:**

- [ ] Determinismo: cuándo se usa Drools vs LLM (y por qué).
- [ ] Trazabilidad: razón de “HITL_REQUIRED” (campo explícito + evidencia).
- [ ] Auditoría: actor/fecha/estado para decisiones revisadas.

---

## 🔗 Correlación con Prometheus / telemetría multi-capa (gap principal)

Los documentos indican:

- ✅ Prometheus ya implementado en AI OS (CPU/mem/I/O/red + procesos SO).
- ❌ Falta correlación entre eventos de aplicación y métricas Prometheus:
  - vincular `event_id` ↔ labels en Prometheus,
  - agregar `prometheus_metrics` y desglose de latencia a eventos,
  - correlacionar **procesos iniciados por agente** (PID/PPID/hijos),
  - dashboards combinados.

**Puntos a auditar:**

- [ ] Estrategia de correlación (labels: `agent_uuid`, `event_id`, `project_id`, etc.).
- [ ] Ventanas de consulta (`queryRange`) alrededor del timestamp del evento.
- [ ] Impacto de rendimiento de correlación (consultas Prometheus + persistencia).

---

## ⚠️ Riesgos / hallazgos (basados en documentación)

- **Riesgo de cumplimiento/security auditing**: sin correlación a nivel SO no se puede responder bien “qué procesos inició realmente” / “de dónde viene la latencia” (requisito típico de auditoría).
- **Riesgo de coste**: 10M eventos/día exige disciplina de retención, particionamiento e índices; cualquier JOIN con BD de negocio debe hacerse en app, no en BD.
- **Riesgo de integridad**: sin idempotencia por `eventId`, los reintentos duplican métricas y contaminan KPIs.

---

## ✅ Checklist de auditoría (accionable)

### Ingestión

- [ ] JSON Schema validado (por tipo de evento).
- [ ] Política de errores (HTTP codes + payload estable).
- [ ] Idempotencia por `eventId`.
- [ ] Batch: límites, reintentos, partial failures.

### Persistencia (BD telemetría)

- [ ] Particionado por tiempo (y/o agente).
- [ ] Índices mínimos: `agent_uuid`, `timestamp`, `event_type`.
- [ ] Retención y agregaciones (rollups) definidos.

### Gobernanza

- [ ] Reglas Drools: versionado, catálogo, pruebas.
- [ ] HITL: creación de tareas + sincronización a negocio (solo lo crítico).
- [ ] Evidencia de revisión (comentario/actor/timestamp).

### Correlación Prometheus

- [ ] Modelo de correlación (labels y campos en eventos).
- [ ] Desglose de latencia (CPU/I/O/red/cola).
- [ ] Procesos SO iniciados por agente correlacionados con eventos.
- [ ] Dashboard unificado (app telemetry + Prometheus + métricas de negocio).

---

## 📌 Referencias internas

- `GUIA_TELEMETRIA_AGENTES.md` (eventos, endpoints, BD separada, HITL)
- `MEJORAS_PENDIENTES_TELEMETRIA.md` (plan de correlación Prometheus)
- `ANALISIS_COMPARATIVO_GOVERNABILIDAD.md` (telemetría multi-capa y brechas)
