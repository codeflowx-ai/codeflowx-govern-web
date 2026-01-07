# Unificación del Módulo de Gestión de Gobierno del Dato

**Fecha:** Diciembre 2025
**Estado:** Propuesta de Arquitectura
**Objetivo:** Unificar la gestión de datasets, orígenes de datos (externos e internos) y gobierno del dato en un módulo centralizado

## 🎯 ESTÁNDAR DE DATOS

**Formato Estándar:** Apache Parquet

Todos los datasets descargados de fuentes externas (HuggingFace, Kaggle, etc.) o internas serán normalizados y almacenados en formato **Apache Parquet** para garantizar:

- ✅ Análisis uniforme por microservicios Python
- ✅ Eficiencia en almacenamiento y procesamiento
- ✅ Schema validado y embebido
- ✅ Compatibilidad con pandas, PyArrow, Spark
- ✅ Compresión integrada

**Ver documentación completa:** `MODELO_DATASET_ESTANDARIZADO.md`

---

## 📋 RESUMEN EJECUTIVO

Actualmente, la gestión de datos está dispersa en múltiples módulos:

1. **DSDDATASETS** - Datasets de gobernanza (compliance/ODS)
2. **RAGDATASOURCES** - Fuentes de datos para sistemas RAG
3. **TRNDATASETSOURCES** - Fuentes de datasets para entrenamiento

**Propuesta:** Crear un módulo unificado **"Data Governance"** que centralice:
- Gestión de datasets (entrenamiento, validación, test, producción)
- Orígenes de datos (externos e internos)
- Gobierno del dato (calidad, sesgos, trazabilidad)
- Compliance y auditoría de datos

---

## 🔍 ANÁLISIS DE LA SITUACIÓN ACTUAL

### 1. **DSDDATASETS** (Gobernanza/Compliance)

**Ubicación:** Módulo de Compliance/ODS
**Prefijo:** `DSD`
**Propósito:** Datasets para análisis de compliance (ODS 5, ODS 10)

**Estructura actual:**
```sql
CREATE TABLE DSDDATASETS (
    IDXDATASET BIGSERIAL PRIMARY KEY,
    IDUUID VARCHAR(36) UNIQUE NOT NULL,
    IDXPROJECT BIGINT REFERENCES PRJPROJECTS(IDXPROJECT),
    DSDNAME VARCHAR(255) NOT NULL,
    DSDDESCRIPTION TEXT,
    DSDTYPE VARCHAR(50), -- TRAINING, VALIDATION, TEST

    -- Métricas de género
    DSDGENDERDISTRIBUTION JSONB,
    DSDGENDERBALANCESCORE DECIMAL(5,2),
    DSDGENDERBALANCED BOOLEAN,

    -- Análisis de sesgos
    DSDBIASANALYSIS JSONB,
    DSDBIASANALYZED BOOLEAN DEFAULT false,
    DSDBIASANALYZEDAT TIMESTAMP,

    -- Representatividad
    DSDREPRESENTATIVITYSCORE DECIMAL(5,2),
    DSDREPRESENTATIVITYMETRICS JSONB,

    -- Auditoría
    DSDCREATEDAT TIMESTAMP NOT NULL,
    DSDUPDATEDAT TIMESTAMP,
    DSDCREATEDBY BIGINT
);
```

**Funcionalidades:**
- Análisis de sesgos (género, edad, etnia)
- Cálculo de representatividad
- Métricas de balance de género
- Integración con KPIs ODS

---

### 2. **RAGDATASOURCES** (RAG)

**Ubicación:** Módulo RAG
**Prefijo:** `RAGDS`
**Propósito:** Fuentes de datos para sistemas RAG

**Estructura actual:**
```sql
CREATE TABLE RAGDATASOURCES (
    IDXRAGDATASOURCE BIGSERIAL PRIMARY KEY,
    IDXRAGSYSTEM BIGINT NOT NULL,
    RAGDSSOURCENAME VARCHAR(255) NOT NULL,
    RAGDSSOURCETYPE VARCHAR(50) NOT NULL, -- PostgreSQL, MongoDB, S3, etc.
    RAGDSSOURCEURL VARCHAR(500),
    RAGDSINDEXSTATUS VARCHAR(50) NOT NULL,
    RAGDSDOCUMENTCOUNT INT,
    RAGDSLASTINDEXED TIMESTAMP NULL,
    RAGDSDATACLASS VARCHAR(50),
    RAGDSQUALITYSCORE DECIMAL(5,2),
    RAGDSBIASSCORE DECIMAL(5,2),
    RAGDSACCESS TEXT,

    -- Auditoría
    RAGDSCREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    RAGDSUPDATEDAT TIMESTAMP NULL,

    FOREIGN KEY (IDXRAGSYSTEM) REFERENCES RAGSYSTEMS(IDXRAGSYSTEM) ON DELETE CASCADE
);
```

**Funcionalidades:**
- Configuración de conexión a fuentes externas/internas
- Sincronización e indexación
- Análisis de calidad y sesgos
- Clasificación de datos

