// Mock data para HITL Supervision según Art. 14 EU AI Act
// Human In The Loop - Supervisión humana de decisiones

export interface HitlIntervention {
  id: number;
  type: "AGENT_APPROVAL" | "MODEL_DEPLOYMENT" | "PROMPT_REVIEW" | "HUMAN_OVERRIDE";
  entityType: "Agent" | "Model" | "Prompt";
  entityId: number;
  entityName: string;
  status: "PENDING" | "IN_REVIEW" | "APPROVED" | "REJECTED" | "MODIFIED";
  createdAt: string;
  slaDeadline: string;
  slaHours: number;
  timeRemaining: number; // horas
  urgency?: "LOW" | "MEDIUM" | "HIGH" | "CRITICAL";
}

export interface HitlDecision {
  id: number;
  type: "AGENT_APPROVAL" | "MODEL_DEPLOYMENT" | "PROMPT_REVIEW" | "HUMAN_OVERRIDE";
  entityType: "Agent" | "Model" | "Prompt";
  entityId: number;
  entityName: string;
  decision: "APPROVED" | "REJECTED" | "MODIFIED";
  decisionReason: string;
  responseTime: number; // horas
  decisionDate: string;
  userId: string;
}

export interface HitlMetrics {
  averageResponseTime: number; // horas
  approvalRate: number; // 0-1
  slaCompliance: number; // 0-1
  pendingInterventions: number;
  totalInterventions: number;
  interventionsByType: {
    AGENT: number;
    MODEL: number;
    PROMPT: number;
  };
  interventionsByStatus: {
    PENDING: number;
    IN_REVIEW: number;
    APPROVED: number;
    REJECTED: number;
  };
}

export interface HitlSupervisionConfig {
  type: "PRE_DEPLOYMENT" | "IN_LOOP" | "POST_DEPLOYMENT" | "OVERRIDE";
  enabled: boolean;
  slaHours: number;
  requiredRoles: string[];
  autoEscalation: boolean;
  escalationHours: number;
}

export interface HitlData {
  metrics: HitlMetrics;
  pendingInterventions: HitlIntervention[];
  recentDecisions: HitlDecision[];
  supervisionConfig: HitlSupervisionConfig[];
}

