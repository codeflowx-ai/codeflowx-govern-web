# PROMPTS_14 - CORRECCIÓN NOMBRES TABLAS REALES

## 🔍 Análisis Tablas Existentes

He revisado las entidades JPA en `nocode.service.entitys` y encontré las siguientes **tablas reales**:

---

## ✅ TABLAS YA EXISTENTES (NO CREAR)

### 1. **Audit Logs Inmutables (Art. 19)** - ✅ YA EXISTE

```java
@Table(name = "IMLIMMUTABLELOGS")
@Entidad(namespace = "logging", pk = "IDXIMMUTABLELOG")
public class ImmutableLog
```

**Columnas principales:**
- `IDXIMMUTABLELOG` (PK)
- `IMLPREVIOUSHASH` → Hash anterior
- `IMLCURRENTHASH` → Hash actual (SHA-256)
- `IMLTIMESTAMP` → Timestamp
- `IMLACTION` → Acción (CREATE, UPDATE, DELETE)
- `IMLENTITYTYPE` → Tipo entidad
- `IMLENTITYID` → ID entidad
- `IMLUSERID` → Usuario

**Estado:** ✅ **COMPLETAMENTE IMPLEMENTADA** con hash chain
- ✅ Triggers para prevenir UPDATE/DELETE
- ✅ Hash SHA-256 automático
- ✅ Comentarios EU AI Act Art. 19

**Acción PROMPTS_14:** 
- **PROMPT 14.2**: ~~Crear tabla~~ → **ACTUALIZAR** para añadir extensiones PostgreSQL (TimescaleDB hypertable, índices optimizados)
- **PROMPT 14.3**: `verify_audit_chain()` función → **CREAR** (no existe aún)

---

### 2. **Modelos** - ✅ YA EXISTE

```java
@Table(name = "MODMODELS")
@Entidad(namespace = "models", pk = "IDXMODEL")
public class Model
```

**Columnas principales:**
- `IDXMODEL` (PK)
- `MODNAME` → Nombre
- `MODDESCRIPTION` → Descripción
- `MODTYPE` → Tipo (LLM, VISION, etc.)
- `MODFRAMEWORK` → Framework
- `MODVERSION` → Versión
- `MODSTATUS` → Estado
- `MODRISKCLASSIFICATION` → Clasificación riesgo (HIGH_RISK, etc.)
- `MODGPAICLASSIFICATION` → GPAI classification

**Acción PROMPTS_14:**
- **NO MODIFICAR** tabla existente
- **PROMPT 14.8**: Añadir columna `capabilities_embedding vector(384)` para clustering modelos
- **PROMPT 14.10**: Añadir columna `hyperparameters hstore` para metadatos flexibles

---

### 3. **Evaluaciones** - ✅ YA EXISTE

```java
@Table(name = "GOVMODELEVALUATIONS")
@Entidad(namespace = "evaluation", pk = "IDXMODELEVALUATION")
public class ModelEvaluation

@Table(name = "MODMODELPERFORMANCES")
@Entidad(namespace = "evaluation", pk = "IDXMODELPERFORMANCE")
public class ModelPerformance
```

**Acción PROMPTS_14:**
- **PROMPT 14.6**: Convertir a hypertables TimescaleDB
- **PROMPT 14.7**: Crear continuous aggregates (métricas diarias)

---

### 4. **Prompts** - ✅ YA EXISTE

```java
@Table(name = "PRMPROMPTS")
@Entidad(namespace = "prompts", pk = "IDXPROMPT")
public class Prompt
```

**Columnas principales:**
- `IDXPROMPT` (PK)
- `PRMNAME` → Nombre
- `PRMDESCRIPTION` → Descripción
- `PRMTYPE` → Tipo
- `PRMTEXT` → Texto prompt

**Acción PROMPTS_14:**
- **PROMPT 14.8**: Añadir columna `embedding vector(1536)` para deduplicación
- **PROMPT 14.9**: Añadir índice `pg_trgm` en `PRMTEXT` para búsqueda fuzzy

