"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { AlertTriangle, Plus, Search, Trash2, Eye } from "lucide-react";
import { useRouter } from "next/navigation";

interface AgentAlert {
  id: number;
  agentUuid?: string;
  agentName?: string;
  alertType: string;
  alertCategory?: string;
  severity: string;
  status: string;
  priority?: string;
  impactLevel?: string;
  urgencyLevel?: string;
  title: string;
  description?: string;
  triggeredAt?: string;
  acknowledgedAt?: string;
  acknowledgedBy?: string;
  resolvedAt?: string;
  resolvedBy?: string;
}

export default function AgentAlertsOverviewPage() {
  const router = useRouter();
  const { t, mounted } = useTranslation();
  const [alerts, setAlerts] = useState<AgentAlert[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

  useEffect(() => {
    loadData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      // Cargar agentes primero
      const agentsResponse = await fetch("/api/governance/agents/registry");
      let agentsList: any[] = [];
      if (agentsResponse.ok) {
        const agentsData = await agentsResponse.json();
        agentsList = agentsData.items || agentsData || [];
      } else {
        agentsList = [
          { id: 1, uuid: "550e8400-e29b-41d4-a716-446655440000", name: "Credit Scoring Agent", status: "ACTIVE" },
          { id: 2, uuid: "550e8400-e29b-41d4-a716-446655440001", name: "Fraud Detection Agent", status: "ACTIVE" },
          { id: 3, uuid: "550e8400-e29b-41d4-a716-446655440002", name: "Customer Service Agent", status: "ACTIVE" },
        ];
      }

      // Cargar alertas
      let alertsList: AgentAlert[] = [];
      try {
        const response = await fetch("/api/governance/agents/alerts");
        if (response.ok) {
          const data = await response.json();
          alertsList = data.items || [];
        }
      } catch (apiError) {
        console.warn("Error al cargar alertas desde API, usando datos mock:", apiError);
      }

      // Usar datos mock (temporalmente para desarrollo)
      if (true || alertsList.length === 0) {
        alertsList = [
          {
            id: 1,
            agentUuid: "550e8400-e29b-41d4-a716-446655440000",
            agentName: "Credit Scoring Agent",
            alertType: "PERFORMANCE_DEGRADATION",
            alertCategory: "PERFORMANCE",
            severity: "HIGH",
            status: "TRIGGERED",
            priority: "HIGH",
            impactLevel: "HIGH",
            urgencyLevel: "HIGH",
            title: "Degradación de Precisión Detectada",
            description: "La precisión del agente ha caído del 95% al 87% en las últimas 24 horas",
            triggeredAt: "2024-01-15T10:30:00",
          },
          {
            id: 2,
            agentUuid: "550e8400-e29b-41d4-a716-446655440001",
            agentName: "Fraud Detection Agent",
            alertType: "SECURITY_THREAT",
            alertCategory: "SECURITY",
            severity: "CRITICAL",
            status: "ACKNOWLEDGED",
            priority: "CRITICAL",
            impactLevel: "CRITICAL",
            urgencyLevel: "CRITICAL",
            title: "Intento de Acceso No Autorizado",
            description: "Se detectaron múltiples intentos de acceso no autorizado al agente",
            triggeredAt: "2024-01-15T09:15:00",
            acknowledgedAt: "2024-01-15T09:20:00",
            acknowledgedBy: "security@codeflowx.com",
          },
          {
            id: 3,
            agentUuid: "550e8400-e29b-41d4-a716-446655440002",
            agentName: "Customer Service Agent",
            alertType: "ERROR_RATE_INCREASE",
            alertCategory: "ERROR",
            severity: "MEDIUM",
            status: "RESOLVED",
            priority: "MEDIUM",
            impactLevel: "MEDIUM",
            urgencyLevel: "MEDIUM",
            title: "Aumento de Tasa de Errores",
            description: "La tasa de errores ha aumentado del 2% al 8% en la última hora",
            triggeredAt: "2024-01-15T08:00:00",
            resolvedAt: "2024-01-15T08:30:00",
            resolvedBy: "ops@codeflowx.com",
          },
          {
            id: 4,
            agentUuid: "550e8400-e29b-41d4-a716-446655440000",
            agentName: "Credit Scoring Agent",
            alertType: "LOW_CONFIDENCE",
            alertCategory: "QUALITY",
            severity: "LOW",
            status: "TRIGGERED",
            priority: "LOW",
            impactLevel: "LOW",
            urgencyLevel: "LOW",
            title: "Baja Confianza en Decisiones",
            description: "El agente está mostrando confianza promedio del 65%, por debajo del umbral del 70%",
            triggeredAt: "2024-01-15T11:45:00",
          },
        ];
      }

      // Hacer match de agentes con alertas
      const alertsWithAgents = alertsList.map((alert: AgentAlert) => {
        if (alert.agentUuid) {
          const agent = agentsList.find((a) => a.uuid === alert.agentUuid || a.id.toString() === alert.agentUuid);
          if (agent) {
            return { ...alert, agentName: agent.name };
          }
        }
        return alert;
      });

      setAlerts(alertsWithAgents);
      setTotalItems(alertsWithAgents.length);
      setActiveItems(alertsWithAgents.filter((a: AgentAlert) => a.status === "TRIGGERED" || a.status === "ACKNOWLEDGED").length);
      setPendingItems(alertsWithAgents.filter((a: AgentAlert) => a.status === "TRIGGERED").length);
    } catch (error) {
      console.error("Error loading alerts:", error);
    } finally {
      setLoading(false);
    }
  };

  const filteredAlerts = alerts.filter((alert) => {
    const searchLower = searchTerm.toLowerCase();
    return (
      (alert.title && alert.title.toLowerCase().includes(searchLower)) ||
      (alert.agentName && alert.agentName.toLowerCase().includes(searchLower)) ||
      (alert.alertType && alert.alertType.toLowerCase().includes(searchLower)) ||
      (alert.alertCategory && alert.alertCategory.toLowerCase().includes(searchLower)) ||
      (alert.description && alert.description.toLowerCase().includes(searchLower))
    );
  });

  if (!mounted || loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <AlertTriangle className="w-8 h-8 animate-spin mx-auto mb-2 text-muted-foreground" />
          <p className="text-muted-foreground">{t("common.loading", "Cargando...")}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-4">
      {/* Header - Título alineado a la izquierda */}
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <AlertTriangle className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("agents.alerts.title", "Alertas de Agentes")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("agents.alerts.subtitle", "Gestión de alertas de agentes")}
          </p>
        </div>
        {/* Las alertas se generan automáticamente - No se crean manualmente */}
      </div>

      {/* Estadísticas */}
      <div className="grid grid-cols-4 gap-4">
        <Card className="border-primary hover:shadow-lg hover:border-primary/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("common.total", "Total")}
                </p>
                <h2 className="text-2xl font-bold text-primary mb-1">
                  {totalItems}
                </h2>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card className="border-red-500 hover:shadow-lg hover:border-red-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("agents.alerts.triggered", "Disparadas")}
                </p>
                <h2 className="text-2xl font-bold text-red-600 mb-1">
                  {alerts.filter((a) => a.status === "TRIGGERED").length}
                </h2>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card className="border-yellow-500 hover:shadow-lg hover:border-yellow-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("agents.alerts.acknowledged", "Reconocidas")}
                </p>
                <h2 className="text-2xl font-bold text-yellow-600 mb-1">
                  {alerts.filter((a) => a.status === "ACKNOWLEDGED").length}
                </h2>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card className="border-green-500 hover:shadow-lg hover:border-green-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("agents.alerts.resolved", "Resueltas")}
                </p>
                <h2 className="text-2xl font-bold text-green-600 mb-1">
                  {alerts.filter((a) => a.status === "RESOLVED").length}
                </h2>
              </div>
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Filtros */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50">
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

      {/* Tabla */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50">
        <CardHeader className="pb-3">
          <CardTitle className="text-base font-medium">{t("agents.alerts.list", "Listado de Alertas")}</CardTitle>
        </CardHeader>
        <CardBody className="pt-0">
          {filteredAlerts.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("common.noResults", "No se encontraron resultados")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border">
                    <th className="text-left p-2">{t("common.id", "ID")}</th>
                    <th className="text-left p-2">{t("agents.alerts.agent", "Agente")}</th>
                    <th className="text-left p-2">{t("agents.alerts.title", "Título")}</th>
                    <th className="text-left p-2">{t("agents.alerts.alertType", "Tipo")}</th>
                    <th className="text-left p-2">{t("agents.alerts.alertCategory", "Categoría")}</th>
                    <th className="text-left p-2">{t("agents.alerts.severity", "Severidad")}</th>
                    <th className="text-left p-2">{t("common.status", "Estado")}</th>
                    <th className="text-left p-2">{t("agents.alerts.triggeredAt", "Disparada")}</th>
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
                      <td className="p-2">{alert.agentName || alert.agentUuid || "-"}</td>
                      <td className="p-2 font-medium">{alert.title}</td>
                      <td className="p-2">
                        <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                          {alert.alertType}
                        </span>
                      </td>
                      <td className="p-2">
                        {alert.alertCategory ? (
                          <span className="px-2 py-1 rounded text-xs bg-blue-500/20 text-blue-500">
                            {alert.alertCategory}
                          </span>
                        ) : (
                          "-"
                        )}
                      </td>
                      <td className="p-2">
                        <span
                          className={`px-2 py-1 rounded text-xs ${
                            alert.severity === "CRITICAL"
                              ? "bg-red-500/20 text-red-500"
                              : alert.severity === "HIGH"
                              ? "bg-orange-500/20 text-orange-500"
                              : alert.severity === "MEDIUM"
                              ? "bg-yellow-500/20 text-yellow-500"
                              : "bg-gray-500/20 text-gray-500"
                          }`}
                        >
                          {alert.severity}
                        </span>
                      </td>
                      <td className="p-2">
                        <span
                          className={`px-2 py-1 rounded text-xs ${
                            alert.status === "RESOLVED"
                              ? "bg-green-500/20 text-green-500"
                              : alert.status === "ACKNOWLEDGED"
                              ? "bg-blue-500/20 text-blue-500"
                              : alert.status === "TRIGGERED"
                              ? "bg-red-500/20 text-red-500"
                              : "bg-yellow-500/20 text-yellow-500"
                          }`}
                        >
                          {alert.status}
                        </span>
                      </td>
                      <td className="p-2">
                        {alert.triggeredAt
                          ? new Date(alert.triggeredAt).toLocaleDateString("es-ES", {
                              year: "numeric",
                              month: "2-digit",
                              day: "2-digit",
                              hour: "2-digit",
                              minute: "2-digit",
                            })
                          : "-"}
                      </td>
                      <td className="p-2">
                        <div className="flex gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            className="h-8 w-8 p-0"
                            onClick={() => router.push(`/governance/agents/alerts/${alert.id}`)}
                            title={t("agents.alerts.view", "Ver Alerta")}
                          >
                            <Eye className="w-4 h-4" />
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
  );
}
