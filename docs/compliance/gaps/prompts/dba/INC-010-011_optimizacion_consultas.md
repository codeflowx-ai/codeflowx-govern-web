# PROMPT: INC-010-011 - Optimización de Consultas con Vistas Materializadas

**Incidencia:** INC-010-011  
**Prioridad:** 🟡 ALTA  
**Artículo EU AI Act:** Art. 72  
**Esfuerzo Estimado:** 2 días  
**Tipo:** DBA - PostgreSQL + TimescaleDB  
**Referencia:** GAP-017

---

## CONTEXTO

Consultas de histórico pueden ser lentas sin optimización. Se requiere crear vistas materializadas con TimescaleDB, continuous aggregates para métricas, índices estratégicos y refresh policies según Art. 72.

**Estado Actual:**
- ✅ Tablas `MONMONITORINGMETRICS` y `MONMONITORINGALERTS` creadas
- ❌ No hay vistas materializadas
- ❌ No hay continuous aggregates
- ❌ Consultas pueden ser lentas con grandes volúmenes de datos

---

## REQUISITOS

1. Crear vistas materializadas con TimescaleDB
2. Continuous aggregates para métricas
3. Índices estratégicos
4. Refresh policies automáticas
5. Optimización de consultas de histórico

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Convertir Tablas a Hypertables (TimescaleDB)

```sql
-- Convertir MONMONITORINGMETRICS a hypertable
SELECT create_hypertable('MONMONITORINGMETRICS', 'MONCREATEDAT',
    chunk_time_interval => INTERVAL '1 day');

-- Convertir MONMONITORINGALERTS a hypertable
SELECT create_hypertable('MONMONITORINGALERTS', 'MONTRIGGEREDAT',
    chunk_time_interval => INTERVAL '1 day');
```

### 2. Crear Continuous Aggregates para Métricas

```sql
-- Continuous aggregate para métricas diarias
CREATE MATERIALIZED VIEW mv_pmm_metrics_daily
WITH (timescaledb.continuous) AS
SELECT 
    time_bucket('1 day', MONCREATEDAT) AS bucket,
    IDXPROJECT,
    IDXMODEL,
    MONMETRICNAME,
    AVG(MONMETRICVALUE) AS avg_value,
    MIN(MONMETRICVALUE) AS min_value,
    MAX(MONMETRICVALUE) AS max_value,
    COUNT(*) AS count,
    STDDEV(MONMETRICVALUE) AS stddev_value
FROM MONMONITORINGMETRICS
GROUP BY bucket, IDXPROJECT, IDXMODEL, MONMETRICNAME;

-- Agregar refresh policy (actualizar cada hora)
SELECT add_continuous_aggregate_policy('mv_pmm_metrics_daily',
    start_offset => INTERVAL '3 days',
    end_offset => INTERVAL '1 hour',
    schedule_interval => INTERVAL '1 hour');

-- Continuous aggregate para métricas semanales
CREATE MATERIALIZED VIEW mv_pmm_metrics_weekly
WITH (timescaledb.continuous) AS
SELECT 
    time_bucket('1 week', MONCREATEDAT) AS bucket,
    IDXPROJECT,
    IDXMODEL,
    MONMETRICNAME,
    AVG(MONMETRICVALUE) AS avg_value,
    MIN(MONMETRICVALUE) AS min_value,
    MAX(MONMETRICVALUE) AS max_value,
    COUNT(*) AS count,
    STDDEV(MONMETRICVALUE) AS stddev_value
FROM MONMONITORINGMETRICS
GROUP BY bucket, IDXPROJECT, IDXMODEL, MONMETRICNAME;

-- Agregar refresh policy (actualizar diariamente)
SELECT add_continuous_aggregate_policy('mv_pmm_metrics_weekly',
    start_offset => INTERVAL '4 weeks',
    end_offset => INTERVAL '1 day',
    schedule_interval => INTERVAL '1 day');

-- Continuous aggregate para métricas mensuales
CREATE MATERIALIZED VIEW mv_pmm_metrics_monthly
WITH (timescaledb.continuous) AS
SELECT 
    time_bucket('1 month', MONCREATEDAT) AS bucket,
    IDXPROJECT,
    IDXMODEL,
    MONMETRICNAME,
    AVG(MONMETRICVALUE) AS avg_value,
    MIN(MONMETRICVALUE) AS min_value,
    MAX(MONMETRICVALUE) AS max_value,
    COUNT(*) AS count,
    STDDEV(MONMETRICVALUE) AS stddev_value
FROM MONMONITORINGMETRICS
GROUP BY bucket, IDXPROJECT, IDXMODEL, MONMETRICNAME;

-- Agregar refresh policy (actualizar semanalmente)
SELECT add_continuous_aggregate_policy('mv_pmm_metrics_monthly',
    start_offset => INTERVAL '6 months',
    end_offset => INTERVAL '1 week',
    schedule_interval => INTERVAL '1 week');
```

