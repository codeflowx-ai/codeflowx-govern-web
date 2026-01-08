"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
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
  FileText,
  Plus,
  Edit,
  Play,
  Pause,
  Trash2,
  X,
  CheckCircle,
  Clock,
  Archive,
} from "lucide-react";
import { useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";
import { mockProjects } from "@/app/(app)/governance/data/mockPMM";

interface PMMPlan {
  idxpmmplan: number;
  iduuid: string;
  pmmplanname: string;
  idxproject: number;
  projectName?: string;
  idxmodel?: number;
  modelName?: string;
  pmmmonitoringfrequency: string;
  pmmcustomfrequencyhours?: number;
  pmmmetrics: string;
  pmmalertthresholds: string;
  pmmreportingfrequency: string;
  pmmstatus: string;
  pmmlastmonitoringdate?: string;
  pmmcreatedat: string;
  pmmupdatedat?: string;
}

interface CreatePlanRequest {
  pmmplanname: string;
  idxproject: number;
  idxmodel?: number;
  pmmmonitoringfrequency: string;
  pmmcustomfrequencyhours?: number;
  pmmmetrics: string;
  pmmalertthresholds: string;
  pmmreportingfrequency: string;
}

const API_BASE_URL = process.env.NEXT_PUBLIC_BFF_BASE_URL || process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080";
const USE_MOCK_DATA = process.env.NEXT_PUBLIC_USE_MOCK_DATA === "true" || true; // Por defecto usar mock para demo

export default function PMMPlansPage() {
  const { t } = useTranslation();
  const searchParams = useSearchParams();
  const projectIdFromUrl = searchParams.get("projectId");

  const [plans, setPlans] = useState<PMMPlan[]>([]);
  const [loading, setLoading] = useState(true);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [isEditDialogOpen, setIsEditDialogOpen] = useState(false);
  const [selectedPlan, setSelectedPlan] = useState<PMMPlan | null>(null);
  const [projectFilter, setProjectFilter] = useState<string>(projectIdFromUrl || "");
  const [statusFilter, setStatusFilter] = useState<string>("");
  const [availableProjects, setAvailableProjects] = useState<Array<{ id: number; name: string }>>([]);

  // Mock data para demo (definido fuera del componente para persistencia)
  const [mockPlansState, setMockPlansState] = useState<PMMPlan[]>([
    {
      idxpmmplan: 1,
      iduuid: "550e8400-e29b-41d4-a716-446655440001",
      pmmplanname: "Plan de Monitoreo Diario - AI Credit Scoring System",
      idxproject: 1,
      projectName: "AI Credit Scoring System",
      pmmmonitoringfrequency: "DAILY",
      pmmmetrics: JSON.stringify({ accuracy: true, latency: true, throughput: true }),
      pmmalertthresholds: JSON.stringify({
        accuracy: { warning: 0.05, critical: 0.10 },
        latency: { warning: 200, critical: 500 },
      }),
      pmmreportingfrequency: "WEEKLY",
      pmmstatus: "ACTIVE",
      pmmcreatedat: new Date().toISOString(),
    },
    {
      idxpmmplan: 2,
      iduuid: "550e8400-e29b-41d4-a716-446655440002",
      pmmplanname: "Plan de Monitoreo Semanal - Facial Recognition System",
      idxproject: 2,
      projectName: "Facial Recognition System",
      pmmmonitoringfrequency: "WEEKLY",
      pmmmetrics: JSON.stringify({ accuracy: true, bias: true }),
      pmmalertthresholds: JSON.stringify({
        accuracy: { warning: 0.03, critical: 0.08 },
        bias: { warning: 0.02, critical: 0.05 },
      }),
      pmmreportingfrequency: "MONTHLY",
      pmmstatus: "ACTIVE",
      pmmcreatedat: new Date(Date.now() - 7 * 24 * 60 * 60 * 1000).toISOString(),
    },
    {
      idxpmmplan: 3,
      iduuid: "550e8400-e29b-41d4-a716-446655440003",
      pmmplanname: "Plan de Monitoreo Mensual - Healthcare Diagnostics AI System",
      idxproject: 3,
      projectName: "Healthcare Diagnostics AI System",
      pmmmonitoringfrequency: "MONTHLY",
      pmmmetrics: JSON.stringify({ accuracy: true, latency: true, throughput: true, bias: true }),
      pmmalertthresholds: JSON.stringify({
        accuracy: { warning: 0.05, critical: 0.10 },
        latency: { warning: 300, critical: 600 },
        bias: { warning: 0.03, critical: 0.07 },
      }),
      pmmreportingfrequency: "QUARTERLY",
      pmmstatus: "DRAFT",
      pmmcreatedat: new Date(Date.now() - 14 * 24 * 60 * 60 * 1000).toISOString(),
    },
  ]);

  const [newPlan, setNewPlan] = useState<CreatePlanRequest>({
    pmmplanname: "",
    idxproject: projectIdFromUrl ? parseInt(projectIdFromUrl) : 0,
    pmmmonitoringfrequency: "DAILY",
    pmmmetrics: JSON.stringify({ accuracy: true, latency: true, throughput: true }),
    pmmalertthresholds: JSON.stringify({
      accuracy: { warning: 0.05, critical: 0.10 },
      latency: { warning: 200, critical: 500 },
    }),
    pmmreportingfrequency: "WEEKLY",
  });

  useEffect(() => {
    if (projectIdFromUrl) {
      setProjectFilter(projectIdFromUrl);
    }
    loadPlans();
    loadProjects();
  }, [projectFilter, statusFilter, projectIdFromUrl]);

  const loadPlans = async () => {
    try {
      setLoading(true);

      if (USE_MOCK_DATA) {
        // Usar datos mock para demo
        await new Promise((resolve) => setTimeout(resolve, 500));
        let filteredPlans = [...mockPlansState];

        if (projectFilter) {
          filteredPlans = filteredPlans.filter(p => p.idxproject.toString() === projectFilter);
        }
        if (statusFilter) {
          filteredPlans = filteredPlans.filter(p => p.pmmstatus === statusFilter);
        }

        setPlans(filteredPlans);
        return;
      }

      // Llamada real a API
      const params = new URLSearchParams();
      if (projectFilter) params.append("projectId", projectFilter);
      if (statusFilter) params.append("status", statusFilter);

      const response = await fetch(`${API_BASE_URL}/api/v1/pmm/plans?${params.toString()}`);
      if (response.ok) {
        const data = await response.json();
        setPlans(data);
      }
    } catch (error) {
      console.error("Error loading plans:", error);
      // Fallback a mock data en caso de error
      if (USE_MOCK_DATA) {
        setPlans(mockPlansState);
      }
    } finally {
      setLoading(false);
    }
  };

  const loadProjects = async () => {
    try {
      if (USE_MOCK_DATA) {
        // Usar proyectos mock sincronizados
        setAvailableProjects(mockProjects);
        return;
      }
      // Llamada real a API
      const response = await fetch(`${API_BASE_URL}/api/v1/projects`);
      if (response.ok) {
        const data = await response.json();
        setAvailableProjects(data);
      }
    } catch (error) {
      console.error("Error loading projects:", error);
      // Fallback a mock data
      if (USE_MOCK_DATA) {
        setAvailableProjects(mockProjects);
      }
    }
  };

  const handleCreatePlan = async () => {
    try {
      if (USE_MOCK_DATA) {
        // Simular creación con mock data
        await new Promise((resolve) => setTimeout(resolve, 500));
        const newPlanData: PMMPlan = {
          idxpmmplan: mockPlansState.length + 1,
          iduuid: `550e8400-e29b-41d4-a716-44665544000${mockPlansState.length + 1}`,
          pmmplanname: newPlan.pmmplanname,
          idxproject: newPlan.idxproject,
          projectName: availableProjects.find(p => p.id === newPlan.idxproject)?.name,
          pmmmonitoringfrequency: newPlan.pmmmonitoringfrequency,
          pmmcustomfrequencyhours: newPlan.pmmcustomfrequencyhours,
          pmmmetrics: newPlan.pmmmetrics,
          pmmalertthresholds: newPlan.pmmalertthresholds,
          pmmreportingfrequency: newPlan.pmmreportingfrequency,
          pmmstatus: "DRAFT",
          pmmcreatedat: new Date().toISOString(),
        };
        setMockPlansState([...mockPlansState, newPlanData]);
        setIsDialogOpen(false);
        setNewPlan({
          pmmplanname: "",
          idxproject: 0,
          pmmmonitoringfrequency: "DAILY",
          pmmmetrics: JSON.stringify({ accuracy: true, latency: true, throughput: true }),
          pmmalertthresholds: JSON.stringify({
            accuracy: { warning: 0.05, critical: 0.10 },
            latency: { warning: 200, critical: 500 },
          }),
          pmmreportingfrequency: "WEEKLY",
        });
        loadPlans();
        return;
      }

      // Llamada real a API
      const response = await fetch(`${API_BASE_URL}/api/v1/pmm/plans`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newPlan),
      });

      if (response.ok) {
        setIsDialogOpen(false);
        setNewPlan({
          pmmplanname: "",
          idxproject: 0,
          pmmmonitoringfrequency: "DAILY",
          pmmmetrics: JSON.stringify({ accuracy: true, latency: true, throughput: true }),
          pmmalertthresholds: JSON.stringify({
            accuracy: { warning: 0.05, critical: 0.10 },
            latency: { warning: 200, critical: 500 },
          }),
          pmmreportingfrequency: "WEEKLY",
        });
        loadPlans();
      }
    } catch (error) {
      console.error("Error creating plan:", error);
    }
  };

  const handleActivatePlan = async (planId: number) => {
    try {
      if (USE_MOCK_DATA) {
        await new Promise((resolve) => setTimeout(resolve, 300));
        const plan = plans.find(p => p.idxpmmplan === planId);
        if (plan) {
          plan.pmmstatus = "ACTIVE";
          setPlans([...plans]);
        }
        return;
      }

      const response = await fetch(`${API_BASE_URL}/api/v1/pmm/plans/${planId}/activate`, {
        method: "POST",
      });

      if (response.ok) {
        loadPlans();
      }
    } catch (error) {
      console.error("Error activating plan:", error);
    }
  };

  const handleSuspendPlan = async (planId: number) => {
    try {
      if (USE_MOCK_DATA) {
        await new Promise((resolve) => setTimeout(resolve, 300));
        const plan = plans.find(p => p.idxpmmplan === planId);
        if (plan) {
          plan.pmmstatus = "SUSPENDED";
          setPlans([...plans]);
        }
        return;
      }

      const response = await fetch(`${API_BASE_URL}/api/v1/pmm/plans/${planId}/suspend`, {
        method: "POST",
      });

      if (response.ok) {
        loadPlans();
      }
    } catch (error) {
      console.error("Error suspending plan:", error);
    }
  };

  const handleDeletePlan = async (planId: number) => {
    if (!confirm(t("governance.compliance.pmm.plans.confirmDelete", "¿Está seguro de eliminar este plan?"))) {
      return;
    }

    try {
      if (USE_MOCK_DATA) {
        await new Promise((resolve) => setTimeout(resolve, 300));
        setPlans(plans.filter(p => p.idxpmmplan !== planId));
        return;
      }

      const response = await fetch(`${API_BASE_URL}/api/v1/pmm/plans/${planId}`, {
        method: "DELETE",
      });

      if (response.ok) {
        loadPlans();
      }
    } catch (error) {
      console.error("Error deleting plan:", error);
    }
  };

  const getStatusBadge = (status: string) => {
    const statusMap: Record<string, { label: string; variant: "primary" | "secondary" | "danger" | "outline" }> = {
      DRAFT: { label: t("governance.compliance.pmm.plans.status.DRAFT", "Borrador"), variant: "outline" },
      ACTIVE: { label: t("governance.compliance.pmm.plans.status.ACTIVE", "Activo"), variant: "primary" },
      SUSPENDED: { label: t("governance.compliance.pmm.plans.status.SUSPENDED", "Suspendido"), variant: "secondary" },
      ARCHIVED: { label: t("governance.compliance.pmm.plans.status.ARCHIVED", "Archivado"), variant: "outline" },
    };

    const statusInfo = statusMap[status] || { label: status, variant: "outline" as const };
    return <Badge variant={statusInfo.variant}>{statusInfo.label}</Badge>;
  };

  const getFrequencyLabel = (frequency: string) => {
    const frequencyMap: Record<string, string> = {
      HOURLY: t("governance.compliance.pmm.plans.frequency.HOURLY", "Cada hora"),
      DAILY: t("governance.compliance.pmm.plans.frequency.DAILY", "Diario"),
      WEEKLY: t("governance.compliance.pmm.plans.frequency.WEEKLY", "Semanal"),
      MONTHLY: t("governance.compliance.pmm.plans.frequency.MONTHLY", "Mensual"),
      CUSTOM: t("governance.compliance.pmm.plans.frequency.CUSTOM", "Personalizado"),
    };
    return frequencyMap[frequency] || frequency;
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        {/* Header */}
        <div className="space-y-2">
          <div className="flex items-center gap-3 mb-4">
            <FileText className="w-8 h-8 text-orange-500" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-orange-500 to-orange-700 bg-clip-text text-transparent">
              {t("governance.compliance.pmm.plans.title", "Planes de Monitoreo Post-Mercado")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t(
              "governance.compliance.pmm.plans.subtitle",
              "Gestión de planes formales de monitoreo según EU AI Act Art. 16.g y Art. 72"
            )}
          </p>
        </div>

        {/* Actions */}
        <div className="flex items-center justify-between">
          <Button
            className="flex items-center gap-2"
            onClick={() => setIsDialogOpen(true)}
          >
            <Plus className="w-4 h-4" />
            {t("governance.compliance.pmm.plans.create", "Crear Plan")}
          </Button>

          {/* Filters */}
          <div className="flex items-center gap-4">
            {projectIdFromUrl ? (
              <div className="px-3 py-2 border rounded-md bg-muted text-muted-foreground">
                {availableProjects.find((p) => p.id.toString() === projectIdFromUrl)?.name || `Proyecto ${projectIdFromUrl}`}
              </div>
            ) : (
              <select
                value={projectFilter}
                onChange={(e) => setProjectFilter(e.target.value)}
                className="px-3 py-2 border rounded-md bg-background"
              >
                <option value="">{t("common.allProjects", "Todos los proyectos")}</option>
                {availableProjects.map((project) => (
                  <option key={project.id} value={project.id.toString()}>
                    {project.name}
                  </option>
                ))}
              </select>
            )}

            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              className="px-3 py-2 border rounded-md bg-background"
            >
              <option value="">{t("common.allStatus", "Todos los estados")}</option>
              <option value="DRAFT">{t("governance.compliance.pmm.plans.status.DRAFT", "Borrador")}</option>
              <option value="ACTIVE">{t("governance.compliance.pmm.plans.status.ACTIVE", "Activo")}</option>
              <option value="SUSPENDED">{t("governance.compliance.pmm.plans.status.SUSPENDED", "Suspendido")}</option>
              <option value="ARCHIVED">{t("governance.compliance.pmm.plans.status.ARCHIVED", "Archivado")}</option>
            </select>
          </div>
        </div>

        {/* Plans List */}
        {loading ? (
          <div className="text-center py-8">{t("common.loading", "Cargando...")}</div>
        ) : (
          <div className="grid gap-4">
            {plans.map((plan) => (
              <Card key={plan.idxpmmplan} className="relative">
                <CardHeader>
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-3">
                      <CardTitle className="text-xl">{plan.pmmplanname}</CardTitle>
                      {getStatusBadge(plan.pmmstatus)}
                    </div>
                    <div className="flex items-center gap-2">
                      {plan.pmmstatus === "DRAFT" && (
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => handleActivatePlan(plan.idxpmmplan)}
                        >
                          <Play className="w-4 h-4 mr-1" />
                          {t("governance.compliance.pmm.plans.activate", "Activar")}
                        </Button>
                      )}
                      {plan.pmmstatus === "ACTIVE" && (
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => handleSuspendPlan(plan.idxpmmplan)}
                        >
                          <Pause className="w-4 h-4 mr-1" />
                          {t("governance.compliance.pmm.plans.suspend", "Suspender")}
                        </Button>
                      )}
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={() => handleDeletePlan(plan.idxpmmplan)}
                      >
                        <Trash2 className="w-4 h-4" />
                      </Button>
                    </div>
                  </div>
                </CardHeader>
                <CardBody>
                  <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm">
                    <div>
                      <span className="text-muted-foreground">
                        {t("governance.compliance.pmm.plans.project", "Proyecto")}:
                      </span>
                      <p className="font-medium">{plan.projectName || `#${plan.idxproject}`}</p>
                    </div>
                    <div>
                      <span className="text-muted-foreground">
                        {t("governance.compliance.pmm.plans.monitoringFrequency", "Frecuencia Monitoreo")}:
                      </span>
                      <p className="font-medium">{getFrequencyLabel(plan.pmmmonitoringfrequency)}</p>
                    </div>
                    <div>
                      <span className="text-muted-foreground">
                        {t("governance.compliance.pmm.plans.reportingFrequency", "Frecuencia Reportes")}:
                      </span>
                      <p className="font-medium">{getFrequencyLabel(plan.pmmreportingfrequency)}</p>
                    </div>
                    <div>
                      <span className="text-muted-foreground">
                        {t("governance.compliance.pmm.plans.createdAt", "Creado")}:
                      </span>
                      <p className="font-medium">
                        {new Date(plan.pmmcreatedat).toLocaleDateString()}
                      </p>
                    </div>
                  </div>
                </CardBody>
              </Card>
            ))}

            {plans.length === 0 && (
              <Card>
                <CardBody className="text-center py-8 text-muted-foreground">
                  {t("governance.compliance.pmm.plans.noPlans", "No hay planes disponibles")}
                </CardBody>
              </Card>
            )}
          </div>
        )}

        {/* Create Plan Dialog */}
        <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
          <DialogContent className="max-w-2xl p-0 max-h-[90vh] overflow-y-auto">
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
                  {t("governance.compliance.pmm.plans.createTitle", "Crear Nuevo Plan PMM")}
                </CardTitle>
                <p className="text-sm text-muted-foreground mt-2">
                  {t(
                    "governance.compliance.pmm.plans.createDescription",
                    "Crea un plan formal de monitoreo post-mercado para cualquier sistema de IA. Disponible para todos los proyectos, no solo de alto riesgo."
                  )}
                </p>
              </CardHeader>
              <CardBody className="space-y-4">
                <div>
                  <Label>{t("governance.compliance.pmm.plans.planName", "Nombre del Plan")}</Label>
                  <Input
                    value={newPlan.pmmplanname}
                    onChange={(e) => setNewPlan({ ...newPlan, pmmplanname: e.target.value })}
                    placeholder={t("governance.compliance.pmm.plans.planNamePlaceholder", "Ej: Plan de Monitoreo Diario")}
                  />
                </div>

                <div>
                  <Label>{t("governance.compliance.pmm.plans.project", "Proyecto")}</Label>
                  <Select
                    value={newPlan.idxproject > 0 ? newPlan.idxproject.toString() : ""}
                    onValueChange={(value) => setNewPlan({ ...newPlan, idxproject: parseInt(value) })}
                  >
                    <SelectTrigger>
                      {newPlan.idxproject > 0 ? (
                        (() => {
                          const selectedProject = availableProjects.find(
                            (p) => p.id === newPlan.idxproject
                          );
                          return (
                            <span>
                              {selectedProject
                                ? `${selectedProject.id} - ${selectedProject.name}`
                                : newPlan.idxproject.toString()}
                            </span>
                          );
                        })()
                      ) : (
                        <span className="text-muted-foreground">
                          {t("governance.compliance.pmm.plans.selectProject", "Seleccionar proyecto")}
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
                  <Label>{t("governance.compliance.pmm.plans.monitoringFrequency", "Frecuencia de Monitoreo")}</Label>
                  <Select
                    value={newPlan.pmmmonitoringfrequency}
                    onValueChange={(value) => setNewPlan({ ...newPlan, pmmmonitoringfrequency: value })}
                  >
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="HOURLY">{t("governance.compliance.pmm.plans.frequency.HOURLY", "Cada hora")}</SelectItem>
                      <SelectItem value="DAILY">{t("governance.compliance.pmm.plans.frequency.DAILY", "Diario")}</SelectItem>
                      <SelectItem value="WEEKLY">{t("governance.compliance.pmm.plans.frequency.WEEKLY", "Semanal")}</SelectItem>
                      <SelectItem value="MONTHLY">{t("governance.compliance.pmm.plans.frequency.MONTHLY", "Mensual")}</SelectItem>
                      <SelectItem value="CUSTOM">{t("governance.compliance.pmm.plans.frequency.CUSTOM", "Personalizado")}</SelectItem>
                    </SelectContent>
                  </Select>
                </div>

                {newPlan.pmmmonitoringfrequency === "CUSTOM" && (
                  <div>
                    <Label>{t("governance.compliance.pmm.plans.customHours", "Horas Personalizadas")}</Label>
                    <Input
                      type="number"
                      value={newPlan.pmmcustomfrequencyhours || ""}
                      onChange={(e) =>
                        setNewPlan({ ...newPlan, pmmcustomfrequencyhours: parseInt(e.target.value) })
                      }
                      placeholder="24"
                    />
                  </div>
                )}

                <div>
                  <Label>{t("governance.compliance.pmm.plans.reportingFrequency", "Frecuencia de Reportes")}</Label>
                  <Select
                    value={newPlan.pmmreportingfrequency}
                    onValueChange={(value) => setNewPlan({ ...newPlan, pmmreportingfrequency: value })}
                  >
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="DAILY">{t("governance.compliance.pmm.plans.frequency.DAILY", "Diario")}</SelectItem>
                      <SelectItem value="WEEKLY">{t("governance.compliance.pmm.plans.frequency.WEEKLY", "Semanal")}</SelectItem>
                      <SelectItem value="MONTHLY">{t("governance.compliance.pmm.plans.frequency.MONTHLY", "Mensual")}</SelectItem>
                      <SelectItem value="QUARTERLY">{t("governance.compliance.pmm.plans.frequency.QUARTERLY", "Trimestral")}</SelectItem>
                      <SelectItem value="ANNUAL">{t("governance.compliance.pmm.plans.frequency.ANNUAL", "Anual")}</SelectItem>
                    </SelectContent>
                  </Select>
                </div>

                <div>
                  <Label>{t("governance.compliance.pmm.plans.metrics", "Métricas (JSON)")}</Label>
                  <Textarea
                    value={newPlan.pmmmetrics}
                    onChange={(e) => setNewPlan({ ...newPlan, pmmmetrics: e.target.value })}
                    rows={4}
                    placeholder='{"accuracy": true, "latency": true}'
                  />
                </div>

                <div>
                  <Label>{t("governance.compliance.pmm.plans.alertThresholds", "Thresholds de Alertas (JSON)")}</Label>
                  <Textarea
                    value={newPlan.pmmalertthresholds}
                    onChange={(e) => setNewPlan({ ...newPlan, pmmalertthresholds: e.target.value })}
                    rows={4}
                    placeholder='{"accuracy": {"warning": 0.05, "critical": 0.10}}'
                  />
                </div>
              </CardBody>
              <div className="border-t px-6 py-4 flex justify-end gap-2">
                <Button variant="outline" onClick={() => setIsDialogOpen(false)}>
                  {t("common.cancel", "Cancelar")}
                </Button>
                <Button onClick={handleCreatePlan}>
                  {t("governance.compliance.pmm.plans.create", "Crear")}
                </Button>
              </div>
            </Card>
          </DialogContent>
        </Dialog>
      </div>
    </div>
  );
}
