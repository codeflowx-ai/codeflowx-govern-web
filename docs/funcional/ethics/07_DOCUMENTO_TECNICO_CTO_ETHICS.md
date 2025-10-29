# ⚖️ ETHICS - DOCUMENTO TÉCNICO CTO

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Audiencia:** CTOs, Arquitectos de Software, Líderes Técnicos  
**Propósito:** Especificación técnica detallada del módulo Ethics

---

## 🎯 RESUMEN EJECUTIVO TÉCNICO

**CodeflowX Govern Ethics** es una plataforma de IA ética enterprise-grade construida sobre **algoritmos de fairness state-of-the-art**, **integración con frameworks académicos** y **monitoreo continuo de sesgos**.

### **Stack Tecnológico:**
- **Backend:** Java 17, Spring Boot 3.2
- **Fairness Engine:** Python 3.11, AIF360, Fairlearn
- **Statistical Analysis:** R 4.3, statsmodels
- **Base de Datos:** PostgreSQL 15
- **ML Pipeline:** Scikit-learn, TensorFlow, PyTorch

---

## 🏗️ ARQUITECTURA TÉCNICA

### **1. Arquitectura General**

```
┌─────────────────────────────────────────────────────────────┐
│                    Frontend Layer (ZKoss)                   │
│              (6 ZUL Screens, ViewModels)                    │
└─────────────────────┬───────────────────────────────────────┘
                      │
         ┌────────────┴──────────────┐
         ▼                           ▼
┌──────────────────┐        ┌──────────────────┐
│  REST API Layer  │        │  BPMN Workflows  │
│  (Spring MVC)    │        │  (Flowable)      │
└────────┬─────────┘        └────────┬─────────┘
         │                           │
    ┌────▼───────────────────────────▼────┐
    │      Ethics Service Layer            │
    │  (Reviews, Bias, Fairness, Impact)   │
    └────┬───────────────────────────┬────┘
         │                           │
    ┌────▼────┐                 ┌────▼────┐
    │ Fairness│                 │ Drools  │
    │ Engine  │                 │ Rules   │
    │ (Python)│                 │ Engine  │
    └────┬────┘                 └────┬────┘
         │                           │
    ┌────▼───────────────────────────▼────┐
    │         PostgreSQL Database          │
    │  (8 Tables, BPMN, Drools Rules)      │
    └──────────────────────────────────────┘
```

### **2. Modelo de Datos**

#### **Esquema de Base de Datos**

