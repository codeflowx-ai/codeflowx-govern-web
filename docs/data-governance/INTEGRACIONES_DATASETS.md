# Integraciones Relacionadas con Datasets

**Fecha:** 2025-01-14
**Propósito:** Identificar clases Java de integración relacionadas con datasets

---

## 📋 Clases Relacionadas con Datasets

### **1. DataLakeCatalogService** ⭐⭐⭐
**Ubicación:** `com.codeflowx.govern.business.integrations.DataLakeCatalogService`

**Propósito:** Catalogación de datasets en data lakes mediante metadata (sin mover datos)

**Funcionalidades:**
- ✅ Catalogación de datasets en **S3** (AWS)
- ✅ Catalogación de datasets en **Azure Blob Storage**
- ✅ Catalogación de datasets en **GCS** (Google Cloud Storage)
- ✅ Crea/actualiza entidades `ExternalDataset` con metadata
- ✅ Extrae información: nombre, tamaño, fecha modificación, ubicación

**Métodos principales:**
- `catalogS3(Long platformId)` - Cataloga datasets de S3
- `catalogAzureBlob(Long platformId)` - Cataloga datasets de Azure Blob
- `catalogGcs(Long platformId)` - Cataloga datasets de GCS
- `upsertDataLakeDataset(...)` - Crea/actualiza dataset en catálogo

**Relación con Data Governance:**
- Puede integrarse con `DTGDATAORIGINS` para orígenes externos tipo "EXTERNAL"
- Los datasets catalogados pueden sincronizarse con `DTGDATASETS`
- Útil para descubrimiento automático de datasets en data lakes

---

### **2. SnowflakeConnectorService** ⭐⭐⭐
**Ubicación:** `com.codeflowx.govern.business.integrations.SnowflakeConnectorService`

**Propósito:** Catalogación y evaluación de calidad de datasets en Snowflake (sin copiar datos completos)

**Funcionalidades:**
- ✅ Catalogación de datasets desde `INFORMATION_SCHEMA.TABLES`
- ✅ Evaluación de calidad usando `SAMPLE` (muestras controladas)
- ✅ Detección de PII (emails, teléfonos) en muestras
- ✅ Análisis de completitud, tipos de datos, estadísticas
- ✅ Crea/actualiza entidades `ExternalDataset`

**Métodos principales:**
- `catalogDatasets(Long platformId)` - Cataloga todas las tablas de Snowflake
- `evaluateDatasetQuality(Long datasetId, int sampleSize)` - Evalúa calidad con muestra
- `upsertDataset(...)` - Crea/actualiza dataset en catálogo

**Relación con Data Governance:**
- **Directamente relacionado** con `DTGDATASETS` y `DTGDATAQUALITYMETRICS`
- Puede alimentar automáticamente métricas de calidad ISO 8000
- Puede detectar PII y alimentar `DTGDATASETPRIVACY`
- Útil para orígenes internos tipo "INTERNAL" (bases de datos)

---

### **3. PurviewConnectorService** ⭐⭐
**Ubicación:** `com.codeflowx.govern.business.integrations.PurviewConnectorService`

**Propósito:** Sincronización de activos del catálogo Microsoft Purview como datasets externos

**Funcionalidades:**
- ✅ Sincroniza activos del catálogo Purview
- ✅ Extrae metadata: nombre, tipo, descripción, clasificación, linaje
- ✅ Crea/actualiza entidades `ExternalDataset`
- ✅ Consume API de búsqueda de Purview

**Métodos principales:**
- `syncCatalog(Long platformId)` - Sincroniza catálogo completo
- `testConnection(ExternalPlatformIntegration platform)` - Prueba conexión
- `upsertDataset(...)` - Crea/actualiza dataset desde activo Purview

**Relación con Data Governance:**
- Puede alimentar `DTGDATASETS` con datasets descubiertos
- Puede proporcionar información de linaje para `DTGDATALINEAGE`
- Útil para integración con catálogos de datos existentes

---

### **4. ServiceNowConnectorService** ⭐
**Ubicación:** `com.codeflowx.govern.business.integrations.ServiceNowConnectorService`

**Propósito:** Sincronización de tickets (change requests, incidentes) como datasets gobernados

**Funcionalidades:**
- ✅ Sincroniza tickets de ServiceNow como datasets
- ✅ Soporta diferentes tablas (change_request, incident, etc.)
- ✅ Crea/actualiza entidades `ExternalDataset`

**Métodos principales:**
- `syncTickets(Long platformId)` - Sincroniza tickets según configuración
- `upsertTicket(...)` - Crea/actualiza dataset desde ticket

**Relación con Data Governance:**
- Caso de uso específico: tickets como datasets estructurados
- Puede alimentar `DTGDATASETS` con información de tickets
- Menos relevante para datasets tradicionales de ML/analytics

---

### **5. SparkEvaluationService** ⭐⭐
**Ubicación:** `com.codeflowx.govern.business.integrations.SparkEvaluationService`

