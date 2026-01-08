"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Award,
  Search,
  Eye,
  CheckCircle,
  Clock,
  FileText,
  Calendar,
  ChevronLeft,
  ChevronRight,
  ExternalLink,
  FolderOpen,
} from "lucide-react";
import { useEffect, useState } from "react";

interface ProjectDeclarationSummary {
  projectId: number;
  projectName: string;
  description?: string;
  totalDeclarations: number;
  signedDeclarations: number;
  draftDeclarations: number;
  latestDeclarationDate?: string;
  latestDeclarationStatus?: "DRAFT" | "SIGNED";
  latestDeclarationVersion?: string;
  hasDeclarations: boolean;
}

interface ProjectsDeclarationsResponse {
  projects: ProjectDeclarationSummary[];
  statistics: {
    totalProjects: number;
    projectsWithDeclarations: number;
    totalDeclarations: number;
    totalSigned: number;
    totalDrafts: number;
  };
  pagination?: {
    page: number;
    size: number;
    totalPages: number;
    totalElements: number;
  };
}

const ITEMS_PER_PAGE = 12;

export default function ConformityDeclarationProjectsPage() {
  const { t, language } = useTranslation();
  const [data, setData] = useState<ProjectsDeclarationsResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [filterHasDeclarations, setFilterHasDeclarations] = useState<string>("all");
  const [currentPage, setCurrentPage] = useState(1);

  useEffect(() => {
    loadProjects();
  }, [currentPage, filterHasDeclarations, searchQuery]);

  const loadProjects = async () => {
    try {
      setLoading(true);

      const params = new URLSearchParams();
      params.append("page", String(currentPage - 1));
      params.append("size", String(ITEMS_PER_PAGE));
      if (filterHasDeclarations !== "all") {
        params.append("hasDeclarations", filterHasDeclarations);
      }
      if (searchQuery) {
        params.append("search", searchQuery);
      }

      const response = await fetch(`/api/governance/compliance/conformity-declaration/projects?${params.toString()}`);

      if (!response.ok) {
        throw new Error("Failed to load projects");
      }

      const result = await response.json();
      if (result.success) {
        setData(result.data);
      } else {
        throw new Error(result.error || "Failed to load projects");
      }
    } catch (error) {
      console.error("Error loading projects:", error);
      // Fallback a datos mock
      setData({
        projects: [
          {
            projectId: 1,
            projectName: "Healthcare Diagnostics AI",
            totalDeclarations: 2,
            signedDeclarations: 1,
            draftDeclarations: 1,
            latestDeclarationDate: "2025-01-15",
            latestDeclarationStatus: "SIGNED",
            latestDeclarationVersion: "v1.2",
            hasDeclarations: true,
          },
          {
            projectId: 2,
            projectName: "Financial Fraud Detection",
            totalDeclarations: 1,
            signedDeclarations: 0,
            draftDeclarations: 1,
            latestDeclarationDate: "2025-01-14",
            latestDeclarationStatus: "DRAFT",
            latestDeclarationVersion: "v1.0",
            hasDeclarations: true,
          },
          {
            projectId: 3,
            projectName: "Manufacturing Quality Control",
            totalDeclarations: 0,
            signedDeclarations: 0,
            draftDeclarations: 0,
            hasDeclarations: false,
          },
        ],
        statistics: {
          totalProjects: 3,
          projectsWithDeclarations: 2,
          totalDeclarations: 3,
          totalSigned: 1,
          totalDrafts: 2,
        },
      });
    } finally {
      setLoading(false);
    }
  };

  const getStatusBadge = (status?: "DRAFT" | "SIGNED") => {
    if (!status) return null;

    switch (status) {
      case "SIGNED":
        return (
          <Badge className="bg-green-100 text-green-800 dark:bg-green-900/20 dark:text-green-300">
            <CheckCircle className="h-3 w-3 mr-1" />
            {t("governance.compliance.conformity.conformityDeclaration.signedStatus", "FIRMADA")}
          </Badge>
        );
      case "DRAFT":
        return (
          <Badge className="bg-yellow-100 text-yellow-800 dark:bg-yellow-900/20 dark:text-yellow-300">
            <Clock className="h-3 w-3 mr-1" />
            {t("governance.compliance.conformity.conformityDeclaration.draftStatus", "BORRADOR")}
          </Badge>
        );
      default:
        return null;
    }
  };

  const formatDate = (dateString?: string): string => {
    if (!dateString) return "-";
    const date = new Date(dateString);
    const localeMap: Record<string, string> = {
      es: "es-ES",
      en: "en-US",
      fr: "fr-FR",
      de: "de-DE",
      it: "it-IT",
      pt: "pt-PT",
    };
    return date.toLocaleDateString(localeMap[language] || "es-ES", {
      year: "numeric",
      month: "short",
      day: "numeric",
    });
  };

  const handleViewProject = (projectId: number) => {
    window.location.href = `/governance/compliance/conformity-declaration-manager?projectId=${projectId}`;
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10 w-full max-w-full px-4 py-6 space-y-6">
        {/* Header */}
        <div className="space-y-4">
          <div className="flex items-center justify-between gap-4">
            <div className="flex items-center gap-3">
              <div className="flex items-center gap-3">
                <Award className="w-8 h-8 text-indigo-500" />
                <div>
                  <h1 className="text-3xl font-bold text-foreground">
                    {t(
                      "governance.compliance.conformity.conformityDeclaration.projects.title",
                      "Proyectos con Declaraciones de Conformidad"
                    )}
                  </h1>
                  <p className="text-muted-foreground text-sm mt-1">
                    {t(
                      "governance.compliance.conformity.conformityDeclaration.projects.subtitle",
                      "Gestión de declaraciones de conformidad por proyecto"
                    )}
                  </p>
                </div>
              </div>
            </div>
          </div>

          {/* Estadísticas */}
          {data && (
            <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
              <Card className="backdrop-blur-md bg-background/60 border-border/50">
                <CardBody className="p-4">
                  <div className="text-sm text-muted-foreground">
                    {t("governance.compliance.conformity.conformityDeclaration.projects.totalProjects", "Total Proyectos")}
                  </div>
                  <div className="text-2xl font-bold">{data.statistics.totalProjects}</div>
                </CardBody>
              </Card>
              <Card className="backdrop-blur-md bg-background/60 border-border/50">
                <CardBody className="p-4">
                  <div className="text-sm text-muted-foreground">
                    {t("governance.compliance.conformity.conformityDeclaration.projects.withDeclarations", "Con Declaraciones")}
                  </div>
                  <div className="text-2xl font-bold text-indigo-600">{data.statistics.projectsWithDeclarations}</div>
                </CardBody>
              </Card>
              <Card className="backdrop-blur-md bg-background/60 border-border/50">
                <CardBody className="p-4">
                  <div className="text-sm text-muted-foreground">
                    {t("governance.compliance.conformity.conformityDeclaration.projects.totalDeclarations", "Total Declaraciones")}
                  </div>
                  <div className="text-2xl font-bold">{data.statistics.totalDeclarations}</div>
                </CardBody>
              </Card>
              <Card className="backdrop-blur-md bg-background/60 border-border/50">
                <CardBody className="p-4">
                  <div className="text-sm text-muted-foreground">
                    {t("governance.compliance.conformity.conformityDeclaration.projects.signedDeclarations", "Firmadas")}
                  </div>
                  <div className="text-2xl font-bold text-green-600">{data.statistics.totalSigned}</div>
                </CardBody>
              </Card>
            </div>
          )}
        </div>

        {/* Filtros y Búsqueda */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardBody className="p-4">
            <div className="flex flex-col md:flex-row gap-4">
              <div className="flex-1">
                <div className="relative">
                  <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
                  <Input
                    placeholder={t("governance.compliance.conformity.conformityDeclaration.projects.searchPlaceholder", "Buscar por nombre de proyecto...")}
                    value={searchQuery}
                    onChange={(e) => {
                      setSearchQuery(e.target.value);
                      setCurrentPage(1);
                    }}
                    className="pl-10"
                  />
                </div>
              </div>
              <div className="flex gap-2">
                <select
                  value={filterHasDeclarations}
                  onChange={(e) => {
                    setFilterHasDeclarations(e.target.value);
                    setCurrentPage(1);
                  }}
                  className="px-3 py-2 border rounded-md bg-background"
                >
                  <option value="all">
                    {t("governance.compliance.conformity.conformityDeclaration.projects.allProjects", "Todos los Proyectos")}
                  </option>
                  <option value="true">
                    {t("governance.compliance.conformity.conformityDeclaration.projects.withDeclarations", "Con Declaraciones")}
                  </option>
                  <option value="false">
                    {t("governance.compliance.conformity.conformityDeclaration.projects.withoutDeclarations", "Sin Declaraciones")}
                  </option>
                </select>
              </div>
            </div>
          </CardBody>
        </Card>

        {/* Lista de Proyectos */}
        {data && data.projects.length === 0 ? (
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardBody className="p-8 text-center">
              <FolderOpen className="w-12 h-12 mx-auto mb-4 text-muted-foreground" />
              <p className="text-muted-foreground">
                {t("governance.compliance.conformity.conformityDeclaration.projects.noProjects", "No se encontraron proyectos")}
              </p>
            </CardBody>
          </Card>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {data?.projects.map((project) => (
              <Card
                key={project.projectId}
                className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all duration-300"
              >
                <CardHeader>
                  <div className="flex items-start justify-between">
                    <div className="flex-1">
                      <CardTitle className="text-lg mb-2">{project.projectName}</CardTitle>
                      {project.description && (
                        <p className="text-sm text-muted-foreground line-clamp-2">{project.description}</p>
                      )}
                    </div>
                    <Badge variant="outline" className="ml-2">
                      #{project.projectId}
                    </Badge>
                  </div>
                </CardHeader>
                <CardBody className="space-y-4">
                  {/* Información de Declaraciones */}
                  <div className="space-y-2">
                    <div className="flex items-center justify-between text-sm">
                      <span className="text-muted-foreground">
                        {t("governance.compliance.conformity.conformityDeclaration.projects.totalDeclarations", "Total Declaraciones")}
                      </span>
                      <span className="font-semibold">{project.totalDeclarations}</span>
                    </div>
                    {project.hasDeclarations && (
                      <>
                        <div className="flex items-center justify-between text-sm">
                          <span className="text-muted-foreground">
                            {t("governance.compliance.conformity.conformityDeclaration.signed", "Firmadas")}
                          </span>
                          <span className="font-semibold text-green-600">{project.signedDeclarations}</span>
                        </div>
                        <div className="flex items-center justify-between text-sm">
                          <span className="text-muted-foreground">
                            {t("governance.compliance.conformity.conformityDeclaration.drafts", "Borradores")}
                          </span>
                          <span className="font-semibold text-yellow-600">{project.draftDeclarations}</span>
                        </div>
                        {project.latestDeclarationDate && (
                          <div className="pt-2 border-t space-y-2">
                            <div className="flex items-center justify-between text-xs">
                              <span className="text-muted-foreground">
                                {t("governance.compliance.conformity.conformityDeclaration.projects.latestDeclaration", "Última Declaración")}
                              </span>
                              {getStatusBadge(project.latestDeclarationStatus)}
                            </div>
                            <div className="flex items-center justify-between text-xs">
                              <span className="text-muted-foreground">
                                {t("governance.compliance.conformity.conformityDeclaration.date", "Fecha")}
                              </span>
                              <span>{formatDate(project.latestDeclarationDate)}</span>
                            </div>
                            {project.latestDeclarationVersion && (
                              <div className="flex items-center justify-between text-xs">
                                <span className="text-muted-foreground">
                                  {t("governance.compliance.conformity.conformityDeclaration.version", "Versión")}
                                </span>
                                <span>{project.latestDeclarationVersion}</span>
                              </div>
                            )}
                          </div>
                        )}
                      </>
                    )}
                    {!project.hasDeclarations && (
                      <div className="pt-2 border-t">
                        <p className="text-xs text-muted-foreground text-center">
                          {t("governance.compliance.conformity.conformityDeclaration.projects.noDeclarations", "Sin declaraciones")}
                        </p>
                      </div>
                    )}
                  </div>

                  {/* Acciones */}
                  <div className="pt-2">
                    <Button
                      onClick={() => handleViewProject(project.projectId)}
                      className="w-full"
                      variant={project.hasDeclarations ? "primary" : "outline"}
                    >
                      <FileText className="h-4 w-4 mr-2" />
                      {project.hasDeclarations
                        ? t("governance.compliance.conformity.conformityDeclaration.projects.manageDeclarations", "Gestionar Declaraciones")
                        : t("governance.compliance.conformity.conformityDeclaration.projects.createDeclaration", "Crear Declaración")}
                    </Button>
                  </div>
                </CardBody>
              </Card>
            ))}
          </div>
        )}

        {/* Paginación */}
        {data && data.pagination && data.pagination.totalPages > 1 && (
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardBody className="p-4">
              <div className="flex items-center justify-between">
                <div className="text-sm text-muted-foreground">
                  {t("governance.compliance.conformity.conformityDeclaration.projects.page", "Página")} {data.pagination.page + 1} {t("common.of", "de")} {data.pagination.totalPages}
                </div>
                <div className="flex gap-2">
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => setCurrentPage((p) => Math.max(1, p - 1))}
                    disabled={currentPage === 1}
                  >
                    <ChevronLeft className="h-4 w-4" />
                  </Button>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => setCurrentPage((p) => Math.min(data.pagination!.totalPages, p + 1))}
                    disabled={currentPage === data.pagination!.totalPages}
                  >
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
