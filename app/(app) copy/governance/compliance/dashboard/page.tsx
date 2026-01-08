"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Shield,
  CheckCircle,
  FileText,
  Database,
  TrendingUp,
  AlertTriangle,
  Clock,
  RefreshCw,
  Bell,
  FileCheck,
  Award,
  Eye,
  ArrowRight,
  Filter,
  Play,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { Input } from "@/components/ui/input";
import {
  mockConformityAssessments,
  mockConformityMetrics,
  type ConformityAssessment,
} from "@/app/(app)/governance/data/mockConformityAssessment";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  ArcElement,
  PointElement,
  LineElement,
  Filler,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import { Bar, Doughnut, Line } from "react-chartjs-2";

// Registrar componentes de Chart.js
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  ArcElement,
  PointElement,
  LineElement,
  Filler,
  Title,
  Tooltip,
  Legend
);

interface ComplianceDashboardData {
  completionRate: number;
  completedAssessments: number;
  totalAssessments: number;
  friaApprovalRate: number;
  approvedFria: number;
  totalFria: number;
  euSubmissionRate: number;
  submittedEuRegistrations: number;
  totalEuRegistrations: number;
  averageComplianceScore: number;
  complianceStatus: string;
  pendingAssessments: number;
  pendingFria: number;
  pendingEuRegistrations: number;
}

const mockDashboardData: ComplianceDashboardData = {
  completionRate: 78.5,
  completedAssessments: 47,
  totalAssessments: 60,
  friaApprovalRate: 85.2,
  approvedFria: 23,
  totalFria: 27,
  euSubmissionRate: 72.0,
  submittedEuRegistrations: 18,
  totalEuRegistrations: 25,
  averageComplianceScore: 82.3,
  complianceStatus: "Cumplimiento Parcial",
  pendingAssessments: 13,
  pendingFria: 4,
  pendingEuRegistrations: 7,
};

// Datos para gráficos
const COLORS = {
  primary: "#3b82f6",
  green: "#10b981",
  yellow: "#f59e0b",
  red: "#ef4444",
  purple: "#8b5cf6",
  blue: "#3b82f6",
  orange: "#f97316",
};

// Función para preparar datos de gráficos
const getChartData = (data: ComplianceDashboardData | null, t: (key: string, defaultValue?: string) => string) => {
  if (!data) {
    return {
      scoreDistribution: [],
      timelineData: [],
      statusDistribution: [],
      complianceOverview: [],
    };
  }

  // Distribución de scores
  const scoreDistribution = [
    { name: t("governance.complianceDashboard.conformity.charts.categories.excellent", "Excelente"), value: 12 },
    { name: t("governance.complianceDashboard.conformity.charts.categories.good", "Bueno"), value: 6 },
    { name: t("governance.complianceDashboard.conformity.charts.categories.regular", "Regular"), value: 4 },
    { name: t("governance.complianceDashboard.conformity.charts.categories.improvement", "Mejora"), value: 3 },
  ];

  // Evolución temporal (últimos 6 meses)
  const timelineData = [
    { month: "Ene", Evaluaciones: 8, FRIA: 5, "Registros EU": 3 },
    { month: "Feb", Evaluaciones: 12, FRIA: 8, "Registros EU": 5 },
    { month: "Mar", Evaluaciones: 15, FRIA: 10, "Registros EU": 7 },
    { month: "Abr", Evaluaciones: 18, FRIA: 12, "Registros EU": 9 },
    { month: "May", Evaluaciones: 22, FRIA: 15, "Registros EU": 12 },
    { month: "Jun", Evaluaciones: data.completedAssessments, FRIA: data.approvedFria, "Registros EU": data.submittedEuRegistrations },
  ];

  // Distribución de estados
  const statusDistribution = [
    { name: t("governance.complianceDashboard.conformity.charts.categories.completed", "Completadas"), value: data.completedAssessments },
    { name: t("governance.complianceDashboard.conformity.charts.categories.pending", "Pendientes"), value: data.pendingAssessments },
    { name: t("governance.complianceDashboard.conformity.charts.categories.friaPending", "FRIA Pendientes"), value: data.pendingFria },
    { name: t("governance.complianceDashboard.conformity.charts.categories.euPending", "EU Pendientes"), value: data.pendingEuRegistrations },
  ];

  // Overview de compliance
  const complianceOverview = [
    { name: "Evaluaciones", Completadas: data.completedAssessments, Total: data.totalAssessments },
    { name: "FRIA", Completadas: data.approvedFria, Total: data.totalFria },
    { name: "Registros EU", Completadas: data.submittedEuRegistrations, Total: data.totalEuRegistrations },
  ];

  return { scoreDistribution, timelineData, statusDistribution, complianceOverview };
};