---

### 5. **RAG Systems** - ✅ PARCIALMENTE EXISTE

```java
@Table(name = "RAGSYSTEMS")
@Table(name = "RAGDATASOURCES")
@Table(name = "RAGVERSIONS")
@Table(name = "RAGROLLBACKS")
```

**Problema:** NO existen tablas para:
- ❌ RAG Documents
- ❌ RAG Chunks
- ❌ Embeddings

**Acción PROMPTS_14:**
- **CREAR NUEVAS TABLAS**: `RAGDOCUMENTS`, `RAGCHUNKS` (ver abajo)

---

## 🆕 TABLAS QUE NECESITAN CREARSE

### 1. **Linaje Modelos (Art. 53 GPAI)** - ❌ NO EXISTE

**Nombre tabla:** `MODMODELLINEAGE` (prefijo MOD_ de models)

```sql
CREATE TABLE MODMODELLINEAGE (
    IDXMODELLINEAGE BIGSERIAL PRIMARY KEY,
    iduuid UUID NOT NULL DEFAULT uuid_generate_v4() UNIQUE,
    
    -- Referencias
    IDXMODEL BIGINT NOT NULL REFERENCES MODMODELS(IDXMODEL),
    MODPARENTIDX BIGINT REFERENCES MODMODELS(IDXMODEL),
    
    -- Linaje jerárquico (ltree) - CRÍTICO ART. 53
    MODLINEAGEPATH ltree NOT NULL UNIQUE,
    MODPARENTPATH ltree,
    
    -- Tipo modificación
    MODMODIFICATIONTYPE VARCHAR(50) NOT NULL,  -- 'BASE', 'FINETUNE', 'ADAPTER', 'MERGE'
    MODSUBSTANTIAL BOOLEAN NOT NULL DEFAULT true,
    MODSUBSTANTIALJUSTIFICATION TEXT,
    
    -- Metadata modificación
    MODMODIFICATIONDATE TIMESTAMP NOT NULL DEFAULT NOW(),
    MODMODIFICATIONDESC TEXT,
    
    -- Métricas cambio
    MODPARAMETERCHANGEPCT NUMERIC(5,2),
    MODPERFORMANCEDELTA NUMERIC(10,4),
    
    -- Compliance
    MODREQUIRESGPAIASSESSMENT BOOLEAN GENERATED ALWAYS AS (
        MODSUBSTANTIAL AND MODMODIFICATIONTYPE IN ('FINETUNE', 'MERGE')
    ) STORED,
    
    -- Metadata
    createdat TIMESTAMP NOT NULL DEFAULT NOW(),
    updatedat TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_mod_lineage_type CHECK (
        MODMODIFICATIONTYPE IN ('BASE', 'FINETUNE', 'ADAPTER', 'MERGE', 'QUANTIZATION', 'PRUNING')
    ),
    CONSTRAINT chk_mod_base_no_parent CHECK (
        MODMODIFICATIONTYPE != 'BASE' OR MODPARENTIDX IS NULL
    )
);

-- Índices
CREATE INDEX idx_mod_lineage_model ON MODMODELLINEAGE(IDXMODEL);
CREATE INDEX idx_mod_lineage_parent ON MODMODELLINEAGE(MODPARENTIDX);
CREATE INDEX idx_mod_lineage_path_gist ON MODMODELLINEAGE USING gist(MODLINEAGEPATH);
CREATE INDEX idx_mod_lineage_type ON MODMODELLINEAGE(MODMODIFICATIONTYPE);
CREATE INDEX idx_mod_lineage_substantial ON MODMODELLINEAGE(MODSUBSTANTIAL) WHERE MODSUBSTANTIAL = true;
```

