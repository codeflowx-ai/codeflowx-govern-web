# ⚖️ ETHICS - INTEGRACIÓN

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación de integración del módulo Ethics

---

## 🎯 RESUMEN EJECUTIVO

El módulo **Ethics** se integra con todos los componentes de la plataforma CodeflowX Govern para proporcionar evaluación ética continua, detección de sesgos y garantía de equidad en sistemas de IA.

**Integraciones Principales:**
- **Models:** Evaluación ética de modelos ML
- **Training:** Detección de sesgos en entrenamiento
- **Serving:** Monitoreo ético en producción
- **Governance:** Compliance ético y auditoría
- **Analytics:** Métricas éticas agregadas

---

## 🔗 INTEGRACIÓN CON MODELS

### **Evaluación Ética de Modelos**

```java
@Service
public class ModelEthicsIntegration {
    
    @Autowired
    private ModelService modelService;
    
    @Autowired
    private EthicalReviewService ethicalReviewService;
    
    @Autowired
    private BiasDetectionService biasDetectionService;
    
    /**
     * Evaluación ética automática al registrar modelo
     */
    @EventListener
    public void onModelRegistered(ModelRegisteredEvent event) {
        Model model = event.getModel();
        
        // 1. Solicitar revisión ética automática
        EthicalReview review = ethicalReviewService.createReview(
            EthicalReviewRequest.builder()
                .entityType(EntityType.MODEL)
                .entityId(model.getId())
                .reviewType(ReviewType.AUTOMATED)
                .priority(Priority.MEDIUM)
                .build()
        );
        
        // 2. Detectar sesgos si hay datos disponibles
        if (model.getTrainingDatasetId() != null) {
            BiasDetectionResult biasResult = biasDetectionService.detectBias(
                BiasDetectionRequest.builder()
                    .entityId(model.getId())
                    .datasetId(model.getTrainingDatasetId())
                    .sensitiveAttributes(getSensitiveAttributes(model))
                    .build()
            );
            
            // 3. Alertar si se detectan sesgos significativos
            if (biasResult.getBiasLevel() == BiasLevel.HIGH) {
                alertService.sendAlert(Alert.builder()
                    .type(AlertType.HIGH_BIAS_DETECTED)
                    .severity(AlertSeverity.HIGH)
                    .modelId(model.getId())
                    .message(String.format(
                        "High bias detected in model %s: %s",
                        model.getName(),
                        biasResult.getRecommendations()
                    ))
                    .build());
            }
        }
        
        // 4. Evaluar equidad
        FairnessAssessment fairness = fairnessService.assess(
            FairnessAssessmentRequest.builder()
                .entityId(model.getId())
                .assessmentType(AssessmentType.BASIC)
                .build()
        );
        
        // 5. Actualizar metadata del modelo
        model.setEthicalReviewId(review.getId());
        model.setBiasScore(biasResult != null ? biasResult.getOverallBiasScore() : null);
        model.setFairnessScore(fairness.getOverallFairnessScore());
        modelService.update(model);
    }
    
    /**
     * Validación ética antes de deployment
     */
    public void validateEthicsBeforeDeployment(Long modelId) {
        Model model = modelService.getModel(modelId);
        
        // Obtener última revisión ética
        EthicalReview latestReview = ethicalReviewService
            .getLatestReview(EntityType.MODEL, modelId);
        
        if (latestReview == null) {
            throw new EthicalValidationException(
                "Model has not been ethically reviewed"
            );
        }
        
        // Verificar que la revisión sea reciente (< 90 días)
        if (latestReview.getCompletedAt().isBefore(
            LocalDateTime.now().minusDays(90))) {
            throw new EthicalValidationException(
                "Ethical review is outdated, new review required"
            );
        }
        
        // Verificar aprobación
        if (latestReview.getRecommendation() == Recommendation.REJECTED) {
            throw new EthicalValidationException(
                "Model has been ethically rejected: " + 
                latestReview.getReviewerNotes()
            );
        }
        
        // Verificar bias score
        if (model.getBiasScore() != null && model.getBiasScore() < 6.0) {
            throw new EthicalValidationException(
                "Model bias score is below acceptable threshold (6.0): " +
                model.getBiasScore()
            );
        }
    }
}
```

