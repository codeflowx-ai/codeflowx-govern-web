# Integraciones Solicitadas: Data Lakes, Databricks/MLflow y MLOps

**Fecha:** 2025-01-14
**Solicitud:** Integraciones con data lakes, Databricks/MLflow y análisis de modelos MLOps

---

## 📋 Resumen de Integraciones Solicitadas

### **1. Data Lakes** ✅
### **2. Databricks y MLflow** ⚠️ (Parcial)
### **3. Análisis de Modelos MLOps** ✅

---

## 🏗️ 1. INTEGRACIÓN CON DATA LAKES

### **✅ Clase Existente: DataLakeCatalogService**

**Ubicación:** `com.codeflowx.govern.business.integrations.DataLakeCatalogService`

**Estado:** ✅ **IMPLEMENTADA**

**Funcionalidades:**
- ✅ Catalogación de datasets en **S3** (AWS)
- ✅ Catalogación de datasets en **Azure Blob Storage**
- ✅ Catalogación de datasets en **GCS** (Google Cloud Storage)
- ✅ Descubrimiento automático mediante metadata (sin mover datos)
- ✅ Crea/actualiza entidades `ExternalDataset`

**Métodos disponibles:**
```java
- catalogS3(Long platformId) → List<ExternalDataset>
- catalogAzureBlob(Long platformId) → List<ExternalDataset>
- catalogGcs(Long platformId) → List<ExternalDataset>
```

**Integración con Data Governance:**
- Puede conectarse con `DTGDATAORIGINS` (tipo: EXTERNAL)
- Los datasets catalogados pueden sincronizarse con `DTGDATASETS`
- Metadata almacenada en `DTGDATASETS.DTGMETADATA`

**✅ Estado:** Lista para usar

---

## 🔷 2. INTEGRACIÓN CON DATABRICKS Y MLFLOW

### **⚠️ Clase Existente: DatabricksConnectorService**

**Ubicación:** `com.codeflowx.govern.business.integrations.DatabricksConnectorService`

**Estado:** ⚠️ **PARCIAL - Solo Modelos**

**Funcionalidades Actuales:**
- ✅ Sincronización de **modelos** desde MLflow Registry
- ✅ Notificación de aprobación a Databricks
- ✅ Gestión de tags y stages en MLflow
- ❌ **NO incluye catalogación de datasets**

**Métodos disponibles:**
```java
- syncModelsFromDatabricks(Long platformId) → void
- notifyApprovalToDatabricks(Long externalModelId, String approvalStatus) → void
- testConnection(ExternalPlatformIntegration platform) → boolean
```

**Limitación:**
- Solo sincroniza **modelos** (`ExternalModel`)
- **NO sincroniza datasets** de Databricks/MLflow

---

### **🔨 NECESARIO: Extender para Datasets**

**Requisitos para Datasets:**

1. **Catalogación de Datasets en Databricks:**
   - Listar tablas de Unity Catalog
   - Listar volúmenes (Volumes)
   - Listar archivos en DBFS (Databricks File System)

2. **Integración con MLflow Tracking:**
   - Obtener datasets asociados a experimentos
   - Extraer información de datasets de entrenamiento
   - Sincronizar con `DTGDATASETS`

3. **Propuesta de Implementación:**

```java
/**
 * Extensión de DatabricksConnectorService para datasets
 */
public class DatabricksConnectorService {

    // NUEVO: Catalogar datasets de Unity Catalog
    public List<ExternalDataset> catalogUnityCatalogDatasets(Long platformId) {
        // Listar tablas de Unity Catalog
        // Listar volúmenes
        // Crear/actualizar ExternalDataset
    }

    // NUEVO: Catalogar datasets de DBFS
    public List<ExternalDataset> catalogDBFSDatasets(Long platformId, String path) {
        // Listar archivos en DBFS
        // Detectar formatos (Parquet, CSV, JSON)
        // Crear/actualizar ExternalDataset
    }

    // NUEVO: Obtener datasets de experimentos MLflow
    public List<ExternalDataset> getMLflowExperimentDatasets(Long platformId, String experimentId) {
        // Obtener datasets de entrenamiento del experimento
        // Extraer información de artifacts
        // Sincronizar con DTGDATASETS
    }
}
```