---

### 3. **TRNDATASETSOURCES** (Training)

**Ubicación:** Módulo Training
**Prefijo:** `TRN`
**Propósito:** Fuentes de datasets para entrenamiento

**Estructura actual:**
- Referenciada en documentación pero estructura no detallada
- Endpoints API definidos:
  - `GET /api/v1/training/dataset-sources`
  - `POST /api/v1/training/dataset-sources`
  - `POST /api/v1/training/dataset-sources/{id}/sync`
  - Integración con HuggingFace, Kaggle, web scraping

**Funcionalidades:**
- Sincronización con plataformas externas (HuggingFace, Kaggle)
- Web scraping programado
- Gestión de versiones de datasets
- Sincronización automática

---

## 🎯 PROPUESTA DE UNIFICACIÓN

### Módulo: **Data Governance** (`/governance/data`)

**Categoría:** Governance
**Orden:** 6 (después de Prompts)

---

## 📊 ARQUITECTURA PROPUESTA

### 1. **Tabla Unificada de Datasets: `DTGDATASETS`**

**Prefijo:** `DTG` (Data Governance)

```sql
CREATE TABLE DTGDATASETS (
    -- Identificación
    IDXDATASET BIGSERIAL PRIMARY KEY,
    IDUUID VARCHAR(36) UNIQUE NOT NULL,
    DTGNAME VARCHAR(255) NOT NULL,
    DTGDESCRIPTION TEXT,

    -- Clasificación
    DTGTYPE VARCHAR(50) NOT NULL, -- TRAINING, VALIDATION, TEST, PRODUCTION, RAG
    DTGCATEGORY VARCHAR(50), -- STRUCTURED, UNSTRUCTURED, SEMI_STRUCTURED
    DTGFORMAT VARCHAR(50), -- CSV, JSON, PARQUET, IMAGES, TEXT, MIXED

    -- Origen
    DTGORIGINTYPE VARCHAR(50) NOT NULL, -- INTERNAL, EXTERNAL, SYNTHETIC, PUBLIC
    IDXORIGIN BIGINT REFERENCES DTGDATAORIGINS(IDXORIGIN), -- FK a orígenes
    DTGSOURCEURL VARCHAR(500), -- URL o path del origen
    DTGSYNCMETHOD VARCHAR(50), -- MANUAL, SCHEDULED, REAL_TIME, API

    -- Relaciones
    IDXPROJECT BIGINT REFERENCES PRJPROJECTS(IDXPROJECT),
    IDXRAGSYSTEM BIGINT REFERENCES RAGSYSTEMS(IDXRAGSYSTEM), -- Si es RAG
    IDXTRAININGEXECUTION BIGINT REFERENCES TRNTRAININGEXECUTIONS(IDXTRAININGEXECUTION), -- Si es training

    -- Metadatos
    DTGSCHEMA JSONB, -- Esquema de datos (columnas, tipos, etc.)
    DTGMETADATA JSONB, -- Metadatos adicionales
    DTGSTATISTICS JSONB, -- Estadísticas del dataset (count, size, etc.)

    -- Calidad y Compliance
    DTGQUALITYSCORE DECIMAL(5,2), -- 0.00 - 1.00
    DTGQUALITYMETRICS JSONB, -- Métricas de calidad detalladas
    DTGBIASSCORE DECIMAL(5,2), -- 0.00 - 1.00
    DTGBIASANALYSIS JSONB, -- Análisis de sesgos detallado
    DTGBIASANALYZED BOOLEAN DEFAULT false,
    DTGBIASANALYZEDAT TIMESTAMP,

    -- Representatividad (ODS 10)
    DTGREPRESENTATIVITYSCORE DECIMAL(5,2), -- 0.00 - 1.00
    DTGREPRESENTATIVITYMETRICS JSONB, -- Métricas de representatividad

    -- Género (ODS 5)
    DTGGENDERDISTRIBUTION JSONB, -- {"male": 45, "female": 48, "non_binary": 4, "other": 3}
    DTGGENDERBALANCESCORE DECIMAL(5,2), -- 0.00 - 1.00
    DTGGENDERBALANCED BOOLEAN, -- true si balance >= 0.40 para cada género
    DTGGENDERANALYZED BOOLEAN DEFAULT false,

    -- Privacidad y Seguridad
    DTGDATACLASS VARCHAR(50), -- PUBLIC, INTERNAL, CONFIDENTIAL, RESTRICTED
    DTGPII DETECTED BOOLEAN DEFAULT false, -- PII detectado
    DTGPIIANALYSIS JSONB, -- Análisis de PII
    DTGENCRYPTED BOOLEAN DEFAULT false,
    DTGENCRYPTIONMETHOD VARCHAR(50),

    -- Versionado
    DTGVERSION VARCHAR(50) NOT NULL DEFAULT '1.0.0',
    DTGVERSIONHASH VARCHAR(64), -- Hash del contenido para integridad
    IDXPARENTDATASET BIGINT REFERENCES DTGDATASETS(IDXDATASET), -- Para versiones

    -- Estado y Ciclo de Vida
    DTGSTATUS VARCHAR(50) NOT NULL DEFAULT 'DRAFT', -- DRAFT, VALIDATED, APPROVED, ACTIVE, ARCHIVED
    DTGAPPROVED BOOLEAN DEFAULT false,
    DTGAPPROVEDBY BIGINT,
    DTGAPPROVEDAT TIMESTAMP,

    -- Almacenamiento
    DTGSTORAGETYPE VARCHAR(50), -- S3, MINIO, LOCAL, DATABASE
    DTGSTORAGEPATH VARCHAR(500), -- Path en el almacenamiento (formato Parquet estandarizado)
    DTGSIZE BIGINT, -- Tamaño en bytes
    DTGRECORDCOUNT BIGINT, -- Número de registros
    DTGSTANDARDIZED BOOLEAN DEFAULT false, -- Si está estandarizado a Parquet
    DTGSTANDARDIZEDAT TIMESTAMP, -- Fecha de estandarización
    DTGSTANDARDIZEDFORMAT VARCHAR(50) DEFAULT 'PARQUET', -- Formato estandarizado (siempre PARQUET)
    DTGORIGINALFORMAT VARCHAR(50), -- Formato original antes de estandarizar
    DTGORIGINALPATH VARCHAR(500), -- Path del archivo original (backup)

    -- Sincronización (para orígenes externos)
    DTGLASTSYNCED TIMESTAMP,
    DTGLASTSYNCSTATUS VARCHAR(50), -- SUCCESS, FAILED, PENDING
    DTGLASTSYNCERROR TEXT,
    DTGNEXTSYNC TIMESTAMP,

    -- Auditoría
    DTGCREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    DTGCREATEDBY BIGINT,
    DTGUPDATEDAT TIMESTAMP,
    DTGUPDATEDBY BIGINT,
    DTGARCHIVEDAT TIMESTAMP,
    DTGARCHIVEDBY BIGINT
);

-- Índices
CREATE INDEX idx_dtgname ON DTGDATASETS(DTGNAME);
CREATE INDEX idx_dtgtype ON DTGDATASETS(DTGTYPE);
CREATE INDEX idx_dtgorigintype ON DTGDATASETS(DTGORIGINTYPE);
CREATE INDEX idx_dtgstatus ON DTGDATASETS(DTGSTATUS);
CREATE INDEX idx_dtgproject ON DTGDATASETS(IDXPROJECT);
CREATE INDEX idx_dtgorigin ON DTGDATASETS(IDXORIGIN);
CREATE INDEX idx_dtgversion ON DTGDATASETS(DTGVERSION);
CREATE INDEX idx_dtgcreatedat ON DTGDATASETS(DTGCREATEDAT);

COMMENT ON TABLE DTGDATASETS IS 'Datasets unificados de gobernanza del dato';
```

