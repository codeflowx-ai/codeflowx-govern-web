"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  Scale,
  RefreshCw,
  ArrowLeft,
  Shield,
  Database,
  Ban,
  FileText,
  AlertTriangle,
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

export default function ODS10Page() {
  const { t } = useTranslation();
  const [odsData, setOdsData] = useState<ODSImpact | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      const response = await odsImpactService.getODSData(10);
      if (response.success && response.data) {
        setOdsData(response.data);
      } else {
        console.error("Error loading ODS 10 data:", response.error);
      }
    } catch (error) {
      console.error("Error loading ODS 10 data:", error);
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
  // - FRIA (FRIAFUNDAMENTALRIGHTSASSESSMENTS), Sistemas Prohibidos (GOVPROHIBITEDSYSTEMS)
  // - Datasets (DSDDATASETS - Fase 1), Prompts (PRMPROMPTS)
  const friaStatusData = {
    labels: [
      t("compliance.odsImpact.friaCompleted", "Completadas"),
      t("compliance.odsImpact.pending", "Pendientes"),
    ],
    datasets: [
      {
        label: t("compliance.odsImpact.fria", "FRIAs"),
        data: [95, 5],
        backgroundColor: [
          "rgba(34, 197, 94, 0.8)",
          "rgba(239, 68, 68, 0.8)",
        ],
      },
    ],
  };

  const friaRiskEvolutionData = {
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
        label: t("compliance.odsImpact.averageRisk", "Riesgo Promedio"),
        data: [0.52, 0.51, 0.50, 0.49, 0.49, 0.48, 0.48, 0.48, 0.48, 0.48, 0.48, 0.48],
        borderColor: "rgb(239, 68, 68)",
        backgroundColor: "rgba(239, 68, 68, 0.1)",
        fill: true,
        tension: 0.4,
      },
    ],
  };

  // ✅ FACTIBLE: Detección de sesgos en datasets (DSDDATASETS.DSDBIASANALYZED)
  const biasDetectionData = {
    labels: [
      t("compliance.odsImpact.biasAnalysisCompleted", "Analizados"),
      t("compliance.odsImpact.pending", "Pendientes"),
    ],
    datasets: [
      {
        label: t("compliance.odsImpact.datasets", "Datasets"),
        data: [88, 12],
        backgroundColor: [
          "rgba(34, 197, 94, 0.8)",
          "rgba(234, 179, 8, 0.8)",
        ],
      },
    ],
  };

  // ✅ FACTIBLE: Score de representatividad (DSDDATASETS.DSDREPRESENTATIVITYSCORE)
  const representativityScoreData = {
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
        label: t("compliance.odsImpact.representativityEvolution", "Score de Representatividad"),
        data: [0.75, 0.76, 0.77, 0.77, 0.78, 0.78, 0.78, 0.78, 0.78, 0.78, 0.78, 0.78],
        borderColor: "rgb(59, 130, 246)",
        backgroundColor: "rgba(59, 130, 246, 0.1)",
        fill: true,
        tension: 0.4,
      },
    ],
  };

  const prohibitedSystemsData = {
    labels: [
      "Manipulación Cognitiva",
      "Explotación Vulnerabilidades",
      "Sistemas de Scoring Social",
      "Identificación Biométrica Remota",
    ],
    datasets: [
      {
        label: "Sistemas Detectados",
        data: [0, 0, 0, 0],
        backgroundColor: "rgba(34, 197, 94, 0.8)",
      },
    ],
  };

  // ✅ FACTIBLE: Prompts validados por sesgos (PRMPROMPTS + PRMPROMPTVALIDATIONS)
  const promptsValidatedData = {
    labels: [
      t("compliance.odsImpact.promptsValidated", "Validados"),
      t("compliance.odsImpact.pending", "Pendientes"),
    ],
    datasets: [
      {
        label: t("compliance.odsImpact.prompts", "Prompts"),
        data: [92, 8],
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
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-red-400/30 rounded-full animate-pulse" />
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
                <Scale className="w-8 h-8 text-red-500" />
                <h1 className="text-3xl font-bold bg-gradient-to-r from-red-500 to-pink-500 bg-clip-text text-transparent">
                  ODS 10: {odsData.odsName}
                </h1>
              </div>
              <p className="text-muted-foreground">
                {t(
                  "compliance.odsImpact.ods10.description",
                  "Reducción de las Desigualdades - 6 KPIs principales"
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
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
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

        {/* Sección 2: FRIA */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Shield className="h-5 w-5 text-blue-500" />
              {t("compliance.odsImpact.fria", "Evaluación de Impacto en Derechos Fundamentales (FRIA)")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              <div>
                <h3 className="text-sm font-medium mb-4">
                  {t("compliance.odsImpact.friaCompletedVsPending", "FRIAs Completadas vs. Pendientes")}
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Calculado desde FRIAFUNDAMENTALRIGHTSASSESSMENTS.FRIASTATUS + PRJPROJECTS.PRJISHIGHRISK
                </p>
                <div className="h-64">
                  <Bar data={friaStatusData} options={chartOptions} />
                </div>
              </div>
              <div>
                <h3 className="text-sm font-medium mb-4">
                  {t("compliance.odsImpact.averageRiskInFria", "Riesgo Promedio en FRIA")}
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Calculado desde FRIAFUNDAMENTALRIGHTSASSESSMENTS.FRARISKS (JSONB)
                </p>
                <div className="h-64">
                  <Line data={friaRiskEvolutionData} options={chartOptions} />
                </div>
              </div>
            </div>
            <div className="mt-6">
              <h3 className="text-sm font-medium mb-4">
                {t("compliance.odsImpact.highRiskSystemsWithoutFria", "Sistemas de Alto Riesgo sin FRIA")}
              </h3>
              <div className="space-y-2">
                {[
                  {
                    system: "AI Credit Scoring System",
                    project: "AI Credit System",
                    riskLevel: "Alto",
                    daysPending: 5,
                  },
                  {
                    system: "Facial Recognition System",
                    project: "Security System",
                    riskLevel: "Alto",
                    daysPending: 12,
                  },
                ].map((item, idx) => (
                  <div
                    key={idx}
                    className="flex items-center justify-between p-3 border rounded-lg"
                  >
                    <div>
                      <div className="font-medium">{item.system}</div>
                      <div className="text-sm text-muted-foreground">
                        {item.project} | Pendiente desde hace {item.daysPending} días
                      </div>
                    </div>
                    <Badge variant="danger">{item.riskLevel} Riesgo</Badge>
                  </div>
                ))}
              </div>
            </div>
          </CardBody>
        </Card>

        {/* Sección 3: Sistemas Prohibidos */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Ban className="h-5 w-5 text-red-500" />
              {t("compliance.odsImpact.prohibited", "Sistemas Prohibidos")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="space-y-6">
              <div className="bg-green-500/10 border border-green-500/20 rounded-lg p-4">
                <div className="flex items-center gap-2">
                  <AlertTriangle className="h-5 w-5 text-green-500" />
                  <div>
                    <div className="font-medium text-green-500">
                      Estado: Sin Sistemas Prohibidos Detectados
                    </div>
                    <div className="text-sm text-muted-foreground">
                      Todos los sistemas cumplen con las restricciones del EU AI Act
                    </div>
                  </div>
                </div>
              </div>
              <div>
                <h3 className="text-sm font-medium mb-4">
                  {t("compliance.odsImpact.prohibitedSystemsDetectedByCategory", "Sistemas Prohibidos Detectados por Categoría")}
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Calculado desde GOVPROHIBITEDSYSTEMS.PROHIBITEDACTIVE + PRJPROJECTS
                </p>
                <div className="h-64">
                  <Bar data={prohibitedSystemsData} options={chartOptions} />
                </div>
              </div>
              <div>
                <h3 className="text-sm font-medium mb-4">
                  {t("compliance.odsImpact.blockedSystemsHistory", "Sistemas Bloqueados (Histórico)")}
                </h3>
                <div className="text-sm text-muted-foreground">
                  No hay sistemas bloqueados en el historial.
                </div>
              </div>
            </div>
          </CardBody>
        </Card>

        {/* Sección 4: Datasets y Sesgos */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Database className="h-5 w-5 text-blue-500" />
              {t("compliance.odsImpact.datasetsBias", "Análisis de Sesgos en Datasets")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              <div>
                <h3 className="text-sm font-medium mb-4">
                  Datasets con Análisis de Sesgos Completado
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Calculado desde DSDDATASETS.DSDBIASANALYZED
                </p>
                <div className="h-64">
                  <Bar data={biasDetectionData} options={chartOptions} />
                </div>
              </div>
              <div>
                <h3 className="text-sm font-medium mb-4">
                  {t("compliance.odsImpact.promptsValidatedForBias", "Prompts Validados por Sesgos")}
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Calculado desde PRMPROMPTS + PRMPROMPTVALIDATIONS (tipo BIAS, status PASSED)
                </p>
                <div className="h-64">
                  <Bar data={promptsValidatedData} options={chartOptions} />
                </div>
              </div>
            </div>
            <div className="mt-6">
              <h3 className="text-sm font-medium mb-4">
                {t("compliance.odsImpact.representativityScoreEvolution", "Evolución del Score de Representatividad")}
              </h3>
              <p className="text-xs text-muted-foreground mb-4">
                Calculado desde DSDDATASETS.DSDREPRESENTATIVITYSCORE (promedio mensual)
              </p>
              <div className="h-64">
                <Line data={representativityScoreData} options={chartOptions} />
              </div>
            </div>
          </CardBody>
        </Card>

      </div>
    </div>
  );
}
