import {
  GovernanceMetricsData,
  AutoApprovalData,
  ComplianceData,
  Decision,
  ComplianceCheck,
  RiskAssessmentData,
  Policy,
  ApprovalChartData,
  ComplianceChartData,
  RiskChartData,
  FrameworkScore,
  RiskFactor
} from '../types/governance';

// Helper functions
const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

const simulateApiCall = async <T>(data: T, errorRate: number = 0.1): Promise<T> => {
  await delay(Math.random() * 1000 + 500); // 500-1500ms delay
  if (Math.random() < errorRate) {
    throw new Error('Simulated API error');
  }
  return data;
};

const simulateError = (message: string) => {
  throw new Error(message);
};

const getRandomSuccess = () => Math.random() > 0.1;

// Mock Governance Metrics
export const mockGovernanceMetrics: GovernanceMetricsData = {
  autoApprovalRate: 87.5,
  complianceScore: 94.2,
  averageRiskLevel: 'LOW',
  policiesApplied: 156,
  totalDecisions: 1247,
  humanReviewRequired: 23,
  lastUpdated: new Date().toISOString()
};

// Mock Auto Approval Data
export const mockApprovalChartData: ApprovalChartData[] = [
  { date: '2024-01-01', approved: 45, rejected: 3, pending: 2 },
  { date: '2024-01-02', approved: 52, rejected: 2, pending: 1 },
  { date: '2024-01-03', approved: 38, rejected: 4, pending: 3 },
  { date: '2024-01-04', approved: 61, rejected: 1, pending: 0 },
  { date: '2024-01-05', approved: 47, rejected: 3, pending: 2 },
  { date: '2024-01-06', approved: 43, rejected: 2, pending: 1 },
  { date: '2024-01-07', approved: 55, rejected: 1, pending: 1 }
];

export const mockAutoApprovalData: AutoApprovalData = {
  approvalRate: 87.5,
  autoDecisions: 1247,
  humanReviewRequired: 23,
  chartData: mockApprovalChartData
};

// Mock Compliance Data
export const mockFrameworkScores: FrameworkScore[] = [
  { name: 'GDPR', score: 96.5, status: 'success' },
  { name: 'SOX', score: 88.2, status: 'success' },
  { name: 'HIPAA', score: 92.1, status: 'success' },
  { name: 'PCI DSS', score: 85.7, status: 'warning' },
  { name: 'ISO 27001', score: 94.3, status: 'success' }
];

export const mockComplianceChartData: ComplianceChartData[] = [
  { date: '2024-01-01', gdpr: 95, sox: 87, hipaa: 91 },
  { date: '2024-01-02', gdpr: 96, sox: 88, hipaa: 92 },
  { date: '2024-01-03', gdpr: 97, sox: 89, hipaa: 93 },
  { date: '2024-01-04', gdpr: 96, sox: 88, hipaa: 92 },
  { date: '2024-01-05', gdpr: 97, sox: 90, hipaa: 93 },
  { date: '2024-01-06', gdpr: 96, sox: 89, hipaa: 92 },
  { date: '2024-01-07', gdpr: 97, sox: 88, hipaa: 93 }
];

export const mockComplianceData: ComplianceData = {
  frameworkScores: mockFrameworkScores,
  chartData: mockComplianceChartData,
  totalChecks: 342,
  compliantChecks: 318
};

// Mock Decisions
export const mockDecisions: Decision[] = [
  {
    id: 'dec_001',
    modelName: 'gpt-4-finetuned',
    version: '1.2.0',
    status: 'approved',
    confidence: 94.5,
    complianceScore: 96.2,
    riskLevel: 'LOW',
    decisionTime: '2024-01-07T10:30:00Z',
    reason: 'Model meets all compliance requirements and risk thresholds'
  },
  {
    id: 'dec_002',
    modelName: 'llama-2-70b',
    version: '2.1.0',
    status: 'human_review',
    confidence: 78.3,
    complianceScore: 82.1,
    riskLevel: 'MEDIUM',
    decisionTime: '2024-01-07T09:15:00Z',
    reason: 'Requires manual review due to medium risk level'
  },
  {
    id: 'dec_003',
    modelName: 'claude-3-opus',
    version: '1.0.0',
    status: 'rejected',
    confidence: 45.2,
    complianceScore: 67.8,
    riskLevel: 'HIGH',
    decisionTime: '2024-01-07T08:45:00Z',
    reason: 'Failed compliance checks and exceeded risk thresholds'
  },
  {
    id: 'dec_004',
    modelName: 'mistral-7b',
    version: '0.2.1',
    status: 'approved',
    confidence: 91.7,
    complianceScore: 94.5,
    riskLevel: 'LOW',
    decisionTime: '2024-01-07T07:30:00Z',
    reason: 'Model approved with minor compliance notes'
  },
  {
    id: 'dec_005',
    modelName: 'falcon-180b',
    version: '1.1.0',
    status: 'pending',
    confidence: 0,
    complianceScore: 0,
    riskLevel: 'UNKNOWN',
    decisionTime: '2024-01-07T06:20:00Z',
    reason: 'Assessment in progress'
  }
];

