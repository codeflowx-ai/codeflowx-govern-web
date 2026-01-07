"use client";

import { useTranslation } from "@/app/config/i18n";
import { SimpleModal } from "@/components/ui/SimpleModal";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import HelpTooltip from "@/components/ui/help-tooltip";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  AlertCircle,
  Brain,
  Calendar,
  CheckCircle,
  Edit,
  Eye,
  Filter,
  Pause,
  Play,
  Plus,
  Search,
  Settings,
  User,
  XCircle,
} from "lucide-react";
import { useState } from "react";

// Mock data basado en la documentación
const mockProjects = [
  {
    id: 1,
    name: "DemoRAGProject",
    description: "Proyecto RAG de demostración para testing",
    status: "active",
    owner: "Admin User",
    createdAt: "2024-01-01T00:00:00Z",
    updatedAt: "2024-01-15T10:30:00Z",
    chunks: 2,
    embeddings: 2,
    searches: 45,
    assignments: {
      documents: 1,
      webscraping: 1,
      datasources: 1,
    },
    models: {
      embedding: "text-embedding-ada-002",
      reranker: "cohere-rerank-multilingual-v2.0",
      llm: "gpt-4-turbo",
    },
    performance: {
      avgResponseTime: 1.2,
      successRate: 94.5,
      totalCost: 12.5,
    },
  },
  {
    id: 2,
    name: "Ecommerce Knowledge Base",
    description:
      "Base de conocimiento para chatbot de ecommerce con productos y políticas",
    status: "active",
    owner: "Ecommerce Team",
    createdAt: "2024-01-05T00:00:00Z",
    updatedAt: "2024-01-15T09:15:00Z",
    chunks: 2,
    embeddings: 2,
    searches: 67,
    assignments: {
      documents: 2,
      webscraping: 2,
      datasources: 2,
    },
    models: {
      embedding: "text-embedding-ada-002",
      reranker: "cohere-rerank-multilingual-v2.0",
      llm: "llama-2-70b-chat",
    },
    performance: {
      avgResponseTime: 0.8,
      successRate: 96.2,
      totalCost: 8.75,
    },
  },
  {
    id: 3,
    name: "Legal Document Assistant",
    description:
      "Asistente legal para análisis de documentos y consultas jurídicas",
    status: "active",
    owner: "Legal Team",
    createdAt: "2024-01-10T00:00:00Z",
    updatedAt: "2024-01-15T08:45:00Z",
    chunks: 2,
    embeddings: 2,
    searches: 23,
    assignments: {
      documents: 1,
      webscraping: 1,
      datasources: 1,
    },
    models: {
      embedding: "text-embedding-ada-002",
      reranker: "cohere-rerank-multilingual-v2.0",
      llm: "claude-3-opus",
    },
    performance: {
      avgResponseTime: 2.1,
      successRate: 98.1,
      totalCost: 25.3,
    },
  },
  {
    id: 4,
    name: "Technical Support RAG",
    description:
      "Sistema RAG para soporte técnico con documentación y troubleshooting",
    status: "active",
    owner: "Support Team",
    createdAt: "2024-01-12T00:00:00Z",
    updatedAt: "2024-01-15T07:20:00Z",
    chunks: 2,
    embeddings: 2,
    searches: 21,
    assignments: {
      documents: 1,
      webscraping: 1,
      datasources: 1,
    },
    models: {
      embedding: "text-embedding-ada-002",
      reranker: "cohere-rerank-multilingual-v2.0",
      llm: "gpt-4-turbo",
    },
    performance: {
      avgResponseTime: 1.5,
      successRate: 92.8,
      totalCost: 15.2,
    },
  },
];

