"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  Users,
  RefreshCw,
  ArrowLeft,
  Database,
  Shield,
  TrendingUp,
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

export default function ODS5Page() {
  const { t } = useTranslation();
  const [odsData, setOdsData] = useState<ODSImpact | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      const response = await odsImpactService.getODSData(5);
      if (response.success && response.data) {
        setOdsData(response.data);
      } else {
        console.error("Error loading ODS 5 data:", response.error);
      }
    } catch (error) {
      console.error("Error loading ODS 5 data:", error);
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

  // Mock data para gráficos (SOLO FACTIBLES)
  // ✅ FACTIBLES:
  // - Evaluación de sesgos de género desde FRIAFUNDAMENTALRIGHTSASSESSMENTS.FRARISKS (JSONB)
  // - Datasets balanceados desde DSDDATASETS.DSDGENDERBALANCED (Fase 1)
  // - Representación género desde DSDDATASETS.DSDGENDERBALANCESCORE (Fase 1)
  // ✅ FACTIBLE: Distribución de género en datasets (DSDDATASETS.DSDGENDERDISTRIBUTION)
  const genderDistributionData = {
    labels: [
      t("compliance.odsImpact.statusLabels.male", "Masculino"),
      t("compliance.odsImpact.statusLabels.female", "Femenino"),
      t("compliance.odsImpact.statusLabels.nonBinary", "No binario"),
      t("compliance.odsImpact.statusLabels.other", "Otros"),
    ],
    datasets: [
      {
        label: t("compliance.odsImpact.chartLabels.distribution", "Distribución (%)"),
        data: [48, 45, 4, 3],
        backgroundColor: [
          "rgba(59, 130, 246, 0.8)",
          "rgba(236, 72, 153, 0.8)",
          "rgba(168, 85, 247, 0.8)",
          "rgba(34, 197, 94, 0.8)",
        ],
      },
    ],
  };

  // ✅ FACTIBLE: Score de representación de género (DSDDATASETS.DSDGENDERBALANCESCORE)
  const genderRepresentationScoreData = {
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
        label: t("compliance.odsImpact.chartLabels.genderRepresentationScore", "Score de Representación de Género"),
        data: [0.82, 0.83, 0.84, 0.84, 0.85, 0.85, 0.85, 0.86, 0.86, 0.86, 0.86, 0.86],
        borderColor: "rgb(236, 72, 153)",
        backgroundColor: "rgba(236, 72, 153, 0.1)",
        fill: true,
        tension: 0.4,
      },
    ],
  };

  // ✅ FACTIBLE: Datasets balanceados vs no balanceados (DSDDATASETS.DSDGENDERBALANCED)
  const datasetsBalancedData = {
    labels: [
      t("compliance.odsImpact.statusLabels.balanced", "Balanceados"),
      t("compliance.odsImpact.statusLabels.notBalanced", "No Balanceados"),
    ],
    datasets: [
      {
        label: "Datasets",
        data: [88, 12],
        backgroundColor: [
          "rgba(34, 197, 94, 0.8)",
          "rgba(234, 179, 8, 0.8)",
        ],
      },
    ],
  };

  const systemsEvaluatedData = {
    labels: ["Evaluados", "Pendientes"],
    datasets: [
      {
        label: "Sistemas",
        data: [85, 15],
        backgroundColor: [
          "rgba(34, 197, 94, 0.8)",
          "rgba(234, 179, 8, 0.8)",
        ],
      },
    ],
  };

  const mitigationMeasuresData = {
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
        label: "Medidas de Mitigación Implementadas",
        data: [12, 15, 18, 20, 22, 25, 28, 30, 32, 35, 38, 40],
        borderColor: "rgb(34, 197, 94)",
        backgroundColor: "rgba(34, 197, 94, 0.1)",
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
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-pink-400/30 rounded-full animate-pulse" />
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
                <Users className="w-8 h-8 text-pink-500" />
                <h1 className="text-3xl font-bold bg-gradient-to-r from-pink-500 to-purple-500 bg-clip-text text-transparent">
                  ODS 5: {odsData.odsName}
                </h1>
              </div>
              <p className="text-muted-foreground">
                {t(
                  "compliance.odsImpact.ods5.description",
                  "Igualdad de Género - 3 KPIs principales"
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

        {/* Sección 2: Datasets de Género */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Database className="h-5 w-5 text-pink-500" />
              {t("compliance.odsImpact.genderDatasets", "Análisis de Género en Datasets")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              <div>
                <h3 className="text-sm font-medium mb-4">
                  Distribución de Género en Datasets
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Calculado desde DSDDATASETS.DSDGENDERDISTRIBUTION (JSONB)
                </p>
                <div className="h-64">
                  <Bar data={genderDistributionData} options={chartOptions} />
                </div>
              </div>
              <div>
                <h3 className="text-sm font-medium mb-4">
                  Datasets Balanceados vs. No Balanceados
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Calculado desde DSDDATASETS.DSDGENDERBALANCED ({'>='} 40% para cada género)
                </p>
                <div className="h-64">
                  <Bar data={datasetsBalancedData} options={chartOptions} />
                </div>
              </div>
            </div>
            <div className="mt-6">
              <h3 className="text-sm font-medium mb-4">
                {t("compliance.odsImpact.genderRepresentationScoreEvolution", "Evolución del Score de Representación de Género")}
              </h3>
              <p className="text-xs text-muted-foreground mb-4">
                Calculado desde DSDDATASETS.DSDGENDERBALANCESCORE (promedio mensual)
              </p>
              <div className="h-64">
                <Line data={genderRepresentationScoreData} options={chartOptions} />
              </div>
            </div>
          </CardBody>
        </Card>

        {/* Sección 3: Evaluación de Sesgos */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Shield className="h-5 w-5 text-purple-500" />
              {t("compliance.odsImpact.biasEvaluation", "Evaluación de Sesgos")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              <div>
                <h3 className="text-sm font-medium mb-4">
                  {t("compliance.odsImpact.systemsEvaluatedVsPending", "Sistemas Evaluados vs. Pendientes")}
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Calculado desde FRIAFUNDAMENTALRIGHTSASSESSMENTS.FRARISKS (JSONB) donde genderBiasAssessed = true
                </p>
                <div className="h-64">
                  <Bar data={systemsEvaluatedData} options={chartOptions} />
                </div>
              </div>
              <div>
                <h3 className="text-sm font-medium mb-4">
                  Medidas de Mitigación Implementadas
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Calculado desde FRIAFUNDAMENTALRIGHTSASSESSMENTS.FRARISKS (JSONB) si contiene medidas de mitigación
                </p>
                <div className="h-64">
                  <Line data={mitigationMeasuresData} options={chartOptions} />
                </div>
              </div>
            </div>
            <div className="mt-6">
              <h3 className="text-sm font-medium mb-4">
                {t("compliance.odsImpact.systemsWithGenderBiasDetected", "Sistemas con Sesgos de Género Detectados")}
              </h3>
              <div className="space-y-2">
                {[
                  {
                    system: "AI Credit Scoring System",
                    project: "AI Credit System",
                    biasType: "Discriminación en scoring",
                    mitigation: "En proceso",
                    severity: "Alto",
                  },
                  {
                    system: "Hiring Assistant",
                    project: "HR System",
                    biasType: "Preferencia por género",
                    mitigation: "Completada",
                    severity: "Medio",
                  },
                ].map((item, idx) => (
                  <div
                    key={idx}
                    className="flex items-center justify-between p-3 border rounded-lg"
                  >
                    <div>
                      <div className="font-medium">{item.system}</div>
                      <div className="text-sm text-muted-foreground">
                        {item.project} | Tipo: {item.biasType} | Mitigación:{" "}
                        {item.mitigation}
                      </div>
                    </div>
                    <Badge
                      variant={
                        item.severity === "Alto"
                          ? "danger"
                          : item.severity === "Medio"
                          ? "primary"
                          : "outline"
                      }
                    >
                      {item.severity}
                    </Badge>
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
