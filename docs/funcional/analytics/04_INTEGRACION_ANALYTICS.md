# 📊 ANALYTICS - INTEGRACIÓN

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación de integración del módulo Analytics

---

## 🎯 RESUMEN EJECUTIVO

El módulo **Analytics** se integra con todos los componentes de la plataforma CodeflowX Govern para proporcionar análisis completo, métricas en tiempo real y reportes automatizados.

**Integraciones Principales:**
- **Models:** Métricas de performance de modelos
- **Training:** Análisis de experimentos y HPO
- **Serving:** Métricas de inferencia
- **Monitoring:** Agregación de métricas operacionales
- **Governance:** Analytics de compliance
- **RAG:** Métricas de calidad RAG

---

## 🔗 INTEGRACIÓN CON MODELS

### **Métricas de Performance de Modelos**

```java
@Service
public class ModelAnalyticsIntegration {
    
    @Autowired
    private ModelService modelService;
    
    @Autowired
    private AnalyticsService analyticsService;
    
    @Autowired
    private MetricsCollector metricsCollector;
    
    /**
     * Registra métricas de evaluación de modelo
     */
    public void recordModelEvaluation(Long modelId, EvaluationResult evaluation) {
        Model model = modelService.getModel(modelId);
        
        // Registrar métricas de performance
        Map<String, Double> performanceMetrics = Map.of(
            "accuracy", evaluation.getAccuracy(),
            "precision", evaluation.getPrecision(),
            "recall", evaluation.getRecall(),
            "f1_score", evaluation.getF1Score(),
            "auc", evaluation.getAuc()
        );
        
        for (Map.Entry<String, Double> entry : performanceMetrics.entrySet()) {
            MetricData metric = MetricData.builder()
                .entityType(EntityType.MODEL)
                .entityId(modelId)
                .metricName(entry.getKey())
                .metricValue(entry.getValue())
                .timestamp(LocalDateTime.now())
                .tags(Map.of(
                    "model_name", model.getName(),
                    "model_version", model.getVersion(),
                    "evaluation_id", evaluation.getId().toString()
                ))
                .build();
                
            metricsCollector.record(metric);
        }
        
        // Actualizar tendencias
        analyticsService.updateTrends(modelId, performanceMetrics);
    }
    
    /**
     * Genera reporte comparativo de modelos
     */
    public ModelComparisonReport compareModels(List<Long> modelIds, String timeRange) {
        List<Model> models = modelService.getModels(modelIds);
        
        // Obtener métricas de cada modelo
        Map<Long, ModelMetrics> metricsMap = new HashMap<>();
        
        for (Model model : models) {
            ModelMetrics metrics = analyticsService.getModelMetrics(
                model.getId(),
                timeRange
            );
            metricsMap.put(model.getId(), metrics);
        }
        
        // Comparar y generar reporte
        return ModelComparisonReport.builder()
            .models(models)
            .metrics(metricsMap)
            .comparison(calculateComparison(metricsMap))
            .recommendation(generateRecommendation(metricsMap))
            .generatedAt(LocalDateTime.now())
            .build();
    }
    
    /**
     * Análisis de tendencias de modelo
     */
    @Scheduled(fixedRate = 3600000) // Cada hora
    public void analyzeModelTrends() {
        List<Model> activeModels = modelService.getActiveModels();
        
        for (Model model : activeModels) {
            // Obtener métricas históricas
            List<MetricData> historicalMetrics = metricsCollector
                .getHistoricalMetrics(
                    EntityType.MODEL,
                    model.getId(),
                    Duration.ofDays(30)
                );
            
            // Analizar tendencias
            TrendAnalysis trends = analyticsService.analyzeTrends(historicalMetrics);
            
            // Alertar si hay degradación
            if (trends.isPerformanceDegrading()) {
                alertService.sendAlert(Alert.builder()
                    .type(AlertType.MODEL_PERFORMANCE_DEGRADATION)
                    .severity(AlertSeverity.HIGH)
                    .modelId(model.getId())
                    .message(String.format(
                        "Model %s showing performance degradation: %.2f%% decline",
                        model.getName(),
                        trends.getDegradationPercent()
                    ))
                    .build());
            }
        }
    }
}
```

