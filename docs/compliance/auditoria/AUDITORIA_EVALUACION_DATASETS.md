# AUDITORÍA - EVALUACIÓN DE DATASETS
**Fecha:** Noviembre 2025  
**Auditor:** Sistema de Gobierno de IA - CodeflowX  
**Base Legal:** EU AI Act Art. 10, Art. 15, ISO 42001  
**Artículos Relevantes:** Art. 10 (Quality criteria for training data), Art. 15 (Accuracy and robustness), ISO 42001 (Data quality management)

---

## 1. TAMAÑO MÁXIMO SOPORTADO

### 1.1 Límites Técnicos

**Microservicio Principal:** `leka-bias-detection-service` (Puerto 8001)

| Componente | Límite Máximo | Configurable | Ubicación |
|------------|---------------|--------------|-----------|
| **Archivo CSV** | **1024 MB (1 GB)** | ✅ SÍ (variable `MAX_FILE_SIZE_MB`, default: 100MB) | `leka-bias-detection-service/utils/file_validation.py` ⭐ ACTUALIZADO |
| **Timeout** | **Adaptativo** (30s-600s) | ✅ SÍ (variables `BASE_TIMEOUT_SECONDS`, `TIMEOUT_PER_MB`) | `leka-bias-detection-service/utils/adaptive_timeout.py` ⭐ ACTUALIZADO |
| **Training Module** | **10 GB** (10,240 MB) | ✅ SÍ | `codeflowx-studio/docs/portal-backend/05_TRAINING_MODULE.md:1969` |
| **Documentos UI** | **50 MB por archivo** | ❌ NO | `DocumentUploadViewModel.java:112` |

### 1.2 Configuración Actual

**Archivo:** `leka-bias-detection-service/utils/file_validation.py` ⭐ ACTUALIZADO (Nov 2025)

```python
# Validación configurable con límite máximo de 1GB
from utils.file_validation import get_file_validator

file_validator = get_file_validator()
file_validator.validate_and_raise(file_size_mb)  # Valida tamaño y memoria
```

**Variables de Entorno:** ⭐ ACTUALIZADO (Nov 2025)
```bash
MAX_FILE_SIZE_MB=100          # Default: 100MB, máximo: 1024MB (1GB)
BASE_TIMEOUT_SECONDS=30       # Timeout base
TIMEOUT_PER_MB=2.0            # Segundos adicionales por MB
MAX_TIMEOUT_SECONDS=600       # Timeout máximo (10 minutos)
MIN_TIMEOUT_SECONDS=30        # Timeout mínimo
```

### 1.3 Estrategias para Datasets Grandes

**Para datasets > 100 MB:** ⭐ ACTUALIZADO (Nov 2025)
1. **Streaming Automático:** ⭐ NUEVO - El sistema usa procesamiento por chunks automáticamente
   - Endpoint `/api/data-quality/validate` detecta archivos > 100MB y usa streaming
   - Nuevo endpoint `/api/data-quality/validate-streaming` para streaming explícito
   - Procesamiento por chunks de 10,000 filas (configurable)
   - Mantiene precisión estadística con agregación incremental
2. **Sampling:** El sistema permite muestreo estadístico para análisis
3. **Chunking:** Procesamiento por lotes (batches)
4. **Validación de Memoria:** ⭐ NUEVO - Valida memoria disponible antes de procesar (requiere 3x tamaño)
5. **Timeout Adaptativo:** ⭐ NUEVO - Timeout dinámico según tamaño de archivo
6. **MinIO Storage:** Datasets grandes se almacenan en MinIO (hasta 4 TB proyectados año 5)

**Código de Sampling:**
```python
# bias-detection-service/services/data_quality_service.py
def validate_dataset(self, df: pd.DataFrame, ...):
    # Auto-sampling para datasets grandes
    if len(df) > 100000:
        sample_size = min(100000, int(len(df) * 0.1))
        df = df.sample(n=sample_size, random_state=42)
        logger.info(f"Sampling dataset: {len(df)} rows for analysis")
```

---

## 2. PROBLEMAS DETECTADOS AUTOMÁTICAMENTE

### 2.1 Categorías de Problemas Detectados

El sistema detecta automáticamente **8 categorías principales** de problemas:

| Categoría | Endpoint | Método de Detección | Severidad |
|-----------|----------|---------------------|-----------|
| **1. Bias (Sesgo)** | `/api/bias-analysis/analyze` | Demographic Parity, Equal Opportunity, Disparate Impact | LOW/MODERATE/HIGH/CRITICAL |
| **2. Drift (Deriva)** | `/api/drift/detect` | Kolmogorov-Smirnov test, Chi-square, PSI | NO_DRIFT/MODERATE/SEVERE |
| **3. Anomalías (Outliers)** | `/api/data-quality/validate` | IQR method (Interquartile Range) | Detección automática |
| **4. Duplicados** | `/api/data-quality/validate` | Hash-based duplicate detection | Porcentaje de duplicados |
| **5. Toxicidad** | `/api/llm/evaluate-toxicity` | Detección de contenido tóxico (hate speech, etc.) | Score 0-1 |
| **6. Leakage (Filtrado)** | `/api/tabular/detect-label-leakage` | Detección de información de target en features | CRITICAL si detectado |
| **7. Missing Values** | `/api/data-quality/validate` | Análisis de valores faltantes | Porcentaje por columna |
| **8. Data Type Inconsistency** | `/api/data-quality/validate` | Validación de tipos de datos | Warnings/Errors |

### 2.2 Detección de Bias

**Microservicio:** `leka-bias-detection-service`  
**Endpoint:** `/api/bias-analysis/analyze`

**Métricas Calculadas:**
- **Demographic Parity:** Diferencia en tasas de predicción positiva entre grupos
- **Equal Opportunity:** Diferencia en True Positive Rates
- **Disparate Impact:** Ratio entre grupos (regla del 80%)