---

### 2. **Tabla de Orígenes de Datos: `DTGDATAORIGINS`**

**Prefijo:** `DTGOR` (Data Governance Origins)

```sql
CREATE TABLE DTGDATAORIGINS (
    -- Identificación
    IDXORIGIN BIGSERIAL PRIMARY KEY,
    IDUUID VARCHAR(36) UNIQUE NOT NULL,
    DTGORNAME VARCHAR(255) NOT NULL,
    DTGORDESCRIPTION TEXT,

    -- Tipo de Origen
    DTGORTYPE VARCHAR(50) NOT NULL, -- INTERNAL, EXTERNAL
    DTGORCATEGORY VARCHAR(50) NOT NULL, -- DATABASE, API, FILE_SYSTEM, CLOUD_STORAGE, MARKETPLACE, WEB

    -- Configuración de Conexión
    DTGORCONNECTIONTYPE VARCHAR(50), -- POSTGRESQL, MONGODB, MYSQL, S3, MINIO, HUGGINGFACE, KAGGLE, REST_API, FTP, SFTP
    DTGORCONNECTIONCONFIG JSONB NOT NULL, -- Configuración de conexión (credenciales, URLs, etc.)
    DTGORCREDENTIALSID BIGINT REFERENCES CREDCREDENTIALS(IDXCREDENTIAL), -- Referencia a credenciales

    -- Ubicación
    DTGORURL VARCHAR(500), -- URL o endpoint
    DTGORPATH VARCHAR(500), -- Path en sistema de archivos
    DTGORHOST VARCHAR(255),
    DTGORPORT INT,
    DTGORDATABASE VARCHAR(255),
    DTGORSCHEMA VARCHAR(255),
    DTGORTABLE VARCHAR(255),
    DTGORCOLLECTION VARCHAR(255),

    -- Autenticación
    DTGORAUTHMETHOD VARCHAR(50), -- NONE, BASIC, OAUTH2, API_KEY, CERTIFICATE
    DTGORAUTHCONFIG JSONB, -- Configuración de autenticación

    -- Sincronización
    DTGORSYNCMETHOD VARCHAR(50) DEFAULT 'MANUAL', -- MANUAL, SCHEDULED, REAL_TIME, WEBHOOK
    DTGORSYNCSCHEDULE VARCHAR(100), -- Cron expression para sincronización programada
    DTGORSYNCFREQUENCY VARCHAR(50), -- HOURLY, DAILY, WEEKLY, MONTHLY, ON_DEMAND
    DTGORLASTSYNCED TIMESTAMP,
    DTGORLASTSYNCSTATUS VARCHAR(50), -- SUCCESS, FAILED, PENDING
    DTGORLASTSYNCERROR TEXT,
    DTGORNEXTSYNC TIMESTAMP,

    -- Validación y Calidad
    DTGORVALIDATED BOOLEAN DEFAULT false,
    DTGORVALIDATIONRULES JSONB, -- Reglas de validación
    DTGORQUALITYSCORE DECIMAL(5,2),

    -- Acceso y Permisos
    DTGORACCESSLEVEL VARCHAR(50) DEFAULT 'PRIVATE', -- PUBLIC, INTERNAL, PRIVATE, RESTRICTED
    DTGORPERMISSIONS JSONB, -- Permisos específicos
    DTGOROWNER BIGINT, -- Usuario/organización propietaria

    -- Organización
    IDXORGANIZATION BIGINT REFERENCES ORGORGANIZATIONS(IDXORGANIZATION),
    IDXPROJECT BIGINT REFERENCES PRJPROJECTS(IDXPROJECT), -- Si es específico de proyecto

    -- Estado
    DTGORSTATUS VARCHAR(50) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, INACTIVE, DEPRECATED, ERROR
    DTGORENABLED BOOLEAN DEFAULT true,

    -- Metadatos
    DTGORMETADATA JSONB, -- Metadatos adicionales
    DTGORPROVIDER VARCHAR(100), -- Proveedor (HuggingFace, Kaggle, AWS, etc.)
    DTGORLICENSE VARCHAR(100), -- Licencia del origen
    DTGORTERMS TEXT, -- Términos de uso

    -- Auditoría
    DTGORCREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    DTGORCREATEDBY BIGINT,
    DTGORUPDATEDAT TIMESTAMP,
    DTGORUPDATEDBY BIGINT
);

-- Índices
CREATE INDEX idx_dtgor_name ON DTGDATAORIGINS(DTGORNAME);
CREATE INDEX idx_dtgor_type ON DTGDATAORIGINS(DTGORTYPE);
CREATE INDEX idx_dtgor_category ON DTGDATAORIGINS(DTGORCATEGORY);
CREATE INDEX idx_dtgor_status ON DTGDATAORIGINS(DTGORSTATUS);
CREATE INDEX idx_dtgor_organization ON DTGDATAORIGINS(IDXORGANIZATION);
CREATE INDEX idx_dtgor_project ON DTGDATAORIGINS(IDXPROJECT);

COMMENT ON TABLE DTGDATAORIGINS IS 'Orígenes de datos externos e internos';
```

