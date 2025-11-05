# 🔗 INTEGRACIÓN PLATAFORMAS ENTERPRISE
## CodeflowX como Governance Overlay sobre Databricks, Spark, Snowflake, etc.

**Fecha:** 5 Noviembre 2025  
**Pregunta:** ¿Qué pasa con empresas que usan Databricks, Spark, Snowflake?  
**Respuesta:** CodeflowX se integra como **capa de governance** sobre su infraestructura existente

---

## 🎯 RESUMEN EJECUTIVO

**Problema:**
Clientes enterprise ya tienen:
- ✅ Databricks (entrenamiento ML + feature engineering)
- ✅ Snowflake (data warehouse)
- ✅ Apache Spark (procesamiento big data)
- ✅ Azure ML / SageMaker (MLOps)
- ✅ Data Lakes (S3, Azure Data Lake, GCS)
- ✅ MLflow (tracking experimentos)

**NO van a migrar TODO a CodeflowX** → Imposible y no deseable

---

## ✅ SOLUCIÓN: GOVERNANCE OVERLAY

**CodeflowX NO reemplaza su infraestructura.**  
**CodeflowX se integra SOBRE ella.**

```
┌─────────────────────────────────────────────────────────┐
│              CODEFLOWX (Governance Layer)                │
│                                                          │
│  ✅ Clasificación riesgo AI Act                          │
│  ✅ FRIA (Art. 27)                                       │
│  ✅ Evaluaciones compliance                              │
│  ✅ Audit trail inmutable (Art. 19)                      │
│  ✅ Aprobaciones workflows                               │
│  ✅ Documentación técnica (Anexo IV)                     │
└────────────────────┬────────────────────────────────────┘
                     │ APIs / Webhooks / Connectors
                     │
        ┌────────────┼────────────┬──────────────┐
        │            │            │              │
┌───────▼─────┐ ┌───▼──────┐ ┌──▼────────┐ ┌──▼─────────┐
│ Databricks  │ │Snowflake │ │Azure ML   │ │ AWS        │
│ (Training)  │ │ (Data)   │ │(Deploy)   │ │ SageMaker  │
└─────────────┘ └──────────┘ └───────────┘ └────────────┘
      │               │             │              │
Cliente mantiene su infraestructura existente
```

**Principio:** **Governance as a Service** (no lock-in)

---

## 🔗 ARQUITECTURA INTEGRACIÓN

### **Modelo 1: API Connectors (Bidireccional)**

**CodeflowX ←→ Databricks/Snowflake/Azure ML**

```
FLUJO TÍPICO:

1. Data scientist entrena modelo en Databricks
   ↓
2. Databricks webhook notifica CodeflowX (modelo nuevo)
   ↓
3. CodeflowX:
   - Registra modelo en governance
   - Clasifica riesgo AI Act (automático)
   - Ejecuta evaluaciones compliance
   - Requiere aprobación si HIGH_RISK
   ↓
4. Si aprobado → CodeflowX callback Databricks (deploy autorizado)
5. Si rechazado → CodeflowX bloquea deployment
```

**Componentes:**

