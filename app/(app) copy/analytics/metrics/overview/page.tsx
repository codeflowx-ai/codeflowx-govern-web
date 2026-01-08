"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Input } from "@/components/ui/input";
import {
  BarChart3,
  Plus,
  Search,
  Trash2,
  Edit,
  TrendingUp,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useState } from "react";

interface AnalyticsMetric {
  idxanalyticsmetric: number;
  anlmetricname: string;
  anlmetrictype: string;
  anlmetricvalue: string;
  anlmetricunit: string;
  anlthresholdmin: string;
  anlstatus?: string;
}

const mockMetrics: AnalyticsMetric[] = [
  {
    idxanalyticsmetric: 1,
    anlmetricname: "Response Time",
    anlmetrictype: "TYPE1",
    anlmetricvalue: "150",
    anlmetricunit: "ms",
    anlthresholdmin: "100",
    anlstatus: "ACTIVE",
  },
  {
    idxanalyticsmetric: 2,
    anlmetricname: "Error Rate",
    anlmetrictype: "TYPE2",
    anlmetricvalue: "0.5",
    anlmetricunit: "%",
    anlthresholdmin: "1.0",
    anlstatus: "ACTIVE",
  },
  {
    idxanalyticsmetric: 3,
    anlmetricname: "Throughput",
    anlmetrictype: "TYPE1",
    anlmetricvalue: "1200",
    anlmetricunit: "req/s",
    anlthresholdmin: "1000",
    anlstatus: "PENDING",
  },
  {
    idxanalyticsmetric: 4,
    anlmetricname: "CPU Usage",
    anlmetrictype: "TYPE1",
    anlmetricvalue: "75",
    anlmetricunit: "%",
    anlthresholdmin: "80",
    anlstatus: "ACTIVE",
  },
  {
    idxanalyticsmetric: 5,
    anlmetricname: "Memory Usage",
    anlmetrictype: "TYPE2",
    anlmetricvalue: "60",
    anlmetricunit: "%",
    anlthresholdmin: "85",
    anlstatus: "INACTIVE",
  },
];

export default function AnalyticsMetricsOverviewPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [searchTerm, setSearchTerm] = useState("");
  const [typeFilter, setTypeFilter] = useState<string>("all");
  const [statusFilter, setStatusFilter] = useState<string>("all");

  const filteredMetrics = mockMetrics.filter((metric) => {
    const matchesSearch =
      !searchTerm ||
      metric.anlmetricname.toLowerCase().includes(searchTerm.toLowerCase()) ||
      metric.anlmetricvalue.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesType = typeFilter === "all" || metric.anlmetrictype === typeFilter;
    const matchesStatus =
      statusFilter === "all" || metric.anlstatus === statusFilter;

    return matchesSearch && matchesType && matchesStatus;
  });

  const totalItems = mockMetrics.length;
  const activeItems = mockMetrics.filter((m) => m.anlstatus === "ACTIVE").length;
  const pendingApproval = mockMetrics.filter(
    (m) => m.anlstatus === "PENDING"
  ).length;
  const inactiveItems = mockMetrics.filter(
    (m) => m.anlstatus === "INACTIVE"
  ).length;

  const getStatusBadge = (status?: string) => {
    switch (status) {
      case "ACTIVE":
        return (
          <Badge className="bg-green-500/20 text-green-400 border-green-500/50">
            {t("common.active", "Activo")}
          </Badge>
        );
      case "INACTIVE":
        return (
          <Badge className="bg-gray-500/20 text-gray-400 border-gray-500/50">
            {t("common.inactive", "Inactivo")}
          </Badge>
        );
      case "PENDING":
        return (
          <Badge className="bg-yellow-500/20 text-yellow-400 border-yellow-500/50">
            {t("common.pending", "Pendiente")}
          </Badge>
        );
      default:
        return (
          <Badge variant="outline">
            {status || t("common.unknown", "Desconocido")}
          </Badge>
        );
    }
  };

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
          <div className="space-y-2">
            <div className="flex items-center gap-3">
              <BarChart3 className="w-8 h-8 text-primary" />
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("analytics.metrics.overview.title", "Analytics Metrics")}
              </h1>
            </div>
            <p className="text-muted-foreground">
              {t(
                "analytics.metrics.overview.subtitle",
                "Gestión de Analytics Metrics"
              )}
            </p>
          </div>
          <Button
            onClick={() => router.push("/analytics/metrics/detail")}
            className="gap-2"
          >
            <Plus className="w-4 h-4" />
            {t("analytics.metrics.overview.register", "Registrar Métrica")}
          </Button>
        </div>

        {/* Métricas Resumen */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <Card className="backdrop-blur-md bg-gradient-to-br from-blue-500/20 to-cyan-500/20 border-blue-500/30">
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground mb-2">
                {t("common.total", "Total")}
              </p>
              <h2 className="text-3xl font-bold">{totalItems}</h2>
            </CardContent>
          </Card>
          <Card className="backdrop-blur-md bg-gradient-to-br from-green-500/20 to-emerald-500/20 border-green-500/30">
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground mb-2">
                {t("common.active", "Activos")}
              </p>
              <h2 className="text-3xl font-bold">{activeItems}</h2>
            </CardContent>
          </Card>
          <Card className="backdrop-blur-md bg-gradient-to-br from-yellow-500/20 to-orange-500/20 border-yellow-500/30">
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground mb-2">
                {t("common.pending", "Pendientes")}
              </p>
              <h2 className="text-3xl font-bold">{pendingApproval}</h2>
            </CardContent>
          </Card>
          <Card className="backdrop-blur-md bg-gradient-to-br from-gray-500/20 to-slate-500/20 border-gray-500/30">
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground mb-2">
                {t("common.inactive", "Inactivos")}
              </p>
              <h2 className="text-3xl font-bold">{inactiveItems}</h2>
            </CardContent>
          </Card>
        </div>

        {/* Filtros */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Search className="w-5 h-5" />
              {t("common.search", "Buscar")}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex gap-4 flex-wrap">
              <div className="flex-1 min-w-[200px]">
                <Input
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full"
                />
              </div>
              <select
                value={typeFilter}
                onChange={(e) => setTypeFilter(e.target.value)}
                className="px-4 py-2 rounded-md border border-border bg-background"
              >
                <option value="all">
                  {t("analytics.metrics.filter.allTypes", "Todos los Tipos")}
                </option>
                <option value="TYPE1">Tipo 1</option>
                <option value="TYPE2">Tipo 2</option>
              </select>
              <select
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value)}
                className="px-4 py-2 rounded-md border border-border bg-background"
              >
                <option value="all">
                  {t("analytics.metrics.filter.allStatus", "Todos los Estados")}
                </option>
                <option value="ACTIVE">
                  {t("common.active", "Activo")}
                </option>
                <option value="INACTIVE">
                  {t("common.inactive", "Inactivo")}
                </option>
                <option value="PENDING">
                  {t("common.pending", "Pendiente")}
                </option>
              </select>
            </div>
          </CardContent>
        </Card>

        {/* Tabla de Métricas */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle>
              {t("analytics.metrics.overview.list", "Listado de Métricas")}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border/50">
                    <th className="text-left p-4 font-semibold">ID</th>
                    <th className="text-left p-4 font-semibold">
                      {t("analytics.metrics.name", "Nombre")}
                    </th>
                    <th className="text-left p-4 font-semibold">
                      {t("analytics.metrics.type", "Tipo")}
                    </th>
                    <th className="text-left p-4 font-semibold">
                      {t("analytics.metrics.value", "Valor")}
                    </th>
                    <th className="text-left p-4 font-semibold">
                      {t("analytics.metrics.unit", "Unidad")}
                    </th>
                    <th className="text-left p-4 font-semibold">
                      {t("analytics.metrics.thresholdMin", "Umbral Mín")}
                    </th>
                    <th className="text-left p-4 font-semibold">
                      {t("common.status", "Estado")}
                    </th>
                    <th className="text-left p-4 font-semibold">
                      {t("common.actions", "Acciones")}
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {filteredMetrics.map((metric) => (
                    <tr
                      key={metric.idxanalyticsmetric}
                      className="border-b border-border/30 hover:bg-background/50 transition-colors"
                    >
                      <td className="p-4">{metric.idxanalyticsmetric}</td>
                      <td className="p-4 font-medium">{metric.anlmetricname}</td>
                      <td className="p-4">
                        <Badge variant="outline">{metric.anlmetrictype}</Badge>
                      </td>
                      <td className="p-4">{metric.anlmetricvalue}</td>
                      <td className="p-4">{metric.anlmetricunit}</td>
                      <td className="p-4">{metric.anlthresholdmin}</td>
                      <td className="p-4">{getStatusBadge(metric.anlstatus)}</td>
                      <td className="p-4">
                        <div className="flex gap-2">
                          <button
                            className="p-2 hover:bg-primary/10 rounded transition-colors"
                            onClick={() =>
                              router.push(
                                `/analytics/metrics/detail?id=${metric.idxanalyticsmetric}`
                              )
                            }
                            title={t("common.edit", "Editar")}
                          >
                            <Edit className="w-4 h-4" />
                          </button>
                          <button
                            className="p-2 hover:bg-destructive/10 rounded transition-colors"
                            onClick={() => {
                              // Eliminar
                            }}
                            title={t("common.delete", "Eliminar")}
                          >
                            <Trash2 className="w-4 h-4 text-destructive" />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}


