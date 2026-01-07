"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  FolderKanban,
  Plus,
  Search,
  Filter,
  ArrowRight,
  Brain,
  Bot,
  Database,
  Shield,
  TrendingUp,
  Eye,
} from "lucide-react";
import Link from "next/link";

interface Project {
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

const mockProjects: Project[] = [
  {
    id: 1,
    name: "Modelo de Detección de Fraude Financiero",
    description: "Sistema de ML para detectar transacciones fraudulentas en tiempo real usando redes neuronales",
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
    description: "Agente conversacional basado en GPT-4 para soporte multicanal (chat, email, teléfono)",
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
    description: "Sistema de análisis de datos con modelos de forecasting para predecir demanda y tendencias",
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
    description: "Sistema de gobernanza y cumplimiento para modelos de alto riesgo según regulación europea",
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
    description: "Modelo de NLP para clasificación automática de documentos legales y administrativos",
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
    description: "Agente inteligente para automatizar procesos repetitivos en sistemas legacy",
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
    description: "Plataforma de gobernanza de datos con validación, calidad y trazabilidad de datasets",
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
    description: "Sistema de recomendación basado en deep learning para e-commerce y contenido",
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
    description: "Agente especializado en análisis de sentimientos en redes sociales y reviews",
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
    description: "Plataforma para tracking completo del ciclo de vida de modelos ML (versiones, despliegues, métricas)",
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

const getStatusColor = (status: string) => {
  switch (status) {
    case "active":
      return "primary";
    case "completed":
      return "secondary";
    case "on-hold":
      return "outline";
    case "cancelled":
      return "danger";
    default:
      return "outline";
  }
};

const getTypeIcon = (type: string) => {
  switch (type) {
    case "model":
      return Brain;
    case "agent":
      return Bot;
    case "data":
      return Database;
    case "compliance":
      return Shield;
    case "governance":
      return TrendingUp;
    default:
      return FolderKanban;
  }
};

const getTypeLabel = (type: string) => {
  switch (type) {
    case "model":
      return "Modelo";
    case "agent":
      return "Agente";
    case "data":
      return "Datos";
    case "compliance":
      return "Cumplimiento";
    case "governance":
      return "Gobierno";
    default:
      return type;
  }
};

export default function ProjectsOverviewPage() {
  const { t } = useTranslation();

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <FolderKanban className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("projects.overview.title", "Proyectos")}
            </h1>
          </div>
          <Button>
            <Plus className="w-4 h-4 mr-2" />
            {t("projects.overview.newProject", "Nuevo Proyecto")}
          </Button>
        </div>

        <div className="flex gap-4">
          <div className="flex-1 relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
            <input
              type="text"
              placeholder={t("projects.overview.search", "Buscar proyectos...")}
              className="w-full pl-10 pr-4 py-2 bg-background/60 backdrop-blur-md border border-border/50 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
            />
          </div>
          <Button variant="outline">
            <Filter className="w-4 h-4 mr-2" />
            {t("common.filter", "Filtrar")}
          </Button>
        </div>

        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardContent className="p-0">
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border/50">
                    <th className="px-6 py-4 text-left text-sm font-semibold text-foreground">
                      {t("projects.overview.name", "Nombre del Proyecto")}
                    </th>
                    <th className="px-6 py-4 text-left text-sm font-semibold text-foreground">
                      {t("projects.overview.type", "Tipo")}
                    </th>
                    <th className="px-6 py-4 text-left text-sm font-semibold text-foreground">
                      {t("projects.overview.status", "Estado")}
                    </th>
                    <th className="px-6 py-4 text-left text-sm font-semibold text-foreground">
                      {t("projects.overview.progress", "Progreso")}
                    </th>
                    <th className="px-6 py-4 text-center text-sm font-semibold text-foreground">
                      {t("common.actions", "Acciones")}
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {mockProjects.map((project) => {
                    const TypeIcon = getTypeIcon(project.type);
                    return (
                      <tr
                        key={project.id}
                        className="border-b border-border/30 hover:bg-background/40 transition-colors"
                      >
                        <td className="px-6 py-4">
                          <div className="flex flex-col">
                            <span className="font-medium text-foreground">{project.name}</span>
                            <span className="text-sm text-muted-foreground line-clamp-1 max-w-md">
                              {project.description}
                            </span>
                          </div>
                        </td>
                        <td className="px-6 py-4">
                          <div className="flex items-center gap-2">
                            <TypeIcon className="w-4 h-4 text-primary" />
                            <Badge variant="outline">{getTypeLabel(project.type)}</Badge>
                            {project.modelsCount && (
                              <span className="text-xs text-muted-foreground">
                                {project.modelsCount} {t("projects.overview.models", "modelos")}
                              </span>
                            )}
                            {project.agentsCount && (
                              <span className="text-xs text-muted-foreground">
                                {project.agentsCount} {t("projects.overview.agents", "agentes")}
                              </span>
                            )}
                          </div>
                        </td>
                        <td className="px-6 py-4">
                          <Badge variant={getStatusColor(project.status) as any}>
                            {t(`projects.status.${project.status}`, project.status)}
                          </Badge>
                        </td>
                        <td className="px-6 py-4">
                          <div className="flex items-center gap-2 min-w-[120px]">
                            <div className="flex-1 bg-muted rounded-full h-2">
                              <div
                                className="bg-primary h-2 rounded-full transition-all duration-500"
                                style={{ width: `${project.progress}%` }}
                              />
                            </div>
                            <span className="text-sm font-medium w-12 text-right">{project.progress}%</span>
                          </div>
                        </td>
                        <td className="px-6 py-4">
                          <div className="flex items-center justify-center gap-2">
                            <Link href={`/projects/${project.id}`}>
                              <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                                <Eye className="w-4 h-4" />
                              </Button>
                            </Link>
                          </div>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}


