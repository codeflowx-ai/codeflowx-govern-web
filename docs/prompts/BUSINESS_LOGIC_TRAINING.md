# PROMPT DE LÓGICA DE NEGOCIO - MÓDULO TRAINING

**Fecha:** Diciembre 2025
**Módulo:** Training (Entrenamiento de Modelos)
**Objetivo:** Definir la lógica de negocio completa para el módulo de entrenamiento de modelos
**Esfuerzo Estimado:** 4-5 días

> **⚠️ IMPORTANTE:** Todas las llamadas a microservicios Python están **COMENTADAS** en este prompt.
> Las operaciones CRUD y consultas BBDD están implementadas, pero las integraciones con microservicios externos están pendientes de implementar cuando estén disponibles.

---

## 📋 CONTEXTO DEL MÓDULO

### **Descripción Funcional**
El módulo Training gestiona el ciclo de vida completo del entrenamiento de modelos de IA, incluyendo:
- Gestión de experimentos y runs
- Hyperparameter Optimization (HPO)
- Seguimiento de métricas y artefactos
- Gestión de datasets y versionado
- Control de costos y recursos
- Integración con frameworks ML (MLflow, Weights & Biases, etc.)

### **Pantallas Asociadas**

#### **ViewModels Identificados (54 ViewModels)**
**Referencia:** `suinsit.nova.web/docs/migration/prompts/MIGRACION_TRAINING_01.md`

**ViewModels Principales:**
- `ExperimentsDetailViewModel` - Detalles de experimentos
- `ExperimentsOverviewViewModel` - Vista general de experimentos
- `HPOTrialOverviewViewModel` - Vista general de trials HPO
- `RunDetailViewModel` - Detalles de runs
- `RunOverviewViewModel` - Vista general de runs
- `TrainingMetricDetailViewModel` - Detalles de métricas
- `TrainingArtifactDetailViewModel` - Detalles de artefactos
- `TrainingGovernanceDetailViewModel` - Governance de entrenamiento
- Y 46 ViewModels adicionales para gestión completa de training

#### **Pantallas ZUL Identificadas (52 pantallas)**
**Referencia:** `suinsit.nova.web/docs/funcional/training/01_REORGANIZACION_PANTALLAS_TRAINING.md`

- **Experiments (8):** `page.zul`, `overview.zul`, `comparison.zul`, `leaderboard.zul`, `lineage.zul`, `lineage-overview.zul`, `template.zul`, `template-overview.zul`
- **HPO (5):** `page.zul`, `overview.zul`, `dashboard.zul`, `trial.zul`, `trial-overview.zul`
- **Runs (4):** `page.zul`, `overview.zul`, `comparison.zul`, `comparison-overview.zul`
- **Metrics (7):** `page.zul`, `overview.zul`, `series.zul`, `series-overview.zul`, `visualization.zul`, `stream.zul`, `stream-overview.zul`, `summary.zul`
- **Artifacts (2):** `page.zul`, `overview.zul`
- Y 26 pantallas adicionales en otros módulos (checkpoints, logs, params, etc.)

#### **Pantallas Next.js Migradas (20+ pantallas)**
**Ubicación:** `app/(app)/training/`

**Pantallas Principales Migradas:**
- ✅ `app/(app)/training/experiments/page.tsx` - Experimentos principal
- ✅ `app/(app)/training/experiments/overview/page.tsx` - Vista general de experimentos
- ✅ `app/(app)/training/experiments/lineage-overview/page.tsx` - Lineage
- ✅ `app/(app)/training/experiments/template-overview/page.tsx` - Templates
- ✅ `app/(app)/training/hpo/overview/page.tsx` - HPO overview
- ✅ `app/(app)/training/hpo/dashboard/page.tsx` - Dashboard HPO
- ✅ `app/(app)/training/hpo/trial-overview/page.tsx` - Trials HPO
- ✅ `app/(app)/training/metrics/overview/page.tsx` - Métricas
- ✅ `app/(app)/training/metrics/series-overview/page.tsx` - Series de métricas
- ✅ `app/(app)/training/metrics/stream-overview/page.tsx` - Streams de métricas
- ✅ `app/(app)/training/artifacts/overview/page.tsx` - Artefactos
- ✅ `app/(app)/training/checkpoints/overview/page.tsx` - Checkpoints
- ✅ `app/(app)/training/execution/overview/page.tsx` - Ejecución
- ✅ `app/(app)/training/execution/log-overview/page.tsx` - Logs de ejecución
- ✅ `app/(app)/training/governance/dashboard/page.tsx` - Dashboard de governance
- ✅ `app/(app)/training/governance/overview/page.tsx` - Governance overview
- ✅ `app/(app)/training/governance/infrastructure-overview/page.tsx` - Infraestructura
- ✅ `app/(app)/training/infrastructure/dataset-overview/page.tsx` - Datasets
- ✅ `app/(app)/training/infrastructure/environment-overview/page.tsx` - Entornos
- ✅ `app/(app)/training/datasets/page.tsx` - Datasets principal

