# PROMPT: Sistema de Experimentos y Runs estilo MLflow para CodeflowX Governance

**Fecha:** Enero 2025
**Objetivo:** Crear un sistema unificado de experimentos y runs para testing y validación de entidades de AI Governance (Prompts, Modelos, Agentes, RAG, Datasets)

---

## 📋 CONTEXTO Y REQUISITOS

### Propósito
Somos una plataforma de **Governance y Cumplimiento de IA**, NO una plataforma MLOps. Necesitamos un sistema centralizado de **testing y validación** para garantizar calidad y cumplimiento regulatorio.

### Entidades Soportadas
El sistema debe soportar testing de:
1. **Prompts** (Prompt testing y validación)
2. **Modelos** (Bias analysis, explainability, performance)
3. **Agentes** (Evaluación de comportamiento, compliance)
4. **RAG** (Evaluación de retrieval, respuesta, accuracy)
5. **Datasets** (Quality metrics, validation, compliance checks)

### Principio de Diseño
El sistema debe estar **organizado como MLflow** para que usuarios técnicos lo reconozcan fácilmente:
- **Experiments**: Contenedores que agrupan runs relacionados
- **Runs**: Ejecuciones individuales con parámetros, métricas, artefactos
- **Métricas**: Resultados numéricos de las pruebas
- **Parámetros**: Configuración de las pruebas
- **Artefactos**: Reportes, visualizaciones, datasets de prueba

---

## 🏗️ ARQUITECTURA Y REFERENCIAS

### 1. Estructura del Proyecto

**Workspace principal:** `nocode-service`

**Módulos Java:**
- `nocode.service.entitys` - Entidades JPA
- `codeflowx.govern.repository` - Repositorios
- `codeflowx.govern.business` - Lógica de negocio
- `codeflowx-governance-models-service` - Microservicio de modelos
- `codeflowx.govern.bff.compliance` - BFF (Backend for Frontend)

**Frontend:**
- `codeflowx-studio/app/(app)/models` - Pantallas de modelos

### 2. Referencias de Arquitectura

#### Documentos de Arquitectura:
- **Estado actual del módulo:** `codeflowx-studio/docs/prompts/governance/models/ESTADO_ACTUAL_MODULO.md`
- **Análisis de valor MLflow:** `codeflowx-studio/docs/prompts/governance/models/ANALISIS_VALOR_MLFLOW_BACKEND.md`
- **Propuesta inicial:** `codeflowx-studio/docs/prompts/governance/models/PROPUESTA_SISTEMA_EXPERIMENTOS_MLFLOW.md`

#### Entidades JPA Existentes:

**Modelos:**
- `nocode.service.entitys/.../entity/models/Model.java`
- `nocode.service.entitys/.../entity/evaluation/ModelBiasAnalysis.java`
- `nocode.service.entitys/.../entity/evaluation/ModelExplainability.java`
- `nocode.service.entitys/.../entity/evaluation/ModelPerformance.java`

**Otras entidades:**
- `nocode.service.entitys/.../entity/prompts/Prompt.java`
- `nocode.service.entitys/.../entity/agents/Agent.java`
- `nocode.service.entitys/.../entity/rag/RAG*.java`
- `nocode.service.entitys/.../entity/governance/DataGovernance*.java` (datasets)

#### Servicios de Negocio Existentes:
- `codeflowx.govern.business/.../evaluation/ModelBiasAnalysisBusinessService.java`
- `codeflowx.govern.business/.../evaluation/ModelExplainabilityBusinessService.java`
- `codeflowx.govern.business/.../evaluation/ModelPerformanceBusinessService.java`

#### Microservicios Python:

**Bias Detection Service:**
- **Puerto:** 8001
- **Endpoint base:** `http://bias-detection:8001`
- **Uso actual:** Análisis de sesgo en modelos
- **Cliente Java:** `AIGovernanceClient.biasDetection()` (ver `codeflowx.govern.nocode.client`)

