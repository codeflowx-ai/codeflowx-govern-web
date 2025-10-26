package com.codeflowx.govern.workflow.drools.facts;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Fact object para evaluación de calidad de datasets
 * Utilizado en el proceso de governance de calidad de datos
 */
public class DatasetQualityFact {

    // Identificadores
    private String datasetId;
    private String datasetName;
    private String datasetVersion;
    private String dataSource;
    
    // Métricas de calidad básicas
    private double completenessScore;     // Completitud (0-1)
    private double accuracyScore;         // Precisión (0-1)
    private double consistencyScore;      // Consistencia (0-1)
    private double validityScore;         // Validez (0-1)
    private double uniquenessScore;       // Unicidad (0-1)
    private double timelinessScore;       // Actualidad (0-1)
    
    // Métricas de sesgo y equidad
    private double biasScore;             // Puntuación de sesgo (0-1, 0=sin sesgo)
    private double fairnessScore;         // Puntuación de equidad (0-1)
    private List<String> biasTypes;       // Tipos de sesgo detectados
    private Map<String, Double> demographicDistribution; // Distribución demográfica
    
    // Métricas de privacidad y cumplimiento
    private boolean piiDetected;          // Si se detectó información personal
    private int piiCount;                 // Cantidad de registros con PII
    private List<String> piiTypes;        // Tipos de PII detectados
    private boolean gdprCompliant;        // Cumplimiento GDPR
    private boolean ccpaCompliant;        // Cumplimiento CCPA
    private boolean hipaaCompliant;       // Cumplimiento HIPAA
    
    // Métricas de integridad
    private int totalRecords;             // Total de registros
    private int duplicateRecords;         // Registros duplicados
    private int nullRecords;              // Registros nulos
    private int invalidRecords;           // Registros inválidos
    private double duplicateRate;         // Tasa de duplicados (0-1)
    private double nullRate;              // Tasa de nulos (0-1)
    private double invalidRate;           // Tasa de inválidos (0-1)
    
    // Métricas de formato y estructura
    private boolean formatValid;          // Si el formato es válido
    private String expectedFormat;        // Formato esperado
    private String actualFormat;          // Formato actual
    private int schemaViolations;         // Violaciones de esquema
    private List<String> missingColumns;  // Columnas faltantes
    private List<String> extraColumns;    // Columnas extra
    
    // Métricas de distribución estadística
    private Map<String, Double> statisticalMeasures; // Medidas estadísticas
    private double outlierPercentage;     // Porcentaje de outliers
    private double varianceScore;         // Puntuación de varianza
    private boolean normalDistribution;   // Si sigue distribución normal
    
    // Puntuación final calculada
    private double overallQualityScore;   // Puntuación general (0-1)
    private String qualityGrade;          // Grado de calidad (A, B, C, D, F)
    private String riskLevel;             // Nivel de riesgo (LOW, MEDIUM, HIGH, CRITICAL)
    private boolean requiresReview;       // Si requiere revisión manual
    
    // Configuración de umbrales
    private double qualityThreshold;      // Umbral de calidad (default: 0.8)
    private double biasThreshold;         // Umbral de sesgo (default: 0.3)
    private double piiThreshold;          // Umbral de PII (default: 0.05)
    
    // Metadatos
    private LocalDateTime evaluationDate;
    private String evaluatorId;
    private String evaluationMethod;      // "automatic", "human", "hybrid"
    private String dataClassification;    // "public", "internal", "confidential", "restricted"
    
    // Resultados de la evaluación
    private List<String> recommendations; // Recomendaciones de mejora
    private String decision;              // Decisión final (APPROVED, REJECTED, REVIEW_REQUIRED)
    private String justification;         // Justificación de la decisión
    private List<String> complianceIssues; // Problemas de cumplimiento
    
    // Constructores
    public DatasetQualityFact() {
        this.qualityThreshold = 0.8;
        this.biasThreshold = 0.3;
        this.piiThreshold = 0.05;
        this.evaluationDate = LocalDateTime.now();
        this.evaluationMethod = "automatic";
        this.riskLevel = "LOW";
        this.requiresReview = false;
        this.dataClassification = "internal";
    }
    
    public DatasetQualityFact(String datasetId, String datasetName) {
        this();
        this.datasetId = datasetId;
        this.datasetName = datasetName;
    }
    
    // Métodos de cálculo
    public void calculateOverallQualityScore() {
        // Cálculo ponderado de la puntuación general
        this.overallQualityScore = (
            completenessScore * 0.20 +
            accuracyScore * 0.20 +
            consistencyScore * 0.15 +
            validityScore * 0.15 +
            uniquenessScore * 0.15 +
            timelinessScore * 0.15
        );
        
        // Ajuste por sesgo
        if (biasScore > biasThreshold) {
            this.overallQualityScore *= (1.0 - biasScore);
        }
        
        // Ajuste por PII
        if (piiDetected && piiCount > (totalRecords * piiThreshold)) {
            this.overallQualityScore *= 0.8;
        }
        
        // Ajuste por duplicados
        if (duplicateRate > 0.1) { // Más del 10% duplicados
            this.overallQualityScore *= 0.9;
        }
    }
    
