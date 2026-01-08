# Integración de Datasets con Proyectos de Compliance (EU AI Act)

## Resumen Ejecutivo

Este documento describe la integración del módulo de **Data Governance** con los **proyectos de compliance** según la **EU AI Act**. Los datasets son un componente crítico del gobierno del dato y deben estar integrados en todo el ciclo de vida de compliance de los sistemas de IA.

---

## 1. Contexto Legal (EU AI Act)

### Artículos Relevantes

#### Art. 10 - Requisitos de Datos de Entrenamiento, Validación y Prueba
> "Los sistemas de IA de alto riesgo deben ser desarrollados y entrenados con datos que sean relevantes, representativos, libres de errores y completos."

**Implicaciones:**
- Los datasets deben estar vinculados a proyectos de compliance
- Debe existir trazabilidad de qué datasets se usaron para entrenar/validar cada sistema
- Los datasets deben cumplir requisitos de calidad y representatividad

#### Art. 11 - Documentación Técnica
> "La documentación técnica debe incluir una descripción de los datos de entrenamiento, validación y prueba utilizados."

**Implicaciones:**
- Los datasets deben estar documentados en la documentación técnica
- Debe existir relación entre Technical Docs y Datasets

#### Art. 27 - Evaluación de Impacto en Derechos Fundamentales (FRIA)
> "Antes de poner en el mercado o poner en servicio un sistema de IA de alto riesgo, se debe realizar una evaluación de impacto en derechos fundamentales."

**Implicaciones:**
- Los datasets usados en sistemas de alto riesgo deben evaluarse en FRIA
- Debe analizarse la calidad, sesgos y representatividad de los datasets
- Debe documentarse el impacto de los datasets en derechos fundamentales

#### Art. 17 - Sistema de Gestión de Calidad (QMS)
> "Los proveedores deben establecer, documentar, implementar, mantener y actualizar continuamente un sistema de gestión de calidad."

**Implicaciones:**
- Los datasets deben estar bajo control de calidad
- Debe existir trazabilidad de cambios en datasets
- Debe haber procesos de aprobación de datasets

---

## 2. Relación Datasets ↔ Proyectos de Compliance

### 2.1 Estructura Actual

#### Tabla: `DTGDATASETS`
```sql
CREATE TABLE DTGDATASETS (
    IDXDATASET BIGSERIAL PRIMARY KEY,
    IDXPROJECT BIGINT REFERENCES PRJPROJECTS(IDXPROJECT), -- ✅ Ya existe
    -- ... otros campos
);
```

#### Tabla: `PRJPROJECTS`
```sql
CREATE TABLE PRJPROJECTS (
    IDXPROJECT BIGSERIAL PRIMARY KEY,
    PRJISHIGHRISK BOOLEAN, -- Clasificación Art. 6
    PRJANNEXIIICATEGORY VARCHAR(50), -- Categoría Anexo III
    -- ... otros campos
);
```

### 2.2 Relaciones Necesarias

#### 1. **Dataset → Proyecto (Ya existe)**
- Un dataset puede estar asociado a un proyecto
- Un proyecto puede tener múltiples datasets (training, validation, test)

#### 2. **Dataset → FRIA (Nueva relación)**
- Un dataset puede ser evaluado en múltiples FRIA
- Un FRIA debe documentar los datasets usados

#### 3. **Dataset → Technical Docs (Nueva relación)**
- Los datasets deben estar documentados en Technical Docs
- Technical Docs debe referenciar los datasets usados

#### 4. **Dataset → QMS (Nueva relación)**
- Los datasets deben estar bajo control de calidad QMS
- Debe existir trazabilidad de aprobaciones de datasets

---

## 3. Nuevas Tablas y Relaciones

### 3.1 Tabla: `DTGDATASETPROJECTS` (Relación Many-to-Many)

**Propósito:** Permitir que un dataset esté asociado a múltiples proyectos (training, validation, test, production)

