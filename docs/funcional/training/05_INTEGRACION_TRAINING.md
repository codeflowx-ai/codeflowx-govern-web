# 🔗 INTEGRACIÓN - MÓDULO TRAINING

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación de integración del módulo training

---

## 🎯 RESUMEN EJECUTIVO

El módulo **training** se integra **completamente** con otros módulos de CodeflowX Govern y sistemas externos, proporcionando **experimentación automatizada**, **HPO integrado** y **governance de entrenamiento** que supera ampliamente las capacidades de MLflow con funcionalidades empresariales avanzadas.

---

## 🔗 INTEGRACIÓN CON MÓDULOS INTERNOS

### **1. Models Module**

#### **Integración Bidireccional:**
- **Entrenamiento automático** de modelos nuevos
- **Versionado** de modelos entrenados
- **Evaluación integrada** de modelos entrenados
- **Gestión** de artefactos de modelos

#### **APIs de Integración:**
```java
@Service
public class ModelTrainingIntegration {
    
    @Autowired
    private ModelService modelService;
    
    @Autowired
    private TrainingService trainingService;
    
    /**
     * Entrenar modelo automáticamente al ser creado
     */
    @EventListener
    public void onModelCreated(ModelCreatedEvent event) {
        String modelId = event.getModelId();
        String modelType = event.getModelType();
        
        // Crear experimento de entrenamiento
        Experiment experiment = trainingService.createExperimentFromTemplate(
            getTemplateForModelType(modelType),
            "Auto-training for " + modelId,
            getCurrentUser()
        );
        
        // Ejecutar entrenamiento
        Run run = trainingService.executeExperiment(
            experiment.getExperimentId(),
            "initial-training"
        );
        
        // Actualizar modelo con run
        modelService.updateModelTrainingRun(modelId, run.getRunId());
    }
    
    /**
     * Actualizar modelo con resultados de entrenamiento
     */
    @EventListener
    public void onTrainingCompleted(TrainingCompletedEvent event) {
        String runId = event.getRunId();
        String modelId = event.getModelId();
        
        // Obtener artefactos del entrenamiento
        List<TrainingArtifact> artifacts = trainingService.getRunArtifacts(runId);
        
        // Actualizar modelo con artefactos
        for (TrainingArtifact artifact : artifacts) {
            if ("MODEL".equals(artifact.getArtifactType())) {
                modelService.updateModelArtifact(modelId, artifact);
            }
        }
        
        // Actualizar score del modelo
        Double bestMetric = trainingService.getBestMetric(runId);
        modelService.updateModelScore(modelId, bestMetric);
    }
}
```

### **2. Evaluation Module**

#### **Integración de Evaluación:**
- **Evaluación automática** de modelos entrenados
- **Métricas de calidad** integradas
- **Detección de sesgos** en modelos entrenados
- **Análisis de fairness** post-entrenamiento

#### **APIs de Integración:**
```java
@Service
public class TrainingEvaluationIntegration {
    
    @Autowired
    private EvaluationService evaluationService;
    
    @Autowired
    private TrainingService trainingService;
    
    /**
     * Evaluar modelo automáticamente después del entrenamiento
     */
    @EventListener
    public void onTrainingCompleted(TrainingCompletedEvent event) {
        String runId = event.getRunId();
        String modelId = event.getModelId();
        
        // Crear evaluación automática
        ModelEvaluationRequest request = ModelEvaluationRequest.builder()
            .modelId(modelId)
            .runId(runId)
            .evaluationType(EvaluationType.POST_TRAINING)
            .enableBiasDetection(true)
            .enableFairnessAnalysis(true)
            .build();
            
        evaluationService.evaluateModel(request);
    }
    
    /**
     * Actualizar experimento con resultados de evaluación
     */
    @EventListener
    public void onEvaluationCompleted(EvaluationCompletedEvent event) {
        String modelId = event.getModelId();
        String runId = event.getRunId();
        
        if (runId != null) {
            // Actualizar run con score de evaluación
            trainingService.updateRunEvaluationScore(runId, event.getOverallScore());
            
            // Si la evaluación es exitosa, marcar como candidato para producción
            if (event.getOverallScore() >= 0.9) {
                trainingService.markRunForProduction(runId);
            }
        }
    }
}
```

