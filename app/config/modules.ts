// Configuración centralizada de módulos y roles
// Aplicación enfocada en Gobierno y Cumplimiento Normativo

export interface ModuleConfig {
  name: string;
  icon: any; // Componente de icono
  roles: string[];
  defaultPath: string;
  description?: string;
  category: "governance"; // Solo categoría governance
  order: number; // Orden dentro de la categoría
}

export interface MenuItem {
  name: string;
  href?: string;
  icon: any;
  roles: string[];
  children?: MenuItem[];
  isDefault?: boolean;
}

// Configuración centralizada de módulos - Separados por categorías
// ========== GOBIERNO Y CUMPLIMIENTO ==========
// Módulos orientados a gobierno, autorizaciones, testing, calidad y cumplimiento normativo
export const modulesConfig: ModuleConfig[] = [
  {
    name: "Projects",
    icon: "FolderKanban",
    roles: [
      "admin",
      "project_manager",
      "compliance_officer",
      "governance_manager",
      "auditor",
      "agencia",
      "it",
      "oem",
      "business_admin",
    ],
    defaultPath: "/projects/portfolio-dashboard",
    description: "Gestión de proyectos, recursos, costos y portfolios",
    category: "governance",
    order: 0,
  },
  {
    name: "Governance",
    icon: "Shield",
    roles: [
      "admin",
      "project_manager",
      "compliance_officer",
      "auditor",
      "governance_manager",
      "risk_manager",
      "security_officer",
    ],
    defaultPath: "/governance",
    description: "Gobierno y gestión de políticas de IA",
    category: "governance",
    order: 1,
  },
  {
    name: "Compliance",
    icon: "ClipboardCheck",
    roles: [
      "admin",
      "project_manager",
      "compliance_officer",
      "auditor",
      "governance_manager",
    ],
    defaultPath: "/governance/compliance",
    description: "Cumplimiento normativo EU AI Act y regulaciones",
    category: "governance",
    order: 2,
  },
  {
    name: "Agents",
    icon: "Bot",
    roles: [
      "admin",
      "project_manager",
      "compliance_officer",
      "governance_manager",
      "auditor",
    ],
    defaultPath: "/governance/agents/registry",
    description: "Gobierno y cumplimiento de agentes de IA (registry, monitoring, versioning, approval, compliance)",
    category: "governance",
    order: 3,
  },
  {
    name: "BPMN",
    icon: "Inbox",
    roles: [
      "admin",
      "project_manager",
      "compliance_officer",
      "auditor",
      "governance_manager",
      "risk_manager",
      "security_officer",
      "viewer",
    ],
    defaultPath: "/bpmn/task-inbox",
    description: "Bandeja de tareas BPMN y procesos de aprobación/autorización",
    category: "governance",
    order: 4,
  },
  {
    name: "Prompts",
    icon: "FileText",
    roles: [
      "admin",
      "project_manager",
      "compliance_officer",
      "governance_manager",
      "data_scientist",
      "developer",
    ],
    defaultPath: "/governance/prompts",
    description: "Gobernanza de prompts de IA (validación, versioning)",
    category: "governance",
    order: 5,
  },
  {
    name: "Data Governance",
    icon: "Database",
    roles: [
      "admin",
      "project_manager",
      "compliance_officer",
      "governance_manager",
      "data_scientist",
    ],
    defaultPath: "/governance/data/dashboard",
    description: "Gestión unificada de datasets y orígenes de datos - Formato estándar: Apache Parquet",
    category: "governance",
    order: 6,
  },
  {
    name: "RAG",
    icon: "Search",
    roles: [
      "admin",
      "project_manager",
      "compliance_officer",
      "governance_manager",
      "auditor",
      "data_scientist",
    ],
    defaultPath: "/governance/rag/compliance/dashboard",
    description: "Gobierno y cumplimiento de proyectos RAG",
    category: "governance",
    order: 7,
  },
  {
    name: "Testing",
    icon: "FlaskConical",
    roles: [
      "admin",
      "project_manager",
      "compliance_officer",
      "governance_manager",
      "auditor",
      "developer",
      "data_scientist",
      "ai_developer",
    ],
    defaultPath: "/governance/testing/execute",
    description: "Sistema de experimentos y runs estilo MLflow para testing y validación",
    category: "governance",
    order: 8,
  },
  {
    name: "Monitoring",
    icon: "Activity",
    roles: [
      "admin",
      "compliance_officer",
      "governance_manager",
      "auditor",
      "project_manager",
    ],
    defaultPath: "/monitoring/dashboard",
    description: "Monitorización y observabilidad operacional de sistemas, servicios e infraestructura",
    category: "governance",
    order: 9,
  },
  {
    name: "Models",
    icon: "Brain",
    roles: [
      "admin",
      "project_manager",
      "developer",
      "data_scientist",
      "ai_developer",
    ],
    defaultPath: "/models/registry",
    description: "Desarrollo y gestión técnica de modelos de IA",
    category: "governance",
    order: 10,
  },
];

