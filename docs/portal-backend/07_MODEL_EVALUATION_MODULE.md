## Módulo de Model Evaluation - Portal Backend

## Descripción General

El módulo de Model Evaluation proporciona capacidades avanzadas de evaluación, auditoría y gobierno de modelos de IA. Incluye funcionalidades de evaluación de rendimiento, análisis de sesgos, auditoría de seguridad, generación de reportes y monitorización continua. Se integra con MLflow para tracking de experimentos y con sistemas de auditoría para cumplimiento regulatorio.

## Entidades del Sistema

### 1\. ModelEvaluation

```java
@Entity
@Table(name = "model_evaluations")
public class ModelEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_id")
    private Long modelId;

    @Column(name = "model_version_id")
    private Long modelVersionId;

    @Column(name = "evaluation_name")
    private String evaluationName;

    @Column
    private String description;

    @Column(name = "evaluation_type")
    @Enumerated(EnumType.STRING)
    private EvaluationType evaluationType;

    @Column(name = "evaluation_framework")
    private String evaluationFramework; // MLflow, custom, etc.

    @Column(name = "dataset_id")
    private Long datasetId;

    @Column(name = "evaluation_config")
    private String evaluationConfig; // JSON configuration

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EvaluationStatus status;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "overall_score")
    private Double overallScore;

    @Column(name = "confidence_interval")
    private String confidenceInterval; // JSON confidence bounds

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL)
    private List<evaluationmetric> metrics = new ArrayList&lt;&gt;();

    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL)
    private List<evaluationresult> results = new ArrayList&lt;&gt;();

    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL)
    private List<evaluationartifact> artifacts = new ArrayList&lt;&gt;();
}

public enum EvaluationType {
    PERFORMANCE, BIAS_FAIRNESS, SECURITY, ROBUSTNESS, INTERPRETABILITY,
    EFFICIENCY, SCALABILITY, COMPLIANCE, REGULATORY, CUSTOM
}

public enum EvaluationStatus {
    PENDING, RUNNING, COMPLETED, FAILED, CANCELLED, VALIDATING
}
```

### 2\. EvaluationMetric

```java
@Entity
@Table(name = "evaluation_metrics")
public class EvaluationMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "evaluation_id")
    private Long evaluationId;

    @Column(name = "metric_name")
    private String metricName;

    @Column(name = "metric_value")
    private Double metricValue;

    @Column(name = "metric_unit")
    private String metricUnit;

    @Column(name = "metric_type")
    @Enumerated(EnumType.STRING)
    private MetricType metricType;

    @Column(name = "threshold")
    private Double threshold;

    @Column(name = "is_passing")
    private Boolean isPassing;

    @Column(name = "weight")
    private Double weight; // For weighted scoring

    @Column(name = "description")
    private String description;

    @Column(name = "formula")
    private String formula; // Mathematical formula used

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

public enum MetricType {
    ACCURACY, PRECISION, RECALL, F1_SCORE, AUC, RMSE, MAE, MAPE,
    BIAS_SCORE, FAIRNESS_SCORE, SECURITY_SCORE, ROBUSTNESS_SCORE,
    INTERPRETABILITY_SCORE, EFFICIENCY_SCORE, COMPLIANCE_SCORE
}
```

### 3\. EvaluationResult

```java
@Entity
@Table(name = "evaluation_results")
public class EvaluationResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "evaluation_id")
    private Long evaluationId;

    @Column(name = "result_type")
    @Enumerated(EnumType.STRING)
    private ResultType resultType;

    @Column(name = "result_data")
    private String resultData; // JSON result data

    @Column(name = "visualization_data")
    private String visualizationData; // JSON visualization config

    @Column(name = "insights")
    private String insights; // JSON insights and analysis

    @Column(name = "recommendations")
    private String recommendations; // JSON recommendations

    @Column(name = "severity")
    @Enumerated(EnumType.STRING)
    private SeverityLevel severity;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

public enum ResultType {
    CONFUSION_MATRIX, ROC_CURVE, PRECISION_RECALL_CURVE, FEATURE_IMPORTANCE,
    BIAS_ANALYSIS, SECURITY_ANALYSIS, ROBUSTNESS_ANALYSIS, COMPLIANCE_REPORT
}

public enum SeverityLevel {
    LOW, MEDIUM, HIGH, CRITICAL
}
```

### 4\. EvaluationArtifact

```java
@Entity
@Table(name = "evaluation_artifacts")
public class EvaluationArtifact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "evaluation_id")
    private Long evaluationId;

    @Column(name = "artifact_type")
    @Enumerated(EnumType.STRING)
    private ArtifactType artifactType;

    @Column(name = "artifact_name")
    private String artifactName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "metadata")
    private String metadata; // JSON metadata

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

public enum ArtifactType {
    REPORT_PDF, VISUALIZATION_PNG, DATA_CSV, MODEL_ANALYSIS,
    COMPLIANCE_CERTIFICATE, SECURITY_AUDIT, BIAS_REPORT
}
```

### 5\. BiasAnalysis

```java
@Entity
@Table(name = "bias_analyses")
public class BiasAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "evaluation_id")
    private Long evaluationId;

    @Column(name = "sensitive_attribute")
    private String sensitiveAttribute; // e.g., gender, race, age

    @Column(name = "bias_metric")
    private String biasMetric; // Statistical parity, equalized odds, etc.

    @Column(name = "bias_value")
    private Double biasValue;

    @Column(name = "threshold")
    private Double threshold;

    @Column(name = "is_biased")
    private Boolean isBiased;

    @Column(name = "bias_direction")
    @Enumerated(EnumType.STRING)
    private BiasDirection biasDirection;

    @Column(name = "impact_assessment")
    private String impactAssessment; // JSON impact analysis

    @Column(name = "mitigation_strategies")
    private String mitigationStrategies; // JSON strategies

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

public enum BiasDirection {
    FAVORS_GROUP_A, FAVORS_GROUP_B, BALANCED, UNKNOWN
}
```

### 6\. SecurityAnalysis