### **3. Governance Module**

#### **Integración de Governance:**
- **Políticas de entrenamiento** automatizadas
- **Compliance** con marcos regulatorios
- **Auditoría** de procesos de entrenamiento
- **Control de acceso** granular

#### **APIs de Integración:**
```java
@Service
public class TrainingGovernanceIntegration {
    
    @Autowired
    private GovernanceService governanceService;
    
    @Autowired
    private TrainingService trainingService;
    
    /**
     * Validar governance antes del entrenamiento
     */
    public GovernanceValidationResult validateTrainingGovernance(
        String experimentId, 
        String userId) {
        
        Experiment experiment = trainingService.getExperiment(experimentId);
        
        // Verificar políticas de entrenamiento
        List<Policy> trainingPolicies = governanceService.getTrainingPolicies();
        
        GovernanceValidationResult result = new GovernanceValidationResult();
        
        for (Policy policy : trainingPolicies) {
            PolicyValidation validation = validatePolicy(experiment, policy);
            result.addValidation(validation);
        }
        
        return result;
    }
    
    /**
     * Generar reporte de governance de entrenamiento
     */
    public TrainingGovernanceReport generateGovernanceReport(String experimentId) {
        Experiment experiment = trainingService.getExperiment(experimentId);
        List<Run> runs = trainingService.getExperimentRuns(experimentId);
        
        return TrainingGovernanceReport.builder()
            .experimentId(experimentId)
            .experimentName(experiment.getName())
            .totalRuns(runs.size())
            .completedRuns(runs.stream()
                .filter(r -> r.getStatus() == RunStatus.COMPLETED)
                .count())
            .governanceScore(calculateGovernanceScore(experiment, runs))
            .complianceViolations(getComplianceViolations(experiment, runs))
            .recommendations(generateGovernanceRecommendations(experiment, runs))
            .build();
    }
}
```

---

## 🔗 INTEGRACIÓN CON SISTEMAS EXTERNOS

### **1. MLflow Integration**

#### **Sincronización Bidireccional:**
```java
@Component
public class MLflowTrainingIntegration {
    
    @Autowired
    private MLflowClient mlflowClient;
    
    @Autowired
    private TrainingService trainingService;
    
    /**
     * Sincronizar experimento con MLflow
     */
    public void syncExperimentWithMLflow(String experimentId) {
        Experiment experiment = trainingService.getExperiment(experimentId);
        
        // Crear experimento en MLflow
        MLflowExperiment mlflowExperiment = mlflowClient.createExperiment()
            .name(experiment.getName())
            .description(experiment.getDescription())
            .build();
        
        // Sincronizar runs
        List<Run> runs = trainingService.getExperimentRuns(experimentId);
        for (Run run : runs) {
            syncRunWithMLflow(run, mlflowExperiment.getExperimentId());
        }
    }
    
    /**
     * Sincronizar run con MLflow
     */
    private void syncRunWithMLflow(Run run, String mlflowExperimentId) {
        // Crear run en MLflow
        MLflowRun mlflowRun = mlflowClient.createRun()
            .experimentId(mlflowExperimentId)
            .runName(run.getName())
            .build();
        
        // Log métricas
        List<TrainingMetric> metrics = trainingService.getRunMetrics(run.getRunId());
        for (TrainingMetric metric : metrics) {
            mlflowClient.logMetric(mlflowRun.getRunId(), 
                metric.getMetricName(), metric.getMetricValue());
        }
        
        // Log parámetros
        List<Parameter> parameters = trainingService.getRunParameters(run.getRunId());
        for (Parameter param : parameters) {
            mlflowClient.logParam(mlflowRun.getRunId(), 
                param.getName(), param.getValue());
        }
        
        // Log artefactos
        List<TrainingArtifact> artifacts = trainingService.getRunArtifacts(run.getRunId());
        for (TrainingArtifact artifact : artifacts) {
            mlflowClient.logArtifact(mlflowRun.getRunId(), 
                artifact.getFilePath(), artifact.getName());
        }
        
        // Finalizar run
        mlflowClient.endRun(mlflowRun.getRunId());
    }
}
```

### **2. Cloud Platforms**