// Mock Compliance Checks
export const mockComplianceChecks: ComplianceCheck[] = [
  {
    id: 'check_001',
    modelName: 'gpt-4-finetuned',
    framework: 'GDPR',
    status: 'completed',
    progress: 100,
    startTime: '2024-01-07T10:00:00Z',
    endTime: '2024-01-07T10:05:00Z'
  },
  {
    id: 'check_002',
    modelName: 'llama-2-70b',
    framework: 'SOX',
    status: 'running',
    progress: 65,
    startTime: '2024-01-07T09:30:00Z'
  },
  {
    id: 'check_003',
    modelName: 'claude-3-opus',
    framework: 'HIPAA',
    status: 'failed',
    progress: 100,
    startTime: '2024-01-07T08:00:00Z',
    endTime: '2024-01-07T08:02:00Z'
  },
  {
    id: 'check_004',
    modelName: 'mistral-7b',
    framework: 'PCI DSS',
    status: 'completed',
    progress: 100,
    startTime: '2024-01-07T07:00:00Z',
    endTime: '2024-01-07T07:03:00Z'
  },
  {
    id: 'check_005',
    modelName: 'falcon-180b',
    framework: 'ISO 27001',
    status: 'running',
    progress: 25,
    startTime: '2024-01-07T06:00:00Z'
  }
];

// Mock Risk Assessment Data
export const mockRiskFactors: RiskFactor[] = [
  { name: 'Data Privacy Risk', score: 15, weight: 0.3 },
  { name: 'Security Vulnerability', score: 8, weight: 0.25 },
  { name: 'Bias and Fairness', score: 12, weight: 0.2 },
  { name: 'Performance Risk', score: 6, weight: 0.15 },
  { name: 'Compliance Risk', score: 4, weight: 0.1 }
];

export const mockRiskChartData: RiskChartData[] = [
  { date: '2024-01-01', low: 45, medium: 12, high: 3, critical: 0 },
  { date: '2024-01-02', low: 48, medium: 10, high: 2, critical: 0 },
  { date: '2024-01-03', low: 42, medium: 15, high: 3, critical: 0 },
  { date: '2024-01-04', low: 50, medium: 8, high: 2, critical: 0 },
  { date: '2024-01-05', low: 46, medium: 11, high: 3, critical: 0 },
  { date: '2024-01-06', low: 44, medium: 13, high: 3, critical: 0 },
  { date: '2024-01-07', low: 47, medium: 10, high: 2, critical: 1 }
];

export const mockRiskAssessmentData: RiskAssessmentData = {
  riskLevel: 'LOW',
  riskScore: 45.2,
  riskFactors: mockRiskFactors,
  mitigationRecommendations: [
    'Implement additional data anonymization techniques',
    'Conduct regular bias audits on model outputs',
    'Enhance security monitoring for model access',
    'Establish automated compliance checks'
  ],
  chartData: mockRiskChartData
};

// Mock Policies
export const mockPolicies: Policy[] = [
  {
    id: 'pol_001',
    name: 'Data Privacy Protection',
    description: 'Ensures all models comply with data privacy regulations',
    category: 'compliance',
    status: 'active',
    enforcementLevel: 'strict',
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-05T00:00:00Z',
    rules: [
      {
        id: 'rule_001',
        name: 'GDPR Compliance',
        condition: 'model.processes_personal_data == true',
        action: 'require_consent_mechanism',
        priority: 1
      },
      {
        id: 'rule_002',
        name: 'Data Retention',
        condition: 'data_retention_period > 30_days',
        action: 'require_justification',
        priority: 2
      }
    ]
  },
  {
    id: 'pol_002',
    name: 'Security Standards',
    description: 'Enforces security requirements for model deployment',
    category: 'security',
    status: 'active',
    enforcementLevel: 'strict',
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-03T00:00:00Z',
    rules: [
      {
        id: 'rule_003',
        name: 'Access Control',
        condition: 'model.access_level == "public"',
        action: 'require_authentication',
        priority: 1
      },
      {
        id: 'rule_004',
        name: 'Encryption',
        condition: 'model.encryption_level < "256_bit"',
        action: 'block_deployment',
        priority: 1
      }
    ]
  },
  {
    id: 'pol_003',
    name: 'Performance Monitoring',
    description: 'Monitors model performance and quality metrics',
    category: 'performance',
    status: 'active',
    enforcementLevel: 'moderate',
    createdAt: '2024-01-02T00:00:00Z',
    updatedAt: '2024-01-04T00:00:00Z',
    rules: [
      {
        id: 'rule_005',
        name: 'Accuracy Threshold',
        condition: 'model.accuracy < 0.85',
        action: 'require_retraining',
        priority: 2
      },
      {
        id: 'rule_006',
        name: 'Latency Check',
        condition: 'model.latency > 1000ms',
        action: 'optimization_required',
        priority: 3
      }
    ]
  },
  {
    id: 'pol_004',
    name: 'Ethical AI Guidelines',
    description: 'Ensures models follow ethical AI principles',
    category: 'ethical',
    status: 'active',
    enforcementLevel: 'moderate',
    createdAt: '2024-01-03T00:00:00Z',
    updatedAt: '2024-01-06T00:00:00Z',
    rules: [
      {
        id: 'rule_007',
        name: 'Bias Detection',
        condition: 'model.bias_score > 0.1',
        action: 'require_bias_mitigation',
        priority: 1
      },
      {
        id: 'rule_008',
        name: 'Transparency',
        condition: 'model.explainability_score < 0.7',
        action: 'require_documentation',
        priority: 2
      }
    ]
  }
];

// Export all mock data
export {
  simulateApiCall,
  simulateError,
  getRandomSuccess
}; 