# 🔌 API & SDK - MÓDULO TRAINING

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación de APIs y SDK del módulo training

---

## 🎯 RESUMEN EJECUTIVO

El módulo **training** proporciona **APIs REST** y **SDK** para la gestión de experimentos, optimización de hiperparámetros (HPO), tracking de métricas, gestión de artefactos y governance de entrenamiento, superando ampliamente las capacidades de MLflow con funcionalidades empresariales avanzadas.

---

## 📋 ENDPOINTS REST API

### **1. Experiments**

#### **GET /api/training/experiments**
- **Descripción:** Obtener lista de experimentos
- **Parámetros:**
  - `status` (opcional): Estado del experimento (CREATED, RUNNING, COMPLETED, FAILED)
  - `templateId` (opcional): ID del template utilizado
  - `createdBy` (opcional): Usuario creador
  - `page` (opcional): Número de página
  - `size` (opcional): Tamaño de página
- **Respuesta:**
```json
{
  "content": [
    {
      "id": "exp-001",
      "experimentId": "EXP_1732456800_ABC12345",
      "name": "LLM Fine-tuning Experiment",
      "description": "Fine-tuning GPT-3.5 for customer support",
      "status": "COMPLETED",
      "templateId": "template-001",
      "createdBy": "data-scientist-001",
      "createdDate": "2025-10-25T10:00:00Z",
      "completedDate": "2025-10-25T15:30:00Z",
      "runCount": 5,
      "bestScore": 0.94
    }
  ],
  "totalElements": 200,
  "totalPages": 20
}
```

#### **POST /api/training/experiments**
- **Descripción:** Crear nuevo experimento
- **Body:**
```json
{
  "name": "New LLM Experiment",
  "description": "Experiment description",
  "templateId": "template-001",
  "parameters": {
    "learning_rate": 0.001,
    "batch_size": 32,
    "epochs": 10
  },
  "metrics": ["accuracy", "loss", "f1_score"],
  "enableHPO": true,
  "maxTrials": 50
}
```

#### **GET /api/training/experiments/{id}**
- **Descripción:** Obtener detalle de experimento específico
- **Respuesta:**
```json
{
  "id": "exp-001",
  "experimentId": "EXP_1732456800_ABC12345",
  "name": "LLM Fine-tuning Experiment",
  "description": "Fine-tuning GPT-3.5 for customer support",
  "status": "COMPLETED",
  "template": {
    "templateId": "template-001",
    "name": "LLM Fine-tuning Template",
    "templateType": "LLM_FINETUNING"
  },
  "runs": [
    {
      "runId": "RUN_001",
      "name": "run-1",
      "status": "COMPLETED",
      "durationSeconds": 3600,
      "bestMetric": 0.94
    }
  ],
  "hpoExperiments": [
    {
      "hpoExperimentId": "HPO_001",
      "algorithm": "BAYESIAN",
      "status": "COMPLETED",
      "bestScore": 0.96,
      "trialCount": 25
    }
  ],
  "lineage": {
    "parentExperiments": [],
    "childExperiments": ["exp-002"],
    "dependencies": ["dataset-001", "model-001"]
  }
}
```

### **2. HPO (Hyperparameter Optimization)**

#### **GET /api/training/hpo-experiments**
- **Descripción:** Obtener experimentos HPO
- **Parámetros:**
  - `experimentId` (opcional): ID del experimento padre
  - `algorithm` (opcional): Algoritmo HPO (RANDOM_SEARCH, BAYESIAN, GRID_SEARCH)
  - `status` (opcional): Estado del HPO
- **Respuesta:**
```json
{
  "content": [
    {
      "id": "hpo-001",
      "hpoExperimentId": "HPO_1732456800_DEF67890",
      "experimentId": "EXP_1732456800_ABC12345",
      "algorithm": "BAYESIAN",
      "objectiveMetric": "accuracy",
      "maxTrials": 50,
      "status": "COMPLETED",
      "bestScore": 0.96,
      "bestTrialId": "TRIAL_HPO_001_25",
      "trialCount": 25,
      "createdDate": "2025-10-25T10:00:00Z",
      "completedDate": "2025-10-25T14:00:00Z"
    }
  ]
}
```

