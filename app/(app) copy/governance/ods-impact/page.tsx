"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  Globe,
  TrendingUp,
  RefreshCw,
  CheckCircle2,
  AlertTriangle,
} from "lucide-react";
import { GaugeChart } from "./components/GaugeChart";
import { ODSCard } from "./components/ODSCard";
import { KPICard } from "./components/KPICard";
import {
  type ODSImpactDashboard,
} from "./data/mockODSKPIs";
import { odsImpactService } from "./services/odsImpactService";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  Filler,
} from "chart.js";
import { Line } from "react-chartjs-2";

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  Filler
);

export default function ODSImpactPage() {
  const { t } = useTranslation();
  const [dashboard, setDashboard] = useState<ODSImpactDashboard | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDashboard();
  }, []);

  const loadDashboard = async () => {
    try {
      setLoading(true);
      const response = await odsImpactService.getDashboard();
      if (response.success && response.data) {
        setDashboard(response.data);
      } else {
        console.error("Error loading ODS impact dashboard:", response.error);
      }
    } catch (error) {
      console.error("Error loading ODS impact dashboard:", error);
    } finally {
      setLoading(false);
    }
  };

  if (loading || !dashboard) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
        <div className="relative z-10 w-full px-6 py-6">
          <div className="flex items-center justify-center h-96">
            <RefreshCw className="h-8 w-8 animate-spin text-primary" />
          </div>
        </div>
      </div>
    );
  }

  // Datos para gráfico de evolución
  const evolutionData = {
    labels: dashboard.evolution.map((e) => e.date),
    datasets: [
      {
        label: t("compliance.odsImpact.chartLabels.overallImpactScore", "Score General de Impacto"),
        data: dashboard.evolution.map((e) => e.overallScore * 100),
        borderColor: "rgb(59, 130, 246)",
        backgroundColor: "rgba(59, 130, 246, 0.1)",
        fill: true,
        tension: 0.4,
      },
      {
        label: t("compliance.odsImpact.chartLabels.complianceRate", "Tasa de Cumplimiento"),
        data: dashboard.evolution.map((e) => e.complianceRate * 100),
        borderColor: "rgb(34, 197, 94)",
        backgroundColor: "rgba(34, 197, 94, 0.1)",
        fill: true,
        tension: 0.4,
      },
    ],
  };

  const evolutionOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: "top" as const,
      },
      title: {
        display: true,
        text: t("compliance.odsImpact.evolution", "Evolución Temporal (Últimos 12 meses)"),
      },
    },
    scales: {
      y: {
        beginAtZero: true,
        max: 100,
        ticks: {
          callback: function (value: any) {
            return value + "%";
          },
        },
      },
    },
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      {/* Partículas flotantes */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-2 h-2 bg-green-400/30 rounded-full animate-pulse delay-300" />
      </div>

      <div className="relative z-10 w-full px-6 py-6 space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div>
            <div className="flex items-center gap-3 mb-2">
              <Globe className="w-8 h-8 text-blue-500" />
              <h1 className="text-3xl font-bold bg-gradient-to-r from-blue-500 to-green-500 bg-clip-text text-transparent">
                {t("compliance.odsImpact.title", "Impacto ODS")}
              </h1>
            </div>
            <p className="text-muted-foreground">
              {t(
                "compliance.odsImpact.subtitle",
                "Medición del impacto de la plataforma en los Objetivos de Desarrollo Sostenible"
              )}
            </p>
          </div>
          <Button onClick={loadDashboard} variant="outline" size="sm">
            <RefreshCw className="h-4 w-4 mr-2" />
            {t("common.refresh", "Actualizar")}
          </Button>
        </div>

        {/* Sección 1: KPIs Compuestos */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <TrendingUp className="h-5 w-5 text-blue-500" />
                {dashboard.compositeKPIs.overallImpact.name}
              </CardTitle>
            </CardHeader>
            <CardBody>
              <div className="flex items-center justify-center mb-4">
                <GaugeChart
                  value={dashboard.compositeKPIs.overallImpact.value * 100}
                  max={100}
                  label={t("compliance.odsImpact.scoreGeneral", "Score General")}
                  meta={dashboard.compositeKPIs.overallImpact.meta * 100}
                />
              </div>
              <div className="space-y-2">
                <div className="text-sm font-medium">
                  {t("compliance.odsImpact.components", "Componentes")}:
                </div>
                {dashboard.compositeKPIs.overallImpact.components.map(
                  (comp, idx) => (
                    <div
                      key={idx}
                      className="flex items-center justify-between text-xs"
                    >
                      <span className="text-muted-foreground">
                        {comp.name} ({comp.weight * 100}%)
                      </span>
                      <span className="font-medium">
                        {(comp.value * 100).toFixed(1)}%
                      </span>
                    </div>
                  )
                )}
              </div>
            </CardBody>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <CheckCircle2 className="h-5 w-5 text-green-500" />
                {dashboard.compositeKPIs.complianceRate.name}
              </CardTitle>
            </CardHeader>
            <CardBody>
              <div className="flex items-center justify-center mb-4">
                <GaugeChart
                  value={dashboard.compositeKPIs.complianceRate.value * 100}
                  max={100}
                  label={t("compliance.odsImpact.compliance", "Cumplimiento")}
                  meta={dashboard.compositeKPIs.complianceRate.meta * 100}
                />
              </div>
              <div className="space-y-2">
                <div className="text-sm font-medium">
                  {t("compliance.odsImpact.modules", "Módulos")}:
                </div>
                {dashboard.compositeKPIs.complianceRate.components.map(
                  (comp, idx) => (
                    <div
                      key={idx}
                      className="flex items-center justify-between text-xs"
                    >
                      <span className="text-muted-foreground">
                        {comp.name} ({comp.weight * 100}%)
                      </span>
                      <span className="font-medium">
                        {(comp.value * 100).toFixed(1)}%
                      </span>
                    </div>
                  )
                )}
              </div>
            </CardBody>
          </Card>
        </div>

        {/* Sección 2: Impacto por ODS */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle>
              {t("compliance.odsImpact.odsImpact", "Impacto por ODS")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-4">
              {dashboard.odsImpacts.map((ods) => (
                <ODSCard
                  key={ods.odsNumber}
                  odsNumber={ods.odsNumber}
                  odsName={ods.odsName}
                  score={ods.score}
                  trend={ods.trend}
                  modulesCount={ods.modulesCount}
                  status={ods.status}
                  href={`/governance/ods-impact/ods-${ods.odsNumber}`}
                />
              ))}
            </div>
          </CardBody>
        </Card>

        {/* Sección 3: Top 5 KPIs Críticos */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <AlertTriangle className="h-5 w-5 text-yellow-500" />
              {t("compliance.odsImpact.topCriticalKPIs", "Top 5 KPIs Críticos")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {dashboard.topCriticalKPIs.map((kpi) => (
                <KPICard key={kpi.code} {...kpi} />
              ))}
            </div>
          </CardBody>
        </Card>

        {/* Sección 4: Evolución Temporal */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle>
              {t(
                "compliance.odsImpact.evolution",
                "Evolución Temporal (Últimos 12 meses)"
              )}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="h-64">
              <Line data={evolutionData} options={evolutionOptions} />
            </div>
          </CardBody>
        </Card>
      </div>
    </div>
  );
}


