export interface ComplianceMetric {
  id: string;
  name: string;
  category: string;
  currentValue: number;
  targetValue: number;
  status: "compliant" | "non-compliant" | "at-risk";
  lastUpdated: string;
  trend: "up" | "down" | "stable";
}

export interface RiskIndicator {
  id: string;
  name: string;
  riskLevel: "low" | "medium" | "high" | "critical";
  probability: number;
  impact: number;
  mitigationStatus: "none" | "planned" | "in-progress" | "completed";
  lastAssessment: string;
  nextAssessment: string;
}

export interface PolicyViolation {
  id: string;
  policy: string;
  severity: "low" | "medium" | "high" | "critical";
  description: string;
  detectedAt: string;
  status: "open" | "investigating" | "resolved";
  assignedTo: string;
  affectedResources: string[];
}

export const mockComplianceMetrics: ComplianceMetric[] = [
  {
    id: "1",
    name: "Cumplimiento de Políticas de Seguridad",
    category: "Seguridad",
    currentValue: 87,
    targetValue: 95,
    status: "at-risk",
    lastUpdated: "2 horas atrás",
    trend: "up",
  },
  {
    id: "2",
    name: "Cumplimiento de GDPR",
    category: "Privacidad",
    currentValue: 92,
    targetValue: 90,
    status: "compliant",
    lastUpdated: "1 día atrás",
    trend: "stable",
  },
  {
    id: "3",
    name: "Cumplimiento de SOX",
    category: "Financiero",
    currentValue: 78,
    targetValue: 85,
    status: "non-compliant",
    lastUpdated: "3 días atrás",
    trend: "down",
  },
  {
    id: "4",
    name: "Cumplimiento de ISO 27001",
    category: "Seguridad de la Información",
    currentValue: 95,
    targetValue: 90,
    status: "compliant",
    lastUpdated: "1 semana atrás",
    trend: "up",
  },
];

export const mockRiskIndicators: RiskIndicator[] = [
  {
    id: "1",
    name: "Riesgo de Brecha de Datos",
    riskLevel: "high",
    probability: 75,
    impact: 90,
    mitigationStatus: "in-progress",
    lastAssessment: "2024-01-15",
    nextAssessment: "2024-04-15",
  },
  {
    id: "2",
    name: "Riesgo de Cumplimiento Regulatorio",
    riskLevel: "medium",
    probability: 60,
    impact: 80,
    mitigationStatus: "planned",
    lastAssessment: "2024-01-10",
    nextAssessment: "2024-03-10",
  },
  {
    id: "3",
    name: "Riesgo de Continuidad del Negocio",
    riskLevel: "low",
    probability: 30,
    impact: 70,
    mitigationStatus: "completed",
    lastAssessment: "2024-01-05",
    nextAssessment: "2024-07-05",
  },
];

export const mockPolicyViolations: PolicyViolation[] = [
  {
    id: "1",
    policy: "Política de Acceso a Datos",
    severity: "high",
    description: "Acceso no autorizado a datos sensibles del cliente",
    detectedAt: "2 horas atrás",
    status: "investigating",
    assignedTo: "Equipo de Seguridad",
    affectedResources: ["Customer Database", "User Service"],
  },
  {
    id: "2",
    policy: "Política de Cifrado",
    severity: "medium",
    description: "Datos en tránsito sin cifrar detectados",
    detectedAt: "1 día atrás",
    status: "open",
    assignedTo: "DevOps Team",
    affectedResources: ["API Gateway", "Payment Service"],
  },
  {
    id: "3",
    policy: "Política de Auditoría",
    severity: "low",
    description: "Logs de auditoría incompletos",
    detectedAt: "3 días atrás",
    status: "resolved",
    assignedTo: "IT Operations",
    affectedResources: ["Audit System", "Log Aggregator"],
  },
];
