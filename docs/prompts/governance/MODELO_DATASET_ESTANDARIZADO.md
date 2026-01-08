# Modelo de Dataset Estandarizado para Análisis

**Fecha:** Diciembre 2025
**Estado:** ✅ ESTÁNDAR OFICIAL
**Formato Estándar:** Apache Parquet
**Objetivo:** Definir un formato estándar para datasets descargados (HuggingFace, Kaggle, etc.) que permita análisis uniforme por microservicios Python

## 🎯 DECISIÓN ARQUITECTÓNICA

**Apache Parquet es el formato estándar oficial** para todos los datasets en el sistema CodeflowX.

Esta decisión garantiza:
- ✅ Análisis uniforme por microservicios Python
- ✅ Eficiencia en almacenamiento y procesamiento
- ✅ Schema validado y embebido
- ✅ Compatibilidad con ecosistema de datos (pandas, PyArrow, Spark)
- ✅ Compresión integrada (Snappy por defecto)

---

## 🎯 PROBLEMA IDENTIFICADO

Al descargar datasets de fuentes externas (HuggingFace, Kaggle, etc.), cada dataset puede tener:
- **Formatos diferentes:** CSV, JSON, Parquet, Arrow, imágenes, texto plano
- **Estructuras diferentes:** Columnas variables, esquemas no documentados
- **Metadatos inconsistentes:** Información faltante o en formatos distintos

**Para que los microservicios Python puedan analizar** (calidad, seguridad, cumplimiento, ODS), necesitamos:
1. **Formato estándar de almacenamiento** → ✅ **Apache Parquet (ESTÁNDAR OFICIAL)**
2. **Esquema de metadatos uniforme**
3. **Proceso de normalización post-descarga**
4. **API unificada para acceso desde microservicios**

**Ver estándar completo:** `ESTANDAR_PARQUET.md`

---

## 📊 ARQUITECTURA PROPUESTA

### Flujo de Procesamiento

```
┌─────────────────┐
│ HuggingFace API │
│ Kaggle API      │
│ Otros Orígenes  │
└────────┬────────┘
         │ Descarga
         ▼
┌─────────────────────────┐
│ Dataset Raw (Original) │
│ - Formato original     │
│ - Sin normalizar       │
└────────┬───────────────┘
         │ Normalización
         ▼
┌─────────────────────────┐
│ Dataset Estandarizado   │
│ - Formato Parquet       │
│ - Schema validado       │
│ - Metadatos completos   │
└────────┬───────────────┘
         │
         ▼
┌─────────────────────────┐
│ Almacenamiento          │
│ - MinIO/S3 (datos)      │
│ - PostgreSQL (metadata)│
└────────┬───────────────┘
         │
         ▼
┌─────────────────────────┐
│ Microservicios Python   │
│ - Quality Analysis      │
│ - Security Analysis     │
│ - Compliance Analysis   │
│ - ODS Analysis          │
└─────────────────────────┘
```

---

## 🗄️ ESTRUCTURA DE ALMACENAMIENTO

### 1. **Formato Estándar: Apache Parquet** ⭐ ESTÁNDAR OFICIAL

**Razones:**
- ✅ Eficiente para análisis (columnar)
- ✅ Soporta tipos complejos (arrays, nested)
- ✅ Compresión integrada (Snappy por defecto)
- ✅ Compatible con pandas, PyArrow, Spark
- ✅ Schema embebido

**Configuración estándar:**
- Versión: Parquet 2.0+
- Compresión: Snappy
- Dictionary encoding: Habilitado
- Estadísticas: Por columna

**Ver especificación completa:** `ESTANDAR_PARQUET.md`

### 2. **Estructura de Archivos**

```
minio/datasets/
├── {dataset_id}/
│   ├── raw/                          # Datos originales (backup)
│   │   ├── original.{ext}            # Formato original
│   │   └── metadata.json             # Metadatos originales
│   ├── standardized/                 # Datos normalizados
│   │   ├── data.parquet              # Dataset en Parquet
│   │   ├── schema.json               # Schema validado
│   │   ├── sample.json                # Muestra (primeras 100 filas)
│   │   └── statistics.json           # Estadísticas básicas
│   └── analysis/                     # Resultados de análisis
│       ├── quality.json              # Análisis de calidad
│       ├── security.json             # Análisis de seguridad
│       ├── compliance.json           # Análisis de cumplimiento
│       └── ods.json                  # Análisis ODS
```

