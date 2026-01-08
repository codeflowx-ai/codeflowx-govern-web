"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Play, Plus, Search, Eye, Trash2, X } from "lucide-react";
import { useState, useEffect } from "react";

interface TrainingExecution {
  id: number;
  trainingJobId: string;
  executionNumber: number;
  executionType: string;
  status: string;
  startedAt: string;
}

const mockExecutions: TrainingExecution[] = [
  {
    id: 1,
    trainingJobId: "TJ-001",
    executionNumber: 1,
    executionType: "TRAINING",
    status: "ACTIVE",
    startedAt: "2024-01-15 10:30:00",
  },
  {
    id: 2,
    trainingJobId: "TJ-002",
    executionNumber: 2,
    executionType: "VALIDATION",
    status: "COMPLETED",
    startedAt: "2024-01-16 14:20:00",
  },
];

export default function TrainingExecutionOverviewPage() {
  const { t } = useTranslation();
  const [executions, setExecutions] = useState<TrainingExecution[]>(mockExecutions);
  const [searchTerm, setSearchTerm] = useState("");
  const [typeFilter, setTypeFilter] = useState<string>("ALL");
  const [statusFilter, setStatusFilter] = useState<string>("ALL");

  const [metrics, setMetrics] = useState({
    totalItems: 0,
    activeItems: 0,
    pendingApproval: 0,
    inactiveItems: 0,
  });

  useEffect(() => {
    setMetrics({
      totalItems: executions.length,
      activeItems: executions.filter((e) => e.status === "ACTIVE").length,
      pendingApproval: executions.filter((e) => e.status === "PENDING").length,
      inactiveItems: executions.filter((e) => e.status === "INACTIVE").length,
    });
  }, [executions]);

  const filteredExecutions = executions.filter((execution) => {
    const matchesSearch =
      execution.trainingJobId.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesType = typeFilter === "ALL" || execution.executionType === typeFilter;
    const matchesStatus = statusFilter === "ALL" || execution.status === statusFilter;
    return matchesSearch && matchesType && matchesStatus;
  });

  const getStatusBadge = (status: string) => {
    const colors: Record<string, string> = {
      ACTIVE: "bg-green-500/20 text-green-500 border-green-500/50",
      COMPLETED: "bg-blue-500/20 text-blue-500 border-blue-500/50",
      PENDING: "bg-yellow-500/20 text-yellow-500 border-yellow-500/50",
      INACTIVE: "bg-gray-500/20 text-gray-500 border-gray-500/50",
    };
    return colors[status] || "bg-gray-500/20 text-gray-500 border-gray-500/50";
  };

  const clearFilters = () => {
    setSearchTerm("");
    setTypeFilter("ALL");
    setStatusFilter("ALL");
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
            <Play className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("training.execution.overview.title", "Training Execution")}
            </h1>
          </div>
          <Button>
            <Plus className="w-4 h-4 mr-2" />
            {t("training.execution.overview.register", "Registrar Training Execution")}
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
              <CardTitle className="text-sm font-medium ">Inactivos</CardTitle>
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
                value={typeFilter}
                onChange={(e) => setTypeFilter(e.target.value)}
                className="px-3 py-2 rounded-md border border-border bg-background text-foreground"
              >
                <option value="ALL">{t("common.all", "TODOS")}</option>
                <option value="TRAINING">TRAINING</option>
                <option value="VALIDATION">VALIDATION</option>
              </select>
              <select
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value)}
                className="px-3 py-2 rounded-md border border-border bg-background text-foreground"
              >
                <option value="ALL">{t("common.all", "TODOS")}</option>
                <option value="ACTIVE">ACTIVE</option>
                <option value="COMPLETED">COMPLETED</option>
                <option value="PENDING">PENDING</option>
                <option value="INACTIVE">INACTIVE</option>
              </select>
              {(searchTerm || typeFilter !== "ALL" || statusFilter !== "ALL") && (
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
            <CardTitle>{t("training.execution.overview.list", "Lista de Executions")}</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border">
                    <th className="text-left p-4">{t("common.id", "ID")}</th>
                    <th className="text-left p-4">{t("training.execution.overview.trainingJobId", "Training Job ID")}</th>
                    <th className="text-left p-4">{t("training.execution.overview.executionNumber", "Execution Number")}</th>
                    <th className="text-left p-4">{t("training.execution.overview.executionType", "Execution Type")}</th>
                    <th className="text-left p-4">{t("training.execution.overview.status", "Status")}</th>
                    <th className="text-left p-4">{t("training.execution.overview.startedAt", "Started At")}</th>
                    <th className="text-left p-4">{t("common.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredExecutions.map((execution) => (
                    <tr key={execution.id} className="border-b border-border/50 hover:bg-background/50 transition-colors">
                      <td className="p-4">{execution.id}</td>
                      <td className="p-4">{execution.trainingJobId}</td>
                      <td className="p-4">{execution.executionNumber}</td>
                      <td className="p-4">{execution.executionType}</td>
                      <td className="p-4">
                        <Badge className={getStatusBadge(execution.status)}>
                          {execution.status}
                        </Badge>
                      </td>
                      <td className="p-4 text-sm text-muted-foreground">{execution.startedAt}</td>
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
              {filteredExecutions.length === 0 && (
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