```java
@Entity
@Table(name = "security_analyses")
public class SecurityAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "evaluation_id")
    private Long evaluationId;

    @Column(name = "security_test_type")
    @Enumerated(EnumType.STRING)
    private SecurityTestType securityTestType;

    @Column(name = "vulnerability_type")
    private String vulnerabilityType;

    @Column(name = "severity")
    @Enumerated(EnumType.STRING)
    private SecuritySeverity severity;

    @Column(name = "cvss_score")
    private Double cvssScore;

    @Column(name = "description")
    private String description;

    @Column(name = "attack_vector")
    private String attackVector;

    @Column(name = "impact")
    private String impact; // JSON impact details

    @Column(name = "mitigation")
    private String mitigation; // JSON mitigation steps

    @Column(name = "is_fixed")
    private Boolean isFixed;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

public enum SecurityTestType {
    ADVERSARIAL_ATTACK, MODEL_INVERSION, MEMBERSHIP_INFERENCE,
    DATA_POISONING, BACKDOOR_ATTACK, PRIVACY_LEAKAGE
}

public enum SecuritySeverity {
    LOW, MEDIUM, HIGH, CRITICAL
}
```

### 7\. ComplianceReport

```java
@Entity
@Table(name = "compliance_reports")
public class ComplianceReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "evaluation_id")
    private Long evaluationId;

    @Column(name = "regulation_framework")
    private String regulationFramework; // GDPR, HIPAA, SOX, etc.

    @Column(name = "compliance_status")
    @Enumerated(EnumType.STRING)
    private ComplianceStatus complianceStatus;

    @Column(name = "compliance_score")
    private Double complianceScore;

    @Column(name = "requirements_met")
    private Integer requirementsMet;

    @Column(name = "total_requirements")
    private Integer totalRequirements;

    @Column(name = "gap_analysis")
    private String gapAnalysis; // JSON gap details

    @Column(name = "remediation_plan")
    private String remediationPlan; // JSON remediation steps

    @Column(name = "auditor")
    private String auditor;

    @Column(name = "audit_date")
    private LocalDate auditDate;

    @Column(name = "next_audit_date")
    private LocalDate nextAuditDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum ComplianceStatus {
    COMPLIANT, NON_COMPLIANT, PARTIALLY_COMPLIANT, UNDER_REVIEW
}
```

### 8\. EvaluationReport

```java
@Entity
@Table(name = "evaluation_reports")
public class EvaluationReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "evaluation_id")
    private Long evaluationId;

    @Column(name = "report_type")
    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Column(name = "report_format")
    private String reportFormat; // PDF, HTML, JSON, etc.

    @Column(name = "report_content")
    private String reportContent; // JSON report structure

    @Column(name = "executive_summary")
    private String executiveSummary;

    @Column(name = "key_findings")
    private String keyFindings; // JSON key findings

    @Column(name = "risk_assessment")
    private String riskAssessment; // JSON risk analysis

    @Column(name = "recommendations")
    private String recommendations; // JSON recommendations

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    @Column(name = "generated_by")
    private Long generatedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

public enum ReportType {
    EXECUTIVE_SUMMARY, TECHNICAL_DETAILED, COMPLIANCE_AUDIT,
    SECURITY_ASSESSMENT, BIAS_ANALYSIS, PERFORMANCE_ANALYSIS
}
```

## API Endpoints

### Model Evaluation Management

```plaintext
GET    /api/v1/model-evaluations
GET    /api/v1/model-evaluations/{id}
POST   /api/v1/model-evaluations
PUT    /api/v1/model-evaluations/{id}
DELETE /api/v1/model-evaluations/{id}
GET    /api/v1/model-evaluations/model/{modelId}
GET    /api/v1/model-evaluations/version/{versionId}
POST   /api/v1/model-evaluations/{id}/start
POST   /api/v1/model-evaluations/{id}/stop
POST   /api/v1/model-evaluations/{id}/validate
```

### Evaluation Metrics

```plaintext
GET    /api/v1/model-evaluations/{id}/metrics
GET    /api/v1/model-evaluations/{id}/metrics/{metricId}
POST   /api/v1/model-evaluations/{id}/metrics
PUT    /api/v1/model-evaluations/{id}/metrics/{metricId}
DELETE /api/v1/model-evaluations/{id}/metrics/{metricId}
GET    /api/v1/model-evaluations/{id}/metrics/summary
GET    /api/v1/model-evaluations/{id}/metrics/trends
```

### Evaluation Results

```plaintext
GET    /api/v1/model-evaluations/{id}/results
GET    /api/v1/model-evaluations/{id}/results/{resultId}
POST   /api/v1/model-evaluations/{id}/results
PUT    /api/v1/model-evaluations/{id}/results/{resultId}
DELETE /api/v1/model-evaluations/{id}/results/{resultId}
GET    /api/v1/model-evaluations/{id}/results/visualizations
```

### Bias Analysis

```plaintext
GET    /api/v1/model-evaluations/{id}/bias-analysis
GET    /api/v1/model-evaluations/{id}/bias-analysis/{analysisId}
POST   /api/v1/model-evaluations/{id}/bias-analysis
PUT    /api/v1/model-evaluations/{id}/bias-analysis/{analysisId}
DELETE /api/v1/model-evaluations/{id}/bias-analysis/{analysisId}
GET    /api/v1/model-evaluations/{id}/bias-analysis/summary
POST   /api/v1/model-evaluations/{id}/bias-analysis/mitigate
```

### Security Analysis

```plaintext
GET    /api/v1/model-evaluations/{id}/security-analysis
GET    /api/v1/model-evaluations/{id}/security-analysis/{analysisId}
POST   /api/v1/model-evaluations/{id}/security-analysis
PUT    /api/v1/model-evaluations/{id}/security-analysis/{analysisId}
DELETE /api/v1/model-evaluations/{id}/security-analysis/{analysisId}
GET    /api/v1/model-evaluations/{id}/security-analysis/vulnerabilities
POST   /api/v1/model-evaluations/{id}/security-analysis/fix
```

### Compliance Reports

```plaintext
GET    /api/v1/model-evaluations/{id}/compliance-reports
GET    /api/v1/model-evaluations/{id}/compliance-reports/{reportId}
POST   /api/v1/model-evaluations/{id}/compliance-reports
PUT    /api/v1/model-evaluations/{id}/compliance-reports/{reportId}
DELETE /api/v1/model-evaluations/{id}/compliance-reports/{reportId}
GET    /api/v1/model-evaluations/{id}/compliance-reports/frameworks
POST   /api/v1/model-evaluations/{id}/compliance-reports/audit
```

