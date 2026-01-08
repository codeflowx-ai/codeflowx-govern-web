# Estándar Apache Parquet - CodeflowX

**Fecha:** Diciembre 2025
**Estado:** ✅ ESTÁNDAR OFICIAL
**Versión:** 1.0

---

## 🎯 DECISIÓN ARQUITECTÓNICA

**Apache Parquet es el formato estándar oficial** para todos los datasets en el sistema CodeflowX.

---

## 📋 ESPECIFICACIÓN DEL ESTÁNDAR

### Formato de Archivo

- **Formato:** Apache Parquet
- **Versión:** Parquet 2.0+
- **Compresión:** Snappy (por defecto)
- **Encoding:** Dictionary encoding habilitado
- **Schema:** Embebido en el archivo

### Estructura de Almacenamiento

```
minio/datasets/
├── {dataset_id}/
│   ├── raw/                          # Datos originales (backup)
│   │   ├── original.{ext}            # Formato original (CSV, JSON, etc.)
│   │   └── metadata.json             # Metadatos originales
│   ├── standardized/                 # Datos normalizados
│   │   ├── data.parquet              # ⭐ Dataset en Parquet (ESTÁNDAR)
│   │   ├── schema.json               # Schema validado (JSON Schema)
│   │   ├── sample.json                # Muestra (primeras 100 filas)
│   │   └── statistics.json           # Estadísticas básicas
│   └── analysis/                     # Resultados de análisis
│       ├── quality.json              # Análisis de calidad
│       ├── security.json             # Análisis de seguridad
│       ├── compliance.json           # Análisis de cumplimiento
│       └── ods.json                  # Análisis ODS
```

---

## ✅ REQUISITOS OBLIGATORIOS

### 1. Todos los Datasets Deben Estar en Parquet

- ✅ Datasets descargados de fuentes externas (HuggingFace, Kaggle) → Convertir a Parquet
- ✅ Datasets subidos por usuarios → Convertir a Parquet
- ✅ Datasets generados internamente → Guardar directamente en Parquet
- ✅ Datasets de bases de datos → Exportar a Parquet

### 2. Proceso de Estandarización

**Obligatorio para todos los datasets:**

1. **Descarga/Ingesta** → Formato original (backup en `raw/`)
2. **Normalización** → Conversión a Parquet
3. **Validación** → Schema validation
4. **Almacenamiento** → Parquet en `standardized/data.parquet`

### 3. Schema Validation

- ✅ Schema debe estar validado antes de almacenar
- ✅ Schema debe guardarse en `DTGDATASETSCHEMA`
- ✅ Schema debe seguir JSON Schema estándar

---

## 🔧 CONFIGURACIÓN TÉCNICA

### Parámetros de Parquet

```python
# Configuración estándar para escritura de Parquet
parquet_config = {
    "compression": "snappy",           # Compresión por defecto
    "use_dictionary": True,            # Dictionary encoding
    "write_statistics": True,          # Estadísticas por columna
    "row_group_size": 128 * 1024 * 1024,  # 128MB por row group
    "data_page_size": 1024 * 1024,    # 1MB por página
    "version": "2.0"                   # Versión Parquet
}
```

### Ejemplo de Escritura

```python
import pyarrow as pa
import pyarrow.parquet as pq

# Convertir DataFrame a Parquet
table = pa.Table.from_pandas(df, schema=pa_schema)

pq.write_table(
    table,
    output_path,
    compression='snappy',
    use_dictionary=True,
    write_statistics=True
)
```

---

## 📊 ESQUEMA DE METADATOS

### Tabla: `DTGDATASETS`

```sql
-- Campos relacionados con Parquet
DTGSTANDARDIZED BOOLEAN DEFAULT false,        -- Si está estandarizado
DTGSTANDARDIZEDAT TIMESTAMP,                   -- Fecha de estandarización
DTGSTANDARDIZEDFORMAT VARCHAR(50) DEFAULT 'PARQUET',  -- Siempre 'PARQUET'
DTGSTANDARDIZEDPATH VARCHAR(500),              -- Path al archivo .parquet
DTGORIGINALFORMAT VARCHAR(50),                -- Formato original
DTGORIGINALPATH VARCHAR(500)                  -- Path del original (backup)
```

### Validación

