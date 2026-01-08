"use client";

import React, { useState } from "react";
import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { SimpleModal } from "@/components/ui/SimpleModal";
import {
  ArrowRight,
  Database,
  Eye,
  FileText,
  Plus,
  RefreshCw,
  Search,
  Shield,
  User,
  X
} from "lucide-react";
import { useRouter } from "next/navigation";

interface RAGProjectRegistry {
  id: number;
  projectId: string; // UUID del Project en PRJPROJECTS (compliance)
  ragProjectId: string; // ID en sistema de ejecución (RagSystem/RagPipeline)
  name: string;
  description: string;
  owner: string;
  createdBy: string;
  createdAt: string;
  updatedAt: string;
  // Vinculación con Project de cumplimiento
  complianceProjectId?: number; // IDXPROJECT en PRJPROJECTS
  complianceProjectName?: string; // Nombre del Project de cumplimiento
  isLinkedToProject: boolean; // Indica si está vinculado a un Project
  classificationStatus: "PENDING" | "CLASSIFIED" | "HIGH_RISK";
  classificationDate?: string;
  classifiedBy?: string;
  friaStatus: "NOT_STARTED" | "IN_PROGRESS" | "COMPLETED" | "EXPIRED";
  friaId?: number;
  euRegistrationStatus: "NOT_REGISTERED" | "PENDING" | "REGISTERED";
  lastSyncAt: string;
  complianceScore: number; // 0-100
  metrics: {
    totalChunks: number;
    totalSearches: number;
    avgResponseTime: number;
    successRate: number;
  };
  telemetryMetrics?: {
    totalEvents: number;
    totalCost: number;
    totalTokens: number;
    avgLatency: number;
  };
}