**Endpoints REST Necesarios:**
- `POST /api/v1/governance/data/origins/{id}/sync-databricks` - Sincronizar datasets de Databricks
- `GET /api/v1/governance/data/datasets/databricks/{path}` - Obtener dataset de Databricks
- `POST /api/v1/governance/data/datasets/{id}/sync-mlflow` - Sincronizar con MLflow experiment

---

## 🤖 3. ANÁLISIS DE MODELOS MLOPS

### **✅ Clases Existentes para Modelos MLOps**

#### **3.1. DatabricksConnectorService** ✅
**Para:** Databricks + MLflow Registry
- Sincroniza modelos desde MLflow
- Notifica aprobaciones
- Gestiona stages y tags

#### **3.2. VertexAIConnectorService** ✅
**Ubicación:** `com.codeflowx.govern.business.integrations.VertexAIConnectorService`

**Para:** Google Vertex AI
- Sincroniza modelos de Vertex AI Model Registry
- Extrae metadata de modelos
- Crea/actualiza `ExternalModel`

**Métodos:**
```java
- syncModels(Long platformId) → List<ExternalModel>
```

#### **3.3. SageMakerConnectorService** ✅
**Ubicación:** `com.codeflowx.govern.business.integrations.SageMakerConnectorService`

**Para:** AWS SageMaker
- Sincroniza modelos de SageMaker Model Registry
- Obtiene métricas de CloudWatch
- Analiza latencia y throughput de endpoints

**Métodos:**
```java
- syncModels(Long platformId) → List<ExternalModel>
- getEndpointMetrics(String endpointName, ...) → Metrics
```

#### **3.4. AzureMLConnectorService** ✅
**Ubicación:** `com.codeflowx.govern.business.integrations.AzureMLConnectorService`

**Para:** Azure Machine Learning
- Sincroniza modelos de Azure ML Registry
- Obtiene métricas de modelos
- Gestiona deployments

**Métodos:**
```java
- syncModels(Long platformId) → List<ExternalModel>
```

#### **3.5. IbmWatsonxConnectorService** ✅
**Ubicación:** `com.codeflowx.govern.business.integrations.IbmWatsonxConnectorService`

**Para:** IBM watsonx.ai
- Sincroniza modelos de watsonx Model Registry
- Extrae metadata de modelos

**Métodos:**
```java
- syncModels(Long platformId) → List<ExternalModel>
```

#### **3.6. FabricConnectorService** ✅
**Ubicación:** `com.codeflowx.govern.business.integrations.FabricConnectorService`

**Para:** Microsoft Fabric
- Sincroniza modelos de Fabric ML
- Integración con Power BI y Azure

**Métodos:**
```java
- syncModels(Long platformId) → List<ExternalModel>
```

---

## 📊 Resumen de Estado

| Integración | Estado | Clase | Funcionalidad |
|------------|--------|-------|---------------|
| **Data Lakes (S3/Azure/GCS)** | ✅ Implementada | `DataLakeCatalogService` | Catalogación de datasets |
| **Databricks/MLflow (Modelos)** | ✅ Implementada | `DatabricksConnectorService` | Sincronización de modelos |
| **Databricks/MLflow (Datasets)** | ❌ **FALTA** | - | Catalogación de datasets |
| **Vertex AI (Modelos)** | ✅ Implementada | `VertexAIConnectorService` | Sincronización de modelos |
| **SageMaker (Modelos)** | ✅ Implementada | `SageMakerConnectorService` | Sincronización + métricas |
| **Azure ML (Modelos)** | ✅ Implementada | `AzureMLConnectorService` | Sincronización de modelos |
| **IBM watsonx (Modelos)** | ✅ Implementada | `IbmWatsonxConnectorService` | Sincronización de modelos |
| **Microsoft Fabric (Modelos)** | ✅ Implementada | `FabricConnectorService` | Sincronización de modelos |

---

## 🎯 Plan de Implementación

### **FASE 1: Extender Databricks para Datasets** (Prioridad Alta)

**Tareas:**
1. ✅ Extender `DatabricksConnectorService` con métodos para datasets
2. ✅ Implementar catalogación de Unity Catalog
3. ✅ Implementar catalogación de DBFS
4. ✅ Integrar con MLflow Tracking para datasets de experimentos
5. ✅ Crear endpoints REST para sincronización
6. ✅ Integrar con `DTGDATAORIGINS` y `DTGDATASETS`

**Tiempo estimado:** 8-12 horas

