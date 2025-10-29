# 🔌 API & SDK - MÓDULO EVALUACIÓN

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación de APIs y SDK del módulo evaluación

---

## 🎯 RESUMEN EJECUTIVO

El módulo **evaluación** proporciona **APIs REST** y **SDK** para la gestión de evaluaciones de modelos, detección de sesgos, análisis de fairness y métricas de calidad, incluyendo integración con BPMN y sistemas externos.

---

## 📋 ENDPOINTS REST API

### **1. Model Evaluation**

#### **GET /api/evaluation/models**
- **Descripción:** Obtener lista de evaluaciones de modelos
- **Parámetros:**
  - `modelType` (opcional): Tipo de modelo (LLM, GENERAL, RAG)
  - `status` (opcional): Estado de la evaluación (PENDING, COMPLETED, FAILED)
  - `page` (opcional): Número de página
  - `size` (opcional): Tamaño de página
- **Respuesta:**
```json
{
  "content": [
    {
      "id": "eval-001",
      "modelId": "model-001",
      "modelType": "LLM",
      "status": "COMPLETED",
      "overallScore": 85.5,
      "accuracy": 0.92,
      "precision": 0.89,
      "recall": 0.91,
      "f1Score": 0.90,
      "createdDate": "2025-10-25T10:00:00Z",
      "completedDate": "2025-10-25T11:30:00Z"
    }
  ],
  "totalElements": 150,
  "totalPages": 15
}
```

#### **POST /api/evaluation/models**
- **Descripción:** Crear nueva evaluación de modelo
- **Body:**
```json
{
  "modelId": "model-001",
  "modelType": "LLM",
  "evaluationType": "COMPREHENSIVE",
  "metrics": ["accuracy", "precision", "recall", "f1Score"],
  "biasDetection": true,
  "fairnessAnalysis": true
}
```

#### **GET /api/evaluation/models/{id}**
- **Descripción:** Obtener detalle de evaluación específica
- **Respuesta:**
```json
{
  "id": "eval-001",
  "modelId": "model-001",
  "modelType": "LLM",
  "status": "COMPLETED",
  "overallScore": 85.5,
  "performanceMetrics": {
    "accuracy": 0.92,
    "precision": 0.89,
    "recall": 0.91,
    "f1Score": 0.90,
    "aucRoc": 0.94,
    "aucPr": 0.88
  },
  "biasDetections": [
    {
      "biasType": "DEMOGRAPHIC_PARITY",
      "biasScore": 0.15,
      "isBiased": true,
      "affectedGroups": ["gender", "age"],
      "recommendations": ["Balance dataset", "Use fairness constraints"]
    }
  ],
  "fairnessMetrics": [
    {
      "metricType": "EQUALIZED_ODDS",
      "metricValue": 0.85,
      "isFair": true,
      "groupAValue": 0.87,
      "groupBValue": 0.83,
      "difference": 0.04
    }
  ]
}
```

### **2. Bias Detection**

#### **GET /api/evaluation/bias-detection**
- **Descripción:** Obtener detecciones de sesgos
- **Parámetros:**
  - `modelId` (opcional): ID del modelo
  - `biasType` (opcional): Tipo de sesgo
  - `severity` (opcional): Severidad (LOW, MEDIUM, HIGH, CRITICAL)
- **Respuesta:**
```json
{
  "content": [
    {
      "id": "bias-001",
      "modelId": "model-001",
      "biasType": "DEMOGRAPHIC_PARITY",
      "biasScore": 0.15,
      "severity": "HIGH",
      "isBiased": true,
      "affectedGroups": ["gender", "age"],
      "description": "Model shows bias against certain demographic groups",
      "detectedDate": "2025-10-25T10:00:00Z"
    }
  ]
}
```

#### **POST /api/evaluation/bias-detection/{id}/recommendations**
- **Descripción:** Generar recomendaciones para sesgo detectado
- **Body:**
```json
{
  "biasId": "bias-001",
  "priority": "HIGH",
  "implementationEffort": "MEDIUM"
}
```

### **3. Fairness Metrics**

#### **GET /api/evaluation/fairness-metrics**
- **Descripción:** Obtener métricas de fairness
- **Parámetros:**
  - `modelId` (opcional): ID del modelo
  - `metricType` (opcional): Tipo de métrica
- **Respuesta:**
```json
{
  "content": [
    {
      "id": "fairness-001",
      "modelId": "model-001",
      "metricType": "EQUALIZED_ODDS",
      "metricValue": 0.85,
      "thresholdValue": 0.8,
      "isFair": true,
      "groupAValue": 0.87,
      "groupBValue": 0.83,
      "difference": 0.04,
      "calculatedDate": "2025-10-25T10:00:00Z"
    }
  ]
}
```