---

## 🔗 INTEGRACIÓN CON TRAINING

### **Detección de Sesgos Durante Entrenamiento**

```java
@Service
public class TrainingEthicsIntegration {
    
    @Autowired
    private ExperimentService experimentService;
    
    @Autowired
    private BiasDetectionService biasDetectionService;
    
    /**
     * Análisis de sesgos durante experimentos
     */
    @EventListener
    public void onExperimentCompleted(ExperimentCompletedEvent event) {
        Experiment experiment = event.getExperiment();
        ExperimentRun bestRun = experimentService.getBestRun(experiment.getId());
        
        // Detectar sesgos en el mejor run
        BiasDetectionResult biasResult = biasDetectionService.detectBias(
            BiasDetectionRequest.builder()
                .entityType(EntityType.EXPERIMENT_RUN)
                .entityId(bestRun.getId())
                .datasetId(experiment.getTrainingDatasetId())
                .sensitiveAttributes(experiment.getSensitiveAttributes())
                .fairnessMetrics(Arrays.asList(
                    "demographic_parity",
                    "equalized_odds",
                    "equal_opportunity"
                ))
                .build()
        );
        
        // Guardar resultados en el experimento
        experiment.setBiasAnalysis(biasResult);
        experimentService.update(experiment);
        
        // Recomendar acciones si hay sesgos
        if (biasResult.getBiasLevel() != BiasLevel.LOW) {
            List<String> recommendations = generateBiasRecommendations(biasResult);
            notificationService.notifyExperimentOwner(
                experiment.getOwnerId(),
                "Bias detected in experiment: " + experiment.getName(),
                recommendations
            );
        }
    }
    
    /**
     * Validación ética de dataset antes de entrenamiento
     */
    public void validateDatasetEthics(Long datasetId) {
        Dataset dataset = datasetService.getDataset(datasetId);
        
        // Analizar representación de grupos
        RepresentationAnalysis representation = 
            analyzeGroupRepresentation(dataset);
        
        if (representation.hasSignificantImbalance()) {
            log.warn("Dataset {} has significant group imbalance: {}",
                datasetId, representation.getImbalanceReport());
            
            // Sugerir técnicas de balanceo
            List<String> suggestions = Arrays.asList(
                "Consider oversampling minority groups",
                "Apply SMOTE or similar techniques",
                "Use stratified sampling",
                "Apply reweighting techniques"
            );
            
            // Registrar en audit
            auditService.logDatasetImbalance(datasetId, representation, suggestions);
        }
    }
    
    /**
     * Fairness constraints durante entrenamiento
     */
    public TrainingConstraints getFairnessConstraints(Long experimentId) {
        Experiment experiment = experimentService.getExperiment(experimentId);
        
        // Obtener configuración de fairness
        FairnessConfig config = fairnessConfigService
            .getConfig(experiment.getProjectId());
        
        if (config == null || !config.isEnforceConstraints()) {
            return TrainingConstraints.none();
        }
        
        // Construir constraints basados en fairness requirements
        return TrainingConstraints.builder()
            .demographicParityConstraint(
                config.getDemographicParityThreshold()
            )
            .equalizedOddsConstraint(
                config.getEqualizedOddsThreshold()
            )
            .maxAllowedDisparity(
                config.getMaxAllowedDisparity()
            )
            .sensitiveAttributes(
                experiment.getSensitiveAttributes()
            )
            .build();
    }
}
```

---

## 🔗 INTEGRACIÓN CON SERVING

### **Monitoreo Ético en Producción**

