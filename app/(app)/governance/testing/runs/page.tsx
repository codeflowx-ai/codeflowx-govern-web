"use client";
import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Play, Search, Eye, Clock, CheckCircle, XCircle, AlertCircle } from "lucide-react";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

interface Run {
  id: number;
  experimentId: number;
  experimentName: string;
  name: string;
  runType: string;
  entityType: string;
  entityId: number;
  entityName?: string;
  status: "PENDING" | "RUNNING" | "COMPLETED" | "FAILED" | "CANCELLED";
  startedAt?: string;
  endedAt?: string;
  duration?: number;
}

export default function RunsPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [runs, setRuns] = useState<Run[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [entityTypeFilter, setEntityTypeFilter] = useState<string>("ALL");
  const [statusFilter, setStatusFilter] = useState<string>("ALL");
  const [runTypeFilter, setRunTypeFilter] = useState<string>("ALL");
  const [stats, setStats] = useState({
    total: 0,
    completed: 0,
    running: 0,
    failed: 0,
    pending: 0,
  });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      // Mock data
      const mockRuns: Run[] = [
        {
          id: 1,
          experimentId: 1,
          experimentName: "Bias Analysis Experiments for Model #1",
          name: "Bias Analysis Run - gender",
          runType: "BIAS_ANALYSIS",
          entityType: "MODEL",
          entityId: 1,
          entityName: "GPT-4 Fine-tuned Classification",
          status: "COMPLETED",
          startedAt: "2024-01-15T10:30:00",
          endedAt: "2024-01-15T10:35:00",
          duration: 300,
        },
        {
          id: 2,
          experimentId: 1,
          experimentName: "Bias Analysis Experiments for Model #1",
          name: "Bias Analysis Run - age",
          runType: "BIAS_ANALYSIS",
          entityType: "MODEL",
          entityId: 1,
          entityName: "GPT-4 Fine-tuned Classification",
          status: "COMPLETED",
          startedAt: "2024-01-16T09:15:00",
          endedAt: "2024-01-16T09:20:00",
          duration: 300,
        },
        {
          id: 3,
          experimentId: 2,
          experimentName: "Explainability Experiments for Model #2",
          name: "Explainability Analysis Run - SHAP",
          runType: "EXPLAINABILITY",
          entityType: "MODEL",
          entityId: 2,
          entityName: "Claude Vision OCR",
          status: "COMPLETED",
          startedAt: "2024-01-14T14:20:00",
          endedAt: "2024-01-14T14:28:00",
          duration: 480,
        },
        {
          id: 4,
          experimentId: 3,
          experimentName: "Performance Tests for Model #3",
          name: "Performance Test - accuracy",
          runType: "PERFORMANCE",
          entityType: "MODEL",
          entityId: 3,
          entityName: "Resume Screening Model",
          status: "COMPLETED",
          startedAt: "2024-01-13T08:00:00",
          endedAt: "2024-01-13T08:05:00",
          duration: 300,
        },
        {
          id: 5,
          experimentId: 1,
          experimentName: "Bias Analysis Experiments for Model #1",
          name: "Bias Analysis Run - race",
          runType: "BIAS_ANALYSIS",
          entityType: "MODEL",
          entityId: 1,
          entityName: "GPT-4 Fine-tuned Classification",
          status: "RUNNING",
          startedAt: "2024-01-20T14:00:00",
        },
        {
          id: 6,
          experimentId: 4,
          experimentName: "Prompt Testing for Customer Service",
          name: "Prompt Test - response_quality",
          runType: "PROMPT_TEST",
          entityType: "PROMPT",
          entityId: 10,
          entityName: "Customer Service Prompt v2",
          status: "COMPLETED",
          startedAt: "2024-01-12T10:00:00",
          endedAt: "2024-01-12T10:02:00",
          duration: 120,
        },
        {
          id: 7,
          experimentId: 5,
          experimentName: "RAG Accuracy Tests",
          name: "RAG Retrieval Test - accuracy",
          runType: "RETRIEVAL_TEST",
          entityType: "RAG",
          entityId: 5,
          entityName: "Technical Documentation RAG",
          status: "COMPLETED",
          startedAt: "2024-01-11T14:30:00",
          endedAt: "2024-01-11T14:35:00",
          duration: 300,
        },
        {
          id: 8,
          experimentId: 1,
          experimentName: "Bias Analysis Experiments for Model #1",
          name: "Bias Analysis Run - location",
          runType: "BIAS_ANALYSIS",
          entityType: "MODEL",
          entityId: 1,
          entityName: "GPT-4 Fine-tuned Classification",
          status: "FAILED",
          startedAt: "2024-01-19T11:00:00",
          endedAt: "2024-01-19T11:02:00",
          duration: 120,
        },
        {
          id: 9,
          experimentId: 6,
          experimentName: "Agent Behavior Tests",
          name: "Agent Behavior Test - compliance",
          runType: "BEHAVIOR_TEST",
          entityType: "AGENT",
          entityId: 7,
          entityName: "Support Agent v1",
          status: "PENDING",
        },
        {
          id: 10,
          experimentId: 7,
          experimentName: "Dataset Quality Checks",
          name: "Dataset Quality Check - completeness",
          runType: "QUALITY_CHECK",
          entityType: "DATASET",
          entityId: 3,
          entityName: "Training Dataset v3",
          status: "COMPLETED",
          startedAt: "2024-01-09T09:00:00",
          endedAt: "2024-01-09T09:10:00",
          duration: 600,
        },
      ];

      setRuns(mockRuns);

      // Calcular estadísticas
      const total = mockRuns.length;
      const completed = mockRuns.filter((r) => r.status === "COMPLETED").length;
      const running = mockRuns.filter((r) => r.status === "RUNNING").length;
      const failed = mockRuns.filter((r) => r.status === "FAILED").length;
      const pending = mockRuns.filter((r) => r.status === "PENDING").length;

      setStats({
        total,
        completed,
        running,
        failed,
        pending,
      });
    } catch (error) {
      console.error("Error loading runs:", error);
    } finally {
      setLoading(false);
    }
  };

  const filteredRuns = runs.filter((run) => {
    const matchesSearch =
      run.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      run.experimentName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      run.entityName?.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesEntityType = entityTypeFilter === "ALL" || run.entityType === entityTypeFilter;
    const matchesStatus = statusFilter === "ALL" || run.status === statusFilter;
    const matchesRunType = runTypeFilter === "ALL" || run.runType === runTypeFilter;
    return matchesSearch && matchesEntityType && matchesStatus && matchesRunType;
  });

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

  const getEntityTypeBadge = (type: string) => {
    const colors: Record<string, string> = {
      MODEL: "bg-blue-500/20 text-blue-400 border-blue-500/50",
      PROMPT: "bg-purple-500/20 text-purple-400 border-purple-500/50",
      AGENT: "bg-green-500/20 text-green-400 border-green-500/50",
      RAG: "bg-orange-500/20 text-orange-400 border-orange-500/50",
      DATASET: "bg-pink-500/20 text-pink-400 border-pink-500/50",
    };
    return (
      <Badge className={colors[type] || "bg-gray-500/20 text-gray-400 border-gray-500/50"}>
        {type}
      </Badge>
    );
  };

  return (
    <div className="space-y-4">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <div className="flex items-center gap-3 mb-2">
            <Play className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("governance.testing.runs.title", "Runs")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("governance.testing.runs.subtitle", "Listado de todos los runs (cross-entity)")}
          </p>
        </div>
        <Button
          onClick={() => router.push("/governance/testing/compare")}
          variant="outline"
        >
          {t("governance.testing.runs.compare", "Comparar Runs")}
        </Button>
      </div>

      {/* Métricas */}
      <div className="grid grid-cols-1 md:grid-cols-5 gap-4">
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">Total</p>
            <h2 className="text-2xl font-bold">{stats.total}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">Completados</p>
            <h2 className="text-2xl font-bold text-green-400">{stats.completed}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">En Ejecución</p>
            <h2 className="text-2xl font-bold text-blue-400">{stats.running}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">Fallidos</p>
            <h2 className="text-2xl font-bold text-red-400">{stats.failed}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">Pendientes</p>
            <h2 className="text-2xl font-bold text-yellow-400">{stats.pending}</h2>
          </CardBody>
        </Card>
      </div>

      {/* Filtros */}
      <Card className="border-2">
        <CardHeader>
          <CardTitle>{t("governance.testing.runs.filters.title", "Filtros")}</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <div className="space-y-2">
              <label className="text-sm font-medium">Búsqueda</label>
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-4 h-4" />
                <Input
                  placeholder="Buscar..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">Tipo de Entidad</label>
              <Select value={entityTypeFilter} onValueChange={setEntityTypeFilter}>
                <SelectTrigger>
                  <SelectValue placeholder="TODOS" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">TODOS</SelectItem>
                  <SelectItem value="MODEL">MODEL</SelectItem>
                  <SelectItem value="PROMPT">PROMPT</SelectItem>
                  <SelectItem value="AGENT">AGENT</SelectItem>
                  <SelectItem value="RAG">RAG</SelectItem>
                  <SelectItem value="DATASET">DATASET</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">Tipo de Run</label>
              <Select value={runTypeFilter} onValueChange={setRunTypeFilter}>
                <SelectTrigger>
                  <SelectValue placeholder="TODOS" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">TODOS</SelectItem>
                  <SelectItem value="BIAS_ANALYSIS">BIAS_ANALYSIS</SelectItem>
                  <SelectItem value="EXPLAINABILITY">EXPLAINABILITY</SelectItem>
                  <SelectItem value="PERFORMANCE">PERFORMANCE</SelectItem>
                  <SelectItem value="PROMPT_TEST">PROMPT_TEST</SelectItem>
                  <SelectItem value="RETRIEVAL_TEST">RETRIEVAL_TEST</SelectItem>
                  <SelectItem value="BEHAVIOR_TEST">BEHAVIOR_TEST</SelectItem>
                  <SelectItem value="QUALITY_CHECK">QUALITY_CHECK</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">Estado</label>
              <Select value={statusFilter} onValueChange={setStatusFilter}>
                <SelectTrigger>
                  <SelectValue placeholder="TODOS" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">TODOS</SelectItem>
                  <SelectItem value="COMPLETED">COMPLETED</SelectItem>
                  <SelectItem value="RUNNING">RUNNING</SelectItem>
                  <SelectItem value="FAILED">FAILED</SelectItem>
                  <SelectItem value="PENDING">PENDING</SelectItem>
                  <SelectItem value="CANCELLED">CANCELLED</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Tabla */}
      <Card className="border-2">
        <CardHeader>
          <CardTitle>{t("governance.testing.runs.table.title", "Listado de Runs")}</CardTitle>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="text-center py-8">{t("common.loading", "Cargando...")}</div>
          ) : filteredRuns.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("governance.testing.runs.table.noResults", "No hay runs disponibles")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b">
                    <th className="text-left p-2">Nombre</th>
                    <th className="text-left p-2">Experimento</th>
                    <th className="text-left p-2">Tipo</th>
                    <th className="text-left p-2">Entidad</th>
                    <th className="text-left p-2">Estado</th>
                    <th className="text-left p-2">Duración</th>
                    <th className="text-left p-2">Iniciado</th>
                    <th className="text-center p-2">Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredRuns.map((run) => (
                    <tr key={run.id} className="border-b hover:bg-muted/50">
                      <td className="p-2 font-medium">{run.name}</td>
                      <td className="p-2">
                        <div className="text-xs text-muted-foreground">{run.experimentName}</div>
                      </td>
                      <td className="p-2">
                        <Badge variant="outline">{run.runType}</Badge>
                      </td>
                      <td className="p-2">
                        <div className="flex items-center gap-2">
                          {getEntityTypeBadge(run.entityType)}
                          {run.entityName && (
                            <span className="text-xs text-muted-foreground">{run.entityName}</span>
                          )}
                        </div>
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
          )}
        </CardContent>
      </Card>
    </div>
  );
}
