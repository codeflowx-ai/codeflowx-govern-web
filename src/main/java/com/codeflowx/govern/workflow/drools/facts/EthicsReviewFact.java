package com.codeflowx.govern.workflow.drools.facts;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Fact object para revisión ética de sistemas IA
 * Utilizado en el proceso de evaluación ética y de impacto social
 */
public class EthicsReviewFact {
    
    // Identificadores
    private String reviewId;
    private String systemId;
    private String systemName;
    private String systemType;          // "generative_ai", "predictive", "decision_making", "recommendation"
    
    // Áreas de impacto ético
    private double fairnessScore;        // Equidad (0-1)
    private double transparencyScore;    // Transparencia (0-1)
    private double accountabilityScore;  // Responsabilidad (0-1)
    private double privacyScore;         // Privacidad (0-1)
    private double safetyScore;          // Seguridad (0-1)
    private double autonomyScore;        // Autonomía humana (0-1)
    
    // Evaluación de riesgos éticos
    private boolean discriminationRisk;  // Riesgo de discriminación
    private boolean manipulationRisk;    // Riesgo de manipulación
    private boolean surveillanceRisk;    // Riesgo de vigilancia masiva
    private boolean environmentalRisk;   // Riesgo ambiental
    private boolean jobDisplacementRisk; // Riesgo de desplazamiento laboral
    private boolean dualUseRisk;         // Riesgo de doble uso (militar/civil)
    
    // Detalles de violaciones éticas
    private boolean violationsDetected;
    private List<String> violationTypes;  // Tipos de violaciones
    private List<String> affectedGroups;  // Grupos afectados
    private String severityLevel;         // "LOW", "MEDIUM", "HIGH", "CRITICAL"
    
    // Stakeholders y afectados
    private List<String> primaryStakeholders;
    private List<String> secondaryStakeholders;
    private int estimatedAffectedUsers;
    private List<String> vulnerableGroups;  // Grupos vulnerables afectados
    
    // Cumplimiento regulatorio
    private boolean euAiActCompliant;     // EU AI Act
    private boolean gdprCompliant;        // GDPR
    private boolean ethicsGuidelinesCompliant; // Guidelines internas
    private String aiRiskCategory;        // "minimal", "limited", "high", "unacceptable"
    
    // Evaluación de transparencia
    private boolean explainabilityProvided;
    private boolean documentationComplete;
    private boolean impactAssessmentDone;
    private boolean stakeholdersConsulted;
    
    // Métricas de sesgo y equidad
    private double demographicParityScore;
    private double equalizedOddsScore;
    private List<String> identifiedBiases;
    private Map<String, Double> groupFairnessMetrics;
    
    // Puntuación final calculada
    private double overallEthicsScore;    // Puntuación general (0-1)
    private String ethicsGrade;           // Grado ético (A, B, C, D, F)
    private String riskLevel;             // Nivel de riesgo (LOW, MEDIUM, HIGH, CRITICAL)
    private boolean requiresCommitteeReview;
    
    // Configuración de umbrales
    private double ethicsThreshold;       // Umbral ético (default: 0.7)
    private double fairnessThreshold;     // Umbral de equidad (default: 0.8)
    
    // Metadatos
    private LocalDateTime evaluationDate;
    private String evaluatorId;
    private String evaluationMethod;      // "automatic", "human", "hybrid"
    
    // Resultados de la evaluación
    private List<String> recommendations; // Recomendaciones
    private String decision;              // Decisión final (APPROVED, CONDITIONAL, REJECTED)
    private String justification;         // Justificación
    private List<String> mitigationActions; // Acciones de mitigación requeridas
    
    // Constructores
    public EthicsReviewFact() {
        this.ethicsThreshold = 0.7;
        this.fairnessThreshold = 0.8;
        this.evaluationDate = LocalDateTime.now();
        this.evaluationMethod = "automatic";
        this.riskLevel = "LOW";
        this.requiresCommitteeReview = false;
    }
    
    public EthicsReviewFact(String reviewId, String systemId, String systemName) {
        this();
        this.reviewId = reviewId;
        this.systemId = systemId;
        this.systemName = systemName;
    }
    
