# PROMPTS_14 - Integración Extensiones PostgreSQL

## 📋 Objetivo

Integrar las 17 extensiones PostgreSQL instaladas en las tablas, vistas y queries existentes de CodeflowX Govern, optimizando rendimiento y compliance EU AI Act.

---

## 🎯 Extensiones a Integrar

| Extensión | Prioridad | Uso Principal |
|-----------|-----------|---------------|
| **uuid-ossp** | CRÍTICA | UUIDs automáticos (todas las tablas) |
| **pgcrypto** | CRÍTICA | Hash chains logs inmutables (Art. 19) |
| **timescaledb** | ALTA | Series temporales (logs, métricas) |
| **vector** | ALTA | Embeddings RAG y búsqueda semántica |
| **ltree** | ALTA | Linaje jerárquico modelos GPAI (Art. 53) |
| **pg_trgm** | MEDIA | Búsqueda difusa modelos/prompts |
| **hstore** | MEDIA | Metadatos flexibles |
| **btree_gin/gist** | MEDIA | Índices compuestos optimizados |

---

## GRUPO 1: Actualizar Tablas Existentes con uuid-ossp y pgcrypto

### PROMPT 14.1: Migración UUID Defaults en Todas las Tablas

**Objetivo:** Añadir defaults `uuid_generate_v4()` en columnas `iduuid` existentes.

**Contexto:**
- Todas las tablas EnArt tienen `iduuid UUID NOT NULL UNIQUE`
- Actualmente sin default → requiere generación manual
- Con default → generación automática

**SQL a ejecutar:**

```sql
-- Script de migración masiva
DO $$
DECLARE
    tbl_name TEXT;
    schema_name TEXT := 'public';
BEGIN
    FOR tbl_name IN 
        SELECT table_name 
        FROM information_schema.columns
        WHERE table_schema = schema_name
          AND column_name = 'iduuid'
          AND column_default IS NULL
    LOOP
        EXECUTE format(
            'ALTER TABLE %I.%I ALTER COLUMN iduuid SET DEFAULT uuid_generate_v4()',
            schema_name, tbl_name
        );
        RAISE NOTICE 'Updated table: %', tbl_name;
    END LOOP;
END $$;

-- Verificar migración
SELECT 
    table_name,
    column_name,
    column_default
FROM information_schema.columns
WHERE column_name = 'iduuid'
  AND table_schema = 'public'
ORDER BY table_name;
```

**Validación:**
- Crear registro test en cada tabla → debe generar UUID automático
- Verificar que UUIDs son únicos

---

### PROMPT 14.2: Fortalecer `IMLIMMUTABLELOGS` con Hash Chain (tabla real)

> Corrección 2025-11-16: en lugar de crear `cor_auditlog`, reutilizamos la tabla existente `IMLIMMUTABLELOGS` (ver `ImmutableLog.java`). Sólo añadimos triggers/extensiones sobre sus columnas reales (`IMLACTION`, `IMLENTITYTYPE`, etc.).

```sql
-- Estructura (ya existe) para referencia
\d+ IMLIMMUTABLELOGS;

-- Función: calcular hash automáticamente usando columnas reales
CREATE OR REPLACE FUNCTION iml_calculate_hash()
RETURNS TRIGGER AS $$
DECLARE
    last_hash VARCHAR(64);
    hash_input TEXT;
BEGIN
    SELECT IMLCURRENTHASH INTO last_hash
    FROM IMLIMMUTABLELOGS
    ORDER BY IDXIMMUTABLELOG DESC
    LIMIT 1;

    IF last_hash IS NULL THEN
        last_hash := 'GENESIS_BLOCK_CODEFLOWX_GOVERN';
    END IF;

    NEW.IMLPREVIOUSHASH := last_hash;

    hash_input :=
        last_hash ||
        NEW.IMLACTION ||
        NEW.IMLENTITYTYPE ||
        COALESCE(NEW.IMLENTITYID::TEXT, '') ||
        COALESCE(NEW.IMLUSERID::TEXT, '') ||
        NEW.IMLTIMESTAMP::TEXT ||
        COALESCE(NEW.IMLDATA::TEXT, '');

    NEW.IMLCURRENTHASH := encode(digest(hash_input, 'sha256'), 'hex');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_iml_hash_chain
    BEFORE INSERT ON IMLIMMUTABLELOGS
    FOR EACH ROW
    EXECUTE FUNCTION iml_calculate_hash();

-- Triggers para prevenir UPDATE/DELETE sobre los logs reales
CREATE OR REPLACE FUNCTION iml_prevent_mutations()
RETURNS TRIGGER AS $$
BEGIN
    RAISE EXCEPTION 'IML logs are immutable per EU AI Act Art. 19. Attempted action: %, ID: %',
        TG_OP,
        COALESCE(OLD.IDXIMMUTABLELOG::TEXT, NEW.IDXIMMUTABLELOG::TEXT);
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_iml_no_update
    BEFORE UPDATE ON IMLIMMUTABLELOGS
    FOR EACH ROW
    EXECUTE FUNCTION iml_prevent_mutations();

CREATE TRIGGER trg_iml_no_delete
    BEFORE DELETE ON IMLIMMUTABLELOGS
    FOR EACH ROW
    EXECUTE FUNCTION iml_prevent_mutations();

-- TimescaleDB sobre la columna real IMLTIMESTAMP
SELECT create_hypertable('IMLIMMUTABLELOGS', 'IMLTIMESTAMP', if_not_exists => TRUE);
SELECT add_retention_policy('IMLIMMUTABLELOGS', INTERVAL '10 years', if_not_exists => TRUE);
SELECT add_compression_policy('IMLIMMUTABLELOGS', INTERVAL '90 days', if_not_exists => TRUE);
```

**Validación (columnas reales):**

```sql
INSERT INTO IMLIMMUTABLELOGS (IMLACTION, IMLENTITYTYPE, IMLENTITYID, IMLUSERID, IMLDATA)
VALUES ('CREATE', 'MODEL', 123, 1, '{"MODNAME": "test_model"}');

SELECT 
    IDXIMMUTABLELOG,
    IMLACTION,
    IMLENTITYTYPE,
    LEFT(IMLPREVIOUSHASH, 8) || '...' AS prev_hash,
    LEFT(IMLCURRENTHASH, 8) || '...' AS curr_hash
FROM IMLIMMUTABLELOGS
ORDER BY IDXIMMUTABLELOG DESC
LIMIT 5;

UPDATE IMLIMMUTABLELOGS SET IMLACTION = 'MODIFIED' WHERE IDXIMMUTABLELOG = 1;
-- ERROR esperado: IML logs are immutable per EU AI Act Art. 19
```