```java
@Service
public class ServingEthicsIntegration {
    
    @Autowired
    private PredictionService predictionService;
    
    @Autowired
    private BiasMonitoringService biasMonitoringService;
    
    /**
     * Monitoreo de sesgos en predicciones en producción
     */
    @Scheduled(fixedRate = 3600000) // Cada hora
    public void monitorProductionBias() {
        List<Deployment> activeDeployments = deploymentService.getActiveDeployments();
        
        for (Deployment deployment : activeDeployments) {
            // Obtener predicciones de la última hora
            List<Prediction> predictions = predictionService.getRecentPredictions(
                deployment.getId(),
                Duration.ofHours(1)
            );
            
            if (predictions.size() < 100) {
                continue; // Datos insuficientes
            }
            
            // Analizar distribución de predicciones por grupos sensibles
            BiasMonitoringResult monitoring = biasMonitoringService.monitor(
                BiasMonitoringRequest.builder()
                    .deploymentId(deployment.getId())
                    .predictions(predictions)
                    .sensitiveAttributes(deployment.getSensitiveAttributes())
                    .build()
            );
            
            // Alertar si se detecta drift en bias
            if (monitoring.hasBiasDrift()) {
                alertService.sendAlert(Alert.builder()
                    .type(AlertType.BIAS_DRIFT_DETECTED)
                    .severity(AlertSeverity.HIGH)
                    .deploymentId(deployment.getId())
                    .message(String.format(
                        "Bias drift detected in deployment %s: %s",
                        deployment.getName(),
                        monitoring.getDriftSummary()
                    ))
                    .build());
            }
            
            // Guardar métricas de bias
            metricsService.recordBiasMetrics(
                deployment.getId(),
                monitoring.getBiasMetrics()
            );
        }
    }
    
    /**
     * Intervención ética en predicciones
     */
    @Around("@annotation(EthicalGuard)")
    public Object applyEthicalGuard(ProceedingJoinPoint joinPoint) throws Throwable {
        PredictionRequest request = (PredictionRequest) joinPoint.getArgs()[0];
        
        // 1. Verificar que el modelo tiene aprobación ética válida
        Deployment deployment = deploymentService.getDeployment(
            request.getDeploymentId()
        );
        
        EthicalReview review = ethicalReviewService.getLatestReview(
            EntityType.MODEL,
            deployment.getModelId()
        );
        
        if (review == null || !review.isApproved()) {
            throw new EthicalViolationException(
                "Model does not have valid ethical approval"
            );
        }
        
        // 2. Realizar predicción
        PredictionResponse response = (PredictionResponse) joinPoint.proceed();
        
        // 3. Aplicar fairness post-processing si está configurado
        if (deployment.isApplyFairnessPostProcessing()) {
            response = fairnessPostProcessor.process(
                response,
                deployment.getFairnessConfig()
            );
        }
        
        // 4. Registrar para auditoría ética
        auditService.logEthicalPrediction(
            deployment.getId(),
            request,
            response
        );
        
        return response;
    }
}
```

---

## 🔗 INTEGRACIÓN CON GOVERNANCE

### **Compliance Ético y Auditoría**

