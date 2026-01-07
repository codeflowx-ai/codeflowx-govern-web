"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Plus, Search, Rocket, Edit, Trash2, Server, Activity } from "lucide-react";
import Link from "next/link";
import { useState } from "react";

export default function ModelDeploymentPage() {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState("ALL");
  const [environmentFilter, setEnvironmentFilter] = useState("ALL");

  // Mock data
  const [data] = useState([
    {
      id: 1,
      deploymentId: "DEP-001",
      modelId: "MOD-001",
      modelName: "GPT-4",
      version: "1.0.0",
      environment: "Production",
      status: "ACTIVE",
      replicas: 3,
      instances: 3,
      requests: 1247,
      latency: 45,
      uptime: 99.9,
    },
    {
      id: 2,
      deploymentId: "DEP-002",
      modelId: "MOD-002",
      modelName: "Claude-3",
      version: "2.1.0",
      environment: "Staging",
      status: "ACTIVE",
      replicas: 2,
      instances: 2,
      requests: 892,
      latency: 52,
      uptime: 99.8,
    },
    {
      id: 3,
      deploymentId: "DEP-003",
      modelId: "MOD-001",
      modelName: "GPT-4",
      version: "1.0.0",
      environment: "Development",
      status: "PENDING",
      replicas: 1,
      instances: 0,
      requests: 0,
      latency: 0,
      uptime: 0,
    },
    {
      id: 4,
      deploymentId: "DEP-004",
      modelId: "MOD-003",
      modelName: "Embeddings-v1",
      version: "1.5.0",
      environment: "Production",
      status: "ACTIVE",
      replicas: 2,
      instances: 2,
      requests: 2156,
      latency: 38,
      uptime: 99.9,
    },
  ]);

  const metrics = {
    total: data.length,
    active: data.filter((d) => d.status === "ACTIVE").length,
    pending: data.filter((d) => d.status === "PENDING").length,
    failed: data.filter((d) => d.status === "FAILED").length,
    totalRequests: data.reduce((sum, d) => sum + d.requests, 0),
  };

  const getStatusBadge = (status: string) => {
    const variants: Record<string, string> = {
      ACTIVE: "primary",
      INACTIVE: "secondary",
      PENDING: "outline",
      FAILED: "danger",
      STOPPED: "secondary",
    };
    return (
      <Badge variant={variants[status] as any}>
        {status}
      </Badge>
    );
  };

  const getEnvironmentBadge = (environment: string) => {
    const colors: Record<string, string> = {
      Production: "bg-green-500",
      Staging: "bg-yellow-500",
      Development: "bg-blue-500",
    };
    return (
      <Badge variant="outline" className={colors[environment] || ""}>
        {environment}
      </Badge>
    );
  };

  return (
    <div className="space-y-4 p-6">
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <Rocket className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("serving.modelDeployment.title", "Deployments de Modelos")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("serving.modelDeployment.subtitle", "Gestión de deployments de modelos")}
          </p>
        </div>
        <Button className="gap-2">
          <Plus className="w-4 h-4" />
          {t("serving.modelDeployment.newDeployment", "Nuevo Deployment")}
        </Button>
      </div>

      {/* Metrics Cards */}
      <div className="grid grid-cols-1 md:grid-cols-5 gap-4">
        <Card>
          <CardContent className="p-6">
            <p className="text-sm text-muted-foreground mb-2">Total</p>
            <p className="text-3xl font-bold">{metrics.total}</p>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-6">
            <p className="text-sm text-muted-foreground mb-2">Activos</p>
            <p className="text-3xl font-bold text-green-500">{metrics.active}</p>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-6">
            <p className="text-sm text-muted-foreground mb-2">Pendientes</p>
            <p className="text-3xl font-bold text-yellow-500">{metrics.pending}</p>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-6">
            <p className="text-sm text-muted-foreground mb-2">Fallidos</p>
            <p className="text-3xl font-bold text-red-500">{metrics.failed}</p>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-6">
            <p className="text-sm text-muted-foreground mb-2">Requests/min</p>
            <p className="text-3xl font-bold text-blue-500">{metrics.totalRequests.toLocaleString()}</p>
          </CardContent>
        </Card>
      </div>

      {/* Filters */}
      <Card>
        <CardContent className="p-4">
          <div className="flex flex-wrap gap-4 items-center">
            <div className="flex-1 min-w-[200px]">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <Input
                  placeholder={t("common.search", "Buscar deployments...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
            <Select value={statusFilter} onValueChange={setStatusFilter}>
              <SelectTrigger className="w-[200px]">
                <SelectValue placeholder="Filtrar por Estado" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="ALL">TODOS</SelectItem>
                <SelectItem value="ACTIVE">ACTIVO</SelectItem>
                <SelectItem value="PENDING">PENDIENTE</SelectItem>
                <SelectItem value="FAILED">FALLIDO</SelectItem>
                <SelectItem value="STOPPED">DETENIDO</SelectItem>
              </SelectContent>
            </Select>
            <Select value={environmentFilter} onValueChange={setEnvironmentFilter}>
              <SelectTrigger className="w-[200px]">
                <SelectValue placeholder="Filtrar por Entorno" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="ALL">TODOS</SelectItem>
                <SelectItem value="Production">Production</SelectItem>
                <SelectItem value="Staging">Staging</SelectItem>
                <SelectItem value="Development">Development</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </CardContent>
      </Card>

      {/* Table */}
      <Card>
        <CardHeader>
          <CardTitle>
            {t("serving.modelDeployment.list", "Listado de Deployments")}
          </CardTitle>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Deployment ID</TableHead>
                <TableHead>Modelo</TableHead>
                <TableHead>Versión</TableHead>
                <TableHead>Entorno</TableHead>
                <TableHead>Instancias</TableHead>
                <TableHead>Métricas</TableHead>
                <TableHead>Estado</TableHead>
                <TableHead className="text-right">Acciones</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {data.map((item) => (
                <TableRow key={item.id}>
                  <TableCell className="font-mono text-sm">{item.deploymentId}</TableCell>
                  <TableCell>
                    <div>
                      <div className="font-semibold">{item.modelName}</div>
                      <div className="text-sm text-muted-foreground">{item.modelId}</div>
                    </div>
                  </TableCell>
                  <TableCell>
                    <Badge variant="outline">{item.version}</Badge>
                  </TableCell>
                  <TableCell>{getEnvironmentBadge(item.environment)}</TableCell>
                  <TableCell>
                    <div className="flex items-center gap-2">
                      <Server className="w-4 h-4 text-muted-foreground" />
                      <span>{item.instances}/{item.replicas}</span>
                    </div>
                  </TableCell>
                  <TableCell>
                    {item.status === "ACTIVE" ? (
                      <div className="flex items-center gap-4 text-sm">
                        <div className="flex items-center gap-1">
                          <Activity className="w-3 h-3 text-muted-foreground" />
                          <span>{item.requests}/min</span>
                        </div>
                        <div>
                          <span>{item.latency}ms</span>
                        </div>
                        <div>
                          <span>{item.uptime}%</span>
                        </div>
                      </div>
                    ) : (
                      <span className="text-muted-foreground">-</span>
                    )}
                  </TableCell>
                  <TableCell>{getStatusBadge(item.status)}</TableCell>
                  <TableCell className="text-right">
                    <div className="flex justify-end gap-2">
                      <Link href={`/serving/deployment-instance?deploymentId=${item.deploymentId}`}>
                        <Button variant="ghost" size="sm">
                          <Edit className="w-4 h-4" />
                        </Button>
                      </Link>
                      <Button variant="ghost" size="sm">
                        <Trash2 className="w-4 h-4" />
                      </Button>
                    </div>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  );
}