    public void determineQualityGrade() {
        if (overallQualityScore >= 0.9) {
            this.qualityGrade = "A";
        } else if (overallQualityScore >= 0.8) {
            this.qualityGrade = "B";
        } else if (overallQualityScore >= 0.7) {
            this.qualityGrade = "C";
        } else if (overallQualityScore >= 0.6) {
            this.qualityGrade = "D";
        } else {
            this.qualityGrade = "F";
        }
    }
    
    public void determineRiskLevel() {
        if (overallQualityScore < 0.5 || biasScore > 0.7 || piiCount > (totalRecords * 0.1)) {
            this.riskLevel = "CRITICAL";
        } else if (overallQualityScore < 0.6 || biasScore > 0.5 || piiDetected) {
            this.riskLevel = "HIGH";
        } else if (overallQualityScore < 0.7 || biasScore > biasThreshold) {
            this.riskLevel = "MEDIUM";
        } else {
            this.riskLevel = "LOW";
        }
    }
    
    public void makeDecision() {
        if (overallQualityScore >= qualityThreshold && 
            biasScore <= biasThreshold && 
            !piiDetected && 
            gdprCompliant) {
            this.decision = "APPROVED";
            this.justification = "Dataset cumple todos los criterios de calidad y cumplimiento";
        } else if (overallQualityScore < 0.5 || 
                   biasScore > 0.7 || 
                   piiCount > (totalRecords * 0.1)) {
            this.decision = "REJECTED";
            this.justification = "Dataset no cumple criterios mínimos de calidad o presenta riesgos críticos";
        } else {
            this.decision = "REVIEW_REQUIRED";
            this.justification = "Dataset requiere revisión manual por criterios de calidad o cumplimiento";
        }
    }
    
    // Getters y Setters
    public String getDatasetId() { return datasetId; }
    public void setDatasetId(String datasetId) { this.datasetId = datasetId; }
    
    public String getDatasetName() { return datasetName; }
    public void setDatasetName(String datasetName) { this.datasetName = datasetName; }
    
    public String getDatasetVersion() { return datasetVersion; }
    public void setDatasetVersion(String datasetVersion) { this.datasetVersion = datasetVersion; }
    
    public String getDataSource() { return dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }
    
    public double getCompletenessScore() { return completenessScore; }
    public void setCompletenessScore(double completenessScore) { this.completenessScore = completenessScore; }
    
    public double getAccuracyScore() { return accuracyScore; }
    public void setAccuracyScore(double accuracyScore) { this.accuracyScore = accuracyScore; }
    
    public double getConsistencyScore() { return consistencyScore; }
    public void setConsistencyScore(double consistencyScore) { this.consistencyScore = consistencyScore; }
    
    public double getValidityScore() { return validityScore; }
    public void setValidityScore(double validityScore) { this.validityScore = validityScore; }
    
    public double getUniquenessScore() { return uniquenessScore; }
    public void setUniquenessScore(double uniquenessScore) { this.uniquenessScore = uniquenessScore; }
    
    public double getTimelinessScore() { return timelinessScore; }
    public void setTimelinessScore(double timelinessScore) { this.timelinessScore = timelinessScore; }
    
    public double getBiasScore() { return biasScore; }
    public void setBiasScore(double biasScore) { this.biasScore = biasScore; }
    
    public double getFairnessScore() { return fairnessScore; }
    public void setFairnessScore(double fairnessScore) { this.fairnessScore = fairnessScore; }
    
    public List<String> getBiasTypes() { return biasTypes; }
    public void setBiasTypes(List<String> biasTypes) { this.biasTypes = biasTypes; }
    
    public Map<String, Double> getDemographicDistribution() { return demographicDistribution; }
    public void setDemographicDistribution(Map<String, Double> demographicDistribution) { this.demographicDistribution = demographicDistribution; }
    
    public boolean isPiiDetected() { return piiDetected; }
    public void setPiiDetected(boolean piiDetected) { this.piiDetected = piiDetected; }
    
    public int getPiiCount() { return piiCount; }
    public void setPiiCount(int piiCount) { this.piiCount = piiCount; }
    
    public List<String> getPiiTypes() { return piiTypes; }
    public void setPiiTypes(List<String> piiTypes) { this.piiTypes = piiTypes; }
    
    public boolean isGdprCompliant() { return gdprCompliant; }
    public void setGdprCompliant(boolean gdprCompliant) { this.gdprCompliant = gdprCompliant; }
    
    public boolean isCcpaCompliant() { return ccpaCompliant; }
    public void setCcpaCompliant(boolean ccpaCompliant) { this.ccpaCompliant = ccpaCompliant; }
    
    public boolean isHipaaCompliant() { return hipaaCompliant; }
    public void setHipaaCompliant(boolean hipaaCompliant) { this.hipaaCompliant = hipaaCompliant; }
    
    public int getTotalRecords() { return totalRecords; }
    public void setTotalRecords(int totalRecords) { this.totalRecords = totalRecords; }
    