**AI Interpreter Service:**
- **Puerto:** 8011
- **Endpoint base:** `http://ai-interpreter:8011`
- **Uso actual:** Explicabilidad de modelos
- **Cliente Java:** `AIGovernanceClient.explainPredictions()` (ver `codeflowx.govern.nocode.client`)

**Referencia de integración:**
Ver cómo se integran estos servicios en:
- `codeflowx.govern.business/.../evaluation/ModelBiasAnalysisBusinessService.java` (método `executeBiasAnalysis`)
- `codeflowx.govern.business/.../evaluation/ModelExplainabilityBusinessService.java` (método `executeExplainability`)

### 3. Base de Datos

**Base de datos:** PostgreSQL
**Formato de campos complejos:** JSONB para parámetros, métricas, artefactos, tags

**Convenciones de nombres de tablas:**
- Prefijo de 3 caracteres según módulo: `mod_` (models), `prm_` (prompts), `agn_` (agents), `rag_` (RAG), `dsg_` (datasets)
- PK autonumérica: `idx{entityname}id`
- Campos en formato camelCase con prefijo del módulo
- Siempre incluir campos de auditoría: `created_by`, `created_at`, `updated_at`

**Ejemplo de estructura JSONB:**

```json
// Parámetros (modrunparameters)
{
  "protectedAttribute": "gender",
  "threshold": 0.8,
  "method": "shap",
  "datasetPath": "s3://bucket/data/test.csv"
}

// Métricas (modrunmetrics) - Sin historial temporal (son ejecuciones puntuales)
{
  "fairness_score": 0.85,
  "demographic_parity": 0.92,
  "accuracy": 0.95,
  "latency_ms": 150
}

// Artefactos (modrunartifacts)
[
  {
    "path": "s3://codeflowx-artifacts/runs/123/report.pdf",
    "type": "report",
    "size": 2048000,
    "mimeType": "application/pdf"
  }
]

// Tags (modruntags)
{
  "user": "john.doe",
  "version": "1.0.0",
  "environment": "production",
  "team": "data-science"
}
```

---

## 📐 DISEÑO DE ENTIDADES

### 1. Tabla: `gov_experiment` (Experimentos)

**Propósito:** Contenedor que agrupa múltiples runs relacionados.

**Campos:**

```sql
CREATE TABLE gov_experiment (
    idxexperimentid BIGSERIAL PRIMARY KEY,

    -- Información básica
    govexperimentname VARCHAR(255) NOT NULL,
    govexperimentdescription TEXT,

    -- Tipo de entidad que se prueba (MODEL, PROMPT, AGENT, RAG, DATASET)
    govexperimententitytype VARCHAR(50) NOT NULL,

    -- Referencia a la entidad (opcional, puede haber experimentos sin entidad específica)
    govexperimententityid BIGINT,

    -- Estado
    govexperimentstatus VARCHAR(50) DEFAULT 'ACTIVE', -- ACTIVE, ARCHIVED, DELETED

    -- Tags como JSONB
    govexperimenttags JSONB,

    -- Campos de auditoría
    govexperimentcreatedby VARCHAR(100),
    govexperimentcreatedat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    govexperimentupdatedat TIMESTAMP,

    -- Índices
    CONSTRAINT fk_experiment_entity CHECK (
        (govexperimententitytype = 'MODEL' AND govexperimententityid IS NOT NULL) OR
        (govexperimententitytype = 'PROMPT' AND govexperimententityid IS NOT NULL) OR
        (govexperimententitytype = 'AGENT' AND govexperimententityid IS NOT NULL) OR
        (govexperimententitytype = 'RAG' AND govexperimententityid IS NOT NULL) OR
        (govexperimententitytype = 'DATASET' AND govexperimententityid IS NOT NULL) OR
        govexperimententityid IS NULL
    )
);

CREATE INDEX idx_govexperiment_entity ON gov_experiment(govexperimententitytype, govexperimententityid);
CREATE INDEX idx_govexperiment_status ON gov_experiment(govexperimentstatus);
CREATE INDEX idx_govexperiment_tags ON gov_experiment USING GIN(govexperimenttags);
```