```java
@Service
public class GovernanceEthicsIntegration {
    
    @Autowired
    private GovernanceService governanceService;
    
    @Autowired
    private EthicalReviewService ethicalReviewService;
    
    /**
     * Verificación de compliance ético
     */
    public ComplianceResult verifyEthicalCompliance(Long modelId) {
        Model model = modelService.getModel(modelId);
        List<ComplianceViolation> violations = new ArrayList<>();
        
        // 1. Verificar revisión ética
        EthicalReview review = ethicalReviewService.getLatestReview(
            EntityType.MODEL, modelId
        );
        
        if (review == null) {
            violations.add(ComplianceViolation.builder()
                .type(ViolationType.MISSING_ETHICAL_REVIEW)
                .severity(ViolationSeverity.HIGH)
                .description("Model has not undergone ethical review")
                .build());
        } else if (!review.isApproved()) {
            violations.add(ComplianceViolation.builder()
                .type(ViolationType.ETHICAL_REVIEW_REJECTED)
                .severity(ViolationSeverity.CRITICAL)
                .description("Model ethical review was rejected")
                .build());
        }
        
        // 2. Verificar análisis de sesgos
        BiasDetectionResult biasResult = biasDetectionService
            .getLatestBiasAnalysis(modelId);
        
        if (biasResult != null && biasResult.getBiasLevel() == BiasLevel.HIGH) {
            violations.add(ComplianceViolation.builder()
                .type(ViolationType.HIGH_BIAS_DETECTED)
                .severity(ViolationSeverity.HIGH)
                .description("Model shows high levels of bias")
                .build());
        }
        
        // 3. Verificar fairness score
        if (model.getFairnessScore() != null && 
            model.getFairnessScore() < 7.0) {
            violations.add(ComplianceViolation.builder()
                .type(ViolationType.LOW_FAIRNESS_SCORE)
                .severity(ViolationSeverity.MEDIUM)
                .description("Model fairness score below threshold")
                .build());
        }
        
        // 4. Verificar análisis de impacto
        ImpactAnalysis impact = impactService.getLatestAnalysis(modelId);
        
        if (impact == null) {
            violations.add(ComplianceViolation.builder()
                .type(ViolationType.MISSING_IMPACT_ANALYSIS)
                .severity(ViolationSeverity.MEDIUM)
                .description("Model lacks impact analysis")
                .build());
        }
        
        return ComplianceResult.builder()
            .compliant(violations.isEmpty())
            .violations(violations)
            .checkedAt(LocalDateTime.now())
            .build();
    }
    
    /**
     * Generación de reporte ético para auditoría
     */
    public EthicalAuditReport generateEthicalAuditReport(
        Long modelId,
        LocalDate from,
        LocalDate to
    ) {
        Model model = modelService.getModel(modelId);
        
        // Recopilar toda la información ética
        List<EthicalReview> reviews = ethicalReviewService.getReviews(
            EntityType.MODEL, modelId, from, to
        );
        
        List<BiasDetectionResult> biasAnalyses = biasDetectionService
            .getBiasHistory(modelId, from, to);
        
        List<FairnessAssessment> fairnessAssessments = fairnessService
            .getAssessmentHistory(modelId, from, to);
        
        ImpactAnalysis impactAnalysis = impactService.getLatestAnalysis(modelId);
        
        // Generar reporte comprehensivo
        return EthicalAuditReport.builder()
            .model(model)
            .reportPeriod(new DateRange(from, to))
            .ethicalReviews(reviews)
            .biasAnalyses(biasAnalyses)
            .fairnessAssessments(fairnessAssessments)
            .impactAnalysis(impactAnalysis)
            .overallEthicalScore(calculateOverallEthicalScore(
                reviews, biasAnalyses, fairnessAssessments, impactAnalysis
            ))
            .recommendations(generateEthicalRecommendations(
                reviews, biasAnalyses, fairnessAssessments
            ))
            .generatedAt(LocalDateTime.now())
            .build();
    }
}
```

---

## 🔗 INTEGRACIÓN CON ANALYTICS

### **Métricas Éticas Agregadas**