// Función para obtener módulos por roles
export const getModulesByRoles = (roles: string[]): ModuleConfig[] => {
  return modulesConfig
    .filter((module) => module.roles.some((role) => roles.includes(role)))
    .sort((a, b) => {
      // Ordenar por order dentro de la categoría governance
      return a.order - b.order;
    });
};

// Función para obtener módulo por ruta
export const getModuleByPath = (pathname: string): string => {
  const path = pathname.split("/")[1];
  const subPath = pathname.split("/")[2];

  // Si la ruta contiene /compliance, es el módulo Compliance
  if (path === "governance" && subPath === "compliance") {
    return "Compliance";
  }

  // Si la ruta contiene /agents, es el módulo Agents
  if (path === "governance" && subPath === "agents") {
    return "Agents";
  }

  // Si la ruta contiene /prompts, es el módulo Prompts
  if (path === "governance" && subPath === "prompts") {
    return "Prompts";
  }

  // Si la ruta contiene /rag bajo governance, es el módulo RAG
  if (path === "governance" && subPath === "rag") {
    return "RAG";
  }

  // Si la ruta contiene /data bajo governance, es el módulo Data Governance
  if (path === "governance" && subPath === "data") {
    return "Data Governance";
  }

  // Si la ruta contiene /testing bajo governance, es el módulo Testing
  if (path === "governance" && subPath === "testing") {
    return "Testing";
  }

  // Si la ruta contiene /calculation-parameters bajo governance, es el módulo BPMN
  if (path === "governance" && subPath === "calculation-parameters") {
    return "BPMN";
  }

  // Si la ruta es bpmn/processes, es el módulo BPMN
  if (path.startsWith("bpmn/processes")) {
    return "BPMN";
  }

  // Si la ruta contiene /models, es el módulo Models
  if (path === "models") {
    return "Models";
  }

  // Si la ruta contiene /approval, /bias-analysis, /explainability, /performance bajo governance, es el módulo Models (legacy)
  if (path === "governance" && (subPath === "approval" || subPath === "bias-analysis" || subPath === "explainability" || subPath === "performance")) {
    return "Models";
  }

  const moduleMap: Record<string, string> = {
    dashboard: "Governance",
    governance: "Governance",
    bpmn: "BPMN",
    rag: "RAG",
    projects: "Projects",
    monitoring: "Monitoring",
    models: "Models",
  };

  return moduleMap[path] || "Governance";
};

// Función para validar si un usuario puede acceder a un módulo
export const canAccessModule = (
  userRoles: string[],
  moduleName: string
): boolean => {
  const module = modulesConfig.find((m) => m.name === moduleName);
  return module ? module.roles.some((role) => userRoles.includes(role)) : false;
};