    // Métodos de cálculo
    public void calculateOverallEthicsScore() {
        // Cálculo ponderado de la puntuación ética general
        this.overallEthicsScore = (
            fairnessScore * 0.25 +
            transparencyScore * 0.20 +
            accountabilityScore * 0.15 +
            privacyScore * 0.20 +
            safetyScore * 0.15 +
            autonomyScore * 0.05
        );
        
        // Ajuste por riesgos críticos
        int criticalRisks = 0;
        if (discriminationRisk) criticalRisks++;
        if (manipulationRisk) criticalRisks++;
        if (surveillanceRisk) criticalRisks++;
        if (dualUseRisk) criticalRisks++;
        
        if (criticalRisks > 0) {
            this.overallEthicsScore *= (1.0 - (criticalRisks * 0.15));
        }
        
        // Ajuste por cumplimiento regulatorio
        if (!euAiActCompliant || !gdprCompliant) {
            this.overallEthicsScore *= 0.8;
        }
    }
    
    public void determineEthicsGrade() {
        if (overallEthicsScore >= 0.9) {
            this.ethicsGrade = "A";
        } else if (overallEthicsScore >= 0.8) {
            this.ethicsGrade = "B";
        } else if (overallEthicsScore >= 0.7) {
            this.ethicsGrade = "C";
        } else if (overallEthicsScore >= 0.6) {
            this.ethicsGrade = "D";
        } else {
            this.ethicsGrade = "F";
        }
    }
    
    public void determineRiskLevel() {
        if (overallEthicsScore < 0.5 || dualUseRisk || discriminationRisk) {
            this.riskLevel = "CRITICAL";
        } else if (overallEthicsScore < 0.6 || manipulationRisk || surveillanceRisk) {
            this.riskLevel = "HIGH";
        } else if (overallEthicsScore < 0.7 || violationsDetected) {
            this.riskLevel = "MEDIUM";
        } else {
            this.riskLevel = "LOW";
        }
    }
    
    public void makeDecision() {
        if (overallEthicsScore >= ethicsThreshold && 
            fairnessScore >= fairnessThreshold && 
            !discriminationRisk &&
            !dualUseRisk &&
            euAiActCompliant &&
            gdprCompliant) {
            this.decision = "APPROVED";
            this.justification = "Sistema cumple todos los criterios éticos y regulatorios";
        } else if (overallEthicsScore < 0.5 || 
                   dualUseRisk || 
                   aiRiskCategory.equals("unacceptable")) {
            this.decision = "REJECTED";
            this.justification = "Sistema presenta riesgos éticos inaceptables";
        } else {
            this.decision = "CONDITIONAL";
            this.justification = "Sistema requiere acciones de mitigación antes de aprobación";
        }
    }
    
    public void determineCommitteeReview() {
        this.requiresCommitteeReview = (
            violationsDetected ||
            overallEthicsScore < ethicsThreshold ||
            discriminationRisk ||
            manipulationRisk ||
            surveillanceRisk ||
            dualUseRisk ||
            aiRiskCategory.equals("high") ||
            aiRiskCategory.equals("unacceptable") ||
            !euAiActCompliant
        );
    }
    
    // Getters y Setters
    public String getReviewId() { return reviewId; }
    public void setReviewId(String reviewId) { this.reviewId = reviewId; }
    
    public String getSystemId() { return systemId; }
    public void setSystemId(String systemId) { this.systemId = systemId; }
    
    public String getSystemName() { return systemName; }
    public void setSystemName(String systemName) { this.systemName = systemName; }
    
    public String getSystemType() { return systemType; }
    public void setSystemType(String systemType) { this.systemType = systemType; }
    
    public double getFairnessScore() { return fairnessScore; }
    public void setFairnessScore(double fairnessScore) { this.fairnessScore = fairnessScore; }
    
    public double getTransparencyScore() { return transparencyScore; }
    public void setTransparencyScore(double transparencyScore) { this.transparencyScore = transparencyScore; }
    
