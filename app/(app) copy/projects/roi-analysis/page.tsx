"use client";

import { useState, useMemo } from "react";
import { useTranslation } from "@/app/config/i18n";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { TrendingUp, DollarSign, BarChart3, Target, Brain, Bot, Calendar, FolderKanban, Search } from "lucide-react";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  ArcElement,
  Title,
  Tooltip,
  Legend,
  Filler,
} from "chart.js";
import { Line, Bar, Doughnut } from "react-chartjs-2";

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  ArcElement,
  Title,
  Tooltip,
  Legend,
  Filler
);

interface ProjectROIData {
  projectId: string;
  projectName: string;
  dataByYear: Record<number, {
    year: number;
    investment: number;
    return: number;
    roi: number;
    roiPercentage: number;
    modelsImpact: number;
    agentsImpact: number;
    monthlyData: Array<{ month: string; investment: number; return: number; roi: number }>;
    cumulativeROI: Array<{ month: string; cumulativeROI: number }>;
  }>;
}

// Mock data estructurado por proyecto y año
const mockProjectsROI: ProjectROIData[] = [
  {
    projectId: "1",
    projectName: "Modelo de Detección de Fraude",
    dataByYear: {
      2024: {
        year: 2024,
        investment: 500000,
        return: 650000,
        roi: 150000,
        roiPercentage: 30.0,
        modelsImpact: 85,
        agentsImpact: 65,
        monthlyData: [
          { month: "Ene", investment: 40000, return: 35000, roi: -5000 },
          { month: "Feb", investment: 42000, return: 50000, roi: 8000 },
          { month: "Mar", investment: 45000, return: 55000, roi: 10000 },
          { month: "Abr", investment: 48000, return: 60000, roi: 12000 },
          { month: "May", investment: 50000, return: 65000, roi: 15000 },
          { month: "Jun", investment: 52000, return: 68000, roi: 16000 },
          { month: "Jul", investment: 54000, return: 70000, roi: 16000 },
          { month: "Ago", investment: 56000, return: 72000, roi: 16000 },
          { month: "Sep", investment: 58000, return: 75000, roi: 17000 },
          { month: "Oct", investment: 60000, return: 78000, roi: 18000 },
          { month: "Nov", investment: 62000, return: 80000, roi: 18000 },
          { month: "Dic", investment: 64000, return: 82000, roi: 18000 },
        ],
        cumulativeROI: [
          { month: "Ene", cumulativeROI: -1.0 },
          { month: "Feb", cumulativeROI: 1.5 },
          { month: "Mar", cumulativeROI: 4.0 },
          { month: "Abr", cumulativeROI: 7.0 },
          { month: "May", cumulativeROI: 10.5 },
          { month: "Jun", cumulativeROI: 14.0 },
          { month: "Jul", cumulativeROI: 17.5 },
          { month: "Ago", cumulativeROI: 21.0 },
          { month: "Sep", cumulativeROI: 24.5 },
          { month: "Oct", cumulativeROI: 28.0 },
          { month: "Nov", cumulativeROI: 29.5 },
          { month: "Dic", cumulativeROI: 30.0 },
        ],
      },
      2023: {
        year: 2023,
        investment: 450000,
        return: 520000,
        roi: 70000,
        roiPercentage: 15.6,
        modelsImpact: 80,
        agentsImpact: 60,
        monthlyData: [
          { month: "Ene", investment: 35000, return: 30000, roi: -5000 },
          { month: "Feb", investment: 37000, return: 40000, roi: 3000 },
          { month: "Mar", investment: 39000, return: 45000, roi: 6000 },
          { month: "Abr", investment: 41000, return: 50000, roi: 9000 },
          { month: "May", investment: 43000, return: 55000, roi: 12000 },
          { month: "Jun", investment: 45000, return: 58000, roi: 13000 },
          { month: "Jul", investment: 47000, return: 60000, roi: 13000 },
          { month: "Ago", investment: 49000, return: 62000, roi: 13000 },
          { month: "Sep", investment: 51000, return: 65000, roi: 14000 },
          { month: "Oct", investment: 53000, return: 68000, roi: 15000 },
          { month: "Nov", investment: 55000, return: 70000, roi: 15000 },
          { month: "Dic", investment: 57000, return: 72000, roi: 15000 },
        ],
        cumulativeROI: [
          { month: "Ene", cumulativeROI: -1.1 },
          { month: "Feb", cumulativeROI: 0.5 },
          { month: "Mar", cumulativeROI: 2.5 },
          { month: "Abr", cumulativeROI: 5.0 },
          { month: "May", cumulativeROI: 8.0 },
          { month: "Jun", cumulativeROI: 11.0 },
          { month: "Jul", cumulativeROI: 13.5 },
          { month: "Ago", cumulativeROI: 15.0 },
          { month: "Sep", cumulativeROI: 15.3 },
          { month: "Oct", cumulativeROI: 15.5 },
          { month: "Nov", cumulativeROI: 15.6 },
          { month: "Dic", cumulativeROI: 15.6 },
        ],
      },
    },
  },
  {
    projectId: "2",
    projectName: "Agente Virtual de Atención",
    dataByYear: {
      2024: {
        year: 2024,
        investment: 750000,
        return: 900000,
        roi: 150000,
        roiPercentage: 20.0,
        modelsImpact: 70,
        agentsImpact: 80,
        monthlyData: [
          { month: "Ene", investment: 60000, return: 50000, roi: -10000 },
          { month: "Feb", investment: 62000, return: 70000, roi: 8000 },
          { month: "Mar", investment: 64000, return: 75000, roi: 11000 },
          { month: "Abr", investment: 66000, return: 80000, roi: 14000 },
          { month: "May", investment: 68000, return: 85000, roi: 17000 },
          { month: "Jun", investment: 70000, return: 90000, roi: 20000 },
          { month: "Jul", investment: 72000, return: 95000, roi: 23000 },
          { month: "Ago", investment: 74000, return: 100000, roi: 26000 },
          { month: "Sep", investment: 76000, return: 105000, roi: 29000 },
          { month: "Oct", investment: 78000, return: 110000, roi: 32000 },
          { month: "Nov", investment: 80000, return: 115000, roi: 35000 },
          { month: "Dic", investment: 82000, return: 120000, roi: 38000 },
        ],
        cumulativeROI: [
          { month: "Ene", cumulativeROI: -1.3 },
          { month: "Feb", cumulativeROI: 0.0 },
          { month: "Mar", cumulativeROI: 2.0 },
          { month: "Abr", cumulativeROI: 4.5 },
          { month: "May", cumulativeROI: 7.0 },
          { month: "Jun", cumulativeROI: 9.5 },
          { month: "Jul", cumulativeROI: 12.0 },
          { month: "Ago", cumulativeROI: 14.5 },
          { month: "Sep", cumulativeROI: 17.0 },
          { month: "Oct", cumulativeROI: 19.0 },
          { month: "Nov", cumulativeROI: 19.5 },
          { month: "Dic", cumulativeROI: 20.0 },
        ],
      },
      2023: {
        year: 2023,
        investment: 680000,
        return: 750000,
        roi: 70000,
        roiPercentage: 10.3,
        modelsImpact: 65,
        agentsImpact: 75,
        monthlyData: [
          { month: "Ene", investment: 55000, return: 45000, roi: -10000 },
          { month: "Feb", investment: 57000, return: 60000, roi: 3000 },
          { month: "Mar", investment: 59000, return: 65000, roi: 6000 },
          { month: "Abr", investment: 61000, return: 70000, roi: 9000 },
          { month: "May", investment: 63000, return: 75000, roi: 12000 },
          { month: "Jun", investment: 65000, return: 80000, roi: 15000 },
          { month: "Jul", investment: 67000, return: 85000, roi: 18000 },
          { month: "Ago", investment: 69000, return: 90000, roi: 21000 },
          { month: "Sep", investment: 71000, return: 95000, roi: 24000 },
          { month: "Oct", investment: 73000, return: 100000, roi: 27000 },
          { month: "Nov", investment: 75000, return: 105000, roi: 30000 },
          { month: "Dic", investment: 77000, return: 110000, roi: 33000 },
        ],
        cumulativeROI: [
          { month: "Ene", cumulativeROI: -1.5 },
          { month: "Feb", cumulativeROI: -0.5 },
          { month: "Mar", cumulativeROI: 1.0 },
          { month: "Abr", cumulativeROI: 3.0 },
          { month: "May", cumulativeROI: 5.0 },
          { month: "Jun", cumulativeROI: 7.0 },
          { month: "Jul", cumulativeROI: 8.5 },
          { month: "Ago", cumulativeROI: 9.5 },
          { month: "Sep", cumulativeROI: 10.0 },
          { month: "Oct", cumulativeROI: 10.2 },
          { month: "Nov", cumulativeROI: 10.3 },
          { month: "Dic", cumulativeROI: 10.3 },
        ],
      },
    },
  },
  {
    projectId: "3",
    projectName: "Análisis Predictivo de Datos",
    dataByYear: {
      2024: {
        year: 2024,
        investment: 300000,
        return: 420000,
        roi: 120000,
        roiPercentage: 40.0,
        modelsImpact: 95,
        agentsImpact: 75,
        monthlyData: [
          { month: "Ene", investment: 24000, return: 28000, roi: 4000 },
          { month: "Feb", investment: 25000, return: 32000, roi: 7000 },
          { month: "Mar", investment: 26000, return: 35000, roi: 9000 },
          { month: "Abr", investment: 27000, return: 38000, roi: 11000 },
          { month: "May", investment: 28000, return: 40000, roi: 12000 },
          { month: "Jun", investment: 29000, return: 42000, roi: 13000 },
          { month: "Jul", investment: 30000, return: 44000, roi: 14000 },
          { month: "Ago", investment: 31000, return: 46000, roi: 15000 },
          { month: "Sep", investment: 32000, return: 48000, roi: 16000 },
          { month: "Oct", investment: 33000, return: 50000, roi: 17000 },
          { month: "Nov", investment: 34000, return: 52000, roi: 18000 },
          { month: "Dic", investment: 35000, return: 54000, roi: 19000 },
        ],
        cumulativeROI: [
          { month: "Ene", cumulativeROI: 1.3 },
          { month: "Feb", cumulativeROI: 3.5 },
          { month: "Mar", cumulativeROI: 6.5 },
          { month: "Abr", cumulativeROI: 10.0 },
          { month: "May", cumulativeROI: 14.0 },
          { month: "Jun", cumulativeROI: 18.0 },
          { month: "Jul", cumulativeROI: 22.0 },
          { month: "Ago", cumulativeROI: 26.0 },
          { month: "Sep", cumulativeROI: 30.0 },
          { month: "Oct", cumulativeROI: 34.0 },
          { month: "Nov", cumulativeROI: 38.0 },
          { month: "Dic", cumulativeROI: 40.0 },
        ],
      },
    },
  },
  {
    projectId: "4",
    projectName: "Cumplimiento Normativo EU AI Act",
    dataByYear: {
      2024: {
        year: 2024,
        investment: 450000,
        return: 580000,
        roi: 130000,
        roiPercentage: 28.9,
        modelsImpact: 60,
        agentsImpact: 40,
        monthlyData: [
          { month: "Ene", investment: 36000, return: 40000, roi: 4000 },
          { month: "Feb", investment: 37000, return: 45000, roi: 8000 },
          { month: "Mar", investment: 38000, return: 50000, roi: 12000 },
          { month: "Abr", investment: 39000, return: 55000, roi: 16000 },
          { month: "May", investment: 40000, return: 60000, roi: 20000 },
          { month: "Jun", investment: 41000, return: 65000, roi: 24000 },
          { month: "Jul", investment: 42000, return: 70000, roi: 28000 },
          { month: "Ago", investment: 43000, return: 75000, roi: 32000 },
          { month: "Sep", investment: 44000, return: 80000, roi: 36000 },
          { month: "Oct", investment: 45000, return: 85000, roi: 40000 },
          { month: "Nov", investment: 46000, return: 90000, roi: 44000 },
          { month: "Dic", investment: 47000, return: 95000, roi: 48000 },
        ],
        cumulativeROI: [
          { month: "Ene", cumulativeROI: 0.9 },
          { month: "Feb", cumulativeROI: 2.5 },
          { month: "Mar", cumulativeROI: 5.0 },
          { month: "Abr", cumulativeROI: 8.0 },
          { month: "May", cumulativeROI: 11.5 },
          { month: "Jun", cumulativeROI: 15.0 },
          { month: "Jul", cumulativeROI: 18.5 },
          { month: "Ago", cumulativeROI: 22.0 },
          { month: "Sep", cumulativeROI: 25.5 },
          { month: "Oct", cumulativeROI: 27.5 },
          { month: "Nov", cumulativeROI: 28.5 },
          { month: "Dic", cumulativeROI: 28.9 },
        ],
      },
    },
  },
  {
    projectId: "5",
    projectName: "Clasificación de Documentos",
    dataByYear: {
      2024: {
        year: 2024,
        investment: 500000,
        return: 650000,
        roi: 150000,
        roiPercentage: 30.0,
        modelsImpact: 90,
        agentsImpact: 10,
        monthlyData: [
          { month: "Ene", investment: 40000, return: 45000, roi: 5000 },
          { month: "Feb", investment: 42000, return: 50000, roi: 8000 },
          { month: "Mar", investment: 44000, return: 55000, roi: 11000 },
          { month: "Abr", investment: 46000, return: 60000, roi: 14000 },
          { month: "May", investment: 48000, return: 65000, roi: 17000 },
          { month: "Jun", investment: 50000, return: 70000, roi: 20000 },
          { month: "Jul", investment: 52000, return: 75000, roi: 23000 },
          { month: "Ago", investment: 54000, return: 80000, roi: 26000 },
          { month: "Sep", investment: 56000, return: 85000, roi: 29000 },
          { month: "Oct", investment: 58000, return: 90000, roi: 32000 },
          { month: "Nov", investment: 60000, return: 95000, roi: 35000 },
          { month: "Dic", investment: 62000, return: 100000, roi: 38000 },
        ],
        cumulativeROI: [
          { month: "Ene", cumulativeROI: 1.0 },
          { month: "Feb", cumulativeROI: 2.5 },
          { month: "Mar", cumulativeROI: 5.0 },
          { month: "Abr", cumulativeROI: 8.0 },
          { month: "May", cumulativeROI: 11.5 },
          { month: "Jun", cumulativeROI: 15.0 },
          { month: "Jul", cumulativeROI: 18.5 },
          { month: "Ago", cumulativeROI: 22.0 },
          { month: "Sep", cumulativeROI: 25.5 },
          { month: "Oct", cumulativeROI: 28.0 },
          { month: "Nov", cumulativeROI: 29.5 },
          { month: "Dic", cumulativeROI: 30.0 },
        ],
      },
    },
  },
];