#### **POST /api/training/hpo-experiments**
- **Descripción:** Crear nuevo experimento HPO
- **Body:**
```json
{
  "experimentId": "EXP_1732456800_ABC12345",
  "algorithm": "BAYESIAN",
  "objectiveMetric": "accuracy",
  "maxTrials": 100,
  "maxDurationHours": 24,
  "searchSpace": {
    "learning_rate": [0.001, 0.1],
    "batch_size": [16, 256],
    "dropout": [0.1, 0.5]
  },
  "earlyStoppingPatience": 10
}
```

#### **GET /api/training/hpo-experiments/{id}/trials**
- **Descripción:** Obtener trials de HPO
- **Respuesta:**
```json
{
  "content": [
    {
      "id": "trial-001",
      "trialId": "TRIAL_HPO_001_1",
      "trialNumber": 1,
      "status": "COMPLETED",
      "parameters": {
        "learning_rate": 0.05,
        "batch_size": 64,
        "dropout": 0.3
      },
      "objectiveValue": 0.89,
      "startedDate": "2025-10-25T10:00:00Z",
      "completedDate": "2025-10-25T10:30:00Z",
      "durationSeconds": 1800
    }
  ]
}
```

### **3. Runs**

#### **GET /api/training/runs**
- **Descripción:** Obtener runs de experimentos
- **Parámetros:**
  - `experimentId` (opcional): ID del experimento
  - `status` (opcional): Estado del run
  - `parentRunId` (opcional): ID del run padre
- **Respuesta:**
```json
{
  "content": [
    {
      "id": "run-001",
      "runId": "RUN_1732456800_GHI12345",
      "experimentId": "EXP_1732456800_ABC12345",
      "name": "run-1",
      "status": "COMPLETED",
      "startedDate": "2025-10-25T10:00:00Z",
      "completedDate": "2025-10-25T11:00:00Z",
      "durationSeconds": 3600,
      "parentRunId": null,
      "metricCount": 150,
      "artifactCount": 3,
      "checkpointCount": 5
    }
  ]
}
```

#### **POST /api/training/runs/{id}/compare**
- **Descripción:** Comparar runs
- **Body:**
```json
{
  "runIds": ["RUN_001", "RUN_002", "RUN_003"],
  "metrics": ["accuracy", "loss", "f1_score"],
  "includeParameters": true,
  "includeArtifacts": false
}
```

### **4. Metrics**

#### **GET /api/training/metrics**
- **Descripción:** Obtener métricas de entrenamiento
- **Parámetros:**
  - `runId` (opcional): ID del run
  - `metricName` (opcional): Nombre de la métrica
  - `metricType` (opcional): Tipo de métrica
- **Respuesta:**
```json
{
  "content": [
    {
      "id": "metric-001",
      "metricId": "METRIC_RUN_001_ACCURACY",
      "runId": "RUN_1732456800_GHI12345",
      "metricName": "accuracy",
      "metricValue": 0.94,
      "step": 100,
      "timestamp": "2025-10-25T10:30:00Z",
      "metricType": "TRAINING"
    }
  ]
}
```

#### **GET /api/training/metrics/series/{runId}**
- **Descripción:** Obtener serie de métricas para un run
- **Respuesta:**
```json
{
  "runId": "RUN_1732456800_GHI12345",
  "metrics": [
    {
      "metricName": "accuracy",
      "series": [
        {"step": 0, "value": 0.5, "timestamp": "2025-10-25T10:00:00Z"},
        {"step": 100, "value": 0.7, "timestamp": "2025-10-25T10:30:00Z"},
        {"step": 200, "value": 0.85, "timestamp": "2025-10-25T11:00:00Z"}
      ]
    }
  ]
}
```

### **5. Artifacts**

