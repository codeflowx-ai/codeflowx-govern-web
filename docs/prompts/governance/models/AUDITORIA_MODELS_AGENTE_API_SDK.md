## 🧾 Auditoría — Agente API & SDK (Módulo de Models)

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Alcance:** Auditoría documental (basada en guías/estado del módulo)

---

## 🎯 Objetivo

Auditar la capa **API (BFF + microservicio)** y su eventual **SDK/cliente** para el módulo **Models**, asegurando que:

- Los **contratos REST** (paths, DTOs, filtros, paginación, errores) están definidos y son consistentes.
- La segmentación **BFF ↔ microservicio** está clara (sin duplicidades, con trazabilidad).
- Se cumple el gobierno de datos: **nombres, prefijos, PK autonumérica**, y **consistencia de estados** (approval, lifecycle).
- Existen puntos de integración para **telemetría** (costes/uso/métricas) y para **workflow** (approval).
- La API es **KISS**, extensible y alineada con **SOLID** y arquitectura hexagonal (puertos/adaptadores).

---

## 📚 Artefactos revisados (fuente)

- `ESTADO_ACTUAL_MODULO.md`
- `ESTADO_IMPLEMENTACION_BACKEND.md`
- `MODEL_APPROVAL_WORKFLOW_STATUS.md`
- `GUIA_FUNCIONAL_MODELOS.md`
- `technical.md`
- `functional.md`

---

## 🧩 Superficie API esperada (contratos)

### BFF: `codeflowx.govern.bff.compliance`

Según el estado del módulo, el BFF expone (mínimo) endpoints:

- **Modelos (CRUD)**
  - `GET /api/v1/models` (filtros: search, type, status)
  - `GET /api/v1/models/{id}`
  - `POST /api/v1/models`
  - `PUT /api/v1/models/{id}`
  - `DELETE /api/v1/models/{id}`

- **Versiones**
  - `GET /api/v1/models/{id}/versions`
  - `POST /api/v1/models/{id}/versions`
  - `PUT /api/v1/models/{id}/versions/{versionId}` (si aplica)
  - `DELETE /api/v1/models/{id}/versions/{versionId}` (si aplica)

- **Providers / Credentials**
  - `GET /api/v1/models/providers`
  - `GET /api/v1/models/providers/{id}`
  - `POST /api/v1/models/providers`
  - `PUT /api/v1/models/providers/{id}`
  - `DELETE /api/v1/models/providers/{id}`
  - `GET /api/v1/models/providers/{id}/credentials`
  - `POST /api/v1/models/providers/{id}/credentials`
  - `PUT /api/v1/models/providers/{id}/credentials/{credentialId}`
  - `DELETE /api/v1/models/providers/{id}/credentials/{credentialId}`

- **Integraciones MLOps**
  - `GET /api/v1/models/{id}/integrations`
  - `GET /api/v1/models/{id}/integrations/{platform}`
  - `POST /api/v1/models/{id}/integrations`
  - `DELETE /api/v1/models/{id}/integrations/{integrationId}`
  - Config global:
    - `GET /api/v1/models/mlops-integrations`
    - `GET /api/v1/models/mlops-integrations/{id}`
    - `POST /api/v1/models/mlops-integrations`
    - `PUT /api/v1/models/mlops-integrations/{id}`
    - `DELETE /api/v1/models/mlops-integrations/{id}`

### Microservicio: `codeflowx-governance-models-service`

Debe reflejar los mismos casos de uso core y exponer endpoints internos coherentes con el BFF (mismo modelo de DTOs, estados y validaciones).

---

## ✅ Checklist de auditoría (accionable)

### Contratos y consistencia

- [ ] Los endpoints listados existen y están documentados (request/response, filtros, validación).
- [ ] Estándar de errores: estructura única (ej: `code`, `message`, `details`, `traceId`).
- [ ] Estados son consistentes y enumerados (ej: `PENDING`, `APPROVED`, `REJECTED`, `CONDITIONAL_APPROVAL`).
- [ ] Versionado de API (prefijo `/api/v1`) y estrategia de compatibilidad.
- [ ] Idempotencia definida para operaciones sensibles (ej: reintentos BFF, creación de workflow).

### DTOs / Mapping / Validación

- [ ] DTOs incluyen campos esperados (incl. precios de referencia, costes agregados, estado de aprobación).
- [ ] Mapeo DTO↔Entidad no filtra datos sensibles (credenciales nunca salen en claro).
- [ ] Validación de inputs: tipos, rangos, listas permitidas, nullability.

### Persistencia y convenciones de BD (gobierno)

- [ ] Tablas del módulo cumplen convención de prefijo de módulo y PK autonumérica:
  - Ejemplos a verificar: `MODMODELAPPROVALS`, tablas `MOD*` relacionadas.
- [ ] Relación de entidades en 3FN (evitar duplicidad de campos derivados sin estrategia clara).
- [ ] Índices por campos de filtrado frecuente (uuid/status/type/providerId) y FKs.
- [ ] Auditoría de cambios: `createdAt/createdBy/updatedAt/updatedBy` (donde aplique).

### Arquitectura (KISS / SOLID / Hexagonal)

- [ ] Casos de uso en servicios de negocio (dominio) no dependen de detalles de transporte (WebClient/REST).
- [ ] Adaptadores (BFF, controllers) delegan a puertos/servicios, sin lógica de negocio compleja.
- [ ] Separación clara: “lectura” vs “escritura” (evitar endpoints “todo en uno”).

### Resiliencia y observabilidad mínima

- [ ] Resilience4j (CB/Retry/Timeout) con políticas explícitas por operación.
- [ ] Propagación de `traceId` / `correlationId` entre BFF y microservicio.
- [ ] Logs estructurados en puntos críticos (create/update, disparo workflow, integraciones MLOps).

---

## 🔌 SDK / Cliente (si aplica)

Si existe (o se va a generar) un SDK:

- [ ] Cliente tipado (DTOs y enums) alineado con API.
- [ ] Manejo de errores con mapeo a excepciones semánticas (ej: `ValidationError`, `NotFound`, `Conflict`).
- [ ] Retries idempotentes únicamente donde aplique.
- [ ] Compatibilidad por versión (API v1) y estrategia de deprecación.

---

## ⚠️ Riesgos / hallazgos típicos (a vigilar)

- **Divergencia BFF ↔ microservicio**: campos/estados distintos, rompen UI y automatizaciones.
- **Aprobación “parcial”**: workflow existe pero faltan endpoints de acción/consulta consistentes.
- **Costes/métricas**: UI con mock data; riesgo de que contratos “cambien” al integrar telemetría real.
- **Credenciales**: exposición accidental en DTOs/logs.

---

## 📌 Entregables esperados del agente (salida)

1. Lista de endpoints por dominio (Models, Versions, Providers, Credentials, Integrations, Approval, Analysis).
2. Tabla “endpoint → DTOs → entidad → persistencia” (rutas, inputs/outputs, validaciones).
3. Lista de brechas (faltantes/duplicados/inconsistencias) con severidad y recomendación KISS.
4. Recomendaciones de versionado y contrato para SDK (si aplica).

---

## 📌 Referencias internas

- `ESTADO_ACTUAL_MODULO.md`
- `ESTADO_IMPLEMENTACION_BACKEND.md`
- `MODEL_APPROVAL_WORKFLOW_STATUS.md`
- `GUIA_FUNCIONAL_MODELOS.md`