const mockProjects: RAGProjectRegistry[] = [
  {
    id: 1,
    projectId: "550e8400-e29b-41d4-a716-446655440000", // UUID del Project en PRJPROJECTS
    ragProjectId: "rag-proj-001", // ID en sistema de ejecución
    name: "DemoRAGProject",
    description: "Proyecto RAG de demostración para testing",
    owner: "Admin User",
    createdBy: "admin",
    createdAt: "2024-01-01T00:00:00Z",
    updatedAt: "2024-01-15T10:30:00Z",
    complianceProjectId: 1001, // IDXPROJECT en PRJPROJECTS
    complianceProjectName: "DemoRAGProject - Compliance",
    isLinkedToProject: true, // Vinculado automáticamente al registrarse
    classificationStatus: "HIGH_RISK",
    classificationDate: "2024-01-10T09:00:00Z",
    classifiedBy: "compliance_officer",
    friaStatus: "COMPLETED",
    friaId: 101,
    euRegistrationStatus: "REGISTERED",
    lastSyncAt: "2024-01-15T10:25:00Z",
    complianceScore: 85,
    metrics: {
      totalChunks: 2500,
      totalSearches: 45,
      avgResponseTime: 1.2,
      successRate: 94.5,
    },
    telemetryMetrics: {
      totalEvents: 1250,
      totalCost: 125.50,
      totalTokens: 125000,
      avgLatency: 1200,
    },
  },
  {
    id: 2,
    projectId: "550e8400-e29b-41d4-a716-446655440001",
    ragProjectId: "rag-proj-002",
    name: "Ecommerce Knowledge Base",
    description: "Base de conocimiento para chatbot de ecommerce con productos y políticas",
    owner: "Ecommerce Team",
    createdBy: "ecommerce_manager",
    createdAt: "2024-01-05T00:00:00Z",
    updatedAt: "2024-01-15T09:15:00Z",
    isLinkedToProject: true,
    classificationStatus: "CLASSIFIED",
    classificationDate: "2024-01-08T11:00:00Z",
    classifiedBy: "compliance_officer",
    friaStatus: "COMPLETED",
    friaId: 102,
    euRegistrationStatus: "PENDING",
    lastSyncAt: "2024-01-15T09:10:00Z",
    complianceScore: 78,
    metrics: {
      totalChunks: 5000,
      totalSearches: 67,
      avgResponseTime: 0.8,
      successRate: 96.2,
    },
    telemetryMetrics: {
      totalEvents: 3200,
      totalCost: 312.75,
      totalTokens: 312000,
      avgLatency: 800,
    },
  },
  {
    id: 3,
    projectId: "550e8400-e29b-41d4-a716-446655440002",
    ragProjectId: "rag-proj-003",
    name: "Legal Document Assistant",
    description: "Asistente legal para análisis de documentos y consultas jurídicas",
    owner: "Legal Team",
    createdBy: "legal_manager",
    createdAt: "2024-01-10T00:00:00Z",
    updatedAt: "2024-01-15T08:45:00Z",
    isLinkedToProject: true,
    classificationStatus: "HIGH_RISK",
    classificationDate: "2024-01-12T14:00:00Z",
    classifiedBy: "compliance_officer",
    friaStatus: "IN_PROGRESS",
    friaId: 103,
    euRegistrationStatus: "NOT_REGISTERED",
    lastSyncAt: "2024-01-15T08:40:00Z",
    complianceScore: 65,
    metrics: {
      totalChunks: 12000,
      totalSearches: 23,
      avgResponseTime: 2.1,
      successRate: 98.1,
    },
    telemetryMetrics: {
      totalEvents: 890,
      totalCost: 245.30,
      totalTokens: 245000,
      avgLatency: 2100,
    },
  },
  {
    id: 4,
    projectId: "550e8400-e29b-41d4-a716-446655440003",
    ragProjectId: "rag-proj-004",
    name: "Technical Support RAG",
    description: "Sistema RAG para soporte técnico con documentación y troubleshooting",
    owner: "Support Team",
    createdBy: "support_manager",
    createdAt: "2024-01-12T00:00:00Z",
    updatedAt: "2024-01-15T07:20:00Z",
    complianceProjectId: undefined, // Aún no vinculado
    isLinkedToProject: false, // No vinculado - necesita registro
    classificationStatus: "PENDING",
    friaStatus: "NOT_STARTED",
    euRegistrationStatus: "NOT_REGISTERED",
    lastSyncAt: "2024-01-15T07:15:00Z",
    complianceScore: 45,
    metrics: {
      totalChunks: 800,
      totalSearches: 21,
      avgResponseTime: 1.5,
      successRate: 92.8,
    },
    telemetryMetrics: {
      totalEvents: 560,
      totalCost: 85.20,
      totalTokens: 85000,
      avgLatency: 1500,
    },
  },
];