**Triggers:**
```sql
-- Trigger: Auto-populate MODPARENTPATH
CREATE OR REPLACE FUNCTION populate_mod_parent_lineage()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.MODPARENTIDX IS NOT NULL THEN
        SELECT MODLINEAGEPATH INTO NEW.MODPARENTPATH
        FROM MODMODELLINEAGE
        WHERE IDXMODEL = NEW.MODPARENTIDX;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_populate_mod_parent_lineage
    BEFORE INSERT OR UPDATE ON MODMODELLINEAGE
    FOR EACH ROW
    WHEN (NEW.MODPARENTIDX IS NOT NULL)
    EXECUTE FUNCTION populate_mod_parent_lineage();

-- Trigger: Validar lineage_path coherente
CREATE OR REPLACE FUNCTION validate_mod_lineage_path()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.MODPARENTPATH IS NOT NULL THEN
        IF NOT (NEW.MODLINEAGEPATH <@ NEW.MODPARENTPATH) THEN
            RAISE EXCEPTION 'Lineage path must be descendant of parent path';
        END IF;
    END IF;
    
    IF NEW.MODMODIFICATIONTYPE = 'BASE' AND nlevel(NEW.MODLINEAGEPATH) != 1 THEN
        RAISE EXCEPTION 'BASE models must have lineage_path depth = 1';
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_mod_lineage_path
    BEFORE INSERT OR UPDATE ON MODMODELLINEAGE
    FOR EACH ROW
    EXECUTE FUNCTION validate_mod_lineage_path();
```

**Acción PROMPTS_14:**
- **PROMPT 14.4**: Crear tabla `MODMODELLINEAGE`
- **PROMPT 14.5**: Crear vistas/funciones linaje

---

### 2. **RAG Documents** - ❌ NO EXISTE

**Nombre tabla:** `RAGDOCUMENTS` (prefijo RAG_)

```sql
CREATE TABLE RAGDOCUMENTS (
    IDXRAGDOCUMENT BIGSERIAL PRIMARY KEY,
    iduuid UUID NOT NULL DEFAULT uuid_generate_v4() UNIQUE,
    
    -- Referencias
    IDXRAGSYSTEM BIGINT NOT NULL REFERENCES RAGSYSTEMS(IDXRAGSYSTEM),
    IDXMODEL BIGINT REFERENCES MODMODELS(IDXMODEL),  -- Modelo asociado (instrucciones de uso, etc.)
    
    -- Documento
    RAGDOCNAME VARCHAR(500) NOT NULL,
    RAGDOCTYPE VARCHAR(50) NOT NULL,  -- 'INSTRUCTIONS_FOR_USE', 'USER_MANUAL', 'TECHNICAL_DOC'
    RAGDOCPATH TEXT,
    RAGDOCURL TEXT,
    RAGDOCMIMETYPE VARCHAR(100),
    RAGDOCSIZE BIGINT,
    RAGDOCHASH VARCHAR(64),  -- SHA-256 del documento
    
    -- Metadata
    RAGDOCMETADATA JSONB,
    RAGDOCLANGUAGE VARCHAR(10),
    
    -- Estado
    RAGDOCSTATUS VARCHAR(50) NOT NULL DEFAULT 'PENDING',  -- 'PENDING', 'PROCESSING', 'INDEXED', 'ERROR'
    RAGDOCERROR TEXT,
    
    -- Timestamps
    createdat TIMESTAMP NOT NULL DEFAULT NOW(),
    updatedat TIMESTAMP,
    indexedat TIMESTAMP
);

-- Índices
CREATE INDEX idx_rag_doc_system ON RAGDOCUMENTS(IDXRAGSYSTEM);
CREATE INDEX idx_rag_doc_model ON RAGDOCUMENTS(IDXMODEL);
CREATE INDEX idx_rag_doc_type ON RAGDOCUMENTS(RAGDOCTYPE);
CREATE INDEX idx_rag_doc_status ON RAGDOCUMENTS(RAGDOCSTATUS);
CREATE INDEX idx_rag_doc_hash ON RAGDOCUMENTS(RAGDOCHASH);
```

