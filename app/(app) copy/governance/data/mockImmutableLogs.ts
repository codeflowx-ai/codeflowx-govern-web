// Mock data para Immutable Logs Module (Art. 12, 19 EU AI Act)

export interface ImmutableLog {
  id: number;
  logType: string;
  entityType: string;
  entityId: number;
  entityName?: string;
  userId: string;
  timestamp: string;
  hash: string;
  previousHash: string;
  logData: Record<string, any>;
  integrityVerified?: boolean;
}

export interface IntegrityVerificationResult {
  startId: number;
  endId: number;
  totalLogs: number;
  verifiedLogs: number;
  integrityScore: number;
  status: "INTEGRITY_OK" | "INTEGRITY_BROKEN" | "INTEGRITY_PARTIAL";
  brokenChains?: Array<{
    logId: number;
    expectedHash: string;
    actualHash: string;
  }>;
}

export const mockImmutableLogs: ImmutableLog[] = [
  {
    id: 1,
    logType: "MODEL_DEPLOYMENT",
    entityType: "Model",
    entityId: 123,
    entityName: "Credit Scoring Model v1.0",
    userId: "user1@example.com",
    timestamp: "2025-12-01T10:30:00Z",
    hash: "a1b2c3d4e5f6789012345678901234567890abcdef1234567890abcdef123456",
    previousHash: "0",
    logData: {
      action: "deploy",
      modelId: 123,
      version: "1.0",
      environment: "production",
      deploymentConfig: {
        replicas: 3,
        resources: { cpu: "2", memory: "4Gi" }
      }
    },
    integrityVerified: true,
  },
  {
    id: 2,
    logType: "AGENT_EXECUTION",
    entityType: "Agent",
    entityId: 456,
    entityName: "Credit Scoring Agent",
    userId: "user2@example.com",
    timestamp: "2025-12-01T11:00:00Z",
    hash: "b2c3d4e5f6a789012345678901234567890abcdef1234567890abcdef1234567",
    previousHash: "a1b2c3d4e5f6789012345678901234567890abcdef1234567890abcdef123456",
    logData: {
      action: "execute",
      agentId: 456,
      input: { customerId: "CUST001", amount: 5000 },
      output: { approved: true, score: 0.85 },
      executionTime: 1250
    },
    integrityVerified: true,
  },
  {
    id: 3,
    logType: "PROMPT_CHANGE",
    entityType: "Prompt",
    entityId: 789,
    entityName: "Credit Assessment Prompt",
    userId: "admin@example.com",
    timestamp: "2025-12-01T12:15:00Z",
    hash: "c3d4e5f6a7b89012345678901234567890abcdef1234567890abcdef12345678",
    previousHash: "b2c3d4e5f6a789012345678901234567890abcdef1234567890abcdef1234567",
    logData: {
      action: "update",
      promptId: 789,
      previousVersion: "v1.2",
      newVersion: "v1.3",
      changes: ["Added bias mitigation", "Updated compliance checks"],
      approvedBy: "compliance@example.com"
    },
    integrityVerified: true,
  },
  {
    id: 4,
    logType: "COMPLIANCE_CHECK",
    entityType: "Project",
    entityId: 1001,
    entityName: "Credit Scoring Project",
    userId: "compliance@example.com",
    timestamp: "2025-12-01T13:00:00Z",
    hash: "d4e5f6a7b8c9012345678901234567890abcdef1234567890abcdef123456789",
    previousHash: "c3d4e5f6a7b89012345678901234567890abcdef1234567890abcdef12345678",
    logData: {
      action: "check",
      projectId: 1001,
      framework: "EU_AI_ACT",
      score: 0.92,
      checks: {
        dataGovernance: "PASS",
        modelTransparency: "PASS",
        humanOversight: "PASS",
        riskManagement: "PASS"
      }
    },
    integrityVerified: true,
  },
  {
    id: 5,
    logType: "MODEL_DEPLOYMENT",
    entityType: "Model",
    entityId: 124,
    entityName: "Fraud Detection Model v2.1",
    userId: "user1@example.com",
    timestamp: "2025-12-01T14:30:00Z",
    hash: "e5f6a7b8c9d012345678901234567890abcdef1234567890abcdef1234567890",
    previousHash: "0",
    logData: {
      action: "deploy",
      modelId: 124,
      version: "2.1",
      environment: "staging",
      deploymentConfig: {
        replicas: 2,
        resources: { cpu: "4", memory: "8Gi" }
      }
    },
    integrityVerified: true,
  },
  {
    id: 6,
    logType: "AGENT_EXECUTION",
    entityType: "Agent",
    entityId: 457,
    entityName: "Fraud Detection Agent",
    userId: "user3@example.com",
    timestamp: "2025-12-01T15:00:00Z",
    hash: "f6a7b8c9d0e12345678901234567890abcdef1234567890abcdef12345678901",
    previousHash: "e5f6a7b8c9d012345678901234567890abcdef1234567890abcdef1234567890",
    logData: {
      action: "execute",
      agentId: 457,
      input: { transactionId: "TXN001", amount: 10000, merchant: "MERCH001" },
      output: { fraudDetected: false, riskScore: 0.12 },
      executionTime: 890
    },
    integrityVerified: true,
  },
];

export const mockIntegrityVerification: IntegrityVerificationResult = {
  startId: 1,
  endId: 100,
  totalLogs: 100,
  verifiedLogs: 100,
  integrityScore: 1.00,
  status: "INTEGRITY_OK"
};

export const mockIntegrityVerificationBroken: IntegrityVerificationResult = {
  startId: 1,
  endId: 50,
  totalLogs: 50,
  verifiedLogs: 48,
  integrityScore: 0.96,
  status: "INTEGRITY_PARTIAL",
  brokenChains: [
    {
      logId: 25,
      expectedHash: "expected_hash_here",
      actualHash: "actual_hash_here"
    },
    {
      logId: 42,
      expectedHash: "expected_hash_here",
      actualHash: "actual_hash_here"
    }
  ]
};

// Tipos de log disponibles
export const LOG_TYPES = [
  "MODEL_DEPLOYMENT",
  "AGENT_EXECUTION",
  "PROMPT_CHANGE",
  "COMPLIANCE_CHECK",
  "RISK_ASSESSMENT",
  "DATA_CHANGE",
  "CONFIGURATION_CHANGE",
  "USER_ACTION"
];

// Tipos de entidad disponibles
export const ENTITY_TYPES = [
  "Model",
  "Agent",
  "Prompt",
  "Project",
  "Dataset",
  "Configuration"
];
