"use client";
import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { ArrowLeft, Eye, Download, Clock, CheckCircle, XCircle, AlertCircle, FlaskConical } from "lucide-react";
import { useEffect, useState } from "react";
import { useRouter, useParams } from "next/navigation";

interface Run {
  id: number;
  experimentId: number;
  experimentName: string;
  name: string;
  description: string;
  runType: string;
  entityType: string;
  entityId: number;
  entityName?: string;
  status: "PENDING" | "RUNNING" | "COMPLETED" | "FAILED" | "CANCELLED";
  startedAt?: string;
  endedAt?: string;
  duration?: number;
  metrics?: Record<string, any>;
  parameters?: Record<string, any>;
  tags?: Record<string, string>;
  artifacts?: Array<{ path: string; type: string; size?: number; mimeType?: string }>;
  error?: string;
  errorDetails?: Record<string, any>;
}

export default function RunDetailPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const params = useParams();
  const runId = params.id as string;
  const [run, setRun] = useState<Run | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, [runId]);

  const loadData = async () => {
    try {
      setLoading(true);
      // Mock data
      const mockRun: Run = {
        id: parseInt(runId),
        experimentId: 1,
        experimentName: "Bias Analysis Experiments for Model #1",
        name: "Bias Analysis Run - gender",
        description: "Análisis de sesgo por género",
        runType: "BIAS_ANALYSIS",
        entityType: "MODEL",
        entityId: 1,
        entityName: "GPT-4 Fine-tuned Classification",
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
          testDatasetFile: "test_dataset.csv",
        },
        tags: {
          analysisType: "BIAS_ANALYSIS",
          modelId: "1",
          version: "1.0.0",
        },
        artifacts: [
          {
            path: "s3://codeflowx-artifacts/runs/1/report.pdf",
            type: "report",
            size: 2048000,
            mimeType: "application/pdf",
          },
          {
            path: "s3://codeflowx-artifacts/runs/1/analysis.json",
            type: "data",
            size: 512000,
            mimeType: "application/json",
          },
        ],
      };

      setRun(mockRun);
    } catch (error) {
      console.error("Error loading run:", error);
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

  const formatFileSize = (bytes?: number) => {
    if (!bytes) return "-";
    if (bytes < 1024) return `${bytes} B`;
    if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(2)} KB`;
    return `${(bytes / (1024 * 1024)).toFixed(2)} MB`;
  };

  if (loading) {
    return <div className="text-center py-8">{t("common.loading", "Cargando...")}</div>;
  }

  if (!run) {
    return <div className="text-center py-8">{t("governance.testing.runs.notFound", "Run no encontrado")}</div>;
  }

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
              <FlaskConical className="w-6 h-6 text-primary" />
              <h1 className="text-2xl font-bold text-foreground">{run.name}</h1>
            </div>
            <p className="text-sm text-muted-foreground ml-9">{run.description}</p>
          </div>
        </div>
        <div className="flex items-center gap-2">
          {getStatusBadge(run.status)}
          <Button
            variant="outline"
            onClick={() => router.push(`/governance/testing/experiments/${run.experimentId}`)}
          >
            <Eye className="w-4 h-4 mr-2" />
            Ver Experimento
          </Button>
        </div>
      </div>

      {/* Información General */}
      <Card className="border-2">
        <CardHeader>
          <CardTitle>{t("governance.testing.runs.detail.info", "Información General")}</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div>
              <label className="text-sm font-medium text-muted-foreground">Experimento</label>
              <p className="text-base font-semibold">{run.experimentName}</p>
            </div>
            <div>
              <label className="text-sm font-medium text-muted-foreground">Tipo de Run</label>
              <p className="text-base">
                <Badge variant="outline">{run.runType}</Badge>
              </p>
            </div>
            <div>
              <label className="text-sm font-medium text-muted-foreground">Entidad</label>
              <p className="text-base">{run.entityName || `ID: ${run.entityId}`}</p>
            </div>
            <div>
              <label className="text-sm font-medium text-muted-foreground">Duración</label>
              <p className="text-base font-semibold">{formatDuration(run.duration)}</p>
            </div>
            <div>
              <label className="text-sm font-medium text-muted-foreground">Iniciado</label>
              <p className="text-base text-sm">
                {run.startedAt ? new Date(run.startedAt).toLocaleString() : "-"}
              </p>
            </div>
            <div>
              <label className="text-sm font-medium text-muted-foreground">Finalizado</label>
              <p className="text-base text-sm">
                {run.endedAt ? new Date(run.endedAt).toLocaleString() : "-"}
              </p>
            </div>
          </div>
          {run.tags && Object.keys(run.tags).length > 0 && (
            <div className="mt-4">
              <label className="text-sm font-medium text-muted-foreground mb-2 block">Tags</label>
              <div className="flex flex-wrap gap-2">
                {Object.entries(run.tags).map(([key, value]) => (
                  <Badge key={key} variant="outline">
                    {key}: {value}
                  </Badge>
                ))}
              </div>
            </div>
          )}
        </CardContent>
      </Card>

      {/* Tabs: Métricas, Parámetros, Artefactos */}
      <Tabs defaultValue="metrics" className="space-y-4">
        <TabsList>
          <TabsTrigger value="metrics">Métricas</TabsTrigger>
          <TabsTrigger value="parameters">Parámetros</TabsTrigger>
          <TabsTrigger value="artifacts">Artefactos</TabsTrigger>
          {run.error && <TabsTrigger value="error">Error</TabsTrigger>}
        </TabsList>

        <TabsContent value="metrics" className="space-y-4">
          <Card className="border-2">
            <CardHeader>
              <CardTitle>Métricas del Run</CardTitle>
            </CardHeader>
            <CardContent>
              {run.metrics && Object.keys(run.metrics).length > 0 ? (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                  {Object.entries(run.metrics).map(([key, value]) => (
                    <div key={key} className="p-3 border rounded-lg">
                      <label className="text-xs font-medium text-muted-foreground block mb-1">
                        {key.replace(/_/g, " ").toUpperCase()}
                      </label>
                      <p className="text-lg font-semibold">
                        {typeof value === "number" ? value.toFixed(4) : String(value)}
                      </p>
                    </div>
                  ))}
                </div>
              ) : (
                <div className="text-center py-8 text-muted-foreground">No hay métricas disponibles</div>
              )}
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="parameters" className="space-y-4">
          <Card className="border-2">
            <CardHeader>
              <CardTitle>Parámetros de Configuración</CardTitle>
            </CardHeader>
            <CardContent>
              {run.parameters && Object.keys(run.parameters).length > 0 ? (
                <div className="space-y-2">
                  {Object.entries(run.parameters).map(([key, value]) => (
                    <div key={key} className="flex items-center justify-between p-2 border rounded">
                      <span className="text-sm font-medium">{key.replace(/([A-Z])/g, " $1").trim()}</span>
                      <span className="text-sm text-muted-foreground">
                        {typeof value === "object" ? JSON.stringify(value) : String(value)}
                      </span>
                    </div>
                  ))}
                </div>
              ) : (
                <div className="text-center py-8 text-muted-foreground">No hay parámetros disponibles</div>
              )}
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="artifacts" className="space-y-4">
          <Card className="border-2">
            <CardHeader>
              <CardTitle>Artefactos</CardTitle>
            </CardHeader>
            <CardContent>
              {run.artifacts && run.artifacts.length > 0 ? (
                <div className="space-y-2">
                  {run.artifacts.map((artifact, idx) => (
                    <div
                      key={idx}
                      className="flex items-center justify-between p-3 border rounded-lg hover:bg-muted/50"
                    >
                      <div className="flex-1">
                        <div className="font-medium">{artifact.path.split("/").pop()}</div>
                        <div className="text-xs text-muted-foreground mt-1">
                          {artifact.type} • {formatFileSize(artifact.size)} • {artifact.mimeType}
                        </div>
                      </div>
                      <Button variant="outline" size="sm">
                        <Download className="w-4 h-4 mr-2" />
                        Descargar
                      </Button>
                    </div>
                  ))}
                </div>
              ) : (
                <div className="text-center py-8 text-muted-foreground">No hay artefactos disponibles</div>
              )}
            </CardContent>
          </Card>
        </TabsContent>

        {run.error && (
          <TabsContent value="error" className="space-y-4">
            <Card className="border-2 border-red-500/50">
              <CardHeader>
                <CardTitle className="text-red-400">Error del Run</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  <div>
                    <label className="text-sm font-medium text-muted-foreground mb-2 block">Mensaje de Error</label>
                    <p className="text-base text-red-400">{run.error}</p>
                  </div>
                  {run.errorDetails && Object.keys(run.errorDetails).length > 0 && (
                    <div>
                      <label className="text-sm font-medium text-muted-foreground mb-2 block">Detalles</label>
                      <pre className="p-3 bg-muted rounded text-xs overflow-auto">
                        {JSON.stringify(run.errorDetails, null, 2)}
                      </pre>
                    </div>
                  )}
                </div>
              </CardContent>
            </Card>
          </TabsContent>
        )}
      </Tabs>
    </div>
  );
}
