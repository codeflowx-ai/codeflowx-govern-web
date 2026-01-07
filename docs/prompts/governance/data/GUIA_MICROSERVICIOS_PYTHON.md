# 🐍 GUÍA DE MICROSERVICIOS PYTHON - GOBIERNO DEL DATO

**Versión:** 1.1
**Fecha:** Enero 2025
**Audiencia:** Data Engineers, Backend Developers, DevOps

---

## 📋 INTRODUCCIÓN

Esta guía describe qué funcionalidades deben crearse o modificarse en los microservicios Python existentes, o si es necesario crear nuevos microservicios para soportar el módulo de Gobierno del Dato.

**IMPORTANTE:** Los microservicios Python **NO acceden directamente a la base de datos de negocio de CodeFlowX**. Solo reciben datos vía API, procesan y retornan resultados. La persistencia se realiza desde el backend Java.

### 📚 Guías Relacionadas

- **Guía de Implementación:** `docs/prompts/governance/testing/GUIA_IMPLEMENTACION_MICROSERVICIOS_PYTHON.md` - Guía completa para implementar microservicios Python siguiendo el patrón arquitectónico existente
- **Guía Funcional:** `docs/prompts/governance/data/GUIA_FUNCIONAL_DATA_GOVERNANCE.md` - Funcionalidades del módulo de Gobierno del Dato

---

## 🏗️ ARQUITECTURA ACTUAL

### Microservicios Python Existentes

Los microservicios Python actuales están organizados bajo el Gateway Spring Cloud y se acceden mediante el cliente Java `AIGovernanceClient`:

| Microservicio | Puerto | Endpoint Base | Propósito |
|--------------|--------|---------------|-----------|
| LLM Evaluation | 8002 | `/api/llm/**` | Evaluación de modelos LLM |
| Prompt Governance | 8003 | `/api/prompt/**` | Evaluación de prompts |
| RAG Evaluation | 8004 | `/api/rag/**` | Evaluación de sistemas RAG |
| Agent Monitoring | 8005 | `/api/agent/**` | Monitoreo de agentes |
| Model Wrapper | 8006 | `/api/models/**` | Wrapper para invocación de modelos |
| AI Interpreter | 8011 | `/api/interpret/**` | Interpretación de resultados técnicos |
| Bias Detection | 8007 | `/api/bias/**` | Detección de sesgos |
| Deepfake Detection | 8008 | `/api/deepfake/**` | Detección de deepfakes |
| Copyright Compliance | 8009 | `/api/copyright/**` | Cumplimiento de copyright |
| FRIA Generator | 8010 | `/api/fria/**` | Generación de FRIA |
| ISO 42001 Assessor | 8012 | `/api/iso42001/**` | Evaluación ISO 42001 |
| Conformity Assessment | 8013 | `/api/conformity/**` | Evaluación de conformidad |
| EU Declaration Generator | 8014 | `/api/eu-declaration/**` | Generación de declaraciones EU |

### Cliente Java

El cliente Java (`codeflowx.govern.nocode.client`) proporciona acceso unificado a todos los microservicios mediante el patrón Factoría:

```java
@Autowired
private AIGovernanceClient governance;

// Ejemplo de uso
BiasAnalysisResponse response = governance.biasDetection()
    .analyzeBias(biasRequest);
```

---

## 🎯 FUNCIONALIDADES REQUERIDAS PARA GOBIERNO DEL DATO

### 1. **Análisis de Calidad de Datos (ISO 8000)**

#### ¿Crear Nuevo Microservicio o Modificar Existente?

**✅ RECOMENDACIÓN: Crear Nuevo Microservicio**

**Nombre:** `dataset-quality-evaluation`
**Puerto:** 8015
**Endpoint Base:** `/api/dataset-quality/**`

#### Justificación

- Funcionalidad específica y compleja (6 dimensiones ISO 8000)
- Requiere procesamiento de datasets grandes (Parquet)
- Lógica de negocio independiente
- Facilita escalabilidad y mantenimiento

#### Funcionalidades a Implementar

