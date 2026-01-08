# Propuesta: Sistema de Experimentos y Runs estilo MLflow para CodeflowX Governance

**Fecha:** Enero 2025

---

## 📚 ¿Cómo funciona MLflow?

MLflow es una plataforma open-source para gestionar el ciclo de vida completo de Machine Learning. Su componente principal es **MLflow Tracking**, que organiza ejecuciones en:

### Conceptos clave de MLflow:

1. **Experiments (Experimentos)**
   - Contenedores que agrupan múltiples **runs** relacionados
   - Tienen un nombre y pueden tener tags
   - Ejemplo: "Bias Detection Q1 2025", "Model Validation Experiments"

2. **Runs (Ejecuciones)**
   - Una ejecución individual dentro de un experimento
   - Contiene:
     - **Parámetros**: Configuración de entrada (hyperparameters, dataset path, etc.)
     - **Métricas**: Resultados numéricos con historial temporal (accuracy, loss, fairness_score)
     - **Artefactos**: Archivos (modelos serializados, visualizaciones, datasets, logs)
     - **Tags**: Metadata clave-valor (user, version, environment)
     - **Timestamps**: start_time, end_time, duration

3. **Model Registry**
   - Registro centralizado de modelos
   - Versionado y stage management (Staging, Production, Archived)

---

## 🎯 ¿Por qué necesitamos esto en CodeflowX Governance?

### Estado actual:
- ✅ Tenemos análisis individuales (ModelBiasAnalysis, ModelExplainability, ModelPerformance)
- ✅ Tenemos una interfaz frontend básica de "runs"
- ❌ **Falta**: Sistema centralizado para organizar y comparar ejecuciones
- ❌ **Falta**: Historial completo y trazabilidad de pruebas
- ❌ **Falta**: Comparación de resultados entre diferentes ejecuciones

### Beneficios de implementar un sistema tipo MLflow:

1. **Organización**
   - Agrupar runs relacionados en experimentos
   - Ejemplo: "Experimento: Validación Bias Q1 2025" contiene todos los análisis de bias ejecutados

2. **Reproducibilidad**
   - Guardar parámetros exactos de cada ejecución
   - Poder re-ejecutar análisis con la misma configuración

3. **Comparación**
   - Comparar métricas entre diferentes runs
   - Ver evolución de métricas a lo largo del tiempo
   - Identificar qué configuración produce mejores resultados

4. **Trazabilidad**
   - Historial completo de todas las pruebas realizadas
   - Auditoría para compliance y governance

5. **Integración con MLOps**
   - Ya tenemos integración con MLflow externo
   - Podemos sincronizar runs de MLflow externo con nuestro sistema interno

---

## 🏗️ Propuesta de Implementación

### 1. Entidades JPA (Backend)

#### `ModelExperiment` (Tabla: `mod_experiment`)

```java
@Entity
@Table(name = "mod_experiment")
public class ModelExperiment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "modexperimentid")
    private Long idxexperiment;

    @Column(name = "modexperimentname", nullable = false, length = 255)
    private String modexperimentname;

    @Column(name = "modexperimentdescription", columnDefinition = "TEXT")
    private String modexperimentdescription;

    // Relación con Model (opcional, puede haber experimentos sin modelo específico)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idxmodel")
    private Model model;

    // Tags como JSONB
    @Column(name = "modexperimenttags", columnDefinition = "jsonb")
    private String modexperimenttags; // {"team": "data-science", "quarter": "Q1-2025"}

    @Column(name = "modexperimentstatus", length = 50)
    private String modexperimentstatus; // ACTIVE, ARCHIVED, DELETED

    // Audit fields
    @Column(name = "modexperimentcreatedby", length = 100)
    private String modexperimentcreatedby;

    @Column(name = "modexperimentcreatedat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modexperimentcreatedat;

    @OneToMany(mappedBy = "experiment", cascade = CascadeType.ALL)
    private List<ModelRun> runs;
}
```

#### `ModelRun` (Tabla: `mod_run`)