**Entidad JPA:** `GovernanceExperiment.java`

### 2. Tabla: `gov_run` (Runs/Ejecuciones)

**Propósito:** Ejecución individual de una prueba dentro de un experimento.

**Campos:**

```sql
CREATE TABLE gov_run (
    idxrunid BIGSERIAL PRIMARY KEY,

    -- Relación con experimento
    idxexperimentid BIGINT NOT NULL REFERENCES gov_experiment(idxexperimentid),

    -- Información básica
    govrunname VARCHAR(255) NOT NULL,
    govrundescription TEXT,

    -- Tipo de run/prueba
    govruntype VARCHAR(100) NOT NULL,
    -- Para MODEL: BIAS_ANALYSIS, EXPLAINABILITY, PERFORMANCE, VALIDATION
    -- Para PROMPT: PROMPT_TEST, COMPLIANCE_CHECK, QUALITY_ASSESSMENT
    -- Para AGENT: BEHAVIOR_TEST, COMPLIANCE_TEST, PERFORMANCE_TEST
    -- Para RAG: RETRIEVAL_TEST, ACCURACY_TEST, LATENCY_TEST
    -- Para DATASET: QUALITY_CHECK, COMPLIANCE_CHECK, VALIDATION

    -- Tipo de entidad
    govrunentitytype VARCHAR(50) NOT NULL, -- MODEL, PROMPT, AGENT, RAG, DATASET
    govrunentityid BIGINT, -- FK a la entidad específica (opcional)

    -- Estado
    govrunstatus VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    -- PENDING, RUNNING, COMPLETED, FAILED, CANCELLED

    -- Timestamps
    govrunstartedat TIMESTAMP,
    govrunendedat TIMESTAMP,
    govrunduration INTEGER, -- Duración en segundos

    -- Parámetros de configuración (JSONB)
    govrunparameters JSONB,

    -- Métricas (JSONB) - Resultados numéricos (sin historial temporal)
    govrunmetrics JSONB,

    -- Tags (JSONB)
    govruntags JSONB,

    -- Artefactos (JSONB) - Referencias a archivos
    govrunartifacts JSONB,

    -- Referencias a resultados específicos (FKs opcionales según tipo)
    -- Para modelos:
    idxbiasanalysis BIGINT REFERENCES mod_biasanalysis(idxbiasanalysisid),
    idxexplainability BIGINT REFERENCES mod_explainability(idxexplainabilityid),
    idxperformance BIGINT REFERENCES mod_performance(idxperformanceid),

    -- Para prompts, agentes, RAG, datasets: futuras FK según necesidades

    -- Error si falló
    govrunerror TEXT,
    govrunerrordetails JSONB,

    -- Integración con workflows BPMN (opcional)
    govrunworkflowinstanceid VARCHAR(255),

    -- Campos de auditoría
    govruncreatedby VARCHAR(100),
    govruncreatedat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    govrunupdatedat TIMESTAMP,

    -- Índices
    CONSTRAINT fk_run_experiment FOREIGN KEY (idxexperimentid)
        REFERENCES gov_experiment(idxexperimentid) ON DELETE CASCADE
);

CREATE INDEX idx_govrun_experiment ON gov_run(idxexperimentid);
CREATE INDEX idx_govrun_entity ON gov_run(govrunentitytype, govrunentityid);
CREATE INDEX idx_govrun_status ON gov_run(govrunstatus);
CREATE INDEX idx_govrun_type ON gov_run(govruntype);
CREATE INDEX idx_govrun_parameters ON gov_run USING GIN(govrunparameters);
CREATE INDEX idx_govrun_metrics ON gov_run USING GIN(govrunmetrics);
CREATE INDEX idx_govrun_tags ON gov_run USING GIN(govruntags);
CREATE INDEX idx_govrun_startedat ON gov_run(govrunstartedat);
```

**Entidad JPA:** `GovernanceRun.java`

### 3. Relaciones con Entidades Existentes

**No modificar entidades existentes**, solo agregar relaciones opcionales desde `gov_run`:

