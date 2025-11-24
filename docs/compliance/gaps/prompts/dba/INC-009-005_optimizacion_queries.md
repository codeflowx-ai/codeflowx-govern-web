# PROMPT: INC-009-005 - Optimización de Queries de Telemetría
## Monitorización en Tiempo Real - EU AI Act Art. 12

**Incidencia:** INC-009-005  
**Prioridad:** 🟢 BAJA  
**Artículo:** Art. 12  
**Estado:** 🔴 PENDIENTE

---

## DESCRIPCIÓN

Optimizar queries de telemetría creando índices GIN adicionales para campos JSONB frecuentes y continuous aggregates por hora para análisis más granulares.

---

## REQUISITOS

### 1. Índices GIN para Campos JSONB Frecuentes

**Archivo:** `nocode.service.entitys/src/main/resources/sql/aiotelemetry_optimization_indexes.sql`

```sql
-- Índice para búsqueda por severity en payload
CREATE INDEX IF NOT EXISTS idx_tel_payload_severity_gin 
ON AIOTELEMETRY 
USING GIN ((TELPAYLOAD->>'severity'))
WHERE TELPAYLOAD->>'severity' IS NOT NULL;

-- Índice para búsqueda por event_type en payload
CREATE INDEX IF NOT EXISTS idx_tel_payload_event_type_gin 
ON AIOTELEMETRY 
USING GIN ((TELPAYLOAD->>'event_type'))
WHERE TELPAYLOAD->>'event_type' IS NOT NULL;

-- Índice para búsqueda por interaction_id en payload
CREATE INDEX IF NOT EXISTS idx_tel_payload_interaction_id_gin 
ON AIOTELEMETRY 
USING GIN ((TELPAYLOAD->>'interaction_id'))
WHERE TELPAYLOAD->>'interaction_id' IS NOT NULL;

-- Índice para búsqueda por source_tool en payload
CREATE INDEX IF NOT EXISTS idx_tel_payload_source_tool_gin 
ON AIOTELEMETRY 
USING GIN ((TELPAYLOAD->>'source_tool'))
WHERE TELPAYLOAD->>'source_tool' IS NOT NULL;

-- Índice compuesto para búsqueda por severity y event_type
CREATE INDEX IF NOT EXISTS idx_tel_payload_severity_event_type_gin 
ON AIOTELEMETRY 
USING GIN ((TELPAYLOAD->>'severity'), (TELPAYLOAD->>'event_type'))
WHERE TELPAYLOAD->>'severity' IS NOT NULL 
  AND TELPAYLOAD->>'event_type' IS NOT NULL;
```

### 2. Continuous Aggregate por Hora

**Archivo:** `nocode.service.entitys/src/main/resources/sql/aiotelemetry_hourly_aggregate.sql`

```sql
-- Vista materializada para estadísticas por hora
CREATE MATERIALIZED VIEW IF NOT EXISTS telemetry_hourly_stats
WITH (timescaledb.continuous) AS
SELECT 
    time_bucket('1 hour', TELTIMESTAMP) AS hour,
    TELCOMPONENTUUID,
    TELAGENTEXTERNALID,
    TELEVENTTYPE,
    COUNT(*) AS event_count,
    AVG((TELMETRICS->>'latency_ms')::numeric) AS avg_latency_ms,
    PERCENTILE_CONT(0.95) WITHIN GROUP (ORDER BY (TELMETRICS->>'latency_ms')::numeric) AS p95_latency_ms,
    PERCENTILE_CONT(0.99) WITHIN GROUP (ORDER BY (TELMETRICS->>'latency_ms')::numeric) AS p99_latency_ms,
    SUM((TELMETRICS->>'tokens_used')::numeric) AS total_tokens,
    SUM((TELMETRICS->>'cost_usd')::numeric) AS total_cost_usd,
    COUNT(*) FILTER (WHERE TELBIASCHECKED = true) AS bias_checked_count,
    COUNT(*) FILTER (WHERE TELPIIDETECTED = true) AS pii_detected_count,
    COUNT(*) FILTER (WHERE TELSECRETDETECTED = true) AS secret_detected_count,
    COUNT(*) FILTER (WHERE TELPROCESSINSTANCEID IS NOT NULL) AS bpmn_triggered_count,
    COUNT(*) FILTER (WHERE TELSEVERITY = 'CRITICAL') AS critical_count,
    COUNT(*) FILTER (WHERE TELSEVERITY = 'WARNING') AS warning_count
FROM AIOTELEMETRY
GROUP BY hour, TELCOMPONENTUUID, TELAGENTEXTERNALID, TELEVENTTYPE;

-- Refresh automático cada 15 minutos
SELECT add_continuous_aggregate_policy('telemetry_hourly_stats',
    start_offset => INTERVAL '3 days',
    end_offset => INTERVAL '15 minutes',
    schedule_interval => INTERVAL '15 minutes');

-- Índice en la vista materializada
CREATE INDEX IF NOT EXISTS idx_telemetry_hourly_stats_hour 
ON telemetry_hourly_stats(hour DESC, TELCOMPONENTUUID);

CREATE INDEX IF NOT EXISTS idx_telemetry_hourly_stats_component 
ON telemetry_hourly_stats(TELCOMPONENTUUID, hour DESC);
```

