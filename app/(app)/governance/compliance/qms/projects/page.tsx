"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  ClipboardCheck,
  Search,
  Eye,
  AlertTriangle,
  CheckCircle,
  TrendingUp,
  Calendar,
  ChevronLeft,
  ChevronRight,
  ExternalLink,
} from "lucide-react";
import { useEffect, useState } from "react";

interface ProjectQmsSummary {
  projectId: number;
  projectName: string;
  description?: string;
  overallScore: number;
  complianceStatus: "COMPLIANT" | "NON_COMPLIANT" | "PARTIAL";
  totalModules: number;
  compliantModules: number;
  gapsCount: number;
  lastUpdated: string;
  createdAt: string;
}

interface QmsProjectsResponse {
  projects: ProjectQmsSummary[];
  statistics: {
    totalProjects: number;
    totalCompliant: number;
    totalPartial: number;
    totalNonCompliant: number;
    averageScore: number;
  };
  pagination?: {
    page: number;
    size: number;
    totalPages: number;
    totalElements: number;
  };
}

const ITEMS_PER_PAGE = 12; // Aumentado para mostrar mejor con 2 columnas

export default function QmsProjectsPage() {
  const { t, language } = useTranslation();
  const [data, setData] = useState<QmsProjectsResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [filterStatus, setFilterStatus] = useState<string>("all");
  const [currentPage, setCurrentPage] = useState(1);

  useEffect(() => {
    loadProjects();
  }, [currentPage, filterStatus, searchQuery]);

  const loadProjects = async () => {
    try {
      setLoading(true);

      const params = new URLSearchParams();
      params.append("page", String(currentPage - 1));
      params.append("size", String(ITEMS_PER_PAGE));
      if (filterStatus !== "all") {
        params.append("status", filterStatus);
      }
      if (searchQuery) {
        params.append("search", searchQuery);
      }

      const response = await fetch(`/api/governance/compliance/qms/projects?${params.toString()}`);

      if (!response.ok) {
        throw new Error("Failed to load QMS projects");
      }

      const result = await response.json();
      if (result.success) {
        setData(result.data);
      } else {
        throw new Error(result.error || "Failed to load QMS projects");
      }
    } catch (error) {
      console.error("Error loading QMS projects:", error);
      // En caso de error, usar datos vacíos
      setData({
        projects: [],
        statistics: {
          totalProjects: 0,
          totalCompliant: 0,
          totalPartial: 0,
          totalNonCompliant: 0,
          averageScore: 0,
        },
      });
    } finally {
      setLoading(false);
    }
  };

  const getComplianceStatusBadge = (status: string) => {
    switch (status) {
      case "COMPLIANT":
        return (
          <Badge className="bg-green-100 text-green-800 dark:bg-green-900/20 dark:text-green-300">
            <CheckCircle className="h-3 w-3 mr-1" />
            {t("governance.compliance.qms.statusValues.compliant", "Cumple")}
          </Badge>
        );
      case "PARTIAL":
        return (
          <Badge className="bg-yellow-100 text-yellow-800 dark:bg-yellow-900/20 dark:text-yellow-300">
            <AlertTriangle className="h-3 w-3 mr-1" />
            {t("governance.compliance.qms.statusValues.partial", "Parcial")}
          </Badge>
        );
      default:
        return (
          <Badge variant="danger">
            <AlertTriangle className="h-3 w-3 mr-1" />
            {t("governance.compliance.qms.statusValues.nonCompliant", "No Cumple")}
          </Badge>
        );
    }
  };

  const getScoreColor = (score: number) => {
    if (score >= 0.85) return "text-green-600";
    if (score >= 0.70) return "text-yellow-600";
    return "text-red-600";
  };

  const formatDate = (dateString: string): string => {
    const date = new Date(dateString);
    const localeMap: Record<string, string> = {
      es: "es-ES",
      en: "en-US",
      fr: "fr-FR",
      de: "de-DE",
      it: "it-IT",
      pt: "pt-PT",
    };
    const locale = localeMap[language] || "es-ES";
    return date.toLocaleDateString(locale, {
      year: "numeric",
      month: "short",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  const handleViewDashboard = (projectId: number) => {
    window.location.href = `/governance/compliance/qms?projectId=${projectId}`;
  };

  const handleViewReview = (projectId: number) => {
    window.location.href = `/governance/compliance/conformity-review?projectId=${projectId}`;
  };

  const handlePreviousPage = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
  };

  const handleNextPage = () => {
    if (data?.pagination && currentPage < data.pagination.totalPages) {
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

  const statistics = data?.statistics || {
    totalProjects: 0,
    totalCompliant: 0,
    totalPartial: 0,
    totalNonCompliant: 0,
    averageScore: 0,
  };

  const projects = data?.projects || [];
  const totalPages = data?.pagination?.totalPages || 1;

  return (
    <div className="min-h-screen w-full bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      {/* Partículas flotantes */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-indigo-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1.5 h-1.5 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1 h-1 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10 w-full max-w-full px-4 py-6 space-y-6">
        {/* Header */}
        <div>
          <div className="flex items-center gap-3 mb-2">
            <ClipboardCheck className="w-8 h-8 text-indigo-500" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-indigo-500 to-indigo-700 bg-clip-text text-transparent">
              {t("governance.compliance.qms.projects.title", "Proyectos con QMS")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t("governance.compliance.qms.projects.subtitle", "Gestión de Quality Management System por proyecto - Art. 17 EU AI Act")}
          </p>
        </div>

        {/* Estadísticas */}
        <div className="grid grid-cols-1 md:grid-cols-5 gap-3">
          <Card className="border-2">
            <CardBody className="p-3">
              <div className="text-xl font-bold">{statistics.totalProjects}</div>
              <div className="text-xs text-muted-foreground">
                {t("governance.compliance.qms.projects.totalProjects", "Total Proyectos")}
              </div>
            </CardBody>
          </Card>
          <Card className="border-2">
            <CardBody className="p-3">
              <div className="text-xl font-bold text-green-600">{statistics.totalCompliant}</div>
              <div className="text-xs text-muted-foreground">
                {t("governance.compliance.qms.projects.compliant", "Cumpliendo")}
              </div>
            </CardBody>
          </Card>
          <Card className="border-2">
            <CardBody className="p-3">
              <div className="text-xl font-bold text-yellow-600">{statistics.totalPartial}</div>
              <div className="text-xs text-muted-foreground">
                {t("governance.compliance.qms.projects.partial", "Parcial")}
              </div>
            </CardBody>
          </Card>
          <Card className="border-2">
            <CardBody className="p-3">
              <div className="text-xl font-bold text-red-600">{statistics.totalNonCompliant}</div>
              <div className="text-xs text-muted-foreground">
                {t("governance.compliance.qms.projects.nonCompliant", "No Cumpliendo")}
              </div>
            </CardBody>
          </Card>
          <Card className="border-2">
            <CardBody className="p-3">
              <div className={`text-xl font-bold ${getScoreColor(statistics.averageScore)}`}>
                {(statistics.averageScore * 100).toFixed(0)}%
              </div>
              <div className="text-xs text-muted-foreground">
                {t("governance.compliance.qms.projects.avgScore", "Score Promedio")}
              </div>
            </CardBody>
          </Card>
        </div>

        {/* Filtros y Búsqueda */}
        <Card className="border-2">
          <CardBody className="p-3">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
                <Input
                  type="text"
                  placeholder={t("governance.compliance.qms.projects.searchPlaceholder", "Buscar por nombre de proyecto...")}
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
                    {t("governance.compliance.qms.projects.allStatuses", "Todos los Estados")}
                  </option>
                  <option value="COMPLIANT">
                    {t("governance.compliance.qms.statusValues.compliant", "Cumple")}
                  </option>
                  <option value="PARTIAL">
                    {t("governance.compliance.qms.statusValues.partial", "Parcial")}
                  </option>
                  <option value="NON_COMPLIANT">
                    {t("governance.compliance.qms.statusValues.nonCompliant", "No Cumple")}
                  </option>
                </select>
              </div>
            </div>
          </CardBody>
        </Card>

        {/* Lista de Proyectos - 2 por fila */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
          {projects.length === 0 ? (
            <Card className="border-2 col-span-2">
              <CardBody className="p-8 text-center">
                <ClipboardCheck className="w-12 h-12 mx-auto mb-4 text-muted-foreground" />
                <p className="text-muted-foreground">
                  {t("governance.compliance.qms.projects.noProjects", "No se encontraron proyectos con QMS")}
                </p>
              </CardBody>
            </Card>
          ) : (
            projects.map((project) => (
              <Card key={project.projectId} className="border-2 hover:shadow-lg transition-shadow">
                <CardBody className="p-4">
                  <div className="flex gap-4">
                    {/* Contenido principal */}
                    <div className="flex-1 flex flex-col gap-3">
                      {/* Header con nombre y badge */}
                      <div className="flex items-center justify-between gap-2">
                        <h3 className="text-lg font-semibold truncate flex-1">{project.projectName}</h3>
                        {getComplianceStatusBadge(project.complianceStatus)}
                      </div>

                      {/* Descripción */}
                      {project.description && (
                        <p className="text-sm text-muted-foreground line-clamp-2">{project.description}</p>
                      )}

                      {/* Métricas */}
                      <div className="grid grid-cols-2 gap-3 mt-auto">
                        <div>
                          <div className="text-xs text-muted-foreground mb-1">
                            {t("governance.compliance.qms.projects.overallScore", "Score Overall")}
                          </div>
                          <div className={`text-xl font-bold ${getScoreColor(project.overallScore)}`}>
                            {(project.overallScore * 100).toFixed(0)}%
                          </div>
                        </div>
                        <div>
                          <div className="text-xs text-muted-foreground mb-1">
                            {t("governance.compliance.qms.projects.compliantModules", "Módulos Cumpliendo")}
                          </div>
                          <div className="text-xl font-bold text-green-600">
                            {project.compliantModules}/{project.totalModules}
                          </div>
                        </div>
                        <div>
                          <div className="text-xs text-muted-foreground mb-1">
                            {t("governance.compliance.qms.projects.gaps", "Gaps")}
                          </div>
                          <div className={`text-xl font-bold ${project.gapsCount > 0 ? "text-red-600" : "text-green-600"}`}>
                            {project.gapsCount}
                          </div>
                        </div>
                        <div>
                          <div className="text-xs text-muted-foreground mb-1">
                            {t("governance.compliance.qms.projects.lastUpdated", "Última Actualización")}
                          </div>
                          <div className="text-xs font-medium flex items-center gap-1">
                            <Calendar className="h-3 w-3" />
                            <span className="truncate">{formatDate(project.lastUpdated)}</span>
                          </div>
                        </div>
                      </div>
                    </div>

                    {/* Botones laterales derechos */}
                    <div className="flex flex-col gap-2 flex-shrink-0">
                      <Button
                        onClick={() => handleViewDashboard(project.projectId)}
                        size="sm"
                        variant="primary"
                        className="h-8 px-3 text-xs"
                      >
                        <Eye className="h-3 w-3 mr-1" />
                        {t("governance.compliance.qms.projects.viewDashboard", "Ver Dashboard")}
                      </Button>
                      {project.gapsCount > 0 && (
                        <Button
                          onClick={() => handleViewReview(project.projectId)}
                          size="sm"
                          variant="outline"
                          className="h-8 px-3 text-xs"
                        >
                          <AlertTriangle className="h-3 w-3 mr-1" />
                          {t("governance.compliance.qms.projects.reviewGaps", "Revisar Gaps")}
                        </Button>
                      )}
                    </div>
                  </div>
                </CardBody>
              </Card>
            ))
          )}
        </div>

        {/* Paginación */}
        {totalPages > 1 && (
          <Card className="border-2">
            <CardBody className="p-3">
              <div className="flex items-center justify-between">
                <div className="text-sm text-muted-foreground">
                  {t("governance.compliance.qms.projects.page", "Página")} {currentPage} {t("governance.compliance.qms.projects.of", t("common.of", "de"))} {totalPages}
                </div>
                <div className="flex gap-2">
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={handlePreviousPage}
                    disabled={currentPage === 1}
                  >
                    <ChevronLeft className="h-4 w-4" />
                    {t("common.previous", "Anterior")}
                  </Button>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={handleNextPage}
                    disabled={currentPage >= totalPages}
                  >
                    {t("common.next", "Siguiente")}
                    <ChevronRight className="h-4 w-4" />
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
