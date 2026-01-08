"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  List,
  Search,
  Plus,
  Edit,
  Calendar,
  User,
  Tag,
  FileText,
  CheckCircle,
  Clock,
  AlertTriangle,
  ClipboardCheck,
  ChevronLeft,
  ChevronRight,
} from "lucide-react";
import { useEffect, useState } from "react";
import {
  mockClassifiedProjects,
  type ClassifiedProject,
} from "../../../data/mockClassification";
import { mockAnnexIIICategories } from "../../../data/mockClassification";

const ITEMS_PER_PAGE = 10;

export default function ClassifiedProjectsPage() {
  const { t, language, mounted } = useTranslation();
  const [projects, setProjects] = useState<ClassifiedProject[]>([]);
  const [filteredProjects, setFilteredProjects] = useState<ClassifiedProject[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [filterCategory, setFilterCategory] = useState<string>("all");
  const [filterStatus, setFilterStatus] = useState<string>("all");
  const [currentPage, setCurrentPage] = useState(1);

  // Forzar re-render cuando cambia el idioma
  useEffect(() => {
    // Este efecto se ejecutará cuando cambie el idioma
  }, [language, mounted]);

  useEffect(() => {
    loadProjects();
  }, []);

  useEffect(() => {
    filterProjects();
    setCurrentPage(1); // Reset a la primera página cuando cambian los filtros
  }, [searchQuery, filterCategory, filterStatus, projects]);

  const loadProjects = async () => {
    try {
      setLoading(true);
      await new Promise((resolve) => setTimeout(resolve, 500));
      setProjects(mockClassifiedProjects);
      setFilteredProjects(mockClassifiedProjects);
    } catch (error) {
      console.error("Error loading classified projects:", error);
    } finally {
      setLoading(false);
    }
  };

  const filterProjects = () => {
    let filtered = [...projects];

    // Filtro por búsqueda
    if (searchQuery) {
      const query = searchQuery.toLowerCase();
      filtered = filtered.filter(
        (project) =>
          project.name.toLowerCase().includes(query) ||
          project.description.toLowerCase().includes(query) ||
          project.category.toLowerCase().includes(query) ||
          project.classifiedBy.toLowerCase().includes(query)
      );
    }

    // Filtro por categoría
    if (filterCategory !== "all") {
      filtered = filtered.filter((project) => project.category === filterCategory);
    }

    // Filtro por estado
    if (filterStatus !== "all") {
      filtered = filtered.filter((project) => project.status === filterStatus);
    }

    setFilteredProjects(filtered);
  };

  const getCategoryName = (categoryCode: string): string => {
    const category = mockAnnexIIICategories.find((cat) => cat.anncategorycode === categoryCode);
    return category ? category.anncategoryname : categoryCode;
  };

  const getStatusBadge = (status: string) => {
    switch (status) {
      case "active":
        return (
          <Badge variant="primary" className="bg-green-500 hover:bg-green-600 text-sm">
            <CheckCircle className="h-4 w-4 mr-1" />
            {t("governance.compliance.classification.projects.statusActive", "Activo")}
          </Badge>
        );
      case "pending_review":
        return (
          <Badge variant="primary" className="bg-yellow-500 hover:bg-yellow-600 text-sm">
            <Clock className="h-4 w-4 mr-1" />
            {t("governance.compliance.classification.projects.statusPending", "Pendiente Revisión")}
          </Badge>
        );
      case "archived":
        return (
          <Badge variant="secondary" className="text-sm">
            <AlertTriangle className="h-4 w-4 mr-1" />
            {t("governance.compliance.classification.projects.statusArchived", "Archivado")}
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
    });
  };


  // Paginación
  const totalPages = Math.ceil(filteredProjects.length / ITEMS_PER_PAGE);
  const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
  const endIndex = startIndex + ITEMS_PER_PAGE;
  const paginatedProjects = filteredProjects.slice(startIndex, endIndex);

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
      {/* Partículas flotantes */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 right-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute bottom-1/4 left-1/4 w-1.5 h-1.5 bg-blue-400/20 rounded-full animate-pulse delay-300" />
      </div>

      <div className="relative z-10 w-full max-w-full px-4 py-6 space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div>
            <div className="flex items-center gap-3 mb-2">
              <List className="w-8 h-8 text-blue-500" />
              <h1 className="text-3xl font-bold bg-gradient-to-r from-blue-500 to-blue-700 bg-clip-text text-transparent">
                {t("governance.compliance.classification.projects.title", "Proyectos Clasificados")}
              </h1>
            </div>
            <p className="text-muted-foreground">
              {t("governance.compliance.classification.projects.subtitle", "Listado de todos los proyectos clasificados como sistemas de alto riesgo según el Anexo III del EU AI Act")}
            </p>
          </div>
          <Button
            onClick={(e) => {
              e.preventDefault();
              e.stopPropagation();
              window.location.href = "/governance/compliance/classification";
            }}
            variant="primary"
          >
            <Plus className="h-4 w-4 mr-2" />
            {t("governance.compliance.classification.projects.new", "Nuevo Proyecto")}
          </Button>
        </div>

        {/* Estadísticas */}
        <div className="grid grid-cols-4 gap-3">
          <Card className="border-2">
            <CardBody className="p-3">
              <div className="text-xl font-bold">{projects.length}</div>
              <div className="text-xs text-muted-foreground">
                {t("governance.compliance.classification.projects.totalProjects", "Total Proyectos")}
              </div>
            </CardBody>
          </Card>
          <Card className="border-2">
            <CardBody className="p-3">
              <div className="text-xl font-bold text-green-600">
                {projects.filter((p) => p.status === "active").length}
              </div>
              <div className="text-xs text-muted-foreground">
                {t("governance.compliance.classification.projects.active", "Activos")}
              </div>
            </CardBody>
          </Card>
          <Card className="border-2">
            <CardBody className="p-3">
              <div className="text-xl font-bold text-yellow-600">
                {projects.filter((p) => p.status === "pending_review").length}
              </div>
              <div className="text-xs text-muted-foreground">
                {t("governance.compliance.classification.projects.pending", "Pendientes")}
              </div>
            </CardBody>
          </Card>
          <Card className="border-2">
            <CardBody className="p-3">
              <div className="text-xl font-bold">
                {new Set(projects.map((p) => p.category)).size}
              </div>
              <div className="text-xs text-muted-foreground">
                {t("governance.compliance.classification.projects.categories", "Categorías")}
              </div>
            </CardBody>
          </Card>
        </div>

        {/* Filtros y Búsqueda */}
        <Card className="border-2">
          <CardBody className="p-3">
            <div className="grid grid-cols-3 gap-3">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
                <Input
                  type="text"
                  placeholder={t("governance.compliance.classification.projects.searchPlaceholder", "Buscar por nombre, descripción, categoría...")}
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="pl-10"
                />
              </div>
              <div>
                <select
                  value={filterCategory}
                  onChange={(e) => setFilterCategory(e.target.value)}
                  className="w-full px-3 py-2 border rounded-md bg-background"
                >
                  <option value="all">
                    {t("governance.compliance.classification.projects.allCategories", "Todas las Categorías")}
                  </option>
                  {mockAnnexIIICategories.map((cat) => (
                    <option key={cat.anncategorycode} value={cat.anncategorycode}>
                      {cat.anncategorycode} - {cat.anncategoryname}
                    </option>
                  ))}
                </select>
              </div>
              <div>
                <select
                  value={filterStatus}
                  onChange={(e) => setFilterStatus(e.target.value)}
                  className="w-full px-3 py-2 border rounded-md bg-background"
                >
                  <option value="all">
                    {t("governance.compliance.classification.projects.allStatuses", "Todos los Estados")}
                  </option>
                  <option value="active">
                    {t("governance.compliance.classification.projects.statusActive", "Activo")}
                  </option>
                  <option value="pending_review">
                    {t("governance.compliance.classification.projects.statusPending", "Pendiente Revisión")}
                  </option>
                  <option value="archived">
                    {t("governance.compliance.classification.projects.statusArchived", "Archivado")}
                  </option>
                </select>
              </div>
            </div>
          </CardBody>
        </Card>

        {/* Tabla de Proyectos */}
        <Card className="border-2">
          <CardBody className="p-0">
            <div className="overflow-x-auto max-h-[calc(100vh-24rem)] overflow-y-auto">
              <table className="w-full">
                <thead className="bg-muted/50 sticky top-0 z-10">
                  <tr>
                    <th className="px-4 py-3 text-left text-sm font-semibold text-muted-foreground uppercase border-b">
                      {t("governance.compliance.classification.projects.table.id", "ID")}
                    </th>
                    <th className="px-4 py-3 text-left text-sm font-semibold text-muted-foreground uppercase border-b">
                      {t("governance.compliance.classification.projects.table.name", "Nombre")}
                    </th>
                    <th className="px-4 py-3 text-left text-sm font-semibold text-muted-foreground uppercase border-b">
                      {t("governance.compliance.classification.projects.table.category", "Categoría")}
                    </th>
                    <th className="px-4 py-3 text-left text-sm font-semibold text-muted-foreground uppercase border-b">
                      {t("governance.compliance.classification.projects.table.status", "Estado")}
                    </th>
                    <th className="px-4 py-3 text-left text-sm font-semibold text-muted-foreground uppercase border-b">
                      {t("governance.compliance.classification.projects.table.classifiedDate", "Fecha Clasificación")}
                    </th>
                    <th className="px-4 py-3 text-left text-sm font-semibold text-muted-foreground uppercase border-b">
                      {t("governance.compliance.classification.projects.table.classifiedBy", "Clasificado Por")}
                    </th>
                    <th className="px-4 py-3 text-left text-sm font-semibold text-muted-foreground uppercase border-b">
                      {t("governance.compliance.classification.projects.table.models", "Modelos")}
                    </th>
                    <th className="px-4 py-3 text-right text-sm font-semibold text-muted-foreground uppercase border-b">
                      {t("governance.compliance.classification.projects.table.actions", "Acciones")}
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {paginatedProjects.length === 0 ? (
                    <tr>
                      <td colSpan={8} className="px-4 py-6 text-center text-muted-foreground">
                        <List className="h-8 w-8 mx-auto mb-2 opacity-50" />
                        <p className="text-base">
                          {t("governance.compliance.classification.projects.noResults", "No se encontraron proyectos que coincidan con los filtros.")}
                        </p>
                      </td>
                    </tr>
                  ) : (
                    paginatedProjects.map((project) => (
                      <tr key={project.id} className="border-b hover:bg-muted/30 transition-colors">
                        <td className="px-4 py-3 text-sm font-mono">
                          {project.id}
                        </td>
                        <td className="px-4 py-3">
                          <div className="font-medium text-sm">{project.name}</div>
                          <div className="text-sm text-muted-foreground line-clamp-1 mt-1">
                            {project.description}
                          </div>
                        </td>
                        <td className="px-4 py-3">
                          <div className="flex items-center gap-2">
                            <Badge variant="outline" className="font-mono text-sm">
                              {project.category}
                            </Badge>
                            <span className="text-sm text-muted-foreground line-clamp-1 max-w-[150px]">
                              {getCategoryName(project.category)}
                            </span>
                          </div>
                        </td>
                        <td className="px-4 py-3">
                          {getStatusBadge(project.status)}
                        </td>
                        <td className="px-4 py-3 text-sm text-muted-foreground">
                          {formatDate(project.classifiedDate)}
                        </td>
                        <td className="px-4 py-3 text-sm text-muted-foreground">
                          {project.classifiedBy}
                        </td>
                        <td className="px-4 py-3 text-sm text-muted-foreground">
                          {project.modelsCount}
                        </td>
                        <td className="px-4 py-3">
                          <div className="flex items-center justify-end gap-1">
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={(e) => {
                                e.preventDefault();
                                e.stopPropagation();
                                window.location.href = `/governance/compliance/incidents?projectId=${project.id}`;
                              }}
                              className="h-8 w-8 p-0 rounded hover:bg-accent/50"
                              title={t("governance.compliance.pmm.actions.viewIncidents", "Ver Incidentes")}
                            >
                              <AlertTriangle className="h-4 w-4" />
                            </Button>
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={(e) => {
                                e.preventDefault();
                                e.stopPropagation();
                                window.location.href = `/governance/compliance/corrective-actions?projectId=${project.id}`;
                              }}
                              className="h-8 w-8 p-0 rounded hover:bg-accent/50"
                              title={t("governance.compliance.pmm.actions.viewActions", "Ver Acciones Correctoras")}
                            >
                              <ClipboardCheck className="h-4 w-4" />
                            </Button>
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={(e) => {
                                e.preventDefault();
                                e.stopPropagation();
                                window.location.href = `/governance/compliance/post-market-monitoring/plans?projectId=${project.id}`;
                              }}
                              className="h-8 w-8 p-0 rounded hover:bg-accent/50"
                              title={t("governance.compliance.pmm.actions.viewPlans", "Ver Planes PMM")}
                            >
                              <FileText className="h-4 w-4" />
                            </Button>
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={(e) => {
                                e.preventDefault();
                                e.stopPropagation();
                                window.location.href = `/governance/compliance/post-market-monitoring/reports?projectId=${project.id}`;
                              }}
                              className="h-8 w-8 p-0 rounded hover:bg-accent/50"
                              title={t("governance.compliance.pmm.actions.viewReports", "Ver Reportes")}
                            >
                              <FileText className="h-4 w-4" />
                            </Button>
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={(e) => {
                                e.preventDefault();
                                e.stopPropagation();
                                window.location.href = `/governance/compliance/classification?projectId=${project.id}`;
                              }}
                              className="h-8 w-8 p-0 rounded hover:bg-accent/50"
                              title={t("common.edit", "Editar")}
                            >
                              <Edit className="h-4 w-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>

            {/* Paginación */}
            {filteredProjects.length > 0 && (
              <div className="border-t px-4 py-3 flex items-center justify-between">
                <div className="text-sm text-muted-foreground">
                  {t("governance.compliance.classification.projects.table.showing", "Mostrando")}{" "}
                  {startIndex + 1} - {Math.min(endIndex, filteredProjects.length)}{" "}
                  {t("governance.compliance.classification.projects.table.of", "de")}{" "}
                  {filteredProjects.length}
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
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}
