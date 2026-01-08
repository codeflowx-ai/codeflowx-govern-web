"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Progress } from "@/components/ui/progress";
import {
  Activity,
  AlertCircle,
  ArrowDownRight,
  ArrowUpRight,
  CheckCircle,
  Clock,
  Database,
  FileText,
  Settings,
  TrendingUp,
  XCircle,
  Zap,
} from "lucide-react";
import { useState } from "react";

// Mock data basado en la documentación
const mockRAGStats = {
  totalProjects: 4,
  activeProjects: 4,
  totalChunks: 8,
  totalEmbeddings: 8,
  totalSearches: 156,
  avgResponseTime: 1.2,
  successRate: 94.5,
  totalModels: 5,
  activeModels: 5,
};

const mockProjects = [
  {
    id: 1,
    name: "DemoRAGProject",
    description: "Proyecto RAG de demostración para testing",
    status: "active",
    chunks: 2,
    embeddings: 2,
    searches: 45,
    lastActivity: "2024-01-15T10:30:00Z",
    models: {
      embedding: "text-embedding-ada-002",
      reranker: "cohere-rerank-multilingual-v2.0",
      llm: "gpt-4-turbo",
    },
  },
  {
    id: 2,
    name: "Ecommerce Knowledge Base",
    description: "Base de conocimiento para chatbot de ecommerce",
    status: "active",
    chunks: 2,
    embeddings: 2,
    searches: 67,
    lastActivity: "2024-01-15T09:15:00Z",
    models: {
      embedding: "text-embedding-ada-002",
      reranker: "cohere-rerank-multilingual-v2.0",
      llm: "llama-2-70b-chat",
    },
  },
  {
    id: 3,
    name: "Legal Document Assistant",
    description: "Asistente legal para análisis de documentos jurídicos",
    status: "active",
    chunks: 2,
    embeddings: 2,
    searches: 23,
    lastActivity: "2024-01-15T08:45:00Z",
    models: {
      embedding: "text-embedding-ada-002",
      reranker: "cohere-rerank-multilingual-v2.0",
      llm: "claude-3-opus",
    },
  },
  {
    id: 4,
    name: "Technical Support RAG",
    description: "Sistema RAG para soporte técnico",
    status: "active",
    chunks: 2,
    embeddings: 2,
    searches: 21,
    lastActivity: "2024-01-15T07:20:00Z",
    models: {
      embedding: "text-embedding-ada-002",
      reranker: "cohere-rerank-multilingual-v2.0",
      llm: "gpt-4-turbo",
    },
  },
];

const mockRecentActivity = [
  {
    id: 1,
    type: "search",
    project: "Ecommerce Knowledge Base",
    description: 'Búsqueda: "política de devoluciones"',
    timestamp: "2024-01-15T10:30:00Z",
    status: "success",
  },
  {
    id: 2,
    type: "chunk",
    project: "Legal Document Assistant",
    description: "Nuevo chunk procesado: Artículo 15",
    timestamp: "2024-01-15T10:15:00Z",
    status: "success",
  },
  {
    id: 3,
    type: "embedding",
    project: "Technical Support RAG",
    description: "Embedding generado para troubleshooting",
    timestamp: "2024-01-15T10:00:00Z",
    status: "success",
  },
  {
    id: 4,
    type: "search",
    project: "DemoRAGProject",
    description: 'Búsqueda: "ejemplos de implementación"',
    timestamp: "2024-01-15T09:45:00Z",
    status: "success",
  },
];

const mockModelStats = [
  {
    name: "text-embedding-ada-002",
    type: "EMBEDDING",
    usage: 100,
    cost: 0.0001,
  },
  {
    name: "cohere-rerank-multilingual-v2.0",
    type: "RERANKER",
    usage: 85,
    cost: 0.001,
  },
  { name: "gpt-4-turbo", type: "LLM", usage: 60, cost: 0.01 },
  { name: "llama-2-70b-chat", type: "LLM", usage: 40, cost: 0.0 },
  { name: "claude-3-opus", type: "LLM", usage: 25, cost: 0.015 },
];

