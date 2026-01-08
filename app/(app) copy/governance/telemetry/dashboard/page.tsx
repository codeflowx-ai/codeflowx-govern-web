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
  DollarSign,
  Activity,
  Cpu,
  Clock,
  Database,
  Search,
} from "lucide-react";
import { telemetryService } from "../services/telemetryService";
import type {
  TelemetryKpisResponseDto,
  ComponentStatisticsDto,
} from "../types/telemetry";
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

export default function TelemetryDashboardPage() {
  const { t } = useTranslation();
  const [kpis, setKpis] = useState<TelemetryKpisResponseDto | null>(null);
  const [componentStats, setComponentStats] = useState<ComponentStatisticsDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [mounted, setMounted] = useState(false);
  const [startDate, setStartDate] = useState<string>("");
  const [endDate, setEndDate] = useState<string>("");

  useEffect(() => {
    setMounted(true);
    // Establecer fechas por defecto (últimos 30 días)
    const end = new Date();
    const start = new Date();
    start.setDate(start.getDate() - 30);
    setEndDate(end.toISOString().split('T')[0]);
    setStartDate(start.toISOString().split('T')[0]);
    loadDashboard();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    if (mounted && (startDate || endDate)) {
      loadDashboard();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [startDate, endDate]);

  const loadDashboard = async () => {
    try {
      setLoading(true);

      // Preparar fechas para la API
      const startTime = startDate ? new Date(startDate).toISOString() : undefined;
      const endTime = endDate ? new Date(endDate + 'T23:59:59').toISOString() : undefined;

      // Cargar KPIs
      const kpisResponse = await telemetryService.getKpis(startTime, endTime);
      if (kpisResponse.success && kpisResponse.data) {
        setKpis(kpisResponse.data);
      }

      // Cargar estadísticas de componentes
      const statsResponse = await telemetryService.getComponentStatistics(undefined, startTime, endTime);
      if (statsResponse.success && statsResponse.data) {
        setComponentStats(statsResponse.data.statistics);
      }
    } catch (error) {
      console.error("Error loading telemetry dashboard:", error);
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

  if (!mounted || loading || !kpis) {
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
  const isDark = typeof window !== 'undefined' && document.documentElement.classList.contains('dark');
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

  return (
    <div className="w-full px-4 py-6 space-y-4">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <BarChart3 className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("governance.telemetryDashboard.title", "Dashboard de Telemetría")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("governance.telemetryDashboard.subtitle", "KPIs y métricas de telemetría de AI OS")}
          </p>
        </div>
        <div className="flex items-center gap-2">
          <Button
            onClick={() => {
              if (typeof window !== 'undefined') {
                window.location.href = '/governance/telemetry/search';
              }
            }}
            variant="outline"
            size="sm"
            className="flex items-center gap-2"
          >
            <Search className="h-4 w-4" />
            {t("governance.telemetryDashboard.search", "Búsqueda")}
          </Button>
          <Button
            onClick={() => {
              if (typeof window !== 'undefined') {
                window.location.href = '/governance/telemetry/analytics';
              }
            }}
            variant="outline"
            size="sm"
            className="flex items-center gap-2"
          >
            <TrendingUp className="h-4 w-4" />
            {t("governance.telemetryDashboard.analytics", "Análisis")}
          </Button>
          <Button onClick={loadDashboard} variant="outline" size="sm" className="flex items-center gap-2">
            <RefreshCw className="h-4 w-4" />
            {t("governance.telemetryDashboard.refresh", "Actualizar")}
          </Button>
        </div>
      </div>

      {/* Filtros de Fecha */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50">
        <CardHeader>
          <CardTitle className="text-base font-medium">
            {t("governance.telemetryDashboard.filters", "Filtros de Fecha")}
          </CardTitle>
        </CardHeader>
        <CardBody>
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <div>
              <Label htmlFor="startDate">
                {t("governance.telemetryDashboard.startDate", "Fecha Inicio")}
              </Label>
              <Input
                id="startDate"
                type="date"
                value={startDate}
                onChange={(e) => setStartDate(e.target.value)}
                className="mt-1"
              />
            </div>
            <div>
              <Label htmlFor="endDate">
                {t("governance.telemetryDashboard.endDate", "Fecha Fin")}
              </Label>
              <Input
                id="endDate"
                type="date"
                value={endDate}
                onChange={(e) => setEndDate(e.target.value)}
                className="mt-1"
              />
            </div>
            <div className="flex items-end">
              <Button
                variant="outline"
                onClick={() => {
                  const end = new Date();
                  const start = new Date();
                  start.setDate(start.getDate() - 30);
                  setEndDate(end.toISOString().split('T')[0]);
                  setStartDate(start.toISOString().split('T')[0]);
                }}
                className="w-full"
              >
                {t("governance.telemetryDashboard.last30Days", "Últimos 30 días")}
              </Button>
            </div>
            <div className="flex items-end">
              <Button
                variant="outline"
                onClick={() => {
                  const end = new Date();
                  const start = new Date();
                  start.setMonth(start.getMonth() - 12);
                  setEndDate(end.toISOString().split('T')[0]);
                  setStartDate(start.toISOString().split('T')[0]);
                }}
                className="w-full"
              >
                {t("governance.telemetryDashboard.lastYear", "Último año")}
              </Button>
            </div>
          </div>
        </CardBody>
      </Card>

      {/* KPIs Principales */}
      <div className="grid grid-cols-4 gap-4">
        {/* Total de Eventos */}
        <Card className="border-primary hover:shadow-lg hover:border-primary/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("governance.telemetryDashboard.totalEvents", "Total Eventos")}
                </p>
                <h2 className="text-2xl font-bold text-primary mb-1">
                  {formatNumber(kpis.totalEvents)}
                </h2>
                <p className="text-xs text-muted-foreground">
                  {t("governance.telemetryDashboard.period", "Últimos 30 días")}
                </p>
              </div>
              <Database className="w-8 h-8 text-primary flex-shrink-0" />
            </div>
          </CardBody>
        </Card>

        {/* Costo Total */}
        <Card className="border-green-500 hover:shadow-lg hover:border-green-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("governance.telemetryDashboard.totalCost", "Costo Total")}
                </p>
                <h2 className="text-2xl font-bold text-green-600 mb-1">
                  {formatCurrency(kpis.totalCostUsd)}
                </h2>
                <p className="text-xs text-muted-foreground">
                  {t("governance.telemetryDashboard.usd", "USD")}
                </p>
              </div>
              <DollarSign className="w-8 h-8 text-green-600 flex-shrink-0" />
            </div>
          </CardBody>
        </Card>

        {/* Total de Tokens */}
        <Card className="border-blue-500 hover:shadow-lg hover:border-blue-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("governance.telemetryDashboard.totalTokens", "Total Tokens")}
                </p>
                <h2 className="text-2xl font-bold text-blue-600 mb-1">
                  {formatNumber(kpis.totalTokens)}
                </h2>
                <p className="text-xs text-muted-foreground">
                  {t("governance.telemetryDashboard.tokens", "Tokens procesados")}
                </p>
              </div>
              <Cpu className="w-8 h-8 text-blue-600 flex-shrink-0" />
            </div>
          </CardBody>
        </Card>

        {/* Latencia Promedio */}
        <Card className="border-yellow-500 hover:shadow-lg hover:border-yellow-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("governance.telemetryDashboard.avgLatency", "Latencia Promedio")}
                </p>
                <h2 className="text-2xl font-bold text-yellow-600 mb-1">
                  {kpis.avgLatencyMs.toFixed(1)} ms
                </h2>
                <p className="text-xs text-muted-foreground">
                  {t("governance.telemetryDashboard.responseTime", "Tiempo de respuesta")}
                </p>
              </div>
              <Clock className="w-8 h-8 text-yellow-600 flex-shrink-0" />
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Métricas Secundarias */}
      <div className="grid grid-cols-2 gap-4">
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader className="pb-3">
            <CardTitle className="text-base font-medium flex items-center gap-2">
              <Activity className="w-4 h-4" />
              {t("governance.telemetryDashboard.componentCount", "Componentes Activos")}
            </CardTitle>
          </CardHeader>
          <CardBody className="pt-0">
            <div className="flex items-center gap-2">
              <Database className="w-6 h-6 text-primary flex-shrink-0" />
              <div className="flex-grow">
                <p className="text-3xl font-bold mb-0">{kpis.componentCount}</p>
                <p className="text-xs text-muted-foreground">
                  {t("governance.telemetryDashboard.uniqueComponents", "Componentes únicos")}
                </p>
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader className="pb-3">
            <CardTitle className="text-base font-medium flex items-center gap-2">
              <TrendingUp className="w-4 h-4" />
              {t("governance.telemetryDashboard.periodInfo", "Período de Análisis")}
            </CardTitle>
          </CardHeader>
          <CardBody className="pt-0">
            <div className="space-y-2">
              <div className="flex items-center justify-between">
                <span className="text-xs text-muted-foreground">
                  {t("governance.telemetryDashboard.startTime", "Inicio")}
                </span>
                <span className="text-sm font-medium">
                  {new Date(kpis.startTime).toLocaleDateString()}
                </span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-xs text-muted-foreground">
                  {t("governance.telemetryDashboard.endTime", "Fin")}
                </span>
                <span className="text-sm font-medium">
                  {new Date(kpis.endTime).toLocaleDateString()}
                </span>
              </div>
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Gráficos de Componentes */}
      <div className="grid grid-cols-3 gap-4">
        {/* Gráfico de Eventos por Componente */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader className="pb-2">
            <CardTitle className="flex items-center gap-2 text-base font-medium">
              <BarChart3 className="w-4 h-4" />
              {t("governance.telemetryDashboard.eventsByComponent", "Eventos por Componente")}
            </CardTitle>
          </CardHeader>
          <CardBody className="pt-0">
            {mounted && componentStats.length > 0 ? (
              <div className="h-[220px] w-full">
                <Bar
                  data={{
                    labels: componentStats.slice(0, 10).map((stat) => stat.componentUuid.substring(0, 8) + "..."),
                    datasets: [
                      {
                        label: t("governance.telemetryDashboard.events", "Eventos"),
                        data: componentStats.slice(0, 10).map((stat) => stat.totalEvents),
                        backgroundColor: "rgba(59, 130, 246, 0.8)",
                        borderColor: "rgb(59, 130, 246)",
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

        {/* Gráfico de Latencia por Componente */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader className="pb-2">
            <CardTitle className="flex items-center gap-2 text-base font-medium">
              <TrendingUp className="w-4 h-4" />
              {t("governance.telemetryDashboard.latencyByComponent", "Latencia por Componente")}
            </CardTitle>
          </CardHeader>
          <CardBody className="pt-0">
            {mounted && componentStats.length > 0 ? (
              <div className="h-[220px]">
                <Line
                  data={{
                    labels: componentStats.slice(0, 10).map((stat) => stat.componentUuid.substring(0, 8) + "..."),
                    datasets: [
                      {
                        label: t("governance.telemetryDashboard.avgLatency", "Latencia Promedio (ms)"),
                        data: componentStats.slice(0, 10).map((stat) => stat.avgLatencyMs),
                        borderColor: "rgb(16, 185, 129)",
                        backgroundColor: "rgba(16, 185, 129, 0.1)",
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

        {/* Gráfico de Costos por Componente */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader className="pb-2">
            <CardTitle className="flex items-center gap-2 text-base font-medium">
              <DollarSign className="w-4 h-4" />
              {t("governance.telemetryDashboard.costByComponent", "Costos por Componente")}
            </CardTitle>
          </CardHeader>
          <CardBody className="pt-0">
            {mounted && componentStats.length > 0 ? (
              <div className="h-[220px] w-full">
                <Bar
                  data={{
                    labels: componentStats.slice(0, 10).map((stat) => stat.componentUuid.substring(0, 8) + "..."),
                    datasets: [
                      {
                        label: t("governance.telemetryDashboard.totalCost", "Costo Total (USD)"),
                        data: componentStats.slice(0, 10).map((stat) => stat.totalCostUsd),
                        backgroundColor: "rgba(245, 158, 11, 0.8)",
                        borderColor: "rgb(245, 158, 11)",
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
      </div>

      {/* Tabla de Estadísticas por Componente */}
      {componentStats.length > 0 && (
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Database className="w-5 h-5" />
              {t("governance.telemetryDashboard.componentStatistics", "Estadísticas por Componente")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b">
                    <th className="text-left p-2">
                      {t("governance.telemetryDashboard.componentUuid", "Componente")}
                    </th>
                    <th className="text-right p-2">
                      {t("governance.telemetryDashboard.totalEvents", "Total Eventos")}
                    </th>
                    <th className="text-right p-2">
                      {t("governance.telemetryDashboard.avgLatency", "Latencia Promedio")}
                    </th>
                    <th className="text-right p-2">
                      {t("governance.telemetryDashboard.totalTokens", "Total Tokens")}
                    </th>
                    <th className="text-right p-2">
                      {t("governance.telemetryDashboard.totalCost", "Costo Total")}
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {componentStats.slice(0, 10).map((stat) => (
                    <tr key={stat.componentUuid} className="border-b hover:bg-muted/50">
                      <td className="p-2 font-mono text-xs">
                        {stat.componentUuid.substring(0, 36)}...
                      </td>
                      <td className="p-2 text-right font-medium">
                        {formatNumber(stat.totalEvents)}
                      </td>
                      <td className="p-2 text-right">
                        {stat.avgLatencyMs.toFixed(1)} ms
                      </td>
                      <td className="p-2 text-right">
                        {formatNumber(stat.totalTokens)}
                      </td>
                      <td className="p-2 text-right font-medium">
                        {formatCurrency(stat.totalCostUsd)}
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