- `ModelBiasAnalysis` → `gov_run.idxbiasanalysis`
- `ModelExplainability` → `gov_run.idxexplainability`
- `ModelPerformance` → `gov_run.idxperformance`

**Futuras relaciones:**
- Prompts: cuando exista `PromptEvaluation` → `gov_run.idxpromptevaluation`
- Agentes: cuando exista `AgentEvaluation` → `gov_run.idxagentevaluation`
- RAG: cuando exista `RAGEvaluation` → `gov_run.idxragevaluation`
- Datasets: cuando exista `DatasetValidation` → `gov_run.idxdatasetvalidation`

---

## 🔧 IMPLEMENTACIÓN BACKEND

### 1. Entidades JPA

**Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/governance/`

**Archivos a crear:**
- `GovernanceExperiment.java`
- `GovernanceRun.java`

**Requisitos:**
- Seguir convenciones del proyecto (prefijos, naming)
- Usar `@Entity`, `@Table`, `@Column`
- JSONB usando `@Column(columnDefinition = "jsonb")`
- Relaciones con `@ManyToOne`, `@OneToMany`
- Campos de auditoría estándar

### 2. Repositorios

**Ubicación:** `codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/governance/`

**Archivos a crear:**
- `GovernanceExperimentRepository.java`
- `GovernanceRunRepository.java`

**Métodos necesarios:**
```java
// GovernanceExperimentRepository
- findByGovExperimentEntityTypeAndGovExperimentEntityId
- findByGovExperimentStatus
- findByNameContainingIgnoreCase

// GovernanceRunRepository
- findByExperiment_IdxExperimentId
- findByGovRunEntityTypeAndGovRunEntityId
- findByGovRunStatus
- findByGovRunType
- findByGovRunStartedAtBetween
```

### 3. Servicios de Negocio

**Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/governance/`

**Archivos a crear:**
- `GovernanceExperimentBusinessService.java`
- `GovernanceRunBusinessService.java`

**Métodos principales:**

```java
// GovernanceExperimentBusinessService
- createExperiment(experimentDto, createdBy)
- getExperimentById(id)
- getExperimentsByEntityType(type, entityId)
- updateExperiment(id, experimentDto)
- deleteExperiment(id) // soft delete
- archiveExperiment(id)

// GovernanceRunBusinessService
- createRun(experimentId, runDto, createdBy)
- getRunById(id)
- getRunsByExperimentId(experimentId)
- getRunsByEntityType(type, entityId)
- updateRunStatus(id, status)
- logMetric(runId, metricName, metricValue)
- logParameters(runId, parameters)
- addArtifact(runId, artifactPath, artifactType)
- completeRun(id, metrics, artifacts)
- failRun(id, error, errorDetails)
- compareRuns(runIds) // Comparar múltiples runs
```

### 4. Integración con Servicios de Análisis Existentes

**Modificar servicios existentes para crear/actualizar runs:**

**En `ModelBiasAnalysisBusinessService.executeBiasAnalysis()`:**
```java
// Después de crear ModelBiasAnalysis
1. Crear o obtener experimento para el modelo
2. Crear run en el experimento (tipo: BIAS_ANALYSIS)
3. Ejecutar análisis (ya existe)
4. Actualizar run con métricas extraídas del análisis
5. Asociar run.idxbiasanalysis = biasAnalysis.id
```

**En `ModelExplainabilityBusinessService.executeExplainability()`:**
```java
// Similar a bias analysis
1. Crear/obtener experimento
2. Crear run (tipo: EXPLAINABILITY)
3. Ejecutar análisis
4. Actualizar run con métricas
5. Asociar run.idxexplainability = explainability.id
```

**En `ModelPerformanceBusinessService`:**
```java
// Similar para performance tests
```

### 5. DTOs

**Ubicación:** `codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/governance/`

**Archivos a crear:**
- `GovernanceExperimentDto.java`
- `GovernanceRunDto.java`
- `GovernanceRunCreateDto.java`
- `GovernanceRunComparisonDto.java`

### 6. Endpoints REST

