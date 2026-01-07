// Mock data para Conformity Assessment Module (Art. 43 + Anexo VI)

export interface ConformityAssessment {
  id: number;
  projectId: number;
  projectName: string;
  assessmentDate: string;
  assessmentType: string;
  step1Score: number | null;
  step2QmsScore: number | null;
  step3DocScore: number | null;
  step4ConsistencyScore: number | null;
  overallScore: number | null;
  readyForCertification: boolean;
  annexViCompliant: boolean;
  certificateId: string | null;
  status: "PENDING" | "IN_PROGRESS" | "COMPLETED" | "REJECTED";
  createdBy: string;
  createdAt: string;
  updatedBy?: string;
  updatedAt?: string;
}

export interface ConformityAssessmentMetrics {
  total: number;
  completed: number;
  readyForCertification: number;
  averageScore: number;
  inProgress: number;
  pending: number;
}

export interface Step2QmsDetails {
  score: number;
  gaps: string[];
  modules: {
    name: string;
    score: number;
    compliant: boolean;
  }[];
}

export interface Step3DocDetails {
  score: number;
  missingSections: string[];
  sections: {
    name: string;
    completed: boolean;
    score: number;
  }[];
}

export interface Step4ConsistencyDetails {
  score: number;
  consistencyIssues: string[];
  qmsDocConsistency: number;
  docClassificationConsistency: number;
}

export const mockConformityAssessments: ConformityAssessment[] = [
  {
    id: 1,
    projectId: 1,
    projectName: "AI Credit Scoring System",
    assessmentDate: "2025-12-01T10:00:00Z",
    assessmentType: "FULL",
    step1Score: 1.0,
    step2QmsScore: 0.85,
    step3DocScore: 0.80,
    step4ConsistencyScore: 0.90,
    overallScore: 0.85,
    readyForCertification: true,
    annexViCompliant: true,
    certificateId: "CERT-2025-001",
    status: "COMPLETED",
    createdBy: "admin@example.com",
    createdAt: "2025-11-15T09:00:00Z",
    updatedBy: "admin@example.com",
    updatedAt: "2025-12-01T10:30:00Z",
  },
  {
    id: 2,
    projectId: 2,
    projectName: "Facial Recognition System",
    assessmentDate: "2025-12-02T14:30:00Z",
    assessmentType: "FULL",
    step1Score: 1.0,
    step2QmsScore: 0.70,
    step3DocScore: 0.65,
    step4ConsistencyScore: null,
    overallScore: null,
    readyForCertification: false,
    annexViCompliant: false,
    certificateId: null,
    status: "IN_PROGRESS",
    createdBy: "admin@example.com",
    createdAt: "2025-11-20T11:00:00Z",
    updatedBy: "admin@example.com",
    updatedAt: "2025-12-02T15:00:00Z",
  },
  {
    id: 3,
    projectId: 3,
    projectName: "Healthcare Diagnostics AI System",
    assessmentDate: "2025-12-03T09:15:00Z",
    assessmentType: "FULL",
    step1Score: 1.0,
    step2QmsScore: 0.92,
    step3DocScore: 0.88,
    step4ConsistencyScore: 0.95,
    overallScore: 0.92,
    readyForCertification: true,
    annexViCompliant: true,
    certificateId: "CERT-2025-002",
    status: "COMPLETED",
    createdBy: "admin@example.com",
    createdAt: "2025-11-10T08:00:00Z",
    updatedBy: "admin@example.com",
    updatedAt: "2025-12-03T09:45:00Z",
  },
  {
    id: 4,
    projectId: 4,
    projectName: "Recruitment AI System",
    assessmentDate: "2025-12-04T16:00:00Z",
    assessmentType: "FULL",
    step1Score: 1.0,
    step2QmsScore: 0.75,
    step3DocScore: null,
    step4ConsistencyScore: null,
    overallScore: null,
    readyForCertification: false,
    annexViCompliant: false,
    certificateId: null,
    status: "IN_PROGRESS",
    createdBy: "admin@example.com",
    createdAt: "2025-11-25T10:00:00Z",
    updatedBy: "admin@example.com",
    updatedAt: "2025-12-04T16:30:00Z",
  },
  {
    id: 5,
    projectId: 5,
    projectName: "Fraud Detection System",
    assessmentDate: "2025-12-05T11:00:00Z",
    assessmentType: "FULL",
    step1Score: null,
    step2QmsScore: null,
    step3DocScore: null,
    step4ConsistencyScore: null,
    overallScore: null,
    readyForCertification: false,
    annexViCompliant: false,
    certificateId: null,
    status: "PENDING",
    createdBy: "admin@example.com",
    createdAt: "2025-12-05T11:00:00Z",
  },
];

