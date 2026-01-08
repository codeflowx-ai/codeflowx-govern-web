"use client";
import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { ArrowLeft, GitCompare, CheckCircle, XCircle, AlertCircle, Clock } from "lucide-react";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

interface RunInfo {
  id: number;
  name: string;
  type: string;
  status: string;
  startedAt?: string;
  endedAt?: string;
  duration?: number;
}

interface Comparison {
  runIds: number[];
  runCount: number;
  metrics: Record<string, Record<string, any>>;
  parameters: Record<string, Record<string, any>>;
  runs: RunInfo[];
}

export default function CompareRunsPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [selectedRunIds, setSelectedRunIds] = useState<string>("");
  const [comparison, setComparison] = useState<Comparison | null>(null);
  const [loading, setLoading] = useState(false);

  const handleCompare = async () => {
    if (!selectedRunIds.trim()) {
      alert("Por favor ingresa al menos un ID de run");
      return;
    }

    try {
      setLoading(true);
      const runIds = selectedRunIds
        .split(",")
        .map((id) => parseInt(id.trim()))
        .filter((id) => !isNaN(id));

      if (runIds.length < 2) {
        alert("Por favor ingresa al menos 2 IDs de runs para comparar");
        return;
      }

      // Mock data
      const mockComparison: Comparison = {
        runIds,
        runCount: runIds.length,
        metrics: {
          "1": {
            bias_score: 0.45,
            demographic_parity_difference: 0.32,
            equal_opportunity_difference: 0.28,
            severity: "HIGH",
          },
          "2": {
            bias_score: 0.28,
            demographic_parity_difference: 0.18,
            equal_opportunity_difference: 0.22,
            severity: "MEDIUM",
          },
          "3": {
            bias_score: 0.15,
            demographic_parity_difference: 0.10,
            equal_opportunity_difference: 0.12,
            severity: "LOW",
          },
        },
        parameters: {
          "1": {
            protectedAttribute: "gender",
            favorableOutcome: "1",
            threshold: 0.8,
          },
          "2": {
            protectedAttribute: "age",
            favorableOutcome: "1",
            threshold: 0.8,
          },
          "3": {
            protectedAttribute: "race",
            favorableOutcome: "1",
            threshold: 0.8,
          },
        },
        runs: [
          {
            id: 1,
            name: "Bias Analysis Run - gender",
            type: "BIAS_ANALYSIS",
            status: "COMPLETED",
            startedAt: "2024-01-15T10:30:00",
            endedAt: "2024-01-15T10:35:00",
            duration: 300,
          },
          {
            id: 2,
            name: "Bias Analysis Run - age",
            type: "BIAS_ANALYSIS",
            status: "COMPLETED",
            startedAt: "2024-01-16T09:15:00",
            endedAt: "2024-01-16T09:20:00",
            duration: 300,
          },
          {
            id: 3,
            name: "Bias Analysis Run - race",
            type: "BIAS_ANALYSIS",
            status: "COMPLETED",
            startedAt: "2024-01-17T11:00:00",
            endedAt: "2024-01-17T11:05:00",
            duration: 300,
          },
        ],
      };

      setComparison(mockComparison);
    } catch (error) {
      console.error("Error comparing runs:", error);
      alert("Error al comparar runs");
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

  // Obtener todas las métricas únicas de todos los runs
  const getAllMetrics = () => {
    if (!comparison) return [];
    const allMetrics = new Set<string>();
    Object.values(comparison.metrics).forEach((runMetrics) => {
      Object.keys(runMetrics).forEach((key) => allMetrics.add(key));
    });
    return Array.from(allMetrics);
  };

  // Obtener todos los parámetros únicos de todos los runs
  const getAllParameters = () => {
    if (!comparison) return [];
    const allParams = new Set<string>();
    Object.values(comparison.parameters).forEach((runParams) => {
      Object.keys(runParams).forEach((key) => allParams.add(key));
    });
    return Array.from(allParams);
  };

  return (
    <div className="space-y-4">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-4">
          <Button variant="ghost" size="sm" onClick={() => router.push("/governance/testing/runs")}>
            <ArrowLeft className="w-4 h-4 mr-2" />
            {t("common.back", "Volver")}
          </Button>
          <div>
            <div className="flex items-center gap-3 mb-2">
              <GitCompare className="w-6 h-6 text-primary" />
              <h1 className="text-2xl font-bold text-foreground">
                {t("governance.testing.compare.title", "Comparar Runs")}
              </h1>
            </div>
            <p className="text-sm text-muted-foreground ml-9">
              {t("governance.testing.compare.subtitle", "Compara múltiples runs y visualiza diferencias")}
            </p>
          </div>
        </div>
      </div>

      {/* Selector de Runs */}
      <Card className="border-2">
        <CardHeader>
          <CardTitle>{t("governance.testing.compare.selectRuns", "Seleccionar Runs")}</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="flex gap-4">
            <Input
              placeholder="Ingresa IDs de runs separados por coma (ej: 1,2,3)"
              value={selectedRunIds}
              onChange={(e) => setSelectedRunIds(e.target.value)}
              className="flex-1"
            />
            <Button onClick={handleCompare} disabled={loading} className="bg-primary hover:bg-primary/90">
              <GitCompare className="w-4 h-4 mr-2" />
              {loading ? "Comparando..." : "Comparar"}
            </Button>
          </div>
          <p className="text-xs text-muted-foreground mt-2">
            Ingresa al menos 2 IDs de runs para comparar (separados por coma)
          </p>
        </CardContent>
      </Card>

      {comparison && (
        <>
          {/* Información de Runs */}
          <Card className="border-2">
            <CardHeader>
              <CardTitle>
                {t("governance.testing.compare.runsInfo", "Información de Runs")} ({comparison.runCount})
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="overflow-x-auto">
                <table className="w-full text-sm">
                  <thead>
                    <tr className="border-b">
                      <th className="text-left p-2">ID</th>
                      <th className="text-left p-2">Nombre</th>
                      <th className="text-left p-2">Tipo</th>
                      <th className="text-left p-2">Estado</th>
                      <th className="text-left p-2">Duración</th>
                      <th className="text-left p-2">Iniciado</th>
                      <th className="text-center p-2">Acciones</th>
                    </tr>
                  </thead>
                  <tbody>
                    {comparison.runs.map((run) => (
                      <tr key={run.id} className="border-b hover:bg-muted/50">
                        <td className="p-2 font-medium">{run.id}</td>
                        <td className="p-2">{run.name}</td>
                        <td className="p-2">
                          <Badge variant="outline">{run.type}</Badge>
                        </td>
                        <td className="p-2">{getStatusBadge(run.status)}</td>
                        <td className="p-2">{formatDuration(run.duration)}</td>
                        <td className="p-2 text-xs text-muted-foreground">
                          {run.startedAt ? new Date(run.startedAt).toLocaleString() : "-"}
                        </td>
                        <td className="p-2">
                          <div className="flex items-center justify-center">
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => router.push(`/governance/testing/runs/${run.id}`)}
                            >
                              Ver
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

          {/* Comparación de Métricas */}
          <Card className="border-2">
            <CardHeader>
              <CardTitle>{t("governance.testing.compare.metrics", "Comparación de Métricas")}</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="overflow-x-auto">
                <table className="w-full text-sm">
                  <thead>
                    <tr className="border-b">
                      <th className="text-left p-2">Métrica</th>
                      {comparison.runs.map((run) => (
                        <th key={run.id} className="text-center p-2">
                          Run #{run.id}
                        </th>
                      ))}
                    </tr>
                  </thead>
                  <tbody>
                    {getAllMetrics().map((metric) => (
                      <tr key={metric} className="border-b hover:bg-muted/50">
                        <td className="p-2 font-medium">{metric.replace(/_/g, " ").toUpperCase()}</td>
                        {comparison.runs.map((run) => {
                          const value = comparison.metrics[run.id.toString()]?.[metric];
                          return (
                            <td key={run.id} className="p-2 text-center">
                              {value !== undefined ? (
                                typeof value === "number" ? (
                                  <span className="font-semibold">{value.toFixed(4)}</span>
                                ) : (
                                  <Badge variant="outline">{String(value)}</Badge>
                                )
                              ) : (
                                <span className="text-muted-foreground">-</span>
                              )}
                            </td>
                          );
                        })}
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </CardContent>
          </Card>

          {/* Comparación de Parámetros */}
          <Card className="border-2">
            <CardHeader>
              <CardTitle>{t("governance.testing.compare.parameters", "Comparación de Parámetros")}</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="overflow-x-auto">
                <table className="w-full text-sm">
                  <thead>
                    <tr className="border-b">
                      <th className="text-left p-2">Parámetro</th>
                      {comparison.runs.map((run) => (
                        <th key={run.id} className="text-center p-2">
                          Run #{run.id}
                        </th>
                      ))}
                    </tr>
                  </thead>
                  <tbody>
                    {getAllParameters().map((param) => (
                      <tr key={param} className="border-b hover:bg-muted/50">
                        <td className="p-2 font-medium">{param.replace(/([A-Z])/g, " $1").trim()}</td>
                        {comparison.runs.map((run) => {
                          const value = comparison.parameters[run.id.toString()]?.[param];
                          return (
                            <td key={run.id} className="p-2 text-center">
                              {value !== undefined ? (
                                <Badge variant="outline">{String(value)}</Badge>
                              ) : (
                                <span className="text-muted-foreground">-</span>
                              )}
                            </td>
                          );
                        })}
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </CardContent>
          </Card>
        </>
      )}
    </div>
  );
}