---

### 3. **Tabla de Relaciones Dataset-Origen: `DTGDATASETORIGINS`**

**Prefijo:** `DTGDSOR` (Data Governance Dataset Origins)

```sql
CREATE TABLE DTGDATASETORIGINS (
    IDXDATASETORIGIN BIGSERIAL PRIMARY KEY,
    IDXDATASET BIGINT NOT NULL REFERENCES DTGDATASETS(IDXDATASET) ON DELETE CASCADE,
    IDXORIGIN BIGINT NOT NULL REFERENCES DTGDATAORIGINS(IDXORIGIN) ON DELETE CASCADE,

    -- Tipo de relación
    DTGDSORRELATIONTYPE VARCHAR(50) NOT NULL, -- PRIMARY, SECONDARY, BACKUP, VERSION

    -- Configuración específica de la relación
    DTGDSORQUERY TEXT, -- Query o filtro específico para este origen
    DTGDSORFILTERS JSONB, -- Filtros adicionales
    DTGDSORTRANSFORMATION JSONB, -- Transformaciones a aplicar

    -- Sincronización específica
    DTGDSORSYNCMETHOD VARCHAR(50),
    DTGDSORLASTSYNCED TIMESTAMP,
    DTGDSORLASTSYNCSTATUS VARCHAR(50),

    -- Prioridad
    DTGDSORPRIORITY INT DEFAULT 0, -- Para ordenar orígenes

    -- Auditoría
    DTGDSORCREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    DTGDSORCREATEDBY BIGINT,

    UNIQUE(IDXDATASET, IDXORIGIN, DTGDSORRELATIONTYPE)
);

CREATE INDEX idx_dtgdsor_dataset ON DTGDATASETORIGINS(IDXDATASET);
CREATE INDEX idx_dtgdsor_origin ON DTGDATASETORIGINS(IDXORIGIN);

COMMENT ON TABLE DTGDATASETORIGINS IS 'Relaciones entre datasets y orígenes de datos';
```

