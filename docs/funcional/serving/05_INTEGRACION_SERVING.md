# 🚀 SERVING - INTEGRACIÓN

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación de integración del módulo Serving

---

## 🎯 RESUMEN EJECUTIVO

El módulo **Serving** se integra con múltiples componentes de la plataforma CodeflowX Govern para proporcionar deployment, inferencia y monitoreo completo de modelos ML.

**Integraciones Principales:**
- **Models:** Gestión de modelos para deployment
- **Training:** Integración con experimentos y HPO
- **Monitoring:** Monitoreo de performance y salud
- **Governance:** Compliance y auditoría
- **Analytics:** Métricas y reportes

---

## 🔗 INTEGRACIÓN CON MODELS

### **Gestión de Modelos**

```java
@Service
public class ServingModelIntegration {
    
    @Autowired
    private ModelService modelService;
    
    @Autowired
    private DeploymentService deploymentService;
    
    public Deployment deployModel(Long modelId, String version) {
        // Obtener modelo del repositorio
        Model model = modelService.getModel(modelId, version);
        
        // Validar modelo para deployment
        ValidationResult validation = modelService.validateForDeployment(model);
        if (!validation.isValid()) {
            throw new DeploymentException("Model validation failed: " + validation.getErrors());
        }
        
        // Crear deployment
        DeploymentRequest request = DeploymentRequest.builder()
            .modelId(modelId)
            .modelVersion(version)
            .modelArtifactUrl(model.getArtifactUrl())
            .framework(model.getFramework())
            .requirements(model.getRequirements())
            .build();
            
        return deploymentService.createDeployment(request);
    }
}
```

### **Versionado de Modelos**

```java
@Service
public class ModelVersioningIntegration {
    
    public void deployNewVersion(Long modelId, String newVersion) {
        // Obtener deployment actual
        Deployment currentDeployment = deploymentService.getActiveDeployment(modelId);
        
        // Crear nuevo deployment con nueva versión
        Deployment newDeployment = deploymentService.createDeployment(
            modelId, newVersion, DeploymentStrategy.CANARY);
            
        // Configurar canary deployment (5% tráfico)
        deploymentService.configureCanary(newDeployment.getId(), 5.0);
        
        // Monitorear métricas
        if (metricsAreGood(newDeployment)) {
            // Gradualmente incrementar tráfico
            deploymentService.incrementCanaryTraffic(newDeployment.getId(), 50.0);
            
            // Si todo OK, switch completo
            deploymentService.promoteToProduction(newDeployment.getId());
            
            // Retirar versión antigua
            deploymentService.retireDeployment(currentDeployment.getId());
        }
    }
}
```

---

## 🔗 INTEGRACIÓN CON TRAINING

### **Experimentos a Producción**

```java
@Service
public class TrainingServingIntegration {
    
    @Autowired
    private ExperimentService experimentService;
    
    @Autowired
    private DeploymentService deploymentService;
    
    public Deployment deployExperiment(Long experimentId) {
        // Obtener mejor run del experimento
        ExperimentRun bestRun = experimentService.getBestRun(experimentId);
        
        // Registrar modelo del run
        Model model = modelService.registerFromExperiment(bestRun);
        
        // Crear deployment
        return deploymentService.createDeployment(
            model.getId(),
            model.getVersion(),
            DeploymentType.REALTIME
        );
    }
}
```

### **HPO a Producción**

```java
@Service
public class HpoServingIntegration {
    
    public Deployment deployBestHpoModel(Long hpoJobId) {
        // Obtener mejores hiperparámetros
        HpoResult bestResult = hpoService.getBestResult(hpoJobId);
        
        // Re-entrenar con mejores hiperparámetros
        TrainingJob trainingJob = trainingService.trainWithParams(
            bestResult.getDatasetId(),
            bestResult.getHyperparameters()
        );
        
        // Esperar completación
        trainingJob = trainingService.waitForCompletion(trainingJob.getId());
        
        // Registrar y desplegar modelo
        Model model = modelService.registerFromTrainingJob(trainingJob.getId());
        return deploymentService.createDeployment(model.getId(), model.getVersion());
    }
}
```

---

## 🔗 INTEGRACIÓN CON MONITORING

### **Métricas en Tiempo Real**