```sql
-- Tabla de revisiones éticas
CREATE TABLE eth_ethical_reviews (
    eth_id BIGSERIAL PRIMARY KEY,
    eth_entity_type VARCHAR(50) NOT NULL,
    eth_entity_id BIGINT NOT NULL,
    eth_review_type VARCHAR(50) NOT NULL,
    eth_status VARCHAR(20) NOT NULL,
    eth_requested_by BIGINT NOT NULL,
    eth_requested_at TIMESTAMP NOT NULL,
    eth_reviewer_id BIGINT,
    eth_completed_at TIMESTAMP,
    eth_overall_score DECIMAL(4,2),
    eth_recommendation VARCHAR(50),
    eth_reviewer_notes TEXT,
    eth_conditions JSONB,
    CONSTRAINT fk_eth_review_requester FOREIGN KEY (eth_requested_by) 
        REFERENCES cor_users(cor_id),
    CONSTRAINT fk_eth_review_reviewer FOREIGN KEY (eth_reviewer_id) 
        REFERENCES cor_users(cor_id)
);

CREATE INDEX idx_eth_reviews_entity ON eth_ethical_reviews(eth_entity_type, eth_entity_id);
CREATE INDEX idx_eth_reviews_status ON eth_ethical_reviews(eth_status);

-- Tabla de evaluaciones éticas (scores detallados)
CREATE TABLE eth_ethical_assessments (
    eth_id BIGSERIAL PRIMARY KEY,
    eth_review_id BIGINT NOT NULL,
    eth_fairness_score DECIMAL(4,2),
    eth_transparency_score DECIMAL(4,2),
    eth_accountability_score DECIMAL(4,2),
    eth_privacy_score DECIMAL(4,2),
    eth_safety_score DECIMAL(4,2),
    eth_overall_score DECIMAL(4,2),
    CONSTRAINT fk_eth_assessment_review FOREIGN KEY (eth_review_id) 
        REFERENCES eth_ethical_reviews(eth_id)
);

-- Tabla de detección de sesgos
CREATE TABLE eth_bias_detections (
    eth_id BIGSERIAL PRIMARY KEY,
    eth_entity_type VARCHAR(50) NOT NULL,
    eth_entity_id BIGINT NOT NULL,
    eth_dataset_id BIGINT,
    eth_sensitive_attribute VARCHAR(100) NOT NULL,
    eth_bias_metric VARCHAR(50) NOT NULL,
    eth_bias_value DECIMAL(10,6),
    eth_threshold DECIMAL(10,6),
    eth_bias_passed BOOLEAN,
    eth_bias_level VARCHAR(20), -- LOW, MODERATE, HIGH
    eth_overall_bias_score DECIMAL(4,2),
    eth_detected_at TIMESTAMP NOT NULL,
    eth_recommendations JSONB,
    CONSTRAINT fk_eth_bias_dataset FOREIGN KEY (eth_dataset_id) 
        REFERENCES dts_datasets(dts_id)
);

CREATE INDEX idx_eth_bias_entity ON eth_bias_detections(eth_entity_type, eth_entity_id);
CREATE INDEX idx_eth_bias_attribute ON eth_bias_detections(eth_sensitive_attribute);
CREATE INDEX idx_eth_bias_timestamp ON eth_bias_detections(eth_detected_at DESC);

-- Tabla de evaluaciones de fairness
CREATE TABLE eth_fairness_assessments (
    eth_id BIGSERIAL PRIMARY KEY,
    eth_entity_type VARCHAR(50) NOT NULL,
    eth_entity_id BIGINT NOT NULL,
    eth_assessment_type VARCHAR(50) NOT NULL,
    eth_protected_attributes VARCHAR(500),
    eth_overall_fairness_score DECIMAL(4,2),
    eth_fairness_level VARCHAR(20), -- EXCELLENT, GOOD, FAIR, POOR
    eth_assessed_at TIMESTAMP NOT NULL,
    eth_remediation_strategies JSONB
);

-- Tabla de métricas de fairness detalladas
CREATE TABLE eth_fairness_metrics (
    eth_id BIGSERIAL PRIMARY KEY,
    eth_assessment_id BIGINT NOT NULL,
    eth_metric_name VARCHAR(100) NOT NULL,
    eth_metric_value DECIMAL(10,6),
    eth_threshold DECIMAL(10,6),
    eth_passed BOOLEAN,
    eth_interpretation TEXT,
    CONSTRAINT fk_eth_fairness_assessment FOREIGN KEY (eth_assessment_id) 
        REFERENCES eth_fairness_assessments(eth_id)
);

-- Tabla de análisis de impacto
CREATE TABLE eth_impact_analyses (
    eth_id BIGSERIAL PRIMARY KEY,
    eth_entity_type VARCHAR(50) NOT NULL,
    eth_entity_id BIGINT NOT NULL,
    eth_overall_impact_score DECIMAL(4,2),
    eth_stakeholders VARCHAR(500),
    eth_time_horizon VARCHAR(20),
    eth_analyzed_at TIMESTAMP NOT NULL,
    eth_recommended_actions JSONB
);

-- Tabla de dimensiones de impacto
CREATE TABLE eth_impact_dimensions (
    eth_id BIGSERIAL PRIMARY KEY,
    eth_analysis_id BIGINT NOT NULL,
    eth_dimension_name VARCHAR(100) NOT NULL,
    eth_score DECIMAL(4,2),
    eth_positive_impacts JSONB,
    eth_negative_impacts JSONB,
    CONSTRAINT fk_eth_impact_analysis FOREIGN KEY (eth_analysis_id) 
        REFERENCES eth_impact_analyses(eth_id)
);
```

---

## 🔧 COMPONENTES PRINCIPALES

### **1. Bias Detection Engine**

