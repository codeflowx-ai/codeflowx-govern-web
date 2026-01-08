"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import {
  Activity,
  RefreshCw,
  TrendingUp,
  Users,
  Settings,
  Server,
  BarChart3,
  AlertTriangle,
  Clock,
  Zap,
  Shield,
} from "lucide-react";
interface DashboardMetrics {
  totalAgents: number;
  activeAgents: number;
  deployedAgents: number;
  healthScore: number;
  avgPerformanceScore: number;
  offlineAgents: number;
  pendingApprovals: number;
  criticalAlerts: number;
  totalInteractions24h: number;
  avgResponseTime: number;
  complianceScore: number;
  recentAgents: Array<{
    id: number;
    name: string;
    status: string;
    version: string;
    healthScore: number;
    lastInteraction: string;
  }>;
}
export default function AgentsDashboardPage() {
  const { t, mounted } = useTranslation();
  const [metrics, setMetrics] = useState<DashboardMetrics | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDashboardData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
  const loadDashboardData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/dashboard/metrics");
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const data = await response.json();
      // Validar y normalizar datos de la API
      setMetrics({
        totalAgents: data.totalAgents ?? 0,
        activeAgents: data.activeAgents ?? 0,
        deployedAgents: data.deployedAgents ?? 0,
        healthScore: data.healthScore ?? 0,
        avgPerformanceScore: data.avgPerformanceScore ?? 0,
        offlineAgents: data.offlineAgents ?? 0,
        pendingApprovals: data.pendingApprovals ?? 0,
        criticalAlerts: data.criticalAlerts ?? 0,
        totalInteractions24h: data.totalInteractions24h ?? 0,
        avgResponseTime: data.avgResponseTime ?? 0,
        complianceScore: data.complianceScore ?? 0,
        recentAgents: data.recentAgents ?? [],
      });
    } catch (error) {
      console.error("Error loading dashboard:", error);
      // Mock data para desarrollo
      setMetrics({
        totalAgents: 24,
        activeAgents: 18,
        deployedAgents: 15,
        healthScore: 87,
        avgPerformanceScore: 92,
        offlineAgents: 3,
        pendingApprovals: 5,
        criticalAlerts: 2,
        totalInteractions24h: 12450,
        avgResponseTime: 245,
        complianceScore: 94,
        recentAgents: [
          { id: 1, name: "Credit Scoring Agent", status: "ACTIVE", version: "v2.1.0", healthScore: 95, lastInteraction: "2 min ago" },
          { id: 2, name: "Document OCR Agent", status: "ACTIVE", version: "v1.9.5", healthScore: 92, lastInteraction: "5 min ago" },
          { id: 3, name: "Fraud Detection Agent", status: "DEPLOYED", version: "v2.0.3", healthScore: 88, lastInteraction: "1 min ago" },
          { id: 4, name: "Invoice Processing Agent", status: "ACTIVE", version: "v1.8.2", healthScore: 90, lastInteraction: "3 min ago" },
          { id: 5, name: "Customer Onboarding Agent", status: "ACTIVE", version: "v2.2.1", healthScore: 93, lastInteraction: "4 min ago" },
          { id: 6, name: "Risk Assessment Agent", status: "DEPLOYED", version: "v1.7.0", healthScore: 87, lastInteraction: "6 min ago" },
          { id: 7, name: "Contract Analysis Agent", status: "ACTIVE", version: "v2.0.5", healthScore: 91, lastInteraction: "1 min ago" },
          { id: 8, name: "Compliance Monitoring Agent", status: "ACTIVE", version: "v1.9.8", healthScore: 94, lastInteraction: "2 min ago" },
          { id: 9, name: "KYC Verification Agent", status: "DEPLOYED", version: "v2.1.2", healthScore: 89, lastInteraction: "3 min ago" },
          { id: 10, name: "Transaction Monitoring Agent", status: "ACTIVE", version: "v1.6.4", healthScore: 96, lastInteraction: "1 min ago" },
        ],
      });
    } finally {
      setLoading(false);
    }
  };
  if (!mounted || loading || !metrics) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <RefreshCw className="w-8 h-8 animate-spin mx-auto mb-2 text-muted-foreground" />
          <p className="text-muted-foreground">{t("common.loading", "Cargando...")}</p>
        </div>
      </div>
    );
  }

  const kpiCards = [
    {
      title: t("agents.dashboard.totalAgents", "Total Agentes"),
      value: (metrics.totalAgents ?? 0).toString(),
      subtitle: `${metrics.activeAgents ?? 0} ${t("agents.dashboard.active", "activos")}`,
      icon: Users,
      color: "#3b82f6",
    },
    {
      title: t("agents.dashboard.healthScore", "Health Score Promedio"),
      value: `${metrics.healthScore ?? 0}%`,
      subtitle: `${metrics.offlineAgents ?? 0} ${t("agents.dashboard.offline", "offline")}`,
      icon: BarChart3,
      color: (metrics.healthScore ?? 0) >= 90 ? "#10b981" : (metrics.healthScore ?? 0) >= 70 ? "#f59e0b" : "#ef4444",
    },
    {
      title: t("agents.dashboard.complianceScore", "Compliance Score"),
      value: `${metrics.complianceScore ?? 0}%`,
      subtitle: "EU AI Act",
      icon: Shield,
      color: "#8b5cf6",
    },
    {
      title: t("agents.dashboard.criticalAlerts", "Alertas Críticas"),
      value: (metrics.criticalAlerts ?? 0).toString(),
      subtitle: `${metrics.pendingApprovals ?? 0} ${t("agents.dashboard.pendingApprovals", "aprobaciones pendientes")}`,
      icon: AlertTriangle,
      color: (metrics.criticalAlerts ?? 0) > 0 ? "#ef4444" : "#10b981",
    },
    {
      title: t("agents.dashboard.totalInteractions24h", "Interacciones 24h"),
      value: (metrics.totalInteractions24h ?? 0).toLocaleString(),
      subtitle: `${metrics.avgResponseTime ?? 0}ms promedio`,
      icon: Activity,
      color: "#06b6d4",
    },
    {
      title: t("agents.dashboard.deployedAgents", "Agentes Desplegados"),
      value: (metrics.deployedAgents ?? 0).toString(),
      subtitle: `${metrics.deployedAgents ?? 0}/${metrics.totalAgents ?? 0} ${t("agents.dashboard.inProduction", "en producción")}`,
      icon: Server,
      color: "#8b5cf6",
    },
  ];

  return (
    <div className="space-y-4 w-full">
      {/* Header - Título alineado a la izquierda */}
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <Settings className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("agents.dashboard.title", "Dashboard de Agentes")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("agents.dashboard.subtitle", "Métricas y estado general de agentes AI")}
          </p>
        </div>
        <Button onClick={loadDashboardData} variant="outline" size="sm" className="flex items-center gap-2">
          <RefreshCw className={`h-4 w-4 ${loading ? "animate-spin" : ""}`} />
          {t("agents.dashboard.refresh", "Actualizar")}
        </Button>
      </div>
      {/* KPIs Principales */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {kpiCards.map((kpi, index) => (
          <Card key={index} className="border-primary hover:shadow-lg hover:border-primary/50 transition-all">
            <CardBody className="p-4">
              <div className="flex items-center justify-between">
                <div className="flex-grow">
                  <p className="text-xs font-medium text-muted-foreground mb-1">
                    {kpi.title}
                  </p>
                  <h2 className="text-2xl font-bold mb-1" style={{ color: kpi.color }}>
                    {kpi.value}
                  </h2>
                  <p className="text-xs text-muted-foreground">
                    {kpi.subtitle}
                  </p>
                </div>
                <kpi.icon className="w-8 h-8 flex-shrink-0" style={{ color: kpi.color }} />
              </div>
            </CardBody>
          </Card>
        ))}
      </div>
      {/* Recent Agents */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50">
        <CardHeader className="pb-3">
          <CardTitle className="text-base font-medium">{t("agents.dashboard.recentAgents", "Agentes Recientes")}</CardTitle>
        </CardHeader>
        <CardBody className="pt-0">
          {metrics.recentAgents && metrics.recentAgents.length > 0 ? (
            <div className="space-y-3">
              {metrics.recentAgents.map((agent) => (
                <div key={agent.id} className="p-3 border rounded-lg bg-background hover:bg-muted/30 transition-colors">
                  <div className="flex items-center justify-between">
                    <div className="flex-1">
                      <div className="flex items-center gap-2 mb-2">
                        <p className="font-medium text-sm">{agent.name}</p>
                        <span className="text-xs text-muted-foreground">
                          {t("agents.dashboard.version", "Versión")}: {agent.version}
                        </span>
                        <span className="text-xs text-muted-foreground">•</span>
                        <span className="text-xs text-muted-foreground">
                          Health: {agent.healthScore ?? 0}%
                        </span>
                        <span className="text-xs text-muted-foreground">•</span>
                        <span className="text-xs text-muted-foreground">
                          {agent.lastInteraction}
                        </span>
                      </div>
                    </div>
                    <span
                      className={`px-2 py-1 rounded text-xs font-medium ${
                        agent.status === "ACTIVE"
                          ? "bg-green-500/20 text-green-500"
                          : agent.status === "DEPLOYED"
                          ? "bg-blue-500/20 text-blue-500"
                          : "bg-gray-500/20 text-gray-500"
                      }`}
                    >
                      {agent.status}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="text-center py-8 text-muted-foreground">
              {t("common.noData", "No hay agentes recientes disponibles.")}
            </div>
          )}
        </CardBody>
      </Card>

      {/* Performance Metrics */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50">
        <CardHeader className="pb-3">
          <CardTitle className="text-base font-medium">{t("agents.dashboard.performanceMetrics", "Métricas de Rendimiento")}</CardTitle>
        </CardHeader>
        <CardBody className="pt-0">
          <div className="space-y-4">
            <div className="p-3 rounded-lg bg-card/50 border border-border">
              <div className="flex items-center justify-between mb-2">
                <span className="text-sm font-medium text-foreground flex items-center gap-2">
                  <TrendingUp className="w-4 h-4" />
                  {t("agents.dashboard.avgPerformanceScore", "Performance Promedio")}
                </span>
                <span className="text-lg font-bold text-primary">
                  {metrics.avgPerformanceScore ?? 0}%
                </span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2 overflow-hidden">
                <div
                  className="h-2 rounded-full transition-all duration-500"
                  style={{
                    width: `${metrics.avgPerformanceScore ?? 0}%`,
                    backgroundColor: "#3b82f6",
                  }}
                />
              </div>
            </div>
            <div className="p-3 rounded-lg bg-card/50 border border-border">
              <div className="flex items-center justify-between mb-2">
                <span className="text-sm font-medium text-foreground flex items-center gap-2">
                  <Clock className="w-4 h-4" />
                  {t("agents.dashboard.avgResponseTime", "Tiempo de Respuesta Promedio")}
                </span>
                <span className="text-lg font-bold text-primary">
                  {metrics.avgResponseTime ?? 0}ms
                </span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2 overflow-hidden">
                <div
                  className="h-2 rounded-full transition-all duration-500"
                  style={{
                    width: `${Math.min(100, ((metrics.avgResponseTime ?? 0) / 1000) * 100)}%`,
                    backgroundColor: (metrics.avgResponseTime ?? 0) < 300 ? "#10b981" : (metrics.avgResponseTime ?? 0) < 500 ? "#f59e0b" : "#ef4444",
                  }}
                />
              </div>
            </div>
            <div className="p-3 rounded-lg bg-card/50 border border-border">
              <div className="flex items-center justify-between mb-2">
                <span className="text-sm font-medium text-foreground flex items-center gap-2">
                  <Zap className="w-4 h-4" />
                  {t("agents.dashboard.totalInteractions24h", "Interacciones Últimas 24h")}
                </span>
                <span className="text-lg font-bold text-primary">
                  {(metrics.totalInteractions24h ?? 0).toLocaleString()}
                </span>
              </div>
              <p className="text-xs text-muted-foreground mt-1">
                {t("agents.dashboard.interactionsPerHour", "Promedio por hora")}: {Math.round((metrics.totalInteractions24h ?? 0) / 24).toLocaleString()}
              </p>
            </div>
          </div>
        </CardBody>
      </Card>
    </div>
  );
}
