"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  Recycle,
  RefreshCw,
  ArrowLeft,
  TrendingUp,
  Zap,
  Cpu,
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

export default function ODS12Page() {
  const { t } = useTranslation();
  const [odsData, setOdsData] = useState<ODSImpact | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      const response = await odsImpactService.getODSData(12);
      if (response.success && response.data) {
        setOdsData(response.data);
      } else {
        console.error("Error loading ODS 12 data:", response.error);
      }
    } catch (error) {
      console.error("Error loading ODS 12 data:", error);
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
  // - Reutilización de Modelos: srvdeployment (relación Project-Model)
  // - Consumo Energético: TELRESOURCETELEMETRY (Fase 2)
  // - Modelos Eficientes: MODMODELS.MODOPTIMIZED (Fase 2)

  // Mock data para gráficos (FACTIBLES)
  const modelReuseEvolutionData = {
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
        label: t("compliance.odsImpact.chartLabels.reuseRate", "Tasa de Reutilización (%)"),
        data: [40, 42, 44, 45, 46, 47, 47, 48, 48, 48, 48, 48],
        borderColor: "rgb(34, 197, 94)",
        backgroundColor: "rgba(34, 197, 94, 0.1)",
        fill: true,
        tension: 0.4,
      },
    ],
  };

  const energyReductionData = {
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
        label: t("compliance.odsImpact.chartLabels.energyReduction", "Reducción de Consumo Energético (%)"),
        data: [12, 14, 15, 16, 17, 17, 18, 18, 18, 18, 18, 18],
        borderColor: "rgb(234, 179, 8)",
        backgroundColor: "rgba(234, 179, 8, 0.1)",
        fill: true,
        tension: 0.4,
      },
    ],
  };

  const efficientModelsUsageData = {
    labels: [
      t("compliance.odsImpact.statusLabels.efficient", "Modelos Eficientes"),
      t("compliance.odsImpact.statusLabels.nonOptimized", "Modelos No Optimizados"),
    ],
    datasets: [
      {
        label: t("compliance.odsImpact.chartLabels.usage", "Uso (%)"),
        data: [58, 42],
        backgroundColor: [
          "rgba(34, 197, 94, 0.8)",
          "rgba(234, 179, 8, 0.8)",
        ],
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
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-yellow-400/30 rounded-full animate-pulse" />
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
                <Recycle className="w-8 h-8 text-yellow-500" />
                <h1 className="text-3xl font-bold bg-gradient-to-r from-yellow-500 to-orange-500 bg-clip-text text-transparent">
                  ODS 12: {odsData.odsName}
                </h1>
              </div>
              <p className="text-muted-foreground">
                {t(
                  "compliance.odsImpact.ods12.description",
                  "Producción y Consumo Responsables - 3 KPIs principales"
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
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {odsData.kpis.map((kpi) => (
            <div key={kpi.code} className="flex flex-col">
              <div className="flex-1 mb-4">
                <GaugeChart
                  value={kpi.value}
                  max={kpi.unit === "%" ? 100 : kpi.meta * 1.2}
                  label={kpi.name}
                  meta={kpi.meta}
                  criticalThreshold={kpi.criticalThreshold}
                  size="md"
                />
              </div>
              <KPICard {...kpi} />
            </div>
          ))}
        </div>

        {/* Sección 2: Reutilización y Eficiencia */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Recycle className="h-5 w-5 text-green-500" />
              {t("compliance.odsImpact.reuseEfficiency", "Reutilización y Eficiencia")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              <div>
                <h3 className="text-sm font-medium mb-4">
                  {t("compliance.odsImpact.modelReuseEvolution", "Evolución de Reutilización de Modelos")}
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Calculado desde srvdeployment (modelos desplegados en múltiples proyectos)
                </p>
                <div className="h-64">
                  <Line data={modelReuseEvolutionData} options={chartOptions} />
                </div>
              </div>
              <div>
                <h3 className="text-sm font-medium mb-4">
                  Reducción de Consumo Energético
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Calculado desde TELRESOURCETELEMETRY.TELENERGYREDUCTIONPERCENTAGE (Fase 2)
                </p>
                <div className="h-64">
                  <Line data={energyReductionData} options={chartOptions} />
                </div>
              </div>
            </div>
            <div className="mt-6">
              <h3 className="text-sm font-medium mb-4">
                Uso de Modelos Eficientes vs. No Optimizados
              </h3>
              <p className="text-xs text-muted-foreground mb-4">
                Calculado desde MODMODELS.MODOPTIMIZED (Fase 2)
              </p>
              <div className="h-64">
                <Bar data={efficientModelsUsageData} options={chartOptions} />
              </div>
            </div>
          </CardBody>
        </Card>
      </div>
    </div>
  );
}