```python
# Connector Databricks
class DatabricksConnector:
    """Integración bidireccional con Databricks"""
    
    def __init__(self, databricks_url, token):
        self.client = DatabricksClient(databricks_url, token)
    
    # 1. Sync models FROM Databricks TO CodeflowX
    def sync_models_from_databricks(self):
        """Pull modelos registrados en Databricks MLflow Registry"""
        
        models = self.client.list_registered_models()
        
        for model in models:
            # Check si ya existe en CodeflowX
            if not self.exists_in_codeflowx(model.name):
                # Register en CodeflowX governance
                codeflowx_model = self.register_model_codeflowx(
                    name=model.name,
                    version=model.latest_version,
                    metadata={
                        "source": "DATABRICKS",
                        "databricks_id": model.id,
                        "databricks_url": model.url,
                        "created_at": model.creation_timestamp
                    }
                )
                
                # Trigger clasificación riesgo automática
                self.classify_risk(codeflowx_model)
    
    # 2. Send approval status FROM CodeflowX TO Databricks
    def notify_approval_status(self, model_id, approval_status):
        """Notificar Databricks sobre aprobación governance"""
        
        # Get Databricks model info
        databricks_id = self.get_databricks_id(model_id)
        
        # Update tags/metadata en Databricks
        self.client.set_model_tag(
            name=databricks_id,
            key="codeflowx_approval",
            value=approval_status  # APPROVED, REJECTED, PENDING
        )
        
        self.client.set_model_tag(
            name=databricks_id,
            key="codeflowx_risk_level",
            value=self.get_risk_level(model_id)
        )
        
        # Si HIGH_RISK + REJECTED → Transition a stage "Blocked"
        if approval_status == "REJECTED":
            self.client.transition_model_version_stage(
                name=databricks_id,
                version=self.get_version(model_id),
                stage="Archived",  # Bloquear deployment
                archive_existing_versions=False
            )
    
    # 3. Webhook receiver (Databricks → CodeflowX)
    async def handle_webhook(self, event_type, payload):
        """Handle webhooks de Databricks (modelo nuevo, deployment, etc.)"""
        
        if event_type == "MODEL_VERSION_CREATED":
            # Nuevo modelo → Registrar en CodeflowX
            await self.sync_model_from_event(payload)
            
        elif event_type == "MODEL_VERSION_TRANSITIONED":
            # Cambio stage → Verificar si requiere re-evaluación
            if payload["new_stage"] == "Production":
                # Bloquear deployment si no aprobado CodeflowX
                if not self.is_approved_codeflowx(payload["model_name"]):
                    raise Exception("Model not approved by CodeflowX governance")
```

---

### **Modelo 2: Data Connectors (Lectura metadatos)**

**CodeflowX lee metadatos SIN mover datos**

```
Cliente datos en Snowflake/S3/Azure Data Lake
    ↓ CodeflowX NO copia datos
    ↓ Solo lee METADATOS
CodeflowX governance:
    - Cataloga datasets
    - Evalúa calidad (sin mover datos)
    - Trackea lineage
    - Verifica GDPR compliance
```

**Ejemplo Snowflake:**

```python
# Connector Snowflake
class SnowflakeConnector:
    """Lee metadatos datasets Snowflake (sin copiar datos)"""
    
    def __init__(self, account, user, password, warehouse):
        self.conn = snowflake.connector.connect(
            account=account,
            user=user,
            password=password,
            warehouse=warehouse
        )
    
    def catalog_datasets(self, database, schema):
        """Catalogar datasets en Snowflake para governance"""
        
        # Query INFORMATION_SCHEMA (metadatos)
        cursor = self.conn.cursor()
        cursor.execute(f"""
            SELECT 
                table_name,
                row_count,
                bytes,
                created,
                last_altered
            FROM {database}.INFORMATION_SCHEMA.TABLES
            WHERE table_schema = '{schema}'
        """)
        
        datasets = cursor.fetchall()
        
        for dataset in datasets:
            # Register en CodeflowX (solo metadata)
            self.register_dataset_codeflowx(
                name=dataset[0],
                source="SNOWFLAKE",
                source_location=f"{database}.{schema}.{dataset[0]}",
                size_bytes=dataset[2],
                record_count=dataset[1],
                created_at=dataset[3]
            )
    
    def evaluate_data_quality(self, table_name, sample_size=1000):
        """Evaluar calidad datos SIN copiar (sample pequeño)"""
        
        # Solo sample para evaluación
        cursor = self.conn.cursor()
        cursor.execute(f"""
            SELECT * FROM {table_name}
            SAMPLE ({sample_size} ROWS)
        """)
        
        sample_data = cursor.fetchall()
        
        # Evaluar calidad en sample
        quality_metrics = self.calculate_quality_metrics(sample_data)
        
        # Guardar en CodeflowX
        self.save_quality_metrics_codeflowx(table_name, quality_metrics)
        
        return quality_metrics
```

**Ventaja:** Cliente datos permanecen en Snowflake (no movemos TB de datos)

---

