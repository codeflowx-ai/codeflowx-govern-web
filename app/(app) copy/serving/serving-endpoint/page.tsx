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
import { Plus, Search, Globe, Edit, Trash2, Activity, Copy, ExternalLink } from "lucide-react";
import Link from "next/link";
import { useState } from "react";

export default function ServingEndpointPage() {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState("");
  const [methodFilter, setMethodFilter] = useState("ALL");
  const [statusFilter, setStatusFilter] = useState("ALL");

  // Mock data
  const [data] = useState([
    {
      id: 1,
      name: "GPT-4 API",
      path: "/api/v1/gpt4",
      method: "POST",
      type: "REST",
      deploymentId: "DEP-001",
      deploymentName: "GPT-4 Production",
      status: "ACTIVE",
      requests: 1247,
      latency: 45,
      errorRate: 0.1,
      successRate: 99.9,
    },
    {
      id: 2,
      name: "Claude API",
      path: "/api/v1/claude",
      method: "POST",
      type: "REST",
      deploymentId: "DEP-002",
      deploymentName: "Claude-3 Staging",
      status: "ACTIVE",
      requests: 892,
      latency: 52,
      errorRate: 0.2,
      successRate: 99.8,
    },
    {
      id: 3,
      name: "Embeddings API",
      path: "/api/v1/embeddings",
      method: "POST",
      type: "REST",
      deploymentId: "DEP-004",
      deploymentName: "Embeddings-v1 Production",
      status: "ACTIVE",
      requests: 2156,
      latency: 38,
      errorRate: 0.05,
      successRate: 99.95,
    },
    {
      id: 4,
      name: "Health Check",
      path: "/api/v1/health",
      method: "GET",
      type: "REST",
      deploymentId: "DEP-001",
      deploymentName: "GPT-4 Production",
      status: "ACTIVE",
      requests: 5432,
      latency: 5,
      errorRate: 0,
      successRate: 100,
    },
  ]);

  const metrics = {
    total: data.length,
    active: data.filter((d) => d.status === "ACTIVE").length,
    inactive: data.filter((d) => d.status === "INACTIVE").length,
    totalRequests: data.reduce((sum, d) => sum + d.requests, 0),
    avgLatency: Math.round(data.reduce((sum, d) => sum + d.latency, 0) / data.length),
  };

  const getStatusBadge = (status: string) => {
    const variants: Record<string, string> = {
      ACTIVE: "primary",
      INACTIVE: "secondary",
      PENDING: "outline",
    };
    return (
      <Badge variant={variants[status] as any}>
        {status}
      </Badge>
    );
  };

  const getMethodBadge = (method: string) => {
    const colors: Record<string, string> = {
      GET: "bg-green-500",
      POST: "bg-blue-500",
      PUT: "bg-yellow-500",
      DELETE: "bg-red-500",
    };
    return (
      <Badge variant="outline" className={colors[method] || ""}>
        {method}
      </Badge>
    );
  };

  const copyToClipboard = (text: string) => {
    navigator.clipboard.writeText(text);
  };

  return (
    <div className="space-y-4 p-6">
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <Globe className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("serving.endpoint.title", "Endpoints de Serving")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("serving.endpoint.subtitle", "Gestión de endpoints para acceso a modelos")}
          </p>
        </div>
        <Button className="gap-2">
          <Plus className="w-4 h-4" />
          {t("serving.endpoint.newEndpoint", "Nuevo Endpoint")}
        </Button>
      </div>

      {/* Metrics Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
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
            <p className="text-sm text-muted-foreground mb-2">Requests/min</p>
            <p className="text-3xl font-bold text-blue-500">{metrics.totalRequests.toLocaleString()}</p>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-6">
            <p className="text-sm text-muted-foreground mb-2">Latencia Promedio</p>
            <p className="text-3xl font-bold text-purple-500">{metrics.avgLatency}ms</p>
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
                  placeholder={t("common.search", "Buscar endpoints...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
            <Select value={methodFilter} onValueChange={setMethodFilter}>
              <SelectTrigger className="w-[200px]">
                <SelectValue placeholder="Filtrar por Método" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="ALL">TODOS</SelectItem>
                <SelectItem value="GET">GET</SelectItem>
                <SelectItem value="POST">POST</SelectItem>
                <SelectItem value="PUT">PUT</SelectItem>
                <SelectItem value="DELETE">DELETE</SelectItem>
              </SelectContent>
            </Select>
            <Select value={statusFilter} onValueChange={setStatusFilter}>
              <SelectTrigger className="w-[200px]">
                <SelectValue placeholder="Filtrar por Estado" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="ALL">TODOS</SelectItem>
                <SelectItem value="ACTIVE">ACTIVO</SelectItem>
                <SelectItem value="INACTIVE">INACTIVO</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </CardContent>
      </Card>

      {/* Table */}
      <Card>
        <CardHeader>
          <CardTitle>
            {t("serving.endpoint.list", "Listado de Endpoints")}
          </CardTitle>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Nombre</TableHead>
                <TableHead>Path</TableHead>
                <TableHead>Método</TableHead>
                <TableHead>Tipo</TableHead>
                <TableHead>Deployment</TableHead>
                <TableHead>Métricas</TableHead>
                <TableHead>Estado</TableHead>
                <TableHead className="text-right">Acciones</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {data.map((item) => (
                <TableRow key={item.id}>
                  <TableCell className="font-semibold">{item.name}</TableCell>
                  <TableCell>
                    <div className="flex items-center gap-2">
                      <code className="text-sm bg-muted px-2 py-1 rounded">{item.path}</code>
                      <Button
                        variant="ghost"
                        size="sm"
                        className="h-6 w-6 p-0"
                        onClick={() => copyToClipboard(item.path)}
                      >
                        <Copy className="w-3 h-3" />
                      </Button>
                    </div>
                  </TableCell>
                  <TableCell>{getMethodBadge(item.method)}</TableCell>
                  <TableCell>
                    <Badge variant="outline">{item.type}</Badge>
                  </TableCell>
                  <TableCell>
                    <div className="flex items-center gap-2">
                      <span className="text-sm">{item.deploymentName}</span>
                      <Link href={`/serving/model-deployment?deploymentId=${item.deploymentId}`}>
                        <Button variant="ghost" size="sm" className="h-6 w-6 p-0">
                          <ExternalLink className="w-3 h-3" />
                        </Button>
                      </Link>
                    </div>
                  </TableCell>
                  <TableCell>
                    <div className="flex items-center gap-4 text-sm">
                      <div className="flex items-center gap-1">
                        <Activity className="w-3 h-3 text-muted-foreground" />
                        <span>{item.requests}/min</span>
                      </div>
                      <div>
                        <span>{item.latency}ms</span>
                      </div>
                      <div className="text-green-500">
                        <span>{item.successRate}%</span>
                      </div>
                    </div>
                  </TableCell>
                  <TableCell>{getStatusBadge(item.status)}</TableCell>
                  <TableCell className="text-right">
                    <div className="flex justify-end gap-2">
                      <Button variant="ghost" size="sm">
                        <Edit className="w-4 h-4" />
                      </Button>
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