---

### 4. **Tabla de Calidad de Datos: `DTGDATAQUALITY`**

**Prefijo:** `DTGQ` (Data Governance Quality)

```sql
CREATE TABLE DTGDATAQUALITY (
    IDXQUALITY BIGSERIAL PRIMARY KEY,
    IDXDATASET BIGINT NOT NULL REFERENCES DTGDATASETS(IDXDATASET) ON DELETE CASCADE,

    -- Métricas de Calidad
    DTGQCOMPLETENESS DECIMAL(5,2), -- Completitud (0.00 - 1.00)
    DTGQACCURACY DECIMAL(5,2), -- Precisión
    DTGQCONSISTENCY DECIMAL(5,2), -- Consistencia
    DTGQVALIDITY DECIMAL(5,2), -- Validez
    DTGQUNIQUENESS DECIMAL(5,2), -- Unicidad
    DTGQTIMELINESS DECIMAL(5,2), -- Actualidad

    -- Score Global
    DTGQOVERALLSCORE DECIMAL(5,2), -- Score promedio

    -- Detalles
    DTGQMETRICS JSONB, -- Métricas detalladas por campo
    DTGQISSUES JSONB, -- Lista de problemas detectados
    DTGQRECOMMENDATIONS JSONB, -- Recomendaciones de mejora

    -- Análisis
    DTGQANALYZEDAT TIMESTAMP NOT NULL,
    DTGQANALYZEDBY BIGINT,
    DTGQANALYZER VARCHAR(100), -- Sistema o herramienta que realizó el análisis

    -- Auditoría
    DTGQCREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_dtgq_dataset ON DTGDATAQUALITY(IDXDATASET);
CREATE INDEX idx_dtgq_overallscore ON DTGDATAQUALITY(DTGQOVERALLSCORE);
CREATE INDEX idx_dtgq_analyzedat ON DTGDATAQUALITY(DTGQANALYZEDAT);

COMMENT ON TABLE DTGDATAQUALITY IS 'Métricas de calidad de datos';
```

---

### 5. **Tabla de Línea de Base de Datos: `DTGDATALINEAGE`**

**Prefijo:** `DTGL` (Data Governance Lineage)

```sql
CREATE TABLE DTGDATALINEAGE (
    IDXLINEAGE BIGSERIAL PRIMARY KEY,

    -- Origen
    IDXSOURCEDATASET BIGINT REFERENCES DTGDATASETS(IDXDATASET),
    IDXSOURCEORIGIN BIGINT REFERENCES DTGDATAORIGINS(IDXORIGIN),

    -- Destino
    IDXTARGETDATASET BIGINT REFERENCES DTGDATASETS(IDXDATASET),
    IDXTARGETORIGIN BIGINT REFERENCES DTGDATAORIGINS(IDXORIGIN),

    -- Transformación
    DTGLTRANSFORMATIONTYPE VARCHAR(50), -- COPY, TRANSFORM, AGGREGATE, FILTER, JOIN, SPLIT
    DTGLTRANSFORMATION JSONB, -- Detalles de la transformación
    DTGLTRANSFORMATIONSCRIPT TEXT, -- Script o query usado

    -- Proceso
    IDXPROCESS BIGINT, -- Referencia a proceso (BPMN, workflow, etc.)
    DTGLPROCESSNAME VARCHAR(255),

    -- Metadatos
    DTGLMETADATA JSONB,

    -- Auditoría
    DTGLCREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    DTGLCREATEDBY BIGINT
);

CREATE INDEX idx_dtgl_source_dataset ON DTGDATALINEAGE(IDXSOURCEDATASET);
CREATE INDEX idx_dtgl_target_dataset ON DTGDATALINEAGE(IDXTARGETDATASET);
CREATE INDEX idx_dtgl_source_origin ON DTGDATALINEAGE(IDXSOURCEORIGIN);
CREATE INDEX idx_dtgl_target_origin ON DTGDATALINEAGE(IDXTARGETORIGIN);

COMMENT ON TABLE DTGDATALINEAGE IS 'Línea de base de datos (data lineage)';
```

---

## 🔄 MIGRACIÓN DESDE ESTRUCTURAS ACTUALES

### Mapeo de Tablas Existentes

#### 1. **DSDDATASETS → DTGDATASETS**

```sql
-- Migración de DSDDATASETS a DTGDATASETS
INSERT INTO DTGDATASETS (
    IDXDATASET, IDUUID, DTGNAME, DTGDESCRIPTION, DTGTYPE,
    DTGORIGINTYPE, IDXPROJECT,
    DTGBIASANALYSIS, DTGBIASANALYZED, DTGBIASANALYZEDAT,
    DTGREPRESENTATIVITYSCORE, DTGREPRESENTATIVITYMETRICS,
    DTGGENDERDISTRIBUTION, DTGGENDERBALANCESCORE, DTGGENDERBALANCED,
    DTGSTATUS, DTGCREATEDAT, DTGCREATEDBY, DTGUPDATEDAT
)
SELECT
    IDXDATASET, IDUUID, DSDNAME, DSDDESCRIPTION, DSDTYPE,
    'INTERNAL' AS DTGORIGINTYPE, IDXPROJECT,
    DSDBIASANALYSIS, DSDBIASANALYZED, DSDBIASANALYZEDAT,
    DSDREPRESENTATIVITYSCORE, DSDREPRESENTATIVITYMETRICS,
    DSDGENDERDISTRIBUTION, DSDGENDERBALANCESCORE, DSDGENDERBALANCED,
    'APPROVED' AS DTGSTATUS, DSDCREATEDAT, DSDCREATEDBY, DSDUPDATEDAT
FROM DSDDATASETS;
```