---

### PROMPT 14.3: Crear Función verify_audit_chain()

**Objetivo:** Función para verificar integridad de logs (detectar manipulación).

**SQL:**

```sql
CREATE OR REPLACE FUNCTION verify_audit_chain(
    start_id BIGINT DEFAULT NULL,
    end_id BIGINT DEFAULT NULL
)
RETURNS TABLE (
    idxauditlog BIGINT,
    is_valid BOOLEAN,
    expected_hash VARCHAR(64),
    actual_hash VARCHAR(64),
    error_message TEXT,
    action VARCHAR(50),
    entity VARCHAR(100),
    timestamp TIMESTAMP
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
        FROM IMLIMMUTABLELOGS
        WHERE (start_id IS NULL OR idxauditlog >= start_id)
          AND (end_id IS NULL OR idxauditlog <= end_id)
        ORDER BY idxauditlog
    ),
    calculated_hashes AS (
        SELECT
            a.*,
            encode(
                digest(
                    COALESCE(expected_previous_hash, 'GENESIS_BLOCK_CODEFLOWX_GOVERN') ||
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
        FROM audit_sequence a
    )
    SELECT
        idxauditlog,
        (current_hash = calculated_hash AND 
         (previous_hash = expected_previous_hash OR expected_previous_hash IS NULL)) AS is_valid,
        calculated_hash AS expected_hash,
        current_hash AS actual_hash,
        CASE
            WHEN current_hash != calculated_hash THEN 'Hash mismatch - potential tampering detected'
            WHEN previous_hash != expected_previous_hash THEN 'Broken chain - missing records or sequence gap'
            ELSE NULL
        END AS error_message,
        action,
        entity,
        timestamp
    FROM calculated_hashes;
$$ LANGUAGE SQL STABLE;

-- Vista: Resumen integridad diaria
CREATE MATERIALIZED VIEW mv_audit_integrity_daily AS
SELECT
    DATE(timestamp) AS audit_date,
    COUNT(*) AS total_logs,
    COUNT(*) FILTER (WHERE is_valid) AS valid_logs,
    COUNT(*) FILTER (WHERE NOT is_valid) AS invalid_logs,
    ROUND(
        100.0 * COUNT(*) FILTER (WHERE is_valid) / NULLIF(COUNT(*), 0),
        2
    ) AS integrity_percentage,
    STRING_AGG(
        CASE WHEN NOT is_valid THEN idxauditlog::TEXT END,
        ', '
    ) AS invalid_log_ids
FROM verify_audit_chain()
GROUP BY DATE(timestamp);

CREATE INDEX idx_mv_audit_integrity ON mv_audit_integrity_daily(audit_date DESC);
```

**ViewModel ZKoss (Java):**

```java
@Command
@NotifyChange("auditIntegrity")
public void verifyAuditIntegrity() {
    String sql = "SELECT * FROM verify_audit_chain() WHERE NOT is_valid LIMIT 100";
    try (Connection conn = dataSource.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        List<Map<String, Object>> issues = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> issue = new HashMap<>();
            issue.put("logId", rs.getLong("idxauditlog"));
            issue.put("action", rs.getString("action"));
            issue.put("entity", rs.getString("entity"));
            issue.put("error", rs.getString("error_message"));
            issue.put("timestamp", rs.getTimestamp("timestamp"));
            issues.add(issue);
        }
        
        if (issues.isEmpty()) {
            Clients.showNotification("Audit chain integrity: VALID ✓", "info", null, "middle_center", 3000);
        } else {
            Clients.showNotification(
                issues.size() + " integrity issues detected! Review immediately.", 
                "error", null, "top_center", 0
            );
            setAuditIntegrity(issues);
        }
    }
}
```

---

## GRUPO 2: Implementar Linaje Modelos con ltree (Art. 53 GPAI)

### PROMPT 14.4: Crear Tabla MODMODELLINEAGE

**Objetivo:** Implementar linaje jerárquico para GPAI (Art. 53).

**SQL:**

```sql
CREATE TABLE MODMODELLINEAGE (
    idxlineage BIGSERIAL PRIMARY KEY,
    iduuid UUID NOT NULL DEFAULT uuid_generate_v4() UNIQUE,
    
    -- Referencia al modelo
    idxmodel BIGINT NOT NULL REFERENCES MODMODELS(idxmodel) ON DELETE CASCADE,
    
    -- Ruta jerárquica (CRITICAL!)
    lineage_path ltree NOT NULL UNIQUE,
    
    -- Tipo de modificación (Art. 53)
    modification_type VARCHAR(50) NOT NULL,  
    -- 'BASE', 'FINETUNE', 'ADAPTER', 'MERGE', 'QUANTIZATION', 'PRUNING'
    
    -- Padre en el linaje
    parent_idxmodel BIGINT REFERENCES MODMODELS(idxmodel),
    parent_lineage_path ltree,
    
    -- Metadata modificación
    modification_date TIMESTAMP NOT NULL DEFAULT NOW(),
    modification_description TEXT,
    
    -- Art. 53: ¿Modificación sustancial?
    substantial BOOLEAN NOT NULL DEFAULT true,
    substantial_justification TEXT,
    
    -- Métricas cambio
    parameter_change_pct NUMERIC(5,2),  -- % parámetros modificados
    performance_delta NUMERIC(10,4),    -- Δ accuracy
    
    -- Compliance
    requires_gpai_assessment BOOLEAN GENERATED ALWAYS AS (
        substantial AND modification_type IN ('FINETUNE', 'MERGE')
    ) STORED,
    
    -- Metadata
    createdat TIMESTAMP NOT NULL DEFAULT NOW(),
    updatedat TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_lineage_type CHECK (
        modification_type IN ('BASE', 'FINETUNE', 'ADAPTER', 'MERGE', 'QUANTIZATION', 'PRUNING')
    ),
    CONSTRAINT chk_base_no_parent CHECK (
        modification_type != 'BASE' OR parent_idxmodel IS NULL
    ),
    
    -- Índices
    INDEX idx_lineage_model (idxmodel),
    INDEX idx_lineage_parent (parent_idxmodel),
    INDEX idx_lineage_path_gist USING gist (lineage_path),
    INDEX idx_lineage_type (modification_type),
    INDEX idx_lineage_substantial (substantial) WHERE substantial = true
);

-- Trigger: Auto-populate parent_lineage_path
CREATE OR REPLACE FUNCTION populate_parent_lineage()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.parent_idxmodel IS NOT NULL THEN
        SELECT lineage_path INTO NEW.parent_lineage_path
        FROM MODMODELLINEAGE
        WHERE idxmodel = NEW.parent_idxmodel;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_populate_parent_lineage
    BEFORE INSERT OR UPDATE ON MODMODELLINEAGE
    FOR EACH ROW
    WHEN (NEW.parent_idxmodel IS NOT NULL)
    EXECUTE FUNCTION populate_parent_lineage();

-- Trigger: Validar lineage_path coherente con parent
CREATE OR REPLACE FUNCTION validate_lineage_path()
RETURNS TRIGGER AS $$
BEGIN
    -- Si tiene padre, el path debe empezar con el path del padre
    IF NEW.parent_lineage_path IS NOT NULL THEN
        IF NOT (NEW.lineage_path <@ NEW.parent_lineage_path) THEN
            RAISE EXCEPTION 'Lineage path must be descendant of parent path';
        END IF;
    END IF;
    
    -- Si es BASE, profundidad debe ser 1
    IF NEW.modification_type = 'BASE' AND nlevel(NEW.lineage_path) != 1 THEN
        RAISE EXCEPTION 'BASE models must have lineage_path depth = 1';
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_lineage_path
    BEFORE INSERT OR UPDATE ON MODMODELLINEAGE
    FOR EACH ROW
    EXECUTE FUNCTION validate_lineage_path();
```