```java
@Service
public class BiasDetectionEngine {
    
    @Autowired
    private PythonExecutor pythonExecutor;
    
    @Autowired
    private DatasetService datasetService;
    
    /**
     * Detección de sesgos usando AI Fairness 360
     */
    public BiasDetectionResult detectBias(BiasDetectionRequest request) {
        // 1. Obtener dataset
        Dataset dataset = datasetService.getDataset(request.getDatasetId());
        
        // 2. Preparar script Python con AIF360
        String pythonScript = String.format("""
            from aif360.datasets import BinaryLabelDataset
            from aif360.metrics import BinaryLabelDatasetMetric, ClassificationMetric
            import pandas as pd
            import json
            
            # Cargar datos
            df = pd.read_csv('data.csv')
            
            # Crear dataset AIF360
            dataset = BinaryLabelDataset(
                df=df,
                label_names=['target'],
                protected_attribute_names=%s
            )
            
            results = {}
            
            # Para cada atributo sensible
            for attr in %s:
                # Definir grupos privilegiados y no privilegiados
                privileged = [{attr: 1}]
                unprivileged = [{attr: 0}]
                
                # Calcular métricas
                metric = BinaryLabelDatasetMetric(
                    dataset,
                    unprivileged_groups=unprivileged,
                    privileged_groups=privileged
                )
                
                results[attr] = {
                    'disparate_impact': float(metric.disparate_impact()),
                    'statistical_parity_difference': float(metric.statistical_parity_difference()),
                    'consistency': float(metric.consistency()),
                    'base_rate_privileged': float(metric.base_rate(privileged=True)),
                    'base_rate_unprivileged': float(metric.base_rate(privileged=False))
                }
            
            print(json.dumps(results))
            """,
            toJsonArray(request.getSensitiveAttributes()),
            toJsonArray(request.getSensitiveAttributes())
        );
        
        // 3. Ejecutar análisis
        String output = pythonExecutor.execute(pythonScript, dataset.toCsv());
        Map<String, BiasMetrics> results = parseAIF360Results(output);
        
        // 4. Evaluar resultados
        double overallBiasScore = calculateOverallBiasScore(results);
        BiasLevel biasLevel = determineBiasLevel(overallBiasScore);
        
        // 5. Generar recomendaciones
        List<String> recommendations = generateBiasRecommendations(results, biasLevel);
        
        // 6. Persistir resultados
        BiasDetectionResult result = BiasDetectionResult.builder()
            .entityType(request.getEntityType())
            .entityId(request.getEntityId())
            .datasetId(request.getDatasetId())
            .biasResults(results)
            .overallBiasScore(overallBiasScore)
            .biasLevel(biasLevel)
            .recommendations(recommendations)
            .detectedAt(LocalDateTime.now())
            .build();
        
        biasDetectionRepository.save(result);
        
        return result;
    }
    
    /**
     * Cálculo de score general de sesgo
     */
    private double calculateOverallBiasScore(Map<String, BiasMetrics> results) {
        double totalScore = 0.0;
        int metricCount = 0;
        
        for (BiasMetrics metrics : results.values()) {
            // Disparate Impact: ideal = 1.0, rango aceptable [0.8, 1.2]
            double diScore = calculateDisparateImpactScore(metrics.getDisparateImpact());
            
            // Statistical Parity: ideal = 0.0, rango aceptable [-0.1, 0.1]
            double spScore = calculateStatisticalParityScore(
                metrics.getStatisticalParityDifference()
            );
            
            totalScore += (diScore + spScore) / 2.0;
            metricCount++;
        }
        
        return metricCount > 0 ? totalScore / metricCount : 0.0;
    }
    
    private double calculateDisparateImpactScore(double disparateImpact) {
        // Convertir disparate impact a score 0-10
        if (disparateImpact >= 0.8 && disparateImpact <= 1.2) {
            return 10.0; // Perfecto
        } else if (disparateImpact >= 0.7 && disparateImpact <= 1.3) {
            return 8.0; // Bueno
        } else if (disparateImpact >= 0.6 && disparateImpact <= 1.4) {
            return 6.0; // Aceptable
        } else {
            return Math.max(0.0, 10.0 - Math.abs(1.0 - disparateImpact) * 10);
        }
    }
}
```

### **2. Fairness Assessment Engine**

