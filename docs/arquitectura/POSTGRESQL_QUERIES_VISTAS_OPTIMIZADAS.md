# PostgreSQL - Queries y Vistas Optimizadas con Extensiones

## 📋 Índice

1. [Vistas Materializadas TimescaleDB](#vistas-materializadas-timescaledb)
2. [Queries Linaje Modelos (ltree)](#queries-linaje-modelos-ltree)
3. [Búsqueda Semántica (pgvector)](#búsqueda-semántica-pgvector)
4. [Logs Inmutables (pgcrypto)](#logs-inmutables-pgcrypto)
5. [Búsqueda Difusa (pg_trgm)](#búsqueda-difusa-pg_trgm)
6. [Reportes Compliance](#reportes-compliance)

---

## Vistas Materializadas TimescaleDB

### Vista: Métricas Diarias por Modelo

```sql
-- Continuous aggregate: actualización automática incremental
CREATE MATERIALIZED VIEW mv_model_daily_metrics
WITH (timescaledb.continuous) AS
SELECT
    time_bucket('1 day', em.createdat) AS day,
    m.idxmodel,
    m.modelname,
    m.modeltype,
    
    -- Accuracy
    AVG(em.accuracy) AS avg_accuracy,
    MIN(em.accuracy) AS min_accuracy,
    MAX(em.accuracy) AS max_accuracy,
    STDDEV(em.accuracy) AS stddev_accuracy,
    
    -- Evaluaciones
    COUNT(*) AS eval_count,
    COUNT(DISTINCT em.idxevaluation) AS unique_evaluations,
    
    -- Compliance
    BOOL_AND(em.accuracy >= 0.85) AS meets_accuracy_threshold,
    
    -- Timestamp
    MAX(em.createdat) AS last_evaluation
FROM eval_modelmetrics em
JOIN cor_model m ON em.idxmodel = m.idxmodel
GROUP BY day, m.idxmodel, m.modelname, m.modeltype
WITH NO DATA;

-- Refresh policy: actualizar cada hora
SELECT add_continuous_aggregate_policy('mv_model_daily_metrics',
    start_offset => INTERVAL '1 month',
    end_offset => INTERVAL '1 hour',
    schedule_interval => INTERVAL '1 hour'
);

-- Refresh inicial
CALL refresh_continuous_aggregate('mv_model_daily_metrics', NULL, NULL);

-- Query rápida (usa vista materializada)
SELECT * FROM mv_model_daily_metrics
WHERE day >= NOW() - INTERVAL '30 days'
  AND modelname = 'llama3_70b'
ORDER BY day DESC;
```

### Vista: Alertas Drift Últimos 7 Días

```sql
CREATE MATERIALIZED VIEW mv_drift_alerts_7d
WITH (timescaledb.continuous) AS
SELECT
    time_bucket('1 hour', createdat) AS hour,
    idxmodel,
    idxdeployment,
    
    -- Drift metrics
    AVG(driftvalue) AS avg_drift,
    MAX(driftvalue) AS max_drift,
    COUNT(*) AS drift_count,
    
    -- Alertas
    SUM(CASE WHEN driftvalue > 0.15 THEN 1 ELSE 0 END) AS critical_alerts,
    SUM(CASE WHEN driftvalue > 0.10 THEN 1 ELSE 0 END) AS warning_alerts,
    
    -- Estado
    MAX(createdat) AS last_check
FROM eval_driftmetrics
GROUP BY hour, idxmodel, idxdeployment
WITH NO DATA;

-- Policy
SELECT add_continuous_aggregate_policy('mv_drift_alerts_7d',
    start_offset => INTERVAL '7 days',
    end_offset => INTERVAL '1 hour',
    schedule_interval => INTERVAL '30 minutes'
);

-- Query alertas críticas
SELECT 
    m.modelname,
    d.deploymentname,
    da.hour,
    da.critical_alerts,
    da.max_drift
FROM mv_drift_alerts_7d da
JOIN cor_model m ON da.idxmodel = m.idxmodel
JOIN cor_deployment d ON da.idxdeployment = d.idxdeployment
WHERE da.critical_alerts > 0
  AND da.hour >= NOW() - INTERVAL '24 hours'
ORDER BY da.max_drift DESC;
```

### Vista: Resumen Bias Semanal

```sql
CREATE MATERIALIZED VIEW mv_bias_weekly_summary
WITH (timescaledb.continuous) AS
SELECT
    time_bucket('1 week', createdat) AS week,
    idxmodel,
    biasmetric,
    protectedattribute,
    
    -- Agregaciones
    AVG(biasvalue) AS avg_bias,
    MAX(biasvalue) AS max_bias,
    MIN(biasvalue) AS min_bias,
    COUNT(*) AS check_count,
    
    -- Compliance (threshold < 0.1)
    AVG(CASE WHEN biasvalue < 0.1 THEN 1.0 ELSE 0.0 END) AS compliance_rate,
    
    -- Tendencia
    regr_slope(biasvalue, extract(epoch from createdat)) AS trend_slope
FROM eval_biasmetrics
GROUP BY week, idxmodel, biasmetric, protectedattribute
WITH NO DATA;

-- Policy
SELECT add_continuous_aggregate_policy('mv_bias_weekly_summary',
    start_offset => INTERVAL '12 weeks',
    end_offset => INTERVAL '1 day',
    schedule_interval => INTERVAL '1 day'
);
```

---

## Queries Linaje Modelos (ltree)

### Query: Árbol Completo de un Modelo

```sql
-- Vista: Árbol completo de linaje
CREATE OR REPLACE VIEW v_model_lineage_tree AS
SELECT
    l.idxlineage,
    l.idxmodel,
    m.modelname,
    l.lineage_path,
    
    -- Nivel en el árbol (1=base, 2=finetune, 3=adapter)
    nlevel(l.lineage_path) AS depth,
    
    -- Path legible
    ltree2text(l.lineage_path) AS path_text,
    
    -- Padre
    subpath(l.lineage_path, 0, -1) AS parent_path,
    
    -- Tipo y metadata
    l.modification_type,
    l.substantial,
    l.modification_description,
    l.modification_date,
    
    -- Info del modelo
    m.modeltype,
    m.riskclassification,
    m.gpaiclassification,
    
    -- Padres
    pm.idxmodel AS parent_idxmodel,
    pm.modelname AS parent_modelname
FROM cor_model_lineage l
JOIN cor_model m ON l.idxmodel = m.idxmodel
LEFT JOIN cor_model_lineage pl ON subpath(l.lineage_path, 0, -1) = pl.lineage_path
LEFT JOIN cor_model pm ON pl.idxmodel = pm.idxmodel;

-- Usar la vista: todos los descendientes de llama3_70b
SELECT 
    REPEAT('  ', depth - 1) || '└─ ' || modelname AS tree,
    modification_type,
    substantial,
    modification_date::DATE
FROM v_model_lineage_tree
WHERE lineage_path <@ 'llama3_70b'
ORDER BY lineage_path;

-- Output:
-- llama3_70b                        BASE      true   2024-01-15
--   └─ finetuned_medical            FINETUNE  true   2024-02-20
--     └─ adapter_spanish            ADAPTER   false  2024-03-10
--     └─ adapter_bias_fix           ADAPTER   true   2024-03-15
--   └─ finetuned_legal              FINETUNE  true   2024-02-25
--     └─ merged_medical_legal       MERGE     true   2024-04-01
```

### Query: Path Completo hasta Base

```sql
-- Función: Obtener path completo de ancestros
CREATE OR REPLACE FUNCTION get_model_ancestry(model_id BIGINT)
RETURNS TABLE (
    idxmodel BIGINT,
    modelname VARCHAR,
    depth INT,
    modification_type VARCHAR,
    substantial BOOLEAN
) AS $$
    SELECT 
        l.idxmodel,
        m.modelname,
        nlevel(l.lineage_path) AS depth,
        l.modification_type,
        l.substantial
    FROM cor_model_lineage l
    JOIN cor_model m ON l.idxmodel = m.idxmodel
    WHERE l.lineage_path @> (
        SELECT lineage_path 
        FROM cor_model_lineage 
        WHERE idxmodel = model_id
    )
    ORDER BY nlevel(l.lineage_path);
$$ LANGUAGE SQL;

-- Usar: obtener ancestros de un adapter
SELECT * FROM get_model_ancestry(456);

-- Output:
-- idxmodel | modelname              | depth | modification_type | substantial
-- ---------+------------------------+-------+-------------------+------------
-- 123      | llama3_70b             | 1     | BASE              | true
-- 234      | finetuned_medical      | 2     | FINETUNE          | true
-- 456      | adapter_spanish        | 3     | ADAPTER           | false
```

### Query: Impact Analysis (¿Qué afecta este cambio?)

```sql
-- Vista: Impacto de modificar un modelo
CREATE OR REPLACE VIEW v_model_impact_analysis AS
SELECT
    base.idxmodel AS base_idxmodel,
    base.modelname AS base_modelname,
    
    -- Descendientes directos (nivel +1)
    COUNT(DISTINCT CASE WHEN nlevel(desc.lineage_path) = nlevel(base.lineage_path) + 1 
                        THEN desc.idxmodel END) AS direct_children,
    
    -- Todos los descendientes
    COUNT(DISTINCT desc.idxmodel) AS total_descendants,
    
    -- Por tipo
    COUNT(DISTINCT CASE WHEN desc.modification_type = 'FINETUNE' THEN desc.idxmodel END) AS finetune_count,
    COUNT(DISTINCT CASE WHEN desc.modification_type = 'ADAPTER' THEN desc.idxmodel END) AS adapter_count,
    COUNT(DISTINCT CASE WHEN desc.modification_type = 'MERGE' THEN desc.idxmodel END) AS merge_count,
    
    -- Modificaciones sustanciales (Art. 53)
    COUNT(DISTINCT CASE WHEN desc.substantial THEN desc.idxmodel END) AS substantial_modifications,
    
    -- En producción
    COUNT(DISTINCT d.idxdeployment) AS deployments_affected
FROM cor_model_lineage base
LEFT JOIN cor_model_lineage desc 
    ON desc.lineage_path <@ base.lineage_path 
    AND desc.idxmodel != base.idxmodel
LEFT JOIN cor_deployment d ON desc.idxmodel = d.idxmodel AND d.status = 'ACTIVE'
GROUP BY base.idxmodel, base.modelname;

-- Usar: impacto de modificar llama3_70b
SELECT * FROM v_model_impact_analysis
WHERE base_modelname = 'llama3_70b';

-- Output:
-- base_idxmodel | base_modelname | direct_children | total_descendants | substantial_modifications | deployments_affected
-- --------------+----------------+-----------------+-------------------+---------------------------+---------------------
-- 123           | llama3_70b     | 2               | 5                 | 3                         | 12
```

### Query: Detectar Modificaciones Sustanciales (Art. 53)

```sql
-- Vista: Compliance GPAI Art. 53
CREATE OR REPLACE VIEW v_gpai_substantial_modifications AS
SELECT
    l.idxlineage,
    m.modelname,
    m.gpaiclassification,
    l.lineage_path,
    l.modification_type,
    l.modification_date,
    l.modification_description,
    
    -- Art. 53: Requiere documentación adicional
    l.substantial,
    
    -- Documentación compliance
    COUNT(DISTINCT td.idxtechdoc) AS technical_docs_count,
    BOOL_AND(td.doctype IN ('MODIFICATION_REPORT', 'GPAI_ASSESSMENT')) AS has_required_docs,
    
    -- Estado compliance
    CASE 
        WHEN l.substantial AND COUNT(DISTINCT td.idxtechdoc) >= 2 THEN 'COMPLIANT'
        WHEN l.substantial THEN 'MISSING_DOCUMENTATION'
        ELSE 'NOT_APPLICABLE'
    END AS compliance_status
FROM cor_model_lineage l
JOIN cor_model m ON l.idxmodel = m.idxmodel
LEFT JOIN cor_technicaldoc td ON m.idxmodel = td.idxmodel
WHERE m.gpaiclassification IN ('GPAI_SYSTEMIC', 'GPAI_STANDARD')
  AND l.modification_type != 'BASE'
GROUP BY l.idxlineage, m.modelname, m.gpaiclassification, l.lineage_path,
         l.modification_type, l.modification_date, l.modification_description, l.substantial;

-- Query: Modelos GPAI sin documentación
SELECT 
    modelname,
    modification_type,
    modification_date,
    compliance_status
FROM v_gpai_substantial_modifications
WHERE compliance_status = 'MISSING_DOCUMENTATION'
ORDER BY modification_date DESC;
```

---

## Búsqueda Semántica (pgvector)

### Query: RAG - Retrieval Chunks Relevantes

```sql
-- Vista: Búsqueda semántica con metadata
CREATE OR REPLACE FUNCTION rag_search_chunks(
    query_embedding vector(1536),
    doc_types TEXT[] DEFAULT NULL,
    idxmodel_filter BIGINT DEFAULT NULL,
    top_k INT DEFAULT 10,
    similarity_threshold FLOAT DEFAULT 0.7
)
RETURNS TABLE (
    idxchunk BIGINT,
    chunktext TEXT,
    similarity FLOAT,
    doctype VARCHAR,
    modelname VARCHAR,
    chunk_metadata JSONB
) AS $$
    SELECT 
        c.idxchunk,
        c.chunktext,
        1 - (c.embedding <=> query_embedding) AS similarity,
        d.doctype,
        m.modelname,
        c.metadata AS chunk_metadata
    FROM rag_chunks c
    JOIN rag_documents d ON c.idxdocument = d.idxdocument
    LEFT JOIN cor_model m ON d.idxmodel = m.idxmodel
    WHERE 
        (doc_types IS NULL OR d.doctype = ANY(doc_types))
        AND (idxmodel_filter IS NULL OR m.idxmodel = idxmodel_filter)
        AND (1 - (c.embedding <=> query_embedding)) >= similarity_threshold
    ORDER BY c.embedding <=> query_embedding
    LIMIT top_k;
$$ LANGUAGE SQL;

-- Usar: buscar instrucciones de uso para un modelo
SELECT 
    chunktext,
    similarity,
    doctype
FROM rag_search_chunks(
    '[0.123, 0.456, ...]'::vector(1536),  -- Query embedding
    ARRAY['INSTRUCTIONS_FOR_USE', 'USER_MANUAL'],  -- Tipos doc
    123,  -- idxmodel
    5,    -- Top 5
    0.75  -- Min similarity 75%
);
```

### Query: Deduplicación Prompts Similares

```sql
-- Vista: Detectar prompts duplicados/similares
CREATE MATERIALIZED VIEW mv_duplicate_prompts AS
SELECT
    p1.idxprompt AS prompt1_id,
    p2.idxprompt AS prompt2_id,
    p1.prompttext AS prompt1_text,
    p2.prompttext AS prompt2_text,
    1 - (p1.embedding <=> p2.embedding) AS similarity,
    p1.createdat AS prompt1_date,
    p2.createdat AS prompt2_date
FROM gov_prompt p1
JOIN gov_prompt p2 ON p1.idxprompt < p2.idxprompt  -- Evitar duplicados
WHERE 1 - (p1.embedding <=> p2.embedding) > 0.95  -- 95% similares
ORDER BY similarity DESC;

-- Refresh periódico
CREATE INDEX idx_mv_dup_prompts_similarity ON mv_duplicate_prompts(similarity DESC);

-- Query: Prompts casi idénticos (posible duplicación)
SELECT 
    prompt1_text,
    prompt2_text,
    ROUND(similarity::NUMERIC, 4) AS similarity_pct,
    prompt1_date,
    prompt2_date
FROM mv_duplicate_prompts
WHERE similarity > 0.98
ORDER BY similarity DESC
LIMIT 20;
```

### Query: Clustering Modelos por Capacidades

```sql
-- Vista: Modelos similares (mismo embedding space)
CREATE OR REPLACE FUNCTION find_similar_models(
    target_model_id BIGINT,
    top_k INT DEFAULT 5
)
RETURNS TABLE (
    idxmodel BIGINT,
    modelname VARCHAR,
    similarity FLOAT,
    modeltype VARCHAR,
    riskclassification VARCHAR
) AS $$
    SELECT 
        m.idxmodel,
        m.modelname,
        1 - (m.capabilities_embedding <=> target.capabilities_embedding) AS similarity,
        m.modeltype,
        m.riskclassification
    FROM cor_model m
    CROSS JOIN (SELECT capabilities_embedding FROM cor_model WHERE idxmodel = target_model_id) target
    WHERE m.idxmodel != target_model_id
    ORDER BY m.capabilities_embedding <=> target.capabilities_embedding
    LIMIT top_k;
$$ LANGUAGE SQL;

-- Usar: encontrar modelos similares a llama3_70b
SELECT * FROM find_similar_models(123, 10);
```

---

## Logs Inmutables (pgcrypto)

### Tabla: Audit Logs con Hash Chain

```sql
-- Tabla con hash chain automático
CREATE TABLE cor_auditlog (
    idxauditlog BIGSERIAL PRIMARY KEY,
    iduuid UUID NOT NULL DEFAULT gen_random_uuid(),
    
    -- Datos del evento
    action VARCHAR(50) NOT NULL,
    entity VARCHAR(100) NOT NULL,
    entityid BIGINT,
    userid BIGINT,
    timestamp TIMESTAMP NOT NULL DEFAULT NOW(),
    payload JSONB,
    ip_address INET,
    user_agent TEXT,
    
    -- Hash chain (Art. 19 - inmutabilidad)
    previous_hash VARCHAR(64),
    current_hash VARCHAR(64),
    
    -- Metadata
    createdat TIMESTAMP NOT NULL DEFAULT NOW(),
    
    -- Índices
    INDEX idx_auditlog_timestamp (timestamp DESC),
    INDEX idx_auditlog_entity (entity, entityid),
    INDEX idx_auditlog_user (userid),
    INDEX idx_auditlog_hash (current_hash)
);

-- Convertir a hypertable (TimescaleDB)
SELECT create_hypertable('cor_auditlog', 'timestamp');

-- Función: Calcular hash automáticamente
CREATE OR REPLACE FUNCTION calculate_audit_hash()
RETURNS TRIGGER AS $$
DECLARE
    last_hash VARCHAR(64);
    hash_input TEXT;
BEGIN
    -- Obtener hash del último registro
    SELECT current_hash INTO last_hash
    FROM cor_auditlog
    ORDER BY idxauditlog DESC
    LIMIT 1;
    
    -- Si es el primer registro
    IF last_hash IS NULL THEN
        last_hash := 'GENESIS_BLOCK';
    END IF;
    
    NEW.previous_hash := last_hash;
    
    -- Calcular hash actual
    hash_input := COALESCE(last_hash, '') ||
                  NEW.action ||
                  NEW.entity ||
                  COALESCE(NEW.entityid::TEXT, '') ||
                  COALESCE(NEW.userid::TEXT, '') ||
                  NEW.timestamp::TEXT ||
                  COALESCE(NEW.payload::TEXT, '');
    
    NEW.current_hash := encode(
        digest(hash_input, 'sha256'),
        'hex'
    );
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger: Aplicar antes de INSERT
CREATE TRIGGER trg_audit_hash_chain
    BEFORE INSERT ON cor_auditlog
    FOR EACH ROW
    EXECUTE FUNCTION calculate_audit_hash();

-- Trigger: PREVENIR UPDATE/DELETE (inmutabilidad Art. 19)
CREATE OR REPLACE FUNCTION prevent_audit_modification()
RETURNS TRIGGER AS $$
BEGIN
    RAISE EXCEPTION 'Audit logs are immutable (EU AI Act Art. 19)';
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_prevent_audit_update
    BEFORE UPDATE ON cor_auditlog
    FOR EACH ROW
    EXECUTE FUNCTION prevent_audit_modification();

CREATE TRIGGER trg_prevent_audit_delete
    BEFORE DELETE ON cor_auditlog
    FOR EACH ROW
    EXECUTE FUNCTION prevent_audit_modification();
```

### Query: Verificar Integridad Hash Chain

```sql
-- Función: Verificar integridad completa de logs
CREATE OR REPLACE FUNCTION verify_audit_chain(
    start_id BIGINT DEFAULT NULL,
    end_id BIGINT DEFAULT NULL
)
RETURNS TABLE (
    idxauditlog BIGINT,
    is_valid BOOLEAN,
    expected_hash VARCHAR(64),
    actual_hash VARCHAR(64),
    error_message TEXT
) AS $$
    WITH audit_sequence AS (
        SELECT 
            idxauditlog,
            action,
            entity,
            entityid,
            userid,
            timestamp,
            payload,
            previous_hash,
            current_hash,
            LAG(current_hash) OVER (ORDER BY idxauditlog) AS expected_previous_hash
        FROM cor_auditlog
        WHERE (start_id IS NULL OR idxauditlog >= start_id)
          AND (end_id IS NULL OR idxauditlog <= end_id)
        ORDER BY idxauditlog
    ),
    calculated_hashes AS (
        SELECT
            idxauditlog,
            previous_hash,
            current_hash,
            expected_previous_hash,
            encode(
                digest(
                    COALESCE(expected_previous_hash, 'GENESIS_BLOCK') ||
                    action ||
                    entity ||
                    COALESCE(entityid::TEXT, '') ||
                    COALESCE(userid::TEXT, '') ||
                    timestamp::TEXT ||
                    COALESCE(payload::TEXT, ''),
                    'sha256'
                ),
                'hex'
            ) AS calculated_hash
        FROM audit_sequence
    )
    SELECT
        idxauditlog,
        (current_hash = calculated_hash AND 
         (previous_hash = expected_previous_hash OR expected_previous_hash IS NULL)) AS is_valid,
        calculated_hash AS expected_hash,
        current_hash AS actual_hash,
        CASE
            WHEN current_hash != calculated_hash THEN 'Hash mismatch - potential tampering'
            WHEN previous_hash != expected_previous_hash THEN 'Broken chain - missing records'
            ELSE NULL
        END AS error_message
    FROM calculated_hashes;
$$ LANGUAGE SQL;

-- Usar: verificar últimos 1000 logs
SELECT * FROM verify_audit_chain()
WHERE NOT is_valid
LIMIT 100;

-- Vista: Resumen integridad diaria
CREATE MATERIALIZED VIEW mv_audit_integrity_daily AS
SELECT
    DATE(timestamp) AS audit_date,
    COUNT(*) AS total_logs,
    COUNT(*) FILTER (WHERE is_valid) AS valid_logs,
    COUNT(*) FILTER (WHERE NOT is_valid) AS invalid_logs,
    ROUND(
        100.0 * COUNT(*) FILTER (WHERE is_valid) / COUNT(*),
        2
    ) AS integrity_percentage
FROM verify_audit_chain()
GROUP BY DATE(timestamp);
```

---

## Búsqueda Difusa (pg_trgm)

### Query: Búsqueda Modelos Tolerante a Typos

```sql
-- Índice trigrama
CREATE INDEX idx_model_name_trgm ON cor_model USING gin (modelname gin_trgm_ops);
CREATE INDEX idx_model_desc_trgm ON cor_model USING gin (modeldescription gin_trgm_ops);

-- Vista: Búsqueda fuzzy
CREATE OR REPLACE FUNCTION fuzzy_search_models(
    search_term TEXT,
    min_similarity FLOAT DEFAULT 0.3
)
RETURNS TABLE (
    idxmodel BIGINT,
    modelname VARCHAR,
    name_similarity FLOAT,
    desc_similarity FLOAT,
    best_match_field VARCHAR
) AS $$
    SELECT 
        idxmodel,
        modelname,
        similarity(modelname, search_term) AS name_similarity,
        similarity(modeldescription, search_term) AS desc_similarity,
        CASE 
            WHEN similarity(modelname, search_term) > similarity(modeldescription, search_term) 
            THEN 'name'
            ELSE 'description'
        END AS best_match_field
    FROM cor_model
    WHERE 
        modelname % search_term 
        OR modeldescription % search_term
    ORDER BY 
        GREATEST(
            similarity(modelname, search_term),
            similarity(modeldescription, search_term)
        ) DESC
    LIMIT 20;
$$ LANGUAGE SQL;

-- Usar: buscar "llam" (encuentra llama2, llama3, etc.)
SELECT * FROM fuzzy_search_models('llam');

-- Output:
-- idxmodel | modelname      | name_similarity | desc_similarity | best_match_field
-- ---------+----------------+-----------------+-----------------+-----------------
-- 123      | llama3_70b     | 0.666           | 0.125           | name
-- 124      | llama2_13b     | 0.666           | 0.100           | name
-- 125      | llama_guard    | 0.727           | 0.080           | name
```

### Query: Autocompletado

```sql
-- Función: Autocompletado modelos
CREATE OR REPLACE FUNCTION autocomplete_models(
    prefix TEXT,
    limit_results INT DEFAULT 10
)
RETURNS TABLE (
    modelname VARCHAR,
    match_score FLOAT
) AS $$
    SELECT 
        modelname,
        similarity(modelname, prefix) AS match_score
    FROM cor_model
    WHERE modelname % prefix
    ORDER BY similarity(modelname, prefix) DESC
    LIMIT limit_results;
$$ LANGUAGE SQL;

-- Usar: usuario escribe "gpt"
SELECT * FROM autocomplete_models('gpt');

-- Output:
-- modelname          | match_score
-- -------------------+-------------
-- gpt4_turbo         | 0.500
-- gpt-4o             | 0.428
-- gpt35_turbo        | 0.375
```

---

## Reportes Compliance

### Reporte: Art. 15 - Accuracy por Modelo (Último Mes)

```sql
CREATE OR REPLACE VIEW v_compliance_art15_accuracy AS
SELECT
    m.idxmodel,
    m.modelname,
    m.riskclassification,
    
    -- Métricas último mes
    COUNT(DISTINCT em.idxevaluation) AS evaluations_last_month,
    AVG(em.accuracy) AS avg_accuracy,
    MIN(em.accuracy) AS min_accuracy,
    MAX(em.accuracy) AS max_accuracy,
    STDDEV(em.accuracy) AS stddev_accuracy,
    
    -- Compliance threshold (ejemplo: >85%)
    CASE 
        WHEN AVG(em.accuracy) >= 0.85 THEN 'COMPLIANT'
        WHEN AVG(em.accuracy) >= 0.75 THEN 'WARNING'
        ELSE 'NON_COMPLIANT'
    END AS compliance_status,
    
    -- Última evaluación
    MAX(em.createdat) AS last_evaluation_date,
    
    -- Tendencia (regresión lineal)
    regr_slope(em.accuracy, extract(epoch from em.createdat)) * 86400 AS daily_trend
FROM cor_model m
LEFT JOIN eval_modelmetrics em ON m.idxmodel = em.idxmodel
    AND em.createdat >= NOW() - INTERVAL '30 days'
WHERE m.riskclassification IN ('HIGH_RISK', 'LIMITED_RISK')
GROUP BY m.idxmodel, m.modelname, m.riskclassification;

-- Query: Modelos con problemas de accuracy
SELECT 
    modelname,
    riskclassification,
    ROUND(avg_accuracy::NUMERIC, 4) AS avg_accuracy,
    compliance_status,
    evaluations_last_month,
    last_evaluation_date::DATE
FROM v_compliance_art15_accuracy
WHERE compliance_status != 'COMPLIANT'
ORDER BY avg_accuracy ASC;
```

### Reporte: Art. 10 - Bias Metrics Dashboard

```sql
CREATE MATERIALIZED VIEW mv_compliance_art10_bias AS
SELECT
    m.idxmodel,
    m.modelname,
    bm.protectedattribute,
    bm.biasmetric,
    
    -- Agregaciones último mes
    AVG(bm.biasvalue) AS avg_bias,
    MAX(bm.biasvalue) AS max_bias,
    COUNT(*) AS measurements,
    
    -- Compliance (threshold < 0.1)
    ROUND(
        100.0 * COUNT(*) FILTER (WHERE bm.biasvalue < 0.1) / COUNT(*),
        2
    ) AS compliance_rate,
    
    -- Estado
    CASE 
        WHEN MAX(bm.biasvalue) < 0.1 THEN 'COMPLIANT'
        WHEN MAX(bm.biasvalue) < 0.15 THEN 'WARNING'
        ELSE 'NON_COMPLIANT'
    END AS compliance_status,
    
    -- Última medición
    MAX(bm.createdat) AS last_measurement
FROM cor_model m
JOIN eval_biasmetrics bm ON m.idxmodel = bm.idxmodel
WHERE bm.createdat >= NOW() - INTERVAL '30 days'
GROUP BY m.idxmodel, m.modelname, bm.protectedattribute, bm.biasmetric;

-- Refresh diario
CREATE INDEX idx_mv_bias_compliance ON mv_compliance_art10_bias(compliance_status, protectedattribute);

-- Query: Dashboard bias por atributo protegido
SELECT 
    protectedattribute,
    COUNT(DISTINCT idxmodel) AS total_models,
    COUNT(*) FILTER (WHERE compliance_status = 'COMPLIANT') AS compliant_models,
    COUNT(*) FILTER (WHERE compliance_status = 'NON_COMPLIANT') AS non_compliant_models,
    ROUND(AVG(avg_bias)::NUMERIC, 4) AS overall_avg_bias
FROM mv_compliance_art10_bias
GROUP BY protectedattribute
ORDER BY overall_avg_bias DESC;
```

---

## Índices Recomendados

```sql
-- ============================================
-- ÍNDICES CRÍTICOS PARA RENDIMIENTO
-- ============================================

-- 1. Búsqueda por UUID (API REST)
CREATE UNIQUE INDEX idx_model_uuid ON cor_model(iduuid);
CREATE UNIQUE INDEX idx_evaluation_uuid ON cor_evaluation(iduuid);
CREATE UNIQUE INDEX idx_deployment_uuid ON cor_deployment(iduuid);

-- 2. Audit logs (TimescaleDB + hash)
CREATE INDEX idx_auditlog_timestamp_desc ON cor_auditlog(timestamp DESC);
CREATE INDEX idx_auditlog_entity ON cor_auditlog(entity, entityid);
CREATE INDEX idx_auditlog_hash ON cor_auditlog(current_hash);

-- 3. Linaje modelos (ltree GIST)
CREATE INDEX idx_lineage_path_gist ON cor_model_lineage USING gist(lineage_path);
CREATE INDEX idx_lineage_model ON cor_model_lineage(idxmodel);

-- 4. Embeddings (vector HNSW)
CREATE INDEX idx_chunks_embedding_hnsw ON rag_chunks USING hnsw (embedding vector_cosine_ops);
CREATE INDEX idx_model_embedding_hnsw ON cor_model USING hnsw (capabilities_embedding vector_cosine_ops);

-- 5. Búsqueda fuzzy (pg_trgm GIN)
CREATE INDEX idx_model_name_trgm ON cor_model USING gin (modelname gin_trgm_ops);
CREATE INDEX idx_prompt_text_trgm ON gov_prompt USING gin (prompttext gin_trgm_ops);

-- 6. Metadatos (hstore GIN)
CREATE INDEX idx_model_hyperparams ON cor_model USING gin (hyperparameters);
CREATE INDEX idx_eval_metadata ON cor_evaluation USING gin (metadata);

-- 7. Timestamps (queries temporales)
CREATE INDEX idx_model_createdat ON cor_model(createdat DESC);
CREATE INDEX idx_eval_createdat ON eval_modelmetrics(createdat DESC, idxmodel);

-- 8. Foreign keys (joins)
CREATE INDEX idx_eval_model ON eval_modelmetrics(idxmodel);
CREATE INDEX idx_deployment_model ON cor_deployment(idxmodel);
CREATE INDEX idx_auditlog_user ON cor_auditlog(userid);
```

---

## Referencias

- **TimescaleDB Continuous Aggregates**: https://docs.timescale.com/use-timescale/latest/continuous-aggregates/
- **pgvector Indexing**: https://github.com/pgvector/pgvector#indexing
- **ltree Documentation**: https://www.postgresql.org/docs/current/ltree.html
- **pg_trgm Similarity**: https://www.postgresql.org/docs/current/pgtrgm.html