export default function ComplianceDashboardPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [data, setData] = useState<ComplianceDashboardData | null>(null);
  const [loading, setLoading] = useState(true);
  const [mounted, setMounted] = useState(false);

  // Estado para evaluaciones de conformidad
  const [conformityMetrics, setConformityMetrics] = useState(mockConformityMetrics);
  const [assessments, setAssessments] = useState<ConformityAssessment[]>(mockConformityAssessments);
  const [filteredAssessments, setFilteredAssessments] = useState<ConformityAssessment[]>(mockConformityAssessments);
  const [filterStatus, setFilterStatus] = useState<string>("all");
  const [filterProject, setFilterProject] = useState<string>("");

  useEffect(() => {
    setMounted(true);
    loadDashboard();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    filterAssessments();
  }, [filterStatus, filterProject, assessments]);

  const loadDashboard = async () => {
    try {
      setLoading(true);

      // Cargar métricas del dashboard
      const dashboardResponse = await fetch('/api/governance/compliance/dashboard');
      if (dashboardResponse.ok) {
        const dashboardData = await dashboardResponse.json();
        setData(dashboardData);
      } else {
        // Fallback a mock si falla
        setData(mockDashboardData);
      }

      // Cargar métricas de conformidad
      const metricsResponse = await fetch('/api/governance/compliance/assessments/metrics');
      if (metricsResponse.ok) {
        const metricsData = await metricsResponse.json();
        setConformityMetrics(metricsData);
      } else {
        // Fallback a mock si falla
        setConformityMetrics(mockConformityMetrics);
      }

      // Cargar listado de evaluaciones
      const assessmentsResponse = await fetch('/api/governance/compliance/assessments');
      if (assessmentsResponse.ok) {
        const assessmentsData = await assessmentsResponse.json();
        setAssessments(assessmentsData);
        setFilteredAssessments(assessmentsData);
      } else {
        // Fallback a mock si falla
        setAssessments(mockConformityAssessments);
        setFilteredAssessments(mockConformityAssessments);
      }
    } catch (error) {
      console.error("Error loading dashboard:", error);
      // Fallback a datos mock en caso de error
      setData(mockDashboardData);
      setConformityMetrics(mockConformityMetrics);
      setAssessments(mockConformityAssessments);
      setFilteredAssessments(mockConformityAssessments);
    } finally {
      setLoading(false);
    }
  };

  const filterAssessments = () => {
    let filtered = [...assessments];

    if (filterStatus !== "all") {
      filtered = filtered.filter((a) => a.status === filterStatus);
    }

    if (filterProject) {
      filtered = filtered.filter((a) =>
        a.projectName.toLowerCase().includes(filterProject.toLowerCase())
      );
    }

    setFilteredAssessments(filtered);
  };

  const formatDecimal = (value: number): string => {
    return value.toFixed(1);
  };

  const formatScore = (score: number | null): string => {
    if (score === null) return "-";
    return (score * 100).toFixed(0) + "%";
  };

  const getStatusBadge = (status: string) => {
    const statusConfig = {
      COMPLETED: { label: "Completada", className: "bg-green-100 text-green-800 dark:bg-green-900/20 dark:text-green-300" },
      IN_PROGRESS: { label: "En Progreso", className: "bg-yellow-100 text-yellow-800 dark:bg-yellow-900/20 dark:text-yellow-300" },
      PENDING: { label: "Pendiente", className: "bg-gray-100 text-gray-800 dark:bg-gray-900/20 dark:text-gray-300" },
      REJECTED: { label: "Rechazada", className: "bg-red-100 text-red-800 dark:bg-red-900/20 dark:text-red-300" },
    };
    const config = statusConfig[status as keyof typeof statusConfig] || statusConfig.PENDING;
    return (
      <Badge className={config.className}>
        {config.label}
      </Badge>
    );
  };

  if (!mounted || loading || !data) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <RefreshCw className="w-8 h-8 animate-spin mx-auto mb-2 text-muted-foreground" />
          <p className="text-muted-foreground">Cargando...</p>
        </div>
      </div>
    );
  }

  // Datos para gráficos
  const chartData = getChartData(data, t);

  // Configuración de colores para modo oscuro/claro
  const isDark = typeof window !== 'undefined' && document.documentElement.classList.contains('dark');
  const textColor = isDark ? '#e5e7eb' : '#000000'; // Negro para modo claro, claro para modo oscuro
  const gridColor = isDark ? 'rgba(255, 255, 255, 0.1)' : 'rgba(0, 0, 0, 0.1)';

  // Configuración común para gráficos
  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        labels: {
          color: textColor,
          font: {
            weight: 'bold' as const,
          },
        },
      },
      tooltip: {
        backgroundColor: isDark ? 'rgba(0, 0, 0, 0.8)' : 'rgba(255, 255, 255, 0.95)',
        titleColor: textColor,
        bodyColor: textColor,
        borderColor: isDark ? 'rgba(255, 255, 255, 0.2)' : 'rgba(0, 0, 0, 0.2)',
        borderWidth: 1,
      },
    },
    scales: {
      x: {
        ticks: {
          color: '#000000', // Siempre negro para mejor visibilidad
          font: {
            weight: 'bold' as const,
            size: 12,
          },
        },
        grid: {
          color: gridColor,
        },
      },
      y: {
        ticks: {
          color: '#000000', // Siempre negro para mejor visibilidad
          font: {
            weight: 'bold' as const,
            size: 12,
          },
        },
        grid: {
          color: gridColor,
        },
      },
    },
  };

  return (
    <div className="space-y-4">
      {/* Header - Título alineado a la izquierda */}
      <div className="flex items-center justify-between">
        <div className="space-y-1">
            <div className="flex items-center gap-3">
              <Shield className="w-6 h-6 text-primary" />
              <h1 className="text-2xl font-bold text-foreground">
                {t("governance.complianceDashboard.title", "Dashboard de Compliance")}
              </h1>
            </div>
            <p className="text-sm text-muted-foreground ml-9">
              {t("governance.complianceDashboard.subtitle", "Métricas y KPIs de cumplimiento EU AI Act")}
            </p>
          </div>
          <Button onClick={loadDashboard} variant="outline" size="sm" className="flex items-center gap-2">
            <RefreshCw className="h-4 w-4" />
            {t("governance.complianceDashboard.refresh", "Actualizar")}
          </Button>
        </div>

        {/* KPIs Principales */}
        <div className="grid grid-cols-4 gap-4">
          {/* % Completitud Compliance */}
          <Card className="border-primary hover:shadow-lg hover:border-primary/50 transition-all">
            <CardBody className="p-4">
              <div className="flex items-center justify-between">
                <div className="flex-grow">
                  <p className="text-xs font-medium text-muted-foreground mb-1">
                    {t("governance.complianceDashboard.completionRate")}
                  </p>
                  <h2 className="text-2xl font-bold text-primary mb-1">
                    {formatDecimal(data?.completionRate || 0)}%
                  </h2>
                  <p className="text-xs text-muted-foreground">
                    {data?.completedAssessments || 0}/{data?.totalAssessments || 0}
                  </p>
                </div>
                <CheckCircle className="w-8 h-8 text-primary flex-shrink-0" />
              </div>
            </CardBody>
          </Card>

          {/* FRIA Aprobadas */}
          <Card className="border-green-500 hover:shadow-lg hover:border-green-500/50 transition-all">
            <CardBody className="p-4">
              <div className="flex items-center justify-between">
                <div className="flex-grow">
                  <p className="text-xs font-medium text-muted-foreground mb-1">
                    {t("governance.complianceDashboard.friaApproved")}
                  </p>
                  <h2 className="text-2xl font-bold text-green-600 mb-1">
                    {formatDecimal(data?.friaApprovalRate || 0)}%
                  </h2>
                  <p className="text-xs text-muted-foreground">
                    {data?.approvedFria || 0}/{data?.totalFria || 0}
                  </p>
                </div>
                <FileText className="w-8 h-8 text-green-600 flex-shrink-0" />
              </div>
            </CardBody>
          </Card>

          {/* Registros EU */}
          <Card className="border-blue-500 hover:shadow-lg hover:border-blue-500/50 transition-all">
            <CardBody className="p-4">
              <div className="flex items-center justify-between">
                <div className="flex-grow">
                  <p className="text-xs font-medium text-muted-foreground mb-1">
                    {t("governance.complianceDashboard.euRegistrations")}
                  </p>
                  <h2 className="text-2xl font-bold text-blue-600 mb-1">
                    {formatDecimal(data?.euSubmissionRate || 0)}%
                  </h2>
                  <p className="text-xs text-muted-foreground">
                    {data?.submittedEuRegistrations || 0}/{data?.totalEuRegistrations || 0}
                  </p>
                </div>
                <Database className="w-8 h-8 text-blue-600 flex-shrink-0" />
              </div>
            </CardBody>
          </Card>

          {/* Score Promedio */}
          <Card className="border-yellow-500 hover:shadow-lg hover:border-yellow-500/50 transition-all">
            <CardBody className="p-4">
              <div className="flex items-center justify-between">
                <div className="flex-grow">
                  <p className="text-xs font-medium text-muted-foreground mb-1">
                    {t("governance.complianceDashboard.averageScore")}
                  </p>
                  <h2 className="text-2xl font-bold text-yellow-600 mb-1">
                    {formatDecimal(data?.averageComplianceScore || 0)}
                  </h2>
                  <p className="text-xs text-muted-foreground">
                    {data?.complianceStatus || "N/A"}
                  </p>
                </div>
                <TrendingUp className="w-8 h-8 text-yellow-600 flex-shrink-0" />
              </div>
            </CardBody>
          </Card>
        </div>

        {/* Estado de Clasificación, FRIA, Evaluaciones Pendientes y Alertas */}
        <div className="grid grid-cols-4 gap-4">
          {/* Estado de Clasificación */}
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
            <CardHeader className="pb-3">
              <CardTitle className="text-base font-medium">{t("governance.complianceDashboard.classificationStatus")}</CardTitle>
            </CardHeader>
            <CardBody className="pt-0">
              <div className="grid grid-cols-2 gap-3">
                <div className="flex items-center gap-2">
                  <AlertTriangle className="w-6 h-6 text-yellow-500 flex-shrink-0" />
                  <div>
                    <p className="text-xs font-medium mb-0">{t("governance.complianceDashboard.highRisk")}</p>
                    <p className="text-xl font-bold">{data?.totalAssessments || 0}</p>
                  </div>
                </div>
                <div className="flex items-center gap-2">
                  <CheckCircle className="w-6 h-6 text-green-500 flex-shrink-0" />
                  <div>
                    <p className="text-xs font-medium mb-0">{t("governance.complianceDashboard.completed")}</p>
                    <p className="text-xl font-bold">{data?.completedAssessments || 0}</p>
                  </div>
                </div>
              </div>
            </CardBody>
          </Card>

          {/* Estado de FRIA */}
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
            <CardHeader className="pb-3">
              <CardTitle className="text-base font-medium">{t("governance.complianceDashboard.friaStatus")}</CardTitle>
            </CardHeader>
            <CardBody className="pt-0">
              <div className="grid grid-cols-2 gap-3">
                <div className="flex items-center gap-2">
                  <FileText className="w-6 h-6 text-primary flex-shrink-0" />
                  <div>
                    <p className="text-xs font-medium mb-0">{t("governance.complianceDashboard.totalFria")}</p>
                    <p className="text-xl font-bold">{data?.totalFria || 0}</p>
                  </div>
                </div>
                <div className="flex items-center gap-2">
                  <CheckCircle className="w-6 h-6 text-green-500 flex-shrink-0" />
                  <div>
                    <p className="text-xs font-medium mb-0">{t("governance.complianceDashboard.approved")}</p>
                    <p className="text-xl font-bold">{data?.approvedFria || 0}</p>
                  </div>
                </div>
              </div>
            </CardBody>
          </Card>

          {/* Evaluaciones Técnicas Pendientes */}
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
            <CardHeader className="pb-3">
              <CardTitle className="text-base font-medium">{t("governance.complianceDashboard.pendingAssessments")}</CardTitle>
            </CardHeader>
            <CardBody className="pt-0">
              <div className="flex items-center gap-2">
                <Clock className="w-6 h-6 text-yellow-500 flex-shrink-0" />
                <div className="flex-grow">
                  <p className="text-xl font-bold mb-0">{data?.pendingAssessments || 0}</p>
                  <p className="text-xs text-muted-foreground">
                    {t("governance.complianceDashboard.pendingEvaluations")}
                  </p>
                </div>
              </div>
            </CardBody>
          </Card>

          {/* Alertas y Acciones Requeridas */}
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
            <CardHeader className="pb-3">
              <CardTitle className="text-base font-medium">{t("governance.complianceDashboard.alerts")}</CardTitle>
            </CardHeader>
            <CardBody className="pt-0">
              <div className="grid grid-cols-2 gap-3">
                <div className="p-2 bg-yellow-50 dark:bg-yellow-900/20 rounded border border-yellow-200 dark:border-yellow-800">
                  <div className="flex items-center gap-1.5 mb-1">
                    <Bell className="w-3.5 h-3.5 text-yellow-600" />
                    <p className="text-xs font-medium text-yellow-800 dark:text-yellow-200">
                      {t("governance.complianceDashboard.pendingFria")}
                    </p>
                  </div>
                  <p className="text-xl font-bold text-yellow-700 dark:text-yellow-300">
                    {data?.pendingFria || 0}
                  </p>
                </div>
                <div className="p-2 bg-blue-50 dark:bg-blue-900/20 rounded border border-blue-200 dark:border-blue-800">
                  <div className="flex items-center gap-1.5 mb-1">
                    <Database className="w-3.5 h-3.5 text-blue-600" />
                    <p className="text-xs font-medium text-blue-800 dark:text-blue-200">
                      {t("governance.complianceDashboard.pendingEuRegistrations")}
                    </p>
                  </div>
                  <p className="text-xl font-bold text-blue-700 dark:text-blue-300">
                    {data?.pendingEuRegistrations || 0}
                  </p>
                </div>
              </div>
            </CardBody>
          </Card>
        </div>

        {/* Sección de Evaluaciones de Conformidad (Art. 43 + Anexo VI) */}
        <div className="mt-6 space-y-4">
          <div className="space-y-1">
            <div className="flex items-center gap-2">
              <FileCheck className="w-5 h-5 text-green-500" />
              <h2 className="text-xl font-bold bg-gradient-to-r from-green-500 to-green-700 bg-clip-text text-transparent">
                {t("governance.complianceDashboard.conformity.title", "Evaluaciones de Conformidad")}
              </h2>
            </div>
            <p className="text-sm text-muted-foreground ml-7">
              {t("governance.complianceDashboard.conformity.subtitle", "Art. 43 EU AI Act - Evaluación de conformidad según Anexo VI")}
            </p>
          </div>

          {/* Métricas de Conformidad */}
          <div className="grid grid-cols-4 gap-4">
            <Card className="border-green-500 hover:shadow-lg hover:border-green-500/50 transition-all">
              <CardBody className="p-4">
                <div className="flex items-center justify-between">
                  <div className="flex-grow">
                    <p className="text-xs font-medium text-muted-foreground mb-1">
                      {t("governance.complianceDashboard.conformity.metrics.total", "Total Evaluaciones")}
                    </p>
                    <h2 className="text-2xl font-bold text-green-600">
                      {conformityMetrics.total}
                    </h2>
                  </div>
                  <FileCheck className="w-8 h-8 text-green-600 flex-shrink-0" />
                </div>
              </CardBody>
            </Card>

            <Card className="border-blue-500 hover:shadow-lg hover:border-blue-500/50 transition-all">
              <CardBody className="p-4">
                <div className="flex items-center justify-between">
                  <div className="flex-grow">
                    <p className="text-xs font-medium text-muted-foreground mb-1">
                      {t("governance.complianceDashboard.conformity.metrics.completed", "Completadas")}
                    </p>
                    <h2 className="text-2xl font-bold text-blue-600">
                      {conformityMetrics.completed}
                    </h2>
                  </div>
                  <CheckCircle className="w-8 h-8 text-blue-600 flex-shrink-0" />
                </div>
              </CardBody>
            </Card>

            <Card className="border-purple-500 hover:shadow-lg hover:border-purple-500/50 transition-all">
              <CardBody className="p-4">
                <div className="flex items-center justify-between">
                  <div className="flex-grow">
                    <p className="text-xs font-medium text-muted-foreground mb-1">
                      {t("governance.complianceDashboard.conformity.metrics.readyForCertification", "Listas para Certificación")}
                    </p>
                    <h2 className="text-2xl font-bold text-purple-600">
                      {conformityMetrics.readyForCertification}
                    </h2>
                  </div>
                  <Award className="w-8 h-8 text-purple-600 flex-shrink-0" />
                </div>
              </CardBody>
            </Card>

            <Card className="border-yellow-500 hover:shadow-lg hover:border-yellow-500/50 transition-all">
              <CardBody className="p-4">
                <div className="flex items-center justify-between">
                  <div className="flex-grow">
                    <p className="text-xs font-medium text-muted-foreground mb-1">
                      {t("governance.complianceDashboard.conformity.metrics.averageScore", "Score Promedio")}
                    </p>
                    <h2 className="text-2xl font-bold text-yellow-600">
                      {formatDecimal(conformityMetrics.averageScore * 100)}%
                    </h2>
                  </div>
                  <TrendingUp className="w-8 h-8 text-yellow-600 flex-shrink-0" />
                </div>
              </CardBody>
            </Card>
          </div>

          {/* Gráficos con Chart.js */}
          <div className="grid grid-cols-3 gap-3">
            {/* Gráfico de Distribución de Scores */}
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader className="pb-2">
                <CardTitle className="flex items-center gap-2 text-base font-medium">
                  <TrendingUp className="w-4 h-4" />
                  {t("governance.complianceDashboard.conformity.charts.scoreDistribution", "Distribución de Scores")}
                </CardTitle>
              </CardHeader>
              <CardBody className="pt-0">
                {mounted && chartData.scoreDistribution.length > 0 ? (
                  <div className="h-[220px] w-full">
                    <Bar
                      data={{
                        labels: chartData.scoreDistribution.map((item) => item.name),
                        datasets: [
                          {
                            label: t("governance.complianceDashboard.conformity.charts.labels.quantity", "Cantidad"),
                            data: chartData.scoreDistribution.map((item) => item.value),
                            backgroundColor: [
                              "rgba(16, 185, 129, 1)", // emerald más oscuro
                              "rgba(245, 158, 11, 1)", // amber más oscuro
                              "rgba(249, 115, 22, 1)", // orange más oscuro
                              "rgba(239, 68, 68, 1)", // red más oscuro
                            ],
                            borderColor: [
                              "rgb(5, 150, 105)",
                              "rgb(217, 119, 6)",
                              "rgb(234, 88, 12)",
                              "rgb(220, 38, 38)",
                            ],
                            borderWidth: 2,
                          },
                        ],
                      }}
                      options={chartOptions}
                    />
                  </div>
                ) : (
                  <div className="h-[220px] flex items-center justify-center text-muted-foreground">
                    {!mounted ? "Cargando..." : "No hay datos disponibles"}
                  </div>
                )}
              </CardBody>
            </Card>

            {/* Gráfico de Evolución Temporal */}
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader className="pb-2">
                <CardTitle className="flex items-center gap-2 text-base font-medium">
                  <TrendingUp className="w-4 h-4" />
                  {t("governance.complianceDashboard.conformity.charts.timeline", "Evolución Temporal")}
                </CardTitle>
              </CardHeader>
              <CardBody className="pt-0">
                {mounted && chartData.timelineData.length > 0 ? (
                  <div className="h-[220px]">
                    <Line
                      data={{
                        labels: chartData.timelineData.map((item) => item.month),
                        datasets: [
                          {
                            label: t("governance.complianceDashboard.conformity.charts.labels.evaluations", "Evaluaciones"),
                            data: chartData.timelineData.map((item) => item.Evaluaciones),
                            borderColor: "rgb(16, 185, 129)",
                            backgroundColor: "rgba(16, 185, 129, 0.1)",
                            fill: true,
                            tension: 0.4,
                          },
                          {
                            label: t("governance.complianceDashboard.conformity.charts.labels.fria", "FRIA"),
                            data: chartData.timelineData.map((item) => item.FRIA),
                            borderColor: "rgb(245, 158, 11)",
                            backgroundColor: "rgba(245, 158, 11, 0.1)",
                            fill: true,
                            tension: 0.4,
                          },
                          {
                            label: t("governance.complianceDashboard.conformity.charts.labels.euRegistrations", "Registros EU"),
                            data: chartData.timelineData.map((item) => item["Registros EU"]),
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
                ) : (
                  <div className="h-[220px] flex items-center justify-center text-muted-foreground">
                    {!mounted ? "Cargando..." : "No hay datos disponibles"}
                  </div>
                )}
              </CardBody>
            </Card>

            {/* Gráfico de Donut - Distribución de Estados */}
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader className="pb-2">
                <CardTitle className="flex items-center gap-2 text-base font-medium">
                  <TrendingUp className="w-4 h-4" />
                  {t("governance.complianceDashboard.conformity.charts.statusDistribution", "Distribución de Estados")}
                </CardTitle>
              </CardHeader>
              <CardBody className="pt-0">
                {mounted && chartData.statusDistribution.length > 0 ? (
                  <div className="h-[220px]">
                    <Doughnut
                      data={{
                        labels: chartData.statusDistribution.map((item) => item.name),
                        datasets: [
                          {
                            data: chartData.statusDistribution.map((item) => item.value),
                            backgroundColor: [
                              "rgba(16, 185, 129, 0.8)", // emerald
                              "rgba(245, 158, 11, 0.8)", // amber
                              "rgba(249, 115, 22, 0.8)", // orange
                              "rgba(59, 130, 246, 0.8)", // blue
                            ],
                            borderColor: [
                              "rgb(16, 185, 129)",
                              "rgb(245, 158, 11)",
                              "rgb(249, 115, 22)",
                              "rgb(59, 130, 246)",
                            ],
                            borderWidth: 2,
                          },
                        ],
                      }}
                      options={{
                        ...chartOptions,
                        plugins: {
                          ...chartOptions.plugins,
                          legend: {
                            ...chartOptions.plugins.legend,
                            position: 'bottom' as const,
                          },
                        },
                      }}
                    />
                  </div>
                ) : (
                  <div className="h-[220px] flex items-center justify-center text-muted-foreground">
                    {!mounted ? "Cargando..." : "No hay datos disponibles"}
                  </div>
                )}
              </CardBody>
            </Card>
          </div>

          {/* Overview de Compliance y Listado de Evaluaciones */}
          <div className="grid grid-cols-3 gap-3">
            {/* Gráfico de Barras - Overview de Compliance */}
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader className="pb-2">
                <CardTitle className="flex items-center gap-2 text-base font-medium">
                  <TrendingUp className="w-4 h-4" />
                  {t("governance.complianceDashboard.conformity.charts.complianceOverview", "Overview de Compliance")}
                </CardTitle>
              </CardHeader>
              <CardBody className="pt-0">
                {mounted && chartData.complianceOverview.length > 0 ? (
                  <div className="h-[500px]">
                    <Bar
                      data={{
                        labels: chartData.complianceOverview.map((item) => item.name),
                        datasets: [
                          {
                            label: t("governance.complianceDashboard.conformity.charts.labels.completed", "Completadas"),
                            data: chartData.complianceOverview.map((item) => item.Completadas),
                            backgroundColor: "rgba(16, 185, 129, 1)",
                            borderColor: "rgb(5, 150, 105)",
                            borderWidth: 2,
                          },
                          {
                            label: t("governance.complianceDashboard.conformity.charts.labels.total", "Total"),
                            data: chartData.complianceOverview.map((item) => item.Total),
                            backgroundColor: "rgba(59, 130, 246, 1)",
                            borderColor: "rgb(37, 99, 235)",
                            borderWidth: 2,
                          },
                        ],
                      }}
                      options={chartOptions}
                    />
                  </div>
                ) : (
                  <div className="h-[500px] flex items-center justify-center text-muted-foreground">
                    {!mounted ? "Cargando..." : "No hay datos disponibles"}
                  </div>
                )}
              </CardBody>
            </Card>

            {/* Listado de Evaluaciones con Filtros */}
            <Card className="backdrop-blur-md bg-background/60 border-border/50 col-span-2">
            <CardHeader>
              <div className="flex items-center justify-between">
                <CardTitle className="flex items-center gap-2">
                  <FileCheck className="w-5 h-5" />
                  {t("governance.complianceDashboard.conformity.list.title", "Listado de Evaluaciones")}
                </CardTitle>
                <Button onClick={loadDashboard} variant="outline" size="sm">
                  <RefreshCw className="w-4 h-4" />
                </Button>
              </div>
            </CardHeader>
            <CardBody className="flex flex-col h-full">
              {/* Filtros */}
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6 flex-shrink-0">
                <div>
                  <label className="block text-sm font-medium mb-2">
                    <Filter className="w-4 h-4 inline mr-1" />
                    {t("governance.complianceDashboard.conformity.list.filterStatus", "Estado")}
                  </label>
                  <select
                    value={filterStatus}
                    onChange={(e) => setFilterStatus(e.target.value)}
                    className="w-full px-3 py-2 border rounded-md bg-background"
                  >
                    <option value="all">{t("governance.complianceDashboard.conformity.list.all", "Todos")}</option>
                    <option value="PENDING">{t("governance.complianceDashboard.conformity.list.pending", "Pendiente")}</option>
                    <option value="IN_PROGRESS">{t("governance.complianceDashboard.conformity.list.inProgress", "En Progreso")}</option>
                    <option value="COMPLETED">{t("governance.complianceDashboard.conformity.list.completed", "Completada")}</option>
                    <option value="REJECTED">{t("governance.complianceDashboard.conformity.list.rejected", "Rechazada")}</option>
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium mb-2">
                    {t("governance.complianceDashboard.conformity.list.filterProject", "Proyecto")}
                  </label>
                  <Input
                    type="text"
                    placeholder={t("governance.complianceDashboard.conformity.list.searchProject", "Buscar por proyecto...")}
                    value={filterProject}
                    onChange={(e) => setFilterProject(e.target.value)}
                  />
                </div>
                <div className="flex items-end">
                  <Button
                    variant="outline"
                    onClick={() => {
                      setFilterStatus("all");
                      setFilterProject("");
                    }}
                    className="w-full"
                  >
                    {t("governance.complianceDashboard.conformity.list.clearFilters", "Limpiar Filtros")}
                  </Button>
                </div>
              </div>

              {/* Tabla de Evaluaciones */}
              <div className="overflow-x-auto overflow-y-auto max-h-[500px] flex-1">
                <table className="w-full text-sm">
                  <thead>
                    <tr className="border-b">
                      <th className="text-left p-2">{t("governance.complianceDashboard.conformity.list.project", "Proyecto")}</th>
                      <th className="text-left p-2">{t("governance.complianceDashboard.conformity.list.date", "Fecha")}</th>
                      <th className="text-center p-2">{t("governance.complianceDashboard.conformity.list.step2", "Step 2")}</th>
                      <th className="text-center p-2">{t("governance.complianceDashboard.conformity.list.step3", "Step 3")}</th>
                      <th className="text-center p-2">{t("governance.complianceDashboard.conformity.list.overall", "Overall")}</th>
                      <th className="text-center p-2">{t("governance.complianceDashboard.conformity.list.status", "Estado")}</th>
                      <th className="text-center p-2">{t("governance.complianceDashboard.conformity.list.actions", "Acciones")}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredAssessments.length === 0 ? (
                      <tr>
                        <td colSpan={7} className="text-center p-8 text-muted-foreground">
                          {t("governance.complianceDashboard.conformity.list.noResults", "No se encontraron evaluaciones")}
                        </td>
                      </tr>
                    ) : (
                      filteredAssessments.map((assessment) => (
                        <tr key={assessment.id} className="border-b hover:bg-muted/50">
                          <td className="p-2 font-medium">{assessment.projectName}</td>
                          <td className="p-2 text-muted-foreground">
                            {new Date(assessment.assessmentDate).toLocaleDateString()}
                          </td>
                          <td className="p-2 text-center">
                            <span className={assessment.step2QmsScore !== null ? "font-semibold" : "text-muted-foreground"}>
                              {formatScore(assessment.step2QmsScore)}
                            </span>
                          </td>
                          <td className="p-2 text-center">
                            <span className={assessment.step3DocScore !== null ? "font-semibold" : "text-muted-foreground"}>
                              {formatScore(assessment.step3DocScore)}
                            </span>
                          </td>
                          <td className="p-2 text-center">
                            <span className={assessment.overallScore !== null ? "font-bold" : "text-muted-foreground"}>
                              {formatScore(assessment.overallScore)}
                            </span>
                          </td>
                          <td className="p-2 text-center">{getStatusBadge(assessment.status)}</td>
                          <td className="p-2">
                            <div className="flex items-center justify-center gap-2">
                              <Button
                                variant="outline"
                                size="sm"
                                onClick={() => router.push(`/governance/compliance/assessments/${assessment.id}`)}
                                title={t("governance.complianceDashboard.conformity.list.viewDetails", "Ver detalle")}
                              >
                                <Eye className="w-4 h-4" />
                              </Button>
                              {assessment.status === "IN_PROGRESS" && (
                                <Button
                                  variant="outline"
                                  size="sm"
                                  onClick={() => router.push(`/governance/compliance/conformity-review?assessmentId=${assessment.id}`)}
                                  title={t("governance.complianceDashboard.conformity.list.continue", "Continuar evaluación")}
                                >
                                  <Play className="w-4 h-4" />
                                </Button>
                              )}
                            </div>
                          </td>
                        </tr>
                      ))
                    )}
                  </tbody>
                </table>
              </div>
            </CardBody>
          </Card>
          </div>
        </div>
    </div>
  );
}