**Clasificación de Severidad:**
- **NO_BIAS:** < 5% desviación
- **LOW:** 5-10% desviación
- **MODERATE:** 10-20% desviación
- **HIGH:** 20-30% desviación
- **CRITICAL:** > 30% desviación

**Código:**
```python
# bias-detection-service/main_v2_complete.py:339-407
@app.post("/api/bias-analysis/analyze")
async def analyze_bias(
    file: UploadFile = File(...),
    protected_attribute: str = Form(...),
    threshold: float = Form(default=0.8)
):
    # Análisis de sesgo con métricas de fairness
    result = bias_service.analyze_bias(df, protected_attribute, threshold)
    return BiasAnalysisResponse(
        fairness_score=result.fairness_score,
        bias_severity=result.classification,
        metrics=result.metrics
    )
```

### 2.3 Detección de Drift

**Microservicio:** `leka-bias-detection-service`  
**Endpoint:** `/api/drift/detect`

**Pruebas Estadísticas:**
- **Features numéricos:** Kolmogorov-Smirnov test (p-value threshold: 0.05)
- **Features categóricos:** Chi-square test
- **PSI (Population Stability Index):** Estabilidad de población

**Interpretación PSI:**
- **< 0.1:** Sin drift
- **0.1-0.2:** Drift moderado
- **> 0.2:** Drift severo

**Código:**
```python
# bias-detection-service/services/drift_detection_service.py:29-49
def detect_data_drift(
    self,
    reference_data: pd.DataFrame,
    current_data: pd.DataFrame,
    numerical_features: List[str],
    categorical_features: List[str]
) -> Dict:
    # KS test para numéricos
    for feature in numerical_features:
        ks_stat, p_value = stats.ks_2samp(
            reference_data[feature],
            current_data[feature]
        )
        if p_value < self.drift_threshold:
            drift_detected = True
    
    # PSI calculation
    psi = calculate_psi(reference_data, current_data, feature)
    return DriftAnalysisResponse(psi=psi, drift_detected=drift_detected)
```

### 2.4 Detección de Anomalías (Outliers)

**Método:** IQR (Interquartile Range)

**Algoritmo:**
1. Calcula Q1 (percentil 25) y Q3 (percentil 75)
2. Calcula IQR = Q3 - Q1
3. Identifica outliers: valores < Q1 - 1.5*IQR o > Q3 + 1.5*IQR

**Código:**
```python
# bias-detection-service/services/data_quality_service.py:84-90
def _detect_outliers(self, df: pd.DataFrame, numerical_features: List[str]):
    outliers = {}
    for feature in numerical_features:
        Q1 = df[feature].quantile(0.25)
        Q3 = df[feature].quantile(0.75)
        IQR = Q3 - Q1
        lower_bound = Q1 - 1.5 * IQR
        upper_bound = Q3 + 1.5 * IQR
        outliers[feature] = df[(df[feature] < lower_bound) | (df[feature] > upper_bound)]
    return outliers
```

### 2.5 Detección de Duplicados

**Método:** Hash-based duplicate detection

**Código:**
```python
# bias-detection-service/services/data_quality_service.py:74-82
def _analyze_duplicates(self, df: pd.DataFrame):
    n_duplicates = df.duplicated().sum()
    duplicate_percentage = (n_duplicates / len(df)) * 100
    
    if duplicate_percentage > self.quality_thresholds["duplicate_threshold"]:
        results["issues"].append({
            "severity": "MEDIUM",
            "category": "Duplicates",
            "message": f"{n_duplicates} duplicate records found"
        })
    return {
        "n_duplicates": n_duplicates,
        "duplicate_percentage": duplicate_percentage
    }
```

### 2.6 Detección de Toxicidad

**Microservicio:** `leka-llm-evaluation`  
**Endpoint:** `/api/llm/evaluate-toxicity`

**Modelo:** Detección de contenido tóxico (hate speech, discriminación, etc.)

**Umbral:** ≤ 0.05 (5% de toxicidad máximo permitido)

### 2.7 Detección de Label Leakage

**Endpoint:** `/api/tabular/detect-label-leakage`

**Método:** Detecta si información del target está presente en features

**Severidad:** CRITICAL si detectado (requiere acción inmediata)

---

## 3. MODELO O PIPELINE DE EVALUACIÓN

### 3.1 Arquitectura del Pipeline

```
┌─────────────────────────────────────────────────────────────┐
│ 1. Carga de Dataset                                          │
│    - Validación formato (CSV, JSON, Parquet)                │
│    - Validación tamaño (< 100 MB)                            │
│    - Parsing y estructura                                    │
└──────────────┬──────────────────────────────────────────────┘
               │
               ├─> 2. Data Profiling
               │   - Análisis de tipos de datos
               │   - Estadísticas descriptivas
               │   - Detección automática de features numéricos/categóricos
               │
               ├─> 3. Data Quality Validation (Paralelo)
               │   ├─> Missing Values Analysis
               │   ├─> Duplicate Detection
               │   ├─> Outlier Detection (IQR)
               │   ├─> Data Type Consistency
               │   └─> Class Imbalance Analysis
               │
               ├─> 4. Bias Detection (Si hay target + protected attribute)
               │   ├─> Demographic Parity
               │   ├─> Equal Opportunity
               │   ├─> Disparate Impact
               │   └─> Statistical Tests (KS, Chi-square)
               │
               ├─> 5. Drift Detection (Si hay datos de referencia)
               │   ├─> Kolmogorov-Smirnov Test
               │   ├─> Chi-square Test
               │   └─> PSI Calculation
               │
               ├─> 6. Privacy Analysis (Si aplica)
               │   ├─> PII Detection (Microsoft Presidio)
               │   ├─> k-anonymity
               │   ├─> l-diversity
               │   └─> t-closeness
               │
               └─> 7. Aggregation y Scoring
                   ├─> Quality Score (0-100)
                   ├─> Risk Level (EXCELLENT/GOOD/FAIR/POOR/CRITICAL)
                   └─> Decision (APPROVED/REVIEW_REQUIRED/REJECTED)
```