    public double getAccountabilityScore() { return accountabilityScore; }
    public void setAccountabilityScore(double accountabilityScore) { this.accountabilityScore = accountabilityScore; }
    
    public double getPrivacyScore() { return privacyScore; }
    public void setPrivacyScore(double privacyScore) { this.privacyScore = privacyScore; }
    
    public double getSafetyScore() { return safetyScore; }
    public void setSafetyScore(double safetyScore) { this.safetyScore = safetyScore; }
    
    public double getAutonomyScore() { return autonomyScore; }
    public void setAutonomyScore(double autonomyScore) { this.autonomyScore = autonomyScore; }
    
    public boolean isDiscriminationRisk() { return discriminationRisk; }
    public void setDiscriminationRisk(boolean discriminationRisk) { this.discriminationRisk = discriminationRisk; }
    
    public boolean isManipulationRisk() { return manipulationRisk; }
    public void setManipulationRisk(boolean manipulationRisk) { this.manipulationRisk = manipulationRisk; }
    
    public boolean isSurveillanceRisk() { return surveillanceRisk; }
    public void setSurveillanceRisk(boolean surveillanceRisk) { this.surveillanceRisk = surveillanceRisk; }
    
    public boolean isEnvironmentalRisk() { return environmentalRisk; }
    public void setEnvironmentalRisk(boolean environmentalRisk) { this.environmentalRisk = environmentalRisk; }
    
    public boolean isJobDisplacementRisk() { return jobDisplacementRisk; }
    public void setJobDisplacementRisk(boolean jobDisplacementRisk) { this.jobDisplacementRisk = jobDisplacementRisk; }
    
    public boolean isDualUseRisk() { return dualUseRisk; }
    public void setDualUseRisk(boolean dualUseRisk) { this.dualUseRisk = dualUseRisk; }
    
    public boolean isViolationsDetected() { return violationsDetected; }
    public void setViolationsDetected(boolean violationsDetected) { this.violationsDetected = violationsDetected; }
    
    public List<String> getViolationTypes() { return violationTypes; }
    public void setViolationTypes(List<String> violationTypes) { this.violationTypes = violationTypes; }
    
    public List<String> getAffectedGroups() { return affectedGroups; }
    public void setAffectedGroups(List<String> affectedGroups) { this.affectedGroups = affectedGroups; }
    
    public String getSeverityLevel() { return severityLevel; }
    public void setSeverityLevel(String severityLevel) { this.severityLevel = severityLevel; }
    
    public List<String> getPrimaryStakeholders() { return primaryStakeholders; }
    public void setPrimaryStakeholders(List<String> primaryStakeholders) { this.primaryStakeholders = primaryStakeholders; }
    
    public List<String> getSecondaryStakeholders() { return secondaryStakeholders; }
    public void setSecondaryStakeholders(List<String> secondaryStakeholders) { this.secondaryStakeholders = secondaryStakeholders; }
    
    public int getEstimatedAffectedUsers() { return estimatedAffectedUsers; }
    public void setEstimatedAffectedUsers(int estimatedAffectedUsers) { this.estimatedAffectedUsers = estimatedAffectedUsers; }
    
    public List<String> getVulnerableGroups() { return vulnerableGroups; }
    public void setVulnerableGroups(List<String> vulnerableGroups) { this.vulnerableGroups = vulnerableGroups; }
    
    public boolean isEuAiActCompliant() { return euAiActCompliant; }
    public void setEuAiActCompliant(boolean euAiActCompliant) { this.euAiActCompliant = euAiActCompliant; }
    
    public boolean isGdprCompliant() { return gdprCompliant; }
    public void setGdprCompliant(boolean gdprCompliant) { this.gdprCompliant = gdprCompliant; }
    
    public boolean isEthicsGuidelinesCompliant() { return ethicsGuidelinesCompliant; }
    public void setEthicsGuidelinesCompliant(boolean ethicsGuidelinesCompliant) { this.ethicsGuidelinesCompliant = ethicsGuidelinesCompliant; }
    