```java
@Entity
@Table(name = "mod_run")
public class ModelRun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "modrunid")
    private Long idxrun;

    @Column(name = "modrunname", nullable = false, length = 255)
    private String modrunname;

    // Relación con Experiment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idxexperiment", nullable = false)
    private ModelExperiment experiment;

    // Relación con Model (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idxmodel")
    private Model model;

    // Tipo de run
    @Column(name = "modruntype", length = 50, nullable = false)
    private String modruntype; // BIAS_ANALYSIS, EXPLAINABILITY, PERFORMANCE, VALIDATION, CUSTOM

    // Estado
    @Column(name = "modrunstatus", length = 50, nullable = false)
    private String modrunstatus; // RUNNING, COMPLETED, FAILED, KILLED

    // Timestamps
    @Column(name = "modrunstartedat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modrunstartedat;

    @Column(name = "modrunendedat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modrunendedat;

    // Parámetros como JSONB
    @Column(name = "modrunparameters", columnDefinition = "jsonb")
    private String modrunparameters; // {"protectedAttribute": "gender", "threshold": 0.8}

    // Métricas como JSONB (historial temporal)
    @Column(name = "modrunmetrics", columnDefinition = "jsonb")
    private String modrunmetrics; // {"accuracy": [{"value": 0.95, "timestamp": "2025-01-01T10:00:00Z"}], "fairness_score": [...]}

    // Tags como JSONB
    @Column(name = "modruntags", columnDefinition = "jsonb")
    private String modruntags; // {"user": "john.doe", "version": "1.0.0"}

    // Referencias a resultados de análisis (FKs opcionales)
    @OneToOne
    @JoinColumn(name = "idxbiasanalysis")
    private ModelBiasAnalysis biasAnalysis;

    @OneToOne
    @JoinColumn(name = "idxexplainability")
    private ModelExplainability explainability;

    @OneToOne
    @JoinColumn(name = "idxperformance")
    private ModelPerformance performance;

    // Artefactos (paths a archivos almacenados)
    @Column(name = "modrunartifacts", columnDefinition = "jsonb")
    private String modrunartifacts; // [{"path": "s3://bucket/runs/123/model.pkl", "type": "model"}, ...]

    // Error message si falló
    @Column(name = "modrunerror", columnDefinition = "TEXT")
    private String modrunerror;

    // Audit fields
    @Column(name = "modruncreatedby", length = 100)
    private String modruncreatedby;

    @Column(name = "modrunupdatedat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modrunupdatedat;
}
```

### 2. Relación con Análisis Existentes

Los análisis existentes (`ModelBiasAnalysis`, `ModelExplainability`, `ModelPerformance`) se relacionan con `ModelRun`:

- **Un Run puede tener 0 o 1 análisis de cada tipo**
- Los análisis mantienen su estructura actual
- El Run actúa como contenedor y punto de referencia único

**Flujo propuesto:**
1. Usuario crea un Run dentro de un Experiment
2. Se ejecuta el análisis (bias, explainability, etc.)
3. El resultado se guarda en la entidad correspondiente (ModelBiasAnalysis, etc.)
4. El Run se actualiza con referencia al análisis y métricas extraídas

### 3. Estructura de Datos JSONB

#### `modrunmetrics` (Métricas con historial temporal):
```json
{
  "accuracy": [
    {"value": 0.95, "timestamp": "2025-01-01T10:00:00Z", "step": 0},
    {"value": 0.96, "timestamp": "2025-01-01T10:05:00Z", "step": 1}
  ],
  "fairness_score": [
    {"value": 0.85, "timestamp": "2025-01-01T10:00:00Z", "step": 0}
  ],
  "latency_ms": [
    {"value": 150, "timestamp": "2025-01-01T10:00:00Z", "step": 0}
  ]
}
```

#### `modrunparameters` (Parámetros de configuración):
```json
{
  "protectedAttribute": "gender",
  "favorableOutcome": "1",
  "threshold": 0.8,
  "method": "shap",
  "datasetPath": "s3://bucket/data/test.csv"
}
```

