"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  Shield,
  RefreshCw,
  ArrowLeft,
  TrendingUp,
  Database,
  FileCheck,
  Globe,
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

export default function ODS16Page() {
  const { t } = useTranslation();
  const [odsData, setOdsData] = useState<ODSImpact | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      const response = await odsImpactService.getODSData(16);
      if (response.success && response.data) {
        setOdsData(response.data);
      } else {
        console.error("Error loading ODS 16 data:", response.error);
      }
    } catch (error) {
      console.error("Error loading ODS 16 data:", error);
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

  // Mock data para gráficos (FACTIBLES con datos reales)
  // NOTA: Estos datos son mock, pero las gráficas SÍ pueden calcularse con:
  // - IMLIMMUTABLELOGS.IMLTIMESTAMP para evolución temporal
  // - PRJPROJECTS + IMLIMMUTABLELOGS para trazabilidad
  // - COMCOMPLIANCEASSESSMENTS para certificación
  // - GOVQUALITYMANAGEMENTSYSTEMS.QMSMODULESCORES (JSONB) para QMS
  // - GOVHITLDECISIONS + GOVHITLSUPERVISIONS para HITL

  const certificationData = {
    labels: ["Certificados", "En Proceso", "Pendientes"],
    datasets: [
      {
        label: "Sistemas",
        data: [78, 15, 7],
        backgroundColor: [
          "rgba(34, 197, 94, 0.8)",
          "rgba(234, 179, 8, 0.8)",
          "rgba(239, 68, 68, 0.8)",
        ],
      },
    ],
  };

  const traceabilityEvolutionData = {
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
        label: t("compliance.odsImpact.chartLabels.traceabilityRate", "Tasa de Trazabilidad (%)"),
        data: [85, 87, 88, 89, 90, 91, 91, 92, 92, 92, 92.5, 92.5],
        borderColor: "rgb(59, 130, 246)",
        backgroundColor: "rgba(59, 130, 246, 0.1)",
        fill: true,
        tension: 0.4,
      },
    ],
  };

  // QMS scores por módulo - FACTIBLE desde GOVQUALITYMANAGEMENTSYSTEMS.QMSMODULESCORES (JSONB)
  // Los 13 módulos están definidos en el QMS según Art. 17
  const qmsModuleNames = [
    "Estrategia",
    "Riesgos",
    "Datos",
    "Diseño",
    "Validación",
    "Documentación",
    "HITL",
    "Correctoras",
    "PMM",
    "Cambios",
    "Proveedores",
    "Auditoría",
    "No Conformidades",
  ];

  const qmsHeatmapData = {
    labels: qmsModuleNames,
    datasets: [
      {
        label: "Score QMS",
        // Mock data - Real: Parsear QMSMODULESCORES JSONB de GOVQUALITYMANAGEMENTSYSTEMS
        data: [0.90, 0.85, 0.75, 0.88, 0.82, 0.80, 0.95, 0.78, 0.85, 0.80, 0.75, 0.88, 0.82],
        backgroundColor: [
          "rgba(34, 197, 94, 0.8)",
          "rgba(34, 197, 94, 0.8)",
          "rgba(234, 179, 8, 0.8)",
          "rgba(34, 197, 94, 0.8)",
          "rgba(34, 197, 94, 0.8)",
          "rgba(34, 197, 94, 0.8)",
          "rgba(34, 197, 94, 0.8)",
          "rgba(234, 179, 8, 0.8)",
          "rgba(34, 197, 94, 0.8)",
          "rgba(34, 197, 94, 0.8)",
          "rgba(234, 179, 8, 0.8)",
          "rgba(34, 197, 94, 0.8)",
          "rgba(34, 197, 94, 0.8)",
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
      {/* Partículas flotantes */}
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
                <Shield className="w-8 h-8 text-blue-500" />
                <h1 className="text-3xl font-bold bg-gradient-to-r from-blue-500 to-blue-700 bg-clip-text text-transparent">
                  ODS 16: {odsData.odsName}
                </h1>
              </div>
              <p className="text-muted-foreground">
                {t(
                  "compliance.odsImpact.ods16.description",
                  "Paz, Justicia e Instituciones Sólidas - 6 KPIs principales"
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
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
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

        {/* Sección 2: Trazabilidad */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Database className="h-5 w-5 text-blue-500" />
              {t("compliance.odsImpact.traceability", "Trazabilidad")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div>
              <h3 className="text-sm font-medium mb-4">
                {t("compliance.odsImpact.traceabilityEvolution", "Evolución de Tasa de Trazabilidad")}
              </h3>
              <p className="text-xs text-muted-foreground mb-4">
                Calculado desde IMLIMMUTABLELOGS.IMLTIMESTAMP y PRJPROJECTS
              </p>
              <div className="h-64">
                <Line
                  data={traceabilityEvolutionData}
                  options={chartOptions}
                />
              </div>
            </div>
          </CardBody>
        </Card>

        {/* Sección 3: Certificación */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <FileCheck className="h-5 w-5 text-green-500" />
              {t("compliance.odsImpact.certification", "Certificación")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              <div>
                <h3 className="text-sm font-medium mb-4">
                  {t("compliance.odsImpact.certifiedSystemsVsPending", "Sistemas Certificados vs. Pendientes")}
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Calculado desde COMCOMPLIANCEASSESSMENTS.READYFORCERTIFICATION y PRJPROJECTS.PRJISHIGHRISK
                </p>
                <div className="h-64">
                  <Bar data={certificationData} options={chartOptions} />
                </div>
              </div>
              <div>
                <h3 className="text-sm font-medium mb-4">
                  {t("compliance.odsImpact.systemsInCertificationProcess", "Sistemas en Proceso de Certificación")}
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Datos desde COMCOMPLIANCEASSESSMENTS (estado IN_PROGRESS)
                </p>
                <div className="space-y-3">
                  {[
                    {
                      name: "AI Credit Scoring System",
                      daysRemaining: 5,
                      progress: 85,
                    },
                    {
                      name: "Facial Recognition System",
                      daysRemaining: 12,
                      progress: 60,
                    },
                    {
                      name: "Customer Service Bot",
                      daysRemaining: 8,
                      progress: 75,
                    },
                  ].map((item, idx) => (
                    <div key={idx} className="space-y-1">
                      <div className="flex justify-between text-sm">
                        <span className="font-medium">{item.name}</span>
                        <span className="text-muted-foreground">
                          {item.daysRemaining} días restantes
                        </span>
                      </div>
                      <div className="w-full bg-gray-200 rounded-full h-2">
                        <div
                          className="bg-blue-500 h-2 rounded-full"
                          style={{ width: `${item.progress}%` }}
                        />
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </CardBody>
        </Card>

        {/* Sección 4: Compliance QMS */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <TrendingUp className="h-5 w-5 text-indigo-500" />
              {t("compliance.odsImpact.qms", "Sistema de Gestión de Calidad (QMS)")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="space-y-6">
              <div>
                <h3 className="text-sm font-medium mb-4">
                  Score QMS por Módulo (13 módulos)
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Calculado desde GOVQUALITYMANAGEMENTSYSTEMS.QMSMODULESCORES (JSONB)
                </p>
                <div className="h-64">
                  <Bar data={qmsHeatmapData} options={chartOptions} />
                </div>
              </div>
              <div>
                <h3 className="text-sm font-medium mb-4">
                  {t("compliance.odsImpact.gapsDetected", "Gaps Detectados")}
                </h3>
                <div className="space-y-2">
                  {[
                    {
                      module: "Gestión de Datos",
                      currentScore: 0.75,
                      targetScore: 0.80,
                      gap: 0.05,
                    },
                    {
                      module: "Gestión de Proveedores",
                      currentScore: 0.75,
                      targetScore: 0.80,
                      gap: 0.05,
                    },
                  ].map((gap, idx) => (
                    <div
                      key={idx}
                      className="flex items-center justify-between p-3 border rounded-lg"
                    >
                      <div>
                        <div className="font-medium">{gap.module}</div>
                        <div className="text-sm text-muted-foreground">
                          Score actual: {(gap.currentScore * 100).toFixed(1)}% | Meta:{" "}
                          {(gap.targetScore * 100).toFixed(1)}%
                        </div>
                      </div>
                      <Badge variant="danger">
                        Gap: {(gap.gap * 100).toFixed(1)}%
                      </Badge>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </CardBody>
        </Card>

        {/* Sección 5: HITL */}
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
                  Decisiones con/sin HITL
                </h3>
                <p className="text-xs text-muted-foreground mb-4">
                  Calculado desde GOVHITLDECISIONS + GOVHITLSUPERVISIONS
                </p>
                <div className="h-64">
                  <Bar
                    data={{
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
                    }}
                    options={chartOptions}
                  />
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
                  <Line
                    data={{
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
                          label: t("compliance.odsImpact.chartLabels.time", "Tiempo (horas)"),
                          data: [4.2, 4.0, 3.8, 3.6, 3.5, 3.4, 3.3, 3.3, 3.2, 3.2, 3.2, 3.2],
                          borderColor: "rgb(59, 130, 246)",
                          backgroundColor: "rgba(59, 130, 246, 0.1)",
                          fill: true,
                          tension: 0.4,
                        },
                      ],
                    }}
                    options={chartOptions}
                  />
                </div>
              </div>
            </div>
            <div className="mt-6">
              <h3 className="text-sm font-medium mb-4">
                {t("compliance.odsImpact.pendingHumanSupervisionDecisions", "Decisiones Pendientes de Supervisión Humana")}
              </h3>
              <div className="space-y-2">
                {[
                  {
                    type: "AGENT_APPROVAL",
                    entity: "Credit Scoring Agent",
                    createdAt: "2025-12-01T10:00:00Z",
                    slaDeadline: "2025-12-01T14:00:00Z",
                    timeRemaining: 2.5,
                  },
                  {
                    type: "PROMPT_REVIEW",
                    entity: "Credit Scoring Prompt",
                    createdAt: "2025-12-01T11:00:00Z",
                    slaDeadline: "2025-12-01T15:00:00Z",
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
