"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { motion } from "framer-motion";
import {
  Activity,
  AlertTriangle,
  BarChart3,
  CheckCircle,
  Clock,
  Server,
  TrendingDown,
  TrendingUp,
} from "lucide-react";
import Link from "next/link";
import { useEffect, useState } from "react";

// Mock data
const mockKPIs = {
  totalAlerts: 24,
  criticalAlerts: 3,
  activeMetrics: 156,
  systemHealth: "Healthy" as const,
};

const mockRecentAlerts = [
  {
    id: 1,
    name: "High CPU Usage Alert",
    type: "PERFORMANCE",
    severity: "HIGH",
    status: "ACTIVE",
    triggeredAt: "2024-01-15T10:30:00Z",
    component: "API Server",
  },
  {
    id: 2,
    name: "Database Connection Pool Exhausted",
    type: "RESOURCE",
    severity: "MEDIUM",
    status: "ACKNOWLEDGED",
    triggeredAt: "2024-01-15T09:15:00Z",
    component: "Database",
  },
  {
    id: 3,
    name: "Memory Leak Detected",
    type: "PERFORMANCE",
    severity: "HIGH",
    status: "ACTIVE",
    triggeredAt: "2024-01-15T08:45:00Z",
    component: "Backend Service",
  },
];

const mockMetrics = {
  latency: { value: 145, unit: "ms", trend: "down" as const },
  throughput: { value: 1250, unit: "req/s", trend: "up" as const },
  errorRate: { value: 0.2, unit: "%", trend: "down" as const },
  availability: { value: 99.8, unit: "%", trend: "stable" as const },
};

const mockComponents = [
  { name: "API Gateway", status: "Healthy", uptime: "99.9%" },
  { name: "Database", status: "Degraded", uptime: "98.5%" },
  { name: "Backend Services", status: "Healthy", uptime: "99.7%" },
  { name: "Infrastructure", status: "Healthy", uptime: "99.8%" },
];