**Ejemplos de inserción:**

```sql
-- Ejemplo 1: Modelo base
INSERT INTO MODMODELLINEAGE (idxmodel, lineage_path, modification_type, substantial)
VALUES (123, 'llama3_70b', 'BASE', true);

-- Ejemplo 2: Fine-tune
INSERT INTO MODMODELLINEAGE (
    idxmodel, 
    lineage_path, 
    parent_idxmodel, 
    modification_type, 
    substantial,
    modification_description,
    parameter_change_pct
)
VALUES (
    234, 
    'llama3_70b.finetuned_medical', 
    123, 
    'FINETUNE', 
    true,
    'Fine-tuned on 50K medical records (PubMed + clinical notes)',
    15.5
);

-- Ejemplo 3: Adapter (no sustancial)
INSERT INTO MODMODELLINEAGE (
    idxmodel, 
    lineage_path, 
    parent_idxmodel, 
    modification_type, 
    substantial,
    modification_description
)
VALUES (
    345, 
    'llama3_70b.finetuned_medical.adapter_spanish', 
    234, 
    'ADAPTER', 
    false,
    'LoRA adapter for Spanish language (rank=8, alpha=16)'
);

-- Ejemplo 4: Merge (sustancial)
INSERT INTO MODMODELLINEAGE (
    idxmodel, 
    lineage_path, 
    parent_idxmodel, 
    modification_type, 
    substantial,
    modification_description
)
VALUES (
    456, 
    'llama3_70b.merged_medical_legal', 
    123, 
    'MERGE', 
    true,
    'Merged finetuned_medical + finetuned_legal with SLERP interpolation'
);
```

**Validación:**
```sql
-- Ver árbol completo
SELECT 
    REPEAT('  ', nlevel(lineage_path) - 1) || '└─ ' || ltree2text(lineage_path) AS tree,
    modification_type,
    substantial,
    modification_date::DATE
FROM MODMODELLINEAGE
WHERE lineage_path <@ 'llama3_70b'
ORDER BY lineage_path;
```

---

### PROMPT 14.5: Crear Vistas y Funciones Linaje

**Objetivo:** Queries optimizadas para consultar linaje.

**SQL:**