---

## 📋 ESQUEMA ESTÁNDAR DE METADATOS

### Tabla: `DTGDATASETS` (Extendida)

```sql
-- Campos adicionales para estandarización
ALTER TABLE DTGDATASETS ADD COLUMN IF NOT EXISTS DTGSTANDARDIZED BOOLEAN DEFAULT false;
ALTER TABLE DTGDATASETS ADD COLUMN IF NOT EXISTS DTGSTANDARDIZEDAT TIMESTAMP;
ALTER TABLE DTGDATASETS ADD COLUMN IF NOT EXISTS DTGSTANDARDIZEDFORMAT VARCHAR(50) DEFAULT 'PARQUET';
ALTER TABLE DTGDATASETS ADD COLUMN IF NOT EXISTS DTGSTANDARDIZEDPATH VARCHAR(500); -- Path en MinIO
ALTER TABLE DTGDATASETS ADD COLUMN IF NOT EXISTS DTGSTANDARDIZEDSCHEMA JSONB; -- Schema validado
ALTER TABLE DTGDATASETS ADD COLUMN IF NOT EXISTS DTGORIGINALFORMAT VARCHAR(50); -- Formato original
ALTER TABLE DTGDATASETS ADD COLUMN IF NOT EXISTS DTGORIGINALPATH VARCHAR(500); -- Path original en MinIO
```

### Tabla: `DTGDATASETSCHEMA` (Nueva)

**Prefijo:** `DTGSCH` (Data Governance Schema)

```sql
CREATE TABLE DTGDATASETSCHEMA (
    IDXSCHEMA BIGSERIAL PRIMARY KEY,
    IDXDATASET BIGINT NOT NULL REFERENCES DTGDATASETS(IDXDATASET) ON DELETE CASCADE,

    -- Información del Schema
    DTGSCHVERSION VARCHAR(50) NOT NULL DEFAULT '1.0.0',
    DTGSCHSCHEMA JSONB NOT NULL, -- Schema completo en formato JSON Schema

    -- Campos del Dataset
    DTGSCHFIELDS JSONB NOT NULL, -- Array de campos con tipos, nullable, etc.
    DTGSCHPRIMARYKEYS TEXT[], -- Campos que forman la clave primaria
    DTGSCHREQUIREDFIELDS TEXT[], -- Campos obligatorios

    -- Validaciones
    DTGSCHVALIDATIONRULES JSONB, -- Reglas de validación personalizadas
    DTGSCHCONSTRAINTS JSONB, -- Constraints (unique, foreign keys, etc.)

    -- Estadísticas del Schema
    DTGSCHTOTALFIELDS INT,
    DTGSCHNUMERICFIELDS INT,
    DTGSCHTEXTFIELDS INT,
    DTGSCHDATEFIELDS INT,
    DTGSCHCATEGORICALFIELDS INT,

    -- Estado
    DTGSCHVALIDATED BOOLEAN DEFAULT false,
    DTGSCHVALIDATEDAT TIMESTAMP,
    DTGSCHVALIDATEDBY BIGINT,

    -- Auditoría
    DTGSCHCREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    DTGSCHCREATEDBY BIGINT,
    DTGSCHUPDATEDAT TIMESTAMP,
    DTGSCHUPDATEDBY BIGINT,

    UNIQUE(IDXDATASET, DTGSCHVERSION)
);

CREATE INDEX idx_dtgsch_dataset ON DTGDATASETSCHEMA(IDXDATASET);
CREATE INDEX idx_dtgsch_validated ON DTGDATASETSCHEMA(DTGSCHVALIDATED);

COMMENT ON TABLE DTGDATASETSCHEMA IS 'Esquemas estandarizados de datasets';
```

### Ejemplo de Schema JSON

