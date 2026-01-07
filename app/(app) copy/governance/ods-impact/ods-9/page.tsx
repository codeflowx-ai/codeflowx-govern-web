"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  Lightbulb,
  RefreshCw,
  ArrowLeft,
  TrendingUp,
  Recycle,
  Cpu,
  FileCode,
  Code,
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

export default function ODS9Page() {
  const { t } = useTranslation();
  const [odsData, setOdsData] = useState<ODSImpact | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      const response = await odsImpactService.getODSData(9);
      if (response.success && response.data) {
        setOdsData(response.data);
      } else {
        console.error("Error loading ODS 9 data:", response.error);
      }
    } catch (error) {
      console.error("Error loading ODS 9 data:", error);
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
  // - Documentación (GOVAIAACTTECHNICALDOCS), Certificación (COMCOMPLIANCEASSESSMENTS), QMS (GOVQUALITYMANAGEMENTSYSTEMS)
  // - Reutilización de Agentes (PRJAGENTS - Fase 2)
  // - Reutilización de Prompts (PRMPROMPTS)
  // - Ciclo MLOps (TRNTRAININGEXECUTIONS + srvdeployment)
  // - LLMs Open Source (AIOCOMPONENTS)

  // ✅ FACTIBLE: Reutilización de Agentes (PRJAGENTS - Fase 2)
  // Contar agentes usados en >1 proyecto
  const agentReuseData = {
    labels: ["Agentes", "Modelos", "Prompts"],
    datasets: [
      {
        label: t("compliance.odsImpact.chartLabels.reuseRate", "Tasa de Reutilización (%)"),
        data: [65, 48, 72],
        backgroundColor: [
          "rgba(59, 130, 246, 0.8)",
          "rgba(34, 197, 94, 0.8)",
          "rgba(234, 179, 8, 0.8)",
        ],
      },
    ],
  };

  const reuseEvolutionData = {
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
        label: "Agentes",
        data: [55, 58, 60, 62, 63, 64, 64, 65, 65, 65, 65, 65],
        borderColor: "rgb(59, 130, 246)",
        backgroundColor: "rgba(59, 130, 246, 0.1)",
        fill: true,
        tension: 0.4,
      },
      {
        label: "Modelos",
        data: [40, 42, 44, 45, 46, 47, 47, 48, 48, 48, 48, 48],
        borderColor: "rgb(34, 197, 94)",
        backgroundColor: "rgba(34, 197, 94, 0.1)",
        fill: true,
        tension: 0.4,
      },
      {
        label: "Prompts",
        data: [65, 68, 70, 71, 71, 72, 72, 72, 72, 72, 72, 72],
        borderColor: "rgb(234, 179, 8)",
        backgroundColor: "rgba(234, 179, 8, 0.1)",
        fill: true,
        tension: 0.4,
      },
    ],
  };

  const mlopsCycleTimeData = {
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
        label: t("compliance.odsImpact.chartLabels.averageTimeDays", "Tiempo Promedio (días)"),
        data: [8, 7.5, 7.2, 7, 6.8, 6.7, 6.6, 6.6, 6.5, 6.5, 6.5, 6.5],
        borderColor: "rgb(59, 130, 246)",
        backgroundColor: "rgba(59, 130, 246, 0.1)",
        fill: true,
        tension: 0.4,
      },
    ],
  };

  const modelsByStatusData = {
    labels: ["Entrenamiento", "Testing", "Producción"],
    datasets: [
      {
        label: "Modelos",
        data: [15, 25, 60],
        backgroundColor: [
          "rgba(234, 179, 8, 0.8)",
          "rgba(59, 130, 246, 0.8)",
          "rgba(34, 197, 94, 0.8)",
        ],
      },
    ],
  };

  const llmUsageData = {
    labels: ["Open Source", "Propietarios"],
    datasets: [
      {
        label: t("compliance.odsImpact.chartLabels.usage", "Uso (%)"),
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
        label: t("compliance.odsImpact.llmAdoption", "Adopción LLMs Open Source (%)"),
        data: [30, 32, 35, 37, 38, 40, 41, 41, 42, 42, 42, 42],
        borderColor: "rgb(34, 197, 94)",
        backgroundColor: "rgba(34, 197, 94, 0.1)",
        fill: true,
        tension: 0.4,
      },
    ],
  };

  // ✅ FACTIBLE: Score de Documentación por Proyecto
  // Calculado desde GOVAIAACTTECHNICALDOCS.TECHSECTIONS (JSONB)
  // Contar secciones completas / 11 (total secciones Anexo IV)
  const documentationScoreData = {
    labels: [
      "Proyecto A",
      "Proyecto B",
      "Proyecto C",
      "Proyecto D",
      "Proyecto E",
    ],
    datasets: [
      {
        label: t("compliance.odsImpact.documentationScore", "Score de Documentación (%)"),
        data: [95, 88, 92, 85, 90],
        backgroundColor: "rgba(59, 130, 246, 0.8)",
      },
    ],
  };

  // ✅ FACTIBLE: Completitud por Sección Anexo IV
  // Calculado desde GOVAIAACTTECHNICALDOCS.TECHSECTIONS (JSONB)
  // Por cada sección (1-11), COUNT proyectos con sección completa / total proyectos
  const annexIVCompletenessData = {
    labels: [
      "1. Descripción",
      "2. Especificaciones",
      "3. Datos",
      "4. Entrenamiento",
      "5. Validación",
      "6. Testing",
      "7. Documentación",
      "8. HITL",
      "9. Monitoreo",
      "10. Correctoras",
      "11. Cambios",
    ],
    datasets: [
      {
        label: "Completitud (%)",
        data: [95, 90, 88, 85, 92, 90, 88, 95, 85, 80, 82],
        backgroundColor: [
          "rgba(34, 197, 94, 0.8)",
          "rgba(34, 197, 94, 0.8)",
          "rgba(234, 179, 8, 0.8)",
          "rgba(234, 179, 8, 0.8)",
          "rgba(34, 197, 94, 0.8)",
          "rgba(34, 197, 94, 0.8)",
          "rgba(234, 179, 8, 0.8)",
          "rgba(34, 197, 94, 0.8)",
          "rgba(234, 179, 8, 0.8)",
          "rgba(239, 68, 68, 0.8)",
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
                <Lightbulb className="w-8 h-8 text-yellow-500" />
                <h1 className="text-3xl font-bold bg-gradient-to-r from-yellow-500 to-orange-500 bg-clip-text text-transparent">
                  ODS 9: {odsData.odsName}
                </h1>
              </div>
              <p className="text-muted-foreground">
                {t(
                  "compliance.odsImpact.ods9.description",
                  "Industria, Innovación e Infraestructura - 7 KPIs principales"
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
          {odsData.kpis.slice(0, 4).map((kpi) => (
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

        {/* KPIs adicionales */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {odsData.kpis.slice(4).map((kpi) => (
            <KPICard key={kpi.code} {...kpi} />
          ))}
        </div>

        {/* Sección 2: Reutilización */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Recycle className="h-5 w-5 text-green-500" />
              {t("compliance.odsImpact.reuse", "Reutilización de Recursos")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              <div>
                <h3 className="text-sm font-medium mb-4">
                  {t("compliance.odsImpact.reuseRateByType", "Tasa de Reutilización por Tipo")}
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Agentes: PRJAGENTS (Fase 2) | Modelos: srvdeployment | Prompts: PRMPROMPTS
                </p>
                <div className="h-64">
                  <Bar data={agentReuseData} options={chartOptions} />
                </div>
              </div>
              <div>
                <h3 className="text-sm font-medium mb-4">
                  {t("compliance.odsImpact.reuseEvolution", "Evolución de Reutilización")}
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Tendencias mensuales de reutilización
                </p>
                <div className="h-64">
                  <Line data={reuseEvolutionData} options={chartOptions} />
                </div>
              </div>
            </div>
            <div className="mt-6 grid grid-cols-1 lg:grid-cols-2 gap-6">
              <div>
                <h3 className="text-sm font-medium mb-4">
                  Tiempo Promedio de Ciclo MLOps
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Calculado desde TRNTRAININGEXECUTIONS.TRNENDDATE hasta srvdeployment.srv_created_at
                </p>
                <div className="h-64">
                  <Line data={mlopsCycleTimeData} options={chartOptions} />
                </div>
              </div>
              <div>
                <h3 className="text-sm font-medium mb-4">
                  Adopción de LLMs Open Source
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Calculado desde AIOCOMPONENTS (tipo LLM, isOpenSource = true)
                </p>
                <div className="h-64">
                  <Line data={llmAdoptionEvolutionData} options={chartOptions} />
                </div>
              </div>
            </div>
          </CardBody>
        </Card>

        {/* Sección 3: Documentación */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <FileCode className="h-5 w-5 text-indigo-500" />
              {t("compliance.odsImpact.documentation", "Documentación")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              <div>
                <h3 className="text-sm font-medium mb-4">
                  Score de Documentación por Proyecto
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Calculado desde GOVAIAACTTECHNICALDOCS.TECHSECTIONS (JSONB)
                </p>
                <div className="h-64">
                  <Bar data={documentationScoreData} options={chartOptions} />
                </div>
              </div>
              <div>
                <h3 className="text-sm font-medium mb-4">
                  Completitud por Sección Anexo IV
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Calculado desde GOVAIAACTTECHNICALDOCS.TECHSECTIONS (JSONB)
                </p>
                <div className="h-64">
                  <Bar data={annexIVCompletenessData} options={chartOptions} />
                </div>
              </div>
            </div>
            <div className="mt-6">
              <h3 className="text-sm font-medium mb-4">
                Proyectos con Documentación Incompleta
              </h3>
              <div className="space-y-2">
                {[
                  {
                    project: "AI Credit System",
                    completeness: 85,
                    missingSections: ["Cambios", "Correctoras"],
                  },
                  {
                    project: "Security System",
                    completeness: 80,
                    missingSections: ["Monitoreo", "Cambios"],
                  },
                ].map((item, idx) => (
                  <div
                    key={idx}
                    className="flex items-center justify-between p-3 border rounded-lg"
                  >
                    <div>
                      <div className="font-medium">{item.project}</div>
                      <div className="text-sm text-muted-foreground">
                        Completitud: {item.completeness}% | Secciones faltantes:{" "}
                        {item.missingSections.join(", ")}
                      </div>
                    </div>
                    <Badge variant="warning">
                      {item.completeness}% completo
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
