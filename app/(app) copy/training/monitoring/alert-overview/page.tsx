"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Bell, Plus, Search, Eye, Trash2, X } from "lucide-react";
import { useState, useEffect } from "react";

interface TrainingAlert {
  id: number;
  name: string;
  severity: string;
  status: string;
  triggeredAt: string;
  resolvedAt?: string;
}

const mockAlerts: TrainingAlert[] = [
  {
    id: 1,
    name: "High Memory Usage",
    severity: "HIGH",
    status: "ACTIVE",
    triggeredAt: "2024-01-15 10:30:00",
  },
  {
    id: 2,
    name: "Training Stalled",
    severity: "MEDIUM",
    status: "RESOLVED",
    triggeredAt: "2024-01-14 15:20:00",
    resolvedAt: "2024-01-14 16:00:00",
  },
];

export default function TrainingAlertOverviewPage() {
  const { t } = useTranslation();
  const [alerts, setAlerts] = useState<TrainingAlert[]>(mockAlerts);
  const [searchTerm, setSearchTerm] = useState("");
  const [severityFilter, setSeverityFilter] = useState<string>("ALL");

  const [metrics, setMetrics] = useState({
    totalItems: 0,
    activeItems: 0,
    pendingApproval: 0,
    inactiveItems: 0,
  });

  useEffect(() => {
    setMetrics({
      totalItems: alerts.length,
      activeItems: alerts.filter((a) => a.status === "ACTIVE").length,
      pendingApproval: 0,
      inactiveItems: alerts.filter((a) => a.status === "RESOLVED").length,
    });
  }, [alerts]);

  const filteredAlerts = alerts.filter((alert) => {
    const matchesSearch = alert.name.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesSeverity = severityFilter === "ALL" || alert.severity === severityFilter;
    return matchesSearch && matchesSeverity;
  });

  const getSeverityBadge = (severity: string) => {
    const colors: Record<string, string> = {
      HIGH: "bg-red-500/20 text-red-500 border-red-500/50",
      MEDIUM: "bg-yellow-500/20 text-yellow-500 border-yellow-500/50",
      LOW: "bg-blue-500/20 text-blue-500 border-blue-500/50",
    };
    return colors[severity] || "bg-gray-500/20 text-gray-500 border-gray-500/50";
  };

  const clearFilters = () => {
    setSearchTerm("");
    setSeverityFilter("ALL");
  };

  return (
    <div className="min-h-screen bg-background relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Bell className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("training.monitoring.alertOverview.title", "Training Alerts")}
            </h1>
          </div>
          <Button>
            <Plus className="w-4 h-4 mr-2" />
            {t("training.monitoring.alertOverview.register", "Registrar Alert")}
          </Button>
        </div>

        {/* Métricas */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <Card className="border-border">
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium ">Total</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold">{metrics.totalItems}</div>
            </CardContent>
          </Card>
          <Card className="border-border">
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium ">Activos</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold">{metrics.activeItems}</div>
            </CardContent>
          </Card>
          <Card className="border-border">
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium ">Pendientes</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold">{metrics.pendingApproval}</div>
            </CardContent>
          </Card>
          <Card className="border-border">
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium ">Resueltos</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold">{metrics.inactiveItems}</div>
            </CardContent>
          </Card>
        </div>

        {/* Filtros */}
        <Card className="border-border">
          <CardContent className="pt-6">
            <div className="flex items-center gap-4 flex-wrap">
              <div className="relative flex-1 min-w-[200px]">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <Input
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
              <select
                value={severityFilter}
                onChange={(e) => setSeverityFilter(e.target.value)}
                className="px-3 py-2 rounded-md border border-border bg-background text-foreground"
              >
                <option value="ALL">{t("common.all", "TODOS")}</option>
                <option value="HIGH">HIGH</option>
                <option value="MEDIUM">MEDIUM</option>
                <option value="LOW">LOW</option>
              </select>
              {(searchTerm || severityFilter !== "ALL") && (
                <Button variant="outline" onClick={clearFilters}>
                  <X className="w-4 h-4 mr-2" />
                  {t("common.clear", "Limpiar")}
                </Button>
              )}
            </div>
          </CardContent>
        </Card>

        {/* Tabla */}
        <Card className="border-border">
          <CardHeader>
            <CardTitle>{t("training.monitoring.alertOverview.list", "Lista de Alerts")}</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border">
                    <th className="text-left p-4">{t("common.id", "ID")}</th>
                    <th className="text-left p-4">{t("training.monitoring.alertOverview.name", "Name")}</th>
                    <th className="text-left p-4">{t("training.monitoring.alertOverview.severity", "Severity")}</th>
                    <th className="text-left p-4">{t("training.monitoring.alertOverview.status", "Status")}</th>
                    <th className="text-left p-4">{t("training.monitoring.alertOverview.triggeredAt", "Triggered At")}</th>
                    <th className="text-left p-4">{t("training.monitoring.alertOverview.resolvedAt", "Resolved At")}</th>
                    <th className="text-left p-4">{t("common.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredAlerts.map((alert) => (
                    <tr key={alert.id} className="border-b border-border/50 hover:bg-background/50 transition-colors">
                      <td className="p-4">{alert.id}</td>
                      <td className="p-4">{alert.name}</td>
                      <td className="p-4">
                        <Badge className={getSeverityBadge(alert.severity)}>
                          {alert.severity}
                        </Badge>
                      </td>
                      <td className="p-4">
                        <Badge className={alert.status === "ACTIVE" ? "bg-red-500/20 text-red-500 border-red-500/50" : "bg-green-500/20 text-green-500 border-green-500/50"}>
                          {alert.status}
                        </Badge>
                      </td>
                      <td className="p-4 text-sm text-muted-foreground">{alert.triggeredAt}</td>
                      <td className="p-4 text-sm text-muted-foreground">{alert.resolvedAt || "-"}</td>
                      <td className="p-4">
                        <div className="flex items-center gap-2">
                          <Button variant="ghost" size="sm">
                            <Eye className="w-4 h-4" />
                          </Button>
                          <Button variant="ghost" size="sm">
                            <Trash2 className="w-4 h-4 text-destructive" />
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              {filteredAlerts.length === 0 && (
                <div className=" py-12 text-muted-foreground">
                  {t("common.noResults", "No se encontraron resultados")}
                </div>
              )}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}