```json
{
  "version": "1.0.0",
  "type": "object",
  "properties": {
    "id": {
      "type": "integer",
      "description": "Identificador único",
      "nullable": false,
      "primaryKey": true
    },
    "text": {
      "type": "string",
      "description": "Texto del registro",
      "nullable": false,
      "maxLength": 10000
    },
    "label": {
      "type": "string",
      "description": "Etiqueta de clasificación",
      "nullable": true,
      "enum": ["positive", "negative", "neutral"]
    },
    "created_at": {
      "type": "datetime",
      "description": "Fecha de creación",
      "nullable": false,
      "format": "ISO8601"
    },
    "metadata": {
      "type": "object",
      "description": "Metadatos adicionales",
      "nullable": true,
      "properties": {
        "source": {"type": "string"},
        "author": {"type": "string"}
      }
    }
  },
  "required": ["id", "text", "created_at"],
  "primaryKeys": ["id"],
  "statistics": {
    "totalFields": 5,
    "numericFields": 1,
    "textFields": 2,
    "dateFields": 1,
    "objectFields": 1
  }
}
```

---

## 🔄 PROCESO DE NORMALIZACIÓN

### Servicio: `DatasetStandardizationService`

**Ubicación:** Microservicio Python (`codeflowx-dataset-service`)