```java
@Service
public class ServingMonitoringIntegration {
    
    @Autowired
    private MonitoringService monitoringService;
    
    @Async
    public void publishPredictionMetrics(Prediction prediction) {
        // Publicar métricas de predicción
        MetricData metrics = MetricData.builder()
            .deploymentId(prediction.getDeploymentId())
            .metricType(MetricType.PREDICTION_LATENCY)
            .value(prediction.getLatencyMs())
            .timestamp(prediction.getTimestamp())
            .tags(Map.of(
                "model_id", prediction.getModelId().toString(),
                "model_version", prediction.getModelVersion()
            ))
            .build();
            
        monitoringService.publishMetric(metrics);
        
        // Verificar SLA
        SlaStatus slaStatus = slaService.checkCompliance(prediction);
        if (!slaStatus.isCompliant()) {
            alertService.sendAlert(createSlaViolationAlert(slaStatus));
        }
    }
}
```

### **Detección de Drift**

```java
@Service
public class DriftMonitoringIntegration {
    
    @Scheduled(fixedRate = 300000) // Cada 5 minutos
    public void monitorDataDrift() {
        List<Deployment> activeDeployments = deploymentService.getActiveDeployments();
        
        for (Deployment deployment : activeDeployments) {
            // Obtener predicciones recientes
            List<Prediction> recentPredictions = predictionService
                .getRecentPredictions(deployment.getId(), Duration.ofHours(1));
                
            // Calcular drift
            DriftAnalysis drift = monitoringService.analyzeDrift(
                deployment.getTrainingDataDistribution(),
                extractDistribution(recentPredictions)
            );
            
            if (drift.getDriftScore() > 0.7) {
                // Crear alerta de drift
                alertService.sendAlert(Alert.builder()
                    .type(AlertType.DATA_DRIFT)
                    .severity(AlertSeverity.HIGH)
                    .deploymentId(deployment.getId())
                    .message("Data drift detected: " + drift.getDriftScore())
                    .build());
                    
                // Sugerir reentrenamiento
                retrainingService.suggestRetraining(deployment.getModelId());
            }
        }
    }
}
```

---

## 🔗 INTEGRACIÓN CON GOVERNANCE

### **Compliance Check**

```java
@Service
public class ServingGovernanceIntegration {
    
    @Autowired
    private GovernanceService governanceService;
    
    public void validateDeploymentCompliance(Long deploymentId) {
        Deployment deployment = deploymentService.getDeployment(deploymentId);
        
        // Verificar compliance del modelo
        ComplianceResult compliance = governanceService.checkModelCompliance(
            deployment.getModelId(),
            deployment.getModelVersion()
        );
        
        if (!compliance.isCompliant()) {
            // Pausar deployment si no cumple
            deploymentService.pauseDeployment(deploymentId);
            
            // Crear incidente de governance
            governanceService.createIncident(Incident.builder()
                .type(IncidentType.COMPLIANCE_VIOLATION)
                .deploymentId(deploymentId)
                .violations(compliance.getViolations())
                .build());
        }
    }
}
```

### **Auditoría de Predicciones**

```java
@Service
public class PredictionAuditIntegration {
    
    @Async
    public void auditPrediction(Prediction prediction) {
        // Registrar predicción para auditoría
        AuditEntry audit = AuditEntry.builder()
            .entityType(EntityType.PREDICTION)
            .entityId(prediction.getId())
            .action(AuditAction.PREDICTION_MADE)
            .userId(prediction.getUserId())
            .details(Map.of(
                "deployment_id", prediction.getDeploymentId(),
                "model_id", prediction.getModelId(),
                "model_version", prediction.getModelVersion(),
                "latency_ms", prediction.getLatencyMs(),
                "input_hash", hashInputs(prediction.getInputs()),
                "output_hash", hashOutputs(prediction.getPredictions())
            ))
            .timestamp(prediction.getTimestamp())
            .build();
            
        governanceService.createAuditEntry(audit);
    }
}
```

---

## 🔗 INTEGRACIÓN CON ANALYTICS

### **Análisis de Performance**

```java
@Service
public class ServingAnalyticsIntegration {
    
    public PerformanceReport generatePerformanceReport(Long deploymentId, LocalDate from, LocalDate to) {
        // Obtener métricas del período
        List<Prediction> predictions = predictionService.getPredictions(
            deploymentId, from, to);
            
        // Calcular estadísticas
        PerformanceStats stats = PerformanceStats.builder()
            .totalPredictions(predictions.size())
            .avgLatency(calculateAvgLatency(predictions))
            .p95Latency(calculatePercentile(predictions, 95))
            .p99Latency(calculatePercentile(predictions, 99))
            .errorRate(calculateErrorRate(predictions))
            .throughput(calculateThroughput(predictions))
            .build();
            
        // Generar report
        return analyticsService.createPerformanceReport(deploymentId, stats);
    }
}
```

### **Análisis de Costos**