```sql
-- Vista: Árbol completo de linaje
CREATE OR REPLACE VIEW v_model_lineage_tree AS
SELECT
    l.idxlineage,
    l.idxmodel,
    m.MODNAME,
    l.lineage_path,
    ltree2text(l.lineage_path) AS path_text,
    nlevel(l.lineage_path) AS depth,
    subpath(l.lineage_path, 0, -1) AS parent_path,
    l.modification_type,
    l.substantial,
    l.modification_description,
    l.modification_date,
    l.parameter_change_pct,
    l.performance_delta,
    l.requires_gpai_assessment,
    m.MODTYPE,
    m.riskclassification,
    m.gpaiclassification,
    pm.idxmodel AS parent_idxmodel,
    pm.MODNAME AS parent_MODNAME
FROM MODMODELLINEAGE l
JOIN MODMODELS m ON l.idxmodel = m.idxmodel
LEFT JOIN MODMODELLINEAGE pl ON l.parent_idxmodel = pl.idxmodel
LEFT JOIN MODMODELS pm ON pl.idxmodel = pm.idxmodel;

-- Función: Obtener ancestros (path completo hasta base)
CREATE OR REPLACE FUNCTION get_model_ancestry(model_id BIGINT)
RETURNS TABLE (
    idxmodel BIGINT,
    MODNAME VARCHAR,
    depth INT,
    modification_type VARCHAR,
    substantial BOOLEAN,
    lineage_path ltree
) AS $$
    SELECT 
        l.idxmodel,
        m.MODNAME,
        nlevel(l.lineage_path) AS depth,
        l.modification_type,
        l.substantial,
        l.lineage_path
    FROM MODMODELLINEAGE l
    JOIN MODMODELS m ON l.idxmodel = m.idxmodel
    WHERE l.lineage_path @> (
        SELECT lineage_path 
        FROM MODMODELLINEAGE 
        WHERE idxmodel = model_id
    )
    ORDER BY nlevel(l.lineage_path);
$$ LANGUAGE SQL STABLE;

-- Función: Obtener descendientes
CREATE OR REPLACE FUNCTION get_model_descendants(model_id BIGINT)
RETURNS TABLE (
    idxmodel BIGINT,
    MODNAME VARCHAR,
    depth INT,
    modification_type VARCHAR,
    substantial BOOLEAN,
    lineage_path ltree
) AS $$
    SELECT 
        l.idxmodel,
        m.MODNAME,
        nlevel(l.lineage_path) AS depth,
        l.modification_type,
        l.substantial,
        l.lineage_path
    FROM MODMODELLINEAGE l
    JOIN MODMODELS m ON l.idxmodel = m.idxmodel
    WHERE l.lineage_path <@ (
        SELECT lineage_path 
        FROM MODMODELLINEAGE 
        WHERE idxmodel = model_id
    )
    AND l.idxmodel != model_id
    ORDER BY nlevel(l.lineage_path);
$$ LANGUAGE SQL STABLE;

-- Vista: Impact Analysis
CREATE OR REPLACE VIEW v_model_impact_analysis AS
SELECT
    base.idxmodel AS base_idxmodel,
    base.MODNAME AS base_MODNAME,
    base.lineage_path,
    
    -- Descendientes directos
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
    COUNT(DISTINCT d.idxdeployment) AS active_deployments
FROM MODMODELLINEAGE base
JOIN MODMODELS m ON base.idxmodel = m.idxmodel
LEFT JOIN MODMODELLINEAGE desc 
    ON desc.lineage_path <@ base.lineage_path 
    AND desc.idxmodel != base.idxmodel
LEFT JOIN srvdeployment d ON desc.idxmodel = d.idxmodel AND d.status = 'ACTIVE'
GROUP BY base.idxmodel, base.MODNAME, base.lineage_path;

-- Vista: Compliance GPAI Art. 53
CREATE OR REPLACE VIEW v_gpai_compliance_art53 AS
SELECT
    l.idxlineage,
    m.MODNAME,
    m.gpaiclassification,
    l.lineage_path,
    l.modification_type,
    l.modification_date,
    l.substantial,
    l.requires_gpai_assessment,
    
    -- Documentación requerida
    COUNT(DISTINCT td.idxtechdoc) FILTER (WHERE td.doctype = 'MODIFICATION_REPORT') AS has_modification_report,
    COUNT(DISTINCT td.idxtechdoc) FILTER (WHERE td.doctype = 'GPAI_ASSESSMENT') AS has_gpai_assessment,
    
    -- Estado compliance
    CASE 
        WHEN NOT l.substantial THEN 'NOT_APPLICABLE'
        WHEN l.substantial AND 
             COUNT(DISTINCT td.idxtechdoc) FILTER (WHERE td.doctype = 'MODIFICATION_REPORT') >= 1 AND
             COUNT(DISTINCT td.idxtechdoc) FILTER (WHERE td.doctype = 'GPAI_ASSESSMENT') >= 1 
        THEN 'COMPLIANT'
        ELSE 'MISSING_DOCUMENTATION'
    END AS compliance_status
FROM MODMODELLINEAGE l
JOIN MODMODELS m ON l.idxmodel = m.idxmodel
LEFT JOIN cor_technicaldoc td ON m.idxmodel = td.idxmodel
WHERE m.gpaiclassification IN ('GPAI_SYSTEMIC', 'GPAI_STANDARD')
  AND l.modification_type != 'BASE'
GROUP BY l.idxlineage, m.MODNAME, m.gpaiclassification, l.lineage_path,
         l.modification_type, l.modification_date, l.substantial, l.requires_gpai_assessment;
```

**ViewModel ZKoss (Java):**

```java
@Command
@NotifyChange("lineageTree")
public void loadModelLineage(@BindingParam("modelId") Long modelId) {
    String sql = """
        SELECT 
            REPEAT('  ', depth - 1) || '└─ ' || MODNAME AS tree_display,
            modification_type,
            substantial,
            modification_date::DATE AS mod_date,
            lineage_path::TEXT AS path
        FROM get_model_ancestry(?)
        ORDER BY depth
        """;
    
    try (Connection conn = dataSource.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setLong(1, modelId);
        ResultSet rs = stmt.executeQuery();
        
        List<Map<String, Object>> tree = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> node = new HashMap<>();
            node.put("display", rs.getString("tree_display"));
            node.put("type", rs.getString("modification_type"));
            node.put("substantial", rs.getBoolean("substantial"));
            node.put("date", rs.getDate("mod_date"));
            tree.add(node);
        }
        setLineageTree(tree);
    }
}
```

---

## GRUPO 3: Optimizar con TimescaleDB

### PROMPT 14.6: Convertir Tablas Métricas a Hypertables

**Objetivo:** Optimizar tablas de alto volumen con TimescaleDB.

**SQL:**

```sql
-- 1. eval_modelmetrics (millones de filas)
SELECT create_hypertable(
    'eval_modelmetrics', 
    'createdat',
    chunk_time_interval => INTERVAL '1 month',
    if_not_exists => TRUE
);

-- Políticas
SELECT add_retention_policy('eval_modelmetrics', INTERVAL '2 years', if_not_exists => TRUE);
SELECT add_compression_policy('eval_modelmetrics', INTERVAL '90 days', if_not_exists => TRUE);

-- 2. eval_biasmetrics
SELECT create_hypertable(
    'eval_biasmetrics', 
    'createdat',
    chunk_time_interval => INTERVAL '1 month',
    if_not_exists => TRUE
);

SELECT add_retention_policy('eval_biasmetrics', INTERVAL '2 years', if_not_exists => TRUE);
SELECT add_compression_policy('eval_biasmetrics', INTERVAL '90 days', if_not_exists => TRUE);

-- 3. eval_driftmetrics
SELECT create_hypertable(
    'eval_driftmetrics', 
    'createdat',
    chunk_time_interval => INTERVAL '1 week',
    if_not_exists => TRUE
);

SELECT add_retention_policy('eval_driftmetrics', INTERVAL '1 year', if_not_exists => TRUE);
SELECT add_compression_policy('eval_driftmetrics', INTERVAL '30 days', if_not_exists => TRUE);

-- 4. cor_predictionlog (alto volumen)
SELECT create_hypertable(
    'cor_predictionlog', 
    'timestamp',
    chunk_time_interval => INTERVAL '1 day',
    if_not_exists => TRUE
);

SELECT add_retention_policy('cor_predictionlog', INTERVAL '6 months', if_not_exists => TRUE);
SELECT add_compression_policy('cor_predictionlog', INTERVAL '7 days', if_not_exists => TRUE);

-- 5. IMLIMMUTABLELOGS (ya creado en PROMPT 14.2, confirmar políticas)
SELECT add_retention_policy('IMLIMMUTABLELOGS', INTERVAL '10 years', if_not_exists => TRUE);
SELECT add_compression_policy('IMLIMMUTABLELOGS', INTERVAL '90 days', if_not_exists => TRUE);
```

