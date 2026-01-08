// Mock data para QMS según Art. 17 EU AI Act
// 13 módulos QMS según especificación

export interface QmsModule {
  name: string;
  code: string;
  displayName: string;
  score: number;
  threshold: number;
  gaps: QmsGap[];
}

export interface QmsGap {
  module: string;
  description: string;
  severity: "LOW" | "MEDIUM" | "HIGH";
  currentScore?: number;
  targetScore?: number;
  gap?: number;
  recommendedActions?: string[];
}

export interface QmsData {
  projectId: number;
  projectName: string;
  overallScore: number;
  complianceStatus: "COMPLIANT" | "NON_COMPLIANT" | "PARTIAL";
  modules: QmsModule[];
  gaps: QmsGap[];
  improvementPlan: {
    priority: "HIGH" | "MEDIUM" | "LOW";
    action: string;
    deadline: string;
    module: string;
  }[];
  historicalScores?: Array<{
    date: string;
    score: number;
  }>;
}

// Los 13 módulos QMS según Art. 17
export const QMS_MODULES = [
  {
    name: "COMPLIANCE_STRATEGY",
    code: "CS",
    displayName: "Estrategia de Compliance",
  },
  {
    name: "RISK_MANAGEMENT",
    code: "RM",
    displayName: "Gestión de Riesgos",
  },
  {
    name: "DATA_GOVERNANCE",
    code: "DG",
    displayName: "Gestión de Datos",
  },
  {
    name: "DESIGN_DEVELOPMENT",
    code: "DD",
    displayName: "Diseño y Desarrollo",
  },
  {
    name: "VALIDATION_TESTING",
    code: "VT",
    displayName: "Validación y Testing",
  },
  {
    name: "TECHNICAL_DOCUMENTATION",
    code: "TD",
    displayName: "Documentación Técnica",
  },
  {
    name: "HUMAN_OVERSIGHT",
    code: "HO",
    displayName: "Supervisión Humana",
  },
  {
    name: "CORRECTIVE_ACTIONS",
    code: "CA",
    displayName: "Acciones Correctoras",
  },
  {
    name: "POST_MARKET_MONITORING",
    code: "PM",
    displayName: "Post-Market Monitoring",
  },
  {
    name: "CHANGE_MANAGEMENT",
    code: "CM",
    displayName: "Gestión de Cambios",
  },
  {
    name: "SUPPLIER_MANAGEMENT",
    code: "SM",
    displayName: "Gestión de Proveedores",
  },
  {
    name: "INTERNAL_AUDIT",
    code: "IA",
    displayName: "Auditoría Interna",
  },
  {
    name: "NON_CONFORMITY_MANAGEMENT",
    code: "NC",
    displayName: "Gestión de No Conformidades",
  },
];