### **4. Evaluation Reports**

#### **GET /api/evaluation/reports/comprehensive**
- **Descripción:** Generar reporte comprensivo de evaluación
- **Parámetros:**
  - `modelId` (requerido): ID del modelo
  - `includeBias` (opcional): Incluir análisis de sesgos
  - `includeFairness` (opcional): Incluir análisis de fairness
- **Respuesta:**
```json
{
  "modelId": "model-001",
  "reportDate": "2025-10-25T10:00:00Z",
  "summary": {
    "overallScore": 85.5,
    "performanceGrade": "B+",
    "biasLevel": "LOW",
    "fairnessLevel": "HIGH",
    "complianceStatus": "COMPLIANT"
  },
  "performanceMetrics": {
    "accuracy": 0.92,
    "precision": 0.89,
    "recall": 0.91,
    "f1Score": 0.90
  },
  "biasAnalysis": {
    "totalBiases": 2,
    "criticalBiases": 0,
    "highBiases": 1,
    "mediumBiases": 1,
    "lowBiases": 0
  },
  "fairnessAnalysis": {
    "overallFairness": 0.85,
    "fairMetrics": 8,
    "unfairMetrics": 2
  },
  "recommendations": [
    {
      "type": "BIAS_MITIGATION",
      "priority": "HIGH",
      "description": "Implement demographic parity constraints",
      "expectedImpact": "MEDIUM"
    }
  ]
}
```

---

## 🔧 SDK JAVA

### **1. EvaluationService**

```java
@Service
public class EvaluationService {
    
    /**
     * Ejecutar evaluación completa de modelo
     */
    public ModelEvaluationResult evaluateModel(
        String modelId, 
        EvaluationType evaluationType,
        List<String> metrics) {
        
        ModelEvaluation evaluation = ModelEvaluation.builder()
            .modelId(modelId)
            .evaluationType(evaluationType)
            .metrics(metrics)
            .status(EvaluationStatus.PENDING)
            .build();
            
        return evaluationEngine.executeEvaluation(evaluation);
    }
    
    /**
     * Detectar sesgos en modelo
     */
    public BiasDetectionResult detectBias(String modelId, List<BiasType> biasTypes) {
        return biasDetectionService.detectModelBias(modelId, biasTypes);
    }
    
    /**
     * Calcular métricas de fairness
     */
    public FairnessMetricsResult calculateFairnessMetrics(
        String modelId, 
        List<FairnessMetricType> metricTypes) {
        
        return fairnessService.calculateMetrics(modelId, metricTypes);
    }
    
    /**
     * Generar reporte de evaluación
     */
    public EvaluationReport generateReport(
        String modelId, 
        boolean includeBias, 
        boolean includeFairness) {
        
        return reportService.generateComprehensiveReport(
            modelId, includeBias, includeFairness);
    }
}
```

### **2. ModelEvaluation**

```java
@Entity
@Table(name = "model_evaluation")
public class ModelEvaluation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "evaluation_id", unique = true)
    private String evaluationId;
    
    @Column(name = "model_id")
    private String modelId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "model_type")
    private ModelType modelType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "evaluation_type")
    private EvaluationType evaluationType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EvaluationStatus status;
    
    @Column(name = "overall_score")
    private Double overallScore;
    
    @Column(name = "accuracy")
    private Double accuracy;
    
    @Column(name = "precision_score")
    private Double precisionScore;
    
    @Column(name = "recall_score")
    private Double recallScore;
    
    @Column(name = "f1_score")
    private Double f1Score;
    
    @Column(name = "auc_roc")
    private Double aucRoc;
    
    @Column(name = "auc_pr")
    private Double aucPr;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    
    @Column(name = "completed_date")
    private LocalDateTime completedDate;
    
    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL)
    private List<BiasDetection> biasDetections;
    
    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL)
    private List<FairnessMetric> fairnessMetrics;
    
    // Getters y setters
}
```

### **3. BiasDetectionService**