**Referencia:** `codeflowx-studio/docs/PLAN_MIGRACION_ZUL_VIEWMODELS.md`

### **Entidades JPA Principales**
- `Experiment` - Experimentos de entrenamiento
- `Run` - Ejecuciones de entrenamiento
- `Metric` - Métricas de entrenamiento
- `MetricSeries` - Series de métricas
- `Artifact` - Artefactos generados
- `Checkpoint` - Checkpoints de modelos
- `Parameter` - Parámetros de entrenamiento
- `TrainingExecution` - Ejecuciones de entrenamiento
- `TrainingCost` - Costos de entrenamiento
- `Environment` - Entornos de entrenamiento
- `Dataset` - Datasets de entrenamiento

---

## 🏗️ ARQUITECTURA Y DEPENDENCIAS

### **Business Services Existentes**
Ninguno identificado específicamente para Training

### **Servicios CRUD (codeflowx.govern.services)**
- `ExperimentService` - CRUD de experimentos
- `RunService` - CRUD de runs
- `MetricService` - CRUD de métricas
- `ArtifactService` - CRUD de artefactos

### **Microservicios Python Disponibles (COMENTADOS)**
- `codeflowx-training-service` - Servicio de entrenamiento
- `codeflowx-mlflow-adapter` - Adaptador MLflow
- `codeflowx-aios-telemetry` - Telemetría y métricas

---

## 💼 LÓGICA DE NEGOCIO - BUSINESS SERVICES

### **1. TrainingBusinessService**

#### **1.1. Operaciones CRUD Básicas**

**Método: `createExperiment(Experiment experiment, String createdBy)`**
```java
/**
 * Crea un nuevo experimento de entrenamiento
 *
 * Validaciones:
 * - Nombre único
 * - Proyecto asociado válido
 * - Framework válido
 *
 * Consulta BBDD:
 * SELECT COUNT(*) FROM trnexperiments WHERE trnexperimentname = ? AND idxproject = ?
 */
public Experiment createExperiment(Experiment experiment, String createdBy) {
    // 1. Validar nombre único por proyecto
    validateUniqueExperimentName(experiment.getTrnexperimentname(), experiment.getIdxproject());

    // 2. Validar proyecto
    if (experiment.getIdxproject() != null) {
        validateProject(experiment.getIdxproject());
    }

    // 3. Establecer valores por defecto
    experiment.setTrnexperimentstatus("DRAFT");
    experiment.setTrnexperimentcreatedby(createdBy);
    experiment.setTrnexperimentcreatedat(new Timestamp(System.currentTimeMillis()));

    // 4. Generar UUID si no existe
    if (experiment.getIduuid() == null) {
        experiment.setIduuid(UUID.randomUUID().toString());
    }

    // 5. Guardar
    return noCodeClient.save(experiment);
}
```