### 3.2 Microservicios Involucrados

| Microservicio | Puerto | Responsabilidad |
|---------------|--------|-----------------|
| **leka-bias-detection-service** | 8001 | Data quality, bias, drift, duplicates, outliers |
| **leka-llm-evaluation** | 8002 | Toxicidad, safety, PII leakage |
| **leka-prompt-governance** | 8003 | PII detection, prompt safety |
| **leka-rag-evaluation** | 8004 | RAG quality (si aplica) |

### 3.3 Workflow BPMN

**Proceso:** `dataset-quality-v1.bpmn`

**Delegates Java:**
- `DataProfilingDelegate` → Análisis inicial
- `ValidateDatasetFormatDelegate` → Validación formato
- `DetectDatasetBiasDelegate` → Detección de sesgo
- `AnalyzeDriftDelegate` → Detección de drift
- `StoreEvaluationDelegate` → Almacenamiento resultados

**Flujo:**
```
Start Event
    ↓
Validate Format (Service Task)
    ↓
Data Profiling (Service Task)
    ↓
Quality Evaluation (Service Task) → leka-bias-detection-service
    ↓
Bias Detection (Service Task) → leka-bias-detection-service
    ↓
Drift Detection (Service Task) → leka-bias-detection-service
    ↓
Label Leakage Detection (Service Task) → leka-bias-detection-service
    ↓
Aggregate Results (Script Task)
    ↓
Human Review? (Gateway)
    ├─> YES → User Task (HITL Review)
    └─> NO → Store Results
    ↓
End Event
```

**Código Delegate:**
```java
// DetectDatasetBiasDelegate.java:77-117
public void execute(DelegateExecution execution) {
    // 1. Preparar request
    Map<String, Object> request = new HashMap<>();
    request.put("dataset_id", datasetId);
    request.put("target_column", targetColumn);
    request.put("fairness_metrics", List.of("demographic_parity", "equal_opportunity", "disparate_impact"));
    
    // 2. Llamar endpoint Python
    String url = evaluationServiceUrl + "/api/v1/evaluation/dataset/bias";
    ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
    
    // 3. Parsear response
    JsonNode jsonResponse = objectMapper.readTree(response.getBody());
    Double fairnessScore = jsonResponse.get("fairness_score").asDouble();
    String biasSeverity = jsonResponse.get("bias_severity").asText();
    
    // 4. Guardar en variables BPMN
    execution.setVariable("biasScore", fairnessScore);
    execution.setVariable("biasSeverity", biasSeverity);
}
```

### 3.4 Modelos y Algoritmos Utilizados

**1. Bias Detection:**
- **Algoritmo:** Statistical parity, equalized odds
- **Tests:** Kolmogorov-Smirnov, Chi-square
- **Librerías:** pandas, numpy, scipy

**2. Drift Detection:**
- **Algoritmo:** PSI (Population Stability Index)
- **Tests:** KS test, Chi-square test
- **Librerías:** scipy.stats

**3. Outlier Detection:**
- **Algoritmo:** IQR (Interquartile Range)
- **Método:** Q1 - 1.5*IQR, Q3 + 1.5*IQR
- **Librerías:** pandas

**4. Duplicate Detection:**
- **Algoritmo:** Hash-based deduplication
- **Método:** pandas.duplicated()
- **Librerías:** pandas

**5. Privacy Analysis:**
- **Algoritmo:** Microsoft Presidio (PII detection)
- **Métodos:** k-anonymity, l-diversity, t-closeness
- **Librerías:** presidio-analyzer, presidio-anonymizer

---

## 4. ALMACENAMIENTO DE RESULTADOS

### 4.1 Tablas de Base de Datos

**Base de Datos:** PostgreSQL  
**Esquema:** Nomenclatura ENART (prefijos de 3 caracteres)

#### **Tabla Principal: DQLDATASETQUALITIES**

**Prefijo:** DQL  
**Tabla:** `DQLDATASETQUALITIES`

**Campos Clave:**
- `IDXDATASETQUALITY` (PK, BIGSERIAL)
- `DQLEVALUATIONID` (VARCHAR, único)
- `DQLDATASETNAME` (VARCHAR)
- `DQLDATASETPATH` (TEXT)
- `DQLOVERALLSCORE` (DECIMAL, 0-100)
- `DQLQUALITYRATING` (TEXT: EXCELLENT/GOOD/FAIR/POOR/CRITICAL)
- `DQLCOMPLETENESSCORE` (DECIMAL)
- `DQLVALIDITYSCORE` (DECIMAL)
- `DQLUNIQUENESSCORE` (DECIMAL)
- `DQLCONSISTENCYSCORE` (DECIMAL)
- `DQLTOTALROWS` (BIGINT)
- `DQLTOTALCOLUMNS` (INTEGER)
- `DQLMISSINGPERCENTAGE` (DECIMAL)
- `DQLOUTLIERSPERCENTAGE` (DECIMAL)
- `DQLDUPLICATESPERCENTAGE` (DECIMAL)
- `DQLDECISION` (TEXT: APPROVED/REVIEW_REQUIRED/REJECTED)
- `DQLREQUIRESHUMANREVIEW` (BOOLEAN)
- `DQLJUSTIFICATION` (TEXT)
- `DQLPROJECTID` (BIGINT, FK a PRJPROJECTS)
- `DQLCREATEDBY` (BIGINT, FK a CORUSERS)
- `DQLCREATEDAT` (TIMESTAMP)
- `DQLSTATUS` (TEXT: COMPLETED/IN_PROGRESS/FAILED)
- `DQLEXECUTIONTIME` (INTEGER, segundos)
- `DQLPROCESSINSTANCEID` (VARCHAR, FK a BPMN)

