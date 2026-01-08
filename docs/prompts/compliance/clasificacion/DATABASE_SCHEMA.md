# 🗄️ DOCUMENTACIÓN DE ESQUEMA DE BASE DE DATOS - CLASIFICACIÓN

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Módulo:** Compliance - Classification (Art. 6 + Anexo III EU AI Act)
**Base de Datos:** PostgreSQL 14+

---

## 📋 ÍNDICE

1. [Diagrama ER](#diagrama-er)
2. [Tablas Principales](#tablas-principales)
3. [Campos de Clasificación en Project](#campos-de-clasificación-en-project)
4. [Tabla AnnexIIICategory](#tabla-annexiiicategory)
5. [Tabla ProhibitedSystem](#tabla-prohibitedsystem)
6. [Relaciones](#relaciones)
7. [Índices y Optimizaciones](#índices-y-optimizaciones)
8. [Queries Comunes](#queries-comunes)
9. [Migraciones](#migraciones)

---

## 📊 DIAGRAMA ER

### Relaciones Principales

```
PRJPROJECTS (Proyectos)
    │
    ├── PRJISHIGHRISK (Boolean) - ¿Es de alto riesgo?
    ├── PRJANNEXIIICATEGORIES (JSONB) - Categorías Anexo III
    ├── PRJCLASSIFICATIONDATE (Timestamp) - Fecha de clasificación
    ├── PRJCLASSIFICATIONAUTHOR (Varchar) - Autor de clasificación
    ├── PRJPROHIBITEDUSECHECKED (Boolean) - Verificación Art. 5
    └── METADATA (JSONB) - Justificación almacenada aquí
         │
         └── classificationJustification (String)

ANNANNEXIIICATEGORIES (Categorías Anexo III)
    │
    ├── IDXANNEXIIICATEGORY (PK)
    ├── ANNCATEGORYCODE (Varchar) - "III.1" a "III.8"
    ├── ANNCATEGORYNAME (Varchar) - Nombre de categoría
    ├── ANNSUBCATEGORYCODE (Varchar) - "III.4.a", "III.4.b", etc.
    ├── ANNISLEVEL1 (Boolean) - TRUE para categorías principales
    ├── ANNISLEVEL2 (Boolean) - TRUE para subcategorías
    └── ANNPARENTCATEGORY (FK) - Self-reference

GOVPROHIBITEDSYSTEMS (Sistemas Prohibidos Art. 5)
    │
    ├── IDXPROHIBITEDSYSTEM (PK)
    ├── PROHIBITEDCODE (Varchar) - Código único
    ├── PROHIBITEDNAME (Varchar) - Nombre
    └── PROHIBITEDACTIVE (Boolean) - TRUE si está activo
```

---

## 📁 TABLAS PRINCIPALES

### 1. PRJPROJECTS

**Descripción:** Tabla principal de proyectos. Contiene campos de clasificación según Art. 6 del EU AI Act.

**Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/projects/Project.java`

**Campos relacionados con clasificación:**

| Campo | Tipo | Nullable | Descripción |
|-------|------|----------|-------------|
| `PRJISHIGHRISK` | BOOLEAN | YES | `true` si el proyecto es de alto riesgo |
| `PRJANNEXIIICATEGORIES` | JSONB | YES | JSON con categoría y subcategorías: `{"category":"III.5","subcategories":["III.5.b"]}` |
| `PRJCLASSIFICATIONDATE` | TIMESTAMP | YES | Fecha y hora de clasificación |
| `PRJCLASSIFICATIONAUTHOR` | VARCHAR(100) | YES | Usuario que realizó la clasificación |
| `PRJPROHIBITEDUSECHECKED` | BOOLEAN | YES | `true` si se verificó Art. 5 (sistemas prohibidos) |
| `PRJPROHIBITEDUSEJUSTIFICATION` | TEXT | YES | Justificación opcional de verificación Art. 5 |
| `PRJREGULATEDSECTOR` | BOOLEAN | YES | `true` si está en sector regulado (Anexo I) |
| `PRJANNEXILEGISLATION` | JSONB | YES | JSON con legislación del Anexo I |
| `METADATA` | JSONB | YES | JSON con metadata, incluye `classificationJustification` |

**Ejemplo de datos:**

```sql
-- Proyecto clasificado como alto riesgo
UPDATE PRJPROJECTS SET
    PRJISHIGHRISK = true,
    PRJANNEXIIICATEGORIES = '{"category":"III.5","subcategories":["III.5.b","III.5.c"]}',
    PRJCLASSIFICATIONDATE = '2025-12-15 10:30:00',
    PRJCLASSIFICATIONAUTHOR = 'user@example.com',
    PRJPROHIBITEDUSECHECKED = true,
    METADATA = '{"classificationJustification":"Este sistema debe clasificarse como de alto riesgo porque afecta significativamente los derechos fundamentales de las personas..."}'
WHERE IDXPROJECT = 1001;
```

---

### 2. ANNANNEXIIICATEGORIES

**Descripción:** Catálogo de categorías y subcategorías del Anexo III del EU AI Act.

**Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/catalogs/AnnexIIICategory.java`

**Script SQL:** `nocode.service.entitys/src/main/resources/sql/annex_iii_category.sql`

**Estructura de la tabla:**

```sql
CREATE TABLE ANNANNEXIIICATEGORIES (
    IDXANNEXIIICATEGORY BIGSERIAL PRIMARY KEY,
    iduuid VARCHAR(36) UNIQUE NOT NULL,

    -- Category Info
    ANNCATEGORYCODE VARCHAR(10) UNIQUE NOT NULL, -- "III.1", "III.2", ..., "III.8"
    ANNCATEGORYNAME VARCHAR(200) NOT NULL,
    ANNCATEGORYDESCRIPTION TEXT,

    -- Subcategory Info (NULL si es categoría principal)
    ANNSUBCATEGORYCODE VARCHAR(10), -- "III.4.a", "III.4.b", etc.
    ANNSUBCATEGORYNAME VARCHAR(300),
    ANNSUBCATEGORYDESCRIPTION TEXT,

    -- Hierarchy
    ANNPARENTCATEGORY BIGINT REFERENCES ANNANNEXIIICATEGORIES(IDXANNEXIIICATEGORY),
    ANNISLEVEL1 BOOLEAN DEFAULT false, -- TRUE para 8 categorías principales
    ANNISLEVEL2 BOOLEAN DEFAULT false, -- TRUE para subcategorías

    -- EU AI Act Reference
    ANNANNEXIIISECTION VARCHAR(500), -- Texto literal del Anexo III
    ANNARTICLEREFERENCE VARCHAR(100), -- "Art. 6.2 + Anexo III punto 4"

    -- Keywords para clasificación automática IA
    ANNKEYWORDS TEXT, -- JSON array de keywords para ML

    -- Metadata
    ANNACTIVE BOOLEAN DEFAULT true,
    ANNDISPLAYORDER INTEGER,
    ANNCREATEDAT TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Campos principales:**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `IDXANNEXIIICATEGORY` | BIGSERIAL | PK, autoincremental |
| `iduuid` | VARCHAR(36) | UUID único estándar |
| `ANNCATEGORYCODE` | VARCHAR(10) | Código de categoría: "III.1" a "III.8" |
| `ANNCATEGORYNAME` | VARCHAR(200) | Nombre de la categoría |
| `ANNCATEGORYDESCRIPTION` | TEXT | Descripción detallada |
| `ANNSUBCATEGORYCODE` | VARCHAR(10) | Código de subcategoría: "III.4.a", "III.4.b", etc. (NULL si es categoría principal) |
| `ANNSUBCATEGORYNAME` | VARCHAR(300) | Nombre de la subcategoría |
| `ANNSUBCATEGORYDESCRIPTION` | TEXT | Descripción de la subcategoría |
| `ANNPARENTCATEGORY` | BIGINT | FK a categoría padre (NULL si es categoría root) |
| `ANNISLEVEL1` | BOOLEAN | `true` para 8 categorías principales |
| `ANNISLEVEL2` | BOOLEAN | `true` para subcategorías |
| `ANNANNEXIIISECTION` | VARCHAR(500) | Texto literal del Anexo III |
| `ANNARTICLEREFERENCE` | VARCHAR(100) | Referencia al artículo del EU AI Act |
| `ANNKEYWORDS` | TEXT | JSON array de keywords para ML |
| `ANNACTIVE` | BOOLEAN | `true` si está activa |
| `ANNDISPLAYORDER` | INTEGER | Orden de visualización |
| `ANNCREATEDAT` | TIMESTAMP | Fecha de creación |

**Ejemplo de datos:**

```sql
-- Categoría principal III.5: Acceso a servicios esenciales
INSERT INTO ANNANNEXIIICATEGORIES (
    iduuid, ANNCATEGORYCODE, ANNCATEGORYNAME, ANNCATEGORYDESCRIPTION,
    ANNISLEVEL1, ANNISLEVEL2, ANNACTIVE, ANNDISPLAYORDER,
    ANNANNEXIIISECTION, ANNARTICLEREFERENCE
) VALUES (
    gen_random_uuid()::text,
    'III.5',
    'Acceso a servicios esenciales',
    'Sistemas de IA utilizados para evaluar la solvencia crediticia de personas físicas o establecer su puntuación crediticia...',
    true,
    false,
    true,
    5,
    'Anexo III punto 5',
    'Art. 6.2 + Anexo III punto 5'
);

-- Subcategoría III.5.b
INSERT INTO ANNANNEXIIICATEGORIES (
    iduuid, ANNCATEGORYCODE, ANNSUBCATEGORYCODE, ANNSUBCATEGORYNAME,
    ANNPARENTCATEGORY, ANNISLEVEL1, ANNISLEVEL2, ANNACTIVE
) VALUES (
    gen_random_uuid()::text,
    'III.5',
    'III.5.b',
    'Evaluación de solvencia crediticia',
    (SELECT IDXANNEXIIICATEGORY FROM ANNANNEXIIICATEGORIES WHERE ANNCATEGORYCODE = 'III.5' AND ANNISLEVEL1 = true),
    false,
    true,
    true
);
```

**8 Categorías Principales:**

1. **III.1** - Biometría y categorización biométrica
2. **III.2** - Gestión de infraestructuras críticas
3. **III.3** - Educación y formación profesional
4. **III.4** - Empleo y gestión de trabajadores
5. **III.5** - Acceso a servicios esenciales
6. **III.6** - Aplicación de la ley
7. **III.7** - Migración, asilo y control de fronteras
8. **III.8** - Administración de justicia

---

### 3. GOVPROHIBITEDSYSTEMS

**Descripción:** Catálogo de sistemas de IA prohibidos según Art. 5 y Anexo II del EU AI Act.

**Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/ProhibitedSystem.java`

**Estructura de la tabla:**

```sql
CREATE TABLE GOVPROHIBITEDSYSTEMS (
    IDXPROHIBITEDSYSTEM BIGSERIAL PRIMARY KEY,
    iduuid VARCHAR(36) UNIQUE NOT NULL,

    PROHIBITEDCODE VARCHAR(50) UNIQUE NOT NULL, -- "ART5_1_A", "ART5_1_B", etc.
    PROHIBITEDNAME VARCHAR(200) NOT NULL,
    PROHIBITEDDESCRIPTION TEXT,
    PROHIBITEDARTICLE VARCHAR(50), -- "Art. 5.1.a", "Art. 5.1.b", etc.
    PROHIBITEDACTIVE BOOLEAN DEFAULT true,
    PROHIBITEDKEYWORDS TEXT, -- JSON array de keywords para detección automática

    CREATEDAT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATEDAT TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Campos principales:**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `IDXPROHIBITEDSYSTEM` | BIGSERIAL | PK, autoincremental |
| `iduuid` | VARCHAR(36) | UUID único estándar |
| `PROHIBITEDCODE` | VARCHAR(50) | Código único del sistema prohibido |
| `PROHIBITEDNAME` | VARCHAR(200) | Nombre del sistema prohibido |
| `PROHIBITEDDESCRIPTION` | TEXT | Descripción detallada |
| `PROHIBITEDARTICLE` | VARCHAR(50) | Referencia al artículo: "Art. 5.1.a", "Art. 5.1.b", etc. |
| `PROHIBITEDACTIVE` | BOOLEAN | `true` si está activo |
| `PROHIBITEDKEYWORDS` | TEXT | JSON array de keywords para detección automática |

**Ejemplo de datos:**

```sql
-- Sistema prohibido: Manipulación subliminal
INSERT INTO GOVPROHIBITEDSYSTEMS (
    iduuid, PROHIBITEDCODE, PROHIBITEDNAME, PROHIBITEDDESCRIPTION,
    PROHIBITEDARTICLE, PROHIBITEDACTIVE, PROHIBITEDKEYWORDS
) VALUES (
    gen_random_uuid()::text,
    'ART5_1_A',
    'Manipulación subliminal',
    'Sistemas de IA que utilizan técnicas subliminales más allá de la conciencia de una persona...',
    'Art. 5.1.a',
    true,
    '["subliminal", "manipulation", "beyond consciousness"]'
);
```

---

## 🔗 RELACIONES

### Relación Project → AnnexIIICategory

**Tipo:** Indirecta (vía JSONB)

**Descripción:** Los proyectos no tienen una relación directa con `AnnexIIICategory`. En su lugar, almacenan el código de categoría y subcategorías en el campo JSONB `PRJANNEXIIICATEGORIES`.

**Formato JSONB:**

```json
{
  "category": "III.5",
  "subcategories": ["III.5.b", "III.5.c"]
}
```

**Query para obtener categoría:**

```sql
-- Obtener proyectos de una categoría específica
SELECT * FROM PRJPROJECTS
WHERE PRJANNEXIIICATEGORIES::jsonb->>'category' = 'III.5';

-- Obtener proyectos con una subcategoría específica
SELECT * FROM PRJPROJECTS
WHERE PRJANNEXIIICATEGORIES::jsonb->'subcategories' @> '["III.5.b"]'::jsonb;
```

### Relación AnnexIIICategory → AnnexIIICategory (Self-Reference)

**Tipo:** Many-to-One (self-reference)

**Descripción:** Las subcategorías tienen una relación con su categoría padre.

**Campo:** `ANNPARENTCATEGORY` (FK a `IDXANNEXIIICATEGORY`)

**Query para obtener subcategorías:**

```sql
-- Obtener subcategorías de una categoría
SELECT * FROM ANNANNEXIIICATEGORIES
WHERE ANNPARENTCATEGORY = (
    SELECT IDXANNEXIIICATEGORY FROM ANNANNEXIIICATEGORIES
    WHERE ANNCATEGORYCODE = 'III.5' AND ANNISLEVEL1 = true
);
```

---

## 📊 ÍNDICES Y OPTIMIZACIONES

### Índices Recomendados

```sql
-- Índice para búsqueda rápida de proyectos de alto riesgo
CREATE INDEX idx_prjprojects_highrisk ON PRJPROJECTS(PRJISHIGHRISK)
WHERE PRJISHIGHRISK = true;

-- Índice para búsqueda por categoría (usando GIN para JSONB)
CREATE INDEX idx_prjprojects_annexiii_categories ON PRJPROJECTS
USING GIN (PRJANNEXIIICATEGORIES);

-- Índice para búsqueda por fecha de clasificación
CREATE INDEX idx_prjprojects_classification_date ON PRJPROJECTS(PRJCLASSIFICATIONDATE)
WHERE PRJCLASSIFICATIONDATE IS NOT NULL;

-- Índice para búsqueda por autor de clasificación
CREATE INDEX idx_prjprojects_classification_author ON PRJPROJECTS(PRJCLASSIFICATIONAUTHOR)
WHERE PRJCLASSIFICATIONAUTHOR IS NOT NULL;

-- Índice para búsqueda de categorías por código
CREATE UNIQUE INDEX idx_annexiii_category_code ON ANNANNEXIIICATEGORIES(ANNCATEGORYCODE);

-- Índice para búsqueda de subcategorías por código
CREATE INDEX idx_annexiii_subcategory_code ON ANNANNEXIIICATEGORIES(ANNSUBCATEGORYCODE)
WHERE ANNSUBCATEGORYCODE IS NOT NULL;

-- Índice para búsqueda de categorías activas
CREATE INDEX idx_annexiii_active ON ANNANNEXIIICATEGORIES(ANNACTIVE)
WHERE ANNACTIVE = true;

-- Índice para búsqueda de sistemas prohibidos activos
CREATE INDEX idx_prohibited_systems_active ON GOVPROHIBITEDSYSTEMS(PROHIBITEDACTIVE)
WHERE PROHIBITEDACTIVE = true;
```

### Optimizaciones JSONB

**Query optimizada para búsqueda en JSONB:**

```sql
-- Usar operador @> para búsqueda eficiente
SELECT * FROM PRJPROJECTS
WHERE PRJANNEXIIICATEGORIES::jsonb @> '{"category": "III.5"}'::jsonb;

-- Usar operador ? para verificar existencia de clave
SELECT * FROM PRJPROJECTS
WHERE PRJANNEXIIICATEGORIES::jsonb ? 'category';
```

---

## 🔍 QUERIES COMUNES

### 1. Obtener Proyectos de Alto Riesgo

```sql
SELECT
    IDXPROJECT,
    PRJNAME,
    PRJISHIGHRISK,
    PRJANNEXIIICATEGORIES::jsonb->>'category' AS category,
    PRJCLASSIFICATIONDATE,
    PRJCLASSIFICATIONAUTHOR
FROM PRJPROJECTS
WHERE PRJISHIGHRISK = true
ORDER BY PRJCLASSIFICATIONDATE DESC;
```

### 2. Obtener Proyectos por Categoría

```sql
SELECT
    IDXPROJECT,
    PRJNAME,
    PRJANNEXIIICATEGORIES::jsonb->>'category' AS category,
    PRJANNEXIIICATEGORIES::jsonb->'subcategories' AS subcategories
FROM PRJPROJECTS
WHERE PRJANNEXIIICATEGORIES::jsonb->>'category' = 'III.5';
```

### 3. Obtener Categorías Principales

```sql
SELECT
    IDXANNEXIIICATEGORY,
    ANNCATEGORYCODE,
    ANNCATEGORYNAME,
    ANNCATEGORYDESCRIPTION
FROM ANNANNEXIIICATEGORIES
WHERE ANNISLEVEL1 = true
  AND ANNACTIVE = true
ORDER BY ANNDISPLAYORDER;
```

### 4. Obtener Subcategorías de una Categoría

```sql
SELECT
    IDXANNEXIIICATEGORY,
    ANNSUBCATEGORYCODE,
    ANNSUBCATEGORYNAME,
    ANNSUBCATEGORYDESCRIPTION
FROM ANNANNEXIIICATEGORIES
WHERE ANNPARENTCATEGORY = (
    SELECT IDXANNEXIIICATEGORY
    FROM ANNANNEXIIICATEGORIES
    WHERE ANNCATEGORYCODE = 'III.5'
      AND ANNISLEVEL1 = true
)
  AND ANNISLEVEL2 = true
  AND ANNACTIVE = true
ORDER BY ANNSUBCATEGORYCODE;
```

### 5. Estadísticas de Clasificaciones

```sql
SELECT
    PRJANNEXIIICATEGORIES::jsonb->>'category' AS category,
    COUNT(*) AS total_projects,
    COUNT(DISTINCT PRJCLASSIFICATIONAUTHOR) AS total_classifiers
FROM PRJPROJECTS
WHERE PRJISHIGHRISK = true
  AND PRJANNEXIIICATEGORIES IS NOT NULL
GROUP BY PRJANNEXIIICATEGORIES::jsonb->>'category'
ORDER BY total_projects DESC;
```

### 6. Proyectos Clasificados en Rango de Fechas

```sql
SELECT
    IDXPROJECT,
    PRJNAME,
    PRJANNEXIIICATEGORIES::jsonb->>'category' AS category,
    PRJCLASSIFICATIONDATE,
    PRJCLASSIFICATIONAUTHOR
FROM PRJPROJECTS
WHERE PRJISHIGHRISK = true
  AND PRJCLASSIFICATIONDATE BETWEEN '2025-01-01' AND '2025-12-31'
ORDER BY PRJCLASSIFICATIONDATE DESC;
```

### 7. Obtener Justificación de Clasificación

```sql
SELECT
    IDXPROJECT,
    PRJNAME,
    METADATA::jsonb->>'classificationJustification' AS justification
FROM PRJPROJECTS
WHERE PRJISHIGHRISK = true
  AND METADATA::jsonb ? 'classificationJustification';
```

---

## 🔄 MIGRACIONES

### Flyway Migrations

**Ubicación:** `codeflowx.govern.repository/src/main/resources/db/migration/`

**Migraciones relacionadas con clasificación:**

- `V001__create_annex_iii_categories.sql` - Crear tabla de categorías
- `V002__insert_annex_iii_categories.sql` - Insertar 8 categorías principales y subcategorías
- `V003__add_classification_fields_to_project.sql` - Agregar campos de clasificación a PRJPROJECTS
- `V004__create_prohibited_systems.sql` - Crear tabla de sistemas prohibidos
- `V005__insert_prohibited_systems.sql` - Insertar sistemas prohibidos del Anexo II

**Ejemplo de migración:**

```sql
-- V003__add_classification_fields_to_project.sql
ALTER TABLE PRJPROJECTS
ADD COLUMN IF NOT EXISTS PRJISHIGHRISK BOOLEAN,
ADD COLUMN IF NOT EXISTS PRJANNEXIIICATEGORIES JSONB,
ADD COLUMN IF NOT EXISTS PRJCLASSIFICATIONDATE TIMESTAMP,
ADD COLUMN IF NOT EXISTS PRJCLASSIFICATIONAUTHOR VARCHAR(100),
ADD COLUMN IF NOT EXISTS PRJPROHIBITEDUSECHECKED BOOLEAN,
ADD COLUMN IF NOT EXISTS PRJPROHIBITEDUSEJUSTIFICATION TEXT;

-- Crear índices
CREATE INDEX IF NOT EXISTS idx_prjprojects_highrisk
ON PRJPROJECTS(PRJISHIGHRISK) WHERE PRJISHIGHRISK = true;

CREATE INDEX IF NOT EXISTS idx_prjprojects_annexiii_categories
ON PRJPROJECTS USING GIN (PRJANNEXIIICATEGORIES);
```

---

## 📚 REFERENCIAS

### Documentación Relacionada

- `DEVELOPER_GUIDE_BACKEND.md` - Guía de desarrollo backend
- `DEVELOPER_GUIDE_FRONTEND.md` - Guía de desarrollo frontend
- `ESTADO_IMPLEMENTACION_CLASSIFICATION.md` - Estado de implementación

### Scripts SQL

- `nocode.service.entitys/src/main/resources/sql/annex_iii_category.sql`
- `nocode.service.entitys/src/main/resources/sql/eu_ai_act_tables_all.sql`
- `nocode.service.entitys/src/main/resources/sql/compliance_assessment.sql`

---

**Última Actualización:** Diciembre 2025
**Versión:** 1.0
**Estado:** ✅ Operativo y listo para producción