**Microservicio:** `codeflowx-governance-models-service` (o crear nuevo `codeflowx-governance-testing-service`)

**Ubicación:** `codeflowx-governance-models-service/src/main/java/com/codeflowx/govern/models/controller/`

**Endpoints:**

```
# Experiments
GET    /api/v1/governance/experiments
POST   /api/v1/governance/experiments
GET    /api/v1/governance/experiments/{id}
PUT    /api/v1/governance/experiments/{id}
DELETE /api/v1/governance/experiments/{id}

GET    /api/v1/governance/experiments/entity/{entityType}/{entityId}

# Runs
GET    /api/v1/governance/experiments/{experimentId}/runs
POST   /api/v1/governance/experiments/{experimentId}/runs
GET    /api/v1/governance/runs/{id}
PUT    /api/v1/governance/runs/{id}
DELETE /api/v1/governance/runs/{id}

GET    /api/v1/governance/runs/entity/{entityType}/{entityId}
GET    /api/v1/governance/runs/compare?runIds=1,2,3

# Métricas y artefactos
POST   /api/v1/governance/runs/{id}/metrics
POST   /api/v1/governance/runs/{id}/artifacts
```

### 7. BFF (Backend for Frontend)

**Ubicación:** `codeflowx.govern.bff.compliance`

**Modificar:**
- `ModelsService.java` - Agregar métodos para experiments/runs
- `ModelsServiceImpl.java` - Implementar llamadas al microservicio
- `ModelsController.java` - Exponer endpoints del BFF

---

## 🎨 IMPLEMENTACIÓN FRONTEND

### 1. Pantallas Principales

**Ubicación:** `codeflowx-studio/app/(app)/governance/testing/`

**Estructura:**
```
/governance/testing
  /experiments              # Listado de experimentos
  /experiments/[id]         # Detalle de experimento con runs
  /runs                     # Listado de todos los runs (cross-entity)
  /runs/[id]                # Detalle de run
  /compare                  # Comparación de runs
```

### 2. Integración con Módulos Existentes

**En módulo de Modelos:**
- `app/(app)/models/registry/[id]/testing/page.tsx` - Integrar con sistema de experiments/runs
- Crear runs automáticamente cuando se ejecuten análisis

**Futuras integraciones:**
- `app/(app)/prompts/.../testing` - Testing de prompts
- `app/(app)/agents/.../testing` - Testing de agentes
- `app/(app)/rag/.../testing` - Testing de RAG
- `app/(app)/datasets/.../testing` - Testing de datasets

### 3. Componentes UI

**Reutilizar componentes existentes:**
- Card, Button, Input, Select, Tabs (ver `codeflowx-studio/components/ui`)
- Tablas con paginación
- Gráficos (Chart.js) para métricas

**Nuevos componentes:**
- `RunComparisonTable` - Tabla comparativa de runs
- `MetricsChart` - Gráfico de métricas
- `ArtifactList` - Lista de artefactos con descarga
- `RunStatusBadge` - Badge de estado de run

---

## 🔌 INTEGRACIÓN CON MICROSERVICIOS PYTHON

### Clientes Python Existentes

**Referencias:**
- `codeflowx.govern.nocode.client.AIGovernanceClient`
- Ver cómo se usa en `ModelBiasAnalysisBusinessService` y `ModelExplainabilityBusinessService`

**Servicios disponibles:**
1. **Bias Detection** (puerto 8001)
   - Endpoint: `/api/v1/bias/analyze`
   - Método: `AIGovernanceClient.biasDetection().analyzeBias(...)`

2. **AI Interpreter** (puerto 8011)
   - Endpoint: `/api/v1/explain/predictions`
   - Método: `AIGovernanceClient.explainPredictions(...)`

**Para futuros servicios:**
- Prompt testing, Agent evaluation, RAG testing, Dataset validation
- Seguir el mismo patrón de integración

---

## 📋 PLAN DE IMPLEMENTACIÓN

