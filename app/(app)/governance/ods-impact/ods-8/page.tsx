"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  Briefcase,
  RefreshCw,
  ArrowLeft,
  UserCheck,
  Clock,
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

export default function ODS8Page() {
  const { t } = useTranslation();
  const [odsData, setOdsData] = useState<ODSImpact | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      const response = await odsImpactService.getODSData(8);
      if (response.success && response.data) {
        setOdsData(response.data);
      } else {
        console.error("Error loading ODS 8 data:", response.error);
      }
    } catch (error) {
      console.error("Error loading ODS 8 data:", error);
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

  // Mock data para gráficos (FACTIBLES)
  // ✅ FACTIBLES: HITL desde GOVHITLDECISIONS + GOVHITLSUPERVISIONS
  const hitlStatusData = {
    labels: [
      t("compliance.odsImpact.statusLabels.withHitl", "Con HITL"),
      t("compliance.odsImpact.statusLabels.withoutHitl", "Sin HITL"),
    ],
    datasets: [
      {
        label: t("compliance.odsImpact.chartLabels.decisions", "Decisiones"),
        data: [98.5, 1.5],
        backgroundColor: [
          "rgba(34, 197, 94, 0.8)",
          "rgba(239, 68, 68, 0.8)",
        ],
      },
    ],
  };

  const hitlResponseTimeData = {
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
        label: t("compliance.odsImpact.chartLabels.averageTime", "Tiempo Promedio (horas)"),
        data: [4.0, 3.8, 3.6, 3.5, 3.4, 3.3, 3.3, 3.2, 3.2, 3.2, 3.2, 3.2],
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
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-green-400/30 rounded-full animate-pulse" />
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
                <Briefcase className="w-8 h-8 text-green-500" />
                <h1 className="text-3xl font-bold bg-gradient-to-r from-green-500 to-emerald-500 bg-clip-text text-transparent">
                  ODS 8: {odsData.odsName}
                </h1>
              </div>
              <p className="text-muted-foreground">
                {t(
                  "compliance.odsImpact.ods8.description",
                  "Trabajo Decente y Crecimiento Económico - 2 KPIs principales"
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

        {/* Sección 2: Supervisión Humana (HITL) */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <UserCheck className="h-5 w-5 text-cyan-500" />
              {t("compliance.odsImpact.hitl", "Supervisión Humana (HITL)")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              <div>
                <h3 className="text-sm font-medium mb-4">
                  Decisiones con/sin Supervisión Humana
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Calculado desde GOVHITLDECISIONS + GOVHITLSUPERVISIONS
                </p>
                <div className="h-64">
                  <Bar data={hitlStatusData} options={chartOptions} />
                </div>
              </div>
              <div>
                <h3 className="text-sm font-medium mb-4">
                  Tiempo Promedio de Respuesta HITL
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Calculado desde GOVHITLDECISIONS.HITLDECISIONDATE - GOVHITLSUPERVISIONS.HITLCREATEDAT
                </p>
                <div className="h-64">
                  <Line data={hitlResponseTimeData} options={chartOptions} />
                </div>
              </div>
            </div>
            <div className="mt-6">
              <h3 className="text-sm font-medium mb-4">
                {t("compliance.odsImpact.pendingSupervisionDecisions", "Decisiones Pendientes de Supervisión")}
              </h3>
              <div className="space-y-2">
                {[
                  {
                    type: "AGENT_APPROVAL",
                    entity: "Credit Scoring Agent",
                    createdAt: "2025-12-01T10:00:00Z",
                    timeRemaining: 2.5,
                  },
                  {
                    type: "PROMPT_REVIEW",
                    entity: "Customer Service Prompt",
                    createdAt: "2025-12-01T11:00:00Z",
                    timeRemaining: 3.5,
                  },
                ].map((decision, idx) => (
                  <div
                    key={idx}
                    className="flex items-center justify-between p-3 border rounded-lg"
                  >
                    <div>
                      <div className="font-medium">{decision.entity}</div>
                      <div className="text-sm text-muted-foreground">
                        Tipo: {decision.type} | Creado:{" "}
                        {new Date(decision.createdAt).toLocaleString()}
                      </div>
                    </div>
                    <div className="text-right">
                      <div className="text-sm font-medium">
                        {decision.timeRemaining}h restantes
                      </div>
                      <Badge variant="outline">Pendiente</Badge>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </CardBody>
        </Card>
      </div>
    </div>
  );
}