```java
@Service
public class AnalyticsEthicsIntegration {
    
    @Autowired
    private AnalyticsService analyticsService;
    
    @Autowired
    private MetricsCollector metricsCollector;
    
    /**
     * Publicar métricas éticas en analytics
     */
    @Async
    public void publishEthicalMetrics(EthicalReview review) {
        if (review.isCompleted()) {
            // Publicar scores individuales
            Map<String, Double> scores = Map.of(
                "fairness_score", review.getEthicalAssessment().getFairness(),
                "transparency_score", review.getEthicalAssessment().getTransparency(),
                "accountability_score", review.getEthicalAssessment().getAccountability(),
                "privacy_score", review.getEthicalAssessment().getPrivacy(),
                "safety_score", review.getEthicalAssessment().getSafety(),
                "overall_ethical_score", review.getOverallScore()
            );
            
            for (Map.Entry<String, Double> entry : scores.entrySet()) {
                metricsCollector.record(MetricData.builder()
                    .entityType(review.getEntityType())
                    .entityId(review.getEntityId())
                    .metricName(entry.getKey())
                    .metricValue(entry.getValue())
                    .timestamp(review.getCompletedAt())
                    .tags(Map.of(
                        "review_id", review.getId().toString(),
                        "review_type", review.getReviewType().name()
                    ))
                    .build());
            }
            
            // Actualizar agregaciones
            analyticsService.updateEthicalMetrics(
                review.getEntityType(),
                review.getEntityId(),
                scores
            );
        }
    }
    
    /**
     * Dashboard de métricas éticas
     */
    public EthicalMetricsDashboard getEthicalDashboard() {
        // Obtener métricas de todos los modelos activos
        List<Model> activeModels = modelService.getActiveModels();
        
        Map<String, Double> aggregatedScores = new HashMap<>();
        List<EthicalIssue> criticalIssues = new ArrayList<>();
        
        for (Model model : activeModels) {
            EthicalReview latestReview = ethicalReviewService
                .getLatestReview(EntityType.MODEL, model.getId());
            
            if (latestReview != null && latestReview.isCompleted()) {
                // Agregar scores
                EthicalAssessment assessment = latestReview.getEthicalAssessment();
                aggregatedScores.merge("fairness", assessment.getFairness(), Double::sum);
                aggregatedScores.merge("transparency", assessment.getTransparency(), Double::sum);
                aggregatedScores.merge("accountability", assessment.getAccountability(), Double::sum);
                
                // Identificar issues críticos
                if (latestReview.getOverallScore() < 6.0) {
                    criticalIssues.add(EthicalIssue.builder()
                        .modelId(model.getId())
                        .modelName(model.getName())
                        .issueType("LOW_ETHICAL_SCORE")
                        .severity(IssueSeverity.HIGH)
                        .score(latestReview.getOverallScore())
                        .build());
                }
            }
        }
        
        // Calcular promedios
        int modelCount = activeModels.size();
        aggregatedScores.replaceAll((k, v) -> v / modelCount);
        
        return EthicalMetricsDashboard.builder()
            .totalModels(modelCount)
            .aggregatedScores(aggregatedScores)
            .criticalIssues(criticalIssues)
            .refreshedAt(LocalDateTime.now())
            .build();
    }
}
```

---

## 🔗 INTEGRACIÓN CON EXTERNAL SYSTEMS

### **Integración con Fairness Toolkits**

```java
@Service
public class FairnessToolkitIntegration {
    
    @Autowired
    private PythonExecutor pythonExecutor;
    
    /**
     * Integración con AI Fairness 360 (IBM)
     */
    public BiasMetrics analyzeWithAIF360(
        Dataset dataset,
        List<String> protectedAttributes
    ) {
        String pythonScript = """
            from aif360.datasets import BinaryLabelDataset
            from aif360.metrics import BinaryLabelDatasetMetric
            import pandas as pd
            import json
            
            # Cargar dataset
            df = pd.read_csv('data.csv')
            
            # Crear AIF360 dataset
            dataset = BinaryLabelDataset(
                df=df,
                label_names=['target'],
                protected_attribute_names=%s
            )
            
            # Calcular métricas
            metric = BinaryLabelDatasetMetric(
                dataset,
                unprivileged_groups=[{'gender': 0}],
                privileged_groups=[{'gender': 1}]
            )
            
            results = {
                'disparate_impact': metric.disparate_impact(),
                'statistical_parity_difference': metric.statistical_parity_difference(),
                'consistency': metric.consistency()
            }
            
            print(json.dumps(results))
            """.formatted(protectedAttributes);
        
        String output = pythonExecutor.execute(pythonScript, dataset.toCsv());
        return parseBiasMetrics(output);
    }
    
    /**
     * Integración con Fairlearn (Microsoft)
     */
    public FairnessMetrics analyzeWithFairlearn(
        Model model,
        Dataset testData
    ) {
        // Similar implementation con Fairlearn
        return fairlearnClient.analyze(model, testData);
    }
}
```

---

## 🎯 CONCLUSIÓN

El módulo **Ethics** está completamente integrado con:

- ✅ **Models** - Evaluación ética automática
- ✅ **Training** - Detección de sesgos en experimentos
- ✅ **Serving** - Monitoreo ético en producción
- ✅ **Governance** - Compliance y auditoría
- ✅ **Analytics** - Métricas éticas agregadas
- ✅ **External Tools** - AIF360, Fairlearn

**Integración completa end-to-end para garantía ética de sistemas AI.**