**Verificación:**
```sql
-- Ver hypertables y políticas
SELECT 
    h.hypertable_name,
    h.compression_enabled,
    p.job_id AS retention_job,
    c.job_id AS compression_job
FROM timescaledb_information.hypertables h
LEFT JOIN timescaledb_information.jobs p ON p.hypertable_name = h.hypertable_name 
    AND p.proc_name = 'policy_retention'
LEFT JOIN timescaledb_information.jobs c ON c.hypertable_name = h.hypertable_name 
    AND c.proc_name = 'policy_compression'
ORDER BY h.hypertable_name;
```

---

### PROMPT 14.7: Crear Continuous Aggregates

**Objetivo:** Vistas materializadas incrementales para dashboards rápidos.

**SQL en:** `/docs/arquitectura/POSTGRESQL_QUERIES_VISTAS_OPTIMIZADAS.md` (sección "Vistas Materializadas TimescaleDB")

**Implementar:**
1. `mv_model_daily_metrics` - Métricas diarias por modelo
2. `mv_drift_alerts_7d` - Alertas drift últimos 7 días
3. `mv_bias_weekly_summary` - Resumen bias semanal

**ViewModel ZKoss (Java):**

```java
@Command
@NotifyChange("dailyMetrics")
public void loadDailyMetrics() {
    String sql = """
        SELECT 
            day::DATE,
            MODNAME,
            ROUND(avg_accuracy::NUMERIC, 4) AS accuracy,
            eval_count,
            meets_accuracy_threshold
        FROM mv_model_daily_metrics
        WHERE day >= CURRENT_DATE - INTERVAL '30 days'
        ORDER BY day DESC, MODNAME
        LIMIT 100
        """;
    
    // Execute y cargar en grid...
}
```

---

## GRUPO 4: Integrar pgvector para RAG

### PROMPT 14.8: Añadir Columnas Embedding a Tablas Existentes

**Objetivo:** Habilitar búsqueda semántica.

**SQL:**

```sql
-- 1. RAGCHUNKS (si no existe ya)
ALTER TABLE RAGCHUNKS 
ADD COLUMN IF NOT EXISTS embedding vector(1536);  -- OpenAI ada-002

-- Índice HNSW (rápido, preciso)
CREATE INDEX IF NOT EXISTS idx_chunks_embedding_hnsw 
ON RAGCHUNKS 
USING hnsw (embedding vector_cosine_ops)
WITH (m = 16, ef_construction = 64);

-- 2. PRMPROMPTS (detectar duplicados)
ALTER TABLE PRMPROMPTS 
ADD COLUMN IF NOT EXISTS embedding vector(1536);

CREATE INDEX IF NOT EXISTS idx_prompt_embedding_hnsw 
ON PRMPROMPTS 
USING hnsw (embedding vector_cosine_ops);

-- 3. MODMODELS (clustering capacidades)
ALTER TABLE MODMODELS 
ADD COLUMN IF NOT EXISTS capabilities_embedding vector(384);  -- MiniLM

CREATE INDEX IF NOT EXISTS idx_model_capabilities_hnsw 
ON MODMODELS 
USING hnsw (capabilities_embedding vector_cosine_ops);
```

**Función RAG search:**

Ver `/docs/arquitectura/POSTGRESQL_QUERIES_VISTAS_OPTIMIZADAS.md` sección "Búsqueda Semántica (pgvector)".

---

## GRUPO 5: Búsqueda Difusa con pg_trgm

### PROMPT 14.9: Añadir Índices Trigrama

**Objetivo:** Búsqueda tolerante a typos.

**SQL:**

```sql
-- 1. Modelos
CREATE INDEX IF NOT EXISTS idx_model_name_trgm 
ON MODMODELS USING gin (MODNAME gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_model_desc_trgm 
ON MODMODELS USING gin (MODDESCRIPTION gin_trgm_ops);

-- 2. Prompts
CREATE INDEX IF NOT EXISTS idx_prompt_text_trgm 
ON PRMPROMPTS USING gin (prompttext gin_trgm_ops);

-- 3. Usuarios
CREATE INDEX IF NOT EXISTS idx_user_name_trgm 
ON cor_user USING gin (username gin_trgm_ops);

-- 4. Proyectos
CREATE INDEX IF NOT EXISTS idx_project_name_trgm 
ON cor_project USING gin (projectname gin_trgm_ops);
```

**Función búsqueda fuzzy:**

Ver `/docs/arquitectura/POSTGRESQL_QUERIES_VISTAS_OPTIMIZADAS.md` sección "Búsqueda Difusa (pg_trgm)".

---

## GRUPO 6: Metadatos Flexibles con hstore

### PROMPT 14.10: Añadir Columnas hstore

**Objetivo:** Metadatos dinámicos sin cambiar esquema.

**SQL:**

```sql
-- 1. Hyperparámetros modelos
ALTER TABLE MODMODELS 
ADD COLUMN IF NOT EXISTS hyperparameters hstore;

CREATE INDEX IF NOT EXISTS idx_model_hyperparams 
ON MODMODELS USING gin (hyperparameters);

-- 2. Metadata evaluaciones
ALTER TABLE GOVMODELEVALUATIONS 
ADD COLUMN IF NOT EXISTS metadata hstore;

CREATE INDEX IF NOT EXISTS idx_eval_metadata 
ON GOVMODELEVALUATIONS USING gin (metadata);

-- 3. Configuración deployments
ALTER TABLE srvdeployment 
ADD COLUMN IF NOT EXISTS config hstore;

CREATE INDEX IF NOT EXISTS idx_deployment_config 
ON srvdeployment USING gin (config);
```

**Ejemplos uso:**