```python
# services/dataset-service/src/main/python/dataset_standardization.py

from typing import Dict, Any, Optional
import pandas as pd
import pyarrow as pa
import pyarrow.parquet as pq
from pathlib import Path
import json
from minio import Minio
from minio.error import S3Error

class DatasetStandardizationService:
    """
    Servicio para normalizar datasets descargados a formato estándar.
    """

    def __init__(self, minio_client: Minio, storage_bucket: str = "datasets"):
        self.minio_client = minio_client
        self.storage_bucket = storage_bucket

    def standardize_dataset(
        self,
        dataset_id: str,
        source_path: str,
        source_format: str,
        metadata: Dict[str, Any]
    ) -> Dict[str, Any]:
        """
        Normaliza un dataset a formato Parquet estándar.

        Args:
            dataset_id: ID del dataset
            source_path: Path del archivo original en MinIO
            source_format: Formato original (CSV, JSON, PARQUET, etc.)
            metadata: Metadatos del dataset

        Returns:
            Dict con información del dataset estandarizado
        """
        # 1. Descargar dataset original
        raw_data = self._download_from_minio(source_path)

        # 2. Cargar según formato
        df = self._load_dataset(raw_data, source_format)

        # 3. Normalizar tipos y estructura
        df_normalized = self._normalize_dataframe(df)

        # 4. Validar schema
        schema = self._infer_and_validate_schema(df_normalized)

        # 5. Convertir a Parquet
        parquet_data = self._convert_to_parquet(df_normalized, schema)

        # 6. Generar estadísticas
        statistics = self._calculate_statistics(df_normalized)

        # 7. Generar muestra
        sample = df_normalized.head(100).to_dict('records')

        # 8. Subir a MinIO
        standardized_path = f"{dataset_id}/standardized/data.parquet"
        self._upload_to_minio(standardized_path, parquet_data)

        # 9. Guardar schema y metadatos
        schema_path = f"{dataset_id}/standardized/schema.json"
        self._upload_schema(schema_path, schema)

        statistics_path = f"{dataset_id}/standardized/statistics.json"
        self._upload_statistics(statistics_path, statistics)

        sample_path = f"{dataset_id}/standardized/sample.json"
        self._upload_sample(sample_path, sample)

        return {
            "dataset_id": dataset_id,
            "standardized": True,
            "format": "PARQUET",
            "path": standardized_path,
            "schema": schema,
            "statistics": statistics,
            "record_count": len(df_normalized),
            "size_bytes": len(parquet_data)
        }

    def _load_dataset(self, data: bytes, format: str) -> pd.DataFrame:
        """Carga dataset según formato."""
        if format.upper() == "CSV":
            return pd.read_csv(io.BytesIO(data))
        elif format.upper() == "JSON":
            return pd.read_json(io.BytesIO(data))
        elif format.upper() == "PARQUET":
            return pd.read_parquet(io.BytesIO(data))
        elif format.upper() == "ARROW":
            return pa.ipc.open_file(io.BytesIO(data)).read_pandas()
        else:
            raise ValueError(f"Formato no soportado: {format}")

    def _normalize_dataframe(self, df: pd.DataFrame) -> pd.DataFrame:
        """Normaliza tipos y estructura del DataFrame."""
        df_normalized = df.copy()

        # Normalizar nombres de columnas (snake_case)
        df_normalized.columns = [
            self._to_snake_case(col) for col in df_normalized.columns
        ]

        # Normalizar tipos de datos
        for col in df_normalized.columns:
            df_normalized[col] = self._normalize_column_type(
                df_normalized[col]
            )

        # Eliminar duplicados
        df_normalized = df_normalized.drop_duplicates()

        # Resetear índice
        df_normalized = df_normalized.reset_index(drop=True)

        return df_normalized

    def _normalize_column_type(self, series: pd.Series) -> pd.Series:
        """Normaliza el tipo de una columna."""
        # Intentar convertir a numérico
        if series.dtype == 'object':
            try:
                numeric = pd.to_numeric(series, errors='coerce')
                if numeric.notna().sum() / len(series) > 0.8:
                    return numeric
            except:
                pass

            # Intentar convertir a fecha
            try:
                dates = pd.to_datetime(series, errors='coerce')
                if dates.notna().sum() / len(series) > 0.8:
                    return dates
            except:
                pass

        return series

    def _infer_and_validate_schema(self, df: pd.DataFrame) -> Dict[str, Any]:
        """Infiere y valida el schema del DataFrame."""
        schema = {
            "version": "1.0.0",
            "type": "object",
            "properties": {},
            "required": [],
            "statistics": {
                "totalFields": len(df.columns),
                "numericFields": 0,
                "textFields": 0,
                "dateFields": 0,
                "categoricalFields": 0
            }
        }

        for col in df.columns:
            col_info = self._get_column_info(df[col])
            schema["properties"][col] = col_info

            if not df[col].isna().any():
                schema["required"].append(col)

            # Actualizar estadísticas
            if col_info["type"] == "number":
                schema["statistics"]["numericFields"] += 1
            elif col_info["type"] == "string":
                schema["statistics"]["textFields"] += 1
            elif col_info["type"] == "datetime":
                schema["statistics"]["dateFields"] += 1

        return schema

    def _get_column_info(self, series: pd.Series) -> Dict[str, Any]:
        """Obtiene información de una columna."""
        info = {
            "type": self._pandas_to_json_type(series.dtype),
            "nullable": series.isna().any(),
            "description": ""
        }

        # Información adicional según tipo
        if info["type"] == "number":
            info["min"] = float(series.min()) if series.notna().any() else None
            info["max"] = float(series.max()) if series.notna().any() else None
            info["mean"] = float(series.mean()) if series.notna().any() else None
        elif info["type"] == "string":
            info["maxLength"] = int(series.str.len().max()) if series.notna().any() else None
            # Detectar si es categórico
            unique_ratio = series.nunique() / len(series)
            if unique_ratio < 0.1:
                info["categorical"] = True
                info["categories"] = series.unique().tolist()

        return info

    def _pandas_to_json_type(self, dtype) -> str:
        """Convierte tipo de pandas a tipo JSON Schema."""
        if pd.api.types.is_integer_dtype(dtype):
            return "integer"
        elif pd.api.types.is_float_dtype(dtype):
            return "number"
        elif pd.api.types.is_bool_dtype(dtype):
            return "boolean"
        elif pd.api.types.is_datetime64_any_dtype(dtype):
            return "datetime"
        else:
            return "string"

    def _convert_to_parquet(self, df: pd.DataFrame, schema: Dict) -> bytes:
        """Convierte DataFrame a Parquet."""
        buffer = io.BytesIO()

        # Crear schema de PyArrow
        pa_schema = self._json_schema_to_pyarrow(schema)

        # Convertir DataFrame a Table de PyArrow
        table = pa.Table.from_pandas(df, schema=pa_schema)

        # Escribir Parquet con compresión
        pq.write_table(
            table,
            buffer,
            compression='snappy',
            use_dictionary=True
        )

        return buffer.getvalue()

    def _calculate_statistics(self, df: pd.DataFrame) -> Dict[str, Any]:
        """Calcula estadísticas básicas del dataset."""
        return {
            "totalRecords": len(df),
            "totalColumns": len(df.columns),
            "nullValues": df.isnull().sum().to_dict(),
            "nullPercentage": (df.isnull().sum() / len(df) * 100).to_dict(),
            "memoryUsage": df.memory_usage(deep=True).sum(),
            "columnTypes": df.dtypes.astype(str).to_dict()
        }
```