**Propósito:** Evaluación de sesgos sobre datasets remotos usando PySpark (S3/HDFS)

**Funcionalidades:**
- ✅ Envía scripts PySpark a Livy para evaluación
- ✅ Evalúa sesgos sobre datasets remotos (sin copiar datos)
- ✅ Soporta datasets en S3, HDFS
- ✅ Analiza atributos protegidos (género, raza, etc.)

**Métodos principales:**
- `submitBiasEvaluation(Long platformId, String datasetPath, List<String> protectedAttributes)` - Evalúa sesgos
- `buildBiasEvaluationScript(...)` - Construye script PySpark
- `fetchResults(...)` - Obtiene resultados de evaluación

**Relación con Data Governance:**
- **Directamente relacionado** con análisis de sesgos
- Puede alimentar `DTGBIASSCORE` y `DTGBIASANALYSIS` en `DTGDATASETS`
- Útil para análisis de sesgos en datasets grandes sin mover datos

---

## 🔗 Integración con Data Governance

### **Mapeo de Integraciones → Tablas Data Governance**

| Integración | Tabla Data Governance | Tipo de Origen | Uso Principal |
|------------|----------------------|----------------|---------------|
| **DataLakeCatalogService** | `DTGDATAORIGINS` + `DTGDATASETS` | EXTERNAL | Descubrimiento de datasets en data lakes |
| **SnowflakeConnectorService** | `DTGDATAORIGINS` + `DTGDATASETS` + `DTGDATAQUALITYMETRICS` + `DTGDATASETPRIVACY` | INTERNAL | Catalogación y evaluación de calidad |
| **PurviewConnectorService** | `DTGDATASETS` + `DTGDATALINEAGE` | EXTERNAL | Sincronización con catálogo existente |
| **ServiceNowConnectorService** | `DTGDATASETS` | EXTERNAL | Tickets como datasets estructurados |
| **SparkEvaluationService** | `DTGDATASETS` (sesgos) | - | Evaluación de sesgos en datasets remotos |

---

## 🎯 Recomendaciones de Integración

### **Para Orígenes Externos (Data Lakes)**

1. **DataLakeCatalogService** → `DTGDATAORIGINS`
   - Crear origen tipo "EXTERNAL" con tipo "S3", "AZURE_BLOB", "GCS"
   - Sincronizar datasets descubiertos → `DTGDATASETS`
   - Metadata almacenada en `DTGDATASETS.DTGMETADATA`

### **Para Orígenes Internos (Bases de Datos)**

2. **SnowflakeConnectorService** → `DTGDATAORIGINS`
   - Crear origen tipo "INTERNAL" con tipo "SNOWFLAKE"
   - Catalogar tablas → `DTGDATASETS`
   - Evaluar calidad → `DTGDATAQUALITYMETRICS`
   - Detectar PII → `DTGDATASETPRIVACY`

### **Para Análisis de Sesgos**

3. **SparkEvaluationService** → `DTGDATASETS`
   - Usar para análisis de sesgos en datasets grandes
   - Actualizar `DTGBIASSCORE` y `DTGBIASANALYSIS`
   - Puede generar riesgos en `DTGDATASETRISKS` si se detectan sesgos

### **Para Catálogos Existentes**

4. **PurviewConnectorService** → `DTGDATASETS` + `DTGDATALINEAGE`
   - Sincronizar activos de Purview
   - Extraer linaje si está disponible
   - Actualizar `DTGDATALINEAGE` con información de dependencias

---

## 📝 Próximos Pasos

1. **Crear Servicio de Integración Unificado**
   - Servicio que orqueste las integraciones existentes
   - Mapeo automático a tablas de Data Governance
   - Sincronización programada

2. **Endpoints REST para Integraciones**
   - `POST /api/v1/governance/data/origins/{id}/sync` - Sincronizar origen
   - `POST /api/v1/governance/data/datasets/{id}/evaluate-quality` - Evaluar calidad
   - `POST /api/v1/governance/data/datasets/{id}/evaluate-bias` - Evaluar sesgos

3. **Workflows BPMN**
   - Workflow de sincronización automática
   - Workflow de evaluación de calidad
   - Workflow de evaluación de sesgos

---

## ✅ Clases NO Relacionadas con Datasets

Las siguientes clases están enfocadas en **modelos** o **otros tipos de integraciones**:

- ❌ `VertexAIConnectorService` - Modelos (Vertex AI)
- ❌ `SageMakerConnectorService` - Modelos (SageMaker)
- ❌ `AzureMLConnectorService` - Modelos (Azure ML)
- ❌ `IbmWatsonxConnectorService` - Modelos (IBM Watson)
- ❌ `FabricConnectorService` - Modelos (Microsoft Fabric)
- ❌ `DatabricksConnectorService` - Modelos (MLflow Registry)
- ❌ `JiraConnectorService` - Tickets/Issues (no datasets)

---

**Última actualización:** 2025-01-14