    public String getAiRiskCategory() { return aiRiskCategory; }
    public void setAiRiskCategory(String aiRiskCategory) { this.aiRiskCategory = aiRiskCategory; }
    
    public boolean isExplainabilityProvided() { return explainabilityProvided; }
    public void setExplainabilityProvided(boolean explainabilityProvided) { this.explainabilityProvided = explainabilityProvided; }
    
    public boolean isDocumentationComplete() { return documentationComplete; }
    public void setDocumentationComplete(boolean documentationComplete) { this.documentationComplete = documentationComplete; }
    
    public boolean isImpactAssessmentDone() { return impactAssessmentDone; }
    public void setImpactAssessmentDone(boolean impactAssessmentDone) { this.impactAssessmentDone = impactAssessmentDone; }
    
    public boolean isStakeholdersConsulted() { return stakeholdersConsulted; }
    public void setStakeholdersConsulted(boolean stakeholdersConsulted) { this.stakeholdersConsulted = stakeholdersConsulted; }
    
    public double getDemographicParityScore() { return demographicParityScore; }
    public void setDemographicParityScore(double demographicParityScore) { this.demographicParityScore = demographicParityScore; }
    
    public double getEqualizedOddsScore() { return equalizedOddsScore; }
    public void setEqualizedOddsScore(double equalizedOddsScore) { this.equalizedOddsScore = equalizedOddsScore; }
    
    public List<String> getIdentifiedBiases() { return identifiedBiases; }
    public void setIdentifiedBiases(List<String> identifiedBiases) { this.identifiedBiases = identifiedBiases; }
    
    public Map<String, Double> getGroupFairnessMetrics() { return groupFairnessMetrics; }
    public void setGroupFairnessMetrics(Map<String, Double> groupFairnessMetrics) { this.groupFairnessMetrics = groupFairnessMetrics; }
    
    public double getOverallEthicsScore() { return overallEthicsScore; }
    public void setOverallEthicsScore(double overallEthicsScore) { this.overallEthicsScore = overallEthicsScore; }
    
    public String getEthicsGrade() { return ethicsGrade; }
    public void setEthicsGrade(String ethicsGrade) { this.ethicsGrade = ethicsGrade; }
    
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    
    public boolean isRequiresCommitteeReview() { return requiresCommitteeReview; }
    public void setRequiresCommitteeReview(boolean requiresCommitteeReview) { this.requiresCommitteeReview = requiresCommitteeReview; }
    
    public double getEthicsThreshold() { return ethicsThreshold; }
    public void setEthicsThreshold(double ethicsThreshold) { this.ethicsThreshold = ethicsThreshold; }
    
    public double getFairnessThreshold() { return fairnessThreshold; }
    public void setFairnessThreshold(double fairnessThreshold) { this.fairnessThreshold = fairnessThreshold; }
    
    public LocalDateTime getEvaluationDate() { return evaluationDate; }
    public void setEvaluationDate(LocalDateTime evaluationDate) { this.evaluationDate = evaluationDate; }
    
    public String getEvaluatorId() { return evaluatorId; }
    public void setEvaluatorId(String evaluatorId) { this.evaluatorId = evaluatorId; }
    
    public String getEvaluationMethod() { return evaluationMethod; }
    public void setEvaluationMethod(String evaluationMethod) { this.evaluationMethod = evaluationMethod; }
    
    public List<String> getRecommendations() { return recommendations; }
    public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }
    
    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
    
    public String getJustification() { return justification; }
    public void setJustification(String justification) { this.justification = justification; }
    
    public List<String> getMitigationActions() { return mitigationActions; }
    public void setMitigationActions(List<String> mitigationActions) { this.mitigationActions = mitigationActions; }
    
    @Override
    public String toString() {
        return "EthicsReviewFact{" +
                "reviewId='" + reviewId + '\'' +
                ", systemName='" + systemName + '\'' +
                ", overallEthicsScore=" + overallEthicsScore +
                ", ethicsGrade='" + ethicsGrade + '\'' +
                ", riskLevel='" + riskLevel + '\'' +
                ", decision='" + decision + '\'' +
                '}';
    }
}
