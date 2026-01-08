"use client";
import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { SimpleModal } from "@/components/ui/SimpleModal";
import { ArrowLeft, FlaskConical, Play, Eye, Download, Clock, CheckCircle, XCircle, AlertCircle } from "lucide-react";
import { useEffect, useState } from "react";
import { useRouter, useParams } from "next/navigation";

interface Run {
  id: number;
  name: string;
  description: string;
  runType: string;
  status: "PENDING" | "RUNNING" | "COMPLETED" | "FAILED" | "CANCELLED";
  startedAt?: string;
  endedAt?: string;
  duration?: number;
  metrics?: Record<string, any>;
  parameters?: Record<string, any>;
  tags?: Record<string, string>;
  artifacts?: Array<{ path: string; type: string; size?: number; mimeType?: string }>;
}

interface Experiment {
  id: number;
  name: string;
  description: string;
  entityType: string;
  entityId: number;
  entityName?: string;
  status: string;
  tags?: Record<string, string>;
  createdAt: string;
  updatedAt: string;
  runs: Run[];
}

export default function ExperimentDetailPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const params = useParams();
  const experimentId = params.id as string;
  const [experiment, setExperiment] = useState<Experiment | null>(null);
  const [loading, setLoading] = useState(true);
  const [selectedRun, setSelectedRun] = useState<Run | null>(null);
  const [showCreateRunModal, setShowCreateRunModal] = useState(false);
  const [newRun, setNewRun] = useState({
    name: "",
    description: "",
    runType: "BIAS_ANALYSIS",
    entityId: "",
  });

  useEffect(() => {
    loadData();
  }, [experimentId]);

  const loadData = async () => {
    try {
      setLoading(true);
      // Mock data
      const mockExperiment: Experiment = {
        id: parseInt(experimentId),
        name: "Bias Analysis Experiments for Model #1",
        description: "Experimentos de análisis de sesgo para GPT-4 Fine-tuned Classification",
        entityType: "MODEL",
        entityId: 1,
        entityName: "GPT-4 Fine-tuned Classification",
        status: "ACTIVE",
        tags: { version: "1.0.0", team: "data-science" },
        createdAt: "2024-01-15T10:30:00",
        updatedAt: "2024-01-20T14:20:00",
        runs: [
          {
            id: 1,
            name: "Bias Analysis Run - gender",
            description: "Análisis de sesgo por género",
            runType: "BIAS_ANALYSIS",
            status: "COMPLETED",
            startedAt: "2024-01-15T10:30:00",
            endedAt: "2024-01-15T10:35:00",
            duration: 300,
            metrics: {
              bias_score: 0.45,
              demographic_parity_difference: 0.32,
              equal_opportunity_difference: 0.28,
              severity: "HIGH",
              classification: "HIGH_BIAS",
            },
            parameters: {
              protectedAttribute: "gender",
              favorableOutcome: "1",
              threshold: 0.8,
            },
            tags: { analysisType: "BIAS_ANALYSIS", modelId: "1" },
            artifacts: [
              {
                path: "s3://codeflowx-artifacts/runs/1/report.pdf",
                type: "report",
                size: 2048000,
                mimeType: "application/pdf",
              },
            ],
          },
          {
            id: 2,
            name: "Bias Analysis Run - age",
            description: "Análisis de sesgo por edad",
            runType: "BIAS_ANALYSIS",
            status: "COMPLETED",
            startedAt: "2024-01-16T09:15:00",
            endedAt: "2024-01-16T09:20:00",
            duration: 300,
            metrics: {
              bias_score: 0.28,
              demographic_parity_difference: 0.18,
              equal_opportunity_difference: 0.22,
              severity: "MEDIUM",
              classification: "MODERATE_BIAS",
            },
            parameters: {
              protectedAttribute: "age",
              favorableOutcome: "1",
              threshold: 0.8,
            },
            tags: { analysisType: "BIAS_ANALYSIS", modelId: "1" },
          },
          {
            id: 3,
            name: "Bias Analysis Run - race",
            description: "Análisis de sesgo por raza",
            runType: "BIAS_ANALYSIS",
            status: "RUNNING",
            startedAt: "2024-01-20T14:00:00",
            parameters: {
              protectedAttribute: "race",
              favorableOutcome: "1",
              threshold: 0.8,
            },
            tags: { analysisType: "BIAS_ANALYSIS", modelId: "1" },
          },
          {
            id: 4,
            name: "Bias Analysis Run - location",
            description: "Análisis de sesgo por ubicación geográfica",
            runType: "BIAS_ANALYSIS",
            status: "FAILED",
            startedAt: "2024-01-19T11:00:00",
            endedAt: "2024-01-19T11:02:00",
            duration: 120,
            parameters: {
              protectedAttribute: "location",
              favorableOutcome: "1",
              threshold: 0.8,
            },
            tags: { analysisType: "BIAS_ANALYSIS", modelId: "1" },
          },
          {
            id: 5,
            name: "Bias Analysis Run - education",
            description: "Análisis de sesgo por nivel educativo",
            runType: "BIAS_ANALYSIS",
            status: "PENDING",
            parameters: {
              protectedAttribute: "education",
              favorableOutcome: "1",
              threshold: 0.8,
            },
            tags: { analysisType: "BIAS_ANALYSIS", modelId: "1" },
          },
        ],
      };

      setExperiment(mockExperiment);
    } catch (error) {
      console.error("Error loading experiment:", error);
    } finally {
      setLoading(false);
    }
  };

  const getStatusBadge = (status: string) => {
    switch (status) {
      case "COMPLETED":
        return (
          <Badge className="bg-green-500/20 text-green-400 border-green-500/50 flex items-center gap-1">
            <CheckCircle className="w-3 h-3" />
            COMPLETED
          </Badge>
        );
      case "RUNNING":
        return (
          <Badge className="bg-blue-500/20 text-blue-400 border-blue-500/50 flex items-center gap-1">
            <Clock className="w-3 h-3" />
            RUNNING
          </Badge>
        );
      case "FAILED":
        return (
          <Badge className="bg-red-500/20 text-red-400 border-red-500/50 flex items-center gap-1">
            <XCircle className="w-3 h-3" />
            FAILED
          </Badge>
        );
      case "PENDING":
        return (
          <Badge className="bg-yellow-500/20 text-yellow-400 border-yellow-500/50 flex items-center gap-1">
            <AlertCircle className="w-3 h-3" />
            PENDING
          </Badge>
        );
      case "CANCELLED":
        return (
          <Badge className="bg-gray-500/20 text-gray-400 border-gray-500/50">CANCELLED</Badge>
        );
      default:
        return <Badge>{status}</Badge>;
    }
  };

  const formatDuration = (seconds?: number) => {
    if (!seconds) return "-";
    if (seconds < 60) return `${seconds}s`;
    if (seconds < 3600) return `${Math.floor(seconds / 60)}m ${seconds % 60}s`;
    return `${Math.floor(seconds / 3600)}h ${Math.floor((seconds % 3600) / 60)}m`;
  };

  if (loading) {
    return <div className="text-center py-8">{t("common.loading", "Cargando...")}</div>;
  }

  if (!experiment) {
    return <div className="text-center py-8">{t("governance.testing.experiments.notFound", "Experimento no encontrado")}</div>;
  }

  return (
    <div className="space-y-4">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-4">
          <Button variant="ghost" size="sm" onClick={() => router.push("/governance/testing/experiments")}>
            <ArrowLeft className="w-4 h-4 mr-2" />
            {t("common.back", "Volver")}
          </Button>
          <div>
            <div className="flex items-center gap-3 mb-2">
              <FlaskConical className="w-6 h-6 text-primary" />
              <h1 className="text-2xl font-bold text-foreground">{experiment.name}</h1>
            </div>
            <p className="text-sm text-muted-foreground ml-9">{experiment.description}</p>
          </div>
        </div>
        <Button
          onClick={() => setShowCreateRunModal(true)}
          className="bg-primary hover:bg-primary/90"
        >
          <Play className="h-4 w-4 mr-2" />
          {t("governance.testing.experiments.newRun", "Nuevo Run")}
        </Button>
      </div>

      {/* Información del Experimento */}
      <Card className="border-2">
        <CardHeader>
          <CardTitle>{t("governance.testing.experiments.detail.info", "Información del Experimento")}</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div>
              <label className="text-sm font-medium text-muted-foreground">
                {t("governance.testing.experiments.detail.entityType", "Tipo de Entidad")}
              </label>
              <p className="text-base font-semibold">{experiment.entityType}</p>
            </div>
            <div>
              <label className="text-sm font-medium text-muted-foreground">
                {t("governance.testing.experiments.detail.entity", "Entidad")}
              </label>
              <p className="text-base">{experiment.entityName || `ID: ${experiment.entityId}`}</p>
            </div>
            <div>
              <label className="text-sm font-medium text-muted-foreground">
                {t("governance.testing.experiments.detail.status", "Estado")}
              </label>
              <p className="text-base">
                <Badge className={experiment.status === "ACTIVE" ? "bg-green-500/20 text-green-400" : ""}>
                  {experiment.status}
                </Badge>
              </p>
            </div>
            <div>
              <label className="text-sm font-medium text-muted-foreground">
                {t("governance.testing.experiments.detail.runs", "Total Runs")}
              </label>
              <p className="text-base font-semibold">{experiment.runs.length}</p>
            </div>
          </div>
          {experiment.tags && Object.keys(experiment.tags).length > 0 && (
            <div className="mt-4">
              <label className="text-sm font-medium text-muted-foreground mb-2 block">Tags</label>
              <div className="flex flex-wrap gap-2">
                {Object.entries(experiment.tags).map(([key, value]) => (
                  <Badge key={key} variant="outline">
                    {key}: {value}
                  </Badge>
                ))}
              </div>
            </div>
          )}
        </CardContent>
      </Card>

      {/* Tabs: Runs y Estadísticas */}
      <Tabs defaultValue="runs" className="space-y-4">
        <TabsList>
          <TabsTrigger value="runs">
            {t("governance.testing.experiments.tabs.runs", "Runs")} ({experiment.runs.length})
          </TabsTrigger>
          <TabsTrigger value="stats">
            {t("governance.testing.experiments.tabs.stats", "Estadísticas")}
          </TabsTrigger>
        </TabsList>

        <TabsContent value="runs" className="space-y-4">
          {/* Tabla de Runs */}
          <Card className="border-2">
            <CardHeader>
              <CardTitle>{t("governance.testing.experiments.runs.title", "Runs del Experimento")}</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="overflow-x-auto">
                <table className="w-full text-sm">
                  <thead>
                    <tr className="border-b">
                      <th className="text-left p-2">{t("governance.testing.experiments.runs.name", "Nombre")}</th>
                      <th className="text-left p-2">{t("governance.testing.experiments.runs.type", "Tipo")}</th>
                      <th className="text-left p-2">{t("governance.testing.experiments.runs.status", "Estado")}</th>
                      <th className="text-left p-2">{t("governance.testing.experiments.runs.duration", "Duración")}</th>
                      <th className="text-left p-2">{t("governance.testing.experiments.runs.started", "Iniciado")}</th>
                      <th className="text-center p-2">{t("governance.testing.experiments.runs.actions", "Acciones")}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {experiment.runs.map((run) => (
                      <tr key={run.id} className="border-b hover:bg-muted/50">
                        <td className="p-2">
                          <div>
                            <div className="font-medium">{run.name}</div>
                            {run.description && (
                              <div className="text-xs text-muted-foreground mt-1">{run.description}</div>
                            )}
                          </div>
                        </td>
                        <td className="p-2">
                          <Badge variant="outline">{run.runType}</Badge>
                        </td>
                        <td className="p-2">{getStatusBadge(run.status)}</td>
                        <td className="p-2">{formatDuration(run.duration)}</td>
                        <td className="p-2 text-xs text-muted-foreground">
                          {run.startedAt ? new Date(run.startedAt).toLocaleString() : "-"}
                        </td>
                        <td className="p-2">
                          <div className="flex items-center justify-center gap-2">
                            <Button
                              variant="ghost"
                              size="sm"
                              title={t("common.view", "Ver")}
                              onClick={() => router.push(`/governance/testing/runs/${run.id}`)}
                            >
                              <Eye className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="stats" className="space-y-4">
          {/* Estadísticas */}
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <Card className="border-2">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">Total Runs</p>
                <h2 className="text-2xl font-bold">{experiment.runs.length}</h2>
              </CardBody>
            </Card>
            <Card className="border-2">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">Completados</p>
                <h2 className="text-2xl font-bold text-green-400">
                  {experiment.runs.filter((r) => r.status === "COMPLETED").length}
                </h2>
              </CardBody>
            </Card>
            <Card className="border-2">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">Fallidos</p>
                <h2 className="text-2xl font-bold text-red-400">
                  {experiment.runs.filter((r) => r.status === "FAILED").length}
                </h2>
              </CardBody>
            </Card>
            <Card className="border-2">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">En Ejecución</p>
                <h2 className="text-2xl font-bold text-blue-400">
                  {experiment.runs.filter((r) => r.status === "RUNNING").length}
                </h2>
              </CardBody>
            </Card>
          </div>
        </TabsContent>
      </Tabs>

      {/* Modal de Crear Run */}
      <SimpleModal
        isOpen={showCreateRunModal}
        onClose={() => {
          setShowCreateRunModal(false);
          setNewRun({
            name: "",
            description: "",
            runType: "BIAS_ANALYSIS",
            entityId: "",
          });
        }}
        title={t("governance.testing.experiments.createRunModal.title", "Crear Nuevo Run")}
        maxWidth="max-w-2xl"
      >
        <div className="space-y-4">
          <div className="space-y-2">
            <label className="text-sm font-medium text-foreground">
              {t("governance.testing.experiments.createRunModal.name", "Nombre del Run")} *
            </label>
            <Input
              placeholder={t("governance.testing.experiments.createRunModal.namePlaceholder", "Ej: Bias Analysis Run - gender")}
              value={newRun.name}
              onChange={(e) => setNewRun({ ...newRun, name: e.target.value })}
              required
            />
          </div>

          <div className="space-y-2">
            <label className="text-sm font-medium text-foreground">
              {t("governance.testing.experiments.createRunModal.description", "Descripción")}
            </label>
            <Textarea
              placeholder={t("governance.testing.experiments.createRunModal.descriptionPlaceholder", "Descripción del run...")}
              value={newRun.description}
              onChange={(e) => setNewRun({ ...newRun, description: e.target.value })}
              rows={3}
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <label className="text-sm font-medium text-foreground">
                {t("governance.testing.experiments.createRunModal.runType", "Tipo de Run")} *
              </label>
              <Select
                value={newRun.runType}
                onValueChange={(value) => setNewRun({ ...newRun, runType: value })}
              >
                <SelectTrigger>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="BIAS_ANALYSIS">BIAS_ANALYSIS</SelectItem>
                  <SelectItem value="EXPLAINABILITY">EXPLAINABILITY</SelectItem>
                  <SelectItem value="PERFORMANCE">PERFORMANCE</SelectItem>
                  <SelectItem value="VALIDATION">VALIDATION</SelectItem>
                  <SelectItem value="PROMPT_TEST">PROMPT_TEST</SelectItem>
                  <SelectItem value="COMPLIANCE_CHECK">COMPLIANCE_CHECK</SelectItem>
                  <SelectItem value="QUALITY_ASSESSMENT">QUALITY_ASSESSMENT</SelectItem>
                  <SelectItem value="BEHAVIOR_TEST">BEHAVIOR_TEST</SelectItem>
                  <SelectItem value="COMPLIANCE_TEST">COMPLIANCE_TEST</SelectItem>
                  <SelectItem value="PERFORMANCE_TEST">PERFORMANCE_TEST</SelectItem>
                  <SelectItem value="RETRIEVAL_TEST">RETRIEVAL_TEST</SelectItem>
                  <SelectItem value="ACCURACY_TEST">ACCURACY_TEST</SelectItem>
                  <SelectItem value="LATENCY_TEST">LATENCY_TEST</SelectItem>
                  <SelectItem value="QUALITY_CHECK">QUALITY_CHECK</SelectItem>
                </SelectContent>
              </Select>
            </div>

            <div className="space-y-2">
              <label className="text-sm font-medium text-foreground">
                {t("governance.testing.experiments.createRunModal.entityId", "ID de la Entidad")}
              </label>
              <Input
                type="number"
                placeholder={t("governance.testing.experiments.createRunModal.entityIdPlaceholder", "Opcional")}
                value={newRun.entityId}
                onChange={(e) => setNewRun({ ...newRun, entityId: e.target.value })}
              />
            </div>
          </div>

          <div className="flex justify-end gap-2 pt-4 border-t">
            <Button
              variant="outline"
              onClick={() => {
                setShowCreateRunModal(false);
                setNewRun({
                  name: "",
                  description: "",
                  runType: "BIAS_ANALYSIS",
                  entityId: "",
                });
              }}
            >
              {t("common.cancel", "Cancelar")}
            </Button>
            <Button
              onClick={() => {
                if (!newRun.name.trim()) {
                  alert(t("governance.testing.experiments.createRunModal.nameRequired", "El nombre es requerido"));
                  return;
                }

                if (!experiment) return;

                // Mock: Crear run
                const newRunData: Run = {
                  id: experiment.runs.length + 1,
                  name: newRun.name,
                  description: newRun.description,
                  runType: newRun.runType,
                  status: "PENDING",
                  parameters: {},
                  tags: {},
                };

                const updatedExperiment = {
                  ...experiment,
                  runs: [...experiment.runs, newRunData],
                };

                setExperiment(updatedExperiment);

                setShowCreateRunModal(false);
                setNewRun({
                  name: "",
                  description: "",
                  runType: "BIAS_ANALYSIS",
                  entityId: "",
                });

                alert(t("governance.testing.experiments.createRunModal.success", "Run creado exitosamente"));
              }}
              className="bg-primary hover:bg-primary/90"
            >
              <Play className="h-4 w-4 mr-2" />
              {t("governance.testing.experiments.createRunModal.create", "Crear Run")}
            </Button>
          </div>
        </div>
      </SimpleModal>
    </div>
  );
}