### Evaluation Reports

```plaintext
GET    /api/v1/model-evaluations/{id}/reports
GET    /api/v1/model-evaluations/{id}/reports/{reportId}
POST   /api/v1/model-evaluations/{id}/reports
PUT    /api/v1/model-evaluations/{id}/reports/{reportId}
DELETE /api/v1/model-evaluations/{id}/reports/{reportId}
GET    /api/v1/model-evaluations/{id}/reports/generate
POST   /api/v1/model-evaluations/{id}/reports/schedule
```

### Monitoring and Analytics

```plaintext
GET    /api/v1/model-evaluations/monitoring/overview
GET    /api/v1/model-evaluations/monitoring/performance
GET    /api/v1/model-evaluations/monitoring/bias-trends
GET    /api/v1/model-evaluations/monitoring/security-alerts
GET    /api/v1/model-evaluations/analytics/comparison
POST   /api/v1/model-evaluations/analytics/benchmark
GET    /api/v1/model-evaluations/analytics/trends
```

## Scripts de Base de Datos

```plaintext
-- Tabla de evaluaciones de modelos
CREATE TABLE model_evaluations (
    id BIGSERIAL PRIMARY KEY,
    model_id BIGINT REFERENCES models(id) ON DELETE CASCADE,
    model_version_id BIGINT REFERENCES model_versions(id) ON DELETE CASCADE,
    evaluation_name VARCHAR(255) NOT NULL,
    description TEXT,
    evaluation_type VARCHAR(50) NOT NULL,
    evaluation_framework VARCHAR(100),
    dataset_id BIGINT REFERENCES domain_datasets(id),
    evaluation_config TEXT,
    status VARCHAR(50) DEFAULT 'PENDING',
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    duration_minutes INTEGER,
    overall_score DECIMAL(5,4),
    confidence_interval TEXT,
    created_by BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de métricas de evaluación
CREATE TABLE evaluation_metrics (
    id BIGSERIAL PRIMARY KEY,
    evaluation_id BIGINT REFERENCES model_evaluations(id) ON DELETE CASCADE,
    metric_name VARCHAR(100) NOT NULL,
    metric_value DECIMAL(15,6),
    metric_unit VARCHAR(50),
    metric_type VARCHAR(50) NOT NULL,
    threshold DECIMAL(15,6),
    is_passing BOOLEAN,
    weight DECIMAL(5,4) DEFAULT 1.0,
    description TEXT,
    formula TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de resultados de evaluación
CREATE TABLE evaluation_results (
    id BIGSERIAL PRIMARY KEY,
    evaluation_id BIGINT REFERENCES model_evaluations(id) ON DELETE CASCADE,
    result_type VARCHAR(50) NOT NULL,
    result_data TEXT,
    visualization_data TEXT,
    insights TEXT,
    recommendations TEXT,
    severity VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de artefactos de evaluación
CREATE TABLE evaluation_artifacts (
    id BIGSERIAL PRIMARY KEY,
    evaluation_id BIGINT REFERENCES model_evaluations(id) ON DELETE CASCADE,
    artifact_type VARCHAR(50) NOT NULL,
    artifact_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500),
    file_size_bytes BIGINT,
    mime_type VARCHAR(100),
    metadata TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de análisis de sesgos
CREATE TABLE bias_analyses (
    id BIGSERIAL PRIMARY KEY,
    evaluation_id BIGINT REFERENCES model_evaluations(id) ON DELETE CASCADE,
    sensitive_attribute VARCHAR(100) NOT NULL,
    bias_metric VARCHAR(100) NOT NULL,
    bias_value DECIMAL(15,6),
    threshold DECIMAL(15,6),
    is_biased BOOLEAN,
    bias_direction VARCHAR(50),
    impact_assessment TEXT,
    mitigation_strategies TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de análisis de seguridad
CREATE TABLE security_analyses (
    id BIGSERIAL PRIMARY KEY,
    evaluation_id BIGINT REFERENCES model_evaluations(id) ON DELETE CASCADE,
    security_test_type VARCHAR(50) NOT NULL,
    vulnerability_type VARCHAR(100),
    severity VARCHAR(20),
    cvss_score DECIMAL(3,1),
    description TEXT,
    attack_vector TEXT,
    impact TEXT,
    mitigation TEXT,
    is_fixed BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de reportes de cumplimiento
CREATE TABLE compliance_reports (
    id BIGSERIAL PRIMARY KEY,
    evaluation_id BIGINT REFERENCES model_evaluations(id) ON DELETE CASCADE,
    regulation_framework VARCHAR(100) NOT NULL,
    compliance_status VARCHAR(50) NOT NULL,
    compliance_score DECIMAL(5,4),
    requirements_met INTEGER,
    total_requirements INTEGER,
    gap_analysis TEXT,
    remediation_plan TEXT,
    auditor VARCHAR(255),
    audit_date DATE,
    next_audit_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de reportes de evaluación
CREATE TABLE evaluation_reports (
    id BIGSERIAL PRIMARY KEY,
    evaluation_id BIGINT REFERENCES model_evaluations(id) ON DELETE CASCADE,
    report_type VARCHAR(50) NOT NULL,
    report_format VARCHAR(50),
    report_content TEXT,
    executive_summary TEXT,
    key_findings TEXT,
    risk_assessment TEXT,
    recommendations TEXT,
    generated_at TIMESTAMP,
    generated_by BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para optimización
CREATE INDEX idx_model_evaluations_model_id ON model_evaluations(model_id);
CREATE INDEX idx_model_evaluations_version_id ON model_evaluations(model_version_id);
CREATE INDEX idx_model_evaluations_type ON model_evaluations(evaluation_type);
CREATE INDEX idx_model_evaluations_status ON model_evaluations(status);
CREATE INDEX idx_model_evaluations_created_by ON model_evaluations(created_by);
CREATE INDEX idx_evaluation_metrics_evaluation_id ON evaluation_metrics(evaluation_id);
CREATE INDEX idx_evaluation_metrics_type ON evaluation_metrics(metric_type);
CREATE INDEX idx_evaluation_metrics_name ON evaluation_metrics(metric_name);
CREATE INDEX idx_evaluation_results_evaluation_id ON evaluation_results(evaluation_id);
CREATE INDEX idx_evaluation_results_type ON evaluation_results(result_type);
CREATE INDEX idx_evaluation_artifacts_evaluation_id ON evaluation_artifacts(evaluation_id);
CREATE INDEX idx_evaluation_artifacts_type ON evaluation_artifacts(artifact_type);
CREATE INDEX idx_bias_analyses_evaluation_id ON bias_analyses(evaluation_id);
CREATE INDEX idx_bias_analyses_attribute ON bias_analyses(sensitive_attribute);
CREATE INDEX idx_security_analyses_evaluation_id ON security_analyses(evaluation_id);
CREATE INDEX idx_security_analyses_test_type ON security_analyses(security_test_type);
CREATE INDEX idx_security_analyses_severity ON security_analyses(severity);
CREATE INDEX idx_compliance_reports_evaluation_id ON compliance_reports(evaluation_id);
CREATE INDEX idx_compliance_reports_framework ON compliance_reports(regulation_framework);
CREATE INDEX idx_compliance_reports_status ON compliance_reports(compliance_status);
CREATE INDEX idx_evaluation_reports_evaluation_id ON evaluation_reports(evaluation_id);
CREATE INDEX idx_evaluation_reports_type ON evaluation_reports(report_type);
```