    public int getDuplicateRecords() { return duplicateRecords; }
    public void setDuplicateRecords(int duplicateRecords) { this.duplicateRecords = duplicateRecords; }
    
    public int getNullRecords() { return nullRecords; }
    public void setNullRecords(int nullRecords) { this.nullRecords = nullRecords; }
    
    public int getInvalidRecords() { return invalidRecords; }
    public void setInvalidRecords(int invalidRecords) { this.invalidRecords = invalidRecords; }
    
    public double getDuplicateRate() { return duplicateRate; }
    public void setDuplicateRate(double duplicateRate) { this.duplicateRate = duplicateRate; }
    
    public double getNullRate() { return nullRate; }
    public void setNullRate(double nullRate) { this.nullRate = nullRate; }
    
    public double getInvalidRate() { return invalidRate; }
    public void setInvalidRate(double invalidRate) { this.invalidRate = invalidRate; }
    
    public boolean isFormatValid() { return formatValid; }
    public void setFormatValid(boolean formatValid) { this.formatValid = formatValid; }
    
    public String getExpectedFormat() { return expectedFormat; }
    public void setExpectedFormat(String expectedFormat) { this.expectedFormat = expectedFormat; }
    
    public String getActualFormat() { return actualFormat; }
    public void setActualFormat(String actualFormat) { this.actualFormat = actualFormat; }
    
    public int getSchemaViolations() { return schemaViolations; }
    public void setSchemaViolations(int schemaViolations) { this.schemaViolations = schemaViolations; }
    
    public List<String> getMissingColumns() { return missingColumns; }
    public void setMissingColumns(List<String> missingColumns) { this.missingColumns = missingColumns; }
    
    public List<String> getExtraColumns() { return extraColumns; }
    public void setExtraColumns(List<String> extraColumns) { this.extraColumns = extraColumns; }
    
    public Map<String, Double> getStatisticalMeasures() { return statisticalMeasures; }
    public void setStatisticalMeasures(Map<String, Double> statisticalMeasures) { this.statisticalMeasures = statisticalMeasures; }
    
    public double getOutlierPercentage() { return outlierPercentage; }
    public void setOutlierPercentage(double outlierPercentage) { this.outlierPercentage = outlierPercentage; }
    
    public double getVarianceScore() { return varianceScore; }
    public void setVarianceScore(double varianceScore) { this.varianceScore = varianceScore; }
    
    public boolean isNormalDistribution() { return normalDistribution; }
    public void setNormalDistribution(boolean normalDistribution) { this.normalDistribution = normalDistribution; }
    
    public double getOverallQualityScore() { return overallQualityScore; }
    public void setOverallQualityScore(double overallQualityScore) { this.overallQualityScore = overallQualityScore; }
    
    public String getQualityGrade() { return qualityGrade; }
    public void setQualityGrade(String qualityGrade) { this.qualityGrade = qualityGrade; }
    
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    
    public boolean isRequiresReview() { return requiresReview; }
    public void setRequiresReview(boolean requiresReview) { this.requiresReview = requiresReview; }
    
    public double getQualityThreshold() { return qualityThreshold; }
    public void setQualityThreshold(double qualityThreshold) { this.qualityThreshold = qualityThreshold; }
    
    public double getBiasThreshold() { return biasThreshold; }
    public void setBiasThreshold(double biasThreshold) { this.biasThreshold = biasThreshold; }
    
    public double getPiiThreshold() { return piiThreshold; }
    public void setPiiThreshold(double piiThreshold) { this.piiThreshold = piiThreshold; }
    
    public LocalDateTime getEvaluationDate() { return evaluationDate; }
    public void setEvaluationDate(LocalDateTime evaluationDate) { this.evaluationDate = evaluationDate; }
    
    public String getEvaluatorId() { return evaluatorId; }
    public void setEvaluatorId(String evaluatorId) { this.evaluatorId = evaluatorId; }
    
    public String getEvaluationMethod() { return evaluationMethod; }
    public void setEvaluationMethod(String evaluationMethod) { this.evaluationMethod = evaluationMethod; }
    
    public String getDataClassification() { return dataClassification; }
    public void setDataClassification(String dataClassification) { this.dataClassification = dataClassification; }
    
    public List<String> getRecommendations() { return recommendations; }
    public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }
    
    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
    
    public String getJustification() { return justification; }
    public void setJustification(String justification) { this.justification = justification; }
    
    public List<String> getComplianceIssues() { return complianceIssues; }
    public void setComplianceIssues(List<String> complianceIssues) { this.complianceIssues = complianceIssues; }
    
    @Override
    public String toString() {
        return "DatasetQualityFact{" +
                "datasetId='" + datasetId + '\'' +
                ", datasetName='" + datasetName + '\'' +
                ", overallQualityScore=" + overallQualityScore +
                ", qualityGrade='" + qualityGrade + '\'' +
                ", riskLevel='" + riskLevel + '\'' +
                ", decision='" + decision + '\'' +
                '}';
    }
}