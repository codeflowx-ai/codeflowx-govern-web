"use client";

import { useState } from "react";
import { useTranslation } from "@/app/config/i18n";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { DollarSign, PieChart, BarChart3, TrendingDown, Calendar } from "lucide-react";
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

interface CostBreakdown {
  year: number;
  totalCost: number;
  byCategory: Array<{ category: string; amount: number; percentage: number }>;
  byProject: Array<{ projectName: string; amount: number; percentage: number }>;
  monthlyData: Array<{ month: string; amount: number; byCategory: Record<string, number> }>;
}

const mockCostDataByYear: Record<number, CostBreakdown> = {
  2024: {
    year: 2024,
    totalCost: 1850000,
    byCategory: [
      { category: "Compute (GPUs/Cloud)", amount: 750000, percentage: 40.5 },
      { category: "APIs de Modelos", amount: 450000, percentage: 24.3 },
      { category: "Storage", amount: 300000, percentage: 16.2 },
      { category: "Licencias de Modelos", amount: 200000, percentage: 10.8 },
      { category: "Infraestructura", amount: 150000, percentage: 8.1 },
    ],
    byProject: [
      { projectName: "Modelo de Detección de Fraude", amount: 325000, percentage: 17.6 },
      { projectName: "Agente Virtual de Atención", amount: 180000, percentage: 9.7 },
      { projectName: "Análisis Predictivo de Datos", amount: 285000, percentage: 15.4 },
      { projectName: "Cumplimiento Normativo EU AI Act", amount: 420000, percentage: 22.7 },
      { projectName: "Clasificación de Documentos", amount: 640000, percentage: 34.6 },
    ],
    monthlyData: [
      { month: "Ene", amount: 150000, byCategory: { compute: 60000, apis: 36000, storage: 24000, licenses: 16000, infra: 12000 } },
      { month: "Feb", amount: 180000, byCategory: { compute: 72000, apis: 44000, storage: 29000, licenses: 19000, infra: 15000 } },
      { month: "Mar", amount: 200000, byCategory: { compute: 81000, apis: 49000, storage: 32000, licenses: 22000, infra: 16000 } },
      { month: "Abr", amount: 190000, byCategory: { compute: 77000, apis: 46000, storage: 31000, licenses: 20000, infra: 15000 } },
      { month: "May", amount: 210000, byCategory: { compute: 85000, apis: 51000, storage: 34000, licenses: 23000, infra: 17000 } },
      { month: "Jun", amount: 220000, byCategory: { compute: 89000, apis: 53000, storage: 36000, licenses: 24000, infra: 18000 } },
      { month: "Jul", amount: 230000, byCategory: { compute: 93000, apis: 56000, storage: 37000, licenses: 25000, infra: 19000 } },
      { month: "Ago", amount: 240000, byCategory: { compute: 97000, apis: 58000, storage: 39000, licenses: 26000, infra: 20000 } },
      { month: "Sep", amount: 250000, byCategory: { compute: 101000, apis: 61000, storage: 41000, licenses: 27000, infra: 20000 } },
      { month: "Oct", amount: 260000, byCategory: { compute: 105000, apis: 63000, storage: 42000, licenses: 28000, infra: 22000 } },
      { month: "Nov", amount: 270000, byCategory: { compute: 109000, apis: 66000, storage: 44000, licenses: 29000, infra: 22000 } },
      { month: "Dic", amount: 280000, byCategory: { compute: 113000, apis: 68000, storage: 45000, licenses: 30000, infra: 24000 } },
    ],
  },
  2023: {
    year: 2023,
    totalCost: 1420000,
    byCategory: [
      { category: "Compute (GPUs/Cloud)", amount: 580000, percentage: 40.8 },
      { category: "APIs de Modelos", amount: 340000, percentage: 23.9 },
      { category: "Storage", amount: 230000, percentage: 16.2 },
      { category: "Licencias de Modelos", amount: 150000, percentage: 10.6 },
      { category: "Infraestructura", amount: 120000, percentage: 8.5 },
    ],
    byProject: [
      { projectName: "Sistema de Trazabilidad", amount: 420000, percentage: 29.6 },
      { projectName: "Análisis de Sentimientos", amount: 280000, percentage: 19.7 },
      { projectName: "Recomendación Personalizada", amount: 320000, percentage: 22.5 },
      { projectName: "Automatización RPA", amount: 400000, percentage: 28.2 },
    ],
    monthlyData: [
      { month: "Ene", amount: 110000, byCategory: { compute: 45000, apis: 26000, storage: 18000, licenses: 12000, infra: 9000 } },
      { month: "Feb", amount: 115000, byCategory: { compute: 47000, apis: 27000, storage: 19000, licenses: 12000, infra: 10000 } },
      { month: "Mar", amount: 120000, byCategory: { compute: 49000, apis: 29000, storage: 19000, licenses: 13000, infra: 10000 } },
      { month: "Abr", amount: 118000, byCategory: { compute: 48000, apis: 28000, storage: 19000, licenses: 12000, infra: 10000 } },
      { month: "May", amount: 125000, byCategory: { compute: 51000, apis: 30000, storage: 20000, licenses: 13000, infra: 11000 } },
      { month: "Jun", amount: 130000, byCategory: { compute: 53000, apis: 31000, storage: 21000, licenses: 14000, infra: 11000 } },
      { month: "Jul", amount: 135000, byCategory: { compute: 55000, apis: 32000, storage: 22000, licenses: 14000, infra: 12000 } },
      { month: "Ago", amount: 140000, byCategory: { compute: 57000, apis: 33000, storage: 23000, licenses: 15000, infra: 12000 } },
      { month: "Sep", amount: 145000, byCategory: { compute: 59000, apis: 35000, storage: 24000, licenses: 15000, infra: 12000 } },
      { month: "Oct", amount: 150000, byCategory: { compute: 61000, apis: 36000, storage: 24000, licenses: 16000, infra: 13000 } },
      { month: "Nov", amount: 155000, byCategory: { compute: 63000, apis: 37000, storage: 25000, licenses: 16000, infra: 13000 } },
      { month: "Dic", amount: 158000, byCategory: { compute: 64000, apis: 38000, storage: 26000, licenses: 17000, infra: 13000 } },
    ],
  },
};