```sql
-- Insertar hyperparámetros
UPDATE MODMODELS 
SET hyperparameters = 'learning_rate=>0.0001, batch_size=>32, epochs=>5, optimizer=>AdamW'::hstore
WHERE idxmodel = 123;

-- Query por hyperparámetro específico
SELECT MODNAME, hyperparameters->'learning_rate' AS lr
FROM MODMODELS
WHERE hyperparameters ? 'learning_rate'
  AND (hyperparameters->'learning_rate')::FLOAT < 0.001;

-- Buscar modelos con configuración específica
SELECT MODNAME
FROM MODMODELS
WHERE hyperparameters @> 'optimizer=>AdamW, epochs=>5'::hstore;
```

---

## GRUPO 7: Índices Compuestos Optimizados

### PROMPT 14.11: Crear Índices btree_gin/gist

**Objetivo:** Índices compuestos para queries complejas.

**SQL:**

```sql
-- 1. Búsqueda modelos por tipo + nombre fuzzy
CREATE INDEX idx_model_type_name_gin 
ON MODMODELS USING gin (MODTYPE, MODNAME gin_trgm_ops);

-- 2. Audit logs por entity + timestamp
CREATE INDEX idx_audit_entity_timestamp_gist 
ON IMLIMMUTABLELOGS USING gist (entity, timestamp);

-- 3. Evaluaciones por modelo + fecha
CREATE INDEX idx_eval_model_date_gist 
ON eval_modelmetrics USING gist (idxmodel, createdat);

-- 4. Prompts por tipo + embedding
CREATE INDEX idx_prompt_type_embedding_gin 
ON PRMPROMPTS USING gin (prompttype, embedding vector_cosine_ops);
```

---

## GRUPO 8: Funciones Utilidad

### PROMPT 14.12: Crear Funciones SQL Útiles

**SQL:**

```sql
-- 1. Generar UUID deterministico (mismo input = mismo UUID)
CREATE OR REPLACE FUNCTION generate_deterministic_uuid(input_text TEXT)
RETURNS UUID AS $$
    SELECT uuid_generate_v5(uuid_ns_url(), 'https://codeflowx.ai/' || input_text);
$$ LANGUAGE SQL IMMUTABLE;

-- Uso: IDs reproducibles para tests
SELECT generate_deterministic_uuid('model_llama3_70b');  -- Siempre mismo UUID

-- 2. Pseudonimizar campo (GDPR)
CREATE OR REPLACE FUNCTION pseudonymize(value TEXT, salt TEXT DEFAULT 'codeflowx_salt')
RETURNS TEXT AS $$
    SELECT encode(digest(value || salt, 'sha256'), 'hex');
$$ LANGUAGE SQL IMMUTABLE;

-- Uso: Logs con datos personales
INSERT INTO IMLIMMUTABLELOGS (action, entity, payload)
VALUES ('LOGIN', 'USER', jsonb_build_object('email', pseudonymize('user@example.com')));

-- 3. Calcular similitud texto (wrapper pg_trgm)
CREATE OR REPLACE FUNCTION text_similarity(text1 TEXT, text2 TEXT)
RETURNS FLOAT AS $$
    SELECT similarity(text1, text2);
$$ LANGUAGE SQL IMMUTABLE;

-- 4. Extraer nivel ltree
CREATE OR REPLACE FUNCTION get_lineage_depth(path ltree)
RETURNS INT AS $$
    SELECT nlevel(path);
$$ LANGUAGE SQL IMMUTABLE;

-- 5. Formato path legible
CREATE OR REPLACE FUNCTION format_lineage_path(path ltree)
RETURNS TEXT AS $$
    SELECT replace(ltree2text(path), '.', ' → ');
$$ LANGUAGE SQL IMMUTABLE;
```

---

## GRUPO 5: Tablas Governance API (Eventos + Webhooks)

### PROMPT 14.11: Crear tabla `GOVGOVERNANCEEVENTS` (TimescaleDB + pgcrypto + pg_trgm)

**Objetivo:** Persistir todos los eventos ingeridos por la API pública (`codeflowx-governance-api`) garantizando trazabilidad, hash del prompt y consultas eficientes.

**SQL propuesta:**

```sql
CREATE TABLE govgovernanceevents (
    idxgovernanceevent BIGSERIAL PRIMARY KEY,
    iduuid UUID NOT NULL DEFAULT uuid_generate_v4() UNIQUE,
    idxproject BIGINT NOT NULL,
    govtraceid VARCHAR(64) NOT NULL,
    goveventtype VARCHAR(30) NOT NULL, -- CHAT_COMPLETION, AGENT_ACTION, RAG_QUERY...
    govsource VARCHAR(30) NOT NULL,    -- N8N, CHATGPT_API, CLAUDE_API, MCP, OTHER
    govprompt TEXT NOT NULL,
    govprompthash VARCHAR(64) NOT NULL,
    govinputmetadata JSONB,
    govoutputtext TEXT,
    govoutputmetadata JSONB,           -- tokens, confidence, latency
    govmodelprovider VARCHAR(50),
    govMODNAME VARCHAR(80),
    govtemperature NUMERIC(4,3),
    govriskflags JSONB,                -- array de flags PII, BIAS, SENSITIVE_TOPIC
    govstatus VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    govdecisionpayload JSONB,          -- resultados agregados (se rellena al cerrar)
    createdat TIMESTAMP NOT NULL DEFAULT NOW(),
    updatedat TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_gov_idxproject FOREIGN KEY (idxproject)
        REFERENCES prjprojects (idxproject)
);

-- Hash prompt automáticamente (pgcrypto) y actualizar updatedat
CREATE OR REPLACE FUNCTION gov_set_prompthash()
RETURNS TRIGGER AS $$
BEGIN
    NEW.govprompthash := encode(digest(COALESCE(NEW.govprompt, '') || COALESCE(NEW.govtraceid, ''), 'sha256'), 'hex');
    NEW.updatedat := NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_gov_set_prompthash
    BEFORE INSERT OR UPDATE ON govgovernanceevents
    FOR EACH ROW
    EXECUTE FUNCTION gov_set_prompthash();

-- Índices (pg_trgm + GIN/BTREE_GIN)
CREATE INDEX idx_gov_events_traceid ON govgovernanceevents (govtraceid);
CREATE INDEX idx_gov_events_status ON govgovernanceevents (govstatus);
CREATE INDEX idx_gov_events_prompthash ON govgovernanceevents (govprompthash);
CREATE INDEX idx_gov_events_source ON govgovernanceevents (govsource, goveventtype);
CREATE INDEX idx_gov_events_prompt_trgm ON govgovernanceevents USING gin (govprompt gin_trgm_ops);
CREATE INDEX idx_gov_events_inputmeta ON govgovernanceevents USING gin (govinputmetadata jsonb_path_ops);
CREATE INDEX idx_gov_events_outputmeta ON govgovernanceevents USING gin (govoutputmetadata jsonb_path_ops);

-- TimescaleDB para series temporales
SELECT create_hypertable('govgovernanceevents', 'createdat', if_not_exists => TRUE);
-- Compresión y retención (Art. 71 → 6 años mínimo)
SELECT add_compression_policy('govgovernanceevents', INTERVAL '30 days', if_not_exists => TRUE);
SELECT add_retention_policy('govgovernanceevents', INTERVAL '6 years', if_not_exists => TRUE);
```

