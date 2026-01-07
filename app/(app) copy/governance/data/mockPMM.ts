// Mock data para Post-Market Monitoring (Art. 20, 72)

export interface PMMMetrics {
  systemsMonitored: number;
  activeIncidents: number;
  pendingActions: number;
  slaCompliance: number;
}

export interface PMMSystem {
  id: number;
  projectId: number;
  projectName: string;
  status: "HEALTHY" | "WARNING" | "CRITICAL";
  metrics: {
    accuracy: number;
    latency: number;
    throughput: number;
  };
  driftDetected: boolean;
  anomaliesDetected: boolean;
  lastCheck: string;
}

export interface Incident {
  id: number;
  projectId: number;
  projectName: string;
  severity: "LOW" | "MEDIUM" | "HIGH" | "CRITICAL";
  description: string;
  reportedAt: string;
  authorityNotified: boolean;
  authorityNotifiedAt: string | null;
  status: "OPEN" | "INVESTIGATING" | "RESOLVED" | "CLOSED";
  rootCauseAnalysis: string | null;
}

export interface CorrectiveAction {
  id: number;
  incidentId: number;
  incidentDescription: string;
  description: string;
  status: "PLANNED" | "IN_PROGRESS" | "COMPLETED";
  effectiveness: number | null;
  plannedDate: string;
  completedDate: string | null;
}

export const mockPMMData = {
  metrics: {
    systemsMonitored: 15,
    activeIncidents: 3,
    pendingActions: 5,
    slaCompliance: 0.92,
  } as PMMMetrics,
  systems: [
    {
      id: 1,
      projectId: 1,
      projectName: "AI Credit Scoring System",
      status: "HEALTHY" as const,
      metrics: {
        accuracy: 0.95,
        latency: 120,
        throughput: 1000,
      },
      driftDetected: false,
      anomaliesDetected: false,
      lastCheck: "2025-12-01T10:00:00Z",
    },
    {
      id: 2,
      projectId: 2,
      projectName: "Facial Recognition System",
      status: "WARNING" as const,
      metrics: {
        accuracy: 0.88,
        latency: 250,
        throughput: 800,
      },
      driftDetected: true,
      anomaliesDetected: true,
      lastCheck: "2025-12-01T10:00:00Z",
    },
    {
      id: 3,
      projectId: 3,
      projectName: "Healthcare Diagnostics AI System",
      status: "HEALTHY" as const,
      metrics: {
        accuracy: 0.92,
        latency: 150,
        throughput: 950,
      },
      driftDetected: false,
      anomaliesDetected: false,
      lastCheck: "2025-12-01T09:45:00Z",
    },
    {
      id: 4,
      projectId: 4,
      projectName: "Recruitment AI System",
      status: "WARNING" as const,
      metrics: {
        accuracy: 0.85,
        latency: 300,
        throughput: 600,
      },
      driftDetected: true,
      anomaliesDetected: false,
      lastCheck: "2025-12-01T09:30:00Z",
    },
  ] as PMMSystem[],
};

export const mockIncidents: Incident[] = [
  {
    id: 1,
    projectId: 1,
    projectName: "AI Credit Scoring System",
    severity: "HIGH",
    description: "Model accuracy dropped below threshold (0.85) for 3 consecutive days",
    reportedAt: "2025-12-01T10:00:00Z",
    authorityNotified: true,
    authorityNotifiedAt: "2025-12-01T10:05:00Z",
    status: "INVESTIGATING",
    rootCauseAnalysis: null,
  },
  {
    id: 2,
    projectId: 2,
    projectName: "Facial Recognition System",
    severity: "MEDIUM",
    description: "Increased false positive rate detected",
    reportedAt: "2025-12-02T14:30:00Z",
    authorityNotified: false,
    authorityNotifiedAt: null,
    status: "OPEN",
    rootCauseAnalysis: null,
  },
  {
    id: 3,
    projectId: 4,
    projectName: "Recruitment AI System",
    severity: "CRITICAL",
    description: "System detected bias in gender-based recommendations",
    reportedAt: "2025-12-03T08:15:00Z",
    authorityNotified: true,
    authorityNotifiedAt: "2025-12-03T08:20:00Z",
    status: "INVESTIGATING",
    rootCauseAnalysis: null,
  },
  {
    id: 4,
    projectId: 2,
    projectName: "Facial Recognition System",
    severity: "LOW",
    description: "Minor latency increase detected",
    reportedAt: "2025-12-04T11:00:00Z",
    authorityNotified: false,
    authorityNotifiedAt: null,
    status: "RESOLVED",
    rootCauseAnalysis: "Network congestion during peak hours",
  },
];

export const mockCorrectiveActions: CorrectiveAction[] = [
  {
    id: 1,
    incidentId: 1,
    incidentDescription: "Model accuracy dropped below threshold",
    description: "Retrain model with updated dataset and implement continuous monitoring",
    status: "IN_PROGRESS",
    effectiveness: null,
    plannedDate: "2025-12-15",
    completedDate: null,
  },
  {
    id: 2,
    incidentId: 2,
    incidentDescription: "Increased false positive rate",
    description: "Adjust classification threshold and add additional validation",
    status: "COMPLETED",
    effectiveness: 0.85,
    plannedDate: "2025-12-10",
    completedDate: "2025-12-10",
  },
  {
    id: 3,
    incidentId: 3,
    incidentDescription: "System detected bias in gender-based recommendations",
    description: "Review training data, remove biased features, and retrain model with balanced dataset",
    status: "PLANNED",
    effectiveness: null,
    plannedDate: "2025-12-20",
    completedDate: null,
  },
  {
    id: 4,
    incidentId: 4,
    incidentDescription: "Minor latency increase detected",
    description: "Optimize model inference and scale infrastructure",
    status: "COMPLETED",
    effectiveness: 0.92,
    plannedDate: "2025-12-05",
    completedDate: "2025-12-05",
  },
  {
    id: 5,
    incidentId: 1,
    incidentDescription: "Model accuracy dropped below threshold",
    description: "Implement automated retraining pipeline",
    status: "PLANNED",
    effectiveness: null,
    plannedDate: "2025-12-18",
    completedDate: null,
  },
];

// Proyectos mock sincronizados en todas las pantallas PMM
export const mockProjects = [
  { id: 1, name: "AI Credit Scoring System" },
  { id: 2, name: "Facial Recognition System" },
  { id: 3, name: "Healthcare Diagnostics AI System" },
  { id: 4, name: "Recruitment AI System" },
];