#### 2. **RAGDATASOURCES → DTGDATAORIGINS + DTGDATASETS**

```sql
-- Paso 1: Migrar RAGDATASOURCES a DTGDATAORIGINS
INSERT INTO DTGDATAORIGINS (
    IDXORIGIN, IDUUID, DTGORNAME, DTGORTYPE, DTGORCATEGORY,
    DTGORCONNECTIONTYPE, DTGORCONNECTIONCONFIG,
    DTGORURL, DTGORSTATUS, DTGORENABLED,
    DTGORCREATEDAT, DTGORUPDATEDAT
)
SELECT
    IDXRAGDATASOURCE,
    gen_random_uuid()::VARCHAR AS IDUUID,
    RAGDSSOURCENAME,
    CASE
        WHEN RAGDSSOURCETYPE IN ('PostgreSQL', 'MongoDB', 'MySQL') THEN 'INTERNAL'
        ELSE 'EXTERNAL'
    END AS DTGORTYPE,
    CASE
        WHEN RAGDSSOURCETYPE IN ('PostgreSQL', 'MongoDB', 'MySQL') THEN 'DATABASE'
        WHEN RAGDSSOURCETYPE IN ('S3', 'MINIO') THEN 'CLOUD_STORAGE'
        ELSE 'API'
    END AS DTGORCATEGORY,
    RAGDSSOURCETYPE AS DTGORCONNECTIONTYPE,
    jsonb_build_object(
        'url', RAGDSSOURCEURL,
        'access', RAGDSACCESS
    ) AS DTGORCONNECTIONCONFIG,
    RAGDSSOURCEURL,
    CASE
        WHEN RAGDSINDEXSTATUS = 'INDEXED' THEN 'ACTIVE'
        ELSE 'INACTIVE'
    END AS DTGORSTATUS,
    true AS DTGORENABLED,
    RAGDSCREATEDAT,
    RAGDSUPDATEDAT
FROM RAGDATASOURCES;

-- Paso 2: Crear datasets asociados a cada origen RAG
INSERT INTO DTGDATASETS (
    IDUUID, DTGNAME, DTGTYPE, DTGORIGINTYPE,
    IDXORIGIN, IDXRAGSYSTEM,
    DTGQUALITYSCORE, DTGBIASSCORE,
    DTGDATACLASS, DTGSTATUS,
    DTGRECORDCOUNT, DTGLASTSYNCED,
    DTGCREATEDAT
)
SELECT
    gen_random_uuid()::VARCHAR,
    RAGDSSOURCENAME || ' Dataset',
    'RAG' AS DTGTYPE,
    CASE
        WHEN RAGDSSOURCETYPE IN ('PostgreSQL', 'MongoDB', 'MySQL') THEN 'INTERNAL'
        ELSE 'EXTERNAL'
    END AS DTGORIGINTYPE,
    IDXRAGDATASOURCE AS IDXORIGIN,
    IDXRAGSYSTEM,
    RAGDSQUALITYSCORE,
    RAGDSBIASSCORE,
    RAGDSDATACLASS,
    'ACTIVE' AS DTGSTATUS,
    RAGDSDOCUMENTCOUNT,
    RAGDSLASTINDEXED,
    RAGDSCREATEDAT
FROM RAGDATASOURCES;

-- Paso 3: Crear relaciones
INSERT INTO DTGDATASETORIGINS (IDXDATASET, IDXORIGIN, DTGDSORRELATIONTYPE)
SELECT
    d.IDXDATASET,
    o.IDXORIGIN,
    'PRIMARY' AS DTGDSORRELATIONTYPE
FROM DTGDATASETS d
JOIN DTGDATAORIGINS o ON d.IDXORIGIN = o.IDXORIGIN
WHERE d.DTGTYPE = 'RAG';
```

#### 3. **TRNDATASETSOURCES → DTGDATAORIGINS + DTGDATASETS**

```sql
-- Similar a RAGDATASOURCES pero con tipo TRAINING
-- (Estructura de TRNDATASETSOURCES debe ser revisada primero)
```

---

## 🏗️ ARQUITECTURA DEL MÓDULO

### Estructura de Carpetas