```sql
CREATE TABLE DTGDATASETPROJECTS (
    IDXRELATION BIGSERIAL PRIMARY KEY,
    IDXDATASET BIGINT NOT NULL REFERENCES DTGDATASETS(IDXDATASET),
    IDXPROJECT BIGINT NOT NULL REFERENCES PRJPROJECTS(IDXPROJECT),
    DTGDATASETROLE VARCHAR(50) NOT NULL, -- TRAINING, VALIDATION, TEST, PRODUCTION, RAG
    DTGDATASETUSAGE TEXT, -- Descripción de cómo se usa el dataset
    DTGCREATEDAT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    DTGCREATEDBY BIGINT,

    UNIQUE(IDXDATASET, IDXPROJECT, DTGDATASETROLE)
);

CREATE INDEX idx_dtgdatasetprojects_dataset ON DTGDATASETPROJECTS(IDXDATASET);
CREATE INDEX idx_dtgdatasetprojects_project ON DTGDATASETPROJECTS(IDXPROJECT);
```

### 3.2 Tabla: `DTGDATASETFRIA` (Relación Dataset ↔ FRIA)

**Propósito:** Vincular datasets con evaluaciones FRIA

```sql
CREATE TABLE DTGDATASETFRIA (
    IDXRELATION BIGSERIAL PRIMARY KEY,
    IDXDATASET BIGINT NOT NULL REFERENCES DTGDATASETS(IDXDATASET),
    IDXFRIA BIGINT NOT NULL REFERENCES FRIAFUNDAMENTALRIGHTSASSESSMENTS(IDXFRIA),
    DTGFRIAIMPACT TEXT, -- Impacto del dataset en derechos fundamentales
    DTGFRIAANALYSIS JSONB, -- Análisis detallado del dataset en el contexto FRIA
    DTGCREATEDAT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE(IDXDATASET, IDXFRIA)
);

CREATE INDEX idx_dtgdatasetfria_dataset ON DTGDATASETFRIA(IDXDATASET);
CREATE INDEX idx_dtgdatasetfria_fria ON DTGDATASETFRIA(IDXFRIA);
```

### 3.3 Tabla: `DTGDATASETTECHDOCS` (Relación Dataset ↔ Technical Docs)

**Propósito:** Vincular datasets con documentación técnica

```sql
CREATE TABLE DTGDATASETTECHDOCS (
    IDXRELATION BIGSERIAL PRIMARY KEY,
    IDXDATASET BIGINT NOT NULL REFERENCES DTGDATASETS(IDXDATASET),
    IDXTECHDOC BIGINT NOT NULL REFERENCES GOVAIAACTTECHNICALDOCS(IDXTECHDOC),
    DTGDOCSECTION VARCHAR(100), -- Sección de la doc donde se menciona (ej: "Training Data", "Validation Data")
    DTGDOCDESCRIPTION TEXT, -- Descripción del dataset en la documentación
    DTGCREATEDAT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE(IDXDATASET, IDXTECHDOC)
);

CREATE INDEX idx_dtgdatasettechdocs_dataset ON DTGDATASETTECHDOCS(IDXDATASET);
CREATE INDEX idx_dtgdatasettechdocs_techdoc ON DTGDATASETTECHDOCS(IDXTECHDOC);
```

### 3.4 Tabla: `DTGDATASETQMS` (Relación Dataset ↔ QMS)

**Propósito:** Vincular datasets con procesos QMS

```sql
CREATE TABLE DTGDATASETQMS (
    IDXRELATION BIGSERIAL PRIMARY KEY,
    IDXDATASET BIGINT NOT NULL REFERENCES DTGDATASETS(IDXDATASET),
    IDXQMS BIGINT NOT NULL REFERENCES QMSQUALITYMANAGEMENTSYSTEMS(IDXQMS),
    DTGQMSSTATUS VARCHAR(50), -- UNDER_REVIEW, APPROVED, REJECTED, CONDITIONAL
    DTGQMSREVIEWDATE TIMESTAMP,
    DTGQMSREVIEWEDBY BIGINT,
    DTGQMSNOTES TEXT,
    DTGCREATEDAT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE(IDXDATASET, IDXQMS)
);

CREATE INDEX idx_dtgdatasetqms_dataset ON DTGDATASETQMS(IDXDATASET);
CREATE INDEX idx_dtgdatasetqms_qms ON DTGDATASETQMS(IDXQMS);
```

---

## 4. Campos Adicionales en Tablas Existentes

### 4.1 Tabla: `DTGDATASETS` - Campos de Compliance