---

## 🔗 INTEGRACIÓN CON TRAINING

### **Analytics de Experimentos**

```java
@Service
public class TrainingAnalyticsIntegration {
    
    @Autowired
    private ExperimentService experimentService;
    
    @Autowired
    private AnalyticsService analyticsService;
    
    /**
     * Analiza resultados de experimentos
     */
    public ExperimentAnalysis analyzeExperiment(Long experimentId) {
        Experiment experiment = experimentService.getExperiment(experimentId);
        List<ExperimentRun> runs = experimentService.getRuns(experimentId);
        
        // Calcular estadísticas
        ExperimentStatistics stats = ExperimentStatistics.builder()
            .totalRuns(runs.size())
            .successfulRuns(runs.stream().filter(r -> r.getStatus() == RunStatus.COMPLETED).count())
            .avgAccuracy(calculateAvgMetric(runs, "accuracy"))
            .bestAccuracy(calculateBestMetric(runs, "accuracy"))
            .avgTrainingTime(calculateAvgTrainingTime(runs))
            .build();
        
        // Analizar convergencia
        ConvergenceAnalysis convergence = analyzeConvergence(runs);
        
        // Identificar mejores hiperparámetros
        Map<String, Object> bestHyperparameters = identifyBestHyperparameters(runs);
        
        // Registrar en analytics
        analyticsService.recordExperimentAnalysis(
            experimentId,
            stats,
            convergence,
            bestHyperparameters
        );
        
        return ExperimentAnalysis.builder()
            .experiment(experiment)
            .statistics(stats)
            .convergence(convergence)
            .bestHyperparameters(bestHyperparameters)
            .build();
    }
    
    /**
     * Análisis de HPO
     */
    public HpoAnalysis analyzeHpoJob(Long hpoJobId) {
        HpoJob hpoJob = hpoService.getHpoJob(hpoJobId);
        List<HpoTrial> trials = hpoService.getTrials(hpoJobId);
        
        // Analizar espacio de búsqueda
        SearchSpaceAnalysis searchSpace = analyzeSearchSpace(trials);
        
        // Identificar regiones prometedoras
        List<PromisingRegion> promisingRegions = identifyPromisingRegions(trials);
        
        // Calcular efficiency score
        double efficiencyScore = calculateHpoEfficiency(trials);
        
        return HpoAnalysis.builder()
            .hpoJob(hpoJob)
            .searchSpaceAnalysis(searchSpace)
            .promisingRegions(promisingRegions)
            .efficiencyScore(efficiencyScore)
            .recommendation(generateHpoRecommendation(trials))
            .build();
    }
}
```

---

## 🔗 INTEGRACIÓN CON SERVING

### **Analytics de Inferencia**

```java
@Service
public class ServingAnalyticsIntegration {
    
    @Autowired
    private PredictionService predictionService;
    
    @Autowired
    private AnalyticsService analyticsService;
    
    /**
     * Analiza métricas de inferencia en tiempo real
     */
    @Async
    public void analyzePredictionMetrics(Prediction prediction) {
        // Registrar latencia
        metricsCollector.record(MetricData.builder()
            .entityType(EntityType.DEPLOYMENT)
            .entityId(prediction.getDeploymentId())
            .metricName("prediction_latency")
            .metricValue(prediction.getLatencyMs())
            .timestamp(prediction.getTimestamp())
            .tags(Map.of(
                "model_id", prediction.getModelId().toString(),
                "model_version", prediction.getModelVersion()
            ))
            .build());
        
        // Actualizar estadísticas en tiempo real
        analyticsService.updateRealtimeStats(
            prediction.getDeploymentId(),
            Map.of(
                "total_predictions", 1L,
                "avg_latency", prediction.getLatencyMs(),
                "errors", prediction.getError() != null ? 1L : 0L
            )
        );
    }
    
    /**
     * Genera reporte de serving performance
     */
    public ServingPerformanceReport generateServingReport(
        Long deploymentId,
        LocalDate from,
        LocalDate to
    ) {
        // Obtener predicciones del período
        List<Prediction> predictions = predictionService.getPredictions(
            deploymentId, from, to
        );
        
        // Calcular métricas agregadas
        ServingMetrics metrics = ServingMetrics.builder()
            .totalPredictions(predictions.size())
            .avgLatency(calculateAvgLatency(predictions))
            .p50Latency(calculatePercentile(predictions, 50))
            .p95Latency(calculatePercentile(predictions, 95))
            .p99Latency(calculatePercentile(predictions, 99))
            .errorRate(calculateErrorRate(predictions))
            .throughput(calculateThroughput(predictions, from, to))
            .build();
        
        // Analizar patrones temporales
        TemporalPatterns patterns = analyzeTemporalPatterns(predictions);
        
        // Detectar anomalías
        List<Anomaly> anomalies = detectAnomalies(predictions);
        
        return ServingPerformanceReport.builder()
            .deploymentId(deploymentId)
            .dateRange(new DateRange(from, to))
            .metrics(metrics)
            .temporalPatterns(patterns)
            .anomalies(anomalies)
            .build();
    }
}
```

