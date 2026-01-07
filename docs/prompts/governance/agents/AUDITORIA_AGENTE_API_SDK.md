## 🧾 Auditoría — Agente API y SDK (Módulo de Agentes)

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Alcance:** Auditoría documental (basada en guías del módulo)

---

## 🎯 Objetivo

Auditar el **contrato de APIs** del módulo de agentes y su **consumo por frontend/SDK**, para asegurar:

- Consistencia de rutas, DTOs y convenciones.
- Trazabilidad/auditoría (campos y flujos).
- Seguridad mínima (validación, errores, control de acceso).
- Preparación para generar/operar un SDK (contrato estable y versionado).

---

## 📚 Artefactos revisados (fuente)

- `GUIA_DESARROLLO_BACKEND_AGENTES.md`
- `GUIA_DESARROLLO_FRONTEND_AGENTES.md`
- `GUIA_FUNCIONAL_AGENTES.md`
- `GUIA_BPMN_AGENTES.md` (por rutas que disparan procesos)
- `GUIA_TELEMETRIA_AGENTES.md` (por endpoints de telemetría relacionados)

---

## 🧱 Arquitectura y puntos de integración (según documentación)

- **Backend (Java/Spring)**:
  - Microservicio REST: `codeflowx-governance-agents-service/`
  - Capas: Controller → Business Service → Repository → PostgreSQL (negocio)
  - Base path de API: `@RequestMapping("/api/v1/agents")`

- **Frontend (Next.js)**:
  - Consumo vía `fetch()` a endpoints `/api/v1/agents/...`
  - Ejemplos documentados: listados y CRUD genéricos (`my-entity`, `registry/list`)

---

## 🔌 Contratos de API identificados en la documentación

### Base y versionado

- **Prefijo de versión**: `/api/v1/...` (documentado).
- **Recomendación de auditoría**: validar que toda la superficie pública del módulo de agentes (registry, approvals, deployments, etc.) cuelga del mismo esquema de versionado.

### Endpoints documentados explícitamente

- **Listado registry (ejemplo)**:
  - `GET /api/v1/agents/registry/list` (ejemplo en guía backend y frontend)

- **Aprobación / workflows BPMN (disparadores)**:
  - `POST /api/v1/agents/approval` (proceso `agent-approval-v1`)
  - `POST /api/v1/agents/registry/{id}/certification` (proceso `agent-certification-v1`)
  - `POST /api/v1/agents/registry/{id}/retirement` (proceso `agent-retirement-v1`)
  - `POST /api/governance/agents/governance` (proceso `agent-governance-policy-v1`)

> Nota: la guía funcional describe muchas capacidades/pestañas. Esta auditoría asume que existen endpoints adicionales para esas áreas, pero **no están enumerados aquí** si no aparecen explícitos en los documentos revisados.

---

## 🗄️ Convenciones de datos y nomenclatura (impacto en API/SDK)

### Modelo de negocio (PostgreSQL)

La guía backend fija:

- **Tablas**: prefijo de 3 caracteres (ej: `AGT...`)
- **PK autonumérica**: `@GeneratedValue(strategy = GenerationType.IDENTITY)`
- **Campos de auditoría**: `createdat`, `createdby`, `updatedat`, `updatedby`

### DTOs (contrato hacia fuera)

- DTOs en camelCase sin prefijo (ej: `AgentDto` con `id`, `uuid`, `name`, `version`, …)

**Puntos a verificar en auditoría real:**

- Alineación estricta entre entidad ↔ DTO (nombres, nulos, longitudes, enums).
- Campos mínimos para trazabilidad (uuid, versión, timestamps, actor).

---

## 🔐 Seguridad y validación (lo documentado y lo verificable)

La guía backend incluye una sección de **validación y seguridad** a nivel de patrones.

**Checklist de verificación (API):**

- [ ] Validación de DTOs en endpoints críticos (create/update/approval).
- [ ] Manejo de errores consistente (códigos HTTP + payload de error estable).
- [ ] Control de acceso por rol/grupo alineado con procesos (ej: aprobadores, compliance, governance).
- [ ] Rate limiting / protección de endpoints sensibles (si aplica).

---

## 🧩 SDK: estado y brechas documentales

En los artefactos revisados:

- Se documenta consumo **Frontend** con `fetch()`.
- **No** se encontró una especificación formal de **SDK** (lenguaje, generación, publicación, versionado, semver, compatibilidad).

**Brechas a cerrar (para poder afirmar “API + SDK”):**

- [ ] Publicar contrato OpenAPI (mencionado como referencia en backend) como **fuente de verdad**.
- [ ] Definir estrategia de generación del SDK (ej: desde OpenAPI).
- [ ] Definir compatibilidad hacia atrás (breaking changes) y política de versionado.
- [ ] Definir autenticación y configuración del cliente (baseURL, headers, retries, timeouts).

---

## ⚠️ Riesgos / hallazgos (basados en documentación)

- **Riesgo de cobertura contractual**: la guía funcional describe una superficie amplia; si el contrato no está centralizado (OpenAPI), el SDK y el frontend pueden divergir.
- **Riesgo de coherencia de rutas**: hay rutas bajo `/api/v1/agents/...` y al menos una bajo `/api/governance/agents/...` (política de gobierno). Conviene decidir si “governance” es un submódulo bajo el mismo versionado.
- **Riesgo de trazabilidad incompleta**: si DTOs expuestos omiten campos de auditoría o correlación, se degrada la auditabilidad.

---

## ✅ Checklist de auditoría (accionable)

### Contrato (API)

- [ ] Inventariar endpoints reales del módulo de agentes (registry, approvals, deployments, monitoring, decisions/HITL, rollback, compliance, etc.).
- [ ] Confirmar base path y versionado consistente (`/api/v1/...`) para toda la superficie pública.
- [ ] Verificar que los endpoints que disparan BPMN están documentados y testados (happy-path y errores).
- [ ] Verificar DTOs: compatibilidad, campos obligatorios, enums, límites de tamaño.
- [ ] Verificar idempotencia donde aplique (aprobaciones, eventos, comandos).

### Seguridad

- [ ] Matriz endpoint → rol/grupo (mínimo: governance managers, compliance officers, ml engineers).
- [ ] Validación de entrada + sanitización (JSON/strings grandes).
- [ ] Política de errores (no filtrar info sensible).

### SDK

- [ ] OpenAPI publicado (y versionado) como fuente de verdad.
- [ ] Pipeline de generación + publicación de SDK.
- [ ] Suite de tests de contrato (contract tests) entre SDK ↔ API.

---

## 📌 Referencias internas

- `GUIA_DESARROLLO_BACKEND_AGENTES.md` (módulos, convenciones, `@RequestMapping("/api/v1/agents")`)
- `GUIA_DESARROLLO_FRONTEND_AGENTES.md` (consumo `fetch()` hacia `/api/v1/agents/...`)
- `GUIA_BPMN_AGENTES.md` (endpoints que disparan procesos y variables)