---

### 3. **RAG Chunks** - ❌ NO EXISTE

**Nombre tabla:** `RAGCHUNKS` (prefijo RAG_)

```sql
CREATE TABLE RAGCHUNKS (
    IDXRAGCHUNK BIGSERIAL PRIMARY KEY,
    iduuid UUID NOT NULL DEFAULT uuid_generate_v4() UNIQUE,
    
    -- Referencia documento
    IDXRAGDOCUMENT BIGINT NOT NULL REFERENCES RAGDOCUMENTS(IDXRAGDOCUMENT) ON DELETE CASCADE,
    
    -- Chunk
    RAGCHUNKTEXT TEXT NOT NULL,
    RAGCHUNKORDER INT NOT NULL,  -- Orden en el documento
    RAGCHUNKSTARTPOS INT,
    RAGCHUNKENDPOS INT,
    RAGCHUNKSIZE INT,
    
    -- Embedding (pgvector) - CRÍTICO PARA RAG
    RAGCHUNKEMBEDDING vector(1536),  -- OpenAI ada-002 o similar
    
    -- Metadata
    RAGCHUNKMETADATA JSONB,
    RAGCHUNKHEADINGLEVEL INT,  -- Si es encabezado (1-6)
    RAGCHUNKTYPE VARCHAR(50),  -- 'TEXT', 'CODE', 'TABLE', 'LIST'
    
    -- Timestamps
    createdat TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Índices
CREATE INDEX idx_rag_chunk_document ON RAGCHUNKS(IDXRAGDOCUMENT);
CREATE INDEX idx_rag_chunk_order ON RAGCHUNKS(IDXRAGDOCUMENT, RAGCHUNKORDER);

-- Índice HNSW para búsqueda semántica (CRÍTICO RENDIMIENTO)
CREATE INDEX idx_rag_chunk_embedding_hnsw 
ON RAGCHUNKS 
USING hnsw (RAGCHUNKEMBEDDING vector_cosine_ops)
WITH (m = 16, ef_construction = 64);
```

**Función búsqueda semántica:**
```sql
CREATE OR REPLACE FUNCTION rag_search_chunks(
    query_embedding vector(1536),
    doc_types TEXT[] DEFAULT NULL,
    idxmodel_filter BIGINT DEFAULT NULL,
    top_k INT DEFAULT 10,
    similarity_threshold FLOAT DEFAULT 0.7
)
RETURNS TABLE (
    IDXRAGCHUNK BIGINT,
    RAGCHUNKTEXT TEXT,
    similarity FLOAT,
    RAGDOCTYPE VARCHAR,
    RAGDOCNAME VARCHAR,
    MODNAME VARCHAR,
    RAGCHUNKMETADATA JSONB
) AS $$
    SELECT 
        c.IDXRAGCHUNK,
        c.RAGCHUNKTEXT,
        1 - (c.RAGCHUNKEMBEDDING <=> query_embedding) AS similarity,
        d.RAGDOCTYPE,
        d.RAGDOCNAME,
        m.MODNAME,
        c.RAGCHUNKMETADATA
    FROM RAGCHUNKS c
    JOIN RAGDOCUMENTS d ON c.IDXRAGDOCUMENT = d.IDXRAGDOCUMENT
    LEFT JOIN MODMODELS m ON d.IDXMODEL = m.IDXMODEL
    WHERE 
        (doc_types IS NULL OR d.RAGDOCTYPE = ANY(doc_types))
        AND (idxmodel_filter IS NULL OR m.IDXMODEL = idxmodel_filter)
        AND (1 - (c.RAGCHUNKEMBEDDING <=> query_embedding)) >= similarity_threshold
    ORDER BY c.RAGCHUNKEMBEDDING <=> query_embedding
    LIMIT top_k;
$$ LANGUAGE SQL STABLE;
```