### 3. Crear Continuous Aggregates para Alertas

```sql
-- Continuous aggregate para alertas diarias
CREATE MATERIALIZED VIEW mv_pmm_alerts_daily
WITH (timescaledb.continuous) AS
SELECT 
    time_bucket('1 day', MONTRIGGEREDAT) AS bucket,
    IDXPROJECT,
    IDXMODEL,
    MONALERTTYPE,
    MONSEVERITY,
    COUNT(*) AS alert_count,
    COUNT(*) FILTER (WHERE 'RESOLVED' = ANY(MONSTATUS)) AS resolved_count,
    COUNT(*) FILTER (WHERE 'ACTIVE' = ANY(MONSTATUS)) AS active_count
FROM MONMONITORINGALERTS
GROUP BY bucket, IDXPROJECT, IDXMODEL, MONALERTTYPE, MONSEVERITY;

-- Agregar refresh policy
SELECT add_continuous_aggregate_policy('mv_pmm_alerts_daily',
    start_offset => INTERVAL '3 days',
    end_offset => INTERVAL '1 hour',
    schedule_interval => INTERVAL '1 hour');
```

### 4. Crear Índices Estratégicos

```sql
-- Índices en tablas base
CREATE INDEX idx_mon_metrics_project_date ON MONMONITORINGMETRICS(IDXPROJECT, MONCREATEDAT DESC);
CREATE INDEX idx_mon_metrics_model_date ON MONMONITORINGMETRICS(IDXMODEL, MONCREATEDAT DESC) WHERE IDXMODEL IS NOT NULL;
CREATE INDEX idx_mon_metrics_name_date ON MONMONITORINGMETRICS(MONMETRICNAME, MONCREATEDAT DESC);

CREATE INDEX idx_mon_alerts_project_date ON MONMONITORINGALERTS(IDXPROJECT, MONTRIGGEREDAT DESC);
CREATE INDEX idx_mon_alerts_model_date ON MONMONITORINGALERTS(IDXMODEL, MONTRIGGEREDAT DESC) WHERE IDXMODEL IS NOT NULL;
CREATE INDEX idx_mon_alerts_severity_date ON MONMONITORINGALERTS(MONSEVERITY, MONTRIGGEREDAT DESC);

-- Índices en continuous aggregates
CREATE INDEX idx_mv_metrics_daily_project_bucket ON mv_pmm_metrics_daily(IDXPROJECT, bucket DESC);
CREATE INDEX idx_mv_metrics_daily_model_bucket ON mv_pmm_metrics_daily(IDXMODEL, bucket DESC) WHERE IDXMODEL IS NOT NULL;
CREATE INDEX idx_mv_metrics_daily_name_bucket ON mv_pmm_metrics_daily(MONMETRICNAME, bucket DESC);
```

### 5. Crear Vistas Materializadas para Consultas Comunes

```sql
-- Vista materializada para dashboard (métricas recientes)
CREATE MATERIALIZED VIEW mv_pmm_dashboard_metrics AS
SELECT 
    IDXPROJECT,
    IDXMODEL,
    MONMETRICNAME,
    AVG(MONMETRICVALUE) AS avg_value,
    MAX(MONMETRICVALUE) AS max_value,
    MIN(MONMETRICVALUE) AS min_value,
    COUNT(*) AS count,
    MAX(MONCREATEDAT) AS last_updated
FROM MONMONITORINGMETRICS
WHERE MONCREATEDAT >= NOW() - INTERVAL '24 hours'
GROUP BY IDXPROJECT, IDXMODEL, MONMETRICNAME;

-- Índice en vista materializada
CREATE UNIQUE INDEX idx_mv_dashboard_metrics_unique 
    ON mv_pmm_dashboard_metrics(IDXPROJECT, IDXMODEL, MONMETRICNAME);

-- Refresh automático cada 5 minutos
CREATE UNIQUE INDEX ON mv_pmm_dashboard_metrics(IDXPROJECT, IDXMODEL, MONMETRICNAME);

-- Función para refresh manual
CREATE OR REPLACE FUNCTION refresh_pmm_dashboard_metrics()
RETURNS void AS $$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY mv_pmm_dashboard_metrics;
END;
$$ LANGUAGE plpgsql;

-- Programar refresh automático (usando pg_cron si está disponible)
-- SELECT cron.schedule('refresh-pmm-dashboard', '*/5 * * * *', 
--     'SELECT refresh_pmm_dashboard_metrics();');
```

