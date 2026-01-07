export interface Project {
  id: number;
  name: string;
  description: string;
  type: "model" | "agent" | "data" | "compliance" | "governance";
  status: "active" | "completed" | "on-hold" | "cancelled";
  startDate: string;
  endDate: string;
  budget: number;
  spent: number;
  teamSize: number;
  progress: number;
  modelsCount?: number;
  agentsCount?: number;
}

export const mockProjects: Project[] = [
  {
    id: 1,
    name: "Modelo de Detección de Fraude Financiero",
    description:
      "Sistema de ML para detectar transacciones fraudulentas en tiempo real usando redes neuronales",
    type: "model",
    status: "active",
    startDate: "2024-01-15",
    endDate: "2024-12-31",
    budget: 850000,
    spent: 520000,
    teamSize: 15,
    progress: 68,
    modelsCount: 3,
  },
  {
    id: 2,
    name: "Agente Virtual de Atención al Cliente",
    description:
      "Agente conversacional basado en GPT-4 para soporte multicanal (chat, email, teléfono)",
    type: "agent",
    status: "active",
    startDate: "2024-03-01",
    endDate: "2025-02-28",
    budget: 650000,
    spent: 285000,
    teamSize: 12,
    progress: 44,
    agentsCount: 2,
  },
  {
    id: 3,
    name: "Plataforma de Análisis Predictivo de Datos",
    description:
      "Sistema de análisis de datos con modelos de forecasting para predecir demanda y tendencias",
    type: "data",
    status: "active",
    startDate: "2024-02-10",
    endDate: "2024-11-30",
    budget: 720000,
    spent: 480000,
    teamSize: 18,
    progress: 67,
    modelsCount: 5,
  },
  {
    id: 4,
    name: "Cumplimiento Normativo EU AI Act",
    description:
      "Sistema de gobernanza y cumplimiento para modelos de alto riesgo según regulación europea",
    type: "compliance",
    status: "active",
    startDate: "2024-04-01",
    endDate: "2025-03-31",
    budget: 950000,
    spent: 380000,
    teamSize: 20,
    progress: 40,
  },
  {
    id: 5,
    name: "Modelo de Clasificación de Documentos",
    description:
      "Modelo de NLP para clasificación automática de documentos legales y administrativos",
    type: "model",
    status: "completed",
    startDate: "2023-09-01",
    endDate: "2024-08-31",
    budget: 450000,
    spent: 435000,
    teamSize: 10,
    progress: 100,
    modelsCount: 2,
  },
  {
    id: 6,
    name: "Agente de Automatización de Procesos RPA",
    description:
      "Agente inteligente para automatizar procesos repetitivos en sistemas legacy",
    type: "agent",
    status: "active",
    startDate: "2024-05-15",
    endDate: "2025-04-30",
    budget: 580000,
    spent: 195000,
    teamSize: 14,
    progress: 34,
    agentsCount: 4,
  },
  {
    id: 7,
    name: "Gobierno de Datos y Calidad",
    description:
      "Plataforma de gobernanza de datos con validación, calidad y trazabilidad de datasets",
    type: "governance",
    status: "active",
    startDate: "2024-01-20",
    endDate: "2024-12-20",
    budget: 680000,
    spent: 510000,
    teamSize: 16,
    progress: 75,
  },
  {
    id: 8,
    name: "Modelo de Recomendación Personalizada",
    description:
      "Sistema de recomendación basado en deep learning para e-commerce y contenido",
    type: "model",
    status: "on-hold",
    startDate: "2024-06-01",
    endDate: "2025-05-31",
    budget: 520000,
    spent: 125000,
    teamSize: 11,
    progress: 24,
    modelsCount: 1,
  },
  {
    id: 9,
    name: "Agente de Análisis de Sentimientos",
    description:
      "Agente especializado en análisis de sentimientos en redes sociales y reviews",
    type: "agent",
    status: "active",
    startDate: "2024-03-15",
    endDate: "2024-12-15",
    budget: 420000,
    spent: 315000,
    teamSize: 9,
    progress: 75,
    agentsCount: 1,
  },
  {
    id: 10,
    name: "Sistema de Trazabilidad de Modelos",
    description:
      "Plataforma para tracking completo del ciclo de vida de modelos ML (versiones, despliegues, métricas)",
    type: "governance",
    status: "completed",
    startDate: "2023-11-01",
    endDate: "2024-10-31",
    budget: 550000,
    spent: 540000,
    teamSize: 13,
    progress: 100,
  },
];
