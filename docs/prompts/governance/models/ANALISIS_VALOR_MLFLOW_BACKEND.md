# Análisis: ¿Qué aporta realmente MLflow como Backend?

**Fecha:** Enero 2025

---

## 🤔 La Pregunta Clave

En el contexto de CodeflowX Governance, ¿qué valor real aporta usar MLflow como backend vs crear nuestro propio sistema?

---

## 📊 Funcionalidades de MLflow

### 1. **Tracking de Métricas con Historial Temporal**

**Qué hace MLflow:**
- Guarda métricas con timestamps y steps
- Permite tracking durante procesos largos (entrenamiento, evaluación iterativa)
- Ejemplo: `log_metric("accuracy", 0.95, step=1)`, `log_metric("accuracy", 0.96, step=2)`

**Nuestro contexto:**
- ❌ Los análisis de bias/explainability son **ejecuciones puntuales**, no procesos iterativos
- ✅ Ya tenemos las métricas finales en las entidades (`ModelBiasAnalysis.modfairnessscore`, etc.)
- ❌ **No necesitamos historial temporal** para estos casos

**Conclusión:** ⚠️ **Valor limitado** - No ejecutamos entrenamientos iterativos

---

### 2. **Almacenamiento de Artefactos**

**Qué hace MLflow:**
- Guarda archivos (modelos, visualizaciones, datasets)
- Usa sistema de almacenamiento backend (S3, local filesystem, etc.)
- Mantiene referencias en base de datos

**Nuestro contexto:**
- ✅ Podríamos guardar artefactos directamente en **S3**
- ✅ Guardar referencia (path) en PostgreSQL como JSONB
- ✅ **No necesitamos MLflow** para esto

**Ejemplo:**
```java
// Guardar en S3
String artifactPath = s3Service.uploadFile("runs/123/bias_report.pdf", file);

// Guardar referencia en PostgreSQL
run.setModrunartifacts("[{\"path\": \"" + artifactPath + "\", \"type\": \"report\"}]");
```

**Conclusión:** ❌ **No aporta valor** - Podemos usar S3 directamente

---

### 3. **Parámetros de Configuración**

**Qué hace MLflow:**
- Guarda parámetros de configuración (hyperparameters, configs)
- Ejemplo: `log_param("learning_rate", 0.001)`

**Nuestro contexto:**
- ✅ **Ya tenemos los parámetros** en nuestras entidades:
  - `ModelBiasAnalysis.modprotectedattribute`, `modthreshold`, etc.
  - `ModelExplainability.modexplainabilitymethod`, etc.
- ✅ Podríamos guardar parámetros adicionales en JSONB si necesitamos

**Conclusión:** ❌ **Redundante** - Ya tenemos los datos

---

### 4. **Experiments y Runs (Organización)**

**Qué hace MLflow:**
- Organiza runs en experiments
- Jerarquía: Experiment → Runs
- Tags y metadata

**Nuestro contexto:**
- ✅ Podríamos crear nuestras propias entidades `ModelExperiment` y `ModelRun`
- ✅ **PostgreSQL es perfecto** para esto (relaciones, queries, etc.)
- ✅ Más simple que sincronizar con MLflow

**Conclusión:** ❌ **No aporta valor** - PostgreSQL es suficiente

---

### 5. **Comparación de Runs**

**Qué hace MLflow:**
- API para comparar múltiples runs
- Compara métricas, parámetros

**Nuestro contexto:**
- ✅ Podríamos hacer queries SQL simples:
  ```sql
  SELECT run1.modrunname, run1.metrics, run2.modrunname, run2.metrics
  FROM mod_run run1, mod_run run2
  WHERE run1.idxexperiment = run2.idxexperiment
  ```
- ✅ O crear endpoint que combine datos de múltiples runs

**Conclusión:** ❌ **Fácil de implementar nosotros** - No necesitamos MLflow

---

### 6. **API REST Completa**

**Qué hace MLflow:**
- API REST para crear/consultar runs, experiments, métricas
- Endpoints estándar

**Nuestro contexto:**
- ✅ **Ya tenemos Spring Boot** con REST controllers
- ✅ Podemos crear nuestros propios endpoints
- ✅ Más control sobre la API (filtros de governance, permisos, etc.)

**Conclusión:** ❌ **No aporta valor** - Spring Boot es suficiente

---

## 📋 Resumen: ¿Qué aporta realmente MLflow?