export default function RAGRegistryPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [projects, setProjects] = useState<RAGProjectRegistry[]>(mockProjects);
  const [searchTerm, setSearchTerm] = useState("");
  const [classificationFilter, setClassificationFilter] = useState("all");
  const [friaFilter, setFriaFilter] = useState("all");
  const [euRegistrationFilter, setEuRegistrationFilter] = useState("all");
  const [selectedProject, setSelectedProject] = useState<RAGProjectRegistry | null>(null);
  const [showDetailsModal, setShowDetailsModal] = useState(false);

  // Calcular métricas
  const totalProjects = projects.length;
  const pendingClassification = projects.filter((p) => p.classificationStatus === "PENDING").length;
  const highRiskProjects = projects.filter((p) => p.classificationStatus === "HIGH_RISK").length;
  const friaCompleted = projects.filter((p) => p.friaStatus === "COMPLETED").length;
  const euRegistered = projects.filter((p) => p.euRegistrationStatus === "REGISTERED").length;

  const filteredProjects = projects.filter((project) => {
    const matchesSearch =
      !searchTerm ||
      project.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      project.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
      project.projectId.toLowerCase().includes(searchTerm.toLowerCase()) ||
      project.owner.toLowerCase().includes(searchTerm.toLowerCase());

    const matchesClassification =
      classificationFilter === "all" || project.classificationStatus === classificationFilter;

    const matchesFria = friaFilter === "all" || project.friaStatus === friaFilter;

    const matchesEuRegistration =
      euRegistrationFilter === "all" || project.euRegistrationStatus === euRegistrationFilter;

    return matchesSearch && matchesClassification && matchesFria && matchesEuRegistration;
  });

  const getClassificationBadge = (status: string): React.ReactElement | null => {
    if (!Badge) return null;
    switch (status) {
      case "PENDING":
        return (
          <Badge variant="outline" className="bg-yellow-50 text-yellow-700 border-yellow-200">
            {t("governance.rag.registry.classification.pending", "Pendiente")}
          </Badge>
        );
      case "CLASSIFIED":
        return (
          <Badge variant="outline" className="bg-blue-50 text-blue-700 border-blue-200">
            {t("governance.rag.registry.classification.classified", "Clasificado")}
          </Badge>
        );
      case "HIGH_RISK":
        return (
          <Badge variant="danger">
            {t("governance.rag.registry.classification.highRisk", "Alto Riesgo")}
          </Badge>
        );
      default:
        return null;
    }
  };

  const getFriaBadge = (status: string): React.ReactElement | null => {
    if (!Badge) return null;
    switch (status) {
      case "NOT_STARTED":
        return (
          <Badge variant="outline" className="bg-gray-50 text-gray-700 border-gray-200">
            {t("governance.rag.registry.fria.notStarted", "No Iniciado")}
          </Badge>
        );
      case "IN_PROGRESS":
        return (
          <Badge variant="outline" className="bg-blue-50 text-blue-700 border-blue-200">
            {t("governance.rag.registry.fria.inProgress", "En Progreso")}
          </Badge>
        );
      case "COMPLETED":
        return (
          <Badge variant="success" className="bg-green-50 text-green-700 border-green-200">
            {t("governance.rag.registry.fria.completed", "Completado")}
          </Badge>
        );
      case "EXPIRED":
        return (
          <Badge variant="danger">
            {t("governance.rag.registry.fria.expired", "Expirado")}
          </Badge>
        );
      default:
        return null;
    }
  };

  const getEuRegistrationBadge = (status: string): React.ReactElement | null => {
    if (!Badge) return null;
    switch (status) {
      case "NOT_REGISTERED":
        return (
          <Badge variant="outline" className="bg-gray-50 text-gray-700 border-gray-200">
            {t("governance.rag.registry.euRegistration.notRegistered", "No Registrado")}
          </Badge>
        );
      case "PENDING":
        return (
          <Badge variant="outline" className="bg-yellow-50 text-yellow-700 border-yellow-200">
            {t("governance.rag.registry.euRegistration.pending", "Pendiente")}
          </Badge>
        );
      case "REGISTERED":
        return (
          <Badge variant="success" className="bg-green-50 text-green-700 border-green-200">
            {t("governance.rag.registry.euRegistration.registered", "Registrado")}
          </Badge>
        );
      default:
        return null;
    }
  };

  const getComplianceScoreColor = (score: number) => {
    if (score >= 80) return "text-green-600";
    if (score >= 60) return "text-yellow-600";
    return "text-red-600";
  };

  const handleViewDetails = (project: RAGProjectRegistry) => {
    router.push(`/rag/projects/${project.ragProjectId}`);
  };

  const handleClassify = (project: RAGProjectRegistry) => {
    router.push(`/governance/rag/classification?projectId=${project.projectId}`);
  };

  const handleStartFria = (project: RAGProjectRegistry) => {
    router.push(`/governance/rag/fria?projectId=${project.projectId}`);
  };

  const handleSync = async (project: RAGProjectRegistry) => {
    // TODO: Implementar sincronización
    console.log("Sincronizando proyecto:", project.ragProjectId);
  };

  const handleViewTelemetry = (project: RAGProjectRegistry) => {
    router.push(`/governance/telemetry/search?projectId=${project.projectId}`);
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString("es-ES", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <Database className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("governance.rag.registry.title", "Registro de Proyectos RAG")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t(
              "governance.rag.registry.subtitle",
              "Registrar y gestionar proyectos RAG desde la perspectiva de gobierno"
            )}
          </p>
        </div>
        <div className="flex items-center gap-2">
          <Button
            variant="outline"
            onClick={() => router.push("/governance/rag/registry/sync")}
            title={t("governance.rag.registry.syncFromExecution", "Sincronizar proyectos desde sistema de ejecución")}
          >
            <RefreshCw className="h-4 w-4 mr-2" />
            {t("governance.rag.registry.syncFromExecution", "Sincronizar")}
          </Button>
          <Button
            onClick={() => router.push("/rag/projects/new")}
            className="bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white"
          >
            <Plus className="h-4 w-4 mr-2" />
            {t("governance.rag.registry.newProject", "Registrar Proyecto")}
          </Button>
        </div>
      </div>

      {/* Métricas */}
      <div className="grid grid-cols-1 md:grid-cols-5 gap-4">
        <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
          <CardBody className="p-6">
            <div className="text-center">
              <p className="text-sm font-medium text-muted-foreground">
                {t("governance.rag.registry.metrics.total", "Total")}
              </p>
              <p className="text-3xl font-bold text-foreground mt-2">{totalProjects}</p>
            </div>
          </CardBody>
        </Card>

        <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
          <CardBody className="p-6">
            <div className="text-center">
              <p className="text-sm font-medium text-muted-foreground">
                {t("governance.rag.registry.metrics.pendingClassification", "Pendientes")}
              </p>
              <p className="text-3xl font-bold text-yellow-600 mt-2">{pendingClassification}</p>
            </div>
          </CardBody>
        </Card>

        <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
          <CardBody className="p-6">
            <div className="text-center">
              <p className="text-sm font-medium text-muted-foreground">
                {t("governance.rag.registry.metrics.highRisk", "Alto Riesgo")}
              </p>
              <p className="text-3xl font-bold text-red-600 mt-2">{highRiskProjects}</p>
            </div>
          </CardBody>
        </Card>

        <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
          <CardBody className="p-6">
            <div className="text-center">
              <p className="text-sm font-medium text-muted-foreground">
                {t("governance.rag.registry.metrics.friaCompleted", "FRIA Completado")}
              </p>
              <p className="text-3xl font-bold text-green-600 mt-2">{friaCompleted}</p>
            </div>
          </CardBody>
        </Card>

        <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
          <CardBody className="p-6">
            <div className="text-center">
              <p className="text-sm font-medium text-muted-foreground">
                {t("governance.rag.registry.metrics.euRegistered", "EU Registrados")}
              </p>
              <p className="text-3xl font-bold text-blue-600 mt-2">{euRegistered}</p>
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Filtros */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50">
        <CardBody className="p-4">
          <div className="flex flex-col md:flex-row gap-4">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
              <Input
                placeholder={t("governance.rag.registry.searchPlaceholder", "Buscar proyectos...")}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
            <Select value={classificationFilter} onValueChange={setClassificationFilter}>
              <SelectTrigger className="w-full md:w-48">
                <SelectValue placeholder={t("governance.rag.registry.filter.classification", "Clasificación")} />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">{t("governance.rag.registry.filter.all", "Todos")}</SelectItem>
                <SelectItem value="PENDING">
                  {t("governance.rag.registry.classification.pending", "Pendiente")}
                </SelectItem>
                <SelectItem value="CLASSIFIED">
                  {t("governance.rag.registry.classification.classified", "Clasificado")}
                </SelectItem>
                <SelectItem value="HIGH_RISK">
                  {t("governance.rag.registry.classification.highRisk", "Alto Riesgo")}
                </SelectItem>
              </SelectContent>
            </Select>
            <Select value={friaFilter} onValueChange={setFriaFilter}>
              <SelectTrigger className="w-full md:w-48">
                <SelectValue placeholder={t("governance.rag.registry.filter.fria", "FRIA")} />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">{t("governance.rag.registry.filter.all", "Todos")}</SelectItem>
                <SelectItem value="NOT_STARTED">
                  {t("governance.rag.registry.fria.notStarted", "No Iniciado")}
                </SelectItem>
                <SelectItem value="IN_PROGRESS">
                  {t("governance.rag.registry.fria.inProgress", "En Progreso")}
                </SelectItem>
                <SelectItem value="COMPLETED">
                  {t("governance.rag.registry.fria.completed", "Completado")}
                </SelectItem>
              </SelectContent>
            </Select>
            <Select value={euRegistrationFilter} onValueChange={setEuRegistrationFilter}>
              <SelectTrigger className="w-full md:w-48">
                <SelectValue placeholder={t("governance.rag.registry.filter.euRegistration", "Registro EU")} />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">{t("governance.rag.registry.filter.all", "Todos")}</SelectItem>
                <SelectItem value="NOT_REGISTERED">
                  {t("governance.rag.registry.euRegistration.notRegistered", "No Registrado")}
                </SelectItem>
                <SelectItem value="PENDING">
                  {t("governance.rag.registry.euRegistration.pending", "Pendiente")}
                </SelectItem>
                <SelectItem value="REGISTERED">
                  {t("governance.rag.registry.euRegistration.registered", "Registrado")}
                </SelectItem>
              </SelectContent>
            </Select>
            {(searchTerm || classificationFilter !== "all" || friaFilter !== "all" || euRegistrationFilter !== "all") && (
              <Button
                variant="outline"
                onClick={() => {
                  setSearchTerm("");
                  setClassificationFilter("all");
                  setFriaFilter("all");
                  setEuRegistrationFilter("all");
                }}
              >
                <X className="h-4 w-4 mr-2" />
                {t("common.clearFilters", "Limpiar")}
              </Button>
            )}
          </div>
        </CardBody>
      </Card>

      {/* Tabla de Proyectos */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
        <CardHeader>
          <CardTitle className="flex items-center gap-2 text-base font-medium">
            <Database className="h-5 w-5 text-primary" />
            {t("governance.rag.registry.projectsList", "Proyectos RAG Registrados")}
          </CardTitle>
        </CardHeader>
        <CardBody>
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="border-b border-border">
                  <th className="text-left p-4 font-semibold text-sm">{t("governance.rag.registry.table.name", "Nombre")}</th>
                  <th className="text-left p-4 font-semibold text-sm">{t("governance.rag.registry.table.owner", "Owner")}</th>
                  <th className="text-center p-4 font-semibold text-sm">
                    {t("governance.rag.registry.table.classification", "Clasificación")}
                  </th>
                  <th className="text-center p-4 font-semibold text-sm">{t("governance.rag.registry.table.fria", "FRIA")}</th>
                  <th className="text-center p-4 font-semibold text-sm">
                    {t("governance.rag.registry.table.euRegistration", "Registro EU")}
                  </th>
                  <th className="text-center p-4 font-semibold text-sm">
                    {t("governance.rag.registry.table.complianceScore", "Score")}
                  </th>
                  <th className="text-center p-4 font-semibold text-sm">{t("common.actions", "Acciones")}</th>
                </tr>
              </thead>
              <tbody>
                {filteredProjects.map((project) => (
                  <tr
                    key={project.id}
                    className="border-b border-border/50 hover:bg-muted/30 transition-colors"
                  >
                    <td className="p-4">
                      <div>
                        <div className="font-medium text-foreground">{project.name}</div>
                        <div className="text-xs text-muted-foreground mt-1">{project.description}</div>
                        <div className="text-xs text-muted-foreground mt-1 font-mono">
                          {project.projectId.substring(0, 8)}...
                        </div>
                        {!project.isLinkedToProject && (
                          <Badge variant="outline" className="mt-1 bg-yellow-50 text-yellow-700 border-yellow-200 text-xs">
                            {t("governance.rag.registry.notLinked", "No vinculado a Project")}
                          </Badge>
                        )}
                        {project.isLinkedToProject && project.complianceProjectName && (
                          <div className="text-xs text-muted-foreground mt-1">
                            {t("governance.rag.registry.linkedTo", "Vinculado a")}: {project.complianceProjectName}
                          </div>
                        )}
                      </div>
                    </td>
                    <td className="p-4">
                      <div className="flex items-center gap-2">
                        <User className="h-4 w-4 text-muted-foreground" />
                        <span className="text-sm">{project.owner}</span>
                      </div>
                    </td>
                    <td className="p-4 text-center">{getClassificationBadge(project.classificationStatus)}</td>
                    <td className="p-4 text-center">{getFriaBadge(project.friaStatus)}</td>
                    <td className="p-4 text-center">{getEuRegistrationBadge(project.euRegistrationStatus)}</td>
                    <td className="p-4 text-center">
                      <span className={`font-semibold ${getComplianceScoreColor(project.complianceScore)}`}>
                        {project.complianceScore}%
                      </span>
                    </td>
                    <td className="p-4">
                      <div className="flex items-center justify-center gap-2">
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleViewDetails(project)}
                          title={t("common.viewDetails", "Ver detalles")}
                        >
                          <Eye className="h-4 w-4" />
                        </Button>
                        {project.classificationStatus === "PENDING" && (
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => handleClassify(project)}
                            title={t("governance.rag.registry.actions.classify", "Clasificar")}
                          >
                            <Shield className="h-4 w-4" />
                          </Button>
                        )}
                        {project.classificationStatus === "HIGH_RISK" && project.friaStatus === "NOT_STARTED" && (
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => handleStartFria(project)}
                            title={t("governance.rag.registry.actions.startFria", "Iniciar FRIA")}
                          >
                            <FileText className="h-4 w-4" />
                          </Button>
                        )}
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleSync(project)}
                          title={t("governance.rag.registry.actions.sync", "Sincronizar")}
                        >
                          <RefreshCw className="h-4 w-4" />
                        </Button>
                        {project.telemetryMetrics && (
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => handleViewTelemetry(project)}
                            title={t("governance.rag.registry.actions.viewTelemetry", "Ver en Telemetría")}
                          >
                            <RefreshCw className="h-4 w-4" />
                          </Button>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
            {filteredProjects.length === 0 && (
              <div className="text-center py-8 text-muted-foreground">
                {t("governance.rag.registry.noResults", "No se encontraron proyectos")}
              </div>
            )}
          </div>
        </CardBody>
      </Card>

      {/* Modal de Detalles */}
      <SimpleModal
        isOpen={showDetailsModal}
        onClose={() => setShowDetailsModal(false)}
        title={selectedProject?.name || ""}
        maxWidth="max-w-4xl"
      >
        {selectedProject && (
          <div className="space-y-6">
            {/* Información General */}
            <div>
              <h4 className="font-medium text-foreground mb-3">
                {t("governance.rag.registry.details.general", "Información General")}
              </h4>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <p className="text-sm text-muted-foreground">
                    {t("governance.rag.registry.details.projectId", "Project ID (UUID)")}
                  </p>
                  <p className="text-sm font-mono text-foreground">{selectedProject.projectId}</p>
                  <p className="text-xs text-muted-foreground mt-1">
                    {t("governance.rag.registry.details.projectIdInfo", "UUID del Project en PRJPROJECTS (compliance)")}
                  </p>
                </div>
                <div>
                  <p className="text-sm text-muted-foreground">
                    {t("governance.rag.registry.details.ragProjectId", "RAG Project ID")}
                  </p>
                  <p className="text-sm text-foreground">{selectedProject.ragProjectId}</p>
                  <p className="text-xs text-muted-foreground mt-1">
                    {t("governance.rag.registry.details.ragProjectIdInfo", "ID en sistema de ejecución")}
                  </p>
                </div>
                <div>
                  <p className="text-sm text-muted-foreground">
                    {t("governance.rag.registry.details.complianceProject", "Proyecto de Cumplimiento")}
                  </p>
                  {selectedProject.isLinkedToProject && selectedProject.complianceProjectName ? (
                    <div className="flex items-center gap-2">
                      <p className="text-sm text-foreground">{selectedProject.complianceProjectName}</p>
                      {selectedProject.complianceProjectId && (
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => {
                            router.push(`/governance/projects/list?projectId=${selectedProject.complianceProjectId}`);
                          }}
                          title={t("governance.rag.registry.details.viewComplianceProject", "Ver proyecto de cumplimiento")}
                        >
                          <Eye className="w-4 h-4" />
                        </Button>
                      )}
                    </div>
                  ) : (
                    <div>
                      <p className="text-sm text-yellow-600">
                        {t("governance.rag.registry.details.notLinked", "No vinculado")}
                      </p>
                      <p className="text-xs text-muted-foreground mt-1">
                        {t("governance.rag.registry.details.linkInfo", "Se vincula automáticamente al registrarse para gobierno")}
                      </p>
                    </div>
                  )}
                </div>
                <div>
                  <p className="text-sm text-muted-foreground">{t("governance.rag.registry.details.owner", "Owner")}</p>
                  <p className="text-sm text-foreground">{selectedProject.owner}</p>
                </div>
                <div>
                  <p className="text-sm text-muted-foreground">
                    {t("governance.rag.registry.details.createdAt", "Fecha Creación")}
                  </p>
                  <p className="text-sm text-foreground">{formatDate(selectedProject.createdAt)}</p>
                </div>
              </div>
            </div>

            {/* Estados de Cumplimiento */}
            <div>
              <h4 className="font-medium text-foreground mb-3">
                {t("governance.rag.registry.details.compliance", "Estados de Cumplimiento")}
              </h4>
              <div className="grid grid-cols-3 gap-4">
                <div>
                  <p className="text-sm text-muted-foreground mb-2">
                    {t("governance.rag.registry.details.classification", "Clasificación")}
                  </p>
                  {getClassificationBadge(selectedProject.classificationStatus)}
                  {selectedProject.classificationDate && (
                    <p className="text-xs text-muted-foreground mt-1">
                      {formatDate(selectedProject.classificationDate)}
                    </p>
                  )}
                </div>
                <div>
                  <p className="text-sm text-muted-foreground mb-2">
                    {t("governance.rag.registry.details.fria", "FRIA")}
                  </p>
                  {getFriaBadge(selectedProject.friaStatus)}
                </div>
                <div>
                  <p className="text-sm text-muted-foreground mb-2">
                    {t("governance.rag.registry.details.euRegistration", "Registro EU")}
                  </p>
                  {getEuRegistrationBadge(selectedProject.euRegistrationStatus)}
                </div>
              </div>
            </div>

            {/* Métricas */}
            <div>
              <h4 className="font-medium text-foreground mb-3">
                {t("governance.rag.registry.details.metrics", "Métricas")}
              </h4>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <p className="text-sm text-muted-foreground">
                    {t("governance.rag.registry.details.totalChunks", "Total Chunks")}
                  </p>
                  <p className="text-lg font-semibold text-foreground">{selectedProject.metrics.totalChunks.toLocaleString()}</p>
                </div>
                <div>
                  <p className="text-sm text-muted-foreground">
                    {t("governance.rag.registry.details.totalSearches", "Total Búsquedas")}
                  </p>
                  <p className="text-lg font-semibold text-foreground">{selectedProject.metrics.totalSearches}</p>
                </div>
                <div>
                  <p className="text-sm text-muted-foreground">
                    {t("governance.rag.registry.details.avgResponseTime", "Tiempo Respuesta Promedio")}
                  </p>
                  <p className="text-lg font-semibold text-foreground">{selectedProject.metrics.avgResponseTime}s</p>
                </div>
                <div>
                  <p className="text-sm text-muted-foreground">
                    {t("governance.rag.registry.details.successRate", "Tasa de Éxito")}
                  </p>
                  <p className="text-lg font-semibold text-foreground">{selectedProject.metrics.successRate}%</p>
                </div>
              </div>
            </div>

            {/* Métricas de Telemetría */}
            {selectedProject.telemetryMetrics && (
              <div>
                <h4 className="font-medium text-foreground mb-3">
                  {t("governance.rag.registry.details.telemetry", "Métricas desde Telemetría")}
                </h4>
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <p className="text-sm text-muted-foreground">
                      {t("governance.rag.registry.details.totalEvents", "Total Eventos")}
                    </p>
                    <p className="text-lg font-semibold text-foreground">
                      {selectedProject.telemetryMetrics.totalEvents.toLocaleString()}
                    </p>
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground">
                      {t("governance.rag.registry.details.totalCost", "Costo Total")}
                    </p>
                    <p className="text-lg font-semibold text-foreground">
                      ${selectedProject.telemetryMetrics.totalCost.toFixed(2)}
                    </p>
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground">
                      {t("governance.rag.registry.details.totalTokens", "Total Tokens")}
                    </p>
                    <p className="text-lg font-semibold text-foreground">
                      {selectedProject.telemetryMetrics.totalTokens.toLocaleString()}
                    </p>
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground">
                      {t("governance.rag.registry.details.avgLatency", "Latencia Promedio")}
                    </p>
                    <p className="text-lg font-semibold text-foreground">
                      {selectedProject.telemetryMetrics.avgLatency}ms
                    </p>
                  </div>
                </div>
                <div className="mt-4">
                  <Button
                    variant="outline"
                    onClick={() => handleViewTelemetry(selectedProject)}
                    className="w-full"
                  >
                    <span>{t("governance.rag.registry.details.viewInTelemetry", "Ver en Telemetría")}</span>
                    <ArrowRight className="h-4 w-4 ml-2" />
                  </Button>
                </div>
              </div>
            )}

            {/* Sincronización */}
            <div>
              <p className="text-sm text-muted-foreground">
                {t("governance.rag.registry.details.lastSync", "Última Sincronización")}
              </p>
              <p className="text-sm text-foreground">{formatDate(selectedProject.lastSyncAt)}</p>
            </div>

            {/* Acciones */}
            <div className="flex justify-end gap-3 pt-4 border-t border-border">
              <Button variant="outline" onClick={() => setShowDetailsModal(false)}>
                {t("common.close", "Cerrar")}
              </Button>
              {selectedProject.classificationStatus === "PENDING" && (
                <Button onClick={() => {
                  setShowDetailsModal(false);
                  handleClassify(selectedProject);
                }}>
                  <Shield className="h-4 w-4 mr-2" />
                  {t("governance.rag.registry.actions.classify", "Clasificar")}
                </Button>
              )}
              {selectedProject.classificationStatus === "HIGH_RISK" && selectedProject.friaStatus === "NOT_STARTED" && (
                <Button onClick={() => {
                  setShowDetailsModal(false);
                  handleStartFria(selectedProject);
                }}>
                  <FileText className="h-4 w-4 mr-2" />
                  {t("governance.rag.registry.actions.startFria", "Iniciar FRIA")}
                </Button>
              )}
            </div>
          </div>
        )}
      </SimpleModal>
    </div>
  );
}