## Servicios de Model Evaluation

### ModelEvaluationService

```java
@Service
@Transactional
public class ModelEvaluationService {

    @Autowired
    private ModelEvaluationRepository evaluationRepository;

    @Autowired
    private EvaluationMetricService metricService;

    @Autowired
    private BiasAnalysisService biasService;

    @Autowired
    private SecurityAnalysisService securityService;

    @Autowired
    private ComplianceService complianceService;

    @Autowired
    private ReportGenerationService reportService;

    @Autowired
    private AuditService auditService;

    public ModelEvaluation createEvaluation(ModelEvaluationDto evaluationDto) {
        // Validar datos de la evaluación
        validateEvaluationData(evaluationDto);

        // Crear evaluación
        ModelEvaluation evaluation = new ModelEvaluation();
        evaluation.setModelId(evaluationDto.getModelId());
        evaluation.setModelVersionId(evaluationDto.getModelVersionId());
        evaluation.setEvaluationName(evaluationDto.getEvaluationName());
        evaluation.setDescription(evaluationDto.getDescription());
        evaluation.setEvaluationType(evaluationDto.getEvaluationType());
        evaluation.setEvaluationFramework(evaluationDto.getEvaluationFramework());
        evaluation.setDatasetId(evaluationDto.getDatasetId());
        evaluation.setEvaluationConfig(evaluationDto.getEvaluationConfig());
        evaluation.setStatus(EvaluationStatus.PENDING);
        evaluation.setCreatedBy(getCurrentUserId());
        evaluation.setCreatedAt(LocalDateTime.now());

        ModelEvaluation savedEvaluation = evaluationRepository.save(evaluation);

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "CREATE_MODEL_EVALUATION",
            "MODEL_EVALUATION",
            savedEvaluation.getId().toString()
        );

        return savedEvaluation;
    }

    public ModelEvaluation startEvaluation(Long evaluationId) {
        ModelEvaluation evaluation = evaluationRepository.findById(evaluationId)
            .orElseThrow(() -&gt; new EvaluationNotFoundException("Evaluation not found"));

        // Validar que la evaluación esté en estado PENDING
        if (evaluation.getStatus() != EvaluationStatus.PENDING) {
            throw new InvalidEvaluationStateException("Evaluation must be in PENDING state to start");
        }

        // Actualizar estado
        evaluation.setStatus(EvaluationStatus.RUNNING);
        evaluation.setStartedAt(LocalDateTime.now());
        evaluation.setUpdatedAt(LocalDateTime.now());

        ModelEvaluation updatedEvaluation = evaluationRepository.save(evaluation);

        // Ejecutar evaluación asíncrona
        executeEvaluationAsync(evaluationId);

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "START_MODEL_EVALUATION",
            "MODEL_EVALUATION",
            evaluationId.toString()
        );

        return updatedEvaluation;
    }

    public ModelEvaluation completeEvaluation(Long evaluationId, EvaluationResultDto resultDto) {
        ModelEvaluation evaluation = evaluationRepository.findById(evaluationId)
            .orElseThrow(() -&gt; new EvaluationNotFoundException("Evaluation not found"));

        // Validar que la evaluación esté ejecutándose
        if (evaluation.getStatus() != EvaluationStatus.RUNNING) {
            throw new InvalidEvaluationStateException("Evaluation must be in RUNNING state to complete");
        }

        // Calcular score general
        Double overallScore = calculateOverallScore(evaluationId);

        // Actualizar evaluación
        evaluation.setStatus(EvaluationStatus.COMPLETED);
        evaluation.setCompletedAt(LocalDateTime.now());
        evaluation.setOverallScore(overallScore);
        evaluation.setDurationMinutes(calculateDuration(evaluation.getStartedAt()));
        evaluation.setUpdatedAt(LocalDateTime.now());

        ModelEvaluation completedEvaluation = evaluationRepository.save(evaluation);

        // Generar reportes automáticamente
        generateAutomaticReports(evaluationId);

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "COMPLETE_MODEL_EVALUATION",
            "MODEL_EVALUATION",
            evaluationId.toString()
        );

        return completedEvaluation;
    }

    private void executeEvaluationAsync(Long evaluationId) {
        // Ejecutar evaluación en background
        CompletableFuture.runAsync(() -&gt; {
            try {
                // Simular ejecución de evaluación
                Thread.sleep(5000); // Placeholder

                // Completar evaluación
                completeEvaluation(evaluationId, new EvaluationResultDto());

            } catch (Exception e) {
                // Marcar como fallida
                markEvaluationAsFailed(evaluationId, e.getMessage());
            }
        });
    }

    private Double calculateOverallScore(Long evaluationId) {
        List<evaluationmetric> metrics = metricService.findByEvaluationId(evaluationId);

        if (metrics.isEmpty()) {
            return 0.0;
        }

        double weightedSum = 0.0;
        double totalWeight = 0.0;

        for (EvaluationMetric metric : metrics) {
            weightedSum += (metric.getMetricValue() * metric.getWeight());
            totalWeight += metric.getWeight();
        }

        return totalWeight &gt; 0 ? weightedSum / totalWeight : 0.0;
    }

    private Integer calculateDuration(LocalDateTime startedAt) {
        if (startedAt == null) {
            return 0;
        }

        Duration duration = Duration.between(startedAt, LocalDateTime.now());
        return (int) duration.toMinutes();
    }

    private void generateAutomaticReports(Long evaluationId) {
        // Generar reporte ejecutivo
        reportService.generateReport(evaluationId, ReportType.EXECUTIVE_SUMMARY);

        // Generar reporte técnico
        reportService.generateReport(evaluationId, ReportType.TECHNICAL_DETAILED);

        // Generar reporte de cumplimiento si es necesario
        if (hasComplianceRequirements(evaluationId)) {
            reportService.generateReport(evaluationId, ReportType.COMPLIANCE_AUDIT);
        }
    }

    private boolean hasComplianceRequirements(Long evaluationId) {
        // Verificar si la evaluación tiene requisitos de cumplimiento
        return complianceService.hasComplianceRequirements(evaluationId);
    }

    private void markEvaluationAsFailed(Long evaluationId, String errorMessage) {
        ModelEvaluation evaluation = evaluationRepository.findById(evaluationId).orElse(null);
        if (evaluation != null) {
            evaluation.setStatus(EvaluationStatus.FAILED);
            evaluation.setUpdatedAt(LocalDateTime.now());
            evaluationRepository.save(evaluation);
        }
    }

    private void validateEvaluationData(ModelEvaluationDto evaluationDto) {
        if (evaluationDto.getEvaluationName() == null || evaluationDto.getEvaluationName().trim().isEmpty()) {
            throw new ValidationException("Evaluation name is required");
        }

        if (evaluationDto.getEvaluationType() == null) {
            throw new ValidationException("Evaluation type is required");
        }

        if (evaluationDto.getModelId() == null) {
            throw new ValidationException("Model ID is required");
        }
    }

    private Long getCurrentUserId() {
        // Obtener ID del usuario actual
        return 1L; // Placeholder
    }

    private String getCurrentUsername() {
        // Obtener username del usuario actual
        return "current_user"; // Placeholder
    }
}
```

