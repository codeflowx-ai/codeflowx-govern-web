"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { ClipboardCheck, CheckCircle, AlertTriangle, TrendingUp, ArrowLeft } from "lucide-react";
import { useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";
import { mockQmsData, QmsData, QmsGap } from "../../data/mockQms";
import { BarChart, LineChart as LineChartComponent, MetricCard } from "@/components/ui/charts";

const DEFAULT_PROJECT_ID = 1;

export default function QmsPage() {
  const { t, language } = useTranslation();
  const searchParams = useSearchParams();
  const [data, setData] = useState<QmsData | null>(null);
  const [loading, setLoading] = useState(true);

  // Obtener projectId de la URL o usar valor por defecto
  const getProjectId = (): number => {
    // Primero intentar obtener de la URL
    const projectIdParam = searchParams.get("projectId");
    if (projectIdParam) {
      const parsed = parseInt(projectIdParam, 10);
      if (!isNaN(parsed)) {
        return parsed;
      }
    }

    // TODO: Si no está en la URL, intentar obtener del contexto de usuario/proyecto
    // Por ahora usar valor por defecto
    return DEFAULT_PROJECT_ID;
  };

  useEffect(() => {
    loadData();
  }, [searchParams]);

  const loadData = async () => {
    try {
      setLoading(true);

      // Obtener projectId de la URL o contexto
      const projectId = getProjectId();

      const response = await fetch(`/api/governance/compliance/qms?projectId=${projectId}`);
      if (!response.ok) {
        throw new Error("Failed to load QMS data");
      }

      const result = await response.json();
      if (result.success) {
        setData(result.data);
      } else {
        throw new Error(result.error || "Failed to load QMS data");
      }
    } catch (error) {
      console.error("Error loading QMS data:", error);
      // En caso de error, usar mock data como fallback
      setData(mockQmsData);
    } finally {
      setLoading(false);
    }
  };

  const getLocale = () => {
    const localeMap: Record<string, string> = {
      es: "es-ES",
      en: "en-US",
      fr: "fr-FR",
      de: "de-DE",
      it: "it-IT",
      pt: "pt-PT",
    };
    return localeMap[language] || "es-ES";
  };

  const getScoreColor = (score: number) => {
    if (score >= 0.85) return "text-green-600";
    if (score >= 0.70) return "text-yellow-600";
    return "text-red-600";
  };

  const getScoreBgColor = (score: number) => {
    if (score >= 0.85) return "bg-green-600";
    if (score >= 0.70) return "bg-yellow-600";
    return "bg-red-600";
  };

  const getSeverityBadge = (severity: string) => {
    switch (severity.toUpperCase()) {
      case "HIGH":
        return <Badge variant="danger">{t("governance.qms.severity.high", "Alta")}</Badge>;
      case "MEDIUM":
        return <Badge className="bg-yellow-500">{t("governance.qms.severity.medium", "Media")}</Badge>;
      default:
        return <Badge variant="outline">{t("governance.qms.severity.low", "Baja")}</Badge>;
    }
  };

  const getComplianceStatusBadge = (status: string) => {
    switch (status) {
      case "COMPLIANT":
        return <Badge className="bg-green-100 text-green-800 dark:bg-green-900/20 dark:text-green-300">{t("governance.qms.status.compliant", "Cumple")}</Badge>;
      case "PARTIAL":
        return <Badge className="bg-yellow-100 text-yellow-800 dark:bg-yellow-900/20 dark:text-yellow-300">{t("governance.qms.status.partial", "Parcial")}</Badge>;
      default:
        return <Badge variant="danger">{t("governance.qms.status.nonCompliant", "No Cumple")}</Badge>;
    }
  };

  if (loading || !data) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  // Calcular métricas después de verificar que data existe
  const compliantModules = data.modules?.filter((m) => m.score >= 0.80).length || 0;
  const totalModules = data.modules?.length || 0;

  return (
    <div className="min-h-screen w-full bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      {/* Partículas flotantes */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-indigo-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1.5 h-1.5 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1 h-1 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10 w-full">
      </div>

      <div className="relative z-10 w-full max-w-full px-4 py-6 space-y-6">
        {/* Header */}
        <div className="space-y-4">
          {/* Fila única: Botón volver, título/subtítulo, datos del proyecto y botones */}
          <div className="flex items-center justify-between gap-4">
            <div className="flex items-center gap-3">
              <Button
                variant="outline"
                onClick={() => window.location.href = "/governance/compliance/qms/projects"}
              >
                <ArrowLeft className="h-4 w-4 mr-2" />
                {t("common.back", "Volver")}
              </Button>
              <div className="flex items-center gap-3">
                <ClipboardCheck className="w-8 h-8 text-indigo-500" />
                <div>
                  <h1 className="text-3xl font-bold bg-gradient-to-r from-indigo-500 to-indigo-700 bg-clip-text text-transparent">
                    {t("governance.qms.title", "Sistema de Gestión de Calidad (QMS)")}
                  </h1>
                  <p className="text-muted-foreground text-sm mt-1">
                    {t("governance.qms.subtitle", "Art. 17 EU AI Act - Monitoreo de compliance por módulos")}
                  </p>
                </div>
              </div>
            </div>

            {/* Datos del proyecto en el centro */}
            <div className="flex items-center gap-6">
              <div className="text-center">
                <div className="text-sm text-muted-foreground">{t("governance.qms.projectName", "Proyecto")}</div>
                <div className="font-semibold text-lg">{data.projectName}</div>
              </div>
              <div className="text-center">
                <div className="text-sm text-muted-foreground">{t("governance.qms.overallScore", "Score Overall")}</div>
                <div className={`text-4xl font-bold ${getScoreColor(data.overallScore)}`}>
                  {(data.overallScore * 100).toFixed(0)}%
                </div>
              </div>
              <div className="text-center">
                <div className="text-sm text-muted-foreground">{t("governance.qms.status", "Estado")}</div>
                <div className="mt-1">{getComplianceStatusBadge(data.complianceStatus)}</div>
              </div>
            </div>

            <div className="flex gap-2">
              <Button variant="outline">{t("common.export", "Exportar")}</Button>
              <Button
                onClick={async () => {
                  try {
                    setLoading(true);
                    const projectId = getProjectId();
                    const response = await fetch("/api/governance/compliance/qms", {
                      method: "POST",
                      headers: {
                        "Content-Type": "application/json",
                      },
                      body: JSON.stringify({ projectId, action: "calculate" }),
                    });

                    if (response.ok) {
                      const result = await response.json();
                      if (result.success) {
                        setData(result.data);
                      }
                    }
                  } catch (error) {
                    console.error("Error calculating QMS score:", error);
                  } finally {
                    setLoading(false);
                  }
                }}
                disabled={loading}
              >
                <TrendingUp className="h-4 w-4 mr-2" />
                {t("common.update", "Actualizar")}
              </Button>
            </div>
          </div>
        </div>

        {/* Métricas Principales */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <MetricCard
            title={t("governance.qms.totalModules", "Total Módulos")}
            value={totalModules}
            description={t("governance.qms.modulesDescription", "13 módulos QMS")}
            icon={<ClipboardCheck className="w-5 h-5" />}
            color="blue"
          />
          <MetricCard
            title={t("governance.qms.compliantModules", "Módulos Cumpliendo")}
            value={compliantModules}
            description={`${((compliantModules / totalModules) * 100).toFixed(0)}% ${t("governance.qms.compliance", "cumplimiento")}`}
            icon={<CheckCircle className="w-5 h-5" />}
            color="green"
          />
          <MetricCard
            title={t("governance.qms.gapsDetected", "Gaps Detectados")}
            value={data.gaps.length}
            description={t("governance.qms.gapsDescription", "Requieren atención")}
            icon={<AlertTriangle className="w-5 h-5" />}
            color="red"
          />
          <MetricCard
            title={t("governance.qms.overallScore", "Score Overall")}
            value={`${(data.overallScore * 100).toFixed(0)}%`}
            description={t("governance.qms.scoreDescription", "Promedio de módulos")}
            icon={<TrendingUp className="w-5 h-5" />}
            color="purple"
          />
        </div>

        {/* Gráficos */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <div className="overflow-auto max-h-[500px]">
            <BarChart
              data={data.modules.map((m) => ({
                label: t(`governance.qms.moduleNames.${m.name}`, m.displayName),
                value: m.score * 100,
                color: m.score >= 0.85 ? "#10b981" : m.score >= 0.70 ? "#f59e0b" : "#ef4444",
              }))}
              title={t("governance.qms.scoresByModule", "Scores por Módulo")}
              description={t("governance.qms.scoresDescription", "Distribución de scores de los 13 módulos QMS")}
              height={400}
              showValues={true}
            />
          </div>
          {data.historicalScores && data.historicalScores.length > 0 && (
            <LineChartComponent
              data={data.historicalScores.map((h) => ({
                label: new Date(h.date).toLocaleDateString(getLocale(), { month: "short", day: "numeric" }),
                value: h.score * 100,
                color: "#6366f1",
              }))}
              title={t("governance.qms.scoreEvolution", "Evolución del Score")}
              description={t("governance.qms.evolutionDescription", "Tendencia del score overall en el tiempo")}
              height={200}
            />
          )}
        </div>

        {/* Gaps y Plan de Mejora */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <Card className="backdrop-blur-md bg-background/60 border-border/50 flex flex-col">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <AlertTriangle className="w-5 h-5 text-yellow-500" />
                {t("governance.qms.gaps", "Gaps Detectados")} ({data.gaps.length})
              </CardTitle>
            </CardHeader>
            <CardBody className="overflow-auto max-h-[500px] flex-1">
              <div className="space-y-3">
                {data.gaps.length === 0 ? (
                  <div className="text-center py-8 text-muted-foreground">
                    <CheckCircle className="w-12 h-12 mx-auto mb-2 text-green-500" />
                    <p>{t("governance.qms.noGaps", "No se detectaron gaps. Todos los módulos cumplen con el umbral.")}</p>
                  </div>
                ) : (
                  data.gaps.map((gap, idx) => (
                    <div key={idx} className="p-4 border rounded-lg backdrop-blur-sm bg-background/40 hover:bg-background/60 transition-colors">
                      <div className="flex items-center justify-between mb-2">
                        <Badge variant="outline">{t("governance.qms.module", "Módulo")}: {gap.module}</Badge>
                        {getSeverityBadge(gap.severity)}
                      </div>
                      <div className="text-sm mb-2">{gap.description}</div>
                      {gap.currentScore !== undefined && gap.targetScore !== undefined && (
                        <div className="flex items-center gap-4 text-xs text-muted-foreground mb-2">
                          <span>{t("governance.qms.currentScore", "Score Actual")}: {(gap.currentScore * 100).toFixed(0)}%</span>
                          <span>{t("governance.qms.targetScore", "Score Objetivo")}: {(gap.targetScore * 100).toFixed(0)}%</span>
                          {gap.gap !== undefined && (
                            <span className="text-red-500 font-semibold">
                              {t("governance.qms.gap", "Gap")}: {(gap.gap * 100).toFixed(1)}%
                            </span>
                          )}
                        </div>
                      )}
                      {gap.recommendedActions && gap.recommendedActions.length > 0 && (
                        <div className="mt-3 pt-3 border-t">
                          <div className="text-xs font-semibold mb-2">{t("governance.qms.recommendedActions", "Acciones Recomendadas")}:</div>
                          <ul className="list-disc list-inside space-y-1 text-xs text-muted-foreground">
                            {gap.recommendedActions.map((action, actionIdx) => (
                              <li key={actionIdx}>{action}</li>
                            ))}
                          </ul>
                        </div>
                      )}
                    </div>
                  ))
                )}
              </div>
            </CardBody>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 flex flex-col">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <TrendingUp className="w-5 h-5 text-indigo-500" />
                {t("governance.qms.improvementPlan", "Plan de Mejora")}
              </CardTitle>
            </CardHeader>
            <CardBody className="overflow-auto max-h-[500px] flex-1">
              <div className="space-y-3">
                {data.improvementPlan.length === 0 ? (
                  <div className="text-center py-8 text-muted-foreground">
                    <p>{t("governance.qms.noImprovements", "No hay acciones de mejora planificadas.")}</p>
                  </div>
                ) : (
                  data.improvementPlan.map((item, idx) => (
                    <div key={idx} className="p-4 border rounded-lg backdrop-blur-sm bg-background/40 hover:bg-background/60 transition-colors">
                      <div className="flex items-center justify-between mb-2">
                        <div className="flex items-center gap-2">
                          {getSeverityBadge(item.priority)}
                          <Badge variant="outline" className="text-xs">{item.module}</Badge>
                        </div>
                        <span className="text-xs text-muted-foreground">
                          {t("governance.qms.deadline", "Plazo")}: {new Date(item.deadline).toLocaleDateString(getLocale())}
                        </span>
                      </div>
                      <div className="text-sm font-medium">{item.action}</div>
                    </div>
                  ))
                )}
              </div>
            </CardBody>
          </Card>
        </div>
      </div>
    </div>
  );
}