```sql
-- Agregar campos de compliance a DTGDATASETS
ALTER TABLE DTGDATASETS ADD COLUMN IF NOT EXISTS DTGCOMPLIANCEPROJECTID BIGINT REFERENCES PRJPROJECTS(IDXPROJECT);
ALTER TABLE DTGDATASETS ADD COLUMN IF NOT EXISTS DTGFRIAEVALUATED BOOLEAN DEFAULT false;
ALTER TABLE DTGDATASETS ADD COLUMN IF NOT EXISTS DTGFRIAEVALUATEDAT TIMESTAMP;
ALTER TABLE DTGDATASETS ADD COLUMN IF NOT EXISTS DTGFRIAIMPACTSCORE DECIMAL(5,2); -- 0.00 - 1.00
ALTER TABLE DTGDATASETS ADD COLUMN IF NOT EXISTS DTGQMSAPPROVED BOOLEAN DEFAULT false;
ALTER TABLE DTGDATASETS ADD COLUMN IF NOT EXISTS DTGQMSAPPROVEDAT TIMESTAMP;
ALTER TABLE DTGDATASETS ADD COLUMN IF NOT EXISTS DTGTECHDOCSDOCUMENTED BOOLEAN DEFAULT false;
ALTER TABLE DTGDATASETS ADD COLUMN IF NOT EXISTS DTGTECHDOCSDOCUMENTEDAT TIMESTAMP;
```

### 4.2 Tabla: `PRJPROJECTS` - Campos de Datasets

```sql
-- Agregar campos de datasets a PRJPROJECTS (opcional, para métricas rápidas)
ALTER TABLE PRJPROJECTS ADD COLUMN IF NOT EXISTS PRJDATASETCOUNT INTEGER DEFAULT 0;
ALTER TABLE PRJPROJECTS ADD COLUMN IF NOT EXISTS PRJDATASETQUALITYSCORE DECIMAL(5,2); -- Promedio de calidad
ALTER TABLE PRJPROJECTS ADD COLUMN IF NOT EXISTS PRJDATASETBIASSCORE DECIMAL(5,2); -- Promedio de sesgos
```

---

## 5. Funcionalidades a Implementar

### 5.1 Pantallas de Compliance - Integración con Datasets

#### 5.1.1 Clasificación (Art. 6) - Mostrar Datasets
**Ruta:** `/governance/compliance/classification/[id]`

**Funcionalidad:**
- Mostrar lista de datasets asociados al proyecto
- Mostrar métricas de calidad y sesgos de los datasets
- Alertar si hay datasets con baja calidad o alto sesgo
- Permitir vincular nuevos datasets al proyecto

#### 5.1.2 FRIA (Art. 27) - Evaluar Datasets
**Ruta:** `/governance/compliance/fria/[id]`

**Funcionalidad:**
- Sección dedicada a "Datasets y Derechos Fundamentales"
- Lista de datasets usados en el sistema
- Análisis de impacto de cada dataset en derechos fundamentales
- Métricas de calidad, sesgos y representatividad
- Recomendaciones de mitigación si hay problemas

#### 5.1.3 Technical Docs (Art. 11) - Documentar Datasets
**Ruta:** `/governance/compliance/technical-docs/[id]`

**Funcionalidad:**
- Sección "Training, Validation and Test Data"
- Lista de datasets con descripción
- Schema de cada dataset
- Estadísticas y métricas de calidad
- Generación automática de documentación

#### 5.1.4 QMS (Art. 17) - Control de Calidad de Datasets
**Ruta:** `/governance/compliance/qms/[id]`

**Funcionalidad:**
- Lista de datasets bajo control de calidad
- Estado de aprobación de cada dataset
- Historial de revisiones
- Acciones correctivas si hay problemas

### 5.2 Pantallas de Data Governance - Integración con Compliance

#### 5.2.1 Detalle de Dataset - Pestaña Compliance
**Ruta:** `/governance/data/datasets/[id]`

**Nueva Pestaña:** "Compliance"

**Funcionalidad:**
- Mostrar proyectos de compliance asociados
- Mostrar FRIA que evalúan este dataset
- Mostrar Technical Docs que documentan este dataset
- Mostrar procesos QMS que controlan este dataset
- Estado de compliance del dataset
- Alertas si el dataset no cumple requisitos

#### 5.2.2 Listado de Datasets - Filtros de Compliance
**Ruta:** `/governance/data/datasets/overview`

**Nuevos Filtros:**
- Proyecto de compliance
- Estado de FRIA (evaluado/no evaluado)
- Estado de QMS (aprobado/no aprobado)
- Estado de Technical Docs (documentado/no documentado)
- Clasificación de riesgo del proyecto asociado