### 6. Crear Funciones de Consulta Optimizadas

```sql
-- Función para obtener métricas históricas optimizadas
CREATE OR REPLACE FUNCTION get_pmm_metrics_history(
    p_project_id BIGINT,
    p_model_id BIGINT,
    p_metric_name VARCHAR,
    p_start_date TIMESTAMP,
    p_end_date TIMESTAMP,
    p_bucket_size INTERVAL DEFAULT '1 day'
)
RETURNS TABLE (
    bucket TIMESTAMP,
    avg_value NUMERIC,
    min_value NUMERIC,
    max_value NUMERIC,
    count BIGINT
) AS $$
BEGIN
    -- Usar continuous aggregate si el rango es grande
    IF p_end_date - p_start_date > INTERVAL '30 days' THEN
        RETURN QUERY
        SELECT 
            mv.bucket,
            mv.avg_value,
            mv.min_value,
            mv.max_value,
            mv.count
        FROM mv_pmm_metrics_daily mv
        WHERE (p_project_id IS NULL OR mv.IDXPROJECT = p_project_id)
            AND (p_model_id IS NULL OR mv.IDXMODEL = p_model_id)
            AND (p_metric_name IS NULL OR mv.MONMETRICNAME = p_metric_name)
            AND mv.bucket >= p_start_date
            AND mv.bucket <= p_end_date
        ORDER BY mv.bucket DESC;
    ELSE
        -- Usar tabla base para rangos pequeños
        RETURN QUERY
        SELECT 
            time_bucket(p_bucket_size, m.MONCREATEDAT) AS bucket,
            AVG(m.MONMETRICVALUE) AS avg_value,
            MIN(m.MONMETRICVALUE) AS min_value,
            MAX(m.MONMETRICVALUE) AS max_value,
            COUNT(*) AS count
        FROM MONMONITORINGMETRICS m
        WHERE (p_project_id IS NULL OR m.IDXPROJECT = p_project_id)
            AND (p_model_id IS NULL OR m.IDXMODEL = p_model_id)
            AND (p_metric_name IS NULL OR m.MONMETRICNAME = p_metric_name)
            AND m.MONCREATEDAT >= p_start_date
            AND m.MONCREATEDAT <= p_end_date
        GROUP BY bucket
        ORDER BY bucket DESC;
    END IF;
END;
$$ LANGUAGE plpgsql;
```

### 7. Crear Vistas para Consultas Rápidas

```sql
-- Vista para alertas activas (sin materializar, se actualiza en tiempo real)
CREATE OR REPLACE VIEW vw_pmm_active_alerts AS
SELECT 
    a.IDXMONALERT,
    a.IDXPROJECT,
    a.IDXMODEL,
    a.MONALERTTYPE,
    a.MONSEVERITY,
    a.MONTRIGGEREDAT,
    a.MONALERTDATA,
    EXTRACT(EPOCH FROM (NOW() - a.MONTRIGGEREDAT)) / 3600 AS hours_since_triggered
FROM MONMONITORINGALERTS a
WHERE 'ACTIVE' = ANY(a.MONSTATUS)
ORDER BY a.MONTRIGGEREDAT DESC;

-- Vista para resumen de métricas por proyecto
CREATE OR REPLACE VIEW vw_pmm_project_summary AS
SELECT 
    p.IDXPROJECT,
    COUNT(DISTINCT m.IDXMODEL) AS model_count,
    COUNT(DISTINCT m.MONMETRICNAME) AS metric_types,
    MAX(m.MONCREATEDAT) AS last_metric_date,
    COUNT(DISTINCT a.IDXMONALERT) FILTER (WHERE 'ACTIVE' = ANY(a.MONSTATUS)) AS active_alerts
FROM PRJPROJECTS p
LEFT JOIN MONMONITORINGMETRICS m ON p.IDXPROJECT = m.IDXPROJECT
LEFT JOIN MONMONITORINGALERTS a ON p.IDXPROJECT = a.IDXPROJECT
GROUP BY p.IDXPROJECT;
```

---

## VALIDACIONES

1. ✅ Hypertables creadas en TimescaleDB
2. ✅ Continuous aggregates para métricas diarias, semanales, mensuales
3. ✅ Continuous aggregates para alertas
4. ✅ Índices estratégicos creados
5. ✅ Refresh policies configuradas
6. ✅ Funciones de consulta optimizadas implementadas
7. ✅ Vistas para consultas rápidas creadas

---

## NOTAS

- TimescaleDB debe estar instalado y habilitado
- Continuous aggregates mejoran significativamente el performance
- Refresh policies deben configurarse según necesidades
- Considerar retention policies para datos antiguos
- Monitorear tamaño de continuous aggregates

