"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  BarChart3,
  RefreshCw,
  TrendingUp,
  Calendar,
  DollarSign,
  Activity,
  Cpu,
  ArrowLeft,
} from "lucide-react";
import { telemetryService } from "../services/telemetryService";
import type { TelemetryKpisResponseDto } from "../types/telemetry";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  PointElement,
  LineElement,
  Filler,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import { Bar, Line } from "react-chartjs-2";

// Registrar componentes de Chart.js
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  PointElement,
  LineElement,
  Filler,
  Title,
  Tooltip,
  Legend
);

interface MonthlyMetrics {
  month: string;
  totalEvents: number;
  totalCostUsd: number;
  totalTokens: number;
  avgLatencyMs: number;
}

export default function TelemetryAnalyticsPage() {
  const { t } = useTranslation();
  // Inicializar con 2024 si el año actual no es 2024 ni 2025
  const currentYear = new Date().getFullYear();
  const initialYear = (currentYear === 2024 || currentYear === 2025) ? currentYear : 2024;
  const [year, setYear] = useState<number>(initialYear);
  const [monthlyMetrics, setMonthlyMetrics] = useState<MonthlyMetrics[]>([]);
  const [loading, setLoading] = useState(false);
  const [mounted, setMounted] = useState(false);

  useEffect(() => {
    setMounted(true);
  }, []);

  // Cargar datos cuando cambia el año o cuando se monta el componente
  useEffect(() => {
    loadMonthlyMetrics();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [year]);

  const loadMonthlyMetrics = async () => {
    try {
      setLoading(true);
      const metrics: MonthlyMetrics[] = [];

      // Solo cargar datos para 2024 y 2025
      if (year !== 2024 && year !== 2025) {
        setMonthlyMetrics([]);
        return;
      }

      // Cargar métricas para cada mes del año
      for (let month = 0; month < 12; month++) {
        const startDate = new Date(year, month, 1);
        const endDate = new Date(year, month + 1, 0, 23, 59, 59);

        const response = await telemetryService.getKpis(
          startDate.toISOString(),
          endDate.toISOString()
        );

        if (response.success && response.data && response.data.totalEvents > 0) {
          metrics.push({
            month: startDate.toLocaleDateString('es-ES', { month: 'short', year: 'numeric' }),
            totalEvents: response.data.totalEvents,
            totalCostUsd: response.data.totalCostUsd,
            totalTokens: response.data.totalTokens,
            avgLatencyMs: response.data.avgLatencyMs,
          });
        } else {
          // Si no hay datos, añadir mes con valores cero
          metrics.push({
            month: startDate.toLocaleDateString('es-ES', { month: 'short', year: 'numeric' }),
            totalEvents: 0,
            totalCostUsd: 0,
            totalTokens: 0,
            avgLatencyMs: 0,
          });
        }
      }

      setMonthlyMetrics(metrics);
    } catch (error) {
      console.error("Error loading monthly metrics:", error);
      setMonthlyMetrics([]);
    } finally {
      setLoading(false);
    }
  };

  const formatNumber = (value: number): string => {
    if (value >= 1000000) {
      return (value / 1000000).toFixed(1) + "M";
    }
    if (value >= 1000) {
      return (value / 1000).toFixed(1) + "K";
    }
    return value.toFixed(0);
  };

  const formatCurrency = (value: number): string => {
    return new Intl.NumberFormat("en-US", {
      style: "currency",
      currency: "USD",
    }).format(value);
  };

  if (!mounted) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <RefreshCw className="w-8 h-8 animate-spin mx-auto mb-2 text-muted-foreground" />
          <p className="text-muted-foreground">Cargando...</p>
        </div>
      </div>
    );
  }

  // Configuración de colores para modo oscuro/claro
  const isDark = typeof window !== 'undefined' && typeof document !== 'undefined' && document.documentElement.classList.contains('dark');
  const textColor = isDark ? '#e5e7eb' : '#000000';
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
          color: '#000000',
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
          color: '#000000',
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

  // Calcular totales del año
  const yearTotal = monthlyMetrics.reduce(
    (acc, month) => ({
      totalEvents: acc.totalEvents + month.totalEvents,
      totalCostUsd: acc.totalCostUsd + month.totalCostUsd,
      totalTokens: acc.totalTokens + month.totalTokens,
      avgLatencyMs: acc.avgLatencyMs + month.avgLatencyMs,
    }),
    { totalEvents: 0, totalCostUsd: 0, totalTokens: 0, avgLatencyMs: 0 }
  );

  const avgLatencyYear = monthlyMetrics.length > 0
    ? yearTotal.avgLatencyMs / monthlyMetrics.length
    : 0;

  return (
    <div className="space-y-4">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <BarChart3 className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("governance.telemetryAnalytics.title", "Análisis de Telemetría")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("governance.telemetryAnalytics.subtitle", "Métricas mensuales y análisis por año")}
          </p>
        </div>
        <div className="flex items-center gap-2">
          <Label htmlFor="year" className="text-sm">
            {t("governance.telemetryAnalytics.year", "Año")}:
          </Label>
          <select
            id="year"
            value={year.toString()}
            onChange={(e) => {
              const newYear = parseInt(e.target.value);
              if (!isNaN(newYear) && newYear !== year) {
                setYear(newYear);
              }
            }}
            className="w-24 px-3 py-2 border rounded-md bg-background text-sm"
          >
            <option value="2024">2024</option>
            <option value="2025">2025</option>
          </select>
          <Button onClick={loadMonthlyMetrics} disabled={loading} variant="outline" size="sm" className="flex items-center gap-2">
            <RefreshCw className={`h-4 w-4 ${loading ? 'animate-spin' : ''}`} />
            {t("governance.telemetryAnalytics.refresh", "Actualizar")}
          </Button>
          <Button
            onClick={() => {
              if (typeof window !== 'undefined') {
                window.location.href = '/governance/telemetry/dashboard';
              }
            }}
            variant="outline"
            size="sm"
            className="flex items-center gap-2"
          >
            <ArrowLeft className="h-4 w-4" />
            {t("governance.telemetryAnalytics.back", "Volver")}
          </Button>
        </div>
      </div>

      {/* KPIs del Año */}
      <div className="grid grid-cols-4 gap-4">
        <Card className="border-primary hover:shadow-lg hover:border-primary/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("governance.telemetryAnalytics.yearTotalEvents", "Total Eventos del Año")}
                </p>
                <h2 className="text-2xl font-bold text-primary mb-1">
                  {formatNumber(yearTotal.totalEvents)}
                </h2>
              </div>
              <Activity className="w-8 h-8 text-primary flex-shrink-0" />
            </div>
          </CardBody>
        </Card>

        <Card className="border-green-500 hover:shadow-lg hover:border-green-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("governance.telemetryAnalytics.yearTotalCost", "Costo Total del Año")}
                </p>
                <h2 className="text-2xl font-bold text-green-600 mb-1">
                  {formatCurrency(yearTotal.totalCostUsd)}
                </h2>
              </div>
              <DollarSign className="w-8 h-8 text-green-600 flex-shrink-0" />
            </div>
          </CardBody>
        </Card>

        <Card className="border-blue-500 hover:shadow-lg hover:border-blue-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("governance.telemetryAnalytics.yearTotalTokens", "Total Tokens del Año")}
                </p>
                <h2 className="text-2xl font-bold text-blue-600 mb-1">
                  {formatNumber(yearTotal.totalTokens)}
                </h2>
              </div>
              <Cpu className="w-8 h-8 text-blue-600 flex-shrink-0" />
            </div>
          </CardBody>
        </Card>

        <Card className="border-yellow-500 hover:shadow-lg hover:border-yellow-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("governance.telemetryAnalytics.yearAvgLatency", "Latencia Promedio del Año")}
                </p>
                <h2 className="text-2xl font-bold text-yellow-600 mb-1">
                  {avgLatencyYear.toFixed(1)} ms
                </h2>
              </div>
              <TrendingUp className="w-8 h-8 text-yellow-600 flex-shrink-0" />
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Gráficos Mensuales */}
      {loading ? (
        <Card>
          <CardBody className="text-center py-12">
            <RefreshCw className="w-8 h-8 animate-spin mx-auto mb-2 text-muted-foreground" />
            <p className="text-muted-foreground">Cargando métricas mensuales...</p>
          </CardBody>
        </Card>
      ) : monthlyMetrics.length > 0 ? (
        <div className="grid grid-cols-2 gap-4">
          {/* Gráfico de Eventos Mensuales */}
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader className="pb-2">
              <CardTitle className="flex items-center gap-2 text-base font-medium">
                <BarChart3 className="w-4 h-4" />
                {t("governance.telemetryAnalytics.monthlyEvents", "Eventos por Mes")}
              </CardTitle>
            </CardHeader>
            <CardBody className="pt-0">
              <div className="h-[300px] w-full">
                <Bar
                  data={{
                    labels: monthlyMetrics.map((m) => m.month),
                    datasets: [
                      {
                        label: t("governance.telemetryAnalytics.events", "Eventos"),
                        data: monthlyMetrics.map((m) => m.totalEvents),
                        backgroundColor: "rgba(59, 130, 246, 0.8)",
                        borderColor: "rgb(59, 130, 246)",
                        borderWidth: 2,
                      },
                    ],
                  }}
                  options={chartOptions}
                />
              </div>
            </CardBody>
          </Card>

          {/* Gráfico de Costos Mensuales */}
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader className="pb-2">
              <CardTitle className="flex items-center gap-2 text-base font-medium">
                <DollarSign className="w-4 h-4" />
                {t("governance.telemetryAnalytics.monthlyCosts", "Costos por Mes")}
              </CardTitle>
            </CardHeader>
            <CardBody className="pt-0">
              <div className="h-[300px] w-full">
                <Bar
                  data={{
                    labels: monthlyMetrics.map((m) => m.month),
                    datasets: [
                      {
                        label: t("governance.telemetryAnalytics.cost", "Costo (USD)"),
                        data: monthlyMetrics.map((m) => m.totalCostUsd),
                        backgroundColor: "rgba(245, 158, 11, 0.8)",
                        borderColor: "rgb(245, 158, 11)",
                        borderWidth: 2,
                      },
                    ],
                  }}
                  options={chartOptions}
                />
              </div>
            </CardBody>
          </Card>

          {/* Gráfico de Tokens Mensuales */}
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader className="pb-2">
              <CardTitle className="flex items-center gap-2 text-base font-medium">
                <Cpu className="w-4 h-4" />
                {t("governance.telemetryAnalytics.monthlyTokens", "Tokens por Mes")}
              </CardTitle>
            </CardHeader>
            <CardBody className="pt-0">
              <div className="h-[300px] w-full">
                <Bar
                  data={{
                    labels: monthlyMetrics.map((m) => m.month),
                    datasets: [
                      {
                        label: t("governance.telemetryAnalytics.tokens", "Tokens"),
                        data: monthlyMetrics.map((m) => m.totalTokens),
                        backgroundColor: "rgba(16, 185, 129, 0.8)",
                        borderColor: "rgb(16, 185, 129)",
                        borderWidth: 2,
                      },
                    ],
                  }}
                  options={chartOptions}
                />
              </div>
            </CardBody>
          </Card>

          {/* Gráfico de Latencia Mensual */}
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader className="pb-2">
              <CardTitle className="flex items-center gap-2 text-base font-medium">
                <TrendingUp className="w-4 h-4" />
                {t("governance.telemetryAnalytics.monthlyLatency", "Latencia Promedio por Mes")}
              </CardTitle>
            </CardHeader>
            <CardBody className="pt-0">
              <div className="h-[300px]">
                <Line
                  data={{
                    labels: monthlyMetrics.map((m) => m.month),
                    datasets: [
                      {
                        label: t("governance.telemetryAnalytics.avgLatency", "Latencia Promedio (ms)"),
                        data: monthlyMetrics.map((m) => m.avgLatencyMs),
                        borderColor: "rgb(139, 92, 246)",
                        backgroundColor: "rgba(139, 92, 246, 0.1)",
                        fill: true,
                        tension: 0.4,
                      },
                    ],
                  }}
                  options={chartOptions}
                />
              </div>
            </CardBody>
          </Card>
        </div>
      ) : (
        <Card>
          <CardBody className="text-center py-12">
            <BarChart3 className="w-12 h-12 mx-auto mb-4 text-muted-foreground" />
            <p className="text-muted-foreground">
              {t("governance.telemetryAnalytics.noData", "No hay datos disponibles para el año seleccionado.")}
            </p>
          </CardBody>
        </Card>
      )}

      {/* Tabla de Métricas Mensuales */}
      {monthlyMetrics.length > 0 && (
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Calendar className="w-5 h-5" />
              {t("governance.telemetryAnalytics.monthlyTable", "Resumen Mensual")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b">
                    <th className="text-left p-2">
                      {t("governance.telemetryAnalytics.month", "Mes")}
                    </th>
                    <th className="text-right p-2">
                      {t("governance.telemetryAnalytics.totalEvents", "Total Eventos")}
                    </th>
                    <th className="text-right p-2">
                      {t("governance.telemetryAnalytics.totalCost", "Costo Total")}
                    </th>
                    <th className="text-right p-2">
                      {t("governance.telemetryAnalytics.totalTokens", "Total Tokens")}
                    </th>
                    <th className="text-right p-2">
                      {t("governance.telemetryAnalytics.avgLatency", "Latencia Promedio")}
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {monthlyMetrics.map((metric, index) => (
                    <tr key={index} className="border-b hover:bg-muted/50">
                      <td className="p-2 font-medium">{metric.month}</td>
                      <td className="p-2 text-right font-medium">
                        {formatNumber(metric.totalEvents)}
                      </td>
                      <td className="p-2 text-right">
                        {formatCurrency(metric.totalCostUsd)}
                      </td>
                      <td className="p-2 text-right">
                        {formatNumber(metric.totalTokens)}
                      </td>
                      <td className="p-2 text-right">
                        {metric.avgLatencyMs.toFixed(1)} ms
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </CardBody>
        </Card>
      )}
    </div>
  );
}