---

## 🔌 API PARA MICROSERVICIOS PYTHON

### Endpoint: `/api/v1/datasets/{id}/standardized`

```python
# services/dataset-service/src/main/python/api/dataset_endpoints.py

from fastapi import APIRouter, HTTPException, Depends
from fastapi.responses import StreamingResponse
from minio import Minio
import io

router = APIRouter(prefix="/api/v1/datasets", tags=["datasets"])

@router.get("/{dataset_id}/standardized")
async def get_standardized_dataset(
    dataset_id: str,
    format: str = "parquet",  # parquet, pandas, json
    minio_client: Minio = Depends(get_minio_client)
):
    """
    Obtiene el dataset estandarizado en formato Parquet.

    Los microservicios Python pueden usar este endpoint para:
    - Descargar el dataset en formato Parquet
    - Cargarlo directamente con pandas/pyarrow
    - Realizar análisis sin preocuparse del formato original
    """
    try:
        # Obtener metadata del dataset
        dataset = get_dataset_metadata(dataset_id)

        if not dataset.get("standardized"):
            raise HTTPException(
                status_code=400,
                detail="Dataset no está estandarizado"
            )

        # Descargar desde MinIO
        standardized_path = dataset["standardized_path"]
        data = minio_client.get_object(
            "datasets",
            standardized_path
        )

        if format == "parquet":
            return StreamingResponse(
                io.BytesIO(data.read()),
                media_type="application/octet-stream",
                headers={
                    "Content-Disposition": f"attachment; filename={dataset_id}.parquet"
                }
            )
        elif format == "pandas":
            # Cargar y devolver como JSON
            import pandas as pd
            df = pd.read_parquet(io.BytesIO(data.read()))
            return df.to_dict('records')
        else:
            raise HTTPException(
                status_code=400,
                detail=f"Formato no soportado: {format}"
            )

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.get("/{dataset_id}/schema")
async def get_dataset_schema(
    dataset_id: str,
    minio_client: Minio = Depends(get_minio_client)
):
    """Obtiene el schema estandarizado del dataset."""
    try:
        schema_path = f"{dataset_id}/standardized/schema.json"
        schema_data = minio_client.get_object("datasets", schema_path)
        return json.loads(schema_data.read())
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.get("/{dataset_id}/statistics")
async def get_dataset_statistics(
    dataset_id: str,
    minio_client: Minio = Depends(get_minio_client)
):
    """Obtiene estadísticas del dataset estandarizado."""
    try:
        stats_path = f"{dataset_id}/standardized/statistics.json"
        stats_data = minio_client.get_object("datasets", stats_path)
        return json.loads(stats_data.read())
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
```

---

## 📊 USO DESDE MICROSERVICIOS DE ANÁLISIS

### Ejemplo: Microservicio de Análisis de Calidad

