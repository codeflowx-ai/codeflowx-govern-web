"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { FileText, Plus, Search, Eye, Trash2, X } from "lucide-react";
import { useState, useEffect } from "react";

interface TrainingLog {
  id: number;
  trainingJobId: string;
  logLevel: string;
  message: string;
  timestamp: string;
}

const mockLogs: TrainingLog[] = [
  {
    id: 1,
    trainingJobId: "TJ-001",
    logLevel: "INFO",
    message: "Training started successfully",
    timestamp: "2024-01-15 10:30:00",
  },
  {
    id: 2,
    trainingJobId: "TJ-001",
    logLevel: "WARNING",
    message: "High memory usage detected",
    timestamp: "2024-01-15 10:35:00",
  },
];

export default function TrainingLogOverviewPage() {
  const { t } = useTranslation();
  const [logs, setLogs] = useState<TrainingLog[]>(mockLogs);
  const [searchTerm, setSearchTerm] = useState("");
  const [levelFilter, setLevelFilter] = useState<string>("ALL");

  const [metrics, setMetrics] = useState({
    totalItems: 0,
    activeItems: 0,
    pendingApproval: 0,
    inactiveItems: 0,
  });

  useEffect(() => {
    setMetrics({
      totalItems: logs.length,
      activeItems: logs.filter((l) => l.logLevel === "INFO").length,
      pendingApproval: logs.filter((l) => l.logLevel === "WARNING").length,
      inactiveItems: logs.filter((l) => l.logLevel === "ERROR").length,
    });
  }, [logs]);

  const filteredLogs = logs.filter((log) => {
    const matchesSearch =
      log.message.toLowerCase().includes(searchTerm.toLowerCase()) ||
      log.trainingJobId.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesLevel = levelFilter === "ALL" || log.logLevel === levelFilter;
    return matchesSearch && matchesLevel;
  });

  const getLevelBadge = (level: string) => {
    const colors: Record<string, string> = {
      INFO: "bg-blue-500/20 text-blue-500 border-blue-500/50",
      WARNING: "bg-yellow-500/20 text-yellow-500 border-yellow-500/50",
      ERROR: "bg-red-500/20 text-red-500 border-red-500/50",
      DEBUG: "bg-gray-500/20 text-gray-500 border-gray-500/50",
    };
    return colors[level] || "bg-gray-500/20 text-gray-500 border-gray-500/50";
  };

  const clearFilters = () => {
    setSearchTerm("");
    setLevelFilter("ALL");
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
            <FileText className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("training.execution.logOverview.title", "Training Logs")}
            </h1>
          </div>
          <Button>
            <Plus className="w-4 h-4 mr-2" />
            {t("training.execution.logOverview.register", "Registrar Log")}
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
              <CardTitle className="text-sm font-medium ">Info</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold">{metrics.activeItems}</div>
            </CardContent>
          </Card>
          <Card className="border-border">
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium ">Warnings</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold">{metrics.pendingApproval}</div>
            </CardContent>
          </Card>
          <Card className="border-border">
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium ">Errors</CardTitle>
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
                value={levelFilter}
                onChange={(e) => setLevelFilter(e.target.value)}
                className="px-3 py-2 rounded-md border border-border bg-background text-foreground"
              >
                <option value="ALL">{t("common.all", "TODOS")}</option>
                <option value="INFO">INFO</option>
                <option value="WARNING">WARNING</option>
                <option value="ERROR">ERROR</option>
                <option value="DEBUG">DEBUG</option>
              </select>
              {(searchTerm || levelFilter !== "ALL") && (
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
            <CardTitle>{t("training.execution.logOverview.list", "Lista de Logs")}</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border">
                    <th className="text-left p-4">{t("common.id", "ID")}</th>
                    <th className="text-left p-4">{t("training.execution.logOverview.trainingJobId", "Training Job ID")}</th>
                    <th className="text-left p-4">{t("training.execution.logOverview.logLevel", "Log Level")}</th>
                    <th className="text-left p-4">{t("training.execution.logOverview.message", "Message")}</th>
                    <th className="text-left p-4">{t("training.execution.logOverview.timestamp", "Timestamp")}</th>
                    <th className="text-left p-4">{t("common.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredLogs.map((log) => (
                    <tr key={log.id} className="border-b border-border/50 hover:bg-background/50 transition-colors">
                      <td className="p-4">{log.id}</td>
                      <td className="p-4">{log.trainingJobId}</td>
                      <td className="p-4">
                        <Badge className={getLevelBadge(log.logLevel)}>
                          {log.logLevel}
                        </Badge>
                      </td>
                      <td className="p-4 text-sm">{log.message}</td>
                      <td className="p-4 text-sm text-muted-foreground">{log.timestamp}</td>
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
              {filteredLogs.length === 0 && (
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