**Método: `createRun(Long experimentId, RunConfig config, String createdBy)`**
```java
/**
 * Crea un nuevo run de entrenamiento
 *
 * Validaciones:
 * - Experimento existe
 * - Configuración válida
 * - Recursos disponibles
 *
 * Consultas BBDD:
 * SELECT * FROM trnexperiments WHERE idxexperiment = ?
 * INSERT INTO trnruns (...)
 *
 * Llamada Microservicio Python (COMENTADA - PENDIENTE):
 * - codeflowx-training-service: POST /api/v1/runs/create
 */
public Run createRun(Long experimentId, RunConfig config, String createdBy) {
    Experiment experiment = noCodeClient.findById(Experiment.class, experimentId);
    if (experiment == null) {
        throw new EntityNotFoundException("Experimento no encontrado: " + experimentId);
    }

    Run run = new Run();
    run.setExperiment(experiment);
    run.setTrnrunnam(config.getRunName());
    run.setTrnrunstatus("SCHEDULED");
    run.setTrnrunconfig(config.toJson());
    run.setTrnruncreatedat(new Timestamp(System.currentTimeMillis()));
    run.setTrnruncreatedby(createdBy);

    // TODO: Llamar a microservicio Python para crear run
    // PENDIENTE: Implementar cuando microservicio esté disponible
    /*
    try {
        RunResponse response = trainingServiceClient.createRun(experimentId, config);
        run.setTrnrunrunid(response.getRunId()); // MLflow run ID
        run.setTrnrunstatus("RUNNING");
    } catch (Exception e) {
        log.error("Error creando run en servicio de entrenamiento", e);
        run.setTrnrunstatus("FAILED");
        run.setTrnrunerror(e.getMessage());
    }
    */

    return noCodeClient.save(run);
}
```

#### **1.2. Operaciones de Seguimiento**

**Método: `recordMetric(Long runId, String metricName, Double metricValue, Long step, String recordedBy)`**
```java
/**
 * Registra una métrica de entrenamiento
 *
 * Consultas BBDD:
 * SELECT * FROM trnruns WHERE idxrun = ?
 * INSERT INTO trnmetrics (...)
 *
 * Llamada Microservicio Python (COMENTADA - PENDIENTE):
 * - codeflowx-mlflow-adapter: POST /api/v1/metrics/log
 */
public Metric recordMetric(Long runId, String metricName, Double metricValue, Long step, String recordedBy) {
    Run run = noCodeClient.findById(Run.class, runId);
    if (run == null) {
        throw new EntityNotFoundException("Run no encontrado: " + runId);
    }

    Metric metric = new Metric();
    metric.setRun(run);
    metric.setTrnmetricname(metricName);
    metric.setTrnmetricvalue(metricValue);
    metric.setTrnmetricstep(step);
    metric.setTrnmetrictimestamp(new Timestamp(System.currentTimeMillis()));
    metric.setTrnmetricrecordedby(recordedBy);

    // TODO: Llamar a microservicio Python para registrar en MLflow
    // PENDIENTE: Implementar cuando microservicio esté disponible
    /*
    try {
        mlflowAdapterClient.logMetric(run.getTrnrunrunid(), metricName, metricValue, step);
    } catch (Exception e) {
        log.error("Error registrando métrica en MLflow", e);
    }
    */

    return noCodeClient.save(metric);
}
```

**Método: `saveArtifact(Long runId, String artifactName, String artifactPath, String artifactType, String savedBy)`**
```java
/**
 * Guarda un artefacto generado durante el entrenamiento
 *
 * Consultas BBDD:
 * SELECT * FROM trnruns WHERE idxrun = ?
 * INSERT INTO trnartifacts (...)
 *
 * Llamada Microservicio Python (COMENTADA - PENDIENTE):
 * - codeflowx-mlflow-adapter: POST /api/v1/artifacts/log
 */
public Artifact saveArtifact(Long runId, String artifactName, String artifactPath, String artifactType, String savedBy) {
    Run run = noCodeClient.findById(Run.class, runId);
    if (run == null) {
        throw new EntityNotFoundException("Run no encontrado: " + runId);
    }

    Artifact artifact = new Artifact();
    artifact.setRun(run);
    artifact.setTrnartifactname(artifactName);
    artifact.setTrnartifactpath(artifactPath);
    artifact.setTrnartifacttype(artifactType);
    artifact.setTrnartifactcreatedat(new Timestamp(System.currentTimeMillis()));
    artifact.setTrnartifactcreatedby(savedBy);

    // TODO: Llamar a microservicio Python para registrar en MLflow
    // PENDIENTE: Implementar cuando microservicio esté disponible
    /*
    try {
        mlflowAdapterClient.logArtifact(run.getTrnrunrunid(), artifactName, artifactPath);
    } catch (Exception e) {
        log.error("Error registrando artefacto en MLflow", e);
    }
    */

    return noCodeClient.save(artifact);
}
```

