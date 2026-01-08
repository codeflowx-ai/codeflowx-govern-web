// Exportar todas las traducciones de governance por pantalla
import { governanceDashboardTranslations } from "./dashboard";
import { governancePoliciesOverviewTranslations } from "./policies-overview";
import { governancePolicyDetailTranslations } from "./policy-detail";
import { governanceSecurityTranslations } from "./security";
import { governanceMonitoringTranslations } from "./monitoring";
import { governanceComplianceTranslations } from "./compliance";
import { governanceComplianceDashboardTranslations } from "./compliance-dashboard";
import { governanceProhibitedSystemsTranslations } from "./prohibited-systems";
import { governanceImmutableLogsTranslations } from "./immutable-logs";
import { governanceSectorTranslations } from "./sector";
import { governanceTraceabilityTranslations } from "./traceability";
import { governanceQmsTranslations } from "./qms";
import { governanceHitlSupervisionTranslations } from "./hitl-supervision";
import { governanceTechnicalDocsTranslations } from "./technical-docs";
import { governanceConformityReviewTranslations } from "./conformity-review";
import { governanceRiskAssessmentTranslations } from "./risk-assessment";
import { governanceAutoApprovalTranslations } from "./auto-approval";
import { governanceClassificationTranslations } from "./classification";
import { governanceModelsApprovalTranslations } from "./models-approval";
import { governanceModelsBiasAnalysisTranslations } from "./models-bias-analysis";
import { governanceModelsExplainabilityTranslations } from "./models-explainability";
import { governanceModelsPerformanceTranslations } from "./models-performance";
import { governanceModelsUsageOverviewTranslations } from "./models-usage-overview";
import { governanceModelsRegistryTranslations } from "./models-registry";
import { governanceModelsProvidersTranslations } from "./models-providers";
import { dataGovernanceTranslations } from "./data/index";

