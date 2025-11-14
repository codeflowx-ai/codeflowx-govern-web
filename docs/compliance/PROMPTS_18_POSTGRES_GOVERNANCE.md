# PROMPTS_18 - POSTGRESQL GOVERNANCE (Eventos + Webhooks + Resultados)
## EU AI Act Compliance · Persistencia y extensiones (TimescaleDB, pgcrypto, pg_trgm)

**Equipo:** Data Engineering / DBA  
**Fecha:** 18 de noviembre de 2025  
**Objetivo:** Crear las tablas y funciones PostgreSQL necesarias para registrar eventos de gobierno, webhooks y resultados de evaluación aprovechando las extensiones instaladas.

---

## 🧱 Alcance
- Tablas `GOVGOVERNANCEEVENTS`, `GOVGOVERNANCERESULTS`, `GOVWEBHOOKSUBSCRIPTIONS`, `GOVWEBHOOKDELIVERIES` y funciones asociadas.
- Uso de extensiones: `uuid-ossp`, `pgcrypto`, `timescaledb`, `pg_trgm`, `jsonb_path_ops`.
- Triggers para hash de prompts y cifrado de secretos.
- Políticas de retención alineadas Art. 15, Art. 71 EU AI Act.

---

## 📋 Prompt 18.1 · Tabla `GOVGOVERNANCEEVENTS`
```
Crear tabla para almacenar eventos ingeridos por la API.
- Campos principales: idx, iduuid, idxproject, traceId, source, prompt, promtHash, metadata, status, timestamps.
- Índices: traceId, status, source+type, gin jsonb, trigram para prompt.
- Trigger BEFORE INSERT/UPDATE que calcula `govprompthash` (sha256 prompt+traceId) y actualiza `updatedAt`.
- Convertir a hypertable TimescaleDB (columna createdAt).
- Políticas: compresión 30 días, retención 6 años.
```

### Checklist
- [ ] Script SQL ejecutado en entorno dev.  
- [ ] Test inserción → hash generado automáticamente.  
- [ ] Explain analyze búsqueda prompt usa índice trigram.

---

## 📋 Prompt 18.2 · Tabla `GOVGOVERNANCERESULTS`
```
Registrar resultados por etapa (LLM_EVAL, BIAS_CHECK, etc.).
- Columnas: idx, iduuid, idxgovernanceevent, stage, score, severity, metrics jsonb, explanations jsonb, recommendations jsonb.
- Foreign key a `GOVGOVERNANCEEVENTS`.
- Índices: por event, stage/severity, gin jsonb.
- Vista materializada opcional `v_governance_results_summary` (agrupa por project/stage) → refresco diario.
```

### Checklist
- [ ] Foreign key ON DELETE CASCADE.  
- [ ] Materialized view + job de refresco (cron).  
- [ ] Consultas de reporting validadas.

---

## 📋 Prompt 18.3 · Tabla `GOVWEBHOOKSUBSCRIPTIONS`
```
Tabla para webhooks registrados.
- Campos: idx, iduuid, idxproject, name, url, events jsonb, secret (bytea), active, created/updated, createdBy.
- Trigger BEFORE INSERT/UPDATE que cifra `gwssecret` usando `pgp_sym_encrypt` con clave `codeflowx.pgcrypto.key`.
- Índices: project+active, gin eventos.
- Constraint: url única por proyecto (evitar duplicados exactos).
```

### Checklist
- [ ] Clave pgcrypto configurada.  
- [ ] Test cifrado (no texto plano).  
- [ ] Consulta por evento usa índice JSONB.

---

## 📋 Prompt 18.4 · Tabla `GOVWEBHOOKDELIVERIES`
```
Almacena cada entrega de webhook.
- Campos: idx, iduuid, idxwebhooksubscription, eventUuid, payload jsonb, status (enum), attempts, nextRetryAt, lastError, createdAt.
- Índices: status+attempts, nextRetryAt (parcial), idxwebhook.
- Convertir a hypertable (createdAt) con retención 18 meses.
- Trigger AFTER UPDATE que registra auditoría cuando status = 'FAILED'.
```

### Checklist
- [ ] Política retención 18 meses + compresión 30 días.  
- [ ] Auditoría insertada en `cor_auditlog`.  
- [ ] Query reintentos usa índice parcial.

---

## 📋 Prompt 18.5 · Scripts de Migración y Versionado
```
- Crear scripts Liquibase (XML) o Flyway (SQL) en repositorio `codeflowx-govern-backend`.
- Incluir verificación de extensiones (`CREATE EXTENSION IF NOT EXISTS`).
- Añadir pruebas automatizadas (Testcontainers) para validar migraciones.
- Documentar rollback (DROP TABLE ... CASCADE) + recreación vistas.
```

### Checklist
- [ ] Pipelines CI ejecutan migraciones en base temporal.  
- [ ] Documentación en `docs/db/README.md` actualizada.  
- [ ] Variables (clave pgcrypto) definidas en `docker-compose` y Helm chart.

---

## 🔁 Dependencias
- PROMPTS_15 y 16 → usan estas tablas vía DAO.  
- PROMPTS_14 → asegura extensiones activas.  
- PROMPTS_21 (cuando exista) podría ampliar reporting.

**Estado inicial:** Pendiente.  
**Duración estimada:** 2 días (1 data engineer).
