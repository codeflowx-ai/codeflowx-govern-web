## 🧾 Auditoría — Agente de Telemetría (Módulo de Models)

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Alcance:** Auditoría documental (basada en guías/estado del módulo)

---

## 🎯 Objetivo

Auditar la **telemetría** del módulo **Models** (costes, uso, métricas, eventos y trazas) para asegurar que:

- Existe un modelo coherente para **capturar uso** (tokens/requests/latencia/errores) por modelo, versión, proyecto y proveedor.
- Se pueden calcular **costes agregados** (diario/mensual/total) de forma reproducible y auditada.
- La observabilidad cubre el ciclo de vida: registro → aprobación → despliegue → uso → incidentes.
- Se preserva la privacidad/seguridad: no se loguea PII ni prompts/outputs sensibles (según política).
- La telemetría habilita gobierno: **alertas**, **dashboards**, **auditoría**, y criterios para aprobación/monitorización.

---

## 📚 Artefactos revisados (fuente)

- `ESTADO_ACTUAL_MODULO.md` (costes/métricas: UI con mock; backend con campos agregados)
- `ESTADO_IMPLEMENTACION_BACKEND.md` (brechas históricas vs estado actual)
- `MODEL_APPROVAL_WORKFLOW_STATUS.md` (requiresMonitoring, scores, decisiones)
- `PROPUESTA_CAMPOS_COSTES.md`
- `RESPUESTAS_CON_INTEGRACION.md` (si aplica a integraciones/telemetría)
- `technical.md`

---

## 🧭 Alcance de telemetría (qué medir)

### Métricas de uso (mínimo)

- Requests: total, éxito/error, tasa de error
- Latencia: p50/p95/p99
- Tokens/consumo: input/output, por modelo y versión
- Coste estimado y real (si hay facturación)
- Uso por dimensiones:
  - Modelo (`modelId`/UUID)
  - Versión (`versionId` / semver)
  - Proyecto (`projectId`)
  - Proveedor / credencial (sin exponer secretos)
  - Entorno (`STAGING`, `PRODUCTION`)

### Eventos de gobierno (mínimo)

- `model.created`, `model.updated`, `model.deleted`
- `model.approval.started`, `model.approval.decision`, `model.approval.completed`
- `model.deployment.created/updated`, `model.integration.enabled/disabled`
- `model.usage.recorded` (agregado o evento por request, según volumen)

---

## ✅ Checklist de auditoría (accionable)

### Modelo de datos y agregación de costes

- [ ] Existe fuente de verdad para “uso” (ej: `ModelUsage` o equivalente) o integración con un sistema de métricas.
- [ ] Si existen campos agregados en `Model` (ej: `modtotalcost`, `modmonthlycost`, `moddailycost`, `modtotaltokensconsumed`):
  - [ ] Hay job/servicio que los actualiza con cadencia definida (no “valores congelados”).
  - [ ] La agregación es reproducible (se puede recalcular desde histórico).
  - [ ] Se documenta precisión y redondeo (por millón tokens, por imagen, por minuto, moneda).
- [ ] La definición de tablas respeta convención del módulo (prefijo + PK autonumérica) y 3FN para histórico.

### Instrumentación (logs, métricas, trazas)

- [ ] Logs estructurados: `traceId`, `correlationId`, `modelId`, `versionId`, `projectId`, `provider`.
- [ ] Métricas técnicas: tiempos de llamada a proveedores y errores por código/causa.
- [ ] Trazas distribuidas (BFF → microservicio → proveedor/external MLOps) con propagación de contexto.
- [ ] Sampling y retención definidos (evitar costes excesivos o pérdida de señales).

### Seguridad y compliance (telemetría segura)

- [ ] No se registran secretos (tokens/keys) ni credenciales.
- [ ] No se registran prompts/outputs completos si violan política (usar hashing/redacción).
- [ ] Roles/permisos para ver telemetría (por proyecto/entorno).
- [ ] Auditoría de accesos (quién consultó métricas sensibles).

### Integración con workflow de aprobación

- [ ] Scores (`performanceScore`, `biasScore`, `complianceScore`) quedan persistidos con trazabilidad.
- [ ] `requiresMonitoring` deriva en una política operativa:
  - [ ] alertas específicas (error rate, drift, bias)
  - [ ] “modo monitorizado” por un periodo, y criterio de salida
- [ ] Eventos del workflow se correlacionan con cambios de estado del modelo (`modapprovalstatus`, etc.).

### Dashboards y alertas (operación)

- [ ] Dashboard por modelo y por proyecto: uso, costes, latencia, errores, tendencias.
- [ ] Alertas: presupuesto/coste, latencia, error rate, anomalías (spikes).
- [ ] Alertas diferenciadas por entorno (PROD más estricto).

---

## ⚠️ Riesgos / hallazgos típicos (a vigilar)

- **Mock data** en UI prolongado: deriva en contratos inestables y métricas no comparables.
- **Campos agregados sin job**: métricas “muertas” que nadie confía.
- **Cardinalidad alta**: métricas con labels demasiado granulares (coste/latencia por user/request) que rompen Prometheus/TSDB.
- **Fuga de datos**: logging de payloads, prompts o credenciales.

---

## 📌 Entregables esperados del agente (salida)

1. Inventario de señales: métricas, eventos, logs y trazas (con nombres y dimensiones).
2. Propuesta/confirmación del modelo de datos histórico (uso/costes) + estrategia de agregación.
3. Matriz de cumplimiento: seguridad/privacidad, retención, acceso, auditoría.
4. Lista de brechas + plan KISS para cerrar (prioridad alta: fuente de verdad de uso y agregación).

---

## 📌 Referencias internas

- `PROPUESTA_CAMPOS_COSTES.md`
- `ESTADO_ACTUAL_MODULO.md`
- `MODEL_APPROVAL_WORKFLOW_STATUS.md`