// Combinar todas las traducciones de governance
export const governanceTranslations = {
  es: {
    dashboard: governanceDashboardTranslations.es,
    overview: governancePoliciesOverviewTranslations.es,
    detail: governancePolicyDetailTranslations.es,
    security: governanceSecurityTranslations.es,
    monitoring: governanceMonitoringTranslations.es,
    compliance: governanceComplianceTranslations.es,
    complianceDashboard: governanceComplianceDashboardTranslations.es,
    prohibitedSystems: governanceProhibitedSystemsTranslations.es,
    immutableLogs: governanceImmutableLogsTranslations.es,
    sector: governanceSectorTranslations.es,
    traceability: governanceTraceabilityTranslations.es,
    qms: governanceQmsTranslations.es,
    hitlSupervision: governanceHitlSupervisionTranslations.es,
    technicalDocs: governanceTechnicalDocsTranslations.es,
    conformityReview: governanceConformityReviewTranslations.es,
    riskAssessment: governanceRiskAssessmentTranslations.es,
    autoApproval: governanceAutoApprovalTranslations.es,
    classification: governanceClassificationTranslations.es,
    models: {
      approval: governanceModelsApprovalTranslations.es,
      biasAnalysis: governanceModelsBiasAnalysisTranslations.es,
      explainability: governanceModelsExplainabilityTranslations.es,
      performance: governanceModelsPerformanceTranslations.es,
      usageOverview: governanceModelsUsageOverviewTranslations.es,
      registry: governanceModelsRegistryTranslations.es,
      providers: governanceModelsProvidersTranslations.es,
    },
    data: dataGovernanceTranslations.es,
  },
  en: {
    dashboard: governanceDashboardTranslations.en,
    overview: governancePoliciesOverviewTranslations.en,
    detail: governancePolicyDetailTranslations.en,
    security: governanceSecurityTranslations.en,
    monitoring: governanceMonitoringTranslations.en,
    compliance: governanceComplianceTranslations.en,
    complianceDashboard: governanceComplianceDashboardTranslations.en,
    prohibitedSystems: governanceProhibitedSystemsTranslations.en,
    immutableLogs: governanceImmutableLogsTranslations.en,
    sector: governanceSectorTranslations.en,
    traceability: governanceTraceabilityTranslations.en,
    qms: governanceQmsTranslations.en,
    hitlSupervision: governanceHitlSupervisionTranslations.en,
    technicalDocs: governanceTechnicalDocsTranslations.en,
    conformityReview: governanceConformityReviewTranslations.en,
    riskAssessment: governanceRiskAssessmentTranslations.en,
    autoApproval: governanceAutoApprovalTranslations.en,
    classification: governanceClassificationTranslations.en,
    models: {
      approval: governanceModelsApprovalTranslations.en,
      biasAnalysis: governanceModelsBiasAnalysisTranslations.en,
      explainability: governanceModelsExplainabilityTranslations.en,
      performance: governanceModelsPerformanceTranslations.en,
      usageOverview: governanceModelsUsageOverviewTranslations.en,
      registry: governanceModelsRegistryTranslations.en,
      providers: governanceModelsProvidersTranslations.en,
    },
    data: dataGovernanceTranslations.en,
  },
  fr: {
    dashboard: governanceDashboardTranslations.fr,
    overview: governancePoliciesOverviewTranslations.fr,
    detail: governancePolicyDetailTranslations.fr,
    security: governanceSecurityTranslations.fr,
    monitoring: governanceMonitoringTranslations.fr,
    compliance: governanceComplianceTranslations.fr,
    complianceDashboard: governanceComplianceDashboardTranslations.fr,
    prohibitedSystems: governanceProhibitedSystemsTranslations.fr,
    immutableLogs: governanceImmutableLogsTranslations.fr,
    sector: governanceSectorTranslations.fr,
    traceability: governanceTraceabilityTranslations.fr,
    qms: governanceQmsTranslations.fr,
    hitlSupervision: governanceHitlSupervisionTranslations.fr,
    technicalDocs: governanceTechnicalDocsTranslations.fr,
    conformityReview: governanceConformityReviewTranslations.fr,
    riskAssessment: governanceRiskAssessmentTranslations.fr,
    autoApproval: governanceAutoApprovalTranslations.fr,
    classification: governanceClassificationTranslations.fr,
    models: {
      approval: governanceModelsApprovalTranslations.fr,
      biasAnalysis: governanceModelsBiasAnalysisTranslations.fr,
      explainability: governanceModelsExplainabilityTranslations.fr,
      performance: governanceModelsPerformanceTranslations.fr,
      usageOverview: governanceModelsUsageOverviewTranslations.fr,
      registry: governanceModelsRegistryTranslations.fr,
      providers: governanceModelsProvidersTranslations.fr,
    },
    data: dataGovernanceTranslations.fr,
  },
  de: {
    dashboard: governanceDashboardTranslations.de,
    overview: governancePoliciesOverviewTranslations.de,
    detail: governancePolicyDetailTranslations.de,
    security: governanceSecurityTranslations.de,
    monitoring: governanceMonitoringTranslations.de,
    compliance: governanceComplianceTranslations.de,
    complianceDashboard: governanceComplianceDashboardTranslations.de,
    prohibitedSystems: governanceProhibitedSystemsTranslations.de,
    immutableLogs: governanceImmutableLogsTranslations.de,
    sector: governanceSectorTranslations.de,
    traceability: governanceTraceabilityTranslations.de,
    qms: governanceQmsTranslations.de,
    hitlSupervision: governanceHitlSupervisionTranslations.de,
    technicalDocs: governanceTechnicalDocsTranslations.de,
    conformityReview: governanceConformityReviewTranslations.de,
    riskAssessment: governanceRiskAssessmentTranslations.de,
    autoApproval: governanceAutoApprovalTranslations.de,
    classification: governanceClassificationTranslations.de,
    models: {
      approval: governanceModelsApprovalTranslations.de,
      biasAnalysis: governanceModelsBiasAnalysisTranslations.de,
      explainability: governanceModelsExplainabilityTranslations.de,
      performance: governanceModelsPerformanceTranslations.de,
      usageOverview: governanceModelsUsageOverviewTranslations.de,
      registry: governanceModelsRegistryTranslations.de,
      providers: governanceModelsProvidersTranslations.de,
    },
    data: dataGovernanceTranslations.de,
  },
  it: {
    dashboard: governanceDashboardTranslations.it,
    overview: governancePoliciesOverviewTranslations.it,
    detail: governancePolicyDetailTranslations.it,
    security: governanceSecurityTranslations.it,
    monitoring: governanceMonitoringTranslations.it,
    compliance: governanceComplianceTranslations.it,
    complianceDashboard: governanceComplianceDashboardTranslations.it,
    prohibitedSystems: governanceProhibitedSystemsTranslations.it,
    immutableLogs: governanceImmutableLogsTranslations.it,
    sector: governanceSectorTranslations.it,
    traceability: governanceTraceabilityTranslations.it,
    qms: governanceQmsTranslations.it,
    hitlSupervision: governanceHitlSupervisionTranslations.it,
    technicalDocs: governanceTechnicalDocsTranslations.it,
    conformityReview: governanceConformityReviewTranslations.it,
    riskAssessment: governanceRiskAssessmentTranslations.it,
    autoApproval: governanceAutoApprovalTranslations.it,
    classification: governanceClassificationTranslations.it,
    models: {
      approval: governanceModelsApprovalTranslations.it,
      biasAnalysis: governanceModelsBiasAnalysisTranslations.it,
      explainability: governanceModelsExplainabilityTranslations.it,
      performance: governanceModelsPerformanceTranslations.it,
      usageOverview: governanceModelsUsageOverviewTranslations.it,
      registry: governanceModelsRegistryTranslations.it,
      providers: governanceModelsProvidersTranslations.it,
    },
    data: dataGovernanceTranslations.it,
  },
  pt: {
    dashboard: governanceDashboardTranslations.pt,
    overview: governancePoliciesOverviewTranslations.pt,
    detail: governancePolicyDetailTranslations.pt,
    security: governanceSecurityTranslations.pt,
    monitoring: governanceMonitoringTranslations.pt,
    compliance: governanceComplianceTranslations.pt,
    complianceDashboard: governanceComplianceDashboardTranslations.pt,
    prohibitedSystems: governanceProhibitedSystemsTranslations.pt,
    immutableLogs: governanceImmutableLogsTranslations.pt,
    sector: governanceSectorTranslations.pt,
    traceability: governanceTraceabilityTranslations.pt,
    qms: governanceQmsTranslations.pt,
    hitlSupervision: governanceHitlSupervisionTranslations.pt,
    technicalDocs: governanceTechnicalDocsTranslations.pt,
    conformityReview: governanceConformityReviewTranslations.pt,
    riskAssessment: governanceRiskAssessmentTranslations.pt,
    autoApproval: governanceAutoApprovalTranslations.pt,
    classification: governanceClassificationTranslations.pt,
    models: {
      approval: governanceModelsApprovalTranslations.pt,
      biasAnalysis: governanceModelsBiasAnalysisTranslations.pt,
      explainability: governanceModelsExplainabilityTranslations.pt,
      performance: governanceModelsPerformanceTranslations.pt,
      usageOverview: governanceModelsUsageOverviewTranslations.pt,
      registry: governanceModelsRegistryTranslations.pt,
      providers: governanceModelsProvidersTranslations.pt,
    },
    data: dataGovernanceTranslations.pt,
  },
};