```java
@Service
public class FairnessAssessmentEngine {
    
    @Autowired
    private PythonExecutor pythonExecutor;
    
    /**
     * Evaluación de fairness usando Fairlearn
     */
    public FairnessAssessment assessFairness(FairnessAssessmentRequest request) {
        // 1. Obtener modelo y datos
        Model model = modelService.getModel(request.getEntityId());
        Dataset testData = datasetService.getDataset(model.getTestDatasetId());
        
        // 2. Script Python con Fairlearn
        String pythonScript = """
            from fairlearn.metrics import MetricFrame
            from fairlearn.metrics import (
                demographic_parity_difference,
                demographic_parity_ratio,
                equalized_odds_difference,
                equalized_odds_ratio
            )
            import pandas as pd
            import json
            
            # Cargar datos
            df = pd.read_csv('data.csv')
            y_true = df['target']
            y_pred = df['prediction']
            
            results = {}
            
            # Para cada atributo protegido
            for attr in %s:
                sensitive_features = df[attr]
                
                # Calcular métricas
                results[attr] = {
                    'demographic_parity_diff': float(
                        demographic_parity_difference(y_true, y_pred, sensitive_features=sensitive_features)
                    ),
                    'demographic_parity_ratio': float(
                        demographic_parity_ratio(y_true, y_pred, sensitive_features=sensitive_features)
                    ),
                    'equalized_odds_diff': float(
                        equalized_odds_difference(y_true, y_pred, sensitive_features=sensitive_features)
                    ),
                    'equalized_odds_ratio': float(
                        equalized_odds_ratio(y_true, y_pred, sensitive_features=sensitive_features)
                    )
                }
            
            print(json.dumps(results))
            """.formatted(toJsonArray(request.getProtectedAttributes()));
        
        String output = pythonExecutor.execute(pythonScript, preparePredictionsData(model, testData));
        Map<String, FairnessMetrics> metrics = parseFairlearnResults(output);
        
        // 3. Calcular overall fairness score
        double overallScore = calculateOverallFairnessScore(metrics);
        FairnessLevel level = determineFairnessLevel(overallScore);
        
        // 4. Generar estrategias de remediación
        List<RemediationStrategy> strategies = generateRemediationStrategies(metrics);
        
        // 5. Persistir resultados
        FairnessAssessment assessment = FairnessAssessment.builder()
            .entityType(request.getEntityType())
            .entityId(request.getEntityId())
            .assessmentType(request.getAssessmentType())
            .protectedAttributes(request.getProtectedAttributes())
            .fairnessMetrics(metrics)
            .overallFairnessScore(overallScore)
            .fairnessLevel(level)
            .remediationStrategies(strategies)
            .assessedAt(LocalDateTime.now())
            .build();
        
        fairnessAssessmentRepository.save(assessment);
        
        return assessment;
    }
}
```

### **3. Ethical Review Workflow (BPMN)**

```java
@Service
public class EthicalReviewWorkflowService {
    
    @Autowired
    private RuntimeService runtimeService;
    
    @Autowired
    private TaskService taskService;
    
    /**
     * Inicia proceso BPMN de revisión ética
     */
    public String startEthicalReviewProcess(EthicalReviewRequest request) {
        // Variables del proceso
        Map<String, Object> variables = Map.of(
            "entityType", request.getEntityType().name(),
            "entityId", request.getEntityId(),
            "reviewType", request.getReviewType().name(),
            "priority", request.getPriority().name(),
            "requestedBy", request.getRequestedBy(),
            "stakeholders", request.getStakeholders()
        );
        
        // Iniciar proceso
        ProcessInstance process = runtimeService.startProcessInstanceByKey(
            "ethics-review-v1",
            "ethics-review-" + UUID.randomUUID(),
            variables
        );
        
        return process.getId();
    }
}
```

**Proceso BPMN:** `ethics-review-v1.bpmn`

```xml
<process id="ethics-review-v1" name="Ethical Review Process">
    
    <startEvent id="start" name="Review Requested"/>
    
    <!-- Automated Bias Detection -->
    <serviceTask id="biasDetection" name="Automated Bias Detection"
                 flowable:class="com.codeflowx.ethics.delegates.BiasDetectionDelegate"/>
    
    <!-- Fairness Assessment -->
    <serviceTask id="fairnessAssessment" name="Fairness Assessment"
                 flowable:class="com.codeflowx.ethics.delegates.FairnessAssessmentDelegate"/>
    
    <!-- Impact Analysis -->
    <serviceTask id="impactAnalysis" name="Impact Analysis"
                 flowable:class="com.codeflowx.ethics.delegates.ImpactAnalysisDelegate"/>
    
    <!-- Human Review -->
    <userTask id="humanReview" name="Human Ethical Review"
              flowable:formKey="ethical-review-form"
              flowable:candidateGroups="ethics-committee"/>
    
    <!-- Decision Gateway -->
    <exclusiveGateway id="decision" name="Review Decision"/>
    
    <!-- Approved -->
    <serviceTask id="approve" name="Approve with Conditions"
                 flowable:class="com.codeflowx.ethics.delegates.ApprovalDelegate"/>
    
    <!-- Rejected -->
    <serviceTask id="reject" name="Reject and Document"
                 flowable:class="com.codeflowx.ethics.delegates.RejectionDelegate"/>
    
    <endEvent id="end" name="Review Completed"/>
    
</process>
```

### **4. Drools Rules Engine**