```
governance/data/
├── overview/              # Dashboard principal
├── datasets/              # Gestión de datasets
│   ├── overview/          # Listado de datasets
│   ├── [id]/              # Detalle de dataset
│   ├── create/            # Crear dataset
│   ├── quality/           # Análisis de calidad
│   ├── bias/              # Análisis de sesgos
│   └── lineage/           # Línea de base
├── origins/               # Gestión de orígenes
│   ├── overview/          # Listado de orígenes
│   ├── [id]/              # Detalle de origen
│   ├── create/            # Crear origen
│   ├── internal/          # Orígenes internos
│   ├── external/          # Orígenes externos
│   └── sync/              # Sincronización
├── compliance/            # Compliance y auditoría
│   ├── ods/               # KPIs ODS
│   ├── audit/             # Auditoría de datos
│   └── reports/           # Reportes
└── analytics/             # Analytics y métricas
    ├── quality/           # Métricas de calidad
    ├── usage/             # Uso de datos
    └── trends/            # Tendencias
```

---

## 🔌 API REST PROPUESTA

### Endpoints de Datasets

```plaintext
# CRUD Datasets
GET    /api/v1/governance/data/datasets
GET    /api/v1/governance/data/datasets/{id}
POST   /api/v1/governance/data/datasets
PUT    /api/v1/governance/data/datasets/{id}
DELETE /api/v1/governance/data/datasets/{id}

# Análisis y Calidad
GET    /api/v1/governance/data/datasets/{id}/quality
POST   /api/v1/governance/data/datasets/{id}/analyze-quality
GET    /api/v1/governance/data/datasets/{id}/bias
POST   /api/v1/governance/data/datasets/{id}/analyze-bias
GET    /api/v1/governance/data/datasets/{id}/lineage

# Versionado
GET    /api/v1/governance/data/datasets/{id}/versions
POST   /api/v1/governance/data/datasets/{id}/versions
GET    /api/v1/governance/data/datasets/{id}/versions/{version}

# Aprobación
POST   /api/v1/governance/data/datasets/{id}/approve
POST   /api/v1/governance/data/datasets/{id}/reject
```

### Endpoints de Orígenes

```plaintext
# CRUD Orígenes
GET    /api/v1/governance/data/origins
GET    /api/v1/governance/data/origins/{id}
POST   /api/v1/governance/data/origins
PUT    /api/v1/governance/data/origins/{id}
DELETE /api/v1/governance/data/origins/{id}

# Sincronización
POST   /api/v1/governance/data/origins/{id}/sync
GET    /api/v1/governance/data/origins/{id}/sync-status
POST   /api/v1/governance/data/origins/{id}/schedule-sync
GET    /api/v1/governance/data/origins/{id}/sync-history

# Validación
POST   /api/v1/governance/data/origins/{id}/validate
GET    /api/v1/governance/data/origins/{id}/connection-test

# Tipos y Proveedores
GET    /api/v1/governance/data/origins/types
GET    /api/v1/governance/data/origins/providers
```

### Endpoints de Compliance

```plaintext
# KPIs ODS
GET    /api/v1/governance/data/compliance/ods/kpi-5-1
GET    /api/v1/governance/data/compliance/ods/kpi-5-2
GET    /api/v1/governance/data/compliance/ods/kpi-10-3
GET    /api/v1/governance/data/compliance/ods/kpi-10-4

# Auditoría
GET    /api/v1/governance/data/compliance/audit
GET    /api/v1/governance/data/compliance/audit/{id}
POST   /api/v1/governance/data/compliance/audit
```

---

## 📱 PANTALLAS PROPUESTAS

### 1. **Dashboard Principal** (`/governance/data/overview`)

**Métricas:**
- Total de datasets
- Total de orígenes (internos/externos)
- Score promedio de calidad
- Score promedio de sesgos
- Datasets pendientes de aprobación
- Sincronizaciones recientes

**Gráficos:**
- Distribución de datasets por tipo
- Distribución de orígenes por categoría
- Evolución de calidad de datos
- Top datasets con mejor calidad
- Alertas y notificaciones

---

### 2. **Listado de Datasets** (`/governance/data/datasets/overview`)

**Filtros:**
- Tipo (TRAINING, VALIDATION, TEST, PRODUCTION, RAG)
- Origen (INTERNAL, EXTERNAL)
- Estado (DRAFT, VALIDATED, APPROVED, ACTIVE, ARCHIVED)
- Proyecto
- Calidad (rango de score)
- Sesgos detectados

**Columnas:**
- Nombre
- Tipo
- Origen
- Proyecto
- Calidad (score)
- Sesgos (score)
- Estado
- Última actualización
- Acciones

---

### 3. **Detalle de Dataset** (`/governance/data/datasets/[id]`)

**Pestañas:**
1. **Información General**
   - Metadatos
   - Esquema
   - Estadísticas
   - Versionado

2. **Orígenes**
   - Listado de orígenes asociados
   - Configuración de sincronización
   - Historial de sincronizaciones

3. **Calidad**
   - Métricas de calidad
   - Issues detectados
   - Recomendaciones