export default function RAGProjectsPage() {
  const { t } = useTranslation();
  const [projects, setProjects] = useState(mockProjects);
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState("all");
  const [selectedProject, setSelectedProject] = useState<any>(null);
  const [showDetailsModal, setShowDetailsModal] = useState(false);

  const getStatusIcon = (status: string) => {
    switch (status) {
      case "active":
        return <CheckCircle className="h-4 w-4 text-green-500" />;
      case "paused":
        return <Pause className="h-4 w-4 text-yellow-500" />;
      case "error":
        return <XCircle className="h-4 w-4 text-red-500" />;
      default:
        return <AlertCircle className="h-4 w-4 text-gray-500" />;
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case "active":
        return "bg-green-50 text-green-700 border-green-200";
      case "paused":
        return "bg-yellow-50 text-yellow-700 border-yellow-200";
      case "error":
        return "bg-red-50 text-red-700 border-red-200";
      default:
        return "bg-gray-50 text-gray-700 border-gray-200";
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

    if (diffInMinutes < 1) return t("rag.projects.timeAgo.now", "Ahora");
    if (diffInMinutes < 60) return `${diffInMinutes}m`;
    if (diffInMinutes < 1440) return `${Math.floor(diffInMinutes / 60)}h`;
    return `${Math.floor(diffInMinutes / 1440)}d`;
  };

  const filteredProjects = projects.filter((project) => {
    const matchesSearch =
      project.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      project.description.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesStatus =
      statusFilter === "all" || project.status === statusFilter;
    return matchesSearch && matchesStatus;
  });

  const handleViewDetails = (project: any) => {
    // Navegar a la página de detalle con tabs
    window.location.href = `/rag/projects/${project.id}`;
  };

  const handleEditProject = (project: any) => {
    // Navegar a la página de detalle/edición
    window.location.href = `/rag/projects/${project.id}`;
  };

  const handleDeleteProject = (project: any) => {
    if (
      confirm(
        `¿Estás seguro de que quieres eliminar el proyecto "${project.name}"?`
      )
    ) {
      setProjects(projects.filter((p) => p.id !== project.id));
    }
  };

  const handleToggleStatus = (project: any) => {
    setProjects(
      projects.map((p) =>
        p.id === project.id
          ? { ...p, status: p.status === "active" ? "paused" : "active" }
          : p
      )
    );
  };

  return (
    <div className="space-y-4">
      {/* Header - Título alineado a la izquierda */}
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <Settings className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("rag.projects.title", "Proyectos RAG")}
            </h1>
            <HelpTooltip helpKey="rag.help.projects" />
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t(
              "rag.projects.subtitle",
              "Gestión de proyectos de recuperación aumentada"
            )}
          </p>
        </div>
        <Button
          onClick={() => (window.location.href = "/rag/projects/new")}
          className="bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white shadow-lg"
        >
          <Plus className="h-4 w-4 mr-2" />
          {t("rag.projects.newProject", "Nuevo Proyecto")}
        </Button>
      </div>

      {/* Filters and Search */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
        <CardBody className="p-4">
          <div className="flex flex-col sm:flex-row gap-4">
            <div className="flex-1">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <Input
                  placeholder={t(
                    "rag.projects.searchPlaceholder",
                    "Buscar proyectos..."
                  )}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
            <div className="flex gap-4">
              <Select value={statusFilter} onValueChange={setStatusFilter}>
                <SelectTrigger className="w-40">
                  <Filter className="h-4 w-4 mr-2" />
                  <SelectValue placeholder={t("rag.projects.filterStatus")} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">
                    {t("rag.projects.allStatus")}
                  </SelectItem>
                  <SelectItem value="active">
                    {t("rag.projects.active")}
                  </SelectItem>
                  <SelectItem value="paused">
                    {t("rag.projects.paused")}
                  </SelectItem>
                  <SelectItem value="error">
                    {t("rag.projects.error")}
                  </SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>
        </CardBody>
      </Card>

      {/* Projects Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-4">
        {filteredProjects.map((project) => (
          <Card
            key={project.id}
            className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all"
          >
            <CardBody className="p-4">
              <div className="flex items-start justify-between mb-4">
                <div className="flex items-center space-x-3">
                  <div className="p-2 bg-gradient-to-r from-blue-500 to-purple-600 rounded-lg">
                    <Brain className="h-5 w-5 text-white" />
                  </div>
                  <div>
                    <h3 className="font-semibold text-foreground">
                      {project.name}
                    </h3>
                    <div className="flex items-center space-x-2 mt-1">
                      {getStatusIcon(project.status)}
                      <Badge
                        variant="outline"
                        className={`text-xs ${getStatusColor(project.status)}`}
                      >
                        {project.status}
                      </Badge>
                    </div>
                  </div>
                </div>
                <div className="flex items-center space-x-1">
                  <Button
                    variant="ghost"
                    size="sm"
                    onClick={() => handleViewDetails(project)}
                  >
                    <Eye className="h-4 w-4" />
                  </Button>
                  <Button
                    variant="ghost"
                    size="sm"
                    onClick={() => handleEditProject(project)}
                  >
                    <Edit className="h-4 w-4" />
                  </Button>
                  <Button
                    variant="ghost"
                    size="sm"
                    onClick={() => handleToggleStatus(project)}
                  >
                    {project.status === "active" ? (
                      <Pause className="h-4 w-4" />
                    ) : (
                      <Play className="h-4 w-4" />
                    )}
                  </Button>
                </div>
              </div>

              <p className="text-sm text-muted-foreground mb-4 line-clamp-2">
                {project.description}
              </p>

              {/* Stats */}
              <div className="grid grid-cols-2 gap-4 mb-4">
                <div className="text-center p-3 bg-muted/30 rounded-lg border border-border">
                  <div className="text-lg font-semibold text-foreground">
                    {project.chunks}
                  </div>
                  <div className="text-xs text-muted-foreground">
                    {t("rag.projects.chunks")}
                  </div>
                </div>
                <div className="text-center p-3 bg-muted/30 rounded-lg border border-border">
                  <div className="text-lg font-semibold text-foreground">
                    {project.searches}
                  </div>
                  <div className="text-xs text-muted-foreground">
                    {t("rag.projects.searches")}
                  </div>
                </div>
              </div>

              {/* Models */}
              <div className="mb-4">
                <div className="text-xs text-muted-foreground mb-2">
                  {t("rag.projects.models")}
                </div>
                <div className="space-y-1">
                  <Badge
                    variant="outline"
                    className={`text-xs ${getModelTypeColor("EMBEDDING")}`}
                  >
                    {project.models.embedding}
                  </Badge>
                  <Badge
                    variant="outline"
                    className={`text-xs ${getModelTypeColor("RERANKER")}`}
                  >
                    {project.models.reranker}
                  </Badge>
                  <Badge
                    variant="outline"
                    className={`text-xs ${getModelTypeColor("LLM")}`}
                  >
                    {project.models.llm}
                  </Badge>
                </div>
              </div>

              {/* Footer */}
              <div className="flex items-center justify-between pt-4 border-t border-border">
                <div className="flex items-center space-x-2 text-xs text-muted-foreground">
                  <User className="h-3 w-3" />
                  <span>{project.owner}</span>
                </div>
                <div className="flex items-center space-x-2 text-xs text-muted-foreground">
                  <Calendar className="h-3 w-3" />
                  <span>{formatTimeAgo(project.updatedAt)}</span>
                </div>
              </div>
            </CardBody>
          </Card>
        ))}
      </div>

      {filteredProjects.length === 0 && (
        <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
          <CardBody className="p-12 text-center">
            <Brain className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
            <h3 className="text-lg font-semibold text-foreground mb-2">
              {t("rag.projects.noProjects")}
            </h3>
            <p className="text-muted-foreground mb-6">
              {t("rag.projects.noProjectsDescription")}
            </p>
            <Button
              onClick={() => (window.location.href = "/rag/projects/new")}
              className="bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white"
            >
              <Plus className="h-4 w-4 mr-2" />
              {t("rag.projects.createFirst")}
            </Button>
          </CardBody>
        </Card>
      )}

      {/* Project Details Modal */}
      <SimpleModal
        isOpen={showDetailsModal}
        onClose={() => setShowDetailsModal(false)}
        title={selectedProject?.name || ""}
        maxWidth="max-w-4xl"
      >
        {selectedProject && (
          <div className="space-y-6">
            <div>
              <h4 className="font-medium text-slate-900 mb-2">
                {t("rag.projects.description")}
              </h4>
              <p className="text-sm text-slate-600">
                {selectedProject.description}
              </p>
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div>
                <h4 className="font-medium text-slate-900 mb-2">
                  {t("rag.projects.owner")}
                </h4>
                <p className="text-sm text-slate-600">
                  {selectedProject.owner}
                </p>
              </div>
              <div>
                <h4 className="font-medium text-slate-900 mb-2">
                  {t("rag.projects.status")}
                </h4>
                <Badge
                  variant="outline"
                  className={`${getStatusColor(selectedProject.status)}`}
                >
                  {selectedProject.status}
                </Badge>
              </div>
            </div>

            <div>
              <h4 className="font-medium text-slate-900 mb-2">
                {t("rag.projects.performance")}
              </h4>
              <div className="grid grid-cols-3 gap-4">
                <div className="text-center p-3 bg-slate-50 rounded-lg">
                  <div className="text-lg font-semibold text-slate-900">
                    {selectedProject.performance.avgResponseTime}s
                  </div>
                  <div className="text-xs text-slate-600">
                    {t("rag.projects.avgResponseTime")}
                  </div>
                </div>
                <div className="text-center p-3 bg-slate-50 rounded-lg">
                  <div className="text-lg font-semibold text-slate-900">
                    {selectedProject.performance.successRate}%
                  </div>
                  <div className="text-xs text-slate-600">
                    {t("rag.projects.successRate")}
                  </div>
                </div>
                <div className="text-center p-3 bg-slate-50 rounded-lg">
                  <div className="text-lg font-semibold text-slate-900">
                    ${selectedProject.performance.totalCost}
                  </div>
                  <div className="text-xs text-slate-600">
                    {t("rag.projects.totalCost")}
                  </div>
                </div>
              </div>
            </div>

            <div className="flex justify-end space-x-3 pt-4 border-t border-slate-200">
              <Button
                variant="outline"
                onClick={() => setShowDetailsModal(false)}
              >
                {t("common.close")}
              </Button>
              <Button
                onClick={() => {
                  setShowDetailsModal(false);
                  handleEditProject(selectedProject);
                }}
                className="bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white"
              >
                <Edit className="h-4 w-4 mr-2" />
                {t("common.edit")}
              </Button>
            </div>
          </div>
        )}
      </SimpleModal>
    </div>
  );
}
