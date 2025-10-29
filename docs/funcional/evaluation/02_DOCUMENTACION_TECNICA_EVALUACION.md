# 🔧 DOCUMENTACIÓN TÉCNICA - MÓDULO EVALUACIÓN

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación técnica de entidades JPA, vistas, funciones y procedimientos del módulo evaluación

---

## 🎯 RESUMEN EJECUTIVO

El módulo **evaluación** implementa **entidades JPA robustas** para la gestión de evaluaciones de modelos, detección de sesgos, análisis de fairness y métricas de calidad, con **vistas optimizadas**, **funciones SQL** y **procedimientos almacenados** para análisis avanzado.

---

## 🗄️ ENTIDADES JPA

### **1. ModelEvaluation**

```java
@Entity
@Table(name = "model_evaluation")
public class ModelEvaluation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "evaluation_id", unique = true)
    private String evaluationId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private Model model;
    
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
    private List<EvaluationMetric> metrics;
    
    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL)
    private List<BiasDetection> biasDetections;
    
    // Getters y setters
}
```

### **2. BiasDetection**

```java
@Entity
@Table(name = "bias_detection")
public class BiasDetection {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "detection_id", unique = true)
    private String detectionId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id")
    private ModelEvaluation evaluation;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "bias_type")
    private BiasType biasType;
    
    @Column(name = "bias_score")
    private Double biasScore;
    
    @Column(name = "threshold_value")
    private Double thresholdValue;
    
    @Column(name = "is_biased")
    private Boolean isBiased;
    
    @Column(name = "affected_groups")
    private String affectedGroups; // JSON
    
    @Column(name = "bias_description")
    private String biasDescription;
    
    @Column(name = "detected_date")
    private LocalDateTime detectedDate;
    
    @OneToMany(mappedBy = "biasDetection", cascade = CascadeType.ALL)
    private List<BiasRecommendation> recommendations;
    
    // Getters y setters
}
```

### **3. BiasRecommendation**

```java
@Entity
@Table(name = "bias_recommendation")
public class BiasRecommendation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "recommendation_id", unique = true)
    private String recommendationId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bias_detection_id")
    private BiasDetection biasDetection;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "recommendation_type")
    private RecommendationType recommendationType;
    
    @Column(name = "priority")
    private Integer priority;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "implementation_effort")
    private String implementationEffort; // LOW, MEDIUM, HIGH
    
    @Column(name = "expected_impact")
    private String expectedImpact; // LOW, MEDIUM, HIGH
    
    @Column(name = "status")
    private String status; // PENDING, IMPLEMENTED, REJECTED
    
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    
    // Getters y setters
}
```

### **4. FairnessMetric**

```java
@Entity
@Table(name = "fairness_metric")
public class FairnessMetric {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "metric_id", unique = true)
    private String metricId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id")
    private ModelEvaluation evaluation;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "metric_type")
    private FairnessMetricType metricType;
    
    @Column(name = "metric_value")
    private Double metricValue;
    
    @Column(name = "threshold_value")
    private Double thresholdValue;
    
    @Column(name = "is_fair")
    private Boolean isFair;
    
    @Column(name = "group_a_value")
    private Double groupAValue;
    
    @Column(name = "group_b_value")
    private Double groupBValue;
    
    @Column(name = "difference")
    private Double difference;
    
    @Column(name = "calculated_date")
    private LocalDateTime calculatedDate;
    
    // Getters y setters
}
```

### **5. EvaluationMetric**

```java
@Entity
@Table(name = "evaluation_metric")
public class EvaluationMetric {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "metric_id", unique = true)
    private String metricId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id")
    private ModelEvaluation evaluation;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "metric_type")
    private MetricType metricType;
    
    @Column(name = "metric_name")
    private String metricName;
    
    @Column(name = "metric_value")
    private Double metricValue;
    
    @Column(name = "metric_unit")
    private String metricUnit;
    
    @Column(name = "threshold_value")
    private Double thresholdValue;
    
    @Column(name = "is_above_threshold")
    private Boolean isAboveThreshold;
    
    @Column(name = "calculated_date")
    private LocalDateTime calculatedDate;
    
    // Getters y setters
}
```

---

## 📊 VISTAS OPTIMIZADAS

### **1. ModelEvaluationSummary**

