# 🔄 REORGANIZACIÓN DE PANTALLAS - EVALUACIÓN Y EXPERIMENTOS

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Reorganización de pantallas de evaluación y experimentos sin referencias a MLflow

---

## 🎯 RESUMEN EJECUTIVO

Se han identificado **80 pantallas** relacionadas con evaluación y experimentos que necesitan ser reorganizadas e integradas en los módulos existentes de CodeflowX Govern, eliminando cualquier referencia a MLflow y consolidando la funcionalidad en una estructura coherente.

### **Objetivos de la Reorganización:**
- **Integrar** 80 pantallas en módulos existentes
- **Eliminar** referencias a MLflow
- **Consolidar** funcionalidades de evaluación y experimentos
- **Mejorar** navegación y experiencia de usuario
- **Alinear** con estructura Next.js original

---

## 📊 ANÁLISIS DE PANTALLAS ACTUALES

### **Distribución Actual (80 pantallas):**

#### **Console/Platform/Evaluation (23 pantallas):**
- `bias-analysis-detail.zul` / `bias-analysis-overview.zul` - Análisis de sesgos
- `bias-detection-detail.zul` / `bias-detection-overview.zul` - Detección de sesgos
- `bias-recommendation-detail.zul` / `bias-recommendation-overview.zul` - Recomendaciones de sesgos
- `evaluation-metric-detail.zul` / `evaluation-metric-overview.zul` - Métricas de evaluación
- `evaluation-metrics-overview.zul` - Vista general de métricas
- `evaluation-summary-overview.zul` - Resumen de evaluaciones
- `fairness-metric-detail.zul` / `fairness-metric-overview.zul` - Métricas de fairness
- `model-bias-analysis-detail.zul` / `model-bias-analysis-overview.zul` - Análisis de sesgos de modelos
- `model-evaluation-detail.zul` / `model-evaluation-overview.zul` - Evaluación de modelos
- `model-performance-detail.zul` / `model-performance-overview.zul` - Rendimiento de modelos

#### **Console/Platform/Training (57 pantallas):**
- `checkpoint-detail.zul` / `checkpoint-overview.zul` - Checkpoints de entrenamiento
- `checkpoint-history-overview.zul` - Historial de checkpoints
- `dataset-source-detail.zul` / `dataset-source-overview.zul` - Fuentes de datasets
- `environment-detail.zul` / `environment-overview.zul` - Entornos de entrenamiento
- `experiment-comparison-matrix-overview.zul` - Matriz de comparación de experimentos
- `experiment-detail.zul` / `experiment-overview.zul` - Experimentos
- `experiment-leaderboard-overview.zul` - Leaderboard de experimentos
- `experiment-lineage-detail.zul` / `experiment-lineage-overview.zul` - Lineage de experimentos
- `experiment-template-detail.zul` / `experiment-template-overview.zul` - Templates de experimentos
- `hpo-experiment-detail.zul` / `hpo-experiment-overview.zul` - Experimentos HPO
- `hpo-progress-dashboard-overview.zul` - Dashboard de progreso HPO
- `hpo-trial-detail.zul` / `hpo-trial-overview.zul` - Trials HPO
- `metric-series-detail.zul` / `metric-series-overview.zul` - Series de métricas
- `metric-series-visualization-overview.zul` - Visualización de series de métricas
- `metric-stream-detail.zul` / `metric-stream-overview.zul` - Streams de métricas
- `param-detail.zul` / `param-overview.zul` - Parámetros
- `run-comparison-detail.zul` / `run-comparison-overview.zul` - Comparación de runs
- `run-detail.zul` / `run-overview.zul` - Runs de experimentos
- `tag-detail.zul` / `tag-overview.zul` - Tags
- `training-alert-detail.zul` / `training-alert-overview.zul` - Alertas de entrenamiento
- `training-alerts-summary-overview.zul` - Resumen de alertas
- `training-artifact-detail.zul` / `training-artifact-overview.zul` - Artefactos de entrenamiento
- `training-cost-analysis-overview.zul` - Análisis de costos
- `training-execution-detail.zul` / `training-execution-overview.zul` - Ejecución de entrenamiento
- `training-governance-detail.zul` / `training-governance-overview.zul` - Governance de entrenamiento
- `training-infrastructure-detail.zul` / `training-infrastructure-overview.zul` - Infraestructura
- `training-log-detail.zul` / `training-log-overview.zul` - Logs de entrenamiento
- `training-metric-detail.zul` / `training-metric-overview.zul` - Métricas de entrenamiento
- `training-metrics-summary-overview.zul` - Resumen de métricas
- `training-overview-overview.zul` - Vista general de entrenamiento
- `training-resource-utilization-overview.zul` - Utilización de recursos

