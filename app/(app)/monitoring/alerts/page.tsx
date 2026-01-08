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
import { AlertTriangle, Plus, Search } from "lucide-react";
import Link from "next/link";
import { useState } from "react";

// Mock data
const mockAlerts = [
  {
    idxmonitoringalert: 1,
    monalertname: "High CPU Usage Alert",
    monalerttype: "PERFORMANCE",
    monseverity: "HIGH",
    monstatus: "ACTIVE",
    mondescription: "CPU usage has exceeded 90% threshold",
    montriggeredat: "2024-01-15T10:30:00Z",
  },
  {
    idxmonitoringalert: 2,
    monalertname: "Database Connection Pool Exhausted",
    monalerttype: "RESOURCE",
    monseverity: "MEDIUM",
    monstatus: "ACKNOWLEDGED",
    mondescription: "Connection pool at 95% capacity",
    montriggeredat: "2024-01-15T09:15:00Z",
  },
  {
    idxmonitoringalert: 3,
    monalertname: "Memory Leak Detected",
    monalerttype: "PERFORMANCE",
    monseverity: "HIGH",
    monstatus: "ACTIVE",
    mondescription: "Memory usage continuously increasing",
    montriggeredat: "2024-01-15T08:45:00Z",
  },
  {
    idxmonitoringalert: 4,
    monalertname: "API Response Time Degraded",
    monalerttype: "PERFORMANCE",
    monseverity: "MEDIUM",
    monstatus: "RESOLVED",
    mondescription: "Average response time above threshold",
    montriggeredat: "2024-01-14T16:20:00Z",
  },
];

export default function MonitoringAlertsOverviewPage() {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState("");
  const [typeFilter, setTypeFilter] = useState("ALL");
  const [severityFilter, setSeverityFilter] = useState("ALL");
  const [statusFilter, setStatusFilter] = useState("ALL");

  const metrics = {
    total: mockAlerts.length,
    active: mockAlerts.filter((a) => a.monstatus === "ACTIVE").length,
    acknowledged: mockAlerts.filter((a) => a.monstatus === "ACKNOWLEDGED").length,
    resolved: mockAlerts.filter((a) => a.monstatus === "RESOLVED").length,
  };

  const getStatusBadge = (status: string) => {
    const variants: Record<string, "primary" | "danger" | "secondary"> = {
      ACTIVE: "danger",
      ACKNOWLEDGED: "primary",
      RESOLVED: "secondary",
    };
    return variants[status] || "primary";
  };

  const getSeverityBadge = (severity: string) => {
    const variants: Record<string, "primary" | "danger" | "outline"> = {
      HIGH: "danger",
      MEDIUM: "primary",
      LOW: "outline",
    };
    return variants[severity] || "outline";
  };

  const filteredAlerts = mockAlerts.filter((alert) => {
    const matchesSearch =
      alert.monalertname.toLowerCase().includes(searchTerm.toLowerCase()) ||
      alert.mondescription.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesType = typeFilter === "ALL" || alert.monalerttype === typeFilter;
    const matchesSeverity =
      severityFilter === "ALL" || alert.monseverity === severityFilter;
    const matchesStatus = statusFilter === "ALL" || alert.monstatus === statusFilter;

    return matchesSearch && matchesType && matchesSeverity && matchesStatus;
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
            <AlertTriangle className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold">
                {t("monitoring.alerts.overview.title", "Alertas de Monitoreo")}
              </h1>
              <p className="text-muted-foreground">
                {t(
                  "monitoring.alerts.overview.description",
                  "Gestión de alertas de monitoreo"
                )}
              </p>
            </div>
          </div>
          <Link href="/monitoring/alerts/response-form">
            <Button className="gap-2">
              <Plus className="w-4 h-4" />
              {t("monitoring.alerts.overview.register", "Registrar Alerta")}
            </Button>
          </Link>
        </div>

        {/* Métricas */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <Card>
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground mb-2">
                {t("monitoring.alerts.overview.total", "Total")}
              </p>
              <p className="text-3xl font-bold">{metrics.total}</p>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground mb-2">
                {t("monitoring.alerts.overview.active", "Activas")}
              </p>
              <p className="text-3xl font-bold">{metrics.active}</p>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground mb-2">
                {t("monitoring.alerts.overview.acknowledged", "Reconocidas")}
              </p>
              <p className="text-3xl font-bold">{metrics.acknowledged}</p>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground mb-2">
                {t("monitoring.alerts.overview.resolved", "Resueltas")}
              </p>
              <p className="text-3xl font-bold">{metrics.resolved}</p>
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
                </SelectContent>
              </Select>

              <Select value={severityFilter} onValueChange={setSeverityFilter}>
                <SelectTrigger className="w-[180px]">
                  <SelectValue placeholder="Severidad" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">Todas las severidades</SelectItem>
                  <SelectItem value="HIGH">Alta</SelectItem>
                  <SelectItem value="MEDIUM">Media</SelectItem>
                  <SelectItem value="LOW">Baja</SelectItem>
                </SelectContent>
              </Select>

              <Select value={statusFilter} onValueChange={setStatusFilter}>
                <SelectTrigger className="w-[180px]">
                  <SelectValue placeholder="Estado" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">Todos los estados</SelectItem>
                  <SelectItem value="ACTIVE">Activa</SelectItem>
                  <SelectItem value="ACKNOWLEDGED">Reconocida</SelectItem>
                  <SelectItem value="RESOLVED">Resuelta</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </CardContent>
        </Card>

        {/* Tabla de Alertas */}
        <Card>
          <CardHeader>
            <CardTitle>
              {t("monitoring.alerts.overview.list", "Lista de Alertas")}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>
                    {t("monitoring.alerts.overview.alertName", "Nombre")}
                  </TableHead>
                  <TableHead>
                    {t("monitoring.alerts.overview.alertType", "Tipo")}
                  </TableHead>
                  <TableHead>
                    {t("monitoring.alerts.overview.severity", "Severidad")}
                  </TableHead>
                  <TableHead>
                    {t("monitoring.alerts.overview.status", "Estado")}
                  </TableHead>
                  <TableHead>
                    {t("monitoring.alerts.overview.triggeredAt", "Activada")}
                  </TableHead>
                  <TableHead>{t("common.actions", "Acciones")}</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {filteredAlerts.map((alert) => (
                  <TableRow key={alert.idxmonitoringalert}>
                    <TableCell className="font-medium">
                      {alert.monalertname}
                    </TableCell>
                    <TableCell>{alert.monalerttype}</TableCell>
                    <TableCell>
                      <Badge variant={getSeverityBadge(alert.monseverity)}>
                        {alert.monseverity}
                      </Badge>
                    </TableCell>
                    <TableCell>
                      <Badge variant={getStatusBadge(alert.monstatus)}>
                        {alert.monstatus}
                      </Badge>
                    </TableCell>
                    <TableCell>{formatDate(alert.montriggeredat)}</TableCell>
                    <TableCell>
                      <Link href={`/monitoring/alerts/${alert.idxmonitoringalert}`}>
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