#### **AWS SageMaker Integration:**
```java
@Component
public class SageMakerTrainingIntegration {
    
    @Autowired
    private SageMakerClient sageMakerClient;
    
    @Autowired
    private TrainingService trainingService;
    
    /**
     * Entrenar modelo en SageMaker
     */
    public Run trainModelInSageMaker(String experimentId, String runName) {
        Experiment experiment = trainingService.getExperiment(experimentId);
        
        // Crear job de entrenamiento en SageMaker
        SageMakerTrainingJob job = sageMakerClient.createTrainingJob()
            .jobName("training-" + experimentId + "-" + runName)
            .roleArn("arn:aws:iam::account:role/SageMakerRole")
            .algorithmSpecification(getAlgorithmSpecification(experiment))
            .resourceConfig(getResourceConfig(experiment))
            .inputDataConfig(getInputDataConfig(experiment))
            .outputDataConfig(getOutputDataConfig(experiment))
            .hyperParameters(getHyperParameters(experiment))
            .build();
        
        // Crear run local
        Run run = trainingService.createRun(experimentId, runName);
        run.setExternalJobId(job.getJobName());
        run.setStatus(RunStatus.RUNNING);
        
        return trainingService.updateRun(run);
    }
    
    /**
     * Monitorear job de SageMaker
     */
    @Scheduled(fixedDelay = 30000) // Cada 30 segundos
    public void monitorSageMakerJobs() {
        List<Run> runningRuns = trainingService.getRunningRunsWithExternalJobId();
        
        for (Run run : runningRuns) {
            SageMakerTrainingJob job = sageMakerClient.getTrainingJob(run.getExternalJobId());
            
            if (job.getStatus() == TrainingJobStatus.COMPLETED) {
                // Actualizar run como completado
                run.setStatus(RunStatus.COMPLETED);
                run.setCompletedDate(LocalDateTime.now());
                trainingService.updateRun(run);
                
                // Descargar artefactos
                downloadSageMakerArtifacts(run, job);
            } else if (job.getStatus() == TrainingJobStatus.FAILED) {
                // Actualizar run como fallido
                run.setStatus(RunStatus.FAILED);
                run.setCompletedDate(LocalDateTime.now());
                trainingService.updateRun(run);
            }
        }
    }
}
```

#### **Azure ML Integration:**
```java
@Component
public class AzureMLTrainingIntegration {
    
    @Autowired
    private AzureMLClient azureMLClient;
    
    @Autowired
    private TrainingService trainingService;
    
    /**
     * Entrenar modelo en Azure ML
     */
    public Run trainModelInAzureML(String experimentId, String runName) {
        Experiment experiment = trainingService.getExperiment(experimentId);
        
        // Crear job de entrenamiento en Azure ML
        AzureMLJob job = azureMLClient.createJob()
            .experimentName("codeflowx-training")
            .jobName("training-" + experimentId + "-" + runName)
            .computeTarget("gpu-cluster")
            .environment(getEnvironment(experiment))
            .script(getTrainingScript(experiment))
            .dataset(getDataset(experiment))
            .build();
        
        // Crear run local
        Run run = trainingService.createRun(experimentId, runName);
        run.setExternalJobId(job.getJobId());
        run.setStatus(RunStatus.RUNNING);
        
        return trainingService.updateRun(run);
    }
}
```

---

## 🔄 INTEGRACIÓN CON BPMN

### **1. Training Process Integration**

```java
@Component
public class TrainingBPMNIntegration {
    
    @Autowired
    private ProcessEngine processEngine;
    
    @Autowired
    private TrainingService trainingService;
    
    /**
     * Iniciar proceso de entrenamiento
     */
    public String startTrainingProcess(String experimentId, String runName) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("experimentId", experimentId);
        variables.put("runName", runName);
        variables.put("startedBy", getCurrentUser());
        
        ProcessInstance processInstance = processEngine.getRuntimeService()
            .startProcessInstanceByKey("experiment-execution-v1", variables);
        
        return processInstance.getId();
    }
    
    /**
     * Completar entrenamiento en proceso BPMN
     */
    public void completeTrainingInProcess(String processInstanceId, Run run) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("runId", run.getRunId());
        variables.put("runStatus", run.getStatus());
        variables.put("durationSeconds", run.getDurationSeconds());
        variables.put("bestMetric", trainingService.getBestMetric(run.getRunId()));
        
        processEngine.getRuntimeService()
            .setVariables(processInstanceId, variables);
    }
}
```