### **Modelo 3: Spark Jobs Integration**

**CodeflowX orquesta jobs Spark para evaluaciones big data**

```python
# Connector Spark
class SparkConnector:
    """Submit jobs Spark para evaluaciones big data"""
    
    def __init__(self, spark_master_url):
        self.spark = SparkSession.builder \
            .master(spark_master_url) \
            .appName("CodeflowX Evaluations") \
            .getOrCreate()
    
    def evaluate_bias_spark(self, dataset_path, protected_attributes):
        """Evaluar sesgos en dataset masivo usando Spark"""
        
        # 1. Load dataset (puede ser TB en Spark)
        df = self.spark.read.parquet(dataset_path)
        
        # 2. Compute bias metrics distribuido
        from pyspark.sql import functions as F
        
        bias_results = {}
        
        for attr in protected_attributes:
            # Demographic parity
            positive_rate_by_group = df.groupBy(attr) \
                .agg(F.avg("prediction").alias("positive_rate")) \
                .collect()
            
            bias_results[attr] = {
                "groups": {row[attr]: row["positive_rate"] 
                          for row in positive_rate_by_group},
                "max_disparity": self.calculate_disparity(positive_rate_by_group)
            }
        
        # 3. Send results a CodeflowX (solo métricas, no datos)
        self.send_to_codeflowx(bias_results)
        
        return bias_results
```

---

## 🏗️ ARQUITECTURA INTEGRACIÓN ENTERPRISE

### **Escenario real: Banco con Databricks + Snowflake + Azure ML**

```
┌─────────────────────────────────────────────────────────────┐
│                  BANCO ACME (Cliente)                        │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Databricks (Training + Feature Engineering)                 │
│       ↓                                                      │
│  Snowflake (Data Warehouse - TB de datos históricos)        │
│       ↓                                                      │
│  Azure ML (Model Deployment + Serving)                       │
│       ↓                                                      │
│  Azure Data Lake (Raw data storage - PB de datos)           │
│                                                              │
└──────────────────────┬──────────────────────────────────────┘
                       │ APIs / Webhooks / Connectors
                       │
┌──────────────────────▼──────────────────────────────────────┐
│               CODEFLOWX (Governance Overlay)                 │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ✅ Registra modelos Databricks (metadata, no modelo)       │
│  ✅ Cataloga datasets Snowflake (metadata, no datos)        │
│  ✅ Clasifica riesgo AI Act automático                      │
│  ✅ Ejecuta evaluaciones compliance:                        │
│      - Bias detection (sample Snowflake o job Spark)        │
│      - Quality metrics (sample, no full scan)               │
│      - GDPR compliance check                                │
│  ✅ FRIA workflow (Art. 27)                                 │
│  ✅ Aprobación/Rechazo deployment                           │
│  ✅ Audit trail inmutable (Art. 19)                         │
│  ✅ Docs técnicos Anexo IV                                  │
│  ✅ Post-market monitoring (métricas Azure ML)              │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

**Datos permanecen en Databricks/Snowflake.**  
**Modelos permanecen en Azure ML.**  
**CodeflowX solo governance + compliance.**

---

## 🔌 CONECTORES NECESARIOS

### **Conector 1: Databricks + MLflow**

**Qué hace:**
- ✅ Sync modelos registrados en Databricks MLflow Registry → CodeflowX
- ✅ Lee metadata experimentos (hiperparámetros, métricas, datasets usados)
- ✅ Notifica aprobación/rechazo governance → Databricks
- ✅ Bloquea deployment si no aprobado (cambia stage MLflow)

**API Databricks usada:**
- `GET /api/2.0/mlflow/registered-models/list` (listar modelos)
- `GET /api/2.0/mlflow/model-versions/get` (metadata versión)
- `POST /api/2.0/mlflow/model-versions/set-tag` (marcar aprobación)
- `POST /api/2.0/mlflow/model-versions/transition-stage` (bloquear/aprobar deployment)

**Webhook Databricks → CodeflowX:**
- Event: `MODEL_VERSION_CREATED` → CodeflowX registra + evalúa
- Event: `MODEL_VERSION_TRANSITIONED` → CodeflowX verifica si aprobado

---

### **Conector 2: Snowflake (Data Catalog)**

**Qué hace:**
- ✅ Cataloga datasets Snowflake en governance CodeflowX
- ✅ Lee metadata (tamaño, filas, columnas, tipos)
- ✅ Evalúa calidad datos (sample pequeño, no full scan)
- ✅ Detecta PII (Presidio sobre sample)
- ✅ Trackea lineage (Snowflake → modelos)

**SQL Snowflake usada:**
```sql
-- Catalogar datasets
SELECT table_name, row_count, bytes, created
FROM INFORMATION_SCHEMA.TABLES
WHERE table_schema = 'ML_DATASETS';

