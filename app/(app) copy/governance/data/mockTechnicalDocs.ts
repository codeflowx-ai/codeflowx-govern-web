// Mock data para Technical Documentation Module (Art. 11 + Anexo IV)

export interface TechnicalDocSection {
  id: number;
  name: string;
  displayName: string;
  description: string;
  complete: boolean;
  score: number;
  content: string;
  lastUpdated?: string;
}

export interface TechnicalDocsData {
  modelId: number;
  modelName: string;
  overallScore: number;
  isComplete: boolean;
  sections: TechnicalDocSection[];
  pdfUrl: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface ModelDocumentationSummary {
  modelId: number;
  modelName: string;
  overallScore: number;
  isComplete: boolean;
  completedSections: number;
  totalSections: number;
  pdfUrl: string | null;
  lastUpdated: string;
}

export const mockTechnicalDocsSections: TechnicalDocSection[] = [
  {
    id: 1,
    name: "GENERAL_DESCRIPTION",
    displayName: "General Description",
    description: "General description of the AI system",
    complete: true,
    score: 0.95,
    content: "The AI system is a credit scoring model that evaluates loan applications using machine learning algorithms. The system processes financial data, credit history, and behavioral patterns to generate risk scores for loan approval decisions. The model is designed to comply with EU AI Act requirements and operates within a regulated financial environment.",
    lastUpdated: "2025-01-15T10:30:00Z",
  },
  {
    id: 2,
    name: "SYSTEM_ARCHITECTURE",
    displayName: "System Architecture",
    description: "System architecture and components",
    complete: true,
    score: 0.90,
    content: "The system architecture consists of a microservices-based design with the following components:\n- Data ingestion layer: Handles incoming loan application data\n- Feature engineering service: Processes and transforms raw data\n- Model inference service: Executes the credit scoring model\n- Decision engine: Applies business rules and generates final decisions\n- API gateway: Manages external and internal API communications\n\nThe system uses containerized deployment on Kubernetes with auto-scaling capabilities.",
    lastUpdated: "2025-01-15T10:30:00Z",
  },
  {
    id: 3,
    name: "DATA_GOVERNANCE",
    displayName: "Data Governance",
    description: "Data governance procedures",
    complete: false,
    score: 0.60,
    content: "Data governance procedures include:\n- Data quality checks and validation\n- Data retention policies\n- Access control mechanisms\n\n[Additional details pending completion]",
    lastUpdated: "2025-01-10T14:20:00Z",
  },
  {
    id: 4,
    name: "RISK_MANAGEMENT",
    displayName: "Risk Management",
    description: "Risk management measures",
    complete: true,
    score: 0.88,
    content: "Risk assessment is performed quarterly with bias testing, accuracy monitoring, and drift detection. The risk management framework includes:\n- Automated bias detection algorithms\n- Regular model performance audits\n- Incident response procedures\n- Risk mitigation strategies for identified vulnerabilities",
    lastUpdated: "2025-01-12T09:15:00Z",
  },
  {
    id: 5,
    name: "HUMAN_OVERSIGHT",
    displayName: "Human Oversight",
    description: "Human oversight measures (HITL)",
    complete: true,
    score: 0.82,
    content: "All decisions above €50,000 require human review. The human oversight framework includes:\n- Automated flagging of high-risk decisions\n- Review workflow for borderline cases\n- Escalation procedures for model uncertainty\n- Regular training for human reviewers",
    lastUpdated: "2025-01-14T16:45:00Z",
  },
  {
    id: 6,
    name: "ACCURACY_ROBUSTNESS",
    displayName: "Accuracy & Robustness",
    description: "Accuracy and robustness measures",
    complete: true,
    score: 0.90,
    content: "Model accuracy is maintained above 85% with continuous monitoring. Robustness measures include:\n- A/B testing for model updates\n- Adversarial testing procedures\n- Performance degradation alerts\n- Fallback mechanisms for system failures",
    lastUpdated: "2025-01-13T11:30:00Z",
  },
  {
    id: 7,
    name: "CYBERSECURITY",
    displayName: "Cybersecurity",
    description: "Cybersecurity measures",
    complete: true,
    score: 0.92,
    content: "System uses encryption at rest and in transit, with regular security audits. Security measures include:\n- TLS 1.3 for data in transit\n- AES-256 encryption for data at rest\n- Regular penetration testing\n- Security incident response plan\n- Access control and authentication mechanisms",
    lastUpdated: "2025-01-15T08:20:00Z",
  },
  {
    id: 8,
    name: "QUALITY_CONTROL",
    displayName: "Quality Control",
    description: "Quality control procedures",
    complete: true,
    score: 0.87,
    content: "Automated testing covers 80% of codebase with integration tests. Quality control includes:\n- Unit test coverage >80%\n- Integration testing for critical paths\n- Code review processes\n- Performance benchmarking\n- Continuous integration/continuous deployment (CI/CD) pipelines",
    lastUpdated: "2025-01-11T13:10:00Z",
  },
  {
    id: 9,
    name: "POST_MARKET_MONITORING",
    displayName: "Post-Market Monitoring",
    description: "Post-market monitoring plan",
    complete: true,
    score: 0.85,
    content: "Monitoring includes performance metrics, bias detection, and user feedback. The monitoring plan covers:\n- Real-time performance dashboards\n- Automated alerting for anomalies\n- Regular bias audits\n- User feedback collection and analysis\n- Incident tracking and resolution",
    lastUpdated: "2025-01-14T10:00:00Z",
  },
  {
    id: 10,
    name: "CONFORMITY_ASSESSMENT",
    displayName: "Conformity Assessment",
    description: "Conformity assessment results",
    complete: false,
    score: 0.60,
    content: "Pending external conformity assessment. Internal assessment completed with the following results:\n- Technical documentation: 82% complete\n- Risk management: Compliant\n- Data governance: Partially compliant\n\nExternal assessment scheduled for Q2 2025.",
    lastUpdated: "2025-01-08T15:30:00Z",
  },
  {
    id: 11,
    name: "RECORD_KEEPING",
    displayName: "Record-Keeping",
    description: "Record-keeping procedures",
    complete: true,
    score: 0.88,
    content: "All decisions and model versions are logged in immutable storage. Record-keeping procedures include:\n- Immutable audit logs for all decisions\n- Model version tracking\n- Data lineage documentation\n- Compliance record retention (7 years)\n- Automated backup and recovery procedures",
    lastUpdated: "2025-01-15T09:00:00Z",
  },
];

export const mockTechnicalDocs: TechnicalDocsData = {
  modelId: 1001,
  modelName: "Credit Scoring Model v1.2",
  overallScore: 0.82,
  isComplete: false,
  sections: mockTechnicalDocsSections,
  pdfUrl: null,
  createdAt: "2025-01-01T00:00:00Z",
  updatedAt: "2025-01-15T10:30:00Z",
};

export const mockModelsSummary: ModelDocumentationSummary[] = [
  {
    modelId: 1001,
    modelName: "Credit Scoring Model v1.2",
    overallScore: 0.82,
    isComplete: false,
    completedSections: 9,
    totalSections: 11,
    pdfUrl: null,
    lastUpdated: "2025-01-15T10:30:00Z",
  },
  {
    modelId: 1002,
    modelName: "Fraud Detection System v2.0",
    overallScore: 0.95,
    isComplete: true,
    completedSections: 11,
    totalSections: 11,
    pdfUrl: "/api/technical-docs/1002/pdf",
    lastUpdated: "2025-01-14T14:20:00Z",
  },
  {
    modelId: 1003,
    modelName: "Customer Churn Prediction v1.0",
    overallScore: 0.64,
    isComplete: false,
    completedSections: 7,
    totalSections: 11,
    pdfUrl: null,
    lastUpdated: "2025-01-12T09:15:00Z",
  },
  {
    modelId: 1004,
    modelName: "Recommendation Engine v3.1",
    overallScore: 0.73,
    isComplete: false,
    completedSections: 8,
    totalSections: 11,
    pdfUrl: null,
    lastUpdated: "2025-01-13T16:45:00Z",
  },
  {
    modelId: 1005,
    modelName: "Image Classification Model v1.5",
    overallScore: 0.91,
    isComplete: true,
    completedSections: 11,
    totalSections: 11,
    pdfUrl: "/api/technical-docs/1005/pdf",
    lastUpdated: "2025-01-15T08:00:00Z",
  },
];

export const mockCompleteDocumentation = {
  modelId: 1001,
  modelName: "Credit Scoring Model v1.2",
  currentScore: 0.82,
  incompleteSections: [
    {
      name: "DATA_GOVERNANCE",
      displayName: "Data Governance",
      currentContent: "Data governance procedures include:\n- Data quality checks and validation\n- Data retention policies\n- Access control mechanisms\n\n[Additional details pending completion]",
      required: true,
    },
    {
      name: "CONFORMITY_ASSESSMENT",
      displayName: "Conformity Assessment",
      currentContent: "Pending external conformity assessment. Internal assessment completed with the following results:\n- Technical documentation: 82% complete\n- Risk management: Compliant\n- Data governance: Partially compliant\n\nExternal assessment scheduled for Q2 2025.",
      required: true,
    },
  ],
  canComplete: false, // Requiere todas las secciones completas
};