---

## 🔗 INTEGRACIÓN CON MONITORING

### **Agregación de Métricas Operacionales**

```java
@Service
public class MonitoringAnalyticsIntegration {
    
    @Autowired
    private MonitoringService monitoringService;
    
    @Autowired
    private AnalyticsService analyticsService;
    
    /**
     * Agrega métricas de múltiples fuentes
     */
    @Scheduled(fixedRate = 300000) // Cada 5 minutos
    public void aggregateMetrics() {
        // Obtener métricas de monitoring
        List<MetricData> monitoringMetrics = monitoringService
            .getRecentMetrics(Duration.ofMinutes(5));
        
        // Agrupar por entidad
        Map<EntityKey, List<MetricData>> groupedMetrics = monitoringMetrics.stream()
            .collect(Collectors.groupingBy(
                m -> new EntityKey(m.getEntityType(), m.getEntityId())
            ));
        
        // Calcular agregaciones
        for (Map.Entry<EntityKey, List<MetricData>> entry : groupedMetrics.entrySet()) {
            EntityKey key = entry.getKey();
            List<MetricData> metrics = entry.getValue();
            
            // Calcular estadísticas agregadas
            MetricAggregation aggregation = MetricAggregation.builder()
                .entityType(key.getEntityType())
                .entityId(key.getEntityId())
                .timeWindow(Duration.ofMinutes(5))
                .metrics(calculateAggregations(metrics))
                .timestamp(LocalDateTime.now())
                .build();
            
            // Guardar en analytics
            analyticsService.saveAggregation(aggregation);
            
            // Verificar umbrales
            checkThresholds(key, aggregation);
        }
    }
    
    /**
     * Análisis de salud del sistema
     */
    public SystemHealthAnalysis analyzeSystemHealth() {
        // Obtener métricas de todos los componentes
        Map<String, ComponentHealth> componentHealth = Map.of(
            "models", analyzeModelsHealth(),
            "serving", analyzeServingHealth(),
            "training", analyzeTrainingHealth(),
            "rag", analyzeRagHealth()
        );
        
        // Calcular health score global
        double globalHealthScore = componentHealth.values().stream()
            .mapToDouble(ComponentHealth::getHealthScore)
            .average()
            .orElse(0.0);
        
        // Identificar issues críticos
        List<HealthIssue> criticalIssues = componentHealth.values().stream()
            .flatMap(c -> c.getIssues().stream())
            .filter(i -> i.getSeverity() == IssueSeverity.CRITICAL)
            .collect(Collectors.toList());
        
        return SystemHealthAnalysis.builder()
            .componentHealth(componentHealth)
            .globalHealthScore(globalHealthScore)
            .criticalIssues(criticalIssues)
            .recommendations(generateHealthRecommendations(componentHealth))
            .analyzedAt(LocalDateTime.now())
            .build();
    }
}
```

---

## 🔗 INTEGRACIÓN CON GOVERNANCE

### **Analytics de Compliance**