## Servicio de Análisis de Sesgos

### BiasAnalysisService

```java
@Service
@Transactional
public class BiasAnalysisService {

    @Autowired
    private BiasAnalysisRepository biasRepository;

    @Autowired
    private DatasetService datasetService;

    public BiasAnalysis analyzeBias(Long evaluationId, String sensitiveAttribute) {
        // Obtener datos de la evaluación
        ModelEvaluation evaluation = evaluationRepository.findById(evaluationId)
            .orElseThrow(() -&gt; new EvaluationNotFoundException("Evaluation not found"));

        // Cargar dataset
        DomainDataset dataset = datasetService.findById(evaluation.getDatasetId())
            .orElseThrow(() -&gt; new DatasetNotFoundException("Dataset not found"));

        // Realizar análisis de sesgos
        BiasAnalysisResult result = performBiasAnalysis(dataset, sensitiveAttribute);

        // Crear entidad de análisis
        BiasAnalysis biasAnalysis = new BiasAnalysis();
        biasAnalysis.setEvaluationId(evaluationId);
        biasAnalysis.setSensitiveAttribute(sensitiveAttribute);
        biasAnalysis.setBiasMetric(result.getBiasMetric());
        biasAnalysis.setBiasValue(result.getBiasValue());
        biasAnalysis.setThreshold(result.getThreshold());
        biasAnalysis.setIsBiased(result.getIsBiased());
        biasAnalysis.setBiasDirection(result.getBiasDirection());
        biasAnalysis.setImpactAssessment(result.getImpactAssessment());
        biasAnalysis.setMitigationStrategies(result.getMitigationStrategies());
        biasAnalysis.setCreatedAt(LocalDateTime.now());

        return biasRepository.save(biasAnalysis);
    }

    private BiasAnalysisResult performBiasAnalysis(DomainDataset dataset, String sensitiveAttribute) {
        // Implementar lógica de análisis de sesgos
        // Usar técnicas como Statistical Parity, Equalized Odds, etc.

        BiasAnalysisResult result = new BiasAnalysisResult();
        result.setBiasMetric("Statistical Parity");
        result.setBiasValue(0.15); // Placeholder
        result.setThreshold(0.1);
        result.setIsBiased(0.15 &gt; 0.1);
        result.setBiasDirection(BiasDirection.FAVORS_GROUP_A);
        result.setImpactAssessment("{\"impact\": \"medium\", \"affected_groups\": [\"group_a\"]}");
        result.setMitigationStrategies("{\"strategies\": [\"data_balancing\", \"algorithmic_fairness\"]}");

        return result;
    }

    private static class BiasAnalysisResult {
        private String biasMetric;
        private Double biasValue;
        private Double threshold;
        private Boolean isBiased;
        private BiasDirection biasDirection;
        private String impactAssessment;
        private String mitigationStrategies;

        // Getters y setters
    }
}
```

## Monitoreo y Métricas

### Prometheus Metrics