```python
# services/quality-analysis-service/src/main/python/quality_analyzer.py

import pandas as pd
import requests
from typing import Dict, Any

class DatasetQualityAnalyzer:
    """
    Analizador de calidad de datasets estandarizados.
    """

    def __init__(self, dataset_service_url: str):
        self.dataset_service_url = dataset_service_url

    def analyze_quality(self, dataset_id: str) -> Dict[str, Any]:
        """
        Analiza la calidad de un dataset estandarizado.

        Todos los datasets están en formato Parquet, por lo que
        el análisis es uniforme independientemente del origen.
        """
        # 1. Descargar dataset estandarizado
        df = self._load_standardized_dataset(dataset_id)

        # 2. Obtener schema
        schema = self._get_schema(dataset_id)

        # 3. Realizar análisis de calidad
        quality_metrics = {
            "completeness": self._analyze_completeness(df),
            "accuracy": self._analyze_accuracy(df, schema),
            "consistency": self._analyze_consistency(df, schema),
            "validity": self._analyze_validity(df, schema),
            "uniqueness": self._analyze_uniqueness(df, schema),
            "timeliness": self._analyze_timeliness(df, schema)
        }

        # 4. Calcular score global
        overall_score = sum(quality_metrics.values()) / len(quality_metrics)

        return {
            "dataset_id": dataset_id,
            "overall_score": overall_score,
            "metrics": quality_metrics,
            "issues": self._detect_issues(df, schema),
            "recommendations": self._generate_recommendations(quality_metrics)
        }

    def _load_standardized_dataset(self, dataset_id: str) -> pd.DataFrame:
        """Carga dataset estandarizado desde el servicio."""
        response = requests.get(
            f"{self.dataset_service_url}/api/v1/datasets/{dataset_id}/standardized",
            params={"format": "parquet"}
        )
        response.raise_for_status()

        # Cargar Parquet directamente
        return pd.read_parquet(io.BytesIO(response.content))

    def _get_schema(self, dataset_id: str) -> Dict[str, Any]:
        """Obtiene el schema del dataset."""
        response = requests.get(
            f"{self.dataset_service_url}/api/v1/datasets/{dataset_id}/schema"
        )
        response.raise_for_status()
        return response.json()

    def _analyze_completeness(self, df: pd.DataFrame) -> float:
        """Analiza completitud (1.0 = sin nulos)."""
        total_cells = len(df) * len(df.columns)
        null_cells = df.isnull().sum().sum()
        return 1.0 - (null_cells / total_cells)

    def _analyze_accuracy(self, df: pd.DataFrame, schema: Dict) -> float:
        """Analiza precisión según schema."""
        # Validar tipos, rangos, formatos según schema
        errors = 0
        total = len(df)

        for field, field_schema in schema["properties"].items():
            if field in df.columns:
                # Validar tipo
                if not self._validate_type(df[field], field_schema):
                    errors += 1

                # Validar rangos
                if "min" in field_schema and df[field].min() < field_schema["min"]:
                    errors += 1
                if "max" in field_schema and df[field].max() > field_schema["max"]:
                    errors += 1

        return 1.0 - (errors / (total * len(schema["properties"])))

    # ... más métodos de análisis
```

### Ejemplo: Microservicio de Análisis de Seguridad

```python
# services/security-analysis-service/src/main/python/security_analyzer.py

class DatasetSecurityAnalyzer:
    """
    Analizador de seguridad de datasets estandarizados.
    """

    def analyze_security(self, dataset_id: str) -> Dict[str, Any]:
        """Analiza seguridad del dataset."""
        df = self._load_standardized_dataset(dataset_id)

        return {
            "pii_detected": self._detect_pii(df),
            "sensitive_data": self._detect_sensitive_data(df),
            "encryption_status": self._check_encryption(dataset_id),
            "access_control": self._check_access_control(dataset_id),
            "compliance": self._check_compliance(df)
        }
```

---

## 🎨 INTEGRACIÓN CON FRONTEND

### Pantalla: Descargar y Estandarizar Dataset