```java
@Service
public class EthicsRulesEngine {
    
    @Autowired
    private KieContainer kieContainer;
    
    /**
     * Evalúa reglas éticas con Drools
     */
    public RulesEvaluationResult evaluateEthicalRules(
        Model model,
        BiasDetectionResult biasResult,
        FairnessAssessment fairness
    ) {
        KieSession kieSession = kieContainer.newKieSession();
        
        try {
            // Insertar hechos
            kieSession.insert(model);
            kieSession.insert(biasResult);
            kieSession.insert(fairness);
            
            // Ejecutar reglas
            int rulesFired = kieSession.fireAllRules();
            
            // Obtener resultados
            List<EthicalViolation> violations = new ArrayList<>();
            kieSession.getObjects(new ClassObjectFilter(EthicalViolation.class))
                .forEach(obj -> violations.add((EthicalViolation) obj));
            
            return RulesEvaluationResult.builder()
                .rulesFired(rulesFired)
                .violations(violations)
                .passed(violations.isEmpty())
                .build();
                
        } finally {
            kieSession.dispose();
        }
    }
}
```

**Ejemplo de Regla Drools:**

```drools
package com.codeflowx.ethics.rules

import com.codeflowx.ethics.model.*

rule "High Bias Violation"
when
    $bias : BiasDetectionResult(biasLevel == BiasLevel.HIGH)
then
    EthicalViolation violation = new EthicalViolation();
    violation.setType(ViolationType.HIGH_BIAS);
    violation.setSeverity(Severity.HIGH);
    violation.setMessage("High bias detected: " + $bias.getOverallBiasScore());
    violation.setRemediation("Apply bias mitigation techniques before deployment");
    insert(violation);
end

rule "Low Fairness Score"
when
    $fairness : FairnessAssessment(overallFairnessScore < 6.0)
then
    EthicalViolation violation = new EthicalViolation();
    violation.setType(ViolationType.LOW_FAIRNESS);
    violation.setSeverity(Severity.CRITICAL);
    violation.setMessage("Fairness score below acceptable threshold");
    violation.setRemediation("Implement fairness constraints or reject deployment");
    insert(violation);
end

rule "Missing Ethical Review"
when
    $model : Model(ethicalReviewId == null, status == ModelStatus.READY_FOR_DEPLOYMENT)
then
    EthicalViolation violation = new EthicalViolation();
    violation.setType(ViolationType.MISSING_REVIEW);
    violation.setSeverity(Severity.CRITICAL);
    violation.setMessage("Model ready for deployment without ethical review");
    violation.setRemediation("Complete ethical review before deployment");
    insert(violation);
end
```

---

## 🎯 ALGORITMOS DE FAIRNESS

### **Técnicas Implementadas:**

#### **1. Pre-processing**
- **Reweighting:** Ajuste de pesos por grupo
- **Resampling:** Oversampling/undersampling
- **Disparate Impact Remover:** Transformación de features

#### **2. In-processing**
- **Fairness Constraints:** Constraints durante entrenamiento
- **Adversarial Debiasing:** Redes adversariales
- **Prejudice Remover:** Regularización por fairness

#### **3. Post-processing**
- **Threshold Optimization:** Umbrales óptimos por grupo
- **Calibration:** Calibración por grupo
- **Reject Option Classification:** Clasificación con opción de rechazo

---

## 📊 PERFORMANCE Y ESCALABILIDAD

### **Capacidad:**
- **Bias Detection:** 100 análisis/hora
- **Fairness Assessment:** 50 evaluaciones/hora
- **Ethical Reviews:** 200 revisiones activas simultáneas
- **Impact Analysis:** 20 análisis/día

### **Optimizaciones:**
- **Caché de resultados:** Redis para análisis recientes
- **Async processing:** Queue para análisis pesados
- **Parallel execution:** ThreadPool para múltiples modelos
- **Pre-computed metrics:** Agregaciones pre-calculadas

---

## 🎯 CONCLUSIONES TÉCNICAS

### **Fortalezas Arquitectónicas:**
1. ✅ **Academic-grade:** Algoritmos state-of-the-art
2. ✅ **Automated:** BPMN + Drools para workflows
3. ✅ **Scalable:** Procesamiento paralelo
4. ✅ **Integrable:** Python + Java seamless
5. ✅ **Auditable:** Trazabilidad completa

### **Decisiones de Diseño:**
1. **AIF360 + Fairlearn:** Frameworks líderes académicos
2. **BPMN:** Workflow estandarizado
3. **Drools:** Reglas éticas configurables
4. **PostgreSQL:** ACID compliance para auditoría
5. **Python Bridge:** Acceso a librerías ML

---

## 📞 CONTACTO TÉCNICO

**Arquitectura:** architecture@codeflowx.com  
**Soporte Técnico:** support@codeflowx.com  
**Documentación:** docs.codeflowx.com/ethics  
**GitHub:** github.com/codeflowx/ethics