### **FASE 2: Integración con Data Governance** (Prioridad Media)

**Tareas:**
1. ✅ Crear servicio unificado de integración
2. ✅ Mapeo automático `ExternalDataset` → `DTGDATASETS`
3. ✅ Sincronización programada
4. ✅ Workflows BPMN para sincronización automática

**Tiempo estimado:** 4-6 horas

### **FASE 3: Análisis MLOps Avanzado** (Prioridad Baja)

**Tareas:**
1. ✅ Extender conectores de modelos con análisis de métricas
2. ✅ Integración con telemetría
3. ✅ Dashboard de modelos MLOps
4. ✅ Alertas de degradación

**Tiempo estimado:** 6-8 horas

---

## 🔌 Endpoints REST Propuestos

### **Para Databricks Datasets:**

```java
// Catalogar datasets de Unity Catalog
POST /api/v1/governance/data/origins/{id}/sync-databricks-unity
Body: { "catalog": "main", "schema": "default" }

// Catalogar datasets de DBFS
POST /api/v1/governance/data/origins/{id}/sync-databricks-dbfs
Body: { "path": "/datasets", "recursive": true }

// Obtener datasets de experimento MLflow
GET /api/v1/governance/data/datasets/mlflow/{experimentId}

// Sincronizar dataset específico
POST /api/v1/governance/data/datasets/{id}/sync-databricks
```

### **Para Data Lakes (Ya disponibles):**

```java
// Sincronizar S3
POST /api/v1/governance/data/origins/{id}/sync-s3

// Sincronizar Azure Blob
POST /api/v1/governance/data/origins/{id}/sync-azure-blob

// Sincronizar GCS
POST /api/v1/governance/data/origins/{id}/sync-gcs
```

### **Para Modelos MLOps (Ya disponibles):**

```java
// Sincronizar modelos Databricks
POST /api/v1/governance/models/origins/{id}/sync-databricks

// Sincronizar modelos Vertex AI
POST /api/v1/governance/models/origins/{id}/sync-vertex-ai

// Sincronizar modelos SageMaker
POST /api/v1/governance/models/origins/{id}/sync-sagemaker

// Sincronizar modelos Azure ML
POST /api/v1/governance/models/origins/{id}/sync-azure-ml
```

---

## 📝 Recomendaciones

### **1. Priorizar Extensión de Databricks**

La extensión de `DatabricksConnectorService` para datasets es **crítica** porque:
- Databricks es ampliamente usado para ML
- Unity Catalog es el estándar de gobernanza de datos en Databricks
- MLflow Tracking contiene información valiosa de datasets de entrenamiento

### **2. Reutilizar Patrón Existente**

Seguir el mismo patrón de `DataLakeCatalogService`:
- Catalogación mediante metadata (sin mover datos)
- Crear/actualizar `ExternalDataset`
- Integrar con `DTGDATAORIGINS` y `DTGDATASETS`

### **3. Integración con MLflow Tracking**

MLflow Tracking almacena información de:
- Datasets de entrenamiento (artifacts)
- Parámetros de experimentos
- Métricas de modelos

Esta información puede alimentar:
- `DTGDATASETS` - Datasets de entrenamiento
- `DTGDATALINEAGE` - Relación modelo-dataset
- `DTGDATAQUALITYMETRICS` - Métricas de calidad

---

## ✅ Checklist de Implementación

### **Data Lakes:**
- [x] DataLakeCatalogService implementado
- [x] Soporte S3, Azure Blob, GCS
- [ ] Integración con DTGDATAORIGINS (pendiente)
- [ ] Endpoints REST (pendiente)

### **Databricks/MLflow:**
- [x] DatabricksConnectorService (modelos) implementado
- [ ] Extensión para datasets Unity Catalog (pendiente)
- [ ] Extensión para datasets DBFS (pendiente)
- [ ] Integración con MLflow Tracking (pendiente)
- [ ] Endpoints REST (pendiente)

### **Modelos MLOps:**
- [x] DatabricksConnectorService implementado
- [x] VertexAIConnectorService implementado
- [x] SageMakerConnectorService implementado
- [x] AzureMLConnectorService implementado
- [x] IbmWatsonxConnectorService implementado
- [x] FabricConnectorService implementado
- [ ] Dashboard consolidado (pendiente)
- [ ] Alertas de degradación (pendiente)

---

**Última actualización:** 2025-01-14