#### **1.3. Operaciones de Costos**

**Método: `calculateTrainingCost(Long runId, String calculatedBy)`**
```java
/**
 * Calcula el costo total de un run de entrenamiento
 *
 * Consultas BBDD:
 * SELECT * FROM trnruns WHERE idxrun = ?
 * SELECT * FROM trntrainingexecutions WHERE idxrun = ?
 * SELECT * FROM trnenvironments WHERE idxenvironment = ?
 */
public TrainingCost calculateTrainingCost(Long runId, String calculatedBy) {
    Run run = noCodeClient.findById(Run.class, runId);
    if (run == null) {
        throw new EntityNotFoundException("Run no encontrado: " + runId);
    }

    // Obtener ejecuciones del run
    List<TrainingExecution> executions = findTrainingExecutions(runId);

    // Calcular costos por tipo de recurso
    BigDecimal computeCost = BigDecimal.ZERO;
    BigDecimal storageCost = BigDecimal.ZERO;
    BigDecimal networkCost = BigDecimal.ZERO;

    for (TrainingExecution execution : executions) {
        Environment env = execution.getEnvironment();
        if (env != null) {
            // Calcular costo de compute (GPU/CPU horas)
            BigDecimal computeHours = calculateComputeHours(execution);
            computeCost = computeCost.add(
                computeHours.multiply(env.getTrnenvironmentgpupriceperhour())
            );

            // Calcular costo de storage
            BigDecimal storageGB = execution.getTrnexecutionstorageused();
            storageCost = storageCost.add(
                storageGB.multiply(env.getTrnenvironmentstoragepricepergb())
            );
        }
    }

    BigDecimal totalCost = computeCost.add(storageCost).add(networkCost);

    // Guardar cálculo de costo
    TrainingCost cost = new TrainingCost();
    cost.setRun(run);
    cost.setTrncostcomputecost(computeCost);
    cost.setTrncoststoragecost(storageCost);
    cost.setTrncostnetworkcost(networkCost);
    cost.setTrncosttotalcost(totalCost);
    cost.setTrncostcalculatedat(new Timestamp(System.currentTimeMillis()));
    cost.setTrncostcalculatedby(calculatedBy);

    return noCodeClient.save(cost);
}
```

---

## 📊 CONSULTAS BBDD ESPECÍFICAS

### **1. Obtener experimentos activos**
```sql
SELECT e.idxexperiment, e.trnexperimentname, e.trnexperimentstatus,
       COUNT(r.idxrun) AS total_runs
FROM trnexperiments e
LEFT JOIN trnruns r ON r.idxexperiment = e.idxexperiment
WHERE e.trnexperimentstatus IN ('ACTIVE', 'RUNNING')
GROUP BY e.idxexperiment, e.trnexperimentname, e.trnexperimentstatus
ORDER BY e.trnexperimentcreatedat DESC;
```

### **2. Obtener runs por estado**
```sql
SELECT r.idxrun, r.trnrunnam, r.trnrunstatus, r.trnruncreatedat,
       e.trnexperimentname
FROM trnruns r
JOIN trnexperiments e ON r.idxexperiment = e.idxexperiment
WHERE r.trnrunstatus = ?
ORDER BY r.trnruncreatedat DESC;
```

### **3. Obtener métricas de un run**
```sql
SELECT m.idxmetric, m.trnmetricname, m.trnmetricvalue, m.trnmetricstep,
       m.trnmetrictimestamp
FROM trnmetrics m
WHERE m.idxrun = ?
ORDER BY m.trnmetricstep ASC, m.trnmetrictimestamp ASC;
```

### **4. Obtener artefactos de un run**
```sql
SELECT a.idxartifact, a.trnartifactname, a.trnartifacttype,
       a.trnartifactpath, a.trnartifactcreatedat
FROM trnartifacts a
WHERE a.idxrun = ?
ORDER BY a.trnartifactcreatedat DESC;
```

### **5. Obtener costos totales por experimento**
```sql
SELECT e.idxexperiment, e.trnexperimentname,
       COALESCE(SUM(tc.trncosttotalcost), 0) AS total_cost
FROM trnexperiments e
LEFT JOIN trnruns r ON r.idxexperiment = e.idxexperiment
LEFT JOIN trntrainingcosts tc ON tc.idxrun = r.idxrun
WHERE e.idxexperiment = ?
GROUP BY e.idxexperiment, e.trnexperimentname;
```