-- Sample para evaluación (NO copia todos los datos)
SELECT * FROM ML_DATASETS.CREDIT_APPLICATIONS
SAMPLE (1000 ROWS);  -- Solo 1K filas para evaluar
```

**Ventaja:** No copias TB de datos. Solo catalogas + evalúas sample.

---

### **Conector 3: Azure ML / SageMaker (Deployment)**

**Qué hace:**
- ✅ Lee modelos deployados Azure ML/SageMaker
- ✅ Monitorea inferencias (Art. 72 post-market)
- ✅ Detecta drift (lee métricas Azure ML)
- ✅ Bloquea deployment si no aprobado

**APIs usadas:**
- Azure ML: `GET /subscriptions/{}/resourceGroups/{}/providers/Microsoft.MachineLearningServices/workspaces/{}/models`
- SageMaker: `list_models()`, `describe_model()`

---

### **Conector 4: S3 / Azure Data Lake / GCS (Data Storage)**

**Qué hace:**
- ✅ Cataloga datasets en data lakes
- ✅ Lee metadata (tamaño, formato, ubicación)
- ✅ NO copia datos (solo referencias)
- ✅ Trackea lineage (data lake → modelos)

**Ejemplo:**

```python
# Connector S3
class S3Connector:
    def catalog_s3_datasets(self, bucket_name, prefix):
        """Catalogar datasets S3 sin copiar"""
        
        s3_client = boto3.client('s3')
        
        # List objects
        objects = s3_client.list_objects_v2(
            Bucket=bucket_name,
            Prefix=prefix
        )
        
        for obj in objects.get('Contents', []):
            # Register en CodeflowX (solo metadata)
            self.register_dataset_codeflowx(
                name=obj['Key'],
                source="S3",
                source_location=f"s3://{bucket_name}/{obj['Key']}",
                size_bytes=obj['Size'],
                last_modified=obj['LastModified']
            )
            
            # NO descarga archivo (solo metadata)
```

---

### **Conector 5: Apache Spark (Evaluation Jobs)**

**Qué hace:**
- ✅ Ejecuta evaluaciones big data en Spark cluster cliente
- ✅ CodeflowX submit job → Spark ejecuta → Resultados a CodeflowX
- ✅ No mueve datos (job ejecuta donde están los datos)

**Ejemplo:**

```python
# Submit evaluation job a Spark
def submit_bias_evaluation_spark(dataset_path, spark_cluster_url):
    """Submit job Spark para evaluar sesgos en dataset masivo"""
    
    # 1. Preparar job Spark
    job_config = {
        "name": "codeflowx-bias-evaluation",
        "spark_python_task": {
            "python_file": "s3://codeflowx-jobs/bias_evaluation.py",
            "parameters": [
                "--dataset-path", dataset_path,
                "--output-path", "s3://codeflowx-results/bias_eval_123.json"
            ]
        },
        "new_cluster": {
            "spark_version": "3.4.x",
            "node_type_id": "i3.xlarge",
            "num_workers": 4
        }
    }
    
    # 2. Submit job
    job_id = spark_client.submit_run(job_config)
    
    # 3. Wait completion (async)
    result = await wait_job_completion(job_id)
    
    # 4. Read results (solo métricas, no datos)
    bias_metrics = s3_client.get_object(
        Bucket="codeflowx-results",
        Key="bias_eval_123.json"
    )
    
    return bias_metrics
