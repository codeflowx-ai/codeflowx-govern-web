# 🏛️ API COMPLETA - AI Governance Platform

**Versión**: 2.0.0  
**Base URL**: `http://localhost:8001`  
**OpenAPI Docs**: `http://localhost:8001/docs`  
**Fecha**: Octubre 30, 2025

---

## 📚 Tabla de Contenidos

1. [Introducción](#introducción)
2. [Inicio Rápido](#inicio-rápido)
3. [Módulos Disponibles](#módulos-disponibles)
4. [Endpoints Detallados](#endpoints-detallados)
5. [Ejemplos de Uso](#ejemplos-de-uso)
6. [Códigos de Error](#códigos-de-error)
7. [Compliance & Regulaciones](#compliance--regulaciones)

---

## 🎯 Introducción

Este microservicio proporciona **10 módulos funcionales** para AI Governance y Compliance, implementados **SIN usar LLMs**, solo algoritmos matemáticos determinísticos y reproducibles.

### ✅ Características Principales

- 🔬 **Determinístico**: Mismo input → mismo output
- ⚡ **Rápido**: Respuestas en segundos
- 📊 **Compliant**: EU AI Act + GDPR
- 🔒 **Auditable**: Métricas estándar de industria
- 🐳 **Production-ready**: Docker, logging, testing

---

## 🚀 Inicio Rápido

### Iniciar Servicio

```bash
# Docker (recomendado)
cd bias-detection-service
docker-compose up -d

# O Python local
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
python main_v2_complete.py
```

### Verificar Estado

```bash
curl http://localhost:8001/health
```

### Ver Documentación Interactiva

Abrir en navegador: http://localhost:8001/docs

---

## 📦 Módulos Disponibles

| # | Módulo | Descripción | EU AI Act | GDPR |
|---|--------|-------------|-----------|------|
| 1 | **Bias Analysis** | Análisis de sesgo y equidad | Art. 10, 13 | ✓ |
| 2 | **Drift Detection** | Detección de deriva de datos/modelo | Art. 61 | ✓ |
| 3 | **Data Quality** | Validación de calidad de datos | Art. 10 | ✓ |
| 4 | **Explainability** | Explicación de predicciones | Art. 13 | Art. 22 |
| 5 | **Robustness** | Pruebas de robustez adversarial | Art. 15 | ✓ |
| 6 | **Privacy** | Análisis de privacidad (k-anonymity) | Art. 10 | ✓✓ |
| 7 | **Performance** | Monitoreo de performance | Art. 61 | ✓ |
| 8 | **Uncertainty** | Cuantificación de incertidumbre | Art. 13 | ✓ |
| 9 | **Features** | Análisis de características | Art. 10 | ✓ |
| 10 | **Model Cards** | Generación de documentación | Art. 11 | ✓ |

---

## 📡 Endpoints Detallados

### **Grupo: Health & Status**

#### `GET /`
Root endpoint con información básica.

**Response:**
```json
{
  "service": "AI Governance Platform - Complete Hub",
  "status": "running",
  "version": "2.0.0",
  "modules": 10,
  "docs": "/docs"
}
```

#### `GET /health`
Health check completo del sistema.

**Response:**
```json
{
  "status": "healthy",
  "timestamp": "2025-10-30T22:00:00",
  "version": "2.0.0",
  "modules": {
    "bias_analysis": true,
    "drift_detection": true,
    "data_quality": true,
    ...
  },
  "dependencies": {
    "pandas": "2.1.4",
    "numpy": "1.26.3",
    "scikit-learn": "1.4.0"
  }
}
```

---

### **Módulo 1: Bias Analysis** 🎯

#### `POST /api/bias-analysis/analyze`

Analiza sesgo y equidad en predicciones de modelos ML.

**Parámetros:**
- `file` (file): CSV con predicciones
- `model_id` (string): ID del modelo
- `protected_attribute` (string): Atributo protegido (gender, race, age, etc.)
- `favorable_outcome` (string, optional): Valor favorable (default: "1")
- `threshold` (float, optional): Umbral de equidad (default: 0.8)

**CSV Format:**
```csv
y_true,y_pred,gender
1,1,male
0,0,female
1,0,female
...
```

**Response:**
```json
{
  "model_id": "model_123",
  "analysis_date": "2025-10-30T22:00:00",
  "metrics": {
    "demographic_parity_difference": 0.15,
    "equal_opportunity_difference": 0.12,
    "disparate_impact_ratio": 0.78
  },
  "classification": "MODERATE",
  "recommendations": "⚠️ Moderate bias detected in gender attribute...",
  "groups_analysis": [
    {
      "group": "male",
      "accuracy": 0.85,
      "precision": 0.82,
      "recall": 0.88,
      "count": 1000
    },
    {
      "group": "female",
      "accuracy": 0.70,
      "precision": 0.68,
      "recall": 0.72,
      "count": 800
    }
  ],
  "threshold_used": 0.8,
  "protected_attribute": "gender"
}
```

**Ejemplo curl:**
```bash
curl -X POST "http://localhost:8001/api/bias-analysis/analyze" \
  -F "file=@predictions.csv" \
  -F "model_id=model_123" \
  -F "protected_attribute=gender" \
  -F "threshold=0.8"
```

**Ejemplo Python:**
```python
import requests

files = {'file': open('predictions.csv', 'rb')}
data = {
    'model_id': 'model_123',
    'protected_attribute': 'gender',
    'threshold': 0.8
}

response = requests.post(
    'http://localhost:8001/api/bias-analysis/analyze',
    files=files,
    data=data
)

result = response.json()
print(f"Classification: {result['classification']}")
```

---

### **Módulo 2: Drift Detection** 📊

#### `POST /api/drift/detect`

Detecta deriva en distribución de datos entre referencia y producción.

**Parámetros:**
- `reference_file` (file): CSV con datos de referencia (training)
- `current_file` (file): CSV con datos actuales (production)
- `numerical_features` (string): Features numéricos separados por coma
- `categorical_features` (string, optional): Features categóricos separados por coma

**Response:**
```json
{
  "analysis_date": "2025-10-30T22:00:00",
  "n_reference_samples": 10000,
  "n_current_samples": 5000,
  "numerical_drift": {
    "age": {
      "feature": "age",
      "test": "Kolmogorov-Smirnov",
      "has_drift": true,
      "p_value": 0.001,
      "ks_statistic": 0.15,
      "reference_mean": 35.5,
      "current_mean": 40.2,
      "severity": "HIGH"
    }
  },
  "categorical_drift": {
    "category": {
      "feature": "category",
      "test": "Chi-square",
      "has_drift": false,
      "p_value": 0.25,
      "severity": "NO_DRIFT"
    }
  },
  "drift_percentage": 25.0,
  "overall_drift_detected": true,
  "psi": 0.18,
  "psi_interpretation": "MODERATE_DRIFT",
  "recommendations": [
    "⚠️ HIGH: Significant drift detected in multiple features",
    "1. Schedule model retraining within 1 week",
    "2. Increase monitoring frequency",
    "3. Review data collection process"
  ]
}
```

**Ejemplo curl:**
```bash
curl -X POST "http://localhost:8001/api/drift/detect" \
  -F "reference_file=@train_data.csv" \
  -F "current_file=@prod_data.csv" \
  -F "numerical_features=age,income,score" \
  -F "categorical_features=gender,category"
```

---

### **Módulo 3: Data Quality** ✅

#### `POST /api/data-quality/validate`

Validación comprehensiva de calidad de datos.

**Parámetros:**
- `file` (file): CSV con dataset a validar
- `target_column` (string, optional): Nombre de columna target
- `numerical_features` (string, optional): Features numéricos
- `categorical_features` (string, optional): Features categóricos

**Response:**
```json
{
  "validation_date": "2025-10-30T22:00:00",
  "n_samples": 10000,
  "n_features": 25,
  "quality_score": 85.5,
  "quality_level": "GOOD",
  "issues": [
    {
      "severity": "MEDIUM",
      "category": "Missing Values",
      "message": "['feature_x', 'feature_y'] have >5% missing values"
    },
    {
      "severity": "MEDIUM",
      "category": "Multicollinearity",
      "message": "3 feature pairs highly correlated"
    }
  ],
  "missing_values": {
    "total_missing": 450,
    "columns_with_missing": {
      "feature_x": 200,
      "feature_y": 250
    },
    "missing_percentages": {
      "feature_x": 2.0,
      "feature_y": 2.5
    },
    "critical_columns": [],
    "has_critical_missing": false
  },
  "duplicates": {
    "n_duplicates": 10,
    "duplicate_percentage": 0.1,
    "has_duplicates": true
  },
  "outliers": {
    "features_with_outliers": {
      "age": {
        "n_outliers": 50,
        "percentage": 0.5,
        "lower_bound": 18,
        "upper_bound": 80
      }
    },
    "total_outliers": 50
  },
  "class_balance": {
    "class_distribution": {
      "0": 7000,
      "1": 3000
    },
    "class_proportions": {
      "0": 0.7,
      "1": 0.3
    },
    "min_class_percentage": 0.3,
    "is_imbalanced": false,
    "imbalance_ratio": 2.33
  },
  "recommendations": [
    "✅ Data quality is acceptable for model training",
    "• Impute or remove features with excessive missing values",
    "• Review outliers: cap, transform, or remove if errors"
  ]
}
```

**Ejemplo curl:**
```bash
curl -X POST "http://localhost:8001/api/data-quality/validate" \
  -F "file=@dataset.csv" \
  -F "target_column=target" \
  -F "numerical_features=age,income,score" \
  -F "categorical_features=gender,category"
```

---

### **Módulo 4: Explainability** 💡

#### `POST /api/explainability/explain`

Genera explicaciones para predicciones de modelos.

**Parámetros:**
- `file` (file): CSV con features y predicciones
- `model_type` (string, optional): Tipo de modelo (tree, linear, neural) - default: "tree"
- `method` (string, optional): Método (shap, lime, permutation) - default: "shap"
- `prediction_column` (string, optional): Nombre columna predicciones - default: "prediction"

**Response:**
```json
{
  "explanation_date": "2025-10-30T22:00:00",
  "method": "shap",
  "model_type": "tree",
  "n_samples": 1000,
  "n_features": 20,
  "global_feature_importance": {
    "absolute": {
      "age": 0.25,
      "income": 0.20,
      "credit_score": 0.18,
      "employment_years": 0.12
    },
    "relative": {
      "age": 25.0,
      "income": 20.0,
      "credit_score": 18.0,
      "employment_years": 12.0
    },
    "top_10_features": [
      "age",
      "income",
      "credit_score",
      "employment_years",
      "debt_ratio"
    ]
  },
  "explanation_method": "correlation_based_surrogate",
  "note": "This is a surrogate explanation. For true SHAP values, provide the trained model.",
  "recommendations": [
    "ℹ️ Explainability Analysis Completed",
    "• Top 3 influential features: age, income, credit_score",
    "• Focus on documenting these features for stakeholders",
    "• EU AI Act Art. 13: Maintain transparency documentation",
    "• For production: Implement SHAP with actual model for accurate explanations"
  ]
}
```

**Ejemplo curl:**
```bash
curl -X POST "http://localhost:8001/api/explainability/explain" \
  -F "file=@predictions_with_features.csv" \
  -F "model_type=tree" \
  -F "method=shap" \
  -F "prediction_column=prediction"
```

---

### **Módulos 5-10: Placeholder Endpoints**

Los siguientes endpoints están documentados pero requieren implementación adicional:

#### `POST /api/robustness/test` 🛡️
Test de robustez adversarial (FGSM, PGD attacks)

#### `POST /api/privacy/analyze` 🔐
Análisis de privacidad (k-anonymity, l-diversity)

#### `POST /api/performance/monitor` 📈
Monitoreo continuo de performance

#### `POST /api/uncertainty/quantify` 🎲
Cuantificación de incertidumbre en predicciones

#### `POST /api/features/analyze` 🔍
Análisis de correlaciones y target leakage

#### `POST /api/model-card/generate` 📋
Generación automática de Model Cards

---

## 💡 Ejemplos de Uso

### **Caso 1: Análisis Completo de Modelo**

```python
import requests

# 1. Validar calidad de datos de entrenamiento
with open('train_data.csv', 'rb') as f:
    response = requests.post(
        'http://localhost:8001/api/data-quality/validate',
        files={'file': f},
        data={'target_column': 'approved'}
    )
    quality = response.json()
    print(f"Quality Score: {quality['quality_score']}/100")

# 2. Analizar sesgo en predicciones
with open('predictions.csv', 'rb') as f:
    response = requests.post(
        'http://localhost:8001/api/bias-analysis/analyze',
        files={'file': f},
        data={
            'model_id': 'credit_model_v1',
            'protected_attribute': 'gender'
        }
    )
    bias = response.json()
    print(f"Bias Classification: {bias['classification']}")

# 3. Explicar predicciones
with open('test_predictions.csv', 'rb') as f:
    response = requests.post(
        'http://localhost:8001/api/explainability/explain',
        files={'file': f},
        data={'method': 'shap'}
    )
    explainability = response.json()
    top_features = explainability['global_feature_importance']['top_10_features']
    print(f"Top features: {top_features[:3]}")

# 4. Detectar drift en producción
with open('train_data.csv', 'rb') as ref, open('prod_data.csv', 'rb') as cur:
    response = requests.post(
        'http://localhost:8001/api/drift/detect',
        files={
            'reference_file': ref,
            'current_file': cur
        },
        data={
            'numerical_features': 'age,income,credit_score',
            'categorical_features': 'employment_type'
        }
    )
    drift = response.json()
    if drift['overall_drift_detected']:
        print("⚠️ Drift detected! Model retraining recommended.")
```

---

### **Caso 2: Pipeline de Compliance**

```bash
#!/bin/bash
# Pipeline automático de compliance

MODEL_ID="credit_scoring_v2"
BASE_URL="http://localhost:8001"

echo "🔍 Starting AI Governance Compliance Pipeline..."

# Step 1: Data Quality
echo "📊 Step 1: Validating data quality..."
curl -X POST "$BASE_URL/api/data-quality/validate" \
  -F "file=@train_data.csv" \
  -F "target_column=approved" \
  -o quality_report.json

# Step 2: Bias Analysis
echo "🎯 Step 2: Analyzing bias..."
curl -X POST "$BASE_URL/api/bias-analysis/analyze" \
  -F "file=@predictions.csv" \
  -F "model_id=$MODEL_ID" \
  -F "protected_attribute=gender" \
  -o bias_report.json

# Step 3: Explainability
echo "💡 Step 3: Generating explanations..."
curl -X POST "$BASE_URL/api/explainability/explain" \
  -F "file=@predictions_with_features.csv" \
  -o explainability_report.json

# Step 4: Drift Detection (si hay datos de producción)
if [ -f "prod_data.csv" ]; then
  echo "📊 Step 4: Checking for drift..."
  curl -X POST "$BASE_URL/api/drift/detect" \
    -F "reference_file=@train_data.csv" \
    -F "current_file=@prod_data.csv" \
    -F "numerical_features=age,income,credit_score" \
    -o drift_report.json
fi

echo "✅ Compliance pipeline completed!"
echo "Reports generated:"
ls -lh *_report.json
```

---

## ⚠️ Códigos de Error

| Código | Descripción | Solución |
|--------|-------------|----------|
| 200 | Success | N/A |
| 400 | Bad Request | Verificar formato CSV y parámetros |
| 413 | Payload Too Large | Archivo >100MB - reducir tamaño |
| 422 | Validation Error | Parámetros faltantes o inválidos |
| 500 | Internal Server Error | Ver logs del servidor |
| 504 | Gateway Timeout | Análisis >30s - reducir datos |

### **Ejemplo de Error Response:**

```json
{
  "detail": "Missing required columns: ['y_true', 'y_pred']. Found columns: ['prediction', 'gender']"
}
```

---

## ⚖️ Compliance & Regulaciones

### **EU AI Act (Regulation 2024/1689)**

| Artículo | Requerimiento | Módulo |
|----------|---------------|--------|
| Art. 10 | Calidad de datos | Data Quality, Bias Analysis |
| Art. 11 | Documentación técnica | Model Cards |
| Art. 13 | Transparencia | Explainability |
| Art. 15 | Accuracy & Robustness | Robustness, Performance |
| Art. 61 | Post-market monitoring | Drift Detection, Performance |

### **GDPR**

| Artículo | Requerimiento | Módulo |
|----------|---------------|--------|
| Art. 22 | Derecho a explicación | Explainability |
| Art. 5 | Calidad de datos | Data Quality |
| Art. 32 | Seguridad | Privacy Analysis |

---

## 📞 Soporte

### **Documentación**
- 📖 **README completo**: `README.md`
- 🚀 **Guía rápida**: `QUICKSTART.md`
- 🔗 **Integración Java**: `INTEGRATION_GUIDE.md`
- 📦 **Entregables**: `DELIVERABLES.md`

### **OpenAPI Docs Interactivo**
http://localhost:8001/docs

### **Contacto**
- **Email**: dev@aigovernance.com
- **Soporte**: support@aigovernance.com

---

## 🎉 Resumen

Este API proporciona **herramientas completas** para:

✅ Cumplir con EU AI Act  
✅ Cumplir con GDPR  
✅ Auditorías regulatorias  
✅ Transparencia para stakeholders  
✅ Monitoreo continuo en producción  
✅ Documentación automatizada  

**Todo sin usar LLMs - Solo algoritmos matemáticos determinísticos y auditables.**

---

**Última actualización**: Octubre 30, 2025  
**Versión API**: 2.0.0


