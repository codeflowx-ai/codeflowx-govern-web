"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import {
  AlertTriangle,
  Plus,
  Edit,
  Trash2,
  X,
  CheckCircle,
  XCircle,
  Activity,
} from "lucide-react";
import { useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";
import { mockProjects } from "@/app/(app)/governance/data/mockPMM";

interface AlertThreshold {
  idxalertthreshold: number;
  iduuid: string;
  idxproject: number;
  projectName?: string;
  idxmodel?: number;
  modelName?: string;
  altmetricname: string;
  altmetrictype: string;
  altwarningthreshold: number;
  altcriticalthreshold: number;
  altseverity: string;
  altstatus: string;
  altcreatedat: string;
  altupdatedat?: string;
}

const USE_MOCK_DATA = process.env.NEXT_PUBLIC_USE_MOCK_DATA === "true";

// Mock data para thresholds
const mockThresholds: AlertThreshold[] = [
  {
    idxalertthreshold: 1,
    iduuid: "uuid-1",
    idxproject: 1,
    projectName: "AI Credit Scoring System",
    altmetricname: "ACCURACY",
    altmetrictype: "PERCENTAGE",
    altwarningthreshold: 0.05,
    altcriticalthreshold: 0.10,
    altseverity: "HIGH",
    altstatus: "ACTIVE",
    altcreatedat: "2025-11-01T10:00:00Z",
  },
  {
    idxalertthreshold: 2,
    iduuid: "uuid-2",
    idxproject: 1,
    projectName: "AI Credit Scoring System",
    altmetricname: "LATENCY",
    altmetrictype: "ABSOLUTE",
    altwarningthreshold: 200,
    altcriticalthreshold: 500,
    altseverity: "MEDIUM",
    altstatus: "ACTIVE",
    altcreatedat: "2025-11-01T10:00:00Z",
  },
  {
    idxalertthreshold: 3,
    iduuid: "uuid-3",
    idxproject: 2,
    projectName: "Facial Recognition System",
    altmetricname: "DRIFT",
    altmetrictype: "PERCENTAGE",
    altwarningthreshold: 0.03,
    altcriticalthreshold: 0.05,
    altseverity: "CRITICAL",
    altstatus: "ACTIVE",
    altcreatedat: "2025-11-02T10:00:00Z",
  },
  {
    idxalertthreshold: 4,
    iduuid: "uuid-4",
    idxproject: 3,
    projectName: "Healthcare Diagnostics AI System",
    altmetricname: "BIAS",
    altmetrictype: "PERCENTAGE",
    altwarningthreshold: 0.02,
    altcriticalthreshold: 0.05,
    altseverity: "HIGH",
    altstatus: "INACTIVE",
    altcreatedat: "2025-11-03T10:00:00Z",
  },
];

export default function PMMThresholdsPage() {
  const { t } = useTranslation();
  const searchParams = useSearchParams();
  const projectIdFromUrl = searchParams.get("projectId");

  const [thresholds, setThresholds] = useState<AlertThreshold[]>([]);
  const [availableProjects, setAvailableProjects] = useState<Array<{ id: number; name: string }>>([]);
  const [loading, setLoading] = useState(true);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingThreshold, setEditingThreshold] = useState<AlertThreshold | null>(null);
  const [projectFilter, setProjectFilter] = useState<string>(projectIdFromUrl || "");
  const [statusFilter, setStatusFilter] = useState<string>("");
  const [metricFilter, setMetricFilter] = useState<string>("");

  const [newThreshold, setNewThreshold] = useState<Partial<AlertThreshold>>({
    idxproject: projectIdFromUrl ? parseInt(projectIdFromUrl) : 0,
    altmetricname: "",
    altmetrictype: "PERCENTAGE",
    altwarningthreshold: 0,
    altcriticalthreshold: 0,
    altseverity: "MEDIUM",
    altstatus: "ACTIVE",
  });

  useEffect(() => {
    loadProjects();
    loadThresholds();
  }, []);

  useEffect(() => {
    if (projectIdFromUrl) {
      setProjectFilter(projectIdFromUrl);
      setNewThreshold((prev) => ({
        ...prev,
        idxproject: parseInt(projectIdFromUrl),
      }));
    }
  }, [projectIdFromUrl]);

  useEffect(() => {
    loadThresholds();
  }, [projectFilter, statusFilter, metricFilter]);

  const loadProjects = async () => {
    try {
      if (USE_MOCK_DATA) {
        setAvailableProjects(mockProjects);
      } else {
        const response = await fetch("/api/v1/projects");
        if (response.ok) {
          const data = await response.json();
          setAvailableProjects(data);
        }
      }
    } catch (error) {
      console.error("Error loading projects:", error);
    }
  };

  const loadThresholds = async () => {
    try {
      setLoading(true);
      if (USE_MOCK_DATA) {
        let filtered = [...mockThresholds];
        if (projectFilter) {
          filtered = filtered.filter((t) => t.idxproject.toString() === projectFilter);
        }
        if (statusFilter) {
          filtered = filtered.filter((t) => t.altstatus === statusFilter);
        }
        if (metricFilter) {
          filtered = filtered.filter((t) => t.altmetricname === metricFilter);
        }
        setThresholds(filtered);
      } else {
        const params = new URLSearchParams();
        if (projectFilter) params.append("projectId", projectFilter);
        if (statusFilter) params.append("status", statusFilter);
        if (metricFilter) params.append("metric", metricFilter);

        const response = await fetch(`/api/v1/pmm/thresholds?${params.toString()}`);
        if (response.ok) {
          const data = await response.json();
          setThresholds(data);
        }
      }
    } catch (error) {
      console.error("Error loading thresholds:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateThreshold = async () => {
    try {
      if (USE_MOCK_DATA) {
        const newId = Math.max(...mockThresholds.map((t) => t.idxalertthreshold), 0) + 1;
        const created: AlertThreshold = {
          ...newThreshold,
          idxalertthreshold: newId,
          iduuid: `uuid-${newId}`,
          projectName: availableProjects.find((p) => p.id === newThreshold.idxproject)?.name,
          altcreatedat: new Date().toISOString(),
        } as AlertThreshold;
        setThresholds([...thresholds, created]);
      } else {
        const response = await fetch("/api/v1/pmm/thresholds", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(newThreshold),
        });
        if (response.ok) {
          await loadThresholds();
        }
      }
      setIsDialogOpen(false);
      resetForm();
    } catch (error) {
      console.error("Error creating threshold:", error);
    }
  };

  const handleUpdateThreshold = async () => {
    if (!editingThreshold) return;

    try {
      if (USE_MOCK_DATA) {
        setThresholds(
          thresholds.map((t) =>
            t.idxalertthreshold === editingThreshold.idxalertthreshold
              ? { ...editingThreshold, altupdatedat: new Date().toISOString() }
              : t
          )
        );
      } else {
        const response = await fetch(`/api/v1/pmm/thresholds/${editingThreshold.idxalertthreshold}`, {
          method: "PATCH",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(editingThreshold),
        });
        if (response.ok) {
          await loadThresholds();
        }
      }
      setIsDialogOpen(false);
      setEditingThreshold(null);
      resetForm();
    } catch (error) {
      console.error("Error updating threshold:", error);
    }
  };

  const handleDeleteThreshold = async (id: number) => {
    if (!confirm(t("governance.compliance.pmm.thresholds.confirmDelete", "¿Eliminar este threshold?"))) {
      return;
    }

    try {
      if (USE_MOCK_DATA) {
        setThresholds(thresholds.filter((t) => t.idxalertthreshold !== id));
      } else {
        const response = await fetch(`/api/v1/pmm/thresholds/${id}`, {
          method: "DELETE",
        });
        if (response.ok) {
          await loadThresholds();
        }
      }
    } catch (error) {
      console.error("Error deleting threshold:", error);
    }
  };

  const handleToggleStatus = async (threshold: AlertThreshold) => {
    try {
      const newStatus = threshold.altstatus === "ACTIVE" ? "INACTIVE" : "ACTIVE";
      if (USE_MOCK_DATA) {
        setThresholds(
          thresholds.map((t) =>
            t.idxalertthreshold === threshold.idxalertthreshold
              ? { ...t, altstatus: newStatus, altupdatedat: new Date().toISOString() }
              : t
          )
        );
      } else {
        if (newStatus === "ACTIVE") {
          await fetch(`/api/v1/pmm/thresholds/${threshold.idxalertthreshold}/activate`, {
            method: "POST",
          });
        } else {
          await fetch(`/api/v1/pmm/thresholds/${threshold.idxalertthreshold}/deactivate`, {
            method: "POST",
          });
        }
        await loadThresholds();
      }
    } catch (error) {
      console.error("Error toggling status:", error);
    }
  };

  const resetForm = () => {
    setNewThreshold({
      idxproject: projectIdFromUrl ? parseInt(projectIdFromUrl) : 0,
      altmetricname: "",
      altmetrictype: "PERCENTAGE",
      altwarningthreshold: 0,
      altcriticalthreshold: 0,
      altseverity: "MEDIUM",
      altstatus: "ACTIVE",
    });
    setEditingThreshold(null);
  };

  const openEditDialog = (threshold: AlertThreshold) => {
    setEditingThreshold(threshold);
    setIsDialogOpen(true);
  };

  const openCreateDialog = () => {
    resetForm();
    setIsDialogOpen(true);
  };

  const getSeverityColor = (severity: string) => {
    switch (severity) {
      case "CRITICAL":
        return "bg-red-500";
      case "HIGH":
        return "bg-orange-500";
      case "MEDIUM":
        return "bg-yellow-500";
      case "LOW":
        return "bg-blue-500";
      default:
        return "bg-gray-500";
    }
  };

  const getStatusBadge = (status: string) => {
    return status === "ACTIVE" ? (
      <Badge className="bg-green-500">
        <CheckCircle className="w-3 h-3 mr-1" />
        {t("governance.compliance.pmm.thresholds.status.active", "Activo")}
      </Badge>
    ) : (
      <Badge variant="secondary">
        <XCircle className="w-3 h-3 mr-1" />
        {t("governance.compliance.pmm.thresholds.status.inactive", "Inactivo")}
      </Badge>
    );
  };

  const filteredThresholds = thresholds.filter((t) => {
    if (projectFilter && t.idxproject.toString() !== projectFilter) return false;
    if (statusFilter && t.altstatus !== statusFilter) return false;
    if (metricFilter && t.altmetricname !== metricFilter) return false;
    return true;
  });

  return (
    <div className="w-full space-y-6 p-6">
      {/* Header */}
      <div className="flex items-start justify-between">
        <div>
          <h1 className="text-3xl font-bold">
            {t("governance.compliance.pmm.thresholds.title", "Configuración de Thresholds de Alertas")}
          </h1>
          <p className="text-muted-foreground mt-2">
            {t(
              "governance.compliance.pmm.thresholds.subtitle",
              "Configura umbrales de alerta para métricas de monitoreo post-mercado"
            )}
          </p>
        </div>
        <Button onClick={openCreateDialog}>
          <Plus className="w-4 h-4 mr-2" />
          {t("governance.compliance.pmm.thresholds.create", "Crear Threshold")}
        </Button>
      </div>

      {/* Filters */}
      <Card>
        <CardHeader>
          <CardTitle>{t("governance.compliance.pmm.thresholds.filters", "Filtros")}</CardTitle>
        </CardHeader>
        <CardBody>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <Label>{t("governance.compliance.pmm.thresholds.project", "Proyecto")}</Label>
              {projectIdFromUrl ? (
                <div className="mt-2 p-2 bg-muted rounded-md">
                  {availableProjects.find((p) => p.id.toString() === projectIdFromUrl)?.name ||
                    projectIdFromUrl}
                </div>
              ) : (
                <select
                  value={projectFilter}
                  onChange={(e) => setProjectFilter(e.target.value)}
                  className="w-full mt-2 p-2 border rounded-md"
                >
                  <option value="">{t("common.all", "Todos")}</option>
                  {availableProjects.map((project) => (
                    <option key={project.id} value={project.id.toString()}>
                      {project.id} - {project.name}
                    </option>
                  ))}
                </select>
              )}
            </div>
            <div>
              <Label>{t("governance.compliance.pmm.thresholds.status", "Estado")}</Label>
              <select
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value)}
                className="w-full mt-2 p-2 border rounded-md"
              >
                <option value="">{t("common.all", "Todos")}</option>
                <option value="ACTIVE">
                  {t("governance.compliance.pmm.thresholds.status.active", "Activo")}
                </option>
                <option value="INACTIVE">
                  {t("governance.compliance.pmm.thresholds.status.inactive", "Inactivo")}
                </option>
              </select>
            </div>
            <div>
              <Label>{t("governance.compliance.pmm.thresholds.metric", "Métrica")}</Label>
              <select
                value={metricFilter}
                onChange={(e) => setMetricFilter(e.target.value)}
                className="w-full mt-2 p-2 border rounded-md"
              >
                <option value="">{t("common.all", "Todas")}</option>
                <option value="ACCURACY">Accuracy</option>
                <option value="LATENCY">Latency</option>
                <option value="THROUGHPUT">Throughput</option>
                <option value="DRIFT">Drift</option>
                <option value="BIAS">Bias</option>
                <option value="PERFORMANCE">Performance</option>
                <option value="USER_SATISFACTION">User Satisfaction</option>
              </select>
            </div>
          </div>
        </CardBody>
      </Card>

      {/* Thresholds Table */}
      <Card>
        <CardHeader>
          <CardTitle>
            {t("governance.compliance.pmm.thresholds.list", "Thresholds Configurados")} (
            {filteredThresholds.length})
          </CardTitle>
        </CardHeader>
        <CardBody>
          {loading ? (
            <div className="text-center py-8">{t("common.loading", "Cargando...")}</div>
          ) : filteredThresholds.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("governance.compliance.pmm.thresholds.noData", "No hay thresholds configurados")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b">
                    <th className="text-left p-2">
                      {t("governance.compliance.pmm.thresholds.project", "Proyecto")}
                    </th>
                    <th className="text-left p-2">
                      {t("governance.compliance.pmm.thresholds.metric", "Métrica")}
                    </th>
                    <th className="text-left p-2">
                      {t("governance.compliance.pmm.thresholds.type", "Tipo")}
                    </th>
                    <th className="text-left p-2">
                      {t("governance.compliance.pmm.thresholds.warning", "Warning")}
                    </th>
                    <th className="text-left p-2">
                      {t("governance.compliance.pmm.thresholds.critical", "Critical")}
                    </th>
                    <th className="text-left p-2">
                      {t("governance.compliance.pmm.thresholds.severity", "Severidad")}
                    </th>
                    <th className="text-left p-2">
                      {t("governance.compliance.pmm.thresholds.status", "Estado")}
                    </th>
                    <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredThresholds.map((threshold) => (
                    <tr key={threshold.idxalertthreshold} className="border-b hover:bg-muted/50">
                      <td className="p-2">{threshold.projectName || threshold.idxproject}</td>
                      <td className="p-2">
                        <Badge variant="outline">{threshold.altmetricname}</Badge>
                      </td>
                      <td className="p-2">{threshold.altmetrictype}</td>
                      <td className="p-2">
                        {threshold.altmetrictype === "PERCENTAGE"
                          ? `${(threshold.altwarningthreshold * 100).toFixed(2)}%`
                          : threshold.altwarningthreshold}
                      </td>
                      <td className="p-2">
                        {threshold.altmetrictype === "PERCENTAGE"
                          ? `${(threshold.altcriticalthreshold * 100).toFixed(2)}%`
                          : threshold.altcriticalthreshold}
                      </td>
                      <td className="p-2">
                        <Badge className={getSeverityColor(threshold.altseverity)}>
                          {threshold.altseverity}
                        </Badge>
                      </td>
                      <td className="p-2">{getStatusBadge(threshold.altstatus)}</td>
                      <td className="p-2">
                        <div className="flex gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => openEditDialog(threshold)}
                          >
                            <Edit className="w-4 h-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => handleToggleStatus(threshold)}
                          >
                            {threshold.altstatus === "ACTIVE" ? (
                              <XCircle className="w-4 h-4" />
                            ) : (
                              <CheckCircle className="w-4 h-4" />
                            )}
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => handleDeleteThreshold(threshold.idxalertthreshold)}
                          >
                            <Trash2 className="w-4 h-4" />
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </CardBody>
      </Card>

      {/* Create/Edit Dialog */}
      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
          <DialogHeader>
            <DialogTitle>
              {editingThreshold
                ? t("governance.compliance.pmm.thresholds.edit", "Editar Threshold")
                : t("governance.compliance.pmm.thresholds.create", "Crear Threshold")}
            </DialogTitle>
            <DialogDescription>
              {t(
                "governance.compliance.pmm.thresholds.dialogDescription",
                "Configura umbrales de alerta para métricas de monitoreo"
              )}
            </DialogDescription>
          </DialogHeader>

          <div className="space-y-4">
            <div>
              <Label>{t("governance.compliance.pmm.thresholds.project", "Proyecto")}</Label>
              <Select
                value={
                  (editingThreshold?.idxproject || newThreshold.idxproject || 0).toString()
                }
                onValueChange={(value) => {
                  const val = parseInt(value);
                  if (editingThreshold) {
                    setEditingThreshold({ ...editingThreshold, idxproject: val });
                  } else {
                    setNewThreshold({ ...newThreshold, idxproject: val });
                  }
                }}
                disabled={!!projectIdFromUrl}
              >
                <SelectTrigger>
                  <SelectValue placeholder={(() => {
                      const projectId = editingThreshold?.idxproject || newThreshold.idxproject || 0;
                      const selectedProject = availableProjects.find((p) => p.id === projectId);
                      return selectedProject
                        ? `${selectedProject.id} - ${selectedProject.name}`
                        : t("governance.compliance.pmm.thresholds.selectProject", "Seleccionar proyecto");
                    })()} />
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
              <Label>{t("governance.compliance.pmm.thresholds.metric", "Métrica")}</Label>
              <Select
                value={editingThreshold?.altmetricname || newThreshold.altmetricname || ""}
                onValueChange={(value) => {
                  if (editingThreshold) {
                    setEditingThreshold({ ...editingThreshold, altmetricname: value });
                  } else {
                    setNewThreshold({ ...newThreshold, altmetricname: value });
                  }
                }}
              >
                <SelectTrigger>
                  <SelectValue
                    placeholder={t(
                      "governance.compliance.pmm.thresholds.selectMetric",
                      "Seleccionar métrica"
                    )}
                  />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ACCURACY">Accuracy</SelectItem>
                  <SelectItem value="LATENCY">Latency</SelectItem>
                  <SelectItem value="THROUGHPUT">Throughput</SelectItem>
                  <SelectItem value="DRIFT">Drift</SelectItem>
                  <SelectItem value="BIAS">Bias</SelectItem>
                  <SelectItem value="PERFORMANCE">Performance</SelectItem>
                  <SelectItem value="USER_SATISFACTION">User Satisfaction</SelectItem>
                </SelectContent>
              </Select>
            </div>

            <div>
              <Label>{t("governance.compliance.pmm.thresholds.type", "Tipo")}</Label>
              <Select
                value={editingThreshold?.altmetrictype || newThreshold.altmetrictype || "PERCENTAGE"}
                onValueChange={(value) => {
                  if (editingThreshold) {
                    setEditingThreshold({ ...editingThreshold, altmetrictype: value });
                  } else {
                    setNewThreshold({ ...newThreshold, altmetrictype: value });
                  }
                }}
              >
                <SelectTrigger>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="PERCENTAGE">Percentage</SelectItem>
                  <SelectItem value="ABSOLUTE">Absolute</SelectItem>
                </SelectContent>
              </Select>
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div>
                <Label>
                  {t("governance.compliance.pmm.thresholds.warning", "Warning Threshold")}
                </Label>
                <Input
                  type="number"
                  step="0.01"
                  value={
                    editingThreshold?.altwarningthreshold || newThreshold.altwarningthreshold || 0
                  }
                  onChange={(e) => {
                    const val = parseFloat(e.target.value) || 0;
                    if (editingThreshold) {
                      setEditingThreshold({ ...editingThreshold, altwarningthreshold: val });
                    } else {
                      setNewThreshold({ ...newThreshold, altwarningthreshold: val });
                    }
                  }}
                />
              </div>
              <div>
                <Label>
                  {t("governance.compliance.pmm.thresholds.critical", "Critical Threshold")}
                </Label>
                <Input
                  type="number"
                  step="0.01"
                  value={
                    editingThreshold?.altcriticalthreshold || newThreshold.altcriticalthreshold || 0
                  }
                  onChange={(e) => {
                    const val = parseFloat(e.target.value) || 0;
                    if (editingThreshold) {
                      setEditingThreshold({ ...editingThreshold, altcriticalthreshold: val });
                    } else {
                      setNewThreshold({ ...newThreshold, altcriticalthreshold: val });
                    }
                  }}
                />
              </div>
            </div>

            <div>
              <Label>{t("governance.compliance.pmm.thresholds.severity", "Severidad")}</Label>
              <Select
                value={editingThreshold?.altseverity || newThreshold.altseverity || "MEDIUM"}
                onValueChange={(value) => {
                  if (editingThreshold) {
                    setEditingThreshold({ ...editingThreshold, altseverity: value });
                  } else {
                    setNewThreshold({ ...newThreshold, altseverity: value });
                  }
                }}
              >
                <SelectTrigger>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="LOW">Low</SelectItem>
                  <SelectItem value="MEDIUM">Medium</SelectItem>
                  <SelectItem value="HIGH">High</SelectItem>
                  <SelectItem value="CRITICAL">Critical</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>

          <div className="flex justify-end gap-2 mt-6">
            <Button variant="outline" onClick={() => setIsDialogOpen(false)}>
              {t("common.cancel", "Cancelar")}
            </Button>
            <Button
              onClick={editingThreshold ? handleUpdateThreshold : handleCreateThreshold}
            >
              {editingThreshold
                ? t("common.update", "Actualizar")
                : t("common.create", "Crear")}
            </Button>
          </div>
        </DialogContent>
      </Dialog>
    </div>
  );
}