#### `modrunartifacts` (Referencias a archivos):
```json
[
  {
    "path": "s3://codeflowx-artifacts/runs/123/input_data.csv",
    "type": "input_data",
    "size": 1024000
  },
  {
    "path": "s3://codeflowx-artifacts/runs/123/bias_report.pdf",
    "type": "report",
    "size": 2048000
  },
  {
    "path": "s3://codeflowx-artifacts/runs/123/visualization.png",
    "type": "visualization",
    "size": 512000
  }
]
```

### 4. Endpoints REST Propuestos

#### Experiments:

```
GET    /api/v1/models/experiments                    # Listar todos los experimentos
POST   /api/v1/models/experiments                    # Crear experimento
GET    /api/v1/models/experiments/{id}               # Detalle de experimento
PUT    /api/v1/models/experiments/{id}               # Actualizar experimento
DELETE /api/v1/models/experiments/{id}               # Eliminar experimento (soft delete)

GET    /api/v1/models/{modelId}/experiments          # Experimentos de un modelo
```

#### Runs:

```
GET    /api/v1/models/experiments/{experimentId}/runs              # Listar runs de un experimento
POST   /api/v1/models/experiments/{experimentId}/runs              # Crear run
GET    /api/v1/models/runs/{runId}                                 # Detalle de run
PUT    /api/v1/models/runs/{runId}                                 # Actualizar run (métricas, parámetros)
DELETE /api/v1/models/runs/{runId}                                 # Eliminar run

GET    /api/v1/models/{modelId}/runs                               # Runs de un modelo
GET    /api/v1/models/runs/{runId}/metrics                         # Métricas de un run
POST   /api/v1/models/runs/{runId}/metrics                         # Agregar métrica a un run
GET    /api/v1/models/runs/{runId}/compare?runIds=1,2,3            # Comparar múltiples runs
```

#### Integración con análisis existentes:

```
POST   /api/v1/models/{modelId}/runs/{runId}/bias-analysis/execute      # Ejecutar bias analysis y asociar al run
POST   /api/v1/models/{modelId}/runs/{runId}/explainability/execute     # Ejecutar explainability y asociar al run
```

### 5. Frontend - Estructura Propuesta

#### Pantalla: `/models/experiments`
- Listado de experimentos
- Filtros: modelo, estado, fecha
- Métricas: total de runs, runs completados, runs fallidos

#### Pantalla: `/models/experiments/[id]`
- Detalle del experimento
- Listado de runs con tabla comparativa
- Gráficos de evolución de métricas
- Filtros y ordenamiento

#### Pantalla: `/models/runs/[id]`
- Detalle completo del run
- Pestañas:
  - **Overview**: Información general, estado, duración
  - **Parameters**: Parámetros de configuración
  - **Metrics**: Métricas con gráficos temporales
  - **Artifacts**: Lista de archivos generados
  - **Results**: Resultados de análisis (bias, explainability, performance)
  - **Logs**: Logs de ejecución

#### Mejora en: `/models/registry/[id]/testing`
- Integrar con el sistema de experiments/runs
- Mostrar runs del modelo agrupados por experimento
- Permitir crear nuevo run que automáticamente cree un experimento si no existe

### 6. Integración con MLflow Externo

Ya tenemos `MLflowIntegrationProvider` que puede sincronizar:

- **Sincronización bidireccional**:
  - Runs creados en CodeflowX → Pueden sincronizarse a MLflow externo
  - Runs de MLflow externo → Pueden importarse como `ModelRun` en CodeflowX

- **Mapeo**:
  - MLflow Experiment → `ModelExperiment`
  - MLflow Run → `ModelRun`
  - MLflow Metrics → `modrunmetrics` (JSONB)
  - MLflow Parameters → `modrunparameters` (JSONB)
  - MLflow Artifacts → `modrunartifacts` (JSONB con paths)

---

## 📋 Plan de Implementación

### Fase 1: Backend Core (Semana 1-2)
1. ✅ Crear entidades JPA: `ModelExperiment`, `ModelRun`
2. ✅ Crear repositorios
3. ✅ Crear servicios de negocio
4. ✅ Crear DTOs
5. ✅ Crear endpoints REST en microservice