**Ubicación SQL:**
```sql
-- sql-scripts/08_dataset_quality_tables.sql
CREATE TABLE IF NOT EXISTS DQLDATASETQUALITIES (
    IDXDATASETQUALITY BIGSERIAL PRIMARY KEY,
    DQLEVALUATIONID VARCHAR(100) UNIQUE NOT NULL,
    DQLDATASETNAME VARCHAR(255) NOT NULL,
    DQLOVERALLSCORE DECIMAL(5,2),
    DQLDECISION TEXT[] NOT NULL,
    -- ... más campos
);
```

#### **Tabla de Métricas: GOVMODELEVALUATIONS**

**Prefijo:** GOV  
**Tabla:** `GOVMODELEVALUATIONS`

**Campos Clave:**
- `IDXMODELEVALUATION` (PK, BIGSERIAL)
- `MODELID` (BIGINT, FK)
- `EVALUATIONTYPE` (TEXT[])
- `DATASETID` (BIGINT, FK)
- `EVALUATIONCONFIG` (JSONB)
- `OVERALLSCORE` (DECIMAL)
- `CONFIDENCEINTERVAL` (JSONB)

**Ubicación SQL:**
```sql
-- sql-scripts/01_create_tables.sql:1060-1080
CREATE TABLE IF NOT EXISTS GOVMODELEVALUATIONS (
    IDXMODELEVALUATION BIGSERIAL PRIMARY KEY,
    MODELID BIGINT NOT NULL,
    DATASETID BIGINT,
    EVALUATIONCONFIG JSONB,
    OVERALLSCORE DECIMAL,
    -- ... más campos
);
```

#### **Tabla de Análisis de Sesgo: MODMODELBIASANALYSES**

**Prefijo:** MOD  
**Tabla:** `MODMODELBIASANALYSES`

**Campos Clave:**
- `IDXMODELBIASANALYSIS` (PK, BIGSERIAL)
- `MODBIASTYPE` (TEXT[])
- `MODBIASDESCRIPTION` (TEXT)
- `MODSEVERITY` (VARCHAR: LOW/MODERATE/HIGH/CRITICAL)
- `MODBIASSCORE` (DECIMAL)
- `MODAFFECTEDGROUPS` (JSONB)
- `MODMITIGATIONSTRATEGIES` (JSONB)
- `MODANALYSISDATA` (JSONB)

**Ubicación SQL:**
```sql
-- sql-scripts/01_create_tables.sql:1082-1100
CREATE TABLE IF NOT EXISTS MODMODELBIASANALYSES (
    IDXMODELBIASANALYSIS BIGSERIAL PRIMARY KEY,
    MODBIASTYPE TEXT[] NOT NULL,
    MODSEVERITY VARCHAR(50) NOT NULL,
    MODBIASSCORE DECIMAL,
    MODAFFECTEDGROUPS JSONB,
    -- ... más campos
);
```

### 4.2 Almacenamiento en MinIO

**Bucket:** `datasets`

**Estructura:**
```
datasets/
  ├── raw/              # Datasets originales
  ├── processed/        # Datasets procesados
  ├── evaluations/      # Resultados de evaluaciones (JSON)
  └── artifacts/       # Artefactos de evaluación (gráficos, reportes)
```

**Tamaño Proyectado Año 5:**
- Datasets: 1,000 datasets × 1 GB avg = **1 TB**
- Evaluaciones: Resultados JSON (~10 MB por evaluación) = **10 GB**

### 4.3 Logs Inmutables

**Tabla:** `IMLIMMUTABLELOGS`  
**Prefijo:** IML

**Eventos Registrados:**
- `DATASET_QUALITY_EVALUATED` → Métricas calidad + resultados
- `BIAS_ANALYSIS_COMPLETED` → Sesgos detectados + scores
- `DRIFT_DETECTED` → Drift detectado + métricas PSI
- `LABEL_LEAKAGE_DETECTED` → Leakage detectado (CRITICAL)

**Estructura del Log:**
```json
{
  "entityType": "DATASET",
  "entityId": 12345,
  "action": "DATASET_QUALITY_EVALUATED",
  "userId": 67890,
  "userName": "system",
  "data": {
    "datasetId": 12345,
    "evaluationId": "EVAL-2025-11-15-001",
    "qualityScore": 87.5,
    "biasScore": 0.12,
    "driftDetected": false,
    "issues": ["Missing values > 5% in column 'age'"],
    "timestamp": "2025-11-15T15:00:00Z"
  },
  "timestamp": "2025-11-15T15:00:00Z",
  "previousHash": "abc123...",
  "currentHash": "def456..."
}
```

### 4.4 Persistencia desde Microservicios

**Flujo de Almacenamiento:**
```
1. Microservicio Python procesa evaluación
   ↓
2. Retorna JSON con resultados
   ↓
3. Delegate Java recibe respuesta
   ↓
4. Guarda en PostgreSQL (DQLDATASETQUALITIES)
   ↓
5. Genera log inmutable (IMLIMMUTABLELOGS)
   ↓
6. Si hay artefactos → Guarda en MinIO
```

**Código:**
```java
// StoreEvaluationDelegate.java:29-64
public void execute(DelegateExecution execution) {
    // 1. Crear registro de evaluación
    DatasetQuality quality = new DatasetQuality();
    quality.setDqlevaluationid(evaluationId);
    quality.setDqldatasetname(datasetName);
    quality.setDqloverallscore(overallScore);
    quality.setDqldecision(decision);
    
    // 2. Guardar en BD
    businessService.save(quality);
    
    // 3. Generar log inmutable
    ImmutableLog log = new ImmutableLog();
    log.setImlentitytype("DATASET");
    log.setImlentityid(quality.getIdxdatasetquality());
    log.setImlaction("DATASET_QUALITY_EVALUATED");
    log.setImldata(serializeResults(results));
    immutableLoggingService.log(log);
}
```

