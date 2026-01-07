"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  FileText,
  Search,
  Plus,
  Eye,
  Edit,
  Calendar,
  User,
  CheckCircle,
  Clock,
  AlertTriangle,
  Shield,
  ChevronLeft,
  ChevronRight,
  History,
  Send,
  Download,
  Copy,
} from "lucide-react";
import { useEffect, useState } from "react";

interface FriaAssessment {
  id: number;
  projectId: number;
  version: string | null;
  createdAt: string;
  updatedAt: string;
  status: "DRAFT" | "COMPLETED" | "NOTIFIED" | "APPROVED";
  completenessScore: number;
  finalRisk: number | null;
  riskLevel: "low" | "medium" | "high" | "critical" | null;
  notified: boolean;
  notificationDate: string | null;
  approved: boolean;
  approvalDate: string | null;
  createdBy: string;
}

interface ProjectWithFrias {
  id: number;
  name: string;
  description: string;
  category: string;
  frias: FriaAssessment[];
  latestFria: FriaAssessment | null;
  totalFrias: number;
}

const ITEMS_PER_PAGE = 10;

// Mock data - TODO: Reemplazar con llamada real a API
const mockProjectsWithFrias: ProjectWithFrias[] = [
  {
    id: 1001,
    name: "AI Credit Scoring System",
    description: "Sistema de evaluación crediticia basado en IA",
    category: "B.1",
    totalFrias: 3,
    latestFria: {
      id: 1,
      projectId: 1001,
      version: "v2.1",
      createdAt: "2025-12-15T10:00:00Z",
      updatedAt: "2025-12-15T14:30:00Z",
      status: "NOTIFIED",
      completenessScore: 1.0,
      finalRisk: 0.85,
      riskLevel: "high",
      notified: true,
      notificationDate: "2025-12-15T14:30:00Z",
      approved: true,
      approvalDate: "2025-12-15T12:00:00Z",
      createdBy: "Juan Pérez",
    },
    frias: [
      {
        id: 1,
        projectId: 1001,
        version: "v2.1",
        createdAt: "2025-12-15T10:00:00Z",
        updatedAt: "2025-12-15T14:30:00Z",
        status: "NOTIFIED",
        completenessScore: 1.0,
        finalRisk: 0.85,
        riskLevel: "high",
        notified: true,
        notificationDate: "2025-12-15T14:30:00Z",
        approved: true,
        approvalDate: "2025-12-15T12:00:00Z",
        createdBy: "Juan Pérez",
      },
      {
        id: 2,
        projectId: 1001,
        version: "v2.0",
        createdAt: "2025-11-20T09:00:00Z",
        updatedAt: "2025-11-20T16:00:00Z",
        status: "COMPLETED",
        completenessScore: 0.95,
        finalRisk: 0.78,
        riskLevel: "high",
        notified: false,
        notificationDate: null,
        approved: true,
        approvalDate: "2025-11-20T15:00:00Z",
        createdBy: "María García",
      },
      {
        id: 3,
        projectId: 1001,
        version: "v1.0",
        createdAt: "2025-10-10T08:00:00Z",
        updatedAt: "2025-10-10T12:00:00Z",
        status: "COMPLETED",
        completenessScore: 0.90,
        finalRisk: 0.72,
        riskLevel: "medium",
        notified: false,
        notificationDate: null,
        approved: true,
        approvalDate: "2025-10-10T11:00:00Z",
        createdBy: "Carlos López",
      },
    ],
  },
  {
    id: 1002,
    name: "Facial Recognition System",
    description: "Sistema de reconocimiento facial para seguridad",
    category: "B.2",
    totalFrias: 2,
    latestFria: {
      id: 4,
      projectId: 1002,
      version: "v1.2",
      createdAt: "2025-12-10T11:00:00Z",
      updatedAt: "2025-12-10T15:00:00Z",
      status: "DRAFT",
      completenessScore: 0.60,
      finalRisk: null,
      riskLevel: null,
      notified: false,
      notificationDate: null,
      approved: false,
      approvalDate: null,
      createdBy: "Ana Martínez",
    },
    frias: [
      {
        id: 4,
        projectId: 1002,
        version: "v1.2",
        createdAt: "2025-12-10T11:00:00Z",
        updatedAt: "2025-12-10T15:00:00Z",
        status: "DRAFT",
        completenessScore: 0.60,
        finalRisk: null,
        riskLevel: null,
        notified: false,
        notificationDate: null,
        approved: false,
        approvalDate: null,
        createdBy: "Ana Martínez",
      },
      {
        id: 5,
        projectId: 1002,
        version: "v1.0",
        createdAt: "2025-11-01T10:00:00Z",
        updatedAt: "2025-11-01T14:00:00Z",
        status: "COMPLETED",
        completenessScore: 0.98,
        finalRisk: 0.92,
        riskLevel: "critical",
        notified: true,
        notificationDate: "2025-11-01T14:00:00Z",
        approved: true,
        approvalDate: "2025-11-01T13:00:00Z",
        createdBy: "Pedro Sánchez",
      },
    ],
  },
  {
    id: 1003,
    name: "Automated Hiring System",
    description: "Sistema automatizado de selección de personal",
    category: "B.4",
    totalFrias: 1,
    latestFria: {
      id: 6,
      projectId: 1003,
      version: "v1.0",
      createdAt: "2025-12-05T09:00:00Z",
      updatedAt: "2025-12-05T13:00:00Z",
      status: "NOTIFIED",
      completenessScore: 1.0,
      finalRisk: 0.88,
      riskLevel: "high",
      notified: true,
      notificationDate: "2025-12-05T13:00:00Z",
      approved: true,
      approvalDate: "2025-12-05T12:00:00Z",
      createdBy: "Laura Fernández",
    },
    frias: [
      {
        id: 6,
        projectId: 1003,
        version: "v1.0",
        createdAt: "2025-12-05T09:00:00Z",
        updatedAt: "2025-12-05T13:00:00Z",
        status: "NOTIFIED",
        completenessScore: 1.0,
        finalRisk: 0.88,
        riskLevel: "high",
        notified: true,
        notificationDate: "2025-12-05T13:00:00Z",
        approved: true,
        approvalDate: "2025-12-05T12:00:00Z",
        createdBy: "Laura Fernández",
      },
    ],
  },
];