export default function ProjectROIAnalysisPage() {
  const { t } = useTranslation();
  const [selectedProjectId, setSelectedProjectId] = useState<string>(mockProjectsROI[0]?.projectId || "");
  const [selectedYear, setSelectedYear] = useState(2024);
  const [compareYears, setCompareYears] = useState(false);
  const [searchQuery, setSearchQuery] = useState("");

  // Obtener proyecto seleccionado
  const selectedProject = mockProjectsROI.find((p) => p.projectId === selectedProjectId);

  // Obtener datos del año seleccionado
  const currentYearData = selectedProject?.dataByYear[selectedYear];

  // Obtener años disponibles para el proyecto seleccionado
  const availableYears = selectedProject
    ? Object.keys(selectedProject.dataByYear)
        .map(Number)
        .sort((a, b) => b - a)
    : [];

  // Obtener datos del año anterior para comparación
  const previousYear = selectedYear - 1;
  const previousYearData = selectedProject?.dataByYear[previousYear];

  // Filtrar proyectos por búsqueda
  const filteredProjects = useMemo(() => {
    if (!searchQuery) return mockProjectsROI;
    return mockProjectsROI.filter((p) =>
      p.projectName.toLowerCase().includes(searchQuery.toLowerCase())
    );
  }, [searchQuery]);

  // Gráfico mensual de inversión vs retorno
  const monthlyChartData = currentYearData
    ? {
        labels: currentYearData.monthlyData.map((d) => d.month),
        datasets: [
          {
            label: t("projects.roiAnalysis.investment", "Inversión"),
            data: currentYearData.monthlyData.map((d) => d.investment / 1000),
            borderColor: "rgb(99, 102, 241)",
            backgroundColor: "rgba(99, 102, 241, 0.1)",
            fill: true,
            tension: 0.4,
          },
          {
            label: t("projects.roiAnalysis.return", "Retorno"),
            data: currentYearData.monthlyData.map((d) => d.return / 1000),
            borderColor: "rgb(34, 197, 94)",
            backgroundColor: "rgba(34, 197, 94, 0.1)",
            fill: true,
            tension: 0.4,
          },
        ],
      }
    : null;

  // Gráfico de ROI acumulado (con comparación de años si está activo)
  const cumulativeROIData = currentYearData
    ? {
        labels: currentYearData.cumulativeROI.map((d) => d.month),
        datasets: [
          {
            label: `${selectedYear} - ${t("projects.roiAnalysis.cumulativeROI", "ROI Acumulado %")}`,
            data: currentYearData.cumulativeROI.map((d) => d.cumulativeROI),
            borderColor: "rgb(99, 102, 241)",
            backgroundColor: "rgba(99, 102, 241, 0.1)",
            fill: true,
            tension: 0.4,
            pointRadius: 4,
            pointHoverRadius: 6,
          },
          ...(compareYears && previousYearData
            ? [
                {
                  label: `${previousYear} - ${t("projects.roiAnalysis.cumulativeROI", "ROI Acumulado %")}`,
                  data: previousYearData.cumulativeROI.map((d) => d.cumulativeROI),
                  borderColor: "rgb(168, 85, 247)",
                  backgroundColor: "rgba(168, 85, 247, 0.1)",
                  fill: true,
                  tension: 0.4,
                  pointRadius: 4,
                  pointHoverRadius: 6,
                },
              ]
            : []),
        ],
      }
    : null;

  // Gráfico comparativo de inversión vs retorno entre años
  const comparisonChartData =
    compareYears && currentYearData && previousYearData
      ? {
          labels: currentYearData.monthlyData.map((d) => d.month),
          datasets: [
            {
              label: `${selectedYear} - ${t("projects.roiAnalysis.investment", "Inversión")}`,
              data: currentYearData.monthlyData.map((d) => d.investment / 1000),
              borderColor: "rgb(99, 102, 241)",
              backgroundColor: "rgba(99, 102, 241, 0.1)",
              fill: false,
              tension: 0.4,
            },
            {
              label: `${selectedYear} - ${t("projects.roiAnalysis.return", "Retorno")}`,
              data: currentYearData.monthlyData.map((d) => d.return / 1000),
              borderColor: "rgb(34, 197, 94)",
              backgroundColor: "rgba(34, 197, 94, 0.1)",
              fill: false,
              tension: 0.4,
            },
            {
              label: `${previousYear} - ${t("projects.roiAnalysis.investment", "Inversión")}`,
              data: previousYearData.monthlyData.map((d) => d.investment / 1000),
              borderColor: "rgb(251, 146, 60)",
              backgroundColor: "rgba(251, 146, 60, 0.1)",
              fill: false,
              tension: 0.4,
              borderDash: [5, 5],
            },
            {
              label: `${previousYear} - ${t("projects.roiAnalysis.return", "Retorno")}`,
              data: previousYearData.monthlyData.map((d) => d.return / 1000),
              borderColor: "rgb(236, 72, 153)",
              backgroundColor: "rgba(236, 72, 153, 0.1)",
              fill: false,
              tension: 0.4,
              borderDash: [5, 5],
            },
          ],
        }
      : null;

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: "top" as const,
        labels: {
          color: "rgb(148, 163, 184)",
        },
      },
      tooltip: {
        backgroundColor: "rgba(15, 23, 42, 0.9)",
        titleColor: "rgb(255, 255, 255)",
        bodyColor: "rgb(255, 255, 255)",
        borderColor: "rgba(148, 163, 184, 0.2)",
        borderWidth: 1,
      },
    },
    scales: {
      y: {
        beginAtZero: true,
        ticks: {
          color: "rgb(148, 163, 184)",
        },
        grid: {
          color: "rgba(148, 163, 184, 0.1)",
        },
      },
      x: {
        ticks: {
          color: "rgb(148, 163, 184)",
        },
        grid: {
          color: "rgba(148, 163, 184, 0.1)",
        },
      },
    },
  };

  const doughnutOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: "bottom" as const,
        labels: {
          color: "rgb(148, 163, 184)",
        },
      },
      tooltip: {
        backgroundColor: "rgba(15, 23, 42, 0.9)",
        titleColor: "rgb(255, 255, 255)",
        bodyColor: "rgb(255, 255, 255)",
        borderColor: "rgba(148, 163, 184, 0.2)",
        borderWidth: 1,
      },
    },
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <TrendingUp className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("projects.roiAnalysis.title", "Análisis de ROI de IA")}
            </h1>
          </div>
        </div>

        {/* Selector de Proyecto */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardContent className="pt-6">
            <div className="space-y-4">
              <div className="flex items-center gap-2">
                <FolderKanban className="w-5 h-5 text-primary" />
                <label className="text-sm font-medium">
                  {t("projects.roiAnalysis.selectProject", "Seleccionar Proyecto")}
                </label>
              </div>
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <input
                  type="text"
                  placeholder={t("projects.roiAnalysis.searchProject", "Buscar proyecto...")}
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 bg-background/60 backdrop-blur-md border border-border/50 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary text-foreground mb-3"
                />
              </div>
              <select
                value={selectedProjectId}
                onChange={(e) => {
                  setSelectedProjectId(e.target.value);
                  // Resetear año al cambiar proyecto
                  const project = mockProjectsROI.find((p) => p.projectId === e.target.value);
                  if (project) {
                    const years = Object.keys(project.dataByYear).map(Number).sort((a, b) => b - a);
                    setSelectedYear(years[0] || 2024);
                  }
                }}
                className="w-full px-3 py-2 bg-background border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary text-foreground"
              >
                {filteredProjects.map((project) => (
                  <option key={project.projectId} value={project.projectId}>
                    {project.projectName}
                  </option>
                ))}
              </select>
            </div>
          </CardContent>
        </Card>

        {/* Selectores de Año y Comparación */}
        {selectedProject && (
          <div className="flex items-center justify-between gap-4">
            <div className="flex items-center gap-3">
              <Calendar className="w-4 h-4 text-muted-foreground" />
              <label className="text-sm font-medium">
                {t("projects.roiAnalysis.selectYear", "Año")}:
              </label>
              <select
                value={selectedYear}
                onChange={(e) => setSelectedYear(Number(e.target.value))}
                className="px-3 py-2 bg-background border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary text-foreground"
              >
                {availableYears.map((year) => (
                  <option key={year} value={year}>
                    {year}
                  </option>
                ))}
              </select>
            </div>
            {previousYearData && (
              <label className="flex items-center gap-2 text-sm cursor-pointer">
                <input
                  type="checkbox"
                  checked={compareYears}
                  onChange={(e) => setCompareYears(e.target.checked)}
                  className="w-4 h-4"
                />
                {t("projects.roiAnalysis.compareYears", `Comparar con ${previousYear}`)}
              </label>
            )}
          </div>
        )}

        {currentYearData ? (
          <>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
              <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                  <CardTitle className="text-sm font-medium">
                    {t("projects.roiAnalysis.totalInvestment", "Inversión Total")}
                  </CardTitle>
                  <DollarSign className="h-4 w-4 text-primary" />
                </CardHeader>
                <CardContent>
                  <div className="text-2xl font-bold">
                    ${(currentYearData.investment / 1000).toLocaleString()}K
                  </div>
                  {compareYears && previousYearData && (
                    <p className="text-xs text-muted-foreground mt-1">
                      {previousYear}: ${(previousYearData.investment / 1000).toLocaleString()}K
                      {currentYearData.investment > previousYearData.investment ? (
                        <span className="text-red-500 ml-2">
                          ↑ {(((currentYearData.investment - previousYearData.investment) / previousYearData.investment) * 100).toFixed(1)}%
                        </span>
                      ) : (
                        <span className="text-green-500 ml-2">
                          ↓ {(((previousYearData.investment - currentYearData.investment) / previousYearData.investment) * 100).toFixed(1)}%
                        </span>
                      )}
                    </p>
                  )}
                </CardContent>
              </Card>

              <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                  <CardTitle className="text-sm font-medium">
                    {t("projects.roiAnalysis.totalReturn", "Retorno Total")}
                  </CardTitle>
                  <Target className="h-4 w-4 text-green-500" />
                </CardHeader>
                <CardContent>
                  <div className="text-2xl font-bold text-green-500">
                    ${(currentYearData.return / 1000).toLocaleString()}K
                  </div>
                  {compareYears && previousYearData && (
                    <p className="text-xs text-muted-foreground mt-1">
                      {previousYear}: ${(previousYearData.return / 1000).toLocaleString()}K
                      {currentYearData.return > previousYearData.return ? (
                        <span className="text-green-500 ml-2">
                          ↑ {(((currentYearData.return - previousYearData.return) / previousYearData.return) * 100).toFixed(1)}%
                        </span>
                      ) : (
                        <span className="text-red-500 ml-2">
                          ↓ {(((previousYearData.return - currentYearData.return) / previousYearData.return) * 100).toFixed(1)}%
                        </span>
                      )}
                    </p>
                  )}
                </CardContent>
              </Card>

              <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                  <CardTitle className="text-sm font-medium">
                    {t("projects.roiAnalysis.netROI", "ROI Neto")}
                  </CardTitle>
                  <TrendingUp className="h-4 w-4 text-primary" />
                </CardHeader>
                <CardContent>
                  <div className="text-2xl font-bold">${(currentYearData.roi / 1000).toLocaleString()}K</div>
                  {compareYears && previousYearData && (
                    <p className="text-xs text-muted-foreground mt-1">
                      {previousYear}: ${(previousYearData.roi / 1000).toLocaleString()}K
                    </p>
                  )}
                </CardContent>
              </Card>

              <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                  <CardTitle className="text-sm font-medium">
                    {t("projects.roiAnalysis.roiPercentage", "ROI %")}
                  </CardTitle>
                  <BarChart3 className="h-4 w-4 text-primary" />
                </CardHeader>
                <CardContent>
                  <div className="text-2xl font-bold">{currentYearData.roiPercentage.toFixed(1)}%</div>
                  {compareYears && previousYearData && (
                    <p className="text-xs text-muted-foreground mt-1">
                      {previousYear}: {previousYearData.roiPercentage.toFixed(1)}%
                      {currentYearData.roiPercentage > previousYearData.roiPercentage ? (
                        <span className="text-green-500 ml-2">
                          ↑ {(currentYearData.roiPercentage - previousYearData.roiPercentage).toFixed(1)}pp
                        </span>
                      ) : (
                        <span className="text-red-500 ml-2">
                          ↓ {(previousYearData.roiPercentage - currentYearData.roiPercentage).toFixed(1)}pp
                        </span>
                      )}
                    </p>
                  )}
                </CardContent>
              </Card>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              {monthlyChartData && (
                <Card className="backdrop-blur-md bg-background/60 border-border/50">
                  <CardHeader>
                    <CardTitle className="flex items-center gap-2">
                      <BarChart3 className="w-5 h-5 text-primary" />
                      {t("projects.roiAnalysis.monthlyTrend", "Tendencia Mensual - Inversión vs Retorno")}
                    </CardTitle>
                  </CardHeader>
                  <CardContent>
                    <div className="h-80">
                      <Line data={monthlyChartData} options={chartOptions} />
                    </div>
                  </CardContent>
                </Card>
              )}

              {cumulativeROIData && (
                <Card className="backdrop-blur-md bg-background/60 border-border/50">
                  <CardHeader>
                    <CardTitle className="flex items-center gap-2">
                      <TrendingUp className="w-5 h-5 text-primary" />
                      {t("projects.roiAnalysis.cumulativeROI", "ROI Acumulado")}
                    </CardTitle>
                  </CardHeader>
                  <CardContent>
                    <div className="h-80">
                      <Line data={cumulativeROIData} options={chartOptions} />
                    </div>
                  </CardContent>
                </Card>
              )}
            </div>

            {comparisonChartData && (
              <Card className="backdrop-blur-md bg-background/60 border-border/50">
                <CardHeader>
                  <CardTitle className="flex items-center gap-2">
                    <BarChart3 className="w-5 h-5 text-primary" />
                    {t("projects.roiAnalysis.yearComparison", `Comparativa ${previousYear} vs ${selectedYear}`)}
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <div className="h-80">
                    <Line data={comparisonChartData} options={chartOptions} />
                  </div>
                </CardContent>
              </Card>
            )}

            {/* Información adicional del proyecto */}
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Target className="w-5 h-5 text-primary" />
                  {t("projects.roiAnalysis.projectDetails", "Detalles del Proyecto")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div className="space-y-2 p-4 rounded-lg bg-muted/30">
                    <div className="flex items-center gap-2">
                      <Brain className="w-4 h-4 text-primary" />
                      <span className="text-sm font-medium">
                        {t("projects.roiAnalysis.modelsImpact", "Impacto de Modelos")}
                      </span>
                    </div>
                    <div className="text-2xl font-bold">{currentYearData.modelsImpact}%</div>
                    <div className="w-full bg-muted rounded-full h-2">
                      <div
                        className="bg-primary h-2 rounded-full transition-all duration-500"
                        style={{ width: `${currentYearData.modelsImpact}%` }}
                      />
                    </div>
                  </div>

                  <div className="space-y-2 p-4 rounded-lg bg-muted/30">
                    <div className="flex items-center gap-2">
                      <Bot className="w-4 h-4 text-primary" />
                      <span className="text-sm font-medium">
                        {t("projects.roiAnalysis.agentsImpact", "Impacto de Agentes")}
                      </span>
                    </div>
                    <div className="text-2xl font-bold">{currentYearData.agentsImpact}%</div>
                    <div className="w-full bg-muted rounded-full h-2">
                      <div
                        className="bg-green-500 h-2 rounded-full transition-all duration-500"
                        style={{ width: `${currentYearData.agentsImpact}%` }}
                      />
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>
          </>
        ) : (
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardContent className="pt-6">
              <p className="text-center text-muted-foreground">
                {t("projects.roiAnalysis.noData", "No hay datos disponibles para el proyecto seleccionado")}
              </p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}


