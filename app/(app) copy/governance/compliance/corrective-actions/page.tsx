"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  ClipboardCheck,
  Plus,
  Filter,
  X,
  RefreshCw,
  Eye,
  TrendingUp,
  CheckCircle,
  Clock,
  AlertCircle,
} from "lucide-react";
import { useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";
import {
  mockCorrectiveActions,
  mockIncidents,
  type CorrectiveAction,
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

export default function CorrectiveActionsPage() {
  const { t } = useTranslation();
  const searchParams = useSearchParams();
  const projectIdFromUrl = searchParams.get("projectId");

  const [actions, setActions] = useState<CorrectiveAction[]>([]);
  const [loading, setLoading] = useState(true);
  const [filteredActions, setFilteredActions] = useState<CorrectiveAction[]>([]);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [selectedAction, setSelectedAction] = useState<CorrectiveAction | null>(null);
  const [isEffectivenessDialogOpen, setIsEffectivenessDialogOpen] = useState(false);
  const [effectivenessValue, setEffectivenessValue] = useState<string>("");

  // Filtros - filtrar por proyecto si viene de la URL
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState<string>("");
  const [incidentFilter, setIncidentFilter] = useState<string>("");

  // Formulario nueva acción
  const [newAction, setNewAction] = useState({
    incidentId: "",
    description: "",
    plannedDate: "",
  });

  useEffect(() => {
    loadActions();
  }, []);

  useEffect(() => {
    applyFilters();
  }, [actions, searchTerm, statusFilter, incidentFilter, projectIdFromUrl]);

  const loadActions = async () => {
    try {
      setLoading(true);
      // TODO: Reemplazar con llamada real a API
      await new Promise((resolve) => setTimeout(resolve, 500));
      setActions(mockCorrectiveActions);
    } catch (error) {
      console.error("Error loading actions:", error);
    } finally {
      setLoading(false);
    }
  };

  const applyFilters = () => {
    let filtered = [...actions];

    if (searchTerm) {
      filtered = filtered.filter(
        (action) =>
          action.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
          action.incidentDescription?.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    if (statusFilter) {
      filtered = filtered.filter((action) => action.status === statusFilter);
    }

    if (incidentFilter) {
      filtered = filtered.filter(
        (action) => action.incidentId.toString() === incidentFilter
      );
    }

    // Filtrar por proyecto si viene de la URL
    if (projectIdFromUrl) {
      filtered = filtered.filter(
        (action) => {
          const incident = mockIncidents.find((i) => i.id === action.incidentId);
          return incident && incident.projectId.toString() === projectIdFromUrl;
        }
      );
    }

    setFilteredActions(filtered);
  };

  const handleCreateAction = async () => {
    try {
      // TODO: Reemplazar con llamada real a API
      const incident = mockIncidents.find(
        (i) => i.id.toString() === newAction.incidentId
      );
      const action: CorrectiveAction = {
        id: actions.length + 1,
        incidentId: parseInt(newAction.incidentId),
        incidentDescription: incident?.description || "",
        description: newAction.description,
        status: "PLANNED",
        effectiveness: null,
        plannedDate: newAction.plannedDate,
        completedDate: null,
      };

      setActions([action, ...actions]);
      setIsDialogOpen(false);
      setNewAction({ incidentId: "", description: "", plannedDate: "" });
    } catch (error) {
      console.error("Error creating action:", error);
    }
  };

  const handleUpdateStatus = async (actionId: number, newStatus: CorrectiveAction["status"]) => {
    try {
      // TODO: Reemplazar con llamada real a API
      setActions(
        actions.map((action) =>
          action.id === actionId
            ? {
                ...action,
                status: newStatus,
                completedDate:
                  newStatus === "COMPLETED" ? new Date().toISOString().split("T")[0] : null,
              }
            : action
        )
      );
    } catch (error) {
      console.error("Error updating status:", error);
    }
  };

  const handleUpdateEffectiveness = async (actionId: number) => {
    try {
      // TODO: Reemplazar con llamada real a API
      const effectiveness = parseFloat(effectivenessValue);
      if (isNaN(effectiveness) || effectiveness < 0 || effectiveness > 1) {
        alert(t("governance.compliance.pmm.actions.invalidEffectiveness", "La efectividad debe estar entre 0.00 y 1.00"));
        return;
      }

      setActions(
        actions.map((action) =>
          action.id === actionId ? { ...action, effectiveness } : action
        )
      );
      setIsEffectivenessDialogOpen(false);
      setEffectivenessValue("");
      setSelectedAction(null);
    } catch (error) {
      console.error("Error updating effectiveness:", error);
    }
  };

  const getStatusColor = (status: string) => {
    switch (status?.toUpperCase()) {
      case "COMPLETED":
        return "bg-green-100 text-green-800 dark:bg-green-900/20 dark:text-green-300";
      case "IN_PROGRESS":
        return "bg-yellow-100 text-yellow-800 dark:bg-yellow-900/20 dark:text-yellow-300";
      case "PLANNED":
        return "bg-blue-100 text-blue-800 dark:bg-blue-900/20 dark:text-blue-300";
      default:
        return "bg-gray-100 text-gray-800 dark:bg-gray-900/20 dark:text-gray-300";
    }
  };

  const getStatusIcon = (status: string) => {
    switch (status?.toUpperCase()) {
      case "COMPLETED":
        return <CheckCircle className="w-4 h-4" />;
      case "IN_PROGRESS":
        return <Clock className="w-4 h-4" />;
      case "PLANNED":
        return <AlertCircle className="w-4 h-4" />;
      default:
        return null;
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
            <ClipboardCheck className="w-8 h-8 text-orange-500" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-orange-500 to-orange-700 bg-clip-text text-transparent">
              {t("governance.compliance.pmm.actions.title", "Acciones Correctoras")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t(
              "governance.compliance.pmm.actions.subtitle",
              "Gestión y seguimiento de acciones correctoras para incidentes"
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
            {t("governance.compliance.pmm.actions.create", "Crear Acción")}
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
                  <CardTitle className="pr-8">
                    {t("governance.compliance.pmm.actions.createTitle", "Crear Nueva Acción Correctora")}
                  </CardTitle>
                  <p className="text-sm text-muted-foreground mt-2">
                    {t(
                      "governance.compliance.pmm.actions.createDescription",
                      "Crea una acción correctora para resolver un incidente"
                    )}
                  </p>
                </CardHeader>
                <CardBody className="space-y-4">
                  <div>
                    <Label>{t("governance.compliance.pmm.actions.incident", "Incidente Asociado")}</Label>
                    <Select
                      value={newAction.incidentId}
                      onValueChange={(value) =>
                        setNewAction({ ...newAction, incidentId: value })
                      }
                    >
                      <SelectTrigger>
                        {newAction.incidentId ? (
                          (() => {
                            const selectedIncident = mockIncidents.find(
                              (i) => i.id.toString() === newAction.incidentId
                            );
                            return (
                              <span>
                                {selectedIncident
                                  ? `#${selectedIncident.id} - ${selectedIncident.projectName} - ${selectedIncident.description.substring(0, 50)}...`
                                  : newAction.incidentId}
                              </span>
                            );
                          })()
                        ) : (
                          <span className="text-muted-foreground">
                            {t("governance.compliance.pmm.actions.selectIncident", "Seleccionar incidente")}
                          </span>
                        )}
                      </SelectTrigger>
                      <SelectContent>
                        {mockIncidents.map((incident) => (
                          <SelectItem key={incident.id} value={incident.id.toString()}>
                            #{incident.id} - {incident.projectName} - {incident.description.substring(0, 50)}...
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>
                  <div>
                    <Label>{t("governance.compliance.pmm.actions.description", "Descripción de la Acción")}</Label>
                    <Textarea
                      value={newAction.description}
                      onChange={(e) =>
                        setNewAction({ ...newAction, description: e.target.value })
                      }
                      placeholder={t(
                        "governance.compliance.pmm.actions.descriptionPlaceholder",
                        "Describe la acción correctora a implementar..."
                      )}
                      rows={4}
                    />
                  </div>
                  <div>
                    <Label>{t("governance.compliance.pmm.actions.plannedDate", "Fecha Objetivo")}</Label>
                    <Input
                      type="date"
                      value={newAction.plannedDate}
                      onChange={(e) =>
                        setNewAction({ ...newAction, plannedDate: e.target.value })
                      }
                    />
                  </div>
                </CardBody>
                <div className="border-t px-6 py-4 flex justify-end gap-2">
                  <Button variant="outline" onClick={() => setIsDialogOpen(false)}>
                    {t("common.cancel", "Cancelar")}
                  </Button>
                  <Button onClick={handleCreateAction}>
                    {t("governance.compliance.pmm.actions.create", "Crear")}
                  </Button>
                </div>
              </Card>
            </DialogContent>
          </Dialog>

          <Button onClick={loadActions} variant="outline" className="flex items-center gap-2">
            <RefreshCw className="w-4 h-4" />
            {t("common.refresh", "Refrescar")}
          </Button>
        </div>

        {/* Filtros */}
        <Card className="bg-background border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Filter className="w-5 h-5" />
              {t("common.filters", "Filtros")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
              <Input
                placeholder={t("common.search", "Buscar...")}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
              <select
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value)}
                className="w-full px-3 py-2 border rounded-md bg-background"
              >
                <option value="">{t("common.all", "Todos")}</option>
                <option value="PLANNED">
                  {t("governance.compliance.pmm.actions.status.PLANNED", "Planificada")}
                </option>
                <option value="IN_PROGRESS">
                  {t("governance.compliance.pmm.actions.status.IN_PROGRESS", "En Progreso")}
                </option>
                <option value="COMPLETED">
                  {t("governance.compliance.pmm.actions.status.COMPLETED", "Completada")}
                </option>
              </select>
              <select
                value={incidentFilter}
                onChange={(e) => setIncidentFilter(e.target.value)}
                className="w-full px-3 py-2 border rounded-md bg-background"
              >
                <option value="">{t("governance.compliance.pmm.actions.incident", "Incidente")}</option>
                <option value="">{t("common.all", "Todos")}</option>
                {Array.from(new Set(actions.map((a) => a.incidentId))).map((incidentId) => (
                  <option key={incidentId} value={incidentId.toString()}>
                    {t("governance.compliance.pmm.actions.incident", "Incidente")} #{incidentId}
                  </option>
                ))}
              </select>
              <Button
                variant="outline"
                onClick={() => {
                  setSearchTerm("");
                  setStatusFilter("");
                  setIncidentFilter("");
                }}
                className="flex items-center gap-2"
              >
                <X className="w-4 h-4" />
                {t("common.clear", "Limpiar")}
              </Button>
            </div>
          </CardBody>
        </Card>

        {/* Tabla de Acciones */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle>
              {t("governance.compliance.pmm.actions.list", "Listado de Acciones")} (
              {filteredActions.length})
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b">
                    <th className="text-left p-3">
                      {t("governance.compliance.pmm.actions.incident", "Incidente")}
                    </th>
                    <th className="text-left p-3">
                      {t("governance.compliance.pmm.actions.description", "Descripción")}
                    </th>
                    <th className="text-left p-3">{t("common.status", "Estado")}</th>
                    <th className="text-left p-3">
                      {t("governance.compliance.pmm.actions.effectiveness", "Efectividad")}
                    </th>
                    <th className="text-left p-3">
                      {t("governance.compliance.pmm.actions.plannedDate", "Fecha Objetivo")}
                    </th>
                    <th className="text-left p-3">{t("common.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredActions.length === 0 ? (
                    <tr>
                      <td colSpan={6} className="text-center p-8 text-muted-foreground">
                        {t("governance.compliance.pmm.actions.noActions", "No se encontraron acciones")}
                      </td>
                    </tr>
                  ) : (
                    filteredActions.map((action) => (
                      <tr key={action.id} className="border-b hover:bg-muted/50">
                        <td className="p-3">
                          <div>
                            <p className="font-medium">#{action.incidentId}</p>
                            <p className="text-xs text-muted-foreground max-w-xs truncate">
                              {action.incidentDescription}
                            </p>
                          </div>
                        </td>
                        <td className="p-3 max-w-md truncate">{action.description}</td>
                        <td className="p-3">
                          <Badge className={getStatusColor(action.status)}>
                            <div className="flex items-center gap-1">
                              {getStatusIcon(action.status)}
                              {t(
                                `governance.compliance.pmm.actions.status.${action.status}`,
                                action.status
                              )}
                            </div>
                          </Badge>
                        </td>
                        <td className="p-3">
                          {action.effectiveness !== null ? (
                            <div className="flex items-center gap-2">
                              <TrendingUp className="w-4 h-4 text-green-600" />
                              <span className="font-semibold">
                                {(action.effectiveness * 100).toFixed(0)}%
                              </span>
                            </div>
                          ) : (
                            <span className="text-muted-foreground">-</span>
                          )}
                        </td>
                        <td className="p-3">
                          {action.plannedDate
                            ? new Date(action.plannedDate).toLocaleDateString()
                            : "-"}
                        </td>
                        <td className="p-3">
                          <div className="flex items-center gap-2">
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => setSelectedAction(action)}
                            >
                              <Eye className="w-4 h-4" />
                            </Button>
                            {action.status !== "COMPLETED" && (
                              <Select
                                value={action.status}
                                onValueChange={(value) =>
                                  handleUpdateStatus(action.id, value as CorrectiveAction["status"])
                                }
                              >
                                <SelectTrigger className="w-32 h-8">
                                  <SelectValue />
                                </SelectTrigger>
                                <SelectContent>
                                  <SelectItem value="PLANNED">
                                    {t("governance.compliance.pmm.actions.status.PLANNED", "Planificada")}
                                  </SelectItem>
                                  <SelectItem value="IN_PROGRESS">
                                    {t("governance.compliance.pmm.actions.status.IN_PROGRESS", "En Progreso")}
                                  </SelectItem>
                                  <SelectItem value="COMPLETED">
                                    {t("governance.compliance.pmm.actions.status.COMPLETED", "Completada")}
                                  </SelectItem>
                                </SelectContent>
                              </Select>
                            )}
                            {action.status === "COMPLETED" && action.effectiveness === null && (
                              <Button
                                variant="ghost"
                                size="sm"
                                onClick={() => {
                                  setSelectedAction(action);
                                  setIsEffectivenessDialogOpen(true);
                                }}
                              >
                                <TrendingUp className="w-4 h-4" />
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

        {/* Dialog Detalle Acción */}
        {selectedAction && !isEffectivenessDialogOpen && (
          <Dialog open={!!selectedAction} onOpenChange={() => setSelectedAction(null)}>
            <DialogContent className="max-w-3xl p-0">
              <Card className="border-0 shadow-none">
                <CardHeader className="relative pb-4">
                  <Button
                    variant="ghost"
                    size="sm"
                    className="absolute right-4 top-4 h-6 w-6"
                    onClick={() => setSelectedAction(null)}
                  >
                    <X className="h-4 w-4" />
                  </Button>
                  <CardTitle className="flex items-center gap-2 pr-8">
                    <ClipboardCheck className="w-5 h-5 text-orange-500" />
                    {t("governance.compliance.pmm.actions.details", "Detalle de la Acción")} #
                    {selectedAction.id}
                  </CardTitle>
                </CardHeader>
                <CardBody className="space-y-6">
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div className="space-y-2">
                      <Label className="text-sm font-medium text-muted-foreground">
                        {t("governance.compliance.pmm.actions.incident", "Incidente")}
                      </Label>
                      <p className="text-base font-semibold">#{selectedAction.incidentId}</p>
                      <p className="text-sm text-muted-foreground">
                        {selectedAction.incidentDescription}
                      </p>
                    </div>
                    <div className="space-y-2">
                      <Label className="text-sm font-medium text-muted-foreground">
                        {t("common.status", "Estado")}
                      </Label>
                      <div>
                        <Badge className={getStatusColor(selectedAction.status)}>
                          <div className="flex items-center gap-1">
                            {getStatusIcon(selectedAction.status)}
                            {t(
                              `governance.compliance.pmm.actions.status.${selectedAction.status}`,
                              selectedAction.status
                            )}
                          </div>
                        </Badge>
                      </div>
                    </div>
                    <div className="space-y-2">
                      <Label className="text-sm font-medium text-muted-foreground">
                        {t("governance.compliance.pmm.actions.plannedDate", "Fecha Objetivo")}
                      </Label>
                      <p className="text-base">
                        {selectedAction.plannedDate
                          ? new Date(selectedAction.plannedDate).toLocaleDateString()
                          : "-"}
                      </p>
                    </div>
                    {selectedAction.completedDate && (
                      <div className="space-y-2">
                        <Label className="text-sm font-medium text-muted-foreground">
                          {t("governance.compliance.pmm.actions.completedDate", "Fecha de Completado")}
                        </Label>
                        <p className="text-base">
                          {new Date(selectedAction.completedDate).toLocaleDateString()}
                        </p>
                      </div>
                    )}
                    {selectedAction.effectiveness !== null && (
                      <div className="space-y-2">
                        <Label className="text-sm font-medium text-muted-foreground">
                          {t("governance.compliance.pmm.actions.effectiveness", "Efectividad")}
                        </Label>
                        <div className="flex items-center gap-2">
                          <TrendingUp className="w-5 h-5 text-green-600" />
                          <span className="text-lg font-semibold">
                            {(selectedAction.effectiveness * 100).toFixed(0)}%
                          </span>
                        </div>
                      </div>
                    )}
                  </div>
                  <div className="space-y-2">
                    <Label className="text-sm font-medium text-muted-foreground">
                      {t("governance.compliance.pmm.actions.description", "Descripción")}
                    </Label>
                    <div className="p-4 bg-muted/50 rounded-lg border">
                      <p className="text-sm leading-relaxed whitespace-pre-wrap">
                        {selectedAction.description}
                      </p>
                    </div>
                  </div>
                </CardBody>
                <div className="border-t px-6 py-4 flex justify-end gap-2">
                  <Button variant="outline" onClick={() => setSelectedAction(null)}>
                    {t("common.close", "Cerrar")}
                  </Button>
                </div>
              </Card>
            </DialogContent>
          </Dialog>
        )}

        {/* Dialog Efectividad */}
        <Dialog open={isEffectivenessDialogOpen} onOpenChange={setIsEffectivenessDialogOpen}>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>
                {t("governance.compliance.pmm.actions.updateEffectiveness", "Actualizar Efectividad")}
              </DialogTitle>
              <DialogDescription>
                {t(
                  "governance.compliance.pmm.actions.effectivenessDescription",
                  "Ingresa la efectividad de la acción (0.00 - 1.00)"
                )}
              </DialogDescription>
            </DialogHeader>
            <div className="space-y-4 mt-4">
              <div>
                <Label>{t("governance.compliance.pmm.actions.effectiveness", "Efectividad")}</Label>
                <Input
                  type="number"
                  min="0"
                  max="1"
                  step="0.01"
                  value={effectivenessValue}
                  onChange={(e) => setEffectivenessValue(e.target.value)}
                  placeholder="0.00 - 1.00"
                />
              </div>
              <div className="flex justify-end gap-2">
                <Button
                  variant="outline"
                  onClick={() => {
                    setIsEffectivenessDialogOpen(false);
                    setEffectivenessValue("");
                    setSelectedAction(null);
                  }}
                >
                  {t("common.cancel", "Cancelar")}
                </Button>
                <Button
                  onClick={() => selectedAction && handleUpdateEffectiveness(selectedAction.id)}
                >
                  {t("common.save", "Guardar")}
                </Button>
              </div>
            </div>
          </DialogContent>
        </Dialog>
      </div>
    </div>
  );
}