export default function ProjectCostBreakdownPage() {
  const { t } = useTranslation();
  const [selectedYear, setSelectedYear] = useState(2024);
  const [compareYears, setCompareYears] = useState(false);
  const currentData = mockCostDataByYear[selectedYear] || mockCostDataByYear[2024];
  const availableYears = Object.keys(mockCostDataByYear).map(Number).sort((a, b) => b - a);
  const previousYear = selectedYear - 1;
  const previousYearData = mockCostDataByYear[previousYear];

  const monthlyChartData = {
    labels: currentData.monthlyData.map((d) => d.month),
    datasets: [
      {
        label: t("projects.costBreakdown.totalCost", "Costo Total"),
        data: currentData.monthlyData.map((d) => d.amount / 1000),
        borderColor: "rgb(99, 102, 241)",
        backgroundColor: "rgba(99, 102, 241, 0.1)",
        fill: true,
        tension: 0.4,
      },
      {
        label: t("projects.costBreakdown.compute", "Compute"),
        data: currentData.monthlyData.map((d) => d.byCategory.compute / 1000),
        borderColor: "rgb(34, 197, 94)",
        backgroundColor: "rgba(34, 197, 94, 0.1)",
        fill: false,
        tension: 0.4,
      },
      {
        label: t("projects.costBreakdown.apis", "APIs"),
        data: currentData.monthlyData.map((d) => d.byCategory.apis / 1000),
        borderColor: "rgb(251, 146, 60)",
        backgroundColor: "rgba(251, 146, 60, 0.1)",
        fill: false,
        tension: 0.4,
      },
    ],
  };

  const comparisonChartData = compareYears && previousYearData ? {
    labels: ["Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"],
    datasets: [
      {
        label: `${selectedYear}`,
        data: currentData.monthlyData.map((d) => d.amount / 1000),
        borderColor: "rgb(99, 102, 241)",
        backgroundColor: "rgba(99, 102, 241, 0.1)",
        fill: true,
        tension: 0.4,
      },
      {
        label: `${previousYear}`,
        data: previousYearData.monthlyData.map((d) => d.amount / 1000),
        borderColor: "rgb(168, 85, 247)",
        backgroundColor: "rgba(168, 85, 247, 0.1)",
        fill: true,
        tension: 0.4,
      },
    ],
  } : null;

  const categoryChartData = {
    labels: currentData.byCategory.map((c) => c.category),
    datasets: [
      {
        data: currentData.byCategory.map((c) => c.amount / 1000),
        backgroundColor: [
          "rgba(99, 102, 241, 0.8)",
          "rgba(34, 197, 94, 0.8)",
          "rgba(251, 146, 60, 0.8)",
          "rgba(236, 72, 153, 0.8)",
          "rgba(168, 85, 247, 0.8)",
        ],
        borderColor: [
          "rgb(99, 102, 241)",
          "rgb(34, 197, 94)",
          "rgb(251, 146, 60)",
          "rgb(236, 72, 153)",
          "rgb(168, 85, 247)",
        ],
        borderWidth: 2,
      },
    ],
  };

  const projectChartData = {
    labels: currentData.byProject.map((p) => p.projectName),
    datasets: [
      {
        label: t("projects.costBreakdown.cost", "Costo (K)"),
        data: currentData.byProject.map((p) => p.amount / 1000),
        backgroundColor: [
          "rgba(99, 102, 241, 0.8)",
          "rgba(34, 197, 94, 0.8)",
          "rgba(251, 146, 60, 0.8)",
          "rgba(236, 72, 153, 0.8)",
          "rgba(168, 85, 247, 0.8)",
        ],
        borderColor: [
          "rgb(99, 102, 241)",
          "rgb(34, 197, 94)",
          "rgb(251, 146, 60)",
          "rgb(236, 72, 153)",
          "rgb(168, 85, 247)",
        ],
        borderWidth: 2,
      },
    ],
  };

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
        callbacks: {
          label: function(context: any) {
            return `${context.dataset.label}: $${context.parsed.y.toLocaleString()}K`;
          },
        },
      },
    },
    scales: {
      y: {
        beginAtZero: true,
        ticks: {
          color: "rgb(148, 163, 184)",
          callback: function(value: any) {
            return `$${value}K`;
          },
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
        callbacks: {
          label: function(context: any) {
            const label = context.label || "";
            const value = context.parsed || 0;
            const total = context.dataset.data.reduce((a: number, b: number) => a + b, 0);
            const percentage = ((value / total) * 100).toFixed(1);
            return `${label}: $${value.toLocaleString()}K (${percentage}%)`;
          },
        },
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
            <DollarSign className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("projects.costBreakdown.title", "Desglose de Costos de IA")}
            </h1>
          </div>
          <div className="flex items-center gap-3">
            <label className="flex items-center gap-2 text-sm">
              <input
                type="checkbox"
                checked={compareYears}
                onChange={(e) => setCompareYears(e.target.checked)}
                className="w-4 h-4"
              />
              {t("projects.costBreakdown.compareYears", "Comparar Años")}
            </label>
            <div className="flex items-center gap-2">
              <Calendar className="w-4 h-4 text-muted-foreground" />
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
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.costBreakdown.totalCost", "Costo Total")}
              </CardTitle>
              <DollarSign className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">${(currentData.totalCost / 1000000).toFixed(2)}M</div>
              {compareYears && previousYearData && (
                <p className="text-xs text-muted-foreground mt-1">
                  {previousYear}: ${(previousYearData.totalCost / 1000000).toFixed(2)}M
                  {currentData.totalCost > previousYearData.totalCost ? (
                    <span className="text-red-500 ml-2">
                      ↑ {(((currentData.totalCost - previousYearData.totalCost) / previousYearData.totalCost) * 100).toFixed(1)}%
                    </span>
                  ) : (
                    <span className="text-green-500 ml-2">
                      ↓ {(((previousYearData.totalCost - currentData.totalCost) / previousYearData.totalCost) * 100).toFixed(1)}%
                    </span>
                  )}
                </p>
              )}
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.costBreakdown.averageMonthly", "Promedio Mensual")}
              </CardTitle>
              <BarChart3 className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">
                ${(currentData.totalCost / 12 / 1000).toFixed(0)}K
              </div>
              <p className="text-xs text-muted-foreground mt-1">
                {t("projects.costBreakdown.perMonth", "por mes")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.costBreakdown.categories", "Categorías")}
              </CardTitle>
              <PieChart className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{currentData.byCategory.length}</div>
              <p className="text-xs text-muted-foreground mt-1">
                {t("projects.costBreakdown.activeCategories", "categorías activas")}
              </p>
            </CardContent>
          </Card>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <BarChart3 className="w-5 h-5 text-primary" />
                {t("projects.costBreakdown.monthlyTrend", "Tendencia Mensual")}
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="h-80">
                <Line data={monthlyChartData} options={chartOptions} />
              </div>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <PieChart className="w-5 h-5 text-primary" />
                {t("projects.costBreakdown.byCategory", "Desglose por Categoría")}
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="h-80">
                <Doughnut data={categoryChartData} options={doughnutOptions} />
              </div>
            </CardContent>
          </Card>
        </div>

        {compareYears && comparisonChartData && (
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <TrendingDown className="w-5 h-5 text-primary" />
                {t("projects.costBreakdown.yearComparison", `Comparativa ${previousYear} vs ${selectedYear}`)}
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="h-80">
                <Line data={comparisonChartData} options={chartOptions} />
              </div>
            </CardContent>
          </Card>
        )}

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <BarChart3 className="w-5 h-5 text-primary" />
                {t("projects.costBreakdown.byProject", "Desglose por Proyecto")}
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="h-80">
                <Bar data={projectChartData} options={chartOptions} />
              </div>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <PieChart className="w-5 h-5 text-primary" />
                {t("projects.costBreakdown.detailedByCategory", "Detalle por Categoría")}
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              {currentData.byCategory.map((category) => (
                <div key={category.category} className="space-y-2">
                  <div className="flex items-center justify-between text-sm">
                    <span className="text-foreground font-medium">{category.category}</span>
                    <span className="font-bold">
                      ${(category.amount / 1000).toLocaleString()}K ({category.percentage.toFixed(1)}%)
                    </span>
                  </div>
                  <div className="w-full bg-muted rounded-full h-2">
                    <div
                      className="bg-primary h-2 rounded-full transition-all duration-500"
                      style={{ width: `${category.percentage}%` }}
                    />
                  </div>
                </div>
              ))}
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}