### Fase 2: Integración con Análisis Existentes (Semana 2-3)
1. ✅ Modificar `ModelBiasAnalysisBusinessService` para crear/actualizar `ModelRun`
2. ✅ Modificar `ModelExplainabilityBusinessService` para crear/actualizar `ModelRun`
3. ✅ Modificar `ModelPerformanceBusinessService` para crear/actualizar `ModelRun`
4. ✅ Extraer métricas de análisis y guardarlas en `modrunmetrics`

### Fase 3: Frontend (Semana 3-4)
1. ✅ Crear pantalla de experimentos
2. ✅ Crear pantalla de detalle de experimento con comparación de runs
3. ✅ Mejorar pantalla de testing para integrar con experiments/runs
4. ✅ Crear pantalla de detalle de run
5. ✅ Implementar gráficos de métricas temporales

### Fase 4: Integración MLflow (Semana 4-5)
1. ✅ Mejorar `MLflowIntegrationProvider` para sincronización bidireccional
2. ✅ Endpoint para importar runs desde MLflow externo
3. ✅ Endpoint para exportar runs a MLflow externo

---

## 🎨 Ejemplo de Flujo de Uso

### Escenario: Validar un modelo para producción

1. **Crear Experiment**:
   ```
   POST /api/v1/models/experiments
   {
     "name": "Production Validation - GPT-4 v1.0",
     "description": "Validación completa antes de producción",
     "modelId": 123
   }
   ```

2. **Ejecutar Bias Analysis dentro del experimento**:
   ```
   POST /api/v1/models/123/runs
   {
     "experimentId": 456,
     "name": "Bias Analysis - Gender",
     "type": "BIAS_ANALYSIS",
     "parameters": {
       "protectedAttribute": "gender",
       "threshold": 0.8
     }
   }
   ```

   Luego ejecutar:
   ```
   POST /api/v1/models/123/runs/{runId}/bias-analysis/execute
   (con archivo CSV)
   ```

3. **Ver resultados**:
   - El run se actualiza automáticamente con:
     - Estado: COMPLETED
     - Métricas extraídas del análisis (fairness_score, demographic_parity, etc.)
     - Referencia a `ModelBiasAnalysis`
     - Artefactos generados (reportes PDF, visualizaciones)

4. **Comparar con otros runs**:
   ```
   GET /api/v1/models/runs/compare?runIds=789,790,791
   ```

   Retorna comparación de métricas entre los 3 runs.

---

## ✅ Ventajas de esta Implementación

1. **Flexibilidad**: Puede manejar cualquier tipo de análisis, no solo los existentes
2. **Extensibilidad**: Fácil agregar nuevos tipos de runs
3. **Compatibilidad**: No rompe la estructura existente, la complementa
4. **Integración**: Se integra naturalmente con MLflow externo
5. **Governance**: Proporciona trazabilidad completa para auditorías

---

## 🤔 Consideraciones

1. **Migración de datos existentes**:
   - Los análisis existentes (`ModelBiasAnalysis`, etc.) pueden crearse como `ModelRun` retroactivamente
   - Opción: Script de migración para crear runs a partir de análisis existentes

2. **Performance**:
   - JSONB es eficiente para búsquedas en PostgreSQL
   - Índices GIN sobre campos JSONB para búsquedas rápidas

3. **Almacenamiento de artefactos**:
   - Usar S3 o similar para almacenar archivos grandes
   - `modrunartifacts` solo almacena referencias (paths)

---

## 📝 Conclusión

Implementar un sistema de experiments/runs estilo MLflow proporcionaría:

- ✅ Mejor organización de pruebas y validaciones
- ✅ Trazabilidad completa para governance
- ✅ Facilidad para comparar y analizar resultados
- ✅ Integración natural con herramientas MLOps externas
- ✅ Base sólida para futuras funcionalidades de ML Ops

**Recomendación**: ✅ **Sí, implementar este sistema**. Sería un gran valor agregado para el módulo de governance de modelos.
