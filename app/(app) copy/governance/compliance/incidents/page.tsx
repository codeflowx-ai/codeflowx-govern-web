"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  AlertTriangle,
  Plus,
  Filter,
  X,
  RefreshCw,
  Eye,
  FileText,
  Bell,
  Search,
} from "lucide-react";
import { useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";
import {
  mockIncidents,
  mockPMMData,
  type Incident,
} from "@/app/(app)/governance/data/mockPMM";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Textarea } from "@/components/ui/textarea";

export default function IncidentsPage() {
  const { t } = useTranslation();
  const searchParams = useSearchParams();
  const projectIdFromUrl = searchParams.get("projectId");

  const [incidents, setIncidents] = useState<Incident[]>([]);
  const [loading, setLoading] = useState(true);
  const [filteredIncidents, setFilteredIncidents] = useState<Incident[]>([]);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [selectedIncident, setSelectedIncident] = useState<Incident | null>(null);

  // Filtros - inicializar projectFilter con el valor de la URL si existe
  const [searchTerm, setSearchTerm] = useState("");
  const [severityFilter, setSeverityFilter] = useState<string>("");
  const [statusFilter, setStatusFilter] = useState<string>("");
  const [projectFilter, setProjectFilter] = useState<string>(projectIdFromUrl || "");

  // Formulario nuevo incidente - inicializar projectId si viene de la URL
  const [newIncident, setNewIncident] = useState({
    projectId: projectIdFromUrl || "",
    severity: "",
    description: "",
    impact: "",
  });

  // Obtener lista de proyectos disponibles
  const availableProjects = Array.from(
    new Map(
      mockPMMData.systems.map((system) => [
        system.projectId,
        { id: system.projectId, name: system.projectName },
      ])
    ).values()
  );

  useEffect(() => {
    loadIncidents();
    // Si hay projectId en la URL, aplicarlo al filtro
    if (projectIdFromUrl) {
      setProjectFilter(projectIdFromUrl);
    }
  }, [projectIdFromUrl]);

  useEffect(() => {
    applyFilters();
  }, [incidents, searchTerm, severityFilter, statusFilter, projectFilter]);

  const loadIncidents = async () => {
    try {
      setLoading(true);
      // TODO: Reemplazar con llamada real a API
      await new Promise((resolve) => setTimeout(resolve, 500));
      setIncidents(mockIncidents);
    } catch (error) {
      console.error("Error loading incidents:", error);
    } finally {
      setLoading(false);
    }
  };

  const applyFilters = () => {
    let filtered = [...incidents];

    if (searchTerm) {
      filtered = filtered.filter(
        (incident) =>
          incident.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
          incident.projectName.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    if (severityFilter) {
      filtered = filtered.filter((incident) => incident.severity === severityFilter);
    }

    if (statusFilter) {
      filtered = filtered.filter((incident) => incident.status === statusFilter);
    }

    if (projectFilter) {
      filtered = filtered.filter(
        (incident) => incident.projectId.toString() === projectFilter
      );
    }

    setFilteredIncidents(filtered);
  };

  const handleReportIncident = async () => {
    try {
      // Validar campos requeridos
      if (!newIncident.projectId) {
        alert(t("governance.compliance.pmm.incidents.errors.projectRequired", "El proyecto es requerido"));
        return;
      }

      if (!newIncident.severity) {
        alert(t("governance.compliance.pmm.incidents.errors.severityRequired", "La severidad es requerida"));
        return;
      }

      if (!newIncident.description || !newIncident.description.trim()) {
        alert(t("governance.compliance.pmm.incidents.errors.descriptionRequired", "La descripción es requerida"));
        return;
      }

      // Obtener projectId y projectName
      const projectIdNum = parseInt(newIncident.projectId);
      const selectedProject = availableProjects.find((p) => p.id === projectIdNum);
      const projectName = selectedProject?.name || `Project ${newIncident.projectId}`;

      // TODO: Reemplazar con llamada real a API
      const incident: Incident = {
        id: incidents.length + 1,
        projectId: projectIdNum,
        projectName: projectName,
        severity: newIncident.severity as Incident["severity"],
        description: newIncident.description,
        reportedAt: new Date().toISOString(),
        authorityNotified:
          newIncident.severity === "HIGH" || newIncident.severity === "CRITICAL",
        authorityNotifiedAt:
          newIncident.severity === "HIGH" || newIncident.severity === "CRITICAL"
            ? new Date().toISOString()
            : null,
        status: "OPEN",
        rootCauseAnalysis: null,
      };

      setIncidents([incident, ...incidents]);
      setIsDialogOpen(false);
      setNewIncident({ projectId: "", severity: "", description: "", impact: "" });

      // Mostrar notificación si se notificó a autoridades
      if (incident.authorityNotified) {
        alert(t("governance.compliance.pmm.incidents.authorityNotified", "Autoridades notificadas automáticamente (Art. 20.1)"));
      }
    } catch (error) {
      console.error("Error reporting incident:", error);
      alert(t("governance.compliance.pmm.incidents.errors.submitError", "Error al reportar el incidente. Por favor, intente nuevamente."));
    }
  };

  const getSeverityColor = (severity: string) => {
    switch (severity?.toUpperCase()) {
      case "CRITICAL":
        return "bg-red-100 text-red-800 dark:bg-red-900/20 dark:text-red-300";
      case "HIGH":
        return "bg-orange-100 text-orange-800 dark:bg-orange-900/20 dark:text-orange-300";
      case "MEDIUM":
        return "bg-yellow-100 text-yellow-800 dark:bg-yellow-900/20 dark:text-yellow-300";
      case "LOW":
        return "bg-blue-100 text-blue-800 dark:bg-blue-900/20 dark:text-blue-300";
      default:
        return "bg-gray-100 text-gray-800 dark:bg-gray-900/20 dark:text-gray-300";
    }
  };

  const getStatusColor = (status: string) => {
    switch (status?.toUpperCase()) {
      case "CLOSED":
      case "RESOLVED":
        return "bg-green-100 text-green-800 dark:bg-green-900/20 dark:text-green-300";
      case "INVESTIGATING":
        return "bg-yellow-100 text-yellow-800 dark:bg-yellow-900/20 dark:text-yellow-300";
      case "OPEN":
        return "bg-blue-100 text-blue-800 dark:bg-blue-900/20 dark:text-blue-300";
      default:
        return "bg-gray-100 text-gray-800 dark:bg-gray-900/20 dark:text-gray-300";
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-orange-600"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="relative z-10">
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        {/* Header */}
        <div className="space-y-2">
          <div className="flex items-center gap-3 mb-4">
            <AlertTriangle className="w-8 h-8 text-orange-500" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-orange-500 to-orange-700 bg-clip-text text-transparent">
              {t("governance.compliance.pmm.incidents.title", "Gestión de Incidentes")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t(
              "governance.compliance.pmm.incidents.subtitle",
              "Reporte y gestión de incidentes según Art. 20 EU AI Act"
            )}
          </p>
        </div>

        {/* Acciones */}
        <div className="flex justify-between items-center">
          <Button
            className="flex items-center gap-2"
            onClick={() => setIsDialogOpen(true)}
          >
            <Plus className="w-4 h-4" />
            {t("governance.compliance.pmm.incidents.reportIncident", "Reportar Incidente")}
          </Button>
          <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
            <DialogContent className="max-w-2xl p-0">
              <Card className="border-0 shadow-none">
                <CardHeader className="relative pb-4">
                  <Button
                    variant="ghost"
                    size="sm"
                    className="absolute right-4 top-4 h-6 w-6"
                    onClick={() => setIsDialogOpen(false)}
                  >
                    <X className="h-4 w-4" />
                  </Button>
                  <CardTitle>
                    {t("governance.compliance.pmm.incidents.reportIncident", "Reportar Incidente")}
                  </CardTitle>
                  <p className="text-sm text-muted-foreground mt-2">
                    {t(
                      "governance.compliance.pmm.incidents.reportDescription",
                      "Los incidentes de severidad HIGH o CRITICAL se notificarán automáticamente a las autoridades (Art. 20.1)"
                    )}
                  </p>
                </CardHeader>
                <CardBody className="space-y-4">
                <div>
                  <Label>{t("governance.compliance.pmm.incidents.project", "Proyecto Afectado")}</Label>
                  <Select
                    value={newIncident.projectId}
                    onValueChange={(value) =>
                      setNewIncident({ ...newIncident, projectId: value })
                    }
                  >
                    <SelectTrigger>
                      {newIncident.projectId ? (
                        (() => {
                          const selectedProject = availableProjects.find(
                            (p) => p.id.toString() === newIncident.projectId
                          );
                          return (
                            <span>
                              {selectedProject
                                ? `${selectedProject.id} - ${selectedProject.name}`
                                : newIncident.projectId}
                            </span>
                          );
                        })()
                      ) : (
                        <span className="text-muted-foreground">
                          {t("governance.compliance.pmm.incidents.project", "Proyecto Afectado")}
                        </span>
                      )}
                    </SelectTrigger>
                    <SelectContent>
                      {availableProjects.map((project) => (
                        <SelectItem key={project.id} value={project.id.toString()}>
                          {project.id} - {project.name}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
                <div>
                  <Label>{t("governance.compliance.pmm.incidents.severity", "Severidad")}</Label>
                  <Select
                    value={newIncident.severity}
                    onValueChange={(value) =>
                      setNewIncident({ ...newIncident, severity: value })
                    }
                  >
                    <SelectTrigger>
                      <SelectValue placeholder={t("governance.compliance.pmm.incidents.selectSeverity", "Seleccionar severidad")} />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="LOW">
                        {t("governance.compliance.pmm.incidents.severity.LOW", "Baja")}
                      </SelectItem>
                      <SelectItem value="MEDIUM">
                        {t("governance.compliance.pmm.incidents.severity.MEDIUM", "Media")}
                      </SelectItem>
                      <SelectItem value="HIGH">
                        {t("governance.compliance.pmm.incidents.severity.HIGH", "Alta")}
                      </SelectItem>
                      <SelectItem value="CRITICAL">
                        {t("governance.compliance.pmm.incidents.severity.CRITICAL", "Crítica")}
                      </SelectItem>
                    </SelectContent>
                  </Select>
                </div>
                <div>
                  <Label>{t("governance.compliance.pmm.incidents.description", "Descripción")}</Label>
                  <Textarea
                    value={newIncident.description}
                    onChange={(e) =>
                      setNewIncident({ ...newIncident, description: e.target.value })
                    }
                    placeholder={t("governance.compliance.pmm.incidents.descriptionPlaceholder", "Describe el incidente en detalle...")}
                    rows={4}
                  />
                </div>
                <div>
                  <Label>{t("governance.compliance.pmm.incidents.impact", "Impacto Estimado")}</Label>
                  <Textarea
                    value={newIncident.impact}
                    onChange={(e) =>
                      setNewIncident({ ...newIncident, impact: e.target.value })
                    }
                    placeholder={t("governance.compliance.pmm.incidents.impactPlaceholder", "Describe el impacto estimado...")}
                    rows={2}
                  />
                </div>
                </CardBody>
                <div className="border-t px-6 py-4 flex justify-end gap-2">
                  <Button
                    type="button"
                    variant="outline"
                    onClick={() => setIsDialogOpen(false)}
                  >
                    {t("common.cancel", "Cancelar")}
                  </Button>
                  <Button
                    type="button"
                    onClick={handleReportIncident}
                  >
                    {t("governance.compliance.pmm.incidents.report", "Reportar")}
                  </Button>
                </div>
              </Card>
            </DialogContent>
          </Dialog>

          <Button onClick={loadIncidents} variant="outline" className="flex items-center gap-2">
            <RefreshCw className="w-4 h-4" />
            {t("common.refresh", "Refrescar")}
          </Button>
        </div>

        {/* Filtros */}
        <Card className="bg-background border-border/50 relative z-10">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Filter className="w-5 h-5" />
              {t("common.filters", "Filtros")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 md:grid-cols-5 gap-4">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-4 h-4" />
                <Input
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
              <select
                value={severityFilter}
                onChange={(e) => setSeverityFilter(e.target.value)}
                className="w-full px-3 py-2 border rounded-md bg-background"
              >
                <option value="">{t("common.all", "Todas")}</option>
                <option value="LOW">
                  {t("governance.compliance.pmm.incidents.severity.LOW", "Baja")}
                </option>
                <option value="MEDIUM">
                  {t("governance.compliance.pmm.incidents.severity.MEDIUM", "Media")}
                </option>
                <option value="HIGH">
                  {t("governance.compliance.pmm.incidents.severity.HIGH", "Alta")}
                </option>
                <option value="CRITICAL">
                  {t("governance.compliance.pmm.incidents.severity.CRITICAL", "Crítica")}
                </option>
              </select>
              <select
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value)}
                className="w-full px-3 py-2 border rounded-md bg-background"
              >
                <option value="">{t("common.all", "Todos")}</option>
                <option value="OPEN">
                  {t("governance.compliance.pmm.incidents.status.OPEN", "Abierto")}
                </option>
                <option value="INVESTIGATING">
                  {t("governance.compliance.pmm.incidents.status.INVESTIGATING", "Investigando")}
                </option>
                <option value="RESOLVED">
                  {t("governance.compliance.pmm.incidents.status.RESOLVED", "Resuelto")}
                </option>
                <option value="CLOSED">
                  {t("governance.compliance.pmm.incidents.status.CLOSED", "Cerrado")}
                </option>
              </select>
              {projectIdFromUrl ? (
                <div className="w-full px-3 py-2 border rounded-md bg-muted text-muted-foreground">
                  {availableProjects.find((p) => p.id.toString() === projectIdFromUrl)?.name || `Proyecto ${projectIdFromUrl}`}
                </div>
              ) : (
                <select
                  value={projectFilter}
                  onChange={(e) => setProjectFilter(e.target.value)}
                  className="w-full px-3 py-2 border rounded-md bg-background"
                >
                  <option value="">{t("common.all", "Todos")}</option>
                  {Array.from(new Set(incidents.map((i) => i.projectId))).map((projectId) => (
                    <option key={projectId} value={projectId.toString()}>
                      {incidents.find((i) => i.projectId === projectId)?.projectName}
                    </option>
                  ))}
                </select>
              )}
              <Button
                variant="outline"
                onClick={() => {
                  setSearchTerm("");
                  setSeverityFilter("");
                  setStatusFilter("");
                  setProjectFilter("");
                }}
                className="flex items-center gap-2"
              >
                <X className="w-4 h-4" />
                {t("common.clear", "Limpiar")}
              </Button>
            </div>
          </CardBody>
        </Card>

        {/* Tabla de Incidentes */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle>
              {t("governance.compliance.pmm.incidents.list", "Listado de Incidentes")} (
              {filteredIncidents.length})
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b">
                    <th className="text-left p-3">{t("common.project", "Proyecto")}</th>
                    <th className="text-left p-3">
                      {t("governance.compliance.pmm.incidents.severity", "Severidad")}
                    </th>
                    <th className="text-left p-3">
                      {t("governance.compliance.pmm.incidents.description", "Descripción")}
                    </th>
                    <th className="text-left p-3">{t("common.date", "Fecha")}</th>
                    <th className="text-left p-3">{t("common.status", "Estado")}</th>
                    <th className="text-left p-3">
                      {t("governance.compliance.pmm.incidents.notified", "Notificado")}
                    </th>
                    <th className="text-left p-3">{t("common.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredIncidents.length === 0 ? (
                    <tr>
                      <td colSpan={7} className="text-center p-8 text-muted-foreground">
                        {t("governance.compliance.pmm.incidents.noIncidents", "No se encontraron incidentes")}
                      </td>
                    </tr>
                  ) : (
                    filteredIncidents.map((incident) => (
                      <tr key={incident.id} className="border-b hover:bg-muted/50">
                        <td className="p-3 font-medium">{incident.projectName}</td>
                        <td className="p-3">
                          <Badge className={getSeverityColor(incident.severity)}>
                            {t(
                              `governance.compliance.pmm.incidents.severity.${incident.severity}`,
                              incident.severity
                            )}
                          </Badge>
                        </td>
                        <td className="p-3 max-w-md truncate">{incident.description}</td>
                        <td className="p-3">
                          {new Date(incident.reportedAt).toLocaleDateString()}
                        </td>
                        <td className="p-3">
                          <Badge className={getStatusColor(incident.status)}>
                            {t(
                              `governance.compliance.pmm.incidents.status.${incident.status}`,
                              incident.status
                            )}
                          </Badge>
                        </td>
                        <td className="p-3">
                          {incident.authorityNotified ? (
                            <div className="flex items-center gap-2">
                              <Bell className="w-4 h-4 text-green-600" />
                              <span className="text-xs text-muted-foreground">
                                {incident.authorityNotifiedAt
                                  ? new Date(incident.authorityNotifiedAt).toLocaleString()
                                  : ""}
                              </span>
                            </div>
                          ) : (
                            <span className="text-muted-foreground">-</span>
                          )}
                        </td>
                        <td className="p-3">
                          <div className="flex items-center justify-center gap-2">
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => setSelectedIncident(incident)}
                              className="h-8 w-8 p-0 flex items-center justify-center"
                              title={t("common.view", "Ver detalles")}
                            >
                              <Eye className="w-4 h-4" />
                            </Button>
                            {!incident.authorityNotified && (
                              <Button
                                variant="ghost"
                                size="sm"
                                className="h-8 w-8 p-0 flex items-center justify-center"
                                title={t("governance.compliance.pmm.incidents.notify", "Notificar autoridades")}
                              >
                                <Bell className="w-4 h-4" />
                              </Button>
                            )}
                          </div>
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>
          </CardBody>
        </Card>

        {/* Dialog Detalle Incidente */}
        {selectedIncident && (
          <Dialog open={!!selectedIncident} onOpenChange={() => setSelectedIncident(null)}>
            <DialogContent className="max-w-3xl max-h-[90vh] overflow-y-auto p-0">
              <Card className="border-0 shadow-none">
                <CardHeader className="relative pb-4">
                  <Button
                    variant="ghost"
                    size="sm"
                    className="absolute right-4 top-4 h-6 w-6"
                    onClick={() => setSelectedIncident(null)}
                  >
                    <X className="h-4 w-4" />
                  </Button>
                  <CardTitle className="flex items-center gap-2 pr-8">
                    <AlertTriangle className="w-5 h-5 text-orange-500" />
                    {t("governance.compliance.pmm.incidents.details", "Detalle del Incidente")} #
                    {selectedIncident.id}
                  </CardTitle>
                </CardHeader>
                <CardBody className="space-y-6">
                {/* Información Principal */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <Label className="text-sm font-medium text-muted-foreground">
                      {t("common.project", "Proyecto")}
                    </Label>
                    <p className="text-base font-semibold">{selectedIncident.projectName}</p>
                  </div>
                  <div className="space-y-2">
                    <Label className="text-sm font-medium text-muted-foreground">
                      {t("common.status", "Estado")}
                    </Label>
                    <div>
                      <Badge className={getStatusColor(selectedIncident.status)}>
                        {t(
                          `governance.compliance.pmm.incidents.status.${selectedIncident.status}`,
                          selectedIncident.status
                        )}
                      </Badge>
                    </div>
                  </div>
                  <div className="space-y-2">
                    <Label className="text-sm font-medium text-muted-foreground">
                      {t("governance.compliance.pmm.incidents.severity", "Severidad")}
                    </Label>
                    <div>
                      <Badge className={getSeverityColor(selectedIncident.severity)}>
                        {t(
                          `governance.compliance.pmm.incidents.severity.${selectedIncident.severity}`,
                          selectedIncident.severity
                        )}
                      </Badge>
                    </div>
                  </div>
                  <div className="space-y-2">
                    <Label className="text-sm font-medium text-muted-foreground">
                      {t("common.date", "Fecha de Reporte")}
                    </Label>
                    <p className="text-base">
                      {new Date(selectedIncident.reportedAt).toLocaleString()}
                    </p>
                  </div>
                </div>

                {/* Descripción */}
                <div className="space-y-2">
                  <Label className="text-sm font-medium text-muted-foreground">
                    {t("governance.compliance.pmm.incidents.description", "Descripción")}
                  </Label>
                  <div className="p-4 bg-muted/50 rounded-lg border">
                    <p className="text-sm leading-relaxed whitespace-pre-wrap">
                      {selectedIncident.description}
                    </p>
                  </div>
                </div>

                {/* Notificación a Autoridades */}
                {selectedIncident.authorityNotified && (
                  <div className="space-y-2">
                    <Label className="text-sm font-medium text-muted-foreground flex items-center gap-2">
                      <Bell className="w-4 h-4 text-orange-500" />
                      {t("governance.compliance.pmm.incidents.authorityNotifiedAt", "Notificado a Autoridades")}
                    </Label>
                    <div className="p-4 bg-orange-50 dark:bg-orange-950/20 rounded-lg border border-orange-200 dark:border-orange-800">
                      <p className="text-sm">
                        {selectedIncident.authorityNotifiedAt
                          ? new Date(selectedIncident.authorityNotifiedAt).toLocaleString()
                          : t("governance.compliance.pmm.incidents.notified", "Notificado")}
                      </p>
                    </div>
                  </div>
                )}

                {/* Análisis de Causa Raíz */}
                {selectedIncident.rootCauseAnalysis && (
                  <div className="space-y-2">
                    <Label className="text-sm font-medium text-muted-foreground">
                      {t("governance.compliance.pmm.incidents.rootCauseAnalysis", "Análisis de Causa Raíz")}
                    </Label>
                    <div className="p-4 bg-muted/50 rounded-lg border">
                      <p className="text-sm leading-relaxed whitespace-pre-wrap">
                        {selectedIncident.rootCauseAnalysis}
                      </p>
                    </div>
                  </div>
                )}
                </CardBody>
              </Card>
            </DialogContent>
          </Dialog>
        )}
      </div>
    </div>
  );
}