export default function MonitoringDashboardPage() {
  const { t } = useTranslation();
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      setIsLoading(false);
    }, 1000);
    return () => clearTimeout(timer);
  }, []);

  const getHealthColor = (health: string) => {
    switch (health) {
      case "Healthy":
        return "text-green-600";
      case "Degraded":
        return "text-yellow-600";
      case "Critical":
        return "text-red-600";
      default:
        return "text-gray-600";
    }
  };

  const getStatusBadge = (status: string) => {
    const variants: Record<string, "primary" | "danger" | "secondary"> = {
      ACTIVE: "danger",
      ACKNOWLEDGED: "primary",
      RESOLVED: "secondary",
    };
    return variants[status] || "primary";
  };

  if (isLoading) {
    return (
      <div className="min-h-screen bg-background">
        <div className="w-full px-4 py-6">
          <div className="flex items-center justify-center h-64">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background">
      <div className="w-full px-4 py-6 space-y-6">
        {/* Header */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
          className="flex items-center justify-between"
        >
          <div className="flex items-center gap-3">
            <Activity className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold">
                {t("monitoring.dashboard.title", "Dashboard de Monitorización")}
              </h1>
              <p className="text-muted-foreground">
                {t(
                  "monitoring.dashboard.description",
                  "Vista general del estado del sistema y servicios"
                )}
              </p>
            </div>
          </div>
        </motion.div>

        {/* KPIs Principales */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.1 }}
          className="grid grid-cols-1 md:grid-cols-4 gap-4"
        >
          <Card>
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-muted-foreground mb-2">
                    {t("monitoring.dashboard.totalAlerts", "Total Alertas")}
                  </p>
                  <p className="text-3xl font-bold">{mockKPIs.totalAlerts}</p>
                </div>
                <AlertTriangle className="w-8 h-8 text-blue-500" />
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-muted-foreground mb-2">
                    {t("monitoring.dashboard.criticalAlerts", "Alertas Críticas")}
                  </p>
                  <p className="text-3xl font-bold">{mockKPIs.criticalAlerts}</p>
                </div>
                <AlertTriangle className="w-8 h-8 text-red-500" />
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-muted-foreground mb-2">
                    {t("monitoring.dashboard.activeMetrics", "Métricas Activas")}
                  </p>
                  <p className="text-3xl font-bold">{mockKPIs.activeMetrics}</p>
                </div>
                <BarChart3 className="w-8 h-8 text-green-500" />
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-muted-foreground mb-2">
                    {t("monitoring.dashboard.systemHealth", "Salud del Sistema")}
                  </p>
                  <p className={`text-3xl font-bold ${getHealthColor(mockKPIs.systemHealth)}`}>
                    {mockKPIs.systemHealth}
                  </p>
                </div>
                <CheckCircle className="w-8 h-8 text-purple-500" />
              </div>
            </CardContent>
          </Card>
        </motion.div>

        {/* Métricas en Tiempo Real */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.2 }}
        >
          <Card>
            <CardHeader>
              <CardTitle>
                {t("monitoring.dashboard.realTimeMetrics", "Métricas en Tiempo Real")}
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <span className="text-sm text-muted-foreground">
                      {t("monitoring.dashboard.latency", "Latencia")}
                    </span>
                    {mockMetrics.latency.trend === "down" ? (
                      <TrendingDown className="w-4 h-4 text-green-500" />
                    ) : (
                      <TrendingUp className="w-4 h-4 text-red-500" />
                    )}
                  </div>
                  <p className="text-2xl font-bold">
                    {mockMetrics.latency.value} {mockMetrics.latency.unit}
                  </p>
                </div>

                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <span className="text-sm text-muted-foreground">
                      {t("monitoring.dashboard.throughput", "Throughput")}
                    </span>
                    {mockMetrics.throughput.trend === "up" ? (
                      <TrendingUp className="w-4 h-4 text-green-500" />
                    ) : (
                      <TrendingDown className="w-4 h-4 text-red-500" />
                    )}
                  </div>
                  <p className="text-2xl font-bold">
                    {mockMetrics.throughput.value} {mockMetrics.throughput.unit}
                  </p>
                </div>

                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <span className="text-sm text-muted-foreground">
                      {t("monitoring.dashboard.errorRate", "Tasa de Error")}
                    </span>
                    {mockMetrics.errorRate.trend === "down" ? (
                      <TrendingDown className="w-4 h-4 text-green-500" />
                    ) : (
                      <TrendingUp className="w-4 h-4 text-red-500" />
                    )}
                  </div>
                  <p className="text-2xl font-bold">
                    {mockMetrics.errorRate.value} {mockMetrics.errorRate.unit}
                  </p>
                </div>

                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <span className="text-sm text-muted-foreground">
                      {t("monitoring.dashboard.availability", "Disponibilidad")}
                    </span>
                    <CheckCircle className="w-4 h-4 text-green-500" />
                  </div>
                  <p className="text-2xl font-bold">
                    {mockMetrics.availability.value} {mockMetrics.availability.unit}
                  </p>
                </div>
              </div>
            </CardContent>
          </Card>
        </motion.div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Alertas Recientes */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5, delay: 0.3 }}
          >
            <Card>
              <CardHeader>
                <div className="flex items-center justify-between">
                  <CardTitle>
                    {t("monitoring.dashboard.recentAlerts", "Alertas Recientes")}
                  </CardTitle>
                  <Link href="/monitoring/alerts">
                    <Button variant="outline" size="sm">
                      {t("common.viewAll", "Ver Todas")}
                    </Button>
                  </Link>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                {mockRecentAlerts.map((alert) => (
                  <div
                    key={alert.id}
                    className="flex items-center justify-between p-4 rounded-lg border border-border/50 hover:bg-background/40 transition-colors"
                  >
                    <div className="flex-1">
                      <div className="flex items-center gap-2 mb-1">
                        <h4 className="font-medium">{alert.name}</h4>
                        <Badge variant={getStatusBadge(alert.status)}>
                          {alert.status}
                        </Badge>
                      </div>
                      <div className="flex items-center gap-4 text-sm text-muted-foreground">
                        <span>{alert.type}</span>
                        <span>{alert.severity}</span>
                        <span className="flex items-center gap-1">
                          <Clock className="w-3 h-3" />
                          {new Date(alert.triggeredAt).toLocaleString("es-ES")}
                        </span>
                      </div>
                      <p className="text-xs text-muted-foreground mt-1">
                        {alert.component}
                      </p>
                    </div>
                    <Link href={`/monitoring/alerts/${alert.id}`}>
                      <Button variant="ghost" size="sm">
                        {t("common.details", "Detalles")}
                      </Button>
                    </Link>
                  </div>
                ))}
              </CardContent>
            </Card>
          </motion.div>

          {/* Salud de Componentes */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5, delay: 0.4 }}
          >
            <Card>
              <CardHeader>
                <CardTitle>
                  {t("monitoring.dashboard.componentHealth", "Salud de Componentes")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                {mockComponents.map((component, index) => (
                  <div
                    key={index}
                    className="flex items-center justify-between p-4 rounded-lg border border-border/50 hover:bg-background/40 transition-colors"
                  >
                    <div className="flex items-center gap-3">
                      <Server className="w-5 h-5 text-muted-foreground" />
                      <div>
                        <p className="font-medium">{component.name}</p>
                        <p className="text-sm text-muted-foreground">
                          Uptime: {component.uptime}
                        </p>
                      </div>
                    </div>
                    <Badge
                      variant={
                        component.status === "Healthy"
                          ? "primary"
                          : component.status === "Degraded"
                          ? "outline"
                          : "danger"
                      }
                    >
                      {component.status}
                    </Badge>
                  </div>
                ))}
              </CardContent>
            </Card>
          </motion.div>
        </div>
      </div>
    </div>
  );
}