| Funcionalidad | Valor en MLflow | Necesidad en CodeflowX | Conclusión |
|--------------|----------------|------------------------|------------|
| Tracking métricas temporal | ✅ Alto | ❌ No necesitamos | ⚠️ No aplica |
| Artefactos | ✅ Medio | ✅ Sí, pero S3 directo | ❌ S3 es mejor |
| Parámetros | ✅ Medio | ✅ Ya los tenemos | ❌ Redundante |
| Experiments/Runs | ✅ Alto | ✅ Sí, pero PostgreSQL | ❌ PostgreSQL es mejor |
| Comparación runs | ✅ Medio | ✅ Sí, pero SQL simple | ❌ Fácil nosotros |
| API REST | ✅ Alto | ✅ Ya tenemos Spring Boot | ❌ Redundante |

---

## ✅ Conclusión: **MLflow NO aporta valor real**

### Razones:

1. **Contexto diferente:**
   - MLflow está diseñado para **training iterativo** (tracking durante entrenamiento)
   - Nosotros necesitamos **governance de análisis puntuales** (bias, explainability)

2. **Ya tenemos todo lo necesario:**
   - ✅ PostgreSQL para almacenar datos
   - ✅ S3 para artefactos
   - ✅ Spring Boot para API
   - ✅ Entidades JPA para organización

3. **Complejidad sin beneficio:**
   - ❌ Sincronización bidireccional MLflow ↔ CodeflowX
   - ❌ Mantener dos sistemas de datos
   - ❌ Mapeo de IDs (CodeflowX ID ↔ MLflow ID)
   - ❌ Posibles inconsistencias

---

## 🎯 Recomendación Final: **Sistema Propio Simple**

### Arquitectura Propuesta:

```
┌─────────────────────────────────────────┐
│         CodeflowX Frontend              │
│    (UI personalizada integrada)         │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│      CodeflowX Backend (Spring Boot)    │
│  - Endpoints REST                        │
│  - Business Logic                        │
│  - Servicios de análisis                 │
└─────────────────┬───────────────────────┘
                  │
        ┌─────────┴─────────┐
        │                   │
┌───────▼────────┐  ┌───────▼────────┐
│  PostgreSQL    │  │   S3 Storage   │
│  - Experiments │  │  - Artefactos  │
│  - Runs        │  │  - Reportes    │
│  - Análisis    │  │  - Visualiz.   │
│  - Métricas    │  │                 │
└────────────────┘  └─────────────────┘
```

### Entidades Necesarias:

```java
@Entity
@Table(name = "mod_experiment")
public class ModelExperiment {
    // Campos básicos
    // Relación con Model (opcional)
    // Tags como JSONB
}

@Entity
@Table(name = "mod_run")
public class ModelRun {
    // Campos básicos
    // Relación con ModelExperiment
    // Relación con Model
    // Relación con ModelBiasAnalysis (opcional)
    // Relación con ModelExplainability (opcional)
    // Parámetros como JSONB
    // Métricas como JSONB (sin historial temporal)
    // Artefactos como JSONB (paths a S3)
    // Tags como JSONB
}
```

### Ventajas:

1. ✅ **Simple**: Una sola fuente de verdad (PostgreSQL)
2. ✅ **Control total**: Diseñado específicamente para governance
3. ✅ **Integración nativa**: Relaciones directas con análisis, workflows, aprobaciones
4. ✅ **Sin complejidad**: No sincronización, no mapeo de IDs
5. ✅ **Mantenible**: Menos dependencias externas
6. ✅ **Performance**: Queries directas a PostgreSQL (más rápido que API externa)

---

## 🔄 ¿Cuándo SÍ tendría sentido MLflow?

MLflow tendría sentido si:

1. **Ejecutáramos entrenamientos iterativos** desde CodeflowX
2. **Necesitáramos integración con MLflow externo** de otros equipos (pero ya tenemos sync de modelos)
3. **Quisiéramos reutilizar código de MLflow** (pero no tenemos)

En nuestro caso: **No aplica ninguno**.

---

## 📝 Implementación Recomendada

### Sistema Propio Simple:

1. **Entidades JPA** (ya propuestas en documento anterior)
2. **Repositorios** (JPA estándar)
3. **Servicios de negocio** (lógica simple)
4. **Endpoints REST** (Spring Boot estándar)
5. **S3 para artefactos** (servicio existente o nuevo)
6. **UI personalizada** (React/Next.js integrada)

### Esfuerzo Estimado:

- **Backend**: 2-3 semanas (más simple sin MLflow)
- **Frontend**: 2-3 semanas
- **Total**: ~1 mes vs ~1.5 meses con MLflow híbrido

---

## ✅ Conclusión Final

**MLflow NO aporta valor real en nuestro contexto de governance.**

**Recomendación:** Crear sistema propio simple con PostgreSQL + S3. Es más simple, más mantenible, y diseñado específicamente para nuestras necesidades de governance.

**El único valor de MLflow sería:** Si ya estuviéramos usando MLflow intensivamente para training, pero ese no es nuestro caso.
