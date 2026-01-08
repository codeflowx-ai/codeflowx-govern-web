"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Activity,
  TrendingUp,
  ClipboardCheck,
  AlertTriangle,
  Bell,
  FileText,
  RefreshCw,
  Filter,
  X,
  CheckCircle,
  XCircle,
  AlertCircle,
  ArrowRight,
  MessageSquare,
} from "lucide-react";
import AdvancedMetricsChart from "./components/AdvancedMetricsChart";
import { useEffect, useState } from "react";
import {
  mockPMMData,
  mockIncidents,
  mockCorrectiveActions,
  type PMMSystem,
} from "@/app/(app)/governance/data/mockPMM";

interface Alert {
  altmetricname: string;
  altmetrictype: string;
  altwarningthreshold: number;
  altcriticalthreshold: number;
  altseverity: string;
  altstatus: string;
}

interface Incident {
  incincidenttitle: string;
  incseverity: string;
  incstatus: string;
  incincidenttype: string;
  incdetectedat: string;
}

interface Plan {
  pmmplanname: string;
  pmmfrequency: string;
  pmmstatus: string;
  pmmlastmonitoringdate: string;
}

interface Report {
  psrreportname: string;
  psrreporttype: string;
  psrreportdate: string;
  psrstatus: string;
}

interface PostMarketMonitoringData {
  activePlansCount: number;
  recentIncidentsCount: number;
  activeAlertsCount: number;
  recentReportsCount: number;
  activeAlerts: Alert[];
  recentIncidents: Incident[];
  activePlans: Plan[];
  recentReports: Report[];
}

const mockData: PostMarketMonitoringData = {
  activePlansCount: 12,
  recentIncidentsCount: 5,
  activeAlertsCount: 8,
  recentReportsCount: 15,
  activeAlerts: [
    {
      altmetricname: "Accuracy Drift",
      altmetrictype: "DRIFT",
      altwarningthreshold: 0.05,
      altcriticalthreshold: 0.10,
      altseverity: "HIGH",
      altstatus: "ACTIVE",
    },
    {
      altmetricname: "Latency Increase",
      altmetrictype: "LATENCY",
      altwarningthreshold: 200,
      altcriticalthreshold: 500,
      altseverity: "MEDIUM",
      altstatus: "ACTIVE",
    },
    {
      altmetricname: "Bias Detection",
      altmetrictype: "BIAS",
      altwarningthreshold: 0.02,
      altcriticalthreshold: 0.05,
      altseverity: "CRITICAL",
      altstatus: "ACTIVE",
    },
  ],
  recentIncidents: [
    {
      incincidenttitle: "Model Performance Degradation",
      incseverity: "HIGH",
      incstatus: "INVESTIGATING",
      incincidenttype: "PERFORMANCE",
      incdetectedat: "2025-01-15T10:30:00Z",
    },
    {
      incincidenttitle: "Data Drift Detected",
      incseverity: "MEDIUM",
      incstatus: "RESOLVED",
      incincidenttype: "DRIFT",
      incdetectedat: "2025-01-14T15:20:00Z",
    },
  ],
  activePlans: [
    {
      pmmplanname: "Daily Performance Monitoring",
      pmmfrequency: "DAILY",
      pmmstatus: "ACTIVE",
      pmmlastmonitoringdate: "2025-01-15T08:00:00Z",
    },
    {
      pmmplanname: "Weekly Bias Analysis",
      pmmfrequency: "WEEKLY",
      pmmstatus: "ACTIVE",
      pmmlastmonitoringdate: "2025-01-13T09:00:00Z",
    },
  ],
  recentReports: [
    {
      psrreportname: "Monthly Compliance Report",
      psrreporttype: "COMPLIANCE",
      psrreportdate: "2025-01-15T00:00:00Z",
      psrstatus: "COMPLETED",
    },
    {
      psrreportname: "Performance Analysis Q1",
      psrreporttype: "PERFORMANCE",
      psrreportdate: "2025-01-14T00:00:00Z",
      psrstatus: "COMPLETED",
    },
  ],
};