```java
@Service
public class ServingCostAnalytics {
    
    public CostReport generateCostReport(Long deploymentId, LocalDate from, LocalDate to) {
        Deployment deployment = deploymentService.getDeployment(deploymentId);
        
        // Calcular costos de infraestructura
        ResourceUsage usage = monitoringService.getResourceUsage(deploymentId, from, to);
        
        CostBreakdown costs = CostBreakdown.builder()
            .computeCost(calculateComputeCost(usage.getCpuHours(), deployment.getCpuLimit()))
            .memoryCost(calculateMemoryCost(usage.getMemoryGbHours(), deployment.getMemoryLimit()))
            .storageCost(calculateStorageCost(usage.getStorageGb()))
            .networkCost(calculateNetworkCost(usage.getNetworkGb()))
            .predictionCount(usage.getPredictionCount())
            .costPerPrediction(calculateCostPerPrediction(usage))
            .build();
            
        return analyticsService.createCostReport(deploymentId, costs);
    }
}
```

---

## 🔗 INTEGRACIÓN CON KUBERNETES

### **Deployment en Kubernetes**

```java
@Service
public class KubernetesDeploymentService {
    
    @Autowired
    private KubernetesClient k8sClient;
    
    public void deployToKubernetes(Deployment deployment) {
        // Crear Deployment
        io.fabric8.kubernetes.api.model.apps.Deployment k8sDeployment = 
            new DeploymentBuilder()
                .withNewMetadata()
                    .withName("model-" + deployment.getId())
                    .withLabels(Map.of(
                        "app", "codeflowx-serving",
                        "model-id", deployment.getModelId().toString(),
                        "deployment-id", deployment.getId().toString()
                    ))
                .endMetadata()
                .withNewSpec()
                    .withReplicas(deployment.getReplicas())
                    .withNewSelector()
                        .withMatchLabels(Map.of("deployment-id", deployment.getId().toString()))
                    .endSelector()
                    .withNewTemplate()
                        .withNewMetadata()
                            .withLabels(Map.of("deployment-id", deployment.getId().toString()))
                        .endMetadata()
                        .withNewSpec()
                            .addNewContainer()
                                .withName("model-server")
                                .withImage("codeflowx/model-server:latest")
                                .withNewResources()
                                    .withRequests(Map.of(
                                        "cpu", deployment.getCpuLimit(),
                                        "memory", deployment.getMemoryLimit()
                                    ))
                                .endResources()
                                .withEnv(
                                    new EnvVar("MODEL_ID", deployment.getModelId().toString(), null),
                                    new EnvVar("MODEL_VERSION", deployment.getModelVersion(), null)
                                )
                            .endContainer()
                        .endSpec()
                    .endTemplate()
                .endSpec()
                .build();
                
        k8sClient.apps().deployments().create(k8sDeployment);
        
        // Crear Service
        Service k8sService = new ServiceBuilder()
            .withNewMetadata()
                .withName("model-service-" + deployment.getId())
            .endMetadata()
            .withNewSpec()
                .withSelector(Map.of("deployment-id", deployment.getId().toString()))
                .addNewPort()
                    .withPort(8080)
                    .withTargetPort(new IntOrString(8080))
                .endPort()
            .endSpec()
            .build();
            
        k8sClient.services().create(k8sService);
    }
}
```

---

## 🔗 INTEGRACIÓN CON MESSAGE QUEUE

### **Predicciones Asíncronas**

```java
@Service
public class AsyncPredictionService {
    
    @Autowired
    private KafkaTemplate<String, PredictionRequest> kafkaTemplate;
    
    public String submitAsyncPrediction(PredictionRequest request) {
        String predictionId = UUID.randomUUID().toString();
        
        // Publicar en Kafka
        kafkaTemplate.send("predictions.requests", predictionId, request);
        
        return predictionId;
    }
    
    @KafkaListener(topics = "predictions.requests")
    public void processPredictionRequest(PredictionRequest request) {
        // Realizar predicción
        PredictionResponse response = predictionService.predict(request);
        
        // Publicar resultado
        kafkaTemplate.send("predictions.results", request.getPredictionId(), response);
        
        // Almacenar en BD
        predictionRepository.save(response);
    }
}
```

---

## 🎯 CONCLUSIÓN

El módulo **Serving** está completamente integrado con:

- ✅ **Models** - Gestión de modelos y versionado
- ✅ **Training** - Deploy desde experimentos y HPO
- ✅ **Monitoring** - Métricas en tiempo real y drift detection
- ✅ **Governance** - Compliance y auditoría
- ✅ **Analytics** - Performance y análisis de costos
- ✅ **Kubernetes** - Orchestration nativa
- ✅ **Message Queue** - Predicciones asíncronas

**Integración completa end-to-end para deployment y serving de modelos ML.**