### **2. HPO Process Integration**

```java
@Component
public class HPOBPMNIntegration {
    
    @Autowired
    private ProcessEngine processEngine;
    
    @Autowired
    private HPOService hpoService;
    
    /**
     * Iniciar proceso de HPO
     */
    public String startHPOProcess(String hpoExperimentId) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("hpoExperimentId", hpoExperimentId);
        variables.put("startedBy", getCurrentUser());
        
        ProcessInstance processInstance = processEngine.getRuntimeService()
            .startProcessInstanceByKey("hpo-optimization-v1", variables);
        
        return processInstance.getId();
    }
}
```

---

## 📊 INTEGRACIÓN CON MONITOREO

### **1. Training Metrics Integration**

```java
@Component
public class TrainingMetricsIntegration {
    
    @Autowired
    private MetricsService metricsService;
    
    @Autowired
    private TrainingService trainingService;
    
    /**
     * Enviar métricas de entrenamiento
     */
    @EventListener
    public void onTrainingCompleted(TrainingCompletedEvent event) {
        String runId = event.getRunId();
        String experimentId = event.getExperimentId();
        
        // Enviar métricas
        metricsService.sendMetric("training.run_duration", event.getDurationSeconds(), 
            Map.of("run_id", runId, "experiment_id", experimentId));
        metricsService.sendMetric("training.best_metric", event.getBestMetric(), 
            Map.of("run_id", runId, "experiment_id", experimentId));
        metricsService.sendMetric("training.resource_utilization", event.getResourceUtilization(), 
            Map.of("run_id", runId, "experiment_id", experimentId));
    }
}
```

### **2. Training Alerts Integration**

```java
@Component
public class TrainingAlertsIntegration {
    
    @Autowired
    private AlertsService alertsService;
    
    @Autowired
    private TrainingService trainingService;
    
    /**
     * Enviar alertas de entrenamiento
     */
    @EventListener
    public void onTrainingCompleted(TrainingCompletedEvent event) {
        String runId = event.getRunId();
        String experimentId = event.getExperimentId();
        
        // Alert si el entrenamiento falló
        if (event.getStatus() == RunStatus.FAILED) {
            alertsService.sendAlert(
                AlertType.TRAINING_FAILED,
                "Training run " + runId + " failed",
                Map.of("run_id", runId, "experiment_id", experimentId)
            );
        }
        
        // Alert si el uso de recursos es alto
        if (event.getResourceUtilization() > 0.9) {
            alertsService.sendAlert(
                AlertType.HIGH_RESOURCE_UTILIZATION,
                "High resource utilization in run " + runId,
                Map.of("run_id", runId, "utilization", event.getResourceUtilization())
            );
        }
    }
}
```

---

## 🎯 BENEFICIOS DE LA INTEGRACIÓN

### **Para Desarrolladores:**
- **Integración completa** con otros módulos
- **APIs estándar** para sistemas externos
- **Automatización** de entrenamientos
- **Monitoreo** en tiempo real

### **Para Data Scientists:**
- **Experimentos automatizados** desde templates
- **HPO inteligente** con algoritmos avanzados
- **Tracking en tiempo real** de métricas
- **Reproducibilidad** garantizada

### **Para ML Engineers:**
- **Infraestructura optimizada** automáticamente
- **Monitoreo proactivo** de recursos
- **Alertas inteligentes** de problemas
- **Escalabilidad** automática

### **Para la Organización:**
- **Eficiencia operativa** mejorada
- **Calidad garantizada** de experimentos
- **Compliance** regulatorio
- **Auditoría** simplificada

---

## 🎯 CONCLUSIÓN

El módulo Training proporciona **integración completa** que permite:

- 🔗 **Integración** con módulos internos de CodeflowX
- 🌐 **Conectividad** con sistemas externos
- 🔄 **Automatización** completa de experimentación
- 📊 **Monitoreo** en tiempo real
- 📋 **Gestión** centralizada de experimentos
- 🚀 **Superación** de capacidades de MLflow

**Esta integración está diseñada** para ser la solución definitiva de experimentación y entrenamiento de modelos de IA en entornos empresariales.