#### **GET /api/training/artifacts**
- **Descripción:** Obtener artefactos de entrenamiento
- **Parámetros:**
  - `runId` (opcional): ID del run
  - `artifactType` (opcional): Tipo de artefacto
- **Respuesta:**
```json
{
  "content": [
    {
      "id": "artifact-001",
      "artifactId": "ARTIFACT_RUN_001_MODEL",
      "runId": "RUN_1732456800_GHI12345",
      "name": "final_model",
      "artifactType": "MODEL",
      "filePath": "/models/RUN_001/final_model.pkl",
      "fileSize": 52428800,
      "checksum": "abc123def456",
      "metadata": {
        "model_type": "pytorch",
        "version": "1.0",
        "framework": "transformers"
      },
      "createdDate": "2025-10-25T11:00:00Z"
    }
  ]
}
```

---

## 🔧 SDK JAVA

### **1. TrainingService**

```java
@Service
public class TrainingService {
    
    /**
     * Crear experimento desde template
     */
    public Experiment createExperimentFromTemplate(
        String templateId, 
        String experimentName,
        Map<String, Object> parameters) {
        
        ExperimentTemplate template = templateService.getTemplate(templateId);
        
        Experiment experiment = Experiment.builder()
            .experimentId(generateExperimentId())
            .name(experimentName)
            .template(template)
            .parameters(parameters)
            .status(ExperimentStatus.CREATED)
            .createdBy(getCurrentUser())
            .createdDate(LocalDateTime.now())
            .build();
            
        return experimentRepository.save(experiment);
    }
    
    /**
     * Ejecutar experimento
     */
    public Run executeExperiment(String experimentId, String runName) {
        Experiment experiment = experimentRepository.findByExperimentId(experimentId);
        
        Run run = Run.builder()
            .runId(generateRunId())
            .experiment(experiment)
            .name(runName)
            .status(RunStatus.RUNNING)
            .startedDate(LocalDateTime.now())
            .build();
            
        return runRepository.save(run);
    }
    
    /**
     * Optimizar hiperparámetros
     */
    public HPOExperiment optimizeHyperparameters(
        String experimentId,
        HPOAlgorithm algorithm,
        String objectiveMetric,
        Map<String, Object> searchSpace) {
        
        HPOExperiment hpoExperiment = HPOExperiment.builder()
            .hpoExperimentId(generateHPOExperimentId())
            .experiment(experimentRepository.findByExperimentId(experimentId))
            .algorithm(algorithm)
            .objectiveMetric(objectiveMetric)
            .searchSpace(searchSpace)
            .status(HPOStatus.RUNNING)
            .createdDate(LocalDateTime.now())
            .build();
            
        return hpoExperimentRepository.save(hpoExperiment);
    }
    
    /**
     * Track métrica en tiempo real
     */
    public TrainingMetric trackMetric(
        String runId, 
        String metricName, 
        Double metricValue, 
        Long step) {
        
        TrainingMetric metric = TrainingMetric.builder()
            .metricId(generateMetricId())
            .run(runRepository.findByRunId(runId))
            .metricName(metricName)
            .metricValue(metricValue)
            .step(step)
            .timestamp(LocalDateTime.now())
            .metricType(MetricType.TRAINING)
            .build();
            
        return trainingMetricRepository.save(metric);
    }
}
```

### **2. ExperimentTemplate**

```java
@Entity
@Table(name = "experiment_template")
public class ExperimentTemplate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "template_id", unique = true)
    private String templateId;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "template_type")
    private TemplateType templateType;
    
    @Column(name = "configuration", columnDefinition = "TEXT")
    private String configuration; // JSON
    
    @Column(name = "parameters", columnDefinition = "TEXT")
    private String parameters; // JSON
    
    @Column(name = "metrics", columnDefinition = "TEXT")
    private String metrics; // JSON
    
    @Column(name = "is_public")
    private Boolean isPublic;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    
    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL)
    private List<Experiment> experiments;
    
    // Getters y setters
}
```

### **3. HPOService**

