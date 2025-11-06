# PostgreSQL - Extensiones CodeflowX Govern

## 📋 Índice

1. [Extensiones Instaladas](#extensiones-instaladas)
2. [Extensiones Críticas](#extensiones-críticas)
3. [Extensiones de Rendimiento](#extensiones-de-rendimiento)
4. [Extensiones de Búsqueda](#extensiones-de-búsqueda)
5. [Extensiones de Estructura](#extensiones-de-estructura)
6. [Casos de Uso por Extensión](#casos-de-uso-por-extensión)
7. [Integración con EU AI Act](#integración-con-eu-ai-act)

---

## Extensiones Instaladas

```sql
-- Script de instalación completo
\c codeflowx_govern

-- CRÍTICAS (Compliance AI Act + EnArt)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";         -- UUIDs automáticos
CREATE EXTENSION IF NOT EXISTS pgcrypto;            -- Hashes inmutables Art. 19

-- SERIES TEMPORALES Y EMBEDDINGS
CREATE EXTENSION IF NOT EXISTS timescaledb;         -- Logs, métricas, evaluaciones
CREATE EXTENSION IF NOT EXISTS vector;              -- Embeddings RAG

-- BÚSQUEDA Y TEXTO
CREATE EXTENSION IF NOT EXISTS pg_trgm;             -- Búsqueda difusa trigrama
CREATE EXTENSION IF NOT EXISTS fuzzystrmatch;       -- Levenshtein, soundex
CREATE EXTENSION IF NOT EXISTS unaccent;            -- Búsqueda sin acentos

-- ÍNDICES AVANZADOS
CREATE EXTENSION IF NOT EXISTS btree_gin;           -- Índices GIN compuestos
CREATE EXTENSION IF NOT EXISTS btree_gist;          -- Índices GIST compuestos

-- ESTRUCTURA Y DATOS
CREATE EXTENSION IF NOT EXISTS ltree;               -- Linaje jerárquico modelos
CREATE EXTENSION IF NOT EXISTS hstore;              -- Key-value metadatos
CREATE EXTENSION IF NOT EXISTS tablefunc;           -- Crosstab, pivote

-- MONITORING Y CONECTIVIDAD
CREATE EXTENSION IF NOT EXISTS pg_stat_statements;  -- Análisis queries
CREATE EXTENSION IF NOT EXISTS dblink;              -- Conexiones remotas

-- GEOMETRÍA (opcional)
CREATE EXTENSION IF NOT EXISTS cube;                -- Embeddings multidimensionales
CREATE EXTENSION IF NOT EXISTS earthdistance;       -- Distancias geográficas
```

---

## Extensiones Críticas

### 1. `uuid-ossp` - Identificadores Únicos

**Por qué es crítica:**
- **TODAS** las tablas EnArt tienen `iduuid UUID NOT NULL UNIQUE`
- Requerido para generación automática de UUIDs
- Sin esto, los inserts fallarán

**Funciones principales:**
```sql
-- Generación UUID v4 (random)
SELECT uuid_generate_v4();
-- Ejemplo: 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'

-- Generación UUID v5 (hash SHA1 namespace+nombre)
SELECT uuid_generate_v5(uuid_ns_url(), 'https://codeflowx.ai/model/llama3');
-- Siempre genera el mismo UUID para el mismo input
```

**Uso en tablas:**
```sql
CREATE TABLE cor_model (
    idxmodel BIGSERIAL PRIMARY KEY,
    iduuid UUID NOT NULL DEFAULT uuid_generate_v4() UNIQUE,
    modelname VARCHAR(200) NOT NULL,
    createdat TIMESTAMP NOT NULL DEFAULT NOW()
);
```

**Casos de uso:**
- ✅ Identificadores externos para API REST
- ✅ Referencias entre microservicios
- ✅ Evitar exponer IDs secuenciales (seguridad)
- ✅ Merge/sync entre bases de datos distribuidas

---

### 2. `pgcrypto` - Criptografía y Hashes

**Por qué es crítica:**
- **EU AI Act Art. 19**: Logs inmutables con hash chains
- GDPR: Pseudonimización reversible
- Integridad: Detectar manipulación logs

**Funciones principales:**
```sql
-- Hash SHA256
SELECT encode(digest('texto', 'sha256'), 'hex');
-- Ejemplo: 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3'

-- HMAC (hash con clave secreta)
SELECT encode(hmac('mensaje', 'secret_key', 'sha256'), 'hex');

-- UUID random criptográfico
SELECT gen_random_uuid();

-- Encriptación simétrica (AES)
SELECT pgp_sym_encrypt('dato sensible', 'clave');
SELECT pgp_sym_decrypt(dato_encriptado, 'clave');
```

**Uso crítico - Logs inmutables (Art. 19):**
```sql
-- Tabla de audit logs con hash chain
CREATE TABLE cor_auditlog (
    idxauditlog BIGSERIAL PRIMARY KEY,
    iduuid UUID NOT NULL DEFAULT gen_random_uuid(),
    
    -- Datos del log
    action VARCHAR(50) NOT NULL,
    entity VARCHAR(100) NOT NULL,
    entityid BIGINT,
    userid BIGINT,
    timestamp TIMESTAMP NOT NULL DEFAULT NOW(),
    payload JSONB,
    
    -- Hash chain (Art. 19 - inmutabilidad)
    previous_hash VARCHAR(64),  -- Hash del registro anterior
    current_hash VARCHAR(64) GENERATED ALWAYS AS (
        encode(
            digest(
                COALESCE(previous_hash, '') || 
                action || 
                entity || 
                COALESCE(entityid::TEXT, '') || 
                timestamp::TEXT ||
                COALESCE(payload::TEXT, ''),
                'sha256'
            ),
            'hex'
        )
    ) STORED,
    
    -- Índices
    INDEX idx_auditlog_hash (current_hash),
    INDEX idx_auditlog_entity (entity, entityid)
);

-- Trigger para calcular previous_hash automáticamente
CREATE OR REPLACE FUNCTION calculate_previous_hash()
RETURNS TRIGGER AS $$
DECLARE
    last_hash VARCHAR(64);
BEGIN
    SELECT current_hash INTO last_hash
    FROM cor_auditlog
    ORDER BY idxauditlog DESC
    LIMIT 1;
    
    NEW.previous_hash := last_hash;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_audit_hash_chain
    BEFORE INSERT ON cor_auditlog
    FOR EACH ROW
    EXECUTE FUNCTION calculate_previous_hash();
```

**Casos de uso:**
- ✅ Art. 19: Hash chain logs inmutables
- ✅ GDPR: Pseudonimización datos personales
- ✅ Firmas digitales: Validar integridad documentos
- ✅ Passwords: bcrypt/scrypt via pgcrypto

---

## Extensiones de Rendimiento

### 3. `timescaledb` - Series Temporales

**Por qué:**
- Logs de evaluación: millones de registros por día
- Métricas temporales: accuracy, bias, drift
- Compresión 10x + queries 100x más rápidos

**Funciones principales:**
```sql
-- Convertir tabla a hypertable (particiona por tiempo)
SELECT create_hypertable('eval_modelmetrics', 'createdat');

-- Políticas de retención automática
SELECT add_retention_policy('eval_modelmetrics', INTERVAL '1 year');

-- Compresión automática (datos > 7 días)
SELECT add_compression_policy('eval_modelmetrics', INTERVAL '7 days');

-- Continuous aggregates (vistas materializadas incrementales)
CREATE MATERIALIZED VIEW eval_daily_stats
WITH (timescaledb.continuous) AS
SELECT
    time_bucket('1 day', createdat) AS day,
    idxmodel,
    AVG(accuracy) AS avg_accuracy,
    MAX(accuracy) AS max_accuracy,
    COUNT(*) AS eval_count
FROM eval_modelmetrics
GROUP BY day, idxmodel;
```

**Tablas candidatas a hypertables:**
```sql
-- Métricas de evaluación (millones de filas)
SELECT create_hypertable('eval_modelmetrics', 'createdat');
SELECT create_hypertable('eval_biasmetrics', 'createdat');

-- Logs de predicción (alto volumen)
SELECT create_hypertable('cor_predictionlog', 'timestamp');

-- Audit logs (append-only)
SELECT create_hypertable('cor_auditlog', 'timestamp');

-- Drift detection (series temporales)
SELECT create_hypertable('eval_driftmetrics', 'createdat');
```

**Casos de uso:**
- ✅ Gráficas temporales: accuracy/bias últimos 30 días
- ✅ Alertas drift: detectar degradación rendimiento
- ✅ Reportes compliance: Art. 15 (logs 10 años)
- ✅ Ahorro storage: compresión 90% datos antiguos

---

### 4. `vector` - Embeddings y RAG

**Por qué:**
- RAG: búsqueda semántica documentos/chunks
- Deduplicación: detectar prompts similares
- Clustering: agrupar modelos por funcionalidad

**Funciones principales:**
```sql
-- Crear columna vector (dimensión depende del modelo)
ALTER TABLE rag_chunks ADD COLUMN embedding vector(1536);  -- OpenAI ada-002
ALTER TABLE rag_chunks ADD COLUMN embedding vector(384);   -- all-MiniLM-L6-v2

-- Índice HNSW (Hierarchical Navigable Small World) - RÁPIDO
CREATE INDEX idx_chunks_embedding_hnsw 
ON rag_chunks 
USING hnsw (embedding vector_cosine_ops);

-- Índice IVFFlat (menos preciso, más rápido en millones de vectores)
CREATE INDEX idx_chunks_embedding_ivfflat 
ON rag_chunks 
USING ivfflat (embedding vector_cosine_ops)
WITH (lists = 100);

-- Búsqueda K-NN (k vecinos más cercanos)
SELECT chunktext, 1 - (embedding <=> '[0.1, 0.2, ...]'::vector) AS similarity
FROM rag_chunks
ORDER BY embedding <=> '[0.1, 0.2, ...]'::vector
LIMIT 10;
```

**Operadores de distancia:**
```sql
-- Cosine distance (0 = idénticos, 2 = opuestos)
embedding <=> query_vector

-- L2 distance (Euclidean)
embedding <-> query_vector

-- Inner product (dot product)
embedding <#> query_vector
```

**Casos de uso:**
- ✅ RAG: retrieval documentos relevantes (Art. 13 - instrucciones de uso)
- ✅ Deduplicación: detectar prompts duplicados/similares
- ✅ Clustering: agrupar modelos por capacidades
- ✅ Recomendaciones: "modelos similares a este"

---

## Extensiones de Búsqueda

### 5. `pg_trgm` - Búsqueda Difusa Trigrama

**Por qué:**
- Búsqueda tolerante a typos: "accuraccy" → "accuracy"
- Autocompletado: "llam" → "llama3_70b", "llama2_13b"
- Búsqueda sin coincidencia exacta

**Funciones principales:**
```sql
-- Similitud (0.0 = sin relación, 1.0 = idénticos)
SELECT similarity('llama3', 'llama2');  -- 0.6
SELECT similarity('gpt4', 'gpt-4');     -- 0.8

-- Búsqueda por similitud
SELECT modelname, similarity(modelname, 'llam') AS sim
FROM cor_model
WHERE modelname % 'llam'  -- Operador % = similar
ORDER BY sim DESC
LIMIT 10;

-- Índices GIN para búsqueda rápida
CREATE INDEX idx_model_name_trgm ON cor_model USING gin (modelname gin_trgm_ops);
```

**Casos de uso:**
- ✅ Búsqueda modelos: tolerante a typos
- ✅ Autocompletado: UI búsqueda
- ✅ Deduplicación: "GPT-4" vs "gpt4" vs "GPT 4"

---

### 6. `fuzzystrmatch` - Matching Difuso

**Funciones principales:**
```sql
-- Levenshtein distance (número de cambios para igualar)
SELECT levenshtein('llama3', 'llama2');  -- 1 (cambiar '3' por '2')

-- Soundex (fonético, inglés)
SELECT soundex('Robert'), soundex('Rupert');  -- 'R163', 'R163' (suenan igual)

-- Metaphone (mejor que soundex)
SELECT metaphone('accurate', 8);  -- 'AKKRT'
```

**Casos de uso:**
- ✅ Búsqueda fonética: nombres usuarios
- ✅ Corrección ortográfica: sugerencias
- ✅ Merge usuarios: detectar duplicados

---

### 7. `unaccent` - Sin Acentos

**Por qué:**
- Búsqueda "etica" → "ética"
- Normalización español/francés/portugués

```sql
-- Quitar acentos
SELECT unaccent('Clasificación ética');  -- 'Clasificacion etica'

-- Índice funcional para búsqueda sin acentos
CREATE INDEX idx_model_desc_unaccent 
ON cor_model (unaccent(lower(modeldescription)));

-- Búsqueda case-insensitive sin acentos
SELECT * FROM cor_model
WHERE unaccent(lower(modeldescription)) LIKE '%etica%';
```

**Casos de uso:**
- ✅ Búsqueda multilingüe
- ✅ Normalización textos español

---

## Extensiones de Estructura

### 8. `ltree` - Linaje Jerárquico Modelos

**Por qué ES CRÍTICO:**
- **Art. 53 GPAI**: Documentar modificaciones sustanciales
- Linaje modelos: base → fine-tune → adapter → merge
- Trazabilidad completa cadena entrenamiento

**Sintaxis ltree:**
```
-- Formato: nivel1.nivel2.nivel3.nivel4
'llama3_70b'                                    -- Modelo base
'llama3_70b.finetuned_medical'                  -- Fine-tune
'llama3_70b.finetuned_medical.adapter_spanish'  -- Adapter
'llama3_70b.finetuned_medical.merged_legal'     -- Merge
```

**Operadores:**
```sql
-- @ : ancestor/descendant (descendiente de)
'llama3_70b.finetuned_medical' @ 'llama3_70b'  -- true

-- ~ : match pattern
'llama3_70b.finetuned_medical' ~ '*.finetuned_*'  -- true

-- @ : ancestor (ancestro de)
'llama3_70b' @ 'llama3_70b.finetuned_medical'  -- true

-- <@ : is descendant (es descendiente)
'llama3_70b.finetuned_medical' <@ 'llama3_70b'  -- true

-- @> : is ancestor (es ancestro)
'llama3_70b' @> 'llama3_70b.finetuned_medical'  -- true
```

**Tabla de linaje:**
```sql
CREATE TABLE cor_model_lineage (
    idxlineage BIGSERIAL PRIMARY KEY,
    iduuid UUID NOT NULL DEFAULT uuid_generate_v4(),
    idxmodel BIGINT NOT NULL REFERENCES cor_model(idxmodel),
    
    -- Ruta jerárquica (critical!)
    lineage_path ltree NOT NULL,
    
    -- Tipo de modificación (Art. 53)
    modification_type VARCHAR(50) NOT NULL,  -- 'BASE', 'FINETUNE', 'ADAPTER', 'MERGE'
    parent_idxmodel BIGINT REFERENCES cor_model(idxmodel),
    
    -- Metadatos
    modification_date TIMESTAMP NOT NULL DEFAULT NOW(),
    modification_description TEXT,
    substantial BOOLEAN DEFAULT true,  -- Art. 53: ¿Modificación sustancial?
    
    -- Índices
    INDEX idx_lineage_path_gist USING gist (lineage_path),
    INDEX idx_lineage_model (idxmodel)
);

-- Queries poderosas:
-- 1. Todos los descendientes de un modelo base
SELECT m.modelname, l.lineage_path, nlevel(l.lineage_path) AS depth
FROM cor_model_lineage l
JOIN cor_model m ON l.idxmodel = m.idxmodel
WHERE lineage_path <@ 'llama3_70b'
ORDER BY lineage_path;

-- 2. Todos los ancestros (path completo hasta base)
SELECT m.modelname, l.lineage_path
FROM cor_model_lineage l
JOIN cor_model m ON l.idxmodel = m.idxmodel
WHERE lineage_path @> 'llama3_70b.finetuned_medical.adapter_spanish'
ORDER BY nlevel(lineage_path);

-- 3. Profundidad del árbol (base=1, adapter=3)
SELECT modelname, nlevel(lineage_path) AS depth
FROM cor_model_lineage l
JOIN cor_model m ON l.idxmodel = m.idxmodel;

-- 4. Todos los fine-tunes (nivel 2)
SELECT m.modelname
FROM cor_model_lineage l
JOIN cor_model m ON l.idxmodel = m.idxmodel
WHERE nlevel(lineage_path) = 2 AND lineage_path ~ '*.finetuned_*';

-- 5. Hermanos (mismo padre)
SELECT m.modelname, l.lineage_path
FROM cor_model_lineage l
JOIN cor_model m ON l.idxmodel = m.idxmodel
WHERE subpath(lineage_path, 0, -1) = subpath('llama3_70b.finetuned_medical', 0, -1)
  AND lineage_path != 'llama3_70b.finetuned_medical';
```

**Casos de uso:**
- ✅ **Art. 53**: Documentar linaje GPAI completo
- ✅ Trazabilidad: "¿De dónde viene este modelo?"
- ✅ Impact analysis: "¿Qué modelos dependen de este?"
- ✅ Compliance: Demostrar chain of custody
- ✅ Visualización: Árbol de modelos en UI

---

### 9. `hstore` - Key-Value Metadatos

**Por qué:**
- Metadatos flexibles sin esquema rígido
- Hyperparámetros variables por modelo
- Configuraciones dinámicas

```sql
-- Columna hstore
ALTER TABLE cor_model ADD COLUMN hyperparameters hstore;

-- Insert key-value
INSERT INTO cor_model (modelname, hyperparameters)
VALUES ('llama3_finetune', 
        'learning_rate=>0.0001, batch_size=>32, epochs=>5'::hstore);

-- Queries
SELECT * FROM cor_model WHERE hyperparameters->'learning_rate' = '0.0001';
SELECT * FROM cor_model WHERE hyperparameters ? 'batch_size';  -- Tiene clave
SELECT * FROM cor_model WHERE hyperparameters @> 'epochs=>5';   -- Contiene par

-- Índice GIN
CREATE INDEX idx_model_hyperparams ON cor_model USING gin (hyperparameters);
```

**Casos de uso:**
- ✅ Hyperparámetros flexibles
- ✅ Metadatos de evaluación
- ✅ Configuraciones deployment

---

### 10. `tablefunc` - Reportes Pivote

**Por qué:**
- Reportes compliance (métricas por modelo/mes)
- Dashboards ejecutivos

```sql
-- Crosstab: métricas por modelo y mes
SELECT * FROM crosstab(
    'SELECT modelname, 
            extract(month from createdat) AS month,
            avg(accuracy)
     FROM eval_modelmetrics
     GROUP BY modelname, month
     ORDER BY 1, 2',
    'SELECT generate_series(1, 12)'
) AS ct(modelname text, jan numeric, feb numeric, mar numeric, ...);
```

**Casos de uso:**
- ✅ Reportes compliance mensuales
- ✅ Dashboards ejecutivos
- ✅ Análisis tendencias

---

## Extensiones de Monitoring

### 11. `pg_stat_statements` - Análisis Queries

**Por qué:**
- Identificar queries lentas
- Optimización índices
- Monitoring producción

```sql
-- Ver queries más lentas
SELECT 
    query,
    calls,
    mean_exec_time,
    max_exec_time,
    rows / calls AS avg_rows
FROM pg_stat_statements
ORDER BY mean_exec_time DESC
LIMIT 20;
```

**Casos de uso:**
- ✅ Optimización rendimiento
- ✅ Identificar queries sin índices
- ✅ Capacity planning

---

### 12. `dblink` - Conexiones Remotas

**Por qué:**
- Multi-tenant: bases de datos separadas por cliente
- Federated queries: agregar datos de múltiples DBs

```sql
-- Conectar a otra base PostgreSQL
SELECT * FROM dblink(
    'host=pg2.example.com dbname=client1 user=readonly',
    'SELECT modelname, accuracy FROM eval_modelmetrics'
) AS t(modelname text, accuracy numeric);
```

**Casos de uso:**
- ✅ Multi-tenant con DB separadas
- ✅ Reportes agregados cross-tenant
- ✅ Migraciones datos

---

## Integración con EU AI Act

### Artículo 19 - Logs Inmutables

**Extensiones:**
- `pgcrypto`: Hash chains
- `timescaledb`: Retención 10 años comprimido

```sql
CREATE TABLE cor_auditlog (
    -- Hash chain usando pgcrypto
    previous_hash VARCHAR(64),
    current_hash VARCHAR(64) GENERATED ALWAYS AS (
        encode(digest(previous_hash || action || timestamp::TEXT, 'sha256'), 'hex')
    ) STORED
);

-- Hypertable para compresión
SELECT create_hypertable('cor_auditlog', 'timestamp');
SELECT add_retention_policy('cor_auditlog', INTERVAL '10 years');
```

### Artículo 53 - Linaje GPAI

**Extensión:**
- `ltree`: Linaje jerárquico

```sql
-- Documentar modificación sustancial
INSERT INTO cor_model_lineage (idxmodel, lineage_path, modification_type, substantial)
VALUES (
    123,
    'llama3_70b.finetuned_medical',
    'FINETUNE',
    true  -- Modificación sustancial Art. 53
);
```

### Artículo 13 - Instrucciones de Uso

**Extensión:**
- `vector`: RAG búsqueda instrucciones

```sql
-- Buscar chunks relevantes para instrucciones
SELECT chunktext 
FROM rag_chunks
WHERE doctype = 'instructions_for_use'
ORDER BY embedding <=> query_embedding
LIMIT 5;
```

---

## Verificación Instalación

```sql
-- Verificar todas las extensiones
SELECT 
    extname AS extension,
    extversion AS version,
    CASE 
        WHEN extname IN ('uuid-ossp', 'pgcrypto') THEN 'CRÍTICA'
        WHEN extname IN ('timescaledb', 'vector') THEN 'CORE'
        WHEN extname IN ('ltree', 'pg_trgm') THEN 'ALTA'
        ELSE 'MEDIA'
    END AS priority
FROM pg_extension
WHERE extname IN (
    'uuid-ossp', 'pgcrypto', 'timescaledb', 'vector',
    'pg_trgm', 'fuzzystrmatch', 'unaccent',
    'btree_gin', 'btree_gist', 'ltree', 'hstore',
    'tablefunc', 'pg_stat_statements', 'dblink',
    'cube', 'earthdistance'
)
ORDER BY 
    CASE priority 
        WHEN 'CRÍTICA' THEN 1
        WHEN 'CORE' THEN 2
        WHEN 'ALTA' THEN 3
        ELSE 4
    END,
    extname;
```

---

## Referencias

- **TimescaleDB**: https://docs.timescale.com/
- **pgvector**: https://github.com/pgvector/pgvector
- **ltree**: https://www.postgresql.org/docs/current/ltree.html
- **pgcrypto**: https://www.postgresql.org/docs/current/pgcrypto.html
- **EU AI Act**: Art. 19 (logs), Art. 53 (GPAI)