```sql
CREATE VIEW model_evaluation_summary AS
SELECT 
    me.id,
    me.evaluation_id,
    m.model_name,
    m.model_version,
    me.evaluation_type,
    me.status,
    me.overall_score,
    me.accuracy,
    me.precision_score,
    me.recall_score,
    me.f1_score,
    me.auc_roc,
    me.auc_pr,
    me.created_date,
    me.completed_date,
    COUNT(bd.id) as bias_count,
    COUNT(fm.id) as fairness_metrics_count,
    AVG(em.metric_value) as avg_metric_value
FROM model_evaluation me
LEFT JOIN model m ON me.model_id = m.id
LEFT JOIN bias_detection bd ON me.id = bd.evaluation_id
LEFT JOIN fairness_metric fm ON me.id = fm.evaluation_id
LEFT JOIN evaluation_metric em ON me.id = em.evaluation_id
GROUP BY me.id, me.evaluation_id, m.model_name, m.model_version, 
         me.evaluation_type, me.status, me.overall_score, me.accuracy,
         me.precision_score, me.recall_score, me.f1_score, me.auc_roc,
         me.auc_pr, me.created_date, me.completed_date;
```

### **2. BiasAnalysisSummary**

```sql
CREATE VIEW bias_analysis_summary AS
SELECT 
    bd.id,
    bd.detection_id,
    me.evaluation_id,
    m.model_name,
    bd.bias_type,
    bd.bias_score,
    bd.threshold_value,
    bd.is_biased,
    bd.affected_groups,
    bd.bias_description,
    bd.detected_date,
    COUNT(br.id) as recommendation_count,
    AVG(br.priority) as avg_priority
FROM bias_detection bd
LEFT JOIN model_evaluation me ON bd.evaluation_id = me.id
LEFT JOIN model m ON me.model_id = m.id
LEFT JOIN bias_recommendation br ON bd.id = br.bias_detection_id
GROUP BY bd.id, bd.detection_id, me.evaluation_id, m.model_name,
         bd.bias_type, bd.bias_score, bd.threshold_value, bd.is_biased,
         bd.affected_groups, bd.bias_description, bd.detected_date;
```

### **3. FairnessMetricsSummary**

```sql
CREATE VIEW fairness_metrics_summary AS
SELECT 
    fm.id,
    fm.metric_id,
    me.evaluation_id,
    m.model_name,
    fm.metric_type,
    fm.metric_value,
    fm.threshold_value,
    fm.is_fair,
    fm.group_a_value,
    fm.group_b_value,
    fm.difference,
    fm.calculated_date
FROM fairness_metric fm
LEFT JOIN model_evaluation me ON fm.evaluation_id = me.id
LEFT JOIN model m ON me.model_id = m.id;
```

---

## 🔧 FUNCIONES SQL

### **1. Calcular Score de Evaluación**

```sql
CREATE OR REPLACE FUNCTION calculate_evaluation_score(
    p_evaluation_id VARCHAR(255)
) RETURNS DECIMAL(5,2) AS $$
DECLARE
    v_accuracy DECIMAL(5,2);
    v_precision DECIMAL(5,2);
    v_recall DECIMAL(5,2);
    v_f1_score DECIMAL(5,2);
    v_auc_roc DECIMAL(5,2);
    v_weighted_score DECIMAL(5,2);
BEGIN
    -- Obtener métricas de la evaluación
    SELECT accuracy, precision_score, recall_score, f1_score, auc_roc
    INTO v_accuracy, v_precision, v_recall, v_f1_score, v_auc_roc
    FROM model_evaluation
    WHERE evaluation_id = p_evaluation_id;
    
    -- Calcular score ponderado
    v_weighted_score := (
        COALESCE(v_accuracy, 0) * 0.25 +
        COALESCE(v_precision, 0) * 0.20 +
        COALESCE(v_recall, 0) * 0.20 +
        COALESCE(v_f1_score, 0) * 0.20 +
        COALESCE(v_auc_roc, 0) * 0.15
    );
    
    RETURN v_weighted_score;
END;
$$ LANGUAGE plpgsql;
```

### **2. Detectar Sesgos Automáticamente**

```sql
CREATE OR REPLACE FUNCTION detect_model_bias(
    p_evaluation_id VARCHAR(255),
    p_threshold DECIMAL(5,2) DEFAULT 0.1
) RETURNS TABLE(
    bias_type VARCHAR(50),
    bias_score DECIMAL(5,2),
    is_biased BOOLEAN,
    affected_groups TEXT
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        fm.metric_type::VARCHAR(50) as bias_type,
        ABS(fm.difference) as bias_score,
        (ABS(fm.difference) > p_threshold) as is_biased,
        CONCAT('Group A: ', fm.group_a_value, ', Group B: ', fm.group_b_value) as affected_groups
    FROM fairness_metric fm
    JOIN model_evaluation me ON fm.evaluation_id = me.id
    WHERE me.evaluation_id = p_evaluation_id
    AND ABS(fm.difference) > p_threshold;
END;
$$ LANGUAGE plpgsql;
```