export default function RAGDashboard() {
  const { t } = useTranslation();
  const [stats, setStats] = useState(mockRAGStats);
  const [projects, setProjects] = useState(mockProjects);
  const [recentActivity, setRecentActivity] = useState(mockRecentActivity);
  const [modelStats, setModelStats] = useState(mockModelStats);

  const getStatusIcon = (status: string) => {
    switch (status) {
      case "success":
        return <CheckCircle className="h-4 w-4 text-green-500" />;
      case "warning":
        return <AlertCircle className="h-4 w-4 text-yellow-500" />;
      case "error":
        return <XCircle className="h-4 w-4 text-red-500" />;
      default:
        return <Clock className="h-4 w-4 text-blue-500" />;
    }
  };

  const getModelTypeColor = (type: string) => {
    switch (type) {
      case "EMBEDDING":
        return "bg-blue-100 text-blue-800";
      case "RERANKER":
        return "bg-purple-100 text-purple-800";
      case "LLM":
        return "bg-green-100 text-green-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };

  const formatTimeAgo = (timestamp: string) => {
    const now = new Date();
    const time = new Date(timestamp);
    const diffInMinutes = Math.floor(
      (now.getTime() - time.getTime()) / (1000 * 60)
    );

    if (diffInMinutes < 1) return "Ahora";
    if (diffInMinutes < 60) return `${diffInMinutes}m`;
    if (diffInMinutes < 1440) return `${Math.floor(diffInMinutes / 60)}h`;
    return `${Math.floor(diffInMinutes / 1440)}d`;
  };

  return (
    <div className="space-y-4">
      {/* Header - Título alineado a la izquierda */}
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <Settings className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("rag.dashboard.title", "RAG Dashboard")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("rag.dashboard.subtitle", "Gestión de sistemas RAG")}
          </p>
        </div>
        <Badge
          variant="outline"
          className="bg-green-100 text-green-800 dark:bg-green-500/20 dark:text-green-300 dark:border-green-400/30"
        >
          <Activity className="h-3 w-3 mr-1" />
          {stats.activeProjects}{" "}
          {t("rag.dashboard.activeProjects", "activos")}
        </Badge>
      </div>

      {/* KPIs Principales */}
      <div className="grid grid-cols-4 gap-4">
        {/* Total Proyectos */}
        <Card className="border-primary hover:shadow-lg hover:border-primary/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("rag.dashboard.totalProjects", "Total Proyectos")}
                </p>
                <h2 className="text-2xl font-bold text-primary mb-1">
                  {stats.totalProjects}
                </h2>
              </div>
              <Database className="w-8 h-8 text-primary flex-shrink-0" />
            </div>
          </CardBody>
        </Card>

        {/* Total Chunks */}
        <Card className="border-purple-500 hover:shadow-lg hover:border-purple-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("rag.dashboard.totalChunks", "Total Chunks")}
                </p>
                <h2 className="text-2xl font-bold text-purple-600 mb-1">
                  {stats.totalChunks}
                </h2>
              </div>
              <FileText className="w-8 h-8 text-purple-600 flex-shrink-0" />
            </div>
          </CardBody>
        </Card>

        {/* Total Búsquedas */}
        <Card className="border-green-500 hover:shadow-lg hover:border-green-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("rag.dashboard.totalSearches", "Total Búsquedas")}
                </p>
                <h2 className="text-2xl font-bold text-green-600 mb-1">
                  {stats.totalSearches}
                </h2>
              </div>
              <TrendingUp className="w-8 h-8 text-green-600 flex-shrink-0" />
            </div>
          </CardBody>
        </Card>

        {/* Tiempo Respuesta */}
        <Card className="border-yellow-500 hover:shadow-lg hover:border-yellow-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("rag.dashboard.avgResponseTime", "Tiempo Respuesta")}
                </p>
                <h2 className="text-2xl font-bold text-yellow-600 mb-1">
                  {stats.avgResponseTime}s
                </h2>
              </div>
              <Zap className="w-8 h-8 text-yellow-600 flex-shrink-0" />
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Main Content Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-4">
        {/* Projects Overview */}
        <div className="lg:col-span-2">
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
            <CardHeader>
              <div className="flex items-center justify-between">
                <CardTitle className="text-base font-medium">
                  {t("rag.dashboard.projectsOverview", "Vista General de Proyectos")}
                </CardTitle>
                <Button variant="outline" size="sm">
                  {t("rag.dashboard.viewAll", "Ver Todos")}
                </Button>
              </div>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                {projects.map((project) => (
                  <div
                    key={project.id}
                    className="p-4 bg-muted/30 rounded-lg border border-border hover:bg-muted/50 transition-all duration-300 hover:border-border/80"
                  >
                    <div className="flex items-center justify-between">
                      <div className="flex-1">
                        <div className="flex items-center space-x-3">
                          <h4 className="font-medium text-foreground">
                            {project.name}
                          </h4>
                          <Badge
                            variant="outline"
                            className="bg-green-100 text-green-800 dark:bg-green-500/20 dark:text-green-300 dark:border-green-400/30"
                          >
                            {project.status}
                          </Badge>
                        </div>
                        <p className="text-sm text-muted-foreground mt-1">
                          {project.description}
                        </p>
                        <div className="flex items-center space-x-4 mt-2 text-xs text-muted-foreground">
                          <span>
                            {project.chunks}{" "}
                            {t("rag.dashboard.chunks", "chunks")}
                          </span>
                          <span>
                            {project.embeddings}{" "}
                            {t("rag.dashboard.embeddings", "embeddings")}
                          </span>
                          <span>
                            {project.searches}{" "}
                            {t("rag.dashboard.searches", "búsquedas")}
                          </span>
                          <span>{formatTimeAgo(project.lastActivity)}</span>
                        </div>
                      </div>
                      <div className="text-right">
                        <div className="text-xs text-muted-foreground mb-1">
                          {t("rag.dashboard.models", "Modelos")}
                        </div>
                        <div className="space-y-1">
                          <Badge
                            variant="outline"
                            className="text-xs bg-blue-100 text-blue-800 dark:bg-blue-500/20 dark:text-blue-300 dark:border-blue-400/30"
                          >
                            {project.models.embedding}
                          </Badge>
                          <Badge
                            variant="outline"
                            className="text-xs bg-purple-100 text-purple-800 dark:bg-purple-500/20 dark:text-purple-300 dark:border-purple-400/30"
                          >
                            {project.models.reranker}
                          </Badge>
                          <Badge
                            variant="outline"
                            className="text-xs bg-green-100 text-green-800 dark:bg-green-500/20 dark:text-green-300 dark:border-green-400/30"
                          >
                            {project.models.llm}
                          </Badge>
                        </div>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </CardBody>
          </Card>
        </div>

        {/* Recent Activity & Model Stats */}
        <div className="space-y-4">
          {/* Recent Activity */}
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
            <CardHeader>
              <CardTitle className="text-base font-medium">
                {t("rag.dashboard.recentActivity", "Actividad Reciente")}
              </CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-3">
                {recentActivity.map((activity) => (
                  <div key={activity.id} className="flex items-start space-x-3">
                    {getStatusIcon(activity.status)}
                    <div className="flex-1 min-w-0">
                      <p className="text-sm font-medium text-foreground truncate">
                        {activity.project}
                      </p>
                      <p className="text-xs text-muted-foreground truncate">
                        {activity.description}
                      </p>
                      <p className="text-xs text-muted-foreground">
                        {formatTimeAgo(activity.timestamp)}
                      </p>
                    </div>
                  </div>
                ))}
              </div>
            </CardBody>
          </Card>

          {/* Model Usage Stats */}
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
            <CardHeader>
              <CardTitle className="text-base font-medium">
                {t("rag.dashboard.modelUsage", "Uso de Modelos")}
              </CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                {modelStats.map((model, index) => (
                  <div key={index} className="space-y-2">
                    <div className="flex items-center justify-between">
                      <div className="flex items-center space-x-2">
                        <Badge
                          className={`text-xs ${getModelTypeColor(model.type)}`}
                        >
                          {model.type}
                        </Badge>
                        <span className="text-sm font-medium text-foreground truncate">
                          {model.name}
                        </span>
                      </div>
                      <span className="text-sm text-muted-foreground">
                        {model.usage}%
                      </span>
                    </div>
                    <Progress value={model.usage} className="h-2" />
                    <div className="flex justify-between text-xs text-muted-foreground">
                      <span>{t("rag.dashboard.usage", "Uso")}</span>
                      <span>${model.cost}/token</span>
                    </div>
                  </div>
                ))}
              </div>
            </CardBody>
          </Card>
        </div>
      </div>
    </div>
  );
}


