"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { BarChart3, Plus, Search } from "lucide-react";
import Link from "next/link";
import { useState } from "react";

// Mock data
const mockMetrics = [
  {
    idxmonitoringmetric: 1,
    monmetricname: "API Latency",
    monmetrictype: "PERFORMANCE",
    monmetricvalue: "145",
    monstatus: "ACTIVE",
    moncollectedat: "2024-01-15T10:30:00Z",
    monunit: "ms",
  },
  {
    idxmonitoringmetric: 2,
    monmetricname: "CPU Usage",
    monmetrictype: "RESOURCE",
    monmetricvalue: "78.5",
    monstatus: "ACTIVE",
    moncollectedat: "2024-01-15T10:30:00Z",
    monunit: "%",
  },
  {
    idxmonitoringmetric: 3,
    monmetricname: "Memory Usage",
    monmetrictype: "RESOURCE",
    monmetricvalue: "65.2",
    monstatus: "ACTIVE",
    moncollectedat: "2024-01-15T10:30:00Z",
    monunit: "%",
  },
  {
    idxmonitoringmetric: 4,
    monmetricname: "Request Throughput",
    monmetrictype: "PERFORMANCE",
    monmetricvalue: "1250",
    monstatus: "ACTIVE",
    moncollectedat: "2024-01-15T10:30:00Z",
    monunit: "req/s",
  },
];

export default function MonitoringMetricsOverviewPage() {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState("");
  const [typeFilter, setTypeFilter] = useState("ALL");
  const [statusFilter, setStatusFilter] = useState("ALL");

  const metrics = {
    total: mockMetrics.length,
    active: mockMetrics.filter((m) => m.monstatus === "ACTIVE").length,
    anomalous: 1,
  };

  const getStatusBadge = (status: string) => {
    const variants: Record<string, "primary" | "secondary"> = {
      ACTIVE: "primary",
      INACTIVE: "secondary",
    };
    return variants[status] || "secondary";
  };

  const filteredMetrics = mockMetrics.filter((metric) => {
    const matchesSearch =
      metric.monmetricname.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesType = typeFilter === "ALL" || metric.monmetrictype === typeFilter;
    const matchesStatus = statusFilter === "ALL" || metric.monstatus === statusFilter;

    return matchesSearch && matchesType && matchesStatus;
  });

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleString("es-ES");
  };

  return (
    <div className="min-h-screen bg-background">
      <div className="w-full px-4 py-6 space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <BarChart3 className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold">
                {t("monitoring.metrics.overview.title", "Métricas de Monitoreo")}
              </h1>
              <p className="text-muted-foreground">
                {t(
                  "monitoring.metrics.overview.description",
                  "Gestión de métricas técnicas de rendimiento"
                )}
              </p>
            </div>
          </div>
        </div>

        {/* Métricas */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card>
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground mb-2">
                {t("monitoring.metrics.overview.total", "Total")}
              </p>
              <p className="text-3xl font-bold">{metrics.total}</p>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground mb-2">
                {t("monitoring.metrics.overview.active", "Activas")}
              </p>
              <p className="text-3xl font-bold">{metrics.active}</p>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground mb-2">
                {t("monitoring.metrics.overview.anomalous", "Anómalas")}
              </p>
              <p className="text-3xl font-bold">{metrics.anomalous}</p>
            </CardContent>
          </Card>
        </div>

        {/* Filtros */}
        <Card>
          <CardContent className="p-4">
            <div className="flex flex-wrap gap-4 items-center">
              <div className="flex-1 min-w-[200px]">
                <div className="relative">
                  <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                  <Input
                    placeholder={t("common.search", "Buscar...")}
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="pl-10"
                  />
                </div>
              </div>

              <Select value={typeFilter} onValueChange={setTypeFilter}>
                <SelectTrigger className="w-[180px]">
                  <SelectValue placeholder="Tipo" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">Todos los tipos</SelectItem>
                  <SelectItem value="PERFORMANCE">Performance</SelectItem>
                  <SelectItem value="RESOURCE">Recurso</SelectItem>
                  <SelectItem value="AVAILABILITY">Disponibilidad</SelectItem>
                  <SelectItem value="ERROR_RATE">Tasa de Error</SelectItem>
                </SelectContent>
              </Select>

              <Select value={statusFilter} onValueChange={setStatusFilter}>
                <SelectTrigger className="w-[180px]">
                  <SelectValue placeholder="Estado" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">Todos los estados</SelectItem>
                  <SelectItem value="ACTIVE">Activa</SelectItem>
                  <SelectItem value="INACTIVE">Inactiva</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </CardContent>
        </Card>

        {/* Tabla de Métricas */}
        <Card>
          <CardHeader>
            <CardTitle>
              {t("monitoring.metrics.overview.list", "Lista de Métricas")}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>
                    {t("monitoring.metrics.overview.metricName", "Nombre")}
                  </TableHead>
                  <TableHead>
                    {t("monitoring.metrics.overview.metricType", "Tipo")}
                  </TableHead>
                  <TableHead>
                    {t("monitoring.metrics.overview.metricValue", "Valor")}
                  </TableHead>
                  <TableHead>
                    {t("monitoring.metrics.overview.status", "Estado")}
                  </TableHead>
                  <TableHead>
                    {t("monitoring.metrics.overview.collectedAt", "Recolectada")}
                  </TableHead>
                  <TableHead>{t("common.actions", "Acciones")}</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {filteredMetrics.map((metric) => (
                  <TableRow key={metric.idxmonitoringmetric}>
                    <TableCell className="font-medium">
                      {metric.monmetricname}
                    </TableCell>
                    <TableCell>{metric.monmetrictype}</TableCell>
                    <TableCell>
                      {metric.monmetricvalue} {metric.monunit}
                    </TableCell>
                    <TableCell>
                      <Badge variant={getStatusBadge(metric.monstatus)}>
                        {metric.monstatus}
                      </Badge>
                    </TableCell>
                    <TableCell>{formatDate(metric.moncollectedat)}</TableCell>
                    <TableCell>
                      <Link href={`/monitoring/metrics/${metric.idxmonitoringmetric}`}>
                        <Button variant="outline" size="sm">
                          {t("common.details", "Detalles")}
                        </Button>
                      </Link>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}