```java
@Component
public class ModelEvaluationMetrics {

    @Autowired
    private MeterRegistry meterRegistry;

    private final Counter evaluationsCreatedCounter;
    private final Counter evaluationsCompletedCounter;
    private final Counter evaluationsFailedCounter;
    private final Counter biasAnalysesCounter;
    private final Counter securityAnalysesCounter;
    private final Timer evaluationExecutionTimer;
    private final Timer biasAnalysisTimer;
    private final Timer securityAnalysisTimer;

    public ModelEvaluationMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.evaluationsCreatedCounter = Counter.builder("model.evaluation.evaluations.created")
            .description("Total evaluations created")
            .register(meterRegistry);

        this.evaluationsCompletedCounter = Counter.builder("model.evaluation.evaluations.completed")
            .description("Total evaluations completed")
            .register(meterRegistry);

        this.evaluationsFailedCounter = Counter.builder("model.evaluation.evaluations.failed")
            .description("Total evaluations failed")
            .register(meterRegistry);

        this.biasAnalysesCounter = Counter.builder("model.evaluation.bias.analyses")
            .description("Total bias analyses performed")
            .register(meterRegistry);

        this.securityAnalysesCounter = Counter.builder("model.evaluation.security.analyses")
            .description("Total security analyses performed")
            .register(meterRegistry);

        this.evaluationExecutionTimer = Timer.builder("model.evaluation.execution.time")
            .description("Evaluation execution time")
            .register(meterRegistry);

        this.biasAnalysisTimer = Timer.builder("model.evaluation.bias.analysis.time")
            .description("Bias analysis time")
            .register(meterRegistry);

        this.securityAnalysisTimer = Timer.builder("model.evaluation.security.analysis.time")
            .description("Security analysis time")
            .register(meterRegistry);
    }

    public void incrementEvaluationsCreated() {
        evaluationsCreatedCounter.increment();
    }

    public void incrementEvaluationsCompleted() {
        evaluationsCompletedCounter.increment();
    }

    public void incrementEvaluationsFailed() {
        evaluationsFailedCounter.increment();
    }

    public void incrementBiasAnalyses() {
        biasAnalysesCounter.increment();
    }

    public void incrementSecurityAnalyses() {
        securityAnalysesCounter.increment();
    }

    public Timer.Sample startEvaluationExecutionTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startBiasAnalysisTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startSecurityAnalysisTimer() {
        return Timer.start(meterRegistry);
    }
}
```

## Configuración de Aplicación

### application-model-evaluation.yml

```plaintext
model-evaluation:
  # Configuración de evaluaciones
  evaluations:
    default-timeout-minutes: 120
    max-concurrent-evaluations: 10
    auto-report-generation: true
    default-confidence-level: 0.95

  # Configuración de métricas
  metrics:
    default-thresholds:
      accuracy: 0.8
      precision: 0.7
      recall: 0.7
      f1_score: 0.7
      bias_score: 0.1
      security_score: 0.8
    weighted-scoring: true
    normalization: true

  # Configuración de análisis de sesgos
  bias-analysis:
    enabled: true
    sensitive-attributes: ["gender", "race", "age", "income", "education"]
    bias-metrics: ["statistical_parity", "equalized_odds", "demographic_parity"]
    threshold: 0.1
    auto-mitigation: false

  # Configuración de análisis de seguridad
  security-analysis:
    enabled: true
    test-types: ["adversarial_attack", "model_inversion", "membership_inference"]
    cvss-threshold: 7.0
    auto-fix: false
    alert-on-high-severity: true

  # Configuración de cumplimiento
  compliance:
    frameworks: ["GDPR", "HIPAA", "SOX", "CCPA", "ISO27001"]
    auto-audit: true
    audit-frequency: "monthly"
    compliance-threshold: 0.9

  # Configuración de reportes
  reports:
    auto-generation: true
    formats: ["PDF", "HTML", "JSON"]
    retention-days: 1095
    template-path: "/templates/reports"

  # Configuración de monitoreo
  monitoring:
    real-time-alerts: true
    alert-channels: ["email", "slack", "webhook"]
    performance-tracking: true
    bias-monitoring: true
    security-monitoring: true
```

## Pendiente

### **Integración con MLflow, CodeflowX y Leka Server**

El almacenamiento de trazas, métricas y logs se realiza exclusivamente en **MLflow** y **CodeflowX**. El servidor Leka está plenamente integrado con CodeflowX, por lo que se deben usar todos los servicios REST o APIs desde Python para mostrar las métricas y logs de las evaluaciones.

#### **1. Integración con MLflow**

```java
@Service
public class MLflowIntegrationService {

    @Autowired
    private MLflowConfig mlflowConfig;

    @Autowired
    private RestTemplate restTemplate;

    public ModelEvaluationResult evaluateModelWithMLflow(String modelUri, String dataPath,
                                                       EvaluationConfig config) {

        // Usar mlflow.models.evaluate
        String mlflowUrl = mlflowConfig.getBaseUrl() + "/api/2.0/mlflow/models/evaluate";

        MLflowEvaluateRequest request = MLflowEvaluateRequest.builder()
            .model_uri(modelUri)
            .data_path(dataPath)
            .evaluator_config(config.getEvaluatorConfig())
            .build();

        ResponseEntity<MLflowEvaluateResponse> response = restTemplate.postForEntity(
            mlflowUrl, request, MLflowEvaluateResponse.class);

        return mapMLflowResponseToResult(response.getBody());
    }

    public GenAIEvaluationResult evaluateGenAIWithMLflow(String modelName, String prompt,
                                                        GenAIEvaluationConfig config) {

        // Usar mlflow.genai.evaluate
        String mlflowUrl = mlflowConfig.getBaseUrl() + "/api/2.0/mlflow/genai/evaluate";

        MLflowGenAIEvaluateRequest request = MLflowGenAIEvaluateRequest.builder()
            .model_name(modelName)
            .prompt(prompt)
            .evaluator_config(config.getEvaluatorConfig())
            .build();

        ResponseEntity<MLflowGenAIEvaluateResponse> response = restTemplate.postForEntity(
            mlflowUrl, request, MLflowGenAIEvaluateResponse.class);

        return mapMLflowGenAIResponseToResult(response.getBody());
    }

    public List<Experiment> getExperiments() {
        String mlflowUrl = mlflowConfig.getBaseUrl() + "/api/2.0/mlflow/experiments/list";

        ResponseEntity<MLflowExperimentsResponse> response = restTemplate.getForEntity(
            mlflowUrl, MLflowExperimentsResponse.class);

        return mapMLflowExperimentsToExperiments(response.getBody());
    }

    public List<Run> getExperimentRuns(String experimentId) {
        String mlflowUrl = mlflowConfig.getBaseUrl() + "/api/2.0/mlflow/runs/search";

        MLflowSearchRunsRequest request = MLflowSearchRunsRequest.builder()
            .experiment_ids(Arrays.asList(experimentId))
            .build();

        ResponseEntity<MLflowSearchRunsResponse> response = restTemplate.postForEntity(
            mlflowUrl, request, MLflowSearchRunsResponse.class);

        return mapMLflowRunsToRuns(response.getBody());
    }

    public Map<String, Object> getRunMetrics(String runId) {
        String mlflowUrl = mlflowConfig.getBaseUrl() + "/api/2.0/mlflow/runs/get";

        MLflowGetRunRequest request = MLflowGetRunRequest.builder()
            .run_id(runId)
            .build();

        ResponseEntity<MLflowGetRunResponse> response = restTemplate.postForEntity(
            mlflowUrl, request, MLflowGetRunResponse.class);

        return response.getBody().getRun().getData().getMetrics();
    }
}
```