export default function PostMarketMonitoringDashboardPage() {
  const { t } = useTranslation();
  const [data, setData] = useState<PostMarketMonitoringData | null>(null);
  const [pmmData, setPmmData] = useState<typeof mockPMMData | null>(null);
  const [loading, setLoading] = useState(true);
  const [selectedProjectId, setSelectedProjectId] = useState<string>("");
  const [selectedMetricType, setSelectedMetricType] = useState<string>("");

  useEffect(() => {
    loadDashboard();
  }, []);

  const loadDashboard = async () => {
    try {
      setLoading(true);
      // TODO: Reemplazar con llamada real a API
      await new Promise((resolve) => setTimeout(resolve, 500));
      setData(mockData);
      setPmmData(mockPMMData);
    } catch (error) {
      console.error("Error loading dashboard:", error);
    } finally {
      setLoading(false);
    }
  };

  const applyFilters = () => {
    loadDashboard();
  };

  const clearFilters = () => {
    setSelectedProjectId("");
    setSelectedMetricType("");
    loadDashboard();
  };

  const getSeverityColor = (severity: string) => {
    switch (severity?.toUpperCase()) {
      case "CRITICAL":
        return "bg-red-100 text-red-800 dark:bg-red-900/20 dark:text-red-300";
      case "HIGH":
        return "bg-orange-100 text-orange-800 dark:bg-orange-900/20 dark:text-orange-300";
      case "MEDIUM":
        return "bg-yellow-100 text-yellow-800 dark:bg-yellow-900/20 dark:text-yellow-300";
      case "LOW":
        return "bg-blue-100 text-blue-800 dark:bg-blue-900/20 dark:text-blue-300";
      default:
        return "bg-gray-100 text-gray-800 dark:bg-gray-900/20 dark:text-gray-300";
    }
  };

  const getStatusColor = (status: string) => {
    switch (status?.toUpperCase()) {
      case "ACTIVE":
      case "COMPLETED":
        return "bg-green-100 text-green-800 dark:bg-green-900/20 dark:text-green-300";
      case "INVESTIGATING":
      case "PENDING":
        return "bg-yellow-100 text-yellow-800 dark:bg-yellow-900/20 dark:text-yellow-300";
      case "RESOLVED":
        return "bg-blue-100 text-blue-800 dark:bg-blue-900/20 dark:text-blue-300";
      default:
        return "bg-gray-100 text-gray-800 dark:bg-gray-900/20 dark:text-gray-300";
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
        {/* Header */}
        <div className="space-y-2">
          <div className="flex items-center gap-3 mb-4">
            <Activity className="w-8 h-8 text-orange-500" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-orange-500 to-orange-700 bg-clip-text text-transparent">
              {t("governance.compliance.pmm.title", "Post-Market Monitoring")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t(
              "governance.compliance.pmm.subtitle",
              "Monitoreo continuo de sistemas de IA según EU AI Act Art. 20 y 72"
            )}
          </p>
        </div>

        {/* Filtros */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Filter className="w-5 h-5" />
              {t("governance.compliance.pmm.dashboard.filters", "Filtros")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
              <div>
                <label className="block text-sm font-medium mb-2">{t("governance.compliance.pmm.dashboard.project", "Proyecto")}</label>
                <select
                  value={selectedProjectId}
                  onChange={(e) => setSelectedProjectId(e.target.value)}
                  className="w-full px-3 py-2 border rounded-md bg-background"
                >
                  <option value="">{t("governance.compliance.pmm.dashboard.allProjects", "Todos los proyectos")}</option>
                  <option value="1">Proyecto 1</option>
                  <option value="2">Proyecto 2</option>
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">{t("governance.compliance.pmm.dashboard.metricType", "Tipo de Métrica")}</label>
                <select
                  value={selectedMetricType}
                  onChange={(e) => setSelectedMetricType(e.target.value)}
                  className="w-full px-3 py-2 border rounded-md bg-background"
                >
                  <option value="">{t("governance.compliance.pmm.dashboard.allMetrics", "Todas las métricas")}</option>
                  <option value="PERFORMANCE">{t("governance.compliance.pmm.dashboard.metricTypes.PERFORMANCE", "Performance")}</option>
                  <option value="DRIFT">{t("governance.compliance.pmm.dashboard.metricTypes.DRIFT", "Drift")}</option>
                  <option value="BIAS">{t("governance.compliance.pmm.dashboard.metricTypes.BIAS", "Bias")}</option>
                  <option value="ACCURACY">{t("governance.compliance.pmm.dashboard.metricTypes.ACCURACY", "Accuracy")}</option>
                  <option value="LATENCY">{t("governance.compliance.pmm.dashboard.metricTypes.LATENCY", "Latency")}</option>
                </select>
              </div>
              <div className="flex items-end gap-2">
                <Button onClick={applyFilters} className="flex-1">
                  {t("governance.compliance.pmm.dashboard.applyFilters", "Aplicar Filtros")}
                </Button>
                <Button onClick={clearFilters} variant="outline">
                  <X className="w-4 h-4" />
                </Button>
              </div>
              <div className="flex items-end">
                <Button onClick={loadDashboard} variant="outline" className="w-full">
                  <RefreshCw className="w-4 h-4 mr-2" />
                  {t("governance.compliance.pmm.dashboard.refresh", "Refrescar")}
                </Button>
              </div>
            </div>
          </CardBody>
        </Card>

        {/* Métricas Principales */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <Card className="border-primary hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardBody className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-muted-foreground mb-2">
                    {t("governance.compliance.pmm.metrics.systemsMonitored", "Sistemas Monitoreados")}
                  </p>
                  <h2 className="text-3xl font-bold text-primary">
                    {pmmData?.metrics.systemsMonitored || 0}
                  </h2>
                </div>
                <Activity className="w-10 h-10 text-primary" />
              </div>
            </CardBody>
          </Card>

          <Card className="border-orange-500 hover:shadow-3xl hover:border-orange-500/50 transition-all duration-300">
            <CardBody className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-muted-foreground mb-2">
                    {t("governance.compliance.pmm.metrics.activeIncidents", "Incidentes Activos")}
                  </p>
                  <h2 className="text-3xl font-bold text-orange-600">
                    {pmmData?.metrics.activeIncidents || 0}
                  </h2>
                </div>
                <AlertTriangle className="w-10 h-10 text-orange-600" />
              </div>
            </CardBody>
          </Card>

          <Card className="border-yellow-500 hover:shadow-3xl hover:border-yellow-500/50 transition-all duration-300">
            <CardBody className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-muted-foreground mb-2">
                    {t("governance.compliance.pmm.metrics.pendingActions", "Acciones Pendientes")}
                  </p>
                  <h2 className="text-3xl font-bold text-yellow-600">
                    {pmmData?.metrics.pendingActions || 0}
                  </h2>
                </div>
                <ClipboardCheck className="w-10 h-10 text-yellow-600" />
              </div>
            </CardBody>
          </Card>

          <Card className="border-green-500 hover:shadow-3xl hover:border-green-500/50 transition-all duration-300">
            <CardBody className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-muted-foreground mb-2">
                    {t("governance.compliance.pmm.metrics.slaCompliance", "Cumplimiento SLA")}
                  </p>
                  <h2 className="text-3xl font-bold text-green-600">
                    {((pmmData?.metrics.slaCompliance || 0) * 100).toFixed(0)}%
                  </h2>
                </div>
                <CheckCircle className="w-10 h-10 text-green-600" />
              </div>
            </CardBody>
          </Card>
        </div>

        {/* Acciones Rápidas */}
        <div className="flex flex-wrap gap-4">
          <Button
            onClick={() => {
              if (globalThis.window !== undefined) {
                globalThis.window.location.href = "/governance/compliance/incidents";
              }
            }}
            variant="outline"
            className="flex items-center gap-2"
          >
            <AlertTriangle className="w-4 h-4" />
            {t("governance.compliance.pmm.actions.viewIncidents", "Ver Incidentes")}
            <ArrowRight className="w-4 h-4" />
          </Button>
          <Button
            onClick={() => {
              if (globalThis.window !== undefined) {
                globalThis.window.location.href = "/governance/compliance/corrective-actions";
              }
            }}
            variant="outline"
            className="flex items-center gap-2"
          >
            <ClipboardCheck className="w-4 h-4" />
            {t("governance.compliance.pmm.actions.viewActions", "Ver Acciones Correctoras")}
            <ArrowRight className="w-4 h-4" />
          </Button>
          <Button
            onClick={() => {
              if (globalThis.window !== undefined) {
                globalThis.window.location.href = "/governance/compliance/post-market-monitoring/plans";
              }
            }}
            variant="outline"
            className="flex items-center gap-2"
          >
            <FileText className="w-4 h-4" />
            {t("governance.compliance.pmm.actions.viewPlans", "Ver Planes PMM")}
            <ArrowRight className="w-4 h-4" />
          </Button>
          <Button
            onClick={() => {
              if (globalThis.window !== undefined) {
                globalThis.window.location.href = "/governance/compliance/post-market-monitoring/reports";
              }
            }}
            variant="outline"
            className="flex items-center gap-2"
          >
            <FileText className="w-4 h-4" />
            {t("governance.compliance.pmm.actions.viewReports", "Ver Reportes")}
            <ArrowRight className="w-4 h-4" />
          </Button>
          <Button
            onClick={() => {
              if (globalThis.window !== undefined) {
                globalThis.window.location.href = "/governance/compliance/post-market-monitoring/feedback";
              }
            }}
            variant="outline"
            className="flex items-center gap-2"
          >
            <MessageSquare className="w-4 h-4" />
            {t("governance.compliance.pmm.actions.viewFeedback", "Ver Feedback")}
            <ArrowRight className="w-4 h-4" />
          </Button>
        </div>

        {/* Monitoreo Continuo - Sistemas en Producción */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle>{t("governance.compliance.pmm.systems.title", "Sistemas en Producción")}</CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {pmmData?.systems.map((system) => (
                <Card
                  key={system.id}
                  className={`border-l-4 ${
                    system.status === "HEALTHY"
                      ? "border-l-green-500"
                      : system.status === "WARNING"
                      ? "border-l-yellow-500"
                      : "border-l-red-500"
                  }`}
                >
                  <CardBody className="p-4">
                    <div className="flex items-start justify-between">
                      <div className="flex-1">
                        <div className="flex items-center gap-3 mb-2">
                          <h3 className="font-semibold text-lg">{system.projectName}</h3>
                          <Badge
                            className={
                              system.status === "HEALTHY"
                                ? "bg-green-100 text-green-800 dark:bg-green-900/20 dark:text-green-300"
                                : system.status === "WARNING"
                                ? "bg-yellow-100 text-yellow-800 dark:bg-yellow-900/20 dark:text-yellow-300"
                                : "bg-red-100 text-red-800 dark:bg-red-900/20 dark:text-red-300"
                            }
                          >
                            {system.status}
                          </Badge>
                          {system.driftDetected && (
                            <Badge className="bg-orange-100 text-orange-800 dark:bg-orange-900/20 dark:text-orange-300">
                              {t("governance.compliance.pmm.systems.driftDetected", "Drift Detectado")}
                            </Badge>
                          )}
                          {system.anomaliesDetected && (
                            <Badge className="bg-red-100 text-red-800 dark:bg-red-900/20 dark:text-red-300">
                              {t("governance.compliance.pmm.systems.anomaliesDetected", "Anomalías")}
                            </Badge>
                          )}
                        </div>
                        <div className="grid grid-cols-3 gap-4 mt-3">
                          <div>
                            <p className="text-sm text-muted-foreground">
                              {t("governance.compliance.pmm.systems.accuracy", "Precisión")}
                            </p>
                            <p className="text-lg font-semibold">
                              {(system.metrics.accuracy * 100).toFixed(1)}%
                            </p>
                          </div>
                          <div>
                            <p className="text-sm text-muted-foreground">
                              {t("governance.compliance.pmm.systems.latency", "Latencia")}
                            </p>
                            <p className="text-lg font-semibold">{system.metrics.latency}ms</p>
                          </div>
                          <div>
                            <p className="text-sm text-muted-foreground">
                              {t("governance.compliance.pmm.systems.throughput", "Throughput")}
                            </p>
                            <p className="text-lg font-semibold">
                              {system.metrics.throughput} req/s
                            </p>
                          </div>
                        </div>
                        <p className="text-xs text-muted-foreground mt-2">
                          {t("governance.compliance.pmm.systems.lastCheck", "Última verificación")}:{" "}
                          {new Date(system.lastCheck).toLocaleString()}
                        </p>
                        <div className="flex flex-wrap gap-2 mt-3">
                          <Button
                            onClick={() => {
                              if (globalThis.window !== undefined) {
                                globalThis.window.location.href = `/governance/compliance/incidents?projectId=${system.projectId}`;
                              }
                            }}
                            variant="outline"
                            size="sm"
                            className="flex items-center gap-1.5 h-8 px-3 text-xs"
                          >
                            <AlertTriangle className="w-3.5 h-3.5" />
                            {t("governance.compliance.pmm.actions.viewIncidents", "Ver Incidentes")}
                          </Button>
                          <Button
                            onClick={() => {
                              if (globalThis.window !== undefined) {
                                globalThis.window.location.href = `/governance/compliance/corrective-actions?projectId=${system.projectId}`;
                              }
                            }}
                            variant="outline"
                            size="sm"
                            className="flex items-center gap-1.5 h-8 px-3 text-xs"
                          >
                            <ClipboardCheck className="w-3.5 h-3.5" />
                            {t("governance.compliance.pmm.actions.viewActions", "Ver Acciones")}
                          </Button>
                          <Button
                            onClick={() => {
                              if (globalThis.window !== undefined) {
                                globalThis.window.location.href = `/governance/compliance/post-market-monitoring/plans?projectId=${system.projectId}`;
                              }
                            }}
                            variant="outline"
                            size="sm"
                            className="flex items-center gap-1.5 h-8 px-3 text-xs"
                          >
                            <FileText className="w-3.5 h-3.5" />
                            {t("governance.compliance.pmm.actions.viewPlans", "Ver Planes")}
                          </Button>
                          <Button
                            onClick={() => {
                              if (globalThis.window !== undefined) {
                                globalThis.window.location.href = `/governance/compliance/post-market-monitoring/reports?projectId=${system.projectId}`;
                              }
                            }}
                            variant="outline"
                            size="sm"
                            className="flex items-center gap-1.5 h-8 px-3 text-xs"
                          >
                            <FileText className="w-3.5 h-3.5" />
                            {t("governance.compliance.pmm.actions.viewReports", "Ver Reportes")}
                          </Button>
                          <Button
                            onClick={() => {
                              if (globalThis.window !== undefined) {
                                globalThis.window.location.href = `/governance/compliance/post-market-monitoring/feedback?projectId=${system.projectId}`;
                              }
                            }}
                            variant="outline"
                            size="sm"
                            className="flex items-center gap-1.5 h-8 px-3 text-xs"
                          >
                            <MessageSquare className="w-3.5 h-3.5" />
                            {t("governance.compliance.pmm.actions.viewFeedback", "Feedback")}
                          </Button>
                        </div>
                      </div>
                    </div>
                  </CardBody>
                </Card>
              ))}
            </div>
          </CardBody>
        </Card>

        {/* Visualizaciones Avanzadas */}
        {pmmData && pmmData.systems && pmmData.systems.length > 0 && (
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            <AdvancedMetricsChart
              title={t("governance.compliance.pmm.advanced.accuracy", "Tendencia de Precisión")}
              data={pmmData.systems.slice(0, 10).map((s, i) => ({
                date: new Date(Date.now() - (10 - i) * 24 * 60 * 60 * 1000).toISOString(),
                value: s.metrics.accuracy,
                baseline: 0.95,
                prediction: s.metrics.accuracy * 1.01,
              }))}
              metricName="accuracy"
              unit="%"
            />
            <AdvancedMetricsChart
              title={t("governance.compliance.pmm.advanced.latency", "Tendencia de Latencia")}
              data={pmmData.systems.slice(0, 10).map((s, i) => ({
                date: new Date(Date.now() - (10 - i) * 24 * 60 * 60 * 1000).toISOString(),
                value: s.metrics.latency,
                baseline: 100,
                prediction: s.metrics.latency * 0.98,
              }))}
              metricName="latency"
              unit="ms"
            />
          </div>
        )}

    </div>
  );
}