4. **Sesgos y Representatividad**
   - Análisis de sesgos
   - Distribución de género
   - Representatividad

5. **Línea de Base**
   - Gráfico de lineage
   - Transformaciones aplicadas
   - Procesos relacionados

6. **Compliance**
   - KPIs ODS relacionados
   - Auditoría
   - Aprobaciones

---

### 4. **Listado de Orígenes** (`/governance/data/origins/overview`)

**Filtros:**
- Tipo (INTERNAL, EXTERNAL)
- Categoría (DATABASE, API, FILE_SYSTEM, CLOUD_STORAGE, MARKETPLACE, WEB)
- Estado (ACTIVE, INACTIVE, DEPRECATED, ERROR)
- Organización
- Proveedor

**Columnas:**
- Nombre
- Tipo
- Categoría
- Conexión
- Estado
- Última sincronización
- Acciones

---

### 5. **Crear/Editar Origen** (`/governance/data/origins/create`)

**Formulario:**
- Información básica (nombre, descripción)
- Tipo y categoría
- Configuración de conexión
- Autenticación
- Sincronización (método, frecuencia, schedule)
- Permisos y acceso
- Validación

---

## 🔗 INTEGRACIONES

### 1. **Con Módulo RAG**
- Los datasets tipo RAG se relacionan con `RAGSYSTEMS`
- Los orígenes RAG se sincronizan automáticamente

### 2. **Con Módulo Training**
- Los datasets tipo TRAINING se relacionan con `TRNTRAININGEXECUTIONS`
- Integración con HuggingFace, Kaggle, etc.

### 3. **Con Módulo Compliance**
- KPIs ODS 5 y ODS 10
- Integración con FRIA
- Auditoría de datos

### 4. **Con Módulo Projects**
- Relación con proyectos
- Trazabilidad

### 5. **Con BPMN**
- Workflows de aprobación de datasets
- Procesos de validación de calidad
- Sincronización automática

---

## ✅ BENEFICIOS DE LA UNIFICACIÓN

1. **Centralización:** Un solo punto de gestión para todos los datos
2. **Trazabilidad:** Línea de base completa desde origen hasta uso
3. **Compliance:** Cumplimiento normativo unificado (ODS, EU AI Act)
4. **Calidad:** Análisis de calidad centralizado
5. **Eficiencia:** Eliminación de duplicación de funcionalidades
6. **Escalabilidad:** Fácil agregar nuevos tipos de orígenes
7. **Gobernanza:** Políticas y controles centralizados

---

## 🚀 PLAN DE IMPLEMENTACIÓN

### Fase 1: Diseño y Preparación (2 semanas)
- ✅ Definir estructura de tablas (completado en este documento)
- Definir entidades JPA
- Definir DTOs y ViewModels
- Diseñar APIs REST

### Fase 2: Backend - Entidades y Repositorios (2 semanas)
- Crear entidades JPA
- Crear repositorios
- Crear servicios de negocio
- Implementar lógica de migración

### Fase 3: Backend - APIs REST (2 semanas)
- Implementar endpoints CRUD
- Implementar endpoints de análisis
- Implementar endpoints de sincronización
- Implementar endpoints de compliance

### Fase 4: Migración de Datos (1 semana)
- Scripts de migración de DSDDATASETS
- Scripts de migración de RAGDATASOURCES
- Scripts de migración de TRNDATASETSOURCES
- Validación de migración

### Fase 5: Frontend - Pantallas Base (3 semanas)
- Dashboard principal
- Listado de datasets
- Detalle de dataset
- Listado de orígenes
- Crear/editar origen

### Fase 6: Frontend - Funcionalidades Avanzadas (2 semanas)
- Análisis de calidad
- Análisis de sesgos
- Línea de base
- Compliance y KPIs

### Fase 7: Integraciones (2 semanas)
- Integración con RAG
- Integración con Training
- Integración con Compliance
- Integración con BPMN

### Fase 8: Testing y Documentación (1 semana)
- Testing end-to-end
- Documentación de usuario
- Documentación técnica

**Total estimado: 15 semanas (~4 meses)**

---

## 📝 NOTAS IMPORTANTES

1. **Compatibilidad hacia atrás:** Las tablas antiguas (DSDDATASETS, RAGDATASOURCES) pueden mantenerse durante un período de transición
2. **Prefijos:** Usar prefijo `DTG` para todas las tablas del módulo
3. **Normalización:** Seguir tercera forma normal según reglas del usuario
4. **KISS y SOLID:** Mantener simplicidad y principios SOLID
5. **Arquitectura Hexagonal:** Separar lógica de negocio de infraestructura

---

## 🔄 PRÓXIMOS PASOS

1. Revisar y aprobar esta propuesta
2. Crear entidades JPA
3. Crear scripts SQL de migración
4. Implementar servicios de negocio
5. Desarrollar APIs REST
6. Crear pantallas frontend
7. Ejecutar migración de datos
8. Testing y validación

---

**Documento creado:** Diciembre 2025
**Autor:** Sistema de IA
**Versión:** 1.0