```typescript
// app/(app)/governance/data/datasets/create/page.tsx

'use client';

import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';

const datasetSourceSchema = z.object({
  source: z.enum(['HUGGINGFACE', 'KAGGLE', 'API', 'UPLOAD']),
  sourceId: z.string().min(1),
  autoStandardize: z.boolean().default(true),
});

export default function CreateDatasetPage() {
  const [standardizing, setStandardizing] = useState(false);
  const [standardizationProgress, setStandardizationProgress] = useState(0);

  const form = useForm({
    resolver: zodResolver(datasetSourceSchema),
  });

  const handleDownloadAndStandardize = async (data: z.infer<typeof datasetSourceSchema>) => {
    setStandardizing(true);

    try {
      // 1. Descargar dataset
      const downloadResponse = await fetch('/api/v1/governance/data/datasets/download', {
        method: 'POST',
        body: JSON.stringify({
          source: data.source,
          sourceId: data.sourceId,
        }),
      });

      const { datasetId } = await downloadResponse.json();

      // 2. Estandarizar (si está habilitado)
      if (data.autoStandardize) {
        const standardizeResponse = await fetch(
          `/api/v1/governance/data/datasets/${datasetId}/standardize`,
          {
            method: 'POST',
          }
        );

        // Monitorear progreso
        const progressStream = standardizeResponse.body;
        // ... manejar progreso

        const result = await standardizeResponse.json();

        // 3. Redirigir a detalle del dataset
        router.push(`/governance/data/datasets/${datasetId}`);
      }
    } catch (error) {
      console.error('Error:', error);
    } finally {
      setStandardizing(false);
    }
  };

  return (
    <div className="container mx-auto p-6">
      <h1 className="text-2xl font-bold mb-6">
        {t('governance.data.datasets.create.title', 'Crear Dataset')}
      </h1>

      <Form {...form}>
        <form onSubmit={form.handleSubmit(handleDownloadAndStandardize)}>
          {/* Selector de origen */}
          <FormField
            control={form.control}
            name="source"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Origen</FormLabel>
                <Select onValueChange={field.onChange} defaultValue={field.value}>
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="Seleccionar origen" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    <SelectItem value="HUGGINGFACE">HuggingFace</SelectItem>
                    <SelectItem value="KAGGLE">Kaggle</SelectItem>
                    <SelectItem value="API">API Externa</SelectItem>
                    <SelectItem value="UPLOAD">Subir Archivo</SelectItem>
                  </SelectContent>
                </Select>
              </FormItem>
            )}
          />

          {/* ID del dataset en el origen */}
          <FormField
            control={form.control}
            name="sourceId"
            render={({ field }) => (
              <FormItem>
                <FormLabel>ID del Dataset</FormLabel>
                <FormControl>
                  <Input
                    placeholder="ej: glue, squad, imdb"
                    {...field}
                  />
                </FormControl>
                <FormDescription>
                  ID del dataset en HuggingFace/Kaggle
                </FormDescription>
              </FormItem>
            )}
          />

          {/* Auto-estandarizar */}
          <FormField
            control={form.control}
            name="autoStandardize"
            render={({ field }) => (
              <FormItem className="flex flex-row items-center justify-between rounded-lg border p-4">
                <div className="space-y-0.5">
                  <FormLabel>Estandarizar Automáticamente</FormLabel>
                  <FormDescription>
                    Convertir a formato Parquet y validar schema
                  </FormDescription>
                </div>
                <FormControl>
                  <Switch
                    checked={field.value}
                    onCheckedChange={field.onChange}
                  />
                </FormControl>
              </FormItem>
            )}
          />

          {standardizing && (
            <div className="mt-4">
              <Progress value={standardizationProgress} />
              <p className="text-sm text-muted-foreground mt-2">
                Estandarizando dataset...
              </p>
            </div>
          )}

          <Button type="submit" disabled={standardizing}>
            {standardizing ? 'Estandarizando...' : 'Descargar y Estandarizar'}
          </Button>
        </form>
      </Form>
    </div>
  );
}
```

---

## ✅ BENEFICIOS DEL MODELO ESTANDARIZADO

1. **Análisis Uniforme:** Todos los microservicios Python trabajan con el mismo formato
2. **Eficiencia:** Parquet es más eficiente que CSV/JSON para análisis
3. **Validación:** Schema validado garantiza calidad de datos
4. **Trazabilidad:** Se mantiene el original + versión estandarizada
5. **Escalabilidad:** Fácil agregar nuevos tipos de análisis
6. **Interoperabilidad:** Compatible con pandas, PyArrow, Spark, etc.

---

## 🚀 PLAN DE IMPLEMENTACIÓN

### Fase 1: Servicio de Estandarización (2 semanas)
- ✅ Crear `DatasetStandardizationService` en Python
- ✅ Implementar conversión a Parquet
- ✅ Implementar inferencia de schema
- ✅ Integración con MinIO

### Fase 2: API REST (1 semana)
- ✅ Endpoints para obtener datasets estandarizados
- ✅ Endpoints para schema y estadísticas
- ✅ Documentación OpenAPI

### Fase 3: Integración Frontend (1 semana)
- ✅ Pantalla de descarga y estandarización
- ✅ Indicadores de progreso
- ✅ Visualización de schema

### Fase 4: Actualización Microservicios (2 semanas)
- ✅ Actualizar microservicios de análisis para usar formato estándar
- ✅ Testing end-to-end
- ✅ Documentación

**Total: 6 semanas**

---

**Documento creado:** Diciembre 2025
**Versión:** 1.0