---

## 5. PANTALLAS QUE MUESTRAN HALLAZGOS

### 5.1 Dashboard Principal de Calidad de Datasets

**Ubicación:** `/console/platform/views/governance/dataset-quality-dashboard-overview.zul`

**ViewModel:** `DatasetQualityDashboardOverviewViewModel.java`

**KPIs Mostrados:**
1. **Total Evaluations** - Número total de evaluaciones
2. **Approved** - Evaluaciones aprobadas + tasa de aprobación
3. **Rejected** - Evaluaciones rechazadas + tasa de rechazo
4. **Pending Review** - Evaluaciones pendientes de revisión HITL
5. **Average Quality Score** - Score promedio (0-100)
6. **Overdue Items** - Items con más de 2 días pendientes

**Lista de Evaluaciones:**
- ID de evaluación
- Nombre del dataset
- Score (con colores: verde ≥90, amarillo ≥75, rojo <75)
- Quality Level (EXCELLENT/GOOD/FAIR/POOR)
- Decision (APPROVED/REVIEW_REQUIRED/REJECTED)
- Status (COMPLETED/IN_PROGRESS/FAILED)
- Días desde evaluación
- Indicador de overdue
- Project ID

**Filtros:**
- Por Decision (APPROVED/REVIEW_REQUIRED/REJECTED)
- Por Status (COMPLETED/IN_PROGRESS/FAILED)

**Código ZUL:**
```xml
<!-- dataset-quality-dashboard-overview.zul:62-100 -->
<vlayout sclass="kpi-card" width="220px">
    <label value="📊" style="font-size: 36px;"/>
    <label value="@load(vm.totalEvaluations)" sclass="kpi-value"/>
    <label value="Total Evaluations" sclass="kpi-label"/>
</vlayout>

<listbox model="@load(vm.dashboardData)">
    <listhead>
        <listheader label="ID" width="70px"/>
        <listheader label="Dataset" width="200px"/>
        <listheader label="Score" width="90px"/>
        <listheader label="Level" width="100px"/>
        <listheader label="Decision" width="140px"/>
    </listhead>
</listbox>
```

### 5.2 Pantalla de Detalle de Evaluación

**Ubicación:** `/console/platform/governance/quality/dataset.zul`

**Información Mostrada:**
- **Métricas de Calidad:**
  - Completitud (Completeness Score)
  - Validez (Validity Score)
  - Unicidad (Uniqueness Score)
  - Consistencia (Consistency Score)
  - Score Global (Overall Score)

- **Estadísticas:**
  - Total de filas
  - Total de columnas
  - Porcentaje de valores faltantes
  - Porcentaje de outliers
  - Porcentaje de duplicados

- **Problemas Detectados:**
  - Lista de issues con severidad (HIGH/MEDIUM/LOW)
  - Categoría del problema (Missing Values, Duplicates, Bias, etc.)
  - Mensaje descriptivo

- **Decision y Justificación:**
  - Decision (APPROVED/REVIEW_REQUIRED/REJECTED)
  - Justificación textual
  - Si requiere revisión humana

### 5.3 Pantallas de Model Evaluation (CodeflowX Studio)

**Ubicación:** `codeflowx-studio/app/(app)/model-evaluation/`

**Pantallas Disponibles:**
1. **Dashboard Principal** (`page.tsx`)
   - Overview de evaluaciones
   - Métricas de rendimiento
   - Actividades recientes

2. **Security Evaluation** (`security/page.tsx`)
   - Evaluaciones de seguridad
   - Tests de adversarios
   - Vulnerabilidades detectadas

3. **Reports** (`reports/page.tsx`)
   - Reportes de evaluación
   - Exportación PDF/Excel

4. **HITL (Human-in-the-Loop)** (`hitl/page.tsx`)
   - Revisión humana de evaluaciones
   - Aprobación/rechazo

5. **AB Testing** (`ab-testing/page.tsx`)
   - Comparación de modelos
   - Tests A/B

### 5.4 Vista SQL para Dashboard

**Vista:** `VW_DATASET_QUALITY_DASHBOARD`

**Ubicación:** `sql-scripts/09_dataset_quality_views.sql`

**Campos Calculados:**
- `QUALITYLEVEL` - Calculado desde `DQLOVERALLSCORE`:
  - EXCELLENT: ≥ 90
  - GOOD: ≥ 75
  - FAIR: ≥ 60
  - POOR: < 60

- `DAYSSINCEEVAL` - Días desde evaluación

- `ISOVERDUE` - Boolean:
  - TRUE si `DQLDECISION = 'REVIEW_REQUIRED'` y días > 2
  - TRUE si `DQLSTATUS = 'PROCESSING'` y días > 1

**Código SQL:**
```sql
-- sql-scripts/09_dataset_quality_views.sql:13-66
CREATE OR REPLACE VIEW VW_DATASET_QUALITY_DASHBOARD AS
SELECT 
    dq.IDXDATASETQUALITY,
    dq.DQLOVERALLSCORE,
    CASE 
        WHEN dq.DQLOVERALLSCORE >= 90 THEN 'EXCELLENT'
        WHEN dq.DQLOVERALLSCORE >= 75 THEN 'GOOD'
        WHEN dq.DQLOVERALLSCORE >= 60 THEN 'FAIR'
        ELSE 'POOR'
    END AS QUALITYLEVEL,
    EXTRACT(DAY FROM NOW() - dq.DQLCREATEDAT)::INTEGER AS DAYSSINCEEVAL,
    CASE 
        WHEN dq.DQLDECISION = 'REVIEW_REQUIRED' AND EXTRACT(DAY FROM NOW() - dq.DQLCREATEDAT) > 2 THEN TRUE
        WHEN dq.DQLSTATUS = 'PROCESSING' AND EXTRACT(DAY FROM NOW() - dq.DQLCREATEDAT) > 1 THEN TRUE
        ELSE FALSE
    END AS ISOVERDUE
FROM DQLDATASETQUALITIES dq;
```

