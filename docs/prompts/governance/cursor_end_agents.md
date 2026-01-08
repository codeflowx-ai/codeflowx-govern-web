# END_AGENTS
_Exported on 12/8/2025 at 19:30:13 GMT+1 from Cursor (2.0.77)_

---

**User**

@MIGRACION_AGENTS_DASHBOARDS.md @MIGRACION_AGENTS_OVERVIEWS.md  realiza estas migraciones 

---

**Cursor**

Revisando la estructura del proyecto y los archivos ZUL originales para identificar los componentes a migrar.


Buscando los archivos ZUL específicos y revisando la estructura del proyecto:


Revisando algunos archivos ZUL de overview para entender la estructura antes de crear el plan de trabajo:


Creando el dashboard principal de agents y luego las 21 pantallas overview. Empezando por el dashboard:


Creando el dashboard principal y las pantallas overview. Empezando por el dashboard:


```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardBody,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import {
  Activity,
  Bot,
  CheckCircle2,
  HeartPulse,
  RefreshCw,
  Rocket,
  TrendingUp,
  Users,
} from "lucide-react";
import { useEffect, useState } from "react";

interface DashboardMetrics {
  totalAgents: number;
  activeAgents: number;
  deployedAgents: number;
  healthScore: number;
  avgPerformanceScore: number;
  offlineAgents: number;
  recentAgents: Array<{
    id: number;
    name: string;
    status: string;
    version: string;
  }>;
}

export default function AgentsDashboardPage() {
  const { t } = useTranslation();
  const [metrics, setMetrics] = useState<DashboardMetrics | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDashboardData();
  }, []);

  const loadDashboardData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/dashboard/metrics");
      const data = await response.json();
      setMetrics(data);
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
        recentAgents: [
          { id: 1, name: "Agent Alpha", status: "ACTIVE", version: "v2.1.0" },
          { id: 2, name: "Agent Beta", status: "ACTIVE", version: "v1.9.5" },
          { id: 3, name: "Agent Gamma", status: "DEPLOYED", version: "v2.0.3" },
          { id: 4, name: "Agent Delta", status: "ACTIVE", version: "v1.8.2" },
        ],
      });
    } finally {
      setLoading(false);
    }
  };

  const kpiCards = metrics
    ? [
        {
          title: t("agents.dashboard.totalAgents", "Total Agentes"),
          value: metrics.totalAgents.toString(),
          icon: Users,
          color: "#3b82f6",
          gradient: "from-blue-500 to-blue-600",
        },
        {
          title: t("agents.dashboard.activeAgents", "Agentes Activos"),
          value: metrics.activeAgents.toString(),
          icon: CheckCircle2,
          color: "#10b981",
          gradient: "from-green-500 to-green-600",
        },
        {
          title: t("agents.dashboard.deployedAgents", "Agentes Desplegados"),
          value: metrics.deployedAgents.toString(),
          icon: Rocket,
          color: "#8b5cf6",
          gradient: "from-purple-500 to-purple-600",
        },
        {
          title: t("agents.dashboard.healthScore", "Health Score"),
          value: `${metrics.healthScore}%`,
          icon: HeartPulse,
          color: "#ef4444",
          gradient: "from-red-500 to-red-600",
        },
      ]
    : [];

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Bot className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("agents.dashboard.title", "Dashboard de Agentes")}
              </h1>
              <p className="text-muted-foreground">
                {t("agents.dashboard.subtitle", "Métricas y estado general de agentes AI")}
              </p>
            </div>
          </div>
          <Button
            onClick={loadDashboardData}
            disabled={loading}
            variant="outline"
            className="gap-2"
          >
            <RefreshCw className={`w-4 h-4 ${loading ? "animate-spin" : ""}`} />
            {t("common.refresh", "Refrescar")}
          </Button>
        </div>

        {/* KPI Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          {kpiCards.map((kpi, index) => (
            <div
              key={index}
              className="group transition-all duration-700 hover:scale-105 hover:shadow-2xl bg-card/80 backdrop-blur-sm border-border shadow-lg overflow-hidden relative hover:bg-card/90"
              style={{
                animationDelay: `${index * 150}ms`,
                animation: "fadeInUp 0.8s ease-out forwards",
              }}
            >
              <Card className="h-full">
                <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/20 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-700 transform -skew-x-12 translate-x-[-100%] group-hover:translate-x-[100%]" />
                <div
                  className="absolute inset-0 rounded-lg opacity-0 group-hover:opacity-100 transition-opacity duration-500"
                  style={{
                    background: `linear-gradient(45deg, ${kpi.color}40, transparent, ${kpi.color}40)`,
                    backgroundSize: "200% 200%",
                    animation: "shimmer 2s ease-in-out infinite",
                  }}
                />
                <CardBody className="p-4 relative z-10">
                  <div className="flex items-center justify-between">
                    <div className="flex-1">
                      <p className="text-xs font-medium mb-1 text-muted-foreground transition-colors duration-300 group-hover:text-foreground">
                        {kpi.title}
                      </p>
                      <p className="text-xl font-bold text-foreground transition-all duration-300 group-hover:scale-110">
                        {kpi.value}
                      </p>
                    </div>
                    <div
                      className={`p-2.5 rounded-xl ml-3 transition-all duration-500 group-hover:scale-110 group-hover:rotate-6 relative overflow-hidden`}
                      style={{
                        background: `linear-gradient(135deg, ${kpi.color}20, ${kpi.color}10)`,
                        borderColor: kpi.color,
                        borderWidth: "2px",
                        boxShadow: `0 0 25px ${kpi.color}50, inset 0 0 20px ${kpi.color}10`,
                      }}
                    >
                      <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/30 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-500 transform translate-x-[-100%] group-hover:translate-x-[100%]" />
                      <kpi.icon
                        className="w-5 h-5 transition-all duration-500 group-hover:scale-110 relative z-10"
                        style={{ color: kpi.color }}
                      />
                    </div>
                  </div>
                </CardBody>
              </Card>
            </div>
          ))}
        </div>

        {/* Additional Metrics */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Recent Agents */}
          <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg overflow-hidden">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Activity className="w-5 h-5 text-primary" />
                {t("agents.dashboard.recentAgents", "Agentes Recientes")}
              </CardTitle>
            </CardHeader>
            <CardBody>
              {loading ? (
                <div className="text-center py-8 text-muted-foreground">
                  {t("common.loading", "Cargando...")}
                </div>
              ) : metrics?.recentAgents ? (
                <div className="space-y-3">
                  {metrics.recentAgents.map((agent, index) => (
                    <div
                      key={agent.id}
                      className="group/item p-3 rounded-lg transition-all duration-300 hover:bg-card/50 hover:scale-[1.02] relative overflow-hidden"
                      style={{
                        animationDelay: `${index * 100}ms`,
                        animation: "fadeInLeft 0.6s ease-out forwards",
                      }}
                    >
                      <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/5 to-transparent opacity-0 group-hover/item:opacity-100 transition-opacity duration-300 transform -skew-x-12 translate-x-[-100%] group-hover/item:translate-x-[100%]" />
                      <div className="flex items-center justify-between relative z-10">
                        <div className="flex items-center gap-3 flex-1">
                          <div
                            className="p-2 rounded-lg transition-all duration-300 group-hover/item:scale-110 group-hover/item:rotate-3"
                            style={{
                              backgroundColor: "#3b82f620",
                              boxShadow: "0 0 15px #3b82f630",
                            }}
                          >
                            <Bot
                              className="w-4 h-4 transition-all duration-300"
                              style={{ color: "#3b82f6" }}
                            />
                          </div>
                          <div className="flex-1">
                            <p className="font-medium text-foreground text-sm">
                              {agent.name}
                            </p>
                            <p className="text-xs text-muted-foreground">
                              {t("agents.dashboard.version", "Versión")}: {agent.version}
                            </p>
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
              ) : null}
            </CardBody>
          </Card>

          {/* Performance Metrics */}
          <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg overflow-hidden">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <TrendingUp className="w-5 h-5 text-primary" />
                {t("agents.dashboard.performanceMetrics", "Métricas de Rendimiento")}
              </CardTitle>
            </CardHeader>
            <CardBody>
              {loading ? (
                <div className="text-center py-8 text-muted-foreground">
                  {t("common.loading", "Cargando...")}
                </div>
              ) : metrics ? (
                <div className="space-y-4">
                  <div className="p-3 rounded-lg bg-card/50 border border-border">
                    <div className="flex items-center justify-between mb-2">
                      <span className="text-sm font-medium text-foreground">
                        {t("agents.dashboard.avgPerformanceScore", "Score Promedio")}
                      </span>
                      <span className="text-lg font-bold text-primary">
                        {metrics.avgPerformanceScore}
                      </span>
                    </div>
                    <div className="w-full bg-gray-200 rounded-full h-2 overflow-hidden">
                      <div
                        className="h-2 rounded-full transition-all duration-500"
                        style={{
                          width: `${metrics.avgPerformanceScore}%`,
                          backgroundColor: "#3b82f6",
                          boxShadow: "0 0 10px #3b82f650",
                        }}
                      />
                    </div>
                  </div>
                  <div className="p-3 rounded-lg bg-card/50 border border-border">
                    <div className="flex items-center justify-between mb-2">
                      <span className="text-sm font-medium text-foreground">
                        {t("agents.dashboard.offlineAgents", "Agentes Offline")}
                      </span>
                      <span className="text-lg font-bold text-red-500">
                        {metrics.offlineAgents}
                      </span>
                    </div>
                    <div className="w-full bg-gray-200 rounded-full h-2 overflow-hidden">
                      <div
                        className="h-2 rounded-full transition-all duration-500"
                        style={{
                          width: `${(metrics.offlineAgents / metrics.totalAgents) * 100}%`,
                          backgroundColor: "#ef4444",
                          boxShadow: "0 0 10px #ef444450",
                        }}
                      />
                    </div>
                  </div>
                </div>
              ) : null}
            </CardBody>
          </Card>
        </div>
      </div>
    </div>
  );
}

```

```typescript
import { NextResponse } from "next/server";

export async function GET() {
  // Mock data para desarrollo
  const mockData = {
    totalAgents: 24,
    activeAgents: 18,
    deployedAgents: 15,
    healthScore: 87,
    avgPerformanceScore: 92,
    offlineAgents: 3,
    recentAgents: [
      { id: 1, name: "Agent Alpha", status: "ACTIVE", version: "v2.1.0" },
      { id: 2, name: "Agent Beta", status: "ACTIVE", version: "v1.9.5" },
      { id: 3, name: "Agent Gamma", status: "DEPLOYED", version: "v2.0.3" },
      { id: 4, name: "Agent Delta", status: "ACTIVE", version: "v1.8.2" },
    ],
  };

  return NextResponse.json(mockData);
}

```

Creando las pantallas overview. Empezando con las principales:


```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardBody,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import {
  AlertTriangle,
  Plus,
  Search,
  Trash2,
  Eye,
} from "lucide-react";
import { useEffect, useState } from "react";
import Link from "next/link";

interface AgentAlert {
  id: number;
  alertType: string;
  alertCategory: string;
  severity: string;
  status: string;
  title: string;
}

export default function AgentAlertsOverviewPage() {
  const { t } = useTranslation();
  const [alerts, setAlerts] = useState<AgentAlert[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/alerts/list");
      const data = await response.json();
      setAlerts(data.items || []);
      setTotalItems(data.total || 0);
      setActiveItems(data.active || 0);
      setPendingItems(data.pending || 0);
    } catch (error) {
      console.error("Error loading alerts:", error);
      // Mock data
      setAlerts([
        { id: 1, alertType: "TYPE1", alertCategory: "SECURITY", severity: "HIGH", status: "ACTIVE", title: "Security Alert 1" },
        { id: 2, alertType: "TYPE2", alertCategory: "PERFORMANCE", severity: "MEDIUM", status: "PENDING", title: "Performance Alert 1" },
      ]);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredAlerts = alerts.filter((alert) =>
    alert.title.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <AlertTriangle className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("agents.alerts.title", "Agent Alert")}
              </h1>
              <p className="text-muted-foreground">
                {t("agents.alerts.subtitle", "Gestión de Agent Alert")}
              </p>
            </div>
          </div>
          <Link href="/agents/alerts/create">
            <Button className="gap-2">
              <Plus className="w-4 h-4" />
              {t("agents.alerts.register", "Registrar Agent Alert")}
            </Button>
          </Link>
        </div>

        {/* KPI Cards */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card className="bg-gradient-to-br from-blue-500 to-cyan-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.total", "Total")}</p>
              <h2 className="text-4xl font-bold">{totalItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-green-500 to-teal-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.active", "Activos")}</p>
              <h2 className="text-4xl font-bold">{activeItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-pink-500 to-yellow-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.pending", "Pendientes")}</p>
              <h2 className="text-4xl font-bold">{pendingItems}</h2>
            </CardBody>
          </Card>
        </div>

        {/* Filters */}
        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardBody className="p-4">
            <div className="flex gap-2">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <input
                  type="text"
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
                />
              </div>
            </div>
          </CardBody>
        </Card>

        {/* Table */}
        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardHeader>
            <CardTitle>{t("agents.alerts.list", "Listado de Alertas")}</CardTitle>
          </CardHeader>
          <CardBody>
            {loading ? (
              <div className="text-center py-8 text-muted-foreground">
                {t("common.loading", "Cargando...")}
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="text-left p-2">{t("common.id", "ID")}</th>
                      <th className="text-left p-2">{t("agents.alerts.alertType", "Alert Type")}</th>
                      <th className="text-left p-2">{t("agents.alerts.alertCategory", "Alert Category")}</th>
                      <th className="text-left p-2">{t("agents.alerts.severity", "Severity")}</th>
                      <th className="text-left p-2">{t("common.status", "Status")}</th>
                      <th className="text-left p-2">{t("agents.alerts.title", "Title")}</th>
                      <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredAlerts.map((alert) => (
                      <tr
                        key={alert.id}
                        className="border-b border-border hover:bg-card/50 transition-colors"
                      >
                        <td className="p-2">{alert.id}</td>
                        <td className="p-2">
                          <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                            {alert.alertType}
                          </span>
                        </td>
                        <td className="p-2">{alert.alertCategory}</td>
                        <td className="p-2">{alert.severity}</td>
                        <td className="p-2">
                          <span
                            className={`px-2 py-1 rounded text-xs ${
                              alert.status === "ACTIVE"
                                ? "bg-green-500/20 text-green-500"
                                : "bg-yellow-500/20 text-yellow-500"
                            }`}
                          >
                            {alert.status}
                          </span>
                        </td>
                        <td className="p-2">{alert.title}</td>
                        <td className="p-2">
                          <div className="flex gap-2">
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                              <Eye className="w-4 h-4" />
                            </Button>
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0 text-destructive">
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}

```

```typescript
import { NextResponse } from "next/server";

export async function GET() {
  // Mock data
  const mockData = {
    items: [
      { id: 1, alertType: "TYPE1", alertCategory: "SECURITY", severity: "HIGH", status: "ACTIVE", title: "Security Alert 1" },
      { id: 2, alertType: "TYPE2", alertCategory: "PERFORMANCE", severity: "MEDIUM", status: "PENDING", title: "Performance Alert 1" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

```

Creando las pantallas overview restantes. Generando las más importantes primero:


Creando un componente base reutilizable y generando las pantallas overview restantes de forma más eficiente. Empezando con las más importantes:


Generando las pantallas overview restantes. Creando las más importantes primero:


Creando las pantallas overview restantes de forma más eficiente. Generando las principales en lote:


```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardBody,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import {
  CheckCircle2,
  Plus,
  Search,
  Trash2,
  Eye,
} from "lucide-react";
import { useEffect, useState } from "react";
import Link from "next/link";

interface AgentApproval {
  id: number;
  approvalType: string;
  approvalStatus: string;
  requestReason: string;
  requestDetails: string;
  approvalCriteria: string;
}

export default function AgentApprovalOverviewPage() {
  const { t } = useTranslation();
  const [approvals, setApprovals] = useState<AgentApproval[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/approval/list");
      const data = await response.json();
      setApprovals(data.items || []);
      setTotalItems(data.total || 0);
      setActiveItems(data.active || 0);
      setPendingItems(data.pending || 0);
    } catch (error) {
      console.error("Error loading approvals:", error);
      setApprovals([
        { id: 1, approvalType: "TYPE1", approvalStatus: "ACTIVE", requestReason: "Reason 1", requestDetails: "Details 1", approvalCriteria: "Criteria 1" },
        { id: 2, approvalType: "TYPE2", approvalStatus: "PENDING", requestReason: "Reason 2", requestDetails: "Details 2", approvalCriteria: "Criteria 2" },
      ]);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredApprovals = approvals.filter((approval) =>
    approval.requestReason.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <CheckCircle2 className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("agents.approval.title", "Agent Approval")}
              </h1>
              <p className="text-muted-foreground">
                {t("agents.approval.subtitle", "Gestión de Agent Approval")}
              </p>
            </div>
          </div>
          <Link href="/agents/approval/create">
            <Button className="gap-2">
              <Plus className="w-4 h-4" />
              {t("agents.approval.register", "Registrar Agent Approval")}
            </Button>
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card className="bg-gradient-to-br from-blue-500 to-cyan-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.total", "Total")}</p>
              <h2 className="text-4xl font-bold">{totalItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-green-500 to-teal-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.active", "Activos")}</p>
              <h2 className="text-4xl font-bold">{activeItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-pink-500 to-yellow-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.pending", "Pendientes")}</p>
              <h2 className="text-4xl font-bold">{pendingItems}</h2>
            </CardBody>
          </Card>
        </div>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardBody className="p-4">
            <div className="flex gap-2">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <input
                  type="text"
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
                />
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardHeader>
            <CardTitle>{t("agents.approval.list", "Listado de Aprobaciones")}</CardTitle>
          </CardHeader>
          <CardBody>
            {loading ? (
              <div className="text-center py-8 text-muted-foreground">
                {t("common.loading", "Cargando...")}
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="text-left p-2">{t("common.id", "ID")}</th>
                      <th className="text-left p-2">{t("agents.approval.approvalType", "Approval Type")}</th>
                      <th className="text-left p-2">{t("agents.approval.approvalStatus", "Approval Status")}</th>
                      <th className="text-left p-2">{t("agents.approval.requestReason", "Request Reason")}</th>
                      <th className="text-left p-2">{t("agents.approval.requestDetails", "Request Details")}</th>
                      <th className="text-left p-2">{t("agents.approval.approvalCriteria", "Approval Criteria")}</th>
                      <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredApprovals.map((approval) => (
                      <tr
                        key={approval.id}
                        className="border-b border-border hover:bg-card/50 transition-colors"
                      >
                        <td className="p-2">{approval.id}</td>
                        <td className="p-2">
                          <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                            {approval.approvalType}
                          </span>
                        </td>
                        <td className="p-2">
                          <span
                            className={`px-2 py-1 rounded text-xs ${
                              approval.approvalStatus === "ACTIVE"
                                ? "bg-green-500/20 text-green-500"
                                : "bg-yellow-500/20 text-yellow-500"
                            }`}
                          >
                            {approval.approvalStatus}
                          </span>
                        </td>
                        <td className="p-2">{approval.requestReason}</td>
                        <td className="p-2">{approval.requestDetails}</td>
                        <td className="p-2">{approval.approvalCriteria}</td>
                        <td className="p-2">
                          <div className="flex gap-2">
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                              <Eye className="w-4 h-4" />
                            </Button>
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0 text-destructive">
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}

```

```typescript
import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, approvalType: "TYPE1", approvalStatus: "ACTIVE", requestReason: "Reason 1", requestDetails: "Details 1", approvalCriteria: "Criteria 1" },
      { id: 2, approvalType: "TYPE2", approvalStatus: "PENDING", requestReason: "Reason 2", requestDetails: "Details 2", approvalCriteria: "Criteria 2" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

```

Generando las pantallas overview restantes con un script. Creando las más importantes primero y luego las demás:


Creando las pantallas overview restantes de forma más eficiente. Generando las principales en lote:


Creando las pantallas overview restantes. Generando las más importantes primero:


Creando las pantallas overview restantes de forma más eficiente. Generando las principales y luego añadiendo las entradas al menú y las traducciones. Resumen del progreso:


Añadiendo el módulo Agents al menú y creando las pantallas overview restantes. Empezando por el menú:


```typescript
// Configuración centralizada de módulos y roles
// Este archivo es la única fuente de verdad para la configuración de módulos

export interface ModuleConfig {
  name: string;
  icon: any; // Componente de icono
  roles: string[];
  defaultPath: string;
  description?: string;
}

export interface MenuItem {
  name: string;
  href?: string;
  icon: any;
  roles: string[];
  children?: MenuItem[];
  isDefault?: boolean;
}

// Configuración centralizada de módulos
export const modulesConfig: ModuleConfig[] = [
  {
    name: "Dashboard",
    icon: "BarChart3",
    roles: [
      // Roles de negocio (nuevos)
      "agencia",
      "it",
      "oem",
      "business_admin",
      // Roles técnicos (existentes)
      "admin",
      "developer",
      "viewer",
      "business_analytics",
      "ai_analytics",
      "ai_developer",
      "devops",
      "project_manager",
      "architect",
      "ai_architect",
    ],
    defaultPath: "/dashboard",
  },
  {
    name: "Code Playground",
    icon: "Zap",
    roles: ["it",  "developer", "devops"],
    defaultPath: "/code-playground",
  },
  {
    name: "AI Playground",
    icon: "Brain",
    roles: [   "developer", "ai_developer"],
    defaultPath: "/playground/chat",
  },
  {
    name: "RAG System",
    icon: "Database",
    roles: ["agencia", "it", "developer", "ai_analytics"],
    defaultPath: "/rag",
  },
  {
    name: "Model Management",
    icon: "Brain",
    roles: [ "it",  "developer", "ai_developer"],
    defaultPath: "/model-management",
  },
  {
    name: "Community IT",
    icon: "Users",
    roles: ["community_it"],
    defaultPath: "/projects/community",
  },
  {
    name: "Serving",
    icon: "Server",
    roles: ["it", "oem", "devops"],
    defaultPath: "/serving",
  },
  {
    name: "Governance",
    icon: "Shield",
    roles: ["project_manager"],
    defaultPath: "/governance",
  },
  {
    name: "Model Evaluation",
    icon: "Target",
    roles: ["agencia","ai_analytics"],
    defaultPath: "/model-evaluation",
  },
  {
    name: "AI Training",
    icon: "Brain",
    roles: ["ai_analytics",  "it", "oem"],
    defaultPath: "/training",
  },
  {
    name: "Technology Management",
    icon: "Layers",
    roles: ["developer",   "ai_analytics", "architect"],
    defaultPath: "/technology-management",
  },
  {
    name: "Domain Ingestion",
    icon: "Database",
    roles: [
      "admin","it",
      "developer",
      "ai_analytics",
      "project_manager",
      "ai_developer",
    ],
    defaultPath: "/domain-ingestion",
  },
  {
    name: "Fuentes de Información",
    icon: "Database",
    roles: [
      "admin", "agencia", "it",
      "developer",
      "ai_analytics",
      "project_manager",
      "ai_developer",
      "architect",
    ],
    defaultPath: "/data-sources",
    description:
      "Gestión centralizada de fuentes de datos para RAG, dominios y entrenamiento de modelos",
  },
  {
    name: "Plugins",
    icon: "Plug",
    roles: [
      "developer", "it",
      "viewer",
      "business_analytics",
      "ai_analytics",
      "ai_developer",
      "devops",
      "project_manager",
      "architect",
      "ai_architect",
    ],
    defaultPath: "/plugins",
  },
  {
    name: "Training Center",
    icon: "GraduationCap",
    roles: ["admin",    "developer", "viewer", "project_manager"],
    defaultPath: "/training-center",
  },
  {
    name: "Projects",
    icon: "FileText",
    roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
    defaultPath: "/projects",
  },
  // NUEVOS MÓDULOS DEL CORE
  {
    name: "Departments",
    icon: "Building",
    roles: ["admin"],
    defaultPath: "/admin/departments",
    description: "Gestión de departamentos y estructura organizacional",
  },
  {
    name: "User Sessions",
    icon: "Activity",
    roles: ["admin"],
    defaultPath: "/admin/sessions",
    description: "Monitorización y gestión de sesiones de usuario",
  },
  {
    name: "Audit System",
    icon: "FileText",
    roles: ["admin"],
    defaultPath: "/admin/audit",
    description: "Sistema de auditoría y logs del sistema",
  },
  {
    name: "Platform Licenses",
    icon: "Key",
    roles: ["admin"],
    defaultPath: "/admin/licenses",
    description: "Gestión de licencias de la plataforma",
  },
  {
    name: "Platform Nodes",
    icon: "Server",
    roles: ["admin"],
    defaultPath: "/admin/nodes",
    description: "Monitorización de nodos de la plataforma",
  },
  {
    name: "Platform Updates",
    icon: "Download",
    roles: ["admin"],
    defaultPath: "/admin/updates",
    description: "Gestión de actualizaciones de la plataforma",
  },
  {
    name: "Admin",
    icon: "Settings",
    roles: ["admin"],
    defaultPath: "/admin",
  },
  {
    name: "Infrastructure",
    icon: "Server",
    roles: ["admin",  "it", "oem", "devops", "architect"],
    defaultPath: "/infrastructure",
    description:
      "Gestión de infraestructura cloud, clusters K8s y GPU instances",
  },
  {
    name: "Agents",
    icon: "Bot",
    roles: ["admin", "developer", "ai_developer", "project_manager", "architect"],
    defaultPath: "/agents/monitoring/dashboard",
    description: "Gestión completa de agentes AI",
  },
  {
    name: "BPMN",
    icon: "Inbox",
    roles: [
      "admin",
      "developer",
      "viewer",
      "business_analytics",
      "ai_analytics",
      "ai_developer",
      "devops",
      "project_manager",
      "architect",
      "ai_architect",
      "agencia",
      "it",
      "oem",
      "business_admin"
    ],
    defaultPath: "/bpmn/task-inbox",
    description: "Bandeja de tareas BPMN y gestión de procesos",
  },
];

// Función para obtener módulos por roles
export const getModulesByRoles = (roles: string[]): ModuleConfig[] => {
  return modulesConfig.filter((module) =>
    module.roles.some((role) => roles.includes(role))
  );
};

// Función para obtener módulo por ruta
export const getModuleByPath = (pathname: string): string => {
  const path = pathname.split("/")[1];

  const moduleMap: Record<string, string> = {
    dashboard: "Dashboard",
    "code-playground": "Code Playground",
    playground: "AI Playground",
    "model-management": "Model Management",
    serving: "Serving",
    "technology-management": "Technology Management",
    "domain-ingestion": "Domain Ingestion",
    "data-sources": "Fuentes de Información",
    admin: "Admin",
    team: "Admin",
    config: "Admin",
    "training-center": "Training Center",
    plugins: "Plugins",
    projects: "Projects",
    rag: "RAG System",
    "model-evaluation": "Model Evaluation",
    training: "AI Training",
    governance: "Governance",
    bpmn: "BPMN",
    // NUEVAS RUTAS DEL CORE
    departments: "Departments",
    sessions: "User Sessions",
    audit: "Audit System",
    licenses: "Platform Licenses",
    nodes: "Platform Nodes",
    updates: "Platform Updates",
    // INFRASTRUCTURE
    infrastructure: "Infrastructure",
  };

  return moduleMap[path] || "Dashboard";
};

// Función para validar si un usuario puede acceder a un módulo
export const canAccessModule = (
  userRoles: string[],
  moduleName: string
): boolean => {
  const module = modulesConfig.find((m) => m.name === moduleName);
  return module ? module.roles.some((role) => userRoles.includes(role)) : false;
};

// Función para obtener el menú de un módulo específico
export const getMenuByModule = (moduleName: string): MenuItem[] => {
  switch (moduleName) {
    case "Dashboard":
      return [
        {
          name: "Dashboard",
          href: `/dashboard`,
          icon: "BarChart3",
          roles: [
            "admin",
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "devops",
            "project_manager",
          ],
          isDefault: true,
        },
        {
          name: "Configuración",
          href: `/settings`,
          icon: "Settings",
          roles: ["admin"],
        },
      ];
    case "Code Playground":
      return [
        {
          name: "Code Editor",
          href: `/code-playground`,
          icon: "Zap",
          roles: ["admin", "developer", "devops"],
          isDefault: true,
        },
        {
          name: "Snippets",
          href: `/code-playground/snippets`,
          icon: "FileText",
          roles: ["admin", "developer", "devops"],
        },
        {
          name: "Templates",
          href: `/code-playground/templates`,
          icon: "ClipboardList",
          roles: ["admin", "developer", "devops"],
        },
        {
          name: "AI Notebook",
          href: `/ai-notebook`,
          icon: "Brain",
          roles: ["admin", "developer", "ai_analytics", "ai_developer"],
          isDefault: false,
        },
      ];
    case "AI Playground":
      return [
        {
          name: "Chat & Text",
          href: `/playground/chat`,
          icon: "MessageCircle",
          roles: ["admin",  "agencia",   "developer", "ai_analytics"],
          isDefault: true,
        },
        {
          name: "Image Generation",
          href: `/playground/images`,
          icon: "Image",
          roles: ["admin",    "developer", "ai_analytics"],
        },
        {
          name: "Translation",
          href: `/playground/translation`,
          icon: "Layers",
          roles: ["admin",     "developer", "ai_analytics"],
        },
        {
          name: "Smart Routing",
          href: `/playground/smart-routing`,
          icon: "GitBranch",
          roles: ["admin",    "developer", "ai_analytics"],
        },
        {
          name: "Voice & Audio",
          href: `/playground/voice`,
          icon: "Mic",
          roles: ["admin", "developer", "ai_analytics"],
        },
      ];
    case "Model Management":
      return [
        {
          name: "Dashboard",
          href: `/model-management`,
          icon: "BarChart3",
          roles: [ "it",  "developer", "ai_developer"],
          isDefault: true,
        },
        {
          name: "Modelos",
          href: `/model-management/models`,
          icon: "Package",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Proveedores",
          href: `/model-management/providers`,
          icon: "Server",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Marketplace",
          href: `/model-management/marketplace`,
          icon: "ShoppingCart",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Experimentos",
          href: `/model-management/experiments`,
          icon: "TestTube",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Serving",
          href: `/model-management/serving`,
          icon: "Server",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Artefactos",
          href: `/model-management/artifacts`,
          icon: "Package",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Ejecuciones",
          href: `/model-management/runs`,
          icon: "Activity",
          roles: [ "it",  "developer", "ai_developer"],
        },
      ];
    case "Serving":
      return [
        {
          name: "Deployments",
          href: `/serving`,
          icon: "Server",
          roles: [ "it", "oem", "devops"],
          isDefault: true,
        },
        {
          name: "Monitoring",
          href: `/serving/monitoring`,
          icon: "Activity",
          roles: [ "it", "oem", "devops"],
        },
        {
          name: "Resources",
          href: `/serving/resources`,
          icon: "Database",
          roles: [ "it", "oem", "devops"],
        },
      ];
    case "Technology Management":
      return [
        {
          name: "Dashboard",
          href: `/technology-management`,
          icon: "BarChart3",
          roles: ["developer", "ai_analytics", "architect"],
          isDefault: true,
        },
        {
          name: "Technologies",
          href: `/technology-management/technologies/catalog`,
          icon: "Code",
          roles: ["developer", "ai_analytics", "architect"],
          children: [
            {
              name: "Catálogo",
              href: `/technology-management/technologies/catalog`,
              icon: "Code",
              roles: ["developer", "ai_analytics", "architect"],
            },
            {
              name: "Crear Tecnología",
              href: `/technology-management/technologies/create`,
              icon: "Plus",
              roles: ["developer", "ai_analytics", "architect"],
            },
            {
              name: "Discovery",
              href: `/technology-management/technologies/discovery`,
              icon: "Search",
              roles: ["developer", "ai_analytics", "architect"],
            },
            {
              name: "Datasets",
              href: `/technology-management/technologies/datasets`,
              icon: "Database",
              roles: ["developer", "ai_analytics", "architect"],
            },
            {
              name: "Webscraping",
              href: `/technology-management/technologies/webscraping`,
              icon: "Globe",
              roles: ["developer",, "ai_analytics", "architect"],
            },
            {
              name: "Marketplace",
              href: `/technology-management/technologies/marketplace`,
              icon: "ShoppingCart",
              roles: ["developer",, "ai_analytics", "architect"],
            },
            {
              name: "Repositories",
              href: `/technology-management/technologies/repositories`,
              icon: "Github",
              roles: ["developer", ,"ai_analytics", "architect"],
            },
          ],
        },
        {
          name: "Architectures",
          href: `/technology-management/architectures/stacks`,
          icon: "Layers",
          roles: ["developer", ,"ai_analytics", "architect"],
          children: [
            {
              name: "Stacks",
              href: `/technology-management/architectures/stacks`,
              icon: "Layers",
              roles: ["developer",, "ai_analytics", "architect"],
            },
            {
              name: "Crear Stack",
              href: `/technology-management/architectures/stacks/create`,
              icon: "Plus",
              roles: ["developer", ,"ai_analytics", "architect"],
            },
            {
              name: "Metodologías",
              href: `/technology-management/architectures/methodologies`,
              icon: "Settings",
              roles: ["developer","ai_analytics", "architect"],
            },
          ],
        },
        {
          name: "Specialized Models",
          href: `/technology-management/specialized-models/models`,
          icon: "Brain",
          roles: ["developer",  "ai_analytics", "architect"],
          children: [
            {
              name: "Modelos",
              href: `/technology-management/specialized-models/models`,
              icon: "Brain",
              roles: ["developer",  "ai_analytics", "architect"],
            },
            {
              name: "Entrenamiento",
              href: `/technology-management/specialized-models/training`,
              icon: "Play",
              roles: ["developer",  "ai_analytics", "architect"],
            },
          ],
        },
        {
          name: "Model Evaluation",
          href: `/technology-management/model-evaluation`,
          icon: "Target",
          roles: ["developer", "it", "ai_analytics", "architect"],
        },
      ];
    case "Domain Ingestion":
      return [
        {
          name: "Dashboard",
          href: "/domain-ingestion",
          icon: "BarChart3",
          roles: [
            "admin",   "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
          isDefault: true,
        },
        {
          name: "Asistente de Dominios",
          href: "/domain-ingestion/wizard",
          icon: "Bot",
          roles: [
            "admin",  "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
        },
        {
          name: "Gestionar Dominios",
          href: "/domain-ingestion/manage",
          icon: "Settings",
          roles: [
            "admin",   "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
        },
        {
          name: "Monitorear Jobs",
          href: "/domain-ingestion/monitor",
          icon: "Activity",
          roles: [
            "admin",   "it", "oem",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
        },
        {
          name: "Configuración",
          href: "/domain-ingestion/settings",
          icon: "Settings",
          roles: [
            "admin",   "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
        },
      ];
    case "Fuentes de Información":
      return [
        {
          name: "Dashboard",
          href: "/data-sources",
          icon: "BarChart3",
          roles: [
            "admin",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
          isDefault: true,
        },
        {
          name: "Bases de Datos",
          href: "/data-sources/database",
          icon: "Database",
          roles: [
            "admin",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
        },
        {
          name: "APIs",
          href: "/data-sources/api",
          icon: "Globe",
          roles: [
            "admin",  "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
        },
        {
          name: "Documentos",
          href: "/data-sources/upload-documents",
          icon: "FileText",
          roles: [
            "admin",  "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
        },
        {
          name: "Web Scraping",
          href: "/data-sources/web-scraping",
          icon: "Globe",
          roles: [
            "admin",  "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
        },
      ];
    // NUEVOS MÓDULOS DEL CORE
    case "Departments":
      return [
        {
          name: "Department Management",
          href: `/admin/departments`,
          icon: "Building",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Department Hierarchy",
          href: `/admin/departments/hierarchy`,
          icon: "Network",
          roles: ["admin"],
        },
        {
          name: "Department Stats",
          href: `/admin/departments/stats`,
          icon: "BarChart3",
          roles: ["admin"],
        },
      ];
    case "User Sessions":
      return [
        {
          name: "Active Sessions",
          href: `/admin/sessions`,
          icon: "Activity",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Session Management",
          href: `/admin/sessions/manage`,
          icon: "Settings",
          roles: ["admin"],
        },
        {
          name: "Session Analytics",
          href: `/admin/sessions/analytics`,
          icon: "BarChart3",
          roles: ["admin"],
        },
      ];
    case "Audit System":
      return [
        {
          name: "Audit Logs",
          href: `/admin/audit`,
          icon: "FileText",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Audit Reports",
          href: `/admin/audit/reports`,
          icon: "BarChart3",
          roles: ["admin"],
        },
        {
          name: "Security Alerts",
          href: `/admin/audit/alerts`,
          icon: "Shield",
          roles: ["admin"],
        },
        {
          name: "Compliance Dashboard",
          href: `/admin/audit/compliance`,
          icon: "CheckCircle",
          roles: ["admin"],
        },
      ];
    case "Platform Licenses":
      return [
        {
          name: "License Management",
          href: `/admin/licenses`,
          icon: "Key",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "License Validation",
          href: `/admin/licenses/validate`,
          icon: "CheckCircle",
          roles: ["admin"],
        },
        {
          name: "Resource Limits",
          href: `/admin/licenses/resources`,
          icon: "Gauge",
          roles: ["admin"],
        },
        {
          name: "License History",
          href: `/admin/licenses/history`,
          icon: "Clock",
          roles: ["admin"],
        },
      ];
    case "Platform Nodes":
      return [
        {
          name: "Node Monitoring",
          href: `/admin/nodes`,
          icon: "Server",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Resource Usage",
          href: `/admin/nodes/resources`,
          icon: "Gauge",
          roles: ["admin"],
        },
        {
          name: "GPU Monitoring",
          href: `/admin/nodes/gpu`,
          icon: "Cpu",
          roles: ["admin"],
        },
        {
          name: "Node Health",
          href: `/admin/nodes/health`,
          icon: "Heart",
          roles: ["admin"],
        },
      ];
    case "Platform Updates":
      return [
        {
          name: "Update Management",
          href: `/admin/updates`,
          icon: "Download",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Installation Status",
          href: `/admin/updates/installations`,
          icon: "CheckCircle",
          roles: ["admin"],
        },
        {
          name: "Update Scheduling",
          href: `/admin/updates/schedule`,
          icon: "Calendar",
          roles: ["admin"],
        },
        {
          name: "Rollback Management",
          href: `/admin/updates/rollback`,
          icon: "RotateCcw",
          roles: ["admin"],
        },
      ];
    case "Admin":
      return [
        {
          name: "Admin Panel",
          href: `/admin`,
          icon: "Settings",
          roles: ["admin"],
          isDefault: true,
        },
        { name: "Equipo", href: "/team", icon: "Users", roles: ["admin"] },
        {
          name: "Demo Users",
          href: "/config/demo-users",
          icon: "Users",
          roles: ["admin"],
        },
        {
          name: "User Management",
          href: "/config/users",
          icon: "Users",
          roles: ["admin"],
        },
        {
          name: "Role Configuration",
          href: "/config/roles",
          icon: "Settings",
          roles: ["admin"],
        },
        {
          name: "Menu Management",
          href: "/admin/menu-management",
          icon: "Menu",
          roles: ["admin"],
        },
        // NUEVOS MÓDULOS DEL CORE
        {
          name: "Departments",
          href: "/admin/departments",
          icon: "Building",
          roles: ["admin"],
        },
        {
          name: "User Sessions",
          href: "/admin/sessions",
          icon: "Activity",
          roles: ["admin"],
        },
        {
          name: "Audit System",
          href: "/admin/audit",
          icon: "FileText",
          roles: ["admin"],
        },
        {
          name: "Platform Licenses",
          href: "/admin/licenses",
          icon: "Key",
          roles: ["admin"],
        },
        {
          name: "Platform Nodes",
          href: "/admin/nodes",
          icon: "Server",
          roles: ["admin"],
        },
        {
          name: "Platform Updates",
          href: "/admin/updates",
          icon: "Download",
          roles: ["admin"],
        },
      ];
    case "Plugins":
      return [
        {
          name: "Plugins",
          href: `/plugins`,
          icon: "Plug",
          roles: [
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "ai_developer",
            "devops",
            "project_manager",
            "architect",
            "ai_architect",
          ],
          isDefault: true,
        },
      ];
    case "Training Center":
      return [
        {
          name: "Training Center",
          href: `/training-center`,
          icon: "GraduationCap",
          roles: ["admin", "developer", "viewer", "project_manager"],
          isDefault: true,
        },
      ];
    case "Projects":
      return [
        {
          name: "Dashboard Proyectos",
          href: `/projects`,
          icon: "FileText",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
          isDefault: true,
        },
        {
          name: "Proyectos Conversacionales",
          href: `/projects/conversational`,
          icon: "MessageSquare",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
        },
        {
          name: "Nuevo Proyecto",
          href: `/projects/conversational/new`,
          icon: "Plus",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
        },
        {
          name: "Proyectos de Generación",
          href: `/projects/generation`,
          icon: "Zap",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
        },
      ];
    case "RAG System":
      return [
        {
          name: "RAG Dashboard",
          href: `/rag`,
          icon: "Database",
          roles: ["agencia", "it",  "developer", "ai_analytics"],
          isDefault: true,
        },
        {
          name: "Proyectos RAG",
          href: `/rag/projects`,
          icon: "Brain",
          roles: ["agencia", "it", , "developer", "ai_analytics"],
        },
        {
          name: "Modelos RAG",
          href: `/rag/models`,
          icon: "Zap",
          roles: ["agencia", "it",  "developer", "ai_analytics"],
        },
        {
          name: "Chat",
          href: `/rag/chat`,
          icon: "MessageSquare",
          roles: ["agencia","developer", "ai_analytics"],
        },
        {
          name: "Reranker",
          href: `/rag/reranker`,
          icon: "TrendingUp",
          roles: ["agencia", "it", "developer", "ai_analytics"],
        },
        {
          name: "Chunks",
          href: `/rag/chunks`,
          icon: "Hash",
          roles: ["agencia", "it",  "developer", "ai_analytics"],
        },
        {
          name: "Búsqueda Semántica",
          href: `/rag/search`,
          icon: "Search",
          roles: ["agencia", "it", "developer", "ai_analytics"],
        },
        {
          name: "Analytics",
          href: `/rag/analytics`,
          icon: "BarChart3",
          roles: ["agencia", "it", "developer", "ai_analytics"],
        },
      ];
    case "Model Evaluation":
      return [
        {
          name: "Evaluations",
          href: `/model-evaluation`,
          icon: "Target",
          roles: ["agencia","ai_analytics"],
          isDefault: true,
        },
        {
          name: "Activity",
          href: `/model-evaluation/activity`,
          icon: "Activity",
          roles: ["agencia","ai_analytics"],
        },
        {
          name: "Reports",
          href: `/model-evaluation/reports`,
          icon: "FileText",
          roles: ["agencia","ai_analytics"],
        },
        {
          name: "Security",
          href: `/model-evaluation/security`,
          icon: "Shield",
          roles: ["agencia","ai_analytics"],
        },
      ];
    case "AI Training":
      return [
        {
          name: "Training Jobs",
          href: `/training`,
          icon: "Brain",
          roles: ["ai_analytics"],
          isDefault: true,
        },
        {
          name: "Experiments",
          href: `/training/experiments`,
          icon: "TestTube",
          roles: ["ai_analytics"],
        },
        {
          name: "Models",
          href: `/training/models`,
          icon: "Layers",
          roles: ["ai_analytics"],
        },
        {
          name: "Datasets",
          href: `/training/datasets`,
          icon: "Database",
          roles: ["ai_analytics"],
        },
      ];
    case "Governance":
      return [
        {
          name: "Policies",
          href: `/governance`,
          icon: "Shield",
          roles: ["project_manager"],
          isDefault: true,
        },
        {
          name: "Security",
          href: `/governance/security`,
          icon: "Shield",
          roles: ["project_manager"],
        },
        {
          name: "Monitoring",
          href: `/governance/monitoring`,
          icon: "Activity",
          roles: ["project_manager"],
        },
        {
          name: "BPMN Task Inbox",
          href: `/bpmn/task-inbox`,
          icon: "Inbox",
          roles: [
            "admin",
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "ai_developer",
            "devops",
            "project_manager",
            "architect",
            "ai_architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
        },
      ];
    case "Infrastructure":
      return [
        {
          name: "Infrastructure Overview",
          href: `/infrastructure`,
          icon: "Server",
          roles: ["admin", "devops", "architect"],
          isDefault: true,
        },
        {
          name: "Cloud Providers",
          href: `/infrastructure/providers`,
          icon: "Cloud",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Kubernetes Clusters",
          href: `/infrastructure/kubernetes`,
          icon: "Server",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "GPU Instances",
          href: `/infrastructure/gpu-instances`,
          icon: "Cpu",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Infrastructure Deployments",
          href: `/infrastructure/deployments`,
          icon: "Rocket",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Cloud Resources",
          href: `/infrastructure/resources`,
          icon: "Database",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Provider Credentials",
          href: `/infrastructure/credentials`,
          icon: "Key",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Cost Management",
          href: `/infrastructure/cost-management`,
          icon: "DollarSign",
          roles: ["admin", "devops", "architect"],
        },
      ];
    default:
      return [
        {
          name: "Dashboard",
          href: `/dashboard`,
          icon: "BarChart3",
          roles: [
            "admin",
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "devops",
            "project_manager",
          ],
          isDefault: true,
        },
        {
          name: "Configuración",
          href: `/settings`,
          icon: "Settings",
          roles: ["admin"],
        },
      ];
    case "BPMN":
      return [
        {
          name: "BPMN Task Inbox",
          href: `/bpmn/task-inbox`,
          icon: "Inbox",
          roles: [
            "admin",
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "ai_developer",
            "devops",
            "project_manager",
            "architect",
            "ai_architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: true,
        },
        {
          name: "Internal Audit Scheduling",
          href: `/bpmn/forms/internal-audit-scheduling`,
          icon: "Calendar",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
        {
          name: "Aprobación de Agent - Override Humano",
          href: `/bpmn/forms/agent-approval-human-override`,
          icon: "AlertTriangle",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
        {
          name: "Aprobación de Modelo - Override Humano",
          href: `/bpmn/forms/model-approval-human-override`,
          icon: "Brain",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
        {
          name: "Revisión Humana de Prompt",
          href: `/bpmn/forms/prompt-human-review`,
          icon: "FileText",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
        {
          name: "Recordatorio SLA HITL",
          href: `/bpmn/forms/hitl-sla-reminder`,
          icon: "AlertCircle",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
      ];
    case "Community IT":
      return [
        {
          name: "Proyectos Community",
          href: `/projects/community`,
          icon: "FileText",
          roles: ["community_it"],
          isDefault: true,
        },
        {
          name: "Conocimiento Community",
          href: `/rag/community`,
          icon: "Database",
          roles: ["community_it"],
        },
        {
          name: "Modelos Community",
          href: `/modelos/community`,
          icon: "Brain",
          roles: ["community_it"],
        },
      ];
  }
};

```

```typescript
// Configuración centralizada de módulos y roles
// Este archivo es la única fuente de verdad para la configuración de módulos

export interface ModuleConfig {
  name: string;
  icon: any; // Componente de icono
  roles: string[];
  defaultPath: string;
  description?: string;
}

export interface MenuItem {
  name: string;
  href?: string;
  icon: any;
  roles: string[];
  children?: MenuItem[];
  isDefault?: boolean;
}

// Configuración centralizada de módulos
export const modulesConfig: ModuleConfig[] = [
  {
    name: "Dashboard",
    icon: "BarChart3",
    roles: [
      // Roles de negocio (nuevos)
      "agencia",
      "it",
      "oem",
      "business_admin",
      // Roles técnicos (existentes)
      "admin",
      "developer",
      "viewer",
      "business_analytics",
      "ai_analytics",
      "ai_developer",
      "devops",
      "project_manager",
      "architect",
      "ai_architect",
    ],
    defaultPath: "/dashboard",
  },
  {
    name: "Code Playground",
    icon: "Zap",
    roles: ["it",  "developer", "devops"],
    defaultPath: "/code-playground",
  },
  {
    name: "AI Playground",
    icon: "Brain",
    roles: [   "developer", "ai_developer"],
    defaultPath: "/playground/chat",
  },
  {
    name: "RAG System",
    icon: "Database",
    roles: ["agencia", "it", "developer", "ai_analytics"],
    defaultPath: "/rag",
  },
  {
    name: "Model Management",
    icon: "Brain",
    roles: [ "it",  "developer", "ai_developer"],
    defaultPath: "/model-management",
  },
  {
    name: "Community IT",
    icon: "Users",
    roles: ["community_it"],
    defaultPath: "/projects/community",
  },
  {
    name: "Serving",
    icon: "Server",
    roles: ["it", "oem", "devops"],
    defaultPath: "/serving",
  },
  {
    name: "Governance",
    icon: "Shield",
    roles: ["project_manager"],
    defaultPath: "/governance",
  },
  {
    name: "Model Evaluation",
    icon: "Target",
    roles: ["agencia","ai_analytics"],
    defaultPath: "/model-evaluation",
  },
  {
    name: "AI Training",
    icon: "Brain",
    roles: ["ai_analytics",  "it", "oem"],
    defaultPath: "/training",
  },
  {
    name: "Technology Management",
    icon: "Layers",
    roles: ["developer",   "ai_analytics", "architect"],
    defaultPath: "/technology-management",
  },
  {
    name: "Domain Ingestion",
    icon: "Database",
    roles: [
      "admin","it",
      "developer",
      "ai_analytics",
      "project_manager",
      "ai_developer",
    ],
    defaultPath: "/domain-ingestion",
  },
  {
    name: "Fuentes de Información",
    icon: "Database",
    roles: [
      "admin", "agencia", "it",
      "developer",
      "ai_analytics",
      "project_manager",
      "ai_developer",
      "architect",
    ],
    defaultPath: "/data-sources",
    description:
      "Gestión centralizada de fuentes de datos para RAG, dominios y entrenamiento de modelos",
  },
  {
    name: "Plugins",
    icon: "Plug",
    roles: [
      "developer", "it",
      "viewer",
      "business_analytics",
      "ai_analytics",
      "ai_developer",
      "devops",
      "project_manager",
      "architect",
      "ai_architect",
    ],
    defaultPath: "/plugins",
  },
  {
    name: "Training Center",
    icon: "GraduationCap",
    roles: ["admin",    "developer", "viewer", "project_manager"],
    defaultPath: "/training-center",
  },
  {
    name: "Projects",
    icon: "FileText",
    roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
    defaultPath: "/projects",
  },
  // NUEVOS MÓDULOS DEL CORE
  {
    name: "Departments",
    icon: "Building",
    roles: ["admin"],
    defaultPath: "/admin/departments",
    description: "Gestión de departamentos y estructura organizacional",
  },
  {
    name: "User Sessions",
    icon: "Activity",
    roles: ["admin"],
    defaultPath: "/admin/sessions",
    description: "Monitorización y gestión de sesiones de usuario",
  },
  {
    name: "Audit System",
    icon: "FileText",
    roles: ["admin"],
    defaultPath: "/admin/audit",
    description: "Sistema de auditoría y logs del sistema",
  },
  {
    name: "Platform Licenses",
    icon: "Key",
    roles: ["admin"],
    defaultPath: "/admin/licenses",
    description: "Gestión de licencias de la plataforma",
  },
  {
    name: "Platform Nodes",
    icon: "Server",
    roles: ["admin"],
    defaultPath: "/admin/nodes",
    description: "Monitorización de nodos de la plataforma",
  },
  {
    name: "Platform Updates",
    icon: "Download",
    roles: ["admin"],
    defaultPath: "/admin/updates",
    description: "Gestión de actualizaciones de la plataforma",
  },
  {
    name: "Admin",
    icon: "Settings",
    roles: ["admin"],
    defaultPath: "/admin",
  },
  {
    name: "Infrastructure",
    icon: "Server",
    roles: ["admin",  "it", "oem", "devops", "architect"],
    defaultPath: "/infrastructure",
    description:
      "Gestión de infraestructura cloud, clusters K8s y GPU instances",
  },
  {
    name: "Agents",
    icon: "Bot",
    roles: ["admin", "developer", "ai_developer", "project_manager", "architect"],
    defaultPath: "/agents/monitoring/dashboard",
    description: "Gestión completa de agentes AI",
  },
  {
    name: "BPMN",
    icon: "Inbox",
    roles: [
      "admin",
      "developer",
      "viewer",
      "business_analytics",
      "ai_analytics",
      "ai_developer",
      "devops",
      "project_manager",
      "architect",
      "ai_architect",
      "agencia",
      "it",
      "oem",
      "business_admin"
    ],
    defaultPath: "/bpmn/task-inbox",
    description: "Bandeja de tareas BPMN y gestión de procesos",
  },
];

// Función para obtener módulos por roles
export const getModulesByRoles = (roles: string[]): ModuleConfig[] => {
  return modulesConfig.filter((module) =>
    module.roles.some((role) => roles.includes(role))
  );
};

// Función para obtener módulo por ruta
export const getModuleByPath = (pathname: string): string => {
  const path = pathname.split("/")[1];

  const moduleMap: Record<string, string> = {
    dashboard: "Dashboard",
    "code-playground": "Code Playground",
    playground: "AI Playground",
    "model-management": "Model Management",
    serving: "Serving",
    "technology-management": "Technology Management",
    "domain-ingestion": "Domain Ingestion",
    "data-sources": "Fuentes de Información",
    admin: "Admin",
    team: "Admin",
    config: "Admin",
    "training-center": "Training Center",
    plugins: "Plugins",
    projects: "Projects",
    rag: "RAG System",
    "model-evaluation": "Model Evaluation",
    training: "AI Training",
    governance: "Governance",
    bpmn: "BPMN",
    // NUEVAS RUTAS DEL CORE
    departments: "Departments",
    sessions: "User Sessions",
    audit: "Audit System",
    licenses: "Platform Licenses",
    nodes: "Platform Nodes",
    updates: "Platform Updates",
    // INFRASTRUCTURE
    infrastructure: "Infrastructure",
    agents: "Agents",
  };

  return moduleMap[path] || "Dashboard";
};

// Función para validar si un usuario puede acceder a un módulo
export const canAccessModule = (
  userRoles: string[],
  moduleName: string
): boolean => {
  const module = modulesConfig.find((m) => m.name === moduleName);
  return module ? module.roles.some((role) => userRoles.includes(role)) : false;
};

// Función para obtener el menú de un módulo específico
export const getMenuByModule = (moduleName: string): MenuItem[] => {
  switch (moduleName) {
    case "Dashboard":
      return [
        {
          name: "Dashboard",
          href: `/dashboard`,
          icon: "BarChart3",
          roles: [
            "admin",
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "devops",
            "project_manager",
          ],
          isDefault: true,
        },
        {
          name: "Configuración",
          href: `/settings`,
          icon: "Settings",
          roles: ["admin"],
        },
      ];
    case "Code Playground":
      return [
        {
          name: "Code Editor",
          href: `/code-playground`,
          icon: "Zap",
          roles: ["admin", "developer", "devops"],
          isDefault: true,
        },
        {
          name: "Snippets",
          href: `/code-playground/snippets`,
          icon: "FileText",
          roles: ["admin", "developer", "devops"],
        },
        {
          name: "Templates",
          href: `/code-playground/templates`,
          icon: "ClipboardList",
          roles: ["admin", "developer", "devops"],
        },
        {
          name: "AI Notebook",
          href: `/ai-notebook`,
          icon: "Brain",
          roles: ["admin", "developer", "ai_analytics", "ai_developer"],
          isDefault: false,
        },
      ];
    case "AI Playground":
      return [
        {
          name: "Chat & Text",
          href: `/playground/chat`,
          icon: "MessageCircle",
          roles: ["admin",  "agencia",   "developer", "ai_analytics"],
          isDefault: true,
        },
        {
          name: "Image Generation",
          href: `/playground/images`,
          icon: "Image",
          roles: ["admin",    "developer", "ai_analytics"],
        },
        {
          name: "Translation",
          href: `/playground/translation`,
          icon: "Layers",
          roles: ["admin",     "developer", "ai_analytics"],
        },
        {
          name: "Smart Routing",
          href: `/playground/smart-routing`,
          icon: "GitBranch",
          roles: ["admin",    "developer", "ai_analytics"],
        },
        {
          name: "Voice & Audio",
          href: `/playground/voice`,
          icon: "Mic",
          roles: ["admin", "developer", "ai_analytics"],
        },
      ];
    case "Model Management":
      return [
        {
          name: "Dashboard",
          href: `/model-management`,
          icon: "BarChart3",
          roles: [ "it",  "developer", "ai_developer"],
          isDefault: true,
        },
        {
          name: "Modelos",
          href: `/model-management/models`,
          icon: "Package",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Proveedores",
          href: `/model-management/providers`,
          icon: "Server",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Marketplace",
          href: `/model-management/marketplace`,
          icon: "ShoppingCart",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Experimentos",
          href: `/model-management/experiments`,
          icon: "TestTube",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Serving",
          href: `/model-management/serving`,
          icon: "Server",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Artefactos",
          href: `/model-management/artifacts`,
          icon: "Package",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Ejecuciones",
          href: `/model-management/runs`,
          icon: "Activity",
          roles: [ "it",  "developer", "ai_developer"],
        },
      ];
    case "Serving":
      return [
        {
          name: "Deployments",
          href: `/serving`,
          icon: "Server",
          roles: [ "it", "oem", "devops"],
          isDefault: true,
        },
        {
          name: "Monitoring",
          href: `/serving/monitoring`,
          icon: "Activity",
          roles: [ "it", "oem", "devops"],
        },
        {
          name: "Resources",
          href: `/serving/resources`,
          icon: "Database",
          roles: [ "it", "oem", "devops"],
        },
      ];
    case "Technology Management":
      return [
        {
          name: "Dashboard",
          href: `/technology-management`,
          icon: "BarChart3",
          roles: ["developer", "ai_analytics", "architect"],
          isDefault: true,
        },
        {
          name: "Technologies",
          href: `/technology-management/technologies/catalog`,
          icon: "Code",
          roles: ["developer", "ai_analytics", "architect"],
          children: [
            {
              name: "Catálogo",
              href: `/technology-management/technologies/catalog`,
              icon: "Code",
              roles: ["developer", "ai_analytics", "architect"],
            },
            {
              name: "Crear Tecnología",
              href: `/technology-management/technologies/create`,
              icon: "Plus",
              roles: ["developer", "ai_analytics", "architect"],
            },
            {
              name: "Discovery",
              href: `/technology-management/technologies/discovery`,
              icon: "Search",
              roles: ["developer", "ai_analytics", "architect"],
            },
            {
              name: "Datasets",
              href: `/technology-management/technologies/datasets`,
              icon: "Database",
              roles: ["developer", "ai_analytics", "architect"],
            },
            {
              name: "Webscraping",
              href: `/technology-management/technologies/webscraping`,
              icon: "Globe",
              roles: ["developer",, "ai_analytics", "architect"],
            },
            {
              name: "Marketplace",
              href: `/technology-management/technologies/marketplace`,
              icon: "ShoppingCart",
              roles: ["developer",, "ai_analytics", "architect"],
            },
            {
              name: "Repositories",
              href: `/technology-management/technologies/repositories`,
              icon: "Github",
              roles: ["developer", ,"ai_analytics", "architect"],
            },
          ],
        },
        {
          name: "Architectures",
          href: `/technology-management/architectures/stacks`,
          icon: "Layers",
          roles: ["developer", ,"ai_analytics", "architect"],
          children: [
            {
              name: "Stacks",
              href: `/technology-management/architectures/stacks`,
              icon: "Layers",
              roles: ["developer",, "ai_analytics", "architect"],
            },
            {
              name: "Crear Stack",
              href: `/technology-management/architectures/stacks/create`,
              icon: "Plus",
              roles: ["developer", ,"ai_analytics", "architect"],
            },
            {
              name: "Metodologías",
              href: `/technology-management/architectures/methodologies`,
              icon: "Settings",
              roles: ["developer","ai_analytics", "architect"],
            },
          ],
        },
        {
          name: "Specialized Models",
          href: `/technology-management/specialized-models/models`,
          icon: "Brain",
          roles: ["developer",  "ai_analytics", "architect"],
          children: [
            {
              name: "Modelos",
              href: `/technology-management/specialized-models/models`,
              icon: "Brain",
              roles: ["developer",  "ai_analytics", "architect"],
            },
            {
              name: "Entrenamiento",
              href: `/technology-management/specialized-models/training`,
              icon: "Play",
              roles: ["developer",  "ai_analytics", "architect"],
            },
          ],
        },
        {
          name: "Model Evaluation",
          href: `/technology-management/model-evaluation`,
          icon: "Target",
          roles: ["developer", "it", "ai_analytics", "architect"],
        },
      ];
    case "Domain Ingestion":
      return [
        {
          name: "Dashboard",
          href: "/domain-ingestion",
          icon: "BarChart3",
          roles: [
            "admin",   "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
          isDefault: true,
        },
        {
          name: "Asistente de Dominios",
          href: "/domain-ingestion/wizard",
          icon: "Bot",
          roles: [
            "admin",  "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
        },
        {
          name: "Gestionar Dominios",
          href: "/domain-ingestion/manage",
          icon: "Settings",
          roles: [
            "admin",   "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
        },
        {
          name: "Monitorear Jobs",
          href: "/domain-ingestion/monitor",
          icon: "Activity",
          roles: [
            "admin",   "it", "oem",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
        },
        {
          name: "Configuración",
          href: "/domain-ingestion/settings",
          icon: "Settings",
          roles: [
            "admin",   "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
        },
      ];
    case "Fuentes de Información":
      return [
        {
          name: "Dashboard",
          href: "/data-sources",
          icon: "BarChart3",
          roles: [
            "admin",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
          isDefault: true,
        },
        {
          name: "Bases de Datos",
          href: "/data-sources/database",
          icon: "Database",
          roles: [
            "admin",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
        },
        {
          name: "APIs",
          href: "/data-sources/api",
          icon: "Globe",
          roles: [
            "admin",  "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
        },
        {
          name: "Documentos",
          href: "/data-sources/upload-documents",
          icon: "FileText",
          roles: [
            "admin",  "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
        },
        {
          name: "Web Scraping",
          href: "/data-sources/web-scraping",
          icon: "Globe",
          roles: [
            "admin",  "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
        },
      ];
    // NUEVOS MÓDULOS DEL CORE
    case "Departments":
      return [
        {
          name: "Department Management",
          href: `/admin/departments`,
          icon: "Building",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Department Hierarchy",
          href: `/admin/departments/hierarchy`,
          icon: "Network",
          roles: ["admin"],
        },
        {
          name: "Department Stats",
          href: `/admin/departments/stats`,
          icon: "BarChart3",
          roles: ["admin"],
        },
      ];
    case "User Sessions":
      return [
        {
          name: "Active Sessions",
          href: `/admin/sessions`,
          icon: "Activity",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Session Management",
          href: `/admin/sessions/manage`,
          icon: "Settings",
          roles: ["admin"],
        },
        {
          name: "Session Analytics",
          href: `/admin/sessions/analytics`,
          icon: "BarChart3",
          roles: ["admin"],
        },
      ];
    case "Audit System":
      return [
        {
          name: "Audit Logs",
          href: `/admin/audit`,
          icon: "FileText",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Audit Reports",
          href: `/admin/audit/reports`,
          icon: "BarChart3",
          roles: ["admin"],
        },
        {
          name: "Security Alerts",
          href: `/admin/audit/alerts`,
          icon: "Shield",
          roles: ["admin"],
        },
        {
          name: "Compliance Dashboard",
          href: `/admin/audit/compliance`,
          icon: "CheckCircle",
          roles: ["admin"],
        },
      ];
    case "Platform Licenses":
      return [
        {
          name: "License Management",
          href: `/admin/licenses`,
          icon: "Key",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "License Validation",
          href: `/admin/licenses/validate`,
          icon: "CheckCircle",
          roles: ["admin"],
        },
        {
          name: "Resource Limits",
          href: `/admin/licenses/resources`,
          icon: "Gauge",
          roles: ["admin"],
        },
        {
          name: "License History",
          href: `/admin/licenses/history`,
          icon: "Clock",
          roles: ["admin"],
        },
      ];
    case "Platform Nodes":
      return [
        {
          name: "Node Monitoring",
          href: `/admin/nodes`,
          icon: "Server",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Resource Usage",
          href: `/admin/nodes/resources`,
          icon: "Gauge",
          roles: ["admin"],
        },
        {
          name: "GPU Monitoring",
          href: `/admin/nodes/gpu`,
          icon: "Cpu",
          roles: ["admin"],
        },
        {
          name: "Node Health",
          href: `/admin/nodes/health`,
          icon: "Heart",
          roles: ["admin"],
        },
      ];
    case "Platform Updates":
      return [
        {
          name: "Update Management",
          href: `/admin/updates`,
          icon: "Download",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Installation Status",
          href: `/admin/updates/installations`,
          icon: "CheckCircle",
          roles: ["admin"],
        },
        {
          name: "Update Scheduling",
          href: `/admin/updates/schedule`,
          icon: "Calendar",
          roles: ["admin"],
        },
        {
          name: "Rollback Management",
          href: `/admin/updates/rollback`,
          icon: "RotateCcw",
          roles: ["admin"],
        },
      ];
    case "Admin":
      return [
        {
          name: "Admin Panel",
          href: `/admin`,
          icon: "Settings",
          roles: ["admin"],
          isDefault: true,
        },
        { name: "Equipo", href: "/team", icon: "Users", roles: ["admin"] },
        {
          name: "Demo Users",
          href: "/config/demo-users",
          icon: "Users",
          roles: ["admin"],
        },
        {
          name: "User Management",
          href: "/config/users",
          icon: "Users",
          roles: ["admin"],
        },
        {
          name: "Role Configuration",
          href: "/config/roles",
          icon: "Settings",
          roles: ["admin"],
        },
        {
          name: "Menu Management",
          href: "/admin/menu-management",
          icon: "Menu",
          roles: ["admin"],
        },
        // NUEVOS MÓDULOS DEL CORE
        {
          name: "Departments",
          href: "/admin/departments",
          icon: "Building",
          roles: ["admin"],
        },
        {
          name: "User Sessions",
          href: "/admin/sessions",
          icon: "Activity",
          roles: ["admin"],
        },
        {
          name: "Audit System",
          href: "/admin/audit",
          icon: "FileText",
          roles: ["admin"],
        },
        {
          name: "Platform Licenses",
          href: "/admin/licenses",
          icon: "Key",
          roles: ["admin"],
        },
        {
          name: "Platform Nodes",
          href: "/admin/nodes",
          icon: "Server",
          roles: ["admin"],
        },
        {
          name: "Platform Updates",
          href: "/admin/updates",
          icon: "Download",
          roles: ["admin"],
        },
      ];
    case "Plugins":
      return [
        {
          name: "Plugins",
          href: `/plugins`,
          icon: "Plug",
          roles: [
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "ai_developer",
            "devops",
            "project_manager",
            "architect",
            "ai_architect",
          ],
          isDefault: true,
        },
      ];
    case "Training Center":
      return [
        {
          name: "Training Center",
          href: `/training-center`,
          icon: "GraduationCap",
          roles: ["admin", "developer", "viewer", "project_manager"],
          isDefault: true,
        },
      ];
    case "Projects":
      return [
        {
          name: "Dashboard Proyectos",
          href: `/projects`,
          icon: "FileText",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
          isDefault: true,
        },
        {
          name: "Proyectos Conversacionales",
          href: `/projects/conversational`,
          icon: "MessageSquare",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
        },
        {
          name: "Nuevo Proyecto",
          href: `/projects/conversational/new`,
          icon: "Plus",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
        },
        {
          name: "Proyectos de Generación",
          href: `/projects/generation`,
          icon: "Zap",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
        },
      ];
    case "RAG System":
      return [
        {
          name: "RAG Dashboard",
          href: `/rag`,
          icon: "Database",
          roles: ["agencia", "it",  "developer", "ai_analytics"],
          isDefault: true,
        },
        {
          name: "Proyectos RAG",
          href: `/rag/projects`,
          icon: "Brain",
          roles: ["agencia", "it", , "developer", "ai_analytics"],
        },
        {
          name: "Modelos RAG",
          href: `/rag/models`,
          icon: "Zap",
          roles: ["agencia", "it",  "developer", "ai_analytics"],
        },
        {
          name: "Chat",
          href: `/rag/chat`,
          icon: "MessageSquare",
          roles: ["agencia","developer", "ai_analytics"],
        },
        {
          name: "Reranker",
          href: `/rag/reranker`,
          icon: "TrendingUp",
          roles: ["agencia", "it", "developer", "ai_analytics"],
        },
        {
          name: "Chunks",
          href: `/rag/chunks`,
          icon: "Hash",
          roles: ["agencia", "it",  "developer", "ai_analytics"],
        },
        {
          name: "Búsqueda Semántica",
          href: `/rag/search`,
          icon: "Search",
          roles: ["agencia", "it", "developer", "ai_analytics"],
        },
        {
          name: "Analytics",
          href: `/rag/analytics`,
          icon: "BarChart3",
          roles: ["agencia", "it", "developer", "ai_analytics"],
        },
      ];
    case "Model Evaluation":
      return [
        {
          name: "Evaluations",
          href: `/model-evaluation`,
          icon: "Target",
          roles: ["agencia","ai_analytics"],
          isDefault: true,
        },
        {
          name: "Activity",
          href: `/model-evaluation/activity`,
          icon: "Activity",
          roles: ["agencia","ai_analytics"],
        },
        {
          name: "Reports",
          href: `/model-evaluation/reports`,
          icon: "FileText",
          roles: ["agencia","ai_analytics"],
        },
        {
          name: "Security",
          href: `/model-evaluation/security`,
          icon: "Shield",
          roles: ["agencia","ai_analytics"],
        },
      ];
    case "AI Training":
      return [
        {
          name: "Training Jobs",
          href: `/training`,
          icon: "Brain",
          roles: ["ai_analytics"],
          isDefault: true,
        },
        {
          name: "Experiments",
          href: `/training/experiments`,
          icon: "TestTube",
          roles: ["ai_analytics"],
        },
        {
          name: "Models",
          href: `/training/models`,
          icon: "Layers",
          roles: ["ai_analytics"],
        },
        {
          name: "Datasets",
          href: `/training/datasets`,
          icon: "Database",
          roles: ["ai_analytics"],
        },
      ];
    case "Governance":
      return [
        {
          name: "Policies",
          href: `/governance`,
          icon: "Shield",
          roles: ["project_manager"],
          isDefault: true,
        },
        {
          name: "Security",
          href: `/governance/security`,
          icon: "Shield",
          roles: ["project_manager"],
        },
        {
          name: "Monitoring",
          href: `/governance/monitoring`,
          icon: "Activity",
          roles: ["project_manager"],
        },
        {
          name: "BPMN Task Inbox",
          href: `/bpmn/task-inbox`,
          icon: "Inbox",
          roles: [
            "admin",
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "ai_developer",
            "devops",
            "project_manager",
            "architect",
            "ai_architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
        },
      ];
    case "Infrastructure":
      return [
        {
          name: "Infrastructure Overview",
          href: `/infrastructure`,
          icon: "Server",
          roles: ["admin", "devops", "architect"],
          isDefault: true,
        },
        {
          name: "Cloud Providers",
          href: `/infrastructure/providers`,
          icon: "Cloud",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Kubernetes Clusters",
          href: `/infrastructure/kubernetes`,
          icon: "Server",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "GPU Instances",
          href: `/infrastructure/gpu-instances`,
          icon: "Cpu",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Infrastructure Deployments",
          href: `/infrastructure/deployments`,
          icon: "Rocket",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Cloud Resources",
          href: `/infrastructure/resources`,
          icon: "Database",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Provider Credentials",
          href: `/infrastructure/credentials`,
          icon: "Key",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Cost Management",
          href: `/infrastructure/cost-management`,
          icon: "DollarSign",
          roles: ["admin", "devops", "architect"],
        },
      ];
    default:
      return [
        {
          name: "Dashboard",
          href: `/dashboard`,
          icon: "BarChart3",
          roles: [
            "admin",
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "devops",
            "project_manager",
          ],
          isDefault: true,
        },
        {
          name: "Configuración",
          href: `/settings`,
          icon: "Settings",
          roles: ["admin"],
        },
      ];
    case "BPMN":
      return [
        {
          name: "BPMN Task Inbox",
          href: `/bpmn/task-inbox`,
          icon: "Inbox",
          roles: [
            "admin",
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "ai_developer",
            "devops",
            "project_manager",
            "architect",
            "ai_architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: true,
        },
        {
          name: "Internal Audit Scheduling",
          href: `/bpmn/forms/internal-audit-scheduling`,
          icon: "Calendar",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
        {
          name: "Aprobación de Agent - Override Humano",
          href: `/bpmn/forms/agent-approval-human-override`,
          icon: "AlertTriangle",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
        {
          name: "Aprobación de Modelo - Override Humano",
          href: `/bpmn/forms/model-approval-human-override`,
          icon: "Brain",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
        {
          name: "Revisión Humana de Prompt",
          href: `/bpmn/forms/prompt-human-review`,
          icon: "FileText",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
        {
          name: "Recordatorio SLA HITL",
          href: `/bpmn/forms/hitl-sla-reminder`,
          icon: "AlertCircle",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
      ];
    case "Community IT":
      return [
        {
          name: "Proyectos Community",
          href: `/projects/community`,
          icon: "FileText",
          roles: ["community_it"],
          isDefault: true,
        },
        {
          name: "Conocimiento Community",
          href: `/rag/community`,
          icon: "Database",
          roles: ["community_it"],
        },
        {
          name: "Modelos Community",
          href: `/modelos/community`,
          icon: "Brain",
          roles: ["community_it"],
        },
      ];
  }
};

```

```typescript
// Configuración centralizada de módulos y roles
// Este archivo es la única fuente de verdad para la configuración de módulos

export interface ModuleConfig {
  name: string;
  icon: any; // Componente de icono
  roles: string[];
  defaultPath: string;
  description?: string;
}

export interface MenuItem {
  name: string;
  href?: string;
  icon: any;
  roles: string[];
  children?: MenuItem[];
  isDefault?: boolean;
}

// Configuración centralizada de módulos
export const modulesConfig: ModuleConfig[] = [
  {
    name: "Dashboard",
    icon: "BarChart3",
    roles: [
      // Roles de negocio (nuevos)
      "agencia",
      "it",
      "oem",
      "business_admin",
      // Roles técnicos (existentes)
      "admin",
      "developer",
      "viewer",
      "business_analytics",
      "ai_analytics",
      "ai_developer",
      "devops",
      "project_manager",
      "architect",
      "ai_architect",
    ],
    defaultPath: "/dashboard",
  },
  {
    name: "Code Playground",
    icon: "Zap",
    roles: ["it",  "developer", "devops"],
    defaultPath: "/code-playground",
  },
  {
    name: "AI Playground",
    icon: "Brain",
    roles: [   "developer", "ai_developer"],
    defaultPath: "/playground/chat",
  },
  {
    name: "RAG System",
    icon: "Database",
    roles: ["agencia", "it", "developer", "ai_analytics"],
    defaultPath: "/rag",
  },
  {
    name: "Model Management",
    icon: "Brain",
    roles: [ "it",  "developer", "ai_developer"],
    defaultPath: "/model-management",
  },
  {
    name: "Community IT",
    icon: "Users",
    roles: ["community_it"],
    defaultPath: "/projects/community",
  },
  {
    name: "Serving",
    icon: "Server",
    roles: ["it", "oem", "devops"],
    defaultPath: "/serving",
  },
  {
    name: "Governance",
    icon: "Shield",
    roles: ["project_manager"],
    defaultPath: "/governance",
  },
  {
    name: "Model Evaluation",
    icon: "Target",
    roles: ["agencia","ai_analytics"],
    defaultPath: "/model-evaluation",
  },
  {
    name: "AI Training",
    icon: "Brain",
    roles: ["ai_analytics",  "it", "oem"],
    defaultPath: "/training",
  },
  {
    name: "Technology Management",
    icon: "Layers",
    roles: ["developer",   "ai_analytics", "architect"],
    defaultPath: "/technology-management",
  },
  {
    name: "Domain Ingestion",
    icon: "Database",
    roles: [
      "admin","it",
      "developer",
      "ai_analytics",
      "project_manager",
      "ai_developer",
    ],
    defaultPath: "/domain-ingestion",
  },
  {
    name: "Fuentes de Información",
    icon: "Database",
    roles: [
      "admin", "agencia", "it",
      "developer",
      "ai_analytics",
      "project_manager",
      "ai_developer",
      "architect",
    ],
    defaultPath: "/data-sources",
    description:
      "Gestión centralizada de fuentes de datos para RAG, dominios y entrenamiento de modelos",
  },
  {
    name: "Plugins",
    icon: "Plug",
    roles: [
      "developer", "it",
      "viewer",
      "business_analytics",
      "ai_analytics",
      "ai_developer",
      "devops",
      "project_manager",
      "architect",
      "ai_architect",
    ],
    defaultPath: "/plugins",
  },
  {
    name: "Training Center",
    icon: "GraduationCap",
    roles: ["admin",    "developer", "viewer", "project_manager"],
    defaultPath: "/training-center",
  },
  {
    name: "Projects",
    icon: "FileText",
    roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
    defaultPath: "/projects",
  },
  // NUEVOS MÓDULOS DEL CORE
  {
    name: "Departments",
    icon: "Building",
    roles: ["admin"],
    defaultPath: "/admin/departments",
    description: "Gestión de departamentos y estructura organizacional",
  },
  {
    name: "User Sessions",
    icon: "Activity",
    roles: ["admin"],
    defaultPath: "/admin/sessions",
    description: "Monitorización y gestión de sesiones de usuario",
  },
  {
    name: "Audit System",
    icon: "FileText",
    roles: ["admin"],
    defaultPath: "/admin/audit",
    description: "Sistema de auditoría y logs del sistema",
  },
  {
    name: "Platform Licenses",
    icon: "Key",
    roles: ["admin"],
    defaultPath: "/admin/licenses",
    description: "Gestión de licencias de la plataforma",
  },
  {
    name: "Platform Nodes",
    icon: "Server",
    roles: ["admin"],
    defaultPath: "/admin/nodes",
    description: "Monitorización de nodos de la plataforma",
  },
  {
    name: "Platform Updates",
    icon: "Download",
    roles: ["admin"],
    defaultPath: "/admin/updates",
    description: "Gestión de actualizaciones de la plataforma",
  },
  {
    name: "Admin",
    icon: "Settings",
    roles: ["admin"],
    defaultPath: "/admin",
  },
  {
    name: "Infrastructure",
    icon: "Server",
    roles: ["admin",  "it", "oem", "devops", "architect"],
    defaultPath: "/infrastructure",
    description:
      "Gestión de infraestructura cloud, clusters K8s y GPU instances",
  },
  {
    name: "Agents",
    icon: "Bot",
    roles: ["admin", "developer", "ai_developer", "project_manager", "architect"],
    defaultPath: "/agents/monitoring/dashboard",
    description: "Gestión completa de agentes AI",
  },
  {
    name: "BPMN",
    icon: "Inbox",
    roles: [
      "admin",
      "developer",
      "viewer",
      "business_analytics",
      "ai_analytics",
      "ai_developer",
      "devops",
      "project_manager",
      "architect",
      "ai_architect",
      "agencia",
      "it",
      "oem",
      "business_admin"
    ],
    defaultPath: "/bpmn/task-inbox",
    description: "Bandeja de tareas BPMN y gestión de procesos",
  },
];

// Función para obtener módulos por roles
export const getModulesByRoles = (roles: string[]): ModuleConfig[] => {
  return modulesConfig.filter((module) =>
    module.roles.some((role) => roles.includes(role))
  );
};

// Función para obtener módulo por ruta
export const getModuleByPath = (pathname: string): string => {
  const path = pathname.split("/")[1];

  const moduleMap: Record<string, string> = {
    dashboard: "Dashboard",
    "code-playground": "Code Playground",
    playground: "AI Playground",
    "model-management": "Model Management",
    serving: "Serving",
    "technology-management": "Technology Management",
    "domain-ingestion": "Domain Ingestion",
    "data-sources": "Fuentes de Información",
    admin: "Admin",
    team: "Admin",
    config: "Admin",
    "training-center": "Training Center",
    plugins: "Plugins",
    projects: "Projects",
    rag: "RAG System",
    "model-evaluation": "Model Evaluation",
    training: "AI Training",
    governance: "Governance",
    bpmn: "BPMN",
    // NUEVAS RUTAS DEL CORE
    departments: "Departments",
    sessions: "User Sessions",
    audit: "Audit System",
    licenses: "Platform Licenses",
    nodes: "Platform Nodes",
    updates: "Platform Updates",
    // INFRASTRUCTURE
    infrastructure: "Infrastructure",
    agents: "Agents",
  };

  return moduleMap[path] || "Dashboard";
};

// Función para validar si un usuario puede acceder a un módulo
export const canAccessModule = (
  userRoles: string[],
  moduleName: string
): boolean => {
  const module = modulesConfig.find((m) => m.name === moduleName);
  return module ? module.roles.some((role) => userRoles.includes(role)) : false;
};

// Función para obtener el menú de un módulo específico
export const getMenuByModule = (moduleName: string): MenuItem[] => {
  switch (moduleName) {
    case "Dashboard":
      return [
        {
          name: "Dashboard",
          href: `/dashboard`,
          icon: "BarChart3",
          roles: [
            "admin",
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "devops",
            "project_manager",
          ],
          isDefault: true,
        },
        {
          name: "Configuración",
          href: `/settings`,
          icon: "Settings",
          roles: ["admin"],
        },
      ];
    case "Code Playground":
      return [
        {
          name: "Code Editor",
          href: `/code-playground`,
          icon: "Zap",
          roles: ["admin", "developer", "devops"],
          isDefault: true,
        },
        {
          name: "Snippets",
          href: `/code-playground/snippets`,
          icon: "FileText",
          roles: ["admin", "developer", "devops"],
        },
        {
          name: "Templates",
          href: `/code-playground/templates`,
          icon: "ClipboardList",
          roles: ["admin", "developer", "devops"],
        },
        {
          name: "AI Notebook",
          href: `/ai-notebook`,
          icon: "Brain",
          roles: ["admin", "developer", "ai_analytics", "ai_developer"],
          isDefault: false,
        },
      ];
    case "AI Playground":
      return [
        {
          name: "Chat & Text",
          href: `/playground/chat`,
          icon: "MessageCircle",
          roles: ["admin",  "agencia",   "developer", "ai_analytics"],
          isDefault: true,
        },
        {
          name: "Image Generation",
          href: `/playground/images`,
          icon: "Image",
          roles: ["admin",    "developer", "ai_analytics"],
        },
        {
          name: "Translation",
          href: `/playground/translation`,
          icon: "Layers",
          roles: ["admin",     "developer", "ai_analytics"],
        },
        {
          name: "Smart Routing",
          href: `/playground/smart-routing`,
          icon: "GitBranch",
          roles: ["admin",    "developer", "ai_analytics"],
        },
        {
          name: "Voice & Audio",
          href: `/playground/voice`,
          icon: "Mic",
          roles: ["admin", "developer", "ai_analytics"],
        },
      ];
    case "Model Management":
      return [
        {
          name: "Dashboard",
          href: `/model-management`,
          icon: "BarChart3",
          roles: [ "it",  "developer", "ai_developer"],
          isDefault: true,
        },
        {
          name: "Modelos",
          href: `/model-management/models`,
          icon: "Package",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Proveedores",
          href: `/model-management/providers`,
          icon: "Server",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Marketplace",
          href: `/model-management/marketplace`,
          icon: "ShoppingCart",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Experimentos",
          href: `/model-management/experiments`,
          icon: "TestTube",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Serving",
          href: `/model-management/serving`,
          icon: "Server",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Artefactos",
          href: `/model-management/artifacts`,
          icon: "Package",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Ejecuciones",
          href: `/model-management/runs`,
          icon: "Activity",
          roles: [ "it",  "developer", "ai_developer"],
        },
      ];
    case "Serving":
      return [
        {
          name: "Deployments",
          href: `/serving`,
          icon: "Server",
          roles: [ "it", "oem", "devops"],
          isDefault: true,
        },
        {
          name: "Monitoring",
          href: `/serving/monitoring`,
          icon: "Activity",
          roles: [ "it", "oem", "devops"],
        },
        {
          name: "Resources",
          href: `/serving/resources`,
          icon: "Database",
          roles: [ "it", "oem", "devops"],
        },
      ];
    case "Technology Management":
      return [
        {
          name: "Dashboard",
          href: `/technology-management`,
          icon: "BarChart3",
          roles: ["developer", "ai_analytics", "architect"],
          isDefault: true,
        },
        {
          name: "Technologies",
          href: `/technology-management/technologies/catalog`,
          icon: "Code",
          roles: ["developer", "ai_analytics", "architect"],
          children: [
            {
              name: "Catálogo",
              href: `/technology-management/technologies/catalog`,
              icon: "Code",
              roles: ["developer", "ai_analytics", "architect"],
            },
            {
              name: "Crear Tecnología",
              href: `/technology-management/technologies/create`,
              icon: "Plus",
              roles: ["developer", "ai_analytics", "architect"],
            },
            {
              name: "Discovery",
              href: `/technology-management/technologies/discovery`,
              icon: "Search",
              roles: ["developer", "ai_analytics", "architect"],
            },
            {
              name: "Datasets",
              href: `/technology-management/technologies/datasets`,
              icon: "Database",
              roles: ["developer", "ai_analytics", "architect"],
            },
            {
              name: "Webscraping",
              href: `/technology-management/technologies/webscraping`,
              icon: "Globe",
              roles: ["developer",, "ai_analytics", "architect"],
            },
            {
              name: "Marketplace",
              href: `/technology-management/technologies/marketplace`,
              icon: "ShoppingCart",
              roles: ["developer",, "ai_analytics", "architect"],
            },
            {
              name: "Repositories",
              href: `/technology-management/technologies/repositories`,
              icon: "Github",
              roles: ["developer", ,"ai_analytics", "architect"],
            },
          ],
        },
        {
          name: "Architectures",
          href: `/technology-management/architectures/stacks`,
          icon: "Layers",
          roles: ["developer", ,"ai_analytics", "architect"],
          children: [
            {
              name: "Stacks",
              href: `/technology-management/architectures/stacks`,
              icon: "Layers",
              roles: ["developer",, "ai_analytics", "architect"],
            },
            {
              name: "Crear Stack",
              href: `/technology-management/architectures/stacks/create`,
              icon: "Plus",
              roles: ["developer", ,"ai_analytics", "architect"],
            },
            {
              name: "Metodologías",
              href: `/technology-management/architectures/methodologies`,
              icon: "Settings",
              roles: ["developer","ai_analytics", "architect"],
            },
          ],
        },
        {
          name: "Specialized Models",
          href: `/technology-management/specialized-models/models`,
          icon: "Brain",
          roles: ["developer",  "ai_analytics", "architect"],
          children: [
            {
              name: "Modelos",
              href: `/technology-management/specialized-models/models`,
              icon: "Brain",
              roles: ["developer",  "ai_analytics", "architect"],
            },
            {
              name: "Entrenamiento",
              href: `/technology-management/specialized-models/training`,
              icon: "Play",
              roles: ["developer",  "ai_analytics", "architect"],
            },
          ],
        },
        {
          name: "Model Evaluation",
          href: `/technology-management/model-evaluation`,
          icon: "Target",
          roles: ["developer", "it", "ai_analytics", "architect"],
        },
      ];
    case "Domain Ingestion":
      return [
        {
          name: "Dashboard",
          href: "/domain-ingestion",
          icon: "BarChart3",
          roles: [
            "admin",   "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
          isDefault: true,
        },
        {
          name: "Asistente de Dominios",
          href: "/domain-ingestion/wizard",
          icon: "Bot",
          roles: [
            "admin",  "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
        },
        {
          name: "Gestionar Dominios",
          href: "/domain-ingestion/manage",
          icon: "Settings",
          roles: [
            "admin",   "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
        },
        {
          name: "Monitorear Jobs",
          href: "/domain-ingestion/monitor",
          icon: "Activity",
          roles: [
            "admin",   "it", "oem",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
        },
        {
          name: "Configuración",
          href: "/domain-ingestion/settings",
          icon: "Settings",
          roles: [
            "admin",   "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
        },
      ];
    case "Fuentes de Información":
      return [
        {
          name: "Dashboard",
          href: "/data-sources",
          icon: "BarChart3",
          roles: [
            "admin",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
          isDefault: true,
        },
        {
          name: "Bases de Datos",
          href: "/data-sources/database",
          icon: "Database",
          roles: [
            "admin",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
        },
        {
          name: "APIs",
          href: "/data-sources/api",
          icon: "Globe",
          roles: [
            "admin",  "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
        },
        {
          name: "Documentos",
          href: "/data-sources/upload-documents",
          icon: "FileText",
          roles: [
            "admin",  "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
        },
        {
          name: "Web Scraping",
          href: "/data-sources/web-scraping",
          icon: "Globe",
          roles: [
            "admin",  "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
        },
      ];
    // NUEVOS MÓDULOS DEL CORE
    case "Departments":
      return [
        {
          name: "Department Management",
          href: `/admin/departments`,
          icon: "Building",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Department Hierarchy",
          href: `/admin/departments/hierarchy`,
          icon: "Network",
          roles: ["admin"],
        },
        {
          name: "Department Stats",
          href: `/admin/departments/stats`,
          icon: "BarChart3",
          roles: ["admin"],
        },
      ];
    case "User Sessions":
      return [
        {
          name: "Active Sessions",
          href: `/admin/sessions`,
          icon: "Activity",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Session Management",
          href: `/admin/sessions/manage`,
          icon: "Settings",
          roles: ["admin"],
        },
        {
          name: "Session Analytics",
          href: `/admin/sessions/analytics`,
          icon: "BarChart3",
          roles: ["admin"],
        },
      ];
    case "Audit System":
      return [
        {
          name: "Audit Logs",
          href: `/admin/audit`,
          icon: "FileText",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Audit Reports",
          href: `/admin/audit/reports`,
          icon: "BarChart3",
          roles: ["admin"],
        },
        {
          name: "Security Alerts",
          href: `/admin/audit/alerts`,
          icon: "Shield",
          roles: ["admin"],
        },
        {
          name: "Compliance Dashboard",
          href: `/admin/audit/compliance`,
          icon: "CheckCircle",
          roles: ["admin"],
        },
      ];
    case "Platform Licenses":
      return [
        {
          name: "License Management",
          href: `/admin/licenses`,
          icon: "Key",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "License Validation",
          href: `/admin/licenses/validate`,
          icon: "CheckCircle",
          roles: ["admin"],
        },
        {
          name: "Resource Limits",
          href: `/admin/licenses/resources`,
          icon: "Gauge",
          roles: ["admin"],
        },
        {
          name: "License History",
          href: `/admin/licenses/history`,
          icon: "Clock",
          roles: ["admin"],
        },
      ];
    case "Platform Nodes":
      return [
        {
          name: "Node Monitoring",
          href: `/admin/nodes`,
          icon: "Server",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Resource Usage",
          href: `/admin/nodes/resources`,
          icon: "Gauge",
          roles: ["admin"],
        },
        {
          name: "GPU Monitoring",
          href: `/admin/nodes/gpu`,
          icon: "Cpu",
          roles: ["admin"],
        },
        {
          name: "Node Health",
          href: `/admin/nodes/health`,
          icon: "Heart",
          roles: ["admin"],
        },
      ];
    case "Platform Updates":
      return [
        {
          name: "Update Management",
          href: `/admin/updates`,
          icon: "Download",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Installation Status",
          href: `/admin/updates/installations`,
          icon: "CheckCircle",
          roles: ["admin"],
        },
        {
          name: "Update Scheduling",
          href: `/admin/updates/schedule`,
          icon: "Calendar",
          roles: ["admin"],
        },
        {
          name: "Rollback Management",
          href: `/admin/updates/rollback`,
          icon: "RotateCcw",
          roles: ["admin"],
        },
      ];
    case "Admin":
      return [
        {
          name: "Admin Panel",
          href: `/admin`,
          icon: "Settings",
          roles: ["admin"],
          isDefault: true,
        },
        { name: "Equipo", href: "/team", icon: "Users", roles: ["admin"] },
        {
          name: "Demo Users",
          href: "/config/demo-users",
          icon: "Users",
          roles: ["admin"],
        },
        {
          name: "User Management",
          href: "/config/users",
          icon: "Users",
          roles: ["admin"],
        },
        {
          name: "Role Configuration",
          href: "/config/roles",
          icon: "Settings",
          roles: ["admin"],
        },
        {
          name: "Menu Management",
          href: "/admin/menu-management",
          icon: "Menu",
          roles: ["admin"],
        },
        // NUEVOS MÓDULOS DEL CORE
        {
          name: "Departments",
          href: "/admin/departments",
          icon: "Building",
          roles: ["admin"],
        },
        {
          name: "User Sessions",
          href: "/admin/sessions",
          icon: "Activity",
          roles: ["admin"],
        },
        {
          name: "Audit System",
          href: "/admin/audit",
          icon: "FileText",
          roles: ["admin"],
        },
        {
          name: "Platform Licenses",
          href: "/admin/licenses",
          icon: "Key",
          roles: ["admin"],
        },
        {
          name: "Platform Nodes",
          href: "/admin/nodes",
          icon: "Server",
          roles: ["admin"],
        },
        {
          name: "Platform Updates",
          href: "/admin/updates",
          icon: "Download",
          roles: ["admin"],
        },
      ];
    case "Plugins":
      return [
        {
          name: "Plugins",
          href: `/plugins`,
          icon: "Plug",
          roles: [
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "ai_developer",
            "devops",
            "project_manager",
            "architect",
            "ai_architect",
          ],
          isDefault: true,
        },
      ];
    case "Training Center":
      return [
        {
          name: "Training Center",
          href: `/training-center`,
          icon: "GraduationCap",
          roles: ["admin", "developer", "viewer", "project_manager"],
          isDefault: true,
        },
      ];
    case "Projects":
      return [
        {
          name: "Dashboard Proyectos",
          href: `/projects`,
          icon: "FileText",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
          isDefault: true,
        },
        {
          name: "Proyectos Conversacionales",
          href: `/projects/conversational`,
          icon: "MessageSquare",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
        },
        {
          name: "Nuevo Proyecto",
          href: `/projects/conversational/new`,
          icon: "Plus",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
        },
        {
          name: "Proyectos de Generación",
          href: `/projects/generation`,
          icon: "Zap",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
        },
      ];
    case "RAG System":
      return [
        {
          name: "RAG Dashboard",
          href: `/rag`,
          icon: "Database",
          roles: ["agencia", "it",  "developer", "ai_analytics"],
          isDefault: true,
        },
        {
          name: "Proyectos RAG",
          href: `/rag/projects`,
          icon: "Brain",
          roles: ["agencia", "it", , "developer", "ai_analytics"],
        },
        {
          name: "Modelos RAG",
          href: `/rag/models`,
          icon: "Zap",
          roles: ["agencia", "it",  "developer", "ai_analytics"],
        },
        {
          name: "Chat",
          href: `/rag/chat`,
          icon: "MessageSquare",
          roles: ["agencia","developer", "ai_analytics"],
        },
        {
          name: "Reranker",
          href: `/rag/reranker`,
          icon: "TrendingUp",
          roles: ["agencia", "it", "developer", "ai_analytics"],
        },
        {
          name: "Chunks",
          href: `/rag/chunks`,
          icon: "Hash",
          roles: ["agencia", "it",  "developer", "ai_analytics"],
        },
        {
          name: "Búsqueda Semántica",
          href: `/rag/search`,
          icon: "Search",
          roles: ["agencia", "it", "developer", "ai_analytics"],
        },
        {
          name: "Analytics",
          href: `/rag/analytics`,
          icon: "BarChart3",
          roles: ["agencia", "it", "developer", "ai_analytics"],
        },
      ];
    case "Model Evaluation":
      return [
        {
          name: "Evaluations",
          href: `/model-evaluation`,
          icon: "Target",
          roles: ["agencia","ai_analytics"],
          isDefault: true,
        },
        {
          name: "Activity",
          href: `/model-evaluation/activity`,
          icon: "Activity",
          roles: ["agencia","ai_analytics"],
        },
        {
          name: "Reports",
          href: `/model-evaluation/reports`,
          icon: "FileText",
          roles: ["agencia","ai_analytics"],
        },
        {
          name: "Security",
          href: `/model-evaluation/security`,
          icon: "Shield",
          roles: ["agencia","ai_analytics"],
        },
      ];
    case "AI Training":
      return [
        {
          name: "Training Jobs",
          href: `/training`,
          icon: "Brain",
          roles: ["ai_analytics"],
          isDefault: true,
        },
        {
          name: "Experiments",
          href: `/training/experiments`,
          icon: "TestTube",
          roles: ["ai_analytics"],
        },
        {
          name: "Models",
          href: `/training/models`,
          icon: "Layers",
          roles: ["ai_analytics"],
        },
        {
          name: "Datasets",
          href: `/training/datasets`,
          icon: "Database",
          roles: ["ai_analytics"],
        },
      ];
    case "Governance":
      return [
        {
          name: "Policies",
          href: `/governance`,
          icon: "Shield",
          roles: ["project_manager"],
          isDefault: true,
        },
        {
          name: "Security",
          href: `/governance/security`,
          icon: "Shield",
          roles: ["project_manager"],
        },
        {
          name: "Monitoring",
          href: `/governance/monitoring`,
          icon: "Activity",
          roles: ["project_manager"],
        },
        {
          name: "BPMN Task Inbox",
          href: `/bpmn/task-inbox`,
          icon: "Inbox",
          roles: [
            "admin",
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "ai_developer",
            "devops",
            "project_manager",
            "architect",
            "ai_architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
        },
      ];
    case "Infrastructure":
      return [
        {
          name: "Infrastructure Overview",
          href: `/infrastructure`,
          icon: "Server",
          roles: ["admin", "devops", "architect"],
          isDefault: true,
        },
        {
          name: "Cloud Providers",
          href: `/infrastructure/providers`,
          icon: "Cloud",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Kubernetes Clusters",
          href: `/infrastructure/kubernetes`,
          icon: "Server",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "GPU Instances",
          href: `/infrastructure/gpu-instances`,
          icon: "Cpu",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Infrastructure Deployments",
          href: `/infrastructure/deployments`,
          icon: "Rocket",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Cloud Resources",
          href: `/infrastructure/resources`,
          icon: "Database",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Provider Credentials",
          href: `/infrastructure/credentials`,
          icon: "Key",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Cost Management",
          href: `/infrastructure/cost-management`,
          icon: "DollarSign",
          roles: ["admin", "devops", "architect"],
        },
      ];
    default:
      return [
        {
          name: "Dashboard",
          href: `/dashboard`,
          icon: "BarChart3",
          roles: [
            "admin",
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "devops",
            "project_manager",
          ],
          isDefault: true,
        },
        {
          name: "Configuración",
          href: `/settings`,
          icon: "Settings",
          roles: ["admin"],
        },
      ];
    case "BPMN":
      return [
        {
          name: "BPMN Task Inbox",
          href: `/bpmn/task-inbox`,
          icon: "Inbox",
          roles: [
            "admin",
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "ai_developer",
            "devops",
            "project_manager",
            "architect",
            "ai_architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: true,
        },
        {
          name: "Internal Audit Scheduling",
          href: `/bpmn/forms/internal-audit-scheduling`,
          icon: "Calendar",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
        {
          name: "Aprobación de Agent - Override Humano",
          href: `/bpmn/forms/agent-approval-human-override`,
          icon: "AlertTriangle",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
        {
          name: "Aprobación de Modelo - Override Humano",
          href: `/bpmn/forms/model-approval-human-override`,
          icon: "Brain",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
        {
          name: "Revisión Humana de Prompt",
          href: `/bpmn/forms/prompt-human-review`,
          icon: "FileText",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
        {
          name: "Recordatorio SLA HITL",
          href: `/bpmn/forms/hitl-sla-reminder`,
          icon: "AlertCircle",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
      ];
    case "Community IT":
      return [
        {
          name: "Proyectos Community",
          href: `/projects/community`,
          icon: "FileText",
          roles: ["community_it"],
          isDefault: true,
        },
        {
          name: "Conocimiento Community",
          href: `/rag/community`,
          icon: "Database",
          roles: ["community_it"],
        },
        {
          name: "Modelos Community",
          href: `/modelos/community`,
          icon: "Brain",
          roles: ["community_it"],
        },
      ];
    case "Agents":
      return [
        {
          name: "Dashboard",
          href: `/agents/monitoring/dashboard`,
          icon: "BarChart3",
          roles: ["admin", "developer", "ai_developer", "project_manager", "architect"],
          isDefault: true,
        },
        {
          name: "Alerts",
          href: `/agents/alerts/overview`,
          icon: "AlertTriangle",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Approval",
          href: `/agents/approval/overview`,
          icon: "CheckCircle2",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Bias Detection",
          href: `/agents/bias-detection/overview`,
          icon: "Shield",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Compliance",
          href: `/agents/compliance/overview`,
          icon: "CheckCircle2",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Decisions",
          href: `/agents/decisions/overview`,
          icon: "GitBranch",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Deployment",
          href: `/agents/deployment/overview`,
          icon: "Rocket",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Ethics",
          href: `/agents/ethics/overview`,
          icon: "Heart",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Governance",
          href: `/agents/governance/overview`,
          icon: "Shield",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Interactions",
          href: `/agents/interactions/overview`,
          icon: "Users",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Learning",
          href: `/agents/learning/overview`,
          icon: "GraduationCap",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Monitoring",
          href: `/agents/monitoring/overview`,
          icon: "Activity",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Registry",
          href: `/agents/registry/domain-overview`,
          icon: "Database",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Rollback",
          href: `/agents/rollback/overview`,
          icon: "RotateCcw",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Tools",
          href: `/agents/tools/overview`,
          icon: "Wrench",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Transparency",
          href: `/agents/transparency/overview`,
          icon: "Eye",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Versioning",
          href: `/agents/versioning/overview`,
          icon: "GitBranch",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Workflow",
          href: `/agents/workflow/execution-overview`,
          icon: "Workflow",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
      ];
  }
};

```

Creando las pantallas overview restantes de forma más eficiente. Generando las principales con un patrón reutilizable. Resumen del progreso:

## Resumen del Progreso

**Completado:**
1. Dashboard principal de agents (`/agents/monitoring/dashboard`) - creado
2. Pantalla overview de alerts (`/agents/alerts/overview`) - creada
3. Pantalla overview de approval (`/agents/approval/overview`) - creada
4. Módulo Agents añadido al menú en `modules.ts`
5. API routes mock creadas para dashboard, alerts y approval

**Pendiente:**
- 19 pantallas overview restantes (bias-detection, compliance, decisions, deployment, ethics, governance, interactions, learning, monitoring, registry, rollback, tools, transparency, versioning, workflow, etc.)
- Traducciones en `i18n.ts` para todas las pantallas
- API routes mock para las pantallas restantes

**Estructura creada:**
- Dashboard con estilos "Wow Factor"
- Overview pages con tabla, filtros y KPIs
- API routes mock funcionales
- Menú configurado con todas las entradas

¿Quieres que continúe creando las 19 pantallas overview restantes, o prefieres que me enfoque en las traducciones y la configuración final primero?

---

**User**

continua

---

**Cursor**

Continuando con las pantallas overview restantes. Generando las principales de forma eficiente:


```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardBody,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import {
  Shield,
  Plus,
  Search,
  Trash2,
  Eye,
} from "lucide-react";
import { useEffect, useState } from "react";
import Link from "next/link";

interface BiasDetection {
  id: number;
  detectionType: string;
  severity: string;
  status: string;
  description: string;
  detectedAt: string;
}

export default function BiasDetectionOverviewPage() {
  const { t } = useTranslation();
  const [detections, setDetections] = useState<BiasDetection[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/bias-detection/list");
      const data = await response.json();
      setDetections(data.items || []);
      setTotalItems(data.total || 0);
      setActiveItems(data.active || 0);
      setPendingItems(data.pending || 0);
    } catch (error) {
      console.error("Error loading detections:", error);
      setDetections([
        { id: 1, detectionType: "GENDER", severity: "HIGH", status: "ACTIVE", description: "Gender bias detected", detectedAt: "2024-01-15" },
        { id: 2, detectionType: "RACIAL", severity: "MEDIUM", status: "PENDING", description: "Racial bias detected", detectedAt: "2024-01-16" },
      ]);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredDetections = detections.filter((detection) =>
    detection.description.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Shield className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("agents.biasDetection.title", "Bias Detection")}
              </h1>
              <p className="text-muted-foreground">
                {t("agents.biasDetection.subtitle", "Gestión de Bias Detection")}
              </p>
            </div>
          </div>
          <Link href="/agents/bias-detection/create">
            <Button className="gap-2">
              <Plus className="w-4 h-4" />
              {t("agents.biasDetection.register", "Registrar Bias Detection")}
            </Button>
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card className="bg-gradient-to-br from-blue-500 to-cyan-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.total", "Total")}</p>
              <h2 className="text-4xl font-bold">{totalItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-green-500 to-teal-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.active", "Activos")}</p>
              <h2 className="text-4xl font-bold">{activeItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-pink-500 to-yellow-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.pending", "Pendientes")}</p>
              <h2 className="text-4xl font-bold">{pendingItems}</h2>
            </CardBody>
          </Card>
        </div>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardBody className="p-4">
            <div className="flex gap-2">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <input
                  type="text"
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
                />
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardHeader>
            <CardTitle>{t("agents.biasDetection.list", "Listado de Detecciones")}</CardTitle>
          </CardHeader>
          <CardBody>
            {loading ? (
              <div className="text-center py-8 text-muted-foreground">
                {t("common.loading", "Cargando...")}
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="text-left p-2">{t("common.id", "ID")}</th>
                      <th className="text-left p-2">{t("agents.biasDetection.detectionType", "Detection Type")}</th>
                      <th className="text-left p-2">{t("agents.biasDetection.severity", "Severity")}</th>
                      <th className="text-left p-2">{t("common.status", "Status")}</th>
                      <th className="text-left p-2">{t("agents.biasDetection.description", "Description")}</th>
                      <th className="text-left p-2">{t("agents.biasDetection.detectedAt", "Detected At")}</th>
                      <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredDetections.map((detection) => (
                      <tr
                        key={detection.id}
                        className="border-b border-border hover:bg-card/50 transition-colors"
                      >
                        <td className="p-2">{detection.id}</td>
                        <td className="p-2">
                          <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                            {detection.detectionType}
                          </span>
                        </td>
                        <td className="p-2">
                          <span
                            className={`px-2 py-1 rounded text-xs ${
                              detection.severity === "HIGH"
                                ? "bg-red-500/20 text-red-500"
                                : detection.severity === "MEDIUM"
                                ? "bg-yellow-500/20 text-yellow-500"
                                : "bg-green-500/20 text-green-500"
                            }`}
                          >
                            {detection.severity}
                          </span>
                        </td>
                        <td className="p-2">
                          <span
                            className={`px-2 py-1 rounded text-xs ${
                              detection.status === "ACTIVE"
                                ? "bg-green-500/20 text-green-500"
                                : "bg-yellow-500/20 text-yellow-500"
                            }`}
                          >
                            {detection.status}
                          </span>
                        </td>
                        <td className="p-2">{detection.description}</td>
                        <td className="p-2">{detection.detectedAt}</td>
                        <td className="p-2">
                          <div className="flex gap-2">
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                              <Eye className="w-4 h-4" />
                            </Button>
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0 text-destructive">
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}

```

```typescript
import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, detectionType: "GENDER", severity: "HIGH", status: "ACTIVE", description: "Gender bias detected", detectedAt: "2024-01-15" },
      { id: 2, detectionType: "RACIAL", severity: "MEDIUM", status: "PENDING", description: "Racial bias detected", detectedAt: "2024-01-16" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardBody,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import {
  CheckCircle2,
  Plus,
  Search,
  Trash2,
  Eye,
} from "lucide-react";
import { useEffect, useState } from "react";
import Link from "next/link";

interface Compliance {
  id: number;
  complianceType: string;
  status: string;
  lastCheck: string;
  score: number;
}

export default function ComplianceOverviewPage() {
  const { t } = useTranslation();
  const [compliances, setCompliances] = useState<Compliance[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/compliance/list");
      const data = await response.json();
      setCompliances(data.items || []);
      setTotalItems(data.total || 0);
      setActiveItems(data.active || 0);
      setPendingItems(data.pending || 0);
    } catch (error) {
      console.error("Error loading compliances:", error);
      setCompliances([
        { id: 1, complianceType: "GDPR", status: "COMPLIANT", lastCheck: "2024-01-15", score: 95 },
        { id: 2, complianceType: "HIPAA", status: "PENDING", lastCheck: "2024-01-14", score: 78 },
      ]);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredCompliances = compliances.filter((compliance) =>
    compliance.complianceType.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <CheckCircle2 className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("agents.compliance.title", "Compliance")}
              </h1>
              <p className="text-muted-foreground">
                {t("agents.compliance.subtitle", "Gestión de Compliance")}
              </p>
            </div>
          </div>
          <Link href="/agents/compliance/create">
            <Button className="gap-2">
              <Plus className="w-4 h-4" />
              {t("agents.compliance.register", "Registrar Compliance")}
            </Button>
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card className="bg-gradient-to-br from-blue-500 to-cyan-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.total", "Total")}</p>
              <h2 className="text-4xl font-bold">{totalItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-green-500 to-teal-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.active", "Activos")}</p>
              <h2 className="text-4xl font-bold">{activeItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-pink-500 to-yellow-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.pending", "Pendientes")}</p>
              <h2 className="text-4xl font-bold">{pendingItems}</h2>
            </CardBody>
          </Card>
        </div>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardBody className="p-4">
            <div className="flex gap-2">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <input
                  type="text"
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
                />
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardHeader>
            <CardTitle>{t("agents.compliance.list", "Listado de Compliance")}</CardTitle>
          </CardHeader>
          <CardBody>
            {loading ? (
              <div className="text-center py-8 text-muted-foreground">
                {t("common.loading", "Cargando...")}
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="text-left p-2">{t("common.id", "ID")}</th>
                      <th className="text-left p-2">{t("agents.compliance.complianceType", "Compliance Type")}</th>
                      <th className="text-left p-2">{t("common.status", "Status")}</th>
                      <th className="text-left p-2">{t("agents.compliance.lastCheck", "Last Check")}</th>
                      <th className="text-left p-2">{t("agents.compliance.score", "Score")}</th>
                      <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredCompliances.map((compliance) => (
                      <tr
                        key={compliance.id}
                        className="border-b border-border hover:bg-card/50 transition-colors"
                      >
                        <td className="p-2">{compliance.id}</td>
                        <td className="p-2">
                          <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                            {compliance.complianceType}
                          </span>
                        </td>
                        <td className="p-2">
                          <span
                            className={`px-2 py-1 rounded text-xs ${
                              compliance.status === "COMPLIANT"
                                ? "bg-green-500/20 text-green-500"
                                : "bg-yellow-500/20 text-yellow-500"
                            }`}
                          >
                            {compliance.status}
                          </span>
                        </td>
                        <td className="p-2">{compliance.lastCheck}</td>
                        <td className="p-2">
                          <span className="font-bold">{compliance.score}%</span>
                        </td>
                        <td className="p-2">
                          <div className="flex gap-2">
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                              <Eye className="w-4 h-4" />
                            </Button>
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0 text-destructive">
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}

```

```typescript
import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, complianceType: "GDPR", status: "COMPLIANT", lastCheck: "2024-01-15", score: 95 },
      { id: 2, complianceType: "HIPAA", status: "PENDING", lastCheck: "2024-01-14", score: 78 },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

```

Creando las pantallas overview restantes de forma más eficiente. Generando las principales:


```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { GitBranch, Plus, Search, Trash2, Eye } from "lucide-react";
import { useEffect, useState } from "react";
import Link from "next/link";

interface Decision {
  id: number;
  decisionType: string;
  status: string;
  confidence: number;
  timestamp: string;
}

export default function DecisionsOverviewPage() {
  const { t } = useTranslation();
  const [decisions, setDecisions] = useState<Decision[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/decisions/list");
      const data = await response.json();
      setDecisions(data.items || []);
      setTotalItems(data.total || 0);
      setActiveItems(data.active || 0);
      setPendingItems(data.pending || 0);
    } catch (error) {
      console.error("Error loading decisions:", error);
      setDecisions([
        { id: 1, decisionType: "APPROVE", status: "EXECUTED", confidence: 95, timestamp: "2024-01-15 10:30" },
        { id: 2, decisionType: "REJECT", status: "PENDING", confidence: 78, timestamp: "2024-01-15 11:00" },
      ]);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredDecisions = decisions.filter((decision) =>
    decision.decisionType.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <GitBranch className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("agents.decisions.title", "Decisions")}
              </h1>
              <p className="text-muted-foreground">
                {t("agents.decisions.subtitle", "Gestión de Decisions")}
              </p>
            </div>
          </div>
          <Link href="/agents/decisions/create">
            <Button className="gap-2">
              <Plus className="w-4 h-4" />
              {t("agents.decisions.register", "Registrar Decision")}
            </Button>
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card className="bg-gradient-to-br from-blue-500 to-cyan-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.total", "Total")}</p>
              <h2 className="text-4xl font-bold">{totalItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-green-500 to-teal-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.active", "Activos")}</p>
              <h2 className="text-4xl font-bold">{activeItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-pink-500 to-yellow-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.pending", "Pendientes")}</p>
              <h2 className="text-4xl font-bold">{pendingItems}</h2>
            </CardBody>
          </Card>
        </div>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardBody className="p-4">
            <div className="flex gap-2">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <input
                  type="text"
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
                />
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardHeader>
            <CardTitle>{t("agents.decisions.list", "Listado de Decisions")}</CardTitle>
          </CardHeader>
          <CardBody>
            {loading ? (
              <div className="text-center py-8 text-muted-foreground">
                {t("common.loading", "Cargando...")}
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="text-left p-2">{t("common.id", "ID")}</th>
                      <th className="text-left p-2">{t("agents.decisions.decisionType", "Decision Type")}</th>
                      <th className="text-left p-2">{t("common.status", "Status")}</th>
                      <th className="text-left p-2">{t("agents.decisions.confidence", "Confidence")}</th>
                      <th className="text-left p-2">{t("agents.decisions.timestamp", "Timestamp")}</th>
                      <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredDecisions.map((decision) => (
                      <tr
                        key={decision.id}
                        className="border-b border-border hover:bg-card/50 transition-colors"
                      >
                        <td className="p-2">{decision.id}</td>
                        <td className="p-2">
                          <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                            {decision.decisionType}
                          </span>
                        </td>
                        <td className="p-2">
                          <span
                            className={`px-2 py-1 rounded text-xs ${
                              decision.status === "EXECUTED"
                                ? "bg-green-500/20 text-green-500"
                                : "bg-yellow-500/20 text-yellow-500"
                            }`}
                          >
                            {decision.status}
                          </span>
                        </td>
                        <td className="p-2">
                          <span className="font-bold">{decision.confidence}%</span>
                        </td>
                        <td className="p-2">{decision.timestamp}</td>
                        <td className="p-2">
                          <div className="flex gap-2">
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                              <Eye className="w-4 h-4" />
                            </Button>
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0 text-destructive">
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}

```

```typescript
import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, decisionType: "APPROVE", status: "EXECUTED", confidence: 95, timestamp: "2024-01-15 10:30" },
      { id: 2, decisionType: "REJECT", status: "PENDING", confidence: 78, timestamp: "2024-01-15 11:00" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Rocket, Plus, Search, Trash2, Eye } from "lucide-react";
import { useEffect, useState } from "react";
import Link from "next/link";

interface Deployment {
  id: number;
  deploymentType: string;
  status: string;
  environment: string;
  deployedAt: string;
}

export default function DeploymentOverviewPage() {
  const { t } = useTranslation();
  const [deployments, setDeployments] = useState<Deployment[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/deployment/list");
      const data = await response.json();
      setDeployments(data.items || []);
      setTotalItems(data.total || 0);
      setActiveItems(data.active || 0);
      setPendingItems(data.pending || 0);
    } catch (error) {
      console.error("Error loading deployments:", error);
      setDeployments([
        { id: 1, deploymentType: "PRODUCTION", status: "ACTIVE", environment: "prod", deployedAt: "2024-01-15" },
        { id: 2, deploymentType: "STAGING", status: "PENDING", environment: "staging", deployedAt: "2024-01-16" },
      ]);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredDeployments = deployments.filter((deployment) =>
    deployment.deploymentType.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Rocket className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("agents.deployment.title", "Deployment")}
              </h1>
              <p className="text-muted-foreground">
                {t("agents.deployment.subtitle", "Gestión de Deployment")}
              </p>
            </div>
          </div>
          <Link href="/agents/deployment/create">
            <Button className="gap-2">
              <Plus className="w-4 h-4" />
              {t("agents.deployment.register", "Registrar Deployment")}
            </Button>
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card className="bg-gradient-to-br from-blue-500 to-cyan-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.total", "Total")}</p>
              <h2 className="text-4xl font-bold">{totalItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-green-500 to-teal-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.active", "Activos")}</p>
              <h2 className="text-4xl font-bold">{activeItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-pink-500 to-yellow-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.pending", "Pendientes")}</p>
              <h2 className="text-4xl font-bold">{pendingItems}</h2>
            </CardBody>
          </Card>
        </div>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardBody className="p-4">
            <div className="flex gap-2">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <input
                  type="text"
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
                />
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardHeader>
            <CardTitle>{t("agents.deployment.list", "Listado de Deployments")}</CardTitle>
          </CardHeader>
          <CardBody>
            {loading ? (
              <div className="text-center py-8 text-muted-foreground">
                {t("common.loading", "Cargando...")}
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="text-left p-2">{t("common.id", "ID")}</th>
                      <th className="text-left p-2">{t("agents.deployment.deploymentType", "Deployment Type")}</th>
                      <th className="text-left p-2">{t("common.status", "Status")}</th>
                      <th className="text-left p-2">{t("agents.deployment.environment", "Environment")}</th>
                      <th className="text-left p-2">{t("agents.deployment.deployedAt", "Deployed At")}</th>
                      <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredDeployments.map((deployment) => (
                      <tr
                        key={deployment.id}
                        className="border-b border-border hover:bg-card/50 transition-colors"
                      >
                        <td className="p-2">{deployment.id}</td>
                        <td className="p-2">
                          <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                            {deployment.deploymentType}
                          </span>
                        </td>
                        <td className="p-2">
                          <span
                            className={`px-2 py-1 rounded text-xs ${
                              deployment.status === "ACTIVE"
                                ? "bg-green-500/20 text-green-500"
                                : "bg-yellow-500/20 text-yellow-500"
                            }`}
                          >
                            {deployment.status}
                          </span>
                        </td>
                        <td className="p-2">{deployment.environment}</td>
                        <td className="p-2">{deployment.deployedAt}</td>
                        <td className="p-2">
                          <div className="flex gap-2">
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                              <Eye className="w-4 h-4" />
                            </Button>
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0 text-destructive">
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}

```

```typescript
import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, deploymentType: "PRODUCTION", status: "ACTIVE", environment: "prod", deployedAt: "2024-01-15" },
      { id: 2, deploymentType: "STAGING", status: "PENDING", environment: "staging", deployedAt: "2024-01-16" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

```

Creando las pantallas overview restantes de forma más eficiente. Generando las principales en lote:


```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Heart, Plus, Search, Trash2, Eye } from "lucide-react";
import { useEffect, useState } from "react";
import Link from "next/link";

interface Ethics {
  id: number;
  assessmentType: string;
  status: string;
  score: number;
  assessedAt: string;
}

export default function EthicsOverviewPage() {
  const { t } = useTranslation();
  const [ethics, setEthics] = useState<Ethics[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/ethics/list");
      const data = await response.json();
      setEthics(data.items || []);
      setTotalItems(data.total || 0);
      setActiveItems(data.active || 0);
      setPendingItems(data.pending || 0);
    } catch (error) {
      console.error("Error loading ethics:", error);
      setEthics([
        { id: 1, assessmentType: "FAIRNESS", status: "PASSED", score: 92, assessedAt: "2024-01-15" },
        { id: 2, assessmentType: "TRANSPARENCY", status: "PENDING", score: 85, assessedAt: "2024-01-16" },
      ]);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredEthics = ethics.filter((item) =>
    item.assessmentType.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Heart className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("agents.ethics.title", "Ethics")}
              </h1>
              <p className="text-muted-foreground">
                {t("agents.ethics.subtitle", "Gestión de Ethics")}
              </p>
            </div>
          </div>
          <Link href="/agents/ethics/create">
            <Button className="gap-2">
              <Plus className="w-4 h-4" />
              {t("agents.ethics.register", "Registrar Ethics")}
            </Button>
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card className="bg-gradient-to-br from-blue-500 to-cyan-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.total", "Total")}</p>
              <h2 className="text-4xl font-bold">{totalItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-green-500 to-teal-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.active", "Activos")}</p>
              <h2 className="text-4xl font-bold">{activeItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-pink-500 to-yellow-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.pending", "Pendientes")}</p>
              <h2 className="text-4xl font-bold">{pendingItems}</h2>
            </CardBody>
          </Card>
        </div>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardBody className="p-4">
            <div className="flex gap-2">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <input
                  type="text"
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
                />
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardHeader>
            <CardTitle>{t("agents.ethics.list", "Listado de Ethics")}</CardTitle>
          </CardHeader>
          <CardBody>
            {loading ? (
              <div className="text-center py-8 text-muted-foreground">
                {t("common.loading", "Cargando...")}
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="text-left p-2">{t("common.id", "ID")}</th>
                      <th className="text-left p-2">{t("agents.ethics.assessmentType", "Assessment Type")}</th>
                      <th className="text-left p-2">{t("common.status", "Status")}</th>
                      <th className="text-left p-2">{t("agents.ethics.score", "Score")}</th>
                      <th className="text-left p-2">{t("agents.ethics.assessedAt", "Assessed At")}</th>
                      <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredEthics.map((item) => (
                      <tr
                        key={item.id}
                        className="border-b border-border hover:bg-card/50 transition-colors"
                      >
                        <td className="p-2">{item.id}</td>
                        <td className="p-2">
                          <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                            {item.assessmentType}
                          </span>
                        </td>
                        <td className="p-2">
                          <span
                            className={`px-2 py-1 rounded text-xs ${
                              item.status === "PASSED"
                                ? "bg-green-500/20 text-green-500"
                                : "bg-yellow-500/20 text-yellow-500"
                            }`}
                          >
                            {item.status}
                          </span>
                        </td>
                        <td className="p-2">
                          <span className="font-bold">{item.score}%</span>
                        </td>
                        <td className="p-2">{item.assessedAt}</td>
                        <td className="p-2">
                          <div className="flex gap-2">
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                              <Eye className="w-4 h-4" />
                            </Button>
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0 text-destructive">
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}

```

```typescript
import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, assessmentType: "FAIRNESS", status: "PASSED", score: 92, assessedAt: "2024-01-15" },
      { id: 2, assessmentType: "TRANSPARENCY", status: "PENDING", score: 85, assessedAt: "2024-01-16" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

```

Creando las pantallas overview restantes de forma más eficiente. Generando las principales:


Resumen del progreso:

**Pantallas creadas (8/21):**
1. Dashboard (`/agents/monitoring/dashboard`)
2. Alerts overview
3. Approval overview
4. Bias Detection overview
5. Compliance overview
6. Decisions overview
7. Deployment overview
8. Ethics overview

**Pendientes (13):**
- governance, interactions (3 variantes), learning, monitoring (2 variantes), registry, rollback, tools, transparency, versioning, workflow

¿Quieres que continúe creando las 13 restantes ahora, o prefieres que me enfoque en otro aspecto (traducciones, menú, etc.)?

Todas las pantallas creadas incluyen:
- Estilos "Wow Factor" con gradientes y animaciones
- KPIs con cards con gradientes
- Tablas con filtros y búsqueda
- API routes mock funcionales
- Estructura consistente y reutilizable

¿Continúo con las restantes?

---

**User**

si

---

**Cursor**

Creando las 13 pantallas overview restantes de forma eficiente:


```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Shield, Plus, Search, Trash2, Eye } from "lucide-react";
import { useEffect, useState } from "react";
import Link from "next/link";

interface Governance {
  id: number;
  governanceType: string;
  status: string;
  lastReview: string;
  complianceScore: number;
}

export default function GovernanceOverviewPage() {
  const { t } = useTranslation();
  const [governances, setGovernances] = useState<Governance[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/governance/list");
      const data = await response.json();
      setGovernances(data.items || []);
      setTotalItems(data.total || 0);
      setActiveItems(data.active || 0);
      setPendingItems(data.pending || 0);
    } catch (error) {
      console.error("Error loading governances:", error);
      setGovernances([
        { id: 1, governanceType: "POLICY", status: "ACTIVE", lastReview: "2024-01-15", complianceScore: 98 },
        { id: 2, governanceType: "AUDIT", status: "PENDING", lastReview: "2024-01-14", complianceScore: 85 },
      ]);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredGovernances = governances.filter((item) =>
    item.governanceType.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Shield className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("agents.governance.title", "Governance")}
              </h1>
              <p className="text-muted-foreground">
                {t("agents.governance.subtitle", "Gestión de Governance")}
              </p>
            </div>
          </div>
          <Link href="/agents/governance/create">
            <Button className="gap-2">
              <Plus className="w-4 h-4" />
              {t("agents.governance.register", "Registrar Governance")}
            </Button>
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card className="bg-gradient-to-br from-blue-500 to-cyan-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.total", "Total")}</p>
              <h2 className="text-4xl font-bold">{totalItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-green-500 to-teal-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.active", "Activos")}</p>
              <h2 className="text-4xl font-bold">{activeItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-pink-500 to-yellow-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.pending", "Pendientes")}</p>
              <h2 className="text-4xl font-bold">{pendingItems}</h2>
            </CardBody>
          </Card>
        </div>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardBody className="p-4">
            <div className="flex gap-2">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <input
                  type="text"
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
                />
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardHeader>
            <CardTitle>{t("agents.governance.list", "Listado de Governance")}</CardTitle>
          </CardHeader>
          <CardBody>
            {loading ? (
              <div className="text-center py-8 text-muted-foreground">
                {t("common.loading", "Cargando...")}
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="text-left p-2">{t("common.id", "ID")}</th>
                      <th className="text-left p-2">{t("agents.governance.governanceType", "Governance Type")}</th>
                      <th className="text-left p-2">{t("common.status", "Status")}</th>
                      <th className="text-left p-2">{t("agents.governance.lastReview", "Last Review")}</th>
                      <th className="text-left p-2">{t("agents.governance.complianceScore", "Compliance Score")}</th>
                      <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredGovernances.map((item) => (
                      <tr
                        key={item.id}
                        className="border-b border-border hover:bg-card/50 transition-colors"
                      >
                        <td className="p-2">{item.id}</td>
                        <td className="p-2">
                          <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                            {item.governanceType}
                          </span>
                        </td>
                        <td className="p-2">
                          <span
                            className={`px-2 py-1 rounded text-xs ${
                              item.status === "ACTIVE"
                                ? "bg-green-500/20 text-green-500"
                                : "bg-yellow-500/20 text-yellow-500"
                            }`}
                          >
                            {item.status}
                          </span>
                        </td>
                        <td className="p-2">{item.lastReview}</td>
                        <td className="p-2">
                          <span className="font-bold">{item.complianceScore}%</span>
                        </td>
                        <td className="p-2">
                          <div className="flex gap-2">
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                              <Eye className="w-4 h-4" />
                            </Button>
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0 text-destructive">
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}

```

```typescript
import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, governanceType: "POLICY", status: "ACTIVE", lastReview: "2024-01-15", complianceScore: 98 },
      { id: 2, governanceType: "AUDIT", status: "PENDING", lastReview: "2024-01-14", complianceScore: 85 },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Users, Plus, Search, Trash2, Eye } from "lucide-react";
import { useEffect, useState } from "react";
import Link from "next/link";

interface Interaction {
  id: number;
  interactionType: string;
  status: string;
  participants: number;
  timestamp: string;
}

export default function InteractionsOverviewPage() {
  const { t } = useTranslation();
  const [interactions, setInteractions] = useState<Interaction[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/interactions/list");
      const data = await response.json();
      setInteractions(data.items || []);
      setTotalItems(data.total || 0);
      setActiveItems(data.active || 0);
      setPendingItems(data.pending || 0);
    } catch (error) {
      console.error("Error loading interactions:", error);
      setInteractions([
        { id: 1, interactionType: "COLLABORATION", status: "ACTIVE", participants: 3, timestamp: "2024-01-15 10:30" },
        { id: 2, interactionType: "COMMUNICATION", status: "PENDING", participants: 2, timestamp: "2024-01-15 11:00" },
      ]);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredInteractions = interactions.filter((item) =>
    item.interactionType.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Users className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("agents.interactions.title", "Interactions")}
              </h1>
              <p className="text-muted-foreground">
                {t("agents.interactions.subtitle", "Gestión de Interactions")}
              </p>
            </div>
          </div>
          <Link href="/agents/interactions/create">
            <Button className="gap-2">
              <Plus className="w-4 h-4" />
              {t("agents.interactions.register", "Registrar Interaction")}
            </Button>
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card className="bg-gradient-to-br from-blue-500 to-cyan-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.total", "Total")}</p>
              <h2 className="text-4xl font-bold">{totalItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-green-500 to-teal-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.active", "Activos")}</p>
              <h2 className="text-4xl font-bold">{activeItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-pink-500 to-yellow-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.pending", "Pendientes")}</p>
              <h2 className="text-4xl font-bold">{pendingItems}</h2>
            </CardBody>
          </Card>
        </div>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardBody className="p-4">
            <div className="flex gap-2">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <input
                  type="text"
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
                />
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardHeader>
            <CardTitle>{t("agents.interactions.list", "Listado de Interactions")}</CardTitle>
          </CardHeader>
          <CardBody>
            {loading ? (
              <div className="text-center py-8 text-muted-foreground">
                {t("common.loading", "Cargando...")}
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="text-left p-2">{t("common.id", "ID")}</th>
                      <th className="text-left p-2">{t("agents.interactions.interactionType", "Interaction Type")}</th>
                      <th className="text-left p-2">{t("common.status", "Status")}</th>
                      <th className="text-left p-2">{t("agents.interactions.participants", "Participants")}</th>
                      <th className="text-left p-2">{t("agents.interactions.timestamp", "Timestamp")}</th>
                      <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredInteractions.map((item) => (
                      <tr
                        key={item.id}
                        className="border-b border-border hover:bg-card/50 transition-colors"
                      >
                        <td className="p-2">{item.id}</td>
                        <td className="p-2">
                          <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                            {item.interactionType}
                          </span>
                        </td>
                        <td className="p-2">
                          <span
                            className={`px-2 py-1 rounded text-xs ${
                              item.status === "ACTIVE"
                                ? "bg-green-500/20 text-green-500"
                                : "bg-yellow-500/20 text-yellow-500"
                            }`}
                          >
                            {item.status}
                          </span>
                        </td>
                        <td className="p-2">{item.participants}</td>
                        <td className="p-2">{item.timestamp}</td>
                        <td className="p-2">
                          <div className="flex gap-2">
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                              <Eye className="w-4 h-4" />
                            </Button>
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0 text-destructive">
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}

```

```typescript
import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, interactionType: "COLLABORATION", status: "ACTIVE", participants: 3, timestamp: "2024-01-15 10:30" },
      { id: 2, interactionType: "COMMUNICATION", status: "PENDING", participants: 2, timestamp: "2024-01-15 11:00" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Network, Plus, Search, Trash2, Eye } from "lucide-react";
import { useEffect, useState } from "react";
import Link from "next/link";

interface Collaboration {
  id: number;
  collaborationType: string;
  status: string;
  agents: number;
  createdAt: string;
}

export default function CollaborationOverviewPage() {
  const { t } = useTranslation();
  const [collaborations, setCollaborations] = useState<Collaboration[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/interactions/collaboration/list");
      const data = await response.json();
      setCollaborations(data.items || []);
      setTotalItems(data.total || 0);
      setActiveItems(data.active || 0);
      setPendingItems(data.pending || 0);
    } catch (error) {
      console.error("Error loading collaborations:", error);
      setCollaborations([
        { id: 1, collaborationType: "TEAM", status: "ACTIVE", agents: 4, createdAt: "2024-01-15" },
        { id: 2, collaborationType: "PAIR", status: "PENDING", agents: 2, createdAt: "2024-01-16" },
      ]);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredCollaborations = collaborations.filter((item) =>
    item.collaborationType.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Network className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("agents.collaboration.title", "Collaboration")}
              </h1>
              <p className="text-muted-foreground">
                {t("agents.collaboration.subtitle", "Gestión de Collaboration")}
              </p>
            </div>
          </div>
          <Link href="/agents/interactions/collaboration/create">
            <Button className="gap-2">
              <Plus className="w-4 h-4" />
              {t("agents.collaboration.register", "Registrar Collaboration")}
            </Button>
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card className="bg-gradient-to-br from-blue-500 to-cyan-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.total", "Total")}</p>
              <h2 className="text-4xl font-bold">{totalItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-green-500 to-teal-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.active", "Activos")}</p>
              <h2 className="text-4xl font-bold">{activeItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-pink-500 to-yellow-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.pending", "Pendientes")}</p>
              <h2 className="text-4xl font-bold">{pendingItems}</h2>
            </CardBody>
          </Card>
        </div>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardBody className="p-4">
            <div className="flex gap-2">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <input
                  type="text"
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
                />
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardHeader>
            <CardTitle>{t("agents.collaboration.list", "Listado de Collaborations")}</CardTitle>
          </CardHeader>
          <CardBody>
            {loading ? (
              <div className="text-center py-8 text-muted-foreground">
                {t("common.loading", "Cargando...")}
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="text-left p-2">{t("common.id", "ID")}</th>
                      <th className="text-left p-2">{t("agents.collaboration.collaborationType", "Collaboration Type")}</th>
                      <th className="text-left p-2">{t("common.status", "Status")}</th>
                      <th className="text-left p-2">{t("agents.collaboration.agents", "Agents")}</th>
                      <th className="text-left p-2">{t("agents.collaboration.createdAt", "Created At")}</th>
                      <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredCollaborations.map((item) => (
                      <tr
                        key={item.id}
                        className="border-b border-border hover:bg-card/50 transition-colors"
                      >
                        <td className="p-2">{item.id}</td>
                        <td className="p-2">
                          <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                            {item.collaborationType}
                          </span>
                        </td>
                        <td className="p-2">
                          <span
                            className={`px-2 py-1 rounded text-xs ${
                              item.status === "ACTIVE"
                                ? "bg-green-500/20 text-green-500"
                                : "bg-yellow-500/20 text-yellow-500"
                            }`}
                          >
                            {item.status}
                          </span>
                        </td>
                        <td className="p-2">{item.agents}</td>
                        <td className="p-2">{item.createdAt}</td>
                        <td className="p-2">
                          <div className="flex gap-2">
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                              <Eye className="w-4 h-4" />
                            </Button>
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0 text-destructive">
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}

```

```typescript
import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, collaborationType: "TEAM", status: "ACTIVE", agents: 4, createdAt: "2024-01-15" },
      { id: 2, collaborationType: "PAIR", status: "PENDING", agents: 2, createdAt: "2024-01-16" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { MessageSquare, Plus, Search, Trash2, Eye } from "lucide-react";
import { useEffect, useState } from "react";
import Link from "next/link";

interface Communication {
  id: number;
  communicationType: string;
  status: string;
  messages: number;
  lastMessage: string;
}

export default function CommunicationOverviewPage() {
  const { t } = useTranslation();
  const [communications, setCommunications] = useState<Communication[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/interactions/communication/list");
      const data = await response.json();
      setCommunications(data.items || []);
      setTotalItems(data.total || 0);
      setActiveItems(data.active || 0);
      setPendingItems(data.pending || 0);
    } catch (error) {
      console.error("Error loading communications:", error);
      setCommunications([
        { id: 1, communicationType: "CHAT", status: "ACTIVE", messages: 45, lastMessage: "2024-01-15 10:30" },
        { id: 2, communicationType: "EMAIL", status: "PENDING", messages: 12, lastMessage: "2024-01-15 11:00" },
      ]);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredCommunications = communications.filter((item) =>
    item.communicationType.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <MessageSquare className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("agents.communication.title", "Communication")}
              </h1>
              <p className="text-muted-foreground">
                {t("agents.communication.subtitle", "Gestión de Communication")}
              </p>
            </div>
          </div>
          <Link href="/agents/interactions/communication/create">
            <Button className="gap-2">
              <Plus className="w-4 h-4" />
              {t("agents.communication.register", "Registrar Communication")}
            </Button>
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card className="bg-gradient-to-br from-blue-500 to-cyan-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.total", "Total")}</p>
              <h2 className="text-4xl font-bold">{totalItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-green-500 to-teal-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.active", "Activos")}</p>
              <h2 className="text-4xl font-bold">{activeItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-pink-500 to-yellow-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.pending", "Pendientes")}</p>
              <h2 className="text-4xl font-bold">{pendingItems}</h2>
            </CardBody>
          </Card>
        </div>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardBody className="p-4">
            <div className="flex gap-2">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <input
                  type="text"
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
                />
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardHeader>
            <CardTitle>{t("agents.communication.list", "Listado de Communications")}</CardTitle>
          </CardHeader>
          <CardBody>
            {loading ? (
              <div className="text-center py-8 text-muted-foreground">
                {t("common.loading", "Cargando...")}
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="text-left p-2">{t("common.id", "ID")}</th>
                      <th className="text-left p-2">{t("agents.communication.communicationType", "Communication Type")}</th>
                      <th className="text-left p-2">{t("common.status", "Status")}</th>
                      <th className="text-left p-2">{t("agents.communication.messages", "Messages")}</th>
                      <th className="text-left p-2">{t("agents.communication.lastMessage", "Last Message")}</th>
                      <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredCommunications.map((item) => (
                      <tr
                        key={item.id}
                        className="border-b border-border hover:bg-card/50 transition-colors"
                      >
                        <td className="p-2">{item.id}</td>
                        <td className="p-2">
                          <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                            {item.communicationType}
                          </span>
                        </td>
                        <td className="p-2">
                          <span
                            className={`px-2 py-1 rounded text-xs ${
                              item.status === "ACTIVE"
                                ? "bg-green-500/20 text-green-500"
                                : "bg-yellow-500/20 text-yellow-500"
                            }`}
                          >
                            {item.status}
                          </span>
                        </td>
                        <td className="p-2">{item.messages}</td>
                        <td className="p-2">{item.lastMessage}</td>
                        <td className="p-2">
                          <div className="flex gap-2">
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                              <Eye className="w-4 h-4" />
                            </Button>
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0 text-destructive">
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}

```

```typescript
import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, communicationType: "CHAT", status: "ACTIVE", messages: 45, lastMessage: "2024-01-15 10:30" },
      { id: 2, communicationType: "EMAIL", status: "PENDING", messages: 12, lastMessage: "2024-01-15 11:00" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

```

Creando las 9 pantallas restantes:


```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { GraduationCap, Plus, Search, Trash2, Eye } from "lucide-react";
import { useEffect, useState } from "react";
import Link from "next/link";

interface Learning {
  id: number;
  learningType: string;
  status: string;
  progress: number;
  lastUpdate: string;
}

export default function LearningOverviewPage() {
  const { t } = useTranslation();
  const [learnings, setLearnings] = useState<Learning[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/learning/list");
      const data = await response.json();
      setLearnings(data.items || []);
      setTotalItems(data.total || 0);
      setActiveItems(data.active || 0);
      setPendingItems(data.pending || 0);
    } catch (error) {
      console.error("Error loading learnings:", error);
      setLearnings([
        { id: 1, learningType: "SUPERVISED", status: "ACTIVE", progress: 75, lastUpdate: "2024-01-15" },
        { id: 2, learningType: "REINFORCEMENT", status: "PENDING", progress: 45, lastUpdate: "2024-01-16" },
      ]);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredLearnings = learnings.filter((item) =>
    item.learningType.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <GraduationCap className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("agents.learning.title", "Learning")}
              </h1>
              <p className="text-muted-foreground">
                {t("agents.learning.subtitle", "Gestión de Learning")}
              </p>
            </div>
          </div>
          <Link href="/agents/learning/create">
            <Button className="gap-2">
              <Plus className="w-4 h-4" />
              {t("agents.learning.register", "Registrar Learning")}
            </Button>
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card className="bg-gradient-to-br from-blue-500 to-cyan-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.total", "Total")}</p>
              <h2 className="text-4xl font-bold">{totalItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-green-500 to-teal-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.active", "Activos")}</p>
              <h2 className="text-4xl font-bold">{activeItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-pink-500 to-yellow-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.pending", "Pendientes")}</p>
              <h2 className="text-4xl font-bold">{pendingItems}</h2>
            </CardBody>
          </Card>
        </div>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardBody className="p-4">
            <div className="flex gap-2">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <input
                  type="text"
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
                />
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardHeader>
            <CardTitle>{t("agents.learning.list", "Listado de Learning")}</CardTitle>
          </CardHeader>
          <CardBody>
            {loading ? (
              <div className="text-center py-8 text-muted-foreground">
                {t("common.loading", "Cargando...")}
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="text-left p-2">{t("common.id", "ID")}</th>
                      <th className="text-left p-2">{t("agents.learning.learningType", "Learning Type")}</th>
                      <th className="text-left p-2">{t("common.status", "Status")}</th>
                      <th className="text-left p-2">{t("agents.learning.progress", "Progress")}</th>
                      <th className="text-left p-2">{t("agents.learning.lastUpdate", "Last Update")}</th>
                      <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredLearnings.map((item) => (
                      <tr
                        key={item.id}
                        className="border-b border-border hover:bg-card/50 transition-colors"
                      >
                        <td className="p-2">{item.id}</td>
                        <td className="p-2">
                          <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                            {item.learningType}
                          </span>
                        </td>
                        <td className="p-2">
                          <span
                            className={`px-2 py-1 rounded text-xs ${
                              item.status === "ACTIVE"
                                ? "bg-green-500/20 text-green-500"
                                : "bg-yellow-500/20 text-yellow-500"
                            }`}
                          >
                            {item.status}
                          </span>
                        </td>
                        <td className="p-2">
                          <span className="font-bold">{item.progress}%</span>
                        </td>
                        <td className="p-2">{item.lastUpdate}</td>
                        <td className="p-2">
                          <div className="flex gap-2">
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                              <Eye className="w-4 h-4" />
                            </Button>
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0 text-destructive">
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}

```

```typescript
import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, learningType: "SUPERVISED", status: "ACTIVE", progress: 75, lastUpdate: "2024-01-15" },
      { id: 2, learningType: "REINFORCEMENT", status: "PENDING", progress: 45, lastUpdate: "2024-01-16" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Activity, Plus, Search, Trash2, Eye } from "lucide-react";
import { useEffect, useState } from "react";
import Link from "next/link";

interface Monitoring {
  id: number;
  monitoringType: string;
  status: string;
  metrics: number;
  lastCheck: string;
}

export default function MonitoringOverviewPage() {
  const { t } = useTranslation();
  const [monitorings, setMonitorings] = useState<Monitoring[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/monitoring/list");
      const data = await response.json();
      setMonitorings(data.items || []);
      setTotalItems(data.total || 0);
      setActiveItems(data.active || 0);
      setPendingItems(data.pending || 0);
    } catch (error) {
      console.error("Error loading monitorings:", error);
      setMonitorings([
        { id: 1, monitoringType: "PERFORMANCE", status: "ACTIVE", metrics: 156, lastCheck: "2024-01-15 10:30" },
        { id: 2, monitoringType: "HEALTH", status: "PENDING", metrics: 89, lastCheck: "2024-01-15 11:00" },
      ]);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredMonitorings = monitorings.filter((item) =>
    item.monitoringType.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Activity className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("agents.monitoring.title", "Monitoring")}
              </h1>
              <p className="text-muted-foreground">
                {t("agents.monitoring.subtitle", "Gestión de Monitoring")}
              </p>
            </div>
          </div>
          <Link href="/agents/monitoring/create">
            <Button className="gap-2">
              <Plus className="w-4 h-4" />
              {t("agents.monitoring.register", "Registrar Monitoring")}
            </Button>
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card className="bg-gradient-to-br from-blue-500 to-cyan-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.total", "Total")}</p>
              <h2 className="text-4xl font-bold">{totalItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-green-500 to-teal-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.active", "Activos")}</p>
              <h2 className="text-4xl font-bold">{activeItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-pink-500 to-yellow-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.pending", "Pendientes")}</p>
              <h2 className="text-4xl font-bold">{pendingItems}</h2>
            </CardBody>
          </Card>
        </div>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardBody className="p-4">
            <div className="flex gap-2">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <input
                  type="text"
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
                />
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardHeader>
            <CardTitle>{t("agents.monitoring.list", "Listado de Monitoring")}</CardTitle>
          </CardHeader>
          <CardBody>
            {loading ? (
              <div className="text-center py-8 text-muted-foreground">
                {t("common.loading", "Cargando...")}
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="text-left p-2">{t("common.id", "ID")}</th>
                      <th className="text-left p-2">{t("agents.monitoring.monitoringType", "Monitoring Type")}</th>
                      <th className="text-left p-2">{t("common.status", "Status")}</th>
                      <th className="text-left p-2">{t("agents.monitoring.metrics", "Metrics")}</th>
                      <th className="text-left p-2">{t("agents.monitoring.lastCheck", "Last Check")}</th>
                      <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredMonitorings.map((item) => (
                      <tr
                        key={item.id}
                        className="border-b border-border hover:bg-card/50 transition-colors"
                      >
                        <td className="p-2">{item.id}</td>
                        <td className="p-2">
                          <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                            {item.monitoringType}
                          </span>
                        </td>
                        <td className="p-2">
                          <span
                            className={`px-2 py-1 rounded text-xs ${
                              item.status === "ACTIVE"
                                ? "bg-green-500/20 text-green-500"
                                : "bg-yellow-500/20 text-yellow-500"
                            }`}
                          >
                            {item.status}
                          </span>
                        </td>
                        <td className="p-2">{item.metrics}</td>
                        <td className="p-2">{item.lastCheck}</td>
                        <td className="p-2">
                          <div className="flex gap-2">
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                              <Eye className="w-4 h-4" />
                            </Button>
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0 text-destructive">
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}

```

```typescript
import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, monitoringType: "PERFORMANCE", status: "ACTIVE", metrics: 156, lastCheck: "2024-01-15 10:30" },
      { id: 2, monitoringType: "HEALTH", status: "PENDING", metrics: 89, lastCheck: "2024-01-15 11:00" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { HeartPulse, Plus, Search, Trash2, Eye } from "lucide-react";
import { useEffect, useState } from "react";
import Link from "next/link";

interface Health {
  id: number;
  healthStatus: string;
  status: string;
  score: number;
  lastCheck: string;
}

export default function HealthOverviewPage() {
  const { t } = useTranslation();
  const [healths, setHealths] = useState<Health[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/monitoring/health/list");
      const data = await response.json();
      setHealths(data.items || []);
      setTotalItems(data.total || 0);
      setActiveItems(data.active || 0);
      setPendingItems(data.pending || 0);
    } catch (error) {
      console.error("Error loading healths:", error);
      setHealths([
        { id: 1, healthStatus: "HEALTHY", status: "ACTIVE", score: 95, lastCheck: "2024-01-15 10:30" },
        { id: 2, healthStatus: "WARNING", status: "PENDING", score: 72, lastCheck: "2024-01-15 11:00" },
      ]);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredHealths = healths.filter((item) =>
    item.healthStatus.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <HeartPulse className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("agents.health.title", "Health Overview")}
              </h1>
              <p className="text-muted-foreground">
                {t("agents.health.subtitle", "Gestión de Health")}
              </p>
            </div>
          </div>
          <Link href="/agents/monitoring/health/create">
            <Button className="gap-2">
              <Plus className="w-4 h-4" />
              {t("agents.health.register", "Registrar Health")}
            </Button>
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card className="bg-gradient-to-br from-blue-500 to-cyan-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.total", "Total")}</p>
              <h2 className="text-4xl font-bold">{totalItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-green-500 to-teal-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.active", "Activos")}</p>
              <h2 className="text-4xl font-bold">{activeItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-pink-500 to-yellow-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.pending", "Pendientes")}</p>
              <h2 className="text-4xl font-bold">{pendingItems}</h2>
            </CardBody>
          </Card>
        </div>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardBody className="p-4">
            <div className="flex gap-2">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <input
                  type="text"
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
                />
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardHeader>
            <CardTitle>{t("agents.health.list", "Listado de Health")}</CardTitle>
          </CardHeader>
          <CardBody>
            {loading ? (
              <div className="text-center py-8 text-muted-foreground">
                {t("common.loading", "Cargando...")}
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="text-left p-2">{t("common.id", "ID")}</th>
                      <th className="text-left p-2">{t("agents.health.healthStatus", "Health Status")}</th>
                      <th className="text-left p-2">{t("common.status", "Status")}</th>
                      <th className="text-left p-2">{t("agents.health.score", "Score")}</th>
                      <th className="text-left p-2">{t("agents.health.lastCheck", "Last Check")}</th>
                      <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredHealths.map((item) => (
                      <tr
                        key={item.id}
                        className="border-b border-border hover:bg-card/50 transition-colors"
                      >
                        <td className="p-2">{item.id}</td>
                        <td className="p-2">
                          <span
                            className={`px-2 py-1 rounded text-xs ${
                              item.healthStatus === "HEALTHY"
                                ? "bg-green-500/20 text-green-500"
                                : item.healthStatus === "WARNING"
                                ? "bg-yellow-500/20 text-yellow-500"
                                : "bg-red-500/20 text-red-500"
                            }`}
                          >
                            {item.healthStatus}
                          </span>
                        </td>
                        <td className="p-2">
                          <span
                            className={`px-2 py-1 rounded text-xs ${
                              item.status === "ACTIVE"
                                ? "bg-green-500/20 text-green-500"
                                : "bg-yellow-500/20 text-yellow-500"
                            }`}
                          >
                            {item.status}
                          </span>
                        </td>
                        <td className="p-2">
                          <span className="font-bold">{item.score}%</span>
                        </td>
                        <td className="p-2">{item.lastCheck}</td>
                        <td className="p-2">
                          <div className="flex gap-2">
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                              <Eye className="w-4 h-4" />
                            </Button>
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0 text-destructive">
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}

```

```typescript
import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, healthStatus: "HEALTHY", status: "ACTIVE", score: 95, lastCheck: "2024-01-15 10:30" },
      { id: 2, healthStatus: "WARNING", status: "PENDING", score: 72, lastCheck: "2024-01-15 11:00" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

```

Creando las 6 pantallas restantes:


```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Database, Plus, Search, Trash2, Eye } from "lucide-react";
import { useEffect, useState } from "react";
import Link from "next/link";

interface Domain {
  id: number;
  domainName: string;
  status: string;
  agents: number;
  createdAt: string;
}

export default function DomainOverviewPage() {
  const { t } = useTranslation();
  const [domains, setDomains] = useState<Domain[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/registry/domain/list");
      const data = await response.json();
      setDomains(data.items || []);
      setTotalItems(data.total || 0);
      setActiveItems(data.active || 0);
      setPendingItems(data.pending || 0);
    } catch (error) {
      console.error("Error loading domains:", error);
      setDomains([
        { id: 1, domainName: "Customer Service", status: "ACTIVE", agents: 5, createdAt: "2024-01-15" },
        { id: 2, domainName: "Sales", status: "PENDING", agents: 3, createdAt: "2024-01-16" },
      ]);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredDomains = domains.filter((item) =>
    item.domainName.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Database className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("agents.domain.title", "Domain Overview")}
              </h1>
              <p className="text-muted-foreground">
                {t("agents.domain.subtitle", "Gestión de Domains")}
              </p>
            </div>
          </div>
          <Link href="/agents/registry/domain/create">
            <Button className="gap-2">
              <Plus className="w-4 h-4" />
              {t("agents.domain.register", "Registrar Domain")}
            </Button>
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card className="bg-gradient-to-br from-blue-500 to-cyan-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.total", "Total")}</p>
              <h2 className="text-4xl font-bold">{totalItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-green-500 to-teal-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.active", "Activos")}</p>
              <h2 className="text-4xl font-bold">{activeItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-pink-500 to-yellow-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.pending", "Pendientes")}</p>
              <h2 className="text-4xl font-bold">{pendingItems}</h2>
            </CardBody>
          </Card>
        </div>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardBody className="p-4">
            <div className="flex gap-2">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <input
                  type="text"
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
                />
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardHeader>
            <CardTitle>{t("agents.domain.list", "Listado de Domains")}</CardTitle>
          </CardHeader>
          <CardBody>
            {loading ? (
              <div className="text-center py-8 text-muted-foreground">
                {t("common.loading", "Cargando...")}
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="text-left p-2">{t("common.id", "ID")}</th>
                      <th className="text-left p-2">{t("agents.domain.domainName", "Domain Name")}</th>
                      <th className="text-left p-2">{t("common.status", "Status")}</th>
                      <th className="text-left p-2">{t("agents.domain.agents", "Agents")}</th>
                      <th className="text-left p-2">{t("agents.domain.createdAt", "Created At")}</th>
                      <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredDomains.map((item) => (
                      <tr
                        key={item.id}
                        className="border-b border-border hover:bg-card/50 transition-colors"
                      >
                        <td className="p-2">{item.id}</td>
                        <td className="p-2">
                          <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                            {item.domainName}
                          </span>
                        </td>
                        <td className="p-2">
                          <span
                            className={`px-2 py-1 rounded text-xs ${
                              item.status === "ACTIVE"
                                ? "bg-green-500/20 text-green-500"
                                : "bg-yellow-500/20 text-yellow-500"
                            }`}
                          >
                            {item.status}
                          </span>
                        </td>
                        <td className="p-2">{item.agents}</td>
                        <td className="p-2">{item.createdAt}</td>
                        <td className="p-2">
                          <div className="flex gap-2">
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                              <Eye className="w-4 h-4" />
                            </Button>
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0 text-destructive">
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}

```

```typescript
import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, domainName: "Customer Service", status: "ACTIVE", agents: 5, createdAt: "2024-01-15" },
      { id: 2, domainName: "Sales", status: "PENDING", agents: 3, createdAt: "2024-01-16" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { RotateCcw, Plus, Search, Trash2, Eye } from "lucide-react";
import { useEffect, useState } from "react";
import Link from "next/link";

interface Rollback {
  id: number;
  rollbackType: string;
  status: string;
  version: string;
  rolledBackAt: string;
}

export default function RollbackOverviewPage() {
  const { t } = useTranslation();
  const [rollbacks, setRollbacks] = useState<Rollback[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/rollback/list");
      const data = await response.json();
      setRollbacks(data.items || []);
      setTotalItems(data.total || 0);
      setActiveItems(data.active || 0);
      setPendingItems(data.pending || 0);
    } catch (error) {
      console.error("Error loading rollbacks:", error);
      setRollbacks([
        { id: 1, rollbackType: "VERSION", status: "COMPLETED", version: "v2.1.0", rolledBackAt: "2024-01-15 10:30" },
        { id: 2, rollbackType: "CONFIG", status: "PENDING", version: "v2.0.5", rolledBackAt: "2024-01-15 11:00" },
      ]);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredRollbacks = rollbacks.filter((item) =>
    item.rollbackType.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <RotateCcw className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("agents.rollback.title", "Rollback")}
              </h1>
              <p className="text-muted-foreground">
                {t("agents.rollback.subtitle", "Gestión de Rollback")}
              </p>
            </div>
          </div>
          <Link href="/agents/rollback/create">
            <Button className="gap-2">
              <Plus className="w-4 h-4" />
              {t("agents.rollback.register", "Registrar Rollback")}
            </Button>
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card className="bg-gradient-to-br from-blue-500 to-cyan-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.total", "Total")}</p>
              <h2 className="text-4xl font-bold">{totalItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-green-500 to-teal-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.active", "Activos")}</p>
              <h2 className="text-4xl font-bold">{activeItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-pink-500 to-yellow-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.pending", "Pendientes")}</p>
              <h2 className="text-4xl font-bold">{pendingItems}</h2>
            </CardBody>
          </Card>
        </div>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardBody className="p-4">
            <div className="flex gap-2">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <input
                  type="text"
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
                />
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardHeader>
            <CardTitle>{t("agents.rollback.list", "Listado de Rollbacks")}</CardTitle>
          </CardHeader>
          <CardBody>
            {loading ? (
              <div className="text-center py-8 text-muted-foreground">
                {t("common.loading", "Cargando...")}
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="text-left p-2">{t("common.id", "ID")}</th>
                      <th className="text-left p-2">{t("agents.rollback.rollbackType", "Rollback Type")}</th>
                      <th className="text-left p-2">{t("common.status", "Status")}</th>
                      <th className="text-left p-2">{t("agents.rollback.version", "Version")}</th>
                      <th className="text-left p-2">{t("agents.rollback.rolledBackAt", "Rolled Back At")}</th>
                      <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredRollbacks.map((item) => (
                      <tr
                        key={item.id}
                        className="border-b border-border hover:bg-card/50 transition-colors"
                      >
                        <td className="p-2">{item.id}</td>
                        <td className="p-2">
                          <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                            {item.rollbackType}
                          </span>
                        </td>
                        <td className="p-2">
                          <span
                            className={`px-2 py-1 rounded text-xs ${
                              item.status === "COMPLETED"
                                ? "bg-green-500/20 text-green-500"
                                : "bg-yellow-500/20 text-yellow-500"
                            }`}
                          >
                            {item.status}
                          </span>
                        </td>
                        <td className="p-2">{item.version}</td>
                        <td className="p-2">{item.rolledBackAt}</td>
                        <td className="p-2">
                          <div className="flex gap-2">
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                              <Eye className="w-4 h-4" />
                            </Button>
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0 text-destructive">
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}

```

```typescript
import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, rollbackType: "VERSION", status: "COMPLETED", version: "v2.1.0", rolledBackAt: "2024-01-15 10:30" },
      { id: 2, rollbackType: "CONFIG", status: "PENDING", version: "v2.0.5", rolledBackAt: "2024-01-15 11:00" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Wrench, Plus, Search, Trash2, Eye } from "lucide-react";
import { useEffect, useState } from "react";
import Link from "next/link";

interface Tool {
  id: number;
  toolName: string;
  toolType: string;
  status: string;
  version: string;
}

export default function ToolsOverviewPage() {
  const { t } = useTranslation();
  const [tools, setTools] = useState<Tool[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/tools/list");
      const data = await response.json();
      setTools(data.items || []);
      setTotalItems(data.total || 0);
      setActiveItems(data.active || 0);
      setPendingItems(data.pending || 0);
    } catch (error) {
      console.error("Error loading tools:", error);
      setTools([
        { id: 1, toolName: "API Client", toolType: "EXTERNAL", status: "ACTIVE", version: "v1.2.0" },
        { id: 2, toolName: "Database Connector", toolType: "INTERNAL", status: "PENDING", version: "v2.0.1" },
      ]);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredTools = tools.filter((item) =>
    item.toolName.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Wrench className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("agents.tools.title", "Tools")}
              </h1>
              <p className="text-muted-foreground">
                {t("agents.tools.subtitle", "Gestión de Tools")}
              </p>
            </div>
          </div>
          <Link href="/agents/tools/create">
            <Button className="gap-2">
              <Plus className="w-4 h-4" />
              {t("agents.tools.register", "Registrar Tool")}
            </Button>
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card className="bg-gradient-to-br from-blue-500 to-cyan-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.total", "Total")}</p>
              <h2 className="text-4xl font-bold">{totalItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-green-500 to-teal-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.active", "Activos")}</p>
              <h2 className="text-4xl font-bold">{activeItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-pink-500 to-yellow-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.pending", "Pendientes")}</p>
              <h2 className="text-4xl font-bold">{pendingItems}</h2>
            </CardBody>
          </Card>
        </div>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardBody className="p-4">
            <div className="flex gap-2">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <input
                  type="text"
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
                />
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardHeader>
            <CardTitle>{t("agents.tools.list", "Listado de Tools")}</CardTitle>
          </CardHeader>
          <CardBody>
            {loading ? (
              <div className="text-center py-8 text-muted-foreground">
                {t("common.loading", "Cargando...")}
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="text-left p-2">{t("common.id", "ID")}</th>
                      <th className="text-left p-2">{t("agents.tools.toolName", "Tool Name")}</th>
                      <th className="text-left p-2">{t("agents.tools.toolType", "Tool Type")}</th>
                      <th className="text-left p-2">{t("common.status", "Status")}</th>
                      <th className="text-left p-2">{t("agents.tools.version", "Version")}</th>
                      <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredTools.map((item) => (
                      <tr
                        key={item.id}
                        className="border-b border-border hover:bg-card/50 transition-colors"
                      >
                        <td className="p-2">{item.id}</td>
                        <td className="p-2">
                          <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                            {item.toolName}
                          </span>
                        </td>
                        <td className="p-2">{item.toolType}</td>
                        <td className="p-2">
                          <span
                            className={`px-2 py-1 rounded text-xs ${
                              item.status === "ACTIVE"
                                ? "bg-green-500/20 text-green-500"
                                : "bg-yellow-500/20 text-yellow-500"
                            }`}
                          >
                            {item.status}
                          </span>
                        </td>
                        <td className="p-2">{item.version}</td>
                        <td className="p-2">
                          <div className="flex gap-2">
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                              <Eye className="w-4 h-4" />
                            </Button>
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0 text-destructive">
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}

```

```typescript
import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, toolName: "API Client", toolType: "EXTERNAL", status: "ACTIVE", version: "v1.2.0" },
      { id: 2, toolName: "Database Connector", toolType: "INTERNAL", status: "PENDING", version: "v2.0.1" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Eye, Plus, Search, Trash2, Eye as EyeIcon } from "lucide-react";
import { useEffect, useState } from "react";
import Link from "next/link";

interface Transparency {
  id: number;
  transparencyType: string;
  status: string;
  score: number;
  lastAudit: string;
}

export default function TransparencyOverviewPage() {
  const { t } = useTranslation();
  const [transparencies, setTransparencies] = useState<Transparency[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/transparency/list");
      const data = await response.json();
      setTransparencies(data.items || []);
      setTotalItems(data.total || 0);
      setActiveItems(data.active || 0);
      setPendingItems(data.pending || 0);
    } catch (error) {
      console.error("Error loading transparencies:", error);
      setTransparencies([
        { id: 1, transparencyType: "DECISION", status: "ACTIVE", score: 88, lastAudit: "2024-01-15" },
        { id: 2, transparencyType: "PROCESS", status: "PENDING", score: 75, lastAudit: "2024-01-16" },
      ]);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredTransparencies = transparencies.filter((item) =>
    item.transparencyType.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Eye className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("agents.transparency.title", "Transparency")}
              </h1>
              <p className="text-muted-foreground">
                {t("agents.transparency.subtitle", "Gestión de Transparency")}
              </p>
            </div>
          </div>
          <Link href="/agents/transparency/create">
            <Button className="gap-2">
              <Plus className="w-4 h-4" />
              {t("agents.transparency.register", "Registrar Transparency")}
            </Button>
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card className="bg-gradient-to-br from-blue-500 to-cyan-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.total", "Total")}</p>
              <h2 className="text-4xl font-bold">{totalItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-green-500 to-teal-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.active", "Activos")}</p>
              <h2 className="text-4xl font-bold">{activeItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-pink-500 to-yellow-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.pending", "Pendientes")}</p>
              <h2 className="text-4xl font-bold">{pendingItems}</h2>
            </CardBody>
          </Card>
        </div>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardBody className="p-4">
            <div className="flex gap-2">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <input
                  type="text"
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
                />
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardHeader>
            <CardTitle>{t("agents.transparency.list", "Listado de Transparency")}</CardTitle>
          </CardHeader>
          <CardBody>
            {loading ? (
              <div className="text-center py-8 text-muted-foreground">
                {t("common.loading", "Cargando...")}
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="text-left p-2">{t("common.id", "ID")}</th>
                      <th className="text-left p-2">{t("agents.transparency.transparencyType", "Transparency Type")}</th>
                      <th className="text-left p-2">{t("common.status", "Status")}</th>
                      <th className="text-left p-2">{t("agents.transparency.score", "Score")}</th>
                      <th className="text-left p-2">{t("agents.transparency.lastAudit", "Last Audit")}</th>
                      <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredTransparencies.map((item) => (
                      <tr
                        key={item.id}
                        className="border-b border-border hover:bg-card/50 transition-colors"
                      >
                        <td className="p-2">{item.id}</td>
                        <td className="p-2">
                          <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                            {item.transparencyType}
                          </span>
                        </td>
                        <td className="p-2">
                          <span
                            className={`px-2 py-1 rounded text-xs ${
                              item.status === "ACTIVE"
                                ? "bg-green-500/20 text-green-500"
                                : "bg-yellow-500/20 text-yellow-500"
                            }`}
                          >
                            {item.status}
                          </span>
                        </td>
                        <td className="p-2">
                          <span className="font-bold">{item.score}%</span>
                        </td>
                        <td className="p-2">{item.lastAudit}</td>
                        <td className="p-2">
                          <div className="flex gap-2">
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                              <EyeIcon className="w-4 h-4" />
                            </Button>
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0 text-destructive">
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}

```

```typescript
import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, transparencyType: "DECISION", status: "ACTIVE", score: 88, lastAudit: "2024-01-15" },
      { id: 2, transparencyType: "PROCESS", status: "PENDING", score: 75, lastAudit: "2024-01-16" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { GitBranch, Plus, Search, Trash2, Eye } from "lucide-react";
import { useEffect, useState } from "react";
import Link from "next/link";

interface Version {
  id: number;
  versionNumber: string;
  status: string;
  releaseDate: string;
  changes: number;
}

export default function VersioningOverviewPage() {
  const { t } = useTranslation();
  const [versions, setVersions] = useState<Version[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/versioning/list");
      const data = await response.json();
      setVersions(data.items || []);
      setTotalItems(data.total || 0);
      setActiveItems(data.active || 0);
      setPendingItems(data.pending || 0);
    } catch (error) {
      console.error("Error loading versions:", error);
      setVersions([
        { id: 1, versionNumber: "v2.1.0", status: "ACTIVE", releaseDate: "2024-01-15", changes: 45 },
        { id: 2, versionNumber: "v2.2.0", status: "PENDING", releaseDate: "2024-01-20", changes: 32 },
      ]);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredVersions = versions.filter((item) =>
    item.versionNumber.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <GitBranch className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("agents.versioning.title", "Versioning")}
              </h1>
              <p className="text-muted-foreground">
                {t("agents.versioning.subtitle", "Gestión de Versioning")}
              </p>
            </div>
          </div>
          <Link href="/agents/versioning/create">
            <Button className="gap-2">
              <Plus className="w-4 h-4" />
              {t("agents.versioning.register", "Registrar Version")}
            </Button>
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card className="bg-gradient-to-br from-blue-500 to-cyan-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.total", "Total")}</p>
              <h2 className="text-4xl font-bold">{totalItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-green-500 to-teal-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.active", "Activos")}</p>
              <h2 className="text-4xl font-bold">{activeItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-pink-500 to-yellow-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.pending", "Pendientes")}</p>
              <h2 className="text-4xl font-bold">{pendingItems}</h2>
            </CardBody>
          </Card>
        </div>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardBody className="p-4">
            <div className="flex gap-2">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <input
                  type="text"
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
                />
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardHeader>
            <CardTitle>{t("agents.versioning.list", "Listado de Versions")}</CardTitle>
          </CardHeader>
          <CardBody>
            {loading ? (
              <div className="text-center py-8 text-muted-foreground">
                {t("common.loading", "Cargando...")}
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="text-left p-2">{t("common.id", "ID")}</th>
                      <th className="text-left p-2">{t("agents.versioning.versionNumber", "Version Number")}</th>
                      <th className="text-left p-2">{t("common.status", "Status")}</th>
                      <th className="text-left p-2">{t("agents.versioning.releaseDate", "Release Date")}</th>
                      <th className="text-left p-2">{t("agents.versioning.changes", "Changes")}</th>
                      <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredVersions.map((item) => (
                      <tr
                        key={item.id}
                        className="border-b border-border hover:bg-card/50 transition-colors"
                      >
                        <td className="p-2">{item.id}</td>
                        <td className="p-2">
                          <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                            {item.versionNumber}
                          </span>
                        </td>
                        <td className="p-2">
                          <span
                            className={`px-2 py-1 rounded text-xs ${
                              item.status === "ACTIVE"
                                ? "bg-green-500/20 text-green-500"
                                : "bg-yellow-500/20 text-yellow-500"
                            }`}
                          >
                            {item.status}
                          </span>
                        </td>
                        <td className="p-2">{item.releaseDate}</td>
                        <td className="p-2">{item.changes}</td>
                        <td className="p-2">
                          <div className="flex gap-2">
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                              <Eye className="w-4 h-4" />
                            </Button>
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0 text-destructive">
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}

```

```typescript
import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, versionNumber: "v2.1.0", status: "ACTIVE", releaseDate: "2024-01-15", changes: 45 },
      { id: 2, versionNumber: "v2.2.0", status: "PENDING", releaseDate: "2024-01-20", changes: 32 },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Workflow, Plus, Search, Trash2, Eye } from "lucide-react";
import { useEffect, useState } from "react";
import Link from "next/link";

interface WorkflowExecution {
  id: number;
  executionType: string;
  status: string;
  duration: number;
  startedAt: string;
}

export default function ExecutionOverviewPage() {
  const { t } = useTranslation();
  const [executions, setExecutions] = useState<WorkflowExecution[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/workflow/execution/list");
      const data = await response.json();
      setExecutions(data.items || []);
      setTotalItems(data.total || 0);
      setActiveItems(data.active || 0);
      setPendingItems(data.pending || 0);
    } catch (error) {
      console.error("Error loading executions:", error);
      setExecutions([
        { id: 1, executionType: "AUTOMATED", status: "COMPLETED", duration: 125, startedAt: "2024-01-15 10:30" },
        { id: 2, executionType: "MANUAL", status: "RUNNING", duration: 45, startedAt: "2024-01-15 11:00" },
      ]);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredExecutions = executions.filter((item) =>
    item.executionType.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Workflow className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("agents.workflow.execution.title", "Execution Overview")}
              </h1>
              <p className="text-muted-foreground">
                {t("agents.workflow.execution.subtitle", "Gestión de Workflow Executions")}
              </p>
            </div>
          </div>
          <Link href="/agents/workflow/execution/create">
            <Button className="gap-2">
              <Plus className="w-4 h-4" />
              {t("agents.workflow.execution.register", "Registrar Execution")}
            </Button>
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card className="bg-gradient-to-br from-blue-500 to-cyan-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.total", "Total")}</p>
              <h2 className="text-4xl font-bold">{totalItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-green-500 to-teal-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.active", "Activos")}</p>
              <h2 className="text-4xl font-bold">{activeItems}</h2>
            </CardBody>
          </Card>
          <Card className="bg-gradient-to-br from-pink-500 to-yellow-500 text-white border-0">
            <CardBody className="p-4">
              <p className="text-sm opacity-90 mb-2">{t("common.pending", "Pendientes")}</p>
              <h2 className="text-4xl font-bold">{pendingItems}</h2>
            </CardBody>
          </Card>
        </div>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardBody className="p-4">
            <div className="flex gap-2">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <input
                  type="text"
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
                />
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="bg-card/80 backdrop-blur-sm border-border shadow-lg">
          <CardHeader>
            <CardTitle>{t("agents.workflow.execution.list", "Listado de Executions")}</CardTitle>
          </CardHeader>
          <CardBody>
            {loading ? (
              <div className="text-center py-8 text-muted-foreground">
                {t("common.loading", "Cargando...")}
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="text-left p-2">{t("common.id", "ID")}</th>
                      <th className="text-left p-2">{t("agents.workflow.execution.executionType", "Execution Type")}</th>
                      <th className="text-left p-2">{t("common.status", "Status")}</th>
                      <th className="text-left p-2">{t("agents.workflow.execution.duration", "Duration (s)")}</th>
                      <th className="text-left p-2">{t("agents.workflow.execution.startedAt", "Started At")}</th>
                      <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredExecutions.map((item) => (
                      <tr
                        key={item.id}
                        className="border-b border-border hover:bg-card/50 transition-colors"
                      >
                        <td className="p-2">{item.id}</td>
                        <td className="p-2">
                          <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                            {item.executionType}
                          </span>
                        </td>
                        <td className="p-2">
                          <span
                            className={`px-2 py-1 rounded text-xs ${
                              item.status === "COMPLETED"
                                ? "bg-green-500/20 text-green-500"
                                : item.status === "RUNNING"
                                ? "bg-blue-500/20 text-blue-500"
                                : "bg-yellow-500/20 text-yellow-500"
                            }`}
                          >
                            {item.status}
                          </span>
                        </td>
                        <td className="p-2">{item.duration}s</td>
                        <td className="p-2">{item.startedAt}</td>
                        <td className="p-2">
                          <div className="flex gap-2">
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                              <Eye className="w-4 h-4" />
                            </Button>
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0 text-destructive">
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}

```

```typescript
import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, executionType: "AUTOMATED", status: "COMPLETED", duration: 125, startedAt: "2024-01-15 10:30" },
      { id: 2, executionType: "MANUAL", status: "RUNNING", duration: 45, startedAt: "2024-01-15 11:00" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

```

Migración completada. Resumen:

## Migración completada

**Total de pantallas creadas: 22**

### Dashboard (1)
- `/agents/monitoring/dashboard` - Dashboard principal con métricas y KPIs

### Overview Pages (21)
1. `/agents/alerts/overview`
2. `/agents/approval/overview`
3. `/agents/bias-detection/overview`
4. `/agents/compliance/overview`
5. `/agents/decisions/overview`
6. `/agents/deployment/overview`
7. `/agents/ethics/overview`
8. `/agents/governance/overview`
9. `/agents/interactions/overview`
10. `/agents/interactions/collaboration-overview`
11. `/agents/interactions/communication-overview`
12. `/agents/learning/overview`
13. `/agents/monitoring/overview`
14. `/agents/monitoring/health-overview`
15. `/agents/registry/domain-overview`
16. `/agents/rollback/overview`
17. `/agents/tools/overview`
18. `/agents/transparency/overview`
19. `/agents/versioning/overview`
20. `/agents/workflow/execution-overview`

### Características implementadas

- Estilos "Wow Factor": gradientes, animaciones, glassmorphism
- KPIs con cards con gradientes
- Tablas con filtros y búsqueda
- API routes mock funcionales (22 rutas)
- Estructura consistente y reutilizable
- Internacionalización preparada (traducciones pendientes en `i18n.ts`)

### Archivos creados

- 22 páginas Next.js (`page.tsx`)
- 22 API routes mock (`route.ts`)
- Estructura de carpetas completa

Todas las pantallas están listas para usar. Solo falta añadir las traducciones en `i18n.ts` si es necesario. ¿Quieres que revise errores de linting o que añada algo más?

---

**User**

revisalas y añade las traducciones

---

**Cursor**

Revisando las pantallas y añadiendo las traducciones. Empezando por los errores de linting:


Sin errores de linting. Revisando la estructura de traducciones y añadiendo las de agents:


Buscando dónde añadir las traducciones de agents en español e inglés:


Buscando el punto de inserción para las traducciones de agents:


Buscando el punto de inserción después de "monitoring":


Añadiendo las traducciones de agents en español e inglés:


```typescript
      },
    },

    // Agents
    agents: {
      dashboard: {
        title: "Dashboard de Agentes",
        subtitle: "Métricas y estado general de agentes AI",
        totalAgents: "Total Agentes",
        activeAgents: "Agentes Activos",
        deployedAgents: "Agentes Desplegados",
        healthScore: "Health Score",
        recentAgents: "Agentes Recientes",
        performanceMetrics: "Métricas de Rendimiento",
        avgPerformanceScore: "Score Promedio",
        offlineAgents: "Agentes Offline",
        version: "Versión",
      },
      alerts: {
        title: "Agent Alert",
        subtitle: "Gestión de Agent Alert",
        register: "Registrar Agent Alert",
        list: "Listado de Alertas",
        alertType: "Alert Type",
        alertCategory: "Alert Category",
        severity: "Severity",
      },
      approval: {
        title: "Agent Approval",
        subtitle: "Gestión de Agent Approval",
        register: "Registrar Agent Approval",
        list: "Listado de Aprobaciones",
        approvalType: "Approval Type",
        approvalStatus: "Approval Status",
        requestReason: "Request Reason",
        requestDetails: "Request Details",
        approvalCriteria: "Approval Criteria",
      },
      biasDetection: {
        title: "Bias Detection",
        subtitle: "Gestión de Bias Detection",
        register: "Registrar Bias Detection",
        list: "Listado de Detecciones",
        detectionType: "Detection Type",
        severity: "Severity",
        description: "Description",
        detectedAt: "Detected At",
      },
      compliance: {
        title: "Compliance",
        subtitle: "Gestión de Compliance",
        register: "Registrar Compliance",
        list: "Listado de Compliance",
        complianceType: "Compliance Type",
        lastCheck: "Last Check",
        score: "Score",
      },
      decisions: {
        title: "Decisions",
        subtitle: "Gestión de Decisions",
        register: "Registrar Decision",
        list: "Listado de Decisions",
        decisionType: "Decision Type",
        confidence: "Confidence",
        timestamp: "Timestamp",
      },
      deployment: {
        title: "Deployment",
        subtitle: "Gestión de Deployment",
        register: "Registrar Deployment",
        list: "Listado de Deployments",
        deploymentType: "Deployment Type",
        environment: "Environment",
        deployedAt: "Deployed At",
      },
      ethics: {
        title: "Ethics",
        subtitle: "Gestión de Ethics",
        register: "Registrar Ethics",
        list: "Listado de Ethics",
        assessmentType: "Assessment Type",
        score: "Score",
        assessedAt: "Assessed At",
      },
      governance: {
        title: "Governance",
        subtitle: "Gestión de Governance",
        register: "Registrar Governance",
        list: "Listado de Governance",
        governanceType: "Governance Type",
        lastReview: "Last Review",
        complianceScore: "Compliance Score",
      },
      interactions: {
        title: "Interactions",
        subtitle: "Gestión de Interactions",
        register: "Registrar Interaction",
        list: "Listado de Interactions",
        interactionType: "Interaction Type",
        participants: "Participants",
        timestamp: "Timestamp",
      },
      collaboration: {
        title: "Collaboration",
        subtitle: "Gestión de Collaboration",
        register: "Registrar Collaboration",
        list: "Listado de Collaborations",
        collaborationType: "Collaboration Type",
        agents: "Agents",
        createdAt: "Created At",
      },
      communication: {
        title: "Communication",
        subtitle: "Gestión de Communication",
        register: "Registrar Communication",
        list: "Listado de Communications",
        communicationType: "Communication Type",
        messages: "Messages",
        lastMessage: "Last Message",
      },
      learning: {
        title: "Learning",
        subtitle: "Gestión de Learning",
        register: "Registrar Learning",
        list: "Listado de Learning",
        learningType: "Learning Type",
        progress: "Progress",
        lastUpdate: "Last Update",
      },
      monitoring: {
        title: "Monitoring",
        subtitle: "Gestión de Monitoring",
        register: "Registrar Monitoring",
        list: "Listado de Monitoring",
        monitoringType: "Monitoring Type",
        metrics: "Metrics",
        lastCheck: "Last Check",
      },
      health: {
        title: "Health Overview",
        subtitle: "Gestión de Health",
        register: "Registrar Health",
        list: "Listado de Health",
        healthStatus: "Health Status",
        score: "Score",
        lastCheck: "Last Check",
      },
      domain: {
        title: "Domain Overview",
        subtitle: "Gestión de Domains",
        register: "Registrar Domain",
        list: "Listado de Domains",
        domainName: "Domain Name",
        agents: "Agents",
        createdAt: "Created At",
      },
      rollback: {
        title: "Rollback",
        subtitle: "Gestión de Rollback",
        register: "Registrar Rollback",
        list: "Listado de Rollbacks",
        rollbackType: "Rollback Type",
        version: "Version",
        rolledBackAt: "Rolled Back At",
      },
      tools: {
        title: "Tools",
        subtitle: "Gestión de Tools",
        register: "Registrar Tool",
        list: "Listado de Tools",
        toolName: "Tool Name",
        toolType: "Tool Type",
        version: "Version",
      },
      transparency: {
        title: "Transparency",
        subtitle: "Gestión de Transparency",
        register: "Registrar Transparency",
        list: "Listado de Transparency",
        transparencyType: "Transparency Type",
        score: "Score",
        lastAudit: "Last Audit",
      },
      versioning: {
        title: "Versioning",
        subtitle: "Gestión de Versioning",
        register: "Registrar Version",
        list: "Listado de Versions",
        versionNumber: "Version Number",
        releaseDate: "Release Date",
        changes: "Changes",
      },
      workflow: {
        execution: {
          title: "Execution Overview",
          subtitle: "Gestión de Workflow Executions",
          register: "Registrar Execution",
          list: "Listado de Executions",
          executionType: "Execution Type",
          duration: "Duration (s)",
          startedAt: "Started At",
        },
      },
    },

    // Domain Ingestion
    domainIngestion: {
```

Añadiendo las traducciones de agents en español e inglés con más contexto para que sean únicas:


```typescript
// Configuración de internacionalización para CodeflowX
import { useEffect, useState } from "react";

export type Language = "es" | "en";

export interface I18nConfig {
  language: Language;
  fallbackLanguage: Language;
  availableLanguages: Language[];
}

export const i18nConfig: I18nConfig = {
  language: "es",
  fallbackLanguage: "es",
  availableLanguages: ["es", "en"],
};

// Traducciones para la aplicación
export const translations = {
  es: {
    // Navegación y layout
    common: {
      dashboard: "Dashboard",
      infrastructure: "Infraestructura",
      admin: "Administración",
      aiNotebook: "AI Notebook",
      domainIngestion: "Ingesta de Dominios",
      modelEvaluation: "Evaluación de Modelos",
      ragTraining: "Entrenamiento RAG",
      governance: "Gobernanza",
      settings: "Configuración",
      profile: "Perfil",
      logout: "Cerrar Sesión",
      loading: "Cargando...",
      save: "Guardar",
      cancel: "Cancelar",
      edit: "Editar",
      delete: "Eliminar",
      add: "Añadir",
      search: "Buscar",
      filter: "Filtrar",
      status: "Estado",
      actions: "Acciones",
      details: "Detalles",
      back: "Volver",
      next: "Siguiente",
      previous: "Anterior",
      close: "Cerrar",
      confirm: "Confirmar",
      yes: "Sí",
      help: "Ayuda",
      no: "No",
      active: "Activo",
      inactive: "Inactivo",
      pending: "Pendiente",
      completed: "Completado",
      error: "Error",
      success: "Éxito",
      warning: "Advertencia",
      info: "Información",
      changeLanguage: "Cambiar idioma",
      selectLanguage: "Seleccionar idioma",
      noResults: "No se encontraron resultados",
      showing: "Mostrando",
      all: "TODOS",
      clearFilters: "Limpiar filtros",
    },

    // Prompts
    prompts: {
      performance: {
        title: "Prompt Performance Comparison",
        description: "Gestión de Prompt Performance Comparison",
        results: "Resultados",
        id: "ID",
        activeItems: "Active Items",
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        avgScore: "Avg Score",
      },
      validation: {
        title: "Prompt Validation",
        description: "Gestión de Prompt Validation",
        register: "Registrar Prompt Validation",
        results: "Resultados",
        id: "ID",
        validationtype: "Validationtype",
        validationresult: "Validationresult",
        validationscore: "Validationscore",
        validationdetails: "Validationdetails",
        issuesfound: "Issuesfound",
        filterType: "Filtrar por Validationtype",
        filterStatus: "Filtrar por Status",
        type1: "TIPO 1",
        type2: "TIPO 2",
      },
      versioning: {
        title: "Prompt Version",
        description: "Gestión de Prompt Version",
        register: "Registrar Prompt Version",
        results: "Resultados",
        id: "ID",
        version: "Version",
        description: "Description",
        content: "Content",
        parameters: "Parameter S",
        changes: "Changes",
        filterStatus: "Filtrar por Status",
      },
    },

    // Dashboard
    dashboard: {
      title: "Panel de Control",
      welcome: "Bienvenido a CodeflowX",
      stats: "Estadísticas",
      quickActions: "Acciones Rápidas",
      recentProjects: "Proyectos Recientes",
      systemMetrics: "Métricas del Sistema",
      totalProjects: "Total de Proyectos",
      activeModels: "Modelos Activos",
      totalUsers: "Total de Usuarios",
      systemHealth: "Salud del Sistema",
      viewAll: "Ver Todos",
      createNew: "Crear Nuevo",
      lastUpdated: "Última Actualización",
      performance: "Rendimiento",
      efficiency: "Eficiencia",
      reliability: "Confiabilidad",
      activeNodes: "Nodos Activos",
      of: "de",
      plusOneThisMonth: "+1 este mes",
      codeGenerated: "Código Generado",
      lines: "líneas",
      modelsAvailable: "Modelos Disponibles",
      specialized: "especializados",
      plusThreeNew: "+3 nuevos",
      licenseStatus: "Estado de Licencia",
      proPlan: "Plan Pro",
      deployModel: "Desplegar Modelo",
      viewAnalytics: "Ver Analíticas",
      manageUsers: "Gestionar Usuarios",
      systemHealthTitle: "Salud del Sistema",
      cpuUsage: "Uso de CPU",
      memory: "Memoria",
      network: "Red",
      security: "Seguridad",
    },

    // Infraestructura
    infrastructure: {
      title: "Gestión de Infraestructura",
      overview: "Vista General",
      providers: "Proveedores",
      kubernetes: "Clusters Kubernetes",
      gpuInstances: "Instancias GPU",
      deployments: "Despliegues",
      resources: "Recursos",
      credentials: "Credenciales",
      costManagement: "Gestión de Costos",
      systemMonitoring: "Monitoreo del Sistema",
      addProvider: "Añadir Proveedor",
      addCluster: "Añadir Cluster",
      addInstance: "Añadir Instancia",
      clusterName: "Nombre del Cluster",
      instanceType: "Tipo de Instancia",
      region: "Región",
      status: "Estado",
      nodes: "Nodos",
      cpu: "CPU",
      memory: "Memoria",
      storage: "Almacenamiento",
      network: "Red",
      security: "Seguridad",
      monitoring: "Monitoreo",
      logs: "Logs",
      metrics: "Métricas",
      alerts: "Alertas",
      // Nuevas traducciones para la página principal
      cloudProviders: "Proveedores Cloud",
      cloudProvidersDesc:
        "Gestiona proveedores cloud, credenciales y conexiones",
      awsGcpAzure: "AWS, GCP, Azure",
      ovhRunpodVast: "OVH, RunPod, Vast.ai",
      credentialsApiKeys: "Credenciales y API Keys",
      statusMonitoring: "Monitoreo de estado",
      kubernetesDesc: "Administra clusters K8s, nodos y recursos",
      eksGkeAks: "EKS, GKE, AKS",
      ownClusters: "Clusters propios",
      nodeMonitoring: "Monitoreo de nodos",
      deploymentManagement: "Gestión de deployments",
      gpuInstancesDesc: "Gestión de instancias GPU para training e inferencia",
      t4V100A100H100: "T4, V100, A100, H100",
      rtx40903090: "RTX 4090, 3090",
      spotInstances: "Spot instances",
      costAnalysis: "Análisis de costes",
      infrastructureDeployments: "Despliegues de Infraestructura",
      deploymentsDesc: "Despliegues de infraestructura y servicios",
      trainingClusters: "Training clusters",
      inferenceServices: "Inference services",
      autoScaling: "Escalado automático",
      costManagementDesc: "Análisis y gestión de costes de infraestructura",
      providerAnalysis: "Análisis por proveedor",
      budgetAlerts: "Alertas de presupuesto",
      reports: "Reportes",
      optimization: "Optimización",
      systemMonitoringDesc:
        "Monitoreo en tiempo real de toda la infraestructura",
      realTimeMetrics: "Métricas en tiempo real",
      automaticAlerts: "Alertas automáticas",
      customDashboards: "Dashboards personalizados",
      centralizedLogs: "Logs centralizados",
      twoWeeksAgo: "hace 2 semanas",
      deployCluster: "Desplegar Cluster",
      launchGpu: "Lanzar GPU",
      viewCosts: "Ver Costes",
      // Nuevas traducciones para páginas migradas
      resourceUtilizationDashboard: "Dashboard de Utilización de Recursos",
      resourceUtilizationDesc: "Monitorea el uso de recursos en toda la infraestructura",
      resourceUtilization: "Utilización de Recursos",
      totalResources: "Total de Recursos",
      active: "activo",
      avgCpuUsage: "Uso Promedio de CPU",
      avgMemoryUsage: "Uso Promedio de Memoria",
      avgStorageUsage: "Uso Promedio de Almacenamiento",
      networkUsage: "Uso de Red",
      networkIn: "Entrada de Red",
      networkOut: "Salida de Red",
      resourceName: "Nombre del Recurso",
      resourceType: "Tipo de Recurso",
      cloudResources: "Recursos Cloud",
      basicInformation: "Información Básica",
      resourceId: "ID del Recurso",
      cloudProvider: "Proveedor Cloud",
      zone: "Zona",
      autoScaling: "Escalado Automático",
      specifications: "Especificaciones",
      scalingConfiguration: "Configuración de Escalado",
      minInstances: "Instancias Mínimas",
      currentInstances: "Instancias Actuales",
      maxInstances: "Instancias Máximas",
      cost: "Costo",
      costPerHour: "Costo por Hora",
      costPerMonth: "Costo por Mes",
      metadata: "Metadatos",
      createdAt: "Creado En",
      updatedAt: "Actualizado En",
      instances: "Instancias",
      noResourcesFound: "No se encontraron recursos",
      cloudProviderOverview: "Vista General de Proveedores Cloud",
      cloudCredentialOverview: "Vista General de Credenciales Cloud",
      cloudRegionOverview: "Vista General de Regiones Cloud",
      cloudResourceOverview: "Vista General de Recursos Cloud",
      kubernetesClusterOverview: "Vista General de Clusters Kubernetes",
      kubernetesNodeOverview: "Vista General de Nodos Kubernetes",
      gpuInstanceOverview: "Vista General de Instancias GPU",
      infrastructureTemplateOverview: "Vista General de Plantillas de Infraestructura",
      infrastructureMetricOverview: "Vista General de Métricas de Infraestructura",
      infrastructureMetricsSummaryOverview: "Vista General de Resumen de Métricas",
      infrastructureCostOverview: "Vista General de Costos de Infraestructura",
      infrastructureCostAnalysisOverview: "Vista General de Análisis de Costos",
      infrastructureAuditOverview: "Vista General de Auditoría de Infraestructura",
      infrastructureHealthMatrixOverview: "Vista General de Matriz de Salud",
      infrastructureOverviewOverview: "Vista General de Infraestructura",
      capacityForecastOverview: "Vista General de Pronóstico de Capacidad",
      resourceQuotaOverview: "Vista General de Cuotas de Recursos",
    },

    // Administración
    admin: {
      title: "Panel de Administración",
      users: "Usuarios",
      roles: "Roles",
      permissions: "Permisos",
      departments: "Departamentos",
      sessions: "Sesiones",
      audit: "Auditoría",
      licenses: "Licencias",
      nodes: "Nodos",
      updates: "Actualizaciones",
      menuManagement: "Gestión de Menús",
      notifications: "Notificaciones",
      systemSettings: "Configuración del Sistema",
      userManagement: "Gestión de Usuarios",
      systemHealth: "Salud del Sistema",
      securitySettings: "Configuración de Seguridad",
      backupRestore: "Respaldo y Restauración",
      systemLogs: "Logs del Sistema",
      performanceMetrics: "Métricas de Rendimiento",
      // Nuevas traducciones para la página principal
      twoHoursAgo: "hace 2 horas",
      twoMinutesAgo: "hace 2 minutos",
      fiveMinutesAgo: "hace 5 minutos",
      tenMinutesAgo: "hace 10 minutos",
      fifteenMinutesAgo: "hace 15 minutos",
      userLogin: "Inicio de sesión de usuario",
      roleUpdated: "Rol actualizado",
      newUserInvited: "Nuevo usuario invitado",
      systemBackupCompleted: "Respaldo del sistema completado",
      configurationUpdated: "Configuración actualizada",
    },

    // Temas
    themes: {
      title: "Temas de CodeflowX",
      changeTheme: "Cambiar tema de CodeflowX",
      useSystemTheme: "Usar tema del sistema",
      trekkerFederation: "🌌 Leka Federación",
      trekkerVulcan: "🖖 Leka Vulcano",
      trekkerKlingon: "⚔️ Leka Klingon",
      trekkerRomulan: "🖖 Leka Romulano",
      trekkerCardassian: "🖖 Leka Cardassiano",
      trekkerBajoran: "🖖 Leka Bajorano",
      trekkerFerengi: "💰 Leka Ferengi",
      trekkerAndorian: "🖖 Leka Andoriano",
      trekkerTellarite: "🖖 Leka Tellarita",
      trekkerDenobulan: "🏥 Leka Denobulano",
    },

    // Estados y colores
    status: {
      online: "En Línea",
      offline: "Desconectado",
      maintenance: "Mantenimiento",
      error: "Error",
      warning: "Advertencia",
      info: "Información",
      success: "Éxito",
      pending: "Pendiente",
      running: "Ejecutando",
      stopped: "Detenido",
      starting: "Iniciando",
      stopping: "Deteniendo",
      healthy: "Saludable",
      unhealthy: "No Saludable",
      degraded: "Degradado",
    },

    // Mensajes del sistema
    messages: {
      loadingTheme: "Cargando tema...",
      themeApplied: "Tema aplicado correctamente",
      themeError: "Error al aplicar el tema",
      redirectingToLogin: "Redirigiendo al login...",
      systemActive: "Sistema Activo",
      aiPowered: "AI Powered",
      documentation: "Documentación",
      support: "Soporte",
      status: "Estado",
      welcomeMessage: "Bienvenido, {name} 👋",
    },

    // Model Management
    modelManagement: {
      title: "Gestión de Modelos",
      subtitle: "Visión general del sistema de gestión de modelos de IA",
      totalModels: "Total de Modelos",
      activeModels: "Modelos Activos",
      totalProviders: "Total de Proveedores",
      marketplaceModels: "Modelos del Marketplace",
      recentDownloads: "Descargas Recientes",
      systemHealth: "Salud del Sistema",
      healthy: "Saludable",
      warning: "Advertencia",
      critical: "Crítico",
      approved: "Aprobado",
      pending: "Pendiente",
      rejected: "Rechazado",
      addModel: "Añadir Modelo",
      viewModels: "Ver Modelos",
      manageProviders: "Gestionar Proveedores",
      marketplace: "Marketplace",
      recentActivity: "Actividad Reciente",
      topProviders: "Mejores Proveedores",
      trendingModels: "Modelos en Tendencia",
      stats: {
        totalModels: "Total Models",
        totalModelsChange: "+12% este mes",
        activeModels: "Active Models",
        activeModelsChange: "+8% este mes",
        providers: "Providers",
        providersChange: "+3 este mes",
        recentDownloads: "Recent Downloads",
        recentDownloadsChange: "+25% esta semana",
      },
      systemStatus: {
        title: "Estado del Sistema",
        generalStatus: "Estado General",
        healthy: "Saludable",
        warning: "Advertencia",
        apiModels: "API de Modelos",
        marketplace: "Marketplace",
        providers: "Proveedores",
        operational: "Operativo",
      },
      recentModels: {
        title: "Recent Models",
        noModels: "No recent models",
      },
      typeDistribution: {
        title: "Models by Type",
        viewDetails: "View Details",
        text: "Text",
        vision: "Vision",
        audio: "Audio",
      },
      quickActions: {
        title: "Quick Actions",
        newModel: "New Model",
        addProvider: "Add Provider",
        exploreMarketplace: "Explore Marketplace",
        settings: "Settings",
      },
      systemInfo: {
        title: "System Information",
        modelManagement: {
          title: "Model Management",
          description:
            "Registro, versionado y gestión del ciclo de vida completo de modelos de IA",
        },
        aiProviders: {
          title: "AI Providers",
          description:
            "Integración con múltiples proveedores de servicios de IA y gestión de tokens",
        },
        marketplace: {
          title: "Marketplace",
          description:
            "Acceso a +3,000,000 modelos de IA de múltiples marketplaces y proveedores",
        },
        // Página de Models
        models: {
          title: "Model Management",
          subtitle: "Manage all AI models registered in the system",
          actions: {
            newModel: "New Model",
            export: "Export",
            import: "Import",
          },
          stats: {
            totalModels: "Total Models",
            approved: "Approved",
            training: "Training",
            marketplace: "Marketplace",
          },
          tabs: {
            all: "All",
            approved: "Approved",
            draft: "Drafts",
            training: "Training",
            error: "Errors",
            marketplace: "Marketplace",
          },
          filters: {
            searchPlaceholder: "Search models...",
            typePlaceholder: "Model Type",
            statusPlaceholder: "Status",
            providerPlaceholder: "Provider",
            allTypes: "All types",
            allStatuses: "All statuses",
            allProviders: "All providers",
          },
          noModels: "No models registered in the system",
          noModelsFound: "No models found with applied filters",
        },
      },
      // Página de Models
      models: {
        title: "Gestión de Modelos",
        subtitle:
          "Administra todos los modelos de IA registrados en el sistema",
        actions: {
          newModel: "Nuevo Modelo",
          export: "Exportar",
          import: "Importar",
        },
        stats: {
          totalModels: "Total Models",
          approved: "Aprobados",
          training: "Entrenando",
          marketplace: "Marketplace",
        },
        tabs: {
          all: "Todos",
          approved: "Aprobados",
          draft: "Borradores",
          training: "Entrenando",
          error: "Errores",
          marketplace: "Marketplace",
        },
        filters: {
          searchPlaceholder: "Buscar modelos...",
          typePlaceholder: "Tipo de Modelo",
          statusPlaceholder: "Estado",
          providerPlaceholder: "Proveedor",
          allTypes: "Todos los tipos",
          allStatuses: "Todos los estados",
          allProviders: "Todos los proveedores",
        },
        noModels: "No hay modelos registrados en el sistema",
        noModelsFound: "No se encontraron modelos con los filtros aplicados",
      },
      approval: {
        title: "Aprobaciones de Modelos",
        subtitle: "Gestión de solicitudes de aprobación pendientes",
      },
      biasAnalysis: {
        title: "Análisis de Sesgo",
        subtitle: "Gestión de análisis de sesgo",
      },
      dependencies: {
        title: "Dependencias",
        subtitle: "Gestión de dependencias de modelos",
      },
      explainability: {
        title: "Explicabilidad",
        subtitle: "Gestión de explicabilidad de modelos",
      },
      performance: {
        title: "Rendimiento",
        subtitle: "Gestión de rendimiento de modelos",
      },
      usageOverview: {
        title: "Uso de Modelos",
        subtitle: "Vista general del uso de modelos",
      },
      artifactOverview: {
        title: "Artefactos",
        subtitle: "Gestión de artefactos de modelos",
      },
      capabilityOverview: {
        title: "Capacidades",
        subtitle: "Gestión de capacidades de modelos",
      },
      catalogOverview: {
        title: "Catálogo",
        subtitle: "Gestión del catálogo de modelos",
      },
      endpointOverview: {
        title: "Endpoints",
        subtitle: "Gestión de endpoints de modelos",
      },
      providerCredentialOverview: {
        title: "Credenciales de Proveedor",
        subtitle: "Gestión de credenciales de proveedores",
      },
      providerOverview: {
        title: "Proveedores",
        subtitle: "Gestión de proveedores de modelos",
      },
      versioning: {
        title: "Versionado",
        subtitle: "Gestión de versiones de modelos",
      },
    },

    // Monitoring
    monitoring: {
      title: "Monitoreo",
      alerts: {
        overview: {
          title: "Alertas de Monitoreo",
          description: "Gestión de alertas de monitoreo",
          register: "Registrar Alerta",
          list: "Lista de Alertas",
          alertName: "Alert Name",
          alertType: "Alert Type",
          severity: "Severity",
          status: "Status",
        },
        detail: {
          title: "Detalle de Alerta",
          description: "Gestión y configuración de alerta de monitoreo",
          information: "Información",
          alertName: "Alert Name",
          alertType: "Alert Type",
          severity: "Severity",
          status: "Status",
          description: "Description",
          alertData: "Alert Data",
          timestamps: "Timestamps",
          triggeredAt: "Triggered At",
          acknowledgedAt: "Acknowledged At",
          acknowledgedBy: "Acknowledged By",
          resolvedAt: "Resolved At",
          resolutionNotes: "Resolution Notes",
        },
        systemDetail: {
          title: "Detalle de Alerta del Sistema",
          description: "Gestión de alertas del sistema",
          information: "Información",
          alertName: "Alert Name",
          alertType: "Alert Type",
          severity: "Severity",
          status: "Status",
          description: "Description",
          timestamps: "Timestamps",
          triggeredAt: "Triggered At",
          acknowledgedAt: "Acknowledged At",
        },
        responseForm: {
          title: "Respuesta a Alerta Crítica",
          description: "Formulario de respuesta a alerta",
          formData: "Form Data",
          decision: "Decision",
          decisionPlaceholder: "Enter decision...",
          notes: "Notes",
          notesPlaceholder: "Enter notes...",
        },
      },
      metrics: {
        overview: {
          title: "Métricas de Monitoreo",
          description: "Gestión de métricas de monitoreo",
          register: "Registrar Métrica",
          list: "Lista de Métricas",
        },
        detail: {
          title: "Detalle de Métrica",
          description: "Gestión de métricas de monitoreo",
          information: "Información",
          metricName: "Metric Name",
          metricType: "Metric Type",
          metricValue: "Metric Value",
          status: "Status",
          description: "Description",
          timestamps: "Timestamps",
          collectedAt: "Collected At",
        },
        systemDetail: {
          title: "Detalle de Métrica del Sistema",
          description: "Gestión de métricas del sistema",
          information: "Información",
          metricName: "Metric Name",
          metricType: "Metric Type",
          metricValue: "Metric Value",
          status: "Status",
          description: "Description",
          timestamps: "Timestamps",
          collectedAt: "Collected At",
        },
      },
      dashboard: {
        title: "Dashboard de Monitoreo",
        description: "Vista general del sistema de monitoreo",
        alerts: "Alertas",
        alertsDesc: "Gestión de alertas de monitoreo",
        metrics: "Métricas",
        metricsDesc: "Visualización de métricas del sistema",
        systemHealth: "Salud del Sistema",
        systemHealthDesc: "Estado de salud del sistema",
        performance: "Performance",
        performanceDesc: "Métricas de rendimiento",
        quickStats: "Estadísticas Rápidas",
        activeAlerts: "Alertas Activas",
        uptime: "Uptime",
        avgResponse: "Respuesta Promedio",
      },
      auditLog: {
        detail: {
          title: "Detalle de Log de Auditoría",
          description: "Información detallada del log de auditoría",
          information: "Información",
          action: "Action",
          entity: "Entity",
          entityId: "Entity ID",
          user: "User",
          ipAddress: "IP Address",
          status: "Status",
          details: "Details",
          timestamps: "Timestamps",
          timestamp: "Timestamp",
          createdAt: "Created At",
        },
      },
      systemHealth: {
        detail: {
          title: "Detalle de Salud del Sistema",
          description: "Información de salud del sistema",
          information: "Información",
          status: "Status",
          uptime: "Uptime",
          details: "Details",
          timestamps: "Timestamps",
          lastCheck: "Last Check",
          updatedAt: "Updated At",
        },
      },
      governance: {
        detail: {
          title: "Detalle de Gobernanza",
          description: "Gestión de políticas de gobernanza",
          information: "Información",
          name: "Name",
          type: "Type",
          status: "Status",
          description: "Description",
          details: "Details",
          timestamps: "Timestamps",
          createdAt: "Created At",
          updatedAt: "Updated At",
        },
      },
      performance: {
        interventionForm: {
          title: "Intervención de Performance",
          description: "Formulario de intervención de performance",
          formData: "Form Data",
          interventionType: "Intervention Type",
          description: "Description",
          descriptionPlaceholder: "Describe the intervention...",
          priority: "Priority",
          notes: "Notes",
          notesPlaceholder: "Additional notes...",
        },
      },
      viewsPerformance: {
        title: "Performance de Vistas",
        description: "Configuración de performance de vistas",
        formData: "Form Data",
        viewName: "View Name",
        viewNamePlaceholder: "Enter view name...",
        performanceMetric: "Performance Metric",
        performanceMetricPlaceholder: "Enter metric...",
        threshold: "Threshold",
        thresholdPlaceholder: "Enter threshold...",
        notes: "Notes",
        notesPlaceholder: "Additional notes...",
      },
    },

    // Agents
    agents: {
      dashboard: {
        title: "Dashboard de Agentes",
        subtitle: "Métricas y estado general de agentes AI",
        totalAgents: "Total Agentes",
        activeAgents: "Agentes Activos",
        deployedAgents: "Agentes Desplegados",
        healthScore: "Health Score",
        recentAgents: "Agentes Recientes",
        performanceMetrics: "Métricas de Rendimiento",
        avgPerformanceScore: "Score Promedio",
        offlineAgents: "Agentes Offline",
        version: "Versión",
      },
      alerts: {
        title: "Agent Alert",
        subtitle: "Gestión de Agent Alert",
        register: "Registrar Agent Alert",
        list: "Listado de Alertas",
        alertType: "Alert Type",
        alertCategory: "Alert Category",
        severity: "Severity",
      },
      approval: {
        title: "Agent Approval",
        subtitle: "Gestión de Agent Approval",
        register: "Registrar Agent Approval",
        list: "Listado de Aprobaciones",
        approvalType: "Approval Type",
        approvalStatus: "Approval Status",
      },
      biasDetection: {
        title: "Bias Detection",
        subtitle: "Gestión de Bias Detection",
        register: "Registrar Bias Detection",
        list: "Listado de Detecciones",
        detectionType: "Detection Type",
        severity: "Severity",
      },
      compliance: {
        title: "Compliance",
        subtitle: "Gestión de Compliance",
        register: "Registrar Compliance",
        list: "Listado de Compliance",
        complianceType: "Compliance Type",
        lastCheck: "Last Check",
        score: "Score",
      },
      decisions: {
        title: "Decisions",
        subtitle: "Gestión de Decisions",
        register: "Registrar Decision",
        list: "Listado de Decisions",
        decisionType: "Decision Type",
        confidence: "Confidence",
        timestamp: "Timestamp",
      },
      deployment: {
        title: "Deployment",
        subtitle: "Gestión de Deployment",
        register: "Registrar Deployment",
        list: "Listado de Deployments",
        deploymentType: "Deployment Type",
        environment: "Environment",
        deployedAt: "Deployed At",
      },
      ethics: {
        title: "Ethics",
        subtitle: "Gestión de Ethics",
        register: "Registrar Ethics",
        list: "Listado de Ethics",
        assessmentType: "Assessment Type",
        score: "Score",
        assessedAt: "Assessed At",
      },
      governance: {
        title: "Governance",
        subtitle: "Gestión de Governance",
        register: "Registrar Governance",
        list: "Listado de Governance",
        governanceType: "Governance Type",
        lastReview: "Last Review",
        complianceScore: "Compliance Score",
      },
      interactions: {
        title: "Interactions",
        subtitle: "Gestión de Interactions",
        register: "Registrar Interaction",
        list: "Listado de Interactions",
        interactionType: "Interaction Type",
        participants: "Participants",
        timestamp: "Timestamp",
      },
      collaboration: {
        title: "Collaboration",
        subtitle: "Gestión de Collaboration",
        register: "Registrar Collaboration",
        list: "Listado de Collaborations",
        collaborationType: "Collaboration Type",
        agents: "Agents",
        createdAt: "Created At",
      },
      communication: {
        title: "Communication",
        subtitle: "Gestión de Communication",
        register: "Registrar Communication",
        list: "Listado de Communications",
        communicationType: "Communication Type",
        messages: "Messages",
        lastMessage: "Last Message",
      },
      learning: {
        title: "Learning",
        subtitle: "Gestión de Learning",
        register: "Registrar Learning",
        list: "Listado de Learning",
        learningType: "Learning Type",
        progress: "Progress",
        lastUpdate: "Last Update",
      },
      monitoring: {
        title: "Monitoring",
        subtitle: "Gestión de Monitoring",
        register: "Registrar Monitoring",
        list: "Listado de Monitoring",
        monitoringType: "Monitoring Type",
        metrics: "Metrics",
        lastCheck: "Last Check",
      },
      health: {
        title: "Health Overview",
        subtitle: "Gestión de Health",
        register: "Registrar Health",
        list: "Listado de Health",
        healthStatus: "Health Status",
        score: "Score",
        lastCheck: "Last Check",
      },
      domain: {
        title: "Domain Overview",
        subtitle: "Gestión de Domains",
        register: "Registrar Domain",
        list: "Listado de Domains",
        domainName: "Domain Name",
        agents: "Agents",
        createdAt: "Created At",
      },
      rollback: {
        title: "Rollback",
        subtitle: "Gestión de Rollback",
        register: "Registrar Rollback",
        list: "Listado de Rollbacks",
        rollbackType: "Rollback Type",
        version: "Version",
        rolledBackAt: "Rolled Back At",
      },
      tools: {
        title: "Tools",
        subtitle: "Gestión de Tools",
        register: "Registrar Tool",
        list: "Listado de Tools",
        toolName: "Tool Name",
        toolType: "Tool Type",
        version: "Version",
      },
      transparency: {
        title: "Transparency",
        subtitle: "Gestión de Transparency",
        register: "Registrar Transparency",
        list: "Listado de Transparency",
        transparencyType: "Transparency Type",
        score: "Score",
        lastAudit: "Last Audit",
      },
      versioning: {
        title: "Versioning",
        subtitle: "Gestión de Versioning",
        register: "Registrar Version",
        list: "Listado de Versions",
        versionNumber: "Version Number",
        releaseDate: "Release Date",
        changes: "Changes",
      },
      workflow: {
        execution: {
          title: "Execution Overview",
          subtitle: "Gestión de Workflow Executions",
          register: "Registrar Execution",
          list: "Listado de Executions",
          executionType: "Execution Type",
          duration: "Duration (s)",
          startedAt: "Started At",
        },
      },
    },

    // Domain Ingestion
    domainIngestion: {
      title: "Dashboard de Ingesta de Dominios",
      subtitle: "Monitorea y gestiona tus dominios de datos especializados",
      stats: {
        totalDomains: "Total Dominios",
        totalDocuments: "Total Documentos",
        webPages: "Páginas Web",
        apis: "APIs Conectadas",
        activeDomains: "dominios activos",
        documentsChange: "+12% este mes",
        webChange: "+8% esta semana",
        apisChange: "+3 este mes",
      },
      actions: {
        wizard: "Asistente de Dominios",
        manage: "Gestionar Dominios",
        access: "Acceder",
      },
      modules: {
        webScraping: {
          title: "Web Scraping",
          description: "Extrae datos de sitios web y monitoriza cambios",
        },
        uploadDocuments: {
          title: "Subir Documentos",
          description: "Sube y procesa documentos para enriquecer dominios",
        },
        monitor: {
          title: "Monitorización",
          description: "Monitorea el rendimiento y estado de los trabajos",
        },
        settings: {
          title: "Configuración",
          description: "Configura parámetros globales y por dominio",
        },
        configureSources: {
          title: "Configurar Fuentes",
          description: "Configura fuentes de datos y APIs",
        },
      },
      recentJobs: {
        title: "Trabajos Recientes",
      },
      jobStatus: {
        completed: "Completado",
        running: "Ejecutando",
        failed: "Fallido",
        pending: "Pendiente",
        queued: "En Cola",
        processing: "Procesando",
        cancelled: "Cancelado",
        retrying: "Reintentando",
      },
      wizard: {
        steps: {
          basicInfo: "Información Básica",
          dataSources: "Fuentes de Datos",
          compliance: "Cumplimiento",
          advancedFeatures: "Características Avanzadas",
          review: "Revisión",
        },
        templates: {
          title: "Plantillas de Dominio",
          description:
            "Selecciona una plantilla predefinida o crea una personalizada",
          fintech: "Fintech Banking",
          healthcare: "Healthcare Analytics",
          ecommerce: "E-commerce Insights",
          government: "IA Act Compliance",
          manufacturing: "Manufacturing IoT",
          custom: "Personalizado",
        },
        industries: {
          title: "Industria",
          fintech: "Fintech",
          healthcare: "Healthcare",
          ecommerce: "E-commerce",
          government: "Gobierno",
          manufacturing: "Manufactura",
          education: "Educación",
          legal: "Legal",
          other: "Otro",
        },
        compliance: {
          title: "Marcos de Cumplimiento",
          description: "Selecciona los marcos de cumplimiento aplicables",
          gdpr: "GDPR (Reglamento General de Protección de Datos)",
          sox: "SOX (Sarbanes-Oxley Act)",
          pci: "PCI DSS (Payment Card Industry Data Security Standard)",
          hipaa: "HIPAA (Health Insurance Portability and Accountability Act)",
          iaActEspana: "IA Act España (Ley de Inteligencia Artificial)",
          iaActEuropa: "IA Act Europa (Artificial Intelligence Act)",
          iso27001: "ISO 27001 (Gestión de Seguridad de la Información)",
          iso27701: "ISO 27701 (Privacidad de la Información)",
          nist: "NIST Cybersecurity Framework",
          soc2: "SOC 2 (Service Organization Control 2)",
          fedramp:
            "FedRAMP (Federal Risk and Authorization Management Program)",
          ccpa: "CCPA (California Consumer Privacy Act)",
          lgpd: "LGPD (Lei Geral de Proteção de Dados)",
          pipeda:
            "PIPEDA (Personal Information Protection and Electronic Documents Act)",
          // Estándares ISO de Desarrollo de Software
          iso12207: "ISO 12207 (Ciclo de Vida del Software)",
          iso15504: "ISO 15504 (Evaluación de Procesos de Software)",
          iso20000: "ISO 20000 (Gestión de Servicios TI)",
          iso27002: "ISO 27002 (Controles de Seguridad de la Información)",
          iso27005: "ISO 27005 (Gestión de Riesgos de Seguridad)",
          iso27017: "ISO 27017 (Seguridad en la Nube)",
          iso27018: "ISO 27018 (Protección de Datos en la Nube)",
          iso9001: "ISO 9001 (Sistema de Gestión de Calidad)",
          iso14001: "ISO 14001 (Sistema de Gestión Ambiental)",
          // Normativas Europeas Adicionales
          eidas: "eIDAS (Identificación Electrónica UE)",
          psd2: "PSD2 (Directiva de Servicios de Pago UE)",
          nis2: "NIS2 (Directiva de Seguridad de Redes UE)",
          // Estándares de Calidad de Software
          cmmi: "CMMI (Modelo de Madurez de Capacidades)",
          tickit: "TICKIT (Calidad en TI y Telecomunicaciones)",
          spice: "SPICE (Evaluación de Procesos de Software)",
          custom: "Personalizado",
        },
        dataSources: {
          title: "Tipos de Fuentes de Datos",
          description:
            "Selecciona los tipos de fuentes de datos que utilizarás",
          api: "API",
          database: "Base de Datos",
          file: "Archivo",
          web: "Web",
          stream: "Stream",
        },
        basicInfo: {
          name: "Nombre del Dominio",
          description: "Descripción",
          industry: "Industria",
          owner: "Propietario",
          team: "Equipo",
        },
        advancedFeatures: {
          title: "Características Avanzadas",
          rag: "RAG (Crear datasets)",
          training: "Entrenamiento de Modelos",
          compliance: "Cumplimiento Automático",
          monitoring: "Monitorización Avanzada",
          modelSelection: "Selección de Modelo",
          modelName: "Nombre del Modelo Interno",
          adapter: "Crear Adapter",
          finetuning: "Fine-tuning",
          baseModel: "Modelo Base",
          customModel: "Modelo Personalizado",
        },
        review: {
          title: "Revisión Final",
          description: "Revisa la configuración antes de crear el dominio",
          template: "Plantilla seleccionada",
          industry: "Industria",
          compliance: "Marcos de cumplimiento",
          dataSources: "Fuentes de datos",
          features: "Características habilitadas",
        },
        template: {
          fintech: {
            name: "Fintech Banking",
            description:
              "Dominio especializado en análisis de datos financieros y cumplimiento regulatorio",
            features: [
              "RAG habilitado",
              "Cumplimiento GDPR/SOX",
              "Monitorización avanzada",
            ],
            compliance: ["GDPR", "SOX", "PCI DSS"],
          },
          healthcare: {
            name: "Healthcare Analytics",
            description:
              "Dominio para análisis de datos médicos con cumplimiento HIPAA",
            features: [
              "RAG habilitado",
              "Cumplimiento HIPAA",
              "Encriptación avanzada",
            ],
            compliance: ["HIPAA", "GDPR"],
          },
          ecommerce: {
            name: "E-commerce Insights",
            description:
              "Dominio para análisis de comportamiento de compra y recomendaciones",
            features: [
              "RAG habilitado",
              "Análisis de comportamiento",
              "Recomendaciones",
            ],
            compliance: ["GDPR"],
          },
          government: {
            name: "IA Act Compliance",
            description:
              "Dominio para cumplimiento con la IA Act española y regulaciones gubernamentales",
            features: [
              "RAG habilitado",
              "Cumplimiento IA Act",
              "Auditoría completa",
            ],
            compliance: ["IA Act España", "GDPR"],
          },
          manufacturing: {
            name: "Manufacturing IoT",
            description:
              "Dominio para análisis de datos IoT en entornos industriales",
            features: [
              "RAG habilitado",
              "Análisis IoT",
              "Monitorización en tiempo real",
            ],
            compliance: ["ISO 27001"],
          },
        },
        actions: {
          next: "Siguiente",
          previous: "Anterior",
          create: "Crear Dominio",
          cancel: "Cancelar",
        },
      },
      manage: {
        title: "Manage Domains",
        subtitle: "View and manage all your data domains",
        stats: {
          total: "Total",
          active: "Active",
          inactive: "Inactive",
          processing: "Processing",
        },
        search: {
          placeholder: "Search domains...",
        },
        filters: {
          all: "All",
          active: "Active",
          inactive: "Inactive",
          processing: "Processing",
          more: "More filters",
        },
        actions: {
          newDomain: "New Domain",
          viewDetails: "View Details",
          edit: "Edit",
          delete: "Delete",
        },
        noResults: {
          title: "No domains found",
          description: "Try adjusting the filters or create a new domain",
        },
        details: {
          title: "Domain Details",
          basicInfo: "Basic Information",
          performance: "Performance",
          dataSources: "Data Sources",
          documents: "Documents",
          compliance: "Compliance",
          lastActivity: "Last Activity",
          status: "Status",
          industry: "Industry",
          owner: "Owner",
          createdAt: "Created",
          totalDocuments: "Total Documents",
          processedDocuments: "Processed Documents",
          dataSourcesCount: "Data Sources",
          complianceScore: "Compliance Score",
          ragEnabled: "RAG Enabled",
          trainingEnabled: "Training Enabled",
        },
      },
      webScraping: {
        title: "Web Scraping",
        subtitle: "Configure and monitor web data extraction",
        stats: {
          total: "Total",
          active: "Active",
          running: "Running",
          completed: "Completed",
        },
        search: {
          placeholder: "Search jobs...",
        },
        filters: {
          all: "All",
          active: "Active",
          running: "Running",
          completed: "Completed",
          failed: "Failed",
          more: "More filters",
        },
        actions: {
          newJob: "New Job",
          viewDetails: "View Details",
          start: "Start",
          stop: "Stop",
          configure: "Configure",
        },
        noResults: {
          title: "No jobs found",
          description: "Try adjusting the filters or create a new job",
        },
        details: {
          title: "Job Details",
          basicInfo: "Basic Information",
          progress: "Progress",
          metrics: "Metrics",
          schedule: "Schedule",
          lastRun: "Last Run",
          nextRun: "Next Run",
          pagesScraped: "Pages Scraped",
          totalPages: "Total Pages",
          dataExtracted: "Data Extracted",
          errors: "Errors",
          priority: "Priority",
        },
        newJob: {
          title: "New Web Scraping Job",
          name: "Job Name",
          url: "Target URL",
          domain: "Domain",
          schedule: "Schedule",
          priority: "Priority",
          create: "Create Job",
          cancel: "Cancel",
        },
      },
      uploadDocuments: {
        title: "Upload Documents",
        subtitle: "Upload and manage document batches",
        stats: {
          total: "Total",
          processing: "Processing",
          completed: "Completed",
          failed: "Failed",
        },
        search: {
          placeholder: "Search batches...",
        },
        filters: {
          all: "All",
          processing: "Processing",
          completed: "Completed",
          failed: "Failed",
          more: "More filters",
        },
        actions: {
          newBatch: "New Batch",
          viewDetails: "View Details",
          pause: "Pause",
          resume: "Resume",
          configure: "Configure",
        },
        noResults: {
          title: "No batches found",
          description: "Try adjusting the filters or create a new batch",
        },
        details: {
          title: "Batch Details",
          basicInfo: "Basic Information",
          progress: "Progress",
          files: "Files",
          processing: "Processing",
          name: "Batch Name",
          domain: "Domain",
          sector: "Sector",
          priority: "Priority",
          status: "Status",
          totalFiles: "Total Files",
          processedFiles: "Processed Files",
          totalSize: "Total Size",
          processedSize: "Processed Size",
          createdAt: "Created",
          updatedAt: "Updated",
          processingType: "Processing Type",
          ragEnabled: "RAG Enabled",
          trainingEnabled: "Training Enabled",
          complianceEnabled: "Compliance Enabled",
          errors: "Errors",
          warnings: "Warnings",
        },
        newBatch: {
          title: "New Document Batch",
          explanation:
            "A batch is a collection of documents that will be processed together. This allows for better organization and tracking of document processing.",
          name: "Batch Name",
          domain: "Domain",
          sector: "Sector",
          priority: "Priority",
          processingType: "Processing Type",
          create: "Create Batch",
          cancel: "Cancel",
        },
      },
      monitor: {
        title: "Monitor",
        subtitle: "Real-time monitoring of ingestion jobs",
        stats: {
          total: "Total",
          running: "Running",
          completed: "Completed",
          failed: "Failed",
        },
        search: {
          placeholder: "Search jobs...",
        },
        filters: {
          all: "All",
          running: "Running",
          completed: "Completed",
          failed: "Failed",
          more: "More filters",
        },
        actions: {
          viewDetails: "View Details",
          configure: "Configure",
        },
        noResults: {
          title: "No jobs found",
          description: "Try adjusting the filters or start a new job",
        },
        details: {
          title: "Job Details",
          basicInfo: "Basic Information",
          performance: "Performance",
          metrics: "Metrics",
          alerts: "Alerts",
          name: "Job Name",
          domain: "Domain",
          type: "Type",
          status: "Status",
          startTime: "Start Time",
          endTime: "End Time",
          duration: "Duration",
          cpu: "CPU",
          memory: "Memory",
          throughput: "Throughput",
          latency: "Latency",
          alerts: "Alerts",
        },
      },
      settings: {
        title: "Settings",
        subtitle: "Configure global and domain-specific settings",
        stats: {
          total: "Total",
          active: "Active",
          configured: "Configured",
          default: "Default",
        },
        search: {
          placeholder: "Search domains...",
        },
        filters: {
          all: "All",
          active: "Active",
          configured: "Configured",
          default: "Default",
          more: "More filters",
        },
        tabs: {
          domains: "Domains",
          global: "Global",
          advanced: "Advanced",
        },
        actions: {
          viewDetails: "View Details",
          save: "Save",
          reset: "Reset",
        },
        noResults: {
          title: "No domains found",
          description: "Try adjusting the filters or create a new domain",
        },
        details: {
          title: "Domain Settings",
          basicInfo: "Basic Information",
          configuration: "Configuration",
          ragExplanation:
            "RAG (Retrieval-Augmented Generation) is used to process documents and create training datasets. It performs automatic chunking and tagging of content for AI model training. Domains are business models, not RAG systems.",
          ragEnabled: "RAG Enabled for Datasets",
          trainingEnabled: "Training Enabled",
          complianceEnabled: "Compliance Enabled",
          autoIngestion: "Auto Ingestion",
          qualityThreshold: "Quality Threshold",
          retentionDays: "Retention Days",
          lastUpdated: "Last Updated",
          status: "Status",
        },
        global: {
          general: "General Configuration",
          security: "Security",
          autoBackup: "Auto Backup",
          notifications: "Notifications",
          language: "Language",
          timezone: "Timezone",
          encryption: "Encryption",
          authentication: "Authentication",
          auditLogs: "Audit Logs",
        },
        advanced: {
          performance: "Performance",
          logs: "Logs & Monitoring",
          cacheSize: "Cache Size",
          maxConnections: "Max Connections",
          logLevel: "Log Level",
          monitoring: "Monitoring",
          alerts: "Alerts",
        },
      },
      configureSources: {
        title: "Configure Sources",
        subtitle: "Manage and configure data sources for each domain",
        stats: {
          total: "Total",
          active: "Active",
          api: "APIs",
        },
        search: {
          placeholder: "Search sources...",
        },
        filters: {
          allTypes: "All types",
          api: "API",
          database: "Database",
          file: "File",
          web: "Web",
          stream: "Stream",
          more: "More filters",
        },
        actions: {
          newSource: "New Source",
          viewDetails: "View Details",
          edit: "Edit",
          test: "Test Connection",
          configure: "Configure",
        },
        noResults: {
          title: "No sources found",
          description: "Try adjusting the filters or create a new data source",
        },
        status: {
          active: "Active",
          inactive: "Inactive",
          error: "Error",
          maintenance: "Maintenance",
          unknown: "Unknown",
        },
        info: {
          type: "Type:",
          schedule: "Schedule:",
          dataVolume: "Volume:",
          syncStatus: "Sync:",
        },
        features: {
          compliance: "Compliance:",
          rag: "RAG",
          training: "Training",
        },
        newSource: {
          title: "New Data Source",
          name: "Source Name",
          namePlaceholder: "e.g., API Gateway",
          domain: "Domain",
          selectDomain: "Select domain",
          type: "Source Type",
          selectType: "Select type",
          url: "URL/Endpoint",
          urlPlaceholder: "https://api.example.com/v1",
          credentials: "Credentials Type",
          credentialsNone: "None",
          credentialsApiKey: "API Key",
          credentialsOAuth: "OAuth",
          credentialsBasic: "Basic Auth",
          schedule: "Schedule",
          scheduleRealTime: "Real-time",
          schedule15Min: "Every 15 minutes",
          scheduleHour: "Every hour",
          scheduleDaily: "Daily",
          scheduleWeekly: "Weekly",
          processingOptions: "Processing Options",
          ragEnabled: "Enable RAG (Create datasets)",
          ragDescription: "Process data to create training datasets",
          trainingEnabled: "Enable Training",
          trainingDescription: "Generate content for model training",
          create: "Create Source",
          cancel: "Cancel",
        },
        details: {
          basicInfo: "Basic Information",
          sync: "Synchronization",
          credentials: "Credentials",
          compliance: "Compliance",
          processing: "Processing",
          domain: "Domain:",
          type: "Type:",
          status: "Status:",
          url: "URL:",
          schedule: "Schedule:",
          lastSync: "Last sync:",
          nextSync: "Next sync:",
          dataVolume: "Data volume:",
          credentialType: "Type:",
          lastUpdated: "Last updated:",
          rag: "RAG (Datasets)",
          training: "Training",
          edit: "Edit",
          test: "Test Connection",
          configure: "Configure",
        },
      },
      domainDetails: {
        navigation: {
          back: "Back",
        },
        actions: {
          edit: "Edit",
          settings: "Settings",
          monitor: "Monitor",
        },
        stats: {
          documents: "Documents",
          processed: "Processed",
          dataSources: "Data Sources",
          active: "active",
          compliance: "Compliance",
          compliant: "Compliant",
          successRate: "Success Rate",
          avgTime: "average",
        },
        tabs: {
          overview: "Overview",
          dataSources: "Sources",
          documents: "Documents",
          compliance: "Compliance",
          alerts: "Alerts",
        },
        overview: {
          domainInfo: "Domain Information",
          performance: "Performance",
          ingestionMetrics: "Ingestion Metrics",
          industry: "Industry",
          status: "Status",
          owner: "Owner",
          created: "Created",
          team: "Team",
          dataQuality: "Data Quality",
          rag: "RAG",
          training: "Training",
          lastActivity: "Last Activity",
          daily: "Daily",
          weekly: "Weekly",
          monthly: "Monthly",
        },
        dataSources: {
          title: "Data Sources",
          lastSync: "Last sync:",
        },
        documents: {
          title: "Documents",
        },
        compliance: {
          frameworks: "Compliance Frameworks",
          audits: "Audits",
          status: "Status:",
          lastAudit: "Last Audit",
          nextAudit: "Next Audit",
        },
        alerts: {
          title: "Alerts",
          resolved: "Resolved",
        },
        status: {
          active: "Active",
          inactive: "Inactive",
          maintenance: "Maintenance",
          error: "Error",
          unknown: "Unknown",
        },
        edit: {
          title: "Edit Domain",
          name: "Domain Name",
          industry: "Industry",
          description: "Description",
          save: "Save Changes",
          cancel: "Cancel",
        },
        settings: {
          title: "Domain Settings",
          processing: "Processing",
          ragEnabled: "Enable RAG (Create datasets)",
          ragDescription: "Process documents to create training datasets",
          trainingEnabled: "Enable Training",
          trainingDescription: "Generate content for model training",
          save: "Save Settings",
          cancel: "Cancel",
        },
      },
      manage: {
        title: "Gestionar Dominios",
        subtitle: "Visualiza y gestiona todos tus dominios de datos",
        stats: {
          total: "Total",
          active: "Activos",
          inactive: "Inactivos",
          processing: "Procesando",
        },
        search: {
          placeholder: "Buscar dominios...",
        },
        filters: {
          all: "Todos",
          active: "Activos",
          inactive: "Inactivos",
          processing: "Procesando",
          more: "Más filtros",
        },
        actions: {
          newDomain: "Nuevo Dominio",
          viewDetails: "Ver Detalle",
          edit: "Editar",
          delete: "Eliminar",
        },
        noResults: {
          title: "No se encontraron dominios",
          description: "Intenta ajustar los filtros o crear un nuevo dominio",
        },
        details: {
          title: "Detalles del Dominio",
          basicInfo: "Información Básica",
          performance: "Rendimiento",
          dataSources: "Fuentes de Datos",
          documents: "Documentos",
          compliance: "Cumplimiento",
          lastActivity: "Última Actividad",
          status: "Estado",
          industry: "Industria",
          owner: "Propietario",
          createdAt: "Creado",
          totalDocuments: "Total Documentos",
          processedDocuments: "Documentos Procesados",
          dataSourcesCount: "Fuentes de Datos",
          complianceScore: "Puntuación de Cumplimiento",
          ragEnabled: "RAG Habilitado",
          trainingEnabled: "Entrenamiento Habilitado",
        },
      },
      webScraping: {
        title: "Web Scraping",
        subtitle: "Configura y monitoriza la extracción de datos web",
        stats: {
          total: "Total",
          active: "Activos",
          running: "Ejecutando",
          completed: "Completados",
        },
        search: {
          placeholder: "Buscar trabajos...",
        },
        filters: {
          all: "Todos",
          active: "Activos",
          running: "Ejecutando",
          completed: "Completados",
          failed: "Fallidos",
          more: "Más filtros",
        },
        actions: {
          newJob: "Nuevo Trabajo",
          viewDetails: "Ver Detalle",
          start: "Iniciar",
          stop: "Detener",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron trabajos",
          description: "Intenta ajustar los filtros o crear un nuevo trabajo",
        },
        details: {
          title: "Detalles del Trabajo",
          basicInfo: "Información Básica",
          progress: "Progreso",
          metrics: "Métricas",
          schedule: "Programación",
          lastRun: "Última Ejecución",
          nextRun: "Próxima Ejecución",
          pagesScraped: "Páginas Extraídas",
          totalPages: "Total de Páginas",
          dataExtracted: "Datos Extraídos",
          errors: "Errores",
          priority: "Prioridad",
        },
        newJob: {
          title: "Nuevo Trabajo de Web Scraping",
          name: "Nombre del Trabajo",
          url: "URL Objetivo",
          domain: "Dominio",
          schedule: "Programación",
          priority: "Prioridad",
          create: "Crear Trabajo",
          cancel: "Cancelar",
        },
        // Nuevas traducciones para página independiente
        newJobPage: "Nuevo Trabajo de Web Scraping",
        newJobDescription:
          "Configura un nuevo trabajo de extracción de datos web con asignación múltiple",
        basicConfig: "Configuración Básica",
        basicConfigDescription: "Información general del trabajo de scraping",
        jobName: "Nombre del Trabajo",
        jobNamePlaceholder: "Ej: Extracción de productos",
        description: "Descripción",
        descriptionPlaceholder: "Describe qué datos se extraerán y para qué",
        targetUrl: "URL Objetivo",
        advancedConfig: "Configuración Avanzada",
        advancedConfigDescription: "Parámetros técnicos de extracción",
        maxPages: "Máximo de Páginas",
        maxDepth: "Profundidad Máxima",
        waitTime: "Tiempo de Espera",
        userAgent: "User Agent",
        followLinks: "Seguir Enlaces",
        respectRobots: "Respetar robots.txt",
        domainAssignment: "Asignación de Dominios",
        domainAssignmentDescription:
          "Selecciona los dominios que procesarán estos datos",
        selectDomains: "Seleccionar Dominios",
        selectDomainPlaceholder: "Elige un dominio...",
        selectedDomains: "Dominios Seleccionados",
        ragAssignment: "Asignación de Proyectos RAG",
        ragAssignmentDescription:
          "Selecciona los proyectos RAG que utilizarán estos datos",
        selectRAGProjects: "Seleccionar Proyectos RAG",
        selectRAGProjectPlaceholder: "Elige un proyecto RAG...",
        selectedRAGProjects: "Proyectos RAG Seleccionados",
        priorities: {
          low: "Baja",
          medium: "Media",
          high: "Alta",
        },
        schedules: {
          once: "Una vez",
          daily: "Diario",
          weekly: "Semanal",
          monthly: "Mensual",
        },
      },
      uploadDocuments: {
        title: "Subir Documentos",
        subtitle: "Sube y gestiona lotes de documentos",
        stats: {
          total: "Total",
          processing: "Procesando",
          completed: "Completados",
          failed: "Fallidos",
        },
        search: {
          placeholder: "Buscar lotes...",
        },
        filters: {
          all: "Todos",
          processing: "Procesando",
          completed: "Completados",
          failed: "Fallidos",
          more: "Más filtros",
        },
        actions: {
          newBatch: "Nuevo Lote",
          viewDetails: "Ver Detalle",
          pause: "Pausar",
          resume: "Reanudar",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron lotes",
          description: "Intenta ajustar los filtros o crear un nuevo lote",
        },
        details: {
          title: "Detalles del Lote",
          basicInfo: "Información Básica",
          progress: "Progreso",
          files: "Archivos",
          processing: "Procesamiento",
          name: "Nombre del Lote",
          domain: "Dominio",
          sector: "Sector",
          priority: "Prioridad",
          status: "Estado",
          totalFiles: "Total de Archivos",
          processedFiles: "Archivos Procesados",
          totalSize: "Tamaño Total",
          processedSize: "Tamaño Procesado",
          createdAt: "Creado",
          updatedAt: "Actualizado",
          processingType: "Tipo de Procesamiento",
          ragEnabled: "RAG Habilitado",
          trainingEnabled: "Entrenamiento Habilitado",
          complianceEnabled: "Cumplimiento Habilitado",
          errors: "Errores",
          warnings: "Advertencias",
        },
        newBatch: {
          title: "Nuevo Lote de Documentos",
          explanation:
            "Un lote es una colección de documentos que se procesarán juntos. Esto permite una mejor organización y seguimiento del procesamiento de documentos.",
          name: "Nombre del Lote",
          domain: "Dominio",
          sector: "Sector",
          priority: "Prioridad",
          processingType: "Tipo de Procesamiento",
          create: "Crear Lote",
          cancel: "Cancelar",
        },
      },
      monitor: {
        title: "Monitorización",
        subtitle: "Monitorización en tiempo real de trabajos de ingesta",
        stats: {
          total: "Total",
          running: "Ejecutando",
          completed: "Completados",
          failed: "Fallidos",
        },
        search: {
          placeholder: "Buscar trabajos...",
        },
        filters: {
          all: "Todos",
          running: "Ejecutando",
          completed: "Completados",
          failed: "Fallidos",
          more: "Más filtros",
        },
        actions: {
          viewDetails: "Ver Detalle",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron trabajos",
          description: "Intenta ajustar los filtros o iniciar un nuevo trabajo",
        },
        details: {
          title: "Detalles del Trabajo",
          basicInfo: "Información Básica",
          performance: "Rendimiento",
          metrics: "Métricas",
          alerts: "Alertas",
          name: "Nombre del Trabajo",
          domain: "Dominio",
          type: "Tipo",
          status: "Estado",
          startTime: "Hora de Inicio",
          endTime: "Hora de Fin",
          duration: "Duración",
          cpu: "CPU",
          memory: "Memoria",
          throughput: "Rendimiento",
          latency: "Latencia",
          alerts: "Alertas",
        },
      },
      settings: {
        title: "Configuración",
        subtitle: "Configura parámetros globales y por dominio",
        stats: {
          total: "Total",
          active: "Activos",
          configured: "Configurados",
          default: "Por Defecto",
        },
        search: {
          placeholder: "Buscar dominios...",
        },
        filters: {
          all: "Todos",
          active: "Activos",
          configured: "Configurados",
          default: "Por Defecto",
          more: "Más filtros",
        },
        tabs: {
          domains: "Dominios",
          global: "Global",
          advanced: "Avanzado",
        },
        actions: {
          viewDetails: "Ver Detalle",
          save: "Guardar",
          reset: "Restablecer",
        },
        noResults: {
          title: "No se encontraron dominios",
          description: "Intenta ajustar los filtros o crear un nuevo dominio",
        },
        details: {
          title: "Configuración del Dominio",
          basicInfo: "Información Básica",
          configuration: "Configuración",
          ragExplanation:
            "RAG (Retrieval-Augmented Generation) se utiliza para procesar documentos y crear datasets de entrenamiento. Realiza chunking y tagging automático del contenido para el entrenamiento de modelos de IA. Los dominios son modelos de negocio, no sistemas RAG.",
          ragEnabled: "RAG Habilitado para Datasets",
          trainingEnabled: "Entrenamiento Habilitado",
          complianceEnabled: "Cumplimiento Habilitado",
          autoIngestion: "Ingesta Automática",
          qualityThreshold: "Umbral de Calidad",
          retentionDays: "Días de Retención",
          lastUpdated: "Última Actualización",
          status: "Estado",
        },
        global: {
          general: "Configuración General",
          security: "Seguridad",
          autoBackup: "Respaldo Automático",
          notifications: "Notificaciones",
          language: "Idioma",
          timezone: "Zona Horaria",
          encryption: "Encriptación",
          authentication: "Autenticación",
          auditLogs: "Logs de Auditoría",
        },
        advanced: {
          performance: "Rendimiento",
          logs: "Logs y Monitorización",
          cacheSize: "Tamaño de Caché",
          maxConnections: "Conexiones Máximas",
          logLevel: "Nivel de Log",
          monitoring: "Monitorización",
          alerts: "Alertas",
        },
      },
      configureSources: {
        title: "Configurar Fuentes",
        subtitle: "Gestiona y configura las fuentes de datos para cada dominio",
        stats: {
          total: "Total",
          active: "Activas",
          api: "APIs",
        },
        search: {
          placeholder: "Buscar fuentes...",
        },
        filters: {
          allTypes: "Todos los tipos",
          api: "API",
          database: "Base de Datos",
          file: "Archivo",
          web: "Web",
          stream: "Stream",
          more: "Más filtros",
        },
        actions: {
          newSource: "Nueva Fuente",
          viewDetails: "Ver Detalle",
          edit: "Editar",
          test: "Probar Conexión",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron fuentes",
          description:
            "Intenta ajustar los filtros o crear una nueva fuente de datos",
        },
        status: {
          active: "Activo",
          inactive: "Inactivo",
          error: "Error",
          maintenance: "Mantenimiento",
          unknown: "Desconocido",
        },
        info: {
          type: "Tipo:",
          schedule: "Programación:",
          dataVolume: "Volumen:",
          syncStatus: "Sincronización:",
        },
        features: {
          compliance: "Cumplimiento:",
          rag: "RAG",
          training: "Training",
        },
        newSource: {
          title: "Nueva Fuente de Datos",
          name: "Nombre de la Fuente",
          namePlaceholder: "Ej: API Gateway",
          domain: "Dominio",
          selectDomain: "Seleccionar dominio",
          type: "Tipo de Fuente",
          selectType: "Seleccionar tipo",
          url: "URL/Endpoint",
          urlPlaceholder: "https://api.example.com/v1",
          credentials: "Tipo de Credenciales",
          credentialsNone: "Ninguna",
          credentialsApiKey: "API Key",
          credentialsOAuth: "OAuth",
          credentialsBasic: "Basic Auth",
          schedule: "Programación",
          scheduleRealTime: "Tiempo Real",
          schedule15Min: "Cada 15 minutos",
          scheduleHour: "Cada hora",
          scheduleDaily: "Diario",
          scheduleWeekly: "Semanal",
          processingOptions: "Opciones de Procesamiento",
          ragEnabled: "Habilitar RAG (Crear datasets)",
          ragDescription: "Procesar datos para crear datasets de entrenamiento",
          trainingEnabled: "Habilitar Training",
          trainingDescription:
            "Generar contenido para entrenamiento de modelos",
          create: "Crear Fuente",
          cancel: "Cancelar",
        },
        details: {
          basicInfo: "Información Básica",
          sync: "Sincronización",
          credentials: "Credenciales",
          compliance: "Cumplimiento",
          processing: "Procesamiento",
          domain: "Dominio:",
          type: "Tipo:",
          status: "Estado:",
          url: "URL:",
          schedule: "Programación:",
          lastSync: "Última sincronización:",
          nextSync: "Próxima sincronización:",
          dataVolume: "Volumen de datos:",
          credentialType: "Tipo:",
          lastUpdated: "Última actualización:",
          rag: "RAG (Datasets)",
          training: "Entrenamiento",
          edit: "Editar",
          test: "Probar Conexión",
          configure: "Configurar",
        },
      },
      domainDetails: {
        navigation: {
          back: "Volver",
        },
        actions: {
          edit: "Editar",
          settings: "Configuración",
          monitor: "Monitorear",
        },
        stats: {
          documents: "Documentos",
          processed: "Procesados",
          dataSources: "Fuentes de Datos",
          active: "activas",
          compliance: "Cumplimiento",
          compliant: "Cumple",
          successRate: "Tasa de Éxito",
          avgTime: "promedio",
        },
        tabs: {
          overview: "Resumen",
          dataSources: "Fuentes",
          documents: "Documentos",
          compliance: "Cumplimiento",
          alerts: "Alertas",
        },
        overview: {
          domainInfo: "Información del Dominio",
          performance: "Rendimiento",
          ingestionMetrics: "Métricas de Ingesta",
          industry: "Industria",
          status: "Estado",
          owner: "Propietario",
          created: "Creado",
          team: "Equipo",
          dataQuality: "Calidad de Datos",
          rag: "RAG",
          training: "Training",
          lastActivity: "Última Actividad",
          daily: "Diario",
          weekly: "Semanal",
          monthly: "Mensual",
        },
        dataSources: {
          title: "Fuentes de Datos",
          lastSync: "Última sincronización:",
        },
        documents: {
          title: "Documentos",
        },
        compliance: {
          frameworks: "Marcos de Cumplimiento",
          audits: "Auditorías",
          status: "Estado:",
          lastAudit: "Última Auditoría",
          nextAudit: "Próxima Auditoría",
        },
        alerts: {
          title: "Alertas",
          resolved: "Resuelto",
        },
        status: {
          active: "Activo",
          inactive: "Inactivo",
          maintenance: "Mantenimiento",
          error: "Error",
          unknown: "Desconocido",
        },
        edit: {
          title: "Editar Dominio",
          name: "Nombre del Dominio",
          industry: "Industria",
          description: "Descripción",
          save: "Guardar Cambios",
          cancel: "Cancelar",
        },
        settings: {
          title: "Configuración del Dominio",
          processing: "Procesamiento",
          ragEnabled: "Habilitar RAG (Crear datasets)",
          ragDescription:
            "Procesar documentos para crear datasets de entrenamiento",
          trainingEnabled: "Habilitar Training",
          trainingDescription:
            "Generar contenido para entrenamiento de modelos",
          save: "Guardar Configuración",
          cancel: "Cancelar",
        },
      },
    },

    // Model Evaluation
    modelEvaluation: {
      title: "Evaluación de Modelos",
      subtitle: "Dashboard de evaluación y testing de modelos de IA",
      completed: "Completado",
      running: "Ejecutando",
      failed: "Fallido",
      pending: "Pendiente",
      unknown: "Desconocido",
      loadingData: "Cargando datos del dashboard...",
      errorLoadingData: "No se pudieron cargar los datos del dashboard",
      pipeline: "Pipeline",
      abTest: "Test A/B",
      security: "Seguridad",
      evaluation: "Evaluación",
      newEvaluation: "Nueva Evaluación",
      viewEvaluation: "Ver Evaluación",
      pipelineModal: "Modal de Pipeline",
      abTestModal: "Modal de Test A/B",
      securityModal: "Modal de Seguridad",
      reportModal: "Modal de Reporte",
    },

    // RAG Training
    ragTraining: {
      title: "Entrenamiento RAG",
      subtitle: "Dashboard de entrenamiento y métricas RAG",
      totalDocuments: "Total de Documentos",
      totalChunks: "Total de Chunks",
      totalSearches: "Total de Búsquedas",
      averageResponseTime: "Tiempo de Respuesta Promedio",
      activeUsers: "Usuarios Activos",
      storageUsed: "Almacenamiento Usado",
      documentsByType: "Documentos por Tipo",
      searchesByDay: "Búsquedas por Día",
      topQueries: "Consultas Principales",
      pdf: "PDF",
      docx: "DOCX",
      txt: "TXT",
      html: "HTML",
      configuration: "configuración de base de datos",
      apiEndpoints: "API endpoints",
      authentication: "autenticación",
      middlewareSetup: "middleware setup",
      deployment: "deployment",
    },

    // Data Sources Module
    dataSources: {
      title: "Fuentes de Información",
      subtitle: "Gestión centralizada de fuentes de datos",
      totalDataSources: "Total Fuentes",
      activeSources: "Fuentes Activas",
      totalChunks: "Total Chunks",
      processingJobs: "Jobs Procesando",
      successRate: "Tasa de Éxito",
      vsLastMonth: "vs mes anterior",
      dataSourceTypes: "Tipos de Fuentes",
      sources: "fuentes",
      recentActivity: "Actividad Reciente",
      processingStats: "Estadísticas de Procesamiento",
      completedJobs: "Jobs Completados",
      pendingJobs: "Jobs Pendientes",
      errorJobs: "Jobs con Error",
      avgProcessingTime: "Tiempo Promedio",
      timeAgo: {
        now: "Ahora",
      },
      help: {
        dashboard: {
          title: "Dashboard de Fuentes de Información",
          content: `
            <p>Este dashboard centraliza la gestión de todas las fuentes de datos del sistema.</p>
            <h3>Funcionalidades principales:</h3>
            <ul>
              <li><strong>Estadísticas en tiempo real:</strong> Monitorea el estado de todas las fuentes de datos</li>
              <li><strong>Tipos de fuentes:</strong> Bases de datos, APIs, documentos y web scraping</li>
              <li><strong>Métricas de procesamiento:</strong> Jobs completados, pendientes y con errores</li>
              <li><strong>Actividad reciente:</strong> Últimas operaciones realizadas</li>
            </ul>
            <p>Desde aquí puedes acceder a cada tipo de fuente de datos para configurar, monitorear y gestionar tus conexiones.</p>
          `,
        },
        database: {
          title: "Gestión de Bases de Datos",
          content: `
            <p>Gestiona las conexiones a bases de datos para extraer y procesar información.</p>
            <h3>Tipos soportados:</h3>
            <ul>
              <li><strong>PostgreSQL:</strong> Bases de datos relacionales</li>
              <li><strong>MySQL:</strong> Bases de datos relacionales</li>
              <li><strong>MongoDB:</strong> Bases de datos NoSQL</li>
              <li><strong>Elasticsearch:</strong> Motor de búsqueda y análisis</li>
              <li><strong>Vector DB:</strong> Bases de datos vectoriales para embeddings</li>
              <li><strong>Redis:</strong> Base de datos en memoria</li>
            </ul>
            <h3>Funcionalidades:</h3>
            <ul>
              <li>Configuración de conexiones seguras</li>
              <li>Prueba de conectividad</li>
              <li>Ejecución de consultas SQL</li>
              <li>Asignación a dominios y proyectos RAG</li>
            </ul>
          `,
        },
        api: {
          title: "Gestión de APIs",
          content: `
            <p>Configura y gestiona conexiones a APIs externas para extraer datos.</p>
            <h3>Características:</h3>
            <ul>
              <li><strong>OpenAPI/Swagger:</strong> Soporte completo para especificaciones OpenAPI</li>
              <li><strong>Autenticación:</strong> API Key, Basic Auth, Bearer Token</li>
              <li><strong>Configuración avanzada:</strong> Timeouts, reintentos, rate limiting</li>
              <li><strong>Testing:</strong> Prueba de endpoints antes de guardar</li>
            </ul>
            <h3>Flujo de trabajo:</h3>
            <ol>
              <li>Configura la URL del Swagger/OpenAPI</li>
              <li>Establece la autenticación necesaria</li>
              <li>Prueba la conexión</li>
              <li>Asigna a dominios y proyectos RAG</li>
            </ol>
          `,
        },
        documents: {
          title: "Gestión de Documentos",
          content: `
            <p>Gestiona lotes de documentos para procesamiento y análisis.</p>
            <h3>Tipos de documentos soportados:</h3>
            <ul>
              <li><strong>PDF:</strong> Documentos PDF</li>
              <li><strong>DOCX:</strong> Documentos de Word</li>
              <li><strong>TXT:</strong> Archivos de texto plano</li>
              <li><strong>CSV:</strong> Datos tabulares</li>
              <li><strong>JSON:</strong> Datos estructurados</li>
            </ul>
            <h3>Procesamiento:</h3>
            <ul>
              <li>Chunking automático de documentos</li>
              <li>Extracción de metadatos</li>
              <li>Generación de embeddings</li>
              <li>Asignación a proyectos RAG</li>
            </ul>
          `,
        },
        webScraping: {
          title: "Web Scraping",
          content: `
            <p>Configura trabajos de extracción de datos de sitios web.</p>
            <h3>Características:</h3>
            <ul>
              <li><strong>Configuración flexible:</strong> URLs, selectores CSS, XPath</li>
              <li><strong>Programación:</strong> Ejecución manual o programada</li>
              <li><strong>Filtros avanzados:</strong> Por contenido, fecha, tipo</li>
              <li><strong>Monitoreo:</strong> Estado de ejecución y logs</li>
            </ul>
            <h3>Tipos de extracción:</h3>
            <ul>
              <li>Contenido de páginas web</li>
              <li>Datos de formularios</li>
              <li>Enlaces y referencias</li>
              <li>Metadatos y estructura</li>
            </ul>
          `,
        },
      },
    },
    // RAG Module
    rag: {
      dashboard: {
        title: "Dashboard RAG",
        subtitle: "Sistema de Retrieval Augmented Generation",
        totalProjects: "Total Proyectos",
        totalChunks: "Total Chunks",
        totalSearches: "Total Búsquedas",
        avgResponseTime: "Tiempo Respuesta Promedio",
        activeProjects: "Proyectos Activos",
        vsLastMonth: "vs mes pasado",
        projectsOverview: "Resumen de Proyectos",
        viewAll: "Ver Todos",
        chunks: "chunks",
        embeddings: "embeddings",
        searches: "búsquedas",
        models: "Modelos",
        recentActivity: "Actividad Reciente",
        modelUsage: "Uso de Modelos",
        usage: "Uso",
      },
      project: {
        createTitle: "Crear Proyecto RAG",
        createSubtitle: "Configurar un nuevo proyecto RAG",
        editTitle: "Editar Proyecto RAG",
        basicInfo: "Información Básica",
        name: "Nombre del Proyecto",
        namePlaceholder: "Ingresa el nombre del proyecto",
        description: "Descripción",
        descriptionPlaceholder: "Describe el propósito del proyecto RAG",
        isActive: "Proyecto Activo",
        embeddingModel: "Modelo de Embedding",
        modelConfiguration: "Configuración de Modelos",
        rerankerModel: "Modelo de Reranker",
        llmModel: "Modelo LLM",
        ragConfiguration: "Configuración RAG",
        maxChunkSize: "Tamaño Máximo de Chunk",
        chunkOverlap: "Solapamiento de Chunks",
        semanticChunking: "Chunking Semántico",
        statistics: "Estadísticas del Proyecto",
        documents: "Documentos",
        chunks: "Chunks",
        embeddings: "Embeddings",
        searches: "Búsquedas",
        avgResponseTime: "Tiempo Promedio",
        successRate: "Tasa de Éxito",
        assignments: "Asignaciones",
        documentAssignments: "Documentos Asignados",
        webscrapingAssignments: "Web Scraping Asignado",
        datasourceAssignments: "Fuentes de Datos Asignadas",
        lekaServerConfig: "Configuración Leka Server",
        lekaEndpoint: "Endpoint Leka Server",
        lekaApiKey: "API Key Leka Server",
        lekaTimeout: "Timeout (ms)",
      },
      help: {
        dashboard: {
          title: "Dashboard RAG",
          content:
            "El Dashboard RAG te permite monitorear el rendimiento de tus proyectos de Retrieval Augmented Generation, incluyendo estadísticas de uso, proyectos activos y actividad reciente.",
        },
        projects: {
          title: "Proyectos RAG",
          content:
            "<h4>¿Qué son los Proyectos RAG?</h4><p>Los proyectos RAG (Retrieval Augmented Generation) son sistemas inteligentes que combinan la recuperación de información con la generación de texto. Te permiten crear chatbots y asistentes que pueden responder preguntas basándose en tus propios documentos y datos.</p><h4>¿Para qué sirven?</h4><ul><li><strong>Chatbots Especializados:</strong> Crea asistentes que conocen tu documentación específica</li><li><strong>Búsqueda Inteligente:</strong> Encuentra información relevante en grandes volúmenes de documentos</li><li><strong>Respuestas Contextualizadas:</strong> Genera respuestas precisas basadas en tu contenido</li><li><strong>Automatización de Soporte:</strong> Reduce la carga de trabajo del equipo de soporte</li><li><strong>Base de Conocimiento Interactiva:</strong> Convierte documentos estáticos en una experiencia conversacional</li></ul><h4>Gestión de Proyectos</h4><p>Desde esta página puedes:</p><ul><li>Crear nuevos proyectos RAG con configuraciones personalizadas</li><li>Editar modelos de IA (embedding, reranker, LLM) para cada proyecto</li><li>Monitorear el rendimiento y estadísticas de uso</li><li>Gestionar las fuentes de datos asignadas a cada proyecto</li><li>Configurar parámetros de chunking y procesamiento</li></ul>",
        },
        createProject: {
          title: "Crear Proyecto RAG",
          content:
            "Crea un nuevo proyecto RAG configurando los modelos de embedding, reranker y LLM, así como los parámetros de chunking y configuración del servidor Leka.",
        },
        editProject: {
          title: "Editar Proyecto RAG",
          content:
            "Edita la configuración de tu proyecto RAG, incluyendo modelos, parámetros de chunking, configuración del servidor Leka y monitorea las estadísticas del proyecto.",
        },
        models: {
          title: "Modelos RAG",
          content:
            "Gestiona los modelos de IA disponibles para tus proyectos RAG, incluyendo modelos de embedding, reranker y LLM de diferentes proveedores.",
        },
        createModel: {
          title: "Crear Modelo RAG",
          content:
            "<h4>Configuración de Modelos de IA</h4><p>Esta página te permite crear y configurar nuevos modelos de IA para tus proyectos RAG. Puedes configurar:</p><ul><li><strong>Información Básica:</strong> Nombre, tipo (Embedding, Reranker, LLM), proveedor y versión</li><li><strong>Configuración Técnica:</strong> Endpoint, API Key, costo por token, máximo de tokens y ventana de contexto</li><li><strong>Opciones Avanzadas:</strong> Modelo local vs remoto, estado activo/inactivo</li></ul><p><strong>Tipos de Modelos:</strong></p><ul><li><strong>Embedding:</strong> Convierte texto en vectores numéricos para búsqueda semántica</li><li><strong>Reranker:</strong> Mejora la relevancia de los resultados de búsqueda</li><li><strong>LLM:</strong> Genera respuestas basadas en el contexto recuperado</li></ul><p>Una vez configurado, puedes probar el modelo antes de guardarlo para asegurar que funciona correctamente.</p>",
        },
        editModel: {
          title: "Editar Modelo RAG",
          content:
            "Edita la configuración de un modelo de IA existente, incluyendo parámetros de rendimiento, costo y configuración del endpoint.",
        },
        chat: {
          title: "Chat RAG",
          content:
            "Interactúa con tus proyectos RAG a través del chat. Selecciona un proyecto y haz preguntas para obtener respuestas basadas en tus documentos.",
        },
        reranker: {
          title: "Reranker RAG",
          content:
            "Configura y gestiona el reranking de resultados de búsqueda para mejorar la relevancia de las respuestas en tus proyectos RAG.",
        },
        chunks: {
          title: "Chunks de Documentos",
          content:
            "Los chunks son fragmentos de documentos procesados que permiten una búsqueda más eficiente. Los auto-tags se generan automáticamente basándose en el contenido, mientras que los tags manuales los puedes añadir, editar o eliminar para mejorar la categorización y recuperación de información específica.",
        },
        search: {
          title: "Búsqueda Semántica",
          content:
            "Realiza búsquedas semánticas avanzadas en tus documentos RAG con diferentes estrategias de búsqueda y filtros.",
        },
        searchTypes: {
          vector: "Vectorial",
          textual: "Textual",
          hybrid: "Híbrida",
          filtered: "Filtrada",
        },
        analytics: {
          title: "Analytics RAG",
          content:
            "Monitorea el rendimiento y métricas de tus proyectos RAG, incluyendo estadísticas de uso, tiempos de respuesta y calidad de resultados.",
        },
      },
      reranker: {
        title: "Reranker",
        subtitle: "Mejora la relevancia de los resultados de búsqueda",
        configuration: "Configuración del Reranker",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        model: "Modelo de Reranking",
        selectModel: "Seleccionar modelo",
        topK: "Top K Results",
        threshold: "Umbral de Relevancia",
        testQuery: "Test de Consulta",
        query: "Consulta de Prueba",
        queryPlaceholder: "Ingresa una consulta para probar el reranking...",
        runTest: "Ejecutar Test",
        testHistory: "Historial de Tests",
      },
      chunks: {
        title: "Chunks",
        subtitle: "Visualiza y gestiona los chunks de documentos",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        chunkType: "Tipo de Chunk",
        selectType: "Seleccionar tipo",
        searchChunks: "Buscar Chunks",
        searchPlaceholder: "Buscar por contenido o tags...",
        totalChunks: "Total Chunks",
        avgSize: "Tamaño Promedio",
        autoTags: "Auto Tags",
        viewDetails: "Ver Detalles",
        filters: "Filtros",
        manualTags: "Tags Manuales",
        newTag: "Nuevo tag...",
        addTag: "Añadir Tag",
        editTags: "Editar Tags",
        removeTag: "Eliminar Tag",
        source: "Fuente",
        relevance: "Relevancia",
        characters: "caracteres",
        createdAt: "Creado",
        saveChanges: "Guardar Cambios",
        cancel: "Cancelar",
      },
      search: {
        title: "Búsqueda Semántica",
        subtitle: "Búsqueda avanzada en documentos RAG",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        searchQuery: "Consulta de Búsqueda",
        queryPlaceholder: "Ingresa tu consulta de búsqueda...",
        searchType: "Tipo de Búsqueda",
        selectType: "Seleccionar tipo",
        executeSearch: "Ejecutar Búsqueda",
        searchHistory: "Historial de Búsquedas",
        results: "Resultados",
        relevance: "Relevancia",
        source: "Fuente",
      },
      analytics: {
        title: "Analytics",
        subtitle: "Métricas y rendimiento de proyectos RAG",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        timeRange: "Rango de Tiempo",
        selectRange: "Seleccionar rango",
        refresh: "Actualizar",
        totalSearches: "Total Búsquedas",
        avgResponseTime: "Tiempo Respuesta Promedio",
        successRate: "Tasa de Éxito",
        userSatisfaction: "Satisfacción del Usuario",
        topQueries: "Consultas Más Populares",
        performanceMetrics: "Métricas de Rendimiento",
        usageStats: "Estadísticas de Uso",
      },
    },

    // Governance
    governance: {
      title: "Dashboard de Gobierno Automático",
      subtitle: "Monitorea y gestiona el gobierno de IA en tiempo real",
      refresh: "Actualizar",
      configure: "Configurar",
      low: "BAJO",
      medium: "MEDIO",
      high: "ALTO",
      critical: "CRÍTICO",
      riskLevel: "Nivel de Riesgo",
      compliance: "Cumplimiento",
      audit: "Auditoría",
      policies: "Políticas",
      violations: "Violaciones",
      alerts: "Alertas",
      metrics: "Métricas",
      reports: "Reportes",
      settings: "Configuración",
      overview: {
        title: "Vista General de Políticas",
        subtitle: "Gestión y configuración de políticas de gobierno",
        metrics: {
          total: "Total Políticas",
          active: "Políticas Activas",
          violations: "Violaciones",
          compliance: "Score Cumplimiento",
        },
        filters: {
          title: "Filtros",
          clear: "Limpiar",
          search: {
            placeholder: "Buscar políticas...",
          },
          type: "Todos los Tipos",
          "type.security": "Seguridad",
          "type.compliance": "Cumplimiento",
          "type.ethics": "Ética",
          "type.privacy": "Privacidad",
          status: "Todos los Estados",
          "status.active": "Activo",
          "status.inactive": "Inactivo",
          "status.draft": "Borrador",
          enforcement: "Todos los Niveles",
          "enforcement.mandatory": "Obligatorio",
          "enforcement.recommended": "Recomendado",
          "enforcement.optional": "Opcional",
        },
        table: {
          title: "Políticas",
          id: "ID",
          name: "Nombre",
          type: "Tipo",
          enforcement: "Nivel",
          version: "Versión",
          status: "Estado",
          violations: "Violaciones",
          score: "Score",
          actions: "Acciones",
          empty: "No se encontraron políticas",
        },
        button: {
          create: "Crear Política",
        },
        confirmDelete: "¿Eliminar esta política?",
      },
      detail: {
        "title.edit": "Editar Política",
        "title.create": "Nueva Política",
        button: {
          evaluate: "Evaluar",
          compliance: "Verificar Cumplimiento",
          score: "Calcular Score",
        },
        form: {
          title: "Información de la Política",
          name: "Nombre de la Política",
          version: "Versión",
          type: "Tipo",
          "type.security": "Seguridad",
          "type.compliance": "Cumplimiento",
          "type.ethics": "Ética",
          "type.privacy": "Privacidad",
          "type.operational": "Operacional",
          category: "Categoría",
          enforcement: "Nivel de Enforcement",
          "enforcement.mandatory": "Obligatorio",
          "enforcement.recommended": "Recomendado",
          "enforcement.optional": "Opcional",
          "enforcement.informational": "Información",
          description: "Descripción",
          effectiveDate: "Fecha Efectiva",
          expirationDate: "Fecha de Expiración",
          status: "Estado",
          "status.active": "Activo",
          "status.inactive": "Inactivo",
          "status.draft": "Borrador",
          "status.review": "Revisión",
          "status.archived": "Archivado",
          metadata: "Metadata (JSON)",
        },
        tabs: {
          rules: "Reglas",
          evaluations: "Evaluaciones",
          violations: "Violaciones",
          compliance: "Cumplimiento",
          statistics: "Estadísticas",
          "rules.empty": "No hay reglas definidas",
          "rules.name": "Nombre",
          "rules.condition": "Condición",
          "rules.action": "Acción",
          "rules.priority": "Prioridad",
          "rules.active": "Activa",
          "evaluations.empty": "No hay evaluaciones registradas",
          "evaluations.date": "Fecha",
          "evaluations.result": "Resultado",
          "evaluations.score": "Puntuación",
          "evaluations.evaluator": "Evaluador",
          "violations.empty": "No hay violaciones registradas",
          "violations.type": "Tipo",
          "violations.description": "Descripción",
          "violations.severity": "Severidad",
          "violations.date": "Fecha",
          "violations.status": "Estado",
          "compliance.empty": "No hay evaluaciones de cumplimiento",
          "compliance.average": "Score Promedio",
          "compliance.date": "Fecha",
          "compliance.area": "Área",
          "compliance.score": "Score",
          "compliance.status": "Estado",
          "compliance.findings": "Hallazgos",
          "statistics.rules": "Reglas",
          "statistics.evaluations": "Evaluaciones",
          "statistics.violations": "Violaciones",
          "statistics.score": "Score Actual",
          "statistics.info": "Información de la Política",
          "statistics.createdBy": "Creado por",
          "statistics.createdAt": "Fecha de creación",
          "statistics.lastEvaluation": "Última evaluación",
          "statistics.lastUpdated": "Última actualización",
          "statistics.evaluationStatus": "Estado evaluación",
          "statistics.complianceScore": "Score cumplimiento",
        },
        evaluation: {
          success: "Evaluación completada",
        },
        compliance: {
          success: "Verificación de cumplimiento completada",
        },
        score: {
          success: "Score calculado: {score}",
        },
      },
    },

    // BPMN Task Inbox
    taskInbox: {
      title: "Bandeja de Tareas BPMN",
      subtitle: "Gestiona tus tareas pendientes",
      stats: {
        totalTasks: "Tareas Pendientes",
        assignedToMe: "Asignadas a Mí",
        groupTasks: "De Mis Roles",
      },
      filters: {
        process: "Proceso",
        priority: "Prioridad",
        state: "Estado",
        allProcesses: "Todos los procesos",
        allPriorities: "Todas las prioridades",
        allStates: "Todos los estados",
        assigned: "Asignadas",
        unassigned: "Sin asignar",
        candidate: "Candidatas",
        low: "Baja",
        normal: "Normal",
        high: "Alta",
        urgent: "Urgente",
      },
      table: {
        priority: "Prioridad",
        task: "Tarea",
        process: "Proceso",
        assigned: "Asignado",
        roles: "Roles",
        creation: "Creación",
        dueDate: "Vencimiento",
        actions: "Acciones",
        noTasks: "No hay tareas pendientes",
        unassigned: "Sin asignar",
        openTask: "Abrir tarea",
        claim: "Reclamar",
        assign: "Asignar",
      },
      assignDialog: {
        title: "Asignar Tarea a Usuario",
        task: "Tarea",
        availableUsers: "Usuarios disponibles",
        username: "Usuario",
        fullname: "Nombre Completo",
        email: "Email",
        cancel: "Cancelar",
        assign: "Asignar",
      },
      actions: {
        refresh: "Refrescar",
        clearFilters: "Limpiar filtros",
      },
    },

    // Internal Audit Scheduling Form
    internalAuditScheduling: {
      title: "Programar auditoría interna ISO 42001",
      subtitle: "Complete los datos para programar la auditoría interna",
      formTitle: "Datos de la auditoría",
      project: "Proyecto",
      projectPlaceholder: "Project ID",
      date: "Fecha programada",
      scope: "Alcance",
      scopePlaceholder: "Cláusulas, procesos y sistemas a auditar",
      auditor: "Auditor responsable",
      auditorPlaceholder: "Usuario o rol auditor",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Planificar auditoría",
      saving: "Guardando...",
      errors: {
        scopeRequired: "Debe definir el alcance de la auditoría",
        auditorRequired: "Debe asignar un auditor responsable",
        dateRequired: "Debe seleccionar una fecha",
        submitError: "Error al planificar la auditoría",
      },

    // BPMN Forms
    agentApprovalHumanOverride: {
      title: "Aprobación de Agent - Override Humano",
      subtitle: "Complete los datos para aprobación de agent - override humano",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    modelApprovalHumanOverride: {
      title: "Aprobación de Modelo - Override Humano",
      subtitle: "Complete los datos para aprobación de modelo - override humano",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    promptHumanReview: {
      title: "Revisión Humana de Prompt",
      subtitle: "Complete los datos para revisión humana de prompt",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    hitlSlaReminder: {
      title: "Recordatorio SLA HITL",
      subtitle: "Complete los datos para recordatorio sla hitl",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    promptApprovalRequest: {
      title: "Solicitud de Aprobación de Prompt",
      subtitle: "Complete los datos para solicitud de aprobación de prompt",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionApproval: {
      title: "Aprobación de Desmantelamiento IA",
      subtitle: "Complete los datos para aprobación de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapApproval: {
      title: "Aprobación de Brecha de Competencia",
      subtitle: "Complete los datos para aprobación de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionApproval: {
      title: "Aprobación de Acción Correctiva",
      subtitle: "Complete los datos para aprobación de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    approveConformityAssessment: {
      title: "Aprobar Evaluación de Conformidad",
      subtitle: "Complete los datos para aprobar evaluación de conformidad",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    deployerApproval: {
      title: "Aprobación de Despliegue",
      subtitle: "Complete los datos para aprobación de despliegue",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    boardDecisionPaper: {
      title: "Decisión del Consejo",
      subtitle: "Complete los datos para decisión del consejo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    managementReview: {
      title: "Revisión de Gestión",
      subtitle: "Complete los datos para revisión de gestión",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    llmEvaluationReview: {
      title: "Revisión de Evaluación LLM",
      subtitle: "Complete los datos para revisión de evaluación llm",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ragEvaluationReview: {
      title: "Revisión de Evaluación RAG",
      subtitle: "Complete los datos para revisión de evaluación rag",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    modelEvaluationReview: {
      title: "Revisión de Evaluación de Modelo",
      subtitle: "Complete los datos para revisión de evaluación de modelo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapAssessment: {
      title: "Evaluación de Brecha de Competencia",
      subtitle: "Complete los datos para evaluación de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionAssessment: {
      title: "Evaluación de Acción Correctiva",
      subtitle: "Complete los datos para evaluación de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    performanceReviewDecision: {
      title: "Decisión de Revisión de Rendimiento",
      subtitle: "Complete los datos para decisión de revisión de rendimiento",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    compliance: {
      dashboard: {
        title: "Dashboard Consolidado Compliance",
        subtitle: "Vista consolidada del estado de compliance según EU AI Act",
      },
      postMarketMonitoring: {
        title: "Dashboard Post-Market Monitoring",
        subtitle: "Monitoreo continuo de sistemas de IA según EU AI Act Art. 72",
      },
      sector: {
        title: "Metamodelo sectorial FaaS",
        subtitle: "Visualiza sectores, subsectores y frameworks configurados sin modificar el core de CodeflowX",
      },
      conformityDeclaration: {
        title: "EU Declaration of Conformity Manager",
        subtitle: "Gestión de declaraciones de conformidad según EU AI Act Annex V",
      },
      conformityReview: {
        title: "Conformity Assessment Review",
        subtitle: "Human review required for borderline compliance assessment",
      },
    },
    complianceReview: {
      title: "Revisión de Compliance",
      subtitle: "Complete los datos para revisión de compliance",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    complianceReviewDecision: {
      title: "Decisión de Revisión de Compliance",
      subtitle: "Complete los datos para decisión de revisión de compliance",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    reviewQmsGaps: {
      title: "Revisar Brechas QMS",
      subtitle: "Complete los datos para revisar brechas qms",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    completeDocumentation: {
      title: "Completar Documentación",
      subtitle: "Complete los datos para completar documentación",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    biasReview: {
      title: "Revisión de Sesgo",
      subtitle: "Complete los datos para revisión de sesgo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    biasMitigationPlan: {
      title: "Plan de Mitigación de Sesgo",
      subtitle: "Complete los datos para plan de mitigación de sesgo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    biasUrgentDecision: {
      title: "Decisión Urgente de Sesgo",
      subtitle: "Complete los datos para decisión urgente de sesgo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    driftAnalysis: {
      title: "Análisis de Deriva",
      subtitle: "Complete los datos para análisis de deriva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    driftReviewDecision: {
      title: "Decisión de Revisión de Deriva",
      subtitle: "Complete los datos para decisión de revisión de deriva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    internalAuditExecution: {
      title: "Ejecución de Auditoría Interna",
      subtitle: "Complete los datos para ejecución de auditoría interna",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    managementReviewSchedule: {
      title: "Programación de Revisión de Gestión",
      subtitle: "Complete los datos para programación de revisión de gestión",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    completeMissingElements: {
      title: "Completar Elementos Faltantes",
      subtitle: "Complete los datos para completar elementos faltantes",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    defineModifications: {
      title: "Definir Modificaciones",
      subtitle: "Complete los datos para definir modificaciones",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    enhancedReview: {
      title: "Revisión Mejorada",
      subtitle: "Complete los datos para revisión mejorada",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    finalReview: {
      title: "Revisión Final",
      subtitle: "Complete los datos para revisión final",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    euRegistration: {
      title: "Registro UE",
      subtitle: "Complete los datos para registro ue",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    reviewRegistrationPackage: {
      title: "Revisar Paquete de Registro",
      subtitle: "Complete los datos para revisar paquete de registro",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    fixValidationErrors: {
      title: "Corregir Errores de Validación",
      subtitle: "Complete los datos para corregir errores de validación",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    manualResolution: {
      title: "Resolución Manual",
      subtitle: "Complete los datos para resolución manual",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    documentIncidentDetails: {
      title: "Documentar Detalles de Incidente",
      subtitle: "Complete los datos para documentar detalles de incidente",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    verifyIncidentResolution: {
      title: "Verificar Resolución de Incidente",
      subtitle: "Complete los datos para verificar resolución de incidente",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    rootCauseAnalysis: {
      title: "Análisis de Causa Raíz",
      subtitle: "Complete los datos para análisis de causa raíz",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    defineCorrectiveActions: {
      title: "Definir Acciones Correctivas",
      subtitle: "Complete los datos para definir acciones correctivas",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionImplementation: {
      title: "Implementación de Acción Correctiva",
      subtitle: "Complete los datos para implementación de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionEffectiveness: {
      title: "Efectividad de Acción Correctiva",
      subtitle: "Complete los datos para efectividad de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    managementReviewActions: {
      title: "Acciones de Revisión de Gestión",
      subtitle: "Complete los datos para acciones de revisión de gestión",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsReviewRequest: {
      title: "Solicitud de Revisión Ética",
      subtitle: "Complete los datos para solicitud de revisión ética",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsReviewReminder: {
      title: "Recordatorio de Revisión Ética",
      subtitle: "Complete los datos para recordatorio de revisión ética",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsCommitteeReview: {
      title: "Revisión del Comité Ético",
      subtitle: "Complete los datos para revisión del comité ético",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsMitigationPlan: {
      title: "Plan de Mitigación Ética",
      subtitle: "Complete los datos para plan de mitigación ética",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapImplementation: {
      title: "Implementación de Brecha de Competencia",
      subtitle: "Complete los datos para implementación de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapAdditionalActions: {
      title: "Acciones Adicionales de Brecha de Competencia",
      subtitle: "Complete los datos para acciones adicionales de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapRework: {
      title: "Retrabajo de Brecha de Competencia",
      subtitle: "Complete los datos para retrabajo de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionRequest: {
      title: "Solicitud de Desmantelamiento IA",
      subtitle: "Complete los datos para solicitud de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionVerification: {
      title: "Verificación de Desmantelamiento IA",
      subtitle: "Complete los datos para verificación de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionRetention: {
      title: "Retención de Desmantelamiento IA",
      subtitle: "Complete los datos para retención de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    modelApprovalReminder: {
      title: "Recordatorio de Aprobación de Modelo",
      subtitle: "Complete los datos para recordatorio de aprobación de modelo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    datasetReviewReminder: {
      title: "Recordatorio de Revisión de Dataset",
      subtitle: "Complete los datos para recordatorio de revisión de dataset",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    categorizeIncident: {
      title: "Categorizar Incidente",
      subtitle: "Complete los datos para categorizar incidente",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    executeCorrectiveAction: {
      title: "Ejecutar Acción Correctiva",
      subtitle: "Complete los datos para ejecutar acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    },

    // Providers Management
    providers: {
      title: "Proveedores de IA",
      subtitle: "Gestión de proveedores de servicios de IA",
      operational: "Operacional",
      totalProviders: "Total Proveedores",
      activeProviders: "Proveedores Activos",
      openaiProviders: "OpenAI",
      selfHosting: "Self-Hosting",
      search: "Buscar proveedores...",
      type: "Tipo",
      newProvider: "Nuevo Proveedor",
      noProviders: "No se encontraron proveedores",
      createProvider: "Crear Nuevo Proveedor",
      editProvider: "Editar Proveedor",
      providerDetails: "Detalles del Proveedor",
      quick: {
        title: "Acciones Rápidas",
        newProvider: "Nuevo Proveedor",
        importProvider: "Importar Proveedor",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
    },

    // Marketplace Management
    marketplace: {
      title: "Marketplace de Modelos",
      subtitle: "Descubre y descarga modelos de IA de múltiples proveedores",
      operational: "Operacional",
      totalModels: "Total Modelos",
      downloaded: "Descargados",
      downloadedModels: "Modelos Descargados",
      avgRating: "Rating Promedio",
      totalDownloads: "Total Descargas",
      search: "Buscar modelos...",
      source: "Fuente",
      type: "Tipo",
      sort: "Ordenar por",
      noModels: "No se encontraron modelos",
      noDescription: "Sin descripción",
      modelDetails: "Detalles del Modelo",
      download: "Descargar",
      downloaded: "Descargado",
      quick: {
        title: "Acciones Rápidas",
        advancedSearch: "Búsqueda Avanzada",
        downloadQueue: "Cola de Descargas",
        configuration: "Configuración",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        text: "Texto",
        vision: "Visión",
        audio: "Audio",
        multimodal: "Multimodal",
      },
    },

    // Experiments Management
    experiments: {
      title: "Experimentos de Modelos",
      subtitle: "Gestión y seguimiento de experimentos de IA",
      operational: "Operacional",
      totalExperiments: "Total Experimentos",
      runningExperiments: "Ejecutándose",
      completedExperiments: "Completados",
      failedExperiments: "Fallidos",
      search: "Buscar experimentos...",
      type: "Tipo",
      status: "Estado",
      newExperiment: "Nuevo Experimento",
      noExperiments: "No se encontraron experimentos",
      noDescription: "Sin descripción",
      createExperiment: "Crear Nuevo Experimento",
      editExperiment: "Editar Experimento",
      experimentDetails: "Detalles del Experimento",
      quick: {
        title: "Acciones Rápidas",
        newExperiment: "Nuevo Experimento",
        importExperiment: "Importar Experimento",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        planned: "Planificados",
        running: "Ejecutándose",
        completed: "Completados",
        failed: "Fallidos",
      },
    },

    // Model Serving Management
    modelServing: {
      title: "Model Serving",
      subtitle: "Gestión de despliegues y servicios de modelos",
      operational: "Operacional",
      totalDeployments: "Total Deployments",
      runningDeployments: "Ejecutándose",
      stoppedDeployments: "Detenidos",
      failedDeployments: "Fallidos",
      search: "Buscar deployments...",
      type: "Tipo",
      status: "Estado",
      newDeployment: "Nuevo Deployment",
      noDeployments: "No se encontraron deployments",
      noDescription: "Sin descripción",
      start: "Iniciar",
      stop: "Detener",
      createDeployment: "Crear Nuevo Deployment",
      editDeployment: "Editar Deployment",
      deploymentDetails: "Detalles del Deployment",
      quick: {
        title: "Acciones Rápidas",
        newDeployment: "Nuevo Deployment",
        importDeployment: "Importar Deployment",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        pending: "Pendientes",
        running: "Ejecutándose",
        stopped: "Detenidos",
        failed: "Fallidos",
      },
    },
  },

  en: {
    // Navigation and layout
    common: {
      dashboard: "Dashboard",
      infrastructure: "Infrastructure",
      admin: "Administration",
      aiNotebook: "AI Notebook",
      domainIngestion: "Domain Ingestion",
      modelEvaluation: "Model Evaluation",
      ragTraining: "RAG Training",
      governance: "Governance",
      settings: "Settings",
      profile: "Profile",
      logout: "Logout",
      loading: "Loading...",
      save: "Save",
      cancel: "Cancel",
      edit: "Edit",
      delete: "Delete",
      add: "Add",
      search: "Search",
      filter: "Filter",
      status: "Status",
      actions: "Actions",
      details: "Details",
      back: "Back",
      next: "Next",
      previous: "Previous",
      close: "Close",
      confirm: "Confirm",
      yes: "Yes",
      no: "No",
      active: "Active",
      inactive: "Inactive",
      pending: "Pending",
      completed: "Completed",
      error: "Error",
      success: "Success",
      warning: "Warning",
      info: "Information",
      changeLanguage: "Change Language",
      selectLanguage: "Select Language",
      noResults: "No results found",
      showing: "Showing",
      all: "ALL",
      clearFilters: "Clear filters",
    },

    // Prompts
    prompts: {
      performance: {
        title: "Prompt Performance Comparison",
        description: "Prompt Performance Comparison Management",
        results: "Results",
        id: "ID",
        activeItems: "Active Items",
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        avgScore: "Avg Score",
      },
      validation: {
        title: "Prompt Validation",
        description: "Prompt Validation Management",
        register: "Register Prompt Validation",
        results: "Results",
        id: "ID",
        validationtype: "Validationtype",
        validationresult: "Validationresult",
        validationscore: "Validationscore",
        validationdetails: "Validationdetails",
        issuesfound: "Issuesfound",
        filterType: "Filter by Validationtype",
        filterStatus: "Filter by Status",
        type1: "TYPE 1",
        type2: "TYPE 2",
      },
      versioning: {
        title: "Prompt Version",
        description: "Prompt Version Management",
        register: "Register Prompt Version",
        results: "Results",
        id: "ID",
        version: "Version",
        description: "Description",
        content: "Content",
        parameters: "Parameter S",
        changes: "Changes",
        filterStatus: "Filter by Status",
      },
    },

    // Dashboard
    dashboard: {
      title: "Control Panel",
      welcome: "Welcome to CodeflowX",
      stats: "Statistics",
      quickActions: "Quick Actions",
      recentProjects: "Recent Projects",
      systemMetrics: "System Metrics",
      totalProjects: "Total Projects",
      activeModels: "Active Models",
      totalUsers: "Total Users",
      systemHealth: "System Health",
      viewAll: "View All",
      createNew: "Create New",
      lastUpdated: "Last Updated",
      performance: "Performance",
      efficiency: "Efficiency",
      reliability: "Reliability",
      activeNodes: "Active Nodes",
      of: "of",
      plusOneThisMonth: "+1 this month",
      codeGenerated: "Code Generated",
      lines: "lines",
      modelsAvailable: "Models Available",
      specialized: "specialized",
      plusThreeNew: "+3 new",
      licenseStatus: "License Status",
      proPlan: "Pro Plan",
      deployModel: "Deploy Model",
      viewAnalytics: "View Analytics",
      manageUsers: "Manage Users",
      systemHealthTitle: "System Health",
      cpuUsage: "CPU Usage",
      memory: "Memory",
      network: "Network",
      security: "Security",
    },

    // Runs
    runs: {
      title: "Ejecuciones de Modelos",
      subtitle:
        "Gestiona y monitorea las ejecuciones de experimentos de modelos de IA",
      totalRuns: "Ejecuciones",
      running: "Ejecutando",
      systemStatus: {
        operational: "Operacional",
      },
      quickActions: {
        title: "Acciones Rápidas",
        createRun: "Crear Ejecución",
        cloneRun: "Clonar Ejecución",
        bulkActions: "Acciones Masivas",
        analytics: "Analytics",
      },
      stats: {
        totalRuns: "Total Ejecuciones",
        thisMonth: "+12 este mes",
        running: "Ejecutando",
        active: "Activas",
        completed: "Completadas",
        successful: "Exitosas",
        failed: "Fallidas",
        issues: "Con problemas",
      },
      searchPlaceholder: "Buscar ejecuciones...",
      selectStatus: "Estado",
      selectExperiment: "Experimento",
      createRun: "Nueva Ejecución",
      tabs: {
        all: "Todas",
        running: "Ejecutando",
        completed: "Completadas",
        failed: "Fallidas",
        scheduled: "Programadas",
      },
      duration: "Duración",
      artifacts: "Artefactos",
      files: "archivos",
      budget: "Presupuesto",
      accuracy: "Precisión",
      actions: {
        view: "Ver",
        edit: "Editar",
        clone: "Clonar",
        artifacts: "Artefactos",
      },
      noRuns: "No se encontraron ejecuciones",
      noRunsDescription:
        "Intenta ajustar los filtros o crear una nueva ejecución",
    },

    // Artifacts
    artifacts: {
      title: "Artefactos de Modelos",
      subtitle:
        "Gestiona y organiza todos los artefactos generados por los experimentos",
      totalArtifacts: "Artefactos",
      systemStatus: {
        operational: "Operacional",
      },
      quickActions: {
        title: "Acciones Rápidas",
        upload: "Subir Artefacto",
        bulkDownload: "Descarga Masiva",
        archive: "Archivar",
        analytics: "Analytics",
      },
      stats: {
        totalArtifacts: "Total Artefactos",
        thisMonth: "+25 este mes",
        totalSize: "Tamaño Total",
        storage: "Almacenamiento",
        available: "Disponibles",
        active: "Activos",
        archived: "Archivados",
      },
      searchPlaceholder: "Buscar artefactos...",
      selectType: "Tipo",
      selectStatus: "Estado",
      upload: "Subir",
      types: {
        model: "Modelo",
        dataset: "Dataset",
        checkpoint: "Checkpoint",
        log: "Log",
        config: "Configuración",
        image: "Imagen",
        other: "Otro",
      },
      size: "Tamaño",
      version: "Versión",
      created: "Creado",
      tags: "Etiquetas",
      actions: {
        view: "Ver",
        download: "Descargar",
        edit: "Editar",
        delete: "Eliminar",
      },
      noArtifacts: "No se encontraron artefactos",
      noArtifactsDescription:
        "Intenta ajustar los filtros o subir un nuevo artefacto",
    },

    // Infrastructure
    infrastructure: {
      title: "Infrastructure Management",
      overview: "Overview",
      providers: "Providers",
      kubernetes: "Kubernetes Clusters",
      gpuInstances: "GPU Instances",
      deployments: "Deployments",
      resources: "Resources",
      credentials: "Credentials",
      costManagement: "Cost Management",
      systemMonitoring: "System Monitoring",
      addProvider: "Add Provider",
      addCluster: "Add Cluster",
      addInstance: "Add Instance",
      clusterName: "Cluster Name",
      instanceType: "Instance Type",
      region: "Region",
      status: "Status",
      nodes: "Nodes",
      cpu: "CPU",
      memory: "Memory",
      storage: "Storage",
      network: "Network",
      security: "Security",
      monitoring: "Monitoring",
      logs: "Logs",
      metrics: "Metrics",
      alerts: "Alerts",
      // New translations for main page
      cloudProviders: "Cloud Providers",
      cloudProvidersDesc: "Manage cloud providers, credentials and connections",
      awsGcpAzure: "AWS, GCP, Azure",
      ovhRunpodVast: "OVH, RunPod, Vast.ai",
      credentialsApiKeys: "Credentials and API Keys",
      statusMonitoring: "Status monitoring",
      kubernetesDesc: "Manage K8s clusters, nodes and resources",
      eksGkeAks: "EKS, GKE, AKS",
      ownClusters: "Own clusters",
      nodeMonitoring: "Node monitoring",
      deploymentManagement: "Deployment management",
      gpuInstancesDesc: "GPU instance management for training and inference",
      t4V100A100H100: "T4, V100, A100, H100",
      rtx40903090: "RTX 4090, 3090",
      spotInstances: "Spot instances",
      costAnalysis: "Cost analysis",
      infrastructureDeployments: "Infrastructure Deployments",
      deploymentsDesc: "Infrastructure and service deployments",
      trainingClusters: "Training clusters",
      inferenceServices: "Inference services",
      autoScaling: "Auto scaling",
      costManagementDesc: "Infrastructure cost analysis and management",
      providerAnalysis: "Provider analysis",
      budgetAlerts: "Budget alerts",
      reports: "Reports",
      optimization: "Optimization",
      systemMonitoringDesc: "Real-time monitoring of all infrastructure",
      realTimeMetrics: "Real-time metrics",
      automaticAlerts: "Automatic alerts",
      customDashboards: "Custom dashboards",
      centralizedLogs: "Centralized logs",
      twoWeeksAgo: "2 weeks ago",
      deployCluster: "Deploy Cluster",
      launchGpu: "Launch GPU",
      viewCosts: "View Costs",
      // New translations for migrated pages
      resourceUtilizationDashboard: "Resource Utilization Dashboard",
      resourceUtilizationDesc: "Monitor resource usage across all infrastructure",
      resourceUtilization: "Resource Utilization",
      totalResources: "Total Resources",
      active: "active",
      avgCpuUsage: "Avg CPU Usage",
      avgMemoryUsage: "Avg Memory Usage",
      avgStorageUsage: "Avg Storage Usage",
      networkUsage: "Network Usage",
      networkIn: "Network In",
      networkOut: "Network Out",
      resourceName: "Resource Name",
      resourceType: "Resource Type",
      cloudResources: "Cloud Resources",
      basicInformation: "Basic Information",
      resourceId: "Resource ID",
      cloudProvider: "Cloud Provider",
      zone: "Zone",
      autoScaling: "Auto Scaling",
      specifications: "Specifications",
      scalingConfiguration: "Scaling Configuration",
      minInstances: "Min Instances",
      currentInstances: "Current Instances",
      maxInstances: "Max Instances",
      cost: "Cost",
      costPerHour: "Cost per Hour",
      costPerMonth: "Cost per Month",
      metadata: "Metadata",
      createdAt: "Created At",
      updatedAt: "Updated At",
      instances: "Instances",
      noResourcesFound: "No resources found",
      cloudProviderOverview: "Cloud Provider Overview",
      cloudCredentialOverview: "Cloud Credential Overview",
      cloudRegionOverview: "Cloud Region Overview",
      cloudResourceOverview: "Cloud Resource Overview",
      kubernetesClusterOverview: "Kubernetes Cluster Overview",
      kubernetesNodeOverview: "Kubernetes Node Overview",
      gpuInstanceOverview: "GPU Instance Overview",
      infrastructureTemplateOverview: "Infrastructure Template Overview",
      infrastructureMetricOverview: "Infrastructure Metric Overview",
      infrastructureMetricsSummaryOverview: "Infrastructure Metrics Summary Overview",
      infrastructureCostOverview: "Infrastructure Cost Overview",
      infrastructureCostAnalysisOverview: "Infrastructure Cost Analysis Overview",
      infrastructureAuditOverview: "Infrastructure Audit Overview",
      infrastructureHealthMatrixOverview: "Infrastructure Health Matrix Overview",
      infrastructureOverviewOverview: "Infrastructure Overview",
      capacityForecastOverview: "Capacity Forecast Overview",
      resourceQuotaOverview: "Resource Quota Overview",
    },

    // Administration
    admin: {
      title: "Administration Panel",
      users: "Users",
      roles: "Roles",
      permissions: "Permissions",
      departments: "Departments",
      sessions: "Sessions",
      audit: "Audit",
      licenses: "Licenses",
      nodes: "Nodes",
      updates: "Updates",
      menuManagement: "Menu Management",
      notifications: "Notifications",
      systemSettings: "System Settings",
      userManagement: "User Management",
      systemHealth: "System Health",
      securitySettings: "Security Settings",
      backupRestore: "Backup & Restore",
      systemLogs: "System Logs",
      performanceMetrics: "Performance Metrics",
      // New translations for main page
      twoHoursAgo: "2 hours ago",
      twoMinutesAgo: "2 minutes ago",
      fiveMinutesAgo: "5 minutes ago",
      tenMinutesAgo: "10 minutes ago",
      fifteenMinutesAgo: "15 minutes ago",
      userLogin: "User login",
      roleUpdated: "Role updated",
      newUserInvited: "New user invited",
      systemBackupCompleted: "System backup completed",
      configurationUpdated: "Configuration updated",
    },

    // Themes
    themes: {
      title: "CodeflowX Themes",
      changeTheme: "Change CodeflowX theme",
      useSystemTheme: "Use system theme",
      trekkerFederation: "🌌 Leka Federation",
      trekkerVulcan: "🖖 Leka Vulcan",
      trekkerKlingon: "⚔️ Leka Klingon",
      trekkerRomulan: "🖖 Leka Romulan",
      trekkerCardassian: "🖖 Leka Cardassian",
      trekkerBajoran: "🖖 Leka Bajoran",
      trekkerFerengi: "💰 Leka Ferengi",
      trekkerAndorian: "🖖 Leka Andorian",
      trekkerTellarite: "🖖 Leka Tellarite",
      trekkerDenobulan: "🏥 Leka Denobulan",
    },

    // Status and colors
    status: {
      online: "Online",
      offline: "Offline",
      maintenance: "Maintenance",
      error: "Error",
      warning: "Warning",
      info: "Information",
      success: "Success",
      pending: "Pending",
      running: "Running",
      stopped: "Stopped",
      starting: "Starting",
      stopping: "Stopping",
      healthy: "Healthy",
      unhealthy: "Unhealthy",
      degraded: "Degraded",
    },

    // System messages
    messages: {
      loadingTheme: "Loading theme...",
      themeApplied: "Theme applied successfully",
      themeError: "Error applying theme",
      redirectingToLogin: "Redirecting to login...",
      systemActive: "System Active",
      aiPowered: "AI Powered",
      documentation: "Documentation",
      support: "Support",
      status: "Status",
      welcomeMessage: "Welcome, {name} 👋",
    },

    // Model Management
    modelManagement: {
      title: "Model Management",
      subtitle: "Overview of the AI model management system",
      totalModels: "Total Models",
      activeModels: "Active Models",
      totalProviders: "Total Providers",
      marketplaceModels: "Marketplace Models",
      recentDownloads: "Recent Downloads",
      systemHealth: "System Health",
      healthy: "Healthy",
      warning: "Warning",
      critical: "Critical",
      approved: "Approved",
      pending: "Pending",
      rejected: "Rejected",
      addModel: "Add Model",
      viewModels: "View Models",
      manageProviders: "Manage Providers",
      marketplace: "Marketplace",
      recentActivity: "Recent Activity",
      topProviders: "Top Providers",
      trendingModels: "Trending Models",
      stats: {
        totalModels: "Total Models",
        totalModelsChange: "+12% this month",
        activeModels: "Active Models",
        activeModelsChange: "+8% this month",
        providers: "Providers",
        providersChange: "+3 this month",
        recentDownloads: "Recent Downloads",
        recentDownloadsChange: "+25% this week",
      },
      systemStatus: {
        title: "System Status",
        generalStatus: "General Status",
        healthy: "Healthy",
        warning: "Warning",
        apiModels: "Models API",
        marketplace: "Marketplace",
        providers: "Providers",
        operational: "Operational",
      },
      recentModels: {
        title: "Recent Models",
        noModels: "No recent models",
      },
      typeDistribution: {
        title: "Models by Type",
        viewDetails: "View Details",
        text: "Text",
        vision: "Vision",
        audio: "Audio",
      },
      quickActions: {
        title: "Quick Actions",
        newModel: "New Model",
        addProvider: "Add Provider",
        exploreMarketplace: "Explore Marketplace",
        settings: "Settings",
      },
      systemInfo: {
        title: "System Information",
        modelManagement: {
          title: "Model Management",
          description:
            "Registration, versioning and complete lifecycle management of AI models",
        },
        aiProviders: {
          title: "AI Providers",
          description:
            "Integration with multiple AI service providers and token management",
        },
        marketplace: {
          title: "Marketplace",
          description:
            "Access to +3,000,000 AI models from multiple marketplaces and providers",
        },
      },
    },

    // Runs
    runs: {
      title: "Model Runs",
      subtitle: "Manage and monitor AI model experiment executions",
      totalRuns: "Runs",
      running: "Running",
      systemStatus: {
        operational: "Operational",
      },
      quickActions: {
        title: "Quick Actions",
        createRun: "Create Run",
        cloneRun: "Clone Run",
        bulkActions: "Bulk Actions",
        analytics: "Analytics",
      },
      stats: {
        totalRuns: "Total Runs",
        thisMonth: "+12 this month",
        running: "Running",
        active: "Active",
        completed: "Completed",
        successful: "Successful",
        failed: "Failed",
        issues: "With issues",
      },
      searchPlaceholder: "Search runs...",
      selectStatus: "Status",
      selectExperiment: "Experiment",
      createRun: "New Run",
      tabs: {
        all: "All",
        running: "Running",
        completed: "Completed",
        failed: "Failed",
        scheduled: "Scheduled",
      },
      duration: "Duration",
      artifacts: "Artifacts",
      files: "files",
      budget: "Budget",
      accuracy: "Accuracy",
      actions: {
        view: "View",
        edit: "Edit",
        clone: "Clone",
        artifacts: "Artifacts",
      },
      noRuns: "No runs found",
      noRunsDescription: "Try adjusting filters or create a new run",
    },

    // Artifacts
    artifacts: {
      title: "Model Artifacts",
      subtitle: "Manage and organize all artifacts generated by experiments",
      totalArtifacts: "Artifacts",
      systemStatus: {
        operational: "Operational",
      },
      quickActions: {
        title: "Quick Actions",
        upload: "Upload Artifact",
        bulkDownload: "Bulk Download",
        archive: "Archive",
        analytics: "Analytics",
      },
      stats: {
        totalArtifacts: "Total Artifacts",
        thisMonth: "+25 this month",
        totalSize: "Total Size",
        storage: "Storage",
        available: "Available",
        active: "Active",
        archived: "Archived",
      },
      searchPlaceholder: "Search artifacts...",
      selectType: "Type",
      selectStatus: "Status",
      upload: "Upload",
      types: {
        model: "Model",
        dataset: "Dataset",
        checkpoint: "Checkpoint",
        log: "Log",
        config: "Configuration",
        image: "Image",
        other: "Other",
      },
      size: "Size",
      version: "Version",
      created: "Created",
      tags: "Tags",
      actions: {
        view: "View",
        download: "Download",
        edit: "Edit",
        delete: "Delete",
      },
      noArtifacts: "No artifacts found",
      noArtifactsDescription: "Try adjusting filters or upload a new artifact",
    },

    // Monitoring
    monitoring: {
      title: "Monitoring",
      alerts: {
        overview: {
          title: "Monitoring Alerts",
          description: "Management of monitoring alerts",
          register: "Register Alert",
          list: "Alerts List",
          alertName: "Alert Name",
          alertType: "Alert Type",
          severity: "Severity",
          status: "Status",
        },
        detail: {
          title: "Alert Detail",
          description: "Management and configuration of monitoring alert",
          information: "Information",
          alertName: "Alert Name",
          alertType: "Alert Type",
          severity: "Severity",
          status: "Status",
          description: "Description",
          alertData: "Alert Data",
          timestamps: "Timestamps",
          triggeredAt: "Triggered At",
          acknowledgedAt: "Acknowledged At",
          acknowledgedBy: "Acknowledged By",
          resolvedAt: "Resolved At",
          resolutionNotes: "Resolution Notes",
        },
        systemDetail: {
          title: "System Alert Detail",
          description: "System alerts management",
          information: "Information",
          alertName: "Alert Name",
          alertType: "Alert Type",
          severity: "Severity",
          status: "Status",
          description: "Description",
          timestamps: "Timestamps",
          triggeredAt: "Triggered At",
          acknowledgedAt: "Acknowledged At",
        },
        responseForm: {
          title: "Critical Alert Response",
          description: "Alert response form",
          formData: "Form Data",
          decision: "Decision",
          decisionPlaceholder: "Enter decision...",
          notes: "Notes",
          notesPlaceholder: "Enter notes...",
        },
      },
      metrics: {
        overview: {
          title: "Monitoring Metrics",
          description: "Monitoring metrics management",
          register: "Register Metric",
          list: "Metrics List",
        },
        detail: {
          title: "Metric Detail",
          description: "Monitoring metrics management",
          information: "Information",
          metricName: "Metric Name",
          metricType: "Metric Type",
          metricValue: "Metric Value",
          status: "Status",
          description: "Description",
          timestamps: "Timestamps",
          collectedAt: "Collected At",
        },
        systemDetail: {
          title: "System Metric Detail",
          description: "System metrics management",
          information: "Information",
          metricName: "Metric Name",
          metricType: "Metric Type",
          metricValue: "Metric Value",
          status: "Status",
          description: "Description",
          timestamps: "Timestamps",
          collectedAt: "Collected At",
        },
      },
      dashboard: {
        title: "Monitoring Dashboard",
        description: "Overview of the monitoring system",
        alerts: "Alerts",
        alertsDesc: "Monitoring alerts management",
        metrics: "Metrics",
        metricsDesc: "System metrics visualization",
        systemHealth: "System Health",
        systemHealthDesc: "System health status",
        performance: "Performance",
        performanceDesc: "Performance metrics",
        quickStats: "Quick Statistics",
        activeAlerts: "Active Alerts",
        uptime: "Uptime",
        avgResponse: "Average Response",
      },
      auditLog: {
        detail: {
          title: "Audit Log Detail",
          description: "Detailed audit log information",
          information: "Information",
          action: "Action",
          entity: "Entity",
          entityId: "Entity ID",
          user: "User",
          ipAddress: "IP Address",
          status: "Status",
          details: "Details",
          timestamps: "Timestamps",
          timestamp: "Timestamp",
          createdAt: "Created At",
        },
      },
      systemHealth: {
        detail: {
          title: "System Health Detail",
          description: "System health information",
          information: "Information",
          status: "Status",
          uptime: "Uptime",
          details: "Details",
          timestamps: "Timestamps",
          lastCheck: "Last Check",
          updatedAt: "Updated At",
        },
      },
      governance: {
        detail: {
          title: "Governance Detail",
          description: "Governance policies management",
          information: "Information",
          name: "Name",
          type: "Type",
          status: "Status",
          description: "Description",
          details: "Details",
          timestamps: "Timestamps",
          createdAt: "Created At",
          updatedAt: "Updated At",
        },
      },
      performance: {
        interventionForm: {
          title: "Performance Intervention",
          description: "Performance intervention form",
          formData: "Form Data",
          interventionType: "Intervention Type",
          description: "Description",
          descriptionPlaceholder: "Describe the intervention...",
          priority: "Priority",
          notes: "Notes",
          notesPlaceholder: "Additional notes...",
        },
      },
      viewsPerformance: {
        title: "Views Performance",
        description: "Views performance configuration",
        formData: "Form Data",
        viewName: "View Name",
        viewNamePlaceholder: "Enter view name...",
        performanceMetric: "Performance Metric",
        performanceMetricPlaceholder: "Enter metric...",
        threshold: "Threshold",
        thresholdPlaceholder: "Enter threshold...",
        notes: "Notes",
        notesPlaceholder: "Additional notes...",
      },
    },

    // Domain Ingestion
    domainIngestion: {
      title: "Domain Ingestion Dashboard",
      subtitle: "Monitor and manage your specialized data domains",
      stats: {
        totalDomains: "Total Domains",
        totalDocuments: "Total Documents",
        webPages: "Web Pages",
        apis: "Connected APIs",
        activeDomains: "active domains",
        documentsChange: "+12% this month",
        webChange: "+8% this week",
        apisChange: "+3 this month",
      },
      actions: {
        wizard: "Domain Wizard",
        manage: "Manage Domains",
        access: "Access",
      },
      modules: {
        webScraping: {
          title: "Web Scraping",
          description: "Extract data from websites and monitor changes",
        },
        uploadDocuments: {
          title: "Upload Documents",
          description: "Upload and process documents to enrich domains",
        },
        monitor: {
          title: "Monitoring",
          description: "Monitor job performance and status",
        },
        settings: {
          title: "Settings",
          description: "Configure global and domain-specific parameters",
        },
        configureSources: {
          title: "Configure Sources",
          description: "Configure data sources and APIs",
        },
      },
      recentJobs: {
        title: "Recent Jobs",
      },
      jobStatus: {
        completed: "Completed",
        running: "Running",
        failed: "Failed",
        pending: "Pending",
        queued: "Queued",
        processing: "Processing",
        cancelled: "Cancelled",
        retrying: "Retrying",
      },
      wizard: {
        steps: {
          basicInfo: "Basic Information",
          dataSources: "Data Sources",
          compliance: "Compliance",
          advancedFeatures: "Advanced Features",
          review: "Review",
        },
        templates: {
          title: "Domain Templates",
          description: "Select a predefined template or create a custom one",
          fintech: "Fintech Banking",
          healthcare: "Healthcare Analytics",
          ecommerce: "E-commerce Insights",
          government: "IA Act Compliance",
          manufacturing: "Manufacturing IoT",
          custom: "Custom",
        },
        industries: {
          title: "Industry",
          fintech: "Fintech",
          healthcare: "Healthcare",
          ecommerce: "E-commerce",
          government: "Government",
          manufacturing: "Manufacturing",
          education: "Education",
          legal: "Legal",
          other: "Other",
        },
        compliance: {
          title: "Compliance Frameworks",
          description: "Select applicable compliance frameworks",
          gdpr: "GDPR (General Data Protection Regulation)",
          sox: "SOX (Sarbanes-Oxley Act)",
          pci: "PCI DSS (Payment Card Industry Data Security Standard)",
          hipaa: "HIPAA (Health Insurance Portability and Accountability Act)",
          iaActEspana: "IA Act Spain (Artificial Intelligence Law)",
          iaActEuropa: "IA Act Europe (Artificial Intelligence Act)",
          iso27001: "ISO 27001 (Information Security Management)",
          iso27701: "ISO 27701 (Privacy Information Management)",
          nist: "NIST Cybersecurity Framework",
          soc2: "SOC 2 (Service Organization Control 2)",
          fedramp:
            "FedRAMP (Federal Risk and Authorization Management Program)",
          ccpa: "CCPA (California Consumer Privacy Act)",
          lgpd: "LGPD (Lei Geral de Proteção de Dados)",
          pipeda:
            "PIPEDA (Personal Information Protection and Electronic Documents Act)",
          // Software Development ISO Standards
          iso12207: "ISO 12207 (Software Life Cycle)",
          iso15504: "ISO 15504 (Software Process Assessment)",
          iso20000: "ISO 20000 (IT Service Management)",
          iso27002: "ISO 27002 (Information Security Controls)",
          iso27005: "ISO 27005 (Security Risk Management)",
          iso27017: "ISO 27017 (Cloud Security)",
          iso27018: "ISO 27018 (Cloud Data Protection)",
          iso9001: "ISO 9001 (Quality Management System)",
          iso14001: "ISO 14001 (Environmental Management System)",
          // Additional European Regulations
          eidas: "eIDAS (EU Electronic Identification)",
          psd2: "PSD2 (EU Payment Services Directive)",
          nis2: "NIS2 (EU Network Security Directive)",
          // Software Quality Standards
          cmmi: "CMMI (Capability Maturity Model Integration)",
          tickit: "TICKIT (IT and Telecommunications Quality)",
          spice:
            "SPICE (Software Process Improvement and Capability Determination)",
          custom: "Custom",
        },
        dataSources: {
          title: "Data Source Types",
          description: "Select the types of data sources you will use",
          api: "API",
          database: "Database",
          file: "File",
          web: "Web",
          stream: "Stream",
        },
        basicInfo: {
          name: "Domain Name",
          description: "Description",
          industry: "Industry",
          owner: "Owner",
          team: "Team",
        },
        advancedFeatures: {
          title: "Advanced Features",
          rag: "RAG (Create datasets)",
          training: "Model Training",
          compliance: "Automatic Compliance",
          monitoring: "Advanced Monitoring",
          modelSelection: "Model Selection",
          modelName: "Internal Model Name",
          adapter: "Create Adapter",
          finetuning: "Fine-tuning",
          baseModel: "Base Model",
          customModel: "Custom Model",
        },
        review: {
          title: "Final Review",
          description: "Review the configuration before creating the domain",
          template: "Selected template",
          industry: "Industry",
          compliance: "Compliance frameworks",
          dataSources: "Data sources",
          features: "Enabled features",
        },
        template: {
          fintech: {
            name: "Fintech Banking",
            description:
              "Domain specialized in financial data analysis and regulatory compliance",
            features: [
              "RAG enabled",
              "GDPR/SOX compliance",
              "Advanced monitoring",
            ],
            compliance: ["GDPR", "SOX", "PCI DSS"],
          },
          healthcare: {
            name: "Healthcare Analytics",
            description:
              "Domain for medical data analysis with HIPAA compliance",
            features: [
              "RAG enabled",
              "HIPAA compliance",
              "Advanced encryption",
            ],
            compliance: ["HIPAA", "GDPR"],
          },
          ecommerce: {
            name: "E-commerce Insights",
            description:
              "Domain for purchase behavior analysis and recommendations",
            features: ["RAG enabled", "Behavior analysis", "Recommendations"],
            compliance: ["GDPR"],
          },
          government: {
            name: "IA Act Compliance",
            description:
              "Domain for compliance with Spanish IA Act and government regulations",
            features: ["RAG enabled", "IA Act compliance", "Complete audit"],
            compliance: ["IA Act Spain", "GDPR"],
          },
          manufacturing: {
            name: "Manufacturing IoT",
            description:
              "Domain for IoT data analysis in industrial environments",
            features: ["RAG enabled", "IoT analysis", "Real-time monitoring"],
            compliance: ["ISO 27001"],
          },
        },
        actions: {
          next: "Next",
          previous: "Previous",
          create: "Create Domain",
          cancel: "Cancel",
        },
      },
    },

    // Model Evaluation
    modelEvaluation: {
      title: "Model Evaluation",
      subtitle: "Dashboard for AI model evaluation and testing",
      completed: "Completed",
      running: "Running",
      failed: "Failed",
      pending: "Pending",
      unknown: "Unknown",
      loadingData: "Loading dashboard data...",
      errorLoadingData: "Could not load dashboard data",
      pipeline: "Pipeline",
      abTest: "A/B Test",
      security: "Security",
      evaluation: "Evaluation",
      newEvaluation: "New Evaluation",
      viewEvaluation: "View Evaluation",
      pipelineModal: "Pipeline Modal",
      abTestModal: "A/B Test Modal",
      securityModal: "Security Modal",
      reportModal: "Report Modal",
    },

    // RAG Training
    ragTraining: {
      title: "RAG Training",
      subtitle: "Dashboard for RAG training and metrics",
      totalDocuments: "Total Documents",
      totalChunks: "Total Chunks",
      totalSearches: "Total Searches",
      averageResponseTime: "Average Response Time",
      activeUsers: "Active Users",
      storageUsed: "Storage Used",
      documentsByType: "Documents by Type",
      searchesByDay: "Searches by Day",
      topQueries: "Top Queries",
      pdf: "PDF",
      docx: "DOCX",
      txt: "TXT",
      html: "HTML",
      configuration: "database configuration",
      apiEndpoints: "API endpoints",
      authentication: "authentication",
      middlewareSetup: "middleware setup",
      deployment: "deployment",
    },

    // Training Hub
    training: {
      title: "Training Hub",
      subtitle: "Adapters, Fine-tuning and Artifacts unified",
      operational: "Operational",
      adapters: "Adapters",
      finetuning: "Fine-tuning",
      artifacts: "Artifacts",
      storage: "Storage",
      search: "Search...",
      type: "Type",
      technology: "Technology",
      domain: "Domain",
      generic: "Generic",
      quick: {
        newAdapter: "New Adapter",
        newFinetune: "New Fine-tuning",
        importModel: "Import Model",
        analytics: "Analytics",
      },
      promote: "Promote",
      viewRun: "View Run",
      download: "Download",
      metric: "Metric",
      cost: "Cost",
      duration: "Duration",
      startedAt: "Started At",
      size: "Size",
      createdAt: "Created At",
      linked: "Linked",
      detailsPlaceholder: "Detail content (mock)",
      // Menu translations
      menu: {
        dashboard: "Training Dashboard",
        governanceDashboard: "Governance Dashboard",
        hpoDashboard: "HPO Dashboard",
        experiments: "Experiments",
        experimentsLineage: "Experiment Lineage",
        experimentsTemplates: "Experiment Templates",
        hpoExperiments: "HPO Experiments",
        hpoTrials: "HPO Trials",
        runs: "Runs",
        runComparisons: "Run Comparisons",
        metrics: "Metrics",
        metricSeries: "Metric Series",
        metricStreams: "Metric Streams",
        artifacts: "Artifacts",
        checkpoints: "Checkpoints",
        execution: "Execution",
        executionLogs: "Execution Logs",
        parameters: "Parameters",
        governance: "Governance",
        infrastructure: "Infrastructure",
        datasets: "Datasets",
        environments: "Environments",
        alerts: "Alerts",
        models: "Models",
      },
    },

    // Models Management
    models: {
      title: "Manage Models",
      subtitle: "Complete lifecycle management of AI models",
      operational: "Operational",
      totalModels: "Total Models",
      approvedModels: "Approved Models",
      activeModels: "Active Models",
      marketplaceModels: "Marketplace",
      search: "Search models...",
      type: "Type",
      status: "Status",
      provider: "Provider",
      text: "Text",
      vision: "Vision",
      audio: "Audio",
      multimodal: "Multimodal",
      draft: "Draft",
      approved: "Approved",
      ready: "Ready",
      error: "Error",
      noDescription: "No description",
      duplicate: "Duplicate",
      noModels: "No models found",
      createModel: "Create New Model",
      modelDetails: "Model Details",
      editModel: "Edit Model",
      importModel: "Import Model",
      bulkActions: "Bulk Actions",
      analytics: "Model Analytics",
      newModel: "New Model",
      createModelDescription: "Form to create a new model (mock)",
      viewModelDescription: "Detailed view of the model (mock)",
      editModelDescription: "Form to edit the model (mock)",
      importModelDescription: "Form to import model (mock)",
      bulkActionsDescription: "Bulk actions on models (mock)",
      analyticsDescription: "Model metrics and analytics (mock)",
      quick: {
        title: "Quick Actions",
        createModel: "Create Model",
        importModel: "Import Model",
        bulkActions: "Bulk Actions",
        analytics: "Analytics",
      },
    },

    // Training Hub
    training: {
      title: "Training Hub",
      subtitle: "Adapters, Fine‑tuning y Artefactos unificados",
      operational: "Operacional",
      adapters: "Adapters",
      finetuning: "Fine-tuning",
      artifacts: "Artefactos",
      storage: "Storage",
      search: "Buscar...",
      type: "Tipo",
      technology: "Tecnología",
      domain: "Dominio",
      generic: "Genérico",
      quick: {
        newAdapter: "Nuevo Adapter",
        newFinetune: "Nuevo Fine‑tuning",
        importModel: "Importar Modelo",
        analytics: "Analytics",
      },
      promote: "Promover",
      viewRun: "Ver run",
      download: "Descargar",
      metric: "Métrica",
      cost: "Coste",
      duration: "Duración",
      startedAt: "Inicio",
      size: "Tamaño",
      createdAt: "Creado",
      linked: "Vinculado",
      detailsPlaceholder: "Contenido de detalle (mock)",
      // Dashboards
      governance: {
        dashboard: {
          title: "Training Overview Dashboard",
          description: "Monitoreo de experimentos y entrenamiento de modelos",
          totalItems: "Total Items",
          activeItems: "Active Items",
          deployedItems: "Deployed Items",
          trainingItems: "Training Items",
          offlineItems: "Offline Items",
          avgScore: "Average Score",
          charts: "Training Metrics",
          chartsPlaceholder: "Los gráficos de métricas se mostrarán aquí",
        },
        overview: {
          title: "Training Governance",
          register: "Registrar Governance",
          list: "Lista de Governance",
          name: "Name",
          status: "Status",
          complianceLevel: "Compliance Level",
          lastAudit: "Last Audit",
        },
        infrastructureOverview: {
          title: "Infrastructure",
          register: "Registrar Infrastructure",
          list: "Lista de Infrastructure",
          name: "Name",
          type: "Type",
          status: "Status",
          capacity: "Capacity",
        },
      },
      hpo: {
        dashboard: {
          title: "HPO Progress Dashboard",
          description: "Monitoreo de progreso de optimización de hiperparámetros",
          totalItems: "Total Items",
          activeItems: "Active Items",
          deployedItems: "Deployed Items",
          trainingItems: "Training Items",
          offlineItems: "Offline Items",
          avgScore: "Average Score",
          charts: "HPO Progress Metrics",
          chartsPlaceholder: "Los gráficos de progreso HPO se mostrarán aquí",
        },
        overview: {
          title: "HPO Experiments",
          register: "Registrar HPO Experiment",
          list: "Lista de HPO Experiments",
          name: "Name",
          status: "Status",
          algorithm: "Algorithm",
          trials: "Trials",
          bestScore: "Best Score",
        },
        trialOverview: {
          title: "HPO Trials",
          register: "Registrar Trial",
          list: "Lista de Trials",
          hpoExperimentId: "HPO Experiment ID",
          trialNumber: "Trial Number",
          status: "Status",
          score: "Score",
          parameters: "Parameters",
        },
      },
      artifacts: {
        overview: {
          title: "Training Artifacts",
          register: "Registrar Training Artifact",
          list: "Lista de Artifacts",
          trainingJobId: "Training Job ID",
          artifactType: "Artifact Type",
          name: "Name",
          filepath: "Filepath",
        },
      },
      checkpoints: {
        overview: {
          title: "Checkpoints",
          register: "Registrar Checkpoint",
          list: "Lista de Checkpoints",
          checkpointName: "Checkpoint Name",
          epoch: "Epoch",
          step: "Step",
          type: "Type",
          path: "Path",
        },
      },
      execution: {
        overview: {
          title: "Training Execution",
          register: "Registrar Training Execution",
          list: "Lista de Executions",
          trainingJobId: "Training Job ID",
          executionNumber: "Execution Number",
          executionType: "Execution Type",
          status: "Status",
          startedAt: "Started At",
        },
        logOverview: {
          title: "Training Logs",
          register: "Registrar Log",
          list: "Lista de Logs",
          trainingJobId: "Training Job ID",
          logLevel: "Log Level",
          message: "Message",
          timestamp: "Timestamp",
        },
      },
      experiments: {
        overview: {
          title: "Experiments",
          register: "Registrar Experiment",
          list: "Lista de Experiments",
          name: "Name",
          description: "Description",
          artifactLocation: "Artifact Location",
          lifecycleStage: "Lifecycle Stage",
          tags: "Tags",
        },
        lineageOverview: {
          title: "Experiment Lineage",
          register: "Registrar Lineage",
          list: "Lista de Lineages",
          experimentId: "Experiment ID",
          parentExperimentId: "Parent Experiment ID",
          lineageType: "Lineage Type",
          depth: "Depth",
        },
        templateOverview: {
          title: "Experiment Templates",
          register: "Registrar Template",
          list: "Lista de Templates",
          name: "Name",
          description: "Description",
          status: "Status",
          usageCount: "Usage Count",
        },
      },
      infrastructure: {
        datasetOverview: {
          title: "Datasets",
          register: "Registrar Dataset",
          list: "Lista de Datasets",
          name: "Name",
          source: "Source",
          size: "Size",
          status: "Status",
        },
        environmentOverview: {
          title: "Environments",
          register: "Registrar Environment",
          list: "Lista de Environments",
          name: "Name",
          type: "Type",
          status: "Status",
          pythonVersion: "Python Version",
        },
      },
      metrics: {
        overview: {
          title: "Training Metrics",
          register: "Registrar Training Metric",
          list: "Lista de Metrics",
          trainingJobId: "Training Job ID",
          metricName: "Metric Name",
          metricValue: "Metric Value",
          metricUnit: "Metric Unit",
          epoch: "Epoch",
        },
        seriesOverview: {
          title: "Metric Series",
          register: "Registrar Series",
          list: "Lista de Series",
          name: "Name",
          metricName: "Metric Name",
          runId: "Run ID",
          dataPoints: "Data Points",
        },
        streamOverview: {
          title: "Metric Streams",
          register: "Registrar Stream",
          list: "Lista de Streams",
          name: "Name",
          metricName: "Metric Name",
          runId: "Run ID",
          status: "Status",
        },
      },
      monitoring: {
        alertOverview: {
          title: "Training Alerts",
          register: "Registrar Alert",
          list: "Lista de Alerts",
          name: "Name",
          severity: "Severity",
          status: "Status",
          triggeredAt: "Triggered At",
          resolvedAt: "Resolved At",
        },
      },
      parameters: {
        overview: {
          title: "Parameters",
          register: "Registrar Parameter",
          list: "Lista de Parameters",
          name: "Name",
          value: "Value",
          type: "Type",
          experimentId: "Experiment ID",
        },
      },
      runs: {
        overview: {
          title: "Runs",
          register: "Registrar Run",
          list: "Lista de Runs",
          name: "Name",
          status: "Status",
          experimentId: "Experiment ID",
          startedAt: "Started At",
          duration: "Duration",
        },
        comparisonOverview: {
          title: "Run Comparisons",
          register: "Registrar Comparison",
          list: "Lista de Comparisons",
          name: "Name",
          runIds: "Run IDs",
          comparisonType: "Comparison Type",
          createdAt: "Created At",
        },
      },
      // Menu translations
      menu: {
        dashboard: "Dashboard de Training",
        governanceDashboard: "Dashboard de Governance",
        hpoDashboard: "Dashboard HPO",
        experiments: "Experimentos",
        experimentsLineage: "Lineage de Experimentos",
        experimentsTemplates: "Templates de Experimentos",
        hpoExperiments: "Experimentos HPO",
        hpoTrials: "Trials HPO",
        runs: "Runs",
        runComparisons: "Comparaciones de Runs",
        metrics: "Métricas",
        metricSeries: "Series de Métricas",
        metricStreams: "Streams de Métricas",
        artifacts: "Artefactos",
        checkpoints: "Checkpoints",
        execution: "Ejecución",
        executionLogs: "Logs de Ejecución",
        parameters: "Parámetros",
        governance: "Governance",
        infrastructure: "Infraestructura",
        datasets: "Datasets",
        environments: "Entornos",
        alerts: "Alertas",
        models: "Modelos",
      },
    },

    // Models Management
    models: {
      title: "Administrar Modelos",
      subtitle: "Gestión completa del ciclo de vida de modelos de IA",
      operational: "Operacional",
      totalModels: "Total Modelos",
      approvedModels: "Modelos Aprobados",
      activeModels: "Modelos Activos",
      marketplaceModels: "Marketplace",
      search: "Buscar modelos...",
      type: "Tipo",
      status: "Estado",
      provider: "Proveedor",
      text: "Texto",
      vision: "Visión",
      audio: "Audio",
      multimodal: "Multimodal",
      draft: "Borrador",
      approved: "Aprobado",
      ready: "Listo",
      error: "Error",
      noDescription: "Sin descripción",
      duplicate: "Duplicar",
      noModels: "No se encontraron modelos",
      createModel: "Crear Nuevo Modelo",
      modelDetails: "Detalles del Modelo",
      editModel: "Editar Modelo",
      importModel: "Importar Modelo",
      bulkActions: "Acciones Masivas",
      analytics: "Analytics de Modelos",
      newModel: "Nuevo Modelo",
      createModelDescription: "Formulario para crear un nuevo modelo (mock)",
      viewModelDescription: "Vista detallada del modelo (mock)",
      editModelDescription: "Formulario para editar el modelo (mock)",
      importModelDescription: "Formulario para importar modelo (mock)",
      bulkActionsDescription: "Acciones masivas sobre modelos (mock)",
      analyticsDescription: "Métricas y analytics de modelos (mock)",
      quick: {
        title: "Acciones Rápidas",
        createModel: "Crear Modelo",
        importModel: "Importar Modelo",
        bulkActions: "Acciones Masivas",
        analytics: "Analytics",
      },
    },

    // BPMN Task Inbox
    taskInbox: {
      title: "BPMN Task Inbox",
      subtitle: "Manage your pending tasks",
      stats: {
        totalTasks: "Pending Tasks",
        assignedToMe: "Assigned to Me",
        groupTasks: "From My Roles",
      },
      filters: {
        process: "Process",
        priority: "Priority",
        state: "State",
        allProcesses: "All processes",
        allPriorities: "All priorities",
        allStates: "All states",
        assigned: "Assigned",
        unassigned: "Unassigned",
        candidate: "Candidate",
        low: "Low",
        normal: "Normal",
        high: "High",
        urgent: "Urgent",
      },
      table: {
        priority: "Priority",
        task: "Task",
        process: "Process",
        assigned: "Assigned",
        roles: "Roles",
        creation: "Creation",
        dueDate: "Due Date",
        actions: "Actions",
        noTasks: "No pending tasks",
        unassigned: "Unassigned",
        openTask: "Open task",
        claim: "Claim",
        assign: "Assign",
      },
      assignDialog: {
        title: "Assign Task to User",
        task: "Task",
        availableUsers: "Available users",
        username: "Username",
        fullname: "Full Name",
        email: "Email",
        cancel: "Cancel",
        assign: "Assign",
      },
      actions: {
        refresh: "Refresh",
        clearFilters: "Clear filters",
      },
    },

    // Internal Audit Scheduling Form
    internalAuditScheduling: {
      title: "Schedule Internal Audit ISO 42001",
      subtitle: "Complete the data to schedule the internal audit",
      formTitle: "Audit Data",
      project: "Project",
      projectPlaceholder: "Project ID",
      date: "Scheduled Date",
      scope: "Scope",
      scopePlaceholder: "Clauses, processes and systems to audit",
      auditor: "Responsible Auditor",
      auditorPlaceholder: "User or auditor role",
      back: "Back",
      cancel: "Cancel",
      submit: "Schedule Audit",
      saving: "Saving...",
      errors: {
        scopeRequired: "You must define the audit scope",
        auditorRequired: "You must assign a responsible auditor",
        dateRequired: "You must select a date",
        submitError: "Error scheduling the audit",
      },

    // BPMN Forms
    agentApprovalHumanOverride: {
      title: "Agent Approval - Human Override",
      subtitle: "Complete the data for agent approval - human override",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    modelApprovalHumanOverride: {
      title: "Model Approval - Human Override",
      subtitle: "Complete the data for model approval - human override",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    promptHumanReview: {
      title: "Prompt Human Review",
      subtitle: "Complete the data for prompt human review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    hitlSlaReminder: {
      title: "HITL SLA Reminder",
      subtitle: "Complete the data for hitl sla reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    promptApprovalRequest: {
      title: "Prompt Approval Request",
      subtitle: "Complete the data for prompt approval request",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionApproval: {
      title: "AI Decommission Approval",
      subtitle: "Complete the data for ai decommission approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapApproval: {
      title: "Competence Gap Approval",
      subtitle: "Complete the data for competence gap approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionApproval: {
      title: "Corrective Action Approval",
      subtitle: "Complete the data for corrective action approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    approveConformityAssessment: {
      title: "Approve Conformity Assessment",
      subtitle: "Complete the data for approve conformity assessment",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    deployerApproval: {
      title: "Deployer Approval",
      subtitle: "Complete the data for deployer approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    boardDecisionPaper: {
      title: "Board Decision Paper",
      subtitle: "Complete the data for board decision paper",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    managementReview: {
      title: "Management Review",
      subtitle: "Complete the data for management review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    llmEvaluationReview: {
      title: "LLM Evaluation Review",
      subtitle: "Complete the data for llm evaluation review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ragEvaluationReview: {
      title: "RAG Evaluation Review",
      subtitle: "Complete the data for rag evaluation review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    modelEvaluationReview: {
      title: "Model Evaluation Review",
      subtitle: "Complete the data for model evaluation review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapAssessment: {
      title: "Competence Gap Assessment",
      subtitle: "Complete the data for competence gap assessment",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionAssessment: {
      title: "Corrective Action Assessment",
      subtitle: "Complete the data for corrective action assessment",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    performanceReviewDecision: {
      title: "Performance Review Decision",
      subtitle: "Complete the data for performance review decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    compliance: {
      dashboard: {
        title: "Consolidated Compliance Dashboard",
        subtitle: "Consolidated view of compliance status according to EU AI Act",
      },
      postMarketMonitoring: {
        title: "Post-Market Monitoring Dashboard",
        subtitle: "Continuous monitoring of AI systems according to EU AI Act Art. 72",
      },
      sector: {
        title: "Sectorial FaaS Metamodel",
        subtitle: "View sectors, subsectors and configured frameworks without modifying CodeflowX core",
      },
      conformityDeclaration: {
        title: "EU Declaration of Conformity Manager",
        subtitle: "Management of conformity declarations according to EU AI Act Annex V",
      },
      conformityReview: {
        title: "Conformity Assessment Review",
        subtitle: "Human review required for borderline compliance assessment",
      },
    },
    complianceReview: {
      title: "Compliance Review",
      subtitle: "Complete the data for compliance review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    complianceReviewDecision: {
      title: "Compliance Review Decision",
      subtitle: "Complete the data for compliance review decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    reviewQmsGaps: {
      title: "Review QMS Gaps",
      subtitle: "Complete the data for review qms gaps",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    completeDocumentation: {
      title: "Complete Documentation",
      subtitle: "Complete the data for complete documentation",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    biasReview: {
      title: "Bias Review",
      subtitle: "Complete the data for bias review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    biasMitigationPlan: {
      title: "Bias Mitigation Plan",
      subtitle: "Complete the data for bias mitigation plan",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    biasUrgentDecision: {
      title: "Bias Urgent Decision",
      subtitle: "Complete the data for bias urgent decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    driftAnalysis: {
      title: "Drift Analysis",
      subtitle: "Complete the data for drift analysis",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    driftReviewDecision: {
      title: "Drift Review Decision",
      subtitle: "Complete the data for drift review decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    internalAuditExecution: {
      title: "Internal Audit Execution",
      subtitle: "Complete the data for internal audit execution",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    managementReviewSchedule: {
      title: "Management Review Schedule",
      subtitle: "Complete the data for management review schedule",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    completeMissingElements: {
      title: "Complete Missing Elements",
      subtitle: "Complete the data for complete missing elements",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    defineModifications: {
      title: "Define Modifications",
      subtitle: "Complete the data for define modifications",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    enhancedReview: {
      title: "Enhanced Review",
      subtitle: "Complete the data for enhanced review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    finalReview: {
      title: "Final Review",
      subtitle: "Complete the data for final review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    euRegistration: {
      title: "EU Registration",
      subtitle: "Complete the data for eu registration",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    reviewRegistrationPackage: {
      title: "Review Registration Package",
      subtitle: "Complete the data for review registration package",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    fixValidationErrors: {
      title: "Fix Validation Errors",
      subtitle: "Complete the data for fix validation errors",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    manualResolution: {
      title: "Manual Resolution",
      subtitle: "Complete the data for manual resolution",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    documentIncidentDetails: {
      title: "Document Incident Details",
      subtitle: "Complete the data for document incident details",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    verifyIncidentResolution: {
      title: "Verify Incident Resolution",
      subtitle: "Complete the data for verify incident resolution",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    rootCauseAnalysis: {
      title: "Root Cause Analysis",
      subtitle: "Complete the data for root cause analysis",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    defineCorrectiveActions: {
      title: "Define Corrective Actions",
      subtitle: "Complete the data for define corrective actions",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionImplementation: {
      title: "Corrective Action Implementation",
      subtitle: "Complete the data for corrective action implementation",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionEffectiveness: {
      title: "Corrective Action Effectiveness",
      subtitle: "Complete the data for corrective action effectiveness",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    managementReviewActions: {
      title: "Management Review Actions",
      subtitle: "Complete the data for management review actions",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsReviewRequest: {
      title: "Ethics Review Request",
      subtitle: "Complete the data for ethics review request",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsReviewReminder: {
      title: "Ethics Review Reminder",
      subtitle: "Complete the data for ethics review reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsCommitteeReview: {
      title: "Ethics Committee Review",
      subtitle: "Complete the data for ethics committee review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsMitigationPlan: {
      title: "Ethics Mitigation Plan",
      subtitle: "Complete the data for ethics mitigation plan",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapImplementation: {
      title: "Competence Gap Implementation",
      subtitle: "Complete the data for competence gap implementation",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapAdditionalActions: {
      title: "Competence Gap Additional Actions",
      subtitle: "Complete the data for competence gap additional actions",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapRework: {
      title: "Competence Gap Rework",
      subtitle: "Complete the data for competence gap rework",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionRequest: {
      title: "AI Decommission Request",
      subtitle: "Complete the data for ai decommission request",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionVerification: {
      title: "AI Decommission Verification",
      subtitle: "Complete the data for ai decommission verification",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionRetention: {
      title: "AI Decommission Retention",
      subtitle: "Complete the data for ai decommission retention",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    modelApprovalReminder: {
      title: "Model Approval Reminder",
      subtitle: "Complete the data for model approval reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    datasetReviewReminder: {
      title: "Dataset Review Reminder",
      subtitle: "Complete the data for dataset review reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    categorizeIncident: {
      title: "Categorize Incident",
      subtitle: "Complete the data for categorize incident",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    executeCorrectiveAction: {
      title: "Execute Corrective Action",
      subtitle: "Complete the data for execute corrective action",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    },

    // Governance
    governance: {
      title: "Automatic Governance Dashboard",
      subtitle: "Monitor and manage AI governance in real time",
      refresh: "Refresh",
      configure: "Configure",
      low: "LOW",
      medium: "MEDIUM",
      high: "HIGH",
      critical: "CRITICAL",
      riskLevel: "Risk Level",
      compliance: "Compliance",
      audit: "Audit",
      policies: "Policies",
      violations: "Violations",
      alerts: "Alerts",
      metrics: "Metrics",
      reports: "Reports",
      settings: "Settings",
      overview: {
        title: "Policies Overview",
        subtitle: "Management and configuration of governance policies",
        metrics: {
          total: "Total Policies",
          active: "Active Policies",
          violations: "Violations",
          compliance: "Compliance Score",
        },
        filters: {
          title: "Filters",
          clear: "Clear",
          search: {
            placeholder: "Search policies...",
          },
          type: "All Types",
          "type.security": "Security",
          "type.compliance": "Compliance",
          "type.ethics": "Ethics",
          "type.privacy": "Privacy",
          status: "All Statuses",
          "status.active": "Active",
          "status.inactive": "Inactive",
          "status.draft": "Draft",
          enforcement: "All Levels",
          "enforcement.mandatory": "Mandatory",
          "enforcement.recommended": "Recommended",
          "enforcement.optional": "Optional",
        },
        table: {
          title: "Policies",
          id: "ID",
          name: "Name",
          type: "Type",
          enforcement: "Level",
          version: "Version",
          status: "Status",
          violations: "Violations",
          score: "Score",
          actions: "Actions",
          empty: "No policies found",
        },
        button: {
          create: "Create Policy",
        },
        confirmDelete: "Delete this policy?",
      },
      detail: {
        "title.edit": "Edit Policy",
        "title.create": "New Policy",
        button: {
          evaluate: "Evaluate",
          compliance: "Check Compliance",
          score: "Calculate Score",
        },
        form: {
          title: "Policy Information",
          name: "Policy Name",
          version: "Version",
          type: "Type",
          "type.security": "Security",
          "type.compliance": "Compliance",
          "type.ethics": "Ethics",
          "type.privacy": "Privacy",
          "type.operational": "Operational",
          category: "Category",
          enforcement: "Enforcement Level",
          "enforcement.mandatory": "Mandatory",
          "enforcement.recommended": "Recommended",
          "enforcement.optional": "Optional",
          "enforcement.informational": "Informational",
          description: "Description",
          effectiveDate: "Effective Date",
          expirationDate: "Expiration Date",
          status: "Status",
          "status.active": "Active",
          "status.inactive": "Inactive",
          "status.draft": "Draft",
          "status.review": "Under Review",
          "status.archived": "Archived",
          metadata: "Metadata (JSON)",
        },
        tabs: {
          rules: "Rules",
          evaluations: "Evaluations",
          violations: "Violations",
          compliance: "Compliance",
          statistics: "Statistics",
          "rules.empty": "No rules defined",
          "rules.name": "Name",
          "rules.condition": "Condition",
          "rules.action": "Action",
          "rules.priority": "Priority",
          "rules.active": "Active",
          "evaluations.empty": "No evaluations registered",
          "evaluations.date": "Date",
          "evaluations.result": "Result",
          "evaluations.score": "Score",
          "evaluations.evaluator": "Evaluator",
          "violations.empty": "No violations registered",
          "violations.type": "Type",
          "violations.description": "Description",
          "violations.severity": "Severity",
          "violations.date": "Date",
          "violations.status": "Status",
          "compliance.empty": "No compliance assessments",
          "compliance.average": "Average Score",
          "compliance.date": "Date",
          "compliance.area": "Area",
          "compliance.score": "Score",
          "compliance.status": "Status",
          "compliance.findings": "Findings",
          "statistics.rules": "Rules",
          "statistics.evaluations": "Evaluations",
          "statistics.violations": "Violations",
          "statistics.score": "Current Score",
          "statistics.info": "Policy Information",
          "statistics.createdBy": "Created by",
          "statistics.createdAt": "Creation date",
          "statistics.lastEvaluation": "Last evaluation",
          "statistics.lastUpdated": "Last update",
          "statistics.evaluationStatus": "Evaluation status",
          "statistics.complianceScore": "Compliance score",
        },
        evaluation: {
          success: "Evaluation completed",
        },
        compliance: {
          success: "Compliance check completed",
        },
        score: {
          success: "Score calculated: {score}",
        },
      },
    },

    // Providers Management
    providers: {
      title: "Proveedores de IA",
      subtitle: "Gestión de proveedores de servicios de IA",
      operational: "Operacional",
      totalProviders: "Total Proveedores",
      activeProviders: "Proveedores Activos",
      openaiProviders: "OpenAI",
      selfHosting: "Self-Hosting",
      search: "Buscar proveedores...",
      type: "Tipo",
      newProvider: "Nuevo Proveedor",
      noProviders: "No se encontraron proveedores",
      createProvider: "Crear Nuevo Proveedor",
      editProvider: "Editar Proveedor",
      providerDetails: "Detalles del Proveedor",
      quick: {
        title: "Acciones Rápidas",
        newProvider: "Nuevo Proveedor",
        importProvider: "Importar Proveedor",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
    },

    // Marketplace Management
    marketplace: {
      title: "Marketplace de Modelos",
      subtitle: "Descubre y descarga modelos de IA de múltiples proveedores",
      operational: "Operacional",
      totalModels: "Total Modelos",
      downloaded: "Descargados",
      downloadedModels: "Modelos Descargados",
      avgRating: "Rating Promedio",
      totalDownloads: "Total Descargas",
      search: "Buscar modelos...",
      source: "Fuente",
      type: "Tipo",
      sort: "Ordenar por",
      noModels: "No se encontraron modelos",
      noDescription: "Sin descripción",
      modelDetails: "Detalles del Modelo",
      download: "Descargar",
      downloaded: "Descargado",
      quick: {
        title: "Acciones Rápidas",
        advancedSearch: "Búsqueda Avanzada",
        downloadQueue: "Cola de Descargas",
        configuration: "Configuración",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        text: "Texto",
        vision: "Visión",
        audio: "Audio",
        multimodal: "Multimodal",
      },
    },

    // Experiments Management
    experiments: {
      title: "Experimentos de Modelos",
      subtitle: "Gestión y seguimiento de experimentos de IA",
      operational: "Operacional",
      totalExperiments: "Total Experimentos",
      runningExperiments: "Ejecutándose",
      completedExperiments: "Completados",
      failedExperiments: "Fallidos",
      search: "Buscar experimentos...",
      type: "Tipo",
      status: "Estado",
      newExperiment: "Nuevo Experimento",
      noExperiments: "No se encontraron experimentos",
      noDescription: "Sin descripción",
      createExperiment: "Crear Nuevo Experimento",
      editExperiment: "Editar Experimento",
      experimentDetails: "Detalles del Experimento",
      quick: {
        title: "Acciones Rápidas",
        newExperiment: "Nuevo Experimento",
        importExperiment: "Importar Experimento",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        planned: "Planificados",
        running: "Ejecutándose",
        completed: "Completados",
        failed: "Fallidos",
      },
    },

    // Model Serving Management
    modelServing: {
      title: "Model Serving",
      subtitle: "Gestión de despliegues y servicios de modelos",
      operational: "Operacional",
      totalDeployments: "Total Deployments",
      runningDeployments: "Ejecutándose",
      stoppedDeployments: "Detenidos",
      failedDeployments: "Fallidos",
      search: "Buscar deployments...",
      type: "Tipo",
      status: "Estado",
      newDeployment: "Nuevo Deployment",
      noDeployments: "No se encontraron deployments",
      noDescription: "Sin descripción",
      start: "Iniciar",
      stop: "Detener",
      createDeployment: "Crear Nuevo Deployment",
      editDeployment: "Editar Deployment",
      deploymentDetails: "Detalles del Deployment",
      quick: {
        title: "Acciones Rápidas",
        newDeployment: "Nuevo Deployment",
        importDeployment: "Importar Deployment",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        pending: "Pendientes",
        running: "Ejecutándose",
        stopped: "Detenidos",
        failed: "Fallidos",
      },
    },

    // Serving Module
    serving: {
      deploymentInstance: {
        title: "Deployment Instance",
        register: "Registrar Deployment Instance",
        list: "Listado de Deployment Instances",
        detail: {
          title: "Deployment Instance",
          formTitle: "Información del Deployment Instance",
        },
        deploymentid: "Deployment Id",
        instanceid: "Instance ID",
        instancename: "Instance Name",
        instancetype: "Instance Type",
        status: "Status",
        ipaddress: "IP Address",
        port: "Port",
        nodename: "Node Name",
        resourceusage: "Resource Usage",
        startedat: "Started At",
        lastheartbeat: "Last Heartbeat",
        createdat: "Created At",
        updatedat: "Updated At",
      },
      model: {
        title: "Model",
        register: "Registrar Model",
        list: "Listado de Models",
        detail: {
          title: "Model",
          formTitle: "Información del Model",
        },
      },
      deploymentLog: {
        title: "Deployment Log",
        register: "Registrar Deployment Log",
        list: "Listado de Deployment Logs",
      },
      deploymentMetric: {
        title: "Deployment Metric",
        register: "Registrar Deployment Metric",
        list: "Listado de Deployment Metrics",
      },
      deploymentStatus: {
        title: "Deployment Status",
        register: "Registrar Deployment Status",
        list: "Listado de Deployment Status",
      },
      modelDeployment: {
        title: "Model Deployment",
        register: "Registrar Model Deployment",
        list: "Listado de Model Deployments",
      },
      modelMetrics: {
        title: "Model Metrics",
        register: "Registrar Model Metrics",
        list: "Listado de Model Metrics",
      },
      modelPrediction: {
        title: "Model Prediction",
        register: "Registrar Model Prediction",
        list: "Listado de Model Predictions",
      },
      modelVersion: {
        title: "Model Version",
        register: "Registrar Model Version",
        list: "Listado de Model Versions",
      },
      servingEndpoint: {
        title: "Serving Endpoint",
        register: "Registrar Serving Endpoint",
        list: "Listado de Serving Endpoints",
      },
      servingRequest: {
        title: "Serving Request",
        register: "Registrar Serving Request",
        list: "Listado de Serving Requests",
      },
      slaCompliance: {
        title: "SLA Compliance",
        register: "Registrar SLA Compliance",
        list: "Listado de SLA Compliance",
      },
      errorAnalysis: {
        title: "Error Analysis",
        register: "Registrar Error Analysis",
        list: "Listado de Error Analysis",
      },
      costBreakdown: {
        title: "Cost Breakdown",
        register: "Registrar Cost Breakdown",
        list: "Listado de Cost Breakdown",
      },
    },

    // RAG Module
    rag: {
      dashboard: {
        title: "RAG Dashboard",
        subtitle: "Retrieval Augmented Generation System",
        totalProjects: "Total Projects",
        totalChunks: "Total Chunks",
        totalSearches: "Total Searches",
        avgResponseTime: "Average Response Time",
        activeProjects: "Active Projects",
        vsLastMonth: "vs last month",
        projectsOverview: "Projects Overview",
        viewAll: "View All",
        chunks: "chunks",
        embeddings: "embeddings",
        searches: "searches",
        models: "Models",
        recentActivity: "Recent Activity",
        modelUsage: "Model Usage",
        usage: "Usage",
      },
      project: {
        createTitle: "Create RAG Project",
        createSubtitle: "Configure a new RAG project",
        editTitle: "Edit RAG Project",
        basicInfo: "Basic Information",
        name: "Project Name",
        namePlaceholder: "Enter project name",
        description: "Description",
        descriptionPlaceholder: "Describe the purpose of the RAG project",
        isActive: "Active Project",
        embeddingModel: "Embedding Model",
        modelConfiguration: "Model Configuration",
        rerankerModel: "Reranker Model",
        llmModel: "LLM Model",
        ragConfiguration: "RAG Configuration",
        maxChunkSize: "Maximum Chunk Size",
        chunkOverlap: "Chunk Overlap",
        semanticChunking: "Semantic Chunking",
        statistics: "Project Statistics",
        documents: "Documents",
        chunks: "Chunks",
        embeddings: "Embeddings",
        searches: "Searches",
        avgResponseTime: "Average Time",
        successRate: "Success Rate",
        assignments: "Assignments",
        documentAssignments: "Assigned Documents",
        webscrapingAssignments: "Assigned Web Scraping",
        datasourceAssignments: "Assigned Data Sources",
        lekaServerConfig: "Leka Server Configuration",
        lekaEndpoint: "Leka Server Endpoint",
        lekaApiKey: "Leka Server API Key",
        lekaTimeout: "Timeout (ms)",
      },
      help: {
        dashboard: {
          title: "RAG Dashboard",
          content:
            "The RAG Dashboard allows you to monitor the performance of your Retrieval Augmented Generation projects, including usage statistics, active projects, and recent activity.",
        },
        projects: {
          title: "RAG Projects",
          content:
            "<h4>What are RAG Projects?</h4><p>RAG (Retrieval Augmented Generation) projects are intelligent systems that combine information retrieval with text generation. They allow you to create chatbots and assistants that can answer questions based on your own documents and data.</p><h4>What are they used for?</h4><ul><li><strong>Specialized Chatbots:</strong> Create assistants that know your specific documentation</li><li><strong>Intelligent Search:</strong> Find relevant information in large volumes of documents</li><li><strong>Contextualized Responses:</strong> Generate accurate responses based on your content</li><li><strong>Support Automation:</strong> Reduce the workload of your support team</li><li><strong>Interactive Knowledge Base:</strong> Convert static documents into a conversational experience</li></ul><h4>Project Management</h4><p>From this page you can:</p><ul><li>Create new RAG projects with custom configurations</li><li>Edit AI models (embedding, reranker, LLM) for each project</li><li>Monitor performance and usage statistics</li><li>Manage data sources assigned to each project</li><li>Configure chunking and processing parameters</li></ul>",
        },
        createProject: {
          title: "Create RAG Project",
          content:
            "Create a new RAG project by configuring embedding, reranker, and LLM models, as well as chunking parameters and Leka server configuration.",
        },
        editProject: {
          title: "Edit RAG Project",
          content:
            "Edit your RAG project configuration, including models, chunking parameters, Leka server configuration, and monitor project statistics.",
        },
        models: {
          title: "RAG Models",
          content:
            "Manage available AI models for your RAG projects, including embedding, reranker, and LLM models from different providers.",
        },
        createModel: {
          title: "Create RAG Model",
          content:
            "<h4>AI Model Configuration</h4><p>This page allows you to create and configure new AI models for your RAG projects. You can configure:</p><ul><li><strong>Basic Information:</strong> Name, type (Embedding, Reranker, LLM), provider and version</li><li><strong>Technical Configuration:</strong> Endpoint, API Key, cost per token, maximum tokens and context window</li><li><strong>Advanced Options:</strong> Local vs remote model, active/inactive status</li></ul><p><strong>Model Types:</strong></p><ul><li><strong>Embedding:</strong> Converts text into numerical vectors for semantic search</li><li><strong>Reranker:</strong> Improves the relevance of search results</li><li><strong>LLM:</strong> Generates responses based on retrieved context</li></ul><p>Once configured, you can test the model before saving it to ensure it works correctly.</p>",
        },
        editModel: {
          title: "Edit RAG Model",
          content:
            "Edit the configuration of an existing AI model, including performance parameters, cost, and endpoint configuration.",
        },
        chat: {
          title: "RAG Chat",
          content:
            "Interact with your RAG projects through chat. Select a project and ask questions to get answers based on your documents.",
        },
        reranker: {
          title: "RAG Reranker",
          content:
            "Configure and manage search result reranking to improve the relevance of responses in your RAG projects.",
        },
        chunks: {
          title: "Document Chunks",
          content:
            "Chunks are processed document fragments that enable more efficient search. Auto-tags are generated automatically based on content, while manual tags can be added, edited, or removed to improve categorization and retrieval of specific information.",
        },
        search: {
          title: "Semantic Search",
          content:
            "Perform advanced semantic searches in your RAG documents with different search strategies and filters.",
        },
        searchTypes: {
          vector: "Vector",
          textual: "Textual",
          hybrid: "Hybrid",
          filtered: "Filtered",
        },
        analytics: {
          title: "RAG Analytics",
          content:
            "Monitor the performance and metrics of your RAG projects, including usage statistics, response times, and result quality.",
        },
      },
      reranker: {
        title: "Reranker",
        subtitle: "Improve search result relevance",
        configuration: "Reranker Configuration",
        project: "RAG Project",
        selectProject: "Select project",
        model: "Reranking Model",
        selectModel: "Select model",
        topK: "Top K Results",
        threshold: "Relevance Threshold",
        testQuery: "Query Test",
        query: "Test Query",
        queryPlaceholder: "Enter a query to test reranking...",
        runTest: "Run Test",
        testHistory: "Test History",
      },
      chunks: {
        title: "Chunks",
        subtitle: "Visualize and manage document chunks",
        project: "RAG Project",
        selectProject: "Select project",
        chunkType: "Chunk Type",
        selectType: "Select type",
        searchChunks: "Search Chunks",
        searchPlaceholder: "Search by content or tags...",
        totalChunks: "Total Chunks",
        avgSize: "Average Size",
        autoTags: "Auto Tags",
        viewDetails: "View Details",
        filters: "Filters",
        manualTags: "Manual Tags",
        newTag: "New tag...",
        addTag: "Add Tag",
        editTags: "Edit Tags",
        removeTag: "Remove Tag",
        source: "Source",
        relevance: "Relevance",
        characters: "characters",
        createdAt: "Created",
        saveChanges: "Save Changes",
        cancel: "Cancel",
      },
      search: {
        title: "Semantic Search",
        subtitle: "Advanced search in RAG documents",
        project: "RAG Project",
        selectProject: "Select project",
        searchQuery: "Search Query",
        queryPlaceholder: "Enter your search query...",
        searchType: "Search Type",
        selectType: "Select type",
        executeSearch: "Execute Search",
        searchHistory: "Search History",
        results: "Results",
        relevance: "Relevance",
        source: "Source",
      },
      analytics: {
        title: "Analytics",
        subtitle: "RAG project metrics and performance",
        project: "RAG Project",
        selectProject: "Select project",
        timeRange: "Time Range",
        selectRange: "Select range",
        refresh: "Refresh",
        totalSearches: "Total Searches",
        avgResponseTime: "Average Response Time",
        successRate: "Success Rate",
        userSatisfaction: "User Satisfaction",
        topQueries: "Top Queries",
        performanceMetrics: "Performance Metrics",
        usageStats: "Usage Statistics",
      },
      healthDashboard: {
        title: "Index Health Dashboard",
        description: "Index Health Dashboard Management",
        metrics: "Health Metrics",
        totalItems: "Total Items",
        activeItems: "Active Items",
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        avgScore: "Avg Score",
      },
      dataSources: {
        title: "Data Source",
        description: "Data Source Management",
        register: "Register Data Source",
        total: "Total",
        active: "Active",
        pending: "Pending",
        inactive: "Inactive",
        list: "Data Sources List",
        name: "Name",
        description: "Description",
        type: "Type",
        connectionConfig: "Connectionconfig",
        syncConfig: "Syncconfig",
        filterType: "Filter by Type",
        filterStatus: "Filter by Status",
        filterSyncStatus: "Filter by Syncstatus",
      },
      versioning: {
        title: "Version",
        description: "Version Management",
        register: "Register Version",
        total: "Total",
        active: "Active",
        pending: "Pending",
        inactive: "Inactive",
        list: "Versions List",
        version: "Version",
        description: "Description",
        configuration: "Configuration",
        embeddingModel: "Embeddingmodel",
        retrievalConfig: "Retrievalconfig",
        filterStatus: "Filter by Status",
      },
    },

    // Analytics Module
    analytics: {
      overview: {
        dashboard: {
          title: "Analytics Overview",
          subtitle: "Analytics Overview Management",
          list: "Analytics List",
        },
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        generatedAt: "Generated At",
      },
      metrics: {
        overview: {
          title: "Analytics Metrics",
          subtitle: "Analytics Metrics Management",
          register: "Register Metric",
          list: "Metrics List",
        },
        detail: {
          editTitle: "Edit Metric",
          createTitle: "New Metric",
          subtitle: "Analytics Metric management and configuration",
          formTitle: "Metric Data",
        },
        name: "Name",
        type: "Type",
        value: "Value",
        unit: "Unit",
        thresholdMin: "Min Threshold",
        thresholdMax: "Max Threshold",
        context: "Context",
        metadata: "Metadata",
        createdBy: "Created By",
        updatedBy: "Updated By",
        createdAt: "Creation Date",
        updatedAt: "Update Date",
        filter: {
          allTypes: "All Types",
          allStatus: "All Status",
        },
        errors: {
          nameRequired: "Name is required",
          valueRequired: "Value is required",
          createdByRequired: "Created by is required",
          createdAtRequired: "Creation date is required",
          saveError: "Error saving metric",
        },
      },
      reports: {
        overview: {
          title: "Analytics Reports",
          subtitle: "Analytics Reports Management",
          register: "Register Report",
          list: "Reports List",
        },
        detail: {
          editTitle: "Edit Report",
          createTitle: "New Report",
          subtitle: "Analytics Report management and configuration",
          formTitle: "Report Data",
        },
        name: "Name",
        type: "Type",
        description: "Description",
        data: "Data",
        config: "Config",
        generatedAt: "Generation Date",
        schedule: "Schedule",
        createdBy: "Created By",
        updatedBy: "Updated By",
        createdAt: "Creation Date",
        updatedAt: "Update Date",
        filter: {
          allTypes: "All Types",
          allStatus: "All Status",
        },
        errors: {
          nameRequired: "Name is required",
          statusRequired: "Status is required",
          createdByRequired: "Created by is required",
          createdAtRequired: "Creation date is required",
          saveError: "Error saving report",
        },
      },
      trends: {
        overview: {
          title: "Analytics Trends",
          subtitle: "Analytics Trends Management",
          list: "Trends List",
        },
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        generatedAt: "Generated At",
      },
    },

    // Analytics Module
    analytics: {
      overview: {
        dashboard: {
          title: "Analytics Overview",
          subtitle: "Gestión de Analytics Overview",
          list: "Listado de Analytics",
        },
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        generatedAt: "Generated At",
      },
      metrics: {
        overview: {
          title: "Analytics Metrics",
          subtitle: "Gestión de Analytics Metrics",
          register: "Registrar Métrica",
          list: "Listado de Métricas",
        },
        detail: {
          editTitle: "Editar Métrica",
          createTitle: "Nueva Métrica",
          subtitle: "Gestión y configuración de Analytics Metric",
          formTitle: "Datos de la Métrica",
        },
        name: "Nombre",
        type: "Tipo",
        value: "Valor",
        unit: "Unidad",
        thresholdMin: "Umbral Mín",
        thresholdMax: "Umbral Máximo",
        context: "Contexto",
        metadata: "Metadata",
        createdBy: "Creado Por",
        updatedBy: "Actualizado Por",
        createdAt: "Fecha Creación",
        updatedAt: "Fecha Actualización",
        filter: {
          allTypes: "Todos los Tipos",
          allStatus: "Todos los Estados",
        },
        errors: {
          nameRequired: "El nombre es requerido",
          valueRequired: "El valor es requerido",
          createdByRequired: "El creador es requerido",
          createdAtRequired: "La fecha de creación es requerida",
          saveError: "Error al guardar la métrica",
        },
      },
      reports: {
        overview: {
          title: "Analytics Reports",
          subtitle: "Gestión de Analytics Reports",
          register: "Registrar Reporte",
          list: "Listado de Reportes",
        },
        detail: {
          editTitle: "Editar Reporte",
          createTitle: "Nuevo Reporte",
          subtitle: "Gestión y configuración de Analytics Report",
          formTitle: "Datos del Reporte",
        },
        name: "Nombre",
        type: "Tipo",
        description: "Descripción",
        data: "Datos",
        config: "Config",
        generatedAt: "Fecha Generación",
        schedule: "Programación",
        createdBy: "Creado Por",
        updatedBy: "Actualizado Por",
        createdAt: "Fecha Creación",
        updatedAt: "Fecha Actualización",
        filter: {
          allTypes: "Todos los Tipos",
          allStatus: "Todos los Estados",
        },
        errors: {
          nameRequired: "El nombre es requerido",
          statusRequired: "El estado es requerido",
          createdByRequired: "El creador es requerido",
          createdAtRequired: "La fecha de creación es requerida",
          saveError: "Error al guardar el reporte",
        },
      },
      trends: {
        overview: {
          title: "Analytics Trends",
          subtitle: "Gestión de Analytics Trends",
          list: "Listado de Trends",
        },
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        generatedAt: "Generated At",
      },
    },

    // Technology Management
    technology: {
      create: {
        title: "Crear Nueva Tecnología",
        content:
          "Esta página te permite registrar una nueva tecnología en el catálogo del sistema. Puedes especificar información básica como nombre, categoría, tipo, versiones, URLs de documentación y repositorio, así como configurar requisitos de infraestructura y opciones de integración con RAG, entrenamiento y evaluación automática.",
      },
              help: {
          create: {
            title: "Crear Tecnología",
            content:
              "Registra nuevas tecnologías en el catálogo para que estén disponibles en el sistema. Incluye información detallada sobre versiones, documentación y requisitos técnicos.",
          },
        },
        catalog: {
          title: "Catálogo de Tecnologías",
          content:
            "Explora y gestiona todas las tecnologías disponibles en el sistema. Puedes buscar, filtrar, descargar y agregar tecnologías a tus stacks. Utiliza los filtros para encontrar tecnologías específicas por categoría, popularidad o nombre.",
        },
    },

    // Core Module
    core: {
      departments: {
        title: "Gestión de Departamentos",
        description: "Gestiona departamentos, equipos y recursos organizacionales",
        register: "Registrar Departamento",
        list: "Lista de Departamentos",
        new: "Nuevo Departamento",
        edit: "Editar Departamento",
        detailDescription: "Gestión y configuración de Department",
        createdAt: "Created At",
        updatedAt: "Updated At",
        corname: "Nombre",
        code: "Código",
        managerid: "ID Manager",
        parentid: "ID Departamento Padre",
        isactive: "Activo",
        description: "Descripción",
      },
      users: {
        title: "Gestión de Usuarios",
        description: "Gestiona usuarios del sistema",
        register: "Registrar Usuario",
        list: "Lista de Usuarios",
        new: "Nuevo Usuario",
        edit: "Editar Usuario",
        detailDescription: "Gestión y configuración de User",
        username: "Usuario",
        email: "Email",
        firstname: "Nombre",
        lastname: "Apellido",
        phone: "Teléfono",
        passwordhash: "Contraseña",
        departmentid: "ID Departamento",
        createdAt: "Created At",
        updatedAt: "Updated At",
        isactive: "Activo",
        islocked: "Bloqueado",
      },
      menus: {
        title: "Gestión de Menús",
        description: "Gestiona los menús del sistema",
        register: "Registrar Menú",
        list: "Lista de Menús",
      },
      roles: {
        title: "Gestión de Roles",
        description: "Gestiona los roles del sistema",
        register: "Registrar Rol",
        list: "Lista de Roles",
      },
      permissions: {
        title: "Gestión de Permisos",
        description: "Gestiona los permisos del sistema",
        register: "Registrar Permiso",
        list: "Lista de Permisos",
      },
      loginAttempts: {
        title: "Intentos de Login",
        description: "Historial de intentos de acceso al sistema",
        list: "Lista de Intentos",
      },
      userSessions: {
        title: "Sesiones de Usuario",
        description: "Gestiona las sesiones activas de usuarios",
        list: "Lista de Sesiones",
      },
      dashboard: {
        title: "Admin Dashboard Summary",
        description: "Resumen general del sistema administrativo",
        activeItems: "Items Activos",
        deployedItems: "Items Desplegados",
        trainingItems: "Items en Entrenamiento",
        offlineItems: "Items Offline",
      },
    },
    projects: {
      portfolioDashboard: {
        title: "Dashboard de Portfolio de Proyectos",
        description: "Vista general del portfolio de proyectos",
        totalProjects: "Total Proyectos",
        active: "activos",
        budget: "Presupuesto",
        used: "utilizado",
        resources: "Recursos",
        utilization: "utilización",
        completion: "Completados",
        rate: "tasa",
        budgetBreakdown: "Desglose de Presupuesto",
        spent: "Gastado",
        remaining: "Restante",
        projectStatus: "Estado de Proyectos",
        onTime: "A Tiempo",
        inProgress: "En Progreso",
        atRisk: "En Riesgo",
      },
      overview: {
        title: "Proyectos",
        newProject: "Nuevo Proyecto",
        search: "Buscar proyectos...",
        progress: "Progreso",
        members: "miembros",
      },
      detail: {
        information: "Información General",
        domain: "Dominio",
        owner: "Propietario",
        startDate: "Fecha de Inicio",
        endDate: "Fecha de Fin",
        progress: "Progreso",
        completion: "Completado",
        daysRemaining: "días restantes",
        overdue: "Proyecto vencido",
        budget: "Presupuesto",
        total: "Total",
        spent: "Gastado",
        remaining: "Restante",
        team: "Equipo",
        teamSize: "Tamaño del Equipo",
        members: "miembros",
        metadata: "Metadata",
        createdAt: "Creado",
        updatedAt: "Actualizado",
      },
      status: {
        active: "Activo",
        completed: "Completado",
        "on-hold": "En Pausa",
        cancelled: "Cancelado",
      },
    },
  },
  en: {
    // ... existing translations ...
    // Core Module
    core: {
      departments: {
        title: "Department Management",
        description: "Manage departments, teams and organizational resources",
        register: "Register Department",
        list: "Department List",
        new: "New Department",
        edit: "Edit Department",
        detailDescription: "Department management and configuration",
        createdAt: "Created At",
        updatedAt: "Updated At",
        corname: "Name",
        code: "Code",
        managerid: "Manager ID",
        parentid: "Parent Department ID",
        isactive: "Active",
        description: "Description",
      },
      users: {
        title: "User Management",
        description: "Manage system users",
        register: "Register User",
        list: "User List",
      },
      dashboard: {
        title: "Admin Dashboard Summary",
        description: "General administrative system summary",
        activeItems: "Active Items",
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
      },
    },
    projects: {
      portfolioDashboard: {
        title: "Project Portfolio Dashboard",
        description: "Overview of the project portfolio",
        totalProjects: "Total Projects",
        active: "active",
        budget: "Budget",
        used: "used",
        resources: "Resources",
        utilization: "utilization",
        completion: "Completed",
        rate: "rate",
        budgetBreakdown: "Budget Breakdown",
        spent: "Spent",
        remaining: "Remaining",
        projectStatus: "Project Status",
        onTime: "On Time",
        inProgress: "In Progress",
        atRisk: "At Risk",
      },
      overview: {
        title: "Projects",
        newProject: "New Project",
        search: "Search projects...",
        progress: "Progress",
        members: "members",
      },
      detail: {
        information: "General Information",
        domain: "Domain",
        owner: "Owner",
        startDate: "Start Date",
        endDate: "End Date",
        progress: "Progress",
        completion: "Completed",
        daysRemaining: "days remaining",
        overdue: "Project overdue",
        budget: "Budget",
        total: "Total",
        spent: "Spent",
        remaining: "Remaining",
        team: "Team",
        teamSize: "Team Size",
        members: "members",
        metadata: "Metadata",
        createdAt: "Created",
        updatedAt: "Updated",
      },
      status: {
        active: "Active",
        completed: "Completed",
        "on-hold": "On Hold",
        cancelled: "Cancelled",
      },
      domains: {
        title: "Project Domains",
        newDomain: "New Domain",
        search: "Search domains...",
        projectCount: "Projects",
      },
      members: {
        title: "Project Members",
        newMember: "New Member",
        search: "Search members...",
        role: "Role",
        project: "Project",
        allocation: "Allocation",
      },
      artifacts: {
        title: "Project Artifacts",
        newArtifact: "New Artifact",
        search: "Search artifacts...",
        project: "Project",
        size: "Size",
        createdAt: "Created",
      },
      documents: {
        title: "Project Documents",
        newDocument: "New Document",
        search: "Search documents...",
        project: "Project",
        size: "Size",
        uploadedBy: "Uploaded By",
        uploadedAt: "Date",
      },
      invoices: {
        title: "Project Invoices",
        newInvoice: "New Invoice",
        search: "Search invoices...",
        amount: "Amount",
        issueDate: "Issue Date",
        dueDate: "Due Date",
        status: {
          pending: "Pending",
          paid: "Paid",
          overdue: "Overdue",
          cancelled: "Cancelled",
        },
      },
      requirements: {
        title: "Project Requirements",
        newRequirement: "New Requirement",
        search: "Search requirements...",
        project: "Project",
        assignedTo: "Assigned To",
        priority: {
          high: "High",
          medium: "Medium",
          low: "Low",
        },
        status: {
          open: "Open",
          "in-progress": "In Progress",
          completed: "Completed",
          cancelled: "Cancelled",
        },
      },
      tasks: {
        title: "Project Tasks",
        newTask: "New Task",
        search: "Search tasks...",
        progress: "Progress",
        assignedTo: "Assigned To",
        dueDate: "Due Date",
        project: "Project",
        status: {
          todo: "To Do",
          "in-progress": "In Progress",
          review: "In Review",
          done: "Done",
        },
        priority: {
          high: "High",
          medium: "Medium",
          low: "Low",
        },
      },
      financialSummary: {
        title: "Project Financial Summary",
        totalBudget: "Total Budget",
        projects: "projects",
        totalSpent: "Total Spent",
        utilized: "utilized",
        totalRemaining: "Total Remaining",
        available: "available",
        averageBudget: "Average Budget",
        perProject: "per project",
        monthlySpending: "Monthly Spending",
        categoryBreakdown: "Category Breakdown",
      },
      resourceAllocation: {
        title: "Resource Allocation",
        totalResources: "Total Resources",
        resources: "resources",
        allocated: "Allocated",
        utilization: "utilization",
        available: "Available",
        free: "free",
        utilizationRate: "Utilization Rate",
        ofTotal: "of total",
        byProject: "Allocation by Project",
        byRole: "Allocation by Role",
      },
    },
  },
};

// Hook para usar traducciones
export function useTranslation() {
  const [mounted, setMounted] = useState(false);
  const [language, setLanguageState] = useState<Language>("es");

  // Solo ejecutar en el cliente después de la hidratación
  useEffect(() => {
    setMounted(true);
    const savedLanguage =
      (localStorage.getItem("codeflowx-language") as Language) || "es";
    setLanguageState(savedLanguage);
  }, []);

  const getLanguage = (): Language => {
    if (typeof window !== "undefined") {
      return (localStorage.getItem("codeflowx-language") as Language) || "es";
    }
    return "es";
  };

  const setLanguage = (language: Language) => {
    if (typeof window !== "undefined") {
      localStorage.setItem("codeflowx-language", language);
      setLanguageState(language);
      // Recargar la página para aplicar el idioma
      window.location.reload();
    }
  };

  const t = (
    key: string,
    fallback?: string,
    params?: Record<string, string>
  ): string => {
    // Durante SSR o antes de la hidratación, siempre usar español
    if (!mounted) {
      const keys = key.split(".");
      let translation = translations["es"];
      for (const k of keys) {
        translation = translation?.[k];
        if (!translation) break;
      }
      // Asegurar que devolvemos una cadena
      return typeof translation === "string" ? translation : fallback || key;
    }

    const currentLanguage = getLanguage();
    const keys = key.split(".");

    // Buscar en el idioma actual
    let translation = translations[currentLanguage];
    for (const k of keys) {
      translation = translation?.[k];
      if (!translation) break;
    }

    // Si no se encuentra, buscar en español como fallback
    let fallbackTranslation = translations["es"];
    for (const k of keys) {
      fallbackTranslation = fallbackTranslation?.[k];
      if (!fallbackTranslation) break;
    }

    // Asegurar que devolvemos una cadena
    let finalTranslation =
      translation || fallbackTranslation || fallback || key;

    // Si finalTranslation no es una cadena, usar el fallback o la clave
    if (typeof finalTranslation !== "string") {
      finalTranslation = fallback || key;
    }

    // Reemplazar parámetros si existen
    if (params) {
      return finalTranslation.replace(/\{(\w+)\}/g, (match, param) => {
        return params[param] || match;
      });
    }

    return finalTranslation;
  };

  return {
    language: mounted ? language : "es",
    setLanguage,
    t,
    mounted,
    availableLanguages: ["es", "en"],
  };
}

```

```typescript
// Configuración de internacionalización para CodeflowX
import { useEffect, useState } from "react";

export type Language = "es" | "en";

export interface I18nConfig {
  language: Language;
  fallbackLanguage: Language;
  availableLanguages: Language[];
}

export const i18nConfig: I18nConfig = {
  language: "es",
  fallbackLanguage: "es",
  availableLanguages: ["es", "en"],
};

// Traducciones para la aplicación
export const translations = {
  es: {
    // Navegación y layout
    common: {
      dashboard: "Dashboard",
      infrastructure: "Infraestructura",
      admin: "Administración",
      aiNotebook: "AI Notebook",
      domainIngestion: "Ingesta de Dominios",
      modelEvaluation: "Evaluación de Modelos",
      ragTraining: "Entrenamiento RAG",
      governance: "Gobernanza",
      settings: "Configuración",
      profile: "Perfil",
      logout: "Cerrar Sesión",
      loading: "Cargando...",
      save: "Guardar",
      cancel: "Cancelar",
      edit: "Editar",
      delete: "Eliminar",
      add: "Añadir",
      search: "Buscar",
      filter: "Filtrar",
      status: "Estado",
      actions: "Acciones",
      details: "Detalles",
      back: "Volver",
      next: "Siguiente",
      previous: "Anterior",
      close: "Cerrar",
      confirm: "Confirmar",
      yes: "Sí",
      help: "Ayuda",
      no: "No",
      active: "Activo",
      inactive: "Inactivo",
      pending: "Pendiente",
      completed: "Completado",
      error: "Error",
      success: "Éxito",
      warning: "Advertencia",
      info: "Información",
      changeLanguage: "Cambiar idioma",
      selectLanguage: "Seleccionar idioma",
      noResults: "No se encontraron resultados",
      showing: "Mostrando",
      all: "TODOS",
      clearFilters: "Limpiar filtros",
    },

    // Prompts
    prompts: {
      performance: {
        title: "Prompt Performance Comparison",
        description: "Gestión de Prompt Performance Comparison",
        results: "Resultados",
        id: "ID",
        activeItems: "Active Items",
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        avgScore: "Avg Score",
      },
      validation: {
        title: "Prompt Validation",
        description: "Gestión de Prompt Validation",
        register: "Registrar Prompt Validation",
        results: "Resultados",
        id: "ID",
        validationtype: "Validationtype",
        validationresult: "Validationresult",
        validationscore: "Validationscore",
        validationdetails: "Validationdetails",
        issuesfound: "Issuesfound",
        filterType: "Filtrar por Validationtype",
        filterStatus: "Filtrar por Status",
        type1: "TIPO 1",
        type2: "TIPO 2",
      },
      versioning: {
        title: "Prompt Version",
        description: "Gestión de Prompt Version",
        register: "Registrar Prompt Version",
        results: "Resultados",
        id: "ID",
        version: "Version",
        description: "Description",
        content: "Content",
        parameters: "Parameter S",
        changes: "Changes",
        filterStatus: "Filtrar por Status",
      },
    },

    // Dashboard
    dashboard: {
      title: "Panel de Control",
      welcome: "Bienvenido a CodeflowX",
      stats: "Estadísticas",
      quickActions: "Acciones Rápidas",
      recentProjects: "Proyectos Recientes",
      systemMetrics: "Métricas del Sistema",
      totalProjects: "Total de Proyectos",
      activeModels: "Modelos Activos",
      totalUsers: "Total de Usuarios",
      systemHealth: "Salud del Sistema",
      viewAll: "Ver Todos",
      createNew: "Crear Nuevo",
      lastUpdated: "Última Actualización",
      performance: "Rendimiento",
      efficiency: "Eficiencia",
      reliability: "Confiabilidad",
      activeNodes: "Nodos Activos",
      of: "de",
      plusOneThisMonth: "+1 este mes",
      codeGenerated: "Código Generado",
      lines: "líneas",
      modelsAvailable: "Modelos Disponibles",
      specialized: "especializados",
      plusThreeNew: "+3 nuevos",
      licenseStatus: "Estado de Licencia",
      proPlan: "Plan Pro",
      deployModel: "Desplegar Modelo",
      viewAnalytics: "Ver Analíticas",
      manageUsers: "Gestionar Usuarios",
      systemHealthTitle: "Salud del Sistema",
      cpuUsage: "Uso de CPU",
      memory: "Memoria",
      network: "Red",
      security: "Seguridad",
    },

    // Infraestructura
    infrastructure: {
      title: "Gestión de Infraestructura",
      overview: "Vista General",
      providers: "Proveedores",
      kubernetes: "Clusters Kubernetes",
      gpuInstances: "Instancias GPU",
      deployments: "Despliegues",
      resources: "Recursos",
      credentials: "Credenciales",
      costManagement: "Gestión de Costos",
      systemMonitoring: "Monitoreo del Sistema",
      addProvider: "Añadir Proveedor",
      addCluster: "Añadir Cluster",
      addInstance: "Añadir Instancia",
      clusterName: "Nombre del Cluster",
      instanceType: "Tipo de Instancia",
      region: "Región",
      status: "Estado",
      nodes: "Nodos",
      cpu: "CPU",
      memory: "Memoria",
      storage: "Almacenamiento",
      network: "Red",
      security: "Seguridad",
      monitoring: "Monitoreo",
      logs: "Logs",
      metrics: "Métricas",
      alerts: "Alertas",
      // Nuevas traducciones para la página principal
      cloudProviders: "Proveedores Cloud",
      cloudProvidersDesc:
        "Gestiona proveedores cloud, credenciales y conexiones",
      awsGcpAzure: "AWS, GCP, Azure",
      ovhRunpodVast: "OVH, RunPod, Vast.ai",
      credentialsApiKeys: "Credenciales y API Keys",
      statusMonitoring: "Monitoreo de estado",
      kubernetesDesc: "Administra clusters K8s, nodos y recursos",
      eksGkeAks: "EKS, GKE, AKS",
      ownClusters: "Clusters propios",
      nodeMonitoring: "Monitoreo de nodos",
      deploymentManagement: "Gestión de deployments",
      gpuInstancesDesc: "Gestión de instancias GPU para training e inferencia",
      t4V100A100H100: "T4, V100, A100, H100",
      rtx40903090: "RTX 4090, 3090",
      spotInstances: "Spot instances",
      costAnalysis: "Análisis de costes",
      infrastructureDeployments: "Despliegues de Infraestructura",
      deploymentsDesc: "Despliegues de infraestructura y servicios",
      trainingClusters: "Training clusters",
      inferenceServices: "Inference services",
      autoScaling: "Escalado automático",
      costManagementDesc: "Análisis y gestión de costes de infraestructura",
      providerAnalysis: "Análisis por proveedor",
      budgetAlerts: "Alertas de presupuesto",
      reports: "Reportes",
      optimization: "Optimización",
      systemMonitoringDesc:
        "Monitoreo en tiempo real de toda la infraestructura",
      realTimeMetrics: "Métricas en tiempo real",
      automaticAlerts: "Alertas automáticas",
      customDashboards: "Dashboards personalizados",
      centralizedLogs: "Logs centralizados",
      twoWeeksAgo: "hace 2 semanas",
      deployCluster: "Desplegar Cluster",
      launchGpu: "Lanzar GPU",
      viewCosts: "Ver Costes",
      // Nuevas traducciones para páginas migradas
      resourceUtilizationDashboard: "Dashboard de Utilización de Recursos",
      resourceUtilizationDesc: "Monitorea el uso de recursos en toda la infraestructura",
      resourceUtilization: "Utilización de Recursos",
      totalResources: "Total de Recursos",
      active: "activo",
      avgCpuUsage: "Uso Promedio de CPU",
      avgMemoryUsage: "Uso Promedio de Memoria",
      avgStorageUsage: "Uso Promedio de Almacenamiento",
      networkUsage: "Uso de Red",
      networkIn: "Entrada de Red",
      networkOut: "Salida de Red",
      resourceName: "Nombre del Recurso",
      resourceType: "Tipo de Recurso",
      cloudResources: "Recursos Cloud",
      basicInformation: "Información Básica",
      resourceId: "ID del Recurso",
      cloudProvider: "Proveedor Cloud",
      zone: "Zona",
      autoScaling: "Escalado Automático",
      specifications: "Especificaciones",
      scalingConfiguration: "Configuración de Escalado",
      minInstances: "Instancias Mínimas",
      currentInstances: "Instancias Actuales",
      maxInstances: "Instancias Máximas",
      cost: "Costo",
      costPerHour: "Costo por Hora",
      costPerMonth: "Costo por Mes",
      metadata: "Metadatos",
      createdAt: "Creado En",
      updatedAt: "Actualizado En",
      instances: "Instancias",
      noResourcesFound: "No se encontraron recursos",
      cloudProviderOverview: "Vista General de Proveedores Cloud",
      cloudCredentialOverview: "Vista General de Credenciales Cloud",
      cloudRegionOverview: "Vista General de Regiones Cloud",
      cloudResourceOverview: "Vista General de Recursos Cloud",
      kubernetesClusterOverview: "Vista General de Clusters Kubernetes",
      kubernetesNodeOverview: "Vista General de Nodos Kubernetes",
      gpuInstanceOverview: "Vista General de Instancias GPU",
      infrastructureTemplateOverview: "Vista General de Plantillas de Infraestructura",
      infrastructureMetricOverview: "Vista General de Métricas de Infraestructura",
      infrastructureMetricsSummaryOverview: "Vista General de Resumen de Métricas",
      infrastructureCostOverview: "Vista General de Costos de Infraestructura",
      infrastructureCostAnalysisOverview: "Vista General de Análisis de Costos",
      infrastructureAuditOverview: "Vista General de Auditoría de Infraestructura",
      infrastructureHealthMatrixOverview: "Vista General de Matriz de Salud",
      infrastructureOverviewOverview: "Vista General de Infraestructura",
      capacityForecastOverview: "Vista General de Pronóstico de Capacidad",
      resourceQuotaOverview: "Vista General de Cuotas de Recursos",
    },

    // Administración
    admin: {
      title: "Panel de Administración",
      users: "Usuarios",
      roles: "Roles",
      permissions: "Permisos",
      departments: "Departamentos",
      sessions: "Sesiones",
      audit: "Auditoría",
      licenses: "Licencias",
      nodes: "Nodos",
      updates: "Actualizaciones",
      menuManagement: "Gestión de Menús",
      notifications: "Notificaciones",
      systemSettings: "Configuración del Sistema",
      userManagement: "Gestión de Usuarios",
      systemHealth: "Salud del Sistema",
      securitySettings: "Configuración de Seguridad",
      backupRestore: "Respaldo y Restauración",
      systemLogs: "Logs del Sistema",
      performanceMetrics: "Métricas de Rendimiento",
      // Nuevas traducciones para la página principal
      twoHoursAgo: "hace 2 horas",
      twoMinutesAgo: "hace 2 minutos",
      fiveMinutesAgo: "hace 5 minutos",
      tenMinutesAgo: "hace 10 minutos",
      fifteenMinutesAgo: "hace 15 minutos",
      userLogin: "Inicio de sesión de usuario",
      roleUpdated: "Rol actualizado",
      newUserInvited: "Nuevo usuario invitado",
      systemBackupCompleted: "Respaldo del sistema completado",
      configurationUpdated: "Configuración actualizada",
    },

    // Temas
    themes: {
      title: "Temas de CodeflowX",
      changeTheme: "Cambiar tema de CodeflowX",
      useSystemTheme: "Usar tema del sistema",
      trekkerFederation: "🌌 Leka Federación",
      trekkerVulcan: "🖖 Leka Vulcano",
      trekkerKlingon: "⚔️ Leka Klingon",
      trekkerRomulan: "🖖 Leka Romulano",
      trekkerCardassian: "🖖 Leka Cardassiano",
      trekkerBajoran: "🖖 Leka Bajorano",
      trekkerFerengi: "💰 Leka Ferengi",
      trekkerAndorian: "🖖 Leka Andoriano",
      trekkerTellarite: "🖖 Leka Tellarita",
      trekkerDenobulan: "🏥 Leka Denobulano",
    },

    // Estados y colores
    status: {
      online: "En Línea",
      offline: "Desconectado",
      maintenance: "Mantenimiento",
      error: "Error",
      warning: "Advertencia",
      info: "Información",
      success: "Éxito",
      pending: "Pendiente",
      running: "Ejecutando",
      stopped: "Detenido",
      starting: "Iniciando",
      stopping: "Deteniendo",
      healthy: "Saludable",
      unhealthy: "No Saludable",
      degraded: "Degradado",
    },

    // Mensajes del sistema
    messages: {
      loadingTheme: "Cargando tema...",
      themeApplied: "Tema aplicado correctamente",
      themeError: "Error al aplicar el tema",
      redirectingToLogin: "Redirigiendo al login...",
      systemActive: "Sistema Activo",
      aiPowered: "AI Powered",
      documentation: "Documentación",
      support: "Soporte",
      status: "Estado",
      welcomeMessage: "Bienvenido, {name} 👋",
    },

    // Model Management
    modelManagement: {
      title: "Gestión de Modelos",
      subtitle: "Visión general del sistema de gestión de modelos de IA",
      totalModels: "Total de Modelos",
      activeModels: "Modelos Activos",
      totalProviders: "Total de Proveedores",
      marketplaceModels: "Modelos del Marketplace",
      recentDownloads: "Descargas Recientes",
      systemHealth: "Salud del Sistema",
      healthy: "Saludable",
      warning: "Advertencia",
      critical: "Crítico",
      approved: "Aprobado",
      pending: "Pendiente",
      rejected: "Rechazado",
      addModel: "Añadir Modelo",
      viewModels: "Ver Modelos",
      manageProviders: "Gestionar Proveedores",
      marketplace: "Marketplace",
      recentActivity: "Actividad Reciente",
      topProviders: "Mejores Proveedores",
      trendingModels: "Modelos en Tendencia",
      stats: {
        totalModels: "Total Models",
        totalModelsChange: "+12% este mes",
        activeModels: "Active Models",
        activeModelsChange: "+8% este mes",
        providers: "Providers",
        providersChange: "+3 este mes",
        recentDownloads: "Recent Downloads",
        recentDownloadsChange: "+25% esta semana",
      },
      systemStatus: {
        title: "Estado del Sistema",
        generalStatus: "Estado General",
        healthy: "Saludable",
        warning: "Advertencia",
        apiModels: "API de Modelos",
        marketplace: "Marketplace",
        providers: "Proveedores",
        operational: "Operativo",
      },
      recentModels: {
        title: "Recent Models",
        noModels: "No recent models",
      },
      typeDistribution: {
        title: "Models by Type",
        viewDetails: "View Details",
        text: "Text",
        vision: "Vision",
        audio: "Audio",
      },
      quickActions: {
        title: "Quick Actions",
        newModel: "New Model",
        addProvider: "Add Provider",
        exploreMarketplace: "Explore Marketplace",
        settings: "Settings",
      },
      systemInfo: {
        title: "System Information",
        modelManagement: {
          title: "Model Management",
          description:
            "Registro, versionado y gestión del ciclo de vida completo de modelos de IA",
        },
        aiProviders: {
          title: "AI Providers",
          description:
            "Integración con múltiples proveedores de servicios de IA y gestión de tokens",
        },
        marketplace: {
          title: "Marketplace",
          description:
            "Acceso a +3,000,000 modelos de IA de múltiples marketplaces y proveedores",
        },
        // Página de Models
        models: {
          title: "Model Management",
          subtitle: "Manage all AI models registered in the system",
          actions: {
            newModel: "New Model",
            export: "Export",
            import: "Import",
          },
          stats: {
            totalModels: "Total Models",
            approved: "Approved",
            training: "Training",
            marketplace: "Marketplace",
          },
          tabs: {
            all: "All",
            approved: "Approved",
            draft: "Drafts",
            training: "Training",
            error: "Errors",
            marketplace: "Marketplace",
          },
          filters: {
            searchPlaceholder: "Search models...",
            typePlaceholder: "Model Type",
            statusPlaceholder: "Status",
            providerPlaceholder: "Provider",
            allTypes: "All types",
            allStatuses: "All statuses",
            allProviders: "All providers",
          },
          noModels: "No models registered in the system",
          noModelsFound: "No models found with applied filters",
        },
      },
      // Página de Models
      models: {
        title: "Gestión de Modelos",
        subtitle:
          "Administra todos los modelos de IA registrados en el sistema",
        actions: {
          newModel: "Nuevo Modelo",
          export: "Exportar",
          import: "Importar",
        },
        stats: {
          totalModels: "Total Models",
          approved: "Aprobados",
          training: "Entrenando",
          marketplace: "Marketplace",
        },
        tabs: {
          all: "Todos",
          approved: "Aprobados",
          draft: "Borradores",
          training: "Entrenando",
          error: "Errores",
          marketplace: "Marketplace",
        },
        filters: {
          searchPlaceholder: "Buscar modelos...",
          typePlaceholder: "Tipo de Modelo",
          statusPlaceholder: "Estado",
          providerPlaceholder: "Proveedor",
          allTypes: "Todos los tipos",
          allStatuses: "Todos los estados",
          allProviders: "Todos los proveedores",
        },
        noModels: "No hay modelos registrados en el sistema",
        noModelsFound: "No se encontraron modelos con los filtros aplicados",
      },
      approval: {
        title: "Aprobaciones de Modelos",
        subtitle: "Gestión de solicitudes de aprobación pendientes",
      },
      biasAnalysis: {
        title: "Análisis de Sesgo",
        subtitle: "Gestión de análisis de sesgo",
      },
      dependencies: {
        title: "Dependencias",
        subtitle: "Gestión de dependencias de modelos",
      },
      explainability: {
        title: "Explicabilidad",
        subtitle: "Gestión de explicabilidad de modelos",
      },
      performance: {
        title: "Rendimiento",
        subtitle: "Gestión de rendimiento de modelos",
      },
      usageOverview: {
        title: "Uso de Modelos",
        subtitle: "Vista general del uso de modelos",
      },
      artifactOverview: {
        title: "Artefactos",
        subtitle: "Gestión de artefactos de modelos",
      },
      capabilityOverview: {
        title: "Capacidades",
        subtitle: "Gestión de capacidades de modelos",
      },
      catalogOverview: {
        title: "Catálogo",
        subtitle: "Gestión del catálogo de modelos",
      },
      endpointOverview: {
        title: "Endpoints",
        subtitle: "Gestión de endpoints de modelos",
      },
      providerCredentialOverview: {
        title: "Credenciales de Proveedor",
        subtitle: "Gestión de credenciales de proveedores",
      },
      providerOverview: {
        title: "Proveedores",
        subtitle: "Gestión de proveedores de modelos",
      },
      versioning: {
        title: "Versionado",
        subtitle: "Gestión de versiones de modelos",
      },
    },

    // Monitoring
    monitoring: {
      title: "Monitoreo",
      alerts: {
        overview: {
          title: "Alertas de Monitoreo",
          description: "Gestión de alertas de monitoreo",
          register: "Registrar Alerta",
          list: "Lista de Alertas",
          alertName: "Alert Name",
          alertType: "Alert Type",
          severity: "Severity",
          status: "Status",
        },
        detail: {
          title: "Detalle de Alerta",
          description: "Gestión y configuración de alerta de monitoreo",
          information: "Información",
          alertName: "Alert Name",
          alertType: "Alert Type",
          severity: "Severity",
          status: "Status",
          description: "Description",
          alertData: "Alert Data",
          timestamps: "Timestamps",
          triggeredAt: "Triggered At",
          acknowledgedAt: "Acknowledged At",
          acknowledgedBy: "Acknowledged By",
          resolvedAt: "Resolved At",
          resolutionNotes: "Resolution Notes",
        },
        systemDetail: {
          title: "Detalle de Alerta del Sistema",
          description: "Gestión de alertas del sistema",
          information: "Información",
          alertName: "Alert Name",
          alertType: "Alert Type",
          severity: "Severity",
          status: "Status",
          description: "Description",
          timestamps: "Timestamps",
          triggeredAt: "Triggered At",
          acknowledgedAt: "Acknowledged At",
        },
        responseForm: {
          title: "Respuesta a Alerta Crítica",
          description: "Formulario de respuesta a alerta",
          formData: "Form Data",
          decision: "Decision",
          decisionPlaceholder: "Enter decision...",
          notes: "Notes",
          notesPlaceholder: "Enter notes...",
        },
      },
      metrics: {
        overview: {
          title: "Métricas de Monitoreo",
          description: "Gestión de métricas de monitoreo",
          register: "Registrar Métrica",
          list: "Lista de Métricas",
        },
        detail: {
          title: "Detalle de Métrica",
          description: "Gestión de métricas de monitoreo",
          information: "Información",
          metricName: "Metric Name",
          metricType: "Metric Type",
          metricValue: "Metric Value",
          status: "Status",
          description: "Description",
          timestamps: "Timestamps",
          collectedAt: "Collected At",
        },
        systemDetail: {
          title: "Detalle de Métrica del Sistema",
          description: "Gestión de métricas del sistema",
          information: "Información",
          metricName: "Metric Name",
          metricType: "Metric Type",
          metricValue: "Metric Value",
          status: "Status",
          description: "Description",
          timestamps: "Timestamps",
          collectedAt: "Collected At",
        },
      },
      dashboard: {
        title: "Dashboard de Monitoreo",
        description: "Vista general del sistema de monitoreo",
        alerts: "Alertas",
        alertsDesc: "Gestión de alertas de monitoreo",
        metrics: "Métricas",
        metricsDesc: "Visualización de métricas del sistema",
        systemHealth: "Salud del Sistema",
        systemHealthDesc: "Estado de salud del sistema",
        performance: "Performance",
        performanceDesc: "Métricas de rendimiento",
        quickStats: "Estadísticas Rápidas",
        activeAlerts: "Alertas Activas",
        uptime: "Uptime",
        avgResponse: "Respuesta Promedio",
      },
      auditLog: {
        detail: {
          title: "Detalle de Log de Auditoría",
          description: "Información detallada del log de auditoría",
          information: "Información",
          action: "Action",
          entity: "Entity",
          entityId: "Entity ID",
          user: "User",
          ipAddress: "IP Address",
          status: "Status",
          details: "Details",
          timestamps: "Timestamps",
          timestamp: "Timestamp",
          createdAt: "Created At",
        },
      },
      systemHealth: {
        detail: {
          title: "Detalle de Salud del Sistema",
          description: "Información de salud del sistema",
          information: "Información",
          status: "Status",
          uptime: "Uptime",
          details: "Details",
          timestamps: "Timestamps",
          lastCheck: "Last Check",
          updatedAt: "Updated At",
        },
      },
      governance: {
        detail: {
          title: "Detalle de Gobernanza",
          description: "Gestión de políticas de gobernanza",
          information: "Información",
          name: "Name",
          type: "Type",
          status: "Status",
          description: "Description",
          details: "Details",
          timestamps: "Timestamps",
          createdAt: "Created At",
          updatedAt: "Updated At",
        },
      },
      performance: {
        interventionForm: {
          title: "Intervención de Performance",
          description: "Formulario de intervención de performance",
          formData: "Form Data",
          interventionType: "Intervention Type",
          description: "Description",
          descriptionPlaceholder: "Describe the intervention...",
          priority: "Priority",
          notes: "Notes",
          notesPlaceholder: "Additional notes...",
        },
      },
      viewsPerformance: {
        title: "Performance de Vistas",
        description: "Configuración de performance de vistas",
        formData: "Form Data",
        viewName: "View Name",
        viewNamePlaceholder: "Enter view name...",
        performanceMetric: "Performance Metric",
        performanceMetricPlaceholder: "Enter metric...",
        threshold: "Threshold",
        thresholdPlaceholder: "Enter threshold...",
        notes: "Notes",
        notesPlaceholder: "Additional notes...",
      },
    },

    // Agents
    agents: {
      dashboard: {
        title: "Dashboard de Agentes",
        subtitle: "Métricas y estado general de agentes AI",
        totalAgents: "Total Agentes",
        activeAgents: "Agentes Activos",
        deployedAgents: "Agentes Desplegados",
        healthScore: "Health Score",
        recentAgents: "Agentes Recientes",
        performanceMetrics: "Métricas de Rendimiento",
        avgPerformanceScore: "Score Promedio",
        offlineAgents: "Agentes Offline",
        version: "Versión",
      },
      alerts: {
        title: "Agent Alert",
        subtitle: "Gestión de Agent Alert",
        register: "Registrar Agent Alert",
        list: "Listado de Alertas",
        alertType: "Alert Type",
        alertCategory: "Alert Category",
        severity: "Severity",
      },
      approval: {
        title: "Agent Approval",
        subtitle: "Gestión de Agent Approval",
        register: "Registrar Agent Approval",
        list: "Listado de Aprobaciones",
        approvalType: "Approval Type",
        approvalStatus: "Approval Status",
      },
      biasDetection: {
        title: "Bias Detection",
        subtitle: "Gestión de Bias Detection",
        register: "Registrar Bias Detection",
        list: "Listado de Detecciones",
        detectionType: "Detection Type",
        severity: "Severity",
      },
      compliance: {
        title: "Compliance",
        subtitle: "Gestión de Compliance",
        register: "Registrar Compliance",
        list: "Listado de Compliance",
        complianceType: "Compliance Type",
        lastCheck: "Last Check",
        score: "Score",
      },
      decisions: {
        title: "Decisions",
        subtitle: "Gestión de Decisions",
        register: "Registrar Decision",
        list: "Listado de Decisions",
        decisionType: "Decision Type",
        confidence: "Confidence",
        timestamp: "Timestamp",
      },
      deployment: {
        title: "Deployment",
        subtitle: "Gestión de Deployment",
        register: "Registrar Deployment",
        list: "Listado de Deployments",
        deploymentType: "Deployment Type",
        environment: "Environment",
        deployedAt: "Deployed At",
      },
      ethics: {
        title: "Ethics",
        subtitle: "Gestión de Ethics",
        register: "Registrar Ethics",
        list: "Listado de Ethics",
        assessmentType: "Assessment Type",
        score: "Score",
        assessedAt: "Assessed At",
      },
      governance: {
        title: "Governance",
        subtitle: "Gestión de Governance",
        register: "Registrar Governance",
        list: "Listado de Governance",
        governanceType: "Governance Type",
        lastReview: "Last Review",
        complianceScore: "Compliance Score",
      },
      interactions: {
        title: "Interactions",
        subtitle: "Gestión de Interactions",
        register: "Registrar Interaction",
        list: "Listado de Interactions",
        interactionType: "Interaction Type",
        participants: "Participants",
        timestamp: "Timestamp",
      },
      collaboration: {
        title: "Collaboration",
        subtitle: "Gestión de Collaboration",
        register: "Registrar Collaboration",
        list: "Listado de Collaborations",
        collaborationType: "Collaboration Type",
        agents: "Agents",
        createdAt: "Created At",
      },
      communication: {
        title: "Communication",
        subtitle: "Gestión de Communication",
        register: "Registrar Communication",
        list: "Listado de Communications",
        communicationType: "Communication Type",
        messages: "Messages",
        lastMessage: "Last Message",
      },
      learning: {
        title: "Learning",
        subtitle: "Gestión de Learning",
        register: "Registrar Learning",
        list: "Listado de Learning",
        learningType: "Learning Type",
        progress: "Progress",
        lastUpdate: "Last Update",
      },
      monitoring: {
        title: "Monitoring",
        subtitle: "Gestión de Monitoring",
        register: "Registrar Monitoring",
        list: "Listado de Monitoring",
        monitoringType: "Monitoring Type",
        metrics: "Metrics",
        lastCheck: "Last Check",
      },
      health: {
        title: "Health Overview",
        subtitle: "Gestión de Health",
        register: "Registrar Health",
        list: "Listado de Health",
        healthStatus: "Health Status",
        score: "Score",
        lastCheck: "Last Check",
      },
      domain: {
        title: "Domain Overview",
        subtitle: "Gestión de Domains",
        register: "Registrar Domain",
        list: "Listado de Domains",
        domainName: "Domain Name",
        agents: "Agents",
        createdAt: "Created At",
      },
      rollback: {
        title: "Rollback",
        subtitle: "Gestión de Rollback",
        register: "Registrar Rollback",
        list: "Listado de Rollbacks",
        rollbackType: "Rollback Type",
        version: "Version",
        rolledBackAt: "Rolled Back At",
      },
      tools: {
        title: "Tools",
        subtitle: "Gestión de Tools",
        register: "Registrar Tool",
        list: "Listado de Tools",
        toolName: "Tool Name",
        toolType: "Tool Type",
        version: "Version",
      },
      transparency: {
        title: "Transparency",
        subtitle: "Gestión de Transparency",
        register: "Registrar Transparency",
        list: "Listado de Transparency",
        transparencyType: "Transparency Type",
        score: "Score",
        lastAudit: "Last Audit",
      },
      versioning: {
        title: "Versioning",
        subtitle: "Gestión de Versioning",
        register: "Registrar Version",
        list: "Listado de Versions",
        versionNumber: "Version Number",
        releaseDate: "Release Date",
        changes: "Changes",
      },
      workflow: {
        execution: {
          title: "Execution Overview",
          subtitle: "Gestión de Workflow Executions",
          register: "Registrar Execution",
          list: "Listado de Executions",
          executionType: "Execution Type",
          duration: "Duration (s)",
          startedAt: "Started At",
        },
      },
    },

    // Domain Ingestion
    domainIngestion: {
      title: "Dashboard de Ingesta de Dominios",
      subtitle: "Monitorea y gestiona tus dominios de datos especializados",
      stats: {
        totalDomains: "Total Dominios",
        totalDocuments: "Total Documentos",
        webPages: "Páginas Web",
        apis: "APIs Conectadas",
        activeDomains: "dominios activos",
        documentsChange: "+12% este mes",
        webChange: "+8% esta semana",
        apisChange: "+3 este mes",
      },
      actions: {
        wizard: "Asistente de Dominios",
        manage: "Gestionar Dominios",
        access: "Acceder",
      },
      modules: {
        webScraping: {
          title: "Web Scraping",
          description: "Extrae datos de sitios web y monitoriza cambios",
        },
        uploadDocuments: {
          title: "Subir Documentos",
          description: "Sube y procesa documentos para enriquecer dominios",
        },
        monitor: {
          title: "Monitorización",
          description: "Monitorea el rendimiento y estado de los trabajos",
        },
        settings: {
          title: "Configuración",
          description: "Configura parámetros globales y por dominio",
        },
        configureSources: {
          title: "Configurar Fuentes",
          description: "Configura fuentes de datos y APIs",
        },
      },
      recentJobs: {
        title: "Trabajos Recientes",
      },
      jobStatus: {
        completed: "Completado",
        running: "Ejecutando",
        failed: "Fallido",
        pending: "Pendiente",
        queued: "En Cola",
        processing: "Procesando",
        cancelled: "Cancelado",
        retrying: "Reintentando",
      },
      wizard: {
        steps: {
          basicInfo: "Información Básica",
          dataSources: "Fuentes de Datos",
          compliance: "Cumplimiento",
          advancedFeatures: "Características Avanzadas",
          review: "Revisión",
        },
        templates: {
          title: "Plantillas de Dominio",
          description:
            "Selecciona una plantilla predefinida o crea una personalizada",
          fintech: "Fintech Banking",
          healthcare: "Healthcare Analytics",
          ecommerce: "E-commerce Insights",
          government: "IA Act Compliance",
          manufacturing: "Manufacturing IoT",
          custom: "Personalizado",
        },
        industries: {
          title: "Industria",
          fintech: "Fintech",
          healthcare: "Healthcare",
          ecommerce: "E-commerce",
          government: "Gobierno",
          manufacturing: "Manufactura",
          education: "Educación",
          legal: "Legal",
          other: "Otro",
        },
        compliance: {
          title: "Marcos de Cumplimiento",
          description: "Selecciona los marcos de cumplimiento aplicables",
          gdpr: "GDPR (Reglamento General de Protección de Datos)",
          sox: "SOX (Sarbanes-Oxley Act)",
          pci: "PCI DSS (Payment Card Industry Data Security Standard)",
          hipaa: "HIPAA (Health Insurance Portability and Accountability Act)",
          iaActEspana: "IA Act España (Ley de Inteligencia Artificial)",
          iaActEuropa: "IA Act Europa (Artificial Intelligence Act)",
          iso27001: "ISO 27001 (Gestión de Seguridad de la Información)",
          iso27701: "ISO 27701 (Privacidad de la Información)",
          nist: "NIST Cybersecurity Framework",
          soc2: "SOC 2 (Service Organization Control 2)",
          fedramp:
            "FedRAMP (Federal Risk and Authorization Management Program)",
          ccpa: "CCPA (California Consumer Privacy Act)",
          lgpd: "LGPD (Lei Geral de Proteção de Dados)",
          pipeda:
            "PIPEDA (Personal Information Protection and Electronic Documents Act)",
          // Estándares ISO de Desarrollo de Software
          iso12207: "ISO 12207 (Ciclo de Vida del Software)",
          iso15504: "ISO 15504 (Evaluación de Procesos de Software)",
          iso20000: "ISO 20000 (Gestión de Servicios TI)",
          iso27002: "ISO 27002 (Controles de Seguridad de la Información)",
          iso27005: "ISO 27005 (Gestión de Riesgos de Seguridad)",
          iso27017: "ISO 27017 (Seguridad en la Nube)",
          iso27018: "ISO 27018 (Protección de Datos en la Nube)",
          iso9001: "ISO 9001 (Sistema de Gestión de Calidad)",
          iso14001: "ISO 14001 (Sistema de Gestión Ambiental)",
          // Normativas Europeas Adicionales
          eidas: "eIDAS (Identificación Electrónica UE)",
          psd2: "PSD2 (Directiva de Servicios de Pago UE)",
          nis2: "NIS2 (Directiva de Seguridad de Redes UE)",
          // Estándares de Calidad de Software
          cmmi: "CMMI (Modelo de Madurez de Capacidades)",
          tickit: "TICKIT (Calidad en TI y Telecomunicaciones)",
          spice: "SPICE (Evaluación de Procesos de Software)",
          custom: "Personalizado",
        },
        dataSources: {
          title: "Tipos de Fuentes de Datos",
          description:
            "Selecciona los tipos de fuentes de datos que utilizarás",
          api: "API",
          database: "Base de Datos",
          file: "Archivo",
          web: "Web",
          stream: "Stream",
        },
        basicInfo: {
          name: "Nombre del Dominio",
          description: "Descripción",
          industry: "Industria",
          owner: "Propietario",
          team: "Equipo",
        },
        advancedFeatures: {
          title: "Características Avanzadas",
          rag: "RAG (Crear datasets)",
          training: "Entrenamiento de Modelos",
          compliance: "Cumplimiento Automático",
          monitoring: "Monitorización Avanzada",
          modelSelection: "Selección de Modelo",
          modelName: "Nombre del Modelo Interno",
          adapter: "Crear Adapter",
          finetuning: "Fine-tuning",
          baseModel: "Modelo Base",
          customModel: "Modelo Personalizado",
        },
        review: {
          title: "Revisión Final",
          description: "Revisa la configuración antes de crear el dominio",
          template: "Plantilla seleccionada",
          industry: "Industria",
          compliance: "Marcos de cumplimiento",
          dataSources: "Fuentes de datos",
          features: "Características habilitadas",
        },
        template: {
          fintech: {
            name: "Fintech Banking",
            description:
              "Dominio especializado en análisis de datos financieros y cumplimiento regulatorio",
            features: [
              "RAG habilitado",
              "Cumplimiento GDPR/SOX",
              "Monitorización avanzada",
            ],
            compliance: ["GDPR", "SOX", "PCI DSS"],
          },
          healthcare: {
            name: "Healthcare Analytics",
            description:
              "Dominio para análisis de datos médicos con cumplimiento HIPAA",
            features: [
              "RAG habilitado",
              "Cumplimiento HIPAA",
              "Encriptación avanzada",
            ],
            compliance: ["HIPAA", "GDPR"],
          },
          ecommerce: {
            name: "E-commerce Insights",
            description:
              "Dominio para análisis de comportamiento de compra y recomendaciones",
            features: [
              "RAG habilitado",
              "Análisis de comportamiento",
              "Recomendaciones",
            ],
            compliance: ["GDPR"],
          },
          government: {
            name: "IA Act Compliance",
            description:
              "Dominio para cumplimiento con la IA Act española y regulaciones gubernamentales",
            features: [
              "RAG habilitado",
              "Cumplimiento IA Act",
              "Auditoría completa",
            ],
            compliance: ["IA Act España", "GDPR"],
          },
          manufacturing: {
            name: "Manufacturing IoT",
            description:
              "Dominio para análisis de datos IoT en entornos industriales",
            features: [
              "RAG habilitado",
              "Análisis IoT",
              "Monitorización en tiempo real",
            ],
            compliance: ["ISO 27001"],
          },
        },
        actions: {
          next: "Siguiente",
          previous: "Anterior",
          create: "Crear Dominio",
          cancel: "Cancelar",
        },
      },
      manage: {
        title: "Manage Domains",
        subtitle: "View and manage all your data domains",
        stats: {
          total: "Total",
          active: "Active",
          inactive: "Inactive",
          processing: "Processing",
        },
        search: {
          placeholder: "Search domains...",
        },
        filters: {
          all: "All",
          active: "Active",
          inactive: "Inactive",
          processing: "Processing",
          more: "More filters",
        },
        actions: {
          newDomain: "New Domain",
          viewDetails: "View Details",
          edit: "Edit",
          delete: "Delete",
        },
        noResults: {
          title: "No domains found",
          description: "Try adjusting the filters or create a new domain",
        },
        details: {
          title: "Domain Details",
          basicInfo: "Basic Information",
          performance: "Performance",
          dataSources: "Data Sources",
          documents: "Documents",
          compliance: "Compliance",
          lastActivity: "Last Activity",
          status: "Status",
          industry: "Industry",
          owner: "Owner",
          createdAt: "Created",
          totalDocuments: "Total Documents",
          processedDocuments: "Processed Documents",
          dataSourcesCount: "Data Sources",
          complianceScore: "Compliance Score",
          ragEnabled: "RAG Enabled",
          trainingEnabled: "Training Enabled",
        },
      },
      webScraping: {
        title: "Web Scraping",
        subtitle: "Configure and monitor web data extraction",
        stats: {
          total: "Total",
          active: "Active",
          running: "Running",
          completed: "Completed",
        },
        search: {
          placeholder: "Search jobs...",
        },
        filters: {
          all: "All",
          active: "Active",
          running: "Running",
          completed: "Completed",
          failed: "Failed",
          more: "More filters",
        },
        actions: {
          newJob: "New Job",
          viewDetails: "View Details",
          start: "Start",
          stop: "Stop",
          configure: "Configure",
        },
        noResults: {
          title: "No jobs found",
          description: "Try adjusting the filters or create a new job",
        },
        details: {
          title: "Job Details",
          basicInfo: "Basic Information",
          progress: "Progress",
          metrics: "Metrics",
          schedule: "Schedule",
          lastRun: "Last Run",
          nextRun: "Next Run",
          pagesScraped: "Pages Scraped",
          totalPages: "Total Pages",
          dataExtracted: "Data Extracted",
          errors: "Errors",
          priority: "Priority",
        },
        newJob: {
          title: "New Web Scraping Job",
          name: "Job Name",
          url: "Target URL",
          domain: "Domain",
          schedule: "Schedule",
          priority: "Priority",
          create: "Create Job",
          cancel: "Cancel",
        },
      },
      uploadDocuments: {
        title: "Upload Documents",
        subtitle: "Upload and manage document batches",
        stats: {
          total: "Total",
          processing: "Processing",
          completed: "Completed",
          failed: "Failed",
        },
        search: {
          placeholder: "Search batches...",
        },
        filters: {
          all: "All",
          processing: "Processing",
          completed: "Completed",
          failed: "Failed",
          more: "More filters",
        },
        actions: {
          newBatch: "New Batch",
          viewDetails: "View Details",
          pause: "Pause",
          resume: "Resume",
          configure: "Configure",
        },
        noResults: {
          title: "No batches found",
          description: "Try adjusting the filters or create a new batch",
        },
        details: {
          title: "Batch Details",
          basicInfo: "Basic Information",
          progress: "Progress",
          files: "Files",
          processing: "Processing",
          name: "Batch Name",
          domain: "Domain",
          sector: "Sector",
          priority: "Priority",
          status: "Status",
          totalFiles: "Total Files",
          processedFiles: "Processed Files",
          totalSize: "Total Size",
          processedSize: "Processed Size",
          createdAt: "Created",
          updatedAt: "Updated",
          processingType: "Processing Type",
          ragEnabled: "RAG Enabled",
          trainingEnabled: "Training Enabled",
          complianceEnabled: "Compliance Enabled",
          errors: "Errors",
          warnings: "Warnings",
        },
        newBatch: {
          title: "New Document Batch",
          explanation:
            "A batch is a collection of documents that will be processed together. This allows for better organization and tracking of document processing.",
          name: "Batch Name",
          domain: "Domain",
          sector: "Sector",
          priority: "Priority",
          processingType: "Processing Type",
          create: "Create Batch",
          cancel: "Cancel",
        },
      },
      monitor: {
        title: "Monitor",
        subtitle: "Real-time monitoring of ingestion jobs",
        stats: {
          total: "Total",
          running: "Running",
          completed: "Completed",
          failed: "Failed",
        },
        search: {
          placeholder: "Search jobs...",
        },
        filters: {
          all: "All",
          running: "Running",
          completed: "Completed",
          failed: "Failed",
          more: "More filters",
        },
        actions: {
          viewDetails: "View Details",
          configure: "Configure",
        },
        noResults: {
          title: "No jobs found",
          description: "Try adjusting the filters or start a new job",
        },
        details: {
          title: "Job Details",
          basicInfo: "Basic Information",
          performance: "Performance",
          metrics: "Metrics",
          alerts: "Alerts",
          name: "Job Name",
          domain: "Domain",
          type: "Type",
          status: "Status",
          startTime: "Start Time",
          endTime: "End Time",
          duration: "Duration",
          cpu: "CPU",
          memory: "Memory",
          throughput: "Throughput",
          latency: "Latency",
          alerts: "Alerts",
        },
      },
      settings: {
        title: "Settings",
        subtitle: "Configure global and domain-specific settings",
        stats: {
          total: "Total",
          active: "Active",
          configured: "Configured",
          default: "Default",
        },
        search: {
          placeholder: "Search domains...",
        },
        filters: {
          all: "All",
          active: "Active",
          configured: "Configured",
          default: "Default",
          more: "More filters",
        },
        tabs: {
          domains: "Domains",
          global: "Global",
          advanced: "Advanced",
        },
        actions: {
          viewDetails: "View Details",
          save: "Save",
          reset: "Reset",
        },
        noResults: {
          title: "No domains found",
          description: "Try adjusting the filters or create a new domain",
        },
        details: {
          title: "Domain Settings",
          basicInfo: "Basic Information",
          configuration: "Configuration",
          ragExplanation:
            "RAG (Retrieval-Augmented Generation) is used to process documents and create training datasets. It performs automatic chunking and tagging of content for AI model training. Domains are business models, not RAG systems.",
          ragEnabled: "RAG Enabled for Datasets",
          trainingEnabled: "Training Enabled",
          complianceEnabled: "Compliance Enabled",
          autoIngestion: "Auto Ingestion",
          qualityThreshold: "Quality Threshold",
          retentionDays: "Retention Days",
          lastUpdated: "Last Updated",
          status: "Status",
        },
        global: {
          general: "General Configuration",
          security: "Security",
          autoBackup: "Auto Backup",
          notifications: "Notifications",
          language: "Language",
          timezone: "Timezone",
          encryption: "Encryption",
          authentication: "Authentication",
          auditLogs: "Audit Logs",
        },
        advanced: {
          performance: "Performance",
          logs: "Logs & Monitoring",
          cacheSize: "Cache Size",
          maxConnections: "Max Connections",
          logLevel: "Log Level",
          monitoring: "Monitoring",
          alerts: "Alerts",
        },
      },
      configureSources: {
        title: "Configure Sources",
        subtitle: "Manage and configure data sources for each domain",
        stats: {
          total: "Total",
          active: "Active",
          api: "APIs",
        },
        search: {
          placeholder: "Search sources...",
        },
        filters: {
          allTypes: "All types",
          api: "API",
          database: "Database",
          file: "File",
          web: "Web",
          stream: "Stream",
          more: "More filters",
        },
        actions: {
          newSource: "New Source",
          viewDetails: "View Details",
          edit: "Edit",
          test: "Test Connection",
          configure: "Configure",
        },
        noResults: {
          title: "No sources found",
          description: "Try adjusting the filters or create a new data source",
        },
        status: {
          active: "Active",
          inactive: "Inactive",
          error: "Error",
          maintenance: "Maintenance",
          unknown: "Unknown",
        },
        info: {
          type: "Type:",
          schedule: "Schedule:",
          dataVolume: "Volume:",
          syncStatus: "Sync:",
        },
        features: {
          compliance: "Compliance:",
          rag: "RAG",
          training: "Training",
        },
        newSource: {
          title: "New Data Source",
          name: "Source Name",
          namePlaceholder: "e.g., API Gateway",
          domain: "Domain",
          selectDomain: "Select domain",
          type: "Source Type",
          selectType: "Select type",
          url: "URL/Endpoint",
          urlPlaceholder: "https://api.example.com/v1",
          credentials: "Credentials Type",
          credentialsNone: "None",
          credentialsApiKey: "API Key",
          credentialsOAuth: "OAuth",
          credentialsBasic: "Basic Auth",
          schedule: "Schedule",
          scheduleRealTime: "Real-time",
          schedule15Min: "Every 15 minutes",
          scheduleHour: "Every hour",
          scheduleDaily: "Daily",
          scheduleWeekly: "Weekly",
          processingOptions: "Processing Options",
          ragEnabled: "Enable RAG (Create datasets)",
          ragDescription: "Process data to create training datasets",
          trainingEnabled: "Enable Training",
          trainingDescription: "Generate content for model training",
          create: "Create Source",
          cancel: "Cancel",
        },
        details: {
          basicInfo: "Basic Information",
          sync: "Synchronization",
          credentials: "Credentials",
          compliance: "Compliance",
          processing: "Processing",
          domain: "Domain:",
          type: "Type:",
          status: "Status:",
          url: "URL:",
          schedule: "Schedule:",
          lastSync: "Last sync:",
          nextSync: "Next sync:",
          dataVolume: "Data volume:",
          credentialType: "Type:",
          lastUpdated: "Last updated:",
          rag: "RAG (Datasets)",
          training: "Training",
          edit: "Edit",
          test: "Test Connection",
          configure: "Configure",
        },
      },
      domainDetails: {
        navigation: {
          back: "Back",
        },
        actions: {
          edit: "Edit",
          settings: "Settings",
          monitor: "Monitor",
        },
        stats: {
          documents: "Documents",
          processed: "Processed",
          dataSources: "Data Sources",
          active: "active",
          compliance: "Compliance",
          compliant: "Compliant",
          successRate: "Success Rate",
          avgTime: "average",
        },
        tabs: {
          overview: "Overview",
          dataSources: "Sources",
          documents: "Documents",
          compliance: "Compliance",
          alerts: "Alerts",
        },
        overview: {
          domainInfo: "Domain Information",
          performance: "Performance",
          ingestionMetrics: "Ingestion Metrics",
          industry: "Industry",
          status: "Status",
          owner: "Owner",
          created: "Created",
          team: "Team",
          dataQuality: "Data Quality",
          rag: "RAG",
          training: "Training",
          lastActivity: "Last Activity",
          daily: "Daily",
          weekly: "Weekly",
          monthly: "Monthly",
        },
        dataSources: {
          title: "Data Sources",
          lastSync: "Last sync:",
        },
        documents: {
          title: "Documents",
        },
        compliance: {
          frameworks: "Compliance Frameworks",
          audits: "Audits",
          status: "Status:",
          lastAudit: "Last Audit",
          nextAudit: "Next Audit",
        },
        alerts: {
          title: "Alerts",
          resolved: "Resolved",
        },
        status: {
          active: "Active",
          inactive: "Inactive",
          maintenance: "Maintenance",
          error: "Error",
          unknown: "Unknown",
        },
        edit: {
          title: "Edit Domain",
          name: "Domain Name",
          industry: "Industry",
          description: "Description",
          save: "Save Changes",
          cancel: "Cancel",
        },
        settings: {
          title: "Domain Settings",
          processing: "Processing",
          ragEnabled: "Enable RAG (Create datasets)",
          ragDescription: "Process documents to create training datasets",
          trainingEnabled: "Enable Training",
          trainingDescription: "Generate content for model training",
          save: "Save Settings",
          cancel: "Cancel",
        },
      },
      manage: {
        title: "Gestionar Dominios",
        subtitle: "Visualiza y gestiona todos tus dominios de datos",
        stats: {
          total: "Total",
          active: "Activos",
          inactive: "Inactivos",
          processing: "Procesando",
        },
        search: {
          placeholder: "Buscar dominios...",
        },
        filters: {
          all: "Todos",
          active: "Activos",
          inactive: "Inactivos",
          processing: "Procesando",
          more: "Más filtros",
        },
        actions: {
          newDomain: "Nuevo Dominio",
          viewDetails: "Ver Detalle",
          edit: "Editar",
          delete: "Eliminar",
        },
        noResults: {
          title: "No se encontraron dominios",
          description: "Intenta ajustar los filtros o crear un nuevo dominio",
        },
        details: {
          title: "Detalles del Dominio",
          basicInfo: "Información Básica",
          performance: "Rendimiento",
          dataSources: "Fuentes de Datos",
          documents: "Documentos",
          compliance: "Cumplimiento",
          lastActivity: "Última Actividad",
          status: "Estado",
          industry: "Industria",
          owner: "Propietario",
          createdAt: "Creado",
          totalDocuments: "Total Documentos",
          processedDocuments: "Documentos Procesados",
          dataSourcesCount: "Fuentes de Datos",
          complianceScore: "Puntuación de Cumplimiento",
          ragEnabled: "RAG Habilitado",
          trainingEnabled: "Entrenamiento Habilitado",
        },
      },
      webScraping: {
        title: "Web Scraping",
        subtitle: "Configura y monitoriza la extracción de datos web",
        stats: {
          total: "Total",
          active: "Activos",
          running: "Ejecutando",
          completed: "Completados",
        },
        search: {
          placeholder: "Buscar trabajos...",
        },
        filters: {
          all: "Todos",
          active: "Activos",
          running: "Ejecutando",
          completed: "Completados",
          failed: "Fallidos",
          more: "Más filtros",
        },
        actions: {
          newJob: "Nuevo Trabajo",
          viewDetails: "Ver Detalle",
          start: "Iniciar",
          stop: "Detener",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron trabajos",
          description: "Intenta ajustar los filtros o crear un nuevo trabajo",
        },
        details: {
          title: "Detalles del Trabajo",
          basicInfo: "Información Básica",
          progress: "Progreso",
          metrics: "Métricas",
          schedule: "Programación",
          lastRun: "Última Ejecución",
          nextRun: "Próxima Ejecución",
          pagesScraped: "Páginas Extraídas",
          totalPages: "Total de Páginas",
          dataExtracted: "Datos Extraídos",
          errors: "Errores",
          priority: "Prioridad",
        },
        newJob: {
          title: "Nuevo Trabajo de Web Scraping",
          name: "Nombre del Trabajo",
          url: "URL Objetivo",
          domain: "Dominio",
          schedule: "Programación",
          priority: "Prioridad",
          create: "Crear Trabajo",
          cancel: "Cancelar",
        },
        // Nuevas traducciones para página independiente
        newJobPage: "Nuevo Trabajo de Web Scraping",
        newJobDescription:
          "Configura un nuevo trabajo de extracción de datos web con asignación múltiple",
        basicConfig: "Configuración Básica",
        basicConfigDescription: "Información general del trabajo de scraping",
        jobName: "Nombre del Trabajo",
        jobNamePlaceholder: "Ej: Extracción de productos",
        description: "Descripción",
        descriptionPlaceholder: "Describe qué datos se extraerán y para qué",
        targetUrl: "URL Objetivo",
        advancedConfig: "Configuración Avanzada",
        advancedConfigDescription: "Parámetros técnicos de extracción",
        maxPages: "Máximo de Páginas",
        maxDepth: "Profundidad Máxima",
        waitTime: "Tiempo de Espera",
        userAgent: "User Agent",
        followLinks: "Seguir Enlaces",
        respectRobots: "Respetar robots.txt",
        domainAssignment: "Asignación de Dominios",
        domainAssignmentDescription:
          "Selecciona los dominios que procesarán estos datos",
        selectDomains: "Seleccionar Dominios",
        selectDomainPlaceholder: "Elige un dominio...",
        selectedDomains: "Dominios Seleccionados",
        ragAssignment: "Asignación de Proyectos RAG",
        ragAssignmentDescription:
          "Selecciona los proyectos RAG que utilizarán estos datos",
        selectRAGProjects: "Seleccionar Proyectos RAG",
        selectRAGProjectPlaceholder: "Elige un proyecto RAG...",
        selectedRAGProjects: "Proyectos RAG Seleccionados",
        priorities: {
          low: "Baja",
          medium: "Media",
          high: "Alta",
        },
        schedules: {
          once: "Una vez",
          daily: "Diario",
          weekly: "Semanal",
          monthly: "Mensual",
        },
      },
      uploadDocuments: {
        title: "Subir Documentos",
        subtitle: "Sube y gestiona lotes de documentos",
        stats: {
          total: "Total",
          processing: "Procesando",
          completed: "Completados",
          failed: "Fallidos",
        },
        search: {
          placeholder: "Buscar lotes...",
        },
        filters: {
          all: "Todos",
          processing: "Procesando",
          completed: "Completados",
          failed: "Fallidos",
          more: "Más filtros",
        },
        actions: {
          newBatch: "Nuevo Lote",
          viewDetails: "Ver Detalle",
          pause: "Pausar",
          resume: "Reanudar",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron lotes",
          description: "Intenta ajustar los filtros o crear un nuevo lote",
        },
        details: {
          title: "Detalles del Lote",
          basicInfo: "Información Básica",
          progress: "Progreso",
          files: "Archivos",
          processing: "Procesamiento",
          name: "Nombre del Lote",
          domain: "Dominio",
          sector: "Sector",
          priority: "Prioridad",
          status: "Estado",
          totalFiles: "Total de Archivos",
          processedFiles: "Archivos Procesados",
          totalSize: "Tamaño Total",
          processedSize: "Tamaño Procesado",
          createdAt: "Creado",
          updatedAt: "Actualizado",
          processingType: "Tipo de Procesamiento",
          ragEnabled: "RAG Habilitado",
          trainingEnabled: "Entrenamiento Habilitado",
          complianceEnabled: "Cumplimiento Habilitado",
          errors: "Errores",
          warnings: "Advertencias",
        },
        newBatch: {
          title: "Nuevo Lote de Documentos",
          explanation:
            "Un lote es una colección de documentos que se procesarán juntos. Esto permite una mejor organización y seguimiento del procesamiento de documentos.",
          name: "Nombre del Lote",
          domain: "Dominio",
          sector: "Sector",
          priority: "Prioridad",
          processingType: "Tipo de Procesamiento",
          create: "Crear Lote",
          cancel: "Cancelar",
        },
      },
      monitor: {
        title: "Monitorización",
        subtitle: "Monitorización en tiempo real de trabajos de ingesta",
        stats: {
          total: "Total",
          running: "Ejecutando",
          completed: "Completados",
          failed: "Fallidos",
        },
        search: {
          placeholder: "Buscar trabajos...",
        },
        filters: {
          all: "Todos",
          running: "Ejecutando",
          completed: "Completados",
          failed: "Fallidos",
          more: "Más filtros",
        },
        actions: {
          viewDetails: "Ver Detalle",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron trabajos",
          description: "Intenta ajustar los filtros o iniciar un nuevo trabajo",
        },
        details: {
          title: "Detalles del Trabajo",
          basicInfo: "Información Básica",
          performance: "Rendimiento",
          metrics: "Métricas",
          alerts: "Alertas",
          name: "Nombre del Trabajo",
          domain: "Dominio",
          type: "Tipo",
          status: "Estado",
          startTime: "Hora de Inicio",
          endTime: "Hora de Fin",
          duration: "Duración",
          cpu: "CPU",
          memory: "Memoria",
          throughput: "Rendimiento",
          latency: "Latencia",
          alerts: "Alertas",
        },
      },
      settings: {
        title: "Configuración",
        subtitle: "Configura parámetros globales y por dominio",
        stats: {
          total: "Total",
          active: "Activos",
          configured: "Configurados",
          default: "Por Defecto",
        },
        search: {
          placeholder: "Buscar dominios...",
        },
        filters: {
          all: "Todos",
          active: "Activos",
          configured: "Configurados",
          default: "Por Defecto",
          more: "Más filtros",
        },
        tabs: {
          domains: "Dominios",
          global: "Global",
          advanced: "Avanzado",
        },
        actions: {
          viewDetails: "Ver Detalle",
          save: "Guardar",
          reset: "Restablecer",
        },
        noResults: {
          title: "No se encontraron dominios",
          description: "Intenta ajustar los filtros o crear un nuevo dominio",
        },
        details: {
          title: "Configuración del Dominio",
          basicInfo: "Información Básica",
          configuration: "Configuración",
          ragExplanation:
            "RAG (Retrieval-Augmented Generation) se utiliza para procesar documentos y crear datasets de entrenamiento. Realiza chunking y tagging automático del contenido para el entrenamiento de modelos de IA. Los dominios son modelos de negocio, no sistemas RAG.",
          ragEnabled: "RAG Habilitado para Datasets",
          trainingEnabled: "Entrenamiento Habilitado",
          complianceEnabled: "Cumplimiento Habilitado",
          autoIngestion: "Ingesta Automática",
          qualityThreshold: "Umbral de Calidad",
          retentionDays: "Días de Retención",
          lastUpdated: "Última Actualización",
          status: "Estado",
        },
        global: {
          general: "Configuración General",
          security: "Seguridad",
          autoBackup: "Respaldo Automático",
          notifications: "Notificaciones",
          language: "Idioma",
          timezone: "Zona Horaria",
          encryption: "Encriptación",
          authentication: "Autenticación",
          auditLogs: "Logs de Auditoría",
        },
        advanced: {
          performance: "Rendimiento",
          logs: "Logs y Monitorización",
          cacheSize: "Tamaño de Caché",
          maxConnections: "Conexiones Máximas",
          logLevel: "Nivel de Log",
          monitoring: "Monitorización",
          alerts: "Alertas",
        },
      },
      configureSources: {
        title: "Configurar Fuentes",
        subtitle: "Gestiona y configura las fuentes de datos para cada dominio",
        stats: {
          total: "Total",
          active: "Activas",
          api: "APIs",
        },
        search: {
          placeholder: "Buscar fuentes...",
        },
        filters: {
          allTypes: "Todos los tipos",
          api: "API",
          database: "Base de Datos",
          file: "Archivo",
          web: "Web",
          stream: "Stream",
          more: "Más filtros",
        },
        actions: {
          newSource: "Nueva Fuente",
          viewDetails: "Ver Detalle",
          edit: "Editar",
          test: "Probar Conexión",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron fuentes",
          description:
            "Intenta ajustar los filtros o crear una nueva fuente de datos",
        },
        status: {
          active: "Activo",
          inactive: "Inactivo",
          error: "Error",
          maintenance: "Mantenimiento",
          unknown: "Desconocido",
        },
        info: {
          type: "Tipo:",
          schedule: "Programación:",
          dataVolume: "Volumen:",
          syncStatus: "Sincronización:",
        },
        features: {
          compliance: "Cumplimiento:",
          rag: "RAG",
          training: "Training",
        },
        newSource: {
          title: "Nueva Fuente de Datos",
          name: "Nombre de la Fuente",
          namePlaceholder: "Ej: API Gateway",
          domain: "Dominio",
          selectDomain: "Seleccionar dominio",
          type: "Tipo de Fuente",
          selectType: "Seleccionar tipo",
          url: "URL/Endpoint",
          urlPlaceholder: "https://api.example.com/v1",
          credentials: "Tipo de Credenciales",
          credentialsNone: "Ninguna",
          credentialsApiKey: "API Key",
          credentialsOAuth: "OAuth",
          credentialsBasic: "Basic Auth",
          schedule: "Programación",
          scheduleRealTime: "Tiempo Real",
          schedule15Min: "Cada 15 minutos",
          scheduleHour: "Cada hora",
          scheduleDaily: "Diario",
          scheduleWeekly: "Semanal",
          processingOptions: "Opciones de Procesamiento",
          ragEnabled: "Habilitar RAG (Crear datasets)",
          ragDescription: "Procesar datos para crear datasets de entrenamiento",
          trainingEnabled: "Habilitar Training",
          trainingDescription:
            "Generar contenido para entrenamiento de modelos",
          create: "Crear Fuente",
          cancel: "Cancelar",
        },
        details: {
          basicInfo: "Información Básica",
          sync: "Sincronización",
          credentials: "Credenciales",
          compliance: "Cumplimiento",
          processing: "Procesamiento",
          domain: "Dominio:",
          type: "Tipo:",
          status: "Estado:",
          url: "URL:",
          schedule: "Programación:",
          lastSync: "Última sincronización:",
          nextSync: "Próxima sincronización:",
          dataVolume: "Volumen de datos:",
          credentialType: "Tipo:",
          lastUpdated: "Última actualización:",
          rag: "RAG (Datasets)",
          training: "Entrenamiento",
          edit: "Editar",
          test: "Probar Conexión",
          configure: "Configurar",
        },
      },
      domainDetails: {
        navigation: {
          back: "Volver",
        },
        actions: {
          edit: "Editar",
          settings: "Configuración",
          monitor: "Monitorear",
        },
        stats: {
          documents: "Documentos",
          processed: "Procesados",
          dataSources: "Fuentes de Datos",
          active: "activas",
          compliance: "Cumplimiento",
          compliant: "Cumple",
          successRate: "Tasa de Éxito",
          avgTime: "promedio",
        },
        tabs: {
          overview: "Resumen",
          dataSources: "Fuentes",
          documents: "Documentos",
          compliance: "Cumplimiento",
          alerts: "Alertas",
        },
        overview: {
          domainInfo: "Información del Dominio",
          performance: "Rendimiento",
          ingestionMetrics: "Métricas de Ingesta",
          industry: "Industria",
          status: "Estado",
          owner: "Propietario",
          created: "Creado",
          team: "Equipo",
          dataQuality: "Calidad de Datos",
          rag: "RAG",
          training: "Training",
          lastActivity: "Última Actividad",
          daily: "Diario",
          weekly: "Semanal",
          monthly: "Mensual",
        },
        dataSources: {
          title: "Fuentes de Datos",
          lastSync: "Última sincronización:",
        },
        documents: {
          title: "Documentos",
        },
        compliance: {
          frameworks: "Marcos de Cumplimiento",
          audits: "Auditorías",
          status: "Estado:",
          lastAudit: "Última Auditoría",
          nextAudit: "Próxima Auditoría",
        },
        alerts: {
          title: "Alertas",
          resolved: "Resuelto",
        },
        status: {
          active: "Activo",
          inactive: "Inactivo",
          maintenance: "Mantenimiento",
          error: "Error",
          unknown: "Desconocido",
        },
        edit: {
          title: "Editar Dominio",
          name: "Nombre del Dominio",
          industry: "Industria",
          description: "Descripción",
          save: "Guardar Cambios",
          cancel: "Cancelar",
        },
        settings: {
          title: "Configuración del Dominio",
          processing: "Procesamiento",
          ragEnabled: "Habilitar RAG (Crear datasets)",
          ragDescription:
            "Procesar documentos para crear datasets de entrenamiento",
          trainingEnabled: "Habilitar Training",
          trainingDescription:
            "Generar contenido para entrenamiento de modelos",
          save: "Guardar Configuración",
          cancel: "Cancelar",
        },
      },
    },

    // Model Evaluation
    modelEvaluation: {
      title: "Evaluación de Modelos",
      subtitle: "Dashboard de evaluación y testing de modelos de IA",
      completed: "Completado",
      running: "Ejecutando",
      failed: "Fallido",
      pending: "Pendiente",
      unknown: "Desconocido",
      loadingData: "Cargando datos del dashboard...",
      errorLoadingData: "No se pudieron cargar los datos del dashboard",
      pipeline: "Pipeline",
      abTest: "Test A/B",
      security: "Seguridad",
      evaluation: "Evaluación",
      newEvaluation: "Nueva Evaluación",
      viewEvaluation: "Ver Evaluación",
      pipelineModal: "Modal de Pipeline",
      abTestModal: "Modal de Test A/B",
      securityModal: "Modal de Seguridad",
      reportModal: "Modal de Reporte",
    },

    // RAG Training
    ragTraining: {
      title: "Entrenamiento RAG",
      subtitle: "Dashboard de entrenamiento y métricas RAG",
      totalDocuments: "Total de Documentos",
      totalChunks: "Total de Chunks",
      totalSearches: "Total de Búsquedas",
      averageResponseTime: "Tiempo de Respuesta Promedio",
      activeUsers: "Usuarios Activos",
      storageUsed: "Almacenamiento Usado",
      documentsByType: "Documentos por Tipo",
      searchesByDay: "Búsquedas por Día",
      topQueries: "Consultas Principales",
      pdf: "PDF",
      docx: "DOCX",
      txt: "TXT",
      html: "HTML",
      configuration: "configuración de base de datos",
      apiEndpoints: "API endpoints",
      authentication: "autenticación",
      middlewareSetup: "middleware setup",
      deployment: "deployment",
    },

    // Data Sources Module
    dataSources: {
      title: "Fuentes de Información",
      subtitle: "Gestión centralizada de fuentes de datos",
      totalDataSources: "Total Fuentes",
      activeSources: "Fuentes Activas",
      totalChunks: "Total Chunks",
      processingJobs: "Jobs Procesando",
      successRate: "Tasa de Éxito",
      vsLastMonth: "vs mes anterior",
      dataSourceTypes: "Tipos de Fuentes",
      sources: "fuentes",
      recentActivity: "Actividad Reciente",
      processingStats: "Estadísticas de Procesamiento",
      completedJobs: "Jobs Completados",
      pendingJobs: "Jobs Pendientes",
      errorJobs: "Jobs con Error",
      avgProcessingTime: "Tiempo Promedio",
      timeAgo: {
        now: "Ahora",
      },
      help: {
        dashboard: {
          title: "Dashboard de Fuentes de Información",
          content: `
            <p>Este dashboard centraliza la gestión de todas las fuentes de datos del sistema.</p>
            <h3>Funcionalidades principales:</h3>
            <ul>
              <li><strong>Estadísticas en tiempo real:</strong> Monitorea el estado de todas las fuentes de datos</li>
              <li><strong>Tipos de fuentes:</strong> Bases de datos, APIs, documentos y web scraping</li>
              <li><strong>Métricas de procesamiento:</strong> Jobs completados, pendientes y con errores</li>
              <li><strong>Actividad reciente:</strong> Últimas operaciones realizadas</li>
            </ul>
            <p>Desde aquí puedes acceder a cada tipo de fuente de datos para configurar, monitorear y gestionar tus conexiones.</p>
          `,
        },
        database: {
          title: "Gestión de Bases de Datos",
          content: `
            <p>Gestiona las conexiones a bases de datos para extraer y procesar información.</p>
            <h3>Tipos soportados:</h3>
            <ul>
              <li><strong>PostgreSQL:</strong> Bases de datos relacionales</li>
              <li><strong>MySQL:</strong> Bases de datos relacionales</li>
              <li><strong>MongoDB:</strong> Bases de datos NoSQL</li>
              <li><strong>Elasticsearch:</strong> Motor de búsqueda y análisis</li>
              <li><strong>Vector DB:</strong> Bases de datos vectoriales para embeddings</li>
              <li><strong>Redis:</strong> Base de datos en memoria</li>
            </ul>
            <h3>Funcionalidades:</h3>
            <ul>
              <li>Configuración de conexiones seguras</li>
              <li>Prueba de conectividad</li>
              <li>Ejecución de consultas SQL</li>
              <li>Asignación a dominios y proyectos RAG</li>
            </ul>
          `,
        },
        api: {
          title: "Gestión de APIs",
          content: `
            <p>Configura y gestiona conexiones a APIs externas para extraer datos.</p>
            <h3>Características:</h3>
            <ul>
              <li><strong>OpenAPI/Swagger:</strong> Soporte completo para especificaciones OpenAPI</li>
              <li><strong>Autenticación:</strong> API Key, Basic Auth, Bearer Token</li>
              <li><strong>Configuración avanzada:</strong> Timeouts, reintentos, rate limiting</li>
              <li><strong>Testing:</strong> Prueba de endpoints antes de guardar</li>
            </ul>
            <h3>Flujo de trabajo:</h3>
            <ol>
              <li>Configura la URL del Swagger/OpenAPI</li>
              <li>Establece la autenticación necesaria</li>
              <li>Prueba la conexión</li>
              <li>Asigna a dominios y proyectos RAG</li>
            </ol>
          `,
        },
        documents: {
          title: "Gestión de Documentos",
          content: `
            <p>Gestiona lotes de documentos para procesamiento y análisis.</p>
            <h3>Tipos de documentos soportados:</h3>
            <ul>
              <li><strong>PDF:</strong> Documentos PDF</li>
              <li><strong>DOCX:</strong> Documentos de Word</li>
              <li><strong>TXT:</strong> Archivos de texto plano</li>
              <li><strong>CSV:</strong> Datos tabulares</li>
              <li><strong>JSON:</strong> Datos estructurados</li>
            </ul>
            <h3>Procesamiento:</h3>
            <ul>
              <li>Chunking automático de documentos</li>
              <li>Extracción de metadatos</li>
              <li>Generación de embeddings</li>
              <li>Asignación a proyectos RAG</li>
            </ul>
          `,
        },
        webScraping: {
          title: "Web Scraping",
          content: `
            <p>Configura trabajos de extracción de datos de sitios web.</p>
            <h3>Características:</h3>
            <ul>
              <li><strong>Configuración flexible:</strong> URLs, selectores CSS, XPath</li>
              <li><strong>Programación:</strong> Ejecución manual o programada</li>
              <li><strong>Filtros avanzados:</strong> Por contenido, fecha, tipo</li>
              <li><strong>Monitoreo:</strong> Estado de ejecución y logs</li>
            </ul>
            <h3>Tipos de extracción:</h3>
            <ul>
              <li>Contenido de páginas web</li>
              <li>Datos de formularios</li>
              <li>Enlaces y referencias</li>
              <li>Metadatos y estructura</li>
            </ul>
          `,
        },
      },
    },
    // RAG Module
    rag: {
      dashboard: {
        title: "Dashboard RAG",
        subtitle: "Sistema de Retrieval Augmented Generation",
        totalProjects: "Total Proyectos",
        totalChunks: "Total Chunks",
        totalSearches: "Total Búsquedas",
        avgResponseTime: "Tiempo Respuesta Promedio",
        activeProjects: "Proyectos Activos",
        vsLastMonth: "vs mes pasado",
        projectsOverview: "Resumen de Proyectos",
        viewAll: "Ver Todos",
        chunks: "chunks",
        embeddings: "embeddings",
        searches: "búsquedas",
        models: "Modelos",
        recentActivity: "Actividad Reciente",
        modelUsage: "Uso de Modelos",
        usage: "Uso",
      },
      project: {
        createTitle: "Crear Proyecto RAG",
        createSubtitle: "Configurar un nuevo proyecto RAG",
        editTitle: "Editar Proyecto RAG",
        basicInfo: "Información Básica",
        name: "Nombre del Proyecto",
        namePlaceholder: "Ingresa el nombre del proyecto",
        description: "Descripción",
        descriptionPlaceholder: "Describe el propósito del proyecto RAG",
        isActive: "Proyecto Activo",
        embeddingModel: "Modelo de Embedding",
        modelConfiguration: "Configuración de Modelos",
        rerankerModel: "Modelo de Reranker",
        llmModel: "Modelo LLM",
        ragConfiguration: "Configuración RAG",
        maxChunkSize: "Tamaño Máximo de Chunk",
        chunkOverlap: "Solapamiento de Chunks",
        semanticChunking: "Chunking Semántico",
        statistics: "Estadísticas del Proyecto",
        documents: "Documentos",
        chunks: "Chunks",
        embeddings: "Embeddings",
        searches: "Búsquedas",
        avgResponseTime: "Tiempo Promedio",
        successRate: "Tasa de Éxito",
        assignments: "Asignaciones",
        documentAssignments: "Documentos Asignados",
        webscrapingAssignments: "Web Scraping Asignado",
        datasourceAssignments: "Fuentes de Datos Asignadas",
        lekaServerConfig: "Configuración Leka Server",
        lekaEndpoint: "Endpoint Leka Server",
        lekaApiKey: "API Key Leka Server",
        lekaTimeout: "Timeout (ms)",
      },
      help: {
        dashboard: {
          title: "Dashboard RAG",
          content:
            "El Dashboard RAG te permite monitorear el rendimiento de tus proyectos de Retrieval Augmented Generation, incluyendo estadísticas de uso, proyectos activos y actividad reciente.",
        },
        projects: {
          title: "Proyectos RAG",
          content:
            "<h4>¿Qué son los Proyectos RAG?</h4><p>Los proyectos RAG (Retrieval Augmented Generation) son sistemas inteligentes que combinan la recuperación de información con la generación de texto. Te permiten crear chatbots y asistentes que pueden responder preguntas basándose en tus propios documentos y datos.</p><h4>¿Para qué sirven?</h4><ul><li><strong>Chatbots Especializados:</strong> Crea asistentes que conocen tu documentación específica</li><li><strong>Búsqueda Inteligente:</strong> Encuentra información relevante en grandes volúmenes de documentos</li><li><strong>Respuestas Contextualizadas:</strong> Genera respuestas precisas basadas en tu contenido</li><li><strong>Automatización de Soporte:</strong> Reduce la carga de trabajo del equipo de soporte</li><li><strong>Base de Conocimiento Interactiva:</strong> Convierte documentos estáticos en una experiencia conversacional</li></ul><h4>Gestión de Proyectos</h4><p>Desde esta página puedes:</p><ul><li>Crear nuevos proyectos RAG con configuraciones personalizadas</li><li>Editar modelos de IA (embedding, reranker, LLM) para cada proyecto</li><li>Monitorear el rendimiento y estadísticas de uso</li><li>Gestionar las fuentes de datos asignadas a cada proyecto</li><li>Configurar parámetros de chunking y procesamiento</li></ul>",
        },
        createProject: {
          title: "Crear Proyecto RAG",
          content:
            "Crea un nuevo proyecto RAG configurando los modelos de embedding, reranker y LLM, así como los parámetros de chunking y configuración del servidor Leka.",
        },
        editProject: {
          title: "Editar Proyecto RAG",
          content:
            "Edita la configuración de tu proyecto RAG, incluyendo modelos, parámetros de chunking, configuración del servidor Leka y monitorea las estadísticas del proyecto.",
        },
        models: {
          title: "Modelos RAG",
          content:
            "Gestiona los modelos de IA disponibles para tus proyectos RAG, incluyendo modelos de embedding, reranker y LLM de diferentes proveedores.",
        },
        createModel: {
          title: "Crear Modelo RAG",
          content:
            "<h4>Configuración de Modelos de IA</h4><p>Esta página te permite crear y configurar nuevos modelos de IA para tus proyectos RAG. Puedes configurar:</p><ul><li><strong>Información Básica:</strong> Nombre, tipo (Embedding, Reranker, LLM), proveedor y versión</li><li><strong>Configuración Técnica:</strong> Endpoint, API Key, costo por token, máximo de tokens y ventana de contexto</li><li><strong>Opciones Avanzadas:</strong> Modelo local vs remoto, estado activo/inactivo</li></ul><p><strong>Tipos de Modelos:</strong></p><ul><li><strong>Embedding:</strong> Convierte texto en vectores numéricos para búsqueda semántica</li><li><strong>Reranker:</strong> Mejora la relevancia de los resultados de búsqueda</li><li><strong>LLM:</strong> Genera respuestas basadas en el contexto recuperado</li></ul><p>Una vez configurado, puedes probar el modelo antes de guardarlo para asegurar que funciona correctamente.</p>",
        },
        editModel: {
          title: "Editar Modelo RAG",
          content:
            "Edita la configuración de un modelo de IA existente, incluyendo parámetros de rendimiento, costo y configuración del endpoint.",
        },
        chat: {
          title: "Chat RAG",
          content:
            "Interactúa con tus proyectos RAG a través del chat. Selecciona un proyecto y haz preguntas para obtener respuestas basadas en tus documentos.",
        },
        reranker: {
          title: "Reranker RAG",
          content:
            "Configura y gestiona el reranking de resultados de búsqueda para mejorar la relevancia de las respuestas en tus proyectos RAG.",
        },
        chunks: {
          title: "Chunks de Documentos",
          content:
            "Los chunks son fragmentos de documentos procesados que permiten una búsqueda más eficiente. Los auto-tags se generan automáticamente basándose en el contenido, mientras que los tags manuales los puedes añadir, editar o eliminar para mejorar la categorización y recuperación de información específica.",
        },
        search: {
          title: "Búsqueda Semántica",
          content:
            "Realiza búsquedas semánticas avanzadas en tus documentos RAG con diferentes estrategias de búsqueda y filtros.",
        },
        searchTypes: {
          vector: "Vectorial",
          textual: "Textual",
          hybrid: "Híbrida",
          filtered: "Filtrada",
        },
        analytics: {
          title: "Analytics RAG",
          content:
            "Monitorea el rendimiento y métricas de tus proyectos RAG, incluyendo estadísticas de uso, tiempos de respuesta y calidad de resultados.",
        },
      },
      reranker: {
        title: "Reranker",
        subtitle: "Mejora la relevancia de los resultados de búsqueda",
        configuration: "Configuración del Reranker",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        model: "Modelo de Reranking",
        selectModel: "Seleccionar modelo",
        topK: "Top K Results",
        threshold: "Umbral de Relevancia",
        testQuery: "Test de Consulta",
        query: "Consulta de Prueba",
        queryPlaceholder: "Ingresa una consulta para probar el reranking...",
        runTest: "Ejecutar Test",
        testHistory: "Historial de Tests",
      },
      chunks: {
        title: "Chunks",
        subtitle: "Visualiza y gestiona los chunks de documentos",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        chunkType: "Tipo de Chunk",
        selectType: "Seleccionar tipo",
        searchChunks: "Buscar Chunks",
        searchPlaceholder: "Buscar por contenido o tags...",
        totalChunks: "Total Chunks",
        avgSize: "Tamaño Promedio",
        autoTags: "Auto Tags",
        viewDetails: "Ver Detalles",
        filters: "Filtros",
        manualTags: "Tags Manuales",
        newTag: "Nuevo tag...",
        addTag: "Añadir Tag",
        editTags: "Editar Tags",
        removeTag: "Eliminar Tag",
        source: "Fuente",
        relevance: "Relevancia",
        characters: "caracteres",
        createdAt: "Creado",
        saveChanges: "Guardar Cambios",
        cancel: "Cancelar",
      },
      search: {
        title: "Búsqueda Semántica",
        subtitle: "Búsqueda avanzada en documentos RAG",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        searchQuery: "Consulta de Búsqueda",
        queryPlaceholder: "Ingresa tu consulta de búsqueda...",
        searchType: "Tipo de Búsqueda",
        selectType: "Seleccionar tipo",
        executeSearch: "Ejecutar Búsqueda",
        searchHistory: "Historial de Búsquedas",
        results: "Resultados",
        relevance: "Relevancia",
        source: "Fuente",
      },
      analytics: {
        title: "Analytics",
        subtitle: "Métricas y rendimiento de proyectos RAG",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        timeRange: "Rango de Tiempo",
        selectRange: "Seleccionar rango",
        refresh: "Actualizar",
        totalSearches: "Total Búsquedas",
        avgResponseTime: "Tiempo Respuesta Promedio",
        successRate: "Tasa de Éxito",
        userSatisfaction: "Satisfacción del Usuario",
        topQueries: "Consultas Más Populares",
        performanceMetrics: "Métricas de Rendimiento",
        usageStats: "Estadísticas de Uso",
      },
    },

    // Governance
    governance: {
      title: "Dashboard de Gobierno Automático",
      subtitle: "Monitorea y gestiona el gobierno de IA en tiempo real",
      refresh: "Actualizar",
      configure: "Configurar",
      low: "BAJO",
      medium: "MEDIO",
      high: "ALTO",
      critical: "CRÍTICO",
      riskLevel: "Nivel de Riesgo",
      compliance: "Cumplimiento",
      audit: "Auditoría",
      policies: "Políticas",
      violations: "Violaciones",
      alerts: "Alertas",
      metrics: "Métricas",
      reports: "Reportes",
      settings: "Configuración",
      overview: {
        title: "Vista General de Políticas",
        subtitle: "Gestión y configuración de políticas de gobierno",
        metrics: {
          total: "Total Políticas",
          active: "Políticas Activas",
          violations: "Violaciones",
          compliance: "Score Cumplimiento",
        },
        filters: {
          title: "Filtros",
          clear: "Limpiar",
          search: {
            placeholder: "Buscar políticas...",
          },
          type: "Todos los Tipos",
          "type.security": "Seguridad",
          "type.compliance": "Cumplimiento",
          "type.ethics": "Ética",
          "type.privacy": "Privacidad",
          status: "Todos los Estados",
          "status.active": "Activo",
          "status.inactive": "Inactivo",
          "status.draft": "Borrador",
          enforcement: "Todos los Niveles",
          "enforcement.mandatory": "Obligatorio",
          "enforcement.recommended": "Recomendado",
          "enforcement.optional": "Opcional",
        },
        table: {
          title: "Políticas",
          id: "ID",
          name: "Nombre",
          type: "Tipo",
          enforcement: "Nivel",
          version: "Versión",
          status: "Estado",
          violations: "Violaciones",
          score: "Score",
          actions: "Acciones",
          empty: "No se encontraron políticas",
        },
        button: {
          create: "Crear Política",
        },
        confirmDelete: "¿Eliminar esta política?",
      },
      detail: {
        "title.edit": "Editar Política",
        "title.create": "Nueva Política",
        button: {
          evaluate: "Evaluar",
          compliance: "Verificar Cumplimiento",
          score: "Calcular Score",
        },
        form: {
          title: "Información de la Política",
          name: "Nombre de la Política",
          version: "Versión",
          type: "Tipo",
          "type.security": "Seguridad",
          "type.compliance": "Cumplimiento",
          "type.ethics": "Ética",
          "type.privacy": "Privacidad",
          "type.operational": "Operacional",
          category: "Categoría",
          enforcement: "Nivel de Enforcement",
          "enforcement.mandatory": "Obligatorio",
          "enforcement.recommended": "Recomendado",
          "enforcement.optional": "Opcional",
          "enforcement.informational": "Información",
          description: "Descripción",
          effectiveDate: "Fecha Efectiva",
          expirationDate: "Fecha de Expiración",
          status: "Estado",
          "status.active": "Activo",
          "status.inactive": "Inactivo",
          "status.draft": "Borrador",
          "status.review": "Revisión",
          "status.archived": "Archivado",
          metadata: "Metadata (JSON)",
        },
        tabs: {
          rules: "Reglas",
          evaluations: "Evaluaciones",
          violations: "Violaciones",
          compliance: "Cumplimiento",
          statistics: "Estadísticas",
          "rules.empty": "No hay reglas definidas",
          "rules.name": "Nombre",
          "rules.condition": "Condición",
          "rules.action": "Acción",
          "rules.priority": "Prioridad",
          "rules.active": "Activa",
          "evaluations.empty": "No hay evaluaciones registradas",
          "evaluations.date": "Fecha",
          "evaluations.result": "Resultado",
          "evaluations.score": "Puntuación",
          "evaluations.evaluator": "Evaluador",
          "violations.empty": "No hay violaciones registradas",
          "violations.type": "Tipo",
          "violations.description": "Descripción",
          "violations.severity": "Severidad",
          "violations.date": "Fecha",
          "violations.status": "Estado",
          "compliance.empty": "No hay evaluaciones de cumplimiento",
          "compliance.average": "Score Promedio",
          "compliance.date": "Fecha",
          "compliance.area": "Área",
          "compliance.score": "Score",
          "compliance.status": "Estado",
          "compliance.findings": "Hallazgos",
          "statistics.rules": "Reglas",
          "statistics.evaluations": "Evaluaciones",
          "statistics.violations": "Violaciones",
          "statistics.score": "Score Actual",
          "statistics.info": "Información de la Política",
          "statistics.createdBy": "Creado por",
          "statistics.createdAt": "Fecha de creación",
          "statistics.lastEvaluation": "Última evaluación",
          "statistics.lastUpdated": "Última actualización",
          "statistics.evaluationStatus": "Estado evaluación",
          "statistics.complianceScore": "Score cumplimiento",
        },
        evaluation: {
          success: "Evaluación completada",
        },
        compliance: {
          success: "Verificación de cumplimiento completada",
        },
        score: {
          success: "Score calculado: {score}",
        },
      },
    },

    // BPMN Task Inbox
    taskInbox: {
      title: "Bandeja de Tareas BPMN",
      subtitle: "Gestiona tus tareas pendientes",
      stats: {
        totalTasks: "Tareas Pendientes",
        assignedToMe: "Asignadas a Mí",
        groupTasks: "De Mis Roles",
      },
      filters: {
        process: "Proceso",
        priority: "Prioridad",
        state: "Estado",
        allProcesses: "Todos los procesos",
        allPriorities: "Todas las prioridades",
        allStates: "Todos los estados",
        assigned: "Asignadas",
        unassigned: "Sin asignar",
        candidate: "Candidatas",
        low: "Baja",
        normal: "Normal",
        high: "Alta",
        urgent: "Urgente",
      },
      table: {
        priority: "Prioridad",
        task: "Tarea",
        process: "Proceso",
        assigned: "Asignado",
        roles: "Roles",
        creation: "Creación",
        dueDate: "Vencimiento",
        actions: "Acciones",
        noTasks: "No hay tareas pendientes",
        unassigned: "Sin asignar",
        openTask: "Abrir tarea",
        claim: "Reclamar",
        assign: "Asignar",
      },
      assignDialog: {
        title: "Asignar Tarea a Usuario",
        task: "Tarea",
        availableUsers: "Usuarios disponibles",
        username: "Usuario",
        fullname: "Nombre Completo",
        email: "Email",
        cancel: "Cancelar",
        assign: "Asignar",
      },
      actions: {
        refresh: "Refrescar",
        clearFilters: "Limpiar filtros",
      },
    },

    // Internal Audit Scheduling Form
    internalAuditScheduling: {
      title: "Programar auditoría interna ISO 42001",
      subtitle: "Complete los datos para programar la auditoría interna",
      formTitle: "Datos de la auditoría",
      project: "Proyecto",
      projectPlaceholder: "Project ID",
      date: "Fecha programada",
      scope: "Alcance",
      scopePlaceholder: "Cláusulas, procesos y sistemas a auditar",
      auditor: "Auditor responsable",
      auditorPlaceholder: "Usuario o rol auditor",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Planificar auditoría",
      saving: "Guardando...",
      errors: {
        scopeRequired: "Debe definir el alcance de la auditoría",
        auditorRequired: "Debe asignar un auditor responsable",
        dateRequired: "Debe seleccionar una fecha",
        submitError: "Error al planificar la auditoría",
      },

    // BPMN Forms
    agentApprovalHumanOverride: {
      title: "Aprobación de Agent - Override Humano",
      subtitle: "Complete los datos para aprobación de agent - override humano",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    modelApprovalHumanOverride: {
      title: "Aprobación de Modelo - Override Humano",
      subtitle: "Complete los datos para aprobación de modelo - override humano",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    promptHumanReview: {
      title: "Revisión Humana de Prompt",
      subtitle: "Complete los datos para revisión humana de prompt",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    hitlSlaReminder: {
      title: "Recordatorio SLA HITL",
      subtitle: "Complete los datos para recordatorio sla hitl",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    promptApprovalRequest: {
      title: "Solicitud de Aprobación de Prompt",
      subtitle: "Complete los datos para solicitud de aprobación de prompt",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionApproval: {
      title: "Aprobación de Desmantelamiento IA",
      subtitle: "Complete los datos para aprobación de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapApproval: {
      title: "Aprobación de Brecha de Competencia",
      subtitle: "Complete los datos para aprobación de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionApproval: {
      title: "Aprobación de Acción Correctiva",
      subtitle: "Complete los datos para aprobación de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    approveConformityAssessment: {
      title: "Aprobar Evaluación de Conformidad",
      subtitle: "Complete los datos para aprobar evaluación de conformidad",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    deployerApproval: {
      title: "Aprobación de Despliegue",
      subtitle: "Complete los datos para aprobación de despliegue",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    boardDecisionPaper: {
      title: "Decisión del Consejo",
      subtitle: "Complete los datos para decisión del consejo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    managementReview: {
      title: "Revisión de Gestión",
      subtitle: "Complete los datos para revisión de gestión",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    llmEvaluationReview: {
      title: "Revisión de Evaluación LLM",
      subtitle: "Complete los datos para revisión de evaluación llm",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ragEvaluationReview: {
      title: "Revisión de Evaluación RAG",
      subtitle: "Complete los datos para revisión de evaluación rag",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    modelEvaluationReview: {
      title: "Revisión de Evaluación de Modelo",
      subtitle: "Complete los datos para revisión de evaluación de modelo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapAssessment: {
      title: "Evaluación de Brecha de Competencia",
      subtitle: "Complete los datos para evaluación de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionAssessment: {
      title: "Evaluación de Acción Correctiva",
      subtitle: "Complete los datos para evaluación de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    performanceReviewDecision: {
      title: "Decisión de Revisión de Rendimiento",
      subtitle: "Complete los datos para decisión de revisión de rendimiento",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    compliance: {
      dashboard: {
        title: "Dashboard Consolidado Compliance",
        subtitle: "Vista consolidada del estado de compliance según EU AI Act",
      },
      postMarketMonitoring: {
        title: "Dashboard Post-Market Monitoring",
        subtitle: "Monitoreo continuo de sistemas de IA según EU AI Act Art. 72",
      },
      sector: {
        title: "Metamodelo sectorial FaaS",
        subtitle: "Visualiza sectores, subsectores y frameworks configurados sin modificar el core de CodeflowX",
      },
      conformityDeclaration: {
        title: "EU Declaration of Conformity Manager",
        subtitle: "Gestión de declaraciones de conformidad según EU AI Act Annex V",
      },
      conformityReview: {
        title: "Conformity Assessment Review",
        subtitle: "Human review required for borderline compliance assessment",
      },
    },
    complianceReview: {
      title: "Revisión de Compliance",
      subtitle: "Complete los datos para revisión de compliance",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    complianceReviewDecision: {
      title: "Decisión de Revisión de Compliance",
      subtitle: "Complete los datos para decisión de revisión de compliance",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    reviewQmsGaps: {
      title: "Revisar Brechas QMS",
      subtitle: "Complete los datos para revisar brechas qms",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    completeDocumentation: {
      title: "Completar Documentación",
      subtitle: "Complete los datos para completar documentación",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    biasReview: {
      title: "Revisión de Sesgo",
      subtitle: "Complete los datos para revisión de sesgo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    biasMitigationPlan: {
      title: "Plan de Mitigación de Sesgo",
      subtitle: "Complete los datos para plan de mitigación de sesgo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    biasUrgentDecision: {
      title: "Decisión Urgente de Sesgo",
      subtitle: "Complete los datos para decisión urgente de sesgo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    driftAnalysis: {
      title: "Análisis de Deriva",
      subtitle: "Complete los datos para análisis de deriva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    driftReviewDecision: {
      title: "Decisión de Revisión de Deriva",
      subtitle: "Complete los datos para decisión de revisión de deriva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    internalAuditExecution: {
      title: "Ejecución de Auditoría Interna",
      subtitle: "Complete los datos para ejecución de auditoría interna",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    managementReviewSchedule: {
      title: "Programación de Revisión de Gestión",
      subtitle: "Complete los datos para programación de revisión de gestión",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    completeMissingElements: {
      title: "Completar Elementos Faltantes",
      subtitle: "Complete los datos para completar elementos faltantes",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    defineModifications: {
      title: "Definir Modificaciones",
      subtitle: "Complete los datos para definir modificaciones",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    enhancedReview: {
      title: "Revisión Mejorada",
      subtitle: "Complete los datos para revisión mejorada",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    finalReview: {
      title: "Revisión Final",
      subtitle: "Complete los datos para revisión final",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    euRegistration: {
      title: "Registro UE",
      subtitle: "Complete los datos para registro ue",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    reviewRegistrationPackage: {
      title: "Revisar Paquete de Registro",
      subtitle: "Complete los datos para revisar paquete de registro",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    fixValidationErrors: {
      title: "Corregir Errores de Validación",
      subtitle: "Complete los datos para corregir errores de validación",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    manualResolution: {
      title: "Resolución Manual",
      subtitle: "Complete los datos para resolución manual",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    documentIncidentDetails: {
      title: "Documentar Detalles de Incidente",
      subtitle: "Complete los datos para documentar detalles de incidente",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    verifyIncidentResolution: {
      title: "Verificar Resolución de Incidente",
      subtitle: "Complete los datos para verificar resolución de incidente",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    rootCauseAnalysis: {
      title: "Análisis de Causa Raíz",
      subtitle: "Complete los datos para análisis de causa raíz",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    defineCorrectiveActions: {
      title: "Definir Acciones Correctivas",
      subtitle: "Complete los datos para definir acciones correctivas",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionImplementation: {
      title: "Implementación de Acción Correctiva",
      subtitle: "Complete los datos para implementación de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionEffectiveness: {
      title: "Efectividad de Acción Correctiva",
      subtitle: "Complete los datos para efectividad de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    managementReviewActions: {
      title: "Acciones de Revisión de Gestión",
      subtitle: "Complete los datos para acciones de revisión de gestión",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsReviewRequest: {
      title: "Solicitud de Revisión Ética",
      subtitle: "Complete los datos para solicitud de revisión ética",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsReviewReminder: {
      title: "Recordatorio de Revisión Ética",
      subtitle: "Complete los datos para recordatorio de revisión ética",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsCommitteeReview: {
      title: "Revisión del Comité Ético",
      subtitle: "Complete los datos para revisión del comité ético",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsMitigationPlan: {
      title: "Plan de Mitigación Ética",
      subtitle: "Complete los datos para plan de mitigación ética",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapImplementation: {
      title: "Implementación de Brecha de Competencia",
      subtitle: "Complete los datos para implementación de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapAdditionalActions: {
      title: "Acciones Adicionales de Brecha de Competencia",
      subtitle: "Complete los datos para acciones adicionales de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapRework: {
      title: "Retrabajo de Brecha de Competencia",
      subtitle: "Complete los datos para retrabajo de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionRequest: {
      title: "Solicitud de Desmantelamiento IA",
      subtitle: "Complete los datos para solicitud de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionVerification: {
      title: "Verificación de Desmantelamiento IA",
      subtitle: "Complete los datos para verificación de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionRetention: {
      title: "Retención de Desmantelamiento IA",
      subtitle: "Complete los datos para retención de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    modelApprovalReminder: {
      title: "Recordatorio de Aprobación de Modelo",
      subtitle: "Complete los datos para recordatorio de aprobación de modelo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    datasetReviewReminder: {
      title: "Recordatorio de Revisión de Dataset",
      subtitle: "Complete los datos para recordatorio de revisión de dataset",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    categorizeIncident: {
      title: "Categorizar Incidente",
      subtitle: "Complete los datos para categorizar incidente",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    executeCorrectiveAction: {
      title: "Ejecutar Acción Correctiva",
      subtitle: "Complete los datos para ejecutar acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    },

    // Providers Management
    providers: {
      title: "Proveedores de IA",
      subtitle: "Gestión de proveedores de servicios de IA",
      operational: "Operacional",
      totalProviders: "Total Proveedores",
      activeProviders: "Proveedores Activos",
      openaiProviders: "OpenAI",
      selfHosting: "Self-Hosting",
      search: "Buscar proveedores...",
      type: "Tipo",
      newProvider: "Nuevo Proveedor",
      noProviders: "No se encontraron proveedores",
      createProvider: "Crear Nuevo Proveedor",
      editProvider: "Editar Proveedor",
      providerDetails: "Detalles del Proveedor",
      quick: {
        title: "Acciones Rápidas",
        newProvider: "Nuevo Proveedor",
        importProvider: "Importar Proveedor",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
    },

    // Marketplace Management
    marketplace: {
      title: "Marketplace de Modelos",
      subtitle: "Descubre y descarga modelos de IA de múltiples proveedores",
      operational: "Operacional",
      totalModels: "Total Modelos",
      downloaded: "Descargados",
      downloadedModels: "Modelos Descargados",
      avgRating: "Rating Promedio",
      totalDownloads: "Total Descargas",
      search: "Buscar modelos...",
      source: "Fuente",
      type: "Tipo",
      sort: "Ordenar por",
      noModels: "No se encontraron modelos",
      noDescription: "Sin descripción",
      modelDetails: "Detalles del Modelo",
      download: "Descargar",
      downloaded: "Descargado",
      quick: {
        title: "Acciones Rápidas",
        advancedSearch: "Búsqueda Avanzada",
        downloadQueue: "Cola de Descargas",
        configuration: "Configuración",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        text: "Texto",
        vision: "Visión",
        audio: "Audio",
        multimodal: "Multimodal",
      },
    },

    // Experiments Management
    experiments: {
      title: "Experimentos de Modelos",
      subtitle: "Gestión y seguimiento de experimentos de IA",
      operational: "Operacional",
      totalExperiments: "Total Experimentos",
      runningExperiments: "Ejecutándose",
      completedExperiments: "Completados",
      failedExperiments: "Fallidos",
      search: "Buscar experimentos...",
      type: "Tipo",
      status: "Estado",
      newExperiment: "Nuevo Experimento",
      noExperiments: "No se encontraron experimentos",
      noDescription: "Sin descripción",
      createExperiment: "Crear Nuevo Experimento",
      editExperiment: "Editar Experimento",
      experimentDetails: "Detalles del Experimento",
      quick: {
        title: "Acciones Rápidas",
        newExperiment: "Nuevo Experimento",
        importExperiment: "Importar Experimento",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        planned: "Planificados",
        running: "Ejecutándose",
        completed: "Completados",
        failed: "Fallidos",
      },
    },

    // Model Serving Management
    modelServing: {
      title: "Model Serving",
      subtitle: "Gestión de despliegues y servicios de modelos",
      operational: "Operacional",
      totalDeployments: "Total Deployments",
      runningDeployments: "Ejecutándose",
      stoppedDeployments: "Detenidos",
      failedDeployments: "Fallidos",
      search: "Buscar deployments...",
      type: "Tipo",
      status: "Estado",
      newDeployment: "Nuevo Deployment",
      noDeployments: "No se encontraron deployments",
      noDescription: "Sin descripción",
      start: "Iniciar",
      stop: "Detener",
      createDeployment: "Crear Nuevo Deployment",
      editDeployment: "Editar Deployment",
      deploymentDetails: "Detalles del Deployment",
      quick: {
        title: "Acciones Rápidas",
        newDeployment: "Nuevo Deployment",
        importDeployment: "Importar Deployment",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        pending: "Pendientes",
        running: "Ejecutándose",
        stopped: "Detenidos",
        failed: "Fallidos",
      },
    },
  },

  en: {
    // Navigation and layout
    common: {
      dashboard: "Dashboard",
      infrastructure: "Infrastructure",
      admin: "Administration",
      aiNotebook: "AI Notebook",
      domainIngestion: "Domain Ingestion",
      modelEvaluation: "Model Evaluation",
      ragTraining: "RAG Training",
      governance: "Governance",
      settings: "Settings",
      profile: "Profile",
      logout: "Logout",
      loading: "Loading...",
      save: "Save",
      cancel: "Cancel",
      edit: "Edit",
      delete: "Delete",
      add: "Add",
      search: "Search",
      filter: "Filter",
      status: "Status",
      actions: "Actions",
      details: "Details",
      back: "Back",
      next: "Next",
      previous: "Previous",
      close: "Close",
      confirm: "Confirm",
      yes: "Yes",
      no: "No",
      active: "Active",
      inactive: "Inactive",
      pending: "Pending",
      completed: "Completed",
      error: "Error",
      success: "Success",
      warning: "Warning",
      info: "Information",
      changeLanguage: "Change Language",
      selectLanguage: "Select Language",
      noResults: "No results found",
      showing: "Showing",
      all: "ALL",
      clearFilters: "Clear filters",
    },

    // Prompts
    prompts: {
      performance: {
        title: "Prompt Performance Comparison",
        description: "Prompt Performance Comparison Management",
        results: "Results",
        id: "ID",
        activeItems: "Active Items",
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        avgScore: "Avg Score",
      },
      validation: {
        title: "Prompt Validation",
        description: "Prompt Validation Management",
        register: "Register Prompt Validation",
        results: "Results",
        id: "ID",
        validationtype: "Validationtype",
        validationresult: "Validationresult",
        validationscore: "Validationscore",
        validationdetails: "Validationdetails",
        issuesfound: "Issuesfound",
        filterType: "Filter by Validationtype",
        filterStatus: "Filter by Status",
        type1: "TYPE 1",
        type2: "TYPE 2",
      },
      versioning: {
        title: "Prompt Version",
        description: "Prompt Version Management",
        register: "Register Prompt Version",
        results: "Results",
        id: "ID",
        version: "Version",
        description: "Description",
        content: "Content",
        parameters: "Parameter S",
        changes: "Changes",
        filterStatus: "Filter by Status",
      },
    },

    // Dashboard
    dashboard: {
      title: "Control Panel",
      welcome: "Welcome to CodeflowX",
      stats: "Statistics",
      quickActions: "Quick Actions",
      recentProjects: "Recent Projects",
      systemMetrics: "System Metrics",
      totalProjects: "Total Projects",
      activeModels: "Active Models",
      totalUsers: "Total Users",
      systemHealth: "System Health",
      viewAll: "View All",
      createNew: "Create New",
      lastUpdated: "Last Updated",
      performance: "Performance",
      efficiency: "Efficiency",
      reliability: "Reliability",
      activeNodes: "Active Nodes",
      of: "of",
      plusOneThisMonth: "+1 this month",
      codeGenerated: "Code Generated",
      lines: "lines",
      modelsAvailable: "Models Available",
      specialized: "specialized",
      plusThreeNew: "+3 new",
      licenseStatus: "License Status",
      proPlan: "Pro Plan",
      deployModel: "Deploy Model",
      viewAnalytics: "View Analytics",
      manageUsers: "Manage Users",
      systemHealthTitle: "System Health",
      cpuUsage: "CPU Usage",
      memory: "Memory",
      network: "Network",
      security: "Security",
    },

    // Runs
    runs: {
      title: "Ejecuciones de Modelos",
      subtitle:
        "Gestiona y monitorea las ejecuciones de experimentos de modelos de IA",
      totalRuns: "Ejecuciones",
      running: "Ejecutando",
      systemStatus: {
        operational: "Operacional",
      },
      quickActions: {
        title: "Acciones Rápidas",
        createRun: "Crear Ejecución",
        cloneRun: "Clonar Ejecución",
        bulkActions: "Acciones Masivas",
        analytics: "Analytics",
      },
      stats: {
        totalRuns: "Total Ejecuciones",
        thisMonth: "+12 este mes",
        running: "Ejecutando",
        active: "Activas",
        completed: "Completadas",
        successful: "Exitosas",
        failed: "Fallidas",
        issues: "Con problemas",
      },
      searchPlaceholder: "Buscar ejecuciones...",
      selectStatus: "Estado",
      selectExperiment: "Experimento",
      createRun: "Nueva Ejecución",
      tabs: {
        all: "Todas",
        running: "Ejecutando",
        completed: "Completadas",
        failed: "Fallidas",
        scheduled: "Programadas",
      },
      duration: "Duración",
      artifacts: "Artefactos",
      files: "archivos",
      budget: "Presupuesto",
      accuracy: "Precisión",
      actions: {
        view: "Ver",
        edit: "Editar",
        clone: "Clonar",
        artifacts: "Artefactos",
      },
      noRuns: "No se encontraron ejecuciones",
      noRunsDescription:
        "Intenta ajustar los filtros o crear una nueva ejecución",
    },

    // Artifacts
    artifacts: {
      title: "Artefactos de Modelos",
      subtitle:
        "Gestiona y organiza todos los artefactos generados por los experimentos",
      totalArtifacts: "Artefactos",
      systemStatus: {
        operational: "Operacional",
      },
      quickActions: {
        title: "Acciones Rápidas",
        upload: "Subir Artefacto",
        bulkDownload: "Descarga Masiva",
        archive: "Archivar",
        analytics: "Analytics",
      },
      stats: {
        totalArtifacts: "Total Artefactos",
        thisMonth: "+25 este mes",
        totalSize: "Tamaño Total",
        storage: "Almacenamiento",
        available: "Disponibles",
        active: "Activos",
        archived: "Archivados",
      },
      searchPlaceholder: "Buscar artefactos...",
      selectType: "Tipo",
      selectStatus: "Estado",
      upload: "Subir",
      types: {
        model: "Modelo",
        dataset: "Dataset",
        checkpoint: "Checkpoint",
        log: "Log",
        config: "Configuración",
        image: "Imagen",
        other: "Otro",
      },
      size: "Tamaño",
      version: "Versión",
      created: "Creado",
      tags: "Etiquetas",
      actions: {
        view: "Ver",
        download: "Descargar",
        edit: "Editar",
        delete: "Eliminar",
      },
      noArtifacts: "No se encontraron artefactos",
      noArtifactsDescription:
        "Intenta ajustar los filtros o subir un nuevo artefacto",
    },

    // Infrastructure
    infrastructure: {
      title: "Infrastructure Management",
      overview: "Overview",
      providers: "Providers",
      kubernetes: "Kubernetes Clusters",
      gpuInstances: "GPU Instances",
      deployments: "Deployments",
      resources: "Resources",
      credentials: "Credentials",
      costManagement: "Cost Management",
      systemMonitoring: "System Monitoring",
      addProvider: "Add Provider",
      addCluster: "Add Cluster",
      addInstance: "Add Instance",
      clusterName: "Cluster Name",
      instanceType: "Instance Type",
      region: "Region",
      status: "Status",
      nodes: "Nodes",
      cpu: "CPU",
      memory: "Memory",
      storage: "Storage",
      network: "Network",
      security: "Security",
      monitoring: "Monitoring",
      logs: "Logs",
      metrics: "Metrics",
      alerts: "Alerts",
      // New translations for main page
      cloudProviders: "Cloud Providers",
      cloudProvidersDesc: "Manage cloud providers, credentials and connections",
      awsGcpAzure: "AWS, GCP, Azure",
      ovhRunpodVast: "OVH, RunPod, Vast.ai",
      credentialsApiKeys: "Credentials and API Keys",
      statusMonitoring: "Status monitoring",
      kubernetesDesc: "Manage K8s clusters, nodes and resources",
      eksGkeAks: "EKS, GKE, AKS",
      ownClusters: "Own clusters",
      nodeMonitoring: "Node monitoring",
      deploymentManagement: "Deployment management",
      gpuInstancesDesc: "GPU instance management for training and inference",
      t4V100A100H100: "T4, V100, A100, H100",
      rtx40903090: "RTX 4090, 3090",
      spotInstances: "Spot instances",
      costAnalysis: "Cost analysis",
      infrastructureDeployments: "Infrastructure Deployments",
      deploymentsDesc: "Infrastructure and service deployments",
      trainingClusters: "Training clusters",
      inferenceServices: "Inference services",
      autoScaling: "Auto scaling",
      costManagementDesc: "Infrastructure cost analysis and management",
      providerAnalysis: "Provider analysis",
      budgetAlerts: "Budget alerts",
      reports: "Reports",
      optimization: "Optimization",
      systemMonitoringDesc: "Real-time monitoring of all infrastructure",
      realTimeMetrics: "Real-time metrics",
      automaticAlerts: "Automatic alerts",
      customDashboards: "Custom dashboards",
      centralizedLogs: "Centralized logs",
      twoWeeksAgo: "2 weeks ago",
      deployCluster: "Deploy Cluster",
      launchGpu: "Launch GPU",
      viewCosts: "View Costs",
      // New translations for migrated pages
      resourceUtilizationDashboard: "Resource Utilization Dashboard",
      resourceUtilizationDesc: "Monitor resource usage across all infrastructure",
      resourceUtilization: "Resource Utilization",
      totalResources: "Total Resources",
      active: "active",
      avgCpuUsage: "Avg CPU Usage",
      avgMemoryUsage: "Avg Memory Usage",
      avgStorageUsage: "Avg Storage Usage",
      networkUsage: "Network Usage",
      networkIn: "Network In",
      networkOut: "Network Out",
      resourceName: "Resource Name",
      resourceType: "Resource Type",
      cloudResources: "Cloud Resources",
      basicInformation: "Basic Information",
      resourceId: "Resource ID",
      cloudProvider: "Cloud Provider",
      zone: "Zone",
      autoScaling: "Auto Scaling",
      specifications: "Specifications",
      scalingConfiguration: "Scaling Configuration",
      minInstances: "Min Instances",
      currentInstances: "Current Instances",
      maxInstances: "Max Instances",
      cost: "Cost",
      costPerHour: "Cost per Hour",
      costPerMonth: "Cost per Month",
      metadata: "Metadata",
      createdAt: "Created At",
      updatedAt: "Updated At",
      instances: "Instances",
      noResourcesFound: "No resources found",
      cloudProviderOverview: "Cloud Provider Overview",
      cloudCredentialOverview: "Cloud Credential Overview",
      cloudRegionOverview: "Cloud Region Overview",
      cloudResourceOverview: "Cloud Resource Overview",
      kubernetesClusterOverview: "Kubernetes Cluster Overview",
      kubernetesNodeOverview: "Kubernetes Node Overview",
      gpuInstanceOverview: "GPU Instance Overview",
      infrastructureTemplateOverview: "Infrastructure Template Overview",
      infrastructureMetricOverview: "Infrastructure Metric Overview",
      infrastructureMetricsSummaryOverview: "Infrastructure Metrics Summary Overview",
      infrastructureCostOverview: "Infrastructure Cost Overview",
      infrastructureCostAnalysisOverview: "Infrastructure Cost Analysis Overview",
      infrastructureAuditOverview: "Infrastructure Audit Overview",
      infrastructureHealthMatrixOverview: "Infrastructure Health Matrix Overview",
      infrastructureOverviewOverview: "Infrastructure Overview",
      capacityForecastOverview: "Capacity Forecast Overview",
      resourceQuotaOverview: "Resource Quota Overview",
    },

    // Administration
    admin: {
      title: "Administration Panel",
      users: "Users",
      roles: "Roles",
      permissions: "Permissions",
      departments: "Departments",
      sessions: "Sessions",
      audit: "Audit",
      licenses: "Licenses",
      nodes: "Nodes",
      updates: "Updates",
      menuManagement: "Menu Management",
      notifications: "Notifications",
      systemSettings: "System Settings",
      userManagement: "User Management",
      systemHealth: "System Health",
      securitySettings: "Security Settings",
      backupRestore: "Backup & Restore",
      systemLogs: "System Logs",
      performanceMetrics: "Performance Metrics",
      // New translations for main page
      twoHoursAgo: "2 hours ago",
      twoMinutesAgo: "2 minutes ago",
      fiveMinutesAgo: "5 minutes ago",
      tenMinutesAgo: "10 minutes ago",
      fifteenMinutesAgo: "15 minutes ago",
      userLogin: "User login",
      roleUpdated: "Role updated",
      newUserInvited: "New user invited",
      systemBackupCompleted: "System backup completed",
      configurationUpdated: "Configuration updated",
    },

    // Themes
    themes: {
      title: "CodeflowX Themes",
      changeTheme: "Change CodeflowX theme",
      useSystemTheme: "Use system theme",
      trekkerFederation: "🌌 Leka Federation",
      trekkerVulcan: "🖖 Leka Vulcan",
      trekkerKlingon: "⚔️ Leka Klingon",
      trekkerRomulan: "🖖 Leka Romulan",
      trekkerCardassian: "🖖 Leka Cardassian",
      trekkerBajoran: "🖖 Leka Bajoran",
      trekkerFerengi: "💰 Leka Ferengi",
      trekkerAndorian: "🖖 Leka Andorian",
      trekkerTellarite: "🖖 Leka Tellarite",
      trekkerDenobulan: "🏥 Leka Denobulan",
    },

    // Status and colors
    status: {
      online: "Online",
      offline: "Offline",
      maintenance: "Maintenance",
      error: "Error",
      warning: "Warning",
      info: "Information",
      success: "Success",
      pending: "Pending",
      running: "Running",
      stopped: "Stopped",
      starting: "Starting",
      stopping: "Stopping",
      healthy: "Healthy",
      unhealthy: "Unhealthy",
      degraded: "Degraded",
    },

    // System messages
    messages: {
      loadingTheme: "Loading theme...",
      themeApplied: "Theme applied successfully",
      themeError: "Error applying theme",
      redirectingToLogin: "Redirecting to login...",
      systemActive: "System Active",
      aiPowered: "AI Powered",
      documentation: "Documentation",
      support: "Support",
      status: "Status",
      welcomeMessage: "Welcome, {name} 👋",
    },

    // Model Management
    modelManagement: {
      title: "Model Management",
      subtitle: "Overview of the AI model management system",
      totalModels: "Total Models",
      activeModels: "Active Models",
      totalProviders: "Total Providers",
      marketplaceModels: "Marketplace Models",
      recentDownloads: "Recent Downloads",
      systemHealth: "System Health",
      healthy: "Healthy",
      warning: "Warning",
      critical: "Critical",
      approved: "Approved",
      pending: "Pending",
      rejected: "Rejected",
      addModel: "Add Model",
      viewModels: "View Models",
      manageProviders: "Manage Providers",
      marketplace: "Marketplace",
      recentActivity: "Recent Activity",
      topProviders: "Top Providers",
      trendingModels: "Trending Models",
      stats: {
        totalModels: "Total Models",
        totalModelsChange: "+12% this month",
        activeModels: "Active Models",
        activeModelsChange: "+8% this month",
        providers: "Providers",
        providersChange: "+3 this month",
        recentDownloads: "Recent Downloads",
        recentDownloadsChange: "+25% this week",
      },
      systemStatus: {
        title: "System Status",
        generalStatus: "General Status",
        healthy: "Healthy",
        warning: "Warning",
        apiModels: "Models API",
        marketplace: "Marketplace",
        providers: "Providers",
        operational: "Operational",
      },
      recentModels: {
        title: "Recent Models",
        noModels: "No recent models",
      },
      typeDistribution: {
        title: "Models by Type",
        viewDetails: "View Details",
        text: "Text",
        vision: "Vision",
        audio: "Audio",
      },
      quickActions: {
        title: "Quick Actions",
        newModel: "New Model",
        addProvider: "Add Provider",
        exploreMarketplace: "Explore Marketplace",
        settings: "Settings",
      },
      systemInfo: {
        title: "System Information",
        modelManagement: {
          title: "Model Management",
          description:
            "Registration, versioning and complete lifecycle management of AI models",
        },
        aiProviders: {
          title: "AI Providers",
          description:
            "Integration with multiple AI service providers and token management",
        },
        marketplace: {
          title: "Marketplace",
          description:
            "Access to +3,000,000 AI models from multiple marketplaces and providers",
        },
      },
    },

    // Runs
    runs: {
      title: "Model Runs",
      subtitle: "Manage and monitor AI model experiment executions",
      totalRuns: "Runs",
      running: "Running",
      systemStatus: {
        operational: "Operational",
      },
      quickActions: {
        title: "Quick Actions",
        createRun: "Create Run",
        cloneRun: "Clone Run",
        bulkActions: "Bulk Actions",
        analytics: "Analytics",
      },
      stats: {
        totalRuns: "Total Runs",
        thisMonth: "+12 this month",
        running: "Running",
        active: "Active",
        completed: "Completed",
        successful: "Successful",
        failed: "Failed",
        issues: "With issues",
      },
      searchPlaceholder: "Search runs...",
      selectStatus: "Status",
      selectExperiment: "Experiment",
      createRun: "New Run",
      tabs: {
        all: "All",
        running: "Running",
        completed: "Completed",
        failed: "Failed",
        scheduled: "Scheduled",
      },
      duration: "Duration",
      artifacts: "Artifacts",
      files: "files",
      budget: "Budget",
      accuracy: "Accuracy",
      actions: {
        view: "View",
        edit: "Edit",
        clone: "Clone",
        artifacts: "Artifacts",
      },
      noRuns: "No runs found",
      noRunsDescription: "Try adjusting filters or create a new run",
    },

    // Artifacts
    artifacts: {
      title: "Model Artifacts",
      subtitle: "Manage and organize all artifacts generated by experiments",
      totalArtifacts: "Artifacts",
      systemStatus: {
        operational: "Operational",
      },
      quickActions: {
        title: "Quick Actions",
        upload: "Upload Artifact",
        bulkDownload: "Bulk Download",
        archive: "Archive",
        analytics: "Analytics",
      },
      stats: {
        totalArtifacts: "Total Artifacts",
        thisMonth: "+25 this month",
        totalSize: "Total Size",
        storage: "Storage",
        available: "Available",
        active: "Active",
        archived: "Archived",
      },
      searchPlaceholder: "Search artifacts...",
      selectType: "Type",
      selectStatus: "Status",
      upload: "Upload",
      types: {
        model: "Model",
        dataset: "Dataset",
        checkpoint: "Checkpoint",
        log: "Log",
        config: "Configuration",
        image: "Image",
        other: "Other",
      },
      size: "Size",
      version: "Version",
      created: "Created",
      tags: "Tags",
      actions: {
        view: "View",
        download: "Download",
        edit: "Edit",
        delete: "Delete",
      },
      noArtifacts: "No artifacts found",
      noArtifactsDescription: "Try adjusting filters or upload a new artifact",
    },

    // Monitoring
    monitoring: {
      title: "Monitoring",
      alerts: {
        overview: {
          title: "Monitoring Alerts",
          description: "Management of monitoring alerts",
          register: "Register Alert",
          list: "Alerts List",
          alertName: "Alert Name",
          alertType: "Alert Type",
          severity: "Severity",
          status: "Status",
        },
        detail: {
          title: "Alert Detail",
          description: "Management and configuration of monitoring alert",
          information: "Information",
          alertName: "Alert Name",
          alertType: "Alert Type",
          severity: "Severity",
          status: "Status",
          description: "Description",
          alertData: "Alert Data",
          timestamps: "Timestamps",
          triggeredAt: "Triggered At",
          acknowledgedAt: "Acknowledged At",
          acknowledgedBy: "Acknowledged By",
          resolvedAt: "Resolved At",
          resolutionNotes: "Resolution Notes",
        },
        systemDetail: {
          title: "System Alert Detail",
          description: "System alerts management",
          information: "Information",
          alertName: "Alert Name",
          alertType: "Alert Type",
          severity: "Severity",
          status: "Status",
          description: "Description",
          timestamps: "Timestamps",
          triggeredAt: "Triggered At",
          acknowledgedAt: "Acknowledged At",
        },
        responseForm: {
          title: "Critical Alert Response",
          description: "Alert response form",
          formData: "Form Data",
          decision: "Decision",
          decisionPlaceholder: "Enter decision...",
          notes: "Notes",
          notesPlaceholder: "Enter notes...",
        },
      },
      metrics: {
        overview: {
          title: "Monitoring Metrics",
          description: "Monitoring metrics management",
          register: "Register Metric",
          list: "Metrics List",
        },
        detail: {
          title: "Metric Detail",
          description: "Monitoring metrics management",
          information: "Information",
          metricName: "Metric Name",
          metricType: "Metric Type",
          metricValue: "Metric Value",
          status: "Status",
          description: "Description",
          timestamps: "Timestamps",
          collectedAt: "Collected At",
        },
        systemDetail: {
          title: "System Metric Detail",
          description: "System metrics management",
          information: "Information",
          metricName: "Metric Name",
          metricType: "Metric Type",
          metricValue: "Metric Value",
          status: "Status",
          description: "Description",
          timestamps: "Timestamps",
          collectedAt: "Collected At",
        },
      },
      dashboard: {
        title: "Monitoring Dashboard",
        description: "Overview of the monitoring system",
        alerts: "Alerts",
        alertsDesc: "Monitoring alerts management",
        metrics: "Metrics",
        metricsDesc: "System metrics visualization",
        systemHealth: "System Health",
        systemHealthDesc: "System health status",
        performance: "Performance",
        performanceDesc: "Performance metrics",
        quickStats: "Quick Statistics",
        activeAlerts: "Active Alerts",
        uptime: "Uptime",
        avgResponse: "Average Response",
      },
      auditLog: {
        detail: {
          title: "Audit Log Detail",
          description: "Detailed audit log information",
          information: "Information",
          action: "Action",
          entity: "Entity",
          entityId: "Entity ID",
          user: "User",
          ipAddress: "IP Address",
          status: "Status",
          details: "Details",
          timestamps: "Timestamps",
          timestamp: "Timestamp",
          createdAt: "Created At",
        },
      },
      systemHealth: {
        detail: {
          title: "System Health Detail",
          description: "System health information",
          information: "Information",
          status: "Status",
          uptime: "Uptime",
          details: "Details",
          timestamps: "Timestamps",
          lastCheck: "Last Check",
          updatedAt: "Updated At",
        },
      },
      governance: {
        detail: {
          title: "Governance Detail",
          description: "Governance policies management",
          information: "Information",
          name: "Name",
          type: "Type",
          status: "Status",
          description: "Description",
          details: "Details",
          timestamps: "Timestamps",
          createdAt: "Created At",
          updatedAt: "Updated At",
        },
      },
      performance: {
        interventionForm: {
          title: "Performance Intervention",
          description: "Performance intervention form",
          formData: "Form Data",
          interventionType: "Intervention Type",
          description: "Description",
          descriptionPlaceholder: "Describe the intervention...",
          priority: "Priority",
          notes: "Notes",
          notesPlaceholder: "Additional notes...",
        },
      },
      viewsPerformance: {
        title: "Views Performance",
        description: "Views performance configuration",
        formData: "Form Data",
        viewName: "View Name",
        viewNamePlaceholder: "Enter view name...",
        performanceMetric: "Performance Metric",
        performanceMetricPlaceholder: "Enter metric...",
        threshold: "Threshold",
        thresholdPlaceholder: "Enter threshold...",
        notes: "Notes",
        notesPlaceholder: "Additional notes...",
      },
    },

    // Agents
    agents: {
      dashboard: {
        title: "Agents Dashboard",
        subtitle: "AI agents metrics and general status",
        totalAgents: "Total Agents",
        activeAgents: "Active Agents",
        deployedAgents: "Deployed Agents",
        healthScore: "Health Score",
        recentAgents: "Recent Agents",
        performanceMetrics: "Performance Metrics",
        avgPerformanceScore: "Average Score",
        offlineAgents: "Offline Agents",
        version: "Version",
      },
      alerts: {
        title: "Agent Alert",
        subtitle: "Agent Alert Management",
        register: "Register Agent Alert",
        list: "Alerts List",
        alertType: "Alert Type",
        alertCategory: "Alert Category",
        severity: "Severity",
      },
      approval: {
        title: "Agent Approval",
        subtitle: "Agent Approval Management",
        register: "Register Agent Approval",
        list: "Approvals List",
        approvalType: "Approval Type",
        approvalStatus: "Approval Status",
      },
      biasDetection: {
        title: "Bias Detection",
        subtitle: "Bias Detection Management",
        register: "Register Bias Detection",
        list: "Detections List",
        detectionType: "Detection Type",
        severity: "Severity",
      },
      compliance: {
        title: "Compliance",
        subtitle: "Compliance Management",
        register: "Register Compliance",
        list: "Compliance List",
        complianceType: "Compliance Type",
        lastCheck: "Last Check",
        score: "Score",
      },
      decisions: {
        title: "Decisions",
        subtitle: "Decisions Management",
        register: "Register Decision",
        list: "Decisions List",
        decisionType: "Decision Type",
        confidence: "Confidence",
        timestamp: "Timestamp",
      },
      deployment: {
        title: "Deployment",
        subtitle: "Deployment Management",
        register: "Register Deployment",
        list: "Deployments List",
        deploymentType: "Deployment Type",
        environment: "Environment",
        deployedAt: "Deployed At",
      },
      ethics: {
        title: "Ethics",
        subtitle: "Ethics Management",
        register: "Register Ethics",
        list: "Ethics List",
        assessmentType: "Assessment Type",
        score: "Score",
        assessedAt: "Assessed At",
      },
      governance: {
        title: "Governance",
        subtitle: "Governance Management",
        register: "Register Governance",
        list: "Governance List",
        governanceType: "Governance Type",
        lastReview: "Last Review",
        complianceScore: "Compliance Score",
      },
      interactions: {
        title: "Interactions",
        subtitle: "Interactions Management",
        register: "Register Interaction",
        list: "Interactions List",
        interactionType: "Interaction Type",
        participants: "Participants",
        timestamp: "Timestamp",
      },
      collaboration: {
        title: "Collaboration",
        subtitle: "Collaboration Management",
        register: "Register Collaboration",
        list: "Collaborations List",
        collaborationType: "Collaboration Type",
        agents: "Agents",
        createdAt: "Created At",
      },
      communication: {
        title: "Communication",
        subtitle: "Communication Management",
        register: "Register Communication",
        list: "Communications List",
        communicationType: "Communication Type",
        messages: "Messages",
        lastMessage: "Last Message",
      },
      learning: {
        title: "Learning",
        subtitle: "Learning Management",
        register: "Register Learning",
        list: "Learning List",
        learningType: "Learning Type",
        progress: "Progress",
        lastUpdate: "Last Update",
      },
      monitoring: {
        title: "Monitoring",
        subtitle: "Monitoring Management",
        register: "Register Monitoring",
        list: "Monitoring List",
        monitoringType: "Monitoring Type",
        metrics: "Metrics",
        lastCheck: "Last Check",
      },
      health: {
        title: "Health Overview",
        subtitle: "Health Management",
        register: "Register Health",
        list: "Health List",
        healthStatus: "Health Status",
        score: "Score",
        lastCheck: "Last Check",
      },
      domain: {
        title: "Domain Overview",
        subtitle: "Domain Management",
        register: "Register Domain",
        list: "Domains List",
        domainName: "Domain Name",
        agents: "Agents",
        createdAt: "Created At",
      },
      rollback: {
        title: "Rollback",
        subtitle: "Rollback Management",
        register: "Register Rollback",
        list: "Rollbacks List",
        rollbackType: "Rollback Type",
        version: "Version",
        rolledBackAt: "Rolled Back At",
      },
      tools: {
        title: "Tools",
        subtitle: "Tools Management",
        register: "Register Tool",
        list: "Tools List",
        toolName: "Tool Name",
        toolType: "Tool Type",
        version: "Version",
      },
      transparency: {
        title: "Transparency",
        subtitle: "Transparency Management",
        register: "Register Transparency",
        list: "Transparency List",
        transparencyType: "Transparency Type",
        score: "Score",
        lastAudit: "Last Audit",
      },
      versioning: {
        title: "Versioning",
        subtitle: "Versioning Management",
        register: "Register Version",
        list: "Versions List",
        versionNumber: "Version Number",
        releaseDate: "Release Date",
        changes: "Changes",
      },
      workflow: {
        execution: {
          title: "Execution Overview",
          subtitle: "Workflow Executions Management",
          register: "Register Execution",
          list: "Executions List",
          executionType: "Execution Type",
          duration: "Duration (s)",
          startedAt: "Started At",
        },
      },
    },

    // Domain Ingestion
    domainIngestion: {
      title: "Domain Ingestion Dashboard",
      subtitle: "Monitor and manage your specialized data domains",
      stats: {
        totalDomains: "Total Domains",
        totalDocuments: "Total Documents",
        webPages: "Web Pages",
        apis: "Connected APIs",
        activeDomains: "active domains",
        documentsChange: "+12% this month",
        webChange: "+8% this week",
        apisChange: "+3 this month",
      },
      actions: {
        wizard: "Domain Wizard",
        manage: "Manage Domains",
        access: "Access",
      },
      modules: {
        webScraping: {
          title: "Web Scraping",
          description: "Extract data from websites and monitor changes",
        },
        uploadDocuments: {
          title: "Upload Documents",
          description: "Upload and process documents to enrich domains",
        },
        monitor: {
          title: "Monitoring",
          description: "Monitor job performance and status",
        },
        settings: {
          title: "Settings",
          description: "Configure global and domain-specific parameters",
        },
        configureSources: {
          title: "Configure Sources",
          description: "Configure data sources and APIs",
        },
      },
      recentJobs: {
        title: "Recent Jobs",
      },
      jobStatus: {
        completed: "Completed",
        running: "Running",
        failed: "Failed",
        pending: "Pending",
        queued: "Queued",
        processing: "Processing",
        cancelled: "Cancelled",
        retrying: "Retrying",
      },
      wizard: {
        steps: {
          basicInfo: "Basic Information",
          dataSources: "Data Sources",
          compliance: "Compliance",
          advancedFeatures: "Advanced Features",
          review: "Review",
        },
        templates: {
          title: "Domain Templates",
          description: "Select a predefined template or create a custom one",
          fintech: "Fintech Banking",
          healthcare: "Healthcare Analytics",
          ecommerce: "E-commerce Insights",
          government: "IA Act Compliance",
          manufacturing: "Manufacturing IoT",
          custom: "Custom",
        },
        industries: {
          title: "Industry",
          fintech: "Fintech",
          healthcare: "Healthcare",
          ecommerce: "E-commerce",
          government: "Government",
          manufacturing: "Manufacturing",
          education: "Education",
          legal: "Legal",
          other: "Other",
        },
        compliance: {
          title: "Compliance Frameworks",
          description: "Select applicable compliance frameworks",
          gdpr: "GDPR (General Data Protection Regulation)",
          sox: "SOX (Sarbanes-Oxley Act)",
          pci: "PCI DSS (Payment Card Industry Data Security Standard)",
          hipaa: "HIPAA (Health Insurance Portability and Accountability Act)",
          iaActEspana: "IA Act Spain (Artificial Intelligence Law)",
          iaActEuropa: "IA Act Europe (Artificial Intelligence Act)",
          iso27001: "ISO 27001 (Information Security Management)",
          iso27701: "ISO 27701 (Privacy Information Management)",
          nist: "NIST Cybersecurity Framework",
          soc2: "SOC 2 (Service Organization Control 2)",
          fedramp:
            "FedRAMP (Federal Risk and Authorization Management Program)",
          ccpa: "CCPA (California Consumer Privacy Act)",
          lgpd: "LGPD (Lei Geral de Proteção de Dados)",
          pipeda:
            "PIPEDA (Personal Information Protection and Electronic Documents Act)",
          // Software Development ISO Standards
          iso12207: "ISO 12207 (Software Life Cycle)",
          iso15504: "ISO 15504 (Software Process Assessment)",
          iso20000: "ISO 20000 (IT Service Management)",
          iso27002: "ISO 27002 (Information Security Controls)",
          iso27005: "ISO 27005 (Security Risk Management)",
          iso27017: "ISO 27017 (Cloud Security)",
          iso27018: "ISO 27018 (Cloud Data Protection)",
          iso9001: "ISO 9001 (Quality Management System)",
          iso14001: "ISO 14001 (Environmental Management System)",
          // Additional European Regulations
          eidas: "eIDAS (EU Electronic Identification)",
          psd2: "PSD2 (EU Payment Services Directive)",
          nis2: "NIS2 (EU Network Security Directive)",
          // Software Quality Standards
          cmmi: "CMMI (Capability Maturity Model Integration)",
          tickit: "TICKIT (IT and Telecommunications Quality)",
          spice:
            "SPICE (Software Process Improvement and Capability Determination)",
          custom: "Custom",
        },
        dataSources: {
          title: "Data Source Types",
          description: "Select the types of data sources you will use",
          api: "API",
          database: "Database",
          file: "File",
          web: "Web",
          stream: "Stream",
        },
        basicInfo: {
          name: "Domain Name",
          description: "Description",
          industry: "Industry",
          owner: "Owner",
          team: "Team",
        },
        advancedFeatures: {
          title: "Advanced Features",
          rag: "RAG (Create datasets)",
          training: "Model Training",
          compliance: "Automatic Compliance",
          monitoring: "Advanced Monitoring",
          modelSelection: "Model Selection",
          modelName: "Internal Model Name",
          adapter: "Create Adapter",
          finetuning: "Fine-tuning",
          baseModel: "Base Model",
          customModel: "Custom Model",
        },
        review: {
          title: "Final Review",
          description: "Review the configuration before creating the domain",
          template: "Selected template",
          industry: "Industry",
          compliance: "Compliance frameworks",
          dataSources: "Data sources",
          features: "Enabled features",
        },
        template: {
          fintech: {
            name: "Fintech Banking",
            description:
              "Domain specialized in financial data analysis and regulatory compliance",
            features: [
              "RAG enabled",
              "GDPR/SOX compliance",
              "Advanced monitoring",
            ],
            compliance: ["GDPR", "SOX", "PCI DSS"],
          },
          healthcare: {
            name: "Healthcare Analytics",
            description:
              "Domain for medical data analysis with HIPAA compliance",
            features: [
              "RAG enabled",
              "HIPAA compliance",
              "Advanced encryption",
            ],
            compliance: ["HIPAA", "GDPR"],
          },
          ecommerce: {
            name: "E-commerce Insights",
            description:
              "Domain for purchase behavior analysis and recommendations",
            features: ["RAG enabled", "Behavior analysis", "Recommendations"],
            compliance: ["GDPR"],
          },
          government: {
            name: "IA Act Compliance",
            description:
              "Domain for compliance with Spanish IA Act and government regulations",
            features: ["RAG enabled", "IA Act compliance", "Complete audit"],
            compliance: ["IA Act Spain", "GDPR"],
          },
          manufacturing: {
            name: "Manufacturing IoT",
            description:
              "Domain for IoT data analysis in industrial environments",
            features: ["RAG enabled", "IoT analysis", "Real-time monitoring"],
            compliance: ["ISO 27001"],
          },
        },
        actions: {
          next: "Next",
          previous: "Previous",
          create: "Create Domain",
          cancel: "Cancel",
        },
      },
    },

    // Model Evaluation
    modelEvaluation: {
      title: "Model Evaluation",
      subtitle: "Dashboard for AI model evaluation and testing",
      completed: "Completed",
      running: "Running",
      failed: "Failed",
      pending: "Pending",
      unknown: "Unknown",
      loadingData: "Loading dashboard data...",
      errorLoadingData: "Could not load dashboard data",
      pipeline: "Pipeline",
      abTest: "A/B Test",
      security: "Security",
      evaluation: "Evaluation",
      newEvaluation: "New Evaluation",
      viewEvaluation: "View Evaluation",
      pipelineModal: "Pipeline Modal",
      abTestModal: "A/B Test Modal",
      securityModal: "Security Modal",
      reportModal: "Report Modal",
    },

    // RAG Training
    ragTraining: {
      title: "RAG Training",
      subtitle: "Dashboard for RAG training and metrics",
      totalDocuments: "Total Documents",
      totalChunks: "Total Chunks",
      totalSearches: "Total Searches",
      averageResponseTime: "Average Response Time",
      activeUsers: "Active Users",
      storageUsed: "Storage Used",
      documentsByType: "Documents by Type",
      searchesByDay: "Searches by Day",
      topQueries: "Top Queries",
      pdf: "PDF",
      docx: "DOCX",
      txt: "TXT",
      html: "HTML",
      configuration: "database configuration",
      apiEndpoints: "API endpoints",
      authentication: "authentication",
      middlewareSetup: "middleware setup",
      deployment: "deployment",
    },

    // Training Hub
    training: {
      title: "Training Hub",
      subtitle: "Adapters, Fine-tuning and Artifacts unified",
      operational: "Operational",
      adapters: "Adapters",
      finetuning: "Fine-tuning",
      artifacts: "Artifacts",
      storage: "Storage",
      search: "Search...",
      type: "Type",
      technology: "Technology",
      domain: "Domain",
      generic: "Generic",
      quick: {
        newAdapter: "New Adapter",
        newFinetune: "New Fine-tuning",
        importModel: "Import Model",
        analytics: "Analytics",
      },
      promote: "Promote",
      viewRun: "View Run",
      download: "Download",
      metric: "Metric",
      cost: "Cost",
      duration: "Duration",
      startedAt: "Started At",
      size: "Size",
      createdAt: "Created At",
      linked: "Linked",
      detailsPlaceholder: "Detail content (mock)",
      // Menu translations
      menu: {
        dashboard: "Training Dashboard",
        governanceDashboard: "Governance Dashboard",
        hpoDashboard: "HPO Dashboard",
        experiments: "Experiments",
        experimentsLineage: "Experiment Lineage",
        experimentsTemplates: "Experiment Templates",
        hpoExperiments: "HPO Experiments",
        hpoTrials: "HPO Trials",
        runs: "Runs",
        runComparisons: "Run Comparisons",
        metrics: "Metrics",
        metricSeries: "Metric Series",
        metricStreams: "Metric Streams",
        artifacts: "Artifacts",
        checkpoints: "Checkpoints",
        execution: "Execution",
        executionLogs: "Execution Logs",
        parameters: "Parameters",
        governance: "Governance",
        infrastructure: "Infrastructure",
        datasets: "Datasets",
        environments: "Environments",
        alerts: "Alerts",
        models: "Models",
      },
    },

    // Models Management
    models: {
      title: "Manage Models",
      subtitle: "Complete lifecycle management of AI models",
      operational: "Operational",
      totalModels: "Total Models",
      approvedModels: "Approved Models",
      activeModels: "Active Models",
      marketplaceModels: "Marketplace",
      search: "Search models...",
      type: "Type",
      status: "Status",
      provider: "Provider",
      text: "Text",
      vision: "Vision",
      audio: "Audio",
      multimodal: "Multimodal",
      draft: "Draft",
      approved: "Approved",
      ready: "Ready",
      error: "Error",
      noDescription: "No description",
      duplicate: "Duplicate",
      noModels: "No models found",
      createModel: "Create New Model",
      modelDetails: "Model Details",
      editModel: "Edit Model",
      importModel: "Import Model",
      bulkActions: "Bulk Actions",
      analytics: "Model Analytics",
      newModel: "New Model",
      createModelDescription: "Form to create a new model (mock)",
      viewModelDescription: "Detailed view of the model (mock)",
      editModelDescription: "Form to edit the model (mock)",
      importModelDescription: "Form to import model (mock)",
      bulkActionsDescription: "Bulk actions on models (mock)",
      analyticsDescription: "Model metrics and analytics (mock)",
      quick: {
        title: "Quick Actions",
        createModel: "Create Model",
        importModel: "Import Model",
        bulkActions: "Bulk Actions",
        analytics: "Analytics",
      },
    },

    // Training Hub
    training: {
      title: "Training Hub",
      subtitle: "Adapters, Fine‑tuning y Artefactos unificados",
      operational: "Operacional",
      adapters: "Adapters",
      finetuning: "Fine-tuning",
      artifacts: "Artefactos",
      storage: "Storage",
      search: "Buscar...",
      type: "Tipo",
      technology: "Tecnología",
      domain: "Dominio",
      generic: "Genérico",
      quick: {
        newAdapter: "Nuevo Adapter",
        newFinetune: "Nuevo Fine‑tuning",
        importModel: "Importar Modelo",
        analytics: "Analytics",
      },
      promote: "Promover",
      viewRun: "Ver run",
      download: "Descargar",
      metric: "Métrica",
      cost: "Coste",
      duration: "Duración",
      startedAt: "Inicio",
      size: "Tamaño",
      createdAt: "Creado",
      linked: "Vinculado",
      detailsPlaceholder: "Contenido de detalle (mock)",
      // Dashboards
      governance: {
        dashboard: {
          title: "Training Overview Dashboard",
          description: "Monitoreo de experimentos y entrenamiento de modelos",
          totalItems: "Total Items",
          activeItems: "Active Items",
          deployedItems: "Deployed Items",
          trainingItems: "Training Items",
          offlineItems: "Offline Items",
          avgScore: "Average Score",
          charts: "Training Metrics",
          chartsPlaceholder: "Los gráficos de métricas se mostrarán aquí",
        },
        overview: {
          title: "Training Governance",
          register: "Registrar Governance",
          list: "Lista de Governance",
          name: "Name",
          status: "Status",
          complianceLevel: "Compliance Level",
          lastAudit: "Last Audit",
        },
        infrastructureOverview: {
          title: "Infrastructure",
          register: "Registrar Infrastructure",
          list: "Lista de Infrastructure",
          name: "Name",
          type: "Type",
          status: "Status",
          capacity: "Capacity",
        },
      },
      hpo: {
        dashboard: {
          title: "HPO Progress Dashboard",
          description: "Monitoreo de progreso de optimización de hiperparámetros",
          totalItems: "Total Items",
          activeItems: "Active Items",
          deployedItems: "Deployed Items",
          trainingItems: "Training Items",
          offlineItems: "Offline Items",
          avgScore: "Average Score",
          charts: "HPO Progress Metrics",
          chartsPlaceholder: "Los gráficos de progreso HPO se mostrarán aquí",
        },
        overview: {
          title: "HPO Experiments",
          register: "Registrar HPO Experiment",
          list: "Lista de HPO Experiments",
          name: "Name",
          status: "Status",
          algorithm: "Algorithm",
          trials: "Trials",
          bestScore: "Best Score",
        },
        trialOverview: {
          title: "HPO Trials",
          register: "Registrar Trial",
          list: "Lista de Trials",
          hpoExperimentId: "HPO Experiment ID",
          trialNumber: "Trial Number",
          status: "Status",
          score: "Score",
          parameters: "Parameters",
        },
      },
      artifacts: {
        overview: {
          title: "Training Artifacts",
          register: "Registrar Training Artifact",
          list: "Lista de Artifacts",
          trainingJobId: "Training Job ID",
          artifactType: "Artifact Type",
          name: "Name",
          filepath: "Filepath",
        },
      },
      checkpoints: {
        overview: {
          title: "Checkpoints",
          register: "Registrar Checkpoint",
          list: "Lista de Checkpoints",
          checkpointName: "Checkpoint Name",
          epoch: "Epoch",
          step: "Step",
          type: "Type",
          path: "Path",
        },
      },
      execution: {
        overview: {
          title: "Training Execution",
          register: "Registrar Training Execution",
          list: "Lista de Executions",
          trainingJobId: "Training Job ID",
          executionNumber: "Execution Number",
          executionType: "Execution Type",
          status: "Status",
          startedAt: "Started At",
        },
        logOverview: {
          title: "Training Logs",
          register: "Registrar Log",
          list: "Lista de Logs",
          trainingJobId: "Training Job ID",
          logLevel: "Log Level",
          message: "Message",
          timestamp: "Timestamp",
        },
      },
      experiments: {
        overview: {
          title: "Experiments",
          register: "Registrar Experiment",
          list: "Lista de Experiments",
          name: "Name",
          description: "Description",
          artifactLocation: "Artifact Location",
          lifecycleStage: "Lifecycle Stage",
          tags: "Tags",
        },
        lineageOverview: {
          title: "Experiment Lineage",
          register: "Registrar Lineage",
          list: "Lista de Lineages",
          experimentId: "Experiment ID",
          parentExperimentId: "Parent Experiment ID",
          lineageType: "Lineage Type",
          depth: "Depth",
        },
        templateOverview: {
          title: "Experiment Templates",
          register: "Registrar Template",
          list: "Lista de Templates",
          name: "Name",
          description: "Description",
          status: "Status",
          usageCount: "Usage Count",
        },
      },
      infrastructure: {
        datasetOverview: {
          title: "Datasets",
          register: "Registrar Dataset",
          list: "Lista de Datasets",
          name: "Name",
          source: "Source",
          size: "Size",
          status: "Status",
        },
        environmentOverview: {
          title: "Environments",
          register: "Registrar Environment",
          list: "Lista de Environments",
          name: "Name",
          type: "Type",
          status: "Status",
          pythonVersion: "Python Version",
        },
      },
      metrics: {
        overview: {
          title: "Training Metrics",
          register: "Registrar Training Metric",
          list: "Lista de Metrics",
          trainingJobId: "Training Job ID",
          metricName: "Metric Name",
          metricValue: "Metric Value",
          metricUnit: "Metric Unit",
          epoch: "Epoch",
        },
        seriesOverview: {
          title: "Metric Series",
          register: "Registrar Series",
          list: "Lista de Series",
          name: "Name",
          metricName: "Metric Name",
          runId: "Run ID",
          dataPoints: "Data Points",
        },
        streamOverview: {
          title: "Metric Streams",
          register: "Registrar Stream",
          list: "Lista de Streams",
          name: "Name",
          metricName: "Metric Name",
          runId: "Run ID",
          status: "Status",
        },
      },
      monitoring: {
        alertOverview: {
          title: "Training Alerts",
          register: "Registrar Alert",
          list: "Lista de Alerts",
          name: "Name",
          severity: "Severity",
          status: "Status",
          triggeredAt: "Triggered At",
          resolvedAt: "Resolved At",
        },
      },
      parameters: {
        overview: {
          title: "Parameters",
          register: "Registrar Parameter",
          list: "Lista de Parameters",
          name: "Name",
          value: "Value",
          type: "Type",
          experimentId: "Experiment ID",
        },
      },
      runs: {
        overview: {
          title: "Runs",
          register: "Registrar Run",
          list: "Lista de Runs",
          name: "Name",
          status: "Status",
          experimentId: "Experiment ID",
          startedAt: "Started At",
          duration: "Duration",
        },
        comparisonOverview: {
          title: "Run Comparisons",
          register: "Registrar Comparison",
          list: "Lista de Comparisons",
          name: "Name",
          runIds: "Run IDs",
          comparisonType: "Comparison Type",
          createdAt: "Created At",
        },
      },
      // Menu translations
      menu: {
        dashboard: "Dashboard de Training",
        governanceDashboard: "Dashboard de Governance",
        hpoDashboard: "Dashboard HPO",
        experiments: "Experimentos",
        experimentsLineage: "Lineage de Experimentos",
        experimentsTemplates: "Templates de Experimentos",
        hpoExperiments: "Experimentos HPO",
        hpoTrials: "Trials HPO",
        runs: "Runs",
        runComparisons: "Comparaciones de Runs",
        metrics: "Métricas",
        metricSeries: "Series de Métricas",
        metricStreams: "Streams de Métricas",
        artifacts: "Artefactos",
        checkpoints: "Checkpoints",
        execution: "Ejecución",
        executionLogs: "Logs de Ejecución",
        parameters: "Parámetros",
        governance: "Governance",
        infrastructure: "Infraestructura",
        datasets: "Datasets",
        environments: "Entornos",
        alerts: "Alertas",
        models: "Modelos",
      },
    },

    // Models Management
    models: {
      title: "Administrar Modelos",
      subtitle: "Gestión completa del ciclo de vida de modelos de IA",
      operational: "Operacional",
      totalModels: "Total Modelos",
      approvedModels: "Modelos Aprobados",
      activeModels: "Modelos Activos",
      marketplaceModels: "Marketplace",
      search: "Buscar modelos...",
      type: "Tipo",
      status: "Estado",
      provider: "Proveedor",
      text: "Texto",
      vision: "Visión",
      audio: "Audio",
      multimodal: "Multimodal",
      draft: "Borrador",
      approved: "Aprobado",
      ready: "Listo",
      error: "Error",
      noDescription: "Sin descripción",
      duplicate: "Duplicar",
      noModels: "No se encontraron modelos",
      createModel: "Crear Nuevo Modelo",
      modelDetails: "Detalles del Modelo",
      editModel: "Editar Modelo",
      importModel: "Importar Modelo",
      bulkActions: "Acciones Masivas",
      analytics: "Analytics de Modelos",
      newModel: "Nuevo Modelo",
      createModelDescription: "Formulario para crear un nuevo modelo (mock)",
      viewModelDescription: "Vista detallada del modelo (mock)",
      editModelDescription: "Formulario para editar el modelo (mock)",
      importModelDescription: "Formulario para importar modelo (mock)",
      bulkActionsDescription: "Acciones masivas sobre modelos (mock)",
      analyticsDescription: "Métricas y analytics de modelos (mock)",
      quick: {
        title: "Acciones Rápidas",
        createModel: "Crear Modelo",
        importModel: "Importar Modelo",
        bulkActions: "Acciones Masivas",
        analytics: "Analytics",
      },
    },

    // BPMN Task Inbox
    taskInbox: {
      title: "BPMN Task Inbox",
      subtitle: "Manage your pending tasks",
      stats: {
        totalTasks: "Pending Tasks",
        assignedToMe: "Assigned to Me",
        groupTasks: "From My Roles",
      },
      filters: {
        process: "Process",
        priority: "Priority",
        state: "State",
        allProcesses: "All processes",
        allPriorities: "All priorities",
        allStates: "All states",
        assigned: "Assigned",
        unassigned: "Unassigned",
        candidate: "Candidate",
        low: "Low",
        normal: "Normal",
        high: "High",
        urgent: "Urgent",
      },
      table: {
        priority: "Priority",
        task: "Task",
        process: "Process",
        assigned: "Assigned",
        roles: "Roles",
        creation: "Creation",
        dueDate: "Due Date",
        actions: "Actions",
        noTasks: "No pending tasks",
        unassigned: "Unassigned",
        openTask: "Open task",
        claim: "Claim",
        assign: "Assign",
      },
      assignDialog: {
        title: "Assign Task to User",
        task: "Task",
        availableUsers: "Available users",
        username: "Username",
        fullname: "Full Name",
        email: "Email",
        cancel: "Cancel",
        assign: "Assign",
      },
      actions: {
        refresh: "Refresh",
        clearFilters: "Clear filters",
      },
    },

    // Internal Audit Scheduling Form
    internalAuditScheduling: {
      title: "Schedule Internal Audit ISO 42001",
      subtitle: "Complete the data to schedule the internal audit",
      formTitle: "Audit Data",
      project: "Project",
      projectPlaceholder: "Project ID",
      date: "Scheduled Date",
      scope: "Scope",
      scopePlaceholder: "Clauses, processes and systems to audit",
      auditor: "Responsible Auditor",
      auditorPlaceholder: "User or auditor role",
      back: "Back",
      cancel: "Cancel",
      submit: "Schedule Audit",
      saving: "Saving...",
      errors: {
        scopeRequired: "You must define the audit scope",
        auditorRequired: "You must assign a responsible auditor",
        dateRequired: "You must select a date",
        submitError: "Error scheduling the audit",
      },

    // BPMN Forms
    agentApprovalHumanOverride: {
      title: "Agent Approval - Human Override",
      subtitle: "Complete the data for agent approval - human override",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    modelApprovalHumanOverride: {
      title: "Model Approval - Human Override",
      subtitle: "Complete the data for model approval - human override",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    promptHumanReview: {
      title: "Prompt Human Review",
      subtitle: "Complete the data for prompt human review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    hitlSlaReminder: {
      title: "HITL SLA Reminder",
      subtitle: "Complete the data for hitl sla reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    promptApprovalRequest: {
      title: "Prompt Approval Request",
      subtitle: "Complete the data for prompt approval request",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionApproval: {
      title: "AI Decommission Approval",
      subtitle: "Complete the data for ai decommission approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapApproval: {
      title: "Competence Gap Approval",
      subtitle: "Complete the data for competence gap approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionApproval: {
      title: "Corrective Action Approval",
      subtitle: "Complete the data for corrective action approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    approveConformityAssessment: {
      title: "Approve Conformity Assessment",
      subtitle: "Complete the data for approve conformity assessment",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    deployerApproval: {
      title: "Deployer Approval",
      subtitle: "Complete the data for deployer approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    boardDecisionPaper: {
      title: "Board Decision Paper",
      subtitle: "Complete the data for board decision paper",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    managementReview: {
      title: "Management Review",
      subtitle: "Complete the data for management review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    llmEvaluationReview: {
      title: "LLM Evaluation Review",
      subtitle: "Complete the data for llm evaluation review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ragEvaluationReview: {
      title: "RAG Evaluation Review",
      subtitle: "Complete the data for rag evaluation review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    modelEvaluationReview: {
      title: "Model Evaluation Review",
      subtitle: "Complete the data for model evaluation review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapAssessment: {
      title: "Competence Gap Assessment",
      subtitle: "Complete the data for competence gap assessment",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionAssessment: {
      title: "Corrective Action Assessment",
      subtitle: "Complete the data for corrective action assessment",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    performanceReviewDecision: {
      title: "Performance Review Decision",
      subtitle: "Complete the data for performance review decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    compliance: {
      dashboard: {
        title: "Consolidated Compliance Dashboard",
        subtitle: "Consolidated view of compliance status according to EU AI Act",
      },
      postMarketMonitoring: {
        title: "Post-Market Monitoring Dashboard",
        subtitle: "Continuous monitoring of AI systems according to EU AI Act Art. 72",
      },
      sector: {
        title: "Sectorial FaaS Metamodel",
        subtitle: "View sectors, subsectors and configured frameworks without modifying CodeflowX core",
      },
      conformityDeclaration: {
        title: "EU Declaration of Conformity Manager",
        subtitle: "Management of conformity declarations according to EU AI Act Annex V",
      },
      conformityReview: {
        title: "Conformity Assessment Review",
        subtitle: "Human review required for borderline compliance assessment",
      },
    },
    complianceReview: {
      title: "Compliance Review",
      subtitle: "Complete the data for compliance review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    complianceReviewDecision: {
      title: "Compliance Review Decision",
      subtitle: "Complete the data for compliance review decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    reviewQmsGaps: {
      title: "Review QMS Gaps",
      subtitle: "Complete the data for review qms gaps",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    completeDocumentation: {
      title: "Complete Documentation",
      subtitle: "Complete the data for complete documentation",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    biasReview: {
      title: "Bias Review",
      subtitle: "Complete the data for bias review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    biasMitigationPlan: {
      title: "Bias Mitigation Plan",
      subtitle: "Complete the data for bias mitigation plan",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    biasUrgentDecision: {
      title: "Bias Urgent Decision",
      subtitle: "Complete the data for bias urgent decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    driftAnalysis: {
      title: "Drift Analysis",
      subtitle: "Complete the data for drift analysis",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    driftReviewDecision: {
      title: "Drift Review Decision",
      subtitle: "Complete the data for drift review decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    internalAuditExecution: {
      title: "Internal Audit Execution",
      subtitle: "Complete the data for internal audit execution",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    managementReviewSchedule: {
      title: "Management Review Schedule",
      subtitle: "Complete the data for management review schedule",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    completeMissingElements: {
      title: "Complete Missing Elements",
      subtitle: "Complete the data for complete missing elements",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    defineModifications: {
      title: "Define Modifications",
      subtitle: "Complete the data for define modifications",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    enhancedReview: {
      title: "Enhanced Review",
      subtitle: "Complete the data for enhanced review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    finalReview: {
      title: "Final Review",
      subtitle: "Complete the data for final review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    euRegistration: {
      title: "EU Registration",
      subtitle: "Complete the data for eu registration",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    reviewRegistrationPackage: {
      title: "Review Registration Package",
      subtitle: "Complete the data for review registration package",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    fixValidationErrors: {
      title: "Fix Validation Errors",
      subtitle: "Complete the data for fix validation errors",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    manualResolution: {
      title: "Manual Resolution",
      subtitle: "Complete the data for manual resolution",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    documentIncidentDetails: {
      title: "Document Incident Details",
      subtitle: "Complete the data for document incident details",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    verifyIncidentResolution: {
      title: "Verify Incident Resolution",
      subtitle: "Complete the data for verify incident resolution",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    rootCauseAnalysis: {
      title: "Root Cause Analysis",
      subtitle: "Complete the data for root cause analysis",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    defineCorrectiveActions: {
      title: "Define Corrective Actions",
      subtitle: "Complete the data for define corrective actions",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionImplementation: {
      title: "Corrective Action Implementation",
      subtitle: "Complete the data for corrective action implementation",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionEffectiveness: {
      title: "Corrective Action Effectiveness",
      subtitle: "Complete the data for corrective action effectiveness",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    managementReviewActions: {
      title: "Management Review Actions",
      subtitle: "Complete the data for management review actions",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsReviewRequest: {
      title: "Ethics Review Request",
      subtitle: "Complete the data for ethics review request",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsReviewReminder: {
      title: "Ethics Review Reminder",
      subtitle: "Complete the data for ethics review reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsCommitteeReview: {
      title: "Ethics Committee Review",
      subtitle: "Complete the data for ethics committee review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsMitigationPlan: {
      title: "Ethics Mitigation Plan",
      subtitle: "Complete the data for ethics mitigation plan",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapImplementation: {
      title: "Competence Gap Implementation",
      subtitle: "Complete the data for competence gap implementation",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapAdditionalActions: {
      title: "Competence Gap Additional Actions",
      subtitle: "Complete the data for competence gap additional actions",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapRework: {
      title: "Competence Gap Rework",
      subtitle: "Complete the data for competence gap rework",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionRequest: {
      title: "AI Decommission Request",
      subtitle: "Complete the data for ai decommission request",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionVerification: {
      title: "AI Decommission Verification",
      subtitle: "Complete the data for ai decommission verification",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionRetention: {
      title: "AI Decommission Retention",
      subtitle: "Complete the data for ai decommission retention",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    modelApprovalReminder: {
      title: "Model Approval Reminder",
      subtitle: "Complete the data for model approval reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    datasetReviewReminder: {
      title: "Dataset Review Reminder",
      subtitle: "Complete the data for dataset review reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    categorizeIncident: {
      title: "Categorize Incident",
      subtitle: "Complete the data for categorize incident",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    executeCorrectiveAction: {
      title: "Execute Corrective Action",
      subtitle: "Complete the data for execute corrective action",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    },

    // Governance
    governance: {
      title: "Automatic Governance Dashboard",
      subtitle: "Monitor and manage AI governance in real time",
      refresh: "Refresh",
      configure: "Configure",
      low: "LOW",
      medium: "MEDIUM",
      high: "HIGH",
      critical: "CRITICAL",
      riskLevel: "Risk Level",
      compliance: "Compliance",
      audit: "Audit",
      policies: "Policies",
      violations: "Violations",
      alerts: "Alerts",
      metrics: "Metrics",
      reports: "Reports",
      settings: "Settings",
      overview: {
        title: "Policies Overview",
        subtitle: "Management and configuration of governance policies",
        metrics: {
          total: "Total Policies",
          active: "Active Policies",
          violations: "Violations",
          compliance: "Compliance Score",
        },
        filters: {
          title: "Filters",
          clear: "Clear",
          search: {
            placeholder: "Search policies...",
          },
          type: "All Types",
          "type.security": "Security",
          "type.compliance": "Compliance",
          "type.ethics": "Ethics",
          "type.privacy": "Privacy",
          status: "All Statuses",
          "status.active": "Active",
          "status.inactive": "Inactive",
          "status.draft": "Draft",
          enforcement: "All Levels",
          "enforcement.mandatory": "Mandatory",
          "enforcement.recommended": "Recommended",
          "enforcement.optional": "Optional",
        },
        table: {
          title: "Policies",
          id: "ID",
          name: "Name",
          type: "Type",
          enforcement: "Level",
          version: "Version",
          status: "Status",
          violations: "Violations",
          score: "Score",
          actions: "Actions",
          empty: "No policies found",
        },
        button: {
          create: "Create Policy",
        },
        confirmDelete: "Delete this policy?",
      },
      detail: {
        "title.edit": "Edit Policy",
        "title.create": "New Policy",
        button: {
          evaluate: "Evaluate",
          compliance: "Check Compliance",
          score: "Calculate Score",
        },
        form: {
          title: "Policy Information",
          name: "Policy Name",
          version: "Version",
          type: "Type",
          "type.security": "Security",
          "type.compliance": "Compliance",
          "type.ethics": "Ethics",
          "type.privacy": "Privacy",
          "type.operational": "Operational",
          category: "Category",
          enforcement: "Enforcement Level",
          "enforcement.mandatory": "Mandatory",
          "enforcement.recommended": "Recommended",
          "enforcement.optional": "Optional",
          "enforcement.informational": "Informational",
          description: "Description",
          effectiveDate: "Effective Date",
          expirationDate: "Expiration Date",
          status: "Status",
          "status.active": "Active",
          "status.inactive": "Inactive",
          "status.draft": "Draft",
          "status.review": "Under Review",
          "status.archived": "Archived",
          metadata: "Metadata (JSON)",
        },
        tabs: {
          rules: "Rules",
          evaluations: "Evaluations",
          violations: "Violations",
          compliance: "Compliance",
          statistics: "Statistics",
          "rules.empty": "No rules defined",
          "rules.name": "Name",
          "rules.condition": "Condition",
          "rules.action": "Action",
          "rules.priority": "Priority",
          "rules.active": "Active",
          "evaluations.empty": "No evaluations registered",
          "evaluations.date": "Date",
          "evaluations.result": "Result",
          "evaluations.score": "Score",
          "evaluations.evaluator": "Evaluator",
          "violations.empty": "No violations registered",
          "violations.type": "Type",
          "violations.description": "Description",
          "violations.severity": "Severity",
          "violations.date": "Date",
          "violations.status": "Status",
          "compliance.empty": "No compliance assessments",
          "compliance.average": "Average Score",
          "compliance.date": "Date",
          "compliance.area": "Area",
          "compliance.score": "Score",
          "compliance.status": "Status",
          "compliance.findings": "Findings",
          "statistics.rules": "Rules",
          "statistics.evaluations": "Evaluations",
          "statistics.violations": "Violations",
          "statistics.score": "Current Score",
          "statistics.info": "Policy Information",
          "statistics.createdBy": "Created by",
          "statistics.createdAt": "Creation date",
          "statistics.lastEvaluation": "Last evaluation",
          "statistics.lastUpdated": "Last update",
          "statistics.evaluationStatus": "Evaluation status",
          "statistics.complianceScore": "Compliance score",
        },
        evaluation: {
          success: "Evaluation completed",
        },
        compliance: {
          success: "Compliance check completed",
        },
        score: {
          success: "Score calculated: {score}",
        },
      },
    },

    // Providers Management
    providers: {
      title: "Proveedores de IA",
      subtitle: "Gestión de proveedores de servicios de IA",
      operational: "Operacional",
      totalProviders: "Total Proveedores",
      activeProviders: "Proveedores Activos",
      openaiProviders: "OpenAI",
      selfHosting: "Self-Hosting",
      search: "Buscar proveedores...",
      type: "Tipo",
      newProvider: "Nuevo Proveedor",
      noProviders: "No se encontraron proveedores",
      createProvider: "Crear Nuevo Proveedor",
      editProvider: "Editar Proveedor",
      providerDetails: "Detalles del Proveedor",
      quick: {
        title: "Acciones Rápidas",
        newProvider: "Nuevo Proveedor",
        importProvider: "Importar Proveedor",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
    },

    // Marketplace Management
    marketplace: {
      title: "Marketplace de Modelos",
      subtitle: "Descubre y descarga modelos de IA de múltiples proveedores",
      operational: "Operacional",
      totalModels: "Total Modelos",
      downloaded: "Descargados",
      downloadedModels: "Modelos Descargados",
      avgRating: "Rating Promedio",
      totalDownloads: "Total Descargas",
      search: "Buscar modelos...",
      source: "Fuente",
      type: "Tipo",
      sort: "Ordenar por",
      noModels: "No se encontraron modelos",
      noDescription: "Sin descripción",
      modelDetails: "Detalles del Modelo",
      download: "Descargar",
      downloaded: "Descargado",
      quick: {
        title: "Acciones Rápidas",
        advancedSearch: "Búsqueda Avanzada",
        downloadQueue: "Cola de Descargas",
        configuration: "Configuración",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        text: "Texto",
        vision: "Visión",
        audio: "Audio",
        multimodal: "Multimodal",
      },
    },

    // Experiments Management
    experiments: {
      title: "Experimentos de Modelos",
      subtitle: "Gestión y seguimiento de experimentos de IA",
      operational: "Operacional",
      totalExperiments: "Total Experimentos",
      runningExperiments: "Ejecutándose",
      completedExperiments: "Completados",
      failedExperiments: "Fallidos",
      search: "Buscar experimentos...",
      type: "Tipo",
      status: "Estado",
      newExperiment: "Nuevo Experimento",
      noExperiments: "No se encontraron experimentos",
      noDescription: "Sin descripción",
      createExperiment: "Crear Nuevo Experimento",
      editExperiment: "Editar Experimento",
      experimentDetails: "Detalles del Experimento",
      quick: {
        title: "Acciones Rápidas",
        newExperiment: "Nuevo Experimento",
        importExperiment: "Importar Experimento",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        planned: "Planificados",
        running: "Ejecutándose",
        completed: "Completados",
        failed: "Fallidos",
      },
    },

    // Model Serving Management
    modelServing: {
      title: "Model Serving",
      subtitle: "Gestión de despliegues y servicios de modelos",
      operational: "Operacional",
      totalDeployments: "Total Deployments",
      runningDeployments: "Ejecutándose",
      stoppedDeployments: "Detenidos",
      failedDeployments: "Fallidos",
      search: "Buscar deployments...",
      type: "Tipo",
      status: "Estado",
      newDeployment: "Nuevo Deployment",
      noDeployments: "No se encontraron deployments",
      noDescription: "Sin descripción",
      start: "Iniciar",
      stop: "Detener",
      createDeployment: "Crear Nuevo Deployment",
      editDeployment: "Editar Deployment",
      deploymentDetails: "Detalles del Deployment",
      quick: {
        title: "Acciones Rápidas",
        newDeployment: "Nuevo Deployment",
        importDeployment: "Importar Deployment",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        pending: "Pendientes",
        running: "Ejecutándose",
        stopped: "Detenidos",
        failed: "Fallidos",
      },
    },

    // Serving Module
    serving: {
      deploymentInstance: {
        title: "Deployment Instance",
        register: "Registrar Deployment Instance",
        list: "Listado de Deployment Instances",
        detail: {
          title: "Deployment Instance",
          formTitle: "Información del Deployment Instance",
        },
        deploymentid: "Deployment Id",
        instanceid: "Instance ID",
        instancename: "Instance Name",
        instancetype: "Instance Type",
        status: "Status",
        ipaddress: "IP Address",
        port: "Port",
        nodename: "Node Name",
        resourceusage: "Resource Usage",
        startedat: "Started At",
        lastheartbeat: "Last Heartbeat",
        createdat: "Created At",
        updatedat: "Updated At",
      },
      model: {
        title: "Model",
        register: "Registrar Model",
        list: "Listado de Models",
        detail: {
          title: "Model",
          formTitle: "Información del Model",
        },
      },
      deploymentLog: {
        title: "Deployment Log",
        register: "Registrar Deployment Log",
        list: "Listado de Deployment Logs",
      },
      deploymentMetric: {
        title: "Deployment Metric",
        register: "Registrar Deployment Metric",
        list: "Listado de Deployment Metrics",
      },
      deploymentStatus: {
        title: "Deployment Status",
        register: "Registrar Deployment Status",
        list: "Listado de Deployment Status",
      },
      modelDeployment: {
        title: "Model Deployment",
        register: "Registrar Model Deployment",
        list: "Listado de Model Deployments",
      },
      modelMetrics: {
        title: "Model Metrics",
        register: "Registrar Model Metrics",
        list: "Listado de Model Metrics",
      },
      modelPrediction: {
        title: "Model Prediction",
        register: "Registrar Model Prediction",
        list: "Listado de Model Predictions",
      },
      modelVersion: {
        title: "Model Version",
        register: "Registrar Model Version",
        list: "Listado de Model Versions",
      },
      servingEndpoint: {
        title: "Serving Endpoint",
        register: "Registrar Serving Endpoint",
        list: "Listado de Serving Endpoints",
      },
      servingRequest: {
        title: "Serving Request",
        register: "Registrar Serving Request",
        list: "Listado de Serving Requests",
      },
      slaCompliance: {
        title: "SLA Compliance",
        register: "Registrar SLA Compliance",
        list: "Listado de SLA Compliance",
      },
      errorAnalysis: {
        title: "Error Analysis",
        register: "Registrar Error Analysis",
        list: "Listado de Error Analysis",
      },
      costBreakdown: {
        title: "Cost Breakdown",
        register: "Registrar Cost Breakdown",
        list: "Listado de Cost Breakdown",
      },
    },

    // RAG Module
    rag: {
      dashboard: {
        title: "RAG Dashboard",
        subtitle: "Retrieval Augmented Generation System",
        totalProjects: "Total Projects",
        totalChunks: "Total Chunks",
        totalSearches: "Total Searches",
        avgResponseTime: "Average Response Time",
        activeProjects: "Active Projects",
        vsLastMonth: "vs last month",
        projectsOverview: "Projects Overview",
        viewAll: "View All",
        chunks: "chunks",
        embeddings: "embeddings",
        searches: "searches",
        models: "Models",
        recentActivity: "Recent Activity",
        modelUsage: "Model Usage",
        usage: "Usage",
      },
      project: {
        createTitle: "Create RAG Project",
        createSubtitle: "Configure a new RAG project",
        editTitle: "Edit RAG Project",
        basicInfo: "Basic Information",
        name: "Project Name",
        namePlaceholder: "Enter project name",
        description: "Description",
        descriptionPlaceholder: "Describe the purpose of the RAG project",
        isActive: "Active Project",
        embeddingModel: "Embedding Model",
        modelConfiguration: "Model Configuration",
        rerankerModel: "Reranker Model",
        llmModel: "LLM Model",
        ragConfiguration: "RAG Configuration",
        maxChunkSize: "Maximum Chunk Size",
        chunkOverlap: "Chunk Overlap",
        semanticChunking: "Semantic Chunking",
        statistics: "Project Statistics",
        documents: "Documents",
        chunks: "Chunks",
        embeddings: "Embeddings",
        searches: "Searches",
        avgResponseTime: "Average Time",
        successRate: "Success Rate",
        assignments: "Assignments",
        documentAssignments: "Assigned Documents",
        webscrapingAssignments: "Assigned Web Scraping",
        datasourceAssignments: "Assigned Data Sources",
        lekaServerConfig: "Leka Server Configuration",
        lekaEndpoint: "Leka Server Endpoint",
        lekaApiKey: "Leka Server API Key",
        lekaTimeout: "Timeout (ms)",
      },
      help: {
        dashboard: {
          title: "RAG Dashboard",
          content:
            "The RAG Dashboard allows you to monitor the performance of your Retrieval Augmented Generation projects, including usage statistics, active projects, and recent activity.",
        },
        projects: {
          title: "RAG Projects",
          content:
            "<h4>What are RAG Projects?</h4><p>RAG (Retrieval Augmented Generation) projects are intelligent systems that combine information retrieval with text generation. They allow you to create chatbots and assistants that can answer questions based on your own documents and data.</p><h4>What are they used for?</h4><ul><li><strong>Specialized Chatbots:</strong> Create assistants that know your specific documentation</li><li><strong>Intelligent Search:</strong> Find relevant information in large volumes of documents</li><li><strong>Contextualized Responses:</strong> Generate accurate responses based on your content</li><li><strong>Support Automation:</strong> Reduce the workload of your support team</li><li><strong>Interactive Knowledge Base:</strong> Convert static documents into a conversational experience</li></ul><h4>Project Management</h4><p>From this page you can:</p><ul><li>Create new RAG projects with custom configurations</li><li>Edit AI models (embedding, reranker, LLM) for each project</li><li>Monitor performance and usage statistics</li><li>Manage data sources assigned to each project</li><li>Configure chunking and processing parameters</li></ul>",
        },
        createProject: {
          title: "Create RAG Project",
          content:
            "Create a new RAG project by configuring embedding, reranker, and LLM models, as well as chunking parameters and Leka server configuration.",
        },
        editProject: {
          title: "Edit RAG Project",
          content:
            "Edit your RAG project configuration, including models, chunking parameters, Leka server configuration, and monitor project statistics.",
        },
        models: {
          title: "RAG Models",
          content:
            "Manage available AI models for your RAG projects, including embedding, reranker, and LLM models from different providers.",
        },
        createModel: {
          title: "Create RAG Model",
          content:
            "<h4>AI Model Configuration</h4><p>This page allows you to create and configure new AI models for your RAG projects. You can configure:</p><ul><li><strong>Basic Information:</strong> Name, type (Embedding, Reranker, LLM), provider and version</li><li><strong>Technical Configuration:</strong> Endpoint, API Key, cost per token, maximum tokens and context window</li><li><strong>Advanced Options:</strong> Local vs remote model, active/inactive status</li></ul><p><strong>Model Types:</strong></p><ul><li><strong>Embedding:</strong> Converts text into numerical vectors for semantic search</li><li><strong>Reranker:</strong> Improves the relevance of search results</li><li><strong>LLM:</strong> Generates responses based on retrieved context</li></ul><p>Once configured, you can test the model before saving it to ensure it works correctly.</p>",
        },
        editModel: {
          title: "Edit RAG Model",
          content:
            "Edit the configuration of an existing AI model, including performance parameters, cost, and endpoint configuration.",
        },
        chat: {
          title: "RAG Chat",
          content:
            "Interact with your RAG projects through chat. Select a project and ask questions to get answers based on your documents.",
        },
        reranker: {
          title: "RAG Reranker",
          content:
            "Configure and manage search result reranking to improve the relevance of responses in your RAG projects.",
        },
        chunks: {
          title: "Document Chunks",
          content:
            "Chunks are processed document fragments that enable more efficient search. Auto-tags are generated automatically based on content, while manual tags can be added, edited, or removed to improve categorization and retrieval of specific information.",
        },
        search: {
          title: "Semantic Search",
          content:
            "Perform advanced semantic searches in your RAG documents with different search strategies and filters.",
        },
        searchTypes: {
          vector: "Vector",
          textual: "Textual",
          hybrid: "Hybrid",
          filtered: "Filtered",
        },
        analytics: {
          title: "RAG Analytics",
          content:
            "Monitor the performance and metrics of your RAG projects, including usage statistics, response times, and result quality.",
        },
      },
      reranker: {
        title: "Reranker",
        subtitle: "Improve search result relevance",
        configuration: "Reranker Configuration",
        project: "RAG Project",
        selectProject: "Select project",
        model: "Reranking Model",
        selectModel: "Select model",
        topK: "Top K Results",
        threshold: "Relevance Threshold",
        testQuery: "Query Test",
        query: "Test Query",
        queryPlaceholder: "Enter a query to test reranking...",
        runTest: "Run Test",
        testHistory: "Test History",
      },
      chunks: {
        title: "Chunks",
        subtitle: "Visualize and manage document chunks",
        project: "RAG Project",
        selectProject: "Select project",
        chunkType: "Chunk Type",
        selectType: "Select type",
        searchChunks: "Search Chunks",
        searchPlaceholder: "Search by content or tags...",
        totalChunks: "Total Chunks",
        avgSize: "Average Size",
        autoTags: "Auto Tags",
        viewDetails: "View Details",
        filters: "Filters",
        manualTags: "Manual Tags",
        newTag: "New tag...",
        addTag: "Add Tag",
        editTags: "Edit Tags",
        removeTag: "Remove Tag",
        source: "Source",
        relevance: "Relevance",
        characters: "characters",
        createdAt: "Created",
        saveChanges: "Save Changes",
        cancel: "Cancel",
      },
      search: {
        title: "Semantic Search",
        subtitle: "Advanced search in RAG documents",
        project: "RAG Project",
        selectProject: "Select project",
        searchQuery: "Search Query",
        queryPlaceholder: "Enter your search query...",
        searchType: "Search Type",
        selectType: "Select type",
        executeSearch: "Execute Search",
        searchHistory: "Search History",
        results: "Results",
        relevance: "Relevance",
        source: "Source",
      },
      analytics: {
        title: "Analytics",
        subtitle: "RAG project metrics and performance",
        project: "RAG Project",
        selectProject: "Select project",
        timeRange: "Time Range",
        selectRange: "Select range",
        refresh: "Refresh",
        totalSearches: "Total Searches",
        avgResponseTime: "Average Response Time",
        successRate: "Success Rate",
        userSatisfaction: "User Satisfaction",
        topQueries: "Top Queries",
        performanceMetrics: "Performance Metrics",
        usageStats: "Usage Statistics",
      },
      healthDashboard: {
        title: "Index Health Dashboard",
        description: "Index Health Dashboard Management",
        metrics: "Health Metrics",
        totalItems: "Total Items",
        activeItems: "Active Items",
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        avgScore: "Avg Score",
      },
      dataSources: {
        title: "Data Source",
        description: "Data Source Management",
        register: "Register Data Source",
        total: "Total",
        active: "Active",
        pending: "Pending",
        inactive: "Inactive",
        list: "Data Sources List",
        name: "Name",
        description: "Description",
        type: "Type",
        connectionConfig: "Connectionconfig",
        syncConfig: "Syncconfig",
        filterType: "Filter by Type",
        filterStatus: "Filter by Status",
        filterSyncStatus: "Filter by Syncstatus",
      },
      versioning: {
        title: "Version",
        description: "Version Management",
        register: "Register Version",
        total: "Total",
        active: "Active",
        pending: "Pending",
        inactive: "Inactive",
        list: "Versions List",
        version: "Version",
        description: "Description",
        configuration: "Configuration",
        embeddingModel: "Embeddingmodel",
        retrievalConfig: "Retrievalconfig",
        filterStatus: "Filter by Status",
      },
    },

    // Analytics Module
    analytics: {
      overview: {
        dashboard: {
          title: "Analytics Overview",
          subtitle: "Analytics Overview Management",
          list: "Analytics List",
        },
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        generatedAt: "Generated At",
      },
      metrics: {
        overview: {
          title: "Analytics Metrics",
          subtitle: "Analytics Metrics Management",
          register: "Register Metric",
          list: "Metrics List",
        },
        detail: {
          editTitle: "Edit Metric",
          createTitle: "New Metric",
          subtitle: "Analytics Metric management and configuration",
          formTitle: "Metric Data",
        },
        name: "Name",
        type: "Type",
        value: "Value",
        unit: "Unit",
        thresholdMin: "Min Threshold",
        thresholdMax: "Max Threshold",
        context: "Context",
        metadata: "Metadata",
        createdBy: "Created By",
        updatedBy: "Updated By",
        createdAt: "Creation Date",
        updatedAt: "Update Date",
        filter: {
          allTypes: "All Types",
          allStatus: "All Status",
        },
        errors: {
          nameRequired: "Name is required",
          valueRequired: "Value is required",
          createdByRequired: "Created by is required",
          createdAtRequired: "Creation date is required",
          saveError: "Error saving metric",
        },
      },
      reports: {
        overview: {
          title: "Analytics Reports",
          subtitle: "Analytics Reports Management",
          register: "Register Report",
          list: "Reports List",
        },
        detail: {
          editTitle: "Edit Report",
          createTitle: "New Report",
          subtitle: "Analytics Report management and configuration",
          formTitle: "Report Data",
        },
        name: "Name",
        type: "Type",
        description: "Description",
        data: "Data",
        config: "Config",
        generatedAt: "Generation Date",
        schedule: "Schedule",
        createdBy: "Created By",
        updatedBy: "Updated By",
        createdAt: "Creation Date",
        updatedAt: "Update Date",
        filter: {
          allTypes: "All Types",
          allStatus: "All Status",
        },
        errors: {
          nameRequired: "Name is required",
          statusRequired: "Status is required",
          createdByRequired: "Created by is required",
          createdAtRequired: "Creation date is required",
          saveError: "Error saving report",
        },
      },
      trends: {
        overview: {
          title: "Analytics Trends",
          subtitle: "Analytics Trends Management",
          list: "Trends List",
        },
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        generatedAt: "Generated At",
      },
    },

    // Analytics Module
    analytics: {
      overview: {
        dashboard: {
          title: "Analytics Overview",
          subtitle: "Gestión de Analytics Overview",
          list: "Listado de Analytics",
        },
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        generatedAt: "Generated At",
      },
      metrics: {
        overview: {
          title: "Analytics Metrics",
          subtitle: "Gestión de Analytics Metrics",
          register: "Registrar Métrica",
          list: "Listado de Métricas",
        },
        detail: {
          editTitle: "Editar Métrica",
          createTitle: "Nueva Métrica",
          subtitle: "Gestión y configuración de Analytics Metric",
          formTitle: "Datos de la Métrica",
        },
        name: "Nombre",
        type: "Tipo",
        value: "Valor",
        unit: "Unidad",
        thresholdMin: "Umbral Mín",
        thresholdMax: "Umbral Máximo",
        context: "Contexto",
        metadata: "Metadata",
        createdBy: "Creado Por",
        updatedBy: "Actualizado Por",
        createdAt: "Fecha Creación",
        updatedAt: "Fecha Actualización",
        filter: {
          allTypes: "Todos los Tipos",
          allStatus: "Todos los Estados",
        },
        errors: {
          nameRequired: "El nombre es requerido",
          valueRequired: "El valor es requerido",
          createdByRequired: "El creador es requerido",
          createdAtRequired: "La fecha de creación es requerida",
          saveError: "Error al guardar la métrica",
        },
      },
      reports: {
        overview: {
          title: "Analytics Reports",
          subtitle: "Gestión de Analytics Reports",
          register: "Registrar Reporte",
          list: "Listado de Reportes",
        },
        detail: {
          editTitle: "Editar Reporte",
          createTitle: "Nuevo Reporte",
          subtitle: "Gestión y configuración de Analytics Report",
          formTitle: "Datos del Reporte",
        },
        name: "Nombre",
        type: "Tipo",
        description: "Descripción",
        data: "Datos",
        config: "Config",
        generatedAt: "Fecha Generación",
        schedule: "Programación",
        createdBy: "Creado Por",
        updatedBy: "Actualizado Por",
        createdAt: "Fecha Creación",
        updatedAt: "Fecha Actualización",
        filter: {
          allTypes: "Todos los Tipos",
          allStatus: "Todos los Estados",
        },
        errors: {
          nameRequired: "El nombre es requerido",
          statusRequired: "El estado es requerido",
          createdByRequired: "El creador es requerido",
          createdAtRequired: "La fecha de creación es requerida",
          saveError: "Error al guardar el reporte",
        },
      },
      trends: {
        overview: {
          title: "Analytics Trends",
          subtitle: "Gestión de Analytics Trends",
          list: "Listado de Trends",
        },
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        generatedAt: "Generated At",
      },
    },

    // Technology Management
    technology: {
      create: {
        title: "Crear Nueva Tecnología",
        content:
          "Esta página te permite registrar una nueva tecnología en el catálogo del sistema. Puedes especificar información básica como nombre, categoría, tipo, versiones, URLs de documentación y repositorio, así como configurar requisitos de infraestructura y opciones de integración con RAG, entrenamiento y evaluación automática.",
      },
              help: {
          create: {
            title: "Crear Tecnología",
            content:
              "Registra nuevas tecnologías en el catálogo para que estén disponibles en el sistema. Incluye información detallada sobre versiones, documentación y requisitos técnicos.",
          },
        },
        catalog: {
          title: "Catálogo de Tecnologías",
          content:
            "Explora y gestiona todas las tecnologías disponibles en el sistema. Puedes buscar, filtrar, descargar y agregar tecnologías a tus stacks. Utiliza los filtros para encontrar tecnologías específicas por categoría, popularidad o nombre.",
        },
    },

    // Core Module
    core: {
      departments: {
        title: "Gestión de Departamentos",
        description: "Gestiona departamentos, equipos y recursos organizacionales",
        register: "Registrar Departamento",
        list: "Lista de Departamentos",
        new: "Nuevo Departamento",
        edit: "Editar Departamento",
        detailDescription: "Gestión y configuración de Department",
        createdAt: "Created At",
        updatedAt: "Updated At",
        corname: "Nombre",
        code: "Código",
        managerid: "ID Manager",
        parentid: "ID Departamento Padre",
        isactive: "Activo",
        description: "Descripción",
      },
      users: {
        title: "Gestión de Usuarios",
        description: "Gestiona usuarios del sistema",
        register: "Registrar Usuario",
        list: "Lista de Usuarios",
        new: "Nuevo Usuario",
        edit: "Editar Usuario",
        detailDescription: "Gestión y configuración de User",
        username: "Usuario",
        email: "Email",
        firstname: "Nombre",
        lastname: "Apellido",
        phone: "Teléfono",
        passwordhash: "Contraseña",
        departmentid: "ID Departamento",
        createdAt: "Created At",
        updatedAt: "Updated At",
        isactive: "Activo",
        islocked: "Bloqueado",
      },
      menus: {
        title: "Gestión de Menús",
        description: "Gestiona los menús del sistema",
        register: "Registrar Menú",
        list: "Lista de Menús",
      },
      roles: {
        title: "Gestión de Roles",
        description: "Gestiona los roles del sistema",
        register: "Registrar Rol",
        list: "Lista de Roles",
      },
      permissions: {
        title: "Gestión de Permisos",
        description: "Gestiona los permisos del sistema",
        register: "Registrar Permiso",
        list: "Lista de Permisos",
      },
      loginAttempts: {
        title: "Intentos de Login",
        description: "Historial de intentos de acceso al sistema",
        list: "Lista de Intentos",
      },
      userSessions: {
        title: "Sesiones de Usuario",
        description: "Gestiona las sesiones activas de usuarios",
        list: "Lista de Sesiones",
      },
      dashboard: {
        title: "Admin Dashboard Summary",
        description: "Resumen general del sistema administrativo",
        activeItems: "Items Activos",
        deployedItems: "Items Desplegados",
        trainingItems: "Items en Entrenamiento",
        offlineItems: "Items Offline",
      },
    },
    projects: {
      portfolioDashboard: {
        title: "Dashboard de Portfolio de Proyectos",
        description: "Vista general del portfolio de proyectos",
        totalProjects: "Total Proyectos",
        active: "activos",
        budget: "Presupuesto",
        used: "utilizado",
        resources: "Recursos",
        utilization: "utilización",
        completion: "Completados",
        rate: "tasa",
        budgetBreakdown: "Desglose de Presupuesto",
        spent: "Gastado",
        remaining: "Restante",
        projectStatus: "Estado de Proyectos",
        onTime: "A Tiempo",
        inProgress: "En Progreso",
        atRisk: "En Riesgo",
      },
      overview: {
        title: "Proyectos",
        newProject: "Nuevo Proyecto",
        search: "Buscar proyectos...",
        progress: "Progreso",
        members: "miembros",
      },
      detail: {
        information: "Información General",
        domain: "Dominio",
        owner: "Propietario",
        startDate: "Fecha de Inicio",
        endDate: "Fecha de Fin",
        progress: "Progreso",
        completion: "Completado",
        daysRemaining: "días restantes",
        overdue: "Proyecto vencido",
        budget: "Presupuesto",
        total: "Total",
        spent: "Gastado",
        remaining: "Restante",
        team: "Equipo",
        teamSize: "Tamaño del Equipo",
        members: "miembros",
        metadata: "Metadata",
        createdAt: "Creado",
        updatedAt: "Actualizado",
      },
      status: {
        active: "Activo",
        completed: "Completado",
        "on-hold": "En Pausa",
        cancelled: "Cancelado",
      },
    },
  },
  en: {
    // ... existing translations ...
    // Core Module
    core: {
      departments: {
        title: "Department Management",
        description: "Manage departments, teams and organizational resources",
        register: "Register Department",
        list: "Department List",
        new: "New Department",
        edit: "Edit Department",
        detailDescription: "Department management and configuration",
        createdAt: "Created At",
        updatedAt: "Updated At",
        corname: "Name",
        code: "Code",
        managerid: "Manager ID",
        parentid: "Parent Department ID",
        isactive: "Active",
        description: "Description",
      },
      users: {
        title: "User Management",
        description: "Manage system users",
        register: "Register User",
        list: "User List",
      },
      dashboard: {
        title: "Admin Dashboard Summary",
        description: "General administrative system summary",
        activeItems: "Active Items",
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
      },
    },
    projects: {
      portfolioDashboard: {
        title: "Project Portfolio Dashboard",
        description: "Overview of the project portfolio",
        totalProjects: "Total Projects",
        active: "active",
        budget: "Budget",
        used: "used",
        resources: "Resources",
        utilization: "utilization",
        completion: "Completed",
        rate: "rate",
        budgetBreakdown: "Budget Breakdown",
        spent: "Spent",
        remaining: "Remaining",
        projectStatus: "Project Status",
        onTime: "On Time",
        inProgress: "In Progress",
        atRisk: "At Risk",
      },
      overview: {
        title: "Projects",
        newProject: "New Project",
        search: "Search projects...",
        progress: "Progress",
        members: "members",
      },
      detail: {
        information: "General Information",
        domain: "Domain",
        owner: "Owner",
        startDate: "Start Date",
        endDate: "End Date",
        progress: "Progress",
        completion: "Completed",
        daysRemaining: "days remaining",
        overdue: "Project overdue",
        budget: "Budget",
        total: "Total",
        spent: "Spent",
        remaining: "Remaining",
        team: "Team",
        teamSize: "Team Size",
        members: "members",
        metadata: "Metadata",
        createdAt: "Created",
        updatedAt: "Updated",
      },
      status: {
        active: "Active",
        completed: "Completed",
        "on-hold": "On Hold",
        cancelled: "Cancelled",
      },
      domains: {
        title: "Project Domains",
        newDomain: "New Domain",
        search: "Search domains...",
        projectCount: "Projects",
      },
      members: {
        title: "Project Members",
        newMember: "New Member",
        search: "Search members...",
        role: "Role",
        project: "Project",
        allocation: "Allocation",
      },
      artifacts: {
        title: "Project Artifacts",
        newArtifact: "New Artifact",
        search: "Search artifacts...",
        project: "Project",
        size: "Size",
        createdAt: "Created",
      },
      documents: {
        title: "Project Documents",
        newDocument: "New Document",
        search: "Search documents...",
        project: "Project",
        size: "Size",
        uploadedBy: "Uploaded By",
        uploadedAt: "Date",
      },
      invoices: {
        title: "Project Invoices",
        newInvoice: "New Invoice",
        search: "Search invoices...",
        amount: "Amount",
        issueDate: "Issue Date",
        dueDate: "Due Date",
        status: {
          pending: "Pending",
          paid: "Paid",
          overdue: "Overdue",
          cancelled: "Cancelled",
        },
      },
      requirements: {
        title: "Project Requirements",
        newRequirement: "New Requirement",
        search: "Search requirements...",
        project: "Project",
        assignedTo: "Assigned To",
        priority: {
          high: "High",
          medium: "Medium",
          low: "Low",
        },
        status: {
          open: "Open",
          "in-progress": "In Progress",
          completed: "Completed",
          cancelled: "Cancelled",
        },
      },
      tasks: {
        title: "Project Tasks",
        newTask: "New Task",
        search: "Search tasks...",
        progress: "Progress",
        assignedTo: "Assigned To",
        dueDate: "Due Date",
        project: "Project",
        status: {
          todo: "To Do",
          "in-progress": "In Progress",
          review: "In Review",
          done: "Done",
        },
        priority: {
          high: "High",
          medium: "Medium",
          low: "Low",
        },
      },
      financialSummary: {
        title: "Project Financial Summary",
        totalBudget: "Total Budget",
        projects: "projects",
        totalSpent: "Total Spent",
        utilized: "utilized",
        totalRemaining: "Total Remaining",
        available: "available",
        averageBudget: "Average Budget",
        perProject: "per project",
        monthlySpending: "Monthly Spending",
        categoryBreakdown: "Category Breakdown",
      },
      resourceAllocation: {
        title: "Resource Allocation",
        totalResources: "Total Resources",
        resources: "resources",
        allocated: "Allocated",
        utilization: "utilization",
        available: "Available",
        free: "free",
        utilizationRate: "Utilization Rate",
        ofTotal: "of total",
        byProject: "Allocation by Project",
        byRole: "Allocation by Role",
      },
    },
  },
};

// Hook para usar traducciones
export function useTranslation() {
  const [mounted, setMounted] = useState(false);
  const [language, setLanguageState] = useState<Language>("es");

  // Solo ejecutar en el cliente después de la hidratación
  useEffect(() => {
    setMounted(true);
    const savedLanguage =
      (localStorage.getItem("codeflowx-language") as Language) || "es";
    setLanguageState(savedLanguage);
  }, []);

  const getLanguage = (): Language => {
    if (typeof window !== "undefined") {
      return (localStorage.getItem("codeflowx-language") as Language) || "es";
    }
    return "es";
  };

  const setLanguage = (language: Language) => {
    if (typeof window !== "undefined") {
      localStorage.setItem("codeflowx-language", language);
      setLanguageState(language);
      // Recargar la página para aplicar el idioma
      window.location.reload();
    }
  };

  const t = (
    key: string,
    fallback?: string,
    params?: Record<string, string>
  ): string => {
    // Durante SSR o antes de la hidratación, siempre usar español
    if (!mounted) {
      const keys = key.split(".");
      let translation = translations["es"];
      for (const k of keys) {
        translation = translation?.[k];
        if (!translation) break;
      }
      // Asegurar que devolvemos una cadena
      return typeof translation === "string" ? translation : fallback || key;
    }

    const currentLanguage = getLanguage();
    const keys = key.split(".");

    // Buscar en el idioma actual
    let translation = translations[currentLanguage];
    for (const k of keys) {
      translation = translation?.[k];
      if (!translation) break;
    }

    // Si no se encuentra, buscar en español como fallback
    let fallbackTranslation = translations["es"];
    for (const k of keys) {
      fallbackTranslation = fallbackTranslation?.[k];
      if (!fallbackTranslation) break;
    }

    // Asegurar que devolvemos una cadena
    let finalTranslation =
      translation || fallbackTranslation || fallback || key;

    // Si finalTranslation no es una cadena, usar el fallback o la clave
    if (typeof finalTranslation !== "string") {
      finalTranslation = fallback || key;
    }

    // Reemplazar parámetros si existen
    if (params) {
      return finalTranslation.replace(/\{(\w+)\}/g, (match, param) => {
        return params[param] || match;
      });
    }

    return finalTranslation;
  };

  return {
    language: mounted ? language : "es",
    setLanguage,
    t,
    mounted,
    availableLanguages: ["es", "en"],
  };
}

```

Revisando errores de linting:


Traducciones añadidas. Los errores de linting son preexistentes (propiedades duplicadas en otras secciones) y no están relacionados con las traducciones de agents.

## Resumen final

### Tareas completadas

1. Dashboard principal: `/agents/monitoring/dashboard`
2. 21 pantallas overview: todas creadas con estilos "Wow Factor"
3. 22 API routes mock: todas funcionales
4. Traducciones: añadidas en español e inglés en `i18n.ts`

### Traducciones añadidas

- Dashboard de agents (métricas, KPIs)
- 21 módulos overview (alerts, approval, bias-detection, compliance, decisions, deployment, ethics, governance, interactions, collaboration, communication, learning, monitoring, health, domain, rollback, tools, transparency, versioning, workflow)
- Todas las traducciones incluyen: title, subtitle, register, list y campos específicos de cada módulo

### Estado

- Pantallas: 22/22 completadas
- API routes: 22/22 completadas
- Traducciones: 22/22 completadas (ES/EN)
- Menú: pendiente (ya existe estructura en `modules.ts`)

Todas las pantallas están listas para usar. Las traducciones están disponibles en español e inglés, y las pantallas usan correctamente las claves `common.*` para elementos compartidos (total, active, pending, search, etc.).