**Acción PROMPTS_14:**
- **PROMPT 14.8**: Crear tablas `RAGDOCUMENTS` y `RAGCHUNKS`
- **PROMPT 14.8**: Crear función `rag_search_chunks()`

---

## 📝 RESUMEN CORRECCIONES PROMPTS_14

| Prompt | Acción Original | Acción Corregida |
|--------|-----------------|------------------|
| **14.1** | Migrar UUIDs | ✅ OK (aplica a TODAS las tablas) |
| **14.2** | Crear `cor_auditlog` | ❌ Cambiar a: **ACTUALIZAR `IMLIMMUTABLELOGS`** (ya existe) |
| **14.3** | Función `verify_audit_chain()` | ✅ OK (usar `IMLIMMUTABLELOGS`) |
| **14.4** | Crear `cor_model_lineage` | ✅ Cambiar nombre a: **`MODMODELLINEAGE`** |
| **14.5** | Vistas linaje | ✅ OK (usar `MODMODELLINEAGE`) |
| **14.6** | Hypertables | ✅ OK (usar `IMLIMMUTABLELOGS`, `GOVMODELEVALUATIONS`, `MODMODELPERFORMANCES`) |
| **14.7** | Continuous aggregates | ✅ OK (usar tablas reales) |
| **14.8** | pgvector RAG | ✅ **CREAR `RAGDOCUMENTS` y `RAGCHUNKS`** (no existen) |
| **14.9** | pg_trgm índices | ✅ OK (usar `MODMODELS`, `PRMPROMPTS`) |
| **14.10** | hstore metadatos | ✅ OK (añadir a `MODMODELS`, `GOVMODELEVALUATIONS`) |
| **14.11** | Índices compuestos | ✅ OK |
| **14.12** | Funciones utilidad | ✅ OK |

---

## 🔄 CONVENCIÓN NOMBRADO ENART

### Reglas identificadas:

1. **Tablas:** `[PREFIJO3][NOMBRE]` (ej: `MODMODELS`, `IMLIMMUTABLELOGS`, `PRMPROMPTS`)

2. **Prefijos módulos:**
   - `MOD_` → models
   - `IML_` → immutable logs
   - `GOV_` → governance
   - `PRM_` → prompts
   - `AGT_` → agents
   - `RAG_` → rag
   - `CMP_` → compliance (GDPR)
   - `COM_` → compliance (assessments)
   - `FRIA_` → FRIA assessments
   - `REG_` → registrations
   - `ANN_` → annexes

3. **Columnas:** `[PREFIJO3][NOMBRE_CAMPO]` (ej: `MODNAME`, `IMLACTION`, `PRMTEXT`)

4. **Primary Keys:** `IDXNOMBRE` (ej: `IDXMODEL`, `IDXIMMUTABLELOG`, `IDXPROMPT`)

5. **UUID:** Siempre columna `iduuid UUID` (lowercase, sin prefijo)

6. **Timestamps:** `createdat`, `updatedat` (lowercase, sin prefijo)

---

## ✅ ACCIÓN INMEDIATA

**Necesito actualizar PROMPTS_14_INTEGRACION_EXTENSIONES_POSTGRESQL.md con:**

1. ✅ Usar `IMLIMMUTABLELOGS` en lugar de `cor_auditlog`
2. ✅ Crear `MODMODELLINEAGE` en lugar de `cor_model_lineage`
3. ✅ Crear `RAGDOCUMENTS` y `RAGCHUNKS` (nuevas tablas)
4. ✅ Usar nombres reales: `MODMODELS`, `GOVMODELEVALUATIONS`, `PRMPROMPTS`
5. ✅ Respetar convención columnas: `MODNAME`, `IMLACTION`, `PRMTEXT`

---

**¿Quieres que actualice ahora PROMPTS_14_INTEGRACION_EXTENSIONES_POSTGRESQL.md con estos nombres correctos?**