export const mockQmsData: QmsData = {
  projectId: 1,
  projectName: "AI Credit Scoring System",
  overallScore: 0.82,
  complianceStatus: "PARTIAL",
  modules: [
    {
      name: "COMPLIANCE_STRATEGY",
      code: "CS",
      displayName: "Estrategia de Compliance",
      score: 0.90,
      threshold: 0.80,
      gaps: [],
    },
    {
      name: "RISK_MANAGEMENT",
      code: "RM",
      displayName: "Gestión de Riesgos",
      score: 0.85,
      threshold: 0.80,
      gaps: [],
    },
    {
      name: "DATA_GOVERNANCE",
      code: "DG",
      displayName: "Gestión de Datos",
      score: 0.75,
      threshold: 0.80,
      gaps: [
        {
          module: "DATA_GOVERNANCE",
          description: "Faltan métricas de calidad de datos",
          severity: "MEDIUM",
        },
      ],
    },
    {
      name: "DESIGN_DEVELOPMENT",
      code: "DD",
      displayName: "Diseño y Desarrollo",
      score: 0.88,
      threshold: 0.80,
      gaps: [],
    },
    {
      name: "VALIDATION_TESTING",
      code: "VT",
      displayName: "Validación y Testing",
      score: 0.78,
      threshold: 0.80,
      gaps: [
        {
          module: "VALIDATION_TESTING",
          description: "Cobertura de pruebas insuficiente (65%)",
          severity: "MEDIUM",
        },
      ],
    },
    {
      name: "TECHNICAL_DOCUMENTATION",
      code: "TD",
      displayName: "Documentación Técnica",
      score: 0.80,
      threshold: 0.80,
      gaps: [],
    },
    {
      name: "HUMAN_OVERSIGHT",
      code: "HO",
      displayName: "Supervisión Humana",
      score: 0.88,
      threshold: 0.80,
      gaps: [],
    },
    {
      name: "CORRECTIVE_ACTIONS",
      code: "CA",
      displayName: "Acciones Correctoras",
      score: 0.82,
      threshold: 0.80,
      gaps: [],
    },
    {
      name: "POST_MARKET_MONITORING",
      code: "PM",
      displayName: "Post-Market Monitoring",
      score: 0.85,
      threshold: 0.80,
      gaps: [],
    },
    {
      name: "CHANGE_MANAGEMENT",
      code: "CM",
      displayName: "Gestión de Cambios",
      score: 0.79,
      threshold: 0.80,
      gaps: [
        {
          module: "CHANGE_MANAGEMENT",
          description: "Proceso de control de cambios no documentado completamente",
          severity: "LOW",
        },
      ],
    },
    {
      name: "SUPPLIER_MANAGEMENT",
      code: "SM",
      displayName: "Gestión de Proveedores",
      score: 0.83,
      threshold: 0.80,
      gaps: [],
    },
    {
      name: "INTERNAL_AUDIT",
      code: "IA",
      displayName: "Auditoría Interna",
      score: 0.75,
      threshold: 0.80,
      gaps: [
        {
          module: "INTERNAL_AUDIT",
          description: "Auditoría externa pendiente",
          severity: "LOW",
        },
      ],
    },
    {
      name: "NON_CONFORMITY_MANAGEMENT",
      code: "NC",
      displayName: "Gestión de No Conformidades",
      score: 0.81,
      threshold: 0.80,
      gaps: [],
    },
  ],
  gaps: [
    {
      module: "DATA_GOVERNANCE",
      currentScore: 0.75,
      targetScore: 0.80,
      gap: 0.05,
      description: "Faltan métricas de calidad de datos",
      severity: "MEDIUM",
      recommendedActions: [
        "Implementar dashboard de métricas de calidad",
        "Definir KPIs de calidad de datos",
        "Establecer proceso de monitoreo continuo",
      ],
    },
    {
      module: "VALIDATION_TESTING",
      currentScore: 0.78,
      targetScore: 0.80,
      gap: 0.02,
      description: "Cobertura de pruebas insuficiente (65%)",
      severity: "MEDIUM",
      recommendedActions: [
        "Aumentar cobertura de pruebas a 85%",
        "Implementar pruebas automatizadas",
        "Establecer criterios de aceptación claros",
      ],
    },
    {
      module: "CHANGE_MANAGEMENT",
      currentScore: 0.79,
      targetScore: 0.80,
      gap: 0.01,
      description: "Proceso de control de cambios no documentado completamente",
      severity: "LOW",
      recommendedActions: [
        "Completar documentación del proceso de cambios",
        "Definir roles y responsabilidades",
      ],
    },
    {
      module: "INTERNAL_AUDIT",
      currentScore: 0.75,
      targetScore: 0.80,
      gap: 0.05,
      description: "Auditoría externa pendiente",
      severity: "LOW",
      recommendedActions: [
        "Programar auditoría externa",
        "Preparar documentación requerida",
      ],
    },
  ],
  improvementPlan: [
    {
      priority: "MEDIUM",
      action: "Implementar dashboard de métricas de calidad de datos",
      deadline: "2025-02-15",
      module: "DATA_GOVERNANCE",
    },
    {
      priority: "MEDIUM",
      action: "Aumentar cobertura de pruebas a 85%",
      deadline: "2025-02-20",
      module: "VALIDATION_TESTING",
    },
    {
      priority: "LOW",
      action: "Completar documentación del proceso de cambios",
      deadline: "2025-03-01",
      module: "CHANGE_MANAGEMENT",
    },
    {
      priority: "LOW",
      action: "Programar auditoría externa",
      deadline: "2025-03-15",
      module: "INTERNAL_AUDIT",
    },
  ],
  historicalScores: [
    { date: "2025-01-01", score: 0.78 },
    { date: "2025-01-15", score: 0.79 },
    { date: "2025-02-01", score: 0.80 },
    { date: "2025-02-15", score: 0.81 },
    { date: "2025-03-01", score: 0.82 },
  ],
};