**Validaciones:**
- Insertar evento dummy y comprobar que `govprompthash` se genera.
- Consultar por `traceId` y `status` (índices en uso).
- Analizar `EXPLAIN` sobre búsqueda `govprompt ILIKE '%riesgo%'` → usa `pg_trgm`.

---

### PROMPT 14.12: Tabla `GOVWEBHOOKSUBSCRIPTIONS` (pgcrypto para secretos)

```sql
CREATE TABLE govwebhooksubscriptions (
    idxwebhooksubscription BIGSERIAL PRIMARY KEY,
    iduuid UUID NOT NULL DEFAULT uuid_generate_v4() UNIQUE,
    idxproject BIGINT NOT NULL,
    gwsname VARCHAR(150) NOT NULL,
    gwsurl VARCHAR(500) NOT NULL,
    gwsevents JSONB NOT NULL,              -- ej. ["EVALUATION_COMPLETED", "INCIDENT_RAISED"]
    gwssecret BYTEA NOT NULL,              -- cifrado con pgp_sym_encrypt
    gwsactive BOOLEAN NOT NULL DEFAULT TRUE,
    gwscreatedby BIGINT,
    gwscreatedat TIMESTAMP NOT NULL DEFAULT NOW(),
    gwsupdatedat TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_gws_project FOREIGN KEY (idxproject)
        REFERENCES prjprojects (idxproject)
);

CREATE OR REPLACE FUNCTION gws_encrypt_secret()
RETURNS TRIGGER AS $$
DECLARE
    key TEXT := current_setting('codeflowx.pgcrypto.key', true);
BEGIN
    IF key IS NULL THEN
        RAISE EXCEPTION 'Missing pgcrypto key (codeflowx.pgcrypto.key)';
    END IF;
    IF TG_OP = 'INSERT' OR NEW.gwssecret <> OLD.gwssecret THEN
        NEW.gwssecret := pgp_sym_encrypt(convert_to(NEW.gwssecret::text, 'UTF8'), key, 'cipher-algo=aes256');
    END IF;
    NEW.gwsupdatedat := NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_gws_encrypt_secret
    BEFORE INSERT OR UPDATE ON govwebhooksubscriptions
    FOR EACH ROW
    EXECUTE FUNCTION gws_encrypt_secret();

CREATE INDEX idx_gws_project ON govwebhooksubscriptions (idxproject, gwsactive);
CREATE INDEX idx_gws_events ON govwebhooksubscriptions USING gin (gwsevents jsonb_path_ops);
```

**Nota:** Guardar la clave `codeflowx.pgcrypto.key` en `postgresql.conf` o `ALTER SYSTEM` (no en SQL plano).

---

### PROMPT 14.13: Tabla `GOVWEBHOOKDELIVERIES` (TimescaleDB + retries)

```sql
CREATE TABLE govwebhookdeliveries (
    idxwebhookdelivery BIGSERIAL PRIMARY KEY,
    iduuid UUID NOT NULL DEFAULT uuid_generate_v4() UNIQUE,
    idxwebhooksubscription BIGINT NOT NULL,
    gwdeventuuid UUID NOT NULL,
    gwdpayload JSONB NOT NULL,
    gwdstatus VARCHAR(20) NOT NULL DEFAULT 'PENDING',   -- PENDING, SENT, RETRYING, FAILED
    gwdattempts SMALLINT NOT NULL DEFAULT 0,
    gwdnextrtryat TIMESTAMP,
    gwdlasterror TEXT,
    createdat TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_gwd_webhook FOREIGN KEY (idxwebhooksubscription)
        REFERENCES govwebhooksubscriptions (idxwebhooksubscription)
);

CREATE INDEX idx_gwd_status ON govwebhookdeliveries (gwdstatus, gwdattempts);
CREATE INDEX idx_gwd_nextretry ON govwebhookdeliveries (gwdnextrtryat) WHERE gwdstatus = 'RETRYING';

SELECT create_hypertable('govwebhookdeliveries', 'createdat', if_not_exists => TRUE);
SELECT add_retention_policy('govwebhookdeliveries', INTERVAL '18 months', if_not_exists => TRUE);
```

---

### PROMPT 14.14: Tabla `GOVGOVERNANCERESULTS` (Resultados evaluaciones Python)

```sql
CREATE TABLE govgovernanceresults (
    idxgovernanceresult BIGSERIAL PRIMARY KEY,
    iduuid UUID NOT NULL DEFAULT uuid_generate_v4() UNIQUE,
    idxgovernanceevent BIGINT NOT NULL,
    gvrstage VARCHAR(30) NOT NULL,          -- LLM_EVAL, BIAS_CHECK, DPIA, RAG_QUALITY, etc.
    gvrscore NUMERIC(5,2),
    gvrseverity VARCHAR(20),                -- INFO, WARNING, CRITICAL
    gvrmetrics JSONB,
    gvrexplanations JSONB,                  -- razonamientos, evidencias, enlaces
    gvrrecommendations JSONB,
    gvrcreatedat TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_gvr_event FOREIGN KEY (idxgovernanceevent)
        REFERENCES govgovernanceevents (idxgovernanceevent)
);

CREATE INDEX idx_gvr_event ON govgovernanceresults (idxgovernanceevent);
CREATE INDEX idx_gvr_stage ON govgovernanceresults (gvrstage, gvrseverity);
CREATE INDEX idx_gvr_metrics ON govgovernanceresults USING gin (gvrmetrics jsonb_path_ops);
```

