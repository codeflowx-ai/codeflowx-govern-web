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
import { Plus, Search, Brain, Edit, Trash2, ExternalLink } from "lucide-react";
import Link from "next/link";
import { useState } from "react";

export default function ServingModelPage() {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState("");
  const [typeFilter, setTypeFilter] = useState("ALL");
  const [statusFilter, setStatusFilter] = useState("ALL");

  // Mock data
  const [data] = useState([
    {
      id: 1,
      modelId: "MOD-001",
      name: "GPT-4",
      displayName: "GPT-4 Base Model",
      version: "1.0.0",
      type: "LLM",
      framework: "PyTorch",
      status: "ACTIVE",
      provider: "OpenAI",
      deployments: 3,
    },
    {
      id: 2,
      modelId: "MOD-002",
      name: "Claude-3",
      displayName: "Claude 3 Opus",
      version: "2.1.0",
      type: "LLM",
      framework: "TensorFlow",
      status: "ACTIVE",
      provider: "Anthropic",
      deployments: 2,
    },
    {
      id: 3,
      modelId: "MOD-003",
      name: "Embeddings-v1",
      displayName: "Text Embeddings Model",
      version: "1.5.0",
      type: "EMBEDDING",
      framework: "PyTorch",
      status: "PENDING",
      provider: "Internal",
      deployments: 1,
    },
    {
      id: 4,
      modelId: "MOD-004",
      name: "Classifier-v2",
      displayName: "Image Classifier",
      version: "2.0.0",
      type: "CLASSIFIER",
      framework: "TensorFlow",
      status: "ACTIVE",
      provider: "Internal",
      deployments: 0,
    },
  ]);

  const metrics = {
    total: data.length,
    active: data.filter((d) => d.status === "ACTIVE").length,
    pending: data.filter((d) => d.status === "PENDING").length,
    inactive: data.filter((d) => d.status === "INACTIVE").length,
  };

  const getStatusBadge = (status: string) => {
    const variants: Record<string, string> = {
      ACTIVE: "primary",
      INACTIVE: "secondary",
      PENDING: "outline",
      REJECTED: "danger",
    };
    return (
      <Badge variant={variants[status] as any}>
        {status}
      </Badge>
    );
  };

  const getTypeBadge = (type: string) => {
    const colors: Record<string, string> = {
      LLM: "bg-blue-500",
      EMBEDDING: "bg-purple-500",
      CLASSIFIER: "bg-green-500",
      REGRESSION: "bg-orange-500",
    };
    return (
      <Badge variant="outline" className={colors[type] || ""}>
        {type}
      </Badge>
    );
  };

  return (
    <div className="space-y-4 p-6">
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <Brain className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("serving.model.title", "Modelos de Serving")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("serving.model.subtitle", "Gestión de modelos disponibles para serving")}
          </p>
        </div>
        <Link href="/models/registry">
          <Button className="gap-2">
            <Plus className="w-4 h-4" />
            {t("serving.model.newModel", "Nuevo Modelo")}
          </Button>
        </Link>
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
            <p className="text-sm text-muted-foreground mb-2">Pendientes</p>
            <p className="text-3xl font-bold text-yellow-500">{metrics.pending}</p>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-6">
            <p className="text-sm text-muted-foreground mb-2">Inactivos</p>
            <p className="text-3xl font-bold text-gray-500">{metrics.inactive}</p>
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
                  placeholder={t("common.search", "Buscar modelos...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
            <Select value={typeFilter} onValueChange={setTypeFilter}>
              <SelectTrigger className="w-[200px]">
                <SelectValue placeholder="Filtrar por Tipo" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="ALL">TODOS</SelectItem>
                <SelectItem value="LLM">LLM</SelectItem>
                <SelectItem value="EMBEDDING">Embedding</SelectItem>
                <SelectItem value="CLASSIFIER">Classifier</SelectItem>
                <SelectItem value="REGRESSION">Regression</SelectItem>
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
                <SelectItem value="PENDING">PENDIENTE</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </CardContent>
      </Card>

      {/* Table */}
      <Card>
        <CardHeader>
          <CardTitle>
            {t("serving.model.list", "Listado de Modelos")}
          </CardTitle>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>ID</TableHead>
                <TableHead>Nombre</TableHead>
                <TableHead>Versión</TableHead>
                <TableHead>Tipo</TableHead>
                <TableHead>Framework</TableHead>
                <TableHead>Proveedor</TableHead>
                <TableHead>Deployments</TableHead>
                <TableHead>Estado</TableHead>
                <TableHead className="text-right">Acciones</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {data.map((item) => (
                <TableRow key={item.id}>
                  <TableCell className="font-mono text-sm">{item.modelId}</TableCell>
                  <TableCell>
                    <div>
                      <div className="font-semibold">{item.displayName}</div>
                      <div className="text-sm text-muted-foreground">{item.name}</div>
                    </div>
                  </TableCell>
                  <TableCell>
                    <Badge variant="outline">{item.version}</Badge>
                  </TableCell>
                  <TableCell>{getTypeBadge(item.type)}</TableCell>
                  <TableCell>{item.framework}</TableCell>
                  <TableCell>{item.provider}</TableCell>
                  <TableCell>
                    <div className="flex items-center gap-2">
                      <span className="font-semibold">{item.deployments}</span>
                      {item.deployments > 0 && (
                        <Link href={`/serving/model-deployment?modelId=${item.modelId}`}>
                          <Button variant="ghost" size="sm" className="h-6 w-6 p-0">
                            <ExternalLink className="w-3 h-3" />
                          </Button>
                        </Link>
                      )}
                    </div>
                  </TableCell>
                  <TableCell>{getStatusBadge(item.status)}</TableCell>
                  <TableCell className="text-right">
                    <div className="flex justify-end gap-2">
                      <Link href={`/models/registry/${item.modelId}`}>
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