```java
@Service
public class BiasDetectionService {
    
    /**
     * Detectar sesgos en modelo
     */
    public BiasDetectionResult detectModelBias(String modelId, List<BiasType> biasTypes) {
        Model model = modelService.getModel(modelId);
        
        List<BiasDetection> detections = new ArrayList<>();
        
        for (BiasType biasType : biasTypes) {
            BiasDetection detection = detectSpecificBias(model, biasType);
            if (detection != null) {
                detections.add(detection);
            }
        }
        
        return BiasDetectionResult.builder()
            .modelId(modelId)
            .detections(detections)
            .totalDetections(detections.size())
            .criticalDetections(detections.stream()
                .filter(d -> d.getSeverity() == BiasSeverity.CRITICAL)
                .count())
            .build();
    }
    
    /**
     * Detectar sesgo específico
     */
    private BiasDetection detectSpecificBias(Model model, BiasType biasType) {
        // Implementación específica por tipo de sesgo
        switch (biasType) {
            case DEMOGRAPHIC_PARITY:
                return detectDemographicParityBias(model);
            case EQUALIZED_ODDS:
                return detectEqualizedOddsBias(model);
            case CALIBRATION:
                return detectCalibrationBias(model);
            default:
                return null;
        }
    }
}
```

---

## 🔄 INTEGRACIÓN CON BPMN

### **1. Evaluation Process Integration**

```java
@Component
public class EvaluationProcessIntegration {
    
    @Autowired
    private EvaluationService evaluationService;
    
    @Autowired
    private ProcessEngine processEngine;
    
    /**
     * Iniciar proceso de evaluación
     */
    public String startEvaluationProcess(String modelId, String modelType) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("modelId", modelId);
        variables.put("modelType", modelType);
        variables.put("startedBy", getCurrentUser());
        
        ProcessInstance processInstance = processEngine.getRuntimeService()
            .startProcessInstanceByKey("model-evaluation-v1", variables);
        
        return processInstance.getId();
    }
    
    /**
     * Completar evaluación en proceso BPMN
     */
    public void completeEvaluationInProcess(String processInstanceId, 
                                          ModelEvaluationResult result) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("evaluationScore", result.getOverallScore());
        variables.put("biasDetected", result.hasBias());
        variables.put("fairnessScore", result.getFairnessScore());
        variables.put("performanceMetrics", result.getPerformanceMetrics());
        
        processEngine.getRuntimeService()
            .setVariables(processInstanceId, variables);
    }
}
```

---

## 📊 MÉTRICAS Y MONITOREO

### **1. Evaluation Metrics**

```java
@Component
public class EvaluationMetricsService {
    
    /**
     * Calcular métricas de evaluación por período
     */
    public EvaluationMetrics calculateMetrics(LocalDate from, LocalDate to) {
        List<ModelEvaluation> evaluations = evaluationRepository
            .findByDateRange(from, to);
        
        return EvaluationMetrics.builder()
            .totalEvaluations(evaluations.size())
            .averageScore(evaluations.stream()
                .mapToDouble(ModelEvaluation::getOverallScore)
                .average()
                .orElse(0.0))
            .biasDetectionRate(evaluations.stream()
                .mapToDouble(e -> e.getBiasDetections().size())
                .average()
                .orElse(0.0))
            .fairnessComplianceRate(evaluations.stream()
                .mapToDouble(e -> e.getFairnessMetrics().stream()
                    .mapToDouble(FairnessMetric::getMetricValue)
                    .average()
                    .orElse(0.0))
                .average()
                .orElse(0.0))
            .build();
    }
}
```

---

## 🎯 BENEFICIOS DE LA API/SDK

### **Para Desarrolladores:**
- **APIs REST** estándar para integración
- **SDK Java** completo para desarrollo interno
- **Integración BPMN** automatizada
- **Métricas** en tiempo real

### **Para Data Scientists:**
- **Evaluación automatizada** de modelos
- **Detección proactiva** de sesgos
- **Métricas estandarizadas** de calidad
- **Reportes** detallados

### **Para Compliance Officers:**
- **Evaluación continua** de compliance
- **Detección automática** de problemas
- **Reportes regulatorios** automatizados
- **Auditoría** simplificada

### **Para la Organización:**
- **Calidad garantizada** de modelos
- **Compliance** regulatorio
- **Reducción de riesgos**
- **Auditoría** simplificada

---

## 🎯 CONCLUSIÓN

El módulo Evaluación proporciona **APIs y SDK robustos** que permiten:

- 🔌 **Integración** con sistemas externos
- 🤖 **Automatización** de evaluaciones
- 📊 **Monitoreo** en tiempo real
- 📋 **Gestión** centralizada de evaluaciones
- 📈 **Reportes** automatizados

**Esta API/SDK está diseñada** para facilitar la evaluación de modelos y garantizar la calidad, equidad y compliance en sistemas de IA.