```

**Ventaja:** Evaluación big data sin copiar datos a CodeflowX.

---

## 📊 TABLA INTEGRACIONES PLATAFORMAS

| Plataforma | Uso cliente | Integración CodeflowX | Datos movidos | Estado |
|------------|-------------|----------------------|---------------|--------|
| **Databricks** | Training + Feature Eng | API + Webhooks (MLflow Registry) | ❌ Metadata only | ⏳ PROMPTS_12 |
| **Snowflake** | Data Warehouse | SQL queries (INFORMATION_SCHEMA) | ❌ Sample only | ⏳ PROMPTS_12 |
| **Azure ML** | Deployment + Serving | Azure SDK (REST API) | ❌ Metadata only | ⏳ PROMPTS_12 |
| **SageMaker** | Deployment AWS | Boto3 SDK | ❌ Metadata only | ⏳ PROMPTS_12 |
| **S3 / Azure Blob** | Raw data storage | SDK (list objects) | ❌ Metadata only | ⏳ PROMPTS_12 |
| **GCS** | Google Cloud Storage | GCS SDK | ❌ Metadata only | ⏳ PROMPTS_12 |
| **Apache Spark** | Big data processing | Submit jobs (Livy API) | ❌ Jobs en cluster cliente | ⏳ PROMPTS_12 |
| **MLflow** | Experiment tracking | MLflow Tracking API | ❌ Metadata only | ⏳ PROMPTS_12 |
| **Kubeflow** | ML pipelines K8s | Kubeflow Pipelines API | ❌ Metadata only | ⏳ PROMPTS_12 |
| **Vertex AI** | Google Cloud ML | Vertex AI SDK | ❌ Metadata only | ⏳ PROMPTS_12 |

---

## ✅ VENTAJAS GOVERNANCE OVERLAY

**Para el cliente:**

1. **No vendor lock-in:**
   - ✅ Mantienen su stack existente (Databricks, Snowflake, etc.)
   - ✅ No migración costosa
   - ✅ No disruption infraestructura

2. **Datos permanecen en su control:**
   - ✅ Datos NO salen de su Snowflake/Databricks
   - ✅ Solo metadata/métricas van a CodeflowX
   - ✅ Soberanía datos garantizada

3. **Compliance sin fricción:**
   - ✅ Governance automático sobre lo que ya tienen
   - ✅ No cambian workflow ML Engineers
   - ✅ Aprobaciones transparentes

4. **Escalabilidad:**
   - ✅ Evaluaciones big data en SU Spark cluster (no CodeflowX infra)
   - ✅ CodeflowX solo orquesta + governance

---

## 💰 IMPLICACIONES COMERCIALES

### **Mensaje venta:**

**❌ INCORRECTO:**
> "CodeflowX reemplaza su Databricks/Snowflake"

**✅ CORRECTO:**
> "CodeflowX se integra con su Databricks/Snowflake existente, añadiendo capa de governance AI Act sin migración."

---

### **Argumento ROI cliente:**

**Sin CodeflowX:**
- Cliente tiene Databricks ($100K/año) + Snowflake ($200K/año)
- Necesita compliance AI Act
- Opciones:
  - Construir governance interno ($500K+ dev)
  - Migrar a plataforma todo-en-uno (imposible, disruptivo)

**Con CodeflowX:**
- ✅ Mantienen Databricks + Snowflake ($300K/año)
- ✅ Añaden CodeflowX governance ($50-100K/año)
- ✅ Compliance AI Act sin migración
- ✅ ROI: $500K ahorro vs desarrollo interno

---

## 🚨 GAPS ACTUALES (NECESITA PROMPTS_12)

**Actualmente CodeflowX tiene:**
- ✅ PostgreSQL/TimescaleDB (propio)
- ✅ Qdrant (propio)
- ✅ MinIO (propio)
- ✅ OpenSearch (propio)

**NO tiene (pendiente):**
- ❌ Conector Databricks
- ❌ Conector Snowflake
- ❌ Conector Azure ML
- ❌ Conector SageMaker
- ❌ Conector S3/Azure Blob (catalogación)
- ❌ Conector Spark (submit jobs)
- ❌ Conector MLflow externo
- ❌ Conector Kubeflow
- ❌ Conector Vertex AI

**Esto requiere NUEVO documento:**

**`PROMPTS_12_CONECTORES_PLATAFORMAS_ENTERPRISE.md`**

---

## 📋 NECESIDAD PROMPTS_12

### **Prompts necesarios (estimado 10-12):**

1. **Conector Databricks + MLflow**
   - API client Databricks
   - Sync models bidireccional
   - Webhooks handling
   - Tag management (approval status)

2. **Conector Snowflake**
   - Catalogación datasets
   - Quality evaluation (samples)
   - Lineage tracking

3. **Conector Azure ML**
   - Sync deployments
   - Monitoring metrics
   - Drift detection integration

4. **Conector SageMaker**
   - Similar Azure ML

5. **Conector S3/Azure Blob/GCS**
   - Catalogación data lakes
   - Metadata extraction
   - No data movement

6. **Conector Spark**
   - Submit evaluation jobs
   - Big data evaluations
   - Results collection

7. **Entity ExternalPlatformIntegration**
   - Tracking conexiones externas
   - Credentials management
   - Sync status

8. **Service ExternalPlatformSyncService**
   - Orquestación sync
   - Conflict resolution
   - Error handling

9. **BPMN external-model-approval-workflow**
   - Workflow aprobación modelos externos
   - Notificación a plataforma externa

10. **UI External Platforms Management**
    - Configurar conexiones
    - Monitoring sync status
    - Credentials management

---

## 🎯 CASOS USO REALES

### **Caso 1: Banco usa Databricks + Snowflake**

**Setup:**
1. Banco entrena modelos en Databricks
2. Datos entrenamiento en Snowflake (200 TB)
3. Deployment en Azure ML

**Integración CodeflowX:**
```
1. Configurar conector Databricks en CodeflowX UI
   - Databricks URL
   - API token
   - Workspace ID