```java
@Service
public class GovernanceAnalyticsIntegration {
    
    @Autowired
    private GovernanceService governanceService;
    
    @Autowired
    private AnalyticsService analyticsService;
    
    /**
     * Analiza compliance metrics
     */
    public ComplianceAnalytics analyzeCompliance() {
        // Obtener evaluaciones de compliance
        List<ComplianceEvaluation> evaluations = governanceService
            .getRecentEvaluations(Duration.ofDays(30));
        
        // Calcular compliance rate por framework
        Map<String, Double> complianceRates = evaluations.stream()
            .collect(Collectors.groupingBy(
                ComplianceEvaluation::getFramework,
                Collectors.averagingDouble(e -> e.getScore() / 100.0)
            ));
        
        // Analizar tendencias
        Map<String, TrendDirection> trends = analyzeTrends(evaluations);
        
        // Identificar áreas de riesgo
        List<RiskArea> riskAreas = identifyRiskAreas(evaluations);
        
        return ComplianceAnalytics.builder()
            .complianceRates(complianceRates)
            .trends(trends)
            .riskAreas(riskAreas)
            .totalEvaluations(evaluations.size())
            .avgComplianceScore(calculateAvgScore(evaluations))
            .build();
    }
}
```

---

## 🔗 INTEGRACIÓN CON RAG

### **Analytics de Calidad RAG**

```java
@Service
public class RagAnalyticsIntegration {
    
    @Autowired
    private RagService ragService;
    
    @Autowired
    private AnalyticsService analyticsService;
    
    /**
     * Analiza métricas de calidad RAG
     */
    public RagQualityAnalytics analyzeRagQuality(Long ragSystemId) {
        // Obtener queries recientes
        List<RagQuery> queries = ragService.getRecentQueries(
            ragSystemId,
            Duration.ofDays(7)
        );
        
        // Calcular métricas de calidad
        RagQualityMetrics metrics = RagQualityMetrics.builder()
            .totalQueries(queries.size())
            .avgRelevanceScore(calculateAvgRelevance(queries))
            .avgContextQuality(calculateAvgContextQuality(queries))
            .avgResponseTime(calculateAvgResponseTime(queries))
            .successRate(calculateSuccessRate(queries))
            .build();
        
        // Analizar patrones de uso
        UsagePatterns patterns = analyzeUsagePatterns(queries);
        
        // Identificar mejoras
        List<Improvement> improvements = identifyImprovements(queries, metrics);
        
        return RagQualityAnalytics.builder()
            .ragSystemId(ragSystemId)
            .metrics(metrics)
            .usagePatterns(patterns)
            .improvements(improvements)
            .build();
    }
}
```

---

## 🔗 INTEGRACIÓN CON BI TOOLS

### **Exportación a Tableau/PowerBI**

```java
@Service
public class BIIntegrationService {
    
    @Autowired
    private AnalyticsService analyticsService;
    
    /**
     * Exporta datos para Tableau
     */
    public void exportToTableau(ExportRequest request) {
        // Obtener datos
        List<MetricData> data = analyticsService.getMetrics(
            request.getEntityType(),
            request.getEntityIds(),
            request.getDateRange()
        );
        
        // Transformar a formato Tableau
        TableauDataset dataset = TableauDataset.builder()
            .columns(defineColumns())
            .rows(transformToRows(data))
            .metadata(generateMetadata(request))
            .build();
        
        // Publicar en Tableau Server
        tableauClient.publishDataset(
            request.getWorkbookName(),
            dataset
        );
    }
    
    /**
     * Conectar con PowerBI
     */
    public void setupPowerBIConnection() {
        // Configurar DirectQuery
        PowerBIDataSource dataSource = PowerBIDataSource.builder()
            .name("CodeflowX Analytics")
            .connectionString(buildConnectionString())
            .refreshSchedule(RefreshSchedule.HOURLY)
            .build();
        
        powerBIClient.createDataSource(dataSource);
    }
}
```

---

## 🎯 CONCLUSIÓN

El módulo **Analytics** está completamente integrado con:

- ✅ **Models** - Performance y tendencias
- ✅ **Training** - Experimentos y HPO
- ✅ **Serving** - Métricas de inferencia
- ✅ **Monitoring** - Agregación operacional
- ✅ **Governance** - Compliance analytics
- ✅ **RAG** - Calidad y uso
- ✅ **BI Tools** - Tableau, PowerBI

**Integración completa end-to-end para análisis y reporting unificado.**