### **3. Generar Recomendaciones de Sesgos**

```sql
CREATE OR REPLACE FUNCTION generate_bias_recommendations(
    p_bias_detection_id BIGINT
) RETURNS TABLE(
    recommendation_type VARCHAR(50),
    priority INTEGER,
    description TEXT,
    implementation_effort VARCHAR(20),
    expected_impact VARCHAR(20)
) AS $$
DECLARE
    v_bias_type VARCHAR(50);
    v_bias_score DECIMAL(5,2);
BEGIN
    -- Obtener información del sesgo
    SELECT bd.bias_type, bd.bias_score
    INTO v_bias_type, v_bias_score
    FROM bias_detection bd
    WHERE bd.id = p_bias_detection_id;
    
    -- Generar recomendaciones basadas en el tipo de sesgo
    RETURN QUERY
    SELECT 
        CASE 
            WHEN v_bias_type = 'DEMOGRAPHIC_PARITY' THEN 'DATA_BALANCING'
            WHEN v_bias_type = 'EQUALIZED_ODDS' THEN 'FEATURE_ENGINEERING'
            WHEN v_bias_type = 'CALIBRATION' THEN 'MODEL_RETRAINING'
            ELSE 'GENERAL_MITIGATION'
        END as recommendation_type,
        CASE 
            WHEN v_bias_score > 0.3 THEN 1
            WHEN v_bias_score > 0.2 THEN 2
            ELSE 3
        END as priority,
        CASE 
            WHEN v_bias_type = 'DEMOGRAPHIC_PARITY' THEN 'Balance the dataset to ensure equal representation across demographic groups'
            WHEN v_bias_type = 'EQUALIZED_ODDS' THEN 'Modify features to reduce differential performance across groups'
            WHEN v_bias_type = 'CALIBRATION' THEN 'Retrain the model with calibration techniques'
            ELSE 'Implement general bias mitigation strategies'
        END as description,
        CASE 
            WHEN v_bias_score > 0.3 THEN 'HIGH'
            WHEN v_bias_score > 0.2 THEN 'MEDIUM'
            ELSE 'LOW'
        END as implementation_effort,
        CASE 
            WHEN v_bias_score > 0.3 THEN 'HIGH'
            WHEN v_bias_score > 0.2 THEN 'MEDIUM'
            ELSE 'LOW'
        END as expected_impact;
END;
$$ LANGUAGE plpgsql;
```

---

## 📋 PROCEDIMIENTOS ALMACENADOS

### **1. Evaluación Completa de Modelo**

```sql
CREATE OR REPLACE PROCEDURE complete_model_evaluation(
    p_evaluation_id VARCHAR(255),
    p_model_id BIGINT,
    p_evaluation_type VARCHAR(50)
) AS $$
DECLARE
    v_evaluation_id BIGINT;
    v_bias_count INTEGER;
    v_fairness_count INTEGER;
BEGIN
    -- Crear evaluación
    INSERT INTO model_evaluation (
        evaluation_id, model_id, evaluation_type, status, created_date
    ) VALUES (
        p_evaluation_id, p_model_id, p_evaluation_type, 'IN_PROGRESS', NOW()
    ) RETURNING id INTO v_evaluation_id;
    
    -- Calcular métricas básicas
    UPDATE model_evaluation 
    SET 
        accuracy = calculate_accuracy(p_model_id),
        precision_score = calculate_precision(p_model_id),
        recall_score = calculate_recall(p_model_id),
        f1_score = calculate_f1_score(p_model_id),
        auc_roc = calculate_auc_roc(p_model_id),
        overall_score = calculate_evaluation_score(p_evaluation_id)
    WHERE id = v_evaluation_id;
    
    -- Detectar sesgos
    INSERT INTO bias_detection (
        detection_id, evaluation_id, bias_type, bias_score, 
        threshold_value, is_biased, detected_date
    )
    SELECT 
        CONCAT('BIAS_', v_evaluation_id, '_', ROW_NUMBER() OVER()),
        v_evaluation_id,
        bias_type,
        bias_score,
        0.1,
        is_biased,
        NOW()
    FROM detect_model_bias(p_evaluation_id, 0.1);
    
    -- Calcular métricas de fairness
    INSERT INTO fairness_metric (
        metric_id, evaluation_id, metric_type, metric_value,
        threshold_value, is_fair, group_a_value, group_b_value,
        difference, calculated_date
    )
    SELECT 
        CONCAT('FAIRNESS_', v_evaluation_id, '_', ROW_NUMBER() OVER()),
        v_evaluation_id,
        metric_type,
        metric_value,
        0.1,
        is_fair,
        group_a_value,
        group_b_value,
        difference,
        NOW()
    FROM calculate_fairness_metrics(p_model_id);
    
    -- Generar recomendaciones
    INSERT INTO bias_recommendation (
        recommendation_id, bias_detection_id, recommendation_type,
        priority, description, implementation_effort, expected_impact,
        status, created_date
    )
    SELECT 
        CONCAT('REC_', bd.id, '_', ROW_NUMBER() OVER()),
        bd.id,
        recommendation_type,
        priority,
        description,
        implementation_effort,
        expected_impact,
        'PENDING',
        NOW()
    FROM bias_detection bd
    CROSS JOIN generate_bias_recommendations(bd.id)
    WHERE bd.evaluation_id = v_evaluation_id;
    
    -- Finalizar evaluación
    UPDATE model_evaluation 
    SET 
        status = 'COMPLETED',
        completed_date = NOW()
    WHERE id = v_evaluation_id;
    
    COMMIT;
END;
$$ LANGUAGE plpgsql;
```