#### **2. Integración con CodeflowX**

```java
@Service
public class CodeflowXIntegrationService {

    @Autowired
    private CodeflowXConfig codeflowXConfig;

    @Autowired
    private RestTemplate restTemplate;

    public List<CodeflowXModel> getAvailableModels() {
        String codeflowXUrl = codeflowXConfig.getBaseUrl() + "/api/v1/models";

        ResponseEntity<CodeflowXModelsResponse> response = restTemplate.getForEntity(
            codeflowXUrl, CodeflowXModelsResponse.class);

        return response.getBody().getModels();
    }

    public CodeflowXEvaluationResult evaluateModelInCodeflowX(String modelId,
                                                             EvaluationDataset dataset) {

        String codeflowXUrl = codeflowXConfig.getBaseUrl() + "/api/v1/models/" + modelId + "/evaluate";

        CodeflowXEvaluateRequest request = CodeflowXEvaluateRequest.builder()
            .dataset_id(dataset.getId())
            .evaluation_type("comprehensive")
            .metrics_config(dataset.getMetricsConfig())
            .build();

        ResponseEntity<CodeflowXEvaluationResponse> response = restTemplate.postForEntity(
            codeflowXUrl, request, CodeflowXEvaluationResponse.class);

        return response.getBody().getResult();
    }

    public List<CodeflowXExperiment> getExperiments() {
        String codeflowXUrl = codeflowXConfig.getBaseUrl() + "/api/v1/experiments";

        ResponseEntity<CodeflowXExperimentsResponse> response = restTemplate.getForEntity(
            codeflowXUrl, CodeflowXExperimentsResponse.class);

        return response.getBody().getExperiments();
    }

    public CodeflowXExperiment createExperiment(String name, String description) {
        String codeflowXUrl = codeflowXConfig.getBaseUrl() + "/api/v1/experiments";

        CodeflowXCreateExperimentRequest request = CodeflowXCreateExperimentRequest.builder()
            .name(name)
            .description(description)
            .build();

        ResponseEntity<CodeflowXExperimentResponse> response = restTemplate.postForEntity(
            codeflowXUrl, request, CodeflowXExperimentResponse.class);

        return response.getBody().getExperiment();
    }
}
```

#### **3. Templates Predefinidos de Métricas**

```java
@Entity
@Table(name = "evl_evaluation_templates")
public class EvaluationTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_name")
    private String templateName;

    @Column(name = "template_type")
    @Enumerated(EnumType.STRING)
    private TemplateType templateType;

    @Column(name = "description")
    private String description;

    @Column(name = "metrics_config")
    private String metricsConfig; // JSON con configuración de métricas

    @Column(name = "evaluation_methodology")
    private String evaluationMethodology; // JSON con metodología

    @Column(name = "judge_model_config")
    private String judgeModelConfig; // Configuración del modelo juez

    @Column(name = "serving_endpoint_id")
    private Long servingEndpointId; // Endpoint del agente juez

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum TemplateType {
    MODEL_EVALUATION, RAG_EVALUATION, INDEX_EVALUATION, AGENT_EVALUATION,
    DATASET_EVALUATION, HITL_EVALUATION, CUSTOM_EVALUATION
}
```

#### **4. Configuración de Métricas por Tipo**

```java
@Service
public class EvaluationTemplateService {

    public EvaluationTemplate getModelEvaluationTemplate() {
        EvaluationTemplate template = new EvaluationTemplate();
        template.setTemplateName("Standard Model Evaluation");
        template.setTemplateType(TemplateType.MODEL_EVALUATION);

        // Configuración de métricas estándar
        MetricsConfig metricsConfig = MetricsConfig.builder()
            .accuracy(true)
            .precision(true)
            .recall(true)
            .f1Score(true)
            .auc(true)
            .logLoss(true)
            .build();

        template.setMetricsConfig(JsonUtils.toJson(metricsConfig));

        // Metodología de evaluación
        EvaluationMethodology methodology = EvaluationMethodology.builder()
            .crossValidation(true)
            .crossValidationFolds(5)
            .stratifiedSampling(true)
            .randomState(42)
            .build();

        template.setEvaluationMethodology(JsonUtils.toJson(methodology));

        return template;
    }

    public EvaluationTemplate getRAGEvaluationTemplate() {
        EvaluationTemplate template = new EvaluationTemplate();
        template.setTemplateName("RAG System Evaluation");
        template.setTemplateType(TemplateType.RAG_EVALUATION);

        // Métricas específicas para RAG
        RAGMetricsConfig ragMetrics = RAGMetricsConfig.builder()
            .retrievalAccuracy(true)
            .responseRelevance(true)
            .contextPrecision(true)
            .contextRecall(true)
            .responseCoherence(true)
            .factualAccuracy(true)
            .build();

        template.setMetricsConfig(JsonUtils.toJson(ragMetrics));

        return template;
    }

    public EvaluationTemplate getHITLEvaluationTemplate() {
        EvaluationTemplate template = new EvaluationTemplate();
        template.setTemplateName("Human-in-the-Loop Evaluation");
        template.setTemplateType(TemplateType.HITL_EVALUATION);

        // Configuración para evaluaciones HITL
        HITLMetricsConfig hitlMetrics = HITLMetricsConfig.builder()
            .userSatisfaction(true)
            .taskCompletionRate(true)
            .responseTime(true)
            .qualityScore(true)
            .confidenceLevel(true)
            .build();

        template.setMetricsConfig(JsonUtils.toJson(hitlMetrics));

        return template;
    }
}
```

#### **5. Conexión con Módulo de Notebook**