---

## 6. Endpoints API Necesarios

### 6.1 Endpoints de Datasets con Compliance

```typescript
// Obtener datasets de un proyecto de compliance
GET /api/v1/governance/data/datasets?projectId={projectId}

// Vincular dataset a proyecto
POST /api/v1/governance/data/datasets/{datasetId}/projects/{projectId}
Body: { role: "TRAINING" | "VALIDATION" | "TEST" | "PRODUCTION" }

// Desvincular dataset de proyecto
DELETE /api/v1/governance/data/datasets/{datasetId}/projects/{projectId}

// Obtener datasets evaluados en FRIA
GET /api/v1/governance/data/datasets?friaId={friaId}

// Vincular dataset a FRIA
POST /api/v1/governance/data/datasets/{datasetId}/fria/{friaId}
Body: { impact: "...", analysis: {...} }

// Obtener datasets documentados en Technical Docs
GET /api/v1/governance/data/datasets?techDocId={techDocId}

// Vincular dataset a Technical Doc
POST /api/v1/governance/data/datasets/{datasetId}/tech-docs/{techDocId}
Body: { section: "...", description: "..." }

// Obtener datasets bajo control QMS
GET /api/v1/governance/data/datasets?qmsId={qmsId}

// Vincular dataset a QMS
POST /api/v1/governance/data/datasets/{datasetId}/qms/{qmsId}
Body: { status: "UNDER_REVIEW" | "APPROVED" | "REJECTED" }
```

### 6.2 Endpoints de Compliance con Datasets

```typescript
// Obtener datasets de un proyecto
GET /api/v1/governance/compliance/projects/{projectId}/datasets

// Obtener datasets evaluados en FRIA
GET /api/v1/governance/compliance/fria/{friaId}/datasets

// Obtener datasets documentados en Technical Doc
GET /api/v1/governance/compliance/technical-docs/{techDocId}/datasets

// Obtener datasets bajo control QMS
GET /api/v1/governance/compliance/qms/{qmsId}/datasets
```

---

## 7. Plan de Implementación

### Fase 1: Estructura de Datos (1 semana)
1. ✅ Crear tablas de relación (DTGDATASETPROJECTS, DTGDATASETFRIA, etc.)
2. ✅ Agregar campos de compliance a DTGDATASETS
3. ✅ Crear entidades JPA
4. ✅ Crear repositorios
5. ✅ Crear DTOs

### Fase 2: Backend Services (1 semana)
1. ✅ Crear servicios de relación
2. ✅ Crear endpoints API
3. ✅ Implementar lógica de negocio
4. ✅ Tests unitarios

### Fase 3: Frontend - Integración en Compliance (2 semanas)
1. ✅ Agregar sección de datasets en Clasificación
2. ✅ Agregar sección de datasets en FRIA
3. ✅ Agregar sección de datasets en Technical Docs
4. ✅ Agregar sección de datasets en QMS

### Fase 4: Frontend - Integración en Data Governance (1 semana)
1. ✅ Agregar pestaña Compliance en detalle de dataset
2. ✅ Agregar filtros de compliance en listado
3. ✅ Mostrar métricas de compliance

### Fase 5: Validación y Testing (1 semana)
1. ✅ Tests end-to-end
2. ✅ Validación de requisitos EU AI Act
3. ✅ Documentación

---

## 8. Checklist de Cumplimiento EU AI Act

### Art. 10 - Requisitos de Datos
- [x] Datasets vinculados a proyectos
- [ ] Trazabilidad de datasets usados en entrenamiento/validación
- [ ] Validación de calidad de datos
- [ ] Validación de representatividad

### Art. 11 - Documentación Técnica
- [ ] Datasets documentados en Technical Docs
- [ ] Schema de datasets documentado
- [ ] Estadísticas de datasets documentadas

### Art. 27 - FRIA
- [ ] Datasets evaluados en FRIA
- [ ] Impacto de datasets en derechos fundamentales documentado
- [ ] Análisis de sesgos en contexto FRIA

### Art. 17 - QMS
- [ ] Datasets bajo control de calidad
- [ ] Procesos de aprobación de datasets
- [ ] Trazabilidad de cambios en datasets

---

**Documento generado:** 2025-01-14
**Versión:** 1.0
**Estado:** ✅ Listo para implementación