### **2. Análisis de Tendencias de Evaluación**

```sql
CREATE OR REPLACE PROCEDURE analyze_evaluation_trends(
    p_model_id BIGINT,
    p_days_back INTEGER DEFAULT 30
) AS $$
DECLARE
    v_start_date TIMESTAMP;
BEGIN
    v_start_date := NOW() - INTERVAL '1 day' * p_days_back;
    
    -- Crear tabla temporal para análisis
    CREATE TEMP TABLE evaluation_trends AS
    SELECT 
        DATE(me.created_date) as evaluation_date,
        COUNT(*) as evaluation_count,
        AVG(me.overall_score) as avg_score,
        AVG(me.accuracy) as avg_accuracy,
        AVG(me.f1_score) as avg_f1_score,
        COUNT(bd.id) as bias_count,
        COUNT(fm.id) as fairness_count
    FROM model_evaluation me
    LEFT JOIN bias_detection bd ON me.id = bd.evaluation_id
    LEFT JOIN fairness_metric fm ON me.id = fm.evaluation_id
    WHERE me.model_id = p_model_id
    AND me.created_date >= v_start_date
    GROUP BY DATE(me.created_date)
    ORDER BY evaluation_date;
    
    -- Analizar tendencias
    SELECT 
        evaluation_date,
        evaluation_count,
        avg_score,
        avg_accuracy,
        avg_f1_score,
        bias_count,
        fairness_count,
        CASE 
            WHEN LAG(avg_score) OVER (ORDER BY evaluation_date) IS NULL THEN 'N/A'
            WHEN avg_score > LAG(avg_score) OVER (ORDER BY evaluation_date) THEN 'IMPROVING'
            WHEN avg_score < LAG(avg_score) OVER (ORDER BY evaluation_date) THEN 'DECLINING'
            ELSE 'STABLE'
        END as score_trend
    FROM evaluation_trends;
    
    DROP TABLE evaluation_trends;
END;
$$ LANGUAGE plpgsql;
```

---

## 🎯 BENEFICIOS DE LA ARQUITECTURA TÉCNICA

### **Para Desarrolladores:**
- **Entidades JPA** bien estructuradas y normalizadas
- **Vistas optimizadas** para consultas frecuentes
- **Funciones SQL** reutilizables y eficientes
- **Procedimientos** para operaciones complejas

### **Para Administradores de Base de Datos:**
- **Índices optimizados** para rendimiento
- **Vistas materializadas** para consultas pesadas
- **Procedimientos** para mantenimiento automatizado
- **Funciones** para cálculos complejos

### **Para el Sistema:**
- **Rendimiento** optimizado con vistas
- **Escalabilidad** con procedimientos almacenados
- **Consistencia** de datos con transacciones
- **Mantenibilidad** con código SQL modular

---

## 🎯 CONCLUSIÓN

La arquitectura técnica del módulo Evaluación proporciona:

- 🗄️ **Entidades JPA** robustas y normalizadas
- 📊 **Vistas optimizadas** para consultas frecuentes
- 🔧 **Funciones SQL** reutilizables y eficientes
- 📋 **Procedimientos** para operaciones complejas
- 🚀 **Rendimiento** optimizado y escalable

**Esta arquitectura está diseñada** para soportar evaluaciones de modelos a escala empresarial con alta disponibilidad y rendimiento.
