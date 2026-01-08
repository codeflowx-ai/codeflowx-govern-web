"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  Zap,
  RefreshCw,
  ArrowLeft,
  TrendingDown,
} from "lucide-react";
import { GaugeChart } from "../components/GaugeChart";
import { KPICard } from "../components/KPICard";
import { type ODSImpact } from "../data/mockODSKPIs";
import { odsImpactService } from "../services/odsImpactService";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import { Line } from "react-chartjs-2";

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

export default function ODS7Page() {
  const { t } = useTranslation();
  const [odsData, setOdsData] = useState<ODSImpact | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      const response = await odsImpactService.getODSData(7);
      if (response.success && response.data) {
        setOdsData(response.data);
      } else {
        console.error("Error loading ODS 7 data:", response.error);
      }
    } catch (error) {
      console.error("Error loading ODS 7 data:", error);
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
  // - Reducción de Consumo Energético: TELRESOURCETELEMETRY.TELENERGYREDUCTIONPERCENTAGE (Fase 2)

  // Mock data para gráficos (FACTIBLES)
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
        data: [15, 17, 18, 19, 20, 21, 21, 22, 22, 22, 22, 22],
        borderColor: "rgb(234, 179, 8)",
        backgroundColor: "rgba(234, 179, 8, 0.1)",
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
                <Zap className="w-8 h-8 text-yellow-500" />
                <h1 className="text-3xl font-bold bg-gradient-to-r from-yellow-500 to-orange-500 bg-clip-text text-transparent">
                  ODS 7: {odsData.odsName}
                </h1>
              </div>
              <p className="text-muted-foreground">
                {t(
                  "compliance.odsImpact.ods7.description",
                  "Energía Asequible y No Contaminante - 1 KPI principal"
                )}
              </p>
            </div>
          </div>
          <Button onClick={loadData} variant="outline" size="sm">
            <RefreshCw className="h-4 w-4 mr-2" />
            {t("common.refresh", "Actualizar")}
          </Button>
        </div>

        {/* Sección 1: KPI Principal */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 max-w-2xl mx-auto">
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

        {/* Sección 2: Reducción de Consumo Energético */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <TrendingDown className="h-5 w-5 text-green-500" />
              {t("compliance.odsImpact.energyReduction", "Reducción de Consumo Energético por Optimización")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div>
              <h3 className="text-sm font-medium mb-4">
                {t("compliance.odsImpact.energyReductionEvolution", "Evolución de Reducción de Consumo Energético")}
              </h3>
              <p className="text-xs text-muted-foreground mb-4">
                Calculado desde TELRESOURCETELEMETRY.TELENERGYREDUCTIONPERCENTAGE (Fase 2)
              </p>
              <div className="h-64">
                <Line data={energyReductionData} options={chartOptions} />
              </div>
            </div>
            <div className="mt-6">
              <h3 className="text-sm font-medium mb-4">
                Optimizaciones Implementadas
              </h3>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {[
                  {
                    optimization: "Modelos Cuantizados",
                    energyReduction: 15,
                    modelsAffected: 25,
                  },
                  {
                    optimization: "Inferencia Optimizada",
                    energyReduction: 12,
                    modelsAffected: 18,
                  },
                  {
                    optimization: "Batch Processing",
                    energyReduction: 8,
                    modelsAffected: 12,
                  },
                  {
                    optimization: "Modelos Eficientes",
                    energyReduction: 10,
                    modelsAffected: 15,
                  },
                ].map((item, idx) => (
                  <Card key={idx} className="p-4">
                    <div className="font-medium mb-2">{item.optimization}</div>
                    <div className="text-sm text-muted-foreground space-y-1">
                      <div>Reducción: {item.energyReduction}%</div>
                      <div>Modelos afectados: {item.modelsAffected}</div>
                    </div>
                  </Card>
                ))}
              </div>
            </div>
          </CardBody>
        </Card>
      </div>
    </div>
  );
}