---

## 6. CUMPLIMIENTO EU AI ACT E ISO 42001

### 6.1 Cumplimiento EU AI Act Art. 10

**Art. 10: Quality criteria for training, validation and testing data**

#### **Requisitos del Artículo 10:**

| Requisito | Implementación | Evidencia |
|-----------|----------------|-----------|
| **10.1.a** - Datos relevantes, representativos y libres de errores | ✅ Validación de calidad automática | `DataQualityService.validate_dataset()` |
| **10.1.b** - Datos con sesgos mínimos | ✅ Detección de bias automática | `BiasDetectionService.analyze_bias()` |
| **10.1.c** - Datos adecuados para propósito específico | ✅ Validación de propósito en workflow BPMN | `dataset-quality-v1.bpmn` |
| **10.2** - Medidas para detectar y corregir sesgos | ✅ Detección + recomendaciones automáticas accionables (INC-011) | `MODMITIGATIONSTRATEGIES` (JSONB) + `POST /api/dataset-quality/generate-recommendations` |
| **10.3** - Documentación de datasets | ✅ Almacenamiento en `DQLDATASETQUALITIES` | Tabla con campos de documentación |

#### **Implementación Técnica:**

**1. Validación de Representatividad:**
```python
# bias-detection-service/services/data_quality_service.py
def validate_dataset(self, df: pd.DataFrame, ...):
    # Verificar balance de clases
    if target_column:
        class_distribution = df[target_column].value_counts(normalize=True)
        min_class_proportion = class_distribution.min()
        
        if min_class_proportion < 0.20:  # 20% mínimo
            results["issues"].append({
                "severity": "HIGH",
                "category": "Class Imbalance",
                "message": f"Class imbalance detected: {min_class_proportion:.2%}"
            })
```

**2. Detección de Sesgos:**
```python
# bias-detection-service/main.py:276-407
@app.post("/api/bias-analysis/analyze")
async def analyze_bias(...):
    # Métricas de fairness
    demographic_parity = calculate_demographic_parity(df, protected_attribute)
    equal_opportunity = calculate_equal_opportunity(df, protected_attribute)
    disparate_impact = calculate_disparate_impact(df, protected_attribute)
    
    # Clasificación de severidad
    if disparate_impact < 0.80:  # Regla del 80%
        classification = "CRITICAL"
    elif demographic_parity > 0.30:
        classification = "HIGH"
    # ...
```

**3. Documentación de Datasets:**
```java
// ModelEvaluation.java (entidad extendida)
@Column(name = "MODTRAININGDATASETDOC")
private String modTrainingDatasetDoc;  // Documentación dataset entrenamiento

@Column(name = "MODVALIDATIONDATASETDOC")
private String modValidationDatasetDoc;  // Documentación dataset validación

@Column(name = "MODTESTDATASETDOC")
private String modTestDatasetDoc;  // Documentación dataset test
```

### 6.2 Cumplimiento EU AI Act Art. 15

**Art. 15: Accuracy, robustness and cybersecurity**

#### **Requisitos del Artículo 15:**

| Requisito | Implementación | Evidencia |
|-----------|----------------|-----------|
| **15.1** - Precisión adecuada | ✅ Métricas de accuracy en evaluaciones | `GOVMODELEVALUATIONS.OVERALLSCORE` |
| **15.2** - Robustez ante errores | ✅ Detección de outliers y anomalías | `DQLOUTLIERSPERCENTAGE` |
| **15.3** - Resiliencia ante ataques | ✅ Tests adversariales (si aplica) | `leka-adversarial-robustness` |

#### **Implementación Técnica:**

**1. Métricas de Precisión:**
```java
// ModelEvaluation.java
@Column(name = "OVERALLSCORE")
private Double overallScore;  // Score global 0-100

@Column(name = "CONFIDENCEINTERVAL")
private String confidenceInterval;  // JSON con intervalos de confianza
```

**2. Detección de Robustez:**
```python
# bias-detection-service/services/data_quality_service.py
def _detect_outliers(self, df: pd.DataFrame, numerical_features: List[str]):
    # IQR method para detectar outliers
    outliers_percentage = (outliers_count / total_rows) * 100
    
    if outliers_percentage > 5.0:  # 5% máximo
        results["issues"].append({
            "severity": "MEDIUM",
            "category": "Outliers",
            "message": f"{outliers_percentage:.2f}% outliers detected"
        })
```

### 6.3 Cumplimiento ISO 42001

**ISO 42001: Artificial intelligence management system**

#### **Requisitos ISO 42001:**

| Cláusula | Requisito | Implementación | Evidencia |
|----------|-----------|----------------|-----------|
| **8.1.3** - Data quality management | Gestión de calidad de datos | ✅ Pipeline completo de evaluación | `dataset-quality-v1.bpmn` |
| **8.2.2** - Data governance | Gobernanza de datos | ✅ Almacenamiento en BD + logs inmutables | `DQLDATASETQUALITIES`, `IMLIMMUTABLELOGS` |
| **8.3.1** - Model performance monitoring | Monitoreo de rendimiento | ✅ Evaluaciones continuas + drift detection | `GOVMODELEVALUATIONS`, `/api/drift/detect` |
| **9.2** - Internal audit | Auditoría interna | ✅ Logs inmutables con hash chain | `IMLIMMUTABLELOGS` |
| **10.2** - Non-conformity and corrective action | No conformidades | ✅ Decision REJECTED + justificación | `DQLDECISION`, `DQLJUSTIFICATION` |