```java
@Service
public class HPOService {
    
    /**
     * Ejecutar trial HPO
     */
    public HPOTrialResult executeTrial(String hpoExperimentId, Integer trialNumber) {
        HPOExperiment hpoExperiment = hpoExperimentRepository
            .findByHpoExperimentId(hpoExperimentId);
        
        // Generar parámetros usando algoritmo HPO
        Map<String, Object> parameters = generateParameters(
            hpoExperiment.getAlgorithm(), 
            hpoExperiment.getSearchSpace());
        
        // Crear trial
        HPOTrial trial = HPOTrial.builder()
            .trialId(generateTrialId())
            .hpoExperiment(hpoExperiment)
            .trialNumber(trialNumber)
            .status(TrialStatus.RUNNING)
            .parameters(parameters)
            .startedDate(LocalDateTime.now())
            .build();
            
        trial = hpoTrialRepository.save(trial);
        
        // Ejecutar trial
        Double objectiveValue = executeTrialRun(trial);
        
        // Actualizar trial
        trial.setStatus(TrialStatus.COMPLETED);
        trial.setObjectiveValue(objectiveValue);
        trial.setCompletedDate(LocalDateTime.now());
        trial.setDurationSeconds(calculateDuration(trial.getStartedDate()));
        
        return hpoTrialRepository.save(trial);
    }
    
    /**
     * Generar parámetros usando algoritmo HPO
     */
    private Map<String, Object> generateParameters(
        HPOAlgorithm algorithm, 
        Map<String, Object> searchSpace) {
        
        switch (algorithm) {
            case RANDOM_SEARCH:
                return randomSearch.generateParameters(searchSpace);
            case BAYESIAN:
                return bayesianOptimization.generateParameters(searchSpace);
            case GRID_SEARCH:
                return gridSearch.generateParameters(searchSpace);
            default:
                return randomSearch.generateParameters(searchSpace);
        }
    }
}
```

---

## 🔄 INTEGRACIÓN CON BPMN

### **1. Training Process Integration**

```java
@Component
public class TrainingProcessIntegration {
    
    @Autowired
    private TrainingService trainingService;
    
    @Autowired
    private ProcessEngine processEngine;
    
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
        variables.put("bestMetric", getBestMetric(run));
        
        processEngine.getRuntimeService()
            .setVariables(processInstanceId, variables);
    }
}
```

---

## 📊 MÉTRICAS Y MONITOREO

### **1. Training Metrics**

```java
@Component
public class TrainingMetricsService {
    
    /**
     * Calcular métricas de entrenamiento por período
     */
    public TrainingMetrics calculateMetrics(LocalDate from, LocalDate to) {
        List<Experiment> experiments = experimentRepository
            .findByDateRange(from, to);
        
        return TrainingMetrics.builder()
            .totalExperiments(experiments.size())
            .completedExperiments(experiments.stream()
                .filter(e -> e.getStatus() == ExperimentStatus.COMPLETED)
                .count())
            .averageDuration(experiments.stream()
                .mapToLong(e -> e.getCompletedDate() != null ? 
                    Duration.between(e.getStartedDate(), e.getCompletedDate()).toSeconds() : 0)
                .average()
                .orElse(0.0))
            .hpoUtilizationRate(experiments.stream()
                .mapToDouble(e -> e.getHpoExperiments().size())
                .average()
                .orElse(0.0))
            .templateReuseRate(experiments.stream()
                .mapToDouble(e -> e.getTemplate() != null ? 1.0 : 0.0)
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

El módulo Training proporciona **APIs y SDK robustos** que permiten:

- 🔌 **Integración** con sistemas externos
- 🤖 **Automatización** completa de experimentación
- 📊 **Monitoreo** en tiempo real
- 📋 **Gestión** centralizada de experimentos
- 📈 **Reportes** automatizados
- 🚀 **Superación** de capacidades de MLflow

**Esta API/SDK está diseñada** para ser la solución definitiva de experimentación y entrenamiento de modelos de IA en entornos empresariales.