2. CodeflowX sync automático:
   - Cada 1 hora: Sync modelos nuevos Databricks → CodeflowX
   - Webhook: Modelo nuevo → Trigger clasificación riesgo
   
3. Data scientist registra modelo en Databricks MLflow:
   - Modelo "credit_scoring_v3"
   - Dataset: Snowflake.ML_DATA.CREDIT_APPLICATIONS (2M filas)
   
4. Webhook Databricks → CodeflowX:
   - CodeflowX recibe notificación
   - Registra modelo (metadata only)
   - Clasifica riesgo → HIGH_RISK (scoring crediticio)
   - Lee sample dataset Snowflake (1K filas) para calidad
   - Detecta campo "race" → PII sensible
   - Ejecuta bias evaluation (Spark job en cluster banco)
   - Detecta demographic disparity 15%
   
5. CodeflowX FRIA workflow:
   - Art. 27 assessment automático
   - Requiere aprobación Compliance Officer
   - Aprobación tarda 3 días
   
6. Si APROBADO:
   - CodeflowX → Databricks: SET TAG approval=APPROVED
   - CodeflowX → Databricks: TRANSITION stage=Production
   - Azure ML puede deployar
   
7. Si RECHAZADO:
   - CodeflowX → Databricks: SET TAG approval=REJECTED
   - Codeflowx → Databricks: TRANSITION stage=Archived
   - Azure ML NO puede deployar (bloqueado)
```

**Cliente workflow NO cambia:**
- ✅ Data scientist sigue usando Databricks normal
- ✅ Governance transparente (solo añade aprobación)
- ✅ Datos NO salen de Snowflake

---

### **Caso 2: Startup usa Azure ML + Azure Data Lake**

**Setup:**
1. Training en Azure ML
2. Datos en Azure Data Lake (500 GB)
3. Feature store en Azure ML

**Integración CodeflowX:**
```
1. Conector Azure ML:
   - Subscription ID
   - Resource Group
   - Workspace