**1.1. Evaluación de 6 Dimensiones ISO 8000**

```python
POST /api/dataset-quality/evaluate-dimensions
```

**Request:**
```json
{
  "datasetPath": "s3://bucket/dataset.parquet",
  "samplePercentage": 10,
  "dimensions": {
    "completeness": {
      "enabled": true,
      "threshold": 0.95
    },
    "accuracy": {
      "enabled": true,
      "threshold": 0.90
    },
    "consistency": {
      "enabled": true,
      "threshold": 0.85
    },
    "validity": {
      "enabled": true,
      "rules": ["email_format", "date_range"]
    },
    "timeliness": {
      "enabled": true,
      "maxAgeDays": 30
    },
    "uniqueness": {
      "enabled": true,
      "threshold": 0.98
    }
  }
}
```

**Response:**
```json
{
  "overallScore": 0.87,
  "dimensions": {
    "completeness": {
      "score": 0.95,
      "status": "PASS",
      "details": {
        "nullPercentage": 0.05,
        "missingColumns": [],
        "totalRows": 100000,
        "completeRows": 95000
      }
    },
    "accuracy": {
      "score": 0.92,
      "status": "PASS",
      "details": {
        "errorRate": 0.08,
        "validationErrors": []
      }
    },
    "consistency": {
      "score": 0.88,
      "status": "WARNING",
      "details": {
        "inconsistencies": [
          {
            "field": "age",
            "issue": "negative_values",
            "count": 15
          }
        ]
      }
    },
    "validity": {
      "score": 0.85,
      "status": "WARNING",
      "details": {
        "invalidRecords": 1500,
        "validationRules": [
          {
            "rule": "email_format",
            "violations": 200
          }
        ]
      }
    },
    "timeliness": {
      "score": 0.90,
      "status": "PASS",
      "details": {
        "lastUpdate": "2025-01-15T10:30:00Z",
        "ageDays": 5
      }
    },
    "uniqueness": {
      "score": 0.99,
      "status": "PASS",
      "details": {
        "duplicateRows": 100,
        "duplicatePercentage": 0.001
      }
    }
  },
  "recommendations": [
    "Revisar valores negativos en campo 'age'",
    "Validar formato de emails en 200 registros"
  ]
}
```

**1.2. Análisis de Muestreo**

```python
POST /api/dataset-quality/analyze-sample
```

Permite analizar un porcentaje del dataset (10-100%) para optimizar rendimiento.

**1.3. Validación de Esquema**

```python
POST /api/dataset-quality/validate-schema
```

Valida que el dataset Parquet cumple con un esquema esperado.

---

### 2. **Detección de PII (Datos Personales)**

#### ¿Crear Nuevo Microservicio o Modificar Existente?

**✅ RECOMENDACIÓN: Modificar Microservicio Existente o Crear Nuevo**

**Opción A:** Extender `Bias Detection` (8007)
**Opción B:** Crear `dataset-privacy-detection` (8016)

**Recomendación:** Crear nuevo microservicio por separación de responsabilidades.

#### Funcionalidades a Implementar

**2.1. Detección de PII**

```python
POST /api/dataset-privacy/detect-pii
```

**Request:**
```json
{
  "datasetPath": "s3://bucket/dataset.parquet",
  "samplePercentage": 10,
  "piiTypes": [
    "EMAIL",
    "PHONE",
    "SSN",
    "CREDIT_CARD",
    "IP_ADDRESS",
    "DATE_OF_BIRTH",
    "PASSPORT",
    "DRIVER_LICENSE"
  ]
}
```

**Response:**
```json
{
  "piiDetected": true,
  "piiTypes": ["EMAIL", "PHONE", "DATE_OF_BIRTH"],
  "columns": [
    {
      "columnName": "email",
      "piiType": "EMAIL",
      "confidence": 0.98,
      "sampleValues": ["user@example.com"]
    },
    {
      "columnName": "phone",
      "piiType": "PHONE",
      "confidence": 0.95,
      "sampleValues": ["+34 600 123 456"]
    }
  ],
  "totalPiiRecords": 85000,
  "percentage": 0.85,
  "recommendations": [
    "Aplicar enmascaramiento de PII antes de análisis",
    "Verificar base legal (GDPR Art. 6)",
    "Considerar DPIA (GDPR Art. 35)"
  ]
}
```