---

## 🔗 INTEGRACIÓN CON MICROSERVICIOS PYTHON

> **⚠️ NOTA IMPORTANTE:** Todas las llamadas a microservicios Python están **COMENTADAS** y pendientes de implementación.
> Se deben implementar cuando los microservicios estén disponibles y operativos.

### **1. codeflowx-training-service**

**Endpoint: `POST /api/v1/runs/create`**
```java
// TODO: PENDIENTE - Implementar cuando microservicio esté disponible
/*
public RunResponse createRun(Long experimentId, RunConfig config) {
    String url = trainingServiceBaseUrl + "/api/v1/runs/create";
    Map<String, Object> payload = Map.of(
        "experimentId", experimentId,
        "config", config
    );
    return restTemplate.postForObject(url, payload, RunResponse.class);
}
*/
```

### **2. codeflowx-mlflow-adapter**

**Endpoint: `POST /api/v1/metrics/log`**
```java
// TODO: PENDIENTE - Implementar cuando microservicio esté disponible
/*
public void logMetric(String runId, String metricName, Double value, Long step) {
    String url = mlflowAdapterBaseUrl + "/api/v1/metrics/log";
    Map<String, Object> payload = Map.of(
        "runId", runId,
        "metricName", metricName,
        "value", value,
        "step", step
    );
    restTemplate.postForObject(url, payload, Void.class);
}
*/
```

**Endpoint: `POST /api/v1/artifacts/log`**
```java
// TODO: PENDIENTE - Implementar cuando microservicio esté disponible
/*
public void logArtifact(String runId, String artifactName, String artifactPath) {
    String url = mlflowAdapterBaseUrl + "/api/v1/artifacts/log";
    Map<String, Object> payload = Map.of(
        "runId", runId,
        "artifactName", artifactName,
        "artifactPath", artifactPath
    );
    restTemplate.postForObject(url, payload, Void.class);
}
*/
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### **Fase 1: Business Service Base**
- [ ] Crear `TrainingBusinessService.java`
- [ ] Implementar operaciones CRUD básicas
- [ ] Implementar gestión de experimentos y runs

### **Fase 2: Seguimiento y Métricas**
- [ ] Implementar registro de métricas
- [ ] Implementar guardado de artefactos
- [ ] Implementar gestión de checkpoints

### **Fase 3: Gestión de Costos**
- [ ] Implementar cálculo de costos de entrenamiento
- [ ] Implementar seguimiento de recursos consumidos
- [ ] Implementar reportes de costos

### **Fase 4: Integración con Microservicios (PENDIENTE)**
- [ ] ⚠️ **PENDIENTE:** Configurar clientes REST para microservicios Python
- [ ] ⚠️ **PENDIENTE:** Implementar llamadas a `codeflowx-training-service`
- [ ] ⚠️ **PENDIENTE:** Implementar llamadas a `codeflowx-mlflow-adapter`
- [ ] **NOTA:** Todas las llamadas a microservicios están comentadas en el código

### **Fase 5: Consultas BBDD**
- [ ] Implementar consultas específicas de experimentos y runs
- [ ] Implementar consultas de métricas y artefactos
- [ ] Implementar consultas de costos

### **Fase 6: Testing y Validación**
- [ ] Crear tests unitarios para cada método
- [ ] Validar integración con microservicios
- [ ] Validar consultas BBDD

---

## 📝 NOTAS IMPORTANTES

1. **Arquitectura EnArt:** Usar `NoCodeClient` (no Repository) para acceso a datos
2. **Microservicios Python:** ⚠️ **TODAS LAS LLAMADAS ESTÁN COMENTADAS** - Pendientes de implementar cuando microservicios estén disponibles
3. **Logging:** Registrar todas las operaciones críticas para auditoría
4. **Transacciones:** Usar `@Transactional` para operaciones que modifican múltiples entidades
5. **Entidades JPA:** Revisar entidades en `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/training/` para campos disponibles
6. **Integración MLflow:** Las métricas y artefactos deben sincronizarse con MLflow cuando esté disponible

---

**Última actualización:** Diciembre 2025
**Estado:** Pendiente de implementación