export const mockHitlData: HitlData = {
  metrics: {
    averageResponseTime: 2.5, // horas
    approvalRate: 0.85,
    slaCompliance: 0.92,
    pendingInterventions: 12,
    totalInterventions: 150,
    interventionsByType: {
      AGENT: 45,
      MODEL: 62,
      PROMPT: 43,
    },
    interventionsByStatus: {
      PENDING: 12,
      IN_REVIEW: 8,
      APPROVED: 105,
      REJECTED: 25,
    },
  },
  pendingInterventions: [
    {
      id: 1,
      type: "AGENT_APPROVAL",
      entityType: "Agent",
      entityId: 123,
      entityName: "Credit Scoring Agent",
      status: "PENDING",
      createdAt: "2025-12-01T10:00:00Z",
      slaDeadline: "2025-12-01T14:00:00Z",
      slaHours: 4,
      timeRemaining: 2.5,
      urgency: "MEDIUM",
    },
    {
      id: 2,
      type: "PROMPT_REVIEW",
      entityType: "Prompt",
      entityId: 456,
      entityName: "Credit Scoring Prompt",
      status: "PENDING",
      createdAt: "2025-12-01T11:00:00Z",
      slaDeadline: "2025-12-01T15:00:00Z",
      slaHours: 4,
      timeRemaining: 3.5,
      urgency: "LOW",
    },
    {
      id: 3,
      type: "MODEL_DEPLOYMENT",
      entityType: "Model",
      entityId: 789,
      entityName: "Credit Scoring Model v2.0",
      status: "PENDING",
      createdAt: "2025-12-01T08:00:00Z",
      slaDeadline: "2025-12-01T16:00:00Z",
      slaHours: 8,
      timeRemaining: 5.0,
      urgency: "LOW",
    },
    {
      id: 4,
      type: "HUMAN_OVERRIDE",
      entityType: "Agent",
      entityId: 124,
      entityName: "Loan Approval Agent",
      status: "PENDING",
      createdAt: "2025-12-01T09:30:00Z",
      slaDeadline: "2025-12-01T13:30:00Z",
      slaHours: 4,
      timeRemaining: 1.0,
      urgency: "HIGH",
    },
    {
      id: 5,
      type: "AGENT_APPROVAL",
      entityType: "Agent",
      entityId: 125,
      entityName: "Customer Support Agent",
      status: "IN_REVIEW",
      createdAt: "2025-12-01T09:00:00Z",
      slaDeadline: "2025-12-01T13:00:00Z",
      slaHours: 4,
      timeRemaining: 2.0,
      urgency: "MEDIUM",
    },
  ],
  recentDecisions: [
    {
      id: 1,
      type: "MODEL_DEPLOYMENT",
      entityType: "Model",
      entityId: 789,
      entityName: "Credit Scoring Model v1.0",
      decision: "APPROVED",
      decisionReason: "Model meets all quality criteria and compliance requirements",
      responseTime: 1.5,
      decisionDate: "2025-11-30T15:00:00Z",
      userId: "user1@example.com",
    },
    {
      id: 2,
      type: "PROMPT_REVIEW",
      entityType: "Prompt",
      entityId: 455,
      entityName: "Loan Application Prompt",
      decision: "MODIFIED",
      decisionReason: "Prompt needed adjustments to comply with bias detection requirements",
      responseTime: 3.2,
      decisionDate: "2025-11-30T12:00:00Z",
      userId: "compliance@example.com",
    },
    {
      id: 3,
      type: "AGENT_APPROVAL",
      entityType: "Agent",
      entityId: 122,
      entityName: "Fraud Detection Agent",
      decision: "APPROVED",
      decisionReason: "Agent configuration approved after review",
      responseTime: 2.0,
      decisionDate: "2025-11-30T10:00:00Z",
      userId: "admin@example.com",
    },
    {
      id: 4,
      type: "MODEL_DEPLOYMENT",
      entityType: "Model",
      entityId: 788,
      entityName: "Risk Assessment Model",
      decision: "REJECTED",
      decisionReason: "Model failed bias testing and requires retraining",
      responseTime: 4.5,
      decisionDate: "2025-11-29T16:00:00Z",
      userId: "governance@example.com",
    },
  ],
  supervisionConfig: [
    {
      type: "PRE_DEPLOYMENT",
      enabled: true,
      slaHours: 8,
      requiredRoles: ["governance-admin", "compliance-officer"],
      autoEscalation: true,
      escalationHours: 12,
    },
    {
      type: "IN_LOOP",
      enabled: true,
      slaHours: 4,
      requiredRoles: ["supervisor", "compliance-officer"],
      autoEscalation: true,
      escalationHours: 6,
    },
    {
      type: "POST_DEPLOYMENT",
      enabled: true,
      slaHours: 24,
      requiredRoles: ["governance-admin"],
      autoEscalation: false,
      escalationHours: 48,
    },
    {
      type: "OVERRIDE",
      enabled: true,
      slaHours: 2,
      requiredRoles: ["supervisor", "admin"],
      autoEscalation: true,
      escalationHours: 4,
    },
  ],
};

export const mockHitlSlaReminder = {
  interventionId: 1,
  entityType: "Agent",
  entityId: 123,
  entityName: "Credit Scoring Agent",
  supervisionType: "AGENT_APPROVAL",
  slaHours: 4,
  createdAt: "2025-12-01T10:00:00Z",
  slaDeadline: "2025-12-01T14:00:00Z",
  timeRemaining: 1.5, // horas
  urgency: "HIGH" as const, // LOW, MEDIUM, HIGH, CRITICAL
};