#### **Implementación Técnica:**

**1. Data Quality Management (8.1.3):**
```java
// QualityManagementSystemBusinessService.java
public DataQualityResult evaluateDatasetQuality(Long datasetId) {
    // 1. Validar calidad
    DataQualityValidationResult validation = dataQualityService.validate(datasetId);
    
    // 2. Detectar problemas
    List<QualityIssue> issues = detectIssues(validation);
    
    // 3. Calcular score
    Double qualityScore = calculateQualityScore(validation, issues);
    
    // 4. Determinar decisión
    String decision = determineDecision(qualityScore, issues);
    
    // 5. Guardar resultados
    DatasetQuality quality = saveResults(datasetId, validation, issues, qualityScore, decision);
    
    return new DataQualityResult(quality);
}
```

**2. Data Governance (8.2.2):**
```java
// ImmutableLoggingBusinessService.java
public void logDatasetEvaluation(Long datasetId, DataQualityResult result) {
    ImmutableLog log = new ImmutableLog();
    
    // Hash chain
    String previousHash = getLastLogHash("DATASET", datasetId);
    log.setImlprevioushash(previousHash);
    
    // Data snapshot
    String data = serializeResult(result);
    String currentHash = calculateSHA256(data + previousHash);
    log.setImlcurrenthash(currentHash);
    
    // Guardar (append-only)
    log.setImlentitytype("DATASET");
    log.setImlentityid(datasetId);
    log.setImlaction("DATASET_QUALITY_EVALUATED");
    log.setImldata(data);
    
    immutableLogDAO.save(log);
}
```

**3. Model Performance Monitoring (8.3.1):**
```python
# bias-detection-service/services/drift_detection_service.py
def detect_data_drift(
    self,
    reference_data: pd.DataFrame,  # Training data
    current_data: pd.DataFrame,     # Production data
    ...
) -> Dict:
    # PSI calculation
    psi = calculate_psi(reference_data, current_data, feature)
    
    # Alert si drift severo
    if psi > self.psi_thresholds["severe"]:
        alert_service.send_alert(
            "SEVERE_DRIFT_DETECTED",
            {"feature": feature, "psi": psi}
        )
    
    return DriftAnalysisResponse(psi=psi, drift_detected=psi > 0.1)
```

**4. Internal Audit (9.2):**
```java
// Verificación de integridad de logs
public IntegrityVerificationResult verifyDatasetEvaluationIntegrity(Long evaluationId) {
    List<ImmutableLog> logs = getLogsForEvaluation(evaluationId);
    
    boolean valid = true;
    for (int i = 1; i < logs.size(); i++) {
        ImmutableLog current = logs.get(i);
        ImmutableLog previous = logs.get(i - 1);
        
        // Verificar hash chain
        if (!current.getImlprevioushash().equals(previous.getImlcurrenthash())) {
            valid = false;
            // Alerta de manipulación
        }
    }
    
    return new IntegrityVerificationResult(valid);
}
```

**5. Non-conformity (10.2):**
```java
// QualityManagementSystemBusinessService.java
public void handleNonConformity(Long evaluationId, String reason) {
    DatasetQuality quality = getEvaluation(evaluationId);
    
    // Marcar como no conforme
    quality.setDqldecision("REJECTED");
    quality.setDqljustification(reason);
    quality.setDqlrequireshumanreview(true);
    
    // Crear acción correctiva
    NonConformity nonConformity = new NonConformity();
    nonConformity.setNcnDescription("Dataset quality evaluation failed: " + reason);
    nonConformity.setNcnSeverity("HIGH");
    nonConformity.setNcnStatus("OPEN");
    
    nonConformityService.create(nonConformity);
    
    // Log inmutable
    logNonConformity(evaluationId, nonConformity);
}
```

### 6.4 Matriz de Cumplimiento

| Normativa | Artículo/Cláusula | Requisito | Implementación | Estado |
|-----------|-------------------|-----------|----------------|--------|
| **EU AI Act** | Art. 10.1.a | Datos relevantes y representativos | ✅ Validación automática | ✅ CUMPLE |
| **EU AI Act** | Art. 10.1.b | Sesgos mínimos | ✅ Detección de bias | ✅ CUMPLE |
| **EU AI Act** | Art. 10.2 | Medidas para detectar sesgos + recomendaciones automáticas | ✅ Pipeline completo + `POST /api/dataset-quality/generate-recommendations` (INC-011) | ✅ CUMPLE |
| **EU AI Act** | Art. 10.3 | Documentación de datasets | ✅ Almacenamiento en BD | ✅ CUMPLE |
| **EU AI Act** | Art. 15.1 | Precisión adecuada | ✅ Métricas de accuracy | ✅ CUMPLE |
| **EU AI Act** | Art. 15.2 | Robustez | ✅ Detección de outliers | ✅ CUMPLE |
| **ISO 42001** | 8.1.3 | Data quality management | ✅ Pipeline completo | ✅ CUMPLE |
| **ISO 42001** | 8.2.2 | Data governance | ✅ Logs inmutables | ✅ CUMPLE |
| **ISO 42001** | 8.3.1 | Performance monitoring | ✅ Drift detection | ✅ CUMPLE |
| **ISO 42001** | 9.2 | Internal audit | ✅ Hash chains | ✅ CUMPLE |
| **ISO 42001** | 10.2 | Non-conformity | ✅ Decision + justificación | ✅ CUMPLE |

---

## 7. RESUMEN EJECUTIVO PARA AUDITOR

### 7.1 Capacidades del Sistema

✅ **Tamaño Máximo:** 100 MB por archivo (configurable hasta 10 GB para training module)  
✅ **Problemas Detectados:** 8 categorías (bias, drift, anomalías, duplicados, toxicidad, leakage, missing values, data type inconsistency)  
✅ **Pipeline:** Microservicios Python + Workflow BPMN + Delegates Java  
✅ **Almacenamiento:** PostgreSQL (tablas DQLDATASETQUALITIES, GOVMODELEVALUATIONS) + MinIO + Logs inmutables  
✅ **Pantallas:** Dashboard principal + detalle + vistas SQL  
✅ **Cumplimiento:** EU AI Act Art. 10, Art. 15 + ISO 42001 completo

### 7.2 Evidencias Disponibles

✅ **Código Fuente:**
- Microservicios Python: `bias-detection-service/`
- Delegates Java: `codeflowx.govern.workflow.lib/`
- ViewModels: `DatasetQualityDashboardOverviewViewModel.java`
- Pantallas ZUL: `dataset-quality-dashboard-overview.zul`

✅ **Base de Datos:**
- Tablas: `DQLDATASETQUALITIES`, `GOVMODELEVALUATIONS`, `MODMODELBIASANALYSES`
- Vistas: `VW_DATASET_QUALITY_DASHBOARD`
- Logs: `IMLIMMUTABLELOGS` (hash chain)

✅ **Documentación:**
- Scripts SQL: `sql-scripts/08_dataset_quality_tables.sql`
- Workflows BPMN: `dataset-quality-v1.bpmn`
- Documentación técnica: `docs/compliance/`

### 7.3 Métricas de Calidad Implementadas

| Métrica | Umbral Mínimo | Endpoint | Estado |
|---------|---------------|----------|--------|
| **Completitud** | ≥ 95% | `/evaluate-data-quality` | ✅ |
| **Consistencia** | ≥ 90% | `/evaluate-data-quality` | ✅ |
| **Sesgo Demográfico** | ≤ 10% | `/analyze-bias` | ✅ |
| **Drift Temporal** | ≤ 15% (PSI) | `/detect-drift` | ✅ |
| **k-anonymity** | k ≥ 5 | `/evaluate-privacy` | ✅ |
| **Toxicidad** | ≤ 5% | `/evaluate-toxicity` | ✅ |

---

## 8. CONCLUSIÓN

**Estado:** ✅ **SISTEMA COMPLETO Y LISTO PARA AUDITORÍA**

El sistema de evaluación de datasets de CodeflowX OS cumple con todos los requisitos del EU AI Act (Art. 10, Art. 15) e ISO 42001, proporcionando:

1. ✅ **Detección automática** de 8 categorías de problemas
2. ✅ **Pipeline robusto** con microservicios especializados
3. ✅ **Almacenamiento completo** en PostgreSQL + MinIO + logs inmutables
4. ✅ **Pantallas de visualización** con dashboards y detalle
5. ✅ **Cumplimiento normativo** documentado y verificado

**Recomendaciones:**
- ~~Considerar aumentar límite de 100 MB a 500 MB para datasets enterprise~~ ✅ **IMPLEMENTADO (INC-001)**
- ~~Implementar streaming para datasets muy grandes (> 1 GB)~~ ✅ **IMPLEMENTADO (INC-002)**
- Añadir visualizaciones gráficas de distribuciones de bias (INC-003)

**Mejoras Implementadas (Noviembre 2025):**
- ✅ **INC-001 RESUELTA**: Límite de tamaño de archivo configurable hasta 1GB
  - Nuevo módulo `utils/file_validation.py` con validación de tamaño y memoria
  - Límite configurable: default 100MB, máximo 1024MB (1GB)
  - Validación de memoria disponible (requiere 3x tamaño del archivo)
  - Aplicado a todos los endpoints que procesan archivos
  - Ver detalles en: `leka-bias-detection-service/CHANGELOG_INCIDENCIAS.md`

- ✅ **INC-002 RESUELTA**: Streaming para datasets grandes implementado
  - Nuevo servicio `services/streaming_data_quality_service.py` con procesamiento por chunks
  - Streaming automático para archivos > 100MB en `/api/data-quality/validate`
  - Nuevo endpoint dedicado `/api/data-quality/validate-streaming`
  - Procesamiento por chunks de 10,000 filas manteniendo precisión estadística
  - Soporte para datasets > 1GB sin OOM errors
  - Ver detalles en: `leka-bias-detection-service/CHANGELOG_INCIDENCIAS.md`

- ✅ **INC-004 RESUELTA**: Timeout adaptativo según tamaño de dataset
  - Nuevo módulo `utils/adaptive_timeout.py` con cálculo dinámico
  - Fórmula: `timeout = base + (tamaño_mb * factor)` con límites (30s-600s default)
  - Nuevo módulo `utils/progress_tracker.py` para rastreo de progreso
  - Timeout calculado automáticamente en todos los endpoints
  - Variables de entorno configurables: `BASE_TIMEOUT_SECONDS`, `TIMEOUT_PER_MB`, etc.
  - Ver detalles en: `leka-bias-detection-service/CHANGELOG_INCIDENCIAS.md`

- ✅ **INC-011 CORREGIDA**: Endpoint de recomendaciones automáticas accionables implementado
  - `POST /api/dataset-quality/generate-recommendations` en `leka-model-wrapper` (puerto 8006)
  - Genera recomendaciones específicas con código Python ejecutable
  - Priorización automática por severidad (CRITICAL > HIGH > MEDIUM > LOW)
  - Soporte para 7 categorías de problemas (Missing Values, Duplicates, Outliers, Bias, Label Leakage, Data Type Inconsistency, Class Imbalance)
  - Cumple con EU AI Act Art. 10.2 (medidas de mitigación)
  - Ver detalles en: `leka-model-wrapper/AUDITORIA_INC-011.md`

---

**Última actualización:** Noviembre 2025  
**Versión:** 1.0  
**Mantenedor:** CodeflowX Compliance Team

