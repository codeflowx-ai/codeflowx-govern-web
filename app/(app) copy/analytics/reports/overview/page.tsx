"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Input } from "@/components/ui/input";
import {
  FileText,
  Plus,
  Search,
  Trash2,
  Edit,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useState } from "react";

interface AnalyticsReport {
  idxanalyticsreport: number;
  anlreportname: string;
  anlreporttype: string;
  anlreportdescription: string;
  anlreportdata: string;
  anlreportconfig: string;
  anlstatus?: string;
}

const mockReports: AnalyticsReport[] = [
  {
    idxanalyticsreport: 1,
    anlreportname: "Daily Performance Report",
    anlreporttype: "TYPE1",
    anlreportdescription: "Reporte diario de rendimiento del sistema",
    anlreportdata: "JSON_DATA_1",
    anlreportconfig: "CONFIG_1",
    anlstatus: "ACTIVE",
  },
  {
    idxanalyticsreport: 2,
    anlreportname: "Weekly Analytics Summary",
    anlreporttype: "TYPE2",
    anlreportdescription: "Resumen semanal de analytics",
    anlreportdata: "JSON_DATA_2",
    anlreportconfig: "CONFIG_2",
    anlstatus: "ACTIVE",
  },
  {
    idxanalyticsreport: 3,
    anlreportname: "Monthly Trends Analysis",
    anlreporttype: "TYPE1",
    anlreportdescription: "Análisis de tendencias mensuales",
    anlreportdata: "JSON_DATA_3",
    anlreportconfig: "CONFIG_3",
    anlstatus: "PENDING",
  },
  {
    idxanalyticsreport: 4,
    anlreportname: "Error Log Report",
    anlreporttype: "TYPE2",
    anlreportdescription: "Reporte de errores del sistema",
    anlreportdata: "JSON_DATA_4",
    anlreportconfig: "CONFIG_4",
    anlstatus: "INACTIVE",
  },
];

export default function AnalyticsReportsOverviewPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [searchTerm, setSearchTerm] = useState("");
  const [typeFilter, setTypeFilter] = useState<string>("all");
  const [statusFilter, setStatusFilter] = useState<string>("all");

  const filteredReports = mockReports.filter((report) => {
    const matchesSearch =
      !searchTerm ||
      report.anlreportname.toLowerCase().includes(searchTerm.toLowerCase()) ||
      report.anlreportdescription.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesType = typeFilter === "all" || report.anlreporttype === typeFilter;
    const matchesStatus =
      statusFilter === "all" || report.anlstatus === statusFilter;

    return matchesSearch && matchesType && matchesStatus;
  });

  const totalItems = mockReports.length;
  const activeItems = mockReports.filter((r) => r.anlstatus === "ACTIVE").length;
  const pendingApproval = mockReports.filter(
    (r) => r.anlstatus === "PENDING"
  ).length;
  const inactiveItems = mockReports.filter(
    (r) => r.anlstatus === "INACTIVE"
  ).length;

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
              <FileText className="w-8 h-8 text-primary" />
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("analytics.reports.overview.title", "Analytics Reports")}
              </h1>
            </div>
            <p className="text-muted-foreground">
              {t(
                "analytics.reports.overview.subtitle",
                "Gestión de Analytics Reports"
              )}
            </p>
          </div>
          <Button
            onClick={() => router.push("/analytics/reports/detail")}
            className="gap-2"
          >
            <Plus className="w-4 h-4" />
            {t("analytics.reports.overview.register", "Registrar Reporte")}
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
                  {t("analytics.reports.filter.allTypes", "Todos los Tipos")}
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
                  {t("analytics.reports.filter.allStatus", "Todos los Estados")}
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

        {/* Tabla de Reportes */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle>
              {t("analytics.reports.overview.list", "Listado de Reportes")}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border/50">
                    <th className="text-left p-4 font-semibold">ID</th>
                    <th className="text-left p-4 font-semibold">
                      {t("analytics.reports.name", "Nombre")}
                    </th>
                    <th className="text-left p-4 font-semibold">
                      {t("analytics.reports.type", "Tipo")}
                    </th>
                    <th className="text-left p-4 font-semibold">
                      {t("analytics.reports.description", "Descripción")}
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
                  {filteredReports.map((report) => (
                    <tr
                      key={report.idxanalyticsreport}
                      className="border-b border-border/30 hover:bg-background/50 transition-colors"
                    >
                      <td className="p-4">{report.idxanalyticsreport}</td>
                      <td className="p-4 font-medium">{report.anlreportname}</td>
                      <td className="p-4">
                        <Badge variant="outline">{report.anlreporttype}</Badge>
                      </td>
                      <td className="p-4 text-sm text-muted-foreground max-w-xs truncate">
                        {report.anlreportdescription}
                      </td>
                      <td className="p-4">
                        <Badge
                          variant="outline"
                          className={
                            report.anlstatus === "ACTIVE"
                              ? "bg-green-500/10 text-green-400 border-green-500/50"
                              : report.anlstatus === "PENDING"
                              ? "bg-yellow-500/10 text-yellow-400 border-yellow-500/50"
                              : "bg-gray-500/10 text-gray-400 border-gray-500/50"
                          }
                        >
                          {report.anlstatus || t("common.unknown", "Desconocido")}
                        </Badge>
                      </td>
                      <td className="p-4">
                        <div className="flex gap-2">
                          <button
                            className="p-2 hover:bg-primary/10 rounded transition-colors"
                            onClick={() =>
                              router.push(
                                `/analytics/reports/detail?id=${report.idxanalyticsreport}`
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