2. CodeflowX sync:
   - Sync modelos Azure ML Registry
   - Catalog datasets Azure Data Lake
   
3. ML Engineer registra modelo Azure ML:
   - CodeflowX detecta vía webhook
   - Clasifica riesgo
   - Evalúa compliance
   - Workflow aprobación
   - Notifica Azure ML (tag aprobación)
```

---

## 📦 PROPUESTA: CREAR PROMPTS_12

**Documento nuevo:** `PROMPTS_12_CONECTORES_PLATAFORMAS_ENTERPRISE.md`

**Objetivo:** Integrar CodeflowX con plataformas ML enterprise que clientes ya usan

**Prompts necesarios (12):**

1. Conector Databricks + MLflow (Python + Java)
2. Conector Snowflake (Java JDBC)
3. Conector Azure ML (Python SDK)
4. Conector SageMaker (Boto3)
5. Conector S3/Azure Blob/GCS (catalogación)
6. Conector Spark (Livy API submit jobs)
7. Entity ExternalPlatformIntegration
8. Service ExternalPlatformSyncService
9. BPMN external-model-approval-workflow
10. UI External Platforms Management (ZUL)
11. Webhook receiver generic (FastAPI)
12. Testing e2e integración Databricks

**Estimación:** 15-20 días (con 3-4 chats paralelos = 6-8 días reales)

**Prioridad:** 🔴 **CRÍTICA COMERCIAL** (clientes enterprise usan estas plataformas)

---

## 💡 ESTRATEGIA COMERCIAL ACTUALIZADA

### **Mensaje clave:**

**"CodeflowX es governance overlay, NO reemplazo infraestructura."**

**Pitch:**
> "¿Ya usan Databricks, Snowflake, Azure ML? Perfecto. CodeflowX se integra con ellos, añadiendo compliance AI Act automático sin migración. Sus datos permanecen donde están. Solo añadimos governance."

**Beneficios cliente:**
- ✅ Mantienen inversión existente (Databricks $100K/año)
- ✅ No migración (0 disrupción)
- ✅ Compliance AI Act automático
- ✅ Governance transparente para ML engineers

**Competidores que requieren migración:** ❌ Cliente rechaza (muy disruptivo)

---

## 🎯 RECOMENDACIÓN

**Crear URGENTE:**

**`PROMPTS_12_CONECTORES_PLATAFORMAS_ENTERPRISE.md`**

**Razón:**
- **Crítico comercial:** 80% clientes enterprise usan Databricks/Snowflake/Azure ML
- **Diferenciador competitivo:** Otros requieren migración, tú no
- **Sin esto:** Solo puedes vender a clientes greenfield (pequeños)
- **Con esto:** Puedes vender a Fortune 500 con infraestructura existente

**Prioridad:** 🔴 **CRÍTICA** (antes de PROMPTS_06 incluso)

**Timeline:** Q4 2025 (1-2 meses) para tener conectores básicos (Databricks + Snowflake)

---

## ✅ CONCLUSIÓN

**Pregunta:** ¿Qué pasa con empresas que usan Databricks, Spark, Snowflake?

**Respuesta:** **CodeflowX se integra con ellos (governance overlay).**

**Arquitectura:**
- Cliente mantiene: Databricks (training), Snowflake (data), Azure ML (deploy)
- CodeflowX añade: Governance, clasificación riesgo, compliance, aprobaciones
- Datos NO se mueven (solo metadata/métricas)

**Necesitas:**
- ✅ PROMPTS_12 (10-12 conectores plataformas enterprise)
- ✅ Prioridad CRÍTICA comercial (80% clientes enterprise usan estas plataformas)

**Mensaje comercial:**
> "No reemplazamos su Databricks. Nos integramos con él para añadir compliance AI Act automático."

**¿Creo PROMPTS_12 ahora?** 🚀

---

**Última actualización:** 5 Noviembre 2025  
**Decisión:** PROMPTS_12 necesario URGENTE para clientes enterprise  
**Impacto comercial:** 🔴 CRÍTICO (sin esto, solo clientes pequeños greenfield)