```java
@Service
public class NotebookIntegrationService {

    @Autowired
    private NotebookConfig notebookConfig;

    @Autowired
    private RestTemplate restTemplate;

    public String createEvaluationVisualization(String experimentId, String runId,
                                              VisualizationConfig config) {

        String notebookUrl = notebookConfig.getBaseUrl() + "/api/v1/notebooks/create";

        NotebookCreateRequest request = NotebookCreateRequest.builder()
            .experiment_id(experimentId)
            .run_id(runId)
            .visualization_type(config.getType())
            .metrics(config.getMetrics())
            .chart_config(config.getChartConfig())
            .build();

        ResponseEntity<NotebookCreateResponse> response = restTemplate.postForEntity(
            notebookUrl, request, NotebookCreateResponse.class);

        return response.getBody().getNotebookUrl();
    }

    public String generateEvaluationReport(String experimentId, String runId,
                                         ReportConfig config) {

        String notebookUrl = notebookConfig.getBaseUrl() + "/api/v1/notebooks/report";

        NotebookReportRequest request = NotebookReportRequest.builder()
            .experiment_id(experimentId)
            .run_id(runId)
            .report_format(config.getFormat())
            .include_charts(config.isIncludeCharts())
            .include_metrics(config.isIncludeMetrics())
            .include_analysis(config.isIncludeAnalysis())
            .build();

        ResponseEntity<NotebookReportResponse> response = restTemplate.postForEntity(
            notebookUrl, request, NotebookReportResponse.class);

        return response.getBody().getReportUrl();
    }

    public List<ChartTemplate> getAvailableChartTemplates() {
        String notebookUrl = notebookConfig.getBaseUrl() + "/api/v1/notebooks/chart-templates";

        ResponseEntity<ChartTemplatesResponse> response = restTemplate.getForEntity(
            notebookUrl, ChartTemplatesResponse.class);

        return response.getBody().getTemplates();
    }
}
```

#### **6. Configuración de Agentes Jueces**

```java
@Entity
@Table(name = "evl_judge_agents")
public class JudgeAgent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agent_name")
    private String agentName;

    @Column(name = "agent_type")
    @Enumerated(EnumType.STRING)
    private JudgeAgentType agentType;

    @Column(name = "model_config")
    private String modelConfig; // JSON con configuración del modelo

    @Column(name = "serving_endpoint_id")
    private Long servingEndpointId;

    @Column(name = "evaluation_capabilities")
    private String evaluationCapabilities; // JSON array

    @Column(name = "performance_metrics")
    private String performanceMetrics; // JSON con métricas de performance

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum JudgeAgentType {
    ACCURACY_JUDGE, RELEVANCE_JUDGE, COHERENCE_JUDGE, FACTUAL_JUDGE,
    BIAS_JUDGE, SECURITY_JUDGE, ETHICAL_JUDGE
}
```

#### **7. Configuración de Aplicación Integrada**

```yaml
# application-model-evaluation-integrated.yml
model-evaluation:
  mlflow-integration:
    enabled: true
    base-url: ${MLFLOW_BASE_URL:http://localhost:5000}
    api-version: "2.0"
    authentication:
      type: "token"
      token: ${MLFLOW_TOKEN}

  codeflowx-integration:
    enabled: true
    base-url: ${CODEFLOWX_BASE_URL:http://localhost:8001}
    api-version: "v1"
    authentication:
      type: "api-key"
      api-key: ${CODEFLOWX_API_KEY}

  leka-server-integration:
    enabled: true
    base-url: ${LEKA_SERVER_BASE_URL:http://localhost:8002}
    api-version: "v1"

  notebook-integration:
    enabled: true
    base-url: ${NOTEBOOK_BASE_URL:http://localhost:8888}
    api-version: "v1"

  evaluation-templates:
    auto-load: true
    template-path: "/templates/evaluation"
    default-templates:
      - "Standard Model Evaluation"
      - "RAG System Evaluation"
      - "Index Evaluation"
      - "Agent Evaluation"
      - "HITL Evaluation"

  judge-agents:
    auto-discovery: true
    health-check-interval: 30s
    load-balancing: true
    fallback-strategy: "round-robin"

  metrics-storage:
    primary: "mlflow"
    secondary: "codeflowx"
    backup: "leka-server"
    sync-interval: 300s

  visualization:
    chart-templates: true
    auto-generation: true
    interactive-charts: true
    export-formats: ["PNG", "SVG", "PDF", "HTML"]
```

#### **8. Flujo de Evaluación Integrada**

1. **Selección de Template**: Usuario selecciona template predefinido
2. **Configuración de Métricas**: Sistema aplica configuración del template
3. **Selección de Agente Juez**: Sistema selecciona agente juez apropiado
4. **Ejecución en MLflow**: Uso de `mlflow.models.evaluate` o `mlflow.genai.evaluate`
5. **Integración con CodeflowX**: Sincronización de métricas y resultados
6. **Almacenamiento en Leka Server**: Backup y persistencia de resultados
7. **Generación de Visualizaciones**: Creación automática de gráficas
8. **Generación de Reportes**: Reportes automáticos con notebook
9. **Dashboard Integrado**: Visualización unificada de resultados
10. **Métricas en Tiempo Real**: Monitoreo continuo de evaluaciones

#### **9. Beneficios de la Integración**

- **Centralización**: Todas las métricas en MLflow y CodeflowX
- **Templates Predefinidos**: Configuración automática de evaluaciones
- **Agentes Jueces**: Evaluación automática con modelos especializados
- **Visualización Automática**: Gráficas y reportes generados automáticamente
- **Integración Completa**: MLflow + CodeflowX + Leka Server
- **Notebook Integration**: Creación de análisis personalizados
- **Métricas Estandarizadas**: Templates para diferentes tipos de evaluación
- **Escalabilidad**: Sistema distribuido y escalable
- **Auditoría**: Trazabilidad completa de evaluaciones
- **Flexibilidad**: Configuración personalizable por tipo de evaluación

## Conclusión

El módulo de Model Evaluation proporciona una evaluación completa y robusta de modelos de IA, incluyendo:

- **Evaluación de Rendimiento**: Métricas estándar y personalizadas de rendimiento
- **Análisis de Sesgos**: Detección y mitigación de sesgos en modelos
- **Análisis de Seguridad**: Pruebas de seguridad y vulnerabilidades
- **Cumplimiento Regulatorio**: Auditoría automática de cumplimiento
- **Generación de Reportes**: Reportes automáticos y personalizables
- **Monitoreo Continuo**: Seguimiento en tiempo real de métricas

El sistema está diseñado para ser escalable, auditable y cumplir con los estándares regulatorios más estrictos, proporcionando transparencia total en la evaluación de modelos de IA.