- ✅ `DTGSTANDARDIZED = true` → Dataset debe tener `DTGSTANDARDIZEDPATH` válido
- ✅ `DTGSTANDARDIZEDFORMAT` debe ser siempre `'PARQUET'`
- ✅ Archivo Parquet debe existir en MinIO/S3

---

## 🔌 API ESTÁNDAR

### Endpoint: Obtener Dataset Estandarizado

```http
GET /api/v1/governance/data/datasets/{id}/standardized
Accept: application/octet-stream
```

**Respuesta:**
- Content-Type: `application/octet-stream`
- Body: Archivo Parquet binario

### Endpoint: Obtener Schema

```http
GET /api/v1/governance/data/datasets/{id}/schema
Accept: application/json
```

**Respuesta:**
```json
{
  "version": "1.0.0",
  "type": "object",
  "properties": {
    "id": {
      "type": "integer",
      "nullable": false,
      "primaryKey": true
    },
    "text": {
      "type": "string",
      "nullable": false,
      "maxLength": 10000
    }
  },
  "required": ["id", "text"]
}
```

---

## 🐍 USO DESDE MICROSERVICIOS PYTHON

### Cargar Dataset Estandarizado

```python
import pandas as pd
import requests

def load_standardized_dataset(dataset_id: str) -> pd.DataFrame:
    """
    Carga un dataset estandarizado en formato Parquet.

    Todos los datasets están en Parquet, por lo que la carga
    es uniforme independientemente del origen.
    """
    response = requests.get(
        f"/api/v1/governance/data/datasets/{dataset_id}/standardized"
    )
    response.raise_for_status()

    # Cargar Parquet directamente
    return pd.read_parquet(io.BytesIO(response.content))
```

### Ventajas

- ✅ **Uniforme:** Todos los datasets se cargan igual
- ✅ **Eficiente:** Parquet es más rápido que CSV/JSON
- ✅ **Tipado:** Tipos preservados correctamente
- ✅ **Compresión:** Menor uso de memoria y red

---

## 📝 PROCESO DE ESTANDARIZACIÓN

### Flujo Automático

```
1. Descarga/Ingesta
   ↓
2. Guardar Original (backup)
   ↓
3. Cargar en DataFrame
   ↓
4. Normalizar tipos y estructura
   ↓
5. Validar schema
   ↓
6. Convertir a Parquet
   ↓
7. Subir a MinIO/S3
   ↓
8. Guardar metadata en PostgreSQL
   ↓
9. Marcar como estandarizado
```

### Servicio: `DatasetStandardizationService`

Ver implementación completa en: `MODELO_DATASET_ESTANDARIZADO.md`

---

## ✅ CHECKLIST DE CUMPLIMIENTO

Para que un dataset cumpla con el estándar:

- [ ] Dataset descargado/ingestado
- [ ] Original guardado en `raw/` (backup)
- [ ] Convertido a Parquet
- [ ] Schema validado y guardado
- [ ] Parquet subido a `standardized/data.parquet`
- [ ] Estadísticas generadas
- [ ] Metadata actualizada en `DTGDATASETS`
- [ ] `DTGSTANDARDIZED = true`
- [ ] `DTGSTANDARDIZEDFORMAT = 'PARQUET'`

---

## 🚫 RESTRICCIONES

### NO Permitido

- ❌ Almacenar datasets en CSV/JSON para análisis
- ❌ Saltarse el proceso de estandarización
- ❌ Usar formatos diferentes a Parquet para datasets estandarizados
- ❌ Acceder directamente a archivos originales desde microservicios de análisis

### Excepciones

- ✅ Archivos originales se mantienen como backup en `raw/`
- ✅ Muestras pequeñas pueden estar en JSON para visualización
- ✅ Datasets temporales durante procesamiento (deben convertirse al final)

---

## 📚 REFERENCIAS

- **Documentación Parquet:** https://parquet.apache.org/
- **PyArrow:** https://arrow.apache.org/docs/python/
- **Pandas Parquet:** https://pandas.pydata.org/docs/reference/api/pandas.DataFrame.to_parquet.html

---

## 🔄 VERSIONAMIENTO

- **v1.0** (Diciembre 2025): Estándar inicial con Parquet 2.0, compresión Snappy

---

**Documento creado:** Diciembre 2025
**Aprobado por:** Arquitectura CodeflowX
**Versión:** 1.0