export default function FriaProjectsPage() {
  const { t, language, mounted } = useTranslation();
  const [projects, setProjects] = useState<ProjectWithFrias[]>([]);
  const [filteredProjects, setFilteredProjects] = useState<ProjectWithFrias[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [filterStatus, setFilterStatus] = useState<string>("all");
  const [expandedProjects, setExpandedProjects] = useState<Set<number>>(new Set());
  const [currentPage, setCurrentPage] = useState(1);
  const [statistics, setStatistics] = useState({
    totalProjects: 0,
    totalFrias: 0,
    notified: 0,
    draft: 0,
  });

  useEffect(() => {
    loadProjects();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentPage, filterStatus, searchQuery]);

  useEffect(() => {
    // El filtrado ahora se hace en el backend, pero mantenemos el filtrado local para búsqueda en tiempo real
    if (!searchQuery && filterStatus === "all") {
      setFilteredProjects(projects);
    } else {
      filterProjects();
    }
  }, [searchQuery, filterStatus, projects]);

  const loadProjects = async () => {
    try {
      setLoading(true);

      // Construir URL con parámetros
      const params = new URLSearchParams();
      params.append("page", String(currentPage - 1)); // Backend usa 0-indexed
      params.append("size", String(ITEMS_PER_PAGE));
      if (filterStatus !== "all") {
        params.append("status", filterStatus);
      }
      if (searchQuery) {
        params.append("search", searchQuery);
      }

      const response = await fetch(`/api/v1/fria/projects?${params.toString()}`);

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();

      // Mapear respuesta del backend al formato del frontend
      const mappedProjects: ProjectWithFrias[] = (data.projects || []).map((project: any) => ({
        id: project.id,
        name: project.name || `Project ${project.id}`,
        description: project.description || "",
        category: project.category || "",
        totalFrias: project.totalFrias || 0,
        latestFria: project.latestFria ? mapFriaSummary(project.latestFria) : null,
        frias: (project.frias || []).map(mapFriaSummary),
      }));

      setProjects(mappedProjects);
      setFilteredProjects(mappedProjects);

      // Actualizar estadísticas desde el backend
      if (data.statistics) {
        setStatistics({
          totalProjects: data.statistics.totalProjects || 0,
          totalFrias: data.statistics.totalFrias || 0,
          notified: data.statistics.notified || 0,
          draft: data.statistics.draft || 0,
        });
      }

      // Actualizar paginación si viene del backend
      if (data.pagination) {
        // El backend ya paginó, así que los proyectos recibidos son los de la página actual
      }
    } catch (error) {
      console.error("Error loading projects with FRIA:", error);
      // En caso de error, usar datos mock como fallback
      setProjects(mockProjectsWithFrias);
      setFilteredProjects(mockProjectsWithFrias);
    } finally {
      setLoading(false);
    }
  };

  const mapFriaSummary = (fria: any): FriaAssessment => ({
    id: fria.id,
    projectId: fria.projectId,
    version: fria.version || null,
    createdAt: fria.createdAt || new Date().toISOString(),
    updatedAt: fria.updatedAt || new Date().toISOString(),
    status: fria.status || "DRAFT",
    completenessScore: fria.completenessScore ? parseFloat(fria.completenessScore) : 0,
    finalRisk: fria.finalRisk ? parseFloat(fria.finalRisk) : null,
    riskLevel: fria.riskLevel || null,
    notified: fria.notified || false,
    notificationDate: fria.notificationDate || null,
    approved: fria.approved || false,
    approvalDate: fria.approvalDate || null,
    createdBy: fria.createdBy || "Unknown",
  });

  const filterProjects = () => {
    let filtered = [...projects];

    if (searchQuery) {
      const query = searchQuery.toLowerCase();
      filtered = filtered.filter(
        (project) =>
          project.name.toLowerCase().includes(query) ||
          project.description.toLowerCase().includes(query) ||
          project.category.toLowerCase().includes(query)
      );
    }

    if (filterStatus !== "all") {
      filtered = filtered.filter((project) => {
        if (!project.latestFria) return false;
        return project.latestFria.status === filterStatus;
      });
    }

    setFilteredProjects(filtered);
  };

  const toggleProjectExpansion = (projectId: number) => {
    const newExpanded = new Set(expandedProjects);
    if (newExpanded.has(projectId)) {
      newExpanded.delete(projectId);
    } else {
      newExpanded.add(projectId);
    }
    setExpandedProjects(newExpanded);
  };

  const getStatusBadge = (status: string) => {
    switch (status) {
      case "DRAFT":
        return (
          <Badge variant="outline" className="bg-yellow-50 text-yellow-700 border-yellow-300 text-sm">
            <Clock className="h-3.5 w-3.5 mr-1" />
            {t("governance.compliance.fria.status.draft", "Borrador")}
          </Badge>
        );
      case "COMPLETED":
        return (
          <Badge variant="outline" className="bg-blue-50 text-blue-700 border-blue-300 text-sm">
            <CheckCircle className="h-3.5 w-3.5 mr-1" />
            {t("governance.compliance.fria.status.completed", "Completada")}
          </Badge>
        );
      case "NOTIFIED":
        return (
          <Badge variant="outline" className="bg-green-50 text-green-700 border-green-300 text-sm">
            <Send className="h-3.5 w-3.5 mr-1" />
            {t("governance.compliance.fria.status.notified", "Notificada")}
          </Badge>
        );
      case "APPROVED":
        return (
          <Badge variant="outline" className="bg-purple-50 text-purple-700 border-purple-300 text-sm">
            <CheckCircle className="h-3.5 w-3.5 mr-1" />
            {t("governance.compliance.fria.status.approved", "Aprobada")}
          </Badge>
        );
      default:
        return null;
    }
  };

  const getRiskBadge = (riskLevel: string | null) => {
    if (!riskLevel) return null;
    switch (riskLevel) {
      case "low":
        return (
          <Badge variant="outline" className="bg-green-50 text-green-700 border-green-300 text-sm">
            {t("governance.compliance.fria.risk.low", "Bajo")}
          </Badge>
        );
      case "medium":
        return (
          <Badge variant="outline" className="bg-yellow-50 text-yellow-700 border-yellow-300 text-sm">
            {t("governance.compliance.fria.risk.medium", "Medio")}
          </Badge>
        );
      case "high":
        return (
          <Badge variant="outline" className="bg-orange-50 text-orange-700 border-orange-300 text-sm">
            {t("governance.compliance.fria.risk.high", "Alto")}
          </Badge>
        );
      case "critical":
        return (
          <Badge variant="outline" className="bg-red-50 text-red-700 border-red-300 text-sm">
            {t("governance.compliance.fria.risk.critical", "Crítico")}
          </Badge>
        );
      default:
        return null;
    }
  };

  const formatDate = (dateString: string): string => {
    const date = new Date(dateString);
    return date.toLocaleDateString("es-ES", {
      year: "numeric",
      month: "short",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  const formatDateShort = (dateString: string): string => {
    const date = new Date(dateString);
    return date.toLocaleDateString("es-ES", {
      year: "numeric",
      month: "short",
      day: "numeric",
    });
  };

  const handleCreateNewFria = (projectId: number) => {
    window.location.href = `/governance/compliance/fria?projectId=${projectId}`;
  };

  const handleCreateVersion = (projectId: number, baseFriaId: number) => {
    if (!projectId || !baseFriaId) {
      console.error("Missing projectId or baseFriaId:", { projectId, baseFriaId });
      return;
    }
    const url = `/governance/compliance/fria?projectId=${projectId}&baseFriaId=${baseFriaId}`;
    window.location.href = url;
  };

  // La paginación ahora se hace en el backend, así que usamos directamente los proyectos recibidos
  const paginatedProjects = filteredProjects;
  // Calcular totalPages basado en el total de elementos del backend
  // Por ahora usamos una estimación basada en los proyectos recibidos
  const totalPages = Math.max(1, Math.ceil((filteredProjects.length || 0) / ITEMS_PER_PAGE));
  const startIndex = 0; // El backend ya paginó
  const endIndex = filteredProjects.length;

  const handlePreviousPage = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
  };

  const handleNextPage = () => {
    if (currentPage < totalPages) {
      setCurrentPage(currentPage + 1);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 right-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute bottom-1/4 left-1/4 w-1.5 h-1.5 bg-blue-400/20 rounded-full animate-pulse delay-300" />
      </div>

      <div className="relative z-10 w-full max-w-full px-4 py-6 space-y-6">
        {/* Header */}
        <div>
          <div className="flex items-center gap-3 mb-2">
            <Shield className="w-8 h-8 text-blue-500" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-blue-500 to-blue-700 bg-clip-text text-transparent">
              {t("governance.compliance.fria.projects.title", "Proyectos con Evaluaciones FRIA")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t("governance.compliance.fria.projects.subtitle", "Gestión de evaluaciones FRIA por proyecto, versiones e historial")}
          </p>
        </div>

        {/* Estadísticas */}
        <div className="grid grid-cols-4 gap-3">
          <Card className="border-2">
            <CardBody className="p-3">
              <div className="text-xl font-bold">{statistics.totalProjects}</div>
              <div className="text-xs text-muted-foreground">
                {t("governance.compliance.fria.projects.totalProjects", "Total Proyectos")}
              </div>
            </CardBody>
          </Card>
          <Card className="border-2">
            <CardBody className="p-3">
              <div className="text-xl font-bold text-blue-600">
                {statistics.totalFrias}
              </div>
              <div className="text-xs text-muted-foreground">
                {t("governance.compliance.fria.projects.totalFrias", "Total Evaluaciones")}
              </div>
            </CardBody>
          </Card>
          <Card className="border-2">
            <CardBody className="p-3">
              <div className="text-xl font-bold text-green-600">
                {statistics.notified}
              </div>
              <div className="text-xs text-muted-foreground">
                {t("governance.compliance.fria.projects.notified", "Notificadas")}
              </div>
            </CardBody>
          </Card>
          <Card className="border-2">
            <CardBody className="p-3">
              <div className="text-xl font-bold text-yellow-600">
                {statistics.draft}
              </div>
              <div className="text-xs text-muted-foreground">
                {t("governance.compliance.fria.projects.draft", "Borradores")}
              </div>
            </CardBody>
          </Card>
        </div>

        {/* Filtros y Búsqueda */}
        <Card className="border-2">
          <CardBody className="p-3">
            <div className="grid grid-cols-2 gap-3">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
                <Input
                  type="text"
                  placeholder={t("governance.compliance.fria.projects.searchPlaceholder", "Buscar por nombre, descripción, categoría...")}
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="pl-10"
                />
              </div>
              <div>
                <select
                  value={filterStatus}
                  onChange={(e) => setFilterStatus(e.target.value)}
                  className="w-full px-3 py-2 border rounded-md bg-background"
                >
                  <option value="all">
                    {t("governance.compliance.fria.projects.allStatuses", "Todos los Estados")}
                  </option>
                  <option value="DRAFT">
                    {t("governance.compliance.fria.status.draft", "Borrador")}
                  </option>
                  <option value="COMPLETED">
                    {t("governance.compliance.fria.status.completed", "Completada")}
                  </option>
                  <option value="NOTIFIED">
                    {t("governance.compliance.fria.status.notified", "Notificada")}
                  </option>
                  <option value="APPROVED">
                    {t("governance.compliance.fria.status.approved", "Aprobada")}
                  </option>
                </select>
              </div>
            </div>
          </CardBody>
        </Card>

        {/* Lista de Proyectos */}
        <div className="space-y-4">
          {paginatedProjects.length === 0 ? (
            <Card className="border-2">
              <CardBody className="p-8 text-center">
                <FileText className="h-12 w-12 mx-auto mb-4 opacity-50 text-muted-foreground" />
                <p className="text-base text-muted-foreground">
                  {t("governance.compliance.fria.projects.noResults", "No se encontraron proyectos que coincidan con los filtros.")}
                </p>
              </CardBody>
            </Card>
          ) : (
            paginatedProjects.map((project) => (
              <Card key={project.id} className="border-2 hover:shadow-lg transition-shadow">
                <CardBody className="p-0">
                  {/* Header del Proyecto */}
                  <div className="p-4 border-b bg-muted/30">
                    <div className="flex items-center justify-between">
                      <div className="flex-1">
                        <div className="flex items-center gap-3 mb-2">
                          <h3 className="text-lg font-semibold">{project.name}</h3>
                          <Badge variant="outline" className="font-mono text-sm">
                            {project.category}
                          </Badge>
                          <Badge variant="secondary" className="text-sm">
                            {project.totalFrias} {t("governance.compliance.fria.projects.frias", "evaluaciones")}
                          </Badge>
                        </div>
                        <p className="text-sm text-muted-foreground line-clamp-2">
                          {project.description}
                        </p>
                      </div>
                      <div className="flex items-center gap-2 ml-4">
                        {project.latestFria && (
                          <div className="flex items-center gap-2">
                            {getStatusBadge(project.latestFria.status)}
                            {getRiskBadge(project.latestFria.riskLevel)}
                          </div>
                        )}
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => toggleProjectExpansion(project.id)}
                          className="h-8 px-3"
                        >
                          <History className="h-4 w-4 mr-2" />
                          {expandedProjects.has(project.id)
                            ? t("common.collapse", "Contraer")
                            : t("common.expand", "Expandir")}
                        </Button>
                        <Button
                          variant="primary"
                          size="sm"
                          onClick={() => handleCreateNewFria(project.id)}
                          className="h-8 px-3"
                        >
                          <Plus className="h-4 w-4 mr-2" />
                          {t("governance.compliance.fria.projects.newFria", "Nueva FRIA")}
                        </Button>
                      </div>
                    </div>
                  </div>

                  {/* Última Evaluación FRIA (Resumen) */}
                  {project.latestFria && (
                    <div className="p-4 border-b bg-muted/10">
                      <div className="flex items-center justify-between">
                        <div className="flex-1">
                          <div className="flex items-center gap-3 mb-2">
                            <span className="text-sm font-medium text-muted-foreground">
                              {t("governance.compliance.fria.projects.latestFria", "Última Evaluación")}:
                            </span>
                            {project.latestFria.version && (
                              <Badge variant="outline" className="text-xs">
                                {project.latestFria.version}
                              </Badge>
                            )}
                            <span className="text-xs text-muted-foreground">
                              {formatDateShort(project.latestFria.createdAt)}
                            </span>
                          </div>
                          <div className="flex items-center gap-4 text-xs text-muted-foreground">
                            <span>
                              {t("governance.compliance.fria.projects.completeness", "Completitud")}:{" "}
                              <strong>{(project.latestFria.completenessScore * 100).toFixed(0)}%</strong>
                            </span>
                            {project.latestFria.finalRisk !== null && (
                              <span>
                                {t("governance.compliance.fria.projects.risk", "Riesgo")}:{" "}
                                <strong>{(project.latestFria.finalRisk * 100).toFixed(0)}%</strong>
                              </span>
                            )}
                            <span>
                              {t("governance.compliance.fria.projects.createdBy", "Creado por")}:{" "}
                              <strong>{project.latestFria.createdBy}</strong>
                            </span>
                          </div>
                        </div>
                        <div className="flex items-center gap-1">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => window.location.href = `/governance/compliance/fria/${project.latestFria!.id}`}
                            className="h-8 w-8 p-0"
                            title={t("common.view", "Ver")}
                          >
                            <Eye className="h-4 w-4" />
                          </Button>
                          {project.latestFria.status === "DRAFT" && (
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => window.location.href = `/governance/compliance/fria?friaId=${project.latestFria!.id}`}
                              className="h-8 w-8 p-0"
                              title={t("common.edit", "Editar")}
                            >
                              <Edit className="h-4 w-4" />
                            </Button>
                          )}
                          {project.latestFria && (
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => {
                                handleCreateVersion(project.id, project.latestFria!.id);
                              }}
                              className="h-8 w-8 p-0"
                              title={t("governance.compliance.fria.projects.createVersion", "Crear Versión")}
                            >
                              <Copy className="h-4 w-4" />
                            </Button>
                          )}
                        </div>
                      </div>
                    </div>
                  )}

                  {/* Lista Expandida de Todas las Evaluaciones FRIA */}
                  {expandedProjects.has(project.id) && project.frias.length > 0 && (
                    <div className="p-4 bg-muted/5">
                      <h4 className="text-sm font-semibold mb-3 text-muted-foreground">
                        {t("governance.compliance.fria.projects.allEvaluations", "Todas las Evaluaciones")} ({project.frias.length})
                      </h4>
                      <div className="space-y-2">
                        {project.frias.map((fria) => (
                          <div
                            key={fria.id}
                            className="p-3 border rounded-lg bg-background hover:bg-muted/30 transition-colors"
                          >
                            <div className="flex items-center justify-between">
                              <div className="flex-1">
                                <div className="flex items-center gap-2 mb-2">
                                  {fria.version && (
                                    <Badge variant="outline" className="text-xs font-mono">
                                      {fria.version}
                                    </Badge>
                                  )}
                                  {getStatusBadge(fria.status)}
                                  {getRiskBadge(fria.riskLevel)}
                                  <span className="text-xs text-muted-foreground">
                                    {formatDate(fria.createdAt)}
                                  </span>
                                </div>
                                <div className="flex items-center gap-4 text-xs text-muted-foreground">
                                  <span>
                                    {t("governance.compliance.fria.projects.completeness", "Completitud")}:{" "}
                                    <strong>{(fria.completenessScore * 100).toFixed(0)}%</strong>
                                  </span>
                                  {fria.finalRisk !== null && (
                                    <span>
                                      {t("governance.compliance.fria.projects.risk", "Riesgo")}:{" "}
                                      <strong>{(fria.finalRisk * 100).toFixed(0)}%</strong>
                                    </span>
                                  )}
                                  {fria.notified && fria.notificationDate && (
                                    <span className="text-green-600">
                                      {t("governance.compliance.fria.projects.notifiedOn", "Notificada")}:{" "}
                                      {formatDateShort(fria.notificationDate)}
                                    </span>
                                  )}
                                  {fria.approved && fria.approvalDate && (
                                    <span className="text-purple-600">
                                      {t("governance.compliance.fria.projects.approvedOn", "Aprobada")}:{" "}
                                      {formatDateShort(fria.approvalDate)}
                                    </span>
                                  )}
                                  <span>
                                    {t("governance.compliance.fria.projects.createdBy", "Por")}:{" "}
                                    <strong>{fria.createdBy}</strong>
                                  </span>
                                </div>
                              </div>
                              <div className="flex items-center gap-1 ml-4">
                                <Button
                                  variant="ghost"
                                  size="sm"
                                  onClick={() => window.location.href = `/governance/compliance/fria/${fria.id}`}
                                  className="h-8 w-8 p-0"
                                  title={t("common.view", "Ver")}
                                >
                                  <Eye className="h-4 w-4" />
                                </Button>
                                {fria.status === "DRAFT" && (
                                  <Button
                                    variant="ghost"
                                    size="sm"
                                    onClick={() => window.location.href = `/governance/compliance/fria?friaId=${fria.id}`}
                                    className="h-8 w-8 p-0"
                                    title={t("common.edit", "Editar")}
                                  >
                                    <Edit className="h-4 w-4" />
                                  </Button>
                                )}
                                <Button
                                  variant="ghost"
                                  size="sm"
                                  onClick={() => {
                                    handleCreateVersion(project.id, fria.id);
                                  }}
                                  className="h-8 w-8 p-0"
                                  title={t("governance.compliance.fria.projects.createVersion", "Crear Versión")}
                                >
                                  <Copy className="h-4 w-4" />
                                </Button>
                                <Button
                                  variant="ghost"
                                  size="sm"
                                  className="h-8 w-8 p-0"
                                  title={t("common.download", "Descargar PDF")}
                                >
                                  <Download className="h-4 w-4" />
                                </Button>
                              </div>
                            </div>
                          </div>
                        ))}
                      </div>
                    </div>
                  )}
                </CardBody>
              </Card>
            ))
          )}
        </div>

        {/* Paginación */}
        {filteredProjects.length > 0 && (
          <Card className="border-2">
            <CardBody className="p-3">
              <div className="flex items-center justify-between">
                <div className="text-sm text-muted-foreground">
                  {t("governance.compliance.fria.projects.table.showing", "Mostrando")}{" "}
                  {paginatedProjects.length > 0 ? (currentPage - 1) * ITEMS_PER_PAGE + 1 : 0} - {Math.min(currentPage * ITEMS_PER_PAGE, statistics.totalProjects)}{" "}
                  {t("governance.compliance.fria.projects.table.of", "de")}{" "}
                  {statistics.totalProjects}
                </div>
                <div className="flex items-center gap-2">
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={handlePreviousPage}
                    disabled={currentPage === 1}
                    className="h-8 px-3 text-sm"
                  >
                    <ChevronLeft className="h-4 w-4 mr-1" />
                    {t("common.previous", "Anterior")}
                  </Button>
                  <div className="text-sm text-muted-foreground px-2">
                    {currentPage} / {totalPages}
                  </div>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={handleNextPage}
                    disabled={currentPage === totalPages}
                    className="h-8 px-3 text-sm"
                  >
                    {t("common.next", "Siguiente")}
                    <ChevronRight className="h-4 w-4 ml-1" />
                  </Button>
                </div>
              </div>
            </CardBody>
          </Card>
        )}
      </div>
    </div>
  );
}