export const mockConformityMetrics: ConformityAssessmentMetrics = {
  total: 25,
  completed: 18,
  readyForCertification: 12,
  averageScore: 0.82,
  inProgress: 5,
  pending: 2,
};

export const mockStep2QmsDetails: Step2QmsDetails = {
  score: 0.85,
  gaps: [],
  modules: [
    { name: "Risk Management System", score: 0.90, compliant: true },
    { name: "Data Governance", score: 0.85, compliant: true },
    { name: "Technical Documentation", score: 0.80, compliant: true },
    { name: "Record-Keeping", score: 0.88, compliant: true },
    { name: "Transparency", score: 0.82, compliant: true },
    { name: "Human Oversight", score: 0.87, compliant: true },
    { name: "Accuracy & Robustness", score: 0.83, compliant: true },
    { name: "Cybersecurity", score: 0.86, compliant: true },
  ],
};

export const mockStep3DocDetails: Step3DocDetails = {
  score: 0.80,
  missingSections: ["Section 8: Cybersecurity Measures"],
  sections: [
    { name: "System Description", completed: true, score: 0.95 },
    { name: "Training Data", completed: true, score: 0.90 },
    { name: "Data Preparation", completed: true, score: 0.85 },
    { name: "Model Architecture", completed: true, score: 0.88 },
    { name: "Training Process", completed: true, score: 0.82 },
    { name: "Performance Metrics", completed: true, score: 0.87 },
    { name: "Risk Management", completed: true, score: 0.83 },
    { name: "Cybersecurity Measures", completed: false, score: 0.0 },
    { name: "Human Oversight", completed: true, score: 0.80 },
    { name: "Conformity Assessment", completed: true, score: 0.85 },
    { name: "Post-Market Monitoring", completed: true, score: 0.78 },
  ],
};

export const mockStep4ConsistencyDetails: Step4ConsistencyDetails = {
  score: 0.90,
  consistencyIssues: [],
  qmsDocConsistency: 0.92,
  docClassificationConsistency: 0.88,
};

export const mockConformityReview = {
  assessmentId: 1,
  project: {
    id: 1,
    name: "AI Credit Scoring System",
  },
  steps: {
    step2: {
      score: 0.85,
      gaps: [],
    },
    step3: {
      score: 0.80,
      missingSections: ["Section 8: Cybersecurity Measures"],
    },
    step4: {
      score: 0.90,
      consistencyIssues: [],
    },
  },
  overallScore: 0.85,
  readyForCertification: true,
};

export const mockConformityDeclarations = [
  {
    id: 1,
    assessmentId: 1,
    projectName: "AI Credit Scoring System",
    providerName: "ACME Corporation",
    generatedAt: "2025-12-01T15:00:00Z",
    articles: {
      art9: true, // Risk Management
      art10: true, // Data Governance
      art11: true, // Technical Documentation
      art12: true, // Record-Keeping
      art13: true, // Transparency
      art14: true, // Human Oversight
      art15: true, // Accuracy, Robustness, Cybersecurity
    },
    pdfUrl: "/api/compliance/declarations/1/pdf",
    status: "SIGNED",
  },
  {
    id: 2,
    assessmentId: 3,
    projectName: "Healthcare Diagnostics AI System",
    providerName: "MedTech Solutions",
    generatedAt: "2025-12-03T10:00:00Z",
    articles: {
      art9: true,
      art10: true,
      art11: true,
      art12: true,
      art13: true,
      art14: true,
      art15: true,
    },
    pdfUrl: "/api/compliance/declarations/2/pdf",
    status: "DRAFT",
  },
];