**2.2. Enmascaramiento de PII**

```python
POST /api/dataset-privacy/mask-pii
```

Genera versión enmascarada del dataset para análisis seguro.

---

### 3. **Análisis de Sesgos en Datasets**

#### ¿Crear Nuevo Microservicio o Modificar Existente?

**✅ RECOMENDACIÓN: Extender Microservicio Existente**

**Microservicio:** `Bias Detection` (8007)
**Endpoint Base:** `/api/bias/**`

Ya existe funcionalidad de detección de sesgos, pero necesita extensión para datasets.

#### Funcionalidades a Agregar

**3.1. Análisis de Sesgos en Dataset**

```python
POST /api/bias/analyze-dataset-bias
```

**Request:**
```json
{
  "datasetPath": "s3://bucket/dataset.parquet",
  "samplePercentage": 10,
  "protectedAttributes": ["gender", "age_group", "ethnicity"],
  "targetColumn": "loan_approved",
  "biasMetrics": [
    "DEMOGRAPHIC_PARITY",
    "EQUAL_OPPORTUNITY",
    "EQUALIZED_ODDS",
    "CALIBRATION"
  ]
}
```

**Response:**
```json
{
  "biasScore": 0.65,
  "biasDetected": true,
  "metrics": {
    "demographicParity": {
      "value": 0.15,
      "threshold": 0.10,
      "status": "FAIL",
      "explanation": "Diferencia significativa en tasas de aprobación entre grupos"
    },
    "equalOpportunity": {
      "value": 0.12,
      "threshold": 0.10,
      "status": "WARNING",
      "explanation": "Diferencia moderada en tasas de verdadero positivo"
    }
  },
  "groupAnalysis": [
    {
      "group": "gender=male",
      "positiveRate": 0.75,
      "sampleSize": 50000
    },
    {
      "group": "gender=female",
      "positiveRate": 0.60,
      "sampleSize": 50000
    }
  ],
  "recommendations": [
    "Balancear dataset por género",
    "Revisar criterios de aprobación",
    "Aplicar técnicas de mitigación de sesgos"
  ]
}
```

---

### 4. **Estandarización a Parquet**

#### ¿Crear Nuevo Microservicio o Modificar Existente?

**✅ RECOMENDACIÓN: Crear Nuevo Microservicio**

**Nombre:** `dataset-standardization`
**Puerto:** 8017
**Endpoint Base:** `/api/dataset-standardization/**`

#### Funcionalidades a Implementar

**4.1. Conversión a Parquet**

```python
POST /api/dataset-standardization/convert-to-parquet
```

**Request:**
```json
{
  "sourcePath": "s3://bucket/dataset.csv",
  "sourceFormat": "CSV",
  "targetPath": "s3://bucket/dataset.parquet",
  "compression": "SNAPPY",
  "schemaValidation": true,
  "options": {
    "delimiter": ",",
    "header": true,
    "encoding": "utf-8"
  }
}
```

**Response:**
```json
{
  "success": true,
  "sourcePath": "s3://bucket/dataset.csv",
  "targetPath": "s3://bucket/dataset.parquet",
  "sourceSize": 104857600,
  "targetSize": 31457280,
  "compressionRatio": 0.30,
  "rows": 1000000,
  "columns": 25,
  "schema": {
    "fields": [
      {
        "name": "id",
        "type": "int64",
        "nullable": false
      },
      {
        "name": "email",
        "type": "string",
        "nullable": true
      }
    ]
  },
  "checksum": "sha256:abc123...",
  "processingTime": 45.2
}
```

**4.2. Validación de Parquet**

```python
POST /api/dataset-standardization/validate-parquet
```

Valida que un archivo Parquet es válido y puede leerse correctamente.

**4.3. Conversión desde Múltiples Formatos**