// Función para obtener el menú de un módulo específico
export const getMenuByModule = (moduleName: string): MenuItem[] => {
  switch (moduleName) {
    case "Governance":
      return [
        {
          name: "Dashboard",
          href: `/governance`,
          icon: "Shield",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
          ],
          isDefault: true,
        },
        {
          name: "Policies Overview",
          href: `/governance/overview`,
          icon: "FileText",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
          ],
        },
        {
          name: "Security",
          href: `/governance/security`,
          icon: "Shield",
          roles: [
            "admin",
            "project_manager",
            "security_officer",
            "governance_manager",
          ],
        },
        {
          name: "Monitoring",
          href: `/governance/monitoring`,
          icon: "Activity",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
          ],
        },
        {
          name: "Telemetry",
          href: `/governance/telemetry/dashboard`,
          icon: "BarChart3",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
            "developer",
            "data_scientist",
          ],
        },
        {
          name: "ODS Impact",
          href: `/governance/ods-impact`,
          icon: "Globe",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
          ],
        },
        {
          name: "Risk Assessment",
          href: `/governance/risk-assessment`,
          icon: "AlertTriangle",
          roles: [
            "admin",
            "project_manager",
            "risk_manager",
            "governance_manager",
          ],
        },
        {
          name: "Auto Approval",
          href: `/governance/auto-approval`,
          icon: "CheckCircle",
          roles: [
            "admin",
            "project_manager",
            "governance_manager",
          ],
        },
      ];
    case "Agents":
      return [
        {
          name: "Dashboard",
          href: `/governance/agents/dashboard`,
          icon: "BarChart3",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
          ],
        },
        {
          name: "Registry",
          href: `/governance/agents/registry`,
          icon: "Package",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
          ],
          isDefault: true,
        },
        {
          name: "Approval",
          href: `/governance/agents/approval/overview`,
          icon: "CheckCircle",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
          ],
        },
        {
          name: "Governance",
          href: `/governance/agents/governance/overview`,
          icon: "Shield",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
          ],
        },
        {
          name: "Compliance",
          href: `/governance/agents/compliance/overview`,
          icon: "ShieldCheck",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
          ],
        },
        {
          name: "Ethics",
          href: `/governance/agents/ethics/overview`,
          icon: "Heart",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
          ],
        },
        {
          name: "Bias Detection",
          href: `/governance/agents/bias-detection/overview`,
          icon: "AlertTriangle",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
          ],
        },
        {
          name: "Transparency",
          href: `/governance/agents/transparency/overview`,
          icon: "Eye",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
          ],
        },
        {
          name: "Decisions",
          href: `/governance/agents/decisions/overview`,
          icon: "Gavel",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
          ],
        },
        {
          name: "Rollback",
          href: `/governance/agents/rollback/overview`,
          icon: "RotateCcw",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
          ],
        },
        {
          name: "Alerts",
          href: `/governance/agents/alerts/overview`,
          icon: "Bell",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
          ],
        },
        {
          name: "Interactions",
          href: `/governance/agents/interactions/overview`,
          icon: "MessageSquare",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
          ],
          children: [
            {
              name: "Collaboration",
              href: `/governance/agents/interactions/collaboration-overview`,
              icon: "Users",
              roles: [
                "admin",
                "project_manager",
                "compliance_officer",
                "governance_manager",
              ],
            },
            {
              name: "Communication",
              href: `/governance/agents/interactions/communication-overview`,
              icon: "Mail",
              roles: [
                "admin",
                "project_manager",
                "compliance_officer",
                "governance_manager",
              ],
            },
          ],
        },
      ];
    case "Compliance":
      return [
        {
          name: "Compliance Dashboard",
          href: `/governance/compliance/dashboard`,
          icon: "BarChart3",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
          ],
          isDefault: true,
        },
        {
          name: "Classification",
          href: `/governance/compliance/classification/projects`,
          icon: "Tag",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
            "viewer",
          ],
        },
        {
          name: "FRIA Projects",
          href: `/governance/compliance/fria/projects`,
          icon: "Shield",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
            "viewer",
          ],
        },
        {
          name: "EU Registration",
          href: `/governance/compliance/eu-registration`,
          icon: "Database",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
          ],
        },
        {
          name: "Post-Market Monitoring",
          href: `/governance/compliance/post-market-monitoring`,
          icon: "Activity",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "auditor",
          ],
        },
        {
          name: "Technical Docs",
          href: `/governance/compliance/technical-docs`,
          icon: "FileCode",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
          ],
        },
        {
          name: "HITL Supervision",
          href: `/governance/compliance/hitl-supervision`,
          icon: "UserCheck",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
          ],
        },
        {
          name: "Conformity Declaration",
          href: `/governance/compliance/conformity-declaration/projects`,
          icon: "FileSignature",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
          ],
        },
        {
          name: "QMS",
          href: `/governance/compliance/qms/projects`,
          icon: "ClipboardList",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
          ],
        },
        {
          name: "Traceability",
          href: `/governance/compliance/traceability`,
          icon: "Network",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
          ],
        },
        {
          name: "Prohibited Systems",
          href: `/governance/compliance/prohibited-systems`,
          icon: "Ban",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
          ],
        },
        {
          name: "Immutable Logs",
          href: `/governance/compliance/immutable-logs`,
          icon: "FileText",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "auditor",
          ],
        },
      ];
    case "BPMN":
      return [
        {
          name: "Task Inbox",
          href: `/bpmn/task-inbox`,
          icon: "Inbox",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
            "risk_manager",
            "security_officer",
            "developer",
            "viewer",
          ],
          isDefault: true,
        },
        {
          name: "BPMN Processes",
          href: `/bpmn/processes`,
          icon: "FileText",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
            "risk_manager",
            "security_officer",
            "developer",
          ],
        },
        {
          name: "Parámetros de Cálculo",
          href: `/governance/calculation-parameters`,
          icon: "Settings",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
            "developer",
            "architect",
          ],
        },


      ];
    case "Prompts":
      return [
        {
          name: "Prompts",
          href: `/governance/prompts`,
          icon: "FileText",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "developer",
            "data_scientist",
          ],
          isDefault: true,
        },
        {
          name: "Validation",
          href: `/governance/prompts/validation/overview`,
          icon: "CheckCircle",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "developer",
            "data_scientist",
          ],
        },
        {
          name: "Versioning",
          href: `/governance/prompts/versioning/overview`,
          icon: "GitBranch",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "developer",
            "data_scientist",
          ],
        },
        {
          name: "Performance",
          href: `/governance/prompts/templates/performance`,
          icon: "TrendingUp",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "developer",
            "data_scientist",
          ],
        },
      ];
    case "RAG":
      return [
        {
          name: "Compliance Dashboard",
          href: `/governance/rag/compliance/dashboard`,
          icon: "BarChart3",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
          ],
          isDefault: true,
        },
        {
          name: "Registry",
          href: `/governance/rag/registry`,
          icon: "Package",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
            "developer",
            "data_scientist",
          ],
        },
        {
          name: "Classification",
          href: `/governance/rag/classification`,
          icon: "Tag",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
          ],
        },
        {
          name: "FRIA",
          href: `/governance/rag/fria`,
          icon: "Shield",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
          ],
        },
      ];
    case "Testing":
      return [
        {
          name: "Execute Test",
          href: `/governance/testing/execute`,
          icon: "Play",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
            "developer",
            "data_scientist",
            "ai_developer",
          ],
          isDefault: true,
        },
        {
          name: "Experiments",
          href: `/governance/testing/experiments`,
          icon: "FlaskConical",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
            "developer",
            "data_scientist",
            "ai_developer",
          ],
        },
        {
          name: "Runs",
          href: `/governance/testing/runs`,
          icon: "Play",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
            "developer",
            "data_scientist",
            "ai_developer",
          ],
        },
        {
          name: "Compare Runs",
          href: `/governance/testing/compare`,
          icon: "GitCompare",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
            "developer",
            "data_scientist",
            "ai_developer",
          ],
        },
      ];
    case "Data Governance":
      return [
        {
          name: "Dashboard",
          href: `/governance/data/dashboard`,
          icon: "BarChart3",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "data_scientist",
            "developer",
          ],
          isDefault: true,
        },
        {
          name: "Integraciones",
          href: `/governance/data/integrations`,
          icon: "Plug",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "data_steward",
            "developer",
            "data_scientist",
            "it",
          ],
        },
        {
          name: "Data Origins",
          href: `/governance/data/origins`,
          icon: "Globe",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "data_scientist",
            "developer",
          ],
        },
        {
          name: "Datasets Overview",
          href: `/governance/data/datasets/overview`,
          icon: "Database",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "data_scientist",
            "developer",
          ],
        },
        {
          name: "Calidad",
          href: `/governance/data/quality`,
          icon: "BarChart3",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "data_scientist",
          ],
        },
        {
          name: "Riesgos",
          href: `/governance/data/risks`,
          icon: "AlertTriangle",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "risk_manager",
          ],
        },
        {
          name: "Privacidad",
          href: `/governance/data/privacy`,
          icon: "Shield",
          roles: [
            "admin",
            "compliance_officer",
            "governance_manager",
          ],
        },
        {
          name: "Línea de Base",
          href: `/governance/data/lineage`,
          icon: "GitBranch",
          roles: [
            "admin",
            "project_manager",
            "governance_manager",
            "data_scientist",
          ],
        },
        {
          name: "Documentación",
          href: `/governance/data/documentation`,
          icon: "FileText",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
          ],
        },
        {
          name: "Roles",
          href: `/governance/data/roles`,
          icon: "Users",
          roles: [
            "admin",
            "governance_manager",
          ],
        },
      ];
    case "Projects":
      return [
        // Dashboard Principal
        {
          name: "Portfolio Dashboard",
          href: `/projects/portfolio-dashboard`,
          icon: "BarChart3",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
            "agencia",
            "it",
            "oem",
            "business_admin",
          ],
          isDefault: true,
        },
        // Gestión de Proyectos
        {
          name: "Projects",
          href: `/projects/list`,
          icon: "FolderKanban",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
            "agencia",
            "it",
            "oem",
            "business_admin",
          ],
        },
        // Integraciones (Hub Central - NO duplica otros módulos)
        {
          name: "Integrations Dashboard",
          href: `/projects/integrations`,
          icon: "Plug",
          roles: [
            "admin",
            "project_manager",
            "governance_manager",
          ],
        },
        {
          name: "External Tools Sync",
          href: `/projects/external-sync`,
          icon: "RefreshCw",
          roles: [
            "admin",
            "project_manager",
          ],
        },
        // Recursos y Costos (Adaptados para IA - NO duplica otros módulos)
        {
          name: "Resource Allocation",
          href: `/projects/resource-allocation`,
          icon: "Cpu",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
          ],
        },
        {
          name: "Cost Breakdown",
          href: `/projects/cost-breakdown`,
          icon: "DollarSign",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
          ],
        },
        {
          name: "ROI Analysis",
          href: `/projects/roi-analysis`,
          icon: "TrendingUp",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
            "auditor",
          ],
        },
      ];
    case "Monitoring":
      return [
        {
          name: "Dashboard",
          href: `/monitoring/dashboard`,
          icon: "BarChart3",
          roles: [
            "admin",
            "compliance_officer",
            "governance_manager",
            "auditor",
            "project_manager",
          ],
          isDefault: true,
        },
        {
          name: "Alertas de Monitoreo",
          href: `/monitoring/alerts`,
          icon: "AlertTriangle",
          roles: [
            "admin",
            "compliance_officer",
            "governance_manager",
            "auditor",
            "project_manager",
          ],
        },
        {
          name: "Métricas",
          href: `/monitoring/metrics`,
          icon: "BarChart3",
          roles: [
            "admin",
            "compliance_officer",
            "governance_manager",
            "auditor",
            "project_manager",
          ],
        },
      ];
    case "Models":
      return [
        {
          name: "Registry",
          href: `/models/registry`,
          icon: "Package",
          roles: [
            "admin",
            "project_manager",
            "developer",
            "data_scientist",
            "ai_developer",
          ],
          isDefault: true,
        },
        {
          name: "Providers",
          href: `/models/providers`,
          icon: "Server",
          roles: [
            "admin",
            "project_manager",
            "developer",
            "data_scientist",
            "ai_developer",
          ],
        },
        {
          name: "Performance",
          href: `/models/performance`,
          icon: "BarChart3",
          roles: [
            "admin",
            "project_manager",
            "developer",
            "data_scientist",
            "ai_developer",
          ],
        },
        {
          name: "MLOps Integrations",
          href: `/models/integrations`,
          icon: "Cloud",
          roles: [
            "admin",
            "project_manager",
            "developer",
            "data_scientist",
            "ai_developer",
          ],
        },
        {
          name: "MLOps Config",
          href: `/models/integrations/config`,
          icon: "Wrench",
          roles: [
            "admin",
            "project_manager",
            "developer",
            "data_scientist",
            "ai_developer",
          ],
        },
      ];
    default:
      return [
        {
          name: "Dashboard",
          href: `/governance`,
          icon: "Shield",
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            "governance_manager",
          ],
          isDefault: true,
        },
      ];
  }
};