**Integración con extensiones:**
- `timescaledb` en eventos + deliveries → queries por rango temporal (monitorización Art. 15/71).
- `pgcrypto` para hashing y cifrado de secretos.
- `pg_trgm` + `jsonb_path_ops` para búsquedas rápidas por contenido (investigaciones auditoras).
- `btree_gin` se recomienda si se añaden columnas compuestas.

---

**Acciones posteriores:**
1. Documentar entidades EnArt correspondientes (prefijo `gov`) en PROMPTS_05 Grupo D.
2. Actualizar migraciones Liquibase/Flyway.
3. Generar vistas materializadas según necesidades (ej. conteo incidentes por proyecto usando Timescale continuous aggregates).

---

## VALIDACIÓN COMPLETA

### Script de Verificación

```sql
-- ============================================
-- VERIFICACIÓN EXTENSIONES POSTGRESQL
-- ============================================

-- 1. Verificar extensiones instaladas
SELECT 
    extname AS extension,
    extversion AS version,
    CASE 
        WHEN extname IN ('uuid-ossp', 'pgcrypto') THEN 'CRÍTICA'
        WHEN extname IN ('timescaledb', 'vector', 'ltree') THEN 'CORE'
        WHEN extname IN ('pg_trgm', 'hstore') THEN 'ALTA'
        ELSE 'MEDIA'
    END AS priority
FROM pg_extension
WHERE extname IN (
    'uuid-ossp', 'pgcrypto', 'timescaledb', 'vector',
    'pg_trgm', 'fuzzystrmatch', 'unaccent',
    'btree_gin', 'btree_gist', 'ltree', 'hstore',
    'tablefunc', 'pg_stat_statements', 'dblink'
)
ORDER BY priority, extname;

-- 2. Verificar columnas UUID tienen default
SELECT 
    table_name,
    column_name,
    column_default,
    CASE 
        WHEN column_default LIKE '%uuid_generate_v4%' THEN '✓ OK'
        ELSE '✗ MISSING DEFAULT'
    END AS status
FROM information_schema.columns
WHERE column_name = 'iduuid'
  AND table_schema = 'public'
ORDER BY status DESC, table_name;

-- 3. Verificar hypertables TimescaleDB
SELECT 
    hypertable_name,
    compression_enabled,
    num_chunks
FROM timescaledb_information.hypertables
ORDER BY hypertable_name;

-- 4. Verificar continuous aggregates
SELECT 
    view_name,
    materialization_hypertable_name,
    refresh_lag,
    refresh_interval
FROM timescaledb_information.continuous_aggregates
ORDER BY view_name;

-- 5. Verificar índices vector
SELECT 
    tablename,
    indexname,
    indexdef
FROM pg_indexes
WHERE indexdef LIKE '%vector%'
ORDER BY tablename;

-- 6. Verificar índices ltree
SELECT 
    tablename,
    indexname,
    indexdef
FROM pg_indexes
WHERE indexdef LIKE '%ltree%'
ORDER BY tablename;

-- 7. Test audit log integrity
SELECT 
    COUNT(*) AS total_logs,
    COUNT(*) FILTER (WHERE current_hash IS NOT NULL) AS with_hash,
    COUNT(*) FILTER (WHERE previous_hash IS NOT NULL) AS with_chain,
    COUNT(*) FILTER (WHERE is_valid) AS valid_logs
FROM (
    SELECT *, 
           (current_hash = encode(digest(COALESCE(previous_hash,'GENESIS_BLOCK_CODEFLOWX_GOVERN')||action||entity||timestamp::TEXT, 'sha256'), 'hex')) AS is_valid
    FROM IMLIMMUTABLELOGS
    LIMIT 100
) t;

-- 8. Test linaje modelos
SELECT 
    COUNT(*) AS total_lineages,
    COUNT(DISTINCT idxmodel) AS unique_models,
    MAX(nlevel(lineage_path)) AS max_depth,
    COUNT(*) FILTER (WHERE substantial) AS substantial_modifications
FROM MODMODELLINEAGE;

-- 9. Estadísticas storage
SELECT 
    schemaname,
    tablename,
    pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) AS total_size,
    pg_size_pretty(pg_relation_size(schemaname||'.'||tablename)) AS table_size,
    pg_size_pretty(pg_indexes_size(schemaname||'.'||tablename)) AS indexes_size
FROM pg_tables
WHERE schemaname = 'public'
  AND tablename IN (
      'IMLIMMUTABLELOGS', 'MODMODELLINEAGE', 'eval_modelmetrics', 
      'eval_biasmetrics', 'RAGCHUNKS', 'PRMPROMPTS'
  )
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;

-- 10. Performance pg_stat_statements
SELECT 
    LEFT(query, 60) AS query_preview,
    calls,
    ROUND(mean_exec_time::NUMERIC, 2) AS avg_ms,
    ROUND(total_exec_time::NUMERIC, 2) AS total_ms
FROM pg_stat_statements
WHERE query LIKE '%MODMODELS%' OR query LIKE '%eval_%'
ORDER BY mean_exec_time DESC
LIMIT 10;
```

---

## Referencias

- **Documentación Arquitectura:** `/docs/arquitectura/POSTGRESQL_EXTENSIONES_COMPLETAS.md`
- **Queries Optimizadas:** `/docs/arquitectura/POSTGRESQL_QUERIES_VISTAS_OPTIMIZADAS.md`
- **PROMPTS Anteriores:** `PROMPTS_03`, `PROMPTS_05`, `PROMPTS_08` (actualizar con estas extensiones)

---

## Orden de Ejecución Recomendado

1. ✅ **PROMPT 14.1**: UUID defaults (rápido, crítico)
2. ✅ **PROMPT 14.2-14.3**: Audit logs (Art. 19 compliance)
3. ✅ **PROMPT 14.4-14.5**: Linaje modelos (Art. 53 GPAI)
4. ✅ **PROMPT 14.6-14.7**: TimescaleDB hypertables + continuous aggregates
5. ✅ **PROMPT 14.8**: pgvector embeddings (RAG)
6. ✅ **PROMPT 14.9**: pg_trgm índices (búsqueda fuzzy)
7. ✅ **PROMPT 14.10**: hstore metadatos
8. ✅ **PROMPT 14.11**: Índices compuestos
9. ✅ **PROMPT 14.12**: Funciones utilidad

**Tiempo estimado total:** 4-6 horas