### 3. Cursor-Based Pagination

**Archivo:** `codeflowx-aios-telemetry-worker/src/main/java/com/codeflowx/aios/telemetry/worker/repository/AioTelemetryRepository.java`

```java
// Método para cursor-based pagination (más eficiente que offset)
List<AioTelemetry> findByComponentUuidAndIdxtelemetryGreaterThan(
    String componentUuid, 
    Long lastId,
    Pageable pageable
);

// Método para obtener último mensaje procesado
Optional<AioTelemetry> findTopByOrderByTeltimestampDesc();

// Método para contar eventos después de timestamp
long countByTeltimestampAfter(Timestamp timestamp);
```

---

## QUERIES OPTIMIZADAS

### Query 1: Búsqueda por Severity y Event Type

**Antes (lento):**
```sql
SELECT * FROM AIOTELEMETRY
WHERE TELPAYLOAD->>'severity' = 'CRITICAL'
  AND TELPAYLOAD->>'event_type' = 'CODE_GENERATION'
ORDER BY TELTIMESTAMP DESC
LIMIT 100;
```

**Después (rápido con índice GIN):**
```sql
-- Usa índice idx_tel_payload_severity_event_type_gin
SELECT * FROM AIOTELEMETRY
WHERE TELPAYLOAD->>'severity' = 'CRITICAL'
  AND TELPAYLOAD->>'event_type' = 'CODE_GENERATION'
ORDER BY TELTIMESTAMP DESC
LIMIT 100;
```

### Query 2: Estadísticas por Hora

**Antes (lento, escanea toda la tabla):**
```sql
SELECT 
    date_trunc('hour', TELTIMESTAMP) AS hour,
    COUNT(*) AS event_count,
    AVG((TELMETRICS->>'latency_ms')::numeric) AS avg_latency
FROM AIOTELEMETRY
WHERE TELTIMESTAMP >= NOW() - INTERVAL '7 days'
GROUP BY hour
ORDER BY hour DESC;
```

**Después (rápido, usa continuous aggregate):**
```sql
-- Usa vista materializada telemetry_hourly_stats
SELECT 
    hour,
    SUM(event_count) AS event_count,
    AVG(avg_latency_ms) AS avg_latency
FROM telemetry_hourly_stats
WHERE hour >= NOW() - INTERVAL '7 days'
GROUP BY hour
ORDER BY hour DESC;
```

### Query 3: Paginación Eficiente

**Antes (lento con OFFSET grande):**
```java
Page<AioTelemetry> page = repository.findByComponentUuid(
    componentUuid, 
    PageRequest.of(pageNumber, pageSize)  // OFFSET crece con pageNumber
);
```

**Después (rápido con cursor):**
```java
List<AioTelemetry> events = repository.findByComponentUuidAndIdxtelemetryGreaterThan(
    componentUuid,
    lastId,  // Cursor
    PageRequest.of(0, pageSize)
);
```

---

## MÉTRICAS DE MEJORA

### Mejoras Esperadas

- **Query por severity/event_type:** Reducción 80-90% tiempo (de 2-5s a 100-300ms)
- **Query estadísticas por hora:** Reducción 95% tiempo (de 10-30s a 200-500ms)
- **Paginación con cursor:** Reducción 70-80% tiempo (de 1-3s a 200-500ms)

---

## EJECUCIÓN

### Script de Aplicación

```bash
# 1. Aplicar índices GIN
psql -U postgres -d codeflowx_telemetry -f aiotelemetry_optimization_indexes.sql

# 2. Crear continuous aggregate
psql -U postgres -d codeflowx_telemetry -f aiotelemetry_hourly_aggregate.sql

# 3. Verificar índices creados
psql -U postgres -d codeflowx_telemetry -c "\d+ AIOTELEMETRY"

# 4. Verificar continuous aggregate
psql -U postgres -d codeflowx_telemetry -c "\d+ telemetry_hourly_stats"
```

---

## MONITOREO

### Verificar Uso de Índices

```sql
-- Verificar si se usan los índices GIN
EXPLAIN ANALYZE
SELECT * FROM AIOTELEMETRY
WHERE TELPAYLOAD->>'severity' = 'CRITICAL'
  AND TELPAYLOAD->>'event_type' = 'CODE_GENERATION'
ORDER BY TELTIMESTAMP DESC
LIMIT 100;

-- Debe mostrar: "Index Scan using idx_tel_payload_severity_event_type_gin"
```

### Verificar Continuous Aggregate

```sql
-- Verificar última actualización
SELECT * FROM timescaledb_information.continuous_aggregates
WHERE view_name = 'telemetry_hourly_stats';

-- Verificar datos en la vista
SELECT COUNT(*) FROM telemetry_hourly_stats;
```

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_009_MONITORIZACION_TIEMPO_REAL.md#inc-009-005`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_009_MONITORIZACION_TIEMPO_REAL.md`

---

**Estado:** 🔴 PENDIENTE  
**Esfuerzo Estimado:** 2-3 días  
**Responsable:** DBA Team + Backend Team