#### **Console/Gobierno/Training (4 pantallas):**
- `experiments-detail.zul` - Detalle de experimentos
- `training-dashboard.zul` - Dashboard de entrenamiento
- `training-detail.zul` / `training-overview.zul` - Entrenamiento

#### **Console/Platform/Governance (3 pantallas):**
- `analytics/evaluation-trends.zul` - Tendencias de evaluación
- `policies/evaluation-overview.zul` - Evaluación de políticas
- `policies/evaluation.zul` - Evaluación

#### **Console/BPMN (3 pantallas):**
- `llm-evaluation-review-form.zul` - Revisión de evaluación LLM
- `model-evaluation-review-form.zul` - Revisión de evaluación de modelos
- `rag-evaluation-review-form.zul` - Revisión de evaluación RAG

---

## 🔄 PROPUESTA DE REORGANIZACIÓN

### **1. MÓDULO MODELS - Evaluación de Modelos**

#### **Integrar en Models (23 pantallas):**
```
models/evaluation/
├── bias-analysis/          # Análisis de sesgos
│   ├── detail.zul
│   └── overview.zul
├── bias-detection/         # Detección de sesgos
│   ├── detail.zul
│   └── overview.zul
├── bias-recommendations/   # Recomendaciones de sesgos
│   ├── detail.zul
│   └── overview.zul
├── fairness-metrics/       # Métricas de fairness
│   ├── detail.zul
│   └── overview.zul
├── model-evaluation/       # Evaluación de modelos
│   ├── detail.zul
│   └── overview.zul
├── model-performance/      # Rendimiento de modelos
│   ├── detail.zul
│   └── overview.zul
├── evaluation-metrics/     # Métricas de evaluación
│   ├── detail.zul
│   ├── overview.zul
│   └── summary.zul
└── evaluation-summary/     # Resumen de evaluaciones
    └── overview.zul
```

### **2. MÓDULO TRAINING - Experimentos y Entrenamiento**

#### **Integrar en Training (57 pantallas):**
```
training/
├── experiments/            # Experimentos
│   ├── detail.zul
│   ├── overview.zul
│   ├── comparison-matrix.zul
│   ├── leaderboard.zul
│   ├── lineage/
│   │   ├── detail.zul
│   │   └── overview.zul
│   └── templates/
│       ├── detail.zul
│       └── overview.zul
├── hpo/                   # Hyperparameter Optimization
│   ├── experiments/
│   │   ├── detail.zul
│   │   └── overview.zul
│   ├── trials/
│   │   ├── detail.zul
│   │   └── overview.zul
│   └── progress-dashboard.zul
├── runs/                  # Runs de experimentos
│   ├── detail.zul
│   ├── overview.zul
│   └── comparison/
│       ├── detail.zul
│       └── overview.zul
├── metrics/               # Métricas
│   ├── detail.zul
│   ├── overview.zul
│   ├── series/
│   │   ├── detail.zul
│   │   ├── overview.zul
│   │   └── visualization.zul
│   ├── streams/
│   │   ├── detail.zul
│   │   └── overview.zul
│   └── summary.zul
├── artifacts/             # Artefactos
│   ├── detail.zul
│   └── overview.zul
├── checkpoints/           # Checkpoints
│   ├── detail.zul
│   ├── overview.zul
│   └── history.zul
├── parameters/            # Parámetros
│   ├── detail.zul
│   └── overview.zul
├── tags/                  # Tags
│   ├── detail.zul
│   └── overview.zul
├── environments/          # Entornos
│   ├── detail.zul
│   └── overview.zul
├── datasets/              # Fuentes de datos
│   ├── detail.zul
│   └── overview.zul
├── execution/             # Ejecución
│   ├── detail.zul
│   └── overview.zul
├── logs/                  # Logs
│   ├── detail.zul
│   └── overview.zul
├── alerts/                # Alertas
│   ├── detail.zul
│   ├── overview.zul
│   └── summary.zul
├── governance/            # Governance
│   ├── detail.zul
│   └── overview.zul
├── infrastructure/        # Infraestructura
│   ├── detail.zul
│   └── overview.zul
├── cost-analysis/         # Análisis de costos
│   └── overview.zul
└── resource-utilization/  # Utilización de recursos
    └── overview.zul
```

