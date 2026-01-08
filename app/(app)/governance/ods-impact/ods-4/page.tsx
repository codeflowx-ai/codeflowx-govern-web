"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  GraduationCap,
  RefreshCw,
  ArrowLeft,
  Code,
  Database,
} from "lucide-react";
import { GaugeChart } from "../components/GaugeChart";
import { KPICard } from "../components/KPICard";
import { type ODSImpact } from "../data/mockODSKPIs";
import { odsImpactService } from "../services/odsImpactService";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import { Bar, Line } from "react-chartjs-2";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

export default function ODS4Page() {
  const { t } = useTranslation();
  const [odsData, setOdsData] = useState<ODSImpact | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      const response = await odsImpactService.getODSData(4);
      if (response.success && response.data) {
        setOdsData(response.data);
      } else {
        console.error("Error loading ODS 4 data:", response.error);
      }
    } catch (error) {
      console.error("Error loading ODS 4 data:", error);
    } finally {
      setLoading(false);
    }
  };

  if (loading || !odsData) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
        <div className="relative z-10 container mx-auto px-4 py-6">
          <div className="flex items-center justify-center h-96">
            <RefreshCw className="h-8 w-8 animate-spin text-primary" />
          </div>
        </div>
      </div>
    );
  }

  // ✅ FACTIBLES (después de Fase 2):
  // - LLMs educativo: ORGORGANIZATIONS.ORGTYPE = 'EDUCATIONAL' + AIOCOMPONENTS (Fase 2)
  // ✅ FACTIBLES (después de Fase 2):
  // - LLMs educativo: ORGORGANIZATIONS.ORGTYPE = 'EDUCATIONAL' + AIOCOMPONENTS (Fase 2)
  // - Bases conocimiento: RAGPIPELINES.RAGEDUCATIONALUSE = true (Fase 2)

  // Mock data para gráficos (FACTIBLES)
  const llmUsageData = {
    labels: [
      t("compliance.odsImpact.openSource", "Open Source"),
      t("compliance.odsImpact.proprietary", "Propietarios"),
    ],
    datasets: [
      {
        label: t("compliance.odsImpact.chartLabels.usageEducational", "Uso en Instituciones Educativas (%)"),
        data: [42, 58],
        backgroundColor: [
          "rgba(34, 197, 94, 0.8)",
          "rgba(59, 130, 246, 0.8)",
        ],
      },
    ],
  };

  const llmAdoptionEvolutionData = {
    labels: [
      "Ene",
      "Feb",
      "Mar",
      "Abr",
      "May",
      "Jun",
      "Jul",
      "Ago",
      "Sep",
      "Oct",
      "Nov",
      "Dic",
    ],
    datasets: [
      {
        label: t("compliance.odsImpact.chartLabels.educationalLLMUsage", "Tasa de Uso Educativo de LLMs Open Source (%)"),
        data: [30, 32, 35, 37, 38, 40, 41, 41, 42, 42, 42, 42],
        borderColor: "rgb(34, 197, 94)",
        backgroundColor: "rgba(34, 197, 94, 0.1)",
        fill: true,
        tension: 0.4,
      },
    ],
  };

  const knowledgeBasesEvolutionData = {
    labels: [
      "Ene",
      "Feb",
      "Mar",
      "Abr",
      "May",
      "Jun",
      "Jul",
      "Ago",
      "Sep",
      "Oct",
      "Nov",
      "Dic",
    ],
    datasets: [
      {
        label: t("compliance.odsImpact.chartLabels.educationalKnowledgeBases", "Número de Bases de Conocimiento Educativas"),
        data: [60, 62, 65, 67, 68, 70, 72, 73, 74, 75, 75, 75],
        borderColor: "rgb(59, 130, 246)",
        backgroundColor: "rgba(59, 130, 246, 0.1)",
        fill: true,
        tension: 0.4,
      },
    ],
  };

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: "top" as const,
      },
    },
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <Button
              variant="ghost"
              size="sm"
              onClick={() => {
                window.location.href = "/governance/ods-impact";
              }}
            >
              <ArrowLeft className="h-5 w-5" />
            </Button>
            <div>
              <div className="flex items-center gap-3 mb-2">
                <GraduationCap className="w-8 h-8 text-blue-500" />
                <h1 className="text-3xl font-bold bg-gradient-to-r from-blue-500 to-cyan-500 bg-clip-text text-transparent">
                  ODS 4: {odsData.odsName}
                </h1>
              </div>
              <p className="text-muted-foreground">
                {t(
                  "compliance.odsImpact.ods4.description",
                  "Educación de Calidad - 2 KPIs principales"
                )}
              </p>
            </div>
          </div>
          <Button onClick={loadData} variant="outline" size="sm">
            <RefreshCw className="h-4 w-4 mr-2" />
            {t("common.refresh", "Actualizar")}
          </Button>
        </div>

        {/* Sección 1: KPIs Principales */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {odsData.kpis.map((kpi) => (
            <div key={kpi.code} className="flex flex-col">
              <div className="flex-1 mb-4">
                <GaugeChart
                  value={kpi.value}
                  max={kpi.unit === "%" ? 100 : kpi.meta * 1.2}
                  label={kpi.name}
                  meta={kpi.meta}
                  criticalThreshold={kpi.criticalThreshold}
                />
              </div>
              <KPICard {...kpi} />
            </div>
          ))}
        </div>

        {/* Sección 2: Uso Educativo */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Code className="h-5 w-5 text-blue-500" />
              {t("compliance.odsImpact.educationalUsage", "Uso Educativo de IA")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              <div>
                <h3 className="text-sm font-medium mb-4">
                  Uso de LLMs Open Source en Instituciones Educativas
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Calculado desde ORGORGANIZATIONS.ORGTYPE = 'EDUCATIONAL' + AIOCOMPONENTS (Fase 2)
                </p>
                <div className="h-64">
                  <Bar data={llmUsageData} options={chartOptions} />
                </div>
              </div>
              <div>
                <h3 className="text-sm font-medium mb-4">
                  {t("compliance.odsImpact.llmAdoptionEvolution", "Evolución de Adopción de LLMs Open Source")}
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Tendencias mensuales de uso educativo
                </p>
                <div className="h-64">
                  <Line data={llmAdoptionEvolutionData} options={chartOptions} />
                </div>
              </div>
            </div>
            <div className="mt-6">
              <h3 className="text-sm font-medium mb-4">
                {t("compliance.odsImpact.educationalKnowledgeBasesEvolution", "Evolución de Bases de Conocimiento Educativas")}
              </h3>
              <p className="text-xs text-muted-foreground mb-4">
                Calculado desde RAGPIPELINES.RAGEDUCATIONALUSE = true (Fase 2)
              </p>
              <div className="h-64">
                <Line data={knowledgeBasesEvolutionData} options={chartOptions} />
              </div>
            </div>
          </CardBody>
        </Card>
      </div>
    </div>
  );
}
