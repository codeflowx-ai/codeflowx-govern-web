export interface GovernanceMetricsData {
  autoApprovalRate: number;
  complianceScore: number;
  averageRiskLevel: string;
  policiesApplied: number;
  totalDecisions: number;
  humanReviewRequired: number;
  lastUpdated: string;
}

export interface AutoApprovalData {
  approvalRate: number;
  autoDecisions: number;
  humanReviewRequired: number;
  chartData: ApprovalChartData[];
}

export interface ApprovalChartData {
  date: string;
  approved: number;
  rejected: number;
  pending: number;
}

export interface ComplianceData {
  frameworkScores: FrameworkScore[];
  chartData: ComplianceChartData[];
  totalChecks: number;
  compliantChecks: number;
}

export interface FrameworkScore {
  name: string;
  score: number;
  status: 'success' | 'warning' | 'error';
}

export interface ComplianceChartData {
  date: string;
  gdpr: number;
  sox: number;
  hipaa: number;
}

export interface Decision {
  id: string;
  modelName: string;
  version: string;
  status: 'approved' | 'rejected' | 'pending' | 'human_review';
  confidence: number;
  complianceScore: number;
  riskLevel: string;
  decisionTime: string;
  reason: string;
}

export interface ComplianceCheck {
  id: string;
  modelName: string;
  framework: string;
  status: 'running' | 'completed' | 'failed';
  progress: number;
  startTime: string;
  endTime?: string;
}

export interface RiskAssessmentData {
  riskLevel: string;
  riskScore: number;
  riskFactors: RiskFactor[];
  mitigationRecommendations: string[];
  chartData: RiskChartData[];
}

export interface RiskFactor {
  name: string;
  score: number;
  weight: number;
}

export interface RiskChartData {
  date: string;
  low: number;
  medium: number;
  high: number;
  critical: number;
}

export interface Policy {
  id: string;
  name: string;
  description: string;
  category: 'security' | 'compliance' | 'performance' | 'ethical';
  status: 'active' | 'inactive' | 'draft';
  enforcementLevel: 'strict' | 'moderate' | 'flexible';
  createdAt: string;
  updatedAt: string;
  rules: PolicyRule[];
}

export interface PolicyRule {
  id: string;
  name: string;
  condition: string;
  action: string;
  priority: number;
}

// API Response Types
export interface GovernanceMetricsResponse {
  success: boolean;
  metrics: GovernanceMetricsData;
}

export interface AutoApprovalResponse {
  success: boolean;
  data: AutoApprovalData;
}

export interface ComplianceResponse {
  success: boolean;
  data: ComplianceData;
}

export interface DecisionsResponse {
  success: boolean;
  decisions: Decision[];
}

export interface ComplianceChecksResponse {
  success: boolean;
  checks: ComplianceCheck[];
}

export interface RiskAssessmentResponse {
  success: boolean;
  data: RiskAssessmentData;
}

export interface PoliciesResponse {
  success: boolean;
  policies: Policy[];
}

// Request Types
export interface ManualReviewRequest {
  decisionId: string;
  reason: string;
}

export interface ComplianceCheckRequest {
  modelName: string;
  framework: string;
}

export interface PolicyUpdateRequest {
  policyId: string;
  status: 'active' | 'inactive';
  enforcementLevel: 'strict' | 'moderate' | 'flexible';
} 