### Fase 1: Backend Core (Semana 1-2)
1. ✅ Crear entidades JPA: `GovernanceExperiment`, `GovernanceRun`
2. ✅ Crear repositorios
3. ✅ Crear servicios de negocio básicos (CRUD)
4. ✅ Crear DTOs
5. ✅ Crear endpoints REST en microservicio

### Fase 2: Integración con Modelos (Semana 2-3)
1. ✅ Modificar `ModelBiasAnalysisBusinessService` para crear/actualizar runs
2. ✅ Modificar `ModelExplainabilityBusinessService` para crear/actualizar runs
3. ✅ Modificar `ModelPerformanceBusinessService` para crear/actualizar runs
4. ✅ Extraer métricas de análisis y guardarlas en `govrunmetrics`

### Fase 3: Frontend Core (Semana 3-4)
1. ✅ Crear pantalla de listado de experimentos
2. ✅ Crear pantalla de detalle de experimento con runs
3. ✅ Crear pantalla de detalle de run
4. ✅ Integrar con módulo de modelos existente

### Fase 4: Funcionalidades Avanzadas (Semana 4-5)
1. ✅ Comparación de runs
2. ✅ Gráficos de métricas
3. ✅ Gestión de artefactos
4. ✅ Filtros y búsqueda avanzada

### Fase 5: Integración con Otras Entidades (Futuro)
1. ⏳ Integración con Prompts
2. ⏳ Integración con Agentes
3. ⏳ Integración con RAG
4. ⏳ Integración con Datasets

---

## ✅ CRITERIOS DE ACEPTACIÓN

### Backend:
- ✅ Entidades JPA creadas y funcionando
- ✅ Repositorios con métodos de búsqueda eficientes
- ✅ Servicios de negocio con lógica completa
- ✅ Endpoints REST documentados y funcionando
- ✅ Integración con análisis existentes (modelos)
- ✅ JSONB correctamente parseado y utilizado

### Frontend:
- ✅ UI organizada como MLflow (experiments → runs)
- ✅ Visualización de métricas y parámetros
- ✅ Gestión de artefactos
- ✅ Comparación de runs
- ✅ Integración con módulos existentes

### Calidad:
- ✅ Tests unitarios para servicios
- ✅ Tests de integración para endpoints
- ✅ Validación de datos
- ✅ Manejo de errores robusto
- ✅ Logging apropiado

---

## 📝 NOTAS IMPORTANTES

1. **No reinventar la rueda:** Reutilizar código existente (servicios, clientes, componentes UI)

2. **Backward compatibility:** No romper funcionalidades existentes, solo agregar

3. **Extensibilidad:** Diseñar para soportar fácilmente nuevas entidades (prompts, agentes, RAG, datasets)

4. **Performance:** Usar índices GIN en campos JSONB para búsquedas rápidas

5. **Artefactos:** Usar S3 o almacenamiento similar, solo guardar referencias en BD

6. **Seguridad:** Validar permisos, sanitizar inputs, proteger endpoints

7. **Documentación:** Documentar APIs con OpenAPI/Swagger

---

## 🚀 COMANDO PARA AGENTE AI

```
Implementa el sistema de experimentos y runs estilo MLflow descrito en este prompt.

Requisitos clave:
- Sistema unificado para testing de Prompts, Modelos, Agentes, RAG y Datasets
- Organización como MLflow (experiments → runs) para reconocimiento técnico
- Integración con servicios Python existentes (bias-detection:8001, ai-interpreter:8011)
- Reutilizar arquitectura y código existente
- Seguir convenciones del proyecto (naming, estructura, JSONB)

Referencias:
- Arquitectura: codeflowx-studio/docs/prompts/governance/models/ESTADO_ACTUAL_MODULO.md
- Clientes Python: codeflowx.govern.nocode.client.AIGovernanceClient
- Ejemplo integración: ModelBiasAnalysisBusinessService.executeBiasAnalysis()

Comenzar con Fase 1 (Backend Core) y seguir el plan de implementación.
```

---

**Este prompt proporciona toda la información necesaria para implementar el sistema de experimentos y runs de manera consistente con la arquitectura existente de CodeflowX Governance.**