Soporte para:
- CSV
- JSON
- Excel (XLSX)
- AVRO
- XML
- ORC

---

### 5. **Análisis de Impacto de Cambios**

#### ¿Crear Nuevo Microservicio o Modificar Existente?

**✅ RECOMENDACIÓN: Crear Nuevo Microservicio**

**Nombre:** `dataset-impact-analysis`
**Puerto:** 8018
**Endpoint Base:** `/api/dataset-impact/**`

#### Funcionalidades a Implementar

**5.1. Análisis de Dependencias**

```python
POST /api/dataset-impact/analyze-dependencies
```

**Request:**
```json
{
  "datasetId": "uuid-123",
  "datasetPath": "s3://bucket/dataset.parquet",
  "lineageData": [
    {
      "sourceDatasetId": "uuid-456",
      "transformationType": "JOIN",
      "columns": ["user_id", "email"]
    }
  ]
}
```

**Response:**
```json
{
  "affectedDatasets": [
    {
      "datasetId": "uuid-789",
      "name": "Dataset de Ventas Agregado",
      "impact": "HIGH",
      "reason": "Depende directamente de columnas que cambiarían",
      "affectedColumns": ["user_id", "email"],
      "transformationType": "AGGREGATION"
    }
  ],
  "affectedOrigins": [
    {
      "originId": "origin-1",
      "name": "PostgreSQL - Ventas",
      "type": "POSTGRESQL",
      "impact": "MEDIUM"
    }
  ],
  "totalImpact": 3,
  "highImpactCount": 1,
  "mediumImpactCount": 2,
  "lowImpactCount": 0
}
```

---

## 📊 RESUMEN DE MICROSERVICIOS

### Nuevos Microservicios a Crear

| Microservicio | Puerto | Endpoint | Prioridad |
|--------------|--------|----------|-----------|
| Dataset Quality Evaluation | 8015 | `/api/dataset-quality/**` | 🔴 Alta |
| Dataset Privacy Detection | 8016 | `/api/dataset-privacy/**` | 🔴 Alta |
| Dataset Standardization | 8017 | `/api/dataset-standardization/**` | 🔴 Alta |
| Dataset Impact Analysis | 8018 | `/api/dataset-impact/**` | 🟡 Media |

### Microservicios a Modificar

| Microservicio | Puerto | Modificaciones | Prioridad |
|--------------|--------|----------------|-----------|
| Bias Detection | 8007 | Agregar análisis de sesgos en datasets | 🟡 Media |

---

## 🔧 ESPECIFICACIONES TÉCNICAS

### Stack Tecnológico

Seguir el mismo stack que los microservicios existentes (ver `GUIA_IMPLEMENTACION_MICROSERVICIOS_PYTHON.md`):

- **Framework:** FastAPI
- **Procesamiento de Datos:** Pandas, PyArrow (Parquet)
- **Validación:** Pydantic
- **Almacenamiento:** MinIO/S3 (solo lectura/escritura de archivos)
- **Detección PII:** Presidio, regex patterns
- **Análisis de Sesgos:** Fairlearn, Aequitas
- **Calidad de Datos:** Great Expectations, Pandas Profiling

### Estructura de Proyecto

Seguir la estructura estándar documentada en `GUIA_IMPLEMENTACION_MICROSERVICIOS_PYTHON.md`:

```
dataset-quality-evaluation/
├── app/
│   ├── __init__.py
│   ├── main.py
│   ├── models/
│   │   ├── request.py
│   │   └── response.py
│   ├── services/
│   │   ├── completeness_service.py
│   │   ├── accuracy_service.py
│   │   ├── consistency_service.py
│   │   ├── validity_service.py
│   │   ├── timeliness_service.py
│   │   └── uniqueness_service.py
│   └── utils/
│       ├── parquet_reader.py
│       └── s3_client.py
├── requirements.txt
├── Dockerfile
└── README.md
```

### Integración con Cliente Java

Después de crear cada microservicio, agregar cliente en `codeflowx.govern.nocode.client` siguiendo el patrón existente:

```java
// En AIGovernanceClient.java
public DatasetQualityClient datasetQuality() {
    return new DatasetQualityClient(webClient, baseUrl + "/api/dataset-quality");
}
```

**Ver:** `GUIA_IMPLEMENTACION_MICROSERVICIOS_PYTHON.md` - Sección "Cliente Java" para detalles completos de implementación.

---

## 📝 CHECKLIST DE IMPLEMENTACIÓN

### Dataset Quality Evaluation (8015)

- [ ] Crear proyecto FastAPI
- [ ] Implementar evaluación de Completitud
- [ ] Implementar evaluación de Precisión
- [ ] Implementar evaluación de Consistencia
- [ ] Implementar evaluación de Validez
- [ ] Implementar evaluación de Puntualidad
- [ ] Implementar evaluación de Unicidad
- [ ] Implementar análisis de muestreo
- [ ] Agregar cliente Java
- [ ] Documentar endpoints
- [ ] Tests unitarios
- [ ] Tests de integración

### Dataset Privacy Detection (8016)

- [ ] Crear proyecto FastAPI
- [ ] Implementar detección de PII (Presidio)
- [ ] Implementar detección por tipo (EMAIL, PHONE, etc.)
- [ ] Implementar enmascaramiento de PII
- [ ] Agregar cliente Java
- [ ] Documentar endpoints
- [ ] Tests unitarios

### Dataset Standardization (8017)

- [ ] Crear proyecto FastAPI
- [ ] Implementar conversión CSV → Parquet
- [ ] Implementar conversión JSON → Parquet
- [ ] Implementar conversión Excel → Parquet
- [ ] Implementar validación de Parquet
- [ ] Agregar compresión Snappy
- [ ] Agregar cliente Java
- [ ] Documentar endpoints
- [ ] Tests unitarios

### Dataset Impact Analysis (8018)

- [ ] Crear proyecto FastAPI
- [ ] Implementar análisis de dependencias
- [ ] Implementar clasificación de impacto
- [ ] Agregar cliente Java
- [ ] Documentar endpoints
- [ ] Tests unitarios

### Bias Detection Extension (8007)

- [ ] Agregar endpoint `/api/bias/analyze-dataset-bias`
- [ ] Implementar análisis de sesgos en datasets
- [ ] Actualizar cliente Java
- [ ] Documentar endpoint
- [ ] Tests unitarios

---

## 🚀 IMPLEMENTACIÓN

### Guía de Implementación Completa

Para implementar estos microservicios, seguir la guía detallada:

**📖 `docs/prompts/governance/testing/GUIA_IMPLEMENTACION_MICROSERVICIOS_PYTHON.md`**

Esta guía incluye:
- Arquitectura general y patrones
- Estructura de proyecto estándar
- Implementación paso a paso
- Integración con cliente Java
- Despliegue y configuración
- Testing y validación
- Mejores prácticas

### Orden de Implementación Recomendado

1. **Dataset Quality Evaluation (8015)** - Crítico, base para otros análisis
2. **Dataset Standardization (8017)** - Necesario para procesar datasets
3. **Dataset Privacy Detection (8016)** - Importante para GDPR
4. **Dataset Impact Analysis (8018)** - Útil pero menos crítico
5. **Extensión Bias Detection (8007)** - Agregar funcionalidad a existente

### Checklist de Implementación

Para cada microservicio, seguir el checklist de `GUIA_IMPLEMENTACION_MICROSERVICIOS_PYTHON.md`:

- [ ] Crear estructura de proyecto
- [ ] Implementar endpoints FastAPI
- [ ] Agregar modelos Pydantic
- [ ] Implementar lógica de negocio
- [ ] Agregar cliente Java
- [ ] Configurar en API Gateway
- [ ] Tests unitarios
- [ ] Tests de integración
- [ ] Documentación Swagger
- [ ] Despliegue en K8s

---

**Última Actualización:** Enero 2025
**Versión:** 1.1
**Estado:** ✅ Guía de Referencia - Ver `GUIA_IMPLEMENTACION_MICROSERVICIOS_PYTHON.md` para implementación