### **3. MÓDULO GOVERNANCE - Evaluación de Políticas**

#### **Integrar en Governance (3 pantallas):**
```
governance/
├── analytics/
│   └── evaluation-trends.zul
└── policies/
    ├── evaluation.zul
    └── evaluation-overview.zul
```

### **4. MÓDULO BPMN - Formularios de Evaluación**

#### **Mantener en BPMN (3 pantallas):**
```
bpmn/
├── llm-evaluation-review-form.zul
├── model-evaluation-review-form.zul
└── rag-evaluation-review-form.zul
```

---

## 📋 MAPEO DE FUNCIONALIDADES

### **Funcionalidades MLflow Equivalentes:**

| Funcionalidad MLflow | Pantalla CodeflowX | Módulo Destino |
|---------------------|-------------------|----------------|
| **Experiment Tracking** | `experiment-detail.zul` | Training |
| **Run Comparison** | `run-comparison-detail.zul` | Training |
| **Metrics Tracking** | `training-metric-detail.zul` | Training |
| **Parameter Logging** | `param-detail.zul` | Training |
| **Artifact Storage** | `training-artifact-detail.zul` | Training |
| **Model Registry** | `model-evaluation-detail.zul` | Models |
| **Model Evaluation** | `model-performance-detail.zul` | Models |
| **Bias Detection** | `bias-detection-detail.zul` | Models |
| **Fairness Metrics** | `fairness-metric-detail.zul` | Models |

### **Funcionalidades Avanzadas CodeflowX:**

| Funcionalidad | Pantalla CodeflowX | Ventaja sobre MLflow |
|---------------|-------------------|---------------------|
| **HPO Integrado** | `hpo-experiment-detail.zul` | ✅ No disponible en MLflow |
| **Experiment Templates** | `experiment-template-detail.zul` | ✅ No disponible en MLflow |
| **Experiment Lineage** | `experiment-lineage-detail.zul` | ✅ No disponible en MLflow |
| **Bias Recommendations** | `bias-recommendation-detail.zul` | ✅ No disponible en MLflow |
| **Training Governance** | `training-governance-detail.zul` | ✅ No disponible en MLflow |
| **Cost Analysis** | `training-cost-analysis-overview.zul` | ✅ No disponible en MLflow |
| **Resource Utilization** | `training-resource-utilization-overview.zul` | ✅ No disponible en MLflow |

---

## 🎯 BENEFICIOS DE LA REORGANIZACIÓN

### **1. Integración Coherente**
- **Models:** Evaluación completa de modelos con sesgos y fairness
- **Training:** Experimentos avanzados con HPO y templates
- **Governance:** Evaluación de políticas y tendencias
- **BPMN:** Formularios de revisión automatizados

### **2. Eliminación de Referencias MLflow**
- **Sin dependencias** externas
- **Funcionalidad nativa** de CodeflowX
- **Integración completa** con el ecosistema

### **3. Mejora de Navegación**
- **Estructura lógica** por módulos
- **Funcionalidades agrupadas** por contexto
- **Experiencia de usuario** mejorada

### **4. Escalabilidad**
- **Módulos independientes** pero integrados
- **Funcionalidades extensibles** por módulo
- **Arquitectura** preparada para crecimiento

---

## 🚀 PLAN DE IMPLEMENTACIÓN

### **Fase 1: Reorganización de Models**
1. Mover pantallas de evaluación a `models/evaluation/`
2. Reorganizar estructura de sesgos y fairness
3. Integrar con procesos BPMN existentes

### **Fase 2: Reorganización de Training**
1. Mover pantallas de experimentos a `training/experiments/`
2. Reorganizar HPO y templates
3. Integrar métricas y artefactos

### **Fase 3: Integración con Governance**
1. Mover pantallas de evaluación de políticas
2. Integrar tendencias y analytics
3. Conectar con procesos de governance

### **Fase 4: Optimización**
1. Eliminar referencias a MLflow
2. Optimizar navegación
3. Mejorar experiencia de usuario

---

## 🎯 CONCLUSIÓN

La reorganización propuesta **integra completamente** las 80 pantallas de evaluación y experimentos en los módulos existentes de CodeflowX Govern, eliminando cualquier dependencia conceptual de MLflow y consolidando la funcionalidad en una estructura coherente y escalable.

**Esta reorganización posiciona CodeflowX** como una plataforma integral de gobierno de IA que supera ampliamente las capacidades de MLflow, proporcionando funcionalidades avanzadas de evaluación, experimentación y governance empresarial.